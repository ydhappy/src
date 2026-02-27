package lineage.world.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import lineage.bean.event.SaveDatabase;
import lineage.database.BackgroundDatabase;
import lineage.database.ExpDatabase;
import lineage.database.ItemDatabase;
import lineage.database.MonsterSpawnlistDatabase;
import lineage.database.NpcSpawnlistDatabase;
import lineage.database.ServerDatabase;
import lineage.network.LineageServer;
import lineage.network.packet.BasePacketPooling;
import lineage.share.Lineage;
import lineage.share.Log;
import lineage.share.Web;
import lineage.thread.AiThread;
import lineage.thread.EventThread;
import lineage.thread.TimerThread;
import lineage.util.Shutdown;
import lineage.util.Util;
import lineage.world.AStar;
import lineage.world.World;
import lineage.world.object.object;

import org.json.simple.JSONObject;

public class SpServerController {

	static private String CHARSET;
	
	static public void init() {
		CHARSET = "UTF-8";
	}
	
	static public void toSave() {
		new Thread( new SaveDatabase() ).start();
	}
	
	static public void toShutdown() {
		new Thread(Shutdown.getInstance()).start();
	}
	
	/**
	 * 전체채팅을 하게되면 이곳으로 오게됨.
	 * @param o
	 * @param msg
	 */
	static public void toChattingGlobal(final object o, final String msg) {
		// 
		if(o == null)
			return;
		// 
		long time = System.currentTimeMillis();
		String passwd = Util.toMD5( Web.web_server_passwd + time );
		try {
			for(String[] address : Web.sp_server_list)
				getHtml(
						String.format(
								"http://%s:%s/?time=%s&sp=%s&action=sp_server_chatting_global&server_version=%d&cha_name=%s&message=%s", 
								address[0], 
								address[1],
								String.valueOf(time), 
								passwd, 
								Lineage.server_version, 
								URLEncoder.encode(o.getName(), "UTF-8"), 
								URLEncoder.encode(msg, "UTF-8")
						), null, null
					);
		} catch (Exception e) { }
	}
	
	/**
	 * WebServer.java 통해 이곳으로 오게됨.
	 * 	: sp controller 프로그램을 통해 요청하게 됨.
	 *  : 현재 서버에 정보를 문자열로 정리하여 리턴.
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static public String toServerInfo(Map<String, List<String>> params) {
		JSONObject obj = new JSONObject();
		obj.put("server_version", 	Lineage.server_version);
		// Object Id
		obj.put("pc_objid", 		String.valueOf(ServerDatabase.getPc_objid()));
		obj.put("item_objid", 		String.valueOf(ServerDatabase.getItem_objid()));
		obj.put("inn_key", 			String.valueOf(ServerDatabase.getInn_objid()));
		obj.put("etc_objid", 		String.valueOf(ServerDatabase.getEtc_objid()));
		obj.put("npc_objid", 		String.valueOf(ServerDatabase.getNpc_objid()));
		// Thread
		obj.put("event_thread",		String.format("%d-%d-%d", EventThread.getListSize(), EventThread.getRunSize(), EventThread.getPoolSize()));
		obj.put("timer_thread",		String.format("%dms", TimerThread.getTimeLine()));
		obj.put("ai_thread",		String.format("%d-%d-%d", AiThread.getListSize(), AiThread.getRunSize(), AiThread.getPoolSize()));
		// World
		obj.put("world_time", 		String.format("%02d:%02d:%02d", ServerDatabase.getLineageTimeHour(), ServerDatabase.getLineageTimeMinute(), ServerDatabase.getLineageTimeSeconds()));
		obj.put("map_count", 		String.valueOf(World.getMapSize()));
		obj.put("player_count", 	String.valueOf(World.getPcSize()));
		obj.put("object_count", 	String.valueOf(World.getSize()));
		// pool
		obj.put("pool_useshop", 		String.valueOf(UserShopController.getPoolSize()));
		obj.put("pool_iteminstance", 	String.valueOf(ItemDatabase.getPoolSize()));
		obj.put("pool_object", 			String.valueOf(NpcSpawnlistDatabase.getPoolSize()));
		obj.put("pool_summon", 			String.valueOf(SummonController.getPoolSize()));
		obj.put("pool_clan", 			String.valueOf(ClanController.getPoolSize()));
		obj.put("pool_monsterinstance", String.valueOf(MonsterSpawnlistDatabase.getPoolSize()));
		obj.put("pool_innkey", 			String.valueOf(InnController.getPoolSize()));
		obj.put("pool_basepacket", 		String.valueOf(BasePacketPooling.getPoolSize()));
		obj.put("pool_trade", 			String.valueOf(TradeController.getPoolSize()));
		obj.put("pool_buff", 			String.valueOf(BuffController.getPoolBuffSize()));
		obj.put("pool_inventory", 		String.valueOf(InventoryController.getPoolSize()));
		obj.put("pool_buffinterface", 	String.valueOf(BuffController.getPoolSize()));
		obj.put("pool_party", 			String.valueOf(PartyController.getPoolSize()));
		obj.put("pool_exp", 			String.valueOf(ExpDatabase.getPoolSize()));
		obj.put("pool_summoninstance", 	String.valueOf(SummonController.getSummonPoolSize()));
		obj.put("pool_event", 			String.valueOf(EventThread.getPoolSize()));
		obj.put("pool_petinstance", 	String.valueOf(SummonController.getPetPoolSize()));
		obj.put("pool_client", 			String.valueOf(LineageServer.getPoolSize()));
		obj.put("pool_book", 			String.valueOf(BookController.getPoolSize()));
		obj.put("pool_board", 			String.valueOf(BoardController.getPoolSize()));
		obj.put("pool_backgroundinstance", 	String.valueOf(BackgroundDatabase.getPoolSize()));
		obj.put("pool_friend", 			String.valueOf(FriendController.getPoolSize()));
		obj.put("pool_log", 			String.valueOf(Log.getPoolSize()));
		obj.put("pool_event_illusion", 	String.valueOf(EventController.getIllusionItemSize()));
		obj.put("pool_astar_node", 		String.valueOf(AStar.getPoolSize()));
		// 
		obj.put("server_operating_time",	ServerDatabase.toOperatingTime());
		// 
		return obj.toJSONString();
	}
	
	/**
	 * WebServer.java 통해 이곳으로 오게됨.
	 * 	: 다른 sp팩에서 이쪽서버와 연결을 시도
	 *  : 전체채팅을 시도하여 이곳에 전달됨.
	 */
	static public String toChattingGlobalRequest(Map<String, List<String>> params) {
		try {
			String server_version = params.get("server_version").get(0);
			String cha_name = params.get("cha_name").get(0);
			String message = params.get("message").get(0);
			String msg = String.format("[%s:%s] %s", cha_name, server_version, message);
			//
			ChattingController.toChatting(null, msg, 100);
		} catch (Exception e) {
			System.out.println("toChattingGlobalRequest(Map<String, List<String>> params) : " + e);
		}
		return null;
	}
	
	static private String getHtml(String url, String parameter, String method) {
		StringBuilder sb = new StringBuilder();
		HttpURLConnection conn = null;
		BufferedReader br = null;
		try {
			//
			conn = toUrl(url, parameter, method);
			//
			String temp;
			if("gzip".equals(conn.getContentEncoding()))
				br = new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.getInputStream()), CHARSET));
			else
				br = new BufferedReader(new InputStreamReader(conn.getInputStream(), CHARSET));
			while ((temp = br.readLine()) != null)
				sb.append(temp).append("\r\n");
			
		} catch (Exception e) {
		} finally {
			try {
				if(br != null)
					br.close();
				if(conn != null)
					conn.disconnect();
			} catch (Exception e2) { }
		}

		return sb.toString();
	}

	static private HttpURLConnection toUrl(String url, String parameter, String method) throws Exception {
		//
		URL Url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)Url.openConnection();
		conn.setRequestMethod(method==null ? "GET" : method);
		conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
		conn.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setConnectTimeout(1000 * 1);
		conn.setReadTimeout(1000 * 1);
		conn.connect();
		// 파라미터 등록.
		if(parameter != null) {
			OutputStream os = conn.getOutputStream();
			os.write(parameter.getBytes());
			os.flush();
			os.close();
		}
		//
		return conn;
	}
}
