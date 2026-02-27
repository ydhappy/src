package lineage.world.object.npc.quest;

import lineage.bean.database.Exp;
import lineage.bean.database.Npc;
import lineage.bean.database.Skill;
import lineage.bean.lineage.Quest;
import lineage.database.ExpDatabase;
import lineage.database.ItemDatabase;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_SoundEffect;
import lineage.share.Lineage;
import lineage.world.controller.CraftController;
import lineage.world.controller.QuestController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestInstance;
import lineage.world.object.magic.EnchantWeapon;
import lineage.world.object.magic.Haste;

public class AdminNovice extends QuestInstance {
	
	public AdminNovice(Npc npc){
		super(npc);
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 27828)); 
		// 퀘스트 추출.
		Quest q = QuestController.find(pc, Lineage.QUEST_NOVICE);
		if(q == null)
			q = QuestController.newQuest(pc, this, Lineage.QUEST_NOVICE);
		
		// 버프
		Haste.onBuff(pc, SkillDatabase.find(7, 5));
		Skill s = SkillDatabase.find(2, 3);
		EnchantWeapon.onBuff(pc, pc.getInventory().getSlot(8), s, s.getBuffDuration());
		
		// 구분 처리.
		if (pc.getLevel() < 5) {
			if (pc.getLevel() == 2) {
				// 경험치 처리.
				Exp e = ExpDatabase.find(pc.getLevel());
				pc.setExp(e.getBonus());
			}
			// 퀘스트 변수 변경.
			q.setQuestStep(2);
			// 안내창
			pc.toSender(new S_Html( this, "admin2"));
			return;
		}
		if (pc.getLevel() >= 5 && q.getQuestStep() == 2) {
			// 퀘스트 변수 변경.
			q.setQuestStep(3);
			// 안내창
			pc.toSender(new S_Html( this, "admin3"));
			return;
		}

		pc.toSender(new S_Html( this, "admin1"));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		// 퀘스트 추출.
		Quest q = QuestController.find(pc, Lineage.QUEST_NOVICE);
		if (q == null)
			return;
		if(action.equalsIgnoreCase("d")){
			// 서쪽 계곡
			pc.toPotal(32547, 32874, 69);
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
		}else if(action.equalsIgnoreCase("e")){
			// 동쪽 계곡
			pc.toPotal(32783, 32896, 69);
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
		}else if(action.equalsIgnoreCase("f")){
			// 남쪽 협곡
			pc.toPotal(32588, 32995, 69);
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
		}else if(action.equalsIgnoreCase("c")){
			// 남쪽 계곡
			pc.toPotal(32664, 32993, 69);
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
		}else if(action.equalsIgnoreCase("b")){
			// 북쪽 계곡
			pc.toPotal(32658, 32735, 69);
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
		}
		
		if (action.equalsIgnoreCase("A") && q.getQuestStep() == 3) {
			if (pc.getLevel() == 5) {
				// 경험치 처리.
				Exp e = ExpDatabase.find(pc.getLevel());
				pc.setExp(e.getBonus());
			}
			// 퀘스트 변수 변경
			q.setQuestStep(4);
			// 장비 지급
			CraftController.toCraft(this, pc, ItemDatabase.find("수련자의 가죽 투구"), 1, true, 0, 0, 1);
			CraftController.toCraft(this, pc, ItemDatabase.find("수련자의 가죽 갑옷"), 1, true, 0, 0, 1);
			CraftController.toCraft(this, pc, ItemDatabase.find("수련자의 가죽 장갑"), 1, true, 0, 0, 1);
			CraftController.toCraft(this, pc, ItemDatabase.find("수련자의 가죽 샌달"), 1, true, 0, 0, 1);
			CraftController.toCraft(this, pc, ItemDatabase.find("수련자의 가죽 방패"), 1, true, 0, 0, 1);
			CraftController.toCraft(this, pc, ItemDatabase.find("수련자의 티셔츠"), 1, true, 0, 0, 1);
			CraftController.toCraft(this, pc, ItemDatabase.find("수련자의 속도향상 물약"), 5, true, 0, 0, 1);
			switch(pc.getClassType()){
			case 0x00:
				CraftController.toCraft(this, pc, ItemDatabase.find("수련자의 악마의 피"), 1, true, 0, 0, 1);
				break;
			case 0x01:
				CraftController.toCraft(this, pc, ItemDatabase.find("수련자의 용기의 물약"), 1, true, 0, 0, 1);
				break;
			case 0x02:
				CraftController.toCraft(this, pc, ItemDatabase.find("수련자의 엘븐 와퍼"), 1, true, 0, 0, 1);
				break;
			case 0x03:
				CraftController.toCraft(this, pc, ItemDatabase.find("수련자의 지혜의 물약"), 1, true, 0, 0, 1);
				CraftController.toCraft(this, pc, ItemDatabase.find("수련자의 파란 물약"), 1, true, 0, 0, 1);
				break;
			}
			// 안내창
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "admin1"));
		}
	}

}
