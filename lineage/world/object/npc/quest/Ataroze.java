package lineage.world.object.npc.quest;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.controller.CraftController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestInstance;

public class Ataroze extends QuestInstance {

	public Ataroze(Npc npc) {
		super(npc);
		
		// 완성한 라스타바드의 역사서
		Item i = ItemDatabase.find(4576);
		if(i != null){
			craft_list.put("request las history book", i);
			
			List<Craft> l = new ArrayList<Craft>();
			// 라스타바드의 역사서 1장 ~ 8장
			l.add( new Craft(ItemDatabase.find(4611), 1) );
			l.add( new Craft(ItemDatabase.find(4612), 1) );
			l.add( new Craft(ItemDatabase.find(4695), 1) );
			l.add( new Craft(ItemDatabase.find(4696), 1) );
			l.add( new Craft(ItemDatabase.find(4697), 1) );
			l.add( new Craft(ItemDatabase.find(4698), 1) );
			l.add( new Craft(ItemDatabase.find(4699), 1) );
			l.add( new Craft(ItemDatabase.find(4700), 1) );
			list.put(i, l);
		}
		
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "ataroze"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		Item craft = craft_list.get(action);
		if(craft != null){
//			if(CraftController.isCraft(pc, list.get(craft), true))
//				toFinal(pc, action, 1);
		}
	}

}
