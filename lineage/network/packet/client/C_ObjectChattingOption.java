package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.ClientBasePacket;
import lineage.world.object.instance.PcInstance;

public class C_ObjectChattingOption extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_ObjectChattingOption(data, length);
		else
			((C_ObjectChattingOption)bp).clone(data, length);
		return bp;
	}
	
	public C_ObjectChattingOption(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		// 버그 방지.
		if(!isRead(2) || pc==null || pc.isWorldDelete())
			return this;
		
		int mode = readC();
		int ok = readC();
//		System.out.println(mode);// 여기서 설정을 안해준다.패킷을 안보내는거네요 보내여  패킷은 오는데 설정을 못해주네염??음 그런ㅁ 변수로 설정해야겟네요
//		
//		System.out.println(ok); // 이것도 들어와여
		
		switch(mode){
			case 0x00:	// 전체 채팅
				pc.setChattingGlobal(ok==1);
				break;
			case 0x02:	// 귓속 말
				pc.setChattingWhisper(ok==1);
				break;
			case 0x06:	// 장사 채팅
				pc.setChattingTrade(ok==1);
				break;
		}
		
		return this;
	}
}
