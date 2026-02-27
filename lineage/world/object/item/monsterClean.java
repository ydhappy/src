package lineage.world.object.item;

import lineage.network.packet.ClientBasePacket;
import lineage.world.controller.CommandController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;


public class monsterClean extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new monsterClean();
		return item;
	}

	public void toClick(Character cha, ClientBasePacket cbp) {
		
		
		PcInstance pc = (PcInstance) cha;
		
		CommandController.toClearMonster(pc);
	}
	

}
