package lineage.world.object.monster;

import lineage.bean.database.Monster;
import lineage.bean.database.MonsterSpawnlist;
import lineage.bean.lineage.Lastavard;
import lineage.database.SpriteFrameDatabase;
import lineage.share.Lineage;
import lineage.world.object.instance.MonsterInstance;

public class LastavardDoorMan extends MonsterInstance {

	static synchronized public MonsterInstance clone(MonsterInstance mi, Monster m) {
		if(mi == null)
			mi = new LastavardDoorMan();
		return MonsterInstance.clone(mi, m);
	}
	
	private Lastavard lastavard;
	
	@Override
	public void close() {
		super.close();
	}
	
	@Override
	public void toTeleport(int x, int y, int map, boolean effect) {
		//
		setDead(false);
		setNowHp(getMaxHp());
		setNowMp(getMaxMp());
		setGfx(getClassGfx());
		setGfxMode(getClassGfxMode());
		setAiStatus(0);
		readDrop();
		//
		super.toTeleport(x, y, map, effect);
		//
		setMonsterSpawnlist( new MonsterSpawnlist() );
		getMonsterSpawnlist().setSentry( true );
		getMonsterSpawnlist().setHeading( getHomeHeading() );
	}

	@Override
	protected void toAiDead(long time) {
		super.toAiDead(time);
		
		getLastavard().updateBossDead();
	}

	@Override
	protected void toAiSpawn(long time){
		ai_time = SpriteFrameDatabase.find(gfx, gfxMode+8);
		// 스폰 대기.
	}

	public Lastavard getLastavard() {
		return lastavard;
	}

	public void setLastavard(Lastavard lastavard) {
		this.lastavard = lastavard;
	}

}
