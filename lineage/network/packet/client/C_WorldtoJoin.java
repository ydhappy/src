package lineage.network.packet.client;

import lineage.database.AccountDatabase;
import lineage.database.CharactersDatabase;
import lineage.network.LineageClient;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Disconnect;
import lineage.network.packet.server.S_Unknow;
import lineage.world.World;
import lineage.world.object.instance.PcInstance;

public class C_WorldtoJoin extends ClientBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length) {
		if (bp == null)
			bp = new C_WorldtoJoin(data, length);
		else
			((C_WorldtoJoin) bp).clone(data, length);
		return bp;
	}

	public C_WorldtoJoin(byte[] data, int length) {
		clone(data, length);
	}

	@Override
	public BasePacket init(LineageClient c) {
		String name = readS();

		if (name == null || name.length() <= 0)
			return this;

		synchronized (LineageClient.sync_global) {
			if (CharactersDatabase.isCharacter(c.getAccountUid(), name)) {
				PcInstance pc = World.findPc(name);
				if (pc != null)
					pc.toWorldOut();

				// 접속 성공 알림
				c.toSender(S_Unknow.clone(BasePacketPooling.getPool(S_Unknow.class)));
				// pc객체 정보 추출
				pc = CharactersDatabase.readCharacter(c, name);
				// 월드 접속한 날자 업데이트
				CharactersDatabase.updateCharacterJoinTimeStamp(name);
				
				pc.setPass2th(AccountDatabase.getPass2th(c.getAccountUid()));
				if (c.getOldIp().equalsIgnoreCase(c.getAccountIp())) {
					pc.setPass2th(true);
				}
				// 월드 진입 알리기.
				if (pc != null)
					pc.toWorldJoin();
			

				// 테스트
//			if(Lineage.server_version>=340){
//				pc.toSender(S_Unknow2.clone(BasePacketPooling.getPool(S_Unknow2.class), 1));
//				pc.toSender(S_Unknow2.clone(BasePacketPooling.getPool(S_Unknow2.class), 2));
//				pc.toSender(S_Unknow2.clone(BasePacketPooling.getPool(S_Unknow2.class), 3));
//			}
			} else {
				c.toSender(S_Disconnect.clone(BasePacketPooling.getPool(S_Disconnect.class), 0x0A));
			}
		}

		return this;
	}
}
