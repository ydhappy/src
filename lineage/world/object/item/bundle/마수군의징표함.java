package lineage.world.object.item.bundle;

import lineage.network.packet.ClientBasePacket;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 마수군의징표함 extends Bundle {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 마수군의징표함();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// nameid, min, max, chance
		int[][] db = {
				// 마수군의 징표
				{3884, 1, 1, 59},
				// 어둠의 광석
				{3876, 1, 3, 12},
				// 정신지배의 돌
				{3878, 1, 1, 10},
				// 무관의 투구
				{3842, 1, 1, 4},
				// 무관의 망토
				{3843, 1, 1, 4},
				// 무관의 장갑
				{3844, 1, 1, 4},
				// 무관의 부츠
				{3845, 1, 1, 5},
				// 마수군왕의 부츠
				{3856, 1, 1, 2}
		};
		//
//		toBundle(cha, db, TYPE.LOOP_1);
		//
		cha.getInventory().count(this, getCount()-1, true);
	}

}
