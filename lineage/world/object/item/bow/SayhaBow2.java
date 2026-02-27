package lineage.world.object.item.bow;

import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;

public class SayhaBow2 extends ItemWeaponInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new SayhaBow2();
		return item;
	}

	@Override
	public boolean toDamage(Character cha, object o) {
		super.toDamage(cha, o);
		return true;
	}

	@Override
	public int toDamage(int dmg){
		return Util.random(10, 12);
	}
	
	@Override
	public int toDamageEffect() {
		return 66;
	}

}
