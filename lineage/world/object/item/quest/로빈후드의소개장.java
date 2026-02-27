package lineage.world.object.item.quest;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 로빈후드의소개장 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 로빈후드의소개장();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "robinhood"));
	}

}
