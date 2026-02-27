package lineage.world.object.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class CaliphPouch extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new CaliphPouch();
		return item;
	}
	
	static private final Integer chance_max = 100000;
	static private Map<String, List<String>> list = new HashMap<String, List<String>>();
	static {
		//
		list.put("카리프의 주머니(루비)", new ArrayList<String>());
		list.put("카리프의 주머니(에메랄드)", new ArrayList<String>());
		list.put("카리프의 주머니(사파이어)", new ArrayList<String>());
		list.put("카리프의 주머니(다이아몬드)", new ArrayList<String>());
		list.put("카리프의 고급 주머니(루비)", new ArrayList<String>());
		list.put("카리프의 고급 주머니(에메랄드)", new ArrayList<String>());
		list.put("카리프의 고급 주머니(사파이어)", new ArrayList<String>());
		list.put("카리프의 고급 주머니(다이아몬드)", new ArrayList<String>());
		list.put("아인하사드의 선물)", new ArrayList<String>());
		//
		list.get("카리프의 주머니(루비)").add("주홍 물약|2|1000");
		list.get("카리프의 주머니(루비)").add("농축 고급 체력 회복제|1|500");
		list.get("카리프의 주머니(루비)").add("초록 물약|1|1000");
		list.get("카리프의 주머니(루비)").add("강화 초록 물약|1|500");
		list.get("카리프의 주머니(루비)").add("용기의 물약|1|500");
		list.get("카리프의 주머니(루비)").add("고급피혁|4|800");
		list.get("카리프의 주머니(루비)").add("미스릴 원석|2|800");
		list.get("카리프의 주머니(루비)").add("백금 원석 조각|1|100");
		list.get("카리프의 주머니(루비)").add("빛나는 비늘|1|100");
		list.get("카리프의 주머니(루비)").add("은 원석 조각|1|50");
		list.get("카리프의 주머니(루비)").add("철괴|2|1000");
		list.get("카리프의 주머니(루비)").add("키메라의 가죽:사자|1|1000");
		list.get("카리프의 주머니(루비)").add("흑마석|3|800");
		list.get("카리프의 주머니(루비)").add("파아그리오의 유산|1|150");
		//
		list.get("카리프의 주머니(에메랄드)").add("빨간 물약|3|1000");
		list.get("카리프의 주머니(에메랄드)").add("맑은 물약|1|800");
		list.get("카리프의 주머니(에메랄드)").add("초록 물약|1|1000");
		list.get("카리프의 주머니(에메랄드)").add("엘븐 와퍼|1|400");
		list.get("카리프의 주머니(에메랄드)").add("파란 물약|1|400");
		list.get("카리프의 주머니(에메랄드)").add("강철괴|1|300");
		list.get("카리프의 주머니(에메랄드)").add("블랙 미스릴 원석|1|50");
		list.get("카리프의 주머니(에메랄드)").add("빛나는 비늘|1|150");
		list.get("카리프의 주머니(에메랄드)").add("오리하루콘|4|500");
		list.get("카리프의 주머니(에메랄드)").add("은 원석 조각|1|50");
		list.get("카리프의 주머니(에메랄드)").add("정령의 돌|1|1000");
		list.get("카리프의 주머니(에메랄드)").add("철괴|3|1000");
		list.get("카리프의 주머니(에메랄드)").add("키메라의 가죽:용|1|1000");
		list.get("카리프의 주머니(에메랄드)").add("에바의 유산|1|150");
		//
		list.get("카리프의 주머니(사파이어)").add("빨간 물약|5|1000");
		list.get("카리프의 주머니(사파이어)").add("초록 물약|2|1000");
		list.get("카리프의 주머니(사파이어)").add("엘븐 와퍼|2|400");
		list.get("카리프의 주머니(사파이어)").add("지혜의 물약|1|400");
		list.get("카리프의 주머니(사파이어)").add("파란 물약|2|400");
		list.get("카리프의 주머니(사파이어)").add("마력의 돌|1|500");
		list.get("카리프의 주머니(사파이어)").add("미스릴|5|1000");
		list.get("카리프의 주머니(사파이어)").add("백금 원석 조각|1|50");
		list.get("카리프의 주머니(사파이어)").add("블랙 미스릴 원석|1|80");
		list.get("카리프의 주머니(사파이어)").add("빛나는 비늘|1|150");
		list.get("카리프의 주머니(사파이어)").add("철괴|2|1000");
		list.get("카리프의 주머니(사파이어)").add("키메라의 가죽:산양|1|1000");
		list.get("카리프의 주머니(사파이어)").add("페어리의 날개|1|600");
		list.get("카리프의 주머니(사파이어)").add("황금 원석 조각|1|50");
		list.get("카리프의 주머니(사파이어)").add("사이하의 유산|1|150");
		//
		list.get("카리프의 주머니(다이아몬드)").add("빨간 물약|2|1000");
		list.get("카리프의 주머니(다이아몬드)").add("농축 강력 체력 회복제|1|500");
		list.get("카리프의 주머니(다이아몬드)").add("초록 물약|1|1000");
		list.get("카리프의 주머니(다이아몬드)").add("강화 초록 물약|1|600");
		list.get("카리프의 주머니(다이아몬드)").add("용기의 물약|1|500");
		list.get("카리프의 주머니(다이아몬드)").add("강철괴|1|300");
		list.get("카리프의 주머니(다이아몬드)").add("달빛의 눈물|1|200");
		list.get("카리프의 주머니(다이아몬드)").add("미스릴|5|1000");
		list.get("카리프의 주머니(다이아몬드)").add("철괴|2|1000");
		list.get("카리프의 주머니(다이아몬드)").add("키메라의 가죽:뱀|1|1000");
		list.get("카리프의 주머니(다이아몬드)").add("황금 원석 조각|1|50");
		list.get("카리프의 주머니(다이아몬드)").add("숫돌|1|1000");
		list.get("카리프의 주머니(다이아몬드)").add("마프르의 유산|1|150");
		//
		list.get("카리프의 고급 주머니(루비)").add("주홍 물약|5|1000");
		list.get("카리프의 고급 주머니(루비)").add("농축 고급 체력 회복제|5|600");
		list.get("카리프의 고급 주머니(루비)").add("농축 강력 체력 회복제|2|400");
		list.get("카리프의 고급 주머니(루비)").add("초록 물약|5|1000");
		list.get("카리프의 고급 주머니(루비)").add("강화 초록 물약|2|600");
		list.get("카리프의 고급 주머니(루비)").add("용기의 물약|2|600");
		list.get("카리프의 고급 주머니(루비)").add("미스릴 원석|3|1000");
		list.get("카리프의 고급 주머니(루비)").add("백금 원석 조각|1|50");
		list.get("카리프의 고급 주머니(루비)").add("빛나는 비늘|2|150");
		list.get("카리프의 고급 주머니(루비)").add("오우거의 피|1|50");
		list.get("카리프의 고급 주머니(루비)").add("은 원석 조각|2|50");
		list.get("카리프의 고급 주머니(루비)").add("철괴|4|1000");
		list.get("카리프의 고급 주머니(루비)").add("키메라의 가죽:사자|1|1000");
		list.get("카리프의 고급 주머니(루비)").add("화룡 비늘|1|5");
		list.get("카리프의 고급 주머니(루비)").add("황금 원석 조각|1|50");
		list.get("카리프의 고급 주머니(루비)").add("흑마석|5|400");
		list.get("카리프의 고급 주머니(루비)").add("파아그리오의 유산|1|250");
		//
		list.get("카리프의 고급 주머니(에메랄드)").add("주홍 물약|8|1000");
		list.get("카리프의 고급 주머니(에메랄드)").add("맑은 물약|5|800");
		list.get("카리프의 고급 주머니(에메랄드)").add("용기의 물약|1|500");
		list.get("카리프의 고급 주머니(에메랄드)").add("엘븐 와퍼|3|400");
		list.get("카리프의 고급 주머니(에메랄드)").add("지혜의 물약|2|500");
		list.get("카리프의 고급 주머니(에메랄드)").add("파란 물약|3|400");
		list.get("카리프의 고급 주머니(에메랄드)").add("강철괴|1|300");
		list.get("카리프의 고급 주머니(에메랄드)").add("바람의 눈물|1|200");
		list.get("카리프의 고급 주머니(에메랄드)").add("빛나는 비늘|2|150");
		list.get("카리프의 고급 주머니(에메랄드)").add("블랙 미스릴 원석|1|100");
		list.get("카리프의 고급 주머니(에메랄드)").add("수룡 비늘|1|5");
		list.get("카리프의 고급 주머니(에메랄드)").add("오리하루콘|8|600");
		list.get("카리프의 고급 주머니(에메랄드)").add("은 원석 조각|2|50");
		list.get("카리프의 고급 주머니(에메랄드)").add("정령의 돌|2|800");
		list.get("카리프의 고급 주머니(에메랄드)").add("철괴|5|1000");
		list.get("카리프의 고급 주머니(에메랄드)").add("키메라의 가죽:용|1|1000");
		list.get("카리프의 고급 주머니(에메랄드)").add("에바의 유산|1|250");
		//
		list.get("카리프의 고급 주머니(사파이어)").add("빨간 물약|20|1000");
		list.get("카리프의 고급 주머니(사파이어)").add("강화 초록 물약|2|500");
		list.get("카리프의 고급 주머니(사파이어)").add("엘븐 와퍼|2|400");
		list.get("카리프의 고급 주머니(사파이어)").add("지혜의 물약|2|400");
		list.get("카리프의 고급 주머니(사파이어)").add("파란 물약|2|400");
		list.get("카리프의 고급 주머니(사파이어)").add("그리폰의 깃털|1|300");
		list.get("카리프의 고급 주머니(사파이어)").add("마력의 돌|4|500");
		list.get("카리프의 고급 주머니(사파이어)").add("미스릴|40|400");
		list.get("카리프의 고급 주머니(사파이어)").add("백금 원석 조각|1|50");
		list.get("카리프의 고급 주머니(사파이어)").add("블랙 미스릴 원석|1|100");
		list.get("카리프의 고급 주머니(사파이어)").add("빛나는 비늘|2|100");
		list.get("카리프의 고급 주머니(사파이어)").add("철괴|5|1000");
		list.get("카리프의 고급 주머니(사파이어)").add("키메라의 가죽:산양|1|1000");
		list.get("카리프의 고급 주머니(사파이어)").add("페어리의 날개|2|500");
		list.get("카리프의 고급 주머니(사파이어)").add("풍룡 비늘|1|5");
		list.get("카리프의 고급 주머니(사파이어)").add("황금 원석 조각|2|50");
		list.get("카리프의 고급 주머니(사파이어)").add("사이하의 유산|1|250");
		//
		list.get("카리프의 고급 주머니(다이아몬드)").add("빨간 물약|20|1000");
		list.get("카리프의 고급 주머니(다이아몬드)").add("농축 강력 체력 회복제|2|400");
		list.get("카리프의 고급 주머니(다이아몬드)").add("강화 초록 물약|3|600");
		list.get("카리프의 고급 주머니(다이아몬드)").add("용기의 물약|2|500");
		list.get("카리프의 고급 주머니(다이아몬드)").add("강철괴|2|100");
		list.get("카리프의 고급 주머니(다이아몬드)").add("달빛의 눈물|1|150");
		list.get("카리프의 고급 주머니(다이아몬드)").add("미스릴|30|1000");
		list.get("카리프의 고급 주머니(다이아몬드)").add("아시타지오의 재|1|300");
		list.get("카리프의 고급 주머니(다이아몬드)").add("지룡 비늘|1|5");
		list.get("카리프의 고급 주머니(다이아몬드)").add("철괴|3|1000");
		list.get("카리프의 고급 주머니(다이아몬드)").add("키메라의 가죽:뱀|1|1000");
		list.get("카리프의 고급 주머니(다이아몬드)").add("황금 원석 조각|2|50");
		list.get("카리프의 고급 주머니(다이아몬드)").add("마력의 돌|2|1000");
		list.get("카리프의 고급 주머니(다이아몬드)").add("정력옥|3|1000");
		list.get("카리프의 고급 주머니(다이아몬드)").add("마프르의 유산|1|250");
		
		
		/////// 뒤에 아이템이름은 성환씨 서버에  맡게 변경 ( 첫번째가 수량 두번째가 축,일반,저주  , 세번째가 인첸 , 네번째가 찬스)
		list.get("아인하사드의 선물").add("전투 강화의 주문서|1|1|0|370");
		list.get("아인하사드의 선물").add("전투 강화의 주문서|1|1|0|150");
		list.get("아인하사드의 선물").add("드래곤의 자수정|1|1|0|310");
		list.get("아인하사드의 선물").add("고대의 주문서|1|1|0|350");
		list.get("아인하사드의 선물").add("무기 마법 주문서|2|1|0|500");
		list.get("아인하사드의 선물").add("갑옷 마법 주문서|2|1|0|480");
		list.get("아인하사드의 선물").add("무기 주문서 상자|3|0|0|320");
		list.get("아인하사드의 선물").add("갑옷 주문서 상자|3|0|0|310");
		list.get("아인하사드의 선물").add("커츠의 부츠|1|1|0|400");
		list.get("아인하사드의 선물").add("커츠의 장갑|1|1|0|390");
		list.get("아인하사드의 선물").add("커츠의 투구|1|1|0|380");
		list.get("아인하사드의 선물").add("커츠의 갑옷|1|1|0|370");
		list.get("아인하사드의 선물").add("커츠의 부츠|1|0|0|360");
		list.get("아인하사드의 선물").add("커츠의 장갑|1|0|0|350");
		list.get("아인하사드의 선물").add("커츠의 투구|1|0|0|340");
		list.get("아인하사드의 선물").add("커츠의 갑옷|1|0|0|330");
		list.get("아인하사드의 선물").add("거대 여왕 개미의 금빛 날개|1|1|0|3");
		list.get("아인하사드의 선물").add("뱀파이어의 망토|1|1|0|180");
		list.get("아인하사드의 선물").add("시어의 심안|1|1|0|80");
		list.get("아인하사드의 선물").add("데스나이트의 장갑|1|1|0|300");
		list.get("아인하사드의 선물").add("데스나이트의 부츠|1|1|0|295");
		list.get("아인하사드의 선물").add("파워 글로브|1|1|0|385");
		list.get("아인하사드의 선물").add("고대 투사의 가더|1|1|0|2");  
		list.get("아인하사드의 선물").add("고대 명궁의 가더|1|1|0|2");
		list.get("아인하사드의 선물").add("마법 망토|1|1|6|170");
		list.get("아인하사드의 선물").add("마법 망토|1|1|5|270");
		list.get("아인하사드의 선물").add("마법 망토|1|1|4|300");
		list.get("아인하사드의 선물").add("마법 방어 투구|1|0|6|200");
		list.get("아인하사드의 선물").add("마법 방어 투구|1|0|5|260");
		list.get("아인하사드의 선물").add("마법 방어 투구|1|0|4|290");
		list.get("아인하사드의 선물").add("커츠의 검|1|1|0|10");
		list.get("아인하사드의 선물").add("커츠의 검|1|0|0|3");
		list.get("아인하사드의 선물").add("마법서 (미티어 스트라이크)|1|1|0|1");
		list.get("아인하사드의 선물").add("정령의 수정 (어스 바인드)|1|1|0|1");
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		int i = 100;
		while(true) {
			if (i-- < 0) {
				break;
			}
			// chance_max
			for(String field : list.get(getItem().getName())) {
				StringTokenizer st = new StringTokenizer(field, "|");
				String name = st.nextToken();
				Integer count = Integer.valueOf(st.nextToken());
				Integer bress =Integer.valueOf(st.nextToken());		
				Integer enchant = Integer.valueOf(st.nextToken());
				Integer chance = Integer.valueOf(st.nextToken());
				if(Util.random(0, chance_max) <= Util.random(0, chance)) {
					// 메모리 생성 및 초기화.
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find(name));
					ii.setCount( count );
					ii.setBress( bress );
					ii.setEnLevel( enchant );
					cha.getInventory().append(ii, ii.getCount());
					// \f1%0%s 당신에게 %1%o 주었습니다.
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 143, getName(), ii.toString()));
					// 메모리 재사용.
					ItemDatabase.setPool(ii);
					// 수량 하향.
					cha.getInventory().count(this, getCount()-1, true);
					return;
				}
			}
		}
	}

}
