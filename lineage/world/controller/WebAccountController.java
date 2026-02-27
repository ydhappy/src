package lineage.world.controller;

import java.util.List;
import java.util.Map;

import lineage.database.AccountDatabase;
import lineage.share.TimeLine;

public class WebAccountController {

	private static Object sync;
	
	public static void init() {
		TimeLine.start("WebAccountController..");
		
		//
		sync = new Object();
		
		TimeLine.end();
	}
	
	public static String toJavaScript(Map<String, List<String>> params) {
		synchronized (sync) {
			try {
				String id = params.get("id").get(0);
				return String.valueOf( AccountDatabase.getUid(id) );
			} catch (Exception e) {
				return ">> error";
			}
		}
	}
	
}
