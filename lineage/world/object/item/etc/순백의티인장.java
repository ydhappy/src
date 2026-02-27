package lineage.world.object.item.etc;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class 순백의티인장 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 순백의티인장();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		//
		if(cha.getInventory() != null){
			ItemInstance item = cha.getInventory().value(cbp.readD());
			if(is(cha, item)) {
				//
				CraftController.toCraft(cha, change(item), 1, true, item.getEnLevel());
				cha.getInventory().count(item, item.getCount()-1, true);
				cha.getInventory().count(this, getCount()-1, true);
				return;
			}
		}
		// 아무일도 일어나지 않았습니다.
		cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
	}

	/**
	 * 인장 처리를 해도 되는지 확인해주는 함수.
	 * @param item
	 * @return
	 */
	private boolean is(Character cha, ItemInstance item) {
		// 없는 아이템은 무시
		if(item == null)
			return false;
		// 방어구가 아니라면 무시
		if(!(item instanceof ItemArmorInstance))
			return false;
		// 요정족티, 티 가 아니라면 무시.
		if(item.getItem().getNameIdNumber()!=2075 && item.getItem().getNameIdNumber()!=168)
			return false;
		// 봉인 확인.
		if(item.getBress() < 0)
			return false;
		// 착용중이라면 무시.
		if(item.isEquipped()) {
			ChattingController.toChatting(cha, "현재 착용한 장비에 마법을 사용할 수 없습니다.", 20);
			return false;
		}
		// 그 외엔 다 성공
		return true;
	}
	
	private Item change(ItemInstance item) {
		switch(getItem().getNameIdNumber()) {
			case 15275:	// 순백의 티 인장:HOLD/STR
				if(item.getItem().getNameIdNumber() == 2075)
					return ItemDatabase.find("순백의 요정족 티:HOLD/STR");
				else
					return ItemDatabase.find("순백의 티:HOLD/STR");
			case 15276:	// 순백의 티 인장:HOLD/DEX
				if(item.getItem().getNameIdNumber() == 2075)
					return ItemDatabase.find("순백의 요정족 티:HOLD/DEX");
				else
					return ItemDatabase.find("순백의 티:HOLD/DEX");
			case 15277:	// 순백의 티 인장:HOLD/INT
				if(item.getItem().getNameIdNumber() == 2075)
					return ItemDatabase.find("순백의 요정족 티:HOLD/INT");
				else
					return ItemDatabase.find("순백의 티:HOLD/INT");
			case 15269:	// 순백의 티 인장:MR/STR
				if(item.getItem().getNameIdNumber() == 2075)
					return ItemDatabase.find("순백의 요정족 티:MR/STR");
				else
					return ItemDatabase.find("순백의 티:MR/STR");
			case 15270:	// 순백의 티 인장:MR/DEX
				if(item.getItem().getNameIdNumber() == 2075)
					return ItemDatabase.find("순백의 요정족 티:MR/DEX");
				else
					return ItemDatabase.find("순백의 티:MR/DEX");
			case 15271:	// 순백의 티 인장:MR/INT
				if(item.getItem().getNameIdNumber() == 2075)
					return ItemDatabase.find("순백의 요정족 티:MR/INT");
				else
					return ItemDatabase.find("순백의 티:MR/INT");
			case 15272:	// 순백의 티 인장:STUN/STR
				if(item.getItem().getNameIdNumber() == 2075)
					return ItemDatabase.find("순백의 요정족 티:STUN/STR");
				else
					return ItemDatabase.find("순백의 티:STUN/STR");
			case 15273:	// 순백의 티 인장:STUN/DEX
				if(item.getItem().getNameIdNumber() == 2075)
					return ItemDatabase.find("순백의 요정족 티:STUN/DEX");
				else
					return ItemDatabase.find("순백의 티:STUN/DEX");
			case 15274:	// 순백의 티 인장:STUN/INT
				if(item.getItem().getNameIdNumber() == 2075)
					return ItemDatabase.find("순백의 요정족 티:STUN/INT");
				else
					return ItemDatabase.find("순백의 티:STUN/INT");
		}
		return null;
	}
}
