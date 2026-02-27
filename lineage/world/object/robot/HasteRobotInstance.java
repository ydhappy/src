package lineage.world.object.robot;

import lineage.bean.lineage.Clan;
import lineage.bean.lineage.Trade;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectHeading;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.ClanController;
import lineage.world.controller.TradeController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.magic.Haste;

public class HasteRobotInstance extends PcRobotInstance {
	
	// 타이머 처리에 사용되는 함수. 멘트표현에 딜레이를 주기 위해. 
	protected long time_ment;
	// 거래가 요청된 시간에서 10초 정도 더 늘린값. 타이머함수에서 해당 시간이 오바되면 자동 거래취소되게 하기위해.
	protected long time_trade;
	//
	

	public HasteRobotInstance() {
		setLight(12);
	}
	
	@Override
	public int getGm() {
		return 1;
	}
	
	@Override
	public void setNowHp(int nowhp) {}
	
	@Override
	protected void gotoHome(boolean isCheck) {}
	
	@Override
	public void toDamage(Character cha, int dmg, int type, Object...opt) {
		// 군주만 처리함.
		Clan c = ClanController.find(this);
		if(c==null || !c.getLord().equalsIgnoreCase(getName())) {
			return;
		}
		// 방향 전환.
		setHeading(Util.calcheading(this, cha));
		toSender(S_ObjectHeading.clone(BasePacketPooling.getPool(S_ObjectHeading.class), this), false);
		// 교환 요청.
		TradeController.toTrade(this, cha);
		// 교환창에 아이템등록까지 대기시간 설정.
		time_trade = System.currentTimeMillis() + (1000 * 15);
	}
	
	@Override
	public void toTradeCancel(Character cha) {
		//
	}
	
	@Override
	public void toTradeOk(Character cha) {
		// 
		for(object o : getInsideList(true)) {
			if(o.getClanId() != getClanId())
				continue;
			if(!(o instanceof HasteRobotInstance))
				continue;
			// 헤이스트
			Haste.init((HasteRobotInstance)o, SkillDatabase.find(6, 2), cha.getObjectId());
		}
		//
	}
	
	@Override
	public void toTimer(long time) {
		if(isWorldDelete())
			return;
		// 군주만 처리함.
		Clan c = ClanController.find(this);
		if(c==null || !c.getLord().equalsIgnoreCase(getName())) {
			return;
		}
		// 거래에 등록된 아이템 확인.
		Trade t = TradeController.find(this);
		if(t!=null){
			if(t.isAden(false, Lineage.robot_auto_haste_aden)) {
				// 거래 승인 요청.
				TradeController.toTradeOk(this);
			}
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
			time_ment = time + (1000 * 5);
			// 홍보문구 표현.
//			ChattingController.toChatting(this, c.getNote(), 0);
		}
	}
	
}
