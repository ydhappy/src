package lineage.world.object.item.SPITEM;

import lineage.bean.database.Item;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectAttack;
import lineage.network.packet.server.S_ObjectHeading;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.Lightning;

public class 화염의막대 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 화염의막대();
		return item;
	}

	@Override
	public ItemInstance clone(Item item) {
		return super.clone(item);
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		object o = null;
		int obj_id = cbp.readD();
		int x = cbp.readH();
		int y = cbp.readH();

		// 객체 찾기.
		if (obj_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList(obj_id);
		// 처리.

		int action = cha.getClassGfx()==cha.getGfx() || Lineage.server_version <= 270 ? Lineage.GFX_MODE_WAND : 9;
		
		try {
			if (cha.getMap() >= 10001 && cha.getMap() <= 10020) {
				if (o == null) {
					int cal_head = Util.calcheading(cha, x, y);
					cha.setHeading(cal_head);
					cha.toSender(S_ObjectHeading.clone(BasePacketPooling.getPool(S_ObjectHeading.class), cha), false);
					cha.toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), cha,
							action, getItem().getEffect(), x, y), cha instanceof PcInstance);
				} else {
					Lightning.init(cha, SkillDatabase.find(500), obj_id, x, y);
				}
			} else {
				ChattingController.toChatting(cha, "해당 맵에서는 사용 불가합니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
