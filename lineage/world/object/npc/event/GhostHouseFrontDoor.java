package lineage.world.object.npc.event;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Door;
import lineage.world.World;
import lineage.world.object.object;
import lineage.world.object.npc.background.door.Door;

public class GhostHouseFrontDoor extends Door {

	public GhostHouseFrontDoor() {
		setHeading(2);
		setHomeLoc(5);
		setGfx(6351);
		toClose();
	}
	
	@Override
	public void toDoorSend(object o) {
		//
		int x = getX()-1;
		int y = getY();
		//
		for(int i=0,j=0 ; i<getHomeLoc() ; ++i, --j) {
			if(o == null) {
				toSender(S_Door.clone(BasePacketPooling.getPool(S_Door.class), x, y+i, heading, isDoorClose()), false);
				toSender(S_Door.clone(BasePacketPooling.getPool(S_Door.class), x, y+j, heading, isDoorClose()), false);
			} else {
				o.toSender(S_Door.clone(BasePacketPooling.getPool(S_Door.class), x, y+i, heading, isDoorClose()));
				o.toSender(S_Door.clone(BasePacketPooling.getPool(S_Door.class), x, y+j, heading, isDoorClose()));
			}
			// 타일 변경.
			World.set_map(x, y+i, map, isDoorClose() ? 16 : homeTile[0]);
			World.set_map(x-1, y+i, map, isDoorClose() ? 16 : homeTile[1]);
			World.set_map(x, y+j, map, isDoorClose() ? 16 : homeTile[0]);
			World.set_map(x-1, y+j, map, isDoorClose() ? 16 : homeTile[1]);
		}
	}
	
}
