package lineage.world.object.item.bundle;

import lineage.network.packet.ClientBasePacket;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 열린하급오시리스의보물상자 extends Bundle {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 열린하급오시리스의보물상자();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// nameid, min, max, chance, en, bless
		toBundle(cha, new int[][] {
			{ 512, 3, 3, 16000, 0, 1, 1 },			// 다이아몬드
			{ 513, 3, 3, 16000, 0, 1, 1 },			// 루비
			{ 514, 3, 3, 16000, 0, 1, 1 },			// 사파이어
			{ 515, 3, 3, 16000, 0, 1, 1 },			// 에메랄드
			{ 799512, 2, 2, 6000, 0, 1, 1 },		// 고급 다이아몬드
			{ 799513, 2, 2, 6000, 0, 1, 1 },		// 고급 루비
			{ 799514, 2, 2, 6000, 0, 1, 1 },		// 고급 사파이어
			{ 799515, 2, 2, 6000, 0, 1, 1 },		// 고급 에메랄드
			{ 244, 1, 1, 5800, 0, 1, 1 },			// 무기 마법 주문서
			{ 249, 1, 1, 5800, 0, 1, 1 },			// 갑옷 마법 주문서
			{ 789, 1, 1, 500, 0, 1, 1 },		    // 레이피어
			{ 105, 1, 1, 500, 0, 1, 1 },		    // 장궁
			{ 1063, 1, 1, 500, 0, 1, 1 },		    // 마나지팡이
		}, TYPE.LOOP_1);
		//
		cha.getInventory().count(this, getCount() - 1, true);
	}

}
