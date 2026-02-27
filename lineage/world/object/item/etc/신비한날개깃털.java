package lineage.world.object.item.etc;

import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 신비한날개깃털 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 신비한날개깃털();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// 기절상태
		if (cha.isLock())
			return;
		
		switch(cha.getMap()) {
		case 57:
		case 70:
		case 5124:
		case 5001:
		case 621:
		case 99:
		case 509:
		case 666:
		case 800:
		case 340:
		case 370:
		case 350:
		case 303:
		case 5:
		ChattingController.toChatting(cha, "아무일도 일어나지 않았습니다.", Lineage.CHATTING_MODE_MESSAGE);
		return;
	  }
	
	int x = 0, y = 0, map = 0;
	//
	switch (getItem().getName()) {
	case "신비한 날개깃털":
		map = 621;
		switch (Util.random(0, 2)) {
		case 0:
			x = 32775;
			y = 32867;
			break;
		case 1:
			x = 32779;
			y = 32872;
			break;
		case 2:
			x = 32788;
			y = 32871;
			break;
		}
		break;
	}
	//
	cha.getInventory().count(this, getCount() - 1, true);
	cha.toPotal(x, y, map);
  }
}
