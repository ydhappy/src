package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_BuffStr;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class DressMighty extends Magic {

	public DressMighty(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new DressMighty(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
	
	@Override
	public void toBuffStart(object o){
		if(o instanceof Character){
			Character cha = (Character)o;
			cha.setDynamicStr( cha.getDynamicStr() + skill.getMaxdmg() );
		}
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o){
		if(o instanceof Character){
			Character cha = (Character)o;
			if(Lineage.server_version>=163)
				cha.toSender(S_BuffStr.clone(BasePacketPooling.getPool(S_BuffStr.class), cha, getTime(), skill.getMaxdmg()));
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
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
		if(o instanceof Character){
			Character cha = (Character)o;
			cha.setDynamicStr( cha.getDynamicStr() - skill.getMaxdmg() );
			if(Lineage.server_version>=163)
				cha.toSender(S_BuffStr.clone(BasePacketPooling.getPool(S_BuffStr.class), cha, 0, skill.getMaxdmg()));
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
	}
	
	static public void init(Character cha, Skill skill) {
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		if(SkillController.isMagic(cha, skill, true))
			onBuff(cha, skill);
	}
	
	static public void onBuff(object o, Skill skill){
		//
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		//
		BuffController.remove(o, EnchantMighty.class);
		//
		BuffController.append(o, DressMighty.clone(BuffController.getPool(DressMighty.class), skill, skill.getBuffDuration()));
	}

}
