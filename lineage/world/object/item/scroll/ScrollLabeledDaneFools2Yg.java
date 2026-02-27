package lineage.world.object.item.scroll;

import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.Enchant;

public class ScrollLabeledDaneFools2Yg extends Enchant {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollLabeledDaneFools2Yg();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		
		if(cha.getInventory() != null){
			ItemInstance weapon = cha.getInventory().value(cbp.readD());
			/*if(weapon.isEquipped()){
				ChattingController.toChatting(cha, String.format("\\fT 장비를 착용한 상태로 러쉬하실 수 없습니다."), 20);
				return;
			}*/
			if(weapon!=null && weapon.getItem().isEnchant() && weapon instanceof ItemWeaponInstance) {
				int en = toEnchant(cha, weapon);
				if(en != -127) {
					if(cha instanceof PcInstance)
						weapon.toEnchant((PcInstance)cha, en);
					cha.getInventory().count(this, getCount()-1, true);
				}
			}
		}
	}
	//성환.무기인첸트	
	@Override
	protected boolean isEnchant(Character cha, ItemInstance weapon) {
		if(cha.getInventory().getSlot(8) != null)
			cha.getInventory().getSlot(8).toClick(cha, null);
		double enchant_chance = 130D;
		if(bress == 2) {
			for(int i=0 ; i>weapon.getEnLevel() ; --i)
				enchant_chance = enchant_chance * 0.0;
		} else {
			// 안전인첸값을 기준으로 인첸값 만큼 확률 낮추기.
			//기존 확률 4 15.2   5 2.432  6 0.38912 7 8
			/*for(int i=weapon.getItem().getSafeEnchant() ; i<weapon.getEnLevel() ; ++i)
				enchant_chance = enchant_chance * 0.23;*/
			if(weapon.getItem().getSafeEnchant()==0){
				switch(weapon.getEnLevel()){
				case 0: enchant_chance = 55;break;
				case 1: enchant_chance = 37;break;
				case 2: enchant_chance = 26;break;
				case 3: enchant_chance = 14;break;
				case 4: enchant_chance = 7;break;
				case 5: enchant_chance = 6;break;
				case 6: enchant_chance = 5;break;
				default:
					for(int i=weapon.getItem().getSafeEnchant() ; i<weapon.getEnLevel() ; ++i)
						enchant_chance = enchant_chance * 0.23;
					break;
				}
			}else if(weapon.getItem().getSafeEnchant()==6){
				switch(weapon.getEnLevel()){
				case 6: enchant_chance = 55;break;
				case 7: enchant_chance = 37;break;
				case 8: enchant_chance = 26;break;
				case 9: enchant_chance = 6;break;
				case 10: enchant_chance = 5;break;
				default:
					for(int i=weapon.getItem().getSafeEnchant() ; i<weapon.getEnLevel() ; ++i)
						enchant_chance = enchant_chance * 0.23;
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
