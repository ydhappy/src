package lineage.world.object.npc.teleporter;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.TeleportInstance;

public class Kun extends TeleportInstance {

	public Kun(Npc npc){
		super(npc);
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		switch(pc.getClassType()){
			case 0x00:
			case 0x03:
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "kun1"));
				break;
			case 0x01:
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "kun2"));
				break;
			case 0x02:
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "kun3"));
				break;
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if(action.equalsIgnoreCase("teleportURL")){
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "kun4"));
		}else if(action.equalsIgnoreCase("teleport dungeon-in")){
			pc.toPotal(32677, 32866, 85);
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
		}
	}

}
