package lineage.world.object.npc.craft;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;
import lineage.world.object.object;
import lineage.world.object.instance.CraftInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class Ryumiel extends CraftInstance {
		
	public Ryumiel(Npc npc){
		super(npc);
	
	// 숨결제작
	Item i = ItemDatabase.find("골렘의 숨결");
	if(i != null){
		craft_list.put("request earth gem", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("마프르의 유산"), 5) );
		l.add( new Craft(ItemDatabase.find("다이아몬드"), 10) );
		list.put(i, l);
	}
	
	i = ItemDatabase.find("불새의 숨결");
	if(i != null){
		craft_list.put("request fire gem", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("파아그리오의 유산"), 5) );
		l.add( new Craft(ItemDatabase.find("루비"), 10) );
		list.put(i, l);
	  }
	
	i = ItemDatabase.find("얼음여왕의 숨결");
	if(i != null){
		craft_list.put("request water gem", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("에바의 유산"), 5) );
		l.add( new Craft(ItemDatabase.find("에메랄드"), 10) );
		list.put(i, l);
	  }
	
	i = ItemDatabase.find("드레이크의 숨결");
	if(i != null){
		craft_list.put("request wind gem", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("사이하의 유산"), 5) );
		l.add( new Craft(ItemDatabase.find("사파이어"), 10) );
		list.put(i, l);
	  }
	
	//엘릭서 제작
	i = ItemDatabase.find("엘릭서 (STR)");
	if(i != null){
		craft_list.put("request str", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("퓨어 엘릭서"), 35) );
		l.add( new Craft(ItemDatabase.find("마물의 기운"), 2) );
		list.put(i, l);
	  }
	
	i = ItemDatabase.find("엘릭서 (CON)");
	if(i != null){
		craft_list.put("request con", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("퓨어 엘릭서"), 35) );
		l.add( new Craft(ItemDatabase.find("마물의 기운"), 2) );
		list.put(i, l);
	  }
	
	i = ItemDatabase.find("엘릭서 (DEX)");
	if(i != null){
		craft_list.put("request dex", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("퓨어 엘릭서"), 35) );
		l.add( new Craft(ItemDatabase.find("마물의 기운"), 2) );
		list.put(i, l);
	  }
	
	i = ItemDatabase.find("엘릭서 (INT)");
	if(i != null){
		craft_list.put("request int", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("퓨어 엘릭서"), 35) );
		l.add( new Craft(ItemDatabase.find("마물의 기운"), 2) );
		list.put(i, l);
	  }
	
	i = ItemDatabase.find("엘릭서 (WIS)");
	if(i != null){
		craft_list.put("request wis", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("퓨어 엘릭서"), 35) );
		l.add( new Craft(ItemDatabase.find("마물의 기운"), 2) );
		list.put(i, l);
	  }
	
	i = ItemDatabase.find("엘릭서 (CHA)");
	if(i != null){
		craft_list.put("request cha", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("퓨어 엘릭서"), 35) );
		l.add( new Craft(ItemDatabase.find("마물의 기운"), 2) );
		list.put(i, l);
	  }
	//기란마을 부적 제작
	i = ItemDatabase.find("기란 마을 이동 부적");
	if(i != null){
		craft_list.put("request giran charm", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("기란 마을 귀환 주문서"), 100) );
		l.add( new Craft(ItemDatabase.find("마물의 기운"), 5) );
		list.put(i, l);
	  }
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "ryumiel1"));

	}
}
