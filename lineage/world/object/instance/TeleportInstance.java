package lineage.world.object.instance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.database.Npc;
import lineage.bean.database.NpcTeleport;
import lineage.database.NpcTeleportDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.util.Util;
import lineage.world.controller.DungeonController;
import lineage.world.object.object;
import lineage.world.object.magic.Teleport;

public class TeleportInstance extends object {
	
	protected Npc npc;
	protected Map<Integer, List<NpcTeleport>> list;
	
	public TeleportInstance(Npc npc){
		this.npc = npc;
		list = new HashMap<Integer, List<NpcTeleport>>();
		// 텔레포트 정보 추출.
		NpcTeleportDatabase.find(npc.getName(), list);
	}

	private NpcTeleport find(String action){
		for(Integer idx : list.keySet()){
			for(NpcTeleport nt : list.get(idx)){
				if(nt.getAction().equals(action))
					return nt;
			}
		}
		return null;
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		NpcTeleport nt = find(action);
		if(nt != null){
			//
			if(DungeonController.isDungeonPartTime(pc, nt.getMap()) && !DungeonController.toDungeonPartTime(pc, nt.getMap(), true)) {
				Teleport.unLock(pc, false);
				return;
			}
			//
			int x, y, map = 0;

			if (nt.isRandomLoc()) {
				lineage.bean.database.NpcTeleport.Location l = null;

				// 처리
				l = nt.getListGoto().get(Util.random(0, nt.getListGoto().size() - 1));
				x = l.x;
				y = l.y;
				map = l.map;
			} else {
				x = nt.getX();
				y = nt.getY();
				map = nt.getMap();
			}

			if (pc.getInventory().isAden(nt.getPrice(), true))
				pc.toPotal(x, y, map);
			else
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));

		}
	}
	
	@Override
	public Npc getNpc() {
		return npc;
	}
}
