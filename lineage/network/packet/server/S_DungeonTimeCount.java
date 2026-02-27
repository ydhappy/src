package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.ServerBasePacket;
import lineage.network.packet.Opcodes;

public class S_DungeonTimeCount extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, int time) {
		if (bp == null)
			bp = new S_DungeonTimeCount(time);
		else
			((S_DungeonTimeCount) bp).clone(time);
		return bp;
	}

	public S_DungeonTimeCount(int time) {
		clone(time);
	}

	public void clone(int time) {
		clear();
		
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(0x99);
		writeH(time);
		writeH(0x00);
	}

}
