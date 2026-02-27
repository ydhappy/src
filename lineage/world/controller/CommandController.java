package lineage.world.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;








































//import org.eclipse.swt.widgets.Shell;
import cholong.util.RealTimeClock;
import jsn_soft.S_InventoryFind;
import lineage.bean.database.Boss;
import lineage.bean.database.BossSpawn;
import lineage.bean.database.DungeonPartTime;
import lineage.bean.database.Exp;
import lineage.bean.database.Item;
import lineage.bean.database.Monster;
import lineage.bean.database.Npc;
import lineage.bean.database.PcShop;
import lineage.bean.database.PcTrade;
import lineage.bean.database.marketPrice;
import lineage.bean.lineage.Board;
import lineage.bean.lineage.Buff;
import lineage.bean.lineage.BuffInterface;
import lineage.bean.lineage.Clan;
import lineage.bean.lineage.Inventory;
import lineage.bean.lineage.Kingdom;
import lineage.bean.lineage.Rank;
import lineage.bean.lineage.Quest;
import lineage.bean.lineage.QuestToday;
import lineage.bean.util.MyComparator;
import lineage.database.AccountDatabase;
import lineage.database.BackgroundDatabase;
import lineage.database.BadIpDatabase;
import lineage.database.CharactersDatabase;
import lineage.database.DatabaseConnection;
import lineage.database.DungeonDatabase;
import lineage.database.DungeonPartTimeDatabase;
import lineage.database.ExpDatabase;
import lineage.database.ExpPaneltyDatabase;
import lineage.database.HackNoCheckDatabase;
import lineage.database.ItemBundleDatabase;
import lineage.database.ItemChanceBundleDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ItemSetoptionDatabase;
import lineage.database.ItemSkillDatabase;
import lineage.database.ItemTeleportDatabase;
import lineage.database.MonsterBossSpawnlistDatabase;
import lineage.database.MonsterDatabase;
import lineage.database.MonsterDropDatabase;
import lineage.database.MonsterSkillDatabase;
import lineage.database.MonsterSpawnlistDatabase;
import lineage.database.NpcDatabase;
import lineage.database.NpcShopDatabase;
import lineage.database.NpcSpawnlistDatabase;
import lineage.database.NpcTeleportDatabase;
import lineage.database.PolyDatabase;
import lineage.database.QuestPresentDatabase;
import lineage.database.QuizQuestionDatabase;
import lineage.database.ServerDatabase;
import lineage.database.ServerMessageDatabase;
import lineage.database.ServerNoticeDatabase;
import lineage.database.SkillDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.database.SummonListDatabase;
import lineage.database.TeleportHomeDatabase;
import lineage.database.TeleportResetDatabase;
import lineage.gui.dialog.MonsterSpawn;
import lineage.network.LineageServer;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.network.packet.server.S_Ability;
import lineage.network.packet.server.S_BlueMessage;
import lineage.network.packet.server.S_BoardView;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ClanWar;
import lineage.network.packet.server.S_Disconnect;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_InventoryAdd;
import lineage.network.packet.server.S_InventoryDelete;
import lineage.network.packet.server.S_LetterNotice;
import lineage.network.packet.server.S_MarblesIvenInfo;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectEffectLocation;
import lineage.network.packet.server.S_ObjectGfx;
import lineage.network.packet.server.S_ObjectHitratio;
import lineage.network.packet.server.S_ObjectLock;
import lineage.network.packet.server.S_SkillListWarrior;
import lineage.network.packet.server.S_SoundEffect;
import lineage.network.packet.server.S_Weather;
import lineage.network.packet.server.S_WorldTime;
import lineage.persnal_shop.PersnalShopInstance;
import lineage.persnal_shop.PersnalShopItem;
import lineage.persnal_shop.S_CharAutoShop;
import lineage.persnal_shop.S_PersnalShopPriceSetting;
import lineage.plugin.PluginController;
import lineage.share.Common;
import lineage.share.GameSetting;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.thread.AiThread;
import lineage.util.Shutdown;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.PcMarketController;
import lineage.world.controller.DogRaceController;
import lineage.world.controller.SlimeRaceController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.BoardInstance;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.PcShopInstance;
import lineage.world.object.item.Arrow;
import lineage.world.object.item.MagicDoll;
import lineage.world.object.item.potion.BluePotion;
import lineage.world.object.item.potion.BraveryPotion;
import lineage.world.object.item.potion.BraveryPotion2;
import lineage.world.object.item.potion.CurePoisonPotion;
import lineage.world.object.item.potion.ElvenWafer;
import lineage.world.object.item.potion.HastePotion;
//import lineage.world.object.item.L1TreasureBox;
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
import lineage.world.object.item.scroll.ScrollPolymorph5;
import lineage.world.object.item.scroll.ScrollResurrection;
import lineage.world.object.item.scroll.ScrollTeleport;
import lineage.world.object.item.scroll.TOITeleportScroll;
import lineage.world.object.magic.AquaProtect;
import lineage.world.object.magic.BlessWeapon;
import lineage.world.object.magic.AdvanceSpirit;
import lineage.world.object.magic.ChattingClose;
import lineage.world.object.magic.EnchantDexterity;
import lineage.world.object.magic.EnchantMighty;
import lineage.world.object.magic.FireWeapon;
import lineage.world.object.magic.Haste;
import lineage.world.object.magic.ImmuneToHarm;
import lineage.world.object.magic.IronSkin;
import lineage.world.object.magic.ShapeChange;
import lineage.world.object.magic.WindShot;

public final class CommandController {

	static public String TOKEN = "-";
	static private Map<Long, String> memory_cmd;
	static private ConcurrentHashMap<String, Long> command_delay;
	static private int op = 0;
	static private long checkTime;

	static private boolean letter = false;
	static {
	command_delay = new ConcurrentHashMap<String, Long>();
	}
	static public void init() {
		TimeLine.start("CommandController..");

		memory_cmd = new HashMap<Long, String>();

		TimeLine.end();
	}

	// 여기에 저희가 아까 백그라운드 데이터베이스에 있는거 넣었는데 이거 자체가 디버깅오류가 나서
	static public BoardInstance adenaBoard = null;

	/**
	 * 명령어 재사용 딜레이 확인.
	 * 
	 * @param o
	 * @param key
	 * @param check_delay
	 * @return
	 */
	static public boolean isCommandDelay(object o, String key, int check_delay) {
		//
		long time = System.currentTimeMillis();
		Long delay = null;
		delay = command_delay.get(String.format("%d_%s", o.getObjectId(), key));
		//
		if (delay != null && time - delay < check_delay * 1000) {
			long t = (delay + (check_delay * 1000)) - time;
			ChattingController.toChatting(o, String.format("%d초 후 재사용 할 수 있습니다.", (int) (t / 1000)),
					Lineage.CHATTING_MODE_MESSAGE);
			return false;
		}
		//
		command_delay.put(String.format("%d_%s", o.getObjectId(), key), time);
		return true;
	}
	
	/**
	 * 명령어 처리 함수
	 * 
	 * @param pc
	 *            : 명령어 요청자
	 * @param cmd
	 *            : 명령어
	 * @return : 명령어 수행 성공 여부
	 */
	static public boolean toCommand(object o, String cmd) {
		if (o == null)
			return false;
		if (cmd.startsWith(Common.COMMAND_TOKEN) == false)
			return false;

		try {
			StringTokenizer st = new StringTokenizer(cmd);
			String key = st.nextToken();
			if (key.equalsIgnoreCase(Common.COMMAND_TOKEN)) {
				cmd = memory_cmd.get(o.getObjectId());
				st = new StringTokenizer(cmd);
				key = st.nextToken();
			} else {
				memory_cmd.put(o.getObjectId(), cmd);
			}

			Object is_check = PluginController.init(CommandController.class,
					"toCommand", o, key, st);
			if (is_check != null)
				return (Boolean) is_check;

			// 유저 사용 명령어
			if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "상점")) {
				if (o.isSound())
					try {
						if (!ismarketLocation(o)) {
							ChattingController.toChatting(o,
									"개인상점은 시장에서만 사용가능합니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							return true;
						}

						String type = st.nextToken();
						if (type.equalsIgnoreCase("시작")
								|| type.equalsIgnoreCase("판매")) {

							PersnalShopInstance psi = World.findPersnalShop(o
									.getObjectId());

							if (psi != null) {
								ChattingController.toChatting(o,
										"\\fY이미 무인상점이 배치 되어 있습니다.",
										Lineage.CHATTING_MODE_MESSAGE);
								return true;
							}

							o.toSender(S_ObjectLock.clone(BasePacketPooling
									.getPool(S_ObjectLock.class), 22));
							o.setPersnalShopInsert(true);
							o.toSender(S_CharAutoShop.clone(BasePacketPooling
									.getPool(S_CharAutoShop.class),
									(PcInstance) o, o.getInventory().getList()));
							ChattingController.toChatting(o,
									"\\fY상점 배치 혹은 .상점 종료시 이동이 가능 해집니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							ChattingController.toChatting(o,
									"\\fR판매등록 : 아이템을 선택 후 OK를 클릭 해주세요.",
									Lineage.CHATTING_MODE_MESSAGE);
							return true;
						} else if (type.equalsIgnoreCase("종료")) {
							PersnalShopInstance psi = World.findPersnalShop(o
									.getObjectId());
							if (psi == null) {
								ChattingController.toChatting(o,
										"\\fY무인상점이 배치되어 있지 않습니다.",
										Lineage.CHATTING_MODE_MESSAGE);
								return true;
							}
							int number = 0;
							for (ItemInstance item : o.getInventory().getList()) {
								number += 1;
							}
							if (number >= 150) {
								ChattingController.toChatting(o,
										"\\fY150개 이하로 인벤토리 공간을 확보해 주세요.",
										Lineage.CHATTING_MODE_MESSAGE);
								return true;
							}

							psi.close();
							o.toSender(S_ObjectLock.clone(BasePacketPooling
									.getPool(S_ObjectLock.class), 23));
							ChattingController.toChatting(o,
									"\\fY상점종료 : 매입 대금과 모든 물품이 회수 되었습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							o.toSender(S_Html.clone(
									BasePacketPooling.getPool(S_Html.class), o,
									"hpgosiv"));
						} else if (type.equalsIgnoreCase("추가")) {
							PersnalShopInstance psi = World.findPersnalShop(o
									.getObjectId());

							if (psi == null) {
								ChattingController.toChatting(o,
										"\\fY무인상점이 배치되어 있지 않습니다.",
										Lineage.CHATTING_MODE_MESSAGE);
								return true;
							}
							/**
							 * 현재 아이템 배치를 할경우 전부 다시 가격을 설정해야함.. 개인상점에 지정된 아이템만
							 * 설정(가격)해서 추가 할수 있도록 구현할것
							 */
							o.setPersnalShopEdit(true);
							o.toSender(S_CharAutoShop.clone(BasePacketPooling
									.getPool(S_CharAutoShop.class),
									(PcInstance) o, o.getInventory().getList()));
							ChattingController.toChatting(o,
									"\\fR판매등록 : 아이템을 선택 후 OK를 클릭 해주세요.",
									Lineage.CHATTING_MODE_MESSAGE);
						} else if (type.equalsIgnoreCase("가격수정")
								|| type.equalsIgnoreCase("수정")) {
							PersnalShopInstance psi = World.findPersnalShop(o
									.getObjectId());
							if (psi == null) {
								ChattingController.toChatting(o,
										"\\fY무인상점이 배치되어 있지 않습니다.",
										Lineage.CHATTING_MODE_MESSAGE);
								return true;
							}
							o.setPersnalShopPriceReset(true);
							o.toSender(S_PersnalShopPriceSetting.clone(
									BasePacketPooling
											.getPool(S_PersnalShopPriceSetting.class),
									(PcInstance) o, psi.getItemList()));
							ChattingController.toChatting(o,
									"\\fT판매금액 : 판매 금액을 신중히 입력하시기 바랍니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							// 본인 상점으로 이동하기 위함
						} else if (type.equalsIgnoreCase("이동")) {
							PersnalShopInstance psi = World.findPersnalShop(o
									.getObjectId());
							if (psi == null) {
								ChattingController.toChatting(o,
										"\\fY무인상점이 배치되어 있지 않습니다.",
										Lineage.CHATTING_MODE_MESSAGE);
								return true;
							}
							if (psi.getMap() != 340 && psi.getMap() != 350
									&& psi.getMap() != 360
									&& psi.getMap() != 370
									&& psi.getMap() != 800) {
								// psi.deleteDBNpc();
								psi.close();
								o.toSender(S_ObjectLock.clone(BasePacketPooling
										.getPool(S_ObjectLock.class), 23));
								ChattingController.toChatting(o,
										"\\fS미등록 물품이 존재하여 상점이 종료됩니다.",
										Lineage.CHATTING_MODE_MESSAGE);
								ChattingController.toChatting(o,
										"물품들이 인벤토리로 회수 되었습니다.",
										Lineage.CHATTING_MODE_MESSAGE);
								o.toSender(S_Html.clone(
										BasePacketPooling.getPool(S_Html.class),
										o, "hpgosiv"));
								return true;

							}
							o.toPotal(psi.getX(), psi.getY(), psi.getMap());
							ChattingController.toChatting(o,
									"\\fW본인의 개인상점으로 이동 되었습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
						} else {
							ChattingController
									.toChatting(
											o,
											Common.COMMAND_TOKEN
													+ "\\fR<상점 명령어> \\fY상점 [시작/종료/이동/추가/가격수정]",
											Lineage.CHATTING_MODE_MESSAGE);
						}

					} catch (Exception e) {
						ChattingController.toChatting(o,
								"\\fR<상점 명령어> \\fY상점 [시작/종료/이동/추가/수정]",
								Lineage.CHATTING_MODE_MESSAGE);
						o.toSender(S_Html.clone(
								BasePacketPooling.getPool(S_Html.class), o,
								"hpgosiv"));

					}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "인벤확인")
					|| key.equalsIgnoreCase(Common.COMMAND_TOKEN + "장비확인")
					|| key.equalsIgnoreCase(Common.COMMAND_TOKEN + "장비정보")
					|| key.equalsIgnoreCase(Common.COMMAND_TOKEN + "인벤정보")) {
				try {
					toMarblesCharacterInvenInfo(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "인벤확인 [구매하고자하는 캐릭터명]",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "마법확인")
					|| key.equalsIgnoreCase(Common.COMMAND_TOKEN + "스킬확인")
					|| key.equalsIgnoreCase(Common.COMMAND_TOKEN + "스킬정보")
					|| key.equalsIgnoreCase(Common.COMMAND_TOKEN + "마법정보")) {
				try {
					toMarblesCharacterSpellInfo(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "마법확인 [구매하고자하는 캐릭터명]",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "캐릭터확인")
					|| key.equalsIgnoreCase(Common.COMMAND_TOKEN + "캐릭확인")
					|| key.equalsIgnoreCase(Common.COMMAND_TOKEN + "캐릭정보")) {
				try {
					toMarblesCharacterInfo(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "캐릭확인 [구매하고자하는 캐릭터명] 혹은 캐릭터확인 [구매하고자하는 캐릭터명]",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "틱")) {
				if (o instanceof Character) {
					Character cha = (Character) o;
					ChattingController.toChatting(cha, String.format(
							"[HP: %d초당 / %d회복] [MP: %d초당 / %d회복]",
							cha.getHpTime(), cha.getHpTic(), cha.getMpTime(),
							cha.getMpTic()), Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "보스")) {
				bossList(o);

				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "시세")) {
				try {
					toPcShopFind(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "시세 아이템명",
							Lineage.CHATTING_MODE_MESSAGE);
					ChattingController.toChatting(o, String.format(
							" * 인챈트 아이템 검색"), Lineage.CHATTING_MODE_MESSAGE);
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "시세 아이템명 인챈트수 축여부[0:축/1:일반/2:저주]", Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;
			}
			if (Lineage.user_command) {
				if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "계좌등록")) {
					toBankAccountSetting(o, st);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "거래게시판")) {
					PcInstance pc = (PcInstance) o;
					pc.setBoard(3);
					viewBoard(o);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "계좌확인")) {
					toBankAccountPrint(o, st);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "판매")) { // 판매글작성

					toAdenaSellRegedit(o, st);

					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "구매")) { // 구매
																				// 한다.
					toAdenaBuy(o, st);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "입금완료")) { // 구매자가

					입금완료1(o, st);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "입금확인")) { // 판매자가

					입금확인(o, st);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "판매취소")) {

					toAdenaCancel(o, st);

					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "구매취소")) {
					// 운영자 전용 명령어
					if (o.getGm() > 0) {
						buyCancel1(o, st);
					}
					return true;

				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "정보")) {
					characterInfo(o, st);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "서버정보")) {
					serverInfo(o, st);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "sp확인")) {
					Character cha = (Character) o;
					ChattingController.toChatting(
							o,
							String.format("나의 sp 는 %d 입니다",
									SkillController.getSp(cha, false)),
							Lineage.CHATTING_MODE_MESSAGE);
					return true;
					// 공통 명령어

				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "피바")) {
					if (Lineage.server_version >= 163) {
						// 설정.
						o.setHpbar(!o.isHpbar());
						ChattingController.toChatting(o, String.format(
								"HP바: %s활성화", o.isHpbar() ? "" : "비"), 20);
						// 표현.
						o.toSender(S_ObjectHitratio.clone(BasePacketPooling.getPool(S_ObjectHitratio.class),
								(Character) o, o.isHpbar()));
						return true;
					}
					
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "물약멘트")
						|| key.equalsIgnoreCase(Common.COMMAND_TOKEN + "물약메세지")) {
					o.setAutoPotionMent(!o.isAutoPotionMent());
					ChattingController.toChatting(o, String.format(
							"물약멘트: %s활성화", o.isAutoPotionMent() ? "" : "비"),
							Lineage.CHATTING_MODE_MESSAGE);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "파티멘트")
						|| key.equalsIgnoreCase(Common.COMMAND_TOKEN + "파티메세지")) {
					PcInstance pc = (PcInstance) o;
					pc.setAuto_party_message(!pc.isAuto_party_message());
					ChattingController.toChatting(
							o,
							String.format("파티멘트: %s활성화",
									pc.isAuto_party_message() ? "" : "비"), 20);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "초대")) {
					try {
						PcInstance user = World.findPc(st.nextToken());
						if (user == null) {
							ChattingController.toChatting(o, "캐릭터가 존재하지 않습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
						} else {
							if (o.getName().equals(user.getName())) {
								ChattingController.toChatting(o,
										"자신은 초대할 수 없습니다.",
										Lineage.CHATTING_MODE_MESSAGE);
							} else {
								PartyController.toParty((PcInstance) o, user,
										false, false);
							}
						}
					} catch (Exception e) {
						ChattingController.toChatting(o, Lineage.command
								+ "초대 아이디", Lineage.CHATTING_MODE_MESSAGE);
					}
					return true;

				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "오토루팅")) {
					o.setAutoPickup(!o.isAutoPickup());
					ChattingController.toChatting(o, String.format("오토루팅: %s활성화", o.isAutoPickup() ? "" : "비"), Lineage.CHATTING_MODE_MESSAGE);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "자동물약")) {
					if (o.getInventory() != null)
						NpcSpawnlistDatabase.autoPotion.toTalk((PcInstance) o, null);
					return true;

				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "라이트")
						|| key.equalsIgnoreCase(Common.COMMAND_TOKEN + "맵핵")) {
						if (!isCommandDelay(o, null, 10))
							return true;
						PcInstance pc = (PcInstance) o;
						try {
						if (pc.isDead()) {
							ChattingController.toChatting(o, "죽은 상태에선 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
							return true;
						}
						if (o.isMapHack) {
							o.isMapHack = false;
							o.toSender(new S_Ability(3, false));
							ChattingController.toChatting(pc, String.format("라이트: 비활성화"),
									Lineage.CHATTING_MODE_MESSAGE);
						} else {
							o.isMapHack = true;
							o.toSender(new S_Ability(3, true));
							ChattingController.toChatting(pc, String.format("라이트: 활성화"),
									Lineage.CHATTING_MODE_MESSAGE);
						}
						} catch (Exception e) {
							ChattingController.toChatting(o, String.format("화면을 환하게 하거나 어둡게 합니다."),
									Lineage.CHATTING_MODE_MESSAGE);
						}
						
				return true;
					
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "드랍멘트")
						|| key.equalsIgnoreCase(Common.COMMAND_TOKEN + "메세지")) {
					o.setAutoPickupMessage(!o.isAutoPickupMessage());
					ChattingController.toChatting(o, String.format("드랍멘트: %s활성화", o.isAutoPickupMessage() ? "" : "비"), Lineage.CHATTING_MODE_MESSAGE);
					return true;

				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "수배")) {
					try {
						String name = st.nextToken();
						WantedController.append(o, name,
								Lineage.wanted_price_min);
					} catch (Exception e) {
						ChattingController.toChatting(o, Common.COMMAND_TOKEN
								+ "수배 아이디", Lineage.CHATTING_MODE_MESSAGE);
					}
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "수배자")) {
					WantedController.checkWanted((PcInstance) o);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "버프시간")) {
					
					int cnt = 0;
					List<String> list = new ArrayList<String>();
					Buff buff = BuffController.find(o);
					
					if (buff != null) {
						for (BuffInterface b : buff.getList()) {
							if (b.getSkill() != null)
								list.add(String.format("%s : %d초", b.getSkill()
										.getName(), b.getTime()));
							o.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), o, "Buff", null, list));
							cnt += 1;
						}
						ChattingController.toChatting(o, "현재 " + cnt
								+ "개의 버프가 적용되어 있습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
					}
						return true;

				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "버프")) {
				        toBuff(o);
				    return true;
					
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "리니지시간")) { // .시간
					String now_date = String.format("%02d:%02d:%02d",
							ServerDatabase.getLineageTimeHour(),
							ServerDatabase.getLineageTimeMinute(),
							ServerDatabase.getLineageTimeSeconds());
					ChattingController.toChatting(o,
							String.format("[리니지 시간] %s", now_date),
							Lineage.CHATTING_MODE_MESSAGE);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "시간")) {
					Calendar cal = Calendar.getInstance(Locale.KOREA);
					String date = cal.get(Calendar.YEAR)
							+ "년 "
							+ (cal.get(Calendar.MONTH) + 1 + "월 "
									+ cal.get(Calendar.DATE) + "일 "
									+ cal.get(Calendar.HOUR_OF_DAY) + "시 "
									+ cal.get(Calendar.MINUTE) + "분 "
									+ cal.get(Calendar.SECOND) + "초 ");
					ChattingController.toChatting(o,
							String.format("[한국 표준시간] %s입니다.", date),
							Lineage.CHATTING_MODE_MESSAGE);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "현재시간")) {
					String now_date = String.format("%02d:%02d:%02d",
							ServerDatabase.getLineageTimeHour(),
							ServerDatabase.getLineageTimeMinute(),
							ServerDatabase.getLineageTimeSeconds());
					ChattingController.toChatting(o,
							String.format("현 시간 %s", now_date), 20);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "랭킹")) {
					ChattingController.toChatting(o, String.format("전체 랭킹: %d위", RankController.getRankAll(o)), 20);
					ChattingController.toChatting(o, String.format("클래스 랭킹: %d위", RankController.getRankClass(o)), 20);
					//o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 562, String.valueOf(((PcInstance) o).getPkCount())));
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "마방")) {
					ChattingController.toChatting(o, String.format(
							"마방(Mr): %d",
							SkillController.getMr((Character) o, false)), 20);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "스펠")) {
					ChattingController.toChatting(o, String.format(
							"스펠파워(Sp): %d",
							SkillController.getSp((Character) o, false)), 20);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "창고이력")) {
					toWarehouseClanLog(o, st);
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "남은시간")) {
					try {
						던전시간(o);
					} catch (Exception e) {
					}
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "기감")) {
					PcInstance pc = (PcInstance) o;
					String dungeon = null;
					
					if (pc.getGiran_dungeon_time() > 0)
						dungeon = "기란감옥";
					if (pc.getGiran_dungeon_time() > 0) {
					    int dungeonTimeInSeconds = pc.getGiran_dungeon_time();
					    final int SECONDS_PER_HOUR = 3600;
					    final int SECONDS_PER_MINUTE = 60;

					    int hours = dungeonTimeInSeconds / SECONDS_PER_HOUR;
					    int remainingSecondsAfterHours = dungeonTimeInSeconds % SECONDS_PER_HOUR;
					    int minutes = remainingSecondsAfterHours / SECONDS_PER_MINUTE;
					    int seconds = remainingSecondsAfterHours % SECONDS_PER_MINUTE;

					    String formattedTime;
					    if (hours > 0) {
					        formattedTime = String.format("%s: %d시간 %d분 %d초", dungeon, hours, minutes, seconds);
					    } else if (minutes > 0) {
					        formattedTime = String.format("%s: %d분 %d초", dungeon, minutes, seconds);
					    } else {
					        formattedTime = String.format("%s: %d초", dungeon, seconds);
					    }

					    ChattingController.toChatting(o, formattedTime, Lineage.CHATTING_MODE_MESSAGE);
					} else {
					    ChattingController.toChatting(o, "기란감옥 이용시간을 모두 사용하셨습니다.", Lineage.CHATTING_MODE_MESSAGE);
					}
						
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "스초완료")) {
					try {
						if (o instanceof PcInstance) {
							PcInstance pc = (PcInstance) o;
							if (pc.stat_clear == false) {
								ChattingController.toChatting(o,
										String.format("회상의 촛불을 사용하신후 시도해주세요."),
										20);
							} else {
								if (pc.toLvStat(false) && pc.getLevel() > 50) {
									ChattingController
											.toChatting(
													o,
													String.format("스탯이 아직 남았습니다. 완료하신 후 다시 시도해 주십시오."),
													20);
								} else {// 앟
									// hp&mp 세팅.
									int hp = 0, mp = 0;

									int level = pc.getLevel() - 50;

									for (int i = 1; i < pc.getLevel(); ++i) {
										if (i == 51)
											pc.el_ok = false;

										hp += CharacterController.toStatusUP(
												pc, true);
										mp += CharacterController.toStatusUP(
												pc, false);
									}

									int new_hp = pc.getMaxHp() + hp;
									int new_mp = pc.getMaxMp() + mp;

									switch (pc.getClassType()) {
									case 0x00:
										if (new_hp >= Lineage.royal_max_hp
												&& Lineage.royal_max_hp > 0)
											new_hp = Lineage.royal_max_hp;
										if (new_mp >= Lineage.royal_max_mp
												&& Lineage.royal_max_mp > 0)
											new_mp = Lineage.royal_max_mp;
										break;
									case 0x01:
										if (new_hp >= Lineage.knight_max_hp
												&& Lineage.knight_max_hp > 0)
											new_hp = Lineage.knight_max_hp;
										if (new_mp >= Lineage.knight_max_mp
												&& Lineage.knight_max_mp > 0)
											new_mp = Lineage.knight_max_mp;
										break;
									case 0x02:
										if (new_hp >= Lineage.elf_max_hp
												&& Lineage.elf_max_hp > 0)
											new_hp = Lineage.elf_max_hp;
										if (new_mp >= Lineage.elf_max_mp
												&& Lineage.elf_max_mp > 0)
											new_mp = Lineage.elf_max_mp;
										break;
									case 0x03:
										if (new_hp >= Lineage.wizard_max_hp
												&& Lineage.wizard_max_hp > 0)
											new_hp = Lineage.wizard_max_hp;
										if (new_mp >= Lineage.wizard_max_mp
												&& Lineage.wizard_max_mp > 0)
											new_mp = Lineage.wizard_max_mp;
										break;
									case 0x04:
										if (new_hp >= Lineage.darkelf_max_hp
												&& Lineage.darkelf_max_hp > 0)
											new_hp = Lineage.darkelf_max_hp;
										if (new_mp >= Lineage.darkelf_max_mp
												&& Lineage.darkelf_max_mp > 0)
											new_mp = Lineage.darkelf_max_mp;
										break;
									case 0x05:
										if (new_hp >= Lineage.dragonknight_max_hp
												&& Lineage.dragonknight_max_hp > 0)
											new_hp = Lineage.dragonknight_max_hp;
										if (new_mp >= Lineage.dragonknight_max_mp
												&& Lineage.dragonknight_max_mp > 0)
											new_mp = Lineage.dragonknight_max_mp;
										break;
									case 0x06:
										if (new_hp >= Lineage.blackwizard_max_hp
												&& Lineage.blackwizard_max_hp > 0)
											new_hp = Lineage.blackwizard_max_hp;
										if (new_mp >= Lineage.blackwizard_max_mp
												&& Lineage.blackwizard_max_mp > 0)
											new_mp = Lineage.blackwizard_max_mp;
										break;
									}

									pc.setMaxHp(new_hp);
									pc.setMaxMp(new_mp);
									pc.setNowHp(pc.getNowHp() + hp);

									pc.setNowMp(pc.getNowMp() + mp);
									Item i = ItemDatabase.find("회상의 촛불");
									ItemInstance temp = pc.getInventory().find(
											i.getName(), i.isPiles());
									// 메세지 출력.
									pc.stat_clear = false;
									ChattingController.toChatting(o,
											String.format("스탯 초기화가 완료 되었습니다."),
											20);
									pc.getInventory().count(temp,
											temp.getCount() - 1, true);
									try {
										CharactersDatabase
												.saveCharacterStatus(pc);
									} catch (Exception e) {
									}
								}
							}
						}
					} catch (Exception e) {
					}
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "명령어")) {
					// 유저 명령어
					o.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), o, "helpys1"));
					return true;
				} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "도움말")) {
					// F1	
					o.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), o, "help"));
					return true;
				}
			}

			if (o.getGm() == 0)
				return false;

			// 운영자 명령어
			// if(o.getGm() >= GmCommandDatabase.find(key.substring(1))) {
			if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "time")) {
				cmd = cmd.substring(6);
				ServerDatabase.LineageWorldTime = Integer.valueOf(cmd);

				o.toSender(S_WorldTime.clone(BasePacketPooling
						.getPool(S_WorldTime.class)));
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "영자명령어")) {
				// 영자 명령어
				o.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), o, "helpys2"));
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "전체메세지")) {
				try {
					StringBuffer msg = new StringBuffer();

					while (st.hasMoreTokens())
						msg.append(st.nextToken() + " ");

					for (PcInstance pc : World.getPcList())
						pc.toSender(S_BlueMessage.clone(
								BasePacketPooling.getPool(S_BlueMessage.class),
								556, "\\fU [******] " + msg.toString()));
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "전체메세지 메세지", Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "op")) {

				ServerBasePacket sbp = (ServerBasePacket) S_Ability.clone(
						BasePacketPooling.getPool(S_Ability.class), 3, true);
				byte[] data = sbp.getByte();
				// data[0] = (byte)Opcodes.S_OP_280[op];
				sbp.clear();
				sbp.writeB(data);
				o.toSender(sbp, true);

				System.out.println(op + ":" + Opcodes.S_OP_280[op]);
				op += 1;

				// op += 1;
				// o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class),
				// op));
				// System.out.println(op);

				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "loc")) {
				int x = Integer.valueOf(st.nextToken());
				int y = Integer.valueOf(st.nextToken());
				int map = Integer.valueOf(st.nextToken());
				o.toTeleport(x, y, map, true);
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "그래픽")) {
				int gfx = Integer.valueOf(st.nextToken());
				o.setGfx(gfx);
				o.toSender(S_ObjectGfx.clone(
						BasePacketPooling.getPool(S_ObjectGfx.class), o), true);
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "모드")) {
				int mode = Integer.valueOf(st.nextToken());
				o.setGfxMode(mode);
				o.toSender(S_ObjectGfx.clone(
						BasePacketPooling.getPool(S_ObjectGfx.class), o), true);
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "액션")) {
				int action = Integer.valueOf(st.nextToken());
				o.toSender(S_ObjectAction.clone(
						BasePacketPooling.getPool(S_ObjectAction.class), o,
						action), true);
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "ef")) {
				try {
					int effect = Integer.valueOf(st.nextToken());
					op = effect;
					o.toSender(S_ObjectEffect.clone(
							BasePacketPooling.getPool(S_ObjectEffect.class), o,
							effect), true);
				} catch (Exception e) {
					System.out.println(++op);
					o.toSender(S_ObjectEffect.clone(
							BasePacketPooling.getPool(S_ObjectEffect.class), o,
							op), true);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "efl")) {
				try {
					int effect = Integer.valueOf(st.nextToken());
					op = effect;
					o.toSender(S_ObjectEffectLocation.clone(BasePacketPooling
							.getPool(S_ObjectEffectLocation.class), effect, o
							.getX(), o.getY()), true);
				} catch (Exception e) {
					System.out.println(++op);
					o.toSender(S_ObjectEffect.clone(
							BasePacketPooling.getPool(S_ObjectEffect.class), o,
							op), true);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "d")) {
				int x = o.getX() + Util.getXY(o.getHeading(), true);
				int y = o.getY() + Util.getXY(o.getHeading(), false);
				int map = o.getMap();
				ChattingController.toChatting(o,
						"map dynamic : " + World.getMapdynamic(x, y, map), 20);
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "f")) {
				Connection con = null;
				PreparedStatement stt = null;
				try {
					int x = o.getX() + Util.getXY(o.getHeading(), true);
					int y = o.getY() + Util.getXY(o.getHeading(), false);
					int map = o.getMap();

					con = DatabaseConnection.getLineage();
					stt = con
							.prepareStatement("INSERT INTO background_spawnlist SET gfx=85, light=13, locX=?, locY=?, locMap=?");
					stt.setInt(1, x);
					stt.setInt(2, y);
					stt.setInt(3, map);
					stt.executeUpdate();

					object obj = BackgroundInstance.clone(BackgroundDatabase
							.getPool(BackgroundInstance.class));
					obj.setObjectId(ServerDatabase.nextNpcObjId());
					obj.setGfx(85);
					obj.setClassGfx(o.getGfx());
					obj.setClassGfxMode(o.getGfxMode());
					obj.setLight(13);
					obj.setHomeX(x);
					obj.setHomeY(y);
					obj.setHomeMap(map);
					obj.toTeleport(obj.getHomeX(), obj.getHomeY(),
							obj.getHomeMap(), false);

					return true;
				} catch (Exception e) {
					lineage.share.System.printf("%s : 횟불 등록\r\n",
							CommandController.class.toString());
					lineage.share.System.println(e);
				} finally {
					DatabaseConnection.close(con, stt);
				}
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "인벤삭제")) {
				try {
					인벤삭제(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, "[서버알림] "
							+ Common.COMMAND_TOKEN
							+ "인벤삭제 [캐릭명] [아이템이름] [갯수] [인챈트] [축/저주]", 20);
					ChattingController.toChatting(o,
							"[서버알림] 아이템이름이 없을시 착용템 제외 모두 삭제.", 20);
				}
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "공성체크")) {
				try {
					toKingdomWarCheck(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, new StringBuilder()
							.append(TOKEN).append("공성체크").toString(), 20);
				}
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "공성")) {
				setKingdomWar();
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "날씨")) {
				setWeather(o, st);
				return true;
				
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "몬스터피바")) {
				if (GameSetting.몬스터피바 == false) {
					GameSetting.몬스터피바 = true;

					ChattingController.toChatting(o, "몬스터피바 활성화 완료.", 20);
				} else if (GameSetting.몬스터피바 == true) {
					GameSetting.몬스터피바 = false;
					ChattingController.toChatting(o, "몬스터피바 비활성화 완료", 20);
				}
				return true;
				
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "유저피바")) {
				if (GameSetting.유저피바 == false) {
					GameSetting.유저피바 = true;
					ChattingController.toChatting(o, "유저피바 활성화 완료.", 20);

				} else if (GameSetting.유저피바 == true) {
					GameSetting.유저피바 = false;
					ChattingController.toChatting(o, "유저피바 비활성화 완료", 20);
				}
				return true;
				
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "대미지확인")) {
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

					ChattingController.toChatting(o, String.format("[%s] 캐릭터 대미지 확인 %s활성화.", pcName, pc.isDmgCheck ? "" : "비"), Lineage.CHATTING_MODE_MESSAGE);
				} catch (Exception e) {
					if (o != null)
						ChattingController.toChatting(o, Common.COMMAND_TOKEN + "대미지확인 아이디", Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;
				
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "변신아이디")) {
				try {
				int gfx = Integer.valueOf(st.nextToken());
				o.setGfx(gfx);
				o.toSender(new S_ObjectGfx(o), true);
			} catch (Exception e) {
				ChattingController.toChatting(o, Common.COMMAND_TOKEN
						+ "변신아이디 [polyid]", 20);
			}
				return true;
				
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "변신")) {
				try {
					int gfx = 1080;
					
					if (st.hasMoreTokens())
						gfx = Integer.valueOf(st.nextToken());
					
					o.setGfx(gfx);
					o.toSender(S_ObjectGfx.clone(BasePacketPooling.getPool(S_ObjectGfx.class), o), true);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN + "변신 [변신번호] 메티스: 1080", Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;
				
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "이동")) {
				try {
					int x = Integer.valueOf(st.nextToken());
					int y = Integer.valueOf(st.nextToken());
					int map = Integer.valueOf(st.nextToken());
					o.toTeleport(x, y, map, true);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "이동 [x, y, map]", Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "뻥")) {
				try {
					toCalcUser(o, st);
				} catch (Exception localException21) {
					if (o != null)
						ChattingController.toChatting(o, Common.COMMAND_TOKEN
								+ "뻥 [+,-] [Count]", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "로봇좌표")) {
				try {
					로봇좌표(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "로봇좌표 [타입]", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "몬스터")) {
				try {
					toMonster(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, "[서버알림] .몬스터 몬스터이름 마릿수",
							20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "pvp초기화")) {
				try {
					toPvpAllRomove(o);
				} catch (Exception e) {
					ChattingController.toChatting(o, "[서버알림] .몬스터 몬스터이름 마릿수",
							20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "아이템")) {
				try {
					toItem(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "아이템 [이름] [갯수] [인챈] [축/일반/저주]", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "검색")) {
				try {
					검색(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "검색 [타입] [아이템이름or번호]", 20);
					ChattingController.toChatting(o,
							"<<타입 (0:아이템,1:엔피씨,2:몬스터)>>", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "소환")) {
				try {
					toCall(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "소환 name", 20);
				}
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "너만떠")) {
				try {
					toperpecten(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "너만떠 name", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "출두")) {
				try {
					toGo(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "출두 name", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "버프")) {
				try {
					toBuff(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN + "버프 name", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "올버프")) {
				try {
					toBuffAll(o);
				} catch (Exception e) {
					if (o != null)
						ChattingController.toChatting(o, Common.COMMAND_TOKEN
								+ "올버프", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "몬뿌")) {
				try {
					toMonster2(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, ".몬뿌 name_id count", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "차단")) {
				try {
					toBan(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "차단 id", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "차단해제")) {
				try {
					toBanClear(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, "차단해제 id", Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "압류")) {
				try {
					toBan2(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, "압류 케릭명", Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "압류해제")) {
				try {
					toBan2Clear(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, "압류해제 케릭명", Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "스킬올마")) {
				try {
					toSkillAllMaster(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "스킬올마 name", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "채금")) {
				try {
					toChattingClose(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "채금 name time", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "채금해제")) {
				try {
					toChattingCloseRemove(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "채금해제 name", Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "청소")) {
				try {
					toWorldItemClear(o);
				} catch (Exception e) {
					if (o != null)
						ChattingController.toChatting(o, Common.COMMAND_TOKEN
								+ "청소", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "셧다운")) {
				try {
					toShutdown(o, st);
				} catch (Exception e) {
					if (o != null)
						ChattingController.toChatting(o, Common.COMMAND_TOKEN
								+ "셧다운 분", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "몹정리")
					|| key.equalsIgnoreCase(Common.COMMAND_TOKEN + "정리")) {
				try {
					toClearMonster(o);
				} catch (Exception e) {
					if (o != null)
						ChattingController.toChatting(o, Common.COMMAND_TOKEN
								+ "몹정리", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "부활")) {
				try {
					o.toRevival(o);
				} catch (Exception e) {
					if (o != null)
						ChattingController.toChatting(o, Common.COMMAND_TOKEN
								+ "부활", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "전체선물")) {
				try {
					전체선물(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "전체선물 [아이템이름] [갯수] [인챈] [축/일반/저주]", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "혈맹선물")) {
				try {
					혈맹선물(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN + "혈맹선물 [혈맹이름][아이템이름] [갯수] [인챈] [축/일반/저주]",
							20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "죽기")) {
				try {
					o.setGm(0);
					o.setNowHp(0);
				} catch (Exception e) {
					if (o != null)
						ChattingController.toChatting(o, Common.COMMAND_TOKEN
								+ "죽기", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "전체소환")) {
				try {
					for (PcInstance pc : World.getPcList())
						pc.toTeleport(o.getX(), o.getY(), o.getMap(), true);
				} catch (Exception e) {
					if (o != null)
						ChattingController.toChatting(o, Common.COMMAND_TOKEN
								+ "전체소환", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "경험치이벤트")) {
				try {
					String type = st.nextToken();
					if (type.equalsIgnoreCase("종료")) {
						Lineage.event_exp = false;
						World.toSender(S_ObjectChatting.clone(BasePacketPooling
								.getPool(S_ObjectChatting.class), String
								.format("서버에 경험치 2배 이벤트가 종료되었습니다.")));
					} else if (type.equalsIgnoreCase("시작")) {
						Lineage.event_exp = true;
						World.toSender(S_ObjectChatting.clone(BasePacketPooling
								.getPool(S_ObjectChatting.class), String
								.format("서버에 경험치 2배 이벤트가 시작되었습니다.")));
					}
				} catch (Exception e) {
					ChattingController.toChatting(o, ".경험치이벤트 [시작/종료]", 20);
				}
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "귀환")) {
				try {
					teleporthome(o, st);
				} catch (Exception e) {
					if (o != null)
						ChattingController.toChatting(o, Common.COMMAND_TOKEN
												+ "말섬 | 글말 | 켄말 | 켄성 | 윈말 | 윈성 | 은말 | 화말 | 요말 | 기란 | 인나드 | 웰던 | 하이네 | 영자| 후원| 잊섬| 지옥", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "날씨")) {
				int num = Integer.parseInt(st.nextToken());
				if (num == 1)
					World.toSender(S_Weather.clone(
							BasePacketPooling.getPool(S_Weather.class),
							S_Weather.WEATHER_FAIR));
				if (num == 2)
					World.toSender(S_Weather.clone(
							BasePacketPooling.getPool(S_Weather.class),
							S_Weather.WEATHER_SNOW_1));
				if (num == 3)
					World.toSender(S_Weather.clone(
							BasePacketPooling.getPool(S_Weather.class),
							S_Weather.WEATHER_SNOW_2));
				if (num == 4)
					World.toSender(S_Weather.clone(
							BasePacketPooling.getPool(S_Weather.class),
							S_Weather.WEATHER_SNOW_3));
				if (num == 5)
					World.toSender(S_Weather.clone(
							BasePacketPooling.getPool(S_Weather.class),
							S_Weather.WEATHER_RAIN_1));
				if (num == 6)
					World.toSender(S_Weather.clone(
							BasePacketPooling.getPool(S_Weather.class),
							S_Weather.WEATHER_RAIN_2));
				if (num == 7)
					World.toSender(S_Weather.clone(
							BasePacketPooling.getPool(S_Weather.class),
							S_Weather.WEATHER_RAIN_3));
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "아지트")) {
				int num = Integer.parseInt(st.nextToken());
				if (num == 1) {
					o.toTeleport(33372, 32653, 4, true);
					ChattingController.toChatting(o, "1번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 2) {
					o.toTeleport(33384, 32654, 4, true);
					ChattingController.toChatting(o, "2번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 3) {
					o.toTeleport(33396, 32654, 4, true);
					ChattingController.toChatting(o, "3번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 4) {
					o.toTeleport(33429, 32659, 4, true);
					ChattingController.toChatting(o, "4번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 5) {
					o.toTeleport(33443, 32667, 4, true);
					ChattingController.toChatting(o, "5번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 6) {
					o.toTeleport(33457, 32652, 4, true);
					ChattingController.toChatting(o, "6번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 7) {
					o.toTeleport(33478, 32668, 4, true);
					ChattingController.toChatting(o, "7번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 8) {
					o.toTeleport(33475, 32680, 4, true);
					ChattingController.toChatting(o, "8번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 9) {
					o.toTeleport(33458, 32696, 4, true);
					ChattingController.toChatting(o, "9번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 10) {
					o.toTeleport(33424, 32689, 4, true);
					ChattingController.toChatting(o, "10번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 11) {
					o.toTeleport(33413, 32675, 4, true);
					ChattingController.toChatting(o, "11번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 12) {
					o.toTeleport(33419, 32705, 4, true);
					ChattingController.toChatting(o, "12번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 13) {
					o.toTeleport(33375, 32696, 4, true);
					ChattingController.toChatting(o, "13번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 14) {
					o.toTeleport(33364, 32684, 4, true);
					ChattingController.toChatting(o, "14번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 15) {
					o.toTeleport(33364, 32671, 4, true);
					ChattingController.toChatting(o, "15번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 16) {
					o.toTeleport(33345, 32662, 4, true);
					ChattingController.toChatting(o, "16번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 17) {
					o.toTeleport(33347, 32675, 4, true);
					ChattingController.toChatting(o, "17번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 18) {
					o.toTeleport(33341, 32708, 4, true);
					ChattingController.toChatting(o, "18번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 19) {
					o.toTeleport(33354, 32730, 4, true);
					ChattingController.toChatting(o, "19번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 20) {
					o.toTeleport(33370, 32715, 4, true);
					ChattingController.toChatting(o, "20번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 21) {
					o.toTeleport(33382, 32715, 4, true);
					ChattingController.toChatting(o, "21번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 22) {
					o.toTeleport(33404, 32737, 4, true);
					ChattingController.toChatting(o, "22번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 23) {
					o.toTeleport(33428, 32719, 4, true);
					ChattingController.toChatting(o, "23번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 24) {
					o.toTeleport(33450, 32732, 4, true);
					ChattingController.toChatting(o, "24번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 25) {
					o.toTeleport(33406, 32757, 4, true);
					ChattingController.toChatting(o, "25번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 26) {
					o.toTeleport(33366, 32759, 4, true);
					ChattingController.toChatting(o, "26번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 27) {
					o.toTeleport(33355, 32776, 4, true);
					ChattingController.toChatting(o, "27번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 28) {
					o.toTeleport(33358, 32788, 4, true);
					ChattingController.toChatting(o, "28번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 29) {
					o.toTeleport(33371, 32788, 4, true);
					ChattingController.toChatting(o, "29번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 30) {
					o.toTeleport(33385, 32776, 4, true);
					ChattingController.toChatting(o, "30번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 31) {
					o.toTeleport(33402, 32790, 4, true);
					ChattingController.toChatting(o, "31번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 32) {
					o.toTeleport(33484, 32790, 4, true);
					ChattingController.toChatting(o, "32번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 33) {
					o.toTeleport(33500, 32804, 4, true);
					ChattingController.toChatting(o, "33번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 34) {
					o.toTeleport(33382, 32803, 4, true);
					ChattingController.toChatting(o, "34번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 35) {
					o.toTeleport(33376, 32826, 4, true);
					ChattingController.toChatting(o, "35번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 36) {
					o.toTeleport(33400, 32813, 4, true);
					ChattingController.toChatting(o, "36번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 37) {
					o.toTeleport(33401, 32823, 4, true);
					ChattingController.toChatting(o, "37번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 38) {
					o.toTeleport(33436, 32840, 4, true);
					ChattingController.toChatting(o, "38번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 39) {
					o.toTeleport(33460, 32833, 4, true);
					ChattingController.toChatting(o, "39번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 40) {
					o.toTeleport(33390, 32847, 4, true);
					ChattingController.toChatting(o, "40번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 41) {
					o.toTeleport(33403, 32861, 4, true);
					ChattingController.toChatting(o, "41번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 42) {
					o.toTeleport(33416, 32853, 4, true);
					ChattingController.toChatting(o, "42번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 43) {
					o.toTeleport(33376, 32871, 4, true);
					ChattingController.toChatting(o, "43번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 44) {
					o.toTeleport(33428, 32869, 4, true);
					ChattingController.toChatting(o, "44번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				if (num == 45) {
					o.toTeleport(33447, 32871, 4, true);
					ChattingController.toChatting(o, "45번째 아지트로 이동하였습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;
				//

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "수배초기화")) {
				WantedController.clear();
				ChattingController.toChatting(o, "수배자가 초기화 되었습니다.", 20);
				return true;
				
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "인벤검색")) {
				try {
				String name = st.nextToken();
				PcInstance use = World.findPc(name);
				List<ItemInstance> _list = use.getInventory().getList();
					o.toSender(S_MarblesIvenInfo.clone(BasePacketPooling.getPool(S_MarblesIvenInfo.class), _list));
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "인벤검색 캐릭터", 20);
				}
				return true;

			} else if (key.equalsIgnoreCase(".경험치")) {
				try {
					toCharacterInfo(o, st);
				} catch (Exception localException54) {
					ChattingController.toChatting(o, new StringBuilder()
							.append(".").append("정보 charname").toString(), 20);
				}
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "이팩트")
					|| key.equalsIgnoreCase(Common.COMMAND_TOKEN + "이펙트")
					|| key.equalsIgnoreCase(Common.COMMAND_TOKEN + "이팩")) {
				try {
					int effect = Integer.valueOf(st.nextToken());
					o.toSender(S_ObjectEffect.clone(
							BasePacketPooling.getPool(S_ObjectEffect.class), o,
							effect), true);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "이팩트 이팩트번호", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "스팟")) {
				spot(o, st);
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "콜롯세움")) {
				try {
					String msg = st.nextToken();

					if (msg.equalsIgnoreCase("사용")) {
						Lineage.colosseum_giran = true;
					} else if (msg.equalsIgnoreCase("중지")) {
						Lineage.colosseum_giran = false;
					}

					ChattingController.toChatting(o, String.format(
							"기란 콜롯세움 기능 : %s",
							Lineage.colosseum_giran ? "사용" : "중지"),
							Lineage.CHATTING_MODE_MESSAGE);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "콜롯세움 사용/중지", Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "만피")) {
				try {
					String name = st.nextToken();
					PcInstance use = World.findPc(name);
					if (use != null)
						use.setNowHp(use.getTotalHp());
					else
						ChattingController.toChatting(o, "'" + name
								+ "' 사용자를 찾을 수 없습니다.", 20);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "만피 [캐릭명]", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "만엠")) {
				try {
					String name = st.nextToken();
					PcInstance use = World.findPc(name);
					if (use != null)
						use.setNowMp(use.getTotalMp());
					else
						ChattingController.toChatting(o, "'" + name
								+ "' 사용자를 찾을 수 없습니다.", 20);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "만엠 [캐릭명]", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "배치")) {
				try {
					배치(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, "배치 [몹/엔] [이름]", 20);
					ChattingController.toChatting(o,
							"<몬스터의경우 : .배치 몹 이름 마리수,랜덤여부,랜덤좌표>", 20);
					ChattingController.toChatting(o,
							"<몬스터의경우 : 랜덤여부 0-안함 / 1-랜덤>", 20);
					ChattingController.toChatting(o,
							"<몬스터의경우 : 마리수까지만입력하면 나머지는 안함으로 자동입력>", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "투명")) {
				o.setTransparent(!o.isInvis());
				o.setInvis(!o.isInvis());
				o.toTeleport(o.getX(), o.getY(), o.getMap(), false);
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "반투명")) {
		            if (o.isTransparent()) {
		               o.setTransparent(false);
		               o.setInvis(true);
		            } else {
		               o.setInvis(!o.isInvis());
		            }
		            o.toTeleport2(o.getX(), o.getY(), o.getMap(), false);
		            return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "힐")) {
				o.setNowHp(o.getMaxHp());
				o.setNowMp(o.getMaxMp());
				ChattingController.toChatting(o, "hp/mp 회복 되었습니다.", 20);
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "힐올")) {
				for (PcInstance pc : World.getPcList()) {
					pc.setNowHp(pc.getTotalHp());
					pc.setNowMp(pc.getTotalMp());
					ChattingController.toChatting(pc, "hp/mp 회복 되었습니다.", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "복구")) {
				try {
					toRepairLocation(o, st);
					// } catch (Exception e) {
				} catch (Exception localException42) {
					ChattingController.toChatting(o, "[서버알림] .복구 [캐릭명]", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "위치")) {
				ChattingController.toChatting(
						o,
						String.format("x:%d y:%d map:%d", o.getX(), o.getY(),
								o.getMap()), 20);
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "리로드")) {
				try {
					Reload(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, "[서버알림] .리로드 (해당데이터베이스)", 20);
					ChattingController.toChatting(o, "[밴아이피, 박스, 아이템, 백그라운드, 스킬]", 20);
					ChattingController.toChatting(o, "[엔피씨, 상점, 드랍, 몬스터, 몬스터스폰]", 20);
					ChattingController.toChatting(o, "[보스, 몬스터스킬, 변신, 번들 ]", 20);
					ChattingController.toChatting(o, "[찬스번들, 경험치패널티, 공성]", 20);

				}
				return true;
				
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "리로드2")) {
				try {
					toReLoad(o, st);
				} catch (Exception localException20) {
					if (o != null)
						ChattingController.toChatting(o, "[서버알림] .리로드2 (해당데이터베이스)", 20);
					ChattingController.toChatting(o, "[서버설정,옥션,아지트,프레임,공지,엔피씨,퀴즈]", 20);
					ChattingController.toChatting(o, "[몬스터,아이템,세트템,드랍,상점,박스,보스,공성,변신,스킬]", 20);
					ChattingController.toChatting(o, "[서먼,공지사항,밴아이피,아이템스킬,몬스터스킬,텔레포트홈,서버메세지]", 20);
					ChattingController.toChatting(o, "[텔레포트리셋,엔피씨텔레포트,아이템텔레포트,레벨보상,스핵]", 20);
				}
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "개경")) {
				try {
					toDogGameControl(o, st);
				} catch (Exception localException60) {
					ChattingController.toChatting(o, new StringBuilder()
							.append(TOKEN).append("개경 [시작,종료]").toString(), 20);
				}
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "킬수갱신")) {
				try {
					업데이트(o);
				} catch (Exception e) {
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "선물")) {
	        	try {
	        		PcInstance pc = World.findPc(st.nextToken());
	        		if (pc != null) {
	        			if (pc.getInventory() != null)
	        				toGiveItem(o, pc, st);
	        		} else
	        			ChattingController.toChatting(o, "해당 캐릭터가 존재하지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
	        	} catch (Exception e) {
	        		ChattingController.toChatting(o, ".선물 케릭명 아이템 갯수 인챈 bless(0.축복 1.일반 2.저주)", Lineage.CHATTING_MODE_MESSAGE);
	        	}
	        	return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "레벨")) {
				try {
					레벨(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, "[서버알림] .레벨 [캐릭명] [레벨]",
							20);
				}
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "스핵제외")) {
				try {
					String pcName = st.nextToken();
					PcInstance pc = World.findPc(pcName);

					if (pc == null) {
						ChattingController.toChatting(o,
								String.format("[%s] 캐릭터는 존재하지 않습니다.", pcName),
								Lineage.CHATTING_MODE_MESSAGE);
						return true;
					} else {
						HackNoCheckDatabase.append(o, pc);
					}
				} catch (Exception e) {
					if (o != null)
						ChattingController.toChatting(o, Common.COMMAND_TOKEN
								+ "스핵제외 아이디", Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "스핵체크")) {
				try {
					String pcName = st.nextToken();
					PcInstance pc = World.findPc(pcName);

					if (pc == null) {
						ChattingController.toChatting(o,
								String.format("[%s] 캐릭터는 존재하지 않습니다.", pcName),
								Lineage.CHATTING_MODE_MESSAGE);
						return true;
					} else {
						HackNoCheckDatabase.remove(o, pc);
					}
				} catch (Exception e) {
					if (o != null)
						ChattingController.toChatting(o, Common.COMMAND_TOKEN
								+ "스핵체크 아이디", Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "저장")) {
				Connection con = null;
				try {
					con = DatabaseConnection.getLineage();
					CharactersDatabase.close(con);
				} catch (Exception e) {
				} finally {
					DatabaseConnection.close(con);
				}
				ChattingController.toChatting(o, "저장 완료.", 20);
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "혈포지급")) {
				try {
					clanpoint(o, st);
				} catch (Exception e) {
					if (o != null)
						ChattingController.toChatting(o,
								"[서버알림] .혈포지급 [타겟군주이름] [점수]", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "킬수조작")) {
				try {
					킬수조작(o, st);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "킬수조작 [유저명] [삭제할갯수]", 20);
				}
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "리로드던전")) {
				Connection con = null;
				try {
					con = DatabaseConnection.getLineage();
					DungeonDatabase.init(con);
					DungeonPartTimeDatabase.init(con);
					ChattingController.toChatting(o, "ok!", 20);
				} catch (Exception e) {
					ChattingController.toChatting(o, ".리로드던전", 20);
				} finally {
					DatabaseConnection.close(con);
				}
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "혈맹리로드")) {
				Connection con = null;
				try {
					con = DatabaseConnection.getLineage();
					ClanController.close(con);
					ClanController.init(con);
				} catch (Exception e) {
				} finally {
					DatabaseConnection.close(con);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "오픈대기")) {
				try {
					serverOpenWait();
				} catch (Exception e) {
					if (o != null)
						ChattingController.toChatting(o, ".오픈대기", 20);
				}
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "오픈시작")) {
				try {
					serverOpen();
				} catch (Exception e) {
					if (o != null)
						ChattingController.toChatting(o, ".오픈시작", 20);
				}
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "게시판리로드")) {
				try {

					UserAdenaSellController.AdminAdenaDBList();

				} catch (Exception e) {
					if (o != null)
						ChattingController.toChatting(o, "[사용방법] .게시판리로드",
								Lineage.CHATTING_MODE_MESSAGE);
				}
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "섭폭")
					|| key.equalsIgnoreCase(Common.COMMAND_TOKEN + "111")) {
				toSoket2(o);
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "공지")) {
				try {
					toNotice(o, st);
				} catch (Exception localException57) {
					ChattingController.toChatting(o, ".공지", 20);
				}
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "스왑")) {
				NpcSpawnlistDatabase.itemSwap.toTalk((PcInstance) o, null);
				return true;
			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "에스크")) {
				int num = Integer.valueOf(st.nextToken());
				o.toSender(S_MessageYesNo.clone(
						BasePacketPooling.getPool(S_MessageYesNo.class), num));
				return true;

			} else if (key.equalsIgnoreCase(Common.COMMAND_TOKEN + "보호모드")) {
				try {
					String msg = st.nextToken();

					if (msg.equalsIgnoreCase("켬")) {
						Lineage.유저보호모드 = true;
					} else if (msg.equalsIgnoreCase("끔")) {
						Lineage.유저보호모드 = false;
					}

					ChattingController.toChatting(o, String.format(
							"유저 사망 패널티 보호모드: %s", Lineage.유저보호모드 ? "켬" : "끔"),
							Lineage.CHATTING_MODE_MESSAGE);
				} catch (Exception e) {
					ChattingController.toChatting(o, Common.COMMAND_TOKEN
							+ "보호모드 켬/끔", Lineage.CHATTING_MODE_MESSAGE);
				}

				return true;
			}

		} catch (Exception e) {
			// lineage.share.System.println(CommandController.class.toString()+" : toCommand(PcInstance pc, String cmd)");
			// lineage.share.System.println(e);
		}
		return false;
	}

	public static void toNotice(object paramobject,
			StringTokenizer paramStringTokenizer) {
		String str = paramStringTokenizer.nextToken();
		if (paramStringTokenizer.hasMoreTokens()) {
			str = new StringBuilder().append(str).append(" ")
					.append(paramStringTokenizer.nextToken()).toString();
		}
		paramobject.setBoardView(false);
		String[] arrayOfString = new String[4];
		// 공지사항은 변동될일도 없구 입력된걸 추가하거나 더하면 되는일이기에
		// 단일 속성을 가진다 라구 생각하시면될거같아용 여기서 변하는거는 요거분이잔아요
		// 하지만 이것은 아마 아 이거 유저가 쓰는거아니져ㅑ? 영자가 쓴느거에요 역시
		// 영자혼자만 쓰니까
		// 스트링은 여러문자를 합칠때 어떻게되죵? 띠어쓰기만 안하면 잘되요
		// 띄어쓰기보다는 스트링은 여러개 합치면 메모리누수가 많ㄱ세죠? 넴
		// 근데 스트링 토커는 띄어쓰기마다 스트링을 하나씩 나눈단말이죠? 네
		// 이걸 합칠때 +를 쓰면 굉장한 누수가 일어날거에여
		// 그래서 어짜피 영자혼자 쓸거니까 단일속성을 띄고
		// 여러개를 합쳐야하는데 메모리를 적게 먹어야하니까
		// 빌더를 썻다 라는 결론이나옵니당 네 ㅋㅋㅋ 그래도 이런게 보여서 나름 공부를 햇습니다 ㅋ
		// 덕분에 저도 알게되궄ㅋㅋㅋㅋㅋ 그러면 상대적으로
		arrayOfString[0] = new StringBuilder().append("테스트").append("서버")
				.toString();
		arrayOfString[1] = "* 공 지 사 항 *";
		StringBuilder localStringBuilder = new StringBuilder();
		localStringBuilder.append("----------------------------\n");
		localStringBuilder.append(new StringBuilder().append(str).append("\n")
				.toString());
		localStringBuilder.append("----------------------------\n");
		arrayOfString[2] = localStringBuilder.toString();
		for (PcInstance localPcInstance : World.getPcList()) {
			localPcInstance.toSender(S_LetterNotice.clone(
					BasePacketPooling.getPool(S_LetterNotice.class),
					arrayOfString));
		}
	}

	private static void toSoket2(object o) {
		try {
			if (Lineage.running == true) {
				Lineage.running = false;
				ChattingController
						.toChatting(o, String
								.format("[******] 현재 모든 클라이언트 접속 시도를 무시합니다."),
								20);
				ChattingController.toChatting(o,
						String.format("[******] 한번 더 입력하면 접속가능상태가 됩니다."), 20);
			} else if (Lineage.running == false) {
				Lineage.running = true;
				ChattingController
						.toChatting(o, String
								.format("[******] 현재 모든 클라이언트 접속 시도를 허용합니다."),
								20);
				ChattingController.toChatting(o,
						String.format("[******] 한번 더 입력하면 접속불가능상태가 됩니다."), 20);
			}
		} catch (Exception e) {
			ChattingController.toChatting(o,
					String.format("[******] .섭폭 .111"), 20);
		}
	}

	/**
	 * 
	 * @param
	 * @return
	 * 2017-08-29
	 * by all_night.
	 */
	static public void serverOpenWait() {
		Lineage.open_wait = true;
		Lineage.level_max = 10;
		Lineage.rate_drop = 0;
		Lineage.rate_aden = 0;
	}

	/**
	 * 
	 * @param
	 * @return
	 * 2017-08-29
	 * by all_night.
	 */
	static public void serverOpen() {
		if (Lineage.open_wait) {
			Lineage.open_wait = false;
			Lineage.init(true);
			World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, Lineage.CHATTING_MODE_MESSAGE, "서버 오픈대기 종료!"));
			World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, Lineage.CHATTING_MODE_MESSAGE, "지금부터 정상 배율이 적용됩니다."));
			World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, Lineage.CHATTING_MODE_MESSAGE, String.format("서버에 오신것을 진심으로 환영합니다.")));
		}
	}

	/**
	 * 화면안에있는 몬스터 정리처리하는 함수.
	 * 
	 * @param o
	 */
	public static void toClearMonster(object o) {
		o.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), o, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 245), true);
		for (object inside_o : o.getInsideList()) {
			if (!inside_o.isDead() && inside_o instanceof MonsterInstance && inside_o.getSummon() == null && !FightController.isFightMonster(inside_o)) {
				MonsterInstance mon = (MonsterInstance) inside_o;
				DamageController.toDamage((Character) o, mon, mon.getTotalHp(), Lineage.ATTACK_TYPE_MAGIC);
			}
		}

		ChattingController.toChatting(o, "몬스터 정리 완료.", Lineage.CHATTING_MODE_MESSAGE);
	}

	/*
	 * 검색
	 */
	static private void 검색(object o, StringTokenizer st) {
		int type = Integer.parseInt(st.nextToken());
		String name = String.valueOf(st.nextToken());
		searchObject(o, type, "%" + name + "%");
	}

	private static void searchObject(object o, int type, String name) {
		try {
			String str1 = null;
			String str2 = null;
			int count = 0;
			Connection con = null;
			con = DatabaseConnection.getLineage();
			PreparedStatement statement = null;
			switch (type) {
			case 0: // etcitem
				statement = con
						.prepareStatement("select item_id, 아이템이름 from item where 아이템이름 Like '"
								+ name + "'");
				break;
			case 1: // npc
				statement = con
						.prepareStatement("select uid, name from npc where name Like '"
								+ name + "'");
				break;
			case 2: // 몬스터
				statement = con
						.prepareStatement("select monster_id, name from monster where name Like '"
								+ name + "'");
				break;
			default:
				break;
			}
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				str1 = rs.getString(1);
				str2 = rs.getString(2);
				ChattingController.toChatting(o, "[" + str1 + "]-[" + str2
						+ "]-", 20);
				count++;
			}
			rs.close();
			statement.close();
			con.close();
			ChattingController.toChatting(o, "[서버알림] 총 [" + count
					+ "]개 검색이 되었습니다.", 20);
		} catch (Exception e) {
		}
	}

	private static void 레벨(object o, StringTokenizer tok) {
		try {
			String user = tok.nextToken();
			PcInstance target = World.findPc(user);
			int level = Integer.parseInt(tok.nextToken());
			if (target == null) {
				ChattingController.toChatting(o, "그러한 캐릭터는 접속중이지 않습니다.", 20);
				return;
			}

			if (level == target.getLevel()) {
				return;
			}

			if (level <= 0 && level >= 100) {
				ChattingController.toChatting(o, "1-99의 범위에서 지정해 주세요", 20);
				return;
			}
			Exp e = new Exp();
			if (level > target.getLevel()) {
				e = ExpDatabase.find(level - 1);
			} else {
				e = ExpDatabase.find(level);
			}
			if (level > target.getLevel()) {
				target.setExp(e.getBonus());
			} else {
				((Character) target).setLevel(e.getLevel());
				target.setExp(e.getBonus() - e.getExp());
				target.setMaxHp(target.getMaxHp()
						- CharacterController.toStatusUP((Character) target,
								true));
				target.setMaxMp(target.getMaxMp()
						- CharacterController.toStatusUP((Character) target,
								false));
			}
			target.toSender(S_CharacterStat.clone(
					BasePacketPooling.getPool(S_CharacterStat.class),
					(Character) target));

			ChattingController.toChatting(o, target.getName() + "님의 레벨이 변경 되었습니다.", 20);
		} catch (Exception e) {
			ChattingController.toChatting(o, ".레벨 [캐릭명] [레벨]", 20);

		}
	}

	static public void 던전시간(object o) {
		PcInstance pc = (PcInstance) o;

		List<DungeonPartTime> list = DungeonController.find(pc);
		if (list.size() == 0 || list == null) {
			ChattingController.toChatting(pc, "갱신된 시간이 존재하지 않습니다.", 20);
			return;
		}

		for (DungeonPartTime d : list) {
			DungeonPartTime dpt = DungeonPartTimeDatabase.findUid(d.getUid());
			if (dpt == null) {
				ChattingController.toChatting(pc, "갱신된 시간이 존재하지 않습니다.", 20);
				continue;
			}
			
			 int dungeonTimeInSeconds = d.getTime();
			    final int SECONDS_PER_HOUR = 3600;
			    final int SECONDS_PER_MINUTE = 60;

			    int hours = dungeonTimeInSeconds / SECONDS_PER_HOUR;
			    int remainingSecondsAfterHours = dungeonTimeInSeconds % SECONDS_PER_HOUR;
			    int minutes = remainingSecondsAfterHours / SECONDS_PER_MINUTE;
			    int seconds = remainingSecondsAfterHours % SECONDS_PER_MINUTE;

			    String formattedTime;
			    if (hours > 0) {
			        formattedTime = String.format("%s: %d시간 %d분 %d초", dpt.getName(), hours, minutes, seconds);
			    } else if (minutes > 0) {
			        formattedTime = String.format("%s: %d분 %d초", dpt.getName(), minutes, seconds);
			    } else {
			        formattedTime = String.format("%s: %d초", dpt.getName(), seconds);
			    }

			    ChattingController.toChatting(o, formattedTime, Lineage.CHATTING_MODE_MESSAGE);
			}
	}
	

	/**
	 * 셧다운 처리 함수.
	 * 
	 * @param o
	 * @param st
	 */
	static private void toShutdown(object o, StringTokenizer st) {
		Shutdown.getInstance();
		Shutdown.shutdown_delay = Integer.valueOf(st.nextToken());
		System.exit(0);
	}

	/**
	 * 채금 해제 2019-07-07 by connector12@nate.com
	 */
	static public void toChattingCloseRemove(object o, StringTokenizer st) {
		String name = st.nextToken();
		PcInstance pc = World.findPc(name);

		if (pc != null)
			BuffController.remove(pc, ChattingClose.class);

		CharactersDatabase.chattingCloseRemove(name);

		if (o != null)
			ChattingController.toChatting(o,
					String.format("[채금 해제] 캐릭터: %s", name),
					Lineage.CHATTING_MODE_MESSAGE);
	}

	/**
	 * 채금처리. : ScreenRenderComposite 에서 사용중
	 * 
	 * @param o
	 * @param st
	 */
	static public void toChattingClose(object o, StringTokenizer st) {
		PcInstance pc = World.findPc(st.nextToken());
		if (pc != null) {
			int time = Integer.valueOf(st.nextToken());
			ChattingClose.init(pc, time);

			// %0의 채팅을 금지시켰습니다.
			if (o != null)
				o.toSender(S_Message.clone(
						BasePacketPooling.getPool(S_Message.class), 287,
						pc.getName()));
		}
	}

	static private void Reload(object o, StringTokenizer st) {
		String number = String.valueOf(st.nextToken());
		try {
			Connection con = null;
			switch (number) {
			case "밴아이피":
				con = DatabaseConnection.getLineage();
				BadIpDatabase.init(con);
				break;
			// case "박스":
			// L1TreasureBox.load();
			// break;
			case "아이템":
				try {
					con = DatabaseConnection.getLineage();
					ItemDatabase.init(con);
					ItemSetoptionDatabase.init(con);
					ItemSkillDatabase.init(con);
					ItemBundleDatabase.init(con);
					ItemTeleportDatabase.init(con);
					// MagicdollListDatabase.init(con);
					ChattingController.toChatting(o, "아이템 테이블이 리로드 되었습니다.", 20);
				} catch (Exception e2) {
				} finally {
					DatabaseConnection.close(con);
				}
				break;
			/*
			 * case "서버설정": Lineage.init(); GameSetting.load(); break;
			 */
			case "백그라운드":
				try {
					con = DatabaseConnection.getLineage();
					BackgroundDatabase.close();
					Thread.sleep(1000);
					BackgroundDatabase.init(con);
					ChattingController.toChatting(o, "백그라운드 테이블이 리로드 되었습니다.",
							20);
				} catch (Exception e2) {
				} finally {
					DatabaseConnection.close(con);
				}
				break;
			case "스킬":
				try {
					con = DatabaseConnection.getLineage();
					SkillDatabase.init(con);
					SkillDatabase.getList();
					SkillDatabase.getSize();
					ChattingController.toChatting(o, "스킬 테이블이 리로드 되었습니다.", 20);
				} catch (Exception e2) {
				} finally {
					DatabaseConnection.close(con);
				}
				break;
			case "엔피씨":
				try {
					con = DatabaseConnection.getLineage();
					NpcDatabase.init(con);
					NpcTeleportDatabase.init(con);
					NpcSpawnlistDatabase.close();
					Thread.sleep(1000);
					NpcSpawnlistDatabase.init(con);
					ChattingController.toChatting(o, "엔피씨 테이블이 리로드 되었습니다.", 20);
				} catch (Exception e2) {
				} finally {
					DatabaseConnection.close(con);
				}
				break;
			case "상점":
				try {
					con = DatabaseConnection.getLineage();
					NpcShopDatabase.ShopInit();
					NpcShopDatabase.init(con);
					ChattingController.toChatting(o, "상점 테이블이 리로드 되었습니다.", 20);
				} catch (Exception e2) {
				} finally {
					DatabaseConnection.close(con);
				}
				break;
			case "드랍":
				try {
					con = DatabaseConnection.getLineage();
					MonsterDropDatabase.DropInit();
					MonsterDropDatabase.init(con);
					ChattingController.toChatting(o, "몬스터 드랍 테이블이 리로드 되었습니다.",
							20);
				} catch (Exception e2) {
				} finally {
					DatabaseConnection.close(con);
				}
				break;
			case "몬스터":
				try {
					con = DatabaseConnection.getLineage();
					MonsterDatabase.init(con);
					ChattingController.toChatting(o, "몬스터 드랍 테이블이 리로드 되었습니다.",
							20);
				} catch (Exception e2) {
				} finally {
					DatabaseConnection.close(con);
				}
				break;
			case "몬스터스폰":
				try {
					con = DatabaseConnection.getLineage();
					MonsterSpawnlistDatabase.close();
					Thread.sleep(1000);
					MonsterSpawnlistDatabase.init(con);
					ChattingController.toChatting(o, "몬스터 스폰 테이블이 리로드 되었습니다.",
							20);
				} catch (Exception e2) {
				} finally {
					DatabaseConnection.close(con);
				}
				break;
			case "보스":
				try {
					con = DatabaseConnection.getLineage();
					MonsterBossSpawnlistDatabase.init(con);
					ChattingController.toChatting(o, "몬스터 보스 테이블이 리로드 되었습니다.",
							20);
				} catch (Exception e2) {
				} finally {
					DatabaseConnection.close(con);
				}
				break;
			case "몬스터스킬":
				try {
					con = DatabaseConnection.getLineage();
					MonsterSkillDatabase.init(con);
					ChattingController.toChatting(o, "몬스터 스킬 테이블이 리로드 되었습니다.",
							20);
				} catch (Exception e2) {
				} finally {
					DatabaseConnection.close(con);
				}
				break;
			case "변신":
				try {
					con = DatabaseConnection.getLineage();
					PolyDatabase.init(con);
					PolyDatabase.getSize();
					ChattingController.toChatting(o, "변신 테이블이 리로드 되었습니다.", 20);
				} catch (Exception e2) {
				} finally {
					DatabaseConnection.close(con);
				}
				break;
			case "번들":
				ItemBundleDatabase.reload();
				ChattingController.toChatting(o, "번들 테이블이 리로드 되었습니다.", 20);
				break;
			case "찬스번들":
				ItemChanceBundleDatabase.reload();
				ChattingController.toChatting(o, "찬스번들 테이블이 리로드 되었습니다.", 20);
				break;

			case "경험치패널티":
				ExpPaneltyDatabase.reload();
				ChattingController.toChatting(o, "경험치패널티 테이블이 리로드 되었습니다.", 20);
				break;

			case "공성":
				try {
					con = DatabaseConnection.getLineage();
					KingdomController.reload();
					ChattingController.toChatting(o, "kingdom 테이블이 리로드 되었습니다.",
							20);
				} catch (Exception e2) {
				} finally {
					DatabaseConnection.close(con);
				}
			default:
				break;

			}
		} catch (Exception e) {
			ChattingController.toChatting(o, "[서버알림] .리로드 (해당데이터베이스)", 20);
			ChattingController.toChatting(o, "[밴아이피, 박스, 아이템, 백그라운드, 스킬]", 20);
			ChattingController.toChatting(o, "[엔피씨, 상점, 드랍, 몬스터, 몬스터스폰]", 20);
			ChattingController.toChatting(o, "[보스, 몬스터스킬, 변신, 번들 ]", 20);
			ChattingController.toChatting(o, "[찬스번들, 경험치패널티, 공성]", 20);
		}
	}

	/**
	 * 귀환. by feel
	 * 
	 * @param o
	 * @param st
	 */
	static private void teleporthome(object o, StringTokenizer st) {
		String chk = st.nextToken().toLowerCase();
		if (o.isDead()) {
			ChattingController.toChatting(o, "죽은 상태에선 사용할 수 없습니다.",
					Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (chk.equalsIgnoreCase("말섬")) {
			o.toTeleport(32582, 32931, 0, true); // 말섬
			ChattingController.toChatting(o, String.format("말하는 섬으로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		if (chk.equalsIgnoreCase("글말")) {
			o.toTeleport(32613, 32796, 4, true); // 글루딘
			ChattingController.toChatting(o, String.format("글루딘 마을로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		if (chk.equalsIgnoreCase("켄말")) {
			o.toTeleport(33050, 32780, 4, true); // 켄트
			ChattingController.toChatting(o, String.format("켄트성 마을로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		if (chk.equalsIgnoreCase("켄성")) {
			o.toTeleport(32737, 32784, 15, true); // 켄트성
			ChattingController.toChatting(o, String.format("켄트성으로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}

		if (chk.equalsIgnoreCase("윈말")) {
			o.toTeleport(32610, 33188, 4, true); // 윈다우드
			ChattingController.toChatting(o,
					String.format("윈다우드 마을로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		if (chk.equalsIgnoreCase("윈성")) {
			o.toTeleport(32735, 32786, 29, true); // 윈다우드성
			ChattingController.toChatting(o, String.format("윈다우드성으로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		if (chk.equalsIgnoreCase("은말")) {
			o.toTeleport(33080, 33392, 4, true); // 은기사
			ChattingController.toChatting(o, String.format("은기사 마을로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		if (chk.equalsIgnoreCase("화말")) {
			o.toTeleport(32750, 32442, 4, true); // 화전민
			ChattingController.toChatting(o, String.format("화전민 마을로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		if (chk.equalsIgnoreCase("요말")) {
			o.toTeleport(33051, 32340, 4, true); // 요정숲
			ChattingController.toChatting(o,
					String.format("요정의숲 마을로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		if (chk.equalsIgnoreCase("기란")) {
			o.toTeleport(33442, 32797, 4, true); // 기란
			ChattingController.toChatting(o, String.format("기란 마을로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		if (chk.equalsIgnoreCase("인나드")) {
			o.toTeleport(32677, 32866, 58, false); // 인나드
			ChattingController.toChatting(o, String.format("인나드 마을로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		if (chk.equalsIgnoreCase("웰던")) {
			o.toTeleport(33702, 32500, 4, false); // 웰던
			ChattingController.toChatting(o, String.format("웰던 마을로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		if (chk.equalsIgnoreCase("하이네")) {
			o.toTeleport(33611, 33244, 4, false); // 하이네
			ChattingController.toChatting(o, String.format("하이네 마을로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		if (chk.equalsIgnoreCase("영자")) {
			o.toTeleport(32736, 32801, 99, false); // 영자방
			ChattingController.toChatting(o, String.format("영자방으로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		if (chk.equalsIgnoreCase("후원")) {
			o.toTeleport(32801, 32797, 5000, false); // 후원방
			ChattingController.toChatting(o, String.format("후원방으로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		if (chk.equalsIgnoreCase("잊섬")) {
			o.toTeleport(32821, 32850, 70, false); // 잊섬
			ChattingController.toChatting(o, String.format("잊혀진 섬으로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		if (chk.equalsIgnoreCase("지옥")) {
			o.toTeleport(32740, 32797, 666, false); // 지옥
			ChattingController.toChatting(o, String.format("지옥으로 이동하였습니다."),
					Lineage.CHATTING_MODE_MESSAGE);
		}
	}
	static private void 혈맹선물(object o, StringTokenizer st) {
		String bloodname = st.nextToken();

		String name = st.nextToken();

		int count = 1;
		if (st.hasMoreTokens()) {
			count = Integer.parseInt(st.nextToken());
		}
		int en = 0;
		if (st.hasMoreTokens()) {
			en = Integer.parseInt(st.nextToken());
		}
		int bress = 1;
		if (st.hasMoreTokens()) {
			bress = Integer.parseInt(st.nextToken());
		}

		int itemid = 0;
		try {
			itemid = Integer.parseInt(name);
		} catch (NumberFormatException e) {
			itemid = ItemDatabase.findItemId(name);
			if (itemid == 0) {
				name = ItemDatabase.findItemIdByNameWithoutSpace2(name);
				if (name == null) {
					ChattingController.toChatting(o, "[서버알림] 해당 아이템이 발견되지 않습니다.", 20);
					return;
				}
			}
		}

		Item i = null;
		if (itemid == 0) {
			i = ItemDatabase.find2(name);
		} else {
			i = ItemDatabase.find_ItemId(itemid);
		}

		if (i != null) {
			ItemInstance temp = ItemDatabase.newInstance(i);
			temp.setCount(count);
			temp.setEnLevel(en);
			temp.setBress(bress);
			temp.setDefinite(true);
			for (PcInstance pc : World.getPcList()) {
				if (pc.getClanName().equalsIgnoreCase(bloodname)) {
					pc.getInventory().append(temp, count);
					ChattingController.toChatting(pc, "[서버알림] 혈맹원 전체 지급 완료.", 20);
				}
			}
			ChattingController.toChatting(o, String.format("[서버알림] 혈맹선물 지급 완료.[%s]", bloodname), 20);

		}
	}
	static private void 전체선물(object o, StringTokenizer st) {
		String name = st.nextToken();

		int count = 1;
		if (st.hasMoreTokens()) {
			count = Integer.parseInt(st.nextToken());
		}
		int en = 0;
		if (st.hasMoreTokens()) {
			en = Integer.parseInt(st.nextToken());
		}
		int bress = 1;
		if (st.hasMoreTokens()) {
			bress = Integer.parseInt(st.nextToken());
		}

		int itemid = 0;
		try {
			itemid = Integer.parseInt(name);
		} catch (NumberFormatException e) {
			itemid = ItemDatabase.findItemId(name);
			if (itemid == 0) {
				name = ItemDatabase.findItemIdByNameWithoutSpace2(name);
				if (name == null) {
					ChattingController.toChatting(o,
							"[서버알림] 해당 아이템이 발견되지 않습니다.", 20);
					return;
				}
			}
		}

		Item i = null;
		if (itemid == 0) {
			i = ItemDatabase.find2(name);
		} else {
			i = ItemDatabase.find_ItemId(itemid);
		}

		if (i != null) {
			ItemInstance temp = ItemDatabase.newInstance(i);
			temp.setCount(count);
			temp.setEnLevel(en);
			temp.setBress(bress);
			temp.setDefinite(true);
			for (PcInstance pc : World.getPcList()) {
				pc.getInventory().append(temp, count);
			}
			ChattingController.toChatting(o, "[서버알림] 전체선물 지급 완료.", 20);
			o.toSender(new S_SoundEffect(17788), true);
		}
	}

	static private void toGiveItem(object o, object pc, StringTokenizer st) {
		String name = st.nextToken();
		long count = 1;
		int en = 0;
		int bless = 1;

		if (st.hasMoreTokens())
			count = Long.valueOf(st.nextToken());
		if (st.hasMoreTokens())
			en = Integer.valueOf(st.nextToken());
		if (st.hasMoreTokens())
			bless = Integer.valueOf(st.nextToken());

		Item i = ItemDatabase.find2(name);

		if (i != null) {
			ItemInstance temp = pc.getInventory().find(i.getName(), bless, i.isPiles());

			if (temp != null && (temp.getBress() != bless || temp.getEnLevel() != en))
				temp = null;

			if (temp == null) {
				// 겹칠수 있는 아이템이 존재하지 않을경우.
				if (i.isPiles()) {
					temp = ItemDatabase.newInstance(i);
					temp.setObjectId(ServerDatabase.nextItemObjId());
					temp.setBress(bless);
					temp.setEnLevel(en);
					temp.setCount(count);
					temp.setDefinite(true);
					pc.getInventory().append(temp, true);
				} else {
					for (int idx = 0; idx < count; idx++) {
						temp = ItemDatabase.newInstance(i);
						temp.setObjectId(ServerDatabase.nextItemObjId());
						temp.setBress(bless);
						temp.setEnLevel(en);
						temp.setDefinite(true);
						pc.getInventory().append(temp, true);
					}
				}
			} else {
				// 겹치는 아이템이 존재할 경우.
				pc.getInventory().count(temp, temp.getCount() + count, true);
			}

			// 알림.
			if (pc != null) {
				if (temp.getItem().getType1().equalsIgnoreCase("weapon") || temp.getItem().getType1().equalsIgnoreCase("armor")) {
					ChattingController.toChatting(pc, String.format("운영자 선물: +%d %s(%d)", en, i.getName(), count), Lineage.CHATTING_MODE_MESSAGE);
					ChattingController.toChatting(o, String.format("+%d %s(%d) 선물: %s", en, i.getName(), count, pc.getName()), Lineage.CHATTING_MODE_MESSAGE);
					//pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 16139), true);
					
				} else {
					ChattingController.toChatting(pc, String.format("운영자 선물: %s(%d)", i.getName(), count), Lineage.CHATTING_MODE_MESSAGE);
					ChattingController.toChatting(o, String.format("%s(%d) 선물: %s", i.getName(), count, pc.getName()), Lineage.CHATTING_MODE_MESSAGE);
					//pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 16139), true);
				}
			}
		} else
			ChattingController.toChatting(o, String.format("아이템 (%s) 생성 실패", name), Lineage.CHATTING_MODE_MESSAGE);
	}

	/**
	 * 스킬 올마
	 * 
	 * @param o
	 * @param st
	 */
	static private void toSkillAllMaster(object o, StringTokenizer st) {
		PcInstance pc = (PcInstance) o;
		if (st.hasMoreTokens()) {
			pc = World.findPc(st.nextToken());
		}
		if (pc != null) {
			switch (pc.getClassType()) {
			case Lineage.LINEAGE_CLASS_ROYAL:
				// 1~2
				for (int i = 1; i < 3; ++i)
					for (int j = 0; j < 5; ++j)
						SkillController.append(pc, i, j, false);
				break;
			case Lineage.LINEAGE_CLASS_KNIGHT:
				// 1
				for (int i = 1; i < 2; ++i)
					for (int j = 0; j < 5; ++j)
						SkillController.append(pc, i, j, false);
				break;
			case Lineage.LINEAGE_CLASS_ELF:
				// 1~6
				for (int i = 1; i < 7; ++i)
					for (int j = 0; j < 5; ++j)
						SkillController.append(pc, i, j, false);
				break;
			case Lineage.LINEAGE_CLASS_WIZARD:
				// 1~10
				for (int i = 1; i < 11; ++i)
					for (int j = 0; j < 5; ++j)
						SkillController.append(pc, i, j, false);
				break;
			}
			SkillController.sendList(pc);

			ChattingController.toChatting(o, "스킬올마 완료.",
					Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	static private void 배치(object o, StringTokenizer st) {
		Connection con = null;
		String type = st.nextToken();
		String name = st.nextToken();

		if (type.equalsIgnoreCase("엔")) {
			int npcid = 0;
			try {
				npcid = Integer.parseInt(name);
			} catch (NumberFormatException e) {
				npcid = NpcDatabase.findNpcUid(name);
				if (npcid == 0) {
					name = NpcDatabase.findNpcUidBy(name);
					if (name == null) {
						ChattingController.toChatting(o,
								"[서버알림] 해당 엔피씨가 발견되지 않습니다.", 20);
						return;
					}
				}
			}

			Npc npc = null;
			if (npcid == 0) {
				npc = NpcDatabase.find2(name);
			} else {
				npc = NpcDatabase.find(npcid);
			}

			if (npc == null) {
				ChattingController.toChatting(o, "해당 NPC가 발견되지 않습니다.", 20);
				return;
			}
			try {
				con = DatabaseConnection.getLineage();
				NpcSpawnlistDatabase.toSpawnNpc(npc.getName(), npc.getName(),
						"", o.getX(), o.getY(), o.getMap(), o.getHeading(), 0);
				NpcSpawnlistDatabase.insert(con, npc.getName(), npc.getName(),
						o.getX(), o.getY(), o.getMap(), o.getHeading(), 0, "");
			} catch (Exception e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			} finally {
				DatabaseConnection.close(con);
			}
		} else if (type.equalsIgnoreCase("몹")) {
			int count = 1;
			if (st.hasMoreTokens()) {
				count = Integer.parseInt(st.nextToken());
			}

			boolean random = false;
			int rnd = 0;
			if (st.hasMoreTokens()) {
				rnd = Integer.parseInt(st.nextToken());
			}
			if (rnd == 1) {
				random = true;
			}

			int randomloc = 0;
			if (st.hasMoreTokens()) {
				randomloc = Integer.parseInt(st.nextToken());
			}

			int monsterid = 0;
			try {
				monsterid = Integer.parseInt(name);
			} catch (NumberFormatException e) {
				monsterid = MonsterDatabase.findItemIdByNameWithoutSpace(name);
				if (monsterid == 0) {
					name = MonsterDatabase.findMonsterIdBy(name);
					if (name == null) {
						ChattingController.toChatting(o,
								"[서버알림] 해당 몬스터가 발견되지 않았습니다.", 20);
						return;
					}
				}
			}

			Monster m = null;
			if (monsterid == 0) {
				m = MonsterDatabase.find2(name);
			} else {
				m = MonsterDatabase.find_MonsterId(monsterid);
			}

			if (m == null) {
				ChattingController.toChatting(o, "해당 NPC가 발견되지 않습니다.", 20);
				return;
			}

			try {
				con = DatabaseConnection.getLineage();
				MonsterInstance a = MonsterSpawnlistDatabase.newInstance(m);
				if (random == true) {
					for (int i = 0; i < count; i++) {
						MonsterSpawnlistDatabase.toSpawnMonster(a, null,
								random, o.getX(), o.getY(), o.getMap(),
								randomloc, 60, true, true, true, true);
					}
				} else {
					MonsterSpawnlistDatabase.toSpawnMonster(a, null, false,
							o.getX(), o.getY(), o.getMap(), 0, 60, true, true,
							true, true);
				}
				MonsterSpawnlistDatabase.insert(con, m.getName(), m.getName(),
						random, count, randomloc, o.getX(), o.getY(),
						o.getMap(), 60, false, "", 0, "", 0, "", 0, "", 0);
			} catch (Exception e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			} finally {
				DatabaseConnection.close(con);
			}

		}
	}

	public static void spot(object o, StringTokenizer st) {
		try {
			String msg = st.nextToken();

			if (msg.equalsIgnoreCase("시작")) {
				if (!SpotController.spot.isStart)
					SpotController.spot.start(System.currentTimeMillis());
				else
					ChattingController.toChatting(o, "스팟 쟁탈전은 이미 진행중입니다.",
							Lineage.CHATTING_MODE_MESSAGE);
			} else if (msg.equalsIgnoreCase("종료")) {
				if (SpotController.spot.isStart)
					SpotController.spot.end(true);
				else
					ChattingController.toChatting(o, "스팟 쟁탈전은 진행중이 아닙니다.",
							Lineage.CHATTING_MODE_MESSAGE);
			}
		} catch (Exception e) {
			ChattingController.toChatting(o, Common.COMMAND_TOKEN + "스팟 시작/종료",
					Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	static public void 복구(object o, StringTokenizer st) {
		Connection con = null;
		String value = String.valueOf(st.nextToken());

		try {
			con = DatabaseConnection.getLineage();
			CharactersDatabase.updateLocation(con, value, 33432, 32807, 4);
		} catch (Exception e) {
		}
		ChattingController.toChatting(o, "[서버알림] 캐릭터의 좌표가 복구되었습니다.", 20);
	}

	@SuppressWarnings("resource")
	private static void toRepairLocation(object paramobject,
			StringTokenizer paramStringTokenizer) {
		String str = paramStringTokenizer.nextToken().toLowerCase();
		Connection localConnection = null;
		PreparedStatement localPreparedStatement = null;
		ResultSet localResultSet = null;
		try {
			localConnection = DatabaseConnection.getLineage();
			localPreparedStatement = localConnection
					.prepareStatement("SELECT account FROM characters WHERE LOWER(name) = ? GROUP BY account");
			localPreparedStatement.setString(1, str);
			localResultSet = localPreparedStatement.executeQuery();
			if (localResultSet.next()) {
				localPreparedStatement = localConnection
						.prepareStatement("UPDATE characters SET locX=?, locY=?, locMap=? WHERE account=?");
				localPreparedStatement.setInt(1, 33437);
				localPreparedStatement.setInt(2, 32813);
				localPreparedStatement.setInt(3, 4);
				localPreparedStatement.setString(4,
						localResultSet.getString("account"));
				localPreparedStatement.executeUpdate();
				ChattingController.toChatting(
						paramobject,
						new StringBuilder().append("[")
								.append(localResultSet.getString("account"))
								.append("] 계정의 모든 캐릭터 좌표 복구 완료!").toString(),
						20);
			} else {
				ChattingController.toChatting(paramobject, new StringBuilder()
						.append("[").append(str).append("] 캐릭터명이 없습니다.")
						.toString(), 20);
			}
		} catch (Exception localException) {
			lineage.share.System.printf(
					"%s : toRepairLocation(object o, StringTokenizer st)\r\n",
					new Object[] { CommandController.class.toString() });
			lineage.share.System.println(localException);
		} finally {

			DatabaseConnection.close(localConnection, localPreparedStatement,
					localResultSet);
		}
	}

	/*
	 * //공성시작 static private void warstart(object o, StringTokenizer st){ String
	 * name = String.valueOf(st.nextToken()); switch(name){ case "켄트":
	 * toKingdomControll(1, true);; break; case "기란": toKingdomControll(4,
	 * true);; break; case "윈다우드": toKingdomControll(3, true);; break; case
	 * "오크": toKingdomControll(2, true); break; case "하이네": toKingdomControll(5,
	 * true);; break; case "지저": toKingdomControll(6, true);; break; case "아덴":
	 * toKingdomControll(7, true);; break; default: break; } }
	 */

	public static void toCharacterInfo(object paramobject,
			StringTokenizer paramStringTokenizer) {
		String str1 = paramStringTokenizer.nextToken();
		PcInstance localPcInstance = World.findPc(str1);
		if (localPcInstance != null) {
			if (paramobject != null) {
				String str2 = "";
				switch (localPcInstance.getClassType()) {
				case 0:
					str2 = "군주";
					break;
				case 1:
					str2 = "기사";
					break;
				case 2:
					str2 = "요정";
					break;
				case 3:
					str2 = "법사";
					break;
				case 4:
					str2 = "다엘";
				}
				double d1 = ExpDatabase.find(localPcInstance.getLevel() + 1)
						.getExp();
				double d2 = localPcInstance.getExp()
						- ExpDatabase.find(localPcInstance.getLevel() - 1)
								.getBonus();
				double d3 = d2 / d1 * 100.0D;
				DecimalFormat localDecimalFormat = new DecimalFormat("0.00");
				ChattingController.toChatting(paramobject, new StringBuilder()
						.append("\\fR").append(Common.HELPER_LINE).toString(),
						20);
				ChattingController.toChatting(
						paramobject,
						new StringBuilder()
								.append("\\fR계정명: \\fY")
								.append(localPcInstance.getClient()
										.getAccountId())
								.append(" \\fR아이피: \\fY")
								.append(localPcInstance.getClient()
										.getAccountIp())
								.append(" \\fR포인트: \\fY").append("0")
								.toString(), 20);
				ChattingController.toChatting(
						paramobject,
						new StringBuilder()
								.append("\\fR캐릭명: \\fY")
								.append(localPcInstance.getName())
								.append(" \\fR레벨: \\fY")
								.append(localPcInstance.getLevel())
								.append("(")
								.append(localDecimalFormat.format(d3))
								.append("%)")
								.append(" \\fR클래스: \\fY")
								.append(str2)
								.append(" \\fR방어력: \\fY")
								.append(localPcInstance.getAc())
								.append(" \\fR마법방어: \\fY")
								.append(SkillController.getMr(localPcInstance,
										false)).toString(), 20);
				ChattingController.toChatting(
						paramobject,
						new StringBuilder().append("\\fRMAXHP: \\fY")
								.append(localPcInstance.getMaxHp())
								.append(" \\fRMAXMP: \\fY")
								.append(localPcInstance.getMaxMp()).toString(),
						20);
				ChattingController.toChatting(
						paramobject,
						new StringBuilder().append("\\fRSTR: \\fY")
								.append(localPcInstance.getStr())
								.append(" \\fRDEX: \\fY")
								.append(localPcInstance.getDex())
								.append(" \\fRCON: \\fY")
								.append(localPcInstance.getCon())
								.append(" \\fRWIS: \\fY")
								.append(localPcInstance.getWis())
								.append(" \\fRCHA: \\fY")
								.append(localPcInstance.getCha())
								.append(" \\fRINT: \\fY")
								.append(localPcInstance.getInt()).toString(),
						20);

				ChattingController.toChatting(paramobject, new StringBuilder()
						.append("\\fR").append(Common.HELPER_LINE).toString(),
						20);
				/*
				 * String str3 = ""; Connection localConnection = null;
				 * PreparedStatement localPreparedStatement = null; ResultSet
				 * localResultSet = null; try { localConnection =
				 * DatabaseConnection.getLineage(); localPreparedStatement =
				 * localConnection
				 * .prepareStatement("SELECT * FROM fix_user WHERE account=?");
				 * localPreparedStatement.setString(1,
				 * ((PcInstance)paramobject).getClient().getAccountId());
				 * localResultSet = localPreparedStatement.executeQuery(); if
				 * (localResultSet.next()) str3 =
				 * localResultSet.getString("phone"); } catch (Exception
				 * localException) {
				 * lineage.share.System.println(localException); } finally {
				 * DatabaseConnection.close(localConnection,
				 * localPreparedStatement, localResultSet); }
				 */
				paramobject.setBoardView(false);
				String[] arrayOfString = new String[4];
				arrayOfString[0] = new StringBuilder().append("02TEST")
						.append("서버").toString();
				arrayOfString[1] = new StringBuilder().append("* ")
						.append(localPcInstance.getName()).append(" 정보 *")
						.toString();
				StringBuilder localStringBuilder = new StringBuilder();
				localStringBuilder.append(new StringBuilder()
						.append("리 덕 션 : ")
						.append(localPcInstance.getTotalReduction())
						.append("\n").toString());
				localStringBuilder.append(new StringBuilder().append("계 정 명: ")
						.append(localPcInstance.getClient().getAccountId())
						.append("\n").toString());
				localStringBuilder.append(new StringBuilder().append("아 이 피: ")
						.append(localPcInstance.getClient().getAccountIp())
						.append("\n").toString());
				localStringBuilder.append(new StringBuilder().append("클 래 스: ")
						.append(str2).append("\n").toString());
				localStringBuilder.append(new StringBuilder()
						.append("레    벨: ").append(localPcInstance.getLevel())
						.append("(").append(localDecimalFormat.format(d3))
						.append("%)\n").toString());
				localStringBuilder.append(new StringBuilder().append("방 어 력: ")
						.append(10 - localPcInstance.getTotalAc()).append("\n")
						.toString());
				localStringBuilder.append(new StringBuilder().append("마법방어: ")
						.append(SkillController.getMr(localPcInstance, false))
						.append("\n").toString());
				localStringBuilder.append(new StringBuilder()
						.append("만    피: ").append(localPcInstance.getMaxHp())
						.append("  ").toString());
				localStringBuilder.append(new StringBuilder()
						.append("만    엠: ").append(localPcInstance.getMaxMp())
						.append("\n").toString());
				localStringBuilder.append(new StringBuilder()
						.append("힘      : ")
						.append(localPcInstance.getTotalStr()).append("   ")
						.toString());
				localStringBuilder.append(new StringBuilder()
						.append("덱    스: ")
						.append(localPcInstance.getTotalDex()).append("\n")
						.toString());
				localStringBuilder.append(new StringBuilder()
						.append("콘      : ")
						.append(localPcInstance.getTotalCon()).append("   ")
						.toString());
				localStringBuilder.append(new StringBuilder()
						.append("위    즈: ")
						.append(localPcInstance.getTotalWis()).append("\n")
						.toString());
				localStringBuilder.append(new StringBuilder()
						.append("카    리: ")
						.append(localPcInstance.getTotalCha()).append("    ")
						.toString());
				localStringBuilder.append(new StringBuilder()
						.append("인    트: ")
						.append(localPcInstance.getTotalInt()).append("\n")
						.toString());
				localStringBuilder.append(new StringBuilder()
						.append("피    틱: ")
						.append(localPcInstance.getDynamicTicHp())
						.append("    ").toString());
				localStringBuilder.append(new StringBuilder()
						.append("엠    틱: ")
						.append(localPcInstance.getDynamicTicMp()).append("\n")
						.toString());

				localStringBuilder.append(new StringBuilder()
						.append("E    R: ")
						.append(localPcInstance.getDynamicEr())
						.append("      ").toString());
				localStringBuilder.append(new StringBuilder()
						.append("닷    지: ")
						.append(localPcInstance.getDynamicDg()).append("\n")
						.toString());
				arrayOfString[2] = localStringBuilder.toString();
				paramobject.toSender(S_LetterNotice.clone(
						BasePacketPooling.getPool(S_LetterNotice.class),
						arrayOfString));
			}
		} else if (paramobject != null)
			ChattingController
					.toChatting(paramobject, new StringBuilder().append(str1)
							.append("은(는) 월드상 접속중이지 않습니다.").toString(), 20);
	}

	/*
	 * //공성종료 static private void warend(object o, StringTokenizer st){ String
	 * name = String.valueOf(st.nextToken()); switch(name){ case "켄트":
	 * toKingdomControll(1, false);; break; case "기란": toKingdomControll(4,
	 * false);; break; case "윈다우드": toKingdomControll(3, false);; break; case
	 * "오크": toKingdomControll(2, false); break; case "하이네":
	 * toKingdomControll(5, false);; break; case "지저": toKingdomControll(6,
	 * false);; break; case "아덴": toKingdomControll(7, false);; break; default:
	 * break; } }
	 */

	/*
	 * static public Shell shell; static public final String SERVER_VERSION =
	 * "v6.14";
	 * 
	 * static public void toMessageBox(final String msg) {
	 * toMessageBox(SERVER_VERSION, msg); }
	 * 
	 * static public void toMessageBox(final String title, final String msg) {
	 * MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING);
	 * messageBox.setText(String.format("경고 :: %s", title));
	 * messageBox.setMessage(msg); messageBox.open(); }
	 */

	/**
	 * 차단. : ScreenRenderComposite 에서 사용중
	 * 
	 * @param o
	 * @param st
	 */
	static public void toBan(object o, StringTokenizer st) {
		boolean find = false; // 찾앗는지 여부
		boolean account = false; // 계정명인지 여부
		String value = st.nextToken().toLowerCase(); // 검색할 명

		// 캐릭 찾기.
		Connection con = null;
		PreparedStatement stt = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			stt = con
					.prepareStatement("SELECT * FROM characters WHERE LOWER(name)=?");
			stt.setString(1, value);
			rs = stt.executeQuery();
			find = rs.next();
		} catch (Exception e) {
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, stt, rs);
		}
		// 계정 찾기.
		if (find == false) {
			try {
				con = DatabaseConnection.getLineage();
				stt = con
						.prepareStatement("SELECT * FROM accounts WHERE LOWER(id)=?");
				stt.setString(1, value);
				rs = stt.executeQuery();
				find = rs.next();
				account = find;
			} catch (Exception e) {
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, stt, rs);
			}
		}

		// 찾았다면
		if (find) {
			try {
				con = DatabaseConnection.getLineage();

				if (account) {
					// 계정 차단.
					stt = con
							.prepareStatement("UPDATE accounts SET block_date=? WHERE LOWER(id)=?");
					stt.setTimestamp(1,
							new java.sql.Timestamp(System.currentTimeMillis()));
					stt.setString(2, value);
					stt.executeUpdate();

					ChattingController.toChatting(o, "계정 차단.", 20);
				} else {
					// 캐릭 차단.
					stt = con
							.prepareStatement("UPDATE characters SET block_date=? WHERE LOWER(name)=?");
					stt.setTimestamp(1,
							new java.sql.Timestamp(System.currentTimeMillis()));
					stt.setString(2, value);
					stt.executeUpdate();

					ChattingController.toChatting(o, "캐릭 차단.", 20);
				}

				// 접속된 사용자 차단.
				PcInstance find_use = null;
				for (PcInstance pc : World.getPcList()) {
					if (account) {
						if (pc.getClient() != null
								&& pc.getClient().getAccountId()
										.equalsIgnoreCase(value)) {
							find_use = pc;
							break;
						}
					} else {
						if (pc.getName().equalsIgnoreCase(value)) {
							find_use = pc;
							break;
						}
					}
				}
				// 찾은 사용자 종료.
				if (find_use != null)
					find_use.toSender(S_Disconnect.clone(
							BasePacketPooling.getPool(S_Disconnect.class), 0x0A));
			} catch (Exception e) {
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, stt);
			}

			ChattingController.toChatting(o, "차단 완료.", 20);
		}
	}

	static public void toBanClear(object o, StringTokenizer tok) {
		String value = tok.nextToken().toLowerCase(); // 검색할 명
		int account_uid = 0;
		String ip = null;
		String ip_old = null;

		//
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			//
			st = con.prepareStatement("SELECT * FROM characters WHERE LOWER(name)=?");
			st.setString(1, value);
			rs = st.executeQuery();
			if (rs.next())
				account_uid = rs.getInt("account_uid");
			DatabaseConnection.close(st, rs);
			//
			st = con.prepareStatement("SELECT * FROM accounts WHERE uid=?");
			st.setInt(1, account_uid);
			rs = st.executeQuery();
			if (rs.next())
				ip = rs.getString("last_ip");
			DatabaseConnection.close(st, rs);
			//
			if (account_uid == 0) {
				ChattingController.toChatting(o,
						String.format("[알림] 캐릭명('%s')이 존재하지 않습니다.", value), 20);
				return;
			}
			if (ip == null) {
				ChattingController.toChatting(o, String.format(
						"[알림] 캐릭명('%s')에 대한 아이피를 확인할 수 없습니다.", value), 20);
				return;
			}
			//
			ip_old = ip;
			StringTokenizer stok = new StringTokenizer(ip, ".");
			ip = String.format("%s.%s.%s.+", stok.nextToken(),
					stok.nextToken(), stok.nextToken());
			BadIpDatabase.remove(ip);
			//
			st = con.prepareStatement("UPDATE accounts SET block_date=? WHERE last_ip=?");
			st.setString(1, "0000-00-00 00:00:00");
			st.setString(2, ip_old);
			st.executeUpdate();
			st.close();
			//
			ChattingController.toChatting(o, String.format(
					"캐릭터('%s')에 대한 아이피('%s')를 해제 하였습니다.", value, ip), 20);
			st = con.prepareStatement("SELECT * FROM accounts WHERE last_ip=?");
			st.setString(1, ip_old);
			rs = st.executeQuery();
			while (rs.next())
				ChattingController
						.toChatting(o, " : " + rs.getString("id"), 20);
		} catch (Exception e) {
			ChattingController.toChatting(o, "[차단해제 캐릭명 : " + e.toString(), 20);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	static public void toBan2(object o, StringTokenizer tok) {
		String value = tok.nextToken().toLowerCase(); // 검색할 명
		int account_uid = 0;
		String ip = null;
		String ip_old = null;

		//
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			//
			st = con.prepareStatement("SELECT * FROM characters WHERE name=?");
			st.setString(1, value.toLowerCase());
			rs = st.executeQuery();
			if (rs.next())
				account_uid = rs.getInt("account_uid");
			DatabaseConnection.close(st, rs);
			//
			st = con.prepareStatement("SELECT * FROM accounts WHERE uid=?");
			st.setInt(1, account_uid);
			rs = st.executeQuery();
			if (rs.next())
				ip = rs.getString("last_ip");
			DatabaseConnection.close(st, rs);
			//
			if (account_uid == 0) {
				ChattingController.toChatting(o, String.format("[알림] 케릭명('%s')이 존재하지 않습니다.", value),
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if (ip == null) {
				ChattingController.toChatting(o, String.format("[알림] 케릭명('%s')에 대한 아이피를 확인할 수 없습니다.", value),
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			//
			ip_old = ip;
			StringTokenizer stok = new StringTokenizer(ip, ".");
			// 기존 광역벤
			// ip = String.format("%s.%s.%s.+", stok.nextToken(), stok.nextToken(),
			// stok.nextToken());
			ip = String.format("%s.%s.%s.%s", stok.nextToken(), stok.nextToken(), stok.nextToken(), stok.nextToken());
			BadIpDatabase.append(ip);
			//
			st = con.prepareStatement("UPDATE accounts SET block_date=? WHERE last_ip=?");
			st.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
			st.setString(2, ip_old);
			st.executeUpdate();
			st.close();
			//
			for (PcInstance pc : World.getPcList())
				if (pc.getName().equalsIgnoreCase(value)/* || pc.getClient().getAccountIp().equalsIgnoreCase(ip_old) */)
					pc.toSender(new S_Disconnect(0x0A));
			//
			ChattingController.toChatting(o, String.format("케릭터('%s')에 대한 아이피('%s')를 차단 하였습니다.", value, ip),
					Lineage.CHATTING_MODE_MESSAGE);
			st = con.prepareStatement("SELECT * FROM accounts WHERE last_ip=?");
			st.setString(1, ip_old);
			rs = st.executeQuery();
			while (rs.next())
				ChattingController.toChatting(o, " : " + rs.getString("id"), Lineage.CHATTING_MODE_MESSAGE);
			DatabaseConnection.close(st, rs);
		} catch (Exception e) {
			ChattingController.toChatting(o, "[차단] 케릭명 : " + e.toString(), Lineage.CHATTING_MODE_MESSAGE);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	static public void toBan2Clear(object o, StringTokenizer tok) {
		String value = tok.nextToken().toLowerCase(); // 검색할 명
		int account_uid = 0;
		String ip = null;
		String ip_old = null;

		//
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			//
			st = con.prepareStatement("SELECT * FROM characters WHERE name=?");
			st.setString(1, value.toLowerCase());
			rs = st.executeQuery();
			if (rs.next())
				account_uid = rs.getInt("account_uid");
			DatabaseConnection.close(st, rs);
			//
			st = con.prepareStatement("SELECT * FROM accounts WHERE uid=?");
			st.setInt(1, account_uid);
			rs = st.executeQuery();
			if (rs.next())
				ip = rs.getString("last_ip");
			DatabaseConnection.close(st, rs);
			//
			if (account_uid == 0) {
				ChattingController.toChatting(o, String.format("[알림] 케릭명('%s')이 존재하지 않습니다.", value),
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if (ip == null) {
				ChattingController.toChatting(o, String.format("[알림] 케릭명('%s')에 대한 아이피를 확인할 수 없습니다.", value),
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			//
			ip_old = ip;
			StringTokenizer stok = new StringTokenizer(ip, ".");
			// ip = String.format("%s.%s.%s.+", stok.nextToken(), stok.nextToken(),
			// stok.nextToken());
			ip = String.format("%s.%s.%s.%s", stok.nextToken(), stok.nextToken(), stok.nextToken(), stok.nextToken());
			BadIpDatabase.remove(ip);
			//
			st = con.prepareStatement("UPDATE accounts SET block_date=? WHERE last_ip=?");
			st.setString(1, "1970-03-20 10:20:30");
			st.setString(2, ip_old);
			st.executeUpdate();
			st.close();
			//
			ChattingController.toChatting(o, String.format("케릭터('%s')에 대한 아이피('%s')를 해제 하였습니다.", value, ip),
					Lineage.CHATTING_MODE_MESSAGE);
			st = con.prepareStatement("SELECT * FROM accounts WHERE last_ip=?");
			st.setString(1, ip_old);
			rs = st.executeQuery();
			while (rs.next())
				ChattingController.toChatting(o, " : " + rs.getString("id"), Lineage.CHATTING_MODE_MESSAGE);
		} catch (Exception e) {
			ChattingController.toChatting(o, "[차단해제] 케릭명 : " + e.toString(), Lineage.CHATTING_MODE_MESSAGE);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	/**
	 * 청소 : GuiMain 에서 사용중
	 * 
	 * @param o
	 */
	static public void toWorldItemClear(object o) {
		World.clearWorldItem();
		if (o != null)
			ChattingController.toChatting(o, "청소 완료.", 20);
	}

	/**
	 * 올버프 ; GuiMain 에서 사용중
	 * 
	 * @param o
	 */
	static public void toBuffAll(object o) {
		for (PcInstance pc : World.getPcList()) {
			EnchantDexterity.onBuff(pc, SkillDatabase.find(4, 1));
			EnchantMighty.onBuff(pc, SkillDatabase.find(6, 1));
			Haste.onBuff(pc, SkillDatabase.find(6, 2));
			IronSkin.onBuff(pc, SkillDatabase.find(21, 7));
			ImmuneToHarm.onBuff(pc, SkillDatabase.find(9, 3));
			BlessWeapon.onBuff(pc, SkillDatabase.find(6, 7));
			// AdvanceSpirit.onBuff(pc, SkillDatabase.find(10, 7));
			// GlowingAura.onBuff(pc, SkillDatabase.find(15, 1));
			// ShiningAura.onBuff(pc, SkillDatabase.find(15, 2));
			// BraveAura.onBuff(pc, SkillDatabase.find(15, 4));
			// ReductionArmor.onBuff(pc, SkillDatabase.find(11, 7));
			// BounceAttack.onBuff(pc, SkillDatabase.find(12, 0));
			// DoubleBreak.onBuff(pc, SkillDatabase.find(14,0));
			// UncannyDodge.onBuff(pc, SkillDatabase.find(14,1));
			// BlessWeapon.onBuff(pc, SkillDatabase.find(6, 7));

			//
			// ItemInstance weapon = pc.getInventory().getSlot(8);
			// if (weapon != null) {
			// if (weapon.getItem().getType2().equalsIgnoreCase("bow")) {
			// StormShot.onBuff(pc, SkillDatabase.find(21, 5));
			// } else {
			// BurningWeapon.onBuff(pc, SkillDatabase.find(21, 2)); //
		}

		// for(PcInstance pc : World.getRobotList())
		// toBuff(pc);

		if (o != null)
			ChattingController.toChatting(o, "올버프 완료.", 20);
	}

	/**
	 * 버프 : ScreenRenderComposite 에서 사용중
	 * 
	 * @param o
	 * @param st
	 */
	static public void toBuff(object o, StringTokenizer st) {
		for (PcInstance pc : World.getPcList()) {
			EnchantDexterity.onBuff(pc, SkillDatabase.find(4, 1));
			EnchantMighty.onBuff(pc, SkillDatabase.find(6, 1));
			Haste.onBuff(pc, SkillDatabase.find(6, 2));
			IronSkin.onBuff(pc, SkillDatabase.find(21, 7));
			ImmuneToHarm.onBuff(pc, SkillDatabase.find(9, 3));
			BlessWeapon.onBuff(pc, SkillDatabase.find(6, 7));
			// AdvanceSpirit.onBuff(pc, SkillDatabase.find(10, 7));
			// GlowingAura.onBuff(pc, SkillDatabase.find(15, 1));
			// ShiningAura.onBuff(pc, SkillDatabase.find(15, 2));
			// BraveAura.onBuff(pc, SkillDatabase.find(15, 4));
			// ReductionArmor.onBuff(pc, SkillDatabase.find(11, 7));
			// BounceAttack.onBuff(pc, SkillDatabase.find(12, 0));
			// DoubleBreak.onBuff(pc, SkillDatabase.find(14,0));
			// UncannyDodge.onBuff(pc, SkillDatabase.find(14,1));
			// BlessWeapon.onBuff(pc, SkillDatabase.find(6, 7));

			//
			// ItemInstance weapon = pc.getInventory().getSlot(8);
			// if (weapon != null) {
			// if (weapon.getItem().getType2().equalsIgnoreCase("bow")) {
			// StormShot.onBuff(pc, SkillDatabase.find(21, 5));
			// } else {
			// BurningWeapon.onBuff(pc, SkillDatabase.find(21, 2)); //
		}
			 
			if (o != null)
				ChattingController.toChatting(o, "버프 완료.", 20);
		}

	/**
	 * 인벤삭제.
	 * 
	 * @param o
	 * @param st
	 **/
	static private void 인벤삭제(object o, StringTokenizer st) {
		PcInstance pc = World.findPc(st.nextToken());

		String itemname = null;

		if (st.hasMoreTokens()) {
			itemname = String.valueOf(st.nextToken());
		}

		int count = 1;
		if (st.hasMoreTokens()) {
			count = Integer.parseInt(st.nextToken());
		}

		int en = 0;
		if (st.hasMoreTokens()) {
			en = Integer.parseInt(st.nextToken());
		}

		int bress = 1;
		if (st.hasMoreTokens()) {
			bress = Integer.parseInt(st.nextToken());
		}

		ItemInstance targetItem = null;

		if (itemname != null) {
			Item i = ItemDatabase.findItemIdByNameWithoutSpace3(itemname);
			targetItem = ItemDatabase.newInstance(i);
		}

		if (pc == null) {
			ChattingController.toChatting(o, "해당캐릭터가 존재하지 않습니다.", 20);
		}

		try {
			for (ItemInstance item : pc.getInventory().getList()) {
				if (itemname == null) {
					if (!item.isEquipped()) {
						pc.getInventory().remove(item, true);
						ChattingController.toChatting(o, pc.getName() + "님의 +"
								+ item.getEnLevel() + " "
								+ item.getItem().getName() + "(" + count
								+ ") 삭제완료.", 20);
						ChattingController.toChatting(pc,
								"운영자님이 당신의 +" + item.getEnLevel() + " "
										+ item.getItem().getName() + "("
										+ count + ") 삭제하였습니다.", 20);
					}
				} else {
					if (item.getName() == targetItem.getName()
							&& item.getEnLevel() == en
							&& item.getBress() == bress) {
						pc.getInventory().remove(item, true);
						ChattingController.toChatting(o, pc.getName() + "님의 +"
								+ item.getEnLevel() + " "
								+ item.getItem().getName() + "(" + count
								+ ") 삭제완료.", 20);
						ChattingController.toChatting(pc,
								"운영자님이 당신의 +" + item.getEnLevel() + " "
										+ item.getItem().getName() + "("
										+ count + ") 삭제하였습니다.", 20);
					}
				}
			}
		} catch (Exception e) {
			ChattingController.toChatting(o, "[서버알림] " + Common.COMMAND_TOKEN
					+ "인벤삭제 [캐릭명] [아이템이름] [갯수] [인챈트] [축/저주]", 20);
			ChattingController.toChatting(o, "[서버알림] 아이템이름이 없을시 착용템 제외 모두 삭제.",
					20);
		}
	}

	static private void 업데이트(object o) {
		Connection con = null;
		try {
			con = DatabaseConnection.getLineage();
			PvpController.close(con);
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n",
					PvpController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con);
		}
	}

	/**
	 * 버프레벨.성환 중복 코드 방지용. : 여기서 사용중. : PcInstnace.toWorldJoin 사용중.
	 * 
	 * @param o
	 */
	static public void toBuff(object o) {
		if (!isCommandDelay(o, null, 10))
			return;
		if (o.getLevel() >= Lineage.buff_level) {
			ChattingController.toChatting(o, String.format(
					"레벨 %d이상은 버프를 사용할 수 없습니다.", Lineage.buff_level), 20);
			return;
		}
		EnchantDexterity.onBuff(o, SkillDatabase.find(4, 1));
		EnchantMighty.onBuff(o, SkillDatabase.find(6, 1));
	}

	static private void 킬수조작(object o, StringTokenizer st) {
		String name = st.nextToken();
		String count = st.nextToken();

		PcInstance pc = World.findPc(name);

		if (pc == null) {
			ChattingController.toChatting(o, "[접속중인 유저가 아닙니다.]", 20);
			return;
		}
		System.out.println("오브젝트아이디 : " + pc.getObjectId());

		PvpController.GmPvpKill(pc, count, pc.getObjectId());

	}

	/**
	 * 아이템 생성
	 * 
	 * @param o
	 * @param st
	 */
	/**
	 * 아이템 생성
	 * 
	 * @param o
	 * @param st
	 */
	static private void toItem(object o, StringTokenizer st) {
		String name = st.nextToken();

		int count = 1;
		if (st.hasMoreTokens()) {
			count = Integer.parseInt(st.nextToken());
		}
		int en = 0;
		if (st.hasMoreTokens()) {
			en = Integer.parseInt(st.nextToken());
		}
		int bress = 1;
		if (st.hasMoreTokens()) {
			bress = Integer.parseInt(st.nextToken());
		}

		int itemid = 0;
		try {
			itemid = Integer.parseInt(name);
		} catch (NumberFormatException e) {
			itemid = ItemDatabase.findItemId(name);
			if (itemid == 0) {
				name = ItemDatabase.findItemIdByNameWithoutSpace2(name);
				if (name == null) {
					ChattingController.toChatting(o,
							String.format("해당 아이템이 발견되지 않습니다."),
							Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
			}
		}

		Item i = null;
		if (itemid == 0) {
			i = ItemDatabase.find2(name);
		} else {
			i = ItemDatabase.find_ItemId(itemid);
		}
		if (i != null) {
			ItemInstance temp = ItemDatabase.newInstance(i);
			temp.setCount(count);
			temp.setEnLevel(en);
			temp.setBress(bress);
			temp.setDefinite(true);
			o.getInventory().append(temp, count);

			// 알림.
			if (temp.getItem().getType1().equalsIgnoreCase("weapon")
					|| temp.getItem().getType1().equalsIgnoreCase("armor"))
				ChattingController.toChatting(o, String.format(
						"아이템 : +%d %s(%d) 생성 완료", en, i.getName(), count),
						Lineage.CHATTING_MODE_MESSAGE);
			else
				ChattingController
						.toChatting(o, String.format("아이템 : %s(%d) 생성 완료",
								i.getName(), count),
								Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	/**
	 * 몬스터 생성.
	 * 
	 * @param o
	 * @param st
	 */
	static private void toMonster(object o, StringTokenizer st) {
		String name_id = st.nextToken();
		int count = 1;
		if (st.hasMoreTokens()) {
			count = Integer.parseInt(st.nextToken());
		}

		int nameid = 0;
		try {
			nameid = Integer.parseInt(name_id);
		} catch (NumberFormatException e) {
			nameid = MonsterDatabase.findItemIdByNameWithoutSpace(name_id);
			if (nameid == 0) {
				ChattingController.toChatting(o, "[서버알림] 해당 몬스터가 발견되지 않았습니다.",
						20);
				return;
			}
		}

		MonsterInstance mi = null;
		for (int i = 0; i < count; ++i) {

			if (nameid != 0) {
				mi = MonsterSpawnlistDatabase.newInstance(MonsterDatabase
						.find2(name_id));
			} else {
				mi = MonsterSpawnlistDatabase.newInstance(MonsterDatabase
						.find_MonsterId(nameid));
			}
			if (mi != null) {
				mi.setHomeX(o.getX());
				mi.setHomeY(o.getY());
				mi.setHomeMap(o.getMap());
				mi.setHeading(o.getHeading());
				mi.setPineWand(true);
				mi.toTeleport(o.getX(), o.getY(), o.getMap(), false);
				mi.readDrop();

				AiThread.append(mi);
			} else {
				return;
			}
		}
		ChattingController.toChatting(o, "[서버알림] 몬스터 생성 완료.", 20);
	}

	static private void clanpoint(object o, StringTokenizer st) {
		try {
			String pobyname = st.nextToken();
			int point = Integer.parseInt(st.nextToken());
			PcInstance target = World.findPc(pobyname);
			if (target != null) {
				if (target.getClanId() != 0) {
					Clan TargetClan = ClanController.find(target);
					// TargetClan.addClanPoint(point);
					ChattingController.toChatting(o, target.getClanName()
							+ " 혈맹에게 [경험치 " + point + "] 지급.", 20);
					for (PcInstance tc : TargetClan.getList()) {
						ChattingController.toChatting(tc, "게임마스터로부터 혈맹경험치 ["
								+ point + "] 를 지급 받았습니다.", 20);
					}
				} else {
					ChattingController.toChatting(o, target.getName()
							+ "님은 혈맹에 속해 있지 않습니다.", 20);

				}
			} else {
				ChattingController.toChatting(o, "존재하지 않는 캐릭터입니다.", 20);
			}
		} catch (Exception e) {
			ChattingController.toChatting(o, ".혈포지급 [지급할혈맹군주이름] [지급할 포인트]", 20);
		}
	}

	/**
	 * 소환.
	 * 
	 * @param o
	 * @param st
	 */
	static private void toCall(object o, StringTokenizer st) {
		PcInstance pc = World.findPc(st.nextToken());
		if (pc != null) {
			pc.toPotal(o.getX(), o.getY(), o.getMap());

			ChattingController.toChatting(o, "소환 완료.", 20);
		}
	}

	static private void toperpecten(object o, StringTokenizer st) {
		PcInstance pc = World.findPc(st.nextToken());
		if (pc != null) {

			pc.setperpecten(!pc.isperpecten());
			ChattingController.toChatting(o, String.format(
					"인첸트 퍼펙트 %s활성화 되었습니다.", pc.isperpecten() ? "" : "비"), 20);

		}
	}

	/**
	 * 출두.
	 * 
	 * @param o
	 * @param st
	 */
	static private void toGo(object o, StringTokenizer st) {
		String name = st.nextToken();
		PcInstance pc = World.findPc(name);
		if (pc == null)
			pc = World.findRobot(name);
		if (pc != null) {
			o.toTeleport(pc.getX(), pc.getY(), pc.getMap(), false);

			ChattingController.toChatting(o, "출두 완료.", 20);
		}
	}

	static private void toWarehouseClanLog(object o, StringTokenizer token) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();

			ChattingController.toChatting(o, "---------창고이력(최근10개)----------",
					20);
			String type = token.nextToken();
			if (type.equalsIgnoreCase("찾기")) {
				st = con.prepareStatement("SELECT * FROM warehouse_clan_log WHERE clan_uid=? AND type='remove' ORDER BY uid DESC LIMIT 10");
			}
			if (type.equalsIgnoreCase("맡기기")) {
				st = con.prepareStatement("SELECT * FROM warehouse_clan_log WHERE clan_uid=? AND type='append' ORDER BY uid DESC LIMIT 10");
			} else {
				ChattingController.toChatting(o, "- 옵션이 정확하지 않습니다.", 20);
				ChattingController
						.toChatting(o, "- '찾기' 혹은 '맡기기'를 넣어주십시오.", 20);
			}
			if (st != null) {
				st.setLong(1, o.getClanId());
				rs = st.executeQuery();
				while (rs.next())
					ChattingController.toChatting(o,
							String.format(
									Common.COMMAND_TOKEN
											+ " '%s' 님께서 '%s' 아이템을 %s셨습니다.",
									rs.getString("character_name"),
									String.format("%d %s(%d)",
											rs.getInt("item_en"),
											rs.getString("item_name"),
											rs.getInt("item_count")),
									rs.getString("type").equalsIgnoreCase(
											"append") ? "맡기" : "찾으"), 20);
			}
			ChattingController.toChatting(o,
					"----------------------------------", 20);
		} catch (Exception e) {
			ChattingController.toChatting(o, Common.COMMAND_TOKEN
					+ "창고이력 [찾기, 맡기기]", 20);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	/**
	 * 변경된 inventory 테이블을 검색하기 위해 사용되는 메서드.
	 * 
	 * @param o
	 * @param token
	 */
	static private void searchInventory(object o, StringTokenizer token,
			boolean all) {
		//
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//
			int token_count = token.countTokens();
			StringBuffer sb = new StringBuffer();
			Integer en = null;
			for (int i = 0; i < token_count - 1; ++i)
				sb.append(token.nextToken()).append(" ");
			String temp = token.nextToken();
			try {
				en = Integer.valueOf(temp);
			} catch (Exception e) {
				sb.append(temp);
			}
			//
			long search_result_count = 0;
			Map<String, String> search_result = new HashMap<String, String>();
			List<String> search_result2 = new ArrayList<String>();
			String value = sb.toString().trim();
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters_inventory WHERE weapon LIKE ? OR armor LIKE ? OR etc LIKE ?");
			st.setString(1, "%" + value + "%");
			st.setString(2, "%" + value + "%");
			st.setString(3, "%" + value + "%");
			rs = st.executeQuery();
			while (rs.next()) {
				sb = new StringBuffer();
				long count = 0;
				// 계정명, 캐릭명 추출.
				int cha_objId = rs.getInt("cha_objId");
				String cha_name = CharactersDatabase.getCharacterName(con,
						cha_objId);
				int accountUid = CharactersDatabase.getCharacterAccountUid(con,
						cha_objId);
				sb.append(cha_name).append(",").append(accountUid)
						.append("\r\n");
				// 찾기
				boolean find = false;
				String db = rs.getString("weapon") + rs.getString("armor")
						+ rs.getString("etc");
				StringTokenizer db_token = new StringTokenizer(db, "\r\n");
				while (db_token.hasMoreTokens()) {
					String db_line = db_token.nextToken();
					if (db_line.indexOf(value) > 0) {
						String[] dbs_line = db_line.split(",");
						if (en == null || Integer.valueOf(dbs_line[4]) == en) {
							find = true;
							sb.append(db_line).append("\r\n");
							// 찾은 아이템 갯수 누적하기
							count = Long.valueOf(dbs_line[2]);
							search_result_count += count;
						}
					}
				}
				// 정보 간략 정리 및 등록.
				if (find)
					search_result.put(String.valueOf(count) + "," + cha_name,
							sb.toString().trim());
			}
			// 정렬
			int limit = 50;
			MyComparator comp = new MyComparator();
			TreeMap<String, String> treeMap = new TreeMap<String, String>(comp);
			treeMap.putAll(search_result);
			search_result.clear();
			int size = treeMap.size();
			Set s = treeMap.entrySet();
			Iterator it = s.iterator();
			int idx = 0;
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String v = (String) entry.getValue();
				if (all || idx++ >= (size - limit))
					search_result2.add(v);
			}
			// 정리
			sb = new StringBuffer();
			sb.append("디비를 기준으로 검색된 결과입니다. 오차가 발생할 수 있음.").append("\r\n");
			if (!all)
				sb.append("검색 최대 " + limit + "개까지만 출력 됩니다.").append("\r\n");
			sb.append("전체갯수 : ")
					.append(String.format("%,d", search_result_count))
					.append("\r\n");
			sb.append("상세정보 :").append("\r\n");
			for (int i = search_result2.size() - 1; i >= 0; --i) {
				String db = search_result2.get(i);
				StringTokenizer db_token = new StringTokenizer(db, "\r\n");
				String info_temp = db_token.nextToken();
				String[] info = info_temp.split(",");
				while (db_token.hasMoreTokens()) {
					String db_line = db_token.nextToken();
					String[] dbs_line = db_line.split(",");
					//
					en = Integer.valueOf(dbs_line[4]);
					String item_name = dbs_line[1];
					long count = Long.valueOf(dbs_line[2]);
					//
					sb.append(
							String.format("[%s] [%s] %s%d %s(%,d)", info[1],
									info[0], (en >= 0 ? "+" : ""), en,
									item_name, count)).append("\r\n");
				}
			}
			search_result2.clear();
			// 결과 출력.
			if (all)
				ChattingController.toChatting(o, "전체조회 완료되었습니다.", 20);
			else
				LetterController.toMessageBox(o, "아이템조회 명령어 결과", sb.toString()
						.trim());
			// System.out.println( sb.toString().trim() );
		} catch (Exception e) {
			ChattingController.toChatting(o, ".아이템조회 [아이템명] [인첸]", 20);
			// System.out.println("-아이템조회  : " + e);
		} finally {
			DatabaseConnection.close(con);
		}
	}

	private static void 로봇좌표(object o, StringTokenizer st) {
		Connection con = null;
		PreparedStatement pstm = null;

		try {
			int tt = 1;

			String type = null;
			if (st.hasMoreTokens()) {
				tt = Integer.parseInt(st.nextToken());
			}

			if (tt == 1) {
				type = "safety";
			} else if (tt == 2) {
				type = "normal";
			} else if (tt == 3) {
				type = "wizard_zone";
			}
			try {
				con = DatabaseConnection.getLineage();
				pstm = con
						.prepareStatement("INSERT INTO _robot_book SET location=?, locX=?, locY=?, locMAP=?, type=?");
				pstm.setString(1, "명령입력");
				pstm.setInt(2, o.getX());
				pstm.setInt(3, o.getY());
				pstm.setInt(4, o.getMap());
				pstm.setString(5, type);
				pstm.executeUpdate();
				pstm.close();
			} catch (Exception e) {
			} finally {
				pstm.close();
				con.close();
			}
			ChattingController.toChatting(o, "[현재지점을 로봇좌표에 등록이 완료되었습니다.]", 20);
		} catch (Exception e) {

		}
	}

	private static void toReLoad(object o, StringTokenizer st) {
		String str = st.nextToken();
		Connection con = null;
		int i = 1;
		try {
			con = DatabaseConnection.getLineage();
			if (str.equals("서버설정")) {
				Lineage.init(true);

			} else if (str.equals("옥션")) {
				AuctionController.init(con);
			} else if (str.equals("아지트")) {
				AgitController.init(con);
			} else if (str.equals("프레임")) {
				SpriteFrameDatabase.init(con);
				// Gm.init();
			} else if (str.equals("공지")) {
				NoticeController.init();
			} else if (str.equals("엔피씨")) {
				NpcDatabase.init(con);
			} else if (str.equals("몬스터")) {
				MonsterDatabase.init(con);
			} else if (str.equals("아이템")) {

				ItemDatabase.init(con);
			} else if (str.equals("세트템")) {
				ItemSetoptionDatabase.init(con);
			}

			else if (str.equals("드랍")) {
				MonsterDropDatabase.DropInit();
				MonsterDropDatabase.init(con);
			} else if (str.equals("상점")) {
				NpcShopDatabase.ShopInit();
				NpcShopDatabase.init(con);
			} else if (str.equals("박스")) {
				ItemBundleDatabase.init(con);
			} else if (str.equals("보스")) {
				MonsterBossSpawnlistDatabase.init(con);
			} else if (str.equals("공성")) {
				KingdomController.init(con);
			} else if (str.equals("변신")) {
				PolyDatabase.init(con);
			} else if (str.equals("스킬")) {
				SkillDatabase.init(con);
			} else if (str.equals("서먼")) {
				SummonListDatabase.init(con);
			} else if (str.equals("공지사항")) {
				ServerNoticeDatabase.init(con);
			} else if (str.equals("밴아이피")) {
				BadIpDatabase.init(con);
			} else if (str.equals("아이템스킬")) {
				ItemSkillDatabase.init(con);
			} else if (str.equals("몬스터스킬")) {
				MonsterSkillDatabase.init(con);
			} else if (str.equals("텔레포트홈")) {
				TeleportHomeDatabase.init(con);
			} else if (str.equals("서버메세지")) {
				ServerMessageDatabase.init(con);
			} else if (str.equals("텔레포트리셋")) {
				TeleportResetDatabase.init(con);
			} else if (str.equals("엔피씨텔레포트")) {
				NpcTeleportDatabase.init(con);
			} else if (str.equals("아이템텔레포트")) {
				ItemTeleportDatabase.init(con);
			} else if (str.equals("레벨보상")) {
				QuestPresentDatabase.init(con);
			} else if (str.equals("퀴즈")) {
				QuizQuestionDatabase.init(con);
			} else if (str.equals("스핵")) {
				HackNoCheckDatabase.reload();
			} else {
				i = 0;
			}
			if (i != 0)
				ChattingController.toChatting(o, new StringBuilder()
						.append(str).append(" : 리로드 완료").toString(), 20);
			else
				ChattingController
						.toChatting(o, "존재하지 않는 항목을 리로드 시도하셨습니다.", 20);
		} catch (Exception localException) {
			lineage.share.System.printf(
					"%s : toReLoad(object o, StringTokenizer st)\r\n",
					new Object[] { CommandController.class.toString() });
			lineage.share.System.println(localException);
		} finally {
			DatabaseConnection.close(con);
		}
	}

	public static void toKingdomWarCheck(object paramobject,
			StringTokenizer paramStringTokenizer) {
		for (Kingdom localKingdom : KingdomController.getList()) {
			if (localKingdom.isWar())
				ChattingController.toChatting(paramobject, String.format(
						"%s : 공성이 진행중입니다.",
						new Object[] { localKingdom.getName() }), 20);
			else
				ChattingController.toChatting(paramobject, String.format(
						"%s : 공성시간이 아닙니다.",
						new Object[] { localKingdom.getName() }), 20);

		}
	}

	static public void setKingdomWar() {
		for (Kingdom kingdom : KingdomController.getList()) {
			if (!kingdom.isWar())
				kingdom.toStartWar(System.currentTimeMillis());
			else
				kingdom.toStopWar(System.currentTimeMillis());
		}
	}

	static private void toMonster2(object o, StringTokenizer st) {
		String str = "";
		int i = 0;
		int j = 1;
		try {
			str = st.nextToken();
			i = Integer.valueOf(str).intValue();
		} catch (Exception localException1) {
		}
		try {
			j = Integer.valueOf(st.nextToken()).intValue();
		} catch (Exception localException2) {
		}
		if (str.equals("")) {
			ChattingController
					.toChatting(o, ("몬뿌 [한글 또는 숫자] count").toString());
			return;
		}
		for (int k = 0; k < j; k++) {
			MonsterInstance localMonsterInstance = null;
			if (!str.equals(""))
				localMonsterInstance = MonsterSpawnlistDatabase
						.newInstance(MonsterDatabase.find2(str));
			else
				localMonsterInstance = MonsterSpawnlistDatabase
						.newInstance(MonsterDatabase.find(i));
			if (localMonsterInstance == null)
				continue;
			localMonsterInstance.setHomeX(o.getX());
			localMonsterInstance.setHomeY(o.getY());
			localMonsterInstance.setHomeMap(o.getMap());
			localMonsterInstance.setHeading(o.getHeading());
			localMonsterInstance.toTeleport(o.getX() + Util.random(-10, 10),
					o.getY() + Util.random(-10, 10), o.getMap(), false);
			localMonsterInstance.readDrop();
			AiThread.append(localMonsterInstance);
			ChattingController.toChatting(o, "몬스터 뿌리기 완료.", 20);
		}
	}

	public static void toDogGameControl(object paramobject,
			StringTokenizer paramStringTokenizer) {
		String str = paramStringTokenizer.nextToken();
		if (str.equals("시작")) {
			DogRaceController.isDogGame = true;
			DogRaceController.start();
		} else if (str.equals("종료")) {
			DogRaceController.isDogGame = false;
			DogRaceController.close();
		}
		if (paramobject != null)
			ChattingController
					.toChatting(paramobject, new StringBuilder().append("개경이 ")
							.append(str).append("되었습니다.").toString(), 20);
	}

	private static void toCalcUser(object paramobject,
			StringTokenizer paramStringTokenizer) {
		String str1 = null;
		String str2 = paramStringTokenizer.nextToken();
		int i = Integer.parseInt(paramStringTokenizer.nextToken());
		if (str2.equalsIgnoreCase("+")) {
			Lineage.world_player_plus_count += i;
			str1 = new StringBuilder().append(
					new StringBuilder().append("뻥튀기 : ").append(i)
							.append("명 추가 / 현재 뻥튀기 : ")
							.append(Lineage.world_player_plus_count)
							.append("명").toString()).toString();
		} else if (str2.equalsIgnoreCase("-")) {
			int j = Lineage.world_player_plus_count - i;
			if (j < 0) {
				ChattingController.toChatting(
						paramobject,
						new StringBuilder()
								.append("뻥튀기가 -가 될수는 없습니다. 현재 뻥튀기 : ")
								.append(Lineage.world_player_plus_count)
								.append("명").toString(), 20);
				return;
			}
			Lineage.world_player_plus_count = j;
			str1 = new StringBuilder().append(
					new StringBuilder().append("뻥튀기 : ").append(i)
							.append("명 감소 / 현재 뻥튀기 : ")
							.append(Lineage.world_player_plus_count)
							.append("명").toString()).toString();
		}
		ChattingController.toChatting(paramobject, str1, 20);
	}

	// 아덴거래
	static public void toBankAccountSetting(object o, StringTokenizer st) {
		PcInstance pc = (PcInstance) o;

		if (accountSaleCount(pc) > 0) {
			ChattingController.toChatting(o, "계정에 판매 중인 게시글이 있을 경우 수정 불가합니다.",
					Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		try {
			String bankName = st.nextToken();
			String bankNumber = st.nextToken();
			String bankUserName = st.nextToken();
			String bankUserNumber = st.nextToken();

			pc.getClient().setBankAccount(
					bankName + " " + bankNumber + " " + bankUserName + " "
							+ bankUserNumber);

			pc.getClient().setBankName(bankName);
			pc.getClient().setBankNumber(bankNumber);
			pc.getClient().setBankUserName(bankUserName);
			pc.getClient().setBankUserNumber(bankUserNumber);

			AccountDatabase.updateBankAccount(pc);

			ChattingController.toChatting(o, "계좌등록을 하였습니다. => "
					+ pc.getClient().getBankAccount(),
					Lineage.CHATTING_MODE_MESSAGE);
		} catch (Exception e) {
			ChattingController.toChatting(o, ".계좌등록 은행명 계좌번호 예금주명 핸드폰번호",
					Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	public static void setWeather(object o, StringTokenizer st) {
		try {
			int weather = Integer.valueOf(st.nextToken());
			LineageServer.weather = weather;
			World.toSender(S_Weather.clone(
					BasePacketPooling.getPool(S_Weather.class),
					LineageServer.weather));
		} catch (Exception e) {
			ChattingController.toChatting(o, Common.COMMAND_TOKEN
					+ "날씨 0:맑음 / 1:눈조금 / 2:눈많이 / 3:눈펑펑",
					Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(o, Common.COMMAND_TOKEN
					+ "날씨 17:비조금 / 18:비많이 / 19:폭우",
					Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	static public void toBankAccountPrint(object o, StringTokenizer st) {
		PcInstance pc = (PcInstance) o;
		try {
			if (pc.getClient().getBankAccount() == null
					|| pc.getClient().getBankAccount().equals("")) {
				ChattingController.toChatting(o, "계좌등록을 하고 확인해 주세요.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			ChattingController.toChatting(o, "등록된 계좌 => "
					+ pc.getClient().getBankAccount(),
					Lineage.CHATTING_MODE_MESSAGE);
		} catch (Exception e) {

		}
	}

	/**
	 * 계정의 판매신청 게시글 갯수 확인 메소드. 2018-07-21 by connector12@nate.com
	 */
	static public int accountSaleCount(PcInstance pc) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			con = DatabaseConnection.getLineage();

			st = con.prepareStatement("SELECT COUNT(*) as cnt FROM boards WHERE sell_account_uid=? AND step=?");
			st.setLong(1, pc.getClient().getAccountUid());
			st.setInt(2, 0);
			rs = st.executeQuery();

			if (rs.next())
				return rs.getInt("cnt");
		} catch (Exception e) {
			lineage.share.System.printf(
					"%s : accountSaleCount(PcInstance pc)\r\n",
					BoardController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return 0;
	}

	static public void toAdenaSellRegedit(object o, StringTokenizer st) {

		PcInstance pc = (PcInstance) o;
		try {
			if (pc.getClient().getBankAccount() == null
					|| pc.getClient().getBankAccount().equals("")) {
				ChattingController.toChatting(o, "계좌등록을 하고 진행해 주세요.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			int count = accountSaleCount(pc);
			if (Lineage.pc_trade_sale_max_count > 0
					&& Lineage.pc_trade_sale_max_count <= count) {
				ChattingController.toChatting(o, String.format(
						"계정의 최대 판매 등록은 %d개입니다. 현재 판매등록: %d개",
						Lineage.pc_trade_sale_max_count, count),
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			if (!pc.isAppend()) {
				long time = (pc.getCancelTime() - System.currentTimeMillis()) / 1000;
				int a1 = (int) (time / 60);
				int a2 = a1 > 60 ? a1 / 60 : 0;
				if (a2 > 0)
					a1 = a1 % 60;
				String data = String.valueOf(a2 > 0 ? (a2 + "시간 " + a1) : a1);
				ChattingController.toChatting(o,
						String.format("%s분 후 등록이 가능합니다.", data),
						Lineage.CHATTING_MODE_MESSAGE);
				return;

			}

			if (pc.getLevel() < Lineage.adena_trade_regedit_min_level) {
				ChattingController.toChatting(o, "판매등록은 최소 "
						+ Lineage.adena_trade_regedit_min_level + "부터 가능합니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			int adenaCount = Integer.parseInt(st.nextToken());
			int moneyCount = Integer.parseInt(st.nextToken());

			int regeditAdena = adenaCount / 20;

			if (adenaCount < Lineage.adena_trade_regedit_min_value) {
				ChattingController
						.toChatting(
								o,
								"최소 등록 아데나는 "
										+ Util.numberFormat(Lineage.adena_trade_regedit_min_value)
										+ "부터 입니다.",
								Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			if (!pc.getInventory().isAden((int) (adenaCount + regeditAdena),
					false)) {
				ChattingController.toChatting(o, "아데나가 부족합니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			// if (AccountDatabase.getTradeing(pc)) {
			// ChattingController.toChatting(o, "이미 등록한 물품이 존재합니다.");
			// return;
			// }

			pc.getInventory().isAden((int) (adenaCount + regeditAdena), true);

			// String subject = String.format("[%.1f만원] %f천 팝니다.", moneyCount /
			// 10000, adenaCount / 1000000);
			String subject = String.format("%,d만 아덴 팝니다.", adenaCount / 10000);

			StringBuilder content = new StringBuilder();

			Connection con = null;
			PreparedStatement stt = null;
			ResultSet rs = null;
			try {
				con = DatabaseConnection.getLineage();

				// uid 추출
				int uid = BoardController.getMaxUid("giran");

				content.append("거래상황: 판매등록완료\r\n");
				content.append("거래번호: " + uid + "\r\n");
				content.append("\r\n");
				content.append("아데나 : " + Util.numberFormat(adenaCount)
						+ "\r\n");
				content.append("판매금액: " + Util.numberFormat(moneyCount)
						+ "\r\n");
				content.append("\r\n");
				content.append("은 행 명: " + pc.getClient().getBankName()
						+ "\r\n");
				content.append("계좌번호: " + pc.getClient().getBankNumber()
						+ "\r\n");
				content.append("예 금 주: " + pc.getClient().getBankUserName()
						+ "\r\n");
				content.append("연 락 처: " + pc.getClient().getBankUserNumber()
						+ "\r\n");

				// 등록
				stt = con
						.prepareStatement("INSERT INTO boards SET uid=?, type=?,sell_account_uid=? , account_id=?, name=?, days=?, subject=?, memo=?, adena=?, money=?, step=?, moddate=now()");
				stt.setInt(1, uid);
				stt.setString(2, "giran");
				stt.setInt(3, pc.getClient().getAccountUid());
				stt.setString(4, pc.getClient().getAccountId());
				stt.setString(5, pc.getName());
				stt.setTimestamp(6,
						new java.sql.Timestamp(System.currentTimeMillis()));
				stt.setString(7, subject);
				stt.setString(8, content.toString());
				stt.setInt(9, (int) adenaCount);
				stt.setInt(10, (int) moneyCount);
				stt.setInt(11, 0);
				stt.executeUpdate();
				stt.close();

				adenaBoard = BackgroundDatabase.getTradeBoard();// 이렇게 다시 뺏어요
																// 그래서 최종저 그니까
																// 결국 변수가 여기로
																// 옴겨진거네요넴! 이거랑
				// 먼저 여기부분에서 걸리나?? 이걸 보려면
				BoardController.toView(pc, adenaBoard, uid);

				AccountDatabase.updateTradeing(pc, true);

				LetterController.toLetter(pc.getName(), pc.getName(), subject,
						content.toString(), 0);

				ChattingController.toChatting(o,
						"아데나: " + Util.numberFormat(adenaCount) + " 판매금액: "
								+ Util.numberFormat(moneyCount),
						Lineage.CHATTING_MODE_MESSAGE);

				// pc.getInventory().count(item, item.getCount() -
				// pc.getPcTrade().getCount(), true);
			} catch (Exception e) {
				lineage.share.System
						.printf("%s : toWrite(PcInstance pc, BoardInstance bi, String subject, String content)\r\n",
								BoardController.class.toString());
				lineage.share.System.println(e);
				// 여기부분에 프린트를 넣어줘용 그러면 어디서 걸리는지 나올거에요 저도 당홯하게 되서 어머 일단 진행을할게요
				// 여기서 걸면 boardcontroller.towite? 이부분이 걸려요
				e.printStackTrace();
			} finally {
				DatabaseConnection.close(con, stt, rs);
			}

		} catch (Exception e) {
			ChattingController.toChatting(o, ".판매 [아덴수량] [가격]",
					Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	/**
	 * 현금 거래 게시판 보여주는 메소드. 2018-07-22 by connector12@nate.com
	 */
	static public void viewBoard(object o) {
		BoardInstance b = BackgroundDatabase.getTradeBoard();
		if (b != null)
			b.toClick((Character) o, null);
	}

	static public void viewBoard1(object o) {
		BoardInstance b = BackgroundDatabase.getServerBoard();
		if (b != null)
			b.toClick((Character) o, null);
	}

	/**
	 * 계정의 구매신청 갯수 확인 메소드. 2018-07-21 by connector12@nate.com
	 */
	static public int accountBuyCount(PcInstance pc) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			con = DatabaseConnection.getLineage();

			st = con.prepareStatement("SELECT COUNT(*) as cnt FROM boards WHERE buy_name=? AND step=?");
			st.setString(1, pc.getName());
			st.setInt(2, 1);
			rs = st.executeQuery();

			if (rs.next())
				return rs.getInt("cnt");
		} catch (Exception e) {
			lineage.share.System.printf(
					"%s : accountBuyCount(PcInstance pc)\r\n",
					BoardController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return 0;
	}

	static public void toAdenaBuy(object o, StringTokenizer st) {
		// long curtime = System.currentTimeMillis() / 1000;
		// if (o.getGm() == 0 && o.getDelaytime() + 5 > curtime) {
		// long sec = (o.getDelaytime() + 5) - curtime;
		// ChattingController.toChatting(o, sec + "초간의 지연시간이 필요합니다.",
		// Lineage.CHATTING_MODE_MESSAGE);
		// return;
		// }

		PcInstance pc = (PcInstance) o;
		if (pc.getLevel() < Lineage.pc_trade_buy_min_level) {
			ChattingController.toChatting(o,
					String.format("구매신청은 최소 %d레벨 부터 이용가능합니다",
							Lineage.pc_trade_buy_min_level),
					Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		int count = accountBuyCount(pc);
		if (Lineage.pc_trade_buy_max_count > 0
				&& Lineage.pc_trade_buy_max_count <= count) {
			ChattingController.toChatting(o, String.format(
					"구매신청은 최대 %d개 등록 가능합니다. 현재 구매신청: %d개",
					Lineage.pc_trade_buy_max_count, count),
					Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		try {
			List<Board> list = new ArrayList<Board>();
			// 해당 게시판의 목록 추출.
			BoardController.getList("giran",
					BoardController.getMaxUid("giran"), list, 5000);

			int uid = Integer.parseInt(st.nextToken());

			for (Board b : list) {
				if (b.getUid() == uid) {
					switch (b.getStep()) {
					case 0:
						if (pc.getGm() == 0
								&& b.getAccountId().equals(
										pc.getClient().getAccountId())) {
							ChattingController.toChatting(o,
									"자신의 물품을 구매신청하실 수 없습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							return;
						}

						// 판매자의 이름을 찾는다.
						PcInstance use = World.findPc(b.getName());
						// 판매자가 접속중이지 않으면 안된다.
						if (use == null) {
							ChattingController.toChatting(o,
									"현재 사용자가 접속중이지 않아 거래가 불가능합니다.");
							return;

						}

						Connection con = null;
						PreparedStatement stt = null;
						ResultSet rs = null;
						try {
							con = DatabaseConnection.getLineage();
							stt = con
									.prepareStatement("SELECT * FROM boards WHERE type=? AND buy_account_uid=? AND step in (1, 2)");
							stt.setString(1, "giran");
							stt.setInt(2, pc.getClient().getAccountUid());
							rs = stt.executeQuery();
							// if (rs.next()) {
							// ChattingController.toChatting(o,
							// "구매중인 물품이 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
							// return;
							// }

							stt = con
									.prepareStatement("UPDATE boards SET buy_account_id=?,buy_account_uid=?, buy_objid=?, subject=?, buy_name=?, step=?, moddate=now(), buy_apply_day=? WHERE uid=? AND type=?");
							stt.setString(1, pc.getClient().getAccountId());
							stt.setInt(2, pc.getClient().getAccountUid());
							stt.setLong(3, pc.getObjectId());
							stt.setString(4, "거래중");
							stt.setString(5, pc.getName());
							stt.setInt(6, 1); // 상태값 구매진행중 : 1 로 변경
							stt.setTimestamp(
									7,
									new java.sql.Timestamp(System
											.currentTimeMillis()));
							stt.setInt(8, uid);
							stt.setString(9, "giran");
							stt.executeUpdate();
							stt.close();
							adenaBoard = BackgroundDatabase.getTradeBoard();
							BoardController.toView(pc, adenaBoard, uid);
						} catch (Exception e) {
							lineage.share.System
									.printf("%s : toWrite(PcInstance pc, BoardInstance bi, String subject, String content)\r\n",
											BoardController.class.toString());
							lineage.share.System.println(e);
						} finally {
							DatabaseConnection.close(con, stt, rs);
						}

						StringBuilder content = new StringBuilder();

						content.append("거래상황: 입금대기중\r\n");
						content.append("거래번호: " + uid + "\r\n");
						content.append("판매자: " + use.getName() + "\r\n");
						content.append("구매자: " + pc.getName() + "\r\n");
						content.append("\r\n");
						content.append("아데나 : "
								+ Util.numberFormat(b.getAdena()) + "\r\n");
						content.append("판매금액: "
								+ Util.numberFormat(b.getMoney()) + "\r\n");
						content.append("\r\n");
						content.append("은 행 명: "
								+ use.getClient().getBankName() + "\r\n");
						content.append("계좌번호: "
								+ use.getClient().getBankNumber() + "\r\n");
						content.append("예 금 주: "
								+ use.getClient().getBankUserName() + "\r\n");
						content.append("연 락 처: "
								+ use.getClient().getBankUserNumber() + "\r\n");
						content.append("\r\n");
						content.append("* 구매신청방법\r\n");
						content.append("위 계좌로입금후 [.입급완료]을 입력해주세요.\r\n");

						LetterController.toLetter(use.getName(), pc.getName(),
								b.getSubject(), content.toString(), 0);

						StringBuilder content1 = new StringBuilder();

						content1.append("거래상황: 입금대기중\r\n");
						content1.append("거래번호: " + uid + "\r\n");
						content1.append("판매자: " + use.getName() + "\r\n");
						content1.append("구매자: " + pc.getName() + "\r\n");
						content1.append("\r\n");
						content1.append("아데나 : "
								+ Util.numberFormat(b.getAdena()) + "\r\n");
						content1.append("판매금액: "
								+ Util.numberFormat(b.getMoney()) + "\r\n");
						content1.append("\r\n");
						content1.append("\r\n");
						content1.append("\r\n");
						content1.append("\r\n");
						content1.append("\r\n");
						content1.append("\r\n");

						LetterController.toLetter(pc.getName(), use.getName(),
								b.getSubject(), content1.toString(), 0);

						// 구매자에게 판매자의 계정u아이디를 넣는다.
						// use = 판매자 , pc = 구매자
						pc.getClient().setTradeUid(
								use.getClient().getAccountUid());
						pc.getClient().setBordUid(uid);
						AccountDatabase.updateTradeUid(pc, use);
						AccountDatabase.updateBordUid(pc, b);

						use.getClient().setTradeUid(
								pc.getClient().getAccountUid());
						use.getClient().setBordUid(uid);
						AccountDatabase.updateTradeUid(use, pc);
						AccountDatabase.updateBordUid(use, b);

						ChattingController
								.toChatting(o, "거래번호:" + uid + "를 구매신청하였습니다.",
										Lineage.CHATTING_MODE_MESSAGE);
						break;
					case 1:
						ChattingController.toChatting(o, "구매진행중입니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						break;
					case 2:
						ChattingController.toChatting(o, "입금대기중입니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						break;
					case 3:
						ChattingController.toChatting(o, "판매완료되었습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						break;
					}
				}
			}

			// o.setDelaytime(curtime);

			// 패킷 처리
			// pc.toSender(S_BoardList.clone(BasePacketPooling.getPool(S_BoardList.class),
			// adenaBoard, list));
		} catch (Exception e) {
			ChattingController.toChatting(o, ".구매 [거래번호]",
					Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	static public void toAdenaCancel(object o, StringTokenizer st) {
		// long curtime = System.currentTimeMillis() / 1000;
		// if (o.getGm() == 0 && o.getDelaytime() + 5 > curtime) {
		// long sec = (o.getDelaytime() + 5) - curtime;
		// ChattingController.toChatting(o, sec + "초간의 지연시간이 필요합니다.",
		// Lineage.CHATTING_MODE_MESSAGE);
		// return;
		// }

		PcInstance pc = (PcInstance) o;
		try {
			List<Board> list = new ArrayList<Board>();
			// 해당 게시판의 목록 추출.
			BoardController.getList("giran",
					BoardController.getMaxUid("giran"), list, 5000);

			int uid = Integer.parseInt(st.nextToken());
			boolean is = false;
			for (Board b : list) {
				if (b.getAccountId().equals(pc.getClient().getAccountId())
						&& b.getUid() == uid
						|| (o.getGm() > 0 && b.getUid() == uid)) {

					// 거래 진행 상태가 판매 등록 : 0, 구매진행중 : 1 일때
					if (b.getStep() == 0) {
						long diffSec = (System.currentTimeMillis() - b
								.getDays()) / 1000;

						Connection con = null;
						PreparedStatement stt = null;
						ResultSet rs = null;
						try {
							con = DatabaseConnection.getLineage();
							stt = con
									.prepareStatement("DELETE FROM boards WHERE uid=? AND type=?");
							stt.setInt(1, uid);
							stt.setString(2, "giran");
							stt.executeUpdate();
							stt.close();
							adenaBoard = BackgroundDatabase.getTradeBoard();
							BoardController.toView(pc, adenaBoard, uid);
						} catch (Exception e) {
							lineage.share.System
									.printf("%s : toWrite(PcInstance pc, BoardInstance bi, String subject, String content)\r\n",
											BoardController.class.toString());
							lineage.share.System.println(e);
						} finally {
							DatabaseConnection.close(con, stt, rs);
						}

						double cancelRate = 1;
						if (diffSec < 60 * 60 * Lineage.adena_trade_regedit_cancel_hour) {
							cancelRate = Lineage.adena_trade_regedit_cancel_rate;
						}
						PcInstance pcc = World.findPc(b.getName());
						ItemInstance adena = pc.getInventory().findAden();
						if (pc.getGm() > 0) {
							if (adena != null) {
								pcc.getInventory().append(adena,
										(long) (b.getAdena() * cancelRate));
							} else {
								Item i = ItemDatabase.find("아데나");
								adena = ItemDatabase.newInstance(i);

								adena.setCount((int) (b.getAdena() * cancelRate));
								// 인벤에 등록처리.

								pcc.getInventory().append(adena,
										(int) (b.getAdena() * cancelRate));
							}
							AccountDatabase.updateTradeing(pc, false);

							ChattingController.toChatting(o, "거래번호:" + uid
									+ "를 판매취소를 하였습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
						} else {
							if (adena != null) {
								pc.getInventory().append(adena,
										(long) (b.getAdena() * cancelRate));
							} else {
								Item i = ItemDatabase.find("아데나");
								adena = ItemDatabase.newInstance(i);

								adena.setCount((int) (b.getAdena() * cancelRate));
								// 인벤에 등록처리.

								pc.getInventory().append(adena,
										(int) (b.getAdena() * cancelRate));
							}
							ChattingController.toChatting(o, "거래번호:" + uid
									+ "를 판매취소를 하였습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							pc.setCancelTime(System.currentTimeMillis()
									+ (1000 * 60 * 60 * Lineage.pc_trade_sell_time));
						}

					} else if (b.getStep() == 1) {
						ChattingController.toChatting(o,
								"입금대기중에는 판매취소를 할 수 없습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
					}
					// 구매자가 입금대기중일때
					else if (b.getStep() == 2) {
						ChattingController.toChatting(o,
								"입금확인중에는 판매취소를 할 수 없습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
					}
					// 판매완료된 건
					else if (b.getStep() == 3) {
						ChattingController.toChatting(o,
								"판매완료되어 판매취소를 할 수 없습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
					}

					is = true;
				}
			}

			if (!is) {
				ChattingController.toChatting(o, "판매등록하신 내역이 없습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
			}
			// o.setDelaytime(curtime);
		} catch (Exception e) {
			ChattingController.toChatting(o, ".판매취소 [거래번호]",
					Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	/**
	 * 구매취소 메소드. 2018-07-22 by connector12@nate.com
	 */
	static public void buyCancel1(object o, StringTokenizer st) {
		PcInstance pc = (PcInstance) o;

		try {
			List<Board> list = new ArrayList<Board>();
			int uid = Integer.valueOf(st.nextToken());
			for (Board b : list) {
				if (b.getUid() == uid) {

					// if(b.getStep() == 2)
					// {
					// ChattingController.toChatting(o,
					// "입금대기중에는 판매취소를 할 수 없습니다.",
					// Lineage.CHATTING_MODE_MESSAGE);
					// return;
					// }
					// 판매완료된 건
					if (b.getStep() == 3) {
						ChattingController.toChatting(o,
								"판매완료되어 판매취소를 할 수 없습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						return;
					}

				}

			}
			toAdenaBuyCancel(pc, uid, false);

			// 구매자가 입금대기중일때

		} catch (Exception e) {
			ChattingController.toChatting(pc, Common.COMMAND_TOKEN
					+ "구매취소 [거래번호]", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	static public void toAdenaBuyCancel(PcInstance pc, int uid,
			boolean autoCancel) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			List<Board> list = new ArrayList<Board>();
			// 해당 게시판의 목록 추출.
			BoardController.getList("giran",
					BoardController.getMaxUid("giran"), list, 5000);

			for (Board b : list) {

				if (b.getUid() == uid) {
					String subject = String.format("%,d만 아덴 팝니다.",
							b.getAdena() / 10000);
					// 거래 진행 상태 구매진행중 : 1 일때
					if (b.getStep() == 1 || b.getStep() == 2) {

						try {

							con = DatabaseConnection.getLineage();
							st = con.prepareStatement("UPDATE boards SET subject=?, step = 0 WHERE uid=? AND type=?");
							st.setString(1, subject);
							st.setInt(2, uid);
							st.setString(3, "giran");
							st.executeUpdate();
							st.close();

							// BoardController.toView(pc, adenaBoard, uid);
						} catch (Exception e) {
							lineage.share.System
									.printf("%s : toAdenaBuyCancel(PcInstance pc, BoardInstance bi, String subject, String content)\r\n",
											BoardController.class.toString());
							lineage.share.System.println(e);
						} finally {
							DatabaseConnection.close(con, st, rs);
						}

						ChattingController.toChatting(pc, "거래번호:" + uid
								+ "를 판매등록 상태로 변경 하였습니다.",
								Lineage.CHATTING_MODE_MESSAGE);
						LetterController.toLetter("유저거래", b.getBuyName(),
								String.format("%d번 물품 구매취소", b.getUid()),
								creatLetterContent(b.getBuyName(), autoCancel),
								0);
						LetterController.toLetter("유저거래", b.getName(),
								String.format("%d번 물품 구매취소", b.getUid()),
								creatLetterContent(b.getName(), autoCancel), 0);

					}

				}
				autoCancel = true;
			}

			if (!autoCancel) {
				ChattingController.toChatting(pc, "판매등록하신 내역이 없습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
			}
		} catch (Exception e) {
			ChattingController.toChatting(pc, ".구매취소 [거래번호]",
					Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	/**
	 * 편지 내용 작성 메소드. 2018-07-21 by connector12@nate.com
	 */
	static public String creatLetterContent(String buyName, boolean autoCancel) {
		String temp_content = null;
		StringBuilder content = new StringBuilder();

		content.append("입금 시간이 초과 하여\r\n");
		content.append("구매가 취소됩니다\r\n");
		content.append("판매 상태로 전환합니다\r\n");
		content.append("\r\n");
		content.append("\r\n");
		if (Lineage.pc_trade_buy_deposit_delay > 0)
			content.append(String.format(
					"구매 신청 후 %d분 이내에 [.입금완료]를 하시지 않을 경우 구매가 취소됩니다.",
					Lineage.pc_trade_buy_deposit_delay));
		else
			content.append("입금을 완료하시면 [.입금완료]를 입력해 주시기 바랍니다.");

		temp_content = content.toString();

		return temp_content;
	}

	static public void 입금완료(object o, int uid) {
		PcInstance pc = (PcInstance) o;
		PcInstance use = World.findPcAccountUid(pc.getClient().getTradeUid());

		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			List<Board> list = new ArrayList<Board>();
			// 해당 게시판의 목록 추출.
			BoardController.getList("giran",
					BoardController.getMaxUid("giran"), list, 5000);
			for (Board b : list) {

				if (b.getUid() == uid) {
					try {

						con = DatabaseConnection.getLineage();
						st = con.prepareStatement("UPDATE boards SET step = 2 WHERE uid=? AND type=?");

						st.setInt(1, uid);
						st.setString(2, "giran");
						st.executeUpdate();
						st.close();

						// BoardController.toView(pc, adenaBoard, uid);
					} catch (Exception e) {
						lineage.share.System
								.printf("%s : 입금완료(PcInstance pc, BoardInstance bi, String subject, String content)\r\n",
										BoardController.class.toString());
						lineage.share.System.println(e);
					} finally {
						DatabaseConnection.close(con, st, rs);
					}
					letter = true;

					StringBuilder content = new StringBuilder();
					content.append("거래상황: 입금메세지\r\n");
					content.append("거래번호: " + pc.getClient().getBordUid()
							+ "\r\n");
					content.append("구매자: " + use.getName() + "\r\n");
					content.append("\r\n");
					content.append("구매자의 입금완료 되었으나  확인바랍니다.\r\n");
					content.append("\r\n");
					content.append("*입금확인이 되셨으면,\r\n");
					content.append("[.입금확인 거래번호]를 입력하여 주시면 거래완료됩니다.\r\n");
					LetterController.toLetter(pc.getName(), use.getName(),
							"입금확인하세요.", content.toString(), 0);

					ChattingController.toChatting(o, "판매자에게 확인 편지를 보냈습니다.");
				}
			}
		} catch (Exception e) {
			ChattingController.toChatting(pc, ".입금완료 [거래번호]",
					Lineage.CHATTING_MODE_MESSAGE);
		}

		// int uid = (int) use.getClient().getAccountUid();
		// if (uid == 0 || letter) {
		// ChattingController.toChatting(o, "이미 편지를 보냈습니다.",
		// Lineage.CHATTING_MODE_MESSAGE);
		// return;
		// }

	}

	/**
	 * 해당 게시글이 존재하는지 확인하는 메소드. 2018-07-20 by connector12@nate.com
	 */
	static public boolean isTradeUid(int uid) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			con = DatabaseConnection.getLineage();

			// 제거.
			st = con.prepareStatement("SELECT buy_objId FROM boards WHERE uid=?");
			st.setInt(1, uid);
			rs = st.executeQuery();

			if (rs.next())
				return true;
		} catch (Exception e) {
			lineage.share.System.printf("%s : isTradeUid(int uid)\r\n",
					BoardController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}

		return false;
	}

	/**
	 * 입금완료 메소드. 2018-07-22 by connector12@nate.com
	 */
	static public void 입금완료1(object o, StringTokenizer st) {
		PcInstance pc = (PcInstance) o;

		try {
			int uid = Integer.valueOf(st.nextToken());

			if (!isTradeUid(uid)) {
				ChattingController.toChatting(pc, "\\fR해당 게시물이 존재하지 않습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			if (pc.getGm() == 0 && !isTradeBuy(pc, uid)) {
				ChattingController.toChatting(pc,
						String.format("\\fR%d번 물품의 구매 신청자가 아닙니다.", uid),
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			if (isTradeStep(uid) != 1) {
				ChattingController.toChatting(pc, "\\fR거래중인 게시글만 입금완료 가능합니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			입금완료(o, uid);
		} catch (Exception e) {
			ChattingController.toChatting(pc, Common.COMMAND_TOKEN
					+ "입금완료 [거래번호]", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	/**
	 * 거래상태 확인하는 메소드. 2018-07-20 by connector12@nate.com
	 */
	static public int isTradeStep(int uid) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		int step = 0;

		try {
			con = DatabaseConnection.getLineage();

			// 제거.
			st = con.prepareStatement("SELECT step FROM boards WHERE uid=?");
			st.setInt(1, uid);
			rs = st.executeQuery();

			if (rs.next())
				step = rs.getInt(1);
		} catch (Exception e) {
			lineage.share.System.printf("%s : isTradeStep(int uid)\r\n",
					BoardController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}

		return step;
	}

	/**
	 * 계정 정보로 해당 게시글 구매자 확인 메소드. 2018-07-20 by connector12@nate.com
	 */
	static public boolean isTradeBuy(PcInstance pc, int uid) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			con = DatabaseConnection.getLineage();

			// 제거.
			st = con.prepareStatement("SELECT buy_account_uid FROM boards WHERE uid=?");
			st.setInt(1, uid);
			rs = st.executeQuery();

			if (rs.next()) {
				if (rs.getInt(1) == pc.getClient().getAccountUid())
					return true;
			}
		} catch (Exception e) {
			lineage.share.System.printf(
					"%s : isTradeBuy(PcInstance pc, int uid)\r\n",
					BoardController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}

		return false;
	}

	/**
	 * 계정 정보로 해당 게시글 판매자 확인 메소드. 2018-07-20 by connector12@nate.com
	 */
	static public boolean isTradeSell(PcInstance pc, int uid) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			con = DatabaseConnection.getLineage();

			// 제거.
			st = con.prepareStatement("SELECT sell_account_uid FROM boards WHERE uid=?");
			st.setInt(1, uid);
			rs = st.executeQuery();

			if (rs.next()) {
				if (rs.getInt(1) == pc.getClient().getAccountUid())
					return true;
			}
		} catch (Exception e) {
			lineage.share.System.printf(
					"%s : isTradeSell(PcInstance pc, int uid)\r\n",
					BoardController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}

		return false;
	}

	/**
	 * 입금확인 메소드. 2018-07-22 by connector12@nate.com
	 */
	static public void 입금확인(object o, StringTokenizer st) {
		PcInstance pc = (PcInstance) o;

		try {
			int uid = Integer.valueOf(st.nextToken());

			if (!isTradeUid(uid)) {
				ChattingController.toChatting(pc, "\\fR해당 게시물이 존재하지 않습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			if (pc.getGm() == 0 && !isTradeSell(pc, uid)) {
				ChattingController.toChatting(pc,
						String.format("\\fR%d번 물품의 판매자가 아닙니다.", uid),
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			if (isTradeStep(uid) == 3) {
				ChattingController.toChatting(pc, "\\fR거래가 완료된 물품입니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			if (isTradeStep(uid) != 2) {
				ChattingController.toChatting(pc, "\\fR해당 물품은 입금완료 상태가 아닙니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			입금확인1(pc, uid);
		} catch (Exception e) {
			ChattingController.toChatting(pc, Common.COMMAND_TOKEN
					+ "입금확인 [거래번호]", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	static public void 입금확인1(object o, int uid) {
		PcInstance pc = (PcInstance) o;
		PcInstance use = World.findPcAccountUid(pc.getClient().getTradeUid());

		// pc 판매자 , use 구매자
		try {
			List<Board> list = new ArrayList<Board>();
			// 해당 게시판의 목록 추출.
			BoardController.getList("giran",
					BoardController.getMaxUid("giran"), list, 5000);

			int step = 3;

			if (uid == 0) {
				ChattingController.toChatting(o, "거래가 종료되었습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			for (Board b : list) {
				if (b.getUid() == uid) {
					if (step == 3) {

						if (use == null) {
							ChattingController.toChatting(o, "구매자가 월드에 없습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							return;
						}

						if (b.getBuyName().equalsIgnoreCase(pc.getName())) {
							ChattingController.toChatting(o,
									"상대방이 입력해야되는 명령어 입니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							return;
						}

						letter = false;

						ItemInstance adena = use.getInventory().findAden();
						if (adena != null) {
							use.getInventory().append(adena, b.getAdena());
						} else {
							Item i = ItemDatabase.find("아데나");
							adena = ItemDatabase.newInstance(i);
							adena.setCount((int) b.getAdena());
							// 인벤에 등록처리.
							use.getInventory()
									.append(adena, (int) b.getAdena());
						}

						ChattingController.toChatting(use,
								"구매한 아데나가 인벤토리에 들어왔습니다.");
					}

					Connection con = null;
					PreparedStatement stt = null;
					ResultSet rs = null;

					try {
						con = DatabaseConnection.getLineage();
						stt = con
								.prepareStatement("UPDATE boards SET subject=?, step=?, moddate=now() WHERE uid=? AND type=?");
						stt.setString(1, "판매완료");
						stt.setInt(2, step); // 상태값 판매완료 : 3 으로 변경.
						stt.setLong(3, uid);
						stt.setString(4, "giran");
						stt.executeUpdate();
						stt.close();

					} catch (Exception e) {
						lineage.share.System
								.printf("%s : toWrite(PcInstance pc, BoardInstance bi, String subject, String content)\r\n",
										BoardController.class.toString());
						lineage.share.System.println(e);
					} finally {
						DatabaseConnection.close(con, stt, rs);
					}

					ChattingController.toChatting(o, "거래번호:" + uid
							+ "가 판매완료 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);

					if (step == 3) {
						AccountDatabase.updateBordUid2(pc);
						AccountDatabase.updateTradeUid2(pc);
						pc.getClient().setBordUid(0);
						pc.getClient().setTradeUid(99999);

						AccountDatabase.updateBordUid2(use);
						AccountDatabase.updateTradeUid2(use);
						use.getClient().setBordUid(0);
						use.getClient().setTradeUid(99999);

						AccountDatabase.updateTradeing(pc, false);
					}
				}
			}
		} catch (Exception e) {
			ChattingController.toChatting(o, ".입금완료확인 [거래번호]",
					Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	// // 쓰이지 않음...??
	// static public void toAdenaStep(object o, StringTokenizer st) {
	// PcInstance pc = (PcInstance) o;
	// try {
	// List<Board> list = new ArrayList<Board>();
	// // 해당 게시판의 목록 추출.
	// BoardController.getList("giran", BoardController.getMaxUid("giran"),
	// list, 5000);
	//
	// int uid = Integer.parseInt(st.nextToken());
	// int step = Integer.parseInt(st.nextToken());
	// for (Board b : list) {
	// if (b.getUid() == uid) {
	//
	// if (step == 3) {
	// PcInstance target = World.findPc(b.getBuyName());
	// if (target == null) {
	// ChattingController.toChatting(o, "구매자가 월드에 존재하지 않습니다.",
	// Lineage.CHATTING_MODE_MESSAGE);
	// return;
	// }
	//
	// ItemInstance adena = target.getInventory().findAden();
	// if (adena != null) {
	// target.getInventory().append(adena, b.getAdena());
	// } else {
	// Item i = ItemDatabase.find("아데나");
	// adena = ItemDatabase.newInstance(i);
	// adena.setCount((int) b.getAdena());
	// // 인벤에 등록처리.
	// target.getInventory().append(adena, (int) b.getAdena());
	// }
	// }
	//
	// Connection con = null;
	// PreparedStatement stt = null;
	// ResultSet rs = null;
	//
	// try {
	// con = DatabaseConnection.getLineage();
	// stt =
	// con.prepareStatement("UPDATE boards SET step=?, moddate=now() WHERE uid=? AND type=?");
	// stt.setInt(1, step);
	// stt.setInt(2, uid);
	// stt.setString(3, "giran");
	// stt.executeUpdate();
	// stt.close();
	// } catch (Exception e) {
	// lineage.share.System.printf(
	// "%s : toWrite(PcInstance pc, BoardInstance bi, String subject, String content)\r\n",
	// BoardController.class.toString());
	// lineage.share.System.println(e);
	// } finally {
	// DatabaseConnection.close(con, stt, rs);
	// }
	//
	// ChattingController.toChatting(o, "거래번호:" + uid + "를 " + step +
	// "으로 변경하였습니다.",
	// Lineage.CHATTING_MODE_MESSAGE);
	//
	// if (step == 3) {
	// }
	// }
	// }
	// } catch (Exception e) {
	// ChattingController.toChatting(o, "-완료 거래번호 [0:판매, 1:구매, 2:대기, 3:완료]",
	// Lineage.CHATTING_MODE_MESSAGE);
	// }
	// }

	// /**
	// * 상점 시세.
	// * 2019-08-19
	// * by connector12@nate.com
	// */
	// static private void shopPrice(object o, StringTokenizer st,
	// PcShopInstance pc_shop) {
	// PcInstance pc = (PcInstance) o;
	// try {
	// pc.marketPrice.clear();
	// List<String> list = new ArrayList<String>();
	// String itemName = st.nextToken();
	// int en = 0;
	// int bless = 1;
	// int index = 1;
	// boolean isEn = false;
	// boolean isBless = false;
	//
	// // 인챈 검색
	// if (st.hasMoreTokens()) {
	// en = Integer.valueOf(st.nextToken());
	//
	// if (en != 0)
	// isEn = true;
	// }
	//
	// // 축복 여부 검색
	// if (st.hasMoreTokens()) {
	// switch (st.nextToken()) {
	// case "축" :
	// case "축복" :
	// bless = 0;
	// isBless = true;
	// break;
	// case "일반" :
	// bless = 1;
	// isBless = true;
	// break;
	// case "저주" :
	// bless = 2;
	// isBless = true;
	// break;
	// }
	// }
	//
	// list.add((bless == 0 ? "(축) " : bless == 2 ? "(저주) " : "") + (en > 0 ?
	// "+" + en + " " : en < 0 ? en + " " : "") + itemName);
	//
	// for (PcShopInstance shpoList : PcMarketController.getShopList().values())
	// {
	// if (shpoList.getX() > 0 && shpoList.getY() > 0 &&
	// shpoList.getPc_objectId() != pc.getObjectId()) {
	// for (PcShop s : shpoList.getShopList_().values()) {
	// if (s.getItem() == null)
	// continue;
	//
	// if (s.getItem().getName().contains(itemName) && (!isEn || (isEn &&
	// s.getInvItemEn() == en)) && (!isBless || (isBless && s.getInvItemBress()
	// == bless))) {
	// marketPrice mp = new marketPrice();
	// StringBuffer sb = new StringBuffer();
	//
	//
	// sb.append(String.format("%d. %s", index++, s.getInvItemBress() == 0 ?
	// "(축) " : s.getInvItemBress() == 1 ? "" : "(저주) "));
	//
	// if (s.getInvItemEn() > 0)
	// sb.append(String.format("+%d ", s.getInvItemEn()));
	//
	// sb.append(String.format("%s", s.getItem().getName()));
	//
	// if (s.getInvItemCount() > 1)
	// sb.append(String.format("(%d)", s.getInvItemCount()));
	//
	// sb.append(String.format(" [판매 금액]: %s 아데나",
	// Util.changePrice(s.getPrice())));
	// list.add(sb.toString());
	//
	// mp.setShopNpc(shpoList);
	// mp.setX(shpoList.getX());
	// mp.setY(shpoList.getY());
	// mp.setMap(shpoList.getMap());
	// mp.setObjId(shpoList.getObjectId());
	// pc.marketPrice.add(mp);
	// }
	// }
	// } else {
	// continue;
	// }
	// }
	//
	// if (list.size() < 2 || pc.marketPrice.size() < 1) {
	// pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class),
	// PcMarketController.marketPriceNPC, "marketprice1", null, list));
	// return;
	// } else {
	// int count = 60 - (list.size() - 1);
	//
	//
	// for (int i = 0; i < count; i++)
	// list.add(" ");
	// // 시세 검색 결과 html 패킷 보냄.
	// pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class),
	// PcMarketController.marketPriceNPC, "marketprice", null, list));
	// }
	// } catch (Exception e) {
	// ChattingController.toChatting(pc, "\\fT.시세 아이템",
	// Lineage.CHATTING_MODE_MESSAGE);
	// ChattingController.toChatting(pc, "\\fT.시세 아이템 인첸(0은 모두)",
	// Lineage.CHATTING_MODE_MESSAGE);
	// ChattingController.toChatting(pc, "\\fT.시세 아이템 인첸(0은 모두) 축여부(축,저주,일반)",
	// Lineage.CHATTING_MODE_MESSAGE);
	// }
	// }

	/**
	 * 일정 기간 경과해도 입금완료 하지 않는 거래 구매취소 처리 메소드. 2018-07-23 by connector12@nate.com
	 */
	static public void toTimer(long time) {

		if (checkTime <= time && Lineage.pc_trade_buy_deposit_delay > 0) {
			// 10초 마다 체크
			checkTime = time + (1000 * 10);

			Connection con = null;
			PreparedStatement st = null;
			ResultSet rs = null;

			try {
				con = DatabaseConnection.getLineage();

				st = con.prepareStatement("SELECT * FROM boards WHERE step=?");
				st.setInt(1, 1);
				rs = st.executeQuery();

				while (rs.next()) {
					if (rs.getTimestamp("buy_apply_day").getTime()
							+ (1000 * 60 * Lineage.pc_trade_buy_deposit_delay) < time)
						toAdenaBuyCancel(null, rs.getInt("uid"), true);
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : toTimer(long time)\r\n",
						CommandController.class.toString());
				lineage.share.System.println(e);
				e.printStackTrace();
			} finally {
				DatabaseConnection.close(con, st, rs);
			}
		}

	}

	static private void toMarblesCharacterInfo(object o, StringTokenizer st) {
		try {
			String char_name = st.nextToken();

			if (isCharacterMarblesMake(char_name)) {
				ChattingController.toChatting(o,
						"[서버알림] 캐릭터 구슬화가 되어있지 않은 캐릭터 입니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			List<String> info = new ArrayList<String>();
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = DatabaseConnection.getLineage();
				pstm = con
						.prepareStatement("SELECT * FROM characters WHERE name=?");
				pstm.setString(1, char_name);
				rs = pstm.executeQuery();
				if (rs.next()) {
					int classType = rs.getInt("class");
					int level = rs.getInt("level");
					Double char_exp = rs.getDouble("exp");
					String class_type = "";
					switch (classType) {
					case 0:
						class_type = "군주";
						break;
					case 1:
						class_type = "기사";
						break;
					case 2:
						class_type = "요정";
						break;
					case 3:
						class_type = "법사";
						break;
					case 4:
						class_type = "다크엘프";
						break;
					}
					info.add(char_name);
					info.add(class_type);
					info.add("" + level);
					double exp = 0.0;
					Exp exp_bean = ExpDatabase.find(level);
					if (exp_bean != null) {
						double e = char_exp
								- (exp_bean.getBonus() - exp_bean.getExp());
						exp = (e / exp_bean.getExp()) * 100;
					}
					info.add(String.format("%.2f%%", exp) + "");

					int hp = rs.getInt("maxHP");
					int mp = rs.getInt("maxMP");
					info.add("" + hp);
					info.add("" + mp);
					int str = rs.getInt("str");
					int dex = rs.getInt("dex");
					int condi = rs.getInt("con");
					int wis = rs.getInt("wis");
					int intel = rs.getInt("inter");
					int cha = rs.getInt("cha");

					int lvStr = rs.getInt("lvStr");
					int lvCon = rs.getInt("lvCon");
					int lvDex = rs.getInt("lvDex");
					int lvWis = rs.getInt("lvWis");
					int lvInt = rs.getInt("lvInt");
					int lvCha = rs.getInt("lvCha");

					int elixir_str = rs.getInt("elixirStr");
					int elixir_con = rs.getInt("elixirCon");
					int elixir_dex = rs.getInt("elixirDex");
					int elixir_wis = rs.getInt("elixirWis");
					int elixir_int = rs.getInt("elixirInt");
					int elixir_cha = rs.getInt("elixirCha");
					info.add(str + " (" + lvStr + " + " + elixir_str + ")");
					info.add(dex + " (" + lvDex + " + " + elixir_dex + ")");
					info.add(condi + " (" + lvCon + " + " + elixir_con + ")");
					info.add(wis + " (" + lvWis + " + " + elixir_wis + ")");
					info.add(intel + " (" + lvInt + " + " + elixir_int + ")");
					info.add(cha + " (" + lvCha + " + " + elixir_cha + ")");

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DatabaseConnection.close(con, pstm, rs);
			}
			o.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), o,
					"marbles_info", null, info));
		} catch (Exception e) {
			ChattingController.toChatting(o, Common.COMMAND_TOKEN
					+ "캐릭확인 [구매하고자하는 캐릭터명]", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	/**
	 * 시장 검색
	 * 
	 * @param
	 * @return 2019-04-08 by june.
	 */
	static public void toPcShopFind(object o, StringTokenizer st) {

		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			pc.clearShopName();
			List<String> info = new ArrayList<String>();
			info.clear();
			String item_name = st.nextToken().toLowerCase(); // 아이템 명
			int en = -1;
			int bless = -1;

			if (st.hasMoreTokens()) {
				en = Integer.valueOf(st.nextToken());
			}
			if (st.hasMoreTokens()) {
				bless = Integer.valueOf(st.nextToken());
			}

			List<PersnalShopItem> find_buy_list = new ArrayList<PersnalShopItem>();
			for (PersnalShopInstance wpcs : World.getPersnalShopList()) {
				for (PersnalShopItem s : wpcs.getItemList()) {
					if (s.getName().replace(" ", "").contains(item_name)
							&& (bless != -1 ? s.getBless() == bless : true)
							&& (en != -1 ? s.getEnLevel() == en : true)) {
						find_buy_list.add(s);
					}
				}
			}

			List<PersnalShopItem> find_sell_list = new ArrayList<PersnalShopItem>();
			for (PersnalShopInstance wpcs : World.getPersnalShopList()) {
				for (PersnalShopItem s : wpcs.getSellList()) {
					if (s.getName().replace(" ", "").contains(item_name)
							&& (bless != -1 ? s.getBless() == bless : true)
							&& (en != -1 ? s.getEnLevel() == en : true)) {
						find_sell_list.add(s);
					}
				}
			}

			info.add("검색명 : "
					+ item_name
					+ (en != -1 ? " / 인챈트 : " + en : "")
					+ (bless != -1 ? " / 축여부 : "
							+ (bless == 0 ? "축복" : bless == 2 ? "저주" : "일반")
							: ""));
			info.add(String.format("총 판매(%d/%d) / 구매(%d/%d) 검색되었습니다",
					find_buy_list.size() < 10 ? find_buy_list.size() : 10,
					find_buy_list.size(),
					find_sell_list.size() < 10 ? find_sell_list.size() : 10,
					find_sell_list.size()));

			if (find_buy_list.size() == 0 && find_sell_list.size() == 0) {
				ChattingController.toChatting(pc, "\\fW검색된 아이템이 없습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			Collections.sort(find_buy_list, new NoDescCompare());
			Collections.sort(find_sell_list, new NoDescCompare1());

			PersnalShopItem find_buy = null;

			for (int i = 0; i < 10; i++) {
				try {
					find_buy = find_buy_list.get(i);
				} catch (Exception e) {
					find_buy = null;
				}

				if (find_buy == null) {
					info.add("-");
					pc.addShopNameList("-");
				} else {
					info.add("["
							+ Util.getPriceFormat((int) find_buy.getPrice())
							+ "] " + find_buy.getName());
					pc.addShopNameList(find_buy.getCharName());
				}
			}

			PersnalShopItem find_sell = null;// 아이건 시세에요 0 0 0 리턴시킬라고 텔포하는데
												// 찾는중이에요
			for (int i = 0; i < 10; i++) {
				try {
					find_sell = find_sell_list.get(i);
				} catch (Exception e) {
					find_sell = null;
				}

				if (find_sell == null) {
					info.add("-");
					pc.addShopNameList("-");
				} else {
					info.add("["
							+ Util.getPriceFormat((int) find_sell.getPrice())
							+ "] " + find_sell.getName());
					pc.addShopNameList(find_sell.getCharName());
				}
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class),
					pc, "sise", null, info));
			ChattingController.toChatting(pc, "\\fW리스트를 클릭하면 해당 무인상점으로 이동합니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	/**
	 * 시장 검색(일반)
	 * 
	 * @param
	 * @return 2019-04-08 by june.
	 */
	static public void toPcShopFindNormal(object o, StringTokenizer st) {

		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			pc.clearShopName();
			List<String> info = new ArrayList<String>();
			info.clear();
			String item_name = st.nextToken().toLowerCase(); // 아이템 명
			int en = -1;
			int bless = -1;
			if (st.hasMoreTokens()) {
				en = Integer.valueOf(st.nextToken());
			}

			List<PersnalShopItem> find_buy_list = new ArrayList<PersnalShopItem>();
			for (PersnalShopInstance wpcs : World.getPersnalShopList()) {
				for (PersnalShopItem s : wpcs.getItemList()) {
					if (s.getName().replace(" ", "").contains(item_name)
							&& (s.getBless() == 1)
							&& (en != -1 ? s.getEnLevel() == en : true)) {
						find_buy_list.add(s);
					}
				}
			}

			List<PersnalShopItem> find_sell_list = new ArrayList<PersnalShopItem>();
			for (PersnalShopInstance wpcs : World.getPersnalShopList()) {
				for (PersnalShopItem s : wpcs.getSellList()) {
					if (s.getName().replace(" ", "").contains(item_name)
							&& (s.getBless() == 1)
							&& (en != -1 ? s.getEnLevel() == en : true)) {
						find_sell_list.add(s);
					}
				}
			}

			info.add("검색명 : "
					+ item_name
					+ (en != -1 ? " / 인챈트 : " + en : "")
					+ (bless != -1 ? " / 축여부 : "
							+ (bless == 0 ? "축복" : bless == 2 ? "저주" : "일반")
							: ""));
			info.add(String.format("총 판매(%d/%d) / 구매(%d/%d) 검색되었습니다",
					find_buy_list.size() < 10 ? find_buy_list.size() : 10,
					find_buy_list.size(),
					find_sell_list.size() < 10 ? find_sell_list.size() : 10,
					find_sell_list.size()));

			if (find_buy_list.size() == 0 && find_sell_list.size() == 0) {
				ChattingController.toChatting(pc, "\\fW검색된 아이템이 없습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			Collections.sort(find_buy_list, new NoDescCompare());
			Collections.sort(find_sell_list, new NoDescCompare1());

			PersnalShopItem find_buy = null;

			for (int i = 0; i < 10; i++) {
				try {
					find_buy = find_buy_list.get(i);
				} catch (Exception e) {
					find_buy = null;
				}

				if (find_buy == null) {
					info.add("-");
					pc.addShopNameList("-");
				} else {
					info.add("["
							+ Util.getPriceFormat((int) find_buy.getPrice())
							+ "] " + find_buy.getName());
					pc.addShopNameList(find_buy.getCharName());
				}
			}

			PersnalShopItem find_sell = null;
			for (int i = 0; i < 10; i++) {
				try {
					find_sell = find_sell_list.get(i);
				} catch (Exception e) {
					find_sell = null;
				}

				if (find_sell == null) {
					info.add("-");
					pc.addShopNameList("-");
				} else {
					info.add("["
							+ Util.getPriceFormat((int) find_sell.getPrice())
							+ "] " + find_sell.getName());
					pc.addShopNameList(find_sell.getCharName());
				}
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class),
					pc, "sise", null, info));
			ChattingController.toChatting(pc, "\\fW리스트를 클릭하면 해당 무인상점으로 이동합니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	/**
	 * 시장 검색(축복)
	 * 
	 * @param
	 * @return 2019-04-08 by june.
	 */
	static public void toPcShopFindBless(object o, StringTokenizer st) {

		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			pc.clearShopName();
			List<String> info = new ArrayList<String>();
			info.clear();
			String item_name = st.nextToken().toLowerCase(); // 아이템 명
			int en = -1;
			int bless = 0;
			if (st.hasMoreTokens()) {
				en = Integer.valueOf(st.nextToken());
			}

			List<PersnalShopItem> find_buy_list = new ArrayList<PersnalShopItem>();
			for (PersnalShopInstance wpcs : World.getPersnalShopList()) {
				for (PersnalShopItem s : wpcs.getItemList()) {
					if (s.getName().replace(" ", "").contains(item_name)
							&& (s.getBless() == 0)
							&& (en != -1 ? s.getEnLevel() == en : true)) {
						find_buy_list.add(s);
					}
				}
			}

			List<PersnalShopItem> find_sell_list = new ArrayList<PersnalShopItem>();
			for (PersnalShopInstance wpcs : World.getPersnalShopList()) {
				for (PersnalShopItem s : wpcs.getSellList()) {
					if (s.getName().replace(" ", "").contains(item_name)
							&& (s.getBless() == 0)
							&& (en != -1 ? s.getEnLevel() == en : true)) {
						find_sell_list.add(s);
					}
				}
			}

			info.add("검색명 : "
					+ item_name
					+ (en != -1 ? " / 인챈트 : " + en : "")
					+ (bless != -1 ? " / 축여부 : "
							+ (bless == 0 ? "축복" : bless == 2 ? "저주" : "일반")
							: ""));
			info.add(String.format("총 판매(%d/%d) / 구매(%d/%d) 검색되었습니다",
					find_buy_list.size() < 10 ? find_buy_list.size() : 10,
					find_buy_list.size(),
					find_sell_list.size() < 10 ? find_sell_list.size() : 10,
					find_sell_list.size()));

			if (find_buy_list.size() == 0 && find_sell_list.size() == 0) {
				ChattingController.toChatting(pc, "\\fW검색된 아이템이 없습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			Collections.sort(find_buy_list, new NoDescCompare());
			Collections.sort(find_sell_list, new NoDescCompare1());

			PersnalShopItem find_buy = null;

			for (int i = 0; i < 10; i++) {
				try {
					find_buy = find_buy_list.get(i);
				} catch (Exception e) {
					find_buy = null;
				}

				if (find_buy == null) {
					info.add("-");
					pc.addShopNameList("-");
				} else {
					info.add("["
							+ Util.getPriceFormat((int) find_buy.getPrice())
							+ "] " + find_buy.getName());
					pc.addShopNameList(find_buy.getCharName());
				}
			}

			PersnalShopItem find_sell = null;
			for (int i = 0; i < 10; i++) {
				try {
					find_sell = find_sell_list.get(i);
				} catch (Exception e) {
					find_sell = null;
				}

				if (find_sell == null) {
					info.add("-");
					pc.addShopNameList("-");
				} else {
					info.add("["
							+ Util.getPriceFormat((int) find_sell.getPrice())
							+ "] " + find_sell.getName());
					pc.addShopNameList(find_sell.getCharName());
				}
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class),
					pc, "sise", null, info));
			ChattingController.toChatting(pc, "\\fW리스트를 클릭하면 해당 무인상점으로 이동합니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	/**
	 * 
	 * 시장 검색(저주)
	 * 
	 * @param
	 * @return 2019-04-08 by june.
	 */
	static public void toPcShopFindCurse(object o, StringTokenizer st) {

		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			pc.clearShopName();
			List<String> info = new ArrayList<String>();
			info.clear();
			String item_name = st.nextToken().toLowerCase(); // 아이템 명
			int en = -1;
			int bless = 2;
			if (st.hasMoreTokens()) {
				en = Integer.valueOf(st.nextToken());
			}

			List<PersnalShopItem> find_buy_list = new ArrayList<PersnalShopItem>();
			for (PersnalShopInstance wpcs : World.getPersnalShopList()) {
				for (PersnalShopItem s : wpcs.getItemList()) {
					if (s.getName().replace(" ", "").contains(item_name)
							&& (s.getBless() == 2)
							&& (en != -1 ? s.getEnLevel() == en : true)) {
						find_buy_list.add(s);
					}
				}
			}

			List<PersnalShopItem> find_sell_list = new ArrayList<PersnalShopItem>();
			for (PersnalShopInstance wpcs : World.getPersnalShopList()) {
				for (PersnalShopItem s : wpcs.getSellList()) {
					if (s.getName().replace(" ", "").contains(item_name)
							&& (s.getBless() == 2)
							&& (en != -1 ? s.getEnLevel() == en : true)) {
						find_sell_list.add(s);
					}
				}
			}

			info.add("검색명 : "
					+ item_name
					+ (en != -1 ? " / 인챈트 : " + en : "")
					+ (bless != -1 ? " / 축여부 : "
							+ (bless == 0 ? "축복" : bless == 2 ? "저주" : "일반")
							: ""));
			info.add(String.format("총 판매(%d/%d) / 구매(%d/%d) 검색되었습니다",
					find_buy_list.size() < 10 ? find_buy_list.size() : 10,
					find_buy_list.size(),
					find_sell_list.size() < 10 ? find_sell_list.size() : 10,
					find_sell_list.size()));

			if (find_buy_list.size() == 0 && find_sell_list.size() == 0) {
				ChattingController.toChatting(pc, "\\fW검색된 아이템이 없습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			Collections.sort(find_buy_list, new NoDescCompare());
			Collections.sort(find_sell_list, new NoDescCompare1());

			PersnalShopItem find_buy = null;

			for (int i = 0; i < 10; i++) {
				try {
					find_buy = find_buy_list.get(i);
				} catch (Exception e) {
					find_buy = null;
				}

				if (find_buy == null) {
					info.add("-");
					pc.addShopNameList("-");
				} else {
					info.add("["
							+ Util.getPriceFormat((int) find_buy.getPrice())
							+ "] " + find_buy.getName());
					pc.addShopNameList(find_buy.getCharName());
				}
			}

			PersnalShopItem find_sell = null;
			for (int i = 0; i < 10; i++) {
				try {
					find_sell = find_sell_list.get(i);
				} catch (Exception e) {
					find_sell = null;
				}

				if (find_sell == null) {
					info.add("-");
					pc.addShopNameList("-");
				} else {
					info.add("["
							+ Util.getPriceFormat((int) find_sell.getPrice())
							+ "] " + find_sell.getName());
					pc.addShopNameList(find_sell.getCharName());
				}
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class),
					pc, "sise", null, info));
			ChattingController.toChatting(pc, "\\fW리스트를 클릭하면 해당 무인상점으로 이동합니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	private static class NoDescCompare implements Comparator<PersnalShopItem> {
		@Override
		public int compare(PersnalShopItem arg0, PersnalShopItem arg1) {
			return arg0.getPrice() < arg1.getPrice() ? -1
					: arg0.getPrice() > arg1.getPrice() ? 1 : 0;
		}
	}

	private static class NoDescCompare1 implements Comparator<PersnalShopItem> {
		@Override
		public int compare(PersnalShopItem arg0, PersnalShopItem arg1) {
			return arg0.getPrice() > arg1.getPrice() ? -1
					: arg0.getPrice() < arg1.getPrice() ? 1 : 0;
		}
	}

	private static long getMarblesCharObjId(String name) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters WHERE name=?");
			st.setString(1, name);
			rs = st.executeQuery();
			if (rs.next()) {
				return rs.getLong("objID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(st, rs);
		}

		return 0;
	}

	public static boolean isCharacterMarblesMake(String name) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters_save_marble WHERE character_name=?");
			st.setString(1, name);
			rs = st.executeQuery();
			if (rs.next()) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(st, rs);
		}

		return true;
	}

	static private void toMarblesCharacterSpellInfo(object o, StringTokenizer st) {
		try {
			String char_name = st.nextToken();

			if (isCharacterMarblesMake(char_name)) {
				ChattingController.toChatting(o,
						"[서버알림] 캐릭터 구슬화가 되어있지 않은 캐릭터 입니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			List<String> info = new ArrayList<String>();
			long objid = getMarblesCharObjId(char_name);
			String skills = CharactersDatabase.getSkillString(objid);

			info.add(char_name);
			info.add(skills);
			o.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), o,
					"marbles_spel", o.getName(), info));
		} catch (Exception e) {
			ChattingController.toChatting(o, Common.COMMAND_TOKEN
					+ "마법확인 [구매하고자하는 캐릭터명]", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	static private void toMarblesCharacterInvenInfo(object o, StringTokenizer st) {
		try {
			String char_name = st.nextToken();

			if (isCharacterMarblesMake(char_name)) {
				ChattingController.toChatting(o,
						"[서버알림] 캐릭터 구슬화가 되어있지 않은 캐릭터 입니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			List<ItemInstance> _list = new ArrayList<ItemInstance>();
			try {
				con = DatabaseConnection.getLineage();
				pstm = con
						.prepareStatement("SELECT * FROM characters_inventory WHERE cha_name=?");
				pstm.setString(1, char_name);
				rs = pstm.executeQuery();
				if (rs.next()) {
					readInventory(_list,
							new StringTokenizer(rs.getString("weapon"), "\r\n"));
					readInventory(_list,
							new StringTokenizer(rs.getString("armor"), "\r\n"));
					readInventory(_list,
							new StringTokenizer(rs.getString("etc"), "\r\n"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DatabaseConnection.close(con, pstm, rs);
			}
			// S_MarblesIvenInfo
			// _list
			o.toSender(S_MarblesIvenInfo.clone(
					BasePacketPooling.getPool(S_MarblesIvenInfo.class), _list));
		} catch (Exception e) {
			ChattingController.toChatting(o, Common.COMMAND_TOKEN
					+ "인벤확인 [구매하고자하는 캐릭터명]", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	static public void readInventory(List<ItemInstance> list,
			StringTokenizer token) {
		while (token.hasMoreTokens()) {
			String data = token.nextToken();
			try {
				String[] db = data.split(",");
				ItemInstance item = ItemDatabase.newInstance(ItemDatabase
						.find(db[1]));
				if (item != null && Long.valueOf(db[2]) > 0) {
					item.setObjectId(Long.valueOf(db[0]));
					item.setCount(Long.valueOf(db[2]));
					item.setQuantity(Integer.valueOf(db[3]));
					item.setEnLevel(Integer.valueOf(db[4]));
					item.setEquipped(Integer.valueOf(db[5]) == 1);
					item.setDefinite(Integer.valueOf(db[6]) == 1);
					item.setBress(Integer.valueOf(db[7]));
					item.setDurability(Integer.valueOf(db[8]));
					item.setNowTime(Integer.valueOf(db[9]));
					item.setPetObjectId(Long.valueOf(db[10]));
					item.setInnRoomKey(Long.valueOf(db[11]));
					item.setLetterUid(Integer.valueOf(db[12]));
					item.setRaceTicket(db[13]);
					if (db.length > 16) {
						StringTokenizer tok = new StringTokenizer(db[16], "|");
						while (tok.hasMoreTokens()) {
							String[] t = tok.nextToken().split("=");
							item.setOption(t[0], t[1]);
						}
					}
					if (db.length > 17) {
						StringTokenizer tok = new StringTokenizer(db[17], "|");
						item.setEnWind(Integer.valueOf(tok.nextToken()));
						item.setEnEarth(Integer.valueOf(tok.nextToken()));
						item.setEnWater(Integer.valueOf(tok.nextToken()));
						item.setEnFire(Integer.valueOf(tok.nextToken()));
					}
					list.add(item);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	// /**
	// * 헬프창 온오프메세지
	// */
	// static public void onoffmassge(object o, StringTokenizer st) {
	// List<String> onoffmassge = new ArrayList<String>();
	// onoffmassge.clear();
	// PcInstance pc = (PcInstance) o;
	//
	// onoffmassge.add(pc.isMark ? "ON" : "OFF");
	// onoffmassge.add(pc.isAutoPickupMessage() ? "ON" : "OFF");
	// // onoffmassge.add(pc.isAutoPotion ? "ON" : "OFF");
	//
	//
	//
	//
	// o.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), o,
	// "help", null, onoffmassge));
	// }

	/**
	 * by all_night
	 */
	static public void characterInfo(object o, StringTokenizer st) {
		List<String> info = new ArrayList<String>();
		info.clear();
		PcInstance pc = (PcInstance) o;
		String name = null;

		if (o.getGm() > 0) {
			try {
				name = st.nextToken();
				pc = World.findPc(name);

				if (pc == null) {
					ChattingController.toChatting(o,
							String.format("%s 캐릭터는 존재하지 않습니다.", name),
							Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
			} catch (Exception e) {
				pc = (PcInstance) o;
			}
		}

		// 캐릭명
		info.add(pc.getName());
		// 클래스
		info.add(pc.getClassType() == 0 ? "Royal"
				: pc.getClassType() == 1 ? "Knight"
						: pc.getClassType() == 2 ? "Elf"
								: pc.getClassType() == 3 ? "Wizard" : "DarkElf");
		// 랭킹
		info.add(String.valueOf(RankController.getRankAll(o)));
		info.add(String.valueOf(RankController.getRankClass(o)));
		info.add(String.valueOf(((PcInstance) o).getPkCount()));
		info.add(String.valueOf(((PcInstance) o).getPkHellCount()));
		int kill = CharactersDatabase.getPvpKill(pc);
		int dead = CharactersDatabase.getPvpDead(pc);
		double total = dead + kill;
		double rate = kill == 0 || total == 0 ? 0 : (kill / total) * 100;
		info.add(String.valueOf(kill));
		info.add(String.valueOf(dead));
		info.add(new DecimalFormat("0.#").format(rate) + "%");

		// Str
		info.add(String.valueOf(pc.getTotalStr()));
		info.add(String.valueOf(pc.getStr()));
		// Dex
		info.add(String.valueOf(pc.getTotalDex()));
		info.add(String.valueOf(pc.getDex()));
		// Con
		info.add(String.valueOf(pc.getTotalCon()));
		info.add(String.valueOf(pc.getCon()));
		// Int
		info.add(String.valueOf(pc.getTotalInt()));
		info.add(String.valueOf(pc.getInt()));
		// Wis
		info.add(String.valueOf(pc.getTotalWis()));
		info.add(String.valueOf(pc.getWis()));
		// Cha
		info.add(String.valueOf(pc.getTotalCha()));
		info.add(String.valueOf(pc.getCha()));

		// 물리 방어력(AC)
		info.add(pc.getTotalAc() - 10 <= 0 ? String.valueOf(10 - pc
				.getTotalAc()) : "-" + String.valueOf(pc.getTotalAc() - 10));
		// 원거리 회피력(ER)
		info.add(String.valueOf(DamageController.toOriginalStatER(pc)));
		// 마법 방어력(MR)
		info.add(String.valueOf(SkillController.getMr(pc, false)));
		// 주술력(SP)
		info.add(String.valueOf(SkillController.getSp((Character) pc, false)));
		// 현재 소지 무게
		info.add(String.valueOf((int) pc.getInventory().getNowWeight()));
		// 최대 소지 무게
		info.add(String.valueOf((int) pc.getInventory().getMaxWeight()));
		// 대미지 감소
		info.add(String.valueOf(pc.getTotalReduction()));
		// 근거리 데미지
		info.add(String.valueOf(pc.getTotalAddDmg()));
		// 근거리 데미지 성공
		info.add(String.valueOf(pc.getDynamicAddHit()));
		// 원거리 데미지
		info.add(String.valueOf(pc.getTotalAddDmgBow()));
		// 원거리 데미지 성공
		info.add(String.valueOf(pc.getDynamicAddHitBow()));
		// pvp대미지
		info.add(String.valueOf(pc.getDynamicAddPvpDmg()));
		// pvp대미지감소
		info.add(String.valueOf(pc.getDynamicAddPvpReduction()));
		// 스턴적중
		// info.add(String.valueOf(pc.getDynamicStunLevel()));
		// 스턴내성
		// info.add(String.valueOf(pc.getDynamicStunDefense() * 100));

		o.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), o, "character", null, info));
	}

	/**
	 * 보스 리스트 2019-08-20 by connector12@nate.com
	 */
	public static void bossList(object o) {
		List<String> bossList = new ArrayList<String>();
		bossList.clear();
		try {
		for (BossSpawn bossSpawn : MonsterBossSpawnlistDatabase.getSpawnList()) {
			bossList.add(String.format("[%s]", bossSpawn.getMonster()));
			bossList.add(String.format("%s", bossSpawn.getSpawnTime()));
			bossList.add(String.format("요일: %s", bossSpawn.getSpawnDay().trim()));
			bossList.add(" ");
		}

		for (int i = 0; i < 150; i++)
			bossList.add(" ");

		o.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), o, "bossList", null, bossList));

		if (BossController.getBossList().size() < 1) {
			ChattingController.toChatting(o, "\\fR생존한 보스가 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		ChattingController.toChatting(o, "\\fRㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ", Lineage.CHATTING_MODE_MESSAGE);

		for (MonsterInstance boss : BossController.getBossList())
			ChattingController.toChatting(o, String.format("\\fY%s %s", Util.getMapName(boss), boss.getMonster().getName()), Lineage.CHATTING_MODE_MESSAGE);

		ChattingController.toChatting(o, "\\fRㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ", Lineage.CHATTING_MODE_MESSAGE);
	
	} catch (Exception e) {
	}
	}
	/**
	 * 시장 좌표
	 * 
	 * @return
	 */
	static public boolean ismarketLocation(object o) {

		if (32718 <= o.getX() && 32818 >= o.getX() && 32781 <= o.getY() && 32880 >= o.getY() && 340 == o.getMap()
				|| (32671 <= o.getX() && 32726 >= o.getX() && 32800 <= o.getY()
						&& 32865 >= o.getY() && 350 == o.getMap() || (32719 <= o.getX()
						&& 32750 >= o.getX()
						&& 32783 <= o.getY()
						&& 32817 >= o.getY() && 360 == o.getMap() || (32721 <= o.getX()
						&& 32751 >= o.getX()
						&& 32784 <= o.getY()
						&& 32814 >= o.getY() && 370 == o.getMap() || (32726 <= o.getX()
						&& 32875 >= o.getX()
						&& 32853 <= o.getY()
						&& 33001 >= o.getY() && 800 == o.getMap()))))) {
			return true;
		}
		return false;
	}

	/**
	 * 시세 좌표
	 * 
	 * @return
	 */
	static public boolean ismarketLocation1(object o) {

		if (32718 <= o.getX()
				&& 32818 >= o.getX()
				&& 32781 <= o.getY()
				&& 32880 >= o.getY()
				&& 340 == o.getMap()
				|| (32671 <= o.getX() && 32726 >= o.getX() && 32800 <= o.getY()
						&& 32865 >= o.getY() && 350 == o.getMap() || (32719 <= o.getX()
						&& 32750 >= o.getX()
						&& 32783 <= o.getY()
						&& 32817 >= o.getY() && 360 == o.getMap() || (32721 <= o.getX()
						&& 32751 >= o.getX()
						&& 32784 <= o.getY()
						&& 32814 >= o.getY() && 370 == o.getMap() || (32726 <= o.getX()
						&& 32875 >= o.getX()
						&& 32853 <= o.getY()
						&& 33001 >= o.getY() && 800 == o.getMap()))))) {
			return true;
		}
		return false;
	}

	static public void toPvpAllRomove(object o) {
		Connection con = null;
		PreparedStatement stt = null;

		try {
			con = DatabaseConnection.getLineage();

			stt = con.prepareStatement("DELETE FROM characters_pvp");
			stt.executeUpdate();
			stt.close();

			stt = con.prepareStatement("UPDATE characters SET pkcount=0");
			stt.executeUpdate();
			stt.close();

			for (PcInstance pc : World.getPcList()) {
				pc.setPkCount(0);
			}
			PvpController.removeAll();
		} catch (Exception e) {
			lineage.share.System.println("[전체 pvp 초기화] 계정, 초기화 오류: " + e);
		} finally {
			DatabaseConnection.close(con, stt);
		}

		if (o != null)
			ChattingController.toChatting(o, "전체 pvp 초기화 완료.",
					Lineage.CHATTING_MODE_MESSAGE);
		else
			lineage.share.System.println("전체 pvp 초기화 완료.");
	}

	static public void inventorySetting(object o, boolean on) {
		if (!isCommandDelay(o, null, 10))
			return;
		try {
			if (o.getInventory() != null && o.getInventory().getList().size() > 0) {
				ChattingController.toChatting(o, "인벤토리 정리가 완료 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
			List<ItemInstance> list = o.getInventory().getList();
			List<ItemInstance> tempList = new ArrayList<ItemInstance>();

			for (ItemInstance i : list) {
				if (i.getItem().getName().equalsIgnoreCase("자동 칼질")) {
					if (!tempList.contains(i)) {
						tempList.add(i);
						o.toSender(S_InventoryDelete.clone(BasePacketPooling
								.getPool(S_InventoryDelete.class), i));
						break;
					}
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getName().equalsIgnoreCase("칼렉 풀기")) {
					if (!tempList.contains(i)) {
						tempList.add(i);
						o.toSender(S_InventoryDelete.clone(BasePacketPooling
								.getPool(S_InventoryDelete.class), i));
						break;
					}
				}
			}
			
			for (ItemInstance i : list) {
				if (i.getItem().getName().equalsIgnoreCase("자동 물약")) {
					if (!tempList.contains(i)) {
						tempList.add(i);
						o.toSender(S_InventoryDelete.clone(BasePacketPooling
								.getPool(S_InventoryDelete.class), i));
						break;
					}
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getName().equalsIgnoreCase("몬스터 정보")) {
					if (!tempList.contains(i)) {
						tempList.add(i);
						o.toSender(S_InventoryDelete.clone(BasePacketPooling
								.getPool(S_InventoryDelete.class), i));
						break;
					}
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getName().equalsIgnoreCase("아이템 정보")) {
					if (!tempList.contains(i)) {
						tempList.add(i);
						o.toSender(S_InventoryDelete.clone(BasePacketPooling
								.getPool(S_InventoryDelete.class), i));
						break;
					}
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getName().equalsIgnoreCase("아이템 제거 막대")) {
					if (!tempList.contains(i)) {
						tempList.add(i);
						o.toSender(S_InventoryDelete.clone(BasePacketPooling
								.getPool(S_InventoryDelete.class), i));
						break;
					}
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getName().equalsIgnoreCase("군주지원")) {
					if (!tempList.contains(i)) {
						tempList.add(i);
						o.toSender(S_InventoryDelete.clone(BasePacketPooling
								.getPool(S_InventoryDelete.class), i));
						break;
					}
				}
			}
			for (ItemInstance i : list) {
				if (i.getItem().getName().equalsIgnoreCase("기사지원")) {
					if (!tempList.contains(i)) {
						tempList.add(i);
						o.toSender(S_InventoryDelete.clone(BasePacketPooling
								.getPool(S_InventoryDelete.class), i));
						break;
					}
				}
			}
			for (ItemInstance i : list) {
				if (i.getItem().getName().equalsIgnoreCase("요정지원")) {
					if (!tempList.contains(i)) {
						tempList.add(i);
						o.toSender(S_InventoryDelete.clone(BasePacketPooling
								.getPool(S_InventoryDelete.class), i));
						break;
					}
				}
			}
			for (ItemInstance i : list) {
				if (i.getItem().getName().equalsIgnoreCase("법사지원")) {
					if (!tempList.contains(i)) {
						tempList.add(i);
						o.toSender(S_InventoryDelete.clone(BasePacketPooling
								.getPool(S_InventoryDelete.class), i));
						break;
					}
				}
			}
			// 착용중인 무기
			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("sword")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("tohandsword")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("claw")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}
			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("edoryu")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}
			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("dagger")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("bow")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}

			for (ItemInstance i : list) {
				if (i instanceof Arrow) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("wand")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}

			for (ItemInstance i : list) {
				if (i instanceof ItemWeaponInstance && i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}

			// 미착용 무기
			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("sword")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("tohandsword")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("bow")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("wand")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			for (ItemInstance i : list) {
				if (i instanceof ItemWeaponInstance) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 착용중인 방어구
			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("helm")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("t")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("armor")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("cloak")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("glove")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("shield")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("boot")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("necklace")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("ring")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("belt")
						&& i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}

			for (ItemInstance i : list) {
				if (i instanceof ItemArmorInstance && i.isEquipped()) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 미착용 방어구
			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("helm")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("t")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("armor")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("cloak")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("glove")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("shield")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("boot")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("necklace")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("ring")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("belt")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			for (ItemInstance i : list) {
				if (i instanceof ItemArmorInstance) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			for (ItemInstance i : list) {
				if (i.getItem().getName().equalsIgnoreCase("아데나")) {
					if (!tempList.contains(i)) {
						tempList.add(i);
						o.toSender(S_InventoryDelete.clone(BasePacketPooling
								.getPool(S_InventoryDelete.class), i));
						break;
					}
				}
			}
			for (ItemInstance i : list) {
				if (i.getItem().getName().equalsIgnoreCase("신비한 날개깃털")) {
					if (!tempList.contains(i)) {
						tempList.add(i);
						o.toSender(S_InventoryDelete.clone(BasePacketPooling
								.getPool(S_InventoryDelete.class), i));
						break;
					}
				}
			}
			// 물약
			for (ItemInstance i : list) {
				if (i instanceof HealingPotion) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 초록 물약
			for (ItemInstance i : list) {
				if (i instanceof HastePotion) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 용기 물약
			for (ItemInstance i : list) {
				if (i instanceof BraveryPotion) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 악마의 피
			for (ItemInstance i : list) {
				if (i instanceof BraveryPotion2) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 와퍼
			for (ItemInstance i : list) {
				if (i instanceof ElvenWafer) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 지혜 물약
			for (ItemInstance i : list) {
				if (i instanceof WisdomPotion) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 파란 물약
			for (ItemInstance i : list) {
				if (i instanceof BluePotion) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 해독제
			for (ItemInstance i : list) {
				if (i instanceof CurePoisonPotion) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			// 카오틱 물약
			for (ItemInstance i : list) {
				if (i instanceof LawfulPotion) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}
			// 변신 주문서
			for (ItemInstance i : list) {
				if (i instanceof ScrollPolymorph5) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 변신 주문서
			for (ItemInstance i : list) {
				if (i instanceof ScrollPolymorph) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 변신 주문서
			for (ItemInstance i : list) {
				if (i instanceof ScrollPolymorph3) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			for (ItemInstance i : list) {
				if (i.getItem().getName().equalsIgnoreCase("랭킹 변신 주문서")) {
					if (!tempList.contains(i)) {
						tempList.add(i);
						o.toSender(S_InventoryDelete.clone(BasePacketPooling
								.getPool(S_InventoryDelete.class), i));
						break;
					}
				}
			}
			// 순간이동 주문서
			for (ItemInstance i : list) {
				if (i instanceof ScrollLabeledVenzarBorgavve) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			for (ItemInstance i : list) {
				if (i.getItem().getName().equalsIgnoreCase("기란 마을 이동 부적")) {
					if (!tempList.contains(i)) {
						tempList.add(i);
						o.toSender(S_InventoryDelete.clone(BasePacketPooling
								.getPool(S_InventoryDelete.class), i));
						break;
					}
				}
			}
			// 귀환 주문서
			for (ItemInstance i : list) {
				if (i instanceof ScrollLabeledVerrYedHorae) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 혈맹 귀환 주문서
			for (ItemInstance i : list) {
				if (i instanceof ScrollLabeledVerrYedHoraePledgeHouse) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 이동 주문서 및 부적
			for (ItemInstance i : list) {
				if (i instanceof ScrollTeleport) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 확인 주문서
			for (ItemInstance i : list) {
				if (i instanceof ScrollLabeledKernodwel) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
					break;
				}
			}
			// 부활 주문서
			for (ItemInstance i : list) {
				if (i instanceof ScrollResurrection) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 저주 풀기 주문서
			for (ItemInstance i : list) {
				if (i instanceof ScrollLabeledPratyavayah) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 데이
			for (ItemInstance i : list) {
				if (i instanceof ScrollLabeledDaneFools) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 젤
			for (ItemInstance i : list) {
				if (i instanceof ScrollLabeledZelgoMer) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}

			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("change bress")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 오만의 탑 이동 주문서
			for (ItemInstance i : list) {
				if (i instanceof TOITeleportScroll) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("[giro] 전투강화")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("[giro] 체력증강")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("[giro] 마력증강")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("드래곤 루비")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("드래곤 다이아몬드")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("드래곤 에메랄드")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			for (ItemInstance i : list) {
				if (i.getItem().getType2().equalsIgnoreCase("드래곤 사파이어")) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 마법인형
			for (ItemInstance i : list) {
				if (i instanceof MagicDoll) {
					if (!tempList.contains(i))
						tempList.add(i);
					o.toSender(S_InventoryDelete.clone(
							BasePacketPooling.getPool(S_InventoryDelete.class),
							i));
				}
			}
			// 마법서류
			for (ItemInstance i : list) {
				if (i != null
						&& i.getItem() != null
						&& (i.getItem().getName().contains("마법서")
								|| i.getItem().getName().contains("기술서") || i
								.getItem().getName().contains("정령의 수정"))) {
					if (!tempList.contains(i))
						tempList.add(i);
				}
			}
			// 기타 아이템
			for (ItemInstance i : list) {
				if (!tempList.contains(i))
					tempList.add(i);
			}
			// 막대
			for (ItemInstance i : list) {
				if (i != null
						&& i.getItem() != null
						&& (i.getItem().getName().contains("단풍나무 막대")
								|| i.getItem().getName().contains("소나무 막대") || i
								.getItem().getName().contains("흑단 막대"))) {
					if (!tempList.contains(i))
						tempList.add(i);
				}
			}
			for (ItemInstance i : tempList)
				o.toSender(S_InventoryDelete.clone(
						BasePacketPooling.getPool(S_InventoryDelete.class), i));

			for (ItemInstance i : tempList)
				o.toSender(S_InventoryAdd.clone(
						BasePacketPooling.getPool(S_InventoryAdd.class), i));
		} catch (Exception e) {
		}
	}

	// f1 자동칼질
	static public void 자동칼질(object o, boolean on) {
		try {
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			
			if (pc.getInventory() != null) {
				if (pc.isDead()) {
					ChattingController.toChatting(pc, "죽은 상태에선 사용할 수 없습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
					return;
				} else if (pc.isLock()) {
					ChattingController.toChatting(pc, "마비 상태에선 사용할 수 없습니다.",
							Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if (o.isAutoAttack)
					o.isAutoAttack = false;
				else
					o.isAutoAttack = true;

			}
			ChattingController.toChatting(pc, String.format("자동칼질: %s",
					pc.isAutoAttack ? "활성화" : "비활성화"),
					Lineage.CHATTING_MODE_MESSAGE);
			}
		} catch (Exception e) {
		}
		}

	// f1 마크
	static public void 마크(object o, boolean on) {
		if (!isCommandDelay(o, null, 10))
			return;
		Clan clan = ClanController.find((PcInstance) o);
		try {
		if (clan == null) {
			ChattingController.toChatting(o, "혈맹에 가입해야 사용할 수 있습니다.",
					Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (clan.getName().equalsIgnoreCase(Lineage.new_clan_name)) {
			ChattingController.toChatting(o, "신규혈맹은 사용할 수 없습니다.",
					Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (o.isMark) {
			o.isMark = false;

			for (Clan c : ClanController.getClanList().values()) {
				if (c != null
						&& !c.getName().equals(clan.getName())
						&& !c.getName().equalsIgnoreCase(Lineage.new_clan_name)) {
					o.toSender(S_ClanWar.clone(
							BasePacketPooling.getPool(S_ClanWar.class), 3,
							clan.getName(), c.getName()));
				}
			}
		} else {
			o.isMark = true;

			for (Clan c : ClanController.getClanList().values()) {
				if (c != null
						&& !c.getName().equals(clan.getName())
						&& !c.getName().equalsIgnoreCase(Lineage.new_clan_name)) {
					o.toSender(S_ClanWar.clone(
							BasePacketPooling.getPool(S_ClanWar.class), 1,
							clan.getName(), c.getName()));
				}
			}
		}
		} catch (Exception e) {
			ChattingController.toChatting(o, "혈맹에 가입해야 사용할 수 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
	
	// f1 파티드랍메세지
	static public void 파티드랍메세지(object o, boolean on) {
		PcInstance pc = (PcInstance) o;
		try {
		if (on) {
			pc.setAuto_party_message(true);
		} else {
			pc.setAuto_party_message(false);
		}

		ChattingController.toChatting(o, String.format("파티 드랍 메세지: %s활성화",
				pc.isAuto_party_message() ? "" : "비"),
				Lineage.CHATTING_MODE_MESSAGE);
		    } catch (Exception e) {
		        // 기타 예외 처리 또는 더 일반적인 메시지 제공
		        e.printStackTrace(); // 또는 에러 로그 남기기
		    }
		}
	/**
	 * 서버 정보
	 * 2017-10-12
	 * by all-night
	 */
	static public void serverInfo(object o, StringTokenizer st) {
		List<String> info = new ArrayList<String>();

		info.clear();
		// 서버명
		info.add(String.valueOf(Lineage.server_name));
		// 운영자 ID
		info.add(String.valueOf(Lineage.master_name));

		// 레벨 제한
		info.add(String.valueOf(Lineage.level_max));
		// 서버 최고 레벨
		info.add(String.valueOf(RankController.rank_top_level));
		// 엘릭서 복용 제한
		info.add(String.valueOf(Lineage.item_elixir_max));
		// 무기 인챈트 제한
		info.add(String.valueOf(Lineage.item_enchant_weapon_max));
		// 방어구 인챈트 제한
		info.add(String.valueOf(Lineage.item_enchant_armor_max));
		// 장신구 인챈트 제한
		info.add(String.valueOf(Lineage.item_acc_en));
		// 혈맹 최대 가입 인원
		info.add(String.valueOf(Lineage.clan_max));
		// 파티 추가 경험치
		info.add(String.format("%.1f", Lineage.rate_party));
		// 랭킹 업데이트 시간
		info.add(String.valueOf(Lineage.board_rank_update_delay / 3600000));
		// 랭킹변신 레벨
		info.add(String.valueOf(Lineage.rank_min_level2));
		// 랭킹변신 순위
		info.add(String.valueOf(Lineage.rank_poly_all));
		info.add(String.valueOf(Lineage.rank_poly_class));

		o.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), o, "serverInfo", null, info));
	}
	
	/**
	 * 월드 종료시 호출됨. : PcInstance 에서만 호출중.
	 * 
	 * @param o
	 */
	static public void toWorldOut(object o) {

	}
}