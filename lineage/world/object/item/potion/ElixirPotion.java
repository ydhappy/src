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

public class ElixirPotion extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ElixirPotion();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if( !isClick(cha) )
			return;
		if(cha instanceof PcInstance){
			PcInstance pc = (PcInstance)cha;
		if (pc.getLevel() < Lineage.elixir_min_level) {
			ChattingController.toChatting(pc, String.format("%d레벨 이상 사용 가능합니다.", Lineage.elixir_min_level), Lineage.CHATTING_MODE_MESSAGE);
			return;
		} else {
		}
			// 최대 엘릭서값보다 낮을때만 처리.
			if(pc.getElixirStat() < Lineage.item_elixir_max) {
				// 스탯 상승.
				switch(getItem().getNameIdNumber()){
					case 2530:	// str
						if(pc.getStr()+pc.getLvStr()+pc.getElixirStr() < Lineage.stat_max)
							pc.setElixirStr(cha.getElixirStr()+1);
						else
							pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
						break;
					case 2532:	// dex
						if(pc.getDex()+pc.getLvDex()+pc.getElixirDex() < Lineage.stat_max)
							pc.setElixirDex(cha.getElixirDex()+1);
						else
							pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
						break;
					case 2531:	// con
						if(pc.getCon()+pc.getLvCon()+pc.getElixirCon() < Lineage.stat_max)
							pc.setElixirCon(cha.getElixirCon()+1);
						else
							pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
						break;
					case 2534:	// wis
						if(pc.getWis()+pc.getLvWis()+pc.getElixirWis() < Lineage.stat_max)
							pc.setElixirWis(cha.getElixirWis()+1);
						else
							pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
						break;
					case 2533:	// int
						if(pc.getInt()+pc.getLvInt()+pc.getElixirInt() < Lineage.stat_max)
							pc.setElixirInt(cha.getElixirInt()+1);
						else
							pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
						break;
					case 2535:	// cha
						if(pc.getCha()+pc.getLvCha()+pc.getElixirCha() < Lineage.stat_max)
							pc.setElixirCha(cha.getElixirCha()+1);
						else
							pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
						break;
				}
				// 스탯 갱신을위해 패킷 전송.
				pc.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), pc));
				
				// 아이템 수량 갱신
				cha.getInventory().count(this, getCount()-1, true);
			}else{
				// \f1아무일도 일어나지 않았습니다.
				ChattingController.toChatting(pc, String.format("엘릭서는 최대 %d개 사용 가능합니다.", Lineage.item_elixir_max), Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}
	
}
