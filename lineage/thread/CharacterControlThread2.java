package lineage.thread;

import lineage.share.Common;
import lineage.share.Lineage;
import lineage.share.System;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.robot.PcRobotInstance;


public class CharacterControlThread2 implements Runnable {
	static private CharacterControlThread2 thread;
	// 쓰레드동작 여부
	static private boolean running;

	static public void init() {
		TimeLine.start("CharacterThread2..");

		 thread = new CharacterControlThread2();
		 start();

		TimeLine.end();
	}

	static public void start() {
		running = true;
		Thread t = new Thread(thread);
		t.setName(CharacterControlThread2.class.toString());
		t.start();
	}

	@Override
	public void run() {
		for (; running;) {
			try {
				if (World.getPcList().size() < 1) {
					Thread.sleep(Common.TIMER_SLEEP);
					continue;
				}
				long time = System.currentTimeMillis();
				// 자동 칼질
				for (PcInstance pc : World.getPcList()) {

					try {
						if (!pc.isAutoAttack || pc instanceof PcRobotInstance || pc.autoAttackTime > time)
							continue;

						if (pc.autoAttackTarget == null) {
							pc.resetAutoAttack();
							continue;
						}

						if (pc.autoAttackTarget.isDead() || pc.autoAttackTarget.isInvis() || pc.autoAttackTarget.isTransparent()) {
							pc.resetAutoAttack();
							continue;
						}
					
						if (pc.isLock() || pc.isDead()) {
							pc.resetAutoAttack();
							continue;
						}
						
						if (!Util.isAreaAttack(pc, pc.autoAttackTarget) || !Util.isAreaAttack(pc, pc.autoAttackTarget)) {
							continue;
						}
						
						if (pc.getMap() != pc.autoAttackTarget.getMap()) {
							pc.resetAutoAttack();
							continue;
						}
						
						boolean bow = false;
						int range = getRange(pc);
						ItemInstance weapon = pc.getInventory().getSlot(Lineage.SLOT_WEAPON);
						if (weapon != null && weapon.getItem() != null) {				
							if (weapon.getItem().getType2().equalsIgnoreCase("bow")) {
								bow = true;
								range = 11;
							} else if (weapon.getItem().getType2().equalsIgnoreCase("spear")) {
								range = 2;
							}
						}
					
						if (!bow && (pc.targetX != pc.autoAttackTarget.getX() || pc.targetY != pc.autoAttackTarget.getY())) {
							pc.resetAutoAttack();
							continue;
						}
						
						if (!Util.isDistance(pc, pc.autoAttackTarget, range)) {
							continue;
						}
						
						if (pc.autoAttackTime < time) {
							if (bow)
								pc.toAttack(pc.autoAttackTarget, pc.autoAttackTarget.getX(), pc.autoAttackTarget.getY(),
										true, 0, 0, false, 0);
							else
								pc.toAttack(pc.autoAttackTarget, pc.autoAttackTarget.getX(), pc.autoAttackTarget.getY(),
										false, 0, 0, false, 0);
						}
					} catch (Exception e) {
						lineage.share.System.printf("자동칼질 시스템 에러\r\n : %s\r\n", e.toString());
						lineage.share.System.printf("캐릭터: %s", pc.getName());
						pc.resetAutoAttack();

					}
				}

				Thread.sleep(Common.THREAD_SLEEP);
			} catch (Exception e) {
				lineage.share.System.printf("lineage.thread.CharacterControlThread2.run()\r\n : %s\r\n", e.toString());
			}
		}
	}

	/**
	 * 쓰레드 종료처리 함수.
	 */
	static public void close() {
		running = false;
		thread = null;
	}
	
		/**
		 * 특정 몹은 사거리 증가
		 * 2021-01-23
		 * by connector12@nate.com
		 */
		static public int getRange(PcInstance pc) {
			int range = 1;
			
			if (pc != null && pc.autoAttackTarget != null && pc.autoAttackTarget instanceof MonsterInstance) {
				MonsterInstance mi = (MonsterInstance) pc.autoAttackTarget;
				
				if (mi.getMonster() != null && mi.getMonster().getName() != null) {
					switch (mi.getMonster().getName()) {
					case "안타라스":
					case "발라카스":
					case "파푸리온":
					case "바실리스크":
					case "[지옥] 바실리스크":
					case "[초보존] 계곡 바실리스크":
					case "[잊혀진섬] 바실리스크":
					case "터틀 드래곤":
					case "에바 터틀 드래곤":
						range = 2;
						break;
					}
				}
			}
			
			return range;
		}
	}