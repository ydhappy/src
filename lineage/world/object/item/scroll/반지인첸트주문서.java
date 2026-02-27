package lineage.world.object.item.scroll;

import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.InventoryController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.Enchant;

public class 반지인첸트주문서 extends Enchant {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 반지인첸트주문서();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if(cha.getInventory() != null){
			ItemInstance armor = cha.getInventory().value(cbp.readD());
			/*if(armor.isEquipped()){
				ChattingController.toChatting(cha, String.format("\\fT 장비를 착용한 상태로 러쉬하실 수 없습니다."), 20);
				return;
			}*/
			if(armor.getItem().getType2().equalsIgnoreCase("luck")
					|| armor.getItem().getType2().equalsIgnoreCase("aden")){
				return;
			}
			
			
			if(armor!=null && isEnchant(armor) && armor instanceof ItemArmorInstance){
				int en = toEnchant(cha, armor);
				if(en != -127) {
					if(cha instanceof PcInstance)
						armor.toEnchant((PcInstance)cha, en);
					cha.getInventory().count(this, getCount()-1, true);
				}
			}
		}
	}
	
	/**
	 * 인첸트가 가능한지 확인해주는 함수.
	 * @param armor
	 * @return
	 */
	@Override
	protected boolean isEnchant(ItemInstance armor) {
		if(!(armor instanceof ItemArmorInstance))
			return false;
		
		if(armor.getItem().getType2().equalsIgnoreCase("ring") && armor.getEnLevel() <= Lineage.Orim_en_level){
			return true;
		}else{
			return false;
		}

	}
	
//성환.방어구인첸트	
@Override
protected boolean isEnchant(Character cha, ItemInstance armor) {
	double enchant_chance = 95D;
	if(bress == 2) {
		for(int i=0 ; i>armor.getEnLevel() ; --i)
			enchant_chance = enchant_chance * 0.0;
	} else {
		// 안전인첸값을 기준으로 인첸값 만큼 확률 낮추기.
		//기존 확률 4 15.2   5 2.432  6 0.38912 7 8
		/*for(int i=armor.getItem().getSafeEnchant() ; i<armor.getEnLevel() ; ++i)
			enchant_chance = enchant_chance * 0.16;*/
		switch(armor.getEnLevel()){
			case 0: enchant_chance = Lineage.acc0;break;
			case 1: enchant_chance = Lineage.acc1;break;
			case 2: enchant_chance = Lineage.acc2;break;
			case 3: enchant_chance = Lineage.acc3;break;
			case 4: enchant_chance = Lineage.acc4;break;
			case 5: enchant_chance = Lineage.acc5;break;
			case 6: enchant_chance = Lineage.acc6;break;
			case 7: enchant_chance = Lineage.acc7;break;
		}
	}
	// 인첸트 성공 확률 추출.
	return enchant_chance*Lineage.rate_enchant >= Util.random(0.0D, 100D);
}

}

