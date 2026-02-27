package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectGfx;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class ScalesEarthDragon extends Magic {

	public ScalesEarthDragon(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new ScalesEarthDragon(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		//
		toBuffUpdate(o);
		//
		if(o instanceof Character) {
			Character cha = (Character)o;
			//
			if(Lineage.server_version >= 360) {
				cha.setDynamicAc(cha.getDynamicAc() + 3);
				cha.setDynamicStunDefense(cha.getDynamicStunDefense() + 10);
				cha.setGfx(9362);
			} else {
				cha.setDynamicAc( cha.getDynamicAc() + 12 );
				cha.setDynamicHp(cha.getDynamicHp() + 127);
				cha.setGfx(6894);
			}
			//
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
			cha.toSender(S_ObjectGfx.clone(BasePacketPooling.getPool(S_ObjectGfx.class), cha), true);
		}
	}
	
	@Override
	public void toBuffUpdate(object o) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		BuffController.remove(o, ScalesEarthDragon.class);
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o){
		//
		if(o instanceof Character) {
			Character cha = (Character)o;
			//
			if(Lineage.server_version >= 360) {
				cha.setDynamicAc(cha.getDynamicAc() - 3);
				cha.setDynamicStunDefense(cha.getDynamicStunDefense() - 10);
			} else {
				cha.setDynamicAc( cha.getDynamicAc() - 12 );
				cha.setDynamicHp(cha.getDynamicHp() - 127);
			}
			toWorldOut(o);
			//
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
			cha.toSender(S_ObjectGfx.clone(BasePacketPooling.getPool(S_ObjectGfx.class), cha), true);
		}
	}
	
	@Override
	public void toBuff(object o) {
		//
		if(Lineage.server_version >= 360)
			return;
		//
		if(o instanceof Character) {
			Character cha = (Character)o;
			//
			cha.setNowMp(cha.getNowMp() - 1);
			if(cha.getNowMp() == 0)
				BuffController.remove(o, ScalesEarthDragon.class);
		}
	}
	
	@Override
	public void toWorldOut(object o) {
		// gfx 복원.
		o.setGfx(o.getClassGfx());
		o.setGfxModeClear();
	}

	static public void init(Character cha, Skill skill) {
		// 3.60이상 버전부터 600초로되고 그 이하에서는 mp가 다 소진될때까지 인듯.
		// 각성중 다시 각성시 재료소모되면서 원래대로 돌아옴.
		
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		if(SkillController.isMagic(cha, skill, true))
			BuffController.append(cha, ScalesEarthDragon.clone(BuffController.getPool(ScalesEarthDragon.class), skill, Lineage.server_version<360?-1:600));
	}
	

}
