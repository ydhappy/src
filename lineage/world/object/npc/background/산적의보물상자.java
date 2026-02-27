package lineage.world.object.npc.background;

import lineage.database.ItemDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 산적의보물상자 extends TreasureBox {

	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		// 닫혀있을때만 처리.
		if(gfxMode == 29) {
			ItemInstance item = cha.getInventory().find(ItemDatabase.find("산적의 보물 상자 열쇠"));
			if(item == null) {
				ChattingController.toChatting(cha, "산적의 보물 상자 열쇠가 필요합니다.", 20);
				return;
			}
			//
			cha.getInventory().count(item, item.getCount()-1, true);
			// 상자 열기.
			toOn();
			toSend();
			// 아이템 지급
			// 젤
			CraftController.toCraft(this, cha, ItemDatabase.find(249), Util.random(1, 3), true, 0, 0, Util.random(0, 2));
			// 데이
			CraftController.toCraft(this, cha, ItemDatabase.find(244), Util.random(1, 3), true, 0, 0, Util.random(0, 2));
			// 아데나
			CraftController.toCraft(this, cha, ItemDatabase.find(4), Util.random(10000, 20000), true, 0, 0, 1);
		}
		
	}
}
