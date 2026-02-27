package lineage.world.object.item.earring;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class 룸티스의붉은빛귀걸이 extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if(item == null)
			item = new 룸티스의붉은빛귀걸이();
		return item;
	}
	
	@Override
	public int toDamageReduction(object attacker, object target) {
		int reduction = 0;
		if(Util.random(0, 99) <= getDynamicReductionPercent()) {
			reduction += getDynamicReductionPercentValue();
			if(Lineage.server_version >= 200)
				target.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), target, Lineage.server_version>=380 ? 12118 : 12118), true);
		}
		return reduction;
	}
	
	@Override
	protected void toEnchantOption(Character cha, boolean sendPacket) {
		int new_hp = getHp();
		int new_reduction = getReduction();
		int new_reduction_percent = getReductionPercent();
		if(isEquipped() && !cha.isWorldDelete()){
			// 이전에 세팅값 빼기.
			cha.setDynamicHp(cha.getDynamicHp()-getDynamicHp());
			// 인첸에따른 새로운값 적용.
			cha.setDynamicHp(cha.getDynamicHp()+new_hp);
		}
		setDynamicHp(new_hp);
		setDynamicReductionPercent(new_reduction_percent);
		setDynamicReductionPercentValue(new_reduction);
		if(sendPacket) {
			if(Lineage.server_version <= 144)
				cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
			else
				cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
		}
	}

	private int getHp() {
		switch(getEnLevel()){
			case 0:
				return 10;
			case 1:
				return 30;
			case 2:
				return 40;
			case 3:
				return 50;
			case 4:
				return 60;
			case 5:
				return 70;
			case 6:
				return 80;
			case 7:
				return 90;
			case 8:
				return 100;
			default:
				return 0;
		}
	}

	private int getReduction() {
		switch(getEnLevel()){
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
				return 3;
			case 7:
				return 4;
			case 8:
				return 5;
			default:
				return 0;
		}
	}

	private int getReductionPercent() {
		switch(getEnLevel()){
			case 0:
				return 0;
			case 1:
				return 0;
			case 2:
				return 0;
			case 3:
				return 0;
			case 4:
				return 0;
			case 5:
				return 2;
			case 6:
				return 3;
			case 7:
				return 4;
			case 8:
				return 5;
			default:
				return 0;
		}
	}
}
