package lineage.world.object;

import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectHitratio;
import lineage.util.Util;
import lineage.world.controller.AgitController;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.ElvenforestController;
import lineage.world.controller.InnController;
import lineage.world.controller.KingdomController;
import lineage.world.object.instance.PcInstance;
import lineage.bean.database.Monster;
import lineage.database.MonsterDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.world.World;
import lineage.world.controller.CharacterController;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.magic.Haste;
import lineage.world.object.magic.HolyWalk;
import lineage.world.object.npc.background.DeathEffect;


public class Character extends object {

	protected int level;
	private int nowHp;
	protected int maxHp;
	private int dynamicHp;
	private int setitemHp;
	private int nowMp;
	protected int maxMp;
	private int dynamicMp;
	private int setitemMp;
	private int ac;
	private int dynamicAc;
	private int setitemAc;
	private int Str;
	private int Con;
	private int Dex;
	private int Wis;
	private int Int;
	private int Cha;
	private int dynamicInt;
	private int dynamicStr;
	private int dynamicCon;
	private int dynamicDex;
	private int dynamicWis;
	private int dynamicCha;
	private int dynamicMr;
	private int dynamicSp;
	private int dynamicTicHp;
	private int dynamicTicMp;
	private double dynamicStunDefense;
	private int dynamicHoldDefense;
	private int dynamicAddWeight;
	private int dynamicAddPvpDmg;
	private int dynamicAddPvpReduction;
	private long lastHitTime;
	private ItemInstance weapon;
	private int minDmg;
	private int maxDmg;
	private int dmg;
	private int hitCount;
	private int setitemInt;
	private int setitemStr;
	private int setitemCon;
	private int setitemDex;
	private int setitemWis;
	private int setitemCha;
	private int setitemMr;
	private int setitemSp;
	private int setitemTicHp;
	private int setitemTicMp;
	private int setitemRed;
	private int setitemHit;
	private int setitemDmg;
	private int setitemBowHit;
	private int setitemBowDmg;
	private double setitemExp;
	private double exp;
	private int food;
	private double itemWeight; // 동적인 아이템을 더 들수있게 하는 변수.
	private int TimeHpTic; // 자연회복에 사용되는 변수
	private int TimeMpTic; // 자연회복에 사용되는 변수
	private boolean hpMove; // 자연회복에 사용되는 변수
	private boolean mpMove; // 자연회복에 사용되는 변수
	private boolean hpFight; // 자연회복에 사용되는 변수
	private boolean mpFight; // 자연회복에 사용되는 변수
	private int lvStr; // 51이상 부터 찍는 레벨스탯 변수.
	private int lvCon;
	private int lvDex;
	private int lvWis;
	private int lvInt;
	private int lvCha;
	private int elixirStr; // 엘릭서 사용된 갯수
	private int elixirCon;
	private int elixirDex;
	private int elixirWis;
	private int elixirInt;
	private int elixirCha;
	private int elixirReset; // 스탯초기화할때 이전에 복용한 엘릭서 전체갯수값.
	private int dynamicEarthress; // 땅 저항력
	private int dynamicWaterress; // 물 저항력
	private int dynamicFireress; // 불 저항력
	private int dynamicWindress; // 바람 저항력
	private int setitemEarthress; // 땅 저항력
	private int setitemWaterress; // 물 저항력
	private int setitemFireress; // 불 저항력
	private int setitemWindress; // 바람 저항력
	private int earthress; // 땅 저항력
	private int waterress; // 물 저항력
	private int fireress; // 불 저항력
	private int windress; // 바람 저항력
	private int reduction; // 리덕션(공격당한 데미지 감소)
	private int dynamicReduction; // 리덕션(공격당한 데미지 감소)
	private int magicdollTimeHpTic; // 마법인형 회복에 사용되는 변수
	private int magicdollHpTic;
	private int magicdollTimeMpTic; //마법인형 회복에 사용되는 변수
	private int magicdollMpTic;
	// 버프 딜레이 확인용 변수.
	public long delay_magic;
	// 마법 딜레이 체크 변수.
	public long magic_time;
	// 동적 데미지 추가연산 처리 변수.
	private int dynamicAddDmg; // 타격치
	private int dynamicAddDmgBow; // 활타격치
	private int dynamicAddHit; // 공성
	private int dynamicAddHitBow; // 활명중
	private int dynamicEr; // 장거리 회피율
	private int dynamicDg; // 근거리 회피율
	private int dynamicHealing; // 물약 회복량
	private int dynamicHealingPercent;// 물약 회복량 퍼센트
	private int dynamicStunLevel; // 스턴 레벨
	private double dynamicExp; // 추가 경험치 지급처리에 사용되는 변수(버프에서 증가 혹은 감소시킴)
	// 낚시를 위한 변수
	private boolean isFishing;
	private long fishingTime;
	private ItemInstance tempFishing;
	private BackgroundInstance fishEffect; // 낚시 이펙트
	private int fishStartHeading;
	
	private int luck;
	private int aden;

	private int six;
	private int five;

	private boolean dodge;
	private boolean evasion;

	private String fast_poly;

	private boolean isBlessArmor;

	private boolean isBlessAcc;

	//
	private Object sync_dynamic = new Object();
	
	public DeathEffect backGround;

	public Character() {
		//
	}

	@Override
	public void close() {
		super.close();
		level = nowHp = maxHp = dynamicHp = nowMp = maxMp = dynamicMp = ac = dynamicAc = Str = Con = Dex = Wis = Int = Cha = dynamicInt = dynamicStr = dynamicCon = dynamicDex = dynamicWis = dynamicCha = dynamicTicHp = dynamicTicMp = dynamicHoldDefense = lvStr = lvCon = lvDex = lvWis = lvInt = lvCha = dynamicSp = dynamicMr = food = dynamicEarthress = dynamicWaterress = dynamicFireress = dynamicWindress = earthress = waterress = fireress = windress = dynamicAddDmg = 0;
		itemWeight = exp = setitemHp = setitemMp = setitemInt = setitemStr = setitemCon = setitemDex = setitemWis = setitemCha = setitemMr = setitemSp = setitemTicHp = setitemTicMp = setitemRed = setitemHit = setitemDmg = setitemBowHit = setitemBowDmg = setitemEarthress = setitemWaterress = setitemFireress = setitemWindress = dynamicAddDmgBow = dynamicAddHit = dynamicAddHitBow = setitemAc = reduction = dynamicReduction = dynamicEr = dynamicDg = elixirStr = elixirDex = elixirCon = elixirWis = elixirInt = elixirCha = elixirReset = dynamicHealing = dynamicHealingPercent = dynamicAddWeight = dynamicAddPvpDmg = dynamicAddPvpReduction = dynamicStunLevel = 0;
		dynamicStunDefense = minDmg = maxDmg = dmg = hitCount = 0;
		fishStartHeading = 0;
		dynamicExp = setitemExp = 0;
		hpMove = mpMove = hpFight = mpFight = dodge = evasion = isBlessArmor = isBlessAcc = isFishing = false;
		delay_magic = magicdollHpTic = magicdollMpTic = 0;
		magic_time = 0;
		luck = six = five = 0;
		fishingTime = lastHitTime = 0L;
		aden = 1;
		TimeHpTic = getHpTime();
		TimeMpTic = getMpTime();
		fishEffect = null;
		fast_poly = "";
		weapon = tempFishing = null;
	}
	
	public String getFast_poly() {
		return fast_poly;
	}

	public void setFast_poly(String fast_poly) {
		this.fast_poly = fast_poly;
	}

	public int getDynamicReduction() {
		return dynamicReduction;
	}

	public void setDynamicReduction(int dynamicReduction) {
		this.dynamicReduction = dynamicReduction;
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

	public int getDynamicAddDmg() {
		return dynamicAddDmg;
	}

	public void setDynamicAddDmg(int dynamicAddDmg) {
		this.dynamicAddDmg = dynamicAddDmg;
	}

	public int getDynamicEr() {
		return dynamicEr;
	}

	public void setDynamicEr(int dynamicEr) {
		this.dynamicEr = dynamicEr;
	}

	public int getDynamicDg() {
		return dynamicDg;
	}

	public void setDynamicDg(int dynamicDg) {
		this.dynamicDg = dynamicDg;
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

	public int getDynamicStunLevel() {
		return dynamicStunLevel;
	}

	public void setDynamicStunLevel(int dynamicStunLevel) {
		this.dynamicStunLevel = dynamicStunLevel;
	}

	public int getDynamicAddWeight() {
		return dynamicAddWeight;
	}

	public void setDynamicAddWeight(int dynamicAddWeight) {
		this.dynamicAddWeight = dynamicAddWeight;
	}

	public int getDynamicAddPvpDmg() {
		return dynamicAddPvpDmg;
	}

	public void setDynamicAddPvpDmg(int dynamicAddPvpDmg) {
		this.dynamicAddPvpDmg = dynamicAddPvpDmg;
	}
	
	public int getDynamicAddPvpReduction() {
		return dynamicAddPvpReduction;
	}

	public void setDynamicAddPvpReduction(int dynamicAddPvpReduction) {
		this.dynamicAddPvpReduction = dynamicAddPvpReduction;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public long getLastHitTime() {
		return lastHitTime;
	}

	public void setLastHitTime(long lastHitTime) {
		this.lastHitTime = lastHitTime;
	}

	public ItemInstance getWeapon() {
		return weapon;
	}

	public void setWeapon(ItemInstance weapon) {
		this.weapon = weapon;
	}
	
	public int getMinDmg() {
		return minDmg;
	}

	public void setMinDmg(int minDmg) {
		this.minDmg = minDmg;
	}

	public int getMaxDmg() {
		return maxDmg;
	}

	public void setMaxDmg(int maxDmg) {
		this.maxDmg = maxDmg;
	}
	
	public int getDmg() {
		return dmg;
	}

	public void setDmg(int dmg) {
		this.dmg = dmg;
	}

	public int getHitCount() {
		return hitCount;
	}

	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}
	
	@Override
	public int getLevel() {
		return level;
	}

	public int getMagicdollHpTic() {
		return magicdollHpTic;
	}

	public void setMagicdollHpTic(int magicdollHpTic) {
		this.magicdollHpTic = magicdollHpTic;
	}
	
	public int getMagicdollMpTic() {
		return magicdollMpTic;
	}

	public void setMagicdollMpTic(int magicdollMpTic) {
		this.magicdollMpTic = magicdollMpTic;
	}
	
	@Override
	public void setNowHp(int nowHp) {
		synchronized (sync_dynamic) {
			if(!isDead()){
				if(getTotalHp() < nowHp){
					nowHp = getTotalHp();
				}else if(nowHp <= 0){
					if(getGm()>0){
						nowHp = getTotalHp();
					}else{
						nowHp = 0;
						setDead(true);
						if (this instanceof PcInstance) {
						if (Lineage.is_character_dead_effect) {
							if (!World.isBattleZone(getX(), getY(), getMap())) {
								setDeathEffect(true);
                                toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, 12111), false);
								}
                        }
                    }
                }
            }
            this.nowHp = nowHp;
        }
    }
}
	@Override
	public int getNowHp() {
		synchronized (sync_dynamic) {
			if (getTotalHp() < nowHp)
				nowHp = getTotalHp();
			return nowHp;
		}
	}

	@Override
	public void setMaxHp(int maxHp) {
		int temp = Lineage.royal_hp;
		switch (getClassType()) {
		case 0x01:
			temp = Lineage.knight_hp;
			break;
		case 0x02:
			temp = Lineage.elf_hp;
			break;
		case 0x03:
			temp = Lineage.wizard_hp;
			break;
		case 0x04:
			temp = Lineage.darkelf_hp;
			break;
		case 0x05:
			temp = Lineage.dragonknight_hp;
			break;
		case 0x06:
			temp = Lineage.blackwizard_hp;
			break;
		case 0x07:
			temp = Lineage.warrior_hp;
			break;
		}
		if (maxHp < temp)
			maxHp = temp;
		this.maxHp = maxHp;
	}

	@Override
	public int getMaxHp() {
		return maxHp;
	}

	public void setDynamicHp(int dynamicHp) {
		synchronized (sync_dynamic) {
			this.dynamicHp = dynamicHp;
		}
	}

	public int getDynamicHp() {
		synchronized (sync_dynamic) {
			return dynamicHp;
		}
	}

	@Override
	public void setNowMp(int nowMp) {
		synchronized (sync_dynamic) {
			if (!isDead()) {
				if (getTotalMp() < nowMp) {
					nowMp = getTotalMp();
				} else if (nowMp <= 0) {
					nowMp = 0;
				}
				this.nowMp = nowMp;
			}
		}
	}

	@Override
	public int getNowMp() {
		synchronized (sync_dynamic) {
			if (getTotalMp() < nowMp)
				nowMp = getTotalMp();
			return nowMp;
		}
	}

	@Override
	public void setMaxMp(int maxMp) {
		int temp = Lineage.royal_mp;
		switch (getClassType()) {
		case 0x01:
			temp = Lineage.knight_mp;
			break;
		case 0x02:
			temp = Lineage.elf_mp;
			break;
		case 0x03:
			temp = Lineage.wizard_mp;
			break;
		case 0x04:
			temp = Lineage.darkelf_mp;
			break;
		case 0x05:
			temp = Lineage.dragonknight_mp;
			break;
		case 0x06:
			temp = Lineage.blackwizard_mp;
			break;
		case 0x07:
			temp = Lineage.warrior_mp;
			break;
		}
		if (maxMp < temp)
			maxMp = temp;
		this.maxMp = maxMp;
	}

	@Override
	public int getMaxMp() {
		return maxMp;
	}

	public void setDynamicMp(int dynamicMp) {
		synchronized (sync_dynamic) {
			this.dynamicMp = dynamicMp;
		}
	}

	public int getDynamicMp() {
		synchronized (sync_dynamic) {
			return dynamicMp;
		}
	}

	public void setAc(int ac) {
		if (ac < 0)
			ac = 0;
		if (ac > Lineage.MAX_AC)
			ac = Lineage.MAX_AC;
		this.ac = ac;
	}

	public int getAc() {
		return ac;
	}

	public void setDynamicAc(int dynamicAc) {
		synchronized (sync_dynamic) {
			this.dynamicAc = dynamicAc;
		}
	}

	public int getDynamicAc() {
		synchronized (sync_dynamic) {
			return dynamicAc;
		}
	}

	public void setStr(int Str) {
		this.Str = Str;
	}

	public int getStr() {
		return Str;
	}

	public void setCon(int Con) {
		this.Con = Con;
	}

	public int getCon() {
		return Con;
	}

	public void setDex(int Dex) {
		this.Dex = Dex;
	}

	public int getDex() {
		return Dex;
	}

	public void setWis(int Wis) {
		this.Wis = Wis;
	}

	public int getWis() {
		return Wis;
	}

	public void setInt(int Int) {
		this.Int = Int;
	}

	public int getInt() {
		return Int;
	}

	public void setCha(int Cha) {
		this.Cha = Cha;
	}

	public int getCha() {
		return Cha;
	}

	public void setDynamicStr(int dynamicStr) {
		this.dynamicStr = dynamicStr;
	}

	public int getDynamicStr() {
		return dynamicStr;
	}

	public void setDynamicCon(int dynamicCon) {
		this.dynamicCon = dynamicCon;
	}

	public int getDynamicCon() {
		return dynamicCon;
	}

	public void setDynamicDex(int dynamicDex) {
		this.dynamicDex = dynamicDex;
	}

	public int getDynamicDex() {
		return dynamicDex;
	}

	public void setDynamicWis(int dynamicWis) {
		this.dynamicWis = dynamicWis;
	}

	public int getDynamicWis() {
		return dynamicWis;
	}

	public void setDynamicInt(int dynamicInt) {
		this.dynamicInt = dynamicInt;
	}

	public int getDynamicInt() {
		return dynamicInt;
	}

	public void setDynamicCha(int dynamicCha) {
		this.dynamicCha = dynamicCha;
	}

	public int getDynamicCha() {
		return dynamicCha;
	}

	public int getLvStr() {
		return lvStr;
	}

	public void setLvStr(int lvStr) {
		this.lvStr = lvStr;
	}

	public int getLvCon() {
		return lvCon;
	}

	public void setLvCon(int lvCon) {
		this.lvCon = lvCon;
	}

	public int getLvDex() {
		return lvDex;
	}

	public void setLvDex(int lvDex) {
		this.lvDex = lvDex;
	}

	public int getLvWis() {
		return lvWis;
	}

	public void setLvWis(int lvWis) {
		this.lvWis = lvWis;
	}

	public int getLvInt() {
		return lvInt;
	}

	public void setLvInt(int lvInt) {
		this.lvInt = lvInt;
	}

	public int getLvCha() {
		return lvCha;
	}

	public void setLvCha(int lvCha) {
		this.lvCha = lvCha;
	}

	public int getLvStat() {
		return lvStr + lvDex + lvCon + lvWis + lvInt + lvCha;
	}

	public int getElixirStr() {
		return elixirStr;
	}

	public void setElixirStr(int elixirStr) {
		this.elixirStr = elixirStr;
	}

	public int getElixirCon() {
		return elixirCon;
	}

	public void setElixirCon(int elixirCon) {
		this.elixirCon = elixirCon;
	}

	public int getElixirDex() {
		return elixirDex;
	}

	public void setElixirDex(int elixirDex) {
		this.elixirDex = elixirDex;
	}

	public int getElixirWis() {
		return elixirWis;
	}

	public void setElixirWis(int elixirWis) {
		this.elixirWis = elixirWis;
	}

	public int getElixirInt() {
		return elixirInt;
	}

	public void setElixirInt(int elixirInt) {
		this.elixirInt = elixirInt;
	}

	public int getElixirCha() {
		return elixirCha;
	}

	public void setElixirCha(int elixirCha) {
		this.elixirCha = elixirCha;
	}

	public int getElixirReset() {
		return elixirReset;
	}

	public void setElixirReset(int elixirReset) {
		this.elixirReset = elixirReset;
	}

	public int getElixirStat() {
		return elixirStr + elixirDex + elixirCon + elixirWis + elixirInt + elixirCha;
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

	public double getDynamicExp() {
		return dynamicExp;
	}

	public void setDynamicExp(double dynamicExp) {
		this.dynamicExp = dynamicExp;
		if (this.dynamicExp < 0)
			this.dynamicExp = 0;
	}

	public double getItemWeight() {
		return itemWeight;
	}

	public void setItemWeight(double itemWeight) {
		this.itemWeight = itemWeight;
	}

	public double getExp() {
		synchronized (sync_dynamic) {
			return exp;
		}
	}

	public void setExp(double exp) {
		synchronized (sync_dynamic) {
			if (exp < 0)
				exp = 0;
			this.exp = exp;
		}
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		if (food >= Lineage.MAX_FOOD)
			food = Lineage.MAX_FOOD;
		if (food < 0)
			food = 0;
		this.food = food;
	}

	public int getDynamicEarthress() {
		return dynamicEarthress;
	}

	public void setDynamicEarthress(int dynamicEarthress) {
		this.dynamicEarthress = dynamicEarthress;
	}

	public int getDynamicWaterress() {
		return dynamicWaterress;
	}

	public void setDynamicWaterress(int dynamicWaterress) {
		this.dynamicWaterress = dynamicWaterress;
	}

	public int getDynamicFireress() {
		return dynamicFireress;
	}

	public void setDynamicFireress(int dynamicFireress) {
		this.dynamicFireress = dynamicFireress;
	}

	public int getDynamicWindress() {
		return dynamicWindress;
	}

	public void setDynamicWindress(int dynamicWindress) {
		this.dynamicWindress = dynamicWindress;
	}

	public int getEarthress() {
		return earthress;
	}

	public void setEarthress(int earthress) {
		this.earthress = earthress;
	}

	public int getWaterress() {
		return waterress;
	}

	public void setWaterress(int waterress) {
		this.waterress = waterress;
	}

	public int getFireress() {
		return fireress;
	}

	public void setFireress(int fireress) {
		this.fireress = fireress;
	}

	public int getWindress() {
		return windress;
	}

	public void setWindress(int windress) {
		this.windress = windress;
	}

	public int getSetitemHp() {
		return setitemHp;
	}

	public void setSetitemHp(int setitemHp) {
		this.setitemHp = setitemHp;
	}

	public int getSetitemMp() {
		return setitemMp;
	}

	public void setSetitemMp(int setitemMp) {
		this.setitemMp = setitemMp;
	}

	public int getSetitemAc() {
		return setitemAc;
	}

	public void setSetitemAc(int setitemAc) {
		this.setitemAc = setitemAc;
	}

	public int getSetitemInt() {
		return setitemInt;
	}

	public void setSetitemInt(int setitemInt) {
		this.setitemInt = setitemInt;
	}

	public int getSetitemStr() {
		return setitemStr;
	}

	public void setSetitemStr(int setitemStr) {
		this.setitemStr = setitemStr;
	}

	public int getSetitemCon() {
		return setitemCon;
	}

	public void setSetitemCon(int setitemCon) {
		this.setitemCon = setitemCon;
	}

	public int getSetitemDex() {
		return setitemDex;
	}

	public void setSetitemDex(int setitemDex) {
		this.setitemDex = setitemDex;
	}

	public int getSetitemWis() {
		return setitemWis;
	}

	public void setSetitemWis(int setitemWis) {
		this.setitemWis = setitemWis;
	}

	public int getSetitemCha() {
		return setitemCha;
	}

	public void setSetitemCha(int setitemCha) {
		this.setitemCha = setitemCha;
	}

	public int getSetitemMr() {
		return setitemMr;
	}

	public void setSetitemMr(int setitemMr) {
		this.setitemMr = setitemMr;
	}

	public int getSetitemSp() {
		return setitemSp;
	}

	public void setSetitemSp(int setitemSp) {
		this.setitemSp = setitemSp;
	}

	public int getSetitemTicHp() {
		return setitemTicHp;
	}

	public void setSetitemTicHp(int setitemTicHp) {
		this.setitemTicHp = setitemTicHp;
	}

	public int getSetitemTicMp() {
		return setitemTicMp;
	}

	public void setSetitemTicMp(int setitemTicMp) {
		this.setitemTicMp = setitemTicMp;
	}

	public int getSetitemEarthress() {
		return setitemEarthress;
	}

	public void setSetitemEarthress(int setitemEarthress) {
		this.setitemEarthress = setitemEarthress;
	}

	public int getSetitemWaterress() {
		return setitemWaterress;
	}

	public void setSetitemWaterress(int setitemWaterress) {
		this.setitemWaterress = setitemWaterress;
	}

	public int getSetitemFireress() {
		return setitemFireress;
	}

	public void setSetitemFireress(int setitemFireress) {
		this.setitemFireress = setitemFireress;
	}

	public int getSetitemWindress() {
		return setitemWindress;
	}

	public void setSetitemWindress(int setitemWindress) {
		this.setitemWindress = setitemWindress;
	}

	public int getMagicdollTimeHpTic() {
		return magicdollTimeHpTic;
	}

	public void setMagicdollTimeHpTic(int magicdollTimeHpTic) {
		this.magicdollTimeHpTic = magicdollTimeHpTic;
	}

	public int getMagicdollTimeMpTic() {
		return magicdollTimeMpTic;
	}

	public void setMagicdollTimeMpTic(int magicdollTimeMpTic) {
		this.magicdollTimeMpTic = magicdollTimeMpTic;
	}
	
	public int getSetitemRed() {
		return setitemRed;
	}

	public void setSetitemRed(int setitemRed) {
		this.setitemRed = setitemRed;
	}

	public int getSetitemHit() {
		return setitemHit;
	}

	public void setSetitemHit(int setitemHit) {
		this.setitemHit = setitemHit;
	}
	
	public int getSetitemDmg() {
		return setitemDmg;
	}

	public void setSetitemDmg(int setitemDmg) {
		this.setitemDmg = setitemDmg;
	}

	public int getSetitemBowHit() {
		return setitemBowHit;
	}

	public void setSetitemBowHit(int setitemBowHit) {
		this.setitemBowHit = setitemBowHit;
	}

	public int getSetitemBowDmg() {
		return setitemBowDmg;
	}

	public void setSetitemBowDmg(int setitemBowDmg) {
		this.setitemBowDmg = setitemBowDmg;
	}
	
	public double getSetitemExp() {
		return setitemExp;
	}

	public void setSetitemExp(double setitemExp) {
		this.setitemExp = setitemExp;
	}
	
	public int getReduction() {
		return reduction;
	}

	public void setReduction(int reduction) {
		this.reduction = reduction;
	}

	public int getTotalAddHit() {
		return dynamicAddHit + setitemHit;
	}

	public int getTotalAddHitBow() {
		return dynamicAddHitBow + setitemBowHit;
	}
	
	public int getTotalAddDmg() {
		return dynamicAddDmg + setitemDmg;
	}

	public int getTotalAddDmgBow() {
		return dynamicAddDmgBow + setitemBowDmg;
	}
	
	public int getFishStartHeading() {
		return fishStartHeading;
	}

	public void setFishStartHeading(int fishStartHeading) {
		this.fishStartHeading = fishStartHeading;
	}

	public BackgroundInstance getFishEffect() {
		return fishEffect;
	}

	public void setFishEffect(BackgroundInstance fishEffect) {
		this.fishEffect = fishEffect;
	}

	public ItemInstance getTempFishing() {
		return tempFishing;
	}

	public void setTempFishing(ItemInstance tempFishing) {
		this.tempFishing = tempFishing;
	}

	public long getFishingTime() {
		return fishingTime;
	}

	public void setFishingTime(long fishingTime) {
		this.fishingTime = fishingTime;
	}

	public boolean isFishing() {
		return isFishing;
	}

	public void setFishing(boolean isFishing) {
		this.isFishing = isFishing;
	}
	
	public int getTotalStr() {
		int sum = 0;
		sum += Str + dynamicStr + lvStr + elixirStr + setitemStr;
		if (sum >= 250)
			sum = 249;
		return sum;
	}

	public int getTotalDex() {
		int sum = 0;
		sum += Dex + dynamicDex + lvDex + elixirDex + setitemDex;
		if (sum >= 250)
			sum = 249;
		return sum;
	}

	public int getTotalCon() {
		return Con + dynamicCon + lvCon + elixirCon + setitemCon;
	}

	public int getTotalWis() {
		return Wis + dynamicWis + lvWis + elixirWis + setitemWis;
	}

	public int getTotalInt() {
		int sum = 0;
		sum += Int + dynamicInt + lvInt + elixirInt + setitemInt;
		if (sum >= 250)
			sum = 249;
		return sum;
	}

	public int getTotalCha() {
		return Cha + dynamicCha + lvCha + elixirCha + setitemCha;
	}

	public int getTotalHp() {
		return maxHp + dynamicHp + setitemHp;
	}

	public int getTotalMp() {
		return maxMp + dynamicMp + setitemMp;
	}

	public int getTotalEarthress() {
		return earthress + dynamicEarthress + setitemEarthress;
	}

	public int getTotalWaterress() {
		return waterress + dynamicWaterress + setitemWaterress;
	}

	public int getTotalFireress() {
		return fireress + dynamicFireress + setitemFireress;
	}

	public int getTotalWindress() {
		return windress + dynamicWindress + setitemWindress;
	}

	public int getTotalAc() {
		//
		Object o = PluginController.init(Character.class, "getTotalAc", this);
		if (o != null)
			return (int) o;
		//
		int total_ac = ac + dynamicAc + setitemAc + (Lineage.is_dex_ac ? getAcDex() : 0);
		// -128
		return total_ac > 138 ? 138 : total_ac;
	}

	public int getTotalReduction() {
		int totalReduction = reduction + dynamicReduction + setitemRed;
		
		if(this instanceof PcInstance && getLevel() >= 50)
			totalReduction += 2;
		return totalReduction;
	}

	public double getTotalExp() {
		return dynamicExp + setitemExp;
	}
	
	public int getTotalDg() {
		return dynamicDg;
	}

	public int getMonsterKillCount() {
		return 0;
	}

	public void setMonsterKillCount(int monsterKillCount) {
	}

	public int getTotalSp() {
		return dynamicSp + setitemSp;
	}

	/**
	 * 현재 hp를 백분율로 연산하여 리턴함.
	 * 
	 * @return
	 */
	public int getHpPercent() {
		double nowhp = getNowHp();
		double maxhp = getTotalHp();
		return (int) ((nowhp / maxhp) * 100);
	}

	/**
	 * 현재 mp를 백분율로 연산하여 리턴함.
	 * 
	 * @return
	 */
	public int getMpPercent() {
		double nowmp = getNowMp();
		double maxmp = getTotalMp();
		return (int) ((nowmp / maxmp) * 100);
	}

	/**
	 * 틱 타임 추출 : 여기 녀석이 틱타임 주기를 리턴하는 함수입니다.
	 */
	public int getHpTime() {
		int time = 45;
		if (food >= Lineage.MAX_FOOD * 0.16) {
			time -= 10;
			if (food >= Lineage.MAX_FOOD * 0.5) {
				time -= 7;
				if (food >= Lineage.MAX_FOOD * 0.96) {
					time -= 5;
				}
			}
		}
		// 아지트좌표 일경우 틱처리
		if (AgitController.isAgitLocation(this))
			time -= 10;
		// 여관일경우 틱 처리.
		if (InnController.isInnMap(this))
			time -= 10;
		// 순수스탯에따른 틱감소하기
		time -= toOriginalStatHpTic();
		if (time < 0)
			time = 0;
		return time;
	}

	/**
	 * 틱 타임 추출
	 */
	public int getMpTime() {
		int time = 45;
		if (food >= Lineage.MAX_FOOD * 0.16) {
			time -= 8;
			if (food >= Lineage.MAX_FOOD * 0.5) {
				time -= 6;
				if (food >= Lineage.MAX_FOOD * 0.96) {
					time -= 4;
				}
			}
		}
		// 아지트좌표 일경우 틱처리
		if (AgitController.isAgitLocation(this))
			time -= 10;
		// 여관일경우 틱 처리.
		if (InnController.isInnMap(this))
			time -= 10;
		// 순수스탯에따른 틱감소하기
		time -= toOriginalStatMpTic();
		if (time < 0)
			time = 0;
		return time;
	}
	
	public boolean isHpTic() {
		// 마법인형 관리
		if (--magicdollTimeHpTic <= 0 && magicdollHpTic > 0) {
			magicdollTimeHpTic = 32;

			setNowHp(getNowHp() + magicdollHpTic);
		}
		// 버서커스 상태일 경우 회복 불가
		if (isBuffBerserks())
			return false;

		if (--TimeHpTic <= 0) {
			moving = false;
			fight = false;
			hpFight = false;
			hpMove = false;
			TimeHpTic = getHpTime(); // HP 회복(틱) 1 당 HP 1을 회복하며 서 있는 경우에는 3초, 이동하는 경우에는 6초, 전투 시에는 12초마다 HP를 회복한다.
			return true;
		}
		return false;
	}

	public boolean isMpTic() {
		// 마법인형 관리
		if (--magicdollTimeMpTic <= 0 && magicdollMpTic > 0) {
			magicdollTimeMpTic = 64;

			setNowMp(getNowMp() + magicdollMpTic);
			toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, Lineage.doll_mana_effect), false);
		}

		if (--TimeMpTic <= 0) {
			moving = false;
			fight = false;
			mpFight = false;
			mpMove = false;
			TimeMpTic = getMpTime(); // MP 회복(틱) 1 당 MP 1을 회복하며 서 있는 경우에는 8초, 이동하는 경우에는 16초, 전투 시에는 32초마다 MP를 회복한다
			if (isBuffMeditation())
				setBuffMeditaitonLevel(getBuffMeditaitonLevel() + 1);
			return true;
		}
		return false;
	}
/*
	public boolean isHpTic() {
		if (--TimeHpTic <= 0) {
			moving = false;
			fight = false;
			hpFight = false;
			hpMove = false;
			TimeHpTic = getHpTime();
			return true;
		}
		return false;
	}

	public boolean isMpTic() {
		if (--TimeMpTic <= 0) {
			moving = false;
			fight = false;
			mpFight = false;
			mpMove = false;
			TimeMpTic = getMpTime();
			return true;
		}
		return false;
	}
*/
	/**
	 * 자연회복의 hp상승값을 리턴함. : 여기 함수가 hp틱 상승값을 리턴하는 녀석입니다.
	 */
	public int hpTic() {
		int tic = dynamicTicHp + setitemTicHp + 4;

		// 스탯에 따른 틱 계산
		switch (getTotalCon()) {
		case 25:
		case 24:
			tic += 10;
		case 23:
		case 22:
			tic += 9;
		case 21:
		case 20:
			tic += 8;
		case 19:
		case 18:
			tic += 7;
		case 17:
		case 16:
			tic += 6;
		case 15:
		case 14:
			tic += 5;
		case 13:
		case 12:
			tic += 4;
		case 11:
		case 10:
			tic += 3;
		case 9:
		case 8:
			tic += 2;
		case 7:
		case 6:
			tic += 1;
		default:
			if (getTotalCon() > 25)
				tic += 10 + 9 + 8 + 7 + 6 + 5 + 4 + 3 + 2 + 1 + (getTotalCon() - 25);
			tic += 1;
		}

		// 여관맵일경우 틱처리
		if (InnController.isInnMap(this))
			tic += 20;
		// 아지트일경우 틱처리
		if (AgitController.isAgitLocation(this))
			tic += 20;
		// 내성 틱처리
		if (KingdomController.isKingdomInsideLocation(this))
			tic += 20;
		// 엄마나무 틱처리
		if (getClassType() == 0x02 && ElvenforestController.isTreeZone(this))
			tic += 30;
		// 신비한 프리즘 : 해츨링
		if (isMagicdollRamia())
			tic += 5;
		return tic <= 0 ? 1 : Util.random(1, tic);
	}

	// .틱 명령어에서 사용중. HP틱은 1 ~ 최대 tic까지 랜덤으로 회복되므로 최대tic값을 리턴
	public int getHpTic() {
		int tic = dynamicTicHp + setitemTicHp + 4;

		// 스탯에 따른 틱 계산
		switch (getTotalCon()) {
		case 25:
		case 24:
			tic += 10;
		case 23:
		case 22:
			tic += 9;
		case 21:
		case 20:
			tic += 8;
		case 19:
		case 18:
			tic += 7;
		case 17:
		case 16:
			tic += 6;
		case 15:
		case 14:
			tic += 5;
		case 13:
		case 12:
			tic += 4;
		case 11:
		case 10:
			tic += 3;
		case 9:
		case 8:
			tic += 2;
		case 7:
		case 6:
			tic += 1;
		default:
			if (getTotalCon() > 25)
				tic += 10 + 9 + 8 + 7 + 6 + 5 + 4 + 3 + 2 + 1 + (getTotalCon() - 25);
			tic += 1;
		}

		// 여관맵일경우 틱처리
		if (InnController.isInnMap(this))
			tic += 20;
		// 아지트일경우 틱처리
		if (AgitController.isAgitLocation(this))
			tic += 20;
		// 내성 틱처리
		if (KingdomController.isKingdomInsideLocation(this))
			tic += 20;
		// 엄마나무 틱처리
		if (getClassType() == 0x02 && ElvenforestController.isTreeZone(this))
			tic += 30;
		return tic <= 0 ? 1 : tic;
	}
	
	/**
	 * 자연회복의 mp상승값을 리턴함.
	 */
	public int mpTic() {
		int tic = dynamicTicMp + setitemTicMp;

		// 스탯에 따른 틱 계산
		switch (getTotalWis()) {
		case 25:
		case 24:
			tic += 7;
		case 23:
		case 22:
			tic += 6;
		case 21:
		case 20:
			tic += 5;
		case 19:
		case 18:
			tic += 4;
		case 17:
		case 16:
			tic += 3;
		case 15:
		case 14:
			tic += 2;
		case 13:
		case 12:
			tic += 1;
		default:
			if (getTotalWis() > 25)
				tic += 7 + 6 + 5 + 4 + 3 + 2 + 1 + (getTotalWis() - 25);
			tic += 2;
		}

		// 여관맵일경우 틱처리
		if (InnController.isInnMap(this))
			tic += 20;
		// 아지트일경우 틱처리
		if (AgitController.isAgitLocation(this))
			tic += 20;
		// 내성 틱처리
		if (KingdomController.isKingdomInsideLocation(this))
			tic += 20;
		// 엄마나무 틱처리
		if (getClassType() == 0x02 && ElvenforestController.isTreeZone(this))
			tic += 30;
		// 마법인형 : 라미아
		if (isMagicdollRamia())
			tic += 4;

		// 0보다 작을경우 0을 리턴하도록 함.
		return tic < 0 ? 0 : tic;
	}
	// .틱 명령어에서 사용중.
		public int getMpTic() {
			int tic = dynamicTicMp + setitemTicMp;

			// 스탯에 따른 틱 계산
			switch (getTotalWis()) {
			case 25:
			case 24:
				tic += 7;
			case 23:
			case 22:
				tic += 6;
			case 21:
			case 20:
				tic += 5;
			case 19:
			case 18:
				tic += 4;
			case 17:
			case 16:
				tic += 3;
			case 15:
			case 14:
				tic += 2;
			case 13:
			case 12:
				tic += 1;
			default:
				if (getTotalWis() > 25)
					tic += 7 + 6 + 5 + 4 + 3 + 2 + 1 + (getTotalWis() - 25);
				tic += 2;
			}

			// 여관맵일경우 틱처리
			if (InnController.isInnMap(this))
				tic += 20;
			// 아지트일경우 틱처리
			if (AgitController.isAgitLocation(this))
				tic += 20;
			// 내성 틱처리
			if (KingdomController.isKingdomInsideLocation(this))
				tic += 20;
			// 엄마나무 틱처리
			if (getClassType() == 0x02 && ElvenforestController.isTreeZone(this))
				tic += 30;
			// 마법인형 : 라미아
			if (isMagicdollRamia())
				tic += 4;

			// 0보다 작을경우 0을 리턴하도록 함.
			return tic < 0 ? 0 : tic;
		}
		
	/**
	 * 클레스별 순수스탯에 따른 mpTic +@ 리턴
	 */
	int toOriginalStatMpTic() {
		int sum = 0;
		int wis = Wis;
		switch (classType) {
		case 0x00:
			wis -= 11;
			if (wis >= 2)
				sum += 1;
			if (wis >= 4)
				sum += 1;
			break;
		case 0x01:
			wis -= 9;
			if (wis >= 2)
				sum += 1;
			if (wis >= 4)
				sum += 1;
			break;
		case 0x02:
			wis -= 12;
			if (wis >= 3)
				sum += 1;
			if (wis >= 6)
				sum += 1;
			break;
		case 0x04:
			wis -= 12;
			if (wis >= 3)
				sum += 1;
			if (wis >= 6)
				sum += 1;
			break;
		case 0x03:
			wis -= 12;
			if (wis >= 2)
				sum += 2;
			if (wis >= 4)
				sum += 2;
			if (wis >= 6)
				sum += 2;
			break;
		}
		return sum;
	}

	/**
	 * 클레스별 hp회복틱 리턴
	 */
	int toOriginalStatHpTic() {
		int sum = 0;
		int con = Con;
		switch (classType) {
		case 0x00:
			con -= 10;
			if (con >= 2)
				sum += 1;
			if (con >= 4)
				sum += 1;
			if (con >= 6)
				sum += 1;
			if (con >= 8)
				sum += 1;
			if (con >= 10)
				sum += 1;
			if (con >= 12)
				sum += 1;
			if (con >= 14)
				sum += 1;
			if (con >= 16)
				sum += 1;
			break;
		case 0x01:
			con -= 10;
			if (con >= 2)
				sum += 1;
			if (con >= 4)
				sum += 1;
			if (con >= 6)
				sum += 2;
			if (con >= 8)
				sum += 2;
			if (con >= 10)
				sum += 2;
			if (con >= 12)
				sum += 2;
			if (con >= 14)
				sum += 2;
			if (con >= 16)
				sum += 2;
			break;
		case 0x02:
			con -= 10;
			if (con >= 2)
				sum += 1;
			if (con >= 4)
				sum += 1;
			if (con >= 6)
				sum += 1;
			if (con >= 8)
				sum += 1;
			if (con >= 10)
				sum += 1;
			if (con >= 12)
				sum += 1;
			if (con >= 14)
				sum += 1;
			if (con >= 16)
				sum += 1;
			break;
		case 0x04:
			con -= 10;
			if (con >= 3)
				sum += 1;
			if (con >= 5)
				sum += 1;
			if (con >= 7)
				sum += 1;
			if (con >= 9)
				sum += 1;
			if (con >= 11)
				sum += 1;
			if (con >= 13)
				sum += 1;
			if (con >= 15)
				sum += 1;
			if (con >= 17)
				sum += 1;
			break;
		case 0x03:
			con -= 10;
			if (con >= 3)
				sum += 1;
			if (con >= 6)
				sum += 1;
			if (con >= 9)
				sum += 1;
			if (con >= 12)
				sum += 1;
			if (con >= 15)
				sum += 1;
			break;
		}
		return sum;
	}

	/**
	 * 클레스별 최대 무게소지 상승값 +@ 리턴
	 */
	public int toOriginalStatStrWeight() {
		int sum = 0;
		int str = Str;
		switch (classType) {
		case 0x00:
			str -= 13;
			if (str >= 1)
				sum += 1;
			if (str >= 4)
				sum += 1;
			if (str >= 7)
				sum += 1;
			break;
		case 0x01:
			str -= 12;
			if (str >= 2)
				sum += 1;
			if (str >= 4)
				sum += 1;
			if (str >= 6)
				sum += 1;
			if (str >= 8)
				sum += 1;
			if (str >= 10)
				sum += 1;
			break;
		case 0x02:
			str -= 11;
			if (str >= 5)
				sum += 1;
			break;
		case 0x04:
			str -= 12;
			if (str >= 5)
				sum += 1;
			break;
		case 0x03:
			str -= 8;
			if (str >= 1)
				sum += 1;
			break;
		}
		return sum;
	}

	/**
	 * 클레스별 콘에따른 최대무게소지 상승값 +@ 리턴
	 */
	public int toOriginalStatConWeight() {
		int sum = 0;
		int con = Con;
		switch (classType) {
		case 0x00:
			con -= 10;
			if (con >= 1)
				sum += 1;
			break;
		case 0x01:
			con -= 14;
			if (con >= 1)
				sum += 1;
			break;
		case 0x02:
			con -= 12;
			if (con >= 3)
				sum += 1;
			break;
		case 0x04:
			con -= 14;
			if (con >= 3)
				sum += 1;
			break;
		case 0x03:
			con -= 12;
			if (con >= 1)
				sum += 1;
			if (con >= 3)
				sum += 1;
			break;
		}
		return sum;
	}

	/**
	 * 케릭터는 자연회복에 영향을 받는 클레스임. 그렇기 때문에 이동하는 초기 시점이라면 자연회복을 좀 지연해야됨.
	 */
	@Override
	public void setMoving(boolean moving) {
		if (!this.moving && moving) {
			if (!hpMove) {
				hpMove = true;
				TimeHpTic += 8;
			}
			if (!mpMove) {
				mpMove = true;
				TimeMpTic += 8;
			}
		}
		super.setMoving(moving);
	}

	/**
	 * 케릭터는 자연회복에 영향을 받는 클레스임. 그렇기 때문에 전투가 활성화 되는 초기 시점이라면 자연회복을 좀 지연해야됨.
	 */
	@Override
	public void setFight(boolean fight) {
		if (!this.fight && fight) {
			if (!hpFight) {
				hpFight = true;
				TimeHpTic += 18;
			}
			if (!mpFight) {
				mpFight = true;
				TimeMpTic += 18;
			}
		}
		super.setFight(fight);
	}

	@Override
	public void toReset(boolean world_out) {
		super.toReset(world_out);
		if (isDead())
			// 버프제거..
			BuffController.toDead(this, world_out);
	}

	/**
	 * 덱스에 따른 ac +@ 연산 리턴.
	 * 
	 * @return
	 */
	public int getAcDex() {
		int total_dex = Dex + lvDex + elixirDex;
		int gab = total_dex >= 18 ? 4
				: total_dex <= 17 && total_dex >= 16 ? 5
						: total_dex <= 15 && total_dex >= 13 ? 6 : total_dex <= 12 && total_dex >= 10 ? 7 : 8;
		return level / gab;
	}

	public void setDeathEffect(boolean dead) {
		if (dead) {
			backGround = new DeathEffect();
			backGround.setGfx(13600);	
			backGround.setObjectId(ServerDatabase.nextEtcObjId());
			backGround.toTeleport(getX(), getY(), getMap(), false);
			backGround.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), backGround, 4), this instanceof PcInstance);
		} else {			
			if (isDead() && backGround != null) {
				backGround.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), backGround, 8), this instanceof PcInstance);
				backGround.clearList(true);
				World.remove(backGround);
				backGround = null;
			}
		}
	}
	
	/**
	 * 낚시터 확인
	 * 
	 */
	public boolean isFishingZone() {
		return getMap() == 5124 && getX() >= Lineage.FISHZONEX1 && getX() <= Lineage.FISHZONEX2 && getY() >= Lineage.FISHZONEY1 && getY() <= Lineage.FISHZONEY2;
	}
	
	public int getLuck() {
		return luck;
	}

	public void setLuck(int lu) {
		this.luck = lu;
	}

	public int getAden() {
		return aden;
	}

	public void setAden(int lu) {
		this.aden = lu;
	}

	public int getSix() {
		return six;
	}

	public void setSix(int six) {
		this.six = six;
	}

	public int getFive() {
		return five;
	}

	public void setFive(int fi) {
		this.five = fi;
	}

	public boolean getDodg() {
		return dodge;
	}

	public void setDodg(boolean dodg) {
		this.dodge = dodg;
	}

	public boolean getEvasion() {
		return evasion;
	}

	public void setEvasion(boolean evasion) {
		this.evasion = evasion;
	}

	public boolean isBlessArmor() {
		return isBlessArmor;
	}

	public void setBlessArmor(boolean isBlessArmor) {
		this.isBlessArmor = isBlessArmor;
	}

	public boolean isBlessAcc() {
		return isBlessAcc;
	}

	public void setBlessAcc(boolean isBlessAcc) {
		this.isBlessAcc = isBlessAcc;
	}

}
