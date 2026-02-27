package lineage.bean.event;

import lineage.world.controller.SpServerController;
import lineage.world.object.object;


public class SpServerChatting implements Event {
	
	static synchronized public Event clone(Event e, object o, String msg){
		if(e == null)
			e = new SpServerChatting();
		((SpServerChatting)e).o = o;
		((SpServerChatting)e).msg = msg;
		return e;
	}
	
	private object o;
	private String msg;
	
	@Override
	public void init() {
		SpServerController.toChattingGlobal(o, msg);
	}

	@Override
	public void close() {
		// 할거 없음..
	}

}
