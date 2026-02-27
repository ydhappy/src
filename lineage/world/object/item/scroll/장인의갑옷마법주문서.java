package lineage.world.object.item.scroll;

import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_SoundEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.InventoryController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.Enchant;

public class 장인의갑옷마법주문서 extends Enchant {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 장인의갑옷마법주문서();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if(cha.getInventory() != null){
			ItemInstance armor = cha.getInventory().value(cbp.readD());
			if (armor == null) {
				return;
			} else if (!armor.getItem().getType1().equalsIgnoreCase("armor") || 
					InventoryController.isAccessory(armor)) {
				ChattingController.toChatting(cha, "+7 이상 방어구에 사용이 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			int en = toEnchant(cha, armor);
			if (en != -127) {
				if(en == 0) 
					cha.toSender(new S_Message(160, EnMsg[0], EnMsg[1], EnMsg[2]));
			    //cha.toSender(new S_SoundEffect( 4470), true);
			    //cha.toSender(new S_SoundEffect( 4471), true);


				// 인첸 실패될경우 무기증발 안되게 하기 위해.
				if(en <= 0)
					en = -127;
				// 
				if (cha instanceof PcInstance)
					armor.toEnchant((PcInstance) cha, en);

				cha.getInventory().count(this, getCount() - 1, true);
			}
		}
	}


	@Override
	public void toEnchant(PcInstance pc, int en) {
		if (getItem() == null)
			return;

		super.toEnchant(pc, en);
		if(en == -127) {
			ChattingController.toChatting(pc, "아무일도 일어나지 않았습니다.", Lineage.CHATTING_MODE_MESSAGE);

		}
	}

	@Override
	protected int toEnchant(Character cha, ItemInstance item) {
		if (item.getItem().getMaterial() != 6 && /*item.getItem().getMaterial() != 18 &&*/ item.getEnLevel() == 7)
			return super.toEnchant(cha, item);
		if (item.getItem().getMaterial() != 6 && /*item.getItem().getMaterial() != 18 &&*/ item.getEnLevel() == 8)
			return super.toEnchant(cha, item);
		if (item.getItem().getMaterial() != 6 && /*item.getItem().getMaterial() != 18 &&*/ item.getEnLevel() == 9)
			return super.toEnchant(cha, item);
		if (item.getItem().getMaterial() != 4 && /*item.getItem().getMaterial() != 18 &&*/ item.getEnLevel() == 7)
			return super.toEnchant(cha, item);
		if (item.getItem().getMaterial() != 4 && /*item.getItem().getMaterial() != 18 &&*/ item.getEnLevel() == 8)
			return super.toEnchant(cha, item);
		if (item.getItem().getMaterial() != 4 && /*item.getItem().getMaterial() != 18 &&*/ item.getEnLevel() == 9)
			return super.toEnchant(cha, item);
		// \f1아무일도 일어나지 않았습니다.
		ChattingController.toChatting(cha, "아무일도 일어나지 않았습니다.", Lineage.CHATTING_MODE_MESSAGE);
		return -127;
	}

	protected boolean isEnchant(Character cha, ItemInstance item) {
//		Random rnd = new Random(System.currentTimeMillis());
//		int chance = rnd.nextInt(100);
//		if ( chance < 10) return true;
//		return false;
		return Util.random(0, 100) < Lineage.jangin_armor_7;
	}
}
