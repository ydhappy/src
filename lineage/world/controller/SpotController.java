package lineage.world.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lineage.bean.database.FirstInventory;
import lineage.bean.database.Item;
import lineage.bean.lineage.Clan;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.share.Lineage_Balance;
import lineage.share.TimeLine;
import lineage.world.World;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.npc.SpotTower;

public final class SpotController {
	static private Calendar calendar;
	static public SpotTower spot;
	static public int spotClanId;	// 스팟 쟁탈전 승리 혈맹.
	static public long stopItemTime;
	static private int count;
	static private int mentCount;

	static public void init() {
		TimeLine.start("SpotController..");

		calendar = Calendar.getInstance();
		count = mentCount = 10;

		TimeLine.end();
	}

	@SuppressWarnings("deprecation")
	static public void toTimer(long time) {
		if (calendar == null)
			calendar = Calendar.getInstance();
		
		// 현재 시간.
		calendar.setTimeInMillis(time);
		Date date = calendar.getTime();
		int hour = date.getHours();
		int min = date.getMinutes();
		
		try {
			if (spot == null)
				setSpotTower();
		} catch (Exception e) {
			lineage.share.System.printf("스팟 쟁탈전 초기화 에러\r\n : %s\r\n", e.toString());
		}
		
		try {
			if (spot != null && !spot.isStart && hour == Lineage_Balance.spot_tower_start_hour && min == Lineage_Balance.spot_tower_start_min)
				spot.start(time);
		} catch (Exception e) {
			lineage.share.System.printf("스팟 쟁탈전 시작\r\n : %s\r\n", e.toString());
		}
		
		try {
			if (spot != null && spot.isStart && spot.getMonsterListSize() < 1 && !spot.isDead() && spot.endTime > 0 && spot.endTime <= time)
				spot.end(true);
		} catch (Exception e) {
			lineage.share.System.printf("스팟 쟁탈전 종료 에러\r\n : %s\r\n", e.toString());
		}
		
		try {
			if (spot != null && spot.isStart && --mentCount < 1) {
				mentCount = 60;
				World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "\\fY      ****** 현재 스팟 쟁탈전이 진행 중입니다. ******") );
			}
		} catch (Exception e) {
			lineage.share.System.printf("스팟 쟁탈전 멘트 에러\r\n : %s\r\n", e.toString());
		}
		
		try {
			spot.toTimer();
		} catch (Exception e) {
			lineage.share.System.printf("스팟 쟁탈전 타이머 에러\r\n : %s\r\n", e.toString());
		}
		
		if (spotClanId > 0 && stopItemTime < time) {
			stopItemTime = time + (1000 * Lineage_Balance.spot_item_delay);
			Clan c = ClanController.find(spotClanId);
			
			if (c != null) {
				List<PcInstance> list = World.getPcList();
				
				if (list != null) {
					for (PcInstance pc : list) {
						try {
							if (pc != null && !pc.isWorldDelete() && pc.getInventory() != null && pc.getClanId() == spotClanId) {
								for (FirstInventory fi : Lineage_Balance.spot_item) {
									Item i = ItemDatabase.find(fi.getName());
									
									if (i != null) {
										ItemInstance temp = pc.getInventory().find(i.getName(), 1, i.isPiles());

										if (temp != null && (temp.getBress() != 1 || temp.getEnLevel() != 0))
											temp = null;

										if (temp == null) {
											// 겹칠수 있는 아이템이 존재하지 않을경우.
											if (i.isPiles()) {
												temp = ItemDatabase.newInstance(i);
												temp.setObjectId(ServerDatabase.nextItemObjId());
												temp.setBress(1);
												temp.setEnLevel(0);
												temp.setCount(fi.getCount());
												temp.setDefinite(true);
												pc.getInventory().append(temp, true);
											} else {
												for (int idx = 0; idx < fi.getCount(); idx++) {
													temp = ItemDatabase.newInstance(i);
													temp.setObjectId(ServerDatabase.nextItemObjId());
													temp.setBress(1);
													temp.setEnLevel(0);
													temp.setDefinite(true);
													pc.getInventory().append(temp, true);
												}
											}
										} else {
											// 겹치는 아이템이 존재할 경우.
											pc.getInventory().count(temp, temp.getCount() + fi.getCount(), true);
										}
										
										ChattingController.toChatting(pc, String.format("스팟 쟁탈전 보상: %s(%d) 획득.", i.getName(), fi.getCount()), Lineage.CHATTING_MODE_MESSAGE);
									}
								}
							}
						} catch (Exception e) {
							lineage.share.System.println(String.format("[스팟 보상 지급 에러] 캐릭터:[%s]", pc.getName()));
							lineage.share.System.println(e);
						}
					}
				}
			}
		}
	}
	
	static public void setSpotTower() {
		spot = new SpotTower();
		spot.setObjectId(ServerDatabase.nextEtcObjId());
		//spot.setClassGfx(2671);
		//spot.setGfx(2671);
		spot.setClassGfx(1672);
		spot.setGfx(1672);
		spot.setGfxMode(32);
		spot.setName("스팟 타워");
		spot.setMaxHp(Lineage_Balance.spot_tower_hp);
		spot.setNowHp(Lineage_Balance.spot_tower_hp);
	}
}
