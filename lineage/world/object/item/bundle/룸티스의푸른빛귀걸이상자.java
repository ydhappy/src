package lineage.world.object.item.bundle;

import lineage.database.ItemDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.world.controller.CraftController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 룸티스의푸른빛귀걸이상자 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 룸티스의푸른빛귀걸이상자();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// 룸티스의 푸른빛 귀걸이 1개
		CraftController.toCraft(cha, ItemDatabase.find(11995), 1, true, 0);
		// 룸티스의 강화 주문서 8장
		CraftController.toCraft(cha, ItemDatabase.find(11997), 8, true, 0);
		//
		cha.getInventory().count(this, getCount()-1, true);
	}

}
