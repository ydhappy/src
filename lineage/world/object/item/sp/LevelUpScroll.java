package lineage.world.object.item.sp;

import lineage.bean.database.Exp;
import lineage.database.ExpDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class LevelUpScroll extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new LevelUpScroll();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		cha.getInventory().count(this, getCount()-1, true);
		
		Exp e = ExpDatabase.find(cha.getLevel());
		cha.setExp( e.getBonus() );
	}

}
