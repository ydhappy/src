package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;

public class S_SkillListWarrior extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, int lv, boolean append){
		if(bp == null)
			bp = new S_SkillListWarrior(lv, append);
		else
			((S_SkillListWarrior)bp).toClone(lv, append);
		return bp;
	}
	
	public S_SkillListWarrior(int lv, boolean append){
		toClone(lv, append);
	}
	
	public void toClone(int lv, boolean append){
		clear();
		
		writeC(Opcodes.S_OPCODE_CRAFTTABLE);
		if(append) {
			writeH(0x0192);	// 402
			writeC(0x08);
			writeC(lv);		// 1-크래쉬(12119), 2-퓨리(12487,12489), 3-슬레이어(12758), 5-아머가드, 6-타이탄:락(12555), 7-타이탄:블릿(12557), 8-타이탄:매직(12559)
			writeH(0x00);
		} else {
			writeH(0x0191);	// 401
			writeC(0x0a);
			writeC(0x02);
			writeC(0x08);
			writeC(lv);
			writeH(0);
		}
	}

}
