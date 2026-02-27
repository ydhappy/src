package lineage.world.object.npc.craft;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.instance.CraftInstance;
import lineage.world.object.instance.PcInstance;

public class PielEmental extends CraftInstance {
	
	public PielEmental(Npc npc){
		super(npc);
		
		// 제작 처리 초기화.
		Item i = ItemDatabase.find("정령의 결정");
		if(i != null){
			craft_list.put("request crystal of soul", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("정령의 파편"), 20) );
			l.add( new Craft(ItemDatabase.find("마력의 돌"), 3) );
			list.put(i, l);
		}
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "pielemental"));
	}

}
