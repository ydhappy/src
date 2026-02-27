package lineage.network.packet.server;

import java.util.List;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;

public class S_BlockNameResult extends ServerBasePacket {

	static public final int LIST = 17;
	static public final int ADD = 18;
	static public final int REMOVE = 19;
	
	static synchronized public BasePacket clone(BasePacket bp, int type, Object o){
		if(bp == null)
			bp = new S_BlockNameResult(type, o);
		else
			((S_BlockNameResult)bp).toClone(type, o);
		return bp;
	}
	
	public S_BlockNameResult(int type, Object o){
		toClone(type, o);
	}
	
	public void toClone(int type, Object o){
		clear();
		
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(type);
		switch(type) {
			case LIST:
				List<String> list = (List<String>)o;
				writeC(list.size());
				for(String name : list)
					writeS(name);
				break;
			case ADD:
			case REMOVE:
				writeS((String)o);
				break;
		}
	}

}
