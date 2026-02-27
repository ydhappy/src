package jsn_soft;

import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class Swap_Plugin {
	static public String action;
	
	public static String getAction() {
		return action;
	}

	public static void setAction(String action) {
		Swap_Plugin.action = action;
	}

	static public boolean toChatting(object o, String msg) {
		if (o == null)
			return false;
		else if (!(o instanceof PcInstance))
			return false;
		PcInstance pc = (PcInstance) o;
		try {
			if (!pc.isInsertSwap)
				return false;
			if (getAction() == null)
				return false;
			if (getAction().equalsIgnoreCase("save swap")) {
				setAction(null);
				pc.insertSwap(msg);
				return true;
			}
			
		} catch (Exception e) {
			setAction(null);
			pc.isInsertSwap = false;
			return false;
		}
		
		return false;
	}
}
