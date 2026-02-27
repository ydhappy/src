package lineage.share;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Desc {

	static private Map<Integer, String> list = new HashMap<Integer, String>();
	
	static public void init() {
		try {
			BufferedReader lnrr = new BufferedReader( new FileReader("resource/desc-k.tbl"));
			String line;
			int line_cnt = Integer.valueOf(lnrr.readLine());
			int idx = 0;
			while((line = lnrr.readLine()) != null)
				list.put(idx++, line);
			lnrr.close();
		} catch (Exception e) {
			lineage.share.System.printf("%s : init()\r\n", Desc.class.toString());
			lineage.share.System.println(e);
		}
	}
	
	static public String find(String nameid) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(nameid, "$");
		while(st.hasMoreTokens()) {
			String name = st.nextToken().trim();
			if(name.startsWith("$"))
				name = name.substring(1);
			sb.append( list.get(Integer.valueOf(name)) ).append(" ");
		}
		return sb.toString().trim();
	}
	
}
