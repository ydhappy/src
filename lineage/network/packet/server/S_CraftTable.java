package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.world.object.npc.craft.CraftTable;

public class S_CraftTable extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, int type, Object...opt) {
		if(bp == null)
			bp = new S_CraftTable(type, opt);
		else
			((S_CraftTable)bp).toClone(type, opt);
		return bp;
	}
	
	public S_CraftTable(int type, Object...opt) {
		toClone(type, opt);
	}
	
	public void toClone(int type, Object...opt) {
		clear();
		
		writeC(Opcodes.S_OPCODE_CRAFTTABLE);
		writeH(type);
		switch(type) {
			case 0x37:	// 제작아이템 전체목록 전송.
				int action = (int)opt[0];
//				if(action == 0x0a) {
//					readCraftTable();
//				} else {
					writeC(0x08);
					writeC(0x03);
//				}
				break;
			case 0x39:	// 제작 리스트
			{
				// 08 02 01 00
				//	: 제작npc와 거리가 너무 멀다는 메세지가 출력됨. 시스템메세지로.
				writeC(0x08);
				boolean is = (boolean)opt[0];
				if(is) {
					writeC(0x00);
					CraftTable ct = (CraftTable)opt[1];
					for(Integer number : ct.getNpc().getCraftList()) {
						writeC(0x12);
						if (number > 127)
							writeC(0x07);
						else
							writeC(0x06);
						writeC(0x08);
						write4(number);
						writeH(0x10);
						writeH(0x18);
					}
				} else {
					// 제작 npc와 거리가 너무 멀다는 메세지 출력.
					writeC(0x02);
					writeC(0x01);
					writeC(0x00);
				}
				break;
			}
			case 0x3b:	// 제작 완료.
			{
				writeH(0x08);
				break;
			}
			case 0x5d:
				writeC(0x08);
				writeC(0x02);
				writeC(0x01);
				writeC(0x00);
				break;
			case 0x7B:	// 마법인형 합성 창 열기 답변.
				writeC(0x08);
				writeC(0x03);
				break;
			case 0x7D:	// 마법인형 조합 결과 부분.
				writeC(0x08);			// 
				writeC(0x00);			// 0:성공, 1:실패
				writeC(0x12);			// 
				writeC(0x0a);			// objId와 gfx값에 따른 길이가 달라질 수 있으며, 그에따른 길이값인거같은데 적절하게 10으로 설정.
				writeC(0x08);			// 
				write4(139251500);		// objId
				writeC(0x10);
				write4(6812);			// inv_gfx
				break;
		}
		writeH(0x00);
	}
}
