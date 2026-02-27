package lineage.world.object.item.quest;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectLock;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class RedKey extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new RedKey();
		return item;
	}
	
	public RedKey(){
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getLevel() > 29) {
			ChattingController.toChatting(cha, String.format("30레벨 이상은 사용할 수 없습니다.", Lineage.oman_min_level), Lineage.CHATTING_MODE_MESSAGE);
			cha.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
			return;
		}
		if (!checkMap(cha)) {
			ChattingController.toChatting(cha, "해당 맵에서 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		// 파고가 있는 수련동굴로 이동.
		switch(Util.random(0, 3)) {
			case 0:
				cha.toPotal(32731, 32807, 87);
				break;
			case 1:
				cha.toPotal(32731, 32803, 87);
				break;
			case 2:
				cha.toPotal(32727, 32803, 87);
				break;
			case 3:
				cha.toPotal(32727, 32807, 87);
				break;
		}
	}
	private boolean checkMap(Character cha) {
		switch (cha.getMap()) {
		case 57:
		case 70:
		case 87:
		case 5124:
		case 5001:
		case 621:
		case 99:
		case 666:
		case 800:
		case 340:
		case 370:
		case 350:
		case 780:
		case 781:
		case 782:
		case 303:
			return false;
		}
		
		return true;
	}
}

