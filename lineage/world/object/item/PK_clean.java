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

public class PK_clean extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new PK_clean();
		return item;
	}
	
	public void toClick(Character cha, ClientBasePacket cbp){
		Connection con = null;
		PreparedStatement stt = null;
		PcInstance pc = (PcInstance)cha;
		if (pc.getPkCount() < 50) {
			ChattingController.toChatting(pc, "PK 횟수 50 이하는 사용할수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		if (pc.isWorldDelete() || pc == null || pc.isDead())
			return;
		try {
			con = DatabaseConnection.getLineage();
		
			
			stt = con.prepareStatement("UPDATE characters SET pkcount=0 WHERE objID=?");
			stt.setLong(1, pc.getObjectId());
			stt.executeUpdate();
			stt.close();
			
			pc.setPkCount(pc.getPkCount() - 5);
			ChattingController.toChatting(pc, "PK 횟수가 5만큼 감소합니다.", Lineage.CHATTING_MODE_MESSAGE);
			cha.getInventory().count(this, getCount()-1, true);
		} catch (Exception e) {
			lineage.share.System.println("PK 횟수 감소가 실패했습니다. : " + pc.getName());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, stt);
		}
	}
}
