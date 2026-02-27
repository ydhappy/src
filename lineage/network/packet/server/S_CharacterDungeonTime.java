package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.world.object.instance.PcInstance;

public class S_CharacterDungeonTime extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, PcInstance pc){
		if(bp == null)
			bp = new S_CharacterDungeonTime(pc);
		else
			((S_CharacterDungeonTime)bp).toClone(pc);
		return bp;
	}
	
	public S_CharacterDungeonTime(PcInstance pc){
		toClone(pc);
	}
	
	public void toClone(PcInstance pc) {
		clear();
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(159);		// type
		writeD(7);			// size
		// 반복
		writeD(1);			// 
        writeS("$12125");	// 기란
        writeD(180);		// 분단위
		writeD(2); 
        writeS("$6081");	// 상아
        writeD(60);
		writeD(6);
        writeS("$14250");
        writeD(120);
		writeD(15);
        writeS("$13527");
        writeD(60);
		writeD(19);
        writeS("$18297");
        writeD(30);
		writeD(500);
        writeS("$19375");
        writeD(30);
		writeD(3);
        writeS("$12126");
        writeD(120);
		writeH(8);
	}


}
