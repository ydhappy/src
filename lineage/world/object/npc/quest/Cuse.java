package lineage.world.object.npc.quest;

import java.util.ArrayList;
import java.util.List;

import lineage.share.System;
import lineage.util.Util;
import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.bean.lineage.Quest;
import lineage.database.ItemDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectAction;
import lineage.share.Lineage;
import lineage.world.controller.QuestController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestInstance;

public class Cuse extends QuestInstance {
	
	private long actionTime; // 액션딜레이

	public Cuse(Npc npc){
		super(npc);
	}
	

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		if(pc.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT){
			if(pc.getLevel() < 15){
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "cuse1"));
			}else{
				Quest q = QuestController.find(pc, Lineage.QUEST_KNIGHT_LV15);
				if(q == null)
					q = QuestController.newQuest(pc, this, Lineage.QUEST_KNIGHT_LV15);
				switch(q.getQuestStep()){
					case 1:
						pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "cuse4"));
						break;
					default:
						pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "cuse1"));
						break;
				}
			}
		}else{
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "cuse1"));
		}
	}

	@Override
	public void toAi(long time) {
		if (actionTime + (1 * (Util.random(1, 6))) < System.currentTimeMillis()) {
				while (true) {
					int tempGfxMode = Lineage.NPCAction[Util.random(1, Lineage.NPCAction.length - 3)];
					if (SpriteFrameDatabase.findGfxMode(getGfx(), tempGfxMode)) {
						actionTime = (int) System.currentTimeMillis();
						toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), this,
								tempGfxMode), true);
						break;
					}
				}
			}
		}	
}
