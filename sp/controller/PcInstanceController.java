package sp.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import lineage.share.TimeLine;

public class PcInstanceController {

	static final private String PATH = "db/";
	static final private String FILE_NAME = "pcinstance.data";
	static private Map<Long, Map<String, String>> list;
	
	static public void init() {
		// 
		//TimeLine.start("PcInstanceController..");
		
		list = new HashMap<Long, Map<String, String>>();
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
					
					// 000000	key|value	key|value	key|value....
					StringTokenizer st = new StringTokenizer(line, "\t");
					long objId = Long.valueOf(st.nextToken());
					Map<String, String> db = new HashMap<String, String>();
					while(st.hasMoreTokens()) {
						String[] dbs = st.nextToken().split("|");
						db.put(dbs[0], dbs[1]);
					}
					list.put(objId, db);
				}
				br.close();
			} catch (Exception e) { }
		}
		
		//TimeLine.end();
	}
	
	static public void close() {
		toSave();
		synchronized (list) {
			list.clear();
		}
	}
	
	static private void toSave() {
		synchronized (list) {
			// 파일 쓰기.
			dirs();
			try {
				FileWriter fw = new FileWriter(PATH + FILE_NAME, false);
				for(long objId : list.keySet()) {
					StringBuffer sb = new StringBuffer();
					for(String key : list.get(objId).keySet())
						sb.append(key).append("|").append(list.get(objId).get(key)).append("\t");
					fw.append( String.format("%s\r\n", sb.toString()) );
				}
				fw.flush();
				fw.close();
			} catch (Exception e) { }
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
	
	static public String get(long objId, String key) {
		synchronized (list) {
			Map<String, String> db = list.get(objId);
			if(db == null)
				return null;
			return db.get(key);
		}
	}
	
	static public void set(long objId, String key, String value) {
		synchronized (list) {
			Map<String, String> db = list.get(objId);
			if(db == null) {
				db = new HashMap<String, String>();
				list.put(objId, db);
			}
			db.put(key, value);
		}
	}
	
	static public int getInt(long objId, String key) {
		String value = get(objId, key);
		try {
			return Integer.valueOf(value);
		} catch (Exception e) { }
		return 0;
	}
	
	static public void setInt(long objId, String key, int value) {
		set(objId, key, String.valueOf(value));
	}
	
}
