package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage.bean.database.Npc;
import lineage.network.packet.ClientBasePacket;
import lineage.share.TimeLine;
import lineage.util.Util;

public class NpcCraftDatabase {

	static public void init(Connection con){
		TimeLine.start("NpcCraftDatabase..");

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM npc_craft ORDER BY uid");
			rs = st.executeQuery();
			while(rs.next()){
				Npc n = NpcDatabase.findNameid( rs.getString("nameid") );
				if(n != null) {
					//
					String number_list = rs.getString("number_list");
					if(number_list!=null && number_list.length()>0) {
						//
						for(String number : number_list.split(",")) {
							number = number.trim();
							if(number.length() <= 0)
								continue;
							
							n.getCraftList().add( Integer.valueOf(number) );
						}
					} else {
						// 
						byte[] data = Util.StringToByte( rs.getString("list") );
						if(data.length <= 4)
							continue;
						// 08 00 12 07 08 c2 05 10 00 18 00 120708c30510001800120708c60510001800120708c7051000180018001e00
						ClientBasePacket cbp = (ClientBasePacket)ClientBasePacket.clone(null, data, data.length);
						cbp.seek(2);
						while(cbp.isRead(9)) {
							cbp.readC();
							cbp.readC();
							cbp.readC();
							int number = cbp.read4(2);
							cbp.readH();
							cbp.readH();
							
							n.getCraftList().add( number );
						}
					}
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", NpcCraftDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}

		TimeLine.end();
	}

}
