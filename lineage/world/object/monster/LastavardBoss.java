package lineage.world.object.monster;

import lineage.bean.database.Monster;
import lineage.world.object.instance.MonsterInstance;

public class LastavardBoss extends LastavardDoorMan {

	static synchronized public MonsterInstance clone(MonsterInstance mi, Monster m) {
		if(mi == null)
			mi = new LastavardBoss();
		return MonsterInstance.clone(mi, m);
	}
	
	@Override
	public void close() {
		super.close();
	}

}
