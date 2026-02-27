package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.ItemTeleport;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectLock;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;

public class ItemTeleportDatabase {

	static private List<ItemTeleport> list;

	/**
	 * 초기화 처리 함수.
	 */
	static public void init(Connection con) {
		TimeLine.start("ItemTeleport..");

		if (list == null)
			list = new ArrayList<ItemTeleport>();
		synchronized (list) {
			list.clear();
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				st = con.prepareStatement("SELECT * FROM item_teleport");
				rs = st.executeQuery();
				while (rs.next()) {
					ItemTeleport i = new ItemTeleport();
					i.setUid(rs.getInt("uid"));
					i.setName(rs.getString("name"));
					i.setX(rs.getInt("goto_x"));
					i.setY(rs.getInt("goto_y"));
					i.setMap(rs.getInt("goto_map"));
					i.setRandomLoc(rs.getString("is_random").equalsIgnoreCase("true"));
					i.appendLocation(rs.getString("goto_1"));
					i.appendLocation(rs.getString("goto_2"));
					i.appendLocation(rs.getString("goto_3"));
					i.appendLocation(rs.getString("goto_4"));
					i.appendLocation(rs.getString("goto_5"));
					i.appendLocation(rs.getString("goto_6"));
					i.setLevel(rs.getInt("if_level"));
					i.setClassType(rs.getInt("if_class"));
					i.setRemove(rs.getInt("if_remove") == 1);

					list.add(i);
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : init(Connection con)\r\n", ItemTeleportDatabase.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(st, rs);
			}
		}

		TimeLine.end();
	}

	static public ItemTeleport find(int uid) {
		synchronized (list) {
			for (ItemTeleport it : list) {
				if (it.getUid() == uid)
					return it;
			}
			return null;
		}
	}

	static public boolean toTeleport(ItemTeleport it, object o, boolean message) {
		if (it == null || o == null) {
			if (o != null)
				o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
			return false;
		}
		if(o.getMap()==57 || o.getMap()==70 || o.getMap()==5124 || o.getMap()==5001 || o.getMap()==621 || o.getMap()==99
				|| o.getMap()==666 || o.getMap()==800 || o.getMap()==340 || o.getMap()==370 || o.getMap()==350
				|| o.getMap()==780 || o.getMap()==781 || o.getMap()==782){
			ChattingController.toChatting(o, "여기에서 사용할 수 없습니다.", 20);
			o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
			return false;
		}

		if (PluginController.init(ItemTeleportDatabase.class, "toTeleport", it, o, message) != null)
			return false;

		// 클레스 확인.
		if ((it.getClassType() & Lineage.getClassType(o.getClassType())) == 0) {
			if (message)
				ChattingController.toChatting(o, "당신의 클래스는 이 아이템을 사용할 수 없습니다.", 20);
			o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
			return false;
		}

		// 레벨 확인.
		if (it.getLevel() != 0 && it.getLevel() > o.getLevel()) {
			if (message)
				ChattingController.toChatting(o, String.format(
						"%d 레벨부터 사용 가능합니다.", it.getLevel()), 20);
			o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
			return false;
		}

		int x, y, map = 0;

		if (it.isRandomLoc()) {
			lineage.bean.database.ItemTeleport.Location l = null;

			// 처리
			l = it.getListGoto().get(Util.random(0, it.getListGoto().size() - 1));
			x = l.x;
			y = l.y;
			map = l.map;
		} else {
			x = it.getX();
			y = it.getY();
			map = it.getMap();
		}
		
		if (Lineage.server_version >= 250)
			o.toPotal(x, y, map);
		else
			o.toTeleport(x, y, map, true);

		return true;
	}
}
