package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.world.object.instance.ItemInstance;

public class S_InventoryEnchantUpdate extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, ItemInstance item){
		if(bp == null)
			bp = new S_InventoryEnchantUpdate(item);
		else
			((S_InventoryEnchantUpdate)bp).clone(item);
		return bp;
	}
	
	public S_InventoryEnchantUpdate(ItemInstance item){
		clone(item);
	}
	
	public void clone(ItemInstance item){
		clear();
		
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeH(172);
		writeD(item.getObjectId());
		writeC(0x17);
		writeC(0);
		writeH(0);
		writeH(0);
		writeC(item.getEnLevel());
		writeD(item.getObjectId());
		writeD(0);
		writeD(0);
		writeD(item.getBress() >= 128 ? 3 : item.getItem().isTrade() ? 7 : 2);
		writeD(0);
	}

}
