package lineage.network.packet.client;

import jsn_soft.SpeedHack;
import jsn_soft.MovingHackDefense;
import lineage.network.packet.BasePacket;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.object.instance.PcInstance;

public class C_ObjectMoving extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_ObjectMoving(data, length);
		else
			((C_ObjectMoving)bp).clone(data, length);
		return bp;
	}
	
	public C_ObjectMoving(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		// 버그 방지.
		if (pc == null || pc.isDead() || !isRead(5) || pc.isWorldDelete())
			return this;

		int locx = readH();
		int locy = readH();
		int heading = readC();
		/*
		 * 3.80 이상부터 아래 비트연산이 필요함. : 2진수로 000 3자리는 어떤용도인지 모르고 나머지 자리가 방향을 타나내고 잇엇음.
		 */
		if (Lineage.server_version >= 380) {
			heading = ((heading << 3) & 255) >> 3;
			heading -= 16;
		}

		switch (heading) {
		case 0:
			locy--;
			break;
		case 1:
			locx++;
			locy--;
			break;
		case 2:
			locx++;
			break;
		case 3:
			locx++;
			locy++;
			break;
		case 4:
			locy++;
			break;
		case 5:
			locx--;
			locy++;
			break;
		case 6:
			locx--;
			break;
		case 7:
			locx--;
			locy--;
			break;
		default:
			heading = 0;
			locy--;
			break;
		}
		
//		pc.isActionCheck(true);
		pc.resetAutoAttack();

		if (MovingHackDefense.HackDefense(pc, locx, locy, pc.getMap()))
			pc.toMoving(locx, locy, heading);
		if (SpeedHack.isAutoMoveTime(pc))
			pc.toMoving(locx, locy, heading);
		else if (pc.isTeleport()) {
			pc.setTeleport(false);
			pc.toMoving(locx, locy, heading);
		}
		
		if (pc.isTeleport()) {
			pc.setTeleport(false);
		}
		return this;
	}
}
