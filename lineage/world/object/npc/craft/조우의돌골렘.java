package lineage.world.object.npc.craft;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.database.ItemDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.instance.CraftInstance;
import lineage.world.object.instance.PcInstance;

public class 조우의돌골렘 extends CraftInstance {

	public 조우의돌골렘(Npc npc) {
		super(npc);

		// hyper text 패킷 구성에 해당 구간을 npc 이름으로 사용함.
		temp_request_list.add(npc.getNameId());

		// 제작 처리 초기화.
		Item i = ItemDatabase.find("균열의 핵");
		if (i != null) {
			craft_list.put("A", i);

			List<Craft> l = new ArrayList<Craft>();
			l.add(new Craft(ItemDatabase.find("시간의 균열 파편"), 100));
			list.put(i, l);
		}
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
                if(pc.getMap() == 780) {
		pc.toSender(new S_Html( this, "joegolem17"));
                }
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		if(action.equalsIgnoreCase("A")) {
			// 균열의 핵을 만든다.
			super.toTalk(pc, action, type, cbp, opt);
		} else if(action.equalsIgnoreCase("B")) {
			// 아덴으로 텔레포트 한다.
			pc.toSender(new S_Html( this, ""));
			if(pc.getInventory().isAden("시간의 균열 파편", 1, true))
				pc.toTeleport(33439, 32812, 4, true);
			else
				pc.toSender(new S_Html( this, "joegolem20"));
		}
	}
}
