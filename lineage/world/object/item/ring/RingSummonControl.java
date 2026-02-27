package lineage.world.object.item.ring;

import lineage.bean.lineage.Inventory;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Ability;
import lineage.network.packet.server.S_Html;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class RingSummonControl extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new RingSummonControl();
		return item;
	}
	
	@Override
	public void toEquipped(Character cha, Inventory inv, int slot){
		super.toEquipped(cha, inv, slot);
		cha.toSender(S_Ability.clone(BasePacketPooling.getPool(S_Ability.class), 5, equipped));
	}

}