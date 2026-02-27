package lineage.world.object.item.scroll;

import lineage.database.PolyDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.ShapeChange;

public class ScrollPolymorph extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollPolymorph();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if (cha.isFishing()) {
			ChattingController.toChatting(cha, "낚시중에는 변신할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		String name = cbp.readS();

		//아이템 인스탕스 연동.
		PcInstance pc = (PcInstance)cha;
		if(pc.getChkPoly()==true){
			ChattingController.toChatting(pc, "세트아이템 착용으로 사용하실 수 없습니다.");
			return;
		}
		// 시전 처리.
		if(ShapeChange.init(cha, cha, PolyDatabase.getPolyName(name), 1200, bress))
			// 아이템 수량 갱신
			cha.getInventory().count(this, getCount()-1, true);

	}
}