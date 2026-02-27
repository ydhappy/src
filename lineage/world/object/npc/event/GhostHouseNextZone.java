package lineage.world.object.npc.event;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.util.Util;
import lineage.world.controller.GhostHouseController;
import lineage.world.object.object;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.PcInstance;

public class GhostHouseNextZone extends BackgroundInstance {

	@Override
	public void toMoving(object o) {
		// 좌표내에 없다면 무시.
		if(Util.isDistance(this, o, 1) == false)
			return;
		if(o instanceof PcInstance == false)
			return;
		//
		toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), this, 0), false);
		//
		GhostHouseController.toNextTeleport((PcInstance)o);
	}

}
