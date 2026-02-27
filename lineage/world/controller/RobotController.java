package lineage.world.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import lineage.bean.database.Exp;
import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.database.RobotDrop;
import lineage.bean.database.Shop;
import lineage.bean.database.Skill;
import lineage.bean.database.SkillRobot;
import lineage.bean.lineage.Book;
import lineage.bean.lineage.Clan;
import lineage.database.DatabaseConnection;
import lineage.database.ExpDatabase;
import lineage.database.ItemDatabase;
import lineage.database.MagicdollListDatabase;
import lineage.database.NpcDatabase;
import lineage.database.ServerDatabase;
import lineage.database.SkillDatabase;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.RobotInstance;
import lineage.world.object.robot.BuffRobotInstance;
import lineage.world.object.robot.BuffRobotInstance1;
import lineage.world.object.robot.BuffRobotInstance2;
import lineage.world.object.robot.BuffRobotInstance3;
import lineage.world.object.robot.BuffRobotInstance4;
import lineage.world.object.robot.CrackerRobotInstance;
import lineage.world.object.robot.HasteRobotInstance;
import lineage.world.object.robot.PcRobotInstance;
import lineage.world.object.robot.PcRobotShopInstance;
import lineage.world.object.robot.WizardZoneRobotInstance;

public class RobotController {

	// 기란
	static private List<BuffRobotInstance> list_buff;
	// 화말
	static private List<BuffRobotInstance1> list_buff1;
	// 글말
	static private List<BuffRobotInstance2> list_buff2;
	// 은말
	// static private List<BuffRobotInstance3> list_buff3;
	// 말섬
	// static private List<BuffRobotInstance4> list_buff4;
	// 무인케릭 관리 목록.
	static private List<PcRobotInstance> list_pc;
	// 상점 npc 목록. (로봇이 아이템구입시 상인을 찾을때 사용됨.)
	static private List<Npc> list_shop;
	//
	private static Map<Long, Clan> list_clan;
	// 로봇이 드랍하게될 아이템 목록.
	private static Map<String, List<RobotDrop>> list_drop;
	// 스폰된 로봇 개체수
	static public int count;
	// 기란꺼.
	static private int royal_location_list[][];
	static private int knight_location_list[][];
	static private int elf_location_list[][];
	static private int wizard_location_list[][];
	static private int random_location_list[][];

	static public void init() {
		TimeLine.start("RobotController..");

		//
		// list_BattleZone = new ArrayList<BattleZoneRobotInstance>();
		list_shop = new ArrayList<Npc>();
		// 자동버프 객체 초기화.
		list_buff = new ArrayList<BuffRobotInstance>();
		list_buff1 = new ArrayList<BuffRobotInstance1>();
		list_buff2 = new ArrayList<BuffRobotInstance2>();
		// list_buff3 = new ArrayList<BuffRobotInstance3>();
		// list_buff4 = new ArrayList<BuffRobotInstance4>();
		list_pc = new ArrayList<PcRobotInstance>();
		list_clan = new HashMap<Long, Clan>();
		list_drop = new HashMap<String, List<RobotDrop>>();
		royal_location_list = new int[][] {
				{ 33631, 32678, 4, 5 }
		};
		knight_location_list = new int[][] {
				{ 33632, 32671, 4, 0 },
				{ 33631, 32671, 4, 0 },
				{ 33632, 32672, 4, 0 },
				{ 33631, 32672, 4, 0 },
				{ 33638, 32677, 4, 2 },
				{ 33638, 32678, 4, 2 },
				{ 33637, 32677, 4, 2 },
				{ 33637, 32678, 4, 2 },
				{ 33632, 32684, 4, 4 },
				{ 33632, 32683, 4, 4 },
				{ 33631, 32683, 4, 4 },
				{ 33631, 32684, 4, 4 },
				{ 33625, 32677, 4, 6 },
				{ 33626, 32677, 4, 6 },
				{ 33625, 32678, 4, 6 },
				{ 33626, 32678, 4, 6 }
		};
		elf_location_list = new int[][] {
				{ 33632, 32673, 4, 0 },
				{ 33632, 32674, 4, 0 },
				{ 33632, 32675, 4, 0 },
				{ 33632, 32680, 4, 4 },
				{ 33632, 32681, 4, 4 },
				{ 33632, 32682, 4, 4 },
				{ 33631, 32673, 4, 0 },
				{ 33631, 32674, 4, 0 },
				{ 33631, 32675, 4, 0 },
				{ 33631, 32680, 4, 4 },
				{ 33631, 32681, 4, 4 },
				{ 33631, 32682, 4, 4 },
				{ 33627, 32677, 4, 6 },
				{ 33628, 32677, 4, 6 },
				{ 33629, 32677, 4, 6 },
				{ 33634, 32677, 4, 2 },
				{ 33635, 32677, 4, 2 },
				{ 33636, 32677, 4, 2 },
				{ 33627, 32678, 4, 6 },
				{ 33628, 32678, 4, 6 },
				{ 33629, 32678, 4, 6 },
				{ 33634, 32678, 4, 2 },
				{ 33635, 32678, 4, 2 },
				{ 33636, 32678, 4, 2 }
		};
		wizard_location_list = new int[][] {
				{ 33629, 32675, 4, 0 },
				{ 33629, 32680, 4, 0 },
				{ 33634, 32680, 4, 0 },
				{ 33634, 32675, 4, 0 }
		};
		random_location_list = new int[][] {
				{ 33619, 32666, 4 },
				{ 33619, 32690, 4 },
				{ 33644, 32665, 4 },
				{ 33643, 32689, 4 },
				{ 33644, 32677, 4 },
				{ 33632, 32690, 4 },
				{ 33619, 32677, 4 },
				{ 33595, 32677, 4 },
				{ 33631, 32713, 4 },
				{ 33666, 32677, 4 },
		};
		// 상점 npc 찾기.
		for (Npc npc : NpcDatabase.getList()) {
			if (npc.getType().equalsIgnoreCase("Shop")) {
				synchronized (list_shop) {
					list_shop.add(npc);
				}
			}
		}

		if (Lineage.robot_auto_buff) {
			readBuffRobot();
			toStartBuff();
		}
		if (Lineage.robot_auto_pc) {
			readClan();
			readClanPvp();
			readDrop();
			readPcRobot();
		}

		// readBattleZoneRobot();
		// toStartBattleZone();

		//
		// PluginController.init(RobotController.class, "init", list_pc, list_buff,
		// list_buff1, list_buff2, list_buff3, list_buff4);
		PluginController.init(RobotController.class, "init", list_pc, list_buff, list_buff1, list_buff2);

		if (Lineage.robot_auto_pc) {
			toStartPc();
		}

		TimeLine.end();
	}

	static public void close() {
		//
		PluginController.init(RobotController.class, "close");
		//
		synchronized (list_shop) {
			list_shop.clear();
		}
		if (Lineage.robot_auto_buff) {
			toStopBuff();
		}
		if (Lineage.robot_auto_pc) {
			synchronized (list_clan) {
				list_clan.clear();
			}
			synchronized (list_drop) {
				list_drop.clear();
			}
			toStopPc();
		}
	}

	public static List<PcRobotInstance> getPc() {
		synchronized (list_pc) {
			return new ArrayList<PcRobotInstance>(list_pc);
		}
	}

	static public void toTimer(long time) {
		synchronized (list_buff) {
			for (RobotInstance bi : list_buff)
				bi.toTimer(time);
		}
		synchronized (list_buff1) {
			for (RobotInstance bi : list_buff1)
				bi.toTimer(time);
		}
		synchronized (list_buff2) {
			for (RobotInstance bi : list_buff2)
				bi.toTimer(time);
		}
		// synchronized (list_buff3) {
		// for(RobotInstance bi : list_buff3)
		// bi.toTimer(time);
		// }
		// synchronized (list_buff4) {
		// for(RobotInstance bi : list_buff4)
		// bi.toTimer(time);
		// }
		synchronized (list_pc) {
			for (RobotInstance bi : list_pc)
				bi.toTimer(time);
		}
	}

	/**
	 * 자동 버프사 시작 처리 함수.
	 */
	static public void toStartBuff() {
		synchronized (list_buff) {
			for (RobotInstance bi : list_buff)
				bi.toWorldJoin();
			count += list_buff.size();

		}
		synchronized (list_buff1) {
			for (RobotInstance bi : list_buff1)
				bi.toWorldJoin();
			count += list_buff1.size();

		}
		synchronized (list_buff2) {
			for (RobotInstance bi : list_buff2)
				bi.toWorldJoin();
			count += list_buff2.size();

		}
		// synchronized (list_buff3) {
		// for(RobotInstance bi : list_buff3)
		// bi.toWorldJoin();
		// count += list_buff3.size();
		//
		// }
		// synchronized (list_buff4) {
		// for(RobotInstance bi : list_buff4)
		// bi.toWorldJoin();
		// count += list_buff4.size();

		// }
	}

	/**
	 * 자동 버프사 종료 처리 함수.
	 */
	static public void toStopBuff() {
		synchronized (list_buff) {
			for (RobotInstance bi : list_buff)
				bi.toWorldOut();
			count -= list_buff.size();
		}
		synchronized (list_buff1) {
			for (RobotInstance bi : list_buff1)
				bi.toWorldOut();
			count -= list_buff1.size();
		}
		synchronized (list_buff2) {
			for (RobotInstance bi : list_buff2)
				bi.toWorldOut();
			count -= list_buff2.size();
		}
		// synchronized (list_buff3) {
		// for(RobotInstance bi : list_buff3)
		// bi.toWorldOut();
		// count -= list_buff3.size();
		// }
		// synchronized (list_buff4) {
		// for(RobotInstance bi : list_buff4)
		// bi.toWorldOut();
		// count -= list_buff4.size();
		// }
	}

	/**
	 * 무인 케릭 시작 처리 함수.
	 */
	static public void toStartPc() {
		synchronized (list_pc) {
			for (PcRobotInstance pri : list_pc)
				pri.toWorldJoin();
			count += list_pc.size();
		}
	}

	/**
	 * 무인 케릭 종료 처리 함수.
	 */
	static public void toStopPc() {
		synchronized (list_pc) {
			for (PcRobotInstance pri : list_pc)
				pri.toWorldOut();
			count -= list_pc.size();
			list_pc.clear();
		}
	}

	// static public void toStartBattleZone(){
	// synchronized (list_BattleZone){
	// for(RobotInstance bi : list_BattleZone)
	// bi.toWorldJoin();
	// count += list_BattleZone.size();
	// }
	// }
	// static public void toStopBattleZone(){
	// synchronized (list_BattleZone){
	// for(RobotInstance bi : list_BattleZone)
	// bi.toWorldOut();
	// count -= list_BattleZone.size();
	// }
	// }
	/**
	 * 월드 아웃 처리 메서드.
	 * 
	 * @param pri
	 */
	static public void toWorldOut(PcRobotInstance pri) {
		pri.toWorldOut();
		synchronized (list_pc) {
			list_pc.remove(pri);
			count -= 1;
		}
	}

	/**
	 * 월드 접속 처리 메서드.
	 * : 로봇 기본변수들 세팅다 한 후 호출해야 정상 처리됨.
	 * 
	 * @param pri
	 */
	static public void toWorldJoin(PcRobotInstance pri) {
		synchronized (list_pc) {
			list_pc.add(pri);
			count += 1;
		}
		pri.toWorldJoin();
	}

	/**
	 * 구매하려는 아이템을 판매하는 상점 찾기.
	 * 
	 * @param item_name
	 * @return
	 */
	public static int[] findShop(String item_name) {
		synchronized (list_shop) {
			List<Npc> temp = new ArrayList<Npc>();
			for (Npc n : list_shop) {
				for (Shop s : n.getShop_list()) {
					if (s.getItemName().equalsIgnoreCase(item_name)) {
						boolean isAppend = false;
						for (int[] location : n.getSpawnList()) {
							// 말섬과 본토지역 상인에게만 구입하기 위하여.
							if (location[2] != 0 && location[2] != 4)
								continue;
							isAppend = true;
							break;
						}
						if (isAppend)
							temp.add(n);
						break;
					}
				}
			}
			if (temp.size() > 0) {
				Npc n = temp.get(Util.random(0, temp.size() - 1));
				temp.clear();
				return n.getSpawnList().get(Util.random(0, n.getSpawnList().size() - 1));
			}
			return null;
		}
	}

	/**
	 * 로봇 이름 존재여부리턴.
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isName(String name) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM _robot WHERE LOWER(name)=?");
			st.setString(1, name.toLowerCase());
			rs = st.executeQuery();
			return rs.next();
		} catch (Exception e) {
			lineage.share.System.printf("%s : isName(String name)\r\n", RobotController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return false;
	}

	/**
	 * 스킬정보 추출.
	 * 
	 * @param pr
	 */
	static public void readSkill(PcRobotInstance pr) {
		List<Skill> list = SkillController.find(pr);
		if (list == null)
			return;

		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM _robot_skill WHERE class=?");
			st.setString(1, getClassNameHangl(pr.getClassType()));
			rs = st.executeQuery();
			while (rs.next()) {
				Skill s = SkillDatabase.find(rs.getInt("skill"));
				if (s != null) {
					SkillRobot sr = new SkillRobot(s);
					sr.setType(rs.getString("skill_type"));
					list.add(sr);
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : readSkill(PcRobotInstance pr)\r\n", RobotController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	/**
	 * 기억정보 추출.
	 * 
	 * @param pr
	 */
	static public void readBook(PcRobotInstance pr) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM _robot_book WHERE (clan_uid=0 OR clan_uid=?) AND type=?");
			st.setLong(1, pr.getClanId());
			st.setString(2, pr.getTypeRobot());
			rs = st.executeQuery();
			while (rs.next()) {
				Book b = BookController.getPool();
				b.setLocation(rs.getString("location"));
				b.setX(rs.getInt("locX"));
				b.setY(rs.getInt("locY"));
				b.setMap(rs.getInt("locMAP"));
				b.setType(rs.getString("type"));
				b.setClanUid(rs.getInt("clan_uid"));
				BookController.append(pr, b);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : readBook(PcRobotInstance pr)\r\n", RobotController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	/**
	 * _robot_drop 테이블에서 읽은 정보를 토대로 로봇 인벤토리에 드랍할 아이템 등록.
	 * : 로딩 후 로봇이 스폰될때 로딩됨.
	 * : 또는 로봇이 죽은 후 다시 스폰될때 로딩됨.
	 * 
	 * @param pr
	 */
	static public void readDrop(PcRobotInstance pr) {
		//
		List<RobotDrop> list = null;
		switch (pr.getClassType()) {
			case 0x00:
				synchronized (list_drop) {
					list = list_drop.get("군주");
				}
				break;
			case 0x02:
				synchronized (list_drop) {
					list = list_drop.get("요정");
				}
				break;
			case 0x01:
				synchronized (list_drop) {
					list = list_drop.get("기사");
				}
				break;
			case 0x03:
				synchronized (list_drop) {
					list = list_drop.get("마법사");
				}
				break;
			case 0x04:
				synchronized (list_drop) {
					list = list_drop.get("다크엘프");
				}
				break;
		}
		if (list == null)
			return;
		//
		for (RobotDrop rd : list) {
			Item item = ItemDatabase.find(rd.getItemName());
			if (Util.random(0, Lineage.robot_auto_pc_chance_max_drop) <= Util.random(0, rd.getChance())
					&& item != null) {
				ItemInstance ii = ItemDatabase.newInstance(item);
				if (ii != null) {
					ii.setObjectId(ServerDatabase.nextItemObjId());
					ii.setDefinite(true);
					ii.setBress(rd.getItemBress());
					ii.setEnLevel(Util.random(rd.getItemEnMin(), rd.getItemEnMax()));
					ii.setCount(Util.random(rd.getCountMin() == 0 ? 1 : rd.getCountMin(),
							rd.getCountMax() == 0 ? 1 : rd.getCountMax()));
					pr.getInventory().append(ii, false);
				}
			}
		}
	}

	/**
	 * 로봇과 연결된 클랜 찾아서 리턴.
	 * 
	 * @param robot
	 * @return
	 */
	static public Clan findClan(RobotInstance robot) {
		synchronized (list_clan) {
			return robot.getClanId() == 0 ? null : list_clan.get(robot.getClanId());
		}
	}

	static public Clan findClan(long uid) {
		synchronized (list_clan) {
			return uid == 0 ? null : list_clan.get(uid);
		}
	}

	static public Clan findClan(String name) {
		synchronized (list_clan) {
			for (Clan c : list_clan.values()) {
				if (c.getName().equalsIgnoreCase(name))
					return c;
			}
			return null;
		}
	}

	/**
	 * 공성전이 진행중일때 호출되며, 로봇이 스폰될 위치가 존재하는지 확인해줌.
	 * 
	 * @param ri
	 * @return
	 */
	public static boolean isKingdomLocation(RobotInstance ri, boolean teleport) {
		//
		switch (ri.getClassType()) {
			case 0x00:
				synchronized (royal_location_list) {
					for (int[] location : royal_location_list) {
						if (World.getMapdynamic(location[0], location[1], location[2]) == 0) {
							ri.setHeading(location[3]);
							if (teleport)
								ri.toTeleport(location[0], location[1], location[2], true);
							return true;
						}
					}
				}
				break;
			case 0x01:
				synchronized (knight_location_list) {
					for (int[] location : knight_location_list) {
						if (World.getMapdynamic(location[0], location[1], location[2]) == 0) {
							ri.setHeading(location[3]);
							if (teleport)
								ri.toTeleport(location[0], location[1], location[2], true);
							return true;
						}
					}
				}
				break;
			case 0x02:
				synchronized (elf_location_list) {
					for (int[] location : elf_location_list) {
						if (World.getMapdynamic(location[0], location[1], location[2]) == 0) {
							ri.setHeading(location[3]);
							if (teleport)
								ri.toTeleport(location[0], location[1], location[2], true);
							return true;
						}
					}
				}
				break;
			case 0x03:
				synchronized (wizard_location_list) {
					for (int[] location : wizard_location_list) {
						if (World.getMapdynamic(location[0], location[1], location[2]) == 0) {
							ri.setHeading(location[3]);
							if (teleport)
								ri.toTeleport(location[0], location[1], location[2], true);
							return true;
						}
					}
				}
				break;
		}
		//
		return false;
	}

	/**
	 * 공성전 중이라면 호출되며, 랜덤워킹 가능한 좌표로 랜덤텔레포트 함.
	 * 
	 * @param ri
	 */
	public static void toKingdomRandomLocationTeleport(RobotInstance ri) {
		int[] location = random_location_list[Util.random(0, random_location_list.length - 1)];
		ri.toTeleport(location[0], location[1], location[2], true);
	}

	/**
	 * 공성전이 시작되면 호출됨.
	 */
	public static void toStartWar() {
	}

	/**
	 * 공성전이 종료되면 호출됨.
	 */
	public static void toStopWar() {
		synchronized (knight_location_list) {
			for (int[] location : knight_location_list)
				location[3] = 0;
		}
		synchronized (elf_location_list) {
			for (int[] location : elf_location_list)
				location[3] = 0;
		}
	}

	/**
	 * 무인 버프 pc 정보 추출.
	 */
	private static void readBuffRobot() {

		for (int i = 0; i < 1; ++i) // 기란
			list_buff.add(new BuffRobotInstance(33446, 32828, 4, 6, "행복헤이", 734));
		for (int t = 0; t < 1; ++t) // 화말
			list_buff1.add(new BuffRobotInstance1(32733, 32449, 4, 4, "화말헤이", 734));
		for (int e = 0; e < 1; ++e) // 글말
			list_buff2.add(new BuffRobotInstance2(32626, 32735, 4, 6, "글말헤이", 734));
		// for(int r=0 ; r<1 ; ++r) //은말
		// list_buff3.add( new BuffRobotInstance3(33064, 33391, 4, 2, "은말헤이", 1186) );
		// for(int s=0 ; s<1 ; ++s) //말섬
		// list_buff4.add( new BuffRobotInstance4(32597, 32916, 0, 5, "말섬헤이", 734) );
	}

	// private static void readBattleZoneRobot() {
	// list_BattleZone.add( new BattleZoneRobotInstance(33432, 32805, 4, 4,
	// "배틀존관리자", 98) );
	// list_BattleZone.add( new BattleZoneRobotInstance(32622, 32795, 4, 5,
	// "배틀존관리자", 98) );
	// list_BattleZone.add( new BattleZoneRobotInstance(33432, 32805, 4, 4,
	// "배틀존관리자", 13717) );
	// list_BattleZone.add( new BattleZoneRobotInstance(34056, 32291, 4, 6,
	// "배틀존관리자", 13717) );

	// list_BattleZone.add( new BattleZoneRobotInstance(32650, 32955, 0, 6,
	// "배틀존관리자", 13717) );
	// list_BattleZone.add( new BattleZoneRobotInstance(32633, 32960, 0, 5,
	// "배틀존관리자", 13717) );
	// list_BattleZone.add( new BattleZoneRobotInstance(32614, 32962, 0, 5,
	// "배틀존관리자", 13717) );

	// list_BattleZone.add( new BattleZoneRobotInstance(32650, 32955, 0, 6,
	// "배틀존관리자", 13717) );
	// list_BattleZone.add( new BattleZoneRobotInstance(32633, 32960, 0, 5,
	// "배틀존관리자", 13717) );
	// list_BattleZone.add( new BattleZoneRobotInstance(32611, 32960, 0, 5,
	// "배틀존관리자", 13717) );
	// }

	/**
	 * 무인PC 정보 추출.
	 */
	private static void readPcRobot() {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM _robot");
			rs = st.executeQuery();
			while (rs.next()) {
				PcRobotInstance pr = null;
				if (rs.getString("type").equalsIgnoreCase("normal"))
					pr = new PcRobotInstance();
				else if (rs.getString("type").equalsIgnoreCase("cracker"))
					pr = new CrackerRobotInstance();
				else if (rs.getString("type").equalsIgnoreCase("haste_shop"))
					pr = new HasteRobotInstance();
				else if (rs.getString("type").equalsIgnoreCase("wizard_zone"))
					pr = new WizardZoneRobotInstance();
				else if (rs.getString("type").equalsIgnoreCase("shop"))
					pr = new PcRobotShopInstance();
				if (pr == null)
					continue;

				//
				pr.setTypeRobot(rs.getString("type"));
				pr.setObjectId(ServerDatabase.nextNpcObjId());
				pr.setName(rs.getString("name"));
				pr.setAc(rs.getInt("ac"));
				pr.setStr(rs.getInt("str"));
				pr.setDex(rs.getInt("dex"));
				pr.setCon(rs.getInt("con"));
				pr.setWis(rs.getInt("wis"));
				pr.setInt(rs.getInt("inter"));
				pr.setCha(rs.getInt("cha"));
				pr.setX(rs.getInt("locX"));
				pr.setY(rs.getInt("locY"));
				pr.setMap(rs.getInt("locMAP"));
				pr.setHeading(pr instanceof PcRobotShopInstance ? 5 : Util.random(0, 7));
				pr.setTitle(rs.getString("title"));
				pr.setLawful(rs.getInt("lawful"));
				pr.setClanId(rs.getInt("clanID"));
				if (rs.getString("class").equalsIgnoreCase("군주")) {
					pr.setClassType(0x00);
					pr.setClassGfx(rs.getString("sex").equalsIgnoreCase("남자") ? Lineage.royal_male_gfx
							: Lineage.royal_female_gfx);
					pr.setGfx(pr.getClassGfx());
					pr.setMaxHp(Lineage.royal_hp);
					pr.setMaxMp(Lineage.royal_mp);
				} else if (rs.getString("class").equalsIgnoreCase("기사")) {
					pr.setClassType(0x01);
					pr.setClassGfx(rs.getString("sex").equalsIgnoreCase("남자") ? Lineage.knight_male_gfx
							: Lineage.knight_female_gfx);
					pr.setGfx(pr.getClassGfx());
					pr.setMaxHp(Lineage.knight_hp);
					pr.setMaxMp(Lineage.knight_mp);
				} else if (rs.getString("class").equalsIgnoreCase("요정")) {
					pr.setClassType(0x02);
					pr.setClassGfx(
							rs.getString("sex").equalsIgnoreCase("남자") ? Lineage.elf_male_gfx : Lineage.elf_female_gfx);
					pr.setGfx(pr.getClassGfx());
					pr.setMaxHp(Lineage.elf_hp);
					pr.setMaxMp(Lineage.elf_mp);
				} else if (rs.getString("class").equalsIgnoreCase("마법사")) {
					pr.setClassType(0x03);
					pr.setClassGfx(rs.getString("sex").equalsIgnoreCase("남자") ? Lineage.wizard_male_gfx
							: Lineage.wizard_female_gfx);
					pr.setGfx(pr.getClassGfx());
					pr.setMaxHp(Lineage.wizard_hp);
					pr.setMaxMp(Lineage.wizard_mp);
				} else if (rs.getString("class").equalsIgnoreCase("다크엘프")) {
					pr.setClassType(0x04);
					pr.setClassGfx(rs.getString("sex").equalsIgnoreCase("남자") ? Lineage.darkelf_male_gfx
							: Lineage.darkelf_female_gfx);
					pr.setGfx(pr.getClassGfx());
					pr.setMaxHp(Lineage.darkelf_hp);
					pr.setMaxMp(Lineage.darkelf_mp);
				} else if (rs.getString("class").equalsIgnoreCase("용기사")) {
					pr.setClassType(0x05);
					pr.setClassGfx(rs.getString("sex").equalsIgnoreCase("남자") ? Lineage.dragonknight_male_gfx
							: Lineage.dragonknight_female_gfx);
					pr.setGfx(pr.getClassGfx());
					pr.setMaxHp(Lineage.dragonknight_hp);
					pr.setMaxMp(Lineage.dragonknight_mp);
				} else if (rs.getString("class").equalsIgnoreCase("환술사")) {
					pr.setClassType(0x06);
					pr.setClassGfx(rs.getString("sex").equalsIgnoreCase("남자") ? Lineage.blackwizard_male_gfx
							: Lineage.blackwizard_female_gfx);
					pr.setGfx(pr.getClassGfx());
					pr.setMaxHp(Lineage.blackwizard_hp);
					pr.setMaxMp(Lineage.blackwizard_mp);
				} else if (rs.getString("class").equalsIgnoreCase("전사")) {
					pr.setClassType(0x07);
					pr.setClassGfx(rs.getString("sex").equalsIgnoreCase("남자") ? Lineage.warrior_male_gfx
							: Lineage.warrior_female_gfx);
					pr.setGfx(pr.getClassGfx());
					pr.setMaxHp(Lineage.warrior_hp);
					pr.setMaxMp(Lineage.warrior_mp);
				}
				if (rs.getInt("gfx") >= 0)
					pr.setGfx(rs.getInt("gfx"));
				//
				Exp e = ExpDatabase.find(rs.getInt("level"));
				int hp = 0;
				int mp = 0;
				for (int i = pr.getLevel() + 1; i < e.getLevel(); ++i) {
					hp += CharacterController.toStatusUP(pr, true);
					mp += CharacterController.toStatusUP(pr, false);
				}
				pr.setMaxHp(pr.getMaxHp() + hp);
				pr.setMaxMp(pr.getMaxMp() + mp);
				pr.setNowHp(pr.getMaxHp());
				pr.setNowMp(pr.getMaxMp());
				pr.setLevel(e.getLevel());
				pr.setExp(e.getBonus() - e.getExp());
				pr.setWeapon(ItemDatabase.find(rs.getString("item_weapon_name")));
				pr.setWeaponEn(rs.getInt("item_weapon_en"));
				pr.setDynamicMr(rs.getInt("mr"));
				pr.setDynamicSp(rs.getInt("sp"));
				pr.setFood(Lineage.MAX_FOOD);
				pr.setAttribute(Util.random(1, 4));
				pr.setMdl(MagicdollListDatabase.find(rs.getString("magicdoll")));
				//
				synchronized (list_pc) {
					list_pc.add(pr);
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : readPcRobot()\r\n", RobotController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	public static String getClassNameHangl(int classType) {
		String className = "군주";
		if (classType == 0x01)
			className = "기사";
		if (classType == 0x02)
			className = "요정";
		if (classType == 0x03)
			className = "마법사";
		if (classType == 0x04)
			className = "다크엘프";
		if (classType == 0x05)
			className = "용기사";
		if (classType == 0x06)
			className = "환술사";
		return className;
	}

	private static void readClan() {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM _robot_clan");
			rs = st.executeQuery();
			while (rs.next()) {
				Clan c = new Clan();
				c.setUid(rs.getInt("uid"));
				c.setName(rs.getString("name"));
				c.setLord(rs.getString("lord"));
				String icon = rs.getString("icon");
				if (icon != null)
					c.setIcon(Util.StringToByte(icon));
				String list = rs.getString("list");
				if (list != null) {
					for (String member : list.split(" "))
						c.appendMemberList(member);
				}
				// c.setNote( rs.getString("note") );

				synchronized (list_clan) {
					list_clan.put((long) c.getUid(), c);
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : readClan()\r\n", RobotController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}

	}

	private static void readClanPvp() {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM _robot_clan_pvp");
			rs = st.executeQuery();
			while (rs.next()) {
				Clan clan = null;
				synchronized (list_clan) {
					clan = list_clan.get(rs.getLong("clan_uid"));
				}
				if (clan == null)
					continue;
				try {
					StringTokenizer token = new StringTokenizer(rs.getString("enemy_clan_uid_list"), "|");
					while (token.hasMoreTokens()) {
						String uid = token.nextToken();
						long clan_uid = 0;
						try {
							clan_uid = Long.valueOf(uid);
						} catch (Exception e) {
							continue;
						}
						// if(clan_uid > 0)
						// clan.getEnemyClanUidList().add( clan_uid );
					}
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : readClanPvp()\r\n", RobotController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	static private void readDrop() {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM _robot_drop");
			rs = st.executeQuery();
			while (rs.next()) {
				RobotDrop rd = new RobotDrop();
				rd.setClassName(rs.getString("class"));
				rd.setItemName(rs.getString("item_name"));
				rd.setItemBress(rs.getInt("item_bress"));
				rd.setItemEnMin(rs.getInt("item_en_min"));
				rd.setItemEnMax(rs.getInt("item_en_max"));
				rd.setCountMin(rs.getInt("count_min"));
				rd.setCountMax(rs.getInt("count_max"));
				rd.setChance(rs.getInt("chance"));

				synchronized (list_drop) {
					List<RobotDrop> list = list_drop.get(rd.getClassName());
					if (list == null) {
						list = new ArrayList<RobotDrop>();
						list_drop.put(rd.getClassName(), list);
					}
					list.add(rd);
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : readDrop()\r\n", RobotController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	public static int getPcRobotListSize() {
		return list_pc.size();
	}
}
