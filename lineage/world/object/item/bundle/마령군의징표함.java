package lineage.world.object.item.bundle;

import lineage.network.packet.ClientBasePacket;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 마령군의징표함 extends Bundle {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 마령군의징표함();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// nameid, min, max, chance
		int[][] db = {
				// 마령군의 징표
				{3882, 1, 1, 60},
				// 어둠의 광석
				{3876, 1, 3, 15},
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
				// 마령군왕의 로브
				{3855, 1, 1, 2},
				// 마령군왕의 목걸이
				{3859, 1, 1, 2}
		};
		//
//		toBundle(cha, db, TYPE.LOOP_1);
		//
		cha.getInventory().count(this, getCount()-1, true);
	}

}
