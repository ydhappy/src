package lineage.thread;

import lineage.bean.event.Timer;
import lineage.share.Common;
import lineage.share.TimeLine;


public final class TimerThread implements Runnable {

	static private TimerThread thread;
	// 쓰레드동작 여부
	static private boolean running;
	// 타이머 각 프레임별 타임라인값.
	static private long timeline;

	/**
	 * 초기화 함수.
	 */
	static public void init(){
		TimeLine.start("TimerThread..");
		
		thread = new TimerThread();
		
		TimeLine.end();
	}
	
	/**
	 * 쓰레드 활성화 함수.
	 */
	static public void start(){
		running = true;
		Thread t = new Thread(thread);
		t.setName("TimerThread");
		t.start();
	}
	
	/**
	 * 종료 함수
	 */
	static public void close(){
		running = false;
		thread = null;
	}
	
	@Override
	public void run(){
		try {
			for(;running;){
				Thread.sleep(Common.TIMER_SLEEP);
				
				long time = System.currentTimeMillis();
				EventThread.append( Timer.clone(EventThread.getPool(Timer.class), Timer.TYPE.PluginController, time) );
				EventThread.append( Timer.clone(EventThread.getPool(Timer.class), Timer.TYPE.ServerDatabase, time) );
//				EventThread.append( Timer.clone(EventThread.getPool(Timer.class), Timer.TYPE.QuestController, time) );
				EventThread.append( Timer.clone(EventThread.getPool(Timer.class), Timer.TYPE.BugScanningController, time) );
//                EventThread.append( Timer.clone(EventThread.getPool(Timer.class), Timer.TYPE.QuestTodayController, time) );
				EventThread.append( Timer.clone(EventThread.getPool(Timer.class), Timer.TYPE.BackgroundDatabase, time) );
//				EventThread.append( Timer.clone(EventThread.getPool(Timer.class), Timer.TYPE.PartyController, time) );
				EventThread.append( Timer.clone(EventThread.getPool(Timer.class), Timer.TYPE.PvpController, time) );
				EventThread.append( Timer.clone(EventThread.getPool(Timer.class), Timer.TYPE.TimerController3, time) );
				EventThread.append( Timer.clone(EventThread.getPool(Timer.class), Timer.TYPE.CommandController, time) );
				EventThread.append( Timer.clone(EventThread.getPool(Timer.class), Timer.TYPE.TrueTargetController, time) );
				//무한대전
				EventThread.append( Timer.clone(EventThread.getPool(Timer.class), Timer.TYPE.ColosseumController, time) );
	

			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : run()\r\n", TimerThread.class.toString());
			lineage.share.System.println(e);
		}
	}
	
	static public long getTimeLine(){
		return timeline;
	}
}
