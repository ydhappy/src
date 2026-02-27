package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.magic.item.Criminal;

public class ImmuneToHarm extends Magic {

	public ImmuneToHarm(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new ImmuneToHarm(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setBuffImmuneToHarm(true);
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o){
		o.setBuffImmuneToHarm(false);
		ChattingController.toChatting(o, "이뮨 투함 마법이 종료 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	@Override
	public void toBuff(object o) {
		if(Lineage.buff_ImmuneToHarm_message>0 && getTime()<=Lineage.buff_ImmuneToHarm_message)
			ChattingController.toChatting(o, "이뮨 투함 종료까지 "+getTime()+"초 남았습니다.", 20);
	}
	
	static public void init(Character cha, Skill skill, long object_id){
		// 초기화
		object o = null;
		// 타겟 찾기
		if(object_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList( object_id );
		// 처리
		if(o != null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
			if(SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, o, skill, false, SkillController.isClan(cha, o))){
				//
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
				BuffController.append(o, ImmuneToHarm.clone(BuffController.getPool(ImmuneToHarm.class), skill, skill.getBuffDuration()));
				//
				if(o.isBuffCriminal() && !cha.isBuffCriminal())
					Criminal.onBuff(cha, SkillDatabase.find(206));
			}
		}
	}
	
	static public void init(Character cha, int time){
		BuffController.append(cha, ImmuneToHarm.clone(BuffController.getPool(ImmuneToHarm.class), SkillDatabase.find(9, 3), time));
	}
	
	static public void onBuff(object o, Skill skill){
		onBuff(o, skill, skill.getBuffDuration());
	}
	
	static public void onBuff(object o, Skill skill, int time){
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		BuffController.append(o, ImmuneToHarm.clone(BuffController.getPool(ImmuneToHarm.class), skill, time));
	}
}
