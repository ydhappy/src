package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;

public class S_MessageYesNo extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, int number){
		return clone(bp, number, null);
	}
	
	static synchronized public BasePacket clone(BasePacket bp, int number, String msg){
		if(bp == null)
			bp = new S_MessageYesNo(number, msg);
		else
			((S_MessageYesNo)bp).clone(number, msg);
		return bp;
	}
	
	public S_MessageYesNo(int number, String msg){
		clone(number, msg);
	}
	
	public void clone(int number, String msg){
		clear();
		writeC(Opcodes.S_OPCODE_YES_NO);
		if(Lineage.server_version >= 360) {
			writeH(0);	// 모름.
			writeD(0);	// uid 구분용으로 사용하는듯.
		}
		writeH(number);
		writeS(msg);
	}
}
