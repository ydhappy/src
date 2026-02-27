package sp.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.server.S_Inventory;
import lineage.share.Lineage;
import sp.bean.shop;
import sp.object.PcShopInstance;

public class S_ShopBuy extends S_Inventory {

	static synchronized public BasePacket clone(BasePacket bp, PcShopInstance psi) {
		if (bp == null)
			bp = new S_ShopBuy(psi);
		else
			((S_ShopBuy) bp).toClone(psi);
		return bp;
	}

	public S_ShopBuy(PcShopInstance psi) {
		toClone(psi);
	}

	public void toClone(PcShopInstance psi) {
		clear();

		writeC(Opcodes.S_OPCODE_SHOPBUY);
		writeD(psi.getObjectId());

		// 일반상점 구성구간.
		writeH(psi.list.size());

		for (shop s : psi.list.values()) {
			writeD(s.getInvItemObjectId());
			writeH(s.getItem().getInvGfx());
			writeD(s.getPrice());

			StringBuffer sb = new StringBuffer();
			// 축저주 구분
			sb.append("[").append(s.getInvItemBress() == 0 ? "축복 " : (s.getInvItemBress() == 1 ? "일반" : "저주 ")).append("]");
			// 인첸트 레벨 표현
			if (s.getInvItemEn() != 0 && (s.getItem().getType1().equalsIgnoreCase("weapon") || s.getItem().getType1().equalsIgnoreCase("armor")))
				sb.append(" ").append(s.getInvItemEn() >= 0 ? "+" : "-").append(s.getInvItemEn());
			// 이름 표현
			sb.append(" ").append(s.getItem().getName());
			// 수량 표현
			if (s.getInvItemCount() > 1)
				sb.append(" (").append(s.getInvItemCount()).append(")");
			writeS(sb.toString());

			if (Lineage.server_version > 144) {
				if (s.getItem().getType1().equalsIgnoreCase("armor")) {
					toArmor(s.getItem(), 0, s.getInvItemEn(), (int)s.getItem().getWeight(), 0, s.getInvItemBress(), 0, 0, 0);
				} else if (s.getItem().getType1().equalsIgnoreCase("weapon")) {
					toWeapon(s.getItem(), 0, s.getInvItemEn(), (int) s.getItem().getWeight(), s.getInvItemBress(), 0, 0, 0, 0);
				} else {
					toEtc(s.getItem(), (int) s.getItem().getWeight());
				}
			}
		}
	}

}
