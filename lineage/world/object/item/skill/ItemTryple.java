package lineage.world.object.item.skill;

import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ItemTryple extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ItemTryple();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getClassType() == 0x02 && cha.getLevel() >= 45) {
			if (cha.getInventory() != null) {
				PcInstance pc = (PcInstance) cha;
				if (pc.isDead()) {
					ChattingController.toChatting(pc, "죽은 상태에선 사용할 수 없습니다.", 20);
					return;
				} else if (pc.isLock()) {
					ChattingController.toChatting(pc, "마비 상태에선 사용할 수 없습니다.", 20);
					return;
				}

				if (pc.getAutoTryple() == false) {
					pc.setAutoTryple(true);
					ChattingController.toChatting(pc, String.format("[자동 트리플: ON][확률: %d]", Lineage.tryple), 20);
				} else {
					pc.setAutoTryple(false);
					ChattingController.toChatting(pc, String.format("[자동 트리플: OFF]"), 20);
				}
			}
		} else {
			ChattingController.toChatting(cha, "45레벨 이상 요정케릭이 사용 가능합니다.");
			return;
		}
	}
}