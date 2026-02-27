package lineage.world.object.npc.guard;

import java.util.ArrayList;
import java.util.List;

import lineage.share.System;
import lineage.util.Util;
import lineage.bean.database.Item;
import lineage.bean.database.MonsterSpawnlist;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.bean.lineage.Quest;
import lineage.database.ItemDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectHeading;
import lineage.share.Lineage;
import lineage.world.controller.CharacterController;
import lineage.world.controller.QuestController;
import lineage.world.object.object;
import lineage.world.object.instance.GuardInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestInstance;
import lineage.world.object.instance.ShopInstance;

public class Sandstorm extends ShopInstance {

	private int temp_idx;	// 
	
	public Sandstorm(Npc npc){
		super(npc);
	}

	private boolean toSearchHuman(){
		boolean find = false;
		temp_idx += 1;
		for(object npc : getInsideList()) {
			if(temp_idx > 2) {	
				temp_idx = 1;
			npc.toSenderMe(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, 1241));

				temp_idx = 2;
				npc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, 1242));
			   find = true;
		   }
		}
	return find;
    }
	


	@Override
	public void toAi(long time){
		if(toSearchHuman());
		super.toAi(time);
	}

	
	@Override
	protected void toAiWalk(long time){
		// 스폰된 좌표와 다르다면 스폰된 좌표로 이동하도록 유도.
		if(x!=homeX || y!=homeY){
			toMoving(homeX, homeY, 0);
			return;
		}
	}
} 

