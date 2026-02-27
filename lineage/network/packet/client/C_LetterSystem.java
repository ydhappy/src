package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_LetterSystem;
import lineage.world.controller.LetterController;
import lineage.world.object.instance.PcInstance;

public class C_LetterSystem extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_LetterSystem(data, length);
		else
			((C_LetterSystem)bp).clone(data, length);
		return bp;
	}
	
	public C_LetterSystem(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		// 버그방지
		if(pc==null || pc.isWorldDelete())
			return this;
		
		int type = readC();
		switch(type) {
			case 0x00:	// 편지 목록
			case 0x01:	// 혈맹 편지 목록
			case 0x02:	// 보관함 목록
				pc.toSender( S_LetterSystem.clone(BasePacketPooling.getPool(S_LetterSystem.class), type, pc) );
				break;
			case 0x10:	// 편지 읽기
			case 0x11:	// 혈맹편지 읽기.
			case 0x12:	// 보관된 편지 읽기
				LetterController.toRead(pc, type, readD());
				break;
			case 0x20:	// 편지 보내기
			case 0x21:	// 혈맹 편지 보내기
				readH();	// price??
				LetterController.toLetter(pc, type, readS(), readSS(), readSS());
				break;
			case 0x30:	// 편지 삭제
			case 0x31:	// 혈맹 편지 삭제
			case 0x32:	// 보관된 편지 삭제
				LetterController.toRemove(pc, type, readD());
				break;
			case 0x40:	// 편지 저장
				LetterController.toSave(pc, type, readD());
				break;
		}
		return this;
	}
}
