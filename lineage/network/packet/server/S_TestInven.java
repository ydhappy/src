package lineage.network.packet.server;

import java.util.List;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.MagicDoll;

public class S_TestInven extends S_Inventory {

	static synchronized public BasePacket clone(BasePacket bp, PcInstance pc, List<ItemInstance> list) {
		if (bp == null)
			bp = new S_TestInven(pc, list);
		else
			((S_TestInven) bp).toClone(pc, list);
		return bp;
	}

	public S_TestInven(PcInstance pc, List<ItemInstance> list) {
		toClone(pc, list);
	}

	public void toClone(PcInstance pc, List<ItemInstance> list) {
		clear();

		writeC(Opcodes.S_OPCODE_WAREHOUSE);
		writeD(pc.getObjectId()); // NPC 오브젝트
		writeH(list.size());
		writeC(0); // 타입부분
		
		for (ItemInstance i : list) {
			Item item = ItemDatabase.find(i.getItem().getName());
			StringBuffer item_name = new StringBuffer();

			if (item == null) {
				item_name.append("none");
			} else {
				
					if (i.isDefinite()) {
						if (i.getEnLevel() < 0)
							item_name.append("-");
						else
							item_name.append("+");
						item_name.append(i.getEnLevel());
					}
					item_name.append(" ").append(item.getNameId());
					if (i.getCount() > 1) {
						item_name.append(" (");
						item_name.append(i.getCount());
						item_name.append(")");
					}
			}

			writeD(i.getObjectId()); // 번호
			writeC(0); // 타입
			writeH(i.getItem().getInvGfx()); // GFX 아이디
			writeC(i.getBress()); // 1: 보통 0: 축 2: 저주
			//0누구 1판매아이템추가 2판매가격추가
			if(pc.getShop_inven_num() == 1 || pc.getShop_inven_num() == 5 || pc.getShop_inven_num() == 7) {
				writeD(i.getCount()); // 현재아템 총수량
			}else {
				writeD(200000000); // 현재아템 총수량
			}
			writeC(i.isDefinite() ? 1 : 0); // 1: 확인 0: 미확인
			writeS(item_name.toString()); // 이름
		}
	}
}
