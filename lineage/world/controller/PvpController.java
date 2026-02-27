package lineage.world.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.lineage.Board;
import lineage.bean.lineage.Pvp;
import lineage.database.DatabaseConnection;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_BoardView;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.object.instance.PcInstance;

public class PvpController {

	//
	static private Map<Long, List<Pvp>> list;
	static private int currentUid;
	//
	static private Map<Long, Integer> listKill;
	static private Map<Long, Integer> listDead;
	//
	static private List<Map.Entry<Long, Integer>> rankKill;
	static private List<Map.Entry<Long, Integer>> rankDead;
	// Book 클레스 재사용을 위한것
	static private List<Pvp> pool;
	
	static public void init(Connection con) {
		//
		TimeLine.start("PvpController..");
		
		//
		if(list == null) {
			list = new HashMap<Long, List<Pvp>>();
			pool = new ArrayList<Pvp>();
			listKill = new HashMap<Long, Integer>();
			listDead = new HashMap<Long, Integer>();
			rankKill = new ArrayList<Map.Entry<Long, Integer>>();
			rankDead = new ArrayList<Map.Entry<Long, Integer>>();
		}
		//
		synchronized (list) {
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				st = con.prepareStatement("SELECT * FROM characters_pvp ORDER BY uid ASC");
				rs = st.executeQuery();
				while(rs.next()) {
					// 그냥 이거 쓰지 마시구요.... 하나 짜드릴게요....
					List<Pvp> db = find(rs.getLong("objectId"));
					//
					Pvp pvp = getPool();
					pvp.setUid(rs.getInt("uid"));
					pvp.setObjectId(rs.getLong("objectId"));
					pvp.setName(rs.getString("name"));
					pvp.setKill(rs.getInt("is_kill") == 1);
					pvp.setDead(rs.getInt("is_dead") == 1);
					pvp.setTargetObjectId(rs.getLong("target_objectId"));
					pvp.setTargetName(rs.getString("target_name"));
					pvp.setPvpDate(rs.getTimestamp("pvp_date").getTime());
					db.add( pvp );
					//
					currentUid = rs.getInt("uid");
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : init(Connection con)\r\n", PvpController.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(st, rs);
			}
		}
		//
		toTimer(0);
		getRank(null, true);
		
		TimeLine.end();
	}
	
	static public void close(Connection con) {
		//
		synchronized (list) {
			for(Long key : list.keySet()) {
				List<Pvp> db = list.get(key);
				for(Pvp pvp : db) {
					PreparedStatement st = null;
					ResultSet rs = null;
					try {
						st = con.prepareStatement("SELECT * FROM characters_pvp WHERE uid=?");
						st.setInt(1, pvp.getUid());
						rs = st.executeQuery();
						if(!rs.next()) {
							PreparedStatement st1 = con.prepareStatement("INSERT INTO characters_pvp SET uid=?, objectId=?, name=?, is_kill=?, is_dead=?, target_objectId=?, target_name=?, pvp_date=?");
							st1.setInt(1, pvp.getUid());
							st1.setLong(2, pvp.getObjectId());
							st1.setString(3, pvp.getName());
							st1.setInt(4, pvp.isKill() ? 1 : 0);
							st1.setInt(5, pvp.isDead() ? 1 : 0);
							st1.setLong(6, pvp.getTargetObjectId());
							st1.setString(7, pvp.getTargetName());
							st1.setTimestamp(8, new Timestamp(pvp.getPvpDate()));
							st1.executeUpdate();
							st1.close();
						}
					} catch (Exception e) {
						lineage.share.System.printf("%s : close(Connection con)\r\n", PvpController.class.toString());
						lineage.share.System.println(e);
					} finally {
						DatabaseConnection.close(st, rs);
					}
					
					setPool(pvp);
				}
				db.clear();
			}
			list.clear();
		}
	}
	
	static private Pvp getPool() {
		Pvp p = null;
		synchronized (pool) {
			if(Lineage.memory_recycle && pool.size()>0){
				p = pool.get(0);
				pool.remove(0);
			}else{
				p = new Pvp();
			}
		}
		return p;
	}
	
	static private void setPool(Pvp p) {
		p.close();
		if(!Lineage.memory_recycle)
			return;
		synchronized (pool) {
			pool.add(p);
		}
	}
	
	static private List<Pvp> find(long objectId) {
		List<Pvp> db = list.get(objectId);
		if(db == null) {
			db = new ArrayList<Pvp>();
			list.put(objectId, db);
		}
		return db;
	}
	
	/**
	 * 주기적으로 호출됨.
	 * @param time
	 */
	static public void toTimer(long time) {
		// 1분마다 한번씩 갱신.
		time = Math.round(time * 0.001);
		if(time%60 != 0)
			return;
		//
		synchronized (list) {
			for(Long key : list.keySet()) {
				int kill = 0;
				int dead = 0;
				for(Pvp pvp : list.get(key)) {
					if(pvp.isKill())
						kill += 1;
					if(pvp.isDead())
						dead += 1;
				}
				listKill.put(key, kill);
				listDead.put(key, dead);
			}
		}
	}
	

	
	/**
	 * pvp 초기화 함수
	 * @param pc
	 */
	static public void clearPvp(PcInstance pc) {
		synchronized (list) {
			List<Pvp> db = find(pc.getObjectId());
			if(db != null) {
				for(Pvp pvp : db)
					setPool(pvp);
				db.clear();
			}
			listKill.remove(pc.getObjectId());
			listDead.remove(pc.getObjectId());
		}
		
		Connection con = null;
		PreparedStatement st = null; 
		try {			
			toTimer(0);
						
			con = DatabaseConnection.getLineage();
			
			// 삭제하기전에 업데이트
			close(con);
			
			st = con.prepareStatement("DELETE FROM characters_pvp WHERE objectId=?");
			st.setLong(1, pc.getObjectId());
			st.executeUpdate();
			st.close();
						
			init(con);
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", PvpController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	
	/**
	 * pvp시 승리햇을경우 호출됨
	 * @param pc		: 승리자
	 * @param target	: 패배자
	 */
	static public void updatePvpKill(PcInstance pc, PcInstance target) {
		synchronized (list) {
			//
			Pvp pvp = getPool();
			pvp.setUid(++currentUid);
			pvp.setObjectId(pc.getObjectId());
			pvp.setName(pc.getName());
			pvp.setKill(true);
			pvp.setTargetObjectId(target.getObjectId());
			pvp.setTargetName(target.getName());
			pvp.setPvpDate(System.currentTimeMillis());
			//
			find(pc.getObjectId()).add( pvp );
		}
	}

	/**
	 * pvp시 패배했을때 호출됨.
	 * @param pc		: 패배자
	 * @param target	: 승리자
	 */
	static public void updatePvpDead(PcInstance pc, PcInstance target) {
		synchronized (list) {
			//
			Pvp pvp = getPool();
			pvp.setUid(++currentUid);
			pvp.setObjectId(pc.getObjectId());
			pvp.setName(pc.getName());
			pvp.setDead(true);
			pvp.setTargetObjectId(target.getObjectId());
			pvp.setTargetName(target.getName());
			pvp.setPvpDate(System.currentTimeMillis());
			//
			find(pc.getObjectId()).add( pvp );
		}
	}

	/**
	 * pvp시 죽인 횟수 리턴함.
	 * @param pc
	 * @return
	 */
	static public int getPvpKill(PcInstance pc) {
		synchronized (list) {
			Integer cnt = listKill.get(pc.getObjectId());
			return cnt==null ? 0 : cnt;
		}
	}

	/**
	 * pvp시 죽은 횟수 리턴함.
	 * @param pc
	 * @return
	 */
	static public int getPvpDead(PcInstance pc) {
		synchronized (list) {
			Integer cnt = listDead.get(pc.getObjectId());
			return cnt==null ? 0 : cnt;
		}
	}
	

	
	/**
	 * pvp 랭킹값을 리턴함.
	 * @param pc		: 확인할 객체
	 * @param kill		: 킬링수에 랭킹인가? 데스수에 랭킹인가?
	 * @return			: 순위 리턴.
	 */
	static public int getRank(PcInstance pc, boolean kill) {
		synchronized (list) {
			//
			List<Map.Entry<Long, Integer>> entries = null;
			if(kill) {
				rankKill.clear();
				rankKill.addAll( listKill.entrySet() );
				entries = rankKill;
			} else {
				rankDead.clear();
				rankDead.addAll( listDead.entrySet() );
				entries = rankDead;
			}
			Collections.sort(entries, new Comparator<Map.Entry<Long, Integer>>() {
				public int compare(Map.Entry<Long, Integer> a, Map.Entry<Long, Integer> b) {
					return b.getValue().compareTo(a.getValue());
				}
			});
			//
			if(pc == null)
				return 0;
			//
			boolean find = false;
			int rank = 0;
			int lastValue = 0;
		    for(int i=0 ; i<entries.size() ; ++i) {
		    	Map.Entry<Long, Integer> db = entries.get(i);
		    	if(lastValue==0 || (db.getValue()>0 && lastValue!=db.getValue())) {
		    		rank += 1;
		    		lastValue = db.getValue();
		    	}
		    	if(db.getValue()>0 && db.getKey()==pc.getObjectId()) {
		    		find = true;
		    		break;
		    	}
		    }
		    //
			return find ? rank : 0;
		}
	}
	
	static public void toView(PcInstance pc) {
		synchronized (list) {
			Board b = BoardController.getPool();
			StringBuffer sb = new StringBuffer();
			//
			b.setUid( 1 );
			b.setName( ServerDatabase.getName() );
			b.setSubject( "PVP 랭킹 정보" );
			b.setDays( java.lang.System.currentTimeMillis() );
			//
			int rank = 0;
			int lastValue = 0;
			for(int i=0 ; i<rankKill.size() && rank<=10 ; ++i) {
		    	Map.Entry<Long, Integer> db = rankKill.get(i);
		    	if(lastValue==0 || (db.getValue()>0 && lastValue!=db.getValue())) {
		    		rank += 1;
		    		lastValue = db.getValue();
		    	}
		    	List<Pvp> pvp_db = list.get(db.getKey());
				sb.append( String.format(rank+"위 %s\r\n", pvp_db.get(0).getName()) );
			}
			b.setMemo( sb.toString() );
			//
			pc.toSender(S_BoardView.clone(BasePacketPooling.getPool(S_BoardView.class), b));
			//
			BoardController.setPool( b );
		}
	}
	
	/**
	 * 원하는 랭커에 객체값을 리턴함.
	 * @param rank
	 * @return
	 */
	static public long getTopRank(int find_rank, boolean kill) {
		synchronized (list) {
			//
			List<Map.Entry<Long, Integer>> entries = kill ? rankKill : rankDead;
			//
			boolean find = false;
			int rank = 0;
			int lastValue = 0;
			long objectId = 0;
		    for(int i=0 ; i<entries.size() ; ++i) {
		    	Map.Entry<Long, Integer> db = entries.get(i);
		    	if(lastValue==0 || (db.getValue()>0 && lastValue!=db.getValue())) {
		    		rank += 1;
		    		lastValue = db.getValue();
		    	}
		    	if(db.getValue()>0 && rank==find_rank) {
		    		find = true;
		    		objectId = db.getKey();
		    		break;
		    	}
		    }
		    //
		    return find ? objectId : 0;
		}
	}
	static public void removeAll() {
		synchronized (list) {
			list.clear();
//			listKill.clear();
//			listDead.clear();
		}
	}
	
	
	static public void GmPvpKill(PcInstance pc, String killcount, long objid) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			int a = Integer.valueOf(killcount);
			
			if(PvpController.getPvpKill(pc) < a){
				ChattingController.toChatting(pc, "삭제하려는 킬수가 해당유저 킬수보다 많습니다.", 20);
				return;
			}
			
			toTimer(0);
						
			con = DatabaseConnection.getLineage();
			
			// 삭제하기전에 업데이트
			close(con);
			
			st = con.prepareStatement("DELETE FROM characters_pvp WHERE objectId=? ORDER BY uid LIMIT " + killcount);
			st.setLong(1, objid);
			st.executeUpdate();
			st.close();
			
			ChattingController.toChatting(pc, killcount + "만큼의 킬수를 삭제하였습니다.", 20);
			
			init(con);
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", PvpController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
}
