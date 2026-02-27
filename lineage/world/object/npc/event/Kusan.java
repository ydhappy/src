package lineage.world.object.npc.event;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.controller.GhostHouseController;
import lineage.world.object.instance.EventInstance;
import lineage.world.object.instance.PcInstance;

public class Kusan extends EventInstance {

	public Kusan(Npc npc) {
		super(npc);
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "kusan"));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		GhostHouseController.toEventAction(pc, action.startsWith("info"));
	}
}
