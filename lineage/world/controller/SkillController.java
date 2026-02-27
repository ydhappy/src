package lineage.world.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jsn_soft.SpeedHack;
import lineage.bean.database.Poly;
import lineage.bean.database.Skill;
import lineage.bean.lineage.Buff;
import lineage.bean.lineage.BuffInterface;
import lineage.bean.lineage.Kingdom;
import lineage.database.PolyDatabase;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectLock;
import lineage.network.packet.server.S_SkillDelete;
import lineage.network.packet.server.S_SkillList;
import lineage.network.packet.server.S_SkillListWarrior;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.Lineage_Balance;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.BoardInstance;
import lineage.world.object.instance.GuardInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MagicDollInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.PetInstance;
import lineage.world.object.instance.RobotInstance;
import lineage.world.object.instance.SummonInstance;
import lineage.world.object.item.armor.AntharasArmor;
import lineage.world.object.magic.*;
import lineage.world.object.magic.item.Criminal;
import lineage.world.object.magic.sp.ExpPotion;
import lineage.world.object.npc.background.Cracker;
import lineage.world.object.npc.background.CrackerDmg;
import lineage.world.object.npc.background.PigeonGroup;
import lineage.world.object.npc.background.Racer;
import lineage.world.object.npc.event.FireOfSoul;
import lineage.world.object.npc.kingdom.KingdomCastleTop;
import lineage.world.object.npc.kingdom.KingdomDoor;
import lineage.world.object.robot.PcRobotInstance;

public final class SkillController {

	// 패킷 전송에 사용됨.
	static private int lv[];
	// 지속형 마법 목록
	static private Map<Character, List<Skill>> list;
	// 마법투구를 통해 익힌 마법
	static private Map<Character, List<Skill>> temp_list;

	// int에 따른 mp패널티 적용값 변수. 해당 값만큼 mp소모를 감소.
	static int intMP[][] = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 0, 1, 2, 2, 2, 2, 2, 2, 2, 2 }, { 0, 1, 2, 3, 3, 3, 3, 3, 3, 3 }, { 0, 1, 2, 3, 4, 4, 4, 4, 4, 4 },
			{ 0, 1, 2, 3, 4, 5, 5, 5, 5, 5 }, { 0, 1, 2, 3, 4, 5, 6, 6, 6, 6 }, { 0, 1, 2, 3, 4, 5, 6, 7, 7, 7 } };

	static public void init() {
		TimeLine.start("SkillController..");

		temp_list = new HashMap<Character, List<Skill>>();
		list = new HashMap<Character, List<Skill>>();
		lv = new int[100];

		TimeLine.end();
	}

	/**
	 * 스킬 관리가 필요할때 호출해서 등록해주는 함수. : 사용자는 월드 접속햇을때 : 몬스터는 객체 생성될때
	 * 
	 * @param cha
	 */
	static public void toWorldJoin(Character cha) {
		synchronized (list) {
			if (list.get(cha) == null)
				list.put(cha, new ArrayList<Skill>());
		}
		synchronized (temp_list) {
			if (temp_list.get(cha) == null)
				temp_list.put(cha, new ArrayList<Skill>());
		}
	}

	/**
	 * 스킬 관리목록에서 제거처리하는 함수.
	 * 
	 * @param cha
	 */
	static public void toWorldOut(Character cha) {
		List<Skill> skill_list = find(cha);
		if (skill_list != null)
			skill_list.clear();
		skill_list = null;
		synchronized (list) {
			list.remove(cha);
		}
		List<Skill> skill_temp_list = findTemp(cha);
		if (skill_temp_list != null)
			skill_temp_list.clear();
		skill_temp_list = null;
		synchronized (temp_list) {
			temp_list.remove(cha);
		}
	}

	/**
	 * 마법 딜레이 확인하는 함수. : 딜레이 확인한후 마법 시전해도 되는지 리턴함.
	 * 
	 * @param cha
	 * @param skill
	 * @return
	 */
	static public boolean isDelay(Character cha, Skill skill) {
		// 딜레이 확인하여 보낸다
		long time = System.currentTimeMillis();

		if(Lineage.delay_semi_bug){
			if (cha instanceof PcInstance) {
				long a = time - cha.delay_magic;
				cha.delay_magic = time;
				
				if (a < Lineage.delay_semi_bug_check_time) 
					return false;
			}
		} 
		
		if ((cha.delay_magic == 0 || cha.delay_magic <= time)) {
			cha.delay_magic = time + skill.getDelay();
			
			if (cha.isViewDelay()) {
				cha.magic_time = time;
				cha.setViewDelaySkillName(skill.getName());
			}
			return true;
		}
		// 실패.
		ChattingController.toChatting(cha, "마법 재사용시간이 남았습니다.", Lineage.CHATTING_MODE_MESSAGE);
		cha.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x09));
		return false;
	}
	/**
	 * 스킬 사용 요청 처리 함수. : 무기에서 마법이 발동될때 처리하는 함수. : 리턴형을 이용해 이곳에서 실패할경우 skill클레스에 의존하여
	 * 데미지 산출함. : ItemWeaponInstance.toDamage(Character cha, object o) : SpellPotion
	 * 아이템쪽에서도 호출해서 사용함. : SpellPotion.toClick(Character cha, ClientBasePacket cbp)
	 * 
	 * @param cha
	 * @param o
	 * @param skill
	 * @return
	 */
	static public boolean toSkill(Character cha, object o, Skill skill, boolean me, int duration, String option) {
		if (skill == null)
			return false;

		//
		Object temp = PluginController.init(SkillController.class, "toSkill.item", cha, o, skill, me, duration, option);
		if (temp != null)
			return (Boolean) temp;
		
		// 자동사냥 방지 확인.
		if (Lineage.is_auto_hunt_check_skill && cha instanceof PcInstance && !AutoHuntCheckController.checkCount((PcInstance) cha))
			return true;
//		if (cha instanceof PcInstance) {
//			if (!SpeedHack.isMagicTime((PcInstance) cha, SpeedHack.getSkillMotion(skill)))
//				return false;
//		}
		//
		switch ((int) skill.getUid()) {
		case 1:
		case 19:
		case 35:
		case 57:
			if (me)
				Heal.onBuff(cha, cha, skill, skill.getCastGfx(), 0);
			else
				Heal.onBuff(cha, o, skill, skill.getCastGfx(), 0);
			return true;
		case 10:
		case 28:
			ChillTouch.toBuff(cha, skill, o, 0, 0, 0);
			return true;
		case 11:
			if (me)
				CursePoison.onBuff(cha, cha, skill);
			else
				CursePoison.onBuff(cha, o, skill);
			return true;
		case 20:
		case 40:
			if (me)
				CurseBlind.onBuff(cha, cha, skill);
			else
				CurseBlind.onBuff(cha, o, skill);
			return true;
		case 27:
			if (me)
				WeaponBreak.onBuff(cha, cha, skill.getCastGfx());
			else
				WeaponBreak.onBuff(cha, o, skill.getCastGfx());
			return true;
		case 29:
			Slow.onBuff(me ? cha : o, skill);
			return true;
		case 33:
			int counter = 0;
			try {
				if (option != null && option.length() > 0)
					counter = Integer.valueOf(option);
			} catch (Exception e) {
				counter = 0;
			}
			if (me)
				CurseParalyze.onBuff(cha, cha, skill, counter);
			else
				CurseParalyze.onBuff(cha, o, skill, counter);
			return true;
		case 37:
			RemoveCurse.onBuff(me ? cha : o, skill);
			return true;
		case 39:
			ManaDrain.onBuff(cha, o, skill);
			return true;
		case 43:
		case 54:
			Haste.onBuff(me ? cha : o, skill);
			return true;
		case 44:
			Cancellation.onBuff(me ? cha : o, skill.getCastGfx());
			return true;
		case 48:
			BlessWeapon.onBuff(me ? cha : o, skill);
			return true;
		case 49:
			HealAll.onBuff(me ? cha : o, skill);
			return true;
		case 50:
			IceLance.init(me ? cha : o, duration);
			return true;
		case 56:
			Disease.onBuff(me ? cha : o, skill, duration);
			return true;
		case 64:
			Silence.onBuff(o, skill, duration);
			return true;
		case 68:
			ImmuneToHarm.onBuff(me ? cha : o, skill, duration);
			return true;
		case 78:
			AbsoluteBarrier.onBuff(me ? cha : o, skill, duration);
			return true;
		case 210:
		case 211:
		case 212:
		case 213:
		case 214:
		case 215:
			ExpPotion.onBuff(me ? cha : o, skill);
			return true;
		}
		return false;
	}

	/**
	 * 스킬 사용 요청 처리 함수.
	 * 
	 * @param cha
	 * @param level
	 * @param number
	 */
	static public void toSkill(Character cha, final ClientBasePacket cbp) {
		int level = cbp.readC() + 1;
		int number = cbp.readC();
		// 플러그인 확인.
		Object p = PluginController.init(SkillController.class, "toSkill", cha, cbp, level, number);
		if (p != null)
			return;

		// 디텍션락인 상태일경우 무시.
		// 텔레포트락인 상태일경우 무시.
	//	if (cha.isBuffDetection() || cha.isBuffTeleport()) {
	//		Teleport.unLock(cha, false);
	//		return;
	//	}
		
		long time = System.currentTimeMillis();
		if(cha.get투망시간() > time) {
			ChattingController.toChatting(cha, "투망딜레이:2초", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}	

		Skill skill = find(cha, level, number);
		if (skill != null && isDelay(cha, skill)) {
//			if (cha instanceof PcInstance) {
//				if (!SpeedHack.isMagicTime((PcInstance) cha, SpeedHack.getSkillMotion(skill)))
//					return;
//			}
			switch (skill.getSkillLevel()) {
			case 1:
				switch (skill.getSkillNumber()) {
				case 0: // 힐
					Heal.init(cha, skill, cbp.readD());
					break;
				case 1:	// 라이트
					Light.init(cha, skill);
					break;
				case 2: // 실드
					Shield.init(cha, skill);
					break;
				case 3: // 에너지볼트
					EnergyBolt.init(cha, skill, cbp.readD());
					break;
				case 4: // 텔레포트
					Teleport.init(cha, skill, cbp);
					break;
				case 5: // 아이스 대거
					EnergyBolt.init(cha, skill, cbp.readD());
					break;
				case 6: // 윈드 커터
					EnergyBolt.init(cha, skill, cbp.readD());
					break;
				case 7:	// 홀리 웨폰
					HolyWeapon.init(cha, skill, cbp.readD());
					break;
				}
				break;
			case 2:
				switch (skill.getSkillNumber()) {
				case 0:
					CurePoison.init(cha, skill, cbp.readD());
					break;
				case 1:
					ChillTouch.init(cha, skill, cbp.readD());
					break;
				case 2:
					CursePoison.init(cha, skill, cbp.readD());
					break;
				case 3:
					EnchantWeapon.init(cha, skill, cbp.readD());
					break;
				case 4:
					Detection.init(cha, skill);
					break;
				case 5:
					DecreaseWeight.init(cha, skill);
					break;
				case 6:
					EnergyBolt.init(cha, skill, cbp.readD());
					break;
				case 7:
					EnergyBolt.init(cha, skill, cbp.readD());
					break;
				}
				break;
			case 3:
				switch (skill.getSkillNumber()) {
				case 0:
					Lightning.init(cha, skill, cbp.readD(), cbp.readH(), cbp.readH());
					break;
				case 1:
					TurnUndead.init(cha, skill, cbp.readD(), cbp.readH(), cbp.readH());
					break;
				case 2:
					Heal.init(cha, skill, cbp.readD());
					break;
				case 3:
					CurseBlind.init(cha, skill, cbp.readD());
					break;
				case 4:
					BlessedArmor.init(cha, skill, cbp.readD());
					break;
				case 5:
					Lightning.init(cha, skill, cbp.readD(), cbp.readH(), cbp.readH());
					break;
				case 6:
				case 7:
					RevealWeakness.init(cha, skill, cbp.readD());
					break;
				}
				break;
			case 4:
				switch (skill.getSkillNumber()) {
				case 0:
					Lightning.init(cha, skill, cbp.readD(), cbp.readH(), cbp.readH());
					break;
				case 1:
					EnchantDexterity.init(cha, skill, cbp.readD());
					break;
				case 2:
					WeaponBreak.init(cha, skill, cbp.readD());
					break;
				case 3:
					ChillTouch.init(cha, skill, cbp.readD());
					break;
				case 4:
					Slow.init(cha, skill, cbp.readD());
					break;
				case 5:
					Lightning.init(cha, skill, cbp.readD(), cbp.readH(), cbp.readH());
					break;		
				case 6:
					CounterMagic.init(cha, skill);
					break;
				case 7:
					Meditation.init(cha, skill);
					break;
				}
				break;
			case 5:
				switch (skill.getSkillNumber()) {
				case 0:
					CurseParalyze.init(cha, skill, cbp.readD());
					break;
				case 1:
					EnergyBolt.init(cha, skill, cbp.readD());
					break;
				case 2:
					Heal.init(cha, skill, cbp.readD());
					break;
				case 3:
					TameMonster.init(cha, skill, cbp.readD());
					break;
				case 4:
					RemoveCurse.init(cha, skill, cbp.readD());
					break;
				case 5:
					ConeOfCold.init(cha, skill, cbp.readD());
					break;
				case 6:
					ManaDrain.init(cha, skill, cbp.readD());
					break;
				case 7:
					CurseBlind.init(cha, skill, cbp.readD());
					break;
				}
				break;
			case 6:
				switch (skill.getSkillNumber()) {
				case 0:
					CreateZombie.init(cha, skill, cbp.readD());
					break;
				case 1:
					EnchantMighty.init(cha, skill, cbp.readD());
					break;
				case 2:
					Haste.init(cha, skill, cbp.readD());
					break;
				case 3:
					Cancellation.init(cha, skill, cbp.readD());
					break;
				case 4:
					Eruption.init(cha, skill, cbp.readD());
					break;
				case 5:
					Sunburst.init(cha, skill, cbp.readD());
					break;
				case 6:
					Weakness.init(cha, skill, cbp.readD());
					break;
				case 7:
					BlessWeapon.init(cha, skill, cbp.readD());
					break;
				}
				break;
			case 7:
				switch (skill.getSkillNumber()) {
				case 0:
					HealAll.init(cha, skill);
					break;
				case 1:
					IceLance.init(cha, skill, cbp.readD());
					break;
				case 2:
					SummonMonster.init(cha, skill, cbp.isRead(2) ? cbp.readH() : 0);
					//SummonMonster.init(cha, skill, cbp.readD());
					break;
				case 3:
					HolyWalk.init(cha, skill);
					break;
				case 4:
					Tornado.init(cha, skill);
					break;
				case 5:
					GreaterHaste.init(cha, skill, cha.getObjectId());
					break;
				case 6:
					ReductionArmor.init(cha, skill);
					break;
				case 7:
					Disease.init(cha, skill, cbp.readD());
					break;
				}
				break;
			case 8:
				switch (skill.getSkillNumber()) {
				case 0:
					FullHeal.init(cha, skill, cbp.readD());
					break;
				case 1:
					Firewall.init(cha, skill, cbp.readH(), cbp.readH());
					break;
				case 2:
					Blizzard.init(cha, skill);
					break;
				case 3:
					InvisiBility.init(cha, skill);
					break;
				case 4:
					Resurrection.init(cha, skill, cbp.readD());
					break;
				case 5:
					Tornado.init(cha, skill);
					break;
				case 6:
					LifeStream.init(cha, skill);
					break;
				case 7:
					Silence.init(cha, skill, cbp.readD());
					break;
				}
				break;
			case 9:
				switch (skill.getSkillNumber()) {
				case 0:
					// 자신에게 시전되는 마법
					Meditation.init(cha, skill);
					break;
				case 1:
					if (Lineage.server_version >= 250)
						FogOfSleeping.init(cha, skill, cbp.readD());
					else
						FogOfSleeping.init(cha, skill, cbp.readH(), cbp.readH());
					break;
				case 2:
					AdvanceSpirit.init(cha, skill, cbp.readD());
					break;
				case 3:
					ImmuneToHarm.init(cha, skill, cbp.readD());
					break;
				case 4:
					MassTeleport.init(cha, skill, cbp);
					break;
				case 5:
					Tornado.init(cha, skill);
					break;
				case 6:
					DecayPotion.init(cha, skill, cbp.readD());
					break;
				case 7:
					SolidCarriage.init(cha, skill);
					break;
				}
				break;
			case 10:
				switch (skill.getSkillNumber()) {
				case 0:
					CreateMagicalWeapon.init(cha, skill, cbp.readD());
					break;
				case 1:
					if (Lineage.server_version > 144)
						Lightning.init(cha, skill, cbp.readD(), cbp.readH(), cbp.readH());
					else
						Lightning.init(cha, skill, cbp.readD(), cha.getX(), cha.getY());
					break;
				case 2:
					FreezingBlizzard.init(cha, skill);
					break;
				case 3:
					//앱솔
					AbsoluteBarrier.init(cha, skill);
					//MassSlow.init(cha, skill, cbp.readD());
					break;
				case 4:
					EnergyBolt.init(cha, skill, cbp.readD());
					break;
				case 5:
					AbsoluteBarrier.init(cha, skill);
					break;
				case 6:
					Meditation.init(cha, skill, cbp.readD());
					break;
				case 7: 
					//어밴
					//AdvanceSpirit.init(cha, skill, cbp.readD());
					ShapeChange.init(cha, skill, cbp.readD());
					break;
				}
				break;
			case 11:
				switch (skill.getSkillNumber()) {
				case 6: // 쇼크스턴
					ShockStun.init(cha, skill, cbp.readD(), cbp.readH(), cbp.readH());
					break;
				case 7: // 리덕션아머
					ReductionArmor.init(cha, skill);
					break;
				}
				break;
			case 12:
				switch (skill.getSkillNumber()) {
				case 0: // 바운스 어택
					BounceAttack.init(cha, skill);
					break;
				case 1: // 솔리드 캐리지
					SolidCarriage.init(cha, skill);
					break;
				case 2: // 카운터 배리어
					CounterBarrier.init(cha, skill);
					break;
				}
				break;
			case 13:
				switch (skill.getSkillNumber()) {
				case 0:
					InvisiBility.init(cha, skill);
					break;
				case 1:
					EnchantVenom.init(cha, skill);
					break;
				case 2:
					ShadowArmor.init(cha, skill);
					break;
				case 3:
					PurifyStone.init(cha, skill, cbp.readD());
					break;
				case 4:
					HolyWalk.init(cha, skill);
					break;
				case 5:
					BurningSpirit.init(cha, skill);
					break;
				case 6:
					CurseBlind.init(cha, skill, cbp.readD());
					break;
				case 7:
					VenomResist.init(cha, skill);
					break;
				}
				break;
			case 14:
				switch (skill.getSkillNumber()) {
				case 0:
					DoubleBreak.init(cha, skill);
					break;
				case 1:
					UncannyDodge.init(cha, skill);
					break;
				case 2:
					ShadowFang.init(cha, skill, cbp.readD());
					break;
				case 3:
					FinalBurn.init(cha, skill, cbp.readD());
					break;
				case 4:
					DressMighty.init(cha, skill);
					break;
				case 5:
					DressDexterity.init(cha, skill);
					break;
				case 6:
					DressEvasion.init(cha, skill);
					break;
				}
				break;
			case 15:
				switch (skill.getSkillNumber()) {
				case 0:
					TrueTarget.init(cha, skill, cbp.readD(), cbp.readH(), cbp.readH(), cbp.readS());
					break;
				case 1:
					GlowingAura.init(cha, skill);
					break;
				case 2:
					ShiningAura.init(cha, skill);
					break;
				case 3:
					CallPledgeMember.init(cha, skill, cbp.readS());
					break;
				case 4:
					BraveAura.init(cha, skill);
					break;
				case 5:
					TeleportPledgeMember.init(cha, skill, cbp.readS());
					break;
				case 6:
					break;
				case 7:
					break;
				}
				break;
			case 17:
				switch (skill.getSkillNumber()) {
				case 0:
					ResistMagic.init(cha, skill);
					break;
				case 1:
					BodyToMind.init(cha, skill);
					break;
				case 2:
					TeleportToMother.init(cha, skill);
					break;
				case 3:
					TripleArrow.init(cha, skill, cbp.readD(), cbp.readH(), cbp.readH());
					// (cha, skill, cbp.readD());
					break;
				case 4:
					ElementalFalldown.init(cha, skill, cbp.readD());
					break;
				case 5:
					CounterMirror.init(cha, skill);
					break;
				}
				break;
			case 18:
				switch (skill.getSkillNumber()) {
				case 0:
					ClearMind.init(cha, skill);
					break;
				case 1:
					ResistElemental.init(cha, skill);
					break;
				}
				break;
			case 19:
				switch (skill.getSkillNumber()) {
				case 0:
					ReturnToNature.init(cha, skill, cbp.readD());
					// TripleArrow.init(cha, skill, cbp.readD());
					break;
				case 1:
					BodyToMind.init(cha, skill);
					break;
				case 2:
					ProtectionFromElemental.init(cha, skill);
					break;
				case 3:
					FireWeapon.init(cha, skill, cbp.readD());
					break;
				case 4:
					WindShot.init(cha, skill, cbp.readD());
					break;
				case 5:
					WindWalk.init(cha, skill);
					break;
				case 6:
					EarthSkin.init(cha, skill, cbp.readD());
					break;
				case 7:
					Slow.init(cha, skill, cbp.readD());
					// TripleArrow.init(cha, skill, cbp.readD()/*, cbp.readH(), cbp.readH()*/);
					break;
				}
				break;
			case 20:
				switch (skill.getSkillNumber()) {
				case 0:
					EraseMagic.init(cha, skill, cbp.readD());
					break;
				case 1:
					SummonLesserElemental.init(cha, skill);
					break;
				case 2:
					BlessOfFire.init(cha, skill);
					break;
				case 3:
					EyeOfStorm.init(cha, skill);
					break;
				case 4:
					EarthBind.init(cha, skill, cbp.readD());
					break;
				case 5:
					NaturesTouch.init(cha, skill, cbp.readD());
					break;
				case 6:
					BlessOfEarth.init(cha, skill);
					break;
				case 7:
					AquaProtect.init(cha, skill, cbp.readD());
					break;
				}
				break;
			case 21:
				switch (skill.getSkillNumber()) {
				case 0:
					AreaOfSilence.init(cha, skill);
					break;
				case 1:
					SummonGreaterElemental.init(cha, skill);
					break;
				case 2:
					BurningWeapon.init(cha, skill);
					//BurningWeapon.init(cha, skill, Lineage.server_version >= 270 ? cha.getObjectId() : cbp.readD());
					break;
				case 3:
					NaturesBlessing.init(cha, skill);
					break;
				case 4:
					NatureMiracle.init(cha, skill, cbp.readD());
					break;
				case 5:
					StormShot.init(cha, skill, Lineage.server_version >= 270 ? cha.getObjectId() : cbp.readD());
					break;
				case 6:
					if (Lineage.skill_StormWalk)
						Slow.init(cha, skill, cbp.readD());
					else
						// TripleArrow.init(cha, skill, cbp.readD(), cbp.readH(), cbp.readH());
						break;
				case 7:
					IronSkin.init(cha, skill, Lineage.server_version >= 270 ? cha.getObjectId() : cbp.readD());
					break;
				}
				break;
			case 22:
				switch (skill.getSkillNumber()) {
				case 0:
					ExoticVitalize.init(cha, skill);
					break;
				case 1:
					WaterLife.init(cha, skill, cbp.readD());
					break;
				case 2:
					ElementalFire.init(cha, skill);
					break;
				case 4:
					PolluteWater.init(cha, skill, cbp.readD());
					break;
				case 5:
					StrikerGale.init(cha, skill, cbp.readD());
					break;
				case 6:
					SoulOfFlame.init(cha, skill);
					break;
				case 7:
					AdditionalFire.init(cha, skill);
					break;
				}
				break;
			case 23:
				switch (skill.getSkillNumber()) {
				case 4:
					DragonSkin.init(cha, skill);
					break;
				case 5:
					BurningSlash.init(cha, skill);
					break;
				case 6:
					GuardBreak.init(cha, skill, cbp.readD());
					break;
				case 7:
					// MagmaBreath
					MagmaBreath.init(cha, skill, cbp.readD());
					break;
				}
				break;
			case 24:
				switch (skill.getSkillNumber()) {
				case 0:
					ScalesEarthDragon.init(cha, skill);
					break;
				case 1:
					BloodLust.init(cha, skill);
					break;
				case 2:
					FouSlayer.init(cha, skill, cbp.readD());
					break;
				}
				break;
			case 29:
				switch (skill.getSkillNumber()) {
				case 0: // 하울
					Howl.init(cha, skill);
					break;
				case 1: // 기간틱
					Gigantic.init(cha, skill);
					break;
				case 3: // 파워그립
					PowerGrip.init(cha, skill, cbp.readD(), cbp.readH(), cbp.readH());
					break;
				case 4: // 토마호크
					Tomahawk.init(cha, skill, cbp.readD(), cbp.readH(), cbp.readH());
					break;
				case 5: // 데스페라도
					Desperado.init(cha, skill, cbp.readD(), cbp.readH(), cbp.readH());
					break;
				}
				break;
			/*
			 * case 0: switch(skill.getSkillNumber()){ case 0: break; case 1: break; case 2:
			 * break; case 3: break; case 4: break; case 5: break; case 6: break; case 7:
			 * break; } break;
			 */
			}
		}
	}

	/**
	 * npc로부터 배울수 있는 스킬 갯수 리턴하는 메서드.
	 */
	static public int getBuySkillCount(Character cha) {
		int count = 0;
		int idx = getBuySkillIdx(cha);
		// 최대 배울수 잇는 범위내에 가지고잇는 스킬 갯수 추출
		for (Skill s : find(cha)) {
			if (s.getUid() <= idx)
				count += 1;
		}
		// 최대배울수잇는갯수 - 가지고잇는 갯수 = 남은갯수를 리턴함.
		return idx - count;
	}

	/**
	 * 클레스별로 레벨에 맞춰서 최대 배울수 잇는 갯수 추출.
	 */
	static public int getBuySkillIdx(Character cha) {
		int level = cha.getLevel();
		int gab = 0;
		switch (cha.getClassType()) {
		case Lineage.LINEAGE_CLASS_ROYAL:
			if (cha.getLevel() > 20)
				level = 20;
			gab = 10;
			break;
		case Lineage.LINEAGE_CLASS_KNIGHT:
			if (cha.getLevel() > 50)
				level = 50;
			gab = 50;
			break;
		case Lineage.LINEAGE_CLASS_ELF:
			if (cha.getLevel() > 24)
				level = 24;
			gab = 8;
			break;
		case Lineage.LINEAGE_CLASS_WIZARD:
			if (cha.getLevel() > 12)
				level = 12;
			gab = 4;
			break;
		case Lineage.LINEAGE_CLASS_DARKELF:
			if (cha.getLevel() > 24)
				level = 24;
			gab = 12;
			break;
		}
		return ((level / gab) * 7);
	}

	/**
	 * 스킬 제거 처리 함수.
	 * 
	 * @param cha
	 * @param uid
	 * @param temp
	 */
	static public void remove(Character cha, int uid, boolean temp) {
		remove(cha, find(cha, uid, temp), temp);
	}

	/**
	 * 스킬 제거 처리 함수.
	 * 
	 * @param cha
	 * @param level
	 * @param number
	 */
	static public void remove(Character cha, int level, int number) {
		remove(cha, find(cha, level, number, false), false);
	}

	/**
	 * 스킬 제거 처리 함수. : 중복 코드 방지용.
	 * 
	 * @param cha
	 * @param s
	 * @param temp
	 */
	static public void remove(Character cha, Skill s, boolean temp) {
		if (s == null)
			return;
		if (temp)
			findTemp(cha).remove(s);
		else
			find(cha).remove(s);
		// 메모리 초기화
		for (int i = lv.length - 1; i >= 0; --i)
			lv[i] = 0;
		// 가지고있는 스킬이라면 갱신 안하기.
		if (find(cha, s.getUid(), false) == null)
			lv[s.getSkillLevel() - 1] += s.getId();
		// 패킷 전송.
		cha.toSender(S_SkillDelete.clone(BasePacketPooling.getPool(S_SkillDelete.class), lv));
	}

	/**
	 * 스킬 추가 처리 함수.
	 * 
	 * @param cha
	 * @param uid
	 * @param temp
	 */
	static public void append(Character cha, int uid, boolean temp) {
		Skill s = find(cha, uid, temp);
		if (s == null) {
			s = SkillDatabase.find(uid);
			if (s != null) {
				if (temp)
					findTemp(cha).add(s);
				else
					find(cha).add(s);
			}
		}
	}

	/**
	 * 스킬 추가 처리 함수.
	 * 
	 * @param cha
	 * @param level
	 * @param number
	 * @param temp
	 */
	static public void append(Character cha, int level, int number, boolean temp) {
		Skill s = find(cha, level, number, temp);
		if (s == null) {
			s = SkillDatabase.find(level, number);
			if (s != null) {
				if (temp)
					findTemp(cha).add(s);
				else
					find(cha).add(s);
			}
		}
	}

	/**
	 * 연결된 객체 찾아서 리턴.
	 * 
	 * @param pc
	 * @return
	 */
	static public List<Skill> find(Character cha) {
		synchronized (list) {
			return list.get(cha);
		}
	}

	/**
	 * 연결된 객체 찾아서 리턴.
	 * 
	 * @param pc
	 * @return
	 */
	static public List<Skill> findTemp(Character cha) {
		synchronized (temp_list) {
			return temp_list.get(cha);
		}
	}
	
	/**
	 * 같은 스킬이 존재하는지 확인하는 메서드.
	 */
	static public Skill find(Character cha, long uid) {
		for (Skill s : findTemp(cha)) {
			if (s != null && uid == s.getUid())
				return s;
		}

		for (Skill s : find(cha)) {
			if (s != null && uid == s.getUid())
				return s;
		}
		
		return null;
	}

	/**
	 * 같은 스킬이 존재하는지 확인하는 메서드.
	 */
	static public Skill find(Character cha, long uid, boolean temp) {
		if (temp) {
			for (Skill s : findTemp(cha)) {
				if (uid == s.getUid())
					return s;
			}
		} else {
			for (Skill s : find(cha)) {
				if (uid == s.getUid())
					return s;
			}
		}
		return null;
	}

	/**
	 * 같은 스킬이 존재하는지 확인하는 메서드.
	 */
	static public Skill find(Character cha, int level, int number) {
		for (Skill s : find(cha)) {
			if (s.getSkillLevel() == level && s.getSkillNumber() == number)
				return s;
		}
		for (Skill s : findTemp(cha)) {
			if (s.getSkillLevel() == level && s.getSkillNumber() == number)
				return s;
		}
		return null;
	}

	/**
	 * 일치하는 마법 리턴함.
	 */
	static public Skill find(Character cha, int level, int number, boolean temp) {
		if (temp) {
			for (Skill s : findTemp(cha)) {
				if (s.getSkillLevel() == level && s.getSkillNumber() == number)
					return s;
			}
		} else {
			for (Skill s : find(cha)) {
				if (s.getSkillLevel() == level && s.getSkillNumber() == number)
					return s;
			}
		}
		return null;
	}

	/**
	 * 습득한 스킬정보 전송하는 함수.
	 */
	static public void sendList(Character cha) {
		// 메모리 초기화
		for (int i = lv.length - 1; i >= 0; --i)
			lv[i] = 0;
		// 실제 습득을 통해 익힌 마법목록
		for (Skill s : find(cha))
			lv[s.getSkillLevel() - 1] += s.getId();
		// 마법투구를 통해 습득한 임시 마법. 실제습득한 마법에 없을경우 그값을 갱신.
		for (Skill s : findTemp(cha)) {
			if (find(cha, s.getUid(), false) == null)
				lv[s.getSkillLevel() - 1] += s.getId();
		}
		// 패킷 전송.
		cha.toSender(S_SkillList.clone(BasePacketPooling.getPool(S_SkillList.class), lv));
	}

	/**
	 * 마법을 시전하기전에 hp, mp, item 및 상태에 따라 마법시전해도 되는지 확인해보는 메서드. : skill 이 null 로 올수도
	 * 잇기 때문에 gamso 값이 true일때만 사용하기.
	 */
	static public boolean isMagic(Character cha, Skill skill, boolean gamso, Object... opt) {
		if (cha.isDead() || cha.isWorldDelete())
			return false;
		
//		if (cha.getMap() == 70 && 
//				((skill.getSkillLevel() == 7 && skill.getSkillNumber() == 1) 
//						|| (skill.getSkillLevel() == 9 && skill.getSkillNumber() == 1))) {
//			return false;
//			
//		}
//		
		// 운영자는 무조건 성공.
		if (cha.getGm() > 0)
			return true;
		
		// 앱솔상태 해제.
		if (cha.isBuffAbsoluteBarrier())
			BuffController.remove(cha, AbsoluteBarrier.class);
		
		// 사일런스 상태라면 마법을 사용 할 수 없도록 차단 [마법주문서로 할경우엔 무시.]
		if (opt.length == 0 && cha.isBuffSilence() && !isKnight(skill))
			return false;
		// 무게 확인
		if (cha instanceof PcInstance && cha.getInventory() != null
				&& cha.getInventory().isWeightPercent(82) == false) {
			// \f1짐이 너무 무거워 마법을 사용할 수 없습니다.
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 316));
			return false;
		}
		// 변신상태일경우 마법 시전가능한지 확인.
		if (cha.getGfx() != cha.getClassGfx()) {
			Poly p = PolyDatabase.getPolyGfx(cha.getGfx());
			if (p != null && !p.isSkill()) {
				// \f1그 상태로는 마법을 사용할 수 없습니다.
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 285));
				return false;
			}
		}
		// 공성전중일때 공성존내에 있을경우 해당 마법을 시전해도 되는지 확인.
		if (SkillController.isKingdomZoneMagic(skill, cha) == false) {
//			// \f1여기에서는 사용할 수 없습니다.
//			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 563));
			ChattingController.toChatting(cha, "공성전 중에는 사용하실수 없습니다.", 20);
			return false;
		}
		// 데스페라도상태일경우 텔레포트 무시.
		if (cha.isBuffDesperado()) {
			// 텔레포트
			if (skill.getSkillLevel() == 1 && skill.getSkillNumber() == 4)
				return false;
			// 매스 텔레포트
			if (skill.getSkillLevel() == 9 && skill.getSkillNumber() == 4)
				return false;
			// 런클렌
			if (skill.getSkillLevel() == 15 && skill.getSkillNumber() == 5)
				return false;
			// 텔레포트투마더
			if (skill.getSkillLevel() == 17 && skill.getSkillNumber() == 2)
				return false;
		}
		// 투명상태일경우 누락처리
/*		if (cha.isInvis()) {
			// 공격마법은 무시.
			if (isAttackMagic(skill) || isZoneCheckMagic(skill)) {
				// \f1그 상태로는 마법을 사용할 수 없습니다.
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 285));
				return false;
			}
		} */
		// 감소 처리를 할경우.
		if (gamso) {
			if (skill == null)
				return false;

			// 처리.
			int hp = skill.getHpConsume();
			int mp = skill.getMpConsume();
			int lawful = cha instanceof MonsterInstance ? 0 : skill.getLawfulConsume();
//			int intmpNum = cha.getTotalInt() - 12;
//			int skilllev = skill.getSkillLevel() - 1;
			ItemInstance item = null;
			// int값에따른 mp소모량 감소
//			if (intmpNum > 7)
//				intmpNum = 7;
//			else if (intmpNum <= 0)
//				intmpNum = 0;
//			if (skilllev > 9)
//				skilllev = 0;
//			else if (skilllev <= 0)
//				skilllev = 0;
//			mp -= intMP[intmpNum][skilllev];
			if (cha.getClassType() == 0x00 && (skill.getUid() == 42 || skill.getUid() == 26 || skill.getUid() == 43)) {
				mp = (int) (mp * 0.5);
				hp = (int) (hp * 0.5);
			}
			// 기사 패널티 적용.
			if (cha.getClassType() == 0x01) {
				mp = (int) (mp * 0.5);
				hp = (int) (hp * 0.5);
			}
			// hp확인
			if (hp > 0 && cha.getNowHp() <= hp) {
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 279));
				return false;
			}
			// mp확인
			if (mp > 0 && cha.getNowMp() < mp) {
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 278));
				return false;
			}
			// 사용자들만 재료 확인.
			if (skill.getItemConsume() > 0 && cha instanceof PcInstance && !(cha instanceof RobotInstance)) {
				item = cha.getInventory().findDbNameId(skill.getItemConsume());
				if (item == null || item.getCount() < skill.getItemConsumeCount()) {
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 299));
					return false;
				}
			}

			if (hp > 0)
				cha.setNowHp(cha.getNowHp() - hp);
			if (mp > 0)
				cha.setNowMp(cha.getNowMp() - mp);
			if (lawful > 0)
				cha.setLawful(cha.getLawful() - lawful);
			if (item != null)
				cha.getInventory().count(item, item.getCount() - skill.getItemConsumeCount(), true);
		}
		return true;
	}

	/**
	 * 해당 스킬에 필요한 hp, mp가 있는지 확인해주는 함수.
	 * 
	 * @param cha
	 * @param skill
	 * @return
	 */
	static public boolean isHpMpCheck(Character cha, Skill skill) {
		int hp = skill.getHpConsume();
		int mp = skill.getMpConsume();
		if (hp > 0 && cha.getNowHp() <= hp)
			return false;
		if (mp > 0 && cha.getNowMp() < mp)
			return false;

		return true;
	}

	/**
	 * 마법시전 성공여부 확인하는 함수.
	 * 
	 * @param cha
	 * @param o
	 * @param skill
	 * @return
	 */
	static public boolean isFigure(Character cha, object o, Skill skill, boolean is_rate, boolean is_clan) {
		PcInstance pc = (PcInstance) cha;
		// 투켠 몬스터 무조건 실패
		if (FightController.isFightMonster(o))
			return false;
		// 영자는 무조건 성공
		if (cha.getGm() > 0)
			return true;

		// 없거나 죽은상태 패스
		if (o == null || o.isDead() || o.getGm() > 0)
			return false;
		// 이레이즈 매직이 걸려있을때에 확률 마법시전시에는 무조건 삭제
		// if(o instanceof PcInstance){
		// if(BuffController.find(o).find(EraseMagic.class) != null) {
		// BuffController.remove(o, EraseMagic.class);
		// }
		// }
		//

		if (cha.getMap() == 300)
			return false;
		if (cha.getMap() == 613)
			return false;

		if (cha instanceof PcInstance && o instanceof PcInstance) {
			if (cha.getMap() == Lineage.SAFETY_ZONE || o.getMap() == Lineage.SAFETY_ZONE)
				return false;
		}
		/*
		 * if(cha.getMap()==0 && World.isNormalZone(o.getX(), o.getY(), o.getMap())){
		 * if(cha instanceof PcInstance && o instanceof PcInstance){ return false; } }
		 */

		if (o instanceof Racer || o instanceof BackgroundInstance || o instanceof KingdomCastleTop)
			return false;
		// 몬스터끼리는 모두 실패시키기.
		if (cha instanceof MonsterInstance && o instanceof MonsterInstance)
			return false;
		// 장애물이 막고있으면 무시.
		if (!Util.isAreaAttack(cha, o))
			return false;
		// 마법시전이 불가능한 몬스터는 무시.
		if (o instanceof MonsterInstance && !((MonsterInstance) o).getMonster().isBuff())
			return false;
		// 존 체크마법일경우라면 보라돌이로 처리.
		if (isZoneCheckMagic(skill) && cha instanceof PcInstance && o instanceof PcInstance) {
			// 보라도리로 변경하기.
			if (Lineage.server_version > 182)
				Criminal.init(cha, o);
		}

		if (cha instanceof PcInstance && o instanceof PcInstance) {
			if (cha.getMap() != 208 && o.getMap() != 208) {
				if ((!BuffSkill(skill) && !isHeal(skill) && cha.getClanId() == 32)
						|| (!BuffSkill(skill) && !isHeal(skill) && o.getClanName() == Lineage.new_clan_name)) {
					ChattingController.toChatting(cha, "[서버알림] 중립혈맹 PK불가.", 20);
					return false;
				}
			}
			if (cha.getMap() == 300)
				return false;
		}

		// 같은 혈맹원끼리 시전하는거라면 무조건 성공.
		if (is_clan)
			return true;
		// 공격존체크해야되는 마법시 공격가능존 체크.
		if (isZoneCheckMagic(skill) && !World.isAttack(cha, o))
			return false;
		// 앱솔 무시.
		if (o.isBuffAbsoluteBarrier() && isAbsoluteBarrier(skill))
			return false;
		// 캔슬레이션을 제외한 모든확률마법은 석화상태 패스하기.
		if (skill.getUid() != 44 && o.isLockHigh())
			return false;
		// 독계열 마법일경우 저항력 체크.
		if (skill.getElement() == Lineage.ELEMENT_POISON && skill.getUid() != 9) {
			if (o.isBuffVenomResist())
				return false;
			if (o.getInventory() != null) {
				// 안타라스의 완력, 예지력, 인내력, 마력 갑옷 독내성
				ItemInstance armor = o.getInventory().getSlot(2);
				if (armor != null && armor instanceof AntharasArmor)
					return false;
			}
		}
		// 힐계열이 아닐때만 카운터매직 확인.
		if (!isHeal(skill) && o.isBuffCounterMagic() && cha.getObjectId() != o.getObjectId() && isCounterMagic(skill)) {
			BuffController.remove(o, CounterMagic.class);
			return false;
		}
		// 텔레포트락인 상태일경우 무시.
		if (o.isBuffTeleport())
			return false;
		// 확률 체크 안할경우 그냥 성공으로 리턴.
		if (!is_rate)
			return true;

		boolean is = false;
		// 확률 체크.
		if (o instanceof Character) {
			Character target = (Character) o;
			int attackLevel = cha.getLevel();
			int defenseLevel = target.getLevel();
			int probability = 0;

			// 프리징블리자드, 아이스랜스 확률, 포그오르슬리핑
	
			if ((skill.getSkillLevel() == 4 && skill.getSkillNumber() == 2)
					
					|| (skill.getSkillLevel() == 9 && skill.getSkillNumber() == 1) 
					// 슬로우, 매스슬로우, 커스 (이렇게 6개)

					||(skill.getSkillLevel() == 4 && skill.getSkillNumber() == 4)
					|| (skill.getSkillLevel() == 10 && skill.getSkillNumber() == 3)
					// 디케이 , 사일
					|| (skill.getSkillLevel() == 9 && skill.getSkillNumber() == 6)
					|| (skill.getSkillLevel() == 8 && skill.getSkillNumber() == 7)) {

				int _int = cha.getTotalInt(); // 케릭터의 인트의 총합을 받는다.
				if (_int < 0)
					_int = 1;
				// mr 하향 처리.
				int target_mr = getMr(target, false);

				if (_int <= 18) {
					if (target_mr <= 59) {
						probability = Lineage.int18_mr59;
					} else if (target_mr <= 69) {
						probability = Lineage.int18_mr69;
					} else if (target_mr <= 79) {
						probability = Lineage.int18_mr79;
					} else if (target_mr <= 89) {
						probability = Lineage.int18_mr89;
					} else if (target_mr <= 99) {
						probability = Lineage.int18_mr99;
					} else if (target_mr <= 109) {
						probability = Lineage.int18_mr109;
					} else if (target_mr <= 119) {
						probability = Lineage.int18_mr119;

					} else
						probability = 1;
				} else if (_int >= 19 && _int <= 22) {
					if (target_mr <= 59) {
						probability = Lineage.int22_mr59;
					} else if (target_mr <= 69) {
						probability = Lineage.int22_mr69;
					} else if (target_mr <= 79) {
						probability = Lineage.int22_mr79;
					} else if (target_mr <= 89) {
						probability = Lineage.int22_mr89;
					} else if (target_mr <= 99) {
						probability = Lineage.int22_mr99;
					} else if (target_mr <= 109) {
						probability = Lineage.int22_mr109;
					} else if (target_mr <= 119) {
						probability = Lineage.int22_mr119;

					} else
						probability = 1;
				} else if (_int >= 23 && _int <= 26) {
					if (target_mr <= 59) {
						probability = Lineage.int26_mr59;
					} else if (target_mr <= 69) {
						probability = Lineage.int26_mr69;
					} else if (target_mr <= 79) {
						probability = Lineage.int26_mr79;
					} else if (target_mr <= 89) {
						probability = Lineage.int26_mr89;
					} else if (target_mr <= 99) {
						probability = Lineage.int26_mr99;
					} else if (target_mr <= 109) {
						probability = Lineage.int26_mr109;
					} else if (target_mr <= 119) {
						probability = Lineage.int26_mr119;

					} else
						probability = 1;
				} else if (_int >= 27 && _int <= 30) {
					if (target_mr <= 59) {
						probability = Lineage.int30_mr59;
					} else if (target_mr <= 69) {
						probability = Lineage.int30_mr69;
					} else if (target_mr <= 79) {
						probability = Lineage.int30_mr79;
					} else if (target_mr <= 89) {
						probability = Lineage.int30_mr89;
					} else if (target_mr <= 99) {
						probability = Lineage.int30_mr99;
					} else if (target_mr <= 109) {
						probability = Lineage.int30_mr109;
					} else if (target_mr <= 119) {
						probability = Lineage.int30_mr119;

					} else
						probability = 1;
				} else if (_int >= 35) {
					if (target_mr <= 59) {
						probability = Lineage.int35_mr59;
					} else if (target_mr <= 69) {
						probability = Lineage.int35_mr69;
					} else if (target_mr <= 79) {
						probability = Lineage.int35_mr79;
					} else if (target_mr <= 89) {
						probability = Lineage.int35_mr89;
					} else if (target_mr <= 99) {
						probability = Lineage.int35_mr99;
					} else if (target_mr <= 109) {
						probability = Lineage.int35_mr109;
					} else if (target_mr <= 119) {
						probability = Lineage.int35_mr119;

					} else
						probability = 1;
				}

				if (target instanceof MonsterInstance)
					probability += 30;

				// 캔슬레이션
			} else if ((skill.getSkillLevel() == 6 && skill.getSkillNumber() == 3)
				|| (skill.getSkillLevel() == 7 && skill.getSkillNumber() == 1)){

				int _int = cha.getTotalInt(); // 케릭터의 인트의 총합을 받는다.
				if (_int < 0)
					_int = 1;
				// mr 하향 처리.
				int target_mr = getMr(target, false);

				if (_int <= 18) {
					if (target_mr <= 59) {
						probability = Lineage.can_int18_mr59;
					} else if (target_mr <= 69) {
						probability = Lineage.can_int18_mr69;
					} else if (target_mr <= 79) {
						probability = Lineage.can_int18_mr79;
					} else if (target_mr <= 89) {
						probability = Lineage.can_int18_mr89;
					} else if (target_mr <= 99) {
						probability = Lineage.can_int18_mr99;
					} else if (target_mr <= 109) {
						probability = Lineage.can_int18_mr109;
					} else if (target_mr <= 119) {
						probability = Lineage.can_int18_mr119;
					} else
						probability = 0;
				} else if (_int >= 19 && _int <= 22) {
					if (target_mr <= 59) {
						probability = Lineage.can_int22_mr59;
					} else if (target_mr <= 69) {
						probability = Lineage.can_int22_mr69;
					} else if (target_mr <= 79) {
						probability = Lineage.can_int22_mr79;
					} else if (target_mr <= 89) {
						probability = Lineage.can_int22_mr89;
					} else if (target_mr <= 99) {
						probability = Lineage.can_int22_mr99;
					} else if (target_mr <= 109) {
						probability = Lineage.can_int22_mr109;
					} else if (target_mr <= 119) {
						probability = Lineage.can_int22_mr119;
					} else
						probability = 0;
				} else if (_int >= 23 && _int <= 26) {
					if (target_mr <= 59) {
						probability = Lineage.can_int26_mr59;
					} else if (target_mr <= 69) {
						probability = Lineage.can_int26_mr69;
					} else if (target_mr <= 79) {
						probability = Lineage.can_int26_mr79;
					} else if (target_mr <= 89) {
						probability = Lineage.can_int26_mr89;
					} else if (target_mr <= 99) {
						probability = Lineage.can_int26_mr99;
					} else if (target_mr <= 109) {
						probability = Lineage.can_int26_mr109;
					} else if (target_mr <= 119) {
						probability = Lineage.can_int26_mr119;
					} else
						probability = 0;
				} else if (_int >= 27 && _int <= 30) {
					if (target_mr <= 59) {
						probability = Lineage.can_int30_mr59;
					} else if (target_mr <= 69) {
						probability = Lineage.can_int30_mr69;
					} else if (target_mr <= 79) {
						probability = Lineage.can_int30_mr79;
					} else if (target_mr <= 89) {
						probability = Lineage.can_int30_mr89;
					} else if (target_mr <= 99) {
						probability = Lineage.can_int30_mr99;
					} else if (target_mr <= 109) {
						probability = Lineage.can_int30_mr109;
					} else if (target_mr <= 119) {
						probability = Lineage.can_int30_mr119;
					} else
						probability = 0;
				} else if (_int >= 35) {
					if (target_mr <= 59) {
						probability = Lineage.can_int35_mr59;
					} else if (target_mr <= 69) {
						probability = Lineage.can_int35_mr69;
					} else if (target_mr <= 79) {
						probability = Lineage.can_int35_mr79;
					} else if (target_mr <= 89) {
						probability = Lineage.can_int35_mr89;
					} else if (target_mr <= 99) {
						probability = Lineage.can_int35_mr99;
					} else if (target_mr <= 109) {
						probability = Lineage.can_int35_mr109;
					} else if (target_mr <= 119) {
						probability = Lineage.can_int35_mr119;
					} else
						probability = 0;
				}

				if (target instanceof MonsterInstance)
					probability += 30;

				// 턴 언데드
			} else if (skill.getSkillLevel() == 3 && skill.getSkillNumber() == 1) {
				// 기본 확률 세팅.
				probability = skill.getProbabilityValue();
				// 인트당 확률 상승 처리.
				if (cha.getClassType() == 0x03) // 14
					probability += (cha.getTotalInt() - 14) * 9;
				else // 12
					probability += (cha.getTotalInt() - 12) * 5;

				// mr 하향 처리.
				int target_mr = getMr(target, false);
				probability -= target_mr / 7;

				// 엘리멘탈폴다운// 이레이즈 매직// 인탱글// 에어리어 오브 사일런스// 스트라이커 게일
			} else if ((skill.getSkillLevel() == 17 && skill.getSkillNumber() == 4) ||

					(skill.getSkillLevel() == 20 && skill.getSkillNumber() == 0)
					|| (skill.getSkillLevel() == 19 && skill.getSkillNumber() == 7)
					|| (skill.getSkillLevel() == 21 && skill.getSkillNumber() == 0)
					|| (skill.getSkillLevel() == 22 && skill.getSkillNumber() == 5)
					|| (skill.getSkillLevel() == 22 && skill.getSkillNumber() == 4) || // 폴루트 워터
					(skill.getSkillLevel() == 21 && skill.getSkillNumber() == 6)) {// 윈드 셰클

				if (cha.getLevel() >= o.getLevel()) { // 방어자가 레벨이 더 클때
					int le = o.getLevel() - cha.getLevel();
					if (le == 0)// 동렙 일때 40%
						return is = Util.random(1, 100) + ((Character) o).getDynamicHoldDefense() < 27; // 동렙일때
					else if (le >= 1 && le <= 5) // 61
						return is = Util.random(1, 100) + ((Character) o).getDynamicHoldDefense() < (32 + le);// 46% +
																												// 1~5
					else if (le >= 6 && le <= 10) // 65
						return is = Util.random(1, 100) + ((Character) o).getDynamicHoldDefense() < (38 + le);// 50% +
																												// 6~10
					else // 11렙차이 이상 //80
						return is = Util.random(1, 100) + ((Character) o).getDynamicHoldDefense() < 50; // 65
				} else {
					int le = cha.getLevel() - o.getLevel();
					if (le >= 1 && le <= 5) // 25
						return is = Util.random(1, 100) + ((Character) o).getDynamicHoldDefense() < (23 - le);//
					else if (le >= 6 && le <= 10) // 14
						return is = Util.random(1, 100) + ((Character) o).getDynamicHoldDefense() < (19 - le);// 9
					else // 10렙 이상 // 1
						return is = Util.random(1, 100) + ((Character) o).getDynamicHoldDefense() < 10;// 1
				}
			} else if (skill.getSkillLevel() == 20 && skill.getSkillNumber() == 4) {// 어스 바인드)
				if (cha.getLevel() >= o.getLevel()) { // 방어자가 레벨이 더 클때
					int le = o.getLevel() - cha.getLevel();
					if (le == 0)// 동렙 일때 40%
						return is = Util.random(1, 100) + ((Character) o).getDynamicHoldDefense() < 25; // 동렙일때
					else if (le >= 1 && le <= 5) // 61
						return is = Util.random(1, 100) + ((Character) o).getDynamicHoldDefense() < (30 + le);// 46% +
																												// 1~5
					else if (le >= 6 && le <= 10) // 65
						return is = Util.random(1, 100) + ((Character) o).getDynamicHoldDefense() < (36 + le);// 50% +
																												// 6~10
					else // 11렙차이 이상 //80
						return is = Util.random(1, 100) + ((Character) o).getDynamicHoldDefense() < 50; // 65
				} else {
					int le = cha.getLevel() - o.getLevel();
					if (le >= 1 && le <= 5) // 25
						return is = Util.random(1, 100) + ((Character) o).getDynamicHoldDefense() < (21 - le);//
					else if (le >= 6 && le <= 10) // 14
						return is = Util.random(1, 100) + ((Character) o).getDynamicHoldDefense() < (17 - le);// 9
					else // 10렙 이상 // 1
						return is = Util.random(1, 100) + ((Character) o).getDynamicHoldDefense() < 10;// 1
				}
				// 디케이 포션 확률.
			} else if ((skill.getSkillLevel() == 9 && skill.getSkillNumber() == 6))
				return Util.random(0, 100) <= 22;
			// 리턴투네이처
			else if (skill.getSkillLevel() == 19 && skill.getSkillNumber() == 0)
				probability = (int) ((30 / 10D) * (attackLevel - defenseLevel)) + 40;
			// 쇼크스턴
			else if (skill.getSkillLevel() == 11 && skill.getSkillNumber() == 6) {
				if (cha.getLevel() >= o.getLevel()) { // 방어자가 레벨이 더 클때
					int le = o.getLevel() - cha.getLevel();
					if (le == 0)// 동렙 일때 40%
						return is = Util.random(1, 100) + ((Character) o).getDynamicStunDefense() <= 30; // 동렙일때 스턴 거는
																											// 확률 100분율
					else if (le >= 1 && le <= 5) // 61
						return is = Util.random(1, 100) + ((Character) o).getDynamicStunDefense() <= (35 + le);// 46% +
																												// 1~5
					else if (le >= 6 && le <= 10) // 65
						return is = Util.random(1, 100) + ((Character) o).getDynamicStunDefense() <= (41 + le);// 50% +
																												// 6~10
					else // 11렙차이 이상 //80
						return is = Util.random(1, 100) + ((Character) o).getDynamicStunDefense() <= 53; // 65
				} else {
					int le = cha.getLevel() - o.getLevel();
					if (le >= 1 && le <= 5) // 25
						return is = Util.random(1, 100) + ((Character) o).getDynamicStunDefense() <= (27 - le);//
					else if (le >= 6 && le <= 10) // 14
						return is = Util.random(1, 100) + ((Character) o).getDynamicStunDefense() <= (23 - le);// 9
					else // 10렙 이상 // 1
						return is = Util.random(1, 100) + ((Character) o).getDynamicStunDefense() <= 14;// 1
				}

				// 모탈 바디
			} else if (skill.getSkillLevel() == 24 && skill.getSkillNumber() == 6)
				probability = 5;
			// 썬더 그랩
			else if (skill.getSkillLevel() == 24 && skill.getSkillNumber() == 7)
				probability = 50 + attackLevel - defenseLevel;
			// 컨퓨전
			else if (skill.getSkillLevel() == 26 && skill.getSkillNumber() == 1)
				probability = (int) ((10 / 10D) * (attackLevel - defenseLevel)) + 10;
			// 마인드 브레이크
			else if (skill.getSkillLevel() == 26 && skill.getSkillNumber() == 6)
				probability = (int) ((20 / 10D) * (attackLevel - defenseLevel)) + 45;
			// 본 브레이크
			else if (skill.getSkillLevel() == 26 && skill.getSkillNumber() == 7)
				probability = (int) ((25 / 10D) * (attackLevel - defenseLevel)) + 25;
			// 판타즘
			else if (skill.getSkillLevel() == 27 && skill.getSkillNumber() == 3)
				probability = (int) ((20 / 10D) * (attackLevel - defenseLevel)) + 50;
			// 암 브레이커
			else if (skill.getSkillLevel() == 27 && skill.getSkillNumber() == 4)
				probability = (int) ((20 / 10D) * (attackLevel - defenseLevel)) + 50;
			// 패닉
			else if (skill.getSkillLevel() == 28 && skill.getSkillNumber() == 0)
				probability = (int) ((20 / 10D) * (attackLevel - defenseLevel)) + 50;
			// 조이 오브 페인
			else if (skill.getSkillLevel() == 28 && skill.getSkillNumber() == 1)
				probability = (int) ((40 / 10D) * (attackLevel - defenseLevel)) + 60;
			// 세이프 체인지
			else if (skill.getSkillLevel() == 10 && skill.getSkillNumber() == 7) {
				probability = (int) ((35 / 10D) * (attackLevel - defenseLevel)) + 40;

			} else {
				int dice = skill.getProbabilityDice();
				int diceCount = getMagicBonus(cha) + getMagicLevel(cha);
				if (cha instanceof PcInstance) {
					switch (cha.getClassType()) {
					case 0x03:
						diceCount = getMagicBonus(cha) + getMagicLevel(cha) + 1;
						break;
					case 0x02:
					case 0x05:
						diceCount = getMagicBonus(cha) + getMagicLevel(cha);
						break;
					default:
						diceCount = getMagicBonus(cha) + getMagicLevel(cha) - 1;
						break;
					}
				}
				if (diceCount < 1)
					diceCount = 1;
				for (int i = 0; i < diceCount; i++)
					probability += Util.random(1, dice);
				probability = probability * 10 / 10;
				//
				probability -= getMr(target, false);
				// 테이밍몬스터
				if (skill.getSkillLevel() == 5 && skill.getSkillNumber() == 3) {
					double probabilityRevision = 1;
					if ((target.getMaxHp() * 1 / 4) > target.getNowHp())
						probabilityRevision = 1.3;
					else if ((target.getMaxHp() * 2 / 4) > target.getNowHp())
						probabilityRevision = 1.2;
					else if ((target.getMaxHp() * 3 / 4) > target.getNowHp())
						probabilityRevision = 1.1;
					probability *= probabilityRevision;
				}
			}
			// 요정 확률마법
			if (cha.getSix() == 22 && cha.getClassType() == 0x02) {
				probability += 5;
			}
			// 요정 이라면 법사보단 확률 나추기
			if (cha.getClassType() == 0x02) {
				if (probability >= 20)
					probability -= 7;
			}
			//
			int rnd = Util.random(1, 100);
			if (probability > 80)
				probability = 80;
			// 5 >= 99
			is = probability >= rnd;

			is = Util.random(1, 100) < (probability < 1 ? Util.random(1, 3) : Math.round(probability));
		} else
			is = Util.random(1, 100) > Util.random(1, 100);

			if (Lineage.is_miss_effect && !is)
				cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, Lineage.miss_effect));

			return is;
		}

	/**
	 * 같은 혈맹원인지 체크
	 * 
	 * @param cha
	 * @param o
	 * @return
	 */
	static public boolean isClan(Character cha, object o) {
		if (o == null)
			return false;
		if (cha.getObjectId() != o.getObjectId()) {
			if (o.getClanId() == 0)
				return false;
			if (cha.getClanId() != o.getClanId())
				return false;
		}
		return true;
	}

	/**
	 * 같은 파티원인지 체크.
	 * 
	 * @param cha
	 * @param o
	 * @return   
	 */
	static public boolean isParty(Character cha, object o) {
		return cha.getObjectId() == o.getObjectId() || (cha.getPartyId() != 0 && o.getPartyId() == cha.getPartyId());
	}

	/**
	 * 공성존에서 공성중일때 마법시전을 해도되는지 확인.
	 * 
	 * @param skill
	 * @return
	 */
	static public boolean isKingdomZoneMagic(Skill skill, object o) {
		Object temp = PluginController.init(SkillController.class, "isKingdomZoneMagic", skill, o);
		if (temp != null && temp instanceof Boolean)
			return (Boolean) temp;
		//
		if (!Lineage.kingdom_war_callclan && (skill.getSkillLevel() == 15 && skill.getSkillNumber() == 3) || // 콜 클랜
				(skill.getSkillLevel() == 15 && skill.getSkillNumber() == 5) // 런 클랜
		) {
			Kingdom k = KingdomController.findKingdomLocation(o);
			if (k != null && k.isWar())
				return false;
		}

		return true;
	}

	/**
	 * 몬스터의 스킬 대미지 추출 함수
	 */
	static public int getMonDamage(Character cha, object target, object o) {
		int dmg = 0;
		return dmg;
	}

	/**
	 * 데미지 추출 처리 함수.
	 * 
	 * @param cha
	 * @param target
	 * @param o
	 * @param skill
	 * @param alpha_dmg
	 * @param skill_element
	 * @return
	 */
	static public int getDamage(Character cha, object target, object o, Skill skill, int alpha_dmg, int skill_element) {
		// 버그 방지
		if (o == null || skill == null/* || o.getGm()!=0 */)
			return 0;
		// 기본적으로 공격 불가능한 객체
		if (o instanceof ItemInstance || o instanceof BoardInstance
				|| (!(o instanceof Cracker || o instanceof CrackerDmg || target instanceof PigeonGroup
						|| target instanceof FireOfSoul) && target instanceof BackgroundInstance))
			return 0;
		// 죽은거 무시.
		if (o.isDead())
			return 0;
		// 굳은거 무시.
		if (o.isLockHigh())
			return 0;
		// 앱솔 무시.
		if (o.isBuffAbsoluteBarrier() && isAbsoluteBarrier(skill))
			return 0;
		// 메디테이션 제거
		if (cha.isBuffMeditation())
			BuffController.remove(cha, Meditation.class);
		
		// 몬스터 끼리는 못하게 20180312
		if (cha instanceof PetInstance || o instanceof PetInstance || cha instanceof SummonInstance
				|| o instanceof SummonInstance) {

		} else if (cha instanceof MonsterInstance && o instanceof MonsterInstance) {
			return 0;
		}

		if (cha == null)
			return alpha_dmg;
		// 힐계열이 아닐때만 카운터매직 확인.
		if (!isHeal(skill) && o.isBuffCounterMagic() && cha.getObjectId() != o.getObjectId() && isCounterMagic(skill)) {
			BuffController.remove(o, CounterMagic.class);
			return 0;
		}

		if (isAttackRangeMagic(skill)) {
			if (cha.getClanId() > 0 && o.getClanId() > 0 && cha.getClanId() == o.getClanId())
				return 0;
		}

		// 공격마법일때 공격 가능존인지 확인.
		if (isAttackMagic(skill) && !World.isAttack(cha, o))
			return 0;
		if (isAttackRangeMagic(skill) && !isMagicAttackRange(cha, o) && target.getObjectId() != o.getObjectId())
			return 0;
		// 장애물이 막고있으면 무시.
		if (!Util.isAreaAttack(cha, o))
			return 0;
		// 내성문이라면 공성중일때만 가능. 힐계열마법 무시.
		if (o instanceof KingdomDoor) {
			KingdomDoor kd = (KingdomDoor) o;
			if (kd.getKingdom() == null || kd.getNpc() == null)
				return 0;
			if (!kd.getKingdom().isWar() && kd.getNpc().getName().indexOf("내성문") > 0)
				return 0;
			if (isHeal(skill))
				return 0;
		}
		//
		if (!target.isAttack(cha, true))
			return 0;

		double o_mr = 0;

		if (o instanceof Character) {
			Character t = (Character) o;
			o_mr = getMr(t, false);
			if (o_mr > Lineage.max_damage_mr)
				o_mr = Lineage.max_damage_mr;
			ItemInstance shield = null;
			// 레이저종류의 마법일경우 타켓의 방패종류에따라 데미지를 적용 안함.
			if (skill.getElement() == Lineage.ELEMENT_LASER && t.getInventory() != null) {
				shield = t.getInventory().getSlot(7);
				if (shield != null) {
					switch (shield.getItem().getNameIdNumber()) {
					case 196: // 반사 방패
						if (shield.getEnLevel() >= 5 && Util.random(1, 100) <= 20)
							return 0;
						break;
					case 2035: // 붉은 기사의 방패
						if (shield.getEnLevel() >= 7 && Util.random(1, 100) <= 20)
							return 0;
						break;
					}
				}
			}
			// 굳는 마법 일경우 반사방패 반사 처리효과 넣기.
			if (skill.getLock().equalsIgnoreCase("low") || skill.getLock().equalsIgnoreCase("high")) {
				shield = t.getInventory().getSlot(7);
				if (shield != null) {
					switch (shield.getItem().getNameIdNumber()) {
					case 196: // 반사 방패
						if (shield.getEnLevel() > 4)
							return 0;
						break;
					case 12423: // 시어의 심안
						if (shield.getEnLevel() > 0)
							return 0;
						break;
					}
				}
			}
		}

		int o_fire = 0;
		int o_warter = 0;
		int o_earh = 0;
		int o_wind = 0;
		int o_element = 0;
		if (o instanceof Character) {
			Character o_cha = (Character) o;
			o_fire = o_cha.getTotalFireress();
			o_earh = o_cha.getTotalEarthress();
			o_wind = o_cha.getTotalWindress();
			o_warter = o_cha.getTotalWaterress();
		}

		double dmg = 0;
		// 베이스 스텟에 의한 마법 추출 대미지
		double stat_dmg = toOriginalStatMagicDamage(cha); // 1 1 2 3 1 군 기 요 마 다 3
		// SP 추출 / 레벨과 인트에 의한 SP 추출
		double sp_dmg = getSp(cha, false); // 1~30 20 인트 25기준
		
		// 인트에 의한 대미지 추출
		double int_dmg = getIntDamage(cha); // 0 ~ 15 4 인트 25기준

		// double ml_dmg = getMagicLevel(cha);
		// double mb_dmg = getMagicBonus(cha);
		// 최소 대미지 3
		double mindmg = (skill.getMindmg() + alpha_dmg) * ((sp_dmg * 0.5) + (stat_dmg * 0.5) + (int_dmg * 0.4));// 5.8 1
		// 최대 대미지 4
		double maxdmg = (skill.getMaxdmg() + alpha_dmg) * ((sp_dmg * 0.6) + (stat_dmg * 0.6) + (int_dmg * 0.5));// 16 2

		double healmindmg = 0; // 2
		double healmaxdmg = 0;//

		// 데미지 추출.
		if (isHeal(skill)) {
			switch (cha.getTotalInt()) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
				healmindmg = 2;
				healmaxdmg = 14;
				break;
			case 9:
			case 10:
			case 11:
			case 12:
				healmindmg = 5;
				healmaxdmg = 20;
				break;
			case 13:
			case 14:
			case 15:
			case 16:
				healmindmg = 7;
				healmaxdmg = 22;
				break;
			case 17:
				healmindmg = 9;
				healmaxdmg = 26;
				break;
			case 18:
				healmindmg = 15;
				healmaxdmg = 33;
				break;
			case 19:
				healmindmg = 17;
				healmaxdmg = 39;
				break;
			case 20:
				healmindmg = 17;
				healmaxdmg = 41;
				break;
			case 21:
				healmindmg = 27;
				healmaxdmg = 51;
				break;
			case 22:
				healmindmg = 27;
				healmaxdmg = 57;
				break;
			case 23:
				healmindmg = 29;
				healmaxdmg = 63;
				break;
			case 24:
				healmindmg = 35;
				healmaxdmg = 69;
				break;
			case 25:
				healmindmg = 41;
				healmaxdmg = 73;
				break;
			case 26:
				healmindmg = 42;
				healmaxdmg = 80;
				break;
			case 27:
				healmindmg = 46;
				healmaxdmg = 86;
				break;
			case 28:
				healmindmg = 49;
				healmaxdmg = 92;
				break;
			case 29:
				healmindmg = 53;
				healmaxdmg = 98;
				break;
			case 30:
				healmindmg = 57;
				healmaxdmg = 104;
				break;
			case 31:
				healmindmg = 60;
				healmaxdmg = 110;
				break;
			case 32:
				healmindmg = 64;
				healmaxdmg = 116;
				break;
			case 33:
				healmindmg = 68;
				healmaxdmg = 121;
				break;
			case 34:
				healmindmg = 71;
				healmaxdmg = 127;
				break;
			case 35:
				healmindmg = 75;
				healmaxdmg = 133;
				break;
			case 36:
				healmindmg = 79;
				healmaxdmg = 139;
				break;
			case 37:
				healmindmg = 82;
				healmaxdmg = 145;
				break;
			case 38:
				healmindmg = 86;
				healmaxdmg = 151;
				break;
			case 39:
				healmindmg = 90;
				healmaxdmg = 157;
				break;
			case 40:
				healmindmg = 93;
				healmaxdmg = 163;
				break;
			case 41:
				healmindmg = 97;
				healmaxdmg = 169;
				break;
			case 42:
				healmindmg = 101;
				healmaxdmg = 175;
				break;
			case 43:
				healmindmg = 104;
				healmaxdmg = 181;
				break;
			case 44:
				healmindmg = 108;
				healmaxdmg = 187;
				break;
			case 45:
				healmindmg = 112;
				healmaxdmg = 193;
				break;
			case 46:
				healmindmg = 115;
				healmaxdmg = 199;
				break;
			case 47:
				healmindmg = 119;
				healmaxdmg = 205;
				break;
			case 48:
				healmindmg = 123;
				healmaxdmg = 211;
				break;
			case 49:
				healmindmg = 126;
				healmaxdmg = 217;
				break;
			case 50:
				healmindmg = 130;
				healmaxdmg = 223;
				break;
			default:
				healmindmg = 134;
				healmaxdmg = 229;
				break;
			}
			// 익스트라 힐
			if (skill.getSkillLevel() == 3 && skill.getSkillNumber() == 2) {
				healmindmg = healmindmg * 1.5;
				healmaxdmg = healmaxdmg * 1.5;
			}
			// 그레이터 힐 , 힐올
			if ((skill.getSkillLevel() == 5 && skill.getSkillNumber() == 2)
					|| (skill.getSkillLevel() == 7 && skill.getSkillNumber() == 0)
					|| (skill.getSkillLevel() == 8 && skill.getSkillNumber() == 0)) {
				switch (cha.getTotalInt()) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
				case 16:
				case 17:
				case 18:
					healmindmg = 65;
					healmaxdmg = 130;
					break;
				case 19:
					healmindmg = 91;
					healmaxdmg = 140;
					break;
				case 20:
					healmindmg = 87;
					healmaxdmg = 150;
					break;
				case 21:
					healmindmg = 105;
					healmaxdmg = 160;
					break;
				case 22:
					healmindmg = 105;
					healmaxdmg = 170;
					break;
				case 23:
					healmindmg = 113;
					healmaxdmg = 180;
					break;
				case 24:
					healmindmg = 139;
					healmaxdmg = 190;
					break;
				case 25:
					healmindmg = 129;
					healmaxdmg = 200;
					break;
				case 26:
				case 27:
				case 28:
				case 29:
				case 30:
					healmindmg = 158;
					healmaxdmg = 210;
					break;
				case 31:
				case 32:
				case 33:
				case 34:
				case 35:
					healmindmg = 182;
					healmaxdmg = 220;
					break;
				case 36:
				case 37:
				case 38:
				case 39:
				case 40:
					healmindmg = 206;
					healmaxdmg = 323;
					break;
				case 41:
				case 42:
				case 43:
				case 44:
				case 45:
					healmindmg = 230;
					healmaxdmg = 354;
					break;
				case 46:
				case 47:
				case 48:
				case 49:
				case 50:
					healmindmg = 263;
					healmaxdmg = 379;
					break;
				default:
					healmindmg = 263;
					healmaxdmg = 379;
					break;
				}
			}
			// 그힐
			if (skill.getSkillLevel() == 5 && skill.getSkillNumber() == 2) {
				healmindmg = healmindmg * Lineage.great_hill;
				healmaxdmg = healmaxdmg * Lineage.great_hill;
			}
			// 힐 올
			if (skill.getSkillLevel() == 7 && skill.getSkillNumber() == 0) {
				healmindmg = healmindmg * Lineage.hill_all;
				healmaxdmg = healmaxdmg * Lineage.hill_all;
			}
			// 풀 힐
			if (skill.getSkillLevel() == 8 && skill.getSkillNumber() == 0) {
				healmindmg = healmindmg * Lineage.full_hill;
				healmaxdmg = healmaxdmg * Lineage.full_hill;
			}
			// 네이처스 터치
			/*
			 * if(skill.getSkillLevel()==20 && skill.getSkillNumber()==5){ healmindmg =
			 * healmindmg * 10; healmaxdmg = healmaxdmg * 10; }
			 */
			// 네이쳐스블레싱
			if (skill.getSkillLevel() == 21 && skill.getSkillNumber() == 3) {
				switch (cha.getTotalInt()) {
				case 12:
					healmindmg = 101;
					healmaxdmg = 179;
					break;
				case 13:
				case 14:
				case 15:
				case 16:
					healmindmg = 107;
					healmaxdmg = 191;
					break;
				case 17:
				case 18:
					healmindmg = 107;
					healmaxdmg = 211;
					break;
				case 19:
					healmindmg = 113;
					healmaxdmg = 231;
					break;
				case 20:
					healmindmg = 145;
					healmaxdmg = 257;
					break;
				case 21:
					healmindmg = 163;
					healmaxdmg = 279;
					break;
				case 22:
					healmindmg = 167;
					healmaxdmg = 293;
					break;
				case 23:
					healmindmg = 171;
					healmaxdmg = 311;
					break;
				case 24:
					healmindmg = 185;
					healmaxdmg = 323;
					break;
				case 25:
					healmindmg = 185;
					healmaxdmg = 323;
					break;
				case 26:
				case 27:
				case 28:
				case 29:
				case 30:
					healmindmg = 187;
					healmaxdmg = 327;
					break;
				case 31:
				case 32:
				case 33:
				case 34:
				case 35:
					healmindmg = 209;
					healmaxdmg = 333;
					break;
				case 36:
				case 37:
				case 38:
				case 39:
				case 40:
					healmindmg = 219;
					healmaxdmg = 337;
					break;
				case 41:
				case 42:
				case 43:
				case 44:
				case 45:
					healmindmg = 231;
					healmaxdmg = 342;
					break;
				case 46:
				case 47:
				case 48:
				case 49:
				case 50:
					healmindmg = 243;
					healmaxdmg = 346;
					break;
				default:
					healmindmg = 255;
					healmaxdmg = 351;
					break;
				}
			}
			dmg = Util.random(healmindmg, healmaxdmg);

			if (cha.getClassType() == 0x03)//
				dmg = dmg * 0.53;// 0.75 //0 1 2 3 4
			else if (cha.getClassType() == 0x02) // 군기요마다
				dmg = dmg * 0.60;// 0.85
			else
				dmg = dmg * 0.3;

		} else {
			// 힐 계열 제외 마법 모두 하향
			dmg += Util.random(mindmg * 1.1, maxdmg * 1.3);
		}

		// 지혜물약 복용상태
		if (cha.isBuffWisdom() && isHeal(skill)) {
			dmg *= 1.1;
		} else if (cha.isBuffWisdom()) {
			dmg *= 1.2;
		}
		
		// 카오틱 마법이라면 라우풀 수치에따라 대미지에 영향주기.
		// 풀카오일 경우 대미지의 49% 추가
		if (isChaoticMagic(skill) && cha instanceof PcInstance && cha.getLawful() < Lineage.NEUTRAL)
			dmg += dmg * (((Lineage.NEUTRAL - cha.getLawful()) * 0.00001) * 1.5);
		
		// 힐계열 마법이라면 라우풀값에 따라 데미지 영향주기.
		if (isHeal(skill)) {
			//
			dmg *= (double) cha.getLawful() / 82200; // (double)Lineage.NEUTRAL; 98303

			// 힐계열 마법이라면 워터라이프 체크.
			if (target.isBuffWaterLife()) {
				dmg = dmg * 2;
				BuffController.remove(target, WaterLife.class);
			}

			// 폴루트워터 상태일때 반으로 처리.
			if (target.isBuffPolluteWater())
				dmg = dmg <= 0 ? 0 : dmg / 2;

			// 힐계열 마법 아닐때.
		} else {
			// 이뮨 투 함
			if (o.isBuffImmuneToHarm())
				dmg = dmg <= 0 ? 0 : dmg * Lineage.imm_dmg_skill;
		}
		// 수룡/탄생/형상/생명의 마안
		if ((target.isBuffMaanWatar() || target.isBuffMaanLife() || target.isBuffMaanBirth() || target.isBuffMaanShape()) && Util.random(1, 100) <= 6) {
			dmg = dmg / 2;
			
				if (target.isBuffMaanWatar())
					target.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), target, Lineage.수룡의마안_이팩트));
				else if (target.isBuffMaanBirth())
					target.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), target, Lineage.탄생의마안_이팩트));
				else if (target.isBuffMaanShape())
					target.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), target, Lineage.형상의마안_이팩트));
				else if (target.isBuffMaanLife())
					target.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), target, Lineage.생명의마안_이팩트));
			
		}
		// 힐계열 마법이 아닐때
		// : mr체크해서 데미지 하향.
		// : 기타 버프 상태따라 처리.
		if (!isHeal(skill)) {
			// 속성저항력값 추출.
			switch (skill_element) {
			case Lineage.ELEMENT_FIRE:
				o_element = o_fire;
				break;
			case Lineage.ELEMENT_WATER:
				o_element = o_warter;
				break;
			case Lineage.ELEMENT_EARTH:
				o_element = o_earh;
				break;
			case Lineage.ELEMENT_WIND:
				o_element = o_wind;
				break;
			}
			// 속성마법 데미지 하향처리.
			if (o_element > 0) {
				double el_dmg = o_element * 0.6;
				if (el_dmg > 100)
					el_dmg = 100;
				el_dmg = el_dmg * 0.01;
				dmg -= dmg * el_dmg;
			}

			if (cha instanceof PcInstance && o instanceof PcInstance) {
				PcInstance use = (PcInstance) o;
				// PvP 대미지 리덕션
				dmg -= use.getDynamicAddPvpReduction();
			}
			
			// 디스만 처리 할때.
			if (skill.getSkillLevel() == 10 && skill.getSkillNumber() == 4) {
				dmg -= dmg * o_mr / 300;
			} else {
				if (o_mr < 50)
					dmg -= dmg * o_mr / 300;
				else if (o_mr >= 50 && o_mr < 60)
					dmg -= dmg * o_mr / 299;
				else if (o_mr >= 60 && o_mr < 70)
					dmg -= dmg * o_mr / 298;
				else if (o_mr >= 70 && o_mr < 80)
					dmg -= dmg * o_mr / 297;
				else if (o_mr >= 80 && o_mr < 90)
					dmg -= dmg * o_mr / 296;
				else if (o_mr >= 90 && o_mr < 100)
					dmg -= dmg * o_mr / 294;
				else if (o_mr >= 100 && o_mr < 110)
					dmg -= dmg * o_mr / 292;
				else if (o_mr >= 110 && o_mr < 120)
					dmg -= dmg * o_mr / 290;
				else if (o_mr >= 120 && o_mr < 130)
					dmg -= dmg * o_mr / 285;
				else if (o_mr >= 130 && o_mr < 140) // 130 같거나크고 140보다 작다
					dmg -= dmg * o_mr / 284;
				else if (o_mr >= 140 && o_mr < 150)
					dmg -= dmg * o_mr / 283;
				else if (o_mr >= 150 && o_mr < 160) // 위내용처럼 120~130은 적용이안되더라구요 ㅠㅠ
					dmg -= dmg * o_mr / 280;
				else if (o_mr >= 160 && o_mr < 170)
					dmg -= dmg * o_mr / 279;
				else if (o_mr >= 170 && o_mr < 180) // 그래서 추가했습니다
					dmg -= dmg * o_mr / 278;
				else if (o_mr >= 180 && o_mr < 190)
					dmg -= dmg * o_mr / 270;
				else if (o_mr >= 190 && o_mr < 200)
					dmg -= dmg * o_mr / 265;
				else if (o_mr >= 200 && o_mr < 210)
					dmg -= dmg * o_mr / 263;
				else if (o_mr >= 210 && o_mr < 220)
					dmg -= dmg * o_mr / 261;
				else if (o_mr >= 220 && o_mr < 230)
					dmg -= dmg * o_mr / 260;
				else if (o_mr >= 230 && o_mr < 240)
					dmg -= dmg * o_mr / 259;
				else if (o_mr >= 240 && o_mr < 250)
					dmg -= dmg * o_mr / 258;
				else if (o_mr >= 250 && o_mr < 260)
					dmg -= dmg * o_mr / 257;
				else if (o_mr >= 260)
					dmg -= dmg * 260 / 290; // 220이상부터 데미지 안 들어옵니다.
			}
			double p_mr = 0;
			if (o_mr < 101) {
				p_mr = o_mr / 2;
			} else {
				p_mr = 50 + (o_mr - 100) / 10;
			}
			dmg -= dmg * p_mr / 100;
			
			if (o instanceof PcInstance) {
				if (o.getInventory() != null) {
					ItemInstance targetShiled = o.getInventory().getSlot(Lineage.SLOT_SHIELD);
					if (targetShiled != null && targetShiled.getItem().getName().equalsIgnoreCase("반역자의 방패") && Util.random(1, 100) <= targetShiled.getEnLevel() * Lineage.banshiled) {
						o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 13418), true);
						dmg -= 50;
					}
				}
			}

			if (cha instanceof MonsterInstance && target instanceof PcInstance)
				dmg = dmg * 0.1;
			if (target instanceof MonsterInstance)
				dmg = dmg * Lineage_Balance.pvp_skill_dmg_mon;
			if (target instanceof PcInstance && target.getClassType() == Lineage.LINEAGE_CLASS_ROYAL)
				dmg = dmg * Lineage_Balance.pvp_skill_dmg_royal;
			if (target instanceof PcInstance && target.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT)
				dmg = dmg * Lineage_Balance.pvp_skill_dmg_knight;
			if (target instanceof PcInstance && target.getClassType() == Lineage.LINEAGE_CLASS_ELF)
				dmg = dmg * Lineage_Balance.pvp_skill_dmg_elf;
			if (target instanceof PcInstance && target.getClassType() == Lineage.LINEAGE_CLASS_WIZARD)
				dmg = dmg * Lineage_Balance.pvp_skill_dmg_wizard;
			if (cha instanceof PcInstance &&  target instanceof PcInstance && cha.getClassType() == Lineage.LINEAGE_CLASS_WIZARD
					&& (target.getClassType() == Lineage.LINEAGE_CLASS_ROYAL ||target.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT))
				dmg = dmg * Lineage_Balance.pvp_skill_dmg_KvsW;

			// }

		}

		if (dmg < 1) {
			int rate = 10;
			dmg *= rate;
		}
		if (target !=null&& cha!=null &&cha instanceof PcInstance && target instanceof MonsterInstance) {
			if (skill.getSkillLevel() == 6 && skill.getSkillNumber() == 4) {
				dmg *= Lineage.irub_mon_dmg;
			}
		}
	
		
		// 디스중첩
		if (target instanceof PcInstance) {
			if (skill.getSkillLevel() == 10 && skill.getSkillNumber() == 1) {
				dmg *= Lineage.miti_cha_dmg;
			}
			if (skill.getSkillLevel() == 10 && skill.getSkillNumber() == 4) {
				if (target.getDisDelay() > 0) {
					dmg = 0;
				}
				if (target.getDisDelay() <= 0) {
					target.setDisDelay(3);

					if (target.isBuffEraseMagic())
						dmg = Util.random(700, 900);
					else
						dmg = Util.random(Lineage.dis_min, Lineage.dis_max);

					if (target.isBuffImmuneToHarm())
						dmg = dmg * Lineage.imm_dmg_skill;

				}
			}
			// 여기가 최소 대미지 설정하는 부분인가봐용
			return (int) (dmg < 0 ? 0 : dmg);
		}	
		// 여기랑
			return (int) (dmg < 0 ? 0 : dmg);
	
    }

	/**
	 * 물리데미지 처리중 버프에따른 데미지를 가중할때 호출해서 사용함.
	 * 
	 * @param c
	 * @param cha
	 * @param target
	 * @return
	 */
	static public int toDamagePlus(Class<?> c, Character cha, object target) {
		Buff b = BuffController.find(cha);
		if (b == null)
			return 0;
		BuffInterface bi = b.find(c);
		if (bi == null)
			return 0;
		return bi.toDamagePlus(cha, target);
	}

	/**
	 * 카오틱 마법인지 확인.
	 */
	static private boolean isChaoticMagic(Skill skill) {
		// 칠터치
		if (skill.getSkillLevel() == 2 && skill.getSkillNumber() == 1)
			return true;
		// 커스:포이즌
		if (skill.getSkillLevel() == 2 && skill.getSkillNumber() == 2)
			return true;
		// 커스:블라인드
		if (skill.getSkillLevel() == 3 && skill.getSkillNumber() == 3)
			return true;
		// 웨폰브레이크
		if (skill.getSkillLevel() == 4 && skill.getSkillNumber() == 2)
			return true;
		// 뱀파이어릭터치
		if (skill.getSkillLevel() == 4 && skill.getSkillNumber() == 3)
			return true;
		// 커스:패럴라이즈
		if (skill.getSkillLevel() == 5 && skill.getSkillNumber() == 0)
			return true;
		// 크리에이트 좀비
		if (skill.getSkillLevel() == 6 && skill.getSkillNumber() == 0)
			return true;
		// 위크니스
		if (skill.getSkillLevel() == 6 && skill.getSkillNumber() == 6)
			return true;
		// 서먼몬스터
		if (skill.getSkillLevel() == 7 && skill.getSkillNumber() == 2)
			return true;
		// 디지즈
		if (skill.getSkillLevel() == 7 && skill.getSkillNumber() == 7)
			return true;
		// 블리자드
		if (skill.getSkillLevel() == 8 && skill.getSkillNumber() == 2)
			return true;
		// 포그오브슬리핑
		if (skill.getSkillLevel() == 9 && skill.getSkillNumber() == 1)
			return true;
		// 매스 슬로우
		if (skill.getSkillLevel() == 10 && skill.getSkillNumber() == 3)
			return true;
		// 프리징 블리자드
		if (skill.getSkillLevel() == 10 && skill.getSkillNumber() == 7)
			return true;
		return false;
	}

	/**
	 * 앱솔루트 마법이 시전중일경우 공격당하면 해당 함수가 호출됨</br>
	 * 공격당한 마법이 앱솔에 영향을 받는지 확인함.
	 * 
	 * @param skill : 확인할 마법
	 * @return : true(영향받음), false(영향안받음)
	 */
	static private boolean isAbsoluteBarrier(Skill skill) {
		// 캔슬레이션
		if (skill.getSkillLevel() == 6 && skill.getSkillNumber() == 3)
			return false;
		// 이레이즈매직
		/*
		 * if(skill.getSkillLevel()==20 && skill.getSkillNumber()==0) return false;
		 */
		// 슬로우
		if (skill.getSkillLevel() == 4 && skill.getSkillNumber() == 4)
			return false;
		// 인탱글
		if (skill.getSkillLevel() == 19 && skill.getSkillNumber() == 7)
			return false;
		// 웨폰 브레이크
		if (skill.getSkillLevel() == 4 && skill.getSkillNumber() == 2)
			return false;
		// 커스: 패럴라이즈
		if (skill.getSkillLevel() == 5 && skill.getSkillNumber() == 0)
			return false;
		// 디지즈
		if (skill.getSkillLevel() == 7 && skill.getSkillNumber() == 7)
			return false;
		// 다크니스
		if (skill.getSkillLevel() == 5 && skill.getSkillNumber() == 7)
			return false;
		// 위크니스
		if (skill.getSkillLevel() == 6 && skill.getSkillNumber() == 6)
			return false;
		// 포그 오브 슬리핑
		if (skill.getSkillLevel() == 9 && skill.getSkillNumber() == 1)
			return false;
		// 세이프 체인지
		if (skill.getSkillLevel() == 10 && skill.getSkillNumber() == 7)
			return false;
		// 폴루트 워터
		if (skill.getSkillLevel() == 22 && skill.getSkillNumber() == 4)
			return false;
		// 스트라이커 게일
		if (skill.getSkillLevel() == 22 && skill.getSkillNumber() == 5)
			return false;
		// 에어리어 오브 사일런스
		if (skill.getSkillLevel() == 21 && skill.getSkillNumber() == 0)
			return false;

		return true;
	}

	/**
	 * 카운터매직 마법이 시전중일경우 공격당하면 해당 함수가 호출되며</br>
	 * 공격당한 마법이 카운터매직에 영향을 받는지 아닌지 확인함.
	 * 
	 * @param skill : 확인할 마법
	 * @return : true(영향받음), false(영향안받음)
	 */
	static private boolean isCounterMagic(Skill skill) {
		// 쇼크스턴
		if (skill.getSkillLevel() == 11 && skill.getSkillNumber() == 6)
			return false;
		// 쇼크스턴
		// if(skill.getSkillLevel()==4 && skill.getSkillNumber()==5)
		// return false;
		// 캔슬레이션
		if (skill.getSkillLevel() == 6 && skill.getSkillNumber() == 3)
			return false;

		if (skill.getSkillLevel() == 8 && skill.getSkillNumber() == 1)
			return false;
		//
		return true;
	}

	/**
	 * 기사 기술서 마법 확인
	 */
	static private boolean isKnight(Skill skill) {
		if (skill.getSkillLevel() == 11 && skill.getSkillNumber() == 6)
			return true;
		if (skill.getSkillLevel() == 11 && skill.getSkillNumber() == 7)
			return true;
		if (skill.getSkillLevel() == 12 && skill.getSkillNumber() == 0)
			return true;
		if (skill.getSkillLevel() == 12 && skill.getSkillNumber() == 1)
			return true;
		if (skill.getSkillLevel() == 12 && skill.getSkillNumber() == 2)
			return true;

		return false;
	}

	/**
	 * 힐 계열 마법인지 확인.
	 * 
	 * @param skill
	 * @return
	 */
	static private boolean isHeal(Skill skill) {
		// 힐
		if (skill.getSkillLevel() == 1 && skill.getSkillNumber() == 0)
			return true;
		// 익스트라 힐
		if (skill.getSkillLevel() == 3 && skill.getSkillNumber() == 2)
			return true;
		// 그레이터 힐
		if (skill.getSkillLevel() == 5 && skill.getSkillNumber() == 2)
			return true;
		// 힐 올
		if (skill.getSkillLevel() == 7 && skill.getSkillNumber() == 0)
			return true;
		// 풀 힐
		if (skill.getSkillLevel() == 8 && skill.getSkillNumber() == 0)
			return true;
		// 네이처스 터치
		if (skill.getSkillLevel() == 20 && skill.getSkillNumber() == 5)
			return true;
		// 네이쳐스블레싱
		if (skill.getSkillLevel() == 21 && skill.getSkillNumber() == 3)
			return true;
		return false;
	}

	/**
	 * 공격형 마법인지 확인.
	 */
	static private boolean isAttackMagic(Skill skill) {
		// 에너지 볼트
		if (skill.getSkillLevel() == 1 && skill.getSkillNumber() == 3)
			return true;
		// 아이스 대거
		if (skill.getSkillLevel() == 1 && skill.getSkillNumber() == 5)
			return true;
		// 윈드 커터
		if (skill.getSkillLevel() == 1 && skill.getSkillNumber() == 6)
			return true;
		// 칠 터치
		if (skill.getSkillLevel() == 2 && skill.getSkillNumber() == 1)
			return true;
		// 파이어 애로우
		if (skill.getSkillLevel() == 2 && skill.getSkillNumber() == 6)
			return true;
		// 스탈락
		if (skill.getSkillLevel() == 2 && skill.getSkillNumber() == 7)
			return true;
		// 라이트닝
		if (skill.getSkillLevel() == 3 && skill.getSkillNumber() == 0)
			return true;
		// 턴 언데드
		if (skill.getSkillLevel() == 3 && skill.getSkillNumber() == 1)
			return true;
		// 프로즌 클라우드
		if (skill.getSkillLevel() == 3 && skill.getSkillNumber() == 5)
			return true;
		// 파이어 볼
		if (skill.getSkillLevel() == 4 && skill.getSkillNumber() == 0)
			return true;
		// 뱀파이어릭 터치
		if (skill.getSkillLevel() == 4 && skill.getSkillNumber() == 3)
			return true;
		// 어스 재일
		if (skill.getSkillLevel() == 4 && skill.getSkillNumber() == 5)
			return true;
		// 콜 라이트닝
		if (skill.getSkillLevel() == 5 && skill.getSkillNumber() == 1)
			return true;
		// 콘 오브 콜드
		if (skill.getSkillLevel() == 5 && skill.getSkillNumber() == 5)
			return true;
		// 이럽션
		if (skill.getSkillLevel() == 6 && skill.getSkillNumber() == 4)
			return true;
		// 선 버스트
		if (skill.getSkillLevel() == 6 && skill.getSkillNumber() == 5)
			return true;
		// 아이스 랜스
		if (skill.getSkillLevel() == 7 && skill.getSkillNumber() == 1)
			return true;
		// 토네이도
		if (skill.getSkillLevel() == 7 && skill.getSkillNumber() == 4)
			return true;
		// 파이어 월
		if (skill.getSkillLevel() == 8 && skill.getSkillNumber() == 1)
			return true;
		// 블리자드
		if (skill.getSkillLevel() == 8 && skill.getSkillNumber() == 2)
			return true;
		// 어스 퀘이크
		if (skill.getSkillLevel() == 8 && skill.getSkillNumber() == 5)
			return true;
		// 라이트닝 스톰
		if (skill.getSkillLevel() == 9 && skill.getSkillNumber() == 0)
			return true;
		// 스톰
		if (skill.getSkillLevel() == 9 && skill.getSkillNumber() == 5)
			return true;
		// 미티어 스트라이크
		if (skill.getSkillLevel() == 10 && skill.getSkillNumber() == 1)
			return true;
		// 디스인티 그레이트
		if (skill.getSkillLevel() == 10 && skill.getSkillNumber() == 4)
			return true;
		// 프리징 블리자드
		if (skill.getSkillLevel() == 10 && skill.getSkillNumber() == 7)
			return true;
		// 파이널 번
		if (skill.getSkillLevel() == 14 && skill.getSkillNumber() == 3)
			return true;
		return false;
	}

	/**
	 * 범위 공격형 마법인지 확인.
	 */
	static private boolean isAttackRangeMagic(Skill skill) {
		// 라이트닝
		if (skill.getSkillLevel() == 3 && skill.getSkillNumber() == 0)
			return true;
		// 프로즌 클라우드
		if (skill.getSkillLevel() == 3 && skill.getSkillNumber() == 5)
			return true;
		// 파이어 볼
		if (skill.getSkillLevel() == 4 && skill.getSkillNumber() == 0)
			return true;
		// 토네이도
		if (skill.getSkillLevel() == 7 && skill.getSkillNumber() == 4)
			return true;
		// 블리자드
		if (skill.getSkillLevel() == 8 && skill.getSkillNumber() == 2)
			return true;
		// 어스 퀘이크
		if (skill.getSkillLevel() == 8 && skill.getSkillNumber() == 5)
			return true;
		// 라이트닝 스톰
		if (skill.getSkillLevel() == 9 && skill.getSkillNumber() == 0)
			return true;
		// 스톰
		if (skill.getSkillLevel() == 9 && skill.getSkillNumber() == 5)
			return true;
		// 미티어 스트라이크
		if (skill.getSkillLevel() == 10 && skill.getSkillNumber() == 1)
			return true;
		// 프리징 블리자드
		if (skill.getSkillLevel() == 10 && skill.getSkillNumber() == 7)
			return true;
		return false;
	}

	/**
	 * 확률 계산시 존을 체크해서 처리해야되는 마법인지 확인용 함수. : 보라돌이 처리할 마법인지 확인할때도 사용중.
	 */
	static private boolean isZoneCheckMagic(Skill skill) {
		// 커스:포이즌
		if (skill.getSkillLevel() == 2 && skill.getSkillNumber() == 2)
			return true;
		// 턴 언데드
		if (skill.getSkillLevel() == 3 && skill.getSkillNumber() == 1)
			return true;
		// 커스:블라인드
		if (skill.getSkillLevel() == 3 && skill.getSkillNumber() == 3)
			return true;
		// 웨폰브레이크
		if (skill.getSkillLevel() == 4 && skill.getSkillNumber() == 2)
			return true;
		// 슬로우
		if (skill.getSkillLevel() == 4 && skill.getSkillNumber() == 4)
			return true;
		// 커스:패럴라이즈
		if (skill.getSkillLevel() == 5 && skill.getSkillNumber() == 0)
			return true;
		// 마나드레인
		if (skill.getSkillLevel() == 5 && skill.getSkillNumber() == 6)
			return true;
		// 다크니스
		if (skill.getSkillLevel() == 5 && skill.getSkillNumber() == 7)
			return true;
		// 쇼크스턴
		// if(skill.getSkillLevel()==5 && skill.getSkillNumber()==7)
		// return true;
		// 풀힐
//		if (skill.getSkillLevel() == 8 && skill.getSkillNumber() == 0)
//			return true;
//		// 힐올
//		if (skill.getSkillLevel() == 7 && skill.getSkillNumber() == 0)
//			return true;
//		// 그레이터힐
//		if (skill.getSkillLevel() == 5 && skill.getSkillNumber() == 2)
//			return true;
		// 캔슬레이션
		if (skill.getSkillLevel() == 6 && skill.getSkillNumber() == 3)
			return true;
		// 위크니스
		if (skill.getSkillLevel() == 6 && skill.getSkillNumber() == 6)
			return true;
		// 아이스랜스
		if (skill.getSkillLevel() == 7 && skill.getSkillNumber() == 1)
			return true;
		// 디지즈
		if (skill.getSkillLevel() == 7 && skill.getSkillNumber() == 7)
			return true;
		// 사일런스
		if (skill.getSkillLevel() == 8 && skill.getSkillNumber() == 7)
			return true;
		// 포그오브슬리핑
		if (skill.getSkillLevel() == 9 && skill.getSkillNumber() == 1)
			return true;
		// 디케이포션
		if (skill.getSkillLevel() == 9 && skill.getSkillNumber() == 6)
			return true;
		// 리턴투네이처
		if (skill.getSkillLevel() == 19 && skill.getSkillNumber() == 0)
			return true;
		// 폴루트 워터
		if (skill.getSkillLevel() == 22 && skill.getSkillNumber() == 4)
			return true;
		// 스트라이커 게일
		if (skill.getSkillLevel() == 22 && skill.getSkillNumber() == 5)
			return true;
		// 쇼크스턴
		if (skill.getSkillLevel() == 11 && skill.getSkillNumber() == 6)
			return true;
		// 인탱글
		if (skill.getSkillLevel() == 19 && skill.getSkillNumber() == 7)
			return true;
		// 이레이즈매직
		if (skill.getSkillLevel() == 20 && skill.getSkillNumber() == 0)
			return true;
		// 어스 바인드
		if (skill.getSkillLevel() == 20 && skill.getSkillNumber() == 4)
			return true;
		// 에어리어 오브 사일런스
		if (skill.getSkillLevel() == 21 && skill.getSkillNumber() == 0)
			return true;
		return false;
	}

	/**
	 * 범위 공격형 마법시 데미지를 연산 해도되는 타켓인지 검색하는 함수.
	 * 
	 * @param cha : 공격자
	 * @param o   : 대상자
	 * @return
	 */
	static public boolean isMagicAttackRange(Character cha, object o) {
		// 마법인형 공격 안되게.
		if (o instanceof MagicDollInstance)
			return false;
		// 사용자 일경우
		if (cha instanceof PcInstance) {
			// 성존 부분 따로 구분.
			Kingdom k = KingdomController.findKingdomLocation(cha);
			if (k != null && k.isWar()) {
				// 성존에 공성중일때 같은 혈맹 소속들은 무시.
				if (cha.getClanId() > 0 && cha.getClanId() == o.getClanId())
					return false;
				return true;
			}
			return !(o instanceof PcInstance) && !(o instanceof SummonInstance) && !(o instanceof GuardInstance);
		}
		// 몬스터일 경우
		if (cha instanceof MonsterInstance)
			return o instanceof PcInstance || o instanceof SummonInstance;
		// 그외엔 걍 성공하기.
		return true;
	}

	/**
	 * 클레스별 순수스탯에따른 마법데미지 +@ 리턴
	 */
	static private int toOriginalStatMagicDamage(Character cha) {
		int sum = 0;
//		int _int = cha.getTotalInt();
		int _int = cha.getInt();
		switch (cha.getClassType()) { // 군기요마다
		case 0x00:
			_int -= 10;
			_int += _int / 8;
			break;
		case 0x01:
			_int -= 8;
			_int += _int / 4;
			break;
		case 0x02:
			_int -= 12;
			_int += _int / 3;
			break;
		case 0x03:
			_int -= 12;
			_int += _int / 2;
			break;
		case 0x04:
			_int -= 11;
			_int += _int / 4;
			break;
		}
		return sum;
	}

	/**
	 * _int 수치에 따른 마법 대미지 추출
	 * 
	 * @return
	 */
	static private int getIntDamage(Character cha) {
		int dmg = 0;
		int _int = cha.getTotalInt();
		switch (_int) {
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
			dmg = 0;
			break;
		case 12:
		case 13:
		case 14:
		case 15:
			dmg = 1;
			break;
		case 16:
		case 17:
		case 18:
		case 19:
			dmg = 2;
			break;
		case 20:
		case 21:
		case 22:
		case 23:
		case 24:
			dmg = 3;
			break;
		case 25:
		case 26:
		case 27:
		case 28:
		case 29:
			dmg = 4;
			break;
		case 30:
		case 31:
		case 32:
		case 33:
		case 34:
			dmg = 5;
			break;
		case 35:
		case 36:
		case 37:
		case 38:
		case 39:
			dmg = 7;
			break;
		case 40:
		case 41:
		case 42:
		case 43:
		case 44:
			dmg = 8;
			break;
		case 45:
		case 46:
		case 47:
		case 48:
		case 49:
			dmg = 10;
			break;
		case 50:
		case 51:
		case 52:
		case 53:
		case 54:
			dmg = 12;
			break;
		case 55:
		case 56:
		case 57:
		case 58:
		case 59:
			dmg = 14;
			break;
		case 60:
			dmg = 15;
			break;
		default:
			dmg = 16;
			break;

		}
		return dmg;
	}

	/**
	 * 스펠파워 리턴
	 */
	static public int getSp(Character cha, boolean packet) {
		Object o = PluginController.init(SkillController.class, "getSp", cha, packet);
		if (o != null && o instanceof Integer)
			return (Integer) o;

		int sp = cha.getTotalSp();

		switch (cha.getClassType()) {
		case 0x00:
			if (cha.getLevel() < 10)
				sp += 0;
			else if (cha.getLevel() < 20)
				sp += 1;
			else
				sp += 2;
			break;
		case 0x01:
			if (cha.getLevel() < 50)
				sp += 0;
			else
				sp += 1;
			break;
		case 0x02:
			if (cha.getLevel() < 8)
				sp += 0;
			else if (cha.getLevel() < 16)
				sp += 1;
			else if (cha.getLevel() < 24)
				sp += 2;
			else if (cha.getLevel() < 32)
				sp += 3;
			else if (cha.getLevel() < 40)
				sp += 4;
			else if (cha.getLevel() < 48)
				sp += 5;
			else
				sp += 6;
			break;
		case 0x03:
			if (cha.getLevel() < 4)
				sp += 0;
			else if (cha.getLevel() < 8)
				sp += 1;
			else if (cha.getLevel() < 12)
				sp += 2;
			else if (cha.getLevel() < 16)
				sp += 3;
			else if (cha.getLevel() < 20)
				sp += 4;
			else if (cha.getLevel() < 24)
				sp += 5;
			else if (cha.getLevel() < 28)
				sp += 6;
			else if (cha.getLevel() < 32)
				sp += 7;
			else if (cha.getLevel() < 36)
				sp += 8;
			else if (cha.getLevel() < 40)
				sp += 9;
			else if (cha.getLevel() < 44)
				sp += 10;
			else if (cha.getLevel() < 48)
				sp += 10;
			else if (cha.getLevel() < 50)
				sp += 11;
			else
				sp += 11;
			break;
		case 0x04:
			if (cha.getLevel() >= 12)
				sp += 1;
			if (cha.getLevel() >= 24)
				sp += 1;
			break;
		case 0x05:
			if (cha.getLevel() >= 20)
				sp += 1;
			if (cha.getLevel() >= 40)
				sp += 1;
			break;
		case 0x06:
			if (cha.getLevel() >= 6)
				sp += 1;
			if (cha.getLevel() >= 12)
				sp += 1;
			if (cha.getLevel() >= 18)
				sp += 1;
			if (cha.getLevel() >= 24)
				sp += 1;
			if (cha.getLevel() >= 30)
				sp += 1;
			if (cha.getLevel() >= 36)
				sp += 1;
			if (cha.getLevel() >= 42)
				sp += 1;
			if (cha.getLevel() >= 48)
				sp += 1;
			break;
		default:
			if (cha.getLevel() < 10)
				sp += 0;
			else if (cha.getLevel() < 20)
				sp += 1;
			else
				sp += 2;
			break;
		}
		switch (cha.getTotalInt()) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
			sp += -1;
			break;
		case 9:
		case 10:
		case 11:
			sp += 0;
			break;
		case 12:
		case 13:
		case 14:
			sp += 1;
			break;
		case 15:
		case 16:
		case 17:
			sp += 2;
			break;
		case 18:
			sp += 3;
			break;
		default:
			if (cha.getTotalInt() <= 24)
				sp += 3 + (cha.getTotalInt() - 18);
			else if (cha.getTotalInt() >= 25 && cha.getTotalInt() <= 35)
				sp += 10;
			else if (cha.getTotalInt() >= 36 && cha.getTotalInt() <= 42)
				sp += 11;
			else
				sp += 12;
			break;
		}
		return sp;
	}

	/**
	 * 마법방어력 리턴
	 * 
	 * @param cha
	 * @param packet : 패킷용과 일반 연산용 구분을 위해. : 패킷에선 클라자체 내부 공식과 리턴한 mr 이 함께 연산됨.
	 * @return
	 */
	static public int getMr(Character cha, boolean packet) {
		Object o = PluginController.init(SkillController.class, "getMr", cha, packet);
		if (o != null && o instanceof Integer)
			return (Integer) o;
		// 기본 mr 추출.
		int mr = cha.getDynamicMr() + cha.getSetitemMr();

		// 패킷 처리용 이 아니라면 연산용 이므로 실제 mr값 추출.
		if (!packet) {
			// 클레스별 보너스 mr 추출.성환
			switch (cha.getClassType()) {
			case 0x00:// 군
			case 0x04:// 다
				mr += 10;
				break;
			case 0x01:// 기
				mr += 0;
				break;
			case 0x02:// 요
				mr += 25;
				break;
			case 0x03:// 마
				mr += 15;
				break;
			}
			// 레벨 보너스 mr
			mr += cha.getLevel() / 2;
			// wis 에 따른 추가
			if (cha.getTotalWis() > 14) {
				switch (cha.getTotalWis()) {
				case 15:
				case 16:
					mr += 3;
					break;
				case 17:
					mr += 6;
					break;
				case 18:
					mr += 10;
					break;
				case 19:
					mr += 15;
					break;
				case 20:
					mr += 21;
					break;
				case 21:
					mr += 28;
					break;
				case 22:
					mr += 37;
					break;
				case 23:
					mr += 47;
					break;
				default:
					mr += 50;
					break;
				}
			}
			// 이레이즈매직
			if (cha.isBuffEraseMagic())
				mr /= 4;// 4
		}

		// 버그 방지.
		if (mr < 0)
			mr = 0;

		// 패킷 용이라면 최대 mr값 확인.
		// 리턴.
		if (packet) {
			int max_packet_mr = Lineage.server_version < 250 ? 100 : Lineage.max_mr;
			if (Lineage.max_mr < max_packet_mr)
				return mr > Lineage.max_mr ? Lineage.max_mr : mr;
			else
				return mr > max_packet_mr ? max_packet_mr : mr;
		} else {
			return mr > Lineage.max_mr ? Lineage.max_mr : mr;
		}
	}

	/**
	 * 스텟에 따른 MAGIC BONUS 값
	 * 
	 * @return
	 */
	static public int getMagicBonus(Character cha) {
		int i = cha.getTotalInt();
		if (i <= 4)
			return -1;
		else if (i <= 6)
			return 0;
		else if (i <= 8)
			return 1;
		else if (i <= 10)
			return 2;
		else if (i <= 12)
			return 3;
		else if (i <= 14)
			return 4;
		else if (i <= 16)
			return 5;
		else if (i <= 18)
			return 6;
		else if (i <= 20)
			return 7;
		else if (i <= 22)
			return 8;
		else if (i <= 24)
			return 9;
		else if (i <= 26)
			return 10;
		else if (i <= 28)
			return 11;
		else if (i <= 30)
			return 12;
		else if (i <= 32)
			return 13;
		else if (i <= 34)
			return 14;
		else if (i <= 36)
			return 15;
		else if (i <= 38)
			return 16;
		else if (i <= 40)
			return 17;
		else if (i <= 42)
			return 18;
		else if (i <= 44)
			return 19;
		else if (i <= 46)
			return 20;
		else if (i <= 48)
			return 21;
		else if (i <= 50)
			return 22;
		else
			return 23;
	}

	/**
	 * 클레스별 렙에 최대 마법레벨값 추출.
	 * 
	 * @param cha
	 * @return
	 */
	static public int getMagicLevel(Character cha) {
		if (cha instanceof PcInstance) {
			switch (cha.getClassType()) {
			case 0x00:
				return Math.min(2, cha.getLevel() / 10);
			case 0x01:
				return cha.getLevel() / 50;
			case 0x02:
				return Math.min(6, cha.getLevel() / 8);
			case 0x0A:
			case 0x0B:
			case 0x03:
				return Math.min(10, cha.getLevel() / 4);
			case 0x04:
				return Math.min(2, cha.getLevel() / 12);
			case 0x05:
				return Math.min(4, cha.getLevel() / 9);
			case 0x06:
				return Math.min(10, cha.getLevel() / 6);
			default:
				return Math.min(2, cha.getLevel() / 10);
			}
		}
		return cha.getLevel() / 4;
	}

	/**
	 * 클래스별 마법 성공률 기본값 리턴
	 * 
	 * @return
	 */
	static private int toMagicFigure(Character cha) {
		int sum = 0;
		switch (cha.getClassType()) {
		case 0x00:
			break;
		case 0x01:
			break;
		case 0x02:
			sum += 2;
			break;
		case 0x03:
			sum += 5;
		case 0x04:
			break;
		case 0x0A:
		case 0x0B:
			sum += 10;
			break;
		}
		return sum;
	}

	/**
	 * 버프 스킬인지 확인하는 함수
	 * 
	 * @param skill
	 * @return
	 */
	static private boolean BuffSkill(Skill skill) {
		int[] skillNumber = { 26, 37, 42, 43, 48, 55, 61, 68, 79 };
		for (int i = 0; i < skillNumber.length; i++) {
			if (skill.getUid() == skillNumber[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 클레스별 순수스탯에 따른 마법성공율 +@ 리턴
	 * 
	 * @return
	 */
	static private int toOriginalStatMagicHit(Character cha) {
		int sum = 0;
		int Int = cha.getTotalInt();
		switch (cha.getClassType()) {
		case 0x00:
			Int -= 10;
			if (Int >= 2)
				sum += 1;
			if (Int >= 4)
				sum += 1;
			if (Int >= 6)
				sum += 1;
			if (Int >= 8)
				sum += 1;
			if (Int >= 10)
				sum += 1;
			if (Int >= 12)
				sum += 1;
			if (Int >= 14)
				sum += 1;
			if (Int >= 16)
				sum += 1;
			if (Int >= 18)
				sum += 1;
			if (Int >= 20)
				sum += 1;
			if (Int >= 22)
				sum += 1;
			if (Int >= 24)
				sum += 1;
			if (Int >= 26)
				sum += 1;
			if (Int >= 28)
				sum += 1;
			if (Int >= 30)
				sum += 1;
			break;
		case 0x01:
			Int -= 8;
			if (Int >= 2)
				sum += 1;
			if (Int >= 4)
				sum += 1;
			if (Int >= 6)
				sum += 1;
			if (Int >= 8)
				sum += 1;
			if (Int >= 10)
				sum += 1;
			if (Int >= 12)
				sum += 1;
			if (Int >= 14)
				sum += 1;
			if (Int >= 16)
				sum += 1;
			if (Int >= 18)
				sum += 1;
			if (Int >= 20)
				sum += 1;
			if (Int >= 22)
				sum += 1;
			if (Int >= 24)
				sum += 1;
			if (Int >= 26)
				sum += 1;
			if (Int >= 28)
				sum += 1;
			if (Int >= 30)
				sum += 1;
			break;
		case 0x02:
			Int -= 12;
			if (Int >= 1)
				sum += 1;
			if (Int >= 3)
				sum += 1;
			if (Int >= 5)
				sum += 1;
			if (Int >= 7)
				sum += 1;
			if (Int >= 9)
				sum += 1;
			if (Int >= 11)
				sum += 1;
			if (Int >= 13)
				sum += 1;
			if (Int >= 15)
				sum += 1;
			if (Int >= 17)
				sum += 1;
			if (Int >= 19)
				sum += 1;
			if (Int >= 21)
				sum += 1;
			if (Int >= 23)
				sum += 1;
			break;
		case 0x03:
			Int -= 10;
			if (Int >= 2)
				sum += 2;
			if (Int >= 4)
				sum += 2;
			if (Int >= 6)
				sum += 2;
			if (Int >= 8)
				sum += 2;
			if (Int >= 10)
				sum += 2;
			if (Int >= 12)
				sum += 2;
			if (Int >= 14)
				sum += 2;
			if (Int >= 16)
				sum += 2;
			if (Int >= 18)
				sum += 2;
			if (Int >= 20)
				sum += 2;
			if (Int >= 22)
				sum += 2;
			if (Int >= 24)
				sum += 2;
			if (Int >= 26)
				sum += 2;
			if (Int >= 28)
				sum += 2;
			if (Int >= 30)
				sum += 2;
			if (Int >= 32)
				sum += 2;
			if (Int >= 34)
				sum += 2;
			if (Int >= 36)
				sum += 2;
			if (Int >= 37)
				sum += 2;
			if (Int >= 40)
				sum += 2;
			if (Int >= 42)
				sum += 2;
			if (Int >= 44)
				sum += 2;
			if (Int >= 46)
				sum += 2;
			if (Int >= 48)
				sum += 2;
			if (Int >= 50)
				sum += 2;
			break;

		case 0x04:
			Int -= 11;
			if (Int >= 1)
				sum += 1;
			if (Int >= 3)
				sum += 1;
			if (Int >= 5)
				sum += 1;
			if (Int >= 7)
				sum += 1;
			if (Int >= 9)
				sum += 1;
			if (Int >= 11)
				sum += 1;
			if (Int >= 13)
				sum += 1;
			if (Int >= 15)
				sum += 1;
			if (Int >= 17)
				sum += 1;
			if (Int >= 19)
				sum += 1;
			if (Int >= 21)
				sum += 1;
			if (Int >= 23)
				sum += 1;
			break;
		}
		return sum;
	}
	
	public static boolean isTurbernGfx(int gfx) {
		switch(gfx) {
		case 2412:
		case 6094:
		case 6080:
			return true;
		}
		return false;
	}
}