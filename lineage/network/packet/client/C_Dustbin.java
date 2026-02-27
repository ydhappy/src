package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.share.Log;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class C_Dustbin extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_Dustbin(data, length);
		else
			((C_Dustbin)bp).clone(data, length);
		return bp;
	}
	
	public C_Dustbin(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		// 버그 방지.
		if(pc==null || pc.isWorldDelete() || !isRead(4) || pc.getInventory()==null)
			return this;
		
		long count = Lineage.server_version>=380 ? readD() : 1;
		
		for(int i=0 ; i<count ; ++i) {
			ItemInstance item = pc.getInventory().value( readD() );
			if(Lineage.server_version>=380)
				readD();	// ?
			if(pc.getInventory().isRemove(item, item.getCount(), true, true, false)) {
				String item_name = item.toStringDB();
				int item_count = (int)item.getCount();
				if(item_count < 0 || item_count > 2000000000)
					return this;
				long item_objid = item.getObjectId();
				//
				pc.getInventory().count(item, 0, true);
				//
				Log.appendItem(pc, "type|휴지통", String.format("item_name|%s", item_name), String.format("name_objid|%d", item_objid), String.format("item_count|%d", item_count));
			}
		}
		
		return this;
	}

}
