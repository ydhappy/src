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

public class ScrollRoonLast extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollRoonLast();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		ItemInstance item = cha.getInventory().value(cbp.readD());
		if(item.isEquipped()){
				ChattingController.toChatting(cha, String.format("\\fT 장비를 착용한 상태로 확인 하실 수 없습니다."), 20);
				return;
			}
		if(!item.isDefinite()){
			ChattingController.toChatting(cha, String.format("\\fT 확인 된 장비에 작업을 하시기 바랍니다."), 20);
			return;
		}
		//수1 = 풍2 = 화3 = 지4 
		if(item.getOne()!=0 && item.getTwo()!=0 && item.getThree()!=0 && item.getFour()!=0){
			if(item.getOne()==1){
				if(item.getTwo()==2){
					if(item.getThree()==4){
						if(item.getFour()==3){
							item.setOne(10);
							item.setTwo(10);
							item.setThree(10);
							item.setFour(11);
							
							item.setAdd_Wiz(item.getAdd_Wiz() + 2);
							item.setAdd_Int(item.getAdd_Int() + 6);
							
						}
					}else{
						item.setOne(10);
						item.setTwo(10);
						item.setThree(10);
						item.setFour(12);
						
						item.setAdd_Dex(item.getAdd_Dex() + 4);
						item.setAdd_Wiz(item.getAdd_Wiz() + 4);
						item.setAdd_Int(item.getAdd_Int() + 4);
						
						item.setAdd_Hp(item.getAdd_Hp() + 500);
						}
					
				}else if(item.getTwo()==4){
					if(item.getThree()==2){
						if(item.getFour()==3){
							item.setOne(10);
							item.setTwo(10);
							item.setThree(10);
							item.setFour(13);
							
							item.setAdd_Wiz(item.getAdd_Wiz() + 2);
							item.setAdd_Int(item.getAdd_Int() + 6);
							
							
						}
					}else{
						item.setOne(10);
						item.setTwo(10);
						item.setThree(10);
						item.setFour(14);
						
						item.setAdd_Min_Dmg(item.getAdd_Min_Dmg() + 10);
						item.setAdd_Max_Dmg(item.getAdd_Max_Dmg() + 10);
						
						item.setAdd_Wiz(item.getAdd_Wiz() + 5);
						item.setAdd_Int(item.getAdd_Int() + 5);
						
						
					}
				}else if(item.getTwo()==3){
					if(item.getThree()==2){
						if(item.getFour()==4){
							item.setOne(10);
							item.setTwo(10);
							item.setThree(10);
							item.setFour(15);
						
							item.setAdd_Wiz(item.getAdd_Wiz() + 7);
							item.setAdd_Int(item.getAdd_Int() + 7);
							
							item.setAdd_Mana(item.getAdd_Mana() + 500);
						}
					}else{
						item.setOne(10);
						item.setTwo(10);
						item.setThree(10);
						item.setFour(16);
						
						item.setAdd_Min_Dmg(item.getAdd_Min_Dmg() + 15);
						item.setAdd_Max_Dmg(item.getAdd_Max_Dmg() + 15);
						item.setAdd_Str(item.getAdd_Str() + 5);
						item.setAdd_Dex(item.getAdd_Dex() + 5);
						
					}
				}				
			}else if(item.getOne()==2){

				if(item.getTwo()==1){
					if(item.getThree()==4){
						if(item.getFour()==3){
							item.setOne(10);
							item.setTwo(10);
							item.setThree(10);
							item.setFour(17);
							
							item.setAdd_Min_Dmg(item.getAdd_Min_Dmg() + 20);
							item.setAdd_Max_Dmg(item.getAdd_Max_Dmg() + 20);
							item.setAdd_Str(item.getAdd_Str() + 2);
							item.setAdd_Dex(item.getAdd_Dex() + 10);
							
						}
					}else{
						item.setOne(10);
						item.setTwo(10);
						item.setThree(10);
						item.setFour(18);
						
						item.setAdd_Min_Dmg(item.getAdd_Min_Dmg() + 20);
						item.setAdd_Max_Dmg(item.getAdd_Max_Dmg() + 20);
						item.setAdd_Str(item.getAdd_Str() + 2);
						item.setAdd_Dex(item.getAdd_Dex() + 10);						
						
					}
				}else if(item.getTwo()==4){
					if(item.getThree()==1){
						if(item.getFour()==3){
							item.setOne(10);
							item.setTwo(10);
							item.setThree(10);
							item.setFour(19);
							
							
							item.setAdd_Str(item.getAdd_Str() + 2);
							item.setAdd_Dex(item.getAdd_Dex() + 5);
							item.setAdd_Wiz(item.getAdd_Wiz() + 3);
							item.setAdd_Int(item.getAdd_Int() + 5);
						
							
							item.setAdd_Hp(item.getAdd_Hp() + 300);
							item.setAdd_Mana(item.getAdd_Mana() + 100);
						}
					}else{
						item.setOne(10);
						item.setTwo(10);
						item.setThree(10);
						item.setFour(20);
						
						item.setAdd_Min_Dmg(item.getAdd_Min_Dmg() + 20);
						item.setAdd_Max_Dmg(item.getAdd_Max_Dmg() + 20);
						item.setAdd_Str(item.getAdd_Str() + 2);
						item.setAdd_Dex(item.getAdd_Dex() + 10);						
						
					}
				}else if(item.getTwo()==3){
					if(item.getThree()==1){
						if(item.getFour()==4){
							item.setOne(10);
							item.setTwo(10);
							item.setThree(10);
							item.setFour(21);
							
							item.setAdd_Min_Dmg(item.getAdd_Min_Dmg() + 10);
							item.setAdd_Max_Dmg(item.getAdd_Max_Dmg() + 10);
							item.setAdd_Str(item.getAdd_Str() + 5);
							item.setAdd_Dex(item.getAdd_Dex() + 5);
							item.setAdd_Wiz(item.getAdd_Wiz() + 5);
							item.setAdd_Int(item.getAdd_Int() + 5);
							
							item.setAdd_Hp(item.getAdd_Hp() + 200);
							item.setAdd_Mana(item.getAdd_Mana() + 200);
						}
					}else{
						item.setOne(10);
						item.setTwo(10);
						item.setThree(10);
						item.setFour(22);
						
						
						item.setAdd_Str(item.getAdd_Str() + 2);
						item.setAdd_Dex(item.getAdd_Dex() + 5);
						item.setAdd_Wiz(item.getAdd_Wiz() + 3);
						item.setAdd_Int(item.getAdd_Int() + 10);
						
						item.setAdd_Mana(item.getAdd_Mana() + 300);
					}
				}
			
			}else if(item.getOne()==3){


				if(item.getTwo()==1){
					if(item.getThree()==2){
						if(item.getFour()==4){
							item.setOne(10);
							item.setTwo(10);
							item.setThree(10);
							item.setFour(23);
							
							item.setAdd_Str(item.getAdd_Str() + 3);
							item.setAdd_Dex(item.getAdd_Dex() + 3);
							item.setAdd_Wiz(item.getAdd_Wiz() + 5);
							item.setAdd_Int(item.getAdd_Int() + 10);
							
							item.setAdd_Mana(item.getAdd_Mana() + 100);
							
						}
					}else{
						item.setOne(10);
						item.setTwo(10);
						item.setThree(10);
						item.setFour(24);
						
						
						item.setAdd_Str(item.getAdd_Str() + 3);
						item.setAdd_Dex(item.getAdd_Dex() + 3);
						item.setAdd_Wiz(item.getAdd_Wiz() + 3);
						item.setAdd_Int(item.getAdd_Int() + 3);
						
						item.setAdd_Mana(item.getAdd_Mana() + 200);
					}
				}else if(item.getTwo()==2){
					if(item.getThree()==1){
						if(item.getFour()==4){
							item.setOne(10);
							item.setTwo(10);
							item.setThree(10);
							item.setFour(25);
							
							
							item.setAdd_Str(item.getAdd_Str() + 3);
							item.setAdd_Dex(item.getAdd_Dex() + 3);
							item.setAdd_Wiz(item.getAdd_Wiz() + 5);
							item.setAdd_Int(item.getAdd_Int() + 10);
							
						
							item.setAdd_Mana(item.getAdd_Mana() + 100);
						}
					}else{
						item.setOne(10);
						item.setTwo(10);
						item.setThree(10);
						item.setFour(26);
						
						
						item.setAdd_Str(item.getAdd_Str() + 7);
						item.setAdd_Dex(item.getAdd_Dex() + 7);
						item.setAdd_Wiz(item.getAdd_Wiz() + 7);
						item.setAdd_Int(item.getAdd_Int() + 7);
						
						item.setAdd_Hp(item.getAdd_Hp() + 1000);
					
					}
				}else if(item.getTwo()==4){
					if(item.getThree()==1){
						if(item.getFour()==2){item.setOne(10);
						item.setTwo(10);
						item.setThree(10);
						item.setFour(27);
						
						item.setAdd_Min_Dmg(item.getAdd_Min_Dmg() + 10);
						item.setAdd_Max_Dmg(item.getAdd_Max_Dmg() + 10);
						item.setAdd_Str(item.getAdd_Str() + 2);
						item.setAdd_Dex(item.getAdd_Dex() + 2);
						item.setAdd_Wiz(item.getAdd_Wiz() + 2);
						item.setAdd_Int(item.getAdd_Int() + 2);
						
						item.setAdd_Hp(item.getAdd_Hp() + 100);
						item.setAdd_Mana(item.getAdd_Mana() + 100);
						}
					}else{item.setOne(10);
					item.setTwo(10);
					item.setThree(10);
					item.setFour(28);
					
					item.setAdd_Min_Dmg(item.getAdd_Min_Dmg() + 10);
					item.setAdd_Max_Dmg(item.getAdd_Max_Dmg() + 10);
					item.setAdd_Str(item.getAdd_Str() + 6);
					item.setAdd_Dex(item.getAdd_Dex() + 6);
					item.setAdd_Wiz(item.getAdd_Wiz() + 2);
					item.setAdd_Int(item.getAdd_Int() + 2);
					
					}
				}
			
			
			}else if(item.getOne()==4){
				if(item.getTwo()==1){
					if(item.getThree()==2){
						if(item.getFour()==3){item.setOne(10);
						item.setTwo(10);
						item.setThree(10);
						item.setFour(29);						
						
						item.setAdd_Dex(item.getAdd_Dex() + 10);
						item.setAdd_Hp(item.getAdd_Hp() + 200);
						item.setAdd_Mana(item.getAdd_Mana() + 200);
						}
					}else{item.setOne(10);
					item.setTwo(10);
					item.setThree(10);
					item.setFour(30);
					item.setAdd_Str(item.getAdd_Str() + 5);
					item.setAdd_Dex(item.getAdd_Dex() + 5);
					item.setAdd_Wiz(item.getAdd_Wiz() + 5);
					item.setAdd_Int(item.getAdd_Int() + 5);
					
					}
				}else if(item.getTwo()==2){
					if(item.getThree()==1){
						if(item.getFour()==3){item.setOne(10);
						item.setTwo(10);
						item.setThree(10);
						item.setFour(31);
												
						item.setAdd_Hp(item.getAdd_Hp() + 500);
						}
					}else{item.setOne(10);
					item.setTwo(10);
					item.setThree(10);
					item.setFour(32);
					
					item.setAdd_Wiz(item.getAdd_Wiz() + 5);
					item.setAdd_Int(item.getAdd_Int() + 5);
					
					item.setAdd_Mana(item.getAdd_Mana() + 300);}
				}else if(item.getTwo()==3){
					if(item.getThree()==1){
						if(item.getFour()==2){item.setOne(10);
						item.setTwo(10);
						item.setThree(10);
						item.setFour(33);
						item.setAdd_Str(item.getAdd_Str() + 3);
						item.setAdd_Dex(item.getAdd_Dex() + 3);
						item.setAdd_Wiz(item.getAdd_Wiz() + 3);
						item.setAdd_Int(item.getAdd_Int() + 3);
						
						item.setAdd_Hp(item.getAdd_Hp() + 100);
						item.setAdd_Mana(item.getAdd_Mana() + 100);}
					}else{item.setOne(10);
					item.setTwo(10);
					item.setThree(10);
					item.setFour(34);
					item.setAdd_Str(item.getAdd_Str() + 5);
					item.setAdd_Dex(item.getAdd_Dex() + 5);
			
					
					item.setAdd_Hp(item.getAdd_Hp() + 250);
					}
				}
			
			
			}
			
			cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
			
		}else{
			ChattingController.toChatting(cha, String.format("\\fT 4개의 룬을 조합해야 사용 가능 합니다."), 20);
			return;
		}
		cha.getInventory().count(this, getCount()-1, true);
	}
}
