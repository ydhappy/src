package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import lineage.bean.database.DungeonPartTime;
import lineage.bean.database.FirstInventory;
import lineage.bean.database.FirstSpawn;
import lineage.bean.database.FirstSpell;
import lineage.bean.database.Item;
import lineage.bean.database.Skill;
import lineage.bean.lineage.Agit;
import lineage.bean.lineage.Book;
import lineage.bean.lineage.Buff;
import lineage.bean.lineage.BuffInterface;
import lineage.bean.lineage.Clan;
import lineage.bean.lineage.Inventory;
import lineage.bean.lineage.Quest;
import lineage.network.LineageClient;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_InventoryList;
import lineage.network.packet.server.S_ObjectGfx;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BookController;
import lineage.world.controller.BuffController;
import lineage.world.controller.DungeonController;
import lineage.world.controller.QuestController;
import lineage.world.controller.SkillController;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.Letter;
import lineage.world.object.magic.BlessOfEarth;
import lineage.world.object.magic.BuffRing;
import lineage.world.object.magic.ChattingClose;
import lineage.world.object.magic.CurseBlind;
import lineage.world.object.magic.CurseParalyze;
import lineage.world.object.magic.CursePoison;
import lineage.world.object.magic.EarthSkin;
import lineage.world.object.magic.EnchantDexterity;
import lineage.world.object.magic.EnchantMighty;
import lineage.world.object.magic.FrameSpeedOverStun;
import lineage.world.object.magic.Haste;
import lineage.world.object.magic.HolyWalk;
import lineage.world.object.magic.IceLance;
import lineage.world.object.magic.ImmuneToHarm;
import lineage.world.object.magic.IronSkin;
import lineage.world.object.magic.Light;
import lineage.world.object.magic.MaanBirth;
import lineage.world.object.magic.MaanBirthDelay;
import lineage.world.object.magic.MaanEarth;
import lineage.world.object.magic.MaanEarthDelay;
import lineage.world.object.magic.MaanFire;
import lineage.world.object.magic.MaanFireDelay;
import lineage.world.object.magic.MaanLife;
import lineage.world.object.magic.MaanLifeDelay;
import lineage.world.object.magic.MaanShape;
import lineage.world.object.magic.MaanShapeDelay;
import lineage.world.object.magic.MaanWatar;
import lineage.world.object.magic.MaanWatarDelay;
import lineage.world.object.magic.MaanWind;
import lineage.world.object.magic.MaanWindDelay;
import lineage.world.object.magic.ResistElemental;
import lineage.world.object.magic.ShadowArmor;
import lineage.world.object.magic.ShapeChange;
import lineage.world.object.magic.Shield;
import lineage.world.object.magic.Slow;
import lineage.world.object.magic.item.Blue;
import lineage.world.object.magic.item.Bravery;
import lineage.world.object.magic.item.Eva;
import lineage.world.object.magic.item.FloatingEyeMeat;
import lineage.world.object.magic.item.Wafer;
import lineage.world.object.magic.item.Wisdom;
import lineage.world.object.magic.monster.CurseGhast;
import lineage.world.object.magic.monster.CurseGhoul;
import lineage.world.object.magic.sp.마력증강마법;
import lineage.world.object.magic.sp.전투강화마법;
import lineage.world.object.magic.sp.체력증강마법;
import sp.magic.드래곤다이아몬드버프;
import sp.magic.드래곤루비버프;
import sp.magic.드래곤사파이어버프;
import sp.magic.드래곤에메랄드버프;

public final class CharactersDatabase {

	// 메모리에 임시 저장하여 사용자가 월드 접속 및 아웃시 성능을 높이기 위한 변수.
	static private Map<String, Map<String, Object>> characters;
	static private Map<Long, List<Map<String, Object>>> book;
	static private Map<Long, List<Map<String, Object>>> dungeon;
	static private Map<Long, Map<String, Object>> inventory;
	static private Map<Long, List<Map<String, Object>>> skill;
	static private Map<Long, List<Map<String, Object>>> quest;

	static public void init() {
		TimeLine.start("CharactersDatabase..");

		// 초기화
		characters = new HashMap<String, Map<String, Object>>();
		book = new HashMap<Long, List<Map<String, Object>>>();
		dungeon = new HashMap<Long, List<Map<String, Object>>>();
		inventory = new HashMap<Long, Map<String, Object>>();
		skill = new HashMap<Long, List<Map<String, Object>>>();
		quest = new HashMap<Long, List<Map<String, Object>>>();
		
		TimeLine.end();
	}

	static public void toWorldOut(PcInstance pc) {
	}

	static public void close(Connection con) {
		// db에 저장.
		saveCharacter(con, true);
		saveDungeon(con, true);
		saveInventory(con, true);
		saveSkill(con, true);
		saveBook(con, true);
		insertQuest(con, true);
	}

	static public void remove(long objectId, String name) {
		Map<String, Object> c = characters.remove(name);
		if (c != null)
			c.clear();
		List<Map<String, Object>> b = book.remove(objectId);
		if (b != null)
			b.clear();
		List<Map<String, Object>> d = dungeon.remove(objectId);
		if (d != null)
			d.clear();
		Map<String, Object> i = inventory.remove(objectId);
		if (i != null)
			i.clear();
		List<Map<String, Object>> s = skill.remove(objectId);
		if (s != null)
			s.clear();
		List<Map<String, Object>> q = quest.remove(objectId);
		if (q != null)
			q.clear();
	}

	static public void toTimer(long time) {
		//
		Connection con = null;
		try {
			con = DatabaseConnection.getLineage();

			// db에 저장.
			saveCharacter(con, false);
			saveDungeon(con, false);
			saveInventory(con, false);
			saveSkill(con, false);
			saveBook(con, false);
			insertQuest(con, false);
			saveSkill(con, true); // 원래 2개 미저장
			saveBook(con, true);
			saveDungeon(con, true);
			insertQuest(con, true);
			
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(con);
		}
	}

	/**
	 * 사용자 정보 저장 함수.
	 * 
	 * @param con
	 * @param pc
	 */
	static public void savePcTrade(Connection con, long cancletime, long accountuid) {
		PreparedStatement st = null;
		try {
			// 처리.
			st = con.prepareStatement("UPDATE accounts SET cancel_time=? WHERE uid=?");
			st.setLong(1, cancletime);
			st.setLong(2, accountuid);
			
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : savePcTrade(Connection con, PcInstance pc)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(st);
		}
	}
	
	/**
	 * 판매 취소 시간 읽어옴
	 * 
	 * @param con
	 * @param c
	 * @param name
	 * @return
	 */
	static public void readPcTrade(LineageClient c, PcInstance pc) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM accounts WHERE uid=?");
			st.setInt(1, c.getAccountUid());
			rs = st.executeQuery();
			if (rs.next()) {
				pc.getClient().setBankAccount(rs.getString("bankaccount"));
				pc.getClient().setBankName(rs.getString("bankname"));
				pc.getClient().setBankNumber(rs.getString("banknumber"));
				pc.getClient().setBankUserName(rs.getString("bankusername"));
				pc.getClient().setBankUserNumber(rs.getString("bankusernumber"));

				pc.setCancelTime(rs.getLong("cancel_time"));
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : 현금거래(LineageClient c, PcInstance pc)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}
	
	/**
	 * 회상의촛불사용자 정보 저장 함수.
	 * 
	 * @param con
	 * @param pc
	 */
	static public void saveStatClear(Connection con, PcInstance pc) {
		PreparedStatement st = null;
		try {
			// 처리.
			st = con.prepareStatement("UPDATE characters SET statclear=? WHERE objID=?");
			st.setLong(1, pc.stat_clear ? 1 : 0);
			st.setLong(2, pc.getObjectId());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : saveStatClear(Connection con, PcInstance pc)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(st);
		}
	}
	

	/**
	 * 회상의촛불
	 * 
	 * @param con
	 * @param c
	 * @param name
	 * @return
	 */
	static public void readStatClear(PcInstance pc) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters WHERE objID=?");
			st.setLong(1, pc.getObjectId());
			rs = st.executeQuery();
			if (rs.next()) {
				pc.stat_clear = rs.getInt("statclear") == 1 ? true : false;
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : readStatClear(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	static public long getCharacterInventoryItemCount(long cha_objid, long item_objid) {
		if (cha_objid == 0 || item_objid == 0)
			return 0;

		//
		String weapon = null;
		String armor = null;
		String etc = null;

		// memory
		Map<String, Object> dbss = null;
		synchronized (inventory) {
			dbss = inventory.get(cha_objid);
			if (dbss != null) {
				weapon = (String) (dbss.get("weapon") == null ? "" : dbss.get("weapon"));
				armor = (String) (dbss.get("armor") == null ? "" : dbss.get("armor"));
				etc = (String) (dbss.get("etc") == null ? "" : dbss.get("etc"));
			}
		}

		// db
		if (weapon == null) {
			Connection con = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				con = DatabaseConnection.getLineage();
				// 찾기
				st = con.prepareStatement("SELECT * FROM characters_inventory WHERE cha_objId=?");
				st.setLong(1, cha_objid);
				rs = st.executeQuery();
				if (rs.next()) {
					weapon = rs.getString("weapon");
					armor = rs.getString("armor");
					etc = rs.getString("etc");
				}
			} catch (Exception e) {
				lineage.share.System.printf(
						"%s : updateCharacterInventoryItem(long cha_objId, long item_objId, long item_count, int item_bress)\r\n",
						CharactersDatabase.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st, rs);
			}
		}

		if (weapon == null)
			return 0;

		// search
		StringTokenizer token = new StringTokenizer(weapon, "\r\n");
		while (token.hasMoreTokens()) {
			String db = token.nextToken();
			if (db.indexOf(item_objid + ",") >= 0) {
				String[] dbs = db.split(",");
				if (Long.valueOf(dbs[0]) == item_objid)
					return Integer.valueOf(dbs[2]);
			}
		}
		token = new StringTokenizer(armor, "\r\n");
		while (token.hasMoreTokens()) {
			String db = token.nextToken();
			if (db.indexOf(item_objid + ",") >= 0) {
				String[] dbs = db.split(",");
				if (Long.valueOf(dbs[0]) == item_objid)
					return Integer.valueOf(dbs[2]);
			}
		}
		token = new StringTokenizer(etc, "\r\n");
		while (token.hasMoreTokens()) {
			String db = token.nextToken();
			if (db.indexOf(item_objid + ",") >= 0) {
				String[] dbs = db.split(",");
				if (Long.valueOf(dbs[0]) == item_objid)
					return Integer.valueOf(dbs[2]);
			}
		}

		return 0;
	}

	static public int getCharacterCharisma(String name) {
		// memory
		synchronized (characters) {
			Map<String, Object> db = characters.get(name);
			if (db != null)
				return (int) db.get("cha");
		}
		// db
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters WHERE LOWER(name)=?");
			st.setString(1, name.toLowerCase());
			rs = st.executeQuery();
			if (rs.next())
				return rs.getInt("cha");
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCharacterCharisma(String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return 0;
	}

	/**
	 * 케릭터 이름을 참고해어 디비 및 메모리정보에 클랜을 갱신.
	 * 
	 * @param name
	 * @param clan_name
	 * @param clan_uid
	 * @param title
	 */
	static public void updateCharacterClan(String name, String clan_name, long clan_uid, String title, int clan_grade) {
		// memory
		synchronized (characters) {
			Map<String, Object> db = characters.get(name);
			if (db != null) {
				if (title != null)
					db.put("title", title);
				db.put("clanID", clan_uid);
				db.put("clanNAME", clan_name);
				db.put("clan_grade", clan_grade);
			}
		}
		// db
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			int idx = 1;
			if (title != null)
				st = con.prepareStatement("UPDATE characters SET clanID=?, clanNAME=?, title=?, clan_grade=? WHERE LOWER(name)=?");
			else
				st = con.prepareStatement("UPDATE characters SET clanID=?, clanNAME=?, clan_grade=? WHERE LOWER(name)=?");
			st.setLong(idx++, clan_uid);
			st.setString(idx++, clan_name);
			if (title != null)
				st.setString(idx++, title);
			st.setInt(idx++, clan_grade);
			st.setString(idx++, name);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf(
					"%s : updateCharacterClan(String name, String clan_name, int clan_uid, String title,clan_grade)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	static public void removeCharacterInventoryItem(long cha_objId, long item_objId) {
		if (cha_objId == 0)
			return;

		//
		String weapon = null;
		String armor = null;
		String etc = null;

		// memory
		Map<String, Object> dbss = null;
		synchronized (inventory) {
			dbss = inventory.get(cha_objId);
			if (dbss != null) {
				weapon = (String) (dbss.get("weapon") == null ? "" : dbss.get("weapon"));
				armor = (String) (dbss.get("armor") == null ? "" : dbss.get("armor"));
				etc = (String) (dbss.get("etc") == null ? "" : dbss.get("etc"));
			}
		}

		// db
		if (weapon == null) {
			Connection con = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				con = DatabaseConnection.getLineage();
				// 찾기
				st = con.prepareStatement(
						"SELECT * FROM characters_inventory WHERE cha_objId=? AND (weapon LIKE ? OR armor LIKE ? OR etc LIKE ?)");
				st.setLong(1, cha_objId);
				st.setString(2, String.format("%%%d,%%", item_objId));
				st.setString(3, String.format("%%%d,%%", item_objId));
				st.setString(4, String.format("%%%d,%%", item_objId));
				rs = st.executeQuery();
				if (rs.next()) {
					weapon = rs.getString("weapon");
					armor = rs.getString("armor");
					etc = rs.getString("etc");
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : removeCharacterInventory(long cha_objId, long item_objId)\r\n",
						CharactersDatabase.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st, rs);
			}
		}

		// 수정
		if (weapon != null) {
			if (weapon.indexOf(String.valueOf(item_objId)) >= 0) {
				StringBuffer sb = new StringBuffer();
				StringTokenizer token = new StringTokenizer(weapon, "\r\n");
				while (token.hasMoreTokens()) {
					String db = token.nextToken();
					if (db.indexOf(String.valueOf(item_objId)) < 0)
						sb.append(db).append("\r\n");
				}
				weapon = sb.toString();
			}
			if (armor.indexOf(String.valueOf(item_objId)) >= 0) {
				StringBuffer sb = new StringBuffer();
				StringTokenizer token = new StringTokenizer(armor, "\r\n");
				while (token.hasMoreTokens()) {
					String db = token.nextToken();
					if (db.indexOf(String.valueOf(item_objId)) < 0)
						sb.append(db).append("\r\n");
				}
				armor = sb.toString();
			}
			if (etc.indexOf(String.valueOf(item_objId)) >= 0) {
				StringBuffer sb = new StringBuffer();
				StringTokenizer token = new StringTokenizer(etc, "\r\n");
				while (token.hasMoreTokens()) {
					String db = token.nextToken();
					if (db.indexOf(String.valueOf(item_objId)) < 0)
						sb.append(db).append("\r\n");
				}
				etc = sb.toString();
			}
		}

		// 갱신
		if (weapon != null) {
			if (dbss != null) {
				dbss.put("weapon", weapon);
				dbss.put("armor", armor);
				dbss.put("etc", etc);
				synchronized (inventory) {
					inventory.put(cha_objId, dbss);
				}
			} else {
				Connection con = null;
				PreparedStatement st = null;
				ResultSet rs = null;
				try {
					con = DatabaseConnection.getLineage();
					st = con.prepareStatement(
							"UPDATE characters_inventory SET weapon=?, armor=?, etc=? WHERE cha_objId=?");
					st.setString(1, weapon);
					st.setString(2, armor);
					st.setString(3, etc);
					st.setLong(4, cha_objId);
					st.executeUpdate();
				} catch (Exception e) {
					lineage.share.System.printf("%s : removeCharacterInventory(long cha_objId, long item_objId) 2\r\n",
							CharactersDatabase.class.toString());
					lineage.share.System.println(e);
				} finally {
					DatabaseConnection.close(con, st, rs);
				}
			}
		}
	}

	// static public void removeCharacterInventoryItem(String text) {
	// Connection con = null;
	// PreparedStatement st = null;
	// ResultSet rs = null;
	// List<Long> list = new ArrayList<Long>();
	// try {
	// con = DatabaseConnection.getLineage();
	// // 찾기
	// st =
	// con.prepareStatement("SELECT * FROM characters_inventory WHERE weapon LIKE ?
	// OR armor LIKE ? OR etc LIKE ?");
	// st.setString(1, String.format("%%,%s,%%", text));
	// st.setString(2, String.format("%%,%s,%%", text));
	// st.setString(3, String.format("%%,%s,%%", text));
	// rs = st.executeQuery();
	// while(rs.next())
	// list.add(rs.getLong("cha_objId"));
	// } catch (Exception e) {
	// lineage.share.System.printf("%s : removeCharacterInventory(String text)\r\n",
	// CharactersDatabase.class.toString());
	// lineage.share.System.println(e);
	// } finally {
	// DatabaseConnection.close(con, st, rs);
	// }
	// //
	// for(Long objId : list)
	// removeCharacterInventoryItem(objId, text);
	// }

	static public void removeCharacterInventoryItem(long objId, String text, int en_limit) {
		if (objId == 0)
			return;

		//
		String weapon = null;
		String armor = null;
		String etc = null;

		// memory
		Map<String, Object> dbss = null;
		synchronized (inventory) {
			dbss = inventory.get(objId);
			if (dbss != null) {
				weapon = (String) (dbss.get("weapon") == null ? "" : dbss.get("weapon"));
				armor = (String) (dbss.get("armor") == null ? "" : dbss.get("armor"));
				etc = (String) (dbss.get("etc") == null ? "" : dbss.get("etc"));
			}
		}

		// db
		if (weapon == null) {
			Connection con = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				con = DatabaseConnection.getLineage();
				// 찾기
				st = con.prepareStatement(
						"SELECT * FROM characters_inventory WHERE cha_objId=? AND (weapon LIKE ? OR armor LIKE ? OR etc LIKE ?)");
				st.setLong(1, objId);
				st.setString(2, String.format("%%,%s,%%", text));
				st.setString(3, String.format("%%,%s,%%", text));
				st.setString(4, String.format("%%,%s,%%", text));
				rs = st.executeQuery();
				if (rs.next()) {
					weapon = rs.getString("weapon");
					armor = rs.getString("armor");
					etc = rs.getString("etc");
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : removeCharacterInventory(long objId, String text)\r\n",
						CharactersDatabase.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st, rs);
			}
		}

		// 수정
		if (weapon != null) {
			if (weapon.indexOf(text) >= 0) {
				StringBuffer sb = new StringBuffer();
				StringTokenizer token = new StringTokenizer(weapon, "\r\n");
				while (token.hasMoreTokens()) {
					String db = token.nextToken();
					String[] dbs = db.split(",");
					if (text.equalsIgnoreCase(dbs[1])) {
						if (Integer.valueOf(dbs[4]) >= en_limit)
							sb.append(db).append("\r\n");
					} else
						sb.append(db).append("\r\n");
				}
				weapon = sb.toString();
			}
			if (armor.indexOf(text) >= 0) {
				StringBuffer sb = new StringBuffer();
				StringTokenizer token = new StringTokenizer(armor, "\r\n");
				while (token.hasMoreTokens()) {
					String db = token.nextToken();
					String[] dbs = db.split(",");
					if (text.equalsIgnoreCase(dbs[1])) {
						if (Integer.valueOf(dbs[4]) >= en_limit)
							sb.append(db).append("\r\n");
					} else
						sb.append(db).append("\r\n");
				}
				armor = sb.toString();
			}
			if (etc.indexOf(text) >= 0) {
				StringBuffer sb = new StringBuffer();
				StringTokenizer token = new StringTokenizer(etc, "\r\n");
				while (token.hasMoreTokens()) {
					String db = token.nextToken();
					String[] dbs = db.split(",");
					if (text.equalsIgnoreCase(dbs[1])) {
						if (Integer.valueOf(dbs[4]) >= en_limit)
							sb.append(db).append("\r\n");
					} else
						sb.append(db).append("\r\n");
				}
				etc = sb.toString();
			}
		}

		// 갱신
		if (weapon != null) {
			if (dbss != null) {
				dbss.put("weapon", weapon);
				dbss.put("armor", armor);
				dbss.put("etc", etc);
				synchronized (inventory) {
					inventory.put(objId, dbss);
				}
			} else {
				Connection con = null;
				PreparedStatement st = null;
				ResultSet rs = null;
				try {
					con = DatabaseConnection.getLineage();
					st = con.prepareStatement(
							"UPDATE characters_inventory SET weapon=?, armor=?, etc=? WHERE cha_objId=?");
					st.setString(1, weapon);
					st.setString(2, armor);
					st.setString(3, etc);
					st.setLong(4, objId);
					st.executeUpdate();
				} catch (Exception e) {
					lineage.share.System.printf("%s : removeCharacterInventory(long cha_objId, long item_objId) 2\r\n",
							CharactersDatabase.class.toString());
					lineage.share.System.println(e);
				} finally {
					DatabaseConnection.close(con, st, rs);
				}
			}
		}
	}

	static public void removeCharacterInventoryItem(long objId, String text) {
		if (objId == 0)
			return;

		//
		String weapon = null;
		String armor = null;
		String etc = null;

		// memory
		Map<String, Object> dbss = null;
		synchronized (inventory) {
			dbss = inventory.get(objId);
			if (dbss != null) {
				weapon = (String) (dbss.get("weapon") == null ? "" : dbss.get("weapon"));
				armor = (String) (dbss.get("armor") == null ? "" : dbss.get("armor"));
				etc = (String) (dbss.get("etc") == null ? "" : dbss.get("etc"));
			}
		}

		// db
		if (weapon == null) {
			Connection con = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				con = DatabaseConnection.getLineage();
				// 찾기
				st = con.prepareStatement(
						"SELECT * FROM characters_inventory WHERE cha_objId=? AND (weapon LIKE ? OR armor LIKE ? OR etc LIKE ?)");
				st.setLong(1, objId);
				st.setString(2, String.format("%%,%s,%%", text));
				st.setString(3, String.format("%%,%s,%%", text));
				st.setString(4, String.format("%%,%s,%%", text));
				rs = st.executeQuery();
				if (rs.next()) {
					weapon = rs.getString("weapon");
					armor = rs.getString("armor");
					etc = rs.getString("etc");
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : removeCharacterInventory(long objId, String text)\r\n",
						CharactersDatabase.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st, rs);
			}
		}

		// 수정
		if (weapon != null) {
			if (weapon.indexOf(text) >= 0) {
				StringBuffer sb = new StringBuffer();
				StringTokenizer token = new StringTokenizer(weapon, "\r\n");
				while (token.hasMoreTokens()) {
					String db = token.nextToken();
					String[] dbs = db.split(",");
					if (!text.equalsIgnoreCase(dbs[1]))
						sb.append(db).append("\r\n");
				}
				weapon = sb.toString();
			}
			if (armor.indexOf(text) >= 0) {
				StringBuffer sb = new StringBuffer();
				StringTokenizer token = new StringTokenizer(armor, "\r\n");
				while (token.hasMoreTokens()) {
					String db = token.nextToken();
					String[] dbs = db.split(",");
					if (!text.equalsIgnoreCase(dbs[1]))
						sb.append(db).append("\r\n");
				}
				armor = sb.toString();
			}
			if (etc.indexOf(text) >= 0) {
				StringBuffer sb = new StringBuffer();
				StringTokenizer token = new StringTokenizer(etc, "\r\n");
				while (token.hasMoreTokens()) {
					String db = token.nextToken();
					String[] dbs = db.split(",");
					if (!text.equalsIgnoreCase(dbs[1]))
						sb.append(db).append("\r\n");
				}
				etc = sb.toString();
			}
		}

		// 갱신
		if (weapon != null) {
			if (dbss != null) {
				dbss.put("weapon", weapon);
				dbss.put("armor", armor);
				dbss.put("etc", etc);
				synchronized (inventory) {
					inventory.put(objId, dbss);
				}
			} else {
				Connection con = null;
				PreparedStatement st = null;
				ResultSet rs = null;
				try {
					con = DatabaseConnection.getLineage();
					st = con.prepareStatement(
							"UPDATE characters_inventory SET weapon=?, armor=?, etc=? WHERE cha_objId=?");
					st.setString(1, weapon);
					st.setString(2, armor);
					st.setString(3, etc);
					st.setLong(4, objId);
					st.executeUpdate();
				} catch (Exception e) {
					lineage.share.System.printf("%s : removeCharacterInventory(long cha_objId, long item_objId) 2\r\n",
							CharactersDatabase.class.toString());
					lineage.share.System.println(e);
				} finally {
					DatabaseConnection.close(con, st, rs);
				}
			}
		}
	}

	// static public void removeCharacterInventoryItem(long objId, String text,
	// int en) {
	// if(objId == 0)
	// return;
	//
	// Connection con = null;
	// PreparedStatement st = null;
	// ResultSet rs = null;
	// try {
	// con = DatabaseConnection.getLineage();
	// String weapon = null;
	// String armor = null;
	// String etc = null;
	// // 찾기
	// st =
	// con.prepareStatement("SELECT * FROM characters_inventory WHERE cha_objId=?
	// AND (weapon LIKE ? OR armor LIKE ? OR etc LIKE ?)");
	// st.setLong(1, objId);
	// st.setString(2, String.format("%%,%s,%%", text));
	// st.setString(3, String.format("%%,%s,%%", text));
	// st.setString(4, String.format("%%,%s,%%", text));
	// rs = st.executeQuery();
	// if(rs.next()) {
	// weapon = rs.getString("weapon");
	// armor = rs.getString("armor");
	// etc = rs.getString("etc");
	// }
	// DatabaseConnection.close(st, rs);
	// // 수정
	// if(weapon != null) {
	// if(weapon.indexOf(text) >= 0) {
	// StringBuffer sb = new StringBuffer();
	// StringTokenizer token = new StringTokenizer(weapon, "\r\n");
	// while(token.hasMoreTokens()) {
	// String db = token.nextToken();
	// String[] dbs = db.split(",");
	// if(text.equalsIgnoreCase(dbs[1]) && Integer.valueOf(dbs[4])==en)
	// sb.append(db).append("\r\n");
	// }
	// weapon = sb.toString();
	// }
	// if(armor.indexOf(text) >= 0) {
	// StringBuffer sb = new StringBuffer();
	// StringTokenizer token = new StringTokenizer(armor, "\r\n");
	// while(token.hasMoreTokens()) {
	// String db = token.nextToken();
	// String[] dbs = db.split(",");
	// if(text.equalsIgnoreCase(dbs[1]) && Integer.valueOf(dbs[4])==en)
	// sb.append(db).append("\r\n");
	// }
	// armor = sb.toString();
	// }
	// if(etc.indexOf(text) >= 0) {
	// StringBuffer sb = new StringBuffer();
	// StringTokenizer token = new StringTokenizer(etc, "\r\n");
	// while(token.hasMoreTokens()) {
	// String db = token.nextToken();
	// String[] dbs = db.split(",");
	// if(text.equalsIgnoreCase(dbs[1]) && Integer.valueOf(dbs[4])==en)
	// sb.append(db).append("\r\n");
	// }
	// etc = sb.toString();
	// }
	// }
	// // 갱신
	// if(weapon != null) {
	// st =
	// con.prepareStatement("UPDATE characters_inventory SET weapon=?, armor=?,
	// etc=? WHERE cha_objId=?");
	// st.setString(1, weapon);
	// st.setString(2, armor);
	// st.setString(3, etc);
	// st.setLong(4, objId);
	// st.executeUpdate();
	// }
	// } catch (Exception e) {
	// lineage.share.System.printf("%s : removeCharacterInventory(long objId, String
	// text)\r\n",
	// CharactersDatabase.class.toString());
	// lineage.share.System.println(e);
	// } finally {
	// DatabaseConnection.close(con, st, rs);
	// }
	// }

	static public void appendCharacterInventoryItem(long cha_objid, String cha_name, ItemInstance item) {
		if (cha_objid == 0)
			return;

		// memory
		Map<String, Object> dbss = null;
		synchronized (inventory) {
			dbss = inventory.get(cha_objid);
		}
		if (dbss != null) {
			String weapon = (String) (dbss.get("weapon") == null ? "" : dbss.get("weapon"));
			String armor = (String) (dbss.get("armor") == null ? "" : dbss.get("armor"));
			String etc = (String) (dbss.get("etc") == null ? "" : dbss.get("etc"));
			if (item.getItem().getType1().equalsIgnoreCase("weapon"))
				weapon += item.toStringSaveDB();
			else if (item.getItem().getType1().equalsIgnoreCase("armor"))
				armor += item.toStringSaveDB();
			else
				etc += item.toStringSaveDB();
			dbss.put("weapon", weapon);
			dbss.put("armor", armor);
			dbss.put("etc", etc);
			synchronized (inventory) {
				inventory.put(cha_objid, dbss);
			}
			return;
		}

		// db
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			boolean find = false;
			boolean update = false;
			String weapon = null;
			String armor = null;
			String etc = null;
			// 찾기
			st = con.prepareStatement("SELECT * FROM characters_inventory WHERE cha_objId=?");
			st.setLong(1, cha_objid);
			rs = st.executeQuery();
			if (rs.next()) {
				find = true;
				weapon = rs.getString("weapon");
				armor = rs.getString("armor");
				etc = rs.getString("etc");
			}
			DatabaseConnection.close(st, rs);
			//
			if (item.getObjectId() == 0)
				item.setObjectId(ServerDatabase.nextItemObjId());
			//
			if (item.getItem().getType1().equalsIgnoreCase("weapon")) {
				if (find) {
					find = false;
					//
					if (item.getItem().isPiles()) {
						StringBuffer sb = new StringBuffer();
						StringTokenizer token = new StringTokenizer(weapon, "\r\n");
						while (token.hasMoreTokens()) {
							String db = token.nextToken();
							if (!update && db.indexOf("," + item.getItem().getName() + ",") >= 0) {
								String[] dbs = db.split(",");
								if (Integer.valueOf(dbs[7]) == item.getBress()) {
									update = find = true;
									dbs[2] = String.valueOf(Long.valueOf(dbs[2]) + item.getCount());
									db = "";
									for (String d : dbs)
										db += d + ",";
								}
							}
							sb.append(db).append("\r\n");
						}
						weapon = sb.toString();
					}
					if (!find)
						weapon += item.toStringSaveDB();
					//
					st = con.prepareStatement("UPDATE characters_inventory SET weapon=? WHERE cha_objId=?");
					st.setString(1, weapon);
					st.setLong(2, cha_objid);
				} else {
					st = con.prepareStatement(
							"INSERT INTO characters_inventory SET cha_objId=?, cha_name=?, weapon=?, armor='', etc=''");
					st.setLong(1, cha_objid);
					st.setString(2, cha_name);
					st.setString(3, item.toStringSaveDB());
				}
				st.executeUpdate();
			} else if (item.getItem().getType1().equalsIgnoreCase("armor")) {
				if (find) {
					find = false;
					//
					if (item.getItem().isPiles()) {
						StringBuffer sb = new StringBuffer();
						StringTokenizer token = new StringTokenizer(armor, "\r\n");
						while (token.hasMoreTokens()) {
							String db = token.nextToken();
							if (!update && db.indexOf("," + item.getItem().getName() + ",") >= 0) {
								String[] dbs = db.split(",");
								if (Integer.valueOf(dbs[7]) == item.getBress()) {
									update = find = true;
									dbs[2] = String.valueOf(Long.valueOf(dbs[2]) + item.getCount());
									db = "";
									for (String d : dbs)
										db += d + ",";
								}
							}
							sb.append(db).append("\r\n");
						}
						armor = sb.toString();
					}
					if (!find)
						armor += item.toStringSaveDB();
					//
					st = con.prepareStatement("UPDATE characters_inventory SET armor=? WHERE cha_objId=?");
					st.setString(1, armor);
					st.setLong(2, cha_objid);
				} else {
					st = con.prepareStatement(
							"INSERT INTO characters_inventory SET cha_objId=?, cha_name=?, armor=?, weapon='', etc=''");
					st.setLong(1, cha_objid);
					st.setString(2, cha_name);
					st.setString(3, item.toStringSaveDB());
				}
				st.executeUpdate();
			} else {
				if (find) {
					find = false;
					//
					if (item.getItem().isPiles()) {
						StringBuffer sb = new StringBuffer();
						StringTokenizer token = new StringTokenizer(etc, "\r\n");
						while (token.hasMoreTokens()) {
							String db = token.nextToken();
							if (!update && db.indexOf("," + item.getItem().getName() + ",") >= 0) {
								String[] dbs = db.split(",");
								if (Integer.valueOf(dbs[7]) == item.getBress()) {
									update = find = true;
									dbs[2] = String.valueOf(Long.valueOf(dbs[2]) + item.getCount());
									db = "";
									for (String d : dbs)
										db += d + ",";
								}
							}
							sb.append(db).append("\r\n");
						}
						etc = sb.toString();
					}
					if (!find)
						etc += item.toStringSaveDB();
					//
					st = con.prepareStatement("UPDATE characters_inventory SET etc=? WHERE cha_objId=?");
					st.setString(1, etc);
					st.setLong(2, cha_objid);
				} else {
					st = con.prepareStatement(
							"INSERT INTO characters_inventory SET cha_objId=?, cha_name=?, etc=?, weapon='', armor=''");
					st.setLong(1, cha_objid);
					st.setString(2, cha_name);
					st.setString(3, item.toStringSaveDB());
				}
				st.executeUpdate();
			}
			//

		} catch (Exception e) {
			lineage.share.System.printf("%s : appendCharacterInventoryItem()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	static public void updateCharacterInventoryItem(long cha_objId, long item_objId, long item_count, int item_bress) {
		if (cha_objId == 0)
			return;

		//
		String weapon = null;
		String armor = null;
		String etc = null;

		// memory
		Map<String, Object> dbss = null;
		synchronized (inventory) {
			dbss = inventory.get(cha_objId);
			if (dbss != null) {
				weapon = (String) (dbss.get("weapon") == null ? "" : dbss.get("weapon"));
				armor = (String) (dbss.get("armor") == null ? "" : dbss.get("armor"));
				etc = (String) (dbss.get("etc") == null ? "" : dbss.get("etc"));
			}
		}

		// db
		if (weapon == null) {
			Connection con = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				con = DatabaseConnection.getLineage();
				// 찾기
				st = con.prepareStatement(
						"SELECT * FROM characters_inventory WHERE cha_objId=? AND (weapon LIKE ? OR armor LIKE ? OR etc LIKE ?)");
				st.setLong(1, cha_objId);
				st.setString(2, String.format("%%%d,%%", item_objId));
				st.setString(3, String.format("%%%d,%%", item_objId));
				st.setString(4, String.format("%%%d,%%", item_objId));
				rs = st.executeQuery();
				if (rs.next()) {
					weapon = rs.getString("weapon");
					armor = rs.getString("armor");
					etc = rs.getString("etc");
				}
			} catch (Exception e) {
				lineage.share.System.printf(
						"%s : updateCharacterInventoryItem(long cha_objId, long item_objId, long item_count, int item_bress)\r\n",
						CharactersDatabase.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st, rs);
			}
		}

		// 수정
		if (weapon != null) {
			if (weapon.indexOf(String.valueOf(item_objId)) >= 0) {
				StringBuffer sb = new StringBuffer();
				StringTokenizer token = new StringTokenizer(weapon, "\r\n");
				while (token.hasMoreTokens()) {
					String db = token.nextToken();
					if (db.indexOf(item_objId + ",") >= 0) {
						String[] dbs = db.split(",");
						if (Integer.valueOf(dbs[7]) == item_bress) {
							if (item_count <= 0)
								continue;
							dbs[2] = String.valueOf(item_count);
							db = "";
							for (String d : dbs)
								db += d + ",";
						}
					}
					sb.append(db).append("\r\n");
				}
				weapon = sb.toString();
			}
			if (armor.indexOf(String.valueOf(item_objId)) >= 0) {
				StringBuffer sb = new StringBuffer();
				StringTokenizer token = new StringTokenizer(armor, "\r\n");
				while (token.hasMoreTokens()) {
					String db = token.nextToken();
					if (db.indexOf(item_objId + ",") >= 0) {
						String[] dbs = db.split(",");
						if (Integer.valueOf(dbs[7]) == item_bress) {
							if (item_count <= 0)
								continue;
							dbs[2] = String.valueOf(item_count);
							db = "";
							for (String d : dbs)
								db += d + ",";
						}
					}
					sb.append(db).append("\r\n");
				}
				armor = sb.toString();
			}
			if (etc.indexOf(String.valueOf(item_objId)) >= 0) {
				StringBuffer sb = new StringBuffer();
				StringTokenizer token = new StringTokenizer(etc, "\r\n");
				while (token.hasMoreTokens()) {
					String db = token.nextToken();
					if (db.indexOf(item_objId + ",") >= 0) {
						String[] dbs = db.split(",");
						if (Integer.valueOf(dbs[7]) == item_bress) {
							if (item_count <= 0)
								continue;
							dbs[2] = String.valueOf(item_count);
							db = "";
							for (String d : dbs)
								db += d + ",";
						}
					}
					sb.append(db).append("\r\n");
				}
				etc = sb.toString();
			}
		}

		// 갱신
		if (weapon != null) {
			if (dbss != null) {
				dbss.put("weapon", weapon);
				dbss.put("armor", armor);
				dbss.put("etc", etc);
				synchronized (inventory) {
					inventory.put(cha_objId, dbss);
				}
			} else {
				Connection con = null;
				PreparedStatement st = null;
				ResultSet rs = null;
				try {
					con = DatabaseConnection.getLineage();
					st = con.prepareStatement(
							"UPDATE characters_inventory SET weapon=?, armor=?, etc=? WHERE cha_objId=?");
					st.setString(1, weapon);
					st.setString(2, armor);
					st.setString(3, etc);
					st.setLong(4, cha_objId);
					st.executeUpdate();
				} catch (Exception e) {
					lineage.share.System.printf("%s : removeCharacterInventory(long cha_objId, long item_objId) 2\r\n",
							CharactersDatabase.class.toString());
					lineage.share.System.println(e);
				} finally {
					DatabaseConnection.close(con, st, rs);
				}
			}
		}
	}

	static public long findCharacterInventoryItem(long cha_objid, long item_objid) {
		if (cha_objid == 0)
			return 0;

		long find_objid = 0;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			boolean find = false;
			String weapon = null;
			String armor = null;
			String etc = null;
			// 찾기
			st = con.prepareStatement("SELECT * FROM characters_inventory WHERE cha_objId=?");
			st.setLong(1, cha_objid);
			rs = st.executeQuery();
			if (rs.next()) {
				find = true;
				weapon = rs.getString("weapon");
				armor = rs.getString("armor");
				etc = rs.getString("etc");
			}
			DatabaseConnection.close(st, rs);
			if (!find)
				return 0;
			//
			StringTokenizer token = new StringTokenizer(weapon, "\r\n");
			while (token.hasMoreTokens()) {
				String db = token.nextToken();
				String[] dbs = db.split(",");
				if (Integer.valueOf(dbs[0]) == item_objid)
					find_objid = Long.valueOf(dbs[0]);
			}
			//
			token = new StringTokenizer(armor, "\r\n");
			while (token.hasMoreTokens()) {
				String db = token.nextToken();
				String[] dbs = db.split(",");
				if (Integer.valueOf(dbs[0]) == item_objid)
					find_objid = Long.valueOf(dbs[0]);
			}
			//
			token = new StringTokenizer(etc, "\r\n");
			while (token.hasMoreTokens()) {
				String db = token.nextToken();
				String[] dbs = db.split(",");
				if (Integer.valueOf(dbs[0]) == item_objid)
					find_objid = Long.valueOf(dbs[0]);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : findCharacterInventoryItem(long cha_objid, long item_objid)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return find_objid;
	}

	static public long findCharacterInventoryItem(long cha_objid, String item_name, int item_bress) {
		if (cha_objid == 0)
			return 0;

		long item_objid = 0;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			boolean find = false;
			String weapon = null;
			String armor = null;
			String etc = null;
			// 찾기
			st = con.prepareStatement("SELECT * FROM characters_inventory WHERE cha_objId=?");
			st.setLong(1, cha_objid);
			rs = st.executeQuery();
			if (rs.next()) {
				find = true;
				weapon = rs.getString("weapon");
				armor = rs.getString("armor");
				etc = rs.getString("etc");
			}
			DatabaseConnection.close(st, rs);
			if (!find)
				return 0;
			//
			StringTokenizer token = new StringTokenizer(weapon, "\r\n");
			while (token.hasMoreTokens()) {
				String db = token.nextToken();
				if (db.indexOf("," + item_name + ",") >= 0) {
					String[] dbs = db.split(",");
					if (Integer.valueOf(dbs[7]) == item_bress)
						item_objid = Long.valueOf(dbs[0]);
				}
			}
			//
			token = new StringTokenizer(armor, "\r\n");
			while (token.hasMoreTokens()) {
				String db = token.nextToken();
				if (db.indexOf("," + item_name + ",") >= 0) {
					String[] dbs = db.split(",");
					if (Integer.valueOf(dbs[7]) == item_bress)
						item_objid = Long.valueOf(dbs[0]);
				}
			}
			//
			token = new StringTokenizer(etc, "\r\n");
			while (token.hasMoreTokens()) {
				String db = token.nextToken();
				if (db.indexOf("," + item_name + ",") >= 0) {
					String[] dbs = db.split(",");
					if (Integer.valueOf(dbs[7]) == item_bress)
						item_objid = Long.valueOf(dbs[0]);
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf(
					"%s : findCharacterInventoryItem(long cha_objid, String item_name, int item_bress)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return item_objid;
	}

	static public int getCharacterCount(Connection con, int account_uid) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT COUNT(*) as cnt FROM characters WHERE account_uid=?");
			st.setInt(1, account_uid);
			rs = st.executeQuery();
			if (rs.next())
				return rs.getInt("cnt");
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCharacterCount(Connection con, int account_uid)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		return 0;
	}

	/**
	 * 디비에서 원하는 인첸트아이템에 전체 갯수를 리턴함.
	 * 
	 * @param en
	 * @param isWeapon
	 * @return
	 */
	static public int getInventoryEnchantCount(int en, boolean isWeapon) {
		int cnt = 0;
		// Connection con = null;
		// PreparedStatement st = null;
		// ResultSet rs = null;
		// try {
		// con = DatabaseConnection.getLineage();
		// //
		// List<Long> temp = new ArrayList<Long>();
		// synchronized (inventory) {
		// for(long key : inventory.keySet()) {
		// Map<String, Object> db = inventory.get(key);
		// if(db == null)
		// continue;
		//
		// StringTokenizer token = null;
		// if(isWeapon && db.get("weapon")!=null)
		// token = new StringTokenizer((String)db.get("weapon"), "\r\n");
		// if(!isWeapon && db.get("armor")!=null)
		// token = new StringTokenizer((String)db.get("armor"), "\r\n");
		// if(token != null) {
		// temp.add(key);
		// while(token.hasMoreTokens()) {
		// String data = token.nextToken();
		// try {
		// String[] dbs = data.split(",");
		// if(Integer.valueOf(dbs[4]) == en)
		// cnt += 1;
		// } catch (Exception e) { }
		// }
		// }
		// }
		// }
		// st = con.prepareStatement("SELECT * FROM characters_inventory");
		// rs = st.executeQuery();
		// while(rs.next()) {
		// if(temp.contains(rs.getLong("cha_objId")))
		// continue;
		//
		// StringTokenizer token = null;
		// if(isWeapon && rs.getString("weapon")!=null)
		// token = new StringTokenizer(rs.getString("weapon"), "\r\n");
		// if(!isWeapon && rs.getString("armor")!=null)
		// token = new StringTokenizer(rs.getString("armor"), "\r\n");
		// if(token != null) {
		// while(token.hasMoreTokens()) {
		// String data = token.nextToken();
		// try {
		// String[] dbs = data.split(",");
		// if(Integer.valueOf(dbs[4]) == en)
		// cnt += 1;
		// } catch (Exception e) { }
		// }
		// }
		// }
		// DatabaseConnection.close(st, rs);
		// temp.clear();
		// //
		// st =
		// con.prepareStatement("SELECT COUNT(*) as cnt FROM warehouse WHERE type=? AND
		// en=?");
		// st.setString(1, isWeapon ? "1" : "2");
		// st.setInt(2, en);
		// rs = st.executeQuery();
		// if(rs.next())
		// cnt += rs.getInt("cnt");
		// DatabaseConnection.close(st, rs);
		// //
		// st =
		// con.prepareStatement("SELECT COUNT(*) as cnt FROM warehouse_clan WHERE type=?
		// AND en=?");
		// st.setString(1, isWeapon ? "1" : "2");
		// st.setInt(2, en);
		// rs = st.executeQuery();
		// if(rs.next())
		// cnt += rs.getInt("cnt");
		// DatabaseConnection.close(st, rs);
		// //
		// st =
		// con.prepareStatement("SELECT COUNT(*) as cnt FROM warehouse_elf WHERE type=?
		// AND en=?");
		// st.setString(1, isWeapon ? "1" : "2");
		// st.setInt(2, en);
		// rs = st.executeQuery();
		// if(rs.next())
		// cnt += rs.getInt("cnt");
		// DatabaseConnection.close(st, rs);
		// } catch (Exception e) {
		// lineage.share.System.printf("%s : getInventoryEnchantCount(int en, boolean
		// isWeapon)\r\n",
		// CharactersDatabase.class.toString());
		// lineage.share.System.println(e);
		// } finally {
		// DatabaseConnection.close(con, st, rs);
		// }
		return cnt;
	}

	/**
	 * 홈페이지 연동 부분에서 사용함. : 글 작성시 계정과 연결된 케릭을 선택하기위해 : 케릭 목록을 리턴함.
	 * 
	 * @param account
	 * @return
	 */
	static public List<String> getCharacters(String account) {
		List<String> list = new ArrayList<String>();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT name FROM characters WHERE account=?");
			st.setString(1, account);
			rs = st.executeQuery();
			while (rs.next())
				list.add(rs.getString("name"));
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCharacters(String account)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return list;
	}
	
	/**
	 * 홈페이지 연동 부분에서 사용함. : BoardController.java 쪽에서 글작성한 사용자에 클레스를 확인하기 위해. : 클레스가
	 * 군주인지, 기사인지 확인하며 성별도 함께 확인해야함. : 리턴형 값에 의해서 적절한 이미지를 처리함.
	 * 
	 * @param name
	 * @return [클레스][성별]
	 */
	static public String getCharacterClass(String name) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT class, sex FROM characters WHERE name=?");
			st.setString(1, name);
			rs = st.executeQuery();
			if (rs.next())
				return String.format("%d%d", rs.getInt("class"), rs.getInt("sex"));
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCharacterClass(String name)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return "00";
	}

	/**
	 * 디비에있는 케릭터를 찾아서 좌표값을 추출.
	 * 
	 * @param con
	 * @param name
	 * @return
	 */
	static public int[] getCharacterLocation(Connection con, String name) {
		//
		synchronized (characters) {
			Map<String, Object> db = characters.get(name);
			if (db != null) {
				int[] loc = new int[3];
				loc[0] = (int) db.get("locX");
				loc[1] = (int) db.get("locY");
				loc[2] = (int) db.get("locMAP");
				return loc;
			}
		}
		//
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM characters WHERE name=?");
			st.setString(1, name);
			rs = st.executeQuery();
			if (rs.next()) {
				int[] loc = new int[3];
				loc[0] = rs.getInt("locX");
				loc[1] = rs.getInt("locY");
				loc[2] = rs.getInt("locMAP");
				return loc;
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCharacterCgetCharacterLocation(Connection con, String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		return null;
	}

	/**
	 * 디비에있는 케릭터를 찾아서 좌표 변경하는 함수.
	 * 
	 * @param con
	 * @param name
	 * @param x
	 * @param y
	 * @param map
	 */
	static public void updateLocation(Connection con, String name, int x, int y, int map) {

		Map<String, Object> db = characters.get(name);
		if (db != null) {
			db.put("locX", x);
			db.put("locY", y);
			db.put("locMAP", map);
			return;

		}
		//
		boolean init = false;
		PreparedStatement st = null;
		try {
			if (con == null) {
				init = true;
				con = DatabaseConnection.getLineage();
			}
			st = con.prepareStatement("UPDATE characters SET locX=?, locY=?, locMAP=? WHERE name=?");
			st.setInt(1, x);
			st.setInt(2, y);
			st.setInt(3, map);
			st.setString(4, name.toLowerCase());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateLocation(Connection con, String name, int x, int y, int map)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
			if (init)
				DatabaseConnection.close(con);
		}
	}

	/**
	 * 디비에있는 케릭터 이름 목록 만들어서 리턴함.
	 * 
	 * @param con
	 * @param r_list
	 */
	static public void getNameAllList(Connection con, List<String> r_list) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM characters ORDER BY name");
			rs = st.executeQuery();
			while (rs.next())
				r_list.add(rs.getString("name"));
		} catch (Exception e) {
			lineage.share.System.printf("%s : getNameAllList(Connection con, List<String> r_list)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
	}

	/**
	 * 경매처리구간에서 Agit객체에 사용자 정보를 등록해야하는 일이 있음.<br/>
	 * 그 구간처리를 여기서 맡음.
	 * 
	 * @param con
	 * @param name
	 * @param agit
	 */
	static public void updateAgit(Connection con, String name, Agit agit) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM characters WHERE LOWER(name)=?");
			st.setString(1, name.toLowerCase());
			rs = st.executeQuery();
			if (rs.next()) {
				agit.setClanId(rs.getInt("clanID"));
				agit.setClanName(rs.getString("clanNAME"));
				agit.setChaName(rs.getString("name"));
				agit.setChaObjectId(rs.getInt("objID"));
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateAgit(Connection con, String name, Agit agit)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
	}
	
	static public void updateGiranDungeonTime() {
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET giran_dungeon_time=?");
			st.setInt(1, Lineage.giran_dungeon_time);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateGiranDungeonTime()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	static public void updateGiranDungeonScrollCount() {
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET giran_dungeon_count=0");
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateGiranDungeonScrollCount()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param name
	 * @return
	 */
	static public long getCharacterObjectId(Connection con, String name) {
		boolean init = false;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			if (con == null) {
				init = true;
				con = DatabaseConnection.getLineage();
			}
			st = con.prepareStatement("SELECT objID FROM characters WHERE LOWER(name)=?");
			st.setString(1, name.toLowerCase());
			rs = st.executeQuery();
			if (rs.next())
				return rs.getLong("objID");
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCharacterObjectId(Connection con, String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
			if (init)
				DatabaseConnection.close(con);
		}
		return 0;
	}

	/**
	 * 
	 * @param con
	 * @param name
	 * @return
	 */
	static public long getCharacterRegisterDate(Connection con, String name) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT register_date FROM characters WHERE LOWER(name)=?");
			st.setString(1, name.toLowerCase());
			rs = st.executeQuery();
			if (rs.next())
				return rs.getLong("register_date");
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCharacterRegisterDate(Connection con, String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		return 0;
	}

	/**
	 * 
	 * @param con
	 * @param name
	 * @return
	 */
	static public int getCharacterLevel(Connection con, String name) {
		boolean init = false;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			if (con == null) {
				init = true;
				con = DatabaseConnection.getLineage();
			}
			st = con.prepareStatement("SELECT level FROM characters WHERE LOWER(name)=?");
			st.setString(1, name.toLowerCase());
			rs = st.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCharacterLevel(Connection con, String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
			if (init)
				DatabaseConnection.close(con);
		}
		return 0;
	}

	static public String getSkillString(long objid) {
		StringBuffer sb = new StringBuffer();
		List<Map<String, Object>> db = null;
		synchronized (skill) {
			db = skill.get((Object) objid);
		}

		if (db != null) {
			for (Map<String, Object> data : db) {
				try {
					Skill s = SkillDatabase.find((long) data.get("skill"));
					if (s != null) {
						sb.append(s.getName() + ",");
					}
				} catch (Exception e) {
				}
			}

			db.clear();
			return sb.toString();
		}

		// 메모리에서 못찾앗으면 디비에서 찾기.
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters_skill WHERE cha_objId=?");
			st.setLong(1, objid);
			rs = st.executeQuery();
			while (rs.next()) {
				Skill s = SkillDatabase.find(rs.getInt("skill"));
				if (s != null) {
					sb.append(s.getName() + ",");
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : readSkill(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}

		return sb.toString();
	}

	/**
	 * 디비에서 이름에 해당하는 케릭터의 혈맹아이디 찾아서 리턴.
	 * 
	 * @param con
	 * @param name
	 * @return
	 */
	static public int getCharacterClanId(Connection con, String name) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT clanID FROM characters WHERE LOWER(name)=?");
			st.setString(1, name.toLowerCase());
			rs = st.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCharacterClanId(Connection con, String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		return 0;
	}

	/**
	 * 디비에서 이름에 해당하는 케릭터의 클레스 종류 리턴.
	 * 
	 * @param con
	 * @param name
	 * @return
	 */
	static public int getCharacterClass(Connection con, String name) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT class FROM characters WHERE LOWER(name)=?");
			st.setString(1, name.toLowerCase());
			rs = st.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCharacterClass(Connection con, String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		return 0;
	}

	/**
	 * 케릭터 테이블에서 해당하는 이름이 존재하는지 확인하는 함수.
	 * 
	 * @param con
	 * @param name
	 * @return
	 */
	static public boolean isCharacterName(Connection con, String name) {
		//
		synchronized (characters) {
			for (String key : characters.keySet()) {
				if (name.equalsIgnoreCase(key))
					return true;
			}
		}
		//
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT name FROM characters WHERE LOWER(name)=?");
			st.setString(1, name.toLowerCase());
			rs = st.executeQuery();
			return rs.next();
		} catch (Exception e) {
			lineage.share.System.printf("%s : isCharacterName(Connection con, String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		return false;
	}

	/**
	 * 생성할수 없는 이름 목록에 확인하려는 이름이 존재하는지 체크하는 메서드.
	 * 
	 * @param con
	 * @param name
	 * @return
	 */

	static public boolean isInvalidName(Connection con, String name) {
		// 이름 검수.
		String cho = "슷숫믓뭇섭QWERTYUIIOPASDFGHJKLZXCVBNMqwertyuiiopasdfghjklzxcvbnm먹튀ㅂㅈㄷㄱㅅㅁㄴㅇㄹㅎㅋㅌㅊㅍㅛㅕㅑㅐㅔㅗㅓㅏㅣㅠㅜㅡㅄㄳㄻㄿㄼㄺㄽㅀ!@#$%^&*()~_-+=|\\<>,.?/[]{};:'\"`";
		for (int i = 0; i < cho.length(); ++i) {
			char comVal = cho.charAt(i);
			for (int j = 0; j < name.length(); ++j) {
				if (comVal == name.charAt(j))
					// 생성불가능한 이름이 존재하므로 true
					return true;
			}
		}
		// 디비에서 확인.
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT name FROM bad_name WHERE LOWER(name)=?");
			st.setString(1, name.toLowerCase());
			rs = st.executeQuery();
			return rs.next();
		} catch (Exception e) {
			lineage.share.System.printf("%s : isInvalidName(Connection con, String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		return false;
	}

	/**
	 * 케릭터 디비에 해당 오브젝트가 존재하는지 확인해주는 함수.
	 * 
	 * @param con
	 * @param obj_id
	 * @return
	 */
	static public boolean isCharacterObjectId(Connection con, long obj_id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM characters WHERE objID=?");
			st.setLong(1, obj_id);
			rs = st.executeQuery();
			return rs.next();
		} catch (Exception e) {
			lineage.share.System.printf("%s : isCharacterObjectId(Connection con, long obj_id)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		return false;
	}

	/**
	 * 케릭터 정보 등록 처리 함수.
	 * 
	 * @param con
	 * @param obj_id
	 * @param name
	 * @param type
	 * @param sex
	 * @param hp
	 * @param mp
	 * @param Str
	 * @param Dex
	 * @param Con
	 * @param Wis
	 * @param Cha
	 * @param Int
	 * @param gfx
	 * @param x
	 * @param y
	 * @param map
	 * @param account_id
	 * @param account_uid
	 */
	static public void insertCharacter(Connection con, long obj_id, String name, int type, int sex, int hp, int mp,
			int Str, int Dex, int Con, int Wis, int Cha, int Int, int gfx, FirstSpawn fs, String account_id,
			int account_uid, long time) {
		PreparedStatement st = null;
		try {
			// characters
			st = con.prepareStatement(
					"INSERT INTO characters SET name=?, account=?, account_uid=?, objID=?, nowHP=?, maxHP=?, nowMP=?, maxMP=?, str=?, dex=?, con=?, wis=?, inter=?, cha=?, sex=?, class=?, locX=?, locY=?, locMAP=?, gfx=?, register_date=?, food=?, save_interface='', block_name='',Giran_Quest=?,Talk_Quest=?");
			st.setString(1, name);
			st.setString(2, account_id);
			st.setInt(3, account_uid);
			st.setLong(4, obj_id);
			st.setInt(5, hp);
			st.setInt(6, hp);
			st.setInt(7, mp);
			st.setInt(8, mp);
			st.setInt(9, Str);
			st.setInt(10, Dex);
			st.setInt(11, Con);
			st.setInt(12, Wis);
			st.setInt(13, Int);
			st.setInt(14, Cha);
			st.setInt(15, sex);
			st.setInt(16, type);
			if (fs == null) {
				st.setInt(17, 32576);
				st.setInt(18, 32926);
				st.setInt(19, 0);
			} else {
				st.setInt(17, fs.getX());
				st.setInt(18, fs.getY());
				st.setInt(19, fs.getMap());
			}
			st.setInt(20, gfx);
			st.setLong(21, time);
			st.setInt(22, Lineage.server_version > 230 ? 40 : 65);
			st.setTimestamp(23, null);
			st.setTimestamp(24, null);
			st.executeUpdate();
			st.close();
			// characters_buff
			st = con.prepareStatement("INSERT INTO characters_buff SET name=?, objID=?");
			st.setString(1, name);
			st.setLong(2, obj_id);
			st.executeUpdate();
			st.close();
		} catch (Exception e) {
			lineage.share.System.printf(
					"%s : insertCharacter(Connection con, long obj_id, String name, int type, int sex, int hp, int mp, int Str, int Dex, int Con, int Wis, int Cha, int Int, int gfx, FirstSpawn fs, String account_id, int account_uid, long time)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}

	public static void saveCharacterStatus(PcInstance pc) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			StringBuffer localStringBuffer = new StringBuffer();
			if (pc.getDbInterface() != null)
				for (int k : pc.getDbInterface())
					localStringBuffer.append(String.format("%02x", Integer.valueOf(k & 0xFF)));
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters SET str=?, dex=?, con=?, wis=?, inter=?, cha=? WHERE objID=?");
			st.setInt(1, pc.getStr());
			st.setInt(2, pc.getDex());
			st.setInt(3, pc.getCon());
			st.setInt(4, pc.getWis());
			st.setInt(5, pc.getInt());
			st.setInt(6, pc.getCha());
			st.setLong(7, pc.getObjectId());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : saveCharacterStatus(PcInstance pc)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	/**
	 * 초기 지급 아이템 처리 함수.
	 * 
	 * @param con
	 * @param obj_id
	 * @param name
	 * @param type
	 */
	static public void insertInventory(Connection con, long obj_id, String name, int type) {
		PreparedStatement st = null;
		try {
			List<FirstInventory> list = null;
			switch (type) {
			case 0x00:
				list = Lineage.royal_first_inventory;
				break;
			case 0x01:
				list = Lineage.knight_first_inventory;
				break;
			case 0x02:
				list = Lineage.elf_first_inventory;
				break;
			case 0x03:
				list = Lineage.wizard_first_inventory;
				break;
			case 0x04:
				list = Lineage.darkelf_first_inventory;
				break;
			case 0x05:
				list = Lineage.dragonknight_first_inventory;
				break;
			case 0x06:
				list = Lineage.blackwizard_first_inventory;
				break;
			case 0x07:
				list = Lineage.warrior_first_inventory;
				break;
			}
			//
			if (list != null) {
				StringBuffer weapon = new StringBuffer();
				StringBuffer armor = new StringBuffer();
				StringBuffer etc = new StringBuffer();
				for (FirstInventory fi : list) {
					Item i = ItemDatabase.find(fi.getName());
					if (i != null) {
						for (int j = i.isPiles() ? 1 : fi.getCount(); j > 0; --j) {
							//
							String db = String.format("%d,%s,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%s,%s,%s,%s,\r\n",
									ServerDatabase.nextItemObjId(), i.getName(), i.isPiles() ? fi.getCount() : 1, 0, 0,
									Lineage.server_version >= 360 && (i.getType1().equalsIgnoreCase("weapon")
											|| i.getType1().equalsIgnoreCase("armor")) ? 1 : 0,
									Lineage.server_version >= 360 ? 1 : 0, 1, 0,
									i.getNameIdNumber() == 67 || i.getNameIdNumber() == 2 || i.getNameIdNumber() == 326
											|| i.getNameIdNumber() == 14220326 ? 600 : 0,
									0, 0, 0, "", i.getType1(), i.getType2(), "");
							//
							if (i.getType1().equalsIgnoreCase("weapon")) {
								weapon.append(db);
							} else if (i.getType1().equalsIgnoreCase("armor")) {
								armor.append(db);
							} else {
								etc.append(db);
							}
						}
					}
				}
				//
				st = con.prepareStatement(
						"INSERT INTO characters_inventory SET cha_objId=?, cha_name=?, weapon=?, armor=?, etc=?");
				st.setLong(1, obj_id);
				st.setString(2, name);
				st.setString(3, weapon.toString());
				st.setString(4, armor.toString());
				st.setString(5, etc.toString());
				st.executeUpdate();
				st.close();
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : insertInventory(Connection con, long obj_id, String name, int type)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}

	static public void insertSkill(Connection con, long obj_id, String name, int type) {
		PreparedStatement st = null;
		try {
			List<FirstSpell> spell = null;
			switch (type) {
			case 0x00:
				spell = Lineage.royal_first_spell;
				break;
			case 0x01:
				spell = Lineage.knight_first_spell;
				break;
			case 0x02:
				spell = Lineage.elf_first_spell;
				break;
			case 0x03:
				spell = Lineage.wizard_first_spell;
				break;
			case 0x04:
				spell = Lineage.darkelf_first_spell;
				break;
			case 0x05:
				spell = Lineage.dragonknight_first_spell;
				break;
			case 0x06:
				spell = Lineage.blackwizard_first_spell;
				break;
			case 0x07:
				spell = Lineage.warrior_first_spell;
				break;
			}
			if (spell != null) {
				for (FirstSpell fs : spell) {
					st = con.prepareStatement("INSERT INTO characters_skill SET cha_objId=?, cha_name=?, skill=?");
					st.setLong(1, obj_id);
					st.setString(2, name);
					st.setInt(3, fs.getSpellUid());
					st.executeUpdate();
					st.close();
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : insertSkill(Connection con, long obj_id, String name, int type)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}

	/**
	 * uid와 연결된 케릭터테이블에 해당하는 이름이 존재하는지 확인.
	 * 
	 * @param con
	 * @param account_uid
	 * @param name
	 * @return
	 */
	static public boolean isCharacter(int account_uid, String name) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement(
					"SELECT * FROM characters WHERE account_uid=? AND LOWER(name)=? AND block_date='0000-00-00 00:00:00'");
			st.setInt(1, account_uid);
			st.setString(2, name.toLowerCase());
			rs = st.executeQuery();
			return rs.next();
		} catch (Exception e) {
			lineage.share.System.printf("%s : isCharacter(int account_uid, String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return false;
	}

	/**
	 * 해당하는 케릭터가 월드에 접속한 시간 갱신해주는 함수
	 * 
	 * @param con
	 * @param name
	 */
	static public void updateCharacterJoinTimeStamp(String name) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters SET join_date=? WHERE LOWER(name)=?");
			st.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			st.setString(2, name.toLowerCase());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateCharacterJoinTimeStamp(String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	/**
	 * 해당 케릭터 차단날자 갱신하는 함수.
	 * 
	 * @param name
	 */
	static public void updateCharacterBlockTimeStamp(String name) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters SET block_date=? WHERE LOWER(name)=?");
			st.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			st.setString(2, name.toLowerCase());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateCharacterJoinTimeStamp(String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	/**
	 * 케릭터 성별 변경하는 메서드.
	 * 
	 * @param name
	 * @param sex
	 */
	static public void updateCharacterSex(String name, int sex) {
		//
		Map<String, Object> db = null;
		synchronized (characters) {
			db = characters.remove(sex);
		}
		if (db != null) {
			db.put("sex", sex);
		}
		//
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters SET sex=? WHERE LOWER(name)=?");
			st.setInt(1, sex);
			st.setString(2, name.toLowerCase());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateCharacterSex(String name, int sex)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	/**
	 * 케릭명 변경처리하는 메서드.
	 * 
	 * @param name
	 * @param newName
	 */
	static public void updateCharacterName(String name, String newName) {
		//
		Map<String, Object> db = null;
		synchronized (characters) {
			db = characters.remove(name);
		}
		if (db != null) {
			db.put("name", newName);
			synchronized (characters) {
				characters.put(newName, db);
			}
		}
		//
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters SET name=? WHERE LOWER(name)=?");
			st.setString(1, newName);
			st.setString(2, name.toLowerCase());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateCharacterJoinTimeStamp(String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * name과 연결된 케릭터테이블에 정보 추출 pcinstance 객체 생성 리턴.
	 * 
	 * @param con
	 * @param c
	 * @param name
	 * @return
	 */
	static public PcInstance readCharacter(long objid) {

		PcInstance pc = null;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters WHERE Objid=?");
			st.setLong(1, objid);
			rs = st.executeQuery();
			if (rs.next()) {
				pc = new PcInstance();
				pc.setName(rs.getString("name"));
				pc.setObjectId(rs.getLong("objID"));
				pc.setLevel(rs.getInt("level"));
				pc.setMaxHp(rs.getInt("maxHP"));
				pc.setNowHp(rs.getInt("nowHP"));
				pc.setMaxMp(rs.getInt("maxMP"));
				pc.setNowMp(rs.getInt("nowMP"));
				pc.setStr(rs.getInt("str"));
				pc.setDex(rs.getInt("dex"));
				pc.setCon(rs.getInt("con"));
				pc.setWis(rs.getInt("wis"));
				pc.setInt(rs.getInt("inter"));
				pc.setCha(rs.getInt("cha"));
				pc.setClassSex(rs.getInt("sex"));
				pc.setClassType(rs.getInt("class"));
				pc.setExp(rs.getDouble("exp"));
				pc.setLostExp(rs.getDouble("lost_exp"));
				pc.setX(rs.getInt("locX"));
				pc.setY(rs.getInt("locY"));
				pc.setMap(rs.getInt("locMAP"));
				pc.setTitle(rs.getString("title"));
				pc.setFood(rs.getInt("food"));
				pc.setGfx(rs.getInt("gfx"));
				pc.setGfxMode(rs.getInt("gfxMode"));
				pc.setLawful(rs.getInt("lawful"));
				pc.setClanId(rs.getInt("clanID"));
				pc.setClanName(rs.getString("clanNAME"));
				pc.setPkCount(rs.getInt("pkcount"));
				try {
					pc.setPkTime(rs.getTimestamp("pkTime").getTime());
				} catch (Exception e) {
				}
				pc.setPkHellTime(rs.getInt("pkHellTime"));
				pc.setPkHellCount(rs.getInt("pkHellCount"));
				pc.setChattingGlobal(rs.getInt("global_chating") == 1);
				pc.setChattingTrade(rs.getInt("trade_chating") == 1);
				pc.setChattingWhisper(rs.getInt("whisper_chating") == 1);
				pc.setAttribute(rs.getInt("attribute"));
				pc.setLvStr(rs.getInt("lvStr"));
				pc.setLvCon(rs.getInt("lvCon"));
				pc.setLvDex(rs.getInt("lvDex"));
				pc.setLvWis(rs.getInt("lvWis"));
				pc.setLvInt(rs.getInt("lvInt"));
				pc.setLvCha(rs.getInt("lvCha"));
				pc.setElixirStr(rs.getInt("elixirStr"));
				pc.setElixirCon(rs.getInt("elixirCon"));
				pc.setElixirDex(rs.getInt("elixirDex"));
				pc.setElixirWis(rs.getInt("elixirWis"));
				pc.setElixirInt(rs.getInt("elixirInt"));
				pc.setElixirCha(rs.getInt("elixirCha"));
				pc.setElixirReset(rs.getInt("elixirReset"));
				pc.setSpeedHackWarningCounting(rs.getInt("speedhack_warning_count"));
				pc.setRegisterDate(rs.getLong("register_date"));
				pc.setMonsterKillCount(rs.getInt("monster_kill_count"));
				pc.setClanGrade(rs.getInt("clan_grade"));

				pc.setPay_info1(rs.getString("pay_info1"));
				pc.setPay_info2(rs.getString("pay_info2"));
				pc.setPay_info3(rs.getString("pay_info3"));
				pc.setAutoHuntMonsterCount(rs.getInt("moncount"));

				try {
					pc.setJoinDate(rs.getLong("join_date"));
				} catch (Exception e) {
				}
				if (rs.getString("save_interface").length() > 0)
					pc.setDbInterface(Util.StringToByte(rs.getString("save_interface")));
				if (rs.getString("block_name").length() > 0) {
					for (String n : rs.getString("block_name").split(","))
						pc.getListBlockName().add(n);
				}
				pc.setKarma(rs.getDouble("karma"));
				if (rs.getString("options").length() > 0) {
					try {
						StringTokenizer tok = new StringTokenizer(rs.getString("options"), "|");
						while (tok.hasMoreTokens()) {
							String[] t = tok.nextToken().split("=");
							if (t[0].equalsIgnoreCase("ac"))
								pc.setDynamicAc(pc.getDynamicAc() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("str"))
								pc.setDynamicStr(pc.getDynamicStr() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("dex"))
								pc.setDynamicDex(pc.getDynamicDex() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("con"))
								pc.setDynamicCon(pc.getDynamicCon() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("wis"))
								pc.setDynamicWis(pc.getDynamicWis() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("int"))
								pc.setDynamicInt(pc.getDynamicInt() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("cha"))
								pc.setDynamicCha(pc.getDynamicCha() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("hp"))
								pc.setDynamicHp(pc.getDynamicHp() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("mp"))
								pc.setDynamicMp(pc.getDynamicMp() + Integer.valueOf(t[1]));
						}
					} catch (Exception e) {
					}
				}

				switch (pc.getClassType()) {
				case 0x00:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.royal_male_gfx : Lineage.royal_female_gfx);
					break;
				case 0x01:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.knight_male_gfx : Lineage.knight_female_gfx);
					break;
				case 0x02:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.elf_male_gfx : Lineage.elf_female_gfx);
					break;
				case 0x03:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.wizard_male_gfx : Lineage.wizard_female_gfx);
					break;
				case 0x04:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.darkelf_male_gfx : Lineage.darkelf_female_gfx);
					break;
				case 0x05:
					pc.setClassGfx(
							pc.getClassSex() == 0 ? Lineage.dragonknight_male_gfx : Lineage.dragonknight_female_gfx);
					break;
				case 0x06:
					pc.setClassGfx(
							pc.getClassSex() == 0 ? Lineage.blackwizard_male_gfx : Lineage.blackwizard_female_gfx);
					break;
				case 0x07:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.warrior_male_gfx : Lineage.warrior_female_gfx);
					break;
				}
				
				pc.setTempName(rs.getString("temp_name"));
				pc.setTempClanName(rs.getString("temp_clan_name"));
				pc.setTempClanId(rs.getInt("temp_clan_id"));
				pc.setTempClanGrade(rs.getInt("temp_clan_grade"));
				pc.setTempTitle(rs.getString("temp_title"));
				pc.setQuestChapter(rs.getInt("questchapter"));
				pc.setQuestKill(rs.getInt("questkill"));
				pc.setRadomQuest(rs.getInt("radomquest"));
				pc.setRandomQuestkill(rs.getInt("randomquestkill"));
				pc.setRandomQuestCount(rs.getInt("RandomQuestCount"));
				pc.setRandomQuestPlay(rs.getInt("RandomQuestPlay"));
				pc.setFast_poly(rs.getString("quick_polymorph") == null ? "" : rs.getString("quick_polymorph"));
				pc.isAutoPotion = rs.getInt("is_auto_potion") == 1 ? true : false;
				pc.isAutoPotion2 = rs.getInt("is_auto_potion2") == 1 ? true : false;
				pc.setAutoPotionPercent(rs.getInt("auto_potion_percent"));
				pc.setAutoPotionName(rs.getString("auto_potion") == null ? "" : rs.getString("auto_potion"));
				
				PluginController.init(CharactersDatabase.class, "readCharacter", pc, rs);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : readCharacter(LineageClient c, String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return pc;
	}
	
	/**
	 * 케릭터 이름으로 정보 불러오기
	 */
	
	static public PcInstance readCharacter(String name) {

		PcInstance pc = null;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters WHERE name=?");
			st.setString(1, name);
			rs = st.executeQuery();
			if (rs.next()) {
				pc = new PcInstance();
				pc.setName(rs.getString("name"));
				pc.setObjectId(rs.getLong("objID"));
				pc.setLevel(rs.getInt("level"));
				pc.setMaxHp(rs.getInt("maxHP"));
				pc.setNowHp(rs.getInt("nowHP"));
				pc.setMaxMp(rs.getInt("maxMP"));
				pc.setNowMp(rs.getInt("nowMP"));
				pc.setStr(rs.getInt("str"));
				pc.setDex(rs.getInt("dex"));
				pc.setCon(rs.getInt("con"));
				pc.setWis(rs.getInt("wis"));
				pc.setInt(rs.getInt("inter"));
				pc.setCha(rs.getInt("cha"));
				pc.setClassSex(rs.getInt("sex"));
				pc.setClassType(rs.getInt("class"));
				pc.setExp(rs.getDouble("exp"));
				pc.setLostExp(rs.getDouble("lost_exp"));
				pc.setX(rs.getInt("locX"));
				pc.setY(rs.getInt("locY"));
				pc.setMap(rs.getInt("locMAP"));
				pc.setTitle(rs.getString("title"));
				pc.setFood(rs.getInt("food"));
				pc.setGfx(rs.getInt("gfx"));
				pc.setGfxMode(rs.getInt("gfxMode"));
				pc.setLawful(rs.getInt("lawful"));
				pc.setClanId(rs.getInt("clanID"));
				pc.setClanName(rs.getString("clanNAME"));
				pc.setPkCount(rs.getInt("pkcount"));
				try {
					pc.setPkTime(rs.getTimestamp("pkTime").getTime());
				} catch (Exception e) {
				}
				pc.setPkHellTime(rs.getInt("pkHellTime"));
				pc.setPkHellCount(rs.getInt("pkHellCount"));
				pc.setChattingGlobal(rs.getInt("global_chating") == 1);
				pc.setChattingTrade(rs.getInt("trade_chating") == 1);
				pc.setChattingWhisper(rs.getInt("whisper_chating") == 1);
				pc.setAttribute(rs.getInt("attribute"));
				pc.setLvStr(rs.getInt("lvStr"));
				pc.setLvCon(rs.getInt("lvCon"));
				pc.setLvDex(rs.getInt("lvDex"));
				pc.setLvWis(rs.getInt("lvWis"));
				pc.setLvInt(rs.getInt("lvInt"));
				pc.setLvCha(rs.getInt("lvCha"));
				pc.setElixirStr(rs.getInt("elixirStr"));
				pc.setElixirCon(rs.getInt("elixirCon"));
				pc.setElixirDex(rs.getInt("elixirDex"));
				pc.setElixirWis(rs.getInt("elixirWis"));
				pc.setElixirInt(rs.getInt("elixirInt"));
				pc.setElixirCha(rs.getInt("elixirCha"));
				pc.setElixirReset(rs.getInt("elixirReset"));
				pc.setSpeedHackWarningCounting(rs.getInt("speedhack_warning_count"));
				pc.setRegisterDate(rs.getLong("register_date"));
				pc.setMonsterKillCount(rs.getInt("monster_kill_count"));
				pc.setClanGrade(rs.getInt("clan_grade"));

				pc.setPay_info1(rs.getString("pay_info1"));
				pc.setPay_info2(rs.getString("pay_info2"));
				pc.setPay_info3(rs.getString("pay_info3"));
				pc.setAutoHuntMonsterCount(rs.getInt("moncount"));

				try {
					pc.setJoinDate(rs.getLong("join_date"));
				} catch (Exception e) {
				}
				if (rs.getString("save_interface").length() > 0)
					pc.setDbInterface(Util.StringToByte(rs.getString("save_interface")));
				if (rs.getString("block_name").length() > 0) {
					for (String n : rs.getString("block_name").split(","))
						pc.getListBlockName().add(n);
				}
				pc.setKarma(rs.getDouble("karma"));
				if (rs.getString("options").length() > 0) {
					try {
						StringTokenizer tok = new StringTokenizer(rs.getString("options"), "|");
						while (tok.hasMoreTokens()) {
							String[] t = tok.nextToken().split("=");
							if (t[0].equalsIgnoreCase("ac"))
								pc.setDynamicAc(pc.getDynamicAc() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("str"))
								pc.setDynamicStr(pc.getDynamicStr() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("dex"))
								pc.setDynamicDex(pc.getDynamicDex() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("con"))
								pc.setDynamicCon(pc.getDynamicCon() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("wis"))
								pc.setDynamicWis(pc.getDynamicWis() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("int"))
								pc.setDynamicInt(pc.getDynamicInt() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("cha"))
								pc.setDynamicCha(pc.getDynamicCha() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("hp"))
								pc.setDynamicHp(pc.getDynamicHp() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("mp"))
								pc.setDynamicMp(pc.getDynamicMp() + Integer.valueOf(t[1]));
						}
					} catch (Exception e) {
					}
				}

				switch (pc.getClassType()) {
				case 0x00:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.royal_male_gfx : Lineage.royal_female_gfx);
					break;
				case 0x01:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.knight_male_gfx : Lineage.knight_female_gfx);
					break;
				case 0x02:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.elf_male_gfx : Lineage.elf_female_gfx);
					break;
				case 0x03:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.wizard_male_gfx : Lineage.wizard_female_gfx);
					break;
				case 0x04:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.darkelf_male_gfx : Lineage.darkelf_female_gfx);
					break;
				case 0x05:
					pc.setClassGfx(
							pc.getClassSex() == 0 ? Lineage.dragonknight_male_gfx : Lineage.dragonknight_female_gfx);
					break;
				case 0x06:
					pc.setClassGfx(
							pc.getClassSex() == 0 ? Lineage.blackwizard_male_gfx : Lineage.blackwizard_female_gfx);
					break;
				case 0x07:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.warrior_male_gfx : Lineage.warrior_female_gfx);
					break;
				}
				
				pc.setTempName(rs.getString("temp_name"));
				pc.setTempClanName(rs.getString("temp_clan_name"));
				pc.setTempClanId(rs.getInt("temp_clan_id"));
				pc.setTempClanGrade(rs.getInt("temp_clan_grade"));
				pc.setTempTitle(rs.getString("temp_title"));
				pc.setQuestChapter(rs.getInt("questchapter"));
				pc.setQuestKill(rs.getInt("questkill"));
				pc.setRadomQuest(rs.getInt("radomquest"));
				pc.setRandomQuestkill(rs.getInt("randomquestkill"));
				pc.setRandomQuestCount(rs.getInt("RandomQuestCount"));
				pc.setRandomQuestPlay(rs.getInt("RandomQuestPlay"));
				pc.setFast_poly(rs.getString("quick_polymorph") == null ? "" : rs.getString("quick_polymorph"));
				pc.isAutoPotion = rs.getInt("is_auto_potion") == 1 ? true : false;
				pc.isAutoPotion2 = rs.getInt("is_auto_potion2") == 1 ? true : false;
				pc.setAutoPotionPercent(rs.getInt("auto_potion_percent"));
				pc.setAutoPotionName(rs.getString("auto_potion") == null ? "" : rs.getString("auto_potion"));
				
				PluginController.init(CharactersDatabase.class, "readCharacter", pc, rs);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : readCharacter(String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return pc;
	}

	/**
	 * name과 연결된 케릭터테이블에 정보 추출 pcinstance 객체 생성 리턴.
	 * 
	 * @param con
	 * @param c
	 * @param name
	 * @return
	 */
	static public PcInstance readCharacter(LineageClient c, String name) {
		// 메모리에서 못찾앗으면 디비에서 찾기.
		PcInstance pc = null;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters WHERE account_uid=? AND LOWER(name)=?");
			st.setInt(1, c.getAccountUid());
			st.setString(2, name.toLowerCase());
			rs = st.executeQuery();
			if (rs.next()) {
				pc = c.getPc();
				pc.setName(rs.getString("name"));
				pc.setObjectId(rs.getLong("objID"));
				pc.setLevel(rs.getInt("level"));
				pc.setMaxHp(rs.getInt("maxHP"));
				pc.setNowHp(rs.getInt("nowHP"));
				pc.setMaxMp(rs.getInt("maxMP"));
				pc.setNowMp(rs.getInt("nowMP"));
				pc.setStr(rs.getInt("str"));
				pc.setDex(rs.getInt("dex"));
				pc.setCon(rs.getInt("con"));
				pc.setWis(rs.getInt("wis"));
				pc.setInt(rs.getInt("inter"));
				pc.setCha(rs.getInt("cha"));
				pc.setClassSex(rs.getInt("sex"));
				pc.setClassType(rs.getInt("class"));
				pc.setExp(rs.getDouble("exp"));
				pc.setLostExp(rs.getDouble("lost_exp"));
				pc.setX(rs.getInt("locX"));
				pc.setY(rs.getInt("locY"));
				pc.setMap(rs.getInt("locMAP"));
				pc.setTitle(rs.getString("title"));
				pc.setFood(rs.getInt("food"));
				pc.setGfx(rs.getInt("gfx"));
				pc.setGfxMode(rs.getInt("gfxMode"));
				pc.setLawful(rs.getInt("lawful"));
				pc.setClanId(rs.getInt("clanID"));
				pc.setClanName(rs.getString("clanNAME"));
				pc.setPkCount(rs.getInt("pkcount"));
				try {
					pc.setPkTime(rs.getTimestamp("pkTime").getTime());
				} catch (Exception e) {
				}
				pc.setPkHellTime(rs.getInt("pkHellTime"));
				pc.setPkHellCount(rs.getInt("pkHellCount"));
				pc.setChattingGlobal(rs.getInt("global_chating") == 1);
				pc.setChattingTrade(rs.getInt("trade_chating") == 1);
				pc.setChattingWhisper(rs.getInt("whisper_chating") == 1);
				pc.setAttribute(rs.getInt("attribute"));
				pc.setLvStr(rs.getInt("lvStr"));
				pc.setLvCon(rs.getInt("lvCon"));
				pc.setLvDex(rs.getInt("lvDex"));
				pc.setLvWis(rs.getInt("lvWis"));
				pc.setLvInt(rs.getInt("lvInt"));
				pc.setLvCha(rs.getInt("lvCha"));
				pc.setElixirStr(rs.getInt("elixirStr"));
				pc.setElixirCon(rs.getInt("elixirCon"));
				pc.setElixirDex(rs.getInt("elixirDex"));
				pc.setElixirWis(rs.getInt("elixirWis"));
				pc.setElixirInt(rs.getInt("elixirInt"));
				pc.setElixirCha(rs.getInt("elixirCha"));
				pc.setElixirReset(rs.getInt("elixirReset"));
				pc.setSpeedHackWarningCounting(rs.getInt("speedhack_warning_count"));
				pc.setRegisterDate(rs.getLong("register_date"));
				pc.setMonsterKillCount(rs.getInt("monster_kill_count"));
				pc.setClanGrade(rs.getInt("clan_grade"));

				pc.setPay_info1(rs.getString("pay_info1"));
				pc.setPay_info2(rs.getString("pay_info2"));
				pc.setPay_info3(rs.getString("pay_info3"));
				pc.setAutoHuntMonsterCount(rs.getInt("moncount"));

				try {
					pc.setJoinDate(rs.getLong("join_date"));
				} catch (Exception e) {
				}
				if (rs.getString("save_interface").length() > 0)
					pc.setDbInterface(Util.StringToByte(rs.getString("save_interface")));
				if (rs.getString("block_name").length() > 0) {
					for (String n : rs.getString("block_name").split(","))
						pc.getListBlockName().add(n);
				}
				pc.setKarma(rs.getDouble("karma"));
				if (rs.getString("options").length() > 0) {
					try {
						StringTokenizer tok = new StringTokenizer(rs.getString("options"), "|");
						while (tok.hasMoreTokens()) {
							String[] t = tok.nextToken().split("=");
							if (t[0].equalsIgnoreCase("ac"))
								pc.setDynamicAc(pc.getDynamicAc() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("str"))
								pc.setDynamicStr(pc.getDynamicStr() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("dex"))
								pc.setDynamicDex(pc.getDynamicDex() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("con"))
								pc.setDynamicCon(pc.getDynamicCon() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("wis"))
								pc.setDynamicWis(pc.getDynamicWis() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("int"))
								pc.setDynamicInt(pc.getDynamicInt() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("cha"))
								pc.setDynamicCha(pc.getDynamicCha() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("hp"))
								pc.setDynamicHp(pc.getDynamicHp() + Integer.valueOf(t[1]));
							if (t[0].equalsIgnoreCase("mp"))
								pc.setDynamicMp(pc.getDynamicMp() + Integer.valueOf(t[1]));
						}
					} catch (Exception e) {
					}
				}

				switch (pc.getClassType()) {
				case 0x00:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.royal_male_gfx : Lineage.royal_female_gfx);
					break;
				case 0x01:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.knight_male_gfx : Lineage.knight_female_gfx);
					break;
				case 0x02:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.elf_male_gfx : Lineage.elf_female_gfx);
					break;
				case 0x03:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.wizard_male_gfx : Lineage.wizard_female_gfx);
					break;
				case 0x04:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.darkelf_male_gfx : Lineage.darkelf_female_gfx);
					break;
				case 0x05:
					pc.setClassGfx(
							pc.getClassSex() == 0 ? Lineage.dragonknight_male_gfx : Lineage.dragonknight_female_gfx);
					break;
				case 0x06:
					pc.setClassGfx(
							pc.getClassSex() == 0 ? Lineage.blackwizard_male_gfx : Lineage.blackwizard_female_gfx);
					break;
				case 0x07:
					pc.setClassGfx(pc.getClassSex() == 0 ? Lineage.warrior_male_gfx : Lineage.warrior_female_gfx);
					break;
				}
				
				pc.setTempName(rs.getString("temp_name"));
				pc.setTempClanName(rs.getString("temp_clan_name"));
				pc.setTempClanId(rs.getInt("temp_clan_id"));
				pc.setTempClanGrade(rs.getInt("temp_clan_grade"));
				pc.setTempTitle(rs.getString("temp_title"));
				pc.setQuestChapter(rs.getInt("questchapter"));
				pc.setQuestKill(rs.getInt("questkill"));
				pc.setRadomQuest(rs.getInt("radomquest"));
				pc.setRandomQuestkill(rs.getInt("randomquestkill"));
				pc.setRandomQuestCount(rs.getInt("RandomQuestCount"));
				pc.setRandomQuestPlay(rs.getInt("RandomQuestPlay"));
				pc.setFast_poly(rs.getString("quick_polymorph") == null ? "" : rs.getString("quick_polymorph"));
				pc.isAutoPotion = rs.getInt("is_auto_potion") == 1 ? true : false;
				pc.isAutoPotion2 = rs.getInt("is_auto_potion2") == 1 ? true : false;
				pc.setAutoPotionPercent(rs.getInt("auto_potion_percent"));
				pc.setAutoPotionName(rs.getString("auto_potion") == null ? "" : rs.getString("auto_potion"));
				
				PluginController.init(CharactersDatabase.class, "readCharacter", pc, rs);
				st.close();
				rs.close();
				
				st = con.prepareStatement("SELECT * FROM accounts WHERE uid=?");
				st.setInt(1, c.getAccountUid());
				rs = st.executeQuery();
				
				if (rs.next()) {
					pc.setDaycount(rs.getInt("daycount"));
					pc.setDaycheck(rs.getInt("daycheck"));
					pc.setDayptime(rs.getInt("daytime"));
					pc.setGiran_dungeon_time(rs.getInt("giran_dungeon_time"));
					pc.setGiran_dungeon_count(rs.getInt("giran_dungeon_count"));
				}	
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : readCharacter(LineageClient c, String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return pc;
	}

	/**
	 * 인벤토리 정보 추출.
	 * 
	 * @param con
	 * @param pc
	 */
	static public void readInventory(PcInstance pc) {
		Inventory inv = pc.getInventory();
		if (inv == null)
			return;

		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();

			// 성능향상을 위한 메모리 처리 부분.
			Map<String, Object> db = null;
			synchronized (inventory) {
				db = inventory.remove(pc.getObjectId());
			}
			if (db != null) {
				readInventory(pc, con, inv, new StringTokenizer((String) db.get("weapon"), "\r\n"));

				readInventory(pc, con, inv, new StringTokenizer((String) db.get("armor"), "\r\n"));

				readInventory(pc, con, inv, new StringTokenizer((String) db.get("etc"), "\r\n"));
			} else {
				// 메모리에서 못찾앗으면 디비에서 찾기.
				st = con.prepareStatement("SELECT * FROM characters_inventory WHERE cha_objId=?");
				st.setLong(1, pc.getObjectId());
				rs = st.executeQuery();
				if (rs.next()) {
					readInventory(pc, con, inv, new StringTokenizer(rs.getString("weapon"), "\r\n"));

					readInventory(pc, con, inv, new StringTokenizer(rs.getString("armor"), "\r\n"));

					readInventory(pc, con, inv, new StringTokenizer(rs.getString("etc"), "\r\n"));
				}

			}

			if (Lineage.server_version > 200)
				pc.toSender(S_InventoryList.clone(BasePacketPooling.getPool(S_InventoryList.class), inv));

		} catch (Exception e) {
			lineage.share.System.printf("%s : readInventory(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	static public void readInventory(PcInstance pc, Connection con, Inventory inv, StringTokenizer token) {
		while (token.hasMoreTokens()) {
			String data = token.nextToken();
			try {
				String[] db = data.split(",");
				ItemInstance item = ItemDatabase.newInstance(ItemDatabase.find(db[1]));
				if (item != null && Long.valueOf(db[2]) > 0) {
					item.setObjectId(Long.valueOf(db[0]));
					item.setCount(Long.valueOf(db[2]));
					item.setQuantity(Integer.valueOf(db[3]));
					item.setEnLevel(Integer.valueOf(db[4]));
					item.setEquipped(Integer.valueOf(db[5]) == 1);
					item.setDefinite(Integer.valueOf(db[6]) == 1);
					item.setBress(Integer.valueOf(db[7]));
					item.setDurability(Integer.valueOf(db[8]));
					item.setNowTime(Integer.valueOf(db[9]));
					item.setPetObjectId(Long.valueOf(db[10]));
					item.setInnRoomKey(Long.valueOf(db[11]));
					item.setLetterUid(Integer.valueOf(db[12]));
					item.setRaceTicket(db[13]);
					if (db.length > 16) {
						StringTokenizer tok = new StringTokenizer(db[16], "|");
						while (tok.hasMoreTokens()) {
							String[] t = tok.nextToken().split("=");
							item.setOption(t[0], t[1]);
						}
					}
					if (db.length > 17) {
						StringTokenizer tok = new StringTokenizer(db[17], "|");
						item.setEnWind(Integer.valueOf(tok.nextToken()));
						item.setEnEarth(Integer.valueOf(tok.nextToken()));
						item.setEnWater(Integer.valueOf(tok.nextToken()));
						item.setEnFire(Integer.valueOf(tok.nextToken()));
					} // 디아블로옵션
					if (db.length > 18) {
						StringTokenizer tok = new StringTokenizer(db[18], "|");
						item.setAdd_Min_Dmg(Integer.valueOf(tok.nextToken()));
						item.setAdd_Max_Dmg(Integer.valueOf(tok.nextToken()));
						item.setAdd_Str(Integer.valueOf(tok.nextToken()));
						item.setAdd_Dex(Integer.valueOf(tok.nextToken()));
						item.setAdd_Con(Integer.valueOf(tok.nextToken()));
						item.setAdd_Int(Integer.valueOf(tok.nextToken()));
						item.setAdd_Wiz(Integer.valueOf(tok.nextToken()));
						item.setAdd_Cha(Integer.valueOf(tok.nextToken()));
						item.setAdd_Hp(Integer.valueOf(tok.nextToken()));
						item.setAdd_Mana(Integer.valueOf(tok.nextToken()));
						item.setAdd_Hpstell(Integer.valueOf(tok.nextToken()));
						item.setAdd_Manastell(Integer.valueOf(tok.nextToken()));
					} // 룬워드
					if (db.length > 19) {
						StringTokenizer tok = new StringTokenizer(db[19], "|");
						item.setOne(Integer.valueOf(tok.nextToken()));
						item.setTwo(Integer.valueOf(tok.nextToken()));
						item.setThree(Integer.valueOf(tok.nextToken()));
						item.setFour(Integer.valueOf(tok.nextToken()));
					}
					// 케릭터 저장구슬 cha_soul
					if (db.length > 20) {
						StringTokenizer tok = new StringTokenizer(db[20], " ");

						item.setSoul_cha(Long.valueOf(tok.nextToken()));
					}
					// 카오스 추가
					if (item.isEquipped() && item.getItem().getType2().equalsIgnoreCase("fishing_rod")) {
						item.setEquipped(false);
						pc.setGfxMode(0);
						pc.toSender(S_ObjectGfx.clone(BasePacketPooling.getPool(S_ObjectGfx.class), pc), true);
					}

					if (data.contains("-") && data.contains(":")) {
						String timestamp = null;
						int num = 0;
						for (String dd : db) {
							if (dd.contains("-") && dd.contains(":")) {
								timestamp = dd;
								break;
							}
							num++;
						}
						// 오차범위 +- 1
						if (db[num].equalsIgnoreCase(timestamp)) {
							System.out.println(timestamp);
							System.out.println(db[num + 1]);
							item.setTimestamp(timestamp);
							item.setTimeCheck(db[num + 1].contains("true"));
						} else if (db[num + 1].equalsIgnoreCase(timestamp)) {
							item.setTimestamp(timestamp);
							item.setTimeCheck(db[num + 2].contains("true"));
						} else if (db[num - 1].equalsIgnoreCase(timestamp)) {
							item.setTimestamp(timestamp);
							item.setTimeCheck(db[num].contains("true"));
						}
					}
					//
					PluginController.init(CharactersDatabase.class, "readInventory", pc, item);

					// 편지지일경우 월드 업데이트를 우선함.
					if (item instanceof Letter) {
						// 착용중인 아이템 정보 갱신.
						item.toWorldJoin(con, pc);
						// 인벤에 등록하면서 패킷 전송.
						inv.append(item, Lineage.server_version <= 200);
					} else {
						// 인벤에 등록하면서 패킷 전송.
						inv.append(item, Lineage.server_version <= 200);
						// 착용중인 아이템 정보 갱신.
						item.toWorldJoin(con, pc);
					}
				}
			} catch (Exception e) {
				System.out
						.println("readInventory(PcInstance pc, Connection con, Inventory inv, StringTokenizer token)");
				System.out.println(data);
				System.out.println(e);
			}
		}
	}

	/**
	 * 스킬 정보 추출.
	 * 
	 * @param con
	 * @param pc
	 */
	static public void readSkill(PcInstance pc) {
		List<Skill> list = SkillController.find(pc);
		if (list == null)
			return;

		// 성능향상을 위한 메모리 처리 부분.
		List<Map<String, Object>> db = null;
		synchronized (skill) {
			db = skill.remove(pc.getObjectId());
		}

		if (db != null) {
			for (Map<String, Object> data : db) {
				try {
					Skill s = SkillDatabase.find((long) data.get("skill"));
					if (s != null && !list.contains(s))
						list.add(s);
				} catch (Exception e) {
				}
			}

			SkillController.sendList(pc);
			db.clear();
			return;
		}

		// 메모리에서 못찾앗으면 디비에서 찾기.
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters_skill WHERE cha_objId=?");
			st.setLong(1, pc.getObjectId());
			rs = st.executeQuery();
			while (rs.next()) {
				Skill s = SkillDatabase.find(rs.getInt("skill"));
				if (s != null && !list.contains(s))
					list.add(s);
			}

			SkillController.sendList(pc);
		} catch (Exception e) {
			lineage.share.System.printf("%s : readSkill(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	/**
	 * 버프 정보 추출.
	 * 
	 * @param con
	 * @param pc
	 */
	static public void readBuff(PcInstance pc) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters_buff WHERE objId=?");
			st.setLong(1, pc.getObjectId());
			rs = st.executeQuery();
			if (rs.next()) {
				int light = rs.getInt("light");
				int shield = rs.getInt("shield");
				int curse_poison = rs.getInt("curse_poison");
				int curse_blind = rs.getInt("curse_blind");
				int slow = rs.getInt("slow");
				int curse_paralyze = rs.getInt("curse_paralyze");
				int enchant_dexterity = rs.getInt("enchant_dexterity");
				int enchant_mighty = rs.getInt("enchant_mighty");
				int haste = rs.getInt("haste");
				int shape_change = rs.getInt("shape_change");
				int immune_to_harm = rs.getInt("immune_to_harm");
				int bravery_potion = rs.getInt("bravery_potion");
				int elvenwafer = rs.getInt("elvenwafer");
				int eva = rs.getInt("eva");
				int wisdom = rs.getInt("wisdom");
				int frameSpeedOverStun = rs.getInt("frame_speed_stun");
				int blue_potion = rs.getInt("blue_potion");
				int floating_eye_meat = rs.getInt("floating_eye_meat");
				int resistelemental = rs.getInt("resistelemental");
				int icelance = rs.getInt("icelance");
				int earthskin = rs.getInt("earthskin");
				int ironskin = rs.getInt("ironskin");
				int blessearth = rs.getInt("blessearth");
				int curse_ghoul = rs.getInt("curse_ghoul");
				int curse_ghast = rs.getInt("curse_ghast");
				int chatting_close = rs.getInt("chatting_close");
				int holywalk = rs.getInt("holywalk");
				int shadowarmor = rs.getInt("shadowarmor");
				int exp_potion_150 = rs.getInt("exp_potion_150");
				int exp_potion_20 = rs.getInt("exp_potion_20");
				int exp_potion_100 = rs.getInt("exp_potion_100");
				int exp_potion_50 = rs.getInt("exp_potion_50");
				int fight_potion = rs.getInt("fight_potion");
				int spell_potion = rs.getInt("spell_potion");
				int health_potion = rs.getInt("health_potion");
				int 수룡의마안 = (rs.getInt("수룡의_마안"));
				int 풍룡의마안 = (rs.getInt("풍룡의_마안"));
				int 지룡의마안 = (rs.getInt("지룡의_마안"));
				int 화룡의마안 = (rs.getInt("화룡의_마안"));
				int 생명의마안 = (rs.getInt("생명의_마안"));
				int 탄생의마안 = (rs.getInt("탄생의_마안"));
				int 형상의마안 = (rs.getInt("형상의_마안"));
				int 수룡의마안_딜레이 = (rs.getInt("수룡의마안_딜레이"));
				int 풍룡의마안_딜레이 = (rs.getInt("풍룡의마안_딜레이"));
				int 지룡의마안_딜레이 = (rs.getInt("지룡의마안_딜레이"));
				int 화룡의마안_딜레이 = (rs.getInt("화룡의마안_딜레이"));
				int 생명의마안_딜레이 = (rs.getInt("생명의마안_딜레이"));
				int 탄생의마안_딜레이 = (rs.getInt("탄생의마안_딜레이"));
				int 형상의마안_딜레이 = (rs.getInt("형상의마안_딜레이"));
				int 지배버프 = rs.getInt("지배버프");
				/*
				 * int exp_potion_20 = rs.getInt("exp_potion_20"); int exp_potion_30 =
				 * rs.getInt("exp_potion_30"); int exp_potion_40 = rs.getInt("exp_potion_40");
				 */

				if (light > 0)
					Light.init(pc, light);
				if (shield > 0)
					Shield.init(pc, shield);
				if (curse_poison > 0)
					CursePoison.init(pc, curse_poison);
				if (curse_blind > 0)
					CurseBlind.init(pc, curse_blind);
				if (slow > 0)
					Slow.init(pc, slow);
				if (curse_paralyze > 0)
					CurseParalyze.init(pc, curse_paralyze);
				if (enchant_dexterity > 0)
					EnchantDexterity.init(pc, enchant_dexterity);
				if (enchant_mighty > 0)
					EnchantMighty.init(pc, enchant_mighty);
				if (haste > 0 || haste == -1)
					Haste.init(pc, haste);
				if (shape_change > 0)
					ShapeChange.init(pc, shape_change);
				if (immune_to_harm > 0)
					ImmuneToHarm.init(pc, immune_to_harm);
				if (bravery_potion > 0 || bravery_potion == -1)
					Bravery.init(pc, bravery_potion);
				if (elvenwafer > 0 || elvenwafer == -1)
					Wafer.init(pc, elvenwafer);
				if (eva > 0)
					Eva.init(pc, eva);
				if (wisdom > 0) {
					// Wisdom.init(pc, wisdom);
				}
				if (frameSpeedOverStun > 0)
					FrameSpeedOverStun.init(pc, frameSpeedOverStun);
				if (blue_potion > 0)
					Blue.init(pc, blue_potion);
				if (floating_eye_meat > 0)
					FloatingEyeMeat.init(pc, floating_eye_meat);
				if (resistelemental > 0)
					ResistElemental.init(pc, resistelemental);
				if (icelance > 0)
					IceLance.init(pc, icelance);
				if (earthskin > 0)
					EarthSkin.init(pc, earthskin);
				if (ironskin > 0)
					IronSkin.init(pc, ironskin);
				if (blessearth > 0)
					BlessOfEarth.init(pc, blessearth);
				if (curse_ghoul > 0)
					CurseGhoul.init(pc, curse_ghoul);
				if (curse_ghast > 0)
					CurseGhast.init(pc, curse_ghast);
				if (chatting_close > 0)
					ChattingClose.init(pc, chatting_close, true);
				if (holywalk > 0 || holywalk == -1)
					HolyWalk.init(pc, holywalk);
				if (shadowarmor > 0)
					ShadowArmor.init(pc, shadowarmor);
				if (exp_potion_150 > 0)
					sp.magic.드래곤사파이어버프.init(pc, exp_potion_150);
				if (exp_potion_20 > 0)
					sp.magic.드래곤루비버프.init(pc, exp_potion_20);
				if (exp_potion_100 > 0)
					sp.magic.드래곤에메랄드버프.init(pc, exp_potion_100);
				if (exp_potion_50 > 0)
					sp.magic.드래곤다이아몬드버프.init(pc, exp_potion_50);
				if (fight_potion > 0)
					전투강화마법.init(pc, fight_potion);
				if (spell_potion > 0)
					마력증강마법.init(pc, spell_potion);
				if (health_potion > 0)
					체력증강마법.init(pc, health_potion);
				if (수룡의마안 > 0)
					MaanWatar.init(pc, 수룡의마안);
				if (풍룡의마안 > 0)
					MaanWind.init(pc, 풍룡의마안);
				if (지룡의마안 > 0)
					MaanEarth.init(pc, 지룡의마안);
				if (화룡의마안 > 0)
					MaanFire.init(pc, 화룡의마안);
				if (생명의마안 > 0)
					MaanLife.init(pc, 생명의마안);
				if (탄생의마안 > 0)
					MaanBirth.init(pc, 탄생의마안);
				if (형상의마안 > 0)
					MaanShape.init(pc, 형상의마안);
				if (수룡의마안_딜레이 > 0)
					MaanWatarDelay.init(pc, 수룡의마안_딜레이);
				if (풍룡의마안_딜레이 > 0)
					MaanWindDelay.init(pc, 풍룡의마안_딜레이);
				if (지룡의마안_딜레이 > 0)
					MaanEarthDelay.init(pc, 지룡의마안_딜레이);
				if (화룡의마안_딜레이 > 0)
					MaanFireDelay.init(pc, 화룡의마안_딜레이);
				if (생명의마안_딜레이 > 0)
					MaanLifeDelay.init(pc, 생명의마안_딜레이);
				if (탄생의마안_딜레이 > 0)
					MaanBirthDelay.init(pc, 탄생의마안_딜레이);
				if (형상의마안_딜레이 > 0)
					MaanShapeDelay.init(pc, 형상의마안_딜레이);
				if (지배버프 > 0)
					BuffRing.init(pc, 지배버프);
				//
				PluginController.init(CharactersDatabase.class, "readBuff", pc, rs);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : readBuff(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	/**
	 * 기억 정보 추출.
	 * 
	 * @param con
	 * @param pc
	 */
	static public void readBook(PcInstance pc) {
		// 성능향상을 위한 메모리 처리 부분.
		List<Map<String, Object>> db = null;
		synchronized (book) {
			db = book.remove(pc.getObjectId());
		}
		if (db != null) {
			for (Map<String, Object> data : db) {
				try {
					Book b = BookController.getPool();
					b.setLocation((String) data.get("location"));
					b.setX((int) data.get("locX"));
					b.setY((int) data.get("locY"));
					b.setMap((int) data.get("locMAP"));

					BookController.append(pc, b);
				} catch (Exception e) {
				}
			}
			db.clear();
			return;
		}

		// 메모리에서 못찾앗으면 디비에서 찾기.
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters_book WHERE objId=? ORDER BY location");
			st.setLong(1, pc.getObjectId());
			rs = st.executeQuery();
			while (rs.next()) {
				Book b = BookController.getPool();
				b.setLocation(rs.getString("location"));
				b.setX(rs.getInt("locX"));
				b.setY(rs.getInt("locY"));
				b.setMap(rs.getInt("locMAP"));

				BookController.append(pc, b);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : readBook(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	/**
	 * 던전정보 추출.
	 * 
	 * @param pc
	 */
	static public void readDungeon(PcInstance pc) {
		List<DungeonPartTime> list = DungeonController.find(pc);
		if (list == null)
			return;

		// 성능향상을 위한 메모리 처리 부분.
		List<Map<String, Object>> db = null;
		synchronized (dungeon) {
			db = dungeon.remove(pc.getObjectId());
		}
		if (db != null) {
			for (Map<String, Object> data : db) {
				try {
					DungeonPartTime dungeon = new DungeonPartTime();
					dungeon.setUid((int) data.get("dungeon_uid"));
					dungeon.setTime((int) data.get("time"));
					dungeon.setUpdateTime((long) data.get("update_time"));
					list.add(dungeon);
				} catch (Exception e) {
				}
			}
			db.clear();
			return;
		}

		// 메모리에서 못찾앗으면 디비에서 찾기.
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters_dungeon WHERE objId=?");
			st.setLong(1, pc.getObjectId());
			rs = st.executeQuery();
			while (rs.next()) {
				DungeonPartTime dungeon = new DungeonPartTime();
				dungeon.setUid(rs.getInt("dungeon_uid"));
				dungeon.setTime(rs.getInt("time"));
				dungeon.setUpdateTime(rs.getTimestamp("update_time").getTime());
				list.add(dungeon);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : readDungeon(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	static public void saveDungeon(Connection con, boolean all) {
		synchronized (dungeon) {
			//
			for (Long objectId : dungeon.keySet()) {
				PreparedStatement st = null;
				try {
					if (all) {
						st = con.prepareStatement("DELETE FROM characters_dungeon WHERE objId=?");
						st.setLong(1, objectId);
						st.executeUpdate();
						st.close();
					}

					List<Map<String, Object>> db = dungeon.get(objectId);
					for (Map<String, Object> data : db) {
						if (!all) {
							// 시간 확인.
							if (data.get("currentTimeMillis") == null)
								data.put("currentTimeMillis", 0L);
							if (Lineage.auto_save_time > 0 && System.currentTimeMillis()
									- (long) data.get("currentTimeMillis") > Lineage.auto_save_time) {
								data.put("currentTimeMillis", System.currentTimeMillis());
								st = con.prepareStatement("DELETE FROM characters_dungeon WHERE objId=?");
								st.setLong(1, objectId);
								st.executeUpdate();
								st.close();
							} else {
								// 설정한 시간이 지나지 않았다면 디비처리 무시.
								break;
							}
						}

						try {
							st = con.prepareStatement(
									"INSERT INTO characters_dungeon SET objId=?, name=?, dungeon_uid=?, time=?, update_time=?");
							st.setLong(1, objectId);
							st.setString(2, (String) data.get("name"));
							st.setInt(3, (int) data.get("dungeon_uid"));
							st.setInt(4, (int) data.get("time"));
							st.setTimestamp(5, new java.sql.Timestamp((long) data.get("update_time")));
							st.executeUpdate();
							st.close();
						} catch (Exception e) {
						} finally {
							DatabaseConnection.close(st);
						}
					}

				} catch (Exception e) {
					lineage.share.System.printf("%s : saveDungeon(Connection con, boolean all)\r\n",
							CharactersDatabase.class.toString());
					lineage.share.System.println(e);
				} finally {
					DatabaseConnection.close(st);
				}
			}
		}
	}

	/**
	 * 
	 * @param pc
	 */
	static public void saveDungeon(Connection con, PcInstance pc) {
		List<DungeonPartTime> list = DungeonController.find(pc);
		if (list == null)
			return;

		List<Map<String, Object>> db = new ArrayList<Map<String, Object>>();
		for (DungeonPartTime d : list) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("name", pc.getName());
			data.put("dungeon_uid", d.getUid());
			data.put("time", d.getTime());
			data.put("update_time", d.getUpdateTime());
			db.add(data);
		}
		synchronized (dungeon) {
			List<Map<String, Object>> temp = dungeon.remove(pc.getObjectId());
			if (temp != null)
				temp.clear();
			dungeon.put(pc.getObjectId(), db);
		}
	}

	static private void saveCharacter(Connection con, boolean all) {
		Collection<PcInstance> save = World.getPcList();

		for (PcInstance pc : save) {
			saveCharacter(con, pc);
		}
	}

	/**
	 * 사용자 정보 저장 함수.
	 * 
	 * @param con
	 * @param pc
	 */
	static public void saveCharacter(Connection con, PcInstance pc) {
		StringBuffer db_interface = new StringBuffer();
		StringBuffer db_blockname = new StringBuffer();
		// 추출.
		if (pc.getDbInterface() != null) {
			for (byte d : pc.getDbInterface())
				db_interface.append(String.format("%02x", d & 0xff));
		}
		for (String name : pc.getListBlockName())
			db_blockname.append(name).append(",");
		PreparedStatement st = null;
		int idx = 0, hp = pc.getNowHp();
		try {
			st = con.prepareStatement("UPDATE characters SET "
					+ "level=?, nowHP=?, maxHP=?, nowMP=?, maxMP=?, ac=?, str=?, dex=?, con=?, wis=?, inter=?, cha=?, sex=?, class=?, "
					+ "exp=?, lost_exp=?, locX=?, locY=?, locMAP=?, title=?, "
					+ "food=?, gfx=?, gfxMode=?, lawful=?, clanID=?, clanNAME=?, pkcount=?, pkTime=?, pkHellTime=?, pkHellCount=?, "
					+ "global_chating=?, trade_chating=?, whisper_chating=?, attribute=?, lvStr=?, lvCon=?, lvDex=?, lvWis=?, "
					+ "lvInt=?, lvCha=?, elixirStr=?, elixirCon=?, elixirDex=?, elixirWis=?, elixirInt=?, "
					+ "elixirCha=?, elixirReset=?, save_interface=?, speedhack_warning_count=?, "
					+ "monster_kill_count=?, clan_grade=?, block_name=?, karma=?, Giran_Quest=?, Talk_Quest=?, pay_info1=?, pay_info2=?, "
					+ "pay_info3=?, moncount=?, temp_name=?, temp_clan_name=?, temp_clan_id=?, temp_title=?, questchapter=?, "
					+ "questkill=?, radomquest=?, randomquestkill=?, RandomQuestCount=?, RandomQuestPlay=?, quick_polymorph=?, is_auto_potion=?, is_auto_potion2=?, auto_potion_percent=?, auto_potion=? WHERE objID=?");
			st.setInt(++idx, pc.getLevel());
			st.setInt(++idx, hp < 0 ? 0 : hp);
			st.setInt(++idx, pc.getMaxHp());
			st.setInt(++idx, pc.getNowMp());
			st.setInt(++idx, pc.getMaxMp());
			st.setInt(++idx, pc.getAc());
			st.setInt(++idx, pc.getStr());
			st.setInt(++idx, pc.getDex());
			st.setInt(++idx, pc.getCon());
			st.setInt(++idx, pc.getWis());
			st.setInt(++idx, pc.getInt());
			st.setInt(++idx, pc.getCha());
			st.setInt(++idx, pc.getClassSex());
			st.setInt(++idx, pc.getClassType());
			st.setDouble(++idx, pc.getExp());
			st.setDouble(++idx, pc.getLostExp());
			st.setInt(++idx, pc.getX());
			st.setInt(++idx, pc.getY());
			st.setInt(++idx, pc.getMap());
			st.setString(++idx, pc.getTitle() == null ? "" : pc.getTitle());
			st.setInt(++idx, pc.getFood());
			st.setInt(++idx, pc.getGfx());
			st.setInt(++idx, pc.getGfxMode() < 0 ? 0 : pc.getGfxMode());
			st.setInt(++idx, pc.getLawful());
			st.setInt(++idx, pc.getClanId());
			st.setString(++idx, pc.getClanName() == null ? "" : pc.getClanName());
			st.setInt(++idx, pc.getPkCount());
			if (pc.getPkTime() == 0)
				st.setString(++idx, "0000-00-00 00:00:00");
			else
				st.setTimestamp(++idx, new Timestamp(pc.getPkTime()));
			st.setInt(++idx, pc.getPkHellTime());
			st.setInt(++idx, pc.getPkHellCount());
			st.setInt(++idx, pc.isChattingGlobal() ? 1 : 0);
			st.setInt(++idx, pc.isChattingTrade() ? 1 : 0);
			st.setInt(++idx, pc.isChattingWhisper() ? 1 : 0);
			st.setInt(++idx, pc.getAttribute());
			st.setInt(++idx, pc.getLvStr());
			st.setInt(++idx, pc.getLvCon());
			st.setInt(++idx, pc.getLvDex());
			st.setInt(++idx, pc.getLvWis());
			st.setInt(++idx, pc.getLvInt());
			st.setInt(++idx, pc.getLvCha());
			st.setInt(++idx, pc.getElixirStr());
			st.setInt(++idx, pc.getElixirCon());
			st.setInt(++idx, pc.getElixirDex());
			st.setInt(++idx, pc.getElixirWis());
			st.setInt(++idx, pc.getElixirInt());
			st.setInt(++idx, pc.getElixirCha());
			st.setInt(++idx, pc.getElixirReset());
			st.setString(++idx, db_interface.toString());
			st.setInt(++idx, pc.getSpeedHackWarningCounting());
			st.setInt(++idx, pc.getMonsterKillCount());
			st.setInt(++idx, pc.getClanGrade());
			st.setString(++idx, db_blockname.toString());
			st.setDouble(++idx, pc.getKarma());
			st.setTimestamp(++idx, pc.getGiranQuestTime());
			st.setTimestamp(++idx, pc.getTalkQuestTime());
			st.setString(++idx, pc.getPay_info1());
			st.setString(++idx, pc.getPay_info2());
			st.setString(++idx, pc.getPay_info3());
			st.setInt(++idx, pc.getAutoHuntMonsterCount());
			st.setString(++idx, pc.getTempName() == null ? "" : pc.getTempName());
			st.setString(++idx, pc.getTempClanName() == null ? "" : pc.getTempClanName());
			st.setInt(++idx, pc.getTempClanId());
			st.setString(++idx, pc.getTempTitle() == null ? "" : pc.getTempTitle());
			st.setInt(++idx, pc.getQuestChapter());
			st.setInt(++idx, pc.getQuestKill());
			st.setInt(++idx, pc.getRadomQuest());
			st.setInt(++idx, pc.getRandomQuestkill());
			st.setInt(++idx, pc.getRandomQuestCount());
			st.setInt(++idx, pc.getRandomQuestPlay());
			st.setString(++idx, pc.getFast_poly() == null ? "" : pc.getFast_poly());
			st.setInt(++idx, pc.isAutoPotion ? 1 : 0);
			st.setInt(++idx, pc.isAutoPotion2 ? 1 : 0);
			st.setInt(++idx, pc.getAutoPotionPercent());
			st.setString(++idx, pc.getAutoPotionName() == null ? "" : pc.getAutoPotionName());
			st.setLong(++idx, pc.getObjectId());
			st.executeUpdate();
			st.close();
			
			st = con.prepareStatement("UPDATE accounts SET daycount=?, daycheck=? , daytime=?, giran_dungeon_time=?, giran_dungeon_count=? WHERE uid=?");
			st.setInt(1, pc.getDaycount());
			st.setInt(2, pc.getDaycheck());
			st.setInt(3, pc.getDayptime());
			st.setInt(4, pc.getGiran_dungeon_time());
			st.setInt(5, pc.getGiran_dungeon_count());
			st.setLong(6, pc.getAccountUid());
			st.executeUpdate();
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : saveCharacter(Connection con, boolean all) : %d\r\n",
					CharactersDatabase.class.toString(), idx);
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}
	
	static public void saveExp(Connection con, PcInstance pc) {
		PreparedStatement st = null;
		try {
			// 처리.
			st = con.prepareStatement("UPDATE characters SET level=?, exp=? WHERE objID=?");
			st.setInt(1, pc.getLevel());
			st.setDouble(2, pc.getExp());
			st.setLong(3, pc.getObjectId());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : saveExp(Connection con, PcInstance pc)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}

	static public void saveInventory(Connection con, boolean all) {
		synchronized (inventory) {
			//
			for (Long objectId : inventory.keySet()) {
				Map<String, Object> db = inventory.get(objectId);
				//
				if (!all) {
					// 시간 확인.
					if (db.get("currentTimeMillis") == null)
						db.put("currentTimeMillis", 0L);
					if (Lineage.auto_save_time > 0 && System.currentTimeMillis()
							- (long) db.get("currentTimeMillis") > Lineage.auto_save_time) {
						db.put("currentTimeMillis", System.currentTimeMillis());
					} else {
						// 설정한 시간이 지나지 않았다면 디비처리 무시.
						continue;
					}
				}
				//
				PreparedStatement st = null;
				try {
					st = con.prepareStatement(
							"UPDATE characters_inventory SET weapon=?, armor=?, etc=? WHERE cha_objId=?");
					st.setString(1, (String) db.get("weapon"));
					st.setString(2, (String) db.get("armor"));
					st.setString(3, (String) db.get("etc"));
					st.setLong(4, objectId);
					st.executeUpdate();
				} catch (Exception e) {
					System.out.println("saveInventory(Connection con, boolean all)");
					System.out.println(e);
				} finally {
					DatabaseConnection.close(st);
				}
			}
		}
	}

	/**
	 * 인벤토리 정보 저장 함수.
	 * 
	 * @param con
	 * @param pc
	 */
	static public void saveInventory(Connection con, PcInstance pc) {
		Inventory inv = pc.getInventory();
		if (inv != null) {
			StringBuffer weapon = new StringBuffer();
			StringBuffer armor = new StringBuffer();
			StringBuffer etc = new StringBuffer();
			for (ItemInstance item : inv.getList()) {
				try {
					if (item.getItem() == null) {
						System.out.println(item);
						System.out.println("getItem() 이 null 입니다.");
						System.out.println(" : name = " + item.getName());
						System.out.println(" : objId = " + item.getObjectId());
						continue;
					}
					if (!item.getItem().isInventorySave()) {
						// 저장 할필요 없는 아이템은 무시.
						if (item.isEquipped())
							// 착용되잇을경우 해제.
							item.toClick(pc, null);
						continue;
					}
					//
					if (item.getItem().getType1().equalsIgnoreCase("weapon"))
						weapon.append(item.toStringSaveDB());
					else if (item.getItem().getType1().equalsIgnoreCase("armor"))
						armor.append(item.toStringSaveDB());
					else
						etc.append(item.toStringSaveDB());
					//
					PluginController.init(CharactersDatabase.class, "saveInventory", pc, item, con);
				} catch (Exception e) {
					System.out.println("saveInventory(Connection con, PcInstance pc) 1");
					System.out.println(e);
				}
			}
			//
			long time = System.currentTimeMillis();
			Map<String, Object> db = new HashMap<String, Object>();
			db.put("name", pc.getName());
			db.put("weapon", weapon.toString());
			db.put("armor", armor.toString());
			db.put("etc", etc.toString());
			synchronized (inventory) {
				inventory.put(pc.getObjectId(), db);
			}
		}
	}

	static public void saveSkill(Connection con, boolean all) {
		synchronized (skill) {
			//
			for (Long objectId : skill.keySet()) {
				PreparedStatement st = null;
				try {
					if (all) {
						st = con.prepareStatement("DELETE FROM characters_skill WHERE cha_objId=?");
						st.setLong(1, objectId);
						st.executeUpdate();
						st.close();
					}

					List<Map<String, Object>> db = skill.get(objectId);
					for (Map<String, Object> data : db) {
						if (!all) {
							// 시간 확인.
							if (data.get("currentTimeMillis") == null)
								data.put("currentTimeMillis", 0L);
							if (Lineage.auto_save_time > 0 && System.currentTimeMillis()
									- (long) data.get("currentTimeMillis") > Lineage.auto_save_time) {
								data.put("currentTimeMillis", System.currentTimeMillis());
								st = con.prepareStatement("DELETE FROM characters_skill WHERE cha_objId=?");
								st.setLong(1, objectId);
								st.executeUpdate();
								st.close();
							} else {
								// 설정한 시간이 지나지 않았다면 디비처리 무시.
								break;
							}
						}

						try {
							st = con.prepareStatement(
									"INSERT INTO characters_skill SET cha_objId=?, cha_name=?, skill=?, skill_name=?");
							st.setLong(1, objectId);
							st.setString(2, (String) data.get("name"));
							st.setLong(3, (long) data.get("skill"));
							st.setString(4, (String) data.get("skill_name"));
							st.executeUpdate();
							st.close();
						} catch (Exception e) {
						} finally {
							DatabaseConnection.close(st);
						}
					}

				} catch (Exception e) {
					lineage.share.System.printf("%s : saveSkill(Connection con, boolean all)\r\n",
							CharactersDatabase.class.toString());
					lineage.share.System.println(e);
				} finally {
					DatabaseConnection.close(st);
				}
			}
		}
	}

	/**
	 * 스킬 정보 저장 함수.
	 * 
	 * @param con
	 * @param pc
	 */
	static public void saveSkill(Connection con, PcInstance pc) {
		List<Skill> list = SkillController.find(pc);
		if (list == null)
			return;

		long time = System.currentTimeMillis();
		List<Map<String, Object>> db = new ArrayList<Map<String, Object>>();
		for (Skill s : list) {
			Map<String, Object> data = new HashMap<String, Object>();
//			data.put("currentTimeMillis", time);
			data.put("name", pc.getName());
			data.put("skill", s.getUid());
			data.put("skill_name", s.getName());
			db.add(data);
		}
		synchronized (skill) {
			List<Map<String, Object>> temp = skill.remove(pc.getObjectId());
			if (temp != null)
				temp.clear();
			skill.put(pc.getObjectId(), db);
		}
	}
	
	/**
	 * 버프 저장 함수.
	 * 
	 * @param con
	 * @param pc
	 */
	static public void saveBuff(Connection con, PcInstance pc) {
		PreparedStatement st = null;
		try {
			Buff b = BuffController.find(pc);
			BuffInterface light = null;
			BuffInterface shield = null;
			BuffInterface curse_poison = null;
			BuffInterface curse_blind = null;
			BuffInterface slow = null;
			BuffInterface curse_paralyze = null;
			BuffInterface enchant_dexterity = null;
			BuffInterface enchant_mighty = null;
			BuffInterface haste = null;
			BuffInterface shape_change = null;
			BuffInterface immune_to_harm = null;
			BuffInterface bravery_potion = null;
			BuffInterface elvenwafer = null;
			BuffInterface eva = null;
			BuffInterface wisdom = null;
			BuffInterface frameSpeedOverSutn = null;
			BuffInterface blue_potion = null;
			BuffInterface floating_eye_meat = null;
			BuffInterface resistelemental = null;
			BuffInterface icelance = null;
			BuffInterface earthskin = null;
			BuffInterface ironskin = null;
			BuffInterface blessearth = null;
			BuffInterface curse_ghoul = null;
			BuffInterface curse_ghast = null;
			BuffInterface chatting_close = null;
			BuffInterface holywalk = null;
			BuffInterface shadowarmor = null;
			BuffInterface exp_potion_150 = null;
			BuffInterface exp_potion_20 = null;
			BuffInterface exp_potion_100 = null;
			BuffInterface exp_potion_50 = null;
			BuffInterface fight_potion = null;
			BuffInterface spell_potion = null;
			BuffInterface health_potion = null;
			BuffInterface 수룡의마안 = null;
			BuffInterface 풍룡의마안 = null;
			BuffInterface 지룡의마안 = null;
			BuffInterface 화룡의마안 = null;
			BuffInterface 생명의마안 = null;
			BuffInterface 탄생의마안 = null;
			BuffInterface 형상의마안 = null;
			BuffInterface 수룡의마안_딜레이 = null;
			BuffInterface 풍룡의마안_딜레이 = null;
			BuffInterface 지룡의마안_딜레이 = null;
			BuffInterface 화룡의마안_딜레이 = null;
			BuffInterface 생명의마안_딜레이 = null;
			BuffInterface 탄생의마안_딜레이 = null;
			BuffInterface 형상의마안_딜레이 = null;
			BuffInterface 지배버프 = null;
			if (b != null) {
				// 월드종료시 버프쪽 종료처리가 미흡함.
				// 2~3초남겨놓고 종료시 종료처리함수를 처리하지 못해서 정보갱신이 누락됨.
				// 담 로직작성시 이상적인 로직으로 코딩할것
				// : 멀티쓰레드에 문제인듯.
				light = b.find(Light.class);
				if (light != null && light.getTime() == 0)
					light.toBuffStop(pc);
				shield = b.find(Shield.class);
				if (shield != null && shield.getTime() == 0)
					shield.toBuffStop(pc);
				curse_poison = b.find(CursePoison.class);
				if (curse_poison != null && curse_poison.getTime() == 0)
					curse_poison.toBuffStop(pc);
				curse_blind = b.find(CurseBlind.class);
				if (curse_blind != null && curse_blind.getTime() == 0)
					curse_blind.toBuffStop(pc);
				slow = b.find(Slow.class);
				if (slow != null && slow.getTime() == 0)
					slow.toBuffStop(pc);
				curse_paralyze = b.find(CurseParalyze.class);
				if (curse_paralyze != null && curse_paralyze.getTime() == 0)
					curse_paralyze.toBuffStop(pc);
				enchant_dexterity = b.find(EnchantDexterity.class);
				if (enchant_dexterity != null && enchant_dexterity.getTime() == 0)
					enchant_dexterity.toBuffStop(pc);
				enchant_mighty = b.find(EnchantMighty.class);
				if (enchant_mighty != null && enchant_mighty.getTime() == 0)
					enchant_mighty.toBuffStop(pc);
				haste = b.find(Haste.class);
				if (haste != null && haste.getTime() == 0)
					haste.toBuffStop(pc);
				shape_change = b.find(ShapeChange.class);
				if (shape_change != null && shape_change.getTime() == 0)
					shape_change.toBuffStop(pc);
				immune_to_harm = b.find(ImmuneToHarm.class);
				if (immune_to_harm != null && immune_to_harm.getTime() == 0)
					immune_to_harm.toBuffStop(pc);
				bravery_potion = b.find(Bravery.class);
				if (bravery_potion != null && bravery_potion.getTime() == 0)
					bravery_potion.toBuffStop(pc);
				elvenwafer = b.find(Wafer.class);
				if (elvenwafer != null && elvenwafer.getTime() == 0)
					elvenwafer.toBuffStop(pc);
				eva = b.find(Eva.class);
				if (eva != null && eva.getTime() == 0)
					eva.toBuffStop(pc);
				wisdom = b.find(Wisdom.class);
				if (wisdom != null && wisdom.getTime() == 0)
					wisdom.toBuffStop(pc);
				frameSpeedOverSutn = b.find(FrameSpeedOverStun.class);
				if (frameSpeedOverSutn != null && frameSpeedOverSutn.getTime() == 0)
					frameSpeedOverSutn.toBuffStop(pc);
				blue_potion = b.find(Blue.class);
				if (blue_potion != null && blue_potion.getTime() == 0)
					blue_potion.toBuffStop(pc);
				floating_eye_meat = b.find(FloatingEyeMeat.class);
				if (floating_eye_meat != null && floating_eye_meat.getTime() == 0)
					floating_eye_meat.toBuffStop(pc);
				resistelemental = b.find(ResistElemental.class);
				if (resistelemental != null && resistelemental.getTime() == 0)
					resistelemental.toBuffStop(pc);
				icelance = b.find(IceLance.class);
				if (icelance != null && icelance.getTime() == 0)
					icelance.toBuffStop(pc);
				blessearth = b.find(BlessOfEarth.class);
				if (blessearth != null && blessearth.getTime() == 0)
					blessearth.toBuffStop(pc);
				earthskin = b.find(EarthSkin.class);
				if (earthskin != null && earthskin.getTime() == 0)
					earthskin.toBuffStop(pc);
				ironskin = b.find(IronSkin.class);
				if (ironskin != null && ironskin.getTime() == 0)
					ironskin.toBuffStop(pc);
				curse_ghoul = b.find(CurseGhoul.class);
				if (curse_ghoul != null && curse_ghoul.getTime() == 0)
					curse_ghoul.toBuffStop(pc);
				curse_ghast = b.find(CurseGhast.class);
				if (curse_ghast != null && curse_ghast.getTime() == 0)
					curse_ghast.toBuffStop(pc);
				chatting_close = b.find(ChattingClose.class);
				if (chatting_close != null && chatting_close.getTime() == 0)
					chatting_close.toBuffStop(pc);
				holywalk = b.find(HolyWalk.class);
				if (holywalk != null && holywalk.getTime() == 0)
					holywalk.toBuffStop(pc);
				shadowarmor = b.find(ShadowArmor.class);
				if (shadowarmor != null && shadowarmor.getTime() == 0)
					shadowarmor.toBuffStop(pc);

				exp_potion_150 = b.find(드래곤사파이어버프.class);
				if (exp_potion_150 != null && exp_potion_150.getTime() == 0)
					exp_potion_150.toBuffStop(pc);

				exp_potion_20 = b.find(드래곤루비버프.class);
				if (exp_potion_20 != null && exp_potion_20.getTime() == 0) {
					exp_potion_20.toBuffStop(pc);
				}

				exp_potion_100 = b.find(드래곤에메랄드버프.class);
				if (exp_potion_100 != null && exp_potion_100.getTime() == 0) {
					exp_potion_100.toBuffStop(pc);
				}

				exp_potion_50 = b.find(드래곤다이아몬드버프.class);
				if (exp_potion_50 != null && exp_potion_50.getTime() == 0) {
					exp_potion_50.toBuffStop(pc);
				}
				fight_potion = b.find(전투강화마법.class);
				if (fight_potion != null && fight_potion.getTime() == 0) {
					fight_potion.toBuffStop(pc);
				}
				spell_potion = b.find(마력증강마법.class);
				if (spell_potion != null && spell_potion.getTime() == 0) {
					spell_potion.toBuffStop(pc);
				}
				health_potion = b.find(체력증강마법.class);
				if (health_potion != null && health_potion.getTime() == 0) {
					health_potion.toBuffStop(pc);
				}
				수룡의마안 = b.find(MaanWatar.class);
				if (수룡의마안 != null && 수룡의마안.getTime() == 0) {
					수룡의마안.toBuffStop(pc);
				}
				풍룡의마안 = b.find(MaanWind.class);
				if (풍룡의마안 != null && 풍룡의마안.getTime() == 0) {
					풍룡의마안.toBuffStop(pc);
				}
				지룡의마안 = b.find(MaanEarth.class);
				if (지룡의마안 != null && 지룡의마안.getTime() == 0) {
					지룡의마안.toBuffStop(pc);
				}
				화룡의마안 = b.find(MaanFire.class);
				if (화룡의마안 != null && 화룡의마안.getTime() == 0) {
					화룡의마안.toBuffStop(pc);
				}
				생명의마안 = b.find(MaanLife.class);
				if (생명의마안 != null && 생명의마안.getTime() == 0) {
					생명의마안.toBuffStop(pc);
				}
				탄생의마안 = b.find(MaanBirth.class);
				if (탄생의마안 != null && 탄생의마안.getTime() == 0) {
					탄생의마안.toBuffStop(pc);
				}
				형상의마안 = b.find(MaanShape.class);
				if (형상의마안 != null && 형상의마안.getTime() == 0) {
					형상의마안.toBuffStop(pc);
				}
				수룡의마안_딜레이 = b.find(MaanWatarDelay.class);
				if (수룡의마안_딜레이 != null && 수룡의마안_딜레이.getTime() == 0) {
					수룡의마안_딜레이.toBuffStop(pc);
				}
				풍룡의마안_딜레이 = b.find(MaanWindDelay.class);
				if (풍룡의마안_딜레이 != null && 풍룡의마안_딜레이.getTime() == 0) {
					풍룡의마안_딜레이.toBuffStop(pc);
				}
				지룡의마안_딜레이 = b.find(MaanEarthDelay.class);
				if (지룡의마안_딜레이 != null && 지룡의마안_딜레이.getTime() == 0) {
					지룡의마안_딜레이.toBuffStop(pc);
				}
				화룡의마안_딜레이 = b.find(MaanFireDelay.class);
				if (화룡의마안_딜레이 != null && 화룡의마안_딜레이.getTime() == 0) {
					화룡의마안_딜레이.toBuffStop(pc);
				}
				생명의마안_딜레이 = b.find(MaanLifeDelay.class);
				if (생명의마안_딜레이 != null && 생명의마안_딜레이.getTime() == 0) {
					생명의마안_딜레이.toBuffStop(pc);
				}
				탄생의마안_딜레이 = b.find(MaanBirthDelay.class);
				if (탄생의마안_딜레이 != null && 탄생의마안_딜레이.getTime() == 0) {
					탄생의마안_딜레이.toBuffStop(pc);
				}
				형상의마안_딜레이 = b.find(MaanShapeDelay.class);
				if (형상의마안_딜레이 != null && 형상의마안_딜레이.getTime() == 0) {
					형상의마안_딜레이.toBuffStop(pc);
				}
				지배버프 = b.find(BuffRing.class);
				if (지배버프 != null && 지배버프.getTime() == 0) {
					지배버프.toBuffStop(pc);
				}
			}

			st = con.prepareStatement("UPDATE characters_buff SET "
					+ "name=?, light=?, shield=?, curse_poison=?, curse_blind=?, slow=?, curse_paralyze=?, enchant_dexterity=?, enchant_mighty=?, "
					+ "haste=?, shape_change=?, immune_to_harm=?, bravery_potion=?, elvenwafer=?, eva=?, wisdom=?, frame_speed_stun=?, blue_potion=?, "
					+ "floating_eye_meat=?, resistelemental=?, icelance=?, earthskin=?, ironskin=?, blessearth=?, curse_ghoul=?, "
					+ "curse_ghast=?, chatting_close=?, holywalk=?, shadowarmor=?, exp_potion_150=?, exp_potion_20=?, exp_potion_100=?, exp_potion_50=?, fight_potion=?, spell_potion=?, health_potion=?, "
					+ "수룡의_마안=?, 풍룡의_마안=?, 지룡의_마안=?, 화룡의_마안=?, 생명의_마안=?, 탄생의_마안=?, 형상의_마안=?, "
					+ "수룡의마안_딜레이=?, 풍룡의마안_딜레이=?, 지룡의마안_딜레이=?, 화룡의마안_딜레이=?, 생명의마안_딜레이=?, 탄생의마안_딜레이=?, 형상의마안_딜레이=? , 지배버프=? "
					+ "WHERE objId=?");
			st.setString(1, pc.getName());
			st.setInt(2, light == null ? 0 : light.getTime());
			st.setInt(3, shield == null ? 0 : shield.getTime());
			st.setInt(4, curse_poison == null ? 0 : curse_poison.getTime());
			st.setInt(5, curse_blind == null ? 0 : curse_blind.getTime());
			st.setInt(6, slow == null ? 0 : slow.getTime());
			st.setInt(7, curse_paralyze == null ? 0 : curse_paralyze.getTime());
			st.setInt(8, enchant_dexterity == null ? 0 : enchant_dexterity.getTime());
			st.setInt(9, enchant_mighty == null ? 0 : enchant_mighty.getTime());
			st.setInt(10, haste == null ? 0 : haste.getTime());
			st.setInt(11, shape_change == null ? 0 : shape_change.getTime());
			st.setInt(12, immune_to_harm == null ? 0 : immune_to_harm.getTime());
			st.setInt(13, bravery_potion == null ? 0 : bravery_potion.getTime());
			st.setInt(14, elvenwafer == null ? 0 : elvenwafer.getTime());
			st.setInt(15, eva == null ? 0 : eva.getTime());
			st.setInt(16, 0);// wisdom == null ? 0 : wisdom.getTime());
			st.setInt(17, frameSpeedOverSutn == null ? 0 : frameSpeedOverSutn.getTime());
			st.setInt(18, blue_potion == null ? 0 : blue_potion.getTime());
			st.setInt(19, floating_eye_meat == null ? 0 : floating_eye_meat.getTime());
			st.setInt(20, resistelemental == null ? 0 : resistelemental.getTime());
			st.setInt(21, icelance == null ? 0 : icelance.getTime());
			st.setInt(22, earthskin == null ? 0 : earthskin.getTime());
			st.setInt(23, ironskin == null ? 0 : ironskin.getTime());
			st.setInt(24, blessearth == null ? 0 : blessearth.getTime());
			st.setInt(25, curse_ghoul == null ? 0 : curse_ghoul.getTime());
			st.setInt(26, curse_ghast == null ? 0 : curse_ghast.getTime());
			st.setInt(27, chatting_close == null ? 0 : chatting_close.getTime());
			st.setInt(28, holywalk == null ? 0 : holywalk.getTime());
			st.setInt(29, shadowarmor == null ? 0 : shadowarmor.getTime());
			st.setInt(30, exp_potion_150 == null ? 0 : exp_potion_150.getTime());
			st.setInt(31, exp_potion_20 == null ? 0 : exp_potion_20.getTime());
			st.setInt(32, exp_potion_100 == null ? 0 : exp_potion_100.getTime());
			st.setInt(33, exp_potion_50 == null ? 0 : exp_potion_50.getTime());
			st.setInt(34, fight_potion == null ? 0 : fight_potion.getTime());
			st.setInt(35, spell_potion == null ? 0 : spell_potion.getTime());
			st.setInt(36, health_potion == null ? 0 : health_potion.getTime());
			st.setInt(37, 수룡의마안 == null ? 0 : 수룡의마안.getTime());
			st.setInt(38, 풍룡의마안 == null ? 0 : 풍룡의마안.getTime());
			st.setInt(39, 지룡의마안 == null ? 0 : 지룡의마안.getTime());
			st.setInt(40, 화룡의마안 == null ? 0 : 화룡의마안.getTime());
			st.setInt(41, 생명의마안 == null ? 0 : 생명의마안.getTime());
			st.setInt(42, 탄생의마안 == null ? 0 : 탄생의마안.getTime());
			st.setInt(43, 형상의마안 == null ? 0 : 형상의마안.getTime());
			st.setInt(44, 수룡의마안_딜레이 == null ? 0 : 수룡의마안_딜레이.getTime());
			st.setInt(45, 풍룡의마안_딜레이 == null ? 0 : 풍룡의마안_딜레이.getTime());
			st.setInt(46, 지룡의마안_딜레이 == null ? 0 : 지룡의마안_딜레이.getTime());
			st.setInt(47, 화룡의마안_딜레이 == null ? 0 : 화룡의마안_딜레이.getTime());
			st.setInt(48, 생명의마안_딜레이 == null ? 0 : 생명의마안_딜레이.getTime());
			st.setInt(49, 탄생의마안_딜레이 == null ? 0 : 탄생의마안_딜레이.getTime());
			st.setInt(50, 형상의마안_딜레이 == null ? 0 : 형상의마안_딜레이.getTime());
			st.setInt(51, 지배버프 == null ? 0 : 지배버프.getTime());
			st.setLong(52, pc.getObjectId());
			st.executeUpdate();

			//
			PluginController.init(CharactersDatabase.class, "saveBuff", pc, con);
		} catch (Exception e) {
			lineage.share.System.printf("%s : saveBuff(Connection con, PcInstance pc)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(st);
		}
	}

	static public void saveBook(Connection con, boolean all) {
		synchronized (book) {
			//
			for (Long objectId : book.keySet()) {
				PreparedStatement st = null;
				try {
					if (all) {
						st = con.prepareStatement("DELETE FROM characters_book WHERE objId=?");
						st.setLong(1, objectId);
						st.executeUpdate();
						st.close();
					}

					List<Map<String, Object>> db = book.get(objectId);
					for (Map<String, Object> data : db) {
						if (!all) {
							// 시간 확인.
							if (data.get("currentTimeMillis") == null)
								data.put("currentTimeMillis", 0L);
							if (Lineage.auto_save_time > 0 && System.currentTimeMillis()
									- (long) data.get("currentTimeMillis") > Lineage.auto_save_time) {
								data.put("currentTimeMillis", System.currentTimeMillis());
								st = con.prepareStatement("DELETE FROM characters_book WHERE objId=?");
								st.setLong(1, objectId);
								st.executeUpdate();
								st.close();
							} else {
								// 설정한 시간이 지나지 않았다면 디비처리 무시.
								break;
							}
						}

						try {
							st = con.prepareStatement(
									"INSERT INTO characters_book SET objId=?, name=?, location=?, locX=?, locY=?, locMAP=?");
							st.setLong(1, objectId);
							st.setString(2, (String) data.get("name"));
							st.setString(3, (String) data.get("location"));
							st.setInt(4, (int) data.get("locX"));
							st.setInt(5, (int) data.get("locY"));
							st.setInt(6, (int) data.get("locMAP"));
							st.executeUpdate();
							st.close();
						} catch (Exception e) {
						} finally {
							DatabaseConnection.close(st);
						}
					}

				} catch (Exception e) {
					lineage.share.System.printf("%s : saveBook(Connection con, boolean all)\r\n",
							CharactersDatabase.class.toString());
					lineage.share.System.println(e);
				} finally {
					DatabaseConnection.close(st);
				}
			}
		}
	}
	
	/**
	 * 기억 정보 저장 함수.
	 * 
	 * @param con
	 * @param pc
	 */
	static public void saveBook(Connection con, PcInstance pc) {
		List<Book> list = BookController.find(pc);
		if (list == null)
			return;

		long time = System.currentTimeMillis();
		List<Map<String, Object>> db = new ArrayList<Map<String, Object>>();
		for (Book b : list) {
			Map<String, Object> data = new HashMap<String, Object>();
//			data.put("currentTimeMillis", time);
			data.put("name", pc.getName());
			data.put("location", b.getLocation());
			data.put("locX", b.getX());
			data.put("locY", b.getY());
			data.put("locMAP", b.getMap());
			db.add(data);
		}
		synchronized (book) {
			List<Map<String, Object>> temp = book.remove(pc.getObjectId());
			if (temp != null)
				temp.clear();
			book.put(pc.getObjectId(), db);
		}
	}

	static public int getPvpKill(PcInstance pc) {
		int cnt = 0;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT COUNT(uid) as cnt FROM characters_pvp WHERE objectId=? AND is_kill=1");
			st.setLong(1, pc.getObjectId());
			rs = st.executeQuery();
			if (rs.next())
				cnt = rs.getInt("cnt");
		} catch (Exception e) {
			lineage.share.System.printf("%s : getPvpKill()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return cnt;
	}

	static public int getPvpDead(PcInstance pc) {
		int cnt = 0;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT COUNT(uid) as cnt FROM characters_pvp WHERE objectId=? AND is_dead=1");
			st.setLong(1, pc.getObjectId());
			rs = st.executeQuery();
			if (rs.next())
				cnt = rs.getInt("cnt");
		} catch (Exception e) {
			lineage.share.System.printf("%s : getPvpDead()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return cnt;
	}
//	static public int getPvpKill(PcInstance pc) {
//		PreparedStatement st = null;
//		ResultSet rs = null;
//		try {
//			Connection con = DatabaseConnection.getLineage();
//			st = con.prepareStatement("SELECT count(1) as cnt FROM characters_pvp WHERE LOWER(name)=? and is_kill >= 1");
//			st.setString(1, pc.getName().toLowerCase());
//			rs = st.executeQuery();
//			if (rs.next())
//				return rs.getInt("cnt");
//		} catch (Exception e) {
//			lineage.share.System 
//					.printf("%s : getPvpKill()\r\n",
//							CharactersDatabase.class.toString());
//			lineage.share.System.println(e);
//		} finally {
//			DatabaseConnection.close(st, rs);
//		}
//		return 0;
//	}
//	static public int getPvpDead(PcInstance pc) {
//		PreparedStatement st = null;
//		ResultSet rs = null;
//		try {
//			Connection con = DatabaseConnection.getLineage();
//			st = con.prepareStatement("SELECT count(1) as cnt FROM characters_pvp WHERE LOWER(name)=? and is_dead >= 1");
//			st.setString(1, pc.getName().toLowerCase());
//			rs = st.executeQuery();
//			if (rs.next())
//				return rs.getInt("cnt");
//		} catch (Exception e) {
//			lineage.share.System
//			.printf("%s : getPvpDead()\r\n",
//					CharactersDatabase.class.toString());
//			lineage.share.System.println(e);
//		} finally {
//			DatabaseConnection.close(st, rs);
//		}
//		return 0;
//	}

	/**
	 * pvp시 승리햇을경우 호출됨
	 * 
	 * @param pc     : 승리자
	 * @param target : 패배자
	 */
	static public void updatePvpKill(PcInstance pc, PcInstance target) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement(
					"INSERT INTO characters_pvp SET objectId=?, name=?, is_kill=?, target_objectId=?, target_name=?, pvp_date=?");
			st.setLong(1, pc.getObjectId());
			st.setString(2, pc.getName());
			st.setInt(3, 1);
			st.setLong(4, target.getObjectId());
			st.setString(5, target.getName());
			st.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			st.executeUpdate();
			st.close();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updatePvpKill(PcInstance pc, PcInstance target)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	/**
	 * pvp시 패배했을때 호출됨.
	 * 
	 * @param pc     : 패배자
	 * @param target : 승리자
	 */
	static public void updatePvpDead(PcInstance pc, PcInstance target) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement(
					"INSERT INTO characters_pvp SET objectId=?, name=?, is_dead=?, target_objectId=?, target_name=?, pvp_date=?");
			st.setLong(1, pc.getObjectId());
			st.setString(2, pc.getName());
			st.setInt(3, 1);
			st.setLong(4, target.getObjectId());
			st.setString(5, target.getName());
			st.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			st.executeUpdate();
			st.close();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updatePvpDead(PcInstance pc, PcInstance target)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	static public int getCharacterAccountUid(Connection con, String name) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT account_uid FROM characters WHERE LOWER(name)=?");
			st.setString(1, name.toLowerCase());
			rs = st.executeQuery();
			if (rs.next())
				return rs.getInt("account_uid");
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCharacterAccountUid(Connection con, String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		return 0;
	}

	static public int getCharacterAccountUid(Connection con, int cha_objectId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT account_uid FROM characters WHERE objID=?");
			st.setInt(1, cha_objectId);
			rs = st.executeQuery();
			if (rs.next())
				return rs.getInt("account_uid");
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCharacterAccountUid(Connection con, int cha_objectId)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		return 0;
	}

	static public String getCharacterName(Connection con, int cha_objectId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT name FROM characters WHERE objID=?");
			st.setInt(1, cha_objectId);
			rs = st.executeQuery();
			if (rs.next())
				return rs.getString("name");
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCharacterName(Connection con, int cha_objectId)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		return null;
	}

	static public void updateGiranQuest(PcInstance pc, Timestamp p) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = DatabaseConnection.getLineage();
			pstm = con.prepareStatement("UPDATE characters SET Giran_Quest=? WHERE objid=?");
			pstm.setTimestamp(1, p);
			pstm.setLong(2, pc.getObjectId());
			pstm.executeUpdate();
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(con, pstm);
		}
	}

	static public void updateTalkQuest(PcInstance pc, Timestamp p) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = DatabaseConnection.getLineage();
			pstm = con.prepareStatement("UPDATE characters SET Talk_Quest=? WHERE objid=?");
			pstm.setTimestamp(1, p);
			pstm.setLong(2, pc.getObjectId());
			pstm.executeUpdate();
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(con, pstm);
		}
	}
	
	static public void insertQuest(Connection con, boolean all) {
		synchronized (quest) {
			//
			for (Long objectId : quest.keySet()) {
				PreparedStatement st = null;
				try {
					if (all) {
						st = con.prepareStatement("DELETE FROM characters_quest WHERE objId=?");
						st.setLong(1, objectId);
						st.executeUpdate();
						st.close();
					}

					List<Map<String, Object>> db = quest.get(objectId);
					for (Map<String, Object> data : db) {
						if (!all) {
							// 시간 확인.
							if (data.get("currentTimeMillis") == null)
								data.put("currentTimeMillis", 0L);
							if (Lineage.auto_save_time > 0 && System.currentTimeMillis()
									- (long) data.get("currentTimeMillis") > Lineage.auto_save_time) {
								data.put("currentTimeMillis", System.currentTimeMillis());
								st = con.prepareStatement("DELETE FROM characters_quest WHERE objId=?");
								st.setLong(1, objectId);
								st.executeUpdate();
								st.close();
							} else {
								// 설정한 시간이 지나지 않았다면 디비처리 무시.
								break;
							}
						}
				//
						try {
							st = con.prepareStatement(
									"INSERT INTO characters_quest SET objId=?, name=?, npc_name=?, quest_action=?, quest_step=?");
							st.setLong(1, objectId);
							st.setString(2, (String) data.get("name"));
							st.setString(3, (String) data.get("npc_name"));
							st.setString(4, (String) data.get("quest_action"));
							st.setInt(5, (int) data.get("quest_step"));
							st.executeUpdate();
							st.close();
						} catch (Exception e) {
						} finally {
							DatabaseConnection.close(st);
						}
					}

				} catch (Exception e) {
					lineage.share.System.printf("%s : insertQuest(Connection con, boolean all)\r\n",
							CharactersDatabase.class.toString());
					lineage.share.System.println(e);
				} finally {
					DatabaseConnection.close(st);
				}
			}
		}
	}
	public static void insertQuest(Connection con, PcInstance pc) {
		List<Quest> list = QuestController.find(pc);
		if (list == null)
			return;

		List<Map<String, Object>> db = new ArrayList<Map<String, Object>>();
		for (Quest q : list) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("name", pc.getName());
			data.put("npc_name", q.getNpcName());
			data.put("quest_action", q.getQuestAction());
			data.put("quest_step", q.getQuestStep());
			db.add(data);
		}
		synchronized (quest) {
			List<Map<String, Object>> temp = quest.remove(pc.getObjectId());
			if (temp != null)
				temp.clear();
			quest.put(pc.getObjectId(), db);
		}
	}

	/**
	 * 계정의 캐릭터들이 혈맹에 가입되어 있는지 확인하는 함수 2017-10-17 by all-night
	 */
	static public boolean isClanJoin(PcInstance pc, String clanName) {
		PreparedStatement st = null;
		ResultSet rs = null;
		Connection con = null;
		boolean result = true;

		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT clanNAME FROM characters WHERE account_uid=?");
			st.setLong(1, pc.getClient().getAccountUid());
			rs = st.executeQuery();

			while (rs.next()) {
				if (!rs.getString("clanNAME").equalsIgnoreCase(Lineage.new_clan_name)
						
						&& rs.getString("clanNAME").length() > 0) {
					result = false;
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCharacterClanId(Connection con, String name)\r\n",
					CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return result;
	}

	/**
	 * 혈맹 가입시 정보 수정. 2019-10-29 by connector12@nate.com
	 */
	static public void updateClan(PcInstance pc, Clan c) {
		if (c != null) {
			Connection con = null;
			PreparedStatement st = null;
			
			pc.setTempClanName(pc.getClanName());
			pc.setTempClanId(pc.getClanId());
			pc.setTempTitle(pc.getTitle());
			pc.setTempClanGrade(pc.getClanGrade());		

			try {
				con = DatabaseConnection.getLineage();
				st = con.prepareStatement(
						"UPDATE characters SET clanID=?, clanNAME=?, title=?, clan_grade=?, temp_clan_id=?, temp_clan_name=?, temp_title=?, temp_clan_grade=? WHERE objID=?");
				st.setLong(1, pc.getClanId());
				st.setString(2, pc.getClanName() == null ? "" : pc.getClanName());
				st.setString(3, pc.getTitle() == null ? "" : pc.getTitle());
				st.setInt(4, pc.getClanGrade());
				st.setInt(5, pc.getTempClanId());
				st.setString(6, pc.getTempClanName() == null ? "" : pc.getTempClanName());
				st.setString(7, pc.getTempTitle() == null ? "" : pc.getTempTitle());
				st.setInt(8, pc.getTempClanGrade());
				st.setLong(9, pc.getObjectId());
				st.executeUpdate();
			} catch (Exception e) {
				lineage.share.System.printf("%s : updateClan(PcInstance pc, Clan c)\r\n",
						CharactersDatabase.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st);
			}
		}
	}
	
	/**
	 * 혈맹 탈퇴시 정보 수정.
	 */
	static public void outClan(PcInstance pc, Clan c) {
		if (c != null) {
			Connection con = null;
			PreparedStatement st = null;

			try {
				con = DatabaseConnection.getLineage();
				st = con.prepareStatement(
						"UPDATE characters SET clanID=?, clanNAME=?, title=?, clan_grade=? WHERE objID=?");
				st.setLong(1, 0);
				st.setString(2, "");
				st.setString(3, "");
				st.setInt(4, 0);

				st.setLong(5, pc.getObjectId());
				st.executeUpdate();
			} catch (Exception e) {
				lineage.share.System.printf("%s : updateClan(PcInstance pc, Clan c)\r\n",
						CharactersDatabase.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st);
			}
		}
	}

	/**
	 * 캐릭터 이전 구슬 저장시 계정정보 변경
	 */
	static public void getChangeAccountName(String before_name, String account_name, int account_uid) {

		synchronized (characters) {
			Map<String, Object> db = characters.get(before_name);
			if (db != null) {
				db.put("account", account_name);
				db.put("account_uid", account_uid);
			}
		}

		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters SET account=?,account_uid=? WHERE name=?");
			st.setString(1, account_name);
			st.setInt(2, account_uid);
			st.setString(3, before_name);
			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 채금 해제
	 * 2019-07-07
	 * by connector12@nate.com
	 */
	static public void chattingCloseRemove(String name) {
		Connection con = null;
		PreparedStatement st = null;

		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters_buff SET chatting_close=0 WHERE LOWER(name)=?");
			st.setString(1, name.toLowerCase());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : chattingCloseRemove(String name)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	public static Map<String, Map<String, Object>> getCharacters() {
		return characters;
	}

	public static void setCharacters(Map<String, Map<String, Object>> characters) {
		CharactersDatabase.characters = characters;
	}
}
