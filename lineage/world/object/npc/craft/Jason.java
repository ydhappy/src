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

public class Jason extends CraftInstance {
	
private long actionTime; // 액션딜레이

	
public Jason(Npc npc){
super(npc);
//숨결제작
	Item i = ItemDatabase.find("빛나는 신체의 벨트");
	if(i != null){
		craft_list.put("빛나는 신체의 벨트", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("신체의 벨트"), 1) );
		l.add( new Craft(ItemDatabase.find("빛나는 비늘"), 30) );
		l.add( new Craft(ItemDatabase.find("고급 루비"), 20) );
		l.add( new Craft(ItemDatabase.find("아데나"), 100000) );
		list.put(i, l);
	}
	
	i = ItemDatabase.find("빛나는 정신의 벨트");
	if(i != null){
		craft_list.put("빛나는 정신의 벨트", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("정신의 벨트"), 1) );
		l.add( new Craft(ItemDatabase.find("빛나는 비늘"), 30) );
		l.add( new Craft(ItemDatabase.find("고급 사파이어"), 20) );
		l.add( new Craft(ItemDatabase.find("아데나"), 100000) );
		list.put(i, l);
	  }
	
	i = ItemDatabase.find("빛나는 영혼의 벨트");
	if(i != null){
		craft_list.put("빛나는 영혼의 벨트", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("영혼의 벨트"), 1) );
		l.add( new Craft(ItemDatabase.find("빛나는 비늘"), 30) );
		l.add( new Craft(ItemDatabase.find("고급 루비"), 10) );
		l.add( new Craft(ItemDatabase.find("고급 사파이어"), 10) );
		l.add( new Craft(ItemDatabase.find("아데나"), 100000) );
		list.put(i, l);
	  }
	
	i = ItemDatabase.find("신체의 벨트");
	if(i != null){
		craft_list.put("신체의 벨트", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("낡은 신체의 벨트"), 1) );
		l.add( new Craft(ItemDatabase.find("고급 루비"), 10) );
		l.add( new Craft(ItemDatabase.find("아데나"), 50000) );
		list.put(i, l);
	  }
	
	i = ItemDatabase.find("정신의 벨트");
	if(i != null){
		craft_list.put("정신의 벨트", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("낡은 정신의 벨트"), 1) );
		l.add( new Craft(ItemDatabase.find("고급 사파이어"), 10) );
		l.add( new Craft(ItemDatabase.find("아데나"), 50000) );
		list.put(i, l);
	  }
	
	i = ItemDatabase.find("영혼의 벨트");
	if(i != null){
		craft_list.put("영혼의 벨트", i);
		
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("낡은 영혼의 벨트"), 1) );
		l.add( new Craft(ItemDatabase.find("고급 루비"), 5) );
		l.add( new Craft(ItemDatabase.find("고급 사파이어"), 5) );
		l.add( new Craft(ItemDatabase.find("아데나"), 50000) );
		list.put(i, l);
	  }
	}
			
	@Override
	public void toAi(long time) {
		if (actionTime + (1 * (Util.random(1, 30))) < System.currentTimeMillis()) {
				while (true) {
					int tempGfxMode = Lineage.NPCAction_Jason[Util.random(1, Lineage.NPCAction_Jason.length - 3)];
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
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "jason1"));
			pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 27794)); 
		}
	}
