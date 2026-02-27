package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.ClientBasePacket;
import lineage.world.controller.BaseStatController;
import lineage.world.object.instance.PcInstance;

public class C_BaseStat extends ClientBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_BaseStat(data, length);
		else
			((C_BaseStat)bp).clone(data, length);
		return bp;
	}
	
	public C_BaseStat(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc) {
		switch(readC()) {
			case 1:	// 스탯분배완료 요청.
				BaseStatController.toStat(pc, readC(), readC(), readC(), readC(), readC(), readC());
				break;
			case 2:	// 레벨업 요청.
				BaseStatController.toLevelUp(pc, readC());
				break;
		}
		return this;
	}

}
