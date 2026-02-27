package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage.bean.lineage.Board;
import lineage.share.Lineage;
import lineage.world.object.instance.PcInstance;

public final class AccountDatabase {
	
	static public void updateNoticeUid(Connection con, int uid, int notice_uid){
		PreparedStatement st = null;
		try{
			st = con.prepareStatement("UPDATE accounts SET notice_uid=? WHERE uid=?");
			st.setInt(1, notice_uid);
			st.setInt(2, uid);
			st.executeUpdate();
		}catch(Exception e){
			lineage.share.System.printf("%s : updateNoticeUid(Connection con, int uid, int notice_uid)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		}finally{
			DatabaseConnection.close(st);
		}
	}
	
	static public void updateDaycheck2() {
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET daytime=0");
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateDaycheck()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 계정에 탐 갯수를 업데이트함.
	 * @param uid
	 * @param count
	 */
	static public void updateTam(int uid, int count) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET tam_count=? WHERE uid=?");
			st.setInt(1, count);
			st.setInt(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateTam(int uid, int count)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 계정과 연결된 탐 갯수를 리턴함.
	 * @param uid
	 * @return
	 */
	static public int getTam(int uid) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT tam_count FROM accounts WHERE uid=?");
			st.setInt(1, uid);
			rs = st.executeQuery();
			if(rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			lineage.share.System.printf("%s : getTam(int uid)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return 0;
	}
	
	/**
	 * 공지사항 확인된 칼럼값 추출.
	 * @param con
	 * @param uid
	 * @return
	 */
	static public int getNoticeUid(int uid){
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT notice_uid FROM accounts WHERE uid=?");
			st.setInt(1, uid);
			rs = st.executeQuery();
			if(rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			lineage.share.System.printf("%s : getNoticeUid(int uid)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return 0;
	}
	
	/**
	 * 아이피당 소유가능한 갯수 확인하여 생성 가능여부 리턴함.
	 * @param ip
	 * @return
	 */
	static public boolean isAccountCount(String ip){
		if(Lineage.account_ip_count<=0 || ip==null || ip.length()<=0)
			return true;
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT COUNT(*) FROM accounts WHERE last_ip=?");
			st.setString(1, ip);
			rs = st.executeQuery();
			if(rs.next())
				return rs.getInt(1) < Lineage.account_ip_count;
		} catch (Exception e) {
			lineage.share.System.printf("%s : isAccountCount(String ip)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		
		return true;
	}

	/**
	 * 계정 아이디를 통한 uid값 추출하는 함수.
	 * @param con
	 * @param id
	 * @return
	 */
	static public int getUid(String id) {
		int uid = 0;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM accounts WHERE LOWER(id)=?");
			st.setString(1, id.toLowerCase());
			rs = st.executeQuery();
			if(rs.next())
				uid = rs.getInt(1);
		} catch (Exception e) {
			lineage.share.System.printf("%s : getUid(String id)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return uid;
	}
	
	/**
	 * uid값에 필드에 id와 pw가 일치하는지 확인.
	 * @param con
	 * @param uid
	 * @param id
	 * @param pw
	 * @return
	 */
	static public boolean isAccount(int uid, String id, String pw){
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM accounts WHERE uid=? AND LOWER(id)=? AND LOWER(pw)=?");
			st.setInt(1, uid);
			st.setString(2, id.toLowerCase());
			st.setString(3, pw.toLowerCase());
			rs = st.executeQuery();
			return rs.next();
		} catch (Exception e) {
			lineage.share.System.printf("%s : isAccount(int uid, String id, String pw)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		
		return false;
	}
	
	/**
	 * 예전 슈롬에서 사용하던 디비값 패스워드 처리를 위해 사용할 함수.
	 * @param con
	 * @param uid
	 * @param id
	 * @param pw
	 * @return
	 */
	static public boolean isAccountOld(int uid, String id, String pw){    
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM accounts WHERE uid=? AND LOWER(id)=? AND old_pw=OLD_PASSWORD(?)");
			st.setInt(1, uid);
			st.setString(2, id.toLowerCase());
			st.setString(3, pw.toLowerCase());
			rs = st.executeQuery();
			return rs.next();
		} catch (Exception e) {
			lineage.share.System.printf("%s : isAccountOld(int uid, String id, String pw)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return false;
	}
	
	/**
	 * 블럭된 계정인지 확인.
	 * @param con
	 * @param uid
	 * @param id
	 * @return
	 */
	static public boolean isBlock(int uid){
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM accounts WHERE uid=?");
			st.setInt(1, uid);
			rs = st.executeQuery();
			if(rs.next()){
				try{ return rs.getTimestamp("block_date").getTime()>0; }catch(Exception e){}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : isBlock(int uid)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return false;
	}
	
	/**
	 * uid와 연결된 계정에 소유중인 케릭터 갯수 리턴.
	 * @param con
	 * @param uid
	 * @return
	 */
	static public int getCharacterLength(Connection con, int uid){
		int length = 0;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT COUNT(*) as cnt FROM characters WHERE account_uid=?");
			st.setInt(1, uid);
			rs = st.executeQuery();
			if(rs.next())
				length = rs.getInt(1);
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCharacterLength(Connection con, int uid)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		return length;
	}
	
	
	/**
	 * uid와 연결된 계정의 패스워드를 변경하는 메서드.
	 * @param con
	 * @param uid
	 * @param nPw
	 */
	static public void changePw(int uid, String nPw){
		Connection con = null;
		PreparedStatement st = null;
		try{
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET pw=? WHERE uid=?");
			st.setString(1, nPw);
			st.setInt(2, uid);
			st.executeUpdate();
		}catch(Exception e){
			lineage.share.System.printf("%s : changePw(int uid, String nPw)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		}finally{
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 계정의 남은시간 추출.
	 * @param con
	 * @param uid
	 * @return
	 */
	static public int getTime(int uid){
		int time = 0;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT time FROM accounts WHERE uid=?");
			st.setInt(1, uid);
			rs = st.executeQuery();
			if(rs.next())
				time = rs.getInt(1);
		} catch (Exception e) {
			lineage.share.System.printf("%s : getTime(int uid)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return time;
	}
	
	/**
	 * 계정 추가 처리 함수.
	 * @param con
	 * @param id
	 * @param pw
	 */
	static public void insert(String id, String pw){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("INSERT INTO accounts SET id=?, pw=?, register_date=?, time=?, giran_dungeon_time=?, daycount=?");
			st.setString(1, id);
			st.setString(2, pw);
			st.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
			st.setInt(4, Lineage.flat_rate_price);
			st.setInt(5, Lineage.giran_dungeon_time);
			st.setInt(6, 1);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : insert(Connection con, String id, String pw)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 로그인한 계정에 대한 아이피 업데이트 함수.
	 * @param con
	 * @param uid
	 * @param ip
	 */
	static public void updateIp(int uid, String ip){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET last_ip=? WHERE uid=?");
			st.setString(1, ip);
			st.setInt(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateIp(int uid, String ip)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	/**
	 * 로그인한 계정에 대한 아이피 업데이트 함수.
	 * @param con
	 * @param uid
	 * @param ip
	 */
	static public void updateIp1(int uid, String ip){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET new_ip=? WHERE uid=?");
			st.setString(1, ip);
			st.setInt(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateIp(int uid, String ip)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 로그인한 시간 갱신 처리 함수.
	 * @param con
	 * @param uid
	 */
	static public void updateLoginsDate(int uid){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET logins_date=? WHERE uid=?");
			st.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
			st.setInt(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateLoginsDate(int uid)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 계정 시간 갱신하는 함수.
	 * @param con
	 * @param uid
	 * @param time
	 */
	static public void updateTime(int uid, int time){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET time=? WHERE uid=?");
			st.setInt(1, time);
			st.setInt(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateTime(int uid, int time)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	static public boolean isAccountIp(String ip) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT uid FROM accounts WHERE last_ip=?");
			st.setString(1, ip);
			rs = st.executeQuery();
			return rs.next();
		} catch (Exception e) {
			lineage.share.System.printf("%s : isAccountIp(String ip)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return false;
	}
	
	static public String getAccountPw(int uid) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT pw FROM accounts WHERE uid=?");
			st.setInt(1, uid);
			rs = st.executeQuery();
			if(rs.next())
				return rs.getString("pw");
		} catch (Exception e) {
			lineage.share.System.printf("%s : getAccountPw(int uid)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return "";
	}
	
	static public String getAccountIp(String id) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT last_ip FROM accounts WHERE id=?");
			st.setString(1, id);
			rs = st.executeQuery();
			if(rs.next())
				return rs.getString("last_ip");
		} catch (Exception e) {
			lineage.share.System.printf("%s : getAccountIp(String id)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return null;
	}
	
	static public String getBankAccount(String id) {
		String bankAccount = "";
		String bankName = "";
		String bankNumber = "";
		String bankUserName = "";
		String bankUserNumber = "";
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM accounts WHERE LOWER(id)=?");
			st.setString(1, id.toLowerCase());
			rs = st.executeQuery();
			if (rs.next()) {
				bankAccount = rs.getString("bankaccount");
			
				bankName = rs.getString("bankname");
				bankNumber = rs.getString("banknumber");
				bankUserName = rs.getString("bankusername");
				bankUserNumber = rs.getString("bankusernumber");
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : getBankAccount(String id)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return bankAccount;
	}
	
	static public String getBankName(String id) {
		String bankName = "";		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM accounts WHERE LOWER(id)=?");
			st.setString(1, id.toLowerCase());
			rs = st.executeQuery();
			if (rs.next()) {
				bankName = rs.getString("bankname");
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : bankName(String id)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return bankName;
	}
	static public String getBankNumber(String id) {
		String bankNumber = "";
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM accounts WHERE LOWER(id)=?");
			st.setString(1, id.toLowerCase());
			rs = st.executeQuery();
			if (rs.next()) {
				bankNumber = rs.getString("banknumber");
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : bankNumber(String id)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return bankNumber;
	}
	static public String getBankUserName(String id) {		
		String bankUserName = "";
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM accounts WHERE LOWER(id)=?");
			st.setString(1, id.toLowerCase());
			rs = st.executeQuery();
			if (rs.next()) {
				bankUserName = rs.getString("bankusername");
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : bankUserName(String id)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return bankUserName;
	}
	static public String getBankUserNumber(String id) {		
		String bankUserNumber = "";
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM accounts WHERE LOWER(id)=?");
			st.setString(1, id.toLowerCase());
			rs = st.executeQuery();
			if (rs.next()) {
				bankUserNumber = rs.getString("bankusernumber");
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : bankUserNumber(String id)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return bankUserNumber;
	}
	static public int getTradeUid(String id) {		
		int tradeUid = 99999;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM accounts WHERE LOWER(id)=?");
			st.setString(1, id.toLowerCase());
			rs = st.executeQuery();
			if (rs.next()) {
				tradeUid = rs.getInt("tradeuid");
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : tradeUid(String id)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return tradeUid;
	}
	static public int getBordUid(String id) {		
		int bordUid = 0;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM accounts WHERE LOWER(id)=?");
			st.setString(1, id.toLowerCase());
			rs = st.executeQuery();
			if (rs.next()) {
				bordUid = rs.getInt("borduid");
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : getBordUid(String id)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return bordUid;
	}
	static public void updateBankAccount(PcInstance pc) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET bankaccount=?, bankname=?, banknumber=?, bankusername=?, bankusernumber=? WHERE uid=?");
			st.setString(1, pc.getClient().getBankAccount());
			st.setString(2, pc.getClient().getBankName());
			st.setString(3, pc.getClient().getBankNumber());
			st.setString(4, pc.getClient().getBankUserName());
			st.setString(5, pc.getClient().getBankUserNumber());			
			st.setInt(6, pc.getClient().getAccountUid());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateBankAccount(PcInstance pc)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	static public void updateTradeing(PcInstance pc, boolean i){
		Connection con = null;
		PreparedStatement st = null;
		String str = null;
				if(i == true)
					str = "true";
				else
					str = "false";
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET tradeing=? WHERE uid=?");
			st.setString(1, str);
			st.setInt(2, pc.getClient().getAccountUid());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateTradeing(PcInstance pc)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	static public boolean getTradeing(PcInstance pc) {		
		String tradeing = "";
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM accounts WHERE uid=?");
			st.setInt(1, pc.getClient().getAccountUid());
			rs = st.executeQuery();
			if (rs.next()) {
				tradeing = rs.getString("tradeing");
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : getradeing(String id)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		
		if (tradeing.equalsIgnoreCase("true"))
			return true;
		else
			return false;
	}
	
	static public void updateTradeUid(PcInstance pc, PcInstance use) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET tradeuid=? WHERE uid=?");
			st.setInt(1, use.getClient().getTradeUid());		
			st.setInt(2, pc.getClient().getAccountUid());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateTradeUid(PcInstance pc)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	
	
	static public void updateTradeUid2(PcInstance pc) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET tradeuid=? WHERE uid=?");
			st.setInt(1, 99999);		
			st.setInt(2, pc.getClient().getAccountUid());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateTradeUid(PcInstance pc)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	static public void updateBordUid(PcInstance pc, Board b) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET borduid=? WHERE uid=?");
			st.setLong(1, b.getUid());		
			st.setInt(2, pc.getClient().getAccountUid());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateBordUid(PcInstance pc)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	//보드 초기화
	static public void updateBordUid2(PcInstance pc) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET borduid=? WHERE uid=?");
			st.setLong(1, 0);		
			st.setInt(2, pc.getClient().getAccountUid());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateBordUid(PcInstance pc)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	static public String getOldIp(long uid) {
		String ip = "";
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT new_ip FROM accounts WHERE uid=?");
			st.setLong(1, uid);
			rs = st.executeQuery();
			if (rs.next()) {
				try {
					ip = rs.getString(1);
				} catch (Exception e) {
					ip = "0";
				}
			}
				
		} catch (Exception e) {
			lineage.share.System.printf("%s : getOldIp(int uid)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
//		System.out.println(ip);
		return ip;
	}
	
	static public String getPass2th(long uid) {
		String pass = "";
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT 2th_pass FROM accounts WHERE uid=?");
			st.setLong(1, uid);
			rs = st.executeQuery();
			if (rs.next())
				pass = rs.getString(1);
		} catch (Exception e) {
			lineage.share.System.printf("%s : getPass2th(int uid)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return pass;
	}
	
	static public void updatePass2th(long uid, String pass) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET 2th_pass=? WHERE uid=?");
			st.setString(1, pass);
			st.setLong(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updatePass2th(int uid, int time)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	
	static public void updateenfail(long uid, int enfail) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET en_fail=? WHERE uid=?");
			st.setInt(1, enfail);
			st.setLong(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateenfail(int uid, int time)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	static public void updateDaycheck(int uid) {
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET daytime=0 WHERE uid=?");
			st.setInt(1, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateDaycheck()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	static public void updateDaycp(int daycheck, int uid){
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET daycheck=? WHERE uid=?");
			st.setInt(1, daycheck);
			st.setInt(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateDaycheck()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	static public void updateDayc(){
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET daycheck=0");

			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateDaycheck()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	static public void updateptime(int dayptime,int uid){
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET daytime=? WHERE uid=?");
			st.setInt(1, dayptime);
			st.setInt(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateDaycount()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	static public void updateDaycount(int daycount,int uid){
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET daycount=? WHERE uid=?");
			st.setInt(1, daycount);
			st.setInt(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateDaycount()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	static public void updateGiran(int girancount, int uid){
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE accounts SET giran_dungeon_count=? WHERE uid=?");
			st.setInt(1, girancount);
			st.setInt(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateGiran(int uid, levelcheck )\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	static public int getenfail(PcInstance pc ,long uid) {
		int en = 0;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT en_fail FROM accounts WHERE uid=?");
			st.setLong(1, uid);
			rs = st.executeQuery();
			if (rs.next())
				en = rs.getInt(1);
		} catch (Exception e) {
			lineage.share.System.printf("%s : getenfail(int uid)\r\n", AccountDatabase.class.toString());
			lineage.share.System.println(e);
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return en;
	}
	
	static public void updateuQuestKill(int uid){
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters SET questkill=0 WHERE objID=?");
			st.setInt(1, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateDaycheck()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	static public void updateuRQuestKill(int uid){
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters SET randomquestkill=0 WHERE objID=?");
			st.setInt(1, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : 3()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	static public void updateQuestChapter(int chapter,int uid){
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters SET questchapter=? WHERE objID=?");
			st.setInt(1, chapter);
			st.setInt(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateDaycount()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	//00시에 초기화
	static public void updateRQuest(){
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters SET radomquest=0");

			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateDaycheck()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
		static public void updateRQuestCount(){
			PreparedStatement st = null;
			Connection con = null;
			
			try {
				con = DatabaseConnection.getLineage();
				st = con.prepareStatement("UPDATE characters SET RandomQuestCount=0");

				st.executeUpdate();
			} catch (Exception e) {
				lineage.share.System.printf("%s : updateDaycheck()\r\n", CharactersDatabase.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st);
			}
		}
		static public void updateRQuestKill(){
			PreparedStatement st = null;
			Connection con = null;
			
			try {
				con = DatabaseConnection.getLineage();
				st = con.prepareStatement("UPDATE characters SET randomquestkill= 0");

				st.executeUpdate();
			} catch (Exception e) {
				lineage.share.System.printf("%s : 3()\r\n", CharactersDatabase.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st);
			}
		}
		static public void updateRQuestPlay(){
			PreparedStatement st = null;
			Connection con = null;
			
			try {
				con = DatabaseConnection.getLineage();
				st = con.prepareStatement("UPDATE characters SET RandomQuestPlay= 0");

				st.executeUpdate();
			} catch (Exception e) {
				lineage.share.System.printf("%s : 3()\r\n", CharactersDatabase.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st);
			}
		}
	static public void updaterquest(int rq,int uid){
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters SET radomquest=? WHERE objID=?");
			st.setInt(1, rq);
			st.setInt(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : 2()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	static public void updatequestcount(int count,int uid){
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters SET RandomQuestCount=? WHERE objID=?");
			st.setInt(1, count);
			st.setInt(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : 1()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
}
