package lineage.world.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lineage.bean.lineage.Board;
import lineage.database.AccountDatabase;
import lineage.database.BackgroundDatabase;
import lineage.database.CharactersDatabase;
import lineage.database.DatabaseConnection;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_BoardView;
import lineage.network.packet.server.S_Message;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.object.instance.BoardInstance;
import lineage.world.object.instance.PcInstance;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public final class BoardController {

	static private List<Board> pool;

	static public void init() {
		TimeLine.start("BoardController..");

		pool = new ArrayList<Board>();

		TimeLine.end();
	}

	static public Board getPool() {
		Board b = null;
		synchronized (pool) {
			if (Lineage.memory_recycle && pool.size() > 0) {
				b = pool.get(0);
				pool.remove(0);
			} else {
				b = new Board();
			}
		}
		return b;
	}

	static public void setPool(Board b) {
		if (!Lineage.memory_recycle)
			return;
		synchronized (pool) {
			if (!pool.contains(b))
				pool.add(b);
		}
	}

	static public int getPoolSize() {
		return pool.size();
	}

	/**
	 * 해당 게시판에 게시판리스트를 만들어서 리턴.
	 * 
	 * @param type    : 어떤 마을에 게시판인지 구분용
	 * @param uid_pos : 표현할 uid에 위치값. 해당 위치에서 아래로 8개 뽑음.
	 * @param r_list  : 리턴될 목록을 담을 변수
	 */
	static public void getList(String type, long uid_pos, List<Board> r_list, int limit) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM boards WHERE type=? AND uid<? ORDER BY uid DESC LIMIT ?");
			st.setString(1, type);
			st.setLong(2, uid_pos);
			st.setInt(3, limit);
			rs = st.executeQuery();
			while (rs.next()) {
				Board b = getPool();
				b.setUid(rs.getInt("uid"));
				b.setType(rs.getString("type"));
				b.setSellAccountId(rs.getInt("sell_account_uid"));
				b.setAccountId(rs.getString("account_id"));
				b.setName(rs.getString("name"));
				try {
					b.setDays(rs.getTimestamp("days").getTime());
				} catch (Exception e) {
				}
				b.setSubject(rs.getString("subject"));
				b.setMemo(rs.getString("memo"));
				b.setAdena(rs.getInt("adena"));
				b.setMoney(rs.getInt("money"));
				b.setStep(rs.getInt("step"));
				b.setBuyAccountId(rs.getString("buy_account_id"));
				b.setbuyAccountUid(rs.getInt("buy_account_uid"));
				b.setBuyObjId(rs.getInt("buy_objid"));
				b.setBuyName(rs.getString("buy_name"));

				r_list.add(b);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : getList(String type, int uid_pos, List<Board> r_list)\r\n",
					BoardController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	/**
	 * uid 해당하는 정보 추출하여 리턴함.
	 * 
	 * @param type
	 * @param uid
	 * @return
	 */
	static public Board find(String type, long uid) {
		Board b = null;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM boards WHERE type=? AND uid=?");
			st.setString(1, type);
			st.setLong(2, uid);
			rs = st.executeQuery();
			if (rs.next()) {
				b = getPool();
				b.setUid(rs.getInt("uid"));
				b.setType(rs.getString("type"));
				b.setSellAccountId(rs.getInt("sell_account_uid"));
				b.setAccountId(rs.getString("account_id"));
				b.setName(rs.getString("name"));
				try {
					b.setDays(rs.getTimestamp("days").getTime());
				} catch (Exception e) {
				}
				b.setSubject(rs.getString("subject"));
				b.setMemo(rs.getString("memo"));
				b.setAdena(rs.getInt("adena"));
				b.setMoney(rs.getInt("money"));
				b.setStep(rs.getInt("step"));
				b.setBuyAccountId(rs.getString("buy_account_id"));
				b.setbuyAccountUid(rs.getInt("buy_account_uid"));
				b.setBuyObjId(rs.getInt("buy_objid"));
				b.setBuyName(rs.getString("buy_name"));
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : find(String type, int uid)\r\n", BoardController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return b;
	}

	/**
	 * 글쓰기 처리 함수.
	 * 
	 * @param pc
	 * @param bi
	 * @param subject
	 * @param content
	 */
	static public void toWrite(PcInstance pc, BoardInstance bi, String subject, String content) {
		//
		Object o = PluginController.init(BoardController.class, "isWrite", pc, bi);
		if (o != null && ((boolean) o) == false)
			return;
		BoardInstance giran = BackgroundDatabase.getTradeBoard();
		if(giran != null && bi.getType().equalsIgnoreCase("giran")){
			ChattingController.toChatting(pc, "아데나 거래 게시판은 명령어로 작성해 주시기 바랍니다. [가이드 참고]", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		BoardInstance rank = BackgroundDatabase.getRankBoard();
		if(rank != null && bi.getType().equalsIgnoreCase("rank")){
			ChattingController.toChatting(pc, "랭킹 게시판은 글쓰기가 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		if (bi.getType().equalsIgnoreCase("silence") && pc.getGm() == 0) {
			ChattingController.toChatting(pc, "해당 게시판은 운영자만 작성이 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		if (pc.getInventory().isAden(Lineage.board_write_price, true)) {
			Connection con = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				con = DatabaseConnection.getLineage();

				// uid 추출
				int uid = getMaxUid(bi.getType());
				// 등록
				st = con.prepareStatement(
						"INSERT INTO boards SET uid=?, type=?, account_id=?, name=?, days=?, subject=?, memo=?");
				st.setInt(1, uid);
				st.setString(2, bi.getType());
				st.setString(3, pc.getClient().getAccountId());
				st.setString(4, pc.getName());
				st.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
				st.setString(6, subject);
				st.setString(7, content);
				st.executeUpdate();
				st.close();

				// 페이지 보기.
				toView(pc, bi, uid);
			} catch (Exception e) {
				lineage.share.System.printf(
						"%s : toWrite(PcInstance pc, BoardInstance bi, String subject, String content)\r\n",
						BoardController.class.toString());
				lineage.share.System.println(e);
				e.printStackTrace();
			} finally {
				DatabaseConnection.close(con, st, rs);
			}
		} else {
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
		}
	}

	/**
	 * 게시판 읽기 처리 함수.
	 * 
	 * @param pc
	 * @param bi
	 * @param uid
	 */
	static public void toView(PcInstance pc, BoardInstance bi, long uid) {
		// 정확히 여기부분에서 걸렸어요 그래서 줄부분을 더블클릭하면 여기서 멈추게 되는게 F6 으로 다음으로 넘길 수가 있어요 그러면이제 걸렸는데 도통모르겠다
		// 그러면 이제 트라이문을 쓰는거에요
		// 트라이문은 브레이킹 포인트로 못잡을때 모를때 쓰면 좋아요
		try {// 여기서 실행한다
		Board b = find(bi.getType(), uid); // 여기서 걸리겠죠??
		if (b != null) {
			pc.toSender(S_BoardView.clone(BasePacketPooling.getPool(S_BoardView.class), b));
			setPool(b);
		}
		} catch (Exception e) { // 오류가 나는지 스캔한다
			e.printStackTrace(); //오류나는부분 그전 부분 경로를 콘솔창에 기록한다..
		}
		// 이렇게 트라이문이 없는데 여기서 걸리는거 같은데 어떤 경로로 어떻게 걸리는지 모를때는 이렇게 넣어주시면 되요
		// 이렇게 해서 나온 결과로 저기 파인드 부분에서 오류가 나는데 파인드는 정상인거에요 그래서 제가 아덴
	}

	/**
	 * 게시판에 게시물 제거 처리 함수.
	 * 
	 * @param pc
	 * @param bi
	 * @param uid
	 */
	static public void toDelete(PcInstance pc, BoardInstance bi, long uid) {
		Board b = find(bi.getType(), uid);
		if (b != null) {
			Connection con = null;
			PreparedStatement st = null;
			try {
				con = DatabaseConnection.getLineage();

				// 제거.
				st = con.prepareStatement("DELETE FROM boards WHERE uid=? AND account_id=? AND name=?");
				st.setLong(1, uid);
				st.setString(2, pc.getClient().getAccountId());
				st.setString(3, pc.getName());
				st.executeUpdate();
				st.close();
				// uid 갱신.
				st = con.prepareStatement("UPDATE boards SET uid=uid-1 WHERE type=? AND uid>?");
				st.setString(1, bi.getType());
				st.setLong(2, uid);
				st.executeUpdate();
				st.close();
			} catch (Exception e) {
				lineage.share.System.printf("%s : toDelete(PcInstance pc, BoardInstance bi, int uid)\r\n",
						BoardController.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st);
			}
			setPool(b);
		}
	}

	/**
	 * 타입에 맞는 게시물들에 최대 uid값 추출.
	 * 
	 * @param type
	 * @return
	 */
	static public int getMaxUid(String type) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT MAX(uid) FROM boards WHERE type=?");
			st.setString(1, type);
			rs = st.executeQuery();
			if (rs.next())
				return rs.getInt(1) + 1;
		} catch (Exception e) {
			lineage.share.System.printf("%s : getMaxUid(String type)\r\n", BoardController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public static String toJavaScript(Map<String, List<String>> params) {
		JSONObject obj = new JSONObject();
		//
		try {
			String id = params.get("id").get(0);
			String pw = params.get("pw").get(0);
			String type = params.get("type").get(0);
			String town = params.get("town").get(0);
			int limit = Integer.valueOf(params.get("limit").get(0));
			int page = Integer.valueOf(params.get("page").get(0));
			int uid = Integer.valueOf(params.get("uid").get(0));
			String name = params.get("name").get(0);
			String subject = params.get("subject").get(0);
			String memo = params.get("memo").get(0);
			if (type.equalsIgnoreCase("list")) {
				toJavaScriptList(obj, town, page, limit);
			} else if (type.equalsIgnoreCase("view")) {
				Board b = find(town, uid);
				// 정보 정리.
				JSONObject board = new JSONObject();
				board.put("uid", b.getUid());
				board.put("name", b.getName());
				board.put("subject", b.getSubject());
				board.put("memo", b.getMemo().replaceAll("\n", "</br>"));
				board.put("date", Util.getLocaleString(b.getDays(), true));
				board.put("photo", CharactersDatabase.getCharacterClass(b.getName()));
				board.put("is_me", b.getAccountId().equalsIgnoreCase(id));
				obj.put("board", board);
			} else if (type.equalsIgnoreCase("write")) {
				// 계정 확인.
				if (AccountDatabase.isAccount(AccountDatabase.getUid(id), id, pw)) {
					//
					List<String> name_list = CharactersDatabase.getCharacters(id);
					if (name_list.size() > 0) {
						JSONArray names = new JSONArray();
						for (String n : name_list)
							names.add(n);
						name_list.clear();
						name_list = null;
						obj.put("names", names);
					} else {
						obj.put("action", "error");
						obj.put("message", "케릭터가 존재하지 않습니다. 케릭를 생성 후 시도하여 주십시오.");
					}
				} else {
					obj.put("action", "error");
					obj.put("message", "계정 정보가 잘못되었거나 존재하지 않습니다.");
				}
			} else if (type.equalsIgnoreCase("submit")) {
				// 계정 확인.
				if (AccountDatabase.isAccount(AccountDatabase.getUid(id), id, pw)) {
					// 케릭명 확인.
					List<String> name_list = CharactersDatabase.getCharacters(id);
					if (name_list.contains(name)) {
						// 유효성 체크.
						if (subject.length() != 0 && memo.length() != 0) {
							//
							Connection con = null;
							PreparedStatement st = null;
							ResultSet rs = null;
							try {
								con = DatabaseConnection.getLineage();

								// uid 추출
								int maxUid = getMaxUid(town);
								// 등록
								st = con.prepareStatement(
										"INSERT INTO boards SET uid=?, type=?, account_id=?, name=?, days=?, subject=?, memo=?");
								st.setInt(1, maxUid);
								st.setString(2, town);
								st.setString(3, id);
								st.setString(4, name);
								st.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
								st.setString(6, subject);
								st.setString(7, memo);
								st.executeUpdate();
								st.close();
							} catch (Exception e) {
								lineage.share.System.printf("%s : toJavaScript(Map<String, List<String>> params)\r\n",
										BoardController.class.toString());
								lineage.share.System.println(e);
							} finally {
								DatabaseConnection.close(con, st, rs);
							}
							//
							toJavaScriptList(obj, town, page, limit);
						} else {
							obj.put("action", "error");
							obj.put("message", "잘못된 정보입니다. 확인 후 다시 시도하여 주십시오.");
						}
					} else {
						obj.put("action", "error");
						obj.put("message", "케릭명이 잘 못 되었습니다.");
					}
				} else {
					obj.put("action", "error");
					obj.put("message", "계정 정보가 잘못되었거나 존재하지 않습니다.");
				}
			} else if (type.equalsIgnoreCase("delete")) {
				// 계정 확인.
				if (AccountDatabase.isAccount(AccountDatabase.getUid(id), id, pw)) {
					Board b = find(town, uid);
					if (b != null) {
						Connection con = null;
						PreparedStatement st = null;
						try {
							con = DatabaseConnection.getLineage();
							// 제거.
							st = con.prepareStatement("DELETE FROM boards WHERE uid=? AND account_id=? AND type=?");
							st.setInt(1, uid);
							st.setString(2, id);
							st.setString(3, town);
							st.executeUpdate();
							st.close();
							// uid 갱신.
							st = con.prepareStatement("UPDATE boards SET uid=uid-1 WHERE type=? AND uid>?");
							st.setString(1, town);
							st.setInt(2, uid);
							st.executeUpdate();
							st.close();
						} catch (Exception e) {
							lineage.share.System.printf("%s : toDelete(PcInstance pc, BoardInstance bi, int uid)\r\n",
									BoardController.class.toString());
							lineage.share.System.println(e);
						} finally {
							DatabaseConnection.close(con, st);
						}
						setPool(b);
					}
					//
					toJavaScriptList(obj, town, page, limit);
				} else {
					obj.put("action", "error");
					obj.put("message", "계정 정보가 잘못되었거나 존재하지 않습니다.");
				}
			} else {
				obj.put("action", "error");
				obj.put("message", "알수없는 요청 입니다.");
			}
		} catch (Exception e) {
			obj.clear();
			obj.put("action", "error");
			obj.put("message", "정상적인 접근이 아닙니다.");
		}
		//
		StringBuffer sb = new StringBuffer();
		sb.append("var board=").append(obj.toJSONString()).append(";");
		obj.clear();
		obj = null;
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private static void toJavaScriptList(JSONObject obj, String town, int page, int limit) {
		List<Board> list = new ArrayList<Board>();
		JSONArray data = new JSONArray();
		int maxUid = getMaxUid(town);
		// 해당 게시판의 목록 추출.
		if (page == 0)
			getList(town, maxUid, list, limit);
		else
			getList(town, page, list, limit);
		// 목록 정리 및 메모리 정리.
		for (Board b : list) {
			// 정보 정리.
			JSONObject board = new JSONObject();
			board.put("uid", b.getUid());
			board.put("name", b.getName());
			board.put("subject", b.getSubject());
			board.put("date", Util.getLocaleString(b.getDays(), false));
			// 등록
			data.add(board);
			// 메모리 정리.
			setPool(b);
		}
		//
		obj.put("total", maxUid - 1);
		obj.put("board", data);
		//
		list.clear();
	}
}
