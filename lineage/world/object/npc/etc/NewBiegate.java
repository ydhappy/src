package lineage.world.object.npc.etc;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.TeleportInstance;

public class NewBiegate extends TeleportInstance {

	public NewBiegate(Npc npc){
		super(npc);
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		if(pc.getLevel() > 51)
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "newbiegate2"));
		else
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "newbiegate1"));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		if(action.equalsIgnoreCase("b") && pc.getLevel()<10) {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "newbiegate3"));
		} else if((action.equals("c") || action.equals("d")) && (pc.getLevel()<10 || pc.getLevel()>29)) {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "newbiegate3"));
		} else if((action.equals("A") || action.equals("B") || action.equals("C") || action.equals("D")) && (pc.getLevel()<10 || pc.getLevel()>44)) {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "newbiegate3"));
		} else if((action.equals("E") || action.equals("F") || action.equals("G") || action.equals("H")) && (pc.getLevel()<45 || pc.getLevel()>51)) {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "newbiegate3"));
		} else {
			super.toTalk(pc, action, type, cbp, opt);
		}
	}

}
