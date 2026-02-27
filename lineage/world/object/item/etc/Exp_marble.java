package lineage.world.object.item.etc;

import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.npc.background.Crackerexp;
//import lineage.world.object.npc.background.FishExp;

public class Exp_marble extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new Exp_marble();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null && !cha.isWorldDelete() && !cha.isLock() && !cha.isDead()) {
//			if (cha.getResetBaseStat() <= 0 && cha.getResetLevelStat() <= 0 && cha.getLevelUpStat() <= 0) {
				Crackerexp e = new Crackerexp();
				double exp = Util.random(Lineage.exp_marble_min, Lineage.exp_marble_max);
				int clv = cha.getLevel();
				
				switch(clv) {
				case 49:
					cha.toExp(e, exp * 1.1);
					break;
				case 50:
					cha.toExp(e, exp * 1.5);
					break;
				case 51:
					cha.toExp(e, exp * 2.0);
					break;
				case 52:
					cha.toExp(e, exp * 2.5);
					break;
				case 53:
					cha.toExp(e, exp * 3);
					break;
				case 54:
					cha.toExp(e, exp * 3.5);
					break;
				case 55:
					cha.toExp(e, exp * 4);
					break;
				case 56:
					cha.toExp(e, exp * 4.5);
					break;
				case 57:
					cha.toExp(e, exp * 5);
					break;
				case 58:
					cha.toExp(e, exp * 5.5);
					break;
				case 59:
					cha.toExp(e, exp * 6);
					break;
				case 60:
					cha.toExp(e, exp * 6.5);
					break;
				case 61:
					cha.toExp(e, exp * 7);
					break;
				case 62:
					cha.toExp(e, exp * 7.5);
					break;
				case 63:
					cha.toExp(e, exp * 8);
					break;
				case 64:
					cha.toExp(e, exp * 8.5);
					break;
				default:
					cha.toExp(e, exp * 1.1);
					break;
					
				}
				cha.toExp(e, exp);
				
				cha.getInventory().count(this, getCount() - 1, true);
//			} else {
//				ChattingController.toChatting(cha, "스탯 능력치를 올리신 후 사용가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
//			}
		}
	}

}
