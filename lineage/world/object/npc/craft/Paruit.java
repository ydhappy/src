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
import lineage.share.Lineage;
import lineage.world.object.instance.CraftInstance;
import lineage.world.object.instance.PcInstance;

public class Paruit extends CraftInstance {

	public Paruit(Npc npc) {
		super(npc);

		// hyper text 패킷 구성에 해당 구간을 npc 이름으로 사용함.
		temp_request_list.add(npc.getNameId());
		// 제작 처리 초기화.
		Item i = ItemDatabase.find("바나나 주스");
		if (i != null) {
			craft_list.put("request banana juice", i);

			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("바나나"), 5));
			l.add(new Craft(ItemDatabase.find("달걀"), 1));
			l.add(new Craft(ItemDatabase.find("당근"), 1));
			l.add(new Craft(ItemDatabase.find("레몬"), 1));
			list.put(i, l);
		}

		i = ItemDatabase.find("오렌지 주스");
		if (i != null) {
			craft_list.put("request orange juice", i);

			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("오렌지"), 5));
			l.add(new Craft(ItemDatabase.find("달걀"), 1));
			l.add(new Craft(ItemDatabase.find("당근"), 1));
			l.add(new Craft(ItemDatabase.find("레몬"), 1));
			list.put(i, l);
		}
		
		i = ItemDatabase.find("사과 주스");
		if (i != null) {
			craft_list.put("request apple juice", i);

			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("사과"), 5));
			l.add(new Craft(ItemDatabase.find("달걀"), 1));
			l.add(new Craft(ItemDatabase.find("당근"), 1));
			l.add(new Craft(ItemDatabase.find("레몬"), 1));
			list.put(i, l);
		}
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "paruit01"));
			pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 27806)); 
	}

}
