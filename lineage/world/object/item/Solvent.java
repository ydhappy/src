package lineage.world.object.item;

import lineage.bean.database.Item;
import lineage.bean.database.Shop;
import lineage.database.ItemDatabase;
import lineage.database.NpcSpawnlistDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.share.Log;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class Solvent extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new Solvent();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (!isClick(cha))
			return;

		if (cha == null || cha.getInventory() == null)
			return;
		//
		ItemInstance item = cha.getInventory().value(cbp.readD());
		if (item == null || item.getItem() == null)
			return;

		// 인첸트된 아이템은 용해제 할 수 없음.
		if (item.getEnLevel() > 0) {
			ChattingController.toChatting(cha, "인챈트된 아이템은 용해할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		// 착용중인 아이템은 할 수 없음.
		if (item.isEquipped()) {
			ChattingController.toChatting(cha, "착용중인 아이템은 용해할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		long solvent_cnt = (getTaxPrice(item) / 2) /5;		
		if (solvent_cnt < 1) {
			// 용해할 수 없습니다.
			ChattingController.toChatting(cha, "용해할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		Item i = ItemDatabase.find("결정체");
		
		if (i != null) {
			ItemInstance temp = cha.getInventory().find(i.getName(), i.isPiles());

			if (temp == null) {
				// 겹칠수 있는 아이템이 존재하지 않을경우.
				if (i.isPiles()) {
					temp = ItemDatabase.newInstance(i);
					temp.setObjectId(ServerDatabase.nextItemObjId());
					temp.setBress(1);
					temp.setEnLevel(0);
					temp.setCount(solvent_cnt);
					temp.setDefinite(false);
					cha.getInventory().append(temp, true);
				} else {
					for (int idx = 0; idx < solvent_cnt; idx++) {
						temp = ItemDatabase.newInstance(i);
						temp.setObjectId(ServerDatabase.nextItemObjId());
						temp.setBress(1);
						temp.setEnLevel(0);
						temp.setDefinite(false);
						cha.getInventory().append(temp, true);
					}
				}
			} else {
				// 겹치는 아이템이 존재할 경우.
				cha.getInventory().count(temp, temp.getCount() + solvent_cnt, true);
			}

			Log.appendItem(cha, "type|용해제 매입금", "매입금|" + solvent_cnt, "아이템|" + item.getItem().getName(), "아이템_objid|" + item.getObjectId());
			
			ChattingController.toChatting(cha, String.format("[용해제] %s(%d) 획득.", i.getName(), solvent_cnt), Lineage.CHATTING_MODE_MESSAGE);
			
			// 아이템 수량 갱신
			cha.getInventory().count(item, item.getCount() - 1, true);
			cha.getInventory().count(this, getCount() - 1, true);
		}
	}

	public long getTaxPrice(ItemInstance item) {
		if (NpcSpawnlistDatabase.sellShop == null || NpcSpawnlistDatabase.sellShop.getNpc() == null || !item.getItem().isSell())
			return 0;
		
		Shop shop = NpcSpawnlistDatabase.sellShop.getNpc().findShopItemId(item.getItem().getName(), item.getBress());		
		if (shop != null && shop.isItemSell()) {
			if (shop.getPrice() != 0)
				return shop.getPrice();
			
			double price = item.getItem().getShopPrice();
			
			
			// 반올림 처리.
			return Math.round(price);
		} else {
			return 0;
		}
	}

}
