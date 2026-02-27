package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_AdenShop;
import lineage.world.object.instance.PcInstance;

public class C_AdenShop extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_AdenShop(data, length);
		else
			((C_AdenShop)bp).clone(data, length);
		return bp;
	}
	
	public C_AdenShop(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		// 버그 방지.
		if(!isRead(2) || pc==null || pc.isWorldDelete())
			return this;
		//
		int type = readH();
		switch(type) {
			case 0x01:
				// 일팩은 그냥 상점을 출력하게 해놓앗네
				
				// 요게 아덴샵 띄우는 창 (아이템 목록은 변경할 수 없음.)
				pc.toSender(S_AdenShop.clone(BasePacketPooling.getPool(S_AdenShop.class), 0x02));
				// n코인 설정.
				pc.toSender(S_AdenShop.clone(BasePacketPooling.getPool(S_AdenShop.class), 0x03));
				// 이메일 설정.
				pc.toSender(S_AdenShop.clone(BasePacketPooling.getPool(S_AdenShop.class), 0x0c));
				break;
			case 0x04:	// 패스워드입력 하여 실제 구매요청
				// 구매성공 및 실패 처리
				pc.toSender(S_AdenShop.clone(BasePacketPooling.getPool(S_AdenShop.class), 0x05));
				// 이메일 설정
				pc.toSender(S_AdenShop.clone(BasePacketPooling.getPool(S_AdenShop.class), 0x0c));
				break;
			case 0x32:	// 구매동의
				// 패스워드창 띄우기
				pc.toSender(S_AdenShop.clone(BasePacketPooling.getPool(S_AdenShop.class), 0x33));
				break;
		}
		
		return this;
	}

}
