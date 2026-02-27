package lineage.world.object.npc.teleporter;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.TeleportInstance;

public class Npcworld extends TeleportInstance {
	
	public Npcworld(Npc npc){
		super(npc);
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "teleworld1"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if(action.equalsIgnoreCase("teleportURL1")){
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "teleworld2", null, list.get(0)));
		}else if(action.equalsIgnoreCase("teleportURL2")){
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "teleworld3", null, list.get(0)));
		}else{
			super.toTalk(pc, action, type, cbp);
		}
	}

}
