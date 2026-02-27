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

public class LuckArmor extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new LuckArmor();
		return item;
	}
	
	@Override
	public void toEnchant(PcInstance pc, int en){
		super.toEnchant(pc, en);
		


// 인첸을 성공했다면
		if(en != 0){
			int new_adb = 5;

			switch(getEnLevel()){
				case 1: new_adb=10;
					break;
				case 2: new_adb=20;
					break;
				case 3: new_adb=30;
					break;
				case 4: new_adb=40;
					break;
				case 5: new_adb=50;
					break;
				case 6: new_adb=60;
					break;
				case 7: new_adb=70;
					break;
				case 8: new_adb=80;
					break;
				case 9: new_adb=90;
					break;
				case 10: new_adb = 100;
					break;

			}

			if(equipped){
				// 이전에 세팅값 빼기.
				pc.setDynamicLuck(pc.getDynamicLuck()-getDynamicLuck());
				// 인첸에따른 새로운값 적용.
				pc.setDynamicLuck(pc.getDynamicLuck()+new_adb);
			}
			setDynamicLuck(new_adb);

			
		}
	}

	@Override
	public void toWorldJoin(Connection con, PcInstance pc){
		if(getEnLevel() !=0){
		int back = 5;
		
		switch(getEnLevel()){
				case 1: back=10;
					break;
				case 2: back=20;
					break;
				case 3: back=30;
					break;
				case 4: back=40;
					break;
				case 5: back=50;
					break;
				case 6: back=60;
					break;
				case 7: back=70;
					break;
				case 8: back=80;
					break;
				case 9: back=90;
					break;
				case 10: back = 100;
					break;
		}
			setDynamicLuck(getDynamicLuck()+back);

		
		super.toWorldJoin(con, pc);
		}
	}

}
