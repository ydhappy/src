package lineage.world.object.item.armor;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class RichLobe extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new RichLobe();
		return item;
	}

	@Override
	protected void toEnchantOption(Character cha, boolean sendPacket) {
		super.toEnchantOption(cha, sendPacket);

		//
		int addSp = getSp();
		if(isEquipped() && !cha.isWorldDelete()) {
			// 이전에 세팅값 빼기.
			cha.setDynamicSp(cha.getDynamicSp()-getDynamicSp());
			// 인첸에따른 새로운값 적용.
			cha.setDynamicSp(cha.getDynamicSp()+addSp);
		}
		setDynamicSp(addSp);
		//
		if(sendPacket) {
			if(Lineage.server_version <= 144)
				cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
			else
				cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
		}
	}
	
	protected int getSp() {
		if(getEnLevel()>3)
			return getEnLevel()-3;
		return 0;
	}
	
}
