package sp.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import jsn_soft.JsnSoft;
import jsn_soft.S_InventoryFind;
import jsn_soft.S_MonDrop;
import lineage.bean.database.Drop;
import lineage.bean.database.Item;
import lineage.bean.database.Monster;
import lineage.bean.lineage.Board;
import lineage.bean.lineage.Clan;
import lineage.bean.lineage.Kingdom;
import lineage.bean.lineage.Party;
import lineage.database.CharactersDatabase;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.database.MonsterDatabase;
import lineage.database.MonsterDropDatabase;
import lineage.database.QuestDatabase;
import lineage.database.QuestDatabase.PcLevelQuest;
import lineage.database.QuestDatabase.RepeatQuest;
import lineage.network.LineageServer;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_BoardList;
import lineage.network.packet.server.S_ClanWar;
import lineage.network.packet.server.S_Disconnect;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_InventoryAdd;
import lineage.network.packet.server.S_InventoryDelete;
import lineage.network.packet.server.S_LetterNotice;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAdd;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectMap;
import lineage.network.packet.server.S_QuestView;
import lineage.share.Common;
import lineage.share.Lineage;
import lineage.share.System;
import lineage.world.World;
import lineage.world.controller.QuestTodayController;
import lineage.world.controller.BoardController;
import lineage.world.controller.BossController;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.ClanController;
import lineage.world.controller.KingdomController;
import lineage.world.controller.LetterController;
import lineage.world.controller.PartyController;
import lineage.world.controller.TradeController;
import lineage.world.controller.무인혈맹컨트롤러;
import lineage.world.object.object;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.Arrow;
import lineage.world.object.item.MagicDoll;
import lineage.world.object.item.potion.BluePotion;
import lineage.world.object.item.potion.BraveryPotion;
import lineage.world.object.item.potion.CurePoisonPotion;
import lineage.world.object.item.potion.ElvenWafer;
import lineage.world.object.item.potion.HastePotion;
import lineage.world.object.item.potion.HealingPotion;
import lineage.world.object.item.potion.LawfulPotion;
import lineage.world.object.item.potion.WisdomPotion;
import lineage.world.object.item.scroll.ScrollLabeledDaneFools;
import lineage.world.object.item.scroll.ScrollLabeledKernodwel;
import lineage.world.object.item.scroll.ScrollLabeledPratyavayah;
import lineage.world.object.item.scroll.ScrollLabeledVenzarBorgavve;
import lineage.world.object.item.scroll.ScrollLabeledVerrYedHorae;
import lineage.world.object.item.scroll.ScrollLabeledVerrYedHoraePledgeHouse;
import lineage.world.object.item.scroll.ScrollLabeledZelgoMer;
import lineage.world.object.item.scroll.ScrollPolymorph;
import lineage.world.object.item.scroll.ScrollPolymorph3;
import lineage.world.object.item.scroll.ScrollPolymorph4;
import lineage.world.object.item.scroll.ScrollResurrection;
import lineage.world.object.item.scroll.ScrollTeleport;
import lineage.world.object.item.scroll.TOITeleportScroll;
import lineage.world.object.magic.ShapeChange;
import sp.bean.shop;
import sp.object.PcShopInstance;
import lineage.world.object.robot.PcRobotInstance;

public class CommandController {

	static public Map<Long, PcShopInstance> shop_list;
	static private Map<Long, Long> delay_temp_list;

	static {
		shop_list = new HashMap<Long, PcShopInstance>();
		delay_temp_list = new HashMap<Long, Long>();
	}

	static public Object toCommand(object o, String key, StringTokenizer st) {

		if (key.equalsIgnoreCase(".변신해제")) {
			BuffController.remove(o, ShapeChange.class);
			return true;
		}

/*		if (key.equalsIgnoreCase(".복구")) {
			try {
				if (o.getMap() != 70) {
					toRepairLocation(o);
				} else {
					ChattingController.toChatting(o, "잊혀진 섬에서는 사용불가합니다", Lineage.CHATTING_MODE_MESSAGE);
				}
			} catch (Exception localException42) {
				if (o != null)
					ChattingController.toChatting(o, ".복구", 20);
			}
			return true;
		} */
		
		if (key.equalsIgnoreCase(".무인군주") || key.equalsIgnoreCase(".무인혈맹")) {
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			무인혈맹컨트롤러.insert(pc);
		}				
		return true;
		}
		
		if (key.equalsIgnoreCase(".데미지") || key.equalsIgnoreCase(".데미지확인")) {
		try {
			String pcName = st.nextToken();
			PcInstance pc = null;

			if (World.findPc(pcName) == null) {
				ChattingController.toChatting(o, String.format("[%s] 캐릭터는 존재하지 않습니다.", pcName), Lineage.CHATTING_MODE_MESSAGE);
				return true;
			} else {			
				pc = World.findPc(pcName);
				pc.isDmgCheck = pc.isDmgCheck ? false : true;
			}

			ChattingController.toChatting(o, String.format("[%s] 캐릭터 데미지 확인 %s활성화.", pcName, pc.isDmgCheck ? "" : "비"), Lineage.CHATTING_MODE_MESSAGE);
		} catch (Exception e) {
			if (o != null)
				ChattingController.toChatting(o, "데미지확인 캐릭명", Lineage.CHATTING_MODE_MESSAGE);
		}
		return true;
		}
		if (key.equalsIgnoreCase(".대미지멘트")
				|| key.equalsIgnoreCase(".허수아비")) {
			o.setDamageMassage(o.isDamageMassage() ? false : true);
			ChattingController.toChatting(o,
					String.format("대미지 멘트: %s활성화", o.isDamageMassage() ? "비" : ""),
					Lineage.CHATTING_MODE_MESSAGE);
			return true;
		}
		/*if (key.equalsIgnoreCase(".인벤") || key.equalsIgnoreCase(".인벤정리")) {
			try {
				if (o.getInventory() != null) {
					inventorySetting(o);

					ChattingController.toChatting(o, "인벤토리 정리 완료.", Lineage.CHATTING_MODE_MESSAGE);
				}
			} catch (Exception e) {
				ChattingController.toChatting(o, "인벤토리 정리 실패.", Lineage.CHATTING_MODE_MESSAGE);
			}
			return true;
		}*/

		if (key.equalsIgnoreCase(".혈맹도움말")) {
			StringBuffer sb = new StringBuffer();
			sb.append(".혈맹 ").append("\n");
			sb.append(".직위 수련,일반,수호 칭호 부여").append("\n");
			sb.append(".혈맹초대").append("\n");
			sb.append(".나이 나이설정(개인)").append("\n");
			sb.append(".무인혈맹 시작 종료 홍보").append("\n");
			/*
			 * sb.append("[.군업버프자동]명령어를 치신 후에 각각 샤이닝,글로잉,브레이브를 한번씩 사용하셔야 됩니다. " +
			 * "만약에 엠피가 부족하여 군버프를 사용하지 못할때 해당 버프는 사라집니다. 기란이나 글루딘 여관에서" +
			 * "군업버프자동을 설정하시는 걸 추천드립니다.").append("\n");
			 */
			LetterController.toMessageBox(o, "혈맹 관련 명령어.", sb.toString());

			return true;

		}
		if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "아이템정보") || key.equalsIgnoreCase(Common.COMMAND_TOKEN + "33")) {
			toItemven(o, st);
			return true;
		}
		if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "템추적") || key.equalsIgnoreCase(Common.COMMAND_TOKEN + "22")) {
			toitemsc(o, st);
			return true;
		}
		if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "몹추적") || key.equalsIgnoreCase(Common.COMMAND_TOKEN + "23")) {
			tomonsc(o, st);
			return true;
		}

		if (key.equalsIgnoreCase(".군업")) {
			if (o.getClassType() == 0x00 && o.getLevel() >= 30) {
				PcInstance pc = (PcInstance) o;

				pc.setRoyalBuff(true);

				return true;
			} else {
				ChattingController.toChatting(o, "군주캐릭 30레벨부터 사용 가능합니다.");
				return true;
			}
		}
		if (key.equalsIgnoreCase("..")) {
			PcInstance pc = (PcInstance) o;
			pc.칼렉풀기();
			return true;
		}
		if (key.equalsIgnoreCase(".매크로") || key.equalsIgnoreCase(".장사글") || key.equalsIgnoreCase(".반복멘트")) {
			macroMessage(o, st);
			return true;
		}
		if (key.equalsIgnoreCase(".장사채팅")) {
			o.setMacro_mag(!o.isMacro_mag());
			ChattingController.toChatting(o, String.format("장사채팅이 %s 활성화 되었습니다", o.isMacro_mag() ? "" : "비"), Lineage.CHATTING_MODE_MESSAGE );
			return true;
		}
		if (key.equalsIgnoreCase(".초기화")) {
			macroMessageClear(o, st);
			return true;
		}
		if (key.equalsIgnoreCase(".주기")) {
			macroPeriod(o, st);
			return true;
		}
		if (key.equalsIgnoreCase(".레벨퀘스트") || key.equalsIgnoreCase(".12")) {
			레벨퀘스트(o, st);
			return true;
		}
		if (key.equalsIgnoreCase(".드랍") || key.equalsIgnoreCase(".템드랍")) {
			dropItem2(o, st);
			return true;
		}
		if (key.equalsIgnoreCase(".몹드랍")) {
			monsterDrop(o, st);
			return true;
		}
		if (key.equalsIgnoreCase(".혈맹파티") || key.equalsIgnoreCase(".혈파")) {
			Party clanParty = null;
			boolean result = true;

			if (o.getClanId() == 0) {
				ChattingController.toChatting(o, "혈맹에 가입되어 있지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return true;
			}

			PcInstance cha = (PcInstance) o;

			try {
				Clan c = ClanController.find(cha);
				if (c != null) {
					for (PcInstance pc : c.getList()) {
						Party p = PartyController.find2(pc);

						if (p != null && pc != null && pc.getClanId() == cha.getClanId() && p.getList().size() > 1) {
							if (p.isClanParty()) {
								clanParty = p;
								break;
							}
						}
					}

					// 혈맹파티를 찾아서 혈맹파티가 있을경우 혈맹파티 명령어를 입력한 대상이 혈맹파티로 가입
					if (clanParty != null) {
						for (PcInstance clan : clanParty.getList()) {
							if (clan.getObjectId() == cha.getObjectId()) {
								result = false;
								break;
							}
						}

						if (result) {
							cha.setPartyId(clanParty.getKey());
							PartyController.toParty(cha, true, true);
						}
					} else {
						for (PcInstance pc : ClanController.find(cha).getList()) {
							if (cha.getClanId() == pc.getClanId() && cha.getObjectId() != pc.getObjectId()) {
								PartyController.toParty(cha, pc, true, false);
							}
						}
					}
				}
			} catch (Exception e) {
				ChattingController.toChatting(o, ".혈맹파티", Lineage.CHATTING_MODE_MESSAGE);
			}
			return true;
		}

		if (key.equalsIgnoreCase(".원격초대") || key.equalsIgnoreCase(".초대") || key.equalsIgnoreCase(".5")) {
			try {
				PcInstance user = World.findPc(st.nextToken());
				if (user == null) {
					ChattingController.toChatting(o, "캐릭터가 존재하지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
				} else {
					if (o.getName().equals(user.getName())) {
						ChattingController.toChatting(o, "자신은 초대할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
					} else {
						PartyController.toParty((PcInstance) o, user, false, false);
					}
				}
			} catch (Exception e) {
				ChattingController.toChatting(o, ".초대 캐릭명", Lineage.CHATTING_MODE_MESSAGE);
			}
			return true;
		}
		
	 if (key.equalsIgnoreCase(".혈맹가입")) {
		try {
			PcInstance user = World.findPc(st.nextToken());
			if (user == null) {
				ChattingController.toChatting(o, "캐릭터가 존재하지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
			} else {
				if (o.getName().equals(user.getName())) {
					ChattingController.toChatting(o, "자신에게 가입신청을 할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				} else {
					ClanController.toJoin((PcInstance) o, user);
				}
			}
		} catch (Exception e) {
			ChattingController.toChatting(o, ".혈맹가입 캐릭명", Lineage.CHATTING_MODE_MESSAGE);
		}
		return true;
	 }
		if (key.equalsIgnoreCase(".원격교환")) {
	        	trade(o, st);
		    return true;
	    }
		if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "전투") || key.equalsIgnoreCase(Common.COMMAND_TOKEN + "마크")) {
			try {
				Clan clan = ClanController.find((PcInstance) o);

				if (clan == null) {
					ChattingController.toChatting(o, "혈맹에 가입해야 사용할 수 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
					return true;
				}
				if (clan.getName().equalsIgnoreCase(Lineage.new_clan_name)) {
					ChattingController.toChatting(o, "신규혈맹은 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
					return true;
				}

				if (o.isMark) {
					o.isMark = false;

					for (Clan c : ClanController.getClanList().values()) {
						if (c != null && !c.getName().equals(clan.getName())
								&& !c.getName().equalsIgnoreCase(Lineage.new_clan_name)) {
							o.toSender(S_ClanWar.clone(BasePacketPooling.getPool(S_ClanWar.class), 3,
									clan.getName(), c.getName()));
						}
					}
				} else {
					o.isMark = true;

					for (Clan c : ClanController.getClanList().values()) {
						if (c != null && !c.getName().equals(clan.getName())
								&& !c.getName().equalsIgnoreCase(Lineage.new_clan_name)) {
							o.toSender(S_ClanWar.clone(BasePacketPooling.getPool(S_ClanWar.class), 1,
									clan.getName(), c.getName()));
						}
					}
				}
			} catch (Exception e) {
				ChattingController.toChatting(o, "혈맹에 가입해야 사용할 수 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
			return true;
		}

		if (key.equalsIgnoreCase(".나이")) {
			clanAge(o, st);
			return true;
		}

		if (key.equalsIgnoreCase(".나이초기화")) {
			clanAgeClean(o);
			return true;
		}

		if (key.equalsIgnoreCase(".나이목록")) {
			clanAgeList(o);
			return true;
		}

		if (key.equalsIgnoreCase(".게시판")) {
			readBoard(o);
			ChattingController.toChatting(o, ".게시판  또는  .111 ", 20);
			return true;
		}

		if (key.equalsIgnoreCase("-html")) {
			o.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), o, "monlist"));
			return true;
		}

	    if (key.equalsIgnoreCase(".고정신청")) {
		     fixUser(o, st);
		    return true;
	    }
	   
		if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "직위")) {
			try {
				String memberName = st.nextToken();
				int grade = Integer.valueOf(st.nextToken());
				PcInstance clanMember = World.findPc(memberName);
				Clan c = ClanController.find((PcInstance) o);

				for (Kingdom k : KingdomController.getKingdomList()) {
					if (k.isWar()) {
						ChattingController.toChatting(o, "공성 진행 중엔 직위 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return true;
					}
				}

				if (grade > 3) {
					ChattingController.toChatting(o, "직위는 3보다 클 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
					return true;
				}

				if (o.getClanGrade() > 1 && o.getClanId() != 0 && grade < 4) {
					if (o.getClanGrade() > 2 && o.getClanGrade() < 4 && grade == 3) {
						if (clanMember.getClassType() != Lineage.LINEAGE_CLASS_ROYAL) {
							ChattingController.toChatting(o, "군주 직위는 군주클래스만 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return true;
						}
						if (checkClanGrade(o)) {
							ChattingController.toChatting(o, "군주 직위는 한명만 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return true;
						}
					}
					if (o.getName().equalsIgnoreCase(memberName)) {
						ChattingController.toChatting(o, "자신에게 직위를 부여할 수 없습니다. ", Lineage.CHATTING_MODE_MESSAGE);
					} else {
						if ((o.getClanGrade() > grade && grade >= 0)
								|| (o.getClanGrade() == 3 && grade >= 0 && grade < 4)) {
							if (clanMember != null) {
								if (clanMember.getClanId() != o.getClanId()) {
									ChattingController.toChatting(o, "혈맹원이 아닙니다.", Lineage.CHATTING_MODE_MESSAGE);
								} else {
									if (o.getClanGrade() > clanMember.getClanGrade() || o.getClanGrade() > 2) {
										clanMember.setClanGrade(grade);
										c.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 768,
												o.getName(), clanMember.getName(), (grade == 0 ? "혈맹원"
														: grade == 1 ? "수호기사" : grade == 2 ? "부군주" : "군주")));
										if (o.getClanGrade() == 3 && grade == 3) {
											o.setClanGrade(2);
											c.toSender(S_ObjectChatting.clone(
													BasePacketPooling.getPool(S_ObjectChatting.class), null,
													Lineage.CHATTING_MODE_MESSAGE,
													String.format("%s님이 부군주로 임명되었습니다.", o.getName())));
										}
									} else {
										ChattingController.toChatting(o, "자신과 같거나 높은 직위에게 직위를 부여할 수 없습니다.",
												Lineage.CHATTING_MODE_MESSAGE);
									}
								}
							} else {
								// 클랜멤버가 존재하지않을때
								checkClanGrade(o, memberName, grade, c);
							}

							if (grade == 3)
								ClanController.setClanLord(memberName, c);
						} else {
							ChattingController.toChatting(o, "자신의 직위보다 높은 직위를 부여할 수 없습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							return true;
						}
					}
				} else {
					ChattingController.toChatting(o, "부군주 이상 직위를 부여할 수 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			} catch (Exception e) {
				ChattingController.toChatting(o, Common.COMMAND_TOKEN + "직위 캐릭명 등급  ex).직위 홍길동 1",
						Lineage.CHATTING_MODE_MESSAGE);
				ChattingController.toChatting(o, "[0:혈맹원] [1:수호기사] [2:부군주] [3:군주]", Lineage.CHATTING_MODE_MESSAGE);
			}
			return true;
		}
	    
		return null;
	}

	/**
	 * by all_night
	 */
	static private void setClanGrade(object o, String name, int grade) {
		Connection con = null;
		Connection conn = null;
		PreparedStatement st = null;
		PreparedStatement stt = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE characters SET clan_grade=? WHERE name=?");
			st.setInt(1, grade);
			st.setString(2, name);
			st.executeUpdate();
			st.close();

			if (o.getClanGrade() == 3 && grade == 3) {
				conn = DatabaseConnection.getLineage();
				stt = conn.prepareStatement("UPDATE characters SET clan_grade=? WHERE name=?");
				stt.setInt(1, 2);
				stt.setString(2, o.getName());
				stt.executeUpdate();
				stt.close();
			}
		} catch (Exception e) {
			lineage.share.System.println("직위 부여 UPDATE 실패 : " + e);
		} finally {
			DatabaseConnection.close(con, stt);
		}
	}

	/**
	 * by all_night
	 */
	static private void checkClanGrade(object o, String name, int grade, Clan c) {
		Connection con = null;
		PreparedStatement stt = null;
		ResultSet rs = null;
		int clanId = 0;
		int clanGrade = 0;
		String charId = null;

		try {
			con = DatabaseConnection.getLineage();
			stt = con.prepareStatement("SELECT name, clanId, clan_grade FROM characters WHERE name = ?");
			stt.setString(1, name);
			rs = stt.executeQuery();
			while (rs.next()) {
				charId = rs.getString("name");
				clanId = rs.getInt("clanId");
				clanGrade = rs.getInt("clan_grade");
			}
		} catch (Exception e) {
			lineage.share.System.println("직위 부여 디비 SELECT 실패 : " + e);
		} finally {
			DatabaseConnection.close(con, stt);
		}

		if (charId == null) {
			ChattingController.toChatting(o, "캐릭터가 존재하지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
		} else {
			if (clanId == o.getClanId()) {
				if (o.getClanGrade() > clanGrade || o.getClanGrade() > 2) {
					setClanGrade(o, charId, grade);
					c.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 768, o.getName(), charId,
							(grade == 0 ? "혈맹원" : grade == 1 ? "수호기사" : grade == 2 ? "부군주" : "군주")));
					if (o.getClanGrade() == 3 && grade == 3) {
						o.setClanGrade(2);
						c.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null,
								Lineage.CHATTING_MODE_MESSAGE, String.format("%s님이 '부군주' 로 임명되셨습니다.", o.getName())));
					}
				} else {
					// 상대방이 등급이 더 높을때
					ChattingController.toChatting(o, "자신과 같거나 높은 직위에게 직위를 부여할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			} else {
				// 혈맹이 다를때
				ChattingController.toChatting(o, "혈맹원이 아닙니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}

	/**
	 * by all_night
	 */
	static private boolean checkClanGrade(object o) {
		// npc 이름은 npc_spawnlist에 있는 name 값으로 입력.
		// 예를 들어 '버질 1888' 이면 띄어쓰기까지 정확하게 입력.
		boolean result = false;
		String name = null;
		int clanGrade = 0;
		Connection con = null;
		PreparedStatement stt = null;
		ResultSet rs = null;

		for (PcInstance clanMember : World.getPcList()) {
			if (clanMember.getClanId() == o.getClanId() && !o.getName().equals(clanMember.getName())
					&& clanMember.getClanGrade() == 3) {
				result = true;
				break;
			}
		}

		if (!result) {
			try {
				con = DatabaseConnection.getLineage();
				stt = con.prepareStatement("SELECT name, clan_grade FROM characters WHERE clanId = ?");
				stt.setLong(1, o.getClanId());
				rs = stt.executeQuery();
				while (rs.next()) {
					name = rs.getString("name");
					clanGrade = rs.getInt("clan_grade");
					if (!name.equals(o.getName()) && clanGrade == 3) {
						result = true;
						break;
					}
				}
			} catch (Exception e) {
				lineage.share.System.println("혈맹 직위 체크 디비 SELECT 실패 : " + e);
			} finally {
				DatabaseConnection.close(con, stt);
			}
		}
		return result;
	}

	/**
	 * PcInventory에서 아이템 사용요청 처리 구간 에서 호출해서 사용함. : 아이템 사용을 하기전에 상점판매목록에 갱신해야 되는지
	 * 확인함. : 성공 여부 리턴하여 그에따라 인벤토리아이템 처리요청구간을 수행함.
	 * 
	 * @param pc
	 * @param item
	 * @return
	 */
	static public boolean isShopToAppend(PcInstance pc, ItemInstance item) {
		// 초기화 안된거 무시.
		PcShopInstance pc_shop = shop_list.get(pc.getObjectId());
		if (pc_shop == null)
			return false;

		// 등록된 아이템은 사용할 수 없음.
		for (shop s : pc_shop.list.values())
			if (s.getInvItemObjectId() == item.getObjectId()) {
				ChattingController.toChatting(pc, "\\fY무인상점에 등록한 아이템은 사용할 수 없습니다.", 20);
				return true;
			}
		for (shop s : pc_shop.list_sell.values())
			if (s.getInvItemObjectId() == item.getObjectId()) {
				ChattingController.toChatting(pc, "\\fY무인상점에 등록한 아이템은 사용할 수 없습니다.", 20);
				return true;
			}

		// 더이상 등록할 공간이 없는건 일반 아이템 사용하듯 처리.
		if (pc_shop.list.get(0L) == null && pc_shop.list_sell.get(0L) == null)
			return false;

		// 착용 한거 무시.
		if (item.isEquipped())
			return false;

		// 거래 안되는 아이템은 무시.
		if (!item.getItem().isTrade())
			return false;

		// 아데나 등록 못하도록 처리
		if (item.getItem().getNameIdNumber() == 4)
			return false;

		boolean buy = pc_shop.list.get(0L) != null;
		shop s = buy ? pc_shop.list.get(0L) : pc_shop.list_sell.get(0L);
		pc_shop.list.remove(0L);
		pc_shop.list_sell.remove(0L);
		s.setItem(item.getItem());
		s.setInvItemObjectId(item.getObjectId());
		s.setInvItemCount(item.getCount());
		s.setInvItemEn(item.getEnLevel());
		s.setInvItemBress(item.getBress());
		s.setInvItemDefinite(item.isDefinite());
		if (buy)
			pc_shop.list.put(s.getInvItemObjectId(), s);
		else
			pc_shop.list_sell.put(s.getInvItemObjectId(), s);
		ChattingController.toChatting(pc,
				"\"" + item.getItem().getName() + " (" + s.getPrice() + ")\"\\fY를 상점에 추가하였습니다.", 20);
		return true;
	}

	static public void attackDelay(object o) {
		//
		if (o.isLock())
			return;
		// 딜레이 적용하기.
		Long time = delay_temp_list.get(o.getObjectId());
		if (time == null || time == 0 || time <= System.currentTimeMillis()) {
			delay_temp_list.put(o.getObjectId(), System.currentTimeMillis() + (1000 * 10));

			o.toSender(S_ObjectMap.clone(BasePacketPooling.getPool(S_ObjectMap.class), o));
			o.toSender(S_ObjectAdd.clone(BasePacketPooling.getPool(S_ObjectAdd.class), o, o));
			for (object obj : o.getInsideList(true))
				o.toSender(S_ObjectAdd.clone(BasePacketPooling.getPool(S_ObjectAdd.class), obj, o));
		}
	}

	static private void readBoard(object o) {
		try {
			List<Board> list = new ArrayList<Board>();
			// 해당 게시판의 목록 추출.
			BoardController.getList("danbi", BoardController.getMaxUid("danbi"), list, 8);
			// 패킷 처리
			o.toSender(S_BoardList.clone(BasePacketPooling.getPool(S_BoardList.class), null, list));
			// 메모리 정리.
			for (Board b : list)
				BoardController.setPool(b);
			list.clear();
		} catch (Exception e) {
			ChattingController.toChatting(o, ".게시판", 20);
		}
	}

	static private void clanAgeList(object o) {
		try {
			String list_str = sp.controller.ClanController.getAgeList(o);
			ChattingController.toChatting(o, list_str, 20);
		} catch (Exception e) {
			ChattingController.toChatting(o, ".나이목록", 20);
		}
	}

	static private void clanAgeClean(object o) {
		try {
			sp.controller.ClanController.clear(o);
		} catch (Exception e) {
			ChattingController.toChatting(o, ".나이초기화", 20);
		}
	}

	static private void clanAge(object o, StringTokenizer st) {
		try {
			// 혈맹원 이름
			String name = st.nextToken();
			// 나이값
			Integer age = Integer.valueOf(st.nextToken());
			// 설정.
			sp.controller.ClanController.setAge(o, name, age);
		} catch (Exception e) {
			ChattingController.toChatting(o, ".나이 [캐릭명] [나이]", 20);
		}
	}

	static private void monsterDrop(object o, StringTokenizer st) {
		try {
			long l1 = java.lang.System.currentTimeMillis() / 1000L;
//			if (o.getDelaytime() + 5L > l1) {
//				long l2 = o.getDelaytime() + 5L - l1;
//				ChattingController.toChatting(o, new StringBuilder().append(l2).append("초간의 지연시간이 필요합니다.").toString(),
//						20);
//				return;
//			}
			String str1 = st.nextToken();
					Monster m = MonsterDatabase.find3(str1);

					if (m != null) {
						if (m.getDropList().size() > 0) {
							o.toSender(S_MonDrop.clone(BasePacketPooling.getPool(S_MonDrop.class), m));
						} else {
							ChattingController.toChatting(o, "해당 몬스터는 드랍목록이 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
						}
					}
//				}

//				ChattingController.toChatting(o, Common.HELPER_LINE, 20);
//			} catch (Exception localException2) {
//				lineage.share.System.printf("%s : monsterDrop(object o, StringTokenizer st)\r\n",
//						new Object[] { CommandController.class.toString() });
//				lineage.share.System.println(localException2);
//			} finally {
//				DatabaseConnection.close(localConnection, localPreparedStatement, localResultSet);
//			}
			o.setDelaytime(l1);
		} catch (Exception localException1) {
			if (o != null)
				ChattingController.toChatting(o, ".몹드랍 몬스터명", 20);
		}
	}

	/**
	 * 원격 교환.
	 */
	public static void trade(object o, StringTokenizer st) {
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			String name = null;
			
			try {
				name = st.nextToken();
				PcInstance use = World.findPc(name);
				//야도란 자신한테는 교환 불가능
				if (name.equals(pc.getName())) {
					ChattingController.toChatting(o, "자신에게 거래 신청은 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if (use != null && !(use instanceof PcRobotInstance)) {
					if (!use.isWorldDelete() && !use.isDead() && !use.isLock() && use.getInventory() != null) {
						if (!pc.isBuffCriminal() && !use.isBuffCriminal()) {
							if (!pc.isTransparent() && !use.isTransparent())
								TradeController.toTrade(pc, use);
						} else {
							ChattingController.toChatting(o, "전투중일 경우 교환이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
					}
				} else {
				
					if (use == null) {
						if (name != null)
							ChattingController.toChatting(o, String.format("[%s] 캐릭터는 존재하지 않습니다.", name), Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
				}
			} catch (Exception e) {
				ChattingController.toChatting(o, ".원격교환 캐릭명", 20);
				return;
			}
		}
	}

	private static void toitemsc(object o, StringTokenizer st) {
		try {

		} catch (Exception e) {
			ChattingController.toChatting(o, ".템추적 아이템이름  [.22 아이템이름]", 20);
		}
	}

	private static void tomonsc(object o, StringTokenizer st) {
		try {
			String str = st.nextToken();
			if (st.hasMoreTokens()) {
				str = new StringBuilder().append(str).append(" ").append(st.nextToken()).toString();
			}
			o.setBoardView(false);
			String[] arrayOfString = new String[4];
			arrayOfString[0] = "이벤트 서버 \n";
			arrayOfString[1] = "* 몬스터 추적 정보 * \n";

			StringBuilder b = new StringBuilder();

			Monster mon = MonsterDatabase.find3(str);

			// MonsterSpawnlistDatabase

			b.append(new StringBuilder().append("몬스터 이름 : ").append(mon.getName()).append("\n").toString());
			b.append(new StringBuilder().append("\n").toString());
			b.append(new StringBuilder().append("몬스터 스폰 장소 : ").append("\n").toString());

		} catch (Exception e) {
			ChattingController.toChatting(o, ".몹추적 몬스터이름  [.23 몬스터이름]", 20);
		}
	}

	private static void toItemven(object o, StringTokenizer st) {
		try {
			String str = st.nextToken();
			if (st.hasMoreTokens()) {
				str = new StringBuilder().append(str).append(" ").append(st.nextToken()).toString();
			}
			o.setBoardView(false);
			String[] arrayOfString = new String[4];

			arrayOfString[0] = "서버 \n";
			arrayOfString[1] = "* 아 이 템  정 보 * \n";

			StringBuilder b = new StringBuilder();

			Item item = ItemDatabase.find4(str);

			b.append(new StringBuilder().append("이름 : ").append(item.getName()).append("\n").toString());
			b.append(new StringBuilder().append("\n").toString());
			b.append(new StringBuilder().append("안전인첸 : ").append(item.getSafeEnchant()).append("\n").toString());
			b.append(new StringBuilder().append("작은 몹(pvp) : ").append(item.getDmgMin()).append("\n").toString());
			b.append(new StringBuilder().append("큰 몹 : ").append(item.getDmgMax()).append("\n").toString());
			b.append(new StringBuilder().append("[추타] : ").append(item.getAddDmg()).append("\n").toString());
			b.append(new StringBuilder().append("[근거리 공격 성공률] : ").append(item.getAddHit()).append("\n").toString());
			b.append(new StringBuilder().append("[원거리 공격 성공률] : ").append(item.getAddBowHit()).append("\n").toString());

			StringBuffer sb = new StringBuffer();
			if (item.getRoyal() != 0) {
				sb.append("[군]");
			}
			if (item.getKnight() != 0) {
				sb.append("[기]");
			}
			if (item.getElf() != 0) {
				sb.append("[요]");
			}
			if (item.getWizard() != 0) {
				sb.append("[마]");
			}
			if (item.getDarkElf() != 0) {
				sb.append("[다]");
			}
			b.append(new StringBuilder().append("착용 :").append(sb.toString()).append("\n").toString());
			b.append(new StringBuilder().append("\n").toString());
			b.append(new StringBuilder().append("[방어력]AC : ").append(item.getAc()).append("\n").toString());

			b.append(new StringBuilder().append("[ 힘 ]Str : ").append(item.getAddStr() + " ").append("[민첩]Dex : ")
					.append(item.getAddDex()).append("\n").toString());

			b.append(new StringBuilder().append("[체력]Con : ").append(item.getAddCon() + " ").append("[지력]Int : ")
					.append(item.getAddInt()).append("\n").toString());

			b.append(new StringBuilder().append("[지혜]Wis : ").append(item.getAddWis() + " ").append("[카리]Cha : ")
					.append(item.getAddCha()).append("\n").toString());

			b.append(new StringBuilder().append("[스펠파워]Sp : ").append(item.getAddSp()).append("\n").toString());
			b.append(new StringBuilder().append("[마법방어]Mr : ").append(item.getAddMr()).append("\n").toString());
			b.append(new StringBuilder().append("[피틱] : ").append(item.getTicHp() + " ").append("[엠틱] : ")
					.append(item.getTicMp()).append("\n").toString());

			arrayOfString[2] = b.toString();

			o.toSender(S_LetterNotice.clone(BasePacketPooling.getPool(S_LetterNotice.class), arrayOfString));
		} catch (Exception e) {
			ChattingController.toChatting(o, ".아이템정보 아이템이름 / .33 아이템이름", 20);
		}

	}

	// 메크로 메세지.
	private static void macroMessage(object paramobject, StringTokenizer paramStringTokenizer) {
		try {
			String str = null;
			for (str = paramStringTokenizer.nextToken(); paramStringTokenizer.hasMoreTokens(); str = new StringBuilder()
					.append(str).append(" ").append(paramStringTokenizer.nextToken()).toString())
				;
			paramobject.getMacroMessage().add(str);
			ChattingController.toChatting(paramobject,
					new StringBuilder().append("매크로 [").append(str).append("] 설정하였습니다.").toString(), 20);
			ChattingController.toChatting(paramobject, "매크로 주기를 설정해주세요. .주기 (299이상)", 20);
		} catch (Exception localException) {
			if (paramobject != null)
				ChattingController.toChatting(paramobject, ".매크로 [메세지]", 20);
		}
	}

	private static void macroMessageClear(object paramobject, StringTokenizer paramStringTokenizer) {
		try {
			paramobject.getMacroMessage().clear();
			ChattingController.toChatting(paramobject, "매크로 메세지를 초기화하였습니다.", 20);
		} catch (Exception localException) {
			if (paramobject != null)
				ChattingController.toChatting(paramobject, ".초기화", 20);
		}
	}

	private static void macroPeriod(object paramobject, StringTokenizer paramStringTokenizer) {

		try {
			int i = Integer.parseInt(paramStringTokenizer.nextToken());

			if (i < 299) {
				ChattingController.toChatting(paramobject, "최소 299 이상으로 설정해 주십시요.(초단위)", 20);
				return;
			}

			paramobject.setMacroPeriod(i);
			ChattingController.toChatting(paramobject,
					new StringBuilder().append("매크로 주기 [").append(i).append("]로 설정하였습니다.").toString(), 20);
			ChattingController.toChatting(paramobject, "메크로를 더이상 사용하지않으시려면 .초기화 를입력해주세요.", 20);
		} catch (Exception localException) {
			if (paramobject != null)
				ChattingController.toChatting(paramobject, ".주기 [수치 (숫자로만 입력,299 이상(초단위))]", 20);
		}
	}

	/**
	 * 레벨퀘스트 수정 feel.
	 * 
	 * @param o
	 * @param st
	 */
	static private void 레벨퀘스트(object o, StringTokenizer st) {
		PcInstance pc = null;
		if (o instanceof PcInstance)
			pc = (PcInstance) o;
		if (pc == null)
			return;
		if (!Lineage.is_level_quest) {
			ChattingController.toChatting(pc, "현재 서버는 레벨퀘스트를 할 수 없습니다. 운영자에게 문의하세요", 20);
			return;
		}

		String result = null;
		// StringTokenizer st1 = new StringTokenizer(st);
		if (st.hasMoreTokens())
			result = st.nextToken();
		if (result == null) { // 현재 진행중인 레벨퀘스트 관련 내용을 모두 출력한다.
			ArrayList<PcLevelQuest> list = pc.getLevelQuestList();
			if (list.size() > 0) {
				ChattingController.toChatting(pc, "현재 아래와 같은 퀘스트가 진행되고 있습니다.", 20);
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < list.size(); i++) {
					sb.append("\\fY");
					sb.append(list.get(i).getQuestId());
					sb.append("- ").append(list.get(i).getMessage());
					ChattingController.toChatting(pc, sb.toString(), 20);
					sb.delete(0, sb.length());
				}
				ChattingController.toChatting(pc,
						"\\fT각 퀘스트의  진행현황 상세정보는 .12 [번호] \n \\fT명령어를 사용하시면 확인하실 수 있습니다. (.12)", 20);
			} else {
				ChattingController.toChatting(pc, "현재 진행중인 퀘스트가 없습니다.", 20);
				ChattingController.toChatting(pc, "레벨퀘스트는 레벨업시에 수행할 퀘스트가 생긴다면 자동으로 추가됩니다.", 20);
			}
		} else { // 입력된게 있다면 상세정보를 보여준다.
			int questid = 0;
			try {
				questid = Integer.parseInt(result);
			} catch (Exception e) {
				String ment = "퀘스트 번호는 숫자로만 입력해주세요.";
				pc.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), pc, 2, ment));
				return;
			}
			PcLevelQuest lvQuest = null;
			for (int i = 0; i < pc.getLevelQuestList().size(); i++) {
				if (questid == pc.getLevelQuestList().get(i).getQuestId()) {
					lvQuest = pc.getLevelQuestList().get(i);
					break;
				}
			}
			StringBuilder sb = new StringBuilder();
			if (lvQuest == null) {
				sb.append("\\fY");
				sb.append(questid).append("번 퀘스트는 현재 진행중인 퀘스트가 아닙니다.");
				ChattingController.toChatting(pc, sb.toString(), 20);
				sb.delete(0, sb.length());
				return;
			} else {
				pc.toSender(S_QuestView.clone(BasePacketPooling.getPool(S_QuestView.class), pc, lvQuest));
			}
		}
	}

	static public void inventorySetting(object o) {
		try {
		List<ItemInstance> list = o.getInventory().getList();
		List<ItemInstance> tempList = new ArrayList<ItemInstance>();

		for (ItemInstance i : list) {
			if (i.getItem().getName().equalsIgnoreCase("자동 칼질")) {
				if (!tempList.contains(i)) {
					tempList.add(i);
					o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
					break;
				}
			}
		}
		
		for (ItemInstance i : list) {
			if (i.getItem().getName().equalsIgnoreCase("몬스터 정보")) {
				if (!tempList.contains(i)) {
					tempList.add(i);
					o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
					break;
				}
			}
		}
		
		for (ItemInstance i : list) {
			if (i.getItem().getName().equalsIgnoreCase("아이템 정보")) {
				if (!tempList.contains(i)) {
					tempList.add(i);
					o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
					break;
				}
			}
		}
		
		for (ItemInstance i : list) {
			if (i.getItem().getName().equalsIgnoreCase("조우의 이동 기억책")) {
				if (!tempList.contains(i)) {
					tempList.add(i);
					o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
					break;
				}
			}
		}
		
		for (ItemInstance i : list) {
			if (i.getItem().getName().equalsIgnoreCase("아데나")) {
				if (!tempList.contains(i)) {
					tempList.add(i);
					o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
					break;
				}
			}
		}
		
		for (ItemInstance i : list) {
			if (i.getItem().getName().equalsIgnoreCase("군주지원")) {
				if (!tempList.contains(i)) {
					tempList.add(i);
					o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
					break;
				}
			}
		}
		for (ItemInstance i : list) {
			if (i.getItem().getName().equalsIgnoreCase("기사지원")) {
				if (!tempList.contains(i)) {
					tempList.add(i);
					o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
					break;
				}
			}
		}
		for (ItemInstance i : list) {
			if (i.getItem().getName().equalsIgnoreCase("요정지원")) {
				if (!tempList.contains(i)) {
					tempList.add(i);
					o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
					break;
				}
			}
		}
		for (ItemInstance i : list) {
			if (i.getItem().getName().equalsIgnoreCase("법사지원")) {
				if (!tempList.contains(i)) {
					tempList.add(i);
					o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
					break;
				}
			}
		}
		// 착용중인 무기
		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("sword") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("tohandsword") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("claw") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}
		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("edoryu") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}
		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("dagger") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("bow") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}

		for (ItemInstance i : list) {
			if (i instanceof Arrow) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("wand") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}

		for (ItemInstance i : list) {
			if (i instanceof ItemWeaponInstance && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}

		// 미착용 무기
		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("sword")) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("tohandsword")) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("bow")) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("wand")) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		for (ItemInstance i : list) {
			if (i instanceof ItemWeaponInstance) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 착용중인 방어구
		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("helm") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("t") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("armor") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("cloak") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("glove") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("shield") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("boot") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("necklace") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("ring") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("belt") && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}

		for (ItemInstance i : list) {
			if (i instanceof ItemArmorInstance && i.isEquipped()) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 미착용 방어구
		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("helm")) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("t")) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("armor")) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("cloak")) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("glove")) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("shield")) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("boot")) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("necklace")) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("ring")) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		for (ItemInstance i : list) {
			if (i.getItem().getType2().equalsIgnoreCase("belt")) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		for (ItemInstance i : list) {
			if (i instanceof ItemArmorInstance) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 물약
		for (ItemInstance i : list) {
			if (i instanceof HealingPotion) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 초록 물약
		for (ItemInstance i : list) {
			if (i instanceof HastePotion) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 용기 물약
		for (ItemInstance i : list) {
			if (i instanceof BraveryPotion) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 와퍼
		for (ItemInstance i : list) {
			if (i instanceof ElvenWafer) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 지혜 물약
		for (ItemInstance i : list) {
			if (i instanceof WisdomPotion) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 파란 물약
		for (ItemInstance i : list) {
			if (i instanceof BluePotion) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 해독제
		for (ItemInstance i : list) {
			if (i instanceof CurePoisonPotion) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		// 카오틱 물약
		for (ItemInstance i : list) {
			if (i instanceof LawfulPotion) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}
		// 변신 주문서
		for (ItemInstance i : list) {
			if (i instanceof ScrollPolymorph) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 변신 주문서
		for (ItemInstance i : list) {
			if (i instanceof ScrollPolymorph3) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 순간이동 주문서
		for (ItemInstance i : list) {
			if (i instanceof ScrollLabeledVenzarBorgavve) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		for (ItemInstance i : list) {
			if (i.getItem().getName().equalsIgnoreCase("기란 마을 이동 부적")) {
				if (!tempList.contains(i)) {
					tempList.add(i);
					o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
					break;
				}
			}
		}
		// 귀환 주문서
		for (ItemInstance i : list) {
			if (i instanceof ScrollLabeledVerrYedHorae) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 혈맹 귀환 주문서
		for (ItemInstance i : list) {
			if (i instanceof ScrollLabeledVerrYedHoraePledgeHouse) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 확인 주문서
		for (ItemInstance i : list) {
			if (i instanceof ScrollLabeledKernodwel) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
				break;
			}
		}
		// 부활 주문서
		for (ItemInstance i : list) {
			if (i instanceof ScrollResurrection) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 저주 풀기 주문서
		for (ItemInstance i : list) {
			if (i instanceof ScrollLabeledPratyavayah) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 데이
		for (ItemInstance i : list) {
			if (i instanceof ScrollLabeledDaneFools) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 젤
		for (ItemInstance i : list) {
			if (i instanceof ScrollLabeledZelgoMer) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}

		// 이동 주문서 및 부적
		for (ItemInstance i : list) {
			if (i instanceof ScrollTeleport) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 오만의 탑 이동 주문서
		for (ItemInstance i : list) {
			if (i instanceof TOITeleportScroll) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 마법인형
		for (ItemInstance i : list) {
			if (i instanceof MagicDoll) {
				if (!tempList.contains(i))
					tempList.add(i);
				o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));
			}
		}
		// 마법서류
		for (ItemInstance i : list) {
			if (i != null && i.getItem() != null && (i.getItem().getName().contains("마법서") || i.getItem().getName().contains("기술서") || i.getItem().getName().contains("정령의 수정"))) {
				if (!tempList.contains(i))
					tempList.add(i);
			}
		}
		// 기타 아이템
		for (ItemInstance i : list) {
			if (!tempList.contains(i))
				tempList.add(i);
		}

		for (ItemInstance i : tempList)
			o.toSender(S_InventoryDelete.clone(BasePacketPooling.getPool(S_InventoryDelete.class), i));

		for (ItemInstance i : tempList)
			o.toSender(S_InventoryAdd.clone(BasePacketPooling.getPool(S_InventoryAdd.class), i));
	} catch (Exception e) {
	}
}

	
	/**
	 * 고정신청 설정
	 * 
	 * @param o
	 * @param st
	 * by 가이아
	 */

	@SuppressWarnings("resource")
	static private void fixUser(object o, StringTokenizer st) {
		try {
			long l1 = java.lang.System.currentTimeMillis() / 1000L;
			if (o.getDelaytime() + 5L > l1) {
				long l2 = o.getDelaytime() + 5L - l1;
				ChattingController.toChatting(o, new StringBuilder().append(l2)
						.append("초간의 지연시간이 필요합니다.").toString(),
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if ((o.getLevel() < 10)) {
				ChattingController.toChatting(o, "10레벨이후에.. 고정신청이 가능합니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			String str = st.nextToken();
			str = str.replaceAll("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", "");
			if ((str.length() > 11) || (str.length() < 10)) {
				ChattingController.toChatting(o, "핸드폰 번호를 잘못 입력하였습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			try {
				Integer.parseInt(str);
			} catch (Exception localException2) {
				ChattingController.toChatting(o, "핸드폰 번호를 잘못 입력하였습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			Connection localConnection = null;
			PreparedStatement localPreparedStatement = null;
			ResultSet localResultSet = null;
			try {
				localConnection = DatabaseConnection.getLineage();
				localPreparedStatement = localConnection
						.prepareStatement("SELECT * FROM member WHERE 계정=?");
				localPreparedStatement.setString(1, ((PcInstance) o)
						.getClient().getAccountId());
				localResultSet = localPreparedStatement.executeQuery();
				if (!localResultSet.next()) {
					localPreparedStatement = localConnection
							.prepareStatement("INSERT INTO member SET 계정=?, 캐릭터=?, 연락처=?, 고정신청날짜=?");
					localPreparedStatement.setString(1, ((PcInstance) o)
							.getClient().getAccountId());
					localPreparedStatement.setString(2, o.getName());
					localPreparedStatement.setString(3, str);
					localPreparedStatement.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
					localPreparedStatement.execute();
					ChattingController.toChatting(o, "고정맴버 신청을 해주셔서 감사합니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				} else {
					ChattingController.toChatting(o, "이미 고정맴버로 등록 되어있습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
			} catch (Exception localException3) {
				lineage.share.System.println(localException3);
			} finally {
				DatabaseConnection.close(localConnection,
						localPreparedStatement, localResultSet);
			}
			o.setDelaytime(l1);
		} catch (Exception localException1) {
			if (o != null)
				ChattingController.toChatting(o, ".고정신청 전화번호",
						20);
		}
	}

	static private void dropItem2(object o, StringTokenizer st) {
		try {
			long l1 = java.lang.System.currentTimeMillis() / 1000L;
			if (o.getDelaytime() + 5L > l1) {
				long l2 = o.getDelaytime() + 5L - l1;
				ChattingController.toChatting(o, new StringBuilder().append(l2).append("초간의 지연시간이 필요합니다.").toString(),
						20);
				return;
			}
			boolean result = false;
			String itemName = st.nextToken();

			List<String> info = new ArrayList<String>();
			info.clear();
			List<String> monlist = new ArrayList<String>();
			info.clear();

			info.add("[" + itemName + "]");

			if (itemName != null) {
				int num = 1;
				String name = null;
				String item = null;
				info.add("========== 드랍 목록 ==========");
				for (Drop d : MonsterDropDatabase.getList()) {
					if (d.getItemName().replace(" ", "").contains(itemName)) {
						result = true;
						name = d.getMonName();
						item = d.getItemName();
						if (name.contains("[잊혀진섬]"))
							name = name.replace("[잊혀진섬]", "");
						if (name.contains("(철퇴)"))
							name = name.replace("(철퇴)", "");
						if (name.contains("(도끼)"))
							name = name.replace("(도끼)", "");
						if (name.contains("(마법)"))
							name = name.replace("(마법)", "");
						if (name.contains("(창)"))
							name = name.replace("(창)", "");
						if (name.contains("(검)"))
							name = name.replace("(검)", "");
						if (name.contains("(포악)"))
							name = name.replace("(포악)", "");
						if (name.contains(" "))
							name = name.replace(" ", "");

						info.add(String.format(" [%s%s]", d.getItemBress() == 0 ? "(축)" : "", item));
						info.add(" [" + name + "]");
						// .드랍시에 추가되는 글 밑에
						// 방금 만든 몬스터 리스트를 추가해줄 소스를 넣는다
						monlist.add(d.getMonName());
//						MonsterDrop.monster.add(name);
						num++;
						if (num == 101)
							break;
					}
				}
				if (!result) {
					info.add("검색된 결과가 없습니다.");
				}
				for (int ww = 0; ww < 210 - (info.size() / 2); ww++) {
					info.add(" ");
				}
				if (monlist.size() > 0)
					JsnSoft.Monster_list.put(o.getObjectId(), monlist);
				o.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), o, "cmddrop", null, info));

			}
			o.setDelaytime(l1);
		} catch (Exception localException1) {
			if (o != null)
				ChattingController.toChatting(o, ".드랍 아이템명", 20);
		}
	}

}
