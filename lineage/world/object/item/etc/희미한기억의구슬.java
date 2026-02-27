package lineage.world.object.item.etc;

import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.BookController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.LocationController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 희미한기억의구슬 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 희미한기억의구슬();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		boolean js = ((cha.getX()>=33338)&&(cha.getX()<=33532))&&((cha.getY()>=32644)&&(cha.getY()<=32879))&&(cha.getMap()==4);
		if(!js){
			ChattingController.toChatting(cha, String.format("기란 마을에서 사용가능합니다."), Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		PcInstance pc = (PcInstance) cha;
			BookController.Bookmarkitem(pc);
				cha.getInventory().count(this, getCount()-1, true);
		}
	}
