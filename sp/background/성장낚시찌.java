package sp.background;

import lineage.network.packet.ClientBasePacket;
import lineage.world.World;
import lineage.world.controller.BuffController;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.PcInstance;
import sp.item.성장낚싯대;

public class 성장낚시찌 extends BackgroundInstance {

	static synchronized public BackgroundInstance clone(BackgroundInstance bi){
		if(bi == null)
			bi = new 성장낚시찌();
		return bi;
	}
	
	private lineage.world.object.Character cha;
	private 성장낚싯대 ii;
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		if(cha==null || pc==null) {
			if(!isWorldDelete()) {
				// 월드에서 제거
				World.remove(this);
				// 주변객체 관리 제거
				clearList(true);
			}
			return;
		}
		//
		if(cha.getObjectId()==pc.getObjectId() && ii!=null)
			BuffController.remove(ii, ii.getClass());
	}

	public void toTeleport(lineage.world.object.Character cha, 성장낚싯대 ii, int x, int y, int map, boolean effect) {
		//
		this.cha = cha;
		this.ii = ii;
		//
		toTeleport(x, y, map, effect);
	}

}
