package lineage.world.object.magic;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.MonsterSkill;
import lineage.bean.database.Skill;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAttackMagic;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.DamageController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class Lightning {

	/**
	 * 사용자용 함수.
	 * @param cha
	 * @param skill
	 * @param object_id
	 * @param x
	 * @param y
	 */
	static public void init(Character cha, Skill skill, long object_id, int x, int y){
		object o = cha.findInsideList( object_id );
		if(o!=null && Util.isDistance(cha, o, 10) && SkillController.isMagic(cha, skill, true)){
			if(Lineage.server_version < 160){
				x = o.getX();
				y = o.getY();
			}
			toBuff(cha, skill, o, 18, skill.getRange(), skill.getCastGfx(), 0, null, x, y);
		}
	}
	
	/**
	 * 몬스터용 함수.
	 * @param mi
	 * @param skill
	 * @param o
	 * @param action
	 */
	static public void init(MonsterInstance mi, MonsterSkill ms, object o, int action, int effect){
		if(SkillController.isMagic(mi, ms, true) && ms.getSkill()!=null)
			toBuff(mi, ms.getSkill(), o, action, ms.getRange(), effect, (int)Util.random(ms.getMindmg()* Lineage.light, ms.getMaxdmg() * Lineage.light), null, o.getX(), o.getY());
	}
	
	/**
	 * 중복코드 방지용.
	 *  : 마법주문서 (라이트닝) 에서도 사용중.
	 * @param cha
	 * @param skill
	 * @param o
	 * @param action
	 */
	static public void toBuff(Character cha, Skill skill, object o, int action, int area, int effect, int alpha_dmg, List<object> list, int x, int y){
		if(list == null)
			list = new ArrayList<object>();
		// 데미지 처리
		int dmg = SkillController.getDamage(cha, o, o, skill, alpha_dmg, skill.getElement());
		DamageController.toDamage(cha, o, dmg, 2);
		if(dmg > 0)
			list.add(o);
		// 주변객체 데미지 처리
		for(object oo : cha.getInsideList(true)){
//		   PcInstance pc = (PcInstance)oo;
//		   if(cha.getClanId() > 0 &&pc.getClanId() > 0 &&cha.getClanId() != pc.getClanId()) {
			
			if(o.getObjectId()!=oo.getObjectId() && Util.isDistance(o, oo, area)){
				int oo_dmg = SkillController.getDamage(cha, o, oo, skill, alpha_dmg, skill.getElement());
				DamageController.toDamage(cha, oo, oo_dmg, 2);
				if(oo_dmg > 0)
					list.add(oo);
//				}
			}
		}
		
		// 패킷 처리.
		cha.setHeading(Util.calcheading(cha, x, y));
		cha.toSender(S_ObjectAttackMagic.clone(BasePacketPooling.getPool(S_ObjectAttackMagic.class), cha, o, list, false, action, dmg, effect, x, y), cha instanceof PcInstance);
		list.clear();
		list = null;
	}
}
