package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;

public class S_BuffRoyal extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, int type, int time, int ac){
		if(bp == null)
			bp = new S_BuffRoyal(type, time, ac);
		else
			((S_BuffRoyal)bp).clone(type, time, ac);
		return bp;
	}
	
	public S_BuffRoyal(int type, int time, int ac){
		clone(type, time, ac);
	}
	
	public void clone(int type, int time, int ac){
		clear();
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(0x16);
		writeC(type);			// 113(글로잉) 114(샤이닝) 116(브레이브) 119(아바타)
		writeH(time);			// 시간
		writeC((266-ac));		// 방어력
	}

}
