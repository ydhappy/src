package lineage.world.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.lineage.Clan;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_InventoryAdd;
import lineage.network.packet.server.S_InventoryDelete;
import lineage.network.packet.server.S_LetterRead;
import lineage.network.packet.server.S_LetterSystem;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.World;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.Letter;

public final class LetterController {

	static public void init() {
		TimeLine.start("LetterController..");
		
		TimeLine.end();
	}
	static public void toDirectMessageBox(object o, String title, String memo) {
		// 서버 안내편지창 자동 띄우기.
		Letter letter = (Letter)Letter.clone(ItemDatabase.getPool(Letter.class)).clone(ItemDatabase.find("편지지 - 읽은 편지"));
		if(letter != null) {
			letter.setObjectId(1);
			letter.setLetterUid(1);
			letter.setFrom(ServerDatabase.getName());
			letter.setTo(o.getName());
			letter.setSubject(title);
			letter.setMemo(memo);
			o.toSender(S_InventoryAdd.clone(BasePacketPooling.getPool(S_InventoryAdd.class), letter));
			o.toSender(S_LetterRead.clone(BasePacketPooling.getPool(S_LetterRead.class), letter));
			o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), letter));
			ItemDatabase.setPool(letter);
		} else {
			ChattingController.toChatting(o, memo, 20);
		}
	}
	
	/**
	 * 편지창으로 메세지박스에 역활을 대신함.
	 *  : 편지지가 디비상에 없을경우 채팅창에 대신 출력.
	 * @param o
	 * @param title
	 * @param memo
	 */
	static public void toMessageBox(object o, String title, String memo) {
		// 레벨스탯 찍을 필요성이 있을경우 무시하기.
		if(o instanceof PcInstance) {
			PcInstance pc = (PcInstance)o;
			if(pc.toLvStat(false))
				return;
		}
		//
		toDirectMessageBox(o, title, memo);
	}
	
	/**
	 * 사용자가 월드에 접속할때 호출되는 함수.
	 * 아직 인벤토리에 추가안된 편지가 있는지 확인함
	 * 있을경우 그에따른 처리를 함께 함.
	 */
	static public void toWorldJoin(PcInstance pc){
		boolean find = false;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			// 검색 후 처리
			st = con.prepareStatement("SELECT * FROM characters_letter WHERE paperInventory='false' AND LOWER(paperTo)=?");
			st.setString(1, pc.getName().toLowerCase());
			rs = st.executeQuery();
			while(rs.next()){
				find = true;
				if(Lineage.server_version >= 300)
					continue;
				// 인벤 등록 처리.
				long time = 0;
				try { time = rs.getTimestamp("paperDate").getTime(); } catch (Exception e) { }
				toLetter(pc, rs.getInt("uid"), rs.getString("paperFrom"), rs.getString("paperTo"), rs.getString("paperSubject"), rs.getString("paperMemo"), time, rs.getInt("paperAden"));
			}
			rs.close();
			st.close();
			// 한개라도 편지가 있을경우 처리.
			if(find){
				// 비둘기 표현
				pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 1091), true);
				// 편지도착 알림 메세지
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 428));
				// 갱신
				st = con.prepareStatement("UPDATE characters_letter SET paperInventory='true' WHERE paperInventory='false' AND LOWER(paperTo)=?");
				st.setString(1, pc.getName().toLowerCase());
				st.executeUpdate();
				st.close();
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : toWorldJoin(PcInstance pc)\r\n", LetterController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	/**
	 * 편지지 작성한것을 처리하는 함수.
	 */
	static public void toLetter(String from, String to, String subject, String memo, long aden, boolean isLetterAppend){
		if(to!=null && subject!=null && memo!=null){
			Connection con = null;
			try {
				con = DatabaseConnection.getLineage();
				toLetter(con, from, "Paper", to, subject, memo, aden, isLetterAppend);
			} catch (Exception e) {
				lineage.share.System.printf("%s : toLetter(object o, String to, String subject, String memo, int aden)\r\n", LetterController.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con);
			}
		}
	}
	
	/**
	 * 편지지 작성한것을 처리하는 함수.
	 */
	static public void toLetter(String from, String to, String subject, String memo, int aden) {
		if (to != null && subject != null && memo != null) {
			Connection con = null;
			try {
				con = DatabaseConnection.getLineage();
				toLetter(con, from, "Paper", to, subject, memo, aden);
			} catch (Exception e) {
				lineage.share.System.printf(
						"%s : toLetter(object o, String to, String subject, String memo, int aden)\r\n",
						LetterController.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con);
			}
		}
	}
	/**
	 * 혈맹 편지처리와 일반 편지처리 가 중복되서 이와같이 따로 함수로 뺌.
	 * @param con
	 * @param o
	 * @param type
	 * @param to
	 * @param subject
	 * @param memo
	 * @param aden
	 * @param isLetterAppend	: 편지지를 생성할지 여부.
	 */
	@SuppressWarnings("unused")
	static private void toLetter(Connection con, String from, String type, String to, String subject, String memo,
			int aden) {
		// uid 생성
		int new_uid = selectMax(con) + 1;
		// to 사용자가 온라인 중일경우
		PcInstance user = World.findPc(to);
		// 사용자가 온라인일 경우 개인상점에서 보내는 편지는 제외
		if (user != null && !from.equalsIgnoreCase("개인상점")) {
			// 인벤 등록 처리.
			toLetter(user, new_uid, from == null ? "메티스" : from, to, subject, memo, System.currentTimeMillis(), aden);
			// 비둘기 표현
			user.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), user, 1091), true);
			// 편지도착 알림 메세지
			user.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 428));
		}
		// 디비에 등록.
		insert(con, new_uid, type, from == null ? "메티스" : from, to, subject, memo, String.valueOf(user != null), aden);
	}
	
	static public void toPledgeLetter(String from, String to, String subject, String memo, boolean isLetterAppend){
		if(to!=null && subject!=null && memo!=null){
			// 혈맹 찾기.
			Clan clan = ClanController.find(to);
			if(clan != null){
				Connection con = null;
				try {
					con = DatabaseConnection.getLineage();
					// 혈맹원들 이름을 참고해서 편지 보내기.
					for(String cm : clan.getMemberList())
						toLetter(con, from, "clanPaper", cm, subject, memo, 0, isLetterAppend);
				} catch (Exception e) {
					lineage.share.System.printf("%s : toPledgeLetter(Character cha, String to, String subject, String memo)\r\n", LetterController.class.toString());
					lineage.share.System.println(e);
				} finally {
					DatabaseConnection.close(con);
				}
			}
		}
	}
	
	static public void toSave(PcInstance pc, int type, long uid) {
		if(Lineage.server_version < 300)
			return;
		//
		boolean isSave = false;
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters_letter SET type='savePaper' WHERE uid=?");
			st.setLong(1, uid);
			st.execute();
			isSave = true;
		}catch(Exception e){
			lineage.share.System.printf("%s : toSave(PcInstance pc, int type, int uid)\r\n", LetterController.class.toString());
			lineage.share.System.println(e);
		}finally{
			DatabaseConnection.close(con, st);
		}
		pc.toSender( S_LetterSystem.clone(BasePacketPooling.getPool(S_LetterSystem.class), type, uid, isSave) );
	}
	
	/**
	 * 편지삭제 요청시 호출됨.
	 * @param pc
	 * @param type
	 * @param uid
	 */
	static public void toRemove(PcInstance pc, int type, long uid) {
		if(Lineage.server_version < 300)
			return;
		//
		boolean isDelete = false;
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("DELETE FROM characters_letter WHERE uid=? AND paperTo=?");
			st.setLong(1, uid);
			st.setString(2, pc.getName());
			st.execute();
			isDelete = true;
		}catch(Exception e){
			lineage.share.System.printf("%s : toRemove(PcInstance pc, int type, int uid)\r\n", LetterController.class.toString());
			lineage.share.System.println(e);
		}finally{
			DatabaseConnection.close(con, st);
		}
		pc.toSender( S_LetterSystem.clone(BasePacketPooling.getPool(S_LetterSystem.class), type, uid, isDelete) );
	}
	
	/**
	 * 편지쓰기 처리 메서드.
	 *  : 3.00이상에서 사용함.
	 * @param pc
	 * @param to
	 * @param subject
	 * @param memo
	 */
	static public void toLetter(PcInstance pc, int type, String to, String subject, String memo) {
		if(Lineage.server_version < 300)
			return;
		if(type==0x20) {
			if(pc.getInventory().isAden(Lineage.letter_price, true))
				toLetter(pc.getName(), to, subject, memo, 0, Lineage.server_version<300);
			else
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
		}
		if(type == 0x21) {
			if(pc.getInventory().isAden(Lineage.letter_price, true))
				toPledgeLetter(pc.getName(), to, subject, memo, Lineage.server_version<300);
			else
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
		}
	}
	
	/**
	 * 편지목록 요청처리 메서드.
	 * @param pc
	 * @param type
	 * @return
	 */
	static public List<Letter> getList(PcInstance pc, int type) {
		//
		List<Letter> list = new ArrayList<Letter>();
		if(Lineage.server_version >= 300) {
			Connection con = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			String Type = "";
			int limit = 0;
			switch(type) {
				case 1:	// 혈맹편지
					Type = "clanPaper";
					limit = 50;
					break;
				case 2:	// 보관함
					Type = "savePaper";
					limit = 10;
					break;
				default: // 일반편지
					Type = "Paper";
					limit = 20;
					break;
			}
			try {
				con = DatabaseConnection.getLineage();
				st = con.prepareStatement("SELECT * FROM characters_letter WHERE type=? AND paperTo=? ORDER BY uid DESC LIMIT ?");
				st.setString(1, Type);
				st.setString(2, pc.getName());
				st.setInt(3, limit);
				rs = st.executeQuery();
				while(rs.next()) {
					Letter l = (Letter)Letter.clone(ItemDatabase.getPool(Letter.class));
					l.setLetterUid(rs.getInt("uid"));
					read(con, l);
					list.add( l );
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : getList(PcInstance pc, int type)\r\n", LetterController.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st, rs);
			}
		}
		return list;
	}
	
	/**
	 * 3.00 버전부터 편지시스템이 적용됨.
	 * 그 기능과 연결된 메서드.
	 *  : 편지내용을 보려고할때 호출됨.
	 * @param pc
	 * @param type
	 * @param uid
	 */
	static public void toRead(PcInstance pc, int type, long uid) {
		if(Lineage.server_version < 300)
			return;
		Connection con = null;
		try {
			con = DatabaseConnection.getLineage();

			Letter l = (Letter)Letter.clone(ItemDatabase.getPool(Letter.class));
			l.setLetterUid(uid);
			read(con, l);
			if(l.isOpen() == false) {
				l.setOpen(true);
				updateRead(con, l.getLetterUid(), l.isOpen());
			}
			//
			pc.toSender( S_LetterSystem.clone(BasePacketPooling.getPool(S_LetterSystem.class), type, l) );
			//
			ItemDatabase.setPool(l);
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(con);
		}
	}
	
	/**
	 * 편지를 읽엇는지 안읽었는지 상태를 변경처리하는 메서드.
	 * @param con
	 * @param uid
	 * @param isOpen
	 */
	static private void updateRead(Connection con, long uid, boolean isOpen) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("UPDATE characters_letter SET paperRead=? WHERE uid=?");
			st.setInt(1, isOpen ? 1 : 0);
			st.setLong(2, uid);
			st.executeUpdate();
			st.close();
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(st);
		}
	}

	/**
	 * 디비로부터 정보를 읽는 함수.
	 *  : 사용자가 월드접속할때 아이템정보를 불러오고
	 *    거기서 아이템마다 toWorldJoin 를 호출함.
	 *    Letter클레스는 toWorldJoin 가 구현되어 있으며, 그곳에서 이곳을 호출하게됨.
	 */
	static public void read(Connection con, Letter l){
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM characters_letter WHERE uid=?");
			st.setLong(1, l.getLetterUid());
			rs = st.executeQuery();
			if(rs.next()){
				l.setFrom( rs.getString("paperFrom") );
				l.setTo( rs.getString("paperTo") );
				l.setSubject( rs.getString("paperSubject") );
				l.setMemo( rs.getString("paperMemo") );
				l.setOpen( rs.getInt("paperRead") == 1 );
				long time = 0;
				try {
					l.setDateString( rs.getString("paperDate") );
					time = rs.getTimestamp("paperDate").getTime();
				} catch (Exception e) { }
				l.setDate( time );
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : read(Connection con, Letter l)\r\n", LetterController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
	}

	/**
	 * 등록된 편지들중 uid값이 젤 큰걸 찾아서 리턴.
	 */
	static private int selectMax(Connection con){
		int max = 0;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT MAX(uid) FROM characters_letter");
			rs = st.executeQuery();
			if(rs.next())
				max = rs.getInt(1);
		} catch (Exception e) {
			lineage.share.System.printf("%s : selectMax(Connection con)\r\n", LetterController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		return max;
	}
	
	/**
	 * 편지 작성한 내용 디비에 기록처리하는 함수.
	 */
	static private void insert(Connection con, int new_uid, String type, String from, String to, String subject, String memo, String inventory, long aden){
		PreparedStatement st = null;
		try{
			st = con.prepareStatement("INSERT INTO characters_letter SET uid=?, type=?, paperFrom=?, paperTo=?, paperSubject=?, paperMemo=?, paperInventory=?, paperAden=?, paperDate=?");
			st.setInt(1, new_uid);
			st.setString(2, type);
			st.setString(3, from);
			st.setString(4, to);
			st.setString(5, subject);
			st.setString(6, memo);
			st.setString(7, inventory);
			st.setLong(8, aden);
			st.setTimestamp(9, new java.sql.Timestamp(System.currentTimeMillis()));
			st.executeUpdate();
		} catch(Exception e) {
			lineage.share.System.printf("%s : insert(Connection con, int new_uid, String type, String from, String to, String subject, String memo, boolean inventory)\r\n", LetterController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}

	/**
	 * 중복되는 처리구문 제거를 위해 따로 뺌.
	 * 편지지를 생성해서 pc 인벤토리에 등록 처리 하는 함수.
	 */
	static private void toLetter(PcInstance pc, int new_uid, String from, String to, String subject, String memo, long date, long aden){
		// 편지지 생성.
		Letter l = (Letter)ItemDatabase.newInstance( ItemDatabase.find("편지지 - 안읽은 편지") );
		l.setObjectId(ServerDatabase.nextItemObjId());
		l.setLetterUid( new_uid );
		l.setFrom( from );
		l.setTo( to );
		l.setSubject( subject );
		l.setMemo( memo );
		l.setDate( date );
		// 편지지 등록.
		pc.getInventory().append(l, true);
		// 아덴 지급 처리.
		if(aden>0){
			ItemInstance ii = pc.getInventory().findAden();
			if(ii == null){
				ii = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
				ii.setObjectId(ServerDatabase.nextItemObjId());
				ii.setCount(0);
				pc.getInventory().append(ii, true);
			}
			//
			pc.getInventory().count(ii, ii.getCount()+aden, true);
		}
	}
	
	/**
	 * 혈맹 편지처리와 일반 편지처리 가 중복되서 이와같이 따로 함수로 뺌.
	 * @param con
	 * @param o
	 * @param type
	 * @param to
	 * @param subject
	 * @param memo
	 * @param aden
	 * @param isLetterAppend	: 편지지를 생성할지 여부.
	 */
	static private void toLetter(Connection con, String from, String type, String to, String subject, String memo, long aden, boolean isLetterAppend){
		// uid 생성
		int new_uid = selectMax(con) + 1;
		// to 사용자가 온라인 중일경우
		PcInstance user = World.findPc(to);
		if(user != null){
			// 인벤 등록 처리.
			if(isLetterAppend)
				toLetter(user, new_uid, from==null ? "메티스" : from, to, subject, memo, System.currentTimeMillis(), aden);
			// 비둘기 표현
			user.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), user, 1091), true);
			// 편지도착 알림 메세지
			user.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 428));
		}
		// 디비에 등록.
		insert(con, new_uid, type, from==null ? "메티스" : from, to, subject, memo, String.valueOf(user!=null), aden);
	}
}
