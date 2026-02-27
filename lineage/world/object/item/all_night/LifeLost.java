package lineage.world.object.item.all_night;

import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.LifeLostItem;
import lineage.database.ItemDatabase;
import lineage.database.LifeLostItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class LifeLost extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new LifeLost();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if(cha.getInventory() != null){
			ItemInstance item = cha.getInventory().value(cbp.readD());
			
			if (item != null && item.getItem() != null) {
				List<LifeLostItem> list = LifeLostItemDatabase.getList();
				
				if (list.size() > 0) {
					LifeLostItem lli = null;
					Item i = null;
					boolean result = false;
					boolean bugChcek = true;
					
					for (LifeLostItem li : list) {
						if (li.getItem().equalsIgnoreCase(item.getItem().getName())) {
							lli = li;
							break;
						}		
					}
					
					if (lli != null) {
						if (getBress() == 0 || getBress() == -128) {
							// 축복받은 생명의 나뭇잎
							if (Math.random() < lli.getBressChance()) {
								// 축복 성공시
								result = true;
								i = ItemDatabase.find(lli.getItemName());
								
								// 버그 확인
								if (i == null)
									bugChcek = false;
							} else {
								// 축복 실패시
								if (Math.random() < lli.getBressContinueChance()) {
									// 생명의 나뭇잎만 소멸
									result = true;
									i = null;
									
									if (ItemDatabase.find(lli.getItemName()) == null)
										bugChcek = false;
								} else {
									result = false;
								}
							}
						} else {
							// 일반 생명의 나뭇잎
							if (Math.random() < lli.getNomalChance()) {
								// 일반 성공시
								result = true;
								i = ItemDatabase.find(lli.getItemName());
								
								// 버그 확인
								if (i == null)
									bugChcek = false;
							}
						}
						
						if (bugChcek) {						
							if (result) {
								if (i != null) {
									String name = item.getItem().getName();
									// 나뭇잎 삭제
									cha.getInventory().count(this, getCount()-1, true);
									// 기운템 삭제
									cha.getInventory().count(item, item.getCount()-1, true);
									// 성공
									ItemInstance temp = cha.getInventory().find(i.getName(), lli.getBress(), i.isPiles());
									int count = (int) Util.random(lli.getMinCount(), lli.getMaxCount());

									if (temp != null && (temp.getBress() != lli.getBress() || temp.getEnLevel() != lli.getEn()))
										temp = null;

									if (temp == null) {
										// 겹칠수 있는 아이템이 존재하지 않을경우.
										if (i.isPiles()) {
											temp = ItemDatabase.newInstance(i);
											temp.setObjectId(ServerDatabase.nextItemObjId());
											temp.setBress(lli.getBress());
											temp.setEnLevel(lli.getEn());
											temp.setCount(count);
											temp.setDefinite(true);
											cha.getInventory().append(temp, true);
										} else {
											
											for (int idx = 0; idx < count; idx++) {
												temp = ItemDatabase.newInstance(i);
												temp.setObjectId(ServerDatabase.nextItemObjId());
												temp.setBress(lli.getBress());
												temp.setEnLevel(lli.getEn());
												temp.setDefinite(true);
												cha.getInventory().append(temp, true);
											}
										}
									} else {
										// 겹치는 아이템이 존재할 경우.
										cha.getInventory().count(temp, temp.getCount() + count, true);
									}

									ChattingController.toChatting(cha, String.format("%s 새 생명이 부여 되었습니다.", Util.getStringWord(name, "은", "는")), Lineage.CHATTING_MODE_MESSAGE);
									cha.toSender(new S_ObjectEffect(cha, 15357), true);
									LifeLostItemDatabase.sendMessageLife(cha, lli.getItem());
								} else {
									// 나뭇잎만 소멸
									cha.getInventory().count(this, getCount()-1, true);
									ChattingController.toChatting(cha, String.format("%s 기운을 흡수하지 못하였으나, 다행히 소멸하지 않았습니다.", Util.getStringWord(item.getName(), "은", "는")), Lineage.CHATTING_MODE_MESSAGE);
								}
							} else {
								// 나뭇잎, 아이템 소멸
								cha.getInventory().count(this, getCount()-1, true);
								//ChattingController.toChatting(cha, "기운을 흡수하지 못하고 소멸하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
								ChattingController.toChatting(cha, String.format("%s 기운을 흡수하지 못하고 소멸하였습니다.", Util.getStringWord(item.getName(), "은", "는")), Lineage.CHATTING_MODE_MESSAGE);
								cha.getInventory().count(item, item.getCount()-1, true);
								
							}
						}
					} else {
						ChattingController.toChatting(cha, "기운을 잃은 아이템에 사용가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					}
				}
			}
		}
	}
}
