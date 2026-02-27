package lineage.world.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.lineage.Kingdom;
import lineage.database.CharactersDatabase;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_TestInven;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public final class UserAdenaSellController {
	
	//유저아데나 판매 리스트
	static public List<String> list;
	
	//어드민 아데나 매입 리스트
	static public List<String> list2;
	
	static public int uid_ = 0;
	
	static public int itemCount;
	static public int adenCount;
	
	static public void init() {
		TimeLine.start("UserAdenaSellController..");
		TimeLine.end();
		list = new ArrayList<String>();
		list2 = new ArrayList<String>();
		AdminAdenaDBList();
//		AdenaDBList();
	}
	
	static public void Append(PcInstance pc) {
		
		ChattingController.toChatting(pc, String.format("판매할 %s를 입력해주세요.", "아데나"), Lineage.CHATTING_MODE_MESSAGE);
		
		pc.setInven_stat_object(null);
		pc.setInven_stat_object(pc);
		pc.setShop_inven_num(5);
		pc.clearTempList();
		itemCount = 0;
		adenCount = 0;
		
		for(ItemInstance ii : pc.getInventory().getList()) {
			if(ii.getItem().getName().equalsIgnoreCase("아데나")) {
				pc.getShop_temp_list().add(ii);
				break;
			}
		}

		pc.toSender(S_TestInven.clone(BasePacketPooling.getPool(S_TestInven.class), pc, pc.getShop_temp_list()));
		
	}
	
	static public void AdminAppend(PcInstance pc) {
		
		ChattingController.toChatting(pc, String.format("판매할 %s를 입력해주세요.", "아데나"), Lineage.CHATTING_MODE_MESSAGE);
		
		pc.setInven_stat_object(null);
		pc.setInven_stat_object(pc);
		pc.setShop_inven_num(7);
		pc.clearTempList();
		itemCount = 0;
		adenCount = 0;
		
		for(ItemInstance ii : pc.getInventory().getList()) {
			if(ii.getItem().getName().equalsIgnoreCase("아데나")) {
				pc.getShop_temp_list().add(ii);
				break;
			}
		}

		pc.toSender(S_TestInven.clone(BasePacketPooling.getPool(S_TestInven.class), pc, pc.getShop_temp_list()));
		
	}
	
	static public void AdminAdenaDBList() {
		
		uid_ = 0;
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM admin_adena_buy ORDER BY uid DESC");
			rs = st.executeQuery();
			while (rs.next()) {
				
//				if(rs.getRow() > 30) {
//					continue;
//				}
				
				int uid = rs.getInt("uid");
				int objid = rs.getInt("cha_objId");
				String chaname = rs.getString("cha_name");
				String status = rs.getString("status");
				int count = rs.getInt("count");
				int pay = rs.getInt("pay");
				int month = rs.getInt("month");
				int day = rs.getInt("day");
				String pay1 = rs.getString("pay_info1");
				String pay2 = rs.getString("pay_info2");
				String pay3 = rs.getString("pay_info3");
				
				DecimalFormat dc = new DecimalFormat("###,###,###,###,###");
				String count_s = "";
				count_s = dc.format(count);
				String pay_s = "";
				pay_s = dc.format(pay);
				
//				System.out.println(String.format("#[%d] %d (%s)\r\n ->금액: %d원 () %d-%d", uid, count, status, pay, month, day));
				
				list2.add(String.format(String.format("#[%d] %s (%s)\r\n ->금액: %s원 () %d-%d", uid, count_s, status, pay_s, month, day)));
				
				if(uid > uid_) {
					uid_ = uid;
				}
				
			}
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : user_adena_sell_read(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		
	}
	
	static public void AdenaDBList() {
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM user_adena_sell ORDER BY uid DESC");
			rs = st.executeQuery();
			while (rs.next()) {
				
//				if(rs.getRow() > 30) {
//					continue;
//				}
				
				int uid = rs.getInt("uid");
				int objid = rs.getInt("cha_objId");
				String chaname = rs.getString("cha_name");
				String status = rs.getString("status");
				int count = rs.getInt("count");
				int pay = rs.getInt("pay");
				int month = rs.getInt("month");
				int day = rs.getInt("day");
				
				DecimalFormat dc = new DecimalFormat("###,###,###,###,###");
				String count_s = "";
				count_s = dc.format(count);
				String pay_s = "";
				pay_s = dc.format(pay);
				
//				System.out.println(String.format("#[%d] %d (%s)\r\n ->금액: %d원 () %d-%d", uid, count, status, pay, month, day));
				
				list.add(String.format(String.format("#[%d] %s (%s)\r\n ->금액: %s원 () %d-%d", uid, count_s, status, pay_s, month, day)));
				
			}
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : user_adena_sell_read(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		
	}
	
	static public void AdenaPage(PcInstance pc, int page) {
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		List<String> info = new ArrayList<String>();
		info.clear();
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM user_adena_sell WHERE uid=?");
			st.setInt(1, page);
			rs = st.executeQuery();
			while (rs.next()) {
				
				int uid = rs.getInt("uid");
				int objid = rs.getInt("cha_objId");
				String chaname = rs.getString("cha_name");
				String status = rs.getString("status");
				int count = rs.getInt("count");
				int pay = rs.getInt("pay");
				int month = rs.getInt("month");
				int day = rs.getInt("day");
				
				DecimalFormat dc = new DecimalFormat("###,###,###,###,###");
				String count_s = "";
				count_s = dc.format(count);
				String pay_s = "";
				pay_s = dc.format(pay);
				
				info.add(String.format("#[%d] %s (%s)\r\n ->금액: %s원 () %d-%d", uid, count_s, status, pay_s, month, day));
				
			}
			
			info.add("구매하기");
			info.add("구매취소");
			
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), pc, "servertraded", null, info));
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : user_adena_sell_read(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		
	}
	
	static public void AdminAdenaPage(PcInstance pc, int page) {
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		List<String> info = new ArrayList<String>();
		info.clear();
		
		int uid = 0;
		int objid = 0;
		String chaname = "";
		String status = "";
		int count = 0;
		int pay = 0;
		int month = 0;
		int day = 0;
		String pay_info1 = "";
		String pay_info2 = "";
		String pay_info3 = "";
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM admin_adena_buy WHERE uid=?");
			st.setInt(1, page);
			rs = st.executeQuery();
			while (rs.next()) {
				
				uid = rs.getInt("uid");
				objid = rs.getInt("cha_objId");
				chaname = rs.getString("cha_name");
				status = rs.getString("status");
				count = rs.getInt("count");
				pay = rs.getInt("pay");
				month = rs.getInt("month");
				day = rs.getInt("day");
				pay_info1 = rs.getString("pay_info1");
				pay_info2 = rs.getString("pay_info2");
				pay_info3 = rs.getString("pay_info3");
				
				
				DecimalFormat dc = new DecimalFormat("###,###,###,###,###");
				String count_s = "";
				count_s = dc.format(count);
				String pay_s = "";
				pay_s = dc.format(pay);
				
				info.add(String.format("#[%d] %s (%s)\r\n ->금액: %s원 () %d-%d", uid, count_s, status, pay_s, month, day/*, pay_info1, pay_info2, pay_info3*/));
				
			}
			
			info.add("[판매완료]");
			info.add("[판매취소]");
			info.add("[구입]");
			
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), pc, "traded2", null, info));
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : user_adena_sell_read(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		
	}
	
	static public void AdenaBuy(PcInstance pc, int page) {
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		int uid = 0;
		int objid = 0;
		String chaname = "";
		String status = "";
		int count = 0;
		int pay = 0;
		int month = 0;
		int day = 0;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM user_adena_sell WHERE uid=?");
			st.setInt(1, page);
			rs = st.executeQuery();
			while (rs.next()) {
				
				uid = rs.getInt("uid");
				objid = rs.getInt("cha_objId");
				chaname = rs.getString("cha_name");
				status = rs.getString("status");
				count = rs.getInt("count");
				pay = rs.getInt("pay");
				month = rs.getInt("month");
				day = rs.getInt("day");
			}
			
//			System.out.println(String.format(String.format("#[%d] %d (%s)\r\n ->금액: %d원 () %d-%d", uid, count, status, pay, month, day)));
			
			if(status.equalsIgnoreCase("판매")) {
				if(pc.getInventory() != null) {
					if(pc.getInventory().isAden(pay, true)) {
						Adena_update(page, "완료");
						
						Item i = ItemDatabase.find("아데나");
						
						ItemInstance temp = pc.getInventory().find(i);

	                     if (temp == null) {
	                        // 겹칠수 있는 아이템이 존재하지 않을경우.
	                        if (i.isPiles()) {
	                           temp = ItemDatabase.newInstance(i);
	                           temp.setObjectId(ServerDatabase.nextItemObjId());
	                           temp.setBress(1);
	                           temp.setCount(count);
	                           temp.setDefinite(true);
	                           pc.getInventory().append(temp, true);
	                        } else {
	                           for (int idx = 0; idx < count; idx++) {
	                              temp = ItemDatabase.newInstance(i);
	                              temp.setObjectId(ServerDatabase.nextItemObjId());
	                              temp.setBress(1);
	                              temp.setDefinite(true);
	                              pc.getInventory().append(temp, true);
	                           }
	                        }
	                     } else {
	                        // 겹치는 아이템이 존재할 경우.
	                        pc.getInventory().count(temp, temp.getCount() + count, true);
	                     }
						
						ChattingController.toChatting(pc, String.format("%d uid 물품 구매 완료", page), Lineage.CHATTING_MODE_MESSAGE);
						
						UserAdenaSellController.AdenaPage(pc, pc.getAdena_uid());
						
						Item item = ItemDatabase.find("순수한 결정체");
						
						if (temp != null) {
							// 메모리 생성 및 초기화.

							// 군주 접속되잇는지 확인.
							PcInstance agent = World.findPc(objid);

							if (agent != null) {
			                    ItemInstance tempitem = agent.getInventory().find(item.getName(), temp.getBress(), item.isPiles());
			                    
			                     if (tempitem == null) {
			                         // 겹칠수 있는 아이템이 존재하지 않을경우.
			                         if (item.isPiles()) {
			     						ItemInstance newitem = ItemDatabase.newInstance(item);
			    						newitem.setObjectId(ServerDatabase.nextItemObjId());
			    						newitem.setName(item.getName());
			    						newitem.setCount(pay);
			    						newitem.setQuantity(temp.getQuantity());
			    						newitem.setEnLevel(temp.getEnLevel());
			    						newitem.setEquipped(false);
			    						newitem.setDefinite(true);
			    						newitem.setBress(temp.getBress());
			    						newitem.setDurability(temp.getDurability());
			    						newitem.setNowTime(0);
			    						newitem.setPetObjectId(temp.getPetObjectId());
			    						newitem.setInnRoomKey(temp.getInnRoomKey());
			    						newitem.setLetterUid(temp.getLetterUid());
			    						newitem.setRaceTicket(temp.getRaceTicket());
			                            agent.getInventory().append(newitem, true);
			                         } else {
				     						ItemInstance newitem = ItemDatabase.newInstance(item);
				    						newitem.setObjectId(ServerDatabase.nextItemObjId());
				    						newitem.setName(item.getName());
				    						newitem.setCount(pay);
				    						newitem.setQuantity(temp.getQuantity());
				    						newitem.setEnLevel(temp.getEnLevel());
				    						newitem.setEquipped(false);
				    						newitem.setDefinite(true);
				    						newitem.setBress(temp.getBress());
				    						newitem.setDurability(temp.getDurability());
				    						newitem.setNowTime(0);
				    						newitem.setPetObjectId(temp.getPetObjectId());
				    						newitem.setInnRoomKey(temp.getInnRoomKey());
				    						newitem.setLetterUid(temp.getLetterUid());
				    						newitem.setRaceTicket(temp.getRaceTicket());
				    						agent.getInventory().append(newitem, true);
			                         }
			                      } else {
			                         // 겹치는 아이템이 존재할 경우.
			                    	  agent.getInventory().count(tempitem, tempitem.getCount() + pay, true);
			                      }
							} else {
								int chaobjid = 0;
								String name = null;
								String invitemname = null;
								int invitemcount = 0;
								
								try {
									con = DatabaseConnection.getLineage();
									st = con.prepareStatement("SELECT * FROM characters WHERE LOWER(name)=?");
									st.setString(1, chaname);
									rs = st.executeQuery();

									while (rs.next()) {
										chaobjid = rs.getInt("objID");
										name = rs.getString("name");
									}

								} catch (Exception e) {
									lineage.share.System.println("캐릭터 계정 찾기 오류: " + e);
								} finally {
									DatabaseConnection.close(con, st, rs);
								}
								
								try {
									con = DatabaseConnection.getLineage();
									st = con.prepareStatement("SELECT * FROM characters_inventory WHERE LOWER(cha_name)=? AND LOWER(name)=?");
									st.setString(1, chaname);
									st.setString(2, item.getName());
									rs = st.executeQuery();

									while (rs.next()) {
										invitemname = rs.getString("name");
										invitemcount = rs.getInt("count");
									}

								} catch (Exception e) {
									lineage.share.System.println("캐릭터 아이템 찾기 오류: " + e);
								} finally {
									DatabaseConnection.close(con, st, rs);
								}
								
//								System.out.println(invitemname);
//								System.out.println(item.isPiles());
								
								if(invitemname == null && item.isPiles()) {
									
		     						ItemInstance newitem = ItemDatabase.newInstance(item);
		    						newitem.setObjectId(ServerDatabase.nextItemObjId());
		    						newitem.setName(item.getName());
		    						newitem.setCount(pay);
		    						newitem.setQuantity(temp.getQuantity());
		    						newitem.setEnLevel(temp.getEnLevel());
		    						newitem.setEquipped(false);
		    						newitem.setDefinite(true);
		    						newitem.setBress(temp.getBress());
		    						newitem.setDurability(temp.getDurability());
		    						newitem.setNowTime(0);
		    						newitem.setPetObjectId(temp.getPetObjectId());
		    						newitem.setInnRoomKey(temp.getInnRoomKey());
		    						newitem.setLetterUid(temp.getLetterUid());
		    						newitem.setRaceTicket(temp.getRaceTicket());
									
									// 접속안되잇어서 디비로 등록처리.
									try {
										con = DatabaseConnection.getLineage();
										st = con.prepareStatement("INSERT INTO characters_inventory SET "
												+ "objId=?, cha_objId=?, cha_name=?, name=?, count=?, quantity=?, en=?, equipped=?, definite=?, bress=?, "
												+ "durability=?, nowtime=?, pet_objid=?, inn_key=?, letter_uid=?, slimerace=?");
										st.setLong(1, newitem.getObjectId());
										st.setLong(2, chaobjid);
										st.setString(3, name);
										st.setString(4, newitem.getItem().getName());
										st.setLong(5, newitem.getCount());
										st.setInt(6, newitem.getQuantity());
										st.setInt(7, newitem.getEnLevel());
										st.setInt(8, newitem.isEquipped() ? 1 : 0);
										st.setInt(9, newitem.isDefinite() ? 1 : 0);
										st.setInt(10, newitem.getBress());
										st.setInt(11, newitem.getDurability());
										st.setInt(12, newitem.getNowTime());
										st.setLong(13, newitem.getPetObjectId());
										st.setLong(14, newitem.getInnRoomKey());
										st.setLong(15, newitem.getLetterUid());
										st.setString(16, newitem.getRaceTicket());
										st.executeUpdate();
										st.close();
									} catch (Exception e) {
										lineage.share.System.printf("%s : 아이템 추가\r\n", Kingdom.class.toString());
										lineage.share.System.println(e);
									} finally {
										DatabaseConnection.close(con, st, rs);
									}
								}else if(invitemname == null && !item.isPiles()) {
									
		     						ItemInstance newitem = ItemDatabase.newInstance(item);
		    						newitem.setObjectId(ServerDatabase.nextItemObjId());
		    						newitem.setName(item.getName());
		    						newitem.setCount(pay);
		    						newitem.setQuantity(temp.getQuantity());
		    						newitem.setEnLevel(temp.getEnLevel());
		    						newitem.setEquipped(false);
		    						newitem.setDefinite(true);
		    						newitem.setBress(temp.getBress());
		    						newitem.setDurability(temp.getDurability());
		    						newitem.setNowTime(0);
		    						newitem.setPetObjectId(temp.getPetObjectId());
		    						newitem.setInnRoomKey(temp.getInnRoomKey());
		    						newitem.setLetterUid(temp.getLetterUid());
		    						newitem.setRaceTicket(temp.getRaceTicket());
									
									// 접속안되잇어서 디비로 등록처리.
									try {
										con = DatabaseConnection.getLineage();
										st = con.prepareStatement("INSERT INTO characters_inventory SET "
												+ "objId=?, cha_objId=?, cha_name=?, name=?, count=?, quantity=?, en=?, equipped=?, definite=?, bress=?, "
												+ "durability=?, nowtime=?, pet_objid=?, inn_key=?, letter_uid=?, slimerace=?");
										st.setLong(1, newitem.getObjectId());
										st.setLong(2, chaobjid);
										st.setString(3, name);
										st.setString(4, newitem.getItem().getName());
										st.setLong(5, newitem.getCount());
										st.setInt(6, newitem.getQuantity());
										st.setInt(7, newitem.getEnLevel());
										st.setInt(8, newitem.isEquipped() ? 1 : 0);
										st.setInt(9, newitem.isDefinite() ? 1 : 0);
										st.setInt(10, newitem.getBress());
										st.setInt(11, newitem.getDurability());
										st.setInt(12, newitem.getNowTime());
										st.setLong(13, newitem.getPetObjectId());
										st.setLong(14, newitem.getInnRoomKey());
										st.setLong(15, newitem.getLetterUid());
										st.setString(16, newitem.getRaceTicket());
										st.executeUpdate();
										st.close();
									} catch (Exception e) {
										lineage.share.System.printf("%s : 아이템 추가\r\n", Kingdom.class.toString());
										lineage.share.System.println(e);
									} finally {
										DatabaseConnection.close(con, st, rs);
									}
								} else if(invitemname != null && !item.isPiles()) {
									
		     						ItemInstance newitem = ItemDatabase.newInstance(item);
		    						newitem.setObjectId(ServerDatabase.nextItemObjId());
		    						newitem.setName(item.getName());
		    						newitem.setCount(pay);
		    						newitem.setQuantity(temp.getQuantity());
		    						newitem.setEnLevel(temp.getEnLevel());
		    						newitem.setEquipped(false);
		    						newitem.setDefinite(true);
		    						newitem.setBress(temp.getBress());
		    						newitem.setDurability(temp.getDurability());
		    						newitem.setNowTime(0);
		    						newitem.setPetObjectId(temp.getPetObjectId());
		    						newitem.setInnRoomKey(temp.getInnRoomKey());
		    						newitem.setLetterUid(temp.getLetterUid());
		    						newitem.setRaceTicket(temp.getRaceTicket());
									
									// 접속안되잇어서 디비로 등록처리.
									try {
										con = DatabaseConnection.getLineage();
										st = con.prepareStatement("INSERT INTO characters_inventory SET "
												+ "objId=?, cha_objId=?, cha_name=?, name=?, count=?, quantity=?, en=?, equipped=?, definite=?, bress=?, "
												+ "durability=?, nowtime=?, pet_objid=?, inn_key=?, letter_uid=?, slimerace=?");
										st.setLong(1, newitem.getObjectId());
										st.setLong(2, chaobjid);
										st.setString(3, name);
										st.setString(4, newitem.getItem().getName());
										st.setLong(5, newitem.getCount());
										st.setInt(6, newitem.getQuantity());
										st.setInt(7, newitem.getEnLevel());
										st.setInt(8, newitem.isEquipped() ? 1 : 0);
										st.setInt(9, newitem.isDefinite() ? 1 : 0);
										st.setInt(10, newitem.getBress());
										st.setInt(11, newitem.getDurability());
										st.setInt(12, newitem.getNowTime());
										st.setLong(13, newitem.getPetObjectId());
										st.setLong(14, newitem.getInnRoomKey());
										st.setLong(15, newitem.getLetterUid());
										st.setString(16, newitem.getRaceTicket());
										st.executeUpdate();
										st.close();
									} catch (Exception e) {
										lineage.share.System.printf("%s : 아이템 추가\r\n", Kingdom.class.toString());
										lineage.share.System.println(e);
									} finally {
										DatabaseConnection.close(con, st, rs);
									}
								}else {
									
		     						ItemInstance newitem = ItemDatabase.newInstance(item);
		    						newitem.setObjectId(ServerDatabase.nextItemObjId());
		    						newitem.setName(item.getName());
		    						newitem.setCount(pay);
		    						newitem.setQuantity(temp.getQuantity());
		    						newitem.setEnLevel(temp.getEnLevel());
		    						newitem.setEquipped(false);
		    						newitem.setDefinite(true);
		    						newitem.setBress(temp.getBress());
		    						newitem.setDurability(temp.getDurability());
		    						newitem.setNowTime(0);
		    						newitem.setPetObjectId(temp.getPetObjectId());
		    						newitem.setInnRoomKey(temp.getInnRoomKey());
		    						newitem.setLetterUid(temp.getLetterUid());
		    						newitem.setRaceTicket(temp.getRaceTicket());
									
									// 접속안되잇어서 디비로 등록처리.
									try {
										con = DatabaseConnection.getLineage();
										st = con.prepareStatement("UPDATE characters_inventory SET count=? WHERE LOWER(cha_name)=? AND LOWER(name)=?");
										st.setInt(1, invitemcount + (int) newitem.getCount());
										st.setString(2, name);
										st.setString(3, invitemname);
										st.executeUpdate();
										st.close();
									} catch (Exception e) {
										lineage.share.System.printf("%s : 아이템 추가\r\n", Kingdom.class.toString());
										lineage.share.System.println(e);
									} finally {
										DatabaseConnection.close(con, st, rs);
									}
								}
							}
						}
						
						if((temp.getCount() - pay) < 1) {
							pc.getInventory().remove(temp, true);
						}else {
							// 판매되는 아이템 제거.
							pc.getInventory().count(temp, temp.getCount() - pay, true);
						}
						
						
					}else {
						ChattingController.toChatting(pc, String.format("화폐부족"), Lineage.CHATTING_MODE_MESSAGE);
					}
				}
			}else {
				ChattingController.toChatting(pc, String.format("판매중인 물품만 구매 가능합니다"), Lineage.CHATTING_MODE_MESSAGE);
			}
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : user_adena_sell_read(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		
	}
	
	static public void AdminAdenaBuy(PcInstance pc, int page, String status2) {
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		int uid = 0;
		int objid = 0;
		String buyname = "";
		String chaname = "";
		String status = "";
		int buyobjid = 0;
		int count = 0;
		int pay = 0;
		int month = 0;
		int day = 0;
		String pay_info1 = "";
		String pay_info2 = "";
		String pay_info3 = "";
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM admin_adena_buy WHERE uid=?");
			st.setInt(1, page);
			rs = st.executeQuery();
			while (rs.next()) {
				uid = rs.getInt("uid");
				objid = rs.getInt("cha_objId");
				buyname = rs.getString("buy_name");
				chaname = rs.getString("cha_name");
				buyobjid = rs.getInt("buy_objId");
				status = rs.getString("status");
				count = rs.getInt("count");
				pay = rs.getInt("pay");
				month = rs.getInt("month");
				day = rs.getInt("day");
				pay_info1 = rs.getString("pay_info1");
				pay_info2 = rs.getString("pay_info2");
				pay_info3 = rs.getString("pay_info3");
			}
			
//			System.out.println(String.format(String.format("#[%d] %d (%s)\r\n ->금액: %d원 () %d-%d", uid, count, status, pay, month, day)));
			
			if(status2.equalsIgnoreCase("판매취소")) {
				if(chaname.equalsIgnoreCase(pc.getName())) {
					if(status.equalsIgnoreCase("판매중")) {
						if(pc.getInventory() != null) {
								Admin_Adena_update(page, status2);
								
								Item i = ItemDatabase.find("아데나");
								
								ItemInstance temp = pc.getInventory().find(i);
								
								double per = 1 - Lineage.adena_cancle_per;
								
								count = (int) (count * per);
								
			                     if (temp == null) {
			                        // 겹칠수 있는 아이템이 존재하지 않을경우.
			                        if (i.isPiles()) {
			                           temp = ItemDatabase.newInstance(i);
			                           temp.setObjectId(ServerDatabase.nextItemObjId());
			                           temp.setBress(1);
			                           temp.setCount(count);
			                           temp.setDefinite(true);
			                           pc.getInventory().append(temp, true);
			                        } else {
			                           for (int idx = 0; idx < count; idx++) {
			                              temp = ItemDatabase.newInstance(i);
			                              temp.setObjectId(ServerDatabase.nextItemObjId());
			                              temp.setBress(1);
			                              temp.setDefinite(true);
			                              pc.getInventory().append(temp, true);
			                           }
			                        }
			                     } else {
			                        // 겹치는 아이템이 존재할 경우.
			                        pc.getInventory().count(temp, temp.getCount() + count, true);
			                     }
								ChattingController.toChatting(pc, String.format("%d uid 물품 판매 취소 완료", page), Lineage.CHATTING_MODE_MESSAGE);
								UserAdenaSellController.AdminAdenaPage(pc, pc.getAdena_uid());
							}
						}else {
							ChattingController.toChatting(pc, String.format("판매중인 물품만 가능합니다."), Lineage.CHATTING_MODE_MESSAGE);
						}
				}else if(pc.getGm() > 0 && !chaname.equalsIgnoreCase(pc.getName())) {
					if(!status.equalsIgnoreCase("판매완료")) {
						PcInstance pcc = World.findPc(chaname);
						if(pcc != null && pcc.getInventory() != null) {
								Admin_Adena_update(page, status2);
								
								Item i = ItemDatabase.find("아데나");
								
								ItemInstance temp = pcc.getInventory().find(i);
		
			                     if (temp == null) {
			                        // 겹칠수 있는 아이템이 존재하지 않을경우.
			                        if (i.isPiles()) {
			                           temp = ItemDatabase.newInstance(i);
			                           temp.setObjectId(ServerDatabase.nextItemObjId());
			                           temp.setBress(1);
			                           temp.setCount(count);
			                           temp.setDefinite(true);
			                           pcc.getInventory().append(temp, true);
			                        } else {
			                           for (int idx = 0; idx < count; idx++) {
			                              temp = ItemDatabase.newInstance(i);
			                              temp.setObjectId(ServerDatabase.nextItemObjId());
			                              temp.setBress(1);
			                              temp.setDefinite(true);
			                              pcc.getInventory().append(temp, true);
			                           }
			                        }
			                     } else {
			                        // 겹치는 아이템이 존재할 경우.
			                        pcc.getInventory().count(temp, temp.getCount() + count, true);
			                     }
			                     ChattingController.toChatting(pcc, String.format("[GM] %d uid 물품 판매 취소 완료", page), Lineage.CHATTING_MODE_MESSAGE);
								ChattingController.toChatting(pc, String.format("%d uid 물품 판매 취소 완료", page), Lineage.CHATTING_MODE_MESSAGE);
								UserAdenaSellController.AdminAdenaPage(pc, pc.getAdena_uid());
							}else {
								ChattingController.toChatting(pc, chaname + "유저가 접속중이 아닙니다.", Lineage.CHATTING_MODE_MESSAGE);
							}
						}else {
							ChattingController.toChatting(pc, String.format("판매완료 물품은 불가합니다."), Lineage.CHATTING_MODE_MESSAGE);
						}
				}else {
					ChattingController.toChatting(pc, "자신의 물품만 관리 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			}
			
			if(status2.equalsIgnoreCase("거래진행중")) {
				if(!chaname.equalsIgnoreCase(pc.getName())) {
					if(status.equalsIgnoreCase("판매중")) {
						Admin_Adena_update2(page, status2, pc.getName(), (int) pc.getObjectId());
						//구매자에게 편지알림
						LetterController.toLetter("아데나거래", pc.getName(), "구입물품 계좌정보", String.format("<판매자 정보>\r\n아이디:[%s]\r\n예금주:%s\r\n은행명:%s\r\n계좌번호:%s\r\n구입 아데나:%s\r\n입금 요청금액:%s\r\n구매자는 판매자에게 입금 후 귓속말로 [판매완료]를 요청하세요.", chaname, pay_info1, pay_info2, pay_info3, count, pay), 0);
						//판매자에게 편지알림
						LetterController.toLetter("아데나거래", chaname, "아데나 거래 알림", String.format("[%d]품목 거래정보\r\n아이디:[%s]님이 판매자분의 물품에 구입신청을 하셨습니다.", uid, pc.getName()), 0);
					}else {
						ChattingController.toChatting(pc, String.format("판매중인 물품만 가능합니다."), Lineage.CHATTING_MODE_MESSAGE);
					}
				}else {
					ChattingController.toChatting(pc, String.format("자신의 물품은 구입불가합니다."), Lineage.CHATTING_MODE_MESSAGE);
				}
			}
			
			if(status2.equalsIgnoreCase("판매완료")) {
				if(chaname.equalsIgnoreCase(pc.getName())) {
					if(status.equalsIgnoreCase("거래진행중")) {
						
					Admin_Adena_update(page, status2);
						
					PcInstance agent = World.findPc(buyname);
					
					Item item = ItemDatabase.find("아데나");
					
					if(agent == null) {
						LetterController.toLetter(null, buyname, "구입하신 아데나가..", String.format("구입하신 아데나 %d가 도착하였습니다.", count), count);
					}
						
					if(agent != null && agent.getInventory() != null) {
						
						Item i = ItemDatabase.find("아데나");
						
						ItemInstance temp = agent.getInventory().find(i);

	                     if (temp == null) {
	                        // 겹칠수 있는 아이템이 존재하지 않을경우.
	                        if (i.isPiles()) {
	                           temp = ItemDatabase.newInstance(i);
	                           temp.setObjectId(ServerDatabase.nextItemObjId());
	                           temp.setBress(1);
	                           temp.setCount(count);
	                           temp.setDefinite(true);
	                           agent.getInventory().append(temp, true);
	                        } else {
	                           for (int idx = 0; idx < count; idx++) {
	                              temp = ItemDatabase.newInstance(i);
	                              temp.setObjectId(ServerDatabase.nextItemObjId());
	                              temp.setBress(1);
	                              temp.setDefinite(true);
	                              agent.getInventory().append(temp, true);
	                           }
	                        }
	                     } else {
	                        // 겹치는 아이템이 존재할 경우.
	                    	 agent.getInventory().count(temp, temp.getCount() + count, true);
	                     }
						
						ChattingController.toChatting(pc, String.format("%d uid 물품 판매 완료", page), Lineage.CHATTING_MODE_MESSAGE);
						
						LetterController.toLetter(null, buyname, "구입하신 아데나가..", String.format("구입하신 아데나 %d가 도착하였습니다.", count), 0);
						
					}
						
					}else {
						ChattingController.toChatting(pc, String.format("거래진행중인 물품만 가능합니다."), Lineage.CHATTING_MODE_MESSAGE);
					}
				}else {
					ChattingController.toChatting(pc, String.format("자신의 물품만 관리가능합니다."), Lineage.CHATTING_MODE_MESSAGE);
				}
			}

			
			pc.setAdena_page(1);
			UserAdenaSellController.AdminAdenaList(pc);
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : user_adena_sell_read(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		
	}
	
	static public void Adena_update(int page, String status) {
		
		Connection con = null;
		PreparedStatement st = null;
		
		try {
			con = DatabaseConnection.getLineage();
			
			st = con.prepareStatement("UPDATE user_adena_sell SET status=? WHERE uid=?");
			st.setString(1, status);
			st.setInt(2, page);
			st.executeUpdate();
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : Adena_update(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
		
	}
	
	static public void Admin_Adena_update(int page, String status) {
		
		Connection con = null;
		PreparedStatement st = null;
		
		try {
			con = DatabaseConnection.getLineage();
			
			st = con.prepareStatement("UPDATE admin_adena_buy SET status=? WHERE uid=?");
			st.setString(1, status);
			st.setInt(2, page);
			st.executeUpdate();
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : Adena_update(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
		
	}
	
	static public void Admin_Adena_update2(int page, String status, String buyname, int objid) {
		
		Connection con = null;
		PreparedStatement st = null;
		
		try {
			con = DatabaseConnection.getLineage();
			
			st = con.prepareStatement("UPDATE admin_adena_buy SET status=?, buy_name=?, buy_objId=? WHERE uid=?");
			st.setString(1, status);
			st.setString(2, buyname);
			st.setInt(3, objid);
			st.setInt(4, page);
			st.executeUpdate();
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : Adena_update(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
		
	}
	
	static public void AdminAdenaList(PcInstance pc) {
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		pc.clearAdenaList2();
		
		List<String> info = new ArrayList<String>();
		info.clear();
		
		int page_first = pc.getAdena_page();
		int page_last = pc.getAdena_page() * 100;
		
		if(page_first > 1) {
			page_first = ((page_first - 1) * 100) + 1;
		}
		
		int maeip = 0;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM admin_adena_buy ORDER BY uid DESC");
			rs = st.executeQuery();
			
			while (rs.next()) {
				
				if(!rs.getString("status").equalsIgnoreCase("판매완료")) {
					continue;
				}
				
				maeip += rs.getInt("count");
			}
			
			DecimalFormat dc = new DecimalFormat("###,###,###,###,###");
			String maeip_s = "";
			maeip_s = dc.format(maeip / 10000);
			
			info.add(maeip_s + "0000 아데나");
			
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : admin_adena_buy_read(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		
//		info.add("0");
		info.add(String.format("%d만당 최저 시세 [%d원]", (Lineage.adminadenasise / 10000), Lineage.adminadenasise2));
		info.add("=====판매등록=====");
		
//		System.out.println("테스트" + page_first + "/" + page_last);
			
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM admin_adena_buy ORDER BY uid DESC");
			rs = st.executeQuery();
			
			while (rs.next()) {
				
//				maeip += rs.getInt("count");
				
				if(rs.getRow() < page_first) {
					continue;
				}
				
				if(rs.getRow() > page_last) {
					continue;
				}

				if(rs.getString("status").equalsIgnoreCase("판매완료")) {
					continue;
				}
				
				if(rs.getString("status").equalsIgnoreCase("판매취소")) {
					continue;
				}
				
				int uid = rs.getInt("uid");
				int objid = rs.getInt("cha_objId");
				String chaname = rs.getString("cha_name");
				String status = rs.getString("status");
				int count = rs.getInt("count");
				int pay = rs.getInt("pay");
				int month = rs.getInt("month");
				int day = rs.getInt("day");
				
				if(pc.isMaeip_check()) {
					if(!chaname.equalsIgnoreCase(pc.getName())) {
						continue;
					}
				}
				
//				System.out.println("#"+uid);
				
				pc.getAdena_list2().add("#"+uid);
				
//				System.out.println(String.format("#[%d] %d (%s)\r\n ->금액: %d원 () %d-%d", uid, count, status, pay, month, day));
				
				DecimalFormat dc = new DecimalFormat("###,###,###,###,###");
				String count_s = "";
				count_s = dc.format(count);
				String pay_s = "";
				pay_s = dc.format(pay);
				
				info.add(String.format("#[%d] %s (%s)\r\n ->금액: %s원 %d월%d일", uid, count_s, status, pay_s, month, day));
			}

			
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), pc, "servertrade2", null, info));
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : admin_adena_buy_read(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		
	}
	
	static public void AdenaList(PcInstance pc) {
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		pc.clearAdenaList();
		
		List<String> info = new ArrayList<String>();
		info.clear();
		
		int page_first = pc.getAdena_page();
		int page_last = pc.getAdena_page() * 100;
		
		if(page_first > 1) {
			page_first = ((page_first - 1) * 100) + 1;
		}
			
		info.add("=====판매등록=====");
		info.add("이전페이지");
		info.add("다음페이지");
		
//		System.out.println("테스트" + page_first + "/" + page_last);
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM user_adena_sell ORDER BY uid DESC");
			rs = st.executeQuery();
			while (rs.next()) {
				
				if(rs.getRow() < page_first) {
					continue;
				}
				
				if(rs.getRow() > page_last) {
					continue;
				}
				
				int uid = rs.getInt("uid");
				int objid = rs.getInt("cha_objId");
				String chaname = rs.getString("cha_name");
				String status = rs.getString("status");
				int count = rs.getInt("count");
				int pay = rs.getInt("pay");
				int month = rs.getInt("month");
				int day = rs.getInt("day");
				
//				System.out.println("#"+uid);
				
				pc.getAdena_list().add("#"+uid);
				
//				System.out.println(String.format("#[%d] %d (%s)\r\n ->금액: %d원 () %d-%d", uid, count, status, pay, month, day));
				
				DecimalFormat dc = new DecimalFormat("###,###,###,###,###");
				String count_s = "";
				count_s = dc.format(count);
				String pay_s = "";
				pay_s = dc.format(pay);
				
				info.add(String.format("#[%d] %s (%s)\r\n ->금액: %s원 %d-%d", uid, count_s, status, pay_s, month, day));
				
			}
			
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), pc, "servertrade", null, info));
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : user_adena_sell_read(PcInstance pc)\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		
	}
	
	static public void AdenaUpload(PcInstance pc, String status, int count, int pay, int month, int day) {
		
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("INSERT INTO user_adena_sell SET uid=?, cha_objId=?, cha_name=?, status=?, count=?, pay=?, month=?, day=?");
			st.setInt(1, list.size() + 1);
			st.setInt(2, (int) pc.getObjectId());
			st.setString(3, pc.getName());
			st.setString(4, status);
			st.setInt(5, count);
			st.setInt(6, pay);
			st.setInt(7, month);
			st.setInt(8, day);
			
			list.add(String.format(String.format("#[%d] %d (%s)\r\n ->금액: %d원 () %d-%d", list.size()+1, count, status, pay, month, day)));
			
			st.execute();
		} catch (Exception e) {
			lineage.share.System.println(SlimeRaceController.class + " : AdenaUpload()");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
		
	}
	
	static public void AdminAdenaUpload(PcInstance pc, String status, int count, int pay, int month, int day, String pay_info1, String pay_info2, String pay_info3) {
		
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("INSERT INTO admin_adena_buy SET uid=?, cha_objId=?, cha_name=?, status=?, count=?, pay=?, month=?, day=?, pay_info1=?, pay_info2=?, pay_info3=?");
			st.setInt(1, uid_ + 1);
			st.setInt(2, (int) pc.getObjectId());
			st.setString(3, pc.getName());
			st.setString(4, status);
			st.setInt(5, count);
			st.setInt(6, pay);
			st.setInt(7, month);
			st.setInt(8, day);
			st.setString(9, pay_info1);
			st.setString(10, pay_info2);
			st.setString(11, pay_info3);
			
			list2.add(String.format(String.format("#[%d] %d (%s)\r\n ->금액: %d원 () %d-%d", list.size()+1, count, status, pay, month, day)));
			uid_ ++;
			
			st.execute();
		} catch (Exception e) {
			lineage.share.System.println(SlimeRaceController.class + " : AdenaUpload()");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
		
	}
	static public void AppendFinal(PcInstance pc, int aden , int item, int oldaden, int olditem) {
		// 아데나 올라가는 최종확인 소스
		if (aden != oldaden || item != olditem) {
			itemCount = 0;
			adenCount = 0;
			ChattingController.toChatting(pc, "오류 발견 등록이 취소됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		UserAdenaSellController.AdminAdenaUpload(pc, "판매중", aden, item, Util.getMonth(System.currentTimeMillis()), Util.getDate(System.currentTimeMillis()), pc.getPay_info1(), pc.getPay_info2(), pc.getPay_info3());
	}
	
}
