package lineage.world.controller;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.robot.PcRobotInstance;


public final class AutoHuntCheckController {

	/**
	 * 몬스터 킬수 누적 메소드.
	 * 2018-07-11
	 * by connectro12@nate.com
	 */
	static public void addCount(PcInstance pc) {
		if (Lineage.is_auto_hunt_check&&!pc.isAutoHunt()) {
			pc.setAutoHuntMonsterCount(pc.getAutoHuntMonsterCount() + 1);
			
			if (pc.getAutoHuntMonsterCount() >= Lineage.auto_hunt_monster_kill_count)
				sendMessage(pc);
		}
	}
	
	/**
	 * 몬스터 킬수 체크 메소드.
	 * 2018-07-11
	 * by connectro12@nate.com
	 */
	static public boolean checkCount(PcInstance pc) {
		if (pc instanceof PcRobotInstance)
			return true;
		if (Lineage.is_auto_hunt_check&& !pc.isAutoHunt() && Lineage.auto_hunt_monster_kill_count <= pc.getAutoHuntMonsterCount() && pc.getAutoHuntAnswerTime() < 1) {
			sendMessage(pc);
			return false;
		} else if (Lineage.is_auto_hunt_check && Lineage.auto_hunt_monster_kill_count <= pc.getAutoHuntMonsterCount() &&
				pc.getAutoHuntAnswerTime() > 0 && pc.getAutoHuntAnswerTime() <= System.currentTimeMillis()) {
			reSendMessage(pc);
			return false;
		}
		return true;
	}
	
	/**
	 * 인증번호 보내는 메소드.
	 * 2018-07-11
	 * by connectro12@nate.com
	 */
	static public void sendMessage(PcInstance pc) {
		if (Lineage.is_auto_hunt_check && pc.getAutoHuntAnswerTime() < 1 &&!pc.isAutoHunt()) {
			int number = Util.random(1, 10);
			
			pc.setAutoHuntAnswer(String.valueOf(number));
			pc.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), pc, Lineage.CHATTING_MODE_MESSAGE, "                  * 자동사냥 방지 확인 *"));
			pc.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), pc, Lineage.CHATTING_MODE_MESSAGE, String.format("    %d초내에 인증을 하지 않을시 몬스터 공격이 제한됩니다.", Lineage.auto_hunt_answer_time / 1000)));
			pc.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), pc, Lineage.CHATTING_MODE_MESSAGE, String.format("\\fR          채팅창에 [\\fY%d\\fR]를 정확히 입력해주세요.", number)));
			pc.setAutoHuntAnswerTime(System.currentTimeMillis() + Lineage.auto_hunt_answer_time);
		}
	}
	
	/**
	 * 시간이 지난 후 메세지 
	 * 2018-07-12
	 * by connector12@nate.com
	 */
	static public void reSendMessage(PcInstance pc) {
		if (Lineage.is_auto_hunt_check&&!pc.isAutoHunt()) {
			if (pc.getAutoHuntAnswer() == null)
				pc.setAutoHuntAnswer(String.valueOf(Util.random(1, 10)));
				
			pc.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), pc, Lineage.CHATTING_MODE_MESSAGE, "                  * 자동사냥 방지 재확인 *"));
			pc.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), pc, Lineage.CHATTING_MODE_MESSAGE, String.format("\\fR          채팅창에 [\\fY%s\\fR]를 정확히 입력해주세요.", pc.getAutoHuntAnswer())));
		}
	}
	
	/**
	 * 인증번호 체크 메소드.
	 * 2018-07-11
	 * by connectro12@nate.com
	 */
	static public boolean checkMessage(PcInstance pc, String answer) {
	

		if (Lineage.is_auto_hunt_check &&!pc.isAutoHunt()) {
			if (pc.getAutoHuntAnswer().equalsIgnoreCase(answer)) {
				pc.setAutoHuntMonsterCount(0);
				pc.setAutoHuntAnswerTime(0);
				pc.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), pc, Lineage.CHATTING_MODE_MESSAGE, "\\fY         자동사냥 방지를 완료하였습니다."));
				
			} else {
				pc.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), pc, Lineage.CHATTING_MODE_MESSAGE, "\\fY         자동사냥 방지 번호를 잘못 입력하셨습니다."));
				reSendMessage(pc);
			}
		}
		return false;
	}
	
	/**
	 * 월드 접속시 자동사냥 방지 인증번호 보내야할지 체크하는 메소드.
	 * 2018-07-11
	 * by connectro12@nate.com
	 */
	static public void toWorldJoin(PcInstance pc) {
		if (Lineage.is_auto_hunt_check && !pc.isAutoHunt()) {
			if (Lineage.auto_hunt_monster_kill_count <= pc.getAutoHuntMonsterCount()) {
				pc.setAutoHuntAnswerTime(1);
				reSendMessage(pc);
			}
		}
	}
	
	/**
	 * 해당 오브젝트가 몬스터인지 체크하는 메소드.
	 * 2018-07-11
	 * by connectro12@nate.com
	 */
	static public boolean checkMonster(object o) {
		if (o instanceof MonsterInstance)
			return true;
		
		return false;
	}
	
}
