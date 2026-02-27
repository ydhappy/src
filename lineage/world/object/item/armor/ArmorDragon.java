package lineage.world.object.item.armor;

import lineage.bean.lineage.Inventory;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class ArmorDragon extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ArmorDragon();
		return item;
	}
	
	@Override
	public void toEquipped(Character cha, Inventory inv, int slot){
		super.toEquipped(cha, inv, slot);

		if(equipped)
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 7676), true);

		
		
	}
}
