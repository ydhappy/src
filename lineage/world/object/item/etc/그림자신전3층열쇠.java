package lineage.world.object.item.etc;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 그림자신전3층열쇠 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 그림자신전3층열쇠();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// 좌표확인.
		if(cha.getMap()==523 && cha.getX()>=32698 && cha.getX()<=32703 && cha.getY()>=32893 && cha.getY()<=32898) {
			if(Lineage.server_version <= 200)
				cha.toTeleport(32670, 32895, 524, true);
			else
				cha.toPotal(32670, 32895, 524);
			cha.getInventory().count(this, getCount()-1, true);
			return;
		}
		// \f1아무일도 일어나지 않았습니다.
		cha.toSender( S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79) );
	}

}
