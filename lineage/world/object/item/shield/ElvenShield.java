package lineage.world.object.item.shield;

import lineage.bean.lineage.Inventory;
import lineage.share.Lineage;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class ElvenShield extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ElvenShield();
		return item;
	}

	@Override
	public void toEquipped(Character cha, Inventory inv, int slot){
		super.toEquipped(cha, inv, slot);
		
		if(cha.getClassType() == 0x02){
			if(equipped){
				// 적용
				cha.setDynamicMr( cha.getDynamicMr() + 5 );
			}else{
				// 해제
				cha.setDynamicMr( cha.getDynamicMr() - 5 );
			}
		}
	}
}
