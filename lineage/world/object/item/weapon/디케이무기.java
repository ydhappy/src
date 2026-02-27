package lineage.world.object.item.weapon;

import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.DecayPotion;

public class 디케이무기 extends ItemWeaponInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 디케이무기();
		return item;
	}

	@Override
	public boolean toDamage(Character cha, object o){
		if(getEnLevel()>=9 && Util.random(0, 100)<5){ //확률
			if(o instanceof PcInstance){
				//Character oo = (Character)o;
				//DecayPotion.init(oo, 5); // 숫자 = 걸리게되는 시간
				ChattingController.toChatting(o, "강력한 마력에 의해 목이 메여집니다.", 20);
				//
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 2232), true);
				//
				BuffController.append(o, DecayPotion.clone(BuffController.getPool(DecayPotion.class), SkillDatabase.find(9, 6), 5));
			}
			return true;
		}else{
			return false;
		}
	}

	@Override
	public int toDamage(int dmg){
		return Util.random(170, 270); // 들어갔을때 대미지 
	}

}
