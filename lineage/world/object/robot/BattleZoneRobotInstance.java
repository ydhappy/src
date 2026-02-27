//package lineage.world.object.robot;
//
//import java.util.ArrayList;
//
//import java.util.StringTokenizer;
//
//import cholong.controller.BattleZoneController;
//
//import java.util.List;
//
//import lineage.bean.database.Item;
//import lineage.bean.lineage.Trade;
//import lineage.database.ServerDatabase;
//import lineage.database.ItemDatabase;
//import lineage.share.Common;
//import lineage.share.GameSetting;
//import lineage.share.Lineage;
//import lineage.network.packet.BasePacketPooling;
//import lineage.network.packet.ClientBasePacket;
//import lineage.network.packet.server.S_ObjectEffect;
//import lineage.util.Util;
//import lineage.world.World;
//import lineage.world.controller.ChattingController;
//import lineage.world.controller.RobotController;
//import lineage.world.controller.TradeController;
//import lineage.world.object.Character;
//import lineage.world.object.object;
//import lineage.world.object.instance.PcInstance;
//import lineage.world.object.instance.ItemInstance;
//import lineage.world.object.instance.RobotInstance;
//import lineage.world.object.item.gamble.Gambledice6;
//
//
//public class BattleZoneRobotInstance extends RobotInstance {
//	
//	// 타이머 처리에 사용되는 함수. 멘트표현에 딜레이를 주기 위해. 
//	protected long time_ment;
//	// 현재 표현된 홍보문구 위치 확인용.
//	protected int idx_ment;
//	// 거래가 요청된 시간에서 10초 정도 더 늘린값. 타이머함수에서 해당 시간이 오바되면 자동 거래취소되게 하기위해.
//	protected long time_trade;
//	// 같이 버프시전할 객체들
//	protected List<BattleZoneRobotInstance> list;
//	
//	static int i=0;
//
//	public BattleZoneRobotInstance() {
//		//
//	}
//	
//	public BattleZoneRobotInstance(int x, int y, int map, int heading, String name, int gfx) {
//		this.objectId = ServerDatabase.nextEtcObjId();
//		this.x = x;
//		this.y = y;
//		this.map = map;
//		this.heading = heading;
//		this.name = name;
//		this.gfx = gfx;
//		
//	}
//	
//	@Override
//	public void toWorldJoin() {
//		super.toWorldJoin();
//		
//		if(list != null) {
//			for(object o : list)
//				o.toTeleport(o.getX(), o.getY(), o.getMap(), false);
//			
//			RobotController.count += list.size();
//		}
//	}
//	
//	@Override
//	public void toWorldOut() {
//		super.toWorldOut();
//		
//		if(list != null) {
//			for(object o : list){
//				o.clearList(true);
//				World.remove(o);
//			}
//			
//			RobotController.count -= list.size();
//		}
//	}
//	
//	@Override
//	public int getGm(){
//		return 1;
//	}
//	
//	@Override
//	public void setNowHp(int nowhp){}
//	
//	@Override
//	public void toDamage(Character cha, int dmg, int type, Object...opt){
//		
//		PcInstance pc = (PcInstance)cha;
//		
//		if (BattleZoneController.getInstance().getDuelOpen()) {
//			if (pc.getLevel() < GameSetting.BATTLEZONE_LEVEL){
//				ChattingController.toChatting(pc, "배틀존은 " + GameSetting.BATTLEZONE_LEVEL + "레벨부터 입장가능합니다.", 20);
//				return;
//			}
//			
//			if (pc.get_DuelLine() != 0 || BattleZoneController.getInstance().is배틀존유저(pc)) {
//				ChattingController.toChatting(pc, "배틀존에서 나왔다가 다시 들어갈 수 없습니다.", 20);
//				return;
//			}
//			if (BattleZoneController.getInstance().get배틀존유저Count() > 50) {
//				ChattingController.toChatting(pc, "프리미엄 배틀존의 인원이 모두 찼습니다.", 20);
//				return;
//			}
//			if (pc.getPartyId() > 0) {
//				ChattingController.toChatting(pc, "파티중에는 프리미엄 배틀존 입장이 불가능합니다.", 20);
//				return;
//			}
//			//20190116
//			if (pc.isInvis()){
//				ChattingController.toChatting(pc, "투명 상태를 해제하셔야 됩니다.", 20);
//				return;
//			}
//			// 라인을 나누자..
//			if (BattleZoneController.getInstance().get배틀존유저Count() % 2 == 0) {
//				// 짝수라인
//				pc.set_DuelLine(2);
//			} else {
//				// 홀수라인
//				pc.set_DuelLine(1);
//			}
//			
//			ChattingController.toChatting(pc, "프리미엄 배틀존 대기실로 입장하셨습니다.", 20);
//			BattleZoneController.getInstance().add배틀존유저(pc);
//			pc.toPotal(32770 ,32837, 5083);
//		} else {
//			ChattingController.toChatting(pc, "현재 프리미엄 배틀존이 열리지 않았습니다.", 20);
//		}
//		
//	/*	PcInstance pc = (PcInstance)cha;
//if(BattleZoneController.getInstance().getBattleOpen() == true){
//  ChattingController.toChatting(this,"정해진 시간에만 입장가능합니다.", 0);
//  return;
// }
//
// if(BattleZoneController.getInstance().getBattleCount() == 20){
//	 ChattingController.toChatting(this,"\\fW배틀존의인원이모두찼습니다.!", 0);
//  return;
// }
//
// if(BattleZoneController.getInstance().getBattleStart() == false){
//	 ChattingController.toChatting(this,"\\fW배틀존의입장시간이아닙니다.", 0);
//  return;
// }
//
// if (pc.getPartyId()!=0) {
//	 ChattingController.toChatting(this,"\\fW파티중에는배틀존입장이불가능합니다.", 0);
//  return;
// }
//
// if(pc.getLevel() < 9){
//	 ChattingController.toChatting(this,"\\fW10레벨부터입장가능합니다.", 0);
//  return;
//    }
//
// 
//
// pc.toPotal(32735 ,32801, 16896);
// ChattingController.toChatting(this,"\\fW 배틀존대기실로 이동합니다.", 0);
//  ChattingController.toChatting(this,"\\fW 배틀이시작되면자동으로경기입장됩니다.", 0);
//   
// BattleZoneController.getInstance().addBattleList(pc); //배틀존 리스트에 pc를 저장한다.
//*/
//	
//	}
//	@Override
//	public void toTradeCancel(Character cha){
//		//
//	}
//	
//	@Override //교환창 ㅇㅋ 를 했을시에.
//	public void toTradeOk(Character cha){
//
//	}
//	
//	@Override
//	public void toTimer(long time){
//		if(isWorldDelete())
//			return;
//		/*
//		// 거래에 등록된 아이템 확인.
//		Trade t = TradeController.find(this);
//
//		if(t!=null && t.isAden(false, 1000000)){
//			// 거래 승인 요청.
//			TradeController.toTradeOk(this);
//			i=1;
//		}
//		// 거래요청된 상태에서 10초가 지낫을경우 자동 취소처리.
//		if(time_trade!=0 && time_trade-time<=0){
//			time_trade = 0;
//			// 거래 취소 요청.
//			TradeController.toTradeCancel(this);
//		}*/
///*
//		// 멘트 처리 구간.
//		if(time_ment-time <= 10 && i==0){
//			// 600초 10분
//			time_ment = time + (1000 * 1200);
//			// 표현할 문구 위치값이 오바됫을경우 초기화.
//			if(idx_ment >= Common.BATTLE_ROBOT_MENT.size())
//				idx_ment = 0;
//			// 홍보문구 표현.
//			ChattingController.toChatting(this, Common.BATTLE_ROBOT_MENT.get(idx_ment++), 0);
//		}*/
//	}
//	
//}
