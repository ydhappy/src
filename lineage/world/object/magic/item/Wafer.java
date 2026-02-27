package lineage.world.object.magic.item;

import lineage.bean.database.ItemSetoption;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectSpeed;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.magic.HolyWalk;
import lineage.world.object.magic.Magic;
import lineage.world.object.magic.Slow;

public class Wafer extends Magic {

	public Wafer(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new Wafer(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
	public Wafer(Skill skill, int time) {
		super(null, skill);
		setSkill(skill);
		setTime(time);
	}
	@Override
	public void toBuffStart(object o) {
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		o.setBrave(true);
		o.toSender(new S_ObjectSpeed( o, S_ObjectSpeed.BRAVE, Lineage.elven_wafer_frame ? 3 : Lineage.LINEAGE_CLASS_KNIGHT, getTime()), true);
	}

	@Override
	public boolean toBuffStop(object o) {
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		if (o.isWorldDelete())
			return;
		o.setBrave(false);
		o.toSender(new S_ObjectSpeed( o, S_ObjectSpeed.BRAVE, 0, 0), true);
	}
	
	@Override
	public void setTime(int time) {
		if (time > 0 && Lineage.skill_Wafer_update) {
			time = getTime() + time;
			// 60분까지만 축적되게.
			if ((60 * 60) < time)
				time = 60 * 60;
			//
			super.setTime(time);
		} else {
			super.setTime(time);
		}
	}
	
	static public void init(Character cha, int time){
		if (cha.getClassType() == Lineage.LINEAGE_CLASS_ELF) {
		//
		/*if(BuffController.find(cha, Slow.class) != null) {
			BuffController.remove(cha, Slow.class);
			return;
		}
		if(BuffController.find(cha, Entangle.class) != null) {
			BuffController.remove(cha, Entangle.class);
			return;
		}*/
		
		// 지혜 제거
//		BuffController.remove(cha, Wisdom.class);
		// 홀리워크 제거
//		BuffController.remove(cha, HolyWalk.class);
		// 용기 제거.
		BuffController.remove(cha, Bravery.class);
		// 슬로우 상태가 아닐경우
		BuffController.append(cha, new Wafer(SkillDatabase.find(200), time));
	}
	}
}
