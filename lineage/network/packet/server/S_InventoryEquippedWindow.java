package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.world.object.instance.ItemInstance;

public class S_InventoryEquippedWindow extends S_Inventory {

	static synchronized public BasePacket clone(BasePacket bp, ItemInstance item, int slot){
		if(bp == null)
			bp = new S_InventoryEquippedWindow(item, slot);
		else
			((S_InventoryEquippedWindow)bp).clone(item, slot);
		return bp;
	}
	
	public S_InventoryEquippedWindow(ItemInstance item, int slot){
		clone(item, slot);
	}
	
	public void clone(ItemInstance item, int slot){
		clear();

		writeC(Opcodes.S_OPCODE_BASESTAT);
        writeC(0x42);
        writeD(item.getObjectId());
        writeC(slot);
        writeC(item.isEquipped() ? 0x01 : 0x00);
	}

}
