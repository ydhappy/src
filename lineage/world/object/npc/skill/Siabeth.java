package lineage.world.object.npc.skill;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.npc.Ellyonne;

public class Siabeth extends Ellyonne {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		if(pc.getClassType() == 0x02)
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "siabeth"));
		else
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "siabeth2"));
	}

}
