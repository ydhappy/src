package lineage.world.object.item.scroll.newscroll;

import lineage.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage.bean.database.Definite;
import lineage.database.AccountDatabase;
import lineage.database.DatabaseConnection;
import lineage.database.DefiniteDatabase;
import lineage.network.LineageServer;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Disconnect;
import lineage.network.packet.server.S_Inventory;
import lineage.network.packet.server.S_InventoryBress;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryIdentify;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ScrollCharUnLock extends ItemInstance {
	
	private long cha_objID; //저장될 값
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollCharUnLock();
		return item;
	}
	/*private static void chk(PcInstance pc){
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT characters FROM account_uid WHERE name=?");
			st.setString(1,  pc.getName());
			rs = st.executeQuery();
			if(rs.next())
				return rs.getInt(1);
			
		}catch(Exception e){
			lineage.share.System.printf("%s : 케릭영혼구슬에러\r\n", ScrollCharUnLock.class.toString());
			lineage.share.System.println(e);
		}finally{
			DatabaseConnection.close(con, st, rs);
		}
	}*/
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		PcInstance pc = (PcInstance)cha;
		int cnt = 0;
		//자신의 케릭터 슬롯 빈공간이 존재하는지 확인.
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		/*try{
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT characters FROM account_uid WHERE name=?");
			st.setString(1,  pc.getName());
			rs = st.executeQuery();
			while(rs.next())
				cnt += 1;
		}catch(Exception e){
			lineage.share.System.printf("%s : 케릭영혼구슬에러\r\n", ScrollCharUnLock.class.toString());
			lineage.share.System.println(e);
		}finally{
			DatabaseConnection.close(con, st, rs);
		}*/
		
		
		//계정 이름과 암호 기억하기
		String Acc = null;
		int pw = 0;

		try{
			con = DatabaseConnection.getLineage();			
			st = con.prepareStatement("SELECT account_uid FROM characters WHERE name=?");
			st.setString(1,  pc.getName());			
			rs = st.executeQuery();
			if(rs.next()){
				pw = rs.getInt("account_uid");
			}
			cnt = AccountDatabase.getCharacterLength(con, pw);
		}catch(Exception e){
			lineage.share.System.printf("%s : 케릭영혼구슬에러2\r\n", ScrollCharUnLock.class.toString());
			lineage.share.System.println(e);
		}finally{
			DatabaseConnection.close(con, st, rs);
		}
		try{
			con = DatabaseConnection.getLineage();
			//cnt = AccountDatabase.getCharacterLength(con, pw);
			st = con.prepareStatement("SELECT account FROM characters WHERE name=?");
			st.setString(1,  pc.getName());
			rs = st.executeQuery();
			if(rs.next()){
				Acc = rs.getString("account");
			}
		}catch(Exception e){
			lineage.share.System.printf("%s : 케릭영혼구슬에러3\r\n", ScrollCharUnLock.class.toString());
			lineage.share.System.println(e);
		}finally{
			DatabaseConnection.close(con, st, rs);
		}
		
		
				
		if(cnt >=3){
			ChattingController.toChatting(cha, "케릭터의 저장 공간이 부족합니다.(케릭터를 1개이상 삭제하세요)");
			return;
		}
		
		
		//케릭터 이동시키기
		try{
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters SET account=?, account_uid=? WHERE objID=?");
			st.setString(1, Acc);
			st.setInt(2, pw);
			st.setLong(3, this.getSoul_Cha());
			st.executeUpdate();
		}catch(Exception e){
			lineage.share.System.printf("%s : 영혼구슬오류4\r\n", ScrollCharUnLock.class.toString());
			lineage.share.System.println(e);
		}finally{
			DatabaseConnection.close(con, st, rs);
		}
		ChattingController.toChatting(pc, "안전한 상황을 위해 강제 종료합니다.");
		ChattingController.toChatting(pc, "케릭터 이전이 완료 되었습니다.");
		
		
		
		cha.getInventory().count(this, getCount()-1, true);
		// 사용자 강제종료 시키기.
		pc.toSender(S_Disconnect.clone(BasePacketPooling.getPool(S_Disconnect.class), 0x0A));
		LineageServer.close(pc.getClient());
	}
	@Override
	public String getName() {
		
		// uid-idx name
		return String.format("영혼이 담겨진 구슬:%d", this.getSoul_Cha());
	}
	
	
}
