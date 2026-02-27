package lineage.world.controller;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Npc;
import lineage.database.BackgroundDatabase;
import lineage.database.MonsterDatabase;
import lineage.database.MonsterSpawnlistDatabase;
import lineage.database.NpcDatabase;
import lineage.database.NpcSpawnlistDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.thread.AiThread;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.object;
import lineage.world.object.instance.ItemIllusionInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.RobotInstance;

public class EventController {

	static private List<object> list_illusion;							// 수렵 : 처리할 엔피시들
	static private List<ItemIllusionInstance> list_item;				// 수렵 
	static private List<object> list_christmas;							// 크리스마스 이벤트 : 처리할 엔피시들
	static private List<object> list_christmas_background;				// 크리스마스 이벤트 : 배경용
	static private List<object> list_halloween;							// 할로윈 이벤트 : 처리할 엔피시들
	static private List<MonsterInstance> list_halloween_mon;			// 할로윈 이벤트 : 관리 몬스터 목록들
	static private List<object> list_bounty;							// 현상범 쿠작 이벤트 : 처리할 엔피시들
	static private List<MonsterInstance> list_bounty_mon;				// 현상범 쿠작 이벤트 : 관리 몬스터 목록들
	
	static private List<object> list_remove;
	static private List<object> list_giran;

	static public void init(){
		TimeLine.start("EventController..");


		// 수렵 이벤트
		list_illusion = new ArrayList<object>();
		list_item = new ArrayList<ItemIllusionInstance>();
		Npc npc = NpcDatabase.find("마리안느");
		if(npc != null){
			for(int i=0 ; i<11 ; ++i){
				object o = NpcSpawnlistDatabase.newObject(npc);
				if(o != null){
					o.setObjectId(ServerDatabase.nextNpcObjId());
					o.setName(npc.getNameId());
					o.setGfx(npc.getGfx());
					o.setGfxMode(npc.getGfxMode());
					list_illusion.add( o );
				}
			}
			if(Lineage.event_illusion)
				toIllusion(Lineage.event_illusion);
		}
		// 크리스마스 이벤트
		list_christmas = new ArrayList<object>();
		list_christmas_background = new ArrayList<object>();
		npc = NpcDatabase.find("오크 산타");
		if(npc != null){
			for(int i=0 ; i<11 ; ++i){
				object o = NpcSpawnlistDatabase.newObject(npc);
				if(o != null){
					// npc
					o.setObjectId(ServerDatabase.nextNpcObjId());
					o.setName(npc.getNameId());
					o.setGfx(npc.getGfx());
					o.setGfxMode(npc.getGfxMode());
					list_christmas.add( o );
					// 트리
					object b = BackgroundDatabase.toObject(null, 1995, null);
					b.setObjectId(ServerDatabase.nextNpcObjId());
					b.setGfx(1995);
					b.setLight(13);
					list_christmas_background.add(b);
				}
			}
			if(Lineage.event_christmas)
				toChristmas(Lineage.event_christmas);
		}
		// 할로윈 이벤트
		list_halloween_mon = new ArrayList<MonsterInstance>();
		list_halloween = new ArrayList<object>();
		npc = NpcDatabase.find("잭-오-랜턴");
		if(npc != null){
			for(int i=0 ; i<12 ; ++i){
				object o = NpcSpawnlistDatabase.newObject(npc);
				if(o != null){
					// npc
					o.setObjectId(ServerDatabase.nextNpcObjId());
					o.setName(npc.getNameId());
					o.setGfx(npc.getGfx());
					o.setGfxMode(npc.getGfxMode());
					list_halloween.add( o );
				}
			}
			if(Lineage.event_halloween)
				toHalloween(Lineage.event_halloween);
		}
		
		// 현상범 쿠작 이벤트
		list_bounty_mon = new ArrayList<MonsterInstance>();
		if(Lineage.event_kujak){
			    tobounty(Lineage.event_kujak);
		}

		list_giran = new ArrayList<object>();
		npc = NpcDatabase.find("기란감옥");
		if(npc != null) {
			object o = NpcSpawnlistDatabase.newObject(npc);
			if(o != null) {
				o.setObjectId(ServerDatabase.nextNpcObjId());
				o.setName(npc.getNameId());
				o.setGfx(npc.getGfx());
				o.setGfxMode(npc.getGfxMode());
				list_giran.add( o );
			}
		}
		TimeLine.end();
	}

	/**
	 * 할로윈 이벤트 몬스터 관리목록에 등록.
	 * 	: lineage.world.object.monster.event.JackLantern.toTeleport(final int x, final int y, final int map, final boolean effect);
	 * @param mi
	 */
	static public void appendHalloweenMonster(MonsterInstance mi){
		if(!Lineage.event_halloween)
			return;

		synchronized (list_halloween_mon) {
			list_halloween_mon.add(mi);
		}
	}

	/**
	 * 할로윈 이벤트 몬스터 관리목록에서 제거.
	 * 	: lineage.world.object.monster.event.JackLantern.toAiDead(long time)
	 * @param mi
	 */
	static public void removeHalloweenMonster(MonsterInstance mi){
		synchronized (list_halloween_mon) {
			list_halloween_mon.remove(mi);
		}
	}

	/**
	 * 현상범 쿠작 이벤트 몬스터 관리목록에 등록.
	 * 	: lineage.world.object.monster.event.JackLantern.toTeleport(final int x, final int y, final int map, final boolean effect);
	 * @param mi
	 */
	static public void appendbountyMonster(MonsterInstance mi){
		if(!Lineage.event_kujak)
			return;

		synchronized (list_bounty_mon) {
			list_bounty_mon.add(mi);
		}
	}

	/**
	 * 현상범 쿠작 이벤트 몬스터 관리목록에서 제거.
	 * 	: lineage.world.object.monster.event.JackLantern.toAiDead(long time)
	 * @param mi
	 */
	static public void removebountyMonster(MonsterInstance mi){
		synchronized (list_bounty_mon) {
			list_bounty_mon.remove(mi);
		}
	}
	/**
	 * 객체가 이동하다가 범위를 재갱신 할때 요청됨.
	 * 	object.toMoving
	 * @param pc
	 */
	static public void toUpdate(PcInstance pc){
		// 할로윈 이벤트 처리. (로봇이 아닐때만.)
		if(Lineage.event_halloween && !(pc instanceof RobotInstance)){
			int rnd = Util.random(0, 100);
			if(rnd>30 || !World.isNormalZone(pc.getX(), pc.getY(), pc.getMap()))
				return;

			// 몬스터 스폰.
			MonsterInstance mi = MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find( rnd<5 ? "잭-0-랜턴" : "잭-O-랜턴" ));
			mi.setHomeX( Util.random(pc.getX()-Lineage.SEARCH_LOCATIONRANGE, pc.getX()+-Lineage.SEARCH_LOCATIONRANGE) );
			mi.setHomeY( Util.random(pc.getY()-Lineage.SEARCH_LOCATIONRANGE, pc.getY()+-Lineage.SEARCH_LOCATIONRANGE) );
			mi.setHomeLoc(Lineage.SEARCH_WORLD_LOCATION);
			mi.setHomeMap(pc.getMap());
			mi.toTeleport(mi.getHomeX(), mi.getHomeY(), mi.getHomeMap(), false);
			mi.readDrop();
			AiThread.append(mi);
		}
		
		// 할로윈 이벤트 처리. (로봇이 아닐때만.)
		if(Lineage.event_kujak && !(pc instanceof RobotInstance)){
			int rnd = Util.random(0, 100);
			if(rnd>40 || !World.isNormalZone(pc.getX(), pc.getY(), pc.getMap()))
				return;

			// 몬스터 스폰.
			MonsterInstance mi = MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find( rnd<10 ? "현상범 쿠작" : "" ));
			mi.setHomeX( Util.random(pc.getX()-Lineage.SEARCH_LOCATIONRANGE, pc.getX()+-Lineage.SEARCH_LOCATIONRANGE) );
			mi.setHomeY( Util.random(pc.getY()-Lineage.SEARCH_LOCATIONRANGE, pc.getY()+-Lineage.SEARCH_LOCATIONRANGE) );
			mi.setHomeLoc(Lineage.SEARCH_WORLD_LOCATION);
			mi.setHomeMap(pc.getMap());
			mi.toTeleport(mi.getHomeX(), mi.getHomeY(), mi.getHomeMap(), false);
			mi.readDrop();
			AiThread.append(mi);
		}
	}

	/**
	 * 할로윈 이벤트 처리 함수.
	 * @param is
	 */
	static public void toHalloween(boolean is){
		Lineage.event_halloween = is;
		World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("할로윈 이벤트가 %s활성화 되었습니다.", Lineage.event_halloween ? "" : "비")) );

		// 스폰된 전체 상점 추출.
		if(Lineage.event_halloween){
			for(int i=0 ; i<list_halloween.size() ; ++i){
				object o = list_halloween.get(i);
				switch(i){
				case 0:	// 기란
					o.setHomeX(33421);
					o.setHomeY(32804);
					o.setHomeMap(4);
					o.setHomeHeading(6);
					break;
				case 1:	// 하이네
					o.setHomeX(33607);
					o.setHomeY(33229);
					o.setHomeMap(4);
					o.setHomeHeading(5);
					break;
				case 2:	// 은기사
					o.setHomeX(33073);
					o.setHomeY(33400);
					o.setHomeMap(4);
					o.setHomeHeading(5);
					break;
				case 3:	// 우드벡
					o.setHomeX(32613);
					o.setHomeY(33184);
					o.setHomeMap(4);
					o.setHomeHeading(5);
					break;
				case 4:	// 켄트
					o.setHomeX(33076);
					o.setHomeY(32795);
					o.setHomeMap(4);
					o.setHomeHeading(6);
					break;
				case 5:	// 화전민
					o.setHomeX(32753);
					o.setHomeY(32440);
					o.setHomeMap(4);
					o.setHomeHeading(6);
					break;
				case 6:	// 글루딘
					o.setHomeX(32610);
					o.setHomeY(32723);
					o.setHomeMap(4);
					o.setHomeHeading(6);
					break;
				case 7:	// 웰던
					o.setHomeX(33726);
					o.setHomeY(32485);
					o.setHomeMap(4);
					o.setHomeHeading(4);
					break;
				case 8:	// 오렌
					o.setHomeX(34053);
					o.setHomeY(32282);
					o.setHomeMap(4);
					o.setHomeHeading(4);
					break;
				case 9:	// 아덴
					o.setHomeX(33967);
					o.setHomeY(33242);
					o.setHomeMap(4);
					o.setHomeHeading(6);
					break;
				case 10:	// 요정숲
					o.setHomeX(33057);
					o.setHomeY(32320);
					o.setHomeMap(4);
					o.setHomeHeading(4);
					break;
				case 11:	// 말하는섬
					o.setHomeX(32575);
					o.setHomeY(32950);
					o.setHomeMap(0);
					o.setHomeHeading(6);
					break;
				}
				o.setHeading( o.getHomeHeading() );
				o.toTeleport(o.getHomeX(), o.getHomeY(), o.getHomeMap(), false);
			}
		}else{
			// npc 제거.
			for(object o : list_halloween){
				World.remove(o);
				o.clearList(true);
			}
			// mon 제거.
			for(MonsterInstance mi : list_halloween_mon){
				World.remove(mi);
				mi.clearList(true);
				mi.setAiStatus( -2 );
			}
			list_halloween_mon.clear();
		}

	}
	
	/**
	 * 현상범 쿠작 이벤트 처리 함수.
	 * @param is
	 */
	static public void tobounty(boolean is){
		Lineage.event_kujak = is;
		if(Lineage.event_kujak){
		    World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("현상범 쿠작을 잡아라 이벤트가 시작되었습니다.")));
		}else{
		    World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("현상범 쿠작을 잡아라 이벤트가 종료되었습니다.")));
		// 스폰된 전체 상점 추출.
		}
		if(Lineage.event_kujak){
			// mon 제거.
			for(MonsterInstance mi : list_bounty_mon){
				World.remove(mi);
				mi.clearList(true);
				mi.setAiStatus( -2 );
			}
			list_bounty_mon.clear();
	    }
	}
	

	/**
	 * 크리스마스 이벤트 처리 함수.
	 * @param is
	 */
	static public void toChristmas(boolean is){
		Lineage.event_christmas = is;
		if(Lineage.event_christmas){
			World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("크리스마스 이벤트가 시작되었습니다.")));
			World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("전체몬스터에게 양말이 드랍됩니다.")));
		}else{
			World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("크리스마스 이벤트가 종료되었습니다.")));
			World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("양말이벤트가 종료되었습니다.")));
		}

		if(Lineage.event_christmas){
			for(int i=0 ; i<list_christmas.size() ; ++i){
				object o = list_christmas.get(i);
				object b = list_christmas_background.get(i);
				switch(i){
				case 0:	// 기란
					o.setHomeX(33431);
					o.setHomeY(32808);
					o.setHomeMap(4);
					o.setHomeHeading(4);
					break;
				case 1:	// 하이네
					o.setHomeX(33607);
					o.setHomeY(33231);
					o.setHomeMap(4);
					o.setHomeHeading(4);
					break;
				case 2:	// 은기사
					o.setHomeX(33075);
					o.setHomeY(33385);
					o.setHomeMap(4);
					o.setHomeHeading(4);
					break;
				case 3:	// 우드벡
					o.setHomeX(32607);
					o.setHomeY(33177);
					o.setHomeMap(4);
					o.setHomeHeading(4);
					break;
				case 4:	// 켄트
					o.setHomeX(33049);
					o.setHomeY(32759);
					o.setHomeMap(4);
					o.setHomeHeading(4);
					break;
				case 5:	// 화전민
					o.setHomeX(32735);
					o.setHomeY(32439);
					o.setHomeMap(4);
					o.setHomeHeading(4);
					break;
				case 6:	// 글루딘
					o.setHomeX(32612);
					o.setHomeY(32783);
					o.setHomeMap(4);
					o.setHomeHeading(4);
					break;
				case 7:	// 웰던
					o.setHomeX(33710);
					o.setHomeY(32492);
					o.setHomeMap(4);
					o.setHomeHeading(4);
					break;
				case 8:	// 오렌
					o.setHomeX(34054);
					o.setHomeY(32277);
					o.setHomeMap(4);
					o.setHomeHeading(4);
					break;
				case 9:	// 아덴
					o.setHomeX(33968);
					o.setHomeY(33256);
					o.setHomeMap(4);
					o.setHomeHeading(6);
					break;
				case 10:	// 요정숲
					o.setHomeX(33062);
					o.setHomeY(32344);
					o.setHomeMap(4);
					o.setHomeHeading(6);
					break;
				}
				o.setHeading( o.getHomeHeading() );
				o.toTeleport(o.getHomeX(), o.getHomeY(), o.getHomeMap(), false);
				b.toTeleport(o.getHomeX()+2, o.getHomeY()-2, o.getHomeMap(), false);
			}
		}else{
			// npc 제거.
			for(object o : list_christmas){
				World.remove(o);
				o.clearList(true);
			}
			// 트리 제거
			for(object o : list_christmas_background){
				World.remove(o);
				o.clearList(true);
			}
		}
	}

	/**
	 * 수렵이벤트 처리 함수.
	 */
	static public void toIllusion(boolean is) {
		if(Lineage.server_version < 160)
			return;
		Lineage.event_mon = is;
		World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("수렵 이벤트가 %s활성화 되었습니다.", Lineage.event_lyra ? "" : "비")) );
		World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("수렵대상: 토끼,사슴,멧돼지,곰,여우")));
	}

	/**
	 * 라이라 토템이벤트 처리 함수.
	 * @param is
	 */
	static public void toTotem(boolean is){
		if(Lineage.server_version < 160)
			return;

		Lineage.event_lyra = is;
		World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("토템 이벤트가 %s활성화 되었습니다.", Lineage.event_lyra ? "" : "비")) );
	}

	/**
	 * 버프이벤트 처리 함수.
	 */
	static public void toBuff(boolean is){
		Lineage.event_buff = is;
		World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("자동버프가 %s활성화 되었습니다.", Lineage.event_buff ? "" : "비")) );
	}

	/**
	 * 변신이벤트 처리 함수.
	 */
	static public void toPoly(boolean is){
		Lineage.event_poly = is;
		if(Lineage.event_poly){
			World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("변신 이벤트가 시작되었습니다.")));
			World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("모든 변신이 가능합니다.")));
		}else{
			World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("변신 이벤트가 종료되었습니다.")));
		}
	//	World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("변신 이벤트가 %s활성화 되었습니다.", Lineage.event_poly ? "" : "비")) );
	}
	
	/**
	 * 랭킹 변신이벤트 처리 함수.
	 */
	static public void toPoly2(boolean is){
		Lineage.event_rank_poly = is;
		if(Lineage.event_rank_poly){
			World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("랭킹 변신 이벤트가 시작되었습니다.")));
			World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("랭킹 변신이 가능합니다.")));
		}else{
			World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("랭킹 변신 이벤트가 종료되었습니다.")));
		}
	}

	/**
	 * 생성된 환상아이템 관리목록에 등록.
	 * @param iii
	 */
	static public void appendIllusion(ItemIllusionInstance iii){
		synchronized (list_item) {
			list_item.add(iii);
		}
	}

	/**
	 * 제거된 환상아이템 관리목록에서 제거.
	 * @param iii
	 */
	static public void removeIllusion(ItemIllusionInstance iii){
		synchronized (list_item) {
			list_item.remove(iii);
		}
	}

	/**
	 * 관리목록에 존재하는지 확인.
	 * @param iii
	 * @return
	 */
	static public boolean containsIllusion(ItemIllusionInstance iii){
		synchronized (list_item) {
			return list_item.contains(iii);
		}
	}

	static public int getIllusionItemSize(){
		return list_item.size();
	}
}

