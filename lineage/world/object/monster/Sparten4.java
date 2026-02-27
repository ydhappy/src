package lineage.world.object.monster;

import lineage.bean.database.Monster;
import lineage.util.Util;
import lineage.world.controller.SkillController;
import lineage.world.controller.LocationController;
import lineage.world.object.Character;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.magic.Teleport;


public class Sparten4 extends MonsterInstance {

	static synchronized public MonsterInstance clone(MonsterInstance mi,
			Monster m) {
		if (mi == null)
			mi = new Sparten4();
		return MonsterInstance.clone(mi, m);

	}



	@Override
	public void toDamage(Character cha, int dmg, int type, Object... cbp) {
		super.toDamage(cha, dmg, type);
		// 랜덤 텔레포트 4층
		if (SkillController.isMagic(cha, null, false)) {
			if (LocationController.isTeleportVerrYedHoraeZone(cha, true))
				Util.toRndLocation(cha);
				cha.toTeleport(cha.getHomeX(), cha.getHomeY(), 811, true);
			return;
		}

		Teleport.unLock(cha, true);
	}
}

