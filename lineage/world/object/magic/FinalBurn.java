package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class FinalBurn {

	static public void init(Character cha, Skill skill, long object_id){
		// 초기화
		object o = cha.findInsideList( object_id );
		// 처리
		if(o!=null && SkillController.isMagic(cha, skill, true)){
			int dmg = EnergyBolt.toBuff(
					cha, 
					o, 
					skill, 
					18, 
					skill.getCastGfx(), 
//					cha.getNowHp()+cha.getNowMp()
					getAlphaDamage(cha)
			);
			if(dmg > 0) {
				cha.setNowHp(1);
				cha.setNowMp(1);
			}
		}
	}
	
	static private int getAlphaDamage(Character cha) {
		if(cha.getLevel() < 50)
			return Util.random(150, 400);
		else if(cha.getLevel() < 55)
			return Util.random(250, 500);
		else if(cha.getLevel() < 60)
			return Util.random(350, 600);
		else if(cha.getLevel() < 65)
			return Util.random(450, 700);
		else if(cha.getLevel() < 70)
			return Util.random(550, 800);
		else
			return Util.random(600, 1000);
	}
}
