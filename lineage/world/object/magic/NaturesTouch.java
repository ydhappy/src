package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.npc.kingdom.KingdomDoor;

public class NaturesTouch extends Magic {
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new NaturesTouch(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	private Integer idx = 0;
	
	public NaturesTouch(Skill skill){
		super(null, skill);
	}
	
	@Override
	public void toBuffStart(object o) {
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
	}
	
	@Override
	public void toBuff(object o) {
		//
		if(++idx%6 != 0)
			return;
		idx = 0;
		if(o instanceof Character) {
			Character cha = (Character)o;
			// 무게 오바상태라면 무시.
			if(cha.getInventory()!=null && cha.getInventory().isWeightPercent(50)==false)
				return;
			// 체력 상승 처리.
			if(cha.getNowHp() != cha.getTotalHp())
				cha.setNowHp( cha.getNowHp() + Util.random(skill.getMindmg(), skill.getMaxdmg()) );
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
		if(o != null) {
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
			if(SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, o, skill, false, false)) {
				if(o instanceof KingdomDoor)
					return;

				if(Lineage.server_version > 250) {
					BuffController.append(o, NaturesTouch.clone(BuffController.getPool(NaturesTouch.class), skill, skill.getBuffDuration()));
				} else {
					// 만피 채우는거라는데..
					if(o instanceof Character){
						Character c = (Character)o;
						c.setNowHp( c.getTotalHp() );
						c.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), c, skill.getCastGfx()), true);
					}
				}
			}
		}
	}
	
}
