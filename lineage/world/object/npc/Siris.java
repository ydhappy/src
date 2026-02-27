package lineage.world.object.npc;

import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.SkillController;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class Siris extends object {

	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		//
		if (pc.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT)
			pc.toSender(new S_Html( this, "sirissnw"));
		else
			pc.toSender(new S_Html( this, "siriss"));
	}

}
