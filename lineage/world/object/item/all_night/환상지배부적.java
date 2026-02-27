package lineage.world.object.item.all_night;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.DungeonBook;
import lineage.bean.database.FirstSpawn;
import lineage.database.OmanBookTeleportDatabase;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.WantedController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 환상지배부적 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 환상지배부적();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		List<String> msg = new ArrayList<String>();

		for (DungeonBook db : OmanBookTeleportDatabase.getList()) {
			if (db.getAden() != null && ItemDatabase.find(db.getAden()) != null && db.getCount() > 0) {
				msg.add(String.format("%s [%s(%,d)]", db.getName(), db.getAden(), db.getCount()));
			} else {
				msg.add(String.format("%s", db.getName()));
			}
		}

		for (int i = 0; i < 100; i++) {
			msg.add(" ");
		}

		cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "omanCharm", null, msg));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if (pc == null || pc.isWorldDelete() || pc.isDead() || pc.isLock() || pc.getInventory() == null) {
			return;
		}
		
		if (Lineage.open_wait) {
			ChattingController.toChatting(pc, "오픈 대기엔 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		if (!checkMap(pc)) {
			ChattingController.toChatting(pc, "해당 맵에서 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		try {
			DungeonBook db = OmanBookTeleportDatabase.find(Integer.valueOf(action));
			
			if (db != null) {
				if (pc.getLevel() < db.getLevel()) {
					ChattingController.toChatting(pc, String.format("오만의 탑은 %d레벨 이상부터 사용 가능합니다.", db.getLevel()), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if (db.isClan()) {
					if (pc.getClanId() < 1) {
						ChattingController.toChatting(pc, "혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
				}
				
				if (db.isWanted()) {
					if (!WantedController.checkWantedPc(pc)) {
						ChattingController.toChatting(pc, "수배 상태에서만 사용이 가능 합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
				}
				
				if (db.getLoc_list() != null && db.getLoc_list().size() > 0) {
					FirstSpawn fs = db.getLoc_list().get(Util.random(0, db.getLoc_list().size() - 1));
					
					if (db.getAden() != null && db.getCount() > 0) {
						if (ItemDatabase.find(db.getAden()) != null) {
							if (pc.getInventory().isAden(db.getAden(), db.getCount(), true)) {
								pc.toPotal(fs.getX(), fs.getY(), fs.getMap());
							} else {
								ChattingController.toChatting(pc, String.format("%s(%,d) 이(가) 부족합니다.", db.getAden(), db.getCount()), Lineage.CHATTING_MODE_MESSAGE);
							}
						}
					} else {		
						pc.toPotal(fs.getX(), fs.getY(), fs.getMap());
						pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
					}	
				}		
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private boolean checkMap(PcInstance pc) {
		switch (pc.getMap()) {
		case 57:
		case 70:
		case 5124:
		case 5001:
		case 621:
		case 99:
		case 509:
		case 666:
		case 800:
		case 340:
		case 370:
		case 350:
		case 780:
		case 781:
		case 782:
		case 5:
			return false;
		}
		
		return true;
	}
}
