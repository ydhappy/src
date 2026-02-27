package lineage.world.object.item.earring;

import lineage.database.MagicdollListDatabase;
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

public class 룸티스의검은빛귀걸이 extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if(item == null)
			item = new 룸티스의검은빛귀걸이();
		return item;
	}
	
	@Override
	protected void toEnchantOption(Character cha, boolean sendPacket) {
		//
		int new_ac = getAc();
		int new_dmg = getDmg();
		if(isEquipped() && !cha.isWorldDelete()){
			// 이전에 세팅값 빼기.
			cha.setDynamicAc(cha.getDynamicAc()-getDynamicAc());
			cha.setDynamicAddDmg(cha.getDynamicAddDmg()-getDynamicAddDmg());
			cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow()-getDynamicAddDmgBow());
			// 인첸에따른 새로운값 적용.
			cha.setDynamicAc(cha.getDynamicAc()+new_ac);
			cha.setDynamicAddDmg(cha.getDynamicAddDmg()+new_dmg);
			cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow()+new_dmg);
		}
		setDynamicAc(new_ac);
		setDynamicAddDmg(new_dmg);
		setDynamicAddDmgBow(new_dmg);
		if(sendPacket) {
			if(Lineage.server_version <= 200)
				cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
			else
				cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
		}
	}
	
	private int getAc() {
		if(getBress() == 0) {
			if(getEnLevel() >= 3)
				return getEnLevel() + 2;
			else
				return getEnLevel() + 1;
		} else
			return getEnLevel() + 1;
	}

	private int getDmg() {
		if(getEnLevel() >= 3) {
			if(getBress() == 0)
				return getEnLevel() - 2;
			else {
				if(getEnLevel() == 4)
					return 1;
				else
					return getEnLevel() - 3;
			}
		} else
			return 0;
	}
	
	@Override
	public int toDamageAddRate(object attacker, object target) {
		int dmg = 0;
		int checkLevel = getBress()==0 ? 4 : 5;
		if(getEnLevel() >= checkLevel)
			if(Util.random(0, 99) <= 3+(getEnLevel()-checkLevel)) {
				dmg = 20;
				attacker.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), attacker, 13931), true);
			}
		return dmg;
	}

}
