package lineage.network.packet;

import lineage.network.LineageClient;
import lineage.share.Common;
import lineage.world.object.instance.PcInstance;

public class ClientBasePacket implements BasePacket {

	private byte[] data;
	private int _off;
	private long result;
	private int size;

	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new ClientBasePacket();
		((ClientBasePacket)bp).clone(data, length);
		return bp;
	}
	
	@Override
	public BasePacket init(LineageClient c){
		return this;
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		return this;
	}
	
	public void clone(byte[] data, int length){
		this.data = data;
		this.size = length;
		_off = 1;
	}
	
	/**
	 * 다음 읽을 데이타가 존재하는지 확인해주는 함수.
	 * @return
	 */
	public boolean isRead(int size){
		return _off+size <= this.size;
	}
	
	/**
	 * SpriteTools.java 에서 사용중.
	 * @param off
	 */
	public void seek(int off) {
		_off = off;
	}

	
	/**
	 * SpriteTools.java 에서 사용중.
	 * @param
	 */
	public int readIndex() {
		return _off;
	}

	public int readC(){// 이게 아마 int랑 같은거로 알고있궁
		return data[_off++] &0xff; // 아 ! 100
	}
// 한때 제가 그 모바일게임 크랙같은거 만든다고 게임가디언 만진적이 있었는데 거기 데이터타입도
	// 바이트 128, 월드 32767 , 인트 2147483647 , 롱 100머머머머 였는데 엄청 길었구
	// float 1.37e ,  더블 4.몇몇e 이렇게 까지있었어여
	// 이렇게 6가지 정도 게임에서 쓰이는거같아욤 네 그럼 디비에서 적는건 우리가 데이터 타입을 적는거네요? 마자용
	// 일단은 클라에받아오는 데이터가 막 @beaes 어쩌구 저쩌구 그런단말이죵?? ㅋㅋㅋ
	// 그걸 아스키코드나 유니코드 이런걸로 바꿔서
	// 숫자로 만들구  이런 보고도 모르는 코드로 인트배열로 바꾸는거죵 그럼 이게 글자가 되는거네요?
	// 맨첨에 패킷이 들어올때 무조건 글자로들어와용 머 @[bs머쩌구] 캐릭터이름 나오구 이런식으루 한번 볼까욤?
	/**
	 * SpriteTools.java 에서 사용중.
	 * @return
	 */
	public int readCC(){
		return data[_off++];
	}

	public int readH(){// 얘가 아니구나 8<<구나 시프트연산자 음 시프트연산자 아셔용?? 네대충 16진수를 2진수로 바꾸는방법은여?? 알죠 만약에 그거 기본에서 배웟어여
		result = data[_off++] &0xff;
		result |= data[_off++] << 8 &0xff00;
		return (int)result;// 얘가 딱255까지 될거에용
	}

	/**
	 * SpriteTools.java 에서 사용중.
	 * @return
	 */
	public int readHH(){
		result = data[_off++] &0xff;
		result |= data[_off++] << 8 &0xff00;
		if(result > 60000)
			result = result - 65536;
		return (int)result; // 얘는 65536
	}
	
	public  int readD(){
		result = data[_off++] &0xff;
		result |= data[_off++] << 8 &0xff00;
		result |= data[_off++] << 0x10 &0xff0000;
		result |= data[_off++] << 0x18 &0xff000000;
		return (int)result; // 이친구가 long이니깐 int수 그대로 오나봐용 2147483647
	}
	/*
	public long readD(){
		result = data[_off++] &0xff;
		result |= data[_off++] << 8 &0xff00;
		result |= data[_off++] << 0x10 &0xff0000;
		result |= data[_off++] << 0x18 &0xff000000;
		return result; // 이친구가 long이니깐 int수 그대로 오나봐용 2147483647
	} */

	public double readF(){
		result = data[_off++] &0xff;
		result |= data[_off++] << 8 &0xff00;
		result |= data[_off++] << 0x10 &0xff0000;
		result |= data[_off++] << 0x18 &0xff000000;
		result |= (long)data[_off++] << 0x20 &0xff00000000l;
		result |= (long)data[_off++] << 0x28 &0xff0000000000l;
		result |= (long)data[_off++] << 0x30 &0xff000000000000l;
		result |= (long)data[_off++] << 0x38 &0xff00000000000000l;
		return Double.longBitsToDouble(result); // 얘가  소숫점까지가넹
	}
	
	// 비트 2진수에 4비트(127-7F) 연산 식. 전 이거 첨봐용 추가한건가봐용 네
	public int read4(int size) {
		int result = 0;
		if (size == 0) return 0;
		if (size >= 1) result = data[_off++] & 0x7f;
		if (size >= 2) result |= (data[_off++] << 8 & 0x7f00) >> 1;
		if (size >= 3) result |= (data[_off++] << 16 & 0x7f0000) >> 2;
		if (size >= 4) result |= (data[_off++] << 24 & 0x7f000000) >> 3;
		if (size >= 5) result |= ((long) data[_off++] << 32 & 0x7f00000000L) >> 4;
		return result;
	}
	
	public int read4size() {
		int i = 0;
		int count = 300;
		while (true) {
			if (count-- < 0) {
				break;
			}
			if ((data[_off + i] & 0xff) < 0x80) {
				break;
			} else {
				i++;
			}
		}
		return i + 1;
	}

	public String readS() {
		String text = null;
		try{
			text = new String(data, _off, size-_off, Common.CHARSET);
			int idx = text.indexOf(0x00);
			if(idx>=0){
				text = text.substring(0, idx);
			}
			for(int i=0; i<text.length() ; i++){
				if (text.charAt(i) >= 127) {
					_off += 2;
				} else {
					_off += 1;
				}
			}
			_off += 1;
		}catch (Exception e){
			text = null;
		}
		return text;
	}

	public String readSS() {
		String text = null;
		int loc = 0;
		int start = 0;
		try{
			start = _off;
			while(readH()!=0){
				loc += 2;
			}
			StringBuffer test = new StringBuffer();
			do{
				if ((data[start]&0xff)>=127 || (data[start+1]&0xff)>=127){
					/** 한글 **/
					byte[] t = new byte[2];
					t[0] = data[start+1];
					t[1] = data[start];
					test.append(new String(t, 0, 2, Common.CHARSET));
				}else{
					/** 영문&숫자 **/
					test.append(new String(data, start, 1, Common.CHARSET));
				}
				start+=2;
				loc-=2;
			}while(0<loc);

			text = test.toString();
		}catch (Exception e){
			text = null;
		}
		return text;
	}

	public byte[] readB() {
		byte[] BYTE = new byte[size-_off];
		System.arraycopy(data, _off, BYTE, 0, BYTE.length);
		_off += (BYTE.length+1);
		return BYTE;
	}

	public byte[] readB(int size){
		byte[] BYTE = new byte[size];
		System.arraycopy(data, _off, BYTE, 0, BYTE.length);
		_off += (BYTE.length+1);
		return BYTE;
	}

	public byte[] readBB(int size){
		byte[] BYTE = new byte[size];
		System.arraycopy(data, _off, BYTE, 0, BYTE.length);
		_off += (BYTE.length);
		return BYTE;
	}
}
