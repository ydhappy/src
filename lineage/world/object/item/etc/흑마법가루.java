package lineage.world.object.item.etc;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.util.Util;
import lineage.world.controller.CraftController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 흑마법가루 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 흑마법가루();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		//
		if(cha.getInventory() != null) {
			ItemInstance item = cha.getInventory().value(cbp.readD());
			if(is(cha, item)) {
				if(chance())
					CraftController.toCraft(cha, change(item.getItem()), 1, true, 0);
				else
					// \f1%0%s 증발되어 사라집니다.
					cha.toSender( S_Message.clone(BasePacketPooling.getPool(S_Message.class), 158, item.getName()) );
				//
				cha.getInventory().count(item, item.getCount()-1, true);
				cha.getInventory().count(this, getCount()-1, true);
				return;
			}
		}
		// 아무일도 일어나지 않았습니다.
		cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
	}
	
	/**
	 * 확률처리 메서드.
	 * @return
	 */
	private boolean chance() {
		return Util.random(0, 99) < 50;
	}
	
	/**
	 * 봉인된 역사서가 어떤 라스타바드의 역사서로 변해야하는지 찾아서 확인해줌.
	 * @param item
	 * @return
	 */
	private Item change(Item item) {
		switch(item.getNameIdNumber()) {
			case 4601:	// 봉인된 역사서 2장 -> 라스타바드의 역사서 2장
				return ItemDatabase.find(4612);
			case 4602:	// 봉인된 역사서 3장 -> 라스타바드의 역사서 3장
				return ItemDatabase.find(4695);
			case 4603:	// 봉인된 역사서 4장 -> 라스타바드의 역사서 4장
				return ItemDatabase.find(4696);
			case 4604:	// 봉인된 역사서 5장 -> 라스타바드의 역사서 5장
				return ItemDatabase.find(4697);
			case 4605:	// 봉인된 역사서 6장 -> 라스타바드의 역사서 6장
				return ItemDatabase.find(4698);
			case 4606:	// 봉인된 역사서 7장 -> 라스타바드의 역사서 7장
				return ItemDatabase.find(4699);
			case 4607:	// 봉인된 역사서 8장 -> 라스타바드의 역사서 8장
				return ItemDatabase.find(4700);
			default:	// 봉인된 역사서 1장 -> 라스타바드의 역사서 1장
				return ItemDatabase.find(4611);
		}
	}

	/**
	 * 처리를 해도 되는지 확인해주는 함수.
	 * @param item
	 * @return
	 */
	private boolean is(Character cha, ItemInstance item) {
		// 없는 아이템은 무시
		if(item == null)
			return false;
		if(item.getItem().getNameIdNumber()<4600 || item.getItem().getNameIdNumber()>4607)
			return false;
		// 그 외엔 다 성공
		return true;
	}

}
