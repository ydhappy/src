package lineage.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.database.DatabaseConnection;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Desc;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.PcInstance;

public class SpTools {
	
	/**
	 * 본섭에서 읽어온 패킷을 자동으로 디비화 해줌.
	 * 	: npc_shop_packet > npc_shop
	 */
	static public void toNpcShopUpdate() {

		//
		TimeLine.start("SpTools.toNpcShopUpdate..");
		//
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM npc_shop_packet");
			rs = st.executeQuery();
			while(rs.next()) {
				String nameid = rs.getString("npc_name_id");
				String name = Desc.find(nameid);
				byte[] packet = Util.StringToByte( rs.getString("list") );
				
				ClientBasePacket cbp = (ClientBasePacket)ClientBasePacket.clone(BasePacketPooling.getPool(ClientBasePacket.class), packet, packet.length);
				cbp.seek(0);
				int count = cbp.readH();
				for(int i=0 ; i<count ; ++i) {
					long uid = cbp.readD();
					int invGfx = cbp.readH();
					long price = cbp.readD();
					String itemName = cbp.readS();
					int itemCount = 1;
					if(itemName.indexOf("(") > 0) {
						int pos = itemName.indexOf("(");
						itemCount = Integer.valueOf( itemName.substring(pos+1, itemName.indexOf(")")) );
						itemName = itemName.substring(0, pos);
					}
					int optionSize = cbp.readC();
					byte[] options = cbp.readBB(optionSize);
					
					PreparedStatement stt = con.prepareStatement("INSERT INTO npc_shop SET uid=?, name=?, itemname=?, itemcount=?, price=?");
					stt.setInt(1, i+1);
					stt.setString(2, name);
					stt.setString(3, Desc.find(itemName));
					stt.setInt(4, itemCount);
					stt.setLong(5, price);
					stt.executeUpdate();
					stt.close();
				}
				BasePacketPooling.setPool(cbp);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : toNpcShopUpdate()\r\n", SpTools.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		//
		TimeLine.end();
	}
	
	/**
	 * 케릭터 인벤토리 디비가 변경됨에 따라 자동변환 툴이 필요하게 되었음.
	 * 그에 기능을 수행.
	 * 대상 테이블 : characters_inventory_backup
	 * 갱신 테이블 : characters_inventory
	 */
	static public void toCharactersInventoryUpdate() {
		//
		TimeLine.start("SpTools.toCharactersInventoryUpdate..");
		//
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//
			Map<Long, List<StringBuffer>> list = new HashMap<Long, List<StringBuffer>>();
			Map<Long, String> list_name = new HashMap<Long, String>();
			//
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters_inventory_backup ORDER BY cha_objId");
			rs = st.executeQuery();
			while(rs.next()) {
				//
				List<StringBuffer> db = list.get(rs.getLong("cha_objId"));
				if(db == null) {
					db = new ArrayList<StringBuffer>();
					db.add(new StringBuffer());	// 0 weapon
					db.add(new StringBuffer());	// 1 armor
					db.add(new StringBuffer());	// 2 etc
					list.put(rs.getLong("cha_objId"), db);
					list_name.put(rs.getLong("cha_objId"), rs.getString("cha_name"));
				}
				//
				StringBuffer field = null;
				if(rs.getString("구분1").equalsIgnoreCase("weapon"))
					field = db.get(0);
				else if(rs.getString("구분1").equalsIgnoreCase("armor"))
					field = db.get(1);
				else
					field = db.get(2);
				field.append(String.format(
					"%d,%s,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%s,%s,%s,%s,\r\n",
					rs.getLong("objId"),
					rs.getString("name"),
					rs.getLong("count"),
					rs.getInt("quantity"),
					rs.getInt("en"),
					rs.getInt("equipped"),
					rs.getInt("definite"),
					rs.getInt("bress"),
					rs.getInt("durability"),
					rs.getInt("nowtime"),
					rs.getLong("pet_objid"),
					rs.getLong("inn_key"),
					rs.getLong("letter_uid"),
					rs.getString("slimerace"),
					rs.getString("구분1"),
					rs.getString("구분2"),
					rs.getString("options")
				));
			}
			DatabaseConnection.close(st, rs);
			//
			for(Long objId : list.keySet()) {
				List<StringBuffer> db = list.get(objId);

				st = con.prepareStatement("INSERT INTO characters_inventory SET cha_objId=?, cha_name=?, weapon=?, armor=?, etc=?");
				st.setLong(1, objId);
				st.setString(2, list_name.get(objId));
				st.setString(3, db.get(0).toString());
				st.setString(4, db.get(1).toString());
				st.setString(5, db.get(2).toString());
				st.executeUpdate();
				st.close();
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : toCharactersInventoryUpdate()\r\n", SpTools.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		//
		TimeLine.end();
	}

	/**
	 * sp client 에서 동적으로 함수를 호출.
	 * 	: 메세지 표현용으로 사용됨.
	 * @param type
	 * @param message
	 */
	static public void toMessage(String type, String message) {
		//
		boolean world = type.equalsIgnoreCase("world") || type.equalsIgnoreCase("all");
		boolean gui = type.equalsIgnoreCase("gui") || type.equalsIgnoreCase("all");
		//
		if(world) {
			for(PcInstance pc : World.getPcList()) {
				try {
					ChattingController.toChatting(pc, message, 20);
				} catch (Exception e) {
				}
			}
		} 
		if(gui) {
			try {
				lineage.share.System.println(message);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	static public int getServerVersion() {
		return Lineage.server_version;
	}
}
