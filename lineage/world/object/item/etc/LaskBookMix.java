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



public class LaskBookMix extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new LaskBookMix();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		Map<Item, List<Craft>> list = new HashMap<Item, List<Craft>>();
		
		Item i = null;
		
			i = ItemDatabase.find("완성한 라스타바드의 역사서");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("라스타바드의 역사서 1장"), 10) );
				l.add( new Craft(ItemDatabase.find("라스타바드의 역사서 2장"), 10) );
				l.add( new Craft(ItemDatabase.find("라스타바드의 역사서 3장"), 10) );
				l.add( new Craft(ItemDatabase.find("라스타바드의 역사서 4장"), 10) );
				l.add( new Craft(ItemDatabase.find("라스타바드의 역사서 5장"), 10) );
				l.add( new Craft(ItemDatabase.find("라스타바드의 역사서 6장"), 10) );
				l.add( new Craft(ItemDatabase.find("라스타바드의 역사서 7장"), 10) );
				l.add( new Craft(ItemDatabase.find("라스타바드의 역사서 8장"), 10) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				//재료제거
				CraftController.toCraft(cha, l);
				// 지급.
				CraftController.toCraft(cha, i, 1, true, 0);
				
				cha.getInventory().count(this, getCount()-1, true);
			}
			
	}
}
