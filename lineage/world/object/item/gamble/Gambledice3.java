package lineage.world.object.item.gamble;

import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class Gambledice3 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Gambledice3();
		return item;
	}

	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		int gfxid = 3229 + Util.random(0,2);
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, gfxid), true);
	}

}
