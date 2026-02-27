package lineage.world.object.monster.quest;

import lineage.bean.database.Item;
import lineage.bean.database.Monster;
import lineage.bean.lineage.Quest;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectMode;
import lineage.share.Lineage;
import lineage.world.controller.QuestController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.RobotInstance;

public class Skeleton extends MonsterInstance {

	private Item quest_item;
	
	static synchronized public MonsterInstance clone(MonsterInstance mi, Monster m){
		if(mi == null)
			mi = new Skeleton();
		return MonsterInstance.clone(mi, m);
	}
	
	@Override
	protected void toAiDead(long time){
		if(getMap() == 201){
			if(quest_item == null)
				quest_item = ItemDatabase.find("해골 열쇠");
			if(getAttackListSize()==1){
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
		}
	super.toAiDead(time);
}
	
	@Override
	public void toTeleport(final int x, final int y, final int map, final boolean effect){
		// 땅속에 박힌 모드로 변경.
		// standby
		setGfxMode(28);
		// 텔레포트 처리.
		super.toTeleport(x, y, map, effect);
		// 땅속에서 나오는 액션 취하기.
		// rise
		if(getGfxMode() == 28){
			toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), this, 30), false);
			setGfxMode(getClassGfxMode());
			toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), this), false);
		}
	}
}
