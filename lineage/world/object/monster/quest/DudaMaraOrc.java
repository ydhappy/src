package lineage.world.object.monster.quest;

import lineage.bean.database.Item;
import lineage.bean.database.Monster;
import lineage.bean.lineage.Quest;
import lineage.database.ItemDatabase;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.CraftController;
import lineage.world.controller.QuestController;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;


public class DudaMaraOrc extends MonsterInstance {

	private Item quest_item;

	static synchronized public MonsterInstance clone(MonsterInstance mi, Monster m) {
		if (mi == null)
			mi = new DudaMaraOrc();
		return MonsterInstance.clone(mi, m);
	}

	public DudaMaraOrc() {
		quest_item = ItemDatabase.find("두다-마라의 토템");
	}

	@Override
	protected void toAiDead(long time) {
		// 공격자 검색 하여 공격자가 1명이며, 퀘스트진행중이고 퀘스트스탭이 맞을경우 퀘아이템 오토루팅 지급 처리.
		if (quest_item != null && getAttackListSize() == 1) {
			object o = getAttackList(0);
			if (o instanceof PcInstance) {
				PcInstance pc = (PcInstance) o;
				Quest q = QuestController.find(pc, Lineage.QUEST_LYRA);
				if (q != null && q.getQuestStep() == 1 && Util.random(0, 100) < 100)
					CraftController.toCraft(this, pc, quest_item, 1, true, 0, 0, 1);
			}
		}
		super.toAiDead(time);
	}

}
