package lineage.world;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ServerBasePacket;
import lineage.network.packet.server.S_WorldTime;
import lineage.persnal_shop.PersnalShopInstance;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.FightController;
import lineage.world.controller.RobotController;
import lineage.world.object.object;
import lineage.world.object.instance.FishermanInstance;
import lineage.world.object.instance.MagicDollInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.PcShopInstance;
import lineage.world.object.instance.PetInstance;
import lineage.world.object.instance.RobotInstance;
import lineage.world.object.instance.SummonInstance;
import lineage.world.object.npc.Promot_npc;
import lineage.world.object.npc.SpotTower;
import lineage.world.object.npc.kingdom.KingdomCastleTop;
import lineage.world.object.npc.kingdom.KingdomDoor;

public final class World {

	static private Map<Integer, lineage.bean.lineage.Map> list;
	static private int maxMapId;
	static private List<PcInstance> pc_list;
	static private List<RobotInstance> robot_list;
	static private List<MonsterInstance> monster_list;
	static private Integer timer_item_idx;
	//private static int clear_time_count;
	static private List<KingdomCastleTop> ctop_list;
	static private List<PcShopInstance> pc_shop_list;
	
	static private List<PersnalShopInstance> persnalShopInstance_list;

	static public void init(){
		TimeLine.start("World..");

		persnalShopInstance_list = new ArrayList<PersnalShopInstance>();
		timer_item_idx = 0;
		list = new HashMap<Integer, lineage.bean.lineage.Map>();
		pc_list = new ArrayList<PcInstance>();
		robot_list = new ArrayList<RobotInstance>();
		monster_list = new ArrayList<MonsterInstance>();
		ctop_list = new ArrayList<KingdomCastleTop>();
		pc_shop_list = new ArrayList<PcShopInstance>();

		try {
			File f = new File(String.format("./maps/Cache/", Lineage.server_version)); 
			// 폴더가 존재할경우 
			if(f.isDirectory()){ 
				// 캐쉬파일로부터 맵 로딩 
				read(false); 
			// 폴더가 존재하지 않을경우 
			}else{ 
				lineage.share.System.println("Cache 폴더 생성 완료."); 
				// 폴더생성 
				f.mkdir(); 
				// txt파일로부터 맵 로딩 
				read(true); 
				// 캐쉬파일 작성 
				writeCache(); 
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init()\r\n");
			lineage.share.System.println(e);
		}

		TimeLine.end();
	}

	static private void writeCache() throws Exception{ 
		lineage.share.System.println("캐쉬 파일을 생성하고 있습니다."); 
		BufferedOutputStream bw = null;
		for(lineage.bean.lineage.Map m : list.values()){ 
			bw = new BufferedOutputStream(new FileOutputStream(String.format("maps/Cache/%d.data", +m.mapid))); 
			bw.write(m.data); 
			bw.close(); 
		}
		lineage.share.System.println("(완료)"); 
	} 

	static private void read(boolean type) throws Exception {
		String maps;
		StringTokenizer st1;
		/** 맵좌표정보 추출 **/
		BufferedReader lnrr = new BufferedReader(new FileReader("maps/Maps.csv"));
		/** byte임시 저장용 **/
		byte[] temp = new byte[22149121];
		/** 첫번째라인 패스 용 **/
		while ((maps = lnrr.readLine()) != null) {
			if (maps.startsWith("#")) {
				continue;
			}

			st1 = new StringTokenizer(maps, ",");
			int readID = Integer.parseInt(st1.nextToken().trim()); 
			int x1 = Integer.parseInt(st1.nextToken().trim()); 
			int x2 = Integer.parseInt(st1.nextToken().trim()); 
			int y1 = Integer.parseInt(st1.nextToken().trim()); 
			int y2 = Integer.parseInt(st1.nextToken().trim()); 
			int size = Integer.parseInt(st1.nextToken());
			
			// gui의 맵 목록을 순서대로 정렬하기 위한 변수.
			if (readID >= maxMapId)
			maxMapId = readID;
			
			if (type) {
				readText(temp, readID, size, x1, x2, y1, y2);
			} else {
				File file = new File(String.format("./maps/Cache/%d.data", readID));
				
				if (file.exists()) {
					readCache(readID, x1, x2, y1, y2, size);
				} else {
					if (readText(temp, readID, size, x1, x2, y1, y2)) {
						readCache(readID, x1, x2, y1, y2, size);
						lineage.share.System.println(String.format("maps/Cache/%d.data 파일 생성 완료.", readID));
					}
				}		
			}
		}
		
		lnrr.close();
	}

	static private boolean readText(byte[] temp, int readID, int size, int x1, int x2, int y1, int y2) throws Exception {
		int TotalSize = -1;
		String line;
		StringTokenizer st;
		File file = new File(String.format("./maps/Text/%d.txt", readID));
		
		if (!file.exists()) {
			lineage.share.System.println(String.format("[심각]  maps/Text/%d.txt 파일이 존재하지 않습니다.", readID));
			return false;
		}
		
		/** 파일로부터 byte로 읽기 **/
		BufferedReader lnr = new BufferedReader(new FileReader(String.format("./maps/Text/%d.txt", readID)));
		while ((line = lnr.readLine()) != null) {
			st = new StringTokenizer(line, ",");
			for (int i = 0; i < size; ++i) {
				int t = Integer.parseInt(st.nextToken());
				if (Byte.MAX_VALUE < t) {
					temp[++TotalSize] = Byte.MAX_VALUE;
				} else {
					temp[++TotalSize] = (byte) t;
				}
			}
		}
		
		/** 추출한 맵정보를 사이즈에맞게 새로 작성 **/
		byte[] MAP = new byte[TotalSize - 1];
		System.arraycopy(temp, 0, MAP, 0, MAP.length);
		lineage.bean.lineage.Map m = new lineage.bean.lineage.Map();
		m.mapid = readID;
		m.locX1 = x1;
		m.locX2 = x2;
		m.locY1 = y1;
		m.locY2 = y2;
		m.size = size;
		m.data = MAP;
		m.data_size = m.data.length;
		m.dataDynamic = new byte[m.data_size];
		list.put(m.mapid, m);
		
		lnr.close();
		
		File cacheFile = new File(String.format("./maps/Cache/%d.data", readID));
		if (!cacheFile.exists()) {
			BufferedOutputStream bw = null;
			bw = new BufferedOutputStream(new FileOutputStream(String.format("./maps/Cache/%d.data", +m.mapid)));
			bw.write(m.data);
			bw.close();
			return true;
		}
		
		return true;
	}

	static private void readCache(int readID, int x1, int x2, int y1, int y2, int size) throws Exception {
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(String.format("maps/Cache/%d.data", readID)));
		byte[] data = new byte[bis.available()];
		bis.read(data, 0, data.length);
		/** 추출한 맵정보를 사이즈에맞게 새로 작성 **/
		lineage.bean.lineage.Map m = new lineage.bean.lineage.Map();
		m.mapid = readID;
		m.locX1 = x1;
		m.locX2 = x2;
		m.locY1 = y1;
		m.locY2 = y2;
		m.size = size;
		m.data = data;
		m.data_size = data.length;
		m.dataDynamic = new byte[m.data_size];
		list.put(m.mapid, m);
		
		bis.close();
	}

	/**
	 * 맵아이디에 해당하는 클레스 찾아서 리턴.
	 */
	static public lineage.bean.lineage.Map get_map(int map){
		return list.get(map);
	}

	/**
	 * 좌표에 해당하는 타일값 추출.
	 */
	static public int get_map(int x, int y, int map){
		lineage.bean.lineage.Map m = get_map(map);
		if(m!=null){
			if(x < m.locX1)
				return 0;
			if(y < m.locY1)
				return 0;
			int pos = ((m.locX2-m.locX1)*(y-m.locY1)) + (x-m.locX1) + (y-m.locY1);
			return pos>=m.data_size || pos<0 ? 0 : m.data[ pos ];
		}
		return 0;
	}

	/**
	 * 이동 불가능한 타일 여부.
	 */
	static public boolean isNotMovingTile(int x, int y, int map) {
		int tile = World.get_map(x, y, map);

		return tile == 0 || tile == 4 || tile == 8 || tile == 12 || tile == 16 || tile == 127 || (map == 70 && (tile == 28 || tile == 32 || tile == 44 || tile == 46));
	}
	
	/**
	 * 좌표에 해당하는 타일값 변경.
	 */
	static public void set_map(int x, int y, int map, int tail){
		lineage.bean.lineage.Map m = get_map(map);
		if(m!=null){
			int pos = ((m.locX2-m.locX1)*(y-m.locY1)) + (x-m.locX1) + (y-m.locY1);
			if(pos<m.data_size && pos>=0)
				m.data[ pos ] = (byte)tail;
		}
	}

	public static int getDoorTile(int x, int y, int map, int heading, boolean isClose) {
		int val = get_map(x, y, map);
		if (isClose) {
			val = val >> 4;
			val = val << 4;

			if ((heading == 4) || (heading == 0)) {
				val += 1;
				val += 4;
			} else {
				val += 2;
				val += 8;
			}
		} else {
			val = val >> 4;
			val = val << 4;

			val += 2;
			val += 8;
			val += 1;
			val += 4;
		}
		return val;
	}
	
	/**
	 * 객체가 좌표값에 얼마만큼 존재하는지 실시간으로 업데이트하는 함수.
	 *  : 몬스터 인공지능에서 과부하를 줄이기위해 insideList를 참고하지 않고 해당 함수를 이용해
	 *    실시간으로 변경되는 좌표에 카운팅값을 참고하기 위함.
	 *  : 기본값은 0이며, 0보다 크면 해당좌표에 객체가 존재한다고 판단하여 해당 좌표로 이동하지 않음.
	 * @param x
	 * @param y
	 * @param map
	 * @param plus	: + 할지 - 할지 구분.
	 */
	static public void update_mapDynamic(int x, int y, int map, boolean plus){
		lineage.bean.lineage.Map m = get_map(map);
		if(m!=null)
			m.update_mapDynamic(x, y, map, plus);
	}
	
	static public void update_mapDynamic(int x, int y, int map) {
		lineage.bean.lineage.Map m = get_map(map);
		if(m!=null)
			m.update_mapDynamic(x, y, map);
	}

	/**
	 * 해당 좌표에 객채가 존재하는지 확인하는 함수.
	 *  : 위 함수 주석에 설명했듯이. 해당 변수는 해당좌표에 객채가 몇개존재하는지 실사간으로 변하는 변수임.
	 *    해당 변수를 통해 좌표에 객채가 존재하는지 확인 가능.
	 * @param x
	 * @param y
	 * @param map
	 * @return
	 */
	static public boolean isMapdynamic(int x, int y, int map){
		lineage.bean.lineage.Map m = get_map(map);
		if(m!=null)
			return m.isMapdynamic(x, y, map);
		return false;
	}

	static public int getMapdynamic(int x, int y, int map){
		lineage.bean.lineage.Map m = get_map(map);
		if(m!=null)
			return m.getMapdynamic(x, y, map);
		return 0;
	}
	
	/**
	 * inside목록에서 지정한 좌표내에 객체가 있는지 확인함.
	 *  : 단 Character 을 상속한 객체만 체크.
	 *  : 뚫어핵 방지용으로 PcInstance에 toMoving에서 사용중.
	 * @param o
	 * @param x
	 * @param y
	 * @return
	 */
	static public boolean isInsideList(object o, int x, int y) {
		for(object temp : o.getInsideList(true)) {
			if((temp instanceof PcInstance || temp instanceof MonsterInstance) && !temp.isDead() && temp.getX()==x && temp.getY()==y)
				return true;
		}
		return false;
	}

	static public int getZone(int x, int y, int map){
		return get_map(x, y, map) & 48;
	}

	/**
	 * 컴뱃존 여부 확인.
	 */
	static public boolean isCombatZone(int x, int y, int map){
		return getZone(x, y, map)==0x20;
	}

	/**
	 * 세이프존 여부 확인.
	 */
	static public boolean isSafetyZone(int x, int y, int map) {
		// 낚시터를 강제 세이프존으로 설정.성환
//		if(map == 0)
//        
//			return true;
		return getZone(x, y, map)==0x10;
	}

	/**
	 * 노말존 여부 확인.
	 */
	static public boolean isNormalZone(int x, int y, int map){
		// 잊혀진섬을 강제로 노멀존 설정
		if(map == 70)
			return true;
		return getZone(x, y, map)==0x00;
	}
	
	/**
	 * 공격 불가능한 타일 여부.
	 * 2019-05-31
	 * by connector12@nate.com
	 */
	static public boolean isNotAttackTile(int x, int y, int map) {
		// 화둥
		if (화룡의둥지(x, y, map) || 오렌(x, y, map)) {
			int tile = World.get_map(x, y, map);
			
			return tile == 12;
		}
		
		//return tile == 4 || tile == 8 || tile == 12 || tile == 16 || tile == 127;
		return false;
	}

	
	/**
	 * 화룡의 둥지 확인.
	 * 2020-07-25
	 * by connector12@nate.com
	 */
	static public boolean 화룡의둥지(int x, int y, int map) {	
		return x >= 33497 && x <= 33781 && y >= 32230 && y <= 32413 && map == 4;
	}
	
	/**
	 * 오렌 확인.
	 * 2020-07-25
	 * by connector12@nate.com
	 */
	static public boolean 오렌(int x, int y, int map) {	
		return x >= 34090 && x <= 34290 && y >= 32175 && y <= 32435 && map == 4;
	}
	
	/**
	 * 결투장 여부 확인.
	 * 2017-12-01
	 * by all-night
	 */
	static public boolean isBattleZone(int x, int y, int map) {	
		return x >= Lineage.battle_zone_x1 && x <= Lineage.battle_zone_x2 && y >= Lineage.battle_zone_y1 && y <= Lineage.battle_zone_y2  && map == Lineage.battle_zone_map;
	}
	
	/**
	 * 오브젝트의 통과가능 여부를 리턴 이동하기전 미리 던져서 리턴받음.
	 */
	static public boolean isThroughObject(int x, int y, int map, int dir){
		switch(dir){
			case 0:
				return (get_map(x, y, map)&2)>0;
			case 1:
				return (get_map(x, y, map)&2)>0 && (get_map(x, y-1, map)&1)>0;
			case 2:
				return (get_map(x, y, map)&1)>0;
			case 3:
				return ((get_map(x, y+1, map)&2)>0 && (get_map(x, y+1, map)&1)>0) || ((get_map(x, y, map)&1)>0 && (get_map(x+1, y+1, map)&2)>0);
			case 4:
				return (get_map(x, y+1, map)&2)>0;
			case 5:
				return ((get_map(x, y+1, map)&2)>0 && (get_map(x-1, y+1, map)&1)>0) || ((get_map(x-1, y, map)&1)>0 && (get_map(x-1, y+1, map)&2)>0);
			case 6:
				return (get_map(x-1, y, map)&1) > 0;
			case 7:
				return ((get_map(x, y, map)&2)>0 && (get_map(x-1, y-1, map)&1)>0) || ((get_map(x-1, y, map)&1)>0 && (get_map(x-1, y, map)&2)>0);
		}
		return false;
	}

	/**
	 * 마법이나 화살등 원거리 공격효과의 통과가능 여부를 리턴 이동하기전 미리 던져서 리턴받음.
	 */
	static public boolean isThroughAttack(int x, int y, int map, int dir){
		//		lineage.share.System.println( get_map(x, y, map) );
		switch(dir){
			case 0:
				return (get_map(x, y, map)&8)>0;
			case 1:
				return ((get_map(x, y, map)&8)>0 && (get_map(x, y-1, map)&4)>0) || ((get_map(x, y, map)&4)>0 && (get_map(x+1, y, map)&8)>0);
			case 2:
				return (get_map(x, y, map)&4)>0;
			case 3:
				return ((get_map(x, y+1, map)&8)>0 && (get_map(x, y+1, map)&4)>0) || ((get_map(x, y, map)&4)>0 && (get_map(x+1, y+1, map)&8)>0);
			case 4:
				return (get_map(x, y+1, map)&8)>0;
			case 5:
				return ((get_map(x, y+1, map)&8)>0 && (get_map(x-1, y+1, map)&4)>0) || ((get_map(x-1, y, map)&4)>0 && (get_map(x-1, y+1, map)&8)>0);
			case 6:
				return (get_map(x-1, y, map)&4) > 0;
			case 7:
				return ((get_map(x, y, map)&8)>0 && (get_map(x-1, y-1, map)&4)>0) || ((get_map(x-1, y, map)&4)>0 && (get_map(x-1, y, map)&8)>0);
		}
		return false;
	}

	/**
	 * 해당 객체의 좌표를 확인해서 공격이 가능한지 판단하는 메서드.
	 */
	static public boolean isAttack(object cha, object use) {
		
		if (cha == null || use == null || FightController.isFightMonster(use))
			return false;
		
		// 내성문은 공격 안됨
		if (use instanceof KingdomDoor && use.getName().equalsIgnoreCase("$441"))
			return false;
		if (cha instanceof FishermanInstance || use instanceof FishermanInstance)
			return false;
		// 마법인형 공격 안되게.
		if (cha instanceof MagicDollInstance || use instanceof MagicDollInstance || use instanceof Promot_npc)
			return false;
		// 스팟 타워는 혈맹이 있을경우 공격 가능.
		if (use instanceof SpotTower && cha.getGm() == 0 && cha.getClanId() < 1)
			return false;
		// 세이프존 확인.
		if ((isSafetyZone(cha.getX(), cha.getY(), cha.getMap()) || isSafetyZone(use.getX(), use.getY(), use.getMap()))
				&& (!isBattleZone(cha.getX(), cha.getY(), cha.getMap()) || !isBattleZone(use.getX(), use.getY(), use.getMap()))) {
		if(use instanceof PcInstance && cha instanceof PcInstance)
			return false;
		// 펫이 도망갓을때를   아래와같이 조건넣음.
		if(use instanceof SummonInstance && cha instanceof PcInstance)
			return use.getSummon()==null;
		if(use instanceof PcInstance && cha instanceof SummonInstance)
			return cha.getSummon()==null;
		// 공격자 혹은 대상자가 소환(펫)된 객체라면 공격 무시.
		if(cha instanceof SummonInstance && use instanceof SummonInstance)
			return false;
			}
		
		// 신규혈맹 및 무혈은 보스몬스터 공격불가
		if (cha instanceof PcInstance && use instanceof MonsterInstance) {
			if (cha != null && use != null && cha.getGm() == 0 && ((MonsterInstance) use).isBoss()&& cha.getClanId() == 0 && !Lineage.is_no_clan_attack_boss) {
				ChattingController.toChatting(cha, "혈맹이 없으면 보스몬스터 공격이 불가능 합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return false;
			}

			if (cha != null && use != null && cha.getGm() == 0 && ((MonsterInstance) use).isBoss()
					&& cha.getClanName().equalsIgnoreCase(Lineage.new_clan_name) && !Lineage.is_new_clan_attack_boss) {
				ChattingController.toChatting(cha, String.format("%s 혈맹은 보스몬스터 공격이 불가능 합니다.", Lineage.new_clan_name), Lineage.CHATTING_MODE_MESSAGE);
				return false;
			}
		}
		
		// 무혈은 컴뱃존 이외에 PK불가
		if (cha instanceof PcInstance && use instanceof PcInstance && !Lineage.is_no_clan_pvp) {
			if (cha.getGm() == 0 && cha.getClanId() == 0 && !isCombatZone(cha.getX(), cha.getY(), cha.getMap()) && !isBattleZone(cha.getX(), cha.getY(), cha.getMap())) {
				//ChattingController.toChatting(cha, "혈맹이 없으면 공격할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return false;
			}
			
			if (use.getGm() == 0 && use.getClanId() == 0 && !isCombatZone(use.getX(), use.getY(), use.getMap()) && !isBattleZone(cha.getX(), cha.getY(), cha.getMap())) {
				//ChattingController.toChatting(cha, "혈맹이 없는 유저를 공격할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return false;
			}
		}

		// 신규혈맹은 컴뱃존 이외에 PK불가
		if (cha instanceof PcInstance && use instanceof PcInstance && !Lineage.is_new_clan_pvp) {
			if (cha.getGm() == 0 && cha.getClanName() != null && cha.getClanName().equalsIgnoreCase(Lineage.new_clan_name) && !isCombatZone(cha.getX(), cha.getY(), cha.getMap()) && !isBattleZone(cha.getX(), cha.getY(), cha.getMap())) {
				//ChattingController.toChatting(cha, "신규 혈맹은 공격할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return false;
			}
			
			if (use.getGm() == 0 && use.getClanName() != null && use.getClanName().equalsIgnoreCase(Lineage.new_clan_name) && !isCombatZone(use.getX(), use.getY(), use.getMap()) && !isBattleZone(cha.getX(), cha.getY(), cha.getMap())) {
				//ChattingController.toChatting(cha, "신규 혈맹을 공격할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return false;
			}
		}
		
		// nonpvp 확인. 컴뱃존만 가능하도록하기.
		if(Lineage.nonpvp){
			if(cha instanceof PcInstance && use instanceof PcInstance)
				return isCombatZone(cha.getX(), cha.getY(), cha.getMap()) && isCombatZone(use.getX(), use.getY(), use.getMap());
			if(cha instanceof SummonInstance && use instanceof PcInstance)
				return isCombatZone(cha.getX(), cha.getY(), cha.getMap()) && isCombatZone(use.getX(), use.getY(), use.getMap());
			if(cha instanceof PcInstance && use instanceof SummonInstance)
				return isCombatZone(cha.getX(), cha.getY(), cha.getMap()) && isCombatZone(use.getX(), use.getY(), use.getMap());
		}
		return true;
	}
	// 여기가 월드에서 지워지는거에요 오브젝트가
	// 모든 오브젝트가 텔레포트나 포탈이나
	// 혹은 없애고싶을때 이걸 사용하는데
	static public void remove(object o){
		try {
			lineage.bean.lineage.Map m = list.get(o.getMap());
			if(m!=null) {
			
				m.remove(o);
		
				o.clearList(true);
			}
		} catch (Exception e) { }
	}

	static public void append(object o){
		lineage.bean.lineage.Map m = list.get(o.getMap());
		if(m!=null)
			m.append(o);
	}

	static public void getLocationList(object o, int loc, List<object> r_list){
		lineage.bean.lineage.Map m = list.get(o.getMap());
		if(m!=null)
			m.getList(o, loc, r_list);
	}
	
	static public void appendPcShop(PcShopInstance pc_shop) {
		if (pc_shop == null)
			return;
		
		synchronized (pc_shop_list) {
			if (!pc_shop_list.contains(pc_shop))
				pc_shop_list.add(pc_shop);
		}
	}
	
	static public void removePcShop(PcShopInstance pc_shop) {
		if (pc_shop == null)
			return;

		synchronized (pc_shop_list) {
			pc_shop_list.remove(pc_shop);
		}
	}
	
	static public PcShopInstance findPcShop(String name) {
		if (name == null)
			return null;
		
		// 찾을 객체 검색.
		for (PcShopInstance pc_shop : getPcShopList()) {
			if (name.equalsIgnoreCase(pc_shop.getName().replaceAll("의 상점", "")))
				return pc_shop;
		}
		return null;
	}
	
	static public List<PcShopInstance> getPcShopList() {
		synchronized (pc_shop_list) {
			return new ArrayList<PcShopInstance>(pc_shop_list);
		}
	}

	static public void appendPc(PcInstance pc){
		if(pc == null)
			return;
		
		synchronized (pc_list) {
			if(!pc_list.contains(pc))
				pc_list.add(pc);
		}
	}
	
	static public void appendRobot(RobotInstance robot) {
		if(robot == null)
			return;
		
		synchronized (robot_list) {
			if(!robot_list.contains(robot))
				robot_list.add(robot);
		}
		
	}

	static public void removePc(PcInstance pc){
		if(pc == null)
			return;
		
		synchronized (pc_list) {
			pc_list.remove(pc);
		}
	}

	static public void removeRobot(RobotInstance robot){
		if(robot == null)
			return;
		
		synchronized (robot_list) {
			robot_list.remove(robot);
		}
	}
	static public void appendMonster(MonsterInstance monster) {
		if (monster == null)
			return;

		synchronized (monster_list) {
			if (!monster_list.contains(monster))
				monster_list.add(monster);
		}
	}
	
	
	static public void removeMonster(MonsterInstance monster) {
		if (monster == null)
			return;

		synchronized (monster_list) {
			monster_list.remove(monster);
		}
	}


	static public PcInstance findPc(String name){
		if(name == null)
			return null;
		
		// 찾을 객체 검색.
		synchronized (pc_list) {
			for(PcInstance pc : pc_list){
				if(name.equalsIgnoreCase(pc.getName()))
					return pc;
			}
			return null;
		}
	}
	
	static public PcInstance findPc(long objId){
		if(objId == 0)
			return null;
		
		// 찾을 객체 검색.
		synchronized (pc_list) {
			for(PcInstance pc : pc_list){
				if(pc.getObjectId() == objId)
					return pc;
			}
			return null;
		}
	}
	
	static public PcInstance findPcAccountUid(int accountUid) {
		if(accountUid == 0)
			return null;
		
		// 찾을 객체 검색.
		synchronized (pc_list) {
			for(PcInstance pc : pc_list){
				if(pc.getClient()!=null && accountUid==pc.getClient().getAccountUid())
					return pc;
			}
			return null;
		}
	}

	static public PcInstance findRobot(String name){
		if(name == null)
			return null;
		
		// 찾을 객체 검색.
		for(PcInstance pc : getRobotList()){
			if(name.equalsIgnoreCase(pc.getName()))
				return pc;
		}
		return null;
	}
	
	static public int getPcSize() {
		Object o = PluginController.init(World.class, "getPcSize");
		return pc_list.size() + (o==null ? 0 : (Integer)o);
	}
	
	static public int getRobotSize() {
		return robot_list.size();
	}

	/**
	 * 월드에 접속되있는 사용자들에게 패킷 전송 처리하는 함수.
	 * @param packet
	 */
	static public void toSender(BasePacket packet){
		if(packet instanceof ServerBasePacket){
			ServerBasePacket sbp = (ServerBasePacket)packet;
			synchronized (pc_list) {
				for(PcInstance pc : pc_list)
					pc.toSender( ServerBasePacket.clone(BasePacketPooling.getPool(ServerBasePacket.class), sbp.getBytes()) );
			}
		}
		BasePacketPooling.setPool(packet);
	}

	/**
	 * 월드에 접속되있는 사용자들중 해당 맵에있는 사용자에게만 패킷전송 처리하는 함수.
	 * @param packet
	 * @param map
	 */
	static public void toSender(BasePacket packet, int map){
		if(packet instanceof ServerBasePacket){
			ServerBasePacket sbp = (ServerBasePacket)packet;

			synchronized (pc_list) {
				for(PcInstance pc : pc_list){
					if(pc.getMap() == map)
						pc.toSender( ServerBasePacket.clone(BasePacketPooling.getPool(ServerBasePacket.class), sbp.getBytes()) );
				}
			}
		}
		BasePacketPooling.setPool(packet);
	}

	static public List<PcInstance> getPcList(){
		synchronized (pc_list) {
			return new ArrayList<PcInstance>(pc_list);
		}
	}

	static public List<PcInstance> getRobotList(){
		synchronized (robot_list) {
			return new ArrayList<PcInstance>(robot_list);
		}
	}
	
	static public void appendCtop(KingdomCastleTop ctop) {
		if (ctop == null)
			return;
		
		synchronized (ctop_list) {
			if (!ctop_list.contains(ctop))
				ctop_list.add(ctop);
		}
	}
	
	static public List<KingdomCastleTop> getCtopList() {
		synchronized (ctop_list) {
			return new ArrayList<KingdomCastleTop>(ctop_list);
		}
	}
	
	static public List<MonsterInstance> getMonsterList() {
		synchronized (monster_list) {
			return  new ArrayList<MonsterInstance>(monster_list);
		}
	}

	/**
	 * 타이머가 주기적으로 호출함.
	 */
	static public void toTimer(long time){
		ServerDatabase.nextTime();

		boolean is_item = ++timer_item_idx%60 == 0;
		boolean is_time = ServerDatabase.LineageWorldTime%60 == 0;

		// 월드에 드랍된 아이템을 모두 순회하면서 제거될 시간이 됫는지 확인후 제거 처리.
		if(is_item && Lineage.world_item_delay!=0){
			timer_item_idx = 0;
			for(lineage.bean.lineage.Map m : list.values())
				m.clearWorldItem(time);
		}
		// 리니지 월드 시간 전송
		if(is_time)
			toSender( S_WorldTime.clone(BasePacketPooling.getPool(S_WorldTime.class)) );
	}

	/**
	 * 리니지 월드에 드랍된 아이템 전체 제거 처리 함수.
	 */
	static public void clearWorldItem(){
		for(lineage.bean.lineage.Map m : list.values())
			m.clearWorldItem();
	}

	/**
	 * 특정 맵에 해당하는 아이템만 제거할때.
	 * @param map
	 */
	static public void clearWorldItem(int map){
		lineage.bean.lineage.Map m = list.get(map);
		if(m != null)
			m.clearWorldItem();
	}
	
	static public void clearWorldMonster(int map) {
		lineage.bean.lineage.Map m = list.get(map);
		if(m != null)
			m.clearWorldMonster();
	}

	/**
	 * 맵 갯수 리턴.
	 * @return
	 */
	static public int getMapSize(){
		return list.size();
	}

	/**
	 * 맵에등록된 전체 개체 갯수 리턴.
	 * @return
	 */
	static public int getSize(){
		int count = 0;
		for(lineage.bean.lineage.Map m : list.values())
			count += m.getSize();
		return count;
	}

	/**
	 * 읽어들인 맵정보들 순회하면서 맵아이디를 String으로 변환해서 배열 리턴함.
	 * @return
	 */
	static public String[] toStringArrayMap(){
		String[] array = new String[list.size()];
		int idx = 0;
		for(lineage.bean.lineage.Map m : list.values())
			array[idx++] = String.valueOf(m.mapid);
		return array;
	}

	/**
	 * 물속 맵에 있는지 확인해주는 함수.
	 * @param o
	 * @return
	 */
	static public boolean isAquaMap(object o){
		for(int map : Lineage.MAP_AQUA)
			if(map == o.getMap())
				return true;
		return false;
	}
	
	/**
	 * 읽어들인 맵정보들 순회하면서 맵아이디를 String으로 변환해서 배열 리턴함.
	 * 
	 * @return
	 */
	static public String[] toStringMap() {
		String[] array = new String[list.size()];
		
		int idx = 0;

		for (int i = 0; i <= maxMapId; i++) {
			lineage.bean.lineage.Map m = list.get(i);

			if (m != null) {
				array[idx] = String.format("%d  %s", m.mapid, Util.getMapName(null, m.mapid));
				idx++;
			}
		}

		return array;
	}
	
	/**
	 * 읽어들인 맵정보들 순회하면서 맵아이디를 int로 변환해서 배열 리턴함.
	 * 
	 * @return
	 */
	static public int[] toIndexMap() {
		int[] array = new int[list.size()];
		
		int idx = 0;

		for (int i = 0; i <= maxMapId; i++) {
			lineage.bean.lineage.Map m = list.get(i);

			if (m != null) {
				array[idx] = m.mapid;
				idx++;
			}
		}

		return array;
	}

	static public void appendPersnalShop(PersnalShopInstance pc_shop) {
		if (pc_shop == null)
			return;

		synchronized (persnalShopInstance_list) {
			if (!persnalShopInstance_list.contains(pc_shop))
				persnalShopInstance_list.add(pc_shop);
		}
	}

	static public void removePersnalShop(PersnalShopInstance pc_shop) {
		if (pc_shop == null)
			return;

		synchronized (persnalShopInstance_list) {
			persnalShopInstance_list.remove(pc_shop);
		}
	}
	

	static public List<PersnalShopInstance> getPersnalShopList() {
		synchronized (persnalShopInstance_list) {
			return new ArrayList<PersnalShopInstance>(persnalShopInstance_list);
		}
	}

	static public PersnalShopInstance findPersnalShop(String name) {
		if (name == null)
			return null;

		// 찾을 객체 검색.
		for (PersnalShopInstance pc_shop : getPersnalShopList()) {
			if (name.equalsIgnoreCase(pc_shop.getName().replaceAll("", "")))//의 상점
				return pc_shop;
		}
		return null;
	}
	
	static public PersnalShopInstance findPersnalShop(long objid) {

		// 찾을 객체 검색.
		for (PersnalShopInstance pc_shop : getPersnalShopList()) {
			if (pc_shop.getPcObjectId() == objid)
				return pc_shop;
		}
		return null;
	}

	static public int getUserSize() {
		return pc_list.size() - (RobotController.getPcRobotListSize());
	}
	

	
	static public boolean getMapObject(int x, int y, int map){
		lineage.bean.lineage.Map m = list.get(map);
		List<object> list_object = new ArrayList<object>();
		list_object.clear();
		if(m!=null)
			m.getObjectList(x, y, map, list_object);
	
		if (list_object.size() > 0) 
			return true;
		
		return false;
	}
	static public PcInstance findGm() {
		// 찾을 객체 검색.
		synchronized (pc_list) {
			for (PcInstance pc : pc_list) {
				if (pc.getGm() > 0)
					return pc;
			}
			return null;
		}
	}
}
