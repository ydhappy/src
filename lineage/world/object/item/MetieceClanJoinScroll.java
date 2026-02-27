package lineage.world.object.item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage.bean.lineage.Clan;
import lineage.database.DatabaseConnection;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.ClanController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class MetieceClanJoinScroll extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new MetieceClanJoinScroll();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		PcInstance pc = (PcInstance) cha;
		
		if(pc.getClanId() > 0){
			ChattingController.toChatting(pc,"이미 혈맹에 가입되어있습니다.", 20);
			return;
		}
	

		if(clanid_search(pc.getClient().getAccountId(), 32)){
			ChattingController.toChatting(pc,"당신의 계정내 한캐릭은 이미 다른 혈맹에 가입중입니다.", 20);
			ChattingController.toChatting(pc,"한 계정내 캐릭들은  같은 혈맹만 가입 가능합니다.", 20);
			return;
		}
		
		Clan c = ClanController.find(32);
		c.setTempPc(pc);
//		ClanController.toJoinScrollFinal(true);
		
		cha.getInventory().count(this, getCount()-1, true);
	}

		

	
	private static boolean clanid_search(String i,int SS) {
		java.sql.Connection accountObjid = null ;
		PreparedStatement Query_objid = null ;
		ResultSet rs = null;
		try {
			int count = 0 ;

			accountObjid = DatabaseConnection.getLineage();
			Query_objid = accountObjid.prepareStatement("SELECT clanID FROM characters WHERE account = '" + i + "'");

			rs = Query_objid.executeQuery() ;
			while (rs.next()) {
				int aa = rs.getInt("ClanID"); 
				if(aa != 0 && aa != SS){
					count++ ;
					break;
				}
			}
			if(count>0){
				return true;
			}
			return false;
		} catch (Exception e) {

		}finally{
			DatabaseConnection.close(accountObjid, Query_objid);
		}
		return false ;
	}
}
	