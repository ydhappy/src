package lineage.world.object.item.etc;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.database.Item;
import lineage.bean.lineage.Craft;
import lineage.network.packet.ClientBasePacket;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

import lineage.database.ItemDatabase;
import lineage.world.controller.CraftController;



public class Mix5 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Mix5();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		Map<Item, List<Craft>> list = new HashMap<Item, List<Craft>>();
		
		Item i = null;
		
		if(this.item.getName().indexOf("오리하루콘도금뿔") > -1){
			i = ItemDatabase.find("오리하루콘 도금 뿔");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("판의 뿔"), 4) );
				l.add( new Craft(ItemDatabase.find("오리하루콘"), 80) );
				l.add( new Craft(ItemDatabase.find("루비"), 3) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				CraftController.toCraft(cha, i, 1, true, 0);				
			}
		}
		else if(this.item.getName().indexOf("장") > -1){
			i = ItemDatabase.find("장검신");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("페어리의 날개"), 3) );
				l.add( new Craft(ItemDatabase.find("미스릴"), 150) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				CraftController.toCraft(cha, i, 1, true, 0);				
			}
		}else if(this.item.getName().indexOf("단") > -1){
			i = ItemDatabase.find("단검신");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("페어리의 날개"), 1) );
				l.add( new Craft(ItemDatabase.find("미스릴"), 50) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				CraftController.toCraft(cha, i, 1, true, 0);				
			}
		}
		else if(this.item.getName().indexOf("블랙 미스릴 화살") > -1){	
			i = ItemDatabase.find("블랙 미스릴 화살");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				
				l.add( new Craft(ItemDatabase.find("블랙 미스릴"), 1) );
				l.add( new Craft(ItemDatabase.find("백금괴"), 1) );
				l.add( new Craft(ItemDatabase.find("엔트의 줄기"), 10) );
				l.add( new Craft(ItemDatabase.find("아데나"), 1000) );
				list.put(i, l);
			}
			//재료 확인
			if(CraftController.isCraft(cha, l, true)){
				//재료제거
				CraftController.toCraft(cha, l);
				// 지급.
				CraftController.toCraft(cha, i, 1, true, 0);				
			}
	
	}
	}
}
