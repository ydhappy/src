package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;

public class S_CharacterEr extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, int er){
		if(bp == null)
			bp = new S_CharacterEr(er);
		else
			((S_CharacterEr)bp).toClone(er);
		return bp;
	}
	
	public S_CharacterEr(int er){
		toClone(er);
	}
	
	public void toClone(int er) {
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(0x84);
		writeC(er);
	}

}
