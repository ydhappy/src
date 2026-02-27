package lineage.world.object.npc.event;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.util.Util;
import lineage.world.controller.GhostHouseController;
import lineage.world.object.object;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.npc.background.door.Door;

public class GhostHouseDoorButton extends BackgroundInstance {

	@Override
	public void toMoving(object o) {
		// 좌표내에 없다면 무시.
		if(Util.isDistance(this, o, 1) == false)
			return;
		if(o instanceof PcInstance == false)
			return;
		// 문 찾기.
		Door d = null;
		for(object obj : getInsideList(true)) {
			if(obj instanceof Door && Util.isDistance(this, obj, 3) && obj.isDoorClose()) {
				d = (Door)obj;
				break;
			}
		}
		if(d == null)
			return;
		// 랜덤 버프.
		GhostHouseController.toRandomBuff((PcInstance)o, false);
		// 모드 변경과 전송.
		toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), this, 28), false);
		toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), this, 29), false);
		// 문 열기.
		d.toOpen();
		d.toSend();
	}
	
}
