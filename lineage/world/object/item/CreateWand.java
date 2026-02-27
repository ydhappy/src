package lineage.world.object.item;

import lineage.database.ItemDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.GameSetting;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class CreateWand extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new CreateWand();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		ItemInstance item = cha.getInventory().value(cbp.readD());
		int enchant = item.getEnLevel();
		int count = GameSetting.제작막대필요갯수;
		if(item != null){
			if(!GameSetting.제작막대무기사용가능여부){
				if(!(item instanceof ItemArmorInstance)){
					ChattingController.toChatting(cha, "방어구 만 사용가능합니다." , 20);
					return;
				}
				if(item.getItem().getType2().equalsIgnoreCase("ring") || 
					item.getItem().getType2().equalsIgnoreCase("belt") || 
					item.getItem().getType2().equalsIgnoreCase("earring") || 
					item.getItem().getType2().equalsIgnoreCase("necklace")){
					ChattingController.toChatting(cha, "악세사리에는 사용할 수 없습니다." , 20);
					return;
				}
			} else {
				if(!(item.getItem().getType1().equalsIgnoreCase("weapon") || item.getItem().getType1().equalsIgnoreCase("armor"))){
					ChattingController.toChatting(cha, "무기와 방어구 에만 사용가능합니다." , 20);
					return;
				}
				
				if(item.getItem().getType2().equalsIgnoreCase("ring") || 
					item.getItem().getType2().equalsIgnoreCase("belt") || 
					item.getItem().getType2().equalsIgnoreCase("earring") || 
					item.getItem().getType2().equalsIgnoreCase("necklace")){
					ChattingController.toChatting(cha, "악세사리에는 사용할 수 없습니다." , 20);
					return;
				}
			}
			
			
			
			if(item.getItem().getSafeEnchant() > 4){
				ChattingController.toChatting(cha, "안전인챈 4이상 아이템에는 사용할 수 없습니다." , 20);
				return;
			}
			
			if(item.getEnLevel() < GameSetting.제작막대인챈제한){
				ChattingController.toChatting(cha, "+" + GameSetting.제작막대인챈제한 + " ~ +"  + GameSetting.제작막대최대인챈제한 + 
						" 인챈 아이템만 사용가능 합니다." , 20);
				return;
			}
			
			if(item.getEnLevel() > GameSetting.제작막대최대인챈제한){
				ChattingController.toChatting(cha, "+" + GameSetting.제작막대인챈제한 + " ~ +"  + GameSetting.제작막대최대인챈제한 + 
						" 인챈 아이템만 사용가능 합니다." , 20);
				return;
			}
			
			if(cha.getInventory().MakeCheckEnchant(item.getItem().getItemId(), enchant, count)){
				for(int i=0; i < count; i++){
					cha.getInventory().MakeDeleteEnchant2(item.getItem().getItemId(), enchant, 1);
				}
				ItemInstance 지급 = ItemDatabase.newInstance(item);
				지급.setEnLevel(enchant+1);
				지급.setDefinite(true);
				if(Util.random(0, 100) < GameSetting.제작막대축아이템지급최대확률 && Util.random(0, 100) >= GameSetting.제작막대축아이템지급최소확률){
					지급.setBress(0);
					cha.getInventory().append(지급, true);
					ChattingController.toChatting(cha, "축하드립니다 인챈아이템 변환 대성공!! 축아이템지급!!" , 20);
				} else {
					지급.setBress(1);
					cha.getInventory().append(지급, true);
					ChattingController.toChatting(cha, "인챈아이템 변환에 성공하였습니다." , 20);
				}
			} else {
				ChattingController.toChatting(cha, "변환에 필요한 아이템이 부족합니다." , 20);
				ChattingController.toChatting(cha, "동일인챈아이템이 " + count + "개가 필요합니다." , 20);
			}
		}
	}
}
