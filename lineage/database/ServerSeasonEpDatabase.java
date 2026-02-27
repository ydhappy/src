/*
ObjectID 관리범위
	- 최대범위
		: 0 ~ 4294967295
		
	1. PC		1000000			<< 
	2. ITEM		10000000		<< 
	4. OBJECT	100000			<< npc, monster, background
	3. ETC		0				<< 
 */

package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import lineage.share.TimeLine;
import lineage.world.World;

public final class ServerSeasonEpDatabase {
	
	static private int season_ep;		//에피 숫자 진행
	static private String season_boss; // 에피 보스
	static private boolean season_clear; // 보스 클리어 여부
	
	/**
	 * 초기화 함수.
	 * @param con
	 */
	static public void init(Connection con){
		TimeLine.start("ServerSeasonEpDatabase..");
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM server_season_ep");
			rs = st.executeQuery();
			if(rs.next()){		
				season_ep = rs.getInt("season_ep");
				season_boss = rs.getString("season_boss");
				season_clear = rs.getBoolean("season_clear");
				
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", ServerSeasonEpDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		
		TimeLine.end();
	}
	/*
	 * 에피소드 진행 단계 불러오기*/
	public static int getSeasonEp(){
		return season_ep;
	}
	/*에피 보스 불러오기*/
	public static String getSeasonBoss(){
		return season_boss;
	}
	/*에피 클리어 여부 불러오기*/
	public static boolean getSeasonClear(int i){
		if(season_ep==i && season_clear)
			return true;
		else 
			return false;
	}
	
	/*해당 수치로 값을 변경하기*/
	static public void setSeasonClear(Connection con, boolean num){
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("INSERT INTO server_season_ep SET season_clear=?");
			st.setBoolean(1, num);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System
					.printf("%s : insertCharacter(Connection con, int num, boolean sc)\r\n",
							ServerSeasonEpDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}
	

}
