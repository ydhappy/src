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

public class Bamute extends CraftInstance {

	public Bamute(Npc npc) {
		super(npc);
		
		// hyper text 패킷 구성에 해당 구간을 npc 이름으로 사용함.
		temp_request_list.add( npc.getNameId() );
		
		// 제작 처리 초기화.
		Item i = ItemDatabase.find("저주의 피혁(열화)");
		if(i != null){
			craft_list.put("request red skin of curse", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("키메라의 가죽:사자"), 5) );
			l.add( new Craft(ItemDatabase.find("아데나"), 500) );
			list.put(i, l);
		}
		//
		i = ItemDatabase.find("저주의 피혁(물결)");
		if(i != null){
			craft_list.put("request blue skin of curse", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("키메라의 가죽:용"), 5) );
			l.add( new Craft(ItemDatabase.find("아데나"), 500) );
			list.put(i, l);
		}
		//
		i = ItemDatabase.find("저주의 피혁(바람)");
		if(i != null){
			craft_list.put("request yellow skin of curse", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("키메라의 가죽:산양"), 5) );
			l.add( new Craft(ItemDatabase.find("아데나"), 500) );
			list.put(i, l);
		}
		//
		i = ItemDatabase.find("저주의 피혁(대지)");
		if(i != null){
			craft_list.put("request green skin of curse", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("키메라의 가죽:뱀"), 5) );
			l.add( new Craft(ItemDatabase.find("아데나"), 500) );
			list.put(i, l);
		}
		//
		i = ItemDatabase.find("열화의 망토");
		if(i != null){
			craft_list.put("request cloak of flame", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("미스릴 실"), 50) );
			l.add( new Craft(ItemDatabase.find("저주의 피혁(열화)"), 100) );
			l.add( new Craft(ItemDatabase.find("화룡 비늘"), 3) );
			l.add( new Craft(ItemDatabase.find("루비"), 30) );
			l.add( new Craft(ItemDatabase.find("아데나"), 100000) );
			list.put(i, l);
		}
		//
		i = ItemDatabase.find("물결의 망토");
		if(i != null){
			craft_list.put("request cloak of water", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("미스릴 실"), 50) );
			l.add( new Craft(ItemDatabase.find("저주의 피혁(물결)"), 100) );
			l.add( new Craft(ItemDatabase.find("수룡 비늘"), 3) );
			l.add( new Craft(ItemDatabase.find("에메랄드"), 30) );
			l.add( new Craft(ItemDatabase.find("아데나"), 100000) );
			list.put(i, l);
		}
		//
		i = ItemDatabase.find("바람의 망토");
		if(i != null){
			craft_list.put("request cloak of wind", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("미스릴 실"), 50) );
			l.add( new Craft(ItemDatabase.find("저주의 피혁(바람)"), 100) );
			l.add( new Craft(ItemDatabase.find("풍룡 비늘"), 3) );
			l.add( new Craft(ItemDatabase.find("사파이어"), 30) );
			l.add( new Craft(ItemDatabase.find("아데나"), 100000) );
			list.put(i, l);
		}
		//
		i = ItemDatabase.find("대지의 망토");
		if(i != null){
			craft_list.put("request cloak of earth", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("미스릴 실"), 50) );
			l.add( new Craft(ItemDatabase.find("저주의 피혁(대지)"), 100) );
			l.add( new Craft(ItemDatabase.find("지룡 비늘"), 3) );
			l.add( new Craft(ItemDatabase.find("다이아몬드"), 30) );
			l.add( new Craft(ItemDatabase.find("아데나"), 100000) );
			list.put(i, l);
		}
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "bamute1"));
	}

}
