package lineage.world.object.npc.quest;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.bean.lineage.Quest;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;
import lineage.world.controller.QuestController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestInstance;

public class Talass extends object {


	public class CreateItem {
		public String itemName;
		public boolean isCheckBless;
		public int bless;
		public boolean isCheckEnchant;
		public int enchant;
		public int count;

		/**
		 * @param itemName       : 재료 아이템 이름
		 * @param isCheckBless   : 재료 축여부 체크
		 * @param bless          : 축복(0~2)
		 * @param isCheckEnchant : 재료 인첸트 체크
		 * @param enchant        : 인첸트
		 * @param count          : 수량
		 */
		public CreateItem(String itemName, boolean isCheckBless, int bless, boolean isCheckEnchant, int enchant,
				int count) {
			this.itemName = itemName;
			this.isCheckBless = isCheckBless;
			this.bless = bless;
			this.isCheckEnchant = isCheckEnchant;
			this.enchant = enchant;
			this.count = count;
		}

	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {

		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "talass"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
	
		if (pc.getInventory() != null) {
			List<CreateItem> createList = new ArrayList<CreateItem>();
			List<CreateItem> createList2 = new ArrayList<CreateItem>();
			List<ItemInstance> itemList = new ArrayList<ItemInstance>();
			
			//사이하의 활 제작
			if (action.equalsIgnoreCase("request bow of sayha")) {
				createList.add(new CreateItem("장궁", true, 1, true, 0, 1));
				createList.add(new CreateItem("풍룡 비늘", true, 1, true, 0, 15));
				createList.add(new CreateItem("그리폰의 깃털", true, 1, true, 0, 30));
				createList.add(new CreateItem("바람의 눈물", true, 1, true, 0, 50));

				
				checkItem(pc, createList, itemList);

				createItem(pc, createList, createList2, itemList, "사이하의 활", 1, 0, 1);
			}
			//타라스 장갑과 부츠
			if (action.equalsIgnoreCase("request taras boots")) {
				createList.add(new CreateItem("데몬의 부츠", true, 1, true, 0, 1));
				createList.add(new CreateItem("마물의 기운", true, 1, true, 0, 50));

				
				checkItem(pc, createList, itemList);

				createItem(pc, createList, createList2, itemList, "타라스의 부츠", 1, 0, 1);
			}
			if (action.equalsIgnoreCase("request taras gloves")) {
				createList.add(new CreateItem("데몬의 장갑", true, 1, true, 0, 1));
				createList.add(new CreateItem("마물의 기운", true, 1, true, 0, 100));

				
				checkItem(pc, createList, itemList);

				createItem(pc, createList, createList2, itemList, "타라스의 장갑", 1, 0, 1);
			}
		}
	}

	public void checkItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList) {
		if (createList != null && itemList != null) {
			if (itemList.size() > 0)
				itemList.clear();

			for (CreateItem list : createList) {
				for (ItemInstance i : pc.getInventory().getList()) {				
					if (i.getItem() != null && i.getItem().getName().equalsIgnoreCase(list.itemName)
							&& i.getCount() >= list.count && !i.isEquipped()) {
						// 축여부 체크일 경우
						if (list.isCheckBless) {
							// 인첸트 체크일 경우
							if (list.isCheckEnchant) {
								if (i.getBress() == list.bless && i.getEnLevel() == list.enchant) {
									itemList.add(i);
									break;
								}
							} else {
								if (i.getBress() == list.bless) {
									itemList.add(i);
									break;
								}
							}
						} else {
							// 인첸트 체크일 경우
							if (list.isCheckEnchant) {
								if (i.getEnLevel() == list.enchant) {
									itemList.add(i);
									break;
								}
							} else {
								itemList.add(i);
								break;
							}
						}
					}
				}
			}
		}
	}

	public void createItem(PcInstance pc, List<CreateItem> createList, List<CreateItem> createList2,
			List<ItemInstance> itemList, String createItemName, int bless, int enchant, int count) {
		if ((createList.size() > 0 && itemList.size() > 0 && createList.size() == itemList.size())
				|| (createList2.size() > 0 && itemList.size() > 0 && createList2.size() == itemList.size())) {

			Item i = ItemDatabase.find(createItemName);

			if (i != null) {
				ItemInstance temp = pc.getInventory().find(i.getName(), bless, i.isPiles());

				if (temp != null && (temp.getBress() != bless || temp.getEnLevel() != enchant))
					temp = null;

				if (temp == null) {
					// 겹칠수 있는 아이템이 존재하지 않을경우.
					if (i.isPiles()) {
						temp = ItemDatabase.newInstance(i);
						temp.setObjectId(ServerDatabase.nextItemObjId());
						temp.setBress(bless);
						temp.setEnLevel(enchant);
						temp.setCount(count);
						temp.setDefinite(true);
						pc.getInventory().append(temp, true);
					} else {
						for (int idx = 0; idx < count; idx++) {
							temp = ItemDatabase.newInstance(i);
							temp.setObjectId(ServerDatabase.nextItemObjId());
							temp.setBress(bless);
							temp.setEnLevel(enchant);
							temp.setDefinite(true);
							pc.getInventory().append(temp, true);
						}
					}
				} else {
					// 겹치는 아이템이 존재할 경우.
					pc.getInventory().count(temp, temp.getCount() + count, true);
				}

				if (createList2.size() == 0) {
					for (CreateItem list : createList) {
						for (ItemInstance item : itemList) {
							if (item != null && item.getItem() != null
									&& list.itemName.equalsIgnoreCase(item.getItem().getName()))
								pc.getInventory().count(item, item.getCount() - list.count, true);
						}
					}
				} else {
					for (CreateItem list : createList2) {
						for (ItemInstance item : itemList) {
							if (item != null && item.getItem() != null
									&& list.itemName.equalsIgnoreCase(item.getItem().getName()))
								pc.getInventory().count(item, item.getCount() - list.count, true);
						}
					}
				}

				ChattingController.toChatting(pc, String.format("'%s' 제작 성공!", createItemName), Lineage.CHATTING_MODE_MESSAGE);
				//창 닫기
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
			}
		} else {
			String msg = "";

			if (createList2.size() > 0) {
				int idx = 0;

				for (CreateItem list : createList) {
					idx++;

					if (list.enchant > 0 && list.count > 0)
						msg += String.format("+%d %s(%,d)", list.enchant, list.itemName, list.count);
					else if (list.enchant > 0 && list.count == 0)
						msg += String.format("+%d %s", list.enchant, list.itemName);
					else if (list.enchant == 0 && list.count > 0)
						msg += String.format("%s(%,d)", list.itemName, list.count);
					else if (list.enchant == 0 && list.count == 0)
						msg += String.format("%s", list.itemName);

					if (idx < createList.size())
						msg += "개, ";
				}

				idx = 0;
				msg += " 또는 ";

				for (CreateItem list : createList2) {
					idx++;

					if (list.enchant > 0 && list.count > 0)
						msg += String.format("+%d %s(%,d)", list.enchant, list.itemName, list.count);
					else if (list.enchant > 0 && list.count == 0)
						msg += String.format("+%d %s", list.enchant, list.itemName);
					else if (list.enchant == 0 && list.count > 0)
						msg += String.format("%s(%,d)", list.itemName, list.count);
					else if (list.enchant == 0 && list.count == 0)
						msg += String.format("%s", list.itemName);

					if (idx < createList2.size())
						msg += "개, ";
				}
			} else {
				int idx = 0;

				for (CreateItem list : createList) {
					idx++;

					if (list.enchant > 0 && list.count > 0)
						msg += String.format("+%d %s(%,d)", list.enchant, list.itemName, list.count);
					else if (list.enchant > 0 && list.count == 0)
						msg += String.format("+%d %s", list.enchant, list.itemName);
					else if (list.enchant == 0 && list.count > 0)
						msg += String.format("%s(%,d)", list.itemName, list.count);
					else if (list.enchant == 0 && list.count == 0)
						msg += String.format("%s", list.itemName);

					if (idx < createList.size())
						msg += "개, ";
				}
			}

			ChattingController.toChatting(pc, String.format("[%s] %s개의 재료가 필요합니다.", createItemName, msg), Lineage.CHATTING_MODE_MESSAGE);
			//창 닫기
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));

		}
	}
}



/*QuestInstance {
	
	public Talass(Npc npc){
		super(npc);
		
		// hyper text 패킷 구성에 해당 구간을 npc 이름으로 사용함.
		temp_request_list.add( npc.getNameId() );
		
		// 제작 처리 초기화.
		Item i = ItemDatabase.find("사이하의 활");
		if(i != null){
			craft_list.put("request bow of sayha", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("장궁"), 1) );
			l.add( new Craft(ItemDatabase.find("풍룡 비늘"), 15) );
			l.add( new Craft(ItemDatabase.find("그리폰의 깃털"), 30) );
			l.add( new Craft(ItemDatabase.find("바람의 눈물"), 50) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("타라스의 부츠");
		if(i != null){
			craft_list.put("request taras boots", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("데몬의 부츠"), 1) );
			l.add( new Craft(ItemDatabase.find("마물의 기운"), 50) );
			l.add( new Craft(ItemDatabase.find("아데나"), 100000) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("타라스의 장갑");
		if(i != null){
			craft_list.put("request taras gloves", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("데몬의 장갑"), 1) );
			l.add( new Craft(ItemDatabase.find("마물의 기운"), 100) );
			l.add( new Craft(ItemDatabase.find("아데나"), 100000) );
			list.put(i, l);
		}
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		switch(pc.getClassType()){
			case 0x03:
				Quest q = QuestController.find(pc, Lineage.QUEST_WIZARD_LV30);
				if(q == null){
					pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "talass"));
				}else{
					switch(q.getQuestStep()){
						case 4:
							pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "talassE1"));
							break;
						case 5:
							pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "talassE2"));
							break;
						default:
							pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "talass"));
							break;
					}
				}
				break;
			default:
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "talass"));
				break;
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if(action.equalsIgnoreCase("quest 16 talassE2")){
			// 언데드의 뼈
			Quest q = QuestController.find(pc, Lineage.QUEST_WIZARD_LV30);
			if(q==null || q.getQuestStep()!=4){
				toTalk(pc, null);
			}else{
				q.setQuestStep(5);
				toTalk(pc, null);
			}
		}else if(action.equalsIgnoreCase("request crystal staff")){
			// 언데드의 뼈조각을 건네 준다.
			Quest q = QuestController.find(pc, Lineage.QUEST_WIZARD_LV30);
			Item craft = craft_list.get(action);
			if(craft!=null && q!=null && q.getQuestStep()==5){
				List<Craft> l = list.get(craft);
				// 재료 확인.
				if(CraftController.isCraft(pc, l, true)){
					// 재료 제거
					CraftController.toCraft(pc, l);
					// 아이템 지급.
					CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
					// 퀘스트 스탭 변경.
					q.setQuestStep( 6 );
					// 안내창 띄우기.
					toTalk(pc, null);
				}
			}
		}else{
			super.toTalk(pc, action, type, cbp);
		}
	}
	
} */
