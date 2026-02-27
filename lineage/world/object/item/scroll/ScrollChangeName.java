package lineage.world.object.item.scroll;

import java.sql.Connection;

import lineage.database.AccountDatabase;
import lineage.database.CharactersDatabase;
import lineage.database.DatabaseConnection;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectName;
import lineage.persnal_shop.PersnalShopInstance;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.controller.ClanController;
import lineage.world.controller.PcMarketController;
import lineage.world.controller.RobotController;
import lineage.world.controller.WantedController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ScrollChangeName extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if(item == null)
			item = new ScrollChangeName();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
	PcInstance pc = (PcInstance) cha;
		
//		if (AccountDatabase.getTradeing(pc)) {
//			ChattingController.toChatting(cha, "계정에 아데나거래 등록/진행중인 물품이 있습니다.");
//			return;
//		}
		
		PersnalShopInstance psi = World.findPersnalShop(cha.getObjectId());
		
		if (psi != null) {
			ChattingController.toChatting(cha, "\\fY무인상점을 해제 후 사용하십시오.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
//		if(cha.getClanId()!=0){
//			ChattingController.toChatting(cha, "혈맹 가입 중에는 사용할 수 없습니다.", 20);
//			return;
//		}
		
		//
		cha.getInventory().changeName = this;
		//
		ChattingController.toChatting(cha, "변경 하실 캐릭터명을 입력해 주십시오.", 20);
	}

	@Override
	public void toClickFinal(Character cha, Object... opt) {
		// 
		Connection con = null;
		try {
			con = DatabaseConnection.getLineage();
			//
			String name = (String)opt[0];
			name = name.replaceAll(" ", "").trim();
			//
			if(CharactersDatabase.isCharacterName(con, name) || RobotController.isName(name)) {
				ChattingController.toChatting(cha, "케릭명이 존재합니다. 다시 시도하여 주십시오.", 20);
				return;
			}
			//
			if(CharactersDatabase.isInvalidName(con, name)) {
				ChattingController.toChatting(cha, "이름이 잘못 되었습니다. 다시 시도하여 주십시오.", 20);
				return;
			}
			//
			ClanController.changeName((PcInstance) cha, cha.getName(), name);
			WantedController.changeName(cha.getName(), name);
			CharactersDatabase.updateCharacterName(cha.getName(), name);
			cha.setName(name);
			cha.toSender(S_ObjectName.clone(BasePacketPooling.getPool(S_ObjectName.class), cha), true);
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(con);
		}
		//
		cha.getInventory().count(this, getCount()-1, true);
	}
}
