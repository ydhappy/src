package lineage.persnal_shop;

import java.sql.Connection;

import lineage.bean.database.Exp;
import lineage.bean.database.Item;
import lineage.database.DatabaseConnection;
import lineage.database.ExpDatabase;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.server.S_Inventory;
import lineage.share.Lineage;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.item.sp.CharacterSaveMarbles;

public class S_PersnalShopBuyList extends S_Inventory {

	static synchronized public BasePacket clone(BasePacket bp, PersnalShopInstance psi) {
		if (bp == null)
			bp = new S_PersnalShopBuyList(psi);
		else
			((S_PersnalShopBuyList) bp).toClone(psi);
		return bp;
	}

	public S_PersnalShopBuyList(PersnalShopInstance psi) {
		toClone(psi);
	}

	public void toClone(PersnalShopInstance psi) {
		try {
			clear();

			writeC(Opcodes.S_OPCODE_SHOPBUY);
			writeD(psi.getObjectId());

			// 일반상점 구성구간.
			writeH(psi.getItemList().size());

			for (PersnalShopItem s : psi.getItemList()) {
				writeD(s.getItemobjid());
				writeH(s.getInvGfx());
				writeD(s.getPrice());

				Item item = ItemDatabase.find_ItemId(s.getItem_id());
				if (item == null) {
					System.out.println("없는 아이템을 패킷에 표현하려합니다. [개인상점 : " + s.getItem_id() + "]");
					continue;
				}
				ItemInstance iteminstance = ItemDatabase.newInstance(item);
				iteminstance.setCount(s.getCount());
				iteminstance.setBress(s.getBless());
				iteminstance.setEnEarth(s.getEnEarth());
				iteminstance.setEnFire(s.getEnFire());
				iteminstance.setEnWater(s.getEnWater());
				iteminstance.setEnWind(s.getEnWind());
				iteminstance.setEnLevel(s.getEnLevel());
				iteminstance.setPetObjectId(s.getPetObjId());
				iteminstance.setDefinite(s.isDefinite());

				if (iteminstance instanceof CharacterSaveMarbles) {
					iteminstance.setObjectId(s.getItemobjid());

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
					writeS(s.getName());
				}

				/*
				 * if (Lineage.server_version > 144) { if (iteminstance instanceof
				 * ItemWeaponInstance) toWeapon(iteminstance.getItem(), null, 0, 0, 0, 0, 0, 0,
				 * 0, 0, 0, 0, 0 ,0); else if (iteminstance instanceof ItemArmorInstance)
				 * toArmor(iteminstance.getItem(), null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ,0);
				 * else if (iteminstance instanceof ItemInstance) toDoll(iteminstance.getItem(),
				 * null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ,0); else if (iteminstance instanceof
				 * ItemInstance) toCook(iteminstance.getItem(), null, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 * 0, 0 ,0); else toEtc(iteminstance); }
				 * 
				 * iteminstance = null;
				 */
				if (Lineage.server_version > 144) {
					if (item.getType1().equalsIgnoreCase("armor") || item.getType1().equalsIgnoreCase("petArmor")) {
						toArmor(item, 0, s.getEnLevel(), (int) item.getWeight(), 0, s.getBless(), 0, 0, 0);
					} else if (item.getType1().equalsIgnoreCase("weapon")
							|| item.getType1().equalsIgnoreCase("petWeapon")) {
						toWeapon(item, 0, s.getEnLevel(), (int) item.getWeight(), s.getBless(), 0, 0, 0, 0);
					}
//					else if (item.getType1().equalsIgnoreCase("doll")
//							) {
//						toDoll(item, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
//					} else if (item.getType2().equalsIgnoreCase("cook")
//							) {
//						toCook(item, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
//					}
					else {
						toEtc(item, (int) item.getWeight());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		writeH(7);
	}

	protected String getName(ItemInstance item) {
		StringBuffer sb = new StringBuffer();

		if (!(item instanceof CharacterSaveMarbles)) {
			if (!item.getItem().getNameId().startsWith("$"))
				sb.append(item.getName());
			else
				sb.append(item.isDefinite() && item.getItem().getNameIdCheck().length() > 0
						? item.getItem().getNameIdCheck()
						: item.getItem().getNameId());
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
							? item.getItem().getNameIdCheck()
							: item.getItem().getNameId());
				}
			}
		}

		return sb.toString().trim();
	}

}
