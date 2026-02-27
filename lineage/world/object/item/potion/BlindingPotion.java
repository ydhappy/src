package lineage.world.object.item.potion;

import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.world.controller.BuffController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.CurseBlind;
import lineage.world.object.magic.Detection;

public class BlindingPotion extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new BlindingPotion();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if( !isClick(cha) )
			return;
		
		Detection.onBuff(cha);
		BuffController.append(cha, CurseBlind.clone(BuffController.getPool(CurseBlind.class), SkillDatabase.find(3, 3), 10*60));
		// 스킬 적용.
	
		// 아이템 수량 갱신
		cha.getInventory().count(this, getCount()-1, true);
	}

}
