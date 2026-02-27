package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import lineage.bean.database.SpriteFrame;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public final class SpriteFrameDatabase {

	static private Map<Integer, SpriteFrame> list;
	
	static public void init(Connection con){
		TimeLine.start("SpriteFrameDatabase..");
		
		list = new HashMap<Integer, SpriteFrame>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM sprite_frame");
			rs = st.executeQuery();
			while(rs.next()){
				int gfx = rs.getInt("gfx");

				SpriteFrame sf = list.get(gfx);
				if(sf == null){
					sf = new SpriteFrame();
					sf.setName(rs.getString("action_name"));
					sf.setGfx(gfx);
					list.put(gfx, sf);
				}
				
				sf.getList().put(rs.getInt("action"), rs.getInt("frame"));
				sf.getListString().put(rs.getString("action_name"), rs.getInt("frame"));
				sf.getListMode().put(rs.getString("action_name"), rs.getInt("action"));
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", SpriteFrameDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}

		TimeLine.end();
	}
	
	static public boolean find(int gfx) {
		SpriteFrame sf = list.get(gfx);
		return sf != null;
	}

	/**
	 * gfx에 해당하는action값에 프레임값을 리턴함.
	 *  : 고유 넘버로 찾음
	 */
	static public int find(int gfx, int action) {
		// 뽕데스는 프레임 무시
		if(gfx == 5641)
			return 0;
		//
		SpriteFrame sf = list.get(gfx);
		if(sf != null){
			Integer frame = sf.getList().get(action);
			if(frame != null){
				return frame.intValue();
			}
		}
		// 해당하는 모드가 없을경우 1초로 정의
		return 640; //640
	}
	static public int find(PcInstance pc, int gfx, int action) {
		// 뽕데스는 프레임 무시
		if(gfx == 5641)
			return 0;
		
		SpriteFrame sf = list.get(gfx);
		if(sf != null){
			Integer frame = sf.getList().get(action);
			double i = frame;
			if(frame != null){
				//기본 상태
				switch(pc.getSpeed()){
					case 1: //헤이스트 0이 보통
						i -= i*0.24; //224
						break;
					case 2: //슬로우
						i += i*0.23;
						break;
				}
				if(pc.isBrave()){
					if(action==0 && pc.getGfx()==4932)
						i -= i * 0.1;
					
					//다엘은 이속만 영향을 받는다.
					if(action==0 && (pc.getClassType()==0x04)){
						i -= i * 0.24;
					}else if(pc.getClassType()==0x04){
						
					}else{
						i -= i * 0.24;
					}
				}
				
				return (int)i;
			}
		}
		// 해당하는 모드가 없을경우 1초로 정의
		return 640;
	}
	
	/**
	 * gfx에 해당하는 action값 찾아서 프레임값 리턴.
	 *  : 문자열로 찾음.
	 * @param gfx
	 * @param mode
	 * @return
	 */
	static public int find(int gfx, String action_name) {
		// 뽕데스는 프레임 무시
		if(gfx == 5641)
			return 0;
		//
		SpriteFrame sf = list.get(gfx);
		if(sf!=null && action_name!=null && action_name.length()>0){
			Integer frame = sf.getListString().get(action_name);
			return frame==null ? 640 : frame.intValue();
		}
		// 해당하는 모드가 없을경우 2초로 정의
		return 640;
	}
	
	/**
	 * 액션이름에 해당하는 액션값 리턴.
	 * @param gfx
	 * @param mode
	 * @return
	 */
	static public int findMode(int gfx, String action_name){
		SpriteFrame sf = list.get(gfx);
		if(sf!=null && action_name!=null && action_name.length()>0){
			Integer action = sf.getListMode().get(action_name);
			return action==null ? 0 : action.intValue();
		}
		return -1;
	}
	
	static public int findMode2(int gfx, int action){
		SpriteFrame sf = list.get(gfx);
		
		
		if(sf!=null && action != 0){
			Integer act = sf.getListMode().get(action);
			return act==null ? 0 : act.intValue();
		}		
		return -1;
	}
	
	/**
	 * gfx에 해당 액션이 있는지 확인
	 * 
	 * @param
	 * @return 2017-09-02 by all_night.
	 */
	static public boolean findGfxMode(int gfx, int action) {
		return list.get(gfx).getList().get(action) != null;
	}
	
	static public int getSize(){
		return list.size();
	}
	/**
	 * gfx에 해당하는action값에 프레임값을 리턴함. PC를 제외한 모든 객체가 사용중
	 */
	static public int getGfxFrameTime(object o, int gfx, int action) {
		SpriteFrame spriteFrame = list.get(gfx);

		if (spriteFrame != null) {
			double frame = 0;
			Integer gfxFrame = spriteFrame.getList().get(action);

			if (gfxFrame != null)
				frame = gfxFrame.intValue();
			else
				return 1000;

			// 일반적인 촐기 용기 안한 상태
			if (o.getSpeed() == 0 && !o.isBrave())
				frame *= 42;
			// 촐기 또는 용기 상태
			else if ((o.getSpeed() == 1 && !o.isBrave()) || (o.getSpeed() == 0 && o.isBrave()))
				frame *= 31.5;
			// 촐기 용기 둘다
			else if (o.getSpeed() == 1 && o.isBrave())
				frame *= 23.5;
			// 슬로우 걸렸을 시, 촐기 용기 안한 상태
			else if (o.getSpeed() == 2 && !o.isBrave())
				frame *= 81;
			// 슬로우 걸렸을 시, 촐기 안한 상태
			else if (o.getSpeed() == 2 && o.isBrave())
				frame *= 61;

			frame *= 1;

			return (int) Math.round(frame);
		}
		// 해당하는 모드가 없을경우 1초로 정의
		return 1000;
	}
	
	/**
	 * gfx에 해당하는action값에 프레임값을 가져와서 캐릭터의 속도에 따라서 프레임에따른 시간 계산 스피드핵 체크에서 사용중
	 */
	static public double getSpeedCheckGfxFrameTime(object o, int gfx, int action) {
		SpriteFrame spriteFrame = list.get(gfx);
		// 해당 gfx가 없을경우 10 프레임으로 정의.
		double frame = 10;

		if (spriteFrame != null) {
			Integer gfxFrame = spriteFrame.getList().get(action);

			if (gfxFrame != null)
				frame = gfxFrame.intValue();
			else
				return 1000;

			// 일반적인 촐기 용기 안한 상태
			if (o.getSpeed() == 0 && !o.isBrave())
				frame *= 42;
			// 촐기 또는 용기 상태
			else if ((o.getSpeed() == 1 && !o.isBrave()) || (o.getSpeed() == 0 && o.isBrave()))
				frame *= 31.5;
			// 촐기 용기 둘다
			else if (o.getSpeed() == 1 && o.isBrave())
				frame *= 23.5;
			// 슬로우 걸렸을 시, 촐기 용기 안한 상태
			else if (o.getSpeed() == 2 && !o.isBrave())
				frame *= 81;
			// 슬로우 걸렸을 시, 촐기 안한 상태
			else if (o.getSpeed() == 2 && o.isBrave())
				frame *= 61;

			frame *= 1;
			
			frame /= 40;

			return Math.round(frame);
		}
		return 1000;
	}
	
	

	public static Map<Integer, SpriteFrame> getList() {
		return list;
	}

	public static void setList(Map<Integer, SpriteFrame> list) {
		SpriteFrameDatabase.list = list;
	}
	
	
}
