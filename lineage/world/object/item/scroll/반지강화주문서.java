package lineage.world.object.item.scroll;

import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 반지강화주문서 extends ScrollLabeledDaneFools {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 반지강화주문서();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if(cha.getInventory() != null){
			ItemInstance armor = cha.getInventory().value(cbp.readD());
			if(armor!=null && armor instanceof ItemArmorInstance){
				int rnd = toEnchant(cha, armor);
				if(rnd != -127) {
					if(cha instanceof PcInstance)
						armor.toEnchant((PcInstance)cha, rnd);
					cha.getInventory().count(this, getCount()-1, true);
				}
			}
		}
	}

	@Override
	protected int toEnchant(Character cha, ItemInstance item){
		if(item.getItem().getType2().equalsIgnoreCase("ring")) {

				// 최대인첸 8까지 제한됨.
				if(item.getEnLevel() >= 5) {
					ChattingController.toChatting(cha, "아무일도 일어나지 않았습니다.", 20);
					return -127;
				}
				
				//
				return super.toEnchant(cha, item);
				
		}else {
			ChattingController.toChatting(cha, "아무일도 일어나지 않았습니다.", 20);
			return -127;
			}
	}
	
	@Override
	protected boolean isEnchant(Character cha, ItemInstance item) {
		double enchant_chance = 0;
		if (bress == 2) {
			for (int i = 0; i > item.getEnLevel(); --i)
				enchant_chance = enchant_chance * 0.20;
		} else {
			// 안전인첸값을 기준으로 인첸값 만큼 확률 낮추기.
			/*
			 * for(int i=item.getItem().getSafeEnchant() ; i<item.getEnLevel() ;
			 * ++i) enchant_chance = enchant_chance * 0.35;
			 */
			switch (item.getEnLevel()) {
			case 0: enchant_chance = Lineage.acc0;
			break;
		case 1: enchant_chance = Lineage.acc1;
			break;
		case 2: enchant_chance = Lineage.acc2;
			break;
		case 3: enchant_chance = Lineage.acc3;
			break;
		case 4: enchant_chance = Lineage.acc4;
			break;
		case 5: enchant_chance = Lineage.acc5;
			break;
		case 6: enchant_chance = Lineage.acc0;
			break;
		case 7: enchant_chance = 20;
			break;				
		case 8: enchant_chance = 10;
			break;	

			}
		}
		// 인첸트 성공 확률 추출.
		return Util.random(1, 100) <= enchant_chance*Lineage.rate_enchant;
	}

}