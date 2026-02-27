package lineage.world.object.item.scroll;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectLock;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.LocationController;
import lineage.world.controller.WantedController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class TOITeleportScroll extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new TOITeleportScroll();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		
		boolean oman = cha.isOman();
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
			cha.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
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

		if(LocationController.isTeleportZone(cha, true, !oman) || oman){
			int floor = Integer.valueOf( getItem().getType2().substring( getItem().getType2().indexOf("_")+1 ) );
			switch(floor){
				case 1:
					cha.setHomeX(32796);
					cha.setHomeY(32800);
					cha.setHomeMap(101);
					break;
				case 2:
					cha.setHomeX(32796);
					cha.setHomeY(32800);
					cha.setHomeMap(102);
					break;
				case 3:
					cha.setHomeX(32796);
					cha.setHomeY(32800);
					cha.setHomeMap(103);
					break;
				case 4:
					cha.setHomeX(32797);
					cha.setHomeY(32798);
					cha.setHomeMap(104);
					break;
				case 5:
					cha.setHomeX(32797);
					cha.setHomeY(32798);
					cha.setHomeMap(105);
					break;
				default:
					if ((cha.getClanName().equalsIgnoreCase(Lineage.new_clan_name) && !Lineage.is_new_clan_oman_top)) {
						ChattingController.toChatting(cha, "신규 혈맹은 이동이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						cha.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
						return;
					}
			}
			cha.toTeleport(cha.getHomeX(), cha.getHomeY(), cha.getHomeMap(), true);
			cha.getInventory().count(this, getCount()-1, true);
		}
	}

}
