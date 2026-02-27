package lineage.world.object.item.diablo;

import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.CurseParalyze;

public class 아이스블링크 extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 아이스블링크();
		return item;
	}
	
	@Override
	public void toDamage(Character cha, int dmg, int type, Object...opt) {
		// 확률 인첸확률에 의하여 커스패럴라이즈 발동.
		if(Util.random(0, 100) <= 1+ getEnLevel()) {
		//	CurseParalyze.onBuff(cha, SkillDatabase.find(5,0));
		}
	}

}
