package sp.instance;

import java.sql.Connection;

import lineage.bean.database.Item;
import lineage.bean.database.Shop;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.share.Log;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.RobotInstance;
import sp.npc.낚싯대;

public class ShopInstance {

	static public void toDwarfAndShop(lineage.world.object.instance.ShopInstance si, PcInstance pc, ClientBasePacket cbp) {
		switch(cbp.readC()){
			case 0:	// 상점 구입
				toBuy(si, pc, cbp);
				break;
			case 1:	// 상점 판매
				toSell(si, pc, cbp);
				break;
		}
	}

	/**
	 * 상점 구매
	 */
	static private void toBuy(lineage.world.object.instance.ShopInstance si, PcInstance pc, ClientBasePacket cbp){
		long count = cbp.readH();
		if(count>0 && count<=100){
			for(int j=0 ; j<count ; ++j){
				long item_idx = cbp.readD();
				long item_count = cbp.readD();
				if(item_count>0 && item_count<=1000){
					Shop s = si.getNpc().findShop(item_idx);
					if(s != null){
						
						Item i = ItemDatabase.find(s.getItemName());
						if(si.getNpc().getNameId().equalsIgnoreCase("[낚싯대]")){
						//if(i.getName().equalsIgnoreCase("미끼")){
							item_count = 1;
							낚싯대.toBuySeccess(pc, i);
							System.out.println("낚시대샀지롱");
						}
						int shop_price = s.getPrice()!=0 ? si.getTaxPrice(s.getPrice(), false) : 0;
						// 아이템 갯수에 맞게 갯수 재 설정.
						long new_item_count = item_count * s.getItemCount();
						//
						if(pc.getInventory().isAppend(i, count, i.isPiles()?1:new_item_count)){

								//
								ItemInstance temp = pc.getInventory().find(s.getItemName(), s.getItemBress(), true);
								if(temp == null){
									// 겹칠수 있는 아이템이 존재하지 않을경우.
									if(i.isPiles()){
										temp = ItemDatabase.newInstance(i);
										temp.setObjectId(ServerDatabase.nextItemObjId());
										temp.setCount(new_item_count);
										temp.setEnLevel(s.getItemEnLevel());
										temp.setBress(s.getItemBress());
										temp.setNowTime(s.getItemTime());
										temp.setDefinite(true);
										pc.getInventory().append(temp, true);
										//낚싯대.toBuySeccess(pc, i);
										//
										Log.appendItem(pc, "type|상점구입", String.format("npc_name|%s", si.getNpc().getName()), String.format("item_name|%s", temp.toStringDB()), String.format("item_objid|%d", temp.getObjectId()), String.format("count|%d", item_count), String.format("shop_uid|%d", s.getUid()), String.format("shop_count|%d", s.getItemCount()));
									}else{
										for(int k=0 ; k<new_item_count ; ++k){
											temp = ItemDatabase.newInstance(i);
											temp.setObjectId(ServerDatabase.nextItemObjId());
											// 겜블 아이템은 겹칠일이 없어서 여기다가 넣음.
											if(s.isGamble()){
												temp.setEnLevel( getGambleEnLevel() );
											}else{
												temp.setEnLevel(s.getItemEnLevel());
												temp.setDefinite(true);
											}
											temp.setNowTime(s.getItemTime());
											temp.setBress(s.getItemBress());
											pc.getInventory().append(temp, true);
											낚싯대.toBuySeccess(pc, i);
											//
											Log.appendItem(pc, "type|상점구입", String.format("npc_name|%s", si.getNpc().getName()), String.format("item_name|%s", temp.toStringDB()), String.format("item_objid|%d", temp.getObjectId()), String.format("count|%d", item_count), String.format("shop_uid|%d", s.getUid()), String.format("shop_count|%d", s.getItemCount()));
										}
									}

								}else{
									// 겹치는 아이템이 존재할 경우.
									pc.getInventory().count(temp, temp.getCount()+new_item_count, true);
									낚싯대.toBuySeccess(pc, i);
									//
									Log.appendItem(pc, "type|상점구입", String.format("npc_name|%s", si.getNpc().getName()), String.format("item_name|%s", s.getItemName()), String.format("target_name|%s", temp.toStringDB()), String.format("target_objid|%d", temp.getObjectId()), String.format("count|%d", item_count), String.format("shop_uid|%d", s.getUid()), String.format("shop_count|%d", s.getItemCount()));

								}
								// 아데나일때만 처리.
								if(s.getAdenType().equalsIgnoreCase("아데나")) {
									// 로봇도 처리할지 여부.
									if(!Lineage.robot_auto_tax && pc instanceof RobotInstance)
										return;
									// 세금으로인한 차액을 공금에 추가.
									if(s.getPrice() != 0)
										si.addTax( (int)((shop_price-s.getPrice())*item_count) );
									else
										si.addTax( (int)((shop_price-i.getShopPrice())*item_count) );
								}
								
							}else{
								// \f1아데나가 충분치 않습니다.
//								pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
								ChattingController.toChatting(pc, String.format("'%s'가 충분치 않습니다.", s.getAdenType()), 20);
								break;
							}

						}
					}
				}
			}
		}
	

	/**
	 * 상점 판매
	 */
	static private void toSell(lineage.world.object.instance.ShopInstance si, PcInstance pc, ClientBasePacket cbp){
		Connection con = null;
		int count = (int) cbp.readH();
		if(count > 0){
			try {
				con = DatabaseConnection.getLineage();
				
				for(int i=0 ; i<count ; ++i){
					int inv_id = (int) cbp.readD();
					long item_count = cbp.readD();
					ItemInstance temp = pc.getInventory().value(inv_id);
					if(temp!=null && !temp.isEquipped() && item_count>0 && temp.getCount()>=item_count){
						//
						String target_name = temp.toStringDB();
						long target_objid = temp.getObjectId();
						long aden_objid = 0;
						//
						Shop s = si.getNpc().findShopItemId(temp.getItem().getName(), temp.getBress(), inv_id);
						// 판매될수 있는 아이템만 처리.
						if(s!=null && s.isItemSell()){
							// 가격 체크
							long target_price = si.getPrice(con, temp);
							// 아덴 지급
							if(target_price > 0){
								ItemInstance aden = pc.getInventory().find(s.getAdenType(), true);
								if(aden == null){
									aden = ItemDatabase.newInstance(ItemDatabase.find(s.getAdenType()));
									aden.setObjectId(ServerDatabase.nextItemObjId());
									aden.setCount(0);
									pc.getInventory().append(aden, true);
								}
								aden_objid = aden.getObjectId();
								//
								pc.getInventory().count(aden, aden.getCount()+(target_price*item_count), true);
								//
								Log.appendItem(pc, "type|상점판매금", "npc_name|"+si.getNpc().getName(), "aden_name|"+s.getAdenType(), "aden_objid|"+aden_objid, "target_name|"+target_name, "target_objid|"+target_objid, "target_price|"+target_price, "item_count|"+item_count);
								// 세금계산은 아데나일때만 처리.
								if(s.getAdenType().equalsIgnoreCase("아데나")){
									// 세금으로인한 차액을 공금에 추가.
									if(s.getPrice() != 0)
										si.addTax((int)((s.getPrice()*0.5)-target_price));
									else
										si.addTax((int)((temp.getItem().getShopPrice()*0.5)-target_price));
								}
							}
							// 판매되는 아이템 제거.
							pc.getInventory().count(temp, temp.getCount()-item_count, true);
							//
							Log.appendItem(pc, "type|상점판매", String.format("npc_name|%s", si.getNpc().getName()), String.format("target_name|%s", target_name), String.format("target_objid|%d", target_objid), String.format("target_price|%d", target_price), String.format("item_count|%d", item_count));
						}
					}
				}
			} catch (Exception e) {
			} finally {
				DatabaseConnection.close(con);
			}
		}
	}
	
	/**
	 * 겜블 상점에서 아이템 구매시 인첸트 값 추출해주는 함수.
	 * @return
	 */
	static private int getGambleEnLevel(){
		int a = Util.random(0, 100);
		int b = Util.random(0, 100);
		int c = 0;
		if((a<=80)&&(b<=80)){
			c = Util.random(3, 6);
		}else if((a<=95)&&(b<=95)){
			c = Util.random(0, 2);
		}else{
			c = 7;
		}
		switch(c){
			case 0:
				switch(Util.random(0, 2)){
					case 0:
						return Util.random(6, 7);
					case 1:
						return Util.random(4, 7);
					case 2:
						return Util.random(2, 7);
				}
				break;
			case 1:
				switch(Util.random(0, 2)){
					case 0:
						return Util.random(5, 6);
					case 1:
						return Util.random(3, 6);
					case 2:
						return Util.random(1, 6);
				}
				break;
			case 2:
				switch(Util.random(0, 2)){
					case 0:
						return Util.random(4, 5);
					case 1:
						return Util.random(2, 5);
					case 2:
						return Util.random(0, 5);
				}
				break;
			case 3:
				switch(Util.random(0, 2)){
					case 0:
						return Util.random(3, 4);
					case 1:
						return Util.random(1, 4);
					case 2:
						return Util.random(0, 4);
				}
				break;
			case 4:
				switch(Util.random(0, 2)){
					case 0:
						return Util.random(2, 3);
					case 1:
						return Util.random(1, 2);
					case 2:
						return Util.random(0, 3);
				}
				break;
			case 5:
				switch(Util.random(0, 1)){
					case 0:
						return Util.random(1, 2);
					case 1:
						return Util.random(0, 2);
				}
				break;
			case 6:
				return 0;
			default:
				switch(Util.random(0, 2)){
					case 0:
						return Util.random(6, 7);
					case 1:
						return Util.random(3, 7);
					case 2:
						return Util.random(0, 7);
				}
				break;
		}
		return 0;
	}
	
}
