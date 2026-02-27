package lineage.world.object.magic;

import lineage.bean.database.Item;
import lineage.bean.database.ItemSetoption;
import lineage.bean.database.MonsterSkill;
import lineage.bean.database.Poly;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.ItemDatabase;
import lineage.database.ItemMaplewandDatabase;
import lineage.database.PolyDatabase;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectGfx;
import lineage.network.packet.server.S_ObjectMode;
import lineage.network.packet.server.S_ObjectPoly;
import lineage.network.packet.server.S_ObjectPolyIcon;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.RankController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.RobotInstance;
import lineage.world.object.item.helm.Turban;
import sp.controller.PcInstanceController;

public class ShapeChange extends Magic {

	public ShapeChange(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new ShapeChange(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public boolean toBuffStop(object o) {
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		if (o.isWorldDelete())
			return;

		o.setGfx(o.getClassGfx());
		o.setGfxModeClear();
		o.toSender(S_ObjectGfx.clone(BasePacketPooling.getPool(S_ObjectGfx.class), o), true);
		// o.toSender(S_ObjectPoly.clone(BasePacketPooling.getPool(S_ObjectPoly.class),
		// o), true);
	}

	static public void init(Character cha, Skill skill, long object_id) {
		// 초기화
		object o = null;
		// 타겟 찾기
		if (object_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList(object_id);
		// 처리
		if (o != null) {
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
			if (SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, o, skill, true, false)) {
				if (World.isSafetyZone(cha.getX(), cha.getY(), cha.getMap()) && o.getObjectId() != cha.getObjectId())
					return;

				// 변신
				onBuff(cha, o, null, skill.getBuffDuration(), true, true);
				// 이팩트
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()),
						true);
			}
		}
	}

	static public void init(Character cha, int time) {
		if (Lineage.server_version > 182)
			cha.toSender(S_ObjectPolyIcon.clone(BasePacketPooling.getPool(S_ObjectPolyIcon.class), time));
		BuffController.append(cha,
				ShapeChange.clone(BuffController.getPool(ShapeChange.class), SkillDatabase.find(10, 7), time));
	}

	/**
	 * 몬스터가 사용하는 함수.
	 * 
	 * @param mi
	 * @param skill
	 * @param action
	 */
	static public void init(MonsterInstance mi, object o, MonsterSkill ms, int action, int effect) {
		//
		onBuff(mi, o, PolyDatabase.getName(ms.getOption()), ms.getBuffDuration(), true, true);
		//
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, ms.getCastGfx()), true);
	}

	/**
	 * 몬스터가 사용하는 변신 처리 함수. : 몬스터 몇셀 주변에 있는 모든 사용자들을 강제 변신시킴. : 리치가 사용중.
	 * 
	 * @param mi
	 */
	static public void init(MonsterInstance mi, int loc, Poly poly, int time) {
		for (object o : mi.getInsideList(true)) {
			if (o instanceof PcInstance) {
				// 변신
				onBuff(mi, o, poly, time, true, true);
			}
		}
	}

	/**
	 * 변신주문서 에서 호출해서 사용중..
	 * 
	 * @param cha
	 * @param target
	 * @param p
	 * @param time
	 */
	static public boolean init(Character cha, Character target, Poly p, int time, int bress) {
		int temp = 0;
		for (ItemInstance item : cha.getInventory().getList()) {
			if (item.getItem().getName().equals("변신 조종 반지5")) {
				if (item.isEquipped()) {
					temp += 1;
				}
			}
		}
		
		if (p != null) {
			if (p.getMinLevel() <= (cha.getLevel() + temp) || Lineage.event_poly || (bress == 0 && Lineage.item_polymorph_bless))
				onBuff(cha, target, p, time, false, true);
			else {
				ChattingController.toChatting(cha, String.format("%d레벨 이상 변신이 가능합니다.", p.getMinLevel()), Lineage.CHATTING_MODE_MESSAGE);
				return false;
			}
			if (!(p.getName().contains("지배") || p.getName().contains("랭커") || p.getName().contains("샤르나")))
			cha.setFast_poly(p.getPolyName());
		} else {
			BuffController.remove(cha, ShapeChange.class);
		}


		return true;
	}

	public static void initGm(Character cha, Character target, Poly p, int time, int bress) {
		if (p != null)
			onBuff(cha, target, p, time, false, true);
		else
			BuffController.remove(cha, ShapeChange.class);

	}

	/**
	 * 변신 최종 뒷처리 구간 : 마법을 통해서 이쪽으로 옴. : 변막을 통해서 이쪽으로 옴.
	 * 
	 * @param cha
	 * @param o
	 * @param time
	 */
	static public void onBuff(Character cha, object o, Poly p, int time, boolean ring, boolean packet) {
		if (cha == null || o == null || !(o instanceof PcInstance)
				|| (cha.getObjectId() != o.getObjectId() && o instanceof RobotInstance))
			return;

		// 앱솔상태 해제.
		if (cha.isBuffAbsoluteBarrier())
			BuffController.remove(cha, AbsoluteBarrier.class);
		
		if (cha.isFishing()) {
			ChattingController.toChatting(cha, "낚시중에는 변신할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		if (!is변신을하거나해제해도되는가(o, p, false)){
			ChattingController.toChatting(o, "세트 변신 때문에 다른 변신을 하실 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		if (cha.isInvis()) {
			cha.setInvis(false);
			BuffController.remove(cha, InvisiBility.class);
		}
		if (o.getInventory() != null && ring && o.getInventory().isRingOfPolymorphControl()) {
			// 변신할 괴물의 이름을 넣으십시오.
			if (packet)
				o.toSender(S_MessageYesNo.clone(BasePacketPooling.getPool(S_MessageYesNo.class), 180));

		} else {
			if (p == null)
				p = ItemMaplewandDatabase.randomPoly();
			if (p != null && !o.isDead()) {
				if (o instanceof Character)
					// 장비 해제.
					PolyDatabase.toEquipped((Character) o, p);
				// 변신
				o.setGfx(p.getGfxId());

				boolean check = false;

				ItemInstance Weapon = o.getInventory().getSlot(Lineage.SLOT_WEAPON);

				if (p.getGfxMode() == 20) {
					if (Weapon == null) {
						check = true;
					}
				}

				if (check) {
					o.setGfxMode(0);
				} else if (Weapon != null && p.getGfxMode() == 0 && (cha.getGfx()==17078 || cha.getGfx()==17083 || cha.getGfx()==19082 || cha.getGfx()==18942 || cha.getGfx()==21248
						|| cha.getGfx()==21066 || cha.getGfx()==17053 || cha.getGfx()==17058
						|| cha.getGfx()==13739 || cha.getGfx()==13741 || cha.getGfx()==22258 || cha.getGfx()==21832 || cha.getGfx()==17541 || (cha.getGfx()>=8922 && cha.getGfx()<=8956))) {
					o.setGfxMode(p.getGfxMode() + Weapon.getItem().getGfxMode());
				} else if (Weapon != null && p.getGfxMode() == 20 && Weapon.getItem().getType2().equalsIgnoreCase("bow") && (cha.getGfx()==17078 || cha.getGfx()==17083 || cha.getGfx()==19082 || cha.getGfx()==18942 || cha.getGfx()==21248
						|| cha.getGfx()==21066 || cha.getGfx()==17053 || cha.getGfx()==17058
						|| cha.getGfx()==13739 || cha.getGfx()==13741 || cha.getGfx()==22258 || cha.getGfx()==21832 || cha.getGfx()==17541 || (cha.getGfx()>=8922 && cha.getGfx()<=8956))) {
					o.setGfxMode(Weapon.getItem().getGfxMode());
				} else {
					o.setGfxMode(p.getGfxMode());
				}
				if (cha.getGfx() == 15660 || cha.getGfx() == 15657 || cha.getGfx()==3869 || cha.getGfx()==2377 || Lineage.server_version >= 270) {
					// 변신상태가 아닐때만 변경하도록 함.
					for (ItemInstance item : cha.getInventory().getSlot()) {
						if (item != null && item.getSlot() == 8
								&& item.getItem().getType2().equalsIgnoreCase("spear")) {
							cha.setGfxMode(cha.getGfxMode() + 24);
							cha.toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), cha), true);
						}
					}
				}

				if (packet) {
					o.toSender(S_ObjectGfx.clone(BasePacketPooling.getPool(S_ObjectGfx.class), o), true);
					if (Lineage.server_version > 182)
						o.toSender(S_ObjectPolyIcon.clone(BasePacketPooling.getPool(S_ObjectPolyIcon.class), time));
				}
				// 버프등록
				BuffController.append(o, ShapeChange.clone(BuffController.getPool(ShapeChange.class), SkillDatabase.find(10, 7), time));
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 13537), true);
			} else {
				if (packet)
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
			}
		}
	}

	static public boolean is변신을하거나해제해도되는가(object o, Poly p, boolean isCancel) {
		boolean check = true;
		
		// 예외변신 목록.
		if (p != null) {
			if (!isCancel && p.getPolyName().startsWith("Pumpkin ") || p.getPolyName().startsWith("horse "))
				return true;
		}

		if (o instanceof PcInstance) {			
			// 터번 사용중일땐 무시.
			ItemInstance helm = o.getInventory().getSlot(Lineage.SLOT_HELM);
			if (helm != null && helm instanceof Turban) {   // && helm instanceof 호박투구) {
				check = false;
			}
			// item_setoption 에 변신이 존재할경우 무시.
			for (ItemSetoption is : o.getInventory().getSetitemList()) {
				if (is.getPolymorph() != 0) {
					if (p == null || is.getPolymorph() != p.getGfxId())
						check = false;
				}
			}
		}
		// }
		return check;
	}
}

