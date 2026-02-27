package lineage.world.object.item.ring;

import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class RingPolyControl extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new RingPolyControl();
		return item;
	}
	
}
