package lineage.network.packet.client;

import lineage.bean.lineage.Board;
import lineage.database.BackgroundDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_BoardView;
import lineage.plugin.PluginController;
import lineage.world.controller.BoardController;
import lineage.world.controller.QuestTodayController;
import lineage.world.controller.RankController;
import lineage.world.object.object;
import lineage.world.object.instance.BoardInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestTodayBoardInstance;
import lineage.world.object.instance.RankBoardInstance;

public class C_BoardView extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_BoardView(data, length);
		else
			((C_BoardView)bp).clone(data, length);
		return bp;
	}
	
	public C_BoardView(byte[] data, int length){
		clone(data, length);
	}

	@Override
	public BasePacket init(PcInstance pc){
		// 버그방지
		if(!isRead(8) || pc==null || pc.isWorldDelete())
			return this;
		
		object o = pc.findInsideList(readD());
		if(o!=null && o instanceof BoardInstance) {
			if(PluginController.init(C_BoardView.class, "init", this, pc, o) == null) {
				if(o instanceof RankBoardInstance){
					RankController.toView(pc, readD());
				}else if(o instanceof QuestTodayBoardInstance) {
					QuestTodayController.toView(pc, readD());
				}else{
					BoardController.toView(pc, (BoardInstance)o, readD());					
				}
			}
		} else {
			
			long uid = readD();
			Board b = BoardController.find("danbi1", uid);
			Board bi = BoardController.find("giran1", uid);
			
			if (pc.getBoard() == 3 && bi != null) {
				pc.toSender(S_BoardView.clone(BasePacketPooling.getPool(S_BoardView.class), bi));
				pc.setBoard(0);
			} else if (pc.getBoard() == 1) {
				RankController.toView(pc, readD());
				pc.setBoard(0);
			} else if(b != null && pc.getBoard() == 2){
				pc.toSender(S_BoardView.clone(BasePacketPooling.getPool(S_BoardView.class), b));
				pc.setBoard(0);
			}
		
		}
		
		return this;
	}

}
