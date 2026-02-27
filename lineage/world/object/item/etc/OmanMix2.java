package lineage.world.object.item.etc;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.database.Item;
import lineage.bean.lineage.Craft;
import lineage.network.packet.ClientBasePacket;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

import lineage.database.ItemDatabase;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;



public class OmanMix2 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new OmanMix2();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		Map<Item, List<Craft>> list = new HashMap<Item, List<Craft>>();
		
		Item i = null;
		
		if(this.item.getName().indexOf("100") > -1){
			i = ItemDatabase.find("오만의탑 100층 이동 부적");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				
				l.add( new Craft(ItemDatabase.find("오만의 탑 100층 이동 주문서"), 100) );
				l.add( new Craft(ItemDatabase.find("아데나"), 1000000) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				if(Util.random(0, 99)<=60){
					CraftController.toCraft(cha, i, 1, true, 0);
				}else{
					ChattingController.toChatting(cha, "제작에 실패하셨습니다.");
				}
			}
		}
		cha.getInventory().count(this, getCount()-1, true);
	}
}
