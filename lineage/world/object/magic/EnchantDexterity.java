package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_BuffDex;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class EnchantDexterity extends Magic {

	public EnchantDexterity(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new EnchantDexterity(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		if(o instanceof Character){
			Character cha = (Character)o;
			cha.setDynamicDex( cha.getDynamicDex() + skill.getMaxdmg() );
		}
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o){
		if(o instanceof Character){
			Character cha = (Character)o;
			// 몸놀림이 가벼워지는 것을 느낍니다.
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 294));
			if(Lineage.server_version>=163)
				cha.toSender(S_BuffDex.clone(BasePacketPooling.getPool(S_BuffDex.class), cha, getTime(), skill.getMaxdmg()));
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
			cha.setDynamicDex( cha.getDynamicDex() - skill.getMaxdmg() );
			if(Lineage.server_version>=163)
				cha.toSender(S_BuffDex.clone(BasePacketPooling.getPool(S_BuffDex.class), cha, 0, skill.getMaxdmg()));
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
	}
	
	static public void init(Character cha, Skill skill, long object_id){
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
			if(SkillController.isMagic(cha, skill, true)){
				if(SkillController.isFigure(cha, o, skill, false, SkillController.isClan(cha, o))){
					onBuff(o, skill);
				}else{
					// \f1마법이 실패했습니다.
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 280));
				}
			}
		}
	}
	
	static public void init(Character cha, int time){
		BuffController.append(cha, EnchantDexterity.clone(BuffController.getPool(EnchantDexterity.class), SkillDatabase.find(4, 1), time));
	}
	
	static public void onBuff(object o, Skill skill){
		//
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		//
		BuffController.remove(o, DressDexterity.class);
		//
		BuffController.append(o, EnchantDexterity.clone(BuffController.getPool(EnchantDexterity.class), skill, skill.getBuffDuration()));
	}
	
}
