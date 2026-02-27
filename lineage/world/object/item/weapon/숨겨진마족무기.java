package lineage.world.object.item.weapon;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class 숨겨진마족무기 extends ItemWeaponInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 숨겨진마족무기();
		return item;
	}

	@Override
	public boolean toDamage(Character cha, object o){
		// 
		if(o!=null/* && Util.random(0, 100)<5 && (o instanceof MonsterInstance || o instanceof PcInstance)*/){
			int steal = 3;
			boolean typeHpMp = true;
			if(getItem().getNameIdNumber()==8826 || getItem().getNameIdNumber()==8825)
				typeHpMp = false;
			if(typeHpMp) {
				if(o.getNowHp()<steal) {
					steal = o.getNowHp();
					o.setNowHp(o.getNowHp()-steal);
					cha.setNowHp(cha.getNowHp()+steal);
					for(int i=0 ; i<2 ; ++i)
						cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 1), true);
					return true;
				}
			} else {
				if(o.getNowMp()<steal) {
					steal = o.getNowMp();
					o.setNowMp(o.getNowMp()-steal);
					cha.setNowMp(cha.getNowMp()+steal);
					for(int i=0 ; i<2 ; ++i)
						cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 1), true);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int toDamageEffect() {
		return 8150;
	}

}
