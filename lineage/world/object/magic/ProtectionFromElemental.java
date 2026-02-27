package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class ProtectionFromElemental extends Magic {

	public ProtectionFromElemental(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new ProtectionFromElemental(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if(o instanceof PcInstance){
			PcInstance pc = (PcInstance)o;
			switch(pc.getAttribute()){
				case Lineage.ELEMENT_EARTH:
					pc.setDynamicEarthress( pc.getDynamicEarthress() + 50 );
					break;
				case Lineage.ELEMENT_FIRE:
					pc.setDynamicFireress( pc.getDynamicFireress() + 50 );
					break;
				case Lineage.ELEMENT_WIND:
					pc.setDynamicWindress( pc.getDynamicWindress() + 50 );
					break;
				case Lineage.ELEMENT_WATER:
					pc.setDynamicWaterress( pc.getDynamicWaterress() + 50 );
					break;
			}
			pc.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), pc));
		}
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o){
		if(o instanceof PcInstance){
			PcInstance pc = (PcInstance)o;
			switch(pc.getAttribute()){
				case Lineage.ELEMENT_EARTH:
					pc.setDynamicEarthress( pc.getDynamicEarthress() - 50 );
					break;
				case Lineage.ELEMENT_FIRE:
					pc.setDynamicFireress( pc.getDynamicFireress() - 50 );
					break;
				case Lineage.ELEMENT_WIND:
					pc.setDynamicWindress( pc.getDynamicWindress() - 50 );
					break;
				case Lineage.ELEMENT_WATER:
					pc.setDynamicWaterress( pc.getDynamicWaterress() - 50 );
					break;
			}
			pc.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), pc));
		}
	}
	
	static public void init(Character cha, Skill skill){
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		if(SkillController.isMagic(cha, skill, true)){
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			BuffController.append(cha, ProtectionFromElemental.clone(BuffController.getPool(ProtectionFromElemental.class), skill, skill.getBuffDuration()));
		}
	}

}
