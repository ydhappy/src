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

public class ScrollLabeledZelgoMer2Ju extends Enchant {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollLabeledZelgoMer2Ju();
		return item;
	}
//고대인의 주술 두루마리
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
		if(armor.getItem().getType2().equalsIgnoreCase("aden")
				||armor.getItem().getType2().equalsIgnoreCase("luck")
				||armor.getItem().getType2().equalsIgnoreCase("aden")
				||armor.getItem().getType2().equalsIgnoreCase("hellf")
				||armor.getItem().getType2().equalsIgnoreCase("ani")
				||armor.getItem().getType2().equalsIgnoreCase("gid")
				||armor.getItem().getType2().equalsIgnoreCase("earring")
				||armor.getItem().getType2().equalsIgnoreCase("necklace")
				||armor.getItem().getType2().equalsIgnoreCase("ring")
				||armor.getItem().getType2().equalsIgnoreCase("belt")){
			return false;
		}
		
		if(!armor.getItem().isEnchant()){
			if(Lineage.item_accessory_bless_enchant && armor.getBress()==0)
				return	InventoryController.isAccessory(armor);
			else
				return false;
		}
		return super.isEnchant(armor);
	}
	
//성환.방어구인첸트	
@Override
protected boolean isEnchant(Character cha, ItemInstance armor) {
	double enchant_chance = 130D;
	if(bress == 2) {
		for(int i=0 ; i>armor.getEnLevel() ; --i)
			enchant_chance = enchant_chance * 0.0;
	} else {
		// 안전인첸값을 기준으로 인첸값 만큼 확률 낮추기.
		//기존 확률 4 15.2   5 2.432  6 0.38912 7 8
		/*for(int i=armor.getItem().getSafeEnchant() ; i<armor.getEnLevel() ; ++i)
			enchant_chance = enchant_chance * 0.16;*/
		if(armor.getItem().getSafeEnchant()==0){
			switch(armor.getEnLevel()){
			case 0: enchant_chance = 37;break;
			case 1: enchant_chance = 17;break;
			case 2: enchant_chance = 16;break;
			case 3: enchant_chance = 14;break;
			case 4: enchant_chance = 7;break;
			case 5: enchant_chance = 6;break;
			case 6: enchant_chance = 5;break;
			default:
				for(int i=armor.getItem().getSafeEnchant() ; i<armor.getEnLevel() ; ++i)
					enchant_chance = enchant_chance * 0.16;
				break;
			}
		}else if(armor.getItem().getSafeEnchant()==4){
			switch(armor.getEnLevel()){
			case 4: enchant_chance = 37;break;
			case 5: enchant_chance = 17;break;
			case 6: enchant_chance = 16;break;
			case 7: enchant_chance = 14;break;
			case 8: enchant_chance = 7;break;
			case 9: enchant_chance = 6;break;
			case 10: enchant_chance = 5;break;
			default:
				for(int i=armor.getItem().getSafeEnchant() ; i<armor.getEnLevel() ; ++i)
					enchant_chance = enchant_chance * 0.16;
				break;
			}
		}else if(armor.getItem().getSafeEnchant()==6){
			switch(armor.getEnLevel()){
			case 6: enchant_chance = 16;break;
			case 7: enchant_chance = 14;break;
			case 8: enchant_chance = 7;break;
			case 9: enchant_chance = 6;break;
			case 10: enchant_chance = 5;break;
			default:
				for(int i=armor.getItem().getSafeEnchant() ; i<armor.getEnLevel() ; ++i)
					enchant_chance = enchant_chance * 0.16;
				break;
			}
		}else{
			ChattingController.toChatting(cha, "문제상황 발생. 운영자에게 문의해주세요!(인첸부분)");
		}
	}
	// 인첸트 성공 확률 추출.
	return enchant_chance*Lineage.rate_enchant >= Util.random(0.0D, 100D);
}

}

