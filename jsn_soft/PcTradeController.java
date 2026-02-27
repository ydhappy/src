package jsn_soft;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;



import lineage.bean.database.Item;
import lineage.database.BackgroundDatabase;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectLock;
import lineage.share.Common;
import lineage.share.Lineage;
import lineage.world.controller.BoardController;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.BoardInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class PcTradeController {
	static public int uid;
	static public final String PC_TRADE = "유저거래";
	static public final String STATE_SALE = "판매중";
	static public final String STATE_BUY = "구매신청";
	static public final String STATE_TRADE = "거래중";
	static public final String STATE_DEPOSIT = "입금완료";
	static public final String STATE_BUY_CANCEL = "구매취소";
	static public final String STATE_COMPLETE = "거래완료";
	static private List<Itemtrade> pool = new ArrayList<Itemtrade>();
	static private long checkTime;
	
	static public Itemtrade getPool() {
		Itemtrade pt = null;
		
		synchronized (pool) {
			if (pool.size() > 0) {
				pt = pool.get(0);
				pool.remove(0);
			} else {
				pt = new Itemtrade();
			}
		}
		
		return pt;
	}
	
	static public void setPool(Itemtrade pt) {
		synchronized (pool) {
			if(!pool.contains(pt))
				pool.add(pt);
		}
	}
	
	static public int getUid() {
		uid++;
		return uid;
	}
	
	/**
	 * 화폐 자릿수 포맷 변환 메소드.
	 * 2018-07-20
	 * by connector12@nate.com
	 */
	static public String changePrice(int price) {
		String temp_price;
		
		DecimalFormat dc = new DecimalFormat("#,###,###,###,###");
		temp_price = dc.format(price);
		
		return temp_price;
	}
	
	/**
	 * 게시글 제목 포맷 변환 메소드.
	 * 2018-07-21
	 * by connector12@nate.com
	 */
	static public String changeSubject(String state, String subject) {
		String temp_subject;
		
		temp_subject = String.format("[%s]%s", state, subject);
		
		return temp_subject;
	}
	
	/**
	 * 아이템 축복여부 포맷 변환 메소드.
	 * 2018-07-20
	 * by connector12@nate.com
	 */
	static public String changeBless(int bless) {
		String temp_bless;
		
		temp_bless = bless == 1 ? "" : bless == 0 ? "[축] " : "[저주] ";
		
		return temp_bless;
	}
	
	/**
	 * 게시글 내용 포맷 변환 메소드.
	 * 2018-07-21
	 * by connector12@nate.com
	 */
	static public String changeContent(Itemtrade pt) {
		String temp_content;
		StringBuilder content = new StringBuilder();
		Item i = ItemDatabase.find(pt.getItem());
		
		content.append(String.format("거래번호: %d\n", pt.getUid()));
		content.append(String.format("판매자: %s\n", pt.getSell_name()));
		content.append(String.format("구매자: %s\n", pt.getBuy_name()));
		content.append(String.format("거래상황: %s\n", pt.getState()));
		content.append(String.format("판매아데나: %s아데나\n", changePrice(pt.getPrice())));

		if (i != null) {
			if (i.getType1().equalsIgnoreCase("weapon") || i.getType1().equalsIgnoreCase("armor"))
				content.append(String.format("아 이 템: %s+%d %s\n", changeBless(pt.getBless()), pt.getEnchant(), pt.getItem()));
			else
				content.append(String.format("아 이 템: %s\n", pt.getItem()));
		}

	
		
		temp_content = content.toString();
		
		return temp_content;
	}
	
	/**
	 * 판매 아이템 등록 메소드.
	 * 2018-07-18
	 * by connector12@nate.com
	 */
	static public void insertItem(object o, StringTokenizer st) {

		
		try {
			PcInstance pc = (PcInstance) o;
			
			if (o.getLevel() < Lineage.item_trade_sell_min_level) {
				ChattingController.toChatting(o, String.format("아이템판매 등록은 %d레벨 이상 가능합니다.", Lineage.item_trade_sell_min_level), Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
		
			
			int count = accountSaleCount(pc);
			if (Lineage.pc_trade_sale_max_count > 0 && Lineage.pc_trade_sale_max_count <= count) {
				ChattingController.toChatting(o, String.format("계정의 최대 판매 등록은 %d개입니다. 현재 판매등록: %d개", Lineage.pc_trade_sale_max_count, count), Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			String subject = st.nextToken();
//			int price = Integer.valueOf(st.nextToken());
			long itemCount = Long.valueOf(st.nextToken());
			int price = Integer.valueOf(st.nextToken());
			
		
		
			System.out.println("아데나 : "+itemCount);
			System.out.println("현금 : "+price);
			
			if (subject == null || subject.length() < 1) {
				ChattingController.toChatting(o, "제목이 잘못되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			} else if (price < 1 || price > 2000000000) {
				ChattingController.toChatting(o, "가격이 잘못되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			} else if (itemCount < 1 || itemCount > 2000000000) {
				ChattingController.toChatting(o, "아이템 수량이 잘못되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			
			
			
			Itemtrade trade = new Itemtrade();
			trade.setSell_account_uid(pc.getAccountUid());
			trade.setSell_name(pc.getName());
			trade.setSell_objId(pc.getObjectId());
			trade.setState(STATE_SALE);
			trade.setSubject(subject);
			trade.setCount(itemCount);
			trade.setPrice(price);
			trade.setCount(itemCount);
			pc.setItemTrade(trade);
			ChattingController.toChatting(o, "\\fY인벤토리에서 판매할 아이템을 더블클릭해주세요.", Lineage.CHATTING_MODE_MESSAGE);
		} catch (Exception e) {
			// TODO: handle exception
			ChattingController.toChatting(o, Common.COMMAND_TOKEN + "판매등록 제목(띄워쓰기 없이) 가격 아이템수량", Lineage.CHATTING_MODE_MESSAGE);
			
	
		}
	}
	
	/**
	 * 판매 아이템 최종 등록 메소드.
	 * 2018-07-20
	 * by connector12@nate.com
	 */
	static public boolean insertItemFinal(PcInstance pc, ItemInstance item, long count) {
		pc.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
		
		if (pc.getItemTrade() == null)
			return false;
		if (item == null || item.getItem() == null)
			return false;
		
		// 착용 한거 무시.
		if (item.isEquipped()) {
			setPool(pc.getItemTrade());
			pc.setItemTrade(null);
			ChattingController.toChatting(pc, "\\fR사용중인 아이템은 등록할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fR판매등록이 취소되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return true;
		}


		
		// 거래 안되는 아이템은 무시.
		if (!item.getItem().isTrade() || item.getBress() < 0 || !item.getItem().isItemTrade()) {
			setPool(pc.getItemTrade());
			pc.setItemTrade(null);
			ChattingController.toChatting(pc, "\\fR거래가 불가능한 아이템 입니다.", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fR판매등록이 취소되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return true;
		}
		
		
		
//			// 수량확인.
//			if (count < pc.getPcTrade().getCount()) {
//				setPool(pc.getPcTrade());
//				pc.setPcTrade(null);
//				ChattingController.toChatting(pc, "\\fR판매등록할 아덴이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
//				ChattingController.toChatting(pc, "\\fR판매등록이 취소되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
//				return true;
//			}
			
			try {
				pc.getItemTrade().setUid(getUid());
				pc.getItemTrade().setItem(item.getItem().getName());
				pc.getItemTrade().setEnchant(item.getEnLevel());
				pc.getItemTrade().setBless(item.getBress());
				pc.getItemTrade().setContent(changeContent(pc.getItemTrade()));
				
				
				pc.getInventory().count(item, item.getCount() - pc.getItemTrade().getCount(), true);
				
				insertBoard(pc, pc.getItemTrade());
			} catch (Exception e) {
				lineage.share.System.printf("%s : insertItemFinal(PcInstance pc, ItemInstance item, long count)\r\n", PcTradeController.class.toString());
				lineage.share.System.println(e);
			}
		

		return true;
	}
	
	/**
	 * 계정의 판매신청 게시글 갯수 확인 메소드.
	 * 2018-07-21
	 * by connector12@nate.com
	 */
	static public int accountSaleCount(PcInstance pc) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			con = DatabaseConnection.getLineage();

			st = con.prepareStatement("SELECT COUNT(*) as cnt FROM pc_trade WHERE sell_account_uid=? AND state=?");
			st.setLong(1, pc.getAccountUid());
			st.setString(2, STATE_COMPLETE);
			rs = st.executeQuery();
			
			if (rs.next())
				return rs.getInt("cnt");
		} catch (Exception e) {
			lineage.share.System.printf("%s : accountSaleCount(PcInstance pc)\r\n", BoardController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return 0;
	}
	
	/**
	 * 게시판 등록 메소드.
	 * 2018-07-20
	 * by connector12@nate.com
	 */
	static public void insertBoard(PcInstance pc, Itemtrade trade) {
		Connection con = null;
		PreparedStatement st = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("INSERT INTO pc_trade SET uid=?, sell_account_uid=?, sell_name=?, sell_objId=?, state=?, price=?, item=?, enchant=?, bless=?, count=?, "
					+ "write_day=?, subject=?, content=?");
			st.setInt(1, trade.getUid());
			st.setLong(2, trade.getSell_account_uid());
			st.setString(3, trade.getSell_name());
			st.setLong(4, trade.getSell_objId());
			st.setString(5, trade.getState());
			st.setInt(6, trade.getPrice());
			st.setString(7, trade.getItem());
			st.setInt(8, trade.getEnchant());
			st.setInt(9, trade.getBless());
			st.setLong(10, trade.getCount());
			st.setTimestamp(11, new java.sql.Timestamp(System.currentTimeMillis()));
			st.setString(12, trade.getSubject());
			st.setString(13, trade.getContent());
			st.executeUpdate();
			st.close();
			
			BoardInstance itemtradeBoard = BackgroundDatabase.getitemTradeBoard();		
			if (itemtradeBoard != null) {
				try {
					st = con.prepareStatement("INSERT INTO boards SET uid=?, type=?, account_id=?, name=?, days=?, subject=?, memo=?");
					st.setInt(1, trade.getUid());
					st.setString(2, itemtradeBoard.getType());
					st.setString(3, pc.getClient().getAccountId());
					st.setString(4, "");
					st.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
					st.setString(6, changeSubject(trade.getState(), trade.getSubject()));
					st.setString(7, trade.getContent());
					st.executeUpdate();
					
					// 페이지 보기.
					BoardController.toView(pc, itemtradeBoard, trade.getUid());
				} catch (Exception e) {
					lineage.share.System.printf("%s : insertBoard(PcInstance pc, PcTrade trade)\r\n", PcTradeController.class.toString());
					lineage.share.System.println(e);
				}
			}
			
			setPool(pc.getItemTrade());
			pc.setItemTrade(null);
		} catch (Exception e) {
			lineage.share.System.printf("%s : insertBoard(PcInstance pc, PcTrade trade)\r\n", PcTradeController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
}
