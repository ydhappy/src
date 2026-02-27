package lineage.world.object.item.scroll.newscroll;

import lineage.gui.GuiMain;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryBress;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Common;
import lineage.share.Lineage;
import lineage.share.Log;
import lineage.share.System;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.PcInstance;

public class ScrollChangeBress extends ItemInstance {
	public static synchronized ItemInstance clone(ItemInstance paramItemInstance) {
		if (paramItemInstance == null)
			paramItemInstance = new ScrollChangeBress();
		return paramItemInstance;
	}

	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null) {
			ItemInstance item = cha.getInventory().value(cbp.readD());
			if (getItem().getName().indexOf("축복") > -1) {
				if ((item != null) && (item instanceof ItemWeaponInstance || item instanceof ItemArmorInstance)
						&& (item.getBress() != 0 && item.getBress() != -128)) {
					if ((cha instanceof PcInstance))
						if(item.getItem().getName().equalsIgnoreCase("화살") || item.getItem().getName().equalsIgnoreCase("은 화살")
								|| item.getItem().getName().equalsIgnoreCase("미스릴 화살") || item.getItem().getName().equalsIgnoreCase("오리하루콘 화살")) {
							ChattingController.toChatting(cha, "축복 부여 사용이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
						if (getItem().getName().indexOf("100") > -1) {
							item.setBress(0);
							cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 15999), true);
							cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
							cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1637, item.toString(), "$7958", "$247"));
						} else if (getItem().getName().indexOf("5") > -1) {
							if (Util.random(0, 99) < 5) {
								item.setBress(0);
								cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 15353), true);
								cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
								cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1637, item.toString(), "$7958", "$247"));
							} else {
								//cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1636, item.toString(), "$7958", "$247"));
								cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
							}
						}
						else if (getItem().getName().indexOf("30") > -1) {
							if (Util.random(0, 99) < 30) {
								item.setBress(0);
								cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 15999), true);
								cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
								cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1637, item.toString(), "$7958", "$247"));
							} else {
								//cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1636, item.toString(), "$7958", "$247"));
								cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
							}
						}else {
							if (Util.random(0, 99) < 1) {
								item.setBress(0);
								cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 15999), true);
								cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
								cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1637, item.toString(), "$7958", "$247"));
							} else {
								//cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1636, item.toString(), "$7958", "$247"));
								cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
							}
						}

					cha.getInventory().count(this, getCount() - 1, true);
				} else {
					ChattingController.toChatting(cha, "축복 부여 사용이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
				}
			long time = System.currentTimeMillis();
			String timeString = Util.getLocaleString(time, true);

			// log
			if (item.getBress() == 0) {

				if (!Common.system_config_console && item.getBress() == 0) {
					String log = String.format("[%s] [축부여 성공]\t [캐릭터: %s]\t [아이템: %s]\t [주문서: %s]\t [bless: %d]",
							timeString, cha.getName(), Util.getItemNameToString(item, item.getCount()),
							Util.getItemNameToString(this, getCount()), item.getBress());

					GuiMain.display.asyncExec(new Runnable() {
						public void run() {
							GuiMain.getViewComposite().getEnchantComposite().toLog(log);
						}
					});
				}
			} else {

				if (!Common.system_config_console) {
					String log = String.format("[%s] [축부여 실패]\t [캐릭터: %s]\t [아이템: %s]\t [주문서: %s]", timeString,
							cha.getName(), Util.getItemNameToString(item, item.getCount()),
							Util.getItemNameToString(this, getCount()));

					GuiMain.display.asyncExec(new Runnable() {
						public void run() {
							GuiMain.getViewComposite().getEnchantComposite().toLog(log);
						}
					});
				}
			}

		}
	}
}
