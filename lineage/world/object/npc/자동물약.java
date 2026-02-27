package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.potion.HealingPotion;

public class 자동물약 extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		showHtml(pc);
	}
	
	public void showHtml(PcInstance pc){
		if (pc.getInventory() != null) {
			if (pc.autoPotionIdx == null)
				pc.autoPotionIdx = new String[20];

			checkPotion(pc);
			
			List<String> autoPotion = new ArrayList<String>();
			autoPotion.clear();

			autoPotion.add(pc.isAutoPotion ? "실행" : "종료");
			autoPotion.add(pc.isAutoPotion2 ? "실행" : "종료");
			autoPotion.add(pc.autoPotionPercent < 1 ? "없음" : String.format("%d%%", pc.autoPotionPercent));
			autoPotion.add(pc.autoPotionName == null || pc.autoPotionName.length() < 2 ? "없음" : pc.autoPotionName);

			// 인벤토리에서 물약종류를 선택.
			int idx = 0;
			for (ItemInstance potion : pc.getInventory().getList()) {
				if (potion != null && potion.getItem() != null && potion instanceof HealingPotion) {
					autoPotion.add(String.format("%s (%s)", potion.getItem().getName(), Util.changePrice(potion.getCount())));
					pc.autoPotionIdx[idx] = potion.getItem().getName();
					idx++;
				}
			}

			for (int i = 0; i < pc.autoPotionIdx.length; i++)
				autoPotion.add(" ");
			
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autopotion", null, autoPotion));
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if (pc.getInventory() != null) {
			if (action.equalsIgnoreCase("pon")) {
				pc.isAutoPotion = true;
				showHtml(pc);
			}
			
			if (action.equalsIgnoreCase("poff")) {
				pc.isAutoPotion = false;
				showHtml(pc);
			}
			
			if (action.equalsIgnoreCase("pon2")) {
				pc.isAutoPotion2 = true;
				showHtml(pc);
			}
			
			if (action.equalsIgnoreCase("poff2")) {
				pc.isAutoPotion2 = false;
				showHtml(pc);
			}
			
			if (action.contains("percent-")) {			
				try {
					int percent = Integer.valueOf(action.replace("percent-", "").trim());
					
					if (percent < 1 || percent > 99)
						percent = 50;
					
					pc.autoPotionPercent = percent;
					showHtml(pc);
				} catch (Exception e) {
					ChattingController.toChatting(pc, "[자동 물약] 체력 설정이 잘못되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			}
			
			if (action.contains("potion-")) {
				try {
					int idx = Integer.valueOf(action.replace("potion-", "").trim());
					pc.autoPotionName = pc.autoPotionIdx[idx];
					showHtml(pc);
				} catch (Exception e) {
					ChattingController.toChatting(pc, "[자동 물약] 물약 설정이 잘못되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			}
		}
	}
	
	public void checkPotion(PcInstance pc) {
		// 인벤토리에서 설정한 물약 찾기.
		boolean isPotion = false;
		for (ItemInstance potion : pc.getInventory().getList()) {
			if (potion != null && potion.getItem() != null && potion instanceof HealingPotion && potion.getItem().getName().equalsIgnoreCase(pc.autoPotionName)) {
				isPotion = true;
				break;
			}
		}
		
		// 설정한 물약이 인벤토리에 존재하지 않으면 설정 초기화.
		if (!isPotion)
			pc.autoPotionName = null;
	}
}
