package jsn_soft;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Monster;
import lineage.database.DatabaseConnection;
import lineage.database.MonsterDatabase;
import lineage.share.TimeLine;
import lineage.world.object.instance.MonsterInstance;

public class KingdomBossDatabase {

	static private List<KingdomBoss> spawn_list;
	static private List<MonsterInstance> list;
	static public List<Long> list_object;

	static public void init(Connection con) {
		TimeLine.start("KingdomBossDatabase..");

		spawn_list = new ArrayList<KingdomBoss>();
		list = new ArrayList<MonsterInstance>();
		list_object = new ArrayList<Long>();

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM Kingdom_Boss");
			rs = st.executeQuery();
			while (rs.next()) {
				KingdomBoss kb = new KingdomBoss();
				Monster m = MonsterDatabase.find(rs.getString("MonsterName"));
				kb.setName(m.getName());
				kb.setKingdomUid(rs.getInt("Kingdom_uid"));
				kb.setOverlap(rs.getString("중복").equalsIgnoreCase("true"));
				kb.setTime(getSpawnTime(rs.getString("스폰 시간")));
				kb.setMon(m);
				
				spawn_list.add(kb);
			}

		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", KingdomBossDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}

		TimeLine.end();
	}

	static private int[] getSpawnTime(String life_time) {
		if (life_time.length() > 0) {
			int[] time = new int[2];
			// 0번 글부터 즉 11:00 이면 1부터
			// : 전까지 11 을 넣는다
			String boss_h = life_time.substring(0, life_time.indexOf(":"));
			// : 후의 00을 넣는다
			String boss_m = life_time.substring(boss_h.length() + 1, life_time.length());
			time[0] = Integer.valueOf(boss_h);
			time[1] = Integer.valueOf(boss_m);
			return time;
		}
		return null;
	}

	public static List<KingdomBoss> getSpawn_list() {
		return spawn_list;
	}

	public static List<MonsterInstance> getList() {
		return list;
	}

	public static void appendSpawn_list(KingdomBoss m) {
		spawn_list.add(m);
	}

	public static void appendList(MonsterInstance m) {
		list.add(m);
	}

	public static KingdomBoss findSpawn(String name, int uid) {
		if (spawn_list == null)
			return null;
		
		for (KingdomBoss kb : spawn_list) {
			if (kb != null && kb.getName().equalsIgnoreCase(name) && kb.getKingdomUid() == uid) {
				return kb;
			}
		}
		return null;
	}
	
	public static KingdomBoss findSpawn(int uid, int h, int m) {
		if (spawn_list == null)
			return null;
		
		for (KingdomBoss kb : spawn_list) {
			if (kb != null && kb.getKingdomUid() == uid) {
				// 10분 진행된애를 찾을때는 커지면 된다 만약 현시간 위엣서 아래로 ==?
				if (kb.getTime()[0] == h && kb.getTime()[1] == m)
					return kb;
			}
		}
		return null;
	}
	
	public static MonsterInstance findBoss(String name) {
		if (list == null)
			return null;
		
		for (MonsterInstance m : list) {
			if (m != null && m.getMonster() != null && m.getMonster().getName().equalsIgnoreCase(name)) {
				return m;
			}
		}
		return null;
	}
}
