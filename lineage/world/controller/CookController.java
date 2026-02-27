package lineage.world.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.database.Item;
import lineage.bean.lineage.Craft;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.npc.background.MagicFirewood;

public class CookController {

	static private Map<Item, List<Craft>> craft_list;		// 지급아이템에 연결된 재료목록.
	static private Map<String, Item> request_list;			// 요청문장에 연결된 지급 아이템.
	
	static public void init() {
		//
		craft_list = new HashMap<Item, List<Craft>>();
		request_list = new HashMap<String, Item>();
		// 괴물눈 스테이크
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("괴물 눈 고기"), 1) );
		craft_list.put(ItemDatabase.find("괴물눈 스테이크"), l);
		craft_list.put(ItemDatabase.find("환상의 괴물눈 스테이크"), l);
		request_list.put("request cook 0", ItemDatabase.find("괴물눈 스테이크"));
		request_list.put("request cook 01", ItemDatabase.find("환상의 괴물눈 스테이크"));
		// 곰고기 구이
		l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("곰 고기"), 1) );
		craft_list.put(ItemDatabase.find("곰고기 구이"), l);
		craft_list.put(ItemDatabase.find("환상의 곰고기 구이"), l);
		request_list.put("request cook 1", ItemDatabase.find("곰고기 구이"));
		request_list.put("request cook 11", ItemDatabase.find("환상의 곰고기 구이"));
		// 씨호떡
		l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("해바라기씨"), 1) );
		l.add( new Craft(ItemDatabase.find("꿀"), 1) );
		craft_list.put(ItemDatabase.find("씨호떡"), l);
		craft_list.put(ItemDatabase.find("환상의 씨호떡"), l);
		request_list.put("request cook 2", ItemDatabase.find("씨호떡"));
		request_list.put("request cook 21", ItemDatabase.find("환상의 씨호떡"));
		// 개미다리 치즈구이
		l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("개미다리"), 1) );
		l.add( new Craft(ItemDatabase.find("치즈"), 1) );
		craft_list.put(ItemDatabase.find("개미다리 치즈구이"), l);
		craft_list.put(ItemDatabase.find("환상의 개미다리 치즈구이"), l);
		request_list.put("request cook 3", ItemDatabase.find("개미다리 치즈구이"));
		request_list.put("request cook 31", ItemDatabase.find("환상의 개미다리 치즈구이"));
		// 과일 샐러드
		l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("사과"), 1) );
		l.add( new Craft(ItemDatabase.find("바나나"), 1) );
		l.add( new Craft(ItemDatabase.find("오렌지"), 1) );
		craft_list.put(ItemDatabase.find("과일 샐러드"), l);
		craft_list.put(ItemDatabase.find("환상의 과일 샐러드"), l);
		request_list.put("request cook 4", ItemDatabase.find("과일 샐러드"));
		request_list.put("request cook 41", ItemDatabase.find("환상의 과일 샐러드"));
		// 과일 탕수육
		l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("고기"), 1) );
		l.add( new Craft(ItemDatabase.find("레몬"), 1) );
		l.add( new Craft(ItemDatabase.find("당근"), 1) );
		craft_list.put(ItemDatabase.find("과일 탕수육"), l);
		craft_list.put(ItemDatabase.find("환상의 과일 탕수육"), l);
		request_list.put("request cook 5", ItemDatabase.find("과일 탕수육"));
		request_list.put("request cook 51", ItemDatabase.find("환상의 과일 탕수육"));
		// 멧돼지 꼬치 구이
		l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("멧돼지 고기"), 1) );
		craft_list.put(ItemDatabase.find("멧돼지 꼬치 구이"), l);
		craft_list.put(ItemDatabase.find("환상의 멧돼지 꼬치 구이"), l);
		request_list.put("request cook 6", ItemDatabase.find("멧돼지 꼬치 구이"));
		request_list.put("request cook 61", ItemDatabase.find("환상의 멧돼지 꼬치 구이"));
		// 버섯 스프
		l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("버섯포자의 즙"), 1) );
		l.add( new Craft(ItemDatabase.find("당근"), 1) );
		craft_list.put(ItemDatabase.find("버섯 스프"), l);
		craft_list.put(ItemDatabase.find("환상의 버섯 스프"), l);
		request_list.put("request cook 7", ItemDatabase.find("버섯 스프"));
		request_list.put("request cook 71", ItemDatabase.find("환상의 버섯 스프"));
		
				// 2단계 거미다리 꼬치 구이
				l = new ArrayList<Craft>();
				l.add( new Craft(ItemDatabase.find("거미 다리 살"), 1) );
				l.add( new Craft(ItemDatabase.find("모듬 양념 소스"), 1) );
				craft_list.put(ItemDatabase.find("거미 다리 꼬치 구이"), l);
				craft_list.put(ItemDatabase.find("환상의 거미 다리 꼬치 구이"), l);
				request_list.put("request cook 8", ItemDatabase.find("거미 다리 꼬치 구이"));
				request_list.put("request cook 81", ItemDatabase.find("환상의 거미 다리 꼬치 구이"));
	}
	
	static public void close() {
		//
	}
	
	/**
	 * 요리 요청시 호출됨.
	 * @param cha
	 * @param action
	 */
	static public void toCook(Character cha, String action) {
//		switch(cbp.readC()) {
//			case 0:	// 괴물눈 스테이크
//				break;
//			case 1:	// 곰고기 구이
//				break;
//			case 2:	// 씨호떡
//				break;
//			case 3:	// 개미다리 치즈구이
//				break;
//			case 4:	// 과일 샐러드
//				break;
//			case 5:	// 과일 탕수육
//				break;
//			case 6:	// 멧돼지 꼬치 구이
//				break;
//			case 7:	// 버섯 스프
//				break;
//			case 8:	// 캐비어 카나페
//				break;
//			case 9:	// 악어 스테이크
//				break;
//			case 10:	// 터틀드래곤 과자
//				break;
//			case 11:	// 키위 패롯 구이
//				break;
//			case 12:	// 스콜피온 구이
//				break;
//			case 13:	// 일렉카둠 스튜
//				break;
//			case 14:	// 거미다리 꼬치 구이
//				break;
//			case 15:	// 크랩살 스프
//				break;
//		}
		
		// 근처에 장작이 있는지 확인.
		boolean find = false;
		for(object o : cha.getInsideList(false)) {
			if(o instanceof MagicFirewood && Util.isDistance(cha, o, 1))
				find = true;
		}
		if(!find) {
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1160));
			return;
		}
		// 환상 아이템으로 변경 확률.
		if(Util.random(0, 100) < 20)
			action += "1";
		//
		Item i = request_list.get(action);
		if(i == null)
			return;
		// 재료 확인.
		if(!CraftController.isCraft(cha, craft_list.get(i), true))
			return;
		// 재료 제거.
		CraftController.toCraft(cha, craft_list.get(i));
		// 지급.
		CraftController.toCraft(cha, i, 1, true, 0);
	}
	
}
