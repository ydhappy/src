package jsn_soft;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.magic.Magic;


public class Kingdom_kent_buff extends Magic {

	public Kingdom_kent_buff(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new Kingdom_kent_buff(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		// 지속시간:30분, 추타3 공성3 활타격치3 활명중치3 주술력3
		if(o instanceof Character){
			Character cha = (Character)o;
			cha.setDynamicAddDmg( cha.getDynamicAddDmg() + 1 );
			cha.setDynamicAddDmgBow( cha.getDynamicAddDmgBow() + 1 );
			cha.setDynamicAddHit( cha.getDynamicAddHit() +1);
			cha.setDynamicAddHitBow( cha.getDynamicAddHitBow() +1);
			cha.setDynamicAc(cha.getDynamicAc() + 2);
		}
		
		ChattingController.toChatting(o, "켄트성 버프의 효과가 당신을 감쌉니다.", 20);
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
			cha.setDynamicAddDmg( cha.getDynamicAddDmg() - 1 );
			cha.setDynamicAddDmgBow( cha.getDynamicAddDmgBow() - 1 );
			cha.setDynamicAddHit( cha.getDynamicAddHit() -1);
			cha.setDynamicAddHitBow( cha.getDynamicAddHitBow() -1);
			cha.setDynamicAc(cha.getDynamicAc() - 2);
		}
		
		ChattingController.toChatting(o, "당신의 기분이 원래대로 돌아왔습니다.", 20);
	}
  
	@Override
	public void toBuff(object o) {
		if(Lineage.potion_message > 0 && getTime() <= Lineage.potion_message)
			ChattingController.toChatting(o, "\\fY켄트성 버프 종료까지 "+getTime()+"초 남았습니다.", 20);
	}

	static public void init(Character cha){
		Skill s = SkillDatabase.find(50017);
		if (s.getCastGfx() > 0)
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, s.getCastGfx()), true);
		if(s != null)
			BuffController.append(cha, Kingdom_kent_buff.clone(BuffController.getPool(Kingdom_kent_buff.class), s, s.getBuffDuration()));
	}

}
