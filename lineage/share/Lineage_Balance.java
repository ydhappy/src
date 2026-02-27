package lineage.share;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lineage.bean.database.FirstInventory;

public final class Lineage_Balance {
	// 레벨업시 캐릭터의 HP 증가량 조절.
	public static double pvp_skill_dmg_mon;
	public static double pvp_skill_dmg_royal;
	public static double pvp_skill_dmg_knight;
	public static double pvp_skill_dmg_elf;
	public static double pvp_skill_dmg_wizard;
	public static double pvp_skill_dmg_KvsW;
	public static double pvp_dmg_royal;
	public static double pvp_dmg_knight;
	public static double pvp_dmg_elf;
	public static double pvp_dmg_wizard;
	public static double pvp_dmg_mon;
	public static double pvp_dmg_elf_near;
	public static double pvp_dmg_elf_near_wizard;
	public static double mon_dmg_knight;
	public static double mon_dmg_royal;
	public static double mon_dmg_elf;
	public static double mon_dmg_wizard;
	
	static public double monsetr_hp100_mindmg;
	static public double monsetr_hp100_maxdmg;
	static public double monsetr_hp200_mindmg;
	static public double monsetr_hp200_maxdmg;
	static public double monsetr_hp300_mindmg;
	static public double monsetr_hp300_maxdmg;
	static public double monsetr_hp400_mindmg;
	static public double monsetr_hp400_maxdmg;
	static public double monsetr_hp655_mindmg;
	static public double monsetr_hp655_maxdmg;
	static public double monsetr_hp900_mindmg;
	static public double monsetr_hp900_maxdmg;
	static public double monsetr_hp1400_mindmg;
	static public double monsetr_hp1400_maxdmg;
	static public double monsetr_hp14000_mindmg;
	static public double monsetr_hp14000_maxdmg;
	static public double monsetr_hp_boss_mindmg;
	static public double monsetr_hp_boss_maxdmg;
	
	// 레벨업시 캐릭터의 HP 증가량 조절.
	static public double level_up_hp_royal;
	static public double level_up_hp_knight;
	static public double level_up_hp_elf;
	static public double level_up_hp_wizard;
	// 레벨업시 캐릭터의 MP 증가량 조절.
	static public double level_up_mp_royal;
	static public double level_up_mp_knight;
	static public double level_up_mp_elf;
	static public double level_up_mp_wizard;

	// 흑장로 마법인형 확율 외부화
	static public int magicDoll_black_elder_persent;
	// 흑장로 마법인형 최소 대미지 외부화
	static public int magicDoll_black_elder_min_damage;
	// 흑장로 마법인형 최대 대미지 외부화
	static public int magicDoll_black_elder_max_damage;
	// 데스 나이트 마법인형 확율 외부화
	static public int magicDoll_death_knight_persent;
	// 데스 나이트 마법인형 최소 대미지 외부화
	static public int magicDoll_death_knight_min_damage;
	// 데스 나이트 마법인형 최대 대미지 외부화
	static public int magicDoll_death_knight_max_damage;
	
	// 기르타스 마법인형 확율 외부화
	static public int magicDoll_girtas_persent;
	// 기르타스 마법인형 최소 대미지 외부화
	static public int magicDoll_girtas_min_damage;
	// 기르타스 마법인형 최대 대미지 외부화
	static public int magicDoll_girtas_max_damage;
	
	//치명타 사용여부
	static public boolean is_critical;
	//치명타 확율 외부화
	static public int weapon_critical_persent;
	//치명타 최소 데미지 외부화
	static public int critical_Min_Dmg;
	//치명타 최대 데미지 외부화
	static public int critical_Max_Dmg;
	
	// 군주 근거리 대미지 밸런스
	static public double royal_damage_figure;
	// 기사 근거리 대미지 밸런스
	static public double knight_damage_figure;
	// 요정 근거리 대미지 밸런스
	static public double elf_damage_figure;
	// 마법사 근거리 대미지 밸런스
	static public double wizard_damage_figure;
	
	// 군주 근거리 명중 밸런스
	static public double royal_hit_figure;
	// 기사 근거리 명중 밸런스
	static public double knight_hit_figure;
	// 요정 근거리 명중 밸런스
	static public double elf_hit_figure;
	// 마법사 근거리 명중 밸런스
	static public double wizard_hit_figure;
	
	// 군주 원거리 대미지 밸런스
	static public double royal_bow_damage_figure;
	// 기사 원거리 대미지 밸런스
	static public double knight_bow_damage_figure;
	// 요정 원거리 대미지 밸런스
	static public double elf_bow_damage_figure;
	// 마법사 원거리 대미지 밸런스
	static public double wizard_bow_damage_figure;
	
	// 군주 원거리 명중 밸런스
	static public double royal_bow_hit_figure;
	// 기사 원거리 명중 밸런스
	static public double knight_bow_hit_figure;
	// 요정 원거리 명중 밸런스
	static public double elf_bow_hit_figure;
	// 마법사 원거리 명중 밸런스
	static public double wizard_bow_hit_figure;
	
	// 1단계 마법인형 합성 확률
		static public double magicDoll_class_1_probability;
		// 1단계 마법인형 합성 대성공 확률
		static public double magicDoll_class_1_perfect_probability;
		// 2단계 마법인형 합성 확률
		static public double magicDoll_class_2_probability;
		// 2단계 마법인형 합성 대성공 확률
		static public double magicDoll_class_2_perfect_probability;
		// 3단계 마법인형 합성 확률
		static public double magicDoll_class_3_probability;
		// 3단계 마법인형 합성 대성공 확률
		static public double magicDoll_class_3_perfect_probability;
		// 4단계 마법인형 합성 확률
		static public double magicDoll_class_4_probability;
		// 특수합성 성공 확률
		static public double magicDoll_class_5_probability;
		
		// 펫 레벨에 따른 대미지
		static public double pet_level_min_damage_rate;
		static public double pet_level_max_damage_rate;
		
		// 서먼몬스터 레벨에 따른 대미지
		static public double summon_level_min_damage_rate;
		static public double summon_level_max_damage_rate;
		
	// 스팟 타워 체력
	static public int spot_tower_hp;
	// 스팟 타워 스폰 위치 X좌표
	static public int spot_tower_x;
	// 스팟 타워 스폰 위치 Y좌표
	static public int spot_tower_y;
	// 스팟 타워 스폰 위치 맵번호
	static public int spot_tower_map;
	// 스팟 시작 시간(시간)
	static public int spot_tower_start_hour;
	// 스팟 시작 시간(분)
	static public int spot_tower_start_min;
	// 스팟 시작 진행시간(초)
	static public int spot_tower_time;
	// 스팟 승리 보상
	static public List<FirstInventory> spot_item = new ArrayList<FirstInventory>();
	// 스팟 보상 딜레이(초)
	static public int spot_item_delay;
	
	// 출석체크 마지막 요일
	static public int lastday;
	// 출석 알람 딜레이
	static public int checkment;
	// 출석체크 완료시간
	static public int dayc;
	// 출석체크
	static public String dayc0 = null;
	static public int daycc0;
	static public String dayc1 = null;
	static public int daycc1;
	static public String dayc2 = null;
	static public int daycc2;
	static public String dayc3 = null;
	static public int daycc3;
	static public String dayc4 = null;
	static public int daycc4;
	static public String dayc5 = null;
	static public int daycc5;
	static public String dayc6 = null;
	static public int daycc6;
	static public String dayc7 = null;
	static public int daycc7;
	static public String dayc8 = null;
	static public int daycc8;
	static public String dayc9 = null;
	static public int daycc9;
	static public String dayc10 = null;
	static public int daycc10;
	static public String dayc11 = null;
	static public int daycc11;
	static public String dayc12 = null;
	static public int daycc12;
	static public String dayc13 = null;
	static public int daycc13;
	static public String dayc14 = null;
	static public int daycc14;
	static public String dayc15 = null;
	static public int daycc15;
	static public String dayc16 = null;
	static public int daycc16;
	static public String dayc17 = null;
	static public int daycc17;
	static public String dayc18 = null;
	static public int daycc18;
	static public String dayc19 = null;
	static public int daycc19;
	static public String dayc20 = null;
	static public int daycc20;
	static public String dayc21 = null;
	static public int daycc21;
	static public String dayc22 = null;
	static public int daycc22;
	static public String dayc23 = null;
	static public int daycc23;
	static public String dayc24 = null;
	static public int daycc24;
	static public String dayc25 = null;
	static public int daycc25;
	static public String dayc26 = null;
	static public int daycc26;
	static public String dayc27 = null;
	static public int daycc27;
	static public String dayc28 = null;
	static public int daycc28;
	static public String dayc29 = null;
	static public int daycc29;
	static public String dayc30 = null;
	static public int daycc30;
	
	/**
	 * 리니지 밸런스에 사용되는 변수 초기화. 2017-10-05 by all-night
	 */
	static public void init() {
		TimeLine.start("Lineage_Balance..");
		
		try {
			BufferedReader lnrr = new BufferedReader(new FileReader("lineage_balance.conf"));
			String line;
			while ((line = lnrr.readLine()) != null) {
				if (line.startsWith("#"))
					continue;

				int pos = line.indexOf("=");
				if (pos > 0) {
					String key = line.substring(0, pos).trim();
					String value = line.substring(pos + 1, line.length()).trim();
					
					if (value.contains("%"))
						value = value.replace("%", "");
					
					if (key.equalsIgnoreCase("pvp_skill_dmg_mon"))
						pvp_skill_dmg_mon = Double.valueOf(value);
					else if (key.equalsIgnoreCase("pvp_skill_dmg_royal"))
						pvp_skill_dmg_royal = Double.valueOf(value);
					else if (key.equalsIgnoreCase("pvp_skill_dmg_knight"))
						pvp_skill_dmg_knight = Double.valueOf(value);
					else if (key.equalsIgnoreCase("pvp_skill_dmg_elf"))
						pvp_skill_dmg_elf = Double.valueOf(value);
					else if (key.equalsIgnoreCase("pvp_skill_dmg_KvsW"))
						pvp_skill_dmg_KvsW = Double.valueOf(value);
					else if (key.equalsIgnoreCase("pvp_skill_dmg_wizard"))
						pvp_skill_dmg_wizard = Double.valueOf(value);
					else if (key.equalsIgnoreCase("pvp_dmg_royal"))
						pvp_dmg_royal = Double.valueOf(value);
					else if (key.equalsIgnoreCase("pvp_dmg_knight"))
						pvp_dmg_knight = Double.valueOf(value);
					else if (key.equalsIgnoreCase("pvp_dmg_elf"))
						pvp_dmg_elf = Double.valueOf(value);
					else if (key.equalsIgnoreCase("pvp_dmg_wizard"))
						pvp_dmg_wizard = Double.valueOf(value);
					else if (key.equalsIgnoreCase("pvp_dmg_mon"))
						pvp_dmg_mon = Double.valueOf(value);
					else if (key.equalsIgnoreCase("pvp_dmg_elf_near"))
						pvp_dmg_elf_near = Double.valueOf(value);
					else if (key.equalsIgnoreCase("pvp_dmg_elf_near_wizard"))
						pvp_dmg_elf_near_wizard = Double.valueOf(value);
					
					else if (key.equalsIgnoreCase("summon_level_min_damage_rate"))
						summon_level_min_damage_rate = Double.valueOf(value);
					else if (key.equalsIgnoreCase("summon_level_max_damage_rate"))
						summon_level_max_damage_rate = Double.valueOf(value);
					
					else if (key.equalsIgnoreCase("pet_level_min_damage_rate"))
						pet_level_min_damage_rate = Double.valueOf(value);
					else if (key.equalsIgnoreCase("pet_level_max_damage_rate"))
						pet_level_max_damage_rate = Double.valueOf(value);
					
					else if (key.equalsIgnoreCase("mon_dmg_wizard"))
						mon_dmg_wizard = Double.valueOf(value);
					else if (key.equalsIgnoreCase("mon_dmg_elf"))
						mon_dmg_elf = Double.valueOf(value);
					else if (key.equalsIgnoreCase("mon_dmg_royal"))
						mon_dmg_royal = Double.valueOf(value);
					else if (key.equalsIgnoreCase("mon_dmg_knight"))
						mon_dmg_knight = Double.valueOf(value);
					
					
					else if (key.equalsIgnoreCase("monsetr_hp300_mindmg"))
						monsetr_hp300_mindmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp300_maxdmg"))
						monsetr_hp300_maxdmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp100_mindmg"))
						monsetr_hp100_mindmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp100_maxdmg"))
						monsetr_hp100_maxdmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp200_mindmg"))
						monsetr_hp200_mindmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp200_maxdmg"))
						monsetr_hp200_maxdmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp400_mindmg"))
						monsetr_hp400_mindmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp400_maxdmg"))
						monsetr_hp400_maxdmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp655_mindmg"))
						monsetr_hp900_maxdmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp655_maxdmg"))
						monsetr_hp655_maxdmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp900_mindmg"))
						monsetr_hp900_mindmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp900_maxdmg"))
						monsetr_hp900_maxdmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp1400_mindmg"))
						monsetr_hp1400_mindmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp1400_maxdmg"))
						monsetr_hp1400_maxdmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp14000_mindmg"))
						monsetr_hp14000_mindmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp14000_maxdmg"))
						monsetr_hp14000_maxdmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp_boss_mindmg"))
						monsetr_hp_boss_mindmg = Double.valueOf(value);
					else if (key.equalsIgnoreCase("monsetr_hp_boss_maxdmg"))
						monsetr_hp_boss_maxdmg = Double.valueOf(value);
					
					
					else if (key.equalsIgnoreCase("level_up_hp_royal"))
						level_up_hp_royal = Double.valueOf(value);
					else if (key.equalsIgnoreCase("level_up_hp_knight"))
						level_up_hp_knight = Double.valueOf(value);
					else if (key.equalsIgnoreCase("level_up_hp_elf"))
						level_up_hp_elf = Double.valueOf(value);
					else if (key.equalsIgnoreCase("level_up_hp_wizard"))
						level_up_hp_wizard = Double.valueOf(value);
					else if (key.equalsIgnoreCase("level_up_mp_royal"))
						level_up_mp_royal = Double.valueOf(value);
					else if (key.equalsIgnoreCase("level_up_mp_knight"))
						level_up_mp_knight = Double.valueOf(value);
					else if (key.equalsIgnoreCase("level_up_mp_elf"))
						level_up_mp_elf = Double.valueOf(value);
					else if (key.equalsIgnoreCase("level_up_mp_wizard"))
						level_up_mp_wizard = Double.valueOf(value);
					else if (key.equalsIgnoreCase("magicDoll_black_elder_persent"))
						magicDoll_black_elder_persent = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("magicDoll_black_elder_min_damage"))
						magicDoll_black_elder_min_damage = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("magicDoll_black_elder_max_damage"))
						magicDoll_black_elder_max_damage = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("magicDoll_death_knight_persent"))
						magicDoll_death_knight_persent = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("magicDoll_death_knight_min_damage"))
						magicDoll_death_knight_min_damage = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("magicDoll_death_knight_max_damage"))
						magicDoll_death_knight_max_damage = Integer.valueOf(value);
					
					else if (key.equalsIgnoreCase("magicDoll_girtas_persent"))
						magicDoll_girtas_persent = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("magicDoll_girtas_min_damage"))
						magicDoll_girtas_min_damage = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("magicDoll_girtas_max_damage"))
						magicDoll_girtas_max_damage = Integer.valueOf(value);
					
					else if (key.equalsIgnoreCase("magicDoll_class_1_probability"))
						magicDoll_class_1_probability = Double.valueOf(value) * 0.01;
					else if (key.equalsIgnoreCase("magicDoll_class_1_perfect_probability"))
						magicDoll_class_1_perfect_probability = Double.valueOf(value) * 0.01;
					else if (key.equalsIgnoreCase("magicDoll_class_2_probability"))
						magicDoll_class_2_probability = Double.valueOf(value) * 0.01;
					else if (key.equalsIgnoreCase("magicDoll_class_2_perfect_probability"))
						magicDoll_class_2_perfect_probability = Double.valueOf(value) * 0.01;
					else if (key.equalsIgnoreCase("magicDoll_class_3_probability"))
						magicDoll_class_3_probability = Double.valueOf(value) * 0.01;
					else if (key.equalsIgnoreCase("magicDoll_class_3_perfect_probability"))
						magicDoll_class_3_perfect_probability = Double.valueOf(value) * 0.01;
					else if (key.equalsIgnoreCase("magicDoll_class_4_probability"))
						magicDoll_class_4_probability = Double.valueOf(value) * 0.01;
					else if (key.equalsIgnoreCase("magicDoll_class_5_probability"))
						magicDoll_class_5_probability = Double.valueOf(value) * 0.01;
					
					else if (key.equalsIgnoreCase("royal_damage_figure"))
						royal_damage_figure = Double.valueOf(value);
					else if (key.equalsIgnoreCase("knight_damage_figure"))
						knight_damage_figure = Double.valueOf(value);
					else if (key.equalsIgnoreCase("elf_damage_figure"))
						elf_damage_figure = Double.valueOf(value);
					else if (key.equalsIgnoreCase("wizard_damage_figure"))
						wizard_damage_figure = Double.valueOf(value);	
					else if (key.equalsIgnoreCase("royal_hit_figure"))
						royal_hit_figure = Double.valueOf(value);
					else if (key.equalsIgnoreCase("knight_hit_figure"))
						knight_hit_figure = Double.valueOf(value);
					else if (key.equalsIgnoreCase("elf_hit_figure"))
						elf_hit_figure = Double.valueOf(value);
					else if (key.equalsIgnoreCase("wizard_hit_figure"))
						wizard_hit_figure = Double.valueOf(value);
					else if (key.equalsIgnoreCase("royal_bow_damage_figure"))
						royal_bow_damage_figure = Double.valueOf(value);
					else if (key.equalsIgnoreCase("knight_bow_damage_figure"))
						knight_bow_damage_figure = Double.valueOf(value);
					else if (key.equalsIgnoreCase("elf_bow_damage_figure"))
						elf_bow_damage_figure = Double.valueOf(value);
					else if (key.equalsIgnoreCase("wizard_bow_damage_figure"))
						wizard_bow_damage_figure = Double.valueOf(value);	
					else if (key.equalsIgnoreCase("royal_bow_hit_figure"))
						royal_bow_hit_figure = Double.valueOf(value);
					else if (key.equalsIgnoreCase("knight_bow_hit_figure"))
						knight_bow_hit_figure = Double.valueOf(value);
					else if (key.equalsIgnoreCase("elf_bow_hit_figure"))
						elf_bow_hit_figure = Double.valueOf(value);
					else if (key.equalsIgnoreCase("wizard_bow_hit_figure"))
						wizard_bow_hit_figure = Double.valueOf(value);
					
					else if (key.equalsIgnoreCase("weapon_critical_persent"))
						weapon_critical_persent = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("is_critical"))
						is_critical = value.equalsIgnoreCase("true");	
					else if (key.equalsIgnoreCase("critical_Min_Dmg"))
						critical_Min_Dmg = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("critical_Max_Dmg"))
						critical_Max_Dmg = Integer.valueOf(value);
					
					else if (key.equalsIgnoreCase("spot_tower_hp"))
						spot_tower_hp = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("spot_tower_x"))
						spot_tower_x = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("spot_tower_y"))
						spot_tower_y = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("spot_tower_map"))
						spot_tower_map = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("spot_tower_start_hour"))
						spot_tower_start_hour = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("spot_tower_start_min"))
						spot_tower_start_min = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("spot_tower_time"))
						spot_tower_time = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("spot_item"))
						toFirstInventory(spot_item, value);
					else if (key.equalsIgnoreCase("spot_item_delay"))
						spot_item_delay = Integer.valueOf(value);
					
					else if (key.equalsIgnoreCase("lastday"))
						lastday = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("checkment"))
						checkment = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc"))
						dayc = Integer.valueOf(value);
					
					else if (key.equalsIgnoreCase("dayc0"))
						dayc0 = value;
					else if (key.equalsIgnoreCase("daycc0"))
						daycc0 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc1"))
						dayc1 = value;
					else if (key.equalsIgnoreCase("daycc1"))
						daycc1 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc2"))
						dayc2 = value;
					else if (key.equalsIgnoreCase("daycc2"))
						daycc2 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc3"))
						dayc3 = value;
					else if (key.equalsIgnoreCase("daycc3"))
						daycc3 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc4"))
						dayc4 = value;
					else if (key.equalsIgnoreCase("daycc4"))
						daycc4 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc5"))
						dayc5 = value;
					else if (key.equalsIgnoreCase("daycc5"))
						daycc5 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc6"))
						dayc6 = value;
					else if (key.equalsIgnoreCase("daycc6"))
						daycc6 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc7"))
						dayc7 = value;
					else if (key.equalsIgnoreCase("daycc7"))
						daycc7 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc8"))
						dayc8 = value;
					else if (key.equalsIgnoreCase("daycc8"))
						daycc8 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc9"))
						dayc9 = value;
					else if (key.equalsIgnoreCase("daycc9"))
						daycc9 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc10"))
						dayc10 = value;
					else if (key.equalsIgnoreCase("daycc10"))
						daycc10 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc11"))
						dayc11 = value;
					else if (key.equalsIgnoreCase("daycc11"))
						daycc11 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc12"))
						dayc12 = value;
					else if (key.equalsIgnoreCase("daycc12"))
						daycc12 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc13"))
						dayc13 = value;
					else if (key.equalsIgnoreCase("daycc13"))
						daycc13 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc14"))
						dayc14 = value;
					else if (key.equalsIgnoreCase("daycc14"))
						daycc14 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc15"))
						dayc15 = value;
					else if (key.equalsIgnoreCase("daycc15"))
						daycc15 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc16"))
						dayc16 = value;
					else if (key.equalsIgnoreCase("daycc16"))
						daycc16 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc17"))
						dayc17 = value;
					else if (key.equalsIgnoreCase("daycc17"))
						daycc17 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc18"))
						dayc18 = value;
					else if (key.equalsIgnoreCase("daycc18"))
						daycc18 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc19"))
						dayc19 = value;
					else if (key.equalsIgnoreCase("daycc19"))
						daycc19 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc20"))
						dayc20 = value;
					else if (key.equalsIgnoreCase("daycc20"))
						daycc20 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc21"))
						dayc21 = value;
					else if (key.equalsIgnoreCase("daycc21"))
						daycc21 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc22"))
						dayc22 = value;
					else if (key.equalsIgnoreCase("daycc22"))
						daycc22 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc23"))
						dayc23 = value;
					else if (key.equalsIgnoreCase("daycc23"))
						daycc23 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc24"))
						dayc24 = value;
					else if (key.equalsIgnoreCase("daycc24"))
						daycc24 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc25"))
						dayc25 = value;
					else if (key.equalsIgnoreCase("daycc25"))
						daycc25 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc26"))
						dayc26 = value;
					else if (key.equalsIgnoreCase("daycc26"))
						daycc26 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc27"))
						dayc27 = value;
					else if (key.equalsIgnoreCase("daycc27"))
						daycc27 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc28"))
						dayc28 = value;
					else if (key.equalsIgnoreCase("daycc28"))
						daycc28 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc29"))
						dayc29 = value;
					else if (key.equalsIgnoreCase("daycc29"))
						daycc29 = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("dayc30"))
						dayc30 = value;
					else if (key.equalsIgnoreCase("daycc30"))
						daycc30 = Integer.valueOf(value);
					
				}
			}
			lnrr.close();
		} catch (Exception e) {
			lineage.share.System.printf("%s : init()\r\n", Lineage.class.toString());
			lineage.share.System.println(e);
			e.printStackTrace();
		}

		TimeLine.end();
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
}

