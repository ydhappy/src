package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_BuffElf;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class BurningWeapon extends Magic {

	public BurningWeapon(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new BurningWeapon(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setBuffBurningWeapon(true);
		if (o instanceof Character) {
			Character target = (Character) o;
			target.setDynamicAddDmg(target.getDynamicAddDmg() + 6);
			target.setDynamicAddHit(target.getDynamicAddHit() + 3);
			toBuffUpdate(o);
		}

	}	
	
	@Override
	public void toBuffUpdate(object o) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		o.setBuffBurningWeapon(false);
		if (o instanceof Character) {
			Character target = (Character) o;
			target.setDynamicAddDmg(target.getDynamicAddDmg() - 6);
			target.setDynamicAddHit(target.getDynamicAddHit() - 3);
			ChattingController.toChatting(o, "버닝 웨폰이 종료 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
	
	static public void init(Character cha, Skill skill) {
		// 처리
		if (cha != null) {
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha,
					Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);

			if (SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, cha, skill, false, false))
				onBuff(cha, skill);
		}
	}
	
	static public void onBuff(object o, Skill skill) {
		// 파이어 웨폰
		BuffController.remove(o, FireWeapon.class);
		//스톰샷
		BuffController.remove(o, StormShot.class);
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		// 버프 등록
		BuffController.append(o,
				BurningWeapon.clone(BuffController.getPool(BurningWeapon.class), skill, skill.getBuffDuration()));
		//ChattingController.toChatting(o, "버닝웨폰: 근거리 대미지+6, 근거리 명중+3", Lineage.CHATTING_MODE_MESSAGE);
	}

}