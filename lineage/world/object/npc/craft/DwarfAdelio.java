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

public class DwarfAdelio extends CraftInstance {

	public DwarfAdelio(Npc npc) {
		super(npc);
		
		// hyper text 패킷 구성에 해당 구간을 npc 이름으로 사용함.
		temp_request_list.add( npc.getNameId() );
		
		// 제작 처리 초기화.
		Item i = ItemDatabase.find("진명황의 집행검");
		if(i != null){
			craft_list.put("request enforce sword of emperor", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("라스타바드 무기제작 비법서"), 1) );
			l.add( new Craft(ItemDatabase.find("무관의 양손검"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴 판금"), 10) );
			l.add( new Craft(ItemDatabase.find("흑마법 가루"), 50) );
			l.add( new Craft(ItemDatabase.find("성지의 유물"), 100) );
			l.add( new Craft(ItemDatabase.find("그랑카인의 눈물"), 10) );
			l.add( new Craft(ItemDatabase.find("어둠의 주괴"), 50) );
			l.add( new Craft(ItemDatabase.find("다크엘프 영혼의 결정체"), 300) );
			list.put(i, l);
		}
		//
		i = ItemDatabase.find("바람칼날의 단검");
		if(i != null){
			craft_list.put("request sword of windblade", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("라스타바드 무기제작 비법서"), 1) );
			l.add( new Craft(ItemDatabase.find("무관의 장검"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴 판금"), 10) );
			l.add( new Craft(ItemDatabase.find("흑마법 가루"), 50) );
			l.add( new Craft(ItemDatabase.find("성지의 유물"), 100) );
			l.add( new Craft(ItemDatabase.find("그랑카인의 눈물"), 10) );
			l.add( new Craft(ItemDatabase.find("어둠의 주괴"), 50) );
			l.add( new Craft(ItemDatabase.find("다크엘프 영혼의 결정체"), 300) );
			list.put(i, l);
		}
		//
		i = ItemDatabase.find("붉은 그림자의 이도류");
		if(i != null){
			craft_list.put("request dualblade of red shadow", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("라스타바드 무기제작 비법서"), 1) );
			l.add( new Craft(ItemDatabase.find("암살군왕의 암살도"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴 판금"), 10) );
			l.add( new Craft(ItemDatabase.find("흑마법 가루"), 50) );
			l.add( new Craft(ItemDatabase.find("성지의 유물"), 100) );
			l.add( new Craft(ItemDatabase.find("그랑카인의 눈물"), 10) );
			l.add( new Craft(ItemDatabase.find("어둠의 주괴"), 50) );
			l.add( new Craft(ItemDatabase.find("다크엘프 영혼의 결정체"), 300) );
			list.put(i, l);
		}
		//
		i = ItemDatabase.find("야수왕의 크로우");
		if(i != null){
			craft_list.put("request claw of beast king", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("라스타바드 무기제작 비법서"), 1) );
			l.add( new Craft(ItemDatabase.find("마수군왕의 크로우"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴 판금"), 10) );
			l.add( new Craft(ItemDatabase.find("흑마법 가루"), 50) );
			l.add( new Craft(ItemDatabase.find("성지의 유물"), 100) );
			l.add( new Craft(ItemDatabase.find("그랑카인의 눈물"), 10) );
			l.add( new Craft(ItemDatabase.find("어둠의 주괴"), 50) );
			l.add( new Craft(ItemDatabase.find("다크엘프 영혼의 결정체"), 300) );
			list.put(i, l);
		}
		//
		i = ItemDatabase.find("수정결정체의 지팡이");
		if(i != null){
			craft_list.put("request staff of holyhedron", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("라스타바드 무기제작 비법서"), 1) );
			l.add( new Craft(ItemDatabase.find("신관의 지팡이"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴 판금"), 10) );
			l.add( new Craft(ItemDatabase.find("흑마법 가루"), 50) );
			l.add( new Craft(ItemDatabase.find("성지의 유물"), 100) );
			l.add( new Craft(ItemDatabase.find("그랑카인의 눈물"), 10) );
			l.add( new Craft(ItemDatabase.find("어둠의 주괴"), 50) );
			l.add( new Craft(ItemDatabase.find("다크엘프 영혼의 결정체"), 300) );
			list.put(i, l);
		}
		//
		i = ItemDatabase.find("진명황의 투구");
		if(i != null){
			craft_list.put("request staff of holyhedron", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("진명황의 방어구 비법서"), 1) );
			l.add( new Craft(ItemDatabase.find("무관의 투구"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴 판금"), 5) );
			l.add( new Craft(ItemDatabase.find("흑마법 가루"), 30) );
			l.add( new Craft(ItemDatabase.find("수행자의 경전"), 50) );
			l.add( new Craft(ItemDatabase.find("그랑카인의 눈물"), 3) );
			l.add( new Craft(ItemDatabase.find("어둠의 주괴"), 50) );
			l.add( new Craft(ItemDatabase.find("다크엘프 영혼의 결정체"), 100) );
			list.put(i, l);
		}
		//
		i = ItemDatabase.find("진명황의 갑옷");
		if(i != null){
			craft_list.put("request armor of dantes", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("진명황의 방어구 비법서"), 1) );
			l.add( new Craft(ItemDatabase.find("무관의 갑옷"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴 판금"), 10) );
			l.add( new Craft(ItemDatabase.find("흑마법 가루"), 50) );
			l.add( new Craft(ItemDatabase.find("수행자의 경전"), 100) );
			l.add( new Craft(ItemDatabase.find("그랑카인의 눈물"), 10) );
			l.add( new Craft(ItemDatabase.find("최고급 다이아몬드"), 20) );
			l.add( new Craft(ItemDatabase.find("어둠의 주괴"), 50) );
			l.add( new Craft(ItemDatabase.find("다크엘프 영혼의 결정체"), 300) );
			list.put(i, l);
		}
		//
		i = ItemDatabase.find("진명황의 장갑");
		if(i != null){
			craft_list.put("request gloves of dantes", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("진명황의 방어구 비법서"), 1) );
			l.add( new Craft(ItemDatabase.find("무관의 장갑"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴 판금"), 3) );
			l.add( new Craft(ItemDatabase.find("흑마법 가루"), 30) );
			l.add( new Craft(ItemDatabase.find("수행자의 경전"), 50) );
			l.add( new Craft(ItemDatabase.find("그랑카인의 눈물"), 3) );
			l.add( new Craft(ItemDatabase.find("어둠의 주괴"), 30) );
			l.add( new Craft(ItemDatabase.find("다크엘프 영혼의 결정체"), 100) );
			list.put(i, l);
		}
		//
		i = ItemDatabase.find("진명황의 망토");
		if(i != null){
			craft_list.put("request cloak of dantes", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("진명황의 방어구 비법서"), 1) );
			l.add( new Craft(ItemDatabase.find("무관의 망토"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴 판금"), 3) );
			l.add( new Craft(ItemDatabase.find("흑마법 가루"), 10) );
			l.add( new Craft(ItemDatabase.find("수행자의 경전"), 30) );
			l.add( new Craft(ItemDatabase.find("그랑카인의 눈물"), 3) );
			l.add( new Craft(ItemDatabase.find("불타는 가죽"), 20) );
			l.add( new Craft(ItemDatabase.find("어둠의 주괴"), 50) );
			l.add( new Craft(ItemDatabase.find("다크엘프 영혼의 결정체"), 100) );
			list.put(i, l);
		}
		//
		i = ItemDatabase.find("진명황의 부츠");
		if(i != null){
			craft_list.put("request boots of dantes", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("진명황의 방어구 비법서"), 1) );
			l.add( new Craft(ItemDatabase.find("무관의 부츠"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴 판금"), 4) );
			l.add( new Craft(ItemDatabase.find("흑마법 가루"), 30) );
			l.add( new Craft(ItemDatabase.find("수행자의 경전"), 50) );
			l.add( new Craft(ItemDatabase.find("그랑카인의 눈물"), 3) );
			l.add( new Craft(ItemDatabase.find("어둠의 주괴"), 30) );
			l.add( new Craft(ItemDatabase.find("다크엘프 영혼의 결정체"), 100) );
			list.put(i, l);
		}
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "adelio"));
	}

}
