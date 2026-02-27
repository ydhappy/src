package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;

public class S_WareHouseHistory extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp){
		if(bp == null)
			bp = new S_WareHouseHistory();
		else
			((S_WareHouseHistory)bp).toClone();
		return bp;
	}
	
	public S_WareHouseHistory(){
		toClone();
	}
	
	public void toClone(){
		clear();
		
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(117);
		
		writeD(2);	// 전체 갯수.
		
		// 반복구간.
		//	: 최근시간순으로 출력
		//	: 3일기간 리밋
		writeS("psjump");	// 사용자명
		writeC(0);		// 0:맡김 1:찾음
		writeS("아데나");	// 아이템이름
		writeD(1000);	// 수량
		writeD(1);		// 경과시간 분단위
		
		writeS("psjump");	// 사용자명
		writeC(1);		// 0:맡김 1:찾음
		writeS("아데나");	// 아이템이름
		writeD(1000);	// 수량
		writeD(10);		// 경과시간 분단위
	}

}
