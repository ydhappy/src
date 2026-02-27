package lineage.world.object.item.sp;

import lineage.database.SkillDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.BuffRing;

public class 지배반지버프 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 지배반지버프();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null) {
			BuffRing.onBuff(cha, SkillDatabase.find(507));
		}
	}
}
