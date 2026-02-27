package lineage.network.packet.server;

import lineage.database.QuestDatabase.PcLevelQuest;
import lineage.database.QuestDatabase.PcRepeatQuest;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.world.object.instance.PcInstance;

public class S_QuestView extends ServerBasePacket {

	static public BasePacket clone(BasePacket bp, PcInstance pc, PcLevelQuest lvQuest){
		if(bp == null)
			bp = new S_QuestView(pc, lvQuest);
		else
			((S_QuestView)bp).toClone(pc, lvQuest);
		return bp;
	}
	
	static public BasePacket clone(BasePacket bp, PcInstance pc, PcRepeatQuest repQuest){
		if(bp == null)
			bp = new S_QuestView(pc, repQuest);
		else
			((S_QuestView)bp).toClone(pc, repQuest);
		return bp;
	}
	
	public S_QuestView(PcInstance pc, PcLevelQuest lvQuest){
		toClone(pc, lvQuest);
	}
	
	public void toClone(PcInstance pc, PcLevelQuest lvQuest){
		clear();
		writeC(Opcodes.S_OPCODE_BOARDREAD);
		writeD(1);			// 순번
		writeS("Level Quest");		// 작성자
		writeS(lvQuest.getTitle());		// 제목
		writeS(" ");	// 날짜
		StringBuilder sb = new StringBuilder();
		sb.append("----------------------------").append("\n\n");
		sb.append("레    벨 : ").append(lvQuest.getQuestLevel()).append("\n");
		sb.append("파    티 : ").append(lvQuest.isParty() ? "파티 없어야됨" : "파티 있어도됨").append("\n");
		sb.append("혈    맹 : ").append(lvQuest.isClan() ? "혈맹 있어야됨" : "혈맹 없어도됨").append("\n");
		sb.append("\n");
		sb.append("보    상 : ").append(lvQuest.getPresentItemName()).append(" (").append(lvQuest.getPresentItemCount()).append(")\n");
		sb.append("내    용 : \n").append(lvQuest.getMessage()).append("\n\n");
		if (lvQuest.getType().equalsIgnoreCase("item")) 
			sb.append("획득갯수 : \n");
		else if (lvQuest.getType().equalsIgnoreCase("monster"))
			sb.append("처리횟수 : \n");
		for (int i = 0; i < lvQuest.getCompleteCount().length; i++) {
			sb.append(lvQuest.getQuestTarget()[i]);
			if (lvQuest.getType().equalsIgnoreCase("item")) 
				sb.append(" 증표");
			sb.append(" : ");
			sb.append("( ").append(lvQuest.getCompleteCount()[i]).append(" / ").append(lvQuest.getQuestCount()[i]).append(" )");
			sb.append("\n");
		}
		sb.append("\n");
		sb.append("----------------------------");
		writeS(sb.toString());		// 내용
		sb.delete(0, sb.length());
		sb = null;
	}
	
	public S_QuestView(PcInstance pc, PcRepeatQuest repQuest){
		toClone(pc, repQuest);
	}
	
	public void toClone(PcInstance pc, PcRepeatQuest repQuest){
		clear();
		writeC(Opcodes.S_OPCODE_BOARDREAD);
		writeD(1);			// 순번
		writeS("Repeat Quest");		// 작성자
		writeS(repQuest.getTitle());		// 제목
		writeS(" ");	// 날짜
		StringBuilder sb = new StringBuilder();
		sb.append("----------------------------").append("\n\n");
		sb.append("레    벨 : ").append(repQuest.getQuestLevel()).append("\n");
		sb.append("파    티 : ").append(repQuest.isParty() ? "파티 없어야됨" : "파티 있어도됨").append("\n");
		sb.append("혈    맹 : ").append(repQuest.isClan() ? "혈맹 있어야됨" : "혈맹 없어도됨").append("\n");
		sb.append("\n");
		sb.append("보    상 : ").append(repQuest.getPresentItemName()).append(" (").append(repQuest.getPresentItemCount()).append(")\n");
		sb.append("내    용 : \n").append(repQuest.getMessage()).append("\n\n");
		if (repQuest.getType().equalsIgnoreCase("item")) 
			sb.append("획득갯수 : \n");
		else if (repQuest.getType().equalsIgnoreCase("monster"))
			sb.append("처리횟수 : \n");
		for (int i = 0; i < repQuest.getCompleteCount().length; i++) {
			sb.append(repQuest.getQuestTarget()[i]);
			if (repQuest.getType().equalsIgnoreCase("item")) 
				sb.append(" 증표");
			sb.append(" : ");
			sb.append("( ").append(repQuest.getCompleteCount()[i]).append(" / ").append(repQuest.getQuestCount()[i]).append(" )");
			sb.append("\n");
		}
		sb.append("\n");
		sb.append("----------------------------");
		writeS(sb.toString());		// 내용
		sb.delete(0, sb.length());
		sb = null;
	}

}
