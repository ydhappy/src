package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;

public class S_FishingPacket extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp){
		if(bp == null)
			bp = new S_FishingPacket();
		else
			((S_FishingPacket)bp).toClone();
		return bp;
	}
	
	public S_FishingPacket(){
		toClone();
	}
	
	public void toClone(){
		clear();
		
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(0x37);
		writeC(0x02);
	}

}
