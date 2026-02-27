package lineage.world.object.item.weapon;

import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;

public class BowOfSin extends ItemWeaponInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new BowOfSin();
		return item;
	}

	@Override
	public boolean toDamage(Character cha, object o){
	if(Util.random(0,99)<10){
		int steal_mp = 1;
			if(o.getNowMp()>3){
				
				//steal_mp = o.getNowMp();

				if(getEnLevel()>0){
					steal_mp += getEnLevel();										
				}

				o.setNowMp(o.getNowMp()-steal_mp);

				cha.setNowMp(cha.getNowMp()+steal_mp);
			}
	}

	return Util.random(0,99)<10;
	}

	@Override
	public int toDamage(int dmg){


		return Util.random(30, 60);
	}
	
	@Override
	public int toDamageEffect(){

		return 3944;
	}

}
