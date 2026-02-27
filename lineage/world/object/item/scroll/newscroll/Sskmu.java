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

public class Sskmu extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Sskmu();
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
				
				cnt = cnt + 1;
				if(item.getName().equalsIgnoreCase("무의 투구")||item.getName().equalsIgnoreCase("무의 갑옷")||item.getName().equalsIgnoreCase("무의 망토")
						||item.getName().equalsIgnoreCase("무의 장갑")||item.getName().equalsIgnoreCase("무의 부츠")){
					if(!item.getItem().getType2().equalsIgnoreCase("luck")
							|| !item.getItem().getType2().equalsIgnoreCase("aden")){
								
					
					int str = Util.random(3,12);
					int dex = Util.random(3,12);
					int con = Util.random(3,12);
					int i = Util.random(3,12);
					int wiz = Util.random(3,12);
					int c = Util.random(3,12);
					int hp = Util.random(200,700);
					int mana = Util.random(200,700);
					
					
					if(Util.random(0, 1000)<=3){
						str =Util.random(8, 12); dex = Util.random(8, 12);
						con =Util.random(8, 12); i =Util.random(8, 12); wiz =Util.random(8, 12); c = Util.random(8, 12);
						
						int rnd = Util.random(1, 3);
						int rndhp = Util.random(150, 200);
						item.setDefinite(true);
						
						item.setAdd_Str(str+rnd);
						item.setAdd_Dex(dex+rnd);
						item.setAdd_Con(con+rnd);
						item.setAdd_Int(i+rnd);
						item.setAdd_Wiz(wiz+rnd);
						item.setAdd_Cha(c+rnd);
						item.setAdd_Hp(hp + rndhp);
						item.setAdd_Mana(mana);
						item.setAdd_Hpstell(Util.random(14, 17));
						item.setAdd_Manastell(Util.random(4, 5));
						
						ChattingController.toChatting(cha, String.format("\\fW 최상급 무의 유니크 방어구가 생성되었습니다."), 20);
					}else{
					
					item.setDefinite(true);
						
						item.setAdd_Str(str);
						item.setAdd_Dex(dex);
						item.setAdd_Con(con);
						item.setAdd_Int(i);
						item.setAdd_Wiz(wiz);
						item.setAdd_Cha(c);
						item.setAdd_Hp(hp);
						item.setAdd_Mana(mana);
						item.setAdd_Hpstell(Util.random(14, 17));
						item.setAdd_Manastell(Util.random(4, 5));
					}			
					cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
					cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
					}
				}
				

			}
			if(cnt>=5){
				ChattingController.toChatting(cha, String.format("\\fW 최대 5개까지 확인 가능합니다."), 20);
				break;
			}
			
		}
		cha.getInventory().count(this, getCount()-1, true);

	}

}
