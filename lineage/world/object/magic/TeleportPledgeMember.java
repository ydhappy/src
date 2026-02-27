package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.Clan;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.share.Lineage;
import lineage.world.controller.ClanController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.instance.PcInstance;

public class TeleportPledgeMember {

	static public void init(Character cha, Skill skill, String name) {
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		if(SkillController.isMagic(cha, skill, true)) {
			//
			Clan c = ClanController.find(cha.getClanName());
			PcInstance pc = c.find(name);
			//
			if(CallPledgeMember.isCall(skill, cha, pc, false)) {
				cha.toPotal(pc.getX(), pc.getY(), pc.getMap());
			} else {
				// 이 위치에서는 그곳으로 이동할 수 없습니다.
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 626));
			}
		}
	}
	
}
