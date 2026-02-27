package lineage.world.object.item.armor;

import java.sql.Connection;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.world.controller.BuffController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 창천갑옷 extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 창천갑옷();
		item.setNowTime(60 * 60 * 3);
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		super.toClick(cha, cbp);

		if (equipped)
			// 버프 등록
			BuffController.append(cha, this);
		else
			// 버프 제거
			BuffController.remove(cha, getClass());
	}

	@Override
	public void toWorldJoin(Connection con, PcInstance pc) {
		super.toWorldJoin(con, pc);
		if (equipped)
			BuffController.append(cha, this);
	}

	@Override
	public void toBuffEnd(object o) {
		if (cha == null || cha.isWorldDelete())
			return;

		// 인벤에서 제거.
		if(getTime() <= 0) {
			if(isEquipped())
				toClick(cha, null);
			
			cha.getInventory().count(this, 0, true);
		}
	}

	@Override
	public void toBuff(object o) {
		if (cha == null || cha.isWorldDelete())
			return;

		// 1분마다 한번씩 아이템정보 패킷 전송.
		// 아이템명에서 시간을 표현하기 때문에.
		if (getTime() % 60 == 0)
			cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
		if (getTime() <= 1) {
			if (isEquipped())
				toClick(cha, null);
			cha.getInventory().count(this, 0, true);
		}
	}
	
	@Override
	public void toWorldOut(object o) {
		if (cha == null || cha.isWorldDelete())
			return;

		toBuffEnd(o);
	}

}
