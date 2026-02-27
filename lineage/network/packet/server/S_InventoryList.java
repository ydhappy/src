package lineage.network.packet.server;

import lineage.bean.lineage.Inventory;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.world.object.instance.ItemInstance;

public class S_InventoryList extends S_InventoryAdd {

	static synchronized public BasePacket clone(BasePacket bp, Inventory inv){
		if(bp == null)
			bp = new S_InventoryList(inv);
		else
			((S_InventoryList)bp).clone(inv);
		return bp;
	}
	
	public S_InventoryList(Inventory inv){
		super(null);
		clone(inv);
	}
	
	public void clone(Inventory inv){
		clear();
		
		writeC(Opcodes.S_OPCODE_ITEMLIST);
		writeC(inv.getList().size());
		
		for(ItemInstance item : inv.getList())
			packet(item);
	}
}
