package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.world.object.Character;
import lineage.world.object.object;

public class FouSlayer {

	static public void init(Character cha, Skill skill, long object_id) {
		// 초기화
		object o = cha.findInsideList( object_id );
		// 처리
		if(o != null) {
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 7020), true);
			o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 6509), true);
		}
	}
	
}
