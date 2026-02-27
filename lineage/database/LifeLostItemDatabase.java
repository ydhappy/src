package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.LifeLostItem;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.World;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public final class LifeLostItemDatabase {
	static private List<LifeLostItem> list;
	
	static public void init(Connection con) {
		TimeLine.start("LifeLostItemDatabase..");

		list = new ArrayList<LifeLostItem>();
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = con.prepareStatement("SELECT * FROM life_lost_item");
			rs = st.executeQuery();

			while (rs.next()) {
				LifeLostItem li= new LifeLostItem();
				li.setItem(rs.getString("기운을 잃은 아이템"));
				li.setNomalChance(Double.valueOf(rs.getString("일반 확률").trim()) * 0.01);
				li.setBressChance(Double.valueOf(rs.getString("축복 확률").trim()) * 0.01);
				li.setBressContinueChance(Double.valueOf(rs.getString("축복 보존 확률").trim()) * 0.01);
				li.setItemName(rs.getString("아이템"));
				li.setBress(rs.getInt("축여부"));
				li.setEn(rs.getInt("인첸트"));
				li.setMinCount(rs.getLong("최소 수량"));
				li.setMaxCount(rs.getLong("최대 수량"));
				list.add(li);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", LifeLostItemDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}

		TimeLine.end();
	}
	
	static public void reload() {
		synchronized (list) {
			TimeLine.start("life_lost_item 테이블 리로드 - ");

			PreparedStatement st = null;
			ResultSet rs = null;
			Connection con = null;
			
			list.clear();

			try {
				con = DatabaseConnection.getLineage();
				st = con.prepareStatement("SELECT * FROM life_lost_item");
				rs = st.executeQuery();

				while (rs.next()) {
					LifeLostItem li= new LifeLostItem();
					li.setItem(rs.getString("기운을 잃은 아이템"));
					li.setNomalChance(Double.valueOf(rs.getString("일반 확률").trim()) * 0.01);
					li.setBressChance(Double.valueOf(rs.getString("축복 확률").trim()) * 0.01);
					li.setBressContinueChance(Double.valueOf(rs.getString("축복 보존 확률").trim()) * 0.01);
					li.setItemName(rs.getString("아이템"));
					li.setBress(rs.getInt("축여부"));
					li.setEn(rs.getInt("인첸트"));
					li.setMinCount(rs.getLong("최소 수량"));
					li.setMaxCount(rs.getLong("최대 수량"));
					list.add(li);
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : reload()\r\n", LifeLostItemDatabase.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st, rs);
			}

			TimeLine.end();
		}
	}
	
	static public List<LifeLostItem> getList() {
		synchronized (list) {
			return new ArrayList<LifeLostItem>(list);
		}
	}
	
	/**
	 * 특정 아이템 획득시 전체 메세지 여부.
	 * 2020-11-29
	 * by connector12@nate.com
	 */
	static public void sendMessageLife(object o, String item) {
		if (Lineage.is_item_drop_msg_life && o != null && o instanceof PcInstance && item != null) {
			if (Lineage.is_item_drop_msg_name) {
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, Lineage.CHATTING_MODE_MESSAGE, String.format("어느 아덴 용사가 %s에 생명을 부여했습니다.", item)));
			} else {
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, Lineage.CHATTING_MODE_MESSAGE, String.format("%s님이 %s에 생명을 부여했습니다.", o.getName(), item)));
			}
		}
	}
}
