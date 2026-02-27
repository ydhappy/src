package lineage.network.packet.client;

import lineage.bean.lineage.StatDice;
import lineage.network.LineageClient;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_StatDice;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.util.Util;

public class C_StatDice extends ClientBasePacket {

	static private int[] addStat = { Lineage.royal_stat_dice, Lineage.knight_stat_dice, Lineage.elf_stat_dice,
			Lineage.wizard_stat_dice };

	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length) {
		if (bp == null)
			bp = new C_StatDice(data, length);
		else
			((C_StatDice) bp).clone(data, length);
		return bp;
	}

	public C_StatDice(byte[] data, int length) {
		clone(data, length);
	}

	@Override
	public BasePacket init(LineageClient c) {
		//
		StatDice sd = c.getStatDice();
		sd.setType(readC());
		//
		if (PluginController.init(C_StatDice.class, "init", this, c) != null)
			return this;
		//
		switch (sd.getType()) {
		case 0x00:
			sd.setStr(Lineage.royal_stat_str);
			sd.setCon(Lineage.royal_stat_con);
			sd.setDex(Lineage.royal_stat_dex);
			sd.setWis(Lineage.royal_stat_wis);
			sd.setCha(Lineage.royal_stat_cha);
			sd.setInt(Lineage.royal_stat_int);
			break;
		case 0x01:
			sd.setStr(Lineage.knight_stat_str);
			sd.setCon(Lineage.knight_stat_con);
			sd.setDex(Lineage.knight_stat_dex);
			sd.setWis(Lineage.knight_stat_wis);
			sd.setCha(Lineage.knight_stat_cha);
			sd.setInt(Lineage.knight_stat_int);
			break;
		case 0x02:
			sd.setStr(Lineage.elf_stat_str);
			sd.setCon(Lineage.elf_stat_con);
			sd.setDex(Lineage.elf_stat_dex);
			sd.setWis(Lineage.elf_stat_wis);
			sd.setCha(Lineage.elf_stat_cha);
			sd.setInt(Lineage.elf_stat_int);
			break;
		case 0x03:
			sd.setStr(Lineage.wizard_stat_str);
			sd.setCon(Lineage.wizard_stat_con);
			sd.setDex(Lineage.wizard_stat_dex);
			sd.setWis(Lineage.wizard_stat_wis);
			sd.setCha(Lineage.wizard_stat_cha);
			sd.setInt(Lineage.wizard_stat_int);
			break;
		case 0x04:
			sd.setStr(Lineage.darkelf_stat_str);
			sd.setCon(Lineage.darkelf_stat_con);
			sd.setDex(Lineage.darkelf_stat_dex);
			sd.setWis(Lineage.darkelf_stat_wis);
			sd.setCha(Lineage.darkelf_stat_cha);
			sd.setInt(Lineage.darkelf_stat_int);
			break;
		}

		if (Lineage.stat_base) {
			// 미리 정의된 스탯으로 처리.
			switch (sd.getType()) {
			case 0x00:
				switch (Util.random(0, 2)) {
				case 0:
					sd.setStr(20);
					sd.setCon(14);
					sd.setDex(12);
					sd.setWis(9);
					sd.setCha(12);
					sd.setInt(8);
					break;
				case 1:
					sd.setStr(16);
					sd.setCon(18);
					sd.setDex(12);
					sd.setWis(9);
					sd.setCha(12);
					sd.setInt(8);
					break;
				case 2:
					sd.setStr(17);
					sd.setCon(17);
					sd.setDex(12);
					sd.setWis(9);
					sd.setCha(12);
					sd.setInt(8);
					break;
				}
				break;
			case 0x01:
				switch (Util.random(0, 2)) {
				case 0:
					sd.setStr(20);
					sd.setCon(14);
					sd.setDex(12);
					sd.setWis(9);
					sd.setCha(12);
					sd.setInt(8);
					break;
				case 1:
					sd.setStr(16);
					sd.setCon(18);
					sd.setDex(12);
					sd.setWis(9);
					sd.setCha(12);
					sd.setInt(8);
					break;
				case 2:
					sd.setStr(17);
					sd.setCon(17);
					sd.setDex(12);
					sd.setWis(9);
					sd.setCha(12);
					sd.setInt(8);
					break;
				}
				break;
			case 0x02:
				switch (Util.random(0, 5)) {
				case 0:
					sd.setStr(12);
					sd.setCon(18);
					sd.setDex(12);
					sd.setWis(12);
					sd.setCha(9);
					sd.setInt(12);
					break;
				case 1:
					sd.setStr(12);
					sd.setCon(12);
					sd.setDex(18);
					sd.setWis(12);
					sd.setCha(9);
					sd.setInt(12);
					break;
				case 2:
					sd.setStr(18);
					sd.setCon(12);
					sd.setDex(12);
					sd.setWis(12);
					sd.setCha(9);
					sd.setInt(12);
					break;
				case 3:
					sd.setStr(11);
					sd.setCon(18);
					sd.setDex(13);
					sd.setWis(12);
					sd.setCha(9);
					sd.setInt(12);
					break;
				case 4:
					sd.setStr(11);
					sd.setCon(13);
					sd.setDex(12);
					sd.setWis(12);
					sd.setCha(9);
					sd.setInt(18);
					break;
				case 5:
					sd.setStr(11);
					sd.setCon(13);
					sd.setDex(18);
					sd.setWis(12);
					sd.setCha(9);
					sd.setInt(12);
					break;

				}
				break;
			case 0x03:
				switch (Util.random(0, 3)) {
				case 0:
					sd.setStr(8);
					sd.setCon(16);
					sd.setDex(7);
					sd.setWis(18);
					sd.setCha(8);
					sd.setInt(18);
					break;
				case 1:
					sd.setStr(8);
					sd.setCon(18);
					sd.setDex(7);
					sd.setWis(16);
					sd.setCha(8);
					sd.setInt(18);
					break;
				case 2:
					sd.setStr(8);
					sd.setCon(18);
					sd.setDex(7);
					sd.setWis(18);
					sd.setCha(8);
					sd.setInt(16);
					break;
				case 3:
					sd.setStr(8);
					sd.setCon(14);
					sd.setDex(7);
					sd.setWis(14);
					sd.setCha(18);
					sd.setInt(14);
					break;
//						case 4:
//							sd.setStr(8);sd.setCon(18);sd.setDex(7);
//							sd.setWis(12);sd.setCha(12);sd.setInt(18);
//							break;
				}
				break;
			case 0x04:
				switch (Util.random(0, 3)) {
				case 0:
					sd.setStr(18);
					sd.setCon(9);
					sd.setDex(18);
					sd.setWis(10);
					sd.setCha(9);
					sd.setInt(11);
					break;
				case 1:
					sd.setStr(18);
					sd.setCon(12);
					sd.setDex(15);
					sd.setWis(10);
					sd.setCha(9);
					sd.setInt(11);
					break;
				case 2:
					sd.setStr(12);
					sd.setCon(15);
					sd.setDex(18);
					sd.setWis(10);
					sd.setCha(9);
					sd.setInt(11);
					break;
				case 3:
					sd.setStr(12);
					sd.setCon(18);
					sd.setDex(15);
					sd.setWis(10);
					sd.setCha(9);
					sd.setInt(11);
					break;

				}
				break;
			}
		} else {
			// 랜덤 스탯 처리.
			for (int i = addStat[sd.getType()]; i > 0; --i) {
				switch (Util.random(0, 5)) {
				case 0:
					sd.setStr(sd.getStr() + 1);
					break;
				case 1:
					sd.setDex(sd.getDex() + 1);
					if (sd.getDex() > 18) {
						++i;
						sd.setDex(18);
					}
					break;
				case 2:
					sd.setCon(sd.getCon() + 1);
					if (sd.getCon() > 18) {
						++i;
						sd.setCon(18);
					}
					break;
				case 3:
					sd.setWis(sd.getWis() + 1);
					if (sd.getWis() > 18) {
						++i;
						sd.setWis(18);
					}
					break;
				case 4:
					sd.setInt(sd.getInt() + 1);
					if (sd.getInt() > 18) {
						++i;
						sd.setInt(18);
					}
					break;
				case 5:
					sd.setCha(sd.getCha() + 1);
					if (sd.getCha() > 18) {
						++i;
						sd.setCha(18);
					}
					break;
				}
			}
		}

		if (c.getPc() == null || c.getPc().isWorldDelete())
			c.toSender(S_StatDice.clone(BasePacketPooling.getPool(S_StatDice.class), sd));
		return this;
	}
}
