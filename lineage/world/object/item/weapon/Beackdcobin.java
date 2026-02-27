package lineage.world.object.item.weapon;

import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.PcInstance;

public class Beackdcobin extends ItemWeaponInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Beackdcobin();
		return item;
	}

	@Override
	public boolean toDamage(Character cha, object o){
		if(o instanceof PcInstance){
			
			return true;
		}else{
		
			return true;
		}
	}

	@Override
	public int toDamage(int dmg){
		return Util.random(0, 3);
	}
	
}
