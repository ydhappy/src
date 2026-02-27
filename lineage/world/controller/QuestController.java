package lineage.world.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.lineage.Quest;
import lineage.database.CharactersDatabase;
import lineage.database.DatabaseConnection;
import lineage.database.ExpDatabase;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterQuest;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.World;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.npc.quest.Dilong;
import lineage.world.object.npc.quest.FairyPrincess;
import lineage.world.object.npc.quest.Gatekeeper;

public final class QuestController {

	static private Map<Long, List<Quest>> list;
	// 퀘스트 npc들..
	static private Gatekeeper gatekeeper; // 은기사마을 문지기.
	static private FairyPrincess fairyprincess; // 요숲 페어리 프린세스
	static private Dilong dilong; // 말섬던전1층 디롱.

	/**
	 * 초기화 함수.
	 * 
	 * @param con
	 */
	static public void init(Connection con) {
		TimeLine.start("QuestController..");

		list = new HashMap<Long, List<Quest>>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM characters_quest ORDER BY objId");
			rs = st.executeQuery();
			while (rs.next()) {
				Quest q = new Quest();
				q.setObjectId(rs.getInt("objId"));
				q.setName(rs.getString("name"));
				q.setNpcName(rs.getString("npc_name"));
				q.setQuestAction(rs.getString("quest_action"));
				q.setQuestStep(rs.getInt("quest_step"));

				List<Quest> l = list.get(q.getObjectId());
				if (l == null) {
					l = new ArrayList<Quest>();
					list.put(q.getObjectId(), l);
				}
				l.add(q);
			}
		} catch (Exception e) {
			lineage.share.System.println(QuestController.class.toString()
					+ " : init(Connection con)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}

		TimeLine.end();
	}

	/**
	 * 서버닫힐때 처리하는 디비저장 및 메모리 제거 함수.
	 * 
	 * @param con
	 */
	static public void close(Connection con) {
		toSave(con);
		synchronized (list) {
			list.clear();
		}
	}

	static public void toSave(Connection con) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("DELETE FROM characters_quest");
			st.executeUpdate();
			st.close();
			synchronized (list) {
				for (List<Quest> q_list : list.values()) {
					for (Quest q : q_list) {
						st = con.prepareStatement("INSERT INTO characters_quest SET objId=?, name=?, npc_name=?, quest_action=?, quest_step=?");
						st.setLong(1, q.getObjectId());
						st.setString(2, q.getName());
						st.setString(3, q.getNpcName());
						st.setString(4, q.getQuestAction());
						st.setInt(5, q.getQuestStep());
						st.executeUpdate();
						st.close();
					}
				}
			}
		} catch (Exception e) {
			lineage.share.System.println(QuestController.class.toString()
					+ " : toSave(Connection con)");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}

	/**
	 * 관리목록 제거.
	 * 
	 * @param objId
	 */
	static public void remove(long objId) {
		synchronized (list) {
			List<Quest> quests = list.remove(objId);
			if (quests != null)
				quests.clear();
			quests = null;
		}
	}

	/**
	 * 사용자가 월드에 진입할때 한번 호출됨.
	 * 
	 * @param pc
	 */
	static public void toWorldJoin(PcInstance pc) {
		if (Lineage.server_version >= 380)
			pc.toSender(S_CharacterQuest.clone(
					BasePacketPooling.getPool(S_CharacterQuest.class), pc));
	}

	/**
	 * 사용자가 월드를 나갈때 한번 호출됨.
	 * 
	 * @param pc
	 */
	static public void toWorldOut(PcInstance pc) {
		//
	}

	/**
	 * 페어리의 축하 선물 클릭시 호출됨.
	 * 
	 * @param pc
	 * @param type
	 */
	static public void toRequest(PcInstance pc, int type) {
		// 아이템 지급.
		switch (type) {
		case 39: // LV10 페어리의 축하 선물
			CraftController.toCraft(pc, ItemDatabase.find(11355), 1, true, 0);
			pc.toExp(null, ExpDatabase.find(10).getExp() * 0.05);
			break;
		case 40: // LV15 페어리의 축하 선물
			CraftController.toCraft(pc, ItemDatabase.find(11356), 1, true, 0);
			pc.toExp(null, ExpDatabase.find(15).getExp() * 0.05);
			break;
		case 41: // LV20 페어리의 축하 선물
			CraftController.toCraft(pc, ItemDatabase.find(11357), 1, true, 0);
			pc.toExp(null, ExpDatabase.find(20).getExp() * 0.05);
			break;
		case 42: // LV25 페어리의 축하 선물
			CraftController.toCraft(pc, ItemDatabase.find(11358), 1, true, 0);
			pc.toExp(null, ExpDatabase.find(25).getExp() * 0.05);
			break;
		case 43: // LV30 페어리의 축하 선물
			CraftController.toCraft(pc, ItemDatabase.find(11359), 1, true, 0);
			pc.toExp(null, ExpDatabase.find(30).getExp() * 0.05);
			break;
		case 44: // LV35 페어리의 축하 선물
			CraftController.toCraft(pc, ItemDatabase.find(11360), 1, true, 0);
			pc.toExp(null, ExpDatabase.find(35).getExp() * 0.05);
			break;
		case 45: // LV40 페어리의 축하 선물
			CraftController.toCraft(pc, ItemDatabase.find(11361), 1, true, 0);
			pc.toExp(null, ExpDatabase.find(40).getExp() * 0.05);
			break;
		case 46: // LV45 페어리의 축하 선물
			CraftController.toCraft(pc, ItemDatabase.find(11362), 1, true, 0);
			pc.toExp(null, ExpDatabase.find(45).getExp() * 0.05);
			break;
		case 47: // LV50 페어리의 축하 선물
			CraftController.toCraft(pc, ItemDatabase.find(11363), 1, true, 0);
			pc.toExp(null, ExpDatabase.find(50).getExp() * 0.05);
			break;
		case 48: // LV52 페어리의 축하 선물
			CraftController.toCraft(pc, ItemDatabase.find(11364), 1, true, 0);
			pc.toExp(null, ExpDatabase.find(52).getExp() * 0.05);
			break;
		case 49: // LV53 페어리의 축하 선물
			break;
		case 62: // 사냥터
			// 축순 2장
			ItemInstance temp = ItemDatabase
					.newInstance(ItemDatabase.find(230));
			temp.setCount(2);
			temp.setDefinite(true);
			temp.setBress(0);
			pc.getInventory().append(temp, 2);
			ItemDatabase.setPool(temp);
			pc.toExp(null, ExpDatabase.find(50).getExp() * 0.05);
			break;
		case 75: // 가이드
			pc.toExp(null, ExpDatabase.find(45).getExp() * 0.05);
			break;
		default:
			ChattingController.toChatting(pc,
					String.format("Fairy GiftBox %d", type),
					20);
			return;
		}
		String quest_name = String.format("Fairy GiftBox %d", type);
		Quest q = QuestController.find(pc, quest_name);
		if (q == null)
			q = newQuest(pc, null, quest_name);
		q.setQuestStep(100);
		// 전체 퀘스트 패킷 전송
		pc.toSender(S_CharacterQuest.clone(
				BasePacketPooling.getPool(S_CharacterQuest.class), pc));
	}

	static public void setGateKeeper(Gatekeeper gk) {
		gatekeeper = gk;
	}

	static public void setFairyPrincess(FairyPrincess fp) {
		fairyprincess = fp;
	}

	static public void setDilong(Dilong d) {
		dilong = d;
	}

	/**
	 * 타이머에서 지속적으로 호출.
	 * 
	 * @param time
	 */
	static public void toTimer(long time) {
		boolean d = false;
		// 수련동굴 확인.
		if (gatekeeper != null && gatekeeper.isDungeon()) {
			d = false;
			for (PcInstance pc : World.getPcList()) {
				if (pc.getMap() == 22) {
					d = true;
					break;
				}
			}
			gatekeeper.setDungeon(d);
		}
		// 다크엘프 던전 확인.
		if (fairyprincess != null && fairyprincess.isDungeon()) {
			d = false;
			for (PcInstance pc : World.getPcList()) {
				if (pc.getMap() == 213) {
					d = true;
					break;
				}
			}
			fairyprincess.setDungeon(d);
		}
		// 디롱쪽 언데드 던전 확인.
		if (dilong != null && dilong.isDungeon()) {
			d = false;
			for (PcInstance pc : World.getPcList()) {
				if (pc.getMap() == 201) {
					d = true;
					break;
				}
			}
			dilong.setDungeon(d);
		}
	}

	/**
	 * 퀘스트 객체 찾기.
	 * 
	 * @param pc
	 * @param action
	 * @return
	 */
	static public Quest find(PcInstance pc, String action) {
		List<Quest> list = find(pc);
		if (list != null) {
			for (Quest q : list) {
				if (q.getQuestAction().equalsIgnoreCase(action))
					return q;
			}
		}
		return null;
	}

	static public Quest find(long key, String action) {
		List<Quest> list = find(key);
		if (list != null) {
			for (Quest q : list) {
				if (q.getQuestAction().equalsIgnoreCase(action))
					return q;
			}
		}
		return null;
	}

	static public List<Quest> find(PcInstance pc) {
		return find(pc.getObjectId());
	}

	static public List<Quest> find(long key) {
			return list.get(key);
	}

	/**
	 * 새로운 퀘스트 등록처리 함수. : 퀘스트 진행하려는 퀘스트처리 객체가 없을경우 해당 객체를 통해 생성처리 함.
	 * 
	 * @param pc
	 * @param qi
	 * @return
	 */
	static public Quest newQuest(PcInstance pc, object o, String action) {
		Quest q = new Quest();
		q.setObjectId(pc.getObjectId());
		q.setName(pc.getName());
		q.setNpcName(o == null ? "" : (o.getNpc() == null ? o.getName() : o.getNpc().getName()));
		q.setQuestAction(action);

		List<Quest> q_list = find(pc);
		if (q_list == null) {
			q_list = new ArrayList<Quest>();
				if (!list.containsKey(pc.getObjectId()))
					list.put(pc.getObjectId(), q_list);
		}
		q_list.add(q);
		return q;
	}

	static public List<Long> getList() {
		synchronized (list) {
			return new ArrayList<Long>(list.keySet());
		}
	}
}
