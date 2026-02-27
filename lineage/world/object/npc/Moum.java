package lineage.world.object.npc;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.util.Util;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class Moum extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		switch(Util.random(0, 2)) {
			case 0:
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "moumone1"));
				break;
			case 1:
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "moumtwo1"));
				break;
			case 2:
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "moumthree1"));
				break;
		}
	}
	
}
