package lineage.world.object.magic.item;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectCriminal;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.magic.Magic;

public class Criminal extends Magic {

	public Criminal(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new Criminal(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setBuffCriminal(true);
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		if (Lineage.server_version >= 163)
			o.toSender(S_ObjectCriminal.clone(BasePacketPooling.getPool(S_ObjectCriminal.class), o, getTime()), true);
                        o.toSender(new S_ObjectCriminal(o, 30));
	}

	@Override
	public boolean toBuffStop(object o) {
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		o.setBuffCriminal(false);
	}

	static public void init(Character cha, object target) {
		// 바포메트 시스템상태에서는 보라돌이를 할 필요가 없음.
		if (Lineage.is_batpomet_system && !Lineage.baphomet_system_criminal)
			return;
		//
		Skill s = SkillDatabase.find(206);
		if (s != null && target.getLawful() >= Lineage.NEUTRAL)
			onBuff(cha, s);
	}

	static public void onBuff(Character cha, Skill s) {
		// 바포메트 시스템상태일때 보라돌이 사용유무 체크.
		if (Lineage.is_batpomet_system && !Lineage.baphomet_system_criminal)
			return;
		if (s == null)
			return;
		BuffController.append(cha, Criminal.clone(BuffController.getPool(Criminal.class), s, s.getBuffDuration()));
	}

}
