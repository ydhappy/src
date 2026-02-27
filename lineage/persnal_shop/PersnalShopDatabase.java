package lineage.persnal_shop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.share.TimeLine;
import lineage.world.World;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;

public class PersnalShopDatabase {
	private static PersnalShopDatabase _instance;
	public static List<PersnalShopInstance> list = null;

	public static PersnalShopDatabase getInstnace() {
		if (_instance == null) {
			_instance = new PersnalShopDatabase();
		}
		return _instance;
	}

	private PersnalShopDatabase() {
		init();
	}

	private void init() {
		TimeLine.start("PersnalShopDatabase..");

		if (list == null)
			list = new ArrayList<PersnalShopInstance>();

		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			list.clear();

			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters_shop");
			rs = st.executeQuery();

			while (rs.next()) {
				int ownerObjId = rs.getInt("char_obj_id");
				String ownerName = rs.getString("char_name");
				int ownerAccountUid = rs.getInt("char_account_uid");
				int bless = rs.getInt("bless");
				int count = rs.getInt("count");
				boolean definite = rs.getInt("definite") == 1 ? true : false;
				int enEarth = rs.getInt("enchant_earth");
				int enFire = rs.getInt("enchant_fire");
				int enWater = rs.getInt("enchant_water");
				int enWind = rs.getInt("enchant_wind");
				int enLevel = rs.getInt("enchant_level");
				int item_id = rs.getInt("item_id");
				long petObjId = rs.getInt("pet_obj_id");
				int itemobjid = rs.getInt("item_obj_id");
				int price = rs.getInt("price");
				String shopType = rs.getString("shop_type");
				String charName = rs.getString("char_name");

				Item item = ItemDatabase.find_ItemId(item_id);
				
				if (item == null) {
					System.out.println("없는 아이템이 로드 됩니다. [개인상점 : " + item_id + "]");
					continue;
				}
				
				ItemInstance iteminstance = ItemDatabase.newInstance(item);
				iteminstance.setCount(count);
				iteminstance.setBress(bless);
				iteminstance.setEnEarth(enEarth);
				iteminstance.setEnFire(enFire);
				iteminstance.setEnWater(enWater);
				iteminstance.setEnWind(enWind);
				iteminstance.setEnLevel(enLevel);
				iteminstance.setPetObjectId(petObjId);
				iteminstance.setDefinite(definite);

				int type = 0;
				if (iteminstance instanceof ItemWeaponInstance)
					type = 1;
				else if (iteminstance instanceof ItemArmorInstance)
					type = 2;
				else
					type = 3;

				PersnalShopInstance psi = World.findPersnalShop(ownerObjId);
				if (psi == null) {
					psi = new PersnalShopInstance(ownerObjId, ownerName, ownerAccountUid);
				}

				PersnalShopItem psItem = new PersnalShopItem();
				psItem.setType(type);
				psItem.setBless(bless);
				psItem.setCount(count);
				psItem.setDefinite(definite);
				psItem.setEnEarth(enEarth);
				psItem.setEnFire(enFire);
				psItem.setEnWater(enWater);
				psItem.setEnWind(enWind);
				psItem.setEnLevel(enLevel);
				psItem.setMainItem(iteminstance);
				psItem.setName(iteminstance.getItem().getName());
				psItem.setItemobjid(itemobjid);
				psItem.setPrice(price);
				psItem.setItem_id(item_id);
				psItem.setInvGfx(iteminstance.getItem().getInvGfx());
				psItem.setShoptype(shopType);
				psItem.setCharName(charName);

				if (shopType.equalsIgnoreCase("판매")) {
					psi.getItemList().add(psItem);
				} else if (shopType.equalsIgnoreCase("매입")) {
					psi.getSellList().add(psItem);
				}

				initSpawn(psi);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st, rs);
		}

		TimeLine.end();
	}

	private void initSpawn(PersnalShopInstance psi) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters_shop_npc WHERE owner_obj_id=?");
			st.setInt(1, (int) psi.getPcObjectId());
			rs = st.executeQuery();
			if (rs.next()) {
				int x = rs.getInt("loc_x");
				int y = rs.getInt("loc_y");
				int map = rs.getInt("loc_map");

				psi.setPurchasePrice(rs.getInt("purchase"));
				psi.toTeleport(x, y, map, false);
				list.add(psi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st, rs);
		}

	}

	static public void toSave() {
		Connection con = null;
		try {
			con = DatabaseConnection.getLineage();

			for (PersnalShopInstance psi : list)
				psi.toSaveAll(con);

		} catch (Exception e) {
			System.out.println("유저상점 toSaveAll() 오류");
			lineage.share.System.println("유저상점 toSaveAll() 오류");
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con);
		}
	}
}
