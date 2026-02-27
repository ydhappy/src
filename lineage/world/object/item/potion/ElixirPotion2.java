package lineage.world.object.item.potion;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ElixirPotion2 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ElixirPotion2();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if( !isClick(cha) )
			return;
		
		if(cha instanceof PcInstance){
			PcInstance pc = (PcInstance)cha;
			// 최대 엘릭서값보다 낮을때만 처리.
			if(pc.getElixirStat() < Lineage.item_elixir_max) {
				
				pc.toLvStatElixir(true);				
				
				// 스탯 갱신을위해 패킷 전송.
				pc.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), pc));
				
				// 아이템 수량 갱신
				cha.getInventory().count(this, getCount()-1, true);
			}else{
				// \f1아무일도 일어나지 않았습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
			}
		}
	}
	
}
