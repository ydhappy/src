package lineage.world.object.npc;

import java.util.ArrayList;


import java.util.List;

import lineage.bean.database.Book;
import lineage.bean.database.DungeonBook;
import lineage.bean.database.FirstSpawn;
import lineage.bean.database.TownBook;
import lineage.database.BookTeleportDatabase;
import lineage.database.DungeonBookTeleportDatabase;
import lineage.database.ItemDatabase;
import lineage.database.TownBookTeleportDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.WantedController;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class 마을책 extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		List<String> msg = new ArrayList<String>();

		for (TownBook db : TownBookTeleportDatabase.getList()) {
			if (db.getAden() != null && ItemDatabase.find(db.getAden()) != null && db.getCount() > 0) {
				msg.add(String.format("%s", db.getName()));
			} else {
				msg.add(String.format("%s", db.getName()));
			}
		}

		for (int i = 0; i < 100; i++) {
			msg.add(" ");
		}
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "dunbook2", null, msg));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if (pc == null || pc.isWorldDelete() || pc.isDead() || pc.isLock() || pc.getInventory() == null) {
			return;
		}
		
		if (Lineage.open_wait) {
			ChattingController.toChatting(pc, "오픈 대기중에는 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		if (!checkMap(pc)) {
			ChattingController.toChatting(pc, "해당 맵에서 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		try {
			TownBook db = TownBookTeleportDatabase.find(Integer.valueOf(action));
			
			if (db != null) {
				if (pc.getLevel() < db.getLevel()) {
					ChattingController.toChatting(pc, String.format("해당 맵은 %d레벨 이상 입장하실 수 있습니다.", db.getLevel()), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if (db.isClan()) {
					if (pc.getClanId() < 1) {
						ChattingController.toChatting(pc, "해당 맵은 혈맹 가입중에만 입장하실 수 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
				}
				
				if (db.isWanted()) {
					if (!WantedController.checkWantedPc(pc)) {
						ChattingController.toChatting(pc, "해당 맵은 수배자만 입장하실 수 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
				}
				
				if (db.getLoc_list() != null && db.getLoc_list().size() > 0) {
					FirstSpawn fs = db.getLoc_list().get(Util.random(0, db.getLoc_list().size() - 1));
					
					if (db.getAden() != null && db.getCount() > 0) {
						if (ItemDatabase.find(db.getAden()) != null) {
							if (pc.getInventory().isAden(db.getAden(), db.getCount(), true)) {
								pc.toPotal(fs.getX(), fs.getY(), fs.getMap());
								pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
							} else {
								//ChattingController.toChatting(pc, String.format("%s(%,d) 이(가) 부족합니다.", db.getAden(), db.getCount()), Lineage.CHATTING_MODE_MESSAGE);
								ChattingController.toChatting(pc, String.format("%s 충분치 않습니다.", Util.getStringWord(db.getAden(), "이", "가")), Lineage.CHATTING_MODE_MESSAGE);
							}
						}
					} else {		
						pc.toPotal(fs.getX(), fs.getY(), fs.getMap());
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
		case 99:
		case 5124:
		case 5001:
		case 621:
		case 303:
		case 666:
			return false;
		}
		
		return true;
	}
}
