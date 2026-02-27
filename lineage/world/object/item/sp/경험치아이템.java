package lineage.world.object.item.sp;

import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.sp.ExpPotion;

public class 경험치아이템 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 경험치아이템();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// 기존 경험치버프 상태면 무시하기.
		try {
			if(BuffController.find(cha).find(sp.magic.경험치물약버프.class) != null) {
				ChattingController.toChatting(cha, "이미 경험치 보너스를 받고 있어, 사용할 수 없습니다.", 20);
				return;
			}
		} catch (Exception e) { }
		//
		if(cha instanceof PcInstance){
			PcInstance pc = (PcInstance)cha;
			Skill s = SkillDatabase.find(405);
			if(s != null)
				// 스킬 적용.
				BuffController.append(cha, ExpPotion.clone(BuffController.getPool(ExpPotion.class), s, 30*60));
			// 아이템 수량 갱신
			pc.getInventory().count(this, getCount()-1, true);
		}
	}

}
