package lineage.persnal_shop;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.world.object.object;

public class S_PersnalShopPurchasePriceOut extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, object o, long k){
		if(bp == null)
			bp = new S_PersnalShopPurchasePriceOut(o, k);
		else
			((S_PersnalShopPurchasePriceOut)bp).toClone(o, k);
		return bp;
	}
	
	public S_PersnalShopPurchasePriceOut(object o, long k){
		toClone(o, k);
	}
	
	public void toClone(object o, long k){
		clear();

		writeC(Opcodes.S_OPCODE_CASTLETAXOUT);
		writeD(o.getObjectId());
		writeD((int) k);
	}

}
