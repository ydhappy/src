package lineage.world.object.instance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cholong.buff.CastleMemberBuff;
import cholong.buff.ClanMemberBuff;
import jsn_soft.AutoHuntDatabase;
import jsn_soft.AutoHuntThread;
import jsn_soft.Itemtrade;
import lineage.bean.database.Exp;
import lineage.bean.database.ExpPanelty;
import lineage.bean.database.Item;
import lineage.bean.database.PcShop;
import lineage.bean.database.Skill;
import lineage.bean.database.marketPrice;
import lineage.bean.lineage.Buff;
import lineage.bean.lineage.Clan;
import lineage.bean.lineage.Inventory;
import lineage.bean.lineage.Kingdom;
import lineage.bean.lineage.Summon;
import lineage.bean.lineage.Swap;
import lineage.database.ServerDatabase;
import lineage.database.TimeDungeonDatabase;
import lineage.database.AccountDatabase;
import lineage.database.CharactersDatabase;
import lineage.database.ConnectorDatabase;
import lineage.database.DatabaseConnection;
import lineage.database.ExpDatabase;
import lineage.database.ExpPaneltyDatabase;
import lineage.database.ItemDatabase;
import lineage.database.NpcSpawnlistDatabase;
import lineage.database.QuestDatabase;
import lineage.database.QuestDatabase.PcLevelQuest;
import lineage.database.QuestDatabase.PcRepeatQuest;
import lineage.database.QuestPresentDatabase;
import lineage.database.SkillDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.database.TeleportHomeDatabase;
import lineage.database.TeleportResetDatabase;
import lineage.gui.GuiMain;
import lineage.network.LineageClient;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Ability;
import lineage.network.packet.server.S_BaseStat;
import lineage.network.packet.server.S_CharacterDg;
import lineage.network.packet.server.S_CharacterEr;
import lineage.network.packet.server.S_CharacterHp;
import lineage.network.packet.server.S_CharacterKarma;
import lineage.network.packet.server.S_CharacterMp;
import lineage.network.packet.server.S_CharacterSpMr;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_Disconnect;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_InterfaceRead;
import lineage.network.packet.server.S_InventorySlot;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.network.packet.server.S_ObjectAttack;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectHeading;
import lineage.network.packet.server.S_ObjectHitratio;
import lineage.network.packet.server.S_ObjectInvis;
import lineage.network.packet.server.S_ObjectLawful;
import lineage.network.packet.server.S_ObjectLight;
import lineage.network.packet.server.S_ObjectLock;
import lineage.network.packet.server.S_ObjectMode;
import lineage.network.packet.server.S_ObjectRevival;
import lineage.network.packet.server.S_Potal;
import lineage.network.packet.server.S_SoundEffect;
import lineage.network.packet.server.S_Test;
import lineage.network.packet.server.S_TestInven;
import lineage.network.packet.server.S_Weather;
import lineage.plugin.PluginController;
import lineage.share.Common;
import lineage.share.GameSetting;
import lineage.share.Lineage;
import lineage.share.Lineage_Balance;
import lineage.share.Log;
import lineage.share.System;
import lineage.util.Util;
import lineage.world.AStar;
import lineage.world.Node;
import lineage.world.World;
import lineage.world.controller.AutoHuntCheckController;
import lineage.world.controller.BaphometSystemController;
import lineage.world.controller.BaseStatController;
import lineage.world.controller.BookController;
import lineage.world.controller.BuffController;
import lineage.world.controller.CharacterController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.ClanController;
import lineage.world.controller.CommandController;
import lineage.world.controller.DamageController;
import lineage.world.controller.DungeonController;
import lineage.world.controller.FishingController;
import lineage.world.controller.FriendController;
import lineage.world.controller.GhostHouseController;
import lineage.world.controller.InventoryController;
import lineage.world.controller.KingdomController;
import lineage.world.controller.LetterController;
import lineage.world.controller.LocationController;
import lineage.world.controller.MagicDollController;
import lineage.world.controller.PartyController;
import lineage.world.controller.PcMarketController;
import lineage.world.controller.QuestController;
import lineage.world.controller.SkillController;
import lineage.world.controller.SummonController;
import lineage.world.controller.TradeController;
import lineage.world.controller.UserAdenaSellController;
import lineage.world.controller.UserShopController;
import lineage.world.controller.WantedController;
import lineage.world.controller.무인혈맹컨트롤러;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.item.MagicDoll;
import lineage.world.object.item.potion.BraveryPotion;
import lineage.world.object.item.potion.BraveryPotion2;
import lineage.world.object.item.potion.ElvenWafer;
import lineage.world.object.item.potion.HastePotion;
import lineage.world.object.item.potion.HealingPotion;
import lineage.world.object.magic.FrameSpeedOverStun;
import lineage.world.object.magic.Haste;
import lineage.world.object.magic.ImmuneToHarm;
import lineage.world.object.magic.ShapeChange;
import lineage.world.object.magic.ShockStun;
import lineage.world.object.magic.TripleArrow;
import lineage.world.object.magic.item.Bravery;
import lineage.world.object.magic.item.Criminal;
import lineage.world.object.magic.item.Wafer;
import lineage.world.object.npc.background.Cracker;
import lineage.world.object.npc.background.CrackerDmg;
import lineage.world.object.npc.teleporter.Esmereld;
import lineage.world.object.robot.PcRobotInstance;
import lineage.network.packet.server.S_ObjectCriminal;

public class PcInstance extends Character {

	private LineageClient client;
	
	private int attribute; // 요정 클레스들 속성마법 값 1[물] 2[바람] 3[땅] 4[불]
	private int attackCnt; // 접속 후 공격한 횟수. (쌍수일때 무기데미지 처리를 번갈아가면서 하기 위해.)
	// 운영자 여부 판단 변수.
	private int gm;
	public int checkmenttime;
	// 스피드핵 처리 참고용 변수
	private int SpeedhackWarningCounting;
	// 파티변수
	private long partyid;
	// 채팅 사용 유무
	private boolean chattingWhisper;
	private boolean chattingGlobal;
	private boolean chattingTrade;
	// 피케이 처리 변수
	private int PkCount; // 누적된 피케이 횟수.
	private long PkTime; // 최근에 실행한 피케이 시간.
	private int PkHellTime; // 지옥에서 머물러야하는 시간 초단위.
	private int PkHellCount; // 지옥 횟수.
	private int DeathCount; // 데스카운트
	public boolean deadtrue;
	// 결투장을 위한 변수
	public boolean isBattlezone;
	private boolean ment = false;
	// 바포메트 시스템을 위한 변수
	private boolean isBaphomet;
	private int baphometLevel;
	
	// 장비 스왑
	private Map<String, Swap[]> swap;
	public String[] swapIdx;
	public String selectSwap;
	public boolean isInsertSwap;

	// 마법인형을 위한 변수
	private MagicDoll magicDoll;
	private MagicDollInstance magicDollinstance;

	// 중립 혈맹
	private boolean newClanOut = false;
	private Timestamp newClanOutTime = null;
	// 중복코드 방지용. : 물약 멘트 체크
	private boolean autoPotionMent = true;
	// 몬스터를죽이 전체 갯수.
	private int MonsterKillCount;
	// 인벤토리
	private Inventory inv;
	// 차단된 이름 목록
	private List<String> listBlockName;
	private Esmereld npc_esmereld; // 에스메랄다 npc 포인터
	// 딜레이용
	private long message_time; // 슈롬 메세지가 표현된 마지막 시간 저장.
	private long auto_save_time; // 자동저장 이전시간 기록용.
	private long premium_item_time; // 자동지급 시간 저장.
	private long open_wait_item_time; // 자동지급 시간 저장.
	// 로그 기록용
	private long register_date; // 케릭 생성 시간
	private long join_date; // 케릭 접속 시간
	// 출석 딜레이
	public double Levelexpname;
	// 출석 체크
	public int daycount;
	public int daycheck;
	public int dayptime;
	// 죽으면서 착감된 경험치 실시간 기록용.
	private double lost_exp;
	// 오토루팅 처리할지 여부를 확인할 변수.
	private boolean auto_pickup;
	private boolean auto_pickup_message;
	// 자동사냥 카운터
	private int autoHuntMonsterCount;
	// 자동사냥 답
	private String autoHuntAnswer;
	// 자동사냥 방지 인증번호 받은 후 공격불가 까지 딜레이
	private long autoHuntAnswerTime;
	public boolean isInsert_autoAnswer;
	private boolean auto_party_message;
	
	// 자신에 hp바를 머리위에 표현할지 여부.
	private boolean is_hpbar;
	// 인터페이스 및 인벤 정보.
	private byte[] db_interface;

	// 아이템 클릭 딜레이를 주기위한 변수.
	public long click_delay;
	public long click_armor_delay;
	public long click_weapon_delay;
	// 케릭터를 월드에 유지해야할 경우에 사용됨.
	private long worldout_time;
	// 우호도 값
	private double karma;
	private Object sync;
	// 공격을 시도하게되면 공격대상장에 객체를 임시 저장하여 활용하는 용도로 사용.
	private object attack_target; // 대상자
	public boolean stat_clear; // 스초에서 사용중.
	private int pevent = 0;
	public boolean el_ok;
	private boolean swardLack;
	private boolean heal_1;
	private boolean heal_2;
	private boolean heal_3;
	private boolean heal_4;

	private boolean adenChange;

	private boolean is_Gaho_Buff;
	public boolean is_Gaho;
	// 칼렉 딜레이를 위한 변수
	private long lastLackTime;

	private String tempName;
	private String tempClanName;
	private String tempTitle;
	private int tempClanId;
	private int tempClanGrade;
	
	// VIP시스템
	private boolean isPandantBuff;
	private int PandantBuff_Level;

	// 퀘스트
	private int questchapter;
	private int questkill;

	private int randomquest;
	private int randomquestkill;
	private int randomquestfinish;
	private int randomquestplay;
	
	// 시간제 맵 위한 변수
	private int giran_dungeon_time;
	// 기란감옥 시간 초기화 주문서 횟수 제한
	public int giran_dungeon_count;
	
	private int enfail;
	// 시세검색
	public List<marketPrice> marketPrice;
	
	public PcInstance(LineageClient client) {
		this.client = client;
		listBlockName = new ArrayList<String>();
		sync = new Object();
		shop_temp_list = new ArrayList<ItemInstance>();
		swap = new HashMap<String, Swap[]>();
		swapIdx = new String[Lineage.SLOT_GARDER];
		adena_list = new ArrayList<String>();
		adena_list2 = new ArrayList<String>();
		autoPotionIdx = new String[20];
		marketPrice = new ArrayList<marketPrice>();
		PickItemSetting();
	}
	
	public PcInstance() {
		listBlockName = new ArrayList<String>();
		sync = new Object();
		shop_temp_list = new ArrayList<ItemInstance>();
		swap = new HashMap<String, Swap[]>();
		swapIdx = new String[Lineage.SLOT_GARDER];
		adena_list = new ArrayList<String>();
		adena_list2 = new ArrayList<String>();
		autoPotionIdx = new String[20];
		marketPrice = new ArrayList<marketPrice>();
		PickItemSetting();
	}

	private int luck;

	private int aden;

	private boolean auto;

	private boolean chkspeed;

	private boolean clanexp;

	private boolean chkpoly;

	private boolean royalbuff;

	private boolean chkinv;
	private int timeinv;
	private boolean chkdoll;

	private boolean Wanted;

	private boolean autostun;
	private boolean autotryple;

	public List<ItemInstance> shop_temp_list;

	private boolean maeip_check;

	// 아데나 매입게시판 변수
	// 계좌 정보
	public String pay_info1;
	public String pay_info2;
	public String pay_info3;

	private long cancelTime;
	private boolean append;

	private object tempShop;

	// 유저아데나 관리목록.
	public List<String> adena_list;
	// 어드민 매입 관리목록.
	public List<String> adena_list2;

	private Itemtrade itemTrade;
	// 커멘드 혹은 다른 경로로 보드를 볼 시에 확인할 변수
	private int board;
	// 계정 UID
	private int accountUid;

	private boolean isPass2th;

	private String pass2th;

	private boolean perpecten;

	private long jahillTime;

	@Override
	public void close() {
		super.close();
		luck = aden = 0;
		auto_pickup = is_hpbar = auto = chkspeed = clanexp = chkpoly = royalbuff = chkinv = el_ok = stat_clear = chkdoll = Wanted = autotryple = isBaphomet = autostun = maeip_check = isBattlezone = false;
		chattingWhisper = chattingGlobal = chattingTrade = auto_pickup_message = auto_party_message = true;
		lost_exp = premium_item_time = register_date = join_date = auto_save_time = message_time = PkTime = partyid = AttackFrameTime = WalkFrameTime = attribute = SpeedhackWarningCounting = PkCount = gm = MonsterKillCount = PkHellTime = PkHellCount = pevent = 0;
		click_delay = click_armor_delay = click_weapon_delay = worldout_time = timeinv = 0;
		autoHuntMonsterCount = baphometLevel = 0;
		attackCnt = 0;
		PandantBuff_Level = 0;
		giran_dungeon_time = 0;
		giran_dungeon_count = 0;
		dayptime = daycount = dayptime = checkmenttime = 0;
		karma = 0D;
		inv = null;
		npc_esmereld = null;
		db_interface = null;
		attack_target = null;
		if (listBlockName != null)
			listBlockName.clear();
		if (levelqstlist != null)
			levelqstlist.clear();
		_repeatqst = null;

		if (shop_temp_list != null)
			clearTempList();

		pay_info1 = pay_info2 = pay_info3 = null;

		if (adena_list != null)
			clearAdenaList();
		if (adena_list2 != null)
			clearAdenaList2();

		deadtrue = false;
		ment = false;
		heal_1 = heal_2 = heal_3 = heal_4 = false;
		isPandantBuff = false;

		if (marketPrice != null)
			marketPrice.clear();
		
		open_wait_item_time = 0;
		accountUid = tempClanId = tempClanGrade = 0;
		board = 0;
		checkmenttime = 0;
		autoHuntAnswerTime = 0L;
		turbernDelay = 0;
		tempName = tempClanName = tempTitle = autoHuntAnswer = null;

		perpecten = false;

		if (swap != null)
			swap.clear();
		swapIdx = null;
		selectSwap = null;
		isInsertSwap = false;
		isInsert_autoAnswer = false;
		magicDoll = null;
		magicDollinstance = null;
		itemTrade = null;
		huntClose();
		MotionClose();
		PickItemClose();
		isShapeChange = swardLack = is_Gaho = is_Gaho_Buff = false;
		enfail = 0;
		questchapter = questkill = randomquest = randomquestkill = randomquestfinish = randomquestplay = 0;
		lastLackTime = 0L;
		adenChange = false;
		isPass2th = false;
		pass2th = null;
		tempShop = null;
		daycount = 0;
		daycheck = 0;
		jahillTime = 0;
	}

	public String getPass2th() {
		return pass2th;
	}

	public void setPass2th(String pass2th) {
		this.pass2th = pass2th;
	}

	public boolean isPass2th() {
		return isPass2th;
	}

	public void setPass2th(boolean isPass2th) {
		this.isPass2th = isPass2th;
	}

	public boolean isMent() {
		return ment;
	}

	public void setMent(boolean is) {
		ment = is;
	}
	
	@Override
	public int getGm() {
		return gm;
	}

	@Override
	public void setGm(int gm) {
		this.gm = gm;
	}

	public List<String> getListBlockName() {
		return listBlockName;
	}

	public int getPkHellCount() {
		return PkHellCount;
	}

	public void setPkHellCount(int pkHellCount) {
		PkHellCount = pkHellCount;
	}
	
	public int getPkCount() {
		return PkCount;
	}

	public void setPkCount(int pkCount) {
		PkCount = pkCount;
	}

	@Override
	public long getPkTime() {
		return PkTime;
	}

	public void setPkTime(long pkTime) {
		PkTime = pkTime;
	}

	public int getPkHellTime() {
		return PkHellTime;
	}

	public void setPkHellTime(int pkHellTime) {
		PkHellTime = pkHellTime;
	}

	public double getlevelexp() {
		return Levelexpname;
	}

	public void setlevelexp(double levelexpname) {
		Levelexpname = levelexpname;
	}

	public int getDayptime() {
		return dayptime;
	}

	public void setDayptime(int dayptime) {
		this.dayptime = dayptime;
	}

	public int getDaycount() {
		return daycount;
	}

	public void setDaycount(int daycount) {
		this.daycount = daycount;
	}

	public int getDaycheck() {
		return daycheck;
	}

	public void setDaycheck(int daycheck) {
		this.daycheck = daycheck;
	}

	@Override
	public long getPartyId() {
		return partyid;
	}

	@Override
	public void setPartyId(long partyid) {
		this.partyid = partyid;
	}

	public LineageClient getClient() {
		return client;
	}

	public void setClient(LineageClient lc) {
		client = lc;
	}

	@Override
	public Inventory getInventory() {
		return inv;
	}

	@Override
	public void setInventory(Inventory inv) {
		this.inv = inv;
	}

	public boolean isChattingTrade() {
		return chattingTrade;
	}

	public void setChattingTrade(boolean chattingTrade) {
		this.chattingTrade = chattingTrade;
	}

	public boolean isChattingWhisper() {
		return chattingWhisper;
	}

	public void setChattingWhisper(boolean chattingWhisper) {
		this.chattingWhisper = chattingWhisper;
	}

	public boolean isAutoPotionMent() {
		return autoPotionMent;
	}

	public void setAutoPotionMent(boolean autoPotionMent) {
		this.autoPotionMent = autoPotionMent;
	}

	public boolean isChattingGlobal() {
		return chattingGlobal;
	}

	public void setChattingGlobal(boolean chattingGlobal) {
		this.chattingGlobal = chattingGlobal;
	}

	@Override
	public int getAttribute() {
		return attribute;
	}

	public void setAttribute(final int attribute) {
		this.attribute = attribute;
	}

	public void setNpcEsmereld(Esmereld esmereld) {
		npc_esmereld = esmereld;
	}

	public Esmereld getNpcEsmereld() {
		return npc_esmereld;
	}

	public boolean isHeal_1() {
		return heal_1;
	}

	public void setHeal_1(boolean heal_1) {
		this.heal_1 = heal_1;
	}

	public boolean isHeal_2() {
		return heal_2;
	}

	public void setHeal_2(boolean heal_2) {
		this.heal_2 = heal_2;
	}

	public boolean isHeal_3() {
		return heal_3;
	}

	public void setHeal_3(boolean heal_3) {
		this.heal_3 = heal_3;
	}

	public boolean isHeal_4() {
		return heal_4;
	}

	public void setHeal_4(boolean heal_4) {
		this.heal_4 = heal_4;
	}

	public boolean isMaeip_check() {
		return maeip_check;
	}

	public void setMaeip_check(boolean maeip_check) {
		this.maeip_check = maeip_check;
	}

	public List<String> getAdena_list2() {
		return adena_list2;
	}

	public void setAdena_list2(List<String> adena_list2) {
		this.adena_list2 = adena_list2;
	}

	public List<String> getAdena_list() {
		synchronized (adena_list) {
			return adena_list;
		}
	}

	public void setAdena_list(List<String> adena_list) {
		synchronized (adena_list) {
			this.adena_list = adena_list;
		}
	}

	// 아데나 게시판 리스트
	public void clearAdenaList() {
		synchronized (adena_list) {
			adena_list.clear();
		}
	}

	// 매입 게시판 리스트
	public void clearAdenaList2() {
		synchronized (adena_list2) {
			adena_list2.clear();
		}
	}

	public void isInsertItem(long time) {
		if (time <= getCancelTime() && getCancelTime() > 0) {
			// java.lang.System.out.println(getCancelTime());
			setAppend(false);
			return;
		}
		setCancelTime(0);
		setAppend(true);
		return;
	}

	public long getJahillTime() {
		return jahillTime;
	}

	public void setJahillTime(long jahillTime) {
		this.jahillTime = jahillTime;
	}

	public long getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(long cancelTime) {
		this.cancelTime = cancelTime;
	}

	public boolean isAppend() {
		return append;
	}

	public void setAppend(boolean append) {
		this.append = append;
	}

	public String getPay_info1() {
		return pay_info1;
	}

	public void setPay_info1(String pay_info1) {
		this.pay_info1 = pay_info1;
	}

	public String getPay_info2() {
		return pay_info2;
	}

	public void setPay_info2(String pay_info2) {
		this.pay_info2 = pay_info2;
	}

	public String getPay_info3() {
		return pay_info3;
	}

	public void setPay_info3(String pay_info3) {
		this.pay_info3 = pay_info3;
	}

	public void clearTempList() {
		synchronized (shop_temp_list) {
			shop_temp_list.clear();
		}
	}

	public List<ItemInstance> getShop_temp_list() {
		synchronized (shop_temp_list) {
			return shop_temp_list;
		}
	}

	public void setShop_temp_list(List<ItemInstance> shop_temp_list) {
		synchronized (shop_temp_list) {
			this.shop_temp_list = shop_temp_list;
		}
	}

	@Override
	public double getKarma() {
		return karma;
	}

	public void setKarma(double karma) {
		if (karma >= Long.MAX_VALUE)
			karma = Long.MAX_VALUE;
		this.karma = karma;
	}

	public boolean getChkspeed() {
		return chkspeed;
	}

	public void setChkspeed(boolean chkspeed) {
		this.chkspeed = chkspeed;
	}

	public boolean getChkPoly() {
		return chkpoly;
	}

	public void setChkPoly(boolean chkpoly) {
		this.chkpoly = chkpoly;
	}

	public boolean getClanexp() {
		return clanexp;
	}

	public void setClanexp(boolean clanexpp) {
		this.clanexp = clanexpp;
	}

	public boolean getRoyalBuff() {
		return royalbuff;
	}

	public void setRoyalBuff(boolean royalbuff) {
		this.royalbuff = royalbuff;
	}

	public boolean getChkInv() {
		return chkinv;
	}

	public void setChkInv(boolean chkinv) {
		this.chkinv = chkinv;
	}

	public int getTimeInv() {
		return timeinv;
	}

	public void setTimeInv(int timeinv) {
		this.timeinv = timeinv;
	}

	public boolean getChkDoll() {
		return chkdoll;
	}

	public void setChkDoll(boolean chkdoll) {
		this.chkdoll = chkdoll;
	}

	public boolean getWanted() {
		return Wanted;
	}

	public void setWanted(boolean wanted) {
		this.Wanted = wanted;
	}

	public int getDeathCount() {
		return DeathCount;
	}

	public void setDeathCount(int paramInt) {
		this.DeathCount = paramInt;
	}

	public boolean getAutoStun() {
		return autostun;
	}

	public void setAutoStun(boolean autostun) {
		this.autostun = autostun;
	}

	public boolean getAutoTryple() {
		return autotryple;
	}

	public void setAutoTryple(boolean autotryple) {
		this.autotryple = autotryple;
	}

	public boolean isperpecten() {
		return perpecten;
	}

	public void setperpecten(boolean perpecten) {
		this.perpecten = perpecten;
	}

	public int getEnfail() {
		return enfail;
	}

	public void setEnfail(int enfail) {
		this.enfail = enfail;
	}

	// 몬스터 퀘스트
	public int getQuestChapter() {
		return questchapter;
	}

	public void setQuestChapter(int questchapter) {
		this.questchapter = questchapter;
	}

	public int getQuestKill() {
		return questkill;
	}

	public void setQuestKill(int questkill) {
		this.questkill = questkill;
	}

	public int getRadomQuest() {
		return randomquest;
	}

	public void setRadomQuest(int randomquest) {
		this.randomquest = randomquest;
	}

	public int getRandomQuestkill() {
		return randomquestkill;
	}

	public void setRandomQuestkill(int randomquestkill) {
		this.randomquestkill = randomquestkill;
	}

	public int getRandomQuestCount() {
		return randomquestfinish;
	}

	public void setRandomQuestCount(int randomquestfinish) {
		this.randomquestfinish = randomquestfinish;
	}

	public int getRandomQuestPlay() {
		return randomquestplay;
	}

	public void setRandomQuestPlay(int randomquestplay) {
		this.randomquestplay = randomquestplay;
	}
	
	/**
	 * 우호도가 야히인지 발록인지 확인해줌. <br/>
	 * : -1(음수)는 야히 <br/>
	 * : +1(양수)는 발록 <br/>
	 * : 0는 어디에도 속해잇지 않음 <br/>
	 */
	public int isKarmaType() {
		return karma == 0 ? 0 : (karma < 0 ? -1 : 1);
	}

	public int getKarmaLevel() {
		/*
		 * 파편 먹일경우 개당 100점을 얻습니다. 우호도를 깎을경우, 파편을 먹이면 개당 100점이 깎입니다.
		 * 
		 * 사냥을 통해 우호도를 깎을경우 올릴때 점수의 3배가 깎입니다.
		 */
		long temp = (long) karma;
		if (temp < 0)
			temp = (~temp) + 1;
		if (temp < 10000D)
			return 0;
		else if (temp < 20000D)
			return 1;
		else if (temp < 100000D)
			return 2;
		else if (temp < 500000D)
			return 3;
		else if (temp < 1500000D)
			return 4;
		else if (temp < 3500000D)
			return 5;
		else if (temp < 5500000D)
			return 6;
		else if (temp < 10500000D)
			return 7;
		else
			return 8;
	}

	public object getAttackTarget() {
		return attack_target;
	}

	public void setAttackTarget(object attack_target) {
		this.attack_target = attack_target;
	}

	@Override
	public int getMonsterKillCount() {
		return MonsterKillCount;
	}

	@Override
	public void setMonsterKillCount(int monsterKillCount) {
		MonsterKillCount = monsterKillCount;
	}

	public long getRegisterDate() {
		return register_date;
	}

	public void setRegisterDate(long register_date) {
		this.register_date = register_date;
	}

	public long getJoinDate() {
		return join_date;
	}

	public void setJoinDate(long join_date) {
		this.join_date = join_date;
	}

	public double getLostExp() {
		return lost_exp;
	}

	public void setLostExp(double lost_exp) {
		this.lost_exp = lost_exp;
	}
	
	public int getBaphometLevel() {
		return baphometLevel;
	}

	public void setBaphometLevel(int baphometLevel) {
		this.baphometLevel = baphometLevel;
	}

	public boolean isBaphomet() {
		return isBaphomet;
	}

	public void setBaphomet(boolean isBaphomet) {
		this.isBaphomet = isBaphomet;
	}

	public int getGiran_dungeon_time() {
		return giran_dungeon_time;
	}

	public void setGiran_dungeon_time(int giran_dungeon_time) {
		this.giran_dungeon_time = giran_dungeon_time;
	}
	
	public int getGiran_dungeon_count() {
		return giran_dungeon_count;
	}

	public void setGiran_dungeon_count(int giran_dungeon_count) {
		this.giran_dungeon_count = giran_dungeon_count;
	}

	@Override
	public void setHeading(int heading) {
		super.setHeading(heading);
		if (!worldDelete && !(this instanceof PcRobotInstance))
			toSender(S_ObjectHeading.clone(
					BasePacketPooling.getPool(S_ObjectHeading.class), this),
					false);
	}

	@Override
	public void setLawful(int lawful) {
		super.setLawful(lawful);
		if (!worldDelete)
			toSender(S_ObjectLawful.clone(
					BasePacketPooling.getPool(S_ObjectLawful.class), this),
					true);
	}

	@Override
	public void setNowHp(int nowhp) {
		// 텔레포트락인 상태일경우 무시.
		if (isBuffTeleport())
			return;
		//
		super.setNowHp(nowhp);
		if (!worldDelete) {
			toSender(S_CharacterHp.clone(
					BasePacketPooling.getPool(S_CharacterHp.class), this));
			PartyController.toUpdate(this);
		}

		if (GameSetting.유저피바) {
			PcInstance gm = World.findPc("카시오페아");
			if (gm != null) {
				gm.toSender(S_ObjectHitratio.clone(
						BasePacketPooling.getPool(S_ObjectHitratio.class),
						this, true));
			}
		} else if (!GameSetting.유저피바) {
			PcInstance gm = World.findPc("카시오페아");
			if (gm != null) {
				gm.toSender(S_ObjectHitratio.clone(
						BasePacketPooling.getPool(S_ObjectHitratio.class),
						this, false));
			}
		}
	}

	@Override
	public void setNowMp(int nowmp) {
		super.setNowMp(nowmp);
		if (!worldDelete)
			toSender(S_CharacterMp.clone(
					BasePacketPooling.getPool(S_CharacterMp.class), this));
	}

	@Override
	public void setLight(int light) {
		super.setLight(light);
		if (!worldDelete)
			toSender(S_ObjectLight.clone(
					BasePacketPooling.getPool(S_ObjectLight.class), this), true);
	}

	@Override
	public void setInvis(boolean invis) {
		super.setInvis(invis);
		if (!worldDelete)
			toSender(S_ObjectInvis.clone(
					BasePacketPooling.getPool(S_ObjectInvis.class), this), true);

		if (getMagicDollinstance() != null)
			getMagicDollinstance().setInvis(invis);
	}

	@Override
	public void setFood(int food) {
		super.setFood(food);
		if (!worldDelete)
			toSender(S_CharacterStat.clone(
					BasePacketPooling.getPool(S_CharacterStat.class), this));
	}

	@Override
	public void setDynamicDg(int dg) {
		super.setDynamicDg(dg);
		if (!worldDelete && Lineage.server_version >= 360)
			toSender(S_CharacterDg.clone(
					BasePacketPooling.getPool(S_CharacterDg.class), false,
					getTotalDg()));
	}

	@Override
	public void toDamage(Character cha, int dmg, int type, Object... opt) {
		// 버그 방지 및 자기자신이 공격햇을경우 무시.
		if (cha == null || cha.getObjectId() == getObjectId() || dmg <= 0
				|| getInventory() == null)
			return;

		// 자힐 pk시해제
		// checkAutohillPvP(cha);
		// 자동물약 pk시해제
		// checkAutoPotionPvP(cha);
		// 자동칼질 pk시해제
		// cancelAutoAttack(cha);
		
		// 방어구에 공격당한거 알리기.
		ItemInstance armor = getInventory().getSlot(2);
		if (armor != null)
			armor.toDamage(cha, dmg, type, opt);

		// 소환객체에게 알리기.
		SummonController.toDamage(this, cha, dmg);

		// 공격자가 사용자일때 구간.
		if (cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha;
			if (pc.getChkspeed() == true)
				pc.setChkspeed(false);

			if (isAutoHunt()) {
				ItemInstance i = getInventory().find("순간이동 주문서");
				if (i != null) {
					i.toClick(this, null);
				}
			}

			// 보라도리로 변경하기.
			if (Lineage.server_version > 160
					&& World.isNormalZone(getX(), getY(), getMap()))
				// switch(type) {
				// case 0:
				// }
				// ChattingClosetwo.init(cha, Lineage.ChatTime);
				Criminal.init(cha, this);
			// 경비병에게 도움요청.
			DamageController.toGuardHelper(this, cha);
		}

	}

	@Override
	public void setExp(double exp) {
		if (isDead() && deadtrue) {
			// 경험치 하향시키려 할때만.
			if (getExp() > exp)
				super.setExp(exp);
			return;
		}

		Exp max = ExpDatabase.find(Lineage.level_max);
		if (max != null && exp > 0 && level <= max.getLevel()) {
			Exp e = ExpDatabase.find(level);
			if (max.getBonus() > exp) {
				super.setExp(exp);
			} else {

				super.setExp(max.getBonus() - 1);
			}
			if (e != null) {
				boolean lvUp = e.getBonus() <= getExp();
				if (lvUp) {
					if (PluginController.init(PcInstance.class,
							"toLevelupSetting", this, e) == null) {
						// 현재 경험치만큼에 해당하는 레벨 정보 추출.
						for (int i = 1; i <= Lineage.level_max; i++) {
							e = ExpDatabase.find(i);
							if (getExp() < e.getBonus())
								break;
						}
						// hp, mp 상승값 추출.
						int hp = CharacterController.toStatusUP(this, true);
						int mp = CharacterController.toStatusUP(this, false);
						for (int i = e.getLevel() - level; i > 1; i--) {
							hp += CharacterController.toStatusUP(this, true);
							mp += CharacterController.toStatusUP(this, false);
						}
						int new_hp = getMaxHp() + hp;
						int new_mp = getMaxMp() + mp;

						switch (classType) {
						case 0x00:
							if (new_hp >= Lineage.royal_max_hp
									&& Lineage.royal_max_hp > 0)
								new_hp = Lineage.royal_max_hp;
							if (new_mp >= Lineage.royal_max_mp
									&& Lineage.royal_max_mp > 0)
								new_mp = Lineage.royal_max_mp;
							break;
						case 0x01:
							if (new_hp >= Lineage.knight_max_hp
									&& Lineage.knight_max_hp > 0)
								new_hp = Lineage.knight_max_hp;
							if (new_mp >= Lineage.knight_max_mp
									&& Lineage.knight_max_mp > 0)
								new_mp = Lineage.knight_max_mp;
							break;
						case 0x02:
							if (new_hp >= Lineage.elf_max_hp
									&& Lineage.elf_max_hp > 0)
								new_hp = Lineage.elf_max_hp;
							if (new_mp >= Lineage.elf_max_mp
									&& Lineage.elf_max_mp > 0)
								new_mp = Lineage.elf_max_mp;
							break;
						case 0x03:
							if (new_hp >= Lineage.wizard_max_hp
									&& Lineage.wizard_max_hp > 0)
								new_hp = Lineage.wizard_max_hp;
							if (new_mp >= Lineage.wizard_max_mp
									&& Lineage.wizard_max_mp > 0)
								new_mp = Lineage.wizard_max_mp;
							break;
						case 0x04:
							if (new_hp >= Lineage.darkelf_max_hp
									&& Lineage.darkelf_max_hp > 0)
								new_hp = Lineage.darkelf_max_hp;
							if (new_mp >= Lineage.darkelf_max_mp
									&& Lineage.darkelf_max_mp > 0)
								new_mp = Lineage.darkelf_max_mp;
							break;
						case 0x05:
							if (new_hp >= Lineage.dragonknight_max_hp
									&& Lineage.dragonknight_max_hp > 0)
								new_hp = Lineage.dragonknight_max_hp;
							if (new_mp >= Lineage.dragonknight_max_mp
									&& Lineage.dragonknight_max_mp > 0)
								new_mp = Lineage.dragonknight_max_mp;
							break;
						case 0x06:
							if (new_hp >= Lineage.blackwizard_max_hp
									&& Lineage.blackwizard_max_hp > 0)
								new_hp = Lineage.blackwizard_max_hp;
							if (new_mp >= Lineage.blackwizard_max_mp
									&& Lineage.blackwizard_max_mp > 0)
								new_mp = Lineage.blackwizard_max_mp;
							break;
						}

						setMaxHp(new_hp);
						setMaxMp(new_mp);
						setNowHp(getNowHp() + hp);
						setNowMp(getNowMp() + mp);
						// 레벨업시 자동만피 기능
						// setNowHp(getMaxHp() + hp + getDynamicHp());
						// setNowMp(getMaxMp() + mp + getDynamicMp());
						setLevel(e.getLevel());
						toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, 2127));
					}

					QuestPresentDatabase.checkLevelCompensation(this);
					// 레벨 51이상시 스탯보너스 부분
					if (level > 50)
						toLvStat(true);
					// if (Lineage.is_quest_present)
					// QuestPresentDatabase.checkLevelCompensation(this);
					// if (Lineage.is_level_quest)
					// QuestDatabase.getInstance().store_pcLevelList(this);
					// 레벨 보상 선물
					//
					toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
					PluginController.init(PcInstance.class, "toLevelup", this);
				} else {
					// 1.82에서 S_CharacterExp이용해 exp만 전송하니 그래프바가 오작동하는 버그가 발생.
					// 그래서 스탯과 경험치등 정보가 포함된걸로 변경함.
					// toSender(S_CharacterExp.clone(BasePacketPooling.getPool(S_CharacterExp.class),
					// this));
					toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				}
			}
		}
	}

	@Override
	public void toDead(Character cha) {
		//
		if (auto == true) {
			auto = false;
		}
		ClanController.toDead(this);
		MagicDollController.toDead(this);

		// 공성존 인지 확인.
		Kingdom kingdom = KingdomController.findKingdomLocation(this);
		// 성존이고 공성중일경우 죽어도 수배 안빠지도록
		if (kingdom != null && kingdom.isWar()) {

		} else {
			WantedController.toDead(cha, this);
		}

	}

	private boolean mm = true;
	private boolean mmm = true;

	/**
	 * 50이상 레벨 보너스스탯 지급해야 하는지, 체크용 메서드
	 */
	public boolean toLvStat(boolean packet) {
		//
		Object o = PluginController.init(PcInstance.class, "toLvStat", this,
				packet);
		if (o != null && o instanceof Boolean)
			return (Boolean) o;
		//
		if (Lineage.server_version < 163)
			return false;
		// 레벨 스탯 확인.
		int lvStat = level - 50;
		if (lvStat > 0) {
			if (getLvStat() < lvStat) {
				if (packet) {
					toSender(S_Html.clone(
							BasePacketPooling.getPool(S_Html.class), this,
							"RaiseAttr"));
					mm = false;
					ChattingController.toChatting(this, "\\fT 현재 스텟  ", 20);
					ChattingController
							.toChatting(
									this,
									String.format(
											"\\fT Str[%d] Dex[%d] Con[%d] Int[%d] Cha[%d] Wis[%d]",
											this.getStr() + this.getLvStr(),
											this.getDex() + this.getLvDex(),
											this.getCon() + this.getLvCon(),
											this.getInt() + this.getLvInt(),
											this.getCha() + this.getLvCha(),
											this.getWis() + this.getLvWis()),
									20);
				}
				if (mm == false && mmm == true) {

					mm = true;
					mmm = false;
				}
				return true;
			}
			mmm = true;
			return false;
		}
		// 엘릭서 스탯 확인.
		if (getElixirStat() < getElixirReset()) {
			if (packet) {
				toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class),
						this, "RaiseAttr"));
				mm = false;
				ChattingController.toChatting(this,
						"\\fT 현재 스텟  :  만 스텟은 30 입니다.", 20);
				ChattingController.toChatting(this, String.format(
						"\\fT Str[%d] Dex[%d] Con[%d] Int[%d] Cha[%d] Wis[%d]",
						this.getStr() + this.getLvStr() + this.getElixirStr(),
						this.getDex() + this.getLvDex() + this.getElixirDex(),
						this.getCon() + this.getLvCon() + this.getElixirCon(),
						this.getInt() + this.getLvInt() + this.getElixirInt(),
						this.getCha() + this.getLvCha() + this.getElixirCha(),
						this.getWis() + this.getLvWis() + this.getElixirWis()),
						20);
			}
			if (mm == false && mmm == true) {

				mm = true;
				mmm = false;
			}
			return true;
		}
		mmm = true;
		return false;
	}

	// 엘릭서
	public boolean toLvStatElixir(boolean packet) {
		if (getElixirStat() < Lineage.item_elixir_max) {
			if (packet)
				toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class),
						this, "RaiseAttr"));
			return true;
		}
		return false;
	}

	/**
	 * 베이스 스탯이 75를 충족하지 못할경우 스탯찍는창을 띄움.
	 * 
	 * @param packet
	 * @return
	 */
	public boolean toBaseStat(boolean packet) {
		//
		if (Lineage.server_version < 163)
			return false;
		// 베이스 스탯 확인.
		int total = getStr() + getDex() + getCon() + getInt() + getCha()
				+ getWis();
		if (total < 75) {
			if (packet)
				toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class),
						this, "RaiseAttr"));
			return true;
		}
		//
		return false;
	}

	/**
	 * gui에서 사용중
	 * 
	 * @param
	 * @return
	 */
	public void toCharacterSave() {
		Connection con = null;
		try {
			con = DatabaseConnection.getLineage();
			toSave(con);
		} catch (Exception e) {
			lineage.share.System.println(PcInstance.class.toString() + " : toSave()");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con);
		}
	}
	
	/**
	 * 사용자 정보 저장 처리 함수.
	 */
	private void toSave() {
		Connection con = null;
		try {
			con = DatabaseConnection.getLineage();
			toSave(con);
		} catch (Exception e) {
			lineage.share.System.println(PcInstance.class.toString()
					+ " : toSave()");
			lineage.share.System.println(e);
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con);
		}
	}

	/**
	 * 사용자 정보 저장 처리 함수.
	 */
	public void toSave(Connection con) {
		// 저장
		try {
			CharactersDatabase.saveDungeon(con, this);
			CharactersDatabase.saveInventory(con, this);
			CharactersDatabase.saveSkill(con, this);
			CharactersDatabase.saveBuff(con, this);
			CharactersDatabase.saveBook(con, this);
			CharactersDatabase.saveCharacter(con, this);
			CharactersDatabase.insertQuest(con, this);
			CharactersDatabase.savePcTrade(con, getCancelTime(),getAccountUid());
			SummonController.toSave(con, this);
			KingdomController.toSave(con);
			무인혈맹컨트롤러.save(con);
			ClanController.saveClan(con);
			FriendController.toSave(con, this);
			AutoHuntDatabase.saveHunt(this);
			AutoHuntDatabase.saveSellList(this, sell_List);
			CharactersDatabase.saveStatClear(con, this);
			saveSwap(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 월드 진입할때 호출됨.
	 */
	public void toWorldJoin() {
			// 버그 방지.
			if (!isWorldDelete())
	            return;

	        if (getGfxMode() == 70 || getGfxMode() == 71) {
	            setGfxMode(0);
	        }
	    

	    // db_interface가 null이 아니거나 (또는 null인데도 서버 버전이 380 이상인 경우) 인터페이스 전송
	    if (db_interface != null || (db_interface == null && Lineage.server_version >= 380)) {
	        toSender(S_InterfaceRead.clone(BasePacketPooling.getPool(S_InterfaceRead.class), db_interface));
	    }
		// er 표현.
		if (Lineage.server_version >= 380) {
			toSender(S_CharacterEr.clone(
					BasePacketPooling.getPool(S_CharacterEr.class), 0x02));
			toSender(S_CharacterEr.clone(
					BasePacketPooling.getPool(S_CharacterEr.class), 0x02));
		}
		// 메모리 세팅
		setAutoPickup(Lineage.auto_pickup);

		World.appendPc(this);
		BookController.toWorldJoin(this);
		InventoryController.toWorldJoin(this);
		SkillController.toWorldJoin(this);
		CharacterController.toWorldJoin(this);
		TradeController.toWorldJoin(this);
		BuffController.toWorldJoin(this);
		SummonController.toWorldJoin(this);
		QuestController.toWorldJoin(this);
		ChattingController.toWorldJoin(this);
		MagicDollController.toWorldJoin(this);
		DungeonController.toWorldJoin(this);
		// 던전 정보 추출.
		CharactersDatabase.readDungeon(this);
		// 인벤토리 추출 및 전송
		CharactersDatabase.readInventory(this);

		if (Lineage.server_version >= 380) {
			toSender(S_Test.clone(BasePacketPooling.getPool(S_Test.class), 1));
			toSender(S_Test.clone(BasePacketPooling.getPool(S_Test.class), 2));
			toSender(S_Test.clone(BasePacketPooling.getPool(S_Test.class), 3));
			toSender(S_InventorySlot.clone(BasePacketPooling.getPool(S_InventorySlot.class), this));
			toSender(S_Test.clone(BasePacketPooling.getPool(S_Test.class), 4));
			toSender(S_Test.clone(BasePacketPooling.getPool(S_Test.class), 5));
			toSender(S_Test.clone(BasePacketPooling.getPool(S_Test.class), 6));
		}
		// 기억리스트 추출 및 전송
		CharactersDatabase.readBook(this);
		// 스킬 추출 및 전송
		CharactersDatabase.readSkill(this);
		// 게시판취소시간 전송
		CharactersDatabase.readPcTrade(client, this);
		// 스탯초기화 여부 전송
		CharactersDatabase.readStatClear(this);
		AccountDatabase.getenfail(this, this.getClient().getAccountUid());
		// 케릭터 정보 전송
		toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
		// 월드 스폰.
		super.toTeleport(x, y, map, false);
		AutoHuntCheckController.toWorldJoin(this);
		// sp, mr 갱신.
		toSender(S_CharacterSpMr.clone(
				BasePacketPooling.getPool(S_CharacterSpMr.class), this));
		// DG 표현.
		if (Lineage.server_version >= 360)
			toSender(S_CharacterDg.clone(
					BasePacketPooling.getPool(S_CharacterDg.class), true,
					getTotalDg()));
		// 버프 잡아주기
		CharactersDatabase.readBuff(this);
		// 맵핵 자동켜짐
		toSender(S_Ability.clone(BasePacketPooling.getPool(S_Ability.class), 3, true));
		// 날씨 보내기
		toSender(S_Weather.clone(BasePacketPooling.getPool(S_Weather.class), 0));
		// 혈전 처리.
		ClanController.toWorldJoin(this);
		// 피바 처리
		setHpbar(true);
		toSender(S_ObjectHitratio.clone(BasePacketPooling.getPool(S_ObjectHitratio.class), this, isHpbar()));
		// 자동사냥 정보 리스트 정보처리
		AutoHuntDatabase.readHunt(this);
		// 자동사냥 아이템 리스트 정보처리
		AutoHuntDatabase.readSellList(this);
		// 성처리
		KingdomController.toWorldJoin(this);

		if (Lineage.server_version >= 360)
			toSender(S_BaseStat.clone(
					BasePacketPooling.getPool(S_BaseStat.class), this));
		// 우호도 표기
		if (Lineage.server_version >= 340)
			toSender(S_CharacterKarma.clone(
					BasePacketPooling.getPool(S_CharacterKarma.class), 0));
		// 스탯 확인
		toLvStat(true);
		// 편지 확인
		LetterController.toWorldJoin(this);
		// 친구 목록 갱신.
		FriendController.toWorldJoin(this);

		toClanLevelBuff(this);

		// %0님께서 방금 게임에 접속하셨습니다.
		if (Lineage.world_message_join)
			World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, 20, String.format("%s님께서 게임에 접속하셨습니다.", getName())));

		// 오픈 대기 중일때 메세지 전송
		if (Lineage.open_wait)
			ChattingController.toChatting(this, String.format("%s [오픈 대기중] 입니다.", ServerDatabase.getName()), Lineage.CHATTING_MODE_MESSAGE);
				
		// 초보존일경우 알림
		if (getMap() == 69 || getMap() == 86) {
			if (getLevel() < 30) {
				if (getMap() == 69)
					ChattingController.toChatting(this, "숨겨진 계곡에서는 30레벨 까지만 이용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				else
					ChattingController.toChatting(this, "숨겨진 계곡 던전에서는 30레벨 까지만 이용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		}

		// 버그확인 - 변신버프중이 아닌데 순수gfx와 다르다면 변신해제처리 요청.
		if (BuffController.find(this).find(ShapeChange.class) == null && getGfx() != getClassGfx())
			ShapeChange.init(this, 1);
				
		PluginController.init(PcInstance.class, "toWorldJoin", this);
		// 30렙 이하 서버정보 창 띄우기
		if (getLevel() < 30)
		toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "help"));
		// 로그 기록.
		if (client != null && Log.isLog(this)) {
	        Log.appendConnect(getRegisterDate(), client.getAccountIp(), client.getAccountId(), getName(), "접속");
	    }
		
		WantedController.toWorldJoin(this);
		toMeticeMessage(this);

		if (Lineage.server_version >= 380) {

			toSender(S_Test.clone(BasePacketPooling.getPool(S_Test.class), 11));
			toSender(S_Test.clone(BasePacketPooling.getPool(S_Test.class), 13));
			toSender(S_Test.clone(BasePacketPooling.getPool(S_Test.class), 14));
			toSender(S_Test.clone(BasePacketPooling.getPool(S_Test.class), 16));

			// 경험치가 손실되지 않습니다 버프
			toSender(S_Test.clone(BasePacketPooling.getPool(S_Test.class), 18));
		}
		// 장비 스왑 데이터 로드.
		readSwap();
		// 레벨 퀘스트 정보 로드
		if (Lineage.is_level_quest)
			QuestDatabase.getInstance().WorldJoin_Load_PcLevelList(this);
		// 반복 퀘스트 정보 로드
		if (Lineage.is_repeat_quest)
			QuestDatabase.getInstance().WorldJoin_Load_PcRepeatList(this);

		setAccountUid(this.getClient().getAccountUid());

	}

	/**
	 * 월드에서 나갈때 호출됨.
	 */
	public void toWorldOut() {
			// 버그 방지.
			if (isWorldDelete()) 
	            return;
	        
			// 낚시중에 종료시 낚싯대 장착 해제
			if (isFishing()) {
				getInventory().getSlot(Lineage.SLOT_WEAPON).toClick(this, null);
			}
			
		// 월드에 케릭터를 유지해야되는 상황이라면
		if ((Lineage.player_worldout_time > 0 && getClient() != null && (World
				.isNormalZone(getX(), getY(), getMap()) || World.isCombatZone(
				getX(), getY(), getMap())))) {
			getClient().setPc(new PcInstance(getClient()));
			setClient(null);
			return;
		}
		if (isAutoHunt()) {
			AutoHuntThread.getInstance().removeAuto(this);
		}
		PluginController.init(PcInstance.class, "toWorldOut", this);
		// 로그 기록.
		if (Log.isLog(this) && client != null)
			Log.appendConnect(getRegisterDate(), client.getAccountIp(),
					client.getAccountId(), getName(), "종료");
		// 사용자 정보 저장전에 처리해야될 메모리 처리
		TradeController.toWorldOut(this); // 거래중인 아이템 때문에 저장전에 처리.
		UserShopController.toStop(this); // gfxmode 때문에 선 처리.
		GhostHouseController.toWorldOut(this); // 버프때문에 선 처리.
		BaseStatController.toWorldOut(this); // 좌표때문에 선 처리.
		BuffController.toWorldOut(this, false); // 객체 저장하기전 뒷처리할게 있을 수 있기
		// 죽어있을경우에 처리를 위해.
		toReset(true);
		// 에스메랄다 미래보기 상태라면
		if (npc_esmereld != null)
			npc_esmereld.toTeleport(this);
		// 근처 마을로 좌표 변경.
		if (TeleportResetDatabase.toLocation(this) && getGm() == 0) {
			x = homeX;
			y = homeY;
			map = homeMap;
		}
		// 성주면에 있는지 확인. 있을경우 근처마을로 좌표 변경.
		Kingdom k = KingdomController.findKingdomLocation(this);
		if (k != null && k.getClanId() > 0 && k.getClanId() != getClanId()) {
			TeleportHomeDatabase.toLocation(this);
			x = homeX;
			y = homeY;
			map = homeMap;
		}
		// 사용자 정보 저장
		toSave();
		savePet();
		// 월드에 갱신
		World.remove(this);
		CharacterController.toWorldOut(this);
		SummonController.toWorldOut(this);
		PartyController.toWorldOut(this, false);
		QuestController.toWorldOut(this);
		MagicDollController.toWorldOut(this);
		DungeonController.toWorldOut(this);
		// 이전에 관리중이던 목록 제거
		clearList(true);
		World.removePc(this);
		// 사용된 메모리 제거 2
		BookController.toWorldOut(this);
		ClanController.toWorldOut(this);
		InventoryController.toWorldOut(this);
		SkillController.toWorldOut(this);
		ChattingController.toWorldOut(this);
		FriendController.toWorldOut(this);
		WantedController.toWorldOut(this);
		CommandController.toWorldOut(this);
		if (chkdoll)
			chkdoll = false;
		// 레벨 퀘스트 정보 로드
		if (Lineage.is_level_quest)
			QuestDatabase.getInstance().WorldOut_Save_PcLevelList(this);
		// 반복 퀘스트 정보 로드
		if (Lineage.is_repeat_quest)
			QuestDatabase.getInstance().WorldOut_Save_PcRepeatList(this);

		this.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, 2236), true);
		// 메모리 초기화
		close();
	}

	@Override
	public void toReset(boolean world_out) {
		super.toReset(world_out);

	    if (isDead()) {
	        // 죽을 경우 이팩트
	        setDeathEffect(false);

	        try {
	            if (world_out && getGm() == 0) {
	                // 죽은 상태로 월드를 나갈 경우 좌표를 근처 마을로..
	                // 성이 존재한다면 내성으로 (잊섬 제외)
	                Kingdom k = KingdomController.find(this);
	                if (k != null && getMap() != 70) {
	                    homeX = k.getX();
	                    homeY = k.getY();
	                    homeMap = k.getMap();
	                } else {
	                    TeleportHomeDatabase.toLocation(this);
	                }

	                // 기존 위치에서 제거하고 근처 마을로 이동 후 월드에 추가
	                World.remove(this);
	                if (isDynamicUpdate()) {
	                    World.update_mapDynamic(this.x, this.y, this.map, false);
	                }

	                x = getHomeX();
	                y = getHomeY();
	                map = getHomeMap();

	                if (isDynamicUpdate()) {
	                    World.update_mapDynamic(x, y, map, true);
	                }
	                World.append(this);
	            }

	            // 다이상태 풀기.
	            setDead(false);

	            if (World.isBattleZone(getX(), getY(), getMap())) {
	                setNowHp(getTotalHp());
	            } else {
	                // 체력 채우기.
	                setNowHp(level);

	                // food게이지 하향.
	                if (getFood() > Lineage.MIN_FOOD) {
	                    setFood(Lineage.MIN_FOOD);
	                }

	                // gfx 복구
	                gfx = classGfx;
	                if (inv.getSlot(Lineage.SLOT_WEAPON) != null) {
	                    gfxMode = classGfxMode + inv.getSlot(Lineage.SLOT_WEAPON).getItem().getGfxMode();
	                } else {
	                    gfxMode = classGfxMode;
	                }
	            }

	            // 플러그인 초기화
	            PluginController.init(PcInstance.class, "toReset", this, world_out);
	        } catch (Exception e) {
	            // 예외 처리 - 필요한 경우 로깅 등 추가
	        }
	    }
	}
	@Override
	public void toRevival(object o) {
		if (PluginController.init(PcInstance.class, "toRevival", this, o) == null
				&& isDead()) {
			// 공성전중 부활이 불가능한지 확인.
			if (!Lineage.kingdom_war_revival) {
				Kingdom k = KingdomController.findKingdomLocation(this);
				if (k != null && k.isWar())
					return;
			}

			temp_object_1 = o;
			toSender(S_MessageYesNo.clone(
					BasePacketPooling.getPool(S_MessageYesNo.class), 321));
		}
	}

	@Override
	public void toRevivalFinal(object o) {
		if (isDead() && temp_object_1 != null) {
			setDeathEffect(false);
			if (Lineage.server_version >= 200) {
				// // 죽은좌표위에 객체갯수가 1개이상 존재한다면 부활 무시.
				// if(World.getMapdynamic(getX(), getY(), getMap()) >= 1)
				// return;
			}
			// 리셋처리.
			super.toReset(false);
			// 다이상태 풀기.
			setDead(false);
			// 체력 채우기.
			setNowHp(temp_hp != -1 ? temp_hp : level);
			setNowMp(temp_mp != -1 ? temp_mp : getNowMp());
			// gfx_mode 복원
			setGfxModeClear();

			// sp.controller.CommandController.attackDelay(o);
			// 패킷 처리.
			toSender(S_ObjectRevival.clone(
					BasePacketPooling.getPool(S_ObjectRevival.class),
					temp_object_1, this), true);
			toSender(
					S_ObjectEffect.clone(
							BasePacketPooling.getPool(S_ObjectEffect.class),
							this, 230), true);
			//
			temp_object_1 = null;
			temp_hp = temp_mp = -1;
		}
	}

	@Override
	public void toMoving(final int x, final int y, final int h) {
		lastMovingTime = System.currentTimeMillis();
		resetAutoAttack();
		// 이동 알리기.
		setMoving(true);
		CharacterController.toMoving(this);

		if (Util.isDistance(this.x, this.y, map, x, y, map, 1)) {
			// isFrameSpeed(Lineage.GFX_MODE_WALK);
			setMove(true);
			super.toMoving(x, y, h);
			setMove(false);
			
			if (World.get_map(x, y, map) == 127) {
				// 던전 이동 처리
				TimeDungeonDatabase.toMovingDungeon(this);
				DungeonController.toDungeon(this);
				// 144이하 버전 공성전 처리.
				if (Lineage.server_version <= 163) {
					// 163이하 버전 공성전 처리.
					// 현재 위치에 성정보 추출.
					Kingdom k = KingdomController.findKingdomLocation(this);
					// 공성중이면서, 옥좌라면 면류관 픽업처리를 통해 성주 변경처리하기.
					if (k != null && k.isWar() && k.isThrone(this)) {
						k.getCrown().toPickup(this);
					}
				}
			}

		} else {
			toPotal(this.x, this.y, map);
		}
	}

	@Override
	public void toSender(BasePacket bp) {
		if (client != null) {
			client.toSender(bp);
		} else {
			BasePacketPooling.setPool(bp);
		}
	}

	private boolean isShapeChange = false;

	public boolean isShapeChange() {
		return isShapeChange;
	}

	public void setShapeChange(boolean isShapeChange) {
		this.isShapeChange = isShapeChange;
	}

	@Override
	public void toPotal(int x, int y, int map) {
		resetAutoAttack();
		// 버그방지.
		if (World.get_map(map) == null) {
			toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
			if (getGm() > 0)
				ChattingController.toChatting(this, map + "맵이 존재하지 않습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		homeX = x;
		homeY = y;
		homeMap = map;

		toSender(S_Potal.clone(BasePacketPooling.getPool(S_Potal.class),
				this.map, map));
		// 소환객체 텔레포트
		SummonController.toTeleport(this);
		MagicDollController.toTeleport(this);
	}

	@Override
	public void toTeleport(final int x, final int y, final int map,
			final boolean effect) {
		resetAutoAttack();
		// 버그방지.
		if (World.get_map(map) == null) {
			toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
			if (getGm() > 0)
				ChattingController.toChatting(this, map + "맵이 존재하지 않습니다.", 20);
			return;
		}

		// 로봇이 아니고 던전컨트롤러의 파트타임이 존재한다면
		if (!(this instanceof PcRobotInstance)
				&& DungeonController.isDungeonPartTime(this, map)) {
			// 던전 파트 타임이 아니면 리턴한다.
			if (!DungeonController.toDungeonPartTime(this, map, true)) {
				toTeleport(getX(), getY(), getMap(), false);
				return;
			}
		}

		// 중복 체크
		setTeleport(true);

		// 2.00이하 버전에서 텔레포트후 sp, mr 이 정상표현 안되는 문제로 인해 추가.
		if (Lineage.server_version <= 200)
			toSender(S_CharacterSpMr.clone(
					BasePacketPooling.getPool(S_CharacterSpMr.class), this));
		super.toTeleport(x, y, map, effect);
		// 소환객체 텔레포트
		SummonController.toTeleport(this);
		MagicDollController.toTeleport(this);
		// 뽕데스일때 mode값 강제 전송.
		if (getGfx() == 5641)
			toSender(S_ObjectMode.clone(
					BasePacketPooling.getPool(S_ObjectMode.class), this));
		// // 텔레포트시 3초 무적되도록 버프 등록.
		// BuffController.append(this,
		// Teleport.clone(BuffController.getPool(Teleport.class),
		// SkillDatabase.find(5),
		// 3));
	}

	public void 칼렉풀기() {
		try {
	        if (isDead()) {
	            ChattingController.toChatting(this, "죽은 상태에선 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
	            return;
	        } else if (isLock()) {
	            ChattingController.toChatting(this, "마비 상태에선 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
	            return;
	        }

	        if (lastMovingTime + 1000 > System.currentTimeMillis()) {
	            ChattingController.toChatting(this, "이동중에 칼렉 풀기를 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
	            return;
	        }

	        if (getLastLackTime() < System.currentTimeMillis()) {
	            setLastLackTime(System.currentTimeMillis() + (1000 * Lineage.sword_rack_delay));
	        } else {
	            ChattingController.toChatting(this, String.format("칼렉 풀기 재사용 시간은 %d초 입니다.", Lineage.sword_rack_delay), Lineage.CHATTING_MODE_MESSAGE);
	            return;
	        }

	        resetAutoAttack();
	        // 버그방지.
	        if (World.get_map(map) == null) {
	            toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
	            if (getGm() > 0)
	                ChattingController.toChatting(this, map + "맵이 존재하지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
	            return;
	        }

	        if (isFishing()) {
	            ChattingController.toChatting(this, "낚시중엔 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
	            toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
	            return;
	        }

	        super.toTeleport(getX(), getY(), getMap(), false);
	        // 소환객체 텔레포트
	        SummonController.toTeleport(this);
	        MagicDollController.toTeleport(this);

	        ChattingController.toChatting(this, "칼렉 풀기를 사용하셨습니다.", Lineage.CHATTING_MODE_MESSAGE);
	    } catch (Exception e) {
	        e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력 또는 원하는 로깅 방식으로 변경 가능
	    }
	}
	
	/**
	 * 스피드핵 경고. 2019-08-28 by connector12@nate.com
	 */
	public void speedCheck(int mode, long 유저프레임, long 정상프레임) {
		++SpeedhackWarningCounting;

		if (Lineage.speedhack_stun) {
			if (SpeedhackWarningCounting >= Lineage.speed_hack_message_count
					&& SpeedhackWarningCounting < Lineage.speedhack_warning_count) {
				if (Lineage.speed_hack_block_time > 0) {
					if (Lineage.speed_hack_block_time >= 60)
						ChattingController.toChatting(this,
								String.format("[스피드핵 경고 %d회] %d회 이상 경고받을 경우 %d분간 마비.", SpeedhackWarningCounting,
										Lineage.speedhack_warning_count, (Lineage.speed_hack_block_time / 60)),
								Lineage.CHATTING_MODE_MESSAGE);
					else
						ChattingController.toChatting(this,
								String.format("[스피드핵 경고 %d회] %d회 이상 경고받을 경우 %d초간 마비.", SpeedhackWarningCounting,
										Lineage.speedhack_warning_count, Lineage.speed_hack_block_time),
								Lineage.CHATTING_MODE_MESSAGE);
				}
			}

			if (SpeedhackWarningCounting >= Lineage.speedhack_warning_count) {
				if (!Common.system_config_console) {
					long time = System.currentTimeMillis();
					String timeString = Util.getLocaleString(time, true);

					String log = String.format(
							"[%s]\t [스피드핵]\t [캐릭터: %s]\t [경고 횟수: %d회]\t [GFX: %d]\t [GFX MODE: %d]\t [유저 프레임: %d]\t [정상 프레임: %d]",
							timeString, getName(), SpeedhackWarningCounting, getGfx(), mode, 유저프레임, 정상프레임);

					GuiMain.display.asyncExec(new Runnable() {
						public void run() {
							GuiMain.getViewComposite().getSpeedHackComposite().toLog(log);
						}
					});
				}

				if (Lineage.speed_hack_block_time > 0) {
					FrameSpeedOverStun.init(this, Lineage.speed_hack_block_time);
					if (Lineage.speed_hack_block_time >= 60)
						ChattingController.toChatting(this,
								String.format("스피드핵 경고 횟수 초과 %d분간 캐릭터 마비.", (Lineage.speed_hack_block_time / 60)),
								Lineage.CHATTING_MODE_MESSAGE);
					else
						ChattingController.toChatting(this,
								String.format("스피드핵 경고 횟수 초과 %d초간 캐릭터 마비.", Lineage.speed_hack_block_time),
								Lineage.CHATTING_MODE_MESSAGE);
				}

				SpeedhackWarningCounting = 0;
			}
		}
	}

	/**
	 * 매개변수 객체 에게물리공격을 가할때 처리하는 메서드.
	 */
	@Override
	public void toAttack(object o, int x, int y, boolean bow, int gfxMode, int alpha_dmg, boolean isTriple, int tripleIdx) {
		long time = System.currentTimeMillis();

		 if (autoAttackTime > time) {
		        return;
		    }

		    setFight(true);

		    if (get투망시간() > time) {
		        ChattingController.toChatting(this, "투망딜레이 : 2초", Lineage.CHATTING_MODE_MESSAGE);
		        return;
		    }

		    if (isBuffDetection()) {
		        return;
		    }

		    if (isBuffTeleport()) {
		        return;
		    }

		    if (this.getChkInv()) {
		        return;
		    }

			setAttackTarget(o);
			
			int gfxmo = 0;
			if (bow)
				gfxmo = 21;
			else
				gfxmo = this.getGfxMode() + 1;

			// 자동 칼질
			if (isAutoAttack) {

				if (o instanceof MonsterInstance || o instanceof Cracker && o!=null) {
					autoAttackTarget = o;
					targetX = o.getX();
					targetY = o.getY();
					autoAttackTime = System.currentTimeMillis() + SpriteFrameDatabase.find(this, getGfx(), gfxmo);
				}
			}
			
		    boolean effect_target = true;
		    int effect = 0;
		    int dmg = 0;
		    ItemInstance weapon = inv.getSlot(8);
		    ItemInstance shield = inv.getSlot(7);

		    if (shield != null && shield instanceof ItemWeaponInstance) {
		        weapon = attackCnt++ % 2 == 0 ? weapon : shield;
		    }

			ItemInstance arrow = null;
			if (bow && weapon != null) {
				arrow = weapon.getItem().getType2().equalsIgnoreCase("gauntlet") ? inv
						.findThrowingKnife() : inv.findArrow();

				if (weapon.getItem().getEffect() > 0) {
					effect = weapon.getItem().getEffect();
				} else {
					if (arrow == null && weapon.getItem().getNameIdNumber() == 1821)
						effect = 2349;
					else if (arrow != null)
						effect = arrow.getItem().getEffect();
				}
			}

		    heading = Util.calcheading(this, x, y);

		    if (AutoHuntCheckController.checkMonster(o) && !AutoHuntCheckController.checkCount(this)) {
		        return;
		    }

		    if (Lineage.server_version <= 200 && getGfx() == 32) {
		        effect = 10;
		    }

		    if (gm > 0 || isTriple || !isTransparent()) {
		        if (!isDead()) {
		            if (inv.isWeightPercent(82)) {
		                dmg = DamageController.getDamage(this, o, bow, weapon, arrow, 0, isTriple, tripleIdx);

		                if (weapon != null && isCriticalEffect() && !(o instanceof Cracker)) {
		                    setCriticalEffect(true);
		                    // 치명타 이팩트 처리
		                    if (weapon.getItem().getType2().equalsIgnoreCase("dagger")
		                            || weapon.getItem().getType2().equalsIgnoreCase("sword")
		                            || weapon.getItem().getType2().equalsIgnoreCase("tohandsword")
		                            || weapon.getItem().getType2().equalsIgnoreCase("axe")
		                            || weapon.getItem().getType2().equalsIgnoreCase("blunt")
		                            || weapon.getItem().getType2().equalsIgnoreCase("spear")
		                            || weapon.getItem().getType2().equalsIgnoreCase("bow")
		                            || weapon.getItem().getType2().equalsIgnoreCase("staff")) {
		                        effect = Lineage.critical_effect;
		                        
		                    }
		                }

		                if (dmg > 0) {
		                    if (weapon != null && weapon.toDamage(this, o)) {
		                        dmg += weapon.toDamage(dmg);
		                        if (weapon.toDamageEffect() > 0) {
		                            effect = weapon.toDamageEffect();
		                            effect_target = weapon.isDamageEffectTarget();
		                            setCriticalEffect(false);
		                        }
		                    }
		            			
		                    //자동스턴 기사,군주
		            		if ((getClassType()==0x00 || getClassType() == 0x01) && getLevel() >= Lineage.stun_level) {
			                    if ((Lineage.stunpc && o instanceof PcInstance && Util.random(0, 99) <= Lineage.stun) || (Lineage.stunmob && o instanceof MonsterInstance && Util.random(0, 99) <= Lineage.stun)) {
			                        ShockStun.init(this, o);
			                    	}
		            			}
		            		 //자동트리플 요정
		            		if (getClassType() == 0x02 && getLevel() >= Lineage.triple_level) {
		            			if ((Lineage.tripc && o instanceof PcInstance && Util.random(0, 99) <= Lineage.tryple) || (Lineage.trimob && o instanceof MonsterInstance && Util.random(0, 99) <= Lineage.tryple)) {
			                        if (TripleArrow.init(this, o)) {
			                            isTriple = true;
			                        }
			                    }

		            		}
		                    DamageController.toDamage(this, o, dmg, bow ? 1 : 0);
		                    if (isTriple) {
		                        DamageController.toDamage(this, o, dmg, bow ? 1 : 0);
		                        DamageController.toDamage(this, o, dmg, bow ? 1 : 0);
		                    }
		                }

		                if (isTriple) {
		                    effect = 11764;
		                }

		                if (!isTriple || Lineage.server_version > 200 || tripleIdx == 0) {
		                    if (Lineage.server_version < 270) {
		                        toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), this, o, 1, dmg, effect, bow, effect > 0, x, y, effect_target), true);
		                    } else {
		                        toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), this, o, 1, dmg, effect, bow, effect > 0, x, y, effect_target), true);
		                    }
		                }

		                if (bow && arrow != null) {
		                    inv.count(arrow, arrow.getCount() - 1, true);
		                    if (isTriple) {
		                        inv.count(arrow, arrow.getCount() - 2, true);
		                    }
		                }

		                if (o instanceof RobotInstance && Util.isDistance(this, o, 1)) {
		                    o.toDamage(this, 0, 0);
		                }

		                setCriticalEffect(false);
		                setCriticalMagicEffect(false);

		            } else {
		                toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 110));
		                toSender(new S_SoundEffect(17786), true);
		            }
		        }
		    }
		}

	@Override
	public synchronized void toExp(object o, double exp) {
		//
		if (PluginController.init(PcInstance.class, "toExp", this, o, exp) != null)
			return;

		if (o.getClanId() > 0) {
			Clan k = ClanController.find(o.getClanName());
			for (object oo : k.getList()) {
				if (k != null) {
					if (Util.isDistance(o, oo, Lineage.SEARCH_LOCATIONRANGE)
							&& k.getList().size() >= Lineage.clan_exp_user
							&& !o.getClanName().equalsIgnoreCase(Lineage.new_clan_name)) {
						exp *= Lineage.buff_exp;

					}
				}

			}
		}

		if (Lineage.open_wait) {
			ChattingController.toChatting(this, "[오픈대기] 오픈대기에는 레벨업이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		// 경치이벤트
		if (Lineage.event_exp) {
			exp *= 2;
		}

		if (isBuffExpPotion()) {
			exp *= 1.2;
		}

		if (isBuffExpPotion2()) {
			exp *= 1.5;
		}

		if (isBuffExpPotion3()) {
			exp *= 2;
		}
		if (isBuffExpPotion4()) {
			exp *= 2.5;
		}

		// 동적 변경된 추가경험치 증가.
		if (getTotalExp() > 0)
			exp += exp * getTotalExp();
		
		// 배율에따른 경험치 증가.
		exp *= Lineage.rate_exp;
		
		Levelexpname = exp;

		// new 불멸의가호 경험치
		if (Lineage.immortality_exp > 0) {
			if (getInventory() != null) {

				boolean 불멸의가호 = false;
				불멸의가호 = getInventory().find(Lineage.immortality_item_name2) == null ? false : true;

				if (불멸의가호) {
					exp = exp + (exp * Lineage.immortality_exp);
				}
			}
		}
		
		ExpPanelty epl = ExpPaneltyDatabase.find(getLevel());
		if (epl != null) {
			if (getLevel() <= Lineage.max_level_exp_panelty)
				exp *= epl.getPanelty();
			else
				exp /= epl.getPanelty();
		}
		setExp(getExp() + exp);

		if (getGm() > 0) {
			ChattingController.toChatting(this, String.format("경험치 : %s", exp), Lineage.CHATTING_MODE_MESSAGE);

		}

		Levelexpname = exp;

		if (o instanceof MonsterInstance) {
			MonsterInstance mon = (MonsterInstance) o;
			// 레벨퀘스트, 반복퀘스트에 대한 처리
			try {
				if (Lineage.is_level_quest && getLevelQuestList().size() > 0) {
					String[] questMobName = null;
					int mapid[] = null;
					boolean loop = true;
					PcLevelQuest completeQuest = null;
					for (int i = 0; i < getLevelQuestList().size(); i++) {
						PcLevelQuest quest = getLevelQuestList().get(i);
						questMobName = quest.getQuestTarget();
						for (int name = 0; name < questMobName.length; name++) {
							if (questMobName[name].equalsIgnoreCase(mon
									.getMonster().getName())) {
								loop = false;
								mapid = quest.getMapId();
								if (this.getPartyId() == 0 && quest.isParty())
									break;
								if (this.getClanId() == 0 && quest.isClan())
									break;
								for (int map = 0; map < mapid.length; map++) {
									if (mapid[map] == -1
											|| this.getMap() == mapid[map]) {
										boolean drop = quest.getDropRate() == -1 ? true
												: quest.getDropRate() >= Util
														.random(1, 100) ? true
														: false;
										if (drop) {
											boolean complete = quest
													.updateCPLCount(name, 1);
											if (quest.getType()
													.equalsIgnoreCase("item")) {
												toSender(S_Message
														.clone(BasePacketPooling
																.getPool(S_Message.class),
																143,
																questMobName[name],
																questMobName[name]
																		+ " 증표"));
											}
											if (complete) {
												ChattingController
														.toChatting(this,
																"\\fU레벨퀘스트를 완료해서 보상을 받습니다.");
												String present = quest
														.getPresentItemName()
														+ " ("
														+ quest.getPresentItemCount()
														+ ")";
												if (quest
														.getPresentItemName()
														.equalsIgnoreCase("포인트")) {
												} else {
													ItemInstance item = ItemDatabase
															.newInstance(ItemDatabase
																	.find3(quest
																			.getPresentItemName()));
													if (item != null) {
														this.getInventory()
																.append(item,
																		(long) quest
																				.getPresentItemCount());
													}
												}
												ChattingController.toChatting(
														this, "\\fU보상아이템 : "
																+ present);
												quest.setComplete(true);
												completeQuest = quest;
											}
										}
										break;
									}
								}
								break;
							}
						}
						if (!loop)
							break;
					}
					if (completeQuest != null) { // 만약 완료된 퀘스트가 있다면, 본 리스트에서
													// 빼준다.
						QuestDatabase.getInstance().LevelQuest_Clear(this,
								completeQuest);
						getLevelQuestList().remove(completeQuest);
						completeQuest = null;
					}
				}
			} catch (Exception e) {
				lineage.share.System.printf(
						"%s : void toExp(object o, double exp) 레벨퀘스트 부분\r\n",
						PcInstance.class.toString());
				lineage.share.System.println(e);
			}

			try {
				if (Lineage.is_repeat_quest && getRepeatQuest() != null) {
					String[] questMobName = null;
					int mapid[] = null;
					PcRepeatQuest quest = getRepeatQuest();
					questMobName = quest.getQuestTarget();
					for (int name = 0; name < questMobName.length; name++) {
						if (questMobName[name].equalsIgnoreCase(mon
								.getMonster().getName())) {
							mapid = quest.getMapId();
							if (this.getPartyId() == 0 && quest.isParty())
								break;
							if (this.getClanId() == 0 && quest.isClan())
								break;
							for (int map = 0; map < mapid.length; map++) {
								if (mapid[map] == -1
										|| this.getMap() == mapid[map]) {
									boolean drop = quest.getDropRate() == -1 ? true
											: quest.getDropRate() >= Util
													.random(1, 100) ? true
													: false;
									if (drop) {
										boolean complete = quest
												.updateCPLCount(name, 1);
										if (quest.getType().equalsIgnoreCase(
												"item")) {
											toSender(S_Message
													.clone(BasePacketPooling
															.getPool(S_Message.class),
															143,
															questMobName[name],
															questMobName[name]
																	+ " 증표"));
										}
										if (complete) {
											ChattingController
													.toChatting(this,
															"\\fU반복퀘스트를 완료해서 보상을 받습니다.");
											String present = quest
													.getPresentItemName()
													+ " ("
													+ quest.getPresentItemCount()
													+ ")";
											if (quest.getPresentItemName()
													.equalsIgnoreCase("포인트")) {
												// this.setPoint(quest.getPresentItemCount()
												// + this.getPoint());
												// CharactersDatabase.updatePoint(this.getName(),
												// this.getPoint(),
												// false);
											} else {
												ItemInstance item = ItemDatabase
														.newInstance(ItemDatabase.find3(quest
																.getPresentItemName()));
												if (item != null) {
													this.getInventory()
															.append(item,
																	(long) quest
																			.getPresentItemCount());
												}
											}
											ChattingController.toChatting(this,
													"\\fU보상아이템 : " + present);
											QuestDatabase.getInstance()
													.RepeatQuest_Clear(this,
															quest.getQuestId());
											this.setRepeatQuest(null);
										}
									}
									break;
								}
							}
							break;
						}
					}

				}
			} catch (Exception e) {
				lineage.share.System.printf(
						"%s : void toExp(object o, double exp) 반복퀘스트 부분\r\n",
						PcInstance.class.toString());
				lineage.share.System.println(e);
			}
		}
		// 로그 기록.
		if (Log.isLog(this)) {
			int o_lv = 0;
			String o_name = null;
			int o_exp = 0;
			if (o != null) {
				if (o instanceof MonsterInstance) {
					MonsterInstance mon = (MonsterInstance) o;
					o_lv = mon.getMonster().getLevel();
					o_name = mon.getMonster().getName();
					o_exp = mon.getMonster().getExp();
				}
				if (o instanceof Cracker || o instanceof CrackerDmg) {
					o_name = "허수아비";
				}
				if (o instanceof NpcInstance) {
					NpcInstance npc = (NpcInstance) o;
					o_name = npc.getNpc().getName();
				}
			}
			if (Log.isLog(this))
				Log.appendExp(getRegisterDate(), getLevel(), (int) exp,
						(int) getExp(), o_lv, o_name, o_exp);
		}
	}

	@Override
	public void toTimer(long time) {
		isInsertItem(time);
		// 캐릭터 타이머시간과 동일하게 리스시 null포인트 에러 해결
		if (isWorldDelete()) {
			return;
		}
		
		// 신규 혈맹 자동탈퇴
		if (getClanId() != 0
				&& getClanName().equalsIgnoreCase(Lineage.new_clan_name)
				&& Lineage.is_new_clan_auto_out
				&& level >= Lineage.new_clan_max_level) {
			ClanController.toOut(this);
			ChattingController.toChatting(this, String.format(
					"신규 혈맹은 %d레벨 이상 자동탈퇴 됩니다.", Lineage.new_clan_max_level),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		// 출석체크
		attendancecheck();
		// BuffController.toBuffend(this);
		BuffController.toAden(this);
		
		if (getClassType() == 0x00 && getRoyalBuff())
			BuffController.toRoyal(this);

		if (this.getTimeInv() > 0) {
			this.setTimeInv(this.getTimeInv() - 1);
		} else if (this.getTimeInv() <= 0) {
			this.setChkInv(false);
		}

		// BuffController.toBAVT(this);
		// BuffController.toGAVT(this);

		// 디스.중첩.타이머
		if (getDisDelay() > 0) {
			addDisDelay(-1);
		}
		// 버그스캔
		int ring_cnt = 0;
		if (getInventory() != null) {
			for (ItemInstance ii : getInventory().getList()) {
				if (ii.getItem() != null
						&& ii.getItem().getType2().equalsIgnoreCase("ring")
						&& ii.isEquipped())
					ring_cnt += 1;
			}
			if (ring_cnt > 2) {
				lineage.share.System.println(String.format(
						"'%s' 2개이상 반지 착용중..", getName()));
				for (ItemInstance ii : getInventory().getList()) {
					if (ii.getItem().getType2().equalsIgnoreCase("ring")
							&& ii.isEquipped())
						ii.toClick(this, null);
				}
			}
		}
		toAcBugScan();

		if (GameSetting.접속기인증) {
			if (getGm() == 0) {
				if (!ConnectorDatabase.isIp(this.getClient().getAccountIp())) {
					ChattingController.toChatting(this,
							"[접속기 인증실패 게임을 종료합니다.]", 20);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO 자동 생성된 catch 블록
						e.printStackTrace();
					}
					toSender(S_Disconnect
							.clone(BasePacketPooling
									.getPool(S_Disconnect.class), 0x0A));
				}
			}
		}

		// 딜레이 체크
		if (isViewDelay() && getViewDelaySkillName() != null
				&& getViewDelaySkillName().length() > 0
				&& this.magic_time < this.delay_magic
				&& (this.delay_magic - time) / 1000 >= 0) {
			if ((this.delay_magic - time) / 1000 > 0)
				ChattingController.toChatting(this, "[마법이름] "
						+ getViewDelaySkillName() + " [남은 딜레이] "
						+ (this.delay_magic - time) / 1000,
						Lineage.CHATTING_MODE_MESSAGE);

			if ((this.delay_magic - time) / 1000 == 0) {
				this.magic_time = 0;
				this.delay_magic = 0;
			}
		}
		// VIP 시스템
		// 아이템 체크
		if (this.getInventory() != null && !this.isWorldDelete()) {
			ItemInstance item = this.getInventory().find(ItemDatabase.find("VIP"));
			int check = 0;

			// 수량 체크
			for (ItemInstance check_item : this.getInventory().getList()) {
				if (check_item != null && check_item.getItem().getName().equalsIgnoreCase("VIP"))
					check += 1;
			}
			// 효과 부여 체크
			if (check == 0 && isPandantBuff) {
				//this.setDynamicAddDmgBow(this.getDynamicAddDmgBow() - 3); // 원뎀
				//this.setDynamicAddDmg(this.getDynamicAddDmg() - 3); // 근뎀
				//this.setDynamicSp(this.getDynamicSp() - 2); // SP
				//this.setDynamicAddHit(this.getDynamicAddHit() - 2); // 공성
				//this.setDynamicAddHitBow(this.getDynamicAddHitBow() - 2); // 활명중
				//this.setDynamicReduction(this.getDynamicReduction() - 2); // 데미지감소
				//this.setDynamicHp(this.getDynamicHp() - 100); // HP
				//this.setDynamicMr(this.getDynamicMr() - 10); // MR
				isPandantBuff = false;
				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
				ChattingController.toChatting(this,"[알림] 인벤토리에 VIP가 존재하지 않아 효과 해제합니다.",Lineage.CHATTING_MODE_MESSAGE);
			}
			// 효과 부여
			if (item != null) {
				// 정상적으로 효과 적용할 경우
				if (check == 1 && !isPandantBuff && PandantBuff_Level == 0) {
					isPandantBuff = true;
					PandantBuff_Level = 0;
					//this.setDynamicAddDmgBow(this.getDynamicAddDmgBow() + 3); // 원뎀
					//this.setDynamicAddDmg(this.getDynamicAddDmg() + 3); // 근뎀
					//this.setDynamicSp(this.getDynamicSp() + 2); // SP
					//this.setDynamicAddHit(this.getDynamicAddHit() + 2); // 공성
					//this.setDynamicAddHitBow(this.getDynamicAddHitBow() + 2); // 활명중
					//this.setDynamicReduction(this.getDynamicReduction() + 2); // 데미지감소
					//this.setDynamicHp(this.getDynamicHp() + 100); // HP
					//this.setDynamicMr(this.getDynamicMr() + 10); // MR
					ChattingController.toChatting(this,"VIP: 경험치 증가+50%, 아데나 획득량 2배",Lineage.CHATTING_MODE_MESSAGE);
					toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class),this));
					toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class),this));
					// 수량 체크

				} else if (check > 1) {
					// 펜던트 2개 이상 소지 중이고 효과 적용 중일 때 효과 해제 시키기
					if (isPandantBuff && PandantBuff_Level > 0) {
						isPandantBuff = false;
						PandantBuff_Level = 0;
						//this.setDynamicAddDmgBow(this.getDynamicAddDmgBow() - 3); // 원뎀
						//this.setDynamicAddDmg(this.getDynamicAddDmg() - 3); // 근뎀
						//this.setDynamicSp(this.getDynamicSp() - 2); // SP
						//this.setDynamicAddHit(this.getDynamicAddHit() - 2); // 공성
						//this.setDynamicAddHitBow(this.getDynamicAddHitBow() - 2); // 활명중
						//this.setDynamicReduction(this.getDynamicReduction() - 2); // 데미지감소
						//this.setDynamicHp(this.getDynamicHp() - 100); // HP
						//this.setDynamicMr(this.getDynamicMr() - 10); // MR
						toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
						toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
						ChattingController.toChatting(this,"[알림] VIP를 2개 이상 소지하여 인첸트 효과가 적용되지 않습니다.",Lineage.CHATTING_MODE_MESSAGE);
						// 펜던트 2개 이상 소지 중이고 효과 적용 중이지 않을 때 알림
					} else {
						ChattingController.toChatting(this,"[알림] VIP를 2개 이상 소지하여 인첸트 효과가 적용되지 않습니다.",Lineage.CHATTING_MODE_MESSAGE);
					}
				}
			}
		}

		// 인벤에 20억 아데나 초과시 1억 아데나 수표로 교환
		try {
			if (!adenChange && inv != null) {
				adenChange = true;
				ItemInstance aden = getInventory().find("아데나");
				long count = 1900000000;
				long newCount = 18;

				if (aden != null && aden.getCount() > count) {
					Item i = ItemDatabase.find("1억원 금괴");

					if (i != null && aden.getCount() > count) {
						inv.count(aden, aden.getCount() - count, true);

						ItemInstance temp = inv.find(i.getName(), 1,
								i.isPiles());

						if (temp != null
								&& (temp.getBress() != 1 || temp.getEnLevel() != 0))
							temp = null;

						if (temp == null) {
							// 겹칠수 있는 아이템이 존재하지 않을경우.
							if (i.isPiles()) {
								temp = ItemDatabase.newInstance(i);
								temp.setObjectId(ServerDatabase.nextItemObjId());
								temp.setBress(1);
								temp.setEnLevel(0);
								temp.setCount(newCount);
								temp.setDefinite(true);
								inv.append(temp, true);
							} else {
								for (int idx = 0; idx < newCount; idx++) {
									temp = ItemDatabase.newInstance(i);
									temp.setObjectId(ServerDatabase
											.nextItemObjId());
									temp.setBress(1);
									temp.setEnLevel(0);
									temp.setDefinite(true);
									inv.append(temp, true);
								}
							}
						} else {
							// 겹치는 아이템이 존재할 경우.
							inv.count(temp, temp.getCount() + newCount, true);
						}

						ChattingController.toChatting(this,
								"[경고] 소지 아데나 19억 초과! : 1억원 금괴(18) 획득",
								Lineage.CHATTING_MODE_MESSAGE);
					}
				}
			}

			adenChange = false;
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 바포메트 시스템
		if (Lineage.is_batpomet_system)
			BaphometSystemController.setBaphomet(this);

		if (!Lineage.is_batpomet_system && isBaphomet) {
			BaphometSystemController.removeBaphomet(this);
			isBaphomet = false;
		}
		
		// -- 성혈혜택버프 적용
		if (GameSetting.CASTLE_BUFF_ONOFF) {
			if (getClanId() > 0) {
				Kingdom k = KingdomController.find(this);
				if (k != null) {
					if (BuffController.find(this).find(CastleMemberBuff.class) == null) {
						ChattingController.toChatting(this, "[성혈혜택] 경험치 30% 증가", 20);
						Skill s = SkillDatabase.find(410);
						BuffController.append(this, CastleMemberBuff.clone(BuffController.getPool(CastleMemberBuff.class), s, -1));
					}
				}
			}
		} else {
			// 성혈버프 리셋을 위한 공간
			if (BuffController.find(this).find(CastleMemberBuff.class) != null) {
				BuffController.remove(this, CastleMemberBuff.class);
				ChattingController.toChatting(this, "[알림] 성혈혜택 리셋중...", 20);
			}
		}

		// -- 일반 클랜 버프
		if (GameSetting.BUFF_ONOFF) {
			if (getClanId() > 0) {
				Clan k = ClanController.find(this);
				if (k != null) {
					if (BuffController.find(this).find(ClanMemberBuff.class) == null
							&& k.getList().size() >= Lineage.clan_exp_user
							&& !getClanName().equalsIgnoreCase(Lineage.new_clan_name)) {
						 ChattingController.toChatting(this, String.format("전체 혈맹원 %s명이상 혈맹혜택 적용.", Lineage.clan_exp_user), 20);
						 ChattingController.toChatting(this, "[혈맹혜택] 경험치 20% 증가", 20);
						Skill s = SkillDatabase.find(8000);
						BuffController.append(this, ClanMemberBuff.clone(
								BuffController.getPool(ClanMemberBuff.class),
								s, -1));

					} else if (BuffController.find(this).find(
							ClanMemberBuff.class) != null
							&& k.getList().size() < Lineage.clan_exp_user) {
						BuffController.remove(this, ClanMemberBuff.class);
						// ChattingController.toChatting(this,
						// String.format("\\fT혈맹원 %s명이상되면 경험치버프를 받을
						// 수 있습니다.", Lineage.clan_exp_user), 20);
					}
				}
			}
		}

		// 낚시 컨트롤러
		if (isFishing() && !isWorldDelete())
			FishingController.startFishing(this);
		
		// 시간제 던전 확인
		if (TimeDungeonDatabase.isTimeDungeon(getMap())) {
			if (--giran_dungeon_time < 1 && getGm() == 0)
				TimeDungeonDatabase.isTimeDungeonFinal(this, 0);
		}

		// 서버 메세지 표현 처리.
		if (Common.SERVER_MESSAGE && message_time <= time) {
			message_time = time + Common.SERVER_MESSAGE_TIME;
			for (String msg : Common.SERVER_MESSAGE_LIST)
				ChattingController.toChatting(this, msg, 20);
		}
		Object o = PluginController.init(PcInstance.class, "toTimer.PkHell", this, time);
		if (o == null) {
		if (getMap() == 666) {
			if (getPkHellTime() > 0) {
					
			// 초단위 하향.
				if (!isDead())
					setPkHellTime(getPkHellTime() - 1);
			} else {
				// 마을로 이동.
				LocationController.toGiran(this);
				toPotal(getHomeX(), getHomeY(), getHomeMap());
				ChattingController.toChatting(this,"지옥 체류 시간이 경과해 마을로 소환되었습니다.",Lineage.CHATTING_MODE_MESSAGE);
				setPkHellCount(getPkHellCount() + 1);
			}
		} else {
			if (getPkHellTime() > 0) {
				// 지옥으로 이동.
				LocationController.toHell(this);
				toPotal(getHomeX(), getHomeY(), getHomeMap());
			}
		}
	}
		// 이펙트
		// if (getGm() > 0) {
		// toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class),
		// this, 16159), true);
		// }
		
		// 자동저장 처리 구간.
				if (Lineage.auto_save_time > 0 && auto_save_time <= time) {
					// 월드 접속하고 타이머에 등록된후 해당 함수가 호출됫을때 auto_save_time 값이 0 임.
					// 접속하자마자 저장하는걸 방지하기위해 아래 코드 삽입.
					if (auto_save_time != 0)
						toSave();
					auto_save_time = time + Lineage.auto_save_time;
				}
				
		// 현재 진행된 time값과 비교하여 24시간이 오바됫을경우 0으로 초기화
		if (getPkTime() > 0 && getPkTime() + (1000 * 60 * 60 * 24) <= time)
			setPkTime(0);

		// 수중에 있을경우 상태에따라 hp 감소처리
		// if (World.isAquaMap(this)) {
		// // 수중에서 숨쉴수 있는 아이템 착용중인지 확인.
		// // 수중에서 숨쉴 수 있는 버프상태인지 확인.
		// if (!inv.isAquaEquipped() && !isBuffEva())
		// setNowHp(getNowHp() - Util.random(15, 30));
		// }
		if (Lineage.open_wait && !isWorldDelete()) {
			// 오픈대기시 아이템 자동 지급.
			if (Lineage.is_world_open_wait_item && open_wait_item_time <= time && !this.isWorldDelete()) {
				if (open_wait_item_time != 0) {
					// 아이템 지급
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find(Lineage.world_open_wait_item));
					if (ii != null) {
						ii.setCount(Util.random(Lineage.world_open_wait_item_min, Lineage.world_open_wait_item_max));
						super.toGiveItem(null, ii, ii.getCount());
					}
				}
				open_wait_item_time = time + Lineage.world_open_wait_item_delay;
			}
		}
		// 아이템 자동 지급.
		if (Lineage.world_premium_item_is && premium_item_time <= time && UserShopController.find(this) == null) {
			if (premium_item_time != 0) {
				// 아이템 지급
				ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find(Lineage.world_premium_item));
				// int maxLevel = PcInstanceController.getLevelMax();
				// if (ii != null && getLevel() >= Lineage.level_max) {
				if (ii != null && getLevel() >= Lineage.world_premium_item_level) {
					int count = Lineage.world_premium_rate;
					if (KingdomController.find(this) != null && Lineage.world_premium_item_kingdom_rate > 0)
						count = (int) Math.round(count * Lineage.world_premium_item_kingdom_rate);
					else if (getClanId() > 0 && Lineage.world_premium_item_clan_rate > 0)
						count = (int) Math.round(count * Lineage.world_premium_item_clan_rate);
					ii.setCount(count);
					super.toGiveItem(null, ii, ii.getCount());
				}
				//
				PluginController.init(PcInstance.class, "toTimer.premium", this, time);
			}
			premium_item_time = time + Lineage.world_premium_item_delay;
		}

		if ((getClanId() == 0 || getClanId() == 1)
				&& ((getMap() == 65 || getMap() == 37 || getMap() == 67) && getGm() == 0)) {
			ChattingController.toChatting(this, "신규혈맹 또는 혈맹이 없을경우 이동이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			// 마을로 보내기
			toTeleport(33438 + Util.random(0, 4), 32812 + Util.random(0, 4), 4, true);

		}
		if (getTurbernDelay() > 0) {
			setTurbernDelay(getTurbernDelay() - 1);
		}
		// 클라하고 연결이 종료된거라면 월드에 접속유지로 간주.
		if (getClient() == null) {
			if (worldout_time == 0)
				worldout_time = time;
			if (UserShopController.find(this) == null) {
				if (worldout_time + (1000 * Lineage.player_worldout_time) < time)
					toWorldOut();
			}
		}

		// 51 레벨 51이상시 스텟보너스
		// if(level>50)
		// toLvStat(true);

		try {
			long l = 0L;
			l = java.lang.System.currentTimeMillis() / 1000L;

			if ((getMacroPeriod() > 0) && (getMacroMessage().size() > 0)
					&& (getMacroDelay() + getMacroPeriod() < l)) {

				ChattingController.toChatting(this, (String) getMacroMessage()
						.get(getMacroIndex()), 12);

				setMacroDelay(l);
				setMacroIndex(getMacroIndex() + 1);
				if (getMacroIndex() >= getMacroMessage().size())
					setMacroIndex(0);
			}
		} catch (Exception localException7) {
			this.getMacroMessage().clear();
			ChattingController.toChatting(this, "현재 매크로 설정이 잘못되어 멘트가 나오지 않으니",
					20);
			ChattingController.toChatting(this, "게임을 종료하시고 재접속하여 설정해주시기 바랍니다.",
					20);
			lineage.share.System.println("메세지2:" + getName()
					+ "-------------------------------");
			lineage.share.System.println(localException7);
		}

		if (getGm() > 0) {
			try {
				long l = 0L;
				l = java.lang.System.currentTimeMillis() / 1000L;
				if ((getMacroPeriod() > 0) && (getMacroMessage().size() > 0)
						&& (getMacroDelay() + getMacroPeriod() < l)) {
					if (((String) getMacroMessage().get(getMacroIndex()))
							.startsWith("!"))
						ChattingController.toChatting(this,
								((String) getMacroMessage()
										.get(getMacroIndex())).substring(
										1,
										((String) getMacroMessage().get(
												getMacroIndex())).length()), 2);
					else if (((String) getMacroMessage().get(getMacroIndex()))
							.startsWith("&"))
						ChattingController.toChatting(this,
								((String) getMacroMessage()
										.get(getMacroIndex())).substring(
										1,
										((String) getMacroMessage().get(
												getMacroIndex())).length()), 3);
					else if (((String) getMacroMessage().get(getMacroIndex()))
							.startsWith("$"))
						ChattingController
								.toChatting(this, ((String) getMacroMessage()
										.get(getMacroIndex())).substring(
										1,
										((String) getMacroMessage().get(
												getMacroIndex())).length()), 12);
					else
						ChattingController
								.toChatting(this, (String) getMacroMessage()
										.get(getMacroIndex()), 0);
					// ChattingController.toChatting(this,
					// (String)getMacroMessage().get(getMacroIndex()), 12);

					setMacroDelay(l);
					setMacroIndex(getMacroIndex() + 1);
					if (getMacroIndex() >= getMacroMessage().size())
						setMacroIndex(0);
				}
			} catch (Exception localException7) {
				this.getMacroMessage().clear();
				ChattingController.toChatting(this,
						"현재 매크로 설정이 잘못되어 멘트가 나오지 않으니", 20);
				ChattingController.toChatting(this,
						"게임을 종료하시고 재접속하여 설정해주시기 바랍니다.", 20);
				lineage.share.System.println("메세지2:" + getName()
						+ "-------------------------------");
				lineage.share.System.println(localException7);
			}
		}

		if (isAutoHunt()) {

			if (getMap() == 36 || getMap() == 37) {
				toTeleport(32614 + Util.random(0, 4),
						32781 + Util.random(0, 4), 4, true);
			}

		}

		if (AutoHuntThread.getInstance().getAuto(this)) {
			setAutoTime(getAutoTime() - 1);
			// 그거 넣어드릴까여 ? 자동사냥 남은시간 10분 남으면 1분동안 10분남았다고 계속뜨는거 한번만 뜨게도가능해용 한번만
			// 떠야겟죠
			if (getAutoTime() / 60 == 10 && getAutoTime() % 60 == 0) {
				ChattingController.toChatting(this, "\\fY자동사냥 시간이 10분 남았습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
			if (!Util.isDistance(getX(), getY(), getMap(), getHomeX(),
					getHomeY(), getHomeMap(), 7)) {
				setHomeX(getX());
				setHomeY(getY());
				setHomeMap(getMap());
				toTeleport(getX(), getY(), getMap(), false);
			}
		}
		getTimeItem();

		pcAttackTarget = null;
		//
		PluginController.init(PcInstance.class, "toTimer", this, time);
	}

	@Override
	public void toGiveItem(object o, ItemInstance item, long count) {
		// 사용자가 사용자에게는 강제로 줄 수 없음.
		if (o != null && o instanceof PcInstance)
			return;

		super.toGiveItem(o, item, count);
	}

	@Override
	public boolean isAutoPickup() {
		return auto_pickup;
	}

	@Override
	public void setAutoPickup(boolean is) {
		auto_pickup = is;
	}

	/**
	 * 오토루팅 메세지를 표현할지 를 설정하는 함수.
	 * 
	 * @param is
	 */
	@Override
	public void setAutoPickupMessage(boolean is) {
		auto_pickup_message = is;
	}

	public boolean isNewClanOut() {
		return this.newClanOut;
	}

	public void setNewClanOut(boolean paramBoolean) {
		this.newClanOut = paramBoolean;
	}

	public Timestamp getNewClanOutTime() {
		return this.newClanOutTime;
	}

	public void setNewClanOutTime(Timestamp paramTimestamp) {
		this.newClanOutTime = paramTimestamp;
	}

	/**
	 * 오토루팅 메세지를 표현할지 여부.
	 * 
	 * @return
	 */
	@Override
	public boolean isAutoPickupMessage() {
		return auto_pickup_message;
	};

	@Override
	public boolean isHpbar() {
		return is_hpbar;
	}

	@Override
	public void setHpbar(boolean is_hpbar) {
		this.is_hpbar = is_hpbar;
	}

	public byte[] getDbInterface() {
		return db_interface;
	}

	public void setDbInterface(byte[] dbInterface) {
		db_interface = dbInterface;
	}

	public int getSpeedHackWarningCounting() {
		return SpeedhackWarningCounting;
	}

	public void setSpeedHackWarningCounting(int SpeedhackWarningCounting) {
		this.SpeedhackWarningCounting = SpeedhackWarningCounting;
	}

	/**
	 * 몬스터에 hp를 볼지 여부. : 디폴트는 true : 플러그인과 연결되 있음. : 안보게 하려면 연결하면됨.
	 * 
	 * @return
	 */
	@Override
	public boolean isMonsterHpBar() {
		Object o = PluginController.init(PcInstance.class, "isMonsterHpBar",
				this);
		return o == null || (Boolean) o;
	}

	@Override
	protected void toAiAttack(long time) {
		super.toAiAttack(time);

		if (attack_target == null || getInventory() == null)
			return;

		// 아이템에 따른 공격가능거리 추출하는 메서드가 필요함.
		// : 몬스터 크기별로 거리를 계산할 필요가 있음.
		ItemInstance weapon = getInventory().getSlot(8);
		int range = weapon != null ? weapon.getItem().getWeaponAttackDistance()
				: 1;
		if (!Util.isDistance(this, attack_target, range))
			setAiStatus(-2);
		if (attack_target.isDead() || attack_target.isWorldDelete())
			setAiStatus(-2);

		toAttack(attack_target, 0, 0, range > 2, 0, 0, false, 0);
	}

	@Override
	public void toDwarfAndShop(PcInstance pc, ClientBasePacket cbp) {
		// switch(cbp.readC()){
		// case 0: // 상점 구입
		// UserShopController.toBuy(this, pc, cbp);
		// break;
		// case 1: // 상점 판매
		// UserShopController.toSell(this, pc, cbp);
		// break;
		// }

		switch (cbp.readC()) {
		case 0: // 상점 구입
			toBuy(pc, cbp); // zzzzzzzzz 여기 있었네요 ㅋㅋㅋㅋㅋㅋㅋㅋ멀어지면 안될거에요
			break;
		}

	}
	
	@Override
	public int getTotalAddDmgBow() {
		int dmg = super.getTotalAddDmgBow();
		return dmg;
	}

	@Override
	public int getTotalAddDmg() {
		int dmg = super.getTotalAddDmg();
		return dmg;
	}

	@Override
	public int getTotalSp() {
		int sp = super.getTotalSp();
		return sp;
	}

	@Override
	public double getTotalExp() {
		double exp = super.getTotalExp();
		return exp;
	}
	
	// 광요 혈맹레벨
	public void toClanLevelBuff(PcInstance pc) {
		if (pc.isClanOneLevel()) {
			ChattingController.toChatting(pc, "혈맹1레벨:데미지+1(마법사:SP)", 20);
		} else if (pc.isClanTwoLevel()) {
			if (pc.getClassType() != 0x03) {
				pc.setDynamicAddDmg(pc.getDynamicAddDmg() + 1);
				pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() + 1);
			} else {
				pc.setDynamicSp(pc.getDynamicSp() + 1);
			}
			// pc.setDynamicAc(pc.getDynamicAc() + 1);
			ChattingController.toChatting(pc, "혈맹2레벨:데미지+1(마법사:SP)", 20);
		} else if (pc.isClanThreeLevel()) {
			if (pc.getClassType() != 0x03) {
				pc.setDynamicAddDmg(pc.getDynamicAddDmg() + 2);
				pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() + 2);
			} else {
				pc.setDynamicSp(pc.getDynamicSp() + 2);
			}
			// pc.setDynamicReduction(pc.getDynamicReduction() + 2);
			// pc.setDynamicAc(pc.getDynamicAc() + 2);
			ChattingController.toChatting(pc, "혈맹3레벨:데미지+2(마법사:SP)", 20);
		} else if (pc.isClanFourLevel()) {
			if (pc.getClassType() != 0x03) {
				pc.setDynamicAddDmg(pc.getDynamicAddDmg() + 3);
				pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() + 3);
			} else {
				pc.setDynamicSp(pc.getDynamicSp() + 3);
			}
			// pc.setDynamicReduction(pc.getDynamicReduction() + 2);
			// pc.setDynamicAc(pc.getDynamicAc() + 2);
			ChattingController.toChatting(pc, "혈맹4레벨:데미지+3(마법사:SP)", 20);
		} else if (pc.isClanFiveLevel()) {
			if (pc.getClassType() != 0x03) {
				pc.setDynamicAddDmg(pc.getDynamicAddDmg() + 4);
				pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() + 4);
			} else {
				pc.setDynamicSp(pc.getDynamicSp() + 4);
			}
			// pc.setDynamicReduction(pc.getDynamicReduction() + 2);
			// pc.setDynamicAc(pc.getDynamicAc() + 2);
			ChattingController.toChatting(pc, "혈맹5레벨:데미지+4(마법사:SP)", 20);
		} else if (pc.isClanSixLevel()) {
			if (pc.getClassType() != 0x03) {
				pc.setDynamicAddDmg(pc.getDynamicAddDmg() + 6);
				pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() + 6);
				pc.setDynamicAddHit(pc.getDynamicAddHit() + 5);
				pc.setDynamicAddHitBow(pc.getDynamicAddHitBow() + 5);
			} else {
				pc.setDynamicSp(pc.getDynamicSp() + 6);
			}
			// pc.setDynamicReduction(pc.getDynamicReduction() + 2);
			// pc.setDynamicAc(pc.getDynamicAc() + 2);
			ChattingController.toChatting(pc,
					"혈맹6레벨:경험치+40%,데미지+6(마법사:SP),명중+5", 20);
		} else if (pc.isClanSevenLevel()) {
			if (pc.getClassType() != 0x03) {
				pc.setDynamicAddDmg(pc.getDynamicAddDmg() + 8);
				pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() + 8);
				pc.setDynamicAddHit(pc.getDynamicAddHit() + 10);
				pc.setDynamicAddHitBow(pc.getDynamicAddHitBow() + 10);
			} else {
				pc.setDynamicSp(pc.getDynamicSp() + 8);
			}
			// pc.setDynamicReduction(pc.getDynamicReduction() + 2);
			// pc.setDynamicAc(pc.getDynamicAc() + 2);
			ChattingController.toChatting(pc,
					"혈맹7레벨:경험치+50%,데미지+8(마법사:SP),명중+10", 20);

			// 클랜벨런스 맞추기용.성환(리덕션,추타)
		}
		if (pc.getClanId() == Lineage.Clan_numone) {
			pc.setDynamicReduction(pc.getDynamicReduction()
					+ Lineage.Clan_Redone);
		}
		if (pc.getClanId() == Lineage.Clan_numtwo) {
			pc.setDynamicReduction(pc.getDynamicReduction()
					+ Lineage.Clan_Redtwo);
		}
		// 성환.클랜밸런스 리덕션 -
		if (pc.getClanId() == Lineage.Clan_numthree) {
			pc.setDynamicReduction(pc.getDynamicReduction()
					- Lineage.Clan_Redthree);
		}
		if (pc.getClanId() == Lineage.Clan_numfour) {
			pc.setDynamicReduction(pc.getDynamicReduction()
					- Lineage.Clan_Redfour);
		}

		pc.toSender(S_CharacterStat.clone(
				BasePacketPooling.getPool(S_CharacterStat.class), pc));
		pc.toSender(S_CharacterSpMr.clone(
				BasePacketPooling.getPool(S_CharacterSpMr.class), pc));
	}

	/*
	 * 접속시 영자에게 접속 알리기
	 */
	public void toMeticeMessage(PcInstance pc) {
		// TimeLine.start("♥♥♥♥ " + pc.getName() + " / " + client.getAccountId()
		// + " / " + client.getAccountIp() + " 접속 ♥♥♥♥♥ [메모리사용: "
		// + SystemUtil.getUsedMemoryMB() + "MB]");
		for (PcInstance admin : World.getPcList()) {
			if (admin.getGm() > 0) {
				ChattingController.toChatting(admin, "접속자: " + pc.getName()
						+ "(" + client.getAccountId() + ")" + "  접속 아이피: "
						+ client.getAccountIp(), 20);
			}
		}
		// TimeLine.end();
	}

	public Timestamp _giranQuest;
	public Timestamp _talkQuest;

	public Timestamp getGiranQuestTime() {
		return _giranQuest;
	}

	public void setGiranQuestTime(Timestamp ts) {
		_giranQuest = ts;
	}

	public Timestamp getTalkQuestTime() {
		return _talkQuest;
	}

	public void setTalkQuestTime(Timestamp ts) {
		_talkQuest = ts;
	}

	public void toUpdate(PcInstance pc, boolean visual) {
		if (Lineage.server_version <= 144)
			return;

		// 자기자신 처리.
		pc.toSender(S_ObjectHitratio.clone(
				BasePacketPooling.getPool(S_ObjectHitratio.class), pc, visual));
	}

	public int getPevent() {
		return this.pevent;
	}

	public void setPevent(int paramInt) {
		this.pevent = paramInt;
	}

	private ArrayList<PcLevelQuest> levelqstlist = new ArrayList<PcLevelQuest>();

	public ArrayList<PcLevelQuest> getLevelQuestList() {
		return levelqstlist;
	}

	private PcRepeatQuest _repeatqst = null;

	public PcRepeatQuest getRepeatQuest() {
		return _repeatqst;
	}

	public void setRepeatQuest(PcRepeatQuest quest) {
		_repeatqst = quest;
	}

	public int getDynamicLuck() {
		return luck;
	}

	public void setDynamicLuck(int l) {
		this.luck = l;
	}

	public int getDynamicAden() {
		return aden;
	}

	public void setDynamicAden(int l) {
		this.aden = l;
	}

	private void toAcBugScan() {
		int armor_ac = 0;// 착용하고 있는 방어구 AC값
		int dex_ac = this.getAcDex(); // 덱방
		int buff_ac = BuffController.toFindacbuff(this, 0);
		for (ItemInstance ii : getInventory().getList()) {
			if (ii.isEquipped()) {
				armor_ac += ii.getItem().getAc() + ii.getEnLevel();
			}
		}
		int sum = armor_ac + dex_ac + buff_ac - 10;
		// lineage.share.System.println(
		// String.format("sum:%s,armor_ac:%s,dex_ac:%s,buff_ac:%s ",
		// sum,armor_ac,dex_ac,buff_ac) );
		if ((this.getTotalAc() - 10) - sum >= Lineage.armor_ac) {
			lineage.share.System.println(String.format(
					"'%s' 방어구 '%s'이상 차이 확인바랍니다.", this.getName(),
					Lineage.armor_ac));
			/*
			 * if(getMap()!=666){ toTeleport(32736, 32774, 666, true);
			 * 
			 * }
			 */
		}
	}
	/**
	 * 액션 가능한지 확인
	 * 2020-09-27
	 * by connector12@nate.com
	 */
	public boolean isActionCheck(boolean isWalk) {
		long time = System.currentTimeMillis();

		if (isWalk) {
			if (time < lastMagicActionTime) {
				halfDelayCount++;
				FrameSpeedOverStun.init(this, 2);

				if (!Common.system_config_console) {
					String timeString = Util.getLocaleString(time, true);
					String log = String.format("[%s]\t [반딜, 노딜]\t [캐릭터: %s]\t [경고 횟수: %d회]\t [GFX: %d]\t [GFX MODE: %d]\t [프레임: %d]", timeString, getName(), halfDelayCount, getGfx(), getGfxMode(),
							lastMagicActionTime - time);

					GuiMain.display.asyncExec(new Runnable() {
						public void run() {
							GuiMain.getViewComposite().getSpeedHackComposite().toLog(log);
						}
					});
				}
				return false;
			}
		} else {
			if (Lineage.attackAndMagic_delay > 0 && time < lastActionTime && lastActionTime - time > Lineage.attackAndMagic_delay) {
				return false;
			}
		}

		return true;
	}
	
	/**
	 * 자동물약 함수. 2018-05-10 by all-night.
	 */
	public void autoPotion() {
		if (getInventory() != null && !isWorldDelete() && !isInvis()
				&& !isTransparent() && !isDead() && !isLock() && !isLockHigh() && !isLockLow()) {
			ItemInstance item = null;

			for (ItemInstance potion : getInventory().getList()) {
				if (potion instanceof HealingPotion) {
					if (potion.getItem() != null && potion.getItem().getName().equalsIgnoreCase(autoPotionName)) {
						item = potion;
						break;
					}
				}
			}

			if (item != null) {
				if (item.isClick(this))
					item.toClick(this, null);
			} else {
				ChattingController.toChatting(this, "[자동 물약] 설정된 물약이 모두 소모되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
				toSender(new S_SoundEffect( 17787), true);
				autoPotionName = null;
			}
	}
	}
	public void autoPotion2() {
			Buff b = BuffController.find(this);

			ItemInstance haste = null;
			ItemInstance brave = null;

			haste = getInventory().find(HastePotion.class);

			switch (getClassType()) {
			case 0:
				brave = getInventory().find(BraveryPotion2.class);
				break;
			case 1:
				brave = getInventory().find(BraveryPotion.class);
				break;
			case 2:
				brave = getInventory().find(ElvenWafer.class);
				break;
			}

			if (haste != null && (b == null || (b.find(Haste.class) == null && b.find(HastePotion.class) == null))) {
				if (haste.isClick(this) && haste.isAutoClick(this))
					haste.toClick(this, null);
			}

			if (getClassType() == Lineage.LINEAGE_CLASS_WIZARD)
				return;

			if (brave != null && (b == null || (b.find(Bravery.class) == null && b.find(BraveryPotion.class) == null && b.find(BraveryPotion2.class) == null)
							&& b.find(Wafer.class) == null && b.find(ElvenWafer.class) == null)) {
				if (brave.isClick(this) && brave.isAutoClick(this))
					brave.toClick(this, null);
			}
		}
	
	public void checkAutoPotionCancel(Character cha) {
	        if (isAutoPotion || isAutoPotion2) {
	            isAutoPotion = isAutoPotion2 = false;
	            ChattingController.toChatting(this, "자동사냥시 자동물약이 비활성화 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
	        }
	    }
	
	
	/**
	 * 자동물약 PvP 여부 확인 메소드. 2019-07-04 by connector12@nate.com
	 */
	public void checkAutoPotionPvP(Character cha) {
		if (cha instanceof PcInstance) {
			PcInstance use = (PcInstance) cha;

			if (isAutoPotion && (World.isNormalZone(getX(), getY(), getMap()) || World.isCombatZone(getX(), getY(), getMap()))) {
				isAutoPotion = false;
				ChattingController.toChatting(this, "PK시 자동물약이 비활성화 됩니다.",
						Lineage.CHATTING_MODE_MESSAGE);
			}

			if (use.isAutoPotion && (World.isNormalZone(use.getX(), use.getY(), use.getMap()) || World.isCombatZone(use.getX(), use.getY(), use.getMap()))) {
				use.isAutoPotion = false;
				ChattingController.toChatting(use, "PK시 자동물약이 비활성화 됩니다.",
						Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}

	public void checkAutohillPvP(Character cha) {

		if (cha instanceof PcInstance) {
			PcInstance use = (PcInstance) cha;

			if (isHeal_1()
					&& (World.isNormalZone(getX(), getY(), getMap()) || World
							.isCombatZone(getX(), getY(), getMap()))) {
				setHeal_1(false);
				ChattingController.toChatting(this, "PK시 자힐이 비활성화 됩니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				setJahillTime(System.currentTimeMillis() + 3000L);
			}
			if (isHeal_2()
					&& (World.isNormalZone(getX(), getY(), getMap()) || World
							.isCombatZone(getX(), getY(), getMap()))) {
				setHeal_2(false);
				ChattingController.toChatting(this, "PK시 자힐이 비활성화 됩니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				setJahillTime(System.currentTimeMillis() + 3000L);
			}
			if (isHeal_3()
					&& (World.isNormalZone(getX(), getY(), getMap()) || World
							.isCombatZone(getX(), getY(), getMap()))) {
				setHeal_3(false);
				ChattingController.toChatting(this, "PK시 자힐이 비활성화 됩니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				setJahillTime(System.currentTimeMillis() + 3000L);
			}

			if (use.isHeal_1()
					&& (World.isNormalZone(getX(), getY(), getMap()) || World
							.isCombatZone(getX(), getY(), getMap()))) {
				use.setHeal_1(false);
				ChattingController.toChatting(this, "PK시 자힐이 비활성화 됩니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				setJahillTime(System.currentTimeMillis() + 3000L);
			}
			if (use.isHeal_2()
					&& (World.isNormalZone(getX(), getY(), getMap()) || World
							.isCombatZone(getX(), getY(), getMap()))) {
				use.setHeal_2(false);
				ChattingController.toChatting(this, "PK시 자힐이 비활성화 됩니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				setJahillTime(System.currentTimeMillis() + 3000L);
			}
			if (use.isHeal_3()
					&& (World.isNormalZone(getX(), getY(), getMap()) || World
							.isCombatZone(getX(), getY(), getMap()))) {
				use.setHeal_3(false);
				ChattingController.toChatting(this, "PK시 자힐이 비활성화 됩니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				setJahillTime(System.currentTimeMillis() + 3000L);
			}

		}
	}

	public void savePet() {
		Summon s = SummonController.find(this);
		if (s != null) {
			Connection con = null;
			try {
				con = DatabaseConnection.getLineage();
				// 모든 펫 저장.
				SummonController.toSave(con, this);
				// 모든 펫 제거 하면서 펫목걸이도 갱신.
				s.removeAllPet();
			} catch (Exception e) {
				lineage.share.System.println(PetMasterInstance.class.toString()
						+ " : toPush(PcInstance pc)");
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con);
			}
		}
	}
	
	/**
	 * 자동칼질 타겟 정보 초기화. 2019-02-14 by connector12@nate.com
	 */
	public void resetAutoAttack() {
		autoAttackTarget = null;
		autoAttackTime = 0L;
		targetX = 0;
		targetY = 0;
	}

	/**
	 * 자동칼질 비활성화 메소드. 2019-07-07 by connector12@nate.com
	 */
	public void AutoHuntcancelAutoAttack(Character cha) {
		if (isAutoAttack) {
		isAutoAttack = false;
		autoAttackTarget = null;
		autoAttackTime = 0L;
		targetX = 0;
		targetY = 0;
		ChattingController.toChatting(this, "자동사냥시 자동칼질이 비활성화 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
	}
	}
	
	public void cancelAutoAttack() {
		isAutoAttack = false;
		autoAttackTarget = null;
		autoAttackTime = 0L;
		targetX = 0;
		targetY = 0;
		ChattingController.toChatting(this, "자동칼질이 비활성화 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
	}

	/**
	 * 자동칼질 비활성화 메소드. 2019-07-07 by connector12@nate.com
	 */
	public void cancelAutoAttack(Character cha) {
		if (cha instanceof PcInstance) {
			PcInstance use = (PcInstance) cha;
			if (isAutoAttack) {
				isAutoAttack = false;
				autoAttackTarget = null;
				autoAttackTime = 0L;
				targetX = 0;
				targetY = 0;
				ChattingController.toChatting(this, "PK시 자동칼질이 비활성화 됩니다.",
						Lineage.CHATTING_MODE_MESSAGE);
			}
			if (use.isAutoAttack) {
				use.isAutoAttack = false;
				use.autoAttackTarget = null;
				use.autoAttackTime = 0L;
				use.targetX = 0;
				use.targetY = 0;
				ChattingController.toChatting(use, "PK시 자동칼질이 비활성화 됩니다.",
						Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}

	/**
	 * 장비 스왑 데이터 로드. 2019-08-01 by connector12@nate.com
	 */
	public void readSwap() {
	    if (swap != null) {
	        Connection con = null;
	        PreparedStatement st = null;
	        ResultSet rs = null;

	        try {
	            synchronized (swap) {
	                con = DatabaseConnection.getLineage();
	                st = con.prepareStatement("SELECT * FROM characters_swap WHERE cha_objId=?");
	                st.setLong(1, getObjectId());
	                rs = st.executeQuery();

	                while (rs.next()) {
	                    String key = rs.getString("swap_name");
	                    Swap[] item = swap.getOrDefault(key, new Swap[Lineage.SLOT_GARDER + 1]);

	                    Swap temp = null;
	                    if (!rs.getString("swap_item_name").equalsIgnoreCase("null")) {
	                        temp = new Swap();
	                        temp.setObjId(rs.getLong("swap_item_objId"));
	                        temp.setItem(rs.getString("swap_item_name"));
	                        temp.setBless(rs.getInt("swap_item_bless"));
	                        temp.setEnLevel(rs.getInt("swap_item_en"));
	                    }

	                    item[rs.getInt("swap_idx")] = temp;
	                    swap.put(key, item);
	                }
	            }
	        } catch (Exception e) {
	            lineage.share.System.println("readSwap 에러. 캐릭터: " + getName());
	            lineage.share.System.println(e);
	        } finally {
	            DatabaseConnection.close(con, st, rs);
	        }
	    }
	}

	/**
	 * 장비 스왑 저장. 2019-08-01 by connector12@nate.com
	 */
	public void saveSwap(Connection con) {
	    if (swap != null) {
	        try {
	            synchronized (swap) {
	                // 기존 항목 삭제
	                try (PreparedStatement deleteSt = con.prepareStatement("DELETE FROM characters_swap WHERE cha_objId=?")) {
	                    deleteSt.setLong(1, getObjectId());
	                    deleteSt.executeUpdate();
	                }

	                // 새 항목 일괄 삽입
	                try (PreparedStatement insertSt = con.prepareStatement(
	                        "INSERT INTO characters_swap SET cha_objId=?, swap_name=?, swap_idx=?, swap_item_objId=?, swap_item_name=?, swap_item_bless=?, swap_item_en=?")) {

	                    for (Map.Entry<String, Swap[]> entry : swap.entrySet()) {
	                        String key = entry.getKey();
	                        Swap[] s = entry.getValue();

	                        for (int i = 0; i < s.length; i++) {
	                            boolean isNull = s[i] == null;
	                            insertSt.setLong(1, getObjectId());
	                            insertSt.setString(2, key);
	                            insertSt.setInt(3, i);
	                            insertSt.setLong(4, isNull ? 0 : s[i].getObjId());
	                            insertSt.setString(5, isNull ? "null" : s[i].getItem());
	                            insertSt.setInt(6, isNull ? 0 : s[i].getBless());
	                            insertSt.setInt(7, isNull ? 0 : s[i].getEnLevel());
	                            insertSt.addBatch();
	                        }
	                    }

	                    insertSt.executeBatch();
	                }
	            }
	        } catch (Exception e) {
	            lineage.share.System.println("saveSwap 에러. 캐릭터: " + getName());
	            lineage.share.System.println(e);
	        }
	    }
	}

	public void removeSwap(String key) {
	    if (swap != null) {
	        Connection con = null;
	        PreparedStatement st = null;

	        try {
	            synchronized (swap) {
	                con = DatabaseConnection.getLineage();
	                st = con.prepareStatement("DELETE FROM characters_swap WHERE cha_objId=? AND swap_name=?");
	                st.setLong(1, getObjectId());
	                st.setString(2, key);
	                st.executeUpdate();

	                swap.remove(key);
	            }
	        } catch (Exception e) {
	            lineage.share.System.println("removeSwap 에러. 캐릭터: " + getName());
	            lineage.share.System.println(e);
	        } finally {
	            DatabaseConnection.close(con, st);
	        }
	    }
	}

	public void putSwap(String key, Swap[] swapList) {
		synchronized (swap) {
			swap.put(key, swapList);
		}
	}

	public Swap[] getSwap(String key) {
		synchronized (swap) {
			return swap.get(key);
		}
	}

	public Map<String, Swap[]> getSwap() {
		synchronized (swap) {
			return swap;
		}
	}

	public void setSwap(Map<String, Swap[]> swap) {
		this.swap = swap;
	}

	/**
	 * 장비 스왑 등록. 2019-08-01 by connector12@nate.com
	 */
	public boolean insertSwap(String key) {
	    // 이미 등록 중이거나 등록이 불가능한 경우
	    if (isInsertSwap) {
	        synchronized (swap) {
	            boolean insert = true; // 스왑이 이미 존재하는지 여부를 나타내는 플래그
	            Inventory inv = getInventory(); // 캐릭터의 인벤토리를 얻어옴
	            Swap[] item = new Swap[Lineage.SLOT_GARDER + 1]; // 스왑 아이템 배열 초기화

	            // 이미 해당 키로 스왑이 등록되어 있는 경우
	            if (swap.get(key) != null) {
	                insert = false; // 등록 플래그를 false로 설정하고
	                swap.remove(key); // 기존 스왑을 제거
	            }

	            // 캐릭터의 인벤토리가 존재하는 경우
	            if (inv != null) {
	                for (int i = 0; i <= Lineage.SLOT_GARDER; i++) {
	                    ItemInstance slot = inv.getSlot(i);

	                    // 특정 슬롯을 제외한 범위에 있는 경우 스킵
	                    if (i > 12 && i < 18)
							continue;
						if (i > 19 && i < 27)
							continue;
						if (i == 9)
							continue;

	                    // 슬롯이 비어 있는 경우
	                    if (slot == null) {
	                        item[i] = null;
	                    } else {
	                        // 스왑 아이템 정보 생성 및 할당
	                        Swap temp = new Swap();
	                        temp.setObjId(slot.getObjectId());
	                        temp.setItem(slot.getItem().getName());
	                        temp.setEnLevel(slot.getEnLevel());
	                        temp.setBless(slot.getBress());

	                        item[i] = temp;
	                    }
	                }

	                // 스왑 맵에 등록
	                swap.put(key, item);
	            }

	            // 메시지 출력 및 기타 처리
	            if (insert)
	                ChattingController.toChatting(this, String.format("[%s] 장비 스왑 등록 완료.", key),
	                        Lineage.CHATTING_MODE_MESSAGE);
	            else
	                ChattingController.toChatting(this, String.format("[%s] 장비 스왑 수정 완료.", key),
	                        Lineage.CHATTING_MODE_MESSAGE);

	            NpcSpawnlistDatabase.itemSwap.toTalk(this, null);
	            isInsertSwap = false; // 등록 완료 후 플래그를 false로 설정
	            return true; // 등록 성공
	        }
	    }
	    return false; // 등록 불가능한 경우
	}

	public void toTalk(PcInstance pc, String action, String type,
			ClientBasePacket cbp) {

		// System.println(action);

		if (action.indexOf("shoptel-") > -1) {

			if (pc.getMap() != 350) {
				ChattingController.toChatting(pc,
						"\\fY시장 맵에서만 시세 명령어 사용 가능합니다.",
						Lineage.CHATTING_MODE_MESSAGE);
			} else {
				List<String> list = pc.getShopList();

				int shoptel_num = Integer.parseInt(action.replaceAll(
						"shoptel-", ""));

				PcShopInstance ps = World.findPcShop(list.get(shoptel_num));

				pc.toTeleport(ps.getX(), ps.getY(), ps.getMap(), false);

				ChattingController.toChatting(pc, ps.getName() + "으로 이동 완료.",
						Lineage.CHATTING_MODE_MESSAGE);
			}
		}

		// if(action.indexOf("rank_") > -1){
		// Connection con = null;
		//
		//
		// RankController.toRankinfo(con, idx, type, time,
		// Lineage.rank_filter_names_query==null ? "ORDER BY exp DESC" :
		// String.format("WHERE %s ORDER BY exp DESC",
		// Lineage.rank_filter_names_query));
		//
		// }

		if (action.equalsIgnoreCase("maeipcheck")) {
			if (!pc.isMaeip_check()) {
				pc.setMaeip_check(true);
				ChattingController.toChatting(pc, "자신의 등록물품만 보기 활성화",
						Lineage.CHATTING_MODE_MESSAGE);
			} else {
				pc.setMaeip_check(false);
				ChattingController.toChatting(pc, "자신의 등록물품만 보기 비활성화",
						Lineage.CHATTING_MODE_MESSAGE);
			}
		}

		if (action.equalsIgnoreCase("admintrade")) {
			pc.setAdena_page(1);
			UserAdenaSellController.AdminAdenaList(pc);
		}

		if (action.contains("adenapay")) {

			int action_num = Integer.valueOf(action.replaceAll("adenapay", "")) - 1;

			int pc_adena_page = Integer.valueOf(pc.getAdena_list()
					.get(action_num).replaceAll("#", ""));

			pc.setAdena_uid(pc_adena_page);

			UserAdenaSellController.AdenaPage(pc, pc_adena_page);

		}

		if (action.contains("aodlq")) {
			// if(pc.getGm() > 0) {
			int action_num = Integer.valueOf(action.replaceAll("aodlq", "")) - 1;

			int pc_adena_page = Integer.valueOf(pc.getAdena_list2()
					.get(action_num).replaceAll("#", ""));

			pc.setAdena_uid(pc_adena_page);

			UserAdenaSellController.AdminAdenaPage(pc, pc_adena_page);
			// }else {
			// ChattingController.toChatting(pc, "운영자만 볼 수 있는 페이지입니다.",
			// Lineage.CHATTING_MODE_MESSAGE);
			// }
		}
		// if(action.contains("ranking")) {
		// // if(pc.getGm() > 0) {
		// int action_num = Integer.valueOf(action.replaceAll("ranking", "")) -
		// 1;
		//
		// int rank =
		// Integer.valueOf(pc.getAdena_list2().get(action_num).replaceAll("#",
		// ""));
		//
		// pc.setAdena_uid(pc_adena_page);
		//
		// UserAdenaSellController.AdminAdenaPage(pc, pc_adena_page);
		// }else {
		// ChattingController.toChatting(pc, "운영자만 볼 수 있는 페이지입니다.",
		// Lineage.CHATTING_MODE_MESSAGE);
		// }
		// }

		if (action.equalsIgnoreCase("maeipnext")) {

			pc.setAdena_page(pc.getAdena_page() + 1);

			UserAdenaSellController.AdminAdenaList(pc);

		}

		if (action.equalsIgnoreCase("maeipback")) {

			if (pc.getAdena_page() > 1) {
				pc.setAdena_page(pc.getAdena_page() - 1);
			} else {
				pc.setAdena_page(1);
			}

			UserAdenaSellController.AdminAdenaList(pc);

		}

		if (action.equalsIgnoreCase("adenanext")) {

			pc.setAdena_page(pc.getAdena_page() + 1);

			UserAdenaSellController.AdenaList(pc);

		}

		if (action.equalsIgnoreCase("adenaback")) {

			if (pc.getAdena_page() > 1) {
				pc.setAdena_page(pc.getAdena_page() - 1);
			} else {
				pc.setAdena_page(1);
			}

			UserAdenaSellController.AdenaList(pc);

		}

		if (action.equalsIgnoreCase("atrade")
				|| action.equalsIgnoreCase("tradecancel")
				|| action.equalsIgnoreCase("servertrade")) {
			pc.setAdena_page(1);
			UserAdenaSellController.AdenaList(pc);
		}

		if (action.equalsIgnoreCase("maeiptrade")) {
			pc.setAdena_page(1);
			UserAdenaSellController.AdminAdenaList(pc);
		}

		if (action.equalsIgnoreCase("maeipok")) {
			UserAdenaSellController
					.AdminAdenaBuy(pc, pc.getAdena_uid(), "판매완료");
		}

		if (action.equalsIgnoreCase("buy_maeip")) {
			UserAdenaSellController.AdminAdenaBuy(pc, pc.getAdena_uid(),
					"거래진행중");
		}

		if (action.equalsIgnoreCase("maeipcancle")) {
			UserAdenaSellController
					.AdminAdenaBuy(pc, pc.getAdena_uid(), "판매취소");
		}

		if (action.equalsIgnoreCase("adenapanmae")) {
			UserAdenaSellController.Append(pc);
		}

		if (action.equalsIgnoreCase("maeipadena")) {
			if (pc.getPay_info1() != null
					&& !pc.getPay_info1().equalsIgnoreCase("")) {
				UserAdenaSellController.AdminAppend(pc);
			} else {
				ChattingController.toChatting(pc, "계좌 등록후 사용가능 합니다.",
						Lineage.CHATTING_MODE_MESSAGE);
			}
		}

		if (action.equalsIgnoreCase("tradeok")) {
			UserAdenaSellController.AdenaBuy(pc, pc.getAdena_uid());
		}

	}

	/**
	 * 영자 아이템 반환
	 */
	protected void toBuy(PcInstance pc, ClientBasePacket cbp) {
		int count = cbp.readH();
		boolean sell_bool = false;
		if (count > 0 && count <= 100) {
			for (int j = 0; j < count; ++j) {
				long item_idx = cbp.readD();
				int item_count = (int) cbp.readD();
				if (item_count > 2000000000 || item_count < 0)
					return;

				// 영자가 누구 했을때...
				if (pc.getShop_inven_num() == 0) {
					for (PcInstance pcc : World.getPcList()) {
						for (ItemInstance ii : pcc.getInventory().getList()) {
							if (ii.getObjectId() == item_idx) {
								ChattingController.toChatting(pcc, String
										.format("메티스가 아이템 %s %d개를 수거하였습니다.", ii
												.getItem().getName(),
												item_count),
										Lineage.CHATTING_MODE_MESSAGE);
								if (ii.getCount() <= item_count) {
									pcc.getInventory().remove(ii, true);
								} else {
									pcc.getInventory().count(ii,
											ii.getCount() - item_count, true);
								}
							}
						}
					}
				}

				if (pc.getShop_inven_num() == 1) {
					for (ItemInstance ii : pc.getInventory().getList()) {
						if (ii == null || item_count < 0) {
							continue;
						}

						if (ii.getObjectId() == item_idx) {
							if (!ii.getItem().getName().equalsIgnoreCase("아데나")) {
								ii.setShop_count(item_count);
								pc.getShop_temp_list().add(ii);
							} else {
								ChattingController.toChatting(pc,
										"아데나는 판매불가합니다.",
										Lineage.CHATTING_MODE_MESSAGE);
							}
						}
					}
				}
				if (pc.getShop_inven_num() == 2) {
					for (ItemInstance ii : pc.getInventory().getList()) {
						if (ii == null || item_count < 0) {
							continue;
						}

						if (ii.getObjectId() == item_idx) {
							PcShopInstance pc_shop = PcMarketController.shop_list
									.get(pc.getObjectId());
							pc_shop.list.put(0L, new PcShop(pc_shop,
									item_count, "아데나", ii.getShop_count()));
							PcMarketController.isShopToAppend(pc, ii,
									ii.getShop_count());
							// if(pc_shop.getListSize() > 0) {
							if (pc_shop.getX() == 0 || pc_shop.getY() == 0) {
								PcMarketController.insertShopRobot(pc, pc_shop);

								// 사용자 스폰시키기.
								pc_shop.setHeading(5);
								pc_shop.toTeleport(pc.getX(), pc.getY(),
										pc.getMap(), false);

								ChattingController.toChatting(pc,
										"\\fR상점이 시작되었습니다.",
										Lineage.CHATTING_MODE_MESSAGE);
							} else
								return;
							// }

						}
					}
					pc.clearTempList();
				}

				if (pc.getShop_inven_num() == 5) {
					for (ItemInstance ii : pc.getInventory().getList()) {
						if (ii.getObjectId() == item_idx) {
							if ((item_count % 100) != 0) {
								ChattingController.toChatting(pc,
										"0단위로 판매 가능합니다.",
										Lineage.CHATTING_MODE_MESSAGE);
								pc.setAdena_page(1);
								UserAdenaSellController.AdenaList(pc);
							} else {
								ii.setShop_count(item_count);
								int sise = Lineage.useradenasise
										/ Lineage.useradenasise2;
								ii.setAdena_sise((int) item_count / sise);
								ChattingController
										.toChatting(
												pc,
												String.format(
														"판매가격을 입력해주세요. 입력가능한  최대 시세는 %d 입니다.",
														(int) ii.getAdena_sise()),
												Lineage.CHATTING_MODE_MESSAGE);
								sell_bool = true;
							}
						}
					}
				}

				if (pc.getShop_inven_num() == 6) {
					for (ItemInstance ii : pc.getInventory().getList()) {
						if (ii.getObjectId() == item_idx) {
							if (item_count <= (int) (ii.getAdena_sise())) {
								UserAdenaSellController.AdenaUpload(pc, "판매",
										(int) ii.getShop_count(),
										(int) item_count, Util.getMonth(System
												.currentTimeMillis()), Util
												.getDate(System
														.currentTimeMillis()));

								if (ii.getCount() <= item_count) {
									pc.getInventory().remove(ii, true);
								} else {
									pc.getInventory().count(ii,
											ii.getCount() - ii.getShop_count(),
											true);
								}

								pc.setAdena_page(1);
								UserAdenaSellController.AdenaList(pc);
							} else {
								ChattingController
										.toChatting(
												pc,
												String.format(
														"판매가격을 입력해주세요. 입력가능한  최대 시세는 %d 입니다.",
														(int) ii.getAdena_sise()),
												Lineage.CHATTING_MODE_MESSAGE);
								pc.setShop_inven_num(6);
								pc.toSender(S_TestInven.clone(BasePacketPooling
										.getPool(S_TestInven.class), pc, pc
										.getShop_temp_list()));
							}
						}
					}
					pc.clearTempList();
				}

				if (pc.getShop_inven_num() == 7) {
					for (ItemInstance ii : pc.getInventory().getList()) {
						if (ii.getObjectId() == item_idx) {
							if ((item_count % 100) != 0) {
								ChattingController.toChatting(pc,
										"0단위로 판매 가능합니다.",
										Lineage.CHATTING_MODE_MESSAGE);
								pc.setAdena_page(1);
								UserAdenaSellController.AdminAdenaList(pc);
							} else {
								UserAdenaSellController.adenCount = item_count;
								ii.setShop_count(item_count);
								int sise = Lineage.adminadenasise
										/ Lineage.adminadenasise2;
								ii.setAdena_sise((int) item_count / sise);
								ChattingController
										.toChatting(
												pc,
												String.format(
														"판매가격을 입력해주세요. 입력가능한 시세는 %d 입니다.",
														(int) ii.getAdena_sise()),
												Lineage.CHATTING_MODE_MESSAGE);
								sell_bool = true;
							}
						}
					}
				}

				if (pc.getShop_inven_num() == 8) {
					for (ItemInstance ii : pc.getInventory().getList()) {
						if (UserAdenaSellController.adenCount < Lineage.adena_trade_regedit_min_valueone) {
							ChattingController
									.toChatting(
											pc,
											"[알림]최소 등록아데나는 "
													+ Util.numberFormat(Lineage.adena_trade_regedit_min_valueone)
													+ "부터 입니다.",
											Lineage.CHATTING_MODE_MESSAGE);
							return;
						}

						if (ii.getObjectId() == item_idx) {
							if (item_count == (int) (ii.getAdena_sise())) {
								UserAdenaSellController.itemCount = item_count;
								UserAdenaSellController.AppendFinal(pc,
										UserAdenaSellController.adenCount,
										UserAdenaSellController.itemCount,
										(int) ii.getShop_count(),
										(int) item_count);
								// UserAdenaSellController.AdminAdenaUpload(pc,
								// "판매중", (int)ii.getShop_count(), (int)
								// item_count,
								// Util.getMonth(System.currentTimeMillis()),
								// Util.getDate(System.currentTimeMillis()),
								// pc.getPay_info1(), pc.getPay_info2(),
								// pc.getPay_info3());

								if (ii.getCount() <= item_count) {

									pc.getInventory().remove(ii, true);
								} else {
									pc.getInventory().count(ii,
											ii.getCount() - ii.getShop_count(),
											true);
								}

								pc.setAdena_page(1);
								UserAdenaSellController.AdminAdenaList(pc);
							} else {
								ChattingController
										.toChatting(
												pc,
												String.format(
														"판매가격을 입력해주세요. 입력가능한 시세는 %d 입니다.",
														(int) ii.getAdena_sise()),
												Lineage.CHATTING_MODE_MESSAGE);
								pc.setShop_inven_num(8);
								pc.toSender(S_TestInven.clone(BasePacketPooling
										.getPool(S_TestInven.class), pc, pc
										.getShop_temp_list()));
							}
						}
					}
				}

			}
		}

		if (pc.getShop_inven_num() == 1) {
			pc.setShop_inven_num(2);
			ChattingController.toChatting(pc,
					String.format("\\fW판매할 가격을 지정해주세요."),
					Lineage.CHATTING_MODE_MESSAGE);
			pc.toSender(S_TestInven.clone(
					BasePacketPooling.getPool(S_TestInven.class), pc,
					pc.getShop_temp_list()));
		}

		if (pc.getShop_inven_num() == 5 && sell_bool) {
			pc.setShop_inven_num(6);
			pc.toSender(S_TestInven.clone(
					BasePacketPooling.getPool(S_TestInven.class), pc,
					pc.getShop_temp_list()));
		}

		if (pc.getShop_inven_num() == 7 && sell_bool) {
			pc.setShop_inven_num(8);
			pc.toSender(S_TestInven.clone(
					BasePacketPooling.getPool(S_TestInven.class), pc,
					pc.getShop_temp_list()));
		}

	}

	public int getAccountUid() {
		return accountUid;
	}

	public void setAccountUid(int accountUid) {
		this.accountUid = accountUid;
	}

	public int getBoard() {
		return board;
	}

	public void setBoard(int board) {
		this.board = board;
	}

	/**
	 * by.JSN_Soft 2021.01.15 자동사냥 추가.
	 */
	public AStar aStar2; // 길찾기 변수
	public Node tail2; // 길찾기 변수
	public int[] iPath2; // 길찾기 변수
	private List<object> astarList; // astar 무시할 객체 목록.
	public int step2; // 일렬에 동작처리중 사용되는 스탭변수.
	public ShopInstance shopTemp2; // 상점 처리 임시 저장용

	private Map<Integer, object> _autoTargetList = new HashMap<Integer, object>();
	private object _autoTarget;
	private int _autoStatus;
	private long _autoTimeAttack;
	private long _autoTimeMove;
	private boolean _autoDead;
	private int _autoDeadTime = 5;
	private long _autoAiTime;

	private boolean autoTargetSummon;// 서먼 타격 변수.
	public String mapname;
	private boolean autoHunt;
	private int autoMap;
	// 충전식 시간을 측정하는 변수.
	private long autoTime;
	// 판매 on/off 설정 변수
	private boolean sellItem;
	// 물약 설정 관련 변수
	private String autoPotion;
	private String autoPotion2;
	private int autoPotionPersent;
	public List<Item> sell_List;
	private boolean autocommand;
	private long potionTime;
	private boolean reShop;

	public void huntClose() {
		//
		shopTemp2 = null;
		step2 = 0;

		if (aStar2 != null)
			aStar2.cleanTail();
		if (astarList != null)
			clearAstarList2();

		autoHunt = false;
		autoMap = 0;
		_autoTarget = null;
		_autoStatus = 0;
		_autoTimeAttack = 0;
		_autoTimeMove = 0;
		_autoDead = false;
		_autoDeadTime = 5;
		_autoAiTime = 0;

		autoTargetSummon = false;
		mapname = null;
		autoTime = 0;
		// 판매 on/off 설정 변수
		sellItem = false;
		// 물약 설정 관련 변수
		autoPotion = autoPotion2 = null;
		autoPotionPersent = 0;
		autocommand = false;
		potionTime = 0;
		reShop = false;

		sell_List = new ArrayList<Item>();

	}

	public void AutoHunt() {
		aStar2 = new AStar();
		iPath2 = new int[2];
		astarList = new ArrayList<object>();
	}

	public boolean isReShop() {
		return reShop;
	}

	public void setReShop(boolean reShop) {
		this.reShop = reShop;
	}

	public boolean isAutocommand() {
		return autocommand;
	}

	public void setAutocommand(boolean autocommand) {
		this.autocommand = autocommand;
	}

	public String getAutoPotion() {
		return autoPotion;
	}

	public void setAutoPotion(String autoPotion) {
		this.autoPotion = autoPotion;
	}
	
	public String getAutoPotion2() {
		return autoPotion2;
	}

	public void setAutoPotion2(String autoPotion2) {
		this.autoPotion2 = autoPotion2;
	}

	public int getAutoPotionPersent() {
		return autoPotionPersent;
	}

	public void setAutoPotionPersent(int autoPotionPersent) {
		this.autoPotionPersent = autoPotionPersent;
	}

	public boolean isSellItem() {
		return sellItem;
	}

	public void setSellItem(boolean sellItem) {
		this.sellItem = sellItem;
	}

	public long getAutoTime() {
		return autoTime;
	}

	public void setAutoTime(long autoTime) {
		this.autoTime = autoTime;
	}

	public boolean isAutoTargetSummon() {
		return autoTargetSummon;
	}

	public void setAutoTargetSummon(boolean autoTargetSummon) {
		this.autoTargetSummon = autoTargetSummon;
	}

	public boolean containsAstarList2(object o) {
		synchronized (astarList) {
			return astarList.contains(o);
		}
	}

	public void appendAstarList2(object o) {
		synchronized (astarList) {
			if (!astarList.contains(o))
				astarList.add(o);
		}
	}

	public void removeAstarList2(object o) {
		synchronized (astarList) {
			astarList.remove(o);
		}
	}

	public void clearAstarList2() {
		synchronized (astarList) {
			astarList.clear();
		}
	}

	private boolean changeDarkelf = false;

	public boolean isChangeDarkelf() {
		return changeDarkelf;
	}

	public void setChangeDarkelf(boolean changeDarkelf) {
		this.changeDarkelf = changeDarkelf;
	}

	public Map<Integer, object> getAutoTargetList() {
		return _autoTargetList;
	}

	public void setAutoTargetList(Map<Integer, object> attackList) {
		this._autoTargetList = attackList;
	}

	public void addAutoTargetList(object mon, int loc) {
		if (_autoTargetList.containsValue(mon)) {
			return;
		}
		_autoTargetList.put(loc, mon);
	}

	public void removeAutoTargetList(object mon) {
		if (mon == null || !_autoTargetList.containsValue(mon))
			return;
		_autoTargetList.values().remove(mon);
	}

	public object getAutoTarget() {
		return _autoTarget;
	}

	public void setAutoTarget(object mon) {
		_autoTarget = mon;
	}

	public int getAutoStatus() {
		return _autoStatus;
	}

	public void setAutoStatus(int i) {
		_autoStatus = i;
	}

	public long getAutoTimeAttack() {
		return _autoTimeAttack;
	}

	public void setAutoTimeAttack(long time) {
		_autoTimeAttack = time;
	}

	public long getAutoTimeMove() {
		return _autoTimeMove;
	}

	public void setAutoTimeMove(long time) {
		_autoTimeMove = time;
	}

	public boolean isAutoDead() {
		return _autoDead;
	}

	public void setAutoDead(boolean b) {
		_autoDead = b;
	}

	public int getAutoDeadTime() {
		return _autoDeadTime;
	}

	public void setAutoDeadTime(int i) {
		_autoDeadTime = i;
	}

	public long getAutoAiTime() {
		return _autoAiTime;
	}

	public void setAutoAiTime(long l) {
		_autoAiTime = l;
	}

	public synchronized boolean isAutoHunt() {
		return autoHunt;
	}

	public synchronized void setAutoHunt(boolean autoHunt) {
		this.autoHunt = autoHunt;
	}

	public synchronized int getAutoMap() {
		return autoMap;
	}

	public synchronized void setAutoMap(int autoMap) {
		this.autoMap = autoMap;
	}

	public long getPotionTime() {
		return potionTime;
	}

	public void setPotionTime(long potionTime) {
		this.potionTime = potionTime;
	}

	/**
	 * 잡기 프로젝트 by Jsn_Soft
	 */
	private long attackTime;
	private long skillTime;
	private int lastAttackMotion;
	private int lastSkillMotion;

	private void MotionClose() {
		attackTime = 0;
		skillTime = 0;
		lastAttackMotion = 0;
		lastSkillMotion = 0;
	}

	public int getLastAttackMotion() {
		return lastAttackMotion;
	}

	public void setLastAttackMotion(int lastAttackMotion) {
		this.lastAttackMotion = lastAttackMotion;
	}

	public int getLastSkillMotion() {
		return lastSkillMotion;
	}

	public void setLastSkillMotion(int lastSkillMotion) {
		this.lastSkillMotion = lastSkillMotion;
	}

	public long getSkillTime() {
		return skillTime;
	}

	public void setSkillTime(long skillTime) {
		this.skillTime = skillTime;
	}

	public long getAttackTime() {
		return attackTime;
	}

	public void setAttackTime(long attackTime) {
		this.attackTime = attackTime;
	}

	/**
	 * 오토루팅 색상 변경 & 특정아이템 선정을 위한 변수. Map 에서 key 가 .메세지 [번호] 번호에 해당함. 번호를 불러와서
	 * 색상이랑 아이템 이름을 불러올 예정임.
	 */
	private Map<Integer, String> pickItem;
	private Map<Integer, String> pickColor;

	private void PickItemSetting() {
		pickItem = new HashMap<Integer, String>();
		pickColor = new HashMap<Integer, String>();
	}

	private void PickItemClose() {
		if (pickItem != null)
			pickItem.clear();
		if (pickColor != null)
			pickColor.clear();
	}

	public Map<Integer, String> getPickItem() {
		return pickItem;
	}

	public void putPickItem(int num, String name) {
		synchronized (pickItem) {
			pickItem.put(num, name);
		}
	}

	public Map<Integer, String> getPickColor() {
		return pickColor;
	}

	public void putPickColor(int num, String name) {
		synchronized (pickColor) {
			pickColor.put(num, name);
		}
	}

	public String getPickItem(int num) {
		return pickItem.get(num);
	}

	public String getPickColor(int num) {
		return pickColor.get(num);
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	public String getTempClanName() {
		return tempClanName;
	}

	public void setTempClanName(String tempClanName) {
		this.tempClanName = tempClanName;
	}

	public String getTempTitle() {
		return tempTitle;
	}

	public void setTempTitle(String tempTitle) {
		this.tempTitle = tempTitle;
	}

	public int getTempClanId() {
		return tempClanId;
	}

	public void setTempClanId(int tempClanId) {
		this.tempClanId = tempClanId;
	}

	public int getTempClanGrade() {
		return tempClanGrade;
	}

	public void setTempClanGrade(int tempClanGrade) {
		this.tempClanGrade = tempClanGrade;
	}
	
	public boolean isSwardLack() {
		return swardLack;
	}

	public void setSwardLack(boolean swardLack) {
		this.swardLack = swardLack;
	}

	public long getLastLackTime() {
		return lastLackTime;
	}

	public void setLastLackTime(long lastLackTime) {
		this.lastLackTime = lastLackTime;
	}

	private int turbernDelay;
	public int getTurbernDelay() {
		return turbernDelay;
	}
	public void setTurbernDelay(int i) {
		this.turbernDelay = i;
	}
	
	public MagicDoll getMagicDoll() {
		return magicDoll;
	}

	public void setMagicDoll(MagicDoll magicDoll) {
		this.magicDoll = magicDoll;
	}

	public MagicDollInstance getMagicDollinstance() {
		return magicDollinstance;
	}

	public void setMagicDollinstance(MagicDollInstance magicDollinstance) {
		this.magicDollinstance = magicDollinstance;
	}
	
	public boolean isAuto_party_message() {
		return auto_party_message;
	}

	public void setAuto_party_message(boolean auto_party_message) {
		this.auto_party_message = auto_party_message;
	}
	
	public object getTempShop() {
		return tempShop;
	}

	public void setTempShop(object tempShop) {
		this.tempShop = tempShop;
	}
	
	public long getAutoHuntAnswerTime() {
		return autoHuntAnswerTime;
	}

	public void setAutoHuntAnswerTime(long autoHuntAnswerTime) {
		this.autoHuntAnswerTime = autoHuntAnswerTime;
	}

	public String getAutoHuntAnswer() {
		return autoHuntAnswer;
	}

	public void setAutoHuntAnswer(String autoHuntAnswer) {
		this.autoHuntAnswer = autoHuntAnswer;
	}

	public int getAutoHuntMonsterCount() {
		return autoHuntMonsterCount;
	}

	public void setAutoHuntMonsterCount(int autoHuntMonsterCount) {
		this.autoHuntMonsterCount = autoHuntMonsterCount;
	}

	public Itemtrade getItemTrade() {
		return itemTrade;
	}

	public void setItemTrade(Itemtrade itemTrade) {
		this.itemTrade = itemTrade;
	}

	public void getTimeItem() {
		try {
			if (inv != null) {
				for (ItemInstance i : inv.getList()) {
					// 도스창 출력
					if (i.isTimeCheck()) {
						if (i.getTimestamp() == null) {
							ChattingController.toChatting(this, "[" + i.getItem().getName() + "] 시간이 다 되어 아이템이 소멸됩니다.", Lineage.CHATTING_MODE_MESSAGE);
							inv.remove(i, true);
						}
						Timestamp nowTime = new Timestamp(
								System.currentTimeMillis());
						String[] nowDate = nowTime.toString().split(" ")[0]
								.split("-");
						int nowYear = Integer.valueOf(nowDate[0]);
						int nowMonth = Integer.valueOf(nowDate[1]);
						int nowDay = Integer.valueOf(nowDate[2]);
						String[] nowDate2 = nowTime.toString().split(" ")[1]
								.split(":");
						int nowHour = Integer.valueOf(nowDate2[0]);
						int nowMin = Integer.valueOf(nowDate2[1]);
						int nowSec = Integer.valueOf(nowDate2[2]
								.substring(0, 2));

						String[] lastDate = i.getTimestamp().split(" ")[0]
								.split("-");
						int lastYear = Integer.valueOf(lastDate[0]);
						int lastMonth = Integer.valueOf(lastDate[1]);
						int lastDay = Integer.valueOf(lastDate[2]);
						String[] lastDate2 = i.getTimestamp().split(" ")[1]
								.split(":");
						int lastHour = Integer.valueOf(lastDate2[0]);
						int lastMin = Integer.valueOf(lastDate2[1]);
						int lastSec = Integer.valueOf(lastDate2[2].substring(0,
								2));

						if (nowYear >= lastYear && nowMonth >= lastMonth
								&& nowDay >= lastDay && nowHour >= lastHour
								&& nowMin >= lastMin && nowSec >= lastSec) {
							ChattingController.toChatting(this, "[" + i.getItem().getName() + "] 시간이 다 되어 아이템이 소멸됩니다.", Lineage.CHATTING_MODE_MESSAGE);
							inv.remove(i, true);
						}
					}
				}
			}
		} catch (Exception e) {
			lineage.share.System.println(PetMasterInstance.class.toString()
					+ " : getTimeItem() -> 캐릭터: " + getName());
			lineage.share.System.println(e);
		}

	}

	/**
	 * 결투장 관련 메서드. 2018-05-02 by all-night.
	 */
	public void battleZone() {
		// 결투장 입장
		if (World.isBattleZone(getX(), getY(), getMap()) && !isBattlezone) {
			isBattlezone = true;
			ChattingController.toChatting(this, "결투장에 입장하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
		}

		// 결투장 피바
		if (!World.isBattleZone(getX(), getY(), getMap()) && isBattlezone) {
			ChattingController.toChatting(this, "결투장을 퇴장하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
			toSender(S_ObjectCriminal.clone(BasePacketPooling.getPool(S_ObjectCriminal.class), this, 0), true);

			isBattlezone = false;
		}

		if (World.isBattleZone(getX(), getY(), getMap()) && isBattlezone)
			toSender(S_ObjectCriminal.clone(BasePacketPooling.getPool(S_ObjectCriminal.class), this, 1), true);

		if (Lineage.is_battle_zone_hp_bar && isBattlezone) {
			for (object o : getInsideList()) {
				if (o instanceof PcInstance && ((PcInstance) o).isBattlezone) {
					toSender(S_ObjectHitratio.clone(BasePacketPooling.getPool(S_ObjectHitratio.class), (Character) o, true));
				}
			}
		}
	}

	public void attendancecheck() {
		// 출석체크 시간 카운팅
		try {
			if (!this.isWorldDelete()) {
				if (this.getDaycount() < Lineage_Balance.lastday) {

					if (this.getDaycheck() == 0) {
						if (++dayptime >= Lineage_Balance.dayc && this.getDaycheck() == 0) {
							if (++checkmenttime >= Lineage_Balance.checkment) {
								checkmenttime = 0;
								ChattingController.toChatting(this, String.format("\\fU출석체크 버튼을 눌러 보상을 수령하세요."), Lineage.CHATTING_MODE_MESSAGE);
							}

						}

						AccountDatabase.updateptime(dayptime, this.accountUid);

					}
				}
			}
		} catch (Exception e) {

		}
	}
}
