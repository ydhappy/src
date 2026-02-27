package lineage.world.object.magic;

import lineage.bean.database.ItemSetoption;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectSpeed;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.magic.item.Wafer;
import lineage.world.object.magic.item.Wisdom;

public class HolyWalk extends Magic {

	public HolyWalk(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new HolyWalk(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		o.setBrave(true);
		if(Lineage.server_version > 200)
			o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, S_ObjectSpeed.BRAVE, Lineage.holywalk_frame ? 4 : 0x01, getTime()), true);
		else
			o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, S_ObjectSpeed.BRAVE, Lineage.holywalk_frame ? 0x03 : 0x01, getTime()), true);
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o){
		if(o.isWorldDelete())
			return;
		o.setBrave(false);
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, S_ObjectSpeed.BRAVE, 0, 0), true);
	}
	
	@Override
	public void toBuff(object o) {
		if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
			ChattingController.toChatting(o, "홀리 워크: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
		
		if (getTime() == 1)
			o.speedCheck = System.currentTimeMillis() + 2000;
	}
	
	static public void init(Character cha, Skill skill){
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		if(SkillController.isMagic(cha, skill, true)){
			// 패킷 처리
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			// 처리.
			if(cha.getSpeed() != 2){
				// 지혜 제거
				BuffController.remove(cha, Wisdom.class);
				// 와퍼 제거
			//	BuffController.remove(cha, Wafer.class);
				// 슬로우 상태가 아닐경우
				BuffController.append(cha, HolyWalk.clone(BuffController.getPool(HolyWalk.class), skill, skill.getBuffDuration()));
			}else{
				// 슬로우 상태일경우 슬로우 제거.
				BuffController.remove(cha, Slow.class);
			}
		}
	}
	
	static public void init(Character cha, int time){
		if (cha.getClassType() == Lineage.LINEAGE_CLASS_WIZARD)
		// 지혜 제거
//		BuffController.remove(cha, Wisdom.class);
		// 와퍼 제거
	//	BuffController.remove(cha, Wafer.class);
		// 적용
		BuffController.append(cha, HolyWalk.clone(BuffController.getPool(HolyWalk.class), SkillDatabase.find(7, 3), time));
	}
	static public void onBuff(object o, Skill skill) {
		//
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		//
		BuffController.remove(o, HolyWalk.class);
		//
		BuffController.append(o, HolyWalk.clone(BuffController.getPool(HolyWalk.class), skill, skill.getBuffDuration()));
		
		ChattingController.toChatting(o, "홀리 워크: 이동속도 향상", Lineage.CHATTING_MODE_MESSAGE);
	}

}
