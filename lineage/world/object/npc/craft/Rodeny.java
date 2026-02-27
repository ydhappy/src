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

public class Rodeny extends CraftInstance {

	public Rodeny(Npc npc){
		super(npc);
		
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
	pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "dogdealer"));
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
			switch(action) {
				case "buy 1":	// 도베르만 50000
					pet_name = "도베르만";
					pet_level = 8;
					pet_hp = 20 + Util.random(6, 12);
					pet_mp = 5 + Util.random(2, 4);
					item = 50000;
					break;
				case "buy 2":	// 세퍼드 50000
					pet_name = "세퍼드";
					pet_level = 7;
					pet_hp = 30 + Util.random(10, 14);
					pet_mp = 5 + Util.random(2, 4);
					item = 50000;
					break;
				case "buy 3":	// 비글 50000
					pet_name = "비글";
					pet_level = 5;
					pet_hp = 20 + Util.random(8, 12);
					pet_mp = 30 + Util.random(6, 12);
					item = 50000;
					break;
				case "buy 4":	// 세인트 버나드 50000
					pet_name = "세인트 버나드";
					pet_level = 5 + 3;
					pet_hp = 30 + Util.random(7, 8) + Util.random(7, 8) + Util.random(7, 8);
					pet_mp = 30 + Util.random(4, 5) + Util.random(4, 5) + Util.random(4, 5);
					item = 50000;
					break;
			}
		//
			if (pet_name == null)
				return;

			if (pc.getInventory().isAden("아데나", item, false) == false) {
				ChattingController.toChatting(pc, "아데나가 부족합니다.",
						Lineage.CHATTING_MODE_MESSAGE);
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
				pc.getInventory().isAden("아데나", item, true);
			else
				ChattingController.toChatting(pc, "구매하실려는 펫이 너무 많습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
			//
			MonsterSpawnlistDatabase.setPool(mi);
		}

	}