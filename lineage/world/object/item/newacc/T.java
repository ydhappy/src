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

public class T extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new T();
		return item;
	}
	
	@Override
	public void toEnchant(PcInstance pc, int en){
		super.toEnchant(pc, en);
		


// 인첸을 성공했다면
		if(en != 0){
			int new_adb = 0;
			int new_adb1 = 0;
			int new_adb2 = 0;

			switch(getEnLevel()){
				case 1: new_adb=0; new_adb1=0; new_adb2=0;
					break;
				case 2: new_adb=0; new_adb1=0; new_adb2=0;
					break;
				case 3: new_adb=0; new_adb1=0; new_adb2=0;
					break;
				case 4: new_adb=0; new_adb1=0; new_adb2=0;
					break;
				case 5: new_adb=0; new_adb1=0; new_adb2=0;
					break;
				case 6: new_adb=1; new_adb1=0; new_adb2=0;
					break;
				case 7: new_adb=1; new_adb1=1; new_adb2=0;
					break;
				case 8: new_adb=1; new_adb1=1; new_adb2=1;
					break;
				case 9: new_adb=2; new_adb1=2; new_adb2=2;
					break;

			}

			if(equipped){
				pc.setDynamicStr(pc.getDynamicStr()-getDynamicStr());
				pc.setDynamicStr(pc.getDynamicStr()+new_adb);

				pc.setDynamicDex(pc.getDynamicDex()-getDynamicDex());
				pc.setDynamicDex(pc.getDynamicDex()+new_adb1);

				pc.setDynamicInt(pc.getDynamicInt()-getDynamicInt());
				pc.setDynamicInt(pc.getDynamicInt()+new_adb2);
			}
			setDynamicStr(new_adb);
			setDynamicDex(new_adb1);
			setDynamicInt(new_adb2);

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
		int back2 = 0;
		
		switch(getEnLevel()){
				case 1: back=0; back1=0; back2=0;
					break;
				case 2: back=0; back1=0; back2=0;
					break;
				case 3: back=0; back1=0; back2=0;
					break;
				case 4: back=0; back1=0; back2=0;
					break;
				case 5: back=0; back1=0; back2=0;
					break;
				case 6: back=1; back1=0; back2=0;
					break;
				case 7: back=1; back1=1; back2=0;
					break;
				case 8: back=1; back1=1; back2=1;
					break;
				case 9: back=2; back1=2; back2=2;
					break;
		}
			setDynamicStr(getDynamicStr()+back);
			setDynamicDex(getDynamicDex()+back1);
			setDynamicInt(getDynamicInt()+back2);
			




		if(Lineage.server_version >= 160)
			pc.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
		}
		super.toWorldJoin(con, pc);
	}

}
