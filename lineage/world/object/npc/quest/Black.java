package lineage.world.object.npc.quest;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.database.Poly;
import lineage.bean.lineage.Craft;
import lineage.bean.lineage.Quest;
import lineage.database.ItemDatabase;
import lineage.database.PolyDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.CraftController;
import lineage.world.controller.QuestController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestInstance;

public class Black extends QuestInstance {
	
	public Black(Npc npc){
		super(npc);
		
		Item i = ItemDatabase.find("커츠의 검");
		if(i != null){
			craft_list.put("black knight chief", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("마력을 잃은 커츠의 검"), 1) );
			list.put(i, l);
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		Poly p = PolyDatabase.getPolyName("커츠(48)");
		if(p != null){
			if(p.getGfxId() == pc.getGfx()){
						pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "Black3"));
					}else{
						pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "Black1"));
					}
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		// 환생의 물약을 건네준다
		if(action.equalsIgnoreCase("black knight chief")){
			Item craft = craft_list.get(action);
			if(craft!=null){
				List<Craft> l = list.get(craft);
				// 재료 확인.
				if(CraftController.isCraft(pc, l, true)){
					// 재료 제거
					CraftController.toCraft(pc, l);
					// 아이템 지급.
					CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
					pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
				}
			}
		}
	}

}
