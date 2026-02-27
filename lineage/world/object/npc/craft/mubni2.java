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
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_SoundEffect;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;
import lineage.world.object.instance.CraftInstance;
import lineage.world.object.instance.PcInstance;
	
	public class mubni2 extends CraftInstance {

	public mubni2(Npc npc) {
	super(npc);

		// hyper text 패킷 구성에 해당 구간을 npc 이름으로 사용함.
		temp_request_list.add(npc.getNameId());
		
		// 제작 처리 초기화. 여기서부터 부적
		Item i = ItemDatabase.find("오만의 탑 1층 지배 부적");
		if (i != null) {
			craft_list.put("A", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("오만의 탑 1층 이동 부적"), 1));
			l.add(new Craft(ItemDatabase.find("오만의 탑 1층 이동 주문서"), 100));
			list.put(i, l);
		}
		i = ItemDatabase.find("오만의 탑 2층 지배 부적");
		if (i != null) {
			craft_list.put("B", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("오만의 탑 2층 이동 부적"), 1));
			l.add(new Craft(ItemDatabase.find("오만의 탑 2층 이동 주문서"), 100));
			list.put(i, l);
		}
		i = ItemDatabase.find("오만의 탑 3층 지배 부적");
		if (i != null) {
			craft_list.put("C", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("오만의 탑 3층 이동 부적"), 1));
			l.add(new Craft(ItemDatabase.find("오만의 탑 3층 이동 주문서"), 100));
			list.put(i, l);
		}
		i = ItemDatabase.find("오만의 탑 4층 지배 부적");
		if (i != null) {
			craft_list.put("D", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("오만의 탑 4층 이동 부적"), 1));
			l.add(new Craft(ItemDatabase.find("오만의 탑 4층 이동 주문서"), 100));
			list.put(i, l);
		}
		i = ItemDatabase.find("오만의 탑 5층 지배 부적");
		if (i != null) {
			craft_list.put("E", i);
			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("오만의 탑 5층 이동 부적"), 1));
			l.add(new Craft(ItemDatabase.find("오만의 탑 5층 이동 주문서"), 100));
			list.put(i, l);
		}
}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "mubni4"));
		pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 27825)); 
	}
	
	private Random rnd = new Random();
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		if (action.equalsIgnoreCase("A")) {
			Item craft = craft_list.get(action);
			if (craft != null) {
				List<Craft> l = list.get(craft);
				// 재료 확인.
				if (CraftController.isCraft(pc, l, true)) {
					// 재료 제거
					CraftController.toCraft(pc, l);
					if(rnd.nextInt(100) < 10) {
					// 아이템 지급.
					ChattingController.toChatting(pc, "오만의 탑 1층 지배 부적 제작에 성공하였습니다.",Lineage.CHATTING_MODE_MESSAGE);
					CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
					pc.toSender(new S_ObjectEffect(pc, 14495), true);
					World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "어느 아덴 용사가 '오만의 탑 1층 지배 부적' 제작에 성공 하였습니다."));
			} else {
					//CraftController.toCraft(this, pc,ItemDatabase.find("순간이동 조종 반지"), 1, true, 0, 0, 1);
					ChattingController.toChatting(pc, "오만의 탑 1층 지배 부적 제작에 실패하였습니다.",Lineage.CHATTING_MODE_MESSAGE);
					pc.toSender(new S_ObjectEffect(pc, 6251), true);
					pc.toSender(new S_SoundEffect( 4470), true);
			        }
				}
			}
		} else if (action.equalsIgnoreCase("B")) {
			Item craft = craft_list.get(action);
			if (craft != null) {
				List<Craft> l = list.get(craft);
				// 재료 확인.
				if (CraftController.isCraft(pc, l, true)) {
					// 재료 제거
					CraftController.toCraft(pc, l);
					if(rnd.nextInt(100) < 10) {
					// 아이템 지급.
					ChattingController.toChatting(pc, "오만의 탑 2층 지배 부적 제작에 성공하였습니다.",Lineage.CHATTING_MODE_MESSAGE);
					CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
					pc.toSender(new S_ObjectEffect(pc, 14495), true);
					World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "어느 아덴 용사가 '오만의 탑 2층 지배 부적' 제작에 성공 하였습니다."));
			} else {
					//CraftController.toCraft(this, pc,ItemDatabase.find("변신 조종 반지"), 1, true, 0, 0, 1);
					ChattingController.toChatting(pc, "오만의 탑 2층 지배 부적 제작에 실패하였습니다.",Lineage.CHATTING_MODE_MESSAGE);
					pc.toSender(new S_ObjectEffect(pc, 6251), true);
					pc.toSender(new S_SoundEffect( 4470), true);
			        }
					//
//					// 안내창 띄우기.
//		            pc.toSender(new S_Html( this, ""));
				}
			}
		} else if (action.equalsIgnoreCase("C")) {
			Item craft = craft_list.get(action);
			if (craft != null) {
				List<Craft> l = list.get(craft);
				// 재료 확인.
				if (CraftController.isCraft(pc, l, true)) {
					// 재료 제거
					CraftController.toCraft(pc, l);
					if(rnd.nextInt(100) < 10) {
					// 아이템 지급.
					ChattingController.toChatting(pc, "오만의 탑 3층 지배 부적 제작에 성공하였습니다.",Lineage.CHATTING_MODE_MESSAGE);
					CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
					pc.toSender(new S_ObjectEffect(pc, 14495), true);
					World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "어느 아덴 용사가 '오만의 탑 3층 지배 부적' 제작에 성공 하였습니다."));
			} else {
					//CraftController.toCraft(this, pc,ItemDatabase.find("변신 조종 반지"), 1, true, 0, 0, 1);
					ChattingController.toChatting(pc, "오만의 탑 3층 지배 부적 제작에 실패하였습니다.",Lineage.CHATTING_MODE_MESSAGE);
					pc.toSender(new S_ObjectEffect(pc, 6251), true);
					pc.toSender(new S_SoundEffect( 4470), true);
			        }
					//
//					// 안내창 띄우기.
//		            pc.toSender(new S_Html( this, ""));
				}
			}
		} else if (action.equalsIgnoreCase("D")) {
			Item craft = craft_list.get(action);
			if (craft != null) {
				List<Craft> l = list.get(craft);
				// 재료 확인.
				if (CraftController.isCraft(pc, l, true)) {
					// 재료 제거
					CraftController.toCraft(pc, l);
					if(rnd.nextInt(100) < 10) {
					// 아이템 지급.
					ChattingController.toChatting(pc, "오만의 탑 4층 지배 부적 제작에 성공하였습니다.",Lineage.CHATTING_MODE_MESSAGE);
					CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
					pc.toSender(new S_ObjectEffect(pc, 14495), true);
					World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "어느 아덴 용사가 '오만의 탑 4층 지배 부적' 제작에 성공 하였습니다."));
			} else {
					//CraftController.toCraft(this, pc,ItemDatabase.find("변신 조종 반지"), 1, true, 0, 0, 1);
					ChattingController.toChatting(pc, "오만의 탑 4층 지배 부적 제작에 실패하였습니다.",Lineage.CHATTING_MODE_MESSAGE);
					pc.toSender(new S_ObjectEffect(pc, 6251), true);
					pc.toSender(new S_SoundEffect( 4470), true);
			        }
					//
//					// 안내창 띄우기.
//		            pc.toSender(new S_Html( this, ""));
				}
			}
		} else if (action.equalsIgnoreCase("E")) {
			Item craft = craft_list.get(action);
			if (craft != null) {
				List<Craft> l = list.get(craft);
				// 재료 확인.
				if (CraftController.isCraft(pc, l, true)) {
					// 재료 제거
					CraftController.toCraft(pc, l);
					if(rnd.nextInt(100) < 10) {
					// 아이템 지급.
					ChattingController.toChatting(pc, "오만의 탑 5층 지배 부적 제작에 성공하였습니다.",Lineage.CHATTING_MODE_MESSAGE);
					CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
					pc.toSender(new S_ObjectEffect(pc, 14495), true);
					World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "어느 아덴 용사가 '오만의 탑 5층 지배 부적' 제작에 성공 하였습니다."));
			} else {
					//CraftController.toCraft(this, pc,ItemDatabase.find("변신 조종 반지"), 1, true, 0, 0, 1);
					ChattingController.toChatting(pc, "오만의 탑 5층 지배 부적 제작에 실패하였습니다.",Lineage.CHATTING_MODE_MESSAGE);
					pc.toSender(new S_ObjectEffect(pc, 6251), true);
					pc.toSender(new S_SoundEffect( 4470), true);
			        }
					//
//					// 안내창 띄우기.
//		            pc.toSender(new S_Html( this, ""));
				}
			}
		}
	}
}
