package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.world.object.Character;

public class S_CharacterStat2 extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, Character cha){
		if(bp == null)
			bp = new S_CharacterStat2(cha);
		else
			((S_CharacterStat2)bp).clone(cha);
		return bp;
	}
	
	public S_CharacterStat2(Character cha){
		clone(cha);
	}
	
	public void clone(Character cha){
		clear();

		writeC(Opcodes.S_OPCODE_STAT);
		writeC(cha.getTotalStr());
		writeC(cha.getTotalInt());
		writeC(cha.getTotalWis());
		writeC(cha.getTotalDex());
		writeC(cha.getTotalCon());
		writeC(cha.getTotalCha());
		writeC(cha.getTotalAc());
	}

}
