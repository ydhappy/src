package lineage.world.object.item.sp;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.ItemBundle;
import lineage.database.ItemBundleDatabase;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class Onea extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Onea();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
				// 아이템 지급.
				List<ItemBundle> list = new ArrayList<ItemBundle>();
				ItemBundleDatabase.find(list, getItem().getName());

						// 메모리 생성 및 초기화.
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find(4));
						ii.setCount( 100000000 );
						cha.getInventory().append(ii, ii.getCount());
						// \f1%0%s 당신에게 %1%o 주었습니다.
						ChattingController.toChatting(cha, "1억원으로 교환 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
						//cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 143, getName(), ii.toString()));
						// 메모리 재사용.
						ItemDatabase.setPool(ii);
				// 수량 하향.
				cha.getInventory().count(this, getCount()-1, true);
		
	}

}
