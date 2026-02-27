package lineage.network.packet.server;

import jsn_soft.SpeedHack;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.monster.TrapArrow;

public class S_ObjectAttack extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, object me,
			object target, int action, int dmg, int effect, boolean bow,
			boolean visual, int x, int y, boolean effect_target) {
		if (bp == null)
			bp = new S_ObjectAttack(me, target, action, dmg, effect, bow, visual, x, y, effect_target);
		else
			((S_ObjectAttack) bp).clone(me, target, action, dmg, effect, bow, visual, x, y, effect_target);
		return bp;
	}

	static synchronized public BasePacket clone(BasePacket bp, object me,
			int action, int effect, int x, int y) {
		if (bp == null)
			bp = new S_ObjectAttack(me, action, effect, x, y);
		else
			((S_ObjectAttack) bp).clone(me, action, effect, x, y);
		return bp;
	}

	public S_ObjectAttack(object me, object target, int action, int dmg, int effect, boolean bow, boolean visual, int x, int y, boolean effect_target) {
		clone(me, target, action, dmg, effect, bow, visual, x, y, effect_target);
	}

	public S_ObjectAttack(object me, int action, int effect, int x, int y) {
		clone(me, action, effect, x, y);
	}

	public void clone(object me, int action, int effect, int x, int y) {
//		if (me instanceof PcInstance) {
//			PcInstance pc = (PcInstance) me;
//			pc.isFrameSpeed(action);
//		}
		clear();

		writeC(Opcodes.S_OPCODE_AttackPacket);
		writeC(action); // 공격 모션 부분
		writeD(me.getObjectId()); // 유저 오브젝
		writeD(0); // 대상 객체가 없기때문에 0
		if (Lineage.server_version <= 300)
			writeC(0); // 들어간 데미지 부분
		else
			writeH(0); // 들어간 데미지 부분
		writeC(Util.calcheading(me, x, y)); // 방향

		writeD(ServerDatabase.nextEtcObjId());
		writeH(effect);
		writeC(0x06); // 타켓지정:6, 범위&타켓지정:8, 범위:0
		writeH(me.getX());
		writeH(me.getY());
		writeH(x);
		writeH(y);
		writeH(0);
	}

	public void clone(object me, object target, int action, int dmg, int effect, boolean bow, boolean visual, int x, int y, boolean effect_target) {
		clear();

		writeC(Opcodes.S_OPCODE_AttackPacket);
		writeC(action); // 공격 모션 부분
		writeD(me.getObjectId()); // 유저 오브젝
		if (target != null)
			writeD(target.getObjectId()); // 타켓 오브젝
		else
			writeD(0);
		if (Lineage.server_version <= 300) {
			if (target instanceof PcInstance && !(me instanceof PcInstance)) {
				PcInstance pc = (PcInstance) target;
				if (System.currentTimeMillis() - pc.getAttackTime() < 0
						|| System.currentTimeMillis() - pc.getSkillTime() < 0) {
					dmg = 0;
				}
			} else if (target instanceof PcInstance && (me instanceof PcInstance)) {
				if (!World.isAttack(me, target)) {
					dmg = 0;
				}
			}
			
			 writeC(dmg);
		} else {
			writeH(dmg); // 들어간 데미지 부분 피격모션 담당부분
		}
		// 방향
		if (target == null)
			writeC(me.getHeading());//writeC(Util.calcheading(me, x, y));
		else
			writeC(Util.calcheading(me, target.getX(), target.getY()));

		if (bow) {
			toBow(me, target, effect, visual, x, y);
		} else {
			toWeapon(me, target, effect, x, y, effect_target);
		}
	}

	private void toBow(object me, object target, int effect, boolean visual, int x, int y) {
		if (visual) {
			writeD(ServerDatabase.nextEtcObjId());
			writeH(effect);
			writeC(0x00);
			writeH(me.getX());
			writeH(me.getY());

			if (target != null) {
				writeH(target.getX());
				writeH(target.getY());
			} else {
				// 장로 변신중일 경우 좌표를 지정한 위치로 넣기.
				if (me.getGfx() == 32) {
					writeH(x);
					writeH(y);
				} else {
					// 일팩에서 가져온 소스.
					final byte HEADING_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
					final byte HEADING_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };
					float disX = Math.abs(me.getX() - x);
					float disY = Math.abs(me.getY() - y);
					float dis = Math.max(disX, disY);
					float avgX = 0;
					float avgY = 0;

					if (dis == 0) {
						avgX = HEADING_X[me.getHeading()];
						avgY = HEADING_Y[me.getHeading()];
					} else {
						avgX = disX / dis;
						avgY = disY / dis;
					}

					int addX = (int) Math.floor((avgX * (me instanceof TrapArrow ? 1 : 15)) + 0.59f);
					int addY = (int) Math.floor((avgY * (me instanceof TrapArrow ? 1 : 15)) + 0.59f);

					if (me.getX() > x) {
						addX *= -1;
					}
					if (me.getY() > y) {
						addY *= -1;
					}

					writeH(x + addX);
					writeH(y + addY);
				}
			}
			writeH(0);
			writeC(0);
		} else {
			writeD(0);
			writeC(0);
		}
	}

	private void toWeapon(object me, object target, int effect, int x, int y, boolean effect_target) {
		//
		x = target!=null ? target.getX() : x;
		y = target!=null ? target.getY() : y;
		//
		if (effect > 0) {
			if (me.isCriticalEffect()) {
				writeD(ServerDatabase.nextEtcObjId());
				writeH(effect);
				writeC(0x06); // 타켓지정:6, 범위&타켓지정:8, 범위:0
				writeH(effect_target ? me.getX() : x);
				writeH(effect_target ? me.getY() : y);
				writeH(x);
				writeH(y);
				writeH(0);
			} else {
				writeD(ServerDatabase.nextEtcObjId());
				writeH(effect);
				writeC(0x06); // 타켓지정:6, 범위&타켓지정:8, 범위:0
				writeH(effect_target ? me.getX() : x);
				writeH(effect_target ? me.getY() : y);
				writeH(x);
				writeH(y);
				writeH(0);
			}
		} else {
			writeD(0);
			writeC(0); // 0:none 2:크로우 4:이도류 0x08:CounterMirror
		}
	}

}
