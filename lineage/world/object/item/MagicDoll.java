package lineage.world.object.item;

import java.sql.Connection;

import lineage.network.packet.ClientBasePacket;
import lineage.world.controller.MagicDollController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class MagicDoll extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new MagicDoll();
		return item;
	}
	
	@Override
	public void close() {
		super.close();
		
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if(cha instanceof PcInstance) {
			PcInstance pc = (PcInstance)cha;
			 
			if (pc.getMagicDollinstance() == null) {
				// 마법인형 소환.
				MagicDollController.toEnabled(pc, this);
			} else {
				if (pc.getMagicDoll().getObjectId() != getObjectId()) {
					// 마법인형 제거.
					MagicDollController.toDisable(pc, pc.getMagicDoll(), false);
					MagicDollController.toEnabled(pc, this);
				} else {
					MagicDollController.toDisable(pc, this, true);
				}
			}
		}
	}
	
	@Override
	public void toWorldJoin(Connection con, PcInstance pc){
		super.toWorldJoin(con, pc);

		setEquipped(false);
	}

}
