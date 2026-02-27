package lineage.world.object.item;

import lineage.bean.database.Item;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class Meat extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Meat();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
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
		updateFood(cha);
		// \f1%0%o 먹었습니다.
		cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 76, toString()));
		// 고기수량 갱신
		cha.getInventory().count(this, getCount()-1, true);
	}
	
	/**
	 * 고기먹을시 food값 처리해주는 함수.
	 * @param cha
	 */
	protected void updateFood(Character cha) {
		/*
		 * if(cha.getFood() == 7 || cha.getFood() == 12 || cha.getFood() == 16
		 * || cha.getFood() == 21 || cha.getFood() == 25){
		 * cha.setFood(cha.getFood() + 2); }else{ cha.setFood(cha.getFood() +
		 * 1); }
		 */
		cha.setFood(cha.getFood() + getFoodValue(getItem()));
	}
	
	static public int getFoodValue(Item item) {
		if (item.getDmgMin() > 0 && item.getDmgMax() > 0)
			return Util.random(item.getDmgMin(), item.getDmgMax());
		else
			return item.getNameIdNumber() == 72 ? 80 : item.getNameIdNumber() == 23 ? 10 : 1;
	}
}
		