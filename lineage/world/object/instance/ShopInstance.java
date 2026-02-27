package lineage.world.object.instance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.database.Shop;
import lineage.bean.lineage.Inventory;
import lineage.bean.lineage.Kingdom;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.gui.GuiMain;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectAttack;
import lineage.network.packet.server.S_ObjectHeading;
import lineage.network.packet.server.S_ObjectHitratio;
import lineage.network.packet.server.S_ShopBuy;
import lineage.network.packet.server.S_ShopSell;
import lineage.plugin.PluginController;
import lineage.share.Common;
import lineage.share.Lineage;
import lineage.share.Log;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.controller.DamageController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.item.RaceTicket;

public class ShopInstance extends object {

	protected Npc npc;
	// 성 정보
	protected Kingdom kingdom;

	protected int ment_show_sec;		// 몇초단위로 멘트를 표현할지 처리하는 변수.
	private int ment_counter;			// 멘트 발사할 주기에 사용될 변수.
	private int ment_index;				// 멘트가 발사된 위치 파악용 변수.
	protected List<String> list_ment;	// 멘트 발사할 목록.
	protected List<object> attackList;		// 전투 목록
	protected int ai_walk_stay_count;		// 랜덤워킹 중 잠시 휴식을 취하기위한 카운팅 값
	protected boolean ai_talk;				// 대화를 걸었는지 여부 확인용.
	private int reSpawnTime;				// 재스폰 하기위한 대기 시간값.
	// 시체유지(toAiCorpse) 구간에서 사용중.
	// 재스폰대기(toAiSpawn) 구간에서 사용중.
	private long ai_time_temp_1;
	// 인벤토리
	private Inventory inv;
	//
	private List<object> temp_list;		// 주변셀 검색에 임시 담기용으로 사용.
	
	public ShopInstance(Npc npc) {
		this.npc = npc;
		kingdom = null;
		
		list_ment = new ArrayList<String>();
		attackList = new ArrayList<object>();
		temp_list = new ArrayList<object>();
	}

	@Override
	public void toTimer(long time){
		// 멘트 없을땐 무시.
		if(list_ment.size()==0)
			return;
		
		// 멘트 표현하기.
		if(++ment_counter%ment_show_sec == 0){
			ment_counter = 0;
			// 추출
			String msg = list_ment.get(ment_index);
			// 처리
			if(msg != null)
				ChattingController.toChatting(this, msg, Lineage.CHATTING_MODE_NORMAL);
			// 정리
			ment_index = list_ment.size()<=++ment_index ? 0 : ment_index;
		}
	}


	/**
	 * 현재 물가 추출.
	 * 
	 * @return
	 */
	public int getTax() {
		//
		int tax = kingdom == null ? 0 : kingdom.getTaxRate();
		//
		Object o = PluginController.init(ShopInstance.class, "getTax", this, kingdom);
		if (o != null)
			tax = (Integer) o;
		//
		return tax;
	}

	/**
	 * 세금으로인한 차액을 공금에 추가하기.
	 * 
	 * @param price
	 */
	public void addTax(int price) {
		if (PluginController.init(ShopInstance.class, "addTax", this, kingdom, price) == null && kingdom != null)
			kingdom.toTax(price, true, "shop");
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		if (action.equalsIgnoreCase("buy")) {
			pc.toSender(S_ShopBuy.clone(BasePacketPooling.getPool(S_ShopBuy.class), this));
		} else if (action.equalsIgnoreCase("sell")) {
			List<ItemInstance> sell_list = new ArrayList<ItemInstance>();
			for (Shop s : npc.getShop_list()) {
				// 판매할 수 있도록 설정된 목록만 처리.
				if (s.isItemSell()) {
					List<ItemInstance> search_list = new ArrayList<ItemInstance>();
					pc.getInventory().findDbName(s.getItemName(), s.getItemBress(), s.getItemEnLevel(), search_list);
					for (ItemInstance item : search_list) {
						if (!item.isEquipped() && item.getItem().isSell() && s.getItemEnLevel() == item.getEnLevel()) {
							//
							if (isSellAdd(item) && !sell_list.contains(item))
								sell_list.add(item);
						}
					}
				}
			}
			if (sell_list.size() > 0)
				pc.toSender(S_ShopSell.clone(BasePacketPooling.getPool(S_ShopSell.class), this, sell_list));
			else
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "nosell"));
		} else if (action.indexOf("3") > 0 || action.indexOf("6") > 0 || action.indexOf("7") > 0) {
			List<String> list_html = new ArrayList<String>();
			list_html.add(String.valueOf(getTax()));
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, action, null, list_html));
		}
	}

	@Override
	public void toDwarfAndShop(PcInstance pc, ClientBasePacket cbp) {
		//
		if (PluginController.init(ShopInstance.class, "toDwarfAndShop", this, pc, cbp) != null)
			return;
		//
		switch (cbp.readC()) {
		case 0: // 상점 구입
			toBuy(pc, cbp);
			break;
		case 1: // 상점 판매
			toSell(pc, cbp);
			break;
		}
	}

	/**
	 * 상점 구매
	 */
	protected void toBuy(PcInstance pc, ClientBasePacket cbp) {
		if (Lineage.open_wait && pc.getGm() == 0) {
			ChattingController.toChatting(pc, "[오픈 대기] 상점을 이용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		int count = cbp.readH();
		if (count > 0 && count <= 100) {
			for (int j = 0; j < count; ++j) {
				long item_idx = cbp.readD();
				int item_count = (int) cbp.readD();
				if (item_count > 2000000000 || item_count < 0)
					return;
				if (item_count > 0 && item_count <= 1000) {
					Shop s = npc.findShop(item_idx);
					if (s != null) {
						Item i = ItemDatabase.find(s.getItemName());
						int shop_price = s.getPrice() !=0 ? getTaxPrice(s.getPrice(), false) : getTaxPrice(s.getPrice(), false);
						// 아이템 갯수에 맞게 갯수 재 설정.
						int new_item_count = item_count * s.getItemCount();
						if (new_item_count < 0 || new_item_count > 2000000000) {
							return;
						}

						if ((long) shop_price * (long) item_count < 0
								|| (long) shop_price * (long) item_count > 2000000000) {
							return;
						}

						//
						if (pc.getInventory().isAppend(i, count, i.isPiles() ? 1 : new_item_count)) {

							if (pc.getInventory().isAden(s.getAdenType(), shop_price * item_count, true)) {
								//
								ItemInstance temp = pc.getInventory().find(s.getItemName(), s.getItemBress(), true);
								if (temp == null) {
									// 겹칠수 있는 아이템이 존재하지 않을경우.
									if (i.isPiles()) {
										temp = ItemDatabase.newInstance(i);
										temp.setObjectId(ServerDatabase.nextItemObjId());
										if (ItemDatabase.isQuantityWand(temp))
											temp.setQuantity((int) new_item_count);
										else
											temp.setCount(new_item_count);
										temp.setEnLevel(s.getItemEnLevel());
										temp.setBress(s.getItemBress());
										temp.setNowTime(s.getItemTime());
										temp.setDefinite(true);
										if (s.getAddTime() != null && s.getAddTime().contains(":")) {
											Timestamp ts = new Timestamp(System.currentTimeMillis());
											String[] addTime = s.getAddTime().split(":");

											int addHour = Integer.valueOf(addTime[0]);
											int addMin = Integer.valueOf(addTime[1]);
											int addSec = 0;
											int rate = 0;
											if (addTime.length > 2)
												addSec = Integer.valueOf(addTime[2]);
											if (ts.getSeconds() + addSec > 60) {
												rate = (ts.getSeconds() + addSec) / 60;
												ts.setMinutes(ts.getMinutes() + rate);
												addSec -= rate * 60;
											}
											if (ts.getMinutes() + addMin > 60) {
												rate = (ts.getMinutes() + addMin) / 60;
												ts.setHours(ts.getHours() + rate);
												addMin -= rate * 60;
											}
											if (ts.getHours() + addHour > 24) {
												rate = (ts.getHours() + addHour) / 24;
												ts.setDate(ts.getDate() + rate);
												addHour -= rate * 24;
											}
											int hour = ts.getHours() + addHour;
											int min = ts.getMinutes() + addMin;
											int sec = ts.getSeconds() + addSec;
											
											ts.setHours(hour);
											ts.setMinutes(min);
											ts.setSeconds(sec);
											
											ChattingController.toChatting(pc, "" +i.getName() +" " + ts.toString() + " 날자에 사라집니다.", Lineage.CHATTING_MODE_MESSAGE);
											
											temp.setTimestamp(ts.toString());
											temp.setTimeCheck(true);
										} else 
											temp.setTimeCheck(false);
										pc.getInventory().append(temp, true);
										//
										Log.appendItem(pc, "type|상점구입",
												String.format("npc_name|%s", getNpc().getName()),
												String.format("item_name|%s", temp.toStringDB()),
												String.format("item_objid|%d", temp.getObjectId()),
												String.format("count|%d", item_count),
												String.format("shop_uid|%d", s.getUid()),
												String.format("shop_count|%d", s.getItemCount()));
									} else {
										for (int k = 0; k < new_item_count; ++k) {
											temp = ItemDatabase.newInstance(i);
											temp.setObjectId(ServerDatabase.nextItemObjId());
											// 겜블 아이템은 겹칠일이 없어서 여기다가 넣음.
											if (s.isGamble()) {
												temp.setEnLevel(getGambleEnLevel());
											} else {
												temp.setEnLevel(s.getItemEnLevel());
												temp.setDefinite(true);
											}
											temp.setNowTime(s.getItemTime());
											temp.setBress(s.getItemBress());
											if (s.getAddTime() != null && s.getAddTime().contains(":")) {
												Timestamp ts = new Timestamp(System.currentTimeMillis());
												String[] addTime = s.getAddTime().split(":");

												int addHour = Integer.valueOf(addTime[0]);
												int addMin = Integer.valueOf(addTime[1]);
												int addSec = 0;
												int rate = 0;
												if (addTime.length > 2)
													addSec = Integer.valueOf(addTime[2]);
												if (ts.getSeconds() + addSec > 60) {
													rate = (ts.getSeconds() + addSec) / 60;
													ts.setMinutes(ts.getMinutes() + rate);
													addSec -= rate * 60;
												}
												if (ts.getMinutes() + addMin > 60) {
													rate = (ts.getMinutes() + addMin) / 60;
													ts.setHours(ts.getHours() + rate);
													addMin -= rate * 60;
												}
												if (ts.getHours() + addHour > 24) {
													rate = (ts.getHours() + addHour) / 24;
													ts.setDate(ts.getDate() + rate);
													addHour -= rate * 24;
												}
												int hour = ts.getHours() + addHour;
												int min = ts.getMinutes() + addMin;
												int sec = ts.getSeconds() + addSec;
												
												ts.setHours(hour);
												ts.setMinutes(min);
												ts.setSeconds(sec);
												
												ChattingController.toChatting(pc, "" +i.getName() +" " + ts.toString() + " 날자에 사라집니다.", Lineage.CHATTING_MODE_MESSAGE);
												
												temp.setTimestamp(ts.toString());
												temp.setTimeCheck(true);
											} else 
												temp.setTimeCheck(false);
											pc.getInventory().append(temp, true);
											//
											Log.appendItem(pc, "type|상점구입",
													String.format("npc_name|%s", getNpc().getName()),
													String.format("item_name|%s", temp.toStringDB()),
													String.format("item_objid|%d", temp.getObjectId()),
													String.format("count|%d", item_count),
													String.format("shop_uid|%d", s.getUid()),
													String.format("shop_count|%d", s.getItemCount()));
											if (!Common.system_config_console && pc instanceof PcInstance) {

												long time = System.currentTimeMillis();
												String timeString = Util.getLocaleString(time, true);
												String log = String.format(
														"[%s]\t[구매상점]\t캐릭터: %s\t캐릭터obj_id: %d\t  아이템: %s\t 구입가격: %d",
														timeString, pc.getName(), pc.getObjectId(),
														temp.toStringDB(), (shop_price));

												GuiMain.display.asyncExec(new Runnable() {
													public void run() {
														GuiMain.getViewComposite().getMaeipComposite().toLog(log);
													}
												});
											}

										}
									}

								} else {
									// 겹치는 아이템이 존재할 경우.
									ItemInstance temp2 = ItemDatabase.newInstance(i);
									pc.getInventory().count(temp, temp2, temp.getCount() + new_item_count, true);
									ItemDatabase.setPool(temp2);
									//
									Log.appendItem(pc, "type|상점구입", String.format("npc_name|%s", getNpc().getName()),
											String.format("item_name|%s", s.getItemName()),
											String.format("target_name|%s", temp.toStringDB()),
											String.format("target_objid|%d", temp.getObjectId()),
											String.format("count|%d", item_count),
											String.format("shop_uid|%d", s.getUid()),
											String.format("shop_count|%d", s.getItemCount()));
									if (!Common.system_config_console && pc instanceof PcInstance) {

										long time = System.currentTimeMillis();
										String timeString = Util.getLocaleString(time, true);
										String log = String.format(
												"[%s]\t[구매상점]\t캐릭터: %s\t캐릭터obj_id: %d\t  아이템: %s\t 구매가격: %d",
												timeString, pc.getName(), pc.getObjectId(),
												Util.getItemNameToString(temp, item_count), (shop_price * item_count));

										GuiMain.display.asyncExec(new Runnable() {
											public void run() {
												GuiMain.getViewComposite().getMaeipComposite().toLog(log);
											}
										});
									}

								}
								// 아데나일때만 처리.
								if (s.getAdenType().equalsIgnoreCase("아데나")) {
									// 로봇도 처리할지 여부.
									if (!Lineage.robot_auto_tax && pc instanceof RobotInstance)
										return;
									// 세금으로인한 차액을 공금에 추가.
									//System.out.println(shop_price);
									//System.out.println(s.getPrice());
									//System.out.println(item_count);
									if (s.getPrice() != 0)
										addTax((int) ((shop_price - s.getPrice()) * item_count));
									
									else
										addTax((int) ((shop_price - i.getShopPrice()) * item_count));
								}

							} else {
								// \f1아데나가 충분치 않습니다.
								// pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class),
								// 189));
								ChattingController.toChatting(pc, String.format("%s가 충분치 않습니다.", s.getAdenType()),
										20);
								break;
							}

						}
					}
				}
			}
		}
	}

	/**
	 * 상점 판매
	 */
	protected void toSell(PcInstance pc, ClientBasePacket cbp) {
		if (Lineage.open_wait && pc.getGm() == 0) {
			ChattingController.toChatting(pc, "[오픈 대기] 상점을 이용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		Connection con = null;
		int count = cbp.readH();
		if (count > 0) {
			try {
				con = DatabaseConnection.getLineage();

				for (int i = 0; i < count; ++i) {
					long inv_id = cbp.readD();
					int item_count = (int) cbp.readD();
					if (item_count > 2000000000 || item_count < 0)
						return;
					ItemInstance temp = pc.getInventory().value(inv_id);
					if (temp != null && !temp.isEquipped() && item_count > 0 && temp.getCount() >= item_count) {
						//
						String target_name = temp.toStringDB();
						long target_objid = temp.getObjectId();
						long aden_objid = 0;
						//
						Shop s = npc.findShopItemId(temp.getItem().getName(), temp.getBress(), temp.getEnLevel());
						// 판매될수 있는 아이템만 처리.
						if (s != null && s.isItemSell()) {
							// 가격 체크
							long target_price = getPrice(con, temp);

							if ((long) target_price * (long) item_count < 0
									|| (long) target_price * (long) item_count > 2000000000) {
								return;
							}
							// 아덴 지급
							if (target_price > 0) {
								ItemInstance aden = pc.getInventory().find(s.getAdenType(), true);
								if (aden == null) {
									aden = ItemDatabase.newInstance(ItemDatabase.find(s.getAdenType()));
									aden.setObjectId(ServerDatabase.nextItemObjId());
									aden.setCount(0);
									pc.getInventory().append(aden, true);
								}
								aden_objid = aden.getObjectId();
								//
								pc.getInventory().count(aden, aden.getCount() + (target_price * item_count), true);
								//

								Log.appendItem(pc, "type|상점판매금", "npc_name|" + getNpc().getName(),
										"aden_name|" + s.getAdenType(), "aden_objid|" + aden_objid,
										"target_name|" + target_name, "target_objid|" + target_objid,
										"target_price|" + target_price, "item_count|" + item_count);
								if (!Common.system_config_console && pc instanceof PcInstance) {

									long time = System.currentTimeMillis();
									String timeString = Util.getLocaleString(time, true);
									String log = String.format(
											"[%s]\t[매입상점]\t캐릭터: %s\t캐릭터obj_id: %d\t  아이템: %s\t 매입가격: %d", timeString,
											pc.getName(), pc.getObjectId(), Util.getItemNameToString(temp, item_count),
											(target_price * item_count));

									GuiMain.display.asyncExec(new Runnable() {
										public void run() {
											GuiMain.getViewComposite().getMaeipComposite().toLog(log);
										}
									});
								}
								// 세금계산은 아데나일때만 처리.
//								if (s.getAdenType().equalsIgnoreCase("아데나")) {
//									// 세금으로인한 차액을 공금에 추가.
//									if (s.getPrice() != 0)
//										addTax((int) ((s.getPrice() * 0.5) - target_price));
//									else
//										addTax((int) ((temp.getItem().getShopPrice() * 0.5) - target_price));
//								}
							}
							// 판매되는 아이템 제거.
							pc.getInventory().count(temp, temp.getCount() - item_count, true);
							//
							Log.appendItem(pc, "type|상점판매", String.format("npc_name|%s", getNpc().getName()),
									String.format("target_name|%s", target_name),
									String.format("target_objid|%d", target_objid),
									String.format("target_price|%d", target_price),
									String.format("item_count|%d", item_count));


						}
					}
				}
			} catch (Exception e) {
			} finally {
				DatabaseConnection.close(con);
			}
		}
	}

	/**
	 * 설정된 세율에 따라 가격을 연산하여 리턴함.
	 * 
	 * @param price
	 * @return
	 */
		// sell 일경우에만 기본가격에 반값
	public int getTaxPrice(double price, boolean sell) {
		// sell 일경우 기본가격의 35%
		double a = sell ? price * Lineage.sell_item_rate : price;
		// 세율값 +@ 또는 -@ [원가에 지정된 세율만큼만]
			if (sell)
				a -= a * (getTax() * 0.01); // 0.01
			else
				a += a * (getTax() * 0.01); // 0.01
		// 반올림 처리.
		return (int) Math.round(a);
	}
	
	/**
	 * 겜블 상점에서 아이템 구매시 인첸트 값 추출해주는 함수.
	 * 
	 * @return
	 */
	private int getGambleEnLevel() {
		int a = Util.random(0, 100);
		int b = Util.random(0, 100);
		int c = 0;
		if ((a <= 80) && (b <= 80)) {
			c = Util.random(3, 6);
		} else if ((a <= 99) && (b <= 99)) {
			c = Util.random(0, 2);
		} else {
			c = 7;
		}
		switch (c) {
		case 0:
			switch (Util.random(0, 2)) {
			case 0:
				return Util.random(6, 7);
			case 1:
				return Util.random(4, 6);
			case 2:
				return Util.random(2, 6);
			}
			break;
		case 1:
			switch (Util.random(0, 2)) {
			case 0:
				return Util.random(5, 6);
			case 1:
				return Util.random(3, 6);
			case 2:
				return Util.random(1, 6);
			}
			break;
		case 2:
			switch (Util.random(0, 2)) {
			case 0:
				return Util.random(4, 5);
			case 1:
				return Util.random(2, 5);
			case 2:
				return Util.random(0, 5);
			}
			break;
		case 3:
			switch (Util.random(0, 2)) {
			case 0:
				return Util.random(3, 4);
			case 1:
				return Util.random(1, 4);
			case 2:
				return Util.random(0, 4);
			}
			break;
		case 4:
			switch (Util.random(0, 2)) {
			case 0:
				return Util.random(2, 3);
			case 1:
				return Util.random(1, 2);
			case 2:
				return Util.random(0, 3);
			}
			break;
		case 5:
			switch (Util.random(0, 1)) {
			case 0:
				return Util.random(1, 2);
			case 1:
				return Util.random(0, 2);
			}
			break;
		case 6:
			return 0;
		default:
			switch (Util.random(0, 2)) {
			case 0:
				return Util.random(6, 7);
			case 1:
				return Util.random(3, 6);
			case 2:
				return Util.random(0, 6);
			}
			break;
		}
		return 0;
	}

	/**
	 * 레이스 상점에 표현될 아이템에 가격을 추출.
	 * 
	 * @param item
	 * @param shop
	 * @return
	 */
	public int getPrice(Connection con, ItemInstance item) {
		// 슬라임 레이스표 가격 추출.
		if (item instanceof RaceTicket) {
			RaceTicket ticket = (RaceTicket) item;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				// 로그 참고로 목록 만들기.
				st = con.prepareStatement("SELECT * FROM race_log WHERE uid=? AND race_idx=? AND type=?");
				st.setInt(1, ticket.getRaceUid());
				st.setInt(2, ticket.getRacerIdx());
				st.setString(3, ticket.getRacerType());
				rs = st.executeQuery();
				if (rs.next())
					return rs.getInt("price");
			} catch (Exception e) {
				lineage.share.System.println(ShopInstance.class + " : getPrice(Connection con, ItemInstance item)");
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(st, rs);
			}
			// 당첨 안된거 0원
			return 0;
		}

		Shop shop = npc.findShopItemId(item.getItem().getName(), item.getBress(), item.getEnLevel());
		// 그외 일반 아이템 가격 추출.
		if (item == null || shop == null || item.getItem().getNameIdNumber() == 61
				|| item.getItem().getNameIdNumber() == 93 || item.getItem().getNameIdNumber() == 773) {
			// 화살 및 은화살은 가격책정하면 안됨.
			// 버그도 무시.
			return 0;
		} else if (item.getItem().getName().equalsIgnoreCase("수의 룬")
				|| item.getItem().getName().equalsIgnoreCase("풍의 룬")
				|| item.getItem().getName().equalsIgnoreCase("지의 룬")
				|| item.getItem().getName().equalsIgnoreCase("화의 룬")) {
			// 2017 06 22
			return 3;
		} else if (item.getItem().getName().equalsIgnoreCase("무의 룬")) {
			return 10;
		} else if (item.getItem().getName().equalsIgnoreCase("노멀 카우방의 열쇠")) {
			return 5;
		} else if (item.getItem().getName().equalsIgnoreCase("나이트메어 카우방의 열쇠")) {
			return 28;
		} else if (item.getItem().getName().equalsIgnoreCase("헬 카우방의 열쇠")) {
			return 54;
		} else {
			if (shop.getPrice() != 0)
				return getTaxPrice(shop.getPrice(), true);
			else
				return getTaxPrice(item.getItem().getShopPrice(), true);
		}
	}

	/**
	 * 상점 판매목록에 추가해도 되는지 확인해주는 함수.
	 * 
	 * @return
	 */
	protected boolean isSellAdd(ItemInstance item) {
		return true;
	}

	@Override
	public void close(){
		super.close();
		
		ai_time_temp_1 = reSpawnTime = 0;
		if(attackList != null) {
			synchronized (attackList) {
				attackList.clear();
			}
		}
		if(temp_list != null)
			temp_list.clear();
	}
	
	public void removeAttackList(object o){
		synchronized (attackList) {
			attackList.remove(o);
		}
	}
	
	private void appendAttackList(object o){
		synchronized (attackList) {
			if(!attackList.contains(o))
				attackList.add(o);
		}
	}
	
	private List<object> getAttackList(){
		synchronized (attackList) {
			return new ArrayList<object>(attackList);
		}
	}

	public boolean containsAttackList(object o){
		synchronized (attackList) {
			return attackList.contains(o);
		}
	}
	
	@Override
	public Npc getNpc(){
		return npc;
	}
	
	@Override
	public Inventory getInventory(){
		return inv;
	}
	
	@Override
	public void setInventory(Inventory inv){
		this.inv = inv;
	}

	@Override
	public void setDead(boolean dead) {
		super.setDead(dead);
		if(dead){
			ai_time = SpriteFrameDatabase.find(gfx, gfxMode+8);
			setAiStatus(2);
		}
	}

	public int getReSpawnTime() {
		return reSpawnTime;
	}

	@Override
	public void setReSpawnTime(int reSpawnTime) {
		this.reSpawnTime = reSpawnTime;
	}

	@Override
	public void toDamage(Character cha, int dmg, int type, Object...opt){
		// 공격목록에 추가.
		addAttackList(cha);
	}

	
	/**
	 * 공격자 목록에 등록처리 함수.
	 * @param o
	 */
	public void addAttackList(object o){
		if(o == null)
			return;
		
		if(!o.isDead() && o.getObjectId()!=getObjectId())
			appendAttackList(o);
	}
	
	/**
	 * 전투목록에서 원하는 위치에있는 객체 찾아서 리턴.
	 * @param index
	 * @return
	 */
	protected object getAttackList(int index){
		try {
			if(attackList.size()>index) {
				synchronized (attackList) {
					return attackList.get(index);
				}
			}
		} catch (Exception e) {}
		return null;
	}

	
	/**
	 * 해당객체를 공격해도 되는지 분석하는 함수.
	 * @param o
	 * @param walk	: 랜덤워킹 상태 체크인지 구분용
	 * @return
	 */
	protected boolean isAttack(object o, boolean walk){
		if(o == null)
			return false;
		if(o.isDead())
			return false;
		if(o.isWorldDelete())
			return false;
		if(o.getGm()>0)
			return false;
		if(o.isInvis())
			return false;
		if(o.isTransparent())
			return false;
		if(!Util.isDistance(this, o, Lineage.SEARCH_MONSTER_TARGET_LOCATION))
			return false;
		return true;
	}
	
	@Override
	public void toAi(long time){
		switch(ai_status){
			// 공격목록이 발생하면 공격모드로 변경. 대신 대화걸린 상태가 아닐때만.
			case 0:
				if(ai_talk==false && attackList.size()>0)
					setAiStatus(1);
				break;
				
			// 전투 처리부분은 항상 타켓들이 공격가능한지 확인할 필요가 있음.
			case 1:
			case 5:
				// 공격자 목록 체크해서 목록에서 제거하는 구간인데..
				// 피케이들은 여기서 제거가 안되서 계속 경비병이 피커를 공격함.(마을이고, 12셀 안에 있을때.)
				// 개선해야함 - psjump
				for(object o : getAttackList()){
					if(!isAttack(o, false))
						removeAttackList(o);
				}
				// 전투목록이 없을경우 랜덤워킹으로 변경. 누군가 나에게 대화를 걸었을경우 랜덤워킹으로 변경.
				if(ai_talk || attackList.size()==0)
					setAiStatus(0);
				break;
		}
		
		super.toAi(time);
	}
	
	@Override
	protected void toAiWalk(long time){
		super.toAiWalk(time);
		
		// 아직 휴식카운팅값이 남앗을경우 리턴.
		if(ai_walk_stay_count-->0)
			return;
		
		// 대화를 걸었는지 여부 확인하는 변수 초기화.
		if(ai_talk)
			ai_talk = false;
		
		do{
			switch(Util.random(0, 5)){
				case 0:
					ai_walk_stay_count = Util.random(5, 10);
					break;
				case 1:
				case 2:
					setHeading(getHeading()+1);
					break;
				case 3:
				case 4:
					setHeading(getHeading()-1);
					break;
				default:
					setHeading(Util.random(0, 7));
					break;
			}
			// 이동 좌표 추출.
			int x = Util.getXY(heading, true)+this.x;
			int y = Util.getXY(heading, false)+this.y;
			// 스폰된 위치에서 너무 벗어낫을경우 스폰쪽으로 유도하기.
			if(!Util.isDistance(x, y, map, homeX, homeY, homeMap, Lineage.SEARCH_LOCATIONRANGE*2)){
				heading = Util.calcheading(this, homeX, homeY);
				x = Util.getXY(heading, true)+this.x;
				y = Util.getXY(heading, false)+this.y;
			}
			// 해당 좌표 이동가능한지 체크.
			boolean tail = World.isThroughObject(this.x, this.y, this.map, heading);
			// 해당좌표에 객체 있는지 확인.
			boolean obj = false;
			temp_list.clear();
			findInsideList(x, y, temp_list);
			for(object o : temp_list){
				if(o instanceof Character){
					obj = true;
					break;
				}
			}
			if(tail && !obj){
				// 타일이 이동가능하고 객체가 방해안하면 이동처리.
				super.toMoving(x, y, heading);
			}else{
				if(Util.random(0, 3) != 0)
					continue;
			}
		}while(false);
	}
	
	@Override
	public void toAiAttack(long time){
		// 전투상태로 전환되면 도망모드로 다시 변경.
		setAiStatus(5);
	}
	
	@Override
	public void toAiEscape(long time){
		super.toAiEscape(time);
		
		// 전투목록에서 가장 근접한 사용자 찾기.
		object o = null;
		for(object oo : getAttackList()){
			if(o == null)
				o = oo;
			else if(Util.getDistance(this, oo)<Util.getDistance(this, o))
				o = oo;
		}
		
		// 못찾앗을경우 무시. 가끔생길수 잇는 현상이기에..
		if(o==null){
			setAiStatus(0);
			return;
		}
		
		// 반대방향 이동처리.
		heading = Util.oppositionHeading(this, o);
		int temp_heading = heading;
		int count = 100;
		do{
			if (count-- < 0) {
				break;
			}
			// 이동 좌표 추출.
			int x = Util.getXY(heading, true)+this.x;
			int y = Util.getXY(heading, false)+this.y;
			// 해당 좌표 이동가능한지 체크.
			boolean tail = World.isThroughObject(this.x, this.y, this.map, heading);
			// 해당좌표에 객체 있는지 확인.
			boolean obj = false;
			temp_list.clear();
			findInsideList(x, y, temp_list);
			for(object oo : temp_list){
				if(oo instanceof Character){
					obj = true;
					break;
				}
			}
			if(tail && !obj){
				// 타일이 이동가능하고 객체가 방해안하면 이동처리.
				super.toMoving(x, y, heading);
				break;
			}else{
				setHeading( heading + 1);
				if(temp_heading == heading)
					break;
			}
		}while(true);
	}

	
	@Override
	protected void toAiCorpse(long time){
		super.toAiCorpse(time);
		
		if(ai_time_temp_1 == 0)
			ai_time_temp_1 = time;
		
		// 시체 유지
		if(ai_time_temp_1+Lineage.ai_corpse_time > time)
			return;
		
		ai_time_temp_1 = 0;
		// 버프제거
		toReset(true);
		// 시체 제거
		World.remove(this);
		clearList(true);
		// 상태 변환.
		setAiStatus(4);
	}
	
	@Override
	protected void toAiSpawn(long time){
		super.toAiSpawn(time);

		// 리스폰값이 정의되어 있지않다면 재스폰 할 필요 없음.
		if(reSpawnTime == 0){
			toAiThreadDelete();
			return;
		}
		
		if(ai_time_temp_1 == 0)
			ai_time_temp_1 = time;
		// 스폰 대기.
		if(ai_time_temp_1+reSpawnTime > time){
			
		}else{
			ai_time_temp_1 = 0;
			// 상태 변환
			setDead(false);
			setNowHp(getMaxHp());
			setNowMp(getMaxMp());
			// 스폰
			toTeleport(homeX, homeY, homeMap, false);
			// 상태 변환.
			setAiStatus(0);
		}
	}
	
}

