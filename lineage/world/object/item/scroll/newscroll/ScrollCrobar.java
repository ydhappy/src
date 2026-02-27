package lineage.world.object.item.scroll.newscroll;

import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.Enchant;

public class ScrollCrobar extends Enchant {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollCrobar();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if(cha.getInventory() != null){
			ItemInstance armor = cha.getInventory().value(cbp.readD());
			if(armor.isEquipped()){
				ChattingController.toChatting(cha, String.format("\\fT 장비를 착용한 상태로 러쉬하실 수 없습니다."), 20);
				return;
			}
			if(armor.getItem().getType2().equalsIgnoreCase("luck")
					|| armor.getItem().getType2().equalsIgnoreCase("aden")){
				if(armor!=null && isEnchant(armor) && armor instanceof ItemArmorInstance){
					if(cha instanceof PcInstance)
						armor.toEnchant((PcInstance)cha, toEnchant(cha, armor));
					cha.getInventory().count(this, getCount()-1, true);
				}

			}else{
				ChattingController.toChatting(cha, String.format("\\fT 크로버에만 인첸 가능 합니다."), 20);
				return;
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
		if(!armor.getItem().isEnchant()){
			if(armor.getItem().getType2().equalsIgnoreCase("necklace") || 
						armor.getItem().getType2().equalsIgnoreCase("ring") || 
						armor.getItem().getType2().equalsIgnoreCase("belt") || 
						armor.getItem().getType2().equalsIgnoreCase("earring")
				     || armor.getItem().getType2().equalsIgnoreCase("guarder")){

				return false;
			}else{
				return false;
			}
		}
		return super.isEnchant(armor);
	}
}
