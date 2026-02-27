package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.ServerMessage;
import lineage.share.System;
import lineage.share.TimeLine;

public class ServerMessageDatabase {
	private static List<ServerMessage> list;

	public static void init(Connection paramConnection) {
		TimeLine.start("ServerMessageDatabase..");
		list = new ArrayList<ServerMessage>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = paramConnection
					.prepareStatement("SELECT id, message FROM server_message");
			rs = st.executeQuery();
			while (rs.next()) {
				ServerMessage localServerMessage = new ServerMessage();
				localServerMessage.setId(rs.getInt("id"));
				localServerMessage.setMessage(rs.getString("message"));
				list.add(localServerMessage);
			}
		} catch (Exception localException) {
			System.printf("%s : void init(Connection con)\r\n",
					new Object[] { ServerMessageDatabase.class.toString() });
			System.println(localException);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		TimeLine.end();
	}

	public static ServerMessage find(int paramInt) {
		for (ServerMessage localServerMessage : list) {
			if (localServerMessage.getId() == paramInt)
				return localServerMessage;
		}
		return new ServerMessage();
	}
}
