package lineage.share;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Zone {

	static private Map<Integer, String> list = new HashMap<Integer, String>();
	
	static public void init() {
		try {
			BufferedReader lnrr = new BufferedReader( new FileReader("resource/zone.txt"));
			String line;
			while((line = lnrr.readLine()) != null) {
				// "말하는 섬" 0 0 0 0 0 0 ;Talking Island
				if(!line.startsWith("\""))
					continue;
				
				String value = line.substring(1, line.indexOf("\"", 1));
				String[] lines = line.substring(value.length()+1).trim().split(" ");
				
				//
				list.put(Integer.valueOf(lines[2]), value);
			}
			lnrr.close();
		} catch (Exception e) {
			lineage.share.System.printf("%s : init()\r\n", Desc.class.toString());
			lineage.share.System.println(e);
		}
	}
	
	static public String find(int mapId) {
		if(mapId == 4)
			return "본토";
		if(mapId == 800)
			return "시장";
		return list.get(mapId);
	}

}
