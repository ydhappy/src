package lineage.util;

import java.awt.Image;
import java.util.Map;

import lineage.bean.util.Pak;
import lineage.network.packet.ClientBasePacket;
import lineage.share.TimeLine;

public class TileTools {

	private static Map<String, Pak> list;

	public static void init() throws Exception {
		TimeLine.start("TileTools..");
		
//		// 파일 읽기.
//		BufferedInputStream bis = new BufferedInputStream(new FileInputStream("resource/Tile.idx"));
//		byte[] data = new byte[bis.available()];
//		bis.read(data, 0, data.length); 
//		bis.close();
//		//
//		bis = new BufferedInputStream(new FileInputStream("resource/Tile.pak"));
//		byte[] pak = new byte[bis.available()];
//		bis.read(pak, 0, pak.length); 
//		bis.close();
//		//
//		int num = 4;
//		int num2 = (data.length - num) / 28;
//		list = new HashMap<String, Pak>();
//		for (int i = 0; i<num2; i++) {
//			Pak p = new Pak(data, num + (i*28));
//			list.put(p.FileName.toLowerCase(), p);
//			if(p.FileName.toLowerCase().endsWith(".til")) {
//				try {
//					//
//					byte[] temp = new byte[p.FileSize];
//					System.arraycopy(pak, p.Offset, temp, 0, p.FileSize);
//					//
//					toTile(p, temp);
//				} catch (Exception e) { }
//			}
//		}
		
		TimeLine.end();
	}
	
	public static Image getTile(int num, int idx) {
		Pak p = find( String.format("%d.til", num) );
		if(p != null)
			return p.tile.get(idx);
		return null;
	}
	
	private static Pak find(String key) {
		return list.get(key.toLowerCase());
	}
	
	private static void toTile(Pak p, byte[] data) {
		//
		ClientBasePacket cbp = (ClientBasePacket)ClientBasePacket.clone(null, data, data.length);
		cbp.seek(0);
		int num = cbp.readH();
		cbp.readH();
        int[] numArray = new int[num + 1];
        int num2 = 4 + (numArray.length * 4);
        for (int i=0 ; i<=num ; i++)
            numArray[i] = num2 + (int)cbp.readD();
        for (int i=0 ; i<num ; i++) {
        	cbp.seek(numArray[i]);
        	int heading = cbp.readC();
    		byte[] tile = cbp.readB(numArray[i+1] - cbp.readIndex());
    		try {
    			System.out.println( p.FileName+" : "+tile.length );
    			System.out.println( Util.printData(tile, tile.length) );
//    			Image img = SpriteTools.toTBT(p, tile);
//    			if(img != null) {
//    				p.tile.put(i, img);
//    				p.tile_heading.put(i, heading);
//    			}
    			
    		} catch (Exception e) {
				//System.out.println(i);
				//System.out.println(" : "+e);
    		}
        }
	}
		
//		// test
//		BufferedInputStream bis = new BufferedInputStream(new FileInputStream("resource/144/15/7fff8000.seg"));
//		byte[] data = new byte[bis.available()];
//		bis.read(data, 0, data.length); 
//		bis.close();
//		
//		ClientBasePacket cbp = (ClientBasePacket)ClientBasePacket.clone(null, data, data.length);
//		cbp.seek(0);
//		
		// seg와 s32는 구조가 다름.
//		// seg 방식
//		// 타일정보
//		for(; cbp.readIndex()<128*128 ;) {
//			cbp.readC();
//		}
//		// 몰라
//		int b_size = cbp.readH();
//		for(int i=0 ; i<b_size ; ++i) {
//			cbp.readD();
//		}
//		// 이동가능여부 (0, 1로 구성됨)
//		for(int i=0 ; i<(128*128)/2 ; ++i) {
//			cbp.readC();
//		}
//		// ?
//		cbp.readH();	<< 아래 e그룹 읽을 최대갯수. -1 한 만큼. 0B면 0A까지 존재.
//		cbp.readD();
//		// d 그룹 (seq는 5개씩 s32는 7개씩)
//		int c_size = cbp.readH();
//		for(int i=0 ; i<c_size ; ++i) {
//			cbp.readH();
//			cbp.readH();
//			cbp.readC();
//		}
//		// e 그룹 01~0A 까지 잇음.(idx) (seq는 5개씩 s32는 7개씩)
//		// 0B 도 있는데?
//		int e_idx = cbp.readH();
//		int e_size = cbp.readH();
//		for(int i=0 ; i<e_size ; ++i) {
//			cbp.readH();
//			cbp.readH();
//			cbp.readC();
//		}
		// f
		// int f_size = dbp.readD();
//		for(int i=0 ; i<f_size ; ++i) {
//			cbp.readC();
//			cbp.readC();
//			cbp.readC();
//			cbp.readC();
//			cbp.readC();
//		}
		// g 모르겟넴~ 아래는 s32방식임~ seq는 모르겠음~~
		// int g_size = dbp.readD();
//		for(int i=0 ; i<f_size ; ++i) {
//			cbp.readC();
//			cbp.readC();
//			cbp.readC();
//			cbp.readC();
//		}
//		System.out.println(cbp.readIndex());
		
		// s32 방식
	
}
