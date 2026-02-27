package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.share.Lineage;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;

public class Howl extends Tornado {

	/**
	 * 사용자 가 사용하는 함수.
	 * @param cha
	 * @param skill
	 */
	static public void init(Character cha, Skill skill) {
		if(SkillController.isMagic(cha, skill, true))
			Tornado.toBuff(cha, skill, 19, skill.getRange(), skill.getCastGfx(), 0);
		else
			// 모션 취하기.
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
	}

}
