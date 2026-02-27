package sp.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import lineage.share.TimeLine;
import lineage.util.Util;

public class FishingController {

	static final private String PATH = "db/";
	static final private String FILE_NAME = "fishing.data";
	static private Map<String, Long> list;
	
	static public void init() {
		TimeLine.start("FishingController..");
		
		//
		list = new HashMap<String, Long>();
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
					String ip = st.nextToken();
					String time = st.nextToken();
					list.put(ip, Long.valueOf(time));
				}
				br.close();
			} catch (Exception e) { }
		}
		
		TimeLine.end();
	}
	
	static public void close() {
		synchronized (list) {
			// 파일 쓰기.
			dirs();
			try {
				FileWriter fw = new FileWriter(PATH + FILE_NAME, false);
				for(String ip : list.keySet())
					fw.append( String.format("%s\t%s\r\n", ip, String.valueOf(list.get(ip))) );
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
	
	static public long find(String ip) {
		synchronized (list) {
			Long time = list.get(ip);
			if(time != null)
				return time;
		}
		return 0L;
	}
	
	static public boolean isBaitBuy(String ip) {
		long time = find(ip);
		// 최근에 미끼를 구입한 시간에 일자와 현재 시간에 일자가 동일하면 무시하기.
		if(time>0 && Util.getDate(time)==Util.getDate(System.currentTimeMillis()))
			return false;
		//
		return true;
	}
	
	static public void update(String ip) {
		System.out.println("update. " + ip);
		synchronized (list) {
			list.put(ip, System.currentTimeMillis());
		}
	}
	
}
