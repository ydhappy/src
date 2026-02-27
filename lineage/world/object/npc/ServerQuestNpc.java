package lineage.world.object.npc;

import java.sql.Timestamp;
import java.util.Calendar;

import lineage.bean.database.Exp;
import lineage.bean.database.Item;
import lineage.database.CharactersDatabase;
import lineage.database.ExpDatabase;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ServerQuestNpc extends object {
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "serverquest"));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		
		if(action.equalsIgnoreCase("giran1")){ // 특화퀘스트 1 시작.
			int delayEffect = 86400; // 86400 = 24시간 (60초*60 = 3600(1시간) // 3600*24=86400  성환퀘스트
			Timestamp lastUsed = pc.getGiranQuestTime();
			if (lastUsed != null) {
				Calendar cal = Calendar.getInstance();
				if ((cal.getTimeInMillis() - lastUsed.getTime()) / 1000 <= delayEffect) {
					ChattingController.toChatting(pc, ((delayEffect - (cal.getTimeInMillis() - lastUsed.getTime()) / 1000) / 60 )+ "분 " +((delayEffect - (cal.getTimeInMillis() - lastUsed.getTime()) / 1000) % 60)+ "초 후에 다시 진행할 수 있습니다. ", 20);
					return;
				}
			}
		}
	}
	
	public void 기본보상(PcInstance pc){ // 드래곤다이아몬드2개 , 마법인형주머니1개
		Item ii = ItemDatabase.find("일일퀘스트보상상자");
		ItemInstance 보상2 = ItemDatabase.newInstance(ii);
		pc.getInventory().append(보상2, 1);
		pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 143, getName(), 보상2.getName()+"(1)"));
	}
	
	private void 레벨52기준보상경험치(PcInstance pc, int type) {
		Exp e = ExpDatabase.find(52);
		Exp ee = ExpDatabase.find(51);
		double needExp = e.getBonus() - ee.getBonus();
		//System.out.println("필요경험치 : " + needExp);
		double exppenalty = ExpDatabase.getPenaltyRate(pc.getLevel());
		//System.out.println("패널티 : " + exppenalty);
		double exp = 0;
		if (type == 1) {
			exp = (needExp * 0.02D * exppenalty);
		} else if (type == 2) {
			exp = (needExp * 0.05D * exppenalty);
		} else if (type == 3) {
			exp = (needExp * 0.08D * exppenalty);
		} else if (type == 4) {
			exp = (needExp * 0.10D * exppenalty);
		} else if (type == 5) {
			exp = (needExp * 0.12D * exppenalty);
		} else if (type == 6) {
			exp = (needExp * 0.15D * exppenalty);
		} else if (type == 7) {
			exp = (needExp * 0.18D * exppenalty);
		} else if (type == 8) {
			exp = (needExp * 0.20D * exppenalty);
		} else if (type == 9) {
			exp = (needExp * 0.22D * exppenalty);
		} else if (type == 10) {
			exp = (needExp * 0.25D * exppenalty);
		} else {
			ChattingController.toChatting(pc, "잘못된 요구입니다.", 20);
		}
		//System.out.println("경험치 : " + exp);
		pc.toExp(pc,exp);
		pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 2245), true);
	}
}
