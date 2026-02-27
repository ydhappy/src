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

	public class Randal extends CraftInstance {

		public Randal(Npc npc) {
			super(npc);
			
	// hyper text 패킷 구성에 해당 구간을 npc 이름으로 사용함.
			temp_request_list.add(npc.getNameId());

			// 제작 처리 초기화.
			Item i = ItemDatabase.find("농축 용기의 물약");
			if (i != null) {
				craft_list.put("A", i);

				List<Craft> l = new ArrayList<Craft>();
				l.add(new Craft(ItemDatabase.find("용기의 물약"), 3));
				l.add(new Craft(ItemDatabase.find("아데나"), 1000));
				list.put(i, l);
			}
			//
			i = ItemDatabase.find("농축 집중의 물약");
			if (i != null) {
				craft_list.put("B", i);

				List<Craft> l = new ArrayList<Craft>();
				l.add(new Craft(ItemDatabase.find("엘븐 와퍼"), 3));
				l.add(new Craft(ItemDatabase.find("아데나"), 1000));
				list.put(i, l);
			}
			//
			i = ItemDatabase.find("농축 지혜의 물약");
			if (i != null) {
				craft_list.put("C", i);

				List<Craft> l = new ArrayList<Craft>();
				l.add(new Craft(ItemDatabase.find("지혜의 물약"), 3));
				l.add(new Craft(ItemDatabase.find("아데나"), 500));
				list.put(i, l);
			}
			//
			i = ItemDatabase.find("농축 마력의 물약");
			if (i != null) {
				craft_list.put("D", i);

				List<Craft> l = new ArrayList<Craft>();
				l.add(new Craft(ItemDatabase.find("파란 물약"), 3));
				l.add(new Craft(ItemDatabase.find("아데나"), 1000));
				list.put(i, l);
			}
			//
			i = ItemDatabase.find("농축 속도의 물약");
			if (i != null) {
				craft_list.put("E", i);

				List<Craft> l = new ArrayList<Craft>();
				l.add(new Craft(ItemDatabase.find("초록 물약"), 3));
				l.add(new Craft(ItemDatabase.find("아데나"), 500));
				list.put(i, l);
			}
			//
			i = ItemDatabase.find("농축 악마의 물약");
			if (i != null) {
				craft_list.put("F", i);

				List<Craft> l = new ArrayList<Craft>();
				l.add(new Craft(ItemDatabase.find("악마의 피"), 3));
				l.add(new Craft(ItemDatabase.find("아데나"), 1000));
				list.put(i, l);
			}
			//
			i = ItemDatabase.find("농축 변신의 물약");
			if (i != null) {
				craft_list.put("G", i);

				List<Craft> l = new ArrayList<Craft>();
				l.add(new Craft(ItemDatabase.find("변신 주문서"), 3));
				l.add(new Craft(ItemDatabase.find("아데나"), 1000));
				list.put(i, l);
			}
		}
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "randal0"));
			pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 27806)); 
		}
	}

