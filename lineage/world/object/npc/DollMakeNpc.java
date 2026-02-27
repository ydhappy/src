package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_BlueMessage;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_MessageGreen;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_SoundEffect;
import lineage.share.Lineage;
import lineage.share.Lineage_Balance;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.MagicDoll;

public class DollMakeNpc extends object {
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "dollmake"));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if (!pc.isWorldDelete() && !pc.isDead() && !pc.isLock() && pc.getInventory() != null) {
			int rnd = Util.random(0, 100);
			if (action.equalsIgnoreCase("마법인형주머니")) {

				// 재료 확인
				ItemInstance item1 = pc.getInventory().find("신비한 날개깃털");
				ItemInstance item2 = pc.getInventory().find("아데나");
				
				if (item1 != null && item2 != null && item1.getCount() >= 20 && item2.getCount() >= 50000) {
					// 확률
					int success_probability = 7;

					// 확률 계산
					if (rnd < success_probability) {
						// 제작 지급
						CraftController.toCraft(this, pc, ItemDatabase.find("마법인형 주머니"), 1, true);
					} else
						ChattingController.toChatting(pc, String.format("%s 제작에 실패하였습니다.", action), 20);
					
					// 재료 제거
					pc.getInventory().count(item1, item1.getCount() - 20, true);
					pc.getInventory().count(item2, item2.getCount() - 50000, true);
				} else {
					ChattingController.toChatting(pc, "[알림] 재료가 부족합니다.", 20);
				}
			}
			// 인형 합성시 필요한 재료 갯수
			int count = action.equalsIgnoreCase("특수 합성") ? 3 : 4;
			int specialCount = 1;
			String itemName = null;
			List<MagicDoll> list = new ArrayList<MagicDoll>();
			List<MagicDoll> magicDollList = new ArrayList<MagicDoll>();
			List<MagicDoll> magicDollSpecialList = new ArrayList<MagicDoll>();
			
			for (ItemInstance item : pc.getInventory().getList()) {
				if (item instanceof MagicDoll && !item.isEquipped())
					list.add((MagicDoll) item);
			}
			
			if (action.equalsIgnoreCase("1단계 합성")) {
				int level = 0;
				
				for (MagicDoll magicdoll : list) {
					for (int i = 0; i < Lineage.magicDoll[level].length; i++) {
						if (magicdoll.getItem().getName().equalsIgnoreCase(Lineage.magicDoll[level][i]))
							magicDollList.add(magicdoll);
					}
				}
				
				if (magicDollList.size() >= count) {
					double probability = Math.random();
					
					if (probability < Lineage_Balance.magicDoll_class_1_perfect_probability) {
						itemName = Lineage.magicDoll[level + 2][Util.random(0, Lineage.magicDoll[level + 2].length - 1)];
						ChattingController.toChatting(pc, "마법인형 합성에 대성공 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(new S_ObjectEffect( pc, 7680), true);
					} else if (probability < Lineage_Balance.magicDoll_class_1_probability) {
						itemName = Lineage.magicDoll[level + 1][Util.random(0, Lineage.magicDoll[level + 1].length - 1)];
						ChattingController.toChatting(pc, "마법인형 합성에 성공 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(new S_ObjectEffect( pc, 7680), true);
					} else {
						itemName = Lineage.magicDoll[level][Util.random(0, Lineage.magicDoll[level].length - 1)];
						ChattingController.toChatting(pc, "마법인형 합성에 실패 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
					}
				} else {
					ChattingController.toChatting(pc, String.format("1단계 마법인형 %d개 부족합니다.", count - magicDollList.size()), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}		
			} else if (action.equalsIgnoreCase("2단계 합성")) {
				int level = 1;
				
				for (MagicDoll magicdoll : list) {
					for (int i = 0; i < Lineage.magicDoll[level].length; i++) {
						if (magicdoll.getItem().getName().equalsIgnoreCase(Lineage.magicDoll[level][i]))
							magicDollList.add(magicdoll);
					}
				}
				
				if (magicDollList.size() >= count) {
					double probability = Math.random();
					
					if (probability < Lineage_Balance.magicDoll_class_2_perfect_probability) {
						itemName = Lineage.magicDoll[level + 2][Util.random(0, Lineage.magicDoll[level + 2].length - 1)];
						ChattingController.toChatting(pc, "마법인형 합성에 대성공 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(new S_ObjectEffect( pc, 7680), true);
					} else if (probability < Lineage_Balance.magicDoll_class_2_probability) {
						itemName = Lineage.magicDoll[level + 1][Util.random(0, Lineage.magicDoll[level + 1].length - 1)];
						ChattingController.toChatting(pc, "마법인형 합성에 성공 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(new S_ObjectEffect( pc, 7680), true);
					} else {
						itemName = Lineage.magicDoll[level][Util.random(0, Lineage.magicDoll[level].length - 1)];
						ChattingController.toChatting(pc, "마법인형 합성에 실패 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
					}
				} else {
					ChattingController.toChatting(pc, String.format("2단계 마법인형 %d개 부족합니다.", count - magicDollList.size()), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}	
			} else if (action.equalsIgnoreCase("3단계 합성")) {
				int level = 2;
				
				for (MagicDoll magicdoll : list) {
					for (int i = 0; i < Lineage.magicDoll[level].length; i++) {
						if (magicdoll.getItem().getName().equalsIgnoreCase(Lineage.magicDoll[level][i]))
							magicDollList.add(magicdoll);
					}
				}
				
				if (magicDollList.size() >= count) {
					double probability = Math.random();
					
					if (probability < Lineage_Balance.magicDoll_class_3_perfect_probability) {
						itemName = Lineage.magicDoll[level + 2][Util.random(0, Lineage.magicDoll[level + 2].length - 1)];
						ChattingController.toChatting(pc, "마법인형 합성에 대성공 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(new S_ObjectEffect( pc, 7680), true);
					} else if (probability < Lineage_Balance.magicDoll_class_3_probability) {
						itemName = Lineage.magicDoll[level + 1][Util.random(0, Lineage.magicDoll[level + 1].length - 1)];
						ChattingController.toChatting(pc, "마법인형 합성에 성공 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(new S_ObjectEffect( pc, 7680), true);
					} else {
						itemName = Lineage.magicDoll[level][Util.random(0, Lineage.magicDoll[level].length - 1)];
						ChattingController.toChatting(pc, "마법인형 합성에 실패 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
					}
				} else {
					ChattingController.toChatting(pc, String.format("3단계 마법인형 %d개 부족합니다.", count - magicDollList.size()), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
			} else if (action.equalsIgnoreCase("4단계 합성")) {
				int level = 3;
				
				for (MagicDoll magicdoll : list) {
					for (int i = 0; i < Lineage.magicDoll[level].length; i++) {
						if (magicdoll.getItem().getName().equalsIgnoreCase(Lineage.magicDoll[level][i]))
							magicDollList.add(magicdoll);
					}
				}
				
				if (magicDollList.size() >= count) {
					double probability = Math.random();
					
					if (probability < Lineage_Balance.magicDoll_class_4_probability) {
						itemName = Lineage.magicDoll[level + 1][Util.random(0, Lineage.magicDoll[level + 1].length - 1)];
						ChattingController.toChatting(pc, "마법인형 합성에 성공 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "어느 아덴 용사가 " + itemName + " 합성에 성공 하였습니다."));
						pc.toSender(new S_ObjectEffect( pc, 7680), true);
						// 화면 중앙에 메세지 알리기.
						//if (Lineage.is_blue_message)
						//	World.toSender(new S_MessageGreen( 556, String.format("어느 아덴 용사가 '%s' 합성에 성공 하였습니다.", itemName)));
					} else {
						itemName = Lineage.magicDoll[level][Util.random(0, Lineage.magicDoll[level].length - 1)];
						ChattingController.toChatting(pc, "마법인형 합성에 실패 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
					}
				} else {
					ChattingController.toChatting(pc, String.format("4단계 마법인형 %d개 부족합니다.", count - magicDollList.size()), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
			} else if (action.equalsIgnoreCase("유일 합성")) {
				int level = 5;
				
				for (MagicDoll magicdoll : list) {
					for (int i = 0; i < Lineage.magicDoll[level].length; i++) {
						if (magicdoll.getItem().getName().equalsIgnoreCase(Lineage.magicDoll[level][i]))
							magicDollList.add(magicdoll);
					}
				}
				
				if (magicDollList.size() >= count) {
					double probability = Math.random();
					
					if (probability < 100) {
						itemName = Lineage.magicDoll[level + 1][Util.random(0, Lineage.magicDoll[level + 1].length - 1)];
						ChattingController.toChatting(pc, "마법인형 합성에 성공 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "어느 아덴 용사가 " + itemName + " 합성에 성공 하였습니다."));
						pc.toSender(new S_ObjectEffect( pc, 7680), true);
						// 화면 중앙에 메세지 알리기.
						if (Lineage.is_blue_message)
							World.toSender(new S_MessageGreen( 556, String.format("\\fZ어느 아덴 용사가 '%s' 합성에 성공 하였습니다.", itemName)));
					} else {
						itemName = Lineage.magicDoll[level][Util.random(0, Lineage.magicDoll[level].length - 1)];
						ChattingController.toChatting(pc, "마법인형 합성에 실패 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
					}
				} else {
					ChattingController.toChatting(pc, String.format("4대용 마법인형 %d개 부족합니다.", count - magicDollList.size()), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
			} else if (action.equalsIgnoreCase("특수 합성")) {
				int level = 3;
				
				for (MagicDoll magicdoll : list) {
					for (int i = 0; i < Lineage.magicDoll[level].length; i++) {
						if (magicdoll.getItem().getName().equalsIgnoreCase(Lineage.magicDoll[level][i]))
							magicDollList.add(magicdoll);
					}
				}
				
				for (MagicDoll specialMagicdoll : list) {
					for (int i = 0; i < Lineage.magicDoll[level + 1].length; i++) {
						if (specialMagicdoll.getItem().getName().equalsIgnoreCase(Lineage.magicDoll[level + 1][i]))
							magicDollSpecialList.add(specialMagicdoll);
					}
				}
				
				if (magicDollList.size() >= count && magicDollSpecialList.size() >= specialCount) {
					double probability = Math.random();
					// 일정 확률로 4대용 인형 성공.
					// 실패시 5단계 인형 지급.
					if (probability < Lineage_Balance.magicDoll_class_5_probability) {
						itemName = Lineage.magicDoll[level + 2][Util.random(0, Lineage.magicDoll[level + 2].length - 1)];
						ChattingController.toChatting(pc, "마법인형 합성에 성공 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(new S_ObjectEffect( pc, 6588), true);
						World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "어느 아덴 용사가 " + itemName + " 합성에 성공 하였습니다."));
						// 화면 중앙에 메세지 알리기.
						//if (Lineage.is_blue_message)
						//	World.toSender(new S_MessageGreen( 556, String.format("\\fZ어느 아덴 용사가 %s 합성에 성공 하였습니다.", itemName)));
					} else {
						itemName = Lineage.magicDoll[level + 1][Util.random(0, Lineage.magicDoll[level + 1].length - 1)];
						ChattingController.toChatting(pc, "마법인형 합성에 실패 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(new S_ObjectEffect(pc, 6251), true);
						pc.toSender(new S_SoundEffect( 4470), true);
					}
				} else {
					if (magicDollList.size() < count && magicDollSpecialList.size() >= specialCount)
						ChattingController.toChatting(pc, String.format("4단계 마법인형 %d개 부족합니다.", count - magicDollList.size()), Lineage.CHATTING_MODE_MESSAGE);
					else if (magicDollList.size() >= count && magicDollSpecialList.size() < specialCount)
						ChattingController.toChatting(pc, String.format("5단계 마법인형이 %d개 부족합니다.", specialCount - magicDollSpecialList.size()), Lineage.CHATTING_MODE_MESSAGE);
					else
						ChattingController.toChatting(pc, String.format("5단계 마법인형 %d개, 4단계 마법인형 %d개 부족합니다.", specialCount - magicDollSpecialList.size(), count - magicDollList.size()), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
			}
			
			if (itemName != null) {
				Item item = ItemDatabase.find(itemName);
				
				if (item != null) {
					ItemInstance temp = ItemDatabase.newInstance(item);
					temp.setObjectId(ServerDatabase.nextItemObjId());
					temp.setBress(1);
					temp.setEnLevel(0);
					temp.setDefinite(true);
					pc.getInventory().append(temp, true);
					
					if (action.equalsIgnoreCase("특수 합성")) {
						for (int i = 0; i < count; i++)
							pc.getInventory().count(magicDollList.get(i), magicDollList.get(i).getCount() - 1, true);
						
						for (int i = 0; i < specialCount; i++)
							pc.getInventory().count(magicDollSpecialList.get(i), magicDollSpecialList.get(i).getCount() - 1, true);
						
					} else {
						for (int i = 0; i < count; i++)
							pc.getInventory().count(magicDollList.get(i), magicDollList.get(i).getCount() - 1, true);
					}
					
					// 월드에 메세지 출력		
					//ItemDropMessageDatabase.sendMessageMagicDoll(pc, itemName);
	
					ChattingController.toChatting(pc, String.format("[%s] 획득! ", temp.toStringDB()), Lineage.CHATTING_MODE_MESSAGE);
				}
			}	
		}
	}
}
