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



public class Mix4 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Mix4();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		Map<Item, List<Craft>> list = new HashMap<Item, List<Craft>>();
		
		Item i = null;
		
		if(this.item.getName().indexOf("오리하루콘검신") > -1){
			i = ItemDatabase.find("오리하루콘 검신");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("페어리의 날개"), 3) );
				l.add( new Craft(ItemDatabase.find("오리하루콘"), 150) );
				l.add( new Craft(ItemDatabase.find("루비"), 3) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				CraftController.toCraft(cha, i, 1, true, 0);				
			}
		}else if(this.item.getName().indexOf("미스릴도금뿔") > -1){
			i = ItemDatabase.find("미스릴 도금 뿔");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("판의 뿔"), 2) );
				l.add( new Craft(ItemDatabase.find("미스릴"), 80) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				CraftController.toCraft(cha, i, 1, true, 0);				
			}
		}else if(this.item.getName().indexOf("강철부츠") > -1){
			i = ItemDatabase.find("강철 부츠");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("철괴"), 160) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				CraftController.toCraft(cha, i, 1, true, 0);				
			}
	
		}else if(this.item.getName().indexOf("도금뿔오리하루콘") > -1){
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
		}else if(this.item.getName().indexOf("파워 글로브") > -1){
			i = ItemDatabase.find("파워 글로브");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("아라크네의 허물"), 5) );
				l.add( new Craft(ItemDatabase.find("미스릴 실"), 20) );
				l.add( new Craft(ItemDatabase.find("오우거의 피"), 1) );
				l.add( new Craft(ItemDatabase.find("고급 다이아몬드"), 1) );
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

