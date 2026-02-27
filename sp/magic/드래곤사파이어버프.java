package sp.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_SoundEffect;
import lineage.share.Common;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.Magic;

public class 드래곤사파이어버프 extends Magic {


	
	public 드래곤사파이어버프(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Character cha, Skill skill, int time) {
		if (bi == null)
			bi = new 드래곤사파이어버프(skill);

		bi.setCharacter(cha);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		o.setBuffExpPotion4(true);
		ChattingController.toChatting(cha, "경험치 획득량이 150% 증가합니다.", Lineage.CHATTING_MODE_MESSAGE);
	}

	@Override
	public void toBuffUpdate(object o) {
		o.setBuffExpPotion4(false);
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}
	
	@Override
	public void toBuff(object o) {
		if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		ChattingController.toChatting(o, "경험치 150% 증가: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	@Override
	public void toBuffEnd(object o) {
		o.setBuffExpPotion4(false);
		ChattingController.toChatting(o, "경험치 획득량이 원래대로 돌아왔습니다.", Lineage.CHATTING_MODE_MESSAGE);
	}


	static public void init(Character cha, int time) {
		Skill s = SkillDatabase.find(213);
		if(s != null)
		BuffController.append(cha, 드래곤사파이어버프.clone(BuffController.getPool(드래곤사파이어버프.class), cha, s, time));
	}


	static public void onBuff(Character cha, Skill skill, int time, boolean restart) {
		Skill s = SkillDatabase.find(213);
	
		
		if (!restart)
			time = BuffController.addBuffTime(cha, skill);
		
		if (skill.getCastGfx() > 0)
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
		if(s != null)
		BuffController.append(cha, 드래곤사파이어버프.clone(BuffController.getPool(드래곤사파이어버프.class), cha, s, s.getBuffDuration()));
		//ChattingController.toChatting(cha, "경험치 획득량이 150% 증가합니다.", Lineage.CHATTING_MODE_MESSAGE);
	
	}

}
