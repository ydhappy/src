package sp.item;

import lineage.database.PolyDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.RankController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.BuffRing;
import lineage.world.object.magic.ShapeChange;

public class RankPolyScroll extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new RankPolyScroll();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.isSetPoly) {
			ChattingController.toChatting(cha, "세트 변신 때문에 다른 변신을 하실 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		switch(cha.getClassType()) {
			case 0x00:
				if(cha.getClassSex() == 0)
					ShapeChange.init(cha, cha, PolyDatabase.getPolyName("왕자 랭커"), 60*20, 1);
				else
					ShapeChange.init(cha, cha, PolyDatabase.getPolyName("공주 랭커"), 60*20, 1);
				break;
			case 0x01:
				if(cha.getClassSex() == 0)
					ShapeChange.init(cha, cha, PolyDatabase.getPolyName("남자기사 랭커"), 60*20, 1);
				else
					ShapeChange.init(cha, cha, PolyDatabase.getPolyName("여자기사 랭커"), 60*20, 1);
				break;
			case 0x02:
				if(cha.getClassSex() == 0)
					ShapeChange.init(cha, cha, PolyDatabase.getPolyName("남자요정 랭커"), 60*20, 1);
				else
					ShapeChange.init(cha, cha, PolyDatabase.getPolyName("여자요정 랭커"), 60*20, 1);
				break;
			case 0x03:
				if(cha.getClassSex() == 0)
					ShapeChange.init(cha, cha, PolyDatabase.getPolyName("남자법사 랭커"), 60*20, 1);
				else
					ShapeChange.init(cha, cha, PolyDatabase.getPolyName("여자법사 랭커"), 60*20, 1);
				break;

			}
		cha.getInventory().count(this, getCount()-1, true);
        if (!cha.getFast_poly().equalsIgnoreCase("Dragon Slayer") || !cha.getFast_poly().equalsIgnoreCase("Platinum death knight")
	    		|| !cha.getFast_poly().equalsIgnoreCase("girtas") || !cha.getFast_poly().equalsIgnoreCase("Doppelganger")
	    		|| !cha.getFast_poly().equalsIgnoreCase("zillian of moon") || !cha.getFast_poly().equalsIgnoreCase("Flame death knight")
	    		|| !cha.getFast_poly().equalsIgnoreCase("Platinum Baphomet")) {

			BuffController.remove(cha, BuffRing.class);
			}
		}
	}
