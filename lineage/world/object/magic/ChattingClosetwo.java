package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class ChattingClosetwo extends Magic {

	public ChattingClosetwo(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new ChattingClosetwo(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		o.setBuffChattingClosetwo(true);
	}

	@Override
	public boolean toBuffStop(object o){
		return false;
	}

	@Override
	public void toBuffEnd(object o){
		// 당신은 이제 채팅을 할 수 있습니다.
		ChattingController.toChatting(o, "\\fY 일반 채팅이 가능 하게 되었습니다.", 20);
		o.setBuffChattingClosetwo(false);
	}
	
	static public void init(Character cha, int time){
		// \f3게임에 적합하지 않은 행동으로 인해 앞으로 %0분간 채팅이 금지됩니다.
		BuffController.append(cha, ChattingClosetwo.clone(BuffController.getPool(ChattingClosetwo.class), SkillDatabase.find(100, 1), time*10));
	}
	
	static public void init(Character cha, int time, boolean message){
		if(message)
			// 현재 채팅 금지중입니다.
			ChattingController.toChatting(cha, "\\fY 외창금지법으로 일정 시간 동안 일반 채팅을 하실수 없습니다.", 20);
		BuffController.append(cha, ChattingClosetwo.clone(BuffController.getPool(ChattingClosetwo.class), SkillDatabase.find(100, 1), time));
	}

}
