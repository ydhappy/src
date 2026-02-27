package lineage.world.object.item.scroll;

import lineage.bean.database.Definite;
import lineage.bean.database.Item;
import lineage.database.DefiniteDatabase;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryBress;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryIdentify;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_LetterNotice;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class ScrollLabeledKernodwel extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollLabeledKernodwel();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if (item.getLevelMin() > 0 && item.getLevelMin() > cha.getLevel()) {
			// cha.toSender(new SItemLevelFails(item.Level));
			// 672 : 이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.
			ChattingController.toChatting(cha, String.format("이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.", item.getLevelMin()), Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		if (item.getLevelMax() > 0 && item.getLevelMax() < cha.getLevel()) {
			// 673 : 이 아이템은 %d레벨 이하일때만 사용할 수 있습니다.
			ChattingController.toChatting(cha, String.format("이 아이템은 %d레벨 이하일 때만 사용할 수 있습니다.", item.getLevelMax()), Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		ItemInstance item = cha.getInventory().value(cbp.readD());
		if(item != null){
			if(!item.isDefinite()){
				item.setDefinite(true);
				cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
				if(Lineage.server_version <= 144){
					cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), item));
				}else{
					cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
				}
			}
			Definite d = DefiniteDatabase.find(item.getItem().getNameIdNumber());
			if(d != null)
				cha.toSender(S_InventoryIdentify.clone(BasePacketPooling.getPool(S_InventoryIdentify.class), item, d.getType(), d.getMessage()));
		}

		cha.getInventory().count(this, getCount()-1, true);
	}
	
}
