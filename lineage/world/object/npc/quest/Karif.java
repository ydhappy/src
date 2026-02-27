package lineage.world.object.npc.quest;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.bean.lineage.Quest;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.CraftController;
import lineage.world.controller.QuestController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestInstance;

public class Karif extends QuestInstance {

	public Karif(Npc npc){
		super(npc);
		
		// hyper text 패킷 구성에 해당 구간을 npc 이름으로 사용함.
		temp_request_list.add( npc.getNameId() );

		Item i = ItemDatabase.find("카리프의 주머니(다이아몬드)");
		if(i != null){
			craft_list.put("request karif bag1", i);

			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("다이아몬드"), 1) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("카리프의 주머니(에메랄드)");
		if(i != null){
			craft_list.put("request karif bag2", i);

			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("에메랄드"), 1) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("카리프의 주머니(루비)");
		if(i != null){
			craft_list.put("request karif bag3", i);

			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("루비"), 1) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("카리프의 주머니(사파이어)");
		if(i != null){
			craft_list.put("request karif bag4", i);

			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("사파이어"), 1) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("카리프의 고급 주머니(다이아몬드)");
		if(i != null){
			craft_list.put("request karif bag5", i);

			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("고급 다이아몬드"), 1) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("카리프의 고급 주머니(에메랄드)");
		if(i != null){
			craft_list.put("request karif bag6", i);

			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("고급 에메랄드"), 1) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("카리프의 고급 주머니(루비)");
		if(i != null){
			craft_list.put("request karif bag7", i);

			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("고급 루비"), 1) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("카리프의 고급 주머니(사파이어)");
		if(i != null){
			craft_list.put("request karif bag8", i);

			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("고급 사파이어"), 1) );
			list.put(i, l);
		}
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "karif1"));
	}
}
