package lineage.bean.lineage;

import java.util.ArrayList;
import java.util.List;

import lineage.database.MonsterSpawnlistDatabase;
import lineage.database.ServerDatabase;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.object.monster.LastavardBoss;
import lineage.world.object.monster.LastavardDoorMan;
import lineage.world.object.npc.background.door.LastavardDoor;

public class Lastavard {

	protected int mapId;
	protected List<LastavardBoss> boss_list;			// 보스 목록
	protected List<LastavardDoorMan> doorman_list;	// 문지기 목록
	protected List<LastavardDoor> door_list;			// 문 목록
	private long door_close_time;					// 문이 다시 닫히는 시간 기록.
	private long boss_spawn_time;					// 문지기 및 보스가 재스폰할 시간 기록.
	private int boosDeadCount;						// 문지기 및 보스가 죽을때 해당값을 카운팅함.
	
	public Lastavard(int mapId) {
		boss_list = new ArrayList<LastavardBoss>();
		doorman_list = new ArrayList<LastavardDoorMan>();
		door_list = new ArrayList<LastavardDoor>();
		this.mapId = mapId;
		door_close_time = boss_spawn_time = boosDeadCount = 0;
	}
	
	public void init() {
		toSpawnDoor();
		toSpawnDoorMan();
		toSpawnBoss();
	}
	
	public void close(boolean clear) {
		synchronized (boss_list) {
			for(LastavardBoss lb : boss_list) {
				World.remove(lb);
				lb.clearList(true);
				if(clear)
					lb.setAiStatus(-1);
			}
			if(clear)
				boss_list.clear();
		}
		synchronized (doorman_list) {
			for(LastavardDoorMan ldm : doorman_list) {
				World.remove(ldm);
				ldm.clearList(true);
				if(clear)
					ldm.setAiStatus(-1);
			}
			if(clear)
				doorman_list.clear();
		}
		synchronized (door_list) {
			for(LastavardDoor door : door_list) {
				World.remove(door);
				door.clearList(true);
			}
			if(clear)
				door_list.clear();
		}
		door_close_time = boss_spawn_time = boosDeadCount = 0;
	}
	
	/**
	 * 해당층에 관리할 보스 등록.
	 * @param ldm
	 */
	public void appendBoss(LastavardBoss lb, int x, int y, int map, int heading) {
		lb.setHomeX(x);
		lb.setHomeY(y);
		lb.setHomeMap(map);
		lb.setHomeHeading(heading);
		lb.setLastavard(this);
		synchronized (boss_list) {
			boss_list.add( lb );
		}
	}
	
	/**
	 * 해당층에 관리할 문지기 등록.
	 * @param ldm
	 */
	public void appendDoorMan(LastavardDoorMan ldm, int x, int y, int heading) {
		ldm.setHomeX(x);
		ldm.setHomeY(y);
		ldm.setHomeHeading(heading);
		ldm.setLastavard(this);
		synchronized (doorman_list) {
			doorman_list.add( ldm );
		}
	}
	
	/**
	 * 해당층에 관리할 문을 등록.
	 * @param door
	 */
	public void appendDoor(LastavardDoor door, int x, int y, int map, int heading, int gfx) {
		door.setHomeX(x);
		door.setHomeY(y);
		door.setHomeMap(map);
		door.setHomeHeading(heading);
		door.setClassGfx(gfx);
		door.setClassGfxMode(29);
		door.setGfx(door.getClassGfx());
		door.setGfxMode(door.getClassGfxMode());
		door.setHeading(door.getHomeHeading());
		synchronized (door_list) {
			door_list.add( door );
		}
	}
	
	public int getMap() {
		return mapId;
	}
	
	public void updateBossDead() {
		boosDeadCount += 1;
	}
	
	/**
	 * LastavardController.toTimer 에서 지속적으로 호출함.
	 * @param time
	 */
	public void toTimer(long time) {
		// 문 관리.
		toDoorCheck(time);
		// 문지기 및 보스 관리.
		toBossCheck(time);
	}
	
	protected void toSpawnDoor() {
		synchronized (door_list) {
			for(LastavardDoor door : door_list) {
				if(door.getObjectId() == 0)
					door.setObjectId(ServerDatabase.nextNpcObjId());
				door.toClose();
				door.toTeleport(door.getHomeX(), door.getHomeY(), door.getHomeMap(), false);
			}
		}
	}
	
	protected void toSpawnDoorMan() {
		synchronized (doorman_list) {
			for(LastavardDoorMan ldm : doorman_list)
				MonsterSpawnlistDatabase.toSpawnMonster(ldm, World.get_map(mapId), false, ldm.getHomeX(), ldm.getHomeY(), mapId, 0, 0, false, true, false, true);
		}
	}
	
	protected void toSpawnBoss() {
		synchronized (boss_list) {
			for(LastavardBoss lb : boss_list)
				MonsterSpawnlistDatabase.toSpawnMonster(lb, World.get_map(lb.getHomeMap()), false, lb.getHomeX(), lb.getHomeY(), lb.getHomeMap(), 0, 0, false, true, false, true);
		}
	}
	
	/**
	 * 관리중인 보스 및 문지기를 재스폰 처리함.
	 * @param time
	 */
	private void toBossCheck(long time) {
		if(boss_spawn_time == 0) {
			// 문지기 및 보스가 스폰되있는 상태.
		} else {
			// 문지기 및 보스가 재스폰 대기중인 상태.
			if(time >= boss_spawn_time) {
				toSpawnDoorMan();
				toSpawnBoss();
				boss_spawn_time = 0;
			}
		}
	}
	
	/**
	 * 관리중인 문을 열거나 닫는걸 처리함.
	 * @param time
	 */
	private void toDoorCheck(long time) {
		// 아직 보스나 문지기가 죽은적이 없으면 무시.
		if(boosDeadCount == 0)
			return;
		// 아직 문이 오픈안됫을때
		if(door_close_time == 0) {
			// 문지기 & 보스 죽엇는지 확인.
			// 죽엇다면 관리중인 문 열기.
			if(boosDeadCount >= doorman_list.size()+boss_list.size()) {
				for(LastavardDoor ld : door_list) {
					ld.toOpen();
					ld.toSend();
				}
				// 10분 후 문 닫히게 하기.
				door_close_time = time + (1000*60*10);
			}
		} else {
			// 문이 오픈된시간에서 10분이 지낫다면 다시 닫기.
			if(time >= door_close_time) {
				for(LastavardDoor ld : door_list) {
					ld.toClose();
					ld.toSend();
				}
				door_close_time = boosDeadCount = 0;
				// 문지기 및 보스 10분 후 재 스폰하기. (문대기 10분지난 후임.)
				boss_spawn_time = time + (1000*60*10);
			}
		}
	}
}
