package jsn_soft;

import lineage.bean.lineage.Kingdom;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.KingdomController;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class 기란성버프강화 extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "xeonbuff1"));
	}
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if(action.equalsIgnoreCase("a")) {
            Kingdom k = KingdomController.find(pc);
            if (k == null) {
            	ChattingController.toChatting(pc, "기란 성혈만 이용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                          return;
	}
					Kingdom_giran_buff.init(pc);
					pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
					
				}
			}
		}
	


