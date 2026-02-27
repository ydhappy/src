package lineage.world.object.magic.sp;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.Magic;

public class 마력증강마법 extends Magic {

	public 마력증강마법(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Character cha, Skill skill, int time) {
		if (bi == null)
			bi = new 마력증강마법(skill);

		bi.setCharacter(cha);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if(o instanceof Character){
			Character cha = (Character)o;
			cha.setDynamicMp(cha.getDynamicMp() + 40);
			cha.setDynamicTicMp(cha.getDynamicTicMp() + 4);
		}
		ChattingController.toChatting(o, "마력 증강의 주문서: 최대 MP+40, MP 회복률+4", Lineage.CHATTING_MODE_MESSAGE);
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		if(o instanceof Character){
			Character cha = (Character)o;
			cha.setDynamicMp(cha.getDynamicMp() - 40);
			cha.setDynamicTicMp(cha.getDynamicTicMp() - 4);
		}
			ChattingController.toChatting(o, "당신의 기분이 원래대로 돌아왔습니다.", Lineage.CHATTING_MODE_MESSAGE);
	}

	@Override
	public void toBuff(object o) {
		if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
			ChattingController.toChatting(o, "마력 증강의 주문서: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
}
	
	static public void init(Character cha, int time) {
		Skill s = SkillDatabase.find(10002);
		if(s != null)
		BuffController.append(cha, 마력증강마법.clone(BuffController.getPool(마력증강마법.class), cha, s, time));
	}

	static public void onBuff(Character cha, Skill skill, int time, boolean restart) {
		Skill s = SkillDatabase.find(10002);
		
		
		if (!restart)
			time = BuffController.addBuffTime(cha, skill);
		
		if(s != null)
			BuffController.append(cha, 마력증강마법.clone(BuffController.getPool(마력증강마법.class), cha, s, s.getBuffDuration()));
	}
}
