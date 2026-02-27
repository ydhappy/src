package sp.item;

import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import sp.magic.경험치물약버프;

public class 경험치물약 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 경험치물약();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		// 기존 경험치버프 상태면 무시하기.
		try {
			if(BuffController.find(cha).find(lineage.world.object.magic.sp.ExpPotion.class) != null) {
				ChattingController.toChatting(cha, "중복 사용할 수 없습니다.", 20);
				return;
			}
		} catch (Exception e) { }
		//
		if(cha instanceof PcInstance){
			PcInstance pc = (PcInstance)cha;
			Skill s = SkillDatabase.find(406);
			if(s != null) {
				// 스킬 적용.
				BuffController.append(cha, 경험치물약버프.clone(BuffController.getPool(경험치물약버프.class), s, 30*60, getItem().getDmgMin()));
				경험치물약버프 bi = (경험치물약버프)BuffController.find(cha).find(경험치물약버프.class);
				bi.dynamicExp = getItem().getDmgMin() * 0.01;
			}
			// 아이템 수량 갱신
			pc.getInventory().count(this, getCount()-1, true);
		}
	}

}
