package lineage.world.object.item.etc;

import lineage.bean.lineage.Inventory;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class Ani extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Ani();
		return item;
	}
	@Override
	public void toEquipped(Character cha, Inventory inv, int slot){
		super.toEquipped(cha, inv, slot);
	
		
		if(equipped){
			cha.setAden(cha.getAden()+4);
			//setDynamicAden(3);
		}else{
			cha.setAden(cha.getAden()-4);
		}
	}
		/*

		if(equipped){
				// 이전에 세팅값 빼기.
				pc.setDynamicAden(pc.getDynamicAden()-getDynamicAden());
				// 인첸에따른 새로운값 적용.
				pc.setDynamicAden(pc.getDynamicAden()+new_adb);
			}
			setDynamicAden(new_adb);

			*/


}
