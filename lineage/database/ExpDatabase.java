package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Exp;
import lineage.share.GameSetting;
import lineage.share.Lineage;
import lineage.share.TimeLine;

public final class ExpDatabase {

	static private List<Exp> pool;
	static private List<Exp> list;
	
	static public void init(Connection con){
		TimeLine.start("ExpDatabase..");
		
		pool = new ArrayList<Exp>();
		list = new ArrayList<Exp>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM exp");
			rs = st.executeQuery();
			while(rs.next()){
				Exp e = new Exp();
				e.setLevel( rs.getInt(1) );
				e.setExp( rs.getDouble(2) );
				e.setBonus( rs.getDouble(3) );
				
				list.add(e);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", ExpDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		
		TimeLine.end();
	}

	/**
	 * 레벨에 해당하는 경험치 정보 클레스 리턴.
	 */
	static public Exp find(final int level){
		for( Exp e : list ){
			if(e.getLevel() == level)
				return e;
		}
		return null;
	}
	
	static public Exp getPool(){
		Exp e = null;
		synchronized (pool) {
			if(Lineage.memory_recycle && pool.size()>0) {
				e = pool.get(pool.size()-1);
				pool.remove(pool.size()-1);
			} else
				e = new Exp();
		}
		return e;
	}
	
	static public void setPool(Exp e){
		e.close();
		if(!Lineage.memory_recycle)
			return;
		synchronized (pool) {
			if(!pool.contains(e))
				pool.add(e);
		}
	}
	
	static public int getSize(){
		return list.size();
	}
	
	static public int getPoolSize(){
		return pool.size();
	}
	
	/**
	 * 현재의 레벨로부터, 경험치의 페널티 레이트를 요구한다
	 * 
	 * @param level
	 *            현재의 레벨
	 * @return 요구된 경험치의 페널티 레이트
	 */
	public static double getPenaltyRate(int level) {
		if (level < 50) {
			return 1.0;
		}
		double expPenalty = 1.0;
		expPenalty = 1.0 / _expPenalty[level - 50];

		return expPenalty;
	}
	
	private static final int _expPenalty[] = { GameSetting.LV50_EXP,
			GameSetting.LV51_EXP, GameSetting.LV52_EXP, GameSetting.LV53_EXP, GameSetting.LV54_EXP,
			GameSetting.LV55_EXP, GameSetting.LV56_EXP, GameSetting.LV57_EXP, GameSetting.LV58_EXP,
			GameSetting.LV59_EXP, GameSetting.LV60_EXP, GameSetting.LV61_EXP, GameSetting.LV62_EXP,
			GameSetting.LV63_EXP, GameSetting.LV64_EXP, GameSetting.LV65_EXP, GameSetting.LV66_EXP,
			GameSetting.LV67_EXP, GameSetting.LV68_EXP, GameSetting.LV69_EXP, GameSetting.LV70_EXP,
			GameSetting.LV71_EXP, GameSetting.LV72_EXP, GameSetting.LV73_EXP, GameSetting.LV74_EXP,
			GameSetting.LV75_EXP, GameSetting.LV76_EXP, GameSetting.LV77_EXP, GameSetting.LV78_EXP,
			GameSetting.LV79_EXP, GameSetting.LV80_EXP, GameSetting.LV81_EXP, GameSetting.LV82_EXP,
			GameSetting.LV83_EXP, GameSetting.LV84_EXP, GameSetting.LV85_EXP, GameSetting.LV86_EXP,
			GameSetting.LV87_EXP, GameSetting.LV88_EXP, GameSetting.LV89_EXP, GameSetting.LV90_EXP,
			GameSetting.LV91_EXP, GameSetting.LV92_EXP, GameSetting.LV93_EXP, GameSetting.LV94_EXP,
			GameSetting.LV95_EXP, GameSetting.LV96_EXP, GameSetting.LV97_EXP, GameSetting.LV98_EXP,
			GameSetting.LV99_EXP };
	
	
}
