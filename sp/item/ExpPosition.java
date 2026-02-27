package sp.item;

import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class ExpPosition extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ExpPosition();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		ChattingController.toChatting(cha, "경험치 물약을 복용 하였습니다.", 20);
		cha.toExp(this, Util.random(370000, 370001));
		cha.getInventory().count(this, getCount()-1, true);
	}

}
