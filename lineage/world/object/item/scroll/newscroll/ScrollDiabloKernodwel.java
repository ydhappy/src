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

public class ScrollDiabloKernodwel extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollDiabloKernodwel();
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
			ChattingController.toChatting(cha, String.format("\\fT 무의 방어구는 확인하실 수 없습니다."), 20);
			return;
		}
		
		if(item != null){
			if(!item.isDefinite()){
				item.setDefinite(true);

			//	ChattingController.toChatting(cha, String.format("[안전인첸:%d][힘:%d][덱:%d][콘:%d][인:%d][위:%d][카:%d][Hp:%d][Mp:%d]",
			//	item.getItem().getSafeEnchant(),item.getAdd_Str(),item.getAdd_Dex(),item.getAdd_Con(),item.getAdd_Int(),item.getAdd_Wiz(),item.getAdd_Cha(),item.getAdd_Hp(),item.getAdd_Mana()), 20);
		
				if(item.getItem().getType1().equalsIgnoreCase("weapon")){
					if(item.getItem().getType2().equalsIgnoreCase("etc")){
						ChattingController.toChatting(cha, String.format("\\fT 화살에는 적용되지 않습니다.", 20));
						return;
					}

				//	World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("\\fT %s 님께서 유니크 아이템을 획득하셨습니다.",cha.getName())));
					
					int i = Util.random(1,1000);
					int luck = 5;
					//1 2
					if(i <= luck + cha.getLuck()){ //0.노멀 1.매직 2.레어 3.에픽 4.유니크
					//	World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("\\fT[******] %s 님께서 유니크 아이템을 획득하셨습니다.",cha.getName())));
						ChattingController.toChatting(cha, String.format("\\fT 류니크 아이템을 획득하셨습니다."), 20);
						item.setAdd_Min_Dmg(Util.random(30, 70));
						item.setAdd_Max_Dmg(Util.random(30, 70));
						item.setAdd_Str(Util.random(5, 10));
						item.setAdd_Dex(Util.random(5, 10));
						item.setAdd_Con(Util.random(5, 10));
						item.setAdd_Int(Util.random(5, 10));
						item.setAdd_Wiz(Util.random(5, 10));
						item.setAdd_Cha(Util.random(5, 10));
						item.setAdd_Hp(Util.random(500, 1000));
						item.setAdd_Mana(Util.random(500, 1000));
						item.setAdd_Hpstell(Util.random(14, 17));
						item.setAdd_Manastell(Util.random(4, 5));
					}else if(i >100 && i <= 200){//3 4 5
						//World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("\\fU[******] %s 님께서 에픽 아이템을 획득하셨습니다.",cha.getName())));
						ChattingController.toChatting(cha, String.format("\\fU 에픽 아이템을 획득하셨습니다."), 20);
						item.setAdd_Min_Dmg(Util.random(20, 35));
						item.setAdd_Max_Dmg(Util.random(20, 35));
						item.setAdd_Str(Util.random(3, 8));
						item.setAdd_Dex(Util.random(3, 8));
						item.setAdd_Con(Util.random(3, 8));
						item.setAdd_Int(Util.random(3, 8));
						item.setAdd_Wiz(Util.random(3, 8));
						item.setAdd_Cha(Util.random(3, 8));
						item.setAdd_Hp(Util.random(500, 700));
						item.setAdd_Mana(Util.random(500, 700));
						item.setAdd_Hpstell(Util.random(10, 13));
						item.setAdd_Manastell(Util.random(3, 4));
					}else if(i > 200 && i <= 300){
						ChattingController.toChatting(cha, String.format("\\fV 레어 아이템을 획득하셨습니다."), 20);
						item.setAdd_Min_Dmg(Util.random(15, 30));
						item.setAdd_Max_Dmg(Util.random(15, 30));
						item.setAdd_Str(Util.random(-1, 6));
						item.setAdd_Dex(Util.random(-1, 6));
						item.setAdd_Con(Util.random(-1, 6));
						item.setAdd_Int(Util.random(-1, 6));
						item.setAdd_Wiz(Util.random(-1, 6));
						item.setAdd_Cha(Util.random(-1, 6));
						item.setAdd_Hp(Util.random(400, 650));
						item.setAdd_Mana(Util.random(400, 650));
						item.setAdd_Hpstell(Util.random(6, 9));
						item.setAdd_Manastell(Util.random(2, 3));
					}else if(i > 300 && i <= 500){
						ChattingController.toChatting(cha, String.format("\\fW 매직 아이템을 획득하셨습니다."), 20);
						item.setAdd_Min_Dmg(Util.random(10, 25));
						item.setAdd_Max_Dmg(Util.random(10, 25));
						item.setAdd_Str(Util.random(-2, 5));
						item.setAdd_Dex(Util.random(-2, 5));
						item.setAdd_Con(Util.random(-2, 5));
						item.setAdd_Int(Util.random(-2, 5));
						item.setAdd_Wiz(Util.random(-2, 5));
						item.setAdd_Cha(Util.random(-2, 5));
						item.setAdd_Hp(Util.random(300, 500));
						item.setAdd_Mana(Util.random(300, 500));
						item.setAdd_Hpstell(Util.random(3, 5));
						item.setAdd_Manastell(Util.random(1, 2));
					}else{
						item.setAdd_Min_Dmg(Util.random(-5, 25));
						item.setAdd_Max_Dmg(Util.random(-5, 25));
						item.setAdd_Str(Util.random(-4, 5));
						item.setAdd_Dex(Util.random(-4, 5));
						item.setAdd_Con(Util.random(-4, 5));
						item.setAdd_Int(Util.random(-4, 5));
						item.setAdd_Wiz(Util.random(-4, 5));
						item.setAdd_Cha(Util.random(-4, 5));
						item.setAdd_Hp(Util.random(0, 500));
						item.setAdd_Mana(Util.random(0, 500));
					}
	
				}else if(item.getItem().getType1().equalsIgnoreCase("armor")){
					if(item.getItem().getType2().equalsIgnoreCase("luck")
							|| item.getItem().getType2().equalsIgnoreCase("aden")
							|| item.getItem().getType2().equalsIgnoreCase("ani")
							|| item.getItem().getType2().equalsIgnoreCase("hellf")
							|| item.getItem().getType2().equalsIgnoreCase("gid")){
						ChattingController.toChatting(cha, String.format("\\fT 크로버 장비에는 사용하실 수 없습니다.", 20));
						return;						
					}
					int i = Util.random(1,1000);
					int luck = 5;
					if(i <= luck + cha.getLuck()){ //0.노멀 1.매직 2.레어 3.에픽 4.유니크
						//World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("\\fT[******] %s 님께서 유니크 아이템을 획득하셨습니다.",cha.getName())));
						ChattingController.toChatting(cha, String.format("\\fT 유니크 아이템을 획득하셨습니다."), 20);
						item.setAdd_Str(Util.random(5, 10));
						item.setAdd_Dex(Util.random(5, 10));
						item.setAdd_Con(Util.random(5, 10));
						item.setAdd_Int(Util.random(5, 10));
						item.setAdd_Wiz(Util.random(5, 10));
						item.setAdd_Cha(Util.random(5, 10));
						item.setAdd_Hp(Util.random(300, 500));
						item.setAdd_Mana(Util.random(300, 500));
						item.setAdd_Hpstell(Util.random(14, 17));
						item.setAdd_Manastell(Util.random(4, 5));
					}else if(i > 5 && i <= 100){//3 4 5
						//World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("\\fU[******] %s 님께서 에픽 아이템을 획득하셨습니다.",cha.getName())));
						ChattingController.toChatting(cha, String.format("\\fU 에픽 아이템을 획득하셨습니다."), 20);
						item.setAdd_Str(Util.random(3, 7));
						item.setAdd_Dex(Util.random(3, 7));
						item.setAdd_Con(Util.random(3, 7));
						item.setAdd_Int(Util.random(3, 7));
						item.setAdd_Wiz(Util.random(3, 7));
						item.setAdd_Cha(Util.random(3, 7));
						item.setAdd_Hp(Util.random(200, 400));
						item.setAdd_Mana(Util.random(200, 400));
						item.setAdd_Hpstell(Util.random(10, 13));
						item.setAdd_Manastell(Util.random(3, 4));
					}else if(i > 100 && i <= 200){
						ChattingController.toChatting(cha, String.format("\\fV 레어 아이템을 획득하셨습니다."), 20);
						item.setAdd_Str(Util.random(2, 5));
						item.setAdd_Dex(Util.random(2, 5));
						item.setAdd_Con(Util.random(2, 5));
						item.setAdd_Int(Util.random(2, 5));
						item.setAdd_Wiz(Util.random(2, 5));
						item.setAdd_Cha(Util.random(2, 5));
						item.setAdd_Hp(Util.random(150, 300));
						item.setAdd_Mana(Util.random(150, 300));
						item.setAdd_Hpstell(Util.random(6, 9));
						item.setAdd_Manastell(Util.random(2, 3));
					}else if(i > 200 && i <= 300){
						ChattingController.toChatting(cha, String.format("\\fW 매직 아이템을 획득하셨습니다."), 20);
						item.setAdd_Str(Util.random(-2, 5));
						item.setAdd_Dex(Util.random(-2, 5));
						item.setAdd_Con(Util.random(-2, 5));
						item.setAdd_Int(Util.random(-2, 5));
						item.setAdd_Wiz(Util.random(-2, 5));
						item.setAdd_Cha(Util.random(-2, 5));
						item.setAdd_Hp(Util.random(100, 200));
						item.setAdd_Mana(Util.random(100, 200));
						item.setAdd_Hpstell(Util.random(3, 5));
						item.setAdd_Manastell(Util.random(1, 2));
						
					}else{
						item.setAdd_Str(Util.random(-5, 5));
						item.setAdd_Dex(Util.random(-5, 5));
						item.setAdd_Con(Util.random(-5, 5));
						item.setAdd_Int(Util.random(-5, 5));
						item.setAdd_Wiz(Util.random(-5, 5));
						item.setAdd_Cha(Util.random(-5, 5));
						item.setAdd_Hp(Util.random(0, 200));
						item.setAdd_Mana(Util.random(0, 200));						
					}

			//		item.setAdd_Min_Dmg(Util.random(-7, 15));
			//		item.setAdd_Max_Dmg(Util.random(-7, 15));
					
	
					
				}
				cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
//				cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), item));
				cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));

			}

		/*	ChattingController.toChatting(cha, String.format("[안전인첸:%d][Mp:%d]",
				item.getItem().getSafeEnchant(),item.getAdd_Mana()), 20);*/
			
			if(item.getItem().getType2().equalsIgnoreCase("luck")){
				switch(item.getEnLevel()){
				case 0:ChattingController.toChatting(cha, String.format("\\fT[유니크 찬스 : 5]"), 20);
					break;
				case 1:ChattingController.toChatting(cha, String.format("\\fT[유니크 찬스 : 10]"), 20);
					break;
				case 2:ChattingController.toChatting(cha, String.format("\\fT[유니크 찬스 : 20]"), 20);
					break;
				case 3:ChattingController.toChatting(cha, String.format("\\fT[유니크 찬스 : 30]"), 20);
					break;
				case 4:ChattingController.toChatting(cha, String.format("\\fT[유니크 찬스 : 40]"), 20);
					break;
				case 5:ChattingController.toChatting(cha, String.format("\\fT[유니크 찬스 : 50]"), 20);
					break;
				case 6:ChattingController.toChatting(cha, String.format("\\fT[유니크 찬스 : 60]"), 20);
					break;
				case 7:ChattingController.toChatting(cha, String.format("\\fT[유니크 찬스 : 70]"), 20);
					break;
				case 8:ChattingController.toChatting(cha, String.format("\\fT[유니크 찬스 : 80]"), 20);
					break;
				case 9:ChattingController.toChatting(cha, String.format("\\fT[유니크 찬스 : 90]"), 20);
					break;
				case 10:ChattingController.toChatting(cha, String.format("\\fT[유니크 찬스 : 100]"), 20);
					break;
				}
			}
			if(item.getItem().getType2().equalsIgnoreCase("aden")){
				ChattingController.toChatting(cha, String.format("\\fT[아데나 획득 찬스 증가 아이템]"), 20);
				ChattingController.toChatting(cha, String.format("\\fT[인첸트0~3 : 1배]"), 20);
				ChattingController.toChatting(cha, String.format("\\fT[인첸트4~6 : 2배]"), 20);
				ChattingController.toChatting(cha, String.format("\\fT[인첸트7 : 3배]"), 20);
				
			}
		//	Definite d = DefiniteDatabase.find(item.getItem().getNameIdNumber());
		//	if(d != null)
		//		cha.toSender(S_InventoryIdentify.clone(BasePacketPooling.getPool(S_InventoryIdentify.class), item, d.getType(), d.getMessage()));

			//itemabilit
		//	ChattingController.toChatting(cha, String.format("\\fT[안전인첸:%d] ,[피틱:%d] ,[엠틱:%d], [추타:%d]",item.getItem().getSafeEnchant(),item.getItem().getTicHp(),item.getItem().getTicMp(), item.getItem().getAddDmg()) , 20);
			
		}


		cha.getInventory().count(this, getCount()-1, true);
	}

}
