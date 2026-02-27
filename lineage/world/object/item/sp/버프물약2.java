package lineage.world.object.item.sp;

import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.BlessedArmor;
import lineage.world.object.magic.EnchantDexterity;
import lineage.world.object.magic.EnchantMighty;
import lineage.world.object.magic.EnchantWeapon;
import lineage.world.object.magic.Haste;

public class 버프물약2 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 버프물약2();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if ((cha instanceof PcInstance)) {
			PcInstance pc = (PcInstance) cha;

			Haste.onBuff(pc, SkillDatabase.find(6, 2));

			EnchantDexterity.onBuff(pc, SkillDatabase.find(4, 1));

			EnchantMighty.onBuff(pc, SkillDatabase.find(6, 1));

			Skill localSkill1 = SkillDatabase.find(3, 4);
			//
			Skill localSkill2 = SkillDatabase.find(2, 3);
			//
			BlessedArmor.onBuff(pc,
					pc.getInventory().getSlot(Lineage.SLOT_ARMOR), localSkill1,
					localSkill1.getBuffDuration());

			EnchantWeapon.onBuff(pc,
					pc.getInventory().getSlot(Lineage.SLOT_WEAPON),
					localSkill2, localSkill2.getBuffDuration());
				}
			}
		}
	
