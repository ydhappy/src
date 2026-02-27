package lineage.world.object.item.scroll;

import lineage.bean.lineage.StatDice;
import lineage.database.CharactersDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.ServerBasePacket;
import lineage.network.packet.client.C_StatDice;
import lineage.network.packet.server.S_CharacterHp;
import lineage.network.packet.server.S_CharacterMp;
import lineage.network.packet.server.S_CharacterStat;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.CharacterController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ResetStatusScroll extends ItemInstance
{
	private int[] addStat = { Lineage.royal_stat_dice, Lineage.knight_stat_dice, Lineage.elf_stat_dice, Lineage.wizard_stat_dice };

	public static synchronized ItemInstance clone(ItemInstance paramItemInstance)
	{
		if (paramItemInstance == null)
			paramItemInstance = new ResetStatusScroll();
		return paramItemInstance;
	}

	public void toClick(Character paramCharacter, ClientBasePacket paramClientBasePacket)
	{
		if(paramCharacter.getLevel()<=1){
			ChattingController.toChatting(paramCharacter, "레벨 52부터 사용 가능합니다.");
			return;
		}
		PcInstance pc  = (PcInstance)paramCharacter;
		if(pc.stat_clear==true){
			ChattingController.toChatting(cha, String.format("스텟 초기화 중에는 사용 하실 수 없습니다."), 20);
			return;
		}
		
		paramCharacter.getInventory().count(this, getCount() - 1L, true);
		
		// 베이스스탯 재 설정.
		ServerBasePacket sbp = (ServerBasePacket)ServerBasePacket.clone(BasePacketPooling.getPool(ServerBasePacket.class), null);
		sbp.writeC(0);
		sbp.writeC(cha.getClassType());
		
		StatDice localStatDice = StatDice(paramCharacter);
		int i = localStatDice.getStr();
		int j = localStatDice.getDex();
		int k = localStatDice.getCon();
		int m = localStatDice.getWis();
		int n = localStatDice.getCha();
		int i1 = localStatDice.getInt();
		
		
		paramCharacter.setMaxHp(200);
		paramCharacter.setMaxMp(200);
		paramCharacter.setStr(i);
		paramCharacter.setDex(j);
		paramCharacter.setCon(k);
		paramCharacter.setWis(m);
		paramCharacter.setCha(n);
		paramCharacter.setInt(i1);
		
		ChattingController.toChatting(paramCharacter,String.format("[Str:%d] [Dex:%d] [Con:%d]",i,j,k));
		ChattingController.toChatting(paramCharacter,String.format("[Wis:%d] [Cha:%d] [Int:%d]",m,n,i1));
		
		ChattingController.toChatting(paramCharacter, "원하시는 스텟이 나올때 까지 반복하시면 됩니다.");
		ChattingController.toChatting(paramCharacter, "원하는 스텟이 나오면 회상의 촛불로 다시 셋팅하시면 완료됩니다.");
		
	
		paramCharacter.toSender(S_CharacterHp.clone(BasePacketPooling.getPool(S_CharacterHp.class), paramCharacter));
		paramCharacter.toSender(S_CharacterMp.clone(BasePacketPooling.getPool(S_CharacterMp.class), paramCharacter));
		paramCharacter.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), paramCharacter));
		

		
	}

	private StatDice StatDice(Character paramCharacter)
	{
		StatDice localStatDice = new StatDice();
		switch (paramCharacter.getClassType())
		{
		case 0:
			switch (Util.random(0, 3))
			{
			case 0:
				localStatDice.setStr(20);
				localStatDice.setDex(10);
				localStatDice.setCon(10);
				localStatDice.setWis(11);
				localStatDice.setCha(13);
				localStatDice.setInt(11);
				break;
			case 1:
				localStatDice.setStr(13);
				localStatDice.setDex(10);
				localStatDice.setCon(10);
				localStatDice.setWis(11);
				localStatDice.setCha(13);
				localStatDice.setInt(18);
				break;
			case 2:
				localStatDice.setStr(13);
				localStatDice.setDex(18);
				localStatDice.setCon(10);
				localStatDice.setWis(11);
				localStatDice.setCha(13);
				localStatDice.setInt(10);
				break;
			case 3:
				localStatDice.setStr(13);
				localStatDice.setDex(10);
				localStatDice.setCon(18);
				localStatDice.setWis(11);
				localStatDice.setCha(13);
				localStatDice.setInt(10);
				break;
			}
			break;
		case 1:
			switch (Util.random(0, 3))
			{
			case 0:
				localStatDice.setStr(20);
				localStatDice.setDex(12);
				localStatDice.setCon(14);
				localStatDice.setWis(9);
				localStatDice.setCha(12);
				localStatDice.setInt(8);
				break;
			case 1:
				localStatDice.setStr(16);
				localStatDice.setDex(12);
				localStatDice.setCon(14);
				localStatDice.setWis(9);
				localStatDice.setCha(12);
				localStatDice.setInt(12);
				break;
			case 2:
				localStatDice.setStr(16);
				localStatDice.setDex(12);
				localStatDice.setCon(18);
				localStatDice.setWis(9);
				localStatDice.setCha(12);
				localStatDice.setInt(8);
				break;
			case 3:
				localStatDice.setStr(17);
				localStatDice.setDex(12);
				localStatDice.setCon(17);
				localStatDice.setWis(9);
				localStatDice.setCha(12);
				localStatDice.setInt(8);
				break;
			}
			break;
		case 2:
			switch (Util.random(0, 5))
			{
			case 0:
				localStatDice.setStr(12);
				localStatDice.setDex(18);
				localStatDice.setCon(12);
				localStatDice.setWis(12);
				localStatDice.setCha(9);
				localStatDice.setInt(12);
				break;
			case 1:
				localStatDice.setStr(18);
				localStatDice.setDex(12);
				localStatDice.setCon(12);
				localStatDice.setWis(12);
				localStatDice.setCha(9);
				localStatDice.setInt(12);
				break;
			case 2:
				localStatDice.setStr(12);
				localStatDice.setDex(12);
				localStatDice.setCon(12);
				localStatDice.setWis(12);
				localStatDice.setCha(9);
				localStatDice.setInt(18);
				break;
			case 3:
				localStatDice.setStr(11);
				localStatDice.setDex(12);
				localStatDice.setCon(18);
				localStatDice.setWis(13);
				localStatDice.setCha(9);
				localStatDice.setInt(12);
				break;
			case 4:
				localStatDice.setStr(11);
				localStatDice.setDex(18);
				localStatDice.setCon(12);
				localStatDice.setWis(13);
				localStatDice.setCha(9);
				localStatDice.setInt(12);
				break;
			case 5:
				localStatDice.setStr(11);
				localStatDice.setDex(13);
				localStatDice.setCon(18);
				localStatDice.setWis(12);
				localStatDice.setCha(9);
				localStatDice.setInt(12);
				break;
			}
			break;
		case 3:
			switch (Util.random(0, 6))
			{
			case 0:
				localStatDice.setStr(18);
				localStatDice.setDex(7);
				localStatDice.setCon(12);
				localStatDice.setWis(12);
				localStatDice.setCha(8);
				localStatDice.setInt(18);
				break;
			case 1:
				localStatDice.setStr(8);
				localStatDice.setDex(17);
				localStatDice.setCon(12);
				localStatDice.setWis(12);
				localStatDice.setCha(8);
				localStatDice.setInt(18);
				break;
			case 2:
				localStatDice.setStr(8);
				localStatDice.setDex(7);
				localStatDice.setCon(12);
				localStatDice.setWis(12);
				localStatDice.setCha(18);
				localStatDice.setInt(18);
				break;
			case 3:
				localStatDice.setStr(18);
				localStatDice.setDex(7);
				localStatDice.setCon(12);
				localStatDice.setWis(12);
				localStatDice.setCha(8);
				localStatDice.setInt(18);
				break;

			case 4:
				localStatDice.setStr(8);
				localStatDice.setDex(11);
				localStatDice.setCon(12);
				localStatDice.setWis(18);
				localStatDice.setCha(8);
				localStatDice.setInt(18);
				break;
			case 5:
				localStatDice.setStr(12);
				localStatDice.setDex(7);
				localStatDice.setCon(12);
				localStatDice.setWis(18);
				localStatDice.setCha(8);
				localStatDice.setInt(18);
				break;
			case 6:
				localStatDice.setStr(8);
				localStatDice.setDex(7);
				localStatDice.setCon(18);
				localStatDice.setWis(18);
				localStatDice.setCha(8);
				localStatDice.setInt(16);
				break;
		}
		break;
		case 4:
			switch (Util.random(0, 3))
			{
			case 0:
				localStatDice.setStr(14);
				localStatDice.setDex(15);
				localStatDice.setCon(8);
				localStatDice.setWis(18);
				localStatDice.setCha(9);
				localStatDice.setInt(11);
				break;
			case 1:
				localStatDice.setStr(12);
				localStatDice.setDex(15);
				localStatDice.setCon(18);
				localStatDice.setWis(10);
				localStatDice.setCha(9);
				localStatDice.setInt(11);
				break;
			case 2:
				localStatDice.setStr(18);
				localStatDice.setDex(18);
				localStatDice.setCon(9);
				localStatDice.setWis(10);
				localStatDice.setCha(9);
				localStatDice.setInt(11);
				break;
			case 3:
				localStatDice.setStr(12);
				localStatDice.setDex(15);
				localStatDice.setCon(10);
				localStatDice.setWis(18);
				localStatDice.setCha(9);
				localStatDice.setInt(11);
				break;
			}

			break;
		case 6:
			switch (Util.random(0, 3))
			{
			case 0:
				localStatDice.setStr(20);
				localStatDice.setDex(10);
				localStatDice.setCon(10);
				localStatDice.setWis(11);
				localStatDice.setCha(13);
				localStatDice.setInt(11);
				break;
			case 1:
				localStatDice.setStr(13);
				localStatDice.setDex(10);
				localStatDice.setCon(10);
				localStatDice.setWis(11);
				localStatDice.setCha(13);
				localStatDice.setInt(18);
				break;
			case 2:
				localStatDice.setStr(13);
				localStatDice.setDex(18);
				localStatDice.setCon(10);
				localStatDice.setWis(11);
				localStatDice.setCha(13);
				localStatDice.setInt(10);
				break;
			case 3:
				localStatDice.setStr(13);
				localStatDice.setDex(10);
				localStatDice.setCon(18);
				localStatDice.setWis(11);
				localStatDice.setCha(13);
				localStatDice.setInt(10);
				break;
			}
			break;

		}
		return localStatDice;
	
	}
}

/* Location:           D:\orim.jar
 * Qualified Name:     lineage.world.object.item.scroll.ResetStatusScroll
 * JD-Core Version:    0.6.0
 */
