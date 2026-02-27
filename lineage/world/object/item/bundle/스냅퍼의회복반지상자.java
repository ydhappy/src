package lineage.world.object.item.bundle;

import lineage.database.ItemDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.world.controller.CraftController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 스냅퍼의회복반지상자 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 스냅퍼의회복반지상자();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// 스냅퍼의 회복 반지 1개
		CraftController.toCraft(cha, ItemDatabase.find(16900), 1, true, 0);
		// 스냅퍼의 반지 강화 주문서 8장
		CraftController.toCraft(cha, ItemDatabase.find(16895), 8, true, 0);
		//
		cha.getInventory().count(this, getCount()-1, true);
	}

}
