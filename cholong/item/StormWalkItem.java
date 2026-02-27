package cholong.item;

import lineage.network.packet.ClientBasePacket;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;

public final class StormWalkItem extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new StormWalkItem();
		return item;
	}

	public void toClick(Character cha, ClientBasePacket cbp){
		// 처리불가능한 패킷상태는 무시.
		//if(!cbp.isRead(4))
		//	return;
		// 초기화
		object o = cha.findInsideList(cbp.readD());
		int x = cbp.readH();
		int y = cbp.readH();
		if(o != null){
			cha.toPotal(o.getX(), o.getY(), cha.getMap());
		} else {
			cha.toPotal(x, y, cha.getMap());
		}
	}
}
