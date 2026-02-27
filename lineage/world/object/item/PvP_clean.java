package lineage.world.object.item;

import java.sql.Connection;
import java.sql.PreparedStatement;

import lineage.database.DatabaseConnection;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.PvpController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class PvP_clean extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new PvP_clean();
		return item;
	}
	
	public void toClick(Character cha, ClientBasePacket cbp){
		Connection con = null;
		PreparedStatement stt = null;
		PcInstance pc = (PcInstance)cha;
		
		if (pc.isWorldDelete() || pc == null || pc.isDead())
			return;
		PvpController.clearPvp((PcInstance)cha);
		try {
			con = DatabaseConnection.getLineage();

			ChattingController.toChatting(pc, "PVP 승/패가 초기화되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			cha.getInventory().count(this, getCount()-1, true);
		} catch (Exception e) {
			lineage.share.System.println("PVP 승/패 초기화하는데 실패했습니다. : " + pc.getName());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, stt);
		}
	}
}
