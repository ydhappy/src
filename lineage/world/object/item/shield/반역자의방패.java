package lineage.world.object.item.shield;

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

public class 반역자의방패 extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 반역자의방패();
		return item;
	}
	//광용 반역자의 방패
	@Override
	public int toDamageReduction(object attacker, object target) {
		int reduction = 0;
		if(Util.random(0, 99) <= getDynamicReductionPercent()) {
			reduction += getDynamicReductionPercentValue();
			if(Lineage.server_version >= 200)
				target.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), target, 6320), true);
		}
		return reduction;
	}

	@Override
	protected void toEnchantOption(Character cha, boolean sendPacket) {
		setDynamicReductionPercent( getReductionPercent() );
		setDynamicStunDefense( getDynamicStunDefense()+3 );
		setDynamicReductionPercentValue(40);
		
		if(sendPacket) {
			if(Lineage.server_version <= 144)
				cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
			else
				cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
		}
	}

	private int getReductionPercent() {
		return getEnLevel() * 1;
	}
	
}
