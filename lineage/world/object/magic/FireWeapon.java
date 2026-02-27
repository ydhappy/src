package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_BuffElf;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class FireWeapon extends Magic {

	public FireWeapon(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new FireWeapon(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setBuffFireWeapon(true);
		if (o instanceof Character) {
			Character target = (Character) o;
			target.setDynamicAddDmg(target.getDynamicAddDmg() + 2);
			target.setDynamicAddHit(target.getDynamicAddHit() + 4);
			toBuffUpdate(o);
		}	
	}
	
	@Override
	public void toBuffUpdate(object o) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		//o.toSender(S_BuffElf.clone(BasePacketPooling.getPool(S_BuffElf.class), 147, skill.getBuffDuration()));
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o){
		o.setBuffFireWeapon(false);
		if (o instanceof Character) {
			Character target = (Character) o;
			target.setDynamicAddDmg(target.getDynamicAddDmg() - 2);
			target.setDynamicAddHit(target.getDynamicAddHit() - 4);
			ChattingController.toChatting(o, "파이어 웨폰 종료", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
	
	static public void init(Character cha, Skill skill, long object_id){
		// 초기화
		object o = null;
		// 타겟 찾기
		if(object_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList( object_id );
		if(o != null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
			if(SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, o, skill, false, false))
				onBuff(o, skill);
		}
	}
	
	static public void onBuff(object o, Skill skill) {
		// 중복되지않게 다른 버프 제거.
		// 블레스 웨폰
		BuffController.remove(o, BlessWeapon.class);
		// 스톰샷
		BuffController.remove(o, StormShot.class);
		// 윈드 샷
		BuffController.remove(o, WindShot.class);
		// 브레스 오브 파이어
		BuffController.remove(o, BlessOfFire.class);
		// 버닝 웨폰
		BuffController.remove(o, BurningWeapon.class);
		
		BuffController.append(o, FireWeapon.clone(BuffController.getPool(FireWeapon.class), skill, skill.getBuffDuration()));
		ChattingController.toChatting(o, "파이어 웨폰: 근거리 대미지+2, 근거리 명중+4", Lineage.CHATTING_MODE_MESSAGE);
	}
	
}
