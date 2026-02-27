package lineage.world.object.npc.kingdom;

import lineage.bean.database.Npc;
import lineage.share.Lineage;
import lineage.world.controller.KingdomController;

public class Kentu extends KingdomSoldierShop {

	public Kentu(Npc n) {
		super(n);
		kingdom = KingdomController.find(Lineage.KINGDOM_ORCISH);
		html = "kentu";
		soldier_name = "'아투바'단";
	}

}
