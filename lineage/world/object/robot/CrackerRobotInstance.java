package lineage.world.object.robot;

import lineage.database.SpriteFrameDatabase;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BuffController;
import lineage.world.controller.RobotController;
import lineage.world.object.object;
import lineage.world.object.npc.background.Cracker;

public class CrackerRobotInstance extends PcRobotInstance {

	private int x;
	private int y;
	private int map;
	
	public CrackerRobotInstance() {
		setLight(8);
	}

	@Override
	public void close(){
		super.close();
	}
	
	@Override
	public void toTeleport(int x, int y, int map, boolean effect) {
		this.x = x;
		this.y = y;
		this.map = map;
		
		super.toTeleport(x, y, map, effect);
	}

	@Override
	public void toAi(long time) {
		for(object o : getAttackList()) {
			if(containsAstarList(o))
				removeAttackList(o);
		}
		switch(getAiStatus()) {
			case 0:
				if(getAttackListSize()>0)
					setAiStatus(Lineage.AI_STATUS_ATTACK);
				break;
			case 1:
				if(getAttackListSize() == 0)
					setAiStatus(Lineage.AI_STATUS_WALK);
				break;
		}
		if(getLevel()>4 && Util.random(0, 9999)==0) {
			setLevel(1);
			setExp(1);
			BuffController.removeAll(this);
			World.remove(this);
			clearList(true);
			//
			ai_time_temp_1 = 0;
			setAiStatus(Lineage.AI_STATUS_SPAWN);
			return;
		}
		super.toAi(time);
	}

	@Override
	protected void toAiWalk(long time) {
		// 무기착용
		if(weapon!=null && getInventory().getSlot(8)==null) {
			if(getInventory().find(weapon, weaponEn)==null) {
				// 무기 없으면 월드에서 제거.
				toWorldOut();
				ai_time = SpriteFrameDatabase.find(getGfx(), getGfxMode()+0);
				return;
			} else {
				getInventory().find(weapon, weaponEn).toClick(this, null);
			}
			return;
		}
		// 이동가능한 허수아비 찾기.
		object o = searchCracker();
		if(o != null)
			addAttackList(o);
		else {
			do{
				// 방향 전환
				switch(Util.random(0, 10)) {
					case 0:
					case 1:
						setHeading(getHeading()+1);
						break;
					case 2:
					case 3:
						setHeading(getHeading()-1);
						break;
					case 4:
					case 5:
						setHeading(Util.random(0, 7));
						break;
				}
				// 이동 좌표 추출.
				int x = Util.getXY(getHeading(), true)+getX();
				int y = Util.getXY(getHeading(), false)+getY();
				// 해당 좌표 이동가능한지 체크.
				boolean tail = World.isThroughObject(getX(), getY(), getMap(), getHeading()) && World.isMapdynamic(x, y, map)==false;
				// 타일이 이동가능하고 객체가 방해안하면 이동처리.
				if(tail){
					toMoving(null, x, y, getHeading(), false);
				}else{
					if(Util.random(0, 3) != 0)
						continue;
				}
			}while(false);
			//
			clearAttackList();
			clearAstarList();
		}
	}

	@Override
	protected void toAiAttack(long time){
		super.toAiAttack(time);
	}
	
	private object searchCracker() {
		object temp = null;
		for(object o : getAllList(true)) {
			if(o instanceof Cracker && !containsAstarList(o)) {
				if(temp==null || Util.getDistance(this, temp)>Util.getDistance(this, o))
					temp = o;
			}
		}
		return temp;
	}
	
	@Override
	protected object findDangerousObject() {
		object temp = null;
		for(object o : getAttackList()){
			if(!containsAstarList(o)){
				if(temp==null || Util.getDistance(this, temp)>Util.getDistance(this, o))
					temp = o;
			}
		}
		return temp;
	}

	@Override
	protected void toAiSpawn(long time) {
		ai_time = SpriteFrameDatabase.find(gfx, gfxMode+8);

		if(ai_time_temp_1 == 0)
			ai_time_temp_1 = time + Util.random(1000*60*1, 1000*60*2);

		// 스폰 대기
		if(ai_time_temp_1+Lineage.ai_robot_spawn_time > time)
			return;

		//
		isWarSentry = false;
		clearAttackList();
		clearAstarList();
		RobotController.readDrop(this);
		toTeleport(x, y, map, false);
		setAiStatus(Lineage.AI_STATUS_WALK);
	}
	
	@Override
	public void toTimer(long time) {
		super.toTimer(time);
	}

}
