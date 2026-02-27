package lineage.world.object.item.earring;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class 룸티스의보랏빛귀걸이 extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if(item == null)
			item = new 룸티스의보랏빛귀걸이();
		return item;
	}
	
	@Override
	protected void toEnchantOption(Character cha, boolean sendPacket) {
		int new_mp = getMp();
		int new_mr = getMr();
		int new_sp = getSp();
		if(isEquipped() && !cha.isWorldDelete()){
			// 이전에 세팅값 빼기.
			cha.setDynamicMp(cha.getDynamicMp()-getDynamicMp());
			cha.setDynamicMr(cha.getDynamicMr()-getDynamicMr());
			cha.setDynamicSp(cha.getDynamicSp()-getDynamicSp());
			// 인첸에따른 새로운값 적용.
			cha.setDynamicMp(cha.getDynamicMp()+new_mp);
			cha.setDynamicMr(cha.getDynamicMr()+new_mr);
			cha.setDynamicSp(cha.getDynamicSp()+new_sp);
		}
		setDynamicMp(new_mp);
		setDynamicMr(new_mr);
		setDynamicSp(new_sp);
		if(sendPacket) {
			if(Lineage.server_version <= 144)
				cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
			else
				cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
		}
	}
	
	private int getMp() {
		switch(getEnLevel()) {
			case 0:
				return 5;
			case 1:
				return 15;
			case 2:
				return 20;
			case 3:
				return 35;
			case 4:
				return 40;
			case 5:
				return 55;
			case 6:
				return 60;
			case 7:
				return 75;
			case 8:
				return 100;
			default:
				return 0;
		}
	}

	private int getMr() {
		switch(getEnLevel()) {
			case 0:
				return 2;
			case 1:
				return 5;
			case 2:
				return 6;
			case 3:
				return 7;
			case 4:
				return 8;
			case 5:
				return 9;
			case 6:
				return 10;
			case 7:
				return 12;
			case 8:
				return 15;
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
				return 1;
			case 5:
				return 2;
			case 6:
				return 2;
			case 7:
				return 3;
			case 8:
				return 3;
			default:
				return 0;
		}
	}
}
