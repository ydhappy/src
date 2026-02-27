package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.world.controller.ClanController;
import lineage.world.object.instance.PcInstance;

public class C_ClanCreate extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_ClanCreate(data, length);
		else
			((C_ClanCreate)bp).clone(data, length);
		return bp;
	}
	
	public C_ClanCreate(byte[] data, int length){
		clone(data, length);
	}

	@Override
	public BasePacket init(PcInstance pc) {
		// 버그방지
		if (pc == null || pc.isWorldDelete())
			return this;

		String clan_name = readS().replaceAll(" ", "").trim();
		//
		if(PluginController.init(C_ClanCreate.class, "init", this, pc, clan_name) != null)
			return this;
		//
		ClanController.toCreate(pc, clan_name);

		return this;
	}

}