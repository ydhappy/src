package lineage.network.packet.server;

import java.util.List;

import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.world.controller.LetterController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.Letter;

public class S_LetterSystem extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, int type, PcInstance pc){
		if(bp == null)
			bp = new S_LetterSystem(type, pc);
		else
			((S_LetterSystem)bp).toClone(type, pc);
		return bp;
	}

	static synchronized public BasePacket clone(BasePacket bp, int type, Letter l){
		if(bp == null)
			bp = new S_LetterSystem(type, l);
		else
			((S_LetterSystem)bp).toClone(type, l);
		return bp;
	}

	static synchronized public BasePacket clone(BasePacket bp, int type, long uid, boolean isSave){
		if(bp == null)
			bp = new S_LetterSystem(type, uid, isSave);
		else
			((S_LetterSystem)bp).toClone(type, uid, isSave);
		return bp;
	}
	
	public S_LetterSystem(int type, PcInstance pc){
		toClone(type, pc);
	}
	
	public S_LetterSystem(int type, Letter l){
		toClone(type, l);
	}
	
	public S_LetterSystem(int type, long uid, boolean isSave){
		toClone(type, uid, isSave);
	}
	
	public void toClone(int type, PcInstance pc) {
		clear();
		//
		writeC(Opcodes.S_OPCODE_LETTERSYSTEM);
		writeC(type);
		//
		List<Letter> list = LetterController.getList(pc, type);
		writeH(list.size());
		for(Letter l : list) {
			writeD(l.getLetterUid());											// 아이디
			writeC(l.isOpen() ? 1 : 0);											// 읽은상태 표현 (0:미확인 1:확인)
			writeC(Integer.valueOf(l.getDateString().substring(2, 4)));			// 년
			writeC(Integer.valueOf(l.getDateString().substring(5, 7)));			// 월
			writeC(Integer.valueOf(l.getDateString().substring(8, 10)));		// 일
			writeS(l.getFrom());												// 보낸이
			writeSS(l.getSubject());											// 제목
			ItemDatabase.setPool(l);
		}
		writeC(0x01);	// 성공 여부.
		list.clear();
	}
	
	public void toClone(int type, Letter l) {
		clear();
		//
		writeC(Opcodes.S_OPCODE_LETTERSYSTEM);
		writeC(type);
		writeD(l.getLetterUid());
		writeSS(l.getMemo());
		writeH(0x01);	// 성공 여부.
	}
	
	public void toClone(int type, long uid, boolean isSave) {
		clear();
		//
		writeC(Opcodes.S_OPCODE_LETTERSYSTEM);
		writeC(type);
		writeD(uid);
		writeC(isSave ? 1 : 0);	// 성공 여부.
	}
}
