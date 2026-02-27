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

public class GuardBreak extends Magic {

	public GuardBreak(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new GuardBreak(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		//
		toBuffUpdate(o);
		//
		if(o instanceof Character) {
			Character cha = (Character)o;
			cha.setDynamicAc( cha.getDynamicAc() - 10 );
		}
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
		//
		if(o instanceof Character) {
			Character cha = (Character)o;
			cha.setDynamicAc( cha.getDynamicAc() + 10 );
		}
	}

	static public void init(Character cha, Skill skill, long object_id) {
		// 초기화
		object o = cha.findInsideList( object_id );
		// 처리
		if(o != null) {
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
			if(SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, o, skill, true, false))
				BuffController.append(o, GuardBreak.clone(BuffController.getPool(GuardBreak.class), skill, skill.getBuffDuration()));
		}
	}
	
}
