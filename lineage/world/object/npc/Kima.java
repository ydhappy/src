package lineage.world.object.npc;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class Kima extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		if(pc.getLevel()  < 50)
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "kima1"));
	}

}
