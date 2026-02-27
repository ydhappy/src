package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;

public class S_CharacterPassworld extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, int type){
		if(bp == null)
			bp = new S_CharacterPassworld(type);
		else
			((S_CharacterPassworld)bp).toClone(type);
		return bp;
	}
	
	public S_CharacterPassworld(int type){
		toClone(type);
	}
	
	public void toClone(int type){
		clear();
		
		writeC(Opcodes.S_OPCODE_BASESTAT);
		writeC(type);
	}

}
