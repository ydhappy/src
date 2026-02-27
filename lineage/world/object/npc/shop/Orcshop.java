package lineage.world.object.npc.shop;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectHeading;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.ShopInstance;

	public class Orcshop extends ShopInstance {
	
	public Orcshop(Npc npc){
		super(npc);
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		// ai_talk true로 변경해야 도망가지 않고 해당 stay_count를 0까지 완료함.
		ai_talk = true;
		ai_walk_stay_count = Lineage.npc_talk_stay_time;
		// pc 쪽으로 방향 전환.
		setHeading( Util.calcheading(this, pc.getX(), pc.getY()) );
		toSender(S_ObjectHeading.clone(BasePacketPooling.getPool(S_ObjectHeading.class), this), false);
		
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "orcshop"));
	}
	
}