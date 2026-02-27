package lineage.world.object.npc.quest;

import lineage.bean.database.Npc;
import lineage.bean.lineage.Quest;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.CraftController;
import lineage.world.controller.QuestController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestInstance;

public class Lyra extends QuestInstance {
	
	public Lyra(Npc npc){
		super(npc);
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		if (Lineage.event_lyra) {
			if(ServerDatabase.isNight()) {
				pc.toSender(new S_Html( this, "lyra1"));
			} else {
				Quest q = QuestController.find(pc, Lineage.QUEST_LYRA);
				if (q != null && q.getQuestStep() > 0) {
					pc.toSender(new S_Html( this, "lyraEv3"));
				} else {
					// 라이라와 계약하기위해 안내창
					pc.toSender(new S_Html( this, "lyraEv1"));
				}
			}
		} else {
			pc.toSender(new S_Html( this, "lyra1"));
		}
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		// 비활성화 된 이벤트라면 무시.
		if (!Lineage.event_lyra)
			return;
		// 퀘스트 추출.
		Quest q = QuestController.find(pc, Lineage.QUEST_LYRA);
		// 퀘스트가 아직 없다면 생성하기.
		if (q == null)
			q = QuestController.newQuest(pc, this, Lineage.QUEST_LYRA);
		// 처리구간.
		if (action.equalsIgnoreCase("contract1")) {
			// 토템 계약하기.
			q.setQuestStep(1);
			// 안내창 띄우기.
			pc.toSender(new S_Html( this, "lyraEv2"));

		} else if (action.equalsIgnoreCase("contract1yes")) {
			// 정산후 재계약한다.
			toQuestItem(pc);
			// 안내창 띄우기.
			pc.toSender(new S_Html( this, "lyraEv5"));

		} else if (action.equalsIgnoreCase("contract1no")) {
			// 정산후 계약을 끝낸다
			q.setQuestStep(0);
			toQuestItem(pc);
			// 안내창 띄우기.
			pc.toSender(new S_Html( this, "lyraEv5"));
		}
	}
	
	/**
	 * 중복코드 방지용.
	 * 	: 퀘스트 아이템 지급처리 함수.
	 */
	private void toQuestItem(PcInstance pc){
		int aden_count = CraftController.toCraft(pc, "아투바의 토템", 200, true);
		aden_count += CraftController.toCraft(pc, "네루가의 토템", 180, true);
		aden_count += CraftController.toCraft(pc, "간디의 토템", 80, true);
		aden_count += CraftController.toCraft(pc, "로바의 토템", 120, true);
		aden_count += CraftController.toCraft(pc, "두다-마라의 토템", 120, true);
		if(aden_count > 0)
			CraftController.toCraft(this, pc, ItemDatabase.find("아데나"), aden_count, true, 0, 0, 1);
	}
	
}
