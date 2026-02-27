package lineage.world.object.npc.quest;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.database.ItemDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.controller.CraftController;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestInstance;
import lineage.world.object.item.quest.로빈후드의소개장;

public class 지브릴 extends QuestInstance {

	public 지브릴(Npc npc) {
		super(npc);

		Item i = ItemDatabase.find("에바의 단검");
		if (i != null) {
			craft_list.put("B", i);

			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("고급 다이아몬드"), 10));
			l.add(new Craft(ItemDatabase.find("고급 루비"), 10));
			l.add(new Craft(ItemDatabase.find("고급 사파이어"), 10));
			l.add(new Craft(ItemDatabase.find("고급 에메랄드"), 10));
			list.put(i, l);
		}
		i = ItemDatabase.find("달빛의 정기");
		if (i != null) {
			craft_list.put("D", i);

			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("에바의 단검"), 1));
			l.add(new Craft(ItemDatabase.find("사엘의 반지"), 1));
			list.put(i, l);
		}
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		if (pc.getInventory().find(ItemDatabase.find("달빛의 정기")) != null)
			pc.toSender(new S_Html( this, "zybril19"));
		else {
			if (pc.getInventory().find(ItemDatabase.find("사엘의 반지")) != null)
				pc.toSender(new S_Html( this, "zybril18"));
			else {
				if (pc.getInventory().find(ItemDatabase.find("신성한 에바의 물")) != null)
					pc.toSender(new S_Html( this, "zybril17"));
				else {
					if (pc.getInventory().find(ItemDatabase.find("에바의 단검")) != null)
						pc.toSender(new S_Html( this, "zybril8"));
					else {
						pc.toSender(new S_Html( this, "zybril1"));
					}
				}
			}
		}
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		if (action.equalsIgnoreCase("A")) {
			// 로빈후드님의 편지를 가지고 왔습니다.
			if (pc.getInventory().find(로빈후드의소개장.class) == null)
				pc.toSender(new S_Html( this, "zybril11"));
			else {
				pc.toSender(new S_Html( this, "zybril3"));
			}
		} else if (action.equalsIgnoreCase("B")) {
			// 고급 보석류 10개씩을 건넨다.
			Item craft = craft_list.get(action);
			if (craft != null) {
				List<Craft> l = list.get(craft);
				if (CraftController.isCraft(pc, l, true)) {
					CraftController.toCraft(pc, l);
					CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
					pc.toSender(new S_Html( this, "zybril15"));
				} else {
					pc.toSender(new S_Html( this, "zybril12"));
				}
			}
		} else if (action.equalsIgnoreCase("C")) {
			// 정령의 눈물 10개를 건넨다.
			ItemInstance item = pc.getInventory().find(ItemDatabase.find("정령의 눈물"));
			if (item == null || item.getCount() < 10)
				pc.toSender(new S_Html( this, "zybril13"));
			else {
				pc.getInventory().count(item, item.getCount() - 10, true);
				CraftController.toCraft(this, pc, ItemDatabase.find("신성한 에바의 물"), 1, true, 0, 0, 1);
				pc.toSender(new S_Html( this, "zybril9"));
			}
		} else if (action.equalsIgnoreCase("D")) {
			ItemInstance item = pc.getInventory().find(ItemDatabase.find("로빈후드의 소개장"));
			// 사엘의 반지를 건넨다.
			Item craft = craft_list.get(action);
			if (craft != null) {
				List<Craft> l = list.get(craft);
				if (CraftController.isCraft(pc, l, true)) {
					CraftController.toCraft(pc, l);
					CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
					pc.toSender(new S_Html( this, "zybril10"));
					pc.getInventory().count(item, item.getCount() - 1, true);
				} else {
					pc.toSender(new S_Html( this, "zybril14"));
				}
			}
		}
	}

}