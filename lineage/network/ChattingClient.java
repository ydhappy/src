package lineage.network;

import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.share.Lineage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

public class ChattingClient {
	
	// 클라가 요청한 패킷.
	private ChannelBuffer buffer;
	// 패킷처리에 사용되는 소켓객체
	protected Channel socket;
	// 핑 타임 기록용.
	private long ping_time;
	
	public ChattingClient() {
		buffer = ChannelBuffers.dynamicBuffer();
	}

	public void update(Channel socket) {
		this.socket = socket;
		ping_time = System.currentTimeMillis();
	}
	
	public void close() {
		if(socket == null)
			return;
		//
		Channel temp = null;
		synchronized (socket) {
			temp = socket;
			socket = null;
		}
		if(temp.isConnected())
			temp.close();
	}
	
	/**
	 * 패킷 전송처리 요청 함수.
	 * @param o
	 */
	public void toSender(Object o){
		if(socket != null) {
			synchronized (socket) {
				if(socket!=null && socket.isConnected()) {
					socket.write(o);
					return;
				}
			}
		}
		
		if(o instanceof BasePacket)
			BasePacketPooling.setPool((BasePacket)o);
	}
	
	/**
	 * 클라이언트 객체를 지워도 되는지 확인해주는 함수.
	 *  : ping이 4분이상 오바됫을경우 true 리턴.
	 * @return
	 */
	public boolean isDelete(long time) {
		// 핑 오바되엇는지 확인.
		boolean is_ping = time-ping_time >= 1000*Lineage.client_ping_time;
		//
		return is_ping;
	}
	
	private void setPing() {
		ping_time = System.currentTimeMillis();
	}
	
	public ChannelBuffer getBuffer() {
		return buffer;
	}
	
	public Channel getSocket() {
		return socket;
	}
	
}
