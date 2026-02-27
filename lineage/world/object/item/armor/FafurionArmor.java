package lineage.world.object.item.armor;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class FafurionArmor extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new FafurionArmor();
		return item;
	}
	
	@Override
	public void toDamage(Character cha, int dmg, int type, Object...opt) {
		// 확률 체크 (5 + 갑옷인첸트값)%
		if(Util.random(0, 99) <= 2+getEnLevel()) {
			int hp = Util.random(5, 10);
			if(getCharacter().isBuffWaterLife())
				hp *= 2;
			if(getCharacter().isBuffPolluteWater())
				hp /= 2;
			// HP를 회복
			getCharacter().setNowHp(getCharacter().getNowHp() + hp);
			// 이팩트 표현
			getCharacter().toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), getCharacter(), 2187), true);
		}
	}
}
