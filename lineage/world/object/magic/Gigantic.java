package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class Gigantic extends Magic {

	public Gigantic(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new Gigantic(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		if(o instanceof Character){
			Character c = (Character)o;
			//
			if(c.getBuffGiganticHp() > 0)
				c.setDynamicHp(c.getDynamicHp()-c.getBuffGiganticHp());
			//
			double percent = Math.floor((double)c.getLevel()/2);
			c.setBuffGiganticHp((int)(c.getMaxHp()*(percent*0.01)));
			c.setDynamicHp(c.getDynamicHp()+c.getBuffGiganticHp());
			c.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), c));
		}
	}
	
	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o){
		if(o instanceof Character){
			Character c = (Character)o;
			//
			c.setDynamicHp(c.getDynamicHp()-c.getBuffGiganticHp());
			c.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), c));
			//
			c.setBuffGiganticHp(0);
		}
	}

	/**
	 * 사용자 가 사용하는 함수.
	 * @param cha
	 * @param skill
	 */
	static public void init(Character cha, Skill skill) {
		//
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
		BuffController.append(cha, Gigantic.clone(BuffController.getPool(Gigantic.class), skill, skill.getBuffDuration()));
	}
	
}
