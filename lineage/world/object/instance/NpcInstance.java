package lineage.world.object.instance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Npc;
import lineage.bean.lineage.Inventory;
import lineage.database.DatabaseConnection;
import lineage.database.ServerDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectAttack;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectHeading;
import lineage.network.packet.server.S_ObjectHitratio;
import lineage.share.Lineage;
import lineage.share.System;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.DamageController;
import lineage.world.controller.InventoryController;
import lineage.world.object.Character;
import lineage.world.object.object;

public class NpcInstance extends Character {

	protected Npc npc;
	protected List<object> attackList;		// 전투 목록
	protected int ai_walk_stay_count;		// 랜덤워킹 중 잠시 휴식을 취하기위한 카운팅 값
	protected boolean ai_talk;				// 대화를 걸었는지 여부 확인용.
	private int reSpawnTime;				// 재스폰 하기위한 대기 시간값.
	// 시체유지(toAiCorpse) 구간에서 사용중.
	// 재스폰대기(toAiSpawn) 구간에서 사용중.
	private long ai_time_temp_1;
	// 인벤토리
	private Inventory inv;
	//
	private List<object> temp_list;		// 주변셀 검색에 임시 담기용으로 사용.

	public NpcInstance(Npc npc){
		this.npc = npc;
		attackList = new ArrayList<object>();
		temp_list = new ArrayList<object>();
		// 인벤토리 활성화를 위해.
		InventoryController.toWorldJoin(this);
	}
	
	@Override
	public void close(){
		super.close();
		
		ai_time_temp_1 = reSpawnTime = 0;
		if(attackList != null) {
			synchronized (attackList) {
				attackList.clear();
			}
		}
		if(temp_list != null)
			temp_list.clear();
	}
	
	public void removeAttackList(object o){
		synchronized (attackList) {
			attackList.remove(o);
		}
	}
	
	private void appendAttackList(object o){
		synchronized (attackList) {
			if(!attackList.contains(o))
				attackList.add(o);
		}
	}
	
	private List<object> getAttackList(){
		synchronized (attackList) {
			return new ArrayList<object>(attackList);
		}
	}

	public boolean containsAttackList(object o){
		synchronized (attackList) {
			return attackList.contains(o);
		}
	}
	
	@Override
	public Npc getNpc(){
		return npc;
	}
	
	@Override
	public Inventory getInventory(){
		return inv;
	}
	
	@Override
	public void setInventory(Inventory inv){
		this.inv = inv;
	}

	@Override
	public void setDead(boolean dead) {
		super.setDead(dead);
		if(dead){
			ai_time = SpriteFrameDatabase.find(gfx, gfxMode+8);
			setAiStatus(2);
		}
	}

	public int getReSpawnTime() {
		return reSpawnTime;
	}

	@Override
	public void setReSpawnTime(int reSpawnTime) {
		this.reSpawnTime = reSpawnTime;
	}

	@Override
	public void toDamage(Character cha, int dmg, int type, Object...opt){
		// 공격목록에 추가.
		addAttackList(cha);
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		// ai_talk true로 변경해야 도망가지 않고 해당 stay_count를 0까지 완료함.
		ai_talk = true;
		ai_walk_stay_count = Lineage.npc_talk_stay_time;
		// pc 쪽으로 방향 전환.
		setHeading(Util.calcheading(this, pc.getX(), pc.getY()));
		toSender(new S_ObjectHeading(this), false);
		//
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM npc_html WHERE npc_name_id=? ORDER BY uid ASC");
			st.setString(1, getName());
			rs = st.executeQuery();
			if (rs.next()) {
				pc.toSender(new S_Html( this, rs.getString("html")));
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : toTalk(PcInstance pc, ClientBasePacket cbp)\r\n", BackgroundInstance.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	@Override
	public void setNowHp(int nowHp){
		super.setNowHp(nowHp);
		if (!worldDelete || Lineage.server_version <= 144)
			return;
		for (object o : getInsideList(true)) {
			if (o instanceof PcInstance) {
				if (o.getGm() > 0 || Lineage.npc_interface_hpbar)
					o.toSender(new S_ObjectHitratio(this, true));
			}
		}
	}
	
	/**
	 * 공격자 목록에 등록처리 함수.
	 * @param o
	 */
	public void addAttackList(object o){
		if(o == null)
			return;
		
		if(!o.isDead() && o.getObjectId()!=getObjectId())
			appendAttackList(o);
	}
	
	/**
	 * 전투목록에서 원하는 위치에있는 객체 찾아서 리턴.
	 * @param index
	 * @return
	 */
	protected object getAttackList(int index){
		try {
			if(attackList.size()>index) {
				synchronized (attackList) {
					return attackList.get(index);
				}
			}
		} catch (Exception e) {}
		return null;
	}
	
	@Override
	public void toAttack(object o, int x, int y, boolean bow, int gfxMode, int alpha_dmg, boolean isTriple, int tripleIdx){
		int effect = bow ? npc.getArrowGfx() != 0 ? npc.getArrowGfx() : 66 : 0;
		int dmg = DamageController.getDamage(this, o, bow, null, null, alpha_dmg, isTriple, tripleIdx);
		DamageController.toDamage(this, o, dmg, bow ? Lineage.ATTACK_TYPE_BOW : Lineage.ATTACK_TYPE_WEAPON);
		toSender(new S_ObjectAttack(this, o, gfxMode, dmg, effect, bow, true, x, y, true), false);
	}
	
	/**
	 * 해당객체를 공격해도 되는지 분석하는 함수.
	 * @param o
	 * @param walk	: 랜덤워킹 상태 체크인지 구분용
	 * @return
	 */
	protected boolean isAttack(object o, boolean walk){
		if(o == null)
			return false;
		if(o.isDead())
			return false;
		if(o.isWorldDelete())
			return false;
		if(o.getGm()>0)
			return false;
		if(o.isInvis())
			return false;
		if(o.isTransparent())
			return false;
		if(!Util.isDistance(this, o, Lineage.SEARCH_MONSTER_TARGET_LOCATION))
			return false;
		return true;
	}
	
	@Override
	public void toAi(long time) {
		switch (ai_status) {
		// 공격목록이 발생하면 공격모드로 변경. 대신 대화걸린 상태가 아닐때만.
			case Lineage.AI_STATUS_WALK:
				if (ai_talk == false && attackList.size() > 0)
					setAiStatus(Lineage.AI_STATUS_ATTACK);
				break;

			// 전투 처리부분은 항상 타켓들이 공격가능한지 확인할 필요가 있음.
			case Lineage.AI_STATUS_ATTACK:
			case Lineage.AI_STATUS_ESCAPE:
				// 공격자 목록 체크해서 목록에서 제거하는 구간인데..
				// 피케이들은 여기서 제거가 안되서 계속 경비병이 피커를 공격함.(마을이고, 12셀 안에 있을때.)
				// 개선해야함 - psjump
				for (object o : getAttackList()) {
					if (!isAttack(o, false))
						removeAttackList(o);
				}
				// 전투목록이 없을경우 랜덤워킹으로 변경. 누군가 나에게 대화를 걸었을경우 랜덤워킹으로 변경.
				if (ai_talk || attackList.size() == 0)
					setAiStatus(Lineage.AI_STATUS_WALK);
				break;
		}

		super.toAi(time);
	}
	
	@Override
	protected void toAiWalk(long time) {
		super.toAiWalk(time);
		// 멘트
		toMent(time);
		// 아직 휴식카운팅값이 남앗을경우 리턴.
		if (ai_walk_stay_count-- > 0)
			return;

		// 대화를 걸었는지 여부 확인하는 변수 초기화.
		if (ai_talk)
			ai_talk = false;

		do {
			switch (Util.random(0, 5)) {
				case 0:
					ai_walk_stay_count = Util.random(5, 10);
					break;
				case 1:
				case 2:
					setHeading(getHeading() + 1);
					break;
				case 3:
				case 4:
					setHeading(getHeading() - 1);
					break;
				default:
					setHeading(Util.random(0, 7));
					break;
			}
			// 해당좌표 멀어지면, 자기자리로 텔레포트.
			if (!Util.isDistance(x, y, map, homeX, homeY, homeMap, Lineage.SEARCH_MONSTER_TARGET_LOCATION))
				toTeleport(homeX, homeY, homeMap, true);
			// 이동 좌표 추출.
			int x = Util.getXY(heading, true) + this.x;
			int y = Util.getXY(heading, false) + this.y;
			// 스폰된 위치에서 너무 벗어낫을경우 스폰쪽으로 유도하기.
			if (!Util.isDistance(x, y, map, homeX, homeY, homeMap, Lineage.SEARCH_LOCATIONRANGE)) {
				heading = Util.calcheading(this, homeX, homeY);
				x = Util.getXY(heading, true) + this.x;
				y = Util.getXY(heading, false) + this.y;
			}
			// 해당 좌표 이동가능한지 체크.
			boolean tail = World.isThroughObject(this.x, this.y, this.map, heading) && World.get_map(x, y, map)!=127;
			// 해당좌표에 객체 있는지 확인.
			boolean obj = false;
			temp_list.clear();
			findInsideList(x, y, temp_list);
			for (object o : temp_list) {
				if (o instanceof Character) {
					obj = true;
					break;
				}
			}
			if (tail && !obj) {
				// 타일이 이동가능하고 객체가 방해안하면 이동처리.
				super.toMoving(x, y, heading);
			} else {
				if (Util.random(0, 3) != 0)
					continue;
			}
		} while (false);
	}

	@Override
	public void toAiAttack(long time) {
		// 전투상태로 전환되면 도망모드로 다시 변경.
		setAiStatus(Lineage.AI_STATUS_ESCAPE);
	}

	@Override
	public void toAiEscape(long time) {
		super.toAiEscape(time);
		// 멘트
		toMent(time);
		// 전투목록에서 가장 근접한 사용자 찾기.
		object o = null;
		for (object oo : getAttackList()) {
			if (o == null)
				o = oo;
			else if (Util.getDistance(this, oo) < Util.getDistance(this, o))
				o = oo;
		}

		// 못찾앗을경우 무시. 가끔생길수 잇는 현상이기에..
		if (o == null) {
			setAiStatus(Lineage.AI_STATUS_WALK);
			return;
		}

		// 반대방향 이동처리.
		heading = Util.oppositionHeading(this, o);
		int temp_heading = heading;
		do {
			// 이동 좌표 추출.
			int x = Util.getXY(heading, true) + this.x;
			int y = Util.getXY(heading, false) + this.y;
			// 해당 좌표 이동가능한지 체크.
			boolean tail = World.isThroughObject(this.x, this.y, this.map, heading) && World.get_map(x, y, map)!=127;
			// 해당좌표에 객체 있는지 확인.
			boolean obj = false;
			temp_list.clear();
			findInsideList(x, y, temp_list);
			for (object oo : temp_list) {
				if (oo instanceof Character) {
					obj = true;
					break;
				}
			}
			if (tail && !obj) {
				// 타일이 이동가능하고 객체가 방해안하면 이동처리.
				super.toMoving(x, y, heading);
				break;
			} else {
				setHeading(heading + 1);
				if (temp_heading == heading)
					break;
			}
		} while (true);
	}

	@Override
	protected void toAiDead(long time) {
		super.toAiDead(time);
		// 멘트
		toMent(time);
		// 아이템 드랍
		int x = 0;
		int y = 0;
		for (ItemInstance ii : inv.getList()) {
			if (ii.getObjectId() == 0)
				ii.setObjectId(ServerDatabase.nextItemObjId());
			x = Util.random(getX() - 1, getX() + 1);
			y = Util.random(getY() - 1, getY() + 1);

			if (World.isThroughObject(x, y, map, 0) && World.get_map(x, y, map)!=127)
				ii.toTeleport(x, y, map, false);
			else
				ii.toTeleport(this.x, this.y, map, false);
			// 드랍됫다는거 알리기.
			ii.toDrop(this);
		}
		inv.clearList();
		// 전투 관련 변수 초기화.
		synchronized (attackList) {
			attackList.clear();
		}
		// 상태 변환
		setAiStatus(Lineage.AI_STATUS_CORPSE);
	}

	@Override
	protected void toAiCorpse(long time) {
		super.toAiCorpse(time);

		if (ai_time_temp_1 == 0)
			ai_time_temp_1 = time;

		// 시체 유지
		if (ai_time_temp_1 + Lineage.ai_corpse_time > time)
			return;

		ai_time_temp_1 = 0;
		// 버프제거
		toReset(true);
		// 시체 제거
		World.remove(this);
		clearList(true);
		// 상태 변환.
		setAiStatus(Lineage.AI_STATUS_SPAWN);
	}

	@Override
	protected void toAiSpawn(long time) {
		super.toAiSpawn(time);

		// 리스폰값이 정의되어 있지않다면 재스폰 할 필요 없음.
		if (reSpawnTime == 0) {
			toAiThreadDelete();
			return;
		}

		if (ai_time_temp_1 == 0)
			ai_time_temp_1 = time;
		// 스폰 대기.
		if (ai_time_temp_1 + reSpawnTime > time) {

		} else {
			ai_time_temp_1 = 0;
			// 상태 변환
			setDead(false);
			setNowHp(getMaxHp());
			setNowMp(getMaxMp());
			// 스폰
			toTeleport(homeX, homeY, homeMap, false);
			// 멘트
			toMent(time);
			// 상태 변환.
			setAiStatus(Lineage.AI_STATUS_WALK);
		}
	}

	/**
	 * 인공지능 처리구간에서 주기적으로 호출됨. : 상태에 맞는 멘트를 구사할지 여부 확인하여 표현 처리하는 함수.
	 * 
	 * @param time
	 *        : 진행되고있는 시간.
	 */
	protected void toMent(long time) {
		try {
			int msgTime = -1;
			int msgSize = 0;
			List<String> msgList = null;
			switch (ai_status) {
				case Lineage.AI_STATUS_WALK:
					msgTime = npc.getMsgWalkTime();
					msgSize = npc.getMsgWalk().size();
					msgList = npc.getMsgWalk();
					break;
				case Lineage.AI_STATUS_ATTACK:
					msgTime = npc.getMsgAtkTime();
					msgSize = npc.getMsgAtk().size();
					msgList = npc.getMsgAtk();
					break;
				case Lineage.AI_STATUS_DEAD:
					msgTime = npc.getMsgDieTime();
					msgSize = npc.getMsgDie().size();
					msgList = npc.getMsgDie();
					break;
				case Lineage.AI_STATUS_SPAWN:
					msgTime = npc.getMsgSpawnTime();
					msgSize = npc.getMsgSpawn().size();
					msgList = npc.getMsgSpawn();
					break;
				case Lineage.AI_STATUS_ESCAPE:
					msgTime = npc.getMsgEscapeTime();
					msgSize = npc.getMsgEscape().size();
					msgList = npc.getMsgEscape();
					break;
			}

			// 멘트를 표현할 수 있는 디비상태일경우.
			if (msgTime != -1 && msgSize > 0 && msgList != null) {
				if (msgTime == 0) {
					// 한번만 처리하기.
					if (!ai_showment) {
						toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), this, 0x02, msgList.get(0)), false);
						ai_showment = true;
					}
				} else {
					// 시간마다 표현하기.
					if (time - ai_showment_time >= msgTime) {
						ai_showment_time = time;
						if (ai_showment_idx >= msgSize)
							ai_showment_idx = 0;
						toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), this, 0x02, msgList.get(ai_showment_idx++)), false);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
