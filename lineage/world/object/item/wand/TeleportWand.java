package lineage.world.object.item.wand;

import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.share.System;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class TeleportWand extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new TeleportWand();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if (cha.isFishing()) {
			ChattingController.toChatting(cha, "낚시중에는 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		long obj_id = cbp.readD();
		int x = cbp.readH();
		int y = cbp.readH();
		
		PcInstance pc = (PcInstance) cha;
		pc.toTeleport(x, y, cha.getMap(), false);
	}
}