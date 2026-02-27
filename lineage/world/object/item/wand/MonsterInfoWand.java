package lineage.world.object.item.wand;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import jsn_soft.S_MonDrop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage.bean.database.Drop;
import lineage.bean.database.Item;
import lineage.bean.database.Monster;
import lineage.bean.lineage.Summon;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.database.MonsterDatabase;
import lineage.database.MonsterDropDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_LetterNotice;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.controller.LetterController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.PetInstance;
import lineage.world.object.instance.SummonInstance;
import lineage.world.object.magic.ShapeChange;

public class MonsterInfoWand extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new MonsterInfoWand();
		return item;
	}

	@Override
	public ItemInstance clone(Item item) {

		return super.clone(item);
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		long objid = cbp.readD();
		object o = cha.findInsideList(objid);
		if (o instanceof MonsterInstance) {
		} else {
			ChattingController.toChatting(cha, "몬스터에게만 사용가능 합니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
		if (o != null && o instanceof MonsterInstance && !(o instanceof SummonInstance)
				&& !(o instanceof PetInstance)) {
			String str = o.getMonster().getName();

			if (o.getMonster().getFaust() != null && o.getMonster().getFaust().length() > 0) {
				str = o.getMonster().getFaust();
			}
			
			Monster m = MonsterDatabase.find(str);

			if (m != null) {
				if (m.getDropList().size() > 0) {
					cha.toSender(S_MonDrop.clone(BasePacketPooling.getPool(S_MonDrop.class), m));
					} else {
						ChattingController.toChatting(cha, "해당 몬스터는 드랍목록이 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				}

			}

		}
	}

}
