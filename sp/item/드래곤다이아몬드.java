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
import sp.magic.드래곤다이아몬드버프;
import sp.magic.드래곤루비버프;
import sp.magic.드래곤사파이어버프;
import sp.magic.드래곤에메랄드버프;

public class 드래곤다이아몬드 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 드래곤다이아몬드();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		
		if(cha.getLevel() < 30) {
			ChattingController.toChatting(cha, "이 아이템은 30레벨 이상이 되어야 사용할 수 있습니다.", 20);
			return;
		}
		
		// 드래곤 루비 또는 드래곤 다이아몬드 버프 중일 경우 사용 못하게.2020.08.08
		if(BuffController.find(cha).find(드래곤다이아몬드버프.class) != null)
		{
			ChattingController.toChatting(cha, "이미 경험치 50% 증가 효과가 적용중입니다.", 20);
			return;
		}
		if(BuffController.find(cha).find(드래곤루비버프.class) != null)
			
		{
			ChattingController.toChatting(cha, "이미 경험치 20% 증가 효과가 적용중입니다.", 20);
			return;
		}
		if(BuffController.find(cha).find(드래곤에메랄드버프.class) != null)
		{
			ChattingController.toChatting(cha, "이미 경험치 100% 증가 효과가 적용중입니다.", 20);
			return;
		}
		if(BuffController.find(cha).find(드래곤사파이어버프.class) != null)
		{
			ChattingController.toChatting(cha, "이미 경험치 150% 증가 효과가 적용중입니다.", 20);
			return;
		}
		
		
		

		
		if(BuffController.find(cha).find(경험치물약버프.class) != null) {
			BuffController.remove(cha, 경험치물약버프.class);
		}
		
		if(cha instanceof PcInstance){
			PcInstance pc = (PcInstance)cha;
			Skill s = SkillDatabase.find(211);

			if(s != null) {
				// 스킬 적용.
				드래곤다이아몬드버프.onBuff(cha, s, s.getBuffDuration(), false);

				pc.getInventory().count(this, getCount()-1, true);
			}
			// 아이템 수량 갱신
		
		}
	}

}
