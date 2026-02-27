package lineage.world.object.npc;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class Kuper extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		// 초본퀘 끝나지 않은상태이면서 레벨이 13이상일 경우??
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "kuper1"));
	}

}
