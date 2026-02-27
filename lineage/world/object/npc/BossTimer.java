package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.BossSpawn;
import lineage.database.MonsterBossSpawnlistDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BossController;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class BossTimer extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		showHtml(pc);
	}
	
	public void showHtml(PcInstance pc){
	
		List<String> bossList = new ArrayList<String>();
		bossList.clear();
		
		for (BossSpawn bossSpawn : MonsterBossSpawnlistDatabase.getSpawnList()) {
			bossList.add(String.format("[%s]", bossSpawn.getMonster()));
			bossList.add(String.format("%s", bossSpawn.getSpawnTime()));
			bossList.add(String.format("요일: %s", bossSpawn.getSpawnDay().trim()));
			bossList.add(" ");
		}
		
		for (int i = 0; i < 150; i++)
			bossList.add(" ");
		
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "bossList", null, bossList));
		
		if (BossController.getBossList().size() < 1) {
			ChattingController.toChatting(pc, "\\fR생존한 보스가 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		ChattingController.toChatting(pc, "\\fRㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ", Lineage.CHATTING_MODE_MESSAGE);

		for (MonsterInstance boss : BossController.getBossList())
			ChattingController.toChatting(pc, String.format("\\fY%s %s", Util.getMapName(boss), boss.getMonster().getName()), Lineage.CHATTING_MODE_MESSAGE);

		ChattingController.toChatting(pc, "\\fRㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		
		
		
	
		showHtml(pc);
	
	}
}