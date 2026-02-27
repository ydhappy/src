package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConnectorDatabase {

	static public void append(final String ip) {
		//
		if(isIp(ip))
			return;
		//
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement( "INSERT INTO _connector_list SET ip=?, register_date=?" );
			st.setString(1, ip);
			st.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
			st.executeUpdate();
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	static public void remove(final String ip) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement( "DELETE FROM _connector_list WHERE ip=?" );
			st.setString(1, ip);
			st.executeUpdate();
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	public static boolean isIp(final String ip) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM _connector_list WHERE ip=?");
			st.setString(1, ip);
			rs = st.executeQuery();
			return rs.next();
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return false;
	}
	
}
