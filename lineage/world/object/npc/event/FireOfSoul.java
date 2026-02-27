package lineage.world.object.npc.event;

import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.world.controller.CraftController;
import lineage.world.controller.GhostHouseController;
import lineage.world.object.Character;
import lineage.world.object.instance.BackgroundInstance;

public class FireOfSoul extends BackgroundInstance {

	static synchronized public BackgroundInstance clone(BackgroundInstance bi){
		if(bi == null)
			bi = new FireOfSoul();
		return bi;
	}

	@Override
	public void toDamage(Character cha, int dmg, int type, Object... opt) {
		// 
		if(PluginController.init(FireOfSoul.class, "toDamage", this, cha) != null)
			return;
		//
		setGfxMode(8);
		// 호박주머니 지급
		CraftController.toCraft(this, cha, ItemDatabase.find(5287), 1, true, 0, 0, 1);
		// 유령의집 이벤트 종료
		GhostHouseController.toEventEnd();
	}
	
	@Override
	public void setGfxMode(int gfxMode) {
		super.setGfxMode(gfxMode);
		toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), this, gfxMode), false);
	}
	
}
