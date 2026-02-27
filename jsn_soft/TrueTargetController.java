package jsn_soft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.lineage.Clan;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class TrueTargetController {
	// 이펙트를 보낼 오브젝트
	public static List<object> list = new ArrayList<object>();
	// 관리 목록.
	// 관리목록은 이펙트를 보내야하는 오브젝트와 이팩트를 보여줘야할 오브젝트 두가지로 나눈다.
	// 이팩트를 보여줘야할 클랜
	public static Map<object, Clan> list_clan = new HashMap<object, Clan>();
	

	public static void toTimer(long time) {
		for (object o : list) {
			o.setTrueTargetTime(o.getTrueTargetTime() - 1);

			if (o.getTrueTargetTime() > 0) {
				Clan c = list_clan.get(o);
				if (c != null) {
					for (PcInstance pc : c.getList()) {
						if (Util.isDistance(o, pc, Lineage.SEARCH_LOCATIONRANGE)) {
							pc.toSenderMe(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 1765));
						}
					}
				}
			} else {
				list.remove(o);
				list_clan.remove(o);
			}
		}
	}

	public static void append(object o, Clan c) {
		if (list.contains(o))
			return;
		
		list.add(o);
		list_clan.put(o, c);
	}

}
