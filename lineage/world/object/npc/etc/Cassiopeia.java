package lineage.world.object.npc.etc;

import lineage.bean.database.Exp;
import lineage.database.ExpDatabase;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.util.Util;
import lineage.world.controller.CharacterController;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.Haste;
import lineage.world.object.magic.Heal;

public class Cassiopeia extends object {

	private long message_time = 0;
	private boolean message_first = false;
	
	public Cassiopeia() {
		CharacterController.toWorldJoin(this);
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		if(pc.getLevel() == 1) {
			// 레벨 2로
			Exp e = ExpDatabase.find(pc.getLevel());
			pc.setExp( e.getBonus() );
			//
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "newtutor1"));
		} else {
			//
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "newtutor2"));
		}
		// 힐, 헤이스트
		if(pc.getLevel() == 2) {
			Heal.onBuff(pc, SkillDatabase.find(1, 0), 1000);
			Haste.onBuff(pc, SkillDatabase.find(6, 2));
		}
	}

	@Override
	public void toTimer(long time) {
		//
		if(time-message_time >= 1000*30) {
			if(message_first == false) {
				toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), this, 0x15, "$18991"), false);
				message_time = time - (1000 * 27);
			} else {
				toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), this, 0x15, "$18992"), false);
				message_time = time + (1000 * Util.random(10, 20));
			}
			message_first = !message_first;
		}
		//
		int sec = (int)(time / 1000);
		if(sec%3 != 0)
			return;
		for(object o : getInsideList(true)) {
			if(!(o instanceof PcInstance))
				continue;
			if(Util.isDistance(this, o, 7)) {
				o.setNowMp(o.getNowMp() + Util.random(1, 4));
				o.setNowHp(o.getNowHp() + Util.random(3, 6));
			}
		}
	}
}
