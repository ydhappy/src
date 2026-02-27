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
import lineage.world.object.instance.PcInstance;

public class Pierce extends CraftInstance {
	
	public Pierce(Npc npc){
		super(npc);
		// hyper text 패킷 구성에 해당 구간을 npc 이름으로 사용함.
				temp_request_list.add( npc.getNameId() );
				
				// 제작 처리 초기화.

				Item i = ItemDatabase.find("실버 스팅"); //1000
				if(i != null){
					craft_list.put("request silver sting knife", i);
					
					List<Craft> l = new ArrayList<Craft>();
					l.add( new Craft(ItemDatabase.find("흑요석"), 1) );
					list.put(i, l);
				}
				i = ItemDatabase.find("헤비 스팅"); //2000
				if(i != null){
					craft_list.put("request heavy sting knife", i);
					
					List<Craft> l = new ArrayList<Craft>();
					l.add( new Craft(ItemDatabase.find("강암석"), 1) );					
					list.put(i, l);
				}
				i = ItemDatabase.find("피어스의 선물주머니");
				if(i != null){
					craft_list.put("request pears itembag", i);
					
					List<Craft> l = new ArrayList<Craft>();
					l.add( new Craft(ItemDatabase.find("현암석"), 1) );					
					list.put(i, l);
				}
				i = ItemDatabase.find("진 건틀렛");
				if(i != null){
					craft_list.put("request jin gauntlet", i);
					
					List<Craft> l = new ArrayList<Craft>();
					l.add( new Craft(ItemDatabase.find("암황석"), 1) );					
					list.put(i, l);
				}
				
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "pears1"));
	}

}
