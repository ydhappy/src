package lineage.world.object.magic;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.bean.lineage.Clan;
import lineage.bean.lineage.Party;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ClanController;
import lineage.world.controller.PartyController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class GreaseAvt extends Magic {

	public GreaseAvt(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new GreaseAvt(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setBuffGreaseAvt( true );
		
		if(o instanceof Character){
			Character cha = (Character)o;
			cha.setDynamicStunDefense( cha.getDynamicStunDefense() + 5);
			
				
		}
		toBuffUpdate(o);
		
	}
	
	@Override
	public void toBuffUpdate(object o) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o){
		o.setBuffGreaseAvt( false );
		if(o instanceof Character){
			Character cha = (Character)o;
			cha.setDynamicStunDefense( cha.getDynamicStunDefense() - 5);
			
		}
	}
	
	
	static public void init(Character cha, Skill skill) {
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		if(SkillController.isMagic(cha, skill, true))
			BuffController.append(cha, GreaseAvt.clone(BuffController.getPool(GreaseAvt.class), skill, skill.getBuffDuration()));
	}

}
