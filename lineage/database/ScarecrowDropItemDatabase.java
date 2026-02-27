package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import lineage.bean.database.Item;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public final class ScarecrowDropItemDatabase {
	private static ScarecrowDropItemDatabase _instance;
	public static ScarecrowDropItemDatabase getInstance() {
		if (_instance == null) {
			_instance = new ScarecrowDropItemDatabase();
		}
		return _instance;
	}

	private ScarecrowDropItemDatabase() {
		init();
	}
	
	private ArrayList<ScarecrowDropItem> _list = new ArrayList<ScarecrowDropItem>();

	/**
	 * @param con
	 * @param uid
	 * @return
	 */
	private void init(){

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			pstm = con.prepareStatement("SELECT * FROM scarecrow_dropitem");
			rs = pstm.executeQuery();
			String name;
			int min, max, ch;
			while(rs.next()) {
				ScarecrowDropItem drop = new ScarecrowDropItem();
				name = rs.getString("item_name");
				min = rs.getInt("drop_min");
				max = rs.getInt("drop_max");
				ch = rs.getInt("chance");
				drop.DropItem(name, min, max, ch);
				_list.add(drop);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", ScarecrowDropItemDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, pstm, rs);
		}
	}
	
	public static void reload() {
		ScarecrowDropItemDatabase oldInstance = _instance;
		_instance = new ScarecrowDropItemDatabase();
		oldInstance._list.clear();
	}
	
	
	static public class ScarecrowDropItem {
		private String itemname;
		private int minCount = 0;
		private int maxCount = 0;
		private int chance = 0;
		
		public void DropItem(String name, int min, int max, int ch) {
			this.itemname = name;
			this.minCount = min;
			this.maxCount = max;
			this.chance = ch;
		}
		
		public String getItemName() { return itemname; }
		public int getMin() { return minCount; }
		public int getMax() { return maxCount; }
		public int getChance() { return chance; }
	}
	
	
	@SuppressWarnings("unused")
	public void Drop(PcInstance pc) {
		final int chance = Util.random(1, 100);
		int i, dropcount, totalChance = 0;
		ScarecrowDropItem drop = new ScarecrowDropItem();
		for (i = 0; i < _list.size(); i++) {
			drop = _list.get(i);
			totalChance += drop.getChance();
			if (totalChance >= chance) {
				if (drop.getMin() == drop.getMax()) 
					dropcount = drop.getMin();
				else if (drop.getMin() < drop.getMax())
					dropcount = Util.random(drop.getMin(), drop.getMax());
				else 
					dropcount = 1;

					Item item = ItemDatabase.find(drop.getItemName());
					ItemInstance ii = ItemDatabase.newInstance(item);
					if (ii != null) {
						ii.setCount(dropcount);
						pc.getInventory().append(ii, ii.getCount());
						// true false 설정
					if (Lineage.view_cracker_damage && !pc.isDamageMassage()) {
						String msg = String.format("아데나 (%s)획득", ii.getCount());
						pc.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), pc,
								Lineage.CHATTING_MODE_MESSAGE, msg));
						
					}
				}
				break;
			}
		}
	}
}

