package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.thread.AiThread;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class C_ObjectAttackThread extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_ObjectAttackThread(data, length);
		else
			((C_ObjectAttackThread)bp).clone(data, length);
		return bp;
	}
	
	public C_ObjectAttackThread(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		long objId = readD();
		
		if(objId != 0) {
			object o = pc.findInsideList(objId);
			if(o != null) {
				// 몬스터 및 엔피시가 사용중인 인공지능 쓰레드에 등록.
				//	: 어차피 isAi에서 프레임에 따른 동작가능여부를 처리하고 있기때문에
				//	: 굳이 따로 또 만들필요성이 없다고 판단.
				pc.setAttackTarget(o);
				pc.setAiStatus(1);
				AiThread.append(pc);
			}
		} else {
			// delete가 아니라 delete_로 처리하므로서 메모리에 뒷처리를 안할 수 잇게 유도.
			//	: pcinstance이기 때문에 메모리 처리하면 문제가 발생할 수 있음.
			pc.setAttackTarget(null);
			pc.setAiStatus(-2);
		}
		
		return this;
	}

}
