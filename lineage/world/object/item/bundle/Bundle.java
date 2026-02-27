package lineage.world.object.item.bundle;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.ItemBundle;
import lineage.database.ItemBundleDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.bundle.Bundle.TYPE;

public class Bundle extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new Bundle();
		return item;
	}
	static synchronized public ItemInstance clone(ItemInstance item, TYPE type) {
		if (item == null)
			item = new Bundle();
		return item;
	}
	public Bundle() {}
	public Bundle(TYPE type) {
	}
	static public enum TYPE {
		LOOP_1, // 1개만 나오면 종료.
		LOOP_2, // 나오든 안나오든 목록 순회 후 종료.
		LOOP_3 // 나올때까지 순회 후 종료.
	};
	
	@SuppressWarnings("deprecation")
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (isLvCheck(cha)) {
			if (cha.getInventory() != null && cha.getInventory().getList().size() >= Lineage.inventory_max) {
				ChattingController.toChatting(cha, "인벤토리 공간이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			// 아이템 지급.
			List<ItemBundle> list = new ArrayList<ItemBundle>();
			ItemBundleDatabase.find(list, getItem().getName());

			if (list.size() < 1)
				return;

			for (ItemBundle ib : list) {
				if (ib.getItemCountMin() > 0) {
					Item i = ItemDatabase.find(ib.getItem());

					if (i != null) {
						ItemInstance temp = cha.getInventory().find(i.getName(), ib.getItemBless(), i.isPiles());
						int count = Util.random(ib.getItemCountMin(), ib.getItemCountMax());

						if (temp != null && (temp.getBress() != ib.getItemBless() || temp.getEnLevel() != ib.getItemEnchant()))
							temp = null;

						if (temp == null) {
							// 겹칠수 있는 아이템이 존재하지 않을경우.
							if (i.isPiles()) {
								temp = ItemDatabase.newInstance(i);
								temp.setObjectId(ServerDatabase.nextItemObjId());
								temp.setBress(ib.getItemBless());
								temp.setEnLevel(ib.getItemEnchant());
								temp.setCount(count);
								temp.setDefinite(true);
								if (ib.getAddTime() != null && ib.getAddTime().contains(":")) {
									Timestamp ts = new Timestamp(System.currentTimeMillis());
									String[] addTime = ib.getAddTime().split(":");

									int addHour = Integer.valueOf(addTime[0]);
									int addMin = Integer.valueOf(addTime[1]);
									int addSec = 0;
									int rate = 0;
									if (addTime.length > 2)
										addSec = Integer.valueOf(addTime[2]);
									if (ts.getSeconds() + addSec > 60) {
										rate = (ts.getSeconds() + addSec) / 60;
										ts.setMinutes(ts.getMinutes() + rate);
										addSec -= rate * 60;
									}
									if (ts.getMinutes() + addMin > 60) {
										rate = (ts.getMinutes() + addMin) / 60;
										ts.setHours(ts.getHours() + rate);
										addMin -= rate * 60;
									}
									if (ts.getHours() + addHour > 24) {
										rate = (ts.getHours() + addHour) / 24;
										ts.setDate(ts.getDate() + rate);
										addHour -= rate * 24;
									}
									int hour = ts.getHours() + addHour;
									int min = ts.getMinutes() + addMin;
									int sec = ts.getSeconds() + addSec;
									
									ts.setHours(hour);
									ts.setMinutes(min);
									ts.setSeconds(sec);
									
									ChattingController.toChatting(cha, "" +i.getName() +" " + ts.toString() + " 날자에 사라집니다.", Lineage.CHATTING_MODE_MESSAGE);
									
									temp.setTimestamp(ts.toString());
									temp.setTimeCheck(true);
								} else 
									temp.setTimeCheck(false);
								cha.getInventory().append(temp, true);
							} else {
								for (int idx = 0; idx < count; idx++) {
									temp = ItemDatabase.newInstance(i);
									temp.setObjectId(ServerDatabase.nextItemObjId());
									temp.setBress(ib.getItemBless());
									temp.setEnLevel(ib.getItemEnchant());
									temp.setDefinite(true);
									if (ib.getAddTime() != null && ib.getAddTime().contains(":")) {
										Timestamp ts = new Timestamp(System.currentTimeMillis());
										String[] addTime = ib.getAddTime().split(":");

										int addHour = Integer.valueOf(addTime[0]);
										int addMin = Integer.valueOf(addTime[1]);
										int addSec = 0;
										int rate = 0;
										if (addTime.length > 2)
											addSec = Integer.valueOf(addTime[2]);
										if (ts.getSeconds() + addSec > 60) {
											rate = (ts.getSeconds() + addSec) / 60;
											ts.setMinutes(ts.getMinutes() + rate);
											addSec -= rate * 60;
										}
										if (ts.getMinutes() + addMin > 60) {
											rate = (ts.getMinutes() + addMin) / 60;
											ts.setHours(ts.getHours() + rate);
											addMin -= rate * 60;
										}
										if (ts.getHours() + addHour > 24) {
											rate = (ts.getHours() + addHour) / 24;
											ts.setDate(ts.getDate() + rate);
											addHour -= rate * 24;
										}
										int hour = ts.getHours() + addHour;
										int min = ts.getMinutes() + addMin;
										int sec = ts.getSeconds() + addSec;
										
										ts.setHours(hour);
										ts.setMinutes(min);
										ts.setSeconds(sec);
										
										ChattingController.toChatting(cha,"" +i.getName() +" " + ts.toString() + " 날자에 사라집니다.", Lineage.CHATTING_MODE_MESSAGE);
										
										temp.setTimestamp(ts.toString());
										temp.setTimeCheck(true);
									} else 
										temp.setTimeCheck(false);
									cha.getInventory().append(temp, true);
								}
							}
						} else {
							// 겹치는 아이템이 존재할 경우.
							cha.getInventory().count(temp, temp.getCount() + count, true);
						}

						// 알림.
						ChattingController.toChatting(cha, String.format("%s(%d) 획득: %s", i.getName(), count, getItem().getName()), Lineage.CHATTING_MODE_MESSAGE);
					}
				}
			}

			// 수량 하향.
			cha.getInventory().count(this, getCount() - 1, true);
		}
	}

	protected void toBundle(Character cha, int[][] db, TYPE type) {
		int idx = 0;
		int cnt = 0;
		if(cha.getInventory().getList().size() >= Lineage.inventory_max) {
			ChattingController.toChatting(cha, "인벤토리 공간이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		do {
			int[] dbs = db[idx++];
			if (dbs[3]==0 || Util.random(0, Lineage.item_bundle_chance) < dbs[3]) {
				// 확률 체크
				Item item = ItemDatabase.find(dbs[0]);
				if(item == null)
					break;
				long count;
				try {
					count = Util.random(dbs[1], dbs[2]);
					CraftController.toCraft(this, cha, item, count, false, Integer.valueOf(dbs[4]), 0, Integer.valueOf(dbs[5]));
				} catch (Exception e) {
					count = Util.random(dbs[1], dbs[2]);
					CraftController.toCraft(cha, item, count, false, 0, Util.random(10, 15));
				}
				/*String format;
				if(count > 1) {
					format = String.format("%s (%d)를 얻었습니다.", item.getNameId(), count);
				} else {
					format = String.format("%s를 얻었습니다.", item.getNameId());
				}
				ChattingController.toChatting(cha, format, Lineage.CHATTING_MODE_MESSAGE);*/
				cha.toSender(new S_Message(403, String.format("%s (%d)", item.getName(), count)));
				for (PcInstance pc : World.getPcList()) {
					if(pc.isMent()) continue;
			 }
				cnt += 1;
				if (type == TYPE.LOOP_1)
					break;
			}
			if (idx >= db.length) {
				if (type == TYPE.LOOP_2)
					break;
				if (cnt > 0 && type == TYPE.LOOP_3)
					break;
				idx = 0;
			}
		} while (true);
	}

}
