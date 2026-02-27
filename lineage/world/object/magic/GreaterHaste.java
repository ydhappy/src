package lineage.world.object.magic;

import lineage.bean.database.ItemSetoption;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectSpeed;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class GreaterHaste extends Magic {

	public GreaterHaste(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new Haste(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		o.setSpeed(1);
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, S_ObjectSpeed.HASTE, o.getSpeed(), getTime()), true);
		// \f1갑자기 빠르게 움직입니다.
		o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 184));
	}

	@Override
	public void toBuffUpdate(object o) {
		o.setSpeed(1);
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, S_ObjectSpeed.HASTE, o.getSpeed(), getTime()), true);
		// \f1다리에 새 힘이 솟습니다.
		o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 183));
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o){
		if(o.isWorldDelete())
			return;
		o.setSpeed(0);
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, S_ObjectSpeed.HASTE, o.getSpeed(), 0), true);
		// \f1느려지는 것을 느낍니다.
		o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 185));
	}
	
	@Override
	public void toBuff(object o) {
		if (getTime() == 1)
			o.speedCheck = System.currentTimeMillis() + 2000;
	}

	@Override
	public void setTime(int time) {
		if(time>0 && Lineage.skill_Haste_update) {
			time = getTime() + time;
			// 60분까지만 축적되게.
			if((60*60) < time)
				time = 60 * 60;
			//
			super.setTime( time );
		} else {
			super.setTime(time);
		}
	}
	
	@Override
	public boolean isBuff(object o, long time) {
		//
		if(o!=null && o.getInventory()!=null && time_end>0) {
			for(ItemSetoption iso : o.getInventory().getSetitemList()) {
				if(iso.isHaste()) {
					time_end += 1000;
					break;
				}
			}
		}
		//
		return super.isBuff(o, time);
	}
	
	static public void init(Character cha, Skill skill, long object_id){
		// 초기화
		object o = null;
		// 타겟 찾기
		if(object_id == cha.getObjectId())
			o = cha;
		//else
		//	o = cha.findInsideList( object_id );
		// 처리
		if(o != null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
			if(SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, o, skill, false, false))
				onBuff(o, skill);
		}
	}
	
	static public void init(Character cha, int time){
		BuffController.append(cha, GreaterHaste.clone(BuffController.getPool(GreaterHaste.class), SkillDatabase.find(6, 2), time));
	}
	
	static public void onBuff(object o, Skill skill){
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		
		if(o.getSpeed() == 2){
			// 슬로우 제거.
			BuffController.remove(o, Slow.class);
			return;
		}
		
//		// 헤이스트가 적용된 셋트옵션아이템을 사용중일경우 무시.
//		for(ItemSetoption iso : o.getInventory().getSetitemList())
//			if(iso.isHaste())
//				return;
		
		// 헤이스트 적용.
		BuffController.append(o, GreaterHaste.clone(BuffController.getPool(GreaterHaste.class), skill, skill.getBuffDuration()));
	}
	
}
