package jsn_soft;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Npc;
import lineage.bean.lineage.Kingdom;
import lineage.database.ServerDatabase;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectTitle;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CommandController;
import lineage.world.controller.KingdomController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.AdvanceSpirit;
import lineage.world.object.magic.BlessWeapon;
import lineage.world.object.magic.BlessedArmor;
import lineage.world.object.magic.DecreaseWeight;
import lineage.world.object.magic.EarthSkin;
import lineage.world.object.magic.EnchantDexterity;
import lineage.world.object.magic.EnchantMighty;
import lineage.world.object.magic.Haste;

public class 윈성버프강화 extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "kingdom_w1"));

	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {

		for (Kingdom k : KingdomController.getList()) {
			Kingdom k1 = KingdomController.find(pc);
			if (k1 != null && k != null && k.getUid() == Lineage.KINGDOM_WINDAWOOD &&  k.getAgentId() == k1.getAgentId()) {

				if (action.equalsIgnoreCase("kingdom_windnpc")) {
					if (pc.getInventory().isAden("아데나", 10000, true)) {
						Kingdom_wind_buff.init(pc);
						EnchantDexterity.onBuff(pc, SkillDatabase.find(4, 1));
						// 힘업
						EnchantMighty.onBuff(pc, SkillDatabase.find(6, 1));
					} else
						ChattingController.toChatting(pc, "\\fF[아데나]가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			}
		}
	}
}