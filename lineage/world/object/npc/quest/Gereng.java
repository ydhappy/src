package lineage.world.object.npc.quest;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.bean.lineage.Quest;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.CraftController;
import lineage.world.controller.QuestController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestInstance;

public class Gereng extends QuestInstance {
		
	public Gereng(Npc n){
		super(n);

		Item i = ItemDatabase.find("신비한 지팡이");
		if(i != null){
			craft_list.put("request bone piece of undead", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("언데드의 뼈조각"), 1) );
			list.put(i, l);
		}
		i = ItemDatabase.find("수정 지팡이");
		if(i != null){
			craft_list.put("request book of magical powers", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("언데드의 뼈"), 1) );
			l.add( new Craft(ItemDatabase.find("신비한 지팡이"), 1) );
			list.put(i, l);
		}
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		switch(pc.getClassType()){
			case 0x00:
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "gerengp1"));
				break;
			case 0x01:
				if(pc.getLevel()>=50){
					pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "gerengEv1"));
				}else{
					pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "gerengk1"));
				}
				break;
			case 0x02:
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "gerenge1"));
				break;
			case 0x03:
					if(pc.getLevel()<30){
						pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "gerengTe1"));
					}else{
						Quest q = QuestController.find(pc, Lineage.QUEST_WIZARD_LV30);
						if(q == null)
							q = QuestController.newQuest(pc, this, Lineage.QUEST_WIZARD_LV30);
						switch(q.getQuestStep()){
							case 0:
								pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "gerengw1"));
								break;
							case 1:
								pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "gerengT4"));
								break;
							default:
								pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "gerengTe1"));
								break;
						}
					}
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		Item craft = craft_list.get(action);
		if(craft != null){
			Quest q = QuestController.find(pc, Lineage.QUEST_WIZARD_LV30);
			if(action.equalsIgnoreCase("request bone piece of undead")){
				if(q!=null && q.getQuestStep()==0){
					List<Craft> l = list.get(craft);
					// 재료 확인.
					if(CraftController.isCraft(pc, l, true)){
						// 재료 제거
						CraftController.toCraft(pc, l);
						// 아이템 지급.
						CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
						// 퀘스트 스탭 변경.
						q.setQuestStep( 1 );
						// 안내창 띄우기.
						toTalk(pc, null);
					}
				}
			}else if(action.equalsIgnoreCase("request book of magical powers")){
				if(q!=null && q.getQuestStep()==1){
					List<Craft> l = list.get(craft);
					// 재료 확인.
					if(CraftController.isCraft(pc, l, true)){
						// 재료 제거
						CraftController.toCraft(pc, l);
						// 아이템 지급.
						CraftController.toCraft(this, pc, craft, 1, true);
						// 퀘스트 스탭 변경.
						q.setQuestStep( 2 );
						// 안내창 띄우기.
						pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "gerengTe6"));
					}
				}
			}
		}
	}
	
}