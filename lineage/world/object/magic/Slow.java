package lineage.world.object.magic;

import lineage.bean.database.ItemSetoption;
import lineage.bean.database.MonsterSkill;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectSpeed;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.item.Bravery;
import lineage.world.object.magic.item.Wafer;

public class Slow extends Magic {
	
	public Slow(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new Slow(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		o.setSpeed(2);
		toBuffUpdate(o);
		// 공격당한거 알리기.
		o.toDamage(cha, 0, 2);
	}

	@Override
	public void toBuffUpdate(object o) {
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, S_ObjectSpeed.HASTE, o.getSpeed(), getTime()), true);
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
	}
	
	static public void init(Character cha, Skill skill, long object_id){
		try {
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
			if(Util.isDistance(cha, o, 10) && SkillController.isMagic(cha, skill, true)) {
				if(SkillController.isFigure(cha, o, skill, true, false))
					onBuff(o, skill);
				// 투망상태 해제
				Detection.onBuff(cha);
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 몬스터용
	 * @param cha
	 * @param o
	 * @param skill
	 * @param action
	 */
	static public void init(Character cha, object o, MonsterSkill skill, int action, int effect){
		if(o!=null && SkillController.isMagic(cha, skill, true))
			onBuff(o, skill.getSkill());
	}
	
	static public void init(Character cha, int time){
		BuffController.append(cha, Slow.clone(BuffController.getPool(Slow.class), SkillDatabase.find(4, 4), time));
	}
	
	static public void onBuff(object o, Skill skill) {
		o.toSender(new S_ObjectEffect(o, skill.getCastGfx()), true);

		BuffInterface bi = BuffController.find(o, Haste.class);
		if (bi != null && bi.getTime() == -1)
			return;

		if (bi != null) {
			// 촐기 및 헤이스트 제거
			BuffController.remove(o, Haste.class);
		} else {
			ChattingController.toChatting(o, "느려지는 것을 느낍니다.", Lineage.CHATTING_MODE_MESSAGE);
			// 슬로우 등록 및 활성화.
			BuffController.append(o, Slow.clone(BuffController.getPool(Slow.class), skill, skill.getBuffDuration()));
		}
	}
}

