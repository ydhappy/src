package lineage.world.object.item.weapon;

import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;

public class Alice extends ItemWeaponInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Alice();
		return item;
	}
	
	@Override
	public void close(){
		super.close();

		grade = 1;
	}

}
