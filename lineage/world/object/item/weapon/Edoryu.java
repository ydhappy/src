package lineage.world.object.item.weapon;

import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.MonsterInstance;

public class Edoryu extends ItemWeaponInstance {
	
	int i = 0;

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Edoryu();
		return item;
	}

	@Override
	public boolean toDamage(Character cha, object o) {
		if (o instanceof MonsterInstance) {
			MonsterInstance mon = (MonsterInstance) o;
			if(mon.getMonster().getSize().equalsIgnoreCase("small"))
				i = 1;
			else
				i = 2;
		}else{
			i=0;
		}
		
		if(super.toDamage(cha, o))
			return true;
		else
			//return Util.random(0, 100)<33;
		return Util.random(0, 100)<33;
	}

	@Override
	public int toDamage(int dmg){
		if(i==1){//몬스터 작은몹
			return skill_effect==0 ? (int)(this.getItem().getDmgMin() * 2) : super.toDamage(dmg);
		}else if(i==2){//몬스터 큰몹
			return skill_effect==0 ? (int)(this.getItem().getDmgMax() * 2) : super.toDamage(dmg);
		}else{//사람
			return skill_effect==0 ? (int)(this.getItem().getDmgMin() * 2) : super.toDamage(dmg);
		}		
	}
	
	@Override
	public int toDamageEffect() {
		return skill_effect==0 ? 3398 : super.toDamageEffect();
	}

}
