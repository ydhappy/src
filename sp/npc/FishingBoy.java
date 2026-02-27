package sp.npc;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.instance.PcInstance;

public class FishingBoy extends lineage.world.object.npc.event.FishingBoy {

	public FishingBoy(Npc npc) {
		super(npc);
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		if(getMap()==4 || getMap()==610)
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "fk_in_1"));
		else
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "fk_out_1"));
	}
}
