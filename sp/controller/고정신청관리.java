package sp.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import lineage.share.TimeLine;
import lineage.world.object.instance.PcInstance;
import sp.bean.GojungMember;

public class 고정신청관리 {

	static final private String PATH = "db/";
	static final private String FILE_NAME = "gojung_member.data";
	private static Map<String, GojungMember> list;

	static public void init() {
		TimeLine.start("고정신청관리..");
		
		//
		list = new HashMap<String, GojungMember>();
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
					GojungMember c = new GojungMember();
					c.accountId = st.nextToken();
					c.cha_name = st.nextToken();
					c.hp = st.nextToken();
					c.date = Long.valueOf(st.nextToken());
					//
					list.put(c.accountId, c);
				}
				br.close();
			} catch (Exception e) { }
		}
		
		TimeLine.end();
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
				for(GojungMember c : list.values())
					fw.append( String.format("%s\t%s\t%s\t%d\r\n", c.accountId, c.cha_name, c.hp, c.date) );
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
	
	static public boolean isGojung(String accountId) {
		synchronized (list) {
			return list.containsKey(accountId);
		}
	}
	
	static public void append(PcInstance pc, String hp) {
		GojungMember g = new GojungMember();
		g.accountId = pc.getClient().getAccountId();
		g.cha_name = pc.getName();
		g.hp = hp;
		g.date = System.currentTimeMillis();
		synchronized (list) {
			list.put(g.accountId, g);
		}
	}

}
