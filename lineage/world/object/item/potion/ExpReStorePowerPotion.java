package lineage.world.object.item.potion;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CharacterStat;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ExpReStorePowerPotion extends ItemInstance
{
  public static synchronized ItemInstance clone(ItemInstance ii)
  {
    if (ii == null)
      ii = new ExpReStorePowerPotion();
    return ii;
  }

  public void toClick(Character cha, ClientBasePacket cbp)
  {
    if ((cha instanceof PcInstance))
    {
      PcInstance pc = (PcInstance)cha;
      if (pc.getLostExp() > 0.0D)
      {
        pc.setExp(pc.getExp() + pc.getLostExp());
        pc.setLostExp(0.0D);
        pc.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), pc));
        ChattingController.toChatting(cha, "불꽃의 힘이 파도처럼 치솟아 오릅니다. 경험치가 복구 되었습니다.", 20);
        cha.getInventory().count(this, getCount() - 1L, true);
      }

    
      else
      ChattingController.toChatting(cha, "더 이상 복구할 경험치가 없습니다.", 20);
      }
    }
  
  }
