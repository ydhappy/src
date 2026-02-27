package lineage.world.object.monster.quest;

import lineage.bean.database.Item;
import lineage.bean.database.Monster;
import lineage.bean.lineage.Quest;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.QuestController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.RobotInstance;

public class Bugbear extends MonsterInstance {
	
	private Item quest_item;
	
	static synchronized public MonsterInstance clone(MonsterInstance mi, Monster m){
		if(mi == null)
			mi = new Bugbear();
		return MonsterInstance.clone(mi, m);
	}
	
	@Override
	protected void toAiDead(long time){
		if(getMap() == 13){
				if(quest_item == null)
					quest_item = ItemDatabase.find("비밀방 열쇠");
				if(getAttackListSize()==1){
					object o = getAttackList(0);
					if(o instanceof PcInstance && !(o instanceof RobotInstance)) {
						PcInstance pc = (PcInstance)o;
						Quest q = QuestController.find(pc, Lineage.QUEST_KNIGHT_LV30);
						if(q!=null && q.getQuestStep()==6 && pc.getInventory().findDbNameId(quest_item.getNameIdNumber())==null){
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

/*	@Override
	public void readDrop(){
		// 본던 7층 버그베어 일경우 비밀방 열쇠 드랍하도록 하기.
		if(getMap() != 13){
			super.readDrop();
		}else{
			Item item = ItemDatabase.find("비밀방 열쇠");
			if(item != null){
				if(Util.random(0,5000) <= 100 * Lineage.rate_drop){
					ItemInstance ii = ItemDatabase.newInstance(item);
					if(ii != null)
						inv.append(ii, true);
				}
			}
		}
	}*/
}