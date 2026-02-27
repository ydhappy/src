package lineage.world.object.instance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.database.Item;
import lineage.bean.database.PcShop;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_PcShopBuy;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.CharacterController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.LetterController;
import lineage.world.controller.PcMarketController;

public class PcShopInstance extends BackgroundInstance {

	public Map<Long, PcShop> list; // 개인상점 판매목록.
	public String shop_comment; // 개인상점 광고 문구.
	private int show_comment; //
	public int classType;
	public int classSex;
	private long pc_objectId;
	public String pc_name;
	public boolean close_status;

	public PcShopInstance(long pc_objId, String pc_name, int classType, int classSex) {
		list = new HashMap<Long, PcShop>();

		pc_objectId = pc_objId;
		this.pc_name = pc_name;
		this.classType = classType;
		this.classSex = classSex;

		switch (classType) {
		case 0:
			if (classSex == 0)
				setGfx(2611);
			else
				setGfx(2612);
			break;
		case 1:
			if (classSex == 0)
				setGfx(2613);
			else
				setGfx(2614);
			break;
		case 2:
			if (classSex == 0)
				setGfx(2615);
			else
				setGfx(2616);
			break;
		case 3:
			if (classSex == 0)
				setGfx(2617);
			else
				setGfx(2618);
			break;
		}	
		setObjectId(ServerDatabase.nextEtcObjId());
		setName(pc_name + "의 상점");
		setClanId(0);
		setClanName("");
		setTitle("");
		setLawful(66536);
		//setGfx(1216);
		setGfxMode(0);
		CharacterController.toWorldJoin(this);
		World.appendPcShop(this);
	}

	@Override
	public void close() {
		if (list != null)
			list.clear();

		clearList(true);
		World.remove(this);

		super.close();

		CharacterController.toWorldOut(this);
		World.removePcShop(this);
	}

	public boolean isClose_status() {
		return close_status;
	}

	public void setClose_status(boolean close_status) {
		this.close_status = close_status;
	}

	public long getPc_objectId() {
		return pc_objectId;
	}

	public String getShopComment() {
		return shop_comment;
	}
	
	public Map<Long, PcShop> getShopList_() {
		synchronized (list) {
			return list;
		}
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		List<String> info = new ArrayList<String>();
		info.clear();
		info.add(getName().replace("의 상점", ""));
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "pcshop", null, info));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		if (action.equalsIgnoreCase("Sell_plus")) {
			if(pc_objectId == pc.getObjectId()) {
				pc.setShop_inven_num(1);
				pc.setInven_stat_object(pc);
				pc.clearTempList();
				PcMarketController.SellAsk(pc);
			}else {
				ChattingController.toChatting(pc, "자신의 상점만 등록 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		}else if (action.equalsIgnoreCase("buy")) {
			
   
			pc.toSender(S_PcShopBuy.clone(BasePacketPooling.getPool(S_PcShopBuy.class), this));
		}
	}

	@Override
	public void toDwarfAndShop(PcInstance pc, ClientBasePacket cbp) {
		switch (cbp.readC()) {
		case 0: // 상점 구입
			toBuy(pc, cbp);
			break;
		}
	}

	@Override
	public void toTimer(long time) {
		//
		if (!isWorldDelete() && shop_comment != null && shop_comment.length() > 0) {
			if (show_comment++ % 20 == 0) {
				ChattingController.toChatting(this, shop_comment, Lineage.CHATTING_MODE_NORMAL);
				toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), this, 68), true);
			}
		}
	}

	/**
	 * 상점 구매
	 */
	protected synchronized void toBuy(PcInstance pc, ClientBasePacket cbp) {
		int count = cbp.readH();
		
    
		
		if (count > 0 && count <= 100) {
			for (int j = 0; j < count; ++j) {
				long item_idx = cbp.readD();
				int item_count = (int)cbp.readD();
				if(item_count >2000000000 ||item_count < 0)
					return;
				if (item_count > 0) {
					PcShop s = list.get(item_idx);
					// 수량이 등록한것보다 많게 요청했을때.
					if (s.getInvItemCount() < item_count)
						item_count = (int)s.getInvItemCount();
					if (pc.getInventory().isAppend(s.getItem(), item_count, s.getItem().isPiles() ? 1 : item_count)) {
						if ((pc.getInventory().isAden(s.getAdenType(), s.getPrice() * item_count, false)
								&& s.getPc().getPc_objectId() != pc.getObjectId())
								|| s.getPc().getPc_objectId() == pc.getObjectId()) {

							if (s.getPc().getPc_objectId() != pc.getObjectId())
								pc.getInventory().isAden(s.getAdenType(), s.getPrice() * item_count, true);

							s.setInvItemCount(s.getInvItemCount() - item_count);

							// 사용자 아이템 추가.
							ItemInstance temp = pc.getInventory().find(s.getItem().getName(), s.getInvItemBress(),
									s.getItem().isPiles());
							if (temp == null) {
								// 겹칠수 있는 아이템이 존재하지 않을경우.
								if (s.getItem().isPiles()) {
									temp = ItemDatabase.newInstance(s.getItem());
									temp.setObjectId(ServerDatabase.nextItemObjId());
									temp.setCount(item_count);
									temp.setEnLevel(s.getInvItemEn());
									temp.setDefinite(s.isInvItemDefinite());
									temp.setBress(s.getInvItemBress());
									pc.getInventory().append(temp, true);
								} else {
									for (int k = 0; k < item_count; ++k) {
										temp = ItemDatabase.newInstance(s.getItem());
										temp.setObjectId(ServerDatabase.nextItemObjId());
										temp.setEnLevel(s.getInvItemEn());
										temp.setDefinite(s.isInvItemDefinite());
										temp.setBress(s.getInvItemBress());
										pc.getInventory().append(temp, true);
									}
								}
							} else {
								// 겹치는 아이템이 존재할 경우.
								pc.getInventory().count(temp, temp.getCount() + item_count, true);
							}
							if (s.getPc().getPc_objectId() != pc.getObjectId()) {
								// 아데나 지급.
								PcInstance pcShop = World.findPc(pc_objectId);
								if (pcShop != null) {
									Item item = ItemDatabase.find(s.getAdenType());
									if (item != null) {
										ItemInstance tempItem = ItemDatabase.newInstance(item);
										tempItem.setCount(s.getPrice() * item_count);
										tempItem.setEnLevel(0);
										tempItem.setBress(1);
										tempItem.setDefinite(true);
										// 인벤에 등록처리.
										pcShop.getInventory().append(tempItem, tempItem.getCount());

										if (item_count > 1)
											ChattingController.toChatting(pcShop,
													String.format("개인상점: '%s(%d)' 판매대금 %s(%d) 입금",
															s.getItem().getName(), item_count, s.getAdenType(),
															s.getPrice() * item_count),
													Lineage.CHATTING_MODE_MESSAGE);
										else {
											if (s.getItem().getType1().equalsIgnoreCase("weapon")
													|| s.getItem().getType1().equalsIgnoreCase("armor"))
												ChattingController.toChatting(pcShop,
														String.format("개인상점: '+%d%s' 판매대금 %s(%d) 입금", s.getInvItemEn(),
																s.getItem().getName(), s.getAdenType(),
																s.getPrice() * item_count),
														Lineage.CHATTING_MODE_MESSAGE);
											else
												ChattingController.toChatting(pcShop,
														String.format("개인상점: '%s' 판매대금 %s(%d) 입금",
																s.getItem().getName(), s.getAdenType(),
																s.getPrice() * item_count),
														Lineage.CHATTING_MODE_MESSAGE);
										}
									}
								} else {
									if (item_count > 1)
										LetterController.toLetter("개인상점", pc_name, "판매완료",
												String.format("'%s(%d)' 아이템이 판매완료되어 %s(%d) 입금 되었습니다.",
														s.getItem().getName(), item_count, s.getAdenType(),
														s.getPrice() * item_count),
												(s.getPrice() * item_count), true);
									else {
										if (s.getItem().getType1().equalsIgnoreCase("weapon")
												|| s.getItem().getType1().equalsIgnoreCase("armor"))
											LetterController.toLetter("개인상점", pc_name, "판매완료",
													String.format("'+%d%s' 아이템이 판매완료되어 %s(%d) 입금 되었습니다.",
															s.getInvItemEn(), s.getItem().getName(), s.getAdenType(),
															s.getPrice() * item_count),
													(s.getPrice() * item_count), true);
										else
											LetterController.toLetter("개인상점", pc_name, "판매완료",
													String.format("'%s' 아이템이 판매완료되어 %s(%d) 입금 되었습니다.",
															s.getItem().getName(), s.getAdenType(),
															s.getPrice() * item_count),
													(s.getPrice() * item_count), true);
									}
								}
							} else {
								if (item_count > 1)
									ChattingController.toChatting(pc,
											String.format("개인상점: '%s(%d)' 상점에서 삭제", s.getItem().getName(), item_count),
											Lineage.CHATTING_MODE_MESSAGE);
								else {
									if (s.getItem().getType1().equalsIgnoreCase("weapon")
											|| s.getItem().getType1().equalsIgnoreCase("armor"))
										ChattingController.toChatting(pc, String.format("개인상점: '+%d%s' 상점에서 삭제",
												s.getInvItemEn(), s.getItem().getName()),
												Lineage.CHATTING_MODE_MESSAGE);
									else
										ChattingController.toChatting(pc,
												String.format("개인상점: '%s' 상점에서 삭제", s.getItem().getName()),
												Lineage.CHATTING_MODE_MESSAGE);
								}

							}

							// 디비 갱신.
							if (s.getInvItemCount() == 0) {
								// 삭제.
								list.remove(s.getInvItemObjectId());
								s = null;
							} else {
//								// 업데이트.
//								toUpdate(s);
							}
							if (list.size() < 1) {
								close();
								setClose_status(true);
							}
						} else {
							ChattingController.toChatting(pc, "아데나가 충분하지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
							break;
						}
					}
				}
			}
		}
	}
	
	public int getListSize() {
		synchronized (list) {
			return list.size();
		}
	}

	private void toUpdate(PcShop s) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters_inventory SET count=? WHERE objId=? AND cha_objId=?");
			st.setLong(1, s.getInvItemCount());
			st.setLong(2, s.getInvItemObjectId());
			st.setLong(3, pc_objectId);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : toUpdate(shop s)\r\n", PcShopInstance.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	private long getAdenObjectId(String adenType) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters_inventory WHERE cha_objId=? AND name=? AND bress=1");
			st.setLong(1, pc_objectId);
			st.setString(2, adenType);
			rs = st.executeQuery();
			if (rs.next())
				return rs.getLong("objId");
		} catch (Exception e) {
			lineage.share.System.printf("%s : getAdenObjectId(String adenType)\r\n", PcShopInstance.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return 0;
	}

	private void toInsertAden(long count, String adenType) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement(
					"INSERT INTO characters_inventory SET objId=?, cha_objId=?, cha_name=?, name=?, count=?, definite=1");
			st.setLong(1, ServerDatabase.nextItemObjId());
			st.setLong(2, pc_objectId);
			st.setString(3, getName());
			st.setString(4, adenType);
			st.setLong(5, count);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : toInsertAden(long count, String adenType)\r\n",
					PcShopInstance.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	private void toUpdateAden(PcShop s, long count) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement(
					"UPDATE characters_inventory SET count=count+? WHERE cha_objId=? AND name=? AND bress=1");
			st.setLong(1, count);
			st.setLong(2, pc_objectId);
			st.setString(3, s.getAdenType());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : toUpdateAden(shop s, long count)\r\n", PcShopInstance.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
}