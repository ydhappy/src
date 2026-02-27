package lineage.world.object.npc.kingdom;

import lineage.bean.database.Npc;
import lineage.share.Lineage;
import lineage.world.controller.KingdomController;

public class Freckson extends KingdomSoldierShop {

	public Freckson(Npc n){
		super(n);
		kingdom = KingdomController.find(6);
		html = "freckson";
		soldier_name = "강철의 문";
	}

}
