package lineage.world.controller;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import lineage.bean.database.Drop;
import lineage.bean.database.Item;
import lineage.bean.database.Monster;
import lineage.database.ItemDatabase;
import lineage.database.MonsterDatabase;
import lineage.share.Lineage;
import lineage.share.TimeLine;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class WebSearchMonsterController {

	private static Object sync;
	
	public static void init() {
		TimeLine.start("WebSearchMonsterController..");
		
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
			sb.append("var monsters=").append( obj.toJSONString() ).append(";");
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
		for(Monster mon : MonsterDatabase.getList()) {
			//
			if (WebSearchItemController.isInitialSound(search_b.charAt(0)) && WebSearchItemController.isHangul(mon.getName().charAt(0))) {
				if (WebSearchItemController.getInitialSound(mon.getName().charAt(0)) == search_b.charAt(0)) {
					//
					JSONObject w = new JSONObject();
					w.put("name", mon.getName());
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
		// 몬스터 정보
		Monster mon = MonsterDatabase.find(data);
		w.put("name", mon.getName());
		w.put("gfx", mon.getGfx());
		w.put("level", mon.getLevel());
		w.put("ac", mon.getAc());
		w.put("hp", mon.getHp());
		w.put("mp", mon.getMp());
		w.put("lawful", Lineage.NEUTRAL-mon.getLawful());
		w.put("mr", mon.getMr());
		w.put("size", mon.getSize().equalsIgnoreCase("small") ? "작은 몬스터" : "큰 몬스터");
		// 드랍 아이템 목록.
		JSONArray d = new JSONArray();
		for(Drop drop : mon.getDropList()) {
			Item item = ItemDatabase.find(drop.getItemName());
			if(item == null)
				continue;
			JSONObject m = new JSONObject();
			m.put("name", drop.getItemName());
			m.put("invGfx", item.getInvGfx());
			m.put("groundGfx", item.getGroundGfx());
			d.add(m);
		}
		//
		obj.put("monster", w);
		obj.put("drop", d);
	}

}
