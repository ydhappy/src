package lineage;

import java.sql.Connection;

import cholong.util.GeneralThreadPool;
import cholong.util.RealTimeClock;
import jsn_soft.AutoHuntDatabase;
import jsn_soft.AutoHuntThread;
import jsn_soft.KingdomBossDatabase;
import lineage.database.DungeonBookTeleportDatabase;
import lineage.database.BookTeleportDatabase;
import lineage.database.EnchantLostItemDatabase;
import lineage.database.BackgroundDatabase;
import lineage.database.BadIpDatabase;
import lineage.database.CharactersDatabase;
import lineage.database.DatabaseConnection;
import lineage.database.DefiniteDatabase;
import lineage.database.DungeonDatabase;
import lineage.database.DungeonPartTimeDatabase;
import lineage.database.ExpDatabase;
import lineage.database.FishItemListDatabase;
import lineage.database.ExpPaneltyDatabase;
import lineage.database.HackNoCheckDatabase;
import lineage.database.ItemBundleDatabase;
import lineage.database.ItemChanceBundleDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ItemMaplewandDatabase;
import lineage.database.ItemPinewandDatabase;
import lineage.database.ItemSetoptionDatabase;
import lineage.database.ItemSkillDatabase;
import lineage.database.ItemTeleportDatabase;
import lineage.database.LifeLostItemDatabase;
import lineage.database.MagicdollListDatabase;
import lineage.database.MonsterBossSpawnlistDatabase;
import lineage.database.MonsterDatabase;
import lineage.database.MonsterDropDatabase;
import lineage.database.MonsterSkillDatabase;
import lineage.database.MonsterSpawnlistDatabase;
import lineage.database.NpcCraftDatabase;
import lineage.database.NpcDatabase;
import lineage.database.NpcShopDatabase;
import lineage.database.NpcSpawnlistDatabase;
import lineage.database.NpcTeleportDatabase;
import lineage.database.OmanBookTeleportDatabase;
import lineage.database.PolyDatabase;
import lineage.database.QuestDatabase;
import lineage.database.QuestPresentDatabase;
import lineage.database.QuizQuestionDatabase;
import lineage.database.ServerDatabase;
import lineage.database.ServerMessageDatabase;
import lineage.database.ServerNoticeDatabase;
import lineage.database.ServerOpcodesDatabase;
import lineage.database.ServerSeasonEpDatabase;
import lineage.database.SkillDatabase;
//import lineage.database.SpeedipDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.database.SummonListDatabase;
import lineage.database.TeleportHomeDatabase;
import lineage.database.TeleportResetDatabase;
import lineage.database.TimeDungeonDatabase;
import lineage.database.TownBookTeleportDatabase;
import lineage.database.WarehouseDatabase;
import lineage.database.WebAuctionDatabase;
import lineage.gui.GuiMain;
import lineage.network.LineageServer;
import lineage.network.WebServer;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.persnal_shop.PersnalShopDatabase;
import lineage.plugin.PluginController;
import lineage.share.Common;
import lineage.share.Desc;
import lineage.share.GameSetting;
import lineage.share.Lineage;
import lineage.share.Lineage_Balance;
import lineage.share.Log;
import lineage.share.Mysql;
import lineage.share.Socket;
import lineage.share.Web;
import lineage.share.Zone;
import lineage.thread.AiThread;
import lineage.thread.CharacterControlThread;
import lineage.thread.CharacterControlThread2;
import lineage.thread.CharacterThread;
import lineage.thread.ControllerTherad;
//import lineage.thread.DatabaseThread;
import lineage.thread.EventThread;
import lineage.thread.GuiThread;
import lineage.thread.SaveThread;
import lineage.thread.ServerThread;
import lineage.thread.TimeThread;
import lineage.thread.TimerThread;
import lineage.util.PakTools;
import lineage.util.Shutdown;
import lineage.util.SpriteTools;
import lineage.util.TileTools;
import lineage.world.AStar;
import lineage.world.World;
import lineage.world.controller.AgitController;
import lineage.world.controller.AuctionController;
import lineage.world.controller.BaseStatController;
import lineage.world.controller.BoardController;
import lineage.world.controller.BookController;
import lineage.world.controller.BossController;
import lineage.world.controller.BuffController;
import lineage.world.controller.BugScanningController;
import lineage.world.controller.CharacterController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.ClanController;
import lineage.world.controller.ClanLordController;
import lineage.world.controller.ColosseumController;
import lineage.world.controller.CommandController;
import lineage.world.controller.CookController;
import lineage.world.controller.CraftController;
import lineage.world.controller.CraftTableController;
import lineage.world.controller.DamageController;
import lineage.world.controller.QuestTodayController;
import lineage.world.controller.DungeonController;
import lineage.world.controller.ElvenforestController;
import lineage.world.controller.EventController;
import lineage.world.controller.FightController;
import lineage.world.controller.FriendController;
import lineage.world.controller.GiranClanLordController;
import lineage.world.controller.GhostHouseController;
import lineage.world.controller.InnController;
import lineage.world.controller.InventoryController;
import lineage.world.controller.KingdomController;
import lineage.world.controller.LastavardController;
import lineage.world.controller.LetterController;
import lineage.world.controller.LuckyController;
import lineage.world.controller.MagicDollController;
import lineage.world.controller.NoticeController;
import lineage.world.controller.PartyController;
import lineage.world.controller.PcMarketController;
//import lineage.world.controller.PcMarketController;
import lineage.world.object.npc.Npc_promotion;
import lineage.world.object.robot.PcRobotInstance;
import lineage.world.controller.무인혈맹컨트롤러;
import lineage.world.controller.PvpController;
import lineage.world.controller.QuestController;
import lineage.world.controller.RankController;
import lineage.world.controller.RobotController;
import lineage.world.controller.ScriptController;
import lineage.world.controller.ShopController;
import lineage.world.controller.SkillController;
import lineage.world.controller.SlimeRaceController;
import lineage.world.controller.DogRaceController;
import lineage.world.controller.SpServerController;
import lineage.world.controller.SummonController;
import lineage.world.controller.TalkIslandDungeonController;
import lineage.world.controller.TimerController3;
import lineage.world.controller.TradeController;
import lineage.world.controller.UserAdenaSellController;
import lineage.world.controller.UserShopController;
import lineage.world.controller.WantedController;
import lineage.world.controller.WebAccountController;
import lineage.world.controller.WebAppCenterController;
import lineage.world.controller.WebAuctionController;
import lineage.world.controller.WebInformationController;
import lineage.world.controller.WebPremiumController;
import lineage.world.controller.WebSearchItemController;
import lineage.world.controller.WebSearchMonsterController;
import lineage.world.controller.WorldClearController;
import lineage.world.object.instance.PcInstance;


public final class Main implements Runnable {
	
	static public boolean running;
	
	/**
	 * 리니지 서버 시작
	 */
	static public void init() {
		if(running == true)
			return;
		
		if(Common.system_config_console == false)
			// gui모드 넣으면서 유연한 처리를위해 쓰레드를 따로빼서 처리함.
			// 이렇게 안하면 단일쓰레드에서 처리하다보니 이 과정이 끝나후 gui처리가 이뤄져서 순간 렉이 발생.
			new Thread(new Main()).start();
		else
			toLoading();
	}
	
	/**
	 * 리니지 서버 종료
	 */
	static public void close() {
		if(running == false) {
			if(!Common.system_config_console)
				Runtime.getRuntime().exit(0);
			return;
		}
		
		if(Common.system_config_console == false)
			// gui모드 넣으면서 유연한 처리를위해 쓰레드를 따로빼서 처리함.
			// 이렇게 안하면 단일쓰레드에서 처리하다보니 이 과정이 끝나여 gui처리가 이뤄져서 순간 렉이 발생.
			new Thread(new Main()).start();
		else
			toDelete();
	}
	
	@Override
	public void run() {
		if(running){
			toDelete();
		}else{
			toLoading();
		}
	}
	
	/**
	 * 서버 로딩 처리.
	 */
	static private void toLoading() {
		//
		try {
			if(Common.system_config_console)
				Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
			
			// 필요한 변수 및 클레스 초기화.
			Desc.init();
			Zone.init();
			Log.init();
			GuiThread.init();
			Mysql.init();
			Lineage.init(false);
			//Lineage.init();
			Lineage_Balance.init();
			Npc_promotion.init();
			Socket.init();
			Web.init();
			Common.init();
			GameSetting.load();
			EventThread.init();
			TimerThread.init();
			SaveThread.init();
		
			AiThread.init();
			
			AutoHuntThread.init();// 자동사냥
			World.init();
			CharacterControlThread2.init();
			AStar.init();
			BookController.init();
			InventoryController.init();
			BasePacketPooling.init();
			SkillController.init();
			DamageController.init();
			CharacterController.init();
			TradeController.init();
			DungeonController.init();
//			ShipController.init();
			BuffController.init();
			SummonController.init();
			PartyController.init();
			InnController.init();
			BoardController.init();
			LetterController.init();
			UserShopController.init();
			ChattingController.init();
//			UserShopController.init();
			CraftController.init();
			BossController.init();
			NoticeController.init();
			FriendController.init();
			BugScanningController.init();
			MagicDollController.init();
	
			ShopController.init();
			WarehouseDatabase.init();
			WebAuctionDatabase.init();
			WebAuctionController.init();
			WebSearchItemController.init();
			WebInformationController.init();
			WebPremiumController.init();
			WebSearchMonsterController.init();
			WebAccountController.init();
			WebAppCenterController.init();
			PakTools.init();
			SpriteTools.init();
			GiranClanLordController.init();
			TileTools.init();
			CommandController.init();
			BaseStatController.init();
			CharactersDatabase.init();
			SpServerController.init();
			ScriptController.init();
			RealTimeClock.init(); 
			ClanLordController.init();
			GeneralThreadPool.getInstance();
			WorldClearController.init();
			ColosseumController.init();
			
			
			// dbcp 활성화.
			DatabaseConnection.init();
			// 디비로부터 메모리 초기화.
			Connection con = DatabaseConnection.getLineage();
			ServerDatabase.init(con);
			//20180628 추가
			ServerSeasonEpDatabase.init(con);
			ServerMessageDatabase.init(con);
			QuestPresentDatabase.init(con);
			ClanController.init(con);
			DogRaceController.init(con);
			SlimeRaceController.init(con);
			KingdomController.init(con);
			AgitController.init(con);
			AuctionController.init(con);
			ItemDatabase.init(con);
			ItemSetoptionDatabase.init(con);
			ItemSkillDatabase.init(con);
			ItemBundleDatabase.init(con);
			ItemChanceBundleDatabase.init(con);
			ItemTeleportDatabase.init(con);
			PolyDatabase.init(con);
			DungeonDatabase.init(con);
			DungeonPartTimeDatabase.init(con);
			DefiniteDatabase.init(con);
			ExpDatabase.init(con);
			FishItemListDatabase.init(con);
			ExpPaneltyDatabase.init(con);
			SpriteFrameDatabase.init(con);
			BackgroundDatabase.init(con);
			SkillDatabase.init(con);
			NpcDatabase.init(con);
			NpcShopDatabase.init(con);
			NpcCraftDatabase.init(con);
			NpcTeleportDatabase.init(con);
			NpcSpawnlistDatabase.init(con);
			EventController.init();
			MonsterDatabase.init(con);
			MonsterDropDatabase.init(con);
			MonsterSkillDatabase.init(con);
			MonsterSpawnlistDatabase.init(con);
			MonsterBossSpawnlistDatabase.init(con);
			WantedController.init(con);
			BadIpDatabase.init(con);
			DungeonBookTeleportDatabase.init(con);
			TownBookTeleportDatabase.init(con);
			OmanBookTeleportDatabase.init(con);
			BookTeleportDatabase.init(con);
			EnchantLostItemDatabase.init(con);
			QuestController.init(con);
//			QuizQuestionDatabase.init(con);
			ServerOpcodesDatabase.init(con);
			ServerNoticeDatabase.init(con);
			RankController.init(con);
			TeleportHomeDatabase.init(con);
			TeleportResetDatabase.init(con);
			TimeDungeonDatabase.init(con);
			ItemPinewandDatabase.init(con);
			ItemMaplewandDatabase.init(con);
			SummonListDatabase.init(con);
			MagicdollListDatabase.init(con);
			HackNoCheckDatabase.init(con);
			LifeLostItemDatabase.init(con);
//			TeamBattleDatabase.init(con);
//			QuestTodayController.init(con);
			PvpController.init(con);
//			PcMarketController.init(con);
			KingdomBossDatabase.init(con);
			UserAdenaSellController.init();
			AutoHuntDatabase.init(con); // 자동사냥
			무인혈맹컨트롤러.init(con);
			DatabaseConnection.close(con);
			
			// 성 스폰처리
			KingdomController.readKingdom();
			// 요정숲 관리 초기화. 디비값을 참고하기때문에 디비로딩후 처리해야함.
			ElvenforestController.init();
			// 로봇 처리. etc_objectid 때문에 여기에서 처리.
			RobotController.init();
			// 유령의 집
//			GhostHouseController.init();
			// 케플리샤의 운세 시스템
//			LuckyController.init();
			// 말하는섬던전.
			TalkIslandDungeonController.init();
			
			TimerController3.init();
			// 제작테이블.
			CraftTableController.init();
			PersnalShopDatabase.getInstnace();
			// 요리.
//			CookController.init();
	
			// 라스타바드
//			LastavardController.init();
			// -- 배틀존
//			BattleZoneController.getInstance();
//			BattleZoneController battleZone = BattleZoneController.getInstance();
//			GeneralThreadPool.getInstance().execute(battleZone);
			
//			L1TreasureBox.load();
			
			// 소켓 활성화.
			LineageServer.init();
			// 웹서버 활성화.
			WebServer.init();
			
			// 필요한 쓰레드 활성화.
			AiThread.start();
			ServerThread.init();
			TimerThread.start();
			CharacterThread.init();
			CharacterControlThread.init();
			//DatabaseThread.init();
			ControllerTherad.init();
			DogRaceController.start();
			TimeThread.init();
			SaveThread.start();
			
			
			AutoHuntThread.start();
			
			FightController.init();
			
		
			
			// 서버 기본정보 표현.
			lineage.share.System.println("======================================================================");
			// Version 200
			lineage.share.System.printf(" 로딩완료 \r\n");
			// Version 200
			lineage.share.System.printf("Server Version : %d\r\n", Lineage.server_version);
			// Port 2000
			lineage.share.System.printf("Server Port : %d\r\n", Socket.PORT);
			// AutoAccount true
			lineage.share.System.printf("계정 생성 : %s\r\n", Lineage.account_auto_create ? "true" : "false");
			// Rate enchant: drop: exp: aden: party:
			lineage.share.System.printf("인챈트 : %.1f배  ||  드랍 : %.1f배  ||  경험치 : %.1f배  ||  아데나 : %.1f배  ||  파티 : %.1f배\r\n", Lineage.rate_enchant, Lineage.rate_drop, Lineage.rate_exp, Lineage.rate_aden, Lineage.rate_party);
			//if(Web.web_server) 
			//	lineage.share.System.printf("웹연동 서버 포트 : %d\r\n", Web.web_server_port);
			//if(Web.chatting)
			//	lineage.share.System.printf("채팅 서버 포트 : %d\r\n", Web.chatting_server_port);
			lineage.share.System.println("======================================================================");
			
			lineage.share.System.printf( "'%s' 가 정상 구동 되었습니다.\r\n", ServerDatabase.getName() );

			running = true;
		} catch (Exception e) {
			lineage.share.System.printf("%s : toLoading()\r\n", Main.class.toString());
			lineage.share.System.println(e);
		}
		// 서버 시작 알리기.
		PluginController.init(Main.class, "toLoading");
		// 
//		PsjumpController.init();
//		lineage.share.System.println( PsjumpController.CLIENT_MAX );
//		lineage.share.System.println( Lineage.thread_event );
		
		//
		System.gc();
		
//		//
//		SpTools.toCharactersInventoryUpdate();
//		SpTools.toNpcShopUpdate();
	}
	
	/**
	 * 서버 종료처리 함수.
	 */
	static private void toDelete(){
		try {
			running = false;
			
			// 종료 알리기.
			World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "서버가 종료 됩니다.."));
			Thread.sleep(1000);
			// 쓰레드 종료
			lineage.share.System.println("Thread close..");
			EventThread.close();
			TimerThread.close();
			ControllerTherad.close();
			ServerThread.close();
			SaveThread.close();
			//DatabaseThread.close();
			AiThread.close();
			AutoHuntThread.close();
			CharacterControlThread.close();
			CharacterControlThread2.close();
			CharacterThread.close();
			DogRaceController.close();
			TimeThread.close();
			Thread.sleep(1000);
			// 서버 종료 알리기.
			PluginController.init(Main.class, "toDelete");
			// 메모리 저장
			lineage.share.System.println("Save Database..");
			Connection con = DatabaseConnection.getLineage();
			ServerDatabase.close(con);
			ClanController.close(con);
			KingdomController.close(con);
			AuctionController.close(con);
			AgitController.close(con);
			QuestController.close(con);
			BadIpDatabase.close(con);
//			SpeedipDatabase.close(con);
			PvpController.close(con);
			무인혈맹컨트롤러.save(con);
//			PcMarketController.close(con);
			TradeController.close();
//			WantedController.close();
			GhostHouseController.close();
			BaseStatController.close();
			PersnalShopDatabase.toSave();
			for(PcInstance pc : World.getPcList()){
				if(pc.isWorldDelete())
					continue;
				// true 로 해놔야 나중에 클라 메모리 제거할때 사용자가 월드에 없다고 판단해서 저장 처리를 반복 하지 않음.
				try { pc.setWorldDelete(true); } catch (Exception e) {}
					// 죽어있을경우에 처리를 위해.
				try { pc.toReset(true); } catch (Exception e) {}
					// 저장
				try { pc.toSave(con); } catch (Exception e) {}
			}
			CharactersDatabase.close(con);
			// 메모리 제거.
			Thread.sleep(500);
//			QuestTodayController.close();
			// 로그 저장.
			Log.close();
			// 디비 컨넥션 닫기.
			DatabaseConnection.close(con);
//			DatabaseConnection.close();
//			// 소켓 닫기
//			lineage.share.System.println("Socket close..");
//			WebServer.close();
//			ChattingServer.close();
//			LineageServer.close();
//			lineage.share.System.println("ok..");
			Thread.sleep(500);
		} catch (Exception e) {
			lineage.share.System.println(Main.class+" : toDelete()");
			lineage.share.System.println(e);
		}
		
//		// 콘솔모드에서 Runtime.getRuntime().addShutdownHook(Shutdown.getInstance()) 때문에 이 구간을 이행하면 재차 해당 함수가 호출됨.
//		if(!Common.system_config_console)
//			Runtime.getRuntime().exit(0);
		System.exit(1);
	}

	public static void main(String[] args) {
		Common.system_config_console = args.length==0;
		if(Common.system_config_console)
			init();
		else
			GuiMain.open();
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
