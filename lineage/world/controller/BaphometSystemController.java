package lineage.world.controller;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterSpMr;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.object.instance.PcInstance;

public final class BaphometSystemController {

	public static void setBaphomet(PcInstance pc) {
		// 라우풀 일때
		if (pc.getLawful() >= 66036) {
			// 500 ~ 19999
			if (pc.getLawful() >= 66036 && pc.getLawful() <= 85535
					&& (!pc.isBaphomet() || pc.getBaphometLevel() != 1)) {
				if (pc.isBaphomet())
					removeBaphomet(pc);
				pc.setBaphomet(true);
				pc.setBaphometLevel(1);
				appendBaphomet(pc);
				pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 10881), true);
				ChattingController.toChatting(pc, "\\fU로우풀 1단계: AC-1", Lineage.CHATTING_MODE_MESSAGE);
			}
			// 20000 ~ 29999
			if (pc.getLawful() >= 85536 && pc.getLawful() <= 95535
					&& (!pc.isBaphomet() || pc.getBaphometLevel() != 2)) {
				if (pc.isBaphomet())
					removeBaphomet(pc);
				pc.setBaphomet(true);
				pc.setBaphometLevel(2);
				appendBaphomet(pc);
				pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 10881), true);
				ChattingController.toChatting(pc, "\\fU로우풀 2단계: AC-2", Lineage.CHATTING_MODE_MESSAGE);
			}
			// 30000 ~ 32767
			if (pc.getLawful() >= 95536 && (!pc.isBaphomet() || pc.getBaphometLevel() != 3)) {
				if (pc.isBaphomet())
					removeBaphomet(pc);
				pc.setBaphomet(true);
				pc.setBaphometLevel(3);
				appendBaphomet(pc);
				pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 10881), true);
				ChattingController.toChatting(pc, "\\fU로우풀 3단계: AC-3", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
		// 카오틱 일때
		if (pc.getLawful() <= 65535) {
			// -1 ~ -19999
			if (pc.getLawful() >= 45537 && pc.getLawful() <= 65535
					&& (!pc.isBaphomet() || pc.getBaphometLevel() != -1)) {
				if (pc.isBaphomet())
					removeBaphomet(pc);
				pc.setBaphomet(true);
				pc.setBaphometLevel(-1);
				appendBaphomet(pc);
				pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 10879), true);
				ChattingController.toChatting(pc, "\\fY카오틱 1단계: AC+1, HP 물약 회복량 2% 감소", Lineage.CHATTING_MODE_MESSAGE);
			}
			// -20000 ~ -29999
			if (pc.getLawful() >= 35537 && pc.getLawful() <= 45536
					&& (!pc.isBaphomet() || pc.getBaphometLevel() != -2)) {
				if (pc.isBaphomet())
					removeBaphomet(pc);
				pc.setBaphomet(true);
				pc.setBaphometLevel(-2);
				appendBaphomet(pc);
				pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 10879), true);
				ChattingController.toChatting(pc, "\\fY카오틱 2단계: AC+2, HP 물약 회복량 4% 감소", Lineage.CHATTING_MODE_MESSAGE);
			}
			// -30000 ~ -32768
			if (pc.getLawful() <= 35536 && (!pc.isBaphomet() || pc.getBaphometLevel() != -3)) {
				if (pc.isBaphomet())
					removeBaphomet(pc);
				pc.setBaphomet(true);
				pc.setBaphometLevel(-3);
				appendBaphomet(pc);
				pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 10879), true);
				ChattingController.toChatting(pc, "\\fY카오틱 3단계: AC+3, HP 물약 회복량 6% 감소", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
		if (pc.getLawful() >= 65536 && pc.getLawful() <= 66035 && pc.isBaphomet()) {
			pc.setBaphomet(false);
			removeBaphomet(pc);
			ChattingController.toChatting(pc, "뉴트럴 단계: 성향치 혜택 및 패널티 종료", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	// 효과 제거
	public static void removeBaphomet(PcInstance pc) {
		// -1 ~ -3 카오틱
		// 1 ~ 3 라우풀
		switch (pc.getBaphometLevel()) {
			case -1:
				pc.setDynamicHealing(pc.getDynamicHealing() +2);
				pc.setDynamicHealingPercent(pc.getDynamicHealingPercent() +2);
				pc.setDynamicAc(pc.getDynamicAc() +1);
				break;
			case -2:
				pc.setDynamicHealing(pc.getDynamicHealing() +4);
				pc.setDynamicHealingPercent(pc.getDynamicHealingPercent() +4);
				pc.setDynamicAc(pc.getDynamicAc() +2);
				break;
			case -3:
				pc.setDynamicHealing(pc.getDynamicHealing() +6);
				pc.setDynamicHealingPercent(pc.getDynamicHealingPercent() +6);
				pc.setDynamicAc(pc.getDynamicAc() +3);
				break;
			case 1:
				pc.setDynamicAc(pc.getDynamicAc() - 1);
				break;
			case 2:
				pc.setDynamicAc(pc.getDynamicAc() - 2);
				break;
			case 3:
				pc.setDynamicAc(pc.getDynamicAc() - 3);
				break;
		}

		pc.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), pc));
		pc.toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), pc));
	}

	// 효과 부여
	public static void appendBaphomet(PcInstance pc) {
		// -1 ~ -3 카오틱
		// 1 ~ 3 라우풀
		switch (pc.getBaphometLevel()) {
			case -1:
				pc.setDynamicHealing(pc.getDynamicHealing() -2);
				pc.setDynamicHealingPercent(pc.getDynamicHealingPercent() -2);
				pc.setDynamicAc(pc.getDynamicAc() -1);
				break;
			case -2:
				pc.setDynamicHealing(pc.getDynamicHealing() -4);
				pc.setDynamicHealingPercent(pc.getDynamicHealingPercent() -4);
				pc.setDynamicAc(pc.getDynamicAc() -2);
				break;
			case -3:
				pc.setDynamicHealing(pc.getDynamicHealing() -6);
				pc.setDynamicHealingPercent(pc.getDynamicHealingPercent() -6);
				pc.setDynamicAc(pc.getDynamicAc() -3);
				break;
			case 1:
				pc.setDynamicAc(pc.getDynamicAc() + 1);
				break;
			case 2:
				pc.setDynamicAc(pc.getDynamicAc() + 2);
				break;
			case 3:
				pc.setDynamicAc(pc.getDynamicAc() + 3);
				break;
		}

		pc.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), pc));
		pc.toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), pc));
	}
}
