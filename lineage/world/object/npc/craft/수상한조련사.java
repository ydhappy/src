package lineage.world.object.npc.craft;

import lineage.bean.database.Npc;
import lineage.database.MonsterDatabase;
import lineage.database.MonsterSpawnlistDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SummonController;
import lineage.world.object.instance.CraftInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class 수상한조련사 extends CraftInstance {

	public 수상한조련사(Npc npc) {
		super(npc);

	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "subsusp1"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		//
		String pet_name = null;
		int pet_level = 0;
		int pet_hp = 0;
		int pet_mp = 0;
		int item = 0;
		//
		switch (action) {
		case "buy 4":
			pet_name = "아기 진돗개";
			pet_level = 15;
			pet_hp = 100 + Util.random(6, 12);
			pet_mp = 70 + Util.random(2, 4);
			item = 1000;
			break;
		case "buy 5":
			pet_name = "아기 판다곰";
			pet_level = 15;
			pet_hp = 100 + Util.random(6, 12);
			pet_mp = 70 + Util.random(2, 4);
			item = 1000;
			break;
		case "buy 6":
			pet_name = "아기 캥거루";
			pet_level = 15;
			pet_hp = 100 + Util.random(6, 12);
			pet_mp = 70 + Util.random(2, 4);
			item = 1000;
			break;
		}

		if (pet_name == null)
			return;

		if (pc.getInventory().isAden("신비한 날개깃털", item, false) == false) {
			ChattingController.toChatting(pc, "신비한 날개깃털이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		//
		MonsterInstance mi = MonsterSpawnlistDatabase
				.newInstance(MonsterDatabase.find(pet_name));
		mi.setLevel(pet_level);
		mi.setMaxHp(pet_hp);
		mi.setMaxMp(pet_mp);
		mi.setNowHp(pet_hp);
		mi.setNowMp(pet_mp);
		mi.setX(pc.getX());
		mi.setY(pc.getY());
		mi.setMap(pc.getMap());
		//
		if (SummonController.toPet(pc, mi))
			pc.getInventory().isAden("신비한 날개깃털", item, true);
		else
			ChattingController.toChatting(pc, "구매하실려는 펫이 너무 많습니다.", Lineage.CHATTING_MODE_MESSAGE);
		//
		MonsterSpawnlistDatabase.setPool(mi);
	}

}
