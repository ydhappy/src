package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAttack;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.DamageController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class MagmaBreath {

	static public void init(Character cha, Skill skill, long object_id) {
		// 
		object o = cha.findInsideList( object_id );
		if(o!=null && Util.isDistance(cha, o, 5) && SkillController.isMagic(cha, skill, true)) {
			// 데미지 처리
			int dmg = SkillController.getDamage(cha, o, o, skill, 0, skill.getElement());
			DamageController.toDamage(cha, o, dmg, 2);
			
			cha.toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), cha, o, 1, dmg, skill.getCastGfx(), false, false, o.getX(), o.getY(), true), true);
		}
	}
	
}
