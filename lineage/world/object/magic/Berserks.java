package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class Berserks extends Magic {

	private int dynamic_ac;
	
	public Berserks(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new Berserks(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		// 대상은 근거리 공격력이 +5 상승하는 대신 AC가 10 하락하고, HP가 자연 회복되지 않는다.
		o.setBuffBerserks(true);
		if(o instanceof Character){
			Character cha = (Character)o;
			dynamic_ac = Util.random(skill.getMindmg(), skill.getMaxdmg());
			cha.setDynamicAc( cha.getDynamicAc()-dynamic_ac );
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
		
		o.setBuffBerserks(false);
		if(o instanceof Character){
			Character cha = (Character)o;
			cha.setDynamicAc( cha.getDynamicAc()+dynamic_ac );
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
			// 확인.
			if(Util.isDistance(cha, o, 15) && SkillController.isMagic(cha, skill, true)){
				// 혈맹원 및 파티원에게만 적용되게.
				if(cha.getObjectId() != o.getObjectId()) {
					if(o.getPartyId()==0 || cha.getPartyId()!=o.getPartyId())
						return;
					if(o.getClanId()==0 || cha.getClanId()!=o.getClanId())
						return;
				}
				// 이팩트 표현.
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
				// 적용.
				BuffController.append(o, Berserks.clone(BuffController.getPool(Berserks.class), skill, skill.getBuffDuration()));
			}
		}
	}

}
