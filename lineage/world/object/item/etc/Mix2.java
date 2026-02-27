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



public class Mix2 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Mix2();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		Map<Item, List<Craft>> list = new HashMap<Item, List<Craft>>();
		
		Item i = null;
		
		if(this.item.getName().indexOf("블랙 미스릴") > -1){	
		i = ItemDatabase.find("블랙 미스릴");
		List<Craft> l = new ArrayList<Craft>();
		if(i != null){	
			
			l.add( new Craft(ItemDatabase.find("블랙 미스릴 원석"), 5) );
			l.add( new Craft(ItemDatabase.find("브롭의 위액"), 1) );
			l.add( new Craft(ItemDatabase.find("화산재"), 1) );
			l.add( new Craft(ItemDatabase.find("흑정령석"), 1) );
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
		else if(this.item.getName().indexOf("강철괴") > -1){	
		i = ItemDatabase.find("강철괴");
		List<Craft> l = new ArrayList<Craft>();
		if(i != null){
			
			
			
			l.add( new Craft(ItemDatabase.find("철괴"), 5) );
			l.add( new Craft(ItemDatabase.find("강철 원석"), 5) );
			l.add( new Craft(ItemDatabase.find("아데나"), 500) );
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
		else if(this.item.getName().indexOf("은괴") > -1){	
		i = ItemDatabase.find("은괴");
		List<Craft> l = new ArrayList<Craft>();
		if(i != null){
			
			l.add( new Craft(ItemDatabase.find("은원석조각"), 10) );
			l.add( new Craft(ItemDatabase.find("아데나"), 500) );
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
		else if(this.item.getName().indexOf("황금괴") > -1){	
		i = ItemDatabase.find("황금괴");
		List<Craft> l = new ArrayList<Craft>();
		if(i != null){			
			
			l.add( new Craft(ItemDatabase.find("황금원석조각"), 10) );
			l.add( new Craft(ItemDatabase.find("아데나"), 1000) );
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
		else if(this.item.getName().indexOf("백금괴") > -1){	
		i = ItemDatabase.find("백금괴");
		List<Craft> l = new ArrayList<Craft>();
		if(i != null){			
			
			l.add( new Craft(ItemDatabase.find("백금원석조각"), 10) );
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
		else if(this.item.getName().indexOf("은 판금") > -1){	
		i = ItemDatabase.find("은 판금");
		List<Craft> l = new ArrayList<Craft>();
		if(i != null){
			
			l.add( new Craft(ItemDatabase.find("은괴"), 5) );
			l.add( new Craft(ItemDatabase.find("강철괴"), 3) );
			l.add( new Craft(ItemDatabase.find("아데나"), 1000) );
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
		else if(this.item.getName().indexOf("황금 판금") > -1){	
		i = ItemDatabase.find("황금 판금");
		List<Craft> l = new ArrayList<Craft>();
		if(i != null){
			
			l.add( new Craft(ItemDatabase.find("황금괴"), 5) );
			l.add( new Craft(ItemDatabase.find("강철괴"), 3) );
			l.add( new Craft(ItemDatabase.find("아데나"), 3000) );
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
		else if(this.item.getName().indexOf("백금 판금") > -1){	
		i = ItemDatabase.find("백금 판금");
		List<Craft> l = new ArrayList<Craft>();
		if(i != null){
			
			l.add( new Craft(ItemDatabase.find("백금괴"), 5) );
			l.add( new Craft(ItemDatabase.find("강철괴"), 3) );
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
		}
		else if(this.item.getName().indexOf("(판금)블랙 미스릴") > -1){	
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
		}
		
		else if(this.item.getName().indexOf("흑왕도") > -1){	
			i = ItemDatabase.find("흑왕도");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("흑빛의 이도류"), 1) );
				l.add( new Craft(ItemDatabase.find("그랑카인의 눈물"), 3) );
				l.add( new Craft(ItemDatabase.find("얼음여왕의 숨결"), 9) );
				l.add( new Craft(ItemDatabase.find("저주의 피혁(물결)"), 10) );
				l.add( new Craft(ItemDatabase.find("최고급 루비"), 3) );
				l.add( new Craft(ItemDatabase.find("용의 심장"), 1) );
				l.add( new Craft(ItemDatabase.find("아데나"), 100000) );
				
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
		else if(this.item.getName().indexOf("흑왕아") > -1){	
			i = ItemDatabase.find("흑왕아");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("흑빛의 크로우"), 1) );
				l.add( new Craft(ItemDatabase.find("그랑카인의 눈물"), 3) );
				l.add( new Craft(ItemDatabase.find("얼음여왕의 숨결"), 9) );
				l.add( new Craft(ItemDatabase.find("저주의 피혁(대지)"), 10) );
				l.add( new Craft(ItemDatabase.find("최고급 에메랄드"), 3) );
				l.add( new Craft(ItemDatabase.find("용의 심장"), 1) );
				l.add( new Craft(ItemDatabase.find("아데나"), 100000) );
				
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
		else if(this.item.getName().indexOf("흑왕궁") > -1){	
			i = ItemDatabase.find("흑왕궁");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("흑빛의 활"), 1) );
				l.add( new Craft(ItemDatabase.find("그랑카인의 눈물"), 3) );
				l.add( new Craft(ItemDatabase.find("얼음여왕의 숨결"), 9) );
				l.add( new Craft(ItemDatabase.find("저주의 피혁(바람)"), 10) );
				l.add( new Craft(ItemDatabase.find("최고급 사파이어"), 3) );
				l.add( new Craft(ItemDatabase.find("용의 심장"), 1) );
				l.add( new Craft(ItemDatabase.find("아데나"), 100000) );				
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
		else if(this.item.getName().indexOf("그랑카인의 눈물") > -1){
			i = ItemDatabase.find("그랑카인의 눈물");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("블랙 미스릴"), 1) );
				l.add( new Craft(ItemDatabase.find("암황석"), 1) );
				l.add( new Craft(ItemDatabase.find("검은 혈흔"), 3) );			
				list.put(i, l);
			}
				if(CraftController.isCraft(cha, l, true)){
					CraftController.toCraft(cha, l);
					CraftController.toCraft(cha, i, 1, true, 0);				
				}
			}
		else if(this.item.getName().indexOf("얼음여왕의 숨결") > -1){
			i = ItemDatabase.find("얼음여왕의 숨결");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("에메랄드"), 10) );
				l.add( new Craft(ItemDatabase.find("에바의 유산"), 5) );
				l.add( new Craft(ItemDatabase.find("아데나"), 5000) );	
				list.put(i, l);
			}
				if(CraftController.isCraft(cha, l, true)){
					CraftController.toCraft(cha, l);
					CraftController.toCraft(cha, i, 1, true, 0);				
				}
		}
		else if(this.item.getName().indexOf("저주의 피혁(열화)") > -1){
			i = ItemDatabase.find("저주의 피혁(열화)");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("키메라의 가죽 (사자)"), 5) );
				l.add( new Craft(ItemDatabase.find("아데나"), 500) );
				list.put(i, l);
			}
				if(CraftController.isCraft(cha, l, true)){
					CraftController.toCraft(cha, l);
					CraftController.toCraft(cha, i, 1, true, 0);				
				}
		}
		else if(this.item.getName().indexOf("저주의 피혁(물결)") > -1){
			i = ItemDatabase.find("저주의 피혁(물결)");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("키메라의 가죽 (용)"), 5) );
				l.add( new Craft(ItemDatabase.find("아데나"), 500) );
				list.put(i, l);
			}
				if(CraftController.isCraft(cha, l, true)){
					CraftController.toCraft(cha, l);
					CraftController.toCraft(cha, i, 1, true, 0);				
				}
		}
		else if(this.item.getName().indexOf("저주의 피혁(바람)") > -1){
			i = ItemDatabase.find("저주의 피혁(바람)");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("키메라의 가죽 (산양)"), 5) );
				l.add( new Craft(ItemDatabase.find("아데나"), 500) );
				list.put(i, l);
			}
				if(CraftController.isCraft(cha, l, true)){
					CraftController.toCraft(cha, l);
					CraftController.toCraft(cha, i, 1, true, 0);				
				}
		}
		else if(this.item.getName().indexOf("저주의 피혁(대지)") > -1){
			i = ItemDatabase.find("저주의 피혁(대지)");
			List<Craft> l = new ArrayList<Craft>();
			if(i != null){
				l.add( new Craft(ItemDatabase.find("키메라의 가죽 (뱀)"), 5) );
				l.add( new Craft(ItemDatabase.find("아데나"), 500) );
				list.put(i, l);
			}
				if(CraftController.isCraft(cha, l, true)){
					CraftController.toCraft(cha, l);
					CraftController.toCraft(cha, i, 1, true, 0);				
				}
		}
	
	}
}
