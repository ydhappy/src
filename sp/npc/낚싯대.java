package sp.npc;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.ShopInstance;
import sp.controller.FishingController;
import sp.network.packet.server.S_ShopBuyFishing;

public class 낚싯대 extends ShopInstance {

	public 낚싯대(Npc npc) {
		super(npc);
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "FishingBoy"));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if(action.equalsIgnoreCase("buy")) {
			pc.toSender(S_ShopBuyFishing.clone(BasePacketPooling.getPool(S_ShopBuyFishing.class), this, pc));
		} else {
			super.toTalk(pc, action, type, cbp, opt);
		}
	}
	
	@Override
	protected void toBuy(PcInstance pc, ClientBasePacket cbp){
		super.toBuy(pc, cbp);
	}
	
	static public void toBuySeccess(PcInstance pc, Item item) {
		// 구입 성공한 아이템이 미끼라면 구입한 시간 갱신 하기.
		if(item.getName().equalsIgnoreCase("미끼"))
			FishingController.update(pc.getClient().getAccountIp());
	}
	
}
