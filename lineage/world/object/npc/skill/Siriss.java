package lineage.world.object.npc.skill;

import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.SkillController;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class Siriss extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		//
		if (SkillController.getBuySkillIdx(pc) <= 0) {
			switch (pc.getClassType()) {
				case Lineage.LINEAGE_CLASS_KNIGHT:
					pc.toSender(new S_Html( this, "sirissnw"));
					break;
				default:
					pc.toSender(new S_Html( this, "sirissnt"));
					break;
			}
		} else {
			int count = SkillController.getBuySkillCount(pc);
			if (count > 0)
				// 마법 배울게 있을경우.
				pc.toSender(new S_Html( this, "siriss"));
			else
				// 마법 배울게 없을 경우.
				pc.toSender(new S_Html( this, "siriss1"));
		}
		
	}

}
