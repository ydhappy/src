package lineage.world.controller;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Boss;
import lineage.bean.database.Item;
import lineage.bean.database.Skill;
import lineage.bean.lineage.Clan;
import lineage.bean.lineage.Inventory;
import lineage.bean.lineage.Kingdom;
import lineage.database.CharactersDatabase;
import lineage.database.ItemDatabase;
import lineage.database.MagicdollListDatabase;
import lineage.database.MonsterBossSpawnlistDatabase;
import lineage.database.MonsterDatabase;
import lineage.database.ServerDatabase;
import lineage.database.SkillDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ClanWar;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_MessageGreen;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.Lineage_Balance;
import lineage.share.System;
import lineage.share.TimeLine;
import lineage.share.Common;
import lineage.util.Util;
import lineage.world.World;
import lineage.gui.GuiMain;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.BoardInstance;
import lineage.world.object.instance.GuardInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.MagicDollInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.NpcInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.PetInstance;
import lineage.world.object.instance.RobotInstance;
import lineage.world.object.instance.SummonInstance;
import lineage.world.object.item.weapon.Claw;
import lineage.world.object.item.weapon.Edoryu;
import lineage.world.object.magic.AbsoluteBarrier;
import lineage.world.object.magic.BurningSlash;
import lineage.world.object.magic.ChattingClosetwo;
import lineage.world.object.magic.CursePoison;
import lineage.world.object.magic.Detection;
import lineage.world.object.magic.EraseMagic;
import lineage.world.object.magic.FogOfSleeping;
import lineage.world.object.magic.Meditation;
import lineage.world.object.magic.UncannyDodge;
import lineage.world.object.npc.background.Cracker;
import lineage.world.object.npc.background.CrackerDmg;
import lineage.world.object.npc.background.PigeonGroup;
import lineage.world.object.npc.event.FireOfSoul;
import lineage.world.object.npc.guard.ElvenGuard;
import lineage.world.object.npc.SpotCrown;
import lineage.world.object.npc.kingdom.KingdomCrown;
import lineage.world.object.npc.kingdom.KingdomCastleTop;
import lineage.world.object.npc.kingdom.KingdomDoor;
import lineage.world.object.npc.kingdom.KingdomGuard;

public final class DamageController {
	public static boolean att = false;

	static public void init() {
		TimeLine.start("DamageController..");
		TimeLine.end();
	}

	/**
	 * 공격이 가해졋을때 공격성공여부를 처리하고 그후 이곳으로 오게 되는데 이부분에서는 데미지공식을 연산해도 되는지 여부를 판단하는
	 * 메서드. 예로 활일경우 화살이 없다면 false를 리턴하며, 사이하활인데 화살이 없을경우 true를 리턴한다. -
	 * PcInstance쪽에서도 호출해서 사용중. 패킷중에 화살이 없더라도 사이하활은 이팩트를 그려줘야 하기때문에 해당 메서드를 활용중.
	 */
	static public boolean isAttack(boolean bow, ItemInstance weapon,
			ItemInstance arrow) {
		if (bow) {
			if (weapon != null) {
				switch (weapon.getItem().getNameIdNumber()) {
				case 1821: // 사이하활
					return true;
				default:
					return arrow != null;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * cha 로부터 o 가 데미지를 입었을때 뒷처리를 담당.
	 * 
	 * @param cha
	 *            : 가격자
	 * @param o
	 *            : 데미지 줄 타격자
	 * @param dmg
	 *            : 데미지
	 * @param type
	 *            : 공격 방식
	 */
	static public void toDamage(Character cha, object o, int dmg, int type) {
		// 칼렉
		if (cha != null && o != null && Lineage.is_sword_lack_check && SpriteFrameDatabase.findGfxMode(o.getGfx(), o.getGfxMode() + Lineage.GFX_MODE_DAMAGE))
			o.lastDamageActionTime = System.currentTimeMillis() + SpriteFrameDatabase.getGfxFrameTime(o, o.getGfx(), o.getGfxMode() + Lineage.GFX_MODE_DAMAGE);
		
		if (dmg <= 0 || cha == null || o == null || cha.isDead() || o.isDead() || o.isLockHigh() || o.isBuffAbsoluteBarrier())
			return;

		// 데미지 입었다는거 알리기.
		o.toDamage(cha, dmg, type);
		// hp 처리
		o.setNowHp(o.getNowHp() - dmg);

		// o.attackPc = 0;
		// 투망상태 해제
		Detection.onBuff(cha);
		Detection.onBuff(o);
		// 관련 버프 제거.
		if (o.isBuffMeditation())
			BuffController.remove(o, Meditation.class);
		if (o.isBuffFogOfSleeping())
			BuffController.remove(o, FogOfSleeping.class);
		if (type == 2) {
			if (o.isBuffEraseMagic())
				BuffController.remove(o, EraseMagic.class);
		}

		if (o.isDead()) {
			// 죽엇을경우.
			toDead(cha, o);
			// 죽은거 알리기.
			if (!(cha instanceof PcInstance && o instanceof PcInstance
					&& World.isBattleZone(cha.getX(), cha.getY(), cha.getMap()) && World
						.isBattleZone(o.getX(), o.getY(), o.getMap())))
				o.toDead(cha);

		} else {
			// 공격자 분류별로 처리
			if (cha instanceof PcInstance) {
				PcInstance pc = (PcInstance) cha;
				// 소환객체에게 알리기.
				SummonController.toDamage(pc, o, dmg);
				// 요정이라면 근처 경비에게 도움 요청.
				toElven(pc, o);
			}
		}
	}

	/**
	 * 기본적으로 공격 불가능한 객체 2020-07-29 by connector12@nate.com
	 */
	static public boolean 공격불가능한객체(object target) {
		if (target instanceof ItemInstance)
			return true;
		if (target instanceof BoardInstance)
			return true;
		if (!(target instanceof Cracker || target instanceof CrackerDmg || target instanceof PigeonGroup)
				&& target instanceof BackgroundInstance)
			return true;
		if (target instanceof ItemInstance)
			return true;
		if (target instanceof KingdomCrown || target instanceof SpotCrown)
			return true;

		return false;
	}

	/**
	 * 대미지 시스템 중심 부분.
	 */
	static public int getDamage(Character cha, object target, boolean bow,
			ItemInstance weapon, ItemInstance arrow, int alpha_dmg,
			boolean isTriple, int tripleIdx) {
		double dmg = 0;
		double countdmg = 0;

		if (Lineage.open_delay) {
			ChattingController.toChatting(cha, "오픈대기중에는 공격이 불가능합니다");
			return 0;
		}

		// 앱솔상태 해제
		if (cha.isBuffAbsoluteBarrier())
			BuffController.remove(cha, AbsoluteBarrier.class);

		if (target == null)
			return 0;

		if (공격불가능한객체(target))
			return 0;

		// 기본적으로 공격 불가능한 객체
		if (target instanceof ItemInstance
				|| target instanceof BoardInstance
				|| (!(target instanceof Cracker || target instanceof CrackerDmg
						|| target instanceof PigeonGroup || target instanceof FireOfSoul) && target instanceof BackgroundInstance))
			return 0;

		// 굳은상태라면 패스
		if (target.isLockHigh())
			return 0;

		// 앱솔 상태라면 패스
		if (target.isBuffAbsoluteBarrier())
			return 0;

		// 타켓이 죽은상태라면 패스
		if (target.isDead()) {
			// 죽은객체에게도 알려야함.
			if (target instanceof KingdomCastleTop)
				target.toDamage(cha, 0, 0);
			return 0;
		}

		// 자기자신을 공격할 순 없음.
		if (cha.getObjectId() == target.getObjectId())
			return 0;

		// 장애물 방해하는지 확인.
		if (!Util.isAreaAttack(cha, target))
			return 0;

		// 내성문이라면 공성중일때만 가능.
		if (target instanceof KingdomDoor) {
			KingdomDoor kd = (KingdomDoor) target;
			if (kd.getKingdom() == null || kd.getNpc() == null)
				return 0;
			if (!kd.getKingdom().isWar()
					&& kd.getNpc().getName().indexOf("내성문") > 0)
				return 0;
		}
		//
		if (!target.isAttack(cha, false))
			return 0;

		Object temp = PluginController.init(DamageController.class,
				"getDamage", cha, target, bow, weapon, arrow);
		if (temp != null && temp instanceof Double)
			dmg += (Double) temp;

		if (cha instanceof PcInstance) {
			try {
				dmg += toPcInstance((PcInstance) cha, target, bow, weapon,
						arrow, isTriple, tripleIdx);
			} catch (Exception e) {
				// System.out.println(
				// "DamageController.toPcInstance((PcInstance)cha, target, bow, weapon, arrow, isTriple, tripleIdx) : "
				// + e);
			}

			// 1월 29일 추가 부분

		} else if (cha instanceof PetInstance) {
			dmg += toPetInstance((PetInstance) cha, target, bow);

		} else if (cha instanceof SummonInstance) {
			dmg += toSummonInstance((SummonInstance) cha, target, bow);

		} else if (cha instanceof MonsterInstance) {
			dmg += toMonsterInstance((MonsterInstance) cha, target, bow);

		} else if (cha instanceof GuardInstance) {
			dmg += toGuardInstance((GuardInstance) cha, target, bow);
		} else {
			dmg += toInstance(cha, target, bow);
		}
		if (dmg <= 0) {
			return 0;
		} else {
			if (alpha_dmg > 0)
				dmg += Util.random(0, alpha_dmg);
			
			if (cha instanceof PcInstance && target instanceof PcInstance) {
				PcInstance use = (PcInstance) target;
				// PvP 대미지 리덕션
				dmg -= use.getDynamicAddPvpReduction();
			}
			// 버닝스피릿 1.5배 (1.3배로 변경)
			if (cha.isBuffBurningSpirit() && Util.random(0, 99) < 32)
				dmg = dmg * 1.5;
			// 엘리멘탈 파이어 1.4배
			if (!bow && cha.isBuffElementalFire() && Util.random(0, 99) < 35)
				dmg += dmg * 0.4;
			// 위크니스 상태라면 데미지 6 감소
			if (target.isBuffWeakness())
				dmg -= 6;
			// 드래곤스킨 상태일경우 데미지 5감소 효과.
			if (target.isBuffDragonSkin())
				dmg -= 5;
			//

			// 20181127 감소율 삭제.
			// if(cha instanceof PcInstance && target instanceof PcInstance){

			// }else if(target instanceof Character) {
			if (target instanceof Character) {
				Character c = (Character) target;
				// 방어력에 따른 데미지 감소.
				if (c.getClassType() == Lineage.LINEAGE_CLASS_ROYAL && c.getTotalAc() >= 100)
					dmg -= Util.random(c.getTotalAc() * 0.12, c.getTotalAc() * 0.22);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ROYAL && c.getTotalAc() >= 90)
					dmg -= Util.random(c.getTotalAc() * 0.11, c.getTotalAc() * 0.21);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ROYAL && c.getTotalAc() >= 80)
					dmg -= Util.random(c.getTotalAc() * 0.11, c.getTotalAc() * 0.20);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ROYAL && c.getTotalAc() >= 70)
					dmg -= Util.random(c.getTotalAc() * 0.11, c.getTotalAc() * 0.19);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ROYAL && c.getTotalAc() >= 60)
					dmg -= Util.random(c.getTotalAc() * 0.1, c.getTotalAc() * 0.18);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ROYAL && c.getTotalAc() >= 55)
					dmg -= Util.random(c.getTotalAc() * 0.09, c.getTotalAc() * 0.17);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ROYAL && c.getTotalAc() >= 50)
					dmg -= Util.random(c.getTotalAc() * 0.09, c.getTotalAc() * 0.16);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ROYAL && c.getTotalAc() >= 45)
					dmg -= Util.random(c.getTotalAc() * 0.08, c.getTotalAc() * 0.15);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ROYAL && c.getTotalAc() >= 40)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.14);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ROYAL && c.getTotalAc() >= 30)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.13);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ROYAL && c.getTotalAc() >= 20)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.12);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ROYAL && c.getTotalAc() >= 10)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.11);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ROYAL && c.getTotalAc() >= 0)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.1);

				else if (c.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT && c.getTotalAc() >= 100)
					dmg -= Util.random(c.getTotalAc() * 0.14, c.getTotalAc() * 0.22);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT && c.getTotalAc() >= 90)
					dmg -= Util.random(c.getTotalAc() * 0.13, c.getTotalAc() * 0.21);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT && c.getTotalAc() >= 80)
					dmg -= Util.random(c.getTotalAc() * 0.12, c.getTotalAc() * 0.2);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT && c.getTotalAc() >= 70)
					dmg -= Util.random(c.getTotalAc() * 0.12, c.getTotalAc() * 0.19);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT && c.getTotalAc() >= 60)
					dmg -= Util.random(c.getTotalAc() * 0.12, c.getTotalAc() * 0.18);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT && c.getTotalAc() >= 55)
					dmg -= Util.random(c.getTotalAc() * 0.11, c.getTotalAc() * 0.17);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT && c.getTotalAc() >= 50)
					dmg -= Util.random(c.getTotalAc() * 0.11, c.getTotalAc() * 0.16);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT && c.getTotalAc() >= 45)
					dmg -= Util.random(c.getTotalAc() * 0.1, c.getTotalAc() * 0.15);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT && c.getTotalAc() >= 40)
					dmg -= Util.random(c.getTotalAc() * 0.1, c.getTotalAc() * 0.14);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT && c.getTotalAc() >= 30)
					dmg -= Util.random(c.getTotalAc() * 0.09, c.getTotalAc() * 0.13);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT && c.getTotalAc() >= 20)
					dmg -= Util.random(c.getTotalAc() * 0.09, c.getTotalAc() * 0.12);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT && c.getTotalAc() >= 0)
					dmg -= Util.random(c.getTotalAc() * 0.09, c.getTotalAc() * 0.1);

				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ELF && c.getTotalAc() >= 100)
					dmg -= Util.random(c.getTotalAc() * 0.12, c.getTotalAc() * 0.22);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ELF && c.getTotalAc() >= 90)
					dmg -= Util.random(c.getTotalAc() * 0.11, c.getTotalAc() * 0.21);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ELF && c.getTotalAc() >= 80)
					dmg -= Util.random(c.getTotalAc() * 0.1, c.getTotalAc() * 0.2);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ELF && c.getTotalAc() >= 70)
					dmg -= Util.random(c.getTotalAc() * 0.09, c.getTotalAc() * 0.19);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ELF && c.getTotalAc() >= 60)
					dmg -= Util.random(c.getTotalAc() * 0.08, c.getTotalAc() * 0.18);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ELF && c.getTotalAc() >= 55)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.17);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ELF && c.getTotalAc() >= 50)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.16);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ELF && c.getTotalAc() >= 45)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.15);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ELF && c.getTotalAc() >= 40)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.14);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ELF && c.getTotalAc() >= 30)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.13);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ELF && c.getTotalAc() >= 20)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.12);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ELF && c.getTotalAc() >= 10)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.11);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_ELF && c.getTotalAc() >= 0)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.1);

				else if (c.getClassType() == Lineage.LINEAGE_CLASS_WIZARD && c.getTotalAc() >= 100)
					dmg -= Util.random(c.getTotalAc() * 0.12, c.getTotalAc() * 0.22);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_WIZARD && c.getTotalAc() >= 90)
					dmg -= Util.random(c.getTotalAc() * 0.11, c.getTotalAc() * 0.21);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_WIZARD && c.getTotalAc() >= 80)
					dmg -= Util.random(c.getTotalAc() * 0.1, c.getTotalAc() * 0.2);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_WIZARD && c.getTotalAc() >= 70)
					dmg -= Util.random(c.getTotalAc() * 0.08, c.getTotalAc() * 0.19);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_WIZARD && c.getTotalAc() >= 60)
					dmg -= Util.random(c.getTotalAc() * 0.08, c.getTotalAc() * 0.18);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_WIZARD && c.getTotalAc() >= 55)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.17);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_WIZARD && c.getTotalAc() >= 50)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.16);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_WIZARD && c.getTotalAc() >= 45)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.15);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_WIZARD && c.getTotalAc() >= 40)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.14);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_WIZARD && c.getTotalAc() >= 30)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.13);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_WIZARD && c.getTotalAc() >= 20)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.12);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_WIZARD && c.getTotalAc() >= 10)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.11);
				else if (c.getClassType() == Lineage.LINEAGE_CLASS_WIZARD && c.getTotalAc() >= 0)
					dmg -= Util.random(c.getTotalAc() * 0.07, c.getTotalAc() * 0.1);
				else
					dmg -= Util.random(c.getTotalAc() * 0.01, c.getTotalAc() * 0.01);
			}

			if (target instanceof PcInstance) {
				if (cha.getInventory() != null) {

					ItemInstance chaWeapon = cha.getInventory().getSlot(
							Lineage.SLOT_WEAPON);
					ItemInstance targetArmor = target.getInventory().getSlot(
							Lineage.SLOT_ARMOR);
					ItemInstance targetShiled = target.getInventory().getSlot(
							Lineage.SLOT_SHIELD);
					if (!bow
							&& chaWeapon != null
							&& targetArmor != null
							&& chaWeapon.getItem().getName().equalsIgnoreCase("벡드코빈")) {

						dmg += ((chaWeapon.getItem().getDmgMax() + chaWeapon
								.getEnLevel()) * Lineage.weapon_bect_damage);

					}
					if (targetShiled != null && targetShiled.getItem().getName().equalsIgnoreCase("반역자의 방패")
							&& Util.random(1, 100) <= Lineage.banshiled + targetShiled.getEnLevel()) {
						target.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), target, 13418), target instanceof PcInstance);
						dmg -= 50;
					}
				}
			}
			// 리덕션 효과 처리.
			dmg = toReduction(cha, target, dmg);
			//
			if (target instanceof MonsterInstance) {
				MonsterInstance mi = (MonsterInstance) target;
				// 언데드속성이 -50보다 낮을경우 밤일때 ac효과 더 주기.
				if (mi.getMonster().getResistanceUndead() <= -50) {
					if (ServerDatabase.isNight())
						dmg -= dmg * 0.2;
				}
			}
			/*
			 * if(bow==false && target.isBuffCounterBarrier() && Util.random(0,
			 * 99)<20) { Skill skill = SkillDatabase.find(31,1); if(dmg > 0){ //
			 * 데미지에 20%만 적용하기. //toDamage((Character)target, cha,
			 * (int)(dmg*0.2), 0); countdmg = (weapon.getItem().getDmgMax() +
			 * weapon.getItem().getAddDmg() +weapon.getEnLevel()) * 2;
			 * toDamage((Character)target, cha, (int)(countdmg),0); }
			 * cha.toSender
			 * (S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.
			 * class), cha, skill.getCastGfx()), true); return 0; }
			 */
			// 카운터배리어 상태라면 일정확률로 데미지 반사.
			temp = PluginController.init(DamageController.class,
					"CounterBarrier", cha, target, bow, weapon, arrow, dmg);
			if (temp == null) {
				// if(bow==false && target.isBuffCounterBarrier() &&
				// Util.random(0, cha.getLevel())<Util.random(0,
				// target.getLevel())) {
				if (bow == false && dmg > 0 && target.isBuffCounterBarrier()
						&& target.getInventory() != null) {
					ItemInstance target_weapon = target.getInventory().getSlot(
							Lineage.SLOT_WEAPON);
					if ((target_weapon != null || cha instanceof MonsterInstance)
							&& Util.random(0, 99) < 20) {
						if (target_weapon != null) {
							/*
							 * if (target instanceof MonsterInstance) //
							 * (큰몹+인첸+추타) * 1.5 dmg =
							 * (target_weapon.getItem().getDmgMax() +
							 * target_weapon.getEnLevel() +
							 * target_weapon.getItem().getAddDmg()) * 1.5; else
							 */
							// (큰몹+인첸+추타) * 2
							dmg = (target_weapon.getItem().getDmgMax()
									+ target_weapon.getEnLevel() + target_weapon
									.getItem().getAddDmg()) * 2;
						} else {
							// 데미지에 50%만 적용하기.
							dmg = dmg * 0.5;
						}

						target.toSender(
								S_ObjectEffect.clone(BasePacketPooling
										.getPool(S_ObjectEffect.class), target,
										10710), true);
						toDamage((Character) target, cha, (int) dmg,
								Lineage.ATTACK_TYPE_WEAPON);
						dmg = 0;
					}
				}
			}
			// 트리플 일경우 데미지 95%만 반영.
			if (isTriple) { // 20180409 트리플 수정
				if (target instanceof PcInstance)
					dmg = (int) (dmg * 0.7);
				else
					dmg = (int) (dmg * 0.9);
				// 트리플인데 상대가 솔리드 캐리지 상태일경우 +@로 80% 낮추기.
				// if (target.isBuffSolidCarriage())
				// dmg = (int) (dmg * 0.8);
			}

			// 이뮨투함 일땐 데미지 50% 만.
			// : 카운터배리어 처리 후 이문투함 처리를 해야 함. 카배에서는 이문효과가 적용되지 않은 상태에서 리턴하기 때문.
			if (target.isBuffImmuneToHarm()) {
				if (target instanceof PcInstance && cha instanceof PcInstance) {
					if (weapon != null
							&& weapon.getItem().getSafeEnchant() == 0
							&& weapon.getEnLevel() >= 6) {
						if (Util.random(0, 99) < ((weapon.getEnLevel() - 6) * 10)
								+ weapon.getItem().getHideAddDmg())
							dmg = dmg <= 0 ? 0 : dmg * (Util.random(0.8, 1));
						else
							dmg = dmg <= 0 ? 0 : dmg * Lineage.imm_dmg;
					} else if (weapon != null && weapon.getEnLevel() >= 9) {
						if (Util.random(0, 99) < ((weapon.getEnLevel() - 9) * 10)
								+ weapon.getItem().getHideAddDmg())
							dmg = dmg <= 0 ? 0 : dmg * (Util.random(0.8, 1));
						else
							dmg = dmg <= 0 ? 0 : dmg * Lineage.imm_dmg;
					} else {
						dmg = dmg <= 0 ? 0 : dmg * Lineage.imm_dmg;
					}
				} else {
					dmg = dmg <= 0 ? 0 : dmg * Lineage.imm_dmg; // 그외 클레스는
																// 50%데미지 낮춤.
				}

			}

			// 자동 스턴 과 자동 트리플

			if (cha instanceof PcInstance && target instanceof PcInstance
					&& cha.getClassType() == Lineage.LINEAGE_CLASS_ROYAL) {
				dmg = dmg * Lineage_Balance.pvp_dmg_royal;

			}
			if (cha instanceof PcInstance && target instanceof PcInstance
					&& cha.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT) {
				dmg = dmg * Lineage_Balance.pvp_dmg_knight;

			}
			if (cha instanceof PcInstance && target instanceof PcInstance
					&& cha.getClassType() == Lineage.LINEAGE_CLASS_ELF) {
				dmg = dmg * Lineage_Balance.pvp_dmg_elf;

			}
			if (cha instanceof PcInstance && target instanceof PcInstance
					&& cha.getClassType() == Lineage.LINEAGE_CLASS_WIZARD) {
				dmg = dmg * Lineage_Balance.pvp_dmg_wizard;

			}
			if (cha instanceof MonsterInstance && target instanceof PcInstance) {
				dmg = dmg * Lineage_Balance.pvp_dmg_mon;

			}
			if (cha instanceof PcInstance
					&& target instanceof PcInstance
					&& cha.getClassType() == Lineage.LINEAGE_CLASS_ELF
					&& (target.getClassType() == Lineage.LINEAGE_CLASS_ROYAL || target
							.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT)) {
				dmg = dmg * Lineage_Balance.pvp_dmg_elf_near;

			}

			if (cha instanceof PcInstance && target instanceof PcInstance
					&& cha.getClassType() == Lineage.LINEAGE_CLASS_ELF
					&& (target.getClassType() == Lineage.LINEAGE_CLASS_WIZARD)) {
				dmg = dmg * Lineage_Balance.pvp_dmg_elf_near_wizard;

			}
			if (cha instanceof PcInstance && target instanceof PcInstance
					&& cha.getClassType() == Lineage.LINEAGE_CLASS_ROYAL
					&& (target.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT)) {
				dmg = dmg * 1;

			}
			if (cha instanceof PcInstance && target instanceof MonsterInstance
					&& cha.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT) {
				dmg = dmg * Lineage_Balance.mon_dmg_knight;

			}
			if (cha instanceof PcInstance && target instanceof MonsterInstance
					&& cha.getClassType() == Lineage.LINEAGE_CLASS_ELF) {
				dmg = dmg * Lineage_Balance.mon_dmg_elf;

			}
			if (cha instanceof PcInstance && target instanceof MonsterInstance
					&& cha.getClassType() == Lineage.LINEAGE_CLASS_WIZARD) {
				dmg = dmg * Lineage_Balance.mon_dmg_wizard;

			}
			if (cha instanceof PcInstance && target instanceof MonsterInstance
					&& cha.getClassType() == Lineage.LINEAGE_CLASS_ROYAL) {
				dmg = dmg * Lineage_Balance.mon_dmg_royal;

			}
			if (weapon == null) {
				dmg += 1;
			}
			if (cha.isDmgCheck && cha instanceof PcInstance) {
				for (PcInstance pc : World.getPcList()) {
					if (pc.getGm() > 0) {
						if (target instanceof PcInstance) {
							String msg = String.format("\\fR[대미지 확인] %s -> %s (대미지: %d)", cha.getName(), target.getName(), (int) Math.round(dmg));
							ChattingController.toChatting(pc, msg, Lineage.CHATTING_MODE_MESSAGE);
							
							if (!Common.system_config_console) {
								long time = System.currentTimeMillis();
								String timeString = Util.getLocaleString(time, true);
								
								msg = String.format("[대미지 확인] %s -> %s (대미지: %d)", cha.getName(), target.getName(), (int) Math.round(dmg));
								String log = String.format("[%s]\t %s", timeString, msg);
								
								GuiMain.display.asyncExec(new Runnable() {
									public void run() {
										GuiMain.getViewComposite().getDamageCheckComposite().toLog(log);
									}
								});
							}
						} else if (target instanceof MonsterInstance) {
							MonsterInstance monster = (MonsterInstance) target;
							String msg = String.format("\\fR[대미지 확인] %s -> %s (대미지: %d)", cha.getName(), monster.getMonster().getName(), (int) Math.round(dmg));
							ChattingController.toChatting(pc, msg, Lineage.CHATTING_MODE_MESSAGE);
							
							if (!Common.system_config_console) {
								long time = System.currentTimeMillis();
								String timeString = Util.getLocaleString(time, true);
								
								msg = String.format("[대미지 확인] %s -> %s (대미지: %d)", cha.getName(), monster.getMonster().getName(), (int) Math.round(dmg));
								String log = String.format("[%s]\t %s", timeString, msg);
								
								GuiMain.display.asyncExec(new Runnable() {
									public void run() {
										GuiMain.getViewComposite().getDamageCheckComposite().toLog(log);
									}
								});
							}
						}
					}
				}
			}

			// 최종 대미지 반올림
			return dmg > 0 ? (int) Math.round(dmg) : 0;
		}
	}

	/**
	 * 리덕션 효과 처리 메서드.
	 * 
	 * @param cha
	 * @param target
	 * @param dmg
	 * @return
	 */
	static private double toReduction(Character cha, object target, double dmg) {
		//
		int reduction = 0;
		//
		Object o = PluginController.init(DamageController.class, "toReduction",
				cha, target, dmg);
		if (o != null)
			reduction += (Integer) o;
		// 리덕션아머 데미지 레벨에 따른 데미지 감소.
//		if (target.isBuffReductionArmor() && target.getLevel() >= 50) {
			// 50레벨 기준으로 4에 배수만큼 데미지 -1씩 처리.
//			int val = (target.getLevel() - 50) / 4;
//			if (reduction < val)
//				reduction = val;
//		}
		if (target instanceof PcInstance) {
			PcInstance t = (PcInstance) target;
			if (t.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT
					|| t.getClassType() == Lineage.LINEAGE_CLASS_ROYAL
					|| t.getClassType() == Lineage.LINEAGE_CLASS_ELF) {

				if (t.getLevel() == 48) {
					reduction += 1;
				}

				if (t.getLevel() == 50) {
					reduction += 1;
				}
			}
			if (t.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT
					|| t.getClassType() == Lineage.LINEAGE_CLASS_ROYAL
					|| t.getClassType() == Lineage.LINEAGE_CLASS_ELF
					|| t.getClassType() == Lineage.LINEAGE_CLASS_WIZARD) {
				if (t.getLevel() == 51) {
					reduction += 1;
				}

				if (t.getLevel() == 53) {
					reduction += 1;

				}
				if (t.getLevel() == 55) {
					reduction += 1;

				}
			}
//			if (t.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT || t.getClassType() == Lineage.LINEAGE_CLASS_ROYAL) {
//				if (t.getLevel() == 45) {
//					reduction += 1;
//				}
//			}
		}
		//
		if (target instanceof Character) {
			Character c = (Character) target;
			reduction += c.getTotalReduction();
			if (c.getInventory() != null) {
				// 룸티스 붉은빛 귀걸이 리덕션 발동.
				ItemInstance item = c.getInventory().getSlot(
						Lineage.SLOT_EARRING);
				ItemInstance item2 = c.getInventory().getSlot(
						Lineage.SLOT_EARRING2);
				if (item != null) {
					reduction += item.toDamageReduction(cha, target);
				}
				if (item2 != null) {
					reduction += item2.toDamageReduction(cha, target);
				}
				// 반역자의 방패 리덕션 발동.
				item = c.getInventory().getSlot(Lineage.SLOT_SHIELD);
				if (item != null)
					reduction += item.toDamageReduction(cha, target);
			}
		}
		//
		return dmg - Util.random(0, reduction);
	}

	/**
	 * 사용자 데미지 추출 함수.
	 * 
	 * @param pc
	 * @param target
	 * @param bow
	 * @param weapon
	 * @param arrow
	 * @return
	 */
	static private double toPcInstance(PcInstance pc, object target, boolean bow, ItemInstance weapon, ItemInstance arrow, boolean isTriple, int tripleIdx) {
		Object o = PluginController.init(DamageController.class,
				"toPcInstance", pc, target, bow, weapon, arrow, isTriple,
				tripleIdx);
		if (o != null)
			return (Double) o;

		boolean Small = true;
		double dmg = 0;
		double basedmg = 0;
		double buffdmg = 0;

		if (World.isAttack(pc, target)) {
			// 장로 변신상태라면 데미지 일정한거 주기.
			if (pc.getGfx() == 32)
				return Util.random(5, 15);

			// 데미지 연산
			if (isAttack(bow, weapon, arrow)
					&& isHitFigure(pc, target, bow, weapon, isTriple, tripleIdx)) {

				if (target instanceof MonsterInstance) {
					MonsterInstance mon = (MonsterInstance) target;
					Small = mon.getMonster().getSize().equalsIgnoreCase("small");
				}

				// 무기 크리티컬 대미지
				if (Lineage_Balance.is_critical && Util.random(0, 99) <= Lineage_Balance.weapon_critical_persent + Critical(pc, bow)) {
				    if (Small || target instanceof PcInstance) {
				        dmg = weapon.getItem().getDmgMin() + weapon.getAdd_Min_Dmg() + Lineage_Balance.critical_Min_Dmg;
				        if (Lineage.is_miss_effect) {
				        pc.setCriticalEffect(true); // Small일 경우에 치명타 효과 설정
				        }
				    } else {
				        dmg = weapon.getItem().getDmgMax() + weapon.getAdd_Max_Dmg() + Lineage_Balance.critical_Max_Dmg;
				        if (Lineage.is_miss_effect) {
				        pc.setCriticalEffect(true); // 그렇지 않은 경우에도 치명타 효과 설정
				    }
				    }
				}
				
				if (weapon == null) {
					// dmg += DmgFigure(pc, bow);
					if (dmg <= 0)
						dmg += Util.random(1, 3);
				} else {

					// 큰몹인지 작은몹인지 설정

					dmg += DmgWeaponFigure(pc, bow, weapon, arrow, Small) + 1; // 무기
																				// 토탈
																				// 대미지

					// 쉐도우팽은 더블 브레이크와 혼용 가능
					if (weapon.isBuffShadowFang())
						dmg += 5;
					// 더블 브레이크 2배 (1.6배로 변경)
					if (weapon != null
							&& (weapon instanceof Claw || weapon instanceof Edoryu)
							&& pc.isBuffDoubleBreak()
							&& Util.random(0, 99) < 35)
						dmg *= 2;

					basedmg += DmgFigure(pc, bow); // 베이스 스텟에 따른 추타 + 올리는 스텟에 의한
													// 추타 + 레벨에 의한 추타 9999
					buffdmg += DmgPlus(pc, weapon, target, bow); // 버프 상태에 따른
																	// 대미지 처리
					// dmg += DmgElement(weapon, target); // 정령
					dmg += PvpMon(pc, weapon, target); // 숨겨진 데미지 item 테이블에서 수정

					// dmg += DexDmg(pc, weapon, dmg);//데미지 명중률을 확인해서 다시 보내준다.

					if (target instanceof MonsterInstance) {
						MonsterInstance mon = (MonsterInstance) target;
						if (mon.getMonster().getResistanceUndead() < 0) {
							double undead_dmg = Util.random(5, 8);
							int material = bow && arrow != null ? arrow
									.getItem().getMaterial() : weapon.getItem()
									.getMaterial();

							int a = mon.getMonster().getResistanceUndead();// -25
							a = (~a) + 1;
							// 언덴드 몬스터일경우
							switch (material) {
							case 14: // 은
							case 17: // 미스릴
							case 22: // 오리하루콘
							case 18: // 블랙 미스릴
								dmg += (int) (undead_dmg * (a * 0.02));
								break;
							}
							// 언데드 추타 20190121
							if (weapon.getBress() == 0 && bow) {
								dmg += (int) (undead_dmg * (a * 0.01));
							} else if (weapon.getBress() == 0) {
								dmg += (int) (undead_dmg * (a * 0.02));
							}
						}
						// 손상을 시키는 몬스터일경우 장거리공격시 데미지 90%만 들어가도록 하기.
						if (mon.getMonster().isToughskin() && bow)
							dmg = (int) (dmg * 0.9);
					}

					// 무기 손상도 데미지 감소
					// : 퍼센트로 낮추는게 좋을듯.
					dmg -= weapon.getDurability() * 2.5;

					if (weapon.getItem().getSafeEnchant() == 6) {
						if (weapon.getEnLevel() > 6) {
							switch (weapon.getEnLevel()) {
							case 7:
								dmg += Lineage.enchant_safe6dmg7;
								break;
							case 8:
								dmg += Lineage.enchant_safe6dmg8;
								break;
							case 9:
								dmg += Lineage.enchant_safe6dmg9;
								break;
							case 10:
								dmg += Lineage.enchant_safe6dmg10;
								break;
							case 11:
								dmg += Lineage.enchant_safe6dmg11;
								break;
							}
						}
					} else if (weapon.getItem().getSafeEnchant() == 0) {
						if (weapon.getItem().getEnchantDmg() > 0) {
							switch (weapon.getEnLevel()) {
							case 1:
								dmg += Lineage.enchant_safe0dmg1;
								break;
							case 2:
								dmg += Lineage.enchant_safe0dmg2;
								break;
							case 3:
								dmg += Lineage.enchant_safe0dmg3;
								break;
							case 4:
								dmg += Lineage.enchant_safe0dmg4;
								break;
							case 5:
								dmg += Lineage.enchant_safe0dmg5;
								break;
							case 6:
								dmg += Lineage.enchant_safe0dmg6;
								break;
							case 7:
								dmg += Lineage.enchant_safe0dmg7;
								break;
							case 8:
								dmg += Lineage.enchant_safe0dmg8;
								break;
							case 9:
								dmg += Lineage.enchant_safe0dmg9;
								break;
							case 10:
								dmg += Lineage.enchant_safe0dmg10;
								break;
							}
						}
					}
					if (weapon.getItem().getName().equalsIgnoreCase("고대의 대검")) {
						dmg *= 0.8;
					}
					if (weapon.getItem().getName().equalsIgnoreCase("고대의 검")) {
						dmg *= 0.8;
					}
					if (weapon.getItem().getName().equalsIgnoreCase("대검")) {
						dmg *= 0.9;
					}

					if (basedmg <= 0) {
						basedmg = Util.random(1, 3);
					}

				}
			}
		}

		return dmg + buffdmg + basedmg;
	}

	private static int Critical(PcInstance pc, boolean bow) {
		int level = 0;

		// 레벨에 의한 크리티컬 확률
		if (pc.getLevel() >= 60) {
			level = pc.getLevel() / 20; // 1~4%
		}

		// 힘에 의한 크리티컬 확률
		if (bow) {
			if (pc.getTotalDex() > 39)
				level += 1;
		} else {
			if (pc.getTotalStr() > 39)
				level += 1;
		}
		return level;
	}

	static private double toSummonInstance(SummonInstance si, object target, boolean bow) {
		Object o = PluginController.init(DamageController.class, "toSummonInstance", si, target, bow);
		if (o != null)
			return (Double) o;

		double dmg = 0;
		// 데미지 연산
		if (World.isAttack(si, target)
				&& isHitFigure(si, target, bow, null, false, 0)) {
			// 대미지 산출
			dmg += DmgFigure(si, bow);

			if (si instanceof PetInstance)
				dmg += getPetLeveltoDamage(si);
			else
				dmg += getSummonLeveltoDamage(si);

			if (target instanceof MonsterInstance) {
				switch (target.getMonster().getName()) {
				case "발라카스":
				case "안타라스":
				case "파푸리온":
				case "테베 아누비스":
				case "데몬":
				case "에이션트 자이언트":
				case "테베 호루스":
				case "데스나이트":
				case "거대 여왕 개미":
				case "그레이트 미노타우르스":
				case "커츠":
				case "피닉스":
				case "얼음 여왕":
				case "좀비로드":
				case "공포의 뱀파이어":
				case "죽음의 좀비 로드":
				case "지옥의 쿠거":
				case "바포메트":
				case "베레스":
				case "제니스 퀸":
				case "정예 흑기사 대장":
				case "왜곡의 제니스 퀸":
				case "불신의 시어":
				case "[무한대전]데스나이트":
				case "나이트메어":
				case "흑장로":
				case "네크로맨서":
				case "아이스 데몬":
					dmg = dmg / 10;
					break;
				default:
				}

				dmg += si.getLevel() * 0.15;
			}
		}
		return dmg;
	}

	static private double toPetInstance(PetInstance si, object target,
			boolean bow) {
		Object o = PluginController.init(DamageController.class,
				"toPetInstance", si, target, bow);
		if (o != null)
			return (Double) o;

		double dmg = 0;
		// 데미지 연산
		if (World.isAttack(si, target)
				&& isHitFigure(si, target, bow, null, false, 0)) {
			//
			if (target instanceof PcInstance) {
				dmg += Util.random(1, 3);
			} else {
				// 대미지 산출
				dmg += DmgFigure(si, bow);
				dmg += DmgPlus(si, null, target, bow);
				// dmg += getMonsterLeveltoDamage(si);
				dmg += si.getLevel();

				//
				// if (Lineage.pet_damage_to_player && target instanceof
				// PcInstance)
				// dmg = dmg / 3;
				//

			}
		}

		if (target instanceof PcInstance)
			dmg = dmg / 8;
		else
			dmg = dmg * 0.5;
		return dmg;
	}

	static private double toMonsterInstance(MonsterInstance mi, object target,
			boolean bow) {
		Object o = PluginController.init(DamageController.class,
				"toMonsterInstance", mi, target, bow);
		if (o != null)
			return (Double) o;

		double dmg = 0;
		// 데미지 연산
		if (isHitFigure(mi, target, bow, null, false, 0)) {
			// 대미지 산출 20181125
			dmg += DmgFigure(mi, bow);
			dmg += DmgPlus(mi, null, target, bow);
			dmg += getMonsterLeveltoDamage(mi);

			/*
			 * if(mi.getMap()>=100 && mi.getMap()<=200) dmg += dmg * 0.3;
			 */

			// 활, 단검 데미지
			if (bow)
				dmg += Util.random(0, 3);
			else
				dmg += Util.random(0, 3);
			// 언데드속성이 -50보다 낮을경우 밤일때 데미지를 +@ 해주기.
			if (mi.getMonster().getResistanceUndead() <= -50) {
				// am 0~6시, pm 7~12시
				if (ServerDatabase.isNight())
					dmg += dmg * 0.2;
			}
		}
		return dmg * 1;
	}

	// if (target instanceof PetInstance) {
	// PetInstance pet = (PetInstance) target;
	//
	// int plevel = pet.getLevel();
	// double minus_dmg = plevel * 0.01;
	//
	// // System.out.println("감소전 " + dmg);
	//
	// dmg -= (dmg * minus_dmg);
	//
	// // System.out.println("감소후 " + dmg);
	//
	// }
	//
	// if (mi.getBoss() != null)
	// return dmg * 0.85;
	// else
	// return dmg * 0.65; // 0.8

	static private double toGuardInstance(GuardInstance gi, object target,
			boolean bow) {
		double dmg = 0;
		// 데미지 연산
		if (isHitFigure(gi, target, bow, null, false, 0))
			dmg = gi instanceof KingdomGuard ? Util.random(10, 30) : Util
					.random(100, 120);
		return dmg;
	}

	static private double toInstance(Character cha, object target, boolean bow) {
		double dmg = 0;
		// 데미지 연산
		if (isHitFigure(cha, target, bow, null, false, 0)) {
			// 대미지 산출
			dmg += DmgFigure(cha, bow);
			dmg += DmgPlus(cha, null, target, bow);
			// 단검 데미지
			dmg += Util.random(0, 4);
		}
		return dmg * 0.8;
	}

	/**
	 * 객체가 죽엇을때 그에따른 처리를 하는 함수.
	 * 
	 * @param cha
	 *            : 가해자
	 * @param o
	 *            : 피해자
	 */
	static private void toDead(Character cha, object o) {
		PluginController.init(DamageController.class, "toDead", cha, o);
		// 객체별 처리 구간.
		if (o instanceof PcInstance) {
			PcInstance use = (PcInstance) o;
			Kingdom kingdom = KingdomController.findKingdomLocation(use);
			boolean exp_drop = (cha.getMap() != Lineage.battle_zone_map
					&& use.getLevel() >= Lineage.player_dead_expdown_level && (cha instanceof NpcInstance
					|| cha instanceof MonsterInstance || World.isNormalZone(
					use.getX(), use.getY(), use.getMap())));
			boolean item_drop = (cha.getMap() != Lineage.battle_zone_map
					&& use.getLevel() >= Lineage.player_dead_itemdrop_level && (cha instanceof NpcInstance
					|| cha instanceof MonsterInstance || !World.isCombatZone(
					use.getX(), use.getY(), use.getMap())));
			boolean magic_drop = (cha.getMap() != Lineage.battle_zone_map
					&& use.getLawful() < Lineage.NEUTRAL && !World
					.isCombatZone(use.getX(), use.getY(), use.getMap()));
			boolean kingdom_war = use.getClassType() == Lineage.LINEAGE_CLASS_ROYAL
					&& // 군주면서
					use.getClanId() > 0 && // 혈이 있으면서
					kingdom != null && // 외성 좌표 안쪽에 있으면서
					kingdom.isWar() && // 해당성이 공성전중이면서
					kingdom.getListWar().contains(use.getClanName()); // 공성전 선포를
																		// 한
																		// 상태라면.
			for (int map : Lineage.MAP_EXP_NOT_LOSE) {
				if (cha instanceof PcInstance) {
					if (map == use.getMap())
						exp_drop = false;
				}
			}
			for (int map : Lineage.MAP_ITEM_NOT_DROP) {
				if (cha instanceof PcInstance) {
					if (map == use.getMap())
						item_drop = false;
						magic_drop = false;
				}
			}

			if (!Lineage.유저보호모드) {

				// 경험치 드랍 처리.
				if (exp_drop) {
					if (kingdom != null && kingdom.isWar()) {
						if (Lineage.kingdom_player_dead_expdown)
							CharacterController.toExpDown(use);
					} else if (Lineage.player_dead_expdown) {
						CharacterController.toExpDown(use);
					}
				}
				// 아이템 드랍 처리.
				// new 불멸의 가호
				boolean 불멸의가호 = 불멸의가호시스템(cha, use, kingdom);

				// 아이템 드랍 처리
				if (item_drop && 불멸의가호) {
					if (kingdom != null && kingdom.isWar()) {
						if (Lineage.kingdom_player_dead_itemdrop) {
							if (Lineage.is_old_gaho
									&& use.getInventory().find(
											ItemDatabase.find("불멸의 가호")) != null) {
								use.is_Gaho = true;
								ItemInstance item = use.getInventory().find(
										ItemDatabase.find("불멸의 가호"));
								ItemInstance item1 = ItemDatabase
										.newInstance(ItemDatabase
												.find("타버린 불멸의 가호"));
								if (item != null && use.getInventory() != null) {
									use.getInventory().count(item,
											item.getCount() - 1, true);
									if (item1 != null) {
										item1.setObjectId(ServerDatabase
												.nextItemObjId());
										item1.setCount(1);
										item1.toTeleport(use.getX(),
												use.getY(), use.getMap(), false);
										item1.toDrop(null);
									}
								}
							} else {
								CharacterController.toItemDrop(use);
							}
						}
					} else if (Lineage.player_dead_itemdrop) {
						if (Lineage.is_old_gaho
								&& use.getInventory().find(
										ItemDatabase.find("불멸의 가호")) != null) {
							use.is_Gaho = true;
							ItemInstance item = use.getInventory().find(
									ItemDatabase.find("불멸의 가호"));
							ItemInstance item1 = ItemDatabase
									.newInstance(ItemDatabase
											.find("타버린 불멸의 가호"));
							if (item != null && use.getInventory() != null) {
								use.getInventory().count(item,
										item.getCount() - 1, true);
								if (item1 != null) {
									item1.setObjectId(ServerDatabase
											.nextItemObjId());
									item1.setCount(1);
									item1.toTeleport(use.getX(), use.getY(),
											use.getMap(), false);
									item1.toDrop(null);
								}
							}
						} else {
							CharacterController.toItemDrop(use);
						}

					}
				}
				/*
				 * if (item_drop) { if (kingdom != null && kingdom.isWar()) { if
				 * (Lineage.kingdom_player_dead_itemdrop)
				 * CharacterController.toItemDrop(use); } else if
				 * (Lineage.player_dead_itemdrop) {
				 * CharacterController.toItemDrop(use); } }
				 */
				// 마법 드랍 처리.
				if (magic_drop) {
					for (int i = 0; i < 3; ++i) {
						Skill s = SkillController.find(use,
								Util.random(-200, 200), false);
						if (s != null)
							SkillController.remove(use, s, false);
					}
				}
			}
			// 공성전 패배 처리.
			/*
			 * if (kingdom_war) { // 전쟁 관리 목록에서 제거.
			 * kingdom.getListWar().remove(use.getClanName()); //
			 * World.toSender(
			 * S_ClanWar.clone(BasePacketPooling.getPool(S_ClanWar.class), 3,
			 * kingdom.getClanName(), use.getClanName())); }
			 */
		}
		// pvp 처리
		if (cha instanceof PcInstance && o instanceof PcInstance
				&& !World.isBattleZone(cha.getX(), cha.getY(), cha.getMap())
				&& !World.isBattleZone(o.getX(), o.getY(), o.getMap())) {
			PcInstance pc = (PcInstance) cha;
			PcInstance use = (PcInstance) o;

			// 컴뱃존이 아니면서 카오틱이 아니라면 pc를 피커로 판단. 보라돌이도.
			boolean is = !World.isCombatZone(use.getX(), use.getY(), use.getMap()) && use.getLawful() >= Lineage.NEUTRAL
					&& !use.isBuffCriminal();
			// 공성존 인지 확인.
			Kingdom kingdom = KingdomController.findKingdomLocation(use);
			// 성존이고 공성중일경우 환경설정에서 피커 처리 하도록 되있을때만 처리하게 유도.
			if (kingdom != null && kingdom.isWar())
				is = Lineage.kingdom_pvp_pk;
			// 혈전상태 확인. (혈전 상태일때 카오처리 할지 여부.)
			Clan clan = ClanController.find(pc);
			if (clan != null && clan.getWarClan() != null && clan.getWarClan().equalsIgnoreCase(use.getClanName()))
				is = false;
			// 현상수배일경우 피커치리 안하도록.
			/*
			 * if (WantedController.findTarget(use.getName()) != null) is = false;
			 */
			// 피커 처리를 해도 된다면.
			if (is) {
				// pkcount 상승
				pc.setPkCount(pc.getPkCount() + 1);
				// pk한 최근 시간값 기록. 만라였다면 한번 바줌.
				if (pc.getLawful() != Lineage.LAWFUL)
					pc.setPkTime(System.currentTimeMillis());
				// 지옥 시간값 상승.
				if (pc.getPkCount() >= Lineage.player_hellsystem_pk_count)
				pc.setPkHellTime(pc.getPkHellTime() + (Lineage.player_hellsystem_delay * 60 * (pc.getPkHellCount() + 1)));
				if (pc.getPkCount() >= 95 && pc.getPkCount() <= 99) {
				cha.toSender(new S_MessageGreen(556, String.format("\\f1당신의 PK 횟수가 %s이 되었습니다. 횟수가 %s이 되면 지옥에 가게 되니 주의하십시오.", pc.getPkCount(), Lineage.player_hellsystem_pk_count)));
				}
				if (pc.getPkCount() >= Lineage.player_hellsystem_pk_count) {
				cha.toSender(new S_MessageGreen(556, String.format("\\f3당신의 PK 횟수가 %s이 되어 지옥에 보내졌습니다. 당신은 이곳에서 %d분간 죄를 뉘우쳐야만 합니다.", pc.getPkCount(), pc.getPkHellTime()/60)));
				}
				// 라우풀값 하향.
                if (pc.getMap() != 5 || pc.getMap() != 70) {
				if (Lineage.is_batpomet_system) {
					// 바포메트시스템일때 라우풀 1만씩 깍기
					pc.setLawful(pc.getLawful() - 10000);
				} else {
					// 본섭화
					if (pc.getLawful() >= Lineage.NEUTRAL)
						pc.setLawful(Lineage.NEUTRAL - (pc.getLevel() * 150));
					else
						pc.setLawful(pc.getLawful() - (5000));
				}
              }
			}
			//
			if (!World.isCombatZone(use.getX(), use.getY(), use.getMap()))
				// 로그 처리.
				if (!World.isCombatZone(use.getX(), use.getY(), use.getMap())
						&& !World.isSafetyZone(use.getX(), use.getY(), use.getMap())) {

					CharactersDatabase.updatePvpKill(pc, use);
					CharactersDatabase.updatePvpDead(use, pc);
					//
					if (Lineage.pvp_print_message) {
						String local = Util.getMapName(cha);

						for (PcInstance p : World.getPcList()) {
							ChattingController.toChatting(p, String.format("PVP: %s (승) vs %s (패)",
									pc.getName(), use.getName()), Lineage.CHATTING_MODE_MESSAGE);
							ChattingController.toChatting(p, String.format("위치: %s", local), Lineage.CHATTING_MODE_MESSAGE);
					}
				}
				}
		}

		// gvp 처리
		if (cha instanceof GuardInstance && o instanceof PcInstance) {
			// GuardInstance gi = (GuardInstance)cha;
			PcInstance pc = (PcInstance) o;

			// 피케이한 이전기록이 남았을경우 그값을 초기화함.
			if (pc.getPkTime() > 0)
				pc.setPkTime(0);
		}
	}

	public static String getMapName(Character cha) {
		String local = null;

		switch (cha.getMap()) {
		case 0:
			local = "[말하는 섬]";
			break;
		case 1:
			local = "[말하는 섬 던전 1층]";
			break;
		case 2:
			local = "[말하는 섬 던전 2층]";
			break;
		case 4:
			if (cha.getX() >= 33315 && cha.getX() <= 33354
					&& cha.getY() >= 32430 && cha.getY() <= 32463) {
				local = "[용의 계곡 삼거리]";
				break;
			} else if (cha.getX() >= 33248 && cha.getX() <= 33284
					&& cha.getY() >= 32374 && cha.getY() <= 32413) {
				local = "[용의 계곡 작은뼈]";
				break;
			} else if (cha.getX() >= 33374 && cha.getX() <= 33406
					&& cha.getY() >= 32319 && cha.getY() <= 32357) {
				local = "[용의 계곡 큰뼈]";
				break;
			} else if (cha.getX() >= 33224 && cha.getX() <= 33445
					&& cha.getY() >= 32266 && cha.getY() <= 32483) {
				local = "[용의 계곡]";
				break;
			} else if (cha.getX() >= 33497 && cha.getX() <= 33781
					&& cha.getY() >= 32230 && cha.getY() <= 32413) {
				local = "[화룡의 둥지]";
				break;
			} else if (cha.getX() >= 33659 && cha.getX() <= 33799
					&& cha.getY() >= 32208 && cha.getY() <= 32348) {
				local = "[화룡의 둥지 정상]";
				break;
			} else if (cha.getX() >= 33497 && cha.getX() <= 33781
					&& cha.getY() >= 32230 && cha.getY() <= 32413) {
				local = "[화룡의 둥지]";
				break;
			} else if (cha.getX() >= 33832 && cha.getX() <= 34039
					&& cha.getY() >= 32341 && cha.getY() <= 32649) {
				local = "[좀비 엘모어 밭]";
				break;
			} else if (cha.getX() >= 32716 && cha.getX() <= 32980
					&& cha.getY() >= 33075 && cha.getY() <= 33391) {
				local = "[사막]";
				break;
			} else if (cha.getX() >= 32833 && cha.getX() <= 32975
					&& cha.getY() >= 32875 && cha.getY() <= 32957) {
				local = "[골밭]";
				break;
			} else if (cha.getX() >= 32707 && cha.getX() <= 32932
					&& cha.getY() >= 32611 && cha.getY() <= 32758) {
				local = "[카오틱 신전]";
				break;
			} else if (cha.getX() >= 33995 && cha.getX() <= 34091
					&& cha.getY() >= 32972 && cha.getY() <= 33045) {
				local = "[린드비오르의 둥지]";
				break;
			} else {
				local = "[아덴필드]";
				break;
			}
		case 5:
			local = "[몽환의 섬]";
			break;
		case 7:
			local = "[글루디오 던전 1층]";
			break;
		case 8:
			local = "[글루디오 던전 2층]";
			break;
		case 9:
			local = "[글루디오 던전 3층]";
			break;
		case 10:
			local = "[글루디오 던전 4층]";
			break;
		case 11:
			local = "[글루디오 던전 5층]";
			break;
		case 12:
			local = "[글루디오 던전 6층]";
			break;
		case 13:
			local = "[글루디오 던전 7층]";
			break;
		case 23:
			local = "[윈다우드 성 던전 1층]";
			break;
		case 24:
			local = "[윈다우드 성 던전 2층]";
			break;
		case 25:
			local = "[수련 던전 1층]";
			break;
		case 26:
			local = "[수련 던전 2층]";
			break;
		case 27:
			local = "[수련 던전 3층]";
			break;
		case 28:
			local = "[수련 던전 4층]";
			break;
		case 30:
			local = "[용의 계곡 던전 1층]";
			break;
		case 31:
			local = "[용의 계곡 던전 2층]";
			break;
		case 32:
			local = "[용의 계곡 던전 3층]";
			break;
		case 33:
			local = "[용의 계곡 던전 4층]";
			break;
		case 34:
			local = "[용의 계곡 던전 5층]";
			break;
		case 36:
			local = "[용의 계곡 던전 6층]";
			break;
		case 37:
			local = "[용의 계곡 던전 7층]";
			break;
		case 43:
		case 44:
		case 45:
		case 46:
		case 47:
		case 48:
		case 49:
		case 50:
			local = "[개미굴 1층]";
			break;
		case 51:
			local = "[개미굴 3층]";
			break;
		case 53:
			local = "[기란 감옥 1층]";
			break;
		case 54:
			local = "[기란 감옥 2층]";
			break;
		case 55:
			local = "[기란 감옥 3층]";
			break;
		case 56:
			local = "[기란 감옥 4층]";
			break;
		case 65:
			local = "[파푸리온의 둥지]";
			break;
		case 67:
			local = "[발라카스의 둥지]";
			break;
		case 70:
			local = "[잊혀진 섬]";
			break;
		case 72:
			local = "[얼음 던전 1층]";
			break;
		case 73:
			local = "[얼음 던전 2층]";
			break;
		case 74:
			local = "[얼음 던전 3층]";
			break;
		case 78:
			local = "[상아탑 4층]";
			break;
		case 79:
			local = "[상아탑 5층]";
			break;
		case 80:
			local = "[상아탑 6층]";
			break;
		case 81:
			local = "[상아탑 7층]";
			break;
		case 82:
			local = "[상아탑 8층]";
			break;
		case 101:
			local = "[오만의 탑 1층]";
			break;
		case 102:
			local = "[오만의 탑 2층]";
			break;
		case 103:
			local = "[오만의 탑 3층]";
			break;
		case 104:
			local = "[오만의 탑 4층]";
			break;
		case 105:
			local = "[오만의 탑 5층]";
			break;
		case 106:
			local = "[오만의 탑 6층]";
			break;
		case 107:
			local = "[오만의 탑 7층]";
			break;
		case 108:
			local = "[오만의 탑 8층]";
			break;
		case 109:
			local = "[오만의 탑 9층]";
			break;
		case 110:
			local = "[오만의 탑 10층]";
			break;
		case 200:
			local = "[오만의 탑 정상]";
			break;
		case 59:
			local = "[에바 던전 1층]";
			break;
		case 60:
			local = "[에바 던전 2층]";
			break;
		case 61:
			local = "[에바 던전 3층]";
			break;
		case 63:
			local = "[에바 던전 4층]";
			break;
		case 19:
			local = "[요정 숲 던전 1층]";
			break;
		case 20:
			local = "[요정 숲 던전 2층]";
			break;
		case 21:
			local = "[요정 숲 던전 3층]";
			break;
		case 777:
			local = "[버려진 땅]";
			break;
		case 780:
			local = "[테베라스 사막]";
			break;
		case 781:
			local = "[테베 피라미드]";
			break;
		case 782:
			local = "[테베 제단]";
			break;
		case 666:
			local = "[지옥]";
			break;
		default:
			local = "[본토]";
			break;
		}

		return local;
	}

	/**
	 * 몬스터의 레벨에따른 데미지 산출 메서드.
	 */
	static private int getMonsterLeveltoDamage(MonsterInstance mi) {
		if (mi.getMaxHp() < 101) {
			return (int) Math.round(Util.random(mi.getLevel()
					* Lineage_Balance.monsetr_hp100_mindmg, mi.getLevel()
					* Lineage_Balance.monsetr_hp100_maxdmg));
		} else if (mi.getMaxHp() < 201) {
			return (int) Math.round(Util.random(mi.getLevel()
					* Lineage_Balance.monsetr_hp200_mindmg, mi.getLevel()
					* Lineage_Balance.monsetr_hp200_maxdmg));
		} else if (mi.getMaxHp() < 301) {
			return (int) Math.round(Util.random(mi.getLevel()
					* Lineage_Balance.monsetr_hp300_mindmg, mi.getLevel()
					* Lineage_Balance.monsetr_hp300_maxdmg));
		} else if (mi.getMaxHp() < 401) {
			return (int) Math.round(Util.random(mi.getLevel()
					* Lineage_Balance.monsetr_hp400_mindmg, mi.getLevel()
					* Lineage_Balance.monsetr_hp400_maxdmg));
		} else if (mi.getMaxHp() < 650) {
			return (int) Math.round(Util.random(mi.getLevel()
					* Lineage_Balance.monsetr_hp655_mindmg, mi.getLevel()
					* Lineage_Balance.monsetr_hp655_maxdmg));
		} else if (mi.getMaxHp() < 900) {
			return (int) Math.round(Util.random(mi.getLevel()
					* Lineage_Balance.monsetr_hp900_mindmg, mi.getLevel()
					* Lineage_Balance.monsetr_hp900_maxdmg));
		} else if (mi.getMaxHp() < 1400) {
			return (int) Math.round(Util.random(mi.getLevel()
					* Lineage_Balance.monsetr_hp1400_mindmg, mi.getLevel()
					* Lineage_Balance.monsetr_hp1400_maxdmg));
		} else if (mi.getMaxHp() < 14000) {
			return (int) Math.round(Util.random(mi.getLevel()
					* Lineage_Balance.monsetr_hp14000_mindmg, mi.getLevel()
					* Lineage_Balance.monsetr_hp14000_mindmg));
		} else {
			return (int) Math.round(Util.random(mi.getLevel()
					* Lineage_Balance.monsetr_hp_boss_mindmg, mi.getLevel()
					* Lineage_Balance.monsetr_hp_boss_maxdmg));

		} /*
		 * if(mi.getMaxHp()<5000){ return (int)Math.round(
		 * Util.random(mi.getLevel()*8, mi.getLevel()* 20)); }else{ return
		 * (int)Math.round( Util.random(mi.getLevel()*2.3, mi.getLevel()*4) ); }
		 */

	}

	/**
	 * Pvp 대미지와 Mon 대미지.
	 * 
	 */
	static private double PvpMon(Character cha, ItemInstance item, object target) {
		double dmg = 0;
		// 대인전 적용
		if (cha instanceof PcInstance && target instanceof PcInstance) {
			if (item.getItem().getPvpDmg() > 0) {
				dmg += item.getItem().getPvpDmg();
			}
			// 몬스터
		} else {
			if (item.getItem().getMonDmg() > 0) {
				dmg += item.getItem().getMonDmg();
			}
		}
		return dmg;
	}

	/**
	 * 명중률을 계산하여 무기의 최대 대미지에 근접하도록 시킨다.
	 */
	static private double DexDmg(Character cha, ItemInstance weapon, double dmg) {
		double dmg2 = 0;
		double en = 0;
		switch (weapon.getEnLevel()) {
		case 2:
		case 3:
			en = 1;
			break;
		case 4:
		case 5:
			en = 2;
			break;
		case 6:
		case 7:
			en = 3;
			break;
		case 8:
			dmg += Util.random(1, 6);
		case 9:
			dmg += Util.random(1, 6);
			en = 4;
			break;
		case 10:
			dmg += Util.random(1, 6);
		case 11:
			dmg += Util.random(1, 6);
			en = 5;
			break;
		case 12:
			dmg += Util.random(1, 6);
		case 13:
			dmg += Util.random(1, 6);
			en = 6;
			break;
		case 14:
			dmg += Util.random(1, 6);
		case 15:
			dmg += Util.random(1, 6);
			en = 7;
			break;
		}
		if (weapon.getItem().getAddHit() > 0)
			en += weapon.getItem().getAddHit();
		if (weapon.getItem().getAddBowHit() > 0)
			en += weapon.getItem().getAddBowHit();

		return dmg;
	}

	/**
	 * 서먼 몬스터의 레벨에따른 데미지 산출 메서드. 2017-10-05 by all-night
	 */
	static private int getSummonLeveltoDamage(SummonInstance si) {
		return (int) Math.round(Util.random(si.getLevel()
				* Lineage_Balance.summon_level_min_damage_rate, si.getLevel()
				* Lineage_Balance.summon_level_max_damage_rate));
	}

	/**
	 * 펫 레벨에따른 데미지 산출 메서드. 2018-4-30 by all-night
	 */
	static private int getPetLeveltoDamage(SummonInstance si) {
		return (int) Math.round(Util.random(si.getLevel()
				* Lineage_Balance.pet_level_min_damage_rate, si.getLevel()
				* Lineage_Balance.pet_level_max_damage_rate));
	}

	/**
	 * 속성 데미지 처리 부분.
	 * 
	 * @param item
	 * @param target
	 * @return
	 */
	static private double DmgElement(ItemInstance item, object target) {
		double dmg = 0;
		int fire = 0, earh = 0, wind = 0, warter = 0;
		//
		if (target instanceof Character) {
			Character cha = (Character) target;
			fire = cha.getTotalFireress();
			earh = cha.getTotalEarthress();
			wind = cha.getTotalWindress();
			warter = cha.getTotalWaterress();
		}
		//
		if (item.getItem().getFireress() > 0 || item.getEnFireDamage() > 0) {
			double item_dmg = item.getItem().getFireress()
					+ item.getEnFireDamage();
			double el_dmg = (fire * 0.25) * 0.01;
			item_dmg -= item_dmg * el_dmg;
			dmg += item_dmg;
		}
		if (item.getItem().getEarthress() > 0 || item.getEnEarthDamage() > 0) {
			double item_dmg = item.getItem().getEarthress()
					+ item.getEnEarthDamage();
			double el_dmg = (earh * 0.25) * 0.01;
			item_dmg -= item_dmg * el_dmg;
			dmg += item_dmg;
		}
		if (item.getItem().getWindress() > 0 || item.getEnWindDamage() > 0) {
			double item_dmg = item.getItem().getWindress()
					+ item.getEnWindDamage();
			double el_dmg = (wind * 0.25) * 0.01;
			item_dmg -= item_dmg * el_dmg;
			dmg += item_dmg;
		}
		if (item.getItem().getWaterress() > 0 || item.getEnWaterDamage() > 0) {
			double item_dmg = item.getItem().getWaterress()
					+ item.getEnWaterDamage();
			double el_dmg = (warter * 0.25) * 0.01;
			item_dmg -= item_dmg * el_dmg;
			dmg += item_dmg;
		}
		//
		return dmg;
	}

	/**
	 * 추가적으로 적용되는 대미지 처리 부분 - 버프상태에 따른처리
	 * 
	 * @param cha
	 *            : 가격자
	 * @param item
	 *            : 착용한 무기
	 * @param target
	 *            : 몬스터인지 유저인지 구분 처리하기위한 객체정보
	 * @return : 계산된 대미지 리턴
	 */
	static private double DmgPlus(Character cha, ItemInstance item, object target, boolean bow) {
		// Object temp = PluginController.init(DamageController.class,
		// "DmgPlus", cha, item, target, bow);
		// if (temp != null && temp instanceof Double)
		// return (Double) temp;

		double dmg = 0;
		// 대상이 유저일경우 pvp동적 데미지 추가.
		if (target instanceof PcInstance) {
			dmg += cha.getDynamicAddPvpDmg();
		}
		//
		if (item != null) {
			ItemWeaponInstance weapon = (ItemWeaponInstance) item;
			// 인첸트 웨폰
			if (weapon.isBuffEnchantWeapon())
				dmg += 1;
			// 블레스웨폰 상태라면 랜타 데미지 2 추가
			if (weapon.isBuffBlessWeapon())
				dmg += Util.random(0, 2);

		}
		// 홀리써클 상태라면 랜타 데미지 2 추가
		// if(buff->findMagic(52))
		// dmg += Common::random(0, 2);
		// 글로잉오라
		if (cha.isBuffGlowingAura())
			dmg += 1;
		// 인첸트베놈
		if (cha.isBuffEnchantVenom() && Util.random(0, 99) < 20) {
			if (target instanceof BackgroundInstance) {

			} else {
				// 제니스 퀸 반지 20190128 , 커스포이즌 에서도 수정 함.
				if (target.getInventory().isRingZnis() || target.isBuffVenomResist()) {

				} else {
					BuffController.append(target, CursePoison.clone(BuffController.getPool(CursePoison.class), cha,
							SkillDatabase.find(2, 2), 32));
				}
			}
		}
		// 마법인형 : 라미아
		if (cha.isMagicdollRamia() && Util.random(0, 99) < 10) {
			if (target.getInventory().isRingZnis() || target.isBuffVenomResist()) {

			} else {
				BuffController.append(target, CursePoison.clone(BuffController.getPool(CursePoison.class), cha,
						SkillDatabase.find(2, 2), 10));
			}
		}

		// 마법인형 : 흑장로 (콜라이팅 발동 적용) 외부화작업
		if (cha.isMagicdollBlackElder()
				&& Util.random(1, 100) <= Lineage_Balance.magicDoll_black_elder_persent) {
			int addDmg = Util.random(
					Lineage_Balance.magicDoll_black_elder_min_damage,
					Lineage_Balance.magicDoll_black_elder_max_damage);
			dmg += addDmg;

			cha.toSender(S_ObjectEffect.clone(
					BasePacketPooling.getPool(S_ObjectEffect.class),
					target, Lineage.call_lighting_effect), true);
		}
		// 마법인형 : 데스나이트 (헬파이어 발동 적용) 외부화작업
		if (cha.isMagicdollDeathKnight()
				&& Util.random(1, 100) <= Lineage_Balance.magicDoll_death_knight_persent) {
			int addDmg = Util.random(
					Lineage_Balance.magicDoll_death_knight_min_damage,
					Lineage_Balance.magicDoll_death_knight_max_damage);
			dmg += addDmg;

			cha.toSender(S_ObjectEffect.clone(
					BasePacketPooling.getPool(S_ObjectEffect.class),
					target, Lineage.hell_fire_effect), true);
		}
		// 마법인형 : 기르타스(마법 발동 적용) 외부화작업
		if (cha.isMagicdollGirtas()
				&& Util.random(1, 100) <= Lineage_Balance.magicDoll_girtas_persent) {
			int addDmg = Util.random(
					Lineage_Balance.magicDoll_girtas_min_damage,
					Lineage_Balance.magicDoll_girtas_max_damage);
			dmg += addDmg;

			cha.toSender(S_ObjectEffect.clone(
					BasePacketPooling.getPool(S_ObjectEffect.class),
					target, Lineage.disinte_grate_effect), true);
		}
		
		// 장거리 공격일때.
		if (bow) {
			dmg += cha.getTotalAddDmgBow();
			// 아이오브스톰
			if (cha.isBuffEyeOfStorm())
				dmg += 2;
			// 스톰샷
			if (cha.isBuffStormShot())
				dmg += 5;

		} else {
			dmg += cha.getTotalAddDmg();
			// 버서커스
			if (cha.isBuffBerserks())
				dmg += 5;
			// 마법인형 : 늑대인간 || 크러스트시안 (근거리 대미지 +15)
			if ((cha.isMagicdollWerewolf() || cha.isMagicdollCrustacean())
					&& Util.random(0, 99) <= 3) {
				dmg += 15;
				int effect = MagicdollListDatabase.getBuffEffect(cha
						.isMagicdollWerewolf() ? "늑대인간" : "크러스트시안");
				if (effect > 0)
					cha.toSender(S_ObjectEffect.clone(
							BasePacketPooling.getPool(S_ObjectEffect.class),
							cha, effect), true);
			}
			// 파이어웨폰 근거리 무기 +4 적용.
			if (cha.isBuffFireWeapon())
				dmg += 4;
			// 버닝웨폰 근거리 무기 +6 적용.
			if (cha.isBuffBurningWeapon())
				dmg += 6;
			// 버닝 슬래쉬
			dmg += SkillController
					.toDamagePlus(BurningSlash.class, cha, target);

		}
		// 홀리웨폰 상태라면 랜타 데미지 2 추가
		if (cha.isBuffHolyWeapon()) {
			if (target instanceof MonsterInstance) {
				MonsterInstance mon = (MonsterInstance) target;
				if (mon.getMonster().getResistanceUndead() < 0)
					dmg += 2;
			}
		}
		return dmg;
	}

	/**
	 * 인벤토리에서 무기를 찾은후 무기의 대미지 토탈값 리턴
	 */
	static private double DmgWeaponFigure(PcInstance pc, boolean bow,
			ItemInstance weapon, ItemInstance arrow, boolean Small) {
		Object o = PluginController.init(DamageController.class,
				"DmgWeaponFigure", bow, weapon, arrow, Small);
		if (o != null)
			return (Double) o;

		double dmg = 0;
		double endmg = 0;
		double lastdmg = 0;

		endmg += weapon.getEnLevel();// 6
		endmg += weapon.getItem().getAddDmg();// 3

		// 무기 대미지 1~ 추출
		if (Small)
			dmg += weapon.getItem().getDmgMin();// 10
		else
			dmg += weapon.getItem().getDmgMax();// 11

		// en : 명중률 값 , endmg = 인첸+추타 대미지 , dmg = 무기대미지
		lastdmg = dmg + endmg; // Util.random(endmg+1, dmg + endmg);

		double bowdmg = 0;
		// 활일경우 화살에 데미지 추출
		if (bow) {
			// 화살이 있을경우.
			if (arrow != null) {
				if (Small)
					bowdmg += Util.random(1, arrow.getItem().getDmgMin());
				else
					bowdmg += Util.random(1, arrow.getItem().getDmgMax());
			} else {
				// 사이하활일 경우 마법 데미지 추가
				if (weapon.getItem().getNameIdNumber() == 1821)
					bowdmg += Util.random(12, 12);
			}
		}
		return lastdmg + bowdmg;
	}

	/**
	 * 해당 객체의 스탯정보만으로 총 대미지 산출.
	 */
	static private double DmgFigure(Character cha, boolean bow) {
		Object temp = PluginController.init(DamageController.class,
				"DmgFigure", cha, bow);

		if (temp != null && temp instanceof Double)
			return (Double) temp;

		double dmg = 0;

		if (bow)
			dmg += CharacterController.toStatDex(cha, "DmgFigure");
		else
			dmg += CharacterController.toStatStr(cha, "DmgFigure");

		if (cha.getLevel() >= 50)
			dmg += (cha.getLevel() - 50) * 0.2;

		return dmg;

		// 순수 스탯 대미지 + 10레벨당 추타+1

	}

	// 경험치 획득 페널티 부분에서 데미지 추가 혜택 적용 (본섭도 이와 같은 상황)

	/**
	 * 공격 성공여부 처리
	 */
	static private boolean isHitFigure(Character cha, object target, boolean bow, ItemInstance weapon, boolean isTriple, int tripleIdx) {
		// 허수아비는 무조건 성공
		if (target instanceof Cracker)
			return true;
		if (target instanceof CrackerDmg)
			return true;
		if (bow && weapon == null && target instanceof PcInstance)
			return true;
		// 지룡/탄생/형상/생명의 마안
		if ((target.isBuffMaanEarth() || target.isBuffMaanLife() || target.isBuffMaanBirth() || target.isBuffMaanShape()) && Util.random(1, 100) <= 6) {
				if (target.isBuffMaanEarth())
					target.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), target, Lineage.지룡의마안_이팩트));
				else if (target.isBuffMaanBirth())
					target.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), target, Lineage.탄생의마안_이팩트));
				else if (target.isBuffMaanShape())
					target.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), target, Lineage.형상의마안_이팩트));
				else if (target.isBuffMaanLife())
					target.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), target, Lineage.생명의마안_이팩트));
		
			return false;
		}
		// 공격성공율
		// 힘40 / 덱스 40 기준으로 제작하였으므로, 근거리 명중, 원거리 명중치를 빼고 계산
		// by all_night
		double probability = 100 - (bow ? 30 : 28);
		double tempProbability = 0;

		// 무기 인첸
		if (weapon != null)
			probability += weapon.getEnLevel();

		if (bow) {
			// 스탯에 따른 보정
			probability += probability += CharacterController.toStatDex(cha, "isHitFigure");
		} else {
			if (weapon != null) {
				// 홀리웨폰 근거리 명중+1
				if (weapon.isBuffHolyWeapon())
					probability += 1;
				// 블레스웨폰 근거리 명중+2
				if (weapon.isBuffBlessWeapon())
					probability += 2;

			}
			// 스탯에 따른 보정
			probability += CharacterController.toStatStr(cha, "isHitFigure");

		}

		if (target instanceof Character) {
			if (bow) {
				// 활공격시 타켓에 er값을 추출함.
				Character c = (Character) target;

				tempProbability = acProbability(c, bow) + getEr(c);

				if (c.isBuffStrikerGale())
					tempProbability /= 3;

			} else {
				// 근거리공격시 타켓에 ac따른 값 추출
				Character c = (Character) target;

				tempProbability = acProbability(c, bow);

				// 보스는 유저 공격시 좀더 잘박히게 설정.
				if (cha instanceof MonsterInstance
						&& target instanceof PcInstance) {
					MonsterInstance boss = (MonsterInstance) cha;
					if (boss != null && boss.getMonster() != null
							&& boss.getMonster().isBoss())
						tempProbability /= 3;

				}
			}
		}

		// System.out.println(probability);

		// 피격시 AC 적용률
		if (target instanceof PcInstance) {
			switch (target.getClassType()) {
			case Lineage.LINEAGE_CLASS_ROYAL:
				if (bow)
					tempProbability *= 40;
				else
					tempProbability *= 30;
				break;
			case Lineage.LINEAGE_CLASS_KNIGHT:
				if (bow)
					tempProbability *= 40;
				else
					tempProbability *= 30;
				break;
			case Lineage.LINEAGE_CLASS_ELF:
				if (bow)
					tempProbability *= 40;
				else
					tempProbability *= 30;
				break;
			case Lineage.LINEAGE_CLASS_WIZARD:
				if (bow)
					tempProbability *= 30;
				else
					tempProbability *= 30;
				break;
			}
		}
		// System.out.println(probability);

		if (bow) {
			if (cha instanceof MonsterInstance)
				tempProbability *= 0.01;
			else if (cha instanceof SummonInstance)
				tempProbability *= 0.02;
			else if (cha instanceof PcInstance)
				tempProbability *= 0.015;
		} else {
			if (cha instanceof MonsterInstance)
				tempProbability *= 0.01;

			else if (cha instanceof SummonInstance)
				tempProbability *= 0.02;
			else if (cha instanceof PcInstance)
				tempProbability *= 0.005;

//			System.out.println(probability);
		}

		// 1레벨 차이당 1%감소
		if (cha != null && target != null && cha instanceof PcInstance
				&& target instanceof PcInstance) {
			if (target.getLevel() > 1)
				probability += ((cha.getLevel() - target.getLevel()));
		}

		probability -= tempProbability;

		if (probability < 5)
			probability = 5;

		if (weapon != null && !bow) {
			if (weapon.getItem().getType2().equalsIgnoreCase("tohandsword"))
				probability *= 1;
		}

		boolean result = Util.random(1, 100) <= (probability < 1 ? 1 : Math
				.round(probability));

		if (!result) {
			// 크리티컬 이팩트 초기화
			cha.setCriticalEffect(false);

			if (Lineage.is_miss_effect)
				target.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), target, Lineage.miss_effect));
		}

		return result;
	}

	static private double acProbability(Character target, boolean bow) {
		double probability = 0;

		for (int i = 0; i < target.getTotalAc(); i++) {
			if (i < 10)
				probability += 0.5;
			else if (i < 20)
				probability += 0.7;
			else if (i < 30)
				probability += 0.8;
			else if (i < 40)
				probability += 0.85;
			else if (i < 50)
				probability += 0.85;
			else if (i < 60)
				probability += 0.85;
			else if (i < 70)
				probability += 0.85;
			else if (i < 80)
				probability += 0.9;
			else if (i < 90)
				probability += 0.9;
			else if (i < 100)
				probability += 1;
			else if (i < 110)
				probability += 1;
			else if (i < 120)
				probability += 1;
			else if (i < 130)
				probability += 1;
			else if (i < 140)
				probability += 1;
			else if (i < 150)
				probability += 1;
			else if (i < 160)
				probability += 1;
			else
				probability += 1;
		}
		
		return probability;
	}

	/*
	 * 클래스별 베이스 스텟 대미지
	 */
	static private double toBaseStrStatDamage(Character cha) {
		double sum = 0;
		int str = cha.getStr();
		switch (cha.getClassType()) {
		case 0:// 최소 13 ~ 20
			str -= 13; // 0~8
			sum += str / 3;
			break;
		case 1:
			str -= 16;
			sum += str;
			break;
		case 2:
			str -= 12; // 0~ 6
			sum += str / 2;
			break;
		case 3:
			str -= 8; // 0~12
			sum += str / 6;
			break;
		case 4:
			str -= 12; // 0~6
			sum += str / 3;
			break;
		}
		return sum;
	}

	/**
	 * 클레스별 추가 타격치 +@ 리턴
	 */
	static private double toOriginalStatDamage(Character cha) {
		double sum = 0;
		int str = cha.getTotalStr();
		switch (cha.getClassType()) {
		/*
		 * case 0: str -= 13; sum += str / 3; break; case 1: str -= 16; sum +=
		 * str / 2; break; case 2: str -= 11; sum += str / 2.5D; break; case 3:
		 * str -= 8; sum += str / 2; break;
		 */
		case 0:
			str -= 13;
			sum += str / 2;
			if (cha.getTotalStr() > 100)
				sum += cha.getTotalStr() - 100;
			break;
		case 1:
		case 5:
			str -= 16;
			sum += str / 2;
			if (cha.getTotalStr() > 100)
				sum += cha.getTotalStr() - 100;
			break;
		case 2:
		case 4:
			str -= 11;
			sum += str / 2.5D;
			if (cha.getTotalStr() > 100)
				sum += cha.getTotalStr() - 100;
			break;
		case 3:
			str -= 8;
			sum += str / 2;
			if (cha.getTotalStr() > 100)
				sum += cha.getTotalStr() - 100;
			break;
		}

		return sum;
	}

	/**
	 * 클레스별 활추가데미지 +@ 리턴
	 */
	static private double toOriginalStatBowDamage(Character cha) {
		double sum = 0;
		int dex = cha.getDex();
		switch (cha.getClassType()) {
		case 0:
			dex -= 10;
			sum += dex / 8;
			break;
		case 1:
			break;
		case 2:
			dex -= 12; // 0~6
			sum += dex / 2;
			break;
		case 3:
			break;
		case 4:
			dex -= 15; // 0~ 3
			sum += dex / 1.5;
			break;
		}
		return sum;
	}

	/**
	 * 클레스별 공격 성공율 +@ 리턴
	 */
	static private int toOriginalStatHit(Character cha) {
		int sum = 0;
		int str = cha.getTotalStr();
		switch (cha.getClassType()) {
		// case 0:
		// str -= 16;
		// if (str >= 1)
		// sum += 2;
		// if (str < 3)
		// break;
		// sum += 2;
		// break;
		case 0:
			str -= 10;
			if (str >= 3)
				sum++;
			if (str < 6)
				break;
			sum++;
			break;
		case 1:
			str -= 16;
			if (str >= 1)
				sum += 2;
			if (str < 3)
				break;
			sum += 2;
			break;
		case 2:
			str -= 11;
			if (str >= 2)
				sum++;
			if (str < 4)
				break;
			sum++;
			break;
		case 3:
			str -= 8;
			if (str >= 3)
				sum++;
			if (str < 5)
				break;
			sum++;
		}
		return sum;
	}

	/**
	 * 힘에 따른 공격성공율 +@ 리턴
	 */
	static private int toHitStr(Character cha) {
		int[] strHit = { -2, -2, -2, -2, -2, -2, -2, -2, -1, -1, 0, 0, 1, 1, 2,
				2, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7, 8, 8, 8, 9, 9, 9,
				10, 10, 10, 11, 11, 11, 12, 12, 12, 13, 13, 13, 14, 14, 14, 15,
				15, 15, 16, 16, 16, 17, 17 };

		try {
			if (cha.getTotalStr() > 59)
				return strHit[58];
			else
				return strHit[cha.getTotalStr() - 1];
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 덱스에 따른 활 전용 공격성공율 +@ 리턴
	 */
	static private int toHitDex(Character cha) {
		int[] dexHit = { -2, -2, -2, -2, -2, -2, -1, -1, 0, 0, 1, 1, 2, 2, 3,
				3, 4, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
				19, 19, 20, 20, 20, 21, 21, 21, 22, 22, 22, 23, 23, 23, 24, 24,
				24, 25, 25, 25, 26, 26, 26, 27, 27, 27, 28 };

		try {
			if (cha.getTotalDex() > 60)
				return dexHit[70];
			else
				return dexHit[cha.getTotalDex() - 1];
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * 레벨에 따른 공격성공율 +@ 리턴
	 */
	static private int toHitLv(Character cha) {
		int hit = 0;
		switch (cha.getClassType()) {
		case 0x00:
			if (cha.getLevel() > 3)
				hit += cha.getLevel() / 2;
			break;
		case 0x01:
			if (cha.getLevel() > 2)
				hit += cha.getLevel() / 2;
			break;
		case 0x02:
			if (cha.getLevel() > 4)
				hit += cha.getLevel() / 3;
			break;
		case 0x03:
			if (cha.getLevel() > 7)
				hit += cha.getLevel() / 5;
			break;
		case 0x04:
			if (cha.getLevel() > 2)
				hit += cha.getLevel() / 3;
			break;
		default:
			return hit += cha.getLevel() / 10;

		}
		return hit;
	}

	/**
	 * 클레스별 활명중률 +@ 리턴
	 */
	static private int toOriginalStatBowHit(Character cha) {
		int sum = 0;
		int dex = cha.getDex() + cha.getLvDex();
		switch (cha.getClassType()) {
		case 0x00:
			break;
		case 0x01:
			break;
		case 0x02:
		case 0x06:
			dex -= 12;
			if (dex >= 1)
				sum += 2;
			if (dex >= 4)
				sum += 1;
			break;
		case 0x03:
			break;
		}
		return sum;
	}

	/**
	 * 장거리 공격 회피율 리턴
	 */
	static public int getEr(Character cha) {
		double total_er = (cha.getTotalDex() / 2) + cha.getDynamicEr();

		switch (cha.getClassType()) {
		case Lineage.LINEAGE_CLASS_ROYAL:
		case Lineage.LINEAGE_CLASS_ELF:
			total_er += cha.getLevel() / 6;
			break;
		case Lineage.LINEAGE_CLASS_KNIGHT:
			total_er += cha.getLevel() / 4;
			break;
		case Lineage.LINEAGE_CLASS_WIZARD:
			total_er += cha.getLevel() / 10;
			break;
		}

		return (int) Math.round(total_er);
	}

	/**
	 * 덱스에 의한 ER +@ 리턴 , 다이나믹 ER 합산
	 */
	static public int toOriginalStatER(Character cha) {
		int sum = 0;

		if (cha.getTotalDex() < 8) {
			sum = -1;
		} else {
			sum += (cha.getTotalDex() - 8) / 2;
		}
		sum += cha.getDynamicEr();

		return sum;
	}

	/**
	 * 요정 클레스 요숲경비병에게 도움처리 함수. : 요정은 요숲에서 사냥시 근처 요숲경비가잇을경우 도움을 줌.
	 */
	static private void toElven(PcInstance pc, object o) {
		if (pc.getClassType() == Lineage.LINEAGE_CLASS_ELF
				&& o instanceof MonsterInstance
				&& !(o instanceof SummonInstance)) {
			for (object inside : pc.getInsideList(true)) {
				if (inside instanceof ElvenGuard)
					inside.toDamage((Character) o, 0,
							Lineage.ATTACK_TYPE_DIRECT);
			}
		}
	}

	/**
	 * 근처 경비병에게 도움요청처리하는 함수. : 다른 놈에게 pk를 당하거나 할때 처리하는 함수.
	 * 
	 * @param pc
	 *            : 요청자
	 * @param cha
	 *            : 공격자
	 */
	static public void toGuardHelper(PcInstance pc, Character cha) {
		// 요청자가 카오라면 무시, 성환 보라돌이
		// if (pc.getLawful() < Lineage.NEUTRAL)
		// return;
		// 요청자가 보라돌이 상태라면 무시. 단! 가격자가 카오가 아닐때만.
		if (pc.isBuffCriminal() && cha.getLawful() >= Lineage.NEUTRAL)
			return;
		// 사용자가 가격햇고 노말존에 잇엇을경우.
		if (World.isNormalZone(pc.getX(), pc.getY(), pc.getMap())) {
			for (object inside : pc.getInsideList(true)) {
				if (inside instanceof GuardInstance)
					inside.toDamage(cha, 0, Lineage.ATTACK_TYPE_DIRECT);
			}
		}
	}

	// new 불멸의 가호
	static public boolean 불멸의가호시스템(Character cha, PcInstance use, Kingdom k) {
		if (Lineage.is_immortality_pvp) {
			if (cha instanceof MonsterInstance) {
				return true;
			}
		}

		try {
			if (!Lineage.is_immortality || cha == null || use == null
					|| cha.getInventory() == null || use.getInventory() == null) {
				return true;
			}

			if (k != null && k.isWar())
				return true;

			Inventory iv = use.getInventory();
			ItemInstance 불멸의가호 = null;

			for (ItemInstance i : iv.getList()) {
				if (i != null
						&& i.getItem() != null
						&& i.getItem().getName().equalsIgnoreCase(Lineage.immortality_item_name)) {
					불멸의가호 = i;
					break;
				}
			}

			if (불멸의가호 != null) {
				iv.count(불멸의가호, 불멸의가호.getCount() - 1, true);
				// ChattingController.toChatting(use,
				// "\\fR불멸의 가호로 사망 패널티를 받지않았습니다.",
				// Lineage.CHATTING_MODE_MESSAGE);

				if (!Lineage.is_immortality_kill_item_drop_monster && cha instanceof MonsterInstance) {
					return false;
				}

				if (Lineage.is_immortality_kill_item) {
					try {
						Item i = ItemDatabase
								.find(Lineage.immortality_kill_item_name);
						long count = 1;

						if (i != null) {
							if (Lineage.is_immortality_kill_item_drop
									|| cha instanceof MonsterInstance) {
								for (int idx = 0; idx < count; idx++) {
									ItemInstance ii = ItemDatabase
											.newInstance(i);
									ii.setObjectId(ServerDatabase
											.nextItemObjId());
									ii.setCount(count);
									ii.toTeleport(use.getX(), use.getY(),
											use.getMap(), false);
									// 드랍됫다는거 알리기.
									ii.toDrop(null);
								}
							} else {
								if (cha instanceof PcInstance) {
									PcInstance pc = (PcInstance) cha;
									ItemInstance temp = pc.getInventory().find(
											i.getName(), 1, i.isPiles());

									if (temp != null
											&& (temp.getEnLevel() != 0))
										temp = null;

									if (temp == null) {
										// 겹칠수 있는 아이템이 존재하지 않을경우.
										if (i.isPiles()) {
											temp = ItemDatabase.newInstance(i);
											temp.setObjectId(ServerDatabase
													.nextItemObjId());
											temp.setEnLevel(0);
											temp.setCount(count);
											temp.setDefinite(true);
											pc.getInventory()
													.append(temp, true);
										} else {
											for (int idx = 0; idx < count; idx++) {
												temp = ItemDatabase
														.newInstance(i);
												temp.setObjectId(ServerDatabase
														.nextItemObjId());
												temp.setEnLevel(0);
												temp.setDefinite(true);
												pc.getInventory().append(temp,
														true);
											}
										}
									} else {
										// 겹치는 아이템이 존재할 경우.
										pc.getInventory().count(temp,
												temp.getCount() + count, true);
									}

									ChattingController.toChatting(pc, String.format("\\fR[불멸의 가호 보상] %s 획득.", i.getName()), Lineage.CHATTING_MODE_MESSAGE);
								}
							}
						}
					} catch (Exception e) {
						lineage.share.System.printf(
								"%s : 불멸의 가호 보상 지급 에러. 캐릭명: %s\r\n",
								DamageController.class.toString(),
								cha.getName());
						lineage.share.System.println(e);
					}
				}
				return false;
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : 불멸의 가호 에러. 캐릭명: %s\r\n",
					DamageController.class.toString(), use.getName());
			lineage.share.System.println(e);
		}
		return true;
	}
}
