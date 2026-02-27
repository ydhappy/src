package lineage.world.object.item.potion;

import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.AbsoluteBarrier;
import lineage.world.object.magic.item.Blue;

public class BluePotion extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new BluePotion();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if( !isClick(cha) )
			return;
		if (item.getLevelMin() > 0 && item.getLevelMin() > cha.getLevel()) {
			// cha.toSender(new SItemLevelFails(item.Level));
			// 672 : 이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.
			ChattingController.toChatting(cha, String.format("이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.", item.getLevelMin()), Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		if (item.getLevelMax() > 0 && item.getLevelMax() < cha.getLevel()) {
			// 673 : 이 아이템은 %d레벨 이하일때만 사용할 수 있습니다.
			ChattingController.toChatting(cha, String.format("이 아이템은 %d레벨 이하일 때만 사용할 수 있습니다.", item.getLevelMax()), Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		// 앱솔상태 해제.
		if (cha.isBuffAbsoluteBarrier())
			BuffController.remove(cha, AbsoluteBarrier.class);
		// 이팩트 표현
		    cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, getItem().getEffect()), true);
		// 버프 처리
		Blue.init(cha, SkillDatabase.find(204).getBuffDuration());
		// 아이템 수량 갱신
		cha.getInventory().count(this, getCount()-1, true);
	}
}
