package lineage.world.object.item.scroll.newscroll;

import lineage.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;

import lineage.bean.database.Definite;
import lineage.bean.database.Item;
import lineage.database.DatabaseConnection;
import lineage.database.DefiniteDatabase;
import lineage.database.ItemDatabase;
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
import lineage.world.controller.CraftController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ScrollCharLock extends ItemInstance {
		
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollCharLock();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		
		if(cha.getLevel()<=Lineage.charlock){
			ChattingController.toChatting(cha,String.format("[레벨:%d]이상부터 사용가능합니다.", Lineage.charlock), 20);
			return;
		}
		
		PcInstance pc = (PcInstance)cha;
		
		ChattingController.toChatting(cha, "케릭터가 저장구슬에 담아졌습니다.");
		ChattingController.toChatting(cha, "올바른 거래를 위하여 자동 종료 됩니다.");
		
		//영혼이 담겨진 케릭터 저장 구슬 생성.
//		ItemInstance i = ItemDatabase.find("영혼이 담겨진 구슬");
//		CraftController.toCraft(cha, i, 1, true, 0);

		//영혼이담긴구슬을 생성하고 그곳에 기록한다.
		Object ob1;
		Object ob2;
		
		ob2 = "영혼이 담겨진 구슬";
		ob1 = ItemDatabase.find4((String)ob2);
		if(ob1 !=null){
			ItemInstance i = ItemDatabase.newInstance((Item)ob1);
			i.setCount(1);
			i.setDefinite(true);
			i.setSoul_cha(cha.getObjectId());
			//생성된 구슬은 창고에 넣기
			Connection con = null;
			PreparedStatement st = null;
			try{
				con = DatabaseConnection.getLineage();
				st = con.prepareStatement("INSERT INTO warehouse SET "
						+"account_uid=?, inv_id=?, name=?, type=?, gfxid=?, count=?, quantity=?, en=?, definite=?, bress=?, durability=?, time=?, pet_id=?, letter_id=?, options=?, enEarth=?, enWater=?, enFire=?, enWind=?, add_min_dmg=?, add_max_dmg=?, add_str=?,add_dex=?,add_con=?,add_int=?,add_wiz=?,add_cha=?,add_mana=?,add_hp=?,add_manastell=?,add_hpstell=?,one=?,two=?,three=?,four=?,soul_cha=?");
				st.setLong(1, pc.getClient().getAccountUid());
				st.setLong(2, i.getObjectId());
				st.setString(3, i.getItem().getName());
				st.setInt(4, 0);
				st.setInt(5, i.getItem().getInvGfx());
				st.setLong(6, i.getCount());
				st.setInt(7, i.getQuantity());
				st.setInt(8, i.getEnLevel());
				st.setInt(9, i.isDefinite() ? 1 : 0);
				st.setInt(10, i.getBress());
				st.setInt(11, i.getDurability());
				st.setInt(12, i.getTime());
				st.setLong(13, i.getPetObjectId());
				st.setLong(14, i.getLetterUid());
				st.setString(15, i.getOption());
				st.setInt(16, i.getEnEarth());
				st.setInt(17, i.getEnWater());
				st.setInt(18, i.getEnFire());
				st.setInt(19, i.getEnWind());
				st.setInt(20, i.getAdd_Min_Dmg());
				st.setInt(21, i.getAdd_Max_Dmg());
				st.setInt(22, i.getAdd_Str());
				st.setInt(23, i.getAdd_Dex());
				st.setInt(24, i.getAdd_Con());
				st.setInt(25, i.getAdd_Int());
				st.setInt(26, i.getAdd_Wiz());
				st.setInt(27, i.getAdd_Cha());
				st.setInt(28, i.getAdd_Mana());
				st.setInt(29, i.getAdd_Hp());
				st.setInt(30, i.getAdd_Manastell());
				st.setInt(31, i.getAdd_Hpstell());
				st.setInt(32, i.getOne());
				st.setInt(33, i.getTwo());
				st.setInt(34, i.getThree());
				st.setInt(35, i.getFour());
				st.setLong(36, i.getSoul_Cha());
				st.executeUpdate();
			}catch(Exception e){
				lineage.share.System.printf("%s : 영혼구슬오류\r\n", ScrollCharLock.class.toString());
				lineage.share.System.println(e);
			}finally{
				DatabaseConnection.close(con, st);
			}
			
			
			//케릭터 이동시키기
			try{
				con = DatabaseConnection.getLineage();
				st = con.prepareStatement("UPDATE characters SET account=?, account_uid=? WHERE objID=?");
				st.setString(1,"999");
				st.setInt(2, 99999);
				st.setLong(3, cha.getObjectId());
				st.executeUpdate();
			}catch(Exception e){
				lineage.share.System.printf("%s : 영혼구슬오류2\r\n", ScrollCharLock.class.toString());
				lineage.share.System.println(e);
			}finally{
				DatabaseConnection.close(con, st);
			}
			
			//해당 구슬 제거
			cha.getInventory().count(this, getCount()-1, true);
			// 사용자 강제종료 시키기.
			pc.toSender(S_Disconnect.clone(BasePacketPooling.getPool(S_Disconnect.class), 0x0A));
			LineageServer.close(pc.getClient());
			
		}
		
		
		
		
		
	}

}
