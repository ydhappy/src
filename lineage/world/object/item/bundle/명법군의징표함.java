package lineage.world.object.item.bundle;

import lineage.network.packet.ClientBasePacket;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 명법군의징표함 extends Bundle {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 명법군의징표함();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// nameid, min, max, chance
		int[][] db = {
				// 명법군의 징표
				{3880, 1, 1, 65},
				// 어둠의 광석
				{3876, 1, 3, 12},
				// 정신지배의 돌
				{3878, 1, 1, 10},
				// 신관의 투구
				{3849, 1, 1, 3},
				// 신관의 망토
				{3850, 1, 1, 3},
				// 신관의 장갑
				{3851, 1, 1, 3},
				// 신관의 부츠
				{3852, 1, 1, 3},
				// 명법군왕의 망토
				{3854, 1, 1, 1},
		};
		//
//		toBundle(cha, db, TYPE.LOOP_1);
		//
		cha.getInventory().count(this, getCount()-1, true);
	}

}
