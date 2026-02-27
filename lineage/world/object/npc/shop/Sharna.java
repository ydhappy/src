package lineage.world.object.npc.shop;

import lineage.bean.database.Npc;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_SoundEffect;
import lineage.world.controller.CraftController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.ShopInstance;

public class Sharna extends ShopInstance {

	public Sharna(Npc npc) {
		super(npc);
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 27813)); 
		if(pc.getLevel() >= 40) {
			pc.toSender(new S_Html( this, "sharna1"));
		} else {
			pc.toSender(new S_Html( this, "sharna4"));
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		switch (action.charAt(0)) {
		   case '0': // 3000아데나
			// 최소 레벨 체크.
			if(pc.getLevel() < 40) {
				pc.toSender(new S_Html( this, "sharna5"));
				return;
			}
			// 아덴 확인 후 지급처리.
			if(pc.getInventory().isAden(3000, true)) {
					if(pc.getLevel() < 45)
					// 샤르나의 변신 주문서 (레벨 45)
						CraftController.toCraft(this, pc, ItemDatabase.find(5879), 1, true);
				else if(pc.getLevel() < 50)
					// 샤르나의 변신 주문서 (레벨 50)
					CraftController.toCraft(this, pc, ItemDatabase.find(5880), 1, true);
				else if(pc.getLevel() < 99)
					// 샤르나의 변신 주문서 (레벨 52)
					CraftController.toCraft(this, pc, ItemDatabase.find(5881), 1, true);
				pc.toSender(new S_Html( this, "sharna3"));
				break;
			} else {
				pc.toSender(new S_Html( this, "sharna5"));
			}
			break;
		   case '1':
			// 최소 레벨 체크.
				if(pc.getLevel() < 40) {
					pc.toSender(new S_Html( this, "sharna5"));
					return;
				}
				// 아덴 확인 후 지급처리.
				if(pc.getInventory().isAden(30000, true)) {
					if(pc.getLevel() < 45)
					// 샤르나의 변신 주문서 (레벨 45)
						CraftController.toCraft(this, pc, ItemDatabase.find(5879), 10, true);
				else if(pc.getLevel() < 50)
					// 샤르나의 변신 주문서 (레벨 50)
					CraftController.toCraft(this, pc, ItemDatabase.find(5880), 10, true);
				else if(pc.getLevel() < 99)
					// 샤르나의 변신 주문서 (레벨 52)
					CraftController.toCraft(this, pc, ItemDatabase.find(5881), 10, true);
					pc.toSender(new S_Html( this, "sharna3"));
					break;
				} else {
					pc.toSender(new S_Html( this, "sharna5"));
				}
				break;
		   case '2':
			// 최소 레벨 체크.
				if(pc.getLevel() < 40) {
					pc.toSender(new S_Html( this, "sharna5"));
					return;
				}
				// 아덴 확인 후 지급처리.
				if(pc.getInventory().isAden(300000, true)) {
					if(pc.getLevel() < 45)
					// 샤르나의 변신 주문서 (레벨 45)
						CraftController.toCraft(this, pc, ItemDatabase.find(5879), 100, true);
				else if(pc.getLevel() < 50)
					// 샤르나의 변신 주문서 (레벨 50)
					CraftController.toCraft(this, pc, ItemDatabase.find(5880), 100, true);
				else if(pc.getLevel() < 99)
					// 샤르나의 변신 주문서 (레벨 52)
					CraftController.toCraft(this, pc, ItemDatabase.find(5881), 100, true);
					pc.toSender(new S_Html( this, "sharna3"));
					break;
				} else {
					pc.toSender(new S_Html( this, "sharna5"));
				}
				break;
		}
	}
}


