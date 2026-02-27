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



public class Mix6 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Mix6();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		Map<Item, List<Craft>> list = new HashMap<Item, List<Craft>>();
		
		Item i = null;
		
		if(this.item.getName().indexOf("사이하의활") > -1){
			i = ItemDatabase.find("사이하의 활");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("장궁"), 1) );
				l.add( new Craft(ItemDatabase.find("풍룡 비늘"), 15) );
				l.add( new Craft(ItemDatabase.find("그리폰의 깃털"), 30) );
				l.add( new Craft(ItemDatabase.find("바람의 눈물"), 50) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				CraftController.toCraft(cha, i, 1, true, 0);				
			}
		}
		if(this.item.getName().indexOf("싸울아비장검") > -1){
			i = ItemDatabase.find("싸울아비 장검");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("오리하루콘"), 500) );
				l.add( new Craft(ItemDatabase.find("최고급 다이아몬드"), 5) );
				l.add( new Craft(ItemDatabase.find("최고급 사파이어"), 5) );
				l.add( new Craft(ItemDatabase.find("최고급 에메랄드"), 5) );
				l.add( new Craft(ItemDatabase.find("최고급 루비"), 5) );
				l.add( new Craft(ItemDatabase.find("아시타지오의 재"), 30) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				CraftController.toCraft(cha, i, 1, true, 0);				
			}
		}
		
	}
}
