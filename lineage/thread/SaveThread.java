package lineage.thread;

import lineage.database.CharactersDatabase;
import lineage.persnal_shop.PersnalShopDatabase;
import lineage.share.Lineage;
import lineage.share.TimeLine;

public class SaveThread implements Runnable {
	static private SaveThread thread;
	// 쓰레드동작 여부
	static private boolean running;
	
	static private long ThreadSleep;
	
	static public void init(){
		TimeLine.start("SaveThread..");
		
		thread = new SaveThread();
		ThreadSleep = Lineage.auto_save_time;
//		start();
				
		TimeLine.end();
	}
	
	static public void start() {
		running = true;
		Thread t = new Thread( thread );
		t.setName(SaveThread.class.toString());
		t.start();
	}
	
	static public void close() {
		running = false;
		thread = null;
	}

	
	@Override
	public void run() {
		for(;running;){
			try {
				Thread.sleep(ThreadSleep);
				long time = System.currentTimeMillis();
				
				try {
					CharactersDatabase.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("CharactersDatabase.toTimer 에러");
					lineage.share.System.println(e);
				}
				
				// 캐릭 저장할때 상점도 같이 저장되게 해놓을까요? 음 그럼 훨씬 더 나을까요? 어때요 스무디님 생각은 혹시나 서버뻗을때 생각해서? 해두면 좋을거같아여 네 그러져 ㅋ
				
				try {
					PersnalShopDatabase.toSave();
				} catch (Exception e) {
					lineage.share.System.println("PersnalShopDatabase.toSave 에러");
					lineage.share.System.println(e);
				}
			} catch (Exception e) {
				lineage.share.System.printf("lineage.thread.SaveThread.run()\r\n : %s\r\n", e.toString());
			}
		}		
	}
}
