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
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestInstance;

public class Yahee extends QuestInstance {

	public Yahee(Npc npc) {
		super(npc);
		
		Item i = ItemDatabase.find("원소의 지배자");
		if(i != null){
			craft_list.put("request free pass", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("흙의 지배자"), 1) );
			l.add( new Craft(ItemDatabase.find("물의 지배자"), 1) );
			l.add( new Craft(ItemDatabase.find("불의 지배자"), 1) );
			l.add( new Craft(ItemDatabase.find("공기의 지배자"), 1) );
			list.put(i, l);
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "qyahee"));
	}
	
}
