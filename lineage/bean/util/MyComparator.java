package lineage.bean.util;

import java.util.Comparator;

public class MyComparator implements Comparator<String> {
    @Override
	public int compare(String key1, String key2) {
    	String[] keys1 = key1.split(",");
    	String[] keys2 = key2.split(",");
    	int c = Long.valueOf(keys1[0]).compareTo(Long.valueOf(keys2[0]));
    	
    	if(keys1[1].equalsIgnoreCase(keys2[1]))
    		return 0;
    	return c==0 ? 2 : c;
    }
}