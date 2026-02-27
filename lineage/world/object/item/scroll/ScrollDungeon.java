package lineage.world.object.item.scroll;

import lineage.bean.database.DungeonPartTime;
import lineage.database.DungeonPartTimeDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.DungeonController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ScrollDungeon extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollDungeon();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		try {
			// 
			int uid = Integer.valueOf( getItem().getType2().substring( getItem().getType2().indexOf("_")+1 ) );
			DungeonPartTime d = DungeonPartTimeDatabase.findUid(uid);
			if(d == null)
				return;
			//
			DungeonPartTime dungeon = DungeonController.find((PcInstance)cha, uid);
			if(dungeon == null) {
				dungeon = new DungeonPartTime();
				dungeon.setUid(uid);
				DungeonController.append((PcInstance)cha, dungeon);
			}
			//
			dungeon.setTime(d.getTime());
			dungeon.setUpdateTime(System.currentTimeMillis());
			//
			ChattingController.toChatting(cha, d.getName()+" 시간이 충전 되었습니다.", 20);
		} catch (Exception e) { }
		// 제거
		cha.getInventory().count(this, getCount()-1, true);
	}

}
