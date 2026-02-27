package lineage.world.object.monster.quest;

import lineage.bean.database.Item;
import lineage.bean.database.Monster;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.world.controller.BuffController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.RobotInstance;
import lineage.world.object.magic.item.신성한에바의물;

public class CursedExorcistSaell extends MonsterInstance {

	private Item quest_item;

	static synchronized public MonsterInstance clone(MonsterInstance mi,
			Monster m) {
		if (mi == null)
			mi = new CursedExorcistSaell();
		return MonsterInstance.clone(mi, m);
	}

	public CursedExorcistSaell() {
		quest_item = ItemDatabase.find("사엘의 반지");
	}

	@Override
	public boolean isAttack(Character cha, boolean magic) {
		try {
			return BuffController.find(cha).find(신성한에바의물.class) != null;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void readDrop() {
		// 공격자 검색 하여 공격자가 1명이며, 1개이상 지급못하도록 하기위해 인벤 검색도 함.
		if (quest_item != null && getAttackListSize() == 1) {
			object o = getAttackList(0);
			if (o instanceof PcInstance && !(o instanceof RobotInstance)) {
				PcInstance pc = (PcInstance) o;
				if (pc.getInventory().findDbNameId(quest_item.getNameIdNumber()) == null) {
					ItemInstance ii = ItemDatabase.newInstance(quest_item);
					ii.setObjectId(ServerDatabase.nextItemObjId());
					pc.getInventory().append(ii, true);
					// \f1%0%s 당신에게 %1%o 주었습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 143, getName(), ii.getName()));
				}
			}
		}
		super.readDrop();
	}
}