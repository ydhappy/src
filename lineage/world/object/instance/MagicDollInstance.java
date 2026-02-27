package lineage.world.object.instance;

import java.util.List;

import lineage.bean.database.MagicdollList;
import lineage.bean.lineage.Doll;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectAdd;
import lineage.network.packet.server.S_ObjectRemove;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.AStar;
import lineage.world.Node;
import lineage.world.World;
import lineage.world.object.Character;
import lineage.world.object.object;

public class MagicDollInstance extends Character {
	
	static synchronized public MagicDollInstance clone(MagicDollInstance mdi, Doll doll, MagicdollList mdl){
		if(mdi == null)
			mdi = new MagicDollInstance();
		// 걷기모드로 변경.
		mdi.setAiStatus(0);
		// 소환된 시간과 종료될 시간 처리하기.
		mdi.time_start = System.currentTimeMillis();
		mdi.time_end = mdi.time_start + (1000*mdl.getDollContinuous());
		//
		mdi.doll = doll;
		mdi.mdl = mdl;
		//
		return mdi;
	}
	
	private Doll doll;
	private MagicdollList mdl;
	private long time_start;	// 소환된 시간
	private long time_end;		// 종료될 시간.
	private AStar aStar;					// 길찾기 변수
	private Node tail;						// 길찾기 변수
	private int[] iPath;					// 길찾기 변수
	private int temp_idx;	// 
	
	public MagicDollInstance() {
		aStar = new AStar();
		iPath = new int[2];
	}
	

	public void setTime(int time) {
		if(time>0)
			time_end = System.currentTimeMillis() + (time*1000);
		else
			time_end = time;
	}


	public int getTime() {
		return time_end>0 ? (int)((time_end-System.currentTimeMillis())*0.001) : (int)time_end;
	}
	
	public long getTimeEnd() {
		return time_end;
	}
	
	public long getTimeStart() {
		return time_start;
	}
	
	public MagicdollList getMDL() {
		return mdl;
	}
	
	public Doll getDoll() {
		return doll;
	}
	
	@Override
	public void close() {
		super.close();
		doll = null;
	}

	@Override
	public void setInvis(boolean invis) {
		//
		if(isInvis() == invis)
			return;
		//
		super.setInvis(invis);
		if(!worldDelete) {
			if(isInvis())
				toSender(S_ObjectRemove.clone(BasePacketPooling.getPool(S_ObjectRemove.class), this), false);
			else
				toSender(S_ObjectAdd.clone(BasePacketPooling.getPool(S_ObjectAdd.class), this, this), false);
		}
	}
	
	@Override
	public void toMoving(final int x, final int y, final int h) {
		if(isInvis() == false) {
			super.toMoving(x, y, h);
			return;
		}
		// 좌표 변경.
		this.x = x;
		this.y = y;
		this.heading = h;
		// 주변객체 갱신
		if( !Util.isDistance(tempX, tempY, map, x, y, map, Lineage.SEARCH_LOCATIONRANGE) ){
			tempX = x;
			tempY = y;
			// 이전에 관리중이던 목록 갱신
			List<object> temp = getAllList(true);
			clearAllList();
			for(object o : temp)
				o.removeAllList(this);
			// 객체 갱신
			temp.clear();
			World.getLocationList(this, Lineage.SEARCH_WORLD_LOCATION, temp);
			for(object o : temp){
				if(isList(o)){
					// 전체 관리목록에 등록.
					appendAllList(o);
					o.appendAllList(this);
				}
			}
		}
	}
	
	/**
	 * 랜덤워킹 처리 함수.
	 */
	@Override
	protected void toAiWalk(long time){
		super.toAiWalk(time);
		
		// 주인 따라다니기.
		if(!Util.isDistance(this, doll.getMaster(), Lineage.magicdoll_location)) {
			aStar.cleanTail();
			tail = aStar.searchTail(this, doll.getMaster().getX(), doll.getMaster().getY(), false);
			if(tail != null){
				while(tail != null){
					// 현재위치 라면 종료
					if(tail.x==getX() && tail.y==getY())
						break;
					//
					iPath[0] = tail.x;
					iPath[1] = tail.y;
					tail = tail.prev;
				}
				toMoving(iPath[0], iPath[1], Util.calcheading(this.x, this.y, iPath[0], iPath[1]));
				//축인형이펙트
				//if(this.getGfx()>=17000)
				//toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, 17633), true);
			}
		} else {
			if(Util.random(0, 60) == 0)
				toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), this, Util.random(66, 67)), false);
//			 time = Math.round(time * 0.001);
//			 if(time%2 != 0)
//			 return;
//			 toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, 17635), true);
//			 toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, 17632), true);
		}
	}

	@Override
	public void toTeleport(int x, int y, int map, boolean effect) {
		//
		setSpeed(1);
		setBrave(true);
		//
		super.toTeleport(x, y, map, effect);
	}
}
