package lineage.world.object.item.wand;

import jsn_soft.TrueTargetController;
import lineage.bean.database.Item;
import lineage.bean.lineage.Clan;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.ClanController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.AbsoluteBarrier;
import lineage.world.object.magic.TrueTarget;

public class EbonyWand2 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new EbonyWand2();
		return item;
	}

	@Override
	public ItemInstance clone(Item item) {
		return super.clone(item);
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// 앱솔상태 해제.
		if (cha.isBuffAbsoluteBarrier())
			BuffController.remove(cha, AbsoluteBarrier.class);
		object o = null;
		long obj_id = cbp.readD();

		// 타겟 찾기
		if (obj_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList(obj_id);

		if (o != null) {
			// 지속시간
			o.setTrueTargetTime(16);
			
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);

			Clan c = ClanController.find(cha.getClanName());
			if (c != null) {
				// 적용
				TrueTargetController.append(o, c);
			}
		}
	}
}
