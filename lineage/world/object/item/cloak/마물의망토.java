package lineage.world.object.item.cloak;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class 마물의망토 extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if(item == null)
			item = new 마물의망토();
		return item;
	}
	
	@Override
	protected void toEnchantOption(Character cha, boolean sendPacket) {
		//
		int new_dmg = getDmg();
		int new_sp = getSp();
		
		if(isEquipped() && !cha.isWorldDelete()){
			// 이전에 세팅값 빼기.
			cha.setDynamicAddDmg(cha.getDynamicAddDmg()-getDynamicAddDmg());
			cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow()-getDynamicAddDmgBow());
			cha.setDynamicSp(cha.getDynamicSp()-getDynamicSp());
			// 인첸에따른 새로운값 적용.
			cha.setDynamicAddDmg(cha.getDynamicAddDmg()+new_dmg);
			cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow()+new_dmg);
			cha.setDynamicSp(cha.getDynamicSp()+new_sp);
		}
		setDynamicAddDmg(new_dmg);
		setDynamicAddDmgBow(new_dmg);
		setDynamicSp(new_sp);
		if(sendPacket) {
			if(Lineage.server_version <= 200)
				cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
			else
				cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
		}
	}
	
	private int getDmg() {
		switch(getEnLevel()) {
			case 0:
				return 0;
			case 1:
				return 0;
			case 2:
				return 0;
			case 3:
				return 1;
			case 4:
				return 2;
			case 5:
				return 3;
			case 6:
				return 4;
			case 7:
				return 5;
			case 8:
				return 6;
			case 9:
				return 7;
			default:
				return 0;
		}
	}
	private int getSp() {
		switch(getEnLevel()) {
			case 0:
				return 0;
			case 1:
				return 0;
			case 2:
				return 0;
			case 3:
				return 1;
			case 4:
				return 2;
			case 5:
				return 3;
			case 6:
				return 4;
			case 7:
				return 5;
			case 8:
				return 6;
			case 9:
				return 7;
			default:
				return 0;
		}
	}
}

