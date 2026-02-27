package sp.magic;

import lineage.bean.database.Skill;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class Berserks extends lineage.world.object.magic.Berserks {

	public Berserks(Skill skill) {
		super(skill);
	}
	
	static public void init(Character cha, Skill skill, long object_id){
		// 초기화
		object o = null;
		// 타겟 찾기
		if(object_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList( object_id );
		// 처리
		if(o != null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
			// 확인.
			if(Util.isDistance(cha, o, 15) && SkillController.isMagic(cha, skill, true)){
				// 혈맹원, 파티원에게만 시전.
				boolean clan = cha.getClanId()>0 && cha.getClanId()==o.getClanId();
				boolean party = cha.getPartyId()>0 && cha.getPartyId()==o.getPartyId();
				if(cha.getObjectId()==o.getObjectId() || clan || party) {
					// 이팩트 표현.
					o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
					// 적용.
					BuffController.append(o, Berserks.clone(BuffController.getPool(Berserks.class), skill, skill.getBuffDuration()));
				}
			}
		}
	}

}
