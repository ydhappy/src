package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.DamageController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class Tomahawk extends Magic {
	
	public Tomahawk(Character cha, Skill skill){
		super(cha, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Character cha, Skill skill, int time){
		if(bi == null)
			bi = new Tomahawk(cha, skill);
		bi.setSkill(skill);
		bi.setTime(time);
		bi.setCharacter(cha);
		return bi;
	}
	
	@Override
	public void toBuff(object o) {
		//
		if(cha==null || cha.isWorldDelete())
			return;
		//
		DamageController.toDamage(cha, o, cha.getLevel()/2, 0);
		o.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), o, 2), true);
	}

	static public void init(Character cha, Skill skill, long object_id, int x, int y) {
		// 타겟 찾기
		object o = cha.findInsideList( object_id );
		// 처리
		if(o != null) {
			//
			if(SkillController.isMagic(cha, skill, true) && Util.isDistance(cha, o, 10) && EnergyBolt.toBuff(cha, o, skill, 18, skill.getCastGfx(), 0)>0)
				BuffController.append(o, Tomahawk.clone(BuffController.getPool(Tomahawk.class), cha, skill, skill.getBuffDuration()));
		}
		
	}

}
