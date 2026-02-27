package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;

public class S_CharacterDg extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, boolean init, int dg){
		if(bp == null)
			bp = new S_CharacterDg(init, dg);
		else
			((S_CharacterDg)bp).toClone(init, dg);
		return bp;
	}
	
	public S_CharacterDg(boolean init, int dg){
		toClone(init, dg);
	}
	
	public void toClone(boolean init, int dg) {
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(init ? 0x58 : 0x65);
		writeC(dg / 10);
	}

}
