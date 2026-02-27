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

public class MaanWind extends Magic {

	public MaanWind(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new MaanWind(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			cha.setBuffMaanWind(true);
			// ER+10
			cha.setDynamicEr(cha.getDynamicEr() + 10);
			cha.setDynamicSp(cha.getDynamicSp() + 1);
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
			cha.setBuffMaanWind(false);
			// ER-10
			cha.setDynamicEr(cha.getDynamicEr() - 10);
			cha.setDynamicSp(cha.getDynamicSp() - 1);
			// 바람 저항 -5
			cha.setDynamicWindress(cha.getDynamicWindress() - 5);
			ChattingController.toChatting(o, "풍룡의 마안 종료", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	@Override
	public void toBuff(object o) {
		if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
			ChattingController.toChatting(o, "풍룡의 마안: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(object o, int time) {
		BuffController.append(o, MaanWind.clone(BuffController.getPool(MaanWind.class), SkillDatabase.find(712), time));
	}

	static public void init(Character cha, Skill skill) {
		BuffController.remove(cha, MaanWatar.class);
		BuffController.remove(cha, MaanEarth.class);
		BuffController.remove(cha, MaanFire.class);
		BuffController.remove(cha, MaanBirth.class);
		BuffController.remove(cha, MaanShape.class);
		BuffController.remove(cha, MaanLife.class);

		cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
		BuffController.append(cha, MaanWind.clone(BuffController.getPool(MaanWind.class), skill, skill.getBuffDuration()));
		ChattingController.toChatting(cha, "풍룡의 마안: SP+1, ER+10, 풍령 내성+5", Lineage.CHATTING_MODE_MESSAGE);
	}

}
