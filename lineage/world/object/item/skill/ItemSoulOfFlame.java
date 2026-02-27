package lineage.world.object.item.skill;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectGfx;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.SoulOfFlame;

public class ItemSoulOfFlame extends ItemInstance {

	private Skill skill;

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ItemSoulOfFlame();
		item.setSkill(SkillDatabase.find(119));
		return item;
	}

	@Override
	public Skill getSkill() {
		return skill;
	}

	@Override
	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		
		if(cha.getClassType()==0x02 && (cha.getAttribute() == 2)){



	//		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
			if(SkillController.isDelay(cha, skill) && SkillController.isMagic(cha, skill, false)){

			SoulOfFlame.init(cha, skill);
			}
		}
	}
}
/*

	@Override
	public void toBuffStart(object o) {
		o.setBuffSoulOfFlame( true );
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		o.setBuffSoulOfFlame( false );
	}
	
	static public void init(Character cha, Skill skill) {
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		if(SkillController.isMagic(cha, skill, true))
			BuffController.append(cha, SoulOfFlame.clone(BuffController.getPool(SoulOfFlame.class), skill, skill.getBuffDuration()));
	}
*/
