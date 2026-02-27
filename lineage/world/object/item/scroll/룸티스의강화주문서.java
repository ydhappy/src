package lineage.world.object.item.scroll;

import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.earring.룸티스의검은빛귀걸이;
import lineage.world.object.item.earring.룸티스의보랏빛귀걸이;
import lineage.world.object.item.earring.룸티스의붉은빛귀걸이;
import lineage.world.object.item.earring.룸티스의푸른빛귀걸이;

public class 룸티스의강화주문서 extends ScrollLabeledDaneFools {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 룸티스의강화주문서();
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
		//
		if(	!(item instanceof 룸티스의보랏빛귀걸이) &&
			!(item instanceof 룸티스의붉은빛귀걸이) &&
			!(item instanceof 룸티스의푸른빛귀걸이) &&
			!(item instanceof 룸티스의검은빛귀걸이)	) {
			ChattingController.toChatting(cha, "아무일도 일어나지 않았습니다.", 20);
			return -127;
		}
		// 최대인첸 8까지 제한됨.
		if(item.getEnLevel() >= 8) {
			ChattingController.toChatting(cha, "아무일도 일어나지 않았습니다.", 20);
			return -127;
		}
		//
		return super.toEnchant(cha, item);
	}
	
	@Override
	protected boolean isEnchant(Character cha, ItemInstance item) {
		double enchant_chance = 80D;
		if(bress == 2) {
			for(int i=0 ; i>item.getEnLevel() ; --i)
				enchant_chance = enchant_chance * 0.20;
		} else {
			// 안전인첸값을 기준으로 인첸값 만큼 확률 낮추기.
			for(int i=item.getItem().getSafeEnchant() ; i<item.getEnLevel() ; ++i)
				enchant_chance = enchant_chance * 0.35;
		}
		// 인첸트 성공 확률 추출.
		return Util.random(0.0D, enchant_chance*Lineage.rate_enchant) >= Util.random(0.0D, 90D);
	}
}
