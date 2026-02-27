package lineage.world.object.item.etc;


import lineage.bean.database.Item;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_LetterNotice;
import lineage.share.Common;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;

import lineage.database.ItemDatabase;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.MagicDoll;
import lineage.world.controller.ChattingController;
import lineage.world.controller.MagicDollController;
import lineage.share.Lineage;


public class MagicDoll3 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new MagicDoll3();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){

		PcInstance pc = (PcInstance)cha;
		if(MagicDollController.getSize(pc) >0){
				ChattingController.toChatting(cha, String.format("인형소환중이라 사용불가합니다..", 20));
				return;
		}

		int cnt = 0;
		int knt = 0;
		
		for(ItemInstance ii : cha.getInventory().getList()){

			if((ii.getName().equalsIgnoreCase("$5419")) || (ii.getName().equalsIgnoreCase("$7449")) || (ii.getName().equalsIgnoreCase("$7400")) ||
				(ii.getName().equalsIgnoreCase("$5826")) || (ii.getName().equalsIgnoreCase("$5417")) || (ii.getName().equalsIgnoreCase("$7511"))){
				cnt = cnt+1;
			//	ChattingController.toChatting(cha, String.format("%s 인형수량체크.cnt", cnt,20));
			}
			
		}

		if(cnt>2){
			for(ItemInstance iii : cha.getInventory().getList()){
				if(iii.getName().equalsIgnoreCase("$5419") ){
					cha.getInventory().remove(iii, true);
					knt = knt +1;
				}else if(iii.getName().equalsIgnoreCase("$7449") ){
					cha.getInventory().remove(iii, true);
					knt = knt +1;
				}else if(iii.getName().equalsIgnoreCase("$7400") ){
					cha.getInventory().remove(iii, true);
					knt = knt +1;
				}else if(iii.getName().equalsIgnoreCase("$5826") ){
					cha.getInventory().remove(iii, true);
					knt = knt +1;
				}else if(iii.getName().equalsIgnoreCase("$5417") ){
					cha.getInventory().remove(iii, true);
					knt = knt +1;
				}else if(iii.getName().equalsIgnoreCase("$7511") ){
					cha.getInventory().remove(iii, true);
					knt = knt +1;
				}
				if(knt == 3){
					Object ob1;
					Object ob2;

					ob2 = "3차마법인형바구니";
					ob1 = ItemDatabase.find4((String)ob2);
					if(ob1 !=null){
						ItemInstance i = ItemDatabase.newInstance((Item)ob1);
						i.setCount(1);
						i.setDefinite(true);
						cha.getInventory().append(i, 1);
						//ItemDatabase.setPool(i);					
					}
					cha.getInventory().count(this, getCount() - 1L, true);
					ChattingController.toChatting(cha, String.format("3차 마법 인형 바구니를 습득 하셨습니다.", 20));
					return;
				}
			}

			
		}else{
			ChattingController.toChatting(cha, String.format("2차인형 3개가 부족합니다.", 20));
			return;
		}
	}
}
