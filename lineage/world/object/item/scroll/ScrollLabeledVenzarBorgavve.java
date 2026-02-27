package lineage.world.object.item.scroll;

import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectLock;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.Teleport;

public class ScrollLabeledVenzarBorgavve extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollLabeledVenzarBorgavve();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
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
		boolean isNight = ServerDatabase.isNight();
		  if (isNight && (cha.getMap() >= 53 && cha.getMap() <= 56)) {
			  ChattingController.toChatting(cha, "기란 감옥은 야간에 무작위 텔레포트가 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			  cha.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
			  return;
		  }
		
		// 메모리제거구문인 delete를 시행하면 가지고있던 bress값이 변하므로 아래와같이 임시로 담아서 아래쪽에서 활용.
		int bress = this.bress;

		if (Teleport.onBuff(cha, cbp, bress, true, true)) {
			// 수량 갱신.
			cha.getInventory().count(this, getCount() - 1, true);
			// 이동
			cha.toTeleport(cha.getHomeX(), cha.getHomeY(), cha.getHomeMap(), true);
		}
	}
	}