package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CraftTable;
import lineage.world.controller.CraftTableController;
import lineage.world.object.instance.PcInstance;

public class C_CraftTable extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_CraftTable(data, length);
		else
			((C_CraftTable)bp).clone(data, length);
		return bp;
	}
	
	public C_CraftTable(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		// 버그 방지.
		if(pc==null || pc.isWorldDelete())
			return this;

		int type = readH();
		int length = readH();
		switch(type) {
			case 0x36:
				CraftTableController.toCraftTableData(pc, this, type);
				break;
			case 0x38:
				CraftTableController.toCraftTableList(pc, this, type);
				break;
			case 0x5C:
				CraftTableController.toCraftTableSubmit(pc, this, type);
				break;
			case 0x7A:	// 마법인형 합성창 열기 요청.
				pc.toSender(S_CraftTable.clone(BasePacketPooling.getPool(S_CraftTable.class), type+1));
				break;
			case 0x7C:	// 마법인형 합성 요청.
				readH();	// 08 01
				int[] doll_list = {0, 0, 0, 0};
				while(isRead(8)) {
					readC();						// 12
					int bit_count = readC() - 6;	// 07
					readC();						// 08
					int idx = readC();				// 
					readD();						// 10 f1 13 18
					doll_list[idx-1] = read4(bit_count);
				}
				
				pc.toSender(S_CraftTable.clone(BasePacketPooling.getPool(S_CraftTable.class), type+1));
				break;
		}
		
		return this;
	}

}
