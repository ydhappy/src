package lineage.world.object.npc.teleporter;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.TeleportInstance;

public class Ishtar extends TeleportInstance {

	public Ishtar(Npc npc){
		super(npc);
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		if(pc.getLevel() < 10) {
			ChattingController.toChatting(pc, String.format("10레벨 이상만 가능합니다."), Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "ishtar"));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if(action.equalsIgnoreCase("teleportURL")){
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "ishtar1"));
		}else if(action.equalsIgnoreCase("teleport talking-island-for-newbie")){
			pc.toPotal(32597, 32916, 0);
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
		}
	}

}
