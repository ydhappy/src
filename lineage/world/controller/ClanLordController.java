package lineage.world.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import lineage.bean.database.Skill;
import lineage.bean.lineage.Clan;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectTitle;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.RobotClanInstance;

public class ClanLordController {

	static public Map<Long, RobotClanInstance> clan_robot_list;
	static public List<RobotClanInstance> list;
	
	static public void init() {
		TimeLine.start("ClanLordController..");
		
		clan_robot_list = new HashMap<Long, RobotClanInstance>();
		list = new ArrayList<RobotClanInstance>();
		
		TimeLine.end();
	}

	static synchronized public Object toCommand(object o, String cmd, StringTokenizer st) {
		if (cmd.equalsIgnoreCase(".무인혈맹")) {
			Start((PcInstance) o, st);
			return true;
		}
		return null;
	}

	static private void Start(PcInstance pc, StringTokenizer st) {
		
		try {
			
//			System.out.println("test");
			
//			System.out.println("next:" + st);
			
			String type = st.nextToken();
			
//			System.out.println(type);
			
			RobotClanInstance clan_robot = clan_robot_list.get(pc.getObjectId());

			if (type.equalsIgnoreCase("시작")) {
				
				pc.setClan_shadow_step(0);
				
				if(pc.getClanId() == 0 || pc.getClanName() == null) {
					ChattingController.toChatting(pc, "혈맹 창설후 사용해 주세요.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if(pc.getClassType() != Lineage.LINEAGE_CLASS_ROYAL) {
					ChattingController.toChatting(pc, "군주/공주만 사용 가능 합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				Clan c = ClanController.find(pc);
				
				if(c != null && !c.getLord().equalsIgnoreCase(pc.getName())) {
					ChattingController.toChatting(pc, "혈맹 군주만 사용 가능 합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				boolean sbuff1 = false;
				boolean sbuff2 = false;
				
				if(SkillController.find(pc) != null) {
					for(Skill s : SkillController.find(pc)) {
						if(s.getName().equalsIgnoreCase("글로잉오라")) {
							sbuff1 = true;
							break;
						}
					}
					
					for(Skill s : SkillController.find(pc)) {
						if(s.getName().equalsIgnoreCase("샤이닝오라")) {
							sbuff2 = true;
							break;
						}
					}
				}
				
				if (clan_robot == null) {
					clan_robot = new RobotClanInstance(pc.getObjectId(), pc.getName(), pc.getClassType(), pc.getClassSex(), c, pc.getClassGfx(), sbuff1, sbuff2);
					clan_robot_list.put(pc.getObjectId(), clan_robot);
					list.add(clan_robot);
				}
				
				clan_robot.setHeading(pc.getHeading());
				clan_robot.toTeleport(pc.getX(), pc.getY(), pc.getMap(), false);
				
				ChattingController.toChatting(pc, "\\fT최소 가입레벨을 설정하여 주세요.", Lineage.CHATTING_MODE_MESSAGE);
				pc.setClan_shadow_step(1);
				
			} else if (type.equalsIgnoreCase("홍보")) {
				StringBuffer sb = new StringBuffer();
				while (st.hasMoreTokens()) {
					sb.append(st.nextToken());
					if (st.hasMoreTokens())
						sb.append(" ");
				}
				clan_robot._comment = sb.toString();
				ChattingController.toChatting(pc, "\\fR\"" + sb.toString() + "\" 홍보멘트 설정",
						Lineage.CHATTING_MODE_MESSAGE);
			} else if (type.equalsIgnoreCase("종료")) {
				clan_robot.close();
				clan_robot_list.remove(pc.getObjectId());
				ChattingController.toChatting(pc, "\\fR무인혈맹이 종료되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			} else {
				ChattingController.toChatting(pc, "\\fR[.무일혈맹] 옵션 : 시작, 홍보, 종료", Lineage.CHATTING_MODE_MESSAGE);
			}
		} catch (Exception e) {
			ChattingController.toChatting(pc, ".무인혈맹 시작/종료/홍보", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
	
	// 버그 방지를 위해 가입요청자 담을 변수
	private static List<PcInstance> tempList = new ArrayList<PcInstance>();

//	static public void toAsk(PcInstance pc, Clan c) {
//		pc.toSender(S_MessageYesNo.clone(BasePacketPooling.getPool(S_MessageYesNo.class), 567, c.getName()));
//		setTempList(pc);
//	}

	public static void toJoin(PcInstance pc) {
		Clan c = ClanController.find(pc.getRobot_clan_name());
		if (pc.getGm()>0 || (c.sizeMemberList()<300 && c.sizeMemberList()<ClanController.getClanMemberMaxSize(c.getLord()) && pc.getLevel() > 4)) {
			pc.setClanId(c.getUid());
			pc.setClanName(c.getName());
			pc.setTitle("");
			// 패킷 처리
			pc.toSender(S_ObjectTitle.clone(BasePacketPooling.getPool(S_ObjectTitle.class), pc), true);
			// 94 \f1%0%o 혈맹의 일원으로 받아들였습니다.
			c.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 94, pc.getName()));
			// 95 \f1%0 혈맹에 가입하였습니다.
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 95, c.getName()));
			// 혈맹 관리목록 갱신
			c.appendMemberList(pc.getName());
			if (!pc.isWorldDelete())
				c.appendList(pc);
			removeTempList(pc);
		} else {
			// 188 %0%s 당신을 혈맹 구성원으로 받아들일 수 없습니다.
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 188, c.getLord()));
		}
	}

	public static List<RobotClanInstance> getList() {
		return list;
	}
	
	public static RobotClanInstance findCr(long objectid) {
		
		RobotClanInstance r = null;
		
		synchronized(list) {
			for(RobotClanInstance robot : list) {
				if(robot.getPc_objid() == objectid) {
					r = robot;
					break;
				}
			}
		}
		
		return r;
	}

	public static void setTempList(PcInstance pc) {
		if (!tempList.contains(pc))
			tempList.add(pc);
	}

	public static boolean isTempCheck(PcInstance pc) {
		if (tempList.contains(pc))
			return true;
		return false;
	}

	public static void removeTempList(PcInstance pc) {
		if (tempList.contains(pc))
			tempList.remove(pc);
	}
	
}
