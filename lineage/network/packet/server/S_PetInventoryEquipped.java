package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;

public class S_PetInventoryEquipped extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, object o, ItemInstance item, int invIdx){
		if(bp == null)
			bp = new S_PetInventoryEquipped(o, item, invIdx);
		else
			((S_PetInventoryEquipped)bp).clone(o, item, invIdx);
		return bp;
	}
	
	public S_PetInventoryEquipped(object o, ItemInstance item, int invIdx){
		clone(o, item, invIdx);
	}
	
	public void clone(object o, ItemInstance item, int invIdx){
		clear();

		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(0x25);								// type
		writeC(invIdx);								// slot
		writeD(o.getObjectId());					// 오브젝트 아이디
		writeC(item.isEquipped() ? 0x01 : 0x00);	// 착용여부
		writeC(0x0a);								// 
	}

}
