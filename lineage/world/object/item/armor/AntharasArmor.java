package lineage.world.object.item.armor;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class AntharasArmor extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new AntharasArmor();
		return item;
	}

	@Override
	protected void toEnchantOption(Character cha, boolean sendPacket) {
		super.toEnchantOption(cha, sendPacket);

		//
		int addReduction = getReduction();
		if(isEquipped() && !cha.isWorldDelete()) {
			// 이전에 세팅값 빼기.
			cha.setDynamicReduction(cha.getDynamicReduction()-getDynamicReduction());
			// 인첸에따른 새로운값 적용.
			cha.setDynamicReduction(cha.getDynamicReduction()+addReduction);
		}
		setDynamicReduction(addReduction);
		//
		if(sendPacket) {
			if(Lineage.server_version <= 144)
				cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
			else
				cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
		}
	}
	
	protected int getReduction() {
		if(getEnLevel() == 6)
			return 1;
		else if(getEnLevel() == 7)
			return 2;
		else if(getEnLevel() >= 10)
			return 1;
		return 0;
	}
	
}
