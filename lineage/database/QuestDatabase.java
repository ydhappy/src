package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.PcInstance;

public final class QuestDatabase {

	static public class RepeatQuest {
		private int _questid;
		private int _questType;
		private boolean _isclan;
		private boolean _isparty;
		private int _questlevel;
		private String _type;
		private String[] _mapid;
		private String[] _questtarget;
		private String[] _questcount;
		private int _droprate;
		private String _title;
		private String _message;
		private String _presentitemname;
		private int _presentitemcount;

		public void setQuestId(int i) { _questid = i; }
		public int getQuestId() { return _questid; }

		public void setQuestType(int i) { _questType = i; }
		public int getQuestType() { return _questType; }

		public void setClan(boolean result) { _isclan = result; }
		public boolean isClan() { return _isclan; }

		public void setParty(boolean result) { _isparty = result; }
		public boolean isParty() { return _isparty; }

		public void setQuestLevel(int i) { _questlevel = i; }
		public int getQuestLevel() { return _questlevel; }

		public void setType(String type) { _type = type; }
		public String getType() { return _type; }

		public void setMapId(String[] mapid) { _mapid = mapid; }
		public String[] getMapId() { return _mapid; }

		public void setQuestTarget(String[] type) { _questtarget = type; }
		public String[] getQuestTarget() { return _questtarget; }

		public void setQuestCount(String[] type) { _questcount = type; }
		public String[] getQuestCount() { return _questcount; }

		public void setDropRate(int i) { _droprate = i; }
		public int getDropRate() { return _droprate; }

		public void setTitle(String title) { _title = title; }
		public String getTitle() { return _title; }

		public void setMessage(String msg) { _message = msg; }
		public String getMessage() { return _message; }

		public void setPresentItemName(String name) { _presentitemname = name; }
		public String getPresentItemName() { return _presentitemname; }

		public void setPresentItemCount(int i) { _presentitemcount = i; }
		public int getPresentItemCount() { return _presentitemcount; }
	}

	static public class LevelQuest {
		private int _questid;
		private int _questType;
		private boolean _isclan;
		private boolean _isparty;
		private int _questlevel;
		private String _type;
		private String[] _mapid;
		private String[] _questtarget;
		private String[] _questcount;
		private int _droprate;
		private String _title;
		private String _message;
		private String _presentitemname;
		private int _presentitemcount;

		public void setQuestId(int i) { _questid = i; }
		public int getQuestId() { return _questid; }

		public void setQuestType(int i) { _questType = i; }
		public int getQuestType() { return _questType; }

		public void setClan(boolean result) { _isclan = result; }
		public boolean isClan() { return _isclan; }

		public void setParty(boolean result) { _isparty = result; }
		public boolean isParty() { return _isparty; }

		public void setQuestLevel(int i) { _questlevel = i; }
		public int getQuestLevel() { return _questlevel; }

		public void setType(String type) { _type = type; }
		public String getType() { return _type; }

		public void setMapId(String[] mapid) { _mapid = mapid; }
		public String[] getMapId() { return _mapid; }

		public void setQuestTarget(String[] type) { _questtarget = type; }
		public String[] getQuestTarget() { return _questtarget; }

		public void setQuestCount(String[] type) { _questcount = type; }
		public String[] getQuestCount() { return _questcount; }

		public void setDropRate(int i) { _droprate = i; }
		public int getDropRate() { return _droprate; }

		public void setTitle(String title) { _title = title; }
		public String getTitle() { return _title; }

		public void setMessage(String msg) { _message = msg; }
		public String getMessage() { return _message; }

		public void setPresentItemName(String name) { _presentitemname = name; }
		public String getPresentItemName() { return _presentitemname; }

		public void setPresentItemCount(int i) { _presentitemcount = i; }
		public int getPresentItemCount() { return _presentitemcount; }
	}

	private static QuestDatabase _instance;
	public static QuestDatabase getInstance() {
		if (_instance == null) {
			_instance = new QuestDatabase();
		}
		return _instance;
	}

	private QuestDatabase() {
		repeat_init();
		level_init();
	}

	private ArrayList<LevelQuest> levellist = new ArrayList<LevelQuest>();
	private ArrayList<RepeatQuest> repeatlist = new ArrayList<RepeatQuest>();
	public ArrayList<LevelQuest> getLevelQuestList() {
		return levellist;
	}
	public ArrayList<RepeatQuest> getRepeatQuestList() {
		return repeatlist;
	}

	static public void reload() {
		QuestDatabase oldInstance = _instance;
		_instance = new QuestDatabase();
		oldInstance.repeatlist.clear();
		oldInstance.levellist.clear();
		oldInstance = null;
	}

	public void repeat_init() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			pstm = con.prepareStatement("SELECT * FROM characters_repeat_quest_info");
			rs = pstm.executeQuery();
			while(rs.next()) {
				RepeatQuest quest = new RepeatQuest();
				quest.setQuestId(rs.getInt("quest_id"));
				quest.setClan(rs.getBoolean("isClan"));
				quest.setParty(rs.getBoolean("isParty"));
				quest.setQuestLevel(rs.getInt("level"));
				quest.setType(rs.getString("type"));
				String[] mapid = rs.getString("mapid").split(",");
				quest.setMapId(mapid);
				String[] quest_target = rs.getString("quest_target").split(",");
				quest.setQuestTarget(quest_target);
				String[] count = rs.getString("quest_count").split(",");
				quest.setQuestCount(count);
				quest.setDropRate(rs.getInt("quest_drop_rate"));
				quest.setTitle(rs.getString("title"));
				quest.setMessage(rs.getString("message"));
				quest.setPresentItemName(rs.getString("presentitem"));
				quest.setPresentItemCount(rs.getInt("presentcount"));
				repeatlist.add(quest);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : void repeat_init()\r\n", QuestDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, pstm, rs);
		}
	}

	public void level_init() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			pstm = con.prepareStatement("SELECT * FROM characters_level_quest_info");
			rs = pstm.executeQuery();
			while(rs.next()) {
				LevelQuest quest = new LevelQuest();
				quest.setQuestId(rs.getInt("quest_id"));
				quest.setClan(rs.getBoolean("isClan"));
				quest.setParty(rs.getBoolean("isParty"));
				quest.setQuestLevel(rs.getInt("level"));
				quest.setType(rs.getString("type"));
				String[] mapid = rs.getString("mapid").split(",");
				quest.setMapId(mapid);
				String[] quest_target = rs.getString("quest_target").split(",");
				quest.setQuestTarget(quest_target);
				String[] count = rs.getString("quest_count").split(",");
				quest.setQuestCount(count);
				quest.setDropRate(rs.getInt("quest_drop_rate"));
				quest.setTitle(rs.getString("title"));
				quest.setMessage(rs.getString("message"));
				quest.setPresentItemName(rs.getString("presentitem"));
				quest.setPresentItemCount(rs.getInt("presentcount"));
				levellist.add(quest);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : void level_init()\r\n", QuestDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, pstm, rs);
		}
	}

	static public class PcLevelQuest {
		private long _objid;
		private int _questid;
		private boolean _isclan;
		private boolean _isparty;
		private int _questlevel;
		private String _type;
		private int[] _mapid;
		private String[] _questtarget;
		private int[] _questcount;
		private int _droprate;
		private String _title;
		private String _message;
		private String _presentitemname;
		private int _presentitemcount;
		private int[] _completecount;
		private boolean _complete;

		public void setObjId(long i) { _objid = i; }
		public long getObjId() { return _objid; }

		public void setQuestId(int i) { _questid = i; }
		public int getQuestId() { return _questid; }

		public void setClan(boolean result) { _isclan = result; }
		public boolean isClan() { return _isclan; }

		public void setParty(boolean result) { _isparty = result; }
		public boolean isParty() { return _isparty; }

		public void setQuestLevel(int i) { _questlevel = i; }
		public int getQuestLevel() { return _questlevel; }

		public void setType(String type) { _type = type; }
		public String getType() { return _type; }

		public void setMapId(int[] mapid) { _mapid = mapid; }
		public int[] getMapId() { return _mapid; }

		public void setQuestTarget(String[] target) { _questtarget = target; }
		public String[] getQuestTarget() { return _questtarget; }

		public void setQuestCount(int[] count) { _questcount = count; }
		public int[] getQuestCount() { return _questcount; }

		public void setDropRate(int i) { _droprate = i; }
		public int getDropRate() { return _droprate; }

		public void setTitle(String title) { _title = title; }
		public String getTitle() { return _title; }

		public void setMessage(String msg) { _message = msg; }
		public String getMessage() { return _message; }

		public void setPresentItemName(String name) { _presentitemname = name; }
		public String getPresentItemName() { return _presentitemname; }

		public void setPresentItemCount(int i) { _presentitemcount = i; }
		public int getPresentItemCount() { return _presentitemcount; }

		public void setCompleteCount(int[] count) { _completecount = count; }
		public int[] getCompleteCount() { return _completecount; }

		public void setComplete(boolean result) { _complete = result; }
		public boolean getComplete() { return _complete; }

		public boolean updateCPLCount(int src, int count) {
			_completecount[src] += count;
			boolean complete = true;
			for (int i = 0; i < _completecount.length; i++) {
				if (_completecount[i] < _questcount[i]) {
					complete = false;
				}
			}
			return complete;
		}
	}

	/**
	 * 월드에 접속할때 레벨퀘스트 상태를 로드함.
	 * @param pc
	 */
	public void WorldJoin_Load_PcLevelList(PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			pstm = con.prepareStatement("select * from characters_level_quest where obj_id = ? and complete = ?");
			pstm.setLong(1, pc.getObjectId()); 
			pstm.setBoolean(2, false);
			rs = pstm.executeQuery();
			while(rs.next()) {
				PcLevelQuest quest = new PcLevelQuest();
				quest.setObjId(rs.getLong("obj_id"));
				quest.setQuestId(rs.getInt("quest_id"));
				quest.setClan(rs.getBoolean("isClan"));
				quest.setParty(rs.getBoolean("isParty"));
				quest.setQuestLevel(rs.getInt("level"));
				quest.setType(rs.getString("type"));
				String[] mapid = rs.getString("mapid").split(",");
				int map[] = new int[mapid.length];
				for (int i = 0; i < map.length; i++) {
					map[i] = Integer.parseInt(mapid[i]);
				}
				quest.setMapId(map);
				String[] quest_target = rs.getString("quest_target").split(",");
				quest.setQuestTarget(quest_target);
				String[] count = rs.getString("quest_count").split(",");
				int questcount[] = new int[count.length];
				for (int i = 0; i < questcount.length; i++) {
					questcount[i] = Integer.parseInt(count[i]);
				}
				quest.setQuestCount(questcount);
				quest.setDropRate(rs.getInt("quest_drop_rate"));
				quest.setTitle(rs.getString("title"));
				quest.setMessage(rs.getString("message"));
				quest.setPresentItemName(rs.getString("presentitem"));
				quest.setPresentItemCount(rs.getInt("presentcount"));
				String[] complete_count = rs.getString("complete_count").split(",");
				int completecount[] = new int[complete_count.length];
				for (int i = 0; i < completecount.length; i++) {
					completecount[i] = Integer.parseInt(complete_count[i]);
				}
				quest.setCompleteCount(completecount);
				quest.setComplete(rs.getBoolean("complete"));
				pc.getLevelQuestList().add(quest);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : void WorldJoin_Load_PcLevelList(PcInstance pc)\r\n", QuestDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, pstm, rs);
		}
	}

	/**
	 * 월드에서 나갈때 레벨퀘스트 상태를 저장한다.
	 * @param pc
	 */
	public void WorldOut_Save_PcLevelList(PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			int next;;
			con = DatabaseConnection.getLineage();
			StringBuilder sb = new StringBuilder();
			boolean complete = false;
			int questid = 0;
			ArrayList<PcLevelQuest> list = pc.getLevelQuestList();
			if (list == null || list.size() <= 0) return;
			pstm = con.prepareStatement("update characters_level_quest set complete_count=?, complete=? where obj_id = ? and quest_id = ?");
			for (int i = 0; i < list.size(); i++) {
				next = 0;
				int[] complete_count = list.get(i).getCompleteCount();
				String[] count = new String[complete_count.length];
				for (int ii = 0; ii < count.length; ii++) {
					count[ii] = Integer.toString(complete_count[ii]);
					sb.append(count[ii]);
					if (ii != count.length-1) {
						sb.append(",");
					}
				}
				pstm.setString(++next, sb.toString());
				sb.delete(0, sb.length());

				complete = list.get(i).getComplete();
				pstm.setBoolean(++next, complete);
				pstm.setLong(++next, pc.getObjectId());
				questid = list.get(i).getQuestId();
				pstm.setInt(++next, questid);
				pstm.execute();
			}
			list.clear();
			list = null;
		} catch (Exception e) {
			lineage.share.System.printf("%s : void WorldOut_Save_PcLevelList(PcInstance pc)\r\n", QuestDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, pstm);
		}
	}

	/**
	 * 레벨업을 해서 레벨퀘스트가 PC에게 추가될때. 
	 * @param pc
	 */
	public void store_pcLevelList(PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		PreparedStatement pstm1 = null;
		PreparedStatement pstm2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			con = DatabaseConnection.getLineage();
			int quest_id = 0, level = 0, presentcount = 0, quest_drop_rate = 0;
			boolean isClan = false, isParty = false, insert = false;
			String type = null, mapid = null, quest_target = null, quest_count = null
					, title = null, message = null, presentitem = null;
			pstm = con.prepareStatement("SELECT * FROM characters_level_quest_info");
			rs = pstm.executeQuery();
			while (rs.next()) {
				insert = false;
				quest_id = rs.getInt("quest_id");
				level = rs.getInt("level");
				
				pstm1 = con.prepareStatement("SELECT * FROM characters_level_quest where obj_id = ? and quest_id = ?");
				pstm1.setLong(1,  pc.getObjectId()); 
				pstm1.setInt(2, quest_id);
				rs1 = pstm1.executeQuery();
				if (!rs1.next()) {
					if (pc.getLevel() >= level) {
						insert = true;
					}
				}
				pstm1.close();


				if (insert) {
					isClan = rs.getBoolean("isClan");
					isParty = rs.getBoolean("isParty");
					type = rs.getString("type");
					mapid = rs.getString("mapid");
					quest_target = rs.getString("quest_target");
					quest_count = rs.getString("quest_count");
					quest_drop_rate = rs.getInt("quest_drop_rate");
					title = rs.getString("title");
					message = rs.getString("message");
					presentitem = rs.getString("presentitem");
					presentcount = rs.getInt("presentcount");

					int next = 0;
					pstm2 = con.prepareStatement("insert into characters_level_quest set obj_id=?, quest_id=?, isClan=?, isParty=?, level=?, type=?, mapid=?, quest_target=?, quest_count=?, quest_drop_rate=?, title=?, message=?, presentitem=?, presentcount=?, complete_count=?, complete=?");
					pstm2.setLong(++next,pc.getObjectId());
					pstm2.setInt(++next, quest_id);
					pstm2.setBoolean(++next, isClan);
					pstm2.setBoolean(++next, isParty);
					pstm2.setInt(++next, level);
					pstm2.setString(++next, type);
					pstm2.setString(++next, mapid);
					pstm2.setString(++next, quest_target);
					pstm2.setString(++next, quest_count);
					pstm2.setInt(++next, quest_drop_rate);
					pstm2.setString(++next, title);
					pstm2.setString(++next, message);
					pstm2.setString(++next, presentitem);
					pstm2.setInt(++next, presentcount);
					String[] count = quest_count.split(",");
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < count.length; i++) {
						sb.append("0");
						if (i != count.length-1) {
							sb.append(",");
						}
					}
					pstm2.setString(++next, sb.toString());
					pstm2.setBoolean(++next, false);
					pstm2.execute();
					addpcLevelList(pc, quest_id);
					sb.delete(0, sb.length());
					pstm2.close();
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : void store_pcLevelList(PcInstance pc)\r\n", QuestDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(pstm2);
			DatabaseConnection.close(pstm1, rs1);
			DatabaseConnection.close(con, pstm, rs);
		}
	}

	/**
	 * 레벨업해서 디비에 갱신된 목록을 PcLevelQuestList에 담을 때.
	 * @param pc
	 * @param questid
	 */
	public void addpcLevelList(PcInstance pc, int questid) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			pstm = con.prepareStatement("select * from characters_level_quest where obj_id = ? and quest_id = ?");
			pstm.setLong(1, pc.getObjectId()); 
			pstm.setInt(2, questid);
			rs = pstm.executeQuery();
			if (rs.next()) {
				PcLevelQuest quest = new PcLevelQuest();
				quest.setObjId(rs.getLong("obj_id"));
				quest.setQuestId(rs.getInt("quest_id"));
				quest.setClan(rs.getBoolean("isClan"));
				quest.setParty(rs.getBoolean("isParty"));
				quest.setQuestLevel(rs.getInt("level"));
				quest.setType(rs.getString("type"));
				String[] mapid = rs.getString("mapid").split(",");
				int map[] = new int[mapid.length];
				for (int i = 0; i < map.length; i++) {
					map[i] = Integer.parseInt(mapid[i]);
				}
				quest.setMapId(map);
				String[] quest_target = rs.getString("quest_target").split(",");
				quest.setQuestTarget(quest_target);
				String[] count = rs.getString("quest_count").split(",");
				int questcount[] = new int[count.length];
				for (int i = 0; i < questcount.length; i++) {
					questcount[i] = Integer.parseInt(count[i]);
				}
				quest.setQuestCount(questcount);
				quest.setDropRate(rs.getInt("quest_drop_rate"));
				quest.setTitle(rs.getString("title"));
				quest.setMessage(rs.getString("message"));
				quest.setPresentItemName(rs.getString("presentitem"));
				quest.setPresentItemCount(rs.getInt("presentcount"));
				String[] complete_count = rs.getString("complete_count").split(",");
				int completecount[] = new int[complete_count.length];
				for (int i = 0; i < completecount.length; i++) {
					completecount[i] = Integer.parseInt(complete_count[i]);
				}
				quest.setCompleteCount(completecount);
				quest.setComplete(rs.getBoolean("complete"));
				pc.getLevelQuestList().add(quest);
				
				ChattingController.toChatting(pc,"레벨퀘스트가추가되었습니다[.12]로확인가능합니다.", 2);
				
				
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : addpcLevelList(PcInstance pc, int questid)\r\n", QuestDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, pstm, rs);
		}
	}

	/**
	 * LevelQuest를 완료 했을때 해당 퀘스트의 complete를 true 상태로 변경하고, pc의 리스트에서 지워준다.
	 * @param pc
	 */
	public void LevelQuest_Clear(PcInstance pc, PcLevelQuest quest) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			int next;;
			con = DatabaseConnection.getLineage();
			StringBuilder sb = new StringBuilder();
			boolean complete = false;
			pstm = con.prepareStatement("update characters_level_quest set complete_count=?, complete=? where obj_id = ? and quest_id = ?");
			next = 0;
			int[] complete_count = quest.getCompleteCount();
			String[] count = new String[complete_count.length];
			for (int ii = 0; ii < count.length; ii++) {
				count[ii] = Integer.toString(complete_count[ii]);
				sb.append(count[ii]);
				if (ii != count.length-1) {
					sb.append(",");
				}
			}
			pstm.setString(++next, sb.toString());
			sb.delete(0, sb.length());

			complete = quest.getComplete();
			pstm.setBoolean(++next, complete);
			pstm.setLong(++next, pc.getObjectId());
			pstm.setInt(++next, quest.getQuestId());
			pstm.execute();
		} catch (Exception e) {
			lineage.share.System.printf("%s : void LevelQuest_Clear(PcInstance pc, PcLevelQuest quest)\r\n", QuestDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, pstm);
		}
	}






	static public class PcRepeatQuest {
		private long _objid;
		private int _questid;
		private boolean _isclan;
		private boolean _isparty;
		private int _questlevel;
		private String _type;
		private int[] _mapid;
		private String[] _questtarget;
		private int[] _questcount;
		private int _droprate;
		private String _title;
		private String _message;
		private String _presentitemname;
		private int _presentitemcount;
		private int[] _completecount;

		public void setObjId(long i) { _objid = i; }
		public long getObjId() { return _objid; }

		public void setQuestId(int i) { _questid = i; }
		public int getQuestId() { return _questid; }

		public void setClan(boolean result) { _isclan = result; }
		public boolean isClan() { return _isclan; }

		public void setParty(boolean result) { _isparty = result; }
		public boolean isParty() { return _isparty; }

		public void setQuestLevel(int i) { _questlevel = i; }
		public int getQuestLevel() { return _questlevel; }

		public void setType(String type) { _type = type; }
		public String getType() { return _type; }

		public void setMapId(int[] mapid) { _mapid = mapid; }
		public int[] getMapId() { return _mapid; }

		public void setQuestTarget(String[] target) { _questtarget = target; }
		public String[] getQuestTarget() { return _questtarget; }

		public void setQuestCount(int[] count) { _questcount = count; }
		public int[] getQuestCount() { return _questcount; }

		public void setDropRate(int i) { _droprate = i; }
		public int getDropRate() { return _droprate; }

		public void setTitle(String title) { _title = title; }
		public String getTitle() { return _title; }

		public void setMessage(String msg) { _message = msg; }
		public String getMessage() { return _message; }

		public void setPresentItemName(String name) { _presentitemname = name; }
		public String getPresentItemName() { return _presentitemname; }

		public void setPresentItemCount(int i) { _presentitemcount = i; }
		public int getPresentItemCount() { return _presentitemcount; }

		public void setCompleteCount(int[] count) { _completecount = count; }
		public int[] getCompleteCount() { return _completecount; }

		public boolean updateCPLCount(int src, int count) {
			_completecount[src] += count;
			boolean complete = true;
			for (int i = 0; i < _completecount.length; i++) {
				if (_completecount[i] < _questcount[i]) {
					complete = false;
				}
			}
			return complete;
		}
	}

	/**
	 * 월드에 접속할때 반복퀘스트 상태를 로드함.
	 * @param pc
	 */
	public void WorldJoin_Load_PcRepeatList(PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			pstm = con.prepareStatement("select * from characters_repeat_quest where obj_id = ?");
			pstm.setLong(1, pc.getObjectId()); 
			rs = pstm.executeQuery();
			if (rs.next()) {
				PcRepeatQuest quest = new PcRepeatQuest();
				quest.setObjId(rs.getLong("obj_id"));
				quest.setQuestId(rs.getInt("quest_id"));
				quest.setClan(rs.getBoolean("isClan"));
				quest.setParty(rs.getBoolean("isParty"));
				quest.setQuestLevel(rs.getInt("level"));
				quest.setType(rs.getString("type"));
				String[] mapid = rs.getString("mapid").split(",");
				int map[] = new int[mapid.length];
				for (int i = 0; i < map.length; i++) {
					map[i] = Integer.parseInt(mapid[i]);
				}
				quest.setMapId(map);
				String[] quest_target = rs.getString("quest_target").split(",");
				quest.setQuestTarget(quest_target);
				String[] count = rs.getString("quest_count").split(",");
				int questcount[] = new int[count.length];
				for (int i = 0; i < questcount.length; i++) {
					questcount[i] = Integer.parseInt(count[i]);
				}
				quest.setQuestCount(questcount);
				quest.setDropRate(rs.getInt("quest_drop_rate"));
				quest.setTitle(rs.getString("title"));
				quest.setMessage(rs.getString("message"));
				quest.setPresentItemName(rs.getString("presentitem"));
				quest.setPresentItemCount(rs.getInt("presentcount"));
				String[] complete_count = rs.getString("complete_count").split(",");
				int completecount[] = new int[complete_count.length];
				for (int i = 0; i < completecount.length; i++) {
					completecount[i] = Integer.parseInt(complete_count[i]);
				}
				quest.setCompleteCount(completecount);
				pc.setRepeatQuest(quest);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : void WorldJoin_Load_PcRepeatList(PcInstance pc)\r\n", QuestDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, pstm, rs);
		}
	}

	/**
	 * 월드에서 나갈때 반복퀘스트 상태를 저장한다.
	 * @param pc
	 */
	public void WorldOut_Save_PcRepeatList(PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			int next;;
			con = DatabaseConnection.getLineage();
			StringBuilder sb = new StringBuilder();
			int questid = 0;
			PcRepeatQuest quest = pc.getRepeatQuest();
			if (quest == null) return;
			pstm = con.prepareStatement("update characters_repeat_quest set complete_count=? where obj_id = ? and quest_id = ?");
			next = 0;
			int[] complete_count = quest.getCompleteCount();
			String[] count = new String[complete_count.length];
			for (int ii = 0; ii < count.length; ii++) {
				count[ii] = Integer.toString(complete_count[ii]);
				sb.append(count[ii]);
				if (ii != count.length-1) {
					sb.append(",");
				}
			}
			pstm.setString(++next, sb.toString());
			sb.delete(0, sb.length());
			pstm.setLong(++next, pc.getObjectId());
			questid = quest.getQuestId();
			pstm.setInt(++next, questid);
			pstm.execute();
		} catch (Exception e) {
			lineage.share.System.printf("%s : WorldOut_Save_PcRepeatList(PcInstance pc)\r\n", QuestDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, pstm);
		}
	}

	/**
	 * 레벨업을 해서 반복퀘스트가 PC에게 추가될때. 
	 * @param pc
	 */
	public void store_pcRepeatList(PcInstance pc, int questId) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = DatabaseConnection.getLineage();
			int quest_id = 0, level = 0, presentcount = 0, quest_drop_rate = 0;
			boolean isClan = false, isParty = false;
			String type = null, mapid = null, quest_target = null, quest_count = null
					, title = null, message = null, presentitem = null;
			RepeatQuest quest = null;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < repeatlist.size(); i++) {
				quest = repeatlist.get(i);
				if (quest.getQuestId() == questId) {
					break;
				}
			}
			if (quest == null) return;
			quest_id = quest.getQuestId();
			level = quest.getQuestLevel();
			if (level > pc.getLevel()) {
				ChattingController.toChatting(pc, "이 퀘스트를 진행하기에는 레벨이 부족합니다.");
				return;
			}
			isClan = quest.isClan();
			isParty = quest.isParty();
			type = quest.getType();

			String[] map = quest.getMapId();
			for (int m = 0; m < map.length; m++) {
				sb.append(map[m]);
				if (m != map.length-1) {
					sb.append(",");
				}
			}
			mapid = sb.toString();
			sb.delete(0, sb.length());

			String[] target = quest.getQuestTarget();
			for (int t = 0; t < target.length; t++) {
				sb.append(target[t]);
				if (t != target.length-1) {
					sb.append(",");
				}
			}
			quest_target = sb.toString();
			sb.delete(0, sb.length());

			String[] count = quest.getQuestCount();
			for (int c = 0; c < count.length; c++) {
				sb.append(count[c]);
				if (c != count.length-1) {
					sb.append(",");
				}
			}
			quest_count = sb.toString();
			sb.delete(0, sb.length());

			quest_drop_rate = quest.getDropRate();
			title = quest.getTitle();
			message = quest.getMessage();
			presentitem = quest.getPresentItemName();
			presentcount = quest.getPresentItemCount();

			int next = 0;
			pstm = con.prepareStatement("insert into characters_repeat_quest set obj_id=?, quest_id=?, isClan=?, isParty=?, level=?, type=?, mapid=?, quest_target=?, quest_count=?, quest_drop_rate=?, title=?, message=?, presentitem=?, presentcount=?, complete_count=?");
			pstm.setLong(++next, pc.getObjectId());
			pstm.setInt(++next, quest_id);
			pstm.setBoolean(++next, isClan);
			pstm.setBoolean(++next, isParty);
			pstm.setInt(++next, level);
			pstm.setString(++next, type);
			pstm.setString(++next, mapid);
			pstm.setString(++next, quest_target);
			pstm.setString(++next, quest_count);
			pstm.setInt(++next, quest_drop_rate);
			pstm.setString(++next, title);
			pstm.setString(++next, message);
			pstm.setString(++next, presentitem);
			pstm.setInt(++next, presentcount);
			String[] qcount = quest_count.split(",");
			for (int i = 0; i < qcount.length; i++) {
				sb.append("0");
				if (i != qcount.length-1) {
					sb.append(",");
				}
			}
			pstm.setString(++next, sb.toString());
			pstm.execute();
			addpcRepeatList(pc, quest_id);
			sb.delete(0, sb.length());
		} catch (Exception e) {
			lineage.share.System.printf("%s : store_pcRepeatList(PcInstance pc, int questId)\r\n", QuestDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, pstm);
		}
	}

	/**
	 * 반복퀘스트 명령어를 통해 디비에 갱신된 목록을 pc의 RepeatQuest에 넣을 때..
	 * @param pc
	 * @param questid
	 */
	public void addpcRepeatList(PcInstance pc, int questid) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			pstm = con.prepareStatement("select * from characters_repeat_quest where obj_id = ? and quest_id = ?");
			pstm.setLong(1, pc.getObjectId()); 
			pstm.setInt(2, questid);
			rs = pstm.executeQuery();
			if (rs.next()) {
				PcRepeatQuest quest = new PcRepeatQuest();
				quest.setObjId(rs.getLong("obj_id"));
				quest.setQuestId(rs.getInt("quest_id"));
				quest.setClan(rs.getBoolean("isClan"));
				quest.setParty(rs.getBoolean("isParty"));
				quest.setQuestLevel(rs.getInt("level"));
				quest.setType(rs.getString("type"));
				String[] mapid = rs.getString("mapid").split(",");
				int map[] = new int[mapid.length];
				for (int i = 0; i < map.length; i++) {
					map[i] = Integer.parseInt(mapid[i]);
				}
				quest.setMapId(map);
				String[] quest_target = rs.getString("quest_target").split(",");
				quest.setQuestTarget(quest_target);
				String[] count = rs.getString("quest_count").split(",");
				int questcount[] = new int[count.length];
				for (int i = 0; i < questcount.length; i++) {
					questcount[i] = Integer.parseInt(count[i]);
				}
				quest.setQuestCount(questcount);
				quest.setDropRate(rs.getInt("quest_drop_rate"));
				quest.setTitle(rs.getString("title"));
				quest.setMessage(rs.getString("message"));
				quest.setPresentItemName(rs.getString("presentitem"));
				quest.setPresentItemCount(rs.getInt("presentcount"));
				String[] complete_count = rs.getString("complete_count").split(",");
				int completecount[] = new int[complete_count.length];
				for (int i = 0; i < completecount.length; i++) {
					completecount[i] = Integer.parseInt(complete_count[i]);
				}
				quest.setCompleteCount(completecount);
				pc.setRepeatQuest(quest);
				StringBuilder sb = new StringBuilder();
				sb.append("\\fT").append(quest.getQuestId()).append("번 ").append("반복 퀘스트가 추가되었습니다.\n");
				sb.append("\\fT.반복퀘스트 확인 명령어로 상세정보를 확인 하실 수 있습니다.");
				ChattingController.toChatting(pc, sb.toString(), Lineage.CHATTING_MODE_MESSAGE);
				sb.delete(0, sb.length());
				sb = null;
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : addpcRepeatList(PcInstance pc, int questid)\r\n", QuestDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, pstm, rs);
		}
	}

	/**
	 * RepeatQuest에 등록된 퀘스트를 소멸시킨다.
	 * @param pc
	 */
	public void RepeatQuest_Clear(PcInstance pc, int questid) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			int next = 0;
			con = DatabaseConnection.getLineage();
			pstm = con.prepareStatement("delete from characters_repeat_quest where obj_id = ? and quest_id = ?");
			pstm.setLong(++next,  pc.getObjectId());
			pstm.setInt(++next, questid);
			pstm.execute();
		} catch (Exception e) {
			lineage.share.System.printf("%s : void RepeatQuest_Clear(PcInstance pc, int questid)\r\n", QuestDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, pstm);
		}
	}


}
