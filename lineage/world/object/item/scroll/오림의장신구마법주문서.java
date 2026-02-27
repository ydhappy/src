package lineage.world.object.item.scroll;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.InventoryController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.Enchant;
import lineage.world.object.item.earring.룸티스의검은빛귀걸이;

public class 오림의장신구마법주문서 extends Enchant {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 오림의장신구마법주문서();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if(cha.getInventory() != null){
			ItemInstance armor = cha.getInventory().value(cbp.readD());
			if(armor.getItem().getType2().equalsIgnoreCase("ring")){
				ChattingController.toChatting(cha, "반지에는 사용할 수 없습니다.");
				return;
			}
			
			if(armor!=null && armor instanceof ItemArmorInstance){
				int rnd = toEnchant(cha, armor);
				if(rnd != 0) {
					if(cha instanceof PcInstance)
						armor.toEnchant((PcInstance)cha, rnd);
					cha.getInventory().count(this, getCount()-1, true);
				}
			}
		}
	}

	@Override
	protected int toEnchant(Character cha, ItemInstance item){
		if(InventoryController.isAccessory(item)) {
			//
			switch(item.getItem().getNameIdNumber()) {
				case 11994:	// 룸티스의 붉은빛 귀걸이
				case 11995:	// 룸티스의 푸른빛 귀걸이
				case 11996:	// 룸티스의 보랏빛 귀걸이
				case 16896:	// 스냅퍼의 체력 반지
				case 16897:	// 스냅퍼의 마법저항 반지
				case 16898:	// 스냅퍼의 집중 반지
				case 16899:	// 스냅퍼의 마나 반지
				case 16900:	// 스냅퍼의 회복 반지
				case 16901:	// 스냅퍼의 지혜 반지
				case 16902:	// 스냅퍼의 용사 반지
					// \f1아무일도 일어나지 않았습니다.
					cha.toSender( S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79) );
					return 0;
			}
			if(item instanceof 룸티스의검은빛귀걸이){
				// \f1아무일도 일어나지 않았습니다.
				cha.toSender( S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79) );
				return 0;
			}
			// 최대인첸 8까지 제한됨.
			if(item.getEnLevel() >= Lineage.Orim_en_level) {
				ChattingController.toChatting(cha, "아무일도 일어나지 않았습니다.", 20);
				return 0;
			}
			// 보통
			//	: -1, 1
			// 축
			//	: 1
			int rnd = super.toEnchant(cha, item);
			if(getBress() == 0)
				rnd = rnd==0 ? -127 : rnd;
			else
				rnd = rnd==0 ? -1 : rnd;
			//
			if(getBress()==0 || item.getEnLevel()+rnd >= 0)
				return rnd;
		}
		// \f1아무일도 일어나지 않았습니다.
		cha.toSender( S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79) );
		return 0;
	}
	
	@Override
	protected boolean isEnchant(Character cha, ItemInstance item) {
		double enchant_chance = 95D;
		if(bress == 2) {
			for(int i=0 ; i>item.getEnLevel() ; --i)
				enchant_chance = enchant_chance * 0.0;
		} else {
			switch(item.getEnLevel()){
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
		return enchant_chance*Lineage.rate_enchant >= Util.random(0.0D, 55D);
	}

}
