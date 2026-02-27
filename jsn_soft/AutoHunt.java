package jsn_soft;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_HtmlAutoHunt;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class AutoHunt extends object {

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {

		jsn_hunt jh = AutoHuntDatabase.find(cha.getObjectId());
		if (jh == null)
			AutoHuntDatabase.insertHunt((PcInstance) cha);
		cha.toSender(S_HtmlAutoHunt.clone(BasePacketPooling.getPool(S_HtmlAutoHunt.class), (PcInstance) cha, this, "autohunt"));
	}

	@Override
	public void toClick(Character cha, String action, String type,
			ClientBasePacket cbp) {
		// System.out.println("check Scroll: " + action);
		jsn_hunt jh = AutoHuntDatabase.find(cha.getObjectId());
		PcInstance pc = (PcInstance) cha;
		if (action.equalsIgnoreCase("ahstart")) {
			if (pc.getLevel() < 30) { // 30렙 이상
				ChattingController.toChatting(pc, "자동사냥은 30레벨 이상 사용이 가능합니다.", 20);
				return;
			}
			if (pc.getAutoTime() <= 0) {
				ChattingController.toChatting(pc, "자동사냥 남은 시간이 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if (jh.getX() == 0 && jh.getY() == 0) {
				ChattingController.toChatting(pc, "사냥터를 설정해 주세요.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if (jh.getPotion() != null && (jh.getPotion().equalsIgnoreCase("") || jh.getPotion().equalsIgnoreCase(" "))) {
				ChattingController.toChatting(pc, "물약을 설정해 주세요.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if (Lineage.autohunt_onoff) {
				jh = AutoHuntDatabase.find(cha.getObjectId());
				if (pc != null
						&& (pc.getMap() == 36 || pc.getMap() == 37
								|| pc.getMap() == 53 || pc.getMap() == 54
								|| pc.getMap() == 55 || pc.getMap() == 56
								|| pc.getMap() == 57 || pc.getMap() == 63
								|| pc.getMap() == 65 || pc.getMap() == 67
								|| pc.getMap() == 68 || pc.getMap() == 69
								|| pc.getMap() == 70 || pc.getMap() == 72
								|| pc.getMap() == 73 || pc.getMap() == 74
								|| pc.getMap() == 99 || pc.getMap() == 5  
								|| pc.getMap() == 101 || pc.getMap() == 102
								|| pc.getMap() == 103 || pc.getMap() == 104
								|| pc.getMap() == 105 || pc.getMap() == 340
								|| pc.getMap() == 350 || pc.getMap() == 370
								|| pc.getMap() == 621 || pc.getMap() == 666
								|| pc.getMap() == 780 || pc.getMap() == 781
								|| pc.getMap() == 782 || pc.getMap() == 800
								|| pc.getMap() == 5124 || pc.getMap() == 86
								|| pc.getMap() == 509
								|| pc.getMap() == 5000 || pc.getMap() == 5001)) {
					ChattingController.toChatting(pc, "현재 맵에서는 자동사냥을 실행할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				AutoHuntController.startHunt(pc);
				pc.setAutocommand(true);
				
			} else {
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "19시~23시까지 자동사냥을 하실수 없습니다."));
			}

		} else if (action.equalsIgnoreCase("ahstop")) {
			AutoHuntController.stopHunt(pc);
		} else if (action.equalsIgnoreCase("ahpickm")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			List<String> info = new ArrayList<String>();
			info.clear();
			String mapname = AutoHuntController.getMapName(jh.getX(), jh.getY(), jh.getMap());

			info.add(mapname);

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt2", null, info));
			return;
		} else if (action.equalsIgnoreCase("ahissell")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(pc.isSellItem() ? "ON" : "OFF");

			if (pc.sell_List.size() > 0) {
				for (Item i : pc.sell_List) {
					info.add(i.getName());
				}
			}

			for (int i = 0; i < (20 - pc.sell_List.size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class),
					this, "autohunt3", null, info));
			return;
		} else if (action.equalsIgnoreCase("ahpotion")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(String.valueOf(jh.getPotionPercent()));
			info.add(jh.getPotion() != "" ? jh.getPotion() : "없음");

			if (ItemDatabase.getPotion().size() > 0) {
				for (Item it : ItemDatabase.getPotion()) {
					info.add(it.getName());
				}
			}

			for (int i = 0; i < (11 - ItemDatabase.getPotion().size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class),
					this, "autohunt4", null, info));
			return;
		} else if (action.equalsIgnoreCase("pickhere")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			if (pc.getMap() == 36 || pc.getMap() == 37
					|| pc.getMap() == 53 || pc.getMap() == 54
					|| pc.getMap() == 55 || pc.getMap() == 56
					|| pc.getMap() == 57 || pc.getMap() == 63
					|| pc.getMap() == 65 || pc.getMap() == 67
					|| pc.getMap() == 68 || pc.getMap() == 69
					|| pc.getMap() == 70 || pc.getMap() == 72
					|| pc.getMap() == 73 || pc.getMap() == 74
					|| pc.getMap() == 99 || pc.getMap() == 5  
					|| pc.getMap() == 101 || pc.getMap() == 102
					|| pc.getMap() == 103 || pc.getMap() == 104
					|| pc.getMap() == 105 || pc.getMap() == 340
					|| pc.getMap() == 350 || pc.getMap() == 370
					|| pc.getMap() == 621 || pc.getMap() == 666
					|| pc.getMap() == 780 || pc.getMap() == 781
					|| pc.getMap() == 782 || pc.getMap() == 800
					|| pc.getMap() == 5124 || pc.getMap() == 86
					|| pc.getMap() == 509
					|| pc.getMap() == 5000 || pc.getMap() == 5001) {
				ChattingController.toChatting(pc, "현재 맵에서는 위치 저장을 하실 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if (pc.getMap() == 4
					&& World.isSafetyZone(pc.getX(), pc.getY(), pc.getMap())) {
				ChattingController.toChatting(pc, "마을은 지정하실 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			} else {
				AutoHuntDatabase.saveLocation(pc, pc.getX(), pc.getY(), pc.getMap());
				ChattingController.toChatting(pc, "현재 위치 저장 성공.", Lineage.CHATTING_MODE_MESSAGE);
			}
			List<String> info = new ArrayList<String>();
			info.clear();
			String mapname = AutoHuntController.getMapName(jh.getX(), jh.getY(), jh.getMap());
			info.add(mapname);
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt2", null, info));
			return;
		} else if (action.equalsIgnoreCase("ddragon")
				|| action.equalsIgnoreCase("dgludio")
				|| action.equalsIgnoreCase("delftree")
				|| action.equalsIgnoreCase("ddesert")
				|| action.equalsIgnoreCase("dwater")) {
			ChattingController.toChatting(pc, "이동하실 던전의 층수를 입력해주세요.", Lineage.CHATTING_MODE_MESSAGE);
			AutoHuntController.setAction(action);
			pc.setAutocommand(true);
			return;
		} else if (action.equalsIgnoreCase("sellon")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			pc.setSellItem(true);
			jh.setAutoSell(true);
			ChattingController.toChatting(pc, "자동 판매가 [ON] 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(pc.isSellItem() ? "ON" : "OFF");

			if (pc.sell_List.size() > 0) {
				for (Item i : pc.sell_List) {
					info.add(i.getName());
				}
			}

			for (int i = 0; i < (30 - pc.sell_List.size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class),
					this, "autohunt3", null, info));
			return;
		} else if (action.equalsIgnoreCase("selloff")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			pc.setSellItem(false);
			jh.setAutoSell(false);
			ChattingController.toChatting(pc, "자동 판매가 [OFF] 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(pc.isSellItem() ? "ON" : "OFF");

			if (pc.sell_List.size() > 0) {
				for (Item i : pc.sell_List) {
					info.add(i.getName());
				}
			}

			for (int i = 0; i < (30 - pc.sell_List.size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class),
					this, "autohunt3", null, info));
			return;
		} else if (action.equalsIgnoreCase("addsell")) {
			if (pc.sell_List.size() > 50) {
				ChattingController.toChatting(pc, "아이템은 최대 50개까지 등록가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			ChattingController.toChatting(pc, "추가할 아이템을 입력해주세요.", Lineage.CHATTING_MODE_MESSAGE);
			AutoHuntController.setAction(action);
			pc.setAutocommand(true);
			return;
		} else if (action.equalsIgnoreCase("delsell")) {
			ChattingController.toChatting(pc, "삭제할 아이템을 입력해주세요.", Lineage.CHATTING_MODE_MESSAGE);
			AutoHuntController.setAction(action);
			pc.setAutocommand(true);
			return;
			//
		} else if (action.equalsIgnoreCase("a90")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			jh.setPotionPercent(90);
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(String.valueOf(jh.getPotionPercent()));
			info.add(jh.getPotion() != "" ? jh.getPotion() : "없음");

			if (ItemDatabase.getPotion().size() > 0) {
				for (Item it : ItemDatabase.getPotion()) {
					info.add(it.getName());
				}
			}

			for (int i = 0; i < (11 - ItemDatabase.getPotion().size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt4", null, info));
			return;
		} else if (action.equalsIgnoreCase("a80")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			jh.setPotionPercent(80);
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(String.valueOf(jh.getPotionPercent()));
			info.add(jh.getPotion() != "" ? jh.getPotion() : "없음");

			if (ItemDatabase.getPotion().size() > 0) {
				for (Item it : ItemDatabase.getPotion()) {
					info.add(it.getName());
				}
			}

			for (int i = 0; i < (11 - ItemDatabase.getPotion().size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt4", null, info));
			return;
		} else if (action.equalsIgnoreCase("a70")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			jh.setPotionPercent(70);
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(String.valueOf(jh.getPotionPercent()));
			info.add(jh.getPotion() != "" ? jh.getPotion() : "없음");

			if (ItemDatabase.getPotion().size() > 0) {
				for (Item it : ItemDatabase.getPotion()) {
					info.add(it.getName());
				}
			}

			for (int i = 0; i < (11 - ItemDatabase.getPotion().size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt4", null, info));
			return;
		} else if (action.equalsIgnoreCase("a60")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			jh.setPotionPercent(60);
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(String.valueOf(jh.getPotionPercent()));
			info.add(jh.getPotion() != "" ? jh.getPotion() : "없음");

			if (ItemDatabase.getPotion().size() > 0) {
				for (Item it : ItemDatabase.getPotion()) {
					info.add(it.getName());
				}
			}

			for (int i = 0; i < (11 - ItemDatabase.getPotion().size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt4", null, info));
			return;
		} else if (action.equalsIgnoreCase("a50")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			jh.setPotionPercent(50);
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(String.valueOf(jh.getPotionPercent()));
			info.add(jh.getPotion() != "" ? jh.getPotion() : "없음");

			if (ItemDatabase.getPotion().size() > 0) {
				for (Item it : ItemDatabase.getPotion()) {
					info.add(it.getName());
				}
			}

			for (int i = 0; i < (11 - ItemDatabase.getPotion().size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt4", null, info));
			return;
		} else if (action.equalsIgnoreCase("ap1")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			jh.setPotion(ItemDatabase.getPotion().get(0).getName());
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(String.valueOf(jh.getPotionPercent()));
			info.add(jh.getPotion() != "" ? jh.getPotion() : "없음");

			if (ItemDatabase.getPotion().size() > 0) {
				for (Item it : ItemDatabase.getPotion()) {
					info.add(it.getName());
				}
			}

			for (int i = 0; i < (11 - ItemDatabase.getPotion().size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt4", null, info));
			return;
		} else if (action.equalsIgnoreCase("ap2")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			jh.setPotion(ItemDatabase.getPotion().get(1).getName());
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(String.valueOf(jh.getPotionPercent()));
			info.add(jh.getPotion() != "" ? jh.getPotion() : "없음");

			if (ItemDatabase.getPotion().size() > 0) {
				for (Item it : ItemDatabase.getPotion()) {
					info.add(it.getName());
				}
			}

			for (int i = 0; i < (11 - ItemDatabase.getPotion().size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt4", null, info));
			return;
		} else if (action.equalsIgnoreCase("ap3")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			jh.setPotion(ItemDatabase.getPotion().get(2).getName());
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(String.valueOf(jh.getPotionPercent()));
			info.add(jh.getPotion() != "" ? jh.getPotion() : "없음");

			if (ItemDatabase.getPotion().size() > 0) {
				for (Item it : ItemDatabase.getPotion()) {
					info.add(it.getName());
				}
			}

			for (int i = 0; i < (11 - ItemDatabase.getPotion().size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt4", null, info));
			return;
		} else if (action.equalsIgnoreCase("ap4")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			jh.setPotion(ItemDatabase.getPotion().get(3).getName());
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(String.valueOf(jh.getPotionPercent()));
			info.add(jh.getPotion() != "" ? jh.getPotion() : "없음");

			if (ItemDatabase.getPotion().size() > 0) {
				for (Item it : ItemDatabase.getPotion()) {
					info.add(it.getName());
				}
			}

			for (int i = 0; i < (11 - ItemDatabase.getPotion().size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt4", null, info));
			return;
		} else if (action.equalsIgnoreCase("ap5")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			jh.setPotion(ItemDatabase.getPotion().get(4).getName());
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(String.valueOf(jh.getPotionPercent()));
			info.add(jh.getPotion() != "" ? jh.getPotion() : "없음");

			if (ItemDatabase.getPotion().size() > 0) {
				for (Item it : ItemDatabase.getPotion()) {
					info.add(it.getName());
				}
			}

			for (int i = 0; i < (11 - ItemDatabase.getPotion().size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt4", null, info));
			return;
		} else if (action.equalsIgnoreCase("ap6")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			jh.setPotion(ItemDatabase.getPotion().get(5).getName());
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(String.valueOf(jh.getPotionPercent()));
			info.add(jh.getPotion() != "" ? jh.getPotion() : "없음");

			if (ItemDatabase.getPotion().size() > 0) {
				for (Item it : ItemDatabase.getPotion()) {
					info.add(it.getName());
				}
			}

			for (int i = 0; i < (11 - ItemDatabase.getPotion().size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt4", null, info));
			return;
		} else if (action.equalsIgnoreCase("ap7")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			jh.setPotion(ItemDatabase.getPotion().get(6).getName());
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(String.valueOf(jh.getPotionPercent()));
			info.add(jh.getPotion() != "" ? jh.getPotion() : "없음");

			if (ItemDatabase.getPotion().size() > 0) {
				for (Item it : ItemDatabase.getPotion()) {
					info.add(it.getName());
				}
			}

			for (int i = 0; i < (11 - ItemDatabase.getPotion().size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt4", null, info));
			return;
		} else if (action.equalsIgnoreCase("ap8")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			jh.setPotion(ItemDatabase.getPotion().get(7).getName());
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(String.valueOf(jh.getPotionPercent()));
			info.add(jh.getPotion() != "" ? jh.getPotion() : "없음");

			if (ItemDatabase.getPotion().size() > 0) {
				for (Item it : ItemDatabase.getPotion()) {
					info.add(it.getName());
				}
			}

			for (int i = 0; i < (11 - ItemDatabase.getPotion().size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt4", null, info));
			return;
		} else if (action.equalsIgnoreCase("ap9")) {
			jh = AutoHuntDatabase.find(cha.getObjectId());
			jh.setPotion(ItemDatabase.getPotion().get(8).getName());
			List<String> info = new ArrayList<String>();
			info.clear();

			info.add(String.valueOf(jh.getPotionPercent()));
			info.add(jh.getPotion() != "" ? jh.getPotion() : "없음");

			if (ItemDatabase.getPotion().size() > 0) {
				for (Item it : ItemDatabase.getPotion()) {
					info.add(it.getName());
				}
			}

			for (int i = 0; i < (11 - ItemDatabase.getPotion().size()); i++) {
				info.add(" ");
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt4", null, info));
			return;
		} else {

		}

		cha.toSender(S_HtmlAutoHunt.clone(
				BasePacketPooling.getPool(S_HtmlAutoHunt.class), (PcInstance) cha, this, "autohunt"));
	}

}