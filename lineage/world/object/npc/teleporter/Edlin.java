package lineage.world.object.npc.teleporter;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.TeleportInstance;

public class Edlin extends TeleportInstance {

	public Edlin(Npc npc){
		super(npc);
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "telephantasm"));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if (action.equalsIgnoreCase("teleport phantasm-island")) {
			if (pc.getLevel() < 40) {
				pc.toSender(new S_Html( this, "edlen5"));
			} else {
				if (pc.getInventory().isAden("아데나", 10000, true)) 
				pc.toPotal(32635, 32817, 5);
				else
					ChattingController.toChatting(pc, "[에들렌] 아데나 10000원이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
			}
		}
	}
}
