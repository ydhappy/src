package lineage.world.object.instance;

import java.util.List;

import lineage.bean.lineage.Board;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_BoardList;
import lineage.world.controller.BoardController;
import lineage.world.controller.QuestTodayController;
import lineage.world.object.Character;

public class QuestTodayBoardInstance extends BoardInstance {
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if(cha instanceof PcInstance) {
			//
			List<Board> list = QuestTodayController.getList((PcInstance)cha );
			// 패킷 처리
			cha.toSender(S_BoardList.clone(BasePacketPooling.getPool(S_BoardList.class), this, list));
			//
			for(Board b : list)
				BoardController.setPool(b);
			list.clear();
		}
	}

}