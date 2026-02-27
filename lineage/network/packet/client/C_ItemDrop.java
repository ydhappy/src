package lineage.network.packet.client;

import lineage.bean.lineage.Inventory;
import lineage.network.packet.BasePacket;
import lineage.network.packet.ClientBasePacket;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class C_ItemDrop extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_ItemDrop(data, length);
		else
			((C_ItemDrop)bp).clone(data, length);
		return bp;
	}
	
	public C_ItemDrop(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		// 버그 방지.
		if(pc==null || pc.getInventory()==null || !isRead(12) || pc.isDead() || pc.isWorldDelete())
			return this;
		
		if (Lineage.open_wait) {
			ChattingController.toChatting(pc, "바닥에 아이템을 버릴 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return this;
		}	
		
		long length = 1;
		if(Lineage.server_version >= 380)
			length = readD();
		for(int i=0 ; i<length ; ++i) {
			int x = readH();
			int y = readH();
			long object_id = readD();
			int count = (int)readD();
			if(count < 0 || count > 2000000000) {
				return this;
			}
			
			Inventory inv = pc.getInventory();
			ItemInstance item = inv.value( object_id );
			
//			if(pc.getMap()>=0 ) {
//				ChattingController.toChatting(pc, "아이템 드랍 불가합니다.");	
//				return this;
//			}
			
//			if (item.getItem().getAction1() != 0 || item.getItem().getAction2() != 0) {
//				ChattingController.toChatting(pc, "해당 아이템은 드롭이 불가능합니다.");
//				return this;
//			}
				
			
			if(item!=null && (pc.getGm()>0 || !pc.isTransparent())){
				if(Util.isDistance(pc.getX(), pc.getY(), pc.getMap(), x, y, pc.getMap(), 2)) {
					// 플러그인 확인.
					if(PluginController.init(C_ItemDrop.class, "init", this, pc, item) == null)
						inv.toDrop(item, count, x, y, true);
				}
			}
		}
		
		return this;
	}
}
