package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CharacterDungeonTime;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.world.object.instance.PcInstance;

public class C_Rank extends ClientBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_Rank(data, length);
		else
			((C_Rank)bp).clone(data, length);
		return bp;
	}
	
	public C_Rank(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc) {
        int type = readC();
        int rank = readC();
        switch (type) {
        	case 0: // 혈맹에 속한 인원수의 변동이 있었을 경우
        		break;
        	case 1:	// 계급
        		break;
        	case 2:// 목록
        		break;
        	case 3:// 가입
        		break;
        	case 4:// 탈퇴
        		break;
        	case 5: // 생존의 외침
        		if(pc.getFood() == Lineage.MAX_FOOD) {
        			if(pc.getInventory().getSlot(8) != null) {
        				// 1분에 1%가 충전됨.
        				// 100% 충전 다되야 사용 가능.
        				// hp 만땅 채워줌.
        				// 독상태든 lock상태든 상관없이 처리됨.
        				// 착용중인 무기인첸트에 따라 다르게 처리.
        			} else {
        				// 무기장착 필요.
        				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1973));
        			}
        		}
        		break;
        	case 6: // 무기 허세 떨기 Alt + 0(숫자)
        		break;
        	case 8:// (/입장시간)
        		break;
        	case 9:// (잔여시간)
        		pc.toSender(S_CharacterDungeonTime.clone(BasePacketPooling.getPool(S_CharacterDungeonTime.class), pc));
        		break;
        }
		return this;
	}

}
