package lineage.persnal_shop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lineage.bean.database.Item;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.database.WarehouseDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectLock;
import lineage.network.packet.server.S_SoundEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.CharacterController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CommandController;
import lineage.world.controller.LetterController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.DogCollar;
import lineage.world.object.item.sp.CharacterSaveMarbles;

public class PersnalShopInstance extends object {
	private List<PersnalShopItem> buyList; // 개인상점 판매목록.
	private List<PersnalShopItem> sellList; // 개인상점 구매목록.
	private int show_comment; //
	private int show_comment_index;
	private int show_comment_sell_index;
	private long pcObjectId;
	private String pcName;
	private int pcAccountUid;
	private List<String> list_html;
	private long purchasePrice;

	public PersnalShopInstance(long pc_objId, String pc_name, int accountUid) {

		try {
			buyList = new ArrayList<PersnalShopItem>();
			list_html = new ArrayList<String>();
			sellList = new ArrayList<PersnalShopItem>();

			setGfx(11322);
			setPcName(pc_name);
			setPcObjectId(pc_objId);
			setPcAccountUid(accountUid);
			setObjectId(ServerDatabase.nextEtcObjId());
			setName(pc_name + "");
			setClanId(0);
			setClanName("");
			setTitle("");
			setLawful(66536);
			setGfxMode(0);
			setHeading(5);
			World.appendPersnalShop(this);
			CharacterController.toWorldJoin(this);
			if (!PersnalShopDatabase.list.contains(this))
				PersnalShopDatabase.list.add(this);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() {
		if (buyList != null) {
			/**
			 * 남아있는 아이템을 돌려준다..
			 */
			returnItem(buyList);
			buyList.clear();
		}

//		deleteDBAll(pcAccountUid);

		if (purchasePrice != 0) {
			PcInstance owner = World.findPc(pcObjectId);
			if (owner != null) {
				ItemInstance aden = owner.getInventory().find("아데나", true);
				if (aden == null) {
					aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
					aden.setObjectId(ServerDatabase.nextItemObjId());
					aden.setCount(0);
					owner.getInventory().append(aden, true);
				}
				//
				// 이용자에게 아데나 지급
				owner.getInventory().count(aden, aden.getCount() + purchasePrice, true);
				ChattingController.toChatting(this, "\\fR개인상점이 종료되어 매입대금이 회수되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			} else {
				/*
				 * LetterController.toLetter("개인상점", getPcName(), "판매완료",
				 * String.format("[개인상점종료] 개인상점종료로 %s (%,d) 매입대금이 창고로 지급되었습니다.","아데나",
				 * purchasePrice), purchasePrice, true);
				 */
				ItemInstance temp_aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
				long invid = temp_aden.getItem().isPiles()
						? WarehouseDatabase.isPiles(temp_aden.getItem().isPiles(), this.getPcAccountUid(),
								temp_aden.getItem().getName(), temp_aden.getBress(), 0)
						: 0;
				if (invid > 0) {
					// update
					WarehouseDatabase.update(temp_aden.getItem().getName(), temp_aden.getBress(),
							this.getPcAccountUid(), purchasePrice, 0);
				} else {

					WarehouseDatabase.insert(temp_aden, ServerDatabase.nextItemObjId(), purchasePrice,
							this.getPcAccountUid(), 0);

				}
			}
		}

		buyList = null;
		list_html = null;
		sellList = null;
		
		clearList(true);
		World.remove(this);
		World.removePersnalShop(this);
		CharacterController.toWorldOut(this);

		super.close();
	}

	public List<PersnalShopItem> getItemList() {
		return buyList;
	}

	public void setPcObjectId(long id) {
		pcObjectId = id;
	}

	public long getPcObjectId() {
		return pcObjectId;
	}

	public long getSellItemPrice(ItemInstance item) {
		for (PersnalShopItem sellItem : sellList) {
			if (item.getItem().getItemId() == sellItem.getItem_id() && item.getBress() == sellItem.getBless()
					&& item.getEnLevel() == sellItem.getEnLevel() && item.getEnEarth() == sellItem.getEnEarth()
					&& item.getEnFire() == sellItem.getEnFire() && item.getEnWater() == sellItem.getEnWater()
					&& item.getEnWind() == sellItem.getEnWind()) {
				return sellItem.getPrice();
			}
		}
		return 0;
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		try {
			list_html.add(getPcName());
			list_html.add(pc.getName());
			if (pc.getObjectId() == this.getPcObjectId()) {
				list_html.add("[현재 매입 대금 : "
						+ (getPurchasePrice() > 0 ? Util.getPriceFormat((int) getPurchasePrice()) : 0) + "]");
				list_html.add("매  입  물  품  추  가");
				list_html.add("매  입  물  품  삭  제");
				list_html.add("매  입  가  격  수  정");
				list_html.add("매  입  대  금  입  금");
				list_html.add("매  입  대  금  출  금");
				list_html.add("판  매  물  품  추  가");
				list_html.add("판  매  가  격  수  정");
				list_html.add("무  인  상  점  종  료");
			} else {
				list_html.add(" ");
				list_html.add(" ");
				list_html.add(" ");
				list_html.add(" ");
				list_html.add(" ");
				list_html.add(" ");
				list_html.add(" ");
				list_html.add(" ");
				list_html.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), pc, "persnal_shop", this.getName(),
					list_html));
			list_html.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public String[] command = { "buy", "sell", "sell_setting" };

	static public boolean isCommand(String action) {
		for (String cmd : command) {
			if (action.contains(cmd)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		if (action.equalsIgnoreCase("buy")) {
			pc.toSender(S_PersnalShopBuyList.clone(BasePacketPooling.getPool(S_PersnalShopBuyList.class), this));
		} else if (action.equalsIgnoreCase("sell")) {
			if (sellList.size() <= 0) {
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "nosell"));
				return;
			}
			List<PersnalShopItem> itemList = sellList;
			List<ItemInstance> addItemList = new ArrayList<ItemInstance>();

			for (PersnalShopItem sellItem : itemList) {
				for (ItemInstance item : pc.getInventory().getList()) {
					if (item.getItem().getItemId() == sellItem.getItem_id() && item.getBress() == sellItem.getBless()
							&& item.getEnLevel() == sellItem.getEnLevel() && item.getEnEarth() == sellItem.getEnEarth()
							&& item.getEnFire() == sellItem.getEnFire() && item.getEnWater() == sellItem.getEnWater()
							&& item.getEnWind() == sellItem.getEnWind()) {
						addItemList.add(item);
					}
				}
			}

			if (addItemList.size() <= 0) {
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "nosell"));
			} else {
				pc.toSender(S_PersnalShopSellList.clone(BasePacketPooling.getPool(S_PersnalShopSellList.class), this,
						addItemList));
			}
		} else if (action.equalsIgnoreCase("sell_setting1")) { // 매입아이템추가
			if (pc.getObjectId() != this.getPcObjectId()) {
				return;
			}
			pc.setPersnalShopSellInsert(true);
			pc.toSender(S_CharAutoShopSell.clone(BasePacketPooling.getPool(S_CharAutoShopSell.class), pc,
			pc.getInventory().getList(), this));
			PcInstance owner = World.findPc(this.getPcObjectId());
			ChattingController.toChatting(owner, "\\fW매입추가 : 구매 개수를 신중히 입력하시기 바랍니다.", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(owner, "\\fR매입개수 : 매입 물품은 50000개 까지 설정 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);

		} else if (action.equalsIgnoreCase("sell_setting2")) { // 매입아이템삭제
			if (pc.getObjectId() != this.getPcObjectId()) {
				return;
			}
			pc.setPersnalShopSellEdit(true);
			pc.toSender(S_PersnalShopSellDelete.clone(BasePacketPooling.getPool(S_PersnalShopSellDelete.class), pc,
					getSellList()));
			ChattingController.toChatting(pc, "\\fW매입삭제 : 목록에서 제외 하시려는 물품을 선택하세요", Lineage.CHATTING_MODE_MESSAGE);

		} else if (action.equalsIgnoreCase("sell_setting3")) { // 매입아이템가격변경
			if (pc.getObjectId() != this.getPcObjectId()) {
				return;
			}
			pc.setPersnalShopSellPriceSetting(true);
			pc.toSender(S_PersnalShopPriceSetting.clone(BasePacketPooling.getPool(S_PersnalShopPriceSetting.class), pc,
					getSellList()));
			
			ChattingController.toChatting(pc, "\\fY매입수정 : 구매 금액을 신중히 입력하시기 바랍니다.", Lineage.CHATTING_MODE_MESSAGE);

		} else if (action.equalsIgnoreCase("sell_setting4")) { // 매입대금입금
			if (pc.getObjectId() != this.getPcObjectId()) {
				return;
			}
			pc.setPersnalShopPurchasePriceIn(true);
			pc.toSender(S_PersnalShopPurchasePrice.clone(BasePacketPooling.getPool(S_PersnalShopPurchasePrice.class),
					pc, pc.getInventory().getList()));
			ChattingController.toChatting(pc, "\\fY대금입금 : 매입 대금은 입금한도 금액이 10억 입니다.", Lineage.CHATTING_MODE_MESSAGE);

		} else if (action.equalsIgnoreCase("sell_setting5")) { // 매입대금출금
			if (pc.getObjectId() != this.getPcObjectId()) {
				return;
			}
			pc.toSender(S_PersnalShopPurchasePriceOut
					.clone(BasePacketPooling.getPool(S_PersnalShopPurchasePriceOut.class), this, getPurchasePrice()));

		} else if (action.equalsIgnoreCase("sell_setting6")) { // 판매물품추가
			if (pc.getObjectId() != this.getPcObjectId()) {
				return;
			}
			pc.setPersnalShopEdit(true);
			pc.toSender(S_CharAutoShop.clone(BasePacketPooling.getPool(S_CharAutoShop.class), (PcInstance) pc,
					pc.getInventory().getList()));
			ChattingController.toChatting(pc, "\\fW판매등록 : 아이템을 선택 후 OK 를 클릭 해주세요.", Lineage.CHATTING_MODE_MESSAGE);
		} else if (action.equalsIgnoreCase("sell_setting7")) { // 판매가격수정
			if (pc.getObjectId() != this.getPcObjectId()) {
				return;
			}

			pc.setPersnalShopPriceReset(true);
			pc.toSender(S_PersnalShopPriceSetting.clone(BasePacketPooling.getPool(S_PersnalShopPriceSetting.class),
					(PcInstance) pc, getItemList()));
			ChattingController.toChatting(pc, "\\fY판매수정 : 판매 금액을 신중히 입력하시기 바랍니다.", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fS현재 화면에서 각 물품 가격을 바로 설정 해주세요!!", Lineage.CHATTING_MODE_MESSAGE);

		} else if (action.equalsIgnoreCase("sell_setting8")) { // 상점 종료
			if (pc.getObjectId() != this.getPcObjectId()) {
				return;
			}
			int number = 0;
			for (ItemInstance item : pc.getInventory().getList()) {
				number += 1;
			}
			if (number >= 150) {
				ChattingController.toChatting(pc, "\\fY150개 이하로 인벤토리 공간을 확보해 주세요.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
//			deleteDBNpc();
			close();
			pc.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 23));
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), pc, "hpgosiv"));
			ChattingController.toChatting(pc, "\\fY상점종료 : 매입 대금과 모든 물품이 회수 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	@Override
	public synchronized void toDwarfAndShop(PcInstance pc, ClientBasePacket cbp) {
		if (pc.getGm() == 0) {
			if (getPcObjectId() == pc.getObjectId()) {
				ChattingController.toChatting(pc, "\\fY상점알림: 본인의 상점과는 거래할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				ChattingController.toChatting(pc, "\\fT0원으로 잘못 등록하셨다면 .상점 종료 후 재등록", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
		}

		switch (cbp.readC()) {
		case 0: // 상점 구입
			toBuy(pc, cbp);
			break;
		case 1:
			toSell(pc, cbp);
			break;
		}
	}

	/*
	 * @Override public void toTimer(long time) { try { if (!isWorldDelete() &&
	 * getItemList() != null && getItemList().size() > 0) { int show_count =
	 * ++show_comment;
	 * 
	 * if (getSellList() != null && getSellList().size() > 0) { if (show_count % 30
	 * == 0) { if (getSellList().size() <= show_comment_sell_index)
	 * show_comment_sell_index = 0;
	 * 
	 * ChattingController.toChatting(this, "[구매중] " +
	 * getSellList().get(show_comment_sell_index++).getName(),
	 * Lineage.CHATTING_MODE_NORMAL); } } //무인상점 판매중. 무인상점 멘트
	 * 
	 * if (show_count % 30 == 0) { show_comment = 0; if (getItemList().size() <=
	 * show_comment_index) show_comment_index = 0;
	 * 
	 * ChattingController.toChatting(this, "[판매중] " +
	 * getItemList().get(show_comment_index++).getName(),Lineage.
	 * CHATTING_MODE_NORMAL); } }
	 * 
	 * if (!isWorldDelete() && buyList.size() == 0 && sellList.size() == 0) {
	 * deleteDBNpc(); close(); }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } // }
	 */

	
	/**
	 * 상점 판매
	 */
	protected synchronized void toSell(PcInstance pc, ClientBasePacket cbp) {
		int count = cbp.readH();
		PersnalShopItem s = null;
		if (count > 0) {
			try {
				for (int i = 0; i < count; ++i) {
					long inv_id = cbp.readD();// 끄,ㅌ
					int item_count = (int) cbp.readD();

					if (item_count < 0 || item_count > 2000000000)
						return;
					ItemInstance temp = pc.getInventory().value(inv_id);

					if (temp == null) {
						System.out.println("[서버알림] 인벤에 없는 아이템을 판매 하려 시도합니다. [" + pc.getName() + "]");
						continue;
					}

					if (temp != null && !temp.isEquipped() && item_count > 0 && temp.getCount() >= item_count) {
						s = findSellItem(temp.getItem().getItemId(), "매입");
						// 판매될수 있는 아이템만 처리.
						if (s != null) {
							// 매입가능갯수 체크
							if (s.getCount() < item_count) {
								ChattingController.toChatting(pc,
										"\\fY매입 가능 수량을 초과하였습니다. [최대매입가능수량 : " + s.getCount() + "]",
										Lineage.CHATTING_MODE_MESSAGE);
								continue;
							}
							if (item_count < 0) {
								continue;
							}
							// 가격 체크
							long target_price = s.getPrice();

							// 매입금액이 남아있는지 체크
							if ((target_price * item_count) > this.getPurchasePrice()) {
								ChattingController.toChatting(pc, "\\fT해당 상점의 매입대금이 소진되어 판매 불가합니다.",
										Lineage.CHATTING_MODE_MESSAGE);
								continue;
							}

							// 아덴 지급
							if (target_price > 0) {
								ItemInstance aden = pc.getInventory().find("아데나", true);
								if (aden == null) {
									aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
									aden.setObjectId(ServerDatabase.nextItemObjId());
									aden.setCount(0);
									pc.getInventory().append(aden, true);
								}
								//
								// 이용자에게 아데나 지급
								pc.getInventory().count(aden, aden.getCount() + (target_price * item_count), true);
								ChattingController.toChatting(pc, "\\fY" + this.getName() + " 에게 "
										+ temp.getItem().getName() + "(" + item_count + ") 를 판매하였습니다.",
										Lineage.CHATTING_MODE_MESSAGE);
								ChattingController.toChatting(pc,
										String.format("\\fS%s (%,d) 지급 되었습니다.", "아데나", target_price * item_count),
										Lineage.CHATTING_MODE_MESSAGE);
							}

							// 판매되는 아이템 제거.
							getResultSellItem(s, item_count);

							boolean buyListCheck = getResultSellItemBuyCheck(s, item_count);

							PcInstance owner = World.findPc(this.getPcObjectId());

							if (owner != null) {
								if (buyListCheck) {
									ChattingController.toChatting(owner, "\\fT매입물품이 판매리스트에 존재하여 판매 개수에 추가됨",
											Lineage.CHATTING_MODE_MESSAGE);
									ChattingController.toChatting(owner,
											"\\fY추가물품: " + temp.getItem().getName() + "(" + item_count + ")].",
											Lineage.CHATTING_MODE_MESSAGE);

								} else {
									ChattingController.toChatting(owner, "\\fT매입물품이 구매되어 창고로 보관 됩니다.",
											Lineage.CHATTING_MODE_MESSAGE);
									ChattingController.toChatting(owner,
											"\\fY보관물품: " + temp.getItem().getName() + "(" + item_count + ")].",
											Lineage.CHATTING_MODE_MESSAGE);
								}
								ChattingController.toChatting(owner, "\\fY매입완료: 상점 매입 잔금 : "
										+ (getPurchasePrice() > 0 ? Util.getPriceFormat((int) getPurchasePrice()) : 0),
										Lineage.CHATTING_MODE_MESSAGE);
							} else {
								if (buyListCheck) {
									/*
									 * LetterController .toLetter("개인상점", getPcName(), "아이템구매",
									 * String.format("'%s' 아이템이 판매리스트에 포함되어있어 갯수를 포함합니다.", temp.getItem().getName()
									 * + "(" + item_count + ")"), 0, true);
									 */
								} else {
									/*
									 * LetterController .toLetter("개인상점", getPcName(), "아이템구매",
									 * String.format("'%s' 아이템이 구매되어 창고로 보관되었습니다.", temp.getItem().getName() + "(" +
									 * item_count + ")"), 0, true);
									 */
								}
							}

							// 등록하려는 아이템이 겹쳐지는 아이템이라면 디비에 겹칠 수 있는 것이
							// 존재하는지 확인.
							if (!buyListCheck) {
								long invid = temp.getItem().isPiles()
										? WarehouseDatabase.isPiles(temp.getItem().isPiles(), this.getPcAccountUid(),
												temp.getItem().getName(), temp.getBress(), 0)
										: 0;
								if (invid > 0) {
									// update
									WarehouseDatabase.update(temp.getItem().getName(), temp.getBress(),
											this.getPcAccountUid(), item_count, 0);
								} else {
									// insert
									WarehouseDatabase.insert(temp, ServerDatabase.nextItemObjId(), item_count,
											this.getPcAccountUid(), 0);
								}
							}
							pc.getInventory().count(temp, temp.getCount() - item_count, true);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 상점 구매
	 */
	protected synchronized void toBuy(PcInstance pc, ClientBasePacket cbp) {
		try {
			long count = cbp.readH();
			PersnalShopItem item;
			if (count > 0 && buyList.size() >= count) {
				for (int i = 0; i < count; i++) {
					long item_idx = cbp.readD();
					int item_count = (int) cbp.readD();
					if (item_count < 0 || item_count > 2000000000)
						return;

					item = findItem(item_idx, "판매");
					if (item == null) {
						continue;
					}

					if (!item.getShoptype().equalsIgnoreCase("판매")) {
						continue;
					}

					if (item.getCount() < item_count) {
						ChattingController.toChatting(pc, "\\fR수량안내 : 구매 가능한 잔여 개수는 " + item.getCount() + "개 입니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						continue;
					}

					if (item.getPrice() == 0) {
						ChattingController.toChatting(pc, "\\fR판매 가격이 0원인 아이템은 구매 할수 없습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						continue;
					}
					if (item_count < 0) {
						continue;
					}
					if (!pc.getInventory().isAden((item.getPrice() * item_count), true)) {
						// 수정.성환
						pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 936));
						continue;
					}

					Item tem = ItemDatabase.find_ItemId(item.getItem_id());
					if (tem == null)
						continue;
					ItemInstance create_item = ItemDatabase.newInstance(tem);

					if (create_item == null)
						continue;

					if (create_item instanceof CharacterSaveMarbles) {
						create_item.setObjectId(item.getItemobjid());
					} else {
						create_item.setObjectId(ServerDatabase.nextItemObjId());
					}
					create_item.setEnLevel(item.getEnLevel());
					create_item.setEnEarth(item.getEnEarth());
					create_item.setEnFire(item.getEnFire());
					create_item.setEnWater(item.getEnWater());
					create_item.setEnWind(item.getEnWind());
					create_item.setBress(item.getBless());
					create_item.setDefinite(item.isDefinite());
					create_item.setCount(item_count);

					ItemInstance temp = pc.getInventory().find(create_item.getItem().getName(), create_item.getBress(),
							true);
					if (tem.getItemId() == 1435315) {
						WarehouseDatabase.insert(create_item, create_item.getObjectId(), create_item.getCount(),
								pc.getClient().getAccountUid(), 3);
						LetterController.toLetter("개인상점", pc.getName(), "구매완료",
								String.format("'%s' 아이템을 구매하여 창고로 저장 되었습니다.", tem.getName()), 1, false);
					} else if (temp == null) {
						pc.getInventory().append(create_item, true);
					} else {
						// 겹치는 아이템이 존재할 경우.
						pc.getInventory().count(temp, create_item, temp.getCount() + item_count, true);
					}

					if (create_item instanceof CharacterSaveMarbles) {
						Connection con = null;
						try {
//							con = DatabaseConnection.getLineage();
							create_item.toWorldJoin(con, pc);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							DatabaseConnection.close(con);
						}
					}

					removeItem(item_idx, item_count);

					PcInstance owner = World.findPc(this.getPcObjectId());

					// toLetter(PcInstance pc, int new_uid, String from, String to, String subject,
					// String memo, long date, long aden)

					if (owner == null) {
						/*
						 * LetterController.toLetter( "개인상점", getPcName(), "판매완료",
						 * String.format("%s(%d) 아이템이 판매완료되어 \r\n%s (%,d)를 \r\n창고로 지급.",
						 * item.getName(),item_count, "아데나",item.getPrice() * item_count),
						 * (item.getPrice() * item_count), true);
						 */
						ItemInstance temp_aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
						long invid = temp_aden.getItem().isPiles()
								? WarehouseDatabase.isPiles(temp_aden.getItem().isPiles(), this.getPcAccountUid(),
										temp_aden.getItem().getName(), temp_aden.getBress(), 0)
								: 0;
						if (invid > 0) {
							// update
							WarehouseDatabase.update(temp_aden.getItem().getName(), temp_aden.getBress(),
									this.getPcAccountUid(), (item.getPrice() * item_count), 0);
						} else {
							WarehouseDatabase.insert(temp_aden, ServerDatabase.nextItemObjId(),
									(item.getPrice() * item_count), this.getPcAccountUid(), 0);

						}

					} else if (owner != null && !owner.isWorldDelete()) {
						/*
						 * Item adena = ItemDatabase.find("아데나"); if (adena != null) { ItemInstance aden
						 * = owner.getInventory().find("아데나", true); if (aden == null) { aden =
						 * ItemDatabase.newInstance(ItemDatabase.find("아데나"));
						 * aden.setObjectId(ServerDatabase.nextItemObjId()); aden.setCount(0);
						 * owner.getInventory().append(aden, true); } //
						 * 
						 * owner.getInventory().count(aden, (aden.getCount() + (item.getPrice() *
						 * item_count)), true);
						 */
						ChattingController.toChatting(owner,
								String.format("\\fS판매완료: %s(%d)개 \r\n\\fS판매대금: %s (%,d)를 창고로 지급.", item.getName(),
										item_count, "아데나", item.getPrice() * item_count),
								Lineage.CHATTING_MODE_MESSAGE);
						owner.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 5374));
						ItemInstance temp_aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
						long invid = temp_aden.getItem().isPiles()
								? WarehouseDatabase.isPiles(temp_aden.getItem().isPiles(), this.getPcAccountUid(),
										temp_aden.getItem().getName(), temp_aden.getBress(), 0)
								: 0;
						if (invid > 0) {
							// update
							WarehouseDatabase.update(temp_aden.getItem().getName(), temp_aden.getBress(),
									this.getPcAccountUid(), (item.getPrice() * item_count), 0);
						} else {
							WarehouseDatabase.insert(temp_aden, ServerDatabase.nextItemObjId(),
									(item.getPrice() * item_count), this.getPcAccountUid(), 0);

						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private PersnalShopItem findItem(long objid, String type) {
		try {
			if (type.equalsIgnoreCase("판매")) {
				for (PersnalShopItem item : buyList) {
					if (item.getItemobjid() == objid && item.getShoptype().equalsIgnoreCase(type)) {
						return item;
					}
				}
			} else if (type.equalsIgnoreCase("매입")) {
				for (PersnalShopItem item : sellList) {
					if (item.getItemobjid() == objid && item.getShoptype().equalsIgnoreCase(type)) {
						return item;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private PersnalShopItem findSellItem(int itemid, String type) {
		try {
			if (type.equalsIgnoreCase("판매")) {
				for (PersnalShopItem item : buyList) {
					if (item.getItem_id() == itemid && item.getShoptype().equalsIgnoreCase(type)) {
						return item;
					}
				}
			} else if (type.equalsIgnoreCase("매입")) {
				for (PersnalShopItem item : sellList) {
					if (item.getItem_id() == itemid && item.getShoptype().equalsIgnoreCase(type)) {
						return item;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void removeItem(long objid, long count) {
		synchronized (buyList) {
			PersnalShopItem item;
			try {
				for (Iterator<PersnalShopItem> iter = buyList.iterator(); iter.hasNext();) {
					item = iter.next();
					if (item == null)
						continue;
					if (item.getItemobjid() == objid) {
						if (item.getCount() == count) {
//							deleteDB(item, "판매");
							iter.remove();
						} else if (item.getCount() > count) {
							item.setCount(item.getCount() - count);
//							insertDB(item);
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setShopPriceSetting(PcInstance pc, ClientBasePacket cbp) {
		try {
			cbp.readC(); // type
			int count = cbp.readH();
			PersnalShopItem item;
			List<PersnalShopItem> check_list = new ArrayList<PersnalShopItem>();
			if (count > 0) {
				long item_id = 0;
				int item_count = 0;

				for (int i = 0; i < count; ++i) {
					item_id = cbp.readD();
					item_count = (int) cbp.readD();

					if (item_count < 0 || item_count > 2000000000)
						return;
					item = findItem(item_id, "판매");
					if (item == null) {
						continue;
					}
					if (item_count < 0) {
						continue;
					}
					item.setPrice(item_count);
//					insertDB(item);
				}
			}

			for (int i = 0; i < buyList.size(); ++i) {
				item = buyList.get(i);
				if (item.getPrice() == 0) {
					check_list.add(item);
				}
			}

			if (check_list.size() > 0) {
				ChattingController.toChatting(pc, "\\fR등록알림 : 금액 설정이 되지 않은 아이템이 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
				pc.toSender(S_PersnalShopPriceSetting.clone(BasePacketPooling.getPool(S_PersnalShopPriceSetting.class),
						(PcInstance) pc, check_list));
				return;
			}
			if (!CommandController.ismarketLocation(pc)) {
				ChattingController.toChatting(pc, "개인상점 배치는 시장에서만 사용가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				close();
				return;
			}

			pc.setPersnalShopPriceSetting(false);
			// 상점 캐릭 세우는 시점
			toTeleport(pc.getX(), pc.getY(), pc.getMap(), false);
//			insertDBNpc(pc.getX(), pc.getY(), pc.getMap());

			pc.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 23));
			ChattingController.toChatting(pc, "무인상점 작동이 시작 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setShopAddPriceSetting(PcInstance pc, ClientBasePacket cbp) {
		try {
			cbp.readC(); // type
			int count = cbp.readH(); // 이건 고를 수 있는 갯수인데 인트로하는게 낫겠죵??? 네
			PersnalShopItem item;
			List<PersnalShopItem> check_list = new ArrayList<PersnalShopItem>();
			if (count > 0) {
				long item_id = 0;
				int item_count = 0;

				for (int i = 0; i < count; ++i) {
					item_id = cbp.readD();
					item_count = (int) cbp.readD();
					if (item_count < 0 || item_count > 2000000000)
						return;
					item = findItem(item_id, "판매");
					if (item == null) {
						continue;
					}

					item.setPrice(item_count);
//					insertDB(item);
				}
			}

			for (int i = 0; i < buyList.size(); ++i) {
				item = buyList.get(i);
				if (item.getPrice() == 0) {
					check_list.add(item);
				}
			}

			if (check_list.size() > 0) {
				ChattingController.toChatting(pc, "\\fR등록알림 : 금액 설정이 되지 않은 아이템이 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
				pc.toSender(S_PersnalShopPriceSetting.clone(BasePacketPooling.getPool(S_PersnalShopPriceSetting.class),
						(PcInstance) pc, check_list));
				return;
			}

			pc.setPersnalShopAddPriceSetting(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setShopResetPriceSetting(PcInstance pc, ClientBasePacket cbp) {
		try {
			cbp.readC(); // type
			int count = cbp.readH();
			PersnalShopItem item;
			List<PersnalShopItem> check_list = new ArrayList<PersnalShopItem>();
			if (count > 0) {
				long item_id = 0;
				int item_count = 0;

				for (int i = 0; i < count; ++i) {
					item_id = cbp.readD();// e됫죠?넹 그럼 더이상 할때가 없는데
					item_count = (int) cbp.readD();
					if (item_count < 0 || item_count > 2000000000)
						return;
					item = findItem(item_id, "판매");
					if (item == null) {
						continue;
					}
					if (item_count < 0) {
						continue;
					}
					item.setPrice(item_count);
//					insertDB(item);
				}
			}

			for (int i = 0; i < buyList.size(); ++i) {
				item = buyList.get(i);
				if (item.getPrice() == 0) {
					check_list.add(item);
				}
			}

			if (check_list.size() > 0) {
				ChattingController.toChatting(pc, "\\fR등록알림 : 금액 설정이 되지 않은 아이템이 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
				pc.toSender(S_PersnalShopPriceSetting.clone(BasePacketPooling.getPool(S_PersnalShopPriceSetting.class),
						(PcInstance) pc, check_list));
				return;
			}

			pc.setPersnalShopPriceReset(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addShopItem(PcInstance pc, ClientBasePacket cbp) {

		try {
			cbp.readC(); // type
			int count = cbp.readH();
			ItemInstance item;

			if (count == 0) {
				pc.setPersnalShopInsert(false);
				ChattingController.toChatting(pc, "\\fR등록하고자 하는 물품이 없어 상점배치를 취소합니다.", Lineage.CHATTING_MODE_MESSAGE);
				pc.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 25));
				close();
				return;
			}

			if (!CommandController.ismarketLocation(pc)) {
				ChattingController.toChatting(pc, "개인상점 배치는 시장에서만 사용가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				close();
				return;
			}

			if (count > 0) {
				long item_id = 0;
				int item_count = 0;

				for (int i = 0; i < count; ++i) {
					item_id = cbp.readD();
					item_count = (int) cbp.readD();
					if (item_count < 0 || item_count > 2000000000)
						return;// 왜이렇게 만짘ㅋㅋㅋㅋㅋㅋㅋ그러게요????ㅋㅋㅋㅋㅋ
					item = pc.getInventory().findByObjId(item_id);

					if (item == null) {
						System.out.println("존재하지 않는 아이템을 판매하려 시도합니다. [" + pc.getName() + "]");
						ChattingController.toChatting(pc, "경고 : 인벤토리에 없는 물품 입니다.", Lineage.CHATTING_MODE_MESSAGE);
						close();
						continue;
					}

					if (item_count > item.getCount()) {
						System.out.println("소유한 아이템 갯수보다 많은 양을 판매하려 시도합니다. [" + pc.getName() + "]");
						ChattingController.toChatting(pc, "경고 : 인벤토리에 아이템 보유 개수가 부족합니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						close();
						continue;
					}
					if (item_count < 0) {
						close();
						continue;
					}

					int type = 0;
					if (item instanceof ItemWeaponInstance)
						type = 1;
					else if (item instanceof ItemArmorInstance)
						type = 2;
					else
						type = 3;

					PersnalShopItem psItem = new PersnalShopItem();
					psItem.setItem_id(item.getItem().getItemId());
					psItem.setItemobjid(item_id);
					psItem.setBless(item.getBress());
					psItem.setEnLevel(item.getEnLevel());
					psItem.setCount(item_count);
					psItem.setEnEarth(item.getEnEarth());
					psItem.setEnWater(item.getEnWater());
					psItem.setEnFire(item.getEnFire());
					psItem.setEnWind(item.getEnWind());
					psItem.setType(type);
					psItem.setInvGfx(item.getItem().getInvGfx());
					psItem.setName(item.getItem().getName());
					psItem.setMainItem(item);
					psItem.setDefinite(item.isDefinite());
					psItem.setCharName(pcName);
					psItem.setShoptype("판매");

					if (item instanceof DogCollar) {
						psItem.setPetObjId(item.getPetObjectId());
					}

					buyList.add(psItem);
//					insertDB(psItem);
					/**
					 * 리스트 추가함과 동시에 인벤에서 삭제 처리
					 */
					pc.getInventory().count(item, item.getCount() - item_count, true);
				}

				pc.setPersnalShopInsert(false);
				pc.setPersnalShopPriceSetting(true);

				pc.toSender(S_PersnalShopPriceSetting.clone(BasePacketPooling.getPool(S_PersnalShopPriceSetting.class),
						(PcInstance) pc, buyList));
				ChattingController.toChatting(pc, "\\fY판매금액 : 판매 금액을 신중히 입력하시기 바랍니다.", Lineage.CHATTING_MODE_MESSAGE);
				/*
				 * ChattingController.toChatting(pc, "\\fT금액 지정을 하지않고 창을 닫으신 경우 .상점 종료",
				 * Lineage.CHATTING_MODE_MESSAGE); ChattingController.toChatting(pc,
				 * "\\fT하시고 새롭게 상점을 개설해 주시기 바랍니다.", Lineage.CHATTING_MODE_MESSAGE);
				 */

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addEditShopItem(PcInstance pc, ClientBasePacket cbp) {
		try {
			cbp.readC(); // type
			int count = cbp.readH();
			ItemInstance item;

			if (count == 0) {
				pc.setPersnalShopEdit(false);
				System.out.println("등록하고자 하는 물품이 없어 상점추가를 취소합니다.");
				return;
			}

			List<PersnalShopItem> add_list = new ArrayList<PersnalShopItem>();
			if (count > 0) {
				long item_id = 0;
				int item_count = 0;

				for (int i = 0; i < count; ++i) {
					item_id = cbp.readD();
					item_count = (int) cbp.readD();
					if (item_count < 0 || item_count > 2000000000)
						return;
					item = pc.getInventory().findByObjId(item_id);

					if (item == null) {
						System.out.println("존재하지 않는 아이템을 판매하려 시도합니다. [" + pc.getName() + "]");
						continue;
					}

					if (item_count > item.getCount()) {
						System.out.println("소유한 아이템 갯수보다 많은 양을 판매하려 시도합니다. [" + pc.getName() + "]");
						continue;
					}
					if (item_count < 0) {
						continue;
					}

					int type = 0;
					if (item instanceof ItemWeaponInstance)
						type = 1;
					else if (item instanceof ItemArmorInstance)
						type = 2;
					else
						type = 3;

					PersnalShopItem psItem = findItem(item_id, "판매");

					if (psItem == null) {
						psItem = new PersnalShopItem();
						psItem.setItem_id(item.getItem().getItemId());
						psItem.setItemobjid(item_id);
						psItem.setBless(item.getBress());
						psItem.setEnLevel(item.getEnLevel());
						psItem.setCount(item_count);
						psItem.setEnEarth(item.getEnEarth());
						psItem.setEnWater(item.getEnWater());
						psItem.setEnFire(item.getEnFire());
						psItem.setEnWind(item.getEnWind());
						psItem.setType(type);
						psItem.setInvGfx(item.getItem().getInvGfx());
						psItem.setName(item.getItem().getName());
						psItem.setMainItem(item);
						psItem.setDefinite(item.isDefinite());
						psItem.setCharName(pcName);
						psItem.setShoptype("판매");
						if (item instanceof DogCollar) {
							psItem.setPetObjId(item.getPetObjectId());
						}

						buyList.add(psItem);
						add_list.add(psItem);
					} else {
						psItem.setCount(psItem.getCount() + item_count);
						add_list.add(psItem);
					}

//					insertDB(psItem);
					/**
					 * 리스트 추가함과 동시에 인벤에서 삭제 처리
					 */
					pc.getInventory().count(item, item.getCount() - item_count, true);
				}

				pc.setPersnalShopEdit(false);
				pc.setPersnalShopAddPriceSetting(true);
				pc.toSender(S_PersnalShopPriceSetting.clone(BasePacketPooling.getPool(S_PersnalShopPriceSetting.class),
						(PcInstance) pc, add_list));
				ChattingController.toChatting(pc, "\\fY판매금액 : 판매 금액을 신중히 입력하시기 바랍니다.", Lineage.CHATTING_MODE_MESSAGE);
				ChattingController.toChatting(pc, "\\fT금액 지정을 하지않고 창을 닫으신 경우 .상점 종료", Lineage.CHATTING_MODE_MESSAGE);
				ChattingController.toChatting(pc, "\\fT하시고 새롭게 상점을 개설해 주시기 바랍니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void insertDBNpc(int x, int y, int map) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement(
					"INSERT INTO characters_shop_npc SET owner_obj_id=?,loc_x=?,loc_y=?,loc_map=?,purchase=?");
			st.setLong(1, getPcObjectId());
			st.setInt(2, x);
			st.setInt(3, y);
			st.setInt(4, map);
			st.setLong(5, purchasePrice);
			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	private void updateDBNpc() {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters_shop_npc SET purchase=? WHERE owner_obj_id=?");
			st.setLong(1, purchasePrice);
			st.setLong(2, getPcObjectId());
			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	private void deleteDBNpc() {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("DELETE FROM characters_shop_npc WHERE owner_obj_id=?");
			st.setLong(1, getPcObjectId());
			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	private void deleteDB(PersnalShopItem psItem, String type) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement(
					"DELETE FROM characters_shop WHERE char_obj_id=? AND shop_type=? AND item_obj_id=?");
			st.setLong(1, getPcObjectId());
			st.setString(2, type);
			st.setLong(3, psItem.getItemobjid());
			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	private void deleteDBAll(long objid) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("DELETE FROM characters_shop WHERE char_obj_id=?");
			st.setLong(1, getPcObjectId());
			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	private void insertDB(PersnalShopItem psItem) {

		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement(
					"INSERT INTO characters_shop (char_obj_id, shop_type, item_obj_id, item_id, item_name, item_type, price, count, bless, definite"
							+ ", enchant_level, enchant_earth, enchant_fire, enchant_water, enchant_wind, pet_obj_id, char_account_uid,char_name) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
							+ "ON DUPLICATE KEY UPDATE char_obj_id=?, shop_type=?, item_obj_id=?,item_id=?,item_name=?,item_type=?,price=?,count=?,bless=?,definite=?"
							+ ",enchant_level=?,enchant_earth=?,enchant_fire=?,enchant_water=?,enchant_wind=?,pet_obj_id=?,char_account_uid=?,char_name=?");

			int index = 0;

			st.setLong(++index, getPcObjectId());
			st.setString(++index, psItem.getShoptype());
			st.setLong(++index, psItem.getItemobjid());
			st.setInt(++index, psItem.getItem_id());
			st.setString(++index, psItem.getName());
			st.setInt(++index, psItem.getType());
			st.setLong(++index, psItem.getPrice());
			st.setLong(++index, psItem.getCount());
			st.setInt(++index, psItem.getBless());
			st.setInt(++index, psItem.isDefinite() ? 1 : 0);
			st.setInt(++index, psItem.getEnLevel());
			st.setInt(++index, psItem.getEnEarth());
			st.setInt(++index, psItem.getEnFire());
			st.setInt(++index, psItem.getEnWater());
			st.setInt(++index, psItem.getEnWind());
			st.setLong(++index, psItem.getPetObjId());
			st.setInt(++index, getPcAccountUid());
			st.setString(++index, getPcName());

			st.setLong(++index, getPcObjectId());
			st.setString(++index, psItem.getShoptype());
			st.setLong(++index, psItem.getItemobjid());
			st.setInt(++index, psItem.getItem_id());
			st.setString(++index, psItem.getName());
			st.setInt(++index, psItem.getType());
			st.setLong(++index, psItem.getPrice());
			st.setLong(++index, psItem.getCount());
			st.setInt(++index, psItem.getBless());
			st.setInt(++index, psItem.isDefinite() ? 1 : 0);
			st.setInt(++index, psItem.getEnLevel());
			st.setInt(++index, psItem.getEnEarth());
			st.setInt(++index, psItem.getEnFire());
			st.setInt(++index, psItem.getEnWater());
			st.setInt(++index, psItem.getEnWind());
			st.setLong(++index, psItem.getPetObjId());
			st.setInt(++index, getPcAccountUid());
			st.setString(++index, getPcName());

			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	private void returnItem(List<PersnalShopItem> psList) {
		try {
			for (PersnalShopItem psi : buyList) {

				Item item = ItemDatabase.find_ItemId(psi.getItem_id());
				if (item == null) {
					System.out.println("없는 아이템이 돌려주려 합니다. [개인상점 : " + psi.getItem_id() + "]");
					continue;
				}
				ItemInstance iteminstance = ItemDatabase.newInstance(item);

				if (iteminstance instanceof CharacterSaveMarbles) {
					iteminstance.setObjectId(psi.getItemobjid());

					Connection con = null;
					try {
						con = DatabaseConnection.getLineage();
						iteminstance.toWorldJoin(con, null);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						DatabaseConnection.close(con);
					}
				}

				iteminstance.setCount(psi.getCount());
				iteminstance.setBress(psi.getBless());
				iteminstance.setEnEarth(psi.getEnEarth());
				iteminstance.setEnFire(psi.getEnFire());
				iteminstance.setEnWater(psi.getEnWater());
				iteminstance.setEnWind(psi.getEnWind());
				iteminstance.setEnLevel(psi.getEnLevel());
				iteminstance.setPetObjectId(psi.getPetObjId());
				iteminstance.setDefinite(psi.isDefinite());

				PcInstance owner = World.findPc(this.getPcObjectId());
				if (owner == null) {
					if (iteminstance != null) {
						long inv_id = iteminstance.getItem().isPiles()
								? WarehouseDatabase.isPiles(iteminstance.getItem().isPiles(), getPcAccountUid(),
										iteminstance.getItem().getName(), iteminstance.getBress(), 3)
								: 0;
						if (inv_id > 0) {
							// update
							WarehouseDatabase.update(iteminstance.getItem().getName(), iteminstance.getBress(),
									getPcAccountUid(), iteminstance.getCount(), 3);
						} else {
							// insert
							WarehouseDatabase.insert(iteminstance, iteminstance.getObjectId(), iteminstance.getCount(),
									getPcAccountUid(), 3);

						}

						/*
						 * LetterController.toLetter("무인상점", getPcName(), "판매종료",
						 * String.format("'%s' 아이템이 판매종료되어 창고로 회수 되었습니다.", psi.getName()),
						 * iteminstance.getCount(), true);
						 */
					}
				} else {
					if (iteminstance != null) {
						//
						if (item.getItemId() == 1435315) {
							WarehouseDatabase.insert(iteminstance, iteminstance.getObjectId(), iteminstance.getCount(),
									getPcAccountUid(), 3);
							LetterController.toLetter("개인상점", getPcName(), "판매종료", String
									.format("'%s' 아이템이 판매종료되어 창고로 회수 되었습니다.\r\n(캐릭터 저장 구슬은 창고로 저장 됩니다)", psi.getName()),
									iteminstance.getCount(), false);
						} else {
							owner.getInventory().append(iteminstance, iteminstance.getCount());
						}

						Connection con = null;
						try {
							con = DatabaseConnection.getLineage();
							iteminstance.toWorldJoin(con, owner);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							DatabaseConnection.close(con);
						}

						// ChattingController.toChatting(owner, "(" + Util.getPriceFormat((int)
						// getPurchasePrice()) + " 아데나) \\fY매입 대금을 회수 하였습니다.",
						// Lineage.CHATTING_MODE_MESSAGE);
						ChattingController.toChatting(owner, String.format("\\fR%s 물품을 회수 하였습니다.", psi.getName()),
								Lineage.CHATTING_MODE_MESSAGE);

//						ChattingController.toChatting(owner, "헤헤헤헤"+getPurchasePrice()+"", Lineage.CHATTING_MODE_MESSAGE);
						// ChattingController.toChatting(owner, "(" + Util.getPriceFormat((int)
						// out_count) + " 아데나) \\fY대금 회수가 완료되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
					}
				}

//				deleteDB(psi, "판매");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getPcAccountUid() {
		return pcAccountUid;
	}

	public void setPcAccountUid(int pcAccountUid) {
		this.pcAccountUid = pcAccountUid;
	}

	public String getPcName() {
		return pcName;
	}

	public void setPcName(String pcName) {
		this.pcName = pcName;
	}

	public List<PersnalShopItem> getSellList() {
		return sellList;
	}

	public void setSellList(List<PersnalShopItem> sellList) {
		this.sellList = sellList;
	}

	public long getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(long purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public void addPurchasePrice(long purchasePrice) {
		this.purchasePrice += purchasePrice;
		updateDBNpc();
	}

	public void addShopSellItem(PcInstance pc, ClientBasePacket cbp) {
		try {
			cbp.readC(); // type
			int count = cbp.readH();
			ItemInstance item;

			if (count == 0) {
				pc.setPersnalShopSellInsert(false);
				return;
			}

			if (count > 0) {
				long item_id = 0;
				int item_count = 0;

				for (int i = 0; i < count; ++i) {
					item_id = cbp.readD();
					item_count = (int) cbp.readD();
					if (item_count < 0 || item_count > 2000000000)
						return;
					item = pc.getInventory().findByObjId(item_id);

					if (item == null) {
						System.out.println("존재하지 않는 아이템을 매입등록 시도합니다. [" + pc.getName() + "]");
						close();
						continue;
					}
					if (item_count < 0) {
						continue;
					}
					int type = 0;
					if (item instanceof ItemWeaponInstance)
						type = 1;
					else if (item instanceof ItemArmorInstance)
						type = 2;
					else
						type = 3;

					PersnalShopItem psItem = new PersnalShopItem();
					psItem.setItem_id(item.getItem().getItemId());
					psItem.setItemobjid(item_id);
					psItem.setBless(item.getBress());
					psItem.setEnLevel(item.getEnLevel());
					psItem.setCount(item_count);
					psItem.setEnEarth(item.getEnEarth());
					psItem.setEnWater(item.getEnWater());
					psItem.setEnFire(item.getEnFire());
					psItem.setEnWind(item.getEnWind());
					psItem.setType(type);
					psItem.setInvGfx(item.getItem().getInvGfx());
					psItem.setName(item.getItem().getName());
					psItem.setMainItem(item);
					psItem.setDefinite(item.isDefinite());
					psItem.setCharName(pcName);
					psItem.setShoptype("매입");

					sellList.add(psItem);
//					insertDB(psItem);
				}

				pc.setPersnalShopSellInsert(false);
				pc.setPersnalShopSellPriceSetting(true);
				pc.toSender(S_PersnalShopSellPriceSetting.clone(
						BasePacketPooling.getPool(S_PersnalShopSellPriceSetting.class), (PcInstance) pc, sellList));
				ChattingController.toChatting(pc, "\\fY매입가격 : 구매 금액을 신중히 입력하시기 바랍니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setShopSellPriceSetting(PcInstance pc, ClientBasePacket cbp) {
		try {
			cbp.readC(); // type
			int count = cbp.readH();
			PersnalShopItem item;
			List<PersnalShopItem> check_list = new ArrayList<PersnalShopItem>();
			if (count > 0) {
				long item_id = 0;
				int item_count = 0;

				for (int i = 0; i < count; ++i) {
					item_id = cbp.readD();
					item_count = (int) cbp.readD();
					if (item_count < 0 || item_count > 2000000000)
						return;
					item = findItem(item_id, "매입");
					if (item == null) {
						continue;
					}
					if (item_count < 0) {
						continue;
					}
					item.setPrice(item_count);
//					insertDB(item);
				}
			}

			for (int i = 0; i < sellList.size(); ++i) {
				item = sellList.get(i);
				if (item.getPrice() <= 0) {
					check_list.add(item);
				}
			}

			if (check_list.size() > 0) {
				ChattingController.toChatting(pc, "\\fR매입 금액이 설정되지 않았습니다.", Lineage.CHATTING_MODE_MESSAGE);
				pc.toSender(S_PersnalShopSellPriceSetting.clone(
						BasePacketPooling.getPool(S_PersnalShopSellPriceSetting.class), (PcInstance) pc, check_list));
				return;
			}

			pc.setPersnalShopSellPriceSetting(false);
			ChattingController.toChatting(pc, "\\fS설정완료 : 매입 아이템 설정이 완료 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addDeleteSellShopItem(PcInstance pc, ClientBasePacket cbp) {
		try {
			cbp.readC(); // type
			int count = cbp.readH();
			PersnalShopItem item;

			if (count == 0) {
				pc.setPersnalShopSellEdit(false);
				return;
			}

			if (count > 0) {
				long item_id = 0;
				int item_count = 0;

				for (int i = 0; i < count; ++i) {
					item_id = cbp.readD();
					item_count = (int) cbp.readD();
					if (item_count < 0 || item_count > 2000000000)
						return;
					item = findItem(item_id, "매입");

					if (item_count == 0)
						continue;

					deleteSellShopItem(item, "매입");
//					deleteDB(item, "매입");
				}

				pc.setPersnalShopSellEdit(false);
				ChattingController.toChatting(pc, "\\fS설정완료 : 매입 아이템 삭제가 완료 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteSellShopItem(PersnalShopItem item, String type) {
		PersnalShopItem shopItem = null;
		for (Iterator<PersnalShopItem> iter = sellList.iterator(); iter.hasNext();) {
			shopItem = iter.next();
			if (shopItem != null && shopItem.getItemobjid() == item.getItemobjid()
					&& shopItem.getShoptype().equalsIgnoreCase(type)) {
				iter.remove();
			}
		}
	}

	public void setShopResetSellPriceSetting(PcInstance pc, ClientBasePacket cbp) {
		try {
			cbp.readC(); // type
			int count = cbp.readH();
			PersnalShopItem item;
			List<PersnalShopItem> check_list = new ArrayList<PersnalShopItem>();
			if (count > 0) {
				long item_id = 0;
				int item_count = 0;

				for (int i = 0; i < count; ++i) {
					item_id = cbp.readD();
					item_count = (int) cbp.readD();
					if (item_count < 0 || item_count > 2000000000)
						return;
					item = findItem(item_id, "매입");
					if (item == null) {
						continue;
					}

					item.setPrice(item_count);
//					insertDB(item);
				}
			}

			for (int i = 0; i < sellList.size(); ++i) {
				item = sellList.get(i);
				if (item.getPrice() <= 0) {
					check_list.add(item);
				}
			}

			if (check_list.size() > 0) {
				ChattingController.toChatting(pc, "\\fR매입 금액이 설정되지 않았습니다.", Lineage.CHATTING_MODE_MESSAGE);
				pc.toSender(S_PersnalShopSellPriceSetting.clone(
						BasePacketPooling.getPool(S_PersnalShopSellPriceSetting.class), (PcInstance) pc, check_list));
				return;
			}

			pc.setPersnalShopSellPriceReSetting(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setShopPurchasePriceIn(PcInstance pc, ClientBasePacket cbp) {
		try {
			cbp.readC(); // type
			int count = cbp.readH();
			ItemInstance item;

			if (count > 0) {
				long item_id = 0;
				int item_count = 0;

				for (int i = 0; i < count; ++i) {
					item_id = cbp.readD();
					item_count = (int) cbp.readD();
					if (item_count < 0 || item_count > 2000000000)
						return;
					item = pc.getInventory().findAden();
					if (item == null) {
						continue;
					}

					if (item_id != item.getObjectId()) {
						ChattingController.toChatting(pc, "\\fY현재 소지중인 아데나에 오류가 있습니다. 운영자문의요망",
								Lineage.CHATTING_MODE_MESSAGE);
						System.out.println("[" + pc.getName() + "] 매입대금입금 버그 사용의심유저. * 모니터링 요함 *");
						continue;
					}

					if (item_count > item.getCount()) {
						ChattingController.toChatting(pc, "\\fY소지금액보다 많은 금액은 입금 할 수 없습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						continue;
					}
					if (item_count < 0) {
						continue;
					}

					if (item_count + getPurchasePrice() > 1000000000) { // 10억 이상 매입대금입금이 불가능하도록
						ChattingController.toChatting(pc, "\\fS잔여대금 : [현재 : "
								+ (getPurchasePrice() > 0 ? Util.getPriceFormat((int) getPurchasePrice()) : 0) + "]",
								Lineage.CHATTING_MODE_MESSAGE);
						continue;
					}

					if (pc.getInventory().isAden(item_count, true)) {
						addPurchasePrice(item_count);
						ChattingController.toChatting(pc,
								"아데나 (" + Util.getPriceFormat((int) item_count) + ") \\fY대금 충전이 완료되었습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
					} else {
						ChattingController.toChatting(pc, "\\fY소지금액이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
					}
				}
			}

			pc.setPersnalShopPurchasePriceIn(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getResultSellItem(PersnalShopItem s, long count) {
		if ((int) count < 0 || (int) count > 2000000000)
			return;

		if (s.getCount() == count) {
			sellList.remove((Object) s);
		} else {
			s.setCount(s.getCount() - count);
		}

		addPurchasePrice(-(s.getPrice() * count));
	}

	private boolean getResultSellItemBuyCheck(PersnalShopItem s, long count) {
		if ((int) count < 0 || (int) count > 2000000000)
			return false;

		int buyListSize = buyList.size();
		PersnalShopItem buyItem = null;
		for (int i = 0; i < buyListSize; i++) {
			buyItem = buyList.get(i);
			if (buyItem != null) {
				if (buyItem.getItem_id() == s.getItem_id() && buyItem.getBless() == s.getBless()
						&& buyItem.getEnLevel() == s.getEnLevel() && buyItem.getEnEarth() == s.getEnEarth()
						&& buyItem.getEnFire() == s.getEnFire() && buyItem.getEnWater() == s.getEnWater()
						&& buyItem.getEnWind() == s.getEnWind()) {
					buyItem.setCount(buyItem.getCount() + count);
//					insertDB(buyItem);
					return true;
				}
			} // 머가 이렇게 만일단 상점부분 클리어!
		}
		return false;
	}

	public void toSaveAll(Connection con) {// 다시한번만 ㅠㅠ디비초기화 
		
		// 저장 전 중복 문제 해소를 위해 선삭제

		// 1.모든 목록 삭제
		deleteDBAll(con);
		// 2.캐릭터 삭제
		deleteDBNpc(con);
		
		boolean listb = true;
		boolean lists = true;
		
		try {
			// 3.구매 목록 저장
			for (PersnalShopItem item : buyList)
				insertDB(con, item);
		} catch (Exception e) {
			// 매입 목록 없으면 건너뜀
			listb = false;
		}
		
		try {
			// 4.판매 목록 저장
			for (PersnalShopItem item : sellList)
				insertDB(con, item);
		} catch (Exception e) {
			// 판매 목록 없으면 건너뜀
			lists = false;
		}
		// 매입 혹은 판매 목록이 남아있으면 엔피씨 저장
		if (lists || listb)
			// 5.캐릭터 저장
			insertDBNpc(con, getX(), getY(), getMap());

	}

	public void deleteDBAll(Connection con) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("DELETE FROM characters_shop WHERE char_obj_id=?");
			st.setLong(1, getPcObjectId());
			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(st);
		}
	}

	public void deleteDBNpc(Connection con) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("DELETE FROM characters_shop_npc WHERE owner_obj_id=?");
			st.setLong(1, getPcObjectId());
			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(st);
		}
	}

	public void insertDB(Connection con, PersnalShopItem psItem) {

		PreparedStatement st = null;
		try {
			st = con.prepareStatement(
					"INSERT INTO characters_shop (char_obj_id, shop_type, item_obj_id, item_id, item_name, item_type, price, count, bless, definite"
							+ ", enchant_level, enchant_earth, enchant_fire, enchant_water, enchant_wind, pet_obj_id, char_account_uid,char_name) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
							+ "ON DUPLICATE KEY UPDATE char_obj_id=?, shop_type=?, item_obj_id=?,item_id=?,item_name=?,item_type=?,price=?,count=?,bless=?,definite=?"
							+ ",enchant_level=?,enchant_earth=?,enchant_fire=?,enchant_water=?,enchant_wind=?,pet_obj_id=?,char_account_uid=?,char_name=?");

			int index = 0;

			st.setLong(++index, getPcObjectId());
			st.setString(++index, psItem.getShoptype());
			st.setLong(++index, psItem.getItemobjid());
			st.setInt(++index, psItem.getItem_id());
			st.setString(++index, psItem.getName());
			st.setInt(++index, psItem.getType());
			st.setLong(++index, psItem.getPrice());
			st.setLong(++index, psItem.getCount());
			st.setInt(++index, psItem.getBless());
			st.setInt(++index, psItem.isDefinite() ? 1 : 0);
			st.setInt(++index, psItem.getEnLevel());
			st.setInt(++index, psItem.getEnEarth());
			st.setInt(++index, psItem.getEnFire());
			st.setInt(++index, psItem.getEnWater());
			st.setInt(++index, psItem.getEnWind());
			st.setLong(++index, psItem.getPetObjId());
			st.setInt(++index, getPcAccountUid());
			st.setString(++index, getPcName());

			st.setLong(++index, getPcObjectId());
			st.setString(++index, psItem.getShoptype());
			st.setLong(++index, psItem.getItemobjid());
			st.setInt(++index, psItem.getItem_id());
			st.setString(++index, psItem.getName());
			st.setInt(++index, psItem.getType());
			st.setLong(++index, psItem.getPrice());
			st.setLong(++index, psItem.getCount());
			st.setInt(++index, psItem.getBless());
			st.setInt(++index, psItem.isDefinite() ? 1 : 0);
			st.setInt(++index, psItem.getEnLevel());
			st.setInt(++index, psItem.getEnEarth());
			st.setInt(++index, psItem.getEnFire());
			st.setInt(++index, psItem.getEnWater());
			st.setInt(++index, psItem.getEnWind());
			st.setLong(++index, psItem.getPetObjId());
			st.setInt(++index, getPcAccountUid());
			st.setString(++index, getPcName());

			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(st);
		}
	}

	private void insertDBNpc(Connection con, int x, int y, int map) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement(
					"INSERT INTO characters_shop_npc SET owner_obj_id=?,loc_x=?,loc_y=?,loc_map=?,purchase=?");
			st.setLong(1, getPcObjectId());
			st.setInt(2, x);
			st.setInt(3, y);
			st.setInt(4, map);
			st.setLong(5, purchasePrice);
			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(st);
		}
	}
}
