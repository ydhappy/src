package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class UncannyDodge extends Magic {
	
	public UncannyDodge(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new UncannyDodge(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		/*if(PluginController.init(UncannyDodge.class, "toBuffStart", o) != null)
			return;*/
		
		if(o instanceof Character) {
			Character cha = (Character)o;
			cha.setDynamicDg( cha.getDynamicDg() + skill.getMaxdmg() );
			cha.setDodg(true);
		}
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o){
		/*if(PluginController.init(UncannyDodge.class, "toBuffEnd", o) != null)
			return;*/
		
		if(o instanceof Character) {
			Character cha = (Character)o;
			cha.setDynamicDg( cha.getDynamicDg() - skill.getMaxdmg() );
			cha.setDodg(false);
		}
	}

	static public void init(Character cha, Skill skill){
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		if(SkillController.isMagic(cha, skill, true))
			onBuff(cha, skill);
	}
	
	static public void onBuff(object o, Skill skill) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		BuffController.append(o, UncannyDodge.clone(BuffController.getPool(UncannyDodge.class), skill, skill.getBuffDuration()));
	}

}
