package lineage.world.object.npc.shop;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_SoundEffect;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.ShopInstance;

public class SellShop extends ShopInstance {
	
	public SellShop(Npc npc){
		super(npc);
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 27816)); 
		super.toTalk(pc, "sell", null, null);
	}

}
