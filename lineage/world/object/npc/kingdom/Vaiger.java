package lineage.world.object.npc.kingdom;

import lineage.bean.database.Npc;
import lineage.share.Lineage;
import lineage.world.controller.KingdomController;

public class Vaiger extends KingdomSoldierShop {

	public Vaiger(Npc n){
		super(n);
		kingdom = KingdomController.find(5);
		html = "vaiger";
		soldier_name = "포이즌 서펜트";
	}

}
