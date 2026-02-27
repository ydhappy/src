package lineage.world.object.item.bundle;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.ItemChanceBundle;
import lineage.database.ItemChanceBundleDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;


public class ChanceBundle extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ChanceBundle();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null && cha.getInventory().getList().size() >= Lineage.inventory_max) {
			ChattingController.toChatting(cha, "인벤토리가 가득찼습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		// 아이템 지급.
		int random = 0;
		int randomCount = 0;
		//double probability = Math.random();
		List<ItemChanceBundle> list = new ArrayList<ItemChanceBundle>();
		ItemChanceBundleDatabase.find(list, getItem().getName());

		if (list.size() < 1)
			return;

		for (;;) {
			if (randomCount++ > 50)
				break;
			
//			if (randomCount++ > list.size())
//				probability = Math.random();
			
			random = Util.random(0, list.size() - 1);
			
			if (list.get(random).getItemCountMin() < 1)
				break;
			
			double probability = Math.random();
			if (probability < list.get(random).getItemChance()) {
//				if (cha instanceof PcRobotInstance) {
//					// 수량 하향.
//					cha.getInventory().count(this, getCount() - 1, true);
//					break;
//				}
				
				ItemChanceBundle ib = list.get(random);
				Item i = ItemDatabase.find(ib.getItem());
				
				if (i != null) {
					ItemInstance temp = cha.getInventory().find(i.getName(), ib.getItemBless(), i.isPiles());
					int count = Util.random(ib.getItemCountMin(), ib.getItemCountMax());

					if (temp != null && (temp.getBress() != list.get(random).getItemBless() || temp.getEnLevel() != ib.getItemEnchant()))
						temp = null;

					if (temp == null) {
						// 겹칠수 있는 아이템이 존재하지 않을경우.
						if (i.isPiles()) {
							temp = ItemDatabase.newInstance(i);
							temp.setObjectId(ServerDatabase.nextItemObjId());
							temp.setBress(ib.getItemBless());
							temp.setEnLevel(ib.getItemEnchant());
							temp.setCount(count);
							temp.setDefinite(true);
							cha.getInventory().append(temp, true);
						} else {
							for (int idx = 0; idx < count; idx++) {
								temp = ItemDatabase.newInstance(i);
								temp.setObjectId(ServerDatabase.nextItemObjId());
								temp.setBress(ib.getItemBless());
								temp.setEnLevel(ib.getItemEnchant());
								temp.setDefinite(true);
								cha.getInventory().append(temp, true);
							}
						}
					} else
						// 겹치는 아이템이 존재할 경우.
						cha.getInventory().count(temp, temp.getCount() + count, true);
					
					// 알림.
					ChattingController.toChatting(cha, String.format("%s(%d) 획득: %s", i.getName(), count, getItem().getName()), Lineage.CHATTING_MODE_MESSAGE);
					// 수량 하향.
					cha.getInventory().count(this, getCount() - 1, true);
				}
				break;
			}
		}
	}
}
