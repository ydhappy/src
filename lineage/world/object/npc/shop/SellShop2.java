package lineage.world.object.npc.shop;

import lineage.bean.database.Npc;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_SoundEffect;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.ShopInstance;

public class SellShop2 extends ShopInstance {
	
	public SellShop2(Npc npc){
		super(npc);
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		super.toTalk(pc, "sell", null, null);
	}

}
