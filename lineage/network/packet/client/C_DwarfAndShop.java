package lineage.network.packet.client;

import lineage.network.LineageClient;
import lineage.network.packet.BasePacket;
import lineage.network.packet.ClientBasePacket;
import lineage.persnal_shop.PersnalShopInstance;
import lineage.world.World;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class C_DwarfAndShop extends ClientBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length) {
		if (bp == null)
			bp = new C_DwarfAndShop(data, length);
		else
			((C_DwarfAndShop) bp).clone(data, length);
		return bp;
	}

	public C_DwarfAndShop(byte[] data, int length) {
		clone(data, length);
	}

	@Override
	public BasePacket init(PcInstance pc) {
		// 버그방지용
		if (pc == null || pc.isWorldDelete() || !isRead(4) || pc.getInventory() == null)
			return this;

		synchronized (LineageClient.sync_global) {
			long objid = readD();
			if (objid == pc.getObjectId()) {
				try {
					PersnalShopInstance psi = World.findPersnalShop(pc.getObjectId());
					if (psi == null) {
						psi = new PersnalShopInstance(pc.getObjectId(), pc.getName(), pc.getClient().getAccountUid());
					}
					if (pc.isPersnalShopInsert()) {
						psi.addShopItem(pc, this);
					} else if (pc.isPersnalShopPriceSetting()) {
						psi.setShopPriceSetting(pc, this);
					} else if (pc.isPersnalShopEdit()) {
						psi.addEditShopItem(pc, this);
					} else if (pc.isPersnalShopAddPriceSetting()) {
						psi.setShopAddPriceSetting(pc, this);
					} else if (pc.isPersnalShopPriceReset()) {
						psi.setShopResetPriceSetting(pc, this);
					} else if (pc.isPersnalShopSellInsert()) {
						psi.addShopSellItem(pc, this);
					} else if (pc.isPersnalShopSellPriceSetting()) {
						psi.setShopSellPriceSetting(pc, this);
					} else if (pc.isPersnalShopSellEdit()) {
						psi.addDeleteSellShopItem(pc, this);
					} else if (pc.isPersnalShopSellPriceReSetting()) {
						psi.setShopResetSellPriceSetting(pc, this);
					} else if (pc.isPersnalShopPurchasePriceIn()) {
						psi.setShopPurchasePriceIn(pc, this);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {

				object o = pc.findInsideList(objid);

				if (o == null && pc.getTempShop() != null) {
					pc.getTempShop().toDwarfAndShop(pc, this);
					boolean truepc = false;
					if (pc.getGm() > 0) {
						o = World.findPc(objid);
						if (o != null)
							truepc = true;
						;
					}
					if (!truepc) {
						if (pc.isPersnalShopInsert()) {
							pc.setPersnalShopInsert(false);
						} else if (pc.isPersnalShopPriceSetting()) {
							pc.setPersnalShopPriceSetting(false);
						} else if (pc.isPersnalShopEdit()) {
							pc.setPersnalShopEdit(false);
						} else if (pc.isPersnalShopAddPriceSetting()) {
							pc.setPersnalShopAddPriceSetting(false);
						} else if (pc.isPersnalShopPriceReset()) {
							pc.setPersnalShopPriceReset(false);
						} else if (pc.isPersnalShopSellInsert()) {
							pc.setPersnalShopSellInsert(false);
						} else if (pc.isPersnalShopSellPriceSetting()) {
							pc.setPersnalShopSellPriceSetting(false);
						} else if (pc.isPersnalShopSellEdit()) {
							pc.setPersnalShopSellEdit(false);
						} else if (pc.isPersnalShopSellPriceReSetting()) {
							pc.setPersnalShopSellPriceReSetting(false);
						} else if (pc.isPersnalShopPurchasePriceIn()) {
							pc.setPersnalShopPurchasePriceIn(false);
						}
						return this;
					}
				}

				if (o != null && (pc.getGm() > 0 || !pc.isTransparent())) {
					if (o instanceof PersnalShopInstance) {
						PersnalShopInstance psss = (PersnalShopInstance) o;
						psss.toDwarfAndShop(pc, this);
					} else {
						o.toDwarfAndShop(pc, this); 
						
					} 
				}

			}
		}

		return this;
	}
}
