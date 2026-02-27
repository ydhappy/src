package lineage.world.object.item;

import lineage.world.object.instance.ItemInstance;

public class Fishing_rice extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Fishing_rice();
		return item;
	}
}
