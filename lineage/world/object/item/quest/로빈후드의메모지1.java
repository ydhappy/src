package lineage.world.object.item.quest;

import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 로빈후드의메모지1 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 로빈후드의메모지1();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		cha.toSender(new S_Html( this, "robinscroll"));
	}
}
