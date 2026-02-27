package jsn_soft;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.share.TimeLine;
import lineage.world.object.instance.PcInstance;

public final class AutoHuntDatabase {
	
	static private List<jsn_hunt> list;
		
	static public void init(Connection con) {
		TimeLine.start("AutoHuntDatabase..");

		list = new ArrayList<jsn_hunt>();
		list.clear();
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM autohunt");
			rs = st.executeQuery();
			while (rs.next()) {
				jsn_hunt jh = new jsn_hunt();
				jh.setPcName(rs.getString("pc_name"));
				jh.setPcObjid(rs.getLong("pc_objid"));
				jh.setPcAccount(rs.getString("pc_account"));
				jh.setPcHuntTime(rs.getLong("pc_auto_time"));
				jh.setX(rs.getInt("x"));
				jh.setY(rs.getInt("y"));
				jh.setMap(rs.getInt("map"));
				jh.setAutoSell(rs.getString("is_sell").equals("true"));
				jh.setPotion(rs.getString("potion"));
				jh.setPotionPercent(rs.getInt("potion_percent"));
				
				list.add(jh);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", AutoHuntDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}

		TimeLine.end();
	}
	
	/**
	 * 자동사냥 정보 불러옴
	 * by Jsn_Soft
	 * 2021-02-25
	 */
	static public void readHunt(PcInstance pc) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			jsn_hunt jh = find(pc.getObjectId());
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM autohunt WHERE pc_objid=?");
			st.setLong(1, pc.getObjectId());
			rs = st.executeQuery();
			if (rs.next()) {
				pc.setAutoTime(rs.getLong("pc_auto_time"));
				pc.setSellItem(rs.getString("is_sell").equals("true"));
				if (jh != null) {
					jh.setPotion(rs.getString("potion"));
					jh.setPotionPercent(rs.getInt("potion_percent"));
					jh.setX(rs.getInt("x"));
					jh.setY(rs.getInt("y"));
					jh.setMap(rs.getInt("map"));
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : readHunt(PcInstance pc)\r\n",
					AutoHuntDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}
	
	/**
	 * 자동사냥 정보 저장
	 * by Jsn_Soft
	 * 2021-02-25
	 */
	static public void saveHunt(PcInstance pc) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			// 처리.
			jsn_hunt jh = find(pc.getObjectId());
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE autohunt SET pc_name=?, pc_auto_time=?, is_sell=?, potion=?, potion_percent=?, x=?, y=?, map=? WHERE pc_objid=?");
			st.setString(1, pc.getName());
			st.setLong(2, pc.getAutoTime());
			st.setString(3, pc.isSellItem() ? "true" : "false");
			if (jh != null) {
				st.setString(4, jh.getPotion());
				st.setLong(5, jh.getPotionPercent());
				st.setLong(6, jh.getX());
				st.setLong(7, jh.getY());
				st.setLong(8, jh.getMap());
			} else {
				st.setString(4, "");
				st.setLong(5, 0);
				st.setLong(6, 0);
				st.setLong(7, 0);
				st.setLong(8, 0);
			}
			st.setLong(9, pc.getObjectId());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : saveHunt(Connection con, PcInstance pc)\r\n",
					AutoHuntDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 자동사냥 정보 제거
	 * by Jsn_Soft
	 * 2021-02-25
	 */
	static public void deleteHunt(PcInstance pc, jsn_hunt jh) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			list.remove(jh);
			
			con = DatabaseConnection.getLineage();
			
			st = con.prepareStatement("DELETE FROM autohunt WHERE pc_objid=?");
			st.setLong(1, pc.getObjectId());
			st.executeUpdate();
			st.close();
		} catch (Exception e) {
			lineage.share.System.printf("%s : deleteHunt(PcInstance pc, jsn_hunt jh)\r\n",
					AutoHuntDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 자동사냥 정보 생성
	 * by Jsn_Soft
	 * 2021-02-25
	 */
	static public void insertHunt(PcInstance pc) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			
			st = con.prepareStatement("SELECT * FROM characters WHERE account=? AND LOWER(name)=?");
			st.setString(1, pc.getClient().getAccountId().toLowerCase());
			st.setString(2, pc.getName().toLowerCase());
			rs = st.executeQuery();
			if (rs.next()) {
				jsn_hunt jh = new jsn_hunt();
				jh.setPcName(rs.getString("name"));
				jh.setPcAccount(rs.getString("account"));
				jh.setPcObjid(rs.getInt("objID"));
				jh.setPcHuntTime(0);
				jh.setX(0);
				jh.setY(0);
				jh.setMap(0);
				jh.setAutoSell(false);
				jh.setPotion("");
				jh.setPotionPercent(0);
				list.add(jh);
				st.close();
				rs.close();
			}
			
			insertHuntFinal(pc);
		} catch (Exception e) {
			lineage.share.System.printf("%s : insertHunt(PcInstance pc)\r\n",
					AutoHuntDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}
	
	/**
	 * 자동사냥 정보 생성
	 * by Jsn_Soft
	 * 2021-02-25
	 */
	static public void insertHuntFinal(PcInstance pc) {
		Connection con = null;
		PreparedStatement st = null;
		jsn_hunt jh = find(pc.getObjectId());
		try {
			con = DatabaseConnection.getLineage();
			// 처리.
			st = con.prepareStatement("INSERT INTO autohunt SET pc_name=?, pc_objid=?, pc_account=?, pc_auto_time=?, x=?, y=?, map=?, is_sell=?, potion=?, potion_percent=?");
			st.setString(1, jh.getPcName());
			st.setLong(2, jh.getPcObjid());
			st.setString(3, jh.getPcAccount());
			st.setLong(4, jh.getPcHuntTime());
			st.setLong(5, jh.getX());
			st.setLong(6, jh.getY());
			st.setInt(7, jh.getMap());
			st.setString(8, jh.isAutoSell() ? "true" : "false");
			st.setString(9, jh.getPotion());
			st.setLong(10, jh.getPotionPercent());
			st.executeUpdate();
			st.close();
			
			readHunt(pc);
		} catch (Exception e) {
			list.remove(jh);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 자동사냥 정보 저장
	 * by Jsn_Soft
	 * 2021-02-25
	 */
	static public void saveTime(PcInstance pc, long time) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			jsn_hunt jh = find(pc.getObjectId());
			jh.setPcHuntTime(jh.getPcHuntTime() + time);
			
			con = DatabaseConnection.getLineage();
			
			st = con.prepareStatement("UPDATE autohunt SET pc_auto_time=? WHERE pc_objid=?");
			// 처리.
			st.setLong(1, jh.getPcHuntTime());
			st.setLong(2, pc.getObjectId());
			st.executeUpdate();
			
			readHunt(pc);
		} catch (Exception e) {
			lineage.share.System.printf("%s : saveTime(PcInstance pc, long time)\r\n",
					AutoHuntDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}
	
	/**
	 * 자동사냥 정보 저장
	 * 현재 위치 저장 매소드
	 * by Jsn_Soft
	 * 2021-02-25
	 */
	static public void saveLocation(PcInstance pc, int x, int y, int map) {
//		Connection con = null;
//		PreparedStatement st = null;
//		try {
			jsn_hunt jh = find(pc.getObjectId());
			jh.setX(x);
			jh.setY(y);
			jh.setMap(map);
			
//			con = DatabaseConnection.getLineage();
//			
//			st = con.prepareStatement("UPDATE autohunt SET x=?, y=?, map=? WHERE pc_objid=?");
//			// 처리.
//			st.setLong(1, jh.getX());
//			st.setLong(2, jh.getY());
//			st.setLong(3, jh.getMap());
//			st.setLong(4, pc.getObjectId());
//			st.executeUpdate();
//			// 이거떄문이구나 왜 일케되있지 제가 말햇잔아요 ㅋㅋ아 예전에 이거  엥 여기 예전에 디비 연결 많이되서 바꾼걸루기억해서
//			// 여기배제했는데 여기에복병이 ㅋㅋㅋㅋ
//			readHunt(pc);
//		} catch (Exception e) {
//			lineage.share.System.printf("%s : saveLocation(PcInstance pc, int x, int y, int map)\r\n",
//					AutoHuntDatabase.class.toString());
//			lineage.share.System.println(e);
//		} finally {
//			DatabaseConnection.close(st);
//		}
	}
	
	/**
	 * 판매 정보 불러옴
	 * by Jsn_Soft
	 * 2021-02-25
	 */
	static public void readSellList(PcInstance pc) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM autohunt_sell WHERE pc_objid=?");
			st.setLong(1, pc.getObjectId());
			rs = st.executeQuery();
			while(rs.next()) {
				Item item = ItemDatabase.find(rs.getString("item_name"));
				if (item != null)
					pc.sell_List.add(item);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : readSellList(PcInstance pc)\r\n",
					AutoHuntDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}
	
	
	/**
	 * 판매 정보 생성
	 * by Jsn_Soft
	 * 2021-02-25
	 */
	static public void saveSellList(PcInstance pc, List<Item> item) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			
			// 전부 삭제
			st = con.prepareStatement("DELETE FROM autohunt_sell WHERE pc_objid=?");
			st.setLong(1, pc.getObjectId());
			st.executeUpdate();
			st.close();
			
			// 다시 넣기
			for (Item i : item) {
				st = con.prepareStatement("INSERT INTO autohunt_sell SET pc_name=?, pc_objid=?, item_name=?");
				st.setString(1, pc.getName());
				st.setLong(2, pc.getObjectId());
				st.setString(3, i.getName());
				st.executeUpdate();
				st.close();
			}
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : saveSellList(PcInstance pc, List<Item> item)\r\n",
					AutoHuntDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	static public List<jsn_hunt> getList() {
		return list;
	}
	
	static public jsn_hunt find(String name) {
		for (jsn_hunt jh : list) {
			if(jh.getPcName() != null && jh.getPcName().equalsIgnoreCase(name)) {
				return jh;
			}
		}
		return null;
	}
	
	static public jsn_hunt findAccount(String account) {
		for (jsn_hunt jh : list) {
			if(jh.getPcAccount() != null && jh.getPcAccount().equalsIgnoreCase(account)) {
				return jh;
			}
		}
		return null;
	}
	
	static public jsn_hunt find(long objid) {
		for (jsn_hunt jh : list) {
			if(jh.getPcObjid() != 0 && jh.getPcObjid() == (objid)) {
				return jh;
			}
		}
		return null;
	}
}
