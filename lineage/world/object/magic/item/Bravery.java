package lineage.world.object.magic.item;

import lineage.bean.database.ItemSetoption;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.server.S_ObjectSpeed;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.magic.Magic;

public class Bravery extends Magic {

	public Bravery(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new Bravery(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
	public Bravery(Skill skill,int time) {
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
		switch(o.getClassType()) {
			case Lineage.LINEAGE_CLASS_ROYAL:
				o.toSender(new S_ObjectSpeed( o, S_ObjectSpeed.BRAVE, Lineage.devil_potion_frame ? 3 : 1, getTime()), true);
				break;
			default:
				o.toSender(new S_ObjectSpeed( o, S_ObjectSpeed.BRAVE, Lineage.bravery_potion_frame ? Lineage.LINEAGE_CLASS_KNIGHT : 3, getTime()), true);
				break;
		}
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
//		ChattingController.toChatting(o, "당신의 기분이 보통으로 돌아왔습니다.", Lineage.CHATTING_MODE_MESSAGE);
	}

	@Override
	public void setTime(int time) {
		if (time > 0 && Lineage.skill_Bravery_update) {
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

	static public void init(Character cha, int time) {
		switch(cha.getClassType()) {
		case Lineage.LINEAGE_CLASS_ROYAL:
		BuffController.append(cha, new Bravery(SkillDatabase.find(503), time));
		break;
		case Lineage.LINEAGE_CLASS_KNIGHT:
			BuffController.append(cha, new Bravery(SkillDatabase.find(201), time));
			break;
		}
	}
}
