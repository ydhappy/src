package lineage.network.packet.client;

import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.ClientBasePacket;
import lineage.persnal_shop.PersnalShopInstance;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class C_TaxGet extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_TaxGet(data, length);
		else
			((C_TaxGet)bp).clone(data, length);
		return bp;
	}
	
	public C_TaxGet(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		// 버그 방지.
		if(pc==null || pc.isWorldDelete() || !isRead(8))
			return this;
		
		object o = pc.findInsideList(readD());
		
		if (o instanceof PersnalShopInstance) {
			PersnalShopInstance psi = (PersnalShopInstance) o;

			long out_count = readD();
			if (psi.getPurchasePrice() < out_count) {
				System.out.println("가지고있는 매입대금보다 많은 금액을 찾으려는 유저가 있습니다(버그의심) [" + pc.getName() + "]");
				return this;
			}

			if (out_count > 0) {
				psi.addPurchasePrice(-out_count);

				ItemInstance aden = pc.getInventory().find("아데나", true);
				if (aden == null) {
					aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
					aden.setObjectId(ServerDatabase.nextItemObjId());
					aden.setCount(0);
					pc.getInventory().append(aden, true);
				}
				//
				// 이용자에게 아데나 지급
				pc.getInventory().count(aden, aden.getCount() + out_count, true);
				ChattingController.toChatting(pc, " 아데나 (" + Util.getPriceFormat((int) out_count) + ") \\fY대금 회수가 완료되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
			return this;
		}
		
		if(o != null)
			o.toTaxGet(pc, readD());
		
		return this;
	}

}
