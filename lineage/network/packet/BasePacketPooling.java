package lineage.network.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.Main;
import lineage.share.Lineage;
import lineage.share.TimeLine;

public final class BasePacketPooling {

	// 동적 생성된 패킷처리용 객체 담을 용도.
	static private Map<String, List<BasePacket>> pool;
	
	static public void init(){
		TimeLine.start("BasePacketPooling..");
		
		pool = new HashMap<String, List<BasePacket>>();
		
		TimeLine.end();
	}
	
	/**
	 * 풀목록에서 찾아서 리턴.
	 * @param c
	 * @return
	 */
	static public BasePacket getPool(Class<?> c){
		if (!Lineage.memory_recycle) {
			return null;
		}
		
		synchronized (pool) {
//			lineage.share.System.printf("pool : %d\r\n", pool.size());
//			lineage.share.System.printf("pool : %d\r\n", pool.size());
			return find(c);
		}
	}
	
//		synchronized (pool) {
//			lineage.share.System.printf("pool : %d\r\n", pool.size());
//			lineage.share.System.printf("pool : %d\r\n", pool.size());
//			return find(c);
//		}
//	}
	
	
	static public void setPool(BasePacket bp) {
		if(!Lineage.memory_recycle)
			return;
		synchronized (pool) {
			if(Main.running) {
				List<BasePacket> list = pool.get(bp.getClass().toString());
				if(list == null) {
					list = new ArrayList<BasePacket>();
					pool.put(bp.getClass().toString(), list);
				}
				synchronized (list) {
					if(!list.contains(bp))
						list.add(bp);
					else
						bp = null;
				}
			} else
				bp = null;
			
//			lineage.share.System.printf("pool : %d\r\n", pool.size());
		}
	}
	
	/**
	 * 해당하는 객체 찾아서 리턴.
	 * @param c
	 * @return
	 */
	static private BasePacket find(Class<?> c){
		if(Lineage.memory_recycle) {
			List<BasePacket> list = pool.get(c.toString());
			if(list == null) {
				list = new ArrayList<BasePacket>();
				pool.put(c.toString(), list);
			}
			synchronized (list) {
				if(list.size() > 0) {
					BasePacket bp = list.get(list.size()-1);
					list.remove(list.size()-1);
					return bp;
				}
			}
		}
		return null;
	}
	
	static public int getPoolSize() {
		int size = 0;
		for(String key : pool.keySet())
			size += pool.get(key).size();
		return size;
	}
	
}
