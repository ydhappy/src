package lineage.world.object.npc;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_MessageGreen;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class Eris extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		if(pc.getLevel() >= 65)
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "eris1"));
		else {
			pc.toSender(new S_MessageGreen(556, "레벨 65부터 입장가능합니다"));
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "eris21"));
		}
	}

}
