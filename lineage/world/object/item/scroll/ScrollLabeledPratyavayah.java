package lineage.world.object.item.scroll;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryBress;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class ScrollLabeledPratyavayah extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollLabeledPratyavayah();
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
		// \f1누군가가 도와주는 것같습니다.
		cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 155));
		ItemInstance item = null;
		if(bress == 0){
			// 축프라 처리. 원하는 아이템을 풀 수 있음.
//			item = cha.getInventory().value(cbp.readD());
			for (ItemInstance i : cha.getInventory().getList()) {
			if(i!=null && i.getBress()==2){
				i.setBress(1);
				if(Lineage.server_version>=144)
					cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), i));
			}
			}
		}else{
			// 걍 처리 착용중인 아이템 목록에서만 처리하면 됨.
			for(int i=0 ; i<=Lineage.SLOT_AMULET5 ; ++i){
				item = cha.getInventory().getSlot(i);
				// 착용상태이며, 저주라면 보통으로 변경하기.
				if(item!=null && item.getBress()==2){
					item.setBress(1);
					if(Lineage.server_version>=144)
						cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
				}
			}
		}

		cha.getInventory().count(this, getCount()-1, true);
	}

}
