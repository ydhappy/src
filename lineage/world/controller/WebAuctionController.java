package lineage.world.controller;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import lineage.bean.database.Item;
import lineage.bean.database.Warehouse;
import lineage.bean.database.WebAuction;
import lineage.database.AccountDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.database.WarehouseDatabase;
import lineage.database.WebAuctionDatabase;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.object.instance.ItemInstance;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class WebAuctionController {
	
	private static Object sync;
	private static Float tax = 0.1F;
	
	public static void init() {
		TimeLine.start("WebAuctionController..");
		
		//
		sync = new Object();
		
		TimeLine.end();
	}
	
	/**
	 * 주기적으로 호출됨.
	 * @param time
	 */
	public static void toTimer(long time) {
		synchronized (sync) {
			// 경매중이고 시간이 오바된거 추출.
			List<WebAuction> list = WebAuctionDatabase.getListEnd(time);
			// 입찰 확인.
			for(WebAuction wa : list) {
				Item item = ItemDatabase.find(wa.getName());
				if(item == null)
					continue;
				// 판매처리가 완료된걸로 변경.
				WebAuctionDatabase.updateIsSell(wa.getUid(), true);
				//
				WebAuction bidding = WebAuctionDatabase.getListBiddingAuction(wa.getUid());
				if(bidding == null) {
					// 미판매
					WebAuctionDatabase.updateNote(wa.getUid(), "미판매");
					// warehouse 등록 (아이템)
					if(WarehouseDatabase.isPiles(item.isPiles(), wa.getAccountUid(), wa.getName(), wa.getBress(), 0) > 0)
						WarehouseDatabase.update(wa.getName(), wa.getBress(), wa.getAccountUid(), wa.getCount(), 0);
					else
						WarehouseDatabase.insert(wa, wa.getAccountUid());
				} else {
					// 판매 완료
					WebAuctionDatabase.updateNote(wa.getUid(), "판매 완료");
					// web_auction_log 에 등록 note 값을 '구입 완료'로 변경.
					WebAuctionDatabase.updateBiddingNote(bidding.getUid(), "구입 완료");
					// warehouse 등록 (아이템)
					WarehouseDatabase.insert(wa, bidding.getAccountUid());
					// warehouse 등록 (아데나)
					ItemInstance aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
					if(WarehouseDatabase.isPiles(aden.getItem().isPiles(), wa.getAccountUid(), aden.getItem().getName(), aden.getBress(), 0) > 0)
						WarehouseDatabase.update(aden.getItem().getName(), aden.getBress(), wa.getAccountUid(), bidding.getBiddingPrice(), 0);
					else
						WarehouseDatabase.insert(aden, ServerDatabase.nextItemObjId(), bidding.getBiddingPrice(), wa.getAccountUid(), 0);
					ItemDatabase.setPool(aden);
				}
				WebAuctionDatabase.setPool(bidding);
			}
			WebAuctionDatabase.setPool(list);
		}
		
	}

	@SuppressWarnings("unchecked")
	public static String toJavaScript(Map<String, List<String>> params) {
		synchronized (sync) {
			JSONObject obj = new JSONObject();
			//
			try {
				String id = params.get("id").get(0);
				String pw = params.get("pw").get(0);
				String type = params.get("type").get(0);
				String data = params.get("data").get(0);
				

				// 계정 확인.
				int account_uid = AccountDatabase.getUid(id);
				if(AccountDatabase.isAccount(account_uid, id, pw)) {
					//
					if(type.equalsIgnoreCase("search")) {
						toSearch(obj, data);
						
					} else if(type.equalsIgnoreCase("search_more")) {
						toSearch(obj, data);
						
					}  else if(type.equalsIgnoreCase("sell")) {
						toSell(obj, account_uid);
						
					} else if(type.equalsIgnoreCase("status")) {
						toStatus(obj, account_uid);
						
					} else if(type.equalsIgnoreCase("result")) {
						toResult(obj, account_uid, data);
						
					} else if(type.equalsIgnoreCase("result_more")) {
						toResult(obj, account_uid, data);
						
					} else if(type.equalsIgnoreCase("sell_submit")) {
						toSellSubmit(obj, account_uid, data);
						
					} else if(type.equalsIgnoreCase("bidding")) {
						toBidding(obj, account_uid, data);
						
					} else if(type.equalsIgnoreCase("bidding_direct")) {
						toBiddingDirect(obj, account_uid, data);
					}
					//
					if(obj.containsKey("aden")==false && obj.containsValue("error")==false) {
						Warehouse wh = WarehouseDatabase.getAden(account_uid, 0);
						obj.put("aden", wh==null ? 0 : wh.getCount());
						WarehouseDatabase.setPool(wh);
					}
					obj.put("account_uid", account_uid);
				}  else {
					obj.put("action", "error");
					obj.put("message", "계정 정보가 잘못되었거나 존재하지 않습니다.");
				}
			} catch (Exception e) {
				obj.clear();
				obj.put("action", "error");
				obj.put("message", "정상적인 접근이 아닙니다.");
			}
			//
			StringBuffer sb = new StringBuffer();
			sb.append("var auction=").append( obj.toJSONString() ).append(";");
			obj.clear();
			obj = null;
			return sb.toString();
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void toSearch(JSONObject obj, String data) {
		//
		String type = null;
		String bress = null;
		String en = null;
		StringBuffer where = new StringBuffer();
		//
		StringTokenizer st = new StringTokenizer(data, "|");
		int item_uid = st.hasMoreTokens() ? Integer.valueOf(st.nextToken()) : 0;			// 검색중 더보기 눌럿을때 그 위치를 파악하기 위해 사용됨.
		if(st.hasMoreTokens())
			type = st.nextToken().replaceAll("\\+", "").replaceAll("└", "").replaceAll("·", "").replaceAll(" ", "").trim();
		if(st.hasMoreTokens())
			bress = st.nextToken().replaceAll("\\+", "").replaceAll("└", "").replaceAll("·", "").replaceAll(" ", "").trim();
		if(st.hasMoreTokens())
			en = st.nextToken().replaceAll("\\+", "").replaceAll("└", "").replaceAll("·", "").replaceAll(" ", "").trim();
		//
		if(type!=null && (type.startsWith("--") || type.startsWith("전체") || type.contains("undefined")))
			type = null;
		if(bress!=null && (bress.startsWith("--") || bress.startsWith("전체") || bress.contains("undefined")))
			bress = null;
		if(en!=null && (en.startsWith("--") || en.startsWith("전체") || en.contains("undefined") || en.indexOf("전체")>0))
			en = null;
		//
		if(type != null) {
			type = type.trim();
			if(type.equalsIgnoreCase("무기"))
				where.append(" AND 구분1='weapon'");
				if(type.equalsIgnoreCase("단검"))
					where.append(" AND 구분1='weapon' AND 구분2='dagger'");
				if(type.equalsIgnoreCase("한손검"))
					where.append(" AND 구분1='weapon' AND 구분2='sword'");
				if(type.equalsIgnoreCase("양손검"))
					where.append(" AND 구분1='weapon' AND 구분2='tohandsword'");
				if(type.equalsIgnoreCase("지팡이"))
					where.append(" AND 구분1='weapon' AND 구분2='wand'");
				if(type.equalsIgnoreCase("활"))
					where.append(" AND 구분1='weapon' AND 구분2='bow'");
				if(type.equalsIgnoreCase("창"))
					where.append(" AND 구분1='weapon' AND 구분2='spear'");
				if(type.equalsIgnoreCase("도끼"))
					where.append(" AND 구분1='weapon' AND 구분2='axe'");
//				if(type.equalsIgnoreCase("기타"))
//					where.append(" AND 구분1='weapon' AND (구분2='etc' OR 구분2='blunt')");
			if(type.equalsIgnoreCase("방어구"))
				where.append(" AND 구분1='armor'");
				if(type.equalsIgnoreCase("갑옷"))
					where.append(" AND 구분1='armor' AND 구분2='armor'");
				if(type.equalsIgnoreCase("벨트"))
					where.append(" AND 구분1='armor' AND 구분2='belt'");
				if(type.equalsIgnoreCase("부츠"))
					where.append(" AND 구분1='armor' AND 구분2='boot'");
				if(type.equalsIgnoreCase("망토"))
					where.append(" AND 구분1='armor' AND 구분2='cloak'");
				if(type.equalsIgnoreCase("장갑"))
					where.append(" AND 구분1='armor' AND 구분2='glove'");
				if(type.equalsIgnoreCase("투구"))
					where.append(" AND 구분1='armor' AND 구분2='helm'");
				if(type.equalsIgnoreCase("목걸이"))
					where.append(" AND 구분1='armor' AND 구분2='necklace'");
				if(type.equalsIgnoreCase("반지"))
					where.append(" AND 구분1='armor' AND 구분2='ring'");
				if(type.equalsIgnoreCase("방패"))
					where.append(" AND 구분1='armor' AND 구분2='shield'");
				if(type.equalsIgnoreCase("티셔츠"))
					where.append(" AND 구분1='armor' AND 구분2='t'");
			if(type.equalsIgnoreCase("마법서"))
				where.append(" AND 구분1='book'");
			if(type.equalsIgnoreCase("수정"))
				where.append(" AND 구분1='crystal'");
			if(type.equalsIgnoreCase("기타"))
				where.append(" AND 구분1='item'");
		}
		if(bress != null) {
			bress = bress.trim();
			if(bress.equalsIgnoreCase("확인"))
				where.append(" AND definite='1'");
				if(bress.equalsIgnoreCase("저주(확인)"))
					where.append(" AND definite='1' AND bress=2");
				if(bress.equalsIgnoreCase("일반(확인)"))
					where.append(" AND definite='1' AND bress=1");
				if(bress.equalsIgnoreCase("축(확인)"))
					where.append(" AND definite='1' AND bress=0");
			if(bress.equalsIgnoreCase("미확인"))
				where.append(" AND definite='0'");
				if(bress.equalsIgnoreCase("저주(미확인)"))
					where.append(" AND definite='0' AND bress=2");
				if(bress.equalsIgnoreCase("일반(미확인)"))
					where.append(" AND definite='0' AND bress=1");
				if(bress.equalsIgnoreCase("축(미확인)"))
					where.append(" AND definite='0' AND bress=0");
		}
		if(en != null) {
			en = en.trim();
			if(en.equalsIgnoreCase("-11이하"))
				where.append(" AND en<=-11");
			else if(en.equalsIgnoreCase("11이상"))
				where.append(" AND en>=11");
			else
				where.append(" AND en=").append(en);
		}
		//
		List<WebAuction> list = WebAuctionDatabase.getListOnSale(item_uid, 10, where.toString());
		JSONArray search = new JSONArray();
		for(WebAuction wa : list) {
			//
			StringBuffer remaining = new StringBuffer();
			long remaining_time = wa.getWebEndDate() - System.currentTimeMillis();
			long day = remaining_time / 1000 / 60 / 60 / 24;
			long hour = (remaining_time - (day*1000*60*60*24)) / 1000 / 60 / 60;
			long min = (remaining_time - (hour*1000*60*60) - (day*1000*60*60*24)) / 1000 / 60;
			if(day > 0)
				remaining.append(day).append("일 ");
			if(hour > 0)
				remaining.append(hour).append("시간 ");
			if(day<=0 && min>0)
				remaining.append(min).append("분 ");
			//
			Item item = ItemDatabase.find( wa.getName() );
			JSONObject w = new JSONObject();
			w.put("uid", wa.getUid());
			w.put("account_uid", wa.getAccountUid());
			w.put("name", wa.getName());
			w.put("gfxId", wa.getGfxid());
			w.put("count", wa.getCount());
			w.put("en", wa.getEn());
			w.put("bress", wa.getBress());
			w.put("price_s", wa.getWebPriceS());
			w.put("price_d", wa.getWebPriceD());
			w.put("insert_time", wa.getInsertDate());
			w.put("remaining_time", remaining.toString());
			w.put("type1", wa.get구분1());
			w.put("type2", wa.get구분2());
			w.put("dmgMin", item.getDmgMin());
			w.put("dmgMax", item.getDmgMax());
			w.put("royal", item.getRoyal());
			w.put("knight", item.getKnight());
			w.put("elf", item.getElf());
			w.put("wizard", item.getWizard());
			w.put("darkelf", item.getDarkElf());
			w.put("dragonknight", item.getDragonKnight());
			w.put("blackwizard", item.getBlackWizard());
			//
			WebAuction bidding = WebAuctionDatabase.getListBiddingAuction(wa.getUid());
			if(bidding != null) {
				w.put("bidding", bidding.getBiddingPrice());
				WebAuctionDatabase.setPool(bidding);
			}
			//
			search.add(w);
		}
		WebAuctionDatabase.setPool(list);
		obj.put("search", search);
	}
	
	@SuppressWarnings("unchecked")
	private static void toSell(JSONObject obj, int account_uid) {
		// 창고 목록 추출.
		List<Warehouse> list = WarehouseDatabase.getList(account_uid, 0);
		// 정리 및 메모리 정리.
		JSONArray warehouse = new JSONArray();
		for(Warehouse wh : list) {
			//
			Item item = ItemDatabase.find( wh.getName() );
			JSONObject w = new JSONObject();
			w.put("uid", wh.getUid());
			w.put("invId", wh.getInvId());
			w.put("name", wh.getName());
			w.put("type", wh.getType());
			w.put("gfxId", wh.getGfxid());
			w.put("count", wh.getCount());
			w.put("quantity", wh.getQuantity());
			w.put("en", wh.getEn());
			w.put("bress", wh.getBress());
			w.put("durability", wh.getDurability());
			w.put("time", wh.getTime());
			w.put("dmgMin", item.getDmgMin());
			w.put("dmgMax", item.getDmgMax());
			w.put("royal", item.getRoyal());
			w.put("knight", item.getKnight());
			w.put("elf", item.getElf());
			w.put("wizard", item.getWizard());
			w.put("darkelf", item.getDarkElf());
			w.put("dragonknight", item.getDragonKnight());
			w.put("blackwizard", item.getBlackWizard());
			w.put("type1", item.getType1());
			w.put("type2", item.getType2());
			w.put("ac", item.getAc());
			warehouse.add(w);
		}
		WarehouseDatabase.setPool(list);
		//
		obj.put("warehouse", warehouse);
	}
	
	@SuppressWarnings("unchecked")
	private static void toStatus(JSONObject obj, int account_uid) {
		//
		JSONArray onsale = new JSONArray();
		JSONArray bidding = new JSONArray();
		// 판매 중인거 추출.
		List<WebAuction> list = WebAuctionDatabase.getListOnSale(account_uid);
		for(WebAuction wa : list) {
			//
			WebAuction user_bidding = WebAuctionDatabase.getListBiddingAuction(wa.getUid());	// 입찰자 정보 추출.
			StringBuffer remaining = new StringBuffer();
			long remaining_time = wa.getWebEndDate() - System.currentTimeMillis();
			long day = remaining_time / 1000 / 60 / 60 / 24;
			long hour = (remaining_time - (day*1000*60*60*24)) / 1000 / 60 / 60;
			long min = (remaining_time - (hour*1000*60*60) - (day*1000*60*60*24)) / 1000 / 60;
			if(day > 0)
				remaining.append(day).append("일 ");
			if(hour > 0)
				remaining.append(hour).append("시간 ");
			if(day<=0 && min>0)
				remaining.append(min).append("분 ");
			//
			JSONObject w = new JSONObject();
			w.put("name", wa.getName());
			w.put("gfxId", wa.getGfxid());
			w.put("count", wa.getCount());
			w.put("en", wa.getEn());
			w.put("bress", wa.getBress());
			w.put("price_s", wa.getWebPriceS());
			w.put("price_d", wa.getWebPriceD());
			w.put("insert_time", wa.getInsertDate());
			w.put("remaining_time", remaining.toString());
			w.put("bidding", user_bidding==null ? "입찰 없음" : user_bidding.getBiddingPrice());
			onsale.add(w);
			
		}
		WebAuctionDatabase.setPool(list);
		// 입찰 중인거 추출.
		list = WebAuctionDatabase.getListBidding(account_uid);
		for(WebAuction wa : list) {
			// 경매물품을 못찾거나 낙찰 및 취소 된 물품은 패스.
			WebAuction auction = WebAuctionDatabase.getOnSale( wa.getAuctionUid() );
			if(auction==null || auction.isWebIsSell())
				continue;
			WebAuction top = WebAuctionDatabase.getListBiddingAuction( wa.getAuctionUid() );
			//
			StringBuffer remaining = new StringBuffer();
			long remaining_time = (wa.getInsertDate() + Lineage.auction_delay) - System.currentTimeMillis();
			long day = remaining_time / 1000 / 60 / 60 / 24;
			long hour = (remaining_time - (day*1000*60*60*24)) / 1000 / 60 / 60;
			long min = (remaining_time - (hour*1000*60*60) - (day*1000*60*60*24)) / 1000 / 60;
			if(day > 0)
				remaining.append(day).append("일 ");
			if(hour > 0)
				remaining.append(hour).append("시간 ");
			if(day<=0 && min>0)
				remaining.append(min).append("분 ");
			//
			JSONObject w = new JSONObject();
			w.put("name", auction.getName());
			w.put("gfxId", auction.getGfxid());
			w.put("count", auction.getCount());
			w.put("en", auction.getEn());
			w.put("bress", auction.getBress());
			w.put("price_s", auction.getWebPriceS());
			w.put("price_d", auction.getWebPriceD());
			w.put("insert_date", auction.getInsertDate());
			w.put("remaining_time", remaining.toString());
			w.put("bidding", wa.getBiddingPrice());							// 나의 입찰가
			w.put("bidding_top", top.getBiddingPrice());					// 최고 입찰가
			w.put("status", wa.getUid()==top.getUid() ? "최고입찰" : wa.getNote());		// 최고입찰, 유찰
			bidding.add(w);
		}
		WebAuctionDatabase.setPool(list);
		//
		obj.put("on_sale", onsale);
		obj.put("bidding", bidding);
	}

	@SuppressWarnings("unchecked")
	private static void toResult(JSONObject obj, int account_uid, String data) {
		JSONArray array = new JSONArray();
		int uid1 = 0;
		int uid2 = 0;
		if(data!=null && data.length()>0) {
			StringTokenizer st = new StringTokenizer(data, "|");
			uid1 = Integer.valueOf(st.nextToken());
			uid2 = Integer.valueOf(st.nextToken());
			if(uid1 <= 0)
				uid1 = -1;
			if(uid2 <= 0)
				uid2 = -1;
		}
		// 처음 목록을 추출할때만 total_cnt값 추출.
		int total_cnt = uid1==0 && uid2==0 ? WebAuctionDatabase.getCountResult(account_uid) : 0;
		//
		List<WebAuction> list = WebAuctionDatabase.getListResult(account_uid, uid1, uid2, 5);
		int log1_uid = 0;
		int log2_uid = 0;
		for(WebAuction wa : list) {
			JSONObject w = new JSONObject();
			if(wa.getAuctionUid() == 0) {
				w.put("uid", wa.getUid());
				w.put("name", wa.getName());
				w.put("en", wa.getEn());
				w.put("bress", wa.getBress());
				w.put("insert_date", Util.getLocaleString(wa.getInsertDate(), false));
				w.put("price", "-");
				log1_uid = log1_uid==0 || log1_uid>wa.getUid() ? wa.getUid() : log1_uid;
			} else {
				WebAuction auction = WebAuctionDatabase.getOnSale(wa.getAuctionUid());
				w.put("uid", auction.getUid());
				w.put("name", auction.getName());
				w.put("en", auction.getEn());
				w.put("bress", auction.getBress());
				w.put("insert_date", Util.getLocaleString(wa.getInsertDate(), false));
				w.put("price", wa.getBiddingPrice());
				WebAuctionDatabase.setPool(auction);
				log2_uid = log2_uid==0 || log2_uid>wa.getUid() ? wa.getUid() : log2_uid;
			}
			w.put("note", wa.getNote());
			array.add(w);
		}
		WebAuctionDatabase.setPool(list);
		//
		obj.put("result_total", total_cnt);
		obj.put("result", array);
		obj.put("result_uid1", log1_uid);	// web_auction 필드에 uid 최상값
		obj.put("result_uid2", log2_uid);	// web_auction_log 필드에 uid 최상값
	}

	@SuppressWarnings("unchecked")
	private static void toSellSubmit(JSONObject obj, int account_uid, String data) {
		StringTokenizer st = new StringTokenizer(data, "|");
		int uid = Integer.valueOf(st.nextToken());
		long price_s = Long.valueOf(st.nextToken());
		long price_d = Long.valueOf(st.nextToken());
		if(price_s > 0) {
			if(price_d < 0)
				price_d = 0;
			if(price_d==0 || price_s<price_d) {
				// 등록가능한 갯수 확인.
				if(WebAuctionDatabase.getCount(account_uid) < 10) {
					// 창고에서 정보 추출.
					Warehouse wh = WarehouseDatabase.find(uid, 0);
					if(wh != null) {
						// 창고 목록에서 제거.
						WarehouseDatabase.delete(uid, 0);
						// 경매 테이블에 등록.
						WebAuctionDatabase.insert(account_uid, wh, price_s, price_d);
						//
						WarehouseDatabase.setPool(wh);
					} else {
						obj.put("action", "error");
						obj.put("message", "아이템 정보를 찾을 수 없습니다.");
					}
				} else {
					obj.put("action", "error");
					obj.put("message", "더이상 경매에 상품을 등록할 수 없습니다.\r\n최대 등록 가능 갯수는 10개 입니다.");
				}
			} else {
				obj.put("action", "error");
				obj.put("message", "즉시구입가 가격이 잘 못 되었습니다.");
			}
		} else {
			obj.put("action", "error");
			obj.put("message", "시작가 가격이 잘 못 되었습니다.");
		}
	}

	@SuppressWarnings("unchecked")
	private static void toBidding(JSONObject obj, int account_uid, String data) {
		//
		StringTokenizer st = new StringTokenizer(data, "|");
		int uid = Integer.valueOf(st.nextToken());
		long bidding_price = Long.valueOf(st.nextToken());
		WebAuction wa = WebAuctionDatabase.getOnSale(uid);
		int min_bidding_price = wa.getWebPriceS();
		WebAuction bidding = WebAuctionDatabase.getListBiddingAuction(wa.getUid());
		if(bidding != null)
			min_bidding_price = bidding.getBiddingPrice();
		min_bidding_price = min_bidding_price + (int)(min_bidding_price*tax);
		Warehouse aden = WarehouseDatabase.getAden(account_uid, 0);
		//
		if(wa.getAccountUid() == account_uid) {
			obj.put("action", "error");
			obj.put("message", "자신에 경매물품을 입찰할 수 없습니다.");
			return;
		}
		if(bidding_price <= 0) {
			obj.put("action", "error");
			obj.put("message", "입찰가를 입력하여 주십시오.");
			return;
		}
		if(bidding_price < min_bidding_price) {
			obj.put("action", "error");
			obj.put("message", "입찰가 가격이 너무 낮습니다.");
			return;
		}
		if(aden==null || aden.getCount() < bidding_price) {
			obj.put("action", "error");
			obj.put("message", "보유한 아데나가 부족 합니다.");
			return;
		}
		// 아데나 제거.
		ItemInstance db_aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
		WarehouseDatabase.update(db_aden.getItem().getName(), db_aden.getBress(), account_uid, bidding_price-(bidding_price*2), 0);
		ItemDatabase.setPool(db_aden);
		// 디비 등록.
		WebAuctionDatabase.insertBidding(uid, account_uid, bidding_price, "");
		if(bidding != null) {
			// 상태글 변경.
			WebAuctionDatabase.updateBiddingNote(bidding.getUid(), "유찰");
			// 아데나 환급.
			db_aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
			WarehouseDatabase.update(db_aden.getItem().getName(), db_aden.getBress(), bidding.getAccountUid(), bidding.getBiddingPrice(), 0);
			ItemDatabase.setPool(db_aden);
		}
		// 메모리 정리.
		WebAuctionDatabase.setPool(wa);
		WarehouseDatabase.setPool(aden);
		if(bidding != null)
			WebAuctionDatabase.setPool(bidding);
	}

	@SuppressWarnings("unchecked")
	private static void toBiddingDirect(JSONObject obj, int account_uid, String data) {
		//
		StringTokenizer st = new StringTokenizer(data, "|");
		int uid = Integer.valueOf(st.nextToken());
		WebAuction wa = WebAuctionDatabase.getOnSale(uid);
		Warehouse aden = WarehouseDatabase.getAden(account_uid, 0);
		//
		if(wa.getAccountUid() == account_uid) {
			obj.put("action", "error");
			obj.put("message", "자신에 경매물품을 구입할 수 없습니다.");
			return;
		}
		if(wa.getWebPriceD() <= 0) {
			obj.put("action", "error");
			obj.put("message", "즉시구입할 수 없는 물품 입니다.");
			return;
		}
		if(aden==null || aden.getCount()<wa.getWebPriceD()) {
			obj.put("action", "error");
			obj.put("message", "보유한 아데나가 부족 합니다.");
			return;
		}
		// 판매처리가 완료된걸로 변경.
		WebAuctionDatabase.updateIsSell(wa.getUid(), true);
		// 판매 완료
		WebAuctionDatabase.updateNote(wa.getUid(), "판매 완료 <span>- 즉시 구입</span>");
		// web_auction_log 에 등록 note 값을 '구입 완료'로 변경.
		WebAuctionDatabase.insertBidding(uid, account_uid, wa.getWebPriceD(), "구입 완료 <span>- 즉시 구입</span>");
		// 구입자 아데나 제거.
		ItemInstance db_aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
		WarehouseDatabase.update(db_aden.getItem().getName(), db_aden.getBress(), account_uid, wa.getWebPriceD()-(wa.getWebPriceD()*2), 0);
		ItemDatabase.setPool(db_aden);
		// 판매자에게 아데나 지급.
		db_aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
		if(WarehouseDatabase.isPiles(db_aden.getItem().isPiles(), wa.getAccountUid(), db_aden.getItem().getName(), db_aden.getBress(), 0) > 0)
			WarehouseDatabase.update(db_aden.getItem().getName(), db_aden.getBress(), wa.getAccountUid(), wa.getWebPriceD(), 0);
		else
			WarehouseDatabase.insert(db_aden, ServerDatabase.nextItemObjId(), wa.getWebPriceD(), wa.getAccountUid(), 0);
		ItemDatabase.setPool(db_aden);
		// 구입자에게 아이템 지급.
		WarehouseDatabase.insert(wa, account_uid);
		// 기존 입찰자에게 아데나 환급.
		WebAuction bidding = WebAuctionDatabase.getListBiddingAuction(wa.getUid());
		if(bidding != null) {
			// 상태글 변경.
			WebAuctionDatabase.updateBiddingNote(bidding.getUid(), "유찰 <span>- 즉시 구입</span>");
			// 아데나 환급.
			db_aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
			WarehouseDatabase.update(db_aden.getItem().getName(), db_aden.getBress(), bidding.getAccountUid(), bidding.getBiddingPrice(), 0);
			ItemDatabase.setPool(db_aden);
		}
		// 메모리 정리.
		WarehouseDatabase.setPool(aden);
		WebAuctionDatabase.setPool(wa);
		if(bidding != null)
			WebAuctionDatabase.setPool(bidding);
	}
	
}
