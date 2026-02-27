package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;
import lineage.world.object.instance.ItemInstance;

public class S_CharacterAttackRange extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, ItemInstance item){
		if(bp == null)
			bp = new S_CharacterAttackRange(item);
		else
			((S_CharacterAttackRange)bp).toClone(item);
		return bp;
	}
	
	public S_CharacterAttackRange(ItemInstance item){
		toClone(item);
	}
	
	public void toClone(ItemInstance item) {
		clear();
		int range = 1;		// 공격 가능 거리.
		int type = 0;		// 무기 구분자.
		int type2 = 0;		// 양손무기일때 1
		if(item != null) {
			if(item.isEquipped() && item.getItem().getType2().equalsIgnoreCase("bow"))
				range = 15;
			
			if(item.isEquipped()) {
				switch(item.getItem().getGfxMode()) {
					case 0x32:
						type = 0x01;
						break;
					case 0x18:
						type = 0x0a;
						break;
					default:
						type = 0x02;
						break;
				}
			}
			if(item.getItem().isTohand())
				type2 = 1;
		}
		//
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(0xa0);
		writeC(range);
		writeC(type);
		writeC(type2);
	}
	
}
