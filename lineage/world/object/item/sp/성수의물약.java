package lineage.world.object.item.sp;

import lineage.bean.lineage.Inventory;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 성수의물약 extends ItemInstance 
{
	public static synchronized ItemInstance clone(ItemInstance ii)
	{
		if (ii == null)
			ii = new 성수의물약();
		return ii;
	}
	public void toClick(Character cha, ClientBasePacket cbp)
	{
		if ((cha instanceof PcInstance))
		{
			PcInstance pc = (PcInstance)cha;
			
			Lineage.rate_drop = 2;
			
			getInventory().count(this, getCount() - 1L, true);
			ChattingController.toChatting(pc, "드랍률이 2배 적용됩니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
}
