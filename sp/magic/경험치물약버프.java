package sp.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Common;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.Magic;

public class 경험치물약버프 extends Magic {

	public double dynamicExp;
	
	public 경험치물약버프(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time, int rate){
		if(bi == null)
			bi = new 경험치물약버프(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		((경험치물약버프)bi).dynamicExp = rate*0.01;
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		//
		if (dynamicExp != 0)
			toBuffEnd(o);
		//
		if (skill != null) {
			if (dynamicExp <= 0)
				dynamicExp = 0.5F;

			if (o instanceof PcInstance) {
				PcInstance pc = (PcInstance) o;
				pc.setDynamicExp(pc.getDynamicExp() + dynamicExp);
				if (Common.requestor.equalsIgnoreCase("giro"))
					ChattingController.toChatting(o, "경험치 효과가 상승하였습니다.",
							20);
				else
					ChattingController.toChatting(o, "추가 경험치를 얻습니다.",
							20);
			}
		}
	}

	@Override
	public boolean toBuffStop(object o){
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		if(o instanceof PcInstance) {
			PcInstance pc = (PcInstance)o;
			pc.setDynamicExp( pc.getDynamicExp() - dynamicExp );
			if(skill != null){
				if(Common.requestor.equalsIgnoreCase("giro"))
					ChattingController.toChatting(o, "경험치 효과가 원래대로 돌아왔습니다.", 20);
				else
					ChattingController.toChatting(o, "정상 경험치를 얻습니다.", 20);
			}
		}
	}

	@Override
	public void toBuff(object o) {
		if(skill!=null && skill.getCastGfx()>0)
			o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
	}

	static public void init(Character cha, int time, int rate){
		Skill s = SkillDatabase.find(406);
		if(s != null)
			BuffController.append(cha, 경험치물약버프.clone(BuffController.getPool(경험치물약버프.class), s, time, rate));
	}

}
