package lineage.bean.event;

import java.util.ArrayList;
import java.util.List;

import jsn_soft.TrueTargetController;
import lineage.bean.lineage.Clan;
import lineage.database.BackgroundDatabase;
import lineage.database.CharactersDatabase;
import lineage.database.ServerDatabase;
import lineage.gui.GuiMain;
import lineage.network.LineageServer;
import lineage.plugin.PluginController;
import lineage.share.Common;
import lineage.share.Lineage;
import lineage.share.Log;
import lineage.share.Mysql;
import lineage.world.World;
import lineage.world.controller.AgitController;
import lineage.world.controller.AuctionController;
import lineage.world.controller.BossController;
import lineage.world.controller.BuffController;
import lineage.world.controller.BugScanningController;
import lineage.world.controller.CharacterController;
import lineage.world.controller.ClanController;
import lineage.world.controller.ColosseumController;
import lineage.world.controller.CommandController;
import lineage.world.controller.DogRaceController;
import lineage.world.controller.DungeonController;
import lineage.world.controller.ElvenforestController;
import lineage.world.controller.GhostHouseController;
import lineage.world.controller.InnController;
import lineage.world.controller.KingdomController;
import lineage.world.controller.LastavardController;
import lineage.world.controller.MagicDollController;
import lineage.world.controller.NoticeController;
import lineage.world.controller.PartyController;
import lineage.world.controller.PvpController;
import lineage.world.controller.QuestController;
import lineage.world.controller.RankController;
import lineage.world.controller.RobotController;
import lineage.world.controller.SlimeRaceController;
import lineage.world.controller.SummonController;
import lineage.world.controller.TimerController3;
import lineage.world.controller.WantedController;
import lineage.world.controller.WebAuctionController;
import lineage.world.controller.WorldClearController;
import lineage.world.controller.FightController;
import lineage.world.controller.QuestTodayController;


public class Timer implements Event {

	static public enum TYPE {
		PluginController,
		ServerDatabase,
		LineageServer,
		NoticeController,
		CharacterController,
		World,
//		ShipController,
//		BuffController,
//		SummonController,
		KingdomController,
//		InnController,
//		AuctionController,
//		AgitController,
		QuestController,
		ColosseumController,
		ElvenforestController,
		BossController,
		SlimeRaceController,
		Mysql,
		Log,
		BugScanningController,
		RobotController,
		RankController,
//		DogRaceController,
		MagicDollController,
		GuiMain,
		WantedController,
		DungeonController,
//		WebAuctionController,
//		GhostHouseController,
		QuestTodayController,
		BackgroundDatabase,
		CharactersDatabase,
		PartyController,
		LastavardController,
		PvpController,
		TimerController3,
		CommandController,
		TrueTargetController,
//		FightController,
//		WorldClearController,
	
	};
	
	/**
	 * 풀링에서 객체를 꺼냈는데 해당 객체가 null일수 있음. 그래서 이곳에서 동적으로 생성.
	 * @param e
	 * @param c
	 * @return
	 */
	static synchronized public Event clone(Event e, TYPE type, long time){
		if(e == null)
			e = new Timer();
		((Timer)e).setType(type);
		((Timer)e).setTime(time);
		return e;
	}
	
	private long time;
	private TYPE type;
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public void setType(TYPE type) {
		this.type = type;
	}
	
	@Override
	public void init() {
		try {
			//
			List<Object> temp = new ArrayList<Object>();
			//
			switch(type) {
				case PluginController:
					PluginController.init(Timer.class, "toTimer", time);
					break;
				case ServerDatabase:
					// 매초마다 서버오브젝트아이디 갱신
					ServerDatabase.toSave();
					break;

//				case ElvenforestController:
//					// 요정숲 관리
//					ElvenforestController.toTimer(time);
//					break;
//				case BossController:
//					// 보스몬스터 관리.
//					BossController.toTimer(time);
//					break;
				case ColosseumController:
					// 무한대전 관리. 
					ColosseumController.toTimer(time);
					break;
				case SlimeRaceController:
					// 슬라임레이스 관리. 
					SlimeRaceController.toTimer(time);
					break;
//				case Mysql:
//					// mysql 관리.
//					Mysql.toTimer(time);
//					break;
//				case Log:
//					// log 관리.
//					Log.toTimer(time);
//					break;
				case BugScanningController:
					// bug 관리. 
					BugScanningController.toTimer();
					break;
//				case RobotController:
//					// robot 관리.
////					RobotController.toTimer(time);
//					break;
//				case RankController:
//					// 랭킹 관리.
//					RankController.toTimer(time);
//					break;
//				case DogRaceController:
//					// 강아지 레이스 관리.
//					DogRaceController.toTimer(time);
//					break;
//				case MagicDollController:
//					// 마법인형 관리.
//					MagicDollController.toTimer(time, temp);
//					break;
//				case GuiMain:
//					// gui 관리.
//					if(Common.system_config_console == false){
//						GuiMain.display.asyncExec(new Runnable(){
//							@Override
//							public void run(){
//								GuiMain.toTimer(time);
//							}
//						});
//					}
//					break;
//				case WantedController:
//					// 현상수배 관리.
//					WantedController.toTimer(time);
//					break;
//				case DungeonController:
//					// 던전 관리.
//					DungeonController.toTimer(time);
//					break;
//				case WebAuctionController:
//					//
//					WebAuctionController.toTimer(time);
//					break;
//				case GhostHouseController:
//					//
//					GhostHouseController.toTimer(time);
//					break;
                case QuestTodayController:
					//
					QuestTodayController.toTimer(time);
					break;
				case BackgroundDatabase:
					//
					BackgroundDatabase.toTimer(time);
					break;
//				case CharactersDatabase:
					//
//					CharactersDatabase.toTimer(time);
//					break;
//				case PartyController:
//					//
//					PartyController.toTimer(time);
//					break;
//				case LastavardController:
//					LastavardController.toTimer(time);
//					break;
				case PvpController:
					PvpController.toTimer(time);
					break;
				case TimerController3:
					TimerController3.toTimer(time);
					break;
				case CommandController:
					CommandController.toTimer(time);
					break;
				case TrueTargetController:
					TrueTargetController.toTimer(time);
					break;
//				case FightController:
//					FightController.toTimer(time);
//					break;
//				case WorldClearController:
//					if (Lineage.is_world_clean) {
//						try { WorldClearController.toTimer(time); } catch (Exception e) {
//							lineage.share.System.println("월드맵 청소 관리.");
//							lineage.share.System.println(e);
//						}
//					}
//					break;
				
			}
		} catch (Exception e) {
//			lineage.share.System.println(type);
//			lineage.share.System.println(e);
		}
	}

	@Override
	public void close() {
		// 할거 없음..
	}
	
}
