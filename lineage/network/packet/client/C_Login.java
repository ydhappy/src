package lineage.network.packet.client;

import lineage.database.AccountDatabase;
import lineage.network.LineageClient;
import lineage.network.LineageServer;
import lineage.network.netty.lineage.ProtocolHandler;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Disconnect;
import lineage.network.packet.server.S_Login;
import lineage.network.packet.server.S_LoginFail;
import lineage.network.packet.server.S_Notice;
import lineage.network.packet.server.S_TimeLeft;
import lineage.share.Lineage;
import lineage.share.Socket;
import lineage.util.Util;

public class C_Login extends ClientBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length) {
		if (bp == null)
			bp = new C_Login(data, length);
		else
			((C_Login) bp).clone(data, length);
		return bp;
	}

	public C_Login(byte[] data, int length) {
		clone(data, length);
	}

	@Override
	public BasePacket init(LineageClient c) {
		String id = readS().trim();
		String pw = readS().trim();

		if (id == null || pw == null || id.length() <= 0 || pw.length() <= 0)
			return this;

		if (Socket.CHECK_CLIENT) {
			if (!c.getAccountIp().equalsIgnoreCase("127.0.0.1") && !c.getAccountIp().equalsIgnoreCase("121.150.2.6")) {
				if (ProtocolHandler.list_ip.contains(c.getAccountIp())) {
				} else {
					c.toSender(S_LoginFail.clone(BasePacketPooling.getPool(S_LoginFail.class),
							S_LoginFail.LOGIN_USER_OR_ID_AND_PASS_WRONG));
					c.toSender(S_Notice.clone(BasePacketPooling.getPool(S_Notice.class),
							String.format("\\f2%s\r\n%s", "비정상적 접근 발견.", "제대로된 접속기로 접속해주시기 바랍니다.")));
					return this;
				}
			}
		}

		// 접속기 인증
//		if(!c.getAccountIp().equalsIgnoreCase("127.0.0.1") 
//				&& !c.getAccountIp().equalsIgnoreCase("192.168.0.1")
//				&& !c.getAccountIp().equalsIgnoreCase("120.29.131.162")
//				&&!c.getAccountIp().equalsIgnoreCase("1.226.90.141") 
//				&&!c.getAccountIp().equalsIgnoreCase("221.157.139.88")
//				&& !c.getAccountIp().equalsIgnoreCase("49.1.107.49")  
//				&& !c.getAccountIp().equalsIgnoreCase("112.149.243.156")
//				&& !c.getAccountIp().equalsIgnoreCase("220.121.56.5")
//				&& !c.getAccountIp().equalsIgnoreCase("118.91.37.129")
//				&& !c.getAccountIp().equalsIgnoreCase("172.30.1.254")
//				&& !c.getAccountIp().equalsIgnoreCase("121.150.150.142")
////				&& !c.getAccountIp().equalsIgnoreCase("112.160.81.215")
//				&& !c.isFromConnector()) {
//			lineage.share.System.println("["+c.getAccountIp() + "] 접속기 미사용으로 인한 Login Failed");
//			// 소켓 닫기 및 뒷처리
//			c.close();
//			return this;
//		}

		synchronized (LineageClient.sync_global) {
			int uid = AccountDatabase.getUid(id);
			if (uid > 0) {
				if ((AccountDatabase.isAccount(uid, id, pw) || AccountDatabase.isAccountOld(uid, id, pw))
						&& !AccountDatabase.isBlock(uid)) {
					LineageClient find_c = LineageServer.find(uid);
					if (find_c != null) {
						// 다른사람이 해당 계정을 사용중일경우.
						c.toSender(S_LoginFail.clone(BasePacketPooling.getPool(S_LoginFail.class),
								S_LoginFail.LOGIN_USER_ON));
						// 해당 클라에게 종료 패킷 전송.
						find_c.toSender(S_Disconnect.clone(BasePacketPooling.getPool(S_Disconnect.class), 0x16));
						// 해당클로 강제 종료 처리.
						LineageServer.close(find_c);
					} else {
						// 패스워드 기록.
						AccountDatabase.changePw(uid, pw);
						// 처리
						toLogin(c, id, uid);
					}
				} else {
					// 패스워드가 일치하지 않음.
					c.toSender(S_LoginFail.clone(BasePacketPooling.getPool(S_LoginFail.class),
							S_LoginFail.LOGIN_USER_OR_PASS_WRONG));
				}
			} else {
				if (Lineage.account_auto_create && AccountDatabase.isAccountCount(c.getAccountIp())) {
					// 계정 자동생성 처리.
					AccountDatabase.insert(id, pw);
					// uid 재 추출
					uid = AccountDatabase.getUid(id);
					// 로그인 처리.
					toLogin(c, id, uid);
				} else {
					// 계정이 존재하지 않음.
					c.toSender(S_LoginFail.clone(BasePacketPooling.getPool(S_LoginFail.class),
							S_LoginFail.LOGIN_USER_OR_ID_AND_PASS_WRONG));
				}
			}
		}

		return this;
	}

	private void toLogin(LineageClient c, String id, int uid) {
		int time = 0;

		try {
			// 0.01~0.1초 랜덤 딜레이
			Thread.sleep(Util.random(10, 300));
		} catch (Exception e) {

		}

		if (Lineage.flat_rate && Lineage.server_version >= 163) {
			// 남은 시간 추출
			time = AccountDatabase.getTime(uid);
			if (time <= 0) {
				// 정액 시간이 완료됨.
				c.toSender(
						S_LoginFail.clone(BasePacketPooling.getPool(S_LoginFail.class), S_LoginFail.REASON_ACCESS_END));
				return;
			}
			c.setAccountTime(time);
		}

		// 로그인 처리.
		c.setAccountUid(uid);
		c.setAccountId(id);
		c.setAccountNoticeStaticUid(0);
		c.setAccountNoticeUid(AccountDatabase.getNoticeUid(uid));
		c.setAccountTam(AccountDatabase.getTam(uid));
		
		c.setOldIp(AccountDatabase.getOldIp(c.getAccountUid()));
		
		// 로그인한 아이피 갱신.
		AccountDatabase.updateIp(c.getAccountUid(), c.getAccountIp());
		// 로그이한 시간 갱신.
		AccountDatabase.updateLoginsDate(c.getAccountUid());
		// 패킷 처리.
		if (time > 0) {
			if (Lineage.server_version < 300)
				c.toSender(S_Login.clone(BasePacketPooling.getPool(S_Login.class), S_LoginFail.REASON_ACCESS_OK, time));
			c.toSender(S_TimeLeft.clone(BasePacketPooling.getPool(S_TimeLeft.class)));
		} else {
//			if(Lineage.server_version >= 360) {
//				c.toSender( S_Login.clone(BasePacketPooling.getPool(S_Login.class), S_LoginFail.REASON_ACCESS_OK, 0) );
//				c.toSender( S_TimeLeft.clone(BasePacketPooling.getPool(S_TimeLeft.class)) );
//			} else {
			BasePacketPooling.setPool(C_NoticeOk.clone(BasePacketPooling.getPool(C_NoticeOk.class), null, 0).init(c));
//			}
		}
	}
}
