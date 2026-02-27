package lineage.world.object.instance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.StringTokenizer;

import lineage.bean.database.Npc;
import lineage.bean.database.Warehouse;
import lineage.bean.lineage.Clan;
import lineage.bean.lineage.Kingdom;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.database.WarehouseClanLogDatabase;
import lineage.database.WarehouseDatabase;
import lineage.gui.GuiMain;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_WareHouse;
import lineage.network.packet.server.S_WareHouseHistory;
import lineage.share.Common;
import lineage.share.Lineage;
import lineage.share.Log;
import lineage.share.System;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.ClanController;
import lineage.world.object.object;
import lineage.world.object.item.sp.CharacterSaveMarbles;

public class DwarfInstance extends object {
   private Npc npc;
   protected Kingdom kingdom;

   public DwarfInstance(Npc npc) {
      this.npc = npc;
   }

   @Override
   public void close() {
      super.close();
      kingdom = null;
      npc = null;
   }

   /**
    * 창고를 이용할 수 있는 레벨인지 확인하는 메서드.
    */
   static public boolean isLevel(int level) {
      return level >= Lineage.warehouse_level;
   }

   @Override
   public void toTalk(PcInstance pc, String action, String type,
         ClientBasePacket cbp, Object... opt) {
      int dwarf_type = 0;
      if (action.indexOf("pledge") > 0)
         dwarf_type = 1;
      else if (action.indexOf("elven") > 0)
         dwarf_type = 2;
      else if (action.equalsIgnoreCase("retrieve"))
         dwarf_type = 0;
      else if (action.equalsIgnoreCase("history"))
         dwarf_type = 3;
      else {
         // System.out.println(action);
         return;
      }
      //
      long id = dwarf_type == 1 ? pc.getClanId() : pc.getClient().getAccountUid();
      Clan clan = ClanController.find(pc);
      //
      if (dwarf_type == 1) {
         // \f1혈맹창고를 사용하려면 혈맹이 있어야 합니다.
         if (pc.getClanId() == 0) {
            pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 208));
            return;
         }
         if(!clan.getLord().equalsIgnoreCase(pc.getName())) {
            // 호칭을 받지 못한 혈맹원이나 견습 혈맹원은 혈맹창고를 사용할 수 없습니다.
            if(pc.getTitle()==null || pc.getTitle().length()==0) {
               pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 728));
               return;
            }
//            if (Lineage.server_version>=270 && clan.containsStaterGuardList(pc.getName())) {
//               pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 728));
//               return;
//            }
         }
         // \f1다른 혈맹원이 창고를 사용중입니다. 잠시 후에 다시 하십시오.
			if (clan.getClanWarehouseTime() + (1000 * 60) >= System.currentTimeMillis()) {
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 209));
				return;
			}
      }
      //
      switch (dwarf_type) {
         case 3:
            // if(clan == null) {
            // pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class),
            // 208));
            // break;
            // }
            // 군주 체크.
            // 계급 체크.
            // if(pc.getClassType() != 0x00)
            // break;
            // pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class),
            // 728));
   
            pc.toSender(S_WareHouseHistory.clone(BasePacketPooling.getPool(S_WareHouseHistory.class)));
            break;
         default:
            int cnt = WarehouseDatabase.getCount(id, dwarf_type);
            if (cnt == 0) {
               // 아이템 없음
               pc.toSender(S_Html.clone(
                     BasePacketPooling.getPool(S_Html.class), this,
                     "noitemret"));
        	} else {
				//
				if (dwarf_type == 1)
					clan.setClanWarehouseTime(System.currentTimeMillis());
				// 창고 목록 열람.
				List<Warehouse> list = WarehouseDatabase.getList(id,
						dwarf_type);
				pc.toSender(S_WareHouse.clone(
						BasePacketPooling.getPool(S_WareHouse.class), this,
						dwarf_type, list));
				for (Warehouse wh : list)
					WarehouseDatabase.setPool(wh);
				list.clear();
			}
			break;
      }
   }

   @Override
   public synchronized  void toDwarfAndShop(PcInstance pc, ClientBasePacket cbp) {
		if (Lineage.open_wait && pc.getGm() == 0) {
			ChattingController.toChatting(pc, "[오픈 대기] 창고를 이용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
      int type = cbp.readC();
      switch (type) {
      case 2: // 창고 맡기기
         insert(pc, 0, cbp);
         break;
      case 3: // 창고 찾기
         select(pc, 0, cbp);
         break;
      case 4: // 혈맹창고 맡기기
         insert(pc, 1, cbp);
         break;
      case 5: // 혈맹창고 찾기
			Clan clan = ClanController.find(pc);

			if (clan != null) {
				if (System.currentTimeMillis() > clan.getClanWarehouseTime()) {
					clan.setClanWarehouseTime(System.currentTimeMillis() + (1000 * 1));
					select(pc, Lineage.DWARF_TYPE_CLAN, cbp);
				} else {
					ChattingController.toChatting(pc, "1초 후 다시 이용하시기 바랍니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			}
			break;
      case 8: // 요정창고 맡기기
         insert(pc, 2, cbp);
         break;
      case 9: // 요정창고 찾기
         select(pc, 2, cbp);
         break;
      }
   }

   /**
    * 창고에서 아이템 꺼낼때 사용하는 메서드.
    */
   private synchronized void select(PcInstance pc, int dwarf_type, ClientBasePacket cbp) {
      //
      Connection con = null;
      PreparedStatement st = null;
      ResultSet rs = null;
      PreparedStatement st2 = null;
      ResultSet rs2 = null;
      Clan clan = dwarf_type == 1 ? ClanController
            .find(pc) : null;
      try {
         con = DatabaseConnection.getLineage();

         int count = cbp.readH();
//         
//  	   if(dwarf_type == 1) {
//  		   	try {
//  				//0.01~0.1초 랜덤 딜레이
//  				Thread.sleep(Util.random(1, 1000));
//  			}catch(Exception e) {
//  				
//  			}
//  		   }
         
         long id = dwarf_type == 1 ? pc.getClanId(): pc.getClient().getAccountUid();
         int w_Count = WarehouseDatabase.getCount(id, dwarf_type);

         if (count > 0 && count <= w_Count) {
            long item_id = 0;
            int item_count = 0;
    
            for (int i = 0; i < count; ++i) {
               item_id = cbp.readD();
               item_count = (int)cbp.readD();
       		if(item_count >2000000000 ||item_count < 0)
				return ;
               switch (dwarf_type) {
               case 1:
                  st = con.prepareStatement("SELECT * FROM warehouse_clan WHERE uid=? AND clan_id=?");
                  break;
               case 2:
                  st = con.prepareStatement("SELECT * FROM warehouse_elf WHERE uid=? AND account_uid=?");
                  break;
               default:
                  st = con.prepareStatement("SELECT * FROM warehouse WHERE uid=? AND account_uid=?");
                  break;
               }
               st.setLong(1, item_id);
               st.setLong(2, id);
               rs = st.executeQuery();
               if (rs.next()) {
                  int db_uid = rs.getInt(1);
                  int db_inv_id = rs.getInt(3);
                  int pet_objid = rs.getInt(4);
                  int letter_id = rs.getInt(5);
                  String db_name = rs.getString(6);
                  long db_count = rs.getLong(9);
                  int db_quantity = rs.getInt(10);
                  int db_en = rs.getInt(11);
                  boolean db_definite = rs.getInt(12) == 1;
                  int db_bress = rs.getInt(13);
                  int db_durability = rs.getInt(14);
                  int db_time = rs.getInt(15);
                  String db_options = rs.getString(16);
                  int enEarth = rs.getInt("enEarth");
                  int enWater = rs.getInt("enWater");
                  int enFire = rs.getInt("enFire");
                  int enWind = rs.getInt("enWind");
                  
                  int db_add_min_dmg = rs.getInt(21);
                  int db_add_max_dmg = rs.getInt(22);
                  int db_add_str = rs.getInt(23);
                  int db_add_dex = rs.getInt(24);
                  int db_add_con = rs.getInt(25);
                  int db_add_int = rs.getInt(26);
                  int db_add_wiz = rs.getInt(27);
                  int db_add_cha = rs.getInt(28);
                  int db_add_mana = rs.getInt(29);
                  int db_add_hp = rs.getInt(30);
                  int db_add_manastell = rs.getInt(31);
                  int db_add_hpstell = rs.getInt(32);
                  
                  int db_one = rs.getInt(33);
                  int db_two = rs.getInt(34);
                  int db_three = rs.getInt(35);
                  int db_four = rs.getInt(36);
                  
                  long db_soul_cha = rs.getInt(37);
                  
                  // log 용
                  String item_name = "-";
                  long item_objid = db_inv_id;
                  String target_name = "-";
                  long target_objid = 0;
                  long item_objid_new = 0;

                  ItemInstance temp = ItemDatabase.newInstance(ItemDatabase.find(db_name));
                  if (temp != null && item_count > 0 && item_count <= db_count) {                 
                     temp.setCount(item_count);
                     temp.setBress(db_bress);
                     if (pc.getInventory().isAppend(temp,  temp.getCount(), false)) {
                         
                        boolean aden = dwarf_type == 2 ? pc.getInventory().isMeterial( Lineage.warehouse_price_elf,  true) : pc.getInventory().isAden(Lineage.warehouse_price, true);
                            
                        if (aden) {
                           ItemInstance temp2 = pc.getInventory()
                                 .find(temp);
                           if (temp2 == null) {
                              // insert
                              item_objid_new = ServerDatabase
                                    .nextItemObjId();
                          	if (temp instanceof CharacterSaveMarbles) {
								item_objid_new = db_inv_id;
							} else {
								item_objid_new = ServerDatabase.nextItemObjId();
							}
                              temp.setObjectId(item_objid_new);
                              temp.setQuantity(db_quantity);
                              temp.setEnLevel(db_en);
                              temp.setDefinite(db_definite);
                              temp.setBress(db_bress);
                              temp.setDurability(db_durability);
                              temp.setTime(db_time);
                              temp.setPetObjectId(pet_objid);
                              temp.setLetterUid(letter_id);
                              StringTokenizer tok = new StringTokenizer(
                                    db_options, "|");
                              while (tok.hasMoreTokens()) {
                                 String[] token = tok.nextToken()
                                       .split("=");
                                 temp.setOption(token[0], token[1]);
                              }
                              temp.setEnEarth(enEarth);
                              temp.setEnWater(enWater);
                              temp.setEnFire(enFire);
                              temp.setEnWind(enWind);
                              
                              temp.setAdd_Min_Dmg(db_add_min_dmg);
                              temp.setAdd_Max_Dmg(db_add_max_dmg);
                              temp.setAdd_Str(db_add_str);
                              temp.setAdd_Dex(db_add_dex);
                              temp.setAdd_Con(db_add_con);
                              temp.setAdd_Int(db_add_int);
                              temp.setAdd_Wiz(db_add_wiz);
                              temp.setAdd_Cha(db_add_cha);
                              temp.setAdd_Mana(db_add_mana);
                              temp.setAdd_Hp(db_add_hp);
                              temp.setAdd_Manastell(db_add_manastell);
                              temp.setAdd_Hpstell(db_add_hpstell);
                              temp.setOne(db_one);
                              temp.setTwo(db_two);
                              temp.setThree(db_three);
                              temp.setFour(db_four);
                              temp.setSoul_cha(db_soul_cha);
                         
                              
                      		if (temp instanceof CharacterSaveMarbles) {
								CharacterSaveMarbles csm = (CharacterSaveMarbles) temp;
								csm.toWorldJoin(con, pc);
						
							} else {
								temp.toWorldJoin(con, pc);
							}
							pc.getInventory().append(temp, true);
                              //
                              WarehouseClanLogDatabase.append(pc,
                                    temp, item_count, "remove",
                                    item_objid);
                              //
                              if (Lineage.clan_warehouse_message
                                    && dwarf_type == 1) {
                                 String msg = String
                                       .format("혈맹창고를 이용해 '%s' 님께서 '%s' 아이템을 %s셨습니다.",
                                             pc.getName(),
                                             temp.toStringDB(),
                                             "찾으");
                                 clan.toSender(S_ObjectChatting.clone(
                                       BasePacketPooling
                                             .getPool(S_ObjectChatting.class),
                                       null,
                                       20,
                                       msg));
                              }
                              // log
                              item_name = temp.toStringDB();
                           } else {
                              //
                              WarehouseClanLogDatabase.append(pc,
                                    temp, item_count, "remove",
                                    item_objid);
                              //
                              if (Lineage.clan_warehouse_message
                                    && dwarf_type == 1) {
                                 String msg = String
                                       .format("혈맹창고를 이용해 '%s' 님께서 '%s' 아이템을 %s셨습니다.",
                                             pc.getName(),
                                             temp.toStringDB(),
                                             "찾으");
                                 clan.toSender(S_ObjectChatting.clone(
                                       BasePacketPooling
                                             .getPool(S_ObjectChatting.class),
                                       null,
                                       20,
                                       msg));
                              }
                              // log
                              item_name = temp.toStringDB();
                              target_name = temp2.toStringDB();
                              target_objid = temp2.getObjectId();
                              // update
                              pc.getInventory()
                                    .count(temp2,
                                          temp,
                                          temp2.getCount()
                                                + temp.getCount(),
                                          true);
//                              ItemDatabase.setPool(temp);
                           }

                           db_count -= item_count;
                           if (db_count <= 0) {
                              // delete
                              switch (dwarf_type) {
                              case 1:
                                 st2 = con
                                       .prepareStatement("DELETE FROM warehouse_clan WHERE uid=?");
                                 break;
                              case 2:
                                 st2 = con
                                       .prepareStatement("DELETE FROM warehouse_elf WHERE uid=?");
                                 break;
                              default:
                                 st2 = con
                                       .prepareStatement("DELETE FROM warehouse WHERE uid=?");
                                 break;
                              }
                              st2.setInt(1, db_uid);
                              st2.executeUpdate();
                              st2.close();
                           } else {
                              // update
                              switch (dwarf_type) {
                              case 1:
                                 st2 = con
                                       .prepareStatement("UPDATE warehouse_clan SET count=? WHERE uid=?");
                                 break;
                              case 2:
                                 st2 = con
                                       .prepareStatement("UPDATE warehouse_elf SET count=? WHERE uid=?");
                                 break;
                              default:
                                 st2 = con
                                       .prepareStatement("UPDATE warehouse SET count=? WHERE uid=?");
                                 break;
                              }
                              st2.setLong(1, db_count);
                              st2.setInt(2, db_uid);
                              st2.executeUpdate();
                              st2.close();
                           }

                           //
                           if (dwarf_type == 1)
                              Log.appendItem(pc, "type|혈맹창고찾기",
                                    String.format("item_name|%s",
                                          item_name),
                                    String.format("item_objid|%d",
                                          item_objid),
                                    String.format(
                                          "item_objid_new|%d",
                                          item_objid_new), String
                                          .format("count|%d",
                                                item_count),
                                    String.format("target_name|%s",
                                          target_name),
                                    String.format(
                                          "target_objid|%d",
                                          target_objid));
                           else
                              Log.appendItem(pc, "type|창고찾기", String
                                    .format("item_name|%s",
                                          item_name), String
                                    .format("item_objid|%d",
                                          item_objid), String
                                    .format("item_objid_new|%d",
                                          item_objid_new),
                                    String.format("count|%d",
                                          item_count),
                                    String.format("target_name|%s",
                                          target_name),
                                    String.format(
                                          "target_objid|%d",
                                          target_objid));
                           
                           
                           
       					// gui 로그
                  
							if (!Common.system_config_console && pc instanceof PcInstance) {
								
								long time = System.currentTimeMillis();
								String timeString = Util.getLocaleString(time, true);
								String log = String.format("[%s]\t[%s]\t캐릭터: %s\t캐릭터obj_id: %d\t  아이템: %s", timeString, 
										dwarf_type == Lineage.DWARF_TYPE_CLAN ? "혈맹 창고 찾기" : "창고 찾기", pc.getName(), pc.getObjectId(), Util.getItemNameToString(temp, item_count));

								GuiMain.display.asyncExec(new Runnable() {
									public void run() {
										GuiMain.getViewComposite().getWarehouseComposite().toLog(log);
									}
								});
							}

                        } else {
                           if (dwarf_type == 2)
                              // \f1%0%s 부족합니다.
                              pc.toSender(S_Message.clone(
                                    BasePacketPooling
                                          .getPool(S_Message.class),
                                    337, "미스릴"));
                           else
                              // \f1아데나가 충분치 않습니다.
                              pc.toSender(S_Message.clone(
                                    BasePacketPooling
                                          .getPool(S_Message.class),
                                    189));
                           ItemDatabase.setPool(temp);
                           break;
                        }
                     } else {
                        ItemDatabase.setPool(temp);
                        break;
                     }
                  }
               }
               rs.close();
               st.close();
            }

         }
      } catch (Exception e) {
         lineage.share.System
               .println(DwarfInstance.class.toString()
                     + " : select(PcInstance pc, boolean clan, ClientBasePacket cbp)");
         lineage.share.System.println(e);
      } finally {
         DatabaseConnection.close(st2, rs2);
         DatabaseConnection.close(con, st, rs);
      }
      //
      if (dwarf_type == 1)
         clan.setClanWarehouseTime(0L);
   }

   /**
    * 창고에 아이템 맡길때 처리하는 메서드.
    */
   private synchronized void insert(PcInstance pc, int dwarf_type, ClientBasePacket cbp) {
      Connection con = null;
      try {
         con = DatabaseConnection.getLineage();

         long uid = dwarf_type == 1 ? pc.getClanId()
               : pc.getClient().getAccountUid();
         int Count = cbp.readH();
         int w_Count = WarehouseDatabase.getCount(uid, dwarf_type);
         boolean is = dwarf_type == 1 ? pc.getClanId() > 0
               : true;
         if (Count > 0) {
            if (is && w_Count + Count <= Lineage.warehouse_max) {

               for (int i = 0; i < Count; ++i) {
                  ItemInstance temp = pc.getInventory()
                        .value(cbp.readD());


                  
                  if (temp != null) {
                     boolean idDB = dwarf_type == 1 ? temp
                           .getItem().isWarehouseClan()
                           : (dwarf_type == 2 ? temp
                                 .getItem().isWarehouseElf() : temp
                                 .getItem().isWarehouse());
                     final int count = (int)cbp.readD();
             		if(count >2000000000 ||count < 0)
        				return ;
                     boolean isRemove = pc.getInventory().isRemove(temp,
                           count, true, true, true);

                     if (!temp.isEquipped() && idDB && isRemove) {
                        if (count > 0 && count <= temp.getCount()) {
                           //
                           String item_name = temp.toStringDB();
                           // 등록하려는 아이템이 겹쳐지는 아이템이라면 디비에 겹칠 수 있는 것이
                           // 존재하는지 확인.
                           long inv_id = temp.getItem().isPiles() ? WarehouseDatabase
                                 .isPiles(temp.getItem().isPiles(),
                                       uid, temp.getItem()
                                             .getName(), temp
                                             .getBress(),
                                       dwarf_type) : 0;
                           if (inv_id > 0) {
                              // update
                              WarehouseDatabase.update(temp.getItem()
                                    .getName(), temp.getBress(),
                                    uid, count, dwarf_type);
                           } else {
                              // insert
                              if (count == temp.getCount()) {
                                 WarehouseDatabase.insert(temp,
                                       temp.getObjectId(), count,
                                       uid, dwarf_type);
                              } else {
                                 WarehouseDatabase.insert(temp,
                                       ServerDatabase
                                             .nextItemObjId(),
                                       count, uid, dwarf_type);
                              }
                           }
                           //
                           WarehouseClanLogDatabase.append(pc, temp,
                                 count, "append", 0);
                           //
                           if (Lineage.clan_warehouse_message
                                 && dwarf_type == 1) {
                              Clan clan = ClanController.find(pc);
                              if (clan != null) {
                                 String msg = String
                                       .format("혈맹창고를 이용해 '%s' 님께서 '%s' 아이템을 %s셨습니다.",
                                             pc.getName(), temp
                                                   .getItem()
                                                   .getName(),
                                             "맡기");
                                 clan.toSender(S_ObjectChatting.clone(
                                       BasePacketPooling
                                             .getPool(S_ObjectChatting.class),
                                       null,
                                       20,
                                       msg));
                              }
                           }
                           //
                         
                     
                           //
                           if (dwarf_type == 1)
                              Log.appendItem(
                                    pc,
                                    "type|혈맹창고등록",
                                    String.format("item_name|%s",
                                          item_name),
                                    String.format("item_objid|%d",
                                          temp.getObjectId()),
                                    String.format("count|%d", count),
                                    String.format(
                                          "target_objid|%d",
                                          inv_id));
                           else
                              Log.appendItem(
                                    pc,
                                    "type|창고등록",
                                    String.format("item_name|%s",
                                          item_name),
                                    String.format("item_objid|%d",
                                          temp.getObjectId()),
                                    String.format("count|%d", count),
                                    String.format(
                                          "target_objid|%d",
                                          inv_id));
                           
           				// gui 로그
							if (!Common.system_config_console  && pc instanceof PcInstance) {
								long time = System.currentTimeMillis();
								String timeString = Util.getLocaleString(time, true);
								String log = String.format("[%s]\t [%s]\t [캐릭터: %s]\t [캐릭터obj_id: %d]\t [아이템: %s]", timeString, 
										dwarf_type == 1 ? "혈맹 창고 맡기기" : "창고 맡기기", pc.getName(), pc.getObjectId(), Util.getItemNameToString(temp, count));

								GuiMain.display.asyncExec(new Runnable() {
									public void run() {
										GuiMain.getViewComposite().getWarehouseComposite().toLog(log);
									}
								});
							}
							  pc.getInventory().count(temp,temp.getCount() - count, true);
						   
                        }
                     } else {
                    	 
                    	 
//                        //
//                        if (!idDB && temp.getItem().getName().equalsIgnoreCase("아데나")) {
//                           if (dwarf_type == 1) {
//                              ChattingController.toChatting(pc,
//                                    "혈매창고에 맡길 수 없는 아이템입니다.",
//                                    20);
//                           } else if (dwarf_type == 2) {
//                              ChattingController.toChatting(pc,
//                                    "요정창고에 맡길 수 없는 아이템입니다.",
//                                    20);
//                           } else {
//                              ChattingController.toChatting(pc,
//                                    "창고에 맡길 수 없는 아이템입니다.",
//                                    20);
//                           }
//                        }
                     }
                  }
               }

            } else {
               // \f1더이상 물건을 넣을 자리가 없습니다.
               pc.toSender(S_Message.clone(
                     BasePacketPooling.getPool(S_Message.class), 75));
            }
         }

      } catch (Exception e) {
         lineage.share.System
               .println(DwarfInstance.class.toString()
                     + " : insert(PcInstance pc, int dwarf_type, ClientBasePacket cbp)");
         lineage.share.System.println(e);
      } finally {
         DatabaseConnection.close(con);
      }
   }

}