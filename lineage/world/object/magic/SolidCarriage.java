package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class SolidCarriage extends Magic {
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new SolidCarriage(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	public SolidCarriage(Skill skill){
		super(null, skill);
	}
	
	@Override
	public void toBuffStart(object o) {
		//
		o.setBuffSolidCarriage(true);
		//
		if(o instanceof Character) {
			Character cha = (Character)o;
			cha.setDynamicEr( cha.getDynamicEr() + skill.getMaxdmg() );
		}
	}

	@Override
	public boolean toBuffStop(object o) {
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		if(o.isWorldDelete())
			return;
		//
		o.setBuffSolidCarriage(false);
		//
		if(o instanceof Character) {
			Character cha = (Character)o;
			cha.setDynamicEr( cha.getDynamicEr() - skill.getMaxdmg() );
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
		if(cha.getInventory().getSlot(7)!=null && SkillController.isMagic(cha, skill, true))
			onBuff(cha, skill);
		
	}

	static public void onBuff(object o, Skill skill) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		BuffController.append(o, SolidCarriage.clone(BuffController.getPool(SolidCarriage.class), skill, skill.getBuffDuration()));
	}

}
