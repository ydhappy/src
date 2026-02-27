package lineage.world.controller;

import java.util.List;
import java.util.Map;

import lineage.database.ServerDatabase;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.World;

import org.json.simple.JSONObject;

public class WebInformationController {

	private static Object sync;
	
	public static void init() {
		TimeLine.start("WebInformationController..");
		
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
				if(type.equalsIgnoreCase("player_cnt")) {
					obj.put("count", String.valueOf(World.getPcSize()+World.getRobotSize()));
				} else {
					obj.put("run_time", ServerDatabase.toOperatingTime());
					obj.put("count", String.valueOf(World.getPcSize()+World.getRobotSize()));
					obj.put("server_version", String.valueOf(Lineage.server_version));
					obj.put("rate_exp", String.valueOf(Lineage.rate_exp));
					obj.put("rate_drop", String.valueOf(Lineage.rate_drop));
					obj.put("rate_aden", String.valueOf(Lineage.rate_aden));
					obj.put("rate_enchant", String.valueOf(Lineage.rate_enchant));
				}
			} catch (Exception e) {
				obj.clear();
				obj.put("action", "error");
				obj.put("message", "정상적인 접근이 아닙니다.");
			}
			//
			StringBuffer sb = new StringBuffer();
			sb.append("var info=").append( obj.toJSONString() ).append(";");
			obj.clear();
			obj = null;
			return sb.toString();
		}
	}
	
}
