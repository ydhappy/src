package lineage.world.object.instance;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import lineage.bean.database.Drop;
import lineage.bean.database.Item;
import lineage.bean.database.ItemSetoption;
import lineage.bean.database.Poly;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.bean.lineage.Inventory;
import lineage.database.ItemSetoptionDatabase;
import lineage.database.PolyDatabase;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CharacterSpMr;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectLock;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.item.MagicFlute;
import lineage.world.object.item.pet.PetArmor;
import lineage.world.object.item.pet.PetWeapon;
import lineage.world.object.item.potion.BraveryPotion;
import lineage.world.object.item.potion.HastePotion;
import lineage.world.object.item.potion.HealingPotion;
import lineage.world.object.item.potion.ManaPotion;
import lineage.world.object.magic.CounterBarrier;
import lineage.world.object.magic.ShapeChange;
import lineage.world.object.magic.SolidCarriage;

public class ItemInstance extends object implements BuffInterface {

	protected Character cha; // 아이템을 소지하고있는 객체
	protected Item item;
	protected int bress; // 축저주 여부
	protected int quantity; // 소막같은 수량
	protected int enLevel; // en
	protected int durability; // 손상도
	protected int dynamicMr; // mr
	protected int dynamicSp; // sp
	protected int dynamicHp; // hp
	protected int dynamicMp; // mp
	protected int dynamicHealing; // 물약 회복량
	protected int dynamicHealingPercent;// 물약 회복량 퍼센트
	protected int dynamicReduction; //
	protected int dynamicReductionPercent; // 확률 리덕션
	protected int dynamicReductionPercentValue; // 확률 리덕션 값
	protected int dynamicAddDmg; // 추가 데미지
	protected int dynamicAddDmgBow; // 추가 데미지
	protected int dynamicAddHit; // 근거리 공격성공
	protected int dynamicAddHitBow; // 장거리 공격성공
	protected int dynamicAc; // 동적 ac 값.
	protected int dynamicLight; // 동적 라이트값. 현재는 양초쪽에서 사용중. 해당 아이템에 밝기값을 저장하기 위해.
	protected int dynamicAddWeight; //
	protected double dynamicStunDefense; //
	protected int dynamicHoldDefense; //
	protected int dynamicAddPvpDmg; //
	protected int dynamicTicHp; //
	protected int dynamicTicMp; //
	protected boolean definite; // 확인 여부
	protected boolean equipped; // 착용 여부
	protected int nowTime; // 아이템 사용 남은 시간.
	public long click_delay; // 아이템 클릭 딜레이를 주기위한 변수.
	protected long time_drop; // 드랍된 시간값.
	protected int enEarth; // 땅 속성 인첸트
	protected int enWater; // 물 속성 인첸트
	protected int enFire; // 불 속성 인첸트
	protected int enWind; // 바람 속성 인첸트
	protected int slot; // 장비가 착용될때 사용된던 슬록정보를 저장.
	protected Map<String, String> option; //
	protected int grade; // 단계 기록용 (앨리스)

	// 개인상점에 사용되는 변수
	private int usershopIdx; // sell 처리시 위치값 지정용.
	private long usershopBuyPrice; // 판매 가격
	private long usershopSellPrice; // 구입 가격
	private long usershopBuyCount; // 판매 갯수
	private long usershopSellCount; // 구입 갯수

	// 몬스터가 사용하는 변수.
	private Drop drop;
		
	// 디아블로 시스템
	protected int Add_Min_Dmg;
	protected int Add_Max_Dmg;
	protected int Add_Str;
	protected int Add_Dex;
	protected int Add_Con;
	protected int Add_Int;
	protected int Add_Wiz;
	protected int Add_Cha;
	protected int Add_Mana;
	protected int Add_Hp;
	protected int Add_Manastell;
	protected int Add_Hpstell;

	protected int one;
	protected int two;
	protected int three;
	protected int four;

	private int dynamicLuck;

	private int dynamicAden;

	protected int dynamicStr;
	protected int dynamicDex;
	protected int dynamicInt;

	// 시간제 아이템을 표시하기위한 변수
	private String timestamp;
	private boolean isTimeCheck;

	// 캐릭터 저장 구슬
	protected long soul_cha;

	public ItemInstance() {
		//
		option = new HashMap<String, String>();
	}

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ItemInstance();
		return item;
	}

	public ItemInstance clone(Item item) {
		this.item = item;
		name = item.getNameId();
		gfx = item.getGroundGfx();

		return this;
	}

	@Override
	public void close() {
		super.close();
		// 메모리 초기화 함수.
		item = null;
		cha = null;
		time_drop = click_delay = quantity = enLevel = durability = dynamicMr = dynamicAddPvpDmg = usershopIdx = dynamicLight = dynamicAc = dynamicAddWeight = dynamicHoldDefense = enEarth = enWater = enFire = enWind = dynamicSp = dynamicMp = dynamicHp = dynamicHealing = dynamicHealingPercent = dynamicReduction = dynamicReductionPercent = dynamicAddDmg = dynamicAddDmgBow = dynamicTicHp = dynamicTicMp = dynamicReductionPercentValue = 0;
		usershopBuyPrice = usershopSellPrice = usershopSellCount = usershopBuyCount = nowTime = dynamicAddHit = dynamicAddHitBow = 0;
		dynamicLuck = dynamicAden = 0;
		dynamicStunDefense = 0;
		dynamicStr = dynamicDex = dynamicInt = Add_Min_Dmg = Add_Max_Dmg = Add_Str = Add_Dex = Add_Con = Add_Int = Add_Wiz = Add_Cha = Add_Mana = Add_Hp = Add_Manastell = Add_Hpstell = one = two = three = four = 0;
		slot = 0;
		grade = 0;
		bress = 1;
		soul_cha = 0;
		definite = equipped = isTimeCheck = false;
		drop = null;
		// isTimeCheck = false;
		if (option != null) {
			synchronized (option) {
				option.clear();
			}
		}
	}

	public boolean isClick(SummonInstance si) {
		// 딜레이 확인.
		long time = System.currentTimeMillis();
		long delay = si.click_delay;
		if (time - delay >= item.getContinuous()) {
			si.click_delay = time;
			return true;
		}
		return false;
	}

	/**
	 * 아이템을 사용해도 되는지 확인해주는 함수.<br/>
	 * : 아이템 더블클릭하면 젤 우선적으로 호출됨.<br/>
	 * : C_ItemClick 에서 사용중.<br/>
	 * 
	 * @return
	 */
	public boolean isClick(PcInstance pc) {
		//
		Object o = PluginController.init(ItemInstance.class, "isClick", this,
				pc);
		if (o != null)
			return (boolean) o;
		//
		if (pc != null) {
			// 맵에따른 아이템 제한 확인.
			switch (pc.getMap()) {
			case 22:
				// 게라드 시험 퀘 맵일경우 비취물약만 사용가능하도록 하기.
				if (item.getNameIdNumber() != 233) {
					// 귀환이나 순간이동 주문서 사용시 케릭동작에 락이 걸리기때문에 그것을 풀기위한것.
					pc.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
					return false;
				}
				break;
			case 201:
				// 마법사 30 퀘 일때. 귀환 빼고 다 불가능.
				if (item.getNameIdNumber() != 505) {
					pc.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
					return false;
				}
				break;
			}
			// 마법의 플룻에 따른 제한 확인.
			if (BuffController.find(pc).find(MagicFlute.class) != null) {
				pc.toSender(S_ObjectLock.clone(
						BasePacketPooling.getPool(S_ObjectLock.class), 7));
				pc.toSender(S_Message.clone(
						BasePacketPooling.getPool(S_Message.class), 343));
				return false;
			}
			// 변신상태에 따른 제한 확인.
			if (pc.getGfx() != pc.getClassGfx()) {
				Poly p = PolyDatabase.getPolyGfx(pc.getGfx());
				if (p != null && !p.isItem()) {
					ChattingController.toChatting(pc,
							"그 상태로는 아이템을 사용할 수 없습니다.", 20);
					return false;
				}
			}
		}

		// 딜레이 확인.
		long time = System.currentTimeMillis();
		long delay = click_delay;
		if (Lineage.item_delay_global && pc != null) {
			if (this instanceof ItemWeaponInstance)
				delay = pc.click_weapon_delay;
			else if (this instanceof ItemArmorInstance)
				delay = pc.click_armor_delay;
			else
				delay = pc.click_delay;
		}
		if (time - delay >= item.getContinuous()) {
			if (Lineage.item_delay_global && pc != null) {
				if (this instanceof ItemWeaponInstance)
					pc.click_weapon_delay = time;
				else if (this instanceof ItemArmorInstance)
					pc.click_armor_delay = time;
				else
					pc.click_delay = time;
			} else {
				click_delay = time;
			}
			return true;
		}
		return false;
	}

	public long getTimeDrop() {
		return time_drop;
	}

	public void setTimeDrop(long time_drop) {
		this.time_drop = time_drop;
	}

	public int getDynamicLight() {
		return dynamicLight;
	}

	public void setDynamicLight(int dynamicLight) {
		this.dynamicLight = dynamicLight;
	}

	public int getDynamicAc() {
		return dynamicAc;
	}

	public void setDynamicAc(int dynamicAc) {
		this.dynamicAc = dynamicAc;
	}

	public int getDynamicAddWeight() {
		return dynamicAddWeight;
	}

	public void setDynamicAddWeight(int dynamicAddWeight) {
		this.dynamicAddWeight = dynamicAddWeight;
	}

	public double getDynamicStunDefense() {
		return dynamicStunDefense;
	}

	public void setDynamicStunDefense(double dynamicStunDefense) {
		this.dynamicStunDefense = dynamicStunDefense;
	}

	public int getDynamicHoldDefense() {
		return dynamicHoldDefense;
	}

	public void setDynamicHoldDefense(int dynamicHoldDefense) {
		this.dynamicHoldDefense = dynamicHoldDefense;
	}

	public int getDynamicAddPvpDmg() {
		return dynamicAddPvpDmg;
	}

	public void setDynamicAddPvpDmg(int dynamicAddPvpDmg) {
		this.dynamicAddPvpDmg = dynamicAddPvpDmg;
	}

	public int getEnEarth() {
		return enEarth;
	}

	public int getEnEarthDamage() {
		return (getEnEarth() * 2) - 1;
	}

	public void setEnEarth(int enEarth) {
		if (enEarth > 5)
			enEarth = 5;
		this.enEarth = enEarth;
	}

	public int getEnWater() {
		return enWater;
	}

	public int getEnWaterDamage() {
		return (getEnWater() * 2) - 1;
	}

	public void setEnWater(int enWater) {
		if (enWater > 5)
			enWater = 5;
		this.enWater = enWater;
	}

	public int getEnFire() {
		return enFire;
	}

	public int getEnFireDamage() {
		return (getEnFire() * 2) - 1;
	}

	public void setEnFire(int enFire) {
		if (enFire > 5)
			enFire = 5;
		this.enFire = enFire;
	}

	public int getEnWind() {
		return enWind;
	}

	public int getEnWindDamage() {
		return (getEnWind() * 2) - 1;
	}

	public void setEnWind(int enWind) {
		if (enWind > 5)
			enWind = 5;
		this.enWind = enWind;
	}

	@Override
	public Character getCharacter() {
		return cha;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	public int getBress() {
		return bress;
	}

	public void setBress(int bress) {
		this.bress = bress;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		if (quantity < 0)
			quantity = 0;
		this.quantity = quantity;
	}

	public int getEnLevel() {
		return enLevel;
	}

	public void setEnLevel(int enLevel) {
		this.enLevel = enLevel;
	}

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		if (durability > Lineage.item_durability_max)
			durability = Lineage.item_durability_max;
		else if (durability < 0)
			durability = 0;
		this.durability = durability;

	}

	public int getDynamicMr() {
		return dynamicMr;
	}

	public void setDynamicMr(int dynamicMr) {
		this.dynamicMr = dynamicMr;
	}

	public int getDynamicSp() {
		return dynamicSp;
	}

	public void setDynamicSp(int dynamicSp) {
		this.dynamicSp = dynamicSp;
	}

	public int getDynamicMp() {
		return dynamicMp;
	}

	public void setDynamicMp(int dynamicMp) {
		this.dynamicMp = dynamicMp;
	}

	public int getDynamicHp() {
		return dynamicHp;
	}

	public void setDynamicHp(int dynamicHp) {
		this.dynamicHp = dynamicHp;
	}

	public int getDynamicHealing() {
		return dynamicHealing;
	}

	public void setDynamicHealing(int dynamicHealing) {
		this.dynamicHealing = dynamicHealing;
	}

	public int getDynamicHealingPercent() {
		return dynamicHealingPercent;
	}

	public void setDynamicHealingPercent(int dynamicHealingPercent) {
		this.dynamicHealingPercent = dynamicHealingPercent;
	}

	public int getDynamicReduction() {
		return dynamicReduction;
	}

	public void setDynamicReduction(int dynamicReduction) {
		this.dynamicReduction = dynamicReduction;
	}

	public int getDynamicReductionPercent() {
		return dynamicReductionPercent;
	}

	public void setDynamicReductionPercent(int dynamicReductionPercent) {
		this.dynamicReductionPercent = dynamicReductionPercent;
	}

	public int getDynamicReductionPercentValue() {
		return dynamicReductionPercentValue;
	}

	public void setDynamicReductionPercentValue(int dynamicReductionPercentValue) {
		this.dynamicReductionPercentValue = dynamicReductionPercentValue;
	}

	public int getDynamicAddDmg() {
		return dynamicAddDmg;
	}

	public void setDynamicAddDmg(int dynamicAddDmg) {
		this.dynamicAddDmg = dynamicAddDmg;
	}

	public int getDynamicAddDmgBow() {
		return dynamicAddDmgBow;
	}

	public void setDynamicAddDmgBow(int dynamicAddDmgBow) {
		this.dynamicAddDmgBow = dynamicAddDmgBow;
	}

	public int getDynamicAddHit() {
		return dynamicAddHit;
	}

	public void setDynamicAddHit(int dynamicAddHit) {
		this.dynamicAddHit = dynamicAddHit;
	}

	public int getDynamicAddHitBow() {
		return dynamicAddHitBow;
	}

	public void setDynamicAddHitBow(int dynamicAddHitBow) {
		this.dynamicAddHitBow = dynamicAddHitBow;
	}

	public int getDynamicStr() {
		return dynamicStr;
	}

	public void setDynamicStr(int paramInt) {
		this.dynamicStr = paramInt;
	}

	public int getDynamicDex() {
		return dynamicDex;
	}

	public void setDynamicDex(int paramInt) {
		this.dynamicDex = paramInt;
	}

	public int getDynamicInt() {
		return dynamicInt;
	}

	public void setDynamicInt(int paramInt) {
		this.dynamicInt = paramInt;
	}

	public int getDynamicTicHp() {
		return dynamicTicHp;
	}

	public void setDynamicTicHp(int dynamicTicHp) {
		this.dynamicTicHp = dynamicTicHp;
	}

	public int getDynamicTicMp() {
		return dynamicTicMp;
	}

	public void setDynamicTicMp(int dynamicTicMp) {
		this.dynamicTicMp = dynamicTicMp;
	}

	public boolean isDefinite() {
		return definite;
	}

	public void setDefinite(boolean definite) {
		this.definite = definite;
	}

	public boolean isEquipped() {
		return equipped;
	}

	public void setEquipped(boolean equipped) {

		this.equipped = equipped;
		//
		PluginController.init(ItemInstance.class, "setEquipped", this, equipped);

	}

	public int getNowTime() {
		return nowTime < 0 ? 0 : nowTime;
		// return nowTime; //디아
	}

	public void setNowTime(int nowTime) {
		if (nowTime >= 0)
			this.nowTime = nowTime;

		// this.nowTime = nowTime; // 디아
	}

	public int getWeight() {
		return item != null ? (int) Math.round(item.getWeight() * getCount()) : 0;
	}

	public long getUsershopBuyPrice() {
		return usershopBuyPrice;
	}

	public void setUsershopBuyPrice(long usershopBuyPrice) {
		this.usershopBuyPrice = usershopBuyPrice;
	}

	public long getUsershopSellPrice() {
		return usershopSellPrice;
	}

	public void setUsershopSellPrice(long usershopSellPrice) {
		this.usershopSellPrice = usershopSellPrice;
	}

	public long getUsershopBuyCount() {
		return usershopBuyCount;
	}

	public void setUsershopBuyCount(long usershopBuyCount) {
		this.usershopBuyCount = usershopBuyCount;
	}

	public long getUsershopSellCount() {
		return usershopSellCount;
	}

	public void setUsershopSellCount(long usershopSellCount) {
		this.usershopSellCount = usershopSellCount;
	}

	public int getUsershopIdx() {
		return usershopIdx;
	}

	public void setUsershopIdx(int usershopIdx) {
		this.usershopIdx = usershopIdx;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	// 디아블로시스템
	public int getAdd_Min_Dmg() {
		return Add_Min_Dmg;
	}

	public void setAdd_Min_Dmg(int Add_Min_Dmg) {
		this.Add_Min_Dmg = Add_Min_Dmg;
	}

	public int getAdd_Max_Dmg() {
		return Add_Max_Dmg;
	}

	public void setAdd_Max_Dmg(int Add_Max_Dmg) {
		this.Add_Max_Dmg = Add_Max_Dmg;
	}

	public int getAdd_Str() {
		return Add_Str;
	}

	public void setAdd_Str(int Add_Str) {
		this.Add_Str = Add_Str;
	}

	public int getAdd_Dex() {
		return Add_Dex;
	}

	public void setAdd_Dex(int Add_Dex) {
		this.Add_Dex = Add_Dex;
	}

	public int getAdd_Con() {
		return Add_Con;
	}

	public void setAdd_Con(int Add_Con) {
		this.Add_Con = Add_Con;
	}

	public int getAdd_Int() {
		return Add_Int;
	}

	public void setAdd_Int(int Add_Int) {
		this.Add_Int = Add_Int;
	}

	public int getAdd_Wiz() {
		return Add_Wiz;
	}

	public void setAdd_Wiz(int Add_Wiz) {
		this.Add_Wiz = Add_Wiz;
	}

	public int getAdd_Cha() {
		return Add_Cha;
	}

	public void setAdd_Cha(int Add_Cha) {
		this.Add_Cha = Add_Cha;
	}

	public int getAdd_Mana() {
		return Add_Mana;
	}

	public void setAdd_Mana(int Add_Mana) {
		this.Add_Mana = Add_Mana;
	}

	public int getAdd_Hp() {
		return Add_Hp;
	}

	public void setAdd_Hp(int Add_Hp) {
		this.Add_Hp = Add_Hp;
	}

	public int getAdd_Manastell() {
		return Add_Manastell;
	}

	public void setAdd_Manastell(int Add_Manastell) {
		this.Add_Manastell = Add_Manastell;
	}

	public int getAdd_Hpstell() {
		return Add_Hpstell;
	}

	public void setAdd_Hpstell(int Add_Hpstell) {
		this.Add_Hpstell = Add_Hpstell;
	}

	public int getOne() {
		return one;
	}

	public void setOne(int one) {
		this.one = one;
	}

	public int getTwo() {
		return two;
	}

	public void setTwo(int two) {
		this.two = two;
	}

	public int getThree() {
		return three;
	}

	public void setThree(int three) {
		this.three = three;
	}

	public int getFour() {
		return four;
	}

	public void setFour(int four) {
		this.four = four;
	}

	public int getDynamicLuck() {
		return this.dynamicLuck;
	}

	public void setDynamicLuck(int i) {
		this.dynamicLuck = i;
	}

	public int getDynamicAden() {
		return this.dynamicAden;
	}

	public void setDynamicAden(int i) {
		this.dynamicAden = i;
	}

	public long getSoul_Cha() {
		return this.soul_cha;
	}

	public void setSoul_cha(long i) {
		this.soul_cha = i;
	}

	public Drop getDrop() {
		return drop;
	}

	public void setDrop(Drop drop) {
		this.drop = drop;
	}
	
	/**
	 * 리니지 월드에 접속했을때 착용중인 아이템 처리를 위해 사용되는 메서드.
	 * 
	 * @param pc
	 */
	public void toWorldJoin(Connection con, PcInstance pc) {
		//
		cha = pc;

		//
		if (getItem().getEnchantMr() != 0) {
			setDynamicMr(getEnLevel() * getItem().getEnchantMr());
			if (Lineage.server_version > 144 && Lineage.server_version <= 200)
				pc.toSender(S_InventoryStatus.clone(
						BasePacketPooling.getPool(S_InventoryStatus.class),
						this));
		}
	}

	@Override
	public void toPickup(Character cha) {
		this.cha = cha;
	}

	/**
	 * 해당 아이템이 드랍됫을때 호출되는 메서드.
	 * 
	 * @param cha
	 */
	public void toDrop(Character cha) {
		this.cha = null;
		setTimeDrop(System.currentTimeMillis());
	}

	/**
	 * 아이템 착용 및 해제시 호출되는 메서드.
	 * 
	 * @param cha
	 * @param inv
	 */
	public void toEquipped(Character cha, Inventory inv, int slot) {
	}

	/**
	 * 인첸트 활성화 됫을때 아이템의 뒷처리를 처리하도록 요청하는 메서드.
	 * 
	 * @param pc
	 * @param en
	 */
	public void toEnchant(PcInstance pc, int en) {
		if (getItem() == null || getItem().getEnchantMr() == 0)
			return;

		// 인첸을 성공했다면 마법망토는 mr값을 상승해야함.
		if (en != 0) {
			int new_mr = getEnLevel() * getItem().getEnchantMr();
			if (equipped) {
				// 이전에 세팅값 빼기.
				pc.setDynamicMr(pc.getDynamicMr() - getDynamicMr());
				// 인첸에따른 새로운값 적용.
				pc.setDynamicMr(pc.getDynamicMr() + new_mr);
				pc.toSender(S_CharacterSpMr.clone(
						BasePacketPooling.getPool(S_CharacterSpMr.class), pc));
			}
			setDynamicMr(new_mr);
			if (Lineage.server_version <= 144)
				pc.toSender(S_InventoryEquipped.clone(
						BasePacketPooling.getPool(S_InventoryEquipped.class),
						this));
			else
				pc.toSender(S_InventoryStatus.clone(
						BasePacketPooling.getPool(S_InventoryStatus.class),
						this));
		}
	}

	/**
	 * 마법책 및 수정에 스킬값 지정하는 함수.
	 * 
	 * @param skill_level
	 */
	@Override
	public void setSkill(Skill skill) {
	}

	/**
	 * 아이템을 이용해 cha 가 o 에게 피해를 입히면 호출되는 함수.
	 * 
	 * @param cha
	 * @param o
	 * @return
	 */
	public boolean toDamage(Character cha, object o) {
		return false;
	}

	/**
	 * toDamage(Character cha, object o) 거친후 값이 true가 될경우 아래 함수를 호출해 추가적으로 데미지를
	 * 더하도록 함.
	 * 
	 * @return
	 */
	public int toDamage(int dmg) {
		return 0;
	}

	/**
	 * toDamage(Character cha, object o) 거친후 값이 true가 될경우 이팩트를 표현할 값을 턴.
	 * 
	 * @return
	 */
	public int toDamageEffect() {
		return 0;
	}

	/**
	 * 마법무기에 이팩트표현 전이 방식을 결정함. : false - 타격자에 위치에서 시작해서 타격자에 위치에서 끝남. (이렵션,
	 * 에너지볼트) : true - 시전자에 위치에서 시작해서 타격자에 위치에서 끝남. (콜라이트닝)
	 * 
	 * @return
	 */
	public boolean isDamageEffectTarget() {
		return true;
	}

	/**
	 * 펫의 오프젝트값 리턴.
	 * 
	 * @return
	 */
	public long getPetObjectId() {
		return 0;
	}

	public void setPetObjectId(final long id) {
	}

	/**
	 * 여관방 열쇠 키값
	 * 
	 * @return
	 */
	public long getInnRoomKey() {
		return 0;
	}

	public void setInnRoomKey(final long key) {
	}

	/**
	 * 편지지 디비 연결 고리인 uid
	 * 
	 * @return
	 */
	public long getLetterUid() {
		return 0;
	}

	public void setLetterUid(final long uid) {
	}

	/**
	 * 레이스 관련 함수
	 * 
	 * @return
	 */
	public String getRaceTicket() {
		return "";
	}

	public void setRaceTicket(String ticket) {
	}

	public int getBressPacket() {
		if (Lineage.server_version > 144) {
			if (definite) {
				if (bress < 0)
					return Lineage.server_version > 280 ? bress : bress + 128;
				else
					return bress;
			} else {
				return 3;
			}
		} else {
			return bress;
		}
	}

	/**
	 * 레벨 제한 체크
	 */
	protected boolean isLvCheck(Character cha) {
		// 착용하지 않은 상태에서만 체크
		if (!isEquipped()) {
			if (item.getLevelMin() > 0 && item.getLevelMin() > cha.getLevel()) {
				// cha.toSender(new SItemLevelFails(item.Level));
				// 672 : 이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.
				ChattingController.toChatting(cha, String.format("이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.", item.getLevelMin()), 20);
				return false;
			}
			if (item.getLevelMax() > 0 && item.getLevelMax() < cha.getLevel()) {
				// 673 : 이 아이템은 %d레벨 이하일때만 사용할 수 있습니다.
				ChattingController.toChatting(cha, String.format("이 아이템은 %d레벨 이하일 때만 사용할 수 있습니다.", item.getLevelMax()), 20);
				return false;
			}
			switch (cha.getClassType()) {
			case 0x00: // 군주
				if (item.getRoyal() > 0 && item.getRoyal() > cha.getLevel()) {
					ChattingController.toChatting(cha,
							String.format("이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.",
									item.getRoyal()), 20);
					return false;
				}
				break;
			case 0x01: // 기사
				if (item.getKnight() > 0 && item.getKnight() > cha.getLevel()) {
					ChattingController.toChatting(cha,
							String.format("이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.",
									item.getKnight()), 20);
					return false;
				}
				break;
			case 0x02: // 요정
				if (item.getElf() > 0 && item.getElf() > cha.getLevel()) {
					ChattingController.toChatting(cha, String.format(
							"이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.", item.getElf()),
							20);
					return false;
				}
				break;
			case 0x03: // 법사
				if (item.getWizard() > 0 && item.getWizard() > cha.getLevel()) {
					ChattingController.toChatting(cha,
							String.format("이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.",
									item.getWizard()), 20);
					return false;
				}
				break;
			case 0x04: // 다크엘프
				if (item.getDarkElf() > 0 && item.getDarkElf() > cha.getLevel()) {
					ChattingController.toChatting(cha, String.format(
							"이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.",
							item.getDarkElf()), 20);
					return false;
				}
				break;
			case 0x05: // 용기사
				if (item.getDragonKnight() > 0
						&& item.getDragonKnight() > cha.getLevel()) {
					ChattingController.toChatting(cha, String.format(
							"이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.",
							item.getDragonKnight()), 20);
					return false;
				}
				break;
			case 0x06: // 환술사
				if (item.getBlackWizard() > 0
						&& item.getBlackWizard() > cha.getLevel()) {
					ChattingController.toChatting(cha, String.format(
							"이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.",
							item.getBlackWizard()), 20);
					return false;
				}
				break;
			}
		}

		return true;
	}

	/**
	 * 클레스 착용가능 여부 체크 부분
	 */
	protected boolean isClassCheck(Character cha) {
		switch (cha.getClassType()) {
		case 0x00: // 군주
			return item.getRoyal() > 0;
		case 0x01: // 기사
			return item.getKnight() > 0;
		case 0x02: // 요정
			return item.getElf() > 0;
		case 0x03: // 법사
			return item.getWizard() > 0;
		case 0x04: // 다크엘프
			return item.getDarkElf() > 0;
		case 0x05: // 용기사
			return item.getDragonKnight() > 0;
		case 0x06: // 환술사
			return item.getBlackWizard() > 0;
		case 0x07: // 전사
			return item.getWarrior() > 0;
		default:
			// 디비로 연동을 시키는게 좋을지도..?
			if (cha instanceof PetInstance)
				return this instanceof PetArmor || this instanceof PetWeapon;
			break;
		}
		return true;
	}

	protected boolean isLevel(Character cha) {
		switch (cha.getClassType()) {
		case 0x00: // 군주
			return item.getRoyal() > 0 && cha.getLevel() >= item.getRoyal();
		case 0x01: // 기사
			return item.getKnight() > 0 && cha.getLevel() >= item.getKnight();
		case 0x02: // 요정
			return item.getElf() > 0 && cha.getLevel() >= item.getElf();
		case 0x03:
			return item.getWizard() > 0 && cha.getLevel() >= item.getWizard();
		case 0x04:
			return item.getDarkElf() > 0 && cha.getLevel() >= item.getDarkElf();
		case 0x05:
			return item.getDragonKnight() > 0
					&& cha.getLevel() >= item.getDragonKnight();
		case 0x06:
			return item.getBlackWizard() > 0
					&& cha.getLevel() >= item.getBlackWizard();
		}
		return true;
	}

	/**
	 * 아이템을 착용 및 해제할때 호출됨. : 장비를 해제할때 제거해야할 버프가 있는지 확인하고 제거하는 메서드.
	 * 
	 * @param cha
	 */
	public void toBuffCheck(Character cha) {
		// 착용상태는 무시.
		if (isEquipped())
			return;

		if (getItem().getSlot() == 8)
			BuffController.remove(cha, CounterBarrier.class);
		if (getItem().getSlot() == 7)
			BuffController.remove(cha, SolidCarriage.class);
	}

	/**
	 * 아이템 부가옵션 적용및 해제 부분
	 */
	public void toOption(Character cha, boolean sendPacket) {
		//
		PluginController.init(ItemInstance.class, "toOption", this, cha,
				sendPacket);
		//
		if (getItem().getAddStr() != 0 || getDynamicStr() > 0
				|| getAdd_Str() > 0) {
			if (equipped) {
				cha.setDynamicStr(cha.getDynamicStr() + getItem().getAddStr()
						+ getDynamicStr() + getAdd_Str());
			} else {
				cha.setDynamicStr(cha.getDynamicStr() - getItem().getAddStr()
						- getDynamicStr() - getAdd_Str());
			}
		}
		if (getItem().getAddDex() != 0 || getDynamicDex() > 0
				|| getAdd_Dex() > 0) {
			if (equipped) {
				cha.setDynamicDex(cha.getDynamicDex() + getItem().getAddDex()
						+ getDynamicDex() + getAdd_Dex());
			} else {
				cha.setDynamicDex(cha.getDynamicDex() - getItem().getAddDex()
						- getDynamicDex() - getAdd_Dex());
			}

		}
		if (getItem().getAddCon() != 0 || getAdd_Con() > 0) {
			if (equipped) {
				cha.setDynamicCon(cha.getDynamicCon() + getItem().getAddCon()
						+ getAdd_Con());
			} else {
				cha.setDynamicCon(cha.getDynamicCon() - getItem().getAddCon()
						- getAdd_Con());
			}
		}
		if (getItem().getAddInt() != 0 || getDynamicInt() > 0
				|| getAdd_Int() > 0) {
			if (equipped) {
				cha.setDynamicInt(cha.getDynamicInt() + getItem().getAddInt()
						+ getDynamicInt() + getAdd_Int());
			} else {
				cha.setDynamicInt(cha.getDynamicInt() - getItem().getAddInt()
						- getDynamicInt() - getAdd_Int());
			}
		}
		if (getItem().getAddCha() != 0 || getAdd_Cha() > 0) {
			if (equipped) {
				cha.setDynamicCha(cha.getDynamicCha() + getItem().getAddCha()
						+ getAdd_Cha());
			} else {
				cha.setDynamicCha(cha.getDynamicCha() - getItem().getAddCha()
						- getAdd_Cha());
			}
		}
		if (getItem().getAddWis() != 0 || getAdd_Wiz() > 0) {
			if (equipped) {
				cha.setDynamicWis(cha.getDynamicWis() + getItem().getAddWis()
						+ getAdd_Wiz());
			} else {
				cha.setDynamicWis(cha.getDynamicWis() - getItem().getAddWis()
						- getAdd_Wiz());
			}
		}
		if (getItem().getAddHp() > 0 || getDynamicHp() > 0 || getAdd_Hp() > 0) {
			if (equipped) {
				cha.setDynamicHp(cha.getDynamicHp() + getItem().getAddHp()
						+ getDynamicHp() + getAdd_Hp());
			} else {
				cha.setDynamicHp(cha.getDynamicHp() - getItem().getAddHp()
						- getDynamicHp() - getAdd_Hp());
			}
		}
		if (getItem().getAddMp() > 0 || getDynamicMp() > 0 || getAdd_Mana() > 0) {
			if (equipped) {
				cha.setDynamicMp(cha.getDynamicMp() + getItem().getAddMp()
						+ getDynamicMp() + getAdd_Mana());
			} else {
				cha.setDynamicMp(cha.getDynamicMp() - getItem().getAddMp()
						- getDynamicMp() - getAdd_Mana());
			}
		}
		if (getItem().getAddMr() > 0 || getDynamicMr() > 0) {
			if (equipped) {
				cha.setDynamicMr(cha.getDynamicMr() + getItem().getAddMr()
						+ getDynamicMr());
			} else {
				cha.setDynamicMr(cha.getDynamicMr() - getItem().getAddMr()
						- getDynamicMr());
			}
		}
		if (getItem().getAddSp() > 0 || getDynamicSp() > 0) {
			if (equipped) {
				cha.setDynamicSp(cha.getDynamicSp() + getItem().getAddSp()
						+ getDynamicSp());
			} else {
				cha.setDynamicSp(cha.getDynamicSp() - getItem().getAddSp()
						- getDynamicSp());
			}
		}
		if (getItem().getAddWeight() > 0) {
			if (equipped) {
				cha.setItemWeight(cha.getItemWeight()
						+ getItem().getAddWeight());
			} else {
				cha.setItemWeight(cha.getItemWeight()
						- getItem().getAddWeight());
			}
		}
		if (getItem().getTicHp() > 0) {
			if (equipped) {
				cha.setDynamicTicHp(cha.getDynamicTicHp()
						+ getItem().getTicHp());
			} else {
				cha.setDynamicTicHp(cha.getDynamicTicHp()
						- getItem().getTicHp());
			}
		}
		if (getItem().getTicMp() > 0) {
			if (equipped) {
				cha.setDynamicTicMp(cha.getDynamicTicMp()
						+ getItem().getTicMp());
			} else {
				cha.setDynamicTicMp(cha.getDynamicTicMp()
						- getItem().getTicMp());
			}
		}
		if (getItem().getEarthress() > 0) {
			if (equipped) {
				cha.setDynamicEarthress(cha.getDynamicEarthress()
						+ getItem().getEarthress());
			} else {
				cha.setDynamicEarthress(cha.getDynamicEarthress()
						- getItem().getEarthress());
			}
		}
		if (getItem().getFireress() > 0) {
			if (equipped) {
				cha.setDynamicFireress(cha.getDynamicFireress()
						+ getItem().getFireress());
			} else {
				cha.setDynamicFireress(cha.getDynamicFireress()
						- getItem().getFireress());
			}
		}
		if (getItem().getWindress() > 0) {
			if (equipped) {
				cha.setDynamicWindress(cha.getDynamicWindress()
						+ getItem().getWindress());
			} else {
				cha.setDynamicWindress(cha.getDynamicWindress()
						- getItem().getWindress());
			}
		}
		if (getItem().getWaterress() > 0) {
			if (equipped) {
				cha.setDynamicWaterress(cha.getDynamicWaterress()
						+ getItem().getWaterress());
			} else {
				cha.setDynamicWaterress(cha.getDynamicWaterress()
						- getItem().getWaterress());
			}
		}
		if (getItem().getAddDmg() > 0 || getDynamicAddDmg() > 0) {
			if (equipped) {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + getItem().getAddDmg() + getDynamicAddDmg());
			} else {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - getItem().getAddDmg() - getDynamicAddDmg());
			}
		}
		if( getDynamicAddDmgBow() > 0 || getItem().getAddBowDmg() > 0) {
			if(equipped){
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + getDynamicAddDmgBow() + getItem().getAddBowDmg());
			}else{
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - getDynamicAddDmgBow() - getItem().getAddBowDmg());
			}
		}
		if (getItem().getAddHit() > 0 || getDynamicAddHit() > 0) {
			if (equipped) {
				cha.setDynamicAddHit(cha.getDynamicAddHit()
						+ getItem().getAddHit() + getDynamicAddHit());
			} else {
				cha.setDynamicAddHit(cha.getDynamicAddHit()
						- getItem().getAddHit() - getDynamicAddHit());
			}
		}
		if (getItem().getAddBowHit() > 0 || getDynamicAddHitBow() > 0) {
			if (equipped) {
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow()
						+ getItem().getAddBowHit() + getDynamicAddHitBow());
			} else {
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow()
						- getItem().getAddBowHit() - getDynamicAddHitBow());
			}
		}
		/*
		 * if(getItem().getPolyName()!=null &&
		 * getItem().getPolyName().length()>0){ Poly p =
		 * PolyDatabase.getPolyName(getItem().getPolyName()); // 변신 상태가 아니거나
		 * 변신하려는 gfx 와 같을때만 처리. if(cha.getGfx()==cha.getClassGfx() ||
		 * cha.getGfx()==p.getGfxId()){ if(equipped){ ShapeChange.onBuff(cha,
		 * cha, p, -1, false, sendPacket); }else{ BuffController.remove(cha,
		 * ShapeChange.class); } } }
		 */
		if (getItem().getReduction() > 0 || getDynamicReduction() > 0) {
			if (equipped) {
				cha.setDynamicReduction(cha.getDynamicReduction()
						+ getItem().getReduction() + getDynamicReduction());
			} else {
				cha.setDynamicReduction(cha.getDynamicReduction()
						- getItem().getReduction() - getDynamicReduction());
			}
		}
		if (getItem().getStunDefense() > 0 || getDynamicStunDefense() > 0) {
			if (equipped) {
				cha.setDynamicStunDefense(cha.getDynamicStunDefense()
						+ getItem().getStunDefense() + getDynamicStunDefense());
			} else {
				cha.setDynamicStunDefense(cha.getDynamicStunDefense()
						- getItem().getStunDefense() - getDynamicStunDefense());
			}
		}
		if (getItem().getHoldDefense() > 0 || getDynamicHoldDefense() > 0) {
			if (equipped) {
				cha.setDynamicHoldDefense(cha.getDynamicHoldDefense()
						+ getItem().getHoldDefense() + getDynamicHoldDefense());
			} else {
				cha.setDynamicHoldDefense(cha.getDynamicHoldDefense()
						- getItem().getHoldDefense() - getDynamicHoldDefense());
			}
		}
		if (getDynamicHealing() > 0) {
			if (equipped) {
				cha.setDynamicHealing(cha.getDynamicHealing()
						+ getDynamicHealing());
			} else {
				cha.setDynamicHealing(cha.getDynamicHealing()
						- getDynamicHealing());
			}
		}
		if (getDynamicHealingPercent() > 0) {
			if (equipped) {
				cha.setDynamicHealingPercent(cha.getDynamicHealingPercent()
						+ getDynamicHealingPercent());
			} else {
				cha.setDynamicHealingPercent(cha.getDynamicHealingPercent()
						- getDynamicHealingPercent());
			}
		}

		if (getDynamicLuck() > 0) {
			if (equipped) {
				cha.setLuck(cha.getLuck() + getDynamicLuck());
			} else {
				cha.setLuck(cha.getLuck() - getDynamicLuck());
			}
		}

		if (getDynamicAden() > 0) {
			if (equipped) {
				cha.setAden(cha.getAden() + getDynamicAden());
			} else {
				cha.setAden(cha.getAden() - getDynamicAden());
			}
		}
		/*if (getBress() == 0) {
			if (getItem().getType1().equalsIgnoreCase("weapon")
					&& !getItem().getType2().equalsIgnoreCase("bow")) {
				if (equipped) {
					cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 2);
				} else {
					cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 2);
				}
			}
			// 축복 활 추가 대미지
			if (getItem().getType2().equalsIgnoreCase("bow")) {
				if (equipped) {
					cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 2);
				} else {

					cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 2);
				}
			}
			// 축복 지팡이 추가 SP
			if (getItem().getType2().equalsIgnoreCase("staff")) {
				if (equipped) {
					cha.setDynamicSp(cha.getDynamicSp() + 1);
				} else {

					cha.setDynamicSp(cha.getDynamicSp() - 1);
				}
			}
		}*/
		
		if (sendPacket && cha instanceof PcInstance) {
			// if(Lineage.server_version < 380)
			cha.toSender(S_CharacterStat.clone(
					BasePacketPooling.getPool(S_CharacterStat.class), cha));
			// else
			// cha.toSender(S_CharacterStat2.clone(BasePacketPooling.getPool(S_CharacterStat2.class),
			// cha));
			cha.toSender(S_CharacterSpMr.clone(
					BasePacketPooling.getPool(S_CharacterSpMr.class), cha));
		}
	}

	/**
	 * 셋트아이템 착용여부 확인하여 옵션 적용및 해제처리하는 함수. : 인벤토리와 연동하여 적용된 세트가 있는지 확인. 있는데 현재 착용된
	 * 아이템에서 없을경우 옵션 해제. 없는데 전체 셋트착용중일경우 옵션 적용.
	 */
	public void toSetoption(Character cha, boolean sendPacket) {
		// PcInstance pc = (PcInstance)cha;
		Inventory inv = cha.getInventory();
		if (inv != null && item.getSetId() > 0) {
			ItemSetoption is = ItemSetoptionDatabase.find(item.getSetId());
			if (is != null) {
				if (equipped) {
					// 적용된 셋트가 없다면.
					if (!inv.isSetoption(is)) {
						// 셋트아이템 갯수 이상일 경우에만 적용.
						int cnt = 1; // 해당 아이템이 착용될 것이기때문에 초기값을 1
						for (int i = 0; i <= 27; ++i) {
							ItemInstance slot = inv.getSlot(i);
							if (slot != null
									&& slot.getItem().getSetId() == is.getUid())
								cnt += 1;
						}
						if (is.getCount() <= cnt) { // <= 20181129 <= 를 ==
							inv.appendSetoption(is);
							ItemSetoptionDatabase.setting(cha, is, equipped,
									sendPacket);
							// pc.setChkPoly(true);
						}
					}
				} else {
					// 적용된 셋트가 있다면
					if (inv.isSetoption(is)) {
						// 셋트아이템 갯수 미만일경우에만 해제.
						int cnt = 0;
						for (int i = 0; i <= 27; ++i) {
							ItemInstance slot = inv.getSlot(i);
							if (slot != null
									&& slot.getItem().getSetId() == is.getUid())
								cnt += 1;
						}
						if (is.getCount() <= cnt) { // <= 20181111
							inv.removeSetoption(is);
							ItemSetoptionDatabase.setting(cha, is, equipped,
									sendPacket);
							// pc.setChkPoly(false);
						}
					}
				}
			}
		}
	}

	/**
	 * 아이템을 사용해도 되는 상태인지 확인해주는 함수.
	 * 
	 * @param cha
	 * @return
	 */
	protected boolean isClick(Character cha) {
		if (cha.isBuffDecayPotion())
			return false;
		if (cha.isBuffAbsoluteBarrier()) {
			if (this instanceof BraveryPotion)
				return false;
			if (this instanceof HastePotion)
				return false;
			if (this instanceof HealingPotion)
				return false;
			if (this instanceof ManaPotion)
				return false;
		}
		return true;
	}

	public boolean isAutoClick(PcInstance pc) {
		if (pc.isBuffDecayPotion())
			return false;
		if (pc.isBuffAbsoluteBarrier()) {
			if (this instanceof BraveryPotion)
				return false;
			if (this instanceof HastePotion)
				return false;
			if (this instanceof HealingPotion)
				return false;
			if (this instanceof ManaPotion)
				return false;
		}
		// 20180319
		if (pc.isLock())
			return false;
		if (pc.isLockLow())
			return false;
		if (pc.isLockHigh())
			return false;

		return true;
	}
	
	public boolean isClickState(object o) {
		if (this == null || getItem() == null || o == null || o.getInventory() == null || o.isWorldDelete()) {
			return false;
		}
		
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			
			if (pc.isDead()) {
				ChattingController.toChatting(pc, "\\fY죽은 상태에서 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return false;
			}
			
			if (pc.isLock()) {
				ChattingController.toChatting(pc, "\\fY기절하거나 굳은 상태에서 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return false;
			}
			
			if (pc.isFishing()) {
				ChattingController.toChatting(pc, "\\fY낚시중에 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha instanceof PcInstance)
			cha.toSender(S_Message.clone(
					BasePacketPooling.getPool(S_Message.class), 330, toString()));
	}

	@Override
	public String toString() {
		StringBuffer msg = new StringBuffer();
		if (definite
				&& (this instanceof ItemWeaponInstance || this instanceof ItemArmorInstance)) {
			if (enLevel >= 0)
				msg.append("+");
			msg.append(enLevel);
			msg.append(" ");
		}
		msg.append(name);
		if (getCount() > 1) {
			msg.append(" (");
			msg.append(getCount());
			msg.append(")");
		}

		return msg.toString();
	}

	public String toStringDB() {
		StringBuffer msg = new StringBuffer();
		if (definite
				&& (this instanceof ItemWeaponInstance || this instanceof ItemArmorInstance)) {
			// 속성 인첸 표현.
			String element_name = null;
			Integer element_en = 0;
			if (getEnWind() > 0) {
				element_name = "풍령";
				element_en = getEnWind();
			}
			if (getEnEarth() > 0) {
				element_name = "지령";
				element_en = getEnEarth();
			}
			if (getEnWater() > 0) {
				element_name = "수령";
				element_en = getEnWater();
			}
			if (getEnFire() > 0) {
				element_name = "화령";
				element_en = getEnFire();
			}
			if (element_name != null) {
				msg.append(element_name).append(":").append(element_en)
						.append("단");
				msg.append(" ");
			}
			if (enLevel >= 0)
				msg.append("+");
			msg.append(enLevel);
			msg.append(" ");
		}
		msg.append(item.getName());
		if (getCount() > 1) {
			msg.append(" (");
			msg.append(getCount());
			msg.append(")");
		}
		return msg.toString();
	}

	public String toStringSaveDB() {
		return String.format("%d,%s,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s \r\n",
						getObjectId(), getItem().getName(), getCount(),
						getQuantity(), getEnLevel(), isEquipped() ? 1 : 0,
						isDefinite() ? 1 : 0, getBress(), getDurability(),
						getNowTime(), getPetObjectId(), getInnRoomKey(),
						getLetterUid(), getRaceTicket(), getItem().getType1(),
						getItem().getType2(), getOption(), String.format(
								"%d|%d|%d|%d", getEnWind(), getEnEarth(),
								getEnWater(), getEnFire()),
						// 디아블로 12개 + 4개
						String.format("%d|%d|%d|%d|%d|%d|%d|%d|%d|%d|%d|%d",
								getAdd_Min_Dmg(), getAdd_Max_Dmg(),
								getAdd_Str(), getAdd_Dex(), getAdd_Con(),
								getAdd_Int(), getAdd_Wiz(), getAdd_Cha(),
								getAdd_Hp(), getAdd_Mana(), getAdd_Hpstell(),
								getAdd_Manastell()), String.format(
								"%d|%d|%d|%d", getOne(), getTwo(), getThree(),
								getFour()),
						// soul_cha
						getSoul_Cha(), (getTimestamp() != null ? getTimestamp() : "0000-00-00 00:00:00.000"), isTimeCheck());
	}

	@Override
	public Skill getSkill() {
		return null;
	}

	@Override
	public void setTime(int time) {
	}

	@Override
	public int getTime() {
		return nowTime;
	}

	@Override
	public void setCharacter(Character cha) {
	}

	@Override
	public boolean isBuff(object o, long time) {
		return --nowTime > 0;
	}

	@Override
	public void toBuffStart(object o) {
	}

	@Override
	public void toBuffUpdate(object o) {
	}

	@Override
	public void toBuff(object o) {
	}

	@Override
	public boolean toBuffStop(object o) {
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
	}
	
	@Override
	public boolean equal(BuffInterface bi) {
		return false;
	}

	@Override
	public void setTime(int time, boolean restart) {
		// TODO Auto-generated method stub

	}

	@Override
	public int toDamagePlus(Character cha, object target) {
		return 0;
	}

	@Override
	public void toWorldOut(object o) {
	}

	@Override
	public int toDamageReduction(object attacker, object target) {
		return 0;
	}
	
	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	/**
	 * 아이템 기타옵션정보를 디비에 등록하기위해 문자열로 만들어 리턴함.
	 * 
	 * @return
	 */
	public String getOption() {
		StringBuffer sb = new StringBuffer();
		synchronized (option) {
			for (String key : option.keySet())
				sb.append(key).append("=").append(option.get(key)).append("|");
		}
		return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
	}

	/**
	 * 키에 해당하는값 추출.
	 * 
	 * @param key
	 * @return
	 */
	public String getOption(String key) {
		synchronized (option) {
			return option.get(key);
		}
	}

	/**
	 * 기타옵션 등록 메서드.
	 * 
	 * @param key
	 * @param val
	 */
	public void setOption(String key, String val) {
		synchronized (option) {
			option.put(key, val);
		}
	}

	public void clearOption() {
		synchronized (option) {
			option.clear();
		}
	}

	/**
	 * 기타옵션을 제거하는 함수. 착용중일경우 해당 옵션이 존재할때 제거 처리함. 패킷처리는 안함.
	 * 
	 * @param is
	 *            : 적용여부
	 */
	public void toOption(boolean is) {
		//
		if (cha == null)
			return;
		//
		String option = getOption("str");
		if (option != null) {
			if (is)
				cha.setDynamicStr(cha.getDynamicStr() + Integer.valueOf(option));
			else
				cha.setDynamicStr(cha.getDynamicStr() - Integer.valueOf(option));
		}
		option = getOption("dex");
		if (option != null) {
			if (is)
				cha.setDynamicDex(cha.getDynamicDex() + Integer.valueOf(option));
			else
				cha.setDynamicDex(cha.getDynamicDex() - Integer.valueOf(option));
		}
		option = getOption("con");
		if (option != null) {
			if (is)
				cha.setDynamicCon(cha.getDynamicCon() + Integer.valueOf(option));
			else
				cha.setDynamicCon(cha.getDynamicCon() - Integer.valueOf(option));
		}
		option = getOption("wis");
		if (option != null) {
			if (is)
				cha.setDynamicWis(cha.getDynamicWis() + Integer.valueOf(option));
			else
				cha.setDynamicWis(cha.getDynamicWis() - Integer.valueOf(option));
		}
		option = getOption("int");
		if (option != null) {
			if (is)
				cha.setDynamicInt(cha.getDynamicInt() + Integer.valueOf(option));
			else
				cha.setDynamicInt(cha.getDynamicInt() - Integer.valueOf(option));
		}
		option = getOption("cha");
		if (option != null) {
			if (is)
				cha.setDynamicCha(cha.getDynamicCha() + Integer.valueOf(option));
			else
				cha.setDynamicCha(cha.getDynamicCha() - Integer.valueOf(option));
		}
		option = getOption("hp");
		if (option != null) {
			if (is)
				cha.setDynamicHp(cha.getDynamicHp() + Integer.valueOf(option));
			else
				cha.setDynamicHp(cha.getDynamicHp() - Integer.valueOf(option));
		}
		option = getOption("mp");
		if (option != null) {
			if (is)
				cha.setDynamicMp(cha.getDynamicMp() + Integer.valueOf(option));
			else
				cha.setDynamicMp(cha.getDynamicMp() - Integer.valueOf(option));
		}
		option = getOption("hit");
		if (option != null) {
			if (is)
				cha.setDynamicAddHit(cha.getDynamicAddHit()
						+ Integer.valueOf(option));
			else
				cha.setDynamicAddHit(cha.getDynamicAddHit()
						- Integer.valueOf(option));
		}
		option = getOption("dmg");
		if (option != null) {
			if (is)
				cha.setDynamicAddDmg(cha.getDynamicAddDmg()
						+ Integer.valueOf(option));
			else
				cha.setDynamicAddDmg(cha.getDynamicAddDmg()
						- Integer.valueOf(option));
		}
		option = getOption("sp");
		if (option != null) {
			if (is)
				cha.setDynamicSp(cha.getDynamicSp() + Integer.valueOf(option));
			else
				cha.setDynamicSp(cha.getDynamicSp() - Integer.valueOf(option));
		}
		option = getOption("mr");
		if (option != null) {
			if (is)
				cha.setDynamicMr(cha.getDynamicMr() + Integer.valueOf(option));
			else
				cha.setDynamicMr(cha.getDynamicMr() - Integer.valueOf(option));
		}
	}

	@Override
	public int toDamageAddRate(object attacker, object target) {
		return 0;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isTimeCheck() {
		return isTimeCheck;
	}

	public void setTimeCheck(boolean isTimeCheck) {
		this.isTimeCheck = isTimeCheck;
	}

	/**
	 * 장신구 확인. 2020-09-04 by connector12@nate.com
	 */
	public boolean isAccessory() {
		if (this != null && this instanceof ItemArmorInstance
				&& getItem() != null) {
			if (getItem().getType2().equalsIgnoreCase("necklace")
					|| getItem().getType2().equalsIgnoreCase("belt")
					|| getItem().getType2().equalsIgnoreCase("ring")
					|| getItem().getType2().equalsIgnoreCase("earring")) {
				return true;
			}
		}

		return false;
	}
}
