package lineage.world.object;

import java.util.ArrayList;
import java.util.List;

import jsn_soft.TrueTargetController;
import lineage.bean.database.Monster;
import lineage.bean.database.Npc;
import lineage.bean.database.Poly;
import lineage.bean.lineage.Inventory;
import lineage.bean.lineage.Summon;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.ServerBasePacket;
import lineage.network.packet.server.S_Door;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectAdd;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectMap;
import lineage.network.packet.server.S_ObjectMoving;
import lineage.network.packet.server.S_ObjectRemove;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.EventController;
import lineage.world.controller.FightController;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.BoardInstance;
import lineage.world.object.instance.DwarfInstance;
import lineage.world.object.instance.GuardInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.MagicDollInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.NpcInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.PetInstance;
import lineage.world.object.instance.ShopInstance;
import lineage.world.object.instance.SummonInstance;
import lineage.world.object.instance.TeleportInstance;
import lineage.world.object.monster.TrapArrow;
import lineage.world.object.monster.event.JackLantern;
import lineage.world.object.npc.TalkNpc;
import lineage.world.object.npc.background.Cracker;
import lineage.world.object.npc.background.CrackerDmg;
import lineage.world.object.npc.background.DeathEffect;
import lineage.world.object.npc.background.Firewall;
import lineage.world.object.npc.background.LifeStream;
import lineage.world.object.npc.background.Racer;
import lineage.world.object.npc.background.Switch;
import lineage.world.object.npc.background.door.Door;
import lineage.world.object.npc.event.GhostHouseDoorButton;
import lineage.world.object.npc.kingdom.KingdomCastleTop;
import lineage.world.object.npc.kingdom.KingdomDoor;
import lineage.world.object.npc.kingdom.KingdomDoorman;
import lineage.world.object.npc.kingdom.KingdomGuard;
import lineage.world.object.robot.PcRobotInstance;

public class object {
	private List<object> insideList; // 12셀 내에있는 객체만 관리할 용도로 사용
	private List<object> allList; // 40셀 내에 있는 객체만 관리할 용도로 사용.
	protected long objectId;
	protected int clanGrade;
	protected String name;
	protected String title;
	protected int clanId;
	protected String clanName;
	protected int clanRank; // 혈맹 직위
	protected object own; // 객체를 관리하고 있는 객체
	protected long own_objectId; // 객체를 관리하고 있는 객체의 아이디
	protected String own_name; // 객체를 관리하고 있는 객체의 이름
	protected int x;
	protected int y;
	protected int map;
	protected int homeX;
	protected int homeY;
	protected int homeMap;
	protected int homeLoc;
	protected int homeHeading;
	protected int homeTile[]; // Door 객체에서 사용중. 스폰된 위치에 고유 타일값 기록을 위해.
	protected int tempX;
	protected int tempY;
	protected int gfx;
	protected int gfxMode;
	protected int classGfx;
	protected int classGfxMode;
	private int lawful;
	protected int heading; // 0~7
	protected int light; // 0~15
	private int speed; // 0~2
	private boolean brave; // 용기 및 와퍼 상태
	protected boolean eva; // 에바의 축복 상태
	protected boolean wisdom; // 지혜의 물약 상태
	private long count;
	protected int tempCount; //
	protected int classSex; // 0~1
	protected int classType; // pc[0~3]
	private boolean dead;
	protected boolean fight;
	protected boolean moving;
	private boolean poison; // 독감염 여부.
	private boolean lock_high; // 굳은상태 여부. 데미지 입지 않음.
	private boolean lock_low; // 굳은상태 여부. 데미지 입음.
	protected boolean invis; // 투명상태 여부.
	private long 투망시간; // 투명상태 여부.
	protected boolean transparent; // 절대적 투명상태 여부. 객체를 뚫을 수 있음. 몬스터도사용중(잭-오-랜턴)
	protected boolean worldDelete = true;//
	protected boolean worldJoin; // 월드접속이 완료되엇는지 여부. 유저 버프메세지 표시가능여부에 사용중.
	public object temp_object_1; // 임시 저장용 변수.
	protected int temp_hp;
	protected int temp_mp;
	private int CurseParalyzeCounter; // 커스:패럴라이즈가 실제 동작되는 시점 체크를 위해 사용하는 변수.
	protected Summon summon; // 서먼 객체 관리 변수. 자신이 해당 서먼객체 소속일경우 사용하는 변수.
	private boolean nameHidden; // 이름 및 타이틀을 표현할지 여부.
	private long _delaytime = 0;
	private boolean isBoardView = false; // 도움말
	public boolean isMapHack; // 맵핵체크를 위한 변수.
	public boolean isMark; // 마크를 위한 변수
	public int lastRank; // 랭킹 채팅 시스템을 위한 변수
	// 칼렉 변수
	public long lastDamageActionTime;
	//딜레이 패킷 1
	private boolean viewDelay;
	//딜레이 패킷 2
	private String viewDelaySkillName;
	// 허수아비 대미지 멘트
	private boolean damageMassage;
	// 전투 멘트
	private boolean isWarMessage;
	// 데미지 확인
	public boolean isDmgCheck;
	// 크리티컬 이팩트
	private boolean criticalEffect;
	private boolean criticalMagicEffect;
	
	private boolean BuffMaanFire;
	private boolean BuffMaanEarth;
	private boolean BuffMaanWatar;
	private boolean BuffMaanWind;
	private boolean BuffMaanLife;
	private boolean BuffMaanBirth;
	private boolean BuffMaanShape;
	
	private Poly lostPoly; // 강제 변신해제됫을경우 마지막 변신 값이 기록됨.
	
	public Object sync_damage = new Object(); // 대미지쪽 대미지를 입엇을때 동기화에 사용하기 위한것.
	// 인첸트 복구
	public long[] enchantRecovery;
	// 락용.
	protected Object sync_ai = new Object();
	protected Object sync_dynamic = new Object(); // 변수 조작시 동기화용.
	protected Object sync_macro = new Object(); /// 9월 22일 메크로 관련 추가.
	public Object sync_pickup = new Object();
	protected Object sync_poison = new Object();
	// 인공지능 처리 변수
	protected long ai_time;
	private long ai_start_time;
	protected int ai_status; // 인공지능제거[-1] 기본[0] 등등....
	protected long ai_showment_time; // 멘트 발생된 마지막 시간값 임시 저장용.
	protected int ai_showment_idx; // 출력된 멘트 위치 저장 변수.
	protected boolean ai_showment; // 멘트 발생여부.
	public double debug_idx;

	// 스피드핵 처리 참고용 변수
	public long AttackFrameTime;
	public long WalkFrameTime;
	public int SpeedhackWarningCounting;
	public int badPacketCount;
	public int goodPacketCount;
	public int gostHackCount;
	public int halfDelayCount;
	public long lastActionTime;
	public long lastMagicActionTime;
	// 촐기 용기 풀릴때 스핵감지 안되게 체크.
	public long speedCheck;
	
	public long magic_doll_effct_AttackObjId;


	// 마지막 움직인 시간
	public long lastMovingTime;

	// 자동물약
	public int autoPotionPercent;
	public boolean isAutoPotion;
	public String[] autoPotionIdx;
	public String autoPotionName;
	public boolean isAutoPotion2;
	// DPS
	public long dps_attack_time;
	
	// 혈맹레벨
	private boolean Level_1_Clan;
	private boolean Level_2_Clan;
	private boolean Level_3_Clan;
	private boolean Level_4_Clan;
	private boolean Level_5_Clan;
	private boolean Level_6_Clan;
	private boolean Level_7_Clan;
	// 경치 물약 변수
	private boolean BuffExpPotion;
	private boolean BuffExpPotion2;
	private boolean BuffExpPotion3;
	private boolean BuffExpPotion4;

	// 버프관리 쪽
	private boolean BuffDecreaseWeight;
	private boolean BuffHolyWeapon;
	private boolean BuffEnchantWeapon;
	private boolean BuffMonsterEyeMeat;
	private boolean BuffCurseParalyze;
	private boolean BuffBlessWeapon;
	private boolean BuffInvisiBility;
	private boolean BuffImmuneToHarm;
	private boolean BuffDecayPotion;
	private boolean BuffAbsoluteBarrier;
	private boolean BuffGlowingAura;
	private boolean BuffFireWeapon;
	private boolean BuffWindShot;
	private boolean BuffEraseMagic;
	private boolean BuffBurningWeapon;
	private boolean BuffStormShot;
	private boolean BuffWisdom;
	private boolean BuffEva;
	private boolean BuffBlessOfFire;
	private boolean BuffCurseGhoul;
	private boolean BuffCurseFloatingEye;
	private boolean BuffChattingClose;
	private boolean BuffChattingClosetwo;
	private boolean BuffDisease;
	private boolean BuffSilence;
	private boolean BuffEyeOfStorm;
	private boolean BuffWeakness;
	private boolean BuffCounterMagic;
	private boolean BuffCriminal;
	private boolean BuffMeditation;
	private boolean BuffFogOfSleeping;
	private boolean BuffEnchantVenom;
	private boolean BuffBurningSpirit;
	private boolean BuffVenomResist;
	private boolean BuffDoubleBreak;
	private boolean BuffShadowFang;
	private boolean BuffBerserks;
	private int BuffAdvanceSpiritHp;
	private int BuffAdvanceSpiritMp;
	private int BuffGiganticHp;
	private boolean BuffReductionArmor;
	private boolean BuffCounterBarrier;
	private boolean BuffCounterMirror;
	private boolean BuffBounceAttack;
	private boolean BuffCurseBlind;
	private boolean BuffExoticVitalize;
	private boolean BuffWaterLife;
	private boolean BuffElementalFire;
	private boolean BuffPolluteWater;
	private boolean BuffStrikerGale;
	private boolean BuffSoulOfFlame;
	private boolean BuffAdditionalFire;
	private boolean BuffSolidCarriage;
	private boolean BuffDragonSkin;
	private boolean BuffBurningSlash;
	private boolean BuffDesperado;
	private boolean BuffDetection;
	private boolean BuffTeleport;
	private boolean BuffAquaProtect;

	// 1단계 마법인형 (최종 수정)
	private boolean MagicdollStoneGolem;	// 돌 골렘
	private boolean MagicdollWerewolf;		// 늑대인간
	private boolean MagicdollBugbear;		// 버그베어
	private boolean MagicdollCrustacean;	// 크러스트시안
	private boolean MagicdollYeti;			// 에티
	private boolean MagicdollTree;			// 목각인형
	
	// 2단계 마법인형 (최종 수정)
	private boolean MagicdollSuccubus;		// 서큐버스
	private boolean MagicdollElder;			// 장로
	private boolean MagicdollCockatrice;	// 코카트리스
	private boolean MagicdollSnowmanB;		// 눈사람
	private boolean MagicdollMermaid;		// 인어
	private boolean MagicdollLavaGolem;		// 라바골렘
	
	// 3단계 마법인형 (최종 수정)
	private boolean MagicdollGiant;			// 자이언트
	private boolean MagicdollKingBugbear;	// 킹버그베어
	private boolean MagicdollBlackElder;	//흑장로
	private boolean MagicdollsuccubusQueen;	//서큐버스 퀸
	private boolean MagicdollDrake;			// 드레이크
	private boolean MagicdollDimondGolem;	//다이아몬드 골렘
	
	private boolean MagicdollGramlin; //그램린
	
	
	
	// 마법인형 관리쪽
	private boolean Magicdollnormal;
	private boolean MagicdollRamia;
	private boolean MagicdollSeaDancer;
	private boolean MagicdollSpartoi;
	private boolean MagicdollHatzling;
	private boolean MagicdollCracker;
	private boolean MagicdollDeathKnight;
	private boolean MagicdollCyclops;
	private boolean MagicdollLich;
	private boolean MagicdollSeer;
	private boolean MagicdollKnightVald;
	private boolean MagicdollDemon;
	// 추가되는 인형
	private boolean MagicdollBleg;
	private boolean MagicdollLleg;
	private boolean MagicdollGleg;
	private boolean MagicdollRleg;
	private boolean MagicdollIris;
	private boolean MagicdollBarancar;
	private boolean MagicdollBampaear;
	private boolean MagicdollIceQueen;
	private boolean MagicdollCuze;
	private boolean MagicdollTarak;
	private boolean MagicdollPapoorion;
	private boolean MagicdollLindvior;
	private boolean MagicdollAntaras;
	private boolean MagicdollValakas;
	private boolean MagicdollMummylord;
	private boolean MagicdollBaphomet;
	private boolean MagicdollGirtas;
	private boolean Magicdollroyal;
	private boolean Magicdollknight;
	private boolean Magicdollelf;
	private boolean Magicdollwizard;
	private boolean Magicdollroyal2;
	private boolean Magicdollknight2;
	private boolean Magicdollelf2;
	private boolean Magicdollwizard2;
	
	
	protected int _디스딜레이;

	// 9무기 이상 단수 능력치 설정하기
	private int WaterWeapon;
	private int WindWeapon;
	private int EarthWeapon;
	private int FireWeapon;

	private double Earring;
	private double Earring2;

	// 브레이브아바타, 그레이스아바타
	private boolean BuffBreaveAvt;
	private boolean BuffGreaseAvt;

	private boolean KnightHoly;

	// 메디테이션이 지속될수록 MP회복량 수치 증가를 위한 변수
	private int buffMeditaitonLevel;
	
	// 20180524 낚시관련 에러때문에 추가
	private boolean fish;

	private int macroPeriod = 0;
	private int macroIndex = 0;
	private long macroDelay = 0L;
	private ArrayList<String> MacroMessage = new ArrayList<String>();

	// 추가 드랍률
	private double addDropItemRate;
	// 추가 아덴 획득률
	private double addDropAdenRate;
	
	// 자동 칼질
	public boolean isAutoAttack;
	public object autoAttackTarget;
	public long autoAttackTime;
	public int targetX;
	public int targetY;

	// 디비처리에 사용되는 변수.
	private Object database_key; // npc_spawnlist(name), monster_spawnlist(uid)

	private int Shop_inven_num;
	public long shop_count;

	// 인벤토리 검사 변수
	private object inven_stat_object;

	private List<String> ShopList; // 샵리스트.

	private double temp_exp;
	// 트루타겟 변수
	private int time;
	// 세트 변신 여부
	public boolean isSetPoly;
	// 아데나 거래게시판 변수
	public int adena_sise;
	public int adena_page;
	public int adena_uid;

	private boolean persnalShopSellInsert;
	private boolean persnalShopSellEdit;
	private boolean persnalShopSellPriceSetting;
	private boolean persnalShopSellPriceReSetting;
	private boolean persnalShopPurchasePriceIn;;

	private boolean persnalShopEdit;
	private boolean persnalShopInsert;
	private boolean persnalShopPriceSetting;
	private boolean persnalShopAddPriceSetting;
	private boolean persnalShopPriceReset;

	private boolean characterMarblesUse;
	private boolean expMarblesUse;
	private boolean saveCharacter;
	private boolean saveExp;
	private long marblesObjectId;
	
	private boolean sound;

	public String sise_tel_name;
	
	private boolean macro_mag;

	// 혈맹그림자
	private int clan_shadow_step;
	private String robot_clan_name;
	
	public int attackPc;
	public object pcAttackTarget;
	public object() {
		insideList = new ArrayList<object>();
		allList = new ArrayList<object>();
		homeTile = new int[2];
		ShopList = new ArrayList<String>();

		close();
	}

	/**
	 * 사용 다된 객체에 대한 메모리 정리 함수.
	 */
	public void close() {

		persnalShopSellInsert = persnalShopSellEdit = persnalShopSellPriceSetting = persnalShopSellPriceReSetting = persnalShopPurchasePriceIn = characterMarblesUse = false;
		persnalShopEdit = persnalShopInsert = persnalShopPriceSetting = persnalShopAddPriceSetting = persnalShopPriceReset = saveCharacter = saveExp = expMarblesUse = false;
		marblesObjectId = 0;
		name = title = clanName = own_name = sise_tel_name = null;
		temp_object_1 = null;
		summon = null;
		own_objectId = objectId = x = y = map = clanRank = homeX = homeY = homeMap = gfx = gfxMode = classGfx = clanId = classGfxMode = lawful = heading = light = speed = classSex = classType = homeHeading = tempCount = homeLoc = Shop_inven_num = adena_sise = adena_page = adena_uid = clan_shadow_step = buffMeditaitonLevel = 0;
		ai_start_time = ai_time = ai_status = _디스딜레이 = 0;
		count = 1;
		Level_1_Clan = Level_2_Clan = Level_3_Clan = Level_4_Clan = Level_5_Clan = Level_6_Clan = Level_7_Clan = false;
		wisdom = eva = brave = dead = fight = moving = poison = lock_low = lock_high =  damageMassage  = isWarMessage = invis = transparent = fish = isMark = isMapHack = false;
		BuffDecreaseWeight = BuffHolyWeapon = BuffEnchantWeapon = BuffMonsterEyeMeat = BuffCurseParalyze = BuffBlessWeapon = BuffInvisiBility = BuffImmuneToHarm = BuffDecayPotion = BuffAbsoluteBarrier = BuffGlowingAura = BuffFireWeapon = BuffWindShot = BuffEraseMagic = BuffBurningWeapon = BuffStormShot = BuffWisdom = BuffEva = BuffBlessOfFire = BuffCurseGhoul = BuffCurseFloatingEye = BuffChattingClose = BuffChattingClosetwo = BuffDisease = BuffSilence = BuffEyeOfStorm = BuffWeakness = BuffCounterMagic = BuffCriminal = BuffMeditation = BuffFogOfSleeping = BuffEnchantVenom = BuffBurningSpirit = BuffVenomResist = BuffDoubleBreak = BuffShadowFang = BuffBerserks = MagicdollWerewolf = MagicdollStoneGolem = MagicdollCrustacean = MagicdollRamia = MagicdollBugbear = MagicdollSuccubus = MagicdollSeaDancer = MagicdollElder = MagicdollCockatrice = MagicdollSpartoi = MagicdollYeti = MagicdollHatzling = MagicdollCracker = BuffReductionArmor = BuffCounterBarrier = BuffCounterMirror = nameHidden = BuffBounceAttack = BuffCurseBlind = BuffExoticVitalize = BuffWaterLife = BuffElementalFire = BuffPolluteWater = BuffStrikerGale = BuffSoulOfFlame = BuffAdditionalFire = BuffSolidCarriage = BuffDragonSkin = BuffBurningSlash = BuffDesperado = MagicdollMermaid = MagicdollLavaGolem = MagicdollGiant = MagicdollBlackElder = MagicdollsuccubusQueen = MagicdollDeathKnight = MagicdollSnowmanB = BuffDetection = BuffTeleport = BuffAquaProtect = MagicdollCyclops = MagicdollLich = MagicdollGirtas = MagicdollValakas = MagicdollLindvior = MagicdollPapoorion = MagicdollAntaras = MagicdollCuze = MagicdollIceQueen = MagicdollBaphomet = Magicdollroyal = Magicdollknight = Magicdollelf = Magicdollwizard = Magicdollroyal2 = Magicdollknight2 = Magicdollelf2 = Magicdollwizard2 = MagicdollTarak = MagicdollBarancar = MagicdollMummylord = MagicdollBampaear = MagicdollIris = MagicdollDrake = MagicdollKingBugbear = MagicdollDimondGolem = MagicdollGramlin = MagicdollSeer = MagicdollKnightVald = MagicdollDemon = BuffBreaveAvt = BuffGreaseAvt = KnightHoly = MagicdollTree = criticalEffect = criticalMagicEffect = false;
		BuffGiganticHp = BuffAdvanceSpiritHp = BuffAdvanceSpiritMp = lastRank = 0;
		_delaytime = 0L;
		WaterWeapon = WindWeapon = EarthWeapon = FireWeapon = 0;
		Earring = Earring2 = 1;
		magic_doll_effct_AttackObjId = 0;
		temp_hp = temp_mp = -1;
		macroPeriod = 0;
		worldDelete = true;
		worldJoin = false;
		own = null;
		database_key = null;
		sound = true;
		time = 0;
		attackPc = 0;
		pcAttackTarget = null;
		SpeedhackWarningCounting = 0;
		AttackFrameTime = 0;
		WalkFrameTime = 0;
		badPacketCount = 0;
		goodPacketCount = 0;
		gostHackCount = 0;
		halfDelayCount = 0;
		lastActionTime = 0L;
		lastMagicActionTime = 0L;
		lastMovingTime = 0L;
		speedCheck = 0L;
		lastDamageActionTime = 0L;
		isSetPoly = false;
		투망시간 = 0;
		dps_attack_time = 0L;
		isAutoAttack = false;
		autoAttackTarget = null;
		autoAttackTime = 0L;
		targetX = targetY = 0;
		addDropItemRate = addDropAdenRate = 0;
		isAutoPotion = false;
		isAutoPotion2 = false;
		autoPotionIdx = null;
		autoPotionName = null;
		autoPotionPercent = 0;
		inven_stat_object = null;
		robot_clan_name = null;
		temp_exp = 0;
		enchantRecovery = null;
		isDmgCheck = false;
		lostPoly = null;
		BuffExpPotion = false;
		BuffExpPotion3 = false;
		BuffExpPotion4 = false;
		BuffExpPotion2 = false;
		BuffMaanFire = BuffMaanEarth = BuffMaanWatar = BuffMaanWind = BuffMaanLife = BuffMaanBirth = BuffMaanShape = false;
		if (insideList != null) {
			synchronized (insideList) {
				insideList.clear();
			}
		}
		if (allList != null) {
			synchronized (allList) {
				allList.clear();
			}
		}
		if (ShopList != null) {
			synchronized (ShopList) {
				ShopList.clear();
			}
		}
		trueTargetTime = 0;
		
		macro_mag = false;
		// 적용된 버프가 있을수 있으므로.
		BuffController.toWorldOut(this, true);
	}

	public int getClan_shadow_step() {
		return clan_shadow_step;
	}

	public void setClan_shadow_step(int clan_shadow_step) {
		this.clan_shadow_step = clan_shadow_step;
	}

	public String getRobot_clan_name() {
		return robot_clan_name;
	}

	public void setRobot_clan_name(String robot_clan_name) {
		this.robot_clan_name = robot_clan_name;
	}

	public int getAdena_sise() {
		return adena_sise;
	}

	public void setAdena_sise(int adena_sise) {
		this.adena_sise = adena_sise;
	}

	public double getAddDropItemRate() {
		return addDropItemRate;
	}

	public void setAddDropItemRate(double addDropItemRate) {
		this.addDropItemRate = addDropItemRate;
	}

	public double getAddDropAdenRate() {
		return addDropAdenRate;
	}

	public void setAddDropAdenRate(double addDropAdenRate) {
		this.addDropAdenRate = addDropAdenRate;
	}
	
	public String getViewDelaySkillName() {
		return viewDelaySkillName;
	}

	public void setViewDelaySkillName(String viewDelaySkillName) {
		this.viewDelaySkillName = viewDelaySkillName;
	}

	public boolean isViewDelay() {
		return viewDelay;
	}

	public void setViewDelay(boolean viewDelay) {
		this.viewDelay = viewDelay;
	}
	
	public int getAdena_page() {
		return adena_page;
	}

	public void setAdena_page(int adena_page) {
		this.adena_page = adena_page;
	}

	public int getAdena_uid() {
		return adena_uid;
	}

	public void setAdena_uid(int adena_uid) {
		this.adena_uid = adena_uid;
	}

	public double getTemp_exp() {
		return temp_exp;
	}

	public void setTemp_exp(double temp_exp) {
		this.temp_exp = temp_exp;
	}

	public boolean isDamageMassage() {
		return damageMassage;
	}

	public void setDamageMassage(boolean damageMassage) {
		this.damageMassage = damageMassage;
	}
	
	public boolean isWarMessage() {
		return isWarMessage;
	}

	public void setWarMessage(boolean isWarMessage) {
		this.isWarMessage = isWarMessage;
	}
	
	public boolean isMark() {
		return isMark;
	}

	public void setMark(boolean isMark) {
		this.isMark = isMark;
	}
	
	public boolean isMapHack() {
		return isMapHack;
	}

	public void setMapHack(boolean isMapHack) {
		this.isMapHack = isMapHack;
	}
	
	public boolean isCriticalMagicEffect() {
		return criticalMagicEffect;
	}

	public void setCriticalMagicEffect(boolean criticalMagicEffect) {
		this.criticalMagicEffect = criticalMagicEffect;
	}

	public boolean isCriticalEffect() {
		return criticalEffect;
	}

	public void setCriticalEffect(boolean criticalEffect) {
		this.criticalEffect = criticalEffect;
	}
	
	public void removeShopList(String msg) {
			if (!ShopList.contains(msg))
				ShopList.remove(msg);
		}

	public void appendShopList(String msg) {
			ShopList.add(msg);
		}

	public void clearShopList() {
			ShopList.clear();
		}

	public List<String> getShopList() {
			return new ArrayList<String>(ShopList);
		}

	public int getShop_inven_num() {
		return Shop_inven_num;
	}

	public void setShop_inven_num(int shop_inven_num) {
		Shop_inven_num = shop_inven_num;
	}

	public long getShop_count() {
		return shop_count;
	}

	public void setShop_count(long shop_count) {
		this.shop_count = shop_count;
	}

	public object getInven_stat_object() {
		return inven_stat_object;
	}

	public void setInven_stat_object(object inven_stat_object) {
		this.inven_stat_object = inven_stat_object;
	}

	public int getDisDelay() {
		return _디스딜레이;
	}

	public void setDisDelay(int i) {
		_디스딜레이 = i;
	}

	public void addDisDelay(int i) {
		_디스딜레이 += i;
	}

	public boolean isNameHidden() {
		return nameHidden;
	}

	public void setNameHidden(boolean nameHidden) {
		this.nameHidden = nameHidden;
	}

	public Object getDatabaseKey() {
		return database_key;
	}

	public void setDatabaseKey(Object database_key) {
		this.database_key = database_key;
	}

	public Summon getSummon() {
		return summon;
	}

	public void setSummon(Summon summon) {
		this.summon = summon;
	}
	
	public boolean isBuffMaanFire() {
		return BuffMaanFire;
	}

	public void setBuffMaanFire(boolean buffMaanFire) {
		BuffMaanFire = buffMaanFire;
	}

	public boolean isBuffMaanEarth() {
		return BuffMaanEarth;
	}

	public void setBuffMaanEarth(boolean buffMaanEarth) {
		BuffMaanEarth = buffMaanEarth;
	}

	public boolean isBuffMaanWatar() {
		return BuffMaanWatar;
	}

	public void setBuffMaanWatar(boolean buffMaanWatar) {
		BuffMaanWatar = buffMaanWatar;
	}

	public boolean isBuffMaanWind() {
		return BuffMaanWind;
	}

	public void setBuffMaanWind(boolean buffMaanWind) {
		BuffMaanWind = buffMaanWind;
	}

	public boolean isBuffMaanLife() {
		return BuffMaanLife;
	}

	public void setBuffMaanLife(boolean buffMaanLife) {
		BuffMaanLife = buffMaanLife;
	}
	
	public boolean isBuffMaanBirth() {
		return BuffMaanBirth;
	}

	public void setBuffMaanBirth(boolean buffMaanBirth) {
		BuffMaanBirth = buffMaanBirth;
	}

	public boolean isBuffMaanShape() {
		return BuffMaanShape;
	}

	public void setBuffMaanShape(boolean buffMaanShape) {
		BuffMaanShape = buffMaanShape;
	}
	
	public void setAiStatus(int ai_status) {
			this.ai_status = ai_status;
			// ai 상태 변경될때마다 멘트표현 변수 초기화.
			ai_showment = false;
			// 멘트 표현위치 초기화.
			ai_showment_idx = 0;
			// 멘트 표현시간 초기화.
			ai_showment_time = 0;
		}

	public int getAiStatus() {
			return ai_status;
		}

	public long getAiTime() {
		return ai_time;
	}

	public void setAiTime(long ai_time) {
		this.ai_time = ai_time;
	}

	public boolean isBuffCurseBlind() {
		return BuffCurseBlind;
	}

	public void setBuffCurseBlind(boolean buffCurseBlind) {
		BuffCurseBlind = buffCurseBlind;
	}

	public boolean isBuffBounceAttack() {
		return BuffBounceAttack;
	}

	public void setBuffBounceAttack(boolean buffBounceAttack) {
		BuffBounceAttack = buffBounceAttack;
	}

	public boolean isMagicdollCracker() {
		return MagicdollCracker;
	}

	public void setMagicdollCracker(boolean magicdollCracker) {
		MagicdollCracker = magicdollCracker;
	}

	public boolean isMagicdollHatzling() {
		return MagicdollHatzling;
	}

	public void setMagicdollHatzling(boolean magicdollHatzling) {
		MagicdollHatzling = magicdollHatzling;
	}

	public boolean isMagicdollYeti() {
		return MagicdollYeti;
	}

	public void setMagicdollYeti(boolean magicdollYeti) {
		MagicdollYeti = magicdollYeti;
	}

	public boolean isMagicdollTree() {
		return MagicdollTree;
	}

	public void setMagicdollTree(boolean magicdollTree) {
		MagicdollTree = magicdollTree;
	}
	
	public boolean isMagicdollSpartoi() {
		return MagicdollSpartoi;
	}

	public void setMagicdollSpartoi(boolean magicdollSpartoi) {
		MagicdollSpartoi = magicdollSpartoi;
	}

	public boolean isMagicdollCockatrice() {
		return MagicdollCockatrice;
	}

	public void setMagicdollCockatrice(boolean magicdollCockatrice) {
		MagicdollCockatrice = magicdollCockatrice;
	}

	public boolean isMagicdollElder() {
		return MagicdollElder;
	}

	public void setMagicdollElder(boolean magicdollElder) {
		MagicdollElder = magicdollElder;
	}

	public boolean isMagicdollSeaDancer() {
		return MagicdollSeaDancer;
	}

	public void setMagicdollSeaDancer(boolean magicdollSeaDancer) {
		MagicdollSeaDancer = magicdollSeaDancer;
	}

	public boolean isMagicdollSuccubus() {
		return MagicdollSuccubus;
	}

	public void setMagicdollSuccubus(boolean magicdollSuccubus) {
		MagicdollSuccubus = magicdollSuccubus;
	}

	public boolean isMagicdollBugbear() {
		return MagicdollBugbear;
	}

	public void setMagicdollBugbear(boolean magicdollBugbear) {
		MagicdollBugbear = magicdollBugbear;
	}

	public boolean isMagicdollRamia() {
		return MagicdollRamia;
	}

	public void setMagicdollRamia(boolean magicdollRamia) {
		MagicdollRamia = magicdollRamia;
	}

	public boolean isMagicdollCrustacean() {
		return MagicdollCrustacean;
	}

	public void setMagicdollCrustacean(boolean magicdollCrustacean) {
		MagicdollCrustacean = magicdollCrustacean;
	}

	public int getBuffMeditaitonLevel() {
		return buffMeditaitonLevel;
	}

	public void setBuffMeditaitonLevel(int buffMeditaitonLevel) {
		this.buffMeditaitonLevel = buffMeditaitonLevel;
	}
	
	public boolean isMagicdollStoneGolem() {
		return MagicdollStoneGolem;
	}

	public void setMagicdollStoneGolem(boolean magicdollStoneGolem) {
		MagicdollStoneGolem = magicdollStoneGolem;
	}

	public boolean isMagicdollWerewolf() {
		return MagicdollWerewolf;
	}

	public void setMagicdollWerewolf(boolean magicdollWerewolf) {
		MagicdollWerewolf = magicdollWerewolf;
	}

	public boolean isMagicdollMermaid() {
		return MagicdollMermaid;
	}

	public void setMagicdollMermaid(boolean magicdollMermaid) {
		MagicdollMermaid = magicdollMermaid;
	}

	public boolean isMagicdollLavaGolem() {
		return MagicdollLavaGolem;
	}

	public void setMagicdollLavaGolem(boolean magicdollLavaGolem) {
		MagicdollLavaGolem = magicdollLavaGolem;
	}
	
	public boolean isMagicdollGiant() {
		return MagicdollGiant;
	}

	public void setMagicdollGiant(boolean magicdollGiant) {
		MagicdollGiant = magicdollGiant;
	}

	public boolean isMagicdollBlackElder() {
		return MagicdollBlackElder;
	}

	public void setMagicdollBlackElder(boolean magicdollBlackElder) {
		MagicdollBlackElder = magicdollBlackElder;
	}
	
	public boolean isMagicdollsuccubusQueen() {
		return MagicdollsuccubusQueen;
	}

	public void setMagicdollsuccubusQueen(boolean magicdollsuccubusQueen) {
		MagicdollsuccubusQueen = magicdollsuccubusQueen;
	}
	
	public boolean isMagicdollDeathKnight() {
		return MagicdollDeathKnight;
	}

	public void setMagicdollDeathKnight(boolean magicdollDeathKnight) {
		MagicdollDeathKnight = magicdollDeathKnight;
	}

	public boolean isMagicdollSnowmanB() {
		return MagicdollSnowmanB;
	}

	public void setMagicdollSnowmanB(boolean magicdollSnowmanB) {
		MagicdollSnowmanB = magicdollSnowmanB;
	}

	public boolean isMagicdollCyclops() {
		return MagicdollCyclops;
	}

	public void setMagicdollCyclops(boolean magicdollCyclops) {
		MagicdollCyclops = magicdollCyclops;
	}
	
	public boolean isMagicdollIris() {
		return MagicdollIris;
	}

	public void setMagicdollIris(boolean magicdollIris) {
		MagicdollIris = magicdollIris;
	}
	
	public boolean isMagicdollBampaear() {
		return MagicdollBampaear;
	}

	public void setMagicdollBampaear(boolean magicdollBampaear) {
		MagicdollBampaear = magicdollBampaear;
	}
	
	public boolean isMagicdollMummylord() {
		return MagicdollMummylord;
	}

	public void setMagicdollMummylord(boolean magicdollMummylord) {
		MagicdollMummylord = magicdollMummylord;
	}
	
	public boolean isMagicdollBarancar() {
		return MagicdollBarancar;
	}

	public void setMagicdollBarancar(boolean magicdollBarancar) {
		MagicdollBarancar = magicdollBarancar;
	}
	
	public boolean isMagicdollTarak() {
		return MagicdollTarak;
	}

	public void setMagicdollTarak(boolean magicdollTarak) {
		MagicdollTarak = magicdollTarak;
	}
	
	public boolean isMagicdollBaphomet() {
		return MagicdollBaphomet;
	}

	public void setMagicdollBaphomet(boolean magicdollBaphomet) {
		MagicdollBaphomet = magicdollBaphomet;
	}
	
	public boolean isMagicdollroyal() {
		return Magicdollroyal;
	}

	public void setMagicdollroyal(boolean magicdollroyal) {
		Magicdollroyal = magicdollroyal;
	}
	
	public boolean isMagicdollknight() {
		return Magicdollknight;
	}

	public void setMagicdollknight(boolean magicdollknight) {
		Magicdollknight = magicdollknight;
	}
	
	public boolean isMagicdollelf() {
		return Magicdollelf;
	}

	public void setMagicdollelf(boolean magicdollelf) {
		Magicdollelf = magicdollelf;
	}
	
	public boolean isMagicdollwizard() {
		return Magicdollwizard;
	}

	public void setMagicdollwizard(boolean magicdollwizard) {
		Magicdollwizard = magicdollwizard;
	}
	
	public boolean isMagicdollroyal2() {
		return Magicdollroyal2;
	}

	public void setMagicdollroyal2(boolean magicdollroyal2) {
		Magicdollroyal2 = magicdollroyal2;
	}
	
	public boolean isMagicdollknight2() {
		return Magicdollknight2;
	}

	public void setMagicdollknight2(boolean magicdollknight2) {
		Magicdollknight2 = magicdollknight2;
	}
	
	public boolean isMagicdollelf2() {
		return Magicdollelf2;
	}

	public void setMagicdollelf2(boolean magicdollelf2) {
		Magicdollelf2 = magicdollelf2;
	}
	
	public boolean isMagicdollwizard2() {
		return Magicdollwizard2;
	}

	public void setMagicdollwizard2(boolean magicdollwizard2) {
		Magicdollwizard2 = magicdollwizard2;
	}
	
	public boolean isMagicdollIceQueen() {
		return MagicdollIceQueen;
	}

	public void setMagicdollIceQueen(boolean magicdollIceQueen) {
		MagicdollIceQueen = magicdollIceQueen;
	}
	
	public boolean isMagicdollCuze() {
		return MagicdollCuze;
	}

	public void setMagicdollCuze(boolean magicdollCuze) {
		MagicdollCuze = magicdollCuze;
	}
	
	public boolean isMagicdollAntaras() {
		return MagicdollAntaras;
	}

	public void setMagicdollAntaras(boolean magicdollAntaras) {
		MagicdollAntaras = magicdollAntaras;
	}
	
	public boolean isMagicdollPapoorion() {
		return MagicdollPapoorion;
	}

	public void setMagicdollPapoorion(boolean magicdollPapoorion) {
		MagicdollPapoorion = magicdollPapoorion;
	}
	
	public boolean isMagicdollLindvior() {
		return MagicdollLindvior;
	}

	public void setMagicdollLindvior(boolean magicdollLindvior) {
		MagicdollLindvior = magicdollLindvior;
	}
	
	public boolean isMagicdollValakas() {
		return MagicdollValakas;
	}

	public void setMagicdollValakas(boolean magicdollValakas) {
		MagicdollValakas = magicdollValakas;
	}
	
	public boolean isMagicdollGirtas() {
		return MagicdollGirtas;
	}

	public void setMagicdollGirtas(boolean magicdollGirtas) {
		MagicdollGirtas = magicdollGirtas;
	}

	public boolean isMagicdollLich() {
		return MagicdollLich;
	}

	public void setMagicdollLich(boolean magicdollLich) {
		MagicdollLich = magicdollLich;
	}

	public boolean isMagicdollDrake() {
		return MagicdollDrake;
	}

	public void setMagicdollDrake(boolean magicdollDrake) {
		MagicdollDrake = magicdollDrake;
	}

	public boolean isMagicdollKingBugbear() {
		return MagicdollKingBugbear;
	}

	public void setMagicdollKingBugbear(boolean magicdollKingBugbear) {
		MagicdollKingBugbear = magicdollKingBugbear;
	}

	public boolean isMagicdollDimondGolem() {
		return MagicdollDimondGolem;
	}

	public void setMagicdollDimondGolem(boolean magicdollDimondGolem) {
		MagicdollDimondGolem = magicdollDimondGolem;
	}
	
	public boolean isMagicdollGramlin() {
		return MagicdollGramlin;
	}

	public void setMagicdollGramlin(boolean magicdollGramlin) {
		MagicdollGramlin = magicdollGramlin;
	}
	
	public boolean isMagicdollSeer() {
		return MagicdollSeer;
	}

	public void setMagicdollSeer(boolean magicdollSeer) {
		MagicdollSeer = magicdollSeer;
	}

	public boolean isMagicdollKnightVald() {
		return MagicdollKnightVald;
	}

	public void setMagicdollKnightVald(boolean magicdollKnightVald) {
		MagicdollKnightVald = magicdollKnightVald;
	}

	public boolean isMagicdollDemon() {
		return MagicdollDemon;
	}

	public void setMagicdollDemon(boolean magicdollDemon) {
		MagicdollDemon = magicdollDemon;
	}

	public int getBuffGiganticHp() {
		return BuffGiganticHp;
	}

	public void setBuffGiganticHp(int buffGiganticHp) {
		BuffGiganticHp = buffGiganticHp;
	}

	public int getBuffAdvanceSpiritHp() {
		return BuffAdvanceSpiritHp;
	}

	public void setBuffAdvanceSpiritHp(int buffAdvanceSpiritHp) {
		BuffAdvanceSpiritHp = buffAdvanceSpiritHp;
	}

	public int getBuffAdvanceSpiritMp() {
		return BuffAdvanceSpiritMp;
	}

	public void setBuffAdvanceSpiritMp(int buffAdvanceSpiritMp) {
		BuffAdvanceSpiritMp = buffAdvanceSpiritMp;
	}

	public boolean isBuffExoticVitalize() {
		return BuffExoticVitalize;
	}

	public void setBuffExoticVitalize(boolean buffExoticVitalize) {
		BuffExoticVitalize = buffExoticVitalize;
	}

	public boolean isBuffWaterLife() {
		return BuffWaterLife;
	}

	public void setBuffWaterLife(boolean buffWaterLife) {
		BuffWaterLife = buffWaterLife;
	}

	public boolean isBuffElementalFire() {
		return BuffElementalFire;
	}

	public void setBuffElementalFire(boolean buffElementalFire) {
		BuffElementalFire = buffElementalFire;
	}

	public boolean isBuffPolluteWater() {
		return BuffPolluteWater;
	}

	public void setBuffPolluteWater(boolean buffPolluteWater) {
		BuffPolluteWater = buffPolluteWater;
	}

	public boolean isBuffStrikerGale() {
		return BuffStrikerGale;
	}

	public void setBuffStrikerGale(boolean buffStrikerGale) {
		BuffStrikerGale = buffStrikerGale;
	}

	public boolean isBuffSoulOfFlame() {
		return BuffSoulOfFlame;
	}

	public void setBuffSoulOfFlame(boolean buffSoulOfFlame) {
		BuffSoulOfFlame = buffSoulOfFlame;
	}

	public boolean isBuffAdditionalFire() {
		return BuffAdditionalFire;
	}

	public void setBuffAdditionalFire(boolean buffAdditionalFire) {
		BuffAdditionalFire = buffAdditionalFire;
	}

	public boolean isBuffCriminal() {
		return BuffCriminal;
	}

	public void setBuffCriminal(boolean buffCriminal) {
		BuffCriminal = buffCriminal;
	}

	public boolean isBuffMeditation() {
		return BuffMeditation;
	}

	public void setBuffMeditation(boolean buffMeditation) {
		BuffMeditation = buffMeditation;
	}

	public boolean isBuffFogOfSleeping() {
		return BuffFogOfSleeping;
	}

	public void setBuffFogOfSleeping(boolean buffFogOfSleeping) {
		BuffFogOfSleeping = buffFogOfSleeping;
	}

	public boolean isBuffCounterMagic() {
		return BuffCounterMagic;
	}

	public void setBuffCounterMagic(boolean buffCounterMagic) {
		BuffCounterMagic = buffCounterMagic;
	}

	public boolean isBuffWeakness() {
		return BuffWeakness;
	}

	public void setBuffWeakness(boolean buffWeakness) {
		BuffWeakness = buffWeakness;
	}

	public boolean isBuffEyeOfStorm() {
		return BuffEyeOfStorm;
	}

	public void setBuffEyeOfStorm(boolean buffEyeOfStorm) {
		BuffEyeOfStorm = buffEyeOfStorm;
	}

	public boolean isBuffSilence() {
		return BuffSilence;
	}

	public void setBuffSilence(boolean buffSilence) {
		BuffSilence = buffSilence;
	}

	public boolean isBuffDisease() {
		return BuffDisease;
	}

	public void setBuffDisease(boolean buffDisease) {
		BuffDisease = buffDisease;
	}

	public boolean isBuffChattingClose() {
		return BuffChattingClose;
	}

	public void setBuffChattingClose(boolean buffChattingClose) {
		BuffChattingClose = buffChattingClose;
	}

	public boolean isBuffChattingClosetwo() {
		return BuffChattingClosetwo;
	}

	public void setBuffChattingClosetwo(boolean buffChattingClosetwo) {
		BuffChattingClosetwo = buffChattingClosetwo;
	}

	public boolean isBuffCurseFloatingEye() {
		return BuffCurseFloatingEye;
	}

	public void setBuffCurseFloatingEye(boolean buffCurseFloatingEye) {
		BuffCurseFloatingEye = buffCurseFloatingEye;
	}

	public boolean isBuffCurseGhoul() {
		return BuffCurseGhoul;
	}

	public void setBuffCurseGhoul(boolean buffCurseGhoul) {
		BuffCurseGhoul = buffCurseGhoul;
	}

	public boolean isBuffBlessOfFire() {
		return BuffBlessOfFire;
	}

	public void setBuffBlessOfFire(boolean buffBlessOfFire) {
		BuffBlessOfFire = buffBlessOfFire;
	}

	public boolean isBuffEva() {
		return BuffEva;
	}

	public void setBuffEva(boolean buffEva) {
		BuffEva = buffEva;
	}

	public boolean isBuffWisdom() {
		return BuffWisdom;
	}

	public void setBuffWisdom(boolean buffWisdom) {
		BuffWisdom = buffWisdom;
	}

	public boolean isBuffStormShot() {
		return BuffStormShot;
	}

	public void setBuffStormShot(boolean buffStormShot) {
		BuffStormShot = buffStormShot;
	}

	public boolean isBuffBurningWeapon() {
		return BuffBurningWeapon;
	}

	public void setBuffBurningWeapon(boolean buffBurningWeapon) {
		BuffBurningWeapon = buffBurningWeapon;
	}

	public boolean isBuffEraseMagic() {
		return BuffEraseMagic;
	}

	public void setBuffEraseMagic(boolean buffEraseMagic) {
		BuffEraseMagic = buffEraseMagic;
	}

	public boolean isBuffWindShot() {
		return BuffWindShot;
	}

	public void setBuffWindShot(boolean buffWindShot) {
		BuffWindShot = buffWindShot;
	}

	public boolean isBuffFireWeapon() {
		return BuffFireWeapon;
	}

	public void setBuffFireWeapon(boolean buffFireWeapon) {
		BuffFireWeapon = buffFireWeapon;
	}

	public boolean isBuffGlowingAura() {
		return BuffGlowingAura;
	}

	public void setBuffGlowingAura(boolean buffGlowingAura) {
		BuffGlowingAura = buffGlowingAura;
	}

	public boolean isBuffAbsoluteBarrier() {
		return BuffAbsoluteBarrier;
	}

	public void setBuffAbsoluteBarrier(boolean buffAbsoluteBarrier) {
		BuffAbsoluteBarrier = buffAbsoluteBarrier;
	}

	public boolean isBuffDecayPotion() {
		return BuffDecayPotion;
	}

	public void setBuffDecayPotion(boolean buffDecayPotion) {
		BuffDecayPotion = buffDecayPotion;
	}

	public boolean isBuffImmuneToHarm() {
		return BuffImmuneToHarm;
	}

	public void setBuffImmuneToHarm(boolean buffImmuneToHarm) {
		BuffImmuneToHarm = buffImmuneToHarm;
	}

	public boolean isBuffInvisiBility() {
		return BuffInvisiBility;
	}

	public void setBuffInvisiBility(boolean buffInvisiBility) {
		BuffInvisiBility = buffInvisiBility;
	}

	public boolean isBuffBlessWeapon() {
		return BuffBlessWeapon;
	}

	public void setBuffBlessWeapon(boolean buffBlessWeapon) {
		BuffBlessWeapon = buffBlessWeapon;
	}

	public boolean isBuffCurseParalyze() {
		return BuffCurseParalyze;
	}

	public void setBuffCurseParalyze(boolean buffCurseParalyze) {
		BuffCurseParalyze = buffCurseParalyze;
	}

	public boolean isBuffSolidCarriage() {
		return BuffSolidCarriage;
	}

	public void setBuffSolidCarriage(boolean buffSolidCarriage) {
		BuffSolidCarriage = buffSolidCarriage;
	}

	public boolean isBuffDragonSkin() {
		return BuffDragonSkin;
	}

	public void setBuffDragonSkin(boolean buffDragonSkin) {
		BuffDragonSkin = buffDragonSkin;
	}

	public boolean isBuffBurningSlash() {
		return BuffBurningSlash;
	}

	public void setBuffBurningSlash(boolean buffBurningSlash) {
		BuffBurningSlash = buffBurningSlash;
	}

	public boolean isBuffDesperado() {
		return BuffDesperado;
	}

	public void setBuffDesperado(boolean buffDesperado) {
		BuffDesperado = buffDesperado;
	}

	public boolean isBuffDetection() {
		return BuffDetection;
	}

	public void setBuffDetection(boolean buffDetection) {
		BuffDetection = buffDetection;
	}

	public boolean isBuffTeleport() {
		return BuffTeleport;
	}

	public void setBuffTeleport(boolean buffTeleport) {
		BuffTeleport = buffTeleport;
	}

	public boolean isBuffAquaProtect() {
		return BuffAquaProtect;
	}

	public void setBuffAquaProtect(boolean buffAquaProtect) {
		BuffAquaProtect = buffAquaProtect;
	}
	
	public int getCurseParalyzeCounter() {
		return CurseParalyzeCounter;
	}

	public void setCurseParalyzeCounter(int curseParalyzeCounter) {
		CurseParalyzeCounter = curseParalyzeCounter;
	}

	public boolean isBuffDecreaseWeight() {
		return BuffDecreaseWeight;
	}

	public void setBuffDecreaseWeight(boolean BuffDecreaseWeight) {
		this.BuffDecreaseWeight = BuffDecreaseWeight;
	}

	public boolean isBuffHolyWeapon() {
		return BuffHolyWeapon;
	}

	public void setBuffHolyWeapon(boolean buffHolyWeapon) {
		BuffHolyWeapon = buffHolyWeapon;
	}

	public void setWorldDelete(boolean worldDelete) {
		this.worldDelete = worldDelete;
	}

	public boolean isWorldJoin() {
		return worldJoin;
	}
	
	public boolean isBuffEnchantWeapon() {
		return BuffEnchantWeapon;
	}

	public void setBuffEnchantWeapon(boolean buffEnchantWeapon) {
		BuffEnchantWeapon = buffEnchantWeapon;
	}

	public boolean isBuffMonsterEyeMeat() {
		return BuffMonsterEyeMeat;
	}

	public void setBuffMonsterEyeMeat(boolean buffMonsterEyeMeat) {
		BuffMonsterEyeMeat = buffMonsterEyeMeat;
	}

	public boolean isBuffEnchantVenom() {
		return BuffEnchantVenom;
	}

	public void setBuffEnchantVenom(boolean buffEnchantVenom) {
		BuffEnchantVenom = buffEnchantVenom;
	}

	public boolean isBuffBurningSpirit() {
		return BuffBurningSpirit;
	}

	public void setBuffBurningSpirit(boolean buffBurningSpirit) {
		BuffBurningSpirit = buffBurningSpirit;
	}

	public boolean isBuffVenomResist() {
		return BuffVenomResist;
	}

	public void setBuffVenomResist(boolean buffVenomResist) {
		BuffVenomResist = buffVenomResist;
	}

	public boolean isBuffDoubleBreak() {
		return BuffDoubleBreak;
	}

	public void setBuffDoubleBreak(boolean buffDoubleBreak) {
		BuffDoubleBreak = buffDoubleBreak;
	}

	public boolean isBuffShadowFang() {
		return BuffShadowFang;
	}

	public void setBuffShadowFang(boolean buffShadowFang) {
		BuffShadowFang = buffShadowFang;
	}

	public boolean isBuffBerserks() {
		return BuffBerserks;
	}

	public void setBuffBerserks(boolean buffBerserks) {
		BuffBerserks = buffBerserks;
	}

	public boolean isBuffReductionArmor() {
		return BuffReductionArmor;
	}

	public void setBuffReductionArmor(boolean buffReductionArmor) {
		BuffReductionArmor = buffReductionArmor;
	}

	public boolean isBuffCounterBarrier() {
		return BuffCounterBarrier;
	}

	public void setBuffCounterBarrier(boolean buffCounterBarrier) {
		BuffCounterBarrier = buffCounterBarrier;
	}

	public boolean isBuffCounterMirror() {
		return BuffCounterMirror;
	}

	public void setBuffCounterMirror(boolean buffCounterMirror) {
		BuffCounterMirror = buffCounterMirror;
	}

	public boolean isWorldDelete() {
		return worldDelete;
	}

	public boolean isClanOneLevel() {
		return Level_1_Clan;
	}

	public void setClanOneLevel(boolean nameHidden) {
		this.Level_1_Clan = nameHidden;
	}

	public boolean isClanTwoLevel() {
		return Level_2_Clan;
	}

	public void setClanTwoLevel(boolean nameHidden) {
		this.Level_2_Clan = nameHidden;
	}

	public boolean isClanThreeLevel() {
		return Level_3_Clan;
	}

	public void setClanThreeLevel(boolean nameHidden) {
		this.Level_3_Clan = nameHidden;
	}

	public boolean isClanFourLevel() {
		return Level_4_Clan;
	}

	public void setClanFourLevel(boolean nameHidden) {
		this.Level_4_Clan = nameHidden;
	}

	public boolean isClanFiveLevel() {
		return Level_5_Clan;
	}

	public void setClanFiveLevel(boolean nameHidden) {
		this.Level_5_Clan = nameHidden;
	}

	public boolean isClanSixLevel() {
		return Level_6_Clan;
	}

	public void setClanSixLevel(boolean nameHidden) {
		this.Level_6_Clan = nameHidden;
	}

	public boolean isClanSevenLevel() {
		return Level_7_Clan;
	}

	public void setClanSevenLevel(boolean nameHidden) {
		this.Level_7_Clan = nameHidden;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getMap() {
		return map;
	}

	public void setMap(int map) {
		this.map = map;
	}

	public int getHomeX() {
		return homeX;
	}

	public void setHomeX(int homeX) {
		this.homeX = homeX;
	}

	public int getHomeY() {
		return homeY;
	}

	public void setHomeY(int homeY) {
		this.homeY = homeY;
	}

	public int getHomeMap() {
		return homeMap;
	}

	public void setHomeMap(int homeMap) {
		this.homeMap = homeMap;
	}

	public int getHomeLoc() {
		return homeLoc;
	}

	public void setHomeLoc(int homeLoc) {
		this.homeLoc = homeLoc;
	}

	public int getHomeHeading() {
		return homeHeading;
	}

	public void setHomeHeading(int homeHeading) {
		this.homeHeading = homeHeading;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public void setClassSex(int classSex) {
		this.classSex = classSex;
	}

	public int getClassSex() {
		return classSex;
	}

	public void setClassType(int classType) {
		this.classType = classType;
	}

	public int getClassType() {
		return classType;
	}

	public void setGfx(int gfx) {
		this.gfx = gfx;
	}

	public int getGfx() {
		return gfx;
	}

	public int getGfxMode() {
		return dead ? 8 : gfxMode;
	}

	public void setGfxMode(int gfxMode) {
		this.gfxMode = gfxMode;
	}

	public int getClassGfx() {
		return classGfx;
	}

	/**
	 * gfxmode 복구 메서드.<br/>
	 * : 부활할때랑 죽은후에 호출됨.
	 */
	public void setGfxModeClear() {
		ItemInstance weapon = getInventory() != null ? getInventory().getSlot(8) : null;
		if (weapon != null) {
			ItemInstance shield = getInventory().getSlot(7);
			if (shield != null && shield instanceof ItemWeaponInstance) {
				if (shield.getItem().getGfxMode() == 0x0B)
					setGfxMode(88);
				else
					setGfxMode(getClassGfxMode() + weapon.getItem().getGfxMode());
			} else
				setGfxMode(getClassGfxMode() + weapon.getItem().getGfxMode());
		} else
			setGfxMode(getClassGfxMode());
	}

	public void setClassGfx(int classGfx) {
		this.classGfx = classGfx;
	}

	public int getClassGfxMode() {
		return classGfxMode;
	}

	public void setClassGfxMode(int classGfxMode) {
		this.classGfxMode = classGfxMode;
	}
	
	public int getLawful() {
		return lawful;
	}

	public void setLawful(int lawful) {
		if (lawful >= 0) {
			if (lawful > Lineage.LAWFUL) {
				lawful = Lineage.LAWFUL;
			} else if (lawful < Lineage.CHAOTIC) {
				lawful = Lineage.CHAOTIC;
			}
			this.lawful = lawful;
		}
	}

	public boolean isDead() {
		return dead;
	}

	/**
	 * 객체가 죽은 상태인지 설정하는 메서드.
	 * 
	 * @param dead
	 */
	public void setDead(boolean dead) {
		synchronized (sync_dynamic) {
			if (!this.dead && dead && !worldDelete) {
				toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), this, 8),
						this instanceof PcInstance);
				// 동적값 갱신. 죽은 객체는 해당좌표에 객체가 없는것으로 판단해야함.
				if (isDynamicUpdate())
					World.update_mapDynamic(x, y, map, false);
			}
			this.dead = dead;
		}
	}

	public int getHeading() {
		return heading;
	}

	public void setHeading(int heading) {
		if (heading < 0 || heading > 7)
			heading = 0;
		this.heading = heading;
	}

	public int getLight() {
		return light;
	}

	public void setLight(int light) {
		this.light = light;
	}

	public int getSpeed() {
		synchronized (sync_dynamic) {
			return speed;
		}
	}

	public void setSpeed(int speed) {
		synchronized (sync_dynamic) {
			this.speed = speed;
		}
	}

	public boolean isBrave() {
		synchronized (sync_dynamic) {
			return brave;
		}
	}

	public void setBrave(boolean brave) {
		synchronized (sync_dynamic) {
			this.brave = brave;
		}
	}

	public String getAutoPotionName() {
		return autoPotionName;
	}

	public void setAutoPotionName(String autoPotionName) {
		this.autoPotionName = autoPotionName;
	}

	public int getAutoPotionPercent() {
		return autoPotionPercent;
	}

	public void setAutoPotionPercent(int autoPotionPercent) {
		this.autoPotionPercent = autoPotionPercent;
	}

	public long getCount() {
		synchronized (sync_dynamic) {
			return count;
		}
	}

	public void setCount(long count) {
		synchronized (sync_dynamic) {
			this.count = count;
		}
	}

	public int getTempCount() {
		return tempCount;
	}

	public void setTempCount(int tempCount) {
		this.tempCount = tempCount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	

	public int getClanId() {
		return clanId;
	}

	public void setClanId(int clanId) {
		this.clanId = clanId;
	}

	public String getClanName() {
		return clanName;
	}

	public void setClanName(String clanName) {
		this.clanName = clanName;
	}

	public int getClanRank() {
		return clanRank;
	}

	public void setClanRank(int clanRank) {
		this.clanRank = clanRank;
	}

	public long getOwnObjectId() {
		return own_objectId;
	}

	public void setOwnObjectId(long own_objectId) {
		this.own_objectId = own_objectId;
	}

	public String getOwnName() {
		return own_name;
	}

	public void setOwnName(String own_name) {
		this.own_name = own_name;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public boolean isFight() {
		return fight;
	}

	public void setFight(boolean fight) {
		this.fight = fight;
	}

	public boolean isInvis() {
		synchronized (sync_dynamic) {
			return invis;
		}
	}

	public void setInvis(boolean invis) {
		synchronized (sync_dynamic) {
			this.invis = invis;
		}
	}

	public boolean isTransparent() {
		return transparent;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	public boolean isPoison() {
		synchronized (sync_dynamic) {
			return poison || BuffCurseGhoul;
		}
	}

	public void setPoison(boolean poison) {
		synchronized (sync_dynamic) {
			this.poison = poison;
		}
	}

	public void removeAllList(object o) {
		synchronized (allList) {
			allList.remove(o);
		}
	}

	public void removeInsideList(object o) {
		synchronized (insideList) {
			insideList.remove(o);
		}
	}

	public void appendAllList(object o) {
		synchronized (allList) {
			if (!allList.contains(o))
				allList.add(o);
		}
	}

	public void appendInsideList(object o) {
		synchronized (insideList) {
			if (!insideList.contains(o))
				insideList.add(o);
		}
	}

	public boolean isContainsAllList(object o) {
		synchronized (allList) {
			return allList.contains(o);
		}
	}

	public void clearAllList() {
		synchronized (allList) {
			allList.clear();
		}
	}

	public boolean isContainsInsideList(object o) {
		synchronized (insideList) {
			return insideList.contains(o);
		}
	}

	public object findInsideList(long object_id) {
		for (object o : getInsideList(true)) {
			if (!FightController.isFightMonster(o) && o.getObjectId() == object_id)
				return o;
		}
		return null;
	}

	public void findInsideList(int x, int y, List<object> r_list) {
		for (object o : getInsideList(true)) {
			if (o.getX() == x && o.getY() == y)
				r_list.add(o);
		}
	}

	public List<object> getInsideList(boolean isNew) {
		synchronized (insideList) {
			return isNew ? new ArrayList<object>(insideList) : insideList;
		}
	}

	public List<object> getInsideList() {
		synchronized (insideList) {
			return new ArrayList<object>(insideList);
		}
	}

	public int getClanGrade() {
		return clanGrade;
	}

	public void setClanGrade(int clanGrade) {
		this.clanGrade = clanGrade;
	}

	public List<object> getAllList(boolean isNew) {
		synchronized (allList) {
			return isNew ? new ArrayList<object>(allList) : allList;
		}
	}

	public void setTempHp(int hp) {
		temp_hp = hp;
	}

	public void setTempMp(int mp) {
		temp_mp = mp;
	}

	public void setLockLow(boolean lock) {
		synchronized (sync_dynamic) {
			lock_low = lock;
		}
	}

	public void setLockHigh(boolean lock) {
		synchronized (sync_dynamic) {
			lock_high = lock;
		}
	}

	public boolean isLockLow() {
		return lock_low;
	}

	public boolean isLockHigh() {
		return lock_high;
	}

	public boolean isLock() {
		return lock_low || lock_high;
	}
	
	public Poly getLostPoly() {
		return lostPoly;
	}

	public void setLostPoly(Poly lostPoly) {
		this.lostPoly = lostPoly;
	}

	public void setNowHp(int nowhp) {
	}

	public void setMaxHp(int maxhp) {
	}

	public int getMaxHp() {
		return 0;
	}

	public void setNowMp(int nowmp) {
	}

	public void setMaxMp(int maxmp) {
	}

	public int getMaxMp() {
		return 0;
	}

	public int getLevel() {
		return 0;
	}

	public int getNowHp() {
		return 0;
	}

	public int getNowMp() {
		return 0;
	}

	public int getGm() {
		return 0;
	}

	public void setGm(int gm) {
	}

	public void setInventory(Inventory inv) {
	}

	public Inventory getInventory() {
		return null;
	}

	public long getPartyId() {
		return 0;
	}

	public void setPartyId(long partyId) {
	}

	public void setReSpawnTime(int reSpawnTime) {
	}

	public boolean isMonsterHpBar() {
		return true;
	}

	/**
	 * 객체 그리는 패킷에서 호출해서 사용. SObjectAdd
	 * 
	 * @param o
	 */
	public int getStatus(object o) {
		Object temp = PluginController.init(object.class, "getStatus", this, o);
		if (temp != null)
			return (Integer) temp;

		int status = 0;
		if (isPoison())
			status += 1;
		if (isBrave()) {
			switch (getClassType()) {
			case 0x00:
				status += Lineage.devil_potion_frame ? 48 : 16;
				break;
			case 0x02:
				status += Lineage.elven_wafer_frame ? 48 : 16;
				break;
			case 0x03:
			case 0x04:
				status += Lineage.holywalk_frame ? 64 : 16;
				break;
			default: // 기사 + 용기사 + 환술사 + 기타객체
				status += Lineage.bravery_potion_frame ? 16 : 48;
				break;
			}
		}
		if (isLock())
			status += 8;
		if (isInvis())
			status += 2;
		if (isTransparent()) {
			status += 128;
			// 잭렌턴일경우 상대방이 호박 가면을 착용중인지에 따라 상태 변경.
			if (this instanceof JackLantern && o instanceof PcInstance && o.getInventory() != null) {
				ItemInstance helm = o.getInventory().getSlot(1);
				if (helm != null && helm.getItem().getNameIdNumber() == 2067)
					status -= 128;
			}
			// 마법인형은 표현해야 하기 때문에
			if (this instanceof MagicDollInstance)
				status -= 128;
		}
		//
		if (this instanceof PcInstance)
			status += 4;
		return status;
	}

	/**
	 * 사용자 객체 정령속성 타입값 확인하는 함수.
	 * 
	 * @return
	 */
	public int getAttribute() {
		return Lineage.ELEMENT_NONE;
	}

	/**
	 * 문 객체가 오버리드해서 사용하며, 문이 닫혀있는지 알려주는 함수.
	 * 
	 * @return
	 */
	public boolean isDoorClose() {
		return false;
	}

	/**
	 * 월드에있는 객체를 클릭했을때 호출되는 메서드. : door클릭하면 호출됨. : 아이템 더블클릭하면 호출됨.
	 * 
	 * @param cha
	 * @param cbp
	 */
	public void toClick(Character cha, ClientBasePacket cbp) {
	}

	public void toClick(Character cha, String action, String type, ClientBasePacket cbp) {
	}

	/**
	 * 아이템 클릭후 따로 연결구현을 할때 사용. 클라가 호출 안함.
	 * 
	 * @param cha
	 */
	public void toClickFinal(Character cha, Object... opt) {
	}

	/**
	 * 매개변수 객체 에게 근접 및 장거리 물리공격을 가할때 처리하는 메서드.
	 * 
	 * @param o
	 * @param x
	 * @param y
	 * @param bow
	 */
	public void toAttack(object o, int x, int y, boolean bow, int gfxMode, int alpha_dmg, boolean isTriple,
			int tripleIdx) {
	}

	/**
	 * 특정 npc를 클릭했을경우 대화를 요청처리하는 메서드.
	 * 
	 * @param pc
	 * @param cbp
	 */
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
	}

	/**
	 * 특정 npc를 클릭했을경우 대화를 요청처리하는 메서드.
	 * 
	 * @param pc
	 * @param action
	 * @param type
	 * @param cbp
	 */
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
	}

//	/**
//	 * 특정 npc를 클릭했을경우 대화를 요청처리하는 메서드.
//	 * 
//	 * @param pc
//	 * @param action
//	 * @param type
//	 * @param cbp
//	 */
//	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
//	}

	/**
	 * 성에 세율 변경요청 처리 함수.
	 * 
	 * @param pc
	 * @param tax_rate
	 */
	public void toTaxSetting(PcInstance pc, int tax_rate) {
	}

	/**
	 * 공금 입금 요청 처리 함수.
	 * 
	 * @param pc
	 * @param count
	 */
	public void toTaxPut(PcInstance pc, long count) {
	}

	/**
	 * 공금 출금 요청 처리 함수.
	 * 
	 * @param pc
	 * @param count
	 */
	public void toTaxGet(PcInstance pc, long count) {
	}

	/**
	 * 상점 및 창고에서 물품 처리시 호출하는 메서드.
	 * 
	 * @param pc
	 * @param cbp
	 */
	public void toDwarfAndShop(PcInstance pc, ClientBasePacket cbp) {
	}

	/**
	 * 던전 이동 및 npc를 이용한 텔레포트시 호출해서 사용함.
	 * 
	 * @param x
	 * @param y
	 * @param map
	 */
	public void toPotal(int x, int y, int map) {
	}

	/**
	 * 객체정보를 초기화할때 사용하는 메서드. : pc에서는 케릭터가 죽고 리스하거나 종료할때 호출해서 상태변환용으로도 사용.
	 * 
	 * @param world_out
	 */
	public void toReset(boolean world_out) {
	}

	/**
	 * 부활 처리 메서드.
	 */
	public void toRevival(object o) {
	}

	/**
	 * 부활 처리 메서드.
	 */
	public void toRevivalFinal(object o) {
	}

	/**
	 * 경험치 등록처리 함수.
	 * 
	 * @param o
	 * @param exp
	 */
	public void toExp(object o, double exp) {
	}
	


	/**
	 * 오토루팅 리능 사용여부 처리 함수.
	 * 
	 * @return
	 */
	public boolean isAutoPickup() {
		return false;
	}

	/**
	 * 오토루팅 리능 사용여부 처리 함수.
	 * 
	 * @param is
	 */
	public void setAutoPickup(boolean is) {
	}

	/**
	 * 오토루팅 메세지를 표현할지 를 설정하는 함수.
	 * 
	 * @param is
	 */
	public void setAutoPickupMessage(boolean is) {
	}

	/**
	 * 콜롯세움 사용 여부.
	 * 
	 * @return
	 */
	public boolean iscolosseum_giran() {
		return false;
	};
	
	/**
	 * 콜롯세움 사용 설정하는 함수.
	 * 
	 * @param is
	 */
	public void setcolosseum_giran(boolean is) {
	}

	/**
	 * 오토루팅 메세지를 표현할지 여부.
	 * 
	 * @return
	 */
	public boolean isAutoPickupMessage() {
		return false;
	}

	public boolean isSound() {
		return sound;
	}

	public void setSound(boolean sound) {
		this.sound = sound;
	}

	/**
	 * hp바를 표현할지 여부를 리턴함.
	 * 
	 * @return
	 */
	public boolean isHpbar() {
		return false;
	}

	/**
	 * hp바를 머리위에 표현할지를 설정처리하는 함수.
	 * 
	 * @param is
	 */
	public void setHpbar(boolean is) {
	}

	/**
	 * 물약 멘트 기능 사용여부 처리 함수.
	 * 
	 */
	private boolean autoPotionMent = true;

	public boolean isAutoPotionMent() {
		return autoPotionMent;
	}

	public void setAutoPotionMent(boolean autoPotionMent) {
		this.autoPotionMent = autoPotionMent;
	}
	
	/**
	 * 교환 처리가 취소됫다면 호출됨. : lineage.bean.lineage.Trade.toCancel() 에서 호출함.
	 */
	public void toTradeCancel(Character cha) {
	}

	/**
	 * 교환 처리가 성공했다면 호출됨. : lineage.bean.lineage.Trade.toOk() 에서 호출함.
	 */
	public void toTradeOk(Character cha) {
	}

	/**
	 * 죽엇을때 호출됨.
	 * 
	 * @param cha : 날 죽인 객체 정보.
	 */
	public void toDead(Character cha) {
	}

	/**
	 * 최근에 피케이한 시간값 리턴.
	 * 
	 * @return
	 */
	public long getPkTime() {
		return 0;
	}

	/**
	 * 우호도를 리턴함
	 * 
	 * @return
	 */
	public double getKarma() {
		return 0;
	}

	/**
	 * 공격 가능여부 판단하여 리턴함.<br/>
	 * : 물리 및 마법공격 데미지를 추출할때 확인함.<br/>
	 * : 추가적인 조건이 필요할때 확장에서 사용.<br/>
	 * 
	 * @param cha
	 * @param magic
	 * @return
	 */
	public boolean isAttack(Character cha, boolean magic) {
		return true;
	}

	/**
	 * 엔피시 객체 리턴.
	 * 
	 * @return
	 */
	public Npc getNpc() {
		return null;
	}

	/**
	 * 몬스터 객체 리턴.
	 * 
	 * @return
	 */
	public Monster getMonster() {
		return null;
	}

	/**
	 * 다른 사용자가 강제적으로 아이템을 넘기려할때 호출되는 메서드.
	 * 
	 * @param o
	 * @param item
	 * @param count
	 */
	public void toGiveItem(object o, ItemInstance item, long count) {
		if (getInventory() == null || item.isEquipped() || count <= 0 || item.getCount() < count
				|| (o != null && o.getObjectId() == getObjectId()))
			return;

		if (this instanceof MonsterInstance && item.getItem().getNameIdNumber() == 4) {
			return;
		}

//		System.out.println(this + " / " + o);

		if (this instanceof PetInstance) {

			if (item != null && !getInventory().isWeight(item.getItem().getWeight() * count)) {
				ChattingController.toChatting(this, "무거움", Lineage.CHATTING_MODE_SHOUT);
				return;
			}
		}

		//
		String item_name = item.getName();
		// 아이템 던진거 알리기용.
		if (o != null && o instanceof Character && this instanceof Character)
			item.toGiveItem((Character) o, (Character) this);
		// 인벤에서 겹칠수 있는 아이템 찾기
		ItemInstance temp = getInventory().find(item);
		if (temp != null) {
			// 존재하면 갯수 갱신
			getInventory().count(temp, item, temp.getCount() + count, true);
			if (o != null && o.getInventory() != null) {
				// 던진놈의 아이템 갯수도 갱신
				o.getInventory().count(item, item.getCount() - count, true);
			} else {
				ItemDatabase.setPool(item);
			}
		} else {
			// 객체아이디값이 설정안된 상태일경우 세팅.
			if (item.getObjectId() <= 0) {
				item.setObjectId(ServerDatabase.nextItemObjId());
			}
			if (item.getCount() - count <= 0) {
				// 전체 이동
				temp = item;
				if (o != null && o.getInventory() != null) {
					// 던진놈의 인벤에서 제거.
					o.getInventory().remove(item, true);
				}
			} else {
				// 일부분 이동
				temp = ItemDatabase.newInstance(item);
				temp.setObjectId(ServerDatabase.nextItemObjId());
				temp.setCount(count);
				if (o != null && o.getInventory() != null) {
					// 던진놈의 아이템 갯수 갱신
					o.getInventory().count(item, item.getCount() - count, true);
				} else {
					ItemDatabase.setPool(item);
				}
			}
			// 처리할 아이템 새로 등록.
			getInventory().append(temp, true);
		}

		if (o == null || o instanceof ItemInstance)
			// %0%o 얻었습니다.
			toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 403,
					count == 1 ? item_name : String.format("%s (%d)", item_name, count)));
		else
			// \f1%0%s 당신에게 %1%o 주었습니다.
			toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 143, o.getName(),
					count == 1 ? item_name : String.format("%s (%d)", item_name, count)));
	}

	/**
	 * 아이템이 강제로 넘어간걸 알리는 용
	 * 
	 * @param cha    : 던진 객체
	 * @param target : 받은 객체
	 */
	public void toGiveItem(Character cha, Character target) {
	}

	/**
	 * 다른 객체로부터 데미지를 입었을때 호출됨.
	 * 
	 * @param cha  : 가격자.
	 * @param dmg  : 입혀진 데미지.
	 * @param type : 공격 방식.
	 */
	public void toDamage(Character cha, int dmg, int type, Object... opt) {
	}

	/**
	 * 다른 객체가 마법을 시전하면 호출되는 함수. : 디텍션을 시전시 그에 따른 처리를위해 만들어짐.
	 * 
	 * @param cha
	 * @param c
	 */
	public void toMagic(Character cha, Class<?> c) {
	}

	/**
	 * HyperText 대한 요청 처리.
	 * 
	 * @param pc
	 * @param count
	 * @param a
	 * @param request
	 */
	public void toHyperText(PcInstance pc, ClientBasePacket cbp) {
	}

	/**
	 * CharacterController에 등록된 객체는 해당 함수가 주기적으로 호출됨.
	 * 
	 * @param time
	 */
	public void toTimer(long time) {
	}

	/**
	 * 해당 객체가 픽업됫을때 호출되는 메서드.
	 * 
	 * @param cha
	 */
	public void toPickup(Character cha) {
	}
	
	/**
	 * 매개변수인 pc가 일반 채팅을 하게되면 주변 객체를 검색하게되고<br>
	 * 주변객체들은 해당 메서드를 호출받게됨.<br>
	 * 해당 메서드를 이용해서 대화처리를 하면됨.
	 * 
	 * @param pc
	 * @param msg
	 */
	public void toChatting(object o, String msg) {
	}

	/**
	 * 이동 알리기 함수.
	 * 
	 * @param o
	 */
	public void toMoving(object o) {
	}

	/**
	 * 문짝 센드처리함수. : 객체가 이동중 해당 객체가 닫혀있는지 여부를 확인. 닫혀잇다면 해당 함수를 호출하여 사용자가 해당 필드에 위치를
	 * 이동 불가능하도록 처리. : Door이나 KingdomDoor 객체에 오버리드되서 사용할것이며, 1픽셀 값이상으로 이동 불가능하게 해야할
	 * 경우가 있음.
	 * 
	 * @param o
	 */
	public void toDoorSend(object o) {
		if (getHomeLoc() > 0) {
			for (int i = 0; i < getHomeLoc(); ++i) {
				switch (getHeading()) {
				case 2: // 4방향으로 증가.
				case 6:
					if (o == null) {
						toSender(
								S_Door.clone(BasePacketPooling.getPool(S_Door.class), x, y + i, heading, isDoorClose()),
								false);
					} else {
						o.toSender(S_Door.clone(BasePacketPooling.getPool(S_Door.class), x, y + i, heading,
								isDoorClose()));
					}
					// 타일 변경.
					World.set_map(x, y + i, map, isDoorClose() ? 16 : homeTile[0]);
					World.set_map(x - 1, y + i, map, isDoorClose() ? 16 : homeTile[1]);
					break;
				case 4: // 6방향으로 증가.
					if (o == null) {
						toSender(
								S_Door.clone(BasePacketPooling.getPool(S_Door.class), x - i, y, heading, isDoorClose()),
								false);
					} else {
						o.toSender(S_Door.clone(BasePacketPooling.getPool(S_Door.class), x - i, y, heading,
								isDoorClose()));
					}
					// 타일 변경.
					World.set_map(x - i, y, map, isDoorClose() ? 16 : homeTile[0]);
					World.set_map(x - i, y + 1, map, isDoorClose() ? 16 : homeTile[1]);
					break;
				}
			}
		} else {
			if (o == null) {
				toSender(S_Door.clone(BasePacketPooling.getPool(S_Door.class), this), false);
			} else {
				o.toSender(S_Door.clone(BasePacketPooling.getPool(S_Door.class), this));
			}
			// 타일 변경.
			switch (getHeading()) {
			case 2:
			case 6:
				World.set_map(x, y, map, isDoorClose() ? 16 : homeTile[0]);
				World.set_map(x - 1, y, map, isDoorClose() ? 16 : homeTile[1]);
				break;
			case 4: // 6방향으로 증가.
				World.set_map(x, y, map, isDoorClose() ? 16 : homeTile[0]);
				World.set_map(x, y + 1, map, isDoorClose() ? 16 : homeTile[1]);
				break;
			}
		}
	}

	/**
	 * 인공지능 활성화된 객체를 인공지능 처리목록에서 제거를 원할때 외부에서 호출해서 사용할수 있도록 함수 제공. : toAiSpawn함수를
	 * 이용할경우 재스폰값이 0일때만 제거처리함. : 해당 함수를 이용하면 재스폰값이 0이상이더라도 그냥 제거함.
	 */
	public void toAiThreadDelete() {
		if (!isWorldDelete()) {
			// 월드에서 제거
			World.remove(this);
			// 주변객체 관리 제거
			clearList(true);
		}
		// 상태 변경. 그래야 인공지능 쓰레드에서 제거됨.
		// 그후 풀에 등록함.
		setAiStatus(-1);
	}

	/**
	 * 랜덤워킹 처리 함수.
	 */
	protected void toAiWalk(long time) {
		ai_time = SpriteFrameDatabase.find(gfx, gfxMode + 0);
	}

	/**
	 * 전투 처리 함수.
	 */
	protected void toAiAttack(long time) {
		ai_time = SpriteFrameDatabase.find(gfx, gfxMode + 1);
	}

	/**
	 * 죽은 객체 처리 함수.
	 */
	protected void toAiDead(long time) {
		ai_time = SpriteFrameDatabase.find(gfx, gfxMode + 8);
	}

	/**
	 * 시체 유지 및 제거 처리 함수.
	 */
	protected void toAiCorpse(long time) {
		ai_time = SpriteFrameDatabase.find(gfx, gfxMode + 8);
	}

	/**
	 * 재스폰 처리 함수.
	 */
	protected void toAiSpawn(long time) {
		ai_time = SpriteFrameDatabase.find(gfx, gfxMode + 8);
	}

	/**
	 * 도망가기 처리 함수.
	 */
	protected void toAiEscape(long time) {
		ai_time = SpriteFrameDatabase.find(gfx, gfxMode + 0);
	}

	/**
	 * 아이템 줍기 처리 함수.
	 */
	protected void toAiPickup(long time) {
		ai_time = SpriteFrameDatabase.find(gfx, gfxMode + 15);
	}

	/**
	 * 인공지능 처리 요청 함수.
	 * 
	 * @param time
	 */
	public void toAi(long time) {
		synchronized (sync_ai) {
			// 일반 적인 인공지능 패턴
			// 랜덤워킹, 죽은거체크, 시체유지, 도망가기, 스폰멘트, 죽을때멘트, 공격할때멘트
			switch (getAiStatus()) {
			case -1:
				break;
			case 0:
				try {
					toAiWalk(time);
				} catch (Exception e) {
				}
				break;
			case 1:
				toAiAttack(time);
				break;
			case 2:
				toAiDead(time);
				break;
			case 3:
				toAiCorpse(time);
				break;
			case 4:
				toAiSpawn(time);
				break;
			case 5:
				toAiEscape(time);
				break;
			case 6:
				toAiPickup(time);
				break;
			default:
				ai_time = SpriteFrameDatabase.find(gfx, gfxMode + 0);
				break;
			}
		}
	}

	/**
	 * 주변객체에게 전송하면서 나에게도 전송할지 여부
	 * 
	 * @param bp
	 * @param me
	 */
	public void toSender(BasePacket bp, boolean me) {
		if (bp instanceof ServerBasePacket) {
			ServerBasePacket sbp = (ServerBasePacket) bp;
			for (object o : getInsideList(true)) {
				if (o instanceof PcInstance)
					o.toSender(ServerBasePacket.clone(BasePacketPooling.getPool(ServerBasePacket.class), sbp.getBytes()));
			}
			if (me)
				toSender(bp);
			else
				BasePacketPooling.setPool(bp);
		} else {
			BasePacketPooling.setPool(bp);
		}
	}

	/**
	 * 본인에게만 전송하는 패킷
	 * 
	 * @param bp
	 * @param me
	 */
	public void toSenderMe(BasePacket bp) {
		if (bp instanceof ServerBasePacket) {
			toSender(bp);
		} else {
			BasePacketPooling.setPool(bp);
		}
	}

	/**
	 * 패킷 전송 처리
	 * 
	 * @param bp
	 */
	public void toSender(BasePacket bp) {
		// 풀에 다시 넣기
		BasePacketPooling.setPool(bp);
	}

	/**
	 * 이동 요청 처리 함수.
	 * 
	 * @param x
	 * @param y
	 * @param h
	 */
	public void toMoving(final int x, final int y, final int h) {

		// 동적값 갱신.
		if (isDynamicUpdate())
			World.update_mapDynamic(this.x, this.y, this.map, false);
		// 좌표 변경.
		this.x = x;
		this.y = y;
		this.heading = h;
		// 동적값 갱신.
		if (isDynamicUpdate())
			World.update_mapDynamic(x, y, map, true);
		// 주변객체 갱신
		if (!Util.isDistance(tempX, tempY, map, x, y, map, Lineage.SEARCH_LOCATIONRANGE)) {
			tempX = x;
			tempY = y;
			List<object> temp = new ArrayList<object>();
			// 이전에 관리중이던 목록 갱신
			synchronized (allList) {
				temp.addAll(allList);
				allList.clear();
			}
			for (object o : temp)
				o.removeAllList(this);
			// 객체 갱신
			temp.clear();
			World.getLocationList(this, Lineage.SEARCH_WORLD_LOCATION, temp);
			for (object o : temp) {
				if (isList(o)) {
					// 전체 관리목록에 등록.
					appendAllList(o);
					o.appendAllList(this);
				}
			}
			// 이벤트 처리 요청.
			if (this instanceof PcInstance)
				EventController.toUpdate((PcInstance) this);
		}
		// 주변객체 패킷 및 갱신 처리
		for (object o : getAllList(true)) {
			if (Util.isDistance(this, o, Lineage.SEARCH_LOCATIONRANGE)) {
				if (isContainsInsideList(o)) {
					if (o instanceof PcInstance)
						o.toSender(S_ObjectMoving.clone(BasePacketPooling.getPool(S_ObjectMoving.class), this));
					// 이동 알리기.
					o.toMoving(this);
				} else {
					appendInsideList(o);
					o.appendInsideList(this);
					//
					if (this instanceof PcInstance) {
						if (o.getGm() > 0 && o.isInvis()) {
						} else {
							toSender(S_ObjectAdd.clone(BasePacketPooling.getPool(S_ObjectAdd.class), o, this));
							if (o instanceof Door || o instanceof KingdomDoor)
								o.toDoorSend(this);
						}
					}
					if (o instanceof PcInstance) {
						if (getGm() > 0 && isInvis()) {
						} else {
							o.toSender(S_ObjectAdd.clone(BasePacketPooling.getPool(S_ObjectAdd.class), this, o));
						}
					}
				}
			} else {
				if (isContainsInsideList(o)) {
					removeInsideList(o);
					o.removeInsideList(this);
					if (this instanceof PcInstance)
						toSender(S_ObjectRemove.clone(BasePacketPooling.getPool(S_ObjectRemove.class), o));
					if (o instanceof PcInstance)
						o.toSender(S_ObjectRemove.clone(BasePacketPooling.getPool(S_ObjectRemove.class), this));
				}
			}
		}
	}

	/**
	 * 텔레포트 처리 함수.
	 * 
	 * @param x
	 * @param y
	 * @param map
	 * @param effect
	 */
	public void toTeleport(final int x, final int y, final int map, final boolean effect) {
		if (effect) {
			if (this instanceof MagicDollInstance)
				toSenderMe(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this,
						Lineage.doll_teleport_effect));
			else
				toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this,
						Lineage.object_teleport_effect), this instanceof PcInstance);
		}
		// 이전에 관리중이던 목록 제거
		clearList(true);
		// 월드에 갱신
		World.remove(this);
		// 동적값 갱신.	
		if (isDynamicUpdate())
			World.update_mapDynamic(this.x, this.y, this.map, false);

		// 좌표 변경.
		this.x = x;
		this.y = y;
		this.map = map;
		tempX = x;
		tempY = y;

		// 동적값 갱신.
		if (isDynamicUpdate())
			World.update_mapDynamic(x, y, map, true);
		// 월드에 갱신.
		World.append(this);
		// 패킷 처리
		if (this instanceof PcInstance) {
			toSender(S_ObjectMap.clone(BasePacketPooling.getPool(S_ObjectMap.class), this));
			toSender(S_ObjectAdd.clone(BasePacketPooling.getPool(S_ObjectAdd.class), this, this));
		}
		// 객체 갱신
		List<object> temp_list = new ArrayList<object>();
		World.getLocationList(this, Lineage.SEARCH_WORLD_LOCATION, temp_list);
		// 순회
		for (object o : temp_list) {
			if (isList(o)) {
				// 전체 관리목록에 등록.
				appendAllList(o);
				o.appendAllList(this);
				// 화면내에 있을경우
				if (Util.isDistance(this, o, Lineage.SEARCH_LOCATIONRANGE)) {
					// 화면내에 관리 목록에 등록
					appendInsideList(o);
					o.appendInsideList(this);
					// 사용자들 패킷 처리
					if (this instanceof PcInstance) {
						toSender(S_ObjectAdd.clone(BasePacketPooling.getPool(S_ObjectAdd.class), o, this));
						if (o.isDoorClose())
							o.toDoorSend(this);
					}
					if (o instanceof PcInstance) {
						o.toSender(S_ObjectAdd.clone(BasePacketPooling.getPool(S_ObjectAdd.class), this, o));
					}
				}
			}
		}
		temp_list.clear();
		temp_list = null;
	}

	 public void toTeleport2(final int x, final int y, final int map, final boolean effect) {
	      //
	      if (!isWorldDelete()) {
	         if (effect) {
	            if (this instanceof MagicDollInstance)
	               toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this,
	                     Lineage.doll_teleport_effect), true);
	            else
	               toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this,
	                     Lineage.object_teleport_effect), this instanceof PcInstance);
	         }
	         // 월드에 갱신
//	         World.remove(this);
	         // 이전에 관리중이던 목록 제거
	         clearList(true);
	      }
	      // 좌표 변경.
	      this.x = x;
	      this.y = y;
	      this.map = map;
	      tempX = x;
	      tempY = y;
	      // 동적값 갱신.
	      if (isDynamicUpdate())
	         World.update_mapDynamic(x, y, map, true);
	      // 월드에 갱신.
//	      World.append(this);
	      // 패킷 처리
	      if (this instanceof PcInstance) {
	         toSender(S_ObjectMap.clone(BasePacketPooling.getPool(S_ObjectMap.class), this));
	         toSender(S_ObjectAdd.clone(BasePacketPooling.getPool(S_ObjectAdd.class), this, this));
	      }
	      // 객체 갱신
	      List<object> temp_list = new ArrayList<object>();
	      World.getLocationList(this, Lineage.SEARCH_WORLD_LOCATION, temp_list);
	      // 순회
	      for (object o : temp_list) {
	         if (isList(o)) {
	            // 전체 관리목록에 등록.
	            appendAllList(o);
	            o.appendAllList(this);
	            // 화면내에 있을경우
	            if (Util.isDistance(this, o, Lineage.SEARCH_LOCATIONRANGE)) {
	               // 화면내에 관리 목록에 등록
	               appendInsideList(o);
	               o.appendInsideList(this);
	               // 사용자들 패킷 처리
	               if (this instanceof PcInstance) {
	                  if (o.getGm() > 0 && o.isInvis()) {
	                  } else {
	                     toSender(S_ObjectAdd.clone(BasePacketPooling.getPool(S_ObjectAdd.class), o, this));
	                     if (o instanceof Door || o instanceof KingdomDoor)
	                        o.toDoorSend(this);
	                  }
	               }
	               if (o instanceof PcInstance) {
	                  if (getGm() > 0 && isInvis()) {
	                  } else {
	                     o.toSender(S_ObjectAdd.clone(BasePacketPooling.getPool(S_ObjectAdd.class), this, o));
	                  }
	               }
	            }
	         }
	      }
	      temp_list.clear();
	      temp_list = null;

	      // 던전
	      /*
	       * if(this instanceof PcInstance){ DungeonController.toDungeon(this); }
	       */
	   }
	/**
	 * 관리중이던 객체 초기화처리 하는 함수.
	 */
	public void clearList(boolean packet) {
		List<object> temp_list = new ArrayList<object>();
		try {
			// LOCATIONRANGE 셀 내에 있는 목록들 둘러보면서 나를 제거.
			synchronized (insideList) {
				temp_list.addAll(insideList);
				insideList.clear();
			}
			for (object o : temp_list) {
				o.removeInsideList(this);
				if (packet && o instanceof PcInstance)
					o.toSender(S_ObjectRemove.clone(BasePacketPooling.getPool(S_ObjectRemove.class), this));
			}
			temp_list.clear();
			synchronized (allList) {
				temp_list.addAll(allList);
				allList.clear();
			}
			for (object o : temp_list) {
				o.removeAllList(this);
				if (o instanceof MonsterInstance) {
					MonsterInstance mi = (MonsterInstance) o;
					mi.removeAttackList(this);
					mi.removeAstarList(this);
					mi.removeExpList(this);
				}
				if (o instanceof NpcInstance) {
					NpcInstance ni = (NpcInstance) o;
					ni.removeAttackList(this);
				}
				if (o instanceof PcRobotInstance) {
					PcRobotInstance pri = (PcRobotInstance) o;
					pri.removeAttackList(this);
					pri.removeAstarList(this);
				}
			}
		} catch (Exception e) {
			System.out.println("clearList : " + e);
		}
		temp_list.clear();
		temp_list = null;

		if (isDynamicUpdate())
			World.update_mapDynamic(getX(), getY(), getMap(), false);
	}

	/**
	 * 인공지능. 객체의 인공지능이 활성화해야 할 시간이 됬는지 확인하는 메서드.
	 */
	public boolean isAi(long time) {
		long speed = ai_time <= 0 ? SpriteFrameDatabase.find(gfx, gfxMode + 0) : ai_time;
		long temp = time - ai_start_time;

		switch (getSpeed()) {
		case 1:
			speed -= (long) (speed * 0.25);
			break;
		case 2:
			speed += (long) (speed * 0.25);
			break;
		}

		if (isBrave()) {
			if (getClassType() == 0x02)
				speed -= (long) (speed * 0.15);
			else
				speed -= (long) (speed * 0.2);
		}

//		if(time==0 || (temp>=speed && ai_time<=temp)){
		if (time == 0 || temp >= speed) {
			ai_start_time = time;
			// 락걸린 상태라면 true가 리턴되며, ai를 활성화 하면 안되기때문에 false로 변환해야됨.
			return !isLock();
		}

		return false;
	}

	/**
	 * 월드 타일에 누적카운팅처리를 할 객체인지 여부를 리턴함.
	 * 
	 * @return
	 */
	public boolean isDynamicUpdate() {
		//
		if (getGm() > 0)
			return false;
		if (isTransparent())
			return false;
		if (isDead())
			return false;
		//
		if (this instanceof KingdomCastleTop)
			return false;
		if (this instanceof MagicDollInstance)
			return false;
		if (this instanceof KingdomDoor)
			return false;
		//
		if (this instanceof Character || this instanceof TeleportInstance || this instanceof DwarfInstance
				|| this instanceof ShopInstance || this instanceof BoardInstance || this instanceof TalkNpc
				|| this instanceof TeleportInstance || this instanceof Cracker || this instanceof CrackerDmg
				|| this instanceof Racer || this instanceof DeathEffect)
			return true;
		//
		if (this instanceof BackgroundInstance)
			return false;
		//
		return false;
	}

	/**
	 * 월드에서 객체를 추출한후 해당 객체를 관리목록에 등록할지 여부를 이 함수가 판단. : 텔레포트후, 이동후 등에서 호출해서 사용중.
	 * 
	 * @param o
	 * @return
	 */
	protected boolean isList(object o) {
		if (getObjectId() == o.getObjectId())
			return false;

		// 사용자일경우 무조건 등록.
		if (this instanceof PcInstance || o instanceof PcInstance)
			return true;
		// 소환객체일 경우.
		if (this instanceof SummonInstance)
			// 아이템
			return o instanceof ItemInstance;
		// 몬스터일 경우.
		if (this instanceof MonsterInstance) {
			MonsterInstance mon = (MonsterInstance) this;
			// 아이템
			if (o instanceof ItemInstance)
				return mon.getMonster().isPickup() || mon.getBoss() != null;
			// 서먼객체
			if (o instanceof SummonInstance)
				return true;
//			// 동족
//			if(o instanceof MonsterInstance)
//				return mon.getMonster().getFamily().length()>0 && ((MonsterInstance)o).getMonster().getFamily().equalsIgnoreCase(mon.getMonster().getFamily());
			if (o instanceof MonsterInstance)
				return true;
		}
		// 스위치 일경우. (법사30퀘 스위치)
		if (this instanceof Switch) {
			// 문
			return o instanceof Door;
		}
		// 아이템일 경우
		if (this instanceof ItemInstance) {
			if (o instanceof PetInstance)
				return true;
			if (o instanceof MonsterInstance)
				return ((MonsterInstance) o).getMonster().isPickup() || ((MonsterInstance) o).getBoss() != null;
		}
		// 성문 일경우
		if (this instanceof KingdomDoor)
			return o instanceof KingdomGuard || o instanceof KingdomDoorman;
		// 성경비 일경우
		if (this instanceof KingdomGuard)
			return o instanceof KingdomDoor || o instanceof KingdomGuard || o instanceof KingdomCastleTop;
		// 경비 일경우
		if (this instanceof GuardInstance)
			return o instanceof GuardInstance && !(o instanceof KingdomGuard);
		// 수호탑일 경우
		if (this instanceof KingdomCastleTop)
			return o instanceof KingdomGuard;
		// 성 문지기 일경우
		if (this instanceof KingdomDoorman)
			return o instanceof KingdomDoor;
		// 배경에쓰이는 객체일 경우
		if (this instanceof BackgroundInstance) {
			// 파이어월 일경우 hp 처리하는 객체는 관리목록에 등록.
			if (this instanceof Firewall)
				return o instanceof Character;
			// 라이프 스트림역시~
			if (this instanceof LifeStream)
				return o instanceof Character;
			// 유령의집 에 사용되는 버튼일경우 문만 관리목록에 등록.
			if (this instanceof GhostHouseDoorButton)
				return o instanceof Door;
		}
		// 트랩
		if (this instanceof TrapArrow) {
			if (o instanceof PcInstance)
				return true;
			if (o instanceof MonsterInstance)
				return true;
		}
		return false;
	}
	
	/**
	 * 오만 맵인지 확인하는 함수
	 **/
	public boolean isOman() {
		if (getMap() == 101 || getMap() == 102 || getMap() == 103
				|| getMap() == 104 || getMap() == 105 || getMap() == 106
				|| getMap() == 107 || getMap() == 108 || getMap() == 109
				|| getMap() == 110 || getMap() == 200)
			return true;
		return false;
	}


	public long getDelaytime() {
		return this._delaytime;
	}

	public void setDelaytime(long paramLong) {
		this._delaytime = paramLong;
	}

	public boolean isBoardView() {
		return this.isBoardView;
	}

	public void setBoardView(boolean paramBoolean) {
		this.isBoardView = paramBoolean;
	}

	/*****************************/
	/***** 9무기이상속성5이상 ******/
//	WaterWeapon = WindWeapon = EarthWeapon = FireWeapon
	public int getWaterWeapon() {
		return WaterWeapon;
	}

	public void setWaterWeapon(int WaterWeapon) {
		this.WaterWeapon = WaterWeapon;
	}

	public int getWindWeapon() {
		return WindWeapon;
	}

	public void setWindWeapon(int WindWeapon) {
		this.WindWeapon = WindWeapon;
	}

	public int getEarthWeapon() {
		return EarthWeapon;
	}

	public void setEarthWeapon(int EarthWeapon) {
		this.EarthWeapon = EarthWeapon;
	}

	public int getFireWeapon() {
		return FireWeapon;
	}

	public void setFireWeapon(int FireWeapon) {
		this.FireWeapon = FireWeapon;
	}

	public double getEarring() {
		synchronized (sync_dynamic) {
			return Earring;
		}
	}

	public void setEarring(double earring) {
		synchronized (sync_dynamic) {
			this.Earring = earring;
		}
	}

	public double getEarring2() {
		synchronized (sync_dynamic) {
			return Earring2;
		}
	}

	public void setEarring2(double earring2) {
		synchronized (sync_dynamic) {
			this.Earring2 = earring2;
		}
	}

	public boolean isBuffBreaveAvt() {
		return BuffBreaveAvt;
	}

	public void setBuffBreaveAvt(boolean buffBreaveAvt) {
		BuffBreaveAvt = buffBreaveAvt;
	}

	public boolean isBuffGreaseAvt() {
		return BuffGreaseAvt;
	}

	public void setBuffGreaseAvt(boolean buffGreaseAvt) {
		BuffGreaseAvt = buffGreaseAvt;
	}

	public boolean isKnightHoly() {
		return KnightHoly;
	}

	public void setKnightHoly(boolean kh) {
		KnightHoly = kh;
	}

	public boolean isFish() {
		return fish;
	}

	public void setFish(boolean fi) {
		fish = fi;
	}

	public int getMacroIndex() {
		synchronized (sync_macro) {
			return this.macroIndex;
		}
	}

	public void setMacroIndex(int paramInt) {
		synchronized (sync_macro) {
			this.macroIndex = paramInt;
		}
	}

	public int getMacroPeriod() {
		synchronized (sync_macro) {
			return this.macroPeriod;
		}
	}

	public void setMacroPeriod(int paramInt) {
		synchronized (sync_macro) {
			this.macroPeriod = paramInt;
		}
	}

	public long getMacroDelay() {
		synchronized (sync_macro) {
			return this.macroDelay;
		}
	}

	public void setMacroDelay(long paramLong) {
		synchronized (sync_macro) {
			this.macroDelay = paramLong;
		}
	}

	public ArrayList<String> getMacroMessage() {
		synchronized (sync_macro) {
			return this.MacroMessage;
		}
	}

	public String getSise_tel_name() {
		return sise_tel_name;
	}

	public void setSise_tel_name(String sise_tel_name) {
		this.sise_tel_name = sise_tel_name;
	}
	
	public boolean isPersnalShopInsert() {
		return persnalShopInsert;
	}

	public void setPersnalShopInsert(boolean persnalShopInsert) {
		this.persnalShopInsert = persnalShopInsert;
	}

	public boolean isPersnalShopPriceSetting() {
		return persnalShopPriceSetting;
	}

	public void setPersnalShopPriceSetting(boolean persnalShopPriceSetting) {
		this.persnalShopPriceSetting = persnalShopPriceSetting;
	}

	public boolean isPersnalShopEdit() {
		return persnalShopEdit;
	}

	public void setPersnalShopEdit(boolean persnalShopEdit) {
		this.persnalShopEdit = persnalShopEdit;
	}

	public boolean isPersnalShopAddPriceSetting() {
		return persnalShopAddPriceSetting;
	}

	public void setPersnalShopAddPriceSetting(boolean persnalShopAddPriceSetting) {
		this.persnalShopAddPriceSetting = persnalShopAddPriceSetting;
	}

	public boolean isPersnalShopPriceReset() {
		return persnalShopPriceReset;
	}

	public void setPersnalShopPriceReset(boolean persnalShopPriceReset) {
		this.persnalShopPriceReset = persnalShopPriceReset;
	}

	public boolean isPersnalShopSellInsert() {
		return persnalShopSellInsert;
	}

	public void setPersnalShopSellInsert(boolean persnalShopSellInsert) {
		this.persnalShopSellInsert = persnalShopSellInsert;
	}

	public boolean isPersnalShopSellEdit() {
		return persnalShopSellEdit;
	}

	public void setPersnalShopSellEdit(boolean persnalShopSellEdit) {
		this.persnalShopSellEdit = persnalShopSellEdit;
	}

	public boolean isPersnalShopSellPriceSetting() {
		return persnalShopSellPriceSetting;
	}

	public void setPersnalShopSellPriceSetting(boolean persnalShopSellPriceSetting) {
		this.persnalShopSellPriceSetting = persnalShopSellPriceSetting;
	}

	public boolean isPersnalShopSellPriceReSetting() {
		return persnalShopSellPriceReSetting;
	}

	public void setPersnalShopSellPriceReSetting(boolean persnalShopSellPriceReSetting) {
		this.persnalShopSellPriceReSetting = persnalShopSellPriceReSetting;
	}

	public boolean isPersnalShopPurchasePriceIn() {
		return persnalShopPurchasePriceIn;
	}

	public void setPersnalShopPurchasePriceIn(boolean persnalShopPurchasePriceIn) {
		this.persnalShopPurchasePriceIn = persnalShopPurchasePriceIn;
	}

	public boolean isSaveCharacter() {
		return saveCharacter;
	}

	public void setSaveCharacter(boolean saveCharacter) {
		this.saveCharacter = saveCharacter;
	}

	public boolean isSaveExp() {
		return saveExp;
	}

	public void setSaveExp(boolean saveExp) {
		this.saveExp = saveExp;
	}

	public boolean isCharacterMarblesUse() {
		return characterMarblesUse;
	}

	public void setCharacterMarblesUse(boolean characterMarblesUse) {
		this.characterMarblesUse = characterMarblesUse;
	}

	public boolean isExpMarblesUse() {
		return expMarblesUse;
	}

	public void setExpMarblesUse(boolean expMarblesUse) {
		this.expMarblesUse = expMarblesUse;
	}

	public long getMarblesObjectId() {
		return marblesObjectId;
	}

	public void setMarblesObjectId(long marblesObjectId) {
		this.marblesObjectId = marblesObjectId;
	}

	private List<String> shopNameList = new ArrayList<String>();

	public List<String> getShopNameList() {
		return shopNameList;
	}

	public void addShopNameList(String shopName) {
		shopNameList.add((String) shopName);
	}

	public void clearShopName() {
		shopNameList.clear();
	}

	public boolean isBuffExpPotion() {
		return BuffExpPotion;
	}

	public void setBuffExpPotion(boolean buffExpPotion) {
		BuffExpPotion = buffExpPotion;
	}

	public boolean isBuffExpPotion2() {
		return BuffExpPotion2;
	}

	public void setBuffExpPotion2(boolean buffExpPotion2) {
		BuffExpPotion2 = buffExpPotion2;
	}

	public boolean isBuffExpPotion3() {
		return BuffExpPotion3;
	}

	public void setBuffExpPotion3(boolean buffExpPotion3) {
		BuffExpPotion3 = buffExpPotion3;
	}
	
	public boolean isBuffExpPotion4() {
		return BuffExpPotion4;
	}

	public void setBuffExpPotion4(boolean buffExpPotion3) {
		BuffExpPotion4 = buffExpPotion3;
	}

	/**
	 * 뚫어핵 잡기위한 코드 by Jsn_Soft
	 * 
	 * @return
	 */
	private boolean move;
	private boolean teleport;

	public boolean isMove() {
		return move;
	}

	public void setMove(boolean move) {
		this.move = move;
	}

	public boolean isTeleport() {
		return teleport;
	}

	public void setTeleport(boolean teleport) {
		this.teleport = teleport;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int trueTargetTime;

	public int getTrueTargetTime() {
		return trueTargetTime;
	}

	public void setTrueTargetTime(int trueTargetTime) {
		this.trueTargetTime = trueTargetTime;
	}

	public long get투망시간() {
		return 투망시간;
	}

	public void set투망시간(long 투망시간) {
		this.투망시간 = 투망시간;
	}

	public boolean isMacro_mag() {
		return macro_mag;
	}

	public void setMacro_mag(boolean macro_mag) {
		this.macro_mag = macro_mag;
	}
	
	
	
//	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
//	}
}
