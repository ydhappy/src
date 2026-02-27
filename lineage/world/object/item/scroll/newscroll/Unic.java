package lineage.world.object.item.scroll.newscroll;

import lineage.util.Util;
import lineage.bean.database.Definite;
import lineage.database.DefiniteDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Inventory;
import lineage.network.packet.server.S_InventoryBress;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryIdentify;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class Unic extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Unic();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		ItemInstance item = cha.getInventory().value(cbp.readD());
		if(item.isEquipped()){
				ChattingController.toChatting(cha, String.format("\\fT 장비를 착용한 상태로 확인 하실 수 없습니다."), 20);
				return;
			}
		if(item.getName().equalsIgnoreCase("무의 투구")||item.getName().equalsIgnoreCase("무의 갑옷")||item.getName().equalsIgnoreCase("무의 망토")
						||item.getName().equalsIgnoreCase("무의 장갑")||item.getName().equalsIgnoreCase("무의 부츠")){
			ChattingController.toChatting(cha, String.format("\\fT 무의 장비는 불가능합니다."), 20);
			return;
		}
			
		
		if(item != null){
			
				item.setDefinite(true);

				if(item.getItem().getType1().equalsIgnoreCase("weapon")){
					if(item.getItem().getType2().equalsIgnoreCase("etc")){
						ChattingController.toChatting(cha, String.format("\\fT 화살에는 적용되지 않습니다.", 20));
						return;
					}
					ChattingController.toChatting(cha, String.format("\\fT 유니크 최고급으로 변경되었습니다.", 20));
						item.setAdd_Min_Dmg(Util.random(69, 70));
						item.setAdd_Max_Dmg(Util.random(69, 70));
						item.setAdd_Str(Util.random(9, 10));
						item.setAdd_Dex(Util.random(9, 10));
						item.setAdd_Con(Util.random(9, 10));
						item.setAdd_Int(Util.random(9, 10));
						item.setAdd_Wiz(Util.random(9, 10));
						item.setAdd_Cha(Util.random(9, 10));
						item.setAdd_Hp(Util.random(990, 1000));
						item.setAdd_Mana(Util.random(990, 1000));
						item.setAdd_Hpstell(Util.random(14, 17));
						item.setAdd_Manastell(Util.random(4, 5));
				
					
				}else if(item.getItem().getType1().equalsIgnoreCase("armor")){
					if(item.getItem().getType2().equalsIgnoreCase("luck")
					|| item.getItem().getType2().equalsIgnoreCase("aden")){
						ChattingController.toChatting(cha, String.format("\\fT 크로버 장비에는 사용하실 수 없습니다.", 20));
						return;						
					}
					ChattingController.toChatting(cha, String.format("\\fT 유니크 최고급으로 변경되었습니다.", 20));					
						item.setAdd_Str(Util.random(9, 10));
						item.setAdd_Dex(Util.random(9, 10));
						item.setAdd_Con(Util.random(9, 10));
						item.setAdd_Int(Util.random(9, 10));
						item.setAdd_Wiz(Util.random(9, 10));
						item.setAdd_Cha(Util.random(9, 10));
						item.setAdd_Hp(Util.random(490, 500));
						item.setAdd_Mana(Util.random(490, 500));
						item.setAdd_Hpstell(Util.random(14, 17));
						item.setAdd_Manastell(Util.random(4, 5));
				}
				
				cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
				cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));

			

			ChattingController.toChatting(cha, String.format("[안전인첸:%d][Mp:%d]",
				item.getItem().getSafeEnchant(),item.getAdd_Mana()), 20);
			
		}


		cha.getInventory().count(this, getCount()-1, true);
	}

}
