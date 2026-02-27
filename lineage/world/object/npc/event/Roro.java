package lineage.world.object.npc.event;

import lineage.bean.database.Npc;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.world.controller.BaseStatController;
import lineage.world.object.instance.EventInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class Roro extends EventInstance {
	
	public Roro(Npc npc) {
		super(npc);
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "baseReset"));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if(action.equalsIgnoreCase("ent")) {
			ItemInstance item = pc.getInventory().find(ItemDatabase.find(5589));
			if(item == null) {
				// 촛불이 없을경우 메세지 표현.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1290));
			} else {
				// 촛불 제거.
				pc.getInventory().count(item, item.getCount()-1, true);
				// 스탯초기화 창 열기.
				BaseStatController.toOpen(pc);
			}
		}
	}
	
}
