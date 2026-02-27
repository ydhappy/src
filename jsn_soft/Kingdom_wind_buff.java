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


public class Kingdom_wind_buff extends Magic {

	public Kingdom_wind_buff(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new Kingdom_wind_buff(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		// 지속시간:30분, 추타3 공성3 활타격치3 활명중치3 주술력3
		if(o instanceof Character){
			Character cha = (Character)o;
			cha.setDynamicAc( cha.getDynamicAc()+5 );
		}
		
		ChattingController.toChatting(o, "윈다우드성 버프의 효과가 당신을 감쌉니다.", 20);
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
			cha.setDynamicAc( cha.getDynamicAc()-5 );
		}
		
		ChattingController.toChatting(o, "당신의 기분이 원래대로 돌아왔습니다.", 20);
	}

	@Override
	public void toBuff(object o) {
		if(Lineage.potion_message > 0 && getTime() <= Lineage.potion_message)
			ChattingController.toChatting(o, "\\fY윈다우드성 버프 종료까지 "+getTime()+"초 남았습니다.", 20);
	}

	static public void init(Character cha){
		Skill s = SkillDatabase.find(50018);
		if (s.getCastGfx() > 0)
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, s.getCastGfx()), true);
		if(s != null)
			BuffController.append(cha, Kingdom_wind_buff.clone(BuffController.getPool(Kingdom_wind_buff.class), s, s.getBuffDuration()));
	}

}
