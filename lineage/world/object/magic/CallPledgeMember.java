package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.Clan;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.network.packet.server.S_ObjectAction;
import lineage.share.Lineage;
import lineage.world.controller.ClanController;
import lineage.world.controller.LocationController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.instance.PcInstance;

public class CallPledgeMember {

	static public void init(Character cha, Skill skill, String name){
		// 처리
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		//
		if(SkillController.isMagic(cha, skill, true)){
			//
			Clan c = ClanController.find(cha.getClanName());
			PcInstance pc = c.find(name);
			if(isCall(skill, cha, pc, true)) {
				//
				c.appendCallList( pc );
				// 729 군주님께서 부르십니다. 소환에 응하시겠습니까? (y/N)
				pc.toSender(S_MessageYesNo.clone(BasePacketPooling.getPool(S_MessageYesNo.class), 729));
			}
		}
	}
	
	/**
	 * 콜클랜호출이 가능한지 체크하는 메서드.
	 * 런클랜에서도 호출해서 사용중.
	 * @param royal		: 콜 시전한 군주
	 * @param target	: 대상.
	 * @return			: 가능여부.
	 */
	static synchronized public boolean isCall(Skill skill, Character royal, PcInstance target, boolean message) {
		// 공성전중 콜클렌이 가능한지 확인.
		if(SkillController.isKingdomZoneMagic(skill, royal) == false){
			// \f1여기에서는 사용할 수 없습니다.
			if(message)
				royal.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 563));
			return false;
		}
		// 콜클렌 가능 지역 및 맵인지 확인.(랜덤텔레포트 가능한 맵 체크로 함.)
		if(LocationController.isTeleportZone(royal, false, false)==false || isMap(royal.getMap())==false) {
			if(message)
				royal.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 563));
			return false;
		}
		//
		if(target!=null && target.getObjectId()!=royal.getObjectId()) {
			//
			if(SkillController.isKingdomZoneMagic(skill, target) == false){
				// \f1여기에서는 사용할 수 없습니다.
				if(message)
					royal.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 563));
				return false;
			}
			//
			if(LocationController.isTeleportZone(target, false, false)==false || isMap(target.getMap())==false) {
				if(message)
					royal.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 563));
				return false;
			}
			//
			if(target.isBuffDesperado())
				return false;
			
		}
		return true;
	}
	
	/**
	 * 콜클렌 및 런클랜 시전시 호출해서 시전가능한 맵인지 확인함.
	 * @param map
	 * @return
	 */
	static private boolean isMap(int map) {
		for(int m : Lineage.notPledgeMemberMap) {
			if(m == map)
				return false;
		}
		return true;
	}
}
