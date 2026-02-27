package lineage.world.object.npc.craft;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_SoundEffect;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class Detecter extends object {

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
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "detecter1"));
		pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 27825)); 
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		if (pc.getInventory() != null) {
			List<CreateItem> createList = new ArrayList<CreateItem>();
			List<CreateItem> createList2 = new ArrayList<CreateItem>();
			List<ItemInstance> itemList = new ArrayList<ItemInstance>();
			
			//고대 검
			if (action.equalsIgnoreCase("request ancient greatsword")) {
				createList.add(new CreateItem("고대의 주문서", true, 1, true, 0, 1));
				createList.add(new CreateItem("잊혀진 대검", true, 1, true, 0, 1));
				
				checkItem(pc, createList, itemList);

				createItem(pc, createList, createList2, itemList, "고대의 대검", 1, 0, 1);
			}
			
			if (action.equalsIgnoreCase("request ancient sword")) {
				createList.add(new CreateItem("고대의 주문서", true, 1, true, 0, 1));
				createList.add(new CreateItem("잊혀진 검", true, 1, true, 0, 1));
				
				checkItem(pc, createList, itemList);

				createItem(pc, createList, createList2, itemList, "고대의 검", 1, 0, 1);
			}
			
			if (action.equalsIgnoreCase("request ancient bowgun")) {
				createList.add(new CreateItem("고대의 주문서", true, 1, true, 0, 1));
				createList.add(new CreateItem("잊혀진 보우건", true, 1, true, 0, 1));
				
				checkItem(pc, createList, itemList);

				createItem(pc, createList, createList2, itemList, "고대의 보우건", 1, 0, 1);
			}
			
			//고대 갑옷
			if (action.equalsIgnoreCase("request ancient plate mail")) {

				createList.add(new CreateItem("고대의 주문서", false, 1, true, 0, 1));
				createList.add(new CreateItem("잊혀진 판금 갑옷", true, 1, true, 0, 1));

				checkItem(pc, createList, itemList);

				createItem(pc, createList, createList2, itemList, "고대의 판금 갑옷", 1, 0, 1);
			}
			
			if (action.equalsIgnoreCase("request ancient leather armor")) {

				createList.add(new CreateItem("고대의 주문서", false, 1, true, 0, 1));
				createList.add(new CreateItem("잊혀진 가죽 갑옷", true, 1, true, 0, 1));

				checkItem(pc, createList, itemList);

				createItem(pc, createList, createList2, itemList, "고대의 가죽 갑옷", 1, 0, 1);
			}
			
			if (action.equalsIgnoreCase("request ancient robe")) {

				createList.add(new CreateItem("고대의 주문서", false, 1, true, 0, 1));
				createList.add(new CreateItem("잊혀진 로브", true, 1, true, 0, 1));

				checkItem(pc, createList, itemList);

				createItem(pc, createList, createList2, itemList, "고대의 로브", 1, 0, 1);
			}
			
			if (action.equalsIgnoreCase("request ancient scale mail")) {

				createList.add(new CreateItem("고대의 주문서", false, 1, true, 0, 1));
				createList.add(new CreateItem("잊혀진 비늘 갑옷", true, 1, true, 0, 1));

				checkItem(pc, createList, itemList);

				createItem(pc, createList, createList2, itemList, "고대의 비늘 갑옷", 1, 0, 1);
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

				ChattingController.toChatting(pc, String.format("'%s' 제작 완료!", createItemName), Lineage.CHATTING_MODE_MESSAGE);
				//창닫기
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
			//창닫기
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));

		}
	}
}

