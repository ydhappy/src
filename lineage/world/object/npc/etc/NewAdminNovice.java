package lineage.world.object.npc.etc;

import lineage.bean.database.Exp;
import lineage.bean.database.Npc;
import lineage.database.ExpDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.TeleportInstance;

public class NewAdminNovice extends TeleportInstance {

	public NewAdminNovice(Npc npc){
		super(npc);
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		if(pc.getLevel() == 1) {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "newadmin3"));
			return;
		}
		if(pc.getLevel() == 2) {
			// 레벨 3로
			Exp e = ExpDatabase.find(pc.getLevel());
			pc.setExp( e.getBonus() );
		}
		if(pc.getLevel() == 5) {
			// 레벨 6로
			Exp e = ExpDatabase.find(pc.getLevel());
			pc.setExp( e.getBonus() );
		}
		if(pc.getLevel() < 5)
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "newadmin1"));
		else
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "newadmin2"));
	}

}
