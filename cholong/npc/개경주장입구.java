package cholong.npc;


import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class 개경주장입구 extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
	//	pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "Dograce1"));
		pc.toTeleport(33520, 32855, 4, true);
		ChattingController.toChatting(pc, "개 경주장으로 이동 되었습니다.", 20);
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){

		//if(action.equalsIgnoreCase("a")){ // 액션은 a로하세요..돈쓸거임?? 아녀 
			pc.toTeleport(33520, 32855, 4, true);
		}
	}

