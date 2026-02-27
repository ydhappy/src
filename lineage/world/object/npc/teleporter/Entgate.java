package lineage.world.object.npc.teleporter;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.TeleportInstance;

public class Entgate extends TeleportInstance {

	public Entgate(Npc npc){
		super(npc);
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		if (pc.getLevel() < 40) 
			pc.toSender(new S_Html( this, "entgate3"));
		else if (pc.getLevel() > 48)
			pc.toSender(new S_Html( this, "entgate3"));
		else
			pc.toSender(new S_Html( this, "entgate"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt) {
		if(action.equalsIgnoreCase("1")) {
			pc.toTeleport(32537, 32955, 777, true);
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
		} else if(action.equalsIgnoreCase("2")) {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
	}
}
}