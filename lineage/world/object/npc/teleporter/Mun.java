package lineage.world.object.npc.teleporter;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.TimerController3;
import lineage.world.object.Character;
import lineage.world.object.instance.NpcInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.TeleportInstance;
						//20181010
public class Mun extends TeleportInstance {
	
	public Mun(Npc npc){
		super(npc);
	}
	
	
	
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		int i = Util.random(0, 2);
		switch(i){
		case 0: pc.toTeleport(32777, 32854, 612, true);break;
		case 1: pc.toTeleport(32786, 32869, 612, true);break;
		case 2: pc.toTeleport(32802, 32858, 612, true);break;
		}
	}
	
	@Override
	public void toClick(Character pc, ClientBasePacket cbp){
		int i = Util.random(0, 2);
		switch(i){
		case 0: pc.toTeleport(32777, 32854, 612, true);break;
		case 1: pc.toTeleport(32786, 32869, 612, true);break;
		case 2: pc.toTeleport(32802, 32858, 612, true);break;
		}
	}
	@Override
	public void toDamage(Character pc, int dmg, int type, Object...opt){
		int i = Util.random(0, 2);
		switch(i){
			case 0: pc.toTeleport(32777, 32854, 612, true);break;
			case 1: pc.toTeleport(32786, 32869, 612, true);break;
			case 2: pc.toTeleport(32802, 32858, 612, true);break;
		}
		dmg = 0;
	}
}
