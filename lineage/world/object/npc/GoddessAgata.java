package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_SoundEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class GoddessAgata extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		List<String> list = new ArrayList<String>();

		list.add(String.valueOf(pc.getLevel() * Lineage.player_lost_exp_aden_rate));
		list.add(String.valueOf(Lineage.player_lost_exp_rate * 100));

		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "restore", null, list));
		pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 27803)); 
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if (action.equalsIgnoreCase("exp")) {
			if (pc.getLostExp() > 0) {
				player_lost_exp(pc, false);
			} else {
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 739));
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "")); // 창제거
			}
		}
		if (action.equalsIgnoreCase("material3")) {
			if (pc.getInventory() != null) {
				createItem(pc, "아데나", 1000000, "마물의 기운", 5, "불멸의 가호", 1,1, 200);
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "")); // 창제거
		}
	}
}


	private void player_lost_exp(PcInstance pc, boolean type) {
			if (pc.getInventory().isAden(pc.getLevel() * Lineage.player_lost_exp_aden_rate, true)) {
				// 경험치90% 복구.
				pc.setExp(pc.getExp() + (pc.getLostExp() * Lineage.player_lost_exp_rate));
				pc.setLostExp(0);
				pc.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), pc));
				pc.toSender(new S_ObjectEffect( pc, 3944), true);
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "")); // 창제거
			} else {
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "")); // 창제거
			}
		}
	
	public void createItem(PcInstance pc, String itemName, long count, String newItemName, int bless, long createCount, double percent) {
		if (pc.getInventory() != null) {
			// 재료
			ItemInstance item1 = null;

			for (ItemInstance i : pc.getInventory().getList()) {
				if (i != null && i.getItem() != null && i.getItem().getName().equalsIgnoreCase(itemName) && i.getBress() == 1 && i.getCount() >= count && !i.isEquipped())
					item1 = i;
				
				if (item1 != null)
					break;
			}

			if (item1 != null) {
				Item i = ItemDatabase.find(newItemName);
				
				if (i != null) {
					if (pc.getGm() > 0 || Util.random(1, 100) < (percent)) {
						ItemInstance temp = pc.getInventory().find(i.getName(), bless, i.isPiles());

						if (temp != null && (temp.getBress() != bless || temp.getEnLevel() != 0))
							temp = null;

						if (temp == null) {
							// 겹칠수 있는 아이템이 존재하지 않을경우.
							if (i.isPiles()) {
								temp = ItemDatabase.newInstance(i);
								temp.setObjectId(ServerDatabase.nextItemObjId());
								temp.setBress(bless);
								temp.setEnLevel(0);
								temp.setCount(createCount);
								temp.setDefinite(true);
								pc.getInventory().append(temp, true);
							} else {
								for (int idx = 0; idx < createCount; idx++) {
									temp = ItemDatabase.newInstance(i);
									temp.setObjectId(ServerDatabase.nextItemObjId());
									temp.setBress(bless);
									temp.setEnLevel(0);
									temp.setDefinite(true);
									pc.getInventory().append(temp, true);
								}
							}
						} else {
							// 겹치는 아이템이 존재할 경우.
							pc.getInventory().count(temp, temp.getCount() + createCount, true);
						}

						ChattingController.toChatting(pc, String.format("'%s' 제작에 성공하였습니다! ", newItemName), Lineage.CHATTING_MODE_MESSAGE);
					} else {
						ChattingController.toChatting(pc, String.format("'%s' 제작에 실패하였습니다. ", newItemName), Lineage.CHATTING_MODE_MESSAGE);
					}

					pc.getInventory().count(item1, item1.getCount() - count, true);
				}
			} else {
				ChattingController.toChatting(pc, String.format("%s(%,d)가 필요합니다.", itemName, count), Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}
	
	public void createItem(PcInstance pc, String name1, long count, String name2, long count2, String newItemName, int bless, long createCount, double percent) {
		if (pc.getInventory() != null) {
			// 재료
			ItemInstance item1 = null;
			// 재료
			ItemInstance item2 = null;

			for (ItemInstance i : pc.getInventory().getList()) {
				if (i != null && i.getItem() != null && i.getItem().getName().equalsIgnoreCase(name1) && !i.isEquipped() && i.getCount() >= count)
					item1 = i;
				if (i != null && i.getItem() != null && i.getItem().getName().equalsIgnoreCase(name2) && !i.isEquipped() && i.getCount() >= count2)
					item2 = i;

				if (item1 != null && item2 != null)
					break;
			}

			if (item1 != null && item2 != null) {
				Item i = ItemDatabase.find(newItemName);

				if (i != null) {
					if (pc.getGm() > 0 || Util.random(1, 100) < (percent)) {
						ItemInstance temp = pc.getInventory().find(i.getName(), bless, i.isPiles());

						if (temp != null && (temp.getBress() != bless || temp.getEnLevel() != 0))
							temp = null;

						if (temp == null) {
							// 겹칠수 있는 아이템이 존재하지 않을경우.
							if (i.isPiles()) {
								temp = ItemDatabase.newInstance(i);
								temp.setObjectId(ServerDatabase.nextItemObjId());
								temp.setBress(bless);
								temp.setEnLevel(0);
								temp.setCount(createCount);
								temp.setDefinite(true);
								pc.getInventory().append(temp, true);
							} else {
								for (int idx = 0; idx < createCount; idx++) {
									temp = ItemDatabase.newInstance(i);
									temp.setObjectId(ServerDatabase.nextItemObjId());
									temp.setBress(bless);
									temp.setEnLevel(0);
									temp.setDefinite(true);
									pc.getInventory().append(temp, true);
								}
							}
						} else {
							// 겹치는 아이템이 존재할 경우.
							pc.getInventory().count(temp, temp.getCount() + createCount, true);
						}

						ChattingController.toChatting(pc, String.format("'%s' 제작에 성공하였습니다! ", newItemName), Lineage.CHATTING_MODE_MESSAGE);
					} else {
						ChattingController.toChatting(pc, String.format("'%s' 제작에 실패하였습니다. ", newItemName), Lineage.CHATTING_MODE_MESSAGE);
					}

					pc.getInventory().count(item1, item1.getCount() - count, true);
					pc.getInventory().count(item2, item2.getCount() - count2, true);
				}
			} else {
				ChattingController.toChatting(pc, String.format("%s(%,d), %s(%,d) 필요합니다.", name1, count, name2, count2), Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}
}