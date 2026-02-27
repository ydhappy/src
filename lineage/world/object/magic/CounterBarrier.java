package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.RobotInstance;

public class CounterBarrier extends Magic {
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new CounterBarrier(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	public CounterBarrier(Skill skill){
		super(null, skill);
	}
	
	@Override
	public void toBuffStart(object o) {
		if(o instanceof Character) {
			Character cha = (Character)o;
			cha.setBuffCounterBarrier(true);
		}
	}

	@Override
	public boolean toBuffStop(object o) {
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		if(o instanceof Character) {
			Character cha = (Character)o;
			cha.setBuffCounterBarrier(false);
		}
	}

	/**
	 * 
	 * @param cha
	 * @param skill
	 * @param object_id
	 * @param x
	 * @param y
	 */
	static public void init(Character cha, Skill skill) {
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		if(cha instanceof RobotInstance) {
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			BuffController.append(cha, CounterBarrier.clone(BuffController.getPool(CounterBarrier.class), skill, skill.getBuffDuration()));
		} else {
			if(cha.getInventory().getSlot(8)!=null && cha.getInventory().getSlot(8).getItem().getType2().equalsIgnoreCase("tohandsword") && SkillController.isMagic(cha, skill, true)) {
				cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
				BuffController.append(cha, CounterBarrier.clone(BuffController.getPool(CounterBarrier.class), skill, skill.getBuffDuration()));
			}
		}
	}

}
