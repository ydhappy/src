package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Drop;
import lineage.bean.database.Monster;
import lineage.share.Lineage;
import lineage.share.TimeLine;

public final class MonsterDropDatabase {
	// 리스트 많이쓰면 안좋아여?? 커넥션수를 최소한으로 줄이는게 좋다고 들엇는데 그쳐?? 리스트 하나가 커넥션 여는것보다
	// 좋을거같아서 저두요 그렇게 할게영네
	static private List<Drop> list;

	static public void init(Connection con) {
		TimeLine.start("MonsterDropDatabase..");
		if (list == null) {
			list = new ArrayList<Drop>();
		}
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			list.clear();
			st = con.prepareStatement("SELECT * FROM monster_drop");

			rs = st.executeQuery();
			while (rs.next()) {
				Monster m = MonsterDatabase.find(rs.getString("monster_name"));
				if (m != null) {
					Drop d = new Drop();

					d.setName(rs.getString("name"));
					d.setMonName(rs.getString("monster_name"));
					d.setItemName(rs.getString("item_name"));
					d.setItemBress(rs.getInt("item_bress"));
					d.setItemEn(rs.getInt("item_en"));
					d.setCountMin(rs.getInt("count_min"));
					d.setCountMax(rs.getInt("count_max"));
					d.setChance(rs.getDouble("chance"));
					d.setEa(rs.getInt("ea"));
					m.getDropList().add(d);
					list.add(d);
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", MonsterDropDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}

		TimeLine.end();
	}

	/**
	 * 아이템이름을 드랍하는 드랍객체 찾아서 리턴.
	 * 
	 * @param itemName
	 * @return
	 */
	public static List<Drop> find(String itemName) {
		List<Drop> list = new ArrayList<Drop>();
		for (Monster m : MonsterDatabase.getList()) {
			for (Drop d : m.getDropList()) {
				if (d.getItemName().indexOf(itemName) >= 0) {
					list.add(d);
					break;
				}
			}
		}
		return list;
	}

	public static List<Monster> findName(String name) {
		List<Monster> d = new ArrayList<Monster>();
		d.clear();
		
		if (!name.contains(" ")) {
			for (Drop dd : list) {
				if (dd.getMonName().replace(" ", "").equalsIgnoreCase(name)) {
					d.add(MonsterDatabase.find(dd.getMonName()));
				}
			}
		} else {
			for (Drop dd : list) {
				if (dd.getMonName().equalsIgnoreCase(name)) {
					d.add(MonsterDatabase.find(dd.getMonName()));
				}
			}
		}

		return d;
	}
	
	public static List<Drop> getList(){
		synchronized (list) {
			return list;
		}
	}

//	/**
//	 * 아이템이름을 드랍하는 드랍객체 찾아서 리턴.
//	 * @param itemName
//	 * @return
//	 */
//	public static List<Drop> find1(String name) {
//		List<Drop> list = new ArrayList<Drop>();
//		for(Monster m : MonsterDatabase.getList()) {
//			for(Drop d : m.getDropList()) {
//				if(m.getName().replace(" ", "").equalsIgnoreCase(name)) {
//					list.add(d);
//					break;
//				}
//			}
//		}
//		return list;
//	}

	/*
	 * public static List<Drop> find(int itemName){ List<Drop> list = new
	 * ArrayList<Drop>(); for(Monster m : MonsterDatabase.getList()){ for (Drop d :
	 * m.getDropList()){ if(d.getItemName().indexOf(itemName) >= 0){ list.add(d);
	 * break; } } } return list; }
	 */

	public static void DropInit() {
		Connection localConnection = null;
		PreparedStatement localPreparedStatement = null;
		ResultSet localResultSet = null;
		try {
			localConnection = DatabaseConnection.getLineage();
			localPreparedStatement = localConnection.prepareStatement("SELECT name FROM monster_drop GROUP BY name");
			localResultSet = localPreparedStatement.executeQuery();
			while (localResultSet.next()) {
				Monster localMonster = MonsterDatabase.find(localResultSet.getString("name"));
				if (localMonster != null)
					localMonster.getDropList().clear();
			}
		} catch (Exception localException) {
			lineage.share.System.printf("%s : DropInit()\r\n", new Object[] { MonsterDropDatabase.class.toString() });
			lineage.share.System.println(localException);
		} finally {
			DatabaseConnection.close(localConnection, localPreparedStatement, localResultSet);
		}
	}

}
