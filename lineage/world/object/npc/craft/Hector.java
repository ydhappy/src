package lineage.world.object.npc.craft;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.database.ItemDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_SoundEffect;
import lineage.share.Lineage;
import lineage.share.System;
import lineage.util.Util;
import lineage.world.object.instance.CraftInstance;
import lineage.world.object.instance.PcInstance;

public class Hector extends CraftInstance {
	
	private long actionTime; // 액션딜레이
	
	public Hector(Npc npc){
		super(npc);
		
		// hyper text 패킷 구성에 해당 구간을 npc 이름으로 사용함.
		temp_request_list.add( npc.getNameId() );
		
		// 제작 처리 초기화.
				Item i = ItemDatabase.find("강철 장갑");
				if(i != null){
					craft_list.put("request iron gloves", i);
					
					List<Craft> l = new ArrayList<Craft>();
					l.add( new Craft(ItemDatabase.find("장갑"), 1) );
					l.add( new Craft(ItemDatabase.find("철괴"), 75) );
					l.add( new Craft(ItemDatabase.find("아데나"), 25000) );
					list.put(i, l);
				}
				
				i = ItemDatabase.find("강철 면갑");
				if(i != null){
					craft_list.put("request iron visor", i);
					
					List<Craft> l = new ArrayList<Craft>();
					l.add( new Craft(ItemDatabase.find("기사의 면갑"), 1) );
					l.add( new Craft(ItemDatabase.find("철괴"), 60) );
					l.add( new Craft(ItemDatabase.find("아데나"), 16500) );
					list.put(i, l);
				}
				
				i = ItemDatabase.find("강철 방패");
				if(i != null){
					craft_list.put("request iron shield", i);
					
					List<Craft> l = new ArrayList<Craft>();
					l.add( new Craft(ItemDatabase.find("사각 방패"), 1) );
					l.add( new Craft(ItemDatabase.find("철괴"), 100) );
					l.add( new Craft(ItemDatabase.find("아데나"), 16000) );
					list.put(i, l);
				}
				
				i = ItemDatabase.find("강철 부츠");
				if(i != null){
					craft_list.put("request iron boots", i);
					
					List<Craft> l = new ArrayList<Craft>();
					l.add( new Craft(ItemDatabase.find("부츠"), 1) );
					l.add( new Craft(ItemDatabase.find("철괴"), 80) );
					l.add( new Craft(ItemDatabase.find("아데나"), 8000) );
					list.put(i, l);
				}
				
				i = ItemDatabase.find("강철 판금 갑옷");
				if(i != null){
					craft_list.put("request iron plate mail", i);
					
					List<Craft> l = new ArrayList<Craft>();
					l.add( new Craft(ItemDatabase.find("판금 갑옷"), 1) );
					l.add( new Craft(ItemDatabase.find("철괴"), 200) );
					l.add( new Craft(ItemDatabase.find("아데나"), 30000) );
					list.put(i, l);
				}
	}

	@Override
	public void toAi(long time) {
		if (actionTime + (1000 * (Util.random(10, 15))) < System.currentTimeMillis()) {
				while (true) {
					int tempGfxMode = Lineage.NPCAction_Hector[Util.random(1, Lineage.NPCAction_Hector.length - 1)];
					if (SpriteFrameDatabase.findGfxMode(getGfx(), tempGfxMode)) {
						actionTime = (int) System.currentTimeMillis();
						toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), this,
								tempGfxMode), true);
						break;
					}
				}
			}
		}	
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "hector1"));
			pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 27794)); 
		
	}
}
