package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class MaanShape extends Magic {

	public MaanShape(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new MaanShape(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			cha.setBuffMaanShape(true);
			// 마법 치명타+2%
			cha.setDynamicSp(cha.getDynamicSp() + 1);
			// ER+10
			cha.setDynamicEr(cha.getDynamicEr() + 10);
			// 땅 저항 +5
			cha.setDynamicEarthress(cha.getDynamicEarthress() + 5);
			// 물 저항 +5
			cha.setDynamicWaterress(cha.getDynamicWaterress() + 5);
			// 바람 저항 +5
			cha.setDynamicWindress(cha.getDynamicWindress() + 5);
		}
	}

	@Override
	public void toBuffUpdate(object o) {

	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		if (o.isWorldDelete())
			return;
		if (o instanceof Character) {
			Character cha = (Character) o;
			cha.setBuffMaanShape(false);
			// 마법 치명타-2%
			cha.setDynamicSp(cha.getDynamicSp() - 1);
			// ER-10
			cha.setDynamicEr(cha.getDynamicEr() - 10);
			// 땅 저항 -5
			cha.setDynamicEarthress(cha.getDynamicEarthress() - 5);
			// 물 저항 -5
			cha.setDynamicWaterress(cha.getDynamicWaterress() - 5);
			// 바람 저항 -5
			cha.setDynamicWindress(cha.getDynamicWindress() - 5);
			
			ChattingController.toChatting(cha, "형상의 마안 종료", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	@Override
	public void toBuff(object o) {
		if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
			ChattingController.toChatting(o, "형상의 마안: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(object o, int time) {
		BuffController.append(o, MaanShape.clone(BuffController.getPool(MaanShape.class), SkillDatabase.find(716), time));
	}

	static public void init(Character cha, Skill skill) {
		BuffController.remove(cha, MaanWatar.class);
		BuffController.remove(cha, MaanWind.class);
		BuffController.remove(cha, MaanEarth.class);
		BuffController.remove(cha, MaanFire.class);
		BuffController.remove(cha, MaanBirth.class);
		BuffController.remove(cha, MaanLife.class);

		cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
		BuffController.append(cha, MaanShape.clone(BuffController.getPool(MaanShape.class), skill, skill.getBuffDuration()));
		ChattingController.toChatting(cha, "형상의 마안: 일정 확률로 물리 회피 상승, 마법 대미지 50% 감소, SP+1, ER+10, 지령 내성+5, 수령 내성+5, 풍령 내성+5", Lineage.CHATTING_MODE_MESSAGE);
	}
}
