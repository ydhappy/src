package lineage.world.object.npc;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.controller.CharacterController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.ShopInstance;

//엔줄편의점 NPC
public class Manless5 extends ShopInstance {
	
	public Manless5(Npc npc){
		super(npc);
		
		// 관리목록에 등록. toTimer가 호출되도록 하기 위해.
		CharacterController.toWorldJoin(this);
		
		// 20초 단위로 멘트 표현.
		ment_show_sec = 6;
		// 멘트
		list_ment.add("");
	}


	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "Manless4"));
	}
}