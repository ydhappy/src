package sp.item;

import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class LawfulPosition extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new LawfulPosition();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		ChattingController.toChatting(cha, "라우풀 물약을 복용 하였습니다.", 20);
		cha.setLawful(cha.getLawful() + 3000);
		cha.getInventory().count(this, getCount()-1, true);
	}

}
