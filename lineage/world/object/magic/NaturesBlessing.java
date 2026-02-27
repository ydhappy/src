package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.Party;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.PartyController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.item.Criminal;

public class NaturesBlessing {

	static public void init(Character cha, Skill skill){
		if(cha instanceof PcInstance){
			PcInstance pc = (PcInstance)cha;
			
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
			if(SkillController.isMagic(cha, skill, true)){
				Party p = PartyController.find2(pc);
				if(p == null){
					onBuff(cha, pc, skill);
					
				}else{
					for(PcInstance use : p.getList()){
						if(Util.isDistance(pc, use, Lineage.SEARCH_LOCATIONRANGE) && SkillController.isFigure(cha, use, skill, false, false)) {
							onBuff(cha, use, skill);
							
							//
							if(use.isBuffCriminal() && !cha.isBuffCriminal())
								Criminal.onBuff(cha, SkillDatabase.find(206));
						}
					}
				}
			}
		}
	}
	//씽요 회복량 수정. 성환
	static public void onBuff(Character cha, Character target, Skill skill){
		target.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), target, skill.getCastGfx()), true);
		int dmg = SkillController.getDamage(cha, target, target, skill, 0, skill.getElement());
		
		if(dmg > 0)
			target.setNowHp(target.getNowHp() + dmg);
	}
}
