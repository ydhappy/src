package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_PetInventory;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.PetInstance;

public class C_PetStatus extends ClientBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_PetStatus(data, length);
		else
			((C_PetStatus)bp).clone(data, length);
		return bp;
	}
	
	public C_PetStatus(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc) {
		// 펫 인벤토리 전송.
		long objId = readD();
		object o = pc.getSummon().find(objId);
		if(o!=null && o instanceof PetInstance)
			pc.toSender(S_PetInventory.clone(BasePacketPooling.getPool(S_PetInventory.class), (PetInstance)o));
		
		return this;
	}

}
