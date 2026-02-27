package lineage.world.object.item.helm;

import java.sql.Connection;

import lineage.bean.database.Poly;
import lineage.database.PolyDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.world.controller.BuffController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.potion.BraveryPotion2;
import lineage.world.object.magic.ShapeChange;
import lineage.world.object.magic.item.Bravery;

public class Turban extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new Turban();
		// 시간 설정 - 깃털 갯수
		// : 180개 3시간
		// : 240개 5시간
		// : 450개 10시간
		// 기본 시간을 2시간으로 설정.
		item.setNowTime(60 * 60 * 3);
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		super.toClick(cha, cbp);

		if (equipped){
			if(cha.getSpeed() != 2) {
				BuffController.remove(cha, Bravery.class);
				BuffController.remove(cha, BraveryPotion2.class);
			}
			// 버프 등록
			BuffController.append(this, this);

			
		}else
			// 버프 제거
			BuffController.remove(this, getClass());
		
	}
	
	@Override
	public void toWorldJoin(Connection con, PcInstance pc) {
		super.toWorldJoin(con, pc);
		if (equipped)
			BuffController.append(this, this);
	}

	@Override
	public void toBuffStart(object o) {
		String polyName = null;
		//
		switch (getItem().getNameIdNumber()) {
		case 5163: // 기마용 투구
			if (cha.getClassType() == 0x00) {
				if (cha.getClassSex() == 0)
					polyName = "horse prince";
				else
					polyName = "horse princess";
			} else if (cha.getClassType() == 0x02) {
				polyName = "horse elf";
			}
			break;
		}
		// 변신 처리.
		Poly p = PolyDatabase.getPolyName(polyName);
		if (p == null)
			return;
		ShapeChange.onBuff(cha, cha, p, getNowTime(), false, !cha.isWorldDelete());
	}

	@Override
	public boolean toBuffStop(object o) {
		// 변신 제거.
		BuffController.remove(cha, ShapeChange.class);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		if (cha == null || cha.isWorldDelete())
			return;

		// 종료 처리.
		toBuffStop(cha);
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