package lineage.world.object.npc.event;

import lineage.bean.database.Npc;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.controller.CraftController;
import lineage.world.object.instance.EventInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class Mary extends EventInstance {
	
	public Mary(Npc npc) {
		super(npc);
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "candleg1"));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if(action.equalsIgnoreCase("0")) {
			ItemInstance item = pc.getInventory().find(ItemDatabase.find(5589));
			if(item == null) {
				CraftController.toCraft(this, pc, ItemDatabase.find(5589), 1, true, 0, 0, 1);
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "candleg2"));
			} else {
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "candleg3"));
			}
		}
	}
	
}
