package sp.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import sp.bean.ClanAge;

public class ClanController {

	static final private String PATH = "db/";
	static final private String FILE_NAME = "clan_age.data";
	private static Map<String, List<ClanAge>> list;

	static public void init() {
	//	TimeLine.start("ClanController..");
		
		//
		list = new HashMap<String, List<ClanAge>>();
		//
		dirs();
		File file = new File( PATH + FILE_NAME );
		if(file.isFile()) {
			//
			try {
				BufferedReader br = new BufferedReader( new FileReader(file));
				String line = null;
				while ( (line=br.readLine()) != null) {
					if(line.startsWith("#"))
						continue;
					StringTokenizer st = new StringTokenizer(line, "\t");
					ClanAge c = new ClanAge();
					c.key = st.nextToken();
					c.name = st.nextToken();
					c.age = Integer.valueOf(st.nextToken());
					//
					List<ClanAge> age_list = list.get(c.key);
					if(age_list == null) {
						age_list = new ArrayList<ClanAge>();
						list.put(c.key, age_list);
					}
					//
					age_list.add( c );
				}
				br.close();
			} catch (Exception e) { }
		}
		
		//TimeLine.end();
	}
	
	/**
	 * 종료처리 함수.
	 */
	static public void close() {
		synchronized (list) {
			// 파일 쓰기.
			dirs();
			try {
				FileWriter fw = new FileWriter(PATH + FILE_NAME, false);
				for(List<ClanAge> age_list : list.values()) {
					for(ClanAge c : age_list)
						fw.append( String.format("%s\t%s\t%d\r\n", c.key, c.name, c.age) );
				}
				fw.flush();
				fw.close();
			} catch (Exception e) { }
			//
			list.clear();
		}
	}
	
	/**
	 * 파일처리에 필요한 디렉토리 생성.
	 */
	static private void dirs() {
		File file = new File( PATH );
		if(!file.isDirectory())
			file.mkdirs();
	}
	
	/**
	 * 나이 설정한거 초기화.
	 * @param o
	 */
	static public void clear(object o) {
		if(o.getClanId() == 0) {
			ChattingController.toChatting(o, "혈맹이 존재하지 않습니다.", 20);
			return;
		}
		lineage.bean.lineage.Clan clan = lineage.world.controller.ClanController.find(o.getClanName());
		if(clan==null || !clan.getLord().equalsIgnoreCase(o.getName())) {
			ChattingController.toChatting(o, "군주만이 해당 명령어를 사용할 수 있습니다.", 20);
			return;
		}
		synchronized (list) {
			List<ClanAge> age_list = list.get(o.getClanName());
			if(age_list == null) {
				age_list = new ArrayList<ClanAge>();
				list.put(o.getClanName(), age_list);
			}
			age_list.clear();
			ChattingController.toChatting(o, "혈맹원들에 나이가 초기화 되었습니다.", 20);
		}
	}
	
	/**
	 * 나이설정
	 * @param o
	 * @param name
	 * @param age
	 */
	static public void setAge(object o, String name, Integer age) {
		if(o.getClanId() == 0) {
			ChattingController.toChatting(o, "혈맹이 존재하지 않습니다.", 20);
			return;
		}
		if(name.length() == 0) {
			ChattingController.toChatting(o, "혈맹원에 이름을 입력하여 주십시오.", 20);
			return;
		}
		if(age <= 0) {
			ChattingController.toChatting(o, "혈맹원에 이름이 잘못 되었습니다.", 20);
			return;
		}
		lineage.bean.lineage.Clan clan = lineage.world.controller.ClanController.find(o.getClanName());
		if(clan==null || (clan.containsMemberList(name)==false && !clan.getLord().equalsIgnoreCase(o.getName()))) {
			ChattingController.toChatting(o, String.format("'%s'님은 '%s'혈맹소속이 아닙니다.", name, o.getClanName()), 20);
			return;
		}
//		if(!clan.getLord().equalsIgnoreCase(o.getName())) {
//			ChattingController.toChatting(o, "나이설정은 군주만 가능합니다.", 20);
//			return;
//		}
		
		synchronized (list) {
			//
			List<ClanAge> age_list = list.get(o.getClanName());
			if(age_list == null) {
				age_list = new ArrayList<ClanAge>();
				list.put(o.getClanName(), age_list);
			}
			//
			ClanAge c = null;
			for(ClanAge ca : age_list) {
				if(ca.name.equalsIgnoreCase(name)) {
					c = ca;
					break;
				}
			}
			if(c == null) {
				c = new ClanAge();
				age_list.add( c );
			}
			//
			c.key = o.getClanName();
			c.name = name;
			c.age = age;
			//
			ChattingController.toChatting(o, String.format("'%s' 님에 나이가 '%d' 로 설정 되었습니다.", name, age), 20);
		}
	}
	
	static public String getAgeList(object o) {
		if(o.getClanId() == 0) {
			ChattingController.toChatting(o, "혈맹이 존재하지 않습니다.", 20);
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		synchronized (list) {
			//
			List<ClanAge> age_list = list.get(o.getClanName());
			if(age_list == null) {
				age_list = new ArrayList<ClanAge>();
				list.put(o.getClanName(), age_list);
			}
			sb.append("---------------------------").append("\r\n");
			sb.append(String.format("'%s'혈맹에 나이목록 입니다.", o.getClanName())).append("\r\n");
			for(ClanAge c : age_list)
				sb.append(String.format(" : %s (%d)", c.name, c.age)).append("\r\n");
			sb.append("---------------------------").append("\r\n");
		}
		return sb.toString();
	}
	
	static public Integer getAge(object o) {
		//
		if(o.getClanId() == 0)
			return 0;
		//
		synchronized (list) {
			//
			List<ClanAge> age_list = list.get(o.getClanName());
			if(age_list == null) {
				age_list = new ArrayList<ClanAge>();
				list.put(o.getClanName(), age_list);
			}
			for(ClanAge c : age_list) {
				if(c.name.equalsIgnoreCase(o.getName()))
					return c.age;
			}
		}
		return 0;
	}
	
}
