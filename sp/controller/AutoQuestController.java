package sp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lineage.bean.database.Item;
import lineage.bean.lineage.Quest;
import lineage.database.ItemDatabase;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;
import lineage.world.controller.QuestController;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class AutoQuestController {

	/**
	 * 월드 접속시 호출됨.
	 * 레벨업 시 호출됨.
	 *  : 
	 * @param pc
	 */
	static public void toQuest(PcInstance pc) {
		//
		if (pc.getLevel() >= 15) {
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_ROYAL)
				toQuest(pc, Lineage.QUEST_ROYAL_LV15);
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT)
				toQuest(pc, Lineage.QUEST_KNIGHT_LV15);
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_ELF)
				toQuest(pc, Lineage.QUEST_ELF_LV15);
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_WIZARD)
				toQuest(pc, Lineage.QUEST_WIZARD_LV15);
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_DARKELF)
				toQuest(pc, Lineage.QUEST_DARKELF_LV15);
		}
		if (pc.getLevel() >= 30) {
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_ROYAL)
				toQuest(pc, Lineage.QUEST_ROYAL_LV30);
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT)
				toQuest(pc, Lineage.QUEST_KNIGHT_LV30);
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_ELF)
				toQuest(pc, Lineage.QUEST_ELF_LV30);
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_WIZARD)
				toQuest(pc, Lineage.QUEST_WIZARD_LV30);
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_DARKELF)
				toQuest(pc, Lineage.QUEST_DARKELF_LV30);
		}
		if (pc.getLevel() >= 45) {
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_ROYAL)
				toQuest(pc, Lineage.QUEST_ROYAL_LV45);
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT)
				toQuest(pc, Lineage.QUEST_KNIGHT_LV45);
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_ELF)
				toQuest(pc, Lineage.QUEST_ELF_LV45);
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_WIZARD)
				toQuest(pc, Lineage.QUEST_WIZARD_LV45);
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_DARKELF)
				toQuest(pc, Lineage.QUEST_DARKELF_LV45);
		}
		if (pc.getLevel() >= 50) {
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_ROYAL)
				toQuest(pc, Lineage.QUEST_ROYAL_LV50);
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT)
				toQuest(pc, Lineage.QUEST_KNIGHT_LV50);
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_ELF)
				toQuest(pc, Lineage.QUEST_ELF_LV50);
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_WIZARD)
				toQuest(pc, Lineage.QUEST_WIZARD_LV50);
			if (pc.getClassType() == Lineage.LINEAGE_CLASS_DARKELF)
				toQuest(pc, Lineage.QUEST_DARKELF_LV50);
		}
	}
	
	static private void toQuest(PcInstance pc, String action) {
		Quest q = QuestController.find(pc, action);
		if(q == null)
			q = QuestController.newQuest(pc, null, action);
		if(q.getQuestStep()==0) {
			List<String> list = new ArrayList<String>();
			// 클레스별 지급할 아이템 을 추가 하세요.
			switch (pc.getClassType()) {
			case Lineage.LINEAGE_CLASS_ROYAL:
				if (action.equalsIgnoreCase(Lineage.QUEST_ROYAL_LV15)) {
					list.add("붉은 망토|1");
					///list.add("마법서 (트루 타겟)|1");
				}
				if (action.equalsIgnoreCase(Lineage.QUEST_ROYAL_LV30)) {
					list.add("군주의 위엄|1");
					//list.add("마법서 (콜 클렌)|1");
				}
				if (action.equalsIgnoreCase(Lineage.QUEST_ROYAL_LV45)) {
					list.add("수호자의 반지|1");
				}
				if (action.equalsIgnoreCase(Lineage.QUEST_ROYAL_LV50)) {
					list.add("황금 지휘봉|1");
				}

				break;
			case Lineage.LINEAGE_CLASS_KNIGHT:
				if (action.equalsIgnoreCase(Lineage.QUEST_KNIGHT_LV15)) {
					list.add("붉은 기사의 두건|1");
				}
				if (action.equalsIgnoreCase(Lineage.QUEST_KNIGHT_LV30)) {
					list.add("붉은 기사의 검|1");
					list.add("붉은 기사의 방패|1");
				}
				if (action.equalsIgnoreCase(Lineage.QUEST_KNIGHT_LV45)) {
					list.add("용기의 벨트|1");
				}
				if (action.equalsIgnoreCase(Lineage.QUEST_KNIGHT_LV50)) {
					list.add("데스 블레이드|1");
				}
				break;
			case Lineage.LINEAGE_CLASS_ELF:
				if (action.equalsIgnoreCase(Lineage.QUEST_ELF_LV15)) {
					list.add("민첩함의 투구|1");
				}
				if (action.equalsIgnoreCase(Lineage.QUEST_ELF_LV30)) {
					list.add("요정족 티셔츠|1");
					//list.add("정령의 수정 (서먼 레서 엘리멘탈)|1");
				}
				if (action.equalsIgnoreCase(Lineage.QUEST_ELF_LV45)) {
					list.add("보호 장갑|1");
					//list.add("정령의 수정 (서먼 그레이터 엘리멘탈)|1");
				}
				if (action.equalsIgnoreCase(Lineage.QUEST_ELF_LV50)) {
					list.add("화염의 활|1");
					list.add("화염의 검|1");
				}
				break;
			case Lineage.LINEAGE_CLASS_WIZARD:
				if (action.equalsIgnoreCase(Lineage.QUEST_WIZARD_LV15)) {
					list.add("마력서|1");
				}
				if (action.equalsIgnoreCase(Lineage.QUEST_WIZARD_LV30)) {
					list.add("수정 지팡이|1");
				}
				if (action.equalsIgnoreCase(Lineage.QUEST_WIZARD_LV45)) {
					list.add("마나 망토|1");
				}
				if (action.equalsIgnoreCase(Lineage.QUEST_WIZARD_LV50)) {
					list.add("마나 수정구|1");
				}
				break;
			case Lineage.LINEAGE_CLASS_DARKELF:
				if (action.equalsIgnoreCase(Lineage.QUEST_DARKELF_LV15)) {
					list.add("그림자 가면|1");
					list.add("흑정령의 수정 (브링 스톤)|1");
				}
				if (action.equalsIgnoreCase(Lineage.QUEST_DARKELF_LV30)) {
					list.add("그림자 장갑|1");
					list.add("흑정령의 수정 (무빙 악셀레이션)|1");
				}
				if (action.equalsIgnoreCase(Lineage.QUEST_DARKELF_LV45)) {
					list.add("그림자 부츠|1");
					list.add("흑정령의 수정 (언케니 닷지)|1");
				}
				if (action.equalsIgnoreCase(Lineage.QUEST_DARKELF_LV50)) {
					list.add("핑거 오브 데스|1");
				}
				break;
			}
			//
			q.setQuestStep(100);
			// 지급
			for (String data : list) {
				StringTokenizer st = new StringTokenizer(data, "|");
				String name = st.nextToken();
				int count = Integer.valueOf(st.nextToken());
				// 아이템 지급
				CraftController.toCraft(pc, ItemDatabase.find(name), count, true, 0, 0);
			}
			//
			// ChattingController.toChatting(pc, "퀘스트가 완료되었습니다.",
			// Lineage.CHATTING_MODE_MESSAGE);
		}
	}
}