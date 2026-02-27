package lineage.network.packet.server;

import lineage.bean.database.Definite;
import lineage.database.DefiniteDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.share.Lineage;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;

public class S_InventoryAdd extends S_Inventory {

	static synchronized public BasePacket clone(BasePacket bp, ItemInstance item){
		if(bp == null)
			bp = new S_InventoryAdd(item);
		else
			((S_InventoryAdd)bp).clone(item);
		return bp;
	}
	
	public S_InventoryAdd(ItemInstance item){
		if(item != null)
			clone(item);
	}
	
	public void clone(ItemInstance item){
		clear();

		writeC(Opcodes.S_OPCODE_ITEMADD);
		packet(item);
	}
	
	protected void packet(ItemInstance item) {
		//
		int status = 0;
		if (!item.getItem().isTrade()) status += 2;//교환 불가능
//		if (item.getItem().isCantDelete()) status  += 4;//삭제 불가능
		if (!item.getItem().isEnchant()) status += 8;//인챈불가능
//		if(item.getItem().getWareHouse()>0&&!item.getItem().isTradable()) bit += 16; // 창고보관가능
		if (item.getBress() < 0) status = 46;	// 봉인
		if (item.isDefinite()) status += 1;//확인
		//
		writeD(item.getObjectId());
		
		if(Lineage.server_version >= 360) {
			// 진명황의 집행검 : 1819
			//	: 위값으로 세팅하면 아이템이 빙글 돌아감~ 애니메이션 효과
			Definite d = DefiniteDatabase.find(item.getItem().getNameIdNumber());
			if(d != null)
				writeH(d.getMessage());
			else
				writeH(0);
		}
		
		if(Lineage.server_version >= 380) {
			if(item.getBress()==0)
				writeC(item.getItem().getAction2());
			else
				writeC(item.getItem().getAction1());
			writeC(item.getQuantity());
		} else {
			if(item instanceof ItemWeaponInstance || item instanceof ItemArmorInstance) {
				writeH(item.getItem().getEquippedSlot());
			} else {
				if(item.getBress()==0)
					writeC(item.getItem().getAction2());
				else
					writeC(item.getItem().getAction1());
				writeC(item.getQuantity());
			}
		}
		
		writeH(item.getItem().getInvGfx());
		writeC(item.getBressPacket());
		writeD((int)item.getCount());
		writeC(status);
		writeS(getName(item));
//		writeS(getName(item) + ":" + (test-1));

		if(Lineage.server_version>144 && item.isDefinite()){
			if(item instanceof ItemWeaponInstance)
				toWeapon(item);
			else if(item instanceof ItemArmorInstance)
				toArmor(item);
			else
				toEtc(item);
		} else {
			writeC(0x00);
		}
		
		if(Lineage.server_version >= 360) {
            writeC(23);
            writeC(0);
            writeD(0);
            // 상품 우 볼륨 레벨
            // 법률 책하면 매직 넘버가 와서 (item.getItem().getType() == 10)
            writeC(item.getEnLevel());
		}
		if(Lineage.server_version >= 380) {
			// 3.80 물 상품 번호
			writeD(item.getObjectId());
			writeD(0);
			writeD(0);
			// 7 : 당신은 삭제할 수 있습니다, 2: 삭제할 수 없습니다, 3: 상태 인감
			writeC(7);
			writeD(0);
		}
	}
}
