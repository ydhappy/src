package lineage.world.object.item.potion;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ElixirPotion3 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ElixirPotion3();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if( !isClick(cha) )
			return;
		
		if(cha instanceof PcInstance){
			PcInstance pc = (PcInstance)cha;
			
			cha.setElixirReset(cha.getElixirStat());
			cha.setElixirStr(0);
			cha.setElixirDex(0);
			cha.setElixirCon(0);
			cha.setElixirWis(0);
			cha.setElixirInt(0);
			cha.setElixirCha(0);
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
			
			pc.toLvStat(true);
			pc.stat_clear = true;
			ChattingController.toChatting(cha, "스탯을 찍으신 후 '.스초완료' 명령어 를 하셔야 합니다.", 20);
			
			cha.getInventory().count(this, getCount()-1, true);
		}
	}
	
}
