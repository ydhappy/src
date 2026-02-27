package lineage.world.object.item.bundle;

import lineage.database.ItemDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.util.Util;
import lineage.world.controller.CraftController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 초록빛나는물고기 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 초록빛나는물고기();
		return item;
	}
	
	static private String[][] rate = {
		{"에메랄드", "80"},
		{"강화 속도 향상 물약", "100"},
		{"반짝이는 비늘", "80"},
		{"고급 에메랄드", "40"},
		{"진귀한 거북이", "10"},
		{"최고급 에메랄드", "5"},
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
