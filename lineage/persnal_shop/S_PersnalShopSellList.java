package lineage.persnal_shop;

import java.util.List;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.world.object.instance.ItemInstance;

public class S_PersnalShopSellList extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, PersnalShopInstance shop, List<ItemInstance> list) {
		if (bp == null)
			bp = new S_PersnalShopSellList(shop, list);
		else
			((S_PersnalShopSellList) bp).toClone(shop, list);
		return bp;
	}

	public S_PersnalShopSellList(PersnalShopInstance shop, List<ItemInstance> list) {
		toClone(shop, list);
	}

	public void toClone(PersnalShopInstance shop, List<ItemInstance> list) {
		clear();

		writeC(Opcodes.S_OPCODE_SHOPSELL);
		writeD(shop.getObjectId());
		writeH(list.size()); // 인벤토리에서 팔수잇는 아템 갯수

		for (ItemInstance item : list) {
			writeD(item.getObjectId());
			writeD(shop.getSellItemPrice(item));
		}
		writeH(7);
	}
}
