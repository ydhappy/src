package lineage.share;

import java.io.BufferedReader;
import java.io.FileReader;

public final class Socket {

	static public int PORT = 2000;
	static public boolean PRINTPACKET = false;
	static public boolean connect_rollback = false;
	static public int connect_rollback_time;
	static public int connect_rollback_cnt;
	static public int connect_rollback_stay_time;
	static public boolean CHECK_CLIENT= false;
	
	// 클라이언트가 초당 서버로 전송할 수 있는 패킷 량.
	static public int packet_recv_max;
	
	static public void init(){
		TimeLine.start("Socket..");
		
		try {
			BufferedReader lnrr = new BufferedReader( new FileReader("socket.conf"));
			String line;
			while ( (line = lnrr.readLine()) != null){
				if(line.startsWith("#"))
					continue;
				
				int pos = line.indexOf("=");
				if(pos>0){
					String key = line.substring(0, pos).trim();
					String value = line.substring(pos+1, line.length()).trim();
					
					if(key.equalsIgnoreCase("Port"))
						PORT = Integer.valueOf(value);
					else if(key.equalsIgnoreCase("print_packet"))
						PRINTPACKET = value.equalsIgnoreCase("true");
					else if(key.equalsIgnoreCase("packet_send_max"))
						packet_recv_max = Integer.valueOf(value);
					else if(key.equalsIgnoreCase("connect_rollback_time"))
						connect_rollback_time = Integer.valueOf(value);
					else if(key.equalsIgnoreCase("connect_rollback_cnt"))
						connect_rollback_cnt = Integer.valueOf(value);
					else if(key.equalsIgnoreCase("connect_rollback_stay_time"))
						connect_rollback_stay_time = Integer.valueOf(value);
					else if (key.equalsIgnoreCase("check_client"))
						CHECK_CLIENT = value.equalsIgnoreCase("true");
					
				}
			}
			lnrr.close();
		} catch (Exception e) {
			lineage.share.System.printf("%s : init()\r\n", Socket.class.toString());
			lineage.share.System.println(e);
		}
		
		TimeLine.end();
	}
	
}
