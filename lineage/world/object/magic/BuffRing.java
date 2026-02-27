package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterSpMr;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class BuffRing extends Magic {

	public BuffRing(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new BuffRing(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character target = (Character) o;
			target.setDynamicStr(target.getDynamicStr() + 1);
			target.setDynamicDex(target.getDynamicDex() + 1);
			target.setDynamicInt(target.getDynamicInt() + 1);
			target.setDynamicCon(target.getDynamicCon() + 1);
			target.setDynamicWis(target.getDynamicWis() + 1);
			target.setDynamicCha(target.getDynamicCha() + 1);
			target.setDynamicHp(target.getDynamicHp() + 100);

			target.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), target));
			target.toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), target));

			toBuffUpdate(o);
		}
	}

	@Override
	public boolean toBuffStop(object o) {
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		if (o instanceof Character) {
			Character target = (Character) o;
			target.setDynamicStr(target.getDynamicStr() - 1);
			target.setDynamicDex(target.getDynamicDex() - 1);
			target.setDynamicInt(target.getDynamicInt() - 1);
			target.setDynamicCon(target.getDynamicCon() - 1);
			target.setDynamicWis(target.getDynamicWis() - 1);
			target.setDynamicCha(target.getDynamicCha() - 1);
			target.setDynamicHp(target.getDynamicHp() - 100);

			target.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), target));
			target.toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), target));

			ChattingController.toChatting(o, "변신 지배 버프 종료", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	//@Override
	//public void toBuff(object o) {
	//	if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
	//		ChattingController.toChatting(o, "변신 지배 버프: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	//}

	static public void init(object o, int time) {
		BuffController.append(o, BuffRing.clone(BuffController.getPool(BuffRing.class), SkillDatabase.find(507), time));
	}
	
	static public void onBuff(object o, Skill skill) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		// 버프 등록
		BuffController.append(o, BuffRing.clone(BuffController.getPool(BuffRing.class), skill, skill.getBuffDuration()));
		ChattingController.toChatting(o, "변신 지배 버프: 모든 스탯+1, 최대 HP+100", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
