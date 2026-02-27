package lineage.thread;

import lineage.share.Common;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.controller.AgitController;
import lineage.world.controller.AuctionController;
import lineage.world.controller.FightController;
import lineage.world.controller.KingdomController;
import lineage.world.controller.RankController;
//import lineage.world.controller.SpotController;
//import lineage.world.controller.악마왕의영토컨트롤러;
//import lineage.world.controller.지옥컨트롤러;
//import lineage.world.controller.테베라스컨트롤러;
public class ControllerTherad implements Runnable {
	static private ControllerTherad thread;
	// 쓰레드동작 여부
	static private boolean running;
	
	static public void init(){
		TimeLine.start("ControllerTherad..");
		
		thread = new ControllerTherad();
		start();
				
		TimeLine.end();
	}
	
	static private void start() {
		running = true;
		Thread t = new Thread( thread );
		t.setName(ControllerTherad.class.toString());
		t.start();
	}
	
	@Override
	public void run() {
		for(;running;){			
			try {
				long time = System.currentTimeMillis();

				// 팀대전 관리.
//				try { TeamBattleController.toTimer(time); } catch (Exception e) {
//					
//					lineage.share.System.println("팀대전 관리.");
//					lineage.share.System.println(e);
//					
//				}
//				
				// 성 관리
				try { KingdomController.toTimer(time); } catch (Exception e) {
					lineage.share.System.println("성 관리");
					lineage.share.System.println(e);
				}
//				
				// 스팟 쟁탈전 관리.
//				try { SpotController.toTimer(time); } catch (Exception e) {
//					lineage.share.System.println("스팟 쟁탈전 관리.");
//					lineage.share.System.println(e);
//				}
				
				// 테베 던전 관리.
//				try { 테베라스컨트롤러.toTimer(time); } catch (Exception e) {
//					lineage.share.System.println("테베 던전 관리.");
//					lineage.share.System.println(e);
//				}
				
				//랭킹 관리.
				try { RankController.toTimer(time); } catch (Exception e) {
					lineage.share.System.println("랭킹 관리.");
					lineage.share.System.println(e);
				}
				
				// 악영 던전 관리.
//				try { 악마왕의영토컨트롤러.toTimer(time); } catch (Exception e) {
//					lineage.share.System.println("악영 던전 관리.");
//					lineage.share.System.println(e);
//				}
				
//				 경매 관리
				try { if(Lineage.server_version >= 163){AuctionController.toTimer(time);} } catch (Exception e) {
					lineage.share.System.println("경매 관리");
					lineage.share.System.println(e);
				}
//				
				// 아지트 관리
				try { if(Lineage.server_version >= 163){AgitController.toTimer(time);} } catch (Exception e) {
					lineage.share.System.println("아지트 관리");
					lineage.share.System.println(e);
				}
				
				// 콜로세움 관리
//				try { if(Lineage.server_version >= 163){ColosseumController.toTimer(time);} } catch (Exception e) {
//					lineage.share.System.println("콜로세움 관리");
//					lineage.share.System.println(e);
//				}
				
				
				// 투기장 관리
				try { FightController.toTimer(time); } catch (Exception e) {
					lineage.share.System.println("투기장 관리");
					lineage.share.System.println(e);
				}
				
				Thread.sleep(Common.TIMER_SLEEP);
			} catch (Exception e) {
				lineage.share.System.printf("lineage.thread.ControllerTherad.run()\r\n : %s\r\n", e.toString());
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
