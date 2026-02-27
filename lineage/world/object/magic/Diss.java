package lineage.world.object.magic;

import lineage.bean.database.MonsterSkill;
import lineage.bean.database.Skill;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAttack;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.DamageController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.MagicDollInstance;
import lineage.world.object.instance.PcInstance;

public class Diss {

	/**
	 * 사용자 용
	 * @param cha
	 * @param skill
	 * @param object_id
	 */
	static public void init(Character cha, Skill skill, long object_id){
		// 타겟 찾기
		object o = cha.findInsideList( object_id );
		if(o!=null && Util.isDistance(cha, o, 10) && SkillController.isMagic(cha, skill, true)){
			if(cha.getSix()==11){
				toBuff(cha, o, skill, 18, skill.getCastGfx(), 10);
			}else{
				toBuff(cha, o, skill, 18, skill.getCastGfx(), 0);
			}
		}
	}
	
	/**
	 * 몬스터용
	 *  : 장로가 사용중.
	 *  : 다크마르가 사용중.
	 * @param cha
	 * @param o
	 * @param skill
	 * @param action
	 */
	static public void init(Character cha, object o, MonsterSkill skill, int action, int effect){
		if(o!=null && SkillController.isMagic(cha, skill, true))
			toBuff(cha, o, skill.getSkill(), action, effect, (int)Util.random(skill.getMindmg()* Lineage.diss, skill.getMaxdmg() * Lineage.diss));
	}
	
	/**
	 * 중복코드 방지용
	 * @param cha
	 * @param o
	 * @param skill
	 * @param action
	 * @param effect
	 */
	static public int toBuff(Character cha, object o, Skill skill, int action, int effect, int alpha_dmg){
		// 데미지 처리
		int dmg = SkillController.getDamage(cha, o, o, skill, alpha_dmg, skill.getElement());
		DamageController.toDamage(cha, o, dmg, 2);
		
		// 패킷 처리
		cha.toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), cha, o, action, dmg, effect, false, false, o.getX(), o.getY(), true), (cha instanceof PcInstance || cha instanceof MagicDollInstance));
		
		return dmg;
	}
}
