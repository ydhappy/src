package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class S_InventorySlot extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, PcInstance pc){
		if(bp == null)
			bp = new S_InventorySlot(pc);
		else
			((S_InventorySlot)bp).toClone(pc);
		return bp;
	}
	
	public S_InventorySlot(PcInstance pc){
		toClone(pc);
	}
	
	public void toClone(PcInstance pc){
		clear();
		writeC(Opcodes.S_OPCODE_BASESTAT);
		writeC(0x41);	// type
		writeC(pc.getInventory().getSlotCount());

		int ring = 0;
		int amulet = 0;
		for(ItemInstance item : pc.getInventory().getSlot()) {
			writeD(item.getObjectId());
			if(item.getSlot() == 18)
				writeD(item.getSlot() + ring++);
			else if(item.getSlot() == 22)
				writeD(item.getSlot() + amulet++);
			else
				writeD(item.getSlot());
		}
	}

}
