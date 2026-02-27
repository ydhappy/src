package lineage.world.object.item.etc;

import lineage.network.packet.ClientBasePacket;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class ShadowShrine extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ShadowShrine();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// 2층 ㄱㄱ
		if(getItem().getNameIdNumber() == 3763) {
			if(cha.getMap()==522 && cha.getX()>=32702 && cha.getX()<=32705 && cha.getY()>=32894 && cha.getY()<=32897)
				cha.toPotal(32701, 32895, 523);
			return;
		}
		// 3층 ㄱㄱ
		if(getItem().getNameIdNumber() == 3764) {
			if(cha.getMap()==523 && cha.getX()>=32699 && cha.getX()<=32702 && cha.getY()>=32894 && cha.getY()<=32897)
				cha.toPotal(32670, 32895, 524);
			return;
		}
	}

}
