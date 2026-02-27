package lineage.world.object.magic;

import lineage.bean.database.MonsterSkill;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.ServerDatabase;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectAttack;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectHeading;
import lineage.network.packet.server.S_ObjectLock;
import lineage.network.packet.server.S_ObjectPoisonLock;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BuffController;
import lineage.world.controller.DamageController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class ShockStun extends Magic {

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new ShockStun(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	private BackgroundInstance stun;
	//private object stun;
	
	public ShockStun(Skill skill) {
		super(null, skill);
		stun = new lineage.world.object.npc.background.ShockStun();
	}

	@Override
	public void toBuffStart(object o) {
	 {
		if (getTime()<=4)
			stun.setGfx(11727);
		else if (getTime()>=5)
			stun.setGfx(11727);

		this.stun.setObjectId(ServerDatabase.nextEtcObjId());
		this.stun.toTeleport(o.getX(), o.getY(), o.getMap(), false);

		o.setLockLow(true);

		o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 2));
		o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 999));
		o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
	  }
	}
	@Override
	public boolean toBuffStop(object o) {
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		//
		World.remove(stun);
		stun.clearList(true);

		if (o.isWorldDelete() || !o.isLock())
			return;
		o.setLockLow(false);
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 15102), false);
		o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0));
		o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
	}

	@Override
	public void setTime(int time) {
		/*
		 * if(time > 0) { time = getTime() + time; super.setTime( time ); } else {
		 */
		super.setTime(time);
		// }
	}

	static public void init(object o, long time) {
		Skill skill = SkillDatabase.find(121);

		if (o != null) {
			BuffController.append(o, ShockStun.clone(BuffController.getPool(ShockStun.class), skill, (int)time));
		}

	}

	/**
	 * 
	 * @param cha
	 * @param skill
	 * @param object_id
	 * @param x
	 * @param y
	 */
	static public void init(Character cha, Skill skill, long object_id, int x, int y) {

		// 타겟 찾기
		object o = cha.findInsideList(object_id);
		// 처리

		if (o != null && Util.isDistance(cha, o, 2) && Util.isAreaAttack(cha, o)) {
			ItemInstance weapon = cha.getInventory().getSlot(8);
			if (weapon != null && weapon.getItem().getType2().equalsIgnoreCase("tohandsword")
					&& SkillController.isMagic(cha, skill, true)) {
            // 공격 패킷을 사용해야함..
				if (Lineage.server_version < 270)
					cha.toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), cha, o,
							cha.getGfxMode() + 1, 0, 0, false, false, o.getX(), o.getY(), true), true);
				else
					cha.toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), cha, o, 1, 0, 0,
							false, false, o.getX(), o.getY(), true), true);
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 15101), true);
				Detection.onBuff(cha);
				if (SkillController.isFigure(cha, o, skill, true, false)) {
					BuffController.append(o, ShockStun.clone(BuffController.getPool(ShockStun.class), skill,
							Util.random(skill.getMindmg(), skill.getMaxdmg())));
				}
			}
		}
	}

	// 자동스턴에서 쓰는중.
	static public void init(Character cha, object o) {
		Skill skill = SkillDatabase.find(121);

		if (o != null && Util.isDistance(cha, o, 2) && Util.isAreaAttack(cha, o)
				&& SkillController.isMagic(cha, skill, true)) {

			if (Lineage.server_version < 270)
				cha.toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), cha, o,
						cha.getGfxMode() + 1, 0, 0, false, false, o.getX(), o.getY(), true), true);
			else
				cha.toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), cha, o, 1, 0, 0,
						false, false, o.getX(), o.getY(), true), true);
			o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
			Detection.onBuff(cha);
				int ti = Util.random(1,100) > 10 + (cha.getDynamicStunLevel()) ? Util.random(1, 3) : Util.random(2, 5);
				BuffController.append(o, ShockStun.clone(BuffController.getPool(ShockStun.class), skill, ti));
			}

		}

	/**
	 * 몬스터용 : 장로가 사용중. : 다크마르가 사용중.
	 * 
	 * @param cha
	 * @param o
	 * @param skill
	 * @param action
	 */
	static public void init(MonsterInstance mi, object o, MonsterSkill ms, int action, int effect) {
		if (action != -1) {
			mi.setHeading(Util.calcheading(mi, o.getX(), o.getY()));
			mi.toSender(S_ObjectHeading.clone(BasePacketPooling.getPool(S_ObjectHeading.class), mi), false);
			mi.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), mi, action), false);
		}
		if (effect > 0)
			o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, effect), true);
		if (SkillController.isMagic(mi, ms, true) && SkillController.isFigure(mi, o, ms.getSkill(), true, false)
				&& !o.isLock())
			BuffController.append(o,
					ShockStun.clone(BuffController.getPool(ShockStun.class), ms.getSkill(), ms.getBuffDuration()));
	}
}