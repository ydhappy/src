package lineage.world.object.item.sp;

import lineage.database.SkillDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.AdvanceSpirit;
import lineage.world.object.magic.FireWeapon;
import lineage.world.object.magic.EnchantDexterity;
import lineage.world.object.magic.EnchantMighty;
import lineage.world.object.magic.EarthSkin;
import lineage.world.object.magic.WindShot;

public class 버프물약3 extends ItemInstance 
{
	public static synchronized ItemInstance clone(ItemInstance ii)
	{
		if (ii == null)
			ii = new 버프물약3();
		return ii;
	}
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if(cha instanceof PcInstance){
			PcInstance pc = (PcInstance)cha;
			AdvanceSpirit.onBuff(cha, SkillDatabase.find(9, 2));
			EnchantDexterity.onBuff(pc, SkillDatabase.find(4, 1));
			EnchantMighty.onBuff(pc, SkillDatabase.find(6, 1));
			EarthSkin.onBuff(pc, SkillDatabase.find(19, 6));
			ItemInstance weapon = cha.getInventory().getSlot(Lineage.SLOT_WEAPON);
			if (weapon != null) {
				if (weapon.getItem().getType2().equalsIgnoreCase("bow")) {
					WindShot.onBuff(cha, SkillDatabase.find(19, 4));
				} else {
					FireWeapon.onBuff(cha, SkillDatabase.find(19, 3));
				}
			}
		}
	}
}
