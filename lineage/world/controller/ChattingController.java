package lineage.world.controller;

import jsn_soft.AutoHuntController;
import jsn_soft.Swap_Plugin;
import lineage.bean.event.SpServerChatting;
import lineage.bean.lineage.Clan;
import lineage.bean.lineage.Party;
import lineage.database.AccountDatabase;
import lineage.database.BanWordDatabase;
import lineage.gui.GuiMain;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_SoundEffect;
import lineage.plugin.PluginController;
import lineage.share.Common;
import lineage.share.Lineage;
import lineage.share.Log;
import lineage.share.TimeLine;
import lineage.thread.EventThread;
import lineage.world.World;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.NpcInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestInstance;
import lineage.world.object.instance.RobotClanInstance;
import lineage.world.object.instance.RobotInstance;
import lineage.world.object.robot.PcRobotInstance;

public final class ChattingController {

	static private boolean global;

	static public void init() {
		TimeLine.start("ChattingController..");

		global = true;

		TimeLine.end();
	}

	static public void toWorldJoin(PcInstance pc) {

	}

	static public void toWorldOut(PcInstance pc) {

	}

	static public void setGlobal(boolean g) {
		global = g;
	}

	static public boolean isGlobal() {
		return global;
	}

	static public void toChatting(object o, String msg) {
		toChatting(o, msg, 20);
	}

	/**
	 * 채팅 처리 함수.
	 * 
	 * @param o
	 * @param msg
	 * @param mod
	 */
	static public void toChatting(object o, String msg, int mode, Object... opt) {
		if(o!=null && o.isBuffChattingClose() && mode!=Lineage.CHATTING_MODE_MESSAGE && mode != Lineage.CHATTING_MODE_NORMAL){
			// 현재 채팅 금지중입니다.
			o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 242));
			return;
		}
		// 사일런스 상태는 무시.
		if (o != null && o.isBuffSilence())
			return;

		// 금지어 체크
		if (o instanceof PcInstance) {
			switch (mode) {
			case 0:
			case 2:
			case 3:
			case 4:
			case 12:
			case 11:
				if (BanWordDatabase.getInstance().checkMsg(msg) && o.getGm() == 0) {
					PcInstance pc = (PcInstance) o;
					// msg ="";
					// toMessage(o, "\\fY금칙어를 사용 하였으므로 "+Lineage.ChatTime+"(분)의
					// 채금을 주었습니다.");
					// ChattingClose.init(pc, Lineage.ChatTime);
					// return;
				}
			}
		}

		// 모든채팅락 무시.
		if (o != null && o.getGm() == 0 && Lineage.chatting_all_lock)
			return;
		// 랭킹 별표 표시를 위해 화살표 나타내는 명령어 제거
				if (msg.contains("\\d0") || msg.contains("\\d1") || msg.contains("\\d2") || msg.contains("\\d3")
						|| msg.contains("\\d4") || msg.contains("\\d5") || msg.contains("\\d6") || msg.contains("\\d7")) {

					if (msg.contains("\\d0"))
						msg = msg.replace("\\d0", "");

					if (msg.contains("\\d1"))
						msg = msg.replace("\\d1", "");

					if (msg.contains("\\d2"))
						msg = msg.replace("\\d2", "");

					if (msg.contains("\\d3"))
						msg = msg.replace("\\d3", "");

					if (msg.contains("\\d4"))
						msg = msg.replace("\\d4", "");

					if (msg.contains("\\d5"))
						msg = msg.replace("\\d5", "");

					if (msg.contains("\\d6"))
						msg = msg.replace("\\d6", "");

					if (msg.contains("\\d6"))
						msg = msg.replace("\\d7", "");
				}
		// System.out.println(mode);
		// 채팅 처리
		switch (mode) {
		case 0:
			toNormal(o, msg);
			break;
		case 2:

			toShout(o, msg);
			break;
		case 3:
			// 전체채팅락 무시.
			if (o != null && o.getGm() == 0 && Lineage.chatting_global_lock)
				return;
			toGlobal(o, msg);
			break;
		case 4:
			toClan(o, msg);
			break;
		case 11:
			toParty(o, msg);
			break;
		case 12:
			toTrade(o, msg);
			
			break;
		// case 13:
		// toClanSafe(o, msg);
		// break;
		case 20:

			toMessage(o, msg);
			break;
		case 100:
			// 전체채팅락 무시.
			if (Lineage.chatting_global_lock)
				return;
			toGlobal(o, msg);
			break;
		default:
			System.out.println(mode);
			break;
		}

		// 로그 기록 (시스템메세지는 기록 안함.)
		if (mode != 20 && (o == null || o instanceof PcInstance)) {
			if (Log.isLog(o))
				Log.appendChatting(o == null ? null : (PcInstance) o, msg, mode);
		}
	}

	/**
	 * 귓속말 처리 함수.
	 * 
	 * @param o
	 * @param name
	 * @param msg
	 */
	static public void toWhisper(final object o, final String name, final String msg) {

		if (msg.startsWith("\\d")) {
			return;
		}
		//
		if (PluginController.init(ChattingController.class, "toWhisper", o, name, msg) != null)
			return;
		if (o != null && o.isBuffChattingClosetwo()) {
			// 현재 채팅 금지중입니다.
			toMessage(o, "\\fY상대방을 타격 하였을 경우 " + Lineage.ChatTimetwo + "(초)의 일반 채팅 금지(외창방지).");
			return;
		}

		//
		if (PluginController.init(ChattingController.class, "toWhisper", o, name, msg) != null)
			return;
		if (o != null && o.isBuffChattingClose()) {
			// 현재 채팅 금지중입니다.
			o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 242));
			return;
		}
		// 사일런스 상태는 무시.
		if (o != null && o.isBuffSilence())
			return;

		if (Lineage.server_version <= 144 || o == null || o.getLevel() >= Lineage.chatting_level_whisper) {
			boolean gui_admin = name.equalsIgnoreCase("******");
			PcInstance user = World.findPc(name);
			if (user == null)
				user = World.findRobot(name);
			if (user != null || gui_admin) {
				if (gui_admin || o == null || user.isChattingWhisper()) {
					if (gui_admin || o == null
							|| (user.getListBlockName() != null && !user.getListBlockName().contains(o.getName()))) {
						if (o != null) {
							o.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), user,
									0x09, msg));

						}
						if (user != null) {
							if (user.getGm() > 0) {
								user.toSender(
										S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 7700));
								user.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),
										o, 0x08, msg));

							} else {
								user.toSender(
										S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 5374));
								user.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),
										o, 0x08, msg));
							}

						}
						// gui 처리.
						if (Common.system_config_console == false) {
							GuiMain.display.asyncExec(new Runnable() {
								@Override
								public void run() {
									GuiMain.getViewComposite().getChattingComposite().toWhisper(o, name, msg);
								}
							});
						}
						// 로그 기록
						if (Log.isLog(user))
							Log.appendChatting(user, msg, 9, name);
					} else {
						// \f1%0%d 현재 귓말을 듣고 있지 않습니다.
						o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 205, name));
					}
				} else {
					if (o != null)
						// \f1%0%d 현재 귓말을 듣고 있지 않습니다.
						o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 205, name));
				}
			} else {
				if (o != null) {
					// \f1%0%d 게임을 하고 있지 않습니다.
					o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 73, name));
				} else {
					// gui 처리.
					if (Common.system_config_console == false) {
						GuiMain.display.asyncExec(new Runnable() {
							@Override
							public void run() {
								GuiMain.getViewComposite().getChattingComposite()
										.toMessage(String.format("%s 게임을 하고 있지 않습니다.", name));
							}
						});
					}
				}
			}
		} else {
			if (o != null)
				o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 404,
						String.valueOf(Lineage.chatting_level_whisper)));
		}
	}

	/**
	 * 일반 채팅 처리.
	 * 
	 * @param o
	 * @param msg
	 */
	static private void toNormal(final object o, final String msg) {
		if (msg.startsWith("\\d")) {
			return;
		}
		if (o != null && o instanceof PcInstance
				&& PluginController.init(ChattingController.class, "toAutoHuntAnswer", o, msg) != null)
			return;
		if (PluginController.init(ChattingController.class, "toNormal", o, msg) != null)
			return;
		// 자동사냥 방지 답변 확인.

		if (o != null && o.isBuffChattingClosetwo()) {
			// 현재 채팅 금지중입니다.
			toMessage(o, "\\fY상대방을 타격 하였을 경우 " + Lineage.ChatTimetwo + "(초)의 일반 채팅 금지(외창방지).");
			return;
		}

		// 이름변경 확인 처리.
		if (o.getInventory() != null && o.getInventory().changeName != null
				&& o.getInventory().value(o.getInventory().changeName.getObjectId()) != null) {
			o.getInventory().changeName.toClickFinal((Character) o, msg);
			o.getInventory().changeName = null;
			return;
		}
		// 명령어 확인 처리.
		if (!CommandController.toCommand(o, msg) && !AutoHuntController.toChatting(o, msg)
				&& !Swap_Plugin.toChatting(o, msg)) {

			if (o instanceof PcInstance && !(o instanceof PcRobotInstance)) {
				tocommand(o, msg);
			}

			if (o != null && o.isBuffChattingClose()) {
				// 현재 채팅 금지중입니다.
				o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 242));
				return;
			}

			if (o instanceof PcInstance) {
				// 나에게 표현.
				if (o.getGm() > 0 || Lineage.chatting_level_normal <= o.getLevel())
					o.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), o, 0, msg));
				else
					o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 195,
							String.valueOf(Lineage.chatting_level_normal)));
				// 주변 객체에게 알리기. npc, monster, robot만.
				for (object oo : o.getInsideList(true)) {
					if (oo instanceof NpcInstance || oo instanceof MonsterInstance || oo instanceof RobotInstance)
						oo.toChatting(o, msg);
				}
			}
			// 주변사용자에게 표현.
			for(object oo : o.getInsideList()){
				if(oo instanceof PcInstance){
					PcInstance use = (PcInstance)oo;
					// 블럭 안된 이름만 표현하기.
					if(use.getListBlockName().contains(o.getName()) == false)
						use.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), o, Lineage.CHATTING_MODE_NORMAL, msg));
				}
			}
			// gui 처리.
			if(Common.system_config_console==false && !(o instanceof RobotInstance) && o instanceof PcInstance){
				GuiMain.display.asyncExec(new Runnable(){
					@Override
					public void run(){
						GuiMain.getViewComposite().getChattingComposite().toNormal(o, msg);
					}
				});
			}
		}
	}

	/**
	 * 노말 채팅을 이용한 커맨드 처리.
	 * 
	 * @param o
	 * @param msg
	 */
	static private void tocommand(object o, String msg) {

		PcInstance pcc = (PcInstance) o;

		RobotClanInstance cr = ClanLordController.findCr(pcc.getObjectId());

		if (cr != null) {
			if (pcc.getClan_shadow_step() == 1) {
				int level = Integer.parseInt(msg);
				cr.setJoin_level(level);
				ChattingController.toChatting(pcc, String.format("%d레벨로 혈맹가입 최소레벨 지정되었습니다.", level),
						Lineage.CHATTING_MODE_MESSAGE);
				ChattingController.toChatting(pcc, "\\fT군주 가입가능 여부 예(1) or 아니오(0)?", Lineage.CHATTING_MODE_MESSAGE);
				pcc.setClan_shadow_step(2);
				return;
			}

			if (pcc.getClan_shadow_step() == 2) {
				int royal = Integer.parseInt(msg);
				if (royal != 0 && royal != 1) {
					ChattingController.toChatting(pcc, "\\fU예(1) or 아니오(0) 중에 골라주세요", Lineage.CHATTING_MODE_MESSAGE);
					pcc.setClan_shadow_step(2);
				}
				cr.setRoyal_true(royal);
				ChattingController.toChatting(pcc, String.format("군주 가입여부 [%s]", royal == 1 ? "가입가능" : "가입불가"),
						Lineage.CHATTING_MODE_MESSAGE);
				ChattingController.toChatting(pcc, "\\fT기사 가입가능 여부 예(1) or 아니오(0)?", Lineage.CHATTING_MODE_MESSAGE);
				pcc.setClan_shadow_step(3);
				return;
			}

			if (pcc.getClan_shadow_step() == 3) {
				int knight = Integer.parseInt(msg);
				if (knight != 0 && knight != 1) {
					ChattingController.toChatting(pcc, "\\fU예(1) or 아니오(0) 중에 골라주세요", Lineage.CHATTING_MODE_MESSAGE);
					pcc.setClan_shadow_step(3);
				}
				cr.setKnight_true(knight);
				ChattingController.toChatting(pcc, String.format("기사 가입여부 [%s]", knight == 1 ? "가입가능" : "가입불가"),
						Lineage.CHATTING_MODE_MESSAGE);
				ChattingController.toChatting(pcc, "\\fT요정 가입가능 여부 예(1) or 아니오(0)?", Lineage.CHATTING_MODE_MESSAGE);
				pcc.setClan_shadow_step(4);
				return;
			}

			if (pcc.getClan_shadow_step() == 4) {
				int elf = Integer.parseInt(msg);
				if (elf != 0 && elf != 1) {
					ChattingController.toChatting(pcc, "\\fU예(1) or 아니오(0) 중에 골라주세요", Lineage.CHATTING_MODE_MESSAGE);
					pcc.setClan_shadow_step(4);
				}
				cr.setElf_true(elf);
				ChattingController.toChatting(pcc, String.format("요정 가입여부 [%s]", elf == 1 ? "가입가능" : "가입불가"),
						Lineage.CHATTING_MODE_MESSAGE);
				ChattingController.toChatting(pcc, "\\fT마법사 가입가능 여부 예(1) or 아니오(0)?", Lineage.CHATTING_MODE_MESSAGE);
				pcc.setClan_shadow_step(5);
				return;
			}

			if (pcc.getClan_shadow_step() == 5) {
				int wizard = Integer.parseInt(msg);
				if (wizard != 0 && wizard != 1) {
					ChattingController.toChatting(pcc, "\\fU예(1) or 아니오(0) 중에 골라주세요", Lineage.CHATTING_MODE_MESSAGE);
					pcc.setClan_shadow_step(5);
				}
				cr.setWizard_true(wizard);
				pcc.setClan_shadow_step(100);
				ChattingController.toChatting(pcc, String.format("마법사 가입여부 [%s]", wizard == 1 ? "가입가능" : "가입불가"),
						Lineage.CHATTING_MODE_MESSAGE);
				ChattingController.toChatting(pcc, "\\fR무인군주 설정 완료", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
		}
	}

	/**
	 * 외치기 처리.
	 * 
	 * @param o
	 * @param msg
	 */
	static private void toShout(object o, String msg) {
		if (msg.startsWith("\\d")) {
			return;
		}
		//
		if (PluginController.init(ChattingController.class, "toGlobal", o, msg) != null)
			return;
		if (o != null && o.isBuffChattingClosetwo()) {
			// 현재 채팅 금지중입니다.
			toMessage(o, "\\fY상대방을 타격 하였을 경우 " + Lineage.ChatTimetwo + "(초)의 채팅 금지(외창방지");
			return;
		}
		if (o != null && o.isBuffChattingClose()) {
			// 현재 채팅 금지중입니다.
			o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 242));
			return;
		}
		// 나에게 표현.
		if (o instanceof PcInstance)
			o.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), o, 2, msg));
		// 주변사용자에게 표현.
		for (object oo : o.getAllList(true)) {
			if (oo instanceof PcInstance) {
				PcInstance use = (PcInstance) oo;
				// 블럭 안된 이름만 표현하기.
				if (o instanceof QuestInstance || !use.getListBlockName().contains(o.getName()))
					use.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), o, 2, msg));
			}
		}
	}

	/**
	 * 전체채팅 처리.
	 * @param o
	 * @param msg
	 */
	static private void toGlobal(final object o, final String msg) {
		if (Lineage.is_gm_global_chat && (o == null || o.getGm() > 0)) {
			for (PcInstance pc : World.getPcList()) {
				pc.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), o, Lineage.CHATTING_MODE_MESSAGE, String.format("\\fY[******] %s", msg)));
			}
			
			// gui 처리.
			if (Common.system_config_console == false && !(o instanceof RobotInstance)) {
				GuiMain.display.asyncExec(new Runnable() {
					@Override
					public void run() {
						GuiMain.getViewComposite().getChattingComposite().toGlobal(o, msg);
					}
				});
			}
			return;
		}
		
		// 처리해도되는지 확인.
		if (!global && o instanceof PcInstance)
			return;

		if (o == null || o.getGm() > 0 || Lineage.chatting_level_global <= o.getLevel()) {
			for (PcInstance use : World.getPcList()) {
				if (o == null || use.isChattingGlobal() && !use.getListBlockName().contains(o.getName())) {
					if (Lineage.is_chatting_clan) {
						use.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), o, Lineage.CHATTING_MODE_GLOBAL, msg));
					} else {
						// 자신이 운영자일 경우
						// 상대방이 무혈일 경우
						// 상대방이 중립혈맹일 경우
						// 상대방이 자신과 같은 혈맹일 경우
						if (o == null || ((o.getGm() > 0 || o.getClanId() == 0 || o.getClanName().equalsIgnoreCase(Lineage.new_clan_name) || o.getClanId() == use.getClanId()
								|| use.getGm() > 0 || use.getClanId() == 0 || use.getClanName().equalsIgnoreCase(Lineage.new_clan_name))))
							use.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), o, Lineage.CHATTING_MODE_GLOBAL, msg));
						else
							use.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), o, Lineage.CHATTING_MODE_GLOBAL, "!@#$%^&*"));
					}
				}
			}
			// gui 처리.
			if (Common.system_config_console == false && !(o instanceof RobotInstance)) {
				GuiMain.display.asyncExec(new Runnable() {
					@Override
					public void run() {
						GuiMain.getViewComposite().getChattingComposite().toGlobal(o, msg);
					}
				});
			}
		} else {
			o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 195, String.valueOf(Lineage.chatting_level_global)));
		}
	}

	/**
	 * 혈맹채팅 처리.
	 * 
	 * @param o
	 * @param msg
	 */
	static private void toClan(final object o, final String msg) {
		if (msg.startsWith("\\d")) {
			return;
		}
		Clan c = ClanController.find(o.getClanName());
		if (c != null) {
			c.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), o, 4, msg));
			// gui 처리.
			if (Common.system_config_console == false && !(o instanceof RobotInstance)) {
				GuiMain.display.asyncExec(new Runnable() {
					@Override
					public void run() {
						GuiMain.getViewComposite().getChattingComposite().toClan(o, msg);
					}
				});
			}
		}
	}

	// /**
	// * 수호기사 채팅 처리.
	// * @param o
	// * @param msg
	// */
	// static private void toClanSafe(final object o, final String msg) {
	// if(msg.startsWith("\\d")){
	// return;
	// }
	// if(o.getMap() == 208){
	// ChattingController.toChatting(o, "[서버알림] 배틀존에서는 일반채팅만 가능합니다.", 20);
	// return;
	// }
	// Clan c = ClanController.find(o.getClanId());
	// if (c!=null && c.isSafeChatting((PcInstance)o)) {
	// for(PcInstance use : c.getList()) {
	// if(c.isSafeChatting(use))
	// use.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),
	// o, 13, msg));
	// }
	// // gui 처리.
	// if (Common.system_config_console == false && !(o instanceof
	// RobotInstance)) {
	// GuiMain.display.asyncExec(new Runnable() {
	// @Override
	// public void run() {
	// GuiMain.getViewComposite().getChattingComposite().toClanSafe(o, msg);
	// }
	// });
	// }
	// }
	// }

	/**
	 * 파티채팅 처리.
	 * 
	 * @param o
	 * @param msg
	 */
	static private void toParty(final object o, final String msg) {
		if (msg.startsWith("\\d")) {
			return;
		}
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			Party p = PartyController.find2(pc);
			if (p != null) {
				p.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), pc, 11, msg));
				// gui 처리.
				if (Common.system_config_console == false && !(o instanceof RobotInstance)) {
					GuiMain.display.asyncExec(new Runnable() {
						@Override
						public void run() {
							GuiMain.getViewComposite().getChattingComposite().toParty(o, msg);
						}
					});
				}
			}
		}
	}

	/**
	 * 장사채팅 처리.
	 * 
	 * @param o
	 * @param msg
	 */
	static private void toTrade(final object o, final String msg) {
		if (msg.startsWith("\\d")) {
			return;
		}
		if (o != null && o.isBuffChattingClose()) {
			// 현재 채팅 금지중입니다.
			o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 242));
			return;
		}
		// 처리해도되는지 확인.
		// if (!global && o instanceof PcInstance && o.getGm() == 0) {
		// ChattingController.toChatting(o, "운영자가 채팅창을 잠시 얼렸습니다",
		// 20);
		// return;
		// }

		if (o.getGm() > 0 || Lineage.chatting_level_trade <= o.getLevel()) {
			for (PcInstance use : World.getPcList()) {
		
					use.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), o,
							Lineage.CHATTING_MODE_TRADE, msg));
		
			}
			// gui 처리.
			if (Common.system_config_console == false && !(o instanceof RobotInstance)) {
				GuiMain.display.asyncExec(new Runnable() {
					@Override
					public void run() {
						GuiMain.getViewComposite().getChattingComposite().toTrade(o, msg);
					}
				});
			}
		} else {
			o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 195,
					String.valueOf(Lineage.chatting_level_global)));
		}
	}

	/**
	 * 일반 메세지 표현용.
	 * 
	 * @param o
	 * @param msg
	 */
	static private void toMessage(object o, final String msg) {
		if (msg.startsWith("\\d")) {
			return;
		}
		if (o == null) {
			if (Common.system_config_console == false) {
				GuiMain.display.asyncExec(new Runnable() {
					@Override
					public void run() {
						GuiMain.getViewComposite().getChattingComposite().toMessage(msg);
					}
				});
			}
		} else {
			o.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), o, 20, msg));
		}
	}

	public static void toWorldMessage(String paramString) {
		for (PcInstance localPcInstance : World.getPcList()) {
			localPcInstance.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),
					localPcInstance, 20, paramString));
		}
	}

}
