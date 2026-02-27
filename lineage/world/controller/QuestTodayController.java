package lineage.world.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.database.Exp;
import lineage.bean.lineage.Board;
import lineage.bean.lineage.Party;
import lineage.bean.lineage.Quest;
import lineage.bean.lineage.QuestToday;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_BoardView;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_MessageGreen;
import lineage.network.packet.server.S_ObjectChatting; 
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.QuestTodayBoardInstance;
import lineage.world.object.robot.PcRobotInstance;

public class QuestTodayController {

	static private Map<Long, QuestToday> list;
	static private QuestTodayBoardInstance board;
	
	static public void init(Connection con) {
		TimeLine.start("QuestTodayController..");
		
		list = new HashMap<Long, QuestToday>();

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM quest_today");
			rs = st.executeQuery();
			while(rs.next()) {
				QuestToday qt = new QuestToday();
				qt.setUid(rs.getInt("uid"));
				qt.setName(rs.getString("name"));
				qt.setType(rs.getString("type"));
				qt.setLevelMin(rs.getInt("level_min"));
				qt.setLevelMax(rs.getInt("level_max"));
				qt.setTargetName(rs.getString("target_name"));
				qt.setTargetItem(rs.getString("target_item"));
				qt.setTargetCount(rs.getInt("target_count"));
				qt.setRewardItem(rs.getString("reward_item"));
				qt.setRewardCount(rs.getInt("reward_count"));
				qt.setRewardBless(rs.getString("reward_bless"));
				qt.setRewardEnchant(rs.getInt("reward_enchant"));
				list.put(qt.getUid(), qt);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init()\r\n", QuestTodayController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}

		if(list.size() > 0) {
			board = new QuestTodayBoardInstance();
			board.setObjectId(ServerDatabase.nextNpcObjId());
			board.setGfx(1546);
			board.setHeading(6);
			board.setName("    \\f:[일일퀘스트 정보]");
			board.setTitle("게시판");
			board.toTeleport(33449, 32818, 4 , false);
		}
		
		TimeLine.end();
	}
	
	static public void close() {

		clear();

		if(board != null) {
			World.remove(board);
			board.clearList(true);
		}
		synchronized (list) {
			list.clear();
		}
	}
	
	static public void clear() {
	}
	/**
	 * Board에 대한 뒷처리 반드시 해야함. (pool 재사용 처리.)
	 * @return
	 */
	
	static public List<Board> getList(PcInstance pc) {
		synchronized (list) {
			List<Board> r_list = new ArrayList<Board>();
			for(QuestToday qt : list.values())
				r_list.add( toView(pc, qt) );
			return r_list;
		}
	}
	
	static public List<Quest> getListQuest(PcInstance pc) {
		synchronized (list) {
			List<Quest> r_list = new ArrayList<Quest>();
			for(QuestToday qt : list.values()) {
				Quest q = QuestController.find(pc, qt.toString());
				if(q != null)
					r_list.add( q );
			}
			return r_list;
		}
	}
	
	/**
	 * 게시판 읽기 처리 함수.
	 * @param pc
	 * @param bi
	 * @param uid
	 */
	static public void toView(PcInstance pc, long uid){
		QuestToday qt = getView(uid);
		if(qt != null) {

			Board b = toView(pc, qt);
			pc.toSender(S_BoardView.clone(BasePacketPooling.getPool(S_BoardView.class), b));

			BoardController.setPool( b );
		}
	}
	
	/**
	 * 타이머가 주기적으로 호출함.
	 * @param time
	 */
	static public void toTimer(long time) {
		int h = Util.getHours(time);
		int m = Util.getMinutes(time);
		int s = Util.getSeconds(time);
		if(h==0 && m==0 && s==0) {
			// 일일퀘스트 초기화 처리.
			List<Quest> temp = new ArrayList<Quest>();
			// 현재 관리중인 전체 퀘스트 추출.
			for(long key : QuestController.getList()) {
				// 키값에 해당하는 퀘스트 추출.
				List<Quest> list = QuestController.find(key);
				// 일일퀘스트만 추려내기.
				for(Quest q : list) {
					if(q.getQuestAction().startsWith("QuestToday:"))
						temp.add(q);
				}
				// 관리목록에서 제거.
				for(Quest q : temp) {
					list.remove(q);
					q.close();
				}
				temp.clear();
			} //월드에 안내하기
			World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "[안내] 일일퀘스트가 초기화 되었습니다."));
		}
	}
	
	/**
	 * 
	 * @param pri
	 * @param pc_objectId
	 */
	static public void toRobotDead(PcRobotInstance pri, long pc_objectId) {
		try {

			PcInstance pc = World.findPc(pc_objectId);

			if(pc == null)
				return;

			List<Quest> list = QuestController.find(pc);
			if(list == null)
				return;
			for(Quest q : list) {
				if(q.getNpcName().equalsIgnoreCase("진행중") && q.getQuestAction().startsWith("QuestToday:")) {
					int pos = q.getQuestAction().indexOf("uid(") + 4;
					int uid = Integer.valueOf( q.getQuestAction().substring(pos, q.getQuestAction().indexOf(")", pos)) );
					QuestToday qt = getView(uid);
					if(qt!=null && qt.getTargetName().equalsIgnoreCase("로봇")) {

						boolean isLevel = q.getQuestStep()>0 || ((qt.getLevelMin()==0 || pc.getLevel()>=qt.getLevelMin()) && (qt.getLevelMax()==0 || pc.getLevel()<=qt.getLevelMax()));
						if(isLevel == false)
							continue;
						// 구분에 따른 처리.
						if(qt.getType().equalsIgnoreCase("hunter")) {
							if(q.getQuestStep() < qt.getTargetCount())
								q.setQuestStep( q.getQuestStep() + 1 );
						} else if(qt.getType().equalsIgnoreCase("find_item")) {

							if(Util.random(0, 100) <= 30) {
								CraftController.toCraft(pri, pc, ItemDatabase.find(qt.getTargetItem()), 1, true, 0, 0, 1, "type|일일퀘스트수집아이템");
								if(q.getQuestStep() < qt.getTargetCount())
									q.setQuestStep( q.getQuestStep() + 1 );
							}
						}
					}
				}
			}
		} catch (Exception e) { }
	}
	
	/**
	 * 몬스터가 죽게되면 호출됨.
	 * 	: 몬스터가 죽은 후 바로 호출되며, 아이템 지급 및 경험치 지급이 이루어지기 전 상태.
	 * @param mi
	 */
	static public void toMonsterDead(MonsterInstance mi) {
		// 몬스터에게 경험치를 지급받을 수 있는 객체 순회.
		for(Exp e : mi.getExpList()) {
			// 유저들만 처리하기.
			if(!(e.getObject() instanceof PcInstance))
				continue;
			// 처리.
			PcInstance pc = (PcInstance)e.getObject();
			if(pc != null){
				// 파티체크
				if(pc.getPartyId() == 0) {
					toMonsterDead(mi, pc);
				} else {
					Party p = PartyController.find(pc);
					if(p == null)
						toMonsterDead(mi, pc);
					else {
						for(PcInstance use : p.getList()) {
							if(Util.isDistance(pc, use, Lineage.SEARCH_LOCATIONRANGE))
								toMonsterDead(mi, use);
						}
					}
				}
			}
		}
	}
	
	static public void toMonsterDead(MonsterInstance mi, PcInstance pc) {
		List<Quest> list = QuestController.find(pc);
		if(list == null)
			return;
		for(Quest q : list) {
			if(q.getNpcName().equalsIgnoreCase("진행중") && q.getQuestAction().startsWith("QuestToday:")) {
				int pos = q.getQuestAction().indexOf("uid(") + 4;
				int uid = Integer.valueOf( q.getQuestAction().substring(pos, q.getQuestAction().indexOf(")", pos)) );
				QuestToday qt = getView(uid);
				if(qt!=null && qt.getTargetName().equalsIgnoreCase(mi.getMonster().getName())) {
					// 레벨체크.
					boolean isLevel = q.getQuestStep()>0 || ((qt.getLevelMin()==0 || pc.getLevel()>=qt.getLevelMin()) && (qt.getLevelMax()==0 || pc.getLevel()<=qt.getLevelMax()));
					if(isLevel == false)
						continue;
					// 구분에 따른 처리.
					if(qt.getType().equalsIgnoreCase("hunter")) {
						if(q.getQuestStep() < qt.getTargetCount())
							q.setQuestStep( q.getQuestStep() + 1 );
					} else if(qt.getType().equalsIgnoreCase("find_item")) {
						if(Util.random(0, 100) <= 30) {
							CraftController.toCraft(mi, pc, ItemDatabase.find(qt.getTargetItem()), 1, true, 0, 0, 1, "type|일일퀘스트수집아이템");
							if(q.getQuestStep() < qt.getTargetCount())
								q.setQuestStep( q.getQuestStep() + 1 );
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param uid
	 */
	static public QuestToday getView(long uid){
		synchronized (list) {
			return list.get(uid);
		}
	}
	
	static private Board toView(PcInstance pc, QuestToday qt) {
		//
		Quest q = QuestController.find(pc, qt.toString());
		boolean isLevel = (q!=null && q.getQuestStep()>0) || ((qt.getLevelMin()==0 || pc.getLevel()>=qt.getLevelMin()) && (qt.getLevelMax()==0 || pc.getLevel()<=qt.getLevelMax()));
		// 레벨이 적합하다면.
		if(isLevel) {
			// 신규 퀘스트 등록 처리.
			if(q == null) {
				q = QuestController.newQuest(pc, null, qt.toString());
				q.setNpcName("진행중");
				ChattingController.toChatting(pc, "[안내] 새로운 일일퀘스트가 등록되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
			// 보상 처리.
			if(q.getNpcName().equalsIgnoreCase("진행중") && q.getQuestStep()>=qt.getTargetCount()) {
				q.setNpcName("완료");
				ChattingController.toChatting(pc, "[안내] '"+qt.getName()+"'퀘스트가 완료되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
				pc.toSender(new S_MessageGreen(556, "\\fZ[안내] '"+qt.getName()+"'퀘스트가 완료되었습니다."));
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 403, qt.getRewardCount()==1 ? qt.getRewardItem() : String.format("%s (%d)", qt.getRewardItem(), qt.getRewardCount())));

				if(qt.getTargetItem().length() > 0) {
					List<ItemInstance> list = new ArrayList<ItemInstance>();
					pc.getInventory().findDbName(qt.getTargetItem(), list);
					for(ItemInstance ii : list)
						pc.getInventory().count(ii, 0, true);
					list.clear();
				}
				// 보상 아이템 지급.
				ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find(qt.getRewardItem()));
				if(ii != null) {
					ii.setCount(qt.getRewardCount());
					ii.setEnLevel(qt.getRewardEnchant());
					ii.setBress(qt.getRewardBless());
					pc.getInventory().append(ii, ii.getCount(), "");
					ItemDatabase.setPool(ii);
					//pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 403, qt.getRewardCount()==1 ? qt.getRewardItem() : String.format("%s (%d)", qt.getRewardItem(), qt.getRewardCount())));
				}
				//ChattingController.toChatting(pc, "[안내] '"+qt.getName()+"'퀘스트가 완료되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
		//
		Board b = BoardController.getPool();
		StringBuffer sb = new StringBuffer();
		//sb.append("\r\n");
		//sb.append(String.format("%s", qt.getTargetName())).append("을 잡아라!\r\n");
		sb.append("\r\n");
		sb.append("----------------------------").append("\r\n");
		sb.append(String.format("퀘스트 레벨: %d ~ %d", qt.getLevelMin(), qt.getLevelMax())).append("\r\n");
		sb.append("----------------------------").append("\r\n");
		sb.append(String.format("대  상  명: %s", qt.getTargetName())).append("\r\n");
		if(qt.getTargetItem().length() > 0) {
		sb.append(String.format("수집아이템: %s", qt.getTargetItem())).append("\r\n");
		sb.append(String.format("수집  갯수: %d/%d", q==null?0:q.getQuestStep(), qt.getTargetCount())).append("\r\n");
		} else {
		sb.append(String.format("대상 마리수: %d/%d", q==null?0:q.getQuestStep(), qt.getTargetCount())).append("\r\n");
		}
		sb.append("----------------------------").append("\r\n");
		sb.append(String.format("보      상: %s", qt.getRewardItem())).append("\r\n");
		sb.append(String.format("보상  갯수: %,d", qt.getRewardCount())).append("\r\n");
		sb.append("----------------------------").append("\r\n");
		sb.append("\r\n");
		if(isLevel) {
			if(q.getNpcName().equalsIgnoreCase("완료"))
				sb.append("퀘스트 상태: [완료]");
			else
				sb.append("퀘스트 상태: [진행중]");
		} else {
			sb.append("퀘스트 상태: [불가능]");
		}
		sb.append("\r\n");
		sb.append("\r\n");
		sb.append(String.format("진행중인 퀘스트 확인: .일퀘", qt.getRewardCount())).append("\r\n");
		//
		b.setUid( qt.getUid() );
		b.setName( ServerDatabase.getName() );
		b.setSubject( qt.getName() );
		b.setDays( System.currentTimeMillis() );
		b.setMemo( sb.toString() );
		//
		return b;
	}
	
}
