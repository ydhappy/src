package lineage.persnal_shop;

import java.sql.Connection;
import java.util.List;

import lineage.bean.database.Exp;
import lineage.bean.database.Item;
import lineage.database.DatabaseConnection;
import lineage.database.ExpDatabase;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.sp.CharacterSaveMarbles;

public class S_PersnalShopPriceSetting extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, PcInstance pc, List<PersnalShopItem> list) {
		if (bp == null)
			bp = new S_PersnalShopPriceSetting(pc, list);
		else
			((S_PersnalShopPriceSetting) bp).toClone(pc, list);
		return bp;
	}

	public S_PersnalShopPriceSetting(PcInstance pc, List<PersnalShopItem> list) {
		toClone(pc, list);
	}

	public void toClone(PcInstance pc, List<PersnalShopItem> list) {
		try {
			clear();

			writeC(Opcodes.S_OPCODE_WAREHOUSE);
			writeD(pc.getObjectId());
			writeH(list.size());
			writeC(3);
			readDB(pc, list); // 창고 목록
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readDB(PcInstance pc, List<PersnalShopItem> list) {
		try {
			for (PersnalShopItem wh : list) {
				
				Item item = ItemDatabase.find_ItemId(wh.getItem_id());
				ItemInstance iteminstance = ItemDatabase.newInstance(item);
				writeD(wh.getItemobjid()); // 번호
				writeC(wh.getType()); // 타입
				writeH(wh.getInvGfx()); // GFX 아이디
				writeC(wh.getBless()); // 1: 보통 0: 축 2: 저주
				writeD(2000000000); // 최대 측정할수있는 가격을 20억으로 설정
				writeC(1); // 1: 확인 0: 미확인
				if (iteminstance instanceof CharacterSaveMarbles) {
					iteminstance.setObjectId(wh.getItemobjid());

					Connection con = null;
					try {
						con = DatabaseConnection.getLineage();
						iteminstance.toWorldJoin(con, null);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						DatabaseConnection.close(con);
					}

					writeS(getName(iteminstance));
				} else {
					writeS(wh.getName());
				}

//				writeS(wh.getName()); // 이름
				if (Lineage.server_version >= 380)
					writeC(0x00);
			}
			writeD(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected String getName(ItemInstance item) {
		StringBuffer sb = new StringBuffer();

		if (!(item instanceof CharacterSaveMarbles)) {
			if (!item.getItem().getNameId().startsWith("$"))
				sb.append(item.getName());
			else
				sb.append(item.isDefinite() && item.getItem().getNameIdCheck().length() > 0
						? item.getItem().getNameIdCheck() : item.getItem().getNameId());
		}

		if (item instanceof CharacterSaveMarbles) {
			CharacterSaveMarbles csm = (CharacterSaveMarbles) item;
			if (csm.getAccountUid() != 0) {

				sb.append("\r\n");
				String class_type = "";
				switch (csm.getCharacterClassType()) {
				case 0:
					class_type = "군주";
					break;
				case 1:
					class_type = "기사";
					break;
				case 2:
					class_type = "요정";
					break;
				case 3:
					class_type = "법사";
					break;
				case 4:
					class_type = "다크엘프";
					break;
				}
				sb.append(class_type + " (" + csm.getCharacterName() + ")");
				sb.append("\r\n");
				sb.append("[Lv." + csm.getCharacterLevel() + " ");
				double exp = 0.0;
				Exp exp_bean = ExpDatabase.find(csm.getCharacterLevel());
				if (exp_bean != null) {
					double e = csm.getCharacterExp() - (exp_bean.getBonus() - exp_bean.getExp());
					exp = (e / exp_bean.getExp()) * 100;
				}
				sb.append(String.format("%.2f%%", exp) + "]");
			} else {
				if (!item.getItem().getNameId().startsWith("$")) {
					sb.append(item.getName());
				} else {
					sb.append(item.isDefinite() && item.getItem().getNameIdCheck().length() > 0
							? item.getItem().getNameIdCheck() : item.getItem().getNameId());
				}
			}
		}

		return sb.toString().trim();
	}

}
