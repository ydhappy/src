package lineage.world.object.npc.teleporter;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.TeleportInstance;

public class sktzguard extends TeleportInstance {

	public sktzguard(Npc npc){
		super(npc);
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		//은기사 시장 경비병
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "sktzguard"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if(action.equalsIgnoreCase("teleportURL")){
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "sktzguard1"));
		}else if(action.equalsIgnoreCase("teleport silver-trade-zone-silver")){
			pc.toPotal(33077, 33388, 4);
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
		}
	}

}