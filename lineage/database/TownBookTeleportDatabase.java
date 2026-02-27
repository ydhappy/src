package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lineage.bean.database.FirstSpawn;
import lineage.bean.database.TownBook;
import lineage.share.TimeLine;

public class TownBookTeleportDatabase {
	static private List<TownBook> list;

	/**
	 * 초기화 처리 함수.
	 */
	static public void init(Connection con) {
		TimeLine.start("TownBookTeleport..");

		list = new ArrayList<TownBook>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM town_book_teleport");
			rs = st.executeQuery();

			while (rs.next()) {
				TownBook db = new TownBook();
				db.setUid(rs.getInt("uid"));
				db.setName(rs.getString("던전명"));
				setLoc(db, rs.getString("좌표"));
				db.setLevel(rs.getInt("입장레벨"));
				db.setClan(rs.getInt("혈맹확인") == 1 ? true : false);
				db.setWanted(rs.getInt("수배확인") == 1 ? true : false);
				db.setAden(rs.getString("입장시_소모_아이템") == null ? "" : rs.getString("입장시_소모_아이템"));
				db.setCount(rs.getLong("수량"));

				list.add(db);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", TownBookTeleportDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}

		TimeLine.end();
	}

	static public void reload() {
		TimeLine.start("town_book_teleport 테이블 리로드 - ");

		list.clear();

		PreparedStatement st = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM town_book_teleport");
			rs = st.executeQuery();

			while (rs.next()) {
				TownBook db = new TownBook();
				db.setUid(rs.getInt("uid"));
				db.setName(rs.getString("던전명"));
				setLoc(db, rs.getString("좌표"));
				db.setLevel(rs.getInt("입장레벨"));
				db.setClan(rs.getInt("혈맹확인") == 1 ? true : false);
				db.setWanted(rs.getInt("수배확인") == 1 ? true : false);
				db.setAden(rs.getString("입장시_소모_아이템") == null ? "" : rs.getString("입장시_소모_아이템"));
				db.setCount(rs.getLong("수량"));

				list.add(db);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : reload()\r\n", TownBookTeleportDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}

		TimeLine.end();
	}

	static public List<TownBook> getList() {
		return new ArrayList<TownBook>(list);
	}
	
	static public TownBook find(int uid) {
		for (TownBook db : getList()) {
			if (db.getUid() == uid) {
				return db;
			}
		}
		
		return null;
	}

	static public void setLoc(TownBook db, String loc) {
		if (db != null && db.getLoc_list() != null && loc != null) {
			StringTokenizer st = new StringTokenizer(loc, ",");

			while (st.hasMoreTokens()) {
				try {
					StringTokenizer stt = new StringTokenizer(st.nextToken(), " ");

					if (stt.hasMoreTokens()) {
						FirstSpawn fs = new FirstSpawn(Integer.valueOf(stt.nextToken()), Integer.valueOf(stt.nextToken()), Integer.valueOf(stt.nextToken()));
						db.getLoc_list().add(fs);
					}
				} catch (Exception e) {
					lineage.share.System.printf("%s : setLoc(TownBook db, String loc) 에러. - [%s]\r\n", TownBookTeleportDatabase.class.toString(), db.getName());
					lineage.share.System.println(e);
				}
			}
		}
	}
}
