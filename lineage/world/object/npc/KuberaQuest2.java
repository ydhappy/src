package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lineage.database.AccountDatabase;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class KuberaQuest2 extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		showHtml(pc);
	}
	
	//몬스터 퀘스트
	public void showHtml(PcInstance pc) {
		List<String> kqrlist = new ArrayList<String>();
		kqrlist.clear();
		switch (pc.getRadomQuest()) {
		case 0:
			kqrlist.add(String.format("%d", pc.getRandomQuestCount()));
			kqrlist.add(String.format("%d", Lineage.dayquest));
			kqrlist.add(String.format("반복퀘스트 시작을 누르시면 퀘스트가 시작 됩니다"));
			break;
		case 1:
			kqrlist.add(String.format("%d", pc.getRandomQuestCount()));
			kqrlist.add(String.format("%d", Lineage.dayquest));
			// 퀘스트명
			kqrlist.add(String.format("%s", "본던5층 소탕"));
			// 퀘스트목표
			kqrlist.add(String.format("%s", "[본던5층] 몬스터 200마리 처치"));
			if (pc.getRandomQuestkill() > 0) {
				// 처치몬스터
				kqrlist.add(String.format("%d", pc.getRandomQuestkill()));
			} else {
				kqrlist.add(String.format("0"));
			}

			// 보상

			kqrlist.add(String.format("%s %d", Lineage.rq1 ,Lineage.rqc1));
			break;
		case 2:
			kqrlist.add(String.format("%d", pc.getRandomQuestCount()));
			kqrlist.add(String.format("%d", Lineage.dayquest));
			// 퀘스트명
			kqrlist.add(String.format("%s", "본던6층 소탕"));
			// 퀘스트목표
			kqrlist.add(String.format("%s", "[본던6층] 몬스터 200마리 처치"));
			// 처치몬스터
			if (pc.getRandomQuestkill() > 0) {
				// 처치몬스터
				kqrlist.add(String.format("%d", pc.getRandomQuestkill()));
			} else {
				kqrlist.add(String.format("0"));
			}
			// 보상
			kqrlist.add(String.format("%s %d", Lineage.rq2 ,Lineage.rqc2));
			break;
		case 3:
			kqrlist.add(String.format("%d", pc.getRandomQuestCount()));
			kqrlist.add(String.format("%d", Lineage.dayquest));
			// 퀘스트명
			kqrlist.add(String.format("%s", "본던7층 소탕"));
			// 퀘스트목표
			kqrlist.add(String.format("%s", "[본던7층] 몬스터 200마리 처치"));
			// 처치몬스터
			if (pc.getRandomQuestkill() > 0) {
				// 처치몬스터
				kqrlist.add(String.format("%d", pc.getRandomQuestkill()));
			} else {
				kqrlist.add(String.format("0"));
			}
			// 보상
			kqrlist.add(String.format("%s %d", Lineage.rq3 ,Lineage.rqc3));
			break;
		case 4:
			kqrlist.add(String.format("%d", pc.getRandomQuestCount()));
			kqrlist.add(String.format("%d", Lineage.dayquest));
			// 퀘스트명
			kqrlist.add(String.format("%s", "용던5층 소탕"));
			// 퀘스트목표
			kqrlist.add(String.format("%s", "[용던5층] 몬스터 200마리 처치"));
			// 처치몬스터
			if (pc.getRandomQuestkill() > 0) {
				// 처치몬스터
				kqrlist.add(String.format("%d", pc.getRandomQuestkill()));
			} else {
				kqrlist.add(String.format("0"));
			}
			// 보상
			kqrlist.add(String.format("%s %d", Lineage.rq4 ,Lineage.rqc4));
			break;
		case 5:
			kqrlist.add(String.format("%d", pc.getRandomQuestCount()));
			kqrlist.add(String.format("%d", Lineage.dayquest));
			// 퀘스트명
			kqrlist.add(String.format("%s", "용던6층 소탕"));
			// 퀘스트목표
			kqrlist.add(String.format("%s", "[용던6층] 몬스터 200마리 처치"));
			// 처치몬스터
			if (pc.getRandomQuestkill() > 0) {
				// 처치몬스터
				kqrlist.add(String.format("%d", pc.getRandomQuestkill()));
			} else {
				kqrlist.add(String.format("0"));
			}
			// 보상
			kqrlist.add(String.format("%s %d", Lineage.rq5 ,Lineage.rqc5));
			break;
		case 6:
			kqrlist.add(String.format("%d", pc.getRandomQuestCount()));
			kqrlist.add(String.format("%d", Lineage.dayquest));
			// 퀘스트명
			kqrlist.add(String.format("%s", "사던3층 소탕"));
			// 퀘스트목표
			kqrlist.add(String.format("%s", "[사던3층] 몬스터 200마리 처치"));
			// 처치몬스터
			if (pc.getRandomQuestkill() > 0) {
				// 처치몬스터
				kqrlist.add(String.format("%d", pc.getRandomQuestkill()));
			} else {
				kqrlist.add(String.format("0"));
			}
			// 보상
			kqrlist.add(String.format("%s %d", Lineage.rq6 ,Lineage.rqc6));
			break;
		case 7:
			kqrlist.add(String.format("%d", pc.getRandomQuestCount()));
			kqrlist.add(String.format("%d", Lineage.dayquest));
			// 퀘스트명
			kqrlist.add(String.format("%s", "사던4층 소탕"));
			// 퀘스트목표
			kqrlist.add(String.format("%s", "[사던4층] 몬스터 200마리 처치"));
			// 처치몬스터
			if (pc.getRandomQuestkill() > 0) {
				// 처치몬스터
				kqrlist.add(String.format("%d", pc.getRandomQuestkill()));
			} else {
				kqrlist.add(String.format("0"));
			}
			// 보상
			kqrlist.add(String.format("%s %d", Lineage.rq7,Lineage.rqc7));
			break;
		case 8:
			kqrlist.add(String.format("%d", pc.getRandomQuestCount()));
			kqrlist.add(String.format("%d", Lineage.dayquest));
			// 퀘스트명
			kqrlist.add(String.format("%s", "에바4층 소탕"));
			// 퀘스트목표
			kqrlist.add(String.format("%s", "[에바4층] 몬스터 200마리 처치"));
			// 처치몬스터
			if (pc.getRandomQuestkill() > 0) {
				// 처치몬스터
				kqrlist.add(String.format("%d", pc.getRandomQuestkill()));
			} else {
				kqrlist.add(String.format("0"));
			}
			// 보상
			kqrlist.add(String.format("%s %d", Lineage.rq8,Lineage.rqc8));
			break;
		default:
			break;
		}

		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "kquest2", null, kqrlist));

	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		int monstkill = 0;
		if (action.equalsIgnoreCase("kquest2-start")) {
			if(pc.getLevel() < 35) {
				ChattingController.toChatting(pc, String.format(" [반복 퀘스트] 35레벨 이상 퀘스트를 진행할 수 있습니다."), Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			Random random = new Random();
			int i = random.nextInt(8) + 1;
			
			if (pc.getRandomQuestCount() > Lineage.dayquest) {
				ChattingController.toChatting(pc, String.format(" 오늘 수행 가능한 퀘스트를 전부 완료 하셨습니다."), Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getRandomQuestPlay()==1){
				ChattingController.toChatting(pc, String.format(" [반복 퀘스트] 이미 진행중인 퀘스트가 있습니다."), Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
		
			pc.setRadomQuest(i );
			pc.setRandomQuestPlay(1);
			AccountDatabase.updaterquest(pc.getRadomQuest(), (int) pc.getObjectId());
			
		}
		if (action.equalsIgnoreCase("kquest2-go")) {
			//boolean js = ((pc.getX()>=33377)&&(pc.getX()<=33478))&&((pc.getY()>=32777)&&(pc.getY()<=32835))&&(pc.getMap()==4);
			boolean js = ((pc.getX()>=33338)&&(pc.getX()<=33532))&&((pc.getY()>=32644)&&(pc.getY()<=32879))&&(pc.getMap()==4);
			if(!js){
				ChattingController.toChatting(pc, String.format(" [반복 퀘스트] 기란 마을에서 이동이 가능합니다."), Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			Random random = new Random();
			int i = random.nextInt(2) + 1;
			
			if(pc.getRadomQuest() == 0) {
				ChattingController.toChatting(pc, String.format(" [반복 퀘스트] 수락된 퀘스트가 없어 이동이 불가능합니다."), Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			
			switch (pc.getRadomQuest()) {
			case 1:
			
				if (i == 1) {
					pc.toTeleport(32728, 32747, 11, true);
				} else if (i == 2) {
					pc.toTeleport(32793, 32778, 11, true);
				}
				break;
			case 2:
		
				if (i == 1) {
					pc.toTeleport(32750, 32754, 12, true);
				} else if (i == 2) {
					pc.toTeleport(32774, 32768, 12, true);
				} 
				break;
			case 3:
				
				if (i == 1) {
					pc.toTeleport(32737, 32764, 13, true);
				} else if (i == 2) {
					pc.toTeleport(32786, 32762, 13, true);
				}
				break;
			case 4:
				
				if (i == 1) {
					pc.toTeleport(32710, 32806, 35, true);
				} else if (i == 2) {
					pc.toTeleport(32690, 32856, 35, true);
				}
				break;
			case 5:
				
				if (i == 1) {
					pc.toTeleport(32701, 32823, 36, true);
				} else if (i == 2) {
					pc.toTeleport(32708, 32855, 36, true);
				}
				break;
			case 6:
				
				if (i == 1) {
					pc.toTeleport(32766, 32775, 27, true);
				} else if (i == 2) {
					pc.toTeleport(32744, 32796, 27, true);
				}
				break;
			case 7:
				
				if (i == 1) {
					pc.toTeleport(32743, 32772, 28, true);
				} else if (i == 2) {
					pc.toTeleport(32777, 32757, 28, true);
				}
				break;
			case 8:
				
				if (i == 1) {
					pc.toTeleport(32832, 32737, 63, true);
				} else if (i == 2) {
					pc.toTeleport(32749, 32800, 63, true);
				}
				break;
			}
			
		}
		switch (pc.getRadomQuest()) {
		case 1:
			monstkill = 200;
			break;
		case 2:
			monstkill = 200;
			break;
		case 3:
			monstkill = 200;
			break;
		case 4:
			monstkill = 200;
			break;
		case 5:
			monstkill = 200;
			break;
		case 6:
			monstkill = 200;
			break;
		case 7:
			monstkill = 200;
			break;
		case 8:
			monstkill = 200;
			break;
		default:
			break;
		}
		if (action.equalsIgnoreCase("kquest2-finish")) {
			if (pc.getRandomQuestCount() < Lineage.dayquest) {
				if(pc.getRadomQuest() == 0) {
					ChattingController.toChatting(pc, String.format(" [반복 퀘스트] 완료 가능한 퀘스트가 없습니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}

				if (pc.getRandomQuestkill() >= monstkill) {
					
					switch (pc.getRadomQuest()) {
					case 1:
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find(Lineage.rq1));
						ii.setCount(Lineage.rqc1);
						pc.toGiveItem(null, ii, ii.getCount());
						break;
					case 2:
						ItemInstance ii2 = ItemDatabase.newInstance(ItemDatabase.find(Lineage.rq2));
						ii2.setCount(Lineage.rqc2);
						pc.toGiveItem(null, ii2, ii2.getCount());
						break;
					case 3:
						ItemInstance ii3 = ItemDatabase.newInstance(ItemDatabase.find(Lineage.rq3));
						ii3.setCount(Lineage.rqc3);
						pc.toGiveItem(null, ii3, ii3.getCount());
						break;
					case 4:
						ItemInstance ii4 = ItemDatabase.newInstance(ItemDatabase.find(Lineage.rq4));
						ii4.setCount(Lineage.rqc4);
						pc.toGiveItem(null, ii4, ii4.getCount());
						break;
					case 5:
						ItemInstance ii5 = ItemDatabase.newInstance(ItemDatabase.find(Lineage.rq5));
						ii5.setCount(Lineage.rqc5);
						pc.toGiveItem(null, ii5, ii5.getCount());
						break;
					case 6:
						ItemInstance ii6 = ItemDatabase.newInstance(ItemDatabase.find(Lineage.rq6));
						ii6.setCount(Lineage.rqc6);
						pc.toGiveItem(null, ii6, ii6.getCount());
						break;
					case 7:
						ItemInstance ii7 = ItemDatabase.newInstance(ItemDatabase.find(Lineage.rq7));
						ii7.setCount(Lineage.rqc7);
						pc.toGiveItem(null, ii7, ii7.getCount());
						break;
					case 8:
						ItemInstance ii8 = ItemDatabase.newInstance(ItemDatabase.find(Lineage.rq8));
						ii8.setCount(Lineage.rqc8 );
						pc.toGiveItem(null, ii8, ii8.getCount());
						break;
					default:
						break;
					}
					//완료후 처리 
					pc.setRandomQuestkill(0);
					pc.setRandomQuestCount(pc.getRandomQuestCount()+1);
					pc.setRadomQuest(0);
					pc.setRandomQuestPlay(0);
					AccountDatabase.updateuQuestKill((int) pc.getObjectId());			
					AccountDatabase.updatequestcount(pc.getRandomQuestCount(), (int) pc.getObjectId());
					ChattingController.toChatting(pc, String.format("\\fU [반복 퀘스트] 완료."), Lineage.CHATTING_MODE_MESSAGE);
					ChattingController.toChatting(pc, String.format("\\fU [반복 퀘스트] 보상이 지급되었습니다."), Lineage.CHATTING_MODE_MESSAGE);
				} else {
					ChattingController.toChatting(pc, String.format(" [반복 퀘스트] 남은 몬스터 %d 마리 ", monstkill - pc.getRandomQuestkill()), Lineage.CHATTING_MODE_MESSAGE);
				}


			}else{
				ChattingController.toChatting(pc, String.format(" 오늘 수행 가능한 퀘스트를 전부 완료 하셨습니다."), Lineage.CHATTING_MODE_MESSAGE);
			}
		}
		showHtml(pc);
	}
}