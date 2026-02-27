package lineage.world.object.item.all_night;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 신규인형상자 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 신규인형상자();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (!(cha instanceof PcInstance))
			return;

		PcInstance pc = (PcInstance) cha;
		//String itemName = "마법인형: 왕자";
		String itemName = "수련자 인형 주머니 [왕자]";
	
		switch (pc.getClassType()) {
		case Lineage.LINEAGE_CLASS_ROYAL:
			if(pc.getClassSex() == 0) //남자
				//itemName = "마법인형: 왕자";
			itemName = "수련자 인형 주머니 [왕자]";
			else
				//itemName = "마법인형: 공주";
			itemName = "수련자 인형 주머니 [공주]";
			break;
		case Lineage.LINEAGE_CLASS_KNIGHT:
			if(pc.getClassSex() == 0) //남자
				//itemName = "마법인형: 남기사";
			itemName = "수련자 인형 주머니 [남기사]";
			else
				//itemName = "마법인형: 여기사";
			itemName = "수련자 인형 주머니 [여기사]";
			break;
		case Lineage.LINEAGE_CLASS_ELF:
			if(pc.getClassSex() == 0) //남자
				//itemName = "마법인형: 남요정";
			itemName = "수련자 인형 주머니 [남요정]";
			else
				//itemName = "마법인형: 여요정";
			itemName = "수련자 인형 주머니 [여요정]";
			break;
		case Lineage.LINEAGE_CLASS_WIZARD:
			if(pc.getClassSex() == 0) //남자
				//itemName = "마법인형: 남마법사";
			itemName = "수련자 인형 주머니 [남법사]";
			else
				//itemName = "마법인형: 여마법사";
			itemName = "수련자 인형 주머니 [여법사]";
			break;
		}
		Item item = ItemDatabase.find(itemName);
		CraftController.toCraft(this, cha, item, 1, true, 0, 0, 1);
		ChattingController.toChatting(cha, item.getName()+"을 얻었습니다", Lineage.CHATTING_MODE_MESSAGE);
		cha.getInventory().count(this, getCount() - 1, true);
	}
}
