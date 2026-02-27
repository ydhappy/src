package lineage.world.object.item.scroll.newscroll;

import lineage.util.Util;
import lineage.bean.database.Definite;
import lineage.database.DefiniteDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryBress;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryIdentify;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class KernodwelWeaponHpMpNomal extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new KernodwelWeaponHpMpNomal();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		ItemInstance item = cha.getInventory().value(cbp.readD());
		if(!(item.getItem().getType1().equalsIgnoreCase("weapon"))){
			ChattingController.toChatting(cha, String.format("\\fT 무기에만 사용 하실 수 있습니다.", 20));
			return;
		}
		if((item.getItem().getType2().equalsIgnoreCase("etc")) && (item.getItem().getType1().equalsIgnoreCase("weapon"))){
			ChattingController.toChatting(cha, String.format("\\fT 화살에는 사용하실 수 없습니다.", 20));
			return;
		}
		if(item.isEquipped()){
				ChattingController.toChatting(cha, String.format("\\fT 장비를 착용한 상태로 확인 하실 수 없습니다."), 20);
				return;
			}
		if(item != null){
		//	if(!item.isDefinite()){
				item.setDefinite(true);
				cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
				if(Lineage.server_version <= 144){
					cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), item));
				}else{
					cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
				}
					item.setAdd_Hp(Util.random(0, 500));
					item.setAdd_Mana(Util.random(0, 500));
					cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));

					ChattingController.toChatting(cha, String.format("\\fT    [Hp:%d][Mp:%d]",item.getAdd_Hp(),item.getAdd_Mana()), 20);
				
		//	}

		}


		cha.getInventory().count(this, getCount()-1, true);
	}

}
