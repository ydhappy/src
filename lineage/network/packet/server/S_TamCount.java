package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;

public class S_TamCount extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, int count){
		if(bp == null)
			bp = new S_TamCount(count);
		else
			((S_TamCount)bp).clone(count);
		return bp;
	}
	
	public S_TamCount(int count){
		clone(count);
	}
	
	public void clone(int count){
		clear();
		writeC(Opcodes.S_OPCODE_CRAFTTABLE);
		writeC(194);							// type
		writeC(0x01);
		writeC(0x08);
		write4(count);							// count
		writeH(0x00);
	}

}
