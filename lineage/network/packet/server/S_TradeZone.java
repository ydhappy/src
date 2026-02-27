package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;

public class S_TradeZone extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, int action){
		if(bp == null)
			bp = new S_TradeZone(action);
		else
			((S_TradeZone)bp).toClone(action);
		return bp;
	}
	
	public S_TradeZone(int action){
		toClone(action);
	}
	
	public void toClone(int action){
		clear();

		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(0xc6);
		writeD(action);
		writeC(0x28);
		writeD(0);
		writeH(0);
		writeC(0);
	}

}
