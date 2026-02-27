package lineage.world.object.magic;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.bean.lineage.Clan;
import lineage.bean.lineage.Party;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_BuffRoyal;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.ClanController;
import lineage.world.controller.PartyController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class BraveAura extends Magic {

	public BraveAura(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new BraveAura(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if(o instanceof Character){
			Character cha = (Character)o;
			// 추뎀 +5
			cha.setDynamicAddDmg( cha.getDynamicAddDmg() + 5 );
			cha.setDynamicAddDmgBow( cha.getDynamicAddDmgBow() + 5 );
		}
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		if(o instanceof Character){
			Character cha = (Character)o;
			cha.toSender(S_BuffRoyal.clone(BasePacketPooling.getPool(S_BuffRoyal.class), 116, getTime(), cha.getTotalAc()));
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
			cha.setDynamicAddDmg( cha.getDynamicAddDmg() - 5 );
			cha.setDynamicAddDmgBow( cha.getDynamicAddDmgBow() - 5 );
		}
	}
	
	static public void init(Character cha, Skill skill){
		// 패킷
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		// 시전가능 확인
		if(SkillController.isMagic(cha, skill, true) && cha instanceof PcInstance){
			// 초기화
			PcInstance royal = (PcInstance)cha;
			List<object> list_temp = new ArrayList<object>();
			list_temp.add(royal);
			
			// 파티원 추출.
			Party p = PartyController.find2(royal);
			if(p != null){
				for(PcInstance pc : p.getList())
					list_temp.add(pc);
			}
			
			Clan c = ClanController.find(royal);
			if(c != null) {
				for(PcInstance pc : c.getList())
					if(!list_temp.contains(pc) && Util.isDistance(royal, pc, Lineage.SEARCH_LOCATIONRANGE))
						list_temp.add(pc);
			}
			
			// 처리.
			for(object o : list_temp) {
				if(Util.isDistance(cha, o, Lineage.SEARCH_LOCATIONRANGE))
					onBuff(o, skill);
			}
		}
	}
	
	/**
	 * 중복코드 방지용.
	 * @param cha
	 * @param pc
	 * @param skill
	 */
	static public void onBuff(object o, Skill skill) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		BuffController.append(o, BraveAura.clone(BuffController.getPool(BraveAura.class), skill, skill.getBuffDuration()));
	}

}
