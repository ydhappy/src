package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class AquaProtect extends Magic {

	public AquaProtect(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new AquaProtect(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 2); //근거리 추타
			cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 2);
			cha.setDynamicSp(cha.getDynamicSp() + 2); //스펠파워
			cha.setBuffAquaProtect(true);
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
		if (o.isWorldDelete())
			return;
		if (o instanceof Character) {
			Character cha = (Character) o;
			cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 2); //근거리 추타
			cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 2);
			cha.setDynamicSp(cha.getDynamicSp() - 2); //스펠파워
			cha.setBuffAquaProtect(false);
		}
	}
	
	static public void init(Character cha, Skill skill, long object_id) {
		// 초기화
		object o = null;
		// 타겟 찾기
		if(object_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList( object_id );
		// 처리
		if(o != null) {
				onBuff(o, skill);
		}
	}

	static public void onBuff(object o, Skill skill) {
		BuffController.append(o, AquaProtect.clone(BuffController.getPool(AquaProtect.class), skill, skill.getBuffDuration()));
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
	}

}
