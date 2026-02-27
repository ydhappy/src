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
import lineage.network.packet.server.S_SoundEffect;
import lineage.world.object.instance.CraftInstance;
import lineage.world.object.instance.PcInstance;
	
	public class mubni extends CraftInstance {

	public mubni(Npc npc) {
	super(npc);

		// hyper text 패킷 구성에 해당 구간을 npc 이름으로 사용함.
		temp_request_list.add(npc.getNameId());
		
		// 제작 처리 초기화. 여기서부터 부적
		Item i = ItemDatabase.find("봉인된 오만의 탑 2층 이동 부적");
		if (i != null) {
			craft_list.put("request book of oman tower 2", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("봉인된 오만의 탑 1층 이동 부적"), 1));
			l.add(new Craft(ItemDatabase.find("아데나"), 10000000));
			list.put(i, l);
		}
		
		i = ItemDatabase.find("봉인된 오만의 탑 3층 이동 부적");
		if (i != null) {
			craft_list.put("request book of oman tower 3", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("봉인된 오만의 탑 2층 이동 부적"), 1));
			l.add(new Craft(ItemDatabase.find("아데나"), 10000000));
			list.put(i, l);
		}
		i = ItemDatabase.find("봉인된 오만의 탑 4층 이동 부적");
		if (i != null) {
			craft_list.put("request book of oman tower 4", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("봉인된 오만의 탑 3층 이동 부적"), 1));
			l.add(new Craft(ItemDatabase.find("아데나"), 10000000));
			list.put(i, l);
		}
		i = ItemDatabase.find("봉인된 오만의 탑 5층 이동 부적");
		if (i != null) {
			craft_list.put("request book of oman tower 5", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("봉인된 오만의 탑 4층 이동 부적"), 1));
			l.add(new Craft(ItemDatabase.find("아데나"), 10000000));
			list.put(i, l);
		}
		i = ItemDatabase.find("오만의 탑 2층 이동 주문서");
		if (i != null) {
			craft_list.put("request scroll of oman tower 2f", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("오만의 탑 1층 이동 주문서"), 2));
			l.add(new Craft(ItemDatabase.find("아데나"), 30000));
			list.put(i, l);
		}
		i = ItemDatabase.find("오만의 탑 3층 이동 주문서");
		if (i != null) {
			craft_list.put("request scroll of oman tower 3f", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("오만의 탑 2층 이동 주문서"), 2));
			l.add(new Craft(ItemDatabase.find("아데나"), 30000));
			list.put(i, l);
		}
		i = ItemDatabase.find("오만의 탑 4층 이동 주문서");
		if (i != null) {
			craft_list.put("request scroll of oman tower 4f", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("오만의 탑 3층 이동 주문서"), 2));
			l.add(new Craft(ItemDatabase.find("아데나"), 30000));
			list.put(i, l);
		}
		i = ItemDatabase.find("오만의 탑 5층 이동 주문서");
		if (i != null) {
			craft_list.put("request scroll of oman tower 5f", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("오만의 탑 4층 이동 주문서"), 2));
			l.add(new Craft(ItemDatabase.find("아데나"), 30000));
			list.put(i, l);
		}
		i = ItemDatabase.find("오만의 탑 1층 이동 주문서");
		if (i != null) {
			craft_list.put("request buy of oman tower 11f", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("아데나"), 50000));
			list2.put("request buy of oman tower 11f", l);
		}
		i = ItemDatabase.find("오만의 탑 2층 이동 주문서");
		if (i != null) {
			craft_list.put("request buy of oman tower 21f", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("아데나"), 100000));
			list2.put("request buy of oman tower 21f", l);
		}
		i = ItemDatabase.find("오만의 탑 3층 이동 주문서");
		if (i != null) {
			craft_list.put("request buy of oman tower 31f", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("아데나"), 150000));
			list2.put("request buy of oman tower 31f", l);
		}
		i = ItemDatabase.find("오만의 탑 4층 이동 주문서");
		if (i != null) {
			craft_list.put("request buy of oman tower 41f", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("아데나"), 200000));
			list2.put("request buy of oman tower 41f", l);
		}
		i = ItemDatabase.find("오만의 탑 5층 이동 주문서");
		if (i != null) {
			craft_list.put("request buy of oman tower 51f", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("아데나"), 250000));
			list2.put("request buy of oman tower 51f", l);
		}
		i = ItemDatabase.find("환상의 오만의 탑 지배 부적");
		if (i != null) {
			craft_list.put("F", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("오만의 탑 1층 지배 부적"), 1));
			l.add(new Craft(ItemDatabase.find("오만의 탑 2층 지배 부적"), 1));
			l.add(new Craft(ItemDatabase.find("오만의 탑 3층 지배 부적"), 1));
			l.add(new Craft(ItemDatabase.find("오만의 탑 4층 지배 부적"), 1));
			l.add(new Craft(ItemDatabase.find("오만의 탑 5층 지배 부적"), 1));
			l.add(new Craft(ItemDatabase.find("아데나"), 5000000));
			list.put(i, l);
	}
}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "mubni"));
		pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 27825)); 
	}
}
