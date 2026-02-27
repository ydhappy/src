package lineage.network.packet.client;

import lineage.network.LineageClient;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_BaseStat;

public class C_NewCharRequest extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_NewCharRequest(data, length);
		else
			((C_NewCharRequest)bp).clone(data, length);
		return bp;
	}
	
	public C_NewCharRequest(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(LineageClient c){
		int type = readC();
		if(type == 0x2b)
			c.toSender(S_BaseStat.clone(BasePacketPooling.getPool(S_BaseStat.class), 0x3f));
		
		return this;
	}

}
