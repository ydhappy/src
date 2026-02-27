package sp.network.packet.server;

import java.util.List;

import lineage.network.packet.BasePacket;
import lineage.network.packet.ServerBasePacket;
import lineage.network.packet.Opcodes;
import lineage.world.object.instance.ItemInstance;
import sp.bean.shop;
import sp.object.PcShopInstance;

public class S_ShopSell extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, PcShopInstance shop, List<ItemInstance> list) {
		if (bp == null)
			bp = new S_ShopSell(shop, list);
		else
			((S_ShopSell) bp).toClone(shop, list);
		return bp;
	}

	public S_ShopSell(PcShopInstance shop, List<ItemInstance> list) {
		toClone(shop, list);
	}

	public void toClone(PcShopInstance shop, List<ItemInstance> list) {
		clear();

		writeC(Opcodes.S_OPCODE_SHOPSELL);
		writeD(shop.getObjectId());
		writeH(list.size()); // 인벤토리에서 팔수잇는 아템 갯수
		for (ItemInstance item : list) {
			shop s = shop.findSell(item);
			writeD(item.getObjectId());
			writeD(s==null ? 0 : s.getPrice());
		}
	}

}
