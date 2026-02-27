package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectLock;
import lineage.network.packet.server.S_ObjectPoisonLock;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.BackgroundInstance;

public class PowerGrip extends Magic {
	
	public PowerGrip(Skill skill){
		super(null, skill);

		chain = new lineage.world.object.npc.background.PowerGrip();
		chain.setGfx(12531);
		chain.setGfxMode(1);
		chain2 = new lineage.world.object.npc.background.PowerGrip();
		chain2.setGfx(12533);
		chain2.setGfxMode(1);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new PowerGrip(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	private BackgroundInstance chain;
	private BackgroundInstance chain2;
	private int counter;

	@Override
	public void toBuffStart(object o) {
		counter = 3;
		chain.setObjectId(ServerDatabase.nextEtcObjId());
		chain.toTeleport(o.getX(), o.getY(), o.getMap(), false);
		//
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		// 굳게 만들기.
		if(!o.isLockLow()) {
			o.setLockLow(true);
			o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
			o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 26));
		}
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}
	
	@Override
	public void toBuff(object o) {
		if(counter < 0)
			return;
		counter -= 1;
		if(counter == 1) {
			chain2.setObjectId(ServerDatabase.nextEtcObjId());
			chain2.toTeleport(o.getX(), o.getY(), o.getMap(), false);
		}
		if(counter == 0) {
			World.remove(chain);
			chain.clearList(true);
		}
	}

	@Override
	public void toBuffEnd(object o){
		//
		if(!chain.isWorldDelete()) {
			World.remove(chain);
			chain.clearList(true);
		}
		if(!chain2.isWorldDelete()) {
			World.remove(chain2);
			chain2.clearList(true);
		}
		//
		if(o.isWorldDelete() || !o.isLock())
			return;
		//
		o.setLockLow(false);
		o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
		o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 27));
	}

	static public void init(Character cha, Skill skill, long object_id, int x, int y) {
		// 타겟 찾기
		object o = cha.findInsideList( object_id );
		// 처리
		if(o != null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
			if(SkillController.isMagic(cha, skill, true) && Util.isDistance(cha, o, 6) && Util.isAreaAttack(cha, o)) {
				// 투망상태 해제
				Detection.onBuff(cha);
				// 공격당한거 알리기.
				o.toDamage(cha, 0, 2);
	
				// 처리
				if(SkillController.isFigure(cha, o, skill, true, false) && !o.isLock()){
					BuffController.append(o, PowerGrip.clone(BuffController.getPool(PowerGrip.class), skill, skill.getBuffDuration()));
					return;
				}
				// \f1마법이 실패했습니다.
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 280));
			}
		}
	}
	
}
