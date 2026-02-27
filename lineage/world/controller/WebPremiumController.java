package lineage.world.controller;

import java.util.List;
import java.util.Map;

import lineage.database.AccountDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_AccountTime;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.World;
import lineage.world.object.instance.PcInstance;

import org.json.simple.JSONObject;

public class WebPremiumController {

	private static Object sync;
	
	public static void init() {
		TimeLine.start("WebPremiumController..");
		
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
				String id = params.get("id").get(0);
				String pw = params.get("pw").get(0);
				String key = params.get("key").get(0);

				// 계정 확인.
				int account_uid = AccountDatabase.getUid(id);
				if(AccountDatabase.isAccount(account_uid, id, pw)) {
					//
					PcInstance pc = World.findPcAccountUid(account_uid);
					int append_time = 0;
					//
					if(key.equalsIgnoreCase("10800"))
						// 3시간
						append_time = AccountDatabase.getTime(account_uid) + Integer.valueOf(key);
					else if(key.equalsIgnoreCase("18000"))
						// 5시간
						append_time = AccountDatabase.getTime(account_uid) + Integer.valueOf(key);
					else if(key.equalsIgnoreCase("36000"))
						// 10시간
						append_time = AccountDatabase.getTime(account_uid) + Integer.valueOf(key);
					else if(key.equalsIgnoreCase("108000"))
						// 30시간
						append_time = AccountDatabase.getTime(account_uid) + Integer.valueOf(key);
					//
					if(append_time > 0) {
						//
						if(pc != null) {
							pc.getClient().setAccountTime( append_time );
							pc.toSender( S_AccountTime.clone(BasePacketPooling.getPool(S_AccountTime.class), pc.getClient().getAccountTime()) );
							ChattingController.toChatting(pc, "계정연장이 완료 되었습니다.", 20);
						}
						//
						AccountDatabase.updateTime(account_uid, append_time);
					} else {
						obj.clear();
						obj.put("action", "error");
						obj.put("message", "정상적인 접근이 아닙니다.");
					}
				}  else {
					obj.put("action", "error");
					obj.put("message", "계정 정보가 잘못되었거나 존재하지 않습니다.");
				}
			} catch (Exception e) {
				obj.clear();
				obj.put("action", "error");
				obj.put("message", "정상적인 접근이 아닙니다.");
			}
			//
			StringBuffer sb = new StringBuffer();
			sb.append("var premium=").append( obj.toJSONString() ).append(";");
			obj.clear();
			obj = null;
			return sb.toString();
		}
	}

}
