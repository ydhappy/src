package lineage.world.controller;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.lineage.Clan;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.network.packet.server.S_ObjectTitle;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.object.instance.PcInstance;

public final class GiranClanLordController {
	
	static public void init(){
		TimeLine.start("GiranClanLordController..");	
		TimeLine.end();
	}
	
	// 버그 방지를 위해 가입요청자 담을 변수
	private static List<PcInstance> tempList = new ArrayList<PcInstance>();
	
	static public void toAsk(PcInstance pc){
		if (pc instanceof PcInstance && !pc.isWorldDelete() && !pc.isLock() && !pc.isDead() ) {

			if (pc.getClanId() > 0) {
				// 89 \f1이미 혈맹에 가입했습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 89));
			} else {
				if (pc.getLevel() < Lineage.new_clan_max_level) {
					Clan c = ClanController.find(Lineage.new_clan_name);
					if(c != null) {
						pc.setClanId(c.getUid());
						pc.setClanName(c.getName());
						pc.setTitle("[신규] \\f>보호");
						pc.setClanGrade(0);
						// 패킷 처리
						pc.toSender(S_ObjectTitle.clone(BasePacketPooling.getPool(S_ObjectTitle.class), pc), true);
						// 94 \f1%0%o 혈맹의 일원으로 받아들였습니다.
						c.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 94, pc.getName()));
						// 95 \f1%0 혈맹에 가입하였습니다.
						pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 95, c.getName()));
						// 혈맹 관리목록 갱신
						c.appendMemberList(pc.getName());
						if (!pc.isWorldDelete())
							c.appendList((PcInstance) pc);
					}else {
						ChattingController.toChatting(pc, String.format("%s혈맹은 창설되어 있지 않습니다.", Lineage.new_clan_name));
					}
				} else
					ChattingController.toChatting(pc,
							String.format("신규 혈맹은 %d레벨 이하 가입 가능합니다.", Lineage.new_clan_max_level - 1),
							Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}
}