package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
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

public class Desperado extends Magic {

	public Desperado(Skill skill){
		super(null, skill);

		axe = new lineage.world.object.npc.background.Desperado();
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new Desperado(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
	
	private BackgroundInstance axe;

	@Override
	public void toBuffStart(object o) {
		axe.setObjectId(ServerDatabase.nextEtcObjId());
		axe.setGfx(skill.getCastGfx() + 2);
		axe.toTeleport(o.getX(), o.getY(), o.getMap(), false);
		//
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		o.setBuffDesperado(true);
		// 굳게 만들기.
		if(!o.isLockLow()) {
			o.setLockLow(true);
			o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
			o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 30));
		}
	}

	@Override
	public boolean toBuffStop(object o) {
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		if(!axe.isWorldDelete()) {
			World.remove(axe);
			axe.clearList(true);
		}
		//
		if(o.isWorldDelete() || !o.isLock())
			return;
		//
		o.setBuffDesperado(false);
		o.setLockLow(false);
		o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
		o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 31));
	}

	static public void init(Character cha, Skill skill, long object_id, int x, int y) {
		// 타겟 찾기
		object o = cha.findInsideList( object_id );
		// 처리
		if(o != null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
			if(SkillController.isMagic(cha, skill, true) && Util.isDistance(cha, o, 1) && Util.isAreaAttack(cha, o)) {
				// 투망상태 해제
				Detection.onBuff(cha);
				// 공격당한거 알리기.
				o.toDamage(cha, 0, 2);
				
				// 처리
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
				if(SkillController.isFigure(cha, o, skill, true, false) && !o.isLock()){
					BuffController.append(o, Desperado.clone(BuffController.getPool(Desperado.class), skill, skill.getBuffDuration()));
					return;
				}
				// \f1마법이 실패했습니다.
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 280));
			}
		}
		
	}
	
}
