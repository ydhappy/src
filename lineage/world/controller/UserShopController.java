package lineage.world.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.lineage.Useshop;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectGfx;
import lineage.network.packet.server.S_ObjectMode;
import lineage.network.packet.server.S_TradeZone;
import lineage.network.packet.server.S_UserShopList;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.Log;
import lineage.share.TimeLine;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.ShapeChange;
import lineage.world.object.robot.PcRobotShopInstance;

public final class UserShopController {

	static private List<Useshop> pool;
	static private Map<PcInstance, Useshop> list;
	static private Map<String, Integer> poly_list;
	
	static public void init(){
		TimeLine.start("UserShopController..");
		
		pool = new ArrayList<Useshop>();
		list = new HashMap<PcInstance, Useshop>();
		poly_list = new HashMap<String, Integer>();
		poly_list.put("tradezone1", 11479);
		poly_list.put("tradezone2", 11427);
		poly_list.put("tradezone3", 10047);
		poly_list.put("tradezone4", 9688);
		poly_list.put("tradezone5", 11322);
		poly_list.put("tradezone6", 10069);
		poly_list.put("tradezone7", 10034);
		poly_list.put("tradezone8", 10032);
		
		TimeLine.end();
	}
	
//	static final int aaaa = 0xa0;
	
	/**
	 * 개인상점 시작 처리 함수.
	 * @param pc
	 * @param cbp
	 */
	static public void toStart(PcInstance pc, ClientBasePacket cbp){
		// 상점 상태인데 다시 상점시작 요청을 할수 있으므로 그에 따른 버그 예방.
		toStop(pc);
		// 맵 확인.
		if(isMap(pc)){
			// 380 {
			// 	룬을 착용하셨다면 해제하시기 바랍니다.
			// 	직장인 경험치 아이템을 해제하세요.
			// 	변신 아이템을 해제하세요.
			// }
			
			Useshop us = getPool();
			// 판매 목록 추출.
			for(int i=cbp.readH() ; i>0 ; --i){
				ItemInstance item = pc.getInventory().value(cbp.readD());
				if(item!=null && !item.isEquipped() && item.getBress()>=0 && item.getBress()<128 && item.getItem().isTrade()) {
					item.setUsershopBuyPrice(cbp.readD());
					item.setUsershopBuyCount(cbp.readD());
					if(item.getUsershopBuyPrice()>0 && item.getUsershopBuyCount()>0 && item.getCount()>=item.getUsershopBuyCount())
						us.getBuy().add(item);
				} else {
					cbp.readD();
					cbp.readD();
				}
			}
			// 구입 목록 추출.
			for(int i=cbp.readH() ; i>0 ; --i){
				ItemInstance item = pc.getInventory().value(cbp.readD());
				if(item!=null && !item.isEquipped() && item.getBress()>=0 && item.getBress()<128 && item.getItem().isTrade()) {
					item.setUsershopSellPrice(cbp.readD());
					item.setUsershopSellCount(cbp.readD());
					if(item.getUsershopSellPrice()>0 && item.getUsershopSellCount()>0)
						us.getSell().add(item);
				} else {
					cbp.readD();
					cbp.readD();
				}
			}
			// 아이템등록됫는지 체크
			if(us.getBuy().size()>0 || us.getSell().size()>0){
				// 착용중인 아이템 해제.
				if(pc.getInventory().getSlot(8) != null)
					pc.getInventory().getSlot(8).toClick(pc, null);
				// 변신상태 해제.
				BuffController.remove(pc, ShapeChange.class);
				// 처리.
				pc.setGfxMode(70);
				if(Lineage.server_version >= 380) {
					int temp = cbp.readC();
					pc.setGfx( poly_list.get(cbp.readS()) );
					
					pc.toSender(S_TradeZone.clone(BasePacketPooling.getPool(S_TradeZone.class), 1));
					pc.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), pc, us, temp), true);
					pc.toSender(S_ObjectGfx.clone(BasePacketPooling.getPool(S_ObjectGfx.class), pc), true);
					pc.toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), pc), true);
				} else {
					us.setMsg(cbp.readB());
					pc.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), pc, 3), true);
					pc.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), pc, us, 0), true);
				}
				
				synchronized (list) {
					if(!list.containsKey(pc))
						list.put(pc, us);
				}
				
				//
				PluginController.init(UserShopController.class, "toStart.end", pc);
				
			}else{
				// 개인 상점에 등록된 아이템이 없습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 908));
				setPool(us);
			}
		}else{
			// 876 : 이 곳에서는 개인 상점을 열 수 없습니다.
			// 3405 : 상점 개설이 불가한 곳입니다. 바닥을 확인해 주세요.
			ChattingController.toChatting(pc, "\\fY.상점으로 이용해 주시기 바랍니다.", Lineage.CHATTING_MODE_MESSAGE);
			//pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), Lineage.server_version>=380 ? 3405 : 876));
		}
	}
	
	/**
	 * 개인상점 종료 처리 함수.
	 * @param pc
	 */
	static public void toStop(PcInstance pc){
		Useshop us = find(pc);
		if(us != null){
			try {
				// 초기화 및 풀에 재등록.
				setPool(us);
				synchronized (list) {
					list.remove(pc);
				}
				// 상태 변경
				pc.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), pc, 3), true);
				pc.setGfxModeClear();
				if(Lineage.server_version >= 380) {
					pc.setGfx(pc.getClassGfx());
					pc.toSender(S_ObjectGfx.clone(BasePacketPooling.getPool(S_ObjectGfx.class), pc), true);
				}
				// 패킷 처리
			} catch (Exception e) {
				lineage.share.System.println(UserShopController.class+" : toStop(PcInstance pc)");
				lineage.share.System.println(e);
			}
		}
	}
	
	/**
	 * 개인상점 buy 및 sell 목록 보기 함수.
	 * @param pc
	 * @param use
	 * @param buy
	 */
	static public void toList(PcInstance pc, PcInstance use, boolean buy) {
		//
		Useshop us = use instanceof PcRobotShopInstance ? ((PcRobotShopInstance)use).getUseShop() : find(use);
		if(us != null)
			pc.toSender(S_UserShopList.clone(BasePacketPooling.getPool(S_UserShopList.class), use, pc, us, buy));
	}
	
	static public Useshop find(PcInstance pc){
		synchronized (list) {
			return list.get(pc);
		}
	}
	
	/**
	 * 해당 객체가 시장맵에 있는지 확인해주는 함수.
	 * @param o
	 * @return
	 */
	static public boolean isMap(object o) {
		//
		Object r = PluginController.init(UserShopController.class, "isMap", o);
		if(r!=null && r instanceof Boolean)
			return (Boolean)r;
		//
		return o.getMap()==8000;
	}
	
	static public Useshop getPool(){
		Useshop us = null;
		synchronized (pool) {
			if(Lineage.memory_recycle && pool.size()>0){
				us = pool.get(0);
				pool.remove(0);
			}else{
				us = new Useshop();
			}
		}
		return us;
	}
	
	static public void setPool(Useshop us){
		us.close();
		if(!Lineage.memory_recycle)
			return;
		synchronized (pool) {
			if(!pool.contains(us))
				pool.add(us);
		}
	}
	
	static public int getPoolSize(){
		synchronized (pool) {
			return pool.size();
		}
	}

	/**
	 * 구입 처리.
	 * @param pc	: 상인
	 * @param use	: 구매요청자.
	 * @param cbp
	 */
	static public void toBuy(PcInstance pc, PcInstance use, ClientBasePacket cbp) {
		//
		Useshop us = pc instanceof PcRobotShopInstance ? ((PcRobotShopInstance)pc).getUseShop() : find(pc);
		if(us == null)
			return;
		//
		long count = cbp.readH();
		if(count>0 && us.getBuy().size()>=count) {
			for(int i=0 ; i<count ; ++i) {
				int item_idx = (int)cbp.readD();
				long item_count = cbp.readD();
				ItemInstance ii = us.getBuy().get(item_idx);
				if(ii!=null && ii.getUsershopBuyCount()>=item_count) {
					if(use.getInventory().isAppend(ii.getItem(), count, ii.getItem().isPiles()?1:item_count)) {
						if(use.getInventory().isAden("아데나", ii.getUsershopBuyPrice()*item_count, true)) {
							// 아데나 지급.
							if(!(pc instanceof PcRobotShopInstance)) {
								ItemInstance aden = pc.getInventory().findAden();
								if(aden == null){
									aden = ItemDatabase.newInstance( ItemDatabase.find("아데나") );
									aden.setObjectId(ServerDatabase.nextItemObjId());
									aden.setCount(0);
									pc.getInventory().append(aden, true);
								}
								pc.getInventory().count(aden, aden.getCount()+(ii.getUsershopBuyPrice()*item_count), true);
							}
							//
							ItemInstance temp = use.getInventory().find(ii.getItem().getName(), ii.getBress(), true);
							if(temp == null) {
								if(ii.getItem().isPiles()) {
									temp = ItemDatabase.newInstance(ii);
									temp.setObjectId(ServerDatabase.nextItemObjId());
									temp.setCount(item_count);
									use.getInventory().append(temp, true);
									//
									Log.appendItem(use, "type|개인상점구입", String.format("pc_name|%s", pc.getName()), String.format("use_name|%s", use.getName()), String.format("item_name|%s", temp.toStringDB()), String.format("item_objid|%d", temp.getObjectId()), String.format("count|%d", item_count), String.format("shop_count|%d", ii.getUsershopBuyCount()));
								} else {
									for(int k=0 ; k<item_count ; ++k){
										temp = ItemDatabase.newInstance(ii);
										temp.setObjectId(ServerDatabase.nextItemObjId());
										temp.setCount(1);
										use.getInventory().append(temp, true);
										//
										Log.appendItem(use, "type|개인상점구입", String.format("pc_name|%s", pc.getName()), String.format("use_name|%s", use.getName()), String.format("item_name|%s", temp.toStringDB()), String.format("item_objid|%d", temp.getObjectId()), String.format("count|%d", 1), String.format("shop_count|%d", ii.getUsershopBuyCount()));
									}
								}
							} else {
								// 겹치는 아이템이 존재할 경우.
								use.getInventory().count(temp, ii, temp.getCount()+item_count, true);
								Log.appendItem(use, "type|개인상점구입", String.format("pc_name|%s", pc.getName()), String.format("use_name|%s", use.getName()), String.format("item_name|%s", ii.getItem().getName()), String.format("target_name|%s", temp.toStringDB()), String.format("target_objid|%d", temp.getObjectId()), String.format("count|%d", item_count), String.format("shop_count|%d", ii.getUsershopBuyCount()));
							}
							// 안내 메세지.
							if(pc instanceof PcRobotShopInstance) {
								pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 877, use.getName(), ii.getName()));
								// 유저상점설정한 갯수 갱신.
								ii.setUsershopBuyCount(ii.getUsershopBuyCount() - item_count);
								// 설정한 갯수만큼 다 판매됫으면 목록에서 제거.
								if(ii.getUsershopBuyCount() <= 0)
									us.getBuy().remove(ii);
								ChattingController.toChatting(use, "[서버알림]"+ii.getName()+"("+item_count+")", Lineage.CHATTING_MODE_MESSAGE);
								// 인벤토리 갯수 갱신.
//								pc.getInventory().count(ii, ii.getCount()-item_count, true);
							}
						} else {
							// \f1아데나가 충분치 않습니다.
							use.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * 판매 처리.
	 * @param pc	: 상인
	 * @param use	: 판매요청자
	 * @param cbp	: 패킷 정보
	 */
	static public void toSell(PcInstance pc, PcInstance use, ClientBasePacket cbp) {
		//
		Useshop us = pc instanceof PcRobotShopInstance ? ((PcRobotShopInstance)pc).getUseShop() : find(pc);
		if(us == null)
			return;
		//
		long count = cbp.readH();
		if(count>0 && us.getSell().size()>=count){
			for(int i=0 ; i<count ; ++i) {
				int inv_id = (int)cbp.readD();
				long item_count = cbp.readH();
				cbp.readH();
				ItemInstance temp = use.getInventory().value(inv_id);
				if(temp!=null && !temp.isEquipped() && item_count>0 && temp.getCount()>=item_count) {
					ItemInstance sell_ii = us.find(temp, false);
					if(sell_ii != null) {
						if(pc instanceof PcRobotShopInstance || pc.getInventory().isAden("아데나", sell_ii.getUsershopSellPrice()*item_count, true)) {
							//
							String target_name = temp.toStringDB();
							long target_objid = temp.getObjectId();
							// 판매되는 아이템 제거.
							use.getInventory().count(temp, temp.getCount()-item_count, true);
							// 아덴 지급
							ItemInstance aden = use.getInventory().find("아데나", true);
							if(aden == null){
								aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
								aden.setObjectId(ServerDatabase.nextItemObjId());
								aden.setCount(0);
								use.getInventory().append(aden, true);
							}
							use.getInventory().count(aden, aden.getCount()+(sell_ii.getUsershopSellPrice()*item_count), true);
							// 상인에게 등록.
							if(!(pc instanceof PcRobotShopInstance)) {
								if(sell_ii.getItem().isPiles()) {
									pc.getInventory().count(sell_ii, sell_ii.getCount()+item_count, true);
								} else {
									for(int k=0 ; k<item_count ; ++k){
										ItemInstance temp2 = ItemDatabase.newInstance(sell_ii);
										temp2.setObjectId(ServerDatabase.nextItemObjId());
										temp2.setCount(1);
										pc.getInventory().append(temp2, true);
									}
								}
							}
							// 안내 메세지.
							use.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 877, pc.getName(), sell_ii.getName()));
							// 유저상점설정한 갯수 갱신.
							if(!(pc instanceof PcRobotShopInstance)) {
								sell_ii.setUsershopSellCount(sell_ii.getUsershopSellCount() - item_count);
								//
	//							Log.appendItem(pc, "type|상점판매금", "npc_name|"+getNpc().getName(), "aden_name|아데나", "aden_objid|"+aden_objid, "target_name|"+target_name, "target_objid|"+target_objid, "target_price|"+target_price, "item_count|"+item_count);
								Log.appendItem(pc, "type|상점판매", String.format("pc_name|%s", pc.getName()), String.format("use_name|%s", use.getName()), String.format("target_name|%s", target_name), String.format("target_objid|%d", target_objid), String.format("item_count|%d", item_count));
							}
						}
					}
				}
			}
		}
	}
	
}
