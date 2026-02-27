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
import lineage.world.controller.CharacterController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;
import lineage.world.controller.QuestController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestInstance;

public class Tio extends QuestInstance {
	
	public Tio(Npc n){
		super(n);
		// 관리목록에 등록. toTimer가 호출되도록 하기 위해.
		CharacterController.toWorldJoin(this);
		// 20초 단위로 멘트 표현.
		ment_show_sec = 20;
		// 지하 던전에는 무엇이 있는 걸까?
		list_ment.add("$1733");
		// 그 곳을 이렇게 계속 방치하다가는 무언가 큰일이 벌어질 거 같아...
		list_ment.add("$1734");
		
		Item i = ItemDatabase.find("계곡의 증표");
		if(i != null){
			craft_list.put(Lineage.QUEST_TIO, i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("붉은 열쇠"), 1) );
			list.put(i, l);
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		Quest q = QuestController.find(pc, Lineage.QUEST_TIO);
		if (q == null)
			q = QuestController.newQuest(pc, this, Lineage.QUEST_TIO);
		if (q.getQuestStep() == 0)
			pc.toSender(new S_Html( this, "tio"));
		else
			pc.toSender(new S_Html( this, "tio6"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if(action.equalsIgnoreCase("request amulet of valley")){
			// 붉은열쇠를 건네준다. 
			// 숨겨진 계곡의 증표
			Item craft = craft_list.get(Lineage.QUEST_TIO);
			Quest q = QuestController.find(pc, Lineage.QUEST_TIO);
			if(craft!=null && q!=null && q.getQuestStep()==0) {
				List<Craft> l = list.get(craft);
				// 재료 확인.
				if(CraftController.isCraft(pc, l, true)){
					// 재료 제거
					CraftController.toCraft(pc, l);
					// 아이템 지급.
					CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
					// 퀘스트 스탭 변경.
					q.setQuestStep( 1 );
					//창닫기
					pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
				}
			}
		}
	}
	
}
