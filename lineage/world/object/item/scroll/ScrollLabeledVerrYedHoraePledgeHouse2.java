package lineage.world.object.item.scroll;

import lineage.bean.lineage.Agit;
import lineage.bean.lineage.Kingdom;
import lineage.database.TeleportHomeDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.AgitController;
import lineage.world.controller.KingdomController;
import lineage.world.controller.LocationController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ScrollLabeledVerrYedHoraePledgeHouse2 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ScrollLabeledVerrYedHoraePledgeHouse2();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		cha.getInventory().count(this, getCount() - 1, true);

		if (LocationController.isTeleportVerrYedHoraeZone(cha, true)) {
			if (cha instanceof PcInstance) {
				PcInstance pc = (PcInstance) cha;
				// 성 확인.
				for (Kingdom k : KingdomController.getList()) {
					Kingdom k1 = KingdomController.find(pc);
					if (k1 != null && k != null && k.getUid() == Lineage.KINGDOM_KENT && k.getClanId() == k1.getClanId()) {
						if (Lineage.server_version <= 200)
							cha.toTeleport(k.getX(), k.getY(), k.getMap(), true);
						else
							cha.toPotal(k.getX(), k.getY(), k.getMap());
						return;
					}
				}
				// 아지트 확인.
				Agit a = AgitController.find(pc);
				if (a != null) {
					if (Lineage.server_version <= 200)
						cha.toTeleport(a.getAgitX(), a.getAgitY(), a.getAgitMap(), true);
					else
						cha.toPotal(a.getAgitX(), a.getAgitY(), a.getAgitMap());
					return;
				}
			}

			// 이도저도 아니면 좌표를 통한 귀환.
			TeleportHomeDatabase.toLocation(cha);
			if (Lineage.server_version <= 200)
				cha.toTeleport(cha.getHomeX(), cha.getHomeY(), cha.getHomeMap(), true);
			else
				cha.toPotal(cha.getHomeX(), cha.getHomeY(), cha.getHomeMap());
		}
	}

}
