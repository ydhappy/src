package lineage.world.object.npc.craft;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_MessageGreen;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_SoundEffect;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;
import lineage.world.object.instance.CraftInstance;
import lineage.world.object.instance.PcInstance;

public class 마안합성사 extends CraftInstance {

	public 마안합성사(Npc npc) {
		super(npc);

		// hyper text 패킷 구성에 해당 구간을 npc 이름으로 사용함.
		temp_request_list.add(npc.getNameId());

		// 제작 처리 초기화.
		Item i = ItemDatabase.find("지룡의 마안");
		if (i != null) {
			craft_list.put("봉인된 지룡의 마안", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("봉인된 지룡의 마안"), 1));
			l.add(new Craft(ItemDatabase.find("아데나"), 100000));
			list.put(i, l);
		}
		i = ItemDatabase.find("화룡의 마안");
		if (i != null) {
			craft_list.put("봉인된 화룡의 마안", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("봉인된 화룡의 마안"), 1));
			l.add(new Craft(ItemDatabase.find("아데나"), 100000));
			list.put(i, l);
		}
		i = ItemDatabase.find("수룡의 마안");
		if (i != null) {
			craft_list.put("봉인된 수룡의 마안", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("봉인된 수룡의 마안"), 1));
			l.add(new Craft(ItemDatabase.find("아데나"), 100000));
			list.put(i, l);
		}
		i = ItemDatabase.find("풍룡의 마안");
		if (i != null) {
			craft_list.put("봉인된 풍룡의 마안", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("봉인된 풍룡의 마안"), 1));
			l.add(new Craft(ItemDatabase.find("아데나"), 100000));
			list.put(i, l);
		}
		i = ItemDatabase.find("탄생의 마안");
		if (i != null) {
			craft_list.put("탄생의 마안", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("지룡의 마안"), 1));
			l.add(new Craft(ItemDatabase.find("수룡의 마안"), 1));
			l.add(new Craft(ItemDatabase.find("아데나"), 200000));
			list.put(i, l);
		}
		i = ItemDatabase.find("형상의 마안");
		if (i != null) {
			craft_list.put("형상의 마안", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("탄생의 마안"), 1));
			l.add(new Craft(ItemDatabase.find("풍룡의 마안"), 1));
			l.add(new Craft(ItemDatabase.find("아데나"), 200000));
			list.put(i, l);
		}
		i = ItemDatabase.find("생명의 마안");
		if (i != null) {
			craft_list.put("생명의 마안", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("형상의 마안"), 1));
			l.add(new Craft(ItemDatabase.find("화룡의 마안"), 1));
			l.add(new Craft(ItemDatabase.find("아데나"), 200000));
			list.put(i, l);
		}
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "maanNpc"));
		pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 27825)); 
	}

	private Random rnd = new Random();

	@Override
	public void toTalk(PcInstance pc, String action, String type,
			ClientBasePacket cbp, Object... opt) {
		if (action.equalsIgnoreCase("봉인된 지룡의 마안")) {
			Item craft = craft_list.get(action);
			if (craft != null) {
				List<Craft> l = list.get(craft);
				// 재료 확인.
				if (CraftController.isCraft(pc, l, true)) {
					// 재료 제거
					CraftController.toCraft(pc, l);
					if (rnd.nextInt(100) < 50) {
						// 아이템 지급.
						ChattingController.toChatting(pc,
								"지룡의 마안 제작에 성공하였습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						CraftController.toCraft(this, pc, craft, 1, true, 0, 0,
								1);
						pc.toSender(new S_ObjectEffect(pc, 7671), true);
					} else {
						ChattingController.toChatting(pc,
								"지룡의 마안 제작에 실패하였습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(new S_ObjectEffect(pc, 6251), true);
						pc.toSender(new S_SoundEffect(4470), true);
					}
				}
			}
		} else if (action.equalsIgnoreCase("봉인된 화룡의 마안")) {
			Item craft = craft_list.get(action);
			if (craft != null) {
				List<Craft> l = list.get(craft);
				// 재료 확인.
				if (CraftController.isCraft(pc, l, true)) {
					// 재료 제거
					CraftController.toCraft(pc, l);
					if (rnd.nextInt(100) < 50) {
						// 아이템 지급.
						ChattingController.toChatting(pc,
								"화룡의 마안 제작에 성공하였습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						CraftController.toCraft(this, pc, craft, 1, true, 0, 0,
								1);
						pc.toSender(new S_ObjectEffect(pc, 7674), true);
					} else {
						ChattingController.toChatting(pc,
								"화룡의 마안 제작에 실패하였습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(new S_ObjectEffect(pc, 6251), true);
						pc.toSender(new S_SoundEffect(4470), true);
					}
				}
			}
		} else if (action.equalsIgnoreCase("봉인된 수룡의 마안")) {
			Item craft = craft_list.get(action);
			if (craft != null) {
				List<Craft> l = list.get(craft);
				// 재료 확인.
				if (CraftController.isCraft(pc, l, true)) {
					// 재료 제거
					CraftController.toCraft(pc, l);
					if (rnd.nextInt(100) < 50) {
						// 아이템 지급.
						ChattingController.toChatting(pc,
								"수룡의 마안 제작에 성공하였습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						CraftController.toCraft(this, pc, craft, 1, true, 0, 0,
								1);
						pc.toSender(new S_ObjectEffect(pc, 7672), true);
					} else {
						ChattingController.toChatting(pc,
								"수룡의 마안 제작에 실패하였습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(new S_ObjectEffect(pc, 6251), true);
						pc.toSender(new S_SoundEffect(4470), true);
					}
				}
			}
		} else if (action.equalsIgnoreCase("봉인된 풍룡의 마안")) {
			Item craft = craft_list.get(action);
			if (craft != null) {
				List<Craft> l = list.get(craft);
				// 재료 확인.
				if (CraftController.isCraft(pc, l, true)) {
					// 재료 제거
					CraftController.toCraft(pc, l);
					if (rnd.nextInt(100) < 50) {
						// 아이템 지급.
						ChattingController.toChatting(pc,
								"풍룡의 마안 제작에 성공하였습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						CraftController.toCraft(this, pc, craft, 1, true, 0, 0,
								1);
						pc.toSender(new S_ObjectEffect(pc, 7673), true);
					} else {
						ChattingController.toChatting(pc,
								"풍룡의 마안 제작에 실패하였습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(new S_ObjectEffect(pc, 6251), true);
						pc.toSender(new S_SoundEffect(4470), true);
					}
				}
			}
		} else if (action.equalsIgnoreCase("탄생의 마안")) {
			Item craft = craft_list.get(action);
			if (craft != null) {
				List<Craft> l = list.get(craft);
				// 재료 확인.
				if (CraftController.isCraft(pc, l, true)) {
					// 재료 제거
					CraftController.toCraft(pc, l);
					if (rnd.nextInt(100) < 20) {
						// 아이템 지급.
						ChattingController.toChatting(pc,
								"탄생의 마안 제작에 성공하였습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						CraftController.toCraft(this, pc, craft, 1, true, 0, 0,
								1);
						pc.toSender(new S_ObjectEffect(pc, 7675), true);
					} else {
						ChattingController.toChatting(pc,
								"탄생의 마안 제작에 실패하였습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(new S_ObjectEffect(pc, 6251), true);
						pc.toSender(new S_SoundEffect(4470), true);
					}
				}
			}
		} else if (action.equalsIgnoreCase("형상의 마안")) {
			Item craft = craft_list.get(action);
			if (craft != null) {
				List<Craft> l = list.get(craft);
				// 재료 확인.
				if (CraftController.isCraft(pc, l, true)) {
					// 재료 제거
					CraftController.toCraft(pc, l);
					if (rnd.nextInt(100) < 10) {
						// 아이템 지급.
						ChattingController.toChatting(pc, "형상의 마안 제작에 성공하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
						pc.toSender(new S_ObjectEffect(pc, 7676), true);
					} else {
						ChattingController.toChatting(pc, "형상의 마안 제작에 실패하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(new S_ObjectEffect(pc, 6251), true);
						pc.toSender(new S_SoundEffect(4470), true);
					}
				}
			}
		} else if (action.equalsIgnoreCase("생명의 마안")) {
			Item craft = craft_list.get(action);
			if (craft != null) {
				List<Craft> l = list.get(craft);
				// 재료 확인.
				if (CraftController.isCraft(pc, l, true)) {
					// 재료 제거
					CraftController.toCraft(pc, l);
					if (rnd.nextInt(100) < 7) {
						// 아이템 지급.
						ChattingController.toChatting(pc, "생명의 마안 제작에 성공하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
						pc.toSender(new S_ObjectEffect(pc, 7678), true);
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "어느 아덴 용사가 생명의 마안 제작에 성공 하였습니다."));
						// 화면 중앙에 메세지 알리기.
						//if (Lineage.is_blue_message)
						//	World.toSender(new S_MessageGreen(556, "\\fZ어느 아덴 용사가 '생명의 마안' 제작에 성공 하였습니다."));
					} else {
						ChattingController.toChatting(pc, "생명의 마안 제작에 실패하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(new S_ObjectEffect(pc, 6251), true);
						pc.toSender(new S_SoundEffect(4470), true);
					}
				}
			}
		}
	}
}