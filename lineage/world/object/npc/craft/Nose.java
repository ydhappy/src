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

public class Nose extends CraftInstance {
	
	public Nose(Npc npc){
		super(npc);
		
		// hyper text 패킷 구성에 해당 구간을 npc 이름으로 사용함.
		temp_request_list.add( npc.getNameId() );
		
		// 제작 처리 초기화.
		Item i = ItemDatabase.find("사냥개의 이빨");
		if(i != null){
			craft_list.put("request pet dog tooth", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("투견의 이빨"), 1) );
			l.add( new Craft(ItemDatabase.find("고급 다이아몬드"), 3) );
			l.add( new Craft(ItemDatabase.find("철괴"), 5) );
			l.add( new Craft(ItemDatabase.find("아데나"), 10000) );
			list.put(i, l);
		}
		i = ItemDatabase.find("승리의 이빨");
		if(i != null){
			craft_list.put("request pet victory tooth", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("강철의 이빨"), 1) );
			l.add( new Craft(ItemDatabase.find("고급 사파이어"), 2) );
			l.add( new Craft(ItemDatabase.find("철괴"), 5) );
			l.add( new Craft(ItemDatabase.find("아데나"), 10000) );
			list.put(i, l);
		}
		i = ItemDatabase.find("황금의 이빨");
		if(i != null){
			craft_list.put("request pet gold tooth", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("강철의 이빨"), 1) );
			l.add( new Craft(ItemDatabase.find("강철괴"), 10) );
			l.add( new Craft(ItemDatabase.find("황금괴"), 10) );
			l.add( new Craft(ItemDatabase.find("아데나"), 20000) );
			list.put(i, l);
		}
		i = ItemDatabase.find("파멸의 이빨");
		if(i != null){
			craft_list.put("request pet end tooth", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("사냥개의 이빨"), 1) );
			l.add( new Craft(ItemDatabase.find("그랑카인의 눈물"), 1) );
			l.add( new Craft(ItemDatabase.find("불새의 숨결"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴"), 5) );
			l.add( new Craft(ItemDatabase.find("아데나"), 50000) );
			list.put(i, l);
		}
		i = ItemDatabase.find("신마의 이빨");
		if(i != null){
			craft_list.put("request pet god tooth", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("황금의 이빨"), 1) );
			l.add( new Craft(ItemDatabase.find("은괴"), 5) );
			l.add( new Craft(ItemDatabase.find("백금괴"), 1) );
			l.add( new Craft(ItemDatabase.find("오리하루콘"), 10) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴"), 1) );
			l.add( new Craft(ItemDatabase.find("아데나"), 30000) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("스켈 펫아머");
		if(i != null){
			craft_list.put("request skel petarmor", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("레더 펫아머"), 1) );
			l.add( new Craft(ItemDatabase.find("고급피혁"), 10) );
			l.add( new Craft(ItemDatabase.find("뼈조각"), 20) );			
			l.add( new Craft(ItemDatabase.find("아데나"), 5000) );
			list.put(i, l);
		}
		i = ItemDatabase.find("크로스 펫아머");
		if(i != null){
			craft_list.put("request cross petarmor", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("강철 펫아머"), 1) );
			l.add( new Craft(ItemDatabase.find("고급피혁"), 50) );
			l.add( new Craft(ItemDatabase.find("강철괴"), 20) );			
			l.add( new Craft(ItemDatabase.find("아데나"), 10000) );
			list.put(i, l);
		}
		i = ItemDatabase.find("미스릴 펫아머");
		if(i != null){
			craft_list.put("request mithril petarmor", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("크로스 펫아머"), 1) );
			l.add( new Craft(ItemDatabase.find("고급피혁"), 50) );
			l.add( new Craft(ItemDatabase.find("백금 판금"), 2) );
			l.add( new Craft(ItemDatabase.find("미스릴 판금"), 10) );
			l.add( new Craft(ItemDatabase.find("드레이크의 숨결"), 1) );
			l.add( new Craft(ItemDatabase.find("아데나"), 30000) );
			list.put(i, l);
		}
		i = ItemDatabase.find("체인 펫아머");
		if(i != null){
			craft_list.put("request chain petarmor", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("강철 펫아머"), 1) );
			l.add( new Craft(ItemDatabase.find("고급피혁"), 50) );
			l.add( new Craft(ItemDatabase.find("강철괴"), 100) );
			l.add( new Craft(ItemDatabase.find("아데나"), 10000) );
			list.put(i, l);
		}
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "nose"));
	}

}
