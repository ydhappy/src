package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemWeaponInstance;

public class ShadowFang extends Magic {
	
	public ShadowFang(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new ShadowFang(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		o.setBuffShadowFang(true);
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o){
		o.setBuffShadowFang(false);
	}

	static public void init(Character cha, Skill skill, long object_id){
		// 타겟 찾기
		object o = cha.getInventory().value(object_id);
		if(object_id == cha.getObjectId())
			o = cha;
		// 처리
		if(o != null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
			if(SkillController.isMagic(cha, skill, true) && o instanceof ItemWeaponInstance) {
				// 인첸트 웨폰
				BuffController.remove(o, EnchantWeapon.class);
				// 블레스 웨폰
				BuffController.remove(o, BlessWeapon.class);
				// 인첸트 베놈
				BuffController.remove(o, EnchantVenom.class);
				// 버프 등록
				BuffController.append(o, ShadowFang.clone(BuffController.getPool(ShadowFang.class), skill, skill.getBuffDuration()));
				
				// 패킷 처리
				cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			}
		}
	}

}
