package lineage.world.object.magic.item;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.magic.Magic;

public class Blue extends Magic {

	public Blue(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new Blue(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
	
	@Override
	public void toBuffUpdate(object o) {
		// 머리 속이 맑아지며 마력이 충만해집니다.
		if(Lineage.server_version >= 182)
			o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 703));
		else
			ChattingController.toChatting(o, "머리 속이 맑아지며 마력이 충만해집니다.", 20);
	}
		
	@Override
	public void toBuffStart(object o){
		if(o instanceof Character){
			Character cha = (Character)o;
			cha.setDynamicTicMp(cha.getDynamicTicMp()+8);
		}
		//
		toBuffUpdate(o);
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
			cha.setDynamicTicMp(cha.getDynamicTicMp()-8);
		}
		// 충만하던 마력이 사라집니다.
		if(Lineage.server_version >= 182)
			o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 722));
		else
			ChattingController.toChatting(o, "충만하던 마력이 사라집니다.", 20);
	}
	
	static public void init(Character cha, int time){
		BuffController.remove(cha, Blue2.class);
		BuffController.append(cha, Blue.clone(BuffController.getPool(Blue.class), SkillDatabase.find(204), time));
	}
}
