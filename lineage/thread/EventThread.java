package lineage.thread;

import java.util.ArrayList;
import java.util.List;

import lineage.Main;
import lineage.bean.event.Ai;
import lineage.bean.event.Event;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;

public final class EventThread implements Runnable {

	static private List<EventThread> threadList;
	// 쓰레드동작 여부
	static private boolean running;
	// 메모리 재사용을 위해
	static private List<Event> pool;
	//
	// 처리할 이벤트 목록
	private List<Event> list;
	// 실제 처리되는 이벤트 목록
	private List<Event> run;
	private Object sync = new Object();

	/**
	 * 초기화 처리 함수.
	 */
	static public void init(){
		TimeLine.start("EventThread..");
		
		pool = new ArrayList<Event>();
		threadList = new ArrayList<EventThread>();
		start();
		
		TimeLine.end();
	}
	
	/**
	 * 쓰레드 활성화 함수.
	 */
	static private void start(){
		running = true;
		for(int i=0 ; i<Lineage.thread_event ; ++i){
			EventThread et = new EventThread();
			Thread t = new Thread(et);
			t.setName( String.format("[%d] %s", i, EventThread.class.toString()) );
			t.start();
			threadList.add( et );
		}
	}
	
	/**
	 * 쓰레드 종료처리 함수.
	 */
	static public void close() {
		running = false;
		for(EventThread t : threadList)
			t.exit();
		threadList.clear();
	}
	
	static public void append(Event e) {
		if(!running)
			return;
		
		// 작업량 확인.
		int min_cnt = 0;
		EventThread et = null;
		// 작업량 적은쪽 찾기.
		for(EventThread t : threadList){
			int count = t.getLength();
			if(et==null || count<=min_cnt){
				min_cnt = count;
				et = t;
			}
		}
		// 요청.
		if(et == null)
			threadList.get(0).add(e);
		else
			et.add(e);
	}
	
	static private void clearPool() {
		TimeLine.start("EventThread 에서 Pool 초과로 메모리 정리 중..");
		
		// 풀 전체 제거.
		pool.clear();
		
		TimeLine.end();
	}
	
	static public void setPool(Event e) {
		if(Lineage.memory_recycle && Lineage.pool_eventthread && running) {
			synchronized (pool) {
				if(Util.isPoolAppend(pool) && pool.contains(e)==false) {
					pool.add(e);
				} else {
					e = null;
					clearPool();
				}
			}
		} else
			e = null;
	}
	
	/**
	 * 풀에 저장된거 재사용을위해 사용.
	 * 있으면 리턴.
	 * @param c
	 * @return
	 */
	static public Event getPool(Class<?> c){
		if(Lineage.memory_recycle && Lineage.pool_eventthread) {
			synchronized (pool) {
				Event e = findPool(c);
				if(e != null)
					pool.remove(e);
				return e;
			}
		} else
			return null;
	}
	
	/**
	 * 찾는 객체 꺼내기.
	 *  : 반드시 이것으루 호출하는 코드내에서는 synchronized (pool) 정의되어 있어야 안전하다.
	 * @param c
	 * @return
	 */
	static private Event findPool(Class<?> c){
		for(Event e : pool){
			if(e.getClass().toString().equals(c.toString()))
				return e;
		}
		return null;
	}
	
	
	

	public EventThread() {
		run = new ArrayList<Event>();
		list = new ArrayList<Event>();
	}
	
	public void add(Event e){
		synchronized (list) {
			list.add(e);
		}
		synchronized (sync) {
			sync.notifyAll();
		}
	}
	
	private int getLength() {
		synchronized (list) {
			return run.size() + list.size();
		}
	}
	
	public void exit() {
		synchronized (sync) {
			sync.notifyAll();
		}
	}
	
	@Override
	public void run(){
		for(;running;){
			try {
				synchronized (sync) {
					sync.wait();
				}
				// 이벤트 처리요청된거 옴기기
				synchronized (list) {
					run.addAll(list);
					list.clear();
				}
				// 실제 이벤트 처리 구간.
				for(Event e : run) {
					try {
						e.init();
					} catch (Exception e2) {
						lineage.share.System.printf("lineage.thread.EventThread.run()\r\n : %s\r\n : %s\r\n", e.toString(), e2.toString());
					}
					e.close();
					//
					if(e instanceof Ai) {
						AiThread.setPool((Ai)e);
					} else {
						if(Lineage.memory_recycle && Lineage.pool_eventthread) {
							// 재사용을위해 풀에 다시 넣기.
							synchronized (pool) {
								if(Main.running)
									pool.add( e );
							}
						}
					}
				}
				if(Util.isPoolAppend(pool) == false)
					clearPool();
				//
				run.clear();
			} catch (Exception e) {
				lineage.share.System.printf("lineage.thread.EventThread.run()\r\n : %s\r\n", e.toString());
			}
		}
	}
	
	static public int getListSize(){
		int length = 0;
		for(EventThread t : threadList)
			length += t.list.size();
		return length;
	}
	
	static public int getRunSize(){
		int length = 0;
		for(EventThread t : threadList)
			length += t.run.size();
		return length;
	}
	
	static public int getPoolSize(){
		return pool.size();
	}
	
}
