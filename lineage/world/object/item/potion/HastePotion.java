package lineage.world.object.item.potion;

import lineage.bean.database.ItemSetoption;
import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.AbsoluteBarrier;
import lineage.world.object.magic.Haste;

public class HastePotion extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new HastePotion();
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
		// 이팩트 표현

			if(getItem().getEffect() == 0)
				cha.toSender(new S_ObjectEffect( cha, 191), true);
			else
				cha.toSender(new S_ObjectEffect( cha, getItem().getEffect()), true);

		// \f1갑자기 빠르게 움직입니다.
	//	cha.toSender(new S_Message( 184));
		//
		Skill s = SkillDatabase.find(43);
		int time = 0;
		switch(getItem().getNameIdNumber()) {
			case 234: // 초록물약
			case 3302: // 상아탑의 속도향상 물약
				time = 300;
				break;
			case 3406: // 위스키
			    time = 900;
			    break;
			case 7898:	// 농축 속도의 물약
			case 1652234: // 강화 초록 물약
				time = 1800;
			    break;
			default:
				time = s.getBuffDuration()==0 ? 300 : s.getBuffDuration();
				break;
		}
		//
		// 버프 처리
		Haste.init(cha, time);
		// 아이템 수량 갱신
		cha.getInventory().count(this, getCount() - 1, true);

	}
}