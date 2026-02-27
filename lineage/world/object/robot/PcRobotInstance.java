package lineage.world.object.robot;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.MagicdollList;
import lineage.bean.database.Poly;
import lineage.bean.database.Shop;
import lineage.bean.database.Skill;
import lineage.bean.database.SkillRobot;
import lineage.bean.lineage.Buff;
import lineage.bean.lineage.Book;
import lineage.bean.lineage.Clan;
import lineage.bean.lineage.Kingdom;
import lineage.bean.lineage.Summon;
import lineage.database.ItemDatabase;
import lineage.database.PolyDatabase;
import lineage.database.ServerDatabase;
import lineage.database.SkillDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.ServerBasePacket;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectRevival;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.thread.AiThread;
import lineage.util.Util;
import lineage.world.AStar;
import lineage.world.Node;
import lineage.world.World;
import lineage.world.controller.BookController;
import lineage.world.controller.BuffController;
import lineage.world.controller.CharacterController;
import lineage.world.controller.ClanController;
import lineage.world.controller.KingdomController;
import lineage.world.controller.LocationController;
import lineage.world.controller.MagicDollController;
//import lineage.world.controller.QuestTodayController;
import lineage.world.controller.RobotController;
import lineage.world.controller.SkillController;
import lineage.world.controller.SummonController;
import lineage.world.controller.SummonController.TYPE;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.DwarfInstance;
import lineage.world.object.instance.GuardInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MagicDollInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.NpcInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.RobotInstance;
import lineage.world.object.instance.ShopInstance;
import lineage.world.object.instance.SummonInstance;
import lineage.world.object.item.Aden;
import lineage.world.object.item.Arrow;
import lineage.world.object.item.MagicDoll;
import lineage.world.object.item.potion.BraveryPotion;
import lineage.world.object.item.potion.ElvenWafer;
import lineage.world.object.item.potion.HastePotion;
import lineage.world.object.item.potion.HealingPotion;
import lineage.world.object.item.scroll.ScrollPolymorph;
import lineage.world.object.magic.Detection;
import lineage.world.object.magic.Haste;
import lineage.world.object.npc.kingdom.KingdomCastleTop;
import lineage.world.object.npc.kingdom.KingdomDoor;

public class PcRobotInstance extends RobotInstance {

	private static int ADEN_LIMIT = 1000000; // 아데나 체크할 최소값 및 추가될 아데나 갯수.
	private static int HEALING_PERCENT = 90; // 체력 회복제를 복용할 시점 백분율
	protected static int GOTOHOME_FERCENT = 40; // 체력이 해당퍼센트값보다 작으면 귀환함.

	protected static enum PCROBOT_MODE {
		None, // 기본값
		HealingPotion, // 물약상점 이동.
		HastePotion, // 초록물약 상점 이동.
		BraveryPotion, // 용기물약 상점 이동.
		Arrow, // 화살 상점 이동.
		InventoryHeavy, // 마을로 이동.
		ElvenWafer, // 엘븐와퍼 상점 이동.
		Stay, // 휴식 모드.
	}

	private AStar aStar; // 길찾기 변수
	private Node tail; // 길찾기 변수
	private int[] iPath; // 길찾기 변수
	private List<object> attackList; // 전투 목록
	private List<object> astarList; // astar 무시할 객체 목록.
	protected Item weapon;
	protected int weaponEn; // 무기 인첸
	protected PCROBOT_MODE pcrobot_mode; // 처리 모드.
	private int step; // 일렬에 동작처리중 사용되는 스탭변수.
	private int tempGfx; // 변신값 임시 저장용
	private ShopInstance shopTemp; // 상점 처리 임시 저장용
	private MagicdollList mdl; // 마법인형
	private String typeRobot; //
	// 시체유지(toAiCorpse) 구간에서 사용중.
	// 재스폰대기(toAiSpawn) 구간에서 사용중.
	protected long ai_time_temp_1;
	//
	protected boolean isWarSentry;
	//
	private List<object> ai_list_temp;

	public PcRobotInstance() {
		aStar = new AStar();
		iPath = new int[2];
		astarList = new ArrayList<object>();
		attackList = new ArrayList<object>();
		ai_list_temp = new ArrayList<object>();
		isWarSentry = false;
		pcrobot_mode = PCROBOT_MODE.None;
	}

	@Override
	public void close() {
		super.close();
		//
		if (getInventory() != null) {
			for (ItemInstance ii : getInventory().getList())
				ItemDatabase.setPool(ii);
			getInventory().clearList();
		}

		typeRobot = null;
		shopTemp = null;
		weapon = null;
		isWarSentry = false;
		ai_time_temp_1 = weaponEn = step = tempGfx = 0;
		if (aStar != null)
			aStar.cleanTail();
		if (attackList != null)
			clearAttackList();
		if (astarList != null)
			clearAstarList();
	}

	@Override
	public void toSave(Connection con) {
	}

	protected int getAttackListSize() {
		return attackList.size();
	}

	private void appendAttackList(object o) {
		synchronized (attackList) {
			if (!attackList.contains(o))
				attackList.add(o);
		}
	}

	public void removeAttackList(object o) {
		synchronized (attackList) {
			attackList.remove(o);
		}
	}

	protected List<object> getAttackList() {
		synchronized (attackList) {
			return new ArrayList<object>(attackList);
		}
	}

	public boolean containsAttackList(object o) {
		synchronized (attackList) {
			return attackList.contains(o);
		}
	}

	protected void clearAttackList() {
		synchronized (attackList) {
			attackList.clear();
		}
	}

	public boolean containsAstarList(object o) {
		synchronized (astarList) {
			return astarList.contains(o);
		}
	}

	private void appendAstarList(object o) {
		synchronized (astarList) {
			if (!astarList.contains(o))
				astarList.add(o);
		}
	}

	public void removeAstarList(object o) {
		synchronized (astarList) {
			astarList.remove(o);
		}
	}

	protected void clearAstarList() {
		synchronized (astarList) {
			astarList.clear();
		}
	}

	public void setWeapon(Item weapon) {
		this.weapon = weapon;
	}

	public void setWeaponEn(int weaponEn) {
		this.weaponEn = weaponEn;
	}

	public int getTempGfx() {
		return tempGfx;
	}

	public void setTempGfx(int tempGfx) {
		this.tempGfx = tempGfx;
	}

	public MagicdollList getMdl() {
		return mdl;
	}

	public void setMdl(MagicdollList mdl) {
		this.mdl = mdl;
	}

	public String getTypeRobot() {
		return typeRobot;
	}

	public void setTypeRobot(String typeRobot) {
		this.typeRobot = typeRobot;
	}

	@Override
	public void toWorldJoin() {
		super.toWorldJoin();
		// 인공지능 상태 변경.
		setAiStatus(Lineage.AI_STATUS_WALK);
		// 메모리 세팅
		setAutoPickup(Lineage.auto_pickup);
		World.appendRobot(this);
		BookController.toWorldJoin(this);
		RobotController.readBook(this);
		ClanController.toWorldJoin(this);
		CharacterController.toWorldJoin(this);
		BuffController.toWorldJoin(this);
		SkillController.toWorldJoin(this);
		RobotController.readSkill(this);
		SummonController.toWorldJoin(this);
		MagicDollController.toWorldJoin(this);
		KingdomController.toWorldJoin(this);
		RobotController.readDrop(this);
		if (weapon != null && weapon.getType1().equalsIgnoreCase("weapon")) {
			ItemInstance item = ItemDatabase.newInstance(weapon);
			item.setObjectId(ServerDatabase.nextNpcObjId());
			item.setEnLevel(weaponEn);
			getInventory().append(item, false);
		}
		// 인공지능 활성화를위해 등록.
		AiThread.append(this);
	}

	@Override
	public void toWorldOut() {
		//
		super.toWorldOut();
		//
		setAiStatus(Lineage.AI_STATUS_DELETE);
		// 죽어있을경우에 처리를 위해.
		toReset(true);
		// 사용된 메모리 제거
		World.removeRobot(this);
		SummonController.toWorldOut(this);
		BookController.toWorldOut(this);
		ClanController.toWorldOut(this);
		SkillController.toWorldOut(this);
		CharacterController.toWorldOut(this);
		MagicDollController.toWorldOut(this);
	}

	@Override
	public void toRevival(object o) {
		if (isDead()) {
			super.toReset(false);
			// 다이상태 풀기.
			setDead(false);
			// 체력 채우기.
			setNowHp(level);
			// 패킷 처리.
			toSender(S_ObjectRevival.clone(BasePacketPooling.getPool(S_ObjectRevival.class), o, this), false);
			// 상태 변경.
			ai_time_temp_1 = 0;
			setAiStatus(Lineage.AI_STATUS_WALK);
		}
	}

	@Override
	public void setDead(boolean dead) {
		super.setDead(dead);
		if (dead) {
			ai_time = 0;
			setAiStatus(Lineage.AI_STATUS_DEAD);
		}
	}

	@Override
	public void toDamage(Character cha, int dmg, int type, Object... opt) {
		super.toDamage(cha, dmg, type);
		// 버그 방지 및 자기자신이 공격햇을경우 무시.
		if (cha == null || cha.getObjectId() == getObjectId() || dmg <= 0)
			return;
		if (this instanceof WizardZoneRobotInstance) {
			if (Util.random(0, 99) <= 10)
				gotoHome(false);
			return;
		}
		// 공격목록에 추가.
		addAttackList(cha);
		// 길찾기 무시할 목록에서 제거.
		removeAstarList(cha);
		// 혈맹원들에게 알리기.
		Clan c = RobotController.findClan(this);
	}

	@Override
	public void toAiThreadDelete() {
		super.toAiThreadDelete();
		// 사용된 메모리 제거
		World.removeRobot(this);
		BookController.toWorldOut(this);
		CharacterController.toWorldOut(this);
	}

	@Override
	public void toAi(long time) {
		//
		if (this instanceof CrackerRobotInstance ||
				this instanceof HasteRobotInstance ||
				this instanceof PcRobotShopInstance ||
				this instanceof WizardZoneRobotInstance) {
			super.toAi(time);
			return;
		}
		//
		if (!isWorldDelete() && !isDead()) {
			// 무기 없으면 무시.
			if (weapon == null) {
				toWorldOut();
				ai_time = SpriteFrameDatabase.find(getGfx(), getGfxMode() + 0);
				return;
			}

			// 무기 착용.
			if (getInventory() != null && getInventory().getSlot(8) == null) {
				if (getInventory().find(weapon, weaponEn) == null) {
					// 무기 없으면 월드에서 제거.
					toWorldOut();
					ai_time = SpriteFrameDatabase.find(getGfx(), getGfxMode() + 0);
					return;
				} else {
					getInventory().find(weapon, weaponEn).toClick(this, null);
				}
			}
			// 필드에서만 체력 확인해서 귀환하기.
			if (World.isSafetyZone(getX(), getY(), getMap()) == false && getHpPercent() <= GOTOHOME_FERCENT) {
				// 너무 도망을 잘 치기 때문에 확률적으로 처리.
				if (Util.random(0, 99) <= 30) {
					gotoHome(true);
					ai_time = SpriteFrameDatabase.find(getGfx(), getGfxMode() + 0);
					return;
				}
			}
			// 스폰된 위치에서 너무 벗어낫을경우 마을로 귀환.
			if (!Util.isDistance(getX(), getY(), getMap(), getHomeX(), getHomeY(), getHomeMap(),
					Lineage.SEARCH_WORLD_LOCATION * 2)) {
				gotoHome(true);
				ai_time = SpriteFrameDatabase.find(getGfx(), getGfxMode() + 0);
				return;
			}
			// 공격자 목록 유효성 검사.
			for (object o : getAttackList()) {
				if (!isAttack(o, false) || containsAstarList(o))
					removeAttackList(o);
			}
			//
			if (getInventory() != null) {
				switch (getAiStatus()) {
					// 공격목록이 발생하면 공격모드로 변경
					case 0:
						if (getAttackListSize() > 0)
							setAiStatus(Lineage.AI_STATUS_ATTACK);
						// 무게가 무거울경우.
						if (pcrobot_mode == PCROBOT_MODE.None && getInventory().isWeightPercent(82) == false)
							pcrobot_mode = PCROBOT_MODE.InventoryHeavy;
						break;
					// 전투 처리부분은 항상 타켓들이 공격가능한지 확인할 필요가 있음.
					case 1:
						// 무게가 무거울경우.
						if (pcrobot_mode == PCROBOT_MODE.None && getInventory().isWeightPercent(82) == false)
							pcrobot_mode = PCROBOT_MODE.InventoryHeavy;
						// 전투목록이 없을경우 랜덤워킹으로 변경.
						if (getAttackListSize() == 0)
							setAiStatus(Lineage.AI_STATUS_WALK);
						// 변신상태가 변질되면
						if (isBadPolymorph()) {
							gotoHome(true);
							ai_time = SpriteFrameDatabase.find(getGfx(), getGfxMode() + 0);
							return;
						}
						break;
				}

				// 힐링 포션이 없다면.
				if (pcrobot_mode == PCROBOT_MODE.None && Lineage.robot_auto_pc_healingpotion != null
						&& getInventory().find(HealingPotion.class) == null)
					pcrobot_mode = PCROBOT_MODE.HealingPotion;
				// 속도향상 물약이 없다면.
				if (pcrobot_mode == PCROBOT_MODE.None && Lineage.robot_auto_pc_hastepotion != null
						&& getInventory().find(HastePotion.class) == null)
					pcrobot_mode = PCROBOT_MODE.HastePotion;
				// 용기물약이 없다면.
				if (pcrobot_mode == PCROBOT_MODE.None && Lineage.robot_auto_pc_braverypotion != null
						&& getClassType() == 0x01 && getInventory().find(BraveryPotion.class) == null)
					pcrobot_mode = PCROBOT_MODE.BraveryPotion;
				// 엘븐 와퍼가 없다면.
				if (pcrobot_mode == PCROBOT_MODE.None && Lineage.robot_auto_pc_elvenwafer != null
						&& getClassType() == 0x02 && getInventory().find(ElvenWafer.class) == null)
					pcrobot_mode = PCROBOT_MODE.ElvenWafer;
				// 화살이 없다면.
				if (pcrobot_mode == PCROBOT_MODE.None && weapon.getType2().equalsIgnoreCase("bow")
						&& getInventory().find(Arrow.class) == null)
					pcrobot_mode = PCROBOT_MODE.Arrow;
				// 모드가 변경되면
				if (pcrobot_mode != PCROBOT_MODE.None) {
					setAiStatus(Lineage.AI_STATUS_WALK);
					// 아데나 없다면 갱신.
					ItemInstance aden = getInventory().findAden();
					if (aden == null || aden.getCount() < ADEN_LIMIT) {
						if (aden == null) {
							aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
							aden.setObjectId(ServerDatabase.nextNpcObjId());
							getInventory().append(aden, false);
						}
						aden.setCount(aden.getCount() + ADEN_LIMIT);
					}
				}
			}
		}
		//
		super.toAi(time);
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	protected void toAiWalk(long time) {
		super.toAiWalk(time);

		//
		switch (pcrobot_mode) {
			case HealingPotion:
				// 물약 상점 으로 이동 및 구매.
				toShop(Lineage.robot_auto_pc_healingpotion, Lineage.robot_auto_pc_healingpotion_cnt);
				return;
			case HastePotion:
				// 속도향상물약 상점으로 이동 및 구매.
				toShop(Lineage.robot_auto_pc_hastepotion, Lineage.robot_auto_pc_hastepotion_cnt);
				return;
			case BraveryPotion:
				// 용기물약 상점으로 이동 및 구매.
				toShop(Lineage.robot_auto_pc_braverypotion, Lineage.robot_auto_pc_braverypotion_cnt);
				return;
			case ElvenWafer:
				// 엘븐 와퍼 상점으로 이동 및 구매.
				toShop(Lineage.robot_auto_pc_elvenwafer, Lineage.robot_auto_pc_elvenwafer_cnt);
				return;
			case Arrow:
				toShop(Lineage.robot_auto_pc_arrow, Lineage.robot_auto_pc_arrow_cnt);
				return;
			case InventoryHeavy:
				toInventoryHeavy();
				return;
			case Stay:
				toStay(time);
				return;
		}

		// 물약 복용. - 대기상태로 피를 채워야 하기 때문에.
		if (toHealingPotion())
			return;
		// 버프 시전.
		List<Skill> skill_list = SkillController.find(this);
		if (toSkillHealMp(skill_list) || toSkillHealHp(skill_list) || toSkillBuff(skill_list)
				|| toSkillSummon(skill_list)) {
			ai_time = SpriteFrameDatabase.getGfxFrameTime(this, gfx, gfxMode + Lineage.GFX_MODE_SPELL_NO_DIRECTION);
			return;
		}

		// 서먼객체에게 버프 시전.
		if (toBuffSummon()) {
			ai_time = SpriteFrameDatabase.getGfxFrameTime(this, gfx, gfxMode + Lineage.GFX_MODE_SPELL_NO_DIRECTION);
			return;
		}

		//
		if (!World.isNormalZone(getX(), getY(), getMap())) {
			// 보라돌이된 상태 제거하기.
			// 사냥터 이동.
			List<Book> list = BookController.find(this);
			// 등록된 사냥터 없으면 무시.
			if (list.size() == 0)
				return;
			//
			Book b = list.get(Util.random(0, list.size() - 1));
			setHomeX(b.getX());
			setHomeY(b.getY());
			setHomeMap(b.getMap());
			toTeleport(b.getX(), b.getY(), b.getMap(), true);
		} else {
			// 아이템 줍기 체크. (아데나 만)
			for (object o : getInsideList()) {
				if (o instanceof Aden && !containsAstarList(o)) {
					// 아이템이 발견되면 아이템줍기 모드로 전환.
					setAiStatus(Lineage.AI_STATUS_PICKUP);
					return;
				}
			}

			// 타켓 찾기.
			for (object o : getInsideList()) {
				if (isAttack(o, true)) {
					// 공격목록에 등록.
					if (!containsAstarList(o))
						addAttackList(o);
				}
			}

			// Astar 발동처리하다가 길이막혀서 이동못하던 객체를 모아놓은 변수를 일정주기마다 클린하기.
			if (Util.random(0, 10) == 0)
				clearAstarList();

			do {
				// 방향 전환
				switch (Util.random(0, 10)) {
					case 0:
					case 1:
						setHeading(getHeading() + 1);
						break;
					case 2:
					case 3:
						setHeading(getHeading() - 1);
						break;
					case 4:
					case 5:
						setHeading(Util.random(0, 7));
						break;
				}
				// 이동 좌표 추출.
				int x = Util.getXY(getHeading(), true) + getX();
				int y = Util.getXY(getHeading(), false) + getY();
				// 스폰된 위치에서 너무 벗어낫을경우 마을로 귀환.
				if (!Util.isDistance(x, y, getMap(), getHomeX(), getHomeY(), getHomeMap(),
						Lineage.SEARCH_WORLD_LOCATION)) {
					gotoHome(true);
					return;
				}
				// 해당 좌표 이동가능한지 체크.
				boolean tail = World.isThroughObject(getX(), getY(), getMap(), getHeading())
						&& World.isMapdynamic(x, y, map) == false;
				// 타일이 이동가능하고 객체가 방해안하면 이동처리.
				if (tail) {
					toMoving(null, x, y, getHeading(), false);
				} else {
					if (Util.random(0, 3) != 0)
						continue;
				}
			} while (false);
		}
	}

	@Override
	protected void toAiAttack(long time) {
		super.toAiAttack(time);
		// 물약 복용.
		toHealingPotion();
		toBuffPotion();
		toMagicDoll();

		// 공격자 확인.
		object o = findDangerousObject();
		// 객체를 찾지못했다면 무시.
		if (o == null)
			return;

		// 타켓이 사용자일때
		if (o instanceof PcInstance) {
			// 근처 경비병잇으면 마을로 귀환. (경비병이 같은 혈맹소속이 아닐때만.)
			for (object oo : getInsideList(true)) {
				if (oo instanceof GuardInstance && (getClanId() == 0 || getClanId() != oo.getClanId())) {
					gotoHome(true);
					return;

				}
			}
		}

		// 디텍션 발동.
		if (o.isInvis() && Util.random(0, 100) <= 30) {
			toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), this, 19), true);
			Detection.onBuff(this, SkillDatabase.find(2, 4));
			ai_time = SpriteFrameDatabase.find(getGfx(), getGfxMode() + 19);
			return;
		}

		// 버프 시전.
		List<Skill> skill_list = SkillController.find(this);
		if (toSkillAttack(skill_list, o, true)) {
			ai_time = SpriteFrameDatabase.find(getGfx(), getGfxMode() + 19) + 1080;
			return;
		}

		// 활공격인지 판단.
		ItemInstance weapon = getInventory().getSlot(8);
		boolean bow = weapon == null ? false : weapon.getItem().getType2().equalsIgnoreCase("bow");
		int atkRange = bow ? 10 : 1;
		// 객체 거리 확인
		if (Util.isDistance(this, o, atkRange) && Util.isAreaAttack(this, o)) {
			// 공격 시전했는지 확인용.
			if (Util.isDistance(this, o, atkRange)) {
				// 물리공격 범위내로 잇을경우 처리.
				try {
					toAttack(o, 0, 0, bow, getGfxMode() + 1, 0, false, 0);
				} catch (Exception e) {
				}
			} else {
				// 객체에게 접근.
				ai_time = SpriteFrameDatabase.find(getGfx(), getGfxMode() + 0);
				if (!isWarSentry)
					toMoving(o, o.getX(), o.getY(), 0, true);
			}
		} else {
			ai_time = SpriteFrameDatabase.find(getGfx(), getGfxMode() + 0);
			// 객체 이동 - 여기
			if (!isWarSentry)
				toMoving(o, o.getX(), o.getY(), 0, true);
		}
	}

	@Override
	protected void toAiDead(long time) {
		super.toAiDead(time);

		//
		for (object o : getAttackList()) {
			long id = 0;
			if (o instanceof PcInstance)
				id = o.getObjectId();
			if (o instanceof SummonInstance)
				id = o.getOwnObjectId();
			if (id == 0)
				continue;
			// QuestTodayController.toRobotDead(this, id);
		}
		//
		ai_time_temp_1 = 0;
		// 전투 관련 변수 초기화.
		clearAttackList();
		clearAstarList();
		// 상태 변환
		setAiStatus(Lineage.AI_STATUS_CORPSE);
	}

	@Override
	protected void toAiCorpse(long time) {
		super.toAiCorpse(time);

		if (ai_time_temp_1 == 0)
			ai_time_temp_1 = time;

		// 시체 유지
		if (ai_time_temp_1 + Lineage.ai_robot_corpse_time > time)
			return;

		ai_time_temp_1 = 0;
		// 버프제거
		toReset(true);
		// 시체 제거
		World.remove(this);
		clearList(true);
		// 상태 변환.
		setAiStatus(Lineage.AI_STATUS_SPAWN);
	}

	@Override
	protected void toAiSpawn(long time) {
		super.toAiSpawn(time);

		if (ai_time_temp_1 == 0)
			ai_time_temp_1 = time;

		// 스폰 대기
		if (ai_time_temp_1 + Lineage.ai_robot_spawn_time > time)
			return;

		//
		isWarSentry = false;
		// 로봇용 드랍목록 추출.
		RobotController.readDrop(this);
		// 부활 뒷 처리.
		toReset(false);
		// 마을로 이동.
		toTeleport(getHomeX(), getHomeY(), getHomeMap(), false);
		// 상태 변환.
		setAiStatus(Lineage.AI_STATUS_WALK);
	}

	@Override
	protected void toAiPickup(long time) {
		// 가장 근접한 아이템 찾기. (아데나 만)
		object o = null;
		for (object oo : getInsideList(true)) {
			if (oo instanceof Aden) {
				if (o == null)
					o = oo;
				else if (Util.getDistance(this, oo) < Util.getDistance(this, o))
					o = oo;
			}
		}
		// 못찾앗을경우 다시 랜덤워킹으로 전환.
		if (o == null) {
			setAiStatus(Lineage.AI_STATUS_WALK);
			return;
		}

		// 객체 거리 확인
		if (Util.isDistance(this, o, 1)) {
			super.toAiPickup(time);
			// 줍기 - 간혹 줍지못하고 멈춤. 픽업기능 제거해야 겟음. (원인을 모르겟음 현재는.)
			synchronized (o.sync_pickup) {
				if (o.isWorldDelete() == false)
					getInventory().toPickup(o, o.getCount());
			}
		} else {
			ai_time = SpriteFrameDatabase.find(getGfx(), getGfxMode() + 0);
			// 아이템쪽으로 이동.
			toMoving(o, o.getX(), o.getY(), 0, true);
		}
	}

	/**
	 * 주변에 상점 npc 찾는 메서드.
	 * 
	 * @return
	 */
	private ShopInstance findShopInstance() {
		object shop = null;
		for (object o : getInsideList(true)) {
			if (o instanceof ShopInstance) {
				if (shop == null)
					shop = o;
				else if (Util.getDistance(this, o) < Util.getDistance(this, shop))
					shop = o;
			}
		}
		return shop == null ? null : (ShopInstance) shop;
	}

	private void toStay(long time) {
		switch (step) {
			case 0:
				// 마을로 이동.
				gotoHome(false);
				step += 1;
				break;
			case 1:
				// 창고 근처로 이동.
				if (ai_list_temp.size() == 0)
					World.getLocationList(this, Lineage.SEARCH_WORLD_LOCATION, ai_list_temp);
				boolean isFind = false;
				for (object o : ai_list_temp) {
					if (o instanceof DwarfInstance) {
						isFind = true;
						// 거리 확인.
						if (Util.isDistance(this, o, Util.random(-12, 12))) {
							ai_list_temp.clear();
							step += 1;
							setHeading(Util.random(0, 7));
						} else {
							isFind = toMoving(o, o.getX(), o.getY(), 0, true);
						}
						break;
					}
				}
				// 창고를 찾지 못했거나 길을 못찾았다면 휴식모드 취소.
				if (isFind == false) {
					ai_list_temp.clear();
					// 초기화.
					step = 0;
					// 기본 모드로 변경.
					pcrobot_mode = PCROBOT_MODE.None;
				}
				break;
			case 2:
				// 휴식.
				if (ai_time_temp_1 == 0)
					ai_time_temp_1 = time;
				if (ai_time_temp_1 + Lineage.robot_auto_pc_stay_time > time)
					return;
				// 휴식 종료.
				ai_time_temp_1 = 0;
				// 초기화.
				step = 0;
				// 기본 모드로 변경.
				pcrobot_mode = PCROBOT_MODE.None;
				break;
		}
	}

	private void toPolymorph() {
		switch (step++) {
			case 0:
				// 마을로 이동.
				gotoHome(true);
				break;
			case 1:
				// 변신.
				ServerBasePacket sbp = (ServerBasePacket) ServerBasePacket
						.clone(BasePacketPooling.getPool(ServerBasePacket.class), null);
				sbp.writeC(0); // opcode
				sbp.writeS(getPolymorph()); // 변신 명
				byte[] data = sbp.getBytes();
				BasePacketPooling.setPool(sbp);
				BasePacket bp = ClientBasePacket.clone(BasePacketPooling.getPool(ClientBasePacket.class), data,
						data.length);
				// 처리 요청.
				getInventory().find(ScrollPolymorph.class).toClick(this, (ClientBasePacket) bp);
				// 메모리 재사용.
				BasePacketPooling.setPool(bp);
				// 초기화.
				step = 0;
				// 기본 모드로 변경.
				pcrobot_mode = PCROBOT_MODE.None;
				break;
		}
	}

	protected void toBadPolymorph() {
		switch (step++) {
			case 0:
				// 마을로 이동.
				gotoHome(true);
				break;
			case 1:
				// 변신 해제
				ServerBasePacket sbp = (ServerBasePacket) ServerBasePacket
						.clone(BasePacketPooling.getPool(ServerBasePacket.class), null);
				sbp.writeC(0); // opcode
				sbp.writeC(0); // 해제
				byte[] data = sbp.getBytes();
				BasePacketPooling.setPool(sbp);
				BasePacket bp = ClientBasePacket.clone(BasePacketPooling.getPool(ClientBasePacket.class), data,
						data.length);
				// 처리 요청.
				getInventory().find(ScrollPolymorph.class).toClick(this, (ClientBasePacket) bp);
				// 메모리 재사용.
				BasePacketPooling.setPool(bp);
				// 초기화.
				step = 0;
				// 기본 모드로 변경.
				pcrobot_mode = PCROBOT_MODE.None;
				break;
		}
	}

	protected void toInventoryHeavy() {
		switch (step++) {
			case 0:
				// 마을로 이동.
				gotoHome(true);
				break;
			case 1:
				// 인벤에 아이템 삭제.
				boolean isDrop = false;
				for (ItemInstance ii : getInventory().getList()) {
					//
					if (ii instanceof HealingPotion) {
						if (ii.getCount() > Lineage.robot_auto_pc_healingpotion_cnt) {
							isDrop = true;
							getInventory().count(ii, Lineage.robot_auto_pc_healingpotion_cnt, false);
							break;
						}
						continue;
					}
					if (ii instanceof HastePotion) {
						if (ii.getCount() > Lineage.robot_auto_pc_hastepotion_cnt) {
							isDrop = true;
							getInventory().count(ii, Lineage.robot_auto_pc_hastepotion_cnt, false);
							break;
						}
						continue;
					}
					if (ii instanceof BraveryPotion) {
						if (ii.getCount() > Lineage.robot_auto_pc_braverypotion_cnt) {
							isDrop = true;
							getInventory().count(ii, Lineage.robot_auto_pc_braverypotion_cnt, false);
							break;
						}
						continue;
					}
					if (ii instanceof ScrollPolymorph) {
						if (ii.getCount() > Lineage.robot_auto_pc_scrollpolymorph_cnt) {
							isDrop = true;
							getInventory().count(ii, Lineage.robot_auto_pc_scrollpolymorph_cnt, false);
							break;
						}
						continue;
					}
					if (ii instanceof ElvenWafer) {
						if (ii.getCount() > Lineage.robot_auto_pc_elvenwafer_cnt) {
							isDrop = true;
							getInventory().count(ii, Lineage.robot_auto_pc_elvenwafer_cnt, false);
							break;
						}
						continue;
					}
					if (ii instanceof Arrow) {
						if (ii.getCount() > Lineage.robot_auto_pc_arrow_cnt) {
							isDrop = true;
							getInventory().count(ii, Lineage.robot_auto_pc_arrow_cnt, false);
							break;
						}
						continue;
					}
					// 아데나는 무시.
					if (ii.getItem().getNameIdNumber() == 4)
						continue;
					// 착용중인 아이템 무시.
					if (ii.isEquipped())
						continue;
					// 그 외엔 다 제거.
					isDrop = true;
					getInventory().count(ii, 0, false);
					break;
				}
				// 드랍된 이력이 있다면 강제로 스탭을 1로 변경하여 재 호출될 수 있도록 함.
				if (isDrop)
					step = 1;
				break;
			case 2:
				// 초기화.
				step = 0;
				// 기본 모드로 변경.
				pcrobot_mode = PCROBOT_MODE.None;
				break;
		}
	}

	private void toShop(String item_name, long count) {
		switch (step++) {
			case 0: // 이동
				// npc 찾기.
				int[] location = RobotController.findShop(item_name);
				if (location != null) {
					int x = 0;
					int y = 0;
					do {
						x = Util.random(location[0] - 3, location[0] + 3);
						y = Util.random(location[1] - 3, location[1] + 3);
						if (World.isThroughObject(x, y + 1, location[2], 0))
							break;
					} while (Util.random(0, 9) != 0);
					toTeleport(x, y, location[2], true);
				} else
					step = 4;
				break;
			case 1: // npc 클릭
				shopTemp = findShopInstance();
				if (shopTemp == null)
					step = 4;
				else
					shopTemp.toTalk(this, null);
				break;
			case 2: // buy 클릭
				shopTemp.toTalk(this, "buy", null, null);
				break;
			case 3: // 아이템선택 구매클릭
				Shop s = shopTemp.getNpc().findShopItemId(item_name, 1, 0);
				ServerBasePacket sbp = (ServerBasePacket) ServerBasePacket
						.clone(BasePacketPooling.getPool(ServerBasePacket.class), null);
				sbp.writeC(0); // opcode
				sbp.writeC(0); // 상점구입
				sbp.writeH(1); // 구매할 전체 갯수
				sbp.writeD(s.getUid()); // 상점 아이템 고유값
				sbp.writeD(count); // 구매 갯수.
				byte[] data = sbp.getBytes();
				BasePacketPooling.setPool(sbp);
				BasePacket bp = ClientBasePacket.clone(BasePacketPooling.getPool(ClientBasePacket.class), data,
						data.length);
				// 처리 요청.
				shopTemp.toDwarfAndShop(this, (ClientBasePacket) bp);
				// 메모리 재사용.
				BasePacketPooling.setPool(bp);
				break;
			case 4:
				// 초기화.
				step = 0;
				// 기본 모드로 변경.
				// 휴식봇이 존재하기때문에 해당 소스는 주석해도 될듯.
				// if(Util.random(0, 99)<10)
				// pcrobot_mode = PCROBOT_MODE.Stay;
				// else
				pcrobot_mode = PCROBOT_MODE.None;
				break;
		}
	}

	/**
	 * 공격자 목록에 등록처리 함수.
	 * 
	 * @param o
	 */
	public void addAttackList(object o) {
		if (!isDead() && !o.isDead() && o.getObjectId() != getObjectId()) {
			// 같은 혈맹원들에게 도움 요청하기.
			if (o instanceof PcInstance) {
				if (pcrobot_mode == PCROBOT_MODE.None && !KingdomController.isKingdomLocation(o)
						&& Util.isDistance(this, o, Lineage.SEARCH_ROBOT_TARGET_LOCATION) == false
						&& Util.random(0, 200) == 200) {
					int x = 0;
					int y = 0;
					int cnt = 0;
					do {
						x = Util.random(0, 1) == 0
								? o.getX() + Util.random(Lineage.SEARCH_LOCATIONRANGE + 5,
										Lineage.SEARCH_ROBOT_TARGET_LOCATION - 5)
								: o.getX() - Util.random(Lineage.SEARCH_LOCATIONRANGE,
										Lineage.SEARCH_ROBOT_TARGET_LOCATION - 5);
						y = Util.random(0, 1) == 0
								? o.getY() + Util.random(Lineage.SEARCH_LOCATIONRANGE + 5,
										Lineage.SEARCH_ROBOT_TARGET_LOCATION - 5)
								: o.getY() - Util.random(Lineage.SEARCH_LOCATIONRANGE,
										Lineage.SEARCH_ROBOT_TARGET_LOCATION - 5);
						if (++cnt > 100)
							break;
					} while (!World.isThroughObject(x, y + 1, o.getMap(), 0) ||
							!World.isThroughObject(x, y - 1, o.getMap(), 4) ||
							!World.isThroughObject(x - 1, y, o.getMap(), 2) ||
							!World.isThroughObject(x + 1, y, o.getMap(), 6) ||
							!World.isThroughObject(x - 1, y + 1, o.getMap(), 1) ||
							!World.isThroughObject(x + 1, y - 1, o.getMap(), 5) ||
							!World.isThroughObject(x + 1, y + 1, o.getMap(), 7) ||
							!World.isThroughObject(x - 1, y - 1, o.getMap(), 3));
					toTeleport(x, y, o.getMap(), true);
				}
			}
			// 공격목록에 추가.
			appendAttackList(o);
		}
	}

	/**
	 * 해당객체를 공격해도 되는지 분석하는 함수.
	 * 
	 * @param o
	 * @param walk
	 * @return
	 */
	protected boolean isAttack(object o, boolean walk) {
		if (o == null)
			return false;
		if (o.isDead())
			return false;
		if (o.isWorldDelete())
			return false;
		if (o.getGm() > 0)
			return false;
		if (o.isTransparent())
			return false;
		if (o.isBuffAbsoluteBarrier())
			return false;
		if (!Util.isDistance(this, o, Lineage.SEARCH_ROBOT_TARGET_LOCATION))
			return false;
		if (!(o instanceof Character))
			return false;
		if (o instanceof MagicDollInstance)
			return false;
		if (o instanceof KingdomCastleTop || o instanceof KingdomDoor)
			return false;
		if (/* o instanceof RobotInstance || */(o instanceof NpcInstance && !(o instanceof GuardInstance)))
			return false;
		if (o instanceof MonsterInstance && o.getGfxMode() != o.getClassGfxMode())
			return false;
		if (o instanceof MonsterInstance && ((MonsterInstance) o).getBoss() != null)
			return false;
		if (o.getClanId() > 0 && o.getClanId() == getClanId())
			return false;
		//
		Kingdom k = getClanId() > 0 ? KingdomController.find(this) : null;
		if (k != null && k.isWar()) {
			if (o instanceof GuardInstance)
				return false;
			if (KingdomController.isKingdomLocation(o, k.getUid()) == false)
				return false;
			return getClanId() != o.getClanId();
		} else {
			// 대상이 로봇일경우 적대관계 혈맹체크.
			if (o instanceof PcRobotInstance) {
				Clan c = ClanController.find(this);
				// if(c!=null && c.getEnemyClanUidList().contains(o.getClanId()))
				// return true;
			}
			//
			if (o instanceof PcInstance) {
				if (Lineage.server_version >= 163)
					return World.isNormalZone(getX(), getY(), getMap())
							&& (containsAttackList(o) || o.isBuffCriminal() || o.getLawful() < Lineage.NEUTRAL);
				else
					return World.isNormalZone(getX(), getY(), getMap())
							&& (containsAttackList(o) || o.isBuffCriminal());
			}
			if (o instanceof SummonInstance || o instanceof GuardInstance)
				return containsAttackList(o);
		}
		// 체크하려는 타켓이 다른 객체와 전투중이면 무시.
		// 랜덤워킹 시도중 체크일때만.
		if (walk && o instanceof MonsterInstance && !(o instanceof SummonInstance)) {
			MonsterInstance mi = (MonsterInstance) o;
			if (mi.getAttackList().size() != 0 && !(mi.getAttackList().get(0) instanceof RobotInstance))
				return false;
		}
		//
		return true;
	}

	/**
	 * 매개변수 좌표로 A스타를 발동시켜 이동시키기.
	 * 객체가 존재하는 지역은 패스하도록 함.
	 * 이동할때마다 aStar가 새로 그려지기때문에 과부하가 심함.
	 */
	protected boolean toMoving(object o, final int x, final int y, final int h, final boolean astar) {
		if (astar) {
			aStar.cleanTail();
			tail = aStar.searchTail(this, x, y, true);
			if (tail != null) {
				while (tail != null) {
					// 현재위치 라면 종료
					if (tail.x == getX() && tail.y == getY())
						break;
					//
					iPath[0] = tail.x;
					iPath[1] = tail.y;
					tail = tail.prev;
				}
				toMoving(iPath[0], iPath[1], Util.calcheading(this.x, this.y, iPath[0], iPath[1]));
				return true;
			} else {
				// 그외엔 에이스타 무시목록에 등록.
				if (o != null)
					appendAstarList(o);
				return false;
			}
		} else {
			toMoving(x, y, h);
			return true;
		}
	}

	/**
	 * 버프 물약 복용
	 * 
	 * @return
	 */
	private boolean toBuffPotion() {
		//
		Buff b = BuffController.find(this);
		if (b == null)
			return false;
		// 촐기 복용.
		if (b.find(Haste.class) == null) {
			ItemInstance item = getInventory().find(HastePotion.class);
			if (item != null && item.isClick(this)) {
				item.toClick(this, null);
				return true;
			}
		}
		// 용기 복용.
		if (getClassType() == 0x01) {
			ItemInstance item = getInventory().find(BraveryPotion.class);
			if (item != null && item.isClick(this)) {
				item.toClick(this, null);
				return true;
			}
		}
		// 엘븐와퍼 복용.
		if (getClassType() == 0x02) {
			ItemInstance item = getInventory().find(ElvenWafer.class);
			if (item != null && item.isClick(this)) {
				item.toClick(this, null);
				return true;
			}
		}
		return false;
	}

	/**
	 * 체력 물약 복용.
	 */
	private boolean toHealingPotion() {
		//
		if (getHpPercent() > HEALING_PERCENT)
			return false;
		//
		ItemInstance item = getInventory().find(HealingPotion.class);
		if (item != null && item.isClick(this))
			item.toClick(this, null);
		return true;
	}

	private boolean toMagicDoll() {
		//
		if (mdl == null)
			return false;
		//
		ItemInstance item = getInventory().find(MagicDoll.class);
		if (item == null) {
			item = ItemDatabase.newInstance(ItemDatabase.find(mdl.getItemName()));
			// 인벤에 등록처리.
			item.setCount(1);
			getInventory().append(item, item.getCount());
			// 메모리 재사용.
			ItemDatabase.setPool(item);
			// 다시 찾기.
			item = getInventory().find(MagicDoll.class);
		}
		if (item != null && item.isClick(this) && !item.isEquipped()) {
			item.toClick(this, null);
			return true;
		}
		return false;
	}

	private void toSkillDefence(List<Skill> list, Character cha) {
		if (list == null)
			return;
		for (Skill s : list) {
			SkillRobot sr = (SkillRobot) s;
			if (sr.getType().equalsIgnoreCase("defence_buff") == false)
				continue;
			// 거리체크
			if (s.getSkillLevel() == 11 && s.getSkillNumber() == 6 && Util.isDistance(this, cha, 1) == false)
				continue;
			//
			if (getTotalMp() >= s.getMpConsume() && BuffController.find(this, s) == null && Util.random(0, 30) == 0) {
				toSkill(s, cha.getObjectId());
				// 강제로 엠 만땅 채우기.
				setNowMp(getTotalMp());
				break;
			}
		}
	}

	/**
	 * 공격스킬 시전처리.
	 * 
	 * @param list
	 * @return
	 */
	protected boolean toSkillAttack(List<Skill> list, object o, boolean isChance) {
		//
		if (list == null)
			return false;
		//
		// if(getClassType() == 0x03)
		// System.out.println(list.size());
		for (Skill s : list) {
			SkillRobot sr = (SkillRobot) s;
			if (sr.getType().equalsIgnoreCase("attack") == false)
				continue;
			if (getTotalMp() >= s.getMpConsume() && (!isChance || Util.random(0, 100) == 0)) {
				toSkill(s, o.getObjectId());
				return true;
			}
		}
		return false;
	}

	/**
	 * 버프스킬 시전처리.
	 * 
	 * @return
	 */
	protected boolean toSkillBuff(List<Skill> list) {
		//
		if (list == null)
			return false;
		for (Skill s : list) {
			SkillRobot sr = (SkillRobot) s;
			if (sr.getType().equalsIgnoreCase("buff") == false)
				continue;
			//
			if (BuffController.find(this, s) == null && getTotalMp() >= s.getMpConsume()) {
				toSkill(s, getObjectId());
				return true;
			}
		}
		return false;
	}

	/**
	 * 소울 스킬 시전
	 * 
	 * @return
	 */
	protected boolean toSkillHealMp(List<Skill> list) {
		//
		if (getNowMp() == getTotalMp())
			return false;
		//
		if (list == null)
			return false;
		for (Skill s : list) {
			SkillRobot sr = (SkillRobot) s;
			if (sr.getType().equalsIgnoreCase("heal_mp") == false)
				continue;
			if (getTotalMp() >= s.getMpConsume()) {
				toSkill(s, getObjectId());
				return true;
			}
		}

		return false;
	}

	/**
	 * 힐 스킬 시전
	 * 
	 * @return
	 */
	protected boolean toSkillHealHp(List<Skill> list) {
		//
		if (getHpPercent() > HEALING_PERCENT)
			return false;
		//
		if (list == null)
			return false;
		for (Skill s : list) {
			SkillRobot sr = (SkillRobot) s;
			if (sr.getType().equalsIgnoreCase("heal_hp") == false)
				continue;
			if (getTotalMp() >= s.getMpConsume()) {
				toSkill(s, getObjectId());
				return true;
			}
		}
		return false;
	}

	/**
	 * 서먼 스킬 시전.
	 * 
	 * @return
	 */
	protected boolean toSkillSummon(List<Skill> list) {
		//
		if (list == null)
			return false;
		for (Skill s : list) {
			SkillRobot sr = (SkillRobot) s;
			if (sr.getType().equalsIgnoreCase("summon") == false)
				continue;
			if (getTotalMp() >= s.getMpConsume()
					&& SummonController.isAppend(null, this, getClassType() == 0x03 ? TYPE.MONSTER : TYPE.ELEMENTAL)) {
				toSkill(s, getObjectId());
				SummonController.find(this).setMode(SummonInstance.SUMMON_MODE.AggressiveMode);
				return true;
			}
		}
		return false;
	}

	/**
	 * 서먼한 객체에게 버프를 시전함.
	 * 
	 * @return
	 */
	private boolean toBuffSummon() {
		//
		Summon s = SummonController.find(this);
		if (s == null || s.getSize() == 0)
			return false;
		//
		for (object o : s.getList()) {
			Buff b = BuffController.find(o);
			// 헤이스트
			if (b == null || b.find(Haste.class) == null) {
				Skill haste = SkillController.find(this, 6, 2);
				if (haste != null) {
					toSkill(haste, o.getObjectId());
					return true;
				}
			}
			// 힐
			Character cha = (Character) o;
			if (cha.getHpPercent() <= HEALING_PERCENT) {
				int[][] heal_list = {
						{ 1, 0 }, // 힐
						{ 3, 2 }, // 익스트라 힐
						{ 5, 2 }, // 그레이터 힐
						{ 7, 0 }, // 힐 올
						{ 8, 0 }, // 풀 힐
						{ 20, 5 }, // 네이처스 터치
				};
				for (int[] data : heal_list) {
					Skill heal = SkillController.find(this, data[0], data[1]);
					if (heal != null) {
						toSkill(heal, o.getObjectId());
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 중복코드 방지용.
	 * 
	 * @param s
	 */
	private void toSkill(Skill s, long objectId) {
		ServerBasePacket sbp = (ServerBasePacket) ServerBasePacket
				.clone(BasePacketPooling.getPool(ServerBasePacket.class), null);
		sbp.writeC(0); // opcode
		sbp.writeC(s.getSkillLevel() - 1); // level
		sbp.writeC(s.getSkillNumber()); // number
		sbp.writeD(objectId); //
		sbp.writeH(0); //
		sbp.writeH(0); //
		byte[] data = sbp.getBytes();
		BasePacketPooling.setPool(sbp);
		BasePacket bp = ClientBasePacket.clone(BasePacketPooling.getPool(ClientBasePacket.class), data, data.length);
		SkillController.toSkill(this, (ClientBasePacket) bp);
		// 메모리 재사용.
		BasePacketPooling.setPool(bp);
	}

	/**
	 * 근처 마을로 귀환.
	 */
	protected void gotoHome(boolean isCheck) {
		// Kingdom k = KingdomController.find(this);
		// if(k != null) {
		// homeX = k.getX();
		// homeY = k.getY();
		// homeMap = k.getMap();
		// } else {
		// // 이미 마을일경우 무시.
		// if(isCheck && World.isSafetyZone(getX(), getY(), getMap()))
		// return;
		//
		LocationController.toHome(this);
		// }
		//
		clearAttackList();
		clearAstarList();
		toTeleport(getHomeX(), getHomeY(), getHomeMap(), isDead() == false);
	}

	/**
	 * 로봇과 전투중인 객체 목록에서 위험순위 높은거를 먼저 찾아서 리턴.
	 * pc를 1순위
	 * 나머지는 가까운 거리대로
	 * 
	 * @return
	 */
	protected object findDangerousObject() {
		object o = null;
		Kingdom k = getClanId() > 0 ? KingdomController.find(this) : null;

		//
		if (k != null && k.isWar()) {
			// 공성전 중일때 세이프존이라면 무시.
			if (World.isSafetyZone(getX(), getY(), getMap()))
				return null;
			//
			for (object oo : getInsideList(true)) {
				if (oo instanceof PcInstance && !(oo instanceof RobotInstance)) {
					if (!containsAstarList(oo) && !oo.isDead() && !oo.isLockHigh() && oo.getGm() <= 0) {
						// 자리를 고수하고 있는 녀석은 장거리 공격 가능한지 체크가 필요.
						if (isWarSentry && Util.isAreaAttack(this, oo) == false)
							continue;
						//
						if (o == null)
							o = oo;
						else if (Util.getDistance(this, oo) < Util.getDistance(this, o))
							o = oo;
					}
				}
			}
		} else {
			// pc사용자를 1순위.
			for (object oo : getAttackList()) {
				if (oo instanceof PcInstance) {
					if (!containsAstarList(oo)) {
						if (o == null)
							o = oo;
						else if (Util.getDistance(this, oo) < Util.getDistance(this, o))
							o = oo;
					}
				}
			}
		}
		// 찾지 못했다면 공격자목록에 등록된 객체에서 찾기.
		if (o == null) {
			for (object oo : getAttackList()) {
				if (!containsAstarList(oo)) {
					if (o == null)
						o = oo;
					else if (Util.getDistance(this, oo) < Util.getDistance(this, o))
						o = oo;
				}
			}
		}
		return o;
	}

	/**
	 * 클레스별로 변신할 이름 리턴.
	 * 
	 * @return
	 */
	protected String getPolymorph() {
		Object o = PluginController.init(PcRobotInstance.class, "getPolymorph", this);
		if (o != null)
			return (String) o;

		// 기사 & 군주
		if (getClassType() == 0x01 || getClassType() == 0x00) {
			if (getLevel() >= 52)
				return "death knight";
			else if (getLevel() >= 40)
				return "ettin";
			else if (getLevel() >= 15)
				return "king bugbear";
			else if (getLevel() >= 10)
				return "lycanthrope";
			else
				return "orc fighter";
		}
		// 요정
		if (getClassType() == 0x02) {
			if (getLevel() >= 52)
				return "dark elf polymorph";
			else if (getLevel() >= 15)
				return "orc scout polymorph";
			else if (getLevel() >= 10)
				return "skeleton archer polymorph";
			else
				return "bow orc";
		}
		// 마법사
		if (getClassType() == 0x03)
			return "dark elder";
		// 그 외엔 늑대인간
		return "werewolf";
	}

	/**
	 * 현재 변신상태가 최적화되지 않은 변신인지 확인하는 메서드.
	 * : 양호하다면 false를 리턴.
	 */
	protected boolean isBadPolymorph() {
		Poly p = PolyDatabase.getPolyName(getPolymorph());
		return p != null && getGfx() != p.getGfxId() && getGfx() != getClassGfx();
	}

	@Override
	public void toTeleport(final int x, final int y, final int map, final boolean effect) {
		//
		setHomeX(x);
		setHomeY(y);
		setHomeMap(map);
		//
		super.toTeleport(x, y, map, effect);
	}
}
