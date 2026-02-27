package lineage.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import lineage.Main;
import lineage.bean.event.Ai;
import lineage.bean.event.DeleteObject;
import lineage.bean.event.Event;
import lineage.share.Common;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.object.object;

public final class AiThread implements Runnable {

	static private List<AiThread> threadList;
	// 쓰레드동작 여부
	static private boolean running;
	
	// 인공지능 처리 목록
	private List<object> list;
	// 실제 처리되는 인공지능 목록
	private List<object> run;
	// 제거 처리해야할 목록
	private List<object> remove;
	// 메모리 재사용을 위해
	private Stack<Event> pool;
	// 동작중인 쓰레드에 고유 값.
	private int uid;

	/**
	 * 초기화 처리 함수.
	 */
	static public void init() {
		TimeLine.start("AiThread..");
		
		threadList = new ArrayList<AiThread>();
		for(int i=0 ; i<Lineage.thread_ai ; ++i)
			threadList.add(new AiThread(i));
		
		TimeLine.end();
	}
	
	/**
	 * 쓰레드 활성화 함수.
	 */
	static public void start() {
		running = true;
		int i = 0;
		for(AiThread at : threadList){
			Thread t = new Thread(at);
			t.setName( String.format("[%d] %s", i++, AiThread.class.toString()) );
			t.start();
		}
	}
	
	/**
	 * 종료 함수
	 */
	static public void close(){
		running = false;
		threadList.clear();
	}
	
	/**
	 * 인공지능 활성화할 객체 등록 함수.
	 * @param o
	 */
	static public void append(object o){
		// 존재 확인.
		boolean is = false;
		for(AiThread t : threadList){
			if(t.contains(o))
				is = true;
		}
		if(is)
			return;
		
		// 작업량 확인.
		int min_cnt = 0;
		AiThread at = null;
		// 작업량 적은쪽 찾기.
		for(AiThread t : threadList){
			int count = t.getLength();
			if(at==null || count<=min_cnt){
				min_cnt = count;
				at = t;
			}
		}
		
		// 요청.
		if(at == null)
			threadList.get(0).add(o);
		else
			at.add(o);
	}
	
	static public void setPool(Ai a) {
		if(Lineage.memory_recycle && Lineage.pool_aithread && Main.running) {
			AiThread at = threadList.get( a.getUid() );
			synchronized (at.pool) {
				if(Util.isPoolAppend(at.pool)/* && at.pool.contains(a)==false*/) {
					at.pool.push(a);
				} else {
					a = null;
					clearPool(at);
				}
			}
		} else
			a = null;
	}
	
	static private void clearPool(AiThread at) {
		TimeLine.start("AiThread("+at.uid+") 에서 Pool 초과로 메모리 정리 중..");
		
		// 풀 전체 제거.
		at.pool.clear();
		
		TimeLine.end();
	}

	public AiThread(int uid) {
		run = new ArrayList<object>();
		list = new ArrayList<object>();
		remove = new ArrayList<object>();
		pool = new Stack<Event>();
		this.uid = uid;
	}
	
	public void add(object o) {
		synchronized (list) {
			list.add(o);
		}
	}
	
	public boolean contains(object o) {
		synchronized (run) {
			return run.contains(o);
		}
	}
	
	private int getLength() {
		synchronized (list) {
			return run.size() + list.size();
		}
	}
	
	private Event getPool() {
		if(Lineage.pool_aithread && running)
			return findPool();
		else
			return null;
	}
	
	private Event findPool() {
		if(Lineage.memory_recycle) {
			synchronized (pool) {
				Event e = null;
				int size = pool.size();
				if(size > 0)
					e = pool.pop();
				return e;
			}
		} else
			return null;
	}
	
	@Override
	public void run() {
		for(long time=0 ; running ;){
			try {
				Thread.sleep(Common.THREAD_SLEEP);
				time = System.currentTimeMillis();
				
				// 등록요청 처리 구간
				synchronized (list) {
					if(list.size() > 0) {
						synchronized (run) {
							run.addAll(list);
						}
						list.clear();
					}
				}

				synchronized (run) {
					// 인공지능 처리 구간
					for(object o : run) {
						// 인공지능에서 제거해야할 경우.
						if(o.getAiStatus()==-1 || o.getAiStatus()==-2)
							remove.add(o);
						// 인공지능 활성화 시간이 되엇을 경우.
						else if(o.isAi(time))
							EventThread.append( Ai.clone(getPool(), o, time, uid) );
					}
				}
				
				// 인공지능 제거 처리 구간
				for(object o : remove) {
					synchronized (run) {
						run.remove(o);
					}
					if(o.getAiStatus() == -1)
						EventThread.append( DeleteObject.clone(EventThread.getPool(DeleteObject.class), o) );
					else {
						if(o.isWorldDelete())
							o.close();
						o = null;
					}
				}
				remove.clear();
			} catch (Exception e) {
				lineage.share.System.printf("%s : run()\r\n", AiThread.class.toString());
				lineage.share.System.println(e.toString());
			}
		}
	}
	
	static public int getListSize() {
		int length = 0;
		for(AiThread t : threadList)
			length += t.list.size();
		return length;
	}
	
	static public int getRunSize() {
		int length = 0;
		for(AiThread t : threadList)
			length += t.run.size();
		return length;
	}
	
	static public int getPoolSize() {
		int length = 0;
		for(AiThread t : threadList)
			length += t.pool.size();
		return length;
	}
	
}
