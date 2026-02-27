package lineage.share;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lineage.bean.database.FirstInventory;
import lineage.bean.database.FirstSpawn;
import lineage.bean.database.FirstSpell;
import lineage.database.BackgroundDatabase;
import lineage.util.Util;
import lineage.world.controller.ClanController;

public final class Lineage {

	// 서버 버전 : 144, 163, 230, 300 등..
	static public int server_version = 0;
	// 칼렉 딜레이(명령어 아이템 전부)
	static public int sword_rack_delay;
	// 유저 사망 패널티 보호모드
	static public boolean 유저보호모드 = false;
	// 반딜 사용시 실패 처리 시키기 여부
	static public boolean delay_semi_bug;
	// 서버 이름
	static public String server_name;
	// 운영자 이름
	static public String master_name;

	// 공격 방식 구분용
	static public final int ATTACK_TYPE_WEAPON = 0;
	static public final int ATTACK_TYPE_BOW = 1;
	static public final int ATTACK_TYPE_MAGIC = 2;
	static public final int ATTACK_TYPE_DIRECT = 3; // 경비에게 데미지 당햇다고 요청하는구간에

	static public final int 수룡의마안_이팩트 = 7672;
	static public final int 풍룡의마안_이팩트 = 7673;
	static public final int 지룡의마안_이팩트 = 7671;
	static public final int 화룡의마안_이팩트 = 7674;
	static public final int 탄생의마안_이팩트 = 7675;
	static public final int 형상의마안_이팩트 = 7676;
	static public final int 생명의마안_이팩트 = 7678;
	// 매입상인에게 팔경우 아이템 가격의 몇%로 팔지 여부
	static public double sell_item_rate;
	// 결투장 사용 여부
	static public boolean is_battle_zone;
	// 결투장 좌표
	static public int battle_zone_x1;
	static public int battle_zone_y1;
	static public int battle_zone_x2;
	static public int battle_zone_y2;
	static public int battle_zone_map;
	// 결투장에서 상대방 피바 보일지 여부
	static public boolean is_battle_zone_hp_bar;
	// 운영자 몹피바
	static public boolean gm_hp_bar;
	// 로봇(무인케릭) 자동버프사들 사용여부
	static public boolean robot_auto_buff;
	// 자동사냥 스틸 방지 여부
	static public boolean robot_auto_pc_steal_prevent = true;
	// 자동버프에 필요한 아데나 수량
	static public int robot_auto_buff_aden;
	// 화말자동버프에 필요한 아데나 수량
	static public int robot_auto_buff_aden1;
	// 글말자동버프에 필요한 아데나 수량
	static public int robot_auto_buff_aden2;
	// 은말자동버프에 필요한 아데나 수량
	static public int robot_auto_buff_aden3;
	// 말섬자동버프에 필요한 아데나 수량
	static public int robot_auto_buff_aden4;

	static public double grimreaper_spawn_probability;
	// 필요해서 만듬.
	// 반딜 타임
	static public int delay_semi_bug_check_time;
	// 액션 표현 대장장이
	static public final int[] NPCAction = { 4, 30, 4, 30, 4, 30 };
	// 액션 표현 안톤
	static public final int[] NPCAction_Anton = { 17, 17, 17, 17, 17, 17 };
	// 액션 표현 제이슨
	static public final int[] NPCAction_Jason = { 18, 18, 3, 18, 18, 3 };
	// 액션 표현 헥터
	static public final int[] NPCAction_Hector = { 17, 17, 18, 18, 18, 17 };
	// 출석, 퀘스트 초기화 시간
	static public int quest_reset_hour;
	// 기란감옥 시간제한 사용여부.
	static public boolean is_giran_dungeon_time;
	// 기란감옥 이용시간(분)
	static public int giran_dungeon_time;
	// 기란감옥 초기화 시간
	static public int giran_dungeon_inti_time;
	// 던전 이용 시간 초기화 알림 여부
	static public boolean dungeon_inti_time_message;
	// 기란 감옥 입장 레벨
	static public int giran_dungeon_level;
	static public int giran_dungeon_level2;
	static public int giran_dungeon_level3;
	// 기란감옥 초기화 주문서 사용 횟수
	static public int giran_dungeon_scroll_count;
	// 몬스터 퀘스트
	static public String q1 = null;
	static public int qc1;
	static public String q2 = null;
	static public int qc2;
	static public String q3 = null;
	static public int qc3;

	// 몬스터 랜덤퀘스트
	static public String rq1 = null;
	static public int rqc1;
	static public String rq2 = null;
	static public int rqc2;
	static public String rq3 = null;
	static public int rqc3;
	static public String rq4 = null;
	static public int rqc4;
	static public String rq5 = null;
	static public int rqc5;
	static public String rq6 = null;
	static public int rqc6;
	static public String rq7 = null;
	static public int rqc7;
	static public String rq8 = null;
	static public int rqc8;

	// 창고 처리 구분 종류
	static public final int DWARF_TYPE_NONE = 0;
	static public final int DWARF_TYPE_CLAN = 1;
	static public final int DWARF_TYPE_ELF = 2;
	static public final int DWARF_TYPE_CLAN_HISTORY = 3;

	// 채팅 구분 종류
	static public final int CHATTING_MODE_NORMAL = 0;
	static public final int CHATTING_MODE_SHOUT = 2;
	static public final int CHATTING_MODE_GLOBAL = 3;
	static public final int CHATTING_MODE_CLAN = 4;
	static public final int CHATTING_MODE_WHISPER = 9;
	static public final int CHATTING_MODE_PARTY = 11;
	static public final int CHATTING_MODE_TRADE = 12;
	static public final int CHATTING_MODE_CLAN_SAFE = 13;
	static public final int CHATTING_MODE_MESSAGE = 20;
	static public final int CHATTING_MODE_WEBSERVER_GLOBAL = 100;

	// 각 성별 고유 아이디
	static public final int KINGDOM_KENT = 1;
	static public final int KINGDOM_ORCISH = 2;
	static public final int KINGDOM_WINDAWOOD = 3;
	static public final int KINGDOM_GIRAN = 4;
	static public final int KINGDOM_HEINE = 5;
	static public final int KINGDOM_ABYSS = 6;
	static public final int KINGDOM_ADEN = 7;

	// 인공지능 상태 변수
	static public final int AI_STATUS_DELETE_ = -2; // 메모리 재사용 처리 안함.
	static public final int AI_STATUS_DELETE = -1; // 죽은상태 쓰레드에서 제거처리됨.
	static public final int AI_STATUS_WALK = 0; // 랜덤워킹 상태
	static public final int AI_STATUS_ATTACK = 1; // 공격 상태
	static public final int AI_STATUS_DEAD = 2; // 죽은 상태
	static public final int AI_STATUS_CORPSE = 3; // 시체 상태
	static public final int AI_STATUS_SPAWN = 4; // 스폰 상태
	static public final int AI_STATUS_ESCAPE = 5; // 도망 상태
	static public final int AI_STATUS_PICKUP = 6; // 아이템 줍기 상태

	// 버프 마법 종료전 알림
	static public int buff_magic_time_min = 5;
	static public int buff_magic_time_max = 10;

	// 파티시 아이템 드랍시 색깔 나오게 할 아이템 이름
	static public String item_print;

	// 무혈, 신규 혈맹을 제외하고 같은 혈맹이 아닐경우 채팅 보일지 여부
	static public boolean is_chatting_clan;

	// 경험치 지급단 경험치
	static public int exp_marble_min;
	static public int exp_marble_max;

	static public int exp2_marble_min;
	static public int exp2_marble_max;

	// 디스 이팩트
	static public int disinte_grate_effect = 6117;
	// 헬파이어 이팩트
	static public int hell_fire_effect = 11660;
	// 콜라이트닝 이팩트
	static public int call_lighting_effect = 11736;
	// MISS 이팩트
	static public int miss_effect = 13418;
	// 치명타 이팩트
	static public int critical_effect = 13412;
	// GM 이팩트
	static public int gm_effect = 16159;

	static public int potion_message = 10;
	// 어드밴스 스피릿을 상대방에게 사용하게 할지 여부
	static public boolean is_advance_spirit_target;
	// 투견
	static public boolean is_fight;
	static public String fight_aden = "아데나";
	static public int fight_ticket_price = 500;
	static public double fight_rate = 1.95;
	static public long fight_max_ticket = 1000;

	static public double great_hill;
	static public double full_hill;
	static public double hill_all;

	static public double weapon_bect_damage;

	// 시체 유지 시간
	static public int ai_robot_corpse_time = 15 * 1000;
	static public int ai_corpse_time = 40 * 1000;
	static public int ai_summon_corpse_time = 40 * 1000;
	static public int ai_pet_corpse_time = 120 * 1000;
	// 로봇 스폰 유지 시간.
	static public int ai_robot_spawn_time = 30 * 1000;
	// 혈맹 자동 버프 시간(분)
	static public int clan_auto_buff_time;
	// 몬스터 체력회복제 복용 타이밍에 퍼센트값.
	static public int ai_auto_healingpotion_percent;
	// 몬스터 자연회복 틱타임 주기값.
	static public int ai_monster_tic_time;
	// 몬스터 레벨에따른 경험치 지급연산 처리 사용 여부.
	static public boolean monster_level_exp;
	// 소환된 몬스터가 아이템을 드랍할지 여부.
	static public boolean monster_summon_item_drop;
	// 몬스터 아이템 드랍 방식 부분. (false:old, true:new)
	static public boolean monster_item_drop;
	// 보스몬스터스폰시 메세지 표현할지 여부.
	static public boolean monster_boss_spawn_message;
	// 보스가 누구에게 죽엇는지 메세지를 표현할지 여부.
	static public boolean monster_boss_dead_message;
	// 보스가 스폰된 후 정해진 시간이내에 못잡을 경우 소멸.
	static public int boss_live_time;
	// miss 이팩트 여부
	static public boolean is_miss_effect;
	// gm 이팩트 여부
	static public boolean is_gm_effect;
	// 레벨업 지원 아이템 사용 가능 여부
	static public boolean is_exp_support;
	// 레벨업 지원 아이템의 최대 사용 레벨
	static public int exp_support_max_level;
	// 아덴판매취소시 대기시간
	static public int pc_trade_sell_time;
	// 구매신청후 입금안하고 거래중일때 취소 대기시간
	static public int pc_trade_buy_deposit_delay;
	// 초보존 사용 레벨
	static public int Beginner_max_level;

	static public int pc_trade_buy_max_count;
	static public int pc_trade_buy_min_level;

	static public int item_trade_buy_min_level;

	static public int item_trade_sell_min_level;

	// 화면 중앙에 메세지 띄우기 사용 여부.
	static public boolean is_blue_message;

	// 펫 최대 렙업값.
	static public int pet_level_max;
	// 펫 진화 레벨.
	static public int evolution_level;

	static public int pc_market_level;
	// 잊혀진섬 텔레포터 외부화
	static public boolean lost_island_wanted = false;
	static public boolean is_lost_island_join = true;
	static public int lost_island_min_level;
	static public int lost_island_join_item_count;
	static public String lost_island_join_item;
	static public boolean lost_no_clan_island = false;

	// 이뮨투함 종료전에 메세지를 표현할지 여부.
	static public int buff_ImmuneToHarm_message;

	// 글로잉오라 종료전에 메세지를 표현할지 여부.
	static public int buff_GlowingAura_message;

	// 자동낚시 사용 여부
	static public boolean is_auto_fishing;
	// 낚시 딜레이(초)
	static public int fish_delay;
	// 낚시 100%확률로 무조건 획득
	static public String fish_exp;
	// 낚시 밑밥
	static public String fish_rice;
	// 자동낚시 가능한 레벨
	static public int auto_fish_level;
	// 자동낚시에 필요한 화폐종류
	static public String auto_fish_coin;
	// 자동낚시 한번당 드는 화폐 비용
	static public long auto_fish_expense;

	// 아이템 별 밝기 값.
	static public final int CANDLE_LIGHT = 8;
	static public final int LAMP_LIGHT = 10;
	static public final int LANTERN_LIGHT = 12;

	static public int useradenasise = 1000000;
	static public int useradenasise2 = 10000;

	static public int adminadenasise = 1000000;
	static public int adminadenasise2 = 10000;

	static public double adena_cancle_per;

	// 실제 메모리에 등록될 슬롯 아이디.
	static public int SLOT_NONE = 0x0E;
	static public int SLOT_HELM = 1;
	static public int SLOT_ARMOR = 2;
	static public int SLOT_SHIRT = 3;
	static public int SLOT_CLOAK = 4;
	static public int SLOT_GAKBAN = 5; // 각반.
	static public int SLOT_BOOTS = 5;
	static public int SLOT_GLOVE = 6;
	static public int SLOT_SHIELD = 7;
	static public int SLOT_WEAPON = 8; // 9 없음.
	static public int SLOT_NECKLACE = 10;
	static public int SLOT_BELT = 11;
	static public int SLOT_EARRING = 12;
	static public int SLOT_EARRING2 = 13; // 13 ~ 17
	static public int SLOT_RING1 = 18;
	static public int SLOT_RING2 = 19;
	static public int SLOT_RING3 = 20;// 20 ~ 26
	static public int SLOT_RING4 = 21;
	static public int SLOT_AMULET1 = 22;
	static public int SLOT_AMULET2 = 23; // 23 ~ 26
	static public int SLOT_AMULET3 = 24;
	static public int SLOT_AMULET4 = 25;
	static public int SLOT_AMULET5 = 26;
	static public int SLOT_GARDER = 27;// 24

	// 해당 무기 착용시 변화되는 gfxmode값
	static public final int WEAPON_NONE = 0x00;
	static public final int WEAPON_SWORD = 0x04;
	static public final int WEAPON_TOHANDSWORD = 0x32;
	static public final int WEAPON_AXE = 0x0B;
	static public final int WEAPON_BOW = 0x14;
	static public final int WEAPON_SPEAR = 0x18;
	static public final int WEAPON_WAND = 0x28;
	static public final int WEAPON_DAGGER = 0x2E;
	static public final int WEAPON_BLUNT = 0x0B;
	static public final int WEAPON_CLAW = 0x3A; // 58
	static public final int WEAPON_EDORYU = 0x36; // 54
	static public final int WEAPON_THROWINGKNIFE = 0x0B6A;
	static public final int WEAPON_ARROW = 0x42;
	static public final int WEAPON_GAUNTLET = 0x3E;
	static public final int WEAPON_CHAINSWORD = 0x18;
	static public final int WEAPON_KIRINGKU = 0x3a;
	static public final int FISHING_ROD = 0x1C;

	// 클레스별 종류
	static public final int LINEAGE_CLASS_ROYAL = 0x00;
	static public final int LINEAGE_CLASS_KNIGHT = 0x01;
	static public final int LINEAGE_CLASS_ELF = 0x02;
	static public final int LINEAGE_CLASS_WIZARD = 0x03;
	static public final int LINEAGE_CLASS_DARKELF = 0x04;
	static public final int LINEAGE_CLASS_DRAGONKNIGHT = 0x05;
	static public final int LINEAGE_CLASS_BLACKWIZARD = 0x06;
	static public final int LINEAGE_CLASS_WARRIOR = 0x07;
	static public final int LINEAGE_CLASS_MONSTER = 0x0A;
	static public final int LINEAGE_CLASS_SUMMON = 0x0B;

	static public final int LINEAGE_ROYAL = 1;
	static public final int LINEAGE_KNIGHT = 2;
	static public final int LINEAGE_ELF = 4;
	static public final int LINEAGE_WIZARD = 8;
	static public final int LINEAGE_DARKELF = 16;

	// 필드 존 체크용
	static public final int NORMAL_ZONE = 0x00;
	static public final int SAFETY_ZONE = 0x10;
	static public final int COMBAT_ZONE = 0x20;

	// 만 카오틱 값 32768
	static public final int CHAOTIC = 32768;
	// 뉴트럴
	static public final int NEUTRAL = 65536;
	// 만 라이풀 값 32767
	static public final int LAWFUL = 98303;

	static public int ChatTime;

	static public int ChatTimetwo;

	// 오브젝트 gfx별 모드 값
	static public final int GFX_MODE_WALK = 0;
	static public final int GFX_MODE_ATTACK = 1;
	static public final int GFX_MODE_DAMAGE = 2;
	static public final int GFX_MODE_BREATH = 3;
	static public final int GFX_MODE_RISE = 4;
	static public final int GFX_MODE_ATTACK_R_CLAW = 5;
	static public final int GFX_MODE_DEAD = 8;
	public static final int GFX_MODE_APPEAR = 11;
	static public final int GFX_MODE_ATTACK_L_CLAW = 12;
	static public final int GFX_MODE_GET = 15;
	static public final int GFX_MODE_THROW = 16;
	static public final int GFX_MODE_WAND = 17;
	static public final int GFX_MODE_ZAP = 17;
	static public final int GFX_MODE_SPELL_DIRECTION = 18;
	static public final int GFX_MODE_SPELL_NO_DIRECTION = 19;
	static public final int GFX_MODE_OPEN = 28;
	static public final int GFX_MODE_CLOSE = 29;
	static public final int GFX_MODE_ALT_ATTACK = 30;
	static public final int GFX_MODE_SEPLL_DIRECTION_EXTRA = 31;
	static public final int GFX_MODE_DOORACTION = 32;
	static public final int GFX_MODE_SWITCH = 100;
	static public final int GFX_MODE_TYPE = 102;
	static public final int GFX_MODE_ATTR = 104;
	static public final int GFX_MODE_CLOTHES = 105;
	static public final int GFX_MODE_EFFECT = 109;
	static public final int GFX_MODE_EFFECT_NONE = 149; // 몬스터 고유 gfxmode로
														// 공격취햇을때 이팩트를 표현하면
														// 안되기때문에 표현업는 effect 값
														// 설정. 149

	// 글루딘 해골밭
	static public final int CHAOTICZONE1_X1 = 32884;
	static public final int CHAOTICZONE1_X2 = 32891;
	static public final int CHAOTICZONE1_Y1 = 32647;
	static public final int CHAOTICZONE1_Y2 = 32656;

	// 화전민 오크숲
	static public final int CHAOTICZONE2_X1 = 32664;
	static public final int CHAOTICZONE2_X2 = 32669;
	static public final int CHAOTICZONE2_Y1 = 32298;
	static public final int CHAOTICZONE2_Y2 = 32308;

	// 켄트숲
	static public final int LAWFULLZONE1_X1 = 33117;
	static public final int LAWFULLZONE1_X2 = 33128;
	static public final int LAWFULLZONE1_Y1 = 32931;
	static public final int LAWFULLZONE1_Y2 = 32942;

	// 요정숲
	static public final int LAWFULLZONE2_X1 = 33136;
	static public final int LAWFULLZONE2_X2 = 33146;
	static public final int LAWFULLZONE2_Y1 = 32236;
	static public final int LAWFULLZONE2_Y2 = 32245;

	// 요정숲
	static public final int TREEX1 = 33050;
	static public final int TREEX2 = 33058;
	static public final int TREEY1 = 32333;
	static public final int TREEY2 = 32341;

	// 낚시터
	static public final int FISHZONEX1 = 32788;
	static public final int FISHZONEX2 = 32815;
	static public final int FISHZONEY1 = 32783;
	static public final int FISHZONEY2 = 32813;

	// 8검인첸 실패 횟수
	static public int enfail_max;
	static public boolean enfail;

	// 혈맹 최대 가입인원
	static public int clan_max;
	static public int new_clan_max;
	// 신규 혈맹 이외에 다른혈맹 가입시 2중(스파이) 혈맹을 가질수 있는지 여부
	static public boolean is_two_clan_join;

	// 성혈맹 가입 여부
	static public boolean kingdom_clan_join = false;
	// 신규 혈맹 혈맹명
	static public String new_clan_name;
	static public String new_clan_name_temp;

	// 신규 혈맹 제한 레벨 이상 자동 탈퇴
	static public boolean is_new_clan_auto_out;
	// 신규 혈맹 레벨 제한
	static public int new_clan_max_level;
	// 신규 혈맹 PvP 가능 여부
	static public boolean is_new_clan_pvp;
	// 신규 혈맹 보스 공격 가능 여부
	static public boolean is_new_clan_attack_boss;
	// 신규 혈맹 오만의 탑 정상 이용 가능 여부
	static public boolean is_new_clan_oman_top;
	// 무혈 PvP 가능 여부
	static public boolean is_no_clan_pvp;
	// 무혈 보스 공격 가능 여부
	static public boolean is_no_clan_attack_boss;
	// 오만의 탑 입장 레벨
	static public int oman_min_level;

	static public double to_monster_dmg_reduc;

	// 퀘스트 종류
	static public final String QUEST_ZERO_ROYAL = "request cloak of red";
	static public final String QUEST_ROYAL_LV15 = "quest royal 15";
	static public final String QUEST_ROYAL_LV30 = "quest royal 30";
	static public final String QUEST_ROYAL_LV40 = "quest royal 40";
	static public final String QUEST_ROYAL_LV45 = "quest royal 45"; // 성환 수정
	static public final String QUEST_ROYAL_LV50 = "quest royal 50";
	static public final String QUEST_ROYAL_LV75 = "quest royal 75";
	static public final String QUEST_ROYAL_LV80 = "quest royal 80";
	// static public final String QUEST_ROYAL_LV99 = "quest royal 99";
	static public final String QUEST_KNIGHT_LV15 = "quest knight 15";
	static public final String QUEST_KNIGHT_LV30 = "quest knight 30";
	static public final String QUEST_KNIGHT_LV45 = "quest knight 45";
	static public final String QUEST_KNIGHT_LV50 = "quest knight 50";
	static public final String QUEST_KNIGHT_LV75 = "quest knight 75";
	static public final String QUEST_KNIGHT_LV80 = "quest knight 80";
	static public final String QUEST_ELF_LV15 = "quest elf 15";
	static public final String QUEST_ELF_LV30 = "quest elf 30";
	static public final String QUEST_ELF_LV45 = "quest elf 45";
	static public final String QUEST_ELF_LV50 = "quest elf 50";
	static public final String QUEST_ELF_LV75 = "quest elf 75";
	static public final String QUEST_ELF_LV80 = "quest elf 80";
	static public final String QUEST_WIZARD_LV15 = "quest wizard 15";
	static public final String QUEST_WIZARD_LV30 = "quest wizard 30";
	static public final String QUEST_WIZARD_LV45 = "quest wizard 45";
	static public final String QUEST_WIZARD_LV50 = "quest wizard 50";
	static public final String QUEST_WIZARD_LV75 = "quest wizard 75";
	static public final String QUEST_WIZARD_LV80 = "quest wizard 80";
	static public final String QUEST_DARKELF_LV15 = "quest darkelf 15";
	static public final String QUEST_DARKELF_LV30 = "quest darkelf 30";
	static public final String QUEST_DARKELF_LV45 = "quest darkelf 45";
	static public final String QUEST_DARKELF_LV50 = "quest darkelf 50";
	static public final String QUEST_DARKELF_LV75 = "quest darkelf 75";
	static public final String QUEST_DARKELF_LV80 = "quest darkelf 80";
	static public final String QUEST_TALKINGSCROLL = "quest talking scroll";
	static public final String QUEST_NOVICE = "quest novice";
	static public final String QUEST_LYRA = "quest lyra";
	static public final String QUEST_TIO = "quest amulet of valley";
	static public final String QUEST_RUBA = "amulet of island";
	static public final String QUEST_SP = "sp ";
	static public final String QUEST_LRING = "quest lring";
	static public final String QUEST_UAMULET = "quest uamulet";
	static public final String QUEST_CSPACE = "quest cspace";
	static public final String QUEST_ROBIEL = "quest robiel";

	// 공성전 이벤트 종류
	static public final int KINGDOM_WARSTATUS_START = 0; // 시작
	static public final int KINGDOM_WARSTATUS_STOP = 1; // 종료
	static public final int KINGDOM_WARSTATUS_PLAY = 2; // 진행중
	static public final int KINGDOM_WARSTATUS_3 = 3; // 주도권
	static public final int KINGDOM_WARSTATUS_4 = 4; // 차지

	// 속성 계열 정보
	static public final int ELEMENT_NONE = 0; // 공통
	static public final int ELEMENT_EARTH = 1; // 땅
	static public final int ELEMENT_FIRE = 2; // 불
	static public final int ELEMENT_WIND = 3; // 바람
	static public final int ELEMENT_WATER = 4; // 물
	static public final int ELEMENT_LASER = 5; // 레이저
	static public final int ELEMENT_POISON = 6; // 독

	// 요숲 채집npc쪽 재채집이 가능한 주기적인 시간 설정.
	static public int elf_gatherup_time;
	// 공성전 진행 주기 일 단위
	static public List<Integer> giran_kingdom_war_day_list = new ArrayList<Integer>();

	// 최소 몇인 이상 접속중인 혈맹원이 있을경우 면류관 먹을수 있는지 여부
	static public int crown_clan_min_people;

	// 켄트성 소모되지 않는 아이템 적용 여부
	static public boolean is_kent_kingdom_war_no_remove;
	// 켄트성 소모되지 않는 아이템 리스트
	static public List<String> kingdom_war_no_remove_item_kent = new ArrayList<String>();
	// 오크성 소모되지 않는 아이템 적용 여부
	static public boolean is_orcish_kingdom_war_no_remove;
	// 오크성 소모되지 않는 아이템 리스트
	static public List<String> kingdom_war_no_remove_item_orcish = new ArrayList<String>();
	// 윈다우드성 소모되지 않는 아이템 적용 여부
	static public boolean is_windawood_kingdom_war_no_remove;
	// 윈다우드성 소모되지 않는 아이템 리스트
	static public List<String> kingdom_war_no_remove_item_windawood = new ArrayList<String>();
	// 기란성 소모되지 않는 아이템 적용 여부
	static public boolean is_giran_kingdom_war_no_remove;
	// 기란성 소모되지 않는 아이템 리스트
	static public List<String> kingdom_war_no_remove_item_giran = new ArrayList<String>();
	// 하이네성 소모되지 않는 아이템 적용 여부
	static public boolean is_heine_kingdom_war_no_remove;
	// 하이네성 소모되지 않는 아이템 리스트
	static public List<String> kingdom_war_no_remove_item_heine = new ArrayList<String>();
	// 지저성 소모되지 않는 아이템 적용 여부
	static public boolean is_abyss_kingdom_war_no_remove;
	// 지저성 소모되지 않는 아이템 리스트
	static public List<String> kingdom_war_no_remove_item_abyss = new ArrayList<String>();
	// 아덴성 소모되지 않는 아이템 적용 여부
	static public boolean is_aden_kingdom_war_no_remove;
	// 아덴성 소모되지 않는 아이템 리스트
	static public List<String> kingdom_war_no_remove_item_aden = new ArrayList<String>();

	// 공성전 사용 여부
	static public boolean is_kingdom_war;
	// 면류관 주울시 몇초 뒤에 공성전 종료.
	static public int giran_kingdom_crown_min;
	// 면류관 주웠을 시 몇초마다 메세지 날릴지(초)
	static public int giran_kingdom_crown_msg_count;

	// 공성전 날짜 알림 여부
	static public boolean is_kingdom_war_notice;
	// 공성전 표현 주기.
	static public int kingdom_war_notice_delay;

	// 공성전 진행 주기 일 단위
	static public int kingdom_war_day = 3;

	static public int kingdom_boss_time;
	// 공성전 진행시간
	static public int kingdom_war_time;
	// 공성중 공성존에서 사용자가 죽엇을때 경험치 떨굴지 여부.
	static public boolean kingdom_player_dead_expdown;
	// 공성중 공성존에서 사용자가 죽엇을때 아이템을 드랍할지 여부.
	static public boolean kingdom_player_dead_itemdrop;
	// 공성전 시작 및 종료후 면류관 아이템 처리를 할지 여부.
	static public boolean kingdom_crown;
	// 공성전 중 전쟁선포된 혈맹원들끼리 pvp 시 카오처리를 할지 여부.
	static public boolean kingdom_pvp_pk;
	//
	static public boolean kingdom_war_revival;
	//
	static public boolean kingdom_war_callclan;
	//
	static public double kingdom_item_count_rate;
	//
	static public int kingdom_soldier_price;

	// 추가 켄성 공성전 설정 외부화 추가
	static public int kent_kingdom_war_hour;
	static public int kent_kingdom_war_min;

	// 추가 기 공성전 설정 외부화 추가
	static public int giran_kingdom_war_hour;
	static public int giran_kingdom_war_min;

	// npc 대화요청된후 잠깐 휴식되는 시간값.
	static public int npc_talk_stay_time;

	static public int whisper_effect;

	static public int singyu_clan_level;
	static public boolean singyu_shop;

	static public int Orim_en_level;

	static public int bow_target_armor_flee;

	static public int target_armor_flee;

	// 성환.클랜리덕 외부화
	static public int Clan_numone;

	static public int Clan_Redone;

	static public int Clan_numtwo;

	static public int Clan_Redtwo;
	// 성환 클랜리덕-외부화
	static public int Clan_numthree;

	static public int Clan_Redthree;

	static public int Clan_numfour;

	static public int Clan_Redfour;

	static public boolean is_quest_present; // Level 보상 여부
	static public boolean is_level_quest; // 레벨퀘스트
	static public boolean is_repeat_quest;

	static public double mon_att_tar_r_b;
	static public double mon_att_tar_r1;
	static public double mon_att_tar_r2;
	static public double mon_att_non_r1;
	static public double mon_att_non_r2;
	static public double mon_att1;
	static public double mon_att2;
	static public double eng_bolt;
	static public double light;
	static public double tornado;
	static public double chill;
	static public double freezing;
	static public double disslight;
	static public double diss;
	static public double jangin_armor_6;
	static public double jangin_armor_7;
	static public double jangin_armor_8;
	static public double jangin_armor_9;
	static public double jangin_weapon_9;

	// 자동사냥 방지 사용 여부
	static public boolean is_auto_hunt_check;
	// 자동사냥 체크를 위한 몬스터 킬 수
	static public int auto_hunt_monster_kill_count;
	// 자동사냥 문자 입력안할시 마법 사용여부(true: 유저에게도 마법 사용 불가 / false: 마법은 체크안함)
	static public boolean is_auto_hunt_check_skill;
	// 인증번호 받을 시 몇초 이내에 답변을 해야 공격가능
	static public int auto_hunt_answer_time;

	static public int clan_exp_user;

	static public int last_dungeon1;
	static public int last_dungeon2;
	static public int last_dungeon3;

	static public int pklevel;

	static public boolean last_dungeon;

	public static List<FirstInventory> quiz_question_present = new ArrayList<FirstInventory>();
	// 아이템 제거 막대로 제거 불가능한 아이템
	static public List<String> no_remove_item = new ArrayList<String>();
	// 요정숲 정령의돌 최대스폰 갯수.
	static public int elvenforest_elementalstone_spawn_count;
	// 요정숲 정령의돌 스폰 주기 시간값.
	static public int elvenforest_elementalstone_spawn_time;
	// 요정숲 정령의돌 스폰갯수 지정값.
	static public int elvenforest_elementalstone_min_count;
	static public int elvenforest_elementalstone_max_count;

	// 군주
	static public List<FirstSpawn> royal_spawn = new ArrayList<FirstSpawn>();
	static public int royal_male_gfx;
	static public int royal_female_gfx;
	static public int royal_hp;
	static public int royal_mp;
	static public int royal_max_hp;
	static public int royal_max_mp;
	static public List<FirstSpell> royal_first_spell = new ArrayList<FirstSpell>();
	static public List<FirstInventory> royal_first_inventory = new ArrayList<FirstInventory>();
	static public int royal_stat_str;
	static public int royal_stat_con;
	static public int royal_stat_dex;
	static public int royal_stat_wis;
	static public int royal_stat_cha;
	static public int royal_stat_int;
	static public int royal_stat_dice;

	// 기사
	static public List<FirstSpawn> knight_spawn = new ArrayList<FirstSpawn>();
	static public int knight_male_gfx;
	static public int knight_female_gfx;
	static public int knight_hp;
	static public int knight_mp;
	static public int knight_max_hp;
	static public int knight_max_mp;
	static public List<FirstSpell> knight_first_spell = new ArrayList<FirstSpell>();
	static public List<FirstInventory> knight_first_inventory = new ArrayList<FirstInventory>();
	static public int knight_stat_str;
	static public int knight_stat_con;
	static public int knight_stat_dex;
	static public int knight_stat_wis;
	static public int knight_stat_cha;
	static public int knight_stat_int;
	static public int knight_stat_dice;

	// 요정
	static public List<FirstSpawn> elf_spawn = new ArrayList<FirstSpawn>();
	static public int elf_male_gfx;
	static public int elf_female_gfx;
	static public int elf_hp;
	static public int elf_mp;
	static public int elf_max_hp;
	static public int elf_max_mp;
	static public List<FirstSpell> elf_first_spell = new ArrayList<FirstSpell>();
	static public List<FirstInventory> elf_first_inventory = new ArrayList<FirstInventory>();
	static public int elf_stat_str;
	static public int elf_stat_con;
	static public int elf_stat_dex;
	static public int elf_stat_wis;
	static public int elf_stat_cha;
	static public int elf_stat_int;
	static public int elf_stat_dice;

	// 마법사 정보
	static public List<FirstSpawn> wizard_spawn = new ArrayList<FirstSpawn>();
	static public int wizard_male_gfx;
	static public int wizard_female_gfx;
	static public int wizard_hp;
	static public int wizard_mp;
	static public int wizard_max_hp;
	static public int wizard_max_mp;
	static public List<FirstSpell> wizard_first_spell = new ArrayList<FirstSpell>();
	static public List<FirstInventory> wizard_first_inventory = new ArrayList<FirstInventory>();
	static public int wizard_stat_str;
	static public int wizard_stat_con;
	static public int wizard_stat_dex;
	static public int wizard_stat_wis;
	static public int wizard_stat_cha;
	static public int wizard_stat_int;
	static public int wizard_stat_dice;

	// 다크엘프 정보
	static public List<FirstSpawn> darkelf_spawn = new ArrayList<FirstSpawn>();
	static public int darkelf_male_gfx;
	static public int darkelf_female_gfx;
	static public int darkelf_hp;
	static public int darkelf_mp;
	static public int darkelf_max_hp;
	static public int darkelf_max_mp;
	static public List<FirstSpell> darkelf_first_spell = new ArrayList<FirstSpell>();
	static public List<FirstInventory> darkelf_first_inventory = new ArrayList<FirstInventory>();
	static public int darkelf_stat_str;
	static public int darkelf_stat_con;
	static public int darkelf_stat_dex;
	static public int darkelf_stat_wis;
	static public int darkelf_stat_cha;
	static public int darkelf_stat_int;

	// 용기사 정보
	static public List<FirstSpawn> dragonknight_spawn = new ArrayList<FirstSpawn>();
	static public int dragonknight_male_gfx;
	static public int dragonknight_female_gfx;
	static public int dragonknight_hp;
	static public int dragonknight_mp;
	static public int dragonknight_max_hp;
	static public int dragonknight_max_mp;
	static public List<FirstSpell> dragonknight_first_spell = new ArrayList<FirstSpell>();
	static public List<FirstInventory> dragonknight_first_inventory = new ArrayList<FirstInventory>();
	static public int dragonknight_stat_str;
	static public int dragonknight_stat_con;
	static public int dragonknight_stat_dex;
	static public int dragonknight_stat_wis;
	static public int dragonknight_stat_cha;
	static public int dragonknight_stat_int;

	// 환술사 정보
	static public List<FirstSpawn> blackwizard_spawn = new ArrayList<FirstSpawn>();
	static public int blackwizard_male_gfx;
	static public int blackwizard_female_gfx;
	static public int blackwizard_hp;
	static public int blackwizard_mp;
	static public int blackwizard_max_hp;
	static public int blackwizard_max_mp;
	static public List<FirstSpell> blackwizard_first_spell = new ArrayList<FirstSpell>();
	static public List<FirstInventory> blackwizard_first_inventory = new ArrayList<FirstInventory>();
	static public int blackwizard_stat_str;
	static public int blackwizard_stat_con;
	static public int blackwizard_stat_dex;
	static public int blackwizard_stat_wis;
	static public int blackwizard_stat_cha;
	static public int blackwizard_stat_int;

	// 전사 정보
	static public List<FirstSpawn> warrior_spawn = new ArrayList<FirstSpawn>();
	static public int warrior_male_gfx;
	static public int warrior_female_gfx;
	static public int warrior_hp;
	static public int warrior_mp;
	static public int warrior_max_hp;
	static public int warrior_max_mp;
	static public List<FirstSpell> warrior_first_spell = new ArrayList<FirstSpell>();
	static public List<FirstInventory> warrior_first_inventory = new ArrayList<FirstInventory>();
	static public int warrior_stat_str;
	static public int warrior_stat_con;
	static public int warrior_stat_dex;
	static public int warrior_stat_wis;
	static public int warrior_stat_cha;
	static public int warrior_stat_int;

	// wanted add stat
	static public int Wanted_addDmg;
	static public int Wanted_addDmg2;

	// 인벤토리 최대 갯수
	static public int inventory_max;

	// 인벤토리에 아이템 무게게이지 최대값
	static public int inventory_weight_max;

	// 최대 렙업값
	static public int level_max;

	// 파티원 최대수
	static public int party_max;

	static public int doll_death_effect_persent;
	static public int doll_death_effect_mindmg;
	static public int doll_death_effect_maxdmg;

	// 창고이용 레벨
	static public int warehouse_level;
	// 창고 아이템 찾을때 비용
	static public int warehouse_price;
	// 창고에 등록가능한 최대값
	static public int warehouse_max;
	// 창고 아이템 찾을때 비용 - 요정숲
	static public int warehouse_price_elf;

	// 펫 찾을때 비용
	static public int warehouse_pet_price;
	// 펫 사용자에게 데미지 가할때 1/3으로 들어가게 할지 여부.
	static public boolean pet_damage_to_player;
	// 펫 길들일 수 있게 할지 여부.
	static public boolean pet_tame_is = true;
	// 인챈트 복구
	static public int recovery_weapon_safe_0_en_min;
	static public int recovery_weapon_safe_6_en_min;
	static public int recovery_armor_safe_0_en_min;
	static public int recovery_armor_safe_4_en_min;
	static public int recovery_armor_safe_6_en_min;
	static public int recovery_time;
	static public boolean is_recovery_scroll;

	static public String weapon_safe_0_0_recovery_item;
	static public int weapon_safe_0_0_recovery_item_count;
	static public String weapon_safe_0_1_recovery_item;
	static public int weapon_safe_0_1_recovery_item_count;
	static public String weapon_safe_0_2_recovery_item;
	static public int weapon_safe_0_2_recovery_item_count;
	static public String weapon_safe_0_3_recovery_item;
	static public int weapon_safe_0_3_recovery_item_count;
	static public String weapon_safe_0_4_recovery_item;
	static public int weapon_safe_0_4_recovery_item_count;
	static public String weapon_safe_0_5_recovery_item;
	static public int weapon_safe_0_5_recovery_item_count;
	static public String weapon_safe_0_6_recovery_item;
	static public int weapon_safe_0_6_recovery_item_count;
	static public String weapon_safe_0_7_recovery_item;
	static public int weapon_safe_0_7_recovery_item_count;
	static public String weapon_safe_0_8_recovery_item;
	static public int weapon_safe_0_8_recovery_item_count;
	static public String weapon_safe_0_9_recovery_item;
	static public int weapon_safe_0_9_recovery_item_count;
	static public String weapon_safe_0_10_recovery_item;
	static public int weapon_safe_0_10_recovery_item_count;

	static public String weapon_safe_6_6_recovery_item;
	static public int weapon_safe_6_6_recovery_item_count;
	static public String weapon_safe_6_7_recovery_item;
	static public int weapon_safe_6_7_recovery_item_count;
	static public String weapon_safe_6_8_recovery_item;
	static public int weapon_safe_6_8_recovery_item_count;
	static public String weapon_safe_6_9_recovery_item;
	static public int weapon_safe_6_9_recovery_item_count;
	static public String weapon_safe_6_10_recovery_item;
	static public int weapon_safe_6_10_recovery_item_count;
	static public String weapon_safe_6_11_recovery_item;
	static public int weapon_safe_6_11_recovery_item_count;
	static public String weapon_safe_6_12_recovery_item;
	static public int weapon_safe_6_12_recovery_item_count;
	static public String weapon_safe_6_13_recovery_item;
	static public int weapon_safe_6_13_recovery_item_count;
	static public String weapon_safe_6_14_recovery_item;
	static public int weapon_safe_6_14_recovery_item_count;
	static public String weapon_safe_6_15_recovery_item;
	static public int weapon_safe_6_15_recovery_item_count;

	static public String armor_safe_0_0_recovery_item;
	static public int armor_safe_0_0_recovery_item_count;
	static public String armor_safe_0_1_recovery_item;
	static public int armor_safe_0_1_recovery_item_count;
	static public String armor_safe_0_2_recovery_item;
	static public int armor_safe_0_2_recovery_item_count;
	static public String armor_safe_0_3_recovery_item;
	static public int armor_safe_0_3_recovery_item_count;
	static public String armor_safe_0_4_recovery_item;
	static public int armor_safe_0_4_recovery_item_count;
	static public String armor_safe_0_5_recovery_item;
	static public int armor_safe_0_5_recovery_item_count;
	static public String armor_safe_0_6_recovery_item;
	static public int armor_safe_0_6_recovery_item_count;
	static public String armor_safe_0_7_recovery_item;
	static public int armor_safe_0_7_recovery_item_count;
	static public String armor_safe_0_8_recovery_item;
	static public int armor_safe_0_8_recovery_item_count;
	static public String armor_safe_0_9_recovery_item;
	static public int armor_safe_0_9_recovery_item_count;
	static public String armor_safe_0_10_recovery_item;
	static public int armor_safe_0_10_recovery_item_count;

	static public String armor_safe_4_4_recovery_item;
	static public int armor_safe_4_4_recovery_item_count;
	static public String armor_safe_4_5_recovery_item;
	static public int armor_safe_4_5_recovery_item_count;
	static public String armor_safe_4_6_recovery_item;
	static public int armor_safe_4_6_recovery_item_count;
	static public String armor_safe_4_7_recovery_item;
	static public int armor_safe_4_7_recovery_item_count;
	static public String armor_safe_4_8_recovery_item;
	static public int armor_safe_4_8_recovery_item_count;
	static public String armor_safe_4_9_recovery_item;
	static public int armor_safe_4_9_recovery_item_count;
	static public String armor_safe_4_10_recovery_item;
	static public int armor_safe_4_10_recovery_item_count;
	static public String armor_safe_4_11_recovery_item;
	static public int armor_safe_4_11_recovery_item_count;
	static public String armor_safe_4_12_recovery_item;
	static public int armor_safe_4_12_recovery_item_count;

	static public String armor_safe_6_6_recovery_item;
	static public int armor_safe_6_6_recovery_item_count;
	static public String armor_safe_6_7_recovery_item;
	static public int armor_safe_6_7_recovery_item_count;
	static public String armor_safe_6_8_recovery_item;
	static public int armor_safe_6_8_recovery_item_count;
	static public String armor_safe_6_9_recovery_item;
	static public int armor_safe_6_9_recovery_item_count;
	static public String armor_safe_6_10_recovery_item;
	static public int armor_safe_6_10_recovery_item_count;
	static public String armor_safe_6_11_recovery_item;
	static public int armor_safe_6_11_recovery_item_count;
	static public String armor_safe_6_12_recovery_item;
	static public int armor_safe_6_12_recovery_item_count;

	// 순수스탯 최대값
	static public int stat_max;
	//
	static public boolean stat_base;

	// 여관방 최대갯수
	static public int inn_max;
	// 여관방 최대접근 인원수
	static public int inn_in_max;
	// 여관방 대여 비용
	static public int inn_price;
	// 여관 대여 시간
	static public int inn_time;
	// 여관 홀 최대갯수
	static public int inn_hall_max;
	// 여관 홀 최대접근 인원수
	static public int inn_hall_in_max;
	// 여관 홀 대여 비용
	static public int inn_hall_price;

	// 게시판 글 작성 가격.
	static public int board_write_price;
	// 랭킹 게시판 업데이트 딜레이.
	static public int board_rank_update_delay;

	// 랭킹이 적용될 최소 레벨
	static public int rank_min_level;
	static public int rank_min_level2;
	// 랭킹 순위에 따른 등급
	static public int rank_class_1;
	static public int rank_class_2;
	static public int rank_class_3;
	static public int rank_class_4;
	// 랭킹 변신 사용 여부
	static public boolean is_rank_poly;
	// 랭커 변신을 할수있는 순위
	static public int rank_poly_all;
	static public int rank_poly_class;

	// 슬라임 레이스표 가격 설정
	static public int slime_race_price;
	// 강아지 레이스표 가격 설정
	static public int dog_race_price;

	// 배율
	static public double rate_enchant = 1;
	static public double rate_drop = 1;
	static public double rate_exp = 1;
	static public double rate_lawful = 1;
	static public double rate_aden = 1;
	static public double rate_party = 1;
	static public double rate_exp_pet = 1;

	// 채팅 레벨설정
	static public int chatting_level_global;
	static public int chatting_level_normal;
	static public int chatting_level_whisper;
	static public int chatting_level_trade;

	// pvp 설정
	static public boolean nonpvp;

	static public boolean tripc; // 자동트리 사람
	static public boolean trimob; // 자동트리 몹

	static public boolean stunpc; // 자동스턴 사람
	static public boolean stunmob; // 자동스턴 몹

	// 계정 자동생성 여부.
	static public boolean account_auto_create;
	// ip 당 소유가능한 계정 값.
	static public int account_ip_count;

	// 정액제 활성화 여부.
	static public boolean flat_rate;
	// 신규생성 계정에 대한 정액시간값. 분단위
	static public int flat_rate_price;

	// 에스메랄다 미래보기 유지시간을 몇초로 할지. 초단위
	static public int esmereld_sec;

	// 기본 세율 값
	static public int min_tax;
	static public int max_tax;

	// /누구 명령어 구성표
	static public String object_who;
	static public String object_who2;

	// 드랍된 아이템 유지시간값. 해당시간이 오버되면 제거됨.
	static public int world_item_delay;

	// 오토루팅 활성화 여부.
	static public boolean auto_pickup;
	// 오토루팅 아데나 활성화 여부.
	static public boolean auto_pickup_aden;
	// 오토루팅 보스 활성화 여부
	static public boolean auto_pickup_boss;
	// 오토루팅 퍼센트 범위.
	static public int auto_pickup_percent;

	// 덱방 활성화 여부
	static public boolean is_dex_ac;

	// 드랍 찬스 범위 최대값.
	static public double chance_max_drop;

	// 죽엇을때 아이템 떨굴지 여부.
	static public boolean player_dead_itemdrop;
	// 죽엇을때 경험치 떨굴지 여부.
	static public boolean player_dead_expdown;
	// 죽엇을때 경험치를 가격자들에게 줄지 여부.
	static public boolean player_dead_exp_gift;
	//
	static public int player_worldout_time;
	//
	static public int player_dead_expdown_level;
	static public int player_dead_itemdrop_level;
	//
	static public int player_hellsystem_pk_count;
	static public int player_hellsystem_delay;

	//
	static public double player_dead_expdown_rate;
	static public double player_lost_exp_rate;
	static public int player_lost_exp_aden_rate;

	static public int SEARCH_LOCATIONRANGE = 14; // 주변셀 검색 범위
	static public int SEARCH_MONSTER_TARGET_LOCATION = 20; // 몬스터가 타겟을 쫒아가는 검색
	static public int SEARCH_ROBOT_TARGET_LOCATION = 35; // 몬스터가 타겟을 쫒아가는 검색 범위
	static public int SEARCH_WORLD_LOCATION = 40; // 전체 객체 검색을 시도할 범위

	// 배고품게이지 최대값
	static public final int MAX_FOOD = 225;
	// 사용자 케릭 죽엇을때 세팅할 배고품게이지 값
	public final static int MIN_FOOD = 40;

	// 혈맹처리 에 참고 정보
	static public final int CLAN_MAKE_LEV = 5;
	static public final int CLAN_NAME_MIN_SIZE = 1;
	static public final int CLAN_NAME_MAX_SIZE = 20;

	// 최대 ac
	static public final int MAX_AC = 138; // -128

	// door 오픈 유지값.
	static public int door_open_delay = 4;

	// 몬스터 hp 바 보게할지 여부.
	static public boolean monster_interface_hpbar;
	// npc hp bar 보이게할지 여부.
	static public boolean npc_interface_hpbar;

	// 무기 손상도 최대치값.
	static public int item_durability_max;
	// 아이템 착용 처리 변수 (old(false), new(true))
	static public boolean item_equipped_type;
	// 축복받은 악세서리들에 아이템은 인첸트가 가능하도록할지 여부.
	static public boolean item_accessory_bless_enchant;
	static public int item_acc_en;
	// 축복받은 변신 주문서일경우 레벨제한을 해제할지 여부.
	static public boolean item_polymorph_bless;
	// 축인첸시 대상 아이템이 확률적 축아이템으로 변경되게할지 여부.
	static public boolean item_enchant_bless = true;
	// 아이템 랜덤 추출쪽 확률 체크용 변수.
	static public int item_bundle_chance = 100;
	// 엘릭서 복용 최대 갯수.
	static public int item_elixir_max = 5;
	// 엘릭서 사용 최소 레벨
	static public int elixir_min_level;
	// 인첸 최대값 설정.
	static public int item_enchant_armor_max = 0;
	static public int item_enchant_weapon_max = 0;
	// 안전 0 최태값 설정
	static public int item_enchant_0armor_max = 0;
	static public int item_enchant_0weapon_max = 0;
	// 고인첸 갯수 제한 설정.
	static public int[] item_enchant_weapon_top = new int[255];
	static public int[] item_enchant_armor_top = new int[255];
	// 마법인형 소환가능한 최대갯수 설정.
	static public int item_magicdoll_max = 1;
	// 마법인형이 소환자와 유지할 거리.
	static public int magicdoll_location = 2;
	//
	static public boolean item_delay_global;
	//
	static public boolean item_spellscroll_delay;
	// 경험치 이벤트
	static public boolean event_exp;

	// 쓰레드 설정
	// 이벤트 쓰레드 갯수.
	static public int thread_event = 6;
	static public int thread_ai = 4;

	// 클라 설정
	// 클라이언트 핑체크 시간값
	static public int client_ping_time = 70;

	// 공지사항 표현 주기.
	static public int notice_delay;

	// 혈맹 레벨 외부화
	static public int One_Clan_Level;
	static public int Two_Clan_Level;
	static public int Three_Clan_Level;
	static public int Four_Clan_Level;
	static public int Five_Clan_Level;
	static public int Six_Clan_Level;
	static public int Seven_Clan_Level;

	// 이벤트 들
	static public boolean event_poly; // 변신 이벤트
	static public boolean event_rank_poly; // 변신 이벤트
	static public boolean event_buff; // 버프 자동지급 이벤트
	static public boolean event_illusion; // 환상 이벤트
	static public boolean event_christmas; // 크리스마스 이벤트
	static public boolean event_halloween; // 할로윈 이벤트
	static public boolean event_lyra; // 라이라 토템 이벤트
	static public boolean event_ghosthouse; // 유령의집 이벤트
	static public boolean event_lucky; // 운세시스템 이벤트
	static public boolean event_kujak; // 현상범 쿠작 이벤트

	static public boolean autohunt_onoff = true;
	// 추가
	static public boolean event_mon;
	static public boolean event_vil;

	// 가속 아이템 프레임
	static public boolean bravery_potion_frame;
	static public boolean elven_wafer_frame;
	static public boolean holywalk_frame;
	static public boolean devil_potion_frame;

	// 오픈대기 아이템 자동지급
	static public boolean is_world_open_wait_item;
	static public String world_open_wait_item;
	static public int world_open_wait_item_min;
	static public int world_open_wait_item_max;
	static public int world_open_wait_item_delay;

	// 프리미엄 아이템 자동지급
	static public boolean world_premium_item_is;
	static public String world_premium_item;
	static public int world_premium_rate;
	static public int world_premium_item_delay;
	static public int world_premium_item_level;
	static public double world_premium_item_clan_rate;
	static public double world_premium_item_kingdom_rate;
	// 픽시의 깃털 자동지급을 위해 변수 추가
	static public String world_premium_item1;
	static public int world_premium_item_min1;
	static public int world_premium_item_max1;

	static public boolean robot_auto_tax;
	//
	static public int robot_auto_pc_chance_max_drop;
	// 무인케릭 사용유무
	static public boolean robot_auto_pc;
	// 무인케릭 아이템 사용 목록.
	static public String robot_auto_pc_healingpotion;
	static public int robot_auto_pc_healingpotion_cnt;
	static public String robot_auto_pc_hastepotion;
	static public int robot_auto_pc_hastepotion_cnt;
	static public String robot_auto_pc_braverypotion;
	static public int robot_auto_pc_braverypotion_cnt;
	static public String robot_auto_pc_elvenwafer;
	static public int robot_auto_pc_elvenwafer_cnt;
	static public String robot_auto_pc_scrollpolymorph;
	static public int robot_auto_pc_scrollpolymorph_cnt;
	static public String robot_auto_pc_arrow;
	static public int robot_auto_pc_arrow_cnt;
	// 무인케릭 휴식 시간 초단위
	static public int robot_auto_pc_stay_time;
	// 헤이스트샵 이용 금액
	static public int robot_auto_haste_aden;

	// 해당 값보다 높은인첸이 이뤄졌을경우 월드에 메세지 표현처리함.
	static public int world_message_enchant_weapon;
	static public int world_message_enchant_armor;
	// 사용자들이 월드에 접속시 접속했다는 메세지를 전체 유저에게 알릴지 여부.
	static public boolean world_message_join;

	// mr 최대치값 설정.
	static public int max_mr = 120;
	static public int max_damage_mr = 160;

	// 메모리 재사용에 사용될 객체 적재 최대 갯수 값.
	static public int pool_max = 30000;
	// 메모리 재사용 기능 사용 여부.
	static public boolean pool_astar = true;
	static public boolean pool_eventthread = true;
	static public boolean pool_aithread = true;

	// 일정 시간마다 월드맵을 청소할지 여부
	static public boolean is_world_clean;
	// 월드맵 청소시 알림 여부
	static public boolean is_world_clean_message;
	// 월드맵 청소 시간(분)
	static public int world_clean_time;
	// 월드맵 청소전 알림 시간(초)
	static public int world_clean_message_time;

	// 콜롯세움 설정 값들
	static public boolean colosseum_giran = false;
	static public boolean colosseum_talkingisland = false;
	static public boolean colosseum_silverknighttown = false;
	static public boolean colosseum_gludin = false;
	static public boolean colosseum_windawood = false;
	static public boolean colosseum_kent = false;

	// 접속자수 표현시 사용되는 변수.
	static public double world_player_count = 1.0d;
	// 플레이어 수 임의 지정 값
	static public int world_player_count_init = 0;

	//
	static public boolean party_autopickup_item_print = false;
	static public boolean party_pickup_item_print = false;
	static public int party_exp_level_range = 0;

	// 현상수배 변수.
	static public boolean wanted_clan = false;
	static public String wanted_name = "(수배)";
	static public int wanted_level_min = 0;
	static public int wanted_level_max = 0;
	static public int wanted_price_min = 0;
	static public int wanted_price_max = 0;
	static public int wanted_price_min49 = 0;
	static public int wanted_price_max49 = 0;
	static public int wanted_price_min50 = 0;
	static public int wanted_price_max50 = 0;
	static public int wanted_price_min51 = 0;
	static public int wanted_price_max51 = 0;
	static public int wanted_price_min52 = 0;
	static public int wanted_price_max52 = 0;

	static public double buff_exp;

	// 스킬설정 변수.
	static public boolean skill_ShapeChange = true;
	static public boolean skill_StormWalk = true;
	static public boolean skill_EarthJail = true;
	static public boolean skill_Haste_update = false;
	static public boolean skill_Bravery_update = false;
	static public boolean skill_Wafer_update = false;

	//
	static public int letter_price = 50;
	static public int letter_clan_price = 1000;

	// 힐링포션 복용시 메세지를 표현할 지 여부.
	static public boolean healingpotion_message = true;

	//
	static public boolean user_command = true;

	// 경매 남은시간 값. 3일로 설정됨.
	static public long auction_delay = 259200 * 1000;

	// pvp 변수.
	static public boolean pvp_print_message = false;

	// 스핵 경고 최대치.
	static public boolean speedhack = true;
	static public int speedhack_warning_count = 10;
	static public boolean speedhack_stun;
	static public int speed_bad_packet_count;
	static public int speed_good_packet_count;
	static public boolean is_attack_count_packet;
	static public int speed_hack_block_time;
	static public int speed_hack_message_count;
	static public double speed_check_walk_frame_rate;
	static public double speed_check_attack_frame_rate;
	static public int delay_hack_count;
	static public double delay_hack_frame_rate;
	static public int gost_hack_block_time;

	static public double speed_check_no_dir_magic_frame_rate;
	static public double speed_check_dir_magic_frame_rate;

	// 칼질 & 휠 마법 안될시 카운트
	static public int attackAndMagic_delay;

	// 마법인형 텔레포트 이팩트 값.
	static public int doll_teleport_effect = 5935;
	// 마법인형 이팩트
	static public int doll_mana_effect = 6321;
	// 기본 객체 텔레포트시 사용될 이팩트 값.
	static public int object_teleport_effect = 169;

	// 허수아비 대미지 DPS 알림 여부
	static public boolean view_cracker_dps = false;
	// 허수아비 데미지 멘트 확인
	static public boolean view_cracker_damage = false;
	// 허수아비 경험치 적용 최대 레벨
	static public int cracker_exp_max_level;

	// 자동스턴 적용 레벨
	static public int stun_level;
	// 자동트리플 적용 레벨
	static public int triple_level;

	static public boolean clan_warehouse_message = false;

	//
	static public boolean character_delete = true;
	static public long auto_save_time = 0;

	//
	static public boolean memory_recycle = true;

	//
	static public boolean dungeon_parttime_message = false;

	// gui
	static public boolean chatting_all_lock = false;
	static public boolean chatting_global_lock = false;

	//
	static public String rank_filter_names_query = null;

	// 죽어도 경험치를 잃어버리지 않는 맵
	static public int MAP_EXP_NOT_LOSE[] = { 5, 70, 303, 5083, 509 };

	// 죽어도 아이템을 드랍하지 않는 맵
	static public int MAP_ITEM_NOT_DROP[] = { 5, 70, 303, 5083, 509 };

	// 수중 맵 목록
	static public int MAP_AQUA[] = { 63, 65, 253, 420, 558, 604 };

	// 서먼객체가 이동 불가능한 맵. 또는 소환 불가능한 맵
	static public int SummonTeleportImpossibleMap[] = { 57, 63, 65, 72, 73, 74, 5140, 5083, 5124, 208, 201 };

	// 펫이 이동 불가능한 맵. (사용자가 텔레포트할때 해당 값을 확인해서 처리할지 여부 구분함.)
	static public int PetTeleportImpossibleMap[] = { 6, 22, 57, 63, 65, 83, 84, 88, 89, 90, 91, 92, 93, 94, 95, 96,
			97, 98, 201, 202, 203, 204, 209, 210, 211, 212, 213, 214, 215, 216, 221, 222, 223, 224, 225, 226, 227, 228,
			613, 5140, 5083, 208, 340, 350, 360, 370, 5124 };// 20181021 613추가

	// 랜덤 텔레포트 가능한 맵 여기에요 괄혼안에 숫자가 맵인데
	static public int TeleportPossibleMap[] = { 0, 1, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
			24, 23, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
			51, 52, 53, 54, 55, 56, 58, 59, 60, 61, 62, 63, 64, 68, 69, 75, 76, 77, 78, 79, 80, 81, 84, 85,
			86, 209, 210, 211, 212, 213, 214, 215, 216, 300, 301, 304, 400, 401, 420, 440, 445, 480, 522, 523, 524, 777,
			814, 2005 };
	static public int TeleportPossibleMapLength = TeleportPossibleMap.length;

	// 귀환 및 축순, 이반 불가능한 맵
	static public final int TeleportHomeImpossibilityMap[] = { 57, 70, 88, 89, 99, 200, 509, 666, 1400, 5167, 340, 350,
			360, 370, 621, 5124, 800, 5001 };
	static public final int TeleportHomeImpossibilityMapLength = TeleportHomeImpossibilityMap.length;

	// 콜클랜 및 런클랜 시전시 불가능한 맵.
	static public int notPledgeMemberMap[] = { 88, 777, 778, 779, 521, 522, 523, 524, 5083, 208, 621, 5124 };
	static public int notPledgeMemberMapLength = notPledgeMemberMap.length;

	static public int marketmap[] = { 33407, 33471, 32756, 32846 };

	// 각 성별 외성 내부 좌표값
	static public final int KINGDOMLOCATION[][] = { { 0, 0, 0, 0, 0, 0 },
			{ 33089, 33219, 32717, 32827, 4, KINGDOM_KENT }, { 32750, 32850, 32250, 32350, 4, KINGDOM_ORCISH },
			{ 32571, 32721, 33350, 33460, 4, KINGDOM_WINDAWOOD }, { 33559, 33686, 32615, 32755, 4, KINGDOM_GIRAN },
			{ 33458, 33583, 33315, 33490, 4, KINGDOM_HEINE }, { 32755, 32870, 32790, 32920, 66, KINGDOM_ABYSS },
			{ 34007, 34162, 33172, 33332, 4, KINGDOM_ADEN }, { 32888, 33070, 32839, 32953, 320, 0 }, };

	// 아지트 좌표값
	static public final int AGITLOCATION[][] = { { 33368, 33375, 32651, 32654 }, // 기란,
																					// 1번,
																					// 78평
			{ 33373, 33375, 32655, 32657 }, { 33381, 33387, 32653, 32656 }, // 기란,
																			// 2번,
																			// 45평
			{ 33392, 33404, 32650, 32657 }, // 기란, 3번, 120평
			{ 33427, 33430, 32656, 32662 }, // 기란, 4번, 45평
			{ 33442, 33445, 32665, 32672 }, // 기란, 5번, 78평
			{ 33439, 33441, 32665, 32667 }, { 33454, 33466, 32648, 32655 }, // 기란,
																			// 6번,
																			// 120평
			{ 33476, 33479, 32665, 32671 }, // 기란, 7번, 45평
			{ 33474, 33477, 32678, 32685 }, // 기란, 8번, 78평
			{ 33471, 33473, 32678, 32680 }, { 33453, 33460, 32694, 32697 }, // 기란,
																			// 9번,
																			// 78평
			{ 33458, 33460, 32698, 32700 }, { 33421, 33433, 32685, 32692 }, // 기란,
																			// 10번,
																			// 120평
			{ 33412, 33415, 32674, 32681 }, // 기란, 11번, 78평
			{ 33409, 33411, 32674, 32676 }, { 33414, 33421, 32703, 32706 }, // 기란,
																			// 12번,
																			// 78평
			{ 33419, 33421, 32707, 32709 }, { 33372, 33384, 32692, 32699 }, // 기란,
																			// 13번,
																			// 120평
			{ 33362, 33365, 32681, 32687 }, // 기란, 14번, 45평
			{ 33363, 33366, 32669, 32676 }, // 기란, 15번, 78평
			{ 33360, 33362, 32669, 32671 }, { 33344, 33347, 32660, 32667 }, // 기란,
																			// 16번,
																			// 78평
			{ 33341, 33343, 32660, 32662 }, { 33345, 33348, 32672, 32678 }, // 기란,
																			// 17번,
																			// 45평
			{ 33338, 33350, 32704, 32711 }, // 기란, 18번, 120평
			{ 33349, 33356, 32728, 32731 }, // 기란, 19번, 78평
			{ 33354, 33356, 32732, 32734 }, { 33369, 33372, 32713, 32720 }, // 기란,
																			// 20번,
																			// 78평
			{ 33366, 33368, 32713, 32715 }, { 33380, 33383, 32712, 32718 }, // 기란,
																			// 21번,
																			// 45평
			{ 33401, 33413, 32733, 32740 }, // 기란, 22번, 120평
			{ 33427, 33430, 32717, 32724 }, // 기란, 23번, 78평
			{ 33424, 33426, 32717, 32719 }, { 33448, 33451, 32729, 32735 }, // 기란,
																			// 24번,
																			// 45평
			{ 33404, 33407, 32754, 32760 }, // 기란, 25번, 45평
			{ 33363, 33375, 32755, 32762 }, // 기란, 26번, 120평
			{ 33354, 33357, 32774, 32781 }, // 기란, 27번, 78평
			{ 33351, 33353, 32774, 32776 }, { 33355, 33361, 32787, 32790 }, // 기란,
																			// 28번,
																			// 45평
			{ 33366, 33373, 32786, 32789 }, // 기란, 29번, 78평
			{ 33371, 33373, 32790, 32792 }, { 33383, 33386, 32773, 32779 }, // 기란,
																			// 30번,
																			// 45평
			{ 33397, 33404, 32788, 32791 }, // 기란, 31번, 78평
			{ 33402, 33404, 32792, 32794 }, { 33479, 33486, 32788, 32791 }, // 기란,
																			// 32번,
																			// 78평
			{ 33484, 33486, 32792, 32794 }, { 33498, 33501, 32801, 32807 }, // 기란,
																			// 33번,
																			// 45평
			{ 33379, 33385, 32802, 32805 }, // 기란, 34번, 45평
			{ 33373, 33385, 32822, 32829 }, // 기란, 35번, 120평
			{ 33398, 33401, 32810, 32816 }, // 기란, 36번, 45평
			{ 33400, 33403, 32821, 32828 }, // 기란, 37번, 78평
			{ 33397, 33399, 32821, 32823 }, { 33431, 33438, 32838, 32841 }, // 기란,
																			// 38번,
																			// 78평
			{ 33436, 33438, 32842, 32844 }, { 33457, 33463, 32832, 32835 }, // 기란,
																			// 39번,
																			// 45평
			{ 33385, 33392, 32845, 32848 }, // 기란, 40번, 78평
			{ 33390, 33392, 32849, 32851 }, { 33402, 33405, 32589, 32866 }, // 기란,
																			// 41번,
																			// 78평
			{ 33399, 33401, 32859, 32861 }, { 33414, 33417, 32850, 32856 }, // 기란,
																			// 42번,
																			// 45평
			{ 33372, 33384, 32867, 32874 }, // 기란, 43번, 120평
			{ 33425, 33437, 32865, 32872 }, // 기란, 44번, 120평
			{ 33446, 33449, 32869, 32876 }, // 기란, 45번, 78평
			{ 33443, 33445, 32869, 32871 } };

	// 마법인형 리스트
	static public final String[][] magicDoll = {
			// 1단계 마법인형
			{ "마법인형: 돌 골렘", "마법인형: 늑대인간", "마법인형: 버그베어", "마법인형: 크러스트시안", "마법인형: 에티", "마법인형: 목각" },
			// 2단계 마법인형
			{ "마법인형: 서큐버스", "마법인형: 장로", "마법인형: 코카트리스", "마법인형: 눈사람", "마법인형: 인어", "마법인형: 라바 골렘" },
			// 3단계 마법인형
			{ "마법인형: 자이언트", "마법인형: 흑장로", "마법인형: 서큐버스 퀸", "마법인형: 드레이크", "마법인형: 킹 버그베어", "마법인형: 다이아몬드 골렘" },
			// 4단계 마법인형
			{ "마법인형: 리치", "마법인형: 사이클롭스", "마법인형: 나이트발드", "마법인형: 시어", "마법인형: 아이리스", "마법인형: 뱀파이어", "마법인형: 머미로드" },
			// 5단계 마법인형
			{ "마법인형: 데몬", "마법인형: 데스나이트", "마법인형: 바란카", "마법인형: 타락", "마법인형: 바포메트", "마법인형: 얼음여왕", "마법인형: 커츠" },
			// 5단계 특수 마법인형
			{ "마법인형: 안타라스", "마법인형: 파푸리온", "마법인형: 린드비오르", "마법인형: 발라카스" },
			// 유일 마법인형
			{ "마법인형: 기르타스" } };

	public static boolean open_delay;

	public static boolean running = true;

	public static int max_level_exp_panelty = 0;

	public static long speedhack_walk = 0;
	public static long speedhack_attack = 0;

	// 접속자수 실시간 뻥튀기.
	public static int world_player_plus_count = 0;

	public static int charlock = 0;
	public static int buff_level = 0;

	// 인첸확률설정하기 걍데이
	public static int safe0weapon0 = 0;
	public static int safe0weapon1 = 0;
	public static int safe0weapon2 = 0;
	public static int safe0weapon3 = 0;
	public static int safe0weapon4 = 0;
	public static int safe0weapon5 = 0;
	public static int safe0weapon6 = 0;
	public static int safe0weapon7 = 0;
	public static int safe0weapon8 = 0;
	public static int safe0weapon9 = 0;
	public static int safe0weapon10 = 0;

	public static int ssafearmor4_3_5rnd2 = 0;
	public static int ssafearmor4_4_6rnd2 = 0;
	public static int ssafearmor4_5_7rnd2 = 0;
	public static int ssafearmor6_5_7rnd2 = 0;
	public static int ssafeweapon_rnd2 = 0;

	// 인첸확률설정하기 축데이
	public static int ssafe0weapon0 = 0;
	public static int ssafe0weapon1 = 0;
	public static int ssafe0weapon2 = 0;
	public static int ssafe0weapon3 = 0;
	public static int ssafe0weapon4 = 0;
	public static int ssafe0weapon5 = 0;
	public static int ssafe0weapon6 = 0;
	public static int ssafe0weapon7 = 0;
	public static int ssafe0weapon8 = 0;
	public static int ssafe0weapon9 = 0;
	public static int ssafe0weapon10 = 0;
	// 인첸확률설정하기 걍데이
	public static int safe6weapon6 = 0;
	public static int safe6weapon7 = 0;
	public static int safe6weapon8 = 0;
	public static int safe6weapon9 = 0;
	public static int safe6weapon10 = 0;
	public static int safe6weapon11 = 0;
	// 축데이 인첸설정
	public static int ssafe6weapon6 = 0;
	public static int ssafe6weapon7 = 0;
	public static int ssafe6weapon8 = 0;
	public static int ssafe6weapon9 = 0;
	public static int ssafe6weapon10 = 0;
	public static int ssafe6weapon11 = 0;
	// 걍젤 인첸확률
	public static int safe0armor0 = 0;
	public static int safe0armor1 = 0;
	public static int safe0armor2 = 0;
	public static int safe0armor3 = 0;
	public static int safe0armor4 = 0;
	public static int safe0armor5 = 0;
	public static int safe0armor6 = 0;
	public static int safe0armor7 = 0;
	public static int safe0armor8 = 0;
	public static int safe0armor9 = 0;

	public static int safe4armor4 = 0;
	public static int safe4armor5 = 0;
	public static int safe4armor6 = 0;
	public static int safe4armor7 = 0;
	public static int safe4armor8 = 0;
	public static int safe4armor9 = 0;

	public static int safe6armor6 = 0;
	public static int safe6armor7 = 0;
	public static int safe6armor8 = 0;
	public static int safe6armor9 = 0;
	// 축젤 인첸확률
	public static int ssafe0armor0 = 0;
	public static int ssafe0armor1 = 0;
	public static int ssafe0armor2 = 0;
	public static int ssafe0armor3 = 0;
	public static int ssafe0armor4 = 0;
	public static int ssafe0armor5 = 0;
	public static int ssafe0armor6 = 0;
	public static int ssafe0armor7 = 0;
	public static int ssafe0armor8 = 0;
	public static int ssafe0armor9 = 0;

	public static int ssafe4armor4 = 0;
	public static int ssafe4armor5 = 0;
	public static int ssafe4armor6 = 0;
	public static int ssafe4armor7 = 0;
	public static int ssafe4armor8 = 0;
	public static int ssafe4armor9 = 0;

	public static int ssafe6armor6 = 0;
	public static int ssafe6armor7 = 0;
	public static int ssafe6armor8 = 0;
	public static int ssafe6armor9 = 0;

	public static int acc0 = 0;
	public static int acc1 = 0;
	public static int acc2 = 0;
	public static int acc3 = 0;
	public static int acc4 = 0;
	public static int acc5 = 0;
	public static int acc6 = 0;
	public static int acc7 = 0;

	public static int enchant_safe6dmg7;
	public static int enchant_safe6dmg8;
	public static int enchant_safe6dmg9;
	public static int enchant_safe6dmg10;
	public static int enchant_safe6dmg11;

	public static int enchant_safe0dmg1;
	public static int enchant_safe0dmg2;
	public static int enchant_safe0dmg3;
	public static int enchant_safe0dmg4;
	public static int enchant_safe0dmg5;
	public static int enchant_safe0dmg6;
	public static int enchant_safe0dmg7;
	public static int enchant_safe0dmg8;
	public static int enchant_safe0dmg9;
	public static int enchant_safe0dmg10;

	static public int adena_trade_regedit_min_level;
	static public int pc_trade_sale_max_count;
	static public int adena_trade_regedit_price;
	static public int adena_trade_regedit_min_value;
	static public int adena_trade_regedit_min_valueone;
	static public int adena_trade_regedit_min_valuetwo;
	static public int adena_trade_regedit_cancel_hour;
	static public double adena_trade_regedit_cancel_rate;

	static public int stun;
	static public int tryple;

	static public double imm_dmg;
	static public double imm_dmg_skill;

	static public int dis_max;
	static public int dis_min;

	// 반역자방패 확률
	static public int banshiled;

	static public int int18_mr59;
	static public int int18_mr69;
	static public int int18_mr79;
	static public int int18_mr89;
	static public int int18_mr99;
	static public int int18_mr109;
	static public int int18_mr119;

	static public int int22_mr59;
	static public int int22_mr69;
	static public int int22_mr79;
	static public int int22_mr89;
	static public int int22_mr99;
	static public int int22_mr109;
	static public int int22_mr119;

	static public int int26_mr59;
	static public int int26_mr69;
	static public int int26_mr79;
	static public int int26_mr89;
	static public int int26_mr99;
	static public int int26_mr109;
	static public int int26_mr119;

	static public int int30_mr59;
	static public int int30_mr69;
	static public int int30_mr79;
	static public int int30_mr89;
	static public int int30_mr99;
	static public int int30_mr109;
	static public int int30_mr119;

	static public int int35_mr59;
	static public int int35_mr69;
	static public int int35_mr79;
	static public int int35_mr89;
	static public int int35_mr99;
	static public int int35_mr109;
	static public int int35_mr119;

	static public int can_int18_mr59;
	static public int can_int18_mr69;
	static public int can_int18_mr79;
	static public int can_int18_mr89;
	static public int can_int18_mr99;
	static public int can_int18_mr109;
	static public int can_int18_mr119;

	static public int can_int22_mr59;
	static public int can_int22_mr69;
	static public int can_int22_mr79;
	static public int can_int22_mr89;
	static public int can_int22_mr99;
	static public int can_int22_mr109;
	static public int can_int22_mr119;

	static public int can_int26_mr59;
	static public int can_int26_mr69;
	static public int can_int26_mr79;
	static public int can_int26_mr89;
	static public int can_int26_mr99;
	static public int can_int26_mr109;
	static public int can_int26_mr119;

	static public int can_int30_mr59;
	static public int can_int30_mr69;
	static public int can_int30_mr79;
	static public int can_int30_mr89;
	static public int can_int30_mr99;
	static public int can_int30_mr109;
	static public int can_int30_mr119;

	static public int can_int35_mr59;
	static public int can_int35_mr69;
	static public int can_int35_mr79;
	static public int can_int35_mr89;
	static public int can_int35_mr99;
	static public int can_int35_mr109;
	static public int can_int35_mr119;

	public static double irub_mon_dmg;
	public static double miti_cha_dmg;

	static public int armor_ac;

	// 뚫어 사용 미사용
	public static boolean move_hack_defence;
	public static boolean move_hack_defence_log;

	public static Object command;
	public static boolean open_wait;
	public static boolean is_gm_global_chat;

	// new 불멸의가호
	static public boolean is_old_gaho = false;
	// 불멸의가호 시스템 사용 여부
	static public boolean is_immortality = false;
	// 불멸의가호 시스템 PvP시에만 적용 여부
	static public boolean is_immortality_pvp;
	// 불멸의가호이름
	static public String immortality_item_name = "";
	static public String immortality_item_name2 = "";
	static public String immortality_item_name3 = "";
	// 불멸의가호 아덴 획득 증가량
	static public double immortality_aden_percent = 0;
	// 불멸의가호 경험치 증가량
	static public double immortality_exp = 0;
	// 캐릭터 죽일시 타버린 불멸의 가호 지급 여부
	static public boolean is_immortality_kill_item = false;
	// 타버린 불멸의 가호 아이템 이름
	static public String immortality_kill_item_name = "";
	// 타버린 불멸의 가호 드랍 여부
	static public boolean is_immortality_kill_item_drop;
	// 몬스터에게 사망시 타버린 불멸의 가호 드랍 여부
	static public boolean is_immortality_kill_item_drop_monster;
	// 바포메트 시스템 적용 여부.
	static public boolean is_batpomet_system;
	static public boolean baphomet_system_criminal;
	// 칼렉 감소 적용 여부
	static public boolean is_sword_lack_check;
	// 캐릭터 사망 이팩트 여부
	static public boolean is_character_dead_effect;
	// 퀘스트 마지막
	static public int lastquest;
	// 랜덤퀘스트 하루제한
	static public int dayquest;

	static public boolean is_item_drop_msg_name;
	static public boolean is_item_drop_msg_life;

	/**
	 * 리니지에 사용되는 변수 초기화 함수.
	 */
	static public void init(boolean reload) {
		TimeLine.start("Lineage..");

		royal_spawn.clear();
		royal_first_spell.clear();
		royal_first_inventory.clear();
		knight_spawn.clear();
		knight_first_spell.clear();
		knight_first_inventory.clear();
		elf_spawn.clear();
		elf_first_spell.clear();
		elf_first_inventory.clear();
		wizard_spawn.clear();
		wizard_first_spell.clear();
		wizard_first_inventory.clear();
		darkelf_spawn.clear();
		darkelf_first_spell.clear();
		darkelf_first_inventory.clear();
		dragonknight_spawn.clear();
		dragonknight_first_spell.clear();
		dragonknight_first_inventory.clear();
		blackwizard_spawn.clear();
		blackwizard_first_spell.clear();
		blackwizard_first_inventory.clear();
		warrior_spawn.clear();
		warrior_first_spell.clear();
		warrior_first_inventory.clear();

		kingdom_war_no_remove_item_kent.clear();
		kingdom_war_no_remove_item_orcish.clear();
		kingdom_war_no_remove_item_windawood.clear();
		kingdom_war_no_remove_item_giran.clear();
		kingdom_war_no_remove_item_heine.clear();
		kingdom_war_no_remove_item_abyss.clear();
		kingdom_war_no_remove_item_aden.clear();
		no_remove_item.clear();

		giran_kingdom_war_day_list.clear();

		try {
			BufferedReader lnrr = new BufferedReader(new FileReader("lineage.conf"));
			String line;
			while ((line = lnrr.readLine()) != null) {
				if (line.startsWith("#"))
					continue;

				int pos = line.indexOf("=");
				if (pos > 0) {
					String key = line.substring(0, pos).trim();
					String value = line.substring(pos + 1, line.length()).trim();

					if (key.equalsIgnoreCase("royal_spawn"))
						toFirstSpawn(royal_spawn, value);
					else if (key.equalsIgnoreCase("royal_male_gfx"))
						royal_male_gfx = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("royal_female_gfx"))
						royal_female_gfx = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("royal_hp"))
						royal_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("royal_mp"))
						royal_mp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("royal_max_hp"))
						royal_max_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("royal_max_mp"))
						royal_max_mp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("delay_semi_bug"))
						delay_semi_bug = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("delay_semi_bug_check_time"))
						delay_semi_bug_check_time = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("royal_spell"))
						toFirstSpell(royal_first_spell, value);
					else if (key.equalsIgnoreCase("royal_first_inventory"))
						toFirstInventory(royal_first_inventory, value);
					else if (key.equalsIgnoreCase("royal_stat_str"))
						royal_stat_str = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("royal_stat_con"))
						royal_stat_con = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("royal_stat_dex"))
						royal_stat_dex = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("royal_stat_wis"))
						royal_stat_wis = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("royal_stat_cha"))
						royal_stat_cha = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("royal_stat_int"))
						royal_stat_int = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("royal_stat_dice"))
						royal_stat_dice = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("is_blue_message"))
						is_blue_message = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("knight_spawn"))
						toFirstSpawn(knight_spawn, value);
					else if (key.equalsIgnoreCase("knight_male_gfx"))
						knight_male_gfx = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("knight_female_gfx"))
						knight_female_gfx = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("knight_hp"))
						knight_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("knight_mp"))
						knight_mp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("knight_max_hp"))
						knight_max_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("knight_max_mp"))
						knight_max_mp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("knight_spell"))
						toFirstSpell(knight_first_spell, value);
					else if (key.equalsIgnoreCase("knight_first_inventory"))
						toFirstInventory(knight_first_inventory, value);
					else if (key.equalsIgnoreCase("knight_stat_str"))
						knight_stat_str = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("knight_stat_con"))
						knight_stat_con = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("knight_stat_dex"))
						knight_stat_dex = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("knight_stat_wis"))
						knight_stat_wis = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("knight_stat_cha"))
						knight_stat_cha = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("knight_stat_int"))
						knight_stat_int = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("knight_stat_dice"))
						knight_stat_dice = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("is_fight"))
						is_fight = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("fight_aden"))
						fight_aden = value;
					else if (key.equalsIgnoreCase("fight_ticket_price"))
						fight_ticket_price = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("fight_rate"))
						fight_rate = Double.valueOf(value);
					else if (key.equalsIgnoreCase("fight_max_ticket"))
						fight_max_ticket = Long.valueOf(value);

					else if (key.equalsIgnoreCase("great_hill"))
						great_hill = Double.valueOf(value);
					else if (key.equalsIgnoreCase("full_hill"))
						full_hill = Double.valueOf(value);
					else if (key.equalsIgnoreCase("hill_all"))
						hill_all = Double.valueOf(value);

					else if (key.equalsIgnoreCase("weapon_bect_damage"))
						weapon_bect_damage = Double.valueOf(value);

					else if (key.equalsIgnoreCase("is_world_clean"))
						is_world_clean = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("is_world_clean_message"))
						is_world_clean_message = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("world_clean_time"))
						world_clean_time = Integer.valueOf(value) * 1000 * 60;
					else if (key.equalsIgnoreCase("world_clean_message_time"))
						world_clean_message_time = (Integer.valueOf(value) * 1000) + 1000;

					else if (key.equalsIgnoreCase("elf_spawn"))
						toFirstSpawn(elf_spawn, value);
					else if (key.equalsIgnoreCase("elf_male_gfx"))
						elf_male_gfx = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("elf_female_gfx"))
						elf_female_gfx = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("elf_hp"))
						elf_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("elf_mp"))
						elf_mp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("elf_max_hp"))
						elf_max_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("elf_max_mp"))
						elf_max_mp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("elf_spell"))
						toFirstSpell(elf_first_spell, value);
					else if (key.equalsIgnoreCase("elf_first_inventory"))
						toFirstInventory(elf_first_inventory, value);
					else if (key.equalsIgnoreCase("elf_stat_str"))
						elf_stat_str = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("elf_stat_con"))
						elf_stat_con = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("elf_stat_dex"))
						elf_stat_dex = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("elf_stat_wis"))
						elf_stat_wis = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("elf_stat_cha"))
						elf_stat_cha = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("elf_stat_int"))
						elf_stat_int = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("elf_stat_dice"))
						elf_stat_dice = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("is_lost_island_join"))
						is_lost_island_join = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("lost_island_wanted"))
						lost_island_wanted = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("lost_island_min_level"))
						lost_island_min_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("lost_island_join_item"))
						lost_island_join_item = value;
					else if (key.equalsIgnoreCase("lost_island_join_item_count"))
						lost_island_join_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("lost_no_clan_island"))
						lost_no_clan_island = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("is_advance_spirit_target"))
						is_advance_spirit_target = value.equalsIgnoreCase("true");

					// 클랜레벨
					else if (key.equalsIgnoreCase("One_Clan_Level"))
						One_Clan_Level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("Two_Clan_Level"))
						Two_Clan_Level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("Three_Clan_Level"))
						Three_Clan_Level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("Four_Clan_Level"))
						Four_Clan_Level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("Five_Clan_Level"))
						Five_Clan_Level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("Six_Clan_Level"))
						Six_Clan_Level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("Seven_Clan_Level"))
						Seven_Clan_Level = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("wizard_spawn"))
						toFirstSpawn(wizard_spawn, value);
					else if (key.equalsIgnoreCase("wizard_male_gfx"))
						wizard_male_gfx = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wizard_female_gfx"))
						wizard_female_gfx = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wizard_hp"))
						wizard_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wizard_mp"))
						wizard_mp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wizard_max_hp"))
						wizard_max_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wizard_max_mp"))
						wizard_max_mp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wizard_spell"))
						toFirstSpell(wizard_first_spell, value);
					else if (key.equalsIgnoreCase("wizard_first_inventory"))
						toFirstInventory(wizard_first_inventory, value);
					else if (key.equalsIgnoreCase("wizard_stat_str"))
						wizard_stat_str = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wizard_stat_con"))
						wizard_stat_con = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wizard_stat_dex"))
						wizard_stat_dex = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wizard_stat_wis"))
						wizard_stat_wis = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wizard_stat_cha"))
						wizard_stat_cha = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wizard_stat_int"))
						wizard_stat_int = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wizard_stat_dice"))
						wizard_stat_dice = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("is_exp_support"))
						is_exp_support = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("exp_support_max_level"))
						exp_support_max_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("beginner_max_level"))
						Beginner_max_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("pc_trade_sell_time"))
						pc_trade_sell_time = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("pc_trade_buy_deposit_delay"))
						pc_trade_buy_deposit_delay = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("pc_trade_buy_max_count"))
						pc_trade_buy_max_count = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("pc_trade_buy_min_level"))
						pc_trade_buy_min_level = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("item_trade_buy_min_level"))
						item_trade_buy_min_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("item_trade_sell_min_level"))
						item_trade_sell_min_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("darkelf_spawn"))
						toFirstSpawn(darkelf_spawn, value);
					else if (key.equalsIgnoreCase("darkelf_male_gfx"))
						darkelf_male_gfx = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("darkelf_female_gfx"))
						darkelf_female_gfx = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("darkelf_hp"))
						darkelf_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("darkelf_mp"))
						darkelf_mp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("darkelf_max_hp"))
						darkelf_max_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("darkelf_max_mp"))
						darkelf_max_mp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("darkelf_spell"))
						toFirstSpell(darkelf_first_spell, value);
					else if (key.equalsIgnoreCase("darkelf_first_inventory"))
						toFirstInventory(darkelf_first_inventory, value);
					else if (key.equalsIgnoreCase("darkelf_stat_str"))
						darkelf_stat_str = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("darkelf_stat_con"))
						darkelf_stat_con = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("darkelf_stat_dex"))
						darkelf_stat_dex = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("darkelf_stat_wis"))
						darkelf_stat_wis = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("darkelf_stat_cha"))
						darkelf_stat_cha = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("darkelf_stat_int"))
						darkelf_stat_int = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("dragonknight_spawn"))
						toFirstSpawn(dragonknight_spawn, value);
					else if (key.equalsIgnoreCase("dragonknight_male_gfx"))
						dragonknight_male_gfx = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dragonknight_female_gfx"))
						dragonknight_female_gfx = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dragonknight_hp"))
						dragonknight_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dragonknight_mp"))
						dragonknight_mp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dragonknight_max_hp"))
						dragonknight_max_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dragonknight_spell"))
						toFirstSpell(dragonknight_first_spell, value);
					else if (key.equalsIgnoreCase("dragonknight_first_inventory"))
						toFirstInventory(dragonknight_first_inventory, value);
					else if (key.equalsIgnoreCase("dragonknight_max_mp"))
						dragonknight_max_mp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dragonknight_stat_str"))
						dragonknight_stat_str = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dragonknight_stat_con"))
						dragonknight_stat_con = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dragonknight_stat_dex"))
						dragonknight_stat_dex = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dragonknight_stat_wis"))
						dragonknight_stat_wis = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dragonknight_stat_cha"))
						dragonknight_stat_cha = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dragonknight_stat_int"))
						dragonknight_stat_int = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("blackwizard_spawn"))
						toFirstSpawn(blackwizard_spawn, value);
					else if (key.equalsIgnoreCase("blackwizard_male_gfx"))
						blackwizard_male_gfx = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("blackwizard_female_gfx"))
						blackwizard_female_gfx = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("blackwizard_hp"))
						blackwizard_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("blackwizard_mp"))
						blackwizard_mp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("blackwizard_max_hp"))
						blackwizard_max_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("blackwizard_max_mp"))
						blackwizard_max_mp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("blackwizard_spell"))
						toFirstSpell(blackwizard_first_spell, value);
					else if (key.equalsIgnoreCase("blackwizard_first_inventory"))
						toFirstInventory(blackwizard_first_inventory, value);
					else if (key.equalsIgnoreCase("blackwizard_stat_str"))
						blackwizard_stat_str = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("blackwizard_stat_con"))
						blackwizard_stat_con = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("blackwizard_stat_dex"))
						blackwizard_stat_dex = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("blackwizard_stat_wis"))
						blackwizard_stat_wis = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("blackwizard_stat_cha"))
						blackwizard_stat_cha = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("blackwizard_stat_int"))
						blackwizard_stat_int = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("warrior_spawn"))
						toFirstSpawn(warrior_spawn, value);
					else if (key.equalsIgnoreCase("warrior_male_gfx"))
						warrior_male_gfx = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("warrior_female_gfx"))
						warrior_female_gfx = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("warrior_hp"))
						warrior_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("warrior_mp"))
						warrior_mp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("warrior_max_hp"))
						warrior_max_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("warrior_max_mp"))
						warrior_max_mp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("warrior_spell"))
						toFirstSpell(warrior_first_spell, value);
					else if (key.equalsIgnoreCase("warrior_first_inventory"))
						toFirstInventory(warrior_first_inventory, value);
					else if (key.equalsIgnoreCase("warrior_stat_str"))
						warrior_stat_str = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("warrior_stat_con"))
						warrior_stat_con = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("warrior_stat_dex"))
						warrior_stat_dex = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("warrior_stat_wis"))
						warrior_stat_wis = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("warrior_stat_cha"))
						warrior_stat_cha = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("warrior_stat_int"))
						warrior_stat_int = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("Wanted_addDmg"))
						Wanted_addDmg = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("Wanted_addDmg2"))
						Wanted_addDmg2 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("inventory_max"))
						inventory_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("inventory_weight_max"))
						inventory_weight_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("level_max"))
						level_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("party_max"))
						party_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("warehouse_level"))
						warehouse_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("warehouse_price"))
						warehouse_price = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("warehouse_price_elf"))
						warehouse_price_elf = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("warehouse_max"))
						warehouse_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("stat_max"))
						stat_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("stat_base"))
						stat_base = Boolean.valueOf(value);
					else if (key.equalsIgnoreCase("rate_enchant"))
						rate_enchant = Double.valueOf(value);
					else if (key.equalsIgnoreCase("rate_drop"))
						rate_drop = Double.valueOf(value);
					else if (key.equalsIgnoreCase("rate_exp"))
						rate_exp = Double.valueOf(value);
					else if (key.equalsIgnoreCase("rate_lawful"))
						rate_lawful = Double.valueOf(value);
					else if (key.equalsIgnoreCase("rate_aden"))
						rate_aden = Double.valueOf(value);
					else if (key.equalsIgnoreCase("rate_party"))
						rate_party = Double.valueOf(value);
					else if (key.equalsIgnoreCase("rate_exp_pet"))
						rate_exp_pet = Double.valueOf(value);
					else if (key.equalsIgnoreCase("chatting_level_global"))
						chatting_level_global = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("chatting_level_normal"))
						chatting_level_normal = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("chatting_level_whisper"))
						chatting_level_whisper = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("chatting_level_trade"))
						chatting_level_trade = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("nonpvp"))
						nonpvp = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("tripc"))
						tripc = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("trimob"))
						trimob = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("stunpc"))
						stunpc = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("stunmob"))
						stunmob = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("stun_level"))
						stun_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("triple_level"))
						triple_level = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("server_version"))
						server_version = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ai_corpse_time"))
						ai_corpse_time = Integer.valueOf(value) * 1000;
					else if (key.equalsIgnoreCase("ai_summon_corpse_time"))
						ai_summon_corpse_time = Integer.valueOf(value) * 1000;
					else if (key.equalsIgnoreCase("ai_pet_corpse_time"))
						ai_pet_corpse_time = Integer.valueOf(value) * 1000;
					else if (key.equalsIgnoreCase("clan_auto_buff_time"))
						clan_auto_buff_time = Integer.valueOf(value) * 1000 * 60;
					else if (key.equalsIgnoreCase("ai_auto_healingpotion_percent"))
						ai_auto_healingpotion_percent = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("warehouse_pet_price"))
						warehouse_pet_price = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("flat_rate"))
						flat_rate = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("flat_rate_price"))
						flat_rate_price = Integer.valueOf(value) * 60;
					else if (key.equalsIgnoreCase("account_auto_create"))
						account_auto_create = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("inn_max"))
						inn_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("inn_in_max"))
						inn_in_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("inn_price"))
						inn_price = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("inn_time"))
						inn_time = Integer.valueOf(value) * 1000 * 60;
					else if (key.equalsIgnoreCase("board_write_price"))
						board_write_price = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("board_rank_update_delay"))
						board_rank_update_delay = Integer.valueOf(value) * 1000 * 60;
					else if (key.equalsIgnoreCase("inn_hall_max"))
						inn_hall_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("inn_hall_in_max"))
						inn_hall_in_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("inn_hall_price"))
						inn_hall_price = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("min_tax"))
						min_tax = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("max_tax"))
						max_tax = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("crown_clan_min_people"))
						crown_clan_min_people = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("is_kingdom_war"))
						is_kingdom_war = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("giran_kingdom_crown_min"))
						giran_kingdom_crown_min = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("giran_kingdom_crown_msg_count"))
						giran_kingdom_crown_msg_count = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("is_kingdom_war_notice"))
						is_kingdom_war_notice = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("kingdom_war_notice_delay"))
						kingdom_war_notice_delay = Integer.valueOf(value) * 1000 * 60;

					else if (key.equalsIgnoreCase("kingdom_war_day"))
						kingdom_war_day = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("kingdom_war_time"))
						kingdom_war_time = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("kingdom_boss_time"))
						kingdom_boss_time = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("kingdom_player_dead_expdown"))
						kingdom_player_dead_expdown = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("kingdom_player_dead_itemdrop"))
						kingdom_player_dead_itemdrop = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("kingdom_crown"))
						kingdom_crown = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("kingdom_pvp_pk"))
						kingdom_pvp_pk = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("kingdom_war_revival"))
						kingdom_war_revival = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("kingdom_war_callclan"))
						kingdom_war_callclan = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("kingdom_item_count_rate"))
						kingdom_item_count_rate = Double.valueOf(value);
					else if (key.equalsIgnoreCase("kingdom_soldier_price"))
						kingdom_soldier_price = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("kent_kingdom_war_hour"))
						kent_kingdom_war_hour = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("kent_kingdom_war_min"))
						kent_kingdom_war_min = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("giran_kingdom_war_hour"))
						giran_kingdom_war_hour = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("giran_kingdom_war_min"))
						giran_kingdom_war_min = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("esmereld_sec"))
						esmereld_sec = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("elf_gatherup_time"))
						elf_gatherup_time = Integer.valueOf(value) * 1000;
					else if (key.equalsIgnoreCase("npc_talk_stay_time"))
						npc_talk_stay_time = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("whisper_effect"))
						whisper_effect = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("ChatTime"))
						ChatTime = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("ChatTimetwo"))
						ChatTimetwo = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("singyu_clan_level"))
						singyu_clan_level = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("singyu_shop"))
						singyu_shop = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("Orim_en_level"))
						Orim_en_level = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("bow_target_armor_flee"))
						bow_target_armor_flee = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("target_armor_flee"))
						target_armor_flee = Integer.valueOf(value);
					// 성환.클랜리덕션

					else if (key.equalsIgnoreCase("Clan_numone"))
						Clan_numone = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("Clan_Redone"))
						Clan_Redone = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("Clan_numtwo"))
						Clan_numtwo = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("Clan_Redtwo"))
						Clan_Redtwo = Integer.valueOf(value);

					// 성환.클랜리덕션 -
					else if (key.equalsIgnoreCase("Clan_numthree"))
						Clan_numthree = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("Clan_Redthree"))
						Clan_Redthree = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("Clan_numfour"))
						Clan_numfour = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("Clan_Redfour"))
						Clan_Redfour = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("sell_item_rate"))
						sell_item_rate = Double.valueOf(value) * 0.01;
					else if (key.equalsIgnoreCase("elvenforest_elementalstone_spawn_count"))
						elvenforest_elementalstone_spawn_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("elvenforest_elementalstone_spawn_time"))
						elvenforest_elementalstone_spawn_time = Integer.valueOf(value) * 1000 * 60;
					else if (key.equalsIgnoreCase("elvenforest_elementalstone_min_count"))
						elvenforest_elementalstone_min_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("elvenforest_elementalstone_max_count"))
						elvenforest_elementalstone_max_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("door_open_delay"))
						door_open_delay = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("object_who"))
						object_who = value;
					else if (key.equalsIgnoreCase("object_who2"))
						object_who2 = value;
					else if (key.equalsIgnoreCase("world_item_delay"))
						world_item_delay = Integer.valueOf(value) * 1000 * 60;
					else if (key.equalsIgnoreCase("auto_pickup"))
						auto_pickup = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("auto_pickup_aden"))
						auto_pickup_aden = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("auto_pickup_boss"))
						auto_pickup_boss = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("auto_pickup_percent"))
						auto_pickup_percent = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("is_dex_ac"))
						is_dex_ac = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("chance_max_drop"))
						chance_max_drop = Double.valueOf(value);
					else if (key.equalsIgnoreCase("player_dead_itemdrop"))
						player_dead_itemdrop = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("player_dead_expdown"))
						player_dead_expdown = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("player_dead_exp_gift"))
						player_dead_exp_gift = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("player_dead_expdown_level"))
						player_dead_expdown_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("player_dead_itemdrop_level"))
						player_dead_itemdrop_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("player_dead_expdown_rate"))
						player_dead_expdown_rate = Double.valueOf(value) * 0.01;
					else if (key.equalsIgnoreCase("player_lost_exp_rate"))
						player_lost_exp_rate = Double.valueOf(value) * 0.01;
					else if (key.equalsIgnoreCase("player_lost_exp_aden_rate"))
						player_lost_exp_aden_rate = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("player_worldout_time"))
						player_worldout_time = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("monster_interface_hpbar"))
						monster_interface_hpbar = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("npc_interface_hpbar"))
						npc_interface_hpbar = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("ai_monster_tic_time"))
						ai_monster_tic_time = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("item_durability_max"))
						item_durability_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("pet_level_max"))
						pet_level_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("evolution_level"))
						evolution_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("pc_market_level"))
						pc_market_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("buff_ImmuneToHarm_message"))
						buff_ImmuneToHarm_message = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("buff_GlowingAura_message"))
						buff_GlowingAura_message = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("rank_min_level"))
						rank_min_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("rank_min_level2"))
						rank_min_level2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("rank_class_1"))
						rank_class_1 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("rank_class_2"))
						rank_class_2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("rank_class_3"))
						rank_class_3 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("rank_class_4"))
						rank_class_4 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("is_rank_poly"))
						is_rank_poly = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("rank_poly_all"))
						rank_poly_all = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("rank_poly_class"))
						rank_poly_class = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("notice_delay"))
						notice_delay = Integer.valueOf(value) * 1000 * 120;
					else if (key.equalsIgnoreCase("event_poly"))
						event_poly = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("event_rank_poly"))
						event_rank_poly = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("event_exp"))
						event_exp = value.equalsIgnoreCase("false");
					else if (key.equalsIgnoreCase("account_ip_count"))
						account_ip_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("event_buff"))
						event_buff = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("event_illusion"))
						event_illusion = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("event_christmas"))
						event_christmas = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("event_halloween"))
						event_halloween = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("event_lyra"))
						event_lyra = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("event_ghosthouse"))
						event_ghosthouse = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("event_lucky"))
						event_lucky = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("event_kujak"))
						event_kujak = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("autohunt_onoff"))
						autohunt_onoff = value.equalsIgnoreCase("true");
					// 추가 20180528
					else if (key.equalsIgnoreCase("event_mon"))
						event_mon = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("event_vil"))
						event_vil = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("irub_mon_dmg"))
						irub_mon_dmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("miti_cha_dmg"))
						miti_cha_dmg = Double.valueOf(value);

					else if (key.equalsIgnoreCase("lastquest"))
						lastquest = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayquest"))
						dayquest = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("q1"))
						q1 = value;
					else if (key.equalsIgnoreCase("qc1"))
						qc1 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("q2"))
						q2 = value;
					else if (key.equalsIgnoreCase("qc2"))
						qc2 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("q3"))
						q3 = value;
					else if (key.equalsIgnoreCase("qc3"))
						qc3 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("rq1"))
						rq1 = value;
					else if (key.equalsIgnoreCase("rqc1"))
						rqc1 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("rq2"))
						rq2 = value;
					else if (key.equalsIgnoreCase("rqc2"))
						rqc2 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("rq3"))
						rq3 = value;
					else if (key.equalsIgnoreCase("rqc3"))
						rqc3 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("rq4"))
						rq4 = value;
					else if (key.equalsIgnoreCase("rqc4"))
						rqc4 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("rq5"))
						rq5 = value;
					else if (key.equalsIgnoreCase("rqc5"))
						rqc5 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("rq6"))
						rq6 = value;
					else if (key.equalsIgnoreCase("rqc6"))
						rqc6 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("rq7"))
						rq7 = value;
					else if (key.equalsIgnoreCase("rqc7"))
						rqc7 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("rq8"))
						rq8 = value;
					else if (key.equalsIgnoreCase("rqc8"))
						rqc8 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("quest_reset_hour"))
						quest_reset_hour = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("is_giran_dungeon_time"))
						is_giran_dungeon_time = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("giran_dungeon_time"))
						giran_dungeon_time = Integer.valueOf(value) * 60;
					else if (key.equalsIgnoreCase("giran_dungeon_inti_time"))
						giran_dungeon_inti_time = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dungeon_inti_time_message"))
						dungeon_inti_time_message = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("giran_dungeon_level"))
						giran_dungeon_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("giran_dungeon_level2"))
						giran_dungeon_level2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("giran_dungeon_level3"))
						giran_dungeon_level3 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("giran_dungeon_scroll_count"))
						giran_dungeon_scroll_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("grimreaper_spawn_probability"))
						grimreaper_spawn_probability = Double.valueOf(value) * 0.01;
					else if (key.equalsIgnoreCase("to_monster_dmg_reduc"))
						to_monster_dmg_reduc = Double.valueOf(value);
					else if (key.equalsIgnoreCase("slime_race_price"))
						slime_race_price = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dog_race_price"))
						dog_race_price = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("bravery_potion_frame"))
						bravery_potion_frame = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("elven_wafer_frame"))
						elven_wafer_frame = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("holywalk_frame"))
						holywalk_frame = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("devil_potion_frame"))
						devil_potion_frame = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("world_premium_item_is"))
						world_premium_item_is = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("world_premium_item"))
						world_premium_item = value;
					else if (key.equalsIgnoreCase("world_premium_rate"))
						world_premium_rate = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("world_premium_item_delay"))
						world_premium_item_delay = Integer.valueOf(value) * 1000 * 60;
					else if (key.equalsIgnoreCase("world_premium_item_level"))
						world_premium_item_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("world_premium_item_clan_rate"))
						world_premium_item_clan_rate = Double.valueOf(value);
					else if (key.equalsIgnoreCase("world_premium_item_kingdom_rate"))
						world_premium_item_kingdom_rate = Double.valueOf(value);

					else if (key.equalsIgnoreCase("is_world_open_wait_item"))
						is_world_open_wait_item = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("world_open_wait_item"))
						world_open_wait_item = value;
					else if (key.equalsIgnoreCase("world_open_wait_item_min"))
						world_open_wait_item_min = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("world_open_wait_item_max"))
						world_open_wait_item_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("world_open_wait_item_delay"))
						world_open_wait_item_delay = Integer.valueOf(value) * 1000;

					else if (key.equalsIgnoreCase("wanted_price_min"))
						wanted_price_min = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wanted_price_max"))
						wanted_price_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wanted_price_min49"))
						wanted_price_min49 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wanted_price_max49"))
						wanted_price_max49 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wanted_price_min50"))
						wanted_price_min50 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wanted_price_max50"))
						wanted_price_max50 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wanted_price_min51"))
						wanted_price_min51 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wanted_price_max51"))
						wanted_price_max51 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wanted_price_min52"))
						wanted_price_min52 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wanted_price_max52"))
						wanted_price_max52 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("world_premium_item_delay"))
						world_premium_item_delay = Integer.valueOf(value) * 1000 * 60;
					else if (key.equalsIgnoreCase("world_premium_item_clan_rate"))
						world_premium_item_clan_rate = Double.valueOf(value);
					else if (key.equalsIgnoreCase("world_premium_item_kingdom_rate"))
						world_premium_item_kingdom_rate = Double.valueOf(value);
					else if (key.equalsIgnoreCase("item_equipped_type"))
						item_equipped_type = value.equalsIgnoreCase("new");
					else if (key.equalsIgnoreCase("is_battle_zone"))
						is_battle_zone = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("battle_zone_x1"))
						battle_zone_x1 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("battle_zone_y1"))
						battle_zone_y1 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("battle_zone_x2"))
						battle_zone_x2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("battle_zone_y2"))
						battle_zone_y2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("battle_zone_map"))
						battle_zone_map = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("is_battle_zone_hp_bar"))
						is_battle_zone_hp_bar = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("gm_hp_bar"))
						gm_hp_bar = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("robot_auto_buff"))
						robot_auto_buff = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("robot_auto_tax"))
						robot_auto_tax = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("robot_auto_buff_aden"))
						robot_auto_buff_aden = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("robot_auto_buff_aden1"))
						robot_auto_buff_aden1 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("robot_auto_buff_aden2"))
						robot_auto_buff_aden2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("robot_auto_buff_aden3"))
						robot_auto_buff_aden3 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("robot_auto_buff_aden4"))
						robot_auto_buff_aden4 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("robot_auto_haste_aden"))
						robot_auto_haste_aden = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("robot_auto_pc_chance_max_drop"))
						robot_auto_pc_chance_max_drop = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("world_message_enchant_weapon"))
						world_message_enchant_weapon = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("world_message_enchant_armor"))
						world_message_enchant_armor = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("world_message_join"))
						world_message_join = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("robot_auto_pc"))
						robot_auto_pc = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("robot_auto_pc_healingpotion")) {
						int f_pos = value.indexOf("(");
						int e_pos = value.indexOf(")");
						String name = value.substring(0, f_pos).trim();
						int count = Integer.valueOf(value.substring(f_pos + 1, e_pos).trim());
						robot_auto_pc_healingpotion = name;
						robot_auto_pc_healingpotion_cnt = count;
					} else if (key.equalsIgnoreCase("robot_auto_pc_hastepotion")) {
						int f_pos = value.indexOf("(");
						int e_pos = value.indexOf(")");
						String name = value.substring(0, f_pos).trim();
						int count = Integer.valueOf(value.substring(f_pos + 1, e_pos).trim());
						robot_auto_pc_hastepotion = name;
						robot_auto_pc_hastepotion_cnt = count;
					} else if (key.equalsIgnoreCase("robot_auto_pc_braverypotion")) {
						int f_pos = value.indexOf("(");
						int e_pos = value.indexOf(")");
						String name = value.substring(0, f_pos).trim();
						int count = Integer.valueOf(value.substring(f_pos + 1, e_pos).trim());
						robot_auto_pc_braverypotion = name;
						robot_auto_pc_braverypotion_cnt = count;
					} else if (key.equalsIgnoreCase("robot_auto_pc_elvenwafer")) {
						int f_pos = value.indexOf("(");
						int e_pos = value.indexOf(")");
						String name = value.substring(0, f_pos).trim();
						int count = Integer.valueOf(value.substring(f_pos + 1, e_pos).trim());
						robot_auto_pc_elvenwafer = name;
						robot_auto_pc_elvenwafer_cnt = count;
					} else if (key.equalsIgnoreCase("robot_auto_pc_scrollpolymorph")) {
						int f_pos = value.indexOf("(");
						int e_pos = value.indexOf(")");
						String name = value.substring(0, f_pos).trim();
						int count = Integer.valueOf(value.substring(f_pos + 1, e_pos).trim());
						robot_auto_pc_scrollpolymorph = name;
						robot_auto_pc_scrollpolymorph_cnt = count;
					} else if (key.equalsIgnoreCase("robot_auto_pc_arrow")) {
						int f_pos = value.indexOf("(");
						int e_pos = value.indexOf(")");
						String name = value.substring(0, f_pos).trim();
						int count = Integer.valueOf(value.substring(f_pos + 1, e_pos).trim());
						robot_auto_pc_arrow = name;
						robot_auto_pc_arrow_cnt = count;
					} else if (key.equalsIgnoreCase("robot_auto_pc_stay_time"))
						robot_auto_pc_stay_time = Util.random(60 * 1000, Integer.valueOf(value) * 1000);
					else if (key.equalsIgnoreCase("max_mr"))
						max_mr = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("max_damage_mr"))
						max_damage_mr = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("boss_live_time"))
						boss_live_time = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("is_miss_effect"))
						is_miss_effect = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("is_gm_effect"))
						is_gm_effect = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("monster_level_exp"))
						monster_level_exp = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("monster_summon_item_drop"))
						monster_summon_item_drop = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("monster_item_drop"))
						monster_item_drop = value.equalsIgnoreCase("new");
					else if (key.equalsIgnoreCase("monster_boss_spawn_message"))
						monster_boss_spawn_message = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("monster_boss_dead_message"))
						monster_boss_dead_message = value.equalsIgnoreCase("true");
					// else if(key.equalsIgnoreCase("pool_max"))
					// pool_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("colosseum_giran"))
						colosseum_giran = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("colosseum_talkingisland"))
						colosseum_talkingisland = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("colosseum_silverknighttown"))
						colosseum_silverknighttown = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("colosseum_gludin"))
						colosseum_gludin = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("colosseum_windawood"))
						colosseum_windawood = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("colosseum_kent"))
						colosseum_kent = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("item_accessory_bless_enchant"))
						item_accessory_bless_enchant = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("item_polymorph_bless"))
						item_polymorph_bless = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("item_enchant_bless"))
						item_enchant_bless = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("item_enchant_armor_max"))
						item_enchant_armor_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("item_enchant_weapon_max"))
						item_enchant_weapon_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("item_enchant_0armor_max"))
						item_enchant_0armor_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("item_enchant_0weapon_max"))
						item_enchant_0weapon_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("item_enchant_weapon_top")) {
						StringTokenizer st = new StringTokenizer(value, ",");
						while (st.hasMoreTokens()) {
							String db = st.nextToken().trim();
							int f_pos = db.indexOf("(");
							int e_pos = db.indexOf(")");
							int en = Integer.valueOf(db.substring(0, f_pos).trim());
							int count = Integer.valueOf(db.substring(f_pos + 1, e_pos).trim());
							item_enchant_weapon_top[en] = count;
						}
					} else if (key.equalsIgnoreCase("item_enchant_armor_top")) {
						StringTokenizer st = new StringTokenizer(value, ",");
						while (st.hasMoreTokens()) {
							String db = st.nextToken().trim();
							int f_pos = db.indexOf("(");
							int e_pos = db.indexOf(")");
							int en = Integer.valueOf(db.substring(0, f_pos).trim());
							int count = Integer.valueOf(db.substring(f_pos + 1, e_pos).trim());
							item_enchant_armor_top[en] = count;
						}
					} else if (key.equalsIgnoreCase("item_acc_en"))
						item_acc_en = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("item_magicdoll_max"))
						item_magicdoll_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("item_delay_global"))
						item_delay_global = Boolean.valueOf(value);
					else if (key.equalsIgnoreCase("item_spellscroll_delay"))
						item_spellscroll_delay = Boolean.valueOf(value);
					else if (key.equalsIgnoreCase("pet_damage_to_player"))
						pet_damage_to_player = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("pet_tame_is"))
						pet_tame_is = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("item_elixir_max"))
						item_elixir_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("elixir_min_level"))
						elixir_min_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("item_bundle_chance"))
						item_bundle_chance = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("thread_event"))
						thread_event = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("thread_ai"))
						thread_ai = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("client_ping_time"))
						client_ping_time = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("world_player_count"))
						world_player_count = Double.valueOf(value);
					else if (key.equalsIgnoreCase("world_player_count_init"))
						world_player_count_init = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("party_autopickup_item_print"))
						party_autopickup_item_print = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("party_pickup_item_print"))
						party_pickup_item_print = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("party_exp_level_range"))
						party_exp_level_range = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("wanted_clan"))
						wanted_clan = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("wanted_name"))
						wanted_name = value;
					else if (key.equalsIgnoreCase("wanted_level")) {
						if (value.startsWith("~")) {
							// max
							wanted_level_min = 0;
							wanted_level_max = Integer.valueOf(value.substring(1).trim());
						} else {
							// min, min~max
							pos = value.indexOf("~");
							if (pos < 0)
								pos = value.length();
							wanted_level_min = Integer.valueOf(value.substring(0, pos).trim());
							if (pos + 1 < value.length())
								wanted_level_max = Integer.valueOf(value.substring(pos + 1).trim());
						}
					} else if (key.equalsIgnoreCase("pvp_print_message"))
						pvp_print_message = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("skill_ShapeChange"))
						skill_ShapeChange = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("skill_StormWalk"))
						skill_StormWalk = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("skill_EarthJail"))
						skill_EarthJail = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("skill_Haste_update"))
						skill_Haste_update = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("view_cracker_damage"))
						view_cracker_damage = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("view_cracker_dps"))
						view_cracker_dps = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("skill_Bravery_update"))
						skill_Bravery_update = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("skill_Wafer_update"))
						skill_Wafer_update = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("user_command"))
						user_command = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("auction_delay"))
						auction_delay = Long.valueOf(value) * 1000;
					else if (key.equalsIgnoreCase("speedhack"))
						speedhack = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("speedhack_warning_count"))
						speedhack_warning_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("speedhack_stun"))
						speedhack_stun = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("speed_bad_packet_count"))
						speed_bad_packet_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("speed_good_packet_count"))
						speed_good_packet_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("is_attack_count_packet"))
						is_attack_count_packet = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("speed_hack_block_time"))
						speed_hack_block_time = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("speed_hack_message_count"))
						speed_hack_message_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("speed_check_walk_frame_rate"))
						speed_check_walk_frame_rate = Double.valueOf(value);
					else if (key.equalsIgnoreCase("speed_check_attack_frame_rate"))
						speed_check_attack_frame_rate = Double.valueOf(value);
					else if (key.equalsIgnoreCase("delay_hack_count"))
						delay_hack_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("delay_hack_frame_rate"))
						delay_hack_frame_rate = Double.valueOf(value);
					else if (key.equalsIgnoreCase("gost_hack_block_time"))
						gost_hack_block_time = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("is_auto_hunt_check"))
						is_auto_hunt_check = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("auto_hunt_monster_kill_count"))
						auto_hunt_monster_kill_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("is_auto_hunt_check_skill"))
						is_auto_hunt_check_skill = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("auto_hunt_answer_time"))
						auto_hunt_answer_time = Integer.valueOf(value) * 1000;
					else if (key.equalsIgnoreCase("is_batpomet_system"))
						is_batpomet_system = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("baphomet_system_criminal"))
						baphomet_system_criminal = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("recovery_weapon_safe_0_en_min"))
						recovery_weapon_safe_0_en_min = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("recovery_weapon_safe_6_en_min"))
						recovery_weapon_safe_6_en_min = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("recovery_armor_safe_0_en_min"))
						recovery_armor_safe_0_en_min = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("recovery_armor_safe_4_en_min"))
						recovery_armor_safe_4_en_min = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("recovery_armor_safe_6_en_min"))
						recovery_armor_safe_6_en_min = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("recovery_time"))
						recovery_time = Integer.valueOf(value) * 1000;
					else if (key.equalsIgnoreCase("is_recovery_scroll"))
						is_recovery_scroll = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("weapon_safe_0_0_recovery_item"))
						weapon_safe_0_0_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_0_0_recovery_item_count"))
						weapon_safe_0_0_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_0_1_recovery_item"))
						weapon_safe_0_1_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_0_1_recovery_item_count"))
						weapon_safe_0_1_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_0_2_recovery_item"))
						weapon_safe_0_2_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_0_2_recovery_item_count"))
						weapon_safe_0_2_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_0_3_recovery_item"))
						weapon_safe_0_3_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_0_3_recovery_item_count"))
						weapon_safe_0_3_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_0_4_recovery_item"))
						weapon_safe_0_4_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_0_4_recovery_item_count"))
						weapon_safe_0_4_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_0_5_recovery_item"))
						weapon_safe_0_5_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_0_5_recovery_item_count"))
						weapon_safe_0_5_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_0_6_recovery_item"))
						weapon_safe_0_6_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_0_6_recovery_item_count"))
						weapon_safe_0_6_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_0_7_recovery_item"))
						weapon_safe_0_7_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_0_7_recovery_item_count"))
						weapon_safe_0_7_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_0_8_recovery_item"))
						weapon_safe_0_8_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_0_8_recovery_item_count"))
						weapon_safe_0_8_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_0_9_recovery_item"))
						weapon_safe_0_9_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_0_9_recovery_item_count"))
						weapon_safe_0_9_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_0_10_recovery_item"))
						weapon_safe_0_10_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_0_10_recovery_item_count"))
						weapon_safe_0_10_recovery_item_count = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("weapon_safe_6_6_recovery_item"))
						weapon_safe_6_6_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_6_6_recovery_item_count"))
						weapon_safe_6_6_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_6_7_recovery_item"))
						weapon_safe_6_7_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_6_7_recovery_item_count"))
						weapon_safe_6_7_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_6_8_recovery_item"))
						weapon_safe_6_8_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_6_8_recovery_item_count"))
						weapon_safe_6_8_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_6_9_recovery_item"))
						weapon_safe_6_9_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_6_9_recovery_item_count"))
						weapon_safe_6_9_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_6_10_recovery_item"))
						weapon_safe_6_10_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_6_10_recovery_item_count"))
						weapon_safe_6_10_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_6_11_recovery_item"))
						weapon_safe_6_11_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_6_11_recovery_item_count"))
						weapon_safe_6_11_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_6_12_recovery_item"))
						weapon_safe_6_12_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_6_12_recovery_item_count"))
						weapon_safe_6_12_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_6_13_recovery_item"))
						weapon_safe_6_13_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_6_13_recovery_item_count"))
						weapon_safe_6_13_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_6_14_recovery_item"))
						weapon_safe_6_14_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_6_14_recovery_item_count"))
						weapon_safe_6_14_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("weapon_safe_6_15_recovery_item"))
						weapon_safe_6_15_recovery_item = value;
					else if (key.equalsIgnoreCase("weapon_safe_6_15_recovery_item_count"))
						weapon_safe_6_15_recovery_item_count = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("armor_safe_0_0_recovery_item"))
						armor_safe_0_0_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_0_0_recovery_item_count"))
						armor_safe_0_0_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_0_1_recovery_item"))
						armor_safe_0_1_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_0_1_recovery_item_count"))
						armor_safe_0_1_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_0_2_recovery_item"))
						armor_safe_0_2_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_0_2_recovery_item_count"))
						armor_safe_0_2_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_0_3_recovery_item"))
						armor_safe_0_3_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_0_3_recovery_item_count"))
						armor_safe_0_3_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_0_4_recovery_item"))
						armor_safe_0_4_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_0_4_recovery_item_count"))
						armor_safe_0_4_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_0_5_recovery_item"))
						armor_safe_0_5_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_0_5_recovery_item_count"))
						armor_safe_0_5_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_0_6_recovery_item"))
						armor_safe_0_6_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_0_6_recovery_item_count"))
						armor_safe_0_6_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_0_7_recovery_item"))
						armor_safe_0_7_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_0_7_recovery_item_count"))
						armor_safe_0_7_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_0_8_recovery_item"))
						armor_safe_0_8_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_0_8_recovery_item_count"))
						armor_safe_0_8_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_0_9_recovery_item"))
						armor_safe_0_9_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_0_9_recovery_item_count"))
						armor_safe_0_9_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_0_10_recovery_item"))
						armor_safe_0_10_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_0_10_recovery_item_count"))
						armor_safe_0_10_recovery_item_count = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("armor_safe_4_4_recovery_item"))
						armor_safe_4_4_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_4_4_recovery_item_count"))
						armor_safe_4_4_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_4_5_recovery_item"))
						armor_safe_4_5_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_4_5_recovery_item_count"))
						armor_safe_4_5_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_4_6_recovery_item"))
						armor_safe_4_6_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_4_6_recovery_item_count"))
						armor_safe_4_6_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_4_7_recovery_item"))
						armor_safe_4_7_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_4_7_recovery_item_count"))
						armor_safe_4_7_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_4_8_recovery_item"))
						armor_safe_4_8_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_4_8_recovery_item_count"))
						armor_safe_4_8_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_4_9_recovery_item"))
						armor_safe_4_9_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_4_9_recovery_item_count"))
						armor_safe_4_9_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_4_10_recovery_item"))
						armor_safe_4_10_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_4_10_recovery_item_count"))
						armor_safe_4_10_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_4_11_recovery_item"))
						armor_safe_4_11_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_4_11_recovery_item_count"))
						armor_safe_4_11_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_4_12_recovery_item"))
						armor_safe_4_12_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_4_12_recovery_item_count"))
						armor_safe_4_12_recovery_item_count = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("armor_safe_6_6_recovery_item"))
						armor_safe_6_6_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_6_6_recovery_item_count"))
						armor_safe_6_6_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_6_7_recovery_item"))
						armor_safe_6_7_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_6_7_recovery_item_count"))
						armor_safe_6_7_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_6_8_recovery_item"))
						armor_safe_6_8_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_6_8_recovery_item_count"))
						armor_safe_6_8_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_6_9_recovery_item"))
						armor_safe_6_9_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_6_9_recovery_item_count"))
						armor_safe_6_9_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_6_10_recovery_item"))
						armor_safe_6_10_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_6_10_recovery_item_count"))
						armor_safe_6_10_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_6_11_recovery_item"))
						armor_safe_6_11_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_6_11_recovery_item_count"))
						armor_safe_6_11_recovery_item_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("armor_safe_6_12_recovery_item"))
						armor_safe_6_12_recovery_item = value;
					else if (key.equalsIgnoreCase("armor_safe_6_12_recovery_item_count"))
						armor_safe_6_12_recovery_item_count = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("speed_check_no_dir_magic_frame_rate"))
						speed_check_no_dir_magic_frame_rate = Double.valueOf(value);
					else if (key.equalsIgnoreCase("speed_check_dir_magic_frame_rate"))
						speed_check_dir_magic_frame_rate = Double.valueOf(value);

					else if (key.equalsIgnoreCase("attackAndMagic_delay"))
						attackAndMagic_delay = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("clan_warehouse_message"))
						clan_warehouse_message = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("character_delete"))
						character_delete = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("dungeon_parttime_message"))
						dungeon_parttime_message = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("rank_filter_names")) {
						StringBuffer sb = new StringBuffer();
						for (String k : value.split(","))
							sb.append("name!='").append(k).append("' AND ");
						rank_filter_names_query = sb.substring(0, sb.length() - 5);
					} else if (key.equalsIgnoreCase("is_auto_fishing"))
						is_auto_fishing = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("fish_delay"))
						fish_delay = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("fish_exp"))
						fish_exp = value;
					else if (key.equalsIgnoreCase("auto_fish_level"))
						auto_fish_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("auto_fish_coin"))
						auto_fish_coin = value;
					else if (key.equalsIgnoreCase("auto_fish_expense"))
						auto_fish_expense = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("fish_rice"))
						fish_rice = value;
					else if (key.equalsIgnoreCase("healingpotion_message"))
						healingpotion_message = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("letter_price"))
						letter_price = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("letter_clan_price"))
						letter_clan_price = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("player_hellsystem_pk_count"))
						player_hellsystem_pk_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("player_hellsystem_delay"))
						player_hellsystem_delay = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("auto_save_time"))
						auto_save_time = Long.valueOf(value) * 1000;
					else if (key.equalsIgnoreCase("memory_recycle"))
						memory_recycle = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("item_print"))
						item_print = value;

					else if (key.equalsIgnoreCase("buff_exp"))
						buff_exp = Double.valueOf(value);

					else if (key.equalsIgnoreCase("cracker_exp_max_level"))
						cracker_exp_max_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("exp_marble_min"))
						exp_marble_min = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("exp_marble_max"))
						exp_marble_max = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("exp2_marble_min"))
						exp2_marble_min = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("exp2_marble_max"))
						exp2_marble_max = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("is_quest_present"))
						is_quest_present = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("quiz_question_present"))
						toFirstInventory(quiz_question_present, value);

					else if (key.equalsIgnoreCase("is_level_quest"))
						is_level_quest = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("is_repeat_quest"))
						is_repeat_quest = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("is_sword_lack_check"))
						is_sword_lack_check = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("is_character_dead_effect"))
						is_character_dead_effect = value.equalsIgnoreCase("true");

					// 보스 스킬 , 어택의 전체 타격 수치 변경하기
					else if (key.equalsIgnoreCase("eng_bolt"))
						eng_bolt = Double.valueOf(value);
					else if (key.equalsIgnoreCase("light"))
						light = Double.valueOf(value);
					else if (key.equalsIgnoreCase("tornado"))
						tornado = Double.valueOf(value);
					else if (key.equalsIgnoreCase("chill"))
						chill = Double.valueOf(value);
					else if (key.equalsIgnoreCase("freezing"))
						freezing = Double.valueOf(value);
					else if (key.equalsIgnoreCase("disslight"))
						disslight = Double.valueOf(value);
					else if (key.equalsIgnoreCase("diss"))
						diss = Double.valueOf(value);
					else if (key.equalsIgnoreCase("mon_att1"))
						mon_att1 = Double.valueOf(value);
					else if (key.equalsIgnoreCase("mon_att2"))
						mon_att2 = Double.valueOf(value);
					else if (key.equalsIgnoreCase("mon_att_non_r1"))
						mon_att_non_r1 = Double.valueOf(value);
					else if (key.equalsIgnoreCase("mon_att_non_r2"))
						mon_att_non_r2 = Double.valueOf(value);
					else if (key.equalsIgnoreCase("mon_att_tar_r1"))
						mon_att_tar_r1 = Double.valueOf(value);
					else if (key.equalsIgnoreCase("mon_att_tar_r2"))
						mon_att_tar_r2 = Double.valueOf(value);
					else if (key.equalsIgnoreCase("mon_att_tar_r_b"))
						mon_att_tar_r_b = Double.valueOf(value);
					else if (key.equalsIgnoreCase("max_level_exp_panelty"))
						max_level_exp_panelty = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("speedhack_walk"))
						speedhack_walk = Long.valueOf(value);
					else if (key.equalsIgnoreCase("speedhack_attack "))
						speedhack_attack = Long.valueOf(value);
					else if (key.equalsIgnoreCase("clan_exp_user"))
						clan_exp_user = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("jangin_armor_6"))
						jangin_armor_6 = Double.valueOf(value);
					else if (key.equalsIgnoreCase("jangin_armor_7"))
						jangin_armor_7 = Double.valueOf(value);
					else if (key.equalsIgnoreCase("jangin_armor_8"))
						jangin_armor_8 = Double.valueOf(value);
					else if (key.equalsIgnoreCase("jangin_armor_9"))
						jangin_armor_9 = Double.valueOf(value);
					else if (key.equalsIgnoreCase("jangin_weapon_9"))
						jangin_weapon_9 = Double.valueOf(value);

					else if (key.equalsIgnoreCase("last_dungeon1"))
						last_dungeon1 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("last_dungeon2"))
						last_dungeon2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("last_dungeon3"))
						last_dungeon3 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("pklevel"))
						pklevel = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("last_dungeon"))
						last_dungeon = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("is_item_drop_msg_name"))
						is_item_drop_msg_name = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("is_item_drop_msg_life"))
						is_item_drop_msg_life = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("charlock"))
						charlock = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("buff_level"))
						buff_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("no_remove_item"))
						isRemoveItem(no_remove_item, value);
					else if (key.equalsIgnoreCase("safe0weapon0"))
						safe0weapon0 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0weapon1"))
						safe0weapon1 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0weapon2"))
						safe0weapon2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0weapon3"))
						safe0weapon3 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0weapon4"))
						safe0weapon4 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0weapon5"))
						safe0weapon5 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0weapon6"))
						safe0weapon6 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0weapon7"))
						safe0weapon7 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0weapon8"))
						safe0weapon8 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0weapon9"))
						safe0weapon9 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0weapon10"))
						safe0weapon10 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("ssafeweapon_rnd2"))
						ssafeweapon_rnd2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafearmor6_5_7rnd2"))
						ssafearmor6_5_7rnd2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafearmor4_5_7rnd2"))
						ssafearmor4_5_7rnd2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafearmor4_4_6rnd2"))
						ssafearmor4_4_6rnd2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafearmor4_3_5rnd2"))
						ssafearmor4_3_5rnd2 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("ssafe0weapon0"))
						ssafe0weapon0 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0weapon1"))
						ssafe0weapon1 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0weapon2"))
						ssafe0weapon2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0weapon3"))
						ssafe0weapon3 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0weapon4"))
						ssafe0weapon4 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0weapon5"))
						ssafe0weapon5 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0weapon6"))
						ssafe0weapon6 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0weapon7"))
						ssafe0weapon7 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0weapon8"))
						ssafe0weapon8 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0weapon9"))
						ssafe0weapon9 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0weapon10"))
						ssafe0weapon10 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("safe6weapon6"))
						safe6weapon6 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe6weapon7"))
						safe6weapon7 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe6weapon8"))
						safe6weapon8 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe6weapon9"))
						safe6weapon9 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe6weapon10"))
						safe6weapon10 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("ssafe6weapon6"))
						ssafe6weapon6 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe6weapon7"))
						ssafe6weapon7 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe6weapon8"))
						ssafe6weapon8 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe6weapon9"))
						ssafe6weapon9 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe6weapon10"))
						ssafe6weapon10 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("safe0armor0"))
						safe0armor0 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0armor1"))
						safe0armor1 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0armor2"))
						safe0armor2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0armor3"))
						safe0armor3 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0armor4"))
						safe0armor4 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0armor5"))
						safe0armor5 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0armor6"))
						safe0armor6 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0armor7"))
						safe0armor7 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0armor8"))
						safe0armor8 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe0armor9"))
						safe0armor9 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("safe4armor4"))
						safe4armor4 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe4armor5"))
						safe4armor5 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe4armor6"))
						safe4armor6 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe4armor7"))
						safe4armor7 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe4armor8"))
						safe4armor8 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe4armor9"))
						safe4armor9 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("safe6armor6"))
						safe6armor6 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe6armor7"))
						safe6armor7 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe6armor8"))
						safe6armor8 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("safe6armor9"))
						safe6armor9 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("sssafe0armor0"))
						ssafe0armor0 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0armor1"))
						ssafe0armor1 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0armor2"))
						ssafe0armor2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0armor3"))
						ssafe0armor3 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0armor4"))
						ssafe0armor4 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0armor5"))
						ssafe0armor5 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0armor6"))
						ssafe0armor6 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0armor7"))
						ssafe0armor7 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0armor8"))
						ssafe0armor8 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe0armor9"))
						ssafe0armor9 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("ssafe4armor4"))
						ssafe4armor4 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe4armor5"))
						ssafe4armor5 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe4armor6"))
						ssafe4armor6 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe4armor7"))
						ssafe4armor7 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe4armor8"))
						ssafe4armor8 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe4armor9"))
						ssafe4armor9 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("ssafe6armor6"))
						ssafe6armor6 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe6armor7"))
						ssafe6armor7 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe6armor8"))
						ssafe6armor8 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("ssafe6armor9"))
						ssafe6armor9 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("acc0"))
						acc0 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("acc1"))
						acc1 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("acc2"))
						acc2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("acc3"))
						acc3 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("acc4"))
						acc4 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("acc5"))
						acc5 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("acc6"))
						acc6 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("acc7"))
						acc7 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("enchant_safe6dmg7"))
						enchant_safe6dmg7 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("enchant_safe6dmg8"))
						enchant_safe6dmg8 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("enchant_safe6dmg9"))
						enchant_safe6dmg9 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("enchant_safe6dmg10"))
						enchant_safe6dmg10 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("enchant_safe6dmg11"))
						enchant_safe6dmg11 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("enchant_safe0dmg1"))
						enchant_safe0dmg1 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("enchant_safe0dmg2"))
						enchant_safe0dmg2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("enchant_safe0dmg3"))
						enchant_safe0dmg3 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("enchant_safe0dmg4"))
						enchant_safe0dmg4 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("enchant_safe0dmg5"))
						enchant_safe0dmg5 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("enchant_safe0dmg6"))
						enchant_safe0dmg6 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("enchant_safe0dmg7"))
						enchant_safe0dmg7 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("enchant_safe0dmg8"))
						enchant_safe0dmg8 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("enchant_safe0dmg9"))
						enchant_safe0dmg9 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("enchant_safe0dmg10"))
						enchant_safe0dmg10 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("adena_trade_regedit_min_level"))
						adena_trade_regedit_min_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("pc_trade_sale_max_count"))
						pc_trade_sale_max_count = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("adena_trade_regedit_price"))
						adena_trade_regedit_price = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("adena_trade_regedit_min_value"))
						adena_trade_regedit_min_value = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("adena_trade_regedit_min_valueone"))
						adena_trade_regedit_min_valueone = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("adena_trade_regedit_min_valuetwo"))
						adena_trade_regedit_min_valuetwo = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("adena_trade_regedit_cancel_hour"))
						adena_trade_regedit_cancel_hour = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("adena_trade_regedit_cancel_rate"))
						adena_trade_regedit_cancel_rate = Double.valueOf(value);

					else if (key.equalsIgnoreCase("potion_message"))
						potion_message = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("stun"))
						stun = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("tryple"))
						tryple = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("imm_dmg"))
						imm_dmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("imm_dmg_skill"))
						imm_dmg_skill = Double.valueOf(value);
					else if (key.equalsIgnoreCase("dis_min"))
						dis_min = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dis_max"))
						dis_max = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("banshiled"))
						banshiled = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("sword_rack_delay"))
						sword_rack_delay = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("can_int18_mr59"))
						can_int18_mr59 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int18_mr69"))
						can_int18_mr69 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int18_mr79"))
						can_int18_mr79 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int18_mr89"))
						can_int18_mr89 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int18_mr99"))
						can_int18_mr99 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int18_mr109"))
						can_int18_mr109 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int18_mr119"))
						can_int18_mr119 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("can_int22_mr59"))
						can_int22_mr59 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int22_mr69"))
						can_int22_mr69 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int22_mr79"))
						can_int22_mr79 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int22_mr89"))
						can_int22_mr89 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int22_mr99"))
						can_int22_mr99 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int22_mr109"))
						can_int22_mr109 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int22_mr119"))
						can_int22_mr119 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("can_int26_mr59"))
						can_int26_mr59 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int26_mr69"))
						can_int26_mr69 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int26_mr79"))
						can_int26_mr79 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int26_mr89"))
						can_int26_mr89 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int26_mr99"))
						can_int26_mr99 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int26_mr109"))
						can_int26_mr109 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int26_mr119"))
						can_int26_mr119 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("can_int30_mr59"))
						can_int30_mr59 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int30_mr69"))
						can_int30_mr69 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int30_mr79"))
						can_int30_mr79 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int30_mr89"))
						can_int30_mr89 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int30_mr99"))
						can_int30_mr99 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int30_mr109"))
						can_int30_mr109 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int30_mr119"))
						can_int30_mr119 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("can_int35_mr59"))
						can_int35_mr59 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int35_mr69"))
						can_int35_mr69 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int35_mr79"))
						can_int35_mr79 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int35_mr89"))
						can_int35_mr89 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int35_mr99"))
						can_int35_mr99 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int35_mr109"))
						can_int35_mr109 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("can_int35_mr119"))
						can_int35_mr119 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("int18_mr59"))
						int18_mr59 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int18_mr69"))
						int18_mr69 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int18_mr79"))
						int18_mr79 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int18_mr89"))
						int18_mr89 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int18_mr99"))
						int18_mr99 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int18_mr109"))
						int18_mr109 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int18_mr119"))
						int18_mr119 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("int22_mr59"))
						int22_mr59 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int22_mr69"))
						int22_mr69 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int22_mr79"))
						int22_mr79 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int22_mr89"))
						int22_mr89 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int22_mr99"))
						int22_mr99 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int22_mr109"))
						int22_mr109 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int22_mr119"))
						int22_mr119 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("int26_mr59"))
						int26_mr59 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int26_mr69"))
						int26_mr69 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int26_mr79"))
						int26_mr79 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int26_mr89"))
						int26_mr89 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int26_mr99"))
						int26_mr99 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int26_mr109"))
						int26_mr109 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int26_mr119"))
						int26_mr119 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("int30_mr59"))
						int30_mr59 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int30_mr69"))
						int30_mr69 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int30_mr79"))
						int30_mr79 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int30_mr89"))
						int30_mr89 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int30_mr99"))
						int30_mr99 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int30_mr109"))
						int30_mr109 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int30_mr119"))
						int30_mr119 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("int35_mr59"))
						int35_mr59 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int35_mr69"))
						int35_mr69 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int35_mr79"))
						int35_mr79 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int35_mr89"))
						int35_mr89 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int35_mr99"))
						int35_mr99 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int35_mr109"))
						int35_mr109 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("int35_mr119"))
						int35_mr119 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("armor_ac"))
						armor_ac = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("kingdom_clan_join"))
						kingdom_clan_join = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("is_two_clan_join"))
						is_two_clan_join = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("enfail_max"))
						enfail_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("enfail"))
						enfail = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("server_name"))
						server_name = value == null || value.length() < 2 ? "없음" : value;
					else if (key.equalsIgnoreCase("master_name"))
						master_name = value == null || value.length() < 2 ? "없음" : value;

					else if (key.equalsIgnoreCase("new_clan_name")) {
						new_clan_name = value == null || value.length() < 2 ? "신규혈맹없습니다." : value;

						if (!reload) {
							new_clan_name_temp = new_clan_name;
						} else {
							ClanController.reloadNewClan(new_clan_name_temp, new_clan_name);
						}
					} else if (key.equalsIgnoreCase("is_new_clan_auto_out"))
						is_new_clan_auto_out = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("clan_max"))
						clan_max = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("new_clan_max_level"))
						new_clan_max_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("is_new_clan_pvp"))
						is_new_clan_pvp = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("is_new_clan_attack_boss"))
						is_new_clan_attack_boss = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("is_no_clan_pvp"))
						is_no_clan_pvp = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("is_no_clan_attack_boss"))
						is_no_clan_attack_boss = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("is_new_clan_oman_top"))
						is_new_clan_oman_top = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("oman_min_level"))
						oman_min_level = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("kingdom_war_no_remove_item_kent"))
						isKingdomWarNoRemoveItem(kingdom_war_no_remove_item_kent, value);
					else if (key.equalsIgnoreCase("kingdom_war_no_remove_item_orcish"))
						isKingdomWarNoRemoveItem(kingdom_war_no_remove_item_orcish, value);
					else if (key.equalsIgnoreCase("kingdom_war_no_remove_item_windawood"))
						isKingdomWarNoRemoveItem(kingdom_war_no_remove_item_windawood, value);
					else if (key.equalsIgnoreCase("kingdom_war_no_remove_item_giran"))
						isKingdomWarNoRemoveItem(kingdom_war_no_remove_item_giran, value);
					else if (key.equalsIgnoreCase("kingdom_war_no_remove_item_heine"))
						isKingdomWarNoRemoveItem(kingdom_war_no_remove_item_heine, value);
					else if (key.equalsIgnoreCase("kingdom_war_no_remove_item_abyss"))
						isKingdomWarNoRemoveItem(kingdom_war_no_remove_item_abyss, value);
					else if (key.equalsIgnoreCase("kingdom_war_no_remove_item_aden"))
						isKingdomWarNoRemoveItem(kingdom_war_no_remove_item_aden, value);

					else if (key.equalsIgnoreCase("giran_kingdom_war_day_list"))
						kingdomDay(giran_kingdom_war_day_list, value);

					else if (key.equalsIgnoreCase("is_chatting_clan"))
						is_chatting_clan = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("useradenasise"))
						useradenasise = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("useradenasise2"))
						useradenasise2 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("adminadenasise"))
						adminadenasise = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("adminadenasise2"))
						adminadenasise2 = Integer.valueOf(value);

					else if (key.equalsIgnoreCase("adena_cancle_per"))
						adena_cancle_per = Double.valueOf(value);

					else if (key.equalsIgnoreCase("move_hack_defence"))
						move_hack_defence = value.equalsIgnoreCase("true");

					else if (key.equalsIgnoreCase("move_hack_defence_log"))
						move_hack_defence_log = value.equalsIgnoreCase("true");
					// else if (key.equalsIgnoreCase("attack_pvp_2_rate"))
					// attack_pvp_2_rate = Double.valueOf(value) * 0.01;
					// new 펜던트
					else if (key.equalsIgnoreCase("is_immortality"))
						is_immortality = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("is_immortality_pvp"))
						is_immortality_pvp = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("immortality_item_name"))
						immortality_item_name = value;
					else if (key.equalsIgnoreCase("immortality_item_name2"))
						immortality_item_name2 = value;
					else if (key.equalsIgnoreCase("immortality_item_name3"))
						immortality_item_name3 = value;
					else if (key.equalsIgnoreCase("immortality_aden_percent"))
						immortality_aden_percent = Double.valueOf(value) * 0.01;
					else if (key.equalsIgnoreCase("immortality_exp"))
						immortality_exp = Double.valueOf(value) * 0.01;
					else if (key.equalsIgnoreCase("is_immortality_kill_item"))
						is_immortality_kill_item = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("immortality_kill_item_name"))
						immortality_kill_item_name = value;
					else if (key.equalsIgnoreCase("is_immortality_kill_item_drop"))
						is_immortality_kill_item_drop = value.equalsIgnoreCase("true");
					else if (key.equalsIgnoreCase("is_immortality_kill_item_drop_monster"))
						is_immortality_kill_item_drop_monster = value.equalsIgnoreCase("true");

				}
			}
			lnrr.close();
			BackgroundDatabase.reloadSpawnBattleZone();
		} catch (Exception e) {
			lineage.share.System.printf("%s : init()\r\n", Lineage.class.toString());
			lineage.share.System.println(e);
			e.printStackTrace();
		}

		// 315 이상부터 해상도가 변경되기때문에
		if (server_version > 310) {
			SEARCH_LOCATIONRANGE = 16;
			SEARCH_MONSTER_TARGET_LOCATION = 21;
		}
		// 380 각반 업데이트 된 후로 슬롯 번호가 변경됨.
		if (server_version >= 380) {
			SLOT_BOOTS += 1;
			SLOT_GLOVE += 1;
			SLOT_SHIELD += 1;
			SLOT_WEAPON += 1;
			SLOT_NECKLACE += 1;
			SLOT_BELT += 1;
			SLOT_EARRING += 1;
			SLOT_RING1 += 1;
			SLOT_RING2 += 1;
			SLOT_RING3 += 1;
			SLOT_RING4 += 1;
			SLOT_AMULET1 += 1;
			SLOT_AMULET2 += 1;
			SLOT_AMULET3 += 1;
			SLOT_AMULET4 += 1;
			SLOT_AMULET5 += 1;
			SLOT_GARDER += 1;
		}

		TimeLine.end();
	}

	/**
	 * 순차적으로 배열된 클레스값을 제곱순에 클레스값으로 리턴하는 함수.
	 * 
	 * @param class_type
	 * @return
	 */
	static public int getClassType(int class_type) {
		switch (class_type) {
			case LINEAGE_CLASS_ROYAL:
				return LINEAGE_ROYAL;
			case LINEAGE_CLASS_KNIGHT:
				return LINEAGE_KNIGHT;
			case LINEAGE_CLASS_ELF:
				return LINEAGE_ELF;
			case LINEAGE_CLASS_WIZARD:
				return LINEAGE_WIZARD;
			case LINEAGE_CLASS_DARKELF:
				return LINEAGE_DARKELF;
			case LINEAGE_CLASS_DRAGONKNIGHT:
				return 0;
			case LINEAGE_CLASS_BLACKWIZARD:
				return 0;
		}
		return 0;
	}

	static public String getClassTypeName(int type) {
		switch (type) {
			case 0x00:
				return "군주";
			case 0x01:
				return "기사";
			case 0x02:
				return "요정";
			case 0x03:
				return "마법사";
			case 0x04:
				return "다크엘프";
			case 0x05:
				return "드래곤기사";
			case 0x06:
				return "블랙위자드";
			case 0x0A:
				return "몬스터";
			case 0x0B:
				return "소환";
		}
		return null;
	}

	/**
	 * 아이템 제거 막대로 삭제 불가능한 아이템 목록 2017-10-24 by all-night
	 */
	static private void isRemoveItem(List<String> list, String value) {
		StringTokenizer st = new StringTokenizer(value, ",");
		while (st.hasMoreTokens()) {
			String item = st.nextToken().trim();
			list.add(item);
		}
	}

	/**
	 * 공성중 소모되지 않는 아이템 리스트 2018-08-04 by connector12@nate.com
	 */
	static private void isKingdomWarNoRemoveItem(List<String> list, String value) {
		if (value.length() > 0) {
			StringTokenizer st = new StringTokenizer(value, ",");
			while (st.hasMoreTokens()) {
				try {
					String item = st.nextToken().trim();
					list.add(item);
				} catch (Exception e) {
					lineage.share.System.printf("%s : isKingdomWarNoRemoveItem(List<String> list, String value)\r\n",
							Lineage.class.toString());
					lineage.share.System.println(e);
				}
			}
		}
	}

	static private void kingdomDay(List<Integer> list, String value) {
		if (value.length() > 0) {
			StringTokenizer st = new StringTokenizer(value, ",");
			while (st.hasMoreTokens()) {
				try {
					String day = st.nextToken().trim();
					list.add(Integer.valueOf(day));
				} catch (Exception e) {
					lineage.share.System.printf("%s : kingdomDay(List<Integer> list, String value)\r\n",
							Lineage.class.toString());
					lineage.share.System.println(e);
				}
			}
		}
	}

	static public List<Integer> getGiranKingdomWarDayList() {
		synchronized (giran_kingdom_war_day_list) {
			return new ArrayList<Integer>(giran_kingdom_war_day_list);
		}
	}

	/**
	 * 공성전 보상 아이템 이름 리스트 2017-10-24 by all-night
	 */
	static private void kingdomWarWinItemList(List<String> list, String value) {
		if (value.length() > 0) {
			StringTokenizer st = new StringTokenizer(value, ",");
			while (st.hasMoreTokens()) {
				try {
					String item = st.nextToken().trim();
					list.add(item);
				} catch (Exception e) {
					lineage.share.System.printf("%s : kingdomWarWinItemList(List<String> list, String value)\r\n",
							Lineage.class.toString());
					lineage.share.System.println(e);
				}
			}
		}
	}

	static private void toFirstSpell(List<FirstSpell> list, String value) {
		StringTokenizer st = new StringTokenizer(value, ",");
		while (st.hasMoreTokens()) {
			int uid = Integer.valueOf(st.nextToken());
			if (uid > 0)
				list.add(new FirstSpell(uid));
		}
	}

	static private void toFirstInventory(List<FirstInventory> list, String value) {
		StringTokenizer st = new StringTokenizer(value, ",");
		while (st.hasMoreTokens()) {
			String db = st.nextToken().trim();
			int f_pos = db.lastIndexOf("(");
			int e_pos = db.lastIndexOf(")");
			String name = db.substring(0, f_pos).trim();
			int count = Integer.valueOf(db.substring(f_pos + 1, e_pos).trim());
			list.add(new FirstInventory(name, count));
		}
	}

	static private void toFirstSpawn(List<FirstSpawn> list, String value) {
		StringTokenizer st = new StringTokenizer(value, ",");
		while (st.hasMoreTokens()) {
			String db = st.nextToken().trim();
			String x = db.substring(0, db.indexOf(" ")).trim();
			String y = db.substring(x.length() + 1, db.indexOf(" ", x.length() + 1)).trim();
			String map = db.substring(x.length() + y.length() + 2).trim();
			list.add(new FirstSpawn(Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(map)));
		}
	}

	/**
	 * 귀환 마을 좌표 2018-08-11 by connector12@nate.com
	 */
	public static int[] getHomeXY() {
		// x, y좌표
		int x = Util.random(33424, 33437);
		int y = Util.random(32801, 32824);
		// 맵번호
		int map = 4;
		int[] loc = { x, y, map };
		int count = 0;

		for (;;) {
			if (count++ > 2) {
				loc[0] = 33430;
				loc[1] = 32817;
				break;
			}

			if ((x >= 33423 && x <= 33425 && y >= 32807 && y <= 32809)
					|| (x >= 33434 && x <= 33436 && y >= 32807 && y <= 32809)
					|| (x >= 33423 && x <= 33425 && y >= 32818 && y <= 32820)
					|| (x >= 33433 && x <= 33435 && y >= 32818 && y <= 32820) || (x == 33430 && y == 32805)
					|| (x == 33429 && y == 32822) || (x == 33430 && y == 32813) || (x == 33424 && y == 32822)) {
				// x, y좌표
				x = Util.random(33424, 33437);
				y = Util.random(32801, 32824);
			} else {
				loc[0] = x;
				loc[1] = y;
				break;
			}
		}
		return loc;
	}

}
