package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.world.controller.CraftController;
import lineage.world.object.object;
import lineage.world.object.instance.EventInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.TeleportInstance;

public class 테베게이트 extends TeleportInstance {

	List<Item> list;

	public 테베게이트(Npc npc) {
		super(npc);
		list = new ArrayList<Item>();
		list.add(ItemDatabase.find("테베 오시리스 제단 열쇠"));
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "tebegate1"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if (action.startsWith("e"))
			if(pc.getInventory().find(ItemDatabase.find("테베 오시리스 제단 열쇠")) != null)
				tebegate(pc);
			else
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 337, "테베 오시리스 제단 열쇠"));
	}



	/**
	 * 제단 입장
	 */
	private void tebegate(PcInstance pc) {
		for (Item item : list) {
		 ItemInstance ii = pc.getInventory().find(item);
			//아이템 제거
			pc.getInventory().count(ii, 0, true);
		}
		pc.toTeleport(32737, 32831, 782 , true);
		//창닫기
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
	}

}
