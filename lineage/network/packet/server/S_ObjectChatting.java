package lineage.network.packet.server;

import lineage.bean.database.Wanted;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
//import lineage.world.controller.PvpController;
import lineage.world.controller.RankController;
import lineage.world.controller.WantedController;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class S_ObjectChatting extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, object o,
			int mode, String msg) {
		if (bp == null)
			bp = new S_ObjectChatting(o, mode, msg);
		else
			((S_ObjectChatting) bp).clone(o, mode, msg);
		return bp;
	}

	static synchronized public BasePacket clone(BasePacket bp, String msg) {
		if (bp == null)
			bp = new S_ObjectChatting(null, 0x14, msg);
		else
			((S_ObjectChatting) bp).clone(null, 0x14, msg);
		return bp;
	}

	public S_ObjectChatting(object o, int mode, String msg) {
		clone(o, mode, msg);
	}
	public S_ObjectChatting(String msg) {
		clone(null,null,0x14,msg);
	}

	public void clone(object o, int mode, String msg) {
		clear();
		boolean ranker = false;
		int rank = 0;
		StringBuffer text = new StringBuffer();
		try {
			// 랭킹 별 표시를 위해서 랭킹 추출
			if (o != null && o instanceof PcInstance && mode == Lineage.CHATTING_MODE_GLOBAL) {
				rank = RankController.getRankAll(o);

				if (rank < 1)
					rank = o.lastRank;

				if (rank > 0 && rank <= Lineage.rank_class_1 && o.getLevel() >= Lineage.rank_min_level)
					ranker = true;
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : 랭커 채팅 오류\r\n", S_ObjectChatting.class.toString());
			lineage.share.System.println(e);
		}
		switch (mode) {
/*		case 0:
			String name = o.getName();

			if (o instanceof PcInstance) {
				// 수배중 체크
				boolean wanted = false;

				for (Wanted w : WantedController.getList()) {
					if (w.target_name.equalsIgnoreCase(name)) {
						wanted = true;
						break;
					}
				}

				if (wanted)
					name = "" + name;
			}

			text.append(name);
			text.append(": ");
			text.append(msg);
			normal(o, mode, text.toString());
			break; */
		case 0:
			text.append(o.getName());
			text.append(": ");
			text.append(msg);
			normal(o, mode, text.toString());
			break;
		case 2:
			if (o == null || o.getName().equals("   \\f:[안내원]") || o.getName().equals("    \\f:[투기장 도우미]") || o.getName().equals("    \\f:[투기장 표판매원]")) {
				text.append(o.getTitle());
				text.append(": ");
			} else {
				text.append(o.getName());
				text.append(": ");
			}
			text.append(msg);
			shotsay(o, text.toString());
			break;
		case 3:
			if (o == null || o.getGm() > 0) {
				text.append("[******] ");
			} else {
				String chaName = "[" + o.getName() + "] ";

				// 랭킹 별 추가
				if (ranker) {
					if (rank <= Lineage.rank_class_4)
						msg = "\\d7" + msg;
					else if (rank <= Lineage.rank_class_3)
						msg = "\\d6" + msg;
					else if (rank <= Lineage.rank_class_2)
						msg = "\\d5" + msg;
					else
						msg = "\\d4" + msg;

					if (msg.contains("\\d4"))
						chaName = " " + chaName;

					if (msg.contains("\\d5"))
						chaName = " " + chaName;

					if (msg.contains("\\d6"))
						chaName = "  " + chaName;
					
					if (msg.contains("\\d7"))
						chaName = "   " + chaName;
				}

				text.append(chaName);
			}
			text.append(msg);
			global(o, mode, text.toString());
			break;
		case 4:
			Object object = PluginController.init(S_ObjectChatting.class, "CHATTING_MODE_CLAN", o, msg);
			if (object == null) {
				text.append("{");
				text.append(o.getName());
				text.append("} ");
				text.append(msg);
			} else {
				text.append((String) object);
			}
			clan(o, mode, text.toString());
			break;
		case 0x08: // 귓속말 - 받는사람
			whisperReceiver(o, mode, msg);
			break;
		case 0x09: // 귓속말 - 보낸사람
			text.append("-> (");
			text.append(o == null ? "******" : o.getName());
			text.append(") ");
			text.append(msg);
			whisperSender(o, mode, text.toString());
			break;
		case 11:
			if(o != null){
				text.append("(");
				text.append(o.getName());
				text.append(") ");
			}
			text.append(msg);
			party(o, mode, text.toString());
			break;
		case 12:
			if (Lineage.server_version <= 200)
				text.append("\\fR");
			text.append("[");
			text.append(o.getName());
			text.append("] ");
			text.append(msg);
			trade(mode, text.toString());
			break;
		case 13: // 혈맹 수호기사 채팅 %
			text.append("{{");
			text.append(o.getName());
			text.append("}} ");
			text.append(msg);
			clan(o, 4, text.toString());
			break;
		case 0x0E: // 채팅파티 채팅 *
			break;
		case 0x14:
			message(0x09, msg);
			break;
		case 0x15:
			text.append(o.getName());
			text.append(": ");
			text.append(msg);
			hideMessage(o, mode, text.toString());
			break;
		case 100:
			text.append(msg);
			global(o, mode, text.toString());
			break;
		}
	}

	private void trade(int mode, String msg) {
		writeC(Opcodes.S_OPCODE_GLOBALCHAT);
		writeC(mode);
		writeS(msg);
	}

	/**
	 * 일반 채팅
	 */
	private void normal(object o, int mode, String msg) {
		if (o instanceof PcInstance)
			writeC(Opcodes.S_OPCODE_NORMALCHAT);
		else
			writeC(Opcodes.S_OPCODE_SHOUTSAY);
		writeC(mode);
		writeD(o.getObjectId());
		writeS(msg);
		writeH(o.getX());
		writeH(o.getY());
	}

	/**
	 * 전체 채팅
	 */
	private void global(object o, int mode, String msg) {
		writeC(Opcodes.S_OPCODE_GLOBALCHAT);
		writeC(mode);
		writeS(msg);
	}

	/**
	 * 혈맹 채팅
	 */
	private void clan(object o, int mode, String msg) {
		writeC(Opcodes.S_OPCODE_GLOBALCHAT);
		writeC(mode);
		writeS(msg);
	}

	/**
	 * 귓속말
	 */
	private void whisperReceiver(object o, int mode, String msg) {
		writeC(Opcodes.S_OPCODE_WHISPERCHAT);
		writeS(o == null ? "******" : o.getName());
		writeS(msg);
	}

	/**
	 * 파티 채팅
	 */
	private void party(object o, int mode, String msg) {
		writeC(Opcodes.S_OPCODE_GLOBALCHAT);
		writeC(mode);
		writeS(msg);
	}

	/**
	 * 귓속말
	 */
	private void whisperSender(object o, int mode, String msg) {
		
		
		if(o == null || o.getGm() >0 || o.getName().equals("메티스") || o.getName().equals("미소피아") || o.getName().equals("운영자")){
			o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, Lineage.whisper_effect));
		
		}
		writeC(Opcodes.S_OPCODE_GLOBALCHAT);
		writeC(mode);
		writeS(msg);
	}

	/**
	 * 일반 텍스트 표현
	 */
	private void message(int mode, String msg) {
		writeC(Opcodes.S_OPCODE_GLOBALCHAT);
		writeC(mode);
		writeS(msg);
	}

	private void shotsay(object o, String msg) {
		writeC(Opcodes.S_OPCODE_SHOUTSAY);
		writeC(0x02); // 0x00:일반채팅색상, 0x02:외치기색상
		if (o != null)
			writeD(o.getObjectId());
		else
			writeD(0);
		writeS(msg);
	}

	private void hideMessage(object o, int mode, String msg) {
		writeC(Opcodes.S_OPCODE_SHOUTSAY);
		writeC(mode);
		writeD(o.getObjectId());
		writeS(msg);
		writeH(o.getX());
		writeH(o.getY());
	}
}
