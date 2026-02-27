package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;

public class S_MessageGreen extends ServerBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, int type, String text) {
		if (!Lineage.is_blue_message)
			return bp;
		
		if (bp == null)
			bp = new S_MessageGreen(type, text);
		else
			((S_MessageGreen) bp).toClone(type, text);
		return bp;
	}

	public S_MessageGreen(int type, String text) {
		toClone(type, text);
	}

	public void toClone(int type, String text) {
		clear();
		writeC(Opcodes.S_OPCODE_BLUEMESSAGE);
		writeH(type);
		writeC(1);
		writeS(text);
		writeH(0x00);
	}

}