package lineage.world.object.item.etc;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.database.Item;
import lineage.bean.lineage.Craft;
import lineage.network.packet.ClientBasePacket;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

import lineage.database.ItemDatabase;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;



public class OmanMix extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new OmanMix();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		Map<Item, List<Craft>> list = new HashMap<Item, List<Craft>>();
		
		Item i = null;
		
		if(this.item.getName().indexOf("10") > -1){
		i = ItemDatabase.find("오만의탑 10층 이동 부적");
		List<Craft> l = new ArrayList<Craft>();
		if(i != null){
			
			l.add( new Craft(ItemDatabase.find("오만의탑 10층 이동 주문서"), 100) );			
			l.add( new Craft(ItemDatabase.find("아데나"), 1000000) );
			list.put(i, l);
		}
				if(CraftController.isCraft(cha, l, true)){
					CraftController.toCraft(cha, l);
					if(Util.random(0, 99)<=60){
						CraftController.toCraft(cha, i, 1, true, 0);
					}else{
						ChattingController.toChatting(cha, "제작에 실패하셨습니다.");
					}
				}
		}else if(this.item.getName().indexOf("20") > -1){
			i = ItemDatabase.find("오만의탑 20층 이동 부적");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				
				l.add( new Craft(ItemDatabase.find("오만의 탑 20층 이동 주문서"), 100) );
				l.add( new Craft(ItemDatabase.find("아데나"), 1000000) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				if(Util.random(0, 99)<=60){
					CraftController.toCraft(cha, i, 1, true, 0);
				}else{
					ChattingController.toChatting(cha, "제작에 실패하셨습니다.");
				}
			}
		}else if(this.item.getName().indexOf("30") > -1){
			i = ItemDatabase.find("오만의탑 30층 이동 부적");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				
				l.add( new Craft(ItemDatabase.find("오만의 탑 30층 이동 주문서"), 100) );
				l.add( new Craft(ItemDatabase.find("아데나"), 1000000) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				if(Util.random(0, 99)<=60){
					CraftController.toCraft(cha, i, 1, true, 0);
				}else{
					ChattingController.toChatting(cha, "제작에 실패하셨습니다.");
				}
			}
		}else if(this.item.getName().indexOf("40") > -1){
			i = ItemDatabase.find("오만의탑 40층 이동 부적");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				
				l.add( new Craft(ItemDatabase.find("오만의 탑 40층 이동 주문서"), 100) );
				l.add( new Craft(ItemDatabase.find("아데나"), 1000000) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				if(Util.random(0, 99)<=60){
					CraftController.toCraft(cha, i, 1, true, 0);
				}else{
					ChattingController.toChatting(cha, "제작에 실패하셨습니다.");
				}
			}
		}else if(this.item.getName().indexOf("50") > -1){
			i = ItemDatabase.find("오만의탑 50층 이동 부적");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				
				l.add( new Craft(ItemDatabase.find("오만의 탑 50층 이동 주문서"), 100) );
				l.add( new Craft(ItemDatabase.find("아데나"), 1000000) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				if(Util.random(0, 99)<=60){
					CraftController.toCraft(cha, i, 1, true, 0);
				}else{
					ChattingController.toChatting(cha, "제작에 실패하셨습니다.");
				}
			}
		}else if(this.item.getName().indexOf("60") > -1){
			i = ItemDatabase.find("오만의탑 60층 이동 부적");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				
				l.add( new Craft(ItemDatabase.find("오만의 탑 60층 이동 주문서"), 100) );
				l.add( new Craft(ItemDatabase.find("아데나"), 1000000) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				if(Util.random(0, 99)<=60){
					CraftController.toCraft(cha, i, 1, true, 0);
				}else{
					ChattingController.toChatting(cha, "제작에 실패하셨습니다.");
				}
			}
		}else if(this.item.getName().indexOf("70") > -1){
			i = ItemDatabase.find("오만의탑 70층 이동 부적");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				
				l.add( new Craft(ItemDatabase.find("오만의 탑 70층 이동 주문서"), 100) );
				l.add( new Craft(ItemDatabase.find("아데나"), 1000000) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				if(Util.random(0, 99)<=60){
					CraftController.toCraft(cha, i, 1, true, 0);
				}else{
					ChattingController.toChatting(cha, "제작에 실패하셨습니다.");
				}
			}
		}else if(this.item.getName().indexOf("80") > -1){
			i = ItemDatabase.find("오만의탑 80층 이동 부적");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				
				l.add( new Craft(ItemDatabase.find("오만의 탑 80층 이동 주문서"), 100) );
				l.add( new Craft(ItemDatabase.find("아데나"), 1000000) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				if(Util.random(0, 99)<=60){
					CraftController.toCraft(cha, i, 1, true, 0);
				}else{
					ChattingController.toChatting(cha, "제작에 실패하셨습니다.");
				}
			}
		}else if(this.item.getName().indexOf("90") > -1){
			i = ItemDatabase.find("오만의탑 90층 이동 부적");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				
				l.add( new Craft(ItemDatabase.find("오만의 탑 90층 이동 주문서"), 100) );
				l.add( new Craft(ItemDatabase.find("아데나"), 1000000) );
				list.put(i, l);
			}
			if(CraftController.isCraft(cha, l, true)){
				CraftController.toCraft(cha, l);
				if(Util.random(0, 99)<=60){
					CraftController.toCraft(cha, i, 1, true, 0);
				}else{
					ChattingController.toChatting(cha, "제작에 실패하셨습니다.");
				}
			}
		}
		cha.getInventory().count(this, getCount()-1, true);
	}
}
