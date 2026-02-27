package lineage.world.object.npc.craft;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.instance.CraftInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class Alice extends CraftInstance {

	public Alice(Npc npc) {
		super(npc);
		
		// 제작 처리 초기화.
		Item i = ItemDatabase.find("앨리스");
		if(i != null){
			craft_list.put("request craft", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("발록의 양손검"), 1) );
			l.add( new Craft(ItemDatabase.find("발록의 손톱"), 100) );
			l.add( new Craft(ItemDatabase.find("혈석 파편"), 100) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("앨리스");
		if(i != null){
			i.getListCraftGrade().put("mate_2", 2);
			craft_list.put("mate_2", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("앨리스"), 1) );
			l.add( new Craft(ItemDatabase.find("그놈의 이빨"), 100) );
			l.add( new Craft(ItemDatabase.find("혈석 파편"), 100) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("앨리스");
		if(i != null){
			i.getListCraftGrade().put("mate_3", 3);
			craft_list.put("mate_3", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("앨리스"), 1) );
			l.add( new Craft(ItemDatabase.find("발록의 날개"), 100) );
			l.add( new Craft(ItemDatabase.find("혈석 파편"), 100) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("앨리스");
		if(i != null){
			i.getListCraftGrade().put("mate_4", 4);
			craft_list.put("mate_4", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("앨리스"), 1) );
			l.add( new Craft(ItemDatabase.find("발록의 분신"), 50) );
			l.add( new Craft(ItemDatabase.find("혈석 파편"), 100) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("앨리스");
		if(i != null){
			i.getListCraftGrade().put("mate_5", 5);
			craft_list.put("mate_5", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("앨리스"), 1) );
			l.add( new Craft(ItemDatabase.find("발록의 뿔"), 50) );
			l.add( new Craft(ItemDatabase.find("혈석 파편"), 100) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("앨리스");
		if(i != null){
			i.getListCraftGrade().put("mate_6", 6);
			craft_list.put("mate_6", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("앨리스"), 1) );
			l.add( new Craft(ItemDatabase.find("발록의 의지"), 50) );
			l.add( new Craft(ItemDatabase.find("혈석 파편"), 100) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("앨리스");
		if(i != null){
			i.getListCraftGrade().put("mate_7", 7);
			craft_list.put("mate_7", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("앨리스"), 1) );
			l.add( new Craft(ItemDatabase.find("발록의 전율"), 10) );
			l.add( new Craft(ItemDatabase.find("혈석 파편"), 100) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("앨리스");
		if(i != null){
			i.getListCraftGrade().put("mate_8", 8);
			craft_list.put("mate_8", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("앨리스"), 1) );
			l.add( new Craft(ItemDatabase.find("발록의 가면"), 10) );
			l.add( new Craft(ItemDatabase.find("혈석 파편"), 100) );
			list.put(i, l);
		}
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		ItemInstance item = pc.getInventory().findDbNameId(4423);
		ItemInstance item2 = pc.getInventory().find(lineage.world.object.item.weapon.Alice.class);
		if(item==null && item2==null) {
			if(pc.isKarmaType() != -1)
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "gd"));
			else
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "aliceyet"));
		} else {
			if(pc.isKarmaType() != -1)
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "alice_gd"));
			else {
				// 찾은 앨리스 무기 단계에 맞는 메세지 출력.
				if(item != null)
					pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "mate_1"));
				else
					pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "mate_"+(item2.getGrade()+1)));
			}
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		if(action.equalsIgnoreCase("request craft")) {
			// 앨리스 제작.
			super.toTalk(pc, action, type, cbp, opt);
		} else {
			// 
			ItemInstance item = pc.getInventory().find(lineage.world.object.item.weapon.Alice.class);
			if(item == null)
				return;
			//
			action = "mate_" + (item.getGrade() + 1);
			super.toTalk(pc, action, type, cbp, opt);
		}
	}

}
