package jsn_soft;


import lineage.database.NpcSpawnlistDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class AddHunt extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new AddHunt();
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
		jsn_hunt jh = AutoHuntDatabase.find(cha.getObjectId());
		PcInstance pc = (PcInstance) cha;
		int time = 60;
		if (jh == null) {
			AutoHuntDatabase.insertHunt((PcInstance) cha); 
		} else {
			// 아이템의 최소 대미지 ~ 최대 대미지 까지의 시간 (분) 을 충전하는 충전석
			int add = Util.random(getItem().getDmgMin(), getItem().getDmgMax());
			
			// 디비에 계속 저장하게 되면 무리가 될 수 있기에 피씨와 jsn_hunt에 저장한다.
			pc.setAutoTime(pc.getAutoTime() + (add * time));
			jh.setPcHuntTime(pc.getAutoTime() + (add * time));
			ChattingController.toChatting(pc, "자동사냥 시간이 " + add + "분 충전되었습니다", Lineage.CHATTING_MODE_MESSAGE);
			cha.getInventory().count(this, this.getCount() -1 , true);
		}
	}

}