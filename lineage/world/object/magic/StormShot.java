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

public class StormShot extends Magic {

	public StormShot(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new StormShot(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		Character cha = (Character) o;
		o.setBuffStormShot(true);
		cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 5);
		cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - 1);
		toBuffUpdate(o);
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
		Character cha = (Character) o;
		o.setBuffStormShot(false);
		cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 5);
		cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + 1);
		ChattingController.toChatting(o, "스톰 샷이 종료 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	@Override
	public void toBuff(object o) {
		if (getTime() == Lineage.buff_magic_time_min)
			ChattingController.toChatting(o, "스톰 샷: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
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
			if(SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, o, skill, false, false)){
				// 중복되지않게 다른 버프 제거.
				// 블레스 웨폰
				BuffController.remove(o, BlessWeapon.class);
				// 파이어 웨폰
				BuffController.remove(o, FireWeapon.class);
				// 윈드 샷
				BuffController.remove(o, WindShot.class);
				// 브레스 오브 파이어
				BuffController.remove(o, BlessOfFire.class);
				// 버닝 웨폰
				BuffController.remove(o, BurningWeapon.class);
				//아이오브스톰
				BuffController.remove(o, EyeOfStorm.class);
				
				// 버프 등록
				BuffController.append(o, StormShot.clone(BuffController.getPool(StormShot.class), skill, skill.getBuffDuration()));
			}
		}
	}
	
	static public void onBuff(object o, Skill skill) {
		BuffController.remove(o, BlessWeapon.class);
		// 파이어 웨폰
		BuffController.remove(o, FireWeapon.class);
		// 윈드 샷
		BuffController.remove(o, WindShot.class);
		// 브레스 오브 파이어
		BuffController.remove(o, BlessOfFire.class);
		// 버닝 웨폰
		BuffController.remove(o, BurningWeapon.class);
		//아이오브스톰
		BuffController.remove(o, EyeOfStorm.class);
		
		// 버프 등록
		BuffController.append(o, StormShot.clone(BuffController.getPool(StormShot.class), skill, skill.getBuffDuration()));
	}
	
}
