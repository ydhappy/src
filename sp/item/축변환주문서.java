package sp.item;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryBress;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;

public class 축변환주문서 extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 축변환주문서();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		long objId = cbp.readD();
		ItemInstance item = cha.getInventory().value(objId);
		if(item!=null && (item instanceof ItemWeaponInstance || item instanceof ItemArmorInstance)) {
			if(item.getBress() >= 0) {
				item.setBress(0);
				cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
				//
				cha.getInventory().count(this, getCount()-1, true);
				ChattingController.toChatting(cha, String.format("'%s' 아이템이 변경 되었습니다.", item.toStringDB()), 20);
				return;
			}
			ChattingController.toChatting(cha, "봉인된 아이템에 사용할 수 없습니다.", 20);
			return;
		}
		//
		ChattingController.toChatting(cha, "아무일도 일어나지 않았습니다.", 20);
	}

}
