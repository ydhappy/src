package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CharacterPassworld;
import lineage.network.packet.server.S_MessageGreen;
import lineage.network.packet.server.S_Test;
import lineage.network.packet.server.S_TradeZone;
import lineage.share.Lineage;
import lineage.world.controller.QuestController;
import lineage.world.object.instance.PcInstance;

public class C_Ui6System extends ClientBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_Ui6System(data, length);
		else
			((C_Ui6System)bp).clone(data, length);
		return bp;
	}
	
	public C_Ui6System(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc) {
		int type = readC();
		switch (type) {
			case 0x03:	//
				break;
			case 0x06:	// 드래곤메뉴
				break;
			case 0x08:
				break;
			case 0x0b:	// 맵전송
				break;
			case 0x0d:
				break;
			case 0x13:	// 앱센터
				if(Lineage.server_version <= 360)
					pc.toSender(new S_MessageGreen(556, "[경고]: \\f3[컨트롤(Ctrl) + F 자신의 캐릭터 클릭] \\f2해주세요."));
				else
					pc.toSender(S_Test.clone(BasePacketPooling.getPool(S_Test.class), 19));
				break;
			case 0x22:	// 기억리스트
				break;
			case 0x27:	// 기억창 save 눌렀을때
				break;
			case 0x28:	// 기억목록명이 변경된게 있을때 호출됨.
				break;
			case 0x2b:	// 케릭생성시 패스워드처리를 위해 호출됨.
				pc.toSender(S_CharacterPassworld.clone(BasePacketPooling.getPool(S_CharacterPassworld.class), 0x3f));
				break;
			case 0x2c:	// 컨트롤+Q 에 reset 누르면 호출됨.
				break;
			case 0x37:	// 페어리의 축하 선물
				QuestController.toRequest(pc, readH());
				break;
			case 0x39:	// 시장에서 상점 메뉴 클릭시 호출됨.
				pc.toSender(S_TradeZone.clone(BasePacketPooling.getPool(S_TradeZone.class), 0));
				break;
			case 0x45:	// test
				break;
		}
		return this;
	}

}
