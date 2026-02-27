package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;

public class S_ServerVersion extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp){
		if(bp == null)
			bp = new S_ServerVersion();
		else
			((S_ServerVersion)bp).toClone();
		return bp;
	}
	
	public S_ServerVersion(){
		toClone();
	}
	
	public void toClone(){
		clear();
		writeC(Opcodes.S_OPCODE_SERVERVERSION);
		// Auth Check client Version
		// 1 = Check
		// 0 = no check
		// > 1 no check
		writeC(0x00); // must be
		// your server id, first id = 2
		if(Lineage.nonpvp)
			writeC(0x32); // low version
		else
			writeC(0x01); 	// low version
		if(Lineage.server_version<=144){
			writeD(0x00009D7C); // serverver
			writeD(0x00009D11); // cache version
			writeD(0x0000791A); // auth ver
			writeD(0x00009D74); // npc ver
		}else if(Lineage.server_version<=240){
			writeD(0x000112a9); // serverver
			writeD(0x0000eb93); // cache version
			writeD(0x000a12a2); // auth ver
			writeD(0x000112b0); // npc ver
		}else if(Lineage.server_version < 360) {
			writeD(0x000189da); // serverver
			writeD(0x000189c5); // cache version
			writeD(0x77cef9f0); // auth ver
			writeD(0x000189d4); // npc ver
		}else{
			writeD(150109101); // serverver
			writeD(150109100); // cache version
			writeD(2014110701); // auth ver
			writeD(150109100); // npc ver
		}
		if(Lineage.server_version < 360) {
			writeD((int)(System.currentTimeMillis() / 1000L)); // 로그인시의 시간 설정
			writeC(0x00); 		// unk 1	[ 0x01:게임상에서 회원가입이 가능해짐. ]
			writeC(0x00); 		// unk 2
			writeC(0x00); 		// language korean, 0.US 3.Taiwan 4.Janpan 5.China
		} else {
			writeD((int)(System.currentTimeMillis() / 1000L)); // 로그인시의 시간 설정
			writeC(0x00);
			writeC(0x00);
			writeC(0x00); 		// language korean, 0.US 3.Taiwan 4.Janpan 5.China
			writeD(889191811);		// server type
			writeD((int)(System.currentTimeMillis() / 1000L)); // uptime
			writeD(140728702);
			writeD(150109701);
			writeD(150112700);
		}
	}
	
}
