package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PetInstance;

public class S_PetInventory extends S_Inventory {

	static synchronized public BasePacket clone(BasePacket bp, PetInstance pet){
		if(bp == null)
			bp = new S_PetInventory(pet);
		else
			((S_PetInventory)bp).toClone(pet);
		return bp;
	}
	
	public S_PetInventory(PetInstance pet){
		toClone(pet);
	}
	
	public void toClone(PetInstance pet){
		clear();
		
		writeC(Opcodes.S_OPCODE_WAREHOUSE);
		writeD(pet.getObjectId());
		writeH(pet.getInventory().getList().size());
		writeC(0x0b);
		for(ItemInstance item : pet.getInventory().getList()){
			writeD(item.getObjectId());
			writeC(22);													// 무기(22), 방어구(2), 기타(0)
			writeH(item.getItem().getInvGfx());	
			writeC(item.getBressPacket());
			writeD(item.getCount());
			if(item.isEquipped())
				writeC(0x03);
			else
				writeC(item.isDefinite() ? 0x01 : 0x00);				// 0:미확인 1:확인 2:착용 3:착용 4:? 5:?
			writeS(getName(item));
		}
		writeC((266-pet.getTotalAc()));
	}

}
