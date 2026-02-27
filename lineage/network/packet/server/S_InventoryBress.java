package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.world.object.instance.ItemInstance;

public class S_InventoryBress extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, ItemInstance item){
		if(bp == null)
			bp = new S_InventoryBress(item);
		else
			((S_InventoryBress)bp).clone(item);
		return bp;
	}
	
	public S_InventoryBress(ItemInstance item){
		clone(item);
	}
	
	public void clone(ItemInstance item){
		clear();

		writeC(Opcodes.S_OPCODE_ItemBressChange);
		writeD(item.getObjectId());
		// 0 : 축복 1: 보통 2: 저주 3: 미확인 128: 축봉인 129: 봉인 130: 저주봉인 131: 미확인봉인
		writeC(item.getBressPacket());
	}
}
