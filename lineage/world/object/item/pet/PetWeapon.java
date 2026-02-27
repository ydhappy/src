package lineage.world.object.item.pet;

import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;

public class PetWeapon extends ItemWeaponInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new PetWeapon();
		return item;
	}

	@Override
	public void close(){
		super.close();
	}

}
