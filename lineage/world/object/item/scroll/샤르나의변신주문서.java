package lineage.world.object.item.scroll;

import lineage.database.PolyDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.AbsoluteBarrier;
import lineage.world.object.magic.ShapeChange;


public class 샤르나의변신주문서 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 샤르나의변신주문서();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.isSetPoly) {
			ChattingController.toChatting(cha, "세트 변신 때문에 다른 변신을 하실 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		int gfx = 0;
		//      
		switch(getItem().getNameIdNumber()) {
			case 5879: // 샤르나의 변신 주문서 (레벨 40)
				switch(cha.getClassType()) {
					case Lineage.LINEAGE_CLASS_ROYAL:
						gfx = cha.getClassSex()==0 ? 8938 : 8942;
						break;
					case Lineage.LINEAGE_CLASS_KNIGHT:
						gfx = cha.getClassSex()==0 ? 8950 : 8946;
						break;
					case Lineage.LINEAGE_CLASS_ELF:
						gfx = cha.getClassSex()==0 ? 8926 : 8922;
						break;
					case Lineage.LINEAGE_CLASS_WIZARD:
						gfx = cha.getClassSex()==0 ? 8934 : 8930;
						break;
				}
				break;
			case 5880: // 샤르나의 변신 주문서 (레벨 45)
				switch(cha.getClassType()) {
					case Lineage.LINEAGE_CLASS_ROYAL:
						gfx = cha.getClassSex()==0 ? 8939 : 8943;
						break;
					case Lineage.LINEAGE_CLASS_KNIGHT:
						gfx = cha.getClassSex()==0 ? 8951 : 8947;
						break;
					case Lineage.LINEAGE_CLASS_ELF:
						gfx = cha.getClassSex()==0 ? 8927 : 8923;
						break;
					case Lineage.LINEAGE_CLASS_WIZARD:
						gfx = cha.getClassSex()==0 ? 8935 : 8931;
						break;
				}
				break;
			case 5881: // 샤르나의 변신 주문서 (레벨 50)
				switch(cha.getClassType()) {
					case Lineage.LINEAGE_CLASS_ROYAL:
						gfx = cha.getClassSex()==0 ? 8939 : 8944;
						break;
					case Lineage.LINEAGE_CLASS_KNIGHT:
						gfx = cha.getClassSex()==0 ? 8952 : 8948;
						break;
					case Lineage.LINEAGE_CLASS_ELF:
						gfx = cha.getClassSex()==0 ? 8928 : 8924;
						break;
					case Lineage.LINEAGE_CLASS_WIZARD:
						gfx = cha.getClassSex()==0 ? 8936 : 8932;
						break;
		
				}
				break;
		}
		//
		if(gfx > 0)
			ShapeChange.init(cha, cha, PolyDatabase.getPolyGfx(gfx), 1800, bress);
		// 제거
		cha.getInventory().count(this, getCount() - 1, true);
	}

}
