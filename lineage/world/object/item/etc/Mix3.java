package lineage.world.object.item.etc;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.database.Item;
import lineage.bean.lineage.Craft;
import lineage.network.packet.ClientBasePacket;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

import lineage.database.ItemDatabase;
import lineage.world.controller.CraftController;



public class Mix3 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Mix3();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		Map<Item, List<Craft>> list = new HashMap<Item, List<Craft>>();
		
		Item i = null;
		
		if(this.item.getName().indexOf("(판금)블랙 미스릴") > -1){
		i = ItemDatabase.find("블랙 미스릴 판금");
		List<Craft> l = new ArrayList<Craft>();
		if(i != null){
			
			l.add( new Craft(ItemDatabase.find("블랙 미스릴"), 10) );
			l.add( new Craft(ItemDatabase.find("백금 판금"), 1) );
			l.add( new Craft(ItemDatabase.find("황금 판금"), 1) );
			l.add( new Craft(ItemDatabase.find("은 판금"), 1) );
			l.add( new Craft(ItemDatabase.find("오리하루콘 판금"), 1) );
			l.add( new Craft(ItemDatabase.find("미스릴 판금"), 1) );
			l.add( new Craft(ItemDatabase.find("아데나"), 10000) );
			list.put(i, l);
		}
		//재료 확인
				if(CraftController.isCraft(cha, l, true)){
					//재료제거
					CraftController.toCraft(cha, l);
					// 지급.
					CraftController.toCraft(cha, i, 1, true, 0);				
				}
		}else if(this.item.getName().indexOf("골렘의 숨결") > -1){
			i = ItemDatabase.find("골렘의 숨결");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				
				l.add( new Craft(ItemDatabase.find("마프르의 유산"), 5) );
				l.add( new Craft(ItemDatabase.find("다이아몬드"), 10) );
				l.add( new Craft(ItemDatabase.find("아데나"), 5000) );
				list.put(i, l);
			}
			//재료 확인
					if(CraftController.isCraft(cha, l, true)){
						//재료제거
						CraftController.toCraft(cha, l);
						// 지급.
						CraftController.toCraft(cha, i, 1, true, 0);				
					}
		}
		//드레이크의 숨결
		else if(this.item.getName().indexOf("드레이크의 숨결") > -1){
			i = ItemDatabase.find("드레이크의 숨결");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				
				l.add( new Craft(ItemDatabase.find("사이하의 유산"), 5) );
				l.add( new Craft(ItemDatabase.find("사파이어"), 10) );
				l.add( new Craft(ItemDatabase.find("아데나"), 5000) );
				list.put(i, l);
			}
			//재료 확인
					if(CraftController.isCraft(cha, l, true)){
						//재료제거
						CraftController.toCraft(cha, l);
						// 지급.
						CraftController.toCraft(cha, i, 1, true, 0);				
					}
		}
		//불새의 숨결
		else if(this.item.getName().indexOf("불새의 숨결") > -1){
			i = ItemDatabase.find("불새의 숨결");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				
				l.add( new Craft(ItemDatabase.find("파아그리오의 유산"), 5) );
				l.add( new Craft(ItemDatabase.find("루비"), 10) );
				l.add( new Craft(ItemDatabase.find("아데나"), 5000) );
				list.put(i, l);
			}
			//재료 확인
					if(CraftController.isCraft(cha, l, true)){
						//재료제거
						CraftController.toCraft(cha, l);
						// 지급.
						CraftController.toCraft(cha, i, 1, true, 0);				
					}
		}
		//미스릴 화살
		else if(this.item.getName().indexOf("미스릴 화살") > -1){
			i = ItemDatabase.find("미스릴 화살");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				
				l.add( new Craft(ItemDatabase.find("미스릴"), 100) );
				l.add( new Craft(ItemDatabase.find("엔트의 줄기"), 10) );
				l.add( new Craft(ItemDatabase.find("강철괴"), 1) );
				list.put(i, l);
			}
			//재료 확인
					if(CraftController.isCraft(cha, l, true)){
						//재료제거
						CraftController.toCraft(cha, l);
						// 지급.
						CraftController.toCraft(cha, i, 2000, true, 0);				
					}
		}
		//오리하루콘 화살
		else if(this.item.getName().indexOf("오리하루콘 화살") > -1){
			i = ItemDatabase.find("오리하루콘 화살");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				
				l.add( new Craft(ItemDatabase.find("오리하루콘"), 100) );
				l.add( new Craft(ItemDatabase.find("엔트의 줄기"), 10) );
				l.add( new Craft(ItemDatabase.find("강철괴"), 1) );
				list.put(i, l);
			}
			//재료 확인
					if(CraftController.isCraft(cha, l, true)){
						//재료제거
						CraftController.toCraft(cha, l);
						// 지급.
						CraftController.toCraft(cha, i, 5000, true, 0);				
					}
		}
		
		else if(this.item.getName().indexOf("고급피혁") > -1){
			i = ItemDatabase.find("고급피혁");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){				
				l.add( new Craft(ItemDatabase.find("동물가죽"), 20) );				
				list.put(i, l);
			}
			//재료 확인
					if(CraftController.isCraft(cha, l, true)){
						//재료제거
						CraftController.toCraft(cha, l);
						// 지급.
						CraftController.toCraft(cha, i, 1, true, 0);				
					}
		}
	
	}
}
