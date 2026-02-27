package lineage.world.object.item.all_night;

import lineage.database.ItemDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.world.controller.CraftController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 테베티칼의보물상자조각 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 테베티칼의보물상자조각();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		//
		ItemInstance item = cha.getInventory().value(cbp.readD());
		switch(getItem().getNameIdNumber()) {
			case 5717:	// 하급
				if(item.getItem().getNameIdNumber() != 5716) {
					super.toClick(cha, cbp);
					return;
				}
				// 잠긴 하급 오시리스의 보물상자
				CraftController.toCraft(this, cha, ItemDatabase.find("잠긴 하급 오시리스의 보물상자"), 1, true, 0, 0, 1);
				break;
			case 5956:	// 상급
				if(item.getItem().getNameIdNumber() != 5955) {
					super.toClick(cha, cbp);
					return;
				}
				// 잠긴 상급 오시리스의 보물상자
				CraftController.toCraft(this, cha, ItemDatabase.find("잠긴 상급 오시리스의 보물상자"), 1, true, 0, 0, 1);
				break;
		}
		// 재료 제거.
		cha.getInventory().count(item, item.getCount()-1, true);
		cha.getInventory().count(this, getCount()-1, true);
	}

}
