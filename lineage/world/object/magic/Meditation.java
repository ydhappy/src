package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class Meditation extends Magic {
	
	public Meditation(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new Meditation(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		o.setBuffMeditation(true);
		if(o instanceof Character){
			Character cha = (Character)o;
			//cha.setDynamicTicMp( cha.getDynamicTicMp()+skill.getMaxdmg() );
			cha.setDynamicTicMp(cha.getDynamicTicMp() + 15);
			cha.setBuffMeditaitonLevel(0);
		}
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o){
		o.setBuffMeditation(false);
		
		if(o instanceof Character){
			Character cha = (Character)o;
			//cha.setDynamicTicMp( cha.getDynamicTicMp()-skill.getMaxdmg() );
			cha.setDynamicTicMp(cha.getDynamicTicMp() - 15);
			cha.setBuffMeditaitonLevel(0);
			ChattingController.toChatting(cha, "회복의 기운이 사라집니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
	
	static public void init(Character cha, Skill skill){
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		if(SkillController.isMagic(cha, skill, true))
			onBuff(cha, skill, skill.getBuffDuration());
	}
	
	static public void init(Character cha, Skill skill, long object_id){
		// 초기화
				object o = null;
				// 타겟 찾기
				if(object_id == cha.getObjectId())
					o = cha;
				else{
					ChattingController.toChatting(cha, "본인에게만 사용 가능합니다.");
					return;
				}
					
		if(o!=null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		}
		if(SkillController.isMagic(cha, skill, true))
			onBuff(cha, skill, skill.getBuffDuration());
	}
	
	static public void onBuff(object o, Skill skill, int time){
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		BuffController.append(o, Meditation.clone(BuffController.getPool(Meditation.class), skill, time));
		ChattingController.toChatting(o, "회복의 기운이 느껴집니다.", Lineage.CHATTING_MODE_MESSAGE);
	}
	
}
