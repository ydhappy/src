package lineage.world.object.item.cloak;

import lineage.bean.lineage.Inventory;
import lineage.network.packet.ClientBasePacket;
import lineage.world.controller.BuffController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.AbsoluteBarrier;

public class CloakInvisibility extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new CloakInvisibility();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		// 앱솔상태 해제.
		if (cha.isBuffAbsoluteBarrier())
		BuffController.remove(cha, AbsoluteBarrier.class);
		
		PcInstance pc = (PcInstance)cha;
		// 3단착용 구현.
		if(cha.isInvis() && !cha.isBuffInvisiBility() && equipped){
			cha.setInvis(false);
			cha.setTransparent(false);
			pc.setTimeInv(2);
			pc.setChkInv(true);
		}else{
			super.toClick(cha, cbp);
		}
	}
	
	@Override
	public void toEquipped(Character cha, Inventory inv, int slot){
		
		super.toEquipped(cha, inv, slot);

		if(equipped){
			cha.setInvis(equipped);
		}else{
			cha.setTransparent(equipped);
			// 인비지블리티 마법시전상태 확인 : 시전상태가 아닐경우 투망해제.
			if(!cha.isBuffInvisiBility())
				cha.setInvis(equipped);
			    cha.set투망시간(System.currentTimeMillis() + 2000);
		}
	}
}
