package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.world.object.instance.PcInstance;

public class C_DungeonTimeRequest extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_DungeonTimeRequest(data, length);
		else
			((C_DungeonTimeRequest)bp).clone(data, length);
		return bp;
	}
	
	public C_DungeonTimeRequest(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		pc.toSender( S_Message.clone(BasePacketPooling.getPool(S_Message.class), 562, "0") );
		
		return this;
	}

}
