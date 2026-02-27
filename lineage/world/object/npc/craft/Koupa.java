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

public class Koupa extends CraftInstance {
	
	public Koupa(Npc npc){
		super(npc);
		
		// hyper text 패킷 구성에 해당 구간을 npc 이름으로 사용함.
		temp_request_list.add( npc.getNameId() );
		
		// 제작 처리 초기화.
		Item i = ItemDatabase.find("파푸리온의 마갑주");
		if(i != null){
			craft_list.put("request ancient blue dragon armor", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("수룡 비늘 갑옷"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴 판금"), 3) );
			l.add( new Craft(ItemDatabase.find("고급 사파이어"), 30) );
			l.add( new Craft(ItemDatabase.find("얼음여왕의 숨결"), 5) );
			l.add( new Craft(ItemDatabase.find("아데나"), 50000) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("안타라스의 마갑주");
		if(i != null){
			craft_list.put("request ancient green dragon armor", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("지룡 비늘 갑옷"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴 판금"), 3) );
			l.add( new Craft(ItemDatabase.find("고급 다이아몬드"), 30) );
			l.add( new Craft(ItemDatabase.find("골렘의 숨결"), 5) );
			l.add( new Craft(ItemDatabase.find("아데나"), 50000) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("발라카스의 마갑주");
		if(i != null){
			craft_list.put("request ancient red dragon armor", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("화룡 비늘 갑옷"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴 판금"), 3) );
			l.add( new Craft(ItemDatabase.find("고급 루비"), 30) );
			l.add( new Craft(ItemDatabase.find("불새의 숨결"), 5) );
			l.add( new Craft(ItemDatabase.find("아데나"), 50000) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("린드비오르의 마갑주");
		if(i != null){
			craft_list.put("request ancient azure dragon armo", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("풍룡 비늘 갑옷"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴 판금"), 3) );
			l.add( new Craft(ItemDatabase.find("고급 사파이어"), 30) );
			l.add( new Craft(ItemDatabase.find("드레이크의 숨결"), 5) );
			l.add( new Craft(ItemDatabase.find("아데나"), 50000) );
			list.put(i, l);
		}
		i = ItemDatabase.find("어둠의 이도류");
		if(i != null){
			craft_list.put("request dark dualblade", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("흑요석"), 100) );
			l.add( new Craft(ItemDatabase.find("철괴"), 10) );
			l.add( new Craft(ItemDatabase.find("고급피혁"), 20) );
			list.put(i, l);
		}
		i = ItemDatabase.find("어둠의 크로우");
		if(i != null){
			craft_list.put("request dark claw", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("흑요석"), 100) );
			l.add( new Craft(ItemDatabase.find("철괴"), 10) );
			l.add( new Craft(ItemDatabase.find("강암석"), 5) );
			l.add( new Craft(ItemDatabase.find("고급피혁"), 30) );
			list.put(i, l);
		}
		i = ItemDatabase.find("어둠의 활");
		if(i != null){
			craft_list.put("request dark crossbow", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("흑요석"), 100) );
			l.add( new Craft(ItemDatabase.find("철괴"), 10) );
			l.add( new Craft(ItemDatabase.find("강암석"), 5) );
			l.add( new Craft(ItemDatabase.find("고급피혁"), 30) );
			list.put(i, l);
		}
		i = ItemDatabase.find("흑빛의 이도류");
		if(i != null){
			craft_list.put("request blind dualblade", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("어둠의 이도류"), 1) );
			l.add( new Craft(ItemDatabase.find("철괴"), 10) );
			l.add( new Craft(ItemDatabase.find("강암석"), 100) );
			l.add( new Craft(ItemDatabase.find("고급피혁"), 20) );
			l.add( new Craft(ItemDatabase.find("현암석"), 5) );
			list.put(i, l);
		}
		i = ItemDatabase.find("흑빛의 크로우");
		if(i != null){
			craft_list.put("request blind claw", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("어둠의 크로우"), 1) );
			l.add( new Craft(ItemDatabase.find("철괴"), 10) );
			l.add( new Craft(ItemDatabase.find("강암석"), 100) );
			l.add( new Craft(ItemDatabase.find("고급피혁"), 10) );
			l.add( new Craft(ItemDatabase.find("현암석"), 10) );
			list.put(i, l);
		}
		i = ItemDatabase.find("흑빛의 활");
		if(i != null){
			craft_list.put("request blind crossbow", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("어둠의 활"), 1) );
			l.add( new Craft(ItemDatabase.find("철괴"), 10) );
			l.add( new Craft(ItemDatabase.find("강암석"), 100) );
			l.add( new Craft(ItemDatabase.find("고급피혁"), 30) );
			l.add( new Craft(ItemDatabase.find("현암석"), 20) );
			list.put(i, l);
		}
		i = ItemDatabase.find("은색의 이도류");
		if(i != null){
			craft_list.put("request blind crossbow", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("어둠의 이도류"), 1) );
			l.add( new Craft(ItemDatabase.find("철괴"), 10) );
			l.add( new Craft(ItemDatabase.find("흑요석"), 50) );
			l.add( new Craft(ItemDatabase.find("현암석"), 1) );
			l.add( new Craft(ItemDatabase.find("은괴"), 20) );
			l.add( new Craft(ItemDatabase.find("다이아몬드"), 1) );
			l.add( new Craft(ItemDatabase.find("고급피혁"), 20) );
			list.put(i, l);
		}
		i = ItemDatabase.find("은색의 크로우");
		if(i != null){
			craft_list.put("request silver claw", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("어둠의 크로우"), 1) );
			l.add( new Craft(ItemDatabase.find("철괴"), 10) );
			l.add( new Craft(ItemDatabase.find("흑요석"), 40) );
			l.add( new Craft(ItemDatabase.find("현암석"), 1) );
			l.add( new Craft(ItemDatabase.find("은괴"), 30) );
			l.add( new Craft(ItemDatabase.find("다이아몬드"), 1) );
			l.add( new Craft(ItemDatabase.find("고급피혁"), 10) );
			list.put(i, l);
		}
		i = ItemDatabase.find("블랙 미스릴");
		if(i != null){
			craft_list.put("request black mithril", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("흑정령석"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴 원석"), 5) );
			l.add( new Craft(ItemDatabase.find("브롭의 위액"), 1) );
			l.add( new Craft(ItemDatabase.find("화산재"), 1) );
			l.add( new Craft(ItemDatabase.find("아데나"), 5000) );
			list.put(i, l);
		}
		i = ItemDatabase.find("강철괴");
		if(i != null){
			craft_list.put("request lump of steel", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("강철 원석"), 5) );
			l.add( new Craft(ItemDatabase.find("철괴"), 5) );
			l.add( new Craft(ItemDatabase.find("아데나"), 500) );
			list.put(i, l);
		}
		i = ItemDatabase.find("은괴");
		if(i != null){
			craft_list.put("request silver bar", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("은 원석 조각"), 10) );
			l.add( new Craft(ItemDatabase.find("아데나"), 500) );
			list.put(i, l);
		}
		i = ItemDatabase.find("황금괴");
		if(i != null){
			craft_list.put("request gold bar", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("황금 원석 조각"), 10) );
			l.add( new Craft(ItemDatabase.find("아데나"), 500) );
			list.put(i, l);
		}
		i = ItemDatabase.find("백금괴");
		if(i != null){
			craft_list.put("request platinum ba", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("백금 원석 조각"), 10) );
			l.add( new Craft(ItemDatabase.find("아데나"), 1000) );
			list.put(i, l);
		}
		i = ItemDatabase.find("은 판금");
		if(i != null){
			craft_list.put("request silver plate", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("은괴"), 5) );
			l.add( new Craft(ItemDatabase.find("강철괴"), 3) );
			l.add( new Craft(ItemDatabase.find("아데나"), 1000) );
			list.put(i, l);
		}
		i = ItemDatabase.find("황금 판금");
		if(i != null){
			craft_list.put("request gold plate", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("황금괴"), 5) );
			l.add( new Craft(ItemDatabase.find("강철괴"), 3) );
			l.add( new Craft(ItemDatabase.find("아데나"), 3000) );
			list.put(i, l);
		}
		i = ItemDatabase.find("백금 판금");
		if(i != null){
			craft_list.put("request platinum plate", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("백금괴"), 5) );
			l.add( new Craft(ItemDatabase.find("강철괴"), 3) );
			l.add( new Craft(ItemDatabase.find("아데나"), 10000) );
			list.put(i, l);
		}
		i = ItemDatabase.find("블랙미스릴 판금");
		if(i != null){
			craft_list.put("request black mithril plate", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("오리하루콘 판금"), 1) );
			l.add( new Craft(ItemDatabase.find("황금 판금"), 1) );
			l.add( new Craft(ItemDatabase.find("미스릴 판금"), 1) );
			l.add( new Craft(ItemDatabase.find("블랙 미스릴"), 10) );
			l.add( new Craft(ItemDatabase.find("백금 판금"), 1) );
			l.add( new Craft(ItemDatabase.find("은 판금"), 1) );
			l.add( new Craft(ItemDatabase.find("아데나"), 10000) );
			list.put(i, l);
		}
		i = ItemDatabase.find("블랙미스릴 화살");
		if(i != null){
			craft_list.put("request black mithril arrow", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("백금괴"), 5) );
			l.add( new Craft(ItemDatabase.find("엔트의 줄기"), 10) );
			l.add( new Craft(ItemDatabase.find("아데나"), 1000) );
			list.put(i, l);
		}
		
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "koup1"));
	}

}
