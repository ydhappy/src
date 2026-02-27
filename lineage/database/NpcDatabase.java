package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lineage.bean.database.Npc;
import lineage.share.Lineage;
import lineage.share.TimeLine;

public final class NpcDatabase {

	static private List<Npc> list;

	static public void init(Connection con){
		TimeLine.start("NpcDatabase..");

		if(list == null)
			list = new ArrayList<Npc>();
		synchronized (list) {
			list.clear();
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				st = con.prepareStatement("SELECT * FROM npc");
				rs = st.executeQuery();
				while(rs.next()){
					Npc n = new Npc();
					n.setNpcUid( rs.getInt("uid") );
					n.setName( rs.getString("name") );
					n.setType( rs.getString("type") );
					n.setNameId( rs.getString("nameid") );
					n.setGfx( rs.getInt("gfxid") );
					n.setGfxMode( rs.getInt("gfxMode") );
					n.setHp( rs.getInt("hp") );
					n.setLawful( rs.getInt("lawful") );
					n.setLight( rs.getInt("light") );
					n.setAi( rs.getString("ai").equalsIgnoreCase("true") );
					n.setAreaAtk( rs.getInt("areaatk") );
					n.setArrowGfx( rs.getInt("arrowGfx") );
					// 라우풀 값 확인.
					if(n.getLawful()<32768)
						n.setLawful( n.getLawful() + Lineage.NEUTRAL );
					
					try {
						StringBuffer sb = new StringBuffer();
						StringTokenizer stt = new StringTokenizer(rs.getString("nameid"), " $ ");
						while(stt.hasMoreTokens())
							sb.append(stt.nextToken());
						n.setNameIdNumber( Integer.valueOf(sb.toString().trim()) );
					} catch (Exception e) { 
					}
					try {
						n.setMsgAtkTime( init(rs.getString("msg_atk"), n.getMsgAtk()) * 1000 );
						n.setMsgDieTime( init(rs.getString("msg_die"), n.getMsgDie()) * 1000 );
						n.setMsgSpawnTime( init(rs.getString("msg_spawn"), n.getMsgSpawn()) * 1000 );
						n.setMsgEscapeTime( init(rs.getString("msg_escape"), n.getMsgEscape()) * 1000 );
						n.setMsgWalkTime( init(rs.getString("msg_walk"), n.getMsgWalk()) * 1000 );
					} catch (Exception e) {
					}
					list.add( n );
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : init(Connection con)\r\n", NpcDatabase.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(st, rs);
			}
		}

		TimeLine.end();
	}

	/**
	 * 중복 코드 방지용.
	 * @param db_msg
	 * @param m
	 */
	static public int init(String db_msg, List<String> list){
		int time = -1;
		StringTokenizer stt = new StringTokenizer(db_msg, "|");
		if(stt.countTokens() > 0){
			try {
				time = Integer.valueOf(stt.nextToken());
				while(stt.hasMoreTokens()){
					String msg = stt.nextToken().trim();
					if(msg.length()>0)
						list.add(msg);
				}
			} catch (Exception e) {
				return -1;
			}
		}
		return time;
	}
	
	/**
	 * 이름으로 원하는 객체 찾기.
	 * @param name
	 * @return
	 */
	static public Npc find(String name) {
		synchronized (list) {
			for(Npc n : list){
				if(n.getName().equalsIgnoreCase(name))
					return n;
			}
			return null;
		}
	}
	
	static public Npc find2(String name){
		for(Npc n : list){
			if(n.getName().replace(" ", "").equalsIgnoreCase(name))
				return n;
		}
		return null;
	}
	
	static public Npc findNameid(String nameid){
		synchronized (list) {
			for(Npc n : list){
				if(n.getNameId().equalsIgnoreCase(nameid))
					return n;
			}
			return null;
		}
	}
	
	static public int getSize(){
		return list.size();
	}
	
	static public List<Npc> getList(){
		synchronized (list) {
			return new ArrayList<Npc>(list);
		}
	}
	
	/*
	 * 엔피씨 uid로 원하는 객체 찾기
	 * by - 초롱
	 */
	static public Npc find(int id) {
		synchronized (list) {
			for(Npc n : list){
				if(n.getNpcUid() == id)
					return n;
			}
			return null;
		}
	}
	
	/*
	 * 엔피씨 이름으로로 
	 * 엔피씨 uid 찾기
	 * by - 초롱
	 */
	static public int findNpcUid(String id) {
		synchronized (list) {
			int npcid = 0;
			for(Npc n : list){
				if(n.getName().equalsIgnoreCase("id"))
					npcid = n.getNpcUid();
					return npcid;
			}
			return 0;
		}
	}
	
	/*
	 * 엔피씨 이름으로로 
	 * 엔피씨 존재여부 찾기
	 * by - 초롱
	 */
	static public String findNpcUidBy(String id) {
		synchronized (list) {
			String npcid = null;
			for(Npc n : list){
				if(n.getName().replace(" ", "").equalsIgnoreCase(id)){
					npcid = id;
					break;
				}
			}
			return npcid;
		}
	}
}
