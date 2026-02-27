package lineage.world.object.npc.quest;

import lineage.bean.database.Npc;
import lineage.bean.lineage.Quest;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.QuestController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestInstance;

public class Mark extends QuestInstance {
	
	public Mark(Npc npc){
		super(npc);
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		if(pc.getClassType() == 0x01){
			if(pc.getLevel() < 30){
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "mark3"));
			}else{
				Quest q = QuestController.find(pc, Lineage.QUEST_KNIGHT_LV30);
				if(q == null)
					q = QuestController.newQuest(pc, this, Lineage.QUEST_KNIGHT_LV30);
				switch(q.getQuestStep()){
				case 0:
					pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "mark1"));
					break;
				case 1:
					pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "mark5"));
					break;
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
					pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "mark4"));
					break;
				case 7:
				case 8:
					pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "markcg"));
					break;
				}
			}
		}else{
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "mark3"));
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		Quest q = null;
		// 짐에 대해서
			q = QuestController.find(pc, Lineage.QUEST_KNIGHT_LV30);
			// 군터의 시험을 받는다
			if(action.equalsIgnoreCase("quest 13 mark2")){
				if(q!=null && q.getQuestStep()==0){
					// 퀘스트 스탭 변경.
					q.setQuestStep( 1 );
					// 안내창 띄우기.
					toTalk(pc, null);
				}
			}
	}
}