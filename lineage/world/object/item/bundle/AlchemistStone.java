package lineage.world.object.item.bundle;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class AlchemistStone extends Bundle {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new AlchemistStone();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// 24시간마다 한번씩 사용 가능.
		String value = getOption("AlchemistStone_Time");
		long time = 0;
		if(value != null)
			time = Long.valueOf(value);
		// 시간확인
		if(time>0 && time>System.currentTimeMillis()) {
			// \f1아무일도 일어나지 않았습니다.
			cha.toSender( S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79) );
			return;
		}
		// 랜덤 아이템 추출
		// nameid, min, max, chance
		int[][] db = {
				// 지혜의 물약
				{944, 1, 10, 20},
				// 엘븐 와퍼
				{110, 1, 10, 20},
				// 강화 속도향상 물약
				{1652264, 1, 10, 20},
				// 고대의 체력 회복제
				{2575, 1, 100, 10},
				// 고대의 고급 체력 회복제
				{2576, 1, 200, 10},
				// 고대의 강력 체력 회복제
				{2577, 1, 300, 10},
				// 정신력의 물약
				{3370, 1, 3, 10},
		};
		//
//		toBundle(cha, db, TYPE.LOOP_1);
		// 횟수확인.
		value = getOption("AlchemistStone_Count");
		if(value == null)
			value = "100";
		int count = Integer.valueOf(value) - 1;
		if(count == 0) {
			cha.getInventory().count(this, getCount()-1, true);
			return;
		}
		// 옵션 갱신
		setOption("AlchemistStone_Time", String.valueOf(System.currentTimeMillis()+(1000*60*60*22)));
		setOption("AlchemistStone_Count", String.valueOf(count));
	}
}
