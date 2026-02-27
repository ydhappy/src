package lineage.world.object.item.earring;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class 룸티스의푸른빛귀걸이 extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if(item == null)
			item = new 룸티스의푸른빛귀걸이();
		return item;
	}
	
	@Override
	protected void toEnchantOption(Character cha, boolean sendPacket) {
		int new_healing = getHealing();
		int new_healing_percent = getHealingPercent();
		int new_ac = getAc();
		if(isEquipped() && !cha.isWorldDelete()){
			// 이전에 세팅값 빼기.
			cha.setDynamicHealing(cha.getDynamicHealing()-getDynamicHealing());
			cha.setDynamicHealingPercent(cha.getDynamicHealingPercent()-getDynamicHealingPercent());
			cha.setDynamicAc(cha.getDynamicAc()-getDynamicAc());
			// 인첸에따른 새로운값 적용.
			cha.setDynamicHealing(cha.getDynamicHealing()+new_healing);
			cha.setDynamicHealingPercent(cha.getDynamicHealingPercent()+new_healing_percent);
			cha.setDynamicAc(cha.getDynamicAc()+new_ac);
		}
		setDynamicHealing(new_healing);
		setDynamicHealingPercent(new_healing_percent);
		setDynamicAc(new_ac);
		if(sendPacket) {
			if(Lineage.server_version <= 144)
				cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
			else
				cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
		}
	}
	
	private int getHealing() {
		switch(getEnLevel()) {
			case 0:
				return 2;
			case 1:
				return 6;
			case 2:
				return 8;
			case 3:
				return 10;
			case 4:
				return 12;
			case 5:
				return 14;
			case 6:
				return 16;
			case 7:
				return 18;
			case 8:
				return 20;
			default:
				return 0;
		}
	}

	private int getHealingPercent() {
		switch(getEnLevel()) {
			case 0:
				return 2;
			case 1:
				return 6;
			case 2:
				return 8;
			case 3:
				return 10;
			case 4:
				return 12;
			case 5:
				return 14;
			case 6:
				return 16;
			case 7:
				return 18;
			case 8:
				return 20;
			default:
				return 0;
		}
	}
	
	private int getAc() {
		switch(getEnLevel()) {
			case 0:
				return 0;
			case 1:
				return 0;
			case 2:
				return 0;
			case 3:
				return 0;
			case 4:
				return 1;
			case 5:
				return 1;
			case 6:
				return 2;
			case 7:
				return 2;
			case 8:
				return 3;
			default:
				return 0;
		}
	}
}
