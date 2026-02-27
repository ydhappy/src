package jsn_soft;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Shop;
import lineage.bean.database.SpriteFrame;
import lineage.database.ItemDatabase;
import lineage.database.NpcSpawnlistDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectLock;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.ShopInstance;
import lineage.world.object.item.Arrow;

public final class AutoHuntController {
	// 카운트 최대 횟수
	private static int maxCount = 100;

	private static String action;

	public static String getAction() {
		return action;
	}

	public static void setAction(String action) {
		AutoHuntController.action = action;
	}

	static public boolean toChatting(object o, String msg) {
		if (o == null)
			return false;
		else if (!(o instanceof PcInstance))
			return false;
		PcInstance pc = (PcInstance) o;
		try {
			object html = NpcSpawnlistDatabase.auto_hunt;
			jsn_hunt jh = AutoHuntDatabase.find(pc.getObjectId());
			if (!pc.isAutocommand())
				return false;
			long l1 = java.lang.System.currentTimeMillis() / 1000L;
			if (getAction() != null && o.getDelaytime() + 5L > l1) {
				long l2 = o.getDelaytime() + 5L - l1;
				// 지연시간 넣었어여 너무 빠르게 하니까 포문을 못따라가영 ㄷㄷ;아 그렇군아 알겟습니다
				ChattingController.toChatting(o, new StringBuilder().append(l2).append("초간의 지연 시간이 필요합니다.").toString(),20);
				setAction(null);
				pc.setAutocommand(false);
				return true;
			}
			if (msg.equalsIgnoreCase("종료")) {
				AutoHuntThread.getInstance().removeAuto(pc);
				setAction(null);
				pc.setAutocommand(false);
				o.setDelaytime(l1);
				return true;
			} else if (msg.equalsIgnoreCase("시간")) {
				long hour = pc.getAutoTime() / 60 / 60;
				long min = pc.getAutoTime() / 60 % 60;
				long sec = pc.getAutoTime() % 60 % 60;
				ChattingController.toChatting(pc,
						"남은 시간: " + (hour > 0 ? hour + "시간 " : " ") + min + "분" + (sec != 0 ? sec + "초" : ""), Lineage.CHATTING_MODE_MESSAGE);
				o.setDelaytime(l1);
				return true;
			} else if (getAction() != null && getAction().equalsIgnoreCase("dgludio")) {
				if (Integer.valueOf(msg) > 5) { // 5
					setAction(null);
					pc.setAutocommand(false);
					ChattingController.toChatting(pc, String.format("본토 던전은 최대 5층까지 선택하실 수 있습니다."), Lineage.CHATTING_MODE_MESSAGE);
					pc.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
					o.setDelaytime(l1);
					return true;
				}
				if (Integer.valueOf(msg) == 1)
					AutoHuntDatabase.saveLocation(pc, 32809, 32732, 6 + Integer.valueOf(msg));
				else if (Integer.valueOf(msg) == 2)
					AutoHuntDatabase.saveLocation(pc, 32731, 32731, 6 + Integer.valueOf(msg));
				else if (Integer.valueOf(msg) == 3)
					AutoHuntDatabase.saveLocation(pc, 32797, 32753, 6 + Integer.valueOf(msg));
				else if (Integer.valueOf(msg) == 4)
					AutoHuntDatabase.saveLocation(pc, 32763, 32773, 6 + Integer.valueOf(msg));
				else if (Integer.valueOf(msg) == 5)
					AutoHuntDatabase.saveLocation(pc, 32729, 32724, 6 + Integer.valueOf(msg));
				setAction(null);
				List<String> info = new ArrayList<String>();
				info.clear();
				String mapname = AutoHuntController.getMapName(jh.getX(), jh.getY(), jh.getMap());
				pc.setAutocommand(false);
				info.add(mapname);
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), html, "autohunt2", null, info));
				ChattingController.toChatting(pc, "설정되었습니다. " + mapname + "", Lineage.CHATTING_MODE_MESSAGE);
				o.setDelaytime(l1);
				return true;
			} else if (getAction() != null && getAction().equalsIgnoreCase("ddragon")) {
				if (Integer.valueOf(msg) > 5) {
					setAction(null);
					pc.setAutocommand(false);
					ChattingController.toChatting(pc, String.format("용의 계곡 던전은 최대 5층까지 선택하실 수 있습니다."), Lineage.CHATTING_MODE_MESSAGE);
					pc.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
					o.setDelaytime(l1);
					return true;
				}
				if (Integer.valueOf(msg) == 1)
					AutoHuntDatabase.saveLocation(pc, 32741, 32760, 29 + Integer.valueOf(msg));
				else if (Integer.valueOf(msg) == 2)
					AutoHuntDatabase.saveLocation(pc, 32759, 32750, 29 + Integer.valueOf(msg));
				else if (Integer.valueOf(msg) == 3)
					AutoHuntDatabase.saveLocation(pc, 32705, 32820, 29 + Integer.valueOf(msg));
				else if (Integer.valueOf(msg) == 4)
					AutoHuntDatabase.saveLocation(pc, 32670, 32867, 29 + Integer.valueOf(msg));
				else if (Integer.valueOf(msg) == 5)
					AutoHuntDatabase.saveLocation(pc, 32746, 32801, 30 + Integer.valueOf(msg));
				setAction(null);
				List<String> info = new ArrayList<String>();
				info.clear();
				String mapname = AutoHuntController.getMapName(jh.getX(), jh.getY(), jh.getMap());
				pc.setAutocommand(false);
				info.add(mapname);
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), html, "autohunt2", null, info));
				ChattingController.toChatting(pc, "설정되었습니다. " + mapname + "", Lineage.CHATTING_MODE_MESSAGE);
				o.setDelaytime(l1);
				return true;
			} else if (getAction() != null && getAction().equalsIgnoreCase("delftree")) {
				if (Integer.valueOf(msg) > 3) { // 바로 인트벨류 때리졍? 네 이러면 오류가 나요 보시면
					setAction(null);
					pc.setAutocommand(false);
					ChattingController.toChatting(pc, String.format("요정의 숲 던전은 최대 3층까지 선택하실 수 있습니다."), Lineage.CHATTING_MODE_MESSAGE);
					pc.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
					o.setDelaytime(l1);
					return true;
				}
				if (Integer.valueOf(msg) == 1)
					AutoHuntDatabase.saveLocation(pc, 32772, 32778, 18 + Integer.valueOf(msg));
				else if (Integer.valueOf(msg) == 2)
					AutoHuntDatabase.saveLocation(pc, 32766, 32795, 18 + Integer.valueOf(msg));
				else if (Integer.valueOf(msg) == 3)
					AutoHuntDatabase.saveLocation(pc, 32730, 32736, 18 + Integer.valueOf(msg));
				setAction(null);
				List<String> info = new ArrayList<String>();
				info.clear();
				String mapname = AutoHuntController.getMapName(jh.getX(), jh.getY(), jh.getMap());
				pc.setAutocommand(false);
				info.add(mapname);
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), html, "autohunt2", null, info));
				ChattingController.toChatting(pc, "설정되었습니다. " + mapname + "", Lineage.CHATTING_MODE_MESSAGE);
				o.setDelaytime(l1);
				return true;
			} else if (getAction() != null && getAction().equalsIgnoreCase("ddesert")) {
				if (Integer.valueOf(msg) > 4) {
					setAction(null);
					pc.setAutocommand(false);
					ChattingController.toChatting(pc, String.format("수련 던전은 최대 4층까지 선택하실 수 있습니다."), Lineage.CHATTING_MODE_MESSAGE);
					pc.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
					o.setDelaytime(l1);
					return true;
				}
				if (Integer.valueOf(msg) == 1)
					AutoHuntDatabase.saveLocation(pc, 32804, 32808, 24 + Integer.valueOf(msg));
				else if (Integer.valueOf(msg) == 2)
					AutoHuntDatabase.saveLocation(pc, 32793, 32766, 24 + Integer.valueOf(msg));
				else if (Integer.valueOf(msg) == 3)
					AutoHuntDatabase.saveLocation(pc, 32724, 32749, 24 + Integer.valueOf(msg));
				else if (Integer.valueOf(msg) == 4)
					AutoHuntDatabase.saveLocation(pc, 32799, 32797, 24 + Integer.valueOf(msg));
				setAction(null);
				List<String> info = new ArrayList<String>();
				info.clear();
				String mapname = AutoHuntController.getMapName(jh.getX(), jh.getY(), jh.getMap());
				pc.setAutocommand(false);
				info.add(mapname);
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), html, "autohunt2", null, info));
				ChattingController.toChatting(pc, "설정되었습니다. " + mapname + "", Lineage.CHATTING_MODE_MESSAGE);
				o.setDelaytime(l1);
				return true;	
			} else if (getAction() != null && getAction().equalsIgnoreCase("dwater")) {
				if (Integer.valueOf(msg) > 4) {
					setAction(null);
					pc.setAutocommand(false);
					ChattingController.toChatting(pc, String.format("에바 던전은 최대 4층까지 선택하실 수 있습니다."), Lineage.CHATTING_MODE_MESSAGE);
					pc.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
					o.setDelaytime(l1);
					return true;
				}
				if (Integer.valueOf(msg) == 1)
					AutoHuntDatabase.saveLocation(pc, 32698, 32800, 58 + Integer.valueOf(msg));
				else if (Integer.valueOf(msg) == 2)
					AutoHuntDatabase.saveLocation(pc, 32724, 32856, 58 + Integer.valueOf(msg));
				else if (Integer.valueOf(msg) == 3)
					AutoHuntDatabase.saveLocation(pc, 32733, 32862, 58 + Integer.valueOf(msg));
				else if (Integer.valueOf(msg) == 4)
					AutoHuntDatabase.saveLocation(pc, 32805, 32870, 59 + Integer.valueOf(msg));
				setAction(null);
				List<String> info = new ArrayList<String>();
				info.clear();
				String mapname = AutoHuntController.getMapName(jh.getX(), jh.getY(), jh.getMap());
				pc.setAutocommand(false);
				info.add(mapname);
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), html, "autohunt2", null, info));
				ChattingController.toChatting(pc, "설정되었습니다. " + mapname + "", Lineage.CHATTING_MODE_MESSAGE);
				o.setDelaytime(l1);
				return true;
			} else if (getAction() != null && getAction().equalsIgnoreCase("addsell")) {
				Item item = ItemDatabase.find(msg);
				if (item == null) {
					int num = Integer.valueOf(msg);
					item = ItemDatabase.find(pc.getInventory().value(num).getItem().getName());
				}
				for (Item i : pc.sell_List) {
					if (i == item) {
						ChattingController.toChatting(pc, String.format("이미 등록하신 아이템입니다. (%s)", item.getName()),
								Lineage.CHATTING_MODE_MESSAGE);
						setAction(null);
						pc.setAutocommand(false);
						o.setDelaytime(l1);
						return true;
					}
				}
				if (isShop(o, item.getName())) {
					ChattingController.toChatting(pc, String.format("자동 사냥에 필요한 아이템입니다. (%s)", item.getName()),
							Lineage.CHATTING_MODE_MESSAGE);
					setAction(null);
					pc.setAutocommand(false);
					o.setDelaytime(l1);
					return true;
				}
				boolean result = false;
				for (ShopInstance si : NpcSpawnlistDatabase.getSellShopList()) {
					for (Shop s : si.getNpc().getShop_list()) {
						// 본토의 상점만 검색.
						if (si.getMap() == 4 && s.getItemName().equalsIgnoreCase(item.getName())
								&& s.getItemBress() == 1 && s.getAdenType().equalsIgnoreCase("아데나")) {
							result = true;
						}

					}
				}
				if (!result) {
					ChattingController.toChatting(pc, String.format("매입하지 않는 아이템입니다. (%s)", item.getName()),
							Lineage.CHATTING_MODE_MESSAGE);
					setAction(null);
					pc.setAutocommand(false);
					o.setDelaytime(l1);
					return true;
				}

				pc.sell_List.add(item);
//            AutoHuntDatabase.insertSellList(pc, item);
				ChattingController.toChatting(pc, String.format("판매 물품이 추가되었습니다. (%s)", item.getName()),
						Lineage.CHATTING_MODE_MESSAGE);
				setAction(null);
				pc.setAutocommand(false);
				List<String> info = new ArrayList<String>();
				info.clear();

				info.add(pc.isSellItem() ? "ON" : "OFF");

				if (pc.sell_List.size() > 0) {
					for (Item i : pc.sell_List) {
						info.add(i.getName());
					}
				}
				for (int i = 0; i < (20 - pc.sell_List.size()); i++) {
					info.add(" ");
				}

				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), html, "autohunt3", null, info));
				pc.setAutocommand(false);
				o.setDelaytime(l1);
				return true;

			} else if (getAction() != null && getAction().equalsIgnoreCase("delsell")) {
				Item item = ItemDatabase.find(msg);
				if (item == null) {
					int num = Integer.valueOf(msg);
					item = ItemDatabase.find(pc.getInventory().value(num).getItem().getName());
				}
				pc.sell_List.remove(item);
				ChattingController.toChatting(pc, String.format("판매 물품이 삭제되었습니다. (%s)", item.getName()),
						Lineage.CHATTING_MODE_MESSAGE);
				setAction(null);
				pc.setAutocommand(false);
				List<String> info = new ArrayList<String>();
				info.clear();

				info.add(pc.isSellItem() ? "ON" : "OFF");

				if (pc.sell_List.size() > 0) {
					for (Item i : pc.sell_List) {
						info.add(i.getName());
					}
				}

				for (int i = 0; i < (20 - pc.sell_List.size()); i++) {
					info.add(" ");
				}

				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), html, "autohunt3", null, info));
				pc.setAutocommand(false);
				o.setDelaytime(l1);
				return true;

			}
		} catch (Exception e) {
		
			setAction(null);
			pc.setAutocommand(false);
			ChattingController.toChatting(o, "잘못된 입력입니다. 잠시 후 다시 시도해 주세요.", Lineage.CHATTING_MODE_MESSAGE);
			return true;
		}
		return false;
	}

	private static boolean isShop(object pc, String name) {
		jsn_hunt jh = AutoHuntDatabase.find(pc.getObjectId());
		// 힐링 포션이 없다면.
		if (jh.getPotion() != "" && name.equalsIgnoreCase(jh.getPotion()))
			return true;
		// 용기의 물약이 없다면.
		else if ((pc.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT) && name.equalsIgnoreCase("용기의 물약"))
			return true;
		// 악마의 피가 없다면.
		else if ((pc.getClassType() == Lineage.LINEAGE_CLASS_ROYAL) && name.equalsIgnoreCase("악마의 피"))
			return true;
		// 엘븐 와퍼가 없다면.
		else if (pc.getClassType() == Lineage.LINEAGE_CLASS_ELF && name.equalsIgnoreCase("엘븐 와퍼"))
			return true;
		// 변신 주문서가 없다면.
		else if (name.equalsIgnoreCase("변신 주문서"))
			return true;
		// 강화 초록 물약이 없다면.
		else if (name.equalsIgnoreCase("강화 초록 물약"))
			return true;
		else if (name.equalsIgnoreCase("비취 물약"))
			return true;
		// 순간이동 주문서가 없다면.
		else if (name.equalsIgnoreCase("순간이동 주문서"))
			return true;
		// 화살이 없다면.
		else if (pc.getInventory().getSlot(Lineage.SLOT_WEAPON) != null
				&& pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2().equalsIgnoreCase("bow")
				&& name.equalsIgnoreCase("은 화살"))
			return true;

		return false;
	}

	/**
	 * 구매하려는 아이템을 판매하는 상점 찾기.
	 * 
	 * @param item_name
	 * @return
	 */
	public static ShopInstance findShop(PcInstance pi, String item_name) {
		for (ShopInstance si : NpcSpawnlistDatabase.getBuyShopList()) {
			for (Shop s : si.getNpc().getShop_list()) {
				// 본토의 상점만 검색.
				if (si.getMap() == 87
						&& Util.isDistance(pi.getX(), pi.getY(), pi.getMap(), si.getX(), si.getY(), si.getMap(), 60)
						&& s.getItemName().equalsIgnoreCase(item_name) && s.getItemBress() == 1
						&& s.getAdenType().equalsIgnoreCase("아데나"))
					return si;
			}
		}
		return null;
	}

	/**
	 * 판매하려는 아이템을 판매하는 상점 찾기.
	 * 
	 * @param item_name
	 * @return
	 */
	public static ShopInstance findSellShop(PcInstance pi, String item_name, int en, int bless) {
		
		long objid = Long.valueOf(item_name);
	
		item_name = pi.getInventory().value(objid).getItem().getName();

		for (ShopInstance si : NpcSpawnlistDatabase.getSellShopList()) {
			for (Shop s : si.getNpc().getShop_list()) {
//				System.out.println(s.getItemName().equalsIgnoreCase(item_name) + " | " + item_name + " | " + s.getItemName());
				// 본토의 상점만 검색.
				if (si.getMap() == 87
						&& Util.isDistance(pi.getX(), pi.getY(), pi.getMap(), si.getX(), si.getY(), si.getMap(), 60)
						&& s.getItemName().equalsIgnoreCase(item_name) && (s.getItemBress() == 1||s.getItemBress() == 0||s.getItemBress() == 2)
						&& s.getAdenType().equalsIgnoreCase("아데나") && s.getItemEnLevel() == en
						&& s.getItemBress() == bless)
					return si;
			}
		}
		return null;
	}
	/**
	 * 마을 여부 확인. 2020-12-15 by Jsn_Soft
	 */
	static public boolean isHome(int x, int y, int map) {
		return x >= 32704 && x <= 32767 && y >= 32768 && y <= 32831 && map == 87;
	}

	/**
	 * 마을 여부 확인. 2020-12-15 by Jsn_Soft
	 */
	static public void toHome(PcInstance pc) {
		pc.setHomeMap(87);
		pc.setHomeX(32734); // 32626
		pc.setHomeY(32801); // 32809
	}

	/**
	 * 자동 사냥 시작
	 */
	public static void startHunt(PcInstance pc) {
		pc.setAutoHunt(true);
		pc.AutoHunt();
		pc.setAutoStatus(4);
		AutoHuntThread.getInstance().addAuto(pc);
		pc.checkAutoPotionCancel(pc);
		pc.AutoHuntcancelAutoAttack(pc);
		ChattingController.toChatting(pc, "자동사냥을 시작합니다.", Lineage.CHATTING_MODE_MESSAGE);
	}
	/**
	 * 자동 사냥 시작
	 */
	public static void stopHunt(PcInstance pc) {
		AutoHuntThread.getInstance().removeAuto(pc);
		setAction(null);
		pc.setAutocommand(false);
		
	}

	/**
	 * 체력 포션 갯수. 2018-08-12 by connector12@nate.com
	 */
	static public int getHealingPotionCnt() {
		return Util.random(300, 300);
	}

	/**
	 * 버프물약. 2020-12-17
	 */
	/*static public String getBuff(PcInstance pi) {
		String[] item = { "버프물약" };
		String temp = null;
		int count = 0;

		do {
			if (count++ > maxCount)
				break;

			temp = item[Util.random(0, item.length - 1)];
		} while (findShop(pi, temp) == null);

		return temp;
	}*/

	/**
	 * 버프물약 갯수. 2020-12-17
	 */
	static public int getBuffCnt() {
		return Util.random(5, 10);
	}
	static public int getbichiCnt() {
		return Util.random(3, 5);
	}

	/**
	 * 변신 주문서. 2018-08-12 by connector12@nate.com
	 */
	static public String getScrollPolymorph(PcInstance pi) {
		String[] item = { "변신 주문서" };
		String temp = null;
		int count = 0;

		do {
			if (count++ > maxCount)
				break;

			temp = item[Util.random(0, item.length - 1)];
		} while (findShop(pi, temp) == null);

		return temp;
	}

	/**
	 * 변신 주문서 갯수. 2018-08-12 by connector12@nate.com
	 */
	static public int getScrollPolymorphCnt() {
		return Util.random(5, 10);
	}
	
	

	/**
	 * 화살. 2018-08-12 by connector12@nate.com
	 */
	static public String getArrow(PcInstance pi) {
		String[] item = { "은 화살" };
		String temp = null;
		int count = 0;

		do {
			if (count++ > maxCount)
				break;

			temp = item[Util.random(0, item.length - 1)];
		} while (findShop(pi, temp) == null);

		return temp;
	}

	/**
	 * 화살 갯수. 2018-08-12 by connector12@nate.com
	 */
	static public int getArrowCnt() {
		return Util.random(1000, 2000);
	}

	public static String getMapName(int x, int y, int map) {
		String local = null;

		if (x == 0 && y == 0 && map == 0)
			return "[없음]";

		switch (map) {
		case 0:
			local = "[말하는 섬]";
			break;
		case 1:
			local = "[말하는 섬 던전 1층]";
			break;
		case 2:
			local = "[말하는 섬 던전 2층]";
			break;
		case 4:
			if (x >= 33315 && x <= 33354 && y >= 32430 && y <= 32463) {
				local = "[용의 계곡 삼거리]";
				break;
			} else if (x >= 33248 && x <= 33284 && y >= 32374 && y <= 32413) {
				local = "[용의 계곡 작은 뼈]";
				break;
			} else if (x >= 33374 && x <= 33406 && y >= 32319 && y <= 32357) {
				local = "[용의 계곡 큰 뼈]";
				break;
			} else if (x >= 33224 && x <= 33445 && y >= 32266 && y <= 32483) {
				local = "[용의 계곡]";
				break;
			} else if (x >= 33485 && x <= 33781 && y >= 32209 && y <= 32470) {
				local = "[화룡의 둥지]";
				break;
			} else if (x >= 33832 && x <= 34039 && y >= 32341 && y <= 32649) {
				local = "[좀비 엘모어 밭]";
				break;
			} else if (x >= 32716 && x <= 32980 && y >= 33075 && y <= 33391) {
				local = "[사막]";
				break;
			} else if (x >= 32833 && x <= 32975 && y >= 32875 && y <= 32957) {
				local = "[글루디오]";
				break;
			} else if (x >= 32707 && x <= 32932 && y >= 32611 && y <= 32758) {
				local = "[카오틱 신전]";
				break;
			} else if (x >= 33995 && x <= 34091 && y >= 32972 && y <= 33045) {
				local = "[린드비오르의 둥지]";
				break;
			} else {
				local = "[아덴월드]";
				break;
			}
		case 5:
			local = "[몽환의 섬]";
			break;
		case 7:
			local = "[본토 던전 1층]";
			break;
		case 8:
			local = "[본토 던전 2층]";
			break;
		case 9:
			local = "[본토 던전 3층]";
			break;
		case 10:
			local = "[본토 던전 4층]";
			break;
		case 11:
			local = "[본토 던전 5층]";
			break;
		case 12:
			local = "[본토 던전 6층]";
			break;
		case 13:
			local = "[본토 던전 7층]";
			break;
		case 14:
			local = "[지하 수로]";
			break;
		case 15:
			local = "[켄트성 내성]";
			break;
		case 17:
			local = "[네루파 동굴]";
			break;
		case 18:
			local = "[듀펠케넌 던전]";
			break;
		case 19:
			local = "[요정의 숲 던전 1층]";
			break;
		case 20:
			local = "[요정의 숲 던전 2층]";
			break;
		case 21:
			local = "[요정의 숲 던전 3층]";
			break;
		case 23:
			local = "[윈다우드 던전 1층]";
			break;
		case 24:
			local = "[윈다우드 던전 2층]";
			break;
		case 25:
			local = "[수련 던전 1층]";
			break;
		case 26:
			local = "[수련 던전 2층]";
			break;
		case 27:
			local = "[수련 던전 3층]";
			break;
		case 28:
			local = "[수련 던전 4층]";
			break;
		case 30:
			local = "[용의 계곡 던전 1층]";
			break;
		case 31:
			local = "[용의 계곡 던전 2층]";
			break;
		case 32:
			local = "[용의 계곡 던전 3층]";
			break;
		case 33:
			local = "[용의 계곡 던전 4층]";
			break;
		case 35:
			local = "[용의 계곡 던전 5층]";
			break;
		case 36:
			local = "[용의 계곡 던전 6층]";
			break;
		case 37:
			local = "[용의 계곡 던전 7층]";
			break;
		case 43:
		case 44:
		case 45:
		case 46:
		case 47:
		case 48:
		case 49:
		case 50:
			local = "[개미 던전 1층]";
			break;
		case 51:
			local = "[개미 던전 2층]";
			break;
		case 53:
			local = "[기란 감옥 1층]";
			break;
		case 54:
			local = "[기란 감옥 2층]";
			break;
		case 55:
			local = "[기란 감옥 3층]";
			break;
		case 56:
			local = "[기란 감옥 4층]";
			break;
		case 59:
			local = "[에바 던전 1층]";
			break;
		case 60:
			local = "[에바 던전 2층]";
			break;
		case 61:
			local = "[에바 던전 3층]";
			break;
		case 62:
			local = "[에바의 왕국]";
			break;
		case 63:
			local = "[에바 던전 4층]";
			break;
		case 64:
			local = "[하이네 성 내성]";
			break;
		case 65:
			local = "[파푸리온의 둥지]";
			break;
		case 67:
			local = "[발라카스의 둥지]";
			break;
		case 68:
			local = "[노래하는 섬]";
			break;
		case 69:
			local = "[숨겨진 계곡]";
			break;
		case 70:
			local = "[잊혀진 섬]";
			break;
		case 72:
			local = "[얼음 던전 1층]";
			break;
		case 73:
			local = "[얼음 던전 2층]";
			break;
		case 74:
			local = "[얼음 던전 3층]";
			break;
		case 75:
			local = "[상아탑 1층]";
			break;
		case 76:
			local = "[상아탑 2층]";
			break;
		case 77:
			local = "[상아탑 3층]";
			break;
		case 78:
			local = "[상아탑 4층]";
			break;
		case 79:
			local = "[상아탑 5층]";
			break;
		case 80:
			local = "[상아탑 6층]";
			break;
		case 81:
			local = "[상아탑 7층]";
			break;
		case 82:
			local = "[상아탑 8층]";
			break;
		case 99:
			local = "[운영자의 방]";
			break;
		case 101:
			local = "[오만의 탑 1층]";
			break;
		case 102:
			local = "[오만의 탑 2층]";
			break;
		case 103:
			local = "[오만의 탑 3층]";
			break;
		case 104:
			local = "[오만의 탑 4층]";
			break;
		case 105:
			local = "[오만의 탑 5층]";
			break;
		case 106:
			local = "[오만의 탑 6층]";
			break;
		case 107:
			local = "[오만의 탑 7층]";
			break;
		case 108:
			local = "[오만의 탑 8층]";
			break;
		case 109:
			local = "[오만의 탑 9층]";
			break;
		case 110:
			local = "[오만의 탑 10층]";
			break;
		case 200:
			local = "[오만의 탑 정상]";
			break;
		case 350:
			local = "[기란 시장]";
			break;
		case 509:
			local = "[결투장]";
			break;
		case 666:
			local = "[지옥]";
			break;
		case 777:
			local = "[버려진 땅]";
			break;
		case 780:
			local = "[테베라스 사막]";
			break;
		case 781:
			local = "[테베 피라미드 내부]";
			break;
		case 782:
			local = "[테베 오시리스의 제단]";
			break;
		case 800:
			local = "[유저 상점]";
			break;
		case 807:
			local = "[리뉴얼 본토 던전 1층]";
			break;
		case 808:
			local = "[리뉴얼 본토 던전 2층]";
			break;
		case 809:
			local = "[리뉴얼 본토 던전 3층]";
			break;
		case 810:
			local = "[리뉴얼 본토 던전 4층]";
			break;
		case 811:
			local = "[리뉴얼 본토 던전 5층]";
			break;
		case 812:
			local = "[리뉴얼 본토 던전 6층]";
			break;
		case 813:
			local = "[리뉴얼 본토 던전 7층]";
			break;
		case 1400:
			local = "[이벤트 맵]";
			break;
		case 5167:
			local = "[악마왕의 영토]";
			break;
		default:
			local = "[없음]";
			break;
		}

		return local;
	}

	/**
	 * gfx에 해당하는action값에 프레임값을 리턴함. PC를 제외한 모든 객체가 사용중
	 */
	static public int getGfxFrameTime(object o, int gfx, int action) {
		SpriteFrame spriteFrame = SpriteFrameDatabase.getList().get(gfx);

		if (spriteFrame != null) {
			double frame = 0;
			Integer gfxFrame = spriteFrame.getList().get(action);

			if (gfxFrame != null)
				frame = gfxFrame.intValue();
			else
				return 1000;

			// 일반적인 촐기 용기 안한 상태
			if (o.getSpeed() == 0 && !o.isBrave())
				frame *= 42;
			// 촐기 또는 용기 상태
			else if ((o.getSpeed() == 1 && !o.isBrave()) || (o.getSpeed() == 0 && o.isBrave()))
				frame *= 31.5;
			// 촐기 용기 둘다
			else if (o.getSpeed() == 1 && o.isBrave())
				frame *= 23.5;
			// 슬로우 걸렸을 시, 촐기 용기 안한 상태
			else if (o.getSpeed() == 2 && !o.isBrave())
				frame *= 81;
			// 슬로우 걸렸을 시, 촐기 안한 상태
			else if (o.getSpeed() == 2 && o.isBrave())
				frame *= 61;

			frame *= 1;

			frame /= 40;

			return (int) (frame);
		}
		// 해당하는 모드가 없을경우 1초로 정의
		return 1000;
	}

}