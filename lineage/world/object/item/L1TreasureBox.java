//package lineage.world.object.item;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.CopyOnWriteArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.Unmarshaller;
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//
//import lineage.bean.lineage.Inventory;
//import lineage.database.ItemDatabase;
//import lineage.network.packet.BasePacketPooling;
//import lineage.network.packet.server.S_Message;
//import lineage.share.Lineage;
//import lineage.world.controller.ChattingController;
//import lineage.world.controller.CraftController;
//import lineage.world.object.instance.ItemInstance;
//import lineage.world.object.instance.PcInstance;
//
//@XmlAccessorType(XmlAccessType.FIELD)
//public class L1TreasureBox {
//
//	private static Logger _log = Logger.getLogger(L1TreasureBox.class.getName());
//
//	@XmlAccessorType(XmlAccessType.FIELD)
//	@XmlRootElement(name = "TreasureBoxList")
//	private static class TreasureBoxList implements Iterable<L1TreasureBox> {
//		@XmlElement(name = "TreasureBox")
//		private List<L1TreasureBox> _list;
//
//		public Iterator<L1TreasureBox> iterator() {
//			return _list.iterator();
//		}
//	}
//
//	@XmlAccessorType(XmlAccessType.FIELD)
//	private static class Item {
//		@XmlAttribute(name = "ItemId")
//		private int _itemId;
//
//		@XmlAttribute(name = "Count")
//		private int _count;
//
//		@XmlAttribute(name = "Enchant")
//		private int _enchant;
//
//		@XmlAttribute(name = "Attr")
//		private int _attr;
//
//		@XmlAttribute(name = "Identi")
//		private boolean _identified;
//
//		@XmlAttribute(name = "Bless")
//		private int _bless;
//		
//		private int _chance;
//
//		@XmlAttribute(name = "Chance")
//		private void setChance(double chance) {
//			_chance = (int) (chance * 10000);
//		}
//
//		public int getItemId() {
//			return _itemId;
//		}
//
//		public int getCount() {
//			return _count;
//		}
//
//		// ������ ��þƮ ����
//		public int getEnchant() {
//			return _enchant;
//		}
//
//		public int getAttr() {
//			return _attr;
//		}
//		
//		public int getBless() {
//			return _bless;
//		}
//
//		public boolean getIdentified() {
//			return _identified;
//		}
//
//		public double getChance() {
//			return _chance;
//		}
//	}
//
//	private static enum TYPE {
//		RANDOM, SPECIFIC, RANDOM_SPECIFIC
//	}
//
//	private static final String PATH = "./TreasureBox.xml";
//
//	private static final HashMap<Integer, L1TreasureBox> _dataMap = new HashMap<Integer, L1TreasureBox>();
//
//	public static L1TreasureBox get(int id) {
//		return _dataMap.get(id);
//	}
//
//	@XmlAttribute(name = "ItemId")
//	private int _boxId;
//
//	@XmlAttribute(name = "Type")
//	private TYPE _type;
//
//	private int getBoxId() {
//		return _boxId;
//	}
//
//	private TYPE getType() {
//		return _type;
//	}
//
//	@XmlElement(name = "Item")
//	private CopyOnWriteArrayList<Item> _items;
//
//	private List<Item> getItems() {
//		return _items;
//	}
//
//	private int _totalChance;
//
//	private int getTotalChance() {
//		return _totalChance;
//	}
//
//	private void init() {
//		for (Item each : getItems()) {
//			_totalChance += each.getChance();
//			if (ItemDatabase.find_ItemId(each.getItemId()) == null) {
//				getItems().remove(each);
//				_log.warning("아이템 ID " + each.getItemId() + " 의 템플릿이 발견되지 않았습니다.");
//			}
//		}
//		if (getType() == TYPE.RANDOM && getTotalChance() != 1000000) {
//			_log.warning("ID " + getBoxId() + "의 확률의 합계가 100%가 되지 않습니다.");
//		}
//	}
//
//	public static void load() {
//		try {
//			JAXBContext context = JAXBContext.newInstance(L1TreasureBox.TreasureBoxList.class);
//
//			Unmarshaller um = context.createUnmarshaller();
//
//			File file = new File(PATH);
//			TreasureBoxList list = (TreasureBoxList) um.unmarshal(file);
//
//			for (L1TreasureBox each : list) {
//				each.init();
//				_dataMap.put(each.getBoxId(), each);
//			}
//		} catch (Exception e) {
//			_log.log(Level.SEVERE, PATH + "의 로드에 실패.", e);
//			System.exit(0);
//		}
//	}
//
//	public boolean open(PcInstance pc) {
//		ItemInstance item = null;
//		Random random = null;
//		if (getType().equals(TYPE.SPECIFIC)) {
//			for (Item each : getItems()) {
//				item = ItemDatabase.newInstance(ItemDatabase.find_ItemId(each.getItemId()));
//				if (item != null && !isOpen(pc)) {
//					item.setCount(each.getCount());
//					item.setEnLevel(each.getEnchant());
//					item.setDefinite(each.getIdentified());
//					item.setBress(each.getBless());
//					if(each.getAttr() == 1){
//						item.setEnFire(1);
//					} else if(each.getAttr() == 2){
//						item.setEnWater(1);
//					} else if(each.getAttr() == 3){
//						item.setEnEarth(1);
//					} else if(each.getAttr() == 4){
//						item.setEnWind(1);
//					}
//					storeItem(pc, item);
//				}
//			}
//
//		} else if (getType().equals(TYPE.RANDOM)) {
//			random = new Random(System.nanoTime());
//			int chance = 0;
//			int r = random.nextInt(getTotalChance());
//			for (Item each : getItems()) {
//				chance += each.getChance();
//				if (r < chance) {
//					item = ItemDatabase.newInstance(ItemDatabase.find_ItemId(each.getItemId()));
//					if (item != null && !isOpen(pc)) {
//						item.setCount(each.getCount());
//						item.setEnLevel(each.getEnchant());
//						item.setBress(each.getBless());
//						item.setDefinite(each.getIdentified());
//						if(each.getAttr() == 1){
//							item.setEnFire(1);
//						} else if(each.getAttr() == 2){
//							item.setEnWater(1);
//						} else if(each.getAttr() == 3){
//							item.setEnEarth(1);
//						} else if(each.getAttr() == 4){
//							item.setEnWind(1);
//						}
//						storeItem(pc, item);
//					}
//					break;
//				}
//			}
//		} else if (getType().equals(TYPE.RANDOM_SPECIFIC)) {
//			random = new Random(System.nanoTime());
//			int chance = 0;
//
//			int r = random.nextInt(getTotalChance());
//
//			for (Item each : getItems()) {
//				if (each.getChance() == 0) {
//					item = ItemDatabase.newInstance(ItemDatabase.find_ItemId(each.getItemId()));
//					if (item != null && !isOpen(pc)) {
//						item.setCount(each.getCount());
//						item.setEnLevel(each.getEnchant());
//						item.setDefinite(each.getIdentified());
//						item.setBress(each.getBless());
//						if(each.getAttr() == 1){
//							item.setEnFire(1);
//						} else if(each.getAttr() == 2){
//							item.setEnWater(1);
//						} else if(each.getAttr() == 3){
//							item.setEnEarth(1);
//						} else if(each.getAttr() == 4){
//							item.setEnWind(1);
//						}
//						storeItem(pc, item);
//						
//					}
//					continue;
//				}
//				chance += each.getChance();
//				if (r < chance) {
//					item = ItemDatabase.newInstance(ItemDatabase.find_ItemId(each.getItemId()));
//					if (item != null && !isOpen(pc)) {
//						item.setCount(each.getCount());
//						item.setEnLevel(each.getEnchant());
//						item.setDefinite(each.getIdentified());
//						item.setBress(each.getBless());
//						if(each.getAttr() == 1){
//							item.setEnFire(1);
//						} else if(each.getAttr() == 2){
//							item.setEnWater(1);
//						} else if(each.getAttr() == 3){
//							item.setEnEarth(1);
//						} else if(each.getAttr() == 4){
//							item.setEnWind(1);
//						}
//						storeItem(pc, item);
//					}
//					break;
//				}
//			}
//		}
//
//		if (item == null) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//
//	private boolean isOpen(PcInstance pc) {
//		if (!pc.getInventory().isWeightPercent(82)) {
//			ChattingController.toChatting(pc, "무게가 너무 무겁습니다.", 20);
//			return true;
//		}
//		return false;
//	}
//	
//	private static void storeItem(PcInstance pc, ItemInstance item) {
//		CraftController.toCraftBox(pc, item.getItem(), item.getCount(), item.isDefinite(), item.getEnLevel(),item.getBress());
//		pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 143, "아이템상자", item.getName()+"("+item.getCount()+")"));
//	
//	}
//}
