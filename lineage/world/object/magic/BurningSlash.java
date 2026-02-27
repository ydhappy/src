package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class BurningSlash extends Magic {

	public BurningSlash(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new BurningSlash(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		//
		toBuffUpdate(o);
		//
		o.setBuffBurningSlash(true);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
	}

	@Override
	public boolean toBuffStop(object o) {
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		o.setBuffBurningSlash(false);
	}

	/**
	 * 물리데미지 처리중 버프에따른 데미지를 가중할때 호출해서 사용함.
	 * @param cha
	 * @param target
	 * @return
	 */
	@Override
	public int toDamagePlus(Character cha, object target) {
		if(cha.isBuffBurningSlash() && Util.random(0, 99)<=10) {
			target.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), target, skill.getCastGfx()+1), true);
			BuffController.remove(cha, BurningSlash.class);
			return 10;
		}
		return 0;
	}

	/**
	 * 사용자 용
	 * @param cha
	 * @param skill
	 * @param object_id
	 */
	static public void init(Character cha, Skill skill){
		// 처리
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		if(SkillController.isMagic(cha, skill, true))
			BuffController.append(cha, BurningSlash.clone(BuffController.getPool(BurningSlash.class), skill, skill.getBuffDuration()));
	}
}
