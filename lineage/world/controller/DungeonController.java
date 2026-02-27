package lineage.world.controller;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.database.Dungeon;
import lineage.bean.database.DungeonPartTime;
import lineage.database.DungeonDatabase;
import lineage.database.DungeonPartTimeDatabase;
import lineage.network.packet.server.S_DungeonTimeCount;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;


public final class DungeonController {

	// 사용자 별로 관리될 기억리스트 목록
	static private Map<PcInstance, List<DungeonPartTime>> list;
	// 클레스 재사용을 위한것
	static private List<DungeonPartTime> pool;

	static public void init() {
		TimeLine.start("DungeonController..");

		list = new HashMap<PcInstance, List<DungeonPartTime>>();
		pool = new ArrayList<DungeonPartTime>();

		TimeLine.end();
	}

	/**
	 * 월드 접속시 호출.
	 * 
	 * @param pc
	 */
	static public void toWorldJoin(PcInstance pc) {
		// 생성된게 없을경우 생성해서 관리목록에 추가.
		synchronized (list) {
			if (!list.containsKey(pc))
				list.put(pc, new ArrayList<DungeonPartTime>());
		}
	}

	/**
	 * 월드에서 나가면 호출.
	 * 
	 * @param pc
	 */
	static public void toWorldOut(PcInstance pc) {
		// 관리되고있는 자료구조 찾기.
		List<DungeonPartTime> dungeon_list = find(pc);
		if (dungeon_list != null) {
			// 순회하면서 풀에 다시 넣기.
			synchronized (dungeon_list) {
				for (DungeonPartTime d : dungeon_list) {
					setPool(d);
				}
				// 메모리에서 제거.
				dungeon_list.clear();
				dungeon_list = null;
			}
		}
		synchronized (list) {
			list.remove(pc);
		}
	}

	/**
	 * 주기적으로 호출됨.
	 * 
	 * @param time
	 */
	static public void toTimer(long time) {
		//
		PluginController.init(DungeonController.class, "toTimer", time);
		//
		for (PcInstance pc : World.getPcList()) {
			List<DungeonPartTime> list = find(pc);
			if (list == null)
				continue;
			synchronized (list) {
				for (DungeonPartTime d : list) {
					DungeonPartTime dpt = DungeonPartTimeDatabase.findUid(d.getUid());
					if (dpt == null)
						continue;
					// 사용자가 시간제던전에 있을떄만 처리.
					if (dpt.getList().contains(pc.getMap())) {

						boolean gamso = true;
						int me_day = Util.getDate(d.getUpdateTime());
						int to_day = Util.getDate(time);
						if (me_day != to_day) {
							// 리셋시간값이 있을경우 그걸 기준으로 처리.
							if(dpt.getTimeReset().length() > 0) {
								long time_reset = Util.getTime( Util.getLocaleString(time, false) + " " + dpt.getTimeReset() );
								if(time_reset <= System.currentTimeMillis()) {
									// 일자가 변경되고 기준시간이 초과되면 마을로 귀환. 던전 진입에 시도하면 자동으로 충전됨.
									LocationController.toHome(pc);
									pc.toTeleport(pc.getHomeX(), pc.getHomeY(), pc.getHomeMap(), true);
									if(dpt.getMessageOut()!=null && dpt.getMessageOut().length()>0)
										ChattingController.toChatting(pc, dpt.getMessageOut(), Lineage.CHATTING_MODE_MESSAGE);
									gamso = false;
								} else {
									// 기준시간전이고 아직 시간이 남은 상태이므로 시간을 계속 착감해야함.
								}
							} else {
								// 일자가 변경되면 마을로 귀환. 던전 진입에 시도하면 자동으로 충전됨.
								LocationController.toHome(pc);
								pc.toTeleport(pc.getHomeX(), pc.getHomeY(), pc.getHomeMap(), true);
								gamso = false;
							}
						}
						
						if(gamso) {
							//
							boolean isOut = d.getTime() <= 0;
							// seek부분이 잡혀있다면 그 시간을 기준으로 확인.
							if (dpt.getTimeSeek().size() > 0) {
								// 사용자를 해당맵에서 퇴출해야되는 로직.
								boolean check = true;
								for(String ts : dpt.getTimeSeek()) {
									String s_datetime_str = String.format("%s %s", Util.getLocaleString(time, false), ts);
									long s_time = Util.getTime(s_datetime_str);
									long e_time = s_time + (dpt.getTime() * 1000);
									if(s_time <= time && e_time >= time)
										check = false;
								}
								isOut = check;
							}
							// 시간다됫을때 마을로 귀환처리.
							if (isOut) {
								LocationController.toHome(pc);
								pc.toTeleport(pc.getHomeX(), pc.getHomeY(), pc.getHomeMap(), true);
								if(dpt.getMessageOut()!=null && dpt.getMessageOut().length()>0)
									ChattingController.toChatting(pc, dpt.getMessageOut(), Lineage.CHATTING_MODE_MESSAGE);
								continue;
							}
							// 시간 감소.
							d.setTime(d.getTime() - 1);
							if(Lineage.server_version >= 360)
								pc.toSender(new S_DungeonTimeCount( d.getTime()));
							// 몇분 남앗는지 표현.
							if (Lineage.dungeon_parttime_message && d.getTime() >= 1200) {
								if (d.getTime() % 1200 == 0) {
									// int h = ((d.getTime()/60)/60);
									// int m = (d.getTime()/60) - (h*60);
									// ChattingController.toChatting(pc,
									// String.format("던전 이용시간이 %d시간 %d분 남았습니다",
									// h, m), Lineage.CHATTING_MODE_MESSAGE);
									Time t = new Time(d.getTime() * 1000);
									t.setTime(t.getTime() - (1000 * 60 * 60 * 9));
									ChattingController.toChatting(pc, String.format("해당 맵 이용시간이 %s 남았습니다.", t.toString()), Lineage.CHATTING_MODE_MESSAGE);
								}
							}
						}
						
					}
				}
			}
		}
	}

	/**
	 * 풀에 사용한 Book클레스 넣는 함수.
	 * 
	 * @param b
	 */
	static public void setPool(DungeonPartTime d) {
		d.close();
		if (!Lineage.memory_recycle)
			return;
		synchronized (pool) {
			if (!pool.contains(d))
				pool.add(d);
		}
	}

	static private DungeonPartTime getPool() {
		DungeonPartTime dpt = null;
		synchronized (pool) {
			if (Lineage.memory_recycle && pool.size() > 0) {
				dpt = pool.get(0);
				pool.remove(0);
			} else {
				dpt = new DungeonPartTime();
			}
		}
		return dpt;
	}

	/**
	 * 사용자와 연결된 객체 찾아서 리턴.
	 * 
	 * @param pc
	 * @return
	 */
	static public List<DungeonPartTime> find(PcInstance pc) {
		synchronized (list) {
			return list.get(pc);
		}
	}

	/**
	 * 던전 고유값으로 객체 찾기.
	 * 
	 * @param pc
	 * @param uid
	 * @return
	 */
	static private DungeonPartTime find(List<DungeonPartTime> list, int uid) {
		if (list == null)
			return null;
		synchronized (list) {
			for (DungeonPartTime d : list) {
				if (d.getUid() == uid)
					return d;
			}
			return null;
		}
	}

	/**
	 * @param pc
	 * @param uid
	 * @return
	 */
	static public DungeonPartTime find(PcInstance pc, int uid) {
		return find(find(pc), uid);
	}

	static private DungeonPartTime find(List<DungeonPartTime> list, String name) {
		if (list == null)
			return null;
		synchronized (list) {
			for (DungeonPartTime d : list) {
				if (d.getName().equalsIgnoreCase(name))
					return d;
			}
			return null;
		}
	}

	static public DungeonPartTime find(PcInstance pc, String name) {
		return find(find(pc), name);
	}

	/**
	 * @param pc
	 * @param d
	 */
	static public void append(PcInstance pc, DungeonPartTime d) {
		List<DungeonPartTime> list = find(pc);
		synchronized (list) {
			list.add(d);
		}
	}
	
	static public boolean isDungeon(object o) {
		return isDungeon(o.getX(), o.getY(), o.getMap());
	}
	
	/**
	 * 던전 입출구 인지 확인하는 메서드.
	 * @param x
	 * @param y
	 * @param map
	 * @return
	 */
	static public boolean isDungeon(int x, int y, int map) {
		return DungeonDatabase.find(x, y, map) != null;
	}

	/**
	 * pcinstance들이 이동하다가 
	 * isDungeon 에서 true로 확인되면 뒷처리하는 메서드.
	 * 함.
	 */
	static public void toDungeon(object o) {
		toDungeon(o, o.getX(), o.getY(), o.getMap());
	}
	
	static public void toDungeon(object o, int x, int y, int map) {
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			Dungeon d = DungeonDatabase.find(x, y, map);
			if (d != null) {
				List<ItemInstance> search_list = new ArrayList<ItemInstance>();
				// 성공적으로 텔레포트를 이행할지를 판단용으로 사용하는 변수.
				boolean tel = true;
				// 아이템이 필요한 던전일경우 확인하기.
				if (d.getItemCount() > 0 && o instanceof PcInstance) {
					// 인벤에 존재하는지 확인해야하므로 false로 기본 설정.
					tel = false;
					// 인벤에 가지고있는 아이템목록에서 해당하는 아이템 전체 불러오기.
					o.getInventory().findDbNameId(d.getItemNameid(), search_list);
					if (d.getItemNameid() == 954) {
						// 여관키 일경우 전체 순회하기
						// for( ItemInstance item : search_list ){
						// 해당 여관키와 연결된 현재 방이 존재하는지 확인.
						// 근데 어떤 여관인지 확인도 해야되네.. 좌표 확인해야할듯.
						// tel = InnController::isRoom(o, *p);
						// if(tel)
						// break;
						// }
					} else {
						// 그외에는 1개 이상일경우 무조건 이동.
						tel = search_list.size() > 0;
					}
				}
				// 플러그인 확인.
				if (PluginController.init(DungeonController.class, "toDungeon", d, o) != null)
					return;
				// 시간제 던전 확인.
				if (tel == false)
					tel = toDungeonPartTime(o, d.getGotoM(), true);

				if (tel)
					o.toPotal(d.getGotoX(), d.getGotoY(), d.getGotoM());
			}
		}
	}

	/**
	 * 시간제 던전 이용 처리 메서드.
	 * 
	 * @param o
	 */
	static public boolean toDungeonPartTime(object o, int mapId, boolean message) {
		boolean tel = false;
		boolean seek_tel = true;
		// 시간제 던전 확인.
		DungeonPartTime dpt = DungeonPartTimeDatabase.find(mapId);
		if (dpt != null && o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			List<DungeonPartTime> list = find(pc);
			if (list != null) {
				// seek부분이 잡혀있다면 그 시간을 기준으로 확인.
				if (dpt.getTimeSeek().size() > 0) {
					// 시간이 존재한다는건 기본적으로 시간을 체크할 시간이 존재한다는것
					// 그렇기에 시작은 접근을 제한해야한다.
					// 정해진 시간에만 이용해야한다.
					boolean check = false;
					for(String ts : dpt.getTimeSeek()) {
						long time = System.currentTimeMillis();
						String s_datetime_str = String.format("%s %s", Util.getLocaleString(time, false), ts);
						long s_time = Util.getTime(s_datetime_str);
						long e_time = s_time + (dpt.getTime() * 1000);
//						String e_datetime_str = String.format("%s %s", Util.getLocaleString(e_time, false), Util.getLocaleString(e_time));
//						System.out.println( Util.getLocaleString(time) +" >> "+ Util.getLocaleString(s_time) +" ~ "+ Util.getLocaleString(e_time) );
						// 정해진 시간에 오픈되는 시간이라면 이동도되도록 유도하기.
						if (s_time <= time && e_time >= time)
							tel = check = true;
					}
					seek_tel = check;
				}

				if (seek_tel) {
					DungeonPartTime dungeon = find(list, dpt.getUid());
					if (dungeon == null) {
						// 관리목록에 존재하지 않다면 성공.
						tel = true;
						// 추가.
						dungeon = getPool();
						dungeon.setName(dpt.getName());
						dungeon.setUid(dpt.getUid());
						dungeon.setTime(dpt.getTime());
						dungeon.setUpdateTime(System.currentTimeMillis());
						synchronized (list) {
							list.add(dungeon);
						}
					} else if (dungeon != null) {
						// 던전을 최초로 이용했던 시간에 일자 추출.
						int me_day = Util.getDate(dungeon.getUpdateTime());
						// 현재 시간에 일자 추출.
						int to_day = Util.getDate(System.currentTimeMillis());
						// 최초일자와 현재 일자가 다르면 시간 다시 지급하기.
						if (me_day != to_day) {
							// 리셋시간값이 있을경우 그걸 기준으로 처리.
							if(dpt.getTimeReset().length() > 0) {
								long time_reset = Util.getTime( Util.getLocaleString(System.currentTimeMillis(), false) + " " + dpt.getTimeReset() );
								if(time_reset <= System.currentTimeMillis()) {
									tel = true;
									dungeon.setTime(dpt.getTime());
									dungeon.setUpdateTime(System.currentTimeMillis());
								}
								
							} else {
								tel = true;
								dungeon.setTime(dpt.getTime());
								dungeon.setUpdateTime(System.currentTimeMillis());
							}
						}
						// 최근에 이용한 것중 시간이 유효하다면 성공.
						if (dungeon.getTime() > 0)
							tel = true;
					}
				}
			}
			// 멘트 표현.
			if (tel == false) {
				if (seek_tel)
					ChattingController.toChatting(o, "해당 맵 시간을 모두 사용하였습니다.\n\r"
							+ "06:00시 기준 초기화됩니다.", Lineage.CHATTING_MODE_MESSAGE);
				else
					ChattingController.toChatting(o, "현재 해당 맵을 이용하실 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
		return tel;
	}

	/**
	 * 이동하려는 맵이 던전파트타임 맵인지 확인해주는 함수.
	 * 
	 * @param o
	 * @param mapId
	 * @return
	 */
	static public boolean isDungeonPartTime(object o, int mapId) {
		DungeonPartTime dpt = DungeonPartTimeDatabase.find(mapId);
		if (dpt != null)
			return true;
		return false;
	}

}
