package lineage.world.object.item.scroll.newscroll;

import lineage.util.Util;
import lineage.bean.database.Definite;
import lineage.bean.database.Item;
import lineage.database.DefiniteDatabase;
import lineage.database.ItemDatabase;
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

public class ScrollRoonReset extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollRoonReset();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		ItemInstance item = cha.getInventory().value(cbp.readD());
		if(item.isEquipped()){
				ChattingController.toChatting(cha, String.format("\\fT 장비를 착용한 상태로 룬 초기화를 할 수 없습니다."), 20);
				return;
			}
		if(!item.isDefinite()){
			ChattingController.toChatting(cha, String.format("\\fT 확인 된 장비에 작업을 하시기 바랍니다."), 20);
			return;
		}
		
		if(item.getFour()>10){
			switch(item.getFour()){
			case 11:item.setAdd_Wiz(item.getAdd_Wiz() - 2);
			item.setAdd_Int(item.getAdd_Int() - 6);
				break;
			case 12:item.setAdd_Dex(item.getAdd_Dex() - 2);
			item.setAdd_Wiz(item.getAdd_Wiz() - 2);
			item.setAdd_Int(item.getAdd_Int() - 2);
			
			item.setAdd_Hp(item.getAdd_Hp() - 300);
				break;
			case 13:item.setAdd_Wiz(item.getAdd_Wiz() - 2);
			item.setAdd_Int(item.getAdd_Int() - 6);
				break;
			case 14:item.setAdd_Min_Dmg(item.getAdd_Min_Dmg() - 10);
			item.setAdd_Max_Dmg(item.getAdd_Max_Dmg() - 10);
			
			item.setAdd_Wiz(item.getAdd_Wiz() - 5);
			item.setAdd_Int(item.getAdd_Int() - 5);
				break;
			case 15:item.setAdd_Wiz(item.getAdd_Wiz() - 2);
			item.setAdd_Int(item.getAdd_Int() - 3);
			
			item.setAdd_Mana(item.getAdd_Mana() - 300);
				break;
			case 16:item.setAdd_Min_Dmg(item.getAdd_Min_Dmg() - 10);
			item.setAdd_Max_Dmg(item.getAdd_Max_Dmg() - 10);
			item.setAdd_Str(item.getAdd_Str() - 5);
			item.setAdd_Dex(item.getAdd_Dex() - 5);
				break;
			case 17:item.setAdd_Min_Dmg(item.getAdd_Min_Dmg() - 20);
			item.setAdd_Max_Dmg(item.getAdd_Max_Dmg() - 20);
			item.setAdd_Str(item.getAdd_Str() - 2);
			item.setAdd_Dex(item.getAdd_Dex() - 10);
				break;
			case 18:item.setAdd_Min_Dmg(item.getAdd_Min_Dmg() - 20);
			item.setAdd_Max_Dmg(item.getAdd_Max_Dmg() - 20);
			item.setAdd_Str(item.getAdd_Str() - 2);
			item.setAdd_Dex(item.getAdd_Dex() - 10);		
				break;
			case 19:item.setAdd_Str(item.getAdd_Str() - 2);
			item.setAdd_Dex(item.getAdd_Dex() - 5);
			item.setAdd_Wiz(item.getAdd_Wiz() - 3);
			item.setAdd_Int(item.getAdd_Int() - 5);
		
			
			item.setAdd_Hp(item.getAdd_Hp() - 300);
			item.setAdd_Mana(item.getAdd_Mana() - 100);
				break;
			case 20:item.setAdd_Min_Dmg(item.getAdd_Min_Dmg() - 20);
			item.setAdd_Max_Dmg(item.getAdd_Max_Dmg() - 20);
			item.setAdd_Str(item.getAdd_Str() - 2);
			item.setAdd_Dex(item.getAdd_Dex() - 10);		
				break;
			case 21:item.setAdd_Min_Dmg(item.getAdd_Min_Dmg() - 10);
			item.setAdd_Max_Dmg(item.getAdd_Max_Dmg() - 10);
			item.setAdd_Str(item.getAdd_Str() - 5);
			item.setAdd_Dex(item.getAdd_Dex() - 5);
			item.setAdd_Wiz(item.getAdd_Wiz() - 5);
			item.setAdd_Int(item.getAdd_Int() - 5);
			
			item.setAdd_Hp(item.getAdd_Hp() - 100);
			item.setAdd_Mana(item.getAdd_Mana() - 100);
				break;
			case 22:item.setAdd_Str(item.getAdd_Str() - 2);
			item.setAdd_Dex(item.getAdd_Dex() - 5);
			item.setAdd_Wiz(item.getAdd_Wiz() - 3);
			item.setAdd_Int(item.getAdd_Int() - 10);
			
			item.setAdd_Mana(item.getAdd_Mana() - 300);
				break;
			case 23:item.setAdd_Str(item.getAdd_Str() - 3);
			item.setAdd_Dex(item.getAdd_Dex() - 3);
			item.setAdd_Wiz(item.getAdd_Wiz() - 5);
			item.setAdd_Int(item.getAdd_Int() - 10);
			
			item.setAdd_Mana(item.getAdd_Mana() - 100);
				break;
			case 24:item.setAdd_Str(item.getAdd_Str() - 3);
			item.setAdd_Dex(item.getAdd_Dex() - 3);
			item.setAdd_Wiz(item.getAdd_Wiz() - 3);
			item.setAdd_Int(item.getAdd_Int() - 3);
			
			item.setAdd_Mana(item.getAdd_Mana() - 200);
				break;
			case 25:item.setAdd_Str(item.getAdd_Str() - 3);
			item.setAdd_Dex(item.getAdd_Dex() - 3);
			item.setAdd_Wiz(item.getAdd_Wiz() - 5);
			item.setAdd_Int(item.getAdd_Int() - 10);
			
		
			item.setAdd_Mana(item.getAdd_Mana() - 100);
				break;
			case 26:item.setAdd_Str(item.getAdd_Str() - 6);
			item.setAdd_Dex(item.getAdd_Dex() - 6);
			item.setAdd_Wiz(item.getAdd_Wiz() - 6);
			item.setAdd_Int(item.getAdd_Int() - 6);
			
			item.setAdd_Hp(item.getAdd_Hp() - 300);
				break;
			case 27:item.setAdd_Min_Dmg(item.getAdd_Min_Dmg() - 10);
			item.setAdd_Max_Dmg(item.getAdd_Max_Dmg() - 10);
			item.setAdd_Str(item.getAdd_Str() - 2);
			item.setAdd_Dex(item.getAdd_Dex() - 2);
			item.setAdd_Wiz(item.getAdd_Wiz() - 2);
			item.setAdd_Int(item.getAdd_Int() - 2);
			
			item.setAdd_Hp(item.getAdd_Hp() - 100);
			item.setAdd_Mana(item.getAdd_Mana() - 100);
				break;
			case 28:item.setAdd_Min_Dmg(item.getAdd_Min_Dmg() - 10);
			item.setAdd_Max_Dmg(item.getAdd_Max_Dmg() - 10);
			item.setAdd_Str(item.getAdd_Str() - 6);
			item.setAdd_Dex(item.getAdd_Dex() - 6);
			item.setAdd_Wiz(item.getAdd_Wiz() - 2);
			item.setAdd_Int(item.getAdd_Int() - 2);
				break;
			case 29:item.setAdd_Dex(item.getAdd_Dex() - 10);
			item.setAdd_Hp(item.getAdd_Hp() - 200);
			item.setAdd_Mana(item.getAdd_Mana() - 200);
				break;
			case 30:item.setAdd_Str(item.getAdd_Str() - 5);
			item.setAdd_Dex(item.getAdd_Dex() - 5);
			item.setAdd_Wiz(item.getAdd_Wiz() - 5);
			item.setAdd_Int(item.getAdd_Int() - 5);
				break;
			case 31:item.setAdd_Hp(item.getAdd_Hp() - 500);
				break;
			case 32:item.setAdd_Wiz(item.getAdd_Wiz() - 5);
			item.setAdd_Int(item.getAdd_Int() - 5);
			
			item.setAdd_Mana(item.getAdd_Mana() - 300);
				break;
			case 33:item.setAdd_Str(item.getAdd_Str() - 3);
			item.setAdd_Dex(item.getAdd_Dex() - 3);
			item.setAdd_Wiz(item.getAdd_Wiz() - 3);
			item.setAdd_Int(item.getAdd_Int() - 3);
			
			item.setAdd_Hp(item.getAdd_Hp() - 100);
			item.setAdd_Mana(item.getAdd_Mana() - 100);
				break;
			case 34:item.setAdd_Str(item.getAdd_Str() - 5);
			item.setAdd_Dex(item.getAdd_Dex() - 5);
			item.setAdd_Hp(item.getAdd_Hp() - 250);
				break;
			}
			
			item.setOne(0);
			item.setTwo(0);
			item.setThree(0);
			item.setFour(0);
			
			if(cha.getSix()!=0)
				cha.setSix(0);
			
			cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
			
			if(Util.random(0, 100) < 30){
				Item it = ItemDatabase.find2("수의룬");
				if(it != null){
					ItemInstance iii = ItemDatabase.newInstance(it);
					iii.setDefinite(false);
					cha.getInventory().append(iii, 1L);
					ItemDatabase.setPool(iii);
				}
			}
			if(Util.random(0, 100) < 30){
				Item it = ItemDatabase.find2("풍의룬");
				if(it != null){
					ItemInstance iii = ItemDatabase.newInstance(it);
					iii.setDefinite(false);
					cha.getInventory().append(iii, 1L);
					ItemDatabase.setPool(iii);
				}
			}
			if(Util.random(0, 100) < 30){
				Item it = ItemDatabase.find2("화의룬");
				if(it != null){
					ItemInstance iii = ItemDatabase.newInstance(it);
					iii.setDefinite(false);
					cha.getInventory().append(iii, 1L);
					ItemDatabase.setPool(iii);
				}
			}
			if(Util.random(0, 100) < 30){
				Item it = ItemDatabase.find2("지의룬");
				if(it != null){
					ItemInstance iii = ItemDatabase.newInstance(it);
					iii.setDefinite(false);
					cha.getInventory().append(iii, 1L);
					ItemDatabase.setPool(iii);
				}
			}
			if(Util.random(0, 100) < 10){
				Item it = ItemDatabase.find2("무의룬");
				if(it != null){
					ItemInstance iii = ItemDatabase.newInstance(it);
					iii.setDefinite(false);
					cha.getInventory().append(iii, 1L);
					ItemDatabase.setPool(iii);
				}
			}
			
		}
		cha.getInventory().count(this, getCount()-1, true);
	}
}
