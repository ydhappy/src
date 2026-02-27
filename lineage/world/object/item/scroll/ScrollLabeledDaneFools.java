package lineage.world.object.item.scroll;

import lineage.database.AccountDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.Enchant;

public class ScrollLabeledDaneFools extends Enchant {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollLabeledDaneFools();
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
			if(weapon!=null && weapon.getItem().isEnchant() && weapon instanceof ItemWeaponInstance){
				if (cha instanceof PcInstance) {
					int en = toEnchant(cha, weapon);

					weapon.toEnchant((PcInstance) cha, en);

					if (en != -127)
						cha.getInventory().count(this, getCount() - 1, true);
				}
			} else {	
				if (weapon instanceof ItemWeaponInstance && !weapon.getItem().isEnchant())
					ChattingController.toChatting(cha, "인챈트가 불가능한 무기입니다.", Lineage.CHATTING_MODE_MESSAGE);
				else
					ChattingController.toChatting(cha, "무기에 사용이 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}
	//성환.무기인첸트	
	@Override
	protected boolean isEnchant(Character cha, ItemInstance weapon) {
		if(cha.getInventory().getSlot(8) != null)
			cha.getInventory().getSlot(8).toClick(cha, null);
		double enchant_chance = 95D;
		PcInstance pc = (PcInstance) cha;
		if(bress == 2) {
			for(int i=0 ; i>weapon.getEnLevel() ; --i)
				enchant_chance = enchant_chance * 0.0;
			
		} 
	
		else if(bress==0) {
			if(weapon.getItem().getSafeEnchant()==0){
				switch(weapon.getEnLevel()){
				case 0: enchant_chance = pc.isperpecten() ? 100 : Lineage.ssafe0weapon0;break;
				case 1: enchant_chance = pc.isperpecten() ? 100 :Lineage.ssafe0weapon1;break;
				case 2: enchant_chance = pc.isperpecten() ? 100 :Lineage.ssafe0weapon2;break;
				case 3: enchant_chance = pc.isperpecten() ? 100 :Lineage.ssafe0weapon3;break;
				case 4: enchant_chance = pc.isperpecten() ? 100 :Lineage.ssafe0weapon4;break;
				case 5: enchant_chance = pc.isperpecten() ? 100 :Lineage.ssafe0weapon5;break;
				case 6: enchant_chance = pc.isperpecten() ? 100 :Lineage.ssafe0weapon6;break;
				case 7: enchant_chance = pc.isperpecten() ? 100 :Lineage.ssafe0weapon7;break;
				case 8: enchant_chance = pc.isperpecten() ? 100 :Lineage.ssafe0weapon8;break;
				case 9: enchant_chance = pc.isperpecten() ? 100 :Lineage.ssafe0weapon9;break;
				case 10: enchant_chance = pc.isperpecten() ? 100 :Lineage.ssafe0weapon10;break;
				default:
					for(int i=weapon.getItem().getSafeEnchant() ; i<weapon.getEnLevel() ; ++i)
						enchant_chance = enchant_chance * 0.23;
					break;
				}
			}else if(weapon.getItem().getSafeEnchant()==6){
				switch(weapon.getEnLevel()){
				case 6: enchant_chance = pc.isperpecten() ? 100 :Lineage.ssafe6weapon6;break;
				case 7: enchant_chance = pc.isperpecten() ? 100 :Lineage.ssafe6weapon7;break;
				case 8: 
					if(pc.isperpecten())
						enchant_chance = 100;
				
				
					else if(Lineage.enfail){
						
						
						if(pc.getEnfail() > Lineage.enfail_max){
							enchant_chance = 100;
							pc.setEnfail(0);
							AccountDatabase.updateenfail(pc.getClient().getAccountUid(), 0);
					
						}else
							enchant_chance = Lineage.ssafe6weapon8;
					}else
						enchant_chance = Lineage.ssafe6weapon8;
					
					
				
				
					break;
					
				
				
				
				case 9: enchant_chance = pc.isperpecten() ? 100 :Lineage.ssafe6weapon9;break;
				case 10: enchant_chance = pc.isperpecten() ? 100 :Lineage.ssafe6weapon10;break;
				case 11: enchant_chance = pc.isperpecten() ? 100 :Lineage.ssafe6weapon11;break;
				default:
					for(int i=weapon.getItem().getSafeEnchant() ; i<weapon.getEnLevel() ; ++i)
						enchant_chance = enchant_chance * 0.23;
					break;
				}
			}else{
				ChattingController.toChatting(cha, "문제상황 발생. 운영자에게 문의해주세요!(인첸부분)");
			}
		}			
		else {
			// 안전인첸값을 기준으로 인첸값 만큼 확률 낮추기.
			//기존 확률 4 15.2   5 2.432  6 0.38912 7 8
			/*for(int i=weapon.getItem().getSafeEnchant() ; i<weapon.getEnLevel() ; ++i)
				enchant_chance = enchant_chance * 0.23;*/
			if(weapon.getItem().getSafeEnchant()==0){
				switch(weapon.getEnLevel()){
				case 0: enchant_chance = pc.isperpecten() ? 100 :Lineage.safe0weapon0;break;
				case 1: enchant_chance = pc.isperpecten() ? 100 :Lineage.safe0weapon1;break;
				case 2: enchant_chance = pc.isperpecten() ? 100 :Lineage.safe0weapon2;break;
				case 3: enchant_chance = pc.isperpecten() ? 100 :Lineage.safe0weapon3;break;
				case 4: enchant_chance = pc.isperpecten() ? 100 :Lineage.safe0weapon4;break;
				case 5: enchant_chance = pc.isperpecten() ? 100 :Lineage.safe0weapon5;break;
				case 6: enchant_chance = pc.isperpecten() ? 100 :Lineage.safe0weapon6;break;
				case 7: enchant_chance = pc.isperpecten() ? 100 :Lineage.safe0weapon7;break;
				case 8: enchant_chance = pc.isperpecten() ? 100 :Lineage.safe0weapon8;break;
				case 9: enchant_chance = pc.isperpecten() ? 100 :Lineage.safe0weapon9;break;
				case 10: enchant_chance = pc.isperpecten() ? 100 :Lineage.safe0weapon10;break;
				default:
					for(int i=weapon.getItem().getSafeEnchant() ; i<weapon.getEnLevel() ; ++i)
						enchant_chance = enchant_chance * 0.23;
					break;
				}
			}else if(weapon.getItem().getSafeEnchant()==6){
				switch(weapon.getEnLevel()){
				case 6: enchant_chance = pc.isperpecten() ? 100 :Lineage.safe6weapon6;break;
				case 7: enchant_chance = pc.isperpecten() ? 100 :Lineage.safe6weapon7;break;
				case 8: 		
					if(pc.isperpecten())
					enchant_chance = 100;
			
			
				else if(Lineage.enfail){
				
				
					if(pc.getEnfail() == Lineage.enfail_max){
						enchant_chance = 100;
						pc.setEnfail(0);
						AccountDatabase.updateenfail(pc.getClient().getAccountUid(), 0);
						
				
					}else
						enchant_chance = Lineage.safe6weapon8;
				}else
					enchant_chance = Lineage.ssafe6weapon8;
				
				
			
			
				break;
				case 9: enchant_chance = pc.isperpecten() ? 100 :Lineage.safe6weapon9;break;
				case 10: enchant_chance = pc.isperpecten() ? 100 :Lineage.safe6weapon10;break;
				case 11: enchant_chance = pc.isperpecten() ? 100 :Lineage.safe6weapon11;break;
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
