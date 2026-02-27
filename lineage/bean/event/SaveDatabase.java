package lineage.bean.event;

import java.sql.Connection;

import lineage.database.BadIpDatabase;
import lineage.database.CharactersDatabase;
import lineage.database.DatabaseConnection;
import lineage.database.ServerDatabase;
import lineage.persnal_shop.PersnalShopDatabase;
import lineage.share.TimeLine;
import lineage.world.World;
import lineage.world.controller.AgitController;
import lineage.world.controller.AuctionController;
import lineage.world.controller.ClanController;
import lineage.world.controller.KingdomController;
import lineage.world.controller.QuestController;
import lineage.world.controller.TradeController;
import lineage.world.controller.WantedController;
import lineage.world.object.instance.PcInstance;

public class SaveDatabase implements Runnable {

	static private boolean action = false;

	@Override
	public void run() {
		if (action)
			return;

		action = true;
		Connection con = null;
		try {
			TimeLine.start("Save Database..");

			con = DatabaseConnection.getLineage();
			ServerDatabase.toSave(con);
//			ClanController.toSave();
 			KingdomController.toSave(con);
//			AuctionController.toSave(con);
//			AgitController.toSave(con);
			QuestController.toSave(con);
			BadIpDatabase.toSave(con);
//			SpeedipDatabase.toSave(con);
			TradeController.toSave();
//			WantedController.toSave(con);
			PersnalShopDatabase.toSave();
			for (PcInstance pc : World.getPcList()) {
				if (pc.isWorldDelete())
					continue;
				try {
					pc.toSave(con);
				} catch (Exception e) {
				}
			}
			CharactersDatabase.close(con);

			TimeLine.end();
		} catch (Exception e) {
			lineage.share.System.printf("lineage.bean.event.SaveDatabase.init()\r\n : %s\r\n", e);
		} finally {
			DatabaseConnection.close(con);
		}
		action = false;
	}

}
