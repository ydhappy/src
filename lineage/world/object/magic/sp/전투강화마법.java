package lineage.world.object.magic.sp;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.magic.Magic;

public class 전투강화마법 extends Magic {

	public 전투강화마법(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Character cha, Skill skill, int time) {
		if (bi == null)
			bi = new 전투강화마법(skill);

		bi.setCharacter(cha);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		// 지속시간:30분, 추타2 공성2 활타격치2 활명중치2 주술력2
		if(o instanceof Character){
			Character cha = (Character)o;
			cha.setDynamicAddDmg( cha.getDynamicAddDmg()+2 );
			cha.setDynamicAddHit( cha.getDynamicAddHit()+2 );
			cha.setDynamicAddDmgBow( cha.getDynamicAddDmgBow()+2 );
			cha.setDynamicAddHitBow( cha.getDynamicAddHitBow()+2 );
			cha.setDynamicSp( cha.getDynamicSp()+2 );
		}
		ChattingController.toChatting(o, "전투 강화의 주문서: 추가 대미지+2, 추가 명중+2, 주술력+2", Lineage.CHATTING_MODE_MESSAGE);
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}
	
	@Override
	public void toBuffEnd(object o){
		if(o instanceof Character){
			Character cha = (Character)o;
			cha.setDynamicAddDmg( cha.getDynamicAddDmg()-2 );
			cha.setDynamicAddHit( cha.getDynamicAddHit()-2 );
			cha.setDynamicAddDmgBow( cha.getDynamicAddDmgBow()-2 );
			cha.setDynamicAddHitBow( cha.getDynamicAddHitBow()-2 );
			cha.setDynamicSp( cha.getDynamicSp()-2 );
		}
			ChattingController.toChatting(o, "당신의 기분이 원래대로 돌아왔습니다.", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	@Override
	public void toBuff(object o) {
		if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
			ChattingController.toChatting(o, "전투 강화의 주문서: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
}
	
	static public void init(Character cha, int time) {
		Skill s = SkillDatabase.find(10001);
		if(s != null)
		BuffController.append(cha, 전투강화마법.clone(BuffController.getPool(전투강화마법.class), cha, s, time));
	}

	static public void onBuff(Character cha, Skill skill, int time, boolean restart) {
		Skill s = SkillDatabase.find(10001);
		
		
		if (!restart)
			time = BuffController.addBuffTime(cha, skill);
		
		if(s != null)
			BuffController.append(cha, 전투강화마법.clone(BuffController.getPool(전투강화마법.class), cha, s, s.getBuffDuration()));
	}
}