package lineage.world.object.item.newacc;

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

public class Boot extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Boot();
		return item;
	}
	
	@Override
	public void toEnchant(PcInstance pc, int en){
		super.toEnchant(pc, en);
		


// 인첸을 성공했다면
		if(en != 0){
			int new_adb = 0;
			int new_adb1 = 0;

			switch(getEnLevel()){
				case 1: new_adb=0; new_adb1=0;
					break;
				case 2: new_adb=0; new_adb1=0;
					break;
				case 3: new_adb=0; new_adb1=0;
					break;
				case 4: new_adb=0; new_adb1=0;
					break;
				case 5: new_adb=0; new_adb1=0;
					break;
				case 6: new_adb=10; new_adb1=10;
					break;
				case 7: new_adb=15; new_adb1=15;
					break;
				case 8: new_adb=20; new_adb1=20;
					break;
				case 9: new_adb=50; new_adb1=50;
					break;

			}

			if(equipped){
				pc.setDynamicHp(pc.getDynamicHp()-getDynamicHp());
				pc.setDynamicHp(pc.getDynamicHp()+new_adb);

				pc.setDynamicMp(pc.getDynamicMp()-getDynamicMp());
				pc.setDynamicMp(pc.getDynamicMp()+new_adb1);


			}
			setDynamicHp(new_adb);
			setDynamicMp(new_adb1);


			if(Lineage.server_version <= 144){
				pc.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
			}else{
				pc.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
			}
		}
	}

	@Override
	public void toWorldJoin(Connection con, PcInstance pc){
		if(getEnLevel() !=0){
		int back = 0;
		int back1 = 0;
		
		switch(getEnLevel()){
				case 1: back=0; back1=0; 
					break;
				case 2: back=0; back1=0; 
					break;
				case 3: back=0; back1=0; 
					break;
				case 4: back=0; back1=0;
					break;
				case 5: back=0; back1=0;
					break;
				case 6: back=10; back1=10; 
					break;
				case 7: back=15; back1=15;
					break;
				case 8: back=20; back1=20; 
					break;
				case 9: back=50; back1=50;
					break;
		}
			setDynamicHp(getDynamicHp()+back);			
			setDynamicMp(getDynamicMp()+back1);

		if(Lineage.server_version >= 160)
			pc.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
		}
		super.toWorldJoin(con, pc);
	}

}
