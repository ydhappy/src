package lineage.network;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lineage.bean.event.Event;
import lineage.network.netty.chatting.CodecFactory;
import lineage.share.Lineage;
import lineage.share.Socket;
import lineage.share.TimeLine;
import lineage.share.Web;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class ChattingServer {

	private static ServerBootstrap sb;
	// 접속한 클라이언트 목록.
	private static Map<Channel, ChattingClient> list;
	// 사용후 버려진 클라이언트 객체들 재사용을위해 사용.
	private static List<ChattingClient> pool;
	//
	private static List<Event> pool_pakcet;
	
	static public void init() throws Exception {
		TimeLine.start("ChattingServer..");
		
		if(Web.chatting) {
			//
			list = new HashMap<Channel, ChattingClient>();
			pool = new ArrayList<ChattingClient>();
			pool_pakcet = new ArrayList<Event>();
			//
			ExecutorService es = Executors.newCachedThreadPool();
			sb = new ServerBootstrap( new NioServerSocketChannelFactory(es, es) );
			sb.setPipelineFactory( new CodecFactory() );
			sb.setOption("child.tcpNoDelay", true);
			sb.setOption("child.receiveBufferSize", Socket.packet_recv_max);
			sb.setOption("connectTimeoutMillis", 1000);
			sb.bind(new InetSocketAddress(Web.chatting_server_port));
		}
		
		TimeLine.end();
	}
	
	static public void close() throws Exception {
		TimeLine.start("ChattingServer Close...");
		
		if(Web.chatting) {
			sb = null;
		}
		
		TimeLine.end();
	}
	
	/**
	 * 클라이언트 접속처리.
	 * @param c
	 */
	public static void connect(Channel socket) {
		
	}
	
	/**
	 * 클라이언트 종료처리.
	 * @param c
	 */
	public static void close(ChattingClient c) {
		
	}

	private static ChattingClient getPool() {
		synchronized (pool) {
			ChattingClient c = null;
			if(Lineage.memory_recycle && pool.size()>0){
				c = pool.get(0);
				pool.remove(0);
			}else{
				c = new ChattingClient();
			}
			return c;
		}
	}
	
	private static void setPool(ChattingClient c) {
		if(!Lineage.memory_recycle)
			return;
		synchronized (pool) {
			if(pool.contains(c) == false)
				pool.add(c);
		}
	}
	
	private static void append(ChattingClient c) {
		synchronized (list) {
			list.put(c.getSocket(), c);
		}
	}
	
	private static ChattingClient remove(ChattingClient c) {
		synchronized (list) {
			return list.remove(c.getSocket());
		}
	}
	
	private static List<ChattingClient> getList() {
		synchronized (list) {
			return new ArrayList<ChattingClient>(list.values());
		}
	}
	
	public static Event getPoolPacket(Class<?> c) {
		synchronized (pool_pakcet) {
			Event e = null;
			for(Event temp : pool_pakcet){
				if(temp.getClass().toString().equals(c.toString())) {
					e = temp;
					break;
				}
			}
			if(e != null)
				pool_pakcet.remove(e);
			return e;
		}
	}
	
	public static void setPoolPacket(List<Event> list) {
		synchronized (pool_pakcet) {
			pool_pakcet.addAll(list);
		}
	}
		
}
