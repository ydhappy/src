package sp;

import java.util.List;
import java.util.StringTokenizer;

import jsn_soft.PcTradeController;
import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.database.Skill;
import lineage.bean.event.Timer;
import lineage.bean.lineage.Clan;
import lineage.bean.lineage.Rank;
import lineage.database.BackgroundDatabase;
import lineage.database.ItemDatabase;
import lineage.database.NpcSpawnlistDatabase;
import lineage.database.ServerNoticeDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.client.C_ItemClick;
import lineage.network.packet.client.C_ItemDrop;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectTitle;
import lineage.plugin.Plugin;
import lineage.share.Common;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.AutoHuntCheckController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.ClanLordController;
import lineage.world.controller.CommandController;
import lineage.world.controller.DamageController;
import lineage.world.controller.FightController;
import lineage.world.controller.LocationController;
import lineage.world.controller.PcMarketController;
import lineage.world.controller.RankController;
import lineage.world.controller.RobotController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.scroll.장인의갑옷마법주문서;
import lineage.world.object.robot.BuffRobotInstance;
import lineage.world.object.robot.PcRobotInstance;
import sp.controller.AutoQuestController;
import sp.controller.ClanController;
import sp.controller.FishingController;
import sp.controller.PcInstanceController;
import sp.controller.고정신청관리;
import sp.item.ChaoticPosition;
import sp.item.ExpPosition;
import sp.item.LawfulPosition;
import sp.item.PvpLogClear;
import sp.item.RankPolyScroll;
//import sp.item.경험치물약;
import sp.item.낚싯대;
import sp.item.드래곤다이아몬드;
import sp.item.드래곤루비;
import sp.item.드래곤사파이어;
import sp.item.드래곤에메랄드;
import sp.item.성장낚싯대;
import sp.item.오만의탑정상지배부적;
import sp.item.축변환주문서;
import sp.item.프리미엄상점;
import sp.npc.내성버프사;
import sp.npc.로빈후드;
import sp.npc.버프사;
import sp.robot.SafetyRobotInstance;

public class Plugins implements Plugin {
	@Override
	public Object init(Class<?> c, Object... opt) {
		try {
			if( c.isAssignableFrom(lineage.Main.class) ){
				if(opt[0].equals("toLoading")){

					// 랜덤 텔레포트 가능한 맵
//					Lineage.TeleportPossibleMap = new int[] {
//							0, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 19, 20, 21, 24, 23, 25, 26, 27, 28, 29, 30,
//							31, 32, 33, 34, 35, 36, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55,
//							57, 58, 59, 60, 61, 62, 63, 64, 68, 69, 75, 76, 77, 78, 79, 83, 84, 85, 86, 209, 210, 211,
//							212, 213, 214, 215, 216, 300, 301, 304, 307, 308, 309, 401, 420, 440, 445, 457, 467, 468,
//							480, 522, 523, 524, 536, 600, 601, 602, 603, 604, 605, 606, 607, 608, 610, 777, 778, 779
//					};
					
					//
					PcInstanceController.init();
					ClanController.init();
//					고정신청관리.init();
//					FishingController.init();
//					
					// 공지사항 제거 및 변경하기.
					ServerNoticeDatabase.setSpNotice(null, null);
				}
				if(opt[0].equals("toDelete")) {
					//
					PcInstanceController.close();
					ClanController.close();
//					FishingController.close();
//					고정신청관리.close();
				}
			}
			
			if(c.toString().equalsIgnoreCase(BackgroundDatabase.class.toString())) {
				if(opt[0].equals("toObject")) {
					String nameid = (String)opt[1];
					int gfx = (int)opt[2];
					String title = (String)opt[3];
				}
			}
			
			if(c.toString().equalsIgnoreCase(PcInstance.class.toString())) {
				if(opt[0].equals("toWorldJoin")) {
					PcInstance pc = (PcInstance)opt[1];

					//
//					AutoQuestController.toQuest(pc);
//					sp.controller.SkillController.toWorldJoin(pc);

					//
//					PcShopInstance psi = sp.controller.CommandController.shop_list.get(pc.getObjectId());
//					if (psi != null) {
//						// 제거 처리.
//						sp.controller.CommandController.shop_list.remove(pc.getObjectId());
//						psi.clearList(true);
//						World.remove(psi);
//						psi.close();
//						psi = null;
//					}
				}
				if(opt[0].equals("toLevelup")){
					PcInstance pc = (PcInstance)opt[1];
					
					//
//					AutoQuestController.toQuest(pc);
					//
					if(pc.getClanId()==15 && pc.getLevel()==99) {
						Clan cc = lineage.world.controller.ClanController.find(pc);
						// 178 \f1%0%s %1 혈맹을 탈퇴했습니다.
						cc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 178, pc.getName(), cc.getName()));
						// 관리목록에서 제거.
						cc.toWorldOut(pc);
						cc.removeMemberList(pc.getName());
						// 탈퇴 회원 정보 변경.
						pc.setClanId(0);
						pc.setClanName(null);
						pc.setTitle(null);
						pc.toSender(S_ObjectTitle.clone(BasePacketPooling.getPool(S_ObjectTitle.class), pc), true);
					}
				}
				if(opt[0].equals("getTotalAc")) {
					PcInstance pc = (PcInstance)opt[1];
					try {
						if(고정신청관리.isGojung(pc.getClient().getAccountId()))
							return 2;
					} catch (Exception e) { }
				}
			}
			
			if(c.toString().equalsIgnoreCase(CommandController.class.toString())) {
				if(opt[0].equals("toCommand")){
					object o = (object)opt[1];
					String key = (String)opt[2];
					StringTokenizer st = (StringTokenizer)opt[3];
					
//					if(key.equalsIgnoreCase(Common.COMMAND_TOKEN+"버프"))
//						return false;
					
//					if(key.equalsIgnoreCase(Common.COMMAND_TOKEN + "상점")) {
//						return PcMarketController.toCommand(o, key, st);
					 if(key.equalsIgnoreCase(Common.COMMAND_TOKEN + "무인혈맹않함")) {
						return ClanLordController.toCommand(o, key, st);
					}if (FightController.isCommand(key)) {
						return FightController.toCommand(o, key, st);
					}else {
						return sp.controller.CommandController.toCommand(o, key, st);
					}
				}
			}

			if (c.toString().equalsIgnoreCase(C_ItemClick.class.toString())) {
				if (opt[0].equals("init")) {
					C_ItemClick cic = (C_ItemClick) opt[1];
					PcInstance pc = (PcInstance) opt[2];
					ItemInstance item = (ItemInstance) opt[3];

				}
			}

			if (c.toString().equalsIgnoreCase(C_ItemDrop.class.toString())) {
				if (opt[0].equals("init")) {
					C_ItemDrop cid = (C_ItemDrop) opt[1];
					PcInstance pc = (PcInstance) opt[2];
					ItemInstance item = (ItemInstance) opt[3];
					// 무인상점 처리 확인.
					if (sp.controller.CommandController.isShopToAppend(pc, item))
						return true;
				}
			}
			
			if(c.toString().equalsIgnoreCase(ItemDatabase.class.toString())) {
				if(opt[0].equals("newInstance")){
					Item item = (Item)opt[1];

					if(item.getName().equalsIgnoreCase("장인의 갑옷 마법 주문서"))
						return 장인의갑옷마법주문서.clone( lineage.database.ItemDatabase.getPool(장인의갑옷마법주문서.class)).clone(item);
					if(item.getName().equalsIgnoreCase("축변환주문서"))
						return 축변환주문서.clone( lineage.database.ItemDatabase.getPool(축변환주문서.class)).clone(item);
					if(item.getName().equalsIgnoreCase("오만의 탑 정상 지배 부적"))
						return 오만의탑정상지배부적.clone( lineage.database.ItemDatabase.getPool(오만의탑정상지배부적.class), 0, 0, 0 ).clone(item);
					if(item.getName().equalsIgnoreCase("프리미엄 상점"))
						return 프리미엄상점.clone( lineage.database.ItemDatabase.getPool(프리미엄상점.class), 0, 0, 0 ).clone(item);
					if(item.getType2().equalsIgnoreCase("chaotic position"))
						return ChaoticPosition.clone( lineage.database.ItemDatabase.getPool(ChaoticPosition.class) ).clone(item);
					if(item.getType2().equalsIgnoreCase("lawful position"))
						return LawfulPosition.clone( lineage.database.ItemDatabase.getPool(LawfulPosition.class) ).clone(item);
					if(item.getType2().equalsIgnoreCase("Exp position"))
						return ExpPosition.clone( lineage.database.ItemDatabase.getPool(ExpPosition.class) ).clone(item);
					if(item.getType2().equalsIgnoreCase("pvplog_clear"))
						return PvpLogClear.clone( lineage.database.ItemDatabase.getPool(PvpLogClear.class) ).clone(item);
					if(item.getName().equalsIgnoreCase("랭킹 변신 주문서"))
						return RankPolyScroll.clone( lineage.database.ItemDatabase.getPool(RankPolyScroll.class) ).clone(item);
					if(item.getType2().equalsIgnoreCase("드래곤 루비"))
						return 드래곤루비.clone( lineage.database.ItemDatabase.getPool(드래곤루비.class) ).clone(item);
					if(item.getType2().equalsIgnoreCase("드래곤 사파이어"))
						return 드래곤사파이어.clone( lineage.database.ItemDatabase.getPool(드래곤사파이어.class) ).clone(item);
					if(item.getType2().equalsIgnoreCase("드래곤 에메랄드"))
						return 드래곤에메랄드.clone( lineage.database.ItemDatabase.getPool(드래곤에메랄드.class) ).clone(item);
					if(item.getType2().equalsIgnoreCase("드래곤 다이아몬드"))
						return 드래곤다이아몬드.clone( lineage.database.ItemDatabase.getPool(드래곤다이아몬드.class) ).clone(item);
					if(item.getName().equalsIgnoreCase("성장의 낚싯대"))
						return 성장낚싯대.clone( lineage.database.ItemDatabase.getPool(성장낚싯대.class) ).clone(item);
					if(item.getName().startsWith("짧고 가벼운 낚싯대"))
						return 낚싯대.clone( lineage.database.ItemDatabase.getPool(낚싯대.class) ).clone(item);
					//
					switch(item.getNameIdNumber()){

					}
				}
			}
			
			if(c.toString().equalsIgnoreCase(NpcSpawnlistDatabase.class.toString())) {
				if(opt[0].equals("newObject")) {
					Npc npc = (Npc)opt[1];
					
					switch(npc.getNameIdNumber()){
						case 5276:	// 낚시 꼬마
							return new sp.npc.FishingBoy(npc);
						case 2574:	// 로빈후드
							return new 로빈후드(npc);
					}
					
					if(npc.getName().equalsIgnoreCase("[RED] 버프사"))
						return new 버프사(npc);
					if(npc.getName().equalsIgnoreCase("내성 버프사"))
						return new 내성버프사(npc);
					if(npc.getName().equalsIgnoreCase("[낚싯대]"))
						return new sp.npc.낚싯대(npc);
				}
			}
		
			if(c.toString().equalsIgnoreCase(DamageController.class.toString())) {
				if(opt[0].equals("DmgPlus")) {
					Character cha = (Character)opt[1];
					ItemInstance item = (ItemInstance)opt[2];
					object target = (object)opt[3];
					boolean bow = (Boolean)opt[4];
					
					return sp.controller.DamageController.DmgPlus(cha, item, target, bow);
				}
				
		
				if(opt[0].equals("DmgWeaponFigure")) {
					boolean bow = (Boolean)opt[1];
					ItemInstance weapon = (ItemInstance)opt[2];
					ItemInstance arrow = (ItemInstance)opt[3];
					boolean Small = (Boolean)opt[4];
					
					return sp.controller.DamageController.DmgWeaponFigure(bow, weapon, arrow, Small);
				}
				if(opt[0].equals("CounterBarrier")) {
					Character cha = (Character)opt[1];
					object target = (object)opt[2];
					boolean bow = (Boolean)opt[3];
					ItemInstance weapon = opt[4]==null ? null : (ItemInstance)opt[4];
					ItemInstance arrow = opt[5]==null ? null : (ItemInstance)opt[5];
					double dmg = (Double)opt[6];
					
					return sp.controller.DamageController.toCounterBarrier(cha, target, bow, weapon, arrow, dmg);
				}
			}
		
			if( c.isAssignableFrom(Timer.class) ){
				if(opt[0].equals("toTimer")){
					long time = (Long)opt[1];

					for(PcInstance pc : World.getPcList()) {
						if (pc.getMap() == 68 || pc.getMap() == 69 || pc.getMap() == 85 || pc.getMap() == 86) {
							if (pc.getGm() == 0 && pc.getLevel() > Lineage.Beginner_max_level) {
								if (pc.getMap() == 68 || pc.getMap() == 85)
									LocationController.toTalkingIsland(pc);
								else
									LocationController.toSilverknightTown(pc);
								pc.toPotal(pc.getHomeX(), pc.getHomeY(), pc.getHomeMap());
								//ChattingController.toChatting(pc, "더이상 초보존 이용이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
							}
						}
					}
				}
			}

			if( c.isAssignableFrom(ChattingController.class) ){
				if(opt[0].equals("toAutoHuntAnswer")){
					PcInstance pc = (PcInstance)opt[1];
					String answer = (String)opt[2];
					
					if (Lineage.auto_hunt_monster_kill_count <= pc.getAutoHuntMonsterCount())
						return AutoHuntCheckController.checkMessage(pc, answer);
				}
			}

			if( c.isAssignableFrom(S_ObjectChatting.class) ){
				if(opt[0].equals("CHATTING_MODE_CLAN")) {
					object o = (object)opt[1];
					String msg = (String)opt[2];
					Integer age = sp.controller.ClanController.getAge(o);
					if(age == 0)
						return String.format("{%s} %s", o.getName(), msg);
					else
						return String.format("{%s(%d)} %s", o.getName(), age, msg);
				}
			}
			
			if( c.isAssignableFrom(C_ItemClick.class) ){
				// 현금 거래 게시판.
				if(opt[0].equals("pcTrade")){
					C_ItemClick cid = (C_ItemClick)opt[1];
					PcInstance pc = (PcInstance)opt[2];
					ItemInstance item = (ItemInstance)opt[3];

					if (PcTradeController.insertItemFinal(pc, item, item.getCount()))
						return true;
				}
			}
			
			if( c.isAssignableFrom(SkillController.class) ) {
				if(opt[0].equals("toSkill")) {
					Character cha = (Character)opt[1];
					ClientBasePacket cbp = (ClientBasePacket)opt[2];
					int level = (int)opt[3];
					int number = (int)opt[4];
					
					Skill skill = SkillController.find(cha, level, number);
					if(skill != null) {
						if(level==7 && number==6 && SkillController.isDelay(cha, skill))
							sp.magic.Berserks.init(cha, skill, cbp.readD());
					}
				}
				if(opt[0].equals("getSp")) {
					Character cha = (Character)opt[1];
					boolean packet = (boolean)opt[2];
					int sp = cha.getTotalSp();
					//
					if(packet)
						return sp;
					//
					switch(cha.getClassType()){
						case 0x00:
							if(cha.getLevel()<10)
								sp += 0;
							else if(cha.getLevel()<20)
								sp += 1;
							else
								sp += 2;
							break;
						case 0x01:
							if(cha.getLevel()<50)
								sp += 0;
							else
								sp += 1;
							break;
						case 0x02:
							if(cha.getLevel()<8)
								sp += 0;
							else if(cha.getLevel()<16)
								sp += 1;
							else if(cha.getLevel()<24)
								sp += 2;
							else if(cha.getLevel()<32)
								sp += 3;
							else if(cha.getLevel()<40)
								sp += 4;
							else if(cha.getLevel()<48)
								sp += 5;
							else
								sp += 6;
							break;
						case 0x03:
							if(cha.getLevel()<4)
								sp += 0;
							else if(cha.getLevel()<8)
								sp += 1;
							else if(cha.getLevel()<12)
								sp += 2;
							else if(cha.getLevel()<16)
								sp += 3;
							else if(cha.getLevel()<20)
								sp += 4;
							else if(cha.getLevel()<24)
								sp += 5;
							else if(cha.getLevel()<28)
								sp += 6;
							else if(cha.getLevel()<32)
								sp += 7;
							else if(cha.getLevel()<36)
								sp += 8;
							else if(cha.getLevel()<40)
								sp += 9;
							else if(cha.getLevel()<44)
								sp += 10;
							else if(cha.getLevel()<50)
								sp += 11;
						
							else
								sp += 12;
							break;
						case 0x04:
							if(cha.getLevel()>=12) sp += 1;
							if(cha.getLevel()>=24) sp += 1;
							break;
						case 0x05:
							if(cha.getLevel()>=20) sp += 1;
							if(cha.getLevel()>=40) sp += 1;
							break;
						case 0x06:
							if(cha.getLevel()>=6) sp += 1;
							if(cha.getLevel()>=12) sp += 1;
							if(cha.getLevel()>=18) sp += 1;
							if(cha.getLevel()>=24) sp += 1;
							if(cha.getLevel()>=30) sp += 1;
							if(cha.getLevel()>=36) sp += 1;
							if(cha.getLevel()>=42) sp += 1;
							if(cha.getLevel()>=48) sp += 1;
							break;
						default:
							if(cha.getLevel()<10)
								sp += 0;
							else if(cha.getLevel()<20)
								sp += 1;
							else
								sp += 2;
							break;
					}
					
					switch(cha.getTotalInt()){
						case 1:
						case 2:
						case 3:
						case 4:
						case 5:
						case 6:
						case 7:
						case 8:
							sp += -1;
							break;
						case 9:
						case 10:
						case 11:
							sp += 0;
							break;
						case 12:
						case 13:
						case 14:
							sp += 1;
							break;
						case 15:
						case 16:
						case 17:
							sp += 2;
							break;
						case 18:
							sp += 3;
							break;
						default:
//							if(cha.getTotalInt() <= 24)
								sp += 3 + (cha.getTotalInt()-18);
//							else if(cha.getTotalInt()>=25 && cha.getTotalInt()<=35)
//								sp += 10;
//							else if(cha.getTotalInt()>=36 && cha.getTotalInt()<=42)
//								sp += 11;
//							else
//								sp += 12;
							break;
					}
					return sp;
				}
			}
			    //축변환시 mr+1 증가소스
     			if(c.isAssignableFrom(ItemInstance.class)) {
				if(opt[0].equals("toOption")) {
					ItemInstance ii = (ItemInstance)opt[1];
					Character cha = (Character)opt[2];
					boolean sendPacket = (boolean)opt[3];
					if(ii.getBress() == 0) {
			     //mr +1,  피틱 +1 , 엠틱 +1
				if(ii.isEquipped()){
							cha.setItemWeight(cha.getItemWeight() + 0.05);
							cha.setDynamicTicHp(cha.getDynamicTicHp() + 1);
							cha.setDynamicTicMp(cha.getDynamicTicMp() + 1);
				}else{
							cha.setItemWeight(cha.getItemWeight() - 0.05);
							cha.setDynamicTicHp(cha.getDynamicTicHp() - 1);
							cha.setDynamicTicMp(cha.getDynamicTicMp() - 1);
						}
					}
				}
			}
			if(c.toString().equalsIgnoreCase(RobotController.class.toString())) {
				if(opt[0].equals("init")) {
					List<PcRobotInstance> list_pc = (List<PcRobotInstance>)opt[1];
					List<BuffRobotInstance> list_buff = (List<BuffRobotInstance>)opt[2];
					
					SafetyRobotInstance.init(list_pc);
				}
			}
			
			//if(c.toString().equalsIgnoreCase(ShopInstance.class.toString())) {
				//if(opt[0].equals("toDwarfAndShop")) {
					//ShopInstance shop = (ShopInstance)opt[1];
					//PcInstance pc = (PcInstance)opt[2];
					//ClientBasePacket cbp = (ClientBasePacket)opt[3];
					//if(pc.getClanId() == 0) {
					//	ChattingController.toChatting(pc, "혈맹 미가입상태에서 상점이용은 하실 수 없습니다.", 20);
						//return true;
					//}
				//	return null;
				//}
		//	}
			
		} catch (Exception e) { 
		}
		return null;
	}

}
