package lineage.world.object.npc.teleporter;

import java.util.List;
import java.util.Map;
import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.TeleportInstance;

public class MarketTeleporter extends TeleportInstance
{
	public MarketTeleporter(Npc npc)
	{
		super(npc);
	}
	
	public void toTalk(PcInstance pc, ClientBasePacket cbp)
	{
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "telemmm"));
	}
	
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object[] opt)
	{
		if (action.equalsIgnoreCase("teleportURL"))
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "teleisland2", null, (List)this.list.get(Integer.valueOf(0))));
		else
			super.toTalk(pc, action, type, cbp, new Object[0]);
	}
}
	


