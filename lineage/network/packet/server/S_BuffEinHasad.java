package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;

public class S_BuffEinHasad extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, int percent){
		if(bp == null)
			bp = new S_BuffEinHasad(percent);
		else
			((S_BuffEinHasad)bp).clone(percent);
		return bp;
	}
	
	public S_BuffEinHasad(int percent){
		clone(percent);
	}
	
	public void clone(int percent){
		clear();
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeD(percent);
//		writeC(0x00);
//		writeC(0x10);
//		writeC(0x27);
//		writeD(0x00);
//		writeH(0x00);
	}

}

/*	아인하사드의 축복 아이콘
--------------------------------------------------------------------------------
S_OPCODE_UNKNOWN2 [Server] 옵코드 83, 길이 14
--------------------------------------------------------------------------------
0000: 53 52 c8 00 00 00 10 27 00 00 00 00 00 00          SR.....'......
 */