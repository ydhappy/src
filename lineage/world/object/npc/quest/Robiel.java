package lineage.world.object.npc.quest;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.bean.lineage.Quest;
import lineage.database.ItemDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.CraftController;
import lineage.world.controller.QuestController;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestInstance;
import lineage.world.object.item.quest.로빈후드의소개장;

public class Robiel extends QuestInstance {
	
	public Robiel(Npc npc){
		super(npc);
		
		Item i = ItemDatabase.find("로빈후드의 반지");
		if (i != null) {
			craft_list.put("C", i);

			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("달빛의 정기"), 1));
			l.add(new Craft(ItemDatabase.find("신성한 유니콘의 뿔"), 4));
			l.add(new Craft(ItemDatabase.find("불의 숨결"), 30));
			l.add(new Craft(ItemDatabase.find("물의 숨결"), 30));
			l.add(new Craft(ItemDatabase.find("바람의 숨결"), 30));
			l.add(new Craft(ItemDatabase.find("대지의 숨결"), 30));
			l.add(new Craft(ItemDatabase.find("어둠의 숨결"), 30));
		    l.add(new Craft(ItemDatabase.find("로빈후드의 메모지1"), 1));
			list.put(i, l);
		}

		i = ItemDatabase.find("달의 장궁");
		if (i != null) {
			craft_list.put("E", i);

			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("그리폰의 깃털"), 30));
			l.add(new Craft(ItemDatabase.find("미스릴 실"), 40));
			l.add(new Craft(ItemDatabase.find("오리하루콘 도금 뿔"), 1));
			l.add(new Craft(ItemDatabase.find("오리하루콘 판금"), 12));
			l.add(new Craft(ItemDatabase.find("정령의 눈물"), 20));
			l.add(new Craft(ItemDatabase.find("최고급 루비"), 1));
			l.add(new Craft(ItemDatabase.find("최고급 사파이어"), 1));
			l.add(new Craft(ItemDatabase.find("최고급 에메랄드"), 1));
			l.add(new Craft(ItemDatabase.find("최고급 다이아몬드"), 1));
			l.add(new Craft(ItemDatabase.find("로빈후드의 반지"), 1));
			l.add(new Craft(ItemDatabase.find("로빈후드의 메모지2"), 1));
			list.put(i, l);
		}
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		if (pc.getClassType() == Lineage.LINEAGE_CLASS_ELF) {
			Quest q = QuestController.find(pc, Lineage.QUEST_ROBIEL);
			if (q == null)
				q = QuestController.newQuest(pc, this, Lineage.QUEST_ROBIEL);
			switch (q.getQuestStep()) {
				case 0:
					pc.toSender(new S_Html( this, "robinhood1"));
					break;
				case 1:
					pc.toSender(new S_Html( this, "robinhood5"));
					q.setQuestStep(1);
					break;
				case 2:
					//if (pc.getInventory().find(로빈후드의소개장.class) == null) {
						
					 if (pc.getInventory().find(ItemDatabase.find("달빛의 정기")) == null)
						pc.toSender(new S_Html( this, "robinhood13"));
					else
						pc.toSender(new S_Html( this, "robinhood9"));
					break;
				case 3:
					if (pc.getInventory().find(ItemDatabase.find("로빈후드의 반지")) == null) {
						q.setQuestStep(2);
						toTalk(pc, cbp);
					} else
						pc.toSender(new S_Html( this, "robinhood11"));
					break;
			}
		} else {
			pc.toSender(new S_Html( this, "robinhood2"));
		}
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		Quest q = QuestController.find(pc, Lineage.QUEST_ROBIEL);
		if (q == null)
			return;
		//
		if (action.equalsIgnoreCase("A")) {
			// 사과주스를 건넨다.
			ItemInstance item = pc.getInventory().find(ItemDatabase.find("사과 주스"));
			if (item == null) {
				pc.toSender(new S_Html( this, "robinhood19"));
			} else {
				pc.getInventory().count(item, item.getCount() - 1, true);
				q.setQuestStep(1);
				pc.toSender(new S_Html( this, "robinhood4"));
			}
		} else if (action.equalsIgnoreCase("B")) {
			// 꼭 구해오도록 하겠습니다.
			q.setQuestStep(2);
			CraftController.toCraft(this, pc, ItemDatabase.find("로빈후드의 소개장"), 1, true, 0, 0, 1);
			CraftController.toCraft(this, pc, ItemDatabase.find("로빈후드의 메모지1"), 1, true, 0, 0, 1);
			pc.toSender(new S_Html( this, "robinhood13"));
		} else if (action.equalsIgnoreCase("C")) {
			// 재료와 메모지를 건넨다
			Item craft = craft_list.get(action);
			if (craft != null) {
				List<Craft> l = list.get(craft);
				if (CraftController.isCraft(pc, l, true)) {
					CraftController.toCraft(pc, l);
					CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
					CraftController.toCraft(this, pc, ItemDatabase.find("로빈후드의 메모지2"), 1, true, 0, 0, 1);
					q.setQuestStep(3);
					pc.toSender(new S_Html( this, "robinhood10"));
				} else {
					pc.toSender(new S_Html( this, "robinhood14"));
				}
			}
		} else if (action.equalsIgnoreCase("E")) {
			// 로빈후드의 반지와 재료를 건네준다.
			Item craft = craft_list.get(action);
			if (craft != null) {
				List<Craft> l = list.get(craft);
				if (CraftController.isCraft(pc, l, true)) {
					CraftController.toCraft(pc, l);
					CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
					q.setQuestStep(1);
					pc.toSender(new S_Html( this, "robinhood12"));
				} else {
					pc.toSender(new S_Html( this, "robinhood17"));
				}
			}
		} else 
			super.toTalk(pc, action, type, cbp, opt);
	}

}

/*
달빛의 정기 구해왔을때						- robinhood9
재료가 부족할때								- robinhood17
'재료와 메모지를 건넨다' 했을때 재료가 없을경우	- robinhood18
사과주스가 생각나는걸?						- robinhood19
아 그 반지는? 자네로구만~!					- robinhood11
완성이야! 대단해! 내 볼 한번 꼬집어 주겠나? 이게 꿈은 아니겠지?	- robinhood12
에바의 성지에 있는 신관 '지브릴'을 찾아가 보도록 하게나. 내가 써준 소개장	- robinhood13
각 <font fg=ffffaf>원소의 숨결</font> 30개씩을 가져왔는가?			- robinhood14
달빛의 정기</font>와 <font fg=ffffaf>신성한 유니콘의 뿔</font> 4개를 가지고 왔는가?	- robinhood15
최고급 보석 4종류</font>를 가져오도록 하게나.	- robinhood16
*/