package lineage.world.object.item.scroll.newscroll;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class ScrollRoon extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollRoon();
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
		if(this.item.getName().indexOf("수") > -1){
			if(item.getOne()==1 || item.getTwo()==1||item.getThree()==1||item.getFour()==1 ){
				ChattingController.toChatting(cha, String.format("\\fT 이미 수의 룬이 발라진 상태 입니다. 다른 룬을 조합하세요"), 20);
				return;
			}
			
			if(item.getOne()==0 && item.getItem().getType2().equalsIgnoreCase("staff") || item.getItem().getType2().equalsIgnoreCase("wand")){
				item.setOne(1);
			}else if(item.getTwo()==0){
				item.setTwo(1);
			}else if(item.getThree()==0){
				item.setThree(1);
			}else if(item.getFour()==0){
				item.setFour(1);
			}else{
				ChattingController.toChatting(cha, String.format("\\fT 첫번째 수의 룬은 마법사 전용 무기에 발라집니다."), 20);
				return;
			}
		}else if(this.item.getName().indexOf("풍") > -1 ){
			if(item.getOne()==2 || item.getTwo()==2||item.getThree()==2||item.getFour()==2 ){
				ChattingController.toChatting(cha, String.format("\\fT 이미 풍의 룬이 발라진 상태 입니다. 다른 룬을 조합하세요"), 20);
				return;
			}
			if(item.getOne()==0 && item.getItem().getType2().equalsIgnoreCase("bow")){
				item.setOne(2);
			}else if(item.getTwo()==0){
				item.setTwo(2);
			}else if(item.getThree()==0){
				item.setThree(2);
			}else if(item.getFour()==0){
				item.setFour(2);
			}else{
				ChattingController.toChatting(cha, String.format("\\fT 첫번째 풍의 룬은 요정 전용 무기(활)에 발라집니다."), 20);
				return;
			}
		}else if(this.item.getName().indexOf("화") > -1 ){
			if(item.getOne()==3 || item.getTwo()==3||item.getThree()==3||item.getFour()==3 ){
				ChattingController.toChatting(cha, String.format("\\fT 이미 화의 룬이 발라진 상태 입니다. 다른 룬을 조합하세요"), 20);
				return;
			}
			if(item.getOne()==0 && (item.getItem().getType2().equalsIgnoreCase("axe") || item.getItem().getType2().equalsIgnoreCase("tohandsword")
					||item.getItem().getType2().equalsIgnoreCase("sword")||item.getItem().getType2().equalsIgnoreCase("spear")||item.getItem().getType2().equalsIgnoreCase("edoryu")
					||item.getItem().getType2().equalsIgnoreCase("dagger")||item.getItem().getType2().equalsIgnoreCase("claw")||item.getItem().getType2().equalsIgnoreCase("blunt"))){
				item.setOne(3);
			}else if(item.getTwo()==0){
				item.setTwo(3);
			}else if(item.getThree()==0){
				item.setThree(3);
			}else if(item.getFour()==0){
				item.setFour(3);
			}else{
				ChattingController.toChatting(cha, String.format("\\fT 첫번째 화의 룬은 근접 전용 무기에 발라집니다."), 20);
				return;
			}
			
		}else if(this.item.getName().indexOf("지") > -1 ){
			if(item.getOne()==4 || item.getTwo()==4||item.getThree()==4||item.getFour()==4 ){
				ChattingController.toChatting(cha, String.format("\\fT 이미 지의 룬이 발라진 상태 입니다. 다른 룬을 조합하세요"), 20);
				return;
			}
			
			if(item.getOne()==0 && item.getItem().getType2().equalsIgnoreCase("armor")){
				item.setOne(4);
			}else if(item.getTwo()==0){
				item.setTwo(4);
			}else if(item.getThree()==0){
				item.setThree(4);
			}else if(item.getFour()==0){
				item.setFour(4);
			}else{
				ChattingController.toChatting(cha, String.format("\\fT 첫번째 지의 룬은 갑옷에 발라집니다."), 20);
				return;
			}
		}else{
			ChattingController.toChatting(cha, String.format("\\fT 룬 과 장비류 조합이 맞지 않습니다."), 20);
			return;
		}
		
		cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
		cha.getInventory().count(this, getCount()-1, true);
	}
}
