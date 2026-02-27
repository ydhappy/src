package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.DungeonPartTime;
import lineage.share.TimeLine;

public class DungeonPartTimeDatabase {

	static private List<DungeonPartTime> list;

	static public void init(Connection con) {
		TimeLine.start("DungeonPartTimeDatabase..");

		if (list == null)
			list = new ArrayList<DungeonPartTime>();
		else
			list.clear();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM dungeon_parttime");
			rs = st.executeQuery();
			while (rs.next()) {
				DungeonPartTime d = new DungeonPartTime();
				d.setUid(rs.getInt("uid"));
				d.setName(rs.getString("name"));
				d.setTime(rs.getInt("time"));
				for (String mapId : rs.getString("mapid").split(","))
					d.getList().add(Integer.valueOf(mapId));
				d.setTimeSeek(rs.getString("time_seek"));
				d.setTimeReset(rs.getString("time_reset"));
				d.setMessageOut(rs.getString("시간다됫을때 멘트"));
				// 등록
				list.add(d);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", DungeonPartTimeDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}

		TimeLine.end();
	}

	static public DungeonPartTime find(int mapId) {
		for (DungeonPartTime d : list) {
			if (d.getList().contains(mapId))
				return d;
		}
		return null;
	}

	static public DungeonPartTime findUid(int uid) {
		for (DungeonPartTime d : list) {
			if (d.getUid() == uid)
				return d;
		}
		return null;
	}

	static public DungeonPartTime find(String name) {
		for (DungeonPartTime d : list) {
			if (d.getName().equalsIgnoreCase(name))
				return d;
		}
		return null;
	}
	
	static public List<DungeonPartTime> getList() {
		return list;
	}
	
}
