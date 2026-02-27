package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.QuestPresent;
import lineage.bean.lineage.Quest;
import lineage.share.System;
import lineage.share.TimeLine;
import lineage.world.controller.ChattingController;
import lineage.world.controller.QuestController;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class QuestPresentDatabase {
	private static String QUEST_LEVEL_ATTAINMENT_10 = "level attainment quest 10";
	private static String QUEST_LEVEL_ATTAINMENT_20 = "level attainment quest 20";
	private static String QUEST_LEVEL_ATTAINMENT_30 = "level attainment quest 30";
	private static String QUEST_LEVEL_ATTAINMENT_40 = "level attainment quest 40";
	private static String QUEST_LEVEL_ATTAINMENT_45 = "level attainment quest 45";
	private static String QUEST_LEVEL_ATTAINMENT_50 = "level attainment quest 50";
	private static String QUEST_LEVEL_ATTAINMENT_55 = "level attainment quest 55";
	private static String QUEST_LEVEL_ATTAINMENT_60 = "level attainment quest 60";
	private static String QUEST_LEVEL_ATTAINMENT_65 = "level attainment quest 65";
	private static String QUEST_LEVEL_ATTAINMENT_70 = "level attainment quest 70";
	private static String QUEST_LEVEL_ATTAINMENT_75 = "level attainment quest 75";
	private static String QUEST_LEVEL_ATTAINMENT_80 = "level attainment quest 80";
	private static String QUEST_LEVEL_ATTAINMENT_85 = "level attainment quest 85";
	private static List<QuestPresent> list;

	public static void init(Connection con) {
		TimeLine.start("QuestPresentDatabase..");
		list = new ArrayList<QuestPresent>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM characters_quest_present");
			rs = st.executeQuery();
			while (rs.next())
			{
				QuestPresent QuestPresent = new QuestPresent();
				QuestPresent.setItemName(rs.getString("item_name"));
				QuestPresent.setItemCount(rs.getInt("item_count"));
				QuestPresent.setItemEnchant(rs.getInt("item_enchant"));
				QuestPresent.setItemBress(rs.getInt("item_bless"));
				QuestPresent.setClassType(rs.getInt("class_type"));
				QuestPresent.setLevel(rs.getInt("level"));
				QuestPresent.setNote(rs.getString("note"));
				list.add(QuestPresent);
			}
		} catch (Exception e) {
			System.printf("%s : init(Connection con)\r\n", new Object[] { QuestPresentDatabase.class.toString() });
			System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		TimeLine.end();
	}



	public static void checkLevelCompensation(PcInstance pc) {
		try {
			Item item = null;
			ItemInstance itemInstance = null;
			ArrayList<QuestPresent> ArrayList = new ArrayList<QuestPresent>();
			Quest q = QuestController.find(pc, QUEST_LEVEL_ATTAINMENT_10);
			if (pc.getLevel() >= 10 && q == null) {
				find(pc.getClassType(), 10, ArrayList);
				if (ArrayList.size() > 0) {
					for (QuestPresent QuestPresent : ArrayList) {
						if ((!QuestPresent.getItemName().equals("")) && (QuestPresent.getItemCount() != 0)) {
								item = ItemDatabase.find(QuestPresent.getItemName());
								if (item != null)
								{
									itemInstance = ItemDatabase.newInstance(item);
									itemInstance.setEnLevel(QuestPresent.getItemEnchant());
									itemInstance.setBress(QuestPresent.getItemBress());
									itemInstance.setDefinite(true);
									pc.getInventory().append(itemInstance, QuestPresent.getItemCount());
									if (!QuestPresent.getNote().equals(""))
										ChattingController.toChatting(pc, QuestPresent.getNote(), 20);
								}
							}
						}
					}
				q = QuestController.newQuest(pc, null, QUEST_LEVEL_ATTAINMENT_10);
			}

			q = QuestController.find(pc, QUEST_LEVEL_ATTAINMENT_20);
			if (pc.getLevel() >= 20 && q == null) {
				find(pc.getClassType(), 20, ArrayList);
				if (ArrayList.size() > 0) {
					for (QuestPresent QuestPresent : ArrayList) {
						if ((!QuestPresent.getItemName().equals("")) && (QuestPresent.getItemCount() != 0)) {
								item = ItemDatabase.find(QuestPresent.getItemName());
								if (item != null)
								{
									itemInstance = ItemDatabase.newInstance(item);
									itemInstance.setEnLevel(QuestPresent.getItemEnchant());
									itemInstance.setBress(QuestPresent.getItemBress());
									itemInstance.setDefinite(true);
									pc.getInventory().append(itemInstance, QuestPresent.getItemCount());
									if (!QuestPresent.getNote().equals(""))
										ChattingController.toChatting(pc, QuestPresent.getNote(), 20);
								}
							}
						}
					}
				q = QuestController.newQuest(pc, null, QUEST_LEVEL_ATTAINMENT_20);
			}
			
			q = QuestController.find(pc, QUEST_LEVEL_ATTAINMENT_30);
			if (pc.getLevel() >= 30 && q == null) {
				find(pc.getClassType(), 30, ArrayList);
				if (ArrayList.size() > 0) {
					for (QuestPresent QuestPresent : ArrayList) {
						if ((!QuestPresent.getItemName().equals("")) && (QuestPresent.getItemCount() != 0)) {
								item = ItemDatabase.find(QuestPresent.getItemName());
								if (item != null)
								{
									itemInstance = ItemDatabase.newInstance(item);
									itemInstance.setEnLevel(QuestPresent.getItemEnchant());
									itemInstance.setBress(QuestPresent.getItemBress());
									itemInstance.setDefinite(true);
									pc.getInventory().append(itemInstance, QuestPresent.getItemCount());
									if (!QuestPresent.getNote().equals(""))
										ChattingController.toChatting(pc, QuestPresent.getNote(), 20);
								}
							}
						}
					}
				q = QuestController.newQuest(pc, null, QUEST_LEVEL_ATTAINMENT_30);
			}

			q = QuestController.find(pc, QUEST_LEVEL_ATTAINMENT_40);
			if (pc.getLevel() >= 40 && q == null) {
				find(pc.getClassType(), 40, ArrayList);
				if (ArrayList.size() > 0) {
					for (QuestPresent QuestPresent : ArrayList) {
						if ((!QuestPresent.getItemName().equals("")) && (QuestPresent.getItemCount() != 0)) {
								item = ItemDatabase.find(QuestPresent.getItemName());
								if (item != null)
								{
									itemInstance = ItemDatabase.newInstance(item);
									itemInstance.setEnLevel(QuestPresent.getItemEnchant());
									itemInstance.setBress(QuestPresent.getItemBress());
									itemInstance.setDefinite(true);
									pc.getInventory().append(itemInstance, QuestPresent.getItemCount());
									if (!QuestPresent.getNote().equals(""))
										ChattingController.toChatting(pc, QuestPresent.getNote(), 20);
								}
							}
						}
					}
				q = QuestController.newQuest(pc, null, QUEST_LEVEL_ATTAINMENT_40);
			}

			q = QuestController.find(pc, QUEST_LEVEL_ATTAINMENT_45);
			if (pc.getLevel() >= 45 && q == null) {
				find(pc.getClassType(), 45, ArrayList);
				if (ArrayList.size() > 0) {
					for (QuestPresent QuestPresent : ArrayList) {
						if ((!QuestPresent.getItemName().equals("")) && (QuestPresent.getItemCount() != 0)) {
								item = ItemDatabase.find(QuestPresent.getItemName());
								if (item != null)
								{
									itemInstance = ItemDatabase.newInstance(item);
									itemInstance.setEnLevel(QuestPresent.getItemEnchant());
									itemInstance.setBress(QuestPresent.getItemBress());
									itemInstance.setDefinite(true);
									pc.getInventory().append(itemInstance, QuestPresent.getItemCount());
									if (!QuestPresent.getNote().equals(""))
										ChattingController.toChatting(pc, QuestPresent.getNote(), 20);
								}
							}
						}
					}
				q = QuestController.newQuest(pc, null, QUEST_LEVEL_ATTAINMENT_45);
			}

			q = QuestController.find(pc, QUEST_LEVEL_ATTAINMENT_50);
			if (pc.getLevel() >= 50 && q == null) {
				find(pc.getClassType(), 50, ArrayList);
				if (ArrayList.size() > 0) {
					for (QuestPresent QuestPresent : ArrayList) {
						if ((!QuestPresent.getItemName().equals("")) && (QuestPresent.getItemCount() != 0)) {
								item = ItemDatabase.find(QuestPresent.getItemName());
								if (item != null)
								{
									itemInstance = ItemDatabase.newInstance(item);
									itemInstance.setEnLevel(QuestPresent.getItemEnchant());
									itemInstance.setBress(QuestPresent.getItemBress());
									itemInstance.setDefinite(true);
									pc.getInventory().append(itemInstance, QuestPresent.getItemCount());
									if (!QuestPresent.getNote().equals(""))
										ChattingController.toChatting(pc, QuestPresent.getNote(), 20);
								}
							}
						}
					}
				q = QuestController.newQuest(pc, null, QUEST_LEVEL_ATTAINMENT_50);
			}

			q = QuestController.find(pc, QUEST_LEVEL_ATTAINMENT_55);
			if ((pc.getLevel() >= 55) && (q == null)) {
				find(pc.getClassType(), 55, ArrayList);
				if (ArrayList.size() > 0)
					for (QuestPresent QuestPresent : ArrayList) {
						if ((!QuestPresent.getItemName().equals("")) && (QuestPresent.getItemCount() != 0))
								item = ItemDatabase.find(QuestPresent.getItemName());
								if (item != null)
								{
									itemInstance = ItemDatabase.newInstance(item);
									itemInstance.setEnLevel(QuestPresent.getItemEnchant());
									itemInstance.setBress(QuestPresent.getItemBress());
									itemInstance.setDefinite(true);
									pc.getInventory().append(itemInstance, QuestPresent.getItemCount());
									if (!QuestPresent.getNote().equals(""))
										ChattingController.toChatting(pc, QuestPresent.getNote(), 20);
								}
							}
				q = QuestController.newQuest(pc, null, QUEST_LEVEL_ATTAINMENT_55);
			}

			q = QuestController.find(pc, QUEST_LEVEL_ATTAINMENT_60);
			if ((pc.getLevel() >= 60) && (q == null)) {
				find(pc.getClassType(), 60, ArrayList);
				if (ArrayList.size() > 0)
					for (QuestPresent QuestPresent : ArrayList) {
						if ((!QuestPresent.getItemName().equals("")) && (QuestPresent.getItemCount() != 0))
								item = ItemDatabase.find(QuestPresent.getItemName());
								if (item != null)
								{
									itemInstance = ItemDatabase.newInstance(item);
									itemInstance.setEnLevel(QuestPresent.getItemEnchant());
									itemInstance.setBress(QuestPresent.getItemBress());
									itemInstance.setDefinite(true);
									pc.getInventory().append(itemInstance, QuestPresent.getItemCount());
									if (!QuestPresent.getNote().equals(""))
										ChattingController.toChatting(pc, QuestPresent.getNote(), 20);
								}
							}
				q = QuestController.newQuest(pc, null, QUEST_LEVEL_ATTAINMENT_60);
			}

			q = QuestController.find(pc, QUEST_LEVEL_ATTAINMENT_65);
			if ((pc.getLevel() >= 65) && (q == null)) {
				find(pc.getClassType(), 65, ArrayList);
				if (ArrayList.size() > 0)
					for (QuestPresent QuestPresent : ArrayList) {
						if ((!QuestPresent.getItemName().equals("")) && (QuestPresent.getItemCount() != 0))
								item = ItemDatabase.find(QuestPresent.getItemName());
								if (item != null)
								{
									itemInstance = ItemDatabase.newInstance(item);
									itemInstance.setEnLevel(QuestPresent.getItemEnchant());
									itemInstance.setBress(QuestPresent.getItemBress());
									itemInstance.setDefinite(true);
									pc.getInventory().append(itemInstance, QuestPresent.getItemCount());
									if (!QuestPresent.getNote().equals(""))
										ChattingController.toChatting(pc, QuestPresent.getNote(), 20);
								}
							}
				q = QuestController.newQuest(pc, null, QUEST_LEVEL_ATTAINMENT_65);
			}



			q = QuestController.find(pc, QUEST_LEVEL_ATTAINMENT_70);
			if ((pc.getLevel() >= 70) && (q == null)) {
				find(pc.getClassType(), 70, ArrayList);
				if (ArrayList.size() > 0)
					for (QuestPresent QuestPresent : ArrayList) {
						if ((!QuestPresent.getItemName().equals("")) && (QuestPresent.getItemCount() != 0))
								item = ItemDatabase.find(QuestPresent.getItemName());
								if (item != null)
								{
									itemInstance = ItemDatabase.newInstance(item);
									itemInstance.setEnLevel(QuestPresent.getItemEnchant());
									itemInstance.setBress(QuestPresent.getItemBress());
									itemInstance.setDefinite(true);
									pc.getInventory().append(itemInstance, QuestPresent.getItemCount());
									if (!QuestPresent.getNote().equals(""))
										ChattingController.toChatting(pc, QuestPresent.getNote(), 20);
								}
							}
				q = QuestController.newQuest(pc, null, QUEST_LEVEL_ATTAINMENT_70);
			}

			q = QuestController.find(pc, QUEST_LEVEL_ATTAINMENT_75);
			if ((pc.getLevel() >= 75) && (q == null)) {
				find(pc.getClassType(), 75, ArrayList);
				if (ArrayList.size() > 0)
					for (QuestPresent QuestPresent : ArrayList) {
						if ((!QuestPresent.getItemName().equals("")) && (QuestPresent.getItemCount() != 0))
								item = ItemDatabase.find(QuestPresent.getItemName());
								if (item != null)
								{
									itemInstance = ItemDatabase.newInstance(item);
									itemInstance.setEnLevel(QuestPresent.getItemEnchant());
									itemInstance.setBress(QuestPresent.getItemBress());
									itemInstance.setDefinite(true);
									pc.getInventory().append(itemInstance, QuestPresent.getItemCount());
									if (!QuestPresent.getNote().equals(""))
										ChattingController.toChatting(pc, QuestPresent.getNote(), 20);
								}
							}
				q = QuestController.newQuest(pc, null, QUEST_LEVEL_ATTAINMENT_75);
			}

			q = QuestController.find(pc, QUEST_LEVEL_ATTAINMENT_80);
			if ((pc.getLevel() >= 80) && (q == null)) {
				find(pc.getClassType(), 80, ArrayList);
				if (ArrayList.size() > 0)
					for (QuestPresent QuestPresent : ArrayList) {
						if ((!QuestPresent.getItemName().equals("")) && (QuestPresent.getItemCount() != 0))
								item = ItemDatabase.find(QuestPresent.getItemName());
								if (item != null)
								{
									itemInstance = ItemDatabase.newInstance(item);
									itemInstance.setEnLevel(QuestPresent.getItemEnchant());
									itemInstance.setBress(QuestPresent.getItemBress());
									itemInstance.setDefinite(true);
									pc.getInventory().append(itemInstance, QuestPresent.getItemCount());
									if (!QuestPresent.getNote().equals(""))
										ChattingController.toChatting(pc, QuestPresent.getNote(), 20);
								}
							}
				q = QuestController.newQuest(pc, null, QUEST_LEVEL_ATTAINMENT_80);
			}

			q = QuestController.find(pc, QUEST_LEVEL_ATTAINMENT_85);
			if ((pc.getLevel() >= 85) && (q == null)) {
				find(pc.getClassType(), 85, ArrayList);
				if (ArrayList.size() > 0)
					for (QuestPresent QuestPresent : ArrayList) {
						if ((!QuestPresent.getItemName().equals("")) && (QuestPresent.getItemCount() != 0))
								item = ItemDatabase.find(QuestPresent.getItemName());
								if (item != null)
								{
									itemInstance = ItemDatabase.newInstance(item);
									itemInstance.setEnLevel(QuestPresent.getItemEnchant());
									itemInstance.setBress(QuestPresent.getItemBress());
									itemInstance.setDefinite(true);
									pc.getInventory().append(itemInstance, QuestPresent.getItemCount());
									if (!QuestPresent.getNote().equals(""))
										ChattingController.toChatting(pc, QuestPresent.getNote(), 20);
								}
							}
				q = QuestController.newQuest(pc, null,  QUEST_LEVEL_ATTAINMENT_85);
			}
		}
		catch (Exception e)
		{
			System.printf("%s : checkLevelCompensation(PcInstance pc, String name)\r\n", new Object[] { QuestPresentDatabase.class.toString() });
			System.println(e);
		}
	}

	public static void find(int classtype, int level, List<QuestPresent> paramList) {
		paramList.clear();
		for (QuestPresent QuestPresent : list) {
			if (((QuestPresent.getClassType() == classtype) || (QuestPresent.getClassType() == -1)) && (QuestPresent.getLevel() == level))
				paramList.add(QuestPresent);
		}
	}
}

/* Location:           D:\orim.jar
 * Qualified Name:     lineage.database.QuestPresentDatabase
 * JD-Core Version:    0.6.0
 */
