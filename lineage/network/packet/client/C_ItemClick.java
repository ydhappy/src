package lineage.network.packet.client;

import jsn_soft.AutoHuntController;
import jsn_soft.AutoHuntThread;
import jsn_soft.Plugins_Of_Test;
import lineage.network.packet.BasePacket;
import lineage.network.packet.ClientBasePacket;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.wand.EbonyWand;

public class C_ItemClick extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_ItemClick(data, length);
		else
			((C_ItemClick)bp).clone(data, length);
		return bp;
	}
	
	public C_ItemClick(byte[] data, int length){	
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc) {
		// 버그 방지.
		if(pc==null || pc.getInventory()==null || !isRead(4) || pc.isDead() || pc.isWorldDelete())
			return this;
		long id =  readD();
		ItemInstance item = pc.getInventory().value(id );
		if(item!=null && item.isClick(pc) && (pc.getGm()>0 || !pc.isTransparent())) {//아까 여기는 머가문제엿어여?우선순위문제였어여 PluginController 얘가 방해해서 그럼 이제는 앞에가 방해해서 뒤가 문제 될건없는거에요?
	
			if(pc.isPersnalShopInsert() || pc.isPersnalShopEdit() || pc.isPersnalShopAddPriceSetting() ||
					pc.isPersnalShopPriceSetting() || pc.isPersnalShopSellInsert() || pc.isPersnalShopSellPriceSetting()
					||pc.isPersnalShopSellPriceReSetting() || pc.isPersnalShopPurchasePriceIn() ||pc.isPersnalShopPriceReset()
					||pc.isPersnalShopSellEdit()){
				ChattingController.toChatting(pc, "개인상점 이용중에는 인벤 클릭이 불가능합니다");
				return this;
			}
			
			if(!AutoHuntController.toChatting(pc, String.valueOf(id)) && PluginController.init(C_ItemClick.class, "init", this, pc, item) == null)
				if (item.getItem().getType2().equalsIgnoreCase("potion") || item.getItem().getType2().equalsIgnoreCase("무한물약")) {
					if (pc.isAutoHunt() && AutoHuntThread.isDelayTime(pc, item.getItem().getContinuous()))
						item.toClick(pc, this);
					else 
						item.toClick(pc, this);
				} else
					item.toClick(pc, this);
		}	
		
		return this;
	}
}
