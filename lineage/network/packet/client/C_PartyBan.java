package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.ClientBasePacket;
import lineage.world.controller.PartyController;
import lineage.world.object.instance.PcInstance;

public class C_PartyBan extends ClientBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_PartyBan(data, length);
		else
			((C_PartyBan)bp).clone(data, length);
		return bp;
	}
	
	public C_PartyBan(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc) {
//		PartyController.toBan(pc, readS());
		return this;
	}

}
