package lineage.network.packet.server;

import lineage.bean.lineage.Useshop;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class S_ObjectAction extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, object o){
		if(bp == null)
			bp = new S_ObjectAction(o);
		else
			((S_ObjectAction)bp).clone(o);
		return bp;
	}

	static synchronized public BasePacket clone(BasePacket bp, object o, int action){
		if(bp == null)
			bp = new S_ObjectAction(o, action);
		else
			((S_ObjectAction)bp).clone(o, action);
		return bp;
	}

	static synchronized public BasePacket clone(BasePacket bp, object o, Useshop us, int temp){
		if(bp == null)
			bp = new S_ObjectAction(o, us, temp);
		else
			((S_ObjectAction)bp).clone(o, us, temp);
		return bp;
	}

	static synchronized public BasePacket clone(BasePacket bp, object o, int x, int y){
		if(bp == null)
			bp = new S_ObjectAction(o, x, y);
		else
			((S_ObjectAction)bp).clone(o, x, y);
		return bp;
	}
	
	public S_ObjectAction(object o){
		clone(o);
	}
	
	public S_ObjectAction(object o, int action){
		clone(o, action);
	}
	
	public S_ObjectAction(object o, Useshop us, int temp){
		clone(o, us, temp);
	}
	
	public S_ObjectAction(object o, int x, int y){
		clone(o, x, y);
	}
	
	public void clone(object o){
		clear();
		writeC(Opcodes.S_OPCODE_DOACTION);
		writeD(o.getObjectId());
		writeC(o.getGfxMode());
	}
	
	public void clone(object o, int action){
//		if (o instanceof PcInstance && (action == 18 || action == 19)) {
//			PcInstance pc = (PcInstance) o;
//			pc.isFrameSpeed(action);
//		}
		clear();
		writeC(Opcodes.S_OPCODE_DOACTION);
		writeD(o.getObjectId());
		writeC(action);
	}
	
	public void clone(object o, Useshop us, int temp){
		clear();
		writeC(Opcodes.S_OPCODE_DOACTION);
		writeD(o.getObjectId());
		writeC(o.getGfxMode());
		if(us.getMsg() != null)
			writeB(us.getMsg());
		writeC(temp);
	}
	
	public void clone(object o, int x, int y){
		clear();
		writeC(Opcodes.S_OPCODE_DOACTION);
		writeD(o.getObjectId());
		writeC(o.getGfxMode());
		writeH(x);
		writeH(y);
	}
}
