package lineage.world.object.npc.teleporter;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.TeleportInstance;

public class Illdrath extends TeleportInstance {

	public Illdrath(Npc npc){
		super(npc);
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(new S_Html( this, "illdrath"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		if (action.equalsIgnoreCase("teleportURL")) {
			pc.toSender(new S_Html( this, "illdrath1"));
		} else if (action.equalsIgnoreCase("teleport hidden-velley-for-newbie")) {
			if(pc.getLevel() < 15) {
				ChattingController.toChatting(pc, String.format("15레벨 이상만 이동하실 수 있습니다."), Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			pc.toPotal(33083, 33387, 4);
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
		}
	}
}
