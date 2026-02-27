package lineage.world.object.magic.monster;

import lineage.bean.database.MonsterSkill;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectPoisonLock;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.magic.Magic;

public class CurseGhast extends Magic {

	public CurseGhast(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new CurseGhast(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		o.setBuffSilence(true);
		o.setBuffCurseGhoul(true);
		o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 310));
		o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o){
		if(o.isWorldDelete())
			return;
		o.setBuffSilence(false);
		o.setBuffCurseGhoul(false);
		o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 311));
		o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
	}
	
	/**
	 * 월드 접속할때 해당 객체 버프 적용하기 위한것
	 * @param cha
	 * @param time
	 */
	static public void init(Character cha, int time){
		BuffController.append(cha, CurseGhast.clone(BuffController.getPool(CurseGhast.class), SkillDatabase.find(100, 2), time));
	}

	static public void init(MonsterInstance mi, object o, MonsterSkill ms, int action, int effect, boolean check){
		if(action != -1)
			mi.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), mi, action), false);
		if (o.getInventory().isRingZnis())
			return;
		/*if(check && !SkillController.isMagic(mi, ms, true) && !SkillController.isFigure(mi, o, ms, true, false))
			return;*/
		if(SkillController.isFigure(mi, o, ms.getSkill(), true, false)){
			if(effect > 0)
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, effect), true);
			BuffController.append(o, CurseGhast.clone(BuffController.getPool(CurseGhast.class), ms.getSkill(), ms.getBuffDuration()));
		}
	}

}
