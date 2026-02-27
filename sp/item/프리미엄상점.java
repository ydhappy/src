package sp.item;

//import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
//import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 프리미엄상점 extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item, int x, int y, int map){
		if(item == null)
			item = new 프리미엄상점();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// 
		boolean bronze = ((cha.getX()>=33377)&&(cha.getX()<=33478))&&((cha.getY()>=32777)&&(cha.getY()<=32835))&&(cha.getMap()==4);
		if(bronze) {
		    cha.setHomeX(32692);
			cha.setHomeY(32794);
			cha.setHomeMap(3);
			cha.toPotal(cha.getHomeX(), cha.getHomeY(), cha.getHomeMap());	
		} else{
			 //아무일도 일어나지 않았습니다.
			ChattingController.toChatting(cha, "[서버알림]기란 마을에서만 사용 가능한 아이템입니다 ", 20);
		}
	}
}



