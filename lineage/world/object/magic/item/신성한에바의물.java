package lineage.world.object.magic.item;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.world.controller.BuffController;
import lineage.world.object.Character;
import lineage.world.object.magic.Magic;

public class 신성한에바의물 extends Magic {

	public 신성한에바의물(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new 신성한에바의물(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
	
	static public void init(Character cha, int time){
		BuffController.append(cha, 신성한에바의물.clone(BuffController.getPool(신성한에바의물.class), SkillDatabase.find(207), time));
	}

}
