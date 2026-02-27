package lineage.world.object.item.sp;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.ServerBasePacket;
import lineage.network.packet.client.C_StatDice;
import lineage.network.packet.server.S_CharacterStat;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class StatClear extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new StatClear();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
//		if (cha.getLevel() < 51){
//			ChattingController.toChatting(cha, "회상의 촛불은 레벨 51 이상만 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
//			return;
//		}
		if(cha.getMap()!=4){
			ChattingController.toChatting(cha, "마을에서만 정상 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		// 스탯 초기화.
		cha.setLvStr(0);
		cha.setLvDex(0);
		cha.setLvCon(0);
		cha.setLvWis(0);
		cha.setLvInt(0);
		cha.setLvCha(0);
		cha.setElixirReset(cha.getElixirStat());
		cha.setElixirStr(0);
		cha.setElixirDex(0);
		cha.setElixirCon(0);
		cha.setElixirWis(0);
		cha.setElixirInt(0);
		cha.setElixirCha(0);
		// 베이스스탯 재 설정.
		ServerBasePacket sbp = (ServerBasePacket)ServerBasePacket.clone(BasePacketPooling.getPool(ServerBasePacket.class), null);
		sbp.writeC(0);
		sbp.writeC(cha.getClassType());
		PcInstance pc = (PcInstance)cha;
		C_StatDice.clone(BasePacketPooling.getPool(C_StatDice.class), sbp.getByte(), sbp.getSize()).init(pc.getClient());
		cha.setStr(pc.getClient().getStatDice().getStr());
		cha.setDex(pc.getClient().getStatDice().getDex());
		cha.setCon(pc.getClient().getStatDice().getCon());
		cha.setWis(pc.getClient().getStatDice().getWis());
		cha.setInt(pc.getClient().getStatDice().getInt());
		cha.setCha(pc.getClient().getStatDice().getCha());
		// hp&mp 초기화
		switch(cha.getClassType()) {
			case Lineage.LINEAGE_CLASS_ROYAL:
				cha.setMaxHp(Lineage.royal_hp);
				cha.setMaxMp(Lineage.royal_mp);
				cha.setNowHp(Lineage.royal_hp);
				cha.setNowMp(Lineage.royal_mp);
				break;
			case Lineage.LINEAGE_CLASS_ELF:
				cha.setMaxHp(Lineage.elf_hp);
				cha.setMaxMp(Lineage.elf_mp);
				cha.setNowHp(Lineage.elf_hp);
				cha.setNowMp(Lineage.elf_mp);
				break;
			case Lineage.LINEAGE_CLASS_KNIGHT:
				cha.setMaxHp(Lineage.knight_hp);
				cha.setMaxMp(Lineage.knight_mp);
				cha.setNowHp(Lineage.knight_hp);
				cha.setNowMp(Lineage.knight_mp);
				break;
			case Lineage.LINEAGE_CLASS_WIZARD:
				cha.setMaxHp(Lineage.wizard_hp);
				cha.setMaxMp(Lineage.wizard_mp);
				cha.setNowHp(Lineage.wizard_hp);
				cha.setNowMp(Lineage.wizard_mp);
				break;
			case Lineage.LINEAGE_CLASS_DARKELF:
				cha.setMaxHp(Lineage.darkelf_hp);
				cha.setMaxMp(Lineage.darkelf_mp);
				cha.setNowHp(Lineage.darkelf_hp);
				cha.setNowMp(Lineage.darkelf_mp);
				break;
		}
		cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		// 알림
		if (pc.getLevel() <= 50) {
			ChattingController.toChatting(pc, "\\fT 현재 스텟  :  만 스텟은 30 입니다.", 20);
			ChattingController.toChatting(pc, String.format("\\fT Str[%d] Dex[%d] Con[%d] Int[%d] Cha[%d] Wis[%d]",
					pc.getStr() + pc.getLvStr(), pc.getDex() + pc.getLvDex(), pc.getCon() + pc.getLvCon(),
					pc.getInt() + pc.getLvInt(), pc.getCha() + pc.getLvCha(), pc.getWis() + pc.getLvWis()),
					20);
		}
		if (pc.getLevel() > 50)
		pc.toLvStat(true);
		pc.stat_clear = true;
		pc.el_ok = true;
		ChattingController.toChatting(cha, "스탯을 찍으신 후 '.스초완료' 명령어 를 하셔야 합니다.", Lineage.CHATTING_MODE_MESSAGE);
		ChattingController.toChatting(cha, "스탯을 찍으신 후 '.스초완료' 명령어 를 하셔야 합니다.", Lineage.CHATTING_MODE_MESSAGE);
//		ChattingController.toChatting(cha, "스탯을 찍으신 후 '.스초완료' 명령어 를 하셔야 합니다.", 0);
		
		// 아이템 수량 갱신
		
	}

}
