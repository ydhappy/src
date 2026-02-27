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

public class SuperScrollLabeledKernodwel extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new SuperScrollLabeledKernodwel();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		//인벤검사하기
		int cnt = 0;
		for(ItemInstance item : cha.getInventory().getList()){
			if(!item.isDefinite() && 
				(((item.getItem().getType1().equalsIgnoreCase("weapon")&& (!item.getItem().getType2().equalsIgnoreCase("etc")) || item.getItem().getType1().equalsIgnoreCase("armor") )))){
				if(item.isEquipped()){
					ChattingController.toChatting(cha, String.format("\\fT 미확인 장비를 착용한 상태로 사용하실 수 없습니다."), 20);
					return;
				}
				
				if(item.getName().equalsIgnoreCase("무의 투구")||item.getName().equalsIgnoreCase("무의 갑옷")||item.getName().equalsIgnoreCase("무의 망토")
						||item.getName().equalsIgnoreCase("무의 장갑")||item.getName().equalsIgnoreCase("무의 부츠")){
					ChattingController.toChatting(cha, String.format("\\fT 무의 방어구는 확인하실 수 없습니다."), 20);
				}else{
				
				cnt = cnt + 1;
				if(item.getItem().getType1().equalsIgnoreCase("weapon")){
					if(!(item.getItem().getType2().equalsIgnoreCase("etc"))){
						item.setDefinite(true);
						int i = Util.random(1,1000);
						int luck = 5;
						//1 2
					if(i <= luck + cha.getLuck()){ //0.노멀 1.매직 2.레어 3.에픽 4.유니크
					//	World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("\\fT[******] %s 님께서 유니크 아이템을 획득하셨습니다.",cha.getName())));
						ChattingController.toChatting(cha, String.format("\\fT 유니크 아이템을 획득하셨습니다."), 20);
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
						ChattingController.toChatting(cha, String.format("\\fV 에픽 아이템을 획득하셨습니다."), 20);
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
					cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
					cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
					}
	
				}else if(item.getItem().getType1().equalsIgnoreCase("armor")){
					if(!item.getItem().getType2().equalsIgnoreCase("luck")
							|| !item.getItem().getType2().equalsIgnoreCase("aden")){
								
					int i = Util.random(1,1000);
					int luck = 5;
					item.setDefinite(true);
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
						
					}else if(i > 100 && i <= 200){//3 4 5
						ChattingController.toChatting(cha, String.format("\\fV 에픽 아이템을 획득하셨습니다."), 20);
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
					}else if(i > 200 && i <= 300){
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
					}else if(i > 300 && i <= 500){
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
					cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
					cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
					}
				}
				}

			}
			if(cnt>=30){
				ChattingController.toChatting(cha, String.format("\\fW 최대 30개까지 확인 가능합니다."), 20);
				break;
			}
			
		}
		cha.getInventory().count(this, getCount()-1, true);

	}

}
