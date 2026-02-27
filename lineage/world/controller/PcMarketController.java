package lineage.world.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import lineage.bean.database.Item;
import lineage.bean.database.PcShop;
import lineage.bean.database.marketPrice;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectName;
import lineage.network.packet.server.S_TestInven;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.PcShopInstance;
import lineage.world.object.npc.시세검색;

public class PcMarketController {

	static public Map<Long, PcShopInstance> shop_list;
	static public object marketPriceNPC;
	
	static public void init(Connection con) {
		TimeLine.start("PcMarketController..");

		shop_list = new HashMap<Long, PcShopInstance>();
		marketPriceNPC = new 시세검색();
		marketPriceNPC.setObjectId(ServerDatabase.nextEtcObjId());

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM pc_shop_robot");
			rs = st.executeQuery();

			while (rs.next()) {
				PcShopInstance pc_shop = shop_list.get(rs.getLong("pc_objId"));
				if (pc_shop == null) {
					pc_shop = new PcShopInstance(rs.getLong("pc_objId"), rs.getString("pc_name"),
							rs.getInt("class_type"), rs.getInt("class_sex"));
					shop_list.put(rs.getLong("pc_objId"), pc_shop);
					pc_shop.setX(rs.getInt("loc_x"));
					pc_shop.setY(rs.getInt("loc_y"));
					pc_shop.setMap(rs.getInt("loc_map"));
					pc_shop.setHeading(rs.getInt("heading"));
					pc_shop.shop_comment = rs.getString("ment");
					pc_shop.toTeleport(pc_shop.getX(), pc_shop.getY(), pc_shop.getMap(), false);
				}
			}

			rs.close();
			st.close();

			st = con.prepareStatement("SELECT * FROM pc_shop");
			rs = st.executeQuery();
			while (rs.next()) {
				PcShopInstance pc_shop = shop_list.get(rs.getLong("pc_objId"));
				PcShop s = new PcShop(pc_shop, rs.getLong("price"), rs.getString("aden_type"), rs.getLong("count"));
				s.setInvItemObjectId(rs.getLong("item_objId"));
				s.setItem(ItemDatabase.find(rs.getString("item_name")));
				s.setInvItemCount(rs.getLong("count"));
				s.setInvItemEn(rs.getInt("en_level"));
				s.setInvItemBress(rs.getInt("bless"));
				s.setInvItemDefinite(rs.getString("definite").equalsIgnoreCase("true"));
				pc_shop.list.put(s.getInvItemObjectId(), s);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", PcMarketController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}

		TimeLine.end();
	}
	
	static public void SellAsk(PcInstance pc) {
		ChattingController.toChatting(pc, String.format("\\fW판매할 수량을 지정해주세요."), Lineage.CHATTING_MODE_MESSAGE);
		pc.toSender(S_TestInven.clone(BasePacketPooling.getPool(S_TestInven.class), pc, pc.getInventory().getList()));
		
	}

	static public boolean toCommand(object o, String cmd, StringTokenizer st){
		if (cmd.equalsIgnoreCase(".상점")) {
			shop((PcInstance) o, st);
			return true;
		}
		return false;
	}

	static private void shop(PcInstance pc, StringTokenizer st) {
		try {
			
			// 버그확인.
			if (pc.getMap() != 800) {
				ChattingController.toChatting(pc, "\\fR상점은 시장에서만 이용가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			String type = st.nextToken();
			
			// 초기화
			PcShopInstance pc_shop = shop_list.get(pc.getObjectId());
			if (pc_shop == null) {
				pc_shop = new PcShopInstance(pc.getObjectId(), pc.getName(), pc.getClassType(), pc.getClassSex());
				shop_list.put(pc.getObjectId(), pc_shop);
			}else {
				if (pc_shop.isClose_status()) {
					pc_shop.close();
					PcMarketController.shop_list.remove(pc.getObjectId());
					pc_shop = new PcShopInstance(pc.getObjectId(), pc.getName(), pc.getClassType(), pc.getClassSex());
					shop_list.put(pc.getObjectId(), pc_shop);
				}
			}

			if (type.equalsIgnoreCase("판매")) {
				shopStart(pc, st, pc_shop);
					

			} else if (type.equalsIgnoreCase("추가")) {
				
				if(pc_shop.getPc_objectId() == pc.getObjectId() && pc_shop.getListSize() > 0  ) {
					pc.setShop_inven_num(1);
					pc.setInven_stat_object(pc);
					pc.clearTempList();
					PcMarketController.SellAsk(pc);
				}else {
					ChattingController.toChatting(pc, "\\fT등록된 상점이 존재하지 않습니다", Lineage.CHATTING_MODE_MESSAGE);
				}
				
			} else if (type.equalsIgnoreCase("홍보")) {
				StringBuffer sb = new StringBuffer();
				while (st.hasMoreTokens()) {
					sb.append(st.nextToken());
					if (st.hasMoreTokens())
						sb.append(" ");
				}
				pc_shop.shop_comment = sb.toString();
				ChattingController.toChatting(pc, "\\fR\"" + sb.toString() + "\" 홍보멘트 설정",
						Lineage.CHATTING_MODE_MESSAGE);
			} 
//			else if (type.equalsIgnoreCase("시세")) {
//				shopPrice(pc, st, pc_shop);
//			} 
			else if (type.equalsIgnoreCase("종료")) {

				if (pc_shop.list.size() > 0) {
					for (PcShop s : pc_shop.list.values()) {
						if (s.getItem() == null)
							continue;
						Item item = ItemDatabase.find(s.getItem().getName());
						if (item != null) {
							ItemInstance temp = ItemDatabase.newInstance(item);
							temp.setObjectId(ServerDatabase.nextItemObjId());
							temp.setCount(s.getInvItemCount());
							temp.setEnLevel(s.getInvItemEn());
							temp.setBress(s.getInvItemBress());
							temp.setDefinite(true);
							// 인벤에 등록처리.
							pc.getInventory().append(temp, temp.getCount());
						}
					}
					pc_shop.list.clear();
				}
				pc_shop.close();
				shop_list.remove(pc.getObjectId());
				ChattingController.toChatting(pc, "\\fR상점이 종료되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			} else {
				ChattingController.toChatting(pc, "\\fR[.상점] 옵션 : 시작, 이동, 홍보, 종료", Lineage.CHATTING_MODE_MESSAGE);
			}
		} catch (Exception e) {
			ChattingController.toChatting(pc, "\\fR---------- 상점 명령어 ---------", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fRex) .상점 시작", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fRex) .상점 홍보 홍보멘트", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fRex) .상점 종료", Lineage.CHATTING_MODE_MESSAGE);

		}
	}
	
	
	/**
	 * 상점 시작.
	 * 2019-08-19
	 * by connector12@nate.com
	 */
	static private void shopStart(PcInstance pc, StringTokenizer st, PcShopInstance pc_shop) {
		
	

		pc.setShop_inven_num(1);
		pc.setInven_stat_object(pc);
		pc.clearTempList();
		PcMarketController.SellAsk(pc);
	}
	
	/**
	 * 상점 시작시 상점 로봇 DB 저장.
	 * 2019-08-17
	 * by connector12@nate.com
	 */
	static public void insertShopRobot(PcInstance pc, PcShopInstance pc_shop) {
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("INSERT INTO pc_shop_robot SET pc_objId=?, pc_name=?, loc_x=?, loc_y=?, loc_map=?, class_type=?, class_sex=?, heading=?, ment=?");
			st.setLong(1, pc_shop.getPc_objectId());
			st.setString(2, pc_shop.pc_name);
			st.setInt(3, pc.getX());
			st.setInt(4, pc.getY());
			st.setInt(5, pc.getMap());
			st.setInt(6, pc_shop.classType);
			st.setInt(7, pc_shop.classSex);
			st.setInt(8, pc.getHeading());
			st.setString(9, pc_shop.shop_comment == null ? "" : pc_shop.shop_comment);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : insertShopRobot(PcShopInstance pc_shop)\r\n", PcMarketController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
		
	
//	/**
//	 * 상점 시세.
//	 * 2019-08-19
//	 * by connector12@nate.com
//	 */
//	static private void shopPrice(PcInstance pc, StringTokenizer st, PcShopInstance pc_shop) {
//		try {
//			pc.marketPrice.clear();
//			List<String> list = new ArrayList<String>();
//			String itemName = st.nextToken();
//			int en = 0;
//			int bless = 1;
//			int index = 1;
//			boolean isEn = false;
//			boolean isBless = false;
//			
//			// 인챈 검색
//			if (st.hasMoreTokens()) {
//				en = Integer.valueOf(st.nextToken());
//				
//				if (en != 0)
//					isEn = true;
//			}
//			
//			// 축복 여부 검색
//			if (st.hasMoreTokens()) {
//				switch (st.nextToken()) {
//				case "축" :
//				case "축복" :
//					bless = 0;
//					isBless = true;
//					break;
//				case "일반" :
//					bless = 1;
//					isBless = true;
//					break;
//				case "저주" :
//					bless = 2;
//					isBless = true;
//					break;
//				}
//			}				
//
//			list.add((bless == 0 ? "(축) " : bless == 2 ? "(저주) " : "") + (en > 0 ? "+" + en + " " : en < 0 ? en + " " : "") + itemName);
//
//			for (PcShopInstance shpoList : getShopList().values()) {
//				if (shpoList.getX() > 0 && shpoList.getY() > 0 && shpoList.getPc_objectId() != pc.getObjectId()) {
//					for (PcShop s : shpoList.getShopList_().values()) {
//						if (s.getItem() == null)
//							continue;
//
//						if (s.getItem().getName().contains(itemName) && (!isEn || (isEn && s.getInvItemEn() == en)) && (!isBless || (isBless && s.getInvItemBress() == bless))) {
//							marketPrice mp = new marketPrice();
//							StringBuffer sb = new StringBuffer();
//
//							sb.append(String.format("%d. %s", index++, s.getInvItemBress() == 0 ? "(축) " : s.getInvItemBress() == 1 ? "" : "(저주) "));
//
//							if (s.getInvItemEn() > 0)
//								sb.append(String.format("+%d ", s.getInvItemEn()));
//
//							sb.append(String.format("%s", s.getItem().getName()));
//
//							if (s.getInvItemCount() > 1)
//								sb.append(String.format("(%d)", s.getInvItemCount()));
//
//							sb.append(String.format(" [판매 금액]: %s 아데나", Util.changePrice(s.getPrice())));
//							list.add(sb.toString());
//
//							mp.setShopNpc(shpoList);
//							mp.setX(shpoList.getX());
//							mp.setY(shpoList.getY());
//							mp.setMap(shpoList.getMap());
//							mp.setObjId(shpoList.getObjectId());
//							pc.marketPrice.add(mp);
//						}
//					}
//				} else {
//					continue;
//				}
//			}
//			
//			if (list.size() < 2 || pc.marketPrice.size() < 1) {
//				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), marketPriceNPC, "marketprice1", null, list));
//				return;
//			} else {
//				int count = 60 - (list.size() - 1);
//				for (int i = 0; i < count; i++)
//					list.add(" ");				
//				// 시세 검색 결과 html 패킷 보냄.
//				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), marketPriceNPC, "marketprice", null, list));
//			}
//		} catch (Exception e) {
//			ChattingController.toChatting(pc, "\\fR.상점 시세 아이템", Lineage.CHATTING_MODE_MESSAGE);
//			ChattingController.toChatting(pc, "\\fR.상점 시세 아이템 인첸(0은 모두)", Lineage.CHATTING_MODE_MESSAGE);
//			ChattingController.toChatting(pc, "\\fR.상점 시세 아이템 인첸(0은 모두) 축여부(축,저주,일반)", Lineage.CHATTING_MODE_MESSAGE);
//		}
//	}
	
	static public Map<Long, PcShopInstance> getShopList() {
		synchronized (shop_list) {
			return shop_list;
		}
	}
	
	
	

	/**
	 * PcInventory에서 아이템 사용요청 처리 구간 에서 호출해서 사용함. : 아이템 사용을 하기전에 상점판매목록에 갱신해야 되는지
	 * 확인함. : 성공 여부 리턴하여 그에따라 인벤토리아이템 처리요청구간을 수행함.
	 * 
	 * @param pc
	 * @param item
	 * @return
	 */
	static public boolean isShopToAppend(PcInstance pc, ItemInstance item, long count) {
		// 초기화 안된거 무시.
		PcShopInstance pc_shop = shop_list.get(pc.getObjectId());
		if (pc_shop == null)
			return false;

		// 더이상 등록할 공간이 없는건 일반 아이템 사용하듯 처리.
		if (pc_shop.list.get(0L) == null)
			return false;

		// 착용 한거 무시.
		if (item.isEquipped()) {
			ChattingController.toChatting(pc, "\\fR사용중인 아이템은 등록할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return false;
		}

		// 거래 안되는 아이템은 무시.
		if (!item.getItem().isTrade() || item.getBress() < 0) {
			ChattingController.toChatting(pc, "\\fR거래가 불가능한 아이템 입니다.", Lineage.CHATTING_MODE_MESSAGE);
			return false;
		}

		// 아데나 등록 못하도록 처리
		if (item.getItem().getNameIdNumber() == 4) {
			ChattingController.toChatting(pc, "\\fR아데나는 판매할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return false;
		}

		// 이소스아니면 치트엔진으로 복사버그발생
		if (count > item.getCount()) {
			count = item.getCount();
		}

		PcShop s = pc_shop.list.get(0L);
		pc_shop.list.remove(0L);
		s.setItem(item.getItem());
		if (pc_shop.list.get(item.getObjectId()) != null)
			s.setInvItemObjectId(ServerDatabase.nextItemObjId());
		else
			s.setInvItemObjectId(item.getObjectId());
		s.setInvItemCount(count);
		s.setInvItemEn(item.getEnLevel());
		s.setInvItemBress(item.getBress());
		s.setInvItemDefinite(true);
		pc_shop.list.put(s.getInvItemObjectId(), s);
		if (count > 1)
			ChattingController.toChatting(pc, String.format("개인상점: '%s(%d)' 1개당 %s(%d) 등록", item.getItem().getName(),
					count, s.getAdenType(), s.getPrice()), Lineage.CHATTING_MODE_MESSAGE);
		else {
			if (item.getItem().getType1().equalsIgnoreCase("weapon")
					|| item.getItem().getType1().equalsIgnoreCase("armor"))
				ChattingController.toChatting(pc, String.format("개인상점: '+%d%s' %s(%d) 등록", item.getEnLevel(),
						item.getItem().getName(), s.getAdenType(), s.getPrice()), Lineage.CHATTING_MODE_MESSAGE);
			else
				ChattingController.toChatting(pc,
						String.format("개인상점: '%s' %s(%d) 등록", item.getItem().getName(), s.getAdenType(), s.getPrice()),
						Lineage.CHATTING_MODE_MESSAGE);
		}
		pc.getInventory().count(item, item.getCount() - count, true);
		return true;
	}
	/**
	 * 캐릭명 변경 처리
	 * 2019-07-29
	 * by connector12@nate.com
	 */
	static public void changeName(long objId, String name) {
		if (name != null) {
			PcShopInstance ps = getShop(objId);
			
			if (ps != null) {
				ps.pc_name = name;
				ps.setName(name + "의 상점");
				ps.toSender(S_ObjectName.clone(BasePacketPooling.getPool(S_ObjectName.class), ps), true);
			}
		}
	}
	static public PcShopInstance getShop(long key) {
		synchronized (shop_list) {
			return shop_list.get(key);
		}
	}

	static public void close(Connection con) {
		PreparedStatement st = null;

		try {
			st = con.prepareStatement("DELETE FROM pc_shop_robot");
			st.executeUpdate();
			st.close();

			st = con.prepareStatement("DELETE FROM pc_shop");
			st.executeUpdate();
			st.close();

			long uid = 1;

			for (PcShopInstance pc_shop : shop_list.values()) {
				if (pc_shop.list.size() > 0) {
					st = con.prepareStatement(
							"INSERT INTO pc_shop_robot SET pc_objId=?, pc_name=?, loc_x=?, loc_y=?, loc_map=?, class_type=?, class_sex=?, heading=?, ment=?");
					st.setLong(1, pc_shop.getPc_objectId());
					st.setString(2, pc_shop.pc_name);
					st.setInt(3, pc_shop.getX());
					st.setInt(4, pc_shop.getY());
					st.setInt(5, pc_shop.getMap());
					st.setInt(6, pc_shop.classType);
					st.setInt(7, pc_shop.classSex);
					st.setInt(8, pc_shop.getHeading());
					st.setString(9, pc_shop.shop_comment == null ? "" : pc_shop.shop_comment);
					st.executeUpdate();
					st.close();
					for (PcShop s : pc_shop.list.values()) {
						if (s.getItem() == null)
							continue;
						st = con.prepareStatement(
								"INSERT INTO pc_shop SET uid=?, pc_objId=?, pc_name=?, item_objId=?, item_name=?, bless=?, en_level=?, count=?, aden_type=?, price=?");
						st.setLong(1, uid++);
						st.setLong(2, pc_shop.getPc_objectId());
						st.setString(3, pc_shop.pc_name);
						st.setLong(4, s.getInvItemObjectId());
						st.setString(5, s.getItem().getName());
						st.setInt(6, s.getInvItemBress());
						st.setInt(7, s.getInvItemEn());
						st.setLong(8, s.getInvItemCount());
						st.setString(9, s.getAdenType());
						st.setLong(10, s.getPrice());
						st.executeUpdate();
						st.close();
					}
				}
			}

		} catch (Exception e) {
			lineage.share.System.printf("%s : close(Connection con)\r\n", PcMarketController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}
}
