package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Warehouse;
import lineage.bean.database.WebAuction;
import lineage.share.Lineage;
import lineage.share.TimeLine;

public class WebAuctionDatabase {
	
	private static List<WebAuction> pool;
	
	public static void init() {
		TimeLine.start("WebAuctionDatabase..");
		
		pool = new ArrayList<WebAuction>();
		
		TimeLine.end();
	}
	

	
	static public WebAuction getPool(){
		WebAuction wa = null;
		synchronized (pool) {
			if(Lineage.memory_recycle && pool.size()>0){
				wa = pool.get(0);
				pool.remove(0);
			}else{
				wa = new WebAuction();
			}
		}
		return wa;
	}
	
	static public void setPool(WebAuction wa){
		if(wa == null)
			return;
		wa.clear();
		if(!Lineage.memory_recycle)
			return;
		synchronized (pool) {
			if(!pool.contains(wa))
				pool.add(wa);
		}
	}
	
	static public void setPool(List<WebAuction> list){
		for(WebAuction wa : list)
			setPool(wa);
		list.clear();
	}
	
	/**
	 * 경매기록을 추출해서 리턴함.
	 * @param uid
	 */
	public static List<WebAuction> getListResult(int uid, int uid1, int uid2, int limit) {
		List<WebAuction> list = new ArrayList<WebAuction>();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			// 경매 이력 추출.
			if(uid1 != -1) {
				if(uid1 > 0)
					st = con.prepareStatement(String.format("SELECT * FROM web_auction WHERE web_is_sell=1 AND account_uid=? AND uid<%d ORDER BY uid DESC LIMIT ?", uid1));
				else
					st = con.prepareStatement("SELECT * FROM web_auction WHERE web_is_sell=1 AND account_uid=? ORDER BY uid DESC LIMIT ?");
				st.setInt(1, uid);
				st.setInt(2, limit);
				rs = st.executeQuery();
				while(rs.next())
					list.add( get(rs) );
				DatabaseConnection.close(st, rs);
			}
			// 입찰 이력 추출.
			if(uid2 != -1) {
				if(uid2 > 0)
					st = con.prepareStatement(String.format("SELECT * FROM web_auction_log WHERE note!='' AND account_uid=? AND uid<%d ORDER BY uid DESC LIMIT ?", uid2));
				else
					st = con.prepareStatement("SELECT * FROM web_auction_log WHERE note!='' AND account_uid=? ORDER BY uid DESC LIMIT ?");
				st.setInt(1, uid);
				st.setInt(2, limit);
				rs = st.executeQuery();
				while(rs.next())
					list.add( getBidding(rs) );
				DatabaseConnection.close(st, rs);
			}
		} catch (Exception e) {
			lineage.share.System.println(WebAuctionDatabase.class.toString()+" : getListResult(int uid)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		// 정렬
		for(int i=0 ; i<list.size() ; ++i) {
			for(int j=0 ; j<list.size() ; ++j) {
				WebAuction a = list.get(i);
				WebAuction b = list.get(j);
				if(a.getUid() > b.getUid()) {
					list.remove(i);
					list.add(i, b);
					list.remove(j);
					list.add(j, a);
				}
			}
		}
		return list;
	}
	
	/**
	 * 경매기록에 갯수를 리턴함.
	 * @param account_uid
	 * @return
	 */
	public static int getCountResult(int uid) {
		int cnt = 0;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			//
			st = con.prepareStatement("SELECT COUNT(*) as cnt FROM web_auction WHERE web_is_sell=1 AND account_uid=?");
			st.setInt(1, uid);
			rs = st.executeQuery();
			if(rs.next())
				cnt += rs.getInt("cnt");
			DatabaseConnection.close(st, rs);
			//
			st = con.prepareStatement("SELECT COUNT(*) as cnt FROM web_auction_log WHERE note!='' AND account_uid=?");
			st.setInt(1, uid);
			rs = st.executeQuery();
			if(rs.next())
				cnt += rs.getInt("cnt");
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCount(int account_uid)\r\n", WebAuctionDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return cnt;
	}
	
	public static void updateBiddingNote(int uid, String note) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement( "UPDATE web_auction_log SET note=? WHERE uid=?" );
			st.setString(1, note);
			st.setInt(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.println(WebAuctionDatabase.class.toString()+" : updateBiddingNote(int uid, String note)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	public static void updateNote(int uid, String note) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement( "UPDATE web_auction SET web_note=? WHERE uid=?" );
			st.setString(1, note);
			st.setInt(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.println(WebAuctionDatabase.class.toString()+" : updateNote(int uid, String note)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	public static void updateIsSell(int uid, boolean is) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement( "UPDATE web_auction SET web_is_sell=? WHERE uid=?" );
			st.setInt(1, is ? 1 : 0);
			st.setInt(2, uid);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.println(WebAuctionDatabase.class.toString()+" : updateIsSell(int uid, boolean is)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 경매에 등록된 물품중 시간이 종료된 것들만 묶어서리턴.
	 * @return
	 */
	public static List<WebAuction> getListEnd(long time) {
		List<WebAuction> list = new ArrayList<WebAuction>();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM web_auction WHERE web_is_sell=0 AND web_end_date<=?");
			st.setTimestamp(1, new Timestamp(time));
			rs = st.executeQuery();
			while(rs.next())
				list.add( get(rs) );
		} catch (Exception e) {
			lineage.share.System.println(WebAuctionDatabase.class.toString()+" : getListEnd(long time)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return list;
	}
	
	public static WebAuction getOnSale(int uid) {
		WebAuction wa = null;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM web_auction WHERE uid=?");
			st.setInt(1, uid);
			rs = st.executeQuery();
			while(rs.next())
				wa = get(rs);
		} catch (Exception e) {
			lineage.share.System.println(WebAuctionDatabase.class.toString()+" : getOnSale(int uid)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return wa;
	}
	
	/**
	 * auction_uid와 연결된 입찰목록 추출.
	 * @param auction_uid
	 * @return
	 */
	public static WebAuction getListBiddingAuction(int auction_uid) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM web_auction_log WHERE auction_uid=? AND note='' ORDER BY bidding_price DESC LIMIT 1");
			st.setInt(1, auction_uid);
			rs = st.executeQuery();
			if(rs.next())
				return getBidding(rs);
		} catch (Exception e) {
			lineage.share.System.println(WebAuctionDatabase.class.toString()+" : getListBiddingAuction(int auction_uid)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return null;
	}
	
	/**
	 * 입찰중 목록 추출.
	 * @param account_uid
	 * @return
	 */
	public static List<WebAuction> getListBidding(int account_uid) {
		List<WebAuction> list = new ArrayList<WebAuction>();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM web_auction_log WHERE account_uid=? ORDER BY insert_date DESC");
			st.setInt(1, account_uid);
			rs = st.executeQuery();
			while(rs.next())
				list.add( getBidding(rs) );
		} catch (Exception e) {
			lineage.share.System.println(WebAuctionDatabase.class.toString()+" : getListBidding(int account_uid)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return list;
	}
	
	/**
	 * 판매중 목록 추출.
	 * @param account_uid
	 * @return
	 */
	public static List<WebAuction> getListOnSale(int account_uid) {
		List<WebAuction> list = new ArrayList<WebAuction>();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM web_auction WHERE web_is_sell=0 AND account_uid=? AND web_end_date>? ORDER BY web_insert_date DESC LIMIT 10");
			st.setInt(1, account_uid);
			st.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			rs = st.executeQuery();
			while(rs.next())
				list.add( get(rs) );
		} catch (Exception e) {
			lineage.share.System.println(WebAuctionDatabase.class.toString()+" : getListOnSale(int account_uid)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return list;
	}
	
	/**
	 * 판매중 목록 추출.
	 * @param account_uid
	 * @return
	 */
	public static List<WebAuction> getListOnSale(int page, int limit, String where) {
		List<WebAuction> list = new ArrayList<WebAuction>();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			if(page == 0) {
				st = con.prepareStatement( String.format("SELECT * FROM web_auction WHERE web_is_sell=0 %s ORDER BY uid DESC LIMIT ?", where) );
				st.setInt(1, limit);
			} else {
				st = con.prepareStatement( String.format("SELECT * FROM web_auction WHERE web_is_sell=0 AND uid<? %s ORDER BY uid DESC LIMIT ?", where) );
				st.setInt(1, page);
				st.setInt(2, limit);	
			}
			rs = st.executeQuery();
			while(rs.next())
				list.add( get(rs) );
		} catch (Exception e) {
			lineage.share.System.println(where);
			lineage.share.System.println(WebAuctionDatabase.class.toString()+" : getListOnSale(int page, int limit, String where)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return list;
	}
	
	public static int getCount(int account_uid) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT COUNT(*) as cnt FROM web_auction WHERE account_uid=? AND web_is_sell=0");
			st.setInt(1, account_uid);
			rs = st.executeQuery();
			if(rs.next())
				return rs.getInt("cnt");
		} catch (Exception e) {
			lineage.share.System.printf("%s : getCount(int account_uid)\r\n", WebAuctionDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return 0;
	}

	public static void insert(int account_uid, Warehouse wh, long price_s, long price_d) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement( "INSERT INTO web_auction SET account_uid=?, inv_id=?, pet_id=?, letter_id=?, name=?, 구분1=?, 구분2=?, type=?, gfxid=?, count=?, quantity=?, en=?, definite=?, bress=?, durability=?, time=?, web_price_s=?, web_price_d=?, web_insert_date=?, web_end_date=?" );
			st.setInt(1, account_uid);
			st.setInt(2, wh.getInvId());
			st.setInt(3, wh.getPetId());
			st.setInt(4, wh.getLetterId());
			st.setString(5, wh.getName());
			st.setString(6, wh.get구분1());
			st.setString(7, wh.get구분2());
			st.setInt(8, wh.getType());
			st.setInt(9, wh.getGfxid());
			st.setLong(10, wh.getCount());
			st.setInt(11, wh.getQuantity());
			st.setInt(12, wh.getEn());
			st.setInt(13, wh.isDefinite() ? 1 : 0);
			st.setInt(14, wh.getBress());
			st.setInt(15, wh.getDurability());
			st.setInt(16, wh.getTime());
			st.setLong(17, price_s);
			st.setLong(18, price_d);
			st.setTimestamp(19, new java.sql.Timestamp(System.currentTimeMillis()));
			st.setTimestamp(20, new java.sql.Timestamp(System.currentTimeMillis() + Lineage.auction_delay));
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : insert(int account_uid, Warehouse wh, long price_s, long price_d)\r\n", WebAuctionDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	public static void insertBidding(int auction_uid, int account_uid, long bidding_price, String note) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement( "INSERT INTO web_auction_log SET auction_uid=?, account_uid=?, bidding_price=?, insert_date=?, note=?" );
			st.setInt(1, auction_uid);
			st.setInt(2, account_uid);
			st.setLong(3, bidding_price);
			st.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
			st.setString(5, note);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : insertBidding(int auction_uid, int account_uid, int bidding_price)\r\n", WebAuctionDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 중복코드 방지용
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	private static WebAuction get(ResultSet rs) throws Exception {
		WebAuction wh = getPool();
		wh.setUid( rs.getInt("uid") );
		wh.setAccountUid( rs.getInt("account_uid") );
		wh.setInvId( rs.getInt("inv_id") );
		wh.setPetId( rs.getInt("pet_id") );
		wh.setLetterId( rs.getInt("letter_id") );
		wh.setName( rs.getString("name") );
		wh.set구분1( rs.getString("구분1") );
		wh.set구분2( rs.getString("구분2") );
		wh.setType( rs.getInt("type") );
		wh.setGfxid( rs.getInt("gfxid") );
		wh.setCount( rs.getInt("count") );
		wh.setQuantity( rs.getInt("quantity") );
		wh.setEn( rs.getInt("en") );
		wh.setDefinite( rs.getInt("definite")==1 );
		wh.setBress( rs.getInt("bress") );
		wh.setDurability( rs.getInt("durability") );
		wh.setTime( rs.getInt("time") );
		wh.setWebPriceS( rs.getInt("web_price_s") );
		wh.setWebPriceD( rs.getInt("web_price_d") );
		wh.setInsertDate( rs.getTimestamp("web_insert_date").getTime() );
		wh.setWebEndDate( rs.getTimestamp("web_end_date").getTime() );
		wh.setWebIsSell( rs.getInt("web_is_sell")==1 );
		wh.setNote( rs.getString("web_note") );
		
		return wh;
	}
	
	/**
	 * 중복코드 방지용
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	private static WebAuction getBidding(ResultSet rs) throws Exception {
		WebAuction wh = getPool();
		wh.setUid( rs.getInt("uid") );
		wh.setAuctionUid( rs.getInt("auction_uid") );
		wh.setAccountUid( rs.getInt("account_uid") );
		wh.setBiddingPrice( rs.getInt("bidding_price") );
		wh.setNote( rs.getString("note") );
		wh.setInsertDate( rs.getTimestamp("insert_date").getTime() );
		
		return wh;
	}
	
}
