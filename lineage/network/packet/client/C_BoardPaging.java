package lineage.network.packet.client;

import lineage.bean.lineage.Board;
import lineage.database.BackgroundDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_BoardView;
import lineage.world.controller.BoardController;
import lineage.world.controller.RankController;
import lineage.world.object.object;
import lineage.world.object.instance.BoardInstance;
import lineage.world.object.instance.PcInstance;

public class C_BoardPaging extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_BoardPaging(data, length);
		else
			((C_BoardPaging)bp).clone(data, length);
		return bp;
	}
	
	public C_BoardPaging(byte[] data, int length){
		clone(data, length);
	}

	@Override
	public BasePacket init(PcInstance pc){
		// 버그방지
		if(!isRead(8) || pc==null || pc.isWorldDelete())
			return this;
		
		object o = pc.findInsideList(readD());
		if(o!=null && o instanceof BoardInstance)
			((BoardInstance)o).toPage(pc, readD());
		else {
			long uid = readD();
			object bo = BackgroundDatabase.find("giran1");
			object ob = BackgroundDatabase.find("danbi1");
			
			if (pc.getBoard() == 3 && bo != null) {
				((BoardInstance)bo).toPage(pc, uid);
			} else if(pc.getBoard() == 2 && ob != null){
				((BoardInstance)ob).toPage(pc, uid);
			}
		
		}
		
		return this;
	}

}
