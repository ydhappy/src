package lineage.world.object.item.newacc;

import java.sql.Connection;

import lineage.bean.lineage.Inventory;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterSpMr;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class Earring extends ItemArmorInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Earring();
		return item;
	}
	
	@Override
	public void toEnchant(PcInstance pc, int en){
		super.toEnchant(pc, en);
		


// 인첸을 성공했다면
		if(en != 0){
			int new_adb = 0;

			switch(getEnLevel()){
				case 1: new_adb=10;
					break;
				case 2: new_adb=20;
					break;
				case 3: new_adb=30;
					break;
				case 4: new_adb=40;
					break;
				case 5: new_adb=40;
					break;
				case 6: new_adb=40;
					break;
				case 7: new_adb=40;
					break;
				case 8: new_adb=50;
					break;

			}

			if(equipped){
				// 이전에 세팅값 빼기.
				pc.setDynamicHp(pc.getDynamicHp()-getDynamicHp());
				// 인첸에따른 새로운값 적용.
				pc.setDynamicHp(pc.getDynamicHp()+new_adb);

			}
			setDynamicHp(new_adb);


			if(Lineage.server_version <= 144){
				pc.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
			}else{
				pc.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
			}
		}
	}

	@Override
	public void toEquipped(Character cha, Inventory inv, int slot){
		super.toEquipped(cha, inv, slot);
		if(equipped){
			switch(getEnLevel()){
			case 1: cha.setEarring(1);
				break;
			case 2: cha.setEarring(1);
				break;
			case 3: cha.setEarring(1);
				break;
			case 4: cha.setEarring(1);
				break;
			case 5: cha.setEarring(1.02);
				break;
			case 6: cha.setEarring(1.04);
				break;
			case 7: cha.setEarring(1.06);
				break;
			case 8: cha.setEarring(1.08);
				break;
		}
		}else{
			cha.setEarring(1);
		}
	}

	@Override
	public void toWorldJoin(Connection con, PcInstance pc){
		if(getEnLevel() !=0){
		int back = 0;
		

		switch(getEnLevel()){
				case 1: back=10;
					break;
				case 2: back=20;
					break;
				case 3: back=30;
					break;
				case 4: back=40;
					break;
				case 5: back=40;
					break;
				case 6: back=40;
					break;
				case 7: back=40;
					break;
				case 8: back=50;
					break;
		}
			setDynamicHp(getDynamicHp()+back);


		if(Lineage.server_version >= 160)
			pc.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
		}
		super.toWorldJoin(con, pc);
	}

}
