package lineage.world.object.item;

import lineage.network.packet.ClientBasePacket;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 회상의촛불 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 회상의촛불();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
//		// 아이템 수량 갱신
//		cha.getInventory().count(this, getCount()-1, true);
//		// 스탯초기화 창 열기.
//		BaseStatController.toOpen((PcInstance)cha);
		super.toClick(cha, cbp);
	}

}
