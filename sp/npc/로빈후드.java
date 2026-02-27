package sp.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.CraftController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.npc.quest.Robiel;

public class 로빈후드 extends Robiel {

	public 로빈후드(Npc npc){
		super(npc);
		
		Item i = ItemDatabase.find("달의 장궁");
		if(i != null){
			craft_list.put("E", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("신성한 유니콘의 뿔"), 4) );
			l.add( new Craft(ItemDatabase.find("불의 숨결"), 30) );
			l.add( new Craft(ItemDatabase.find("물의 숨결"), 30) );
			l.add( new Craft(ItemDatabase.find("바람의 숨결"), 30) );
			l.add( new Craft(ItemDatabase.find("대지의 숨결"), 30) );
			l.add( new Craft(ItemDatabase.find("어둠의 숨결"), 30) );
			l.add( new Craft(ItemDatabase.find("그리폰의 깃털"), 30) );
			
			list.put(i, l);
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		if(pc.getClassType() == 0x02) {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "robinhood11"));
		} else {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "robinhood2"));
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		//
		if(action.equalsIgnoreCase("E")) {
			// 로빈후드의 반지와 재료를 건네준다.
			Item craft = craft_list.get(action);
			if(craft != null) {
				List<Craft> l = list.get(craft);
				if(CraftController.isCraft(pc, l, true)) {
					CraftController.toCraft(pc, l);
					CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
					pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "robinhood12"));
				} else {
					pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "robinhood17"));
				}
			}
		}
	}
	
}
