package lineage.world.object.item;

import lineage.bean.database.Exp;
import lineage.database.ExpDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class Exp_support2 extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new Exp_support2();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null && !cha.isWorldDelete() && !cha.isLock() && !cha.isDead()) {
			int maxLevel = Lineage.exp_support_max_level;
			
			if (Lineage.open_wait) {
				ChattingController.toChatting(cha, "[오픈대기] 오픈대기에는 레벨업 하실수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if (Lineage.is_exp_support && cha.getLevel() < maxLevel && cha.getLevel() < Lineage.exp_support_max_level) {
				
					Exp e = ExpDatabase.find(cha.getLevel());
					cha.setExp(e.getBonus() + 0.01);
			
				
			} else {
				if (Lineage.is_exp_support) {
					if (cha.getLevel() >= Lineage.exp_support_max_level)
						ChattingController.toChatting(cha, String.format("신규레벨업 지원은 %d레벨까지 사용 가능합니다.", Lineage.exp_support_max_level ), Lineage.CHATTING_MODE_MESSAGE);
					else if (maxLevel > 0 && cha.getLevel() >= maxLevel)
						ChattingController.toChatting(cha, String.format("신규레벨업 지원은 %d레벨까지 사용 가능합니다.", maxLevel ), Lineage.CHATTING_MODE_MESSAGE);
				} else {
					ChattingController.toChatting(cha, "신규레벨업 지원은 현재 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			}
		}
	}

}
