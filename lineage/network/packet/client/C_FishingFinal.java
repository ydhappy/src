package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.ClientBasePacket;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.FishingPole;

public class C_FishingFinal extends ClientBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_FishingFinal(data, length);
		else
			((C_FishingFinal)bp).clone(data, length);
		return bp;
	}
	
	public C_FishingFinal(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		// 낚싯대 찾기.
		ItemInstance item = pc.getInventory().find(FishingPole.class);
		if(item != null)
			item.toClickFinal(pc);
		return this;
	}

}
