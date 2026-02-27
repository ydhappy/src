package lineage.world.object.magic;

import lineage.bean.database.MonsterSkill;
import lineage.bean.database.Skill;
import lineage.bean.lineage.Book;
import lineage.bean.lineage.BuffInterface;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectAdd;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectLock;
import lineage.network.packet.server.S_ObjectMap;
import lineage.share.Lineage;
import lineage.thread.AiThread;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.AgitController;
import lineage.world.controller.BookController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.LocationController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class Teleport extends Magic {

	public Teleport(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new Teleport(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setBuffTeleport(true);
	}

	@Override
	public boolean toBuffStop(object o) {
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		o.setBuffTeleport(false);
	}

	// 텔포 진행도
	static public void init(Character cha, Skill skill, final ClientBasePacket cbp) {
		if (SkillController.isMagic(cha, skill, true)) {
			cha.toSender(new S_ObjectAction(cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
			if (onBuff(cha, cbp, 1, false, false))
				cha.toTeleport(cha.getHomeX(), cha.getHomeY(), cha.getHomeMap(), true);
			else
				unLock(cha, true);
		} else {
			unLock(cha, false);
		}
	}
	
	/**
	 * 몬스터 용.
	 * @param mi
	 * @param o
	 * @param ms
	 * @param action
	 * @param effect
	 * @param check
	 */
	static public void init(MonsterInstance mi, object o, MonsterSkill ms, int action, int effect, boolean check){
		//
		mi.toSender(new S_ObjectAction(mi, action), false);
		//
		if (check && !SkillController.isMagic(mi, ms, true))
			return;
		//
		if (effect > 0)
			mi.toSender(new S_ObjectEffect(mi, effect), false);
		//
		int x = o.getX();
		int y = o.getY();
		// 일정 범위
		int goCount = 0;
		do {
			int cx = Util.random(0,1) == 0 ? x-1 : x+1;
			int cy = Util.random(0,1) == 0 ? y-1 : y+1;
			if (World.isSafetyZone(x, y, o.getMap())) {
				cx = x;
				cy = y;
			}
			if (World.isThroughObject(cx, cy, o.getMap(), 0)) {
				x = cx;
				y = cy;
				break;
			}
		} while (goCount++ < 200);
		mi.toTeleport(x, y, o.getMap(), effect == 0);
	}

	static public boolean onBuff(Character cha, final ClientBasePacket cbp, int bress, boolean item, boolean ment) {
		boolean isNight = ServerDatabase.isNight();
		  if (isNight && (cha.getMap() >= 53 && cha.getMap() <= 56)) {
			  ChattingController.toChatting(cha, "기란 감옥은 야간에 무작위 텔레포트가 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			  cha.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
			  return false;
		  }
		// 이반을 통한 좌표 텔레포트.
			if (cha instanceof PcInstance && (bress == 0 || cha.getInventory().isRingOfTeleportControl()
					|| (cha.getClassType() == Lineage.LINEAGE_CLASS_ELF && cha.getLevel() >= 50))) {
				// 패킷이 유효하고 시전이 가능한 맵인지 확인.
				if(cbp.isRead(6) && LocationController.isTeleportVerrYedHoraeZone(cha, true)){
					int map = cbp.readH();
					int x = cbp.readH();
					int y = cbp.readH();
					Book b = BookController.find((PcInstance)cha, x, y, map);
					if(b != null){
						// 요정 50레벨 순간이동일경우 일정확률로 좀 다르게 좌표 이동하기.
						if(!item && !cha.getInventory().isRingOfTeleportControl() && cha.getClassType()==0x02 && Util.random(0, 3)==0){
							int loc = Util.random(10, 30);
							if(loc == 30){
								// 랜덤
								Util.toRndLocation(cha);
							}else if(loc == 10){
								// 지정된 위치로
								cha.setHomeX(x);
								cha.setHomeY(y);
							}else{
								// 일정 범위
								cha.setHomeX(Util.random(x-10, x-10));
								cha.setHomeY(Util.random(y-10, y-10));
							}
						}else{
							cha.setHomeX(x);
							cha.setHomeY(y);
						}
						cha.setHomeMap(map);
						return true;
					}
				}
			}
		
		// 랜덤 텔레포트
		if (LocationController.isTeleportZone(cha, true, ment)) {
			Util.toRndLocation(cha);
			return true;
		}

		return false;
	}
	
	static public void unLock(Character cha, boolean message) {
		//
		if (Lineage.server_version <= 163) {
			if (message)
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 647));
			cha.toSender(new S_ObjectMap(cha));
			cha.toSender(new S_ObjectAdd(cha, cha));
			for (object obj : cha.getInsideList(true))
				cha.toSender(new S_ObjectAdd(obj, cha));

		} else {
			if (message)
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 647));
			cha.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 7));
		}

	}

}