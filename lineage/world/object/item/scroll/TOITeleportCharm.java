package lineage.world.object.item.scroll;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectLock;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.WantedController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class TOITeleportCharm extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new TOITeleportCharm();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// 오만의 탑 이동 부적 1~10층 해당맵에서는 사용불가능하게
		switch (cha.getMap()) {
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
		case 780:
		case 781:
		case 782:
		case 5:
			ChattingController.toChatting(cha, "해당 맵에서 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		if (cha.getLevel() < Lineage.oman_min_level) {
			ChattingController.toChatting(cha, String.format("오만의 탑은 %d레벨 이상부터 사용 가능합니다.", Lineage.oman_min_level), Lineage.CHATTING_MODE_MESSAGE);
			cha.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
			return;
		}

		if (!WantedController.checkWantedPc(cha)) {
			ChattingController.toChatting(cha, "수배 상태에서만 사용이 가능 합니다.", Lineage.CHATTING_MODE_MESSAGE);
			cha.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
			return;
		}
		//
		int x = 0, y = 0, map = 0;
		//
		switch (getItem().getName()) {
		case "오만의 탑 1층 이동 부적":
		case "오만의 탑 1층 지배 부적":
			map = 101;
			switch (Util.random(0, 1)) {
			case 0:
				x = 32731;
				y = 32798;
				break;
			case 1:
				x = 32730;
				y = 32798;
				break;
			}
			break;
		case "오만의 탑 2층 이동 부적":
		case "오만의 탑 2층 지배 부적":
			map = 102;
			switch (Util.random(0, 1)) {
			case 0:
				x = 32797;
				y = 32798;
				break;
			case 1:
				x = 32796;
				y = 32798;
				break;
			}
			break;
		case "오만의 탑 3층 이동 부적":
		case "오만의 탑 3층 지배 부적":
			map = 103;
			switch (Util.random(0, 1)) {
			case 0:
				x = 32797;
				y = 32798;
				break;
			case 1:
				x = 32796;
				y = 32798;
				break;
			}
			break;
		case "오만의 탑 4층 이동 부적":
		case "오만의 탑 4층 지배 부적":
			map = 104;
			switch (Util.random(0, 1)) {
			case 0:
				x = 32797;
				y = 32798;
				break;
			case 1:
				x = 32796;
				y = 32798;
				break;
			}
			break;
		case "오만의 탑 5층 이동 부적":
		case "오만의 탑 5층 지배 부적":
			map = 105;
			switch (Util.random(0, 1)) {
			case 0:
				x = 32797;
				y = 32798;
				break;
			case 1:
				x = 32796;
				y = 32798;
				break;
			}
			break;
		}
		cha.toPotal(x, y, map);
	}
}
