package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 반지교환사 extends object {

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
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "ringtrade"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		if (pc.getInventory() != null) {
			List<CreateItem> createList = new ArrayList<CreateItem>();
			List<CreateItem> createList2 = new ArrayList<CreateItem>();
			List<ItemInstance> itemList = new ArrayList<ItemInstance>();

			if (action.equalsIgnoreCase("민첩1")) {

				createList.add(new CreateItem("민첩의 반지", false, 1, false, 0, 1));
				checkItem(pc, createList, itemList);

				createItem(pc, createList, createList2, itemList, "완력의 반지", 1, 0, 1);

			} else if (action.equalsIgnoreCase("민첩2")) {

				createList.add(new CreateItem("민첩의 반지", false, 1, false, 0, 1));
				checkItem(pc, createList, itemList);

				createItem(pc, createList, createList2, itemList, "지식의 반지", 1, 0, 1);

			} else if (action.equalsIgnoreCase("지식1")) {

				createList.add(new CreateItem("지식의 반지", false, 1, false, 0, 1));
				checkItem(pc, createList, itemList);

				createItem(pc, createList, createList2, itemList, "완력의 반지", 1, 0, 1);
			} else if (action.equalsIgnoreCase("지식2")) {

				createList.add(new CreateItem("지식의 반지", false, 1, false, 0, 1));
				checkItem(pc, createList, itemList);

				createItem(pc, createList, createList2, itemList, "민첩의 반지", 1, 0, 1);
				
			} else if (action.equalsIgnoreCase("완력1")) {

				createList.add(new CreateItem("완력의 반지", false, 1, false, 0, 1));
				checkItem(pc, createList, itemList);

				createItem(pc, createList, createList2, itemList, "지식의 반지", 1, 0, 1);
			} else if (action.equalsIgnoreCase("완력2")) {

				createList.add(new CreateItem("완력의 반지", false, 1, false, 0, 1));
				checkItem(pc, createList, itemList);

				createItem(pc, createList, createList2, itemList, "민첩의 반지", 1, 0, 1);

//			} else if (action.equalsIgnoreCase("무관의 양손검")) {
//				
//				createList.add(new CreateItem("양손검", false, 1, true, 9, 1));
//				createList.add(new CreateItem("마물의 기운", false, 1, false, 0, 100));				
//				checkItem(pc, createList, itemList);
//				
//				createItem(pc, createList, createList2, itemList, "무관의 양손검", 1, 0, 1);

//			} else if (action.equalsIgnoreCase("붉은 기사의 대검")) {
//				
//				createList.add(new CreateItem("대검", false, 1, true, 9, 1));
//				createList.add(new CreateItem("마물의 기운", false, 1, false, 0, 100));			
//				checkItem(pc, createList, itemList);
//				
//				createItem(pc, createList, createList2, itemList, "붉은 기사의 대검", 1, 0, 1);
//				

//			} else if (action.equalsIgnoreCase("사이하의 활")) {
//				
//				createList.add(new CreateItem("크로스 보우", false, 1, true, 0, 1));
//				createList.add(new CreateItem("그리폰의 깃털", false, 1, false, 0, 5));
//				createList.add(new CreateItem("마물의 기운", false, 1, false, 0, 100));				
//				checkItem(pc, createList, itemList);
//				
//				if (createList.size() != itemList.size()) {
//					createList2.add(new CreateItem("크로스 보우", false, 1, true, 9, 1));
//					createList2.add(new CreateItem("마물의 기운", false, 1, false, 0, 50));				
//					checkItem(pc, createList2, itemList);
//				}
//				
//				createItem(pc, createList, createList2, itemList, "사이하의 활", 1, 0, 1);
//				
//			} else if (action.equalsIgnoreCase("달의 장궁")) {
//				
//				createList.add(new CreateItem("장궁", false, 1, true, 0, 1));
//				createList.add(new CreateItem("유니콘의 뿔", false, 1, false, 0, 5));
//				createList.add(new CreateItem("마물의 기운", false, 1, false, 0, 100));				
//				checkItem(pc, createList, itemList);
//				
//				if (createList.size() != itemList.size()) {
//					createList2.add(new CreateItem("장궁", false, 1, true, 9, 1));
//					createList2.add(new CreateItem("마물의 기운", false, 1, false, 0, 50));	
//					checkItem(pc, createList2, itemList);
//				}
//				
//				createItem(pc, createList, createList2, itemList, "달의 장궁", 1, 0, 1);
//				
//			} else if (action.equalsIgnoreCase("파괴의 장궁")) {
//				
//				createList.add(new CreateItem("달의 장궁", false, 1, true, 9, 1));
//				createList.add(new CreateItem("마물의 기운", false, 1, false, 0, 250));			
//				checkItem(pc, createList, itemList);
//				
//				createItem(pc, createList, createList2, itemList, "파괴의 장궁", 1, 0, 1);
//			} else if (action.equalsIgnoreCase("악몽의 장궁")) {
//				
//				createList.add(new CreateItem("사이하의 활", false, 1, true, 9, 1));
//				createList.add(new CreateItem("마물의 기운", false, 1, false, 0, 250));			
//				checkItem(pc, createList, itemList);
//				
//				createItem(pc, createList, createList2, itemList, "악몽의 장궁", 1, 0, 1);
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

				ChattingController.toChatting(pc, String.format("\\fT'%s' 제작 완료!", createItemName),
						Lineage.CHATTING_MODE_MESSAGE);
			}
		} else {
			String msg = "";

			if (createList2.size() > 0) {
				int idx = 0;

				for (CreateItem list : createList) {
					idx++;

					if (list.enchant > 0 && list.count > 1)
						msg += String.format("+%d %s(%,d)", list.enchant, list.itemName, list.count);
					else if (list.enchant > 0 && list.count == 1)
						msg += String.format("+%d %s", list.enchant, list.itemName);
					else if (list.enchant == 0 && list.count > 1)
						msg += String.format("%s(%,d)", list.itemName, list.count);
					else if (list.enchant == 0 && list.count == 1)
						msg += String.format("%s", list.itemName);

					if (idx < createList.size())
						msg += ", ";
				}

				idx = 0;
				msg += " 또는 ";

				for (CreateItem list : createList2) {
					idx++;

					if (list.enchant > 0 && list.count > 1)
						msg += String.format("+%d %s(%,d)", list.enchant, list.itemName, list.count);
					else if (list.enchant > 0 && list.count == 1)
						msg += String.format("+%d %s", list.enchant, list.itemName);
					else if (list.enchant == 0 && list.count > 1)
						msg += String.format("%s(%,d)", list.itemName, list.count);
					else if (list.enchant == 0 && list.count == 1)
						msg += String.format("%s", list.itemName);

					if (idx < createList2.size())
						msg += ", ";
				}
			} else {
				int idx = 0;

				for (CreateItem list : createList) {
					idx++;

					if (list.enchant > 0 && list.count > 1)
						msg += String.format("+%d %s(%,d)", list.enchant, list.itemName, list.count);
					else if (list.enchant > 0 && list.count == 1)
						msg += String.format("+%d %s", list.enchant, list.itemName);
					else if (list.enchant == 0 && list.count > 1)
						msg += String.format("%s(%,d)", list.itemName, list.count);
					else if (list.enchant == 0 && list.count == 1)
						msg += String.format("%s", list.itemName);

					if (idx < createList.size())
						msg += ", ";
				}
			}

			ChattingController.toChatting(pc, String.format("\\fT[%s]  %s 필요합니다.", createItemName, msg),
					Lineage.CHATTING_MODE_MESSAGE);
		}
	}
}
