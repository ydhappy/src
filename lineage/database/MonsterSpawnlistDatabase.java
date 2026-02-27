package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lineage.bean.database.Boss;
import lineage.bean.database.Monster;
import lineage.bean.database.MonsterGroup;
import lineage.bean.database.MonsterSpawnlist;
import lineage.bean.lineage.Map;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectGfx;
import lineage.network.packet.server.S_ObjectName;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.thread.AiThread;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CommandController;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.monster.Antharas;
import lineage.world.object.monster.ArachnevilElder;
import lineage.world.object.monster.Elder;
import lineage.world.object.monster.Baphomet;
import lineage.world.object.monster.Blob;
import lineage.world.object.monster.BombFlower;
import lineage.world.object.monster.DeepFlower;
import lineage.world.object.monster.Doppelganger;
import lineage.world.object.monster.Eagle;
import lineage.world.object.monster.Ethin;
import lineage.world.object.monster.FloatingEye;
import lineage.world.object.monster.Gremlin;
import lineage.world.object.monster.IceMonster;
import lineage.world.object.monster.LastavardBoss;
import lineage.world.object.monster.LastavardDoorMan;
import lineage.world.object.monster.Lindvior;
import lineage.world.object.monster.NotSelectedPerson;
import lineage.world.object.monster.SeaAnemone;
import lineage.world.object.monster.Slime;
import lineage.world.object.monster.Sparten;
import lineage.world.object.monster.Sparten1;
import lineage.world.object.monster.Sparten2;
import lineage.world.object.monster.Sparten3;
import lineage.world.object.monster.Spartoi;
import lineage.world.object.monster.Spartoi2;
import lineage.world.object.monster.StoneGolem;
import lineage.world.object.monster.Succubusqueen;
import lineage.world.object.monster.Unicorn;
import lineage.world.object.monster.Wolf;
import lineage.world.object.monster.jevman;
import lineage.world.object.monster.jevwomon;
import lineage.world.object.monster.kirtas;
import lineage.world.object.monster.kirtas1;
import lineage.world.object.monster.Harphy;
import lineage.world.object.monster.Troll;
import lineage.world.object.monster.만드라고라;
import lineage.world.object.monster.event.JackLantern;
import lineage.world.object.monster.event.Wanted;
import lineage.world.object.monster.quest.AtubaOrc;
import lineage.world.object.monster.quest.BetrayedOrcChief;
import lineage.world.object.monster.quest.BetrayerOfUndead;
import lineage.world.object.monster.quest.Bugbear;
import lineage.world.object.monster.quest.CursedExorcistSaell;
import lineage.world.object.monster.quest.DarkElf;
import lineage.world.object.monster.quest.Darkmar;
import lineage.world.object.monster.quest.DudaMaraOrc;
import lineage.world.object.monster.quest.GandiOrc;
import lineage.world.object.monster.quest.MutantGiantQueenAnt;
import lineage.world.object.monster.quest.NerugaOrc;
import lineage.world.object.monster.quest.OrcZombie;
import lineage.world.object.monster.quest.Ramia;
import lineage.world.object.monster.quest.RovaOrc;
import lineage.world.object.monster.quest.Skeleton;
import lineage.world.object.monster.quest.Zombie;
import lineage.world.object.monster.Balthazar;
import lineage.world.object.monster.Deer;
import lineage.world.object.monster.Demon;
import lineage.world.object.monster.Duck;
import lineage.world.object.monster.Hen;
import lineage.world.object.monster.Kaspar;
import lineage.world.object.monster.Knight;
import lineage.world.object.monster.Kouts;
import lineage.world.object.monster.Milkcow;
import lineage.world.object.monster.Nancy;
import lineage.world.object.monster.Necromancer;
import lineage.world.object.monster.Perez;
import lineage.world.object.monster.Pig;
import lineage.world.object.monster.Sema;

public final class MonsterSpawnlistDatabase {

	static private List<MonsterInstance> pool;
	static public List<MonsterInstance> list;
	
	static public void init(Connection con) {
		TimeLine.start("MonsterSpawnlistDatabase..");
		
		// 몬스터 스폰 처리.
			pool = new ArrayList<MonsterInstance>();
			list = new ArrayList<MonsterInstance>();
//		synchronized (pool) {
//			pool.clear();
//		}
//		synchronized (list) {
//			list.clear();
//		}
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM monster_spawnlist");
			rs = st.executeQuery();
			while(rs.next()){
				Monster m = MonsterDatabase.find(rs.getString("monster"));
				if(m != null){
					MonsterSpawnlist ms = new MonsterSpawnlist();
					ms.setUid(rs.getInt("uid"));
					ms.setName(rs.getString("name"));
					ms.setMonster(m);
					ms.setRandom(rs.getString("random").equalsIgnoreCase("true"));
					ms.setCount(rs.getInt("count"));
					ms.setLocSize(rs.getInt("loc_size"));
					ms.setX(rs.getInt("spawn_x"));
					ms.setY(rs.getInt("spawn_y"));
					StringTokenizer stt = new StringTokenizer(rs.getString("spawn_map"), "|");
					while(stt.hasMoreTokens()){
						String map = stt.nextToken();
						if(map.length() > 0)
							ms.getMap().add(Integer.valueOf(map));
					}
					ms.setReSpawn(rs.getInt("re_spawn") * 1000);
					ms.setGroup(rs.getString("groups").equalsIgnoreCase("true"));
					if(ms.isGroup()){
						Monster g1 = MonsterDatabase.find(rs.getString("monster_1"));
						Monster g2 = MonsterDatabase.find(rs.getString("monster_2"));
						Monster g3 = MonsterDatabase.find(rs.getString("monster_3"));
						Monster g4 = MonsterDatabase.find(rs.getString("monster_4"));
						if(g1 != null)
							ms.getListGroup().add(new MonsterGroup(g1, rs.getInt("monster_1_count")));
						if(g2 != null)
							ms.getListGroup().add(new MonsterGroup(g2, rs.getInt("monster_2_count")));
						if(g3 != null)
							ms.getListGroup().add(new MonsterGroup(g3, rs.getInt("monster_3_count")));
						if(g4 != null)
							ms.getListGroup().add(new MonsterGroup(g4, rs.getInt("monster_4_count")));
					}
					ms.setSentry( Boolean.valueOf(rs.getString("sentry")) );
					ms.setHeading(rs.getInt("heading"));
					
					try {
//						System.out.println(ms.getMonster().getName() + " 몬스터 굴러가유");
						toSpawnMonster(ms, null, false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", MonsterSpawnlistDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		
		TimeLine.end();
	}
	
	static public void close() {
		synchronized (list) {
			for(MonsterInstance mi : list) {
				mi.setDead(true);
				World.remove(mi);
				mi.clearList(true);
				mi.setAiStatus(-2);
				
				
			}
		}
	}
	
	/**
	 * 중복코드 방지용 : gui 기능에서 사용중 lineage.gui.dialog.MonsterSpawn.step4()
	 */
	static public void toSpawnMonster(MonsterSpawnlist ms, Map map, boolean isBoss) {
		// 버그 방지
		if (ms == null || ms.getMap().size() <= 0)
			return;
		// 맵 확인.
		if (map == null) {
			if (ms.getMap().size() > 1)
				map = World.get_map(ms.getMap().get(Util.random(0, ms.getMap().size() - 1)));
			else
				map = World.get_map(ms.getMap().get(0));
		}
		if (map == null)
			return;
		// 스폰처리.
		for (int i = 0; i < ms.getCount(); ++i) {
			MonsterInstance mi = newInstance(ms.getMonster());
			if (mi == null)
				return;
			mi.setBoss(isBoss ? new Boss() : null);
			mi.setMonsterSpawnlist(ms);
			mi.setDatabaseKey(Integer.valueOf(ms.getUid()));
			mi.setHomeHeading(ms.getHeading());
			if (ms.isGroup()) {
				mi.setGroupMaster(mi);
				// 마스터 스폰.
				toSpawnMonster(mi, map, ms.isRandom(), ms.getX(), ms.getY(), map.mapid, ms.getLocSize(),
						ms.getReSpawn(), true, true, true, true);
				// 관리객체 생성.
				for (MonsterGroup mg : ms.getListGroup()) {
					for (int j = mg.getCount(); j > 0; --j) {
						MonsterInstance a = newInstance(mg.getMonster());
						if (a != null) {
							// 스폰
							toSpawnMonster(a, map, false, mi.getHomeX(), mi.getHomeY(), map.mapid, ms.getLocSize(),
									ms.getReSpawn(), true, true, true, true);
							// 마스터관리 목록에 등록.
							mi.getGroupList().add(a);
							// 관리하고있는 마스터가 누군지 지정.
							a.setGroupMaster(mi);
						}
					}
				}
			} else {
				toSpawnMonster(mi, map, ms.isRandom(), ms.getX(), ms.getY(), map.mapid, ms.getLocSize(),
						ms.getReSpawn(), true, true, true, true);
			}
		}
	}
	
	/**
	 * 중복 코드를 막기위해 함수로 따로 뺌.
	 */
	static public void toSpawnMonster(MonsterInstance mi, Map m, boolean random, int x, int y, int map, int loc,
			int respawn, boolean drop, boolean ai, boolean append, boolean spawn) {
		if (mi == null)
			return;

		int roop_cnt = 0;
		int lx = x;
		int ly = y;
		if (random) {
			//
			if (loc <= 1)
				loc = Lineage.SEARCH_WORLD_LOCATION;
			// 랜덤 좌표 스폰
			do {
				if (x > 0) {
					lx = Util.random(x - (loc / 2), x + (loc / 2));
					ly = Util.random(y - (loc / 2), y + (loc / 2));
				} else {
					lx = Util.random(m.locX1, m.locX2);
					ly = Util.random(m.locY1, m.locY2);
				}
				if (roop_cnt++ > 100) {
					lx = x;
					ly = y;
					break;
				}
				// 모든방향을 다 체크했었는데 그냥 0방향만 이동가능한지만 체크하도록 변경.
			} while (!World.isThroughObject(lx, ly, map, 0));
		} else {
			// 좌표 스폰
			lx = x;
			ly = y;
			if (loc > 1 && x > 0) {
				// // 모든방향을 다 체크했었는데 그냥 0방향만 이동가능한지만 체크하도록 변경.
				while (!World.isThroughObject(lx, ly, map, 0)) {
					lx = Util.random(x - (loc / 2), x + (loc / 2));
					ly = Util.random(y - (loc / 2), y + (loc / 2));
					if (roop_cnt++ > 100) {
						lx = x;
						ly = y;
						break;
					}
				}
			}
		}
		mi.setReSpawnTime(respawn);
		mi.setHomeX(lx);
		mi.setHomeY(ly);
		mi.setHomeLoc(loc);
		mi.setHomeMap(map);

		if (spawn)
			mi.toTeleport(lx, ly, map, false);
		if(drop)
			mi.readDrop();
		if (ai)
			AiThread.append(mi);
		if (append) {
			synchronized (list) {
				list.add(mi);
			}
		}
	}

	static public MonsterInstance newInstance(Monster m, Object...opt) {
		//
		MonsterInstance mi = null;
		if(m != null) {
			//
			Object o = PluginController.init(MonsterSpawnlistDatabase.class, "newInstance", m);
			if(o == null) {
				switch(m.getNameIdNumber()){
				    //보스 멘트작업
			   	   	case 936: //토끼
				   	case 256: //개구리
				    case 930: //사슴
					   mi = Deer.clone(getPool(Deer.class), m);
					   break;
					case 952:	// 오리
					   mi = Duck.clone(getPool(Duck.class), m);
					   break;
					case 927:	// 돼지
					   mi = Pig.clone(getPool(Pig.class), m);
					   break;
					case 928:	// 암닭
					   mi = Hen.clone(getPool(Hen.class), m);
					   break;
					case 929:	// 젖소
					   mi = Milkcow.clone(getPool(Milkcow.class), m);
					   break;
					case 331:	// 네크로맨서
					   mi = Necromancer.clone(getPool(Necromancer.class), m);
					   break;
					case 335:	// 발터자르
					   mi = Balthazar.clone(getPool(Balthazar.class), m);
					   break;
					case 336:	// 카스파
					   mi = Kaspar.clone(getPool(Kaspar.class), m);
					   break;
					case 337:	// 메르키오르
					   mi = Nancy.clone(getPool(Nancy.class), m);
					   break;
					case 338:	// 세마
					   mi = Sema.clone(getPool(Sema.class), m);
					   break;
					case 371:	// 데스나이트
					   mi = Knight.clone(getPool(Knight.class), m);
					   break;
					case 306:	// 바포메트
						mi = Baphomet.clone(getPool(Baphomet.class), m);
						break;
					case 945:	// 베레스
					   mi = Perez.clone(getPool(Perez.class), m);
					   break;
					case 274:	// 커츠
					   mi = Kouts.clone(getPool(Kouts.class), m);
					   break;
					case 1175:	// 데몬
					   mi = Demon.clone(getPool(Demon.class), m);
					   break;
					case 992:	// 흑장로
					   mi = Elder.clone(getPool(Elder.class), m);
					   break; 
					case 1116:	// 안타라스
						mi = Antharas.clone(getPool(Antharas.class), m);
						break;
					case 6:		// 괴물 눈
						mi = FloatingEye.clone(getPool(FloatingEye.class), m);
						break;
					case 7:		// 해골
						mi = Skeleton.clone(getPool(Skeleton.class), m);
						break;
					case 8:		// 슬라임
						mi = Slime.clone(getPool(Slime.class), m);
						break;
					case 56:	// 돌골렘
						mi = StoneGolem.clone(getPool(StoneGolem.class), m);
						break;
					case 57:	// 좀비
						mi = Zombie.clone(getPool(Zombie.class), m);
						break;
					case 268: // 늑대
					case 904: // 세인트버나드
					case 905: // 도베르만
					case 906: // 콜리
					case 907: // 세퍼드
					case 908: // 비글
					case 1397: // 여우
					case 1495: // 곰
					case 1788: // 허스키
					case 2260: // 팬더
					case 2261: // 너구리
					case 4073: // 진돗개
					case 4072: // 아기 진돗개
					case 2264: // 캥거루
					case 2563: // 열혈토끼
					case 2701: // 고양이
					case 3508: // 라쿤
					case 4077: // 아기 판다곰
					case 4079: // 아기 캥거루
					case 4078: // 공포의 판다곰
					case 4074:
					case 17669:
						mi = Wolf.clone(getPool(Wolf.class), m);
						break;
					case 5492: //만드라고라
						mi = 만드라고라.clone(getPool(만드라고라.class), m);
						break;
					case 271:	// 독수리
						mi = Eagle.clone(getPool(Eagle.class), m);
						break;
					case 318:	// 스파토이
						mi = Spartoi.clone(getPool(Spartoi.class), m);
						break;
					case 2081:	// 배신자의 해골근위병
						mi = Spartoi2.clone(getPool(Spartoi.class), m);
						break;
					case 1745:	// 봉인된 서큐버스 퀸
						mi = Succubusqueen.clone(getPool(Spartoi.class), m);
						break;
					case 1739:	// 저주받은 에틴
						mi = Ethin.clone(getPool(Spartoi.class), m);
						break;
					case 319:	// 웅골리언트
						mi = ArachnevilElder.clone(getPool(ArachnevilElder.class), m);
						break;
					case 325:	// 버그베어
						mi = Bugbear.clone(getPool(Bugbear.class), m);
						break;
					case 494:	// 아투바 오크
						mi = AtubaOrc.clone(getPool(AtubaOrc.class), m);
						break;
					case 495:	// 네루가 오크
						mi = NerugaOrc.clone(getPool(NerugaOrc.class), m);
						break;
					case 496:	// 간디 오크
						mi = GandiOrc.clone(getPool(GandiOrc.class), m);
						break;
					case 497:	// 로바 오크
						mi = RovaOrc.clone(getPool(RovaOrc.class), m);
						break;
					case 498:	// 두다-마라 오크
						mi = DudaMaraOrc.clone(getPool(DudaMaraOrc.class), m);
						break;
					case 758:	// 브롭
						mi = Blob.clone(getPool(Blob.class), m);
						break;
					case 959:	// 하피
					case 1704:
						mi = Harphy.clone(getPool(Harphy.class), m);
						break;
					case 989:	// 트롤
					case 1716:	// 굶주린 트롤
						mi = Troll.clone(getPool(Troll.class), m);
						break;
					case 1041:	// 오크좀비
						mi = OrcZombie.clone(getPool(OrcZombie.class), m);
						break;
					case 1042:	// 다크엘프
						mi = DarkElf.clone(getPool(DarkElf.class), m);
						break;
					case 1046:	// 그렘린
						mi = Gremlin.clone(getPool(Gremlin.class), m);
						break;
					case 1428:	// 라미아
						mi = Ramia.clone(getPool(Ramia.class), m);
						break;
					case 1425:	// 말미잘
						mi = SeaAnemone.clone(getPool(SeaAnemone.class), m);
						break;
					case 1554:	// 도펠갱어
						mi = Doppelganger.clone(getPool(Doppelganger.class), m);
						break;
					case 1571:	// 폭탄꽃
						mi = BombFlower.clone(getPool(BombFlower.class), m);
						break;
					case 2017:	// 다크마르
						mi = Darkmar.clone(getPool(Darkmar.class), m);
						break;
					case 2020:	// 언데드의 배신자
						mi = BetrayerOfUndead.clone(getPool(BetrayerOfUndead.class), m);
						break;
					case 2063:	// 잭-O-랜턴
					case 2064:	// 잭-0-랜턴
						mi = JackLantern.clone(getPool(JackLantern.class), m);
						break;
					case 5232: // 현상범 쿠작
						mi = Wanted.clone(getPool(Wanted.class), m);
						break;
					case 2073:	// 변종 거대 여왕 개미
						mi = MutantGiantQueenAnt.clone(getPool(MutantGiantQueenAnt.class), m);
						break;
					case 2116:	// 린드비오르
						mi = Lindvior.clone(getPool(Lindvior.class), m);
						break;
					case 2219:	// 배신당한 오크대장
						mi = BetrayedOrcChief.clone(getPool(BetrayedOrcChief.class), m);
						break;
					case 2488:	// 유니콘
						mi = Unicorn.clone(getPool(Unicorn.class), m);
						break;
					case 3148:	// 딥플라워
						mi = DeepFlower.clone(getPool(DeepFlower.class), m);
						break;
					case 3139:	// 라이아
					case 3798:	// 암살군왕 슬레이브
					case 3799:	// 암살단장 블레이즈
					case 3804:	// 마수군왕 바란카
					case 3805:	// 마수단장 카이바르
					case 3807:	// 여단장 다크펜서
					case 3814:	// 팬텀 나이트 (라스타바드 4F)
					case 3816:	// 신관장 바운티
					case 3817:	// 마법단장 카르미엘
					case 3825:	// 명법군왕 헬바인
					case 3826:	// 명법단장 크리퍼스
					case 3827:	// 용병대장 메파이스토
					case 3828:	// 어둠의 복수자
					case 3829:	// 핏빛 기사
					case 4297:	// 대법관 바로메스
					case 4300:	// 대법관 케이나
					case 4302:	// 대법관 비아타스
					case 4303:	// 대법관 엔디아스
					case 4304:	// 장로 수행원
					case 4383:	// 라스타바드 근위대
					case 4465:	// 부제사장 카산드라
					case 4299:	// 대법관 이데아
					case 4301:	// 대법관 티아메스
					case 4298:	// 대법관 라미아스
					case 4296:	// 대법관 바로드
						try {
							if((boolean)opt[0])
								mi = LastavardBoss.clone(getPool(LastavardBoss.class), m);
						} catch (Exception e) {
							mi = MonsterInstance.clone(getPool(MonsterInstance.class), m);
						}
						break;
					case 3887:	// 라스타바드 문지기
						mi = LastavardDoorMan.clone(getPool(LastavardDoorMan.class), m);
						break;
					case 4986:	// 선택받지 못한 자
						mi = NotSelectedPerson.clone(getPool(NotSelectedPerson.class), m);
						break;
					case 4347:	// 저주받은 무녀 사엘
						mi = CursedExorcistSaell.clone(getPool(CursedExorcistSaell.class), m);
						break;
					case 4305:	// 기르타스
						mi = kirtas.clone(getPool(kirtas.class), m);
						break;
					case 6444:
						mi = jevman.clone(getPool(jevman.class), m);
						break;
					case 8319:
						mi = jevwomon.clone(getPool(jevwomon.class), m);
						break;
					case 17661://랜덤텔레포터
						mi = Sparten.clone(getPool(Sparten.class), m);
						break;
					case 17662://1층텔레포터
						mi = Sparten1.clone(getPool(Sparten1.class), m);
						break;
					case 17663://2층텔레포터
						mi = Sparten2.clone(getPool(Sparten2.class), m);
						break;
					case 17664://3층텔레포터
						mi = Sparten3.clone(getPool(Sparten3.class), m);
						break;
					default:
						//
						switch (m.getGfx()) {
						case 6632: // 얼음동굴 얼려있는 몬스터.
						case 6634:
						case 6638:
						case 7143:
							mi = IceMonster.clone(getPool(IceMonster.class), m);
							break;
						default:
						mi = MonsterInstance.clone(getPool(MonsterInstance.class), m);
						break;
						
						}
				}
			} else {
				if(m.getName().equalsIgnoreCase("제브레퀴(남)") ||(m.getName().equalsIgnoreCase("제브레퀴(여)"))){
					mi = kirtas1.clone(getPool(kirtas1.class), m);
				}
				mi = (MonsterInstance)o;
				mi = MonsterInstance.clone(getPool(MonsterInstance.class), m);
			}
			if(mi != null)
				mi = newInstance(mi, m);
		}
		return mi;
	}
	
	static public void storeSpwan(object o, Monster m){
		Connection con = null;
		PreparedStatement stt = null;
		
		// 디비 등록 처리.
		try{
			int x = o.getX();
			int y = o.getY();
			int mapid = o.getMap();
			con = DatabaseConnection.getLineage();
			stt = con.prepareStatement("INSERT INTO monster_spawnlist SET name=?,monster=?,count=?,loc_size=?,spawn_x=?, spawn_y=?, spawn_map=?");
			stt.setString(1, m.getName());
			stt.setString(2, m.getName());
			stt.setInt(3, 1);
			stt.setInt(4, 0);
			stt.setInt(5, x);
			stt.setInt(6, y);
			stt.setInt(7, mapid);
			stt.execute();
			
			MonsterSpawnlist ms2 = new MonsterSpawnlist();
			ms2.setName(m.getName());
			ms2.setMonster(m);
			ms2.setCount(1);
			ms2.setLocSize(0);
			ms2.setX(x);
			ms2.setY(y);
			ms2.getMap().add(mapid);
			
			MonsterSpawnlistDatabase.toSpawnMonster(ms2, null, false);
			ChattingController.toChatting(o, "[서버알림] " + m.getName() + " <스폰및DB등록> 완료.", 20);
		} catch (Exception e) {
			lineage.share.System.printf("%s : 몬스터 등록에러", CommandController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, stt);
		}
	}
	
	static private MonsterInstance newInstance(MonsterInstance mi, Monster m) {
		mi.setObjectId(mi.getObjectId()==0 ? ServerDatabase.nextNpcObjId() : mi.getObjectId());
		mi.setGfx(m.getGfx());
		mi.setGfxMode(m.getGfxMode());
		mi.setClassGfx(m.getGfx());
		mi.setClassGfxMode(m.getGfxMode());
		mi.setName(m.getNameId());
		mi.setLevel(m.getLevel());
		mi.setExp(m.getExp());
		mi.setMaxHp(m.getHp());
		mi.setMaxMp(m.getMp());
		mi.setNowHp(m.getHp());
		mi.setNowMp(m.getMp());
		mi.setLawful(m.getLawful());
		mi.setEarthress(m.getResistanceEarth());
		mi.setFireress(m.getResistanceFire());
		mi.setWindress(m.getResistanceWind());
		mi.setWaterress(m.getResistanceWater());
		return mi;
	}
	

	static public void changeMonsterRenew(MonsterInstance mi, Monster m) {
		// monster
		newInstance(mi, m);
		mi.setMonster(m);
		mi.clearExpList();
		mi.setGfxMode(0);
		//
		mi.toSender(S_ObjectGfx.clone(BasePacketPooling.getPool(S_ObjectGfx.class), mi), false);
		mi.toSender(S_ObjectName.clone(BasePacketPooling.getPool(S_ObjectName.class), mi), false);
	}

	
	static public void changeMonster(MonsterInstance mi, Monster m) {
		// monster
		newInstance(mi, m);
		mi.setMonster(m);
		// drop
		mi.readDrop();
		// exp
		mi.clearExpList();
		mi.setGfxMode(0);
		//
		mi.toSender(S_ObjectGfx.clone(BasePacketPooling.getPool(S_ObjectGfx.class), mi), false);
		mi.toSender(S_ObjectName.clone(BasePacketPooling.getPool(S_ObjectName.class), mi), false);
	}
	
	static public MonsterInstance getPool(Class<?> c){
		synchronized (pool) {
			MonsterInstance mon = null;
			if(Lineage.memory_recycle) {
				for(MonsterInstance mi : pool){
					if(mi.getClass().toString().equals(c.toString())){
						mon = mi;
						break;
					}
				}
				if(mon != null)
					pool.remove(mon);
			}
			return mon;
		}
	}
	
	static public void setPool(MonsterInstance mi){
		mi.close();
		synchronized (list) {
			list.remove(mi);
		}
		if(!Lineage.memory_recycle)
			return;
		synchronized (pool) {
			if(!pool.contains(mi))
				pool.add(mi);
		}
		
//		lineage.share.System.println(MonsterSpawnlistDatabase.class.toString()+" : pool.add("+pool.size()+")");
	}
	
	static public int getPoolSize(){
		return pool.size();
	}
	
	static public void insert(
				Connection con, String name, String monster, boolean random, int count, int loc_size, 
				int x, int y, int map, int re_spawn, boolean groups, 
				String monster_1, int monster_1_count, String monster_2, int monster_2_count, 
				String monster_3, int monster_3_count, String monster_4, int monster_4_count
			){
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("INSERT INTO monster_spawnlist SET name=?, monster=?, random=?, count=?, loc_size=?, spawn_x=?, spawn_y=?, spawn_map=?, re_spawn=?, groups=?, monster_1=?, monster_1_count=?, monster_2=?, monster_2_count=?, monster_3=?, monster_3_count=?, monster_4=?, monster_4_count=?");
			st.setString(1, name);
			st.setString(2, monster);
			st.setString(3, String.valueOf(random));
			st.setInt(4, count);
			st.setInt(5, loc_size);
			st.setInt(6, x);
			st.setInt(7, y);
			st.setInt(8, map);
			st.setInt(9, re_spawn);
			st.setString(10, String.valueOf(groups));
			st.setString(11, monster_1);
			st.setInt(12, monster_1_count);
			st.setString(13, monster_2);
			st.setInt(14, monster_2_count);
			st.setString(15, monster_3);
			st.setInt(16, monster_3_count);
			st.setString(17, monster_4);
			st.setInt(18, monster_4_count);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : insert()\r\n", MonsterSpawnlistDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}
	
	/**
	 * 
	 * 어느 맵에 스폰되어있는지 확인해주는 함수
	 * @param map 
	 */
	
	static public MonsterInstance find(String name) {
		synchronized (list) {
			for(MonsterInstance m : list){
				if(m.getMonster().getName().equalsIgnoreCase(name))
					return m;
			}
			return null;
		}
	}
	
	/**
	 * 지정한 맵에 해당하는 필드만 찾아서 스폰처리.
	 * @param map
	 */
	static public void toSpawnMonster(int mapId) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM monster_spawnlist WHERE spawn_map=?");
			st.setInt(1, mapId);
			rs = st.executeQuery();
			while(rs.next()) {
				Monster m = MonsterDatabase.find(rs.getString("monster"));
				if(m != null){
					MonsterSpawnlist ms = new MonsterSpawnlist();
					ms.setUid(rs.getInt("uid"));
					ms.setName(rs.getString("name"));
					ms.setMonster(m);
					ms.setRandom(rs.getString("random").equalsIgnoreCase("true"));
					ms.setCount(rs.getInt("count"));
					ms.setLocSize(rs.getInt("loc_size"));
					ms.setX(rs.getInt("spawn_x"));
					ms.setY(rs.getInt("spawn_y"));
					StringTokenizer stt = new StringTokenizer(rs.getString("spawn_map"), "|");
					while(stt.hasMoreTokens()){
						String map = stt.nextToken();
						if(map.length() > 0)
							ms.getMap().add(Integer.valueOf(map));
					}
					ms.setReSpawn(rs.getInt("re_spawn") * 1000);
					ms.setGroup(rs.getString("groups").equalsIgnoreCase("true"));
					if(ms.isGroup()){
						Monster g1 = MonsterDatabase.find(rs.getString("monster_1"));
						Monster g2 = MonsterDatabase.find(rs.getString("monster_2"));
						Monster g3 = MonsterDatabase.find(rs.getString("monster_3"));
						Monster g4 = MonsterDatabase.find(rs.getString("monster_4"));
						if(g1 != null)
							ms.getListGroup().add(new MonsterGroup(g1, rs.getInt("monster_1_count")));
						if(g2 != null)
							ms.getListGroup().add(new MonsterGroup(g2, rs.getInt("monster_2_count")));
						if(g3 != null)
							ms.getListGroup().add(new MonsterGroup(g3, rs.getInt("monster_3_count")));
						if(g4 != null)
							ms.getListGroup().add(new MonsterGroup(g4, rs.getInt("monster_4_count")));
					}
					
					toSpawnMonster(ms, null, false);
				}
			}
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}
	
	static public MonsterInstance find(long objid) {
		synchronized (list) {
			for (MonsterInstance m : list) {
				if (m.getObjectId() == objid)
					return m;
			}
		}
		return null;
	}
}
