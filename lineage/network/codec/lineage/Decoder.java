package lineage.network.codec.lineage;

import java.util.List;

import lineage.network.LineageClient;
import lineage.network.netty.lineage.ProtocolHandler;
import lineage.share.Lineage;
import lineage.share.Socket;
import lineage.util.Util;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

public final class Decoder extends OneToOneDecoder {

	// 초당 받은 패킷양 기록용. 로그에 사용됨.
	static public int recv_length;
	//private static byte CHOLONG_CONNECTOR_KEY = 6;
	static {
		recv_length = 0;
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		//
		LineageClient c = (LineageClient)channel.getAttachment();
		if(c == null)
			return msg;
		//
		synchronized (c.getBuffer()) {
			if(msg instanceof ChannelBuffer) {
				ChannelBuffer buffer = (ChannelBuffer)msg;
				
				// 데이타 축적
				int size = buffer.readableBytes();
				byte[] data = new byte[size];
				buffer.readBytes(data);
				buffer = null;
				
				 //-!!-
			//	for(int i=0; i<data.length; i++){
			//		data[i] ^= CHOLONG_CONNECTOR_KEY;
			//		System.out.println(data[i]);
				//}
			
				c.getBuffer().writeBytes(data);
				// 로그 기록을위해 패킷량 갱신.
				recv_length += size;
				
				List<byte[]> list = ProtocolHandler.getPool();
				msg = list;
				// 처리.
				for(;;){
					
//					// 접속기에서 받은 패킷 처리
					if (fromConnector(c))
						return msg;
					
					// head 사이즈 추출
					int length = c.getBuffer().getByte(0) & 0xff;
					length |= c.getBuffer().getByte(1) << 8 & 0xff00;
					if(c.getBuffer().readableBytes() >= length) {
						c.getBuffer().readBytes(2);
						length -= 2;
						// 담을 메모리 생성.
						data = new byte[length];
						c.getBuffer().readBytes(data);
						// 읽은 부분까지 제거하고 읽는위치값 0으로 초기화.
						c.getBuffer().discardReadBytes();
						
						
						// -- 창고팅김 접속기
						//for(int i=0; i< length; i++){
						//data[i] ^= CHOLONG_CONNECTOR_KEY;
					//	}
						
						// 처리.
						if(Lineage.server_version > 360) {
							synchronized (c.getEncryption380()) {
								data = c.getEncryption380().decrypt_S(data);
							}
						} else if(Lineage.server_version>200) {
							synchronized (c.getEncryption()) {
								data = c.getEncryption().decrypt(data);
							}
						} else if(Lineage.server_version>138) {
							decrypt(c, data);
							c.setPacketSize( c.getPacketSize() + length );
						} else {
							decrypt( data );
						}
						// 로그 기록을위해 패킷량 갱신.
						c.setSendLength( c.getSendLength() +  length + 2);
						// 패킷 출력
						if(Socket.PRINTPACKET)
							lineage.share.System.printf("[client] opcode:%d, size:%d\r\n%s\r\n", data[0]&0xff, data.length, Util.printData(data, data.length) );
						//
						list.add( data );
					} else {
						break;
					}
				}
			}
		}
		return msg;
	}
	
	private void decrypt(byte[] data){
		int idx = data[0];
		int idx_temp = 0;
		for(int i=1 ; i<data.length ; ++i){
			idx_temp = data[i];
			data[i] = (byte)(data[i] ^ idx);
			idx = idx_temp;
		}
	}
	
	private void decrypt(LineageClient c, byte[] data) {
		byte[] header = {
				(byte)(c.getPacketSize() & 0xff),
				(byte)(c.getPacketSize() >> 8 & 0xff),
				(byte)(c.getPacketSize() >> 16 & 0xff),
				(byte)(c.getPacketSize() >> 24 & 0xff)
		};
		byte[] temp = new byte[data.length];
		System.arraycopy(data, 0, temp, 0, data.length);
		int idx = header[0];
		for(int i=0 ; i<data.length ; ++i){
			if(i>0 && i%8 == 0){
				for(int j=0 ; j<i ; ++j)
					data[i] ^= data[j];
				if(i%16 == 0){
					for(byte st : header)
						data[i] ^= st;
				}
				for(int j=1 ; j<header.length ; ++j)
					data[i] ^= header[j];
				try {
					for(int j=1 ; j<header.length ; ++j)
						data[i+j] ^= header[j];
				} catch (Exception e) { }
			}else{
				data[i] ^= idx;
				try {
					if(i==0){
						for(int j=1 ; j<header.length ; ++j){
							data[i+j] ^= header[j];
						}
					}
				} catch (Exception e) { }
			}
			idx = temp[i];
		}
		temp = null;
		header = null;
	}
	
	// 접속기에서 받은 패킷 처리
	private boolean fromConnector(LineageClient client) {
		byte[] header = new byte[4];
		header[0] = (byte) (client.getBuffer().getByte(0) & 0xff);
		header[1] = (byte) (client.getBuffer().getByte(1) & 0xff);
		header[2] = (byte) (client.getBuffer().getByte(2) & 0xff);
		header[3] = (byte) (client.getBuffer().getByte(3) & 0xff);

		boolean isFromConnector = header[0] == 127 && header[1] == 127 && header[2] == 127 && header[3] == 127;
		if (!isFromConnector)
			return false;

		// 헤더 4바이트 읽기 처리
		client.getBuffer().readBytes(4);

		// 읽은 부분까지 제거하고 읽는위치값 0으로 초기화.
		client.getBuffer().discardReadBytes();

		// 접속기를 통한 접속임을 저장
		client.setFromConnector(true);

		return true;
	}
	
}