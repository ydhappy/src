package lineage.network.packet.client;

import java.text.DecimalFormat;

import lineage.database.CharactersDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.PvpController;
import lineage.world.object.instance.PcInstance;

public class C_ObjectWho extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_ObjectWho(data, length);
		else
			((C_ObjectWho)bp).clone(data, length);
		return bp;
	}
	
	public C_ObjectWho(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		// 버그 방지.
		if(pc==null || pc.isWorldDelete())
			return this;
		
		if(PluginController.init(C_ObjectWho.class, "init", pc, this) != null)
			return this;
		
		// 접속된 전체유저 출력.
		if(pc.getGm()>1){
			pc.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "+------------------------------"));
			for(PcInstance use : World.getPcList())
				pc.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("| Lv.%2d %s", use.getLevel(), use.getName())));
			pc.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "+------------------------------"));
		}
		// 사용자 이름 찾기.
		String name = readS();
		PcInstance use = World.findPc(name);
		if(use == null)
			use = World.findRobot(name);
		if(use != null) {
			
			String msg = Lineage.object_who;
			String msg2 = Lineage.object_who2;
			//  title  name lawful clan kill(pvpKill) Dead(pvpDead) 승률(pvpRate) class PKmap
			
			msg = msg.replace("wanted", use.getWanted()==false ? "":"(수배중)");
			// 이름
			msg = msg.replace("name", use.getName());
			// 호칭
			msg = msg.replace("title", use.getTitle()==null ? "" : use.getTitle());
			// 혈맹
			msg = msg.replace("clan", use.getClanId()==0 ? "" : String.format("%s]", use.getClanName()));
			// 피케이
			msg = msg.replace("pkcount", String.valueOf(use.getPkCount()));
			// 라우풀
			if(use.getLawful() < Lineage.NEUTRAL)
				msg = msg.replace("lawful", "(Chaotic)");
			else if(use.getLawful()>=Lineage.NEUTRAL && use.getLawful()<Lineage.NEUTRAL+500)
				msg = msg.replace("lawful", "(Neutral)");
			else
				msg = msg.replace("lawful", "(Lawful)");
			// pvp
			int kill = CharactersDatabase.getPvpKill(use);
			int dead = CharactersDatabase.getPvpDead(use);
			double total = dead+kill;
			double rate = kill==0||total==0 ? 0 : (kill / total) * 100;
			msg = msg.replace("pvpKill", String.valueOf(kill));
			msg = msg.replace("pvpDead", String.valueOf(dead));
			msg = msg.replace("pvpRate", new DecimalFormat("0.#").format(rate) + "%");
			// 클레스
			msg = msg.replace("class", Lineage.getClassTypeName(use.getClassType()));
			//위치
			int killmap =use.getMap();
			String PKmap = "";
		     switch (killmap) {
		     case 0:
		    	 PKmap = "말하는섬";
					break;
				case 1:
					PKmap = "말던 1층";
					break;
				case 2:
					PKmap = "말던 2층";
					break;
				case 4:
					if (use.getX() >= 33315 && use.getX() <= 33354
							&& use.getY() >= 32430 && use.getY() <= 32463) {
						PKmap = "용의 계곡 삼거리";
						break;
					} else if (use.getX() >= 33248 && use.getX() <= 33284
							&& use.getY() >= 32374 && use.getY() <= 32413) {
						PKmap = "용의 계곡 작은뼈";
						break;
					} else if (use.getX() >= 33374 && use.getX() <= 33406
							&& use.getY() >= 32319 && use.getY() <= 32357) {
						PKmap = "용의 계곡 큰뼈";
						break;
					} else if (use.getX() >= 33224 && use.getX() <= 33445
							&& use.getY() >= 32266 && use.getY() <= 32483) {
						PKmap = "용의 계곡";
						break;
					} else if (use.getX() >= 33497 && use.getX() <= 33781
							&& use.getY() >= 32230 && use.getY() <= 32413) {
						PKmap = "화룡의 둥지";
						break;
					} else if (use.getX() >= 33659 && use.getX() <= 33799
							&& use.getY() >= 32208 && use.getY() <= 32348) {
						PKmap = "화룡의 둥지 정상";
						break;
					} else if (use.getX() >= 33497 && use.getX() <= 33781
							&& use.getY() >= 32230 && use.getY() <= 32413) {
						PKmap = "화룡의 둥지";
						break;
					} else if (use.getX() >= 33832 && use.getX() <= 34039
							&& use.getY() >= 32341 && use.getY() <= 32649) {
						PKmap = "좀비 엘모어 밭";
						break;
					} else if (use.getX() >= 32716 && use.getX() <= 32980
							&& use.getY() >= 33075 && use.getY() <= 33391) {
						PKmap = "사막";
						break;
					} else if (use.getX() >= 32833 && use.getX() <= 32975
							&& use.getY() >= 32875 && use.getY() <= 32957) {
						PKmap = "골밭";
						break;
					} else if (use.getX() >= 32707 && use.getX() <= 32932
							&& use.getY() >= 32611 && use.getY() <= 32758) {
						PKmap = "카오틱 신전";
						break;
					} else if (use.getX() >= 33995 && use.getX() <= 34091
							&& use.getY() >= 32972 && use.getY() <= 33045) {
						PKmap = "린드비오르의 둥지";
						break;
					} else {
						PKmap = "아덴필드";
						break;
					}
				case 5:
					PKmap = "몽환의 섬";
					break;
				case 7:
					PKmap = "본토 던전 1층";
					break;
				case 8:
					PKmap = "본토 던전 2층";
					break;
				case 9:
					PKmap = "본토 던전 3층";
					break;
				case 10:
					PKmap = "본토 던전 4층";
					break;
				case 11:
					PKmap = "본토 던전 5층";
					break;
				case 12:
					PKmap = "본토 던전 6층";
					break;
				case 13:
					PKmap = "본토 던전 7층";
					break;
				case 14:
					PKmap = "지하 수로";
					break;
				case 15:
					PKmap = "켄트성 내성";
					break;
				case 16:
					PKmap = "하딘의 연구소";
					break;
				case 17:
					PKmap = "네루파 동굴";
					break;
				case 18:
					PKmap = "듀펠케넌 던전";
					break;
				case 19:
					PKmap = "요정숲 던전 1층";
					break;
				case 20:
					PKmap = "요정숲 던전 2층";
					break;
				case 21:
					PKmap = "요정숲 던전 3층";
					break;
				case 22:
					PKmap = "게라드의 시험 던전";
					break;
				case 23:
					PKmap = "윈다우드 던전 1층";
					break;
				case 24:
					PKmap = "윈다우드 던전 2층";
					break;
				case 25:
					PKmap = "사던 1층";
					break;
				case 26:
					PKmap = "사던 2층";
					break;
				case 27:
					PKmap = "사던 3층";
					break;
				case 28:
					PKmap = "사던 4층";
					break;
				case 29:
					PKmap = "윈다우드 내성";
					break;
				case 30:
					PKmap = "용의 계곡 던전 1층";
					break;
				case 31:
					PKmap = "용의 계곡 던전 2층";
					break;
				case 32:
					PKmap = "용의 계곡 던전 3층";
					break;
				case 33:
					PKmap = "용의 계곡 던전 4층";
					break;
				case 35:
					PKmap = "용의 계곡 던전 5층";
					break;
				case 36:
					PKmap = "용의 계곡 던전 6층";
					break;
				case 37:
					PKmap = "용의 계곡 던전 7층";
					break;
				case 43:
				case 44:
				case 45:
				case 46:
				case 47:
				case 48:
				case 49:
				case 50:
					PKmap = "개미굴 1층";
					break;
				case 51:
					PKmap = "개미굴 2층";
					break;
				case 52:
					PKmap = "기란 내성";
					break;
				case 53:
					PKmap = "기란감옥 1층";
					break;
				case 54:
					PKmap = "기란감옥 2층";
					break;
				case 55:
					PKmap = "기란감옥 3층";
					break;
				case 56:
					PKmap = "기란감옥 4층";
					break;
				case 59:
					PKmap = "에바 던전 1층";
					break;
				case 60:
					PKmap = "에바 던전 2층";
					break;
				case 61:
					PKmap = "에바 던전 3층";
					break;
				case 62:
					PKmap = "에바의 성지";
					break;
				case 63:
					PKmap = "에바 던전 4층";
					break;
				case 64:
					PKmap = "하이네 내성";
					break;
				case 65:
					PKmap = "파푸리온의 둥지";
					break;
				case 67:
					PKmap = "발라카스의 둥지";
					break;
				case 68:
					PKmap = "노래하는 섬";
					break;
				case 69:
					PKmap = "숨겨진 계곡";
					break;
				case 70:
					PKmap = "잊혀진 섬";
					break;
				case 72:
					PKmap = "얼음 던전 1층";
					break;
				case 73:
					PKmap = "얼음 던전 2층";
					break;
				case 74:
					PKmap = "얼음 던전 3층";
					break;
				case 75:
					PKmap = "상아탑 1층";
					break;
				case 76:
					PKmap = "상아탑 2층";
					break;
				case 77:
					PKmap = "상아탑 3층";
					break;
				case 78:
					PKmap = "상아탑 4층";
					break;
				case 79:
					PKmap = "상아탑 5층";
					break;
				case 80:
					PKmap = "상아탑 6층";
					break;
				case 81:
					PKmap = "상아탑 7층";
					break;
				case 82:
					PKmap = "상아탑 8층";
					break;
				case 85:
					PKmap = "노래하는 섬 던전";
					break;
				case 86:
					PKmap = "숨겨진 계곡 던전";
					break;
				case 87:
					PKmap = "숨겨진 계곡 던전";
					break;
				case 88:
					PKmap = "기란 콜로세움";
					break;
				case 93:
					PKmap = "켄트 콜로세움";
					break;
				case 101:
					PKmap = "오만의탑 1층";
					break;
				case 102:
					PKmap = "오만의탑 2층";
					break;
				case 103:
					PKmap = "오만의탑 3층";
					break;
				case 104:
					PKmap = "오만의탑 4층";
					break;
				case 105:
					PKmap = "오만의탑 5층";
					break;
				case 106:
					PKmap = "오만의탑 6층";
					break;
				case 107:
					PKmap = "오만의탑 7층";
					break;
				case 108:
					PKmap = "오만의탑 8층";
					break;
				case 109:
					PKmap = "오만의탑 9층";
					break;
				case 110:
					PKmap = "오만의탑 10층";
					break;
				case 200:
					PKmap = "오만의탑 정상";
					break;
				case 451:
					PKmap = "라스타바드던전";
					break;
				case 454:
					PKmap = "라스타바드던전";
					break;
				case 455:
					PKmap = "라스타바드던전";
					break;
				case 472:
					PKmap = "라스타바드던전";
					break;
				case 666:
					PKmap = "지옥";
					break;
				case 777:
					PKmap = "버땅";
					break;
				case 780:
					PKmap = "테베라스 사막";
					break;
				case 781:
					PKmap = "테베 피라미드";
					break;
				case 782:
					PKmap = "테베 제단";
					break;
				case 5001:
					PKmap = "결투장";
					break;
				case 5124:
					PKmap = "낚시터";
					break;
				default:
					PKmap = "본토";
					break;
		     }
			msg = msg.replace("PKmap", String.format("위치: "+PKmap+""));
			
			pc.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), msg));
		}
		// 접속자수 표현.
		int count = Lineage.world_player_count_init + (int)Math.round( (World.getPcSize()/*+World.getRobotSize()*/) * Lineage.world_player_count + Lineage.world_player_plus_count );
		pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 81, String.valueOf(count)));
		
		return this;
	}
}
