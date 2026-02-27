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

public class 순백의티_천연비누 extends 순백의티인장 {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 순백의티_천연비누();
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
		// 봉인 확인.
		if(item.getBress() < 0)
			return false;
		// 착용중이라면 무시.
		if(item.isEquipped()) {
			ChattingController.toChatting(cha, "현재 착용한 장비에 마법을 사용할 수 없습니다.", 20);
			return false;
		}
		//
		switch(item.getItem().getNameIdNumber()) {
			case 15284:
			case 15285:
			case 15286:
			case 15278:
			case 15279:
			case 15280:
			case 15281:
			case 15282:
			case 15283:
			case 15293:
			case 15294:
			case 15295:
			case 15287:
			case 15288:
			case 15289:
			case 15290:
			case 15291:
			case 15292:
				return true;
		}
		// 그 외엔 다 성공
		return false;
	}

	private Item change(ItemInstance item) {
		switch(item.getItem().getNameIdNumber()) {
			case 15284:
			case 15285:
			case 15286:
			case 15278:
			case 15279:
			case 15280:
			case 15281:
			case 15282:
			case 15283:
				return ItemDatabase.find("요정족 티셔츠");
			case 15293:
			case 15294:
			case 15295:
			case 15287:
			case 15288:
			case 15289:
			case 15290:
			case 15291:
			case 15292:
				return ItemDatabase.find("티셔츠");
		}
		return null;
	}
}
