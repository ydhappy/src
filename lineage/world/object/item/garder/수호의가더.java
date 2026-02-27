package lineage.world.object.item.garder;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class 수호의가더 extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 수호의가더();
		return item;
	}

	@Override
	protected void toEnchantOption(Character cha, boolean sendPacket) {
		int reduction = getReduction();
		//
		if(isEquipped() && !cha.isWorldDelete()) {
			// 이전에 세팅값 빼기.
			cha.setDynamicReduction(cha.getDynamicReduction()-getDynamicReduction());
			// 인첸에따른 새로운값 적용.
			cha.setDynamicReduction(cha.getDynamicReduction()+reduction);
		}
		setDynamicReduction(reduction);
		
		if(sendPacket) {
			if(Lineage.server_version <= 144)
				cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
			else
				cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
		}
	}

	private int getReduction() {
		int reduction = 0;
		if(getEnLevel() >= 5)
			reduction += 1;
		if(getEnLevel() >= 7)
			reduction += 1;
		if(getEnLevel() >= 9)
			reduction += 1;
		return reduction;
	}

}
