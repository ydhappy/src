package sp.item;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 오만의탑정상지배부적 extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item, int x, int y, int map){
		if(item == null)
			item = new 오만의탑정상지배부적();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// 
		boolean bronze = ((cha.getX()>=33377)&&(cha.getX()<=33478))&&((cha.getY()>=32777)&&(cha.getY()<=32835))&&(cha.getMap()==4);
		if(bronze) {
			cha.setHomeX(32726);
			cha.setHomeY(32810);
			cha.setHomeMap(109);
			cha.toPotal(cha.getHomeX(), cha.getHomeY(), cha.getHomeMap());
		} else {
			// 아무일도 일어나지 않았습니다.
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
		}
	}

}
