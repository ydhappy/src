package lineage.world.controller;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import lineage.bean.database.Drop;
import lineage.bean.database.Item;
import lineage.bean.database.Monster;
import lineage.database.ItemDatabase;
import lineage.database.MonsterDatabase;
import lineage.share.TimeLine;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class WebSearchItemController {

	private static Object sync;
	
	private static final char HANGUL_BEGIN_UNICODE = 44032; // 가
	private static final char HANGUL_LAST_UNICODE = 55203; // 힣
	private static final char HANGUL_BASE_UNIT = 588;// 각자음 마다 가지는 글자수
	// 자음
	private static final char[] INITIAL_SOUND = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ','ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };
	
	public static void init() {
		TimeLine.start("WebSearchItemController..");
		
		//
		sync = new Object();
		
		TimeLine.end();
	}

	@SuppressWarnings("unchecked")
	public static String toJavaScript(Map<String, List<String>> params) {
		synchronized (sync) {
			JSONObject obj = new JSONObject();
			//
			try {
				String type = params.get("type").get(0);
				String data = params.get("data").get(0);
				if(type.length()==0)
					type = "list";
				//
				if(type.equalsIgnoreCase("list"))
					toList(obj, data);
				else if(type.equalsIgnoreCase("view"))
					toView(obj, data);
				
			} catch (Exception e) {
				obj.clear();
				obj.put("action", "error");
				obj.put("message", "정상적인 접근이 아닙니다.");
			}
			//
			StringBuffer sb = new StringBuffer();
			sb.append("var items=").append( obj.toJSONString() ).append(";");
			obj.clear();
			obj = null;
			return sb.toString();
		}
	}

	@SuppressWarnings("unchecked")
	private static void toList(JSONObject obj, String data) {
		String search_a = "all";
		String search_b = "ㄱ";
		JSONArray search = new JSONArray();
		//
		StringTokenizer st = new StringTokenizer(data, "|");
		search_a = st.nextToken();
		search_b = st.nextToken();
		//
		for(Item item : ItemDatabase.getList()) {
			//
			if(search_a.equalsIgnoreCase("weapon") && !item.getType1().equalsIgnoreCase("weapon"))
				continue;
			if(search_a.equalsIgnoreCase("armor") && (!item.getType1().equalsIgnoreCase("armor") || item.getType2().equalsIgnoreCase("belt") || item.getType2().equalsIgnoreCase("necklace") || item.getType2().equalsIgnoreCase("ring")))
				continue;
			if(search_a.equalsIgnoreCase("accessory") && (!item.getType1().equalsIgnoreCase("armor") || (!item.getType2().equalsIgnoreCase("belt") && !item.getType2().equalsIgnoreCase("necklace") && !item.getType2().equalsIgnoreCase("ring"))))
				continue;
			if(search_a.equalsIgnoreCase("etc") && !item.getType2().equalsIgnoreCase("etc"))
				continue;
			//
			if (isInitialSound(search_b.charAt(0)) && isHangul(item.getName().charAt(0))) {
				if (getInitialSound(item.getName().charAt(0)) == search_b.charAt(0)) {
					//
					JSONObject w = new JSONObject();
					w.put("name", item.getName());
					search.add(w);
				}
			}
		}
		//
		obj.put("search", search);
	}

	@SuppressWarnings("unchecked")
	private static void toView(JSONObject obj, String data) {
		//
		JSONObject w = new JSONObject();
		// 아이템 정보
		Item item = ItemDatabase.find(data);
		w.put("name", item.getName());
		w.put("type1", item.getType1());
		w.put("type2", item.getType2());
		w.put("material", item.getMaterialName());
		w.put("dmgMin", item.getDmgMin());
		w.put("dmgMax", item.getDmgMax());
		w.put("weight", item.getWeight());
		w.put("invGfx", item.getInvGfx());
		w.put("groundGfx", item.getGroundGfx());
		w.put("sell", item.isSell());
		w.put("piles", item.isPiles());
		w.put("trade", item.isTrade());
		w.put("drop", item.isDrop());
		w.put("warehouse", item.isWarehouse());
		w.put("enchant", item.isEnchant());
		w.put("safeEnchant", item.getSafeEnchant());
		w.put("royal", item.getRoyal());
		w.put("knight", item.getKnight());
		w.put("elf", item.getElf());
		w.put("wizard", item.getWizard());
		w.put("darkElf", item.getDarkElf());
		w.put("dragonKnight", item.getDragonKnight());
		w.put("blackWizard", item.getBlackWizard());
		w.put("addHit", item.getAddHit());
		w.put("addDmg", item.getAddDmg());
		w.put("ac", item.getAc());
		w.put("addStr", item.getAddStr());
		w.put("addDex", item.getAddDex());
		w.put("addCon", item.getAddCon());
		w.put("addInt", item.getAddInt());
		w.put("addWis", item.getAddWis());
		w.put("addCha", item.getAddCha());
		w.put("addHp", item.getAddHp());
		w.put("addMp", item.getAddMp());
		w.put("addSp", item.getAddSp());
		w.put("addMr", item.getAddMr());
		w.put("canbedmg", item.isCanbedmg());
		w.put("effect", item.getEffect());
		w.put("tohand", item.isTohand());
		w.put("setId", item.getSetId());
		w.put("continuous", item.getContinuous());
		w.put("waterress", item.getWaterress());
		w.put("windress", item.getWindress());
		w.put("earthress", item.getEarthress());
		w.put("fireress", item.getFireress());
		w.put("addWeight", item.getAddWeight());
		w.put("ticHp", item.getTicHp());
		w.put("ticMp", item.getTicMp());
		w.put("stealHp", item.getStealHp());
		w.put("stealMp", item.getStealMp());
		w.put("enchantMr", item.getEnchantMr());
		w.put("levelMin", item.getLevelMin());
		w.put("levelMax", item.getLevelMax());
		w.put("note", item.getNote());
		w.put("reduction", item.getReduction());
		// 드랍 몬스터 목록.
		JSONArray d = new JSONArray();
		for(Monster monster : MonsterDatabase.getList()) {
			for(Drop drop : monster.getDropList()) {
				if(drop.getItemName().equalsIgnoreCase(data)) {
					JSONObject m = new JSONObject();
					m.put("name", monster.getName());
					m.put("gfx", monster.getGfx());
					m.put("level", monster.getLevel());
					m.put("hp", monster.getHp());
					m.put("mp", monster.getMp());
					d.add(m);
				}
			}
		}
		//
		obj.put("item", w);
		obj.put("drop", d);
	}

	/**
	 * 해당 문자가 한글인지 검사
	 * 
	 * @param c 문자 하나
	 * @return
	 */
	public static boolean isHangul(char c) {
		return HANGUL_BEGIN_UNICODE <= c && c <= HANGUL_LAST_UNICODE;
	}

	/**
	 * 해당 문자가 INITIAL_SOUND인지 검사.
	 * 
	 * @param searchar
	 * @return
	 */
	public static boolean isInitialSound(char searchar) {
		for (char c : INITIAL_SOUND)
			if (c == searchar)
				return true;
		return false;
	}
	
	/**
	 * 해당 문자의 자음을 얻는다.
	 * 
	 * @param c 검사할 문자
	 * @return
	 */
	public static char getInitialSound(char c) {
		int hanBegin = (c - HANGUL_BEGIN_UNICODE);
		int index = hanBegin / HANGUL_BASE_UNIT;
		return INITIAL_SOUND[index];
	}

}
