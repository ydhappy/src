package lineage.world.object.item.diablo;

import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class 디아블로의가더 extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 디아블로의가더();
		return item;
	}
	
	@Override
	public void toDamage(Character cha, int dmg, int type, Object...opt) {
		// 관통벨트
		if(Util.random(0, 200) <= getEnLevel()) {
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 1810), true);
			
		}
	}
}
