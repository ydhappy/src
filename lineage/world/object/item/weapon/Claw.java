package lineage.world.object.item.weapon;

import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class Claw extends ItemWeaponInstance {
	
	int i = 0;

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Claw();
		return item;
	}

	@Override
	public boolean toDamage(Character cha, object o){
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
		//	return Util.random(0, 100)<40;
		return Util.random(0, 100)<45;
	}

	@Override
	public int toDamage(int dmg){
		if(i==1){//몬스터 작은몹
			return skill_effect==0 ? (int)(this.getItem().getDmgMin()) : super.toDamage(dmg);
		}else if(i==2){//몬스터 큰몹
			return skill_effect==0 ? (int)(this.getItem().getDmgMax()) : super.toDamage(dmg);
		}else{//사람
			return skill_effect==0 ? (int)(this.getItem().getDmgMin()) : super.toDamage(dmg);
		}
	}
	
	@Override
	public int toDamageEffect() {
		return skill_effect==0 ? 3671 : super.toDamageEffect();
	}
}
