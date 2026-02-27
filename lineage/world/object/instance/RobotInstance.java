package lineage.world.object.instance;

import java.sql.Connection;

import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.world.World;
import lineage.world.controller.InventoryController;

public class RobotInstance extends PcInstance {

	public RobotInstance() {
		super(null);
	}
	
	@Override
	public void toTimer(long time){ }

	@Override
	public void toWorldJoin() {
		InventoryController.toWorldJoin(this);
		
		toTeleport(getX(), getY(), getMap(), false);
	}
	
	@Override
	public void toWorldOut() {
		World.remove(this);
		clearList(true);
		
		InventoryController.toWorldOut(this);
	}
	
	@Override
	public void toSave(Connection con) { }

	@Override
	public void toSender(BasePacket bp) {
		BasePacketPooling.setPool(bp);
	}

}
