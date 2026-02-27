package lineage.share;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public final class Web {
	
	// 웹채팅 사용 여부
	static public boolean chatting;
	// 웹채팅에 사용될 서버 포트
	static public int chatting_server_port;
	
	// 웹서버 사용 여부
	static public boolean web_server;
	// 웹서버 포트
	static public int web_server_port;
	// 웹서버 인증 패스워드
	static public String web_server_passwd;
	
	// sp 서버 연동 목록.
	static public List<String[]> sp_server_list;
	
	/**
	 * 필요한 정보 초기화 처리하는 함수.
	 */
	static public void init(){
		TimeLine.start("Web..");
		
		try {
			//
			sp_server_list = new ArrayList<String[]>();
			//
			BufferedReader lnrr = new BufferedReader( new FileReader("web.conf"));
			String line;
			while ( (line = lnrr.readLine()) != null){
				if(line.startsWith("#"))
					continue;
				
				int pos = line.indexOf("=");
				if(pos>0){
					String key = line.substring(0, pos).trim();
					String value = line.substring(pos+1, line.length()).trim();
					
					if(key.equalsIgnoreCase("chatting"))
						chatting = value.equalsIgnoreCase("true");
					else if(key.equalsIgnoreCase("chatting_server_port"))
						chatting_server_port = Integer.valueOf(value);
					else if(key.equalsIgnoreCase("web_server"))
						web_server = value.equalsIgnoreCase("true");
					else if(key.equalsIgnoreCase("web_server_port"))
						web_server_port = Integer.valueOf(value);
					else if(key.equalsIgnoreCase("web_server_passwd"))
						web_server_passwd = value;
					else if(key.equalsIgnoreCase("sp_server_list")) {
						StringTokenizer st = new StringTokenizer(value, ",");
						while(st.hasMoreTokens()) {
							String address = st.nextToken().trim();
							sp_server_list.add(address.split(":"));
						}
					}
				}
			}
			lnrr.close();
		} catch (Exception e) {
			lineage.share.System.printf("%s : init()\r\n", Web.class.toString());
			lineage.share.System.println(e);
		}
		
		TimeLine.end();
	}
	
}
