package lineage.world.object.item.bundle;

import lineage.database.ItemDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.util.Util;
import lineage.world.controller.CraftController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 파란빛나는물고기 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 파란빛나는물고기();
		return item;
	}
	
	static private String[][] rate = {
		{"사파이어", "80"},
		{"마력 회복 물약", "100"},
		{"반짝이는 비늘", "80"},
		{"고급 사파이어", "40"},
		{"진귀한 거북이", "10"},
		{"최고급 사파이어", "5"},
	};

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		//
		cha.getInventory().count(this, getCount()-1, true);
		//
		for(;;) {
			String[] db = rate[Util.random(0, rate.length-1)];
			if(Integer.valueOf(db[1]) <= Util.random(0, 100))
				continue;
			
			CraftController.toCraft(cha, ItemDatabase.find(db[0]), 1, true, 0);
			break;
		}
	}

}
