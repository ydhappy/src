package lineage.world.object.magic;

import lineage.bean.database.MonsterSkill;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectPoisonLock;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.DamageController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;

public class CursePoison extends Magic {

	public CursePoison(Character cha, Skill skill) {
		super(cha, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Character cha, Skill skill, int time) {
		if (bi == null)
			bi = new CursePoison(cha, skill);
		bi.setSkill(skill);
		bi.setTime(time);
		bi.setCharacter(cha);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setPoison(true);
		o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
		o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 3051));
		if(o.isWorldJoin())
		// 공격당한거 알리기.
		o.toDamage(cha, 0, Lineage.ATTACK_TYPE_MAGIC);
	}

	@Override
	public void toBuff(object o) {

			if (!o.isDead() && !o.isLockHigh()) {
				int dmg = Util.random(4, 12);
				if(cha.isDead())
					o.setNowHp(o.getNowHp() - dmg);
				else
					DamageController.toDamage(cha, o, dmg, Lineage.ATTACK_TYPE_MAGIC);
			}
	}
	@Override
	public boolean toBuffStop(object o) {
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		if (o.isWorldDelete())
			return;
		o.setPoison(false);
		o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
	}

	static public void init(Character cha, Skill skill, long object_id) {
		// 타겟 찾기
		object o = cha.findInsideList(object_id);
		// 처리
		if (o != null) {
			BuffController.remove(cha, Meditation.class);
			if (SkillController.isMagic(cha, skill, true)) {
				cha.toSender(new S_ObjectAction(cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
				onBuff(cha, o, skill, true);
			}
		}
	}

	static public void init(Character cha, int time) {
		BuffController.append(cha, CursePoison.clone(BuffController.getPool(CursePoison.class), null, SkillDatabase.find(2, 2), time));
	}

	/**
	 * 몬스터 용
	 */
	static public void init(MonsterInstance mi, object o, MonsterSkill ms, int action, int effect, boolean check){
		if(action != -1)
			mi.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), mi, action), true);
		if (o.getInventory().isRingZnis())
			return;
		if(check && !SkillController.isMagic(mi, ms, true))
			return;
		if(SkillController.isFigure(mi, o, ms.getSkill(), true, false)){
			if(effect != 0)
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, effect), true);
			BuffController.append(o, CursePoison.clone(BuffController.getPool(CursePoison.class), mi, ms.getSkill(), ms.getBuffDuration()));
		}
	}

	/**
	 * 중복코드 방지용
	 * 
	 * @param cha
	 * @param o
	 * @param skill
	 */
	static public void onBuff(Character cha, object o, Skill skill, boolean ment) {
		// 투망상태 해제
		Detection.onBuff(cha);
		if (o.getInventory().isRingZnis())
			return;
		// 처리
		if (SkillController.isFigure(cha, o, skill, true, false)) {
			onBuff(cha, o, skill);
			return;
		}
		// \f1마법이 실패했습니다.
		if (ment)
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 280));
	}

	static public void onBuff(Character cha, object o, Skill skill) {
		o.toSender(new S_ObjectEffect(o, skill.getCastGfx()), true);
		//BuffController.append(o, new CursePoison(cha, skill, skill.getBuffDuration()));
		BuffController.append(o, CursePoison.clone(BuffController.getPool(CursePoison.class), cha, skill, skill.getBuffDuration()));
	}
}
