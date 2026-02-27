package lineage.world.object.magic.item;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.world.controller.BuffController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.magic.Magic;

public class FloatingEyeMeat extends Magic {

	public FloatingEyeMeat(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new FloatingEyeMeat(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
	
	@Override
	public void toBuffStart(object o){
		o.setBuffMonsterEyeMeat(true);
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o){
		o.setBuffMonsterEyeMeat(false);
	}

	static public void init(Character cha, int time){
		BuffController.append(cha, FloatingEyeMeat.clone(BuffController.getPool(FloatingEyeMeat.class), SkillDatabase.find(205), time));
	}
}
