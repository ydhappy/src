package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.ClientBasePacket;
import lineage.world.controller.ClanController;
import lineage.world.object.instance.PcInstance;

public class C_ClanStatus extends ClientBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length) {
		if (bp == null)
			bp = new C_ClanStatus(data, length);
		else
			((C_ClanStatus) bp).clone(data, length);
		return bp;
	}

	public C_ClanStatus(byte[] data, int length) {
		clone(data, length);
	}

	@Override
	public BasePacket init(PcInstance pc) {
		//
		int temp = readC();
		int status = readC();
		String name = readS();
		//
//		ClanController.toStatus(pc, status, name);
		//
		return this;
	}

}
