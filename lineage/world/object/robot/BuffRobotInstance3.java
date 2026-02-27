package lineage.world.object.robot;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.lineage.Trade;
import lineage.database.ServerDatabase;
import lineage.database.SkillDatabase;
import lineage.share.Common;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.controller.RobotController;
import lineage.world.controller.TradeController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.RobotInstance;
import lineage.world.object.magic.AdvanceSpirit;
import lineage.world.object.magic.BlessWeapon;
import lineage.world.object.magic.BurningWeapon;
import lineage.world.object.magic.EarthSkin;
import lineage.world.object.magic.EnchantDexterity;
import lineage.world.object.magic.EnchantMighty;
import lineage.world.object.magic.Haste;
import lineage.world.object.magic.Heal;
import lineage.world.object.magic.IronSkin;
import lineage.world.object.magic.WindShot;

public class BuffRobotInstance3 extends RobotInstance {
	
	// 타이머 처리에 사용되는 함수. 멘트표현에 딜레이를 주기 위해. 
	protected long time_ment;
	// 홍보 멘트 목록
	protected List<String> list_ment;
	// 현재 표현된 홍보문구 위치 확인용.
	protected int idx_ment;
	// 거래가 요청된 시간에서 10초 정도 더 늘린값. 타이머함수에서 해당 시간이 오바되면 자동 거래취소되게 하기위해.
	protected long time_trade;
	// 같이 버프시전할 객체들
	protected List<BuffRobotInstance3> list;
	
	public BuffRobotInstance3() {
		//
	}
	
	public BuffRobotInstance3(int x, int y, int map, int heading, String name, int gfx) {
		this.objectId = ServerDatabase.nextEtcObjId();
		this.x = x;
		this.y = y;
		this.map = map;
		this.heading = heading;
		this.name = name;
		this.gfx = gfx;
		
		// 보조 케릭
		list = new ArrayList<BuffRobotInstance3>();

		for(int r=1 ; r<4 ; ++r) {
			
			BuffRobotInstance3 o = new BuffRobotInstance3();
			o.setObjectId(ServerDatabase.nextEtcObjId());
			o.setX(33061);
			o.setY(33392 + r);
			o.setMap(map);
			o.setHeading(heading);
			//o.setGfx(1186);
			o.setLawful(-12000);
			o.setGfx(Util.random(0, 1)==0 ? 1186 : 734);
			o.setName(String.format("%s%d", "은말보조", r));
			//o.setName(String.format(name));
			o.setTitle("1000원");
			list.add(o);
		  }
		for(int r=1 ; r<6 ; ++r) {
			
			BuffRobotInstance3 o = new BuffRobotInstance3();
			o.setObjectId(ServerDatabase.nextEtcObjId());
			o.setX(33060);
			o.setY(33392 + r);
			o.setMap(map);
			o.setHeading(heading);
			//o.setGfx(1186);
			o.setLawful(-32767);
			o.setGfx(Util.random(0, 1)==0 ? 1186 : 734);
			o.setName(String.format("%s%d", "은말샵", r));
			//o.setName(String.format(name));
			o.setTitle("1000원");
			list.add(o);
		}
		for(int r=1 ; r<7 ; ++r) {
			
			BuffRobotInstance3 o = new BuffRobotInstance3();
			o.setObjectId(ServerDatabase.nextEtcObjId());
			o.setX(33058);
			o.setY(33392 + r);
			o.setMap(map);
			o.setHeading(heading);
			//o.setGfx(1186);
			o.setLawful(-32767);
			o.setGfx(Util.random(0, 1)==0 ? 1186 : 734);
			o.setName(String.format("%s%d", "은말버프", r));
			//o.setName(String.format(name));
			o.setTitle("1000원");
			list.add(o);
		}
		for(int r=1 ; r<7 ; ++r) {
			
			BuffRobotInstance3 o = new BuffRobotInstance3();
			o.setObjectId(ServerDatabase.nextEtcObjId());
			o.setX(33059);
			o.setY(33392 + r);
			o.setMap(map);
			o.setHeading(heading);
			//o.setGfx(1186);
			o.setLawful(-32767);
			o.setGfx(Util.random(0, 1)==0 ? 1186 : 734);
			o.setName(String.format("%s%d", "은말지킴이", r));
			//o.setName(String.format(name));
			o.setTitle("1000원");
			list.add(o);
		}
		// 멘트 목록.
		list_ment = new ArrayList<String>();
		list_ment.add(String.format("[은말헤이샵] %d원 서비스 팍팍해드립니다.", Lineage.robot_auto_buff_aden3));
		list_ment.add("앞에서 어택해주세요.");
	}
	

	
	@Override
	public void toWorldJoin() {
		super.toWorldJoin();
		
		if(list != null) {
			for(object o : list)
				o.toTeleport(o.getX(), o.getY(), o.getMap(), false);
			
			RobotController.count += list.size();
		}
	}
	
	@Override
	public void toWorldOut() {
		super.toWorldOut();
		
		if(list != null) {
			for(object o : list){
				o.clearList(true);
				World.remove(o);
			}
			
			RobotController.count -= list.size();
		}
	}
	
	@Override
	public int getGm(){
		return 1;
	}
	
	@Override
	public void setNowHp(int nowhp){}
	
	@Override
	public void toDamage(Character cha, int dmg, int type, Object...opt){
		// 교환 요청.
		TradeController.toTrade(this, cha);
		ChattingController.toChatting(this, "방갑습니다. 고객님", Lineage.CHATTING_MODE_NORMAL);
		
		// 교환창에 아이템등록까지 대기시간 설정.
		time_trade = System.currentTimeMillis() + (1000 * 15);
	}
	
	@Override
	public void toTradeCancel(Character cha){
		//
	}
	
	@Override
	public void toTradeOk(Character cha){
        //멘트치기
		ChattingController.toChatting(this, "\f         \\\\f;f;감사합니다^^ 또 오세요~", Lineage.CHATTING_MODE_NORMAL);
		
		// 풀힐 서비스
		Heal.init(list.get(0), SkillDatabase.find(8, 0), cha.getObjectId());
	    // 덱스업 서비스
		EnchantDexterity.init(list.get(1), SkillDatabase.find(4, 1), cha.getObjectId());
		// 힘업 서비스
		EnchantMighty.init(list.get(2), SkillDatabase.find(6, 1), cha.getObjectId());
		//헤이스트
		Haste.init(list.get(3), SkillDatabase.find(6, 2), cha.getObjectId());
		Haste.init(list.get(4), SkillDatabase.find(6, 2), cha.getObjectId());
		Haste.init(list.get(5), SkillDatabase.find(6, 2), cha.getObjectId());
		Haste.init(list.get(6), SkillDatabase.find(6, 2), cha.getObjectId());
		Haste.init(list.get(7), SkillDatabase.find(6, 2), cha.getObjectId());
		Haste.init(list.get(8), SkillDatabase.find(6, 2), cha.getObjectId());
		Haste.init(list.get(9), SkillDatabase.find(6, 2), cha.getObjectId());
		Haste.init(list.get(10), SkillDatabase.find(6, 2), cha.getObjectId());
		Haste.init(list.get(11), SkillDatabase.find(6, 2), cha.getObjectId());
		Haste.init(list.get(12), SkillDatabase.find(6, 2), cha.getObjectId());
		Haste.init(list.get(13), SkillDatabase.find(6, 2), cha.getObjectId());
		Haste.init(list.get(14), SkillDatabase.find(6, 2), cha.getObjectId());
		Haste.init(list.get(15), SkillDatabase.find(6, 2), cha.getObjectId());
		Haste.init(list.get(16), SkillDatabase.find(6, 2), cha.getObjectId());
		Haste.init(list.get(17), SkillDatabase.find(6, 2), cha.getObjectId());
		Haste.init(list.get(18), SkillDatabase.find(6, 2), cha.getObjectId());
		
		}

	@Override
	public void toTimer(long time){
		if(isWorldDelete())
			return;
		
		// 거래에 등록된 아이템 확인.
		Trade t = TradeController.find(this);
		if(t!=null && t.isAden(false, Lineage.robot_auto_buff_aden3)){
			// 거래 승인 요청.
			TradeController.toTradeOk(this);
		}
		// 거래요청된 상태에서 10초가 지낫을경우 자동 취소처리.
		if(time_trade!=0 && time_trade-time<=0){
			time_trade = 0;
			// 거래 취소 요청.
			TradeController.toTradeCancel(this);
		}
		// 멘트 처리 구간.
		if(time_ment-time <= 0){
			// 5초마다 홍보하기.
			time_ment = time + (1000 * 20);
			// 표현할 문구 위치값이 오바됫을경우 초기화.
			if(idx_ment >= list_ment.size())
				idx_ment = 0;
			// 홍보문구 표현.
			ChattingController.toChatting(this, list_ment.get(idx_ment++), Lineage.CHATTING_MODE_NORMAL);
		}
	}
	
}