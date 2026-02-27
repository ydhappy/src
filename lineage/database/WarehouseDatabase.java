package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Warehouse;
import lineage.bean.database.WebAuction;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;

public class WarehouseDatabase {

	private static List<Warehouse> pool;

	public static void init() {
		TimeLine.start("WarehouseDatabase..");

		pool = new ArrayList<Warehouse>();

		TimeLine.end();
	}

	static public Warehouse getPool() {
		Warehouse wh = null;
		synchronized (pool) {
			if (Lineage.memory_recycle && pool.size() > 0) {
				wh = pool.get(0);
				pool.remove(0);
			} else {
				wh = new Warehouse();
			}
		}
		return wh;
	}

	static public void setPool(Warehouse wh) {
		if (wh == null)
			return;
		wh.clear();
		if (!Lineage.memory_recycle)
			return;
		synchronized (pool) {
			if (!pool.contains(wh))
				pool.add(wh);
		}
	}

	static public void setPool(List<Warehouse> list) {
		for (Warehouse wh : list)
			setPool(wh);
		list.clear();
	}

	public static Warehouse getAden(int uid, int dwarf_type) {
		Warehouse wh = null;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			switch (dwarf_type) {
			case 1:
				st = con.prepareStatement("SELECT * FROM warehouse_clan WHERE account_uid=? AND name='아데나' AND bress=1");
				break;
			case 2:
				st = con.prepareStatement("SELECT * FROM warehouse_elf WHERE account_uid=? AND name='아데나' AND bress=1");
				break;
			default:
				st = con.prepareStatement("SELECT * FROM warehouse WHERE account_uid=? AND name='아데나' AND bress=1");
				break;
			}
			st.setInt(1, uid);
			rs = st.executeQuery();
			if (rs.next())
				wh = get(rs, dwarf_type);

		} catch (Exception e) {
			lineage.share.System.println(WarehouseDatabase.class.toString()
					+ " : getAden(int uid, int dwarf_type)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return wh;
	}

	public static void delete(int uid, int dwarf_type) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			switch (dwarf_type) {
			case 1:
				st = con.prepareStatement("DELETE FROM warehouse_clan WHERE uid=?");
				break;
			case 2:
				st = con.prepareStatement("DELETE FROM warehouse_elf WHERE uid=?");
				break;
			default:
				st = con.prepareStatement("DELETE FROM warehouse WHERE uid=?");
				break;
			}
			st.setInt(1, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.println(WarehouseDatabase.class.toString()
					+ " : delete(int uid, int dwarf_type)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	public static void clear(long key, int dwarf_type) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			switch (dwarf_type) {
			case 1:
				st = con.prepareStatement("DELETE FROM warehouse_clan WHERE clan_id=?");
				break;
			case 2:
				st = con.prepareStatement("DELETE FROM warehouse_elf WHERE account_uid=?");
				break;
			case 3:
				st = con.prepareStatement("DELETE FROM warehouse_clan_log WHERE clan_uid=?");
				break;
			default:
				st = con.prepareStatement("DELETE FROM warehouse WHERE account_uid=?");
				break;
			}
			st.setLong(1, key);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.println(WarehouseDatabase.class.toString()
					+ " : clear(int uid, int dwarf_type)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	public static Warehouse find(int uid, int dwarf_type) {
		Warehouse wh = null;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			switch (dwarf_type) {
			case 1:
				st = con.prepareStatement("SELECT * FROM warehouse_clan WHERE uid=?");
				break;
			case 2:
				st = con.prepareStatement("SELECT * FROM warehouse_elf WHERE uid=?");
				break;
			default:
				st = con.prepareStatement("SELECT * FROM warehouse WHERE uid=?");
				break;
			}
			st.setInt(1, uid);
			rs = st.executeQuery();
			if (rs.next())
				wh = get(rs, dwarf_type);

		} catch (Exception e) {
			lineage.share.System.println(WarehouseDatabase.class.toString()
					+ " : find(int uid, int dwarf_type)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return wh;
	}

	public static List<Warehouse> getList(long id, int dwarf_type) {
		List<Warehouse> list = new ArrayList<Warehouse>();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			switch (dwarf_type) {
			case 1:
				st = con.prepareStatement("SELECT * FROM warehouse_clan WHERE clan_id=?");
				break;
			case 2:
				st = con.prepareStatement("SELECT * FROM warehouse_elf WHERE account_uid=?");
				break;
			default:
				st = con.prepareStatement("SELECT * FROM warehouse WHERE account_uid=?");
				break;
			}
			st.setLong(1, id);
			rs = st.executeQuery();
			while (rs.next())
				list.add(get(rs, dwarf_type));
		} catch (Exception e) {
			lineage.share.System.println(WarehouseDatabase.class.toString()
					+ " : getList(int id, int dwarf_type)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return list;
	}

	/**
	 * 중복코드 방지용.
	 * 
	 * @param rs
	 * @param dwarf_type
	 * @return
	 * @throws Exception
	 */
	private static Warehouse get(ResultSet rs, int dwarf_type) throws Exception {
		//
		Warehouse wh = getPool();
		wh.setUid(rs.getInt("uid"));
		if (dwarf_type == 1)
			wh.setClanId(rs.getInt("clan_id"));
		else
			wh.setAccountUid(rs.getInt("account_uid"));
		wh.setInvId(rs.getInt("inv_id"));
		wh.setPetId(rs.getInt("pet_id"));
		wh.setLetterId(rs.getInt("letter_id"));
		wh.setName(rs.getString("name"));
		wh.setType(rs.getInt("type"));
		wh.setGfxid(rs.getInt("gfxid"));
		wh.setCount(rs.getLong("count"));
		wh.setQuantity(rs.getInt("quantity"));
		wh.setEn(rs.getInt("en"));
		wh.setDefinite(rs.getInt("definite") == 1);
		wh.setBress(rs.getInt("bress"));
		wh.setDurability(rs.getInt("durability"));
		wh.setTime(rs.getInt("time"));
		wh.setOption(rs.getString("options"));
		wh.setEnEarth(rs.getInt("enEarth"));
		wh.setEnWater(rs.getInt("enWater"));
		wh.setEnFire(rs.getInt("enFire"));
		wh.setEnWind(rs.getInt("enWind"));
		
		wh.setAdd_Min_Dmg ( rs.getInt("add_min_dmg") );
		wh.setAdd_Max_Dmg ( rs.getInt("add_max_dmg") );
		wh.setAdd_Str ( rs.getInt("add_str"));
		wh.setAdd_Dex ( rs.getInt("add_dex"));
		wh.setAdd_Con ( rs.getInt("add_con"));
		wh.setAdd_Int ( rs.getInt("add_int"));
		wh.setAdd_Wiz ( rs.getInt("add_wiz"));
		wh.setAdd_Cha ( rs.getInt("add_cha"));
		wh.setAdd_Mana ( rs.getInt("add_mana"));
		wh.setAdd_Hp (rs.getInt("add_hp"));
		wh.setAdd_Manastell (rs.getInt("add_manastell"));
		wh.setAdd_Hpstell (rs.getInt("add_hpstell"));
		wh.setOne(rs.getInt("one"));
		wh.setTwo(rs.getInt("two"));
		wh.setThree(rs.getInt("three"));
		wh.setFour(rs.getInt("four"));
		wh.setSoul_Cha(rs.getInt("soul_cha"));
		//
		Item item = ItemDatabase.find(wh.getName());
		if (item != null) {
			wh.set구분1(item.getType1());
			wh.set구분2(item.getType2());
		}
		//
		return wh;
	}

	/**
	 * 창고에 아이템이 몇개있는지 추출하는 메서드.
	 */
	public static int getCount(long uid, int dwarf_type) {
		int count = 0;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			switch (dwarf_type) {
			case 1:
				st = con.prepareStatement("SELECT COUNT(*) FROM warehouse_clan WHERE clan_id=?");
				break;
			case 2:
				st = con.prepareStatement("SELECT COUNT(*) FROM warehouse_elf WHERE account_uid=?");
				break;
			default:
				st = con.prepareStatement("SELECT COUNT(*) FROM warehouse WHERE account_uid=?");
				break;
			}
			st.setLong(1, uid);
			rs = st.executeQuery();
			if (rs.next())
				count = rs.getInt(1);
		} catch (Exception e) {
			lineage.share.System.println(WarehouseDatabase.class.toString()
					+ " : getCount(int uid, int dwarf_type)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}

		return count;
	}

	/**
	 * 
	 * @param wa
	 */
	public static void insert(WebAuction wa, int account_uid) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			//
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("INSERT INTO warehouse SET account_uid=?, inv_id=?, name=?, type=?, gfxid=?, count=?, quantity=?, en=?, definite=?, bress=?, durability=?, time=?, pet_id=?, letter_id=?");
			st.setInt(1, account_uid);
			st.setInt(2, wa.getInvId());
			st.setString(3, wa.getName());
			st.setInt(4, wa.getType());
			st.setInt(5, wa.getGfxid());
			st.setLong(6, wa.getCount());
			st.setInt(7, wa.getQuantity());
			st.setInt(8, wa.getEn());
			st.setInt(9, wa.isDefinite() ? 1 : 0);
			st.setInt(10, wa.getBress());
			st.setInt(11, wa.getDurability());
			st.setInt(12, wa.getTime());
			st.setInt(13, wa.getPetId());
			st.setInt(14, wa.getLetterId());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.println(WarehouseDatabase.class.toString()
					+ " : insert(WebAuction wa)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	/**
	 * 디비에 아이템 등록할때 처리하는 메서드.
	 */
	public static void insert(ItemInstance item, long inv_id, long count,
			long uid, int dwarf_type) {
		int type = 0;
		if (item instanceof ItemWeaponInstance)
			type = 1;
		else if (item instanceof ItemArmorInstance)
			type = 2;
		else
			type = 3;

		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			switch (dwarf_type) {
			case 1:
				st = con.prepareStatement("INSERT INTO warehouse_clan SET clan_id=?, inv_id=?, name=?, type=?, gfxid=?, count=?, quantity=?, en=?, definite=?, bress=?, durability=?, time=?, pet_id=?, letter_id=?, options=?, enEarth=?, enWater=?, enFire=?, enWind=?, add_min_dmg=?, add_max_dmg=?, add_str=?,add_dex=?,add_con=?,add_int=?,add_wiz=?,add_cha=?,add_mana=?,add_hp=?,add_manastell=?,add_hpstell=?,one=?,two=?,three=?,four=?,soul_cha=?");
				break;
			case 2:
				st = con.prepareStatement("INSERT INTO warehouse_elf SET account_uid=?, inv_id=?, name=?, type=?, gfxid=?, count=?, quantity=?, en=?, definite=?, bress=?, durability=?, time=?, pet_id=?, letter_id=?, options=?, enEarth=?, enWater=?, enFire=?, enWind=?, add_min_dmg=?, add_max_dmg=?, add_str=?,add_dex=?,add_con=?,add_int=?,add_wiz=?,add_cha=?,add_mana=?,add_hp=?,add_manastell=?,add_hpstell=?,one=?,two=?,three=?,four=?,soul_cha=?");
				break;
			default:
				st = con.prepareStatement("INSERT INTO warehouse SET account_uid=?, inv_id=?, name=?, type=?, gfxid=?, count=?, quantity=?, en=?, definite=?, bress=?, durability=?, time=?, pet_id=?, letter_id=?, options=?, enEarth=?, enWater=?, enFire=?, enWind=?, add_min_dmg=?, add_max_dmg=?, add_str=?,add_dex=?,add_con=?,add_int=?,add_wiz=?,add_cha=?,add_mana=?,add_hp=?,add_manastell=?,add_hpstell=?,one=?,two=?,three=?,four=?,soul_cha=?");
				break;
			}
			st.setLong(1, uid);
			st.setLong(2, inv_id);
			st.setString(3, item.getItem().getName());
			st.setInt(4, type);
			st.setInt(5, item.getItem().getInvGfx());
			st.setLong(6, count);
			st.setInt(7, item.getQuantity());
			st.setInt(8, item.getEnLevel());
			st.setInt(9, item.isDefinite() ? 1 : 0);
			st.setInt(10, item.getBress());
			st.setInt(11, item.getDurability());
			st.setInt(12, item.getTime());
			st.setLong(13, item.getPetObjectId());
			st.setLong(14, item.getLetterUid());
			st.setString(15, item.getOption());
			st.setInt(16, item.getEnEarth());
			st.setInt(17, item.getEnWater());
			st.setInt(18, item.getEnFire());
			st.setInt(19, item.getEnWind());
			st.setInt(20, item.getAdd_Min_Dmg());
			st.setInt(21, item.getAdd_Max_Dmg());
			st.setInt(22, item.getAdd_Str());
			st.setInt(23, item.getAdd_Dex());
			st.setInt(24, item.getAdd_Con());
			st.setInt(25, item.getAdd_Int());
			st.setInt(26, item.getAdd_Wiz());
			st.setInt(27, item.getAdd_Cha());
			st.setInt(28, item.getAdd_Mana());
			st.setInt(29, item.getAdd_Hp());
			st.setInt(30, item.getAdd_Manastell());
			st.setInt(31, item.getAdd_Hpstell());
			st.setInt(32, item.getOne());
			st.setInt(33, item.getTwo());
			st.setInt(34, item.getThree());
			st.setInt(35, item.getFour());
			st.setLong(36, item.getSoul_Cha());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System
					.println(WarehouseDatabase.class.toString()
							+ " : insert(ItemInstance item, int inv_id, int count, int uid, int dwarf_type)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}

	}

	/**
	 * 창고에 같은 종류에 아이템이 존재한다는걸 미리 확인 했기때문에 그 정보를 토대로 count값만 갱신함.
	 */
	public static void update(String name, int bress, long uid, long count,
			int dwarf_type) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			switch (dwarf_type) {
			case 1:
				st = con.prepareStatement("UPDATE warehouse_clan SET count=count+? WHERE clan_id=? AND name=? AND bress=?");
				break;
			case 2:
				st = con.prepareStatement("UPDATE warehouse_elf SET count=count+? WHERE account_uid=? AND name=? AND bress=?");
				break;
			default:
				st = con.prepareStatement("UPDATE warehouse SET count=count+? WHERE account_uid=? AND name=? AND bress=?");
				break;
			}
			st.setLong(1, count);
			st.setLong(2, uid);
			st.setString(3, name);
			st.setInt(4, bress);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System
					.println(WarehouseDatabase.class.toString()
							+ " : update(ItemInstance item, int uid, int count, boolean clan)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	/**
	 * 창고에 겹쳐질 수 있는 아이템이 존재하는지 확인하는 메서드.
	 */
	//
	public static long isPiles(boolean piles, long uid, String name, int bress,
			int dwarf_type) {
		long inv_id = 0;

		if (piles) {
			Connection con = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				con = DatabaseConnection.getLineage();
				switch (dwarf_type) {
				case 1:
					st = con.prepareStatement("SELECT * FROM warehouse_clan WHERE clan_id=? AND name=? AND bress=?");
					break;
				case 2:
					st = con.prepareStatement("SELECT * FROM warehouse_elf WHERE account_uid=? AND name=? AND bress=?");
					break;
				default:
					st = con.prepareStatement("SELECT * FROM warehouse WHERE account_uid=? AND name=? AND bress=?");
					break;
				}
				st.setLong(1, uid);
				st.setString(2, name);
				st.setInt(3, bress);
				rs = st.executeQuery();
				if (rs.next())
					inv_id = rs.getLong("inv_id");
			} catch (Exception e) {
				lineage.share.System
						.println(WarehouseDatabase.class.toString()
								+ " : isPiles(ItemInstance item, int uid, int dwarf_type)");
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st, rs);
			}
		}

		return inv_id;
	}

}
