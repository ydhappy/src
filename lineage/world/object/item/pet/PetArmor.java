package lineage.world.object.item.pet;

import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class PetArmor extends ItemArmorInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new PetArmor();
		return item;
	}

	@Override
	public void close(){
		super.close();
	}

}
