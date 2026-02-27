package lineage.network.packet.server;

import lineage.bean.lineage.Board;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.util.Util;

public class S_BoardView extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, Board b){
		if(bp == null)
			bp = new S_BoardView(b);
		else
			((S_BoardView)bp).toClone(b);
		return bp;
	}
	
	public S_BoardView(Board b){
		toClone(b);
	}
	
	public void toClone(Board b){
		clear();
		writeC(Opcodes.S_OPCODE_BOARDREAD);
		writeD(b.getUid()); // 순번
		if ("giran".equals(b.getType())) {
			writeS(b.getName());		// 작성자
		} else {
			writeS(b.getName());		// 작성자
		}
		writeS(b.getSubject()); // 제목
		writeS(b.toStringDays()); // 날짜
		
		if ("giran".equals(b.getType())) {
			StringBuilder sb = new StringBuilder();
			
			switch (b.getStep()) {
			case 0:
				sb.append("거래상황: 판매중\r\n");
				sb.append("\r\n");
				sb.append("판매자:" +b.getName()+"\r\n");
				break;
			case 1:
				sb.append("거래상황: 입금대기중\r\n");
				sb.append("\r\n");
				sb.append("판매자:" +b.getName()+"\r\n");
				sb.append("구매자:" +b.getBuyName()+"\r\n");
				break;
			case 2:
				sb.append("거래상황: 입급확인중\r\n");
				sb.append("\r\n");
				sb.append("판매자:" +b.getName()+"\r\n");
				sb.append("구매자:" +b.getBuyName()+"\r\n");
				break;
			case 3:
				sb.append("거래상황: 거래완료\r\n");
				sb.append("\r\n");
				sb.append("판매자:" +b.getName()+"\r\n");
				sb.append("구매자:" +b.getBuyName()+"\r\n");
				break;
			}
			
			sb.append("거래번호: " + b.getUid() + "\r\n");
			sb.append("\r\n");
			sb.append("아데나 : " + Util.numberFormat(b.getAdena()) + "\r\n");
			sb.append("금액(현금): " + Util.numberFormat(b.getMoney()) + "\r\n");
			sb.append("\r\n");
			//sb.append("\r\n");	
			sb.append("구매를 원하시면\r\n");
			sb.append("  [.구매 거래번호]\r\n");
			sb.append("를 입력하시면 됩니다.\r\n");
			sb.append("\r\n");
			sb.append("* 안전한 거래를 위하여\r\n");
			sb.append("  전화통화를 하시기바랍니다.\r\n");
			sb.append("  입금을 하신 후에 \r\n");
			sb.append("  [.입금완료 거래번호] 입력\r\n");
			sb.append("\r\n");
			sb.append(".\r\n");
			
			writeS(sb.toString());		// 내용
		} else {
			writeS(b.getMemo()); // 내용
		}
	}

}
