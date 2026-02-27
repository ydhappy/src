package lineage.world.object.item.scroll;

import lineage.bean.lineage.Inventory;
import lineage.bean.lineage.StatDice;
import lineage.database.CharactersDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
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

public class ResetStatusScroll8 extends ItemInstance
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
    paramCharacter.getInventory().count(this, getCount() - 1L, true);
    int i;
    int j;
    switch (paramCharacter.getClassType())
    {
    case 0:
      i = Lineage.royal_hp;
      j = Lineage.royal_mp;
      break;
    case 1:
      i = Lineage.knight_hp;
      j = Lineage.knight_mp;
      break;
    case 2:
      i = Lineage.elf_hp;
      j = Lineage.elf_mp;
      break;
    case 3:
      i = Lineage.wizard_hp;
      j = Lineage.wizard_mp;
      break;
    case 4:
      i = Lineage.darkelf_hp;
      j = Lineage.darkelf_mp;
      break;
    case 5:
      i = Lineage.dragonknight_hp;
      j = Lineage.dragonknight_mp;
      break;
    default:
      i = Lineage.blackwizard_hp;
      j = Lineage.blackwizard_mp;
    }
    StatDice localStatDice = StatDice(paramCharacter);
    int k = localStatDice.getStr();
    int m = localStatDice.getDex();
    int n = localStatDice.getCon();
    int i1 = localStatDice.getWis();
    int i2 = localStatDice.getCha();
    int i3 = localStatDice.getInt();
    paramCharacter.setMaxHp(i);
    paramCharacter.setMaxMp(j);
    paramCharacter.setStr(k);
    paramCharacter.setDex(m);
    paramCharacter.setCon(n);
    paramCharacter.setWis(i1);
    paramCharacter.setCha(i2);
    paramCharacter.setInt(i3);
    
    paramCharacter.setLvStr(0);
    paramCharacter.setLvDex(0);
    paramCharacter.setLvCon(0);
    paramCharacter.setLvWis(0);
    paramCharacter.setLvCha(0);
    paramCharacter.setLvInt(0);
    
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(new StringBuilder().append("Str: ").append(k).toString());
    localStringBuilder.append(new StringBuilder().append(" Dex: ").append(m).toString());
    localStringBuilder.append(new StringBuilder().append(" Con: ").append(n).toString());
    localStringBuilder.append(new StringBuilder().append(" Wis: ").append(i1).toString());
    localStringBuilder.append(new StringBuilder().append(" Cha: ").append(i2).toString());
    localStringBuilder.append(new StringBuilder().append(" Int: ").append(i3).toString());
    int i4 = 0;
    int i5 = 0;
    int i6 = 1;
    for (int i7 = 1; i7 < paramCharacter.getLevel(); i7++)
    {
      i4 += CharacterController.toStatusUP(paramCharacter, true);
      i5 += CharacterController.toStatusUP(paramCharacter, false);
      i6++;
    }
    int i7 = getMaxHp() + i4;
    int i8 = getMaxMp() + i5;
    switch (paramCharacter.getClassType())
    {
    case 0:
      if (i7 >= Lineage.royal_max_hp)
        i7 = Lineage.royal_max_hp;
      if (i8 < Lineage.royal_max_mp)
        break;
      i8 = Lineage.royal_max_mp;
      break;
    case 1:
      if (i7 >= Lineage.knight_max_hp)
        i7 = Lineage.knight_max_hp;
      if (i8 < Lineage.knight_max_mp)
        break;
      i8 = Lineage.knight_max_mp;
      break;
    case 2:
      if (i7 >= Lineage.elf_max_hp)
        i7 = Lineage.elf_max_hp;
      if (i8 < Lineage.elf_max_mp)
        break;
      i8 = Lineage.elf_max_mp;
      break;
    case 3:
      if (i7 >= Lineage.wizard_max_hp)
        i7 = Lineage.wizard_max_hp;
      if (i8 < Lineage.wizard_max_mp)
        break;
      i8 = Lineage.wizard_max_mp;
      break;
    case 4:
      if (i7 >= Lineage.darkelf_max_hp)
        i7 = Lineage.darkelf_max_hp;
      if (i8 < Lineage.darkelf_max_mp)
        break;
      i8 = Lineage.darkelf_max_mp;
      break;
    case 5:
      if (i7 >= Lineage.dragonknight_max_hp)
        i7 = Lineage.dragonknight_max_hp;
      if (i8 < Lineage.dragonknight_max_mp)
        break;
      i8 = Lineage.dragonknight_max_mp;
      break;
    case 6:
      if (i7 >= Lineage.blackwizard_max_hp)
        i7 = Lineage.blackwizard_max_hp;
      if (i8 < Lineage.blackwizard_max_mp)
        break;
      i8 = Lineage.blackwizard_max_mp;
    }
    paramCharacter.setMaxHp(i7);
    paramCharacter.setMaxMp(i8);
    paramCharacter.setNowHp(paramCharacter.getNowHp() + i4);
    paramCharacter.setNowMp(paramCharacter.getNowMp() + i5);
    localStringBuilder.append(new StringBuilder().append(" Hp: ").append(i7).toString());
    localStringBuilder.append(new StringBuilder().append(" Mp: ").append(i8).toString());
    localStringBuilder.append(new StringBuilder().append(" lv: ").append(i6).toString());
    ChattingController.toChatting(paramCharacter, localStringBuilder.toString(), 20);
    paramCharacter.toSender(S_CharacterHp.clone(BasePacketPooling.getPool(S_CharacterHp.class), paramCharacter));
    paramCharacter.toSender(S_CharacterMp.clone(BasePacketPooling.getPool(S_CharacterMp.class), paramCharacter));
    paramCharacter.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), paramCharacter));
    
    PcInstance pc = (PcInstance) paramCharacter;
 	if (pc.getLevel() > 50)
 		pc.toLvStat(true);
     
    try
    {
      CharactersDatabase.saveCharacterStatus((PcInstance)paramCharacter);
    }
    catch (Exception localException)
    {
    }
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
        localStatDice.setCon(18);
        localStatDice.setWis(11);
        localStatDice.setCha(13);
        localStatDice.setInt(10);
        break;
      case 2:
        localStatDice.setStr(13);
        localStatDice.setDex(10);
        localStatDice.setCon(13);
        localStatDice.setWis(11);
        localStatDice.setCha(18);
        localStatDice.setInt(10);
      }
      break;
    case 1:
      switch (Util.random(0, 0))
      {
      case 0:
        localStatDice.setStr(20);
        localStatDice.setDex(12);
        localStatDice.setCon(14);
        localStatDice.setWis(9);
        localStatDice.setCha(12);
        localStatDice.setInt(8);
      }
      break;
    case 2:
      switch (Util.random(0, 3))
      {
      case 0:
        localStatDice.setStr(11);
        localStatDice.setDex(18);
        localStatDice.setCon(13);
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
        localStatDice.setStr(11);
        localStatDice.setDex(13);
        localStatDice.setCon(18);
        localStatDice.setWis(12);
        localStatDice.setCha(9);
        localStatDice.setInt(12);
        break;
      case 3:
        localStatDice.setStr(12);
        localStatDice.setDex(12);
        localStatDice.setCon(18);
        localStatDice.setWis(12);
        localStatDice.setCha(9);
        localStatDice.setInt(12);
      }
      break;
    case 3:
      switch (Util.random(0, 0))
      {
      case 0:
        localStatDice.setStr(8);
        localStatDice.setDex(7);
        localStatDice.setCon(16);
        localStatDice.setWis(18);
        localStatDice.setCha(8);
        localStatDice.setInt(18);
      }
      break;
    case 4:
      switch (Util.random(0, 3))
      {
      case 0:
        localStatDice.setStr(18);
        localStatDice.setDex(12);
        localStatDice.setCon(18);
        localStatDice.setWis(11);
        localStatDice.setCha(13);
        localStatDice.setInt(11);
        break;
      case 1:
        localStatDice.setStr(18);
        localStatDice.setDex(10);
        localStatDice.setCon(12);
        localStatDice.setWis(11);
        localStatDice.setCha(13);
        localStatDice.setInt(18);
        break;
      case 2:
        localStatDice.setStr(18);
        localStatDice.setDex(18);
        localStatDice.setCon(10);
        localStatDice.setWis(11);
        localStatDice.setCha(13);
        localStatDice.setInt(15);
        break;
      case 3:
        localStatDice.setStr(13);
        localStatDice.setDex(17);
        localStatDice.setCon(18);
        localStatDice.setWis(18);
        localStatDice.setCha(13);
        localStatDice.setInt(10);
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
      }
    case 5:
    }
    return localStatDice;
  }
}