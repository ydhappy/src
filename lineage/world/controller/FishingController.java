package lineage.world.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import lineage.bean.database.FishList;
import lineage.bean.database.Item;
import lineage.database.DatabaseConnection;
import lineage.database.FishItemListDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.LineageServer;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Disconnect;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.Character;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.FishermanInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.ShapeChange;

public final class FishingController {
		static public Map<Integer, FishermanInstance> auto_fish_list;
		static public Map<Integer, BackgroundInstance> auto_fish_effect_list;

		static public void init(Connection con) {
			TimeLine.start("FishingController..");

			auto_fish_list = new HashMap<Integer, FishermanInstance>();
			auto_fish_effect_list = new HashMap<Integer, BackgroundInstance>();

			auto_fish_list.clear();

			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				st = con.prepareStatement("SELECT * FROM auto_fish_list");
				rs = st.executeQuery();

				while (rs.next()) {
					new FishermanInstance(rs.getLong("pc_objId"), rs.getInt("account_uid"), rs.getString("pc_name"),
							rs.getInt("gfx"), rs.getInt("gfx_mode"), rs.getInt("fish_time"), rs.getLong("coin_count"),
							rs.getInt("loc_x"), rs.getInt("loc_y"), rs.getInt("loc_map"), rs.getInt("heading"),
							rs.getLong("rice_count"));

					int x = rs.getInt("loc_x");
					int y = rs.getInt("loc_y");
					switch (rs.getInt("heading")) {
						case 0:
							y -= 5;
							break;
						case 1:
							x += 5;
							y -= 5;
							break;
						case 2:
							x += 5;
							break;
						case 3:
							x += 5;
							y += 5;
							break;
						case 4:
							y += 5;
							break;
						case 5:
							x -= 5;
							y += 5;
							break;
						case 6:
							x -= 5;
							break;
						case 7:
							x -= 5;
							y -= 5;
							break;
					}

					BackgroundInstance effect = new lineage.world.object.npc.background.FishingEffect();
					effect.setGfx(365);
					effect.setObjectId(ServerDatabase.nextEtcObjId());
					effect.toTeleport(x, y, rs.getInt("loc_map"), false);
					auto_fish_effect_list.put(rs.getInt("account_uid"), effect);
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : init(Connection con)\r\n", FishingController.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(st, rs);
			}

			TimeLine.end();
		}

		static public void close(Connection con) {
			PreparedStatement st = null;
			try {
				st = con.prepareStatement("DELETE FROM auto_fish_list");
				st.executeUpdate();
				st.close();

				for (FishermanInstance fisherman : auto_fish_list.values()) {
					st = con.prepareStatement(
							"INSERT INTO auto_fish_list SET account_uid=?, pc_objId=?, pc_name=?, loc_x=?, loc_y=?, loc_map=?, heading=?, gfx=?, gfx_mode=?, fish_time=?, coin_count=?, rice_count=?");
					st.setInt(1, fisherman.getPc_accountUid());
					st.setLong(2, fisherman.getPc_objectId());
					st.setString(3, fisherman.getPc_name());
					st.setInt(4, fisherman.getX());
					st.setInt(5, fisherman.getY());
					st.setInt(6, fisherman.getMap());
					st.setInt(7, fisherman.getHeading());
					st.setInt(8, fisherman.getGfx());
					st.setInt(9, fisherman.getGfxMode());
					st.setInt(10, fisherman.getFishTime());
					st.setLong(11, fisherman.getCoin());
					st.setLong(12, fisherman.getRice());
					st.executeUpdate();
					st.close();
				}

				auto_fish_list.clear();
				auto_fish_effect_list.clear();
			} catch (Exception e) {
				lineage.share.System.printf("%s : close(Connection con)\r\n", FishingController.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(st);
			}
		}

		/**
		 * 변신 상태일 경우 변신해제를 위해 메시지 창을 띄워서 y/n을 입력받았을때 처리
		 */
		public static void toAsk(PcInstance pc, boolean yes) {
			if (yes && pc.isFishingZone()) {
				BuffController.remove(pc, ShapeChange.class);
				ChattingController.toChatting(pc, "변신 해제", Lineage.CHATTING_MODE_MESSAGE);
				if (pc.getClassType() == 0) {
					if (pc.getClassSex() == 0) {
						pc.setGfx(0);
					} else {
						pc.setGfx(1);
					}
				}
				if (pc.getClassType() == 1) {
					if (pc.getClassSex() == 0) {
						pc.setGfx(61);
					} else {
						pc.setGfx(48);
					}
				}
				if (pc.getClassType() == 2) {
					if (pc.getClassSex() == 0) {
						pc.setGfx(138);
					} else {
						pc.setGfx(37);
					}
				}
				if (pc.getClassType() == 3) {
					if (pc.getClassSex() == 0) {
						pc.setGfx(734);
					} else {
						pc.setGfx(1186);
					}
				}
				pc.getTempFishing().toClick(pc, null);
			}
		}

		public static void startFishing(PcInstance pc) {
			ItemInstance fishing = pc.getInventory().getSlot(Lineage.SLOT_WEAPON);
			// 인벤토리에서 영양 미끼 찾음
			if (fishing != null) {
				// 낚시중 기본 gfx가 아닐경우 낚시종료.
				if (pc.getGfx() != pc.getClassGfx()) {
					pc.setFishing(false);
					fishing.toClick(pc, null);
					ChattingController.toChatting(pc, "세트 아이템 착용 중 일 경우 낚시가 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				ItemInstance rice = pc.getInventory().find(ItemDatabase.find(Lineage.fish_rice));
				// 영양미끼가 없으면 낚시 종료
				if (rice == null) {
					pc.setFishing(false);
					fishing.toClick(pc, null);
					// 영양 미끼가 충분치 않습니다.
					pc.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null,
							Lineage.CHATTING_MODE_MESSAGE, String.format("(%s)가 충분치 않습니다.", Lineage.fish_rice)));
					
					return;
				} else {
					// 영양 미끼가 있을 경우, 낚시 시작. 주기적으로 위치 체크
					if (pc.isFishingZone()) {
						fishing(pc, rice, fishing);
					} else {
						pc.setFishing(false);
						fishing.toClick(pc, null);
					}
				}
			}
			// 낚시 찌 이팩트 주기적으로 보내기
			pc.getFishEffect().toSender(
					S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc.getFishEffect(), 366), true);

			if ((pc.isFishing() && pc.getFishStartHeading() != pc.getHeading())) {
				int x = pc.getX();
				int y = pc.getY();
				switch (pc.getHeading()) {
					case 0:
						y -= 5;
						break;
					case 1:
						x += 5;
						y -= 5;
						break;
					case 2:
						x += 5;
						break;
					case 3:
						x += 5;
						y += 5;
						break;
					case 4:
						y += 5;
						break;
					case 5:
						x -= 5;
						y += 5;
						break;
					case 6:
						x -= 5;
						break;
					case 7:
						x -= 5;
						y -= 5;
						break;
				}
				pc.getFishEffect().toTeleport(x, y, pc.getMap(), false);
			}
		}

		// 낚싯대 마다 시간체크하여 물고기 낚음
		public static void fishing(PcInstance pc, ItemInstance fishRice, ItemInstance fishing) {
			if (fishing != null) {
				if (pc.getFishingTime() + (1000 * Lineage.fish_delay) < System.currentTimeMillis()) {
					huntFish(pc);
					pc.setFishingTime(System.currentTimeMillis());
					pc.getInventory().count(fishRice, fishRice.getCount() - 1, true);
				}
			}
		}

		// 물고기 낚는 처리
		public static void huntFish(PcInstance pc) {

			if (FishItemListDatabase.getFishList().size() > 0) {
				FishList fishList = FishItemListDatabase.getFishList()
						.get(Util.random(0, FishItemListDatabase.getFishList().size() - 1));

				if (fishList != null) {
					Item ii = ItemDatabase.find(fishList.getItemName());

					if (ii != null) {
						ItemInstance temp = pc.getInventory().find(fishList.getItemName(), fishList.getItemBress(),
								ii.isPiles());
						int count = Util.random(fishList.getItemCountMin(), fishList.getItemCountMax());

						if (temp != null && (temp.getBress() != fishList.getItemBress()
								|| temp.getEnLevel() != fishList.getItemEnchant()))
							temp = null;

						if (temp == null) {
							// 겹칠수 있는 아이템이 존재하지 않을경우.
							if (ii.isPiles()) {
								temp = ItemDatabase.newInstance(ii);
								temp.setObjectId(ServerDatabase.nextItemObjId());
								temp.setBress(fishList.getItemBress());
								temp.setEnLevel(fishList.getItemEnchant());
								temp.setCount(count);
								temp.setDefinite(true);
								pc.getInventory().append(temp, true);
							} else {
								for (int idx = 0; idx < count; idx++) {
									temp = ItemDatabase.newInstance(ii);
									temp.setObjectId(ServerDatabase.nextItemObjId());
									temp.setBress(fishList.getItemBress());
									temp.setEnLevel(fishList.getItemEnchant());
									temp.setDefinite(true);
									pc.getInventory().append(temp, true);
								}
							}
						} else {
							// 겹치는 아이템이 존재할 경우.
							pc.getInventory().count(temp, temp.getCount() + count, true);
						}

						ChattingController.toChatting(pc, String.format("%s(%d) 획득: 낚시", fishList.getItemName(), count),
								Lineage.CHATTING_MODE_MESSAGE);
					}
				}
			}
		}

		// 낚시 시작시 이펙트 처리
		public static void setFishEffect(Character cha) {
			if (cha.isFishing() && !cha.isWorldDelete() && cha.isFishingZone()) {
				int x = cha.getX();
				int y = cha.getY();
				switch (cha.getHeading()) {
					case 0:
						y -= 5;
						break;
					case 1:
						x += 5;
						y -= 5;
						break;
					case 2:
						x += 5;
						break;
					case 3:
						x += 5;
						y += 5;
						break;
					case 4:
						y += 5;
						break;
					case 5:
						x -= 5;
						y += 5;
						break;
					case 6:
						x -= 5;
						break;
					case 7:
						x -= 5;
						y -= 5;
						break;
				}
				cha.setFishEffect(new lineage.world.object.npc.background.FishingEffect());
				cha.getFishEffect().setGfx(365);
				cha.getFishEffect().setObjectId(ServerDatabase.nextEtcObjId());
				cha.getFishEffect().toTeleport(x, y, cha.getMap(), false);
			} else {
				cha.getFishEffect().clearList(true);
				World.remove(cha.getFishEffect());
				cha.setFishEffect(null);
			}
		}

		/**
		 * 자동낚시가 가능한지 여부를 체크하는 함수
		 */
		public static void isAutoFishing(PcInstance pc) {
			// 군터의 인장 찾아서 체크
			ItemInstance coin = pc.getInventory().find(ItemDatabase.find(Lineage.auto_fish_coin));
			ItemInstance rice = pc.getInventory().find(ItemDatabase.find(Lineage.fish_rice));

			if (coin == null || coin.getCount() < Lineage.auto_fish_expense) {
				// 군터의인장이 충분치 않습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 776, Lineage.auto_fish_coin));
				return;
			}

			if (rice == null) {
				// 영양 미끼가 충분치 않습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 776, Lineage.fish_rice));
				return;
			}

			// 자동낚시를 시작하시겠습니까? (y/N)
			pc.toSender(S_MessageYesNo.clone(BasePacketPooling.getPool(S_MessageYesNo.class), 778));
		}

		public static void startAutoFishing(PcInstance pc, boolean yes) {
			if (pc.isFishing() && !pc.isLock() && !pc.isWorldDelete() && !pc.isDead()) {
				ChattingController.toChatting(pc, "자동낚시 시작. 클라이언트가 종료됩니다.", Lineage.CHATTING_MODE_MESSAGE);

				try {
					ItemInstance coin = pc.getInventory().find(ItemDatabase.find(Lineage.auto_fish_coin));
					ItemInstance rice = pc.getInventory().find(ItemDatabase.find(Lineage.fish_rice));

					new FishermanInstance(pc.getObjectId(), pc.getAccountUid(), pc.getName(), pc.getGfx(), pc.getGfxMode(),
							Lineage.fish_delay, coin.getCount(), pc.getX(), pc.getY(), pc.getMap(), pc.getHeading(),
							rice.getCount());

					int x = pc.getX();
					int y = pc.getY();
					switch (pc.getHeading()) {
						case 0:
							y -= 1;
							break;
						case 1:
							x += 1;
							y -= 1;
							break;
						case 2:
							x += 1;
							break;
						case 3:
							x += 1;
							y += 1;
							break;
						case 4:
							y += 1;
							break;
						case 5:
							x -= 1;
							y += 1;
							break;
						case 6:
							x -= 1;
							break;
						case 7:
							x -= 1;
							y -= 1;
							break;
					}

					BackgroundInstance effect = new lineage.world.object.npc.background.FishingEffect();
					effect.setGfx(365);
					effect.setObjectId(ServerDatabase.nextEtcObjId());
					effect.toTeleport(x, y, pc.getMap(), false);
					auto_fish_effect_list.put(pc.getAccountUid(), effect);
				} catch (Exception e) {
					lineage.share.System.printf("%s : startAutoFishing(PcInstance pc, boolean yes)\r\n",
							FishingController.class.toString());
					lineage.share.System.println(e);
				}

				// 사용자 강제종료 시키기.
				pc.toSender(S_Disconnect.clone(BasePacketPooling.getPool(S_Disconnect.class), 0x0A));
				LineageServer.close(pc.getClient());
			}
		}

	}