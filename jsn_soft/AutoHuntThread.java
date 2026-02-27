package jsn_soft;

import java.util.ArrayList;
import java.util.Iterator;

import lineage.bean.database.Item;
import lineage.bean.database.Poly;
import lineage.bean.database.Shop;
import lineage.bean.database.Skill;
import lineage.bean.lineage.Buff;
import lineage.database.ItemDatabase;
import lineage.database.PolyDatabase;
import lineage.database.ServerDatabase;
import lineage.database.SkillDatabase;
import lineage.database.TeleportHomeDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectLock;
import lineage.network.packet.server.S_ObjectMoving;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.thread.AiThread;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.LocationController;
import lineage.world.controller.SkillController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.ShopInstance;
import lineage.world.object.item.Arrow;
import lineage.world.object.item.potion.HastePotion;
import lineage.world.object.magic.AdvanceSpirit;
import lineage.world.object.magic.CursePoison;
import lineage.world.object.magic.EnchantDexterity;
import lineage.world.object.magic.EnchantMighty;
import lineage.world.object.magic.EnergyBolt;
import lineage.world.object.magic.Haste;
import lineage.world.object.magic.HolyWalk;
import lineage.world.object.magic.ShapeChange;
import lineage.world.object.magic.Shield;
import lineage.world.object.magic.item.Bravery;
import lineage.world.object.magic.item.Wafer;
import lineage.world.object.magic.monster.CurseGhast;
import lineage.world.object.magic.monster.CurseGhoul;
import lineage.world.object.monster.만드라고라;

public class AutoHuntThread implements Runnable {
	public static AutoHuntThread _instance;
	// 쓰레드동작 여부
	static private boolean running;
	// 타이머 각 프레임별 타임라인값.

	/** 자동 사냥 PC 리스트 */
	private static ArrayList<PcInstance> _pcList;

	private final long _sleepTime = 10L;

	public final int AUTO_STATUS_NONE = -1;
	public final int AUTO_STATUS_SETTING = 0;
	public final int AUTO_STATUS_WALK = 1;
	public final int AUTO_STATUS_ATTACK = 2;
	public final int AUTO_STATUS_DEAD = 3;
	public final int AUTO_STATUS_MOVE_SHOP = 4;
	public final int AUTO_STATUS_MPREGEON = 5;

	public static AutoHuntThread getInstance() {
		if (_instance == null) {
			_instance = new AutoHuntThread();
		}
		return _instance;
	}

	/**
	 * 초기화 처리 함수.
	 */
	static public void init() {
		TimeLine.start("자동사냥..");

		_instance = new AutoHuntThread();
		_pcList = new ArrayList<PcInstance>();

		TimeLine.end();
	}

	/**
	 * 쓰레드 활성화 함수.
	 */
	static public void start() {
		running = true;
		Thread t = new Thread(_instance);
		t.setName(AiThread.class.toString());
		t.start();
	}

	/**
	 * 종료 함수
	 */
	static public void close() {
		running = false;
		_instance = null;
	}

	@Override
	public void run() {
		while (running) {
			try {
				for (int i = 0; i < _pcList.size(); i++) {
					PcInstance current_pc = _pcList.get(i);
					try {
						getSource(current_pc);
					} catch (Exception e) {
						if (current_pc != null) {
							if (current_pc.getClient() == null) {
								System.out.println(
										String.format("%s 커넥션 정보 찾을 수 없음. 강제로 자동 사냥을 종료합니다.", current_pc.getName()));
								removeAuto(current_pc);
								World.removePc(current_pc);
								World.remove(current_pc);
								e.printStackTrace();
								return;
							}
							System.out.println(String.format("예외 유저 정보 --> 아이디 : %s, 오브젝트 아이디 %d", current_pc.getName(),
									current_pc.getObjectId()));
							e.printStackTrace();
							returnScroll(current_pc);
							this.removeAuto(current_pc);
						} else {
							System.out.println("예외 유저 정보를 찾을 수 없음.");
							e.printStackTrace();
						}
					} finally {
						long sleep = _sleepTime;
						if (current_pc != null) {
							if (current_pc.getAutoStatus() == AUTO_STATUS_WALK) {
								sleep = 100L;
							} else if (current_pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
								sleep = 20L;
							}
						}
						try {
							Thread.sleep(sleep);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void getSource(PcInstance pc) {

		if (pc == null) {
			removeAuto(pc);
			World.removePc(pc);
			World.remove(pc);
			return;
		}

		jsn_hunt jh = AutoHuntDatabase.find(pc.getObjectId());

		if (!Lineage.autohunt_onoff) {
			ChattingController.toChatting(pc, "\\fT피크 타임 시작!", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fT[자동사냥] 종료 / 23시까지 자동사냥을 하실수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			returnScroll(pc);
			removeAuto(pc);
			return;
		}

		if (pc.getAutoTime() <= 0) {
			ChattingController.toChatting(pc, "자동사냥 남은 시간이 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			returnScroll(pc);
			removeAuto(pc);
			return;
		}

		if (jh.getX() == 0 && jh.getY() == 0) {
			ChattingController.toChatting(pc, "사냥터 설정을 다시 해주세요.", Lineage.CHATTING_MODE_MESSAGE);
			returnScroll(pc);
			removeAuto(pc);
			return;
		}

		if (jh.getPotion() != null && (jh.getPotion().equalsIgnoreCase("") || jh.getPotion().equalsIgnoreCase(" "))) {
			ChattingController.toChatting(pc, "물약 설정을 다시 해주세요.", Lineage.CHATTING_MODE_MESSAGE);
			returnScroll(pc);
			removeAuto(pc);
			return;
		}

		if (!pc.isAutoHunt()) {
			returnScroll(pc);
			removeAuto(pc);
			return;
		}

		if (pc.getInventory().getSlot(Lineage.SLOT_WEAPON) == null) {
			returnScroll(pc);
			removeAuto(pc);
			ChattingController.toChatting(pc, "착용된 무기가 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (!pc.getInventory().isWeightPercent(82)) {
			returnScroll(pc);
			removeAuto(pc);
			ChattingController.toChatting(pc, "무게가 무거워 자동사냥이 종료됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (pc.getAutoStatus() != AUTO_STATUS_WALK && pc.getAutoStatus() != AUTO_STATUS_ATTACK) {
			pc.setAutoStatus(AUTO_STATUS_WALK);
		}

		if ((pc.getAutoStatus() != AUTO_STATUS_MOVE_SHOP && pc.getAutoStatus() != AUTO_STATUS_SETTING)
				&& isShop(pc, pc.isReShop())) {
			pc.getAutoTargetList().clear();
			pc.setAutoTarget(null);
			pc.setAutoStatus(AUTO_STATUS_MOVE_SHOP);
		}

		if (pc.isDead()) {
			restart(pc);
			return;
		}
		// ChattingController.toChatting(pc, "자동사냥 상태 : " + pc.getAutoStatus(),
		// Lineage.CHATTING_MODE_MESSAGE);
		// ChattingController.toChatting(pc, "타겟 정보: " + (pc.getAutoTarget() == null ?
		// "없음"
		// : pc.getAutoTarget().getName())
		// , Lineage.CHATTING_MODE_NORMAL);
		switch (pc.getAutoStatus()) {
			case AUTO_STATUS_MOVE_SHOP:
				toShopItem(pc);
				toSellItem(pc);
				break;
			case AUTO_STATUS_WALK:
				searchTarget(pc, false);
				if (pc.getAutoTargetList().size() > 0) {
					pc.setAutoStatus(AUTO_STATUS_ATTACK);
				}
				noTargetTeleport(pc);
				toRandomWalk(pc);
				toReturnScroll(pc);
				break;
			case AUTO_STATUS_ATTACK:
				if (pc.getAutoTargetList().size() == 0) {
					pc.getAutoTargetList().clear();
					pc.setAutoTarget(null);
					pc.setAutoStatus(AUTO_STATUS_WALK);
				}
				toAttackMonster(pc);
				toReturnScroll(pc);
				break;
			case AUTO_STATUS_MPREGEON:
				break;
			default:
				break;
		}
		if (pc.isDead())
			return;
		toUseItem(pc);
		toPolyScroll(pc);
		pc.setAutocommand(true);
	}

	private boolean isShop(PcInstance pc, boolean shop) {
		jsn_hunt jh = AutoHuntDatabase.find(pc.getObjectId());
		// 힐링 포션이 없다면.
		if (jh.getPotion() != "" && pc.getInventory().find(jh.getPotion()) == null)
			return true;
		// 용기의 물약이 없다면.
		else if ((pc.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT) && pc.getInventory().find("용기의 물약") == null)
			return true;
		// 악마의 피가 없다면.
		else if ((pc.getClassType() == Lineage.LINEAGE_CLASS_ROYAL) && pc.getInventory().find("악마의 피") == null)
			return true;
		// 엘븐 와퍼가 없다면.
		else if (pc.getClassType() == Lineage.LINEAGE_CLASS_ELF && pc.getInventory().find("엘븐 와퍼") == null)
			return true;
		// 변신주문서가 없다면.
		else if (pc.getInventory().find("변신 주문서") == null)
			return true;
		else if (pc.getInventory().find("비취 물약") == null)
			return true;
		// 강화 초록 물약이 없다면.
		else if (pc.getInventory().find("강화 초록 물약") == null)
			return true;
		// 순간이동 주문서가 없다면.
		else if (pc.getInventory().find("순간이동 주문서") == null)
			return true;
		// 화살이 없다면.
		else if (pc.getInventory().getSlot(Lineage.SLOT_WEAPON) != null
				&& pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2().equalsIgnoreCase("bow")
				&& pc.getInventory().find(Arrow.class) == null)
			return true;
		else if (pc.isSellItem()) {
			int num = 0;
			for (Item i : pc.sell_List) {
				for (ItemInstance it : pc.getInventory().getList()) {
					if (it.getItem() == i && !it.isEquipped() && it.getEnLevel() == 0
							&& (it.getBress() == 1 || it.getBress() == 0 || it.getBress() == 2)) {
						if (shop)
							return true;
						num += 1;
					}

				}

			}
			// System.out.println(num);
			if (num >= 5 && !shop)
				return true;
		}
		return false;
	}

	private void toShopItem(PcInstance pc) {
		pc.clearAstarList2();
		jsn_hunt jh = AutoHuntDatabase.find(pc.getObjectId());
		if (jh.getPotion() != "" && pc.getInventory().find(jh.getPotion()) == null) {
			toShopWalk(pc, jh.getPotion(), AutoHuntController.getHealingPotionCnt(), true, null);
			return;
		} else if (pc.getInventory().find("강화 초록 물약") == null) {
			toShopWalk(pc, "강화 초록 물약", AutoHuntController.getBuffCnt(), true, null);
			return;
		} else if (pc.getClassType() == Lineage.LINEAGE_CLASS_ROYAL && pc.getInventory().find("악마의 피") == null) {
			toShopWalk(pc, "악마의 피", AutoHuntController.getBuffCnt(), true, null);
			return;
		} else if (pc.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT && pc.getInventory().find("용기의 물약") == null) {
			toShopWalk(pc, "용기의 물약", AutoHuntController.getBuffCnt(), true, null);
			return;
		} else if (pc.getClassType() == Lineage.LINEAGE_CLASS_ELF && pc.getInventory().find("엘븐 와퍼") == null) {
			toShopWalk(pc, "엘븐 와퍼", AutoHuntController.getBuffCnt(), true, null);
			return;
		} else if (pc.getInventory().find("변신 주문서") == null) {
			toShopWalk(pc, "변신 주문서", AutoHuntController.getScrollPolymorphCnt(), true, null);
			return;
		} else if (pc.getInventory().find("비취 물약") == null) {
			toShopWalk(pc, "비취 물약", AutoHuntController.getbichiCnt(), true, null);
			return;
		} else if (pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2().equalsIgnoreCase("bow")
				&& pc.getInventory().find(Arrow.class) == null) {
			toShopWalk(pc, AutoHuntController.getArrow(pc), AutoHuntController.getArrowCnt(), true, null);
			return;
		} else if (pc.getInventory().find("순간이동 주문서") == null) {
			toShopWalk(pc, "순간이동 주문서", 100, true, null);
			return;
		}
	}

	private void toSellItem(PcInstance pc) {
		for (Iterator<Item> itr = pc.sell_List.iterator(); itr.hasNext();) {
			Item i = itr.next();

			for (ItemInstance it : pc.getInventory().getList()) {
				if (it.getItem() == i && !it.isEquipped() && it.getEnLevel() == 0
						&& (it.getBress() == 1 || it.getBress() == 0 || it.getBress() == 2)) {
					// 오브젝트 아이디는 원래 long 데이터타입의 숫자입니당
					// 그래서 String.valueOf() << 어떠한 데이터타입이던 글자로 바꾸어주는 매서드
					// 위의 것을 사용해서 숫자를 글자로 데이터타입을 바꿔서 들어가게했어요 아하
					toShopWalk(pc, String.valueOf(it.getObjectId()), it.getCount(), false, itr);
				}
			}
		}
	}

	private void toPolyScroll(PcInstance pc) {

		if (pc.getGfx() != pc.getClassGfx())
			return;

		ItemInstance poly = pc.getInventory().find("변신 주문서");

		if (poly != null && poly.getCount() > 0) {
			// 변신.
			Poly p = PolyDatabase.getPolyName(getPolymorph(pc));

			if (p != null) {
				// 변신
				ShapeChange.init(pc, pc, p, 1200, poly.getBress());
				ChattingController.toChatting(pc, "변신 완료: " + p.getName(), Lineage.CHATTING_MODE_MESSAGE);
				// 수량 변경
				pc.getInventory().count(poly, poly.getCount() - 1, true);
			}
		}
	}

	/**
	 * 클레스별로 변신할 이름 리턴.
	 * 
	 * @return
	 */
	private String getPolymorph(PcInstance pc) {
		if (pc.getFast_poly() != null) {
			if (!(pc.getFast_poly().equalsIgnoreCase("Dragon Slayer")
					|| pc.getFast_poly().equalsIgnoreCase("Platinum death knight")
					|| pc.getFast_poly().equalsIgnoreCase("Doppelganger")
					|| pc.getFast_poly().equalsIgnoreCase("girtas")
					|| pc.getFast_poly().equalsIgnoreCase("Platinum Baphomet")
					|| pc.getFast_poly().equalsIgnoreCase("zillian of moon")
					|| pc.getFast_poly().equalsIgnoreCase("Flame death knight")
					|| pc.getFast_poly().equalsIgnoreCase("왕자 랭커") || pc.getFast_poly().equalsIgnoreCase("공주 랭커")
					|| pc.getFast_poly().equalsIgnoreCase("남자기사 랭커") || pc.getFast_poly().equalsIgnoreCase("여자기사 랭커")
					|| pc.getFast_poly().equalsIgnoreCase("남자요정 랭커") || pc.getFast_poly().equalsIgnoreCase("여자요정 랭커")
					|| pc.getFast_poly().equalsIgnoreCase("남자법사 랭커") || pc.getFast_poly().equalsIgnoreCase("여자법사 랭커")))
				return pc.getFast_poly();
		}
		return null;
	}

	private void toReturnScroll(PcInstance pc) {
		int percent = (int) Math.round(((double) pc.getNowHp() / (double) pc.getMaxHp()) * 100);
		if (percent <= 30) {
			if (pc.isLock())
				return;
			returnScroll(pc);
		}
	}

	private void returnScroll(PcInstance pc) {
		// 이미 마을일경우 무시.
		if (AutoHuntController.isHome(pc.getX(), pc.getY(), pc.getMap()))
			return;

		pc.clearAstarList2();

		AutoHuntController.toHome(pc);
		pc.toTeleport(pc.getHomeX(), pc.getHomeY(), pc.getHomeMap(), false);
		ChattingController.toChatting(pc, "상점으로 이동합니다.", Lineage.CHATTING_MODE_MESSAGE);
		pc.setAutoTarget(null);
		pc.getAutoTargetList().clear();
		pc.clearAstarList2();
	}

	private void toUseItem(PcInstance pc) {
		toBuffPotion(pc);
		toBuffskill(pc);
		toHealingPotion(pc);
	}

	// 이걸 계속 반복을하는데
	private void toAttackMonster(PcInstance pc) {
		boolean bow = pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2().equalsIgnoreCase("bow");
		int atkRange = bow ? Lineage.SEARCH_LOCATIONRANGE - 2 : 1;
		long time = System.currentTimeMillis();
		try {
			// 때려도되는시작인지 체크
			if (!isAutoAttackTime(pc))
				return;
			// 마을인지 체크
			if (AutoHuntController.isHome(pc.getX(), pc.getY(), pc.getMap())) {
				tagertClear(pc);
				pc.setAutoTarget(null);
				return;
			}
			// 대상 선정 및 유효성 확인 (AutoHuntCommonLogic 활용)
			object target = pc.getAutoTarget();

			if (target != null && !AutoHuntCommonLogic.isAttackable(pc, target, atkRange)) {
				pc.removeAutoTargetList(target);
				pc.setAutoTarget(null);
				return;
			}
			// 때릴애가 없는지 체크
			if (pc.getAutoTarget() == null)
				pc.setAutoTarget(getTarget(pc));

			if (pc.getAutoTarget() == null)
				searchTarget(pc, true);

			if (pc.getAutoTarget() == null) {
				pc.setAutoStatus(AUTO_STATUS_WALK);
				return;
			} else {
				target = pc.getAutoTarget();

				if (time - pc.getAutoTimeMove() > 1000 * 10) {
					pc.removeAutoTargetList(target);
					pc.setAutoTarget(null);
					searchTarget(pc, true);
				}
				// 객체 이동 - 여기
				// 객체 거리 확인
				// 거리를 확인 떄릴 수 없는 위치면
				if (isDistance(pc.getX(), pc.getY(), pc.getMap(), target.getX(), target.getY(), target.getMap(),
						atkRange) && Util.isAreaAttack(pc, target)) {

					if (pc.getClassType() == Lineage.LINEAGE_CLASS_WIZARD) {

						int mp = SkillDatabase.find(45).getMpConsume();
						if (mp > 0 && pc.getNowMp() > mp && pc.getNowMp() > 90) {
							if (SkillController.find(pc) != null) {
								for (Skill s : SkillController.find(pc)) {
									if (s.getName().equalsIgnoreCase("이럽션")) {
										object o = pc.findInsideList(target.getObjectId());
										if (o != null && Util.isDistance(pc, o, 3)
												&& SkillController.isMagic(pc, SkillDatabase.find(45), true)) {
											EnergyBolt.toBuff(pc, o, SkillDatabase.find(45),
													Lineage.GFX_MODE_SPELL_DIRECTION,
													SkillDatabase.find(45).getCastGfx(), 0);
											return;
										}
									}
								}
							}
						}
					}
					// 공격 시전했는지 확인용.
					// if (toSkillAttack(pc, pc.getAutoTarget()))
					// return;
					// 물리공격 범위내로 잇을경우 처리.
					pc.toAttack(target, 0, 0, bow, pc.getGfxMode() + Lineage.GFX_MODE_ATTACK, 0, false, 0);
					return;
					// 이동이 가능한 곳에 있는지 확인
				} else if (toMoving(pc, target.getX(), target.getY(), 0, true, true)) {
					toMoving(pc, target.getX(), target.getY(), 0, true, false);
					pc.clearAstarList2();
					return;
					// 이동을 못하면 다시검색 이렇게 진행되요
				} else {
					pc.removeAutoTargetList(target);
					pc.setAutoTarget(null);
					searchTarget(pc, true);
				}
			}
		} catch (Exception e) {
			pc.removeAutoTargetList(pc.getAutoTarget());
			pc.setAutoTarget(null);
			pc.clearAstarList2();
			e.printStackTrace();
		}
	}

	public boolean isAttack(PcInstance pc, object cha) {
		try {
			if (cha == null)
				return false;

			if (World.isSafetyZone(cha.getX(), cha.getY(), cha.getMap()))
				return false;
			if (cha.isDead())
				return false;

			if (cha.isInvis())
				return false;
			if (!isDistance(pc.getX(), pc.getY(), pc.getMap(), cha.getX(), cha.getY(), cha.getMap(), 16))
				return false;

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private object getTarget(PcInstance pc) {
		object realTarget = null;

		try {
			// for (int i = 0; i < pc.getAutoTargetList().size(); i++) {
			// object target = pc.getAutoTargetList().get(i);
			for (int i = 0; i < 20; i++) {
				object target = pc.getAutoTargetList().get(i);
				if (target == null)
					continue;

				if (target.isDead() || AutoHuntCommonLogic.isSummonOrPet(target, pc)) {
					pc.removeAutoTargetList(target);
					continue;
				}
				if (!Util.isAreaAttack(pc, target)) {
					pc.removeAutoTargetList(target);
					continue;
				}

				if (realTarget == null) {
					realTarget = target;
					break;
				} else if (!target.isDead() && getDistance(pc.getX(), pc.getY(), target.getX(),
						target.getY()) < getDistance(pc.getX(), pc.getY(), realTarget.getX(), realTarget.getY())) {
					realTarget = target;
					break;
				}
			}

			return realTarget;
		} catch (Exception e) {
			e.printStackTrace();
			pc.getAutoTargetList().clear();
			pc.setAutoTarget(null);
			pc.clearAstarList2();
			return realTarget;
		}
	}

	private boolean isDistance(int x, int y, int m, int tx, int ty, int tm, int loc) {
		int distance = getDistance(x, y, tx, ty);
		if (loc < distance)
			return false;
		if (m != tm)
			return false;
		return true;
	}

	private int getDistance(int x, int y, int tx, int ty) {
		long dx = tx - x;
		long dy = ty - y;
		return (int) Math.sqrt(dx * dx + dy * dy);
	}

	private void searchTarget(PcInstance pc, boolean reSearch) {
		if (AutoHuntController.isHome(pc.getX(), pc.getY(), pc.getMap()))
			return;

		if (pc.getAutoTarget() != null) {
			return;
		}
		tagertClear(pc);
		for (object obj : pc.getInsideList()) {
			if (!AutoHuntCommonLogic.isAttackable(pc, obj, 16))
				continue;

			if (obj instanceof MonsterInstance) {
				MonsterInstance mon = (MonsterInstance) obj;
				int loc = Util.getDistance(pc, mon);
				pc.addAutoTargetList(mon, loc);
				if (pc.getAutoTargetList().size() >= 10)
					break;
			}
		}
	}

	public void checkTarget(PcInstance pc) {
		try {
			object target = pc.getAutoTarget();
			if (target == null || target.getMap() != pc.getMap() || target.isDead() || target.getNowHp() <= 0
					|| (target.isInvis() && !pc.getAutoTargetList().containsValue(target))) {
				if (target != null) {
					tagertClear(pc);
				}

				if (pc.getAutoTargetList() != null) {
					pc.setAutoTarget(target);
					return;
				}
			} else {
				searchTarget(pc, false);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void tagertClear(PcInstance pc) {
		object target = pc.getAutoTarget();
		if (target == null) {
			return;
		}
		pc.getAutoTargetList().values().remove(target);
		pc.clearAstarList2();
	}

	private void toShopWalk(PcInstance pc, String item_name, long count, boolean buy, Iterator<Item> itr) {
		if (!isAutoMoveTime(pc))
			return;
		pc.getAutoTargetList().clear();
		pc.setAutoTarget(null);
		tagertClear(pc);
		pc.clearAstarList2();
		switch (pc.step2) {
			case 0: // 이동
				// npc 찾기.
				// System.out.println(item_name);
				if (pc.shopTemp2 == null) {
					returnScroll(pc);
					if (buy)
						pc.shopTemp2 = AutoHuntController.findShop(pc, item_name);
					else {
						int value = 0;
						ItemInstance item = null;
						try {
							value = Integer.valueOf(item_name);
						} catch (Exception e) {
							value = 0;
						}
						if (value != 0)
							item = pc.getInventory().value(value);
						int en = 0;
						int bless = 1;
						if (item != null) {
							en = item.getEnLevel();
							bless = item.getBress();
						}
						// 여기서는 이제 글자 타입으로 들어가게되서
						pc.shopTemp2 = AutoHuntController.findSellShop(pc, item_name, en, bless);

						if (pc.shopTemp2 == null && item != null && itr != null) {
							ChattingController.toChatting(pc,
									"판매 불가능한 아이템 발견. 판매목록에서 제거합니다. [" + item.getItem().getName() + "]");
							itr.remove();
						}
					}
					if (pc.shopTemp2 == null)
						pc.step2 = 5;
				} else {
					if (Util.isDistance(pc, pc.shopTemp2, Util.random(1, 5)))
						pc.step2 = 1;
					else
						toMoving(pc, pc.shopTemp2.getX(), pc.shopTemp2.getY(), 0, true, false);
					pc.clearAstarList2();
				}
				break;
			case 1: // npc 클릭
				if (pc.shopTemp2 == null)
					pc.step2 = 5;
				else
					pc.shopTemp2.toTalk(pc, null);

				pc.step2 = 2;
				break;
			case 2: // buy 클릭
				if (buy)
					pc.shopTemp2.toTalk(pc, "buy", null, null);
				else
					pc.shopTemp2.toTalk(pc, "sell", null, null);
				pc.step2 = 3;
				break;
			case 3: // 아이템선택 구매클릭
				ItemInstance aden = pc.getInventory().find("아데나");
				Item buyitem = ItemDatabase.find(item_name);
				ItemInstance item = pc.getInventory().find(item_name);

				// 이것은 item_name << 이 이름 자체가 글자로 되어있기때문에
				// Long.valueOf(item_name) << 바꾸면 에러가 일어나요
				// 왜냐면 글자로 된걸 억지로 바꾸려고했기 때문이에요
				// 근데 판매는 무조건 숫자가 오게 했는데
				// 어떻게 해야 구분을할 수 있을까? 할때는
				// 먼저 사용할 변수를 null 로 두고
				ItemInstance sellitem = null;// 오케이 그러면 그 오류가 안나게 코드를 짜는방식이에용 이거가 한번 공부해봐용
				String sellitemname = null;

				// sell일때 즉 숫자로 올 경우에만
				if (!buy) {
					// 숫자로 바꿔줘라~ 라는 의미에용 이런식으로 오류가 나거나 버그가일어나면
					// null 후에 조건에맞게 지정을 해줘도 됩니당 아하이게 가장 중요한 부분이네요 넵
					// 코드를 짜다보면 예상치 못한 부분에서 에러가 일어나는데 대부분 변수문제니까
					// 인스텐스 봐도 다 int a; 혹은 int a = 0; 이런식으로 초기화를 하고 시작을 하는이유도
					// 여기에 있어용 아
					// 그럼 고생하셨습니당 !네 인제 진짜 끝낫겟죠 ㅋ 자사 ㅋㅋ 그만 보고 싶네요 ㅋㅋ 자사는
					// 월요일 수업때 이부분정확히 해봐요 ㅋ 넹 이런부분들 공부좀 하고싶네요 갑자기 ㅋ
					// 가장 중요하구 실용적인 부분이죵 월요일에 같이 해봐용네 ㅋㅋ 수업 준비좀 잘부탁드려여 넹!
					sellitem = pc.getInventory().value(Long.valueOf(item_name));
					sellitemname = sellitem.getItem().getName();
				}

				Shop s = null;

				if (buy) {
					s = pc.shopTemp2.getNpc().findShopItemId(item_name, 1);
				} else {
					s = pc.shopTemp2.getNpc().findShopItemId(sellitemname, sellitem.getBress(), sellitem.getEnLevel());
				}

				if (s == null) {
					pc.step2 = 5;
					break;
				}
				ShopInstance shop = pc.shopTemp2;
				int price = 0; // 상점일단 완료

				if (buy) {
					price = (int) ((s.getPrice() != 0 ? shop.getTaxPrice(s.getPrice(), false)
							: shop.getTaxPrice(buyitem.getShopPrice(), false)) * count);
				} else {
					price = (int) (shop.getPrice(null, sellitem) * count);
				}
				// 잠시만요
				if (s != null && buy && (aden == null || (aden != null && aden.getCount() < price))) {
					ChattingController.toChatting(pc, "아데나가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
					removeAuto(pc);
					pc.step2 = 5;
					break;
				}

				if (buy) {
					// System.out.println(price);
					pc.getInventory().count(aden, aden.getCount() - price, true);

					if (item != null && item.getItem().isPiles())
						pc.getInventory().count(item, item.getCount() + count, true);
					else {
						ItemInstance newitem = ItemDatabase.newInstance(buyitem);
						if (newitem.getObjectId() == 0)
							newitem.setObjectId(ServerDatabase.nextItemObjId());
						newitem.setCount(count);
						pc.getInventory().append(newitem, true);
					}

				} else {
					if (sellitem != null) {
						pc.getInventory().count(sellitem, sellitem.getCount() - count, true);

						if (aden != null)
							pc.getInventory().count(aden, aden.getCount() + price, true);
						else {
							ItemInstance newaden = ItemDatabase.newInstance(aden);
							if (newaden.getObjectId() == 0)
								newaden.setObjectId(ServerDatabase.nextItemObjId());
							newaden.setCount(price);
							pc.getInventory().append(newaden, true);
						}
					}
				}

				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), pc, " "));
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), pc, ""));
				if (buy)
					ChattingController.toChatting(pc, "아이템 구매: " + item_name + "(" + count + ")",
							Lineage.CHATTING_MODE_MESSAGE);
				else
					ChattingController.toChatting(pc, "아이템 판매: " + sellitemname + "(" + count + ")",
							Lineage.CHATTING_MODE_MESSAGE);
				if (isShop(pc, !pc.isReShop()))
					pc.setReShop(!pc.isReShop());
				else if (!isShop(pc, pc.isReShop()))
					pc.setReShop(!pc.isReShop());
				pc.step2 = 5;
				break;
			case 4:
			case 5:
				// 초기화.
				pc.step2 = 0;
				pc.shopTemp2 = null;
				// 기본 모드로 변경.
				pc.setAutoStatus(AUTO_STATUS_WALK);
				break;
		}
	}

	private boolean isAutoAttackTime(PcInstance pc) {
		long temp = System.currentTimeMillis() - pc.getAutoTimeAttack(); // 지금 시간에서 기존시간을 뺌
		if (pc.isLock()) {
			return false;
		}
		long interval = (long) (AutoHuntController.getGfxFrameTime(pc, pc.getGfx(),
				pc.getGfxMode() + Lineage.GFX_MODE_ATTACK) * Lineage.speed_check_attack_frame_rate); // 프레임을 측정함..
		interval *= 1.2;
		if (temp < interval) {
			return false;
		}
		if (temp >= interval) {
			pc.setAutoTimeAttack(System.currentTimeMillis()); // 지금 시간을 기록함..
			return true;
		}
		return false;
	}

	private boolean isAutoMoveTime(PcInstance pc) {
		long temp = System.currentTimeMillis() - pc.getAutoTimeMove();
		if (pc.isLock()) {
			return false;
		}
		long interval = (long) (AutoHuntController.getGfxFrameTime(pc, pc.getGfx(),
				pc.getGfxMode() + Lineage.GFX_MODE_WALK) * Lineage.speed_check_walk_frame_rate);
		interval *= 1.7;
		// System.out.println(temp);
		// System.out.println(interval);
		if (temp < interval) {
			return false;
		}
		if (temp >= interval) {
			pc.setAutoTimeMove(System.currentTimeMillis());
			return true;
		}
		return false;
	}

	static public boolean isDelayTime(PcInstance pc, long delay) {
		long temp = System.currentTimeMillis() - pc.getPotionTime();
		if (pc.isLock()) {
			return false;
		}

		long interval = (long) (delay * 1.2);

		if (temp < interval) {
			return false;
		}
		if (temp >= interval) {
			pc.setPotionTime(System.currentTimeMillis());
			return true;
		}
		return false;
	}

	private void toRandomWalk(PcInstance pc) {
		if (!isAutoMoveTime(pc))
			return;
		tagertClear(pc);
		pc.clearAstarList2();
		jsn_hunt jh = AutoHuntDatabase.find(pc.getObjectId());
		if (AutoHuntController.isHome(pc.getX(), pc.getY(), pc.getMap())
				&& pc.getHpPercent() >= jh.getPotionPercent()) {
			toTeleport(pc);
		} else if (AutoHuntController.isHome(pc.getX(), pc.getY(), pc.getMap())
				&& pc.getHpPercent() <= jh.getPotionPercent()) {
			return;
		}

		if (pc.getAutoMap() != 0 && pc.getMap() != pc.getAutoMap()
				&& !AutoHuntController.isHome(pc.getX(), pc.getY(), pc.getMap()) && pc.getMap() != 666
				&& pc.getMap() != 5167 && pc.getMap() != 68 && pc.getMap() != 69 && pc.getMap() != 110) {
			LocationController.toGiran(pc);
			pc.toTeleport(pc.getHomeX(), pc.getHomeY(), pc.getHomeMap(), false);
		}
		if (pc.getMap() == 4
				|| AutoHuntController.getMapName(pc.getX(), pc.getY(), pc.getMap()).equalsIgnoreCase("[아덴필드]")) {
			if (!Util.isDistance(pc.getX(), pc.getY(), pc.getMap(), jh.getX(), jh.getY(), jh.getMap(), 60)) {
				toTeleport(pc);
			}
		}

		do {
			switch (Util.random(0, 10)) {
				case 0:
				case 1:
					pc.setHeading(pc.getHeading() + 1);
					break;
				case 2:
				case 3:
					pc.setHeading(pc.getHeading() - 1);
					break;
				case 4:
				case 5:
					pc.setHeading(Util.random(0, 7));
					break;
			}
			// 이동 좌표 추출.
			int x = Util.getXY(pc.getHeading(), true) + pc.getX();
			int y = Util.getXY(pc.getHeading(), false) + pc.getY();
			int heading = pc.getHeading();
			// 해당 좌표 이동가능한지 체크.
			boolean tail = World.isThroughObject(x, y, pc.getMap(), heading)
					&& (World.isMapdynamic(x, y, pc.getMap()) == false);

			// 타일이 이동가능하고 객체가 방해안하면 이동처리.
			if (tail && toMoving(pc, x, y, heading, true, true)) {
				toMoving(pc, x, y, heading, true, false);
				pc.clearAstarList2();
			} else {
				pc.clearAstarList2();
			}
		} while (false);
	}

	private boolean toMoving(object o, final int x, final int y, final int h, final boolean astar, boolean ischack) {
		if (o == null)
			return false;
		PcInstance pc = World.findPc(o.getName());
		if (astar) {

			for (object oo : pc.getInsideList()) {
				if (oo.getClanId() != pc.getClanId() && oo.getX() == x && oo.getY() == y && oo.getNowHp() > 1
						&& Util.isDistance(pc, oo, 1)) {
					// System.out.println("이동 불가 오브젝트 있음.");
					return false;
				}
			}

			if (World.getMapdynamic(x, y, pc.getMap()) > 1) {
				return false;
			}

			pc.aStar2.cleanTail();
			pc.tail2 = pc.aStar2.searchTail(pc, x, y, true);

			if (pc.tail2 != null) {
				while (pc.tail2 != null) {
					// 현재위치 라면 종료
					if (pc.tail2.x == pc.getX() && pc.tail2.y == pc.getY())
						break;
					//
					pc.iPath2[0] = pc.tail2.x;
					pc.iPath2[1] = pc.tail2.y;
					pc.tail2 = pc.tail2.prev;
				}

				if (!ischack) {
					pc.toMoving(pc.iPath2[0], pc.iPath2[1],
							Util.calcheading(pc.getX(), pc.getY(), pc.iPath2[0], pc.iPath2[1]));
					pc.toSender(S_ObjectMoving.clone(BasePacketPooling.getPool(S_ObjectMoving.class), pc));
				}
				return true;
			} else {
				// 그외엔 에이스타 무시목록에 등록.
				if (o != null)
					pc.appendAstarList2(o);
				return false;
			}
		} else {
			pc.toMoving(x, y, h);
			pc.toSender(S_ObjectMoving.clone(BasePacketPooling.getPool(S_ObjectMoving.class), pc));
			// pc.toTeleport2(pc.getX(), pc.getY(), pc.getMap(), false);
			return true;
		}
	}

	private void toTeleport(PcInstance pc) {
		jsn_hunt jh = AutoHuntDatabase.find(pc.getObjectId());
		pc.toTeleport(jh.getX(), jh.getY(), jh.getMap(), false);
	}

	/**
	 * 버프 물약 복용
	 * 
	 * @return
	 */
	private boolean toBuffPotion(PcInstance pc) {
		//
		Buff b = BuffController.find(pc);
		if (b == null)
			return false;
		if (b.find(HastePotion.class) == null && b.find(Haste.class) == null) {
			ItemInstance item3 = pc.getInventory().find("강화 초록 물약");
			if (item3 != null && isDelayTime(pc, item3.getItem().getContinuous())) {
				item3.toClick(pc, null);
				return true;
			}
		}

		if ((b.find(Bravery.class) == null && b.find(Wafer.class) == null && b.find(HolyWalk.class) == null)) {
			ItemInstance item = pc.getInventory().find("용기의 물약");
			ItemInstance item3 = pc.getInventory().find("악마의 피");
			ItemInstance item2 = pc.getInventory().find("엘븐 와퍼");

			if (item2 != null && pc.getClassType() == Lineage.LINEAGE_CLASS_ELF
					&& isDelayTime(pc, item2.getItem().getContinuous())) {
				ChattingController.toChatting(pc, "물약 사용: " + item2.getItem().getName(), Lineage.CHATTING_MODE_MESSAGE);
				item2.toClick(pc, null);
				return true;
			}
			if (item != null && pc.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT
					&& isDelayTime(pc, item.getItem().getContinuous())) {
				ChattingController.toChatting(pc, "물약 사용: " + item.getItem().getName(), Lineage.CHATTING_MODE_MESSAGE);
				item.toClick(pc, null);
				return true;
			}
			if (item3 != null && pc.getClassType() == Lineage.LINEAGE_CLASS_ROYAL
					&& isDelayTime(pc, item3.getItem().getContinuous())) {
				ChattingController.toChatting(pc, "물약 사용: " + item3.getItem().getName(), Lineage.CHATTING_MODE_MESSAGE);
				item3.toClick(pc, null);
				return true;
			}
		}

		if ((b.find(CursePoison.class) != null || b.find(CurseGhoul.class) != null
				|| b.find(CurseGhast.class) != null)) {
			ItemInstance item = pc.getInventory().find("비취 물약");

			if (item != null && isDelayTime(pc, item.getItem().getContinuous())) {
				ChattingController.toChatting(pc, "물약 사용: " + item.getItem().getName(), Lineage.CHATTING_MODE_MESSAGE);
				item.toClick(pc, null);
				return true;
			}
		}

		return false;
	}

	private boolean toBuffskill(PcInstance pc) {
		//
		Buff b = BuffController.find(pc);
		if (b == null)
			return false;

		if (b.find(HolyWalk.class) == null) {
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_WIZARD) {
				int mp = SkillDatabase.find(52).getMpConsume();
				if (mp > 0 && pc.getNowMp() > mp) {
					if (SkillController.find(pc) != null) {
						for (Skill s : SkillController.find(pc)) {
							if (s.getName().equalsIgnoreCase("홀리 워크")) {
								if (SkillController.isMagic(pc, SkillDatabase.find(52), true)) {
									HolyWalk.onBuff(pc, SkillDatabase.find(52));
									return true;
								}
							}
						}
					}
				}
			}
		}
		if (b.find(Shield.class) == null) {
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_ROYAL || pc.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT
					|| pc.getClassType() == Lineage.LINEAGE_CLASS_ELF
					|| pc.getClassType() == Lineage.LINEAGE_CLASS_WIZARD) {
				if (SkillController.find(pc) != null) {
					int mp = SkillDatabase.find(3).getMpConsume();
					if (mp > 0 && pc.getNowMp() > mp) {
						for (Skill s : SkillController.find(pc)) {
							if (s.getName().equalsIgnoreCase("실드")) {
								if (SkillController.isMagic(pc, SkillDatabase.find(3), true)) {
									Shield.onBuff(pc, SkillDatabase.find(3));
									return true;

								}
							}
						}
					}
				}
			}
		}
		if (b.find(EnchantDexterity.class) == null) {
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_ROYAL || pc.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT) {
				for (ItemInstance i : pc.getInventory().getList()) {
					if (i != null && i.getItem().getName().equalsIgnoreCase("마법의 투구: 신속")) {
						int mp = SkillDatabase.find(26).getMpConsume();
						if (mp > 0 && pc.getNowMp() > mp) {
							if (SkillController.isMagic(pc, SkillDatabase.find(26), true)) {
								EnchantDexterity.onBuff(pc, SkillDatabase.find(26));
								return true;
							}
						}
					}

				}
			}

		}
		if (b.find(EnchantMighty.class) == null) {
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_ROYAL || pc.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT) {
				for (ItemInstance i : pc.getInventory().getList()) {
					if (i != null && i.getItem().getName().equalsIgnoreCase("마법의 투구: 힘")) {
						int mp = SkillDatabase.find(3).getMpConsume();
						if (mp > 0 && pc.getNowMp() > mp) {
							if (SkillController.isMagic(pc, SkillDatabase.find(42), true)) {
								EnchantMighty.onBuff(pc, SkillDatabase.find(42));
								return true;
							}
						}
					}
				}
			}
		}
		if (b.find(EnchantMighty.class) == null) {
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_ELF || pc.getClassType() == Lineage.LINEAGE_CLASS_WIZARD) {
				int mp = SkillDatabase.find(42).getMpConsume();
				if (mp > 0 && pc.getNowMp() > mp) {
					if (SkillController.find(pc) != null) {
						for (Skill s : SkillController.find(pc)) {
							if (s.getName().equalsIgnoreCase("피지컬 인첸트-STR")) {
								if (SkillController.isMagic(pc, SkillDatabase.find(42), true)) {
									EnchantMighty.onBuff(pc, SkillDatabase.find(42));
									return true;
								}
							}
						}
					}
				}
			}
		}
		if (b.find(EnchantDexterity.class) == null) {
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_ELF || pc.getClassType() == Lineage.LINEAGE_CLASS_WIZARD) {
				int mp = SkillDatabase.find(26).getMpConsume();
				if (mp > 0 && pc.getNowMp() > mp) {
					if (SkillController.find(pc) != null) {
						for (Skill s : SkillController.find(pc)) {
							if (s.getName().equalsIgnoreCase("피지컬 인첸트-DEX")) {
								if (SkillController.isMagic(pc, SkillDatabase.find(26), true)) {
									EnchantDexterity.onBuff(pc, SkillDatabase.find(26));
									return true;
								}
							}
						}
					}
				}
			}
		}
		if (b.find(AdvanceSpirit.class) == null) {
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_WIZARD) {
				int mp = SkillDatabase.find(67).getMpConsume();
				if (mp > 0 && pc.getNowMp() > mp) {
					if (SkillController.find(pc) != null) {
						for (Skill s : SkillController.find(pc)) {
							if (s.getName().equalsIgnoreCase("어드밴스 스피릿")) {
								if (SkillController.isMagic(pc, SkillDatabase.find(67), true)) {
									AdvanceSpirit.onBuff(pc, SkillDatabase.find(67));
									return true;
								}
							}
						}
					}
				}
			}
		}

		// if (pc.getClassType() == Lineage.LINEAGE_CLASS_WIZARD) {
		//
		// int mp = SkillDatabase.find(46).getMpConsume();
		// if (mp > 0 && pc.getNowMp() > mp) {
		// if (SkillController.find(pc) != null) {
		// for (Skill s : SkillController.find(pc)) {
		// if (s.getName().equalsIgnoreCase("선 버스트")) {
		// object o = pc.findInsideList(pc.getAutoTargetList().size());
		// if (o != null && Util.isDistance(pc, o, 3) &&
		// SkillController.isMagic(pc, SkillDatabase.find(46), true)) {
		// EnergyBolt.toBuff(pc, o, SkillDatabase.find(46),
		// Lineage.GFX_MODE_SPELL_DIRECTION,
		// SkillDatabase.find(46).getCastGfx(), 0);
		// return true;
		// }
		//
		// }
		// }
		// }
		// }
		// }

		return false;
	}

	/**
	 * 체력 물약 복용.
	 */
	private boolean toHealingPotion(PcInstance pc) {
		//
		jsn_hunt jh = AutoHuntDatabase.find(pc.getObjectId());
		if (pc.getHpPercent() > jh.getPotionPercent())
			return false;
		//
		ItemInstance item = pc.getInventory().find(jh.getPotion());
		if (item != null && isDelayTime(pc, item.getItem().getContinuous()))
			item.toClick(pc, null);
		return true;
	}

	private void restart(PcInstance pc) {
		// if (pc == null) {
		// return;
		// }
		// if (pc.getAutoDeadTime() < 500) {
		// pc.setAutoDeadTime(pc.getAutoDeadTime() + 1);
		// return;
		// }
		// if (!pc.isDead()) {
		// return;
		// }
		// pc.toReset(false);
		// pc.setNowHp(pc.getMaxHp());
		// pc.setFood(25);
		// pc.setDead(false);
		// pc.setAutoDead(true);
		// pc.setAiStatus(0);
		// AutoHuntController.toHome(pc);
		// pc.toTeleport(pc.getHomeX(), pc.getHomeY(), pc.getHomeMap(), false);
		// pc.setAutoTarget(null);
		// pc.getAutoTargetList().clear();
		// pc.setAutoDeadTime(0);
		// pc.setAutoStatus(AUTO_STATUS_MOVE_SHOP);
		// pc.toSender(S_ObjectRevival.clone(BasePacketPooling.getPool(S_ObjectRevival.class),
		// pc, pc), true);
		// pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class),
		// pc, 230), true);
		removeAuto(pc);
	}

	public boolean getAuto(PcInstance pc) {
		if (_pcList.contains(pc))
			return true;
		else
			return false;
	}

	public void addAuto(PcInstance pc) {
		if (_pcList.contains(pc))
			return;
		_pcList.add(pc);
	}

	public void removeAuto(PcInstance pc) {
		if (!_pcList.contains(pc))
			return;
		if (pc != null) {
			resetAuto(pc);
			pc.setAutoStatus(-1);
			pc.setAutoHunt(false);
			ChattingController.toChatting(pc, "자동사냥을 종료합니다.", Lineage.CHATTING_MODE_MESSAGE);
			// AutoHuntController.toHome(pc);
			pc.toTeleport(pc.getHomeX(), pc.getHomeY(), pc.getHomeMap(), false);
			if (pc.getClient() == null) {
				World.removePc(pc);
				World.remove(pc);
			}
			_pcList.remove(pc);
		} else {
		}
	}

	private void noTargetTeleport(PcInstance pc) {
		if (pc.getAutoTarget() != null)
			return;
		if (pc.getAutoAiTime() < 0) {
			pc.setAutoAiTime(150);
		} else {
			pc.setAutoAiTime(pc.getAutoAiTime() - 1);
			// System.out.println(pc.getAutoAiTime());
			if (pc.getAutoTargetList().size() == 0 && pc.getAutoAiTime() == 0) {
				if (!AutoHuntController.isHome(pc.getX(), pc.getY(), pc.getMap())) {
					// String local = AutoHuntController.getMapName(pc.getX(), pc.getY(),
					// pc.getMap());
					pc.clearAstarList2();

					if (!LocationController.isTeleportZone(pc, true, false)) {
						pc.setAutoAiTime(150);
						return;
					} else if (pc.getMap() == 4) {
						pc.setAutoAiTime(150);
						return;
					}
					ItemInstance item = pc.getInventory().find("순간이동 주문서");
					// 랜덤 텔레포트
					Util.toRndLocation(pc);
					pc.toTeleport(pc.getHomeX(), pc.getHomeY(), pc.getHomeMap(), false);
					pc.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
					pc.setAutoAiTime(150);
					pc.getInventory().count(item, item.getCount() - 1, true);
					ChattingController.toChatting(pc, "순간이동 주문서를 사용합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				pc.setAutoAiTime(1000);
			}
		}
	}

	public void resetAuto(PcInstance pc) {
		pc.setAutoStatus(AUTO_STATUS_NONE);
		pc.setAutoDead(false);
		pc.setAutoDeadTime(0);
		pc.setAutoTarget(null);
		pc.getAutoTargetList().clear();
		pc.setAutoAiTime(0);
		pc.setAutoHunt(false);
	}
}