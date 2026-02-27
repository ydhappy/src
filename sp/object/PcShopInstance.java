package sp.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import lineage.database.CharactersDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.share.Log;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.CharacterController;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.RobotInstance;
import sp.bean.shop;
import sp.network.packet.server.S_ShopBuy;
import sp.network.packet.server.S_ShopSell;

public class PcShopInstance extends BackgroundInstance {

	public Map<Long, shop> list; 		// 개인상점 판매목록.
	public Map<Long, shop> list_sell; 	// 개인상점 매입목록.
	public List<String> shop_comment_list; // 개인상점 광고 문구.
	private int show_comment_p;
	private int show_comment; //
	private int show_comment_idx;
	public long pc_objectId;
	private boolean showIdx = true;

	public PcShopInstance(PcInstance pc) {
		list = new HashMap<Long, shop>();
		list_sell = new HashMap<Long, shop>();
		shop_comment_list = new ArrayList<String>();
		show_comment_p = Util.random(10, 20);

		pc_objectId = pc instanceof RobotInstance ? 0 : pc.getObjectId();
		setObjectId(ServerDatabase.nextEtcObjId());
		setName(pc.getName());
		setClanId(pc.getClanId());
		setClanName(pc.getClanName());
		setTitle(pc.getTitle());
		setLawful(pc.getLawful());
		// 산판도라 gfx
		// setGfx( 1224 );
		setGfx(2499);
		setGfxMode(0);

		CharacterController.toWorldJoin(this);
	}

	@Override
	public void close() {
		super.close();
		if (list != null)
			list.clear();
		if (list_sell != null)
			list_sell.clear();
		if (shop_comment_list != null)
			shop_comment_list.clear();

		show_comment = show_comment_idx = 0;
		showIdx = true;
		CharacterController.toWorldOut(this);
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
//		if(list_sell.size() == 0)
//			pc.toSender(S_ShopBuy.clone(BasePacketPooling.getPool(S_ShopBuy.class), this));
//		else
//			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "pandora"));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		if (action.equalsIgnoreCase("buy")) {
//			pc.toSender(S_ShopBuy.clone(BasePacketPooling.getPool(S_ShopBuy.class), this));
		} else if (action.equalsIgnoreCase("sell")) {
			List<ItemInstance> sell_list = new ArrayList<ItemInstance>();
			for (Long key : list_sell.keySet()) {
				shop s = list_sell.get(key);
				List<ItemInstance> search_list = new ArrayList<ItemInstance>();
				pc.getInventory().findDbName(s.getItem().getName(), s.getInvItemBress(), s.getInvItemEn(), search_list);
				for (ItemInstance item : search_list) {
					if (!item.isEquipped() && item.getItem().isSell()) {
						if (!sell_list.contains(item))
							sell_list.add(item);
					}
				}
			}
//			if (sell_list.size() > 0)
//				pc.toSender(S_ShopSell.clone(BasePacketPooling.getPool(S_ShopSell.class), this, sell_list));
//			else
//				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "nosell"));
		}
	}
	
	public shop findSell(ItemInstance item) {
		for (Long key : list_sell.keySet()) {
			shop s = list_sell.get(key);
			if(s.getItem().getName().equalsIgnoreCase(item.getItem().getName()) && s.getInvItemBress()==item.getBress() && s.getInvItemEn()==item.getEnLevel())
				return s;
		}
		return null;
	}

	@Override
	public void toDwarfAndShop(PcInstance pc, ClientBasePacket cbp) {
		switch (cbp.readC()) {
			case 0: // 상점 구입
				toBuy(pc, cbp);
				break;
			case 1: // 상점 판매
				toSell(pc, cbp);
				break;
		}
	}

	@Override
	public void toTimer(long time) {
		//
		try {
			if (!isWorldDelete() && shop_comment_list.size() > 0) {
				if (++show_comment % show_comment_p == 0) {
					show_comment = 0;
					if (shop_comment_list.size() <= show_comment_idx)
						show_comment_idx = 0;
					ChattingController.toChatting(this, shop_comment_list.get(show_comment_idx++), 0);
				}
			}
		} catch (Exception e) {
		}
		//
		if (pc_objectId > 0) {
			if(list_sell.size()==0 && list.size()==0) {
				sp.controller.CommandController.shop_list.remove(pc_objectId);
				clearList(true);
				World.remove(this);
				close();
			}
		}
	}
	
	/**
	 * 상점 판매
	 */
	protected void toSell(PcInstance pc, ClientBasePacket cbp) {
		long count = cbp.readH();
		if (count > 0 && count <= 100) {
			for (int j = 0; j < count; ++j) {
				long inv_id = cbp.readD();
				long item_count = cbp.readH();
				ItemInstance temp = pc.getInventory().value(inv_id);
				if (temp!=null && !temp.isEquipped() && item_count>0 && temp.getCount()>=item_count) {
					//
					shop s = findSell(temp);
					if(s != null) {
						//
						String target_name = temp.toStringDB();
						long target_objid = temp.getObjectId();
						long target_price = s.getPrice() * item_count;
						long aden_count = getAdenCount();
						if (s!=null && aden_count>=target_price) {
							// 아덴 삭제
							CharactersDatabase.updateCharacterInventoryItem(pc_objectId, getAdenObjectId(), aden_count-target_price, 1);
							// 판매되는 아이템 제거.
							pc.getInventory().count(temp, temp.getCount() - item_count, true);
							// 아덴 지급
							ItemInstance aden = pc.getInventory().find("아데나", true);
							if (aden == null) {
								aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
								aden.setObjectId(ServerDatabase.nextItemObjId());
								aden.setCount(0);
								pc.getInventory().append(aden, true);
							}
							pc.getInventory().count(aden, aden.getCount() + target_price, true);
							// 상인에게 등록.
							toUpdate(s, item_count, true);
							// 안내 메세지.
							pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 877, getName(), s.getItem().getNameId()));
							// log
							Log.appendItem(pc, "type|개인상점판매", String.format("pc_name|%s", getName()), String.format("use_name|%s", pc.getName()), String.format("target_name|%s", target_name),
									String.format("target_objid|%d", target_objid), String.format("item_count|%d", item_count));
						} else {
							ChattingController.toChatting(pc, "판매에 실패 하였습니다.", 20);
							return;
						}
					}
				}
			}
		}
	}

	/**
	 * 상점 구매
	 */
	protected void toBuy(PcInstance pc, ClientBasePacket cbp) {
		int count = cbp.readH();
		if (count > 0 && count <= 100) {
			for (int j = 0; j < count; ++j) {
				long item_idx = cbp.readD();
				long item_count = cbp.readD();
				if (item_count > 0) {
					shop s = list.get(item_idx);
					// 수량이 등록한것보다 많게 요청했을때.
					if (s.getInvItemCount() < item_count)
						item_count = s.getInvItemCount();
					// 메세지표현
					// ChattingController.toChatting(pc, "판매 수량이 맞지않습니다. 다시 확인하시고 구매해주세여", 20);

					if (pc.getInventory().isAppend(s.getItem(), item_count, s.getItem().isPiles() ? 1 : item_count)) {
						if (pc.getInventory().isAden("아데나", s.getPrice() * item_count, true)) {
							s.setInvItemCount(s.getInvItemCount() - item_count);

							// 사용자 아이템 추가.
							ItemInstance temp = pc.getInventory().find(s.getItem().getName(), s.getInvItemBress(), true);
							if (temp == null) {
								// 겹칠수 있는 아이템이 존재하지 않을경우.
								if (s.getItem().isPiles()) {
									temp = ItemDatabase.newInstance(s.getItem());
									temp.setObjectId(ServerDatabase.nextItemObjId());
									temp.setCount(item_count);
									temp.setEnLevel(s.getInvItemEn());
									temp.setDefinite(s.isInvItemDefinite());
									temp.setBress(s.getInvItemBress());
									if (s.getOption() != null) {
										StringTokenizer tok = new StringTokenizer(s.getOption(), "|");
										while (tok.hasMoreTokens()) {
											String[] token = tok.nextToken().split("=");
											temp.setOption(token[0], token[1]);
										}
									}
									pc.getInventory().append(temp, true);
									//
									Log.appendItem(this, "type|개인상점구입", String.format("pc_name|%s", getName()), String.format("use_name|%s", pc.getName()),
											String.format("item_name|%s", temp.toStringDB()), String.format("item_objid|%d", temp.getObjectId()), String.format("count|%d", item_count),
											String.format("shop_count|%d", s.getInvItemCount()));
								} else {
									for (int k = 0; k < item_count; ++k) {
										temp = ItemDatabase.newInstance(s.getItem());
										temp.setObjectId(ServerDatabase.nextItemObjId());
										temp.setEnLevel(s.getInvItemEn());
										temp.setDefinite(s.isInvItemDefinite());
										temp.setBress(s.getInvItemBress());
										if (s.getOption() != null) {
											StringTokenizer tok = new StringTokenizer(s.getOption(), "|");
											while (tok.hasMoreTokens()) {
												String[] token = tok.nextToken().split("=");
												temp.setOption(token[0], token[1]);
											}
										}
										pc.getInventory().append(temp, true);
										//
										Log.appendItem(this, "type|개인상점구입", String.format("pc_name|%s", getName()), String.format("use_name|%s", pc.getName()),
												String.format("item_name|%s", temp.toStringDB()), String.format("item_objid|%d", temp.getObjectId()), String.format("count|%d", 1),
												String.format("shop_count|%d", s.getInvItemCount()));
									}
								}
							} else {
								// 겹치는 아이템이 존재할 경우.
								pc.getInventory().count(temp, temp.getCount() + item_count, true);
								//
								Log.appendItem(this, "type|개인상점구입", String.format("pc_name|%s", getName()), String.format("use_name|%s", pc.getName()),
										String.format("item_name|%s", s.getItem().getName()), String.format("target_name|%s", temp.toStringDB()),
										String.format("target_objid|%d", temp.getObjectId()), String.format("count|%d", item_count), String.format("shop_count|%d", s.getInvItemCount()));
							}

							// 아데나 지급.
							long aden_objectid = getAdenObjectId();
							if (aden_objectid == 0) {
								// 등록
								toInsertAden(s.getPrice() * item_count);
							} else {
								// 갱신
								toUpdateAden(s, s.getPrice() * item_count);
							}

							// 디비 갱신.
							if (s.getInvItemCount() == 0) {
								// 삭제.
								toDelete(s);
								list.remove(s.getInvItemObjectId());
								s = null;
							} else {
								// 업데이트.
								toUpdate(s, s.getInvItemCount(), false);
							}

						} else {
							// \f1아데나가 충분치 않습니다.
							ChattingController.toChatting(pc, "아데나가 충분치 않습니다.", 20);
							break;
						}
					}
				}
			}
		}
	}

	private void toDelete(shop s) {
		CharactersDatabase.removeCharacterInventoryItem(pc_objectId, s.getInvItemObjectId());
	}

	private void toUpdate(shop s, long count, boolean append) {
		long item_objectid = CharactersDatabase.findCharacterInventoryItem(pc_objectId, s.getItem().getName(), s.getInvItemBress());
		if (item_objectid == 0) {
			ItemInstance temp = ItemDatabase.newInstance(s.getItem());
			temp.setObjectId(s.getInvItemObjectId());
			temp.setCount(count);
			temp.setEnLevel(s.getInvItemEn());
			temp.setDefinite(s.isInvItemDefinite());
			temp.setBress(s.getInvItemBress());
			StringTokenizer tok = new StringTokenizer(s.getOption(), "|");
			while (tok.hasMoreTokens()) {
				String[] t = tok.nextToken().split("=");
				temp.setOption(t[0], t[1]);
			}
			CharactersDatabase.appendCharacterInventoryItem(pc_objectId, getName(), temp);
			ItemDatabase.setPool(temp);
		} else {
			long item_count = append ? CharactersDatabase.getCharacterInventoryItemCount(pc_objectId, item_objectid) : 0;
			CharactersDatabase.updateCharacterInventoryItem(pc_objectId, s.getInvItemObjectId(), item_count+count, s.getInvItemBress());
		}
	}
	
	private long getAdenCount() {
		long aden_objectId = getAdenObjectId();
		return CharactersDatabase.getCharacterInventoryItemCount(pc_objectId, aden_objectId);
	}

	private long getAdenObjectId() {
		return CharactersDatabase.findCharacterInventoryItem(pc_objectId, "아데나", 1);
	}

	private void toInsertAden(long count) {
		ItemInstance temp = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
		temp.setObjectId(ServerDatabase.nextItemObjId());
		temp.setCount(count);
		CharactersDatabase.appendCharacterInventoryItem(pc_objectId, getName(), temp);
		ItemDatabase.setPool(temp);
	}

	private void toUpdateAden(shop s, long count) {
		ItemInstance temp = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
		temp.setObjectId(ServerDatabase.nextItemObjId());
		temp.setCount(count);
		CharactersDatabase.appendCharacterInventoryItem(pc_objectId, getName(), temp);
		ItemDatabase.setPool(temp);
	}

}
