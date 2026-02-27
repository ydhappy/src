package sp.item;

import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.PvpController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class PvpLogClear extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new PvpLogClear();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		ChattingController.toChatting(cha, "PVP 로그가 초기화 되었습니다.", 20);
		PvpController.clearPvp((PcInstance)cha);
		cha.getInventory().count(this, getCount()-1, true);
	}

}
