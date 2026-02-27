package lineage.world.object.item;

import lineage.plugin.PluginController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class Aden extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Aden();
		return item;
	}

	@Override
	public void toPickup(Character cha) {
		super.toPickup(cha);
		
		PluginController.init(Aden.class, "toPickup", this, cha);
	}
}
