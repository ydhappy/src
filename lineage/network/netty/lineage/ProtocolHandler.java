package lineage.network.netty.lineage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import lineage.Main;
import lineage.bean.database.Ip;
import lineage.database.AccountDatabase;
import lineage.database.BadIpDatabase;
import lineage.network.LineageClient;
import lineage.network.LineageServer;
import lineage.share.Lineage;
import lineage.share.Socket;

public class ProtocolHandler extends SimpleChannelUpstreamHandler {

	//
	static private Stack<List<byte[]>> pool = new Stack<List<byte[]>>();
	// 무한접속형 섭폭 필터링 목록.
	static private Map<String, Ip> list_dos = new HashMap<String, Ip>();
	// 최근 마지막에 접속이 시도됫던 시간 임시 저장.
	// : 1초내에 10개 이상에 접속이 시도된다면 10초정도 접속을 막음.
	static private Long last_connect_time = 0L;
	static private Integer last_connect_warning = 0;
	static private Boolean isRollback = false;

	// 접속기 인증형 패킷을 위한 목록.
	public static List<String> list_ip = new ArrayList<String>();

	static public void setPool(List<byte[]> list) {
		list.clear();
		if (!Lineage.memory_recycle)
			return;
		synchronized (pool) {
			pool.push(list);
		}
	}

	static public List<byte[]> getPool() {
		if (!Lineage.memory_recycle)
			return new ArrayList<byte[]>();
		synchronized (pool) {
			if (pool.size() > 0)
				return pool.pop();
			else
				return new ArrayList<byte[]>();
		}
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		if (Lineage.running == false) {
			e.getChannel().close();
			return;
		}
		// 서버가 종료되는 상황에서는 클라 요청을 차단.
		if (Main.running == false) {
			e.getChannel().close();
			return;
		}
		// 접속방식 체크. 잘못된 접근이면 클라 요청 차단.
		if (isBadClient(e.getChannel())) {
			e.getChannel().close();
			return;
		}
//		// 클라이언트 최대접속수용 확인.
//		if(LineageServer.getClientSize() >= GuiMain.CLIENT_MAX){
//			e.getChannel().close();
//			return;
//		}
	}

	/**
	 * 클라 접속처리 함수.
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		// 접속 허용된 클라만 처리.
		if (e.getChannel().isConnected())
			LineageServer.connect(e.getChannel());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent me) {
		//
		if (me.getMessage() == null)
			return;
		//
		Object o = me.getChannel().getAttachment();
		if (o == null)
			return;
		//
		if (me.getMessage() instanceof List) {
			LineageClient c = (LineageClient) o;
			List<byte[]> list = (List<byte[]>) me.getMessage();
			for (byte[] data : list)
				try {
//					System.out.println(data);
					if (Socket.CHECK_CLIENT) {
						if (data.length == 66 && data[0] == 0 && data[1] == 0 && data[2] == 91) {
							boolean login = true;
							for (int i = 0; i < data.length; i++) {
								if (data[i] != clientByte[i]) {
									login = false;
									break;
								}
							}
							if (login) {
								list_ip.add(c.getAccountIp());
							}
						}
					}
					c.toPacket(data);
					if (Socket.CHECK_CLIENT) {
						if ((int) (data[0] & 0xff) == 15 && list_ip.contains(c.getAccountIp())) {
							list_ip.remove(c.getAccountIp());
						}
					}
				} catch (Exception e) {
				}
			setPool(list);
		}
	}

	/**
	 * 클라 종료처리 함수.
	 */
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		Object o = e.getChannel().getAttachment();
		if (o == null)
			return;

		LineageServer.close((LineageClient) o);
//		System.out.println("channelClosed : "+e);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		Object o = e.getChannel().getAttachment();
		if (o == null)
			return;

		LineageServer.close((LineageClient) o);
//		System.out.println("exceptionCaught : "+e);
	}

	/**
	 * 
	 * @param socket
	 * @return
	 */
	static protected Boolean isBadClient(Channel socket) {
		String[] address = socket.getRemoteAddress().toString().substring(1).split(":");
		String ip = address[0];
		Integer port = Integer.valueOf(address[1]);
		Long time = System.currentTimeMillis();
		Long ocha = last_connect_time == 0 ? 0 : time - last_connect_time;

		// 접속시도 시간 체크.
		// 계정생성을 한 아이피는 접속시도 시간 체크 안하기.
		// 마지막에 접근햇던 시간 확인.
		// 재접근 시간이 0.1~0.3초 내로 온다면 차단.
		if (ocha != 0 && ocha <= 300 && !AccountDatabase.isAccountIp(ip))
			return true;
//			if(Socket.connect_rollback) {
//				if(time > last_connect_time+Socket.connect_rollback_stay_time) {
//					Socket.connect_rollback = false;
//				} else {
//					return true;
//				}
//			}
//			if(last_connect_time == 0)
//				last_connect_time = time;
//			if(time < last_connect_time+Socket.connect_rollback_time) {
//				last_connect_warning += 1;
//				if(last_connect_warning >= Socket.connect_rollback_cnt) {
//					Socket.connect_rollback = true;
//					last_connect_time = time;
//					return true;
//				}
//			} else {
//				last_connect_warning = 0;
//				last_connect_time = time;
//			}
//		}
		//
		last_connect_time = time;
		// 배드 아이피들 차단.
		if (BadIpDatabase.find(ip) != null) {
			lineage.share.System.print("제한된 아이피에서 접속시도 : " + ip + "\r\n");
			return true;
		}
		// ddos 공격 방어
		if (port <= 0)
			return true;
		// 무한접속형 섭폭 필터링 (dos공격)
		Ip IP = null;
		synchronized (list_dos) {
			IP = list_dos.get(ip);
			if (IP == null) {
				IP = new Ip();
				IP.setCount(0);
				IP.setBlock(false);
				IP.setIp(ip);
				IP.setTime(time);
				list_dos.put(IP.getIp(), IP);
				return false;
			}
		}
		// 블럭된 놈은 무시.
		if (IP.getBlock())
			return true;
		// 1초내에 3번이상 접속하는 놈들 무시.
		if (time < IP.getTime() + 1000) {
			if (IP.getCount() > 2) {
				IP.setBlock(true);
				BadIpDatabase.append(IP.getIp());
				return true;
			} else {
				IP.setCount(IP.getCount() + 1);
			}
		} else {
			IP.setCount(0);
			IP.setTime(time);
		}
		//
		return false;
	}

	public byte[] clientByte = new byte[] { 0, 0, 91, 8, 54, 23, 4, 19, 23, 82, 108, 35, 8, 14, 7, 51, 125, 8, 127, 62,
			7, 0, 11, 23, 69, 30, 9, 8, 123, 54, 19, 19, 11, 71, 9, 8, 100, 35, 12, 8, 14, 28, 80, 9, 8, 107, 47, 5, 12,
			11, 26, 93, 9, 8, 125, 6, 22, 23, 123, 9, 98, 59, 38, 21, 57, 29 };
}
