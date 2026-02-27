package lineage.world.controller;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Npc;
import lineage.database.NpcDatabase;
import lineage.database.PolyDatabase;
import lineage.database.ServerDatabase;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectAdd;
import lineage.network.packet.server.S_ObjectRemove;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.CurseBlind;
import lineage.world.object.magic.CurseParalyze;
import lineage.world.object.magic.Haste;
import lineage.world.object.magic.ShapeChange;
import lineage.world.object.magic.Slow;
import lineage.world.object.npc.background.door.Door;
import lineage.world.object.npc.event.FireOfSoul;
import lineage.world.object.npc.event.GhostHouseDoorButton;
import lineage.world.object.npc.event.GhostHouseFrontDoor;
import lineage.world.object.npc.event.GhostHouseNextZone;

public class GhostHouseController {

	static private String[][] MENT_LIST = {			// 멘트 용
			{	"다투 : 자 준비해 10초 남았어!", 
				"다투 : 달려! 경기 시작이라구!", 
				"다투 : 영혼의 불꽃을 먼저 파괴해야 승자가 되는거야! 잊지마!", 
				"다투 : 영혼의 불이 파괴되었군, 멋진 녀석이 있나보군!",
			"다투 : 형편없어! 시간이 다  지나버렸잖아!"},
			{	"쿠산 : 10초후 문이 열린다네.", 
				"쿠산 : 자! 게임을 시작 해볼까?", 
				"쿠산 : 영혼의 불꽃을 먼저 찾아야 해!", 
				"쿠산 : 생각보다 승자가 빨리 나왔는걸.",
			"쿠산 : 시간이 초과했다네, 그럼 다음 기회를 기다리게."},
			{	"듀오 : 10초 후 문이 열릴게야.", 
				"듀오 : 자! 게임을 시작하겠네.", 
				"듀오 : 영혼의 불꽃을 먼저 파괴해야 승자라는 것을 잊지말게!", 
				"듀오 : 오 승자가 나왔으니 게임을 종료하겠네.",
			"듀오 : 이런! 시간이 지나버렸군. 다음 기회를 기다리게."}
	};
	static private int [][] TELEPORT_LOCATION = {	// next 텔레포트 용
			{32784, 32874},
			{32776, 32856},
			{32784, 32831},
			{32773, 32816},
			{32834, 32787},
			{32832, 32807},
			{32825, 32828},
			{32822, 32849},
			{32829, 32875}
	};
	static private int [][] BUTTON_LOCATION = {
		{32761, 32868},
		{32764, 32849},
		{32766, 32828},
		{32761, 32808},
		{32764, 32789},
		{32787, 32872},
		{32783, 32854},
		{32788, 32831},
		{32776, 32816},
		{32776, 32797},
		{32786, 32788},
		{32815, 32791},
		{32815, 32819},
		{32815, 32839},
		{32809, 32859},
		{32807, 32874},
		{32791, 32803},
		{32789, 32817},
		{32798, 32811},
		{32799, 32868},
		{32801, 32850},
		{32833, 32875},
		{32828, 32846},
		{32831, 32828},
		{32838, 32807},
		{32839, 32788},
		{32862, 32787},
		{32846, 32833},
		{32856, 32829},
		{32859, 32853},
		{32850, 32873}
	};
	static private int [][] DOOR_LOCATION = {
		{32763, 32867},
		{32766, 32848},
		{32768, 32827},
		{32763, 32807},
		{32766, 32788},
		{32789, 32871},
		{32785, 32853},
		{32790, 32830},
		{32778, 32815},
		{32778, 32796},
		{32788, 32787},
		{32817, 32790},
		{32817, 32818},
		{32817, 32838},
		{32811, 32858},
		{32809, 32873},
		{32793, 32802},
		{32791, 32816},
		{32800, 32810},
		{32801, 32867},
		{32803, 32849},
		{32835, 32874},
		{32830, 32845},
		{32833, 32827},
		{32840, 32806},
		{32841, 32787},
		{32864, 32786},
		{32848, 32832},
		{32858, 32828},
		{32861, 32852},
		{32852, 32872}
	};
	static private int [][] NEXTZONE_LOCATION = {
		{32769, 32867},
		{32771, 32846},
		{32773, 32828},
		{32768, 32806},
		{32770, 32787},
		
		{32822, 32793},
		{32822, 32818},
		{32821, 32839},
		{32815, 32859},
		{32813, 32874}
	};
	
	static private EventList STATUS;				// 현재 이벤트 상태
	static private enum EventList {					// 이벤트 상태 종류
		CLEANING,
		READY,
		GO
	}
	static private Integer POLY;					// 변신 시킬 gfx 아이디
	static private Integer ADEN;					// 게임참여 비용
	static private Integer MAP;						// 맵 구분용
	static private List<PcInstance> list;			// 참여한 유저 리스트
	static private List<Door> door_list;			// 문 리스트 - 제어용으로 사용
	static private Integer idx;						// toTimer에서 사용됨. 슬립할때 용도로 사용.
	static private Integer idx2;					// toTimer에서 사용됨.
	static private FireOfSoul flame;				// 영혼의불꽃 객체
	static private GhostHouseFrontDoor front_door;	// 출발선 문
	
	static public void init() {
		TimeLine.start("GhostHouseController..");
		
		if(Lineage.server_version >= 250) {
			idx = idx2 = 0;
			STATUS = EventList.CLEANING;
			POLY = 6284;
			ADEN = 1000;
			MAP = 5140;
			list = new ArrayList<PcInstance>();
			door_list = new ArrayList<Door>();
			//
			flame = new FireOfSoul();
			flame.setName("영혼의 불꽃");
			flame.setObjectId(ServerDatabase.nextNpcObjId());
			flame.setGfx(6331);
			flame.toTeleport(32871, 32830, MAP, false);
			//
			front_door = new GhostHouseFrontDoor();
			front_door.setObjectId(ServerDatabase.nextNpcObjId());
			front_door.toTeleport(32727, 32829, MAP, false);
			//
			for(int[] loc : DOOR_LOCATION) {
				Door d = new Door();
				d.setObjectId(ServerDatabase.nextNpcObjId());
				d.setGfx(6379);
				d.setHeading(6);
				d.setHomeLoc(3);
				d.toClose();
				d.toTeleport(loc[0], loc[1], MAP, false);
				door_list.add(d);
			}
			//
			for(int[] loc : BUTTON_LOCATION) {
				GhostHouseDoorButton d = new GhostHouseDoorButton();
				d.setObjectId(ServerDatabase.nextNpcObjId());
				d.setGfx(6335);
				d.setGfxMode(29);
				d.toTeleport(loc[0], loc[1], MAP, false);
			}
			//
			for(int[] loc : NEXTZONE_LOCATION) {
				GhostHouseNextZone nz = new GhostHouseNextZone();
				nz.setObjectId(ServerDatabase.nextNpcObjId());
				nz.setGfx(6339);
				nz.toTeleport(loc[0], loc[1], MAP, false);
			}
		}
		
		TimeLine.end();
	}
	
	static public void close() {
		if(Lineage.server_version < 250)
			return;
		idx = idx2 = 0;
		STATUS = EventList.CLEANING;
		toPlayerOut();
	}
	
	/**
	 * 
	 * @param pc
	 */
	static public void toWorldOut(PcInstance pc) {
		if(Lineage.server_version < 250)
			return;
		// 관리목록 제거.
		if(removeList(pc)) {
			//
			if(PluginController.init(GhostHouseController.class, "toWorldOut", pc) != null)
				return;
			// 모든 버프 제거.
			BuffController.removeAll(pc);
		}
	}

	/*
	 * 골인지점에 도착하여 이벤트가 종료
	 */
	static public void toEventEnd() {
		// 메세지 전송
		toMessage(MENT_LIST[2][3]);
		// 이벤트중인 타임을 종료 임박 으로 세팅.
		idx2 = 294;
	}
	
	/**
	 * 다음존으로 텔레포트 처리.
	 * @param pc
	 */
	static public void toNextTeleport(PcInstance pc) {
		int rnd = Util.random(0, TELEPORT_LOCATION.length-1);
		pc.toPotal(TELEPORT_LOCATION[rnd][0], TELEPORT_LOCATION[rnd][1], MAP);
	}
	
	/**
	 * 이벤트 액션 에 따른 처리
	 * @param pc
	 * @param isInfo	: 정보확인일지 아닐지. 아닐경우 입장처리임.
	 */
	static public void toEventAction(PcInstance pc, boolean isInfo) {
		//
		if(Lineage.event_ghosthouse == false) {
			ChattingController.toChatting(pc, "지금은 유령의집을 이용할 수 없습니다.", 20);
			return;
		}
		//
		switch(STATUS){
			case CLEANING:
				// 지금은 유령의 집을 청소 중이라네.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1183));
				break;
			case READY:
				if(isInfo)
					toEventMessage(pc);
				else
					toEventJoin(pc);
				break;
			case GO:
				if(isInfo)
					toEventMessage(pc);
				else
					// 이미 게임이 진행 되었네
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1182));
				break;
		}
	}
	
	/**
	 * 주기적으로 호출됨.
	 * @param time
	 */
	static public void toTimer(long time) {
		//
		if(Lineage.server_version < 250)
			return;
		if(Lineage.event_ghosthouse==false) {
			if(STATUS != EventList.CLEANING) {
				idx = idx2 = 0;
				STATUS = EventList.CLEANING;
				toCleaning();
			}
			return;
		}
		//
		switch(STATUS) {
			case CLEANING:
				// 1분정도 쉬고, 이벤트 상태 변경.
				if(idx++ == 0) {
					toCleaning();
				} else {
					if(idx >= 60) {
						idx = 0;
						idx2 = 60;
						STATUS = EventList.READY;
					}
				}
				break;
			case READY:
				if(idx++ < 59) {
					// 1분 대기.
					idx2--;
				} else {
					if(list.size() > 1) {
						toReady();
						idx = idx2 = 0;
						STATUS = EventList.GO;
					} else {
						toMessage("이용자수가 부족하여 게임 대기시간을 연장 합니다.");
						idx = idx2 = 0;
					}
				}
				break;
			case GO:
				if(idx++ < 9) {
					// 10초 대기.
				} else {
					if(idx == 10) {
						toGo();
					} else {
						if(++idx2 < 294) {
							// 고스트 발동 체크
							toGhost();
							// 가상트랩 발동 체크
							toTrap();
						} else {
							if(idx2==294)
								toMessage(MENT_LIST[2][4]);
							if(idx2>294 && idx2%2 != 0)
								toMessage( String.format("종료 %d초전..", 300-idx2) );
							if(idx2 == 300) {
								idx = idx2 = 0;
								STATUS = EventList.CLEANING;
							}
						}
					}
				}
				break;
		}
	}
	
	/**
	 * 청소중 상태 세팅.
	 */
	static private void toCleaning() {
		// 버프해제
		toBuffClean();
		// 유저내보내기
		toPlayerOut();
		// list 초기화
		clearList();
		// door_list 초기화
		for(Door door : door_list)
			door.toClose();
		toCloseFrontDoor();
		// 불꽃 모드 복원
		if(flame != null)
			flame.setGfxMode(0);
		// 드랍된 아이템 제거.
		World.clearWorldItem(MAP);
	}
	
	/**
	 * 겜 시작전 대기 상태 세팅
	 */
	static private void toReady() {
		// 버프해제
		toBuffClean();
		// 유저 호박으로 변신
		toChangAmber();
		// 멘트 쏘기
		toMessage(MENT_LIST[2][0]);
	}
	
	static private void toGo() {
		// 멘트 쏘기
		toMessage(MENT_LIST[2][1]);
		toMessage(MENT_LIST[2][2]);
		// 문열기
		toOpenFrontDoor();
	}
	
	/**
	 * 입장한 유저들에 버프를 초기화함.
	 */
	static private void toBuffClean() {
		//
		if(PluginController.init(GhostHouseController.class, "toBuffClean", list) != null)
			return;
		//
		synchronized (list) {
			for(PcInstance pc : list)
				BuffController.removeAll(pc);
		}
	}
	
	/**
	 * 입장한 유저들 마을로 돌려보내기.
	 */
	static private void toPlayerOut() {
		synchronized (list) {
			for(PcInstance pc : list) {
				// 변신 해제
				BuffController.remove(pc, ShapeChange.class);
				// 글루딘으로 내보내기.
				LocationController.toGludio(pc);
				pc.toPotal(pc.getHomeX(), pc.getHomeY(), pc.getHomeMap());
			}
		}
	}
	
	static private boolean removeList(PcInstance pc) {
		synchronized (list) {
			return list.remove(pc);
		}
	}
	
	static private boolean addList(PcInstance pc) {
		synchronized (list) {
			return list.add(pc);
		}
	}
	
	static private boolean containsList(PcInstance pc) {
		synchronized (list) {
			return list.contains(pc);
		}
	}
	
	/**
	 * 관리목록 초기화.
	 */
	static private void clearList() {
		synchronized (list) {
			list.clear();
		}
	}
	
	/**
	 * 이벤트에 참여한 유저 호박으로 변신시키기
	 */
	static private void toChangAmber() {
		synchronized (list) {
			for(PcInstance pc : list)
				ShapeChange.init(pc, pc, PolyDatabase.getPolyGfx(POLY), -1, 1);
		}
	}
	
	static private void toMessage(String msg) {
		synchronized (list) {
			for(PcInstance pc : list)
				ChattingController.toChatting(pc, msg, 20);
		}
	}
	
	static private void toOpenFrontDoor() {
		front_door.toOpen();
		front_door.toSend();
	}
	
	static private void toCloseFrontDoor() {
		front_door.toClose();
		front_door.toSend();
	}
	
	/**
	 * 고스트 발동 여부 체크
	 * 1. 30% 확율로 고스트 표현
	 * 발동 종류는 2가지
	 * 1-1. 1마리의 고스트 표현 80%
	 * 1-2. 1~3마리의 고스트 표현 20%
	 */
	static private void toGhost() {
		synchronized (list) {
			int[] loc = new int[2];
			for(PcInstance use : list){
				int rnd = Util.random(0, 100);
				loc[0] = use.getX();
				loc[1] = use.getY();
				if(rnd<20){
					// 1마리 표현
					if(rnd > 10)
						toVisualGhost(use, loc, 1);
					// 1~3마리 표현
					else
						toVisualGhost(use, loc, Util.random(1, 3));
				}
			}
		}
	}
	
	/**
	 * 가상의 트랩 발동 여부 체크
	 * 1. 30% 확율로 버프 발동
	 * 2. 10% 확율로 랜덤 텔레포트 발동
	 */
	static private void toTrap() {
		synchronized (list) {
			for(PcInstance pc : list) {
				int rnd = Util.random(0, 100);
				// 랜덤 버프
				if(rnd>5 && rnd<20) {
					// 트랩 표현
					toVisualTrap(pc);
					// 버프
					toRandomBuff(pc, false);
				} else if(rnd <= 5) {
					// 랜덤 좌표 세팅
					rnd = Util.random(0, TELEPORT_LOCATION.length-1);
					pc.toPotal(TELEPORT_LOCATION[rnd][0], TELEPORT_LOCATION[rnd][1], MAP);
				}
			}
		}
	}

	/**
	 * 화면에 고스트를 그리기
	 * 1. 고스트 생성
	 * 2. 랜덤으로 8방향중 한개 선택 그에따른 방향 잡아줌.
	 * 3. 액션 모드 랜덤선택
	 * 4. 랜덤 버프 선택
	 */
	static private void toVisualGhost(PcInstance use, int[] loc, int count) {
		for( ; count>0 ; --count) {
			// 고스트 위치 세팅 리턴값은 바라볼 최종 방향
			int heading = toGhostLocation(Util.random(0, 7), loc);
			// 고스트 생성
			BackgroundInstance ghost = toGhostCreate(loc[0], loc[1], heading);
			ghost.toTeleport(ghost.getX(), ghost.getY(), MAP, false);
			// 2로나눈 나머지몫이 0과같으면 1액션 취함
			if(heading%2 == 0)
				ghost.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), ghost, 1), false);
			// 나머지몫이 1과같다면 30액션 취함
			else
				ghost.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), ghost, 30), false);
			// 고스트 삭제
			World.remove(ghost);
			ghost.clearList(true);
			ghost = null;
			// 랜덤 버프
			toRandomBuff(use, true);
		}
	}

	/**
	 * 고스트 스폰할 좌표 세팅
	 * rnd - 방향
	 * loc - 스폰될 위치
	 * 리턴값 - 고스트가 바라보는 방향
	 */
	static private int toGhostLocation(int rnd, int[] loc) {
		switch(rnd){
			case 0:
				loc[0] -= 1;
				rnd = 4;
				break;
			case 1:
				loc[0] += 1;
				loc[1] -= 1;
				rnd = 5;
				break;
			case 2:
				loc[0] += 1;
				rnd = 6;
				break;
			case 3:
				loc[0] += 1;
				loc[1] += 1;
				rnd = 7;
				break;
			case 4:
				loc[1] += 1;
				rnd = 0;
				break;
			case 5:
				loc[0] -= 1;
				loc[1] += 1;
				rnd = 1;
				break;
			case 6:
				loc[0] -= 1;
				rnd = 2;
				break;
			case 7:
				loc[0] -= 1;
				loc[1] -= 1;
				rnd = 3;
				break;
		}
		return rnd;
	}

	/**
	 * 고스트 객체 만들기.
	 */
	static private BackgroundInstance toGhostCreate(int x, int y, int h) {
		Npc n = NpcDatabase.findNameid("$5286");
		BackgroundInstance ghost = new BackgroundInstance();
		ghost.setObjectId(ServerDatabase.nextEtcObjId());
		ghost.setName(n.getNameId());
		ghost.setGfx(n.getGfx());	// 6249
		ghost.setGfxMode(n.getGfxMode());
		ghost.setX(x);
		ghost.setY(y);
		ghost.setHeading(h);
		ghost.setLawful(n.getLawful());
		return ghost;
	}

	/*
	 * 랜덤 버프 용
	 * 이벤트클레스랑 외부 트랩클레스에서 호출됨.
	 * 그래서 동기화
	 */
	static public void toRandomBuff(PcInstance pc, boolean ghost) {
		switch( Util.random(0, 10) ){
			case 0:
				// 커스 블라인드
				CurseBlind.init(pc, SkillDatabase.find(3, 3).getBuffDuration());
				break;
			case 1:
				// 슬로우
				Slow.init(pc, SkillDatabase.find(4, 4).getBuffDuration());
				break;
			case 2:
				// 고스트로부터의 버프
				if(ghost)
					// 커스 패럴라이즈
					CurseParalyze.init(pc, SkillDatabase.find(5, 0).getBuffDuration());
				// 트랩을 통한 버프
				else
					// 헤이스트
					Haste.init(pc, SkillDatabase.find(6, 2).getBuffDuration());
				break;
		}
	}

	/*
	 * 트랩 표현하기
	 * 1. 트랩 표현
	 * 2. 두액션 
	 * 3. 트랩 제거
	 */
	static private void toVisualTrap(PcInstance pc) {
		// 트랩 객체 생성
		BackgroundInstance trap = new BackgroundInstance();
		trap.setObjectId(ServerDatabase.nextEtcObjId());
		trap.setGfx(6335);
		trap.setX(pc.getX());
		trap.setY(pc.getY());
		// 트랩 그리기
		pc.toSender(S_ObjectAdd.clone(BasePacketPooling.getPool(S_ObjectAdd.class), trap, pc), true);
		// 액션 취하기
		pc.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), trap, 28), true);
		// 삭제
		pc.toSender(S_ObjectRemove.clone(BasePacketPooling.getPool(S_ObjectRemove.class), trap), true);
	}
	
	/**
	 * EventAction 에 따른 처리 멘트
	 * @param pc
	 */
	static private void toEventMessage(PcInstance pc) {
		switch(STATUS) {
			case READY:
				// 0명의 선수가 대기 중이군, 앞으로 0초후에 시작 될게야.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1168, String.valueOf(list.size()), String.valueOf(idx2)));
				break;
			case GO:
				// 0명의 선수가 경기를 시작한지 0초 정도 되었다네.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1169, String.valueOf(list.size()), String.valueOf(idx2)));
				break;
		}
	}
	
	static private void toEventJoin(PcInstance pc) {
		// 등록되지않은 사용자일 때만. 최대 10명까지 받기.
		if(!containsList(pc)) {
			if(list.size() >= 15) {
				ChattingController.toChatting(pc, "더이상 참여할 수 없습니다.", 20);
				return;
			}
			// 아데나확인
			if(pc.getInventory().isAden(ADEN, true)) {
				// 파티해제
				PartyController.close(pc);
				// 유저 추가
				addList(pc);
				// 유령의 집으로 텔레포트
				pc.toPotal(32722, 32830, MAP);
			}else{
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
	}
	
}
