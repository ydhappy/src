package lineage.world.object.item.bundle;

import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 무한의화살통 extends Bundle {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 무한의화살통();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// 22시간마다 한번씩 사용 가능.
		String value = getOption("무한의화살통_시간");
		long time = 0;
		if (value != null)
			time = Long.valueOf(value);
		// 시간확인
		long remain = time - System.currentTimeMillis();
		long hour = remain / (1000 * 3600);
		long minute = (remain % (3600 * 1000)) / 60000;
		if (time > 0 && time > System.currentTimeMillis()) {
			// \f1아무일도 일어나지 않았습니다.
			ChattingController.toChatting(cha, +hour + "시간 " + minute + "분후 사용이 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		// 랜덤 아이템 추출
		// nameid, min, max, chance
		int[][] db = {
				// 은 화살
				{ 93, 4000, 4000, 80, 0, 1, 1 }, 
				// 미스릴 화살
				{ 773, 1000, 1000, 60, 0, 1, 1 }, 
				// 미스릴 화살
				{ 773, 2000, 2000, 40, 0, 1, 1 }, 
				// 미스릴 화살
				{ 773, 3000, 3000, 20, 0, 1, 1 }
				};
		//
		toBundle(cha, db, TYPE.LOOP_1);
		// 횟수확인.
		value = getOption("무한의화살통_갯수");
		if (value == null)
			value = "100";
		int count = Integer.valueOf(value) - 1;
		if (count == 0) {
			cha.getInventory().count(this, getCount() - 1, true);
			return;
		}
		// 옵션 갱신
		setOption("무한의화살통_시간", String.valueOf(System.currentTimeMillis() + (1000 * 60 * 60 * 22)));
		setOption("무한의화살통_갯수", String.valueOf(count));
	}
}
