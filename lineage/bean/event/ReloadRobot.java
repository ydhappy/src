package lineage.bean.event;

import lineage.world.controller.RobotController;


public class ReloadRobot implements Event {
	
	static synchronized public Event clone(Event e){
		if(e == null)
			e = new ReloadRobot();
		return e;
	}
	
	static private boolean is;
	
	@Override
	public void init() {
		//
		if(is)
			return;
		//
		try {
			is = true;
			RobotController.close();
			RobotController.init();
			is = false;
		} catch (Exception e) {
			lineage.share.System.println("lineage.bean.event.ReloadRobot");
			lineage.share.System.println(e);
		}
	}

	@Override
	public void close() {
		// 할거 없음..
	}
	
}
