package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class ReductionArmor extends Magic {
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new ReductionArmor(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
	
	public ReductionArmor(Skill skill){
		super(null, skill);
	}
	
	@Override
	public void toBuffStart(object o) {
		ChattingController.toChatting(o, "물러서지 않겠다는 기사의 결의가 느껴집니다.", 20);
		o.setBuffReductionArmor(true);
		if(o instanceof PcInstance){
			PcInstance pc = (PcInstance)o;
			pc.setReduction(pc.getReduction()+skill.getMindmg());
		}
	}

	@Override
	public boolean toBuffStop(object o) {
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		ChattingController.toChatting(o, "기사의 결의가 사라져갑니다.", 20);
		o.setBuffReductionArmor(false);
		if(o instanceof PcInstance){
			PcInstance pc = (PcInstance)o;
			pc.setReduction(pc.getReduction()-skill.getMindmg());
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
	static public void init(Character cha, Skill skill) {
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		if(SkillController.isMagic(cha, skill, true))
			onBuff(cha, skill);
	}
	
	static public void onBuff(object o, Skill skill) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		BuffController.append(o, ReductionArmor.clone(BuffController.getPool(ReductionArmor.class), skill, skill.getBuffDuration()));
	}

}
