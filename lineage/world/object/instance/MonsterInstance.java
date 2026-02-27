package lineage.world.object.instance;

import java.util.ArrayList;
import java.util.List;

import jsn_soft.MovingHackDefense;
import lineage.bean.database.Boss;
import lineage.bean.database.Drop;
import lineage.bean.database.Exp;
import lineage.bean.database.Item;
import lineage.bean.database.Monster;
import lineage.bean.database.MonsterSkill;
import lineage.bean.database.MonsterSpawnlist;
import lineage.bean.lineage.Inventory;
import lineage.bean.lineage.Party;
import lineage.database.ExpDatabase;
import lineage.database.ItemDatabase;
import lineage.database.MonsterDatabase;
import lineage.database.MonsterSpawnlistDatabase;
import lineage.database.ServerDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectAttack;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectHeading;
import lineage.network.packet.server.S_ObjectHitratio;
import lineage.network.packet.server.S_ObjectRevival;
import lineage.plugin.PluginController;
import lineage.share.GameSetting;
import lineage.share.Lineage;
import lineage.share.System;
import lineage.util.Util;
import lineage.world.AStar;
import lineage.world.Node;
import lineage.world.World;
import lineage.world.controller.AutoHuntCheckController;
import lineage.world.controller.BossController;
import lineage.world.controller.CharacterController;
import lineage.world.controller.DamageController;
import lineage.world.controller.InventoryController;
import lineage.world.controller.KingdomController;
import lineage.world.controller.PartyController;
import lineage.world.controller.QuestTodayController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.SummonInstance.SUMMON_MODE;
import lineage.world.object.item.potion.CurePoisonPotion;
import lineage.world.object.item.potion.HastePotion;
import lineage.world.object.item.potion.HealingPotion;
import lineage.world.object.magic.monster.ShareMagic;

public class MonsterInstance extends Character {

	protected Monster mon; //
	protected Monster monOld; //
	private MonsterSpawnlist monSpawn; //
	private AStar aStar; // 길찾기 변수
	private Node tail; // 길찾기 변수
	private int[] iPath; // 길찾기 변수
	private double add_exp; // hp1당 지급될 경험치 값.
	private double dangerous_exp; // 공격자 우선처리를위한 변수로 경험치의 70% 값을 저장.
	private List<object> attackList; // 전투 목록
	private List<object> astarList; // astar 무시할 객체 목록.
	private List<Exp> expList; // 경험치 지급해야할 목록
	protected int ai_walk_stay_count; // 랜덤워킹 중 잠시 휴식을 취하기위한 카운팅 값
	protected int reSpawnTime; // 재스폰 하기위한 대기 시간값.
	protected Boss boss; // 보스 몬스터인지 여부. monster_spawnlist_boss 를 거쳐서 스폰된것만 세팅함.
	public int bossLiveTime; // 보스 생존 시간.
	// 시체유지(toAiCorpse) 구간에서 사용중.
	// protected boolean boss; // 보스 몬스터인지 여부. monster_spawnlist_boss 를 거쳐서
	// 스폰도니것만
	// 재스폰대기(toAiSpawn) 구간에서 사용중.
	protected long ai_time_temp_1;
	// 인벤토리
	protected Inventory inv;
	// 그룹몬스터에 사용되는 변수.
	protected List<MonsterInstance> group_list; // 현재 관리되고있는 몬스터 목록.
	protected MonsterInstance group_master; // 나를 관리하고있는 마스터객체 임시 저장용.
	// 스킬 딜레이를 주기위한 변수
	public long lastSkillTime;
	
	// 스팟 몬스터
	private boolean isSpotMonster;
	// 동적 변환 값
	protected int dynamic_attack_area; // 공격 범위 값.
	private boolean pineWand; // 소막 및 gm명령어 소환으로 처리된 몬스터인지 여부.

	private double temp_exp = 0;
	private boolean ice_dun_mob;
	private int ice_dun_step;
	private boolean ice_dun_boss;

	static synchronized public MonsterInstance clone(MonsterInstance mi,
			Monster m) {
		if (mi == null)
			mi = new MonsterInstance();
		mi.setMonster(m);
		mi.setAiStatus(0);
		return mi;
	}

	public MonsterInstance() {
		aStar = new AStar();
		astarList = new ArrayList<object>();
		attackList = new ArrayList<object>();
		expList = new ArrayList<Exp>();
		group_list = new ArrayList<MonsterInstance>();
		iPath = new int[2];
		// 인벤토리 활성화를 위해.
		InventoryController.toWorldJoin(this);
	}

	@Override
	public void close() {
		super.close();

		mon = monOld = null;
		classType = 0x0A;
		ai_time_temp_1 = reSpawnTime = dynamic_attack_area = 0;
		pineWand = false;
		boss = null;
		bossLiveTime = 0;
		monSpawn = null;
		group_master = null;
		lastSkillTime = 0L;
		temp_exp = 0;
		if (attackList != null)
			clearAttackList();
		if (astarList != null)
			clearAstarList();
		if (expList != null) {
			synchronized (expList) {
				for (Exp e : expList)
					ExpDatabase.setPool(e);
				expList.clear();
			}
		}
		if (inv != null) {
			for (ItemInstance ii : inv.getList())
				ItemDatabase.setPool(ii);
			inv.clearList();
		}
		if (group_list != null)
			group_list.clear();
		
		ice_dun_mob = ice_dun_boss = false;
		ice_dun_step = 0;
		
		isSpotMonster = false;
		kingdomBoss = false;
		//
		CharacterController.toWorldOut(this);
	}

	public boolean isIce_dun_mob() {
		return ice_dun_mob;
	}

	public void setIce_dun_mob(boolean ice_dun_mob) {
		this.ice_dun_mob = ice_dun_mob;
	}

	public int getIce_dun_step() {
		return ice_dun_step;
	}

	public void setIce_dun_step(int ice_dun_step) {
		this.ice_dun_step = ice_dun_step;
	}

	public boolean isIce_dun_boss() {
		return ice_dun_boss;
	}

	public void setIce_dun_boss(boolean ice_dun_boss) {
		this.ice_dun_boss = ice_dun_boss;
	}
	
	public boolean isSpotMonster() {
		return isSpotMonster;
	}

	public void setSpotMonster(boolean isSpotMonster) {
		this.isSpotMonster = isSpotMonster;
	}
	
	public void setMonsterSpawnlist(MonsterSpawnlist monSpawn) {
		this.monSpawn = monSpawn;
	}

	public MonsterSpawnlist getMonsterSpawnlist() {
		return monSpawn;
	}

	public void setAiTimeTemp1(long ai_time_temp_1) {
		this.ai_time_temp_1 = ai_time_temp_1;
	}

	public long getAiTimeTemp1() {
		return ai_time_temp_1;
	}

	public MonsterInstance getGroupMaster() {
		return group_master;
	}

	public void setGroupMaster(MonsterInstance group_master) {
		this.group_master = group_master;
	}

	/**
	 * 그룹에 속한 몬스터중 마스터를 찾아 리턴한다. : 실제 마스터가 죽엇을경우 그룹에 속한 목록에서 살아있는 놈을 마스터로 잡는다. :
	 * 모두 죽거나 하면 null을 리턴.
	 * 
	 * @return
	 */
	public MonsterInstance getGroupMasterDynamic() {
		if (group_master != null) {
			// System.out.println(this.getMonster().getName() + " : " +
			// group_master.getName());
			if (group_master.isDead()) {
				for (MonsterInstance mi : group_master.getGroupList()) {
					if (!mi.isDead())
						return mi;
				}
			} else if (group_master.getName() == null
					&& getGroupList().size() > 0) {
				for (MonsterInstance mi : getGroupList()) {
					if (!mi.isDead())
						return mi;
				}
			} else {
				return group_master;
			}
		}
		return null;
	}

	public List<MonsterInstance> getGroupList() {
		return group_list;
	}

	@Override
	public Inventory getInventory() {
		return inv;
	}

	@Override
	public void setInventory(Inventory inv) {
		this.inv = inv;
	}

	public int getReSpawnTime() {
		return reSpawnTime;
	}

	@Override
	public void setReSpawnTime(int reSpawnTime) {
		this.reSpawnTime = reSpawnTime;
	}

	@Override
	public int getHpTime() {
		return Lineage.ai_monster_tic_time;
	}

	@Override
	public int hpTic() {
		return mon.getTicHp();
	}

	@Override
	public int mpTic() {
		return mon.getTicMp();
	}

	public void setAddExp(double add_exp) {
		this.add_exp = add_exp;
	}

	public void setDangerousExp(double dangerous_exp) {
		this.dangerous_exp = dangerous_exp;
	}

	public void setMonster(Monster m) {
		if (m == null)
			return;
		this.mon = m;
		if (monOld == null)
			monOld = m;
		setAddExp((double) m.getExp() / (double) m.getHp());
		setDangerousExp(m.getExp() * 0.7);
		// 시전되는 스킬목록에서 거리범위가 세팅된게 잇을경우 그것으로 거리변경함.
		// 이렇게 해야 몬스터 인공지능 발동시 공격거리가 최상위로 잡혀서 리얼해짐.
		for (MonsterSkill ms : m.getSkillList()) {
			if (ms.getRange() > 0 && dynamic_attack_area < ms.getRange())
				dynamic_attack_area = ms.getRange();
		}
		// 틱값이 존재한다면 관리를위해 등록.
		if (m.getTicHp() > 0 || m.getTicMp() > 0)
			CharacterController.toWorldJoin(this);
	}

	@Override
	public Monster getMonster() {
		return mon;
	}

	protected int getAtkRange() {
		return dynamic_attack_area > 0 ? dynamic_attack_area : mon
				.getAtkRange();
	}

	
	public Boss getBoss() {
		return boss;
	}

	public void setBoss(Boss boss) {
		this.boss = boss;
	}

	// public boolean isBoss() {
	// return boss;
	//}
	//
	// public void setBoss(boolean boss) {
	// this.boss = boss;
	// }

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

	public List<object> getAttackList() {
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

	public List<Exp> getExpList() {
		synchronized (expList) {
			return new ArrayList<Exp>(expList);
		}
	}

	private void appendExpList(Exp e) {
		synchronized (expList) {
			if (!expList.contains(e))
				expList.add(e);
		}
	}

	protected void removeExpList(Exp e) {
		synchronized (expList) {
			expList.remove(e);
		}
	}

	private Exp getExpList(int index) {
		try {
			if (expList.size() > index)
				synchronized (expList) {
					return expList.get(index);
				}
		} catch (Exception e) {
		}
		return null;
	}

	public void removeExpList(object o) {
		for (Exp e : getExpList()) {
			if (e.getObject() == null
					|| o.getObjectId() == e.getObject().getObjectId()
					|| o.getObjectId() == e.getObject().getOwnObjectId())
				removeExpList(e);
		}
	}

	public void clearExpList() {
		synchronized (expList) {
			for (Exp e : expList)
				ExpDatabase.setPool(e);
			expList.clear();
		}
	}

	/**
	 * 전투목록에서 원하는 위치에있는 객체 찾아서 리턴.
	 * 
	 * @param index
	 * @return
	 */
	protected object getAttackList(int index) {
		try {
			if (getAttackListSize() > index) {
				synchronized (attackList) {
					return attackList.get(index);
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	protected int getAttackListSize() {
		return attackList.size();
	}

	public boolean isPineWand() {
		return pineWand;
	}

	public void setPineWand(boolean pineWand) {
		this.pineWand = pineWand;
	}

	/**
	 * 해당 객체와 연결된 경험치 객체 찾기.
	 * 
	 * @param o
	 * @return
	 */
	private Exp findExp(object o) {
		for (Exp e : getExpList()) {
			if (e.getObject() != null
					&& e.getObject().getObjectId() == o.getObjectId())
				return e;
		}
		return null;
	}

	/**
	 * 몬스터와 전투중인 객체들중 가장 위험수위에 있는 객체를 찾아서 리턴. 몬스터를 공격한 객체목록(경험치지급목록) 에서 지급된 경험치의
	 * 70% 이상값을 가진 사용자가 최우선.
	 * 
	 * @return
	 */
	protected object findDangerousObject() {
		object o = null;
		// 경험치의 70% 이상값을 가진 사용자 찾기.
		for (Exp e : getExpList()) {
			if (e.getObject() != null && !containsAstarList(e.getObject())) {

				if (e.getExp() > 0) {
					e.getObject().setTemp_exp(e.getExp());
				}

				if (e.getObject().getTemp_exp() > temp_exp) {
					temp_exp = e.getObject().getTemp_exp();
					o = e.getObject();
					return e.getObject();
				}

				// 70% 달하는 경험치 받아가는 놈 발견하면 무조건 공격하기.
				if (e.getExp() >= dangerous_exp)
					return e.getObject();
				// 가장근접한 객체 찾기.
				else if (o == null)
					o = e.getObject();
				else if (Util.getDistance(this, e.getObject()) < Util
						.getDistance(this, o))
					o = e.getObject();
			}
		}
		if (o != null)
			return o;

		// 찾지 못했다면 공격자목록에 등록된 사용자에서 찾기.
		for (object oo : getAttackList()) {
			if (!containsAstarList(oo)) {
				if (o == null)
					o = oo;
				else if (Util.getDistance(this, oo) < Util.getDistance(this, o))
					o = oo;
			}
		}
		return o;
	}

	@Override
	public void toRevival(object o) {
		if (this instanceof PetInstance) {
			super.toReset(false);
			// 다이상태 풀기.
			setDead(false);
			// 체력 채우기.
			setNowHp(level);
			// 패킷 처리.
			toSender(S_ObjectRevival.clone(
					BasePacketPooling.getPool(S_ObjectRevival.class), o, this),
					false);
			// 상태 변경.
			ai_time_temp_1 = 0;
			setAiStatus(0);

			setGfxModeClear();
		}

		if (o instanceof PcInstance && this instanceof PetInstance) {
			super.toReset(false);
			// 다이상태 풀기.
			setDead(false);
			// 체력 채우기.
			setNowHp(level);
			// 패킷 처리.
			toSender(S_ObjectRevival.clone(
					BasePacketPooling.getPool(S_ObjectRevival.class), o, this),
					false);
			// 상태 변경.
			ai_time_temp_1 = 0;
			setAiStatus(0);

			setGfxModeClear();
		}

		// 사용자가 부활을 시키는거라면 디비상에 부활가능 할때만 처리.
		if (o instanceof PcInstance && !mon.isRevival())
			return;

		if (isDead()) {
			super.toReset(false);
			// 다이상태 풀기.
			setDead(false);
			// 체력 채우기.
			setNowHp(level);
			// 패킷 처리.
			toSender(S_ObjectRevival.clone(
					BasePacketPooling.getPool(S_ObjectRevival.class), o, this),
					false);
			// 상태 변경.
			ai_time_temp_1 = 0;
			setAiStatus(0);
		}
	}

	@Override
	public void setNowHp(int nowHp) {

			if (getMonster().getName() != null) {
				switch (getMonster().getName()) {
				case "[오만의 탑 4층] 난폭한 구울": 
				case "[오만의 탑 4층] 난폭한 스파토이": 
				case "[오만의 탑 4층] 난폭한 해골 도끼병": 
				case "[오만의 탑 4층] 난폭한 해골 돌격병": 
				case "[오만의 탑 4층] 난폭한 해골 저격병": 
				case "[오만의 탑 5층] 공포의 아이언 골렘": 
				case "[오만의 탑 5층] 레서 드래곤": 
				case "[오만의 탑 5층] 본 드래곤": 
				case "[오만의 탑 5층] 화염의 고스트": 
				case "[오만의 탑 5층] 화염의 고스트1": 
				case "[오만의 탑 5층] 화염의 네크로맨서": 
				{
					if (nowHp > 0)
						break;

					int rnd = Util.random(0, 99);
					String name = null;
					name = "[오만의 탑] 감시자 리퍼";
					if (name != null && rnd < Lineage.grimreaper_spawn_probability) {
						this.toSender(new S_ObjectEffect(this, 4784), true);
						MonsterSpawnlistDatabase.changeMonsterRenew(this, MonsterDatabase.find(name));
						return;
					} else {
					}
					break;
				}
			}
		}
			// 파우스트 설정.
			if (nowHp <= 0 && getMonster().getFaust() != null && getMonster().getFaust().length() > 0) {
				Monster m = MonsterDatabase.find(getMonster().getFaust());
				if (m != null) {
					toSender(new S_ObjectEffect(this, 5483), false);
					MonsterSpawnlistDatabase.changeMonsterRenew(this, m);
					return;
				}
			}
	
		super.setNowHp(nowHp);
		if (!worldDelete && Lineage.monster_interface_hpbar && Lineage.server_version > 144) {
			for (object o : getInsideList(true)) {
				if (o instanceof PcInstance && containsAttackList(o))
					o.toSender(S_ObjectHitratio.clone(BasePacketPooling.getPool(S_ObjectHitratio.class), this, true));
			}
		}

		if (GameSetting.몬스터피바) {
			PcInstance gm = World.findGm();
			if (gm != null) {
				gm.toSender(S_ObjectHitratio.clone(BasePacketPooling.getPool(S_ObjectHitratio.class), this, true));
			}

		} else if (!GameSetting.몬스터피바) {
			PcInstance gm = World.findGm();
			if (gm != null) {
				gm.toSender(S_ObjectHitratio.clone(BasePacketPooling.getPool(S_ObjectHitratio.class), this, false));
			}

		}
	}

	/**
	 * 인공지능에 사용된 모든 변수 초기화. 객체를 스폰되기전 상태로 전환하는 함수. : 펫을 길들인후 뒷처리 함수로 사용함. : 펫
	 * 맡길때 제거용으로도 사용함. : 테이밍몬스터후 뒷처리함수로 사용함.
	 * 
	 * @param packet
	 *            : 패킷 처리 할지 여부.
	 */
	public void toAiClean(boolean packet) {
		// 인벤토리 제거.
		for (ItemInstance ii : inv.getList())
			ItemDatabase.setPool(ii);
		inv.clearList();
		// 경험치 지급 제거.
		clearExpList();
		// 전투 관련 변수 초기화.
		clearAttackList();
		// 버프제거
		toReset(true);
		// 객체관리목록 제거.
		World.remove(this);
		clearList(packet);
		// 스폰 준비상태로 변환.
		setAiStatus(4);
		ai_time_temp_1 = 0;
	}

	@Override
	public void setDead(boolean dead) {
		super.setDead(dead);
		if (isDead()) {
			ai_time = 0;
			setAiStatus(2);
		}
	}

	@Override
	public void toDamage(Character cha, int dmg, int type, Object... opt) {
		if (cha == null)
			return;

		if (cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha;

			if (pc.getChkspeed() == true)
				pc.setChkspeed(false);
		}

		// 서먼몬스터가 아닐때만.
		if (ai_status != 1 && summon == null) {
			// 전투상태가 아니엿을때 공격받으면 처리하기. 첫전투전환 시점으로 생각하면됨.
			// 일정 확률로 인벤에서 초록물약 찾아서 복용하기.
			// 아직 복용상태가 아닐때만
			if (getSpeed() == 0 && Util.random(0, 10) == 0) {
				ItemInstance ii = getInventory().findDbNameId(234);
				if (ii != null)
					ii.toClick(this, null);
			}
		}
		// 경험치 지급될 목록에 추가.
		if (dmg > 0)
			appendExp(cha, dmg);
		// 공격목록에 추가.
		addAttackList(cha);
		// 동족인식.
		try {
		if (mon.getFamily().length() > 0 && group_master == null) {
			for (object inside_o : getInsideList(true)) {
				if (inside_o.getObjectId() == 0)
					continue;
				if (inside_o instanceof MonsterInstance
						&& !(inside_o instanceof SummonInstance)) {
					MonsterInstance inside_mon = (MonsterInstance) inside_o;
					if (inside_mon.getMonster().getFamily()
							.equalsIgnoreCase(mon.getFamily())
							&& inside_mon.getGroupMaster() == null)
						inside_mon.addAttackList(cha);
				}
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 그룹 알림.
				for (MonsterInstance mi : group_list)
					mi.addAttackList(cha);
				if (group_master != null && group_master.getObjectId() != getObjectId()
						&& group_master.getName() != null)
					group_master.toDamage(cha, 0, type);
				// 손상 처리.
				if (mon.isToughskin() && type == 0 && cha.isBuffSoulOfFlame() == false) {
					if (PluginController.init(MonsterInstance.class, "toDamage.손상처리",
							cha) == null) {
						ItemInstance weapon = cha.getInventory().getSlot(8);
						if (weapon != null && weapon.getItem().isCanbedmg()
								&& Util.random(0, 100) < 7 && weapon.getBress() != 0) {
							weapon.setDurability(weapon.getDurability() + 1);
							if (Lineage.server_version >= 160)
								cha.toSender(S_InventoryStatus.clone(BasePacketPooling
										.getPool(S_InventoryStatus.class), weapon));
							cha.toSender(S_Message.clone(
									BasePacketPooling.getPool(S_Message.class), 268,
									weapon.toString()));
						}
					}
				}
				// 길찾기 무시할 목록에서 제거.
				removeAstarList(cha);
			}

	/**
	 * 경험치 지급목록 처리 함수.
	 * 
	 * @param cha
	 * @param dmg
	 */
	protected void appendExp(Character cha, int dmg) {
		if (dmg == 0)
			return;
		Exp e = findExp(cha);
		if (e == null) {
			e = ExpDatabase.getPool();
			e.setObject(cha);
			appendExpList(e);
		}

		// 연산
		double exp = add_exp;
		if (getNowHp() < dmg)
			exp *= getNowHp();
		else
			exp *= dmg;
		// 축적.
		e.setExp(e.getExp() + exp);
		e.setDmg(e.getDmg() + dmg);
	}

	@Override
	public void toGiveItem(object o, ItemInstance item, long count) {
		int bress = item.getBress();
		String name = item.getItem().getName();

		super.toGiveItem(o, item, count);

		// 넘겨받은 아이템 복용 처리. 1개일경우 바로 처리.
		if (count == 1) {
			ItemInstance temp = getInventory().find(name, bress, false);
			if (temp instanceof HastePotion
					|| temp instanceof HealingPotion
					|| temp instanceof CurePoisonPotion)
				temp.toClick(this, null);

		}
	}

	/**
	 * 공격자 목록에 등록처리 함수.
	 * 
	 * @param o
	 */
	public void addAttackList(object o) {
		if (!isDead() && !o.isDead() && o.getObjectId() != getObjectId()
				&& !group_list.contains(o)) {
			// 공격목록에 추가.
			appendAttackList(o);
		}
	}

	/* 최초 몬스터 생성시에 몬스터 인벤 만들기 */
	/*
	 * public void readDrop2() { if (inv == null) return; // 인벤토리 아이템 제거. for
	 * (ItemInstance ii : inv.getList()) ItemDatabase.setPool(ii);
	 * inv.clearList(); }
	 */

	/**
	 * 몬스터별 drop_list 정보를 참고해서 확률 연산후 인벤토리에 갱신.
	 */
	public void readDrop() {
		if(inv == null)
			return;
		// 인벤토리 아이템 제거.
		
		object first = this.findDangerousObject();
		
		for(ItemInstance ii : inv.getList())
			ItemDatabase.setPool(ii);
		inv.clearList();
		//if (!drop_append_status) {
		// 인벤토리에 드랍아이템 등록.
		for(Drop d : mon.getDropList()){
			Item item = ItemDatabase.find(d.getItemName());
			if(item != null){
				// 기본 찬스
				double chance = d.getChance()==0 ? item.getDropChance() : d.getChance();
					
				// 배율 적용.
				chance *= Lineage.rate_drop;
				// 체크.
				if(Util.random(0, Lineage.chance_max_drop) <= chance){
					ItemInstance ii = ItemDatabase.newInstance(item);
					if(ii != null) {
						ii.setBress(d.getItemBress());
						ii.setEnLevel(d.getItemEn());
						ii.setCount(Util.random(d.getCountMin()==0 ? 1 : d.getCountMin(), d.getCountMax()==0 ? 1 : d.getCountMax()));
						inv.append(ii, true);
						//getFirst_drop_list().add(ii);
					}
				}
			}
		}
//		}
		// 아덴 등록.
		if(mon.isAdenDrop()){
			int count = (int)(Util.random(mon.getMonAdenMin(), mon.getMonAdenMax()) * Lineage.rate_aden);
			
			// 신규 인형 소환시 아데나 1.5배 
			if(first != null && (first.isMagicdollroyal() || first.isMagicdollknight()
					|| first.isMagicdollelf()|| first.isMagicdollwizard()
					|| first.isMagicdollroyal2()|| first.isMagicdollknight2()
					|| first.isMagicdollelf2()|| first.isMagicdollwizard2()) 
					&& first instanceof PcInstance)
				count *= 1.5;
			
			if (count > 0) {
				ItemInstance aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
				if (aden != null) {
					aden.setObjectId(ServerDatabase.nextItemObjId());
					aden.setCount(count);
					inv.append(aden, true);
				}
			}
		}
	}

	/**
	 * 해당객체를 공격해도 되는지 분석하는 함수.
	 * 
	 * @param o
	 * @param walk
	 *            : 랜덤워킹 상태 체크인지 구분용
	 * @return
	 */
	protected boolean isAttack(object o, boolean walk) {
		if (o == null)
			return false;
		if (o.isDead())
			return false;
		if (o.isWorldDelete())
			return false;
		// if(o.getGm()>0)
		// return false;
		if (o.isTransparent())
			return false;
		if (o instanceof MagicDollInstance)
			return false;
		if (!Util.isDistance(this, o, Lineage.SEARCH_MONSTER_TARGET_LOCATION))
			return false;
		if (walk) {
			if (o.getGfx() != o.getClassGfx() && !mon.isAtkPoly())
				return false;
			// 우호도
			if (mon.getKarma() < 0 && o.getKarma() < 0)
				return false;
			if (mon.getKarma() > 0 && o.getKarma() > 0)
				return false;
		}
		// 특정 몬스터들은 굳은상태를 확인해서 무시하기.
		switch (mon.getNameIdNumber()) {
		case 962: // 바실리스크
		case 969: // 코카트리스
			if (o.isLockHigh())
				return false;
		}
		// -- 몬스터 인공지능 설정에서 변경하도록 하는게 좋을듯. 지금은 투망일때 무조건 무시하도록 설정.
		if (!mon.isAtkInvis() && o.isInvis())
			return false;
		// // 투망상태일경우 공격목록에 없으면 무시.
		// if(!mon.isAtkInvis() && o.isInvis())
		// // 동기화 할필요 없음. 동기화 한 상태로 들어옴.
		// return containsAttackList(o);

		if (this instanceof SummonInstance) {
			// 소환및 펫은 거의다..
			return true;
		} else {
			// 몬스터는 pc, sum, pet
			// 랜덤워킹중에 선인식 체크함. 랜덤워킹이라면 서먼몬스터는 제외.
			if (walk)
				return o instanceof PcInstance || o instanceof SummonInstance
						|| o instanceof PetInstance;
			else
				return o instanceof PcInstance || o instanceof SummonInstance;
		}
	}

	/**
	 * 픽업아이템인지 여부를 리턴.
	 * 
	 * @param o
	 * @return
	 */
	protected boolean isPickupItem(object o) {
		return o instanceof ItemInstance && !containsAstarList(o);
	}

	/**
	 * 매개변수 좌표로 A스타를 발동시켜 이동시키기. 객체가 존재하는 지역은 패스하도록 함. 이동할때마다 aStar가 새로 그려지기때문에
	 * 과부하가 심함.
	 */
	public void toMoving(object o, final int x, final int y, final int h,
			final boolean astar) {
		if (astar) {
			aStar.cleanTail();
			// 여기서 시작해서 헬스님꼐서 생각하시는 길찾기를 담당하는 주된지점이 어디라구 생각하셔용?
			// searchTail << 여기가 길찾기의 주된지점이에용 맞앗네요 ㅋ맞아용ㅋㅋㅋㅋ
			tail = aStar.searchTail(this, x, y, true);
			if (tail != null) {
				int count = 100;
				while (tail != null) {
					if (count-- < 0) {
						break;
					}
					// 현재위치 라면 종료
					if (tail.x == getX() && tail.y == getY())
						break;
					//
					iPath[0] = tail.x;
					iPath[1] = tail.y;
					tail = tail.prev;
				}
				toMoving(iPath[0], iPath[1],
						Util.calcheading(this.x, this.y, iPath[0], iPath[1]));
			} else {
				if (o != null) {
					// o 객체가 마스터일경우 휴식모드로 전환.
					if (this instanceof SummonInstance) {
						SummonInstance si = (SummonInstance) this;
						if (si.getSummon() != null
								&& si.getSummon().getMasterObjectId() == o
										.getObjectId()) {
							si.setSummonMode(SUMMON_MODE.Rest);
							return;
						}
					}
					// // 그외엔 에이스타 무시목록에 등록.
					// // aStar처리중 길을 못찾을때 객체에 길막으로 못찾은거라면 등록하지 않기.
					// if(aStar.Result == 1)
					appendAstarList(o);
					// else {
					// // 공격상태일경우 최대한 타켓과 근접하게 이동하기.
					// // aStar처리중 타켓과 가장 근접했던 좌료를 토대로 이동 처리.
					// // (이동이 가능했던 루트에 한해서.)
					// if(getAiStatus() == 1) {
					// if(this.x!=aStar.x && this.y!=aStar.y) {
					// aStar.cleanTail();
					// tail = aStar.searchTail(this, aStar.x, aStar.y, true);
					// if(tail != null){
					// while(tail != null){
					// // 현재위치 라면 종료
					// if(tail.x==getX() && tail.y==getY())
					// break;
					// //
					// iPath[0] = tail.x;
					// iPath[1] = tail.y;
					// tail = tail.prev;
					// }
					// toMoving(iPath[0], iPath[1], Util.calcheading(this.x,
					// this.y, iPath[0], iPath[1]));
					// }
					// }
					// }
					// }
				}
			}
		} else {
			toMoving(x, y, h);
		}
	}

	@Override
	public void toAttack(object o, int x, int y, boolean bow, int gfxMode,
			int alpha_dmg, boolean isTriple, int tripleIdx) {
			// 일반적인 공격mode와 다를경우 프레임값 재 설정.
			if (this.gfxMode + 1 != gfxMode)
				ai_time = SpriteFrameDatabase.find(gfx, gfxMode);
			int effect = bow ? mon.getArrowGfx() != 0 ? mon.getArrowGfx() : 66 : 0;
			int dmg = DamageController.getDamage(this, o, bow, null, null, alpha_dmg, isTriple, tripleIdx);
			DamageController.toDamage(this, o, dmg, 0);
			toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), this, o, gfxMode, dmg, effect, bow, true, 0, 0, true), false);

	}

	@Override
	public void toAi(long time) {
		switch (ai_status) {
		// 공격목록이 발생하면 공격모드로 변경
		case 0:
		case 6:
			if (isDead())
				setAiStatus(2);
			else {
				if (getAttackListSize() > 0)
					setAiStatus(1);
			}
			break;

		// 전투 처리부분은 항상 타켓들이 공격가능한지 확인할 필요가 있음.
		case 1:
		case 5:
			// 보스일경우 스폰위치에서 너무 벗어나면 스폰위치로 강제 텔레포트.
			if (getBoss() != null
					&& !Util.isDistance(x, y, map, homeX, homeY, homeMap,
							Lineage.SEARCH_ROBOT_TARGET_LOCATION)) {
				toTeleport(homeX, homeY, homeMap, true);
				this.setNowHp(this.getMaxHp());
			}
			if (isKingdomBoss()
					&& !Util.isDistance(x, y, map, homeX, homeY, homeMap, 12)) {
				toTeleport(homeX, homeY, homeMap, true);
				this.setNowHp(this.getMaxHp());
			}
			//
			for (object o : getAttackList()) {
				if (!isAttack(o, false) || containsAstarList(o)) {
					removeAttackList(o);
					Exp e = findExp(o);
					if (e != null) {
						removeExpList(e);
						ExpDatabase.setPool(e);
					}
				}
			}
			// 전투목록이 없을경우 랜덤워킹으로 변경.
			if (getAttackListSize() == 0)
				setAiStatus(0);
			break;
		}

		super.toAi(time);
	}

	@Override
	protected void toAiWalk(long time) {
		if (!SpriteFrameDatabase.findGfxMode(getGfx(), getGfxMode() + Lineage.GFX_MODE_WALK))
			return;
		
		super.toAiWalk(time);
		
		List<object> insideList = getInsideList();
		
		if (getMonster() != null) {
			boolean isWalk = false;
			// 주변에 유저가 있는지 검색.
			for (object o : insideList) {
				if (o instanceof PcInstance) {
					isWalk = true;
					break;
				}			
			}
			// 주변에 유저가 없을경우 움직이지 않음.
			if (!isWalk)
				return;
		}
		// 선인식 체크
		if (mon.getAtkType() == 1) {
			for (object o : getInsideList(true)) {
				if (isAttack(o, true)) {
					// 공격목록에 등록.
					if (!containsAstarList(o))
						addAttackList(o);
					// 그룹 알림.
					if (group_list != null) {
						for (MonsterInstance mi : group_list) {
							if (!mi.containsAstarList(o))
								mi.addAttackList(o);
						}
						if (group_master != null
								&& group_master.getObjectId() != getObjectId()
								&& o instanceof Character)
							group_master.toDamage((Character) o, 0, 3);
					}
				}
			}
		}

		// 아이템 줍기 체크
		if (getAttackListSize() == 0 && mon.isPickup() && group_master == null) {
			for (object o : getInsideList(true)) {
				if (isPickupItem(o)) {
					// 아이템이 발견되면 아이템줍기 모드로 전환.
					setAiStatus(6);
					return;
				}
			}
		}
		// 멘트
		toMent(time);

		// 아직 휴식카운팅값이 남앗을경우 리턴.
		if (ai_walk_stay_count-- > 0)
			return;

		// Astar 발동처리하다가 길이막혀서 이동못하던 객체를 모아놓은 변수를 일정주기마다 클린하기.
		if (Util.random(0, 5) == 0)
			clearAstarList();

		do {
			switch (Util.random(0, 7)) {
			case 0:
			case 1:
				ai_walk_stay_count = Util.random(3, 6);
				break;
			case 2:
			case 3:
				setHeading(getHeading() + 1);
				break;
			case 4:
			case 5:
				setHeading(getHeading() - 1);
				break;
			case 6:
			case 7:
				setHeading(Util.random(0, 7));
				break;
			}
			// 이동 좌표 추출.
			int x = Util.getXY(heading, true) + this.x;
			int y = Util.getXY(heading, false) + this.y;
			// 그룹 마스터로 지정된놈 추출.
			MonsterInstance master = getGroupMasterDynamic();
			// 스폰된 위치에서 너무 벗어낫을경우 스폰쪽으로 유도하기.
			if (master == null || master.getObjectId() == getObjectId() || group_master == null) {
				if (!Util.isDistance(x, y, map, homeX, homeY, homeMap, Lineage.SEARCH_LOCATIONRANGE)) {
					heading = Util.calcheading(this, homeX, homeY);
					x = Util.getXY(heading, true) + this.x;
					y = Util.getXY(heading, false) + this.y;
				}
				// 마스터가 존재하는 그룹몬스터일경우 마스터가 있는 좌표 내에서 왓다갓다 하기.
			} else {
				// 이동 범위는 마스터 목록에 등록된 순번+3으로 범위지정. 등록순번이 뒤에잇을수록 이동반경은 넓어짐.
				if (!Util.isDistance(x, y, map, master.getX(), master.getY(), master.getMap(), group_master.getGroupList().indexOf(this) + 3)) {
					heading = Util.calcheading(this, master.getX(), master.getY());
					x = Util.getXY(heading, true) + this.x;
					y = Util.getXY(heading, false) + this.y;
				}
			}
			// 해당 좌표 이동가능한지 체크.
			boolean tail = World.isThroughObject(this.x, this.y, this.map, heading) && (World.isMapdynamic(x, y, map) == false) && !World.isNotAttackTile(x, y, map);
			if (tail)
				// 타일이 이동가능하고 객체가 방해안하면 이동처리.
				toMoving(null, x, y, heading, false);	
		} while (false);

	}
			// 보스는 객체방해 조건 무시하기.
/*			boolean tail = World.isThroughObject(this.x, this.y, this.map,
					heading)
					&& (World.isMapdynamic(x, y, map) == false || getBoss() != null);
			if (tail) {
				// 타일이 이동가능하고 객체가 방해안하면 이동처리.
				toMoving(null, x, y, heading, false);
			} else {
				if (Util.random(0, 3) != 0)
					continue;
			}
		} while (false);
	}
*/
	/**
	 * 벽에 끼인 객체확인하여 텔레포트
	 */
	public boolean toAiBugCheck() {
		if (!World.isThroughObject(getX(), getY(), getMap(), 0) && !World.isThroughObject(getX(), getY(), getMap(), 1)
				&& !World.isThroughObject(getX(), getY(), getMap(), 2)
				&& !World.isThroughObject(getX(), getY(), getMap(), 3)
				&& !World.isThroughObject(getX(), getY(), getMap(), 4)
				&& !World.isThroughObject(getX(), getY(), getMap(), 5)
				&& !World.isThroughObject(getX(), getY(), getMap(), 6)
				&& !World.isThroughObject(getX(), getY(), getMap(), 7)) {

			// 좌표 스폰
			int lx = getX();
			int ly = getY();
			int loc = 1;
			int roop_cnt = 0;

			while (!World.isThroughObject(lx, ly + 1, getMap(), 0) || !World.isThroughObject(lx, ly - 1, getMap(), 4)
					|| !World.isThroughObject(lx - 1, ly, getMap(), 2)
					|| !World.isThroughObject(lx + 1, ly, getMap(), 6)
					|| !World.isThroughObject(lx - 1, ly + 1, getMap(), 1)
					|| !World.isThroughObject(lx + 1, ly - 1, getMap(), 5)
					|| !World.isThroughObject(lx + 1, ly + 1, getMap(), 7)
					|| !World.isThroughObject(lx - 1, ly - 1, getMap(), 3)) {
				lx = Util.random(getX() - loc, getX() + loc);
				ly = Util.random(getY() - loc, getY() + loc);

				if (roop_cnt++ > 100) {
					roop_cnt = 0;
					loc++;
					if (loc > 1) {
						lx = getHomeX();
						ly = getHomeY();
						break;
					}
				}
			}
			ai_walk_stay_count = 0;
			clearAstarList();
			toTeleport(lx, ly, getMap(), false);
			return false;
		}

		return true;
	}
	
	@Override
	public void toAiAttack(long time) {
		super.toAiAttack(time);
		
		try {
			// 몹몰이하는 애들때문에 넣을 수 없음.
			 if(!(this instanceof SummonInstance) && !Util.isDistance(getX(), getY(), getMap(), getHomeX(), getHomeY(), getHomeMap(), Lineage.SEARCH_WORLD_LOCATION * 2)) {
			 toTeleport(homeX, homeY, homeMap, true);
			 return;
			 }
			// 벽에 끼었을 경우 처리
			if (!toAiBugCheck())
				return;
			// 멘트
			toMent(time);

			// 공격자 확인.
			object o = findDangerousObject();
			// 객체를 찾지못했다면 무시.
			if (o == null)
				return;

			// 2거리 이상은 모두 활공격으로 판단.
			boolean bow = getAtkRange() > 2;
			//
			boolean blind = isBuffCurseBlind() && !Util.isDistance(this, o, 2);
			MonsterSkill skill = null;
			Monster mon = getMonster();
			if (mon != null && (mon.getNameIdNumber() == 1000 || mon.getNameIdNumber() == 1019)) {
				skill = findMagic();
				if (o != null && skill != null) {
					if (!blind && Util.getDistance(this, o) > 3 && !Util.isAreaAttack(this, o)) {
						long current = System.currentTimeMillis();
						if (lastSkillTime < current) {
							if (ShareMagic.init(this, o, skill, 0, 0, bow)) {
								lastSkillTime = current + 2000L;
								return;
							}
						}
					}
				}
			}
			// 객체 거리 확인
			if (Util.isDistance(this, o, getAtkRange()) && Util.isAreaAttack(this, o) && !blind) {
				// 공격 시전했는지 확인용.
				boolean is_attack = false;
				try {
					// 스킬 확인하기.
					for (MonsterSkill ms : mon.getSkillList()) {
						// 마법시전 시도.
						if (ShareMagic.init(this, o, ms, 0, 0, bow)) {
							// 시전 성공시 루프종료.
							is_attack = true;
							break;
						}
					}
				} catch (Exception e) {
					is_attack = false;
				}
				// 마법 시전이 실패됫을때.
				if (!is_attack) {
					if (Util.isDistance(this, o, mon.getAtkRange())) {
						// 물리공격 범위내로 잇을경우 처리.
						// if(getGfx()==66)
						// toAttack(o, 0, 0, mon.getAtkRange()>2, 20 + 1, 0, false, 0);
						// else
						toAttack(o, 0, 0, mon.getAtkRange() > 2, gfxMode + 1, 0, false, 0);
					} else {
						// 객체에게 접근.
						ai_time = SpriteFrameDatabase.find(gfx, gfxMode + 0);
						toMoving(o, o.getX(), o.getY(), 0, true);
					}
				}
			} else {
				ai_time = SpriteFrameDatabase.find(gfx, gfxMode + 0);
				// 객체 이동
				if (blind) {
					if (Util.random(0, 2) == 0)
						toMoving(null, o.getX() + Util.random(0, 1), o.getY() + Util.random(0, 1), 0, true);
				} else {
					toMoving(o, o.getX(), o.getY(), 0, true);
				}
			}

			// 일정확률로 인벤에 있는 체력회복물약 복용하기.
			if (Util.random(0, 5) == 0 && !(this instanceof SummonInstance)) {
				// 현재 hp의 백분율값 추출.
				int now_percent = (int) (((double) getNowHp() / (double) getTotalHp()) * 100);
				// 복용해야한다면
				if (Lineage.ai_auto_healingpotion_percent >= now_percent) {
					// 찾고
					ItemInstance ii = getInventory().find(HealingPotion.class);
					// 복용
					if (ii != null)
						ii.toClick(this, null);
				}
			}
		} catch (Exception e) {
			//System.out.println("toAiAttack(long time) 2 : " + e + " : " + test);
			// 전투 관련 변수 초기화.
			clearExpList();
			clearAttackList();
			clearAstarList();
		}
	}

	@Override
	protected void toAiDead(long time) {
		super.toAiDead(time);

		int pc_count = 0; // 아이템 지급해야할 유저갯수.성환
		int _mapId = (int) getMap();
		PcInstance tempPc = null;

		
		long pc_objectId = 0;
		// new 방식이라면 가격자 체크해서 땅에 드랍처리할지 자동픽업 처리할지 확인함.
		for (Exp e : getExpList()) {
			long id = 0;
			if (e.getObject() instanceof PcInstance)
				id = e.getObject().getObjectId();
			if (e.getObject() instanceof SummonInstance)
				id = e.getObject().getOwnObjectId();
			if (id == 0)
				continue;
			if (pc_objectId == 0 || pc_objectId != id) {
				pc_count += 1;
				pc_objectId = id;
		
			}
		}

		// 20180320
		/*
		 * /if(getBoss()!=null){ readDrop(); }
		 */
		if (this instanceof SummonInstance || this instanceof PetInstance
				|| getMonster().getName().equalsIgnoreCase("브롭")) {

		} else {
			readDrop();
		}
		//
		PluginController.init(MonsterInstance.class, "toAiDead", this, time,
				pc_count, pc_objectId);
		// 멘트
		toMent(time);
		// 일일퀘스트 처리.
		// 20190120 일퀘 몬데드
		QuestTodayController.toMonsterDead(this);
		if (Lineage.level_max <= 5) {

		} else {

			// 아이템 드랍
			for (ItemInstance ii : inv.getList()) {

				if (ii.isEquipped())
					ii.toClick(this, null);
				//
				boolean add = false;// 원래 false 니까 지급됬으면 true로 바꾸구 건너 뛰어라
									// 이런이야기죵아하

				// 오토루팅 처리.
				if (isAutoPickup(ii)) { // 오토 루팅일때
					int cnt = 0;
					if (ii.getItem().getName().equalsIgnoreCase("아데나")) {
						// 아데나는 파티원에게 균등분배하기
						// 아이템이 지급되었다고 선언하기위해 카운트 +1
						cnt += 1;
						// 타격자중에 가장 먼저 타격한 사람을 설정.
						Exp e = null;
						// 타격자를 못잡나??
						for (int i = 0; i < 100; ++i) {
							if (getExpList(i) != null
									&& isAutoPickupItem(getExpList(i)
											.getObject())) {
								e = getExpList(i);
								break;
							}
						}

						if (e != null && e.getObject() != null
								&& isAutoPickupItem(e.getObject())) {
							Character cha = e.getObject() instanceof SummonInstance ? World
									.findPc(e.getObject().getOwnName())
									: (Character) e.getObject();
							// 타격자가 있을경우에
							if (cha != null && isAutoPickupItem(cha)) {
								if (cha instanceof PcInstance) {
									// 타격자의 파티를 검색
									PcInstance pc = (PcInstance) cha;
									Party p = PartyController.find2(pc);
									// 파티가 존재할경우
									if (p != null) {
										// 본인을 포함해야하기때문에 1부터 시작
										int partyMember = 1;
										for (PcInstance pc2 : p.getList()) {
											// 파티원이 타격자와 다르고 존재하며 12픽셀 안에 있을경우
											if (pc2 != pc
													&& pc2 != null
													&& Util.isDistance(pc, pc2,
															12)) {
												// 실제파티원이 더해짐
												partyMember += 1;
											}
										}
										// 새로운 아이템을 몬스터에게 넣어 파티원에게 주기위한 아이템 생성
										Item i = ItemDatabase.find("아데나");
										// 기존의 아데나를 먼저 파티 인원수만큼 나눔

										ii.setCount(ii.getCount() / partyMember);
										// 파티원 개인당 얻어야하는 갯수를 기록
										long count = ii.getCount();
										// 파티리스트만큼 반복
										for (PcInstance p2 : p.getList()) {
											ItemInstance iii = ItemDatabase
													.newInstance(i);
											iii.setCount(count);
											// 파티원이 타격자와 다르고 존재하며 12픽셀 안에 있을경우
											if (p2 != pc
													&& p2 != null
													&& Util.isDistance(pc, p2,
															12)) {
												// 새로운 아데나를 생성 몬스터인벤에 등록

												if (iii.getObjectId() == 0)
													iii.setObjectId(ServerDatabase
															.nextItemObjId());

												inv.append(iii, true);
												// 파티원에게 지급
												if (p2.isAutoPickup()
														&& toAutoPickupItem(p2,
																iii)) {
													add = true;
												}
											}
										}
										// 타격자를 마지막으로 지급
										if (pc != null && pc.isAutoPickup()
												&& toAutoPickupItem(pc, ii)) {
											add = true;
										}
										// 파티가 있을경우로 바꿔 아데나가 씹히는 경우를 막는다.
										// 아데나 지급을 마친 후엔 건너 뛰어서 중복을 막는다
										continue;
									}
								}
							}
						}
					}

					if (!Lineage.monster_item_drop && !add) {
						// 지급하지 못햇을경우 랜덤으로 아무에게나 주기.
						cnt += 1;
						for (int i = 0; i < 100; ++i) {
							Exp e = getExpList(Util.random(0,
									expList.size() - 1));
							if (e != null && e.getObject() != null
									&& isAutoPickupItem(e.getObject())) {
								Character cha = e.getObject() instanceof SummonInstance ? World
										.findPc(e.getObject().getOwnName())
										: (Character) e.getObject();
								if (cha == null)
									cha = World.findRobot(e.getObject()
											.getOwnName());
								if (cha != null && cha.isAutoPickup()
										&& toAutoPickupItem(cha, ii)) {
									add = true;
									break;
								}
								if (cnt >= 100)
									break;
							}
						}
					}
				}
			
				// 땅에 드랍.
				// 여기서 중복이되기때문이에요 아하
				if (!add) {
					if (ii.getObjectId() == 0)
						ii.setObjectId(ServerDatabase.nextItemObjId());
					int x = Util.random(getX() - 1, getX() + 1);
					int y = Util.random(getY() - 1, getY() + 1);

					if (World.isThroughObject(x, y + 1, map, 0))
						ii.toTeleport(x, y, map, false);
					else
						ii.toTeleport(this.x, this.y, map, false);
					// 드랍됫다는거 알리기.
					ii.toDrop(this);
				}

				// 착용 안되어있으면
			}
		}
			inv.clearList();
			// 경험치 지급
			double total_dmg = 0;
			for (Exp e : getExpList())
				total_dmg += e.getDmg();
			for (object oo : getAttackList()) {
				Exp e = findExp(oo);
				if (e != null) {
				// 데미지를 준만큼 경험치 설정.
				double percent = e.getDmg() / total_dmg;
				e.setExp(getMonster().getExp() * percent);
				//
				if (e.getObject() instanceof Character) {
					Character cha = (Character) e.getObject();
					// 화면안에 존재할 경우에만 경험치 지급.
					if (Util.isDistance(this, cha, 14)) {// Lineage.SEARCH_LOCATIONRANGE))
															// {
						// 파티 경험치 처리. 실패하면 혼자 독식.
						if (!PartyController.toExp(cha, this, e.getExp())
								&& !cha.isDead()) {
							// 유저와 펫,로봇 만 처리.
							if (cha instanceof PcInstance
									|| cha instanceof PetInstance) {
								// 경험치 지급.
								cha.toExp(this, e.getExp());
								// 라우풀 지급.
								double lawful = Math
										.round(((getLevel() * 3) / 2)
												* Lineage.rate_lawful);
								if (getMonster().getLawful() > Lineage.NEUTRAL)
									lawful = ~(int) lawful + 1;
								cha.setLawful(cha.getLawful() + (int) lawful);
								// 킬수 증가.
								cha.setMonsterKillCount(cha
										.getMonsterKillCount() + 1);
							}
						}
					
						if (Lineage.is_auto_hunt_check && cha.getGm() == 0
								&& cha instanceof PcInstance)
							AutoHuntCheckController.addCount((PcInstance) cha);
					}
				}
				// 데미지 준만큼 카르마 설정.
				if (getMonster().getKarma() != 0
						&& e.getObject() instanceof PcInstance) {
					PcInstance pc = (PcInstance) e.getObject();
					pc.setKarma(pc.getKarma()
							+ (getMonster().getKarma() * percent));
				}

				/*if (getBoss() != null && e.getObject() instanceof PcInstance) {
					PcInstance pc = (PcInstance) e.getObject();
					if (pc != null) {
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("보스 상자"));
						if (!toAutoPickupItem((PcInstance) pc, ii))
							ItemDatabase.setPool(ii);
					}
				}*/
			}

			// 라바던전 흑마법가루 및 봉인된 역사서 지급하기.
			if (Lineage.last_dungeon) {
				if (getMap() >= 451 && getMap() <= 457) {
					int chance = Util.random(0, Lineage.last_dungeon1);
					if (chance <= 1) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 1장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}

					} else if (chance <= 2) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 2장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 3) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 3장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 4) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 4장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 5) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 5장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 6) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 6장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 7) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 7장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 8) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 8장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 18) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase.find("흑마법 가루"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					}
				} else if (getMap() >= 460 && getMap() <= 468) {

					int chance = Util.random(0, Lineage.last_dungeon2);
					if (chance <= 1) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 1장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 2) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 2장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 3) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 3장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 4) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 4장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 5) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 5장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 6) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 6장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 7) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 7장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 8) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 8장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 18) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase.find("흑마법 가루"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					}

				} else if (getMap() >= 470 && getMap() <= 478) {

					int chance = Util.random(0, Lineage.last_dungeon3);
					if (chance <= 1) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 1장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 2) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 2장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 3) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 3장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 4) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 4장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 5) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 5장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 6) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 6장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 7) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 7장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 8) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase
											.find("봉인된 역사서 8장"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					} else if (chance <= 18) {
						object o = getAttackList(0);
						if (o != null) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase.find("흑마법 가루"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					}

				}
			}
			
			// 크리스마스 이벤트 양말 지급.
			if (Lineage.event_christmas && getAttackListSize() > 0) {
				int c = Util.random(0, 60);
				if (c <= 8) {
					object o = getAttackList(0);
					if (o != null) {
						if (o instanceof PcInstance
								&& !(o instanceof RobotInstance)) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase.find("빨간색 양말"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					}
				} else if (c <= 10) {
					object o = getAttackList(0);
					if (o != null) {
						if (o instanceof PcInstance
								&& !(o instanceof RobotInstance)) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase.find("녹색 양말"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					}
				} else if (c <= 11) {
					object o = getAttackList(0);
					if (o != null) {
						if (o instanceof PcInstance
								&& !(o instanceof RobotInstance)) {
							ItemInstance ii = ItemDatabase
									.newInstance(ItemDatabase.find("황금색 양말"));
							if (!toAutoPickupItem((PcInstance) o, ii))
								ItemDatabase.setPool(ii);
						}
					}
				}
			}
			// 수렵이벤트 추가
			if (Lineage.event_mon && getAttackListSize() > 0) {
							if (getMonster().getName().equalsIgnoreCase("토끼")
									|| getMonster().getName().equalsIgnoreCase("사슴")
									|| getMonster().getName().equalsIgnoreCase("멧돼지")
									|| getMonster().getName().equalsIgnoreCase("곰")
									|| getMonster().getName().equalsIgnoreCase("여우")) {
								
								int rand = Util.random(0, 100);
								object o = getAttackList(0);
								if (rand < 1) {
								if (o != null && o instanceof PcInstance && !(o instanceof RobotInstance)) {
										ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("변신 주문서"));
										ii.setBress(0);
										if (!toAutoPickupItem((PcInstance) o, ii))
											ItemDatabase.setPool(ii);
								}
								} else if (rand < 3) {
									if (o != null && o instanceof PcInstance && !(o instanceof RobotInstance)) {
											ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("무기 마법 주문서"));
											ii.setBress(0);
											if (!toAutoPickupItem((PcInstance) o, ii))
												ItemDatabase.setPool(ii);
										}
								} else if (rand < 5) {
									if (o != null && o instanceof PcInstance && !(o instanceof RobotInstance)) {
											ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("갑옷 마법 주문서"));
											ii.setBress(0);
											if (!toAutoPickupItem((PcInstance) o, ii))
												ItemDatabase.setPool(ii);
										}
								} else if (rand < 8) {
									if (o != null && o instanceof PcInstance && !(o instanceof RobotInstance)) {
											ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("무기 마법 주문서"));
											ii.setBress(2);
											if (!toAutoPickupItem((PcInstance) o, ii))
												ItemDatabase.setPool(ii);
										}
								
								} else if (rand < 11) {
									if (o != null && o instanceof PcInstance && !(o instanceof RobotInstance)) {
											ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("갑옷 마법 주문서"));
											ii.setBress(2);
											if (!toAutoPickupItem((PcInstance) o, ii))
												ItemDatabase.setPool(ii);
										}
								} else if (rand < 20) {
									if (o != null && o instanceof PcInstance && !(o instanceof RobotInstance)) {
											ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("무기 마법 주문서"));
											ii.setBress(1);
											if (!toAutoPickupItem((PcInstance) o, ii))
												ItemDatabase.setPool(ii);
										}
								} else if (rand < 30) {
									if (o != null && o instanceof PcInstance && !(o instanceof RobotInstance)) {
											ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("갑옷 마법 주문서"));
											ii.setBress(1);
											if (!toAutoPickupItem((PcInstance) o, ii))
												ItemDatabase.setPool(ii);
										}

								} else if (rand < 50) {
									if (o != null && o instanceof PcInstance && !(o instanceof RobotInstance)) {
											ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("변신 주문서"));
											ii.setBress(1);
											if (!toAutoPickupItem((PcInstance) o, ii))
												ItemDatabase.setPool(ii);
										}
								} else if (rand < 70) {
									if (o != null && o instanceof PcInstance && !(o instanceof RobotInstance)) {
											ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("순간이동 주문서"));
											ii.setBress(1);
											if (!toAutoPickupItem((PcInstance) o, ii))
												ItemDatabase.setPool(ii);
										}
								} else {
									if (o != null && o instanceof PcInstance && !(o instanceof RobotInstance)) {
											ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("고기"));
											ii.setBress(1);
											if (!toAutoPickupItem((PcInstance) o, ii))
												ItemDatabase.setPool(ii);
										}
									}
								}
							}
			
			//몬스터 퀘스트
			if (oo instanceof PcInstance) {
				PcInstance pc = (PcInstance) oo;
				int questChapter = pc.getQuestChapter();
				int rquestChapter = pc.getRadomQuest();
				int map = pc.getMap();
				switch (questChapter) {
				case 1:
					if (map == 32) {
						((PcInstance) oo).setQuestKill(((PcInstance) oo).getQuestKill() + 1);
					}
					break;
				case 2:
					if (map == 33) {
						((PcInstance) oo).setQuestKill(((PcInstance) oo).getQuestKill() + 1);
					}
					break;
				case 3:
					if (map == 35) {
						((PcInstance) oo).setQuestKill(((PcInstance) oo).getQuestKill() + 1);
					}
					break;
				case 4:
					if (map == 36) {
						((PcInstance) oo).setQuestKill(((PcInstance) oo).getQuestKill() + 1);
					}
					break;
				}
				switch (rquestChapter) {
				case 1:
					if (map == 11) {
						((PcInstance) oo).setRandomQuestkill(((PcInstance) oo).getRandomQuestkill() + 1);
					}
					break;
				case 2:
					if (map == 12) {
						((PcInstance) oo).setRandomQuestkill(((PcInstance) oo).getRandomQuestkill() + 1);
					}
					break;
				case 3:
					if (map == 13) {
						((PcInstance) oo).setRandomQuestkill(((PcInstance) oo).getRandomQuestkill() + 1);
					}
					break;
				case 4:
					if (map == 35) {
						((PcInstance) oo).setRandomQuestkill(((PcInstance) oo).getRandomQuestkill() + 1);
					}
					break;
				case 5:
					if (map == 36) {
						((PcInstance) oo).setRandomQuestkill(((PcInstance) oo).getRandomQuestkill() + 1);
					}
					break;
				case 6:
					if (map == 27) {
						((PcInstance) oo).setRandomQuestkill(((PcInstance) oo).getRandomQuestkill() + 1);
					}
					break;
				case 7:
					if (map == 28) {
						((PcInstance) oo).setRandomQuestkill(((PcInstance) oo).getRandomQuestkill() + 1);
					}
					break;
				case 8:
					if (map == 63) {
						((PcInstance) oo).setRandomQuestkill(((PcInstance) oo).getRandomQuestkill() + 1);
					}
					break;
				}
			}
			//ExpDatabase.setPool(e);
			}

		if (Lineage.monster_boss_dead_message && isBoss() ) {
			String msg = null;
			object o = getAttackList(0);
			if (getAttackList().size() > 0) {
				if (o != null && o instanceof PcInstance  ) {
					if (getAttackList().size() > 1)
						msg = String.format("%s (살해자: %s외 %d명) %s", Util.getMapName(this), getAttackList(0).getName(),
								getAttackList().size() - 1, getMonster().getName());
					else
						msg = String.format("%s (살해자: %s) %s", Util.getMapName(this), getAttackList(0).getName(),
								getMonster().getName());
				}
			} else {
				msg = Util.getMapName(this) + " " + getMonster().getName();
			}

			BossController.toWorldOut(this);
			World.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1415, msg));
		}
		
		ai_time_temp_1 = 0;
		// 전투 관련 변수 초기화.
		clearExpList();
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
		if (summon != null) {
			if (this instanceof PetInstance
					&& ai_time_temp_1 + Lineage.ai_pet_corpse_time > time)
				return;
			if (this instanceof SummonInstance) {
				SummonInstance si = (SummonInstance) this;
				// 요정이 소환한 정령이라면 바로 소멸.
				// 그외엔 시체 유지.
				if (si.isElemental() == false
						&& ai_time_temp_1 + Lineage.ai_summon_corpse_time > time)
					return;
				
			}
			
		} else {
			if (this instanceof MonsterInstance
					&& ai_time_temp_1 + Lineage.ai_corpse_time > time)
				return;
		}

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
	protected void toAiSpawn(long time) {
		super.toAiSpawn(time);

		// 스폰 유지딜레이 값 초기화.
		if (ai_time_temp_1 == 0)
			ai_time_temp_1 = time;
		// 그룹몬스터쪽에 그룹원들이 스폰할 상태인지 확인. 아닐경우 딜레이 시키기.
		if (group_master != null) {
			if (group_master.getObjectId() != getObjectId()) {
				if (ai_time_temp_1 != 1)
					ai_time_temp_1 = time;
			} else {
				if (getGroupMasterDynamic() != null)
					ai_time_temp_1 = time;
			}
		}
		// 스폰 대기.
		if (ai_time_temp_1 + reSpawnTime > time) {

		} else {
			// 리스폰값이 정의되어 있지않다면 재스폰 할 필요 없음.
			// 서먼몬스터에서도 이걸 호출함.
			if (reSpawnTime == 0) {
				//
				toAiThreadDelete();
			} else {
				// 스폰할 위치 세팅.
				if (monSpawn != null)
					MonsterSpawnlistDatabase.toSpawnMonster(this,
							World.get_map(homeMap), monSpawn.isRandom(),
							monSpawn.getX(), monSpawn.getY(), map,
							monSpawn.getLocSize(), monSpawn.getReSpawn(),
							false, false, false, false);

				// 그룹몬스터 마스터가 스폰한다면 해당 그룹원들도 스폰시키기.
				if (group_master != null
						&& group_master.getObjectId() == getObjectId()) {
					for (MonsterInstance mi : group_master.getGroupList())
						mi.setAiTimeTemp1(1);
				}

				mon = monOld;
				// 상태 변환
				setDead(false);
				setGfx(getMonster().getGfx());
				setGfxMode(getMonster().getGfxMode());
				setClassGfx(getMonster().getGfx());
				setClassGfxMode(getMonster().getGfxMode());
				setName(getMonster().getNameId());
				setLevel(getMonster().getLevel());
				setExp(getMonster().getExp());
				setMaxHp(getMonster().getHp());
				setMaxMp(getMonster().getMp());
				setNowHp(getMonster().getHp());
				setNowMp(getMonster().getMp());
				setLawful(getMonster().getLawful());
				setEarthress(getMonster().getResistanceEarth());
				setFireress(getMonster().getResistanceFire());
				setWindress(getMonster().getResistanceWind());
				setWaterress(getMonster().getResistanceWater());
				// 스폰
				if (group_master == null) {
					MonsterSpawnlistDatabase.toSpawnMonster(this, World
							.get_map(getMap()), getMonsterSpawnlist()
							.isRandom(), getMonsterSpawnlist().getX(),
							getMonsterSpawnlist().getY(), getMap(),
							getMonsterSpawnlist().getLocSize(),
							getMonsterSpawnlist().getReSpawn(), false, false,
							false, false);
					toTeleport(getHomeX(), getHomeY(), getHomeMap(), false);
				} else
					toTeleport(group_master.getX(), group_master.getY(),
							group_master.getMap(), false);
				// 멘트
				toMent(time);
				// 상태 변환.
				setAiStatus(0);
			}
		}

	}

	@Override
	public void toAiEscape(long time) {
		super.toAiEscape(time);
		// 멘트
		toMent(time);

		// 전투목록에서 가장 근접한 사용자 찾기.
		object o = null;
		for (object oo : getAttackList()) {
			if (o == null)
				o = oo;
			else if (Util.getDistance(this, oo) < Util.getDistance(this, o))
				o = oo;
		}

		// 못찾앗을경우 무시. 가끔생길수 잇는 현상이기에..
		if (o == null) {
			setAiStatus(Lineage.AI_STATUS_WALK);
			return;
		}

		// 반대방향 이동처리.
		setHeading(Util.oppositionHeading(this, o));
		int temp_heading = getHeading();
		do {
			// 이동 좌표 추출.
			int x = Util.getXY(getHeading(), true) + getX();
			int y = Util.getXY(getHeading(), false) + getY();
			// 해당 좌표 이동가능한지 체크.
			boolean tail = World.isThroughObject(getX(), getY(), getMap(), getHeading()) && (World.isMapdynamic(x, y, getMap()) == false || getBoss() != null);
			if (tail) {
				// 타일이 이동가능하고 객체가 방해안하면 이동처리.
				toMoving(null, x, y, getHeading(), false);
				break;
			} else {
				setHeading(getHeading() + 1);
				if (temp_heading == getHeading())
					break;
			}
		} while (true);
	}

	@Override
	protected void toAiPickup(long time) {
		// 가장 근접한 아이템 찾기.
		object o = null;
		for (object oo : getInsideList(true)) {
			if (isPickupItem(oo)) {
				if (o == null)
					o = oo;
				else if (Util.getDistance(this, oo) < Util.getDistance(this, o))
					o = oo;
			}
		}
		// 못찾앗을경우 다시 랜덤워킹으로 전환.
		if (o == null) {
			setAiStatus(0);
			// 소환객체라면 아이템 줍기 완료 상태로 전환.
			if (this instanceof SummonInstance)
				((SummonInstance) this)
						.setSummonMode(SUMMON_MODE.ItemPickUpFinal);
			return;
		}

		// 객체 거리 확인
		if (Util.isDistance(this, o, 0)) {
			super.toAiPickup(time);
			// 줍기
			synchronized (o.sync_pickup) {
				if (o.isWorldDelete() == false)
					inv.toPickup(o, o.getCount());
			}
		} else {
			ai_time = SpriteFrameDatabase.find(gfx, gfxMode + 0);
			// 아이템쪽으로 이동.
			toMoving(o, o.getX(), o.getY(), 0, true);
		}
	}

	/**
	 * 오토루팅 처리시 처리해도 되는 객체인지 확인하는 함수. : 중복코드 방지용.
	 * 
	 * @param o
	 * @return
	 */
	private boolean isAutoPickupItem(object o) {
		if (o instanceof PcInstance) {
			/* PcInstance pc = (PcInstance)o; */
			// 죽지않았고, 케릭터면서, 범위안에 있을경우.
			return !o.isDead()
					&& Util.isDistance(o, this,
							Lineage.SEARCH_MONSTER_TARGET_LOCATION);
		} else {
			return false;
		}
	}

	private boolean toAutoPickupItem(Character cha, ItemInstance ii) {
		// 로그 확인을 위한 아이템
		String i2 = ii.getItem().getName();
		int bress = ii.getBress();
		// 소환객체는 무시.
		if (this instanceof SummonInstance)
			return false;
		//
		if (cha.isAutoPickup() && cha.getInventory().isAppend(ii, ii.getCount(), false) || ii.getItem().getNameIdNumber() == 4) {
			try {
				if (ii.getItem().getNameIdNumber() == 4) {
					ii.setCount(Math.round(ii.getCount() * cha.getAden()));
				}

				// 성혈맹원이라면 자동픽업될 아이템 량 증가.
				if (cha instanceof PcInstance && KingdomController.find((PcInstance) cha) != null)
					ii.setCount(Math.round(ii.getCount() * Lineage.kingdom_item_count_rate));
				
				// new 불멸의가호
				if (ii != null && ii.getItem() != null && ii.getItem().getName() != null && ii.getItem().getName().equalsIgnoreCase("아데나")) {

					boolean 불멸의가호 = false;
					if (cha != null && cha.getInventory() != null)
						불멸의가호 = cha.getInventory().find(Lineage.immortality_item_name3) == null ? false : true;

					// 추가 아데나
					if (Lineage.immortality_aden_percent > 0 && 불멸의가호) {
						long tempCount = ii.getCount();
						tempCount = (long) (tempCount + (tempCount * Lineage.immortality_aden_percent));

						ii.setCount(tempCount);
					}
				}

				// 메세지 변수.
				String msg = ii.toString();
				String msg_db = ii.toStringDB();
				// 혹시모를 처리변수가 있을지 모르기때문에 아래와같이 패턴 적용.
				ii.toDrop(this);
				ii.toPickup(cha);// 일단 숙제검사
				// 지급.
				ItemInstance temp = cha.getInventory().find(ii.getItem().getName(), ii.getBress(), ii.getItem().isPiles());
				if (temp != null) {
					if (ii.getItem().getName().equalsIgnoreCase("흑단 막대")) {
						temp.setQuantity(temp.getQuantity() + ii.getQuantity());
						cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), temp));
						ItemDatabase.setPool(ii);
					} else {
						//
						cha.getInventory().count(temp, ii, temp.getCount() + ii.getCount(), true);
						ItemDatabase.setPool(ii);
					}

				} else {
					if (ii.getObjectId() == 0)
						ii.setObjectId(ServerDatabase.nextItemObjId());
					cha.getInventory().append(ii, true);
				}

				//
				if (Lineage.party_autopickup_item_print && cha instanceof PcInstance) {
					try {
						PcInstance pc = (PcInstance) cha;
						Party p = PartyController.find2(pc);
						if (p != null) {

							msg = String.format("%s 님께서 %s 를 획득 하였습니다..", cha.getName(), msg_db);
							// }
							// 아이템이 굴러들어오면
							boolean send = false; // 채팅을 보냈는지 확인한다.
							// 파티원마다 설정한 채팅의 색상이 달라야하기에 파티원마다 다르게 보내준다.
							for (PcInstance use : p.getList()) {
								// 최대 4번 까지 설정가능하기에 1 ~ 4까지를 확인
								for (int n = 1; n < 5; n++) {
									// 축복받은, 저주받은, 일반의 아이템이름 을 추출
									String name = use.getPickItem(n);
									// 이름이 존재할때
									if (name != null) {
										// 축복여부를 추출
										int bressi = name.contains("축복받은") ? 0 : name.contains("일반의") ? 1 : 2;
										// name 에 붙은 수식어를 제거
										String itemname = bressi == 0 ? name.replace("축복받은", "") : bressi == 1 ? name.replace("일반의", "") : name.replace("저주받은", "");
										// 조건에 맞는 아이템이 있다면
										if (i2 != null && i2.equalsIgnoreCase(itemname) && bress == bressi) {
											// 설정된 메세지 종류가 있다면 나한테는 다르게 나오게 한다.
											p.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, 11, use.getPickColor(n) + msg), use, true, true);
											send = true;
										}
									}
								}
								if (!send) { // 없다면 일반적으로 표시한다.
									if (use.isAuto_party_message())
										p.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, 11, msg), use, true, true);
								}
							}
							// 파티원 개개인에게 보내고 빠져나왔으면 ?
							// 다시 보낼필요는 없다..
							// 파티원 전체에게 보내면 중복이 있을 수 있으니 일반적으로 보낸당
							// p.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),
							// null,
							// 11, msg), pc, true, false);
							return true;
						}
					} catch (Exception e) {// 잠시만
						lineage.share.System.println(MonsterInstance.class + " : toAutoPickupItem(Character cha, ItemInstance ii)");
						lineage.share.System.println(e);
						e.printStackTrace();
					}
				}
				// \f1%0%s 당신에게 %1%o 주었습니다.
				if (cha.isAutoPickupMessage()) {
					PcInstance pc = (PcInstance) cha;
					boolean send = false; // 채팅을 보냈는지 확인한다.
					for (int n = 1; n < 5; n++) {
						// 축복받은, 저주받은, 일반의 아이템이름 을 추출
						String name = pc.getPickItem(n);
						// 이름이 존재할때
						if (name != null) {
							// 축복여부를 추출
							int bressi = name.contains("축복받은") ? 0 : name .contains("일반의") ? 1 : 2;
							// name 에 붙은 수식어를 제거
							String itemname = bressi == 0 ? name.replace("축복받은", "") : bressi == 1 ? name.replace("일반의", "") : name.replace("저주받은", "");
							// 조건에 맞는 아이템이 있다면
							if (i2 != null && i2.equalsIgnoreCase(itemname) && bress == bressi) {
								// 설정된 메세지 종류가 있다면 나한테는 다르게 나오게 한다.
								cha.toSender(S_Message.clone(BasePacketPooling .getPool(S_Message.class), 143, getName(), pc.getPickColor(n) + msg));
								send = true;
							}
						}
					}
					if (!send)
						cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 143, getName(), msg));
				}
				return true;
			} catch (Exception e) {
				lineage.share.System.printf("%s.toAutoPickupItem(Character cha, ItemInstance ii)\r\n : %s\r\n", MonsterInstance.class.toString(), e.toString());
			}
		}
		return false;
	}

	/**
	 * 인공지능 처리구간에서 주기적으로 호출됨. : 상태에 맞는 멘트를 구사할지 여부 확인하여 표현 처리하는 함수.
	 * 
	 * @param time
	 *            : 진행되고있는 시간.
	 */
	protected void toMent(long time) {
		try {
			int msgTime = -1;
			int msgSize = 0;
			List<String> msgList = null;
			switch (ai_status) {
			case 0:
				msgTime = mon.getMsgWalkTime();
				msgSize = mon.getMsgWalk().size();
				msgList = mon.getMsgWalk();
				break;
			case 1:
				msgTime = mon.getMsgAtkTime();
				msgSize = mon.getMsgAtk().size();
				msgList = mon.getMsgAtk();
				break;
			case 2:
				msgTime = mon.getMsgDieTime();
				msgSize = mon.getMsgDie().size();
				msgList = mon.getMsgDie();
				break;
			case 4:
				msgTime = mon.getMsgSpawnTime();
				msgSize = mon.getMsgSpawn().size();
				msgList = mon.getMsgSpawn();
				break;
			case 5:
				msgTime = mon.getMsgEscapeTime();
				msgSize = mon.getMsgEscape().size();
				msgList = mon.getMsgEscape();
				break;
			}

			// 멘트를 표현할 수 있는 디비상태일경우.
			if (msgTime != -1 && msgSize > 0 && msgList != null) {
				if (msgTime == 0) {
					// 한번만 처리하기.
					if (!ai_showment) {
						toSender(S_ObjectChatting.clone(BasePacketPooling
								.getPool(S_ObjectChatting.class), this, 0x02,
								msgList.get(0)), false);
						ai_showment = true;
					}
				} else {
					// 시간마다 표현하기.
					if (time - ai_showment_time >= msgTime) {
						ai_showment_time = time;
						if (ai_showment_idx >= msgSize)
							ai_showment_idx = 0;
						toSender(S_ObjectChatting.clone(BasePacketPooling
								.getPool(S_ObjectChatting.class), this, 0x02,
								msgList.get(ai_showment_idx++)), false);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 해당 아이템 오토루팅 할지 여부.
	 * 
	 * @param ii
	 * @return
	 */
	private boolean isAutoPickup(ItemInstance ii) {
		// 소환객체가 죽을경우 오토루팅 처리 안됨.
		if (this instanceof SummonInstance)
			return false;
		// 보스는 오토루팅 안됨.
		if (getBoss() != null)
			return Lineage.auto_pickup_boss;
		// 아데나 오토루팅 여부
		if (ii.getItem().getNameIdNumber() == 4) {
			return Lineage.auto_pickup_aden;
		} else {
			return Lineage.auto_pickup;
		}
	}

	@Override
	public int getTotalStr() {
		return super.getTotalStr() + mon.getStr();
	}

	@Override
	public int getTotalDex() {
		return super.getTotalDex() + mon.getDex();
	}

	@Override
	public int getTotalCon() {
		return super.getTotalCon() + mon.getCon();
	}

	@Override
	public int getTotalWis() {
		return super.getTotalWis() + mon.getWis();
	}

	@Override
	public int getTotalInt() {
		return super.getTotalInt() + mon.getInt();
	}

	@Override
	public int getTotalCha() {
		return super.getTotalCha() + mon.getCha();
	}

	@Override
	public int getTotalAc() {
		return super.getTotalAc() + mon.getAc();
	}

	@Override
	public int getDynamicMr() {
		return super.getDynamicMr() + mon.getMr();
	}
	
	public MonsterSkill findMagic() {
		Monster monTemp = mon;
		if (monTemp == null) {
			return null;
		}
		
		for (MonsterSkill temp : mon.getSkillList()) {
			if (temp == null) {
				continue;
			} else if (temp.getUid() == 5 || temp.getUid() == 8) {
				return temp;
			}
		}
		return null;
	}

	@Override
	public int getLevel() {
		return this instanceof PetInstance ? super.getLevel() : mon.getLevel();
	}

	@Override
	public int getLawful() {
		return this instanceof PetInstance ? super.getLawful() : mon
				.getLawful();
	}

	@Override
	public void toTeleport(int x, int y, int map, boolean effect) {
		//
		if (getMonster().isHaste())
			setSpeed(1);
		if (getMonster().isBravery())
			setBrave(true);
		//
		super.toTeleport(x, y, map, effect);
	}

	public boolean isBoss() {
	if (getMonster().getName().equalsIgnoreCase("유니콘")
			|| getMonster().getName().equalsIgnoreCase("안타라스")
			|| getMonster().getName().equalsIgnoreCase("발라카스")
			|| getMonster().getName().equalsIgnoreCase("파푸리온")
			|| getMonster().getName().equalsIgnoreCase("테베 아누비스")
			|| getMonster().getName().equalsIgnoreCase("데몬")
			|| getMonster().getName().equalsIgnoreCase("에이션트 자이언트")
			|| getMonster().getName().equalsIgnoreCase("테베 호루스")
			|| getMonster().getName().equalsIgnoreCase("데스나이트")
			|| getMonster().getName().equalsIgnoreCase("거대 여왕 개미")
			|| getMonster().getName().equalsIgnoreCase("그레이트 미노타우르스")
			|| getMonster().getName().equalsIgnoreCase("커츠")
			|| getMonster().getName().equalsIgnoreCase("피닉스")
			|| getMonster().getName().equalsIgnoreCase("얼음 여왕")
			|| getMonster().getName().equalsIgnoreCase("좀비로드")
			|| getMonster().getName().equalsIgnoreCase("공포의 뱀파이어")
			|| getMonster().getName().equalsIgnoreCase("죽음의 좀비 로드")
			|| getMonster().getName().equalsIgnoreCase("지옥의 쿠거")
			|| getMonster().getName().equalsIgnoreCase("바포메트")
			|| getMonster().getName().equalsIgnoreCase("베레스")
			|| getMonster().getName().equalsIgnoreCase("제니스 퀸")
			|| getMonster().getName().equalsIgnoreCase("정예 흑기사 대장")
			|| getMonster().getName().equalsIgnoreCase("왜곡의 제니스 퀸")
			|| getMonster().getName().equalsIgnoreCase("불신의 시어")
			|| getMonster().getName().equalsIgnoreCase("[무한대전]데스나이트")
			|| getMonster().getName().equalsIgnoreCase("나이트메어")
			|| getMonster().getName().equalsIgnoreCase("흑장로")
			|| getMonster().getName().equalsIgnoreCase("네크로맨서")
			|| getMonster().getName().equalsIgnoreCase("아이스 데몬")
			|| getMonster().getName().equalsIgnoreCase("땅의 대정령")
			|| getMonster().getName().equalsIgnoreCase("물의 대정령")
			|| getMonster().getName().equalsIgnoreCase("바람의 대정령")
			|| getMonster().getName().equalsIgnoreCase("불의 대정령")
			|| getMonster().getName().equalsIgnoreCase("발터자르")
			|| getMonster().getName().equalsIgnoreCase("카스파")
			|| getMonster().getName().equalsIgnoreCase("메르키오르")
			|| getMonster().getName().equalsIgnoreCase("세마")
			|| getMonster().getName().equalsIgnoreCase("파우스트")
			|| getMonster().getName().equalsIgnoreCase("[오만의 탑] 감시자 리퍼")) {
			return true;
		}
		return false;
	}

	private boolean kingdomBoss;

	public boolean isKingdomBoss() {
		return kingdomBoss;
	}

	public void setKingdomBoss(boolean kingdomBoss) {
		this.kingdomBoss = kingdomBoss;
	}

}
