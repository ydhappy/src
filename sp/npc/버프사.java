package sp.npc;

import lineage.bean.database.Npc;
import lineage.database.SkillDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.AdvanceSpirit;
import lineage.world.object.magic.BlessWeapon;
import lineage.world.object.magic.EnchantDexterity;
import lineage.world.object.magic.EnchantMighty;
import lineage.world.object.magic.FireWeapon;
import lineage.world.object.magic.Haste;
import lineage.world.object.magic.Heal;
//import lineage.world.object.magic.IronSkin;
import lineage.world.object.magic.EarthSkin;
import lineage.world.object.magic.WindShot;

public class 버프사 extends object {

	public 버프사(Npc npc){
		
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
//		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "red_buff"));
		toTalk(pc, null, null, null);
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt) {
		// 10000아데나 확인.
		if(!pc.getInventory().isAden(30000, true)) {
			ChattingController.toChatting(pc, "버프를 받기 위해서는 30,000 아데나가 필요합니다.", 20);
			return;
		}
		// 덱업
		EnchantDexterity.onBuff(pc, SkillDatabase.find(4, 1));
		// 힘업
		EnchantMighty.onBuff(pc, SkillDatabase.find(6, 1));
		// 헤이스트
		Haste.onBuff(pc, SkillDatabase.find(6, 2));
		// 블레스 웨폰
		BlessWeapon.onBuff(pc, SkillDatabase.find(6, 7));
		// 풀 힐
		Heal.onBuff(pc, SkillDatabase.find(8, 0), 600);
		// 아이언 스킨
		//IronSkin.onBuff(pc, SkillDatabase.find(21, 7));
		// 어스 스킨.성환(변경)
		EarthSkin.onBuff(pc, SkillDatabase.find(19, 6));
		//
		ItemInstance weapon = pc.getInventory().getSlot(8);
		if(weapon != null) {
			if(weapon.getItem().getType2().equalsIgnoreCase("bow")) {
				WindShot.onBuff(pc, SkillDatabase.find(19, 4));
			} else {
				FireWeapon.onBuff(pc, SkillDatabase.find(19, 3));
			}
		}

	}
	
}
