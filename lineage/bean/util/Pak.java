package lineage.bean.util;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.util.PakTools;

public class Pak {
	public int Offset;
	public String FileName;
	public int FileSize;
	public List<Image> frame = new ArrayList<Image>();	// 프레임 목록
	public int frameIdx = 0;							// 최근에 출력된 프레임 위치
	public long frameTime = 0;							// 최근에 출력된 프레임 시간
	public Map<Integer, Image> tile = new HashMap<Integer, Image>();	// 
	public Map<Integer, Integer> tile_heading = new HashMap<Integer, Integer>();	// 
	
	public Pak(byte[] data, int index) {
		this.Offset = PakTools.ToInt32(data, index);
		this.FileName = new String(data, index+4, 20).trim();
		this.FileSize = PakTools.ToInt32(data, index+24);
	}

	public Pak(String filename, int size, int offset) {
		this.Offset = offset;
		this.FileName = filename;
		this.FileSize = size;
	}
}
