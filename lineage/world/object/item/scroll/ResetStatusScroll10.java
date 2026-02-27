package lineage.world.object.item.scroll;

import java.util.StringTokenizer;

import lineage.bean.database.Item;
import lineage.bean.lineage.Inventory;
import lineage.bean.lineage.StatDice;
import lineage.database.CharactersDatabase;
import lineage.database.ItemDatabase;
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
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ResetStatusScroll10 extends ItemInstance
{
  private int[] addStat = { Lineage.royal_stat_dice, Lineage.knight_stat_dice, Lineage.elf_stat_dice, Lineage.wizard_stat_dice };

  public static synchronized ItemInstance clone(ItemInstance i)
  {
    if (i == null)
      i = new ResetStatusScroll10();
    return i;
  }

  public void toClick(Character cha, ClientBasePacket cbp)
  {

//	  cha.getInventory().count(this, getCount() - 1L, true);
    int i;
    int j;
    switch (cha.getClassType())
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
    StatDice sd = StatDice(cha);
    int k = sd.getStr();
    int m = sd.getDex();
    int n = sd.getCon();
    int i1 = sd.getWis();
    int i2 = sd.getCha();
    int i3 = sd.getInt();
    cha.setMaxHp(i);
    cha.setMaxMp(j);
    cha.setStr(k);
    cha.setDex(m);
    cha.setCon(n);
    cha.setWis(i1);
    cha.setCha(i2);
    cha.setInt(i3);
    
    cha.setLvStr(0);
    cha.setLvDex(0);
    cha.setLvCon(0);
    cha.setLvWis(0);
    cha.setLvCha(0);
    cha.setLvInt(0);
    
    StringBuilder sb = new StringBuilder();
    sb.append(new StringBuilder().append("Str: ").append(k).toString());
    sb.append(new StringBuilder().append(" Dex: ").append(m).toString());
    sb.append(new StringBuilder().append(" Con: ").append(n).toString());
    sb.append(new StringBuilder().append(" Wis: ").append(i1).toString());
    sb.append(new StringBuilder().append(" Cha: ").append(i2).toString());
    sb.append(new StringBuilder().append(" Int: ").append(i3).toString());
    int i4 = 0;
    int i5 = 0;
    int i6 = 1;
    for (int i7 = 1; i7 < cha.getLevel(); i7++)
    {
      i4 += CharacterController.toStatusUP(cha, true);
      i5 += CharacterController.toStatusUP(cha, false);
      i6++;
    }
     int i7 = getMaxHp() + i4;
    int i8 = getMaxMp() + i5;
    switch (cha.getClassType())
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
    cha.setMaxHp(i7);
    cha.setMaxMp(i8);
    cha.setNowHp(cha.getNowHp() + i4);
    cha.setNowMp(cha.getNowMp() + i5);
    sb.append(new StringBuilder().append(" Hp: ").append(i7).toString());
    sb.append(new StringBuilder().append(" Mp: ").append(i8).toString());
    sb.append(new StringBuilder().append(" lv: ").append(i6).toString());
    ChattingController.toChatting(cha, sb.toString(), 20);
    cha.toSender(S_CharacterHp.clone(BasePacketPooling.getPool(S_CharacterHp.class), cha));
    cha.toSender(S_CharacterMp.clone(BasePacketPooling.getPool(S_CharacterMp.class), cha));
    cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
    
    PcInstance pc = (PcInstance) cha;
 	if (pc.getLevel() > 50)
 		pc.toLvStat(true);
 	
 	
 	 
     
    try
    {
      CharactersDatabase.saveCharacterStatus((PcInstance)cha);
    }
    catch (Exception e)
    {
    }
  }
 
public  boolean toCommand(Character cha, String cmd){
			Item i = ItemDatabase.find("스텟초기화물약");
		if (cmd.equalsIgnoreCase(".스초마감")) {
			ItemInstance temp = getInventory().find(i.getName(), i.isPiles());
			cha.getInventory().count(temp, temp.getCount() - 1, true);;
			return true;
		}
		return false;
	}
  

  private StatDice StatDice(Character cha)
  {
    StatDice sd = new StatDice();
    switch (cha.getClassType())
    {
    case 0:
      switch (Util.random(0, 1))
      {
      case 0:
    	  sd.setStr(18);
    	  sd.setDex(10);
    	  sd.setCon(13);
    	  sd.setWis(11);
    	  sd.setCha(13);
    	  sd.setInt(10);
      
      break;
    case 1:
    	sd.setStr(13);
        sd.setDex(10);
        sd.setCon(18);
        sd.setWis(11);
        sd.setCha(13);
        sd.setInt(10);
      }
      break;
    case 1:
      switch (Util.random(0, 0))
      {
      case 0:
    	  sd.setStr(20);
    	  sd.setDex(12);
    	  sd.setCon(14);
    	  sd.setWis(9);
    	  sd.setCha(12);
    	  sd.setInt(8);
        break;
      case 1:
    	  sd.setStr(16);
    	  sd.setDex(12);
    	  sd.setCon(18);
    	  sd.setWis(9);
    	  sd.setCha(12);
    	  sd.setInt(8);
      }
      break;
    case 2:
      switch (Util.random(0, 3))
      {
      case 0:
    	  sd.setStr(11);
    	  sd.setDex(18);
    	  sd.setCon(13);
    	  sd.setWis(12);
    	  sd.setCha(9);
    	  sd.setInt(12);
        break;
      case 1:
    	  sd.setStr(18);
    	  sd.setDex(12);
    	  sd.setCon(12);
    	  sd.setWis(12);
    	  sd.setCha(9);
    	  sd.setInt(12);
        break;
      case 2:
    	  sd.setStr(11);
    	  sd.setDex(13);
    	  sd.setCon(18);
    	  sd.setWis(12);
    	  sd.setCha(9);
    	  sd.setInt(12);
        break;
      case 3:
    	  sd.setStr(12);
    	  sd.setDex(12);
    	  sd.setCon(18);
    	  sd.setWis(12);
    	  sd.setCha(9);
    	  sd.setInt(12);
      }
      break;
    case 3:
      switch (Util.random(0, 4))
      {
      case 0:
        sd.setStr(20);
        sd.setDex(13);
        sd.setCon(18);
        sd.setWis(8);
        sd.setCha(8);
        sd.setInt(8);
        break;
      case 1:
    	  sd.setStr(8);
    	  sd.setDex(7);
    	  sd.setCon(14);
    	  sd.setWis(14);
    	  sd.setCha(18);
    	  sd.setInt(14);
        break;
      case 2:
    	  sd.setStr(8);
    	  sd.setDex(7);
    	  sd.setCon(16);
    	  sd.setWis(18);
    	  sd.setCha(8);
    	  sd.setInt(18);
        break;
      case 3:
    	  sd.setStr(8);
    	  sd.setDex(7);
    	  sd.setCon(18);
    	  sd.setWis(16);
    	  sd.setCha(8);
    	  sd.setInt(18);
        break;
      case 4:
    	  sd.setStr(8);
    	  sd.setDex(7);
    	  sd.setCon(18);
    	  sd.setWis(18);
    	  sd.setCha(8);
    	  sd.setInt(16);
      }
      break;

    }
    return sd;
  }
}