package lineage.world.object.npc.quest;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestInstance;

public class Dh extends QuestInstance {
	
	public Dh(Npc npc){
		super(npc);
		
		Item i = ItemDatabase.find("복원된 고대 목걸이");
		if(i != null){
			craft_list.put("request dh A", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("볼품없는 고대 목걸이"), 1) );
			l.add( new Craft(ItemDatabase.find("타로스의 루비"), 1) );
			l.add( new Craft(ItemDatabase.find("타로스의 사파이어"), 1) );
			l.add( new Craft(ItemDatabase.find("타로스의 에메랄드"), 1) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("빛나는 고대의 목걸이");
		if(i != null){
			craft_list.put("request dh B", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("복원된 고대 목걸이"), 1) );
			l.add( new Craft(ItemDatabase.find("타로스의 다이아몬드"), 1) );
			list.put(i, l);
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "dh1"));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt) {
		super.toTalk(pc, String.format("request dh %s", action), type, cbp, opt);
	}
}
