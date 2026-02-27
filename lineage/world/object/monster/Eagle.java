package lineage.world.object.monster;

import lineage.bean.database.Monster;
import lineage.world.object.Character;
import lineage.world.object.instance.MonsterInstance;

public class Eagle extends MonsterInstance {
	
	static synchronized public MonsterInstance clone(MonsterInstance mi, Monster m){
		if(mi == null)
			mi = new Eagle();
		return MonsterInstance.clone(mi, m);
	}

	@Override
	protected void toAiWalk(long time) {
		// 휴식시간 없음. 계속 움직이게 하기 위해.
		ai_walk_stay_count = 0;
		//
		super.toAiWalk(time);
	}


	@Override
	public void toDamage(Character cha, int dmg, int type, Object...opt) {
		// 공격받아도 무시하기 위해.
	}
}
