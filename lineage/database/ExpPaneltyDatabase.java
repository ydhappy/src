package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.ExpPanelty;
import lineage.bean.database.ItemBundle;
import lineage.share.Lineage;
import lineage.share.System;
import lineage.share.TimeLine;

public final class ExpPaneltyDatabase
{
	private static List<ExpPanelty> list;

	public static void init(Connection con)
	{
		TimeLine.start("ExpPaneltyDatabase..");
		list = new ArrayList<ExpPanelty>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try
		{
				st = con.prepareStatement("SELECT * FROM exp_panelty");
			
			rs = st.executeQuery();
			while (rs.next())
			{
				ExpPanelty ep = new ExpPanelty();
				ep.setLevel(rs.getInt(1));
				ep.setPanelty(rs.getInt(2));
				list.add(ep);
			}
		}
		catch (Exception localException)
		{
			System.printf("%s : init(Connection con)\r\n", new Object[] { ExpPaneltyDatabase.class.toString() });
			System.println(localException);
		}
		finally
		{
			DatabaseConnection.close(st, rs);
		}
		TimeLine.end();
	}

	public static ExpPanelty find(int i)
	{
		for (ExpPanelty exp : list) {
			if (exp.getLevel() == i)
				return exp;
		}
		return null;
	}
	
	/**
	 * 
	 * @param
	 * @return
	 * 2021-0-03
	 */
	static public void reload() {
		TimeLine.start("ExpPaneltyDatabase 리로드 완료-");
		
		list.clear();
		
		PreparedStatement st = null;
		ResultSet rs = null;
		Connection con = null;
		
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM exp_panelty");
			rs = st.executeQuery();
			while(rs.next()){
				ExpPanelty ep = new ExpPanelty();
				ep.setLevel(rs.getInt(1));
				ep.setPanelty(rs.getInt(2));
			
				list.add(ep);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : reload()\r\n", ExpPaneltyDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		TimeLine.end();
	}
}

/* Location:           D:\orim.jar
 * Qualified Name:     lineage.database.ExpPaneltyDatabase
 * JD-Core Version:    0.6.0
 */
