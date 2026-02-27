package lineage.world.object.item.scroll;

import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectAction;
import lineage.share.Lineage;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.DecreaseWeight;

public final class SpellScrollDecreaseWeight extends ItemInstance {
	
	private Skill skill;
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new SpellScrollDecreaseWeight();
		item.setSkill( SkillDatabase.find(2, 5) );
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
		if(Lineage.item_spellscroll_delay && !SkillController.isDelay(cha, skill))
			return;
		
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
		if(SkillController.isMagic(cha, skill, false, true)){
			// 처리
			DecreaseWeight.onBuff(cha, skill);
			// 수량 하향
			cha.getInventory().count(this, getCount()-1, true);
		}
	}

}
