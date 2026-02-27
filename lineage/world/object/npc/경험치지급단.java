package lineage.world.object.npc;

import lineage.bean.database.Exp;
import lineage.database.ExpDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.GameSetting;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class 경험치지급단 extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "expgivenpc"));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){

		if(action.equalsIgnoreCase("a")){
			if(pc.getLevel() < GameSetting.경험치지급단레벨){
				Exp e = ExpDatabase.find(pc.getLevel());
				pc.setExp( e.getBonus() );
			}
		}
	}
}
