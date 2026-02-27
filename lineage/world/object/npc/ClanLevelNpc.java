package lineage.world.object.npc;

import lineage.bean.database.Item;
import lineage.bean.lineage.Clan;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.ClanController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ClanLevelNpc extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "clanlevel"));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if(action.equalsIgnoreCase("A")){
			Item coin = ItemDatabase.find("혈맹 경험치 코인"); // 혈맹코인사용
			ItemInstance clancoin = pc.getInventory().find(coin);
			if(pc.getClanId() == 0){
				ChattingController.toChatting(pc, "혈맹에 소속되어있지 않습니다.", 20);
				return;
			}
			if(clancoin != null){
				Clan cc = ClanController.find(pc);
				pc.getInventory().count(clancoin, clancoin.getCount()-1, true);
//				cc.addClanPoint(100);
				ChattingController.toChatting(pc, "혈맹경험치가 100 증가하였습니다..", 20);
			} else {
				ChattingController.toChatting(pc, "혈맹경험치코인을 보유하고있지 않습니다.", 20);
			}
		}
	}
}
