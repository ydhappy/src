package lineage.world.object.item.scroll;

import lineage.bean.database.ItemTeleport;
import lineage.database.ItemTeleportDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectLock;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class ScrollTeleport extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ScrollTeleport();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		try {
			if (item.getLevelMin() > 0 && item.getLevelMin() > cha.getLevel()) {
				// 672 : 이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.
				ChattingController.toChatting(cha, String.format("이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.", item.getLevelMin()), Lineage.CHATTING_MODE_MESSAGE);
				cha.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
				return;
			}
			if (item.getLevelMax() > 0 && item.getLevelMax() < cha.getLevel()) {
				// 673 : 이 아이템은 %d레벨 이하일때만 사용할 수 있습니다.
				ChattingController.toChatting(cha, String.format("이 아이템은 %d레벨 이하일 때만 사용할 수 있습니다.", item.getLevelMax()), Lineage.CHATTING_MODE_MESSAGE);
				cha.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
				return;
			}
			// 초기화
			int uid = Integer.valueOf(getItem().getType2().substring(getItem().getType2().indexOf("_") + 1));
			ItemTeleport it = ItemTeleportDatabase.find(uid);
			// 델레포트
			if (ItemTeleportDatabase.toTeleport(it, cha, true)) {
				// 제거
				if(it.isRemove())
					cha.getInventory().count(this, getCount() - 1, true);
			}
		} catch (Exception e) {
			//lineage.share.System.printf("%s : toClick(Character cha, ClientBasePacket cbp)\r\n", Teleport.class.toString());
			//lineage.share.System.println(e);
		}
	}
}
