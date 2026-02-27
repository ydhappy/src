package sp.item;

import lineage.bean.database.Item;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 이동주문서 extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item, int x, int y, int map){
		if(item == null)
			item = new 이동주문서();
		((이동주문서)item).x = x;
		((이동주문서)item).y = y;
		((이동주문서)item).map = map;
		return item;
	}
	
	private int x;
	private int y;
	private int map;

	@Override
	public ItemInstance clone(Item item) {
		return super.clone(item);
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		//
		
		if(cha.getMap() == 5083 || cha.getMap() == 208){
			ChattingController.toChatting(cha, "[서버알림] 현재공간에서는 사용이 불가능합니다.", 20);
			return;
		}
		
		cha.getInventory().count(this, getCount()-1, true);
		//
		cha.toPotal(x, y, map);
	}

}
