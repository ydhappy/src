package lineage.world.object.item.potion;

import lineage.bean.database.ItemSetoption;
import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.PetInstance;
import lineage.world.object.magic.item.Bravery;
import lineage.world.object.magic.AbsoluteBarrier;

public class BraveryPotion extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new BraveryPotion();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
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
		
		if (isClassCheck(cha)) {
			// 이팩트 표현
			cha.toSender(new S_ObjectEffect( cha, getItem().getEffect()), true);
			// 버프 처리
			Skill s = SkillDatabase.find(201);
			int time = 0;
			switch(getItem().getNameIdNumber()) {
				case 7896:	// 농축 용기의 물약
					time = 1200;
					break;
				default:
					time = s.getBuffDuration()==0 ? 300 : s.getBuffDuration();
					break;
			}
			Bravery.init(cha, time);
		} else {
			// 아무일도 일어나지 않았습니다.
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
		}

		// 아이템 수량 갱신
		cha.getInventory().count(this, getCount() - 1, true);
	}

}