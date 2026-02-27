package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class TripleArrow {
	
	static public boolean init(Character cha, object o){
		Skill skill = SkillDatabase.find(145);
		if(o!=null && SkillController.isMagic(cha, skill, true)&& cha.getInventory().getSlot(8).getItem().getType2().equalsIgnoreCase("bow")) {
			return true;
		}
		return false;
	}

	static public void init(Character cha, Skill skill, long object_id/*, int x, int y*/) {
		// 타겟 찾기
		object o = cha.findInsideList( object_id );
		if(o!=null && SkillController.isMagic(cha, skill, true)&& cha.getInventory().getSlot(8).getItem().getType2().equalsIgnoreCase("bow")) {
			// 
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			for(int i=0 ; i<3 ; ++i)
				//(object o, int x, int y, boolean bow, int gfxMode, int alpha_dmg, boolean isTriple, int tripleIdx){
				cha.toAttack(o, o.getX(), o.getY(), true, 0, 0, true, 0);
		}
	}
	
	static public void init(Character cha, Skill skill, long object_id, int x, int y) {
		// 타겟 찾기
		object o = cha.findInsideList( object_id );
		if(o!=null && SkillController.isMagic(cha, skill, true)&& cha.getInventory().getSlot(8).getItem().getType2().equalsIgnoreCase("bow")) {
			// 
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true); 
			for(int i=0 ; i<3 ; ++i)
				cha.toAttack(o, x, y, true, 0, 0, true, 0);
		}
	}
}
