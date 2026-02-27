package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_PetInventoryEquipped;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.PetInstance;
import lineage.world.object.item.pet.PetArmor;
import lineage.world.object.item.pet.PetWeapon;

public class C_PetItemClick extends ClientBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_PetItemClick(data, length);
		else
			((C_PetItemClick)bp).clone(data, length);
		return bp;
	}
	
	public C_PetItemClick(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc) {
		readC();
		long objId = readD();		// 아이템 사용 요청된 펫 아이디
		int invId = readC();		// 펫 인벤토리에 있는 아이템 순번.
		
		object o = pc.getSummon().find(objId);
		if(o!=null && o instanceof PetInstance) {
			PetInstance pet = (PetInstance)o;
			ItemInstance item = pet.getInventory().getList().get(invId);
			item.toClick(pet, null);
			if(item instanceof PetArmor || item instanceof PetWeapon)
				pc.toSender(S_PetInventoryEquipped.clone(BasePacketPooling.getPool(S_PetInventoryEquipped.class), o, item, invId));
		}
		
		return this;
	}

}
