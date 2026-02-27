package lineage.thread;

import java.util.ArrayList;
import java.util.List;

import lineage.network.LineageServer;
import lineage.share.Common;
import lineage.share.TimeLine;
import lineage.database.ServerDatabase;
import lineage.database.TimeDungeonDatabase;
import lineage.world.controller.BossController;
import lineage.world.controller.CharacterController;
import lineage.world.controller.ColosseumController;
import lineage.world.controller.ElvenforestController;
import lineage.world.controller.SlimeRaceController;
import lineage.world.controller.BuffController;
import lineage.world.controller.ClanController;
import lineage.world.controller.DungeonController;
import lineage.world.controller.InnController;
import lineage.world.controller.MagicDollController;
import lineage.world.controller.PcMarketController;
import lineage.world.controller.QuestController;
import lineage.world.controller.SummonController;
import lineage.world.controller.WantedController;
import lineage.world.controller.DogRaceController;



public class TimeThread implements Runnable {
	static public TimeThread thread;
	// 쓰레드동작 여부
	static private boolean running;
	
	static public void init(){
		TimeLine.start("TimeThread..");
		
		thread = new TimeThread();
		start();
				
		TimeLine.end();
	}
	
	static public void start() {
		if (thread == null)
			thread = new TimeThread();
		
		running = true;
		Thread t = new Thread( thread );
		t.setName(TimeThread.class.toString());
		t.start();
	}
	
	@Override
	public void run() {
		for(;running;){
			try {
				List<Object> temp = new ArrayList<Object>();
				long time = System.currentTimeMillis();
				// 버프 관리
				try { BuffController.toTimer(time); } catch (Exception e) {
					lineage.share.System.println("버프 관리");
					lineage.share.System.println(e);
				}
				// 서먼몬스터 관리
				try { SummonController.toTimer(); } catch (Exception e) {
					lineage.share.System.println("서먼몬스터 관리");
					lineage.share.System.println(e);
				}

				// 여관 관리
				try { InnController.toTimer(time, temp); } catch (Exception e) {
					lineage.share.System.println("여관 관리");
					lineage.share.System.println(e);
				}

				// 보스몬스터 관리.
				try { BossController.toTimer(time); } catch (Exception e) {
					lineage.share.System.println("보스몬스터 관리.");
					lineage.share.System.println(e);
				}
				
				// 콜로세움 관리
//				try { ColosseumController.toTimer(time); } catch (Exception e) {
//					lineage.share.System.println("콜로세움 관리");
//					lineage.share.System.println(e);
//				}
				
				// 마법인형 관리.
				try { MagicDollController.toTimer(time, temp); } catch(Exception e) {
					lineage.share.System.println("마법인형 관리.");
					lineage.share.System.println(e);
				}

				// 현상수배 관리.
				try { WantedController.toTimer(time); } catch (Exception e) {
					lineage.share.System.println("현상수배 관리.");
					lineage.share.System.println(e);
				}
				
				// 요정숲 관리
				try { ElvenforestController.toTimer(time); } catch (Exception e) {
					lineage.share.System.println("요정숲 관리");
					lineage.share.System.println(e);
				}
				
				try { DungeonController.toTimer(time); } catch (Exception e) {
					lineage.share.System.println("던전 이용 시간 관리.");
					lineage.share.System.println(e);
				}
				
				try { TimeDungeonDatabase.toTimer(time); } catch (Exception e) {
					lineage.share.System.println("이벤트맵 이용 시간 관리.");
					lineage.share.System.println(e);
				}
				
//				try { ClanController.toTimer(time); } catch (Exception e) {
//					lineage.share.System.println("혈맹 관리.");
//					lineage.share.System.println(e);
//				}
				
//				//무인 상점 관리.
//				try { PcMarketController.toTimer(time); } catch (Exception e) {
//					lineage.share.System.println("무인 상점 관리.");
//					lineage.share.System.println(e);
//				}
				
				// 강아지 레이스 관리.
				try {
					DogRaceController.toTimer(time); } catch (Exception e) {
					lineage.share.System.println("강아지 레이스 관리.");
					lineage.share.System.println(e);
				}
				
			    // 슬라임레이스 관리. 
			 	try { SlimeRaceController.toTimer(time); } catch (Exception e) {
					//lineage.share.System.println("슬라임레이스 관리.");
					//lineage.share.System.println(e);
				}
				
				// 퀘스트 관리
				try { QuestController.toTimer(time); } catch (Exception e) {
					lineage.share.System.println("퀘스트 관리");
					lineage.share.System.println(e);
				}
				
				Thread.sleep(Common.TIMER_SLEEP);
			} catch (Exception e) {
				lineage.share.System.printf("lineage.thread.TimeThread.run()\r\n : %s\r\n", e.toString());
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
}
