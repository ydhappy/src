package cholong.buff;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterSpMr;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.GameSetting;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.magic.Magic;

public class ClanMemberBuff extends Magic{
	
	
	private int Member_AC = GameSetting.BUFF_AC;
	private int Member_HIT = GameSetting.BUFF_HIT;
	private int Member_DMG = GameSetting.BUFF_DMG;
	private int Member_SP = GameSetting.BUFF_SP;
	private int Member_REDUC = GameSetting.BUFF_REDUC;
	private double Member_EXP = GameSetting.BUFF_EXP * 0.01;
	
	
	public ClanMemberBuff(Skill skill){
		super(null, skill);
	}

	public static synchronized BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new ClanMemberBuff(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	public void toBuffStart(object o){
	  if(o instanceof Character){
			Character cha = (Character)o;
			cha.setDynamicAc(cha.getDynamicAc() + Member_AC);
			cha.setDynamicAddHit(cha.getDynamicAddHit() + Member_HIT);
			cha.setDynamicAddHitBow(cha.getDynamicAddHit() + Member_HIT);
			cha.setDynamicReduction(cha.getDynamicReduction() + Member_REDUC);
			if(cha.getClassType() == 0x03){
				cha.setDynamicSp(cha.getDynamicSp() + Member_SP);
			} else {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + Member_DMG);
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmg() + Member_DMG);
			}
			
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
			cha.toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), cha));
		}
		toBuffUpdate(o);
	}

	public void toBuffUpdate(object o){
    
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	public void toBuffEnd(object o){
		if(o.isWorldDelete())
			return;
		if(o instanceof Character){
			Character cha = (Character)o;
			cha.setDynamicAc(cha.getDynamicAc() - Member_AC);
			cha.setDynamicAddHit(cha.getDynamicAddHit() - Member_HIT);
			cha.setDynamicAddHitBow(cha.getDynamicAddHit() - Member_HIT);
			cha.setDynamicReduction(cha.getDynamicReduction() - Member_REDUC);
			if(cha.getClassType() == 0x03){
				cha.setDynamicSp(cha.getDynamicSp() - Member_SP);
			} else {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - Member_DMG);
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmg() - Member_DMG);
			}
			
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
			cha.toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), cha));
		}
	}

	public void toBuff(object o){
		if ((this.skill != null) && (this.skill.getCastGfx() > 0))
			o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, this.skill.getCastGfx()), true);
	}

	public static void onBuff(object o, Skill skill) {
		BuffController.append(o, clone(BuffController.getPool(ClanMemberBuff.class), skill, skill.getBuffDuration()));
	}
	public static void init(Character cha, int time) {
		BuffController.append(cha, clone(BuffController.getPool(ClanMemberBuff.class), SkillDatabase.find(8000), time));
	}
}