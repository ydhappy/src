package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.World;
import lineage.world.object.object;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.BoardInstance;
import lineage.world.object.instance.RankBoardInstance;
import lineage.world.object.npc.background.Cracker;
import lineage.world.object.npc.background.Chaotic;
import lineage.world.object.npc.background.Lawful;
import lineage.world.object.npc.background.CrackerDmg;
import lineage.world.object.npc.background.PigeonGroup;
import lineage.world.object.npc.background.Sign;
import lineage.world.object.npc.background.Switch;
import lineage.world.object.npc.background.Torch;
import lineage.world.object.npc.background.TreasureBox;
import lineage.world.object.npc.background.산적의보물상자;
import lineage.world.object.npc.background.door.DogRaceDoor;
import lineage.world.object.npc.background.door.Door;
import lineage.world.object.npc.background.door.LastavardDoor;
import lineage.world.object.npc.quest.MotherOfTheForestAndElves;

public final class BackgroundDatabase {

	static private List<BackgroundInstance> pool;
	static private List<object> list;
	// 결투장 타일 리스트
	public static List<BackgroundInstance> list_battle_zone_tile;
	// 결투장 울타리 리스트
	public static List<BackgroundInstance> list_battle_zone_fence;
	static public BoardInstance tradeBoard;
	static public BoardInstance itemtradeBoard;
	static public BoardInstance rankBoard;
	static public BoardInstance serverguide;
	static public Object sync;

	static public void init(Connection con) {
		TimeLine.start("BackgroundDatabase..");

		if (pool == null) {
			pool = new ArrayList<BackgroundInstance>();
			list_battle_zone_tile = new ArrayList<BackgroundInstance>();
			list_battle_zone_fence = new ArrayList<BackgroundInstance>();
			list = new ArrayList<object>();
			sync = new Object();
		}
		pool.clear();
		synchronized (list) {
			list.clear();
		}
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM background_spawnlist");
			rs = st.executeQuery();
			while (rs.next()) {
				object o = toObject(rs.getString("nameid"), rs.getInt("gfx"), rs.getString("title"));
				o.setObjectId(ServerDatabase.nextNpcObjId());
				o.setName(rs.getString("nameid"));
				o.setGfx(rs.getInt("gfx"));
				o.setGfxMode(rs.getInt("gfx_mode"));
				o.setLawful(rs.getInt("lawful"));
				o.setClassGfx(o.getGfx());
				o.setClassGfxMode(o.getGfxMode());
				o.setLight(rs.getInt("light"));
				o.setHomeX(rs.getInt("locX"));
				o.setHomeY(rs.getInt("locY"));
				o.setHomeMap(rs.getInt("locMap"));
				o.setHomeLoc(rs.getInt("locSize"));
				o.setHeading(rs.getInt("heading"));
				o.toTeleport(o.getHomeX(), o.getHomeY(), o.getHomeMap(), false);

				if (o instanceof BoardInstance) {
					BoardInstance b = (BoardInstance) o;
					((BoardInstance) o).setType(rs.getString("title"));
					if ("heine".equals(rs.getString("title"))) {
						o.setTitle( "게시판" );
					}
					if ("rank".equals(rs.getString("title"))) {
						o.setTitle( "게시판" );
					}
					if ("aden".equals(rs.getString("title"))) {
						o.setTitle( "게시판" );
					}
					if ("quest".equals(rs.getString("title"))) {
						o.setTitle( "게시판" );
					}
					if ("giran".equals(rs.getString("title"))) {
						o.setTitle( "게시판" );
					}
					if ("silence".equals(rs.getString("title"))) {
						o.setTitle( "게시판" );
					}
					if (rs.getString("title").equalsIgnoreCase("silence"))
						itemtradeBoard = b;
					
					if (rs.getString("title").equalsIgnoreCase("giran"))
						tradeBoard = b;

					if (rs.getString("title").equalsIgnoreCase("rank"))
						rankBoard = b;
					
					if (rs.getString("title").equalsIgnoreCase("danbi"))
						serverguide = b;
				}

				else if (o instanceof Door)
					((Door) o).setKey(rs.getInt("item_nameid"), rs.getInt("item_count"),
							rs.getString("item_remove").equalsIgnoreCase("true"));
				else
					o.setTitle(rs.getString("title"));

				synchronized (list) {
					list.add(o);
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : void init(Connection con)\r\n", BackgroundDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		spawnBattleZone();
		TimeLine.end();
	}

	static public BoardInstance getServerBoard() {
		synchronized (sync) {
			return serverguide;
		}
	}

	static public BoardInstance getTradeBoard() {
		synchronized (sync) {
			return tradeBoard;
		}
	}
	
	static public BoardInstance getitemTradeBoard() {
		synchronized (sync) {
			return itemtradeBoard;
		}
	}

	static public BoardInstance getRankBoard() {
		synchronized (sync) {
			return rankBoard;
		}
	}

	static public void close() {
		synchronized (list) {
			for (object o : list) {
				World.remove(o);
				o.clearList(true);
				o.setAiStatus(-2);
			}
		}
	}

	static public object find(String type) {
		synchronized (list) {
			for (object o : list) {
				if (o != null && o instanceof BoardInstance) {
					BoardInstance b = (BoardInstance) o;
					if (b.getType().equalsIgnoreCase(type))
						return o;
				}
			}
		}
		return null;
	}

	/**
	 * gfx에 맞는 객체 만들어서 리턴.
	 * 
	 * @param gfx
	 * @param title
	 * @return
	 */
	static public object toObject(String nameid, int gfx, String title) {
		Object o = PluginController.init(BackgroundDatabase.class, "toObject", nameid, gfx, title);
		if (o != null)
			return (object) o;

		switch (gfx) {
		case 85:
		case 86:
		case 87:
		case 157:
		case 764:
		case 765:
			return new Torch();
	    case 62: // 슬경기장 문
	    case 63: // 슬경기장 문
		case 88: // [말섬던전2층] 문
		case 89: // [말섬던전2층] 문
		case 90: // [말섬던전1층] 문
		case 91: // [말섬던전1층] 문
		case 92: // [말섬던전2층] 문
		case 93: // [말섬던전2층] 은회색열쇠 문
		case 251:
		case 442: // [켄트마을] 문
		case 444: // [은기사마을] 문
		case 446: // [은기사마을] 문
		case 447: // [켄트마을] 문
		case 448: // [은기사마을] 문
		case 766: // [화민촌] 문
		case 767: // [화민촌] 문
		case 768: // [화민촌] 문
		case 845: // [은기사마을] 문
		case 1010: // [은기사마을] 문
		case 1327: // [기란] 문
		case 1328: // [기란] 문
		case 1330: // [기란] 문
		case 1331: // [기란] 문
		case 1332: // [기란] 문
		case 1333: // [기란] 문
		case 1334: // [기란] 문
		case 1335: // [기란] 문
		case 1341: // [기란] 문
		case 1342: // [기란] 문
		case 1347: // [기란] 문
		case 1348: // [기란] 문
		case 1349: // [기란] 문
		case 1350: // [기란] 문
		case 1351: // [기란] 문
		case 1352: // [기란] 문
		case 1371: // [기란] 문
		case 1372: // [기란] 문
		case 1373: // [기란] 문
		case 1664: // [하이네] 문
		case 1665: // [하이네] 문
		case 1688: //
		case 1689: // [하이네] 문
		case 1690: // [하이네] 문
		case 1691: // [하이네] 문
		case 1692: // [하이네] 문
		case 1700: // [하이네] 문
		case 1701: // [하이네] 문
		case 1734: // [하이네] 문
		case 1735: // [하이네] 문
		case 1738: // [하이네] 문
		case 1739: // [하이네] 문
		case 1740: // [하이네] 문
		case 1741: // [하이네] 문
		case 1742: // [하이네] 문
		case 1743: // [하이네] 문
		case 1744: // [하이네] 문
		case 1750: // [하이네] 문
		case 1751: // [하이네] 문
		case 2083: // [노래하는섬] 문, [숨겨진계곡] 문
		case 2089: // [숨겨진계곡] 문
		case 2114: // [숨겨진계곡] 문
		case 2128: // [숨겨진계곡] 문
		case 2136: // [노래하는섬] 문
		case 2160: // [오렌] 문
		case 2161: // [오렌] 문
		case 2162: // [오렌] 문
		case 2163: // [오렌] 문
		case 2164: // [오렌] 문
		case 2189: // [오렌] 문
		case 2190: // [오렌] 문
		case 2191: // [오렌] 문
		case 2192: // [오렌] 문
		case 2301: // [말섬] 게렝의집 문
		case 2303: // [말섬] 마을 문
		case 2304: // [말섬] 마을 문
		case 2305: // [말섬] 마을 문
		case 2344: // [말섬] 마을 문
		case 2345: // [말섬] 마을 문
		case 2346: // [말섬] 여관 문
		case 2547:
		case 2548:
		case 2554:
		case 2555:
		case 2556:
		case 2557:
		case 2558:
		case 2574:
		case 2575:
		case 2576:
		case 2577:
		case 2578:
		case 2579:
		case 2580:
		case 2581:
		case 2582:
		case 2583:
		case 2584:
		case 2585:
		case 2586:
		case 2587:
		case 2588:
		case 2589:
		case 2590:
		case 2591:
		case 2592:
		case 2593:
		case 2594:
		case 2595:
		case 2596:
		case 2597:
		case 2598:
		case 2599:
		case 2600:
		case 2603:
		case 2605:
		case 2606:
		case 2610:
		case 2628:
		case 2629:
		case 2630:
		case 2631:
		case 2634:
		case 2635:
		case 2682:
		case 6026:
		case 6027:
		case 6028:
		case 6029:
		case 6030:
		case 6031:
		case 6032:
		case 6033:
		case 6076: // 글루딘 아지트 뮨
		case 6077: // 글루딘 아지트 문
			return new Door();
		case 841: // 푯말
		case 1536:
		case 1537:
		case 1548:
			return new Sign();
		case 155:
			return new Chaotic();
		case 780:
			return new Lawful();
		case 5719:
		case 12548: // 성환.허수아비
			return new Cracker();
		case 114:
		case 116:
		case 2084: // [숨겨진계곡] 허수아비, [노래하는섬] 허수아비
			return new CrackerDmg();
		case 1094: // 비둘기
			return new PigeonGroup();
		case 1544:
		case 1546: // 마을 게시판.
		case 1552: // 경매 게시판.
		case 2205:
		case 2207: // 마을 게시판.
			if (title.equalsIgnoreCase("fish") || title.equalsIgnoreCase("cash"))
				return new Sign();
		case 2690:
		case 1557: // 마을 게시판.
		case 1559: // 마을 게시판.
		case 12648: // 마을 게시판.
			if (title.equalsIgnoreCase("rank"))
				return new RankBoardInstance();
			else
				return new BoardInstance();
		case 869: // 숲과 요정의 어머니
			return new MotherOfTheForestAndElves();
		case 125: // 스위치 (발판)
		case 126:
			return new Switch();
		case 122: // 보물상자
			return new TreasureBox();
		case 4331: // 산적의 보물 상자
			return new 산적의보물상자();
		case 1487: // 강아지 레이스 문
			return new DogRaceDoor();
		case 4710: // 라스타바드 문
		case 4711: // 라스타바드 문
		case 4712: // 라스타바드 문
		case 4713: // 라스타바드 문
		case 4732: // 라스타바드 문
		case 4733: // 라스타바드 문
		case 4734: // 라스타바드 문
		case 4735: // 라스타바드 문
			return new LastavardDoor();
		default:
			if (nameid != null) {
				// 하딘의 일기장 교환^마법문자
				if (nameid.equalsIgnoreCase("$9528"))
					return BackgroundInstance.clone(getPool(BackgroundInstance.class));
				// 오림의 일기장 교환^칠흑의 문자
				if (nameid.equalsIgnoreCase("$15709"))
					return BackgroundInstance.clone(getPool(BackgroundInstance.class));
			}
			//
			return BackgroundInstance.clone(getPool(BackgroundInstance.class));
		}
	}

	static public BackgroundInstance getPool(Class<?> c) {
		//
		BackgroundInstance b = null;
		if (Lineage.memory_recycle) {
			for (BackgroundInstance bi : pool) {
				if (bi.getClass().toString().equals(c.toString())) {
					b = bi;
					break;
				}
			}
			if (b != null) {
				pool.remove(b);
			}
		}
		return b;
	}

	static public void setPool(BackgroundInstance bi) {
		bi.close();
		if (!Lineage.memory_recycle)
			return;
		synchronized (pool) {
			if (!pool.contains(bi))
				pool.add(bi);
		}
	}

	static public int getPoolSize() {
		return pool.size();
	}
	/**
	 * 배틀존 스폰
	 * 2019-06-19
	 * by connector12@nate.com
	 */
	static public void spawnBattleZone() {
		int count = ((Lineage.battle_zone_x2 - Lineage.battle_zone_x1) + 1) * ((Lineage.battle_zone_y2 - Lineage.battle_zone_y1) + 1);

		// 타일 생성
		for (int i = 0; i < count; i++)
			list_battle_zone_tile.add(new lineage.world.object.npc.background.BackgroundTile());
		
		// 울타리 생성
		for (int i = 0; i < 12; i++)
			list_battle_zone_fence.add(new lineage.world.object.npc.background.BattleRoyalTeamLine());

		//int x = Lineage.battle_zone_x1;
		//int y = Lineage.battle_zone_y1;
		
		// 타일 스폰
		/*for (BackgroundInstance tile : list_battle_zone_tile) {	
			if (x > Lineage.battle_zone_x2) {
				x = Lineage.battle_zone_x1;
				y++;
			}
			
			tile.setGfx(441);
			tile.setObjectId(ServerDatabase.nextEtcObjId());
			tile.toTeleport(x, y, Lineage.battle_zone_map, false);
			x++;
		}*/
		
		// 울타리 스폰
		int idx = 0;
		int x1 = Lineage.battle_zone_x1;
		int y1 = Lineage.battle_zone_y1;
		int x2 = Lineage.battle_zone_x2;
		int y2 = Lineage.battle_zone_y2;
		
		// 모서리 1
		list_battle_zone_fence.get(idx).setGfx(11204);
		list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
		list_battle_zone_fence.get(idx).toTeleport(x1, y1 - 1, Lineage.battle_zone_map, false);
		idx++;
		
		list_battle_zone_fence.get(idx).setGfx(11204);
		list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
		list_battle_zone_fence.get(idx).toTeleport(x1 - 1, y1 - 1, Lineage.battle_zone_map, false);
		idx++;
		
		list_battle_zone_fence.get(idx).setGfx(11204);
		list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
		list_battle_zone_fence.get(idx).toTeleport(x1 - 1, y1, Lineage.battle_zone_map, false);
		idx++;
		
		// 모서리 2
		list_battle_zone_fence.get(idx).setGfx(11204);
		list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
		list_battle_zone_fence.get(idx).toTeleport(x2, y1 - 1, Lineage.battle_zone_map, false);
		idx++;
		
		list_battle_zone_fence.get(idx).setGfx(11204);
		list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
		list_battle_zone_fence.get(idx).toTeleport(x2 + 1, y1 - 1, Lineage.battle_zone_map, false);
		idx++;
		
		list_battle_zone_fence.get(idx).setGfx(11204);
		list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
		list_battle_zone_fence.get(idx).toTeleport(x2 + 1, y1, Lineage.battle_zone_map, false);
		idx++;
		
		// 모서리 3
		list_battle_zone_fence.get(idx).setGfx(11204);
		list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
		list_battle_zone_fence.get(idx).toTeleport(x2 + 1, y2, Lineage.battle_zone_map, false);
		idx++;
		
		list_battle_zone_fence.get(idx).setGfx(11204);
		list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
		list_battle_zone_fence.get(idx).toTeleport(x2 + 1, y2 + 1, Lineage.battle_zone_map, false);
		idx++;
		
		list_battle_zone_fence.get(idx).setGfx(11204);
		list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
		list_battle_zone_fence.get(idx).toTeleport(x2, y2 + 1, Lineage.battle_zone_map, false);
		idx++;
		
		// 모서리 4
		list_battle_zone_fence.get(idx).setGfx(11204);
		list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
		list_battle_zone_fence.get(idx).toTeleport(x1 - 1, y2, Lineage.battle_zone_map, false);
		idx++;
		
		list_battle_zone_fence.get(idx).setGfx(11204);
		list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
		list_battle_zone_fence.get(idx).toTeleport(x1 - 1, y2 + 1, Lineage.battle_zone_map, false);
		idx++;
		
		list_battle_zone_fence.get(idx).setGfx(11204);
		list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
		list_battle_zone_fence.get(idx).toTeleport(x1, y2 + 1, Lineage.battle_zone_map, false);
	}
	
	/**
	 * 배틀존 스폰 리로드
	 * 2019-06-19
	 * by connector12@nate.com
	 */
	static public void reloadSpawnBattleZone() {
		if (list_battle_zone_tile != null && list_battle_zone_fence != null) {
			for (BackgroundInstance tile : list_battle_zone_tile) {
				tile.clearList(true);
				World.remove(tile);
			}
			
			for (BackgroundInstance fence : list_battle_zone_fence) {
				fence.clearList(true);
				World.remove(fence);
			}
			
			list_battle_zone_tile.clear();
			list_battle_zone_fence.clear();
			
			int count = ((Lineage.battle_zone_x2 - Lineage.battle_zone_x1) + 1) * ((Lineage.battle_zone_y2 - Lineage.battle_zone_y1) + 1);
			
			// 타일 생성
			for (int i = 0; i < count; i++)
				list_battle_zone_tile.add(new lineage.world.object.npc.background.BackgroundTile());
			
			// 울타리 생성
			for (int i = 0; i < 12; i++)
				list_battle_zone_fence.add(new lineage.world.object.npc.background.BattleRoyalTeamLine());

			//int x = Lineage.battle_zone_x1;
			//int y = Lineage.battle_zone_y1;
			
			// 타일 스폰
			/*for (BackgroundInstance tile : list_battle_zone_tile) {	
				if (x > Lineage.battle_zone_x2) {
					x = Lineage.battle_zone_x1;
					y++;
				}
				
				tile.setGfx(441);
				tile.setObjectId(ServerDatabase.nextEtcObjId());
				tile.toTeleport(x, y, Lineage.battle_zone_map, false);
				x++;
			}*/
			
			// 울타리 스폰
			int idx = 0;
			int x1 = Lineage.battle_zone_x1;
			int y1 = Lineage.battle_zone_y1;
			int x2 = Lineage.battle_zone_x2;
			int y2 = Lineage.battle_zone_y2;
			
			// 모서리 1
			list_battle_zone_fence.get(idx).setGfx(11204);
			list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
			list_battle_zone_fence.get(idx).toTeleport(x1, y1 - 1, Lineage.battle_zone_map, false);
			idx++;
			
			list_battle_zone_fence.get(idx).setGfx(11204);
			list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
			list_battle_zone_fence.get(idx).toTeleport(x1 - 1, y1 - 1, Lineage.battle_zone_map, false);
			idx++;
			
			list_battle_zone_fence.get(idx).setGfx(11204);
			list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
			list_battle_zone_fence.get(idx).toTeleport(x1 - 1, y1, Lineage.battle_zone_map, false);
			idx++;
			
			// 모서리 2
			list_battle_zone_fence.get(idx).setGfx(11204);
			list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
			list_battle_zone_fence.get(idx).toTeleport(x2, y1 - 1, Lineage.battle_zone_map, false);
			idx++;
			
			list_battle_zone_fence.get(idx).setGfx(11204);
			list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
			list_battle_zone_fence.get(idx).toTeleport(x2 + 1, y1 - 1, Lineage.battle_zone_map, false);
			idx++;
			
			list_battle_zone_fence.get(idx).setGfx(11204);
			list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
			list_battle_zone_fence.get(idx).toTeleport(x2 + 1, y1, Lineage.battle_zone_map, false);
			idx++;
			
			// 모서리 3
			list_battle_zone_fence.get(idx).setGfx(11204);
			list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
			list_battle_zone_fence.get(idx).toTeleport(x2 + 1, y2, Lineage.battle_zone_map, false);
			idx++;
			
			list_battle_zone_fence.get(idx).setGfx(11204);
			list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
			list_battle_zone_fence.get(idx).toTeleport(x2 + 1, y2 + 1, Lineage.battle_zone_map, false);
			idx++;
			
			list_battle_zone_fence.get(idx).setGfx(11204);
			list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
			list_battle_zone_fence.get(idx).toTeleport(x2, y2 + 1, Lineage.battle_zone_map, false);
			idx++;
			
			// 모서리 4
			list_battle_zone_fence.get(idx).setGfx(11204);
			list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
			list_battle_zone_fence.get(idx).toTeleport(x1 - 1, y2, Lineage.battle_zone_map, false);
			idx++;
			
			list_battle_zone_fence.get(idx).setGfx(11204);
			list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
			list_battle_zone_fence.get(idx).toTeleport(x1 - 1, y2 + 1, Lineage.battle_zone_map, false);
			idx++;
			
			list_battle_zone_fence.get(idx).setGfx(11204);
			list_battle_zone_fence.get(idx).setObjectId(ServerDatabase.nextEtcObjId());
			list_battle_zone_fence.get(idx).toTeleport(x1, y2 + 1, Lineage.battle_zone_map, false);
		}
	}
	/**
	 * 타이머가 주기적으로 호출함.
	 * 
	 * @param time
	 */
	static public void toTimer(long time) {
		synchronized (list) {
			boolean isNight = ServerDatabase.isNight();
			for (object o : list) {
				// 횟불일경우 밤낮을 구분해서 스폰처리 하기.
				if (o instanceof Torch) {
					if (o.getMap() == 0 || o.getMap() == 4) {
						if (o.isWorldDelete()) {
							if (isNight)
								o.toTeleport(o.getHomeX(), o.getHomeY(), o.getHomeMap(), false);
						} else {
							if (!isNight) {
								World.remove(o);
								o.clearList(true);
							}
						}
					}
				}
			}
		}
	}

}
