package lineage.world.object.item.etc;

import java.sql.Connection;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterSpMr;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class AdenArmor extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new AdenArmor();
		return item;
	}
	
	@Override
	public void toEnchant(PcInstance pc, int en){
		super.toEnchant(pc, en);
		


// 인첸을 성공했다면
		if(en != 0){
			int new_adb = 1;
			switch(getEnLevel()){
				case 1: new_adb=1;
					break;
				case 2: new_adb=1;
					break;
				case 3: new_adb=1;
					break;
				case 4: new_adb=2;
					break;
				case 5: new_adb=2;
					break;
				case 6: new_adb=2;
					break;
				case 7: new_adb=3;
					break;				
				case 8: new_adb=4;
					break;
				case 9: new_adb=5;
					break;
				case 10: new_adb = 6;
					break;
			}

			if(equipped){
				// 이전에 세팅값 빼기.
				pc.setDynamicAden(pc.getDynamicAden()-getDynamicAden());
				// 인첸에따른 새로운값 적용.
				pc.setDynamicAden(pc.getDynamicAden()+new_adb);
			}
			setDynamicAden(new_adb);

			
		}
	}

	@Override
	public void toWorldJoin(Connection con, PcInstance pc){
		if(getEnLevel() !=0){
		int back = 1;
		
		switch(getEnLevel()){
				case 1: back=1;
					break;
				case 2: back=1;
					break;
				case 3: back=1;
					break;
				case 4: back=2;
					break;
				case 5: back=2;
					break;
				case 6: back=2;
					break;
				case 7: back=3;
					break;
				case 8: back=4;
					break;
				case 9: back=5;
					break;
				case 10: back = 6;
					break;
		}
			setDynamicAden(getDynamicAden()+back);

		
		super.toWorldJoin(con, pc);
		}
	}

}
