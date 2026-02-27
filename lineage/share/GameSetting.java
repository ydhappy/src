package lineage.share;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class GameSetting {
	private static final Logger _log = Logger.getLogger(GameSetting.class.getName());

	/** Configuration files */
	public static final String GAMESETTING_CONFIG_FILE = "./GameSetting.properties";
	
	// -- Thread pools size 
	public static int THREAD_P_EFFECTS;
	public static int THREAD_P_GENERAL;
	public static int AI_MAX_THREAD;
	public static int THREAD_P_TYPE_GENERAL;
	public static int THREAD_P_SIZE_GENERAL;
	public static int LOG_TIMER;
	public static short MAX_ONLINE_USERS;
	
	
		
	public static int 제작막대인챈제한;
	public static int 제작막대최대인챈제한;
	public static int 제작막대축아이템지급최소확률;
	public static int 제작막대축아이템지급최대확률;
	public static int 제작막대필요갯수;
	public static boolean 제작막대무기사용가능여부;
	
	public static boolean 유저피바 = false;
	public static boolean 몬스터피바 = false;
	
	public static boolean CLAN_MEMBER_JOIN;
	public static int CLAN_MAX_JOIN_MEMBER;
	
	// 접속기 인증을 사용할 것인가..
	public static boolean 접속기인증;
	
	public static int NEWCLAN_MAX_JOIN_MEMBER;
	
	// -- 성혈혜택버프
		public static boolean CASTLE_BUFF_ONOFF;
		public static int CASTLE_BUFF_AC;
		public static int CASTLE_BUFF_DMG;
		public static int CASTLE_BUFF_HIT;
		public static int CASTLE_BUFF_SP;
		public static int CASTLE_BUFF_REDUC;
		public static int CASTLE_BUFF_EXP;	
		
		// -- 무혈혜택버프
		public static boolean BUFF_ONOFF;
		public static int BUFF_AC;
		public static int BUFF_DMG;
		public static int BUFF_HIT;
		public static int BUFF_SP;
		public static int BUFF_REDUC;
		public static int BUFF_EXP;	
	
	public static int 경험치지급단레벨;
	
	public static int LV50_EXP;
	public static int LV51_EXP;
	public static int LV52_EXP;
	public static int LV53_EXP;
	public static int LV54_EXP;
	public static int LV55_EXP;
	public static int LV56_EXP;
	public static int LV57_EXP;
	public static int LV58_EXP;
	public static int LV59_EXP;
	public static int LV60_EXP;
	public static int LV61_EXP;
	public static int LV62_EXP;
	public static int LV63_EXP;
	public static int LV64_EXP;
	public static int LV65_EXP;
	public static int LV66_EXP;
	public static int LV67_EXP;
	public static int LV68_EXP;
	public static int LV69_EXP;
	public static int LV70_EXP;
	public static int LV71_EXP;
	public static int LV72_EXP;
	public static int LV73_EXP;
	public static int LV74_EXP;
	public static int LV75_EXP;
	public static int LV76_EXP;
	public static int LV77_EXP;
	public static int LV78_EXP;
	public static int LV79_EXP;
	public static int LV80_EXP;
	public static int LV81_EXP;
	public static int LV82_EXP;
	public static int LV83_EXP;
	public static int LV84_EXP;
	public static int LV85_EXP;
	public static int LV86_EXP;
	public static int LV87_EXP;
	public static int LV88_EXP;
	public static int LV89_EXP;
	public static int LV90_EXP;
	public static int LV91_EXP;
	public static int LV92_EXP;
	public static int LV93_EXP;
	public static int LV94_EXP;
	public static int LV95_EXP;
	public static int LV96_EXP;
	public static int LV97_EXP;
	public static int LV98_EXP;
	public static int LV99_EXP;
	
	public static void load() {
		_log.info("loading gameserver config");
		try {
			Properties GameSetting = new Properties();
			FileReader is = new FileReader(new File(GAMESETTING_CONFIG_FILE));
			GameSetting.load(is);
			is.close();
			
			THREAD_P_TYPE_GENERAL = Integer.parseInt(GameSetting.getProperty("GeneralThreadPoolType", "0"), 10);
			THREAD_P_SIZE_GENERAL = Integer.parseInt(GameSetting.getProperty("GeneralThreadPoolSize", "0"), 10);
			MAX_ONLINE_USERS = Short.parseShort(GameSetting.getProperty("MaximumOnlineUsers", "30"));

			제작막대인챈제한 = Integer.parseInt(GameSetting.getProperty("CreateWandEnchant", "7"));
			제작막대최대인챈제한 = Integer.parseInt(GameSetting.getProperty("CreateWandMaxEnchant", "7"));
			제작막대축아이템지급최소확률 = Integer.parseInt(GameSetting.getProperty("CreateWandBlessRateMin", "5"));
			제작막대축아이템지급최대확률 = Integer.parseInt(GameSetting.getProperty("CreateWandBlessRateMax", "10"));
			제작막대필요갯수 = Integer.parseInt(GameSetting.getProperty("CreateWandCount", "3"));
			제작막대무기사용가능여부 = Boolean.parseBoolean(GameSetting.getProperty("CreateWeaponUse", "false"));
			
			경험치지급단레벨 = Integer.parseInt(GameSetting.getProperty("ExpGiveNpcLevel", "60"));
			
			CASTLE_BUFF_ONOFF = Boolean.parseBoolean(GameSetting.getProperty("CastleBuffOnOff", "true"));
			CASTLE_BUFF_AC = Integer.parseInt(GameSetting.getProperty("CastleBuffAc", "2"));
			CASTLE_BUFF_DMG = Integer.parseInt(GameSetting.getProperty("CastleBuffDmg", "2"));
			CASTLE_BUFF_HIT = Integer.parseInt(GameSetting.getProperty("CastleBuffHit", "2"));
			CASTLE_BUFF_SP = Integer.parseInt(GameSetting.getProperty("CastleBuffSp", "2"));
			CASTLE_BUFF_REDUC = Integer.parseInt(GameSetting.getProperty("CastleBuffReduc", "2"));
			CASTLE_BUFF_EXP = Integer.parseInt(GameSetting.getProperty("CastleBuffExp", "1000"));
			
			BUFF_ONOFF = Boolean.parseBoolean(GameSetting.getProperty("BuffOnOff", "true"));
			BUFF_AC = Integer.parseInt(GameSetting.getProperty("BuffAc", "2"));
			BUFF_DMG = Integer.parseInt(GameSetting.getProperty("BuffDmg", "2"));
			BUFF_HIT = Integer.parseInt(GameSetting.getProperty("BuffHit", "2"));
			BUFF_SP = Integer.parseInt(GameSetting.getProperty("BuffSp", "2"));
			BUFF_REDUC = Integer.parseInt(GameSetting.getProperty("BuffReduc", "2"));
			BUFF_EXP = Integer.parseInt(GameSetting.getProperty("BuffExp", "1000"));
			
			접속기인증 = Boolean.parseBoolean(GameSetting.getProperty("ConnecterON", "true"));
			
			CLAN_MEMBER_JOIN = Boolean.parseBoolean(GameSetting.getProperty("ClanMemberJoin", "true"));
			CLAN_MAX_JOIN_MEMBER = Integer.parseInt(GameSetting.getProperty("ClanMaxJoinMember", "100"));
			NEWCLAN_MAX_JOIN_MEMBER = Integer.parseInt(GameSetting.getProperty("NewClanMaxJoinMember", "100"));
			//궁금한게 잇는데요 퀘스트 부분인데요 
			LV50_EXP = Integer.parseInt(GameSetting.getProperty("Lv50Exp", "1"));
			LV51_EXP = Integer.parseInt(GameSetting.getProperty("Lv51Exp", "1"));
			LV52_EXP = Integer.parseInt(GameSetting.getProperty("Lv52Exp", "1"));
			LV53_EXP = Integer.parseInt(GameSetting.getProperty("Lv53Exp", "1"));
			LV54_EXP = Integer.parseInt(GameSetting.getProperty("Lv54Exp", "1"));
			LV55_EXP = Integer.parseInt(GameSetting.getProperty("Lv55Exp", "1"));
			LV56_EXP = Integer.parseInt(GameSetting.getProperty("Lv56Exp", "1"));
			LV57_EXP = Integer.parseInt(GameSetting.getProperty("Lv57Exp", "1"));
			LV58_EXP = Integer.parseInt(GameSetting.getProperty("Lv58Exp", "1"));
			LV59_EXP = Integer.parseInt(GameSetting.getProperty("Lv59Exp", "1"));
			LV60_EXP = Integer.parseInt(GameSetting.getProperty("Lv60Exp", "1"));
			LV61_EXP = Integer.parseInt(GameSetting.getProperty("Lv61Exp", "1"));
			LV62_EXP = Integer.parseInt(GameSetting.getProperty("Lv62Exp", "1"));
			LV63_EXP = Integer.parseInt(GameSetting.getProperty("Lv63Exp", "1"));
			LV64_EXP = Integer.parseInt(GameSetting.getProperty("Lv64Exp", "1"));
			LV65_EXP = Integer.parseInt(GameSetting.getProperty("Lv65Exp", "2"));
			LV66_EXP = Integer.parseInt(GameSetting.getProperty("Lv66Exp", "2"));
			LV67_EXP = Integer.parseInt(GameSetting.getProperty("Lv67Exp", "2"));
			LV68_EXP = Integer.parseInt(GameSetting.getProperty("Lv68Exp", "2"));
			LV69_EXP = Integer.parseInt(GameSetting.getProperty("Lv69Exp", "2"));
			LV70_EXP = Integer.parseInt(GameSetting.getProperty("Lv70Exp", "4"));
			LV71_EXP = Integer.parseInt(GameSetting.getProperty("Lv71Exp", "4"));
			LV72_EXP = Integer.parseInt(GameSetting.getProperty("Lv72Exp", "4"));
			LV73_EXP = Integer.parseInt(GameSetting.getProperty("Lv73Exp", "4"));
			LV74_EXP = Integer.parseInt(GameSetting.getProperty("Lv74Exp", "4"));
			LV75_EXP = Integer.parseInt(GameSetting.getProperty("Lv75Exp", "8"));
			LV76_EXP = Integer.parseInt(GameSetting.getProperty("Lv76Exp", "8"));
			LV77_EXP = Integer.parseInt(GameSetting.getProperty("Lv77Exp", "8"));
			LV78_EXP = Integer.parseInt(GameSetting.getProperty("Lv78Exp", "8"));
			LV79_EXP = Integer.parseInt(GameSetting.getProperty("Lv79Exp", "16"));
			LV80_EXP = Integer.parseInt(GameSetting.getProperty("Lv80Exp", "32"));
			LV81_EXP = Integer.parseInt(GameSetting.getProperty("Lv81Exp", "64"));
			LV82_EXP = Integer.parseInt(GameSetting.getProperty("Lv82Exp", "128"));
			LV83_EXP = Integer.parseInt(GameSetting.getProperty("Lv83Exp", "256"));
			LV84_EXP = Integer.parseInt(GameSetting.getProperty("Lv84Exp", "512"));
			LV85_EXP = Integer.parseInt(GameSetting.getProperty("Lv85Exp", "1024"));
			LV86_EXP = Integer.parseInt(GameSetting.getProperty("Lv86Exp", "2048"));
			LV87_EXP = Integer.parseInt(GameSetting.getProperty("Lv87Exp", "4096"));
			LV88_EXP = Integer.parseInt(GameSetting.getProperty("Lv88Exp", "8192"));
			LV89_EXP = Integer.parseInt(GameSetting.getProperty("Lv89Exp", "16384"));
			LV90_EXP = Integer.parseInt(GameSetting.getProperty("Lv90Exp", "32768"));
			LV91_EXP = Integer.parseInt(GameSetting.getProperty("Lv91Exp", "65536"));
			LV92_EXP = Integer.parseInt(GameSetting.getProperty("Lv92Exp", "131072"));
			LV93_EXP = Integer.parseInt(GameSetting.getProperty("Lv93Exp", "262144"));
			LV94_EXP = Integer.parseInt(GameSetting.getProperty("Lv94Exp", "524288"));
			LV95_EXP = Integer.parseInt(GameSetting.getProperty("Lv95Exp", "1048576"));
			LV96_EXP = Integer.parseInt(GameSetting.getProperty("Lv96Exp", "2097152"));
			LV97_EXP = Integer.parseInt(GameSetting.getProperty("Lv97Exp", "4194304"));
			LV98_EXP = Integer.parseInt(GameSetting.getProperty("Lv98Exp", "8388608"));
			LV99_EXP = Integer.parseInt(GameSetting.getProperty("Lv99Exp", "16777216"));

		} catch (Exception e) {		
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + GAMESETTING_CONFIG_FILE + " File.");
		}
	}
	
//	public static boolean setParameterValue(String pName, String pValue) {
//		
//		if (pName.equalsIgnoreCase("MonsterDamageOne")) {
//			MONSTER_DAMAGE = Double.parseDouble(pValue);
//		}  else {
//			return false;
//		}
//		return true;
//	}
		
	private GameSetting() {
		
	}
}