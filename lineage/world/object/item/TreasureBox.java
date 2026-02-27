//package lineage.world.object.item;
//
//import lineage.network.packet.ClientBasePacket;
//import lineage.share.Lineage;
//import lineage.world.controller.ChattingController;
//import lineage.world.object.Character;
//import lineage.world.object.instance.ItemInstance;
//import lineage.world.object.instance.PcInstance;
//
//public class TreasureBox extends ItemInstance {
//
//	static synchronized public ItemInstance clone(ItemInstance item){
//		if(item == null)
//			item = new TreasureBox();
//		return item;
//	}
//	
//	@Override
//	public void toClick(Character cha, ClientBasePacket cbp){
//		L1TreasureBox box = L1TreasureBox.get(item.getItemId());
//		PcInstance pc = (PcInstance) cha;
//		if (!pc.getInventory().isWeightPercent(82)) {
//			ChattingController.toChatting(pc, "무게가 너무 무겁습니다.", 20);
//			return ;
//		}
//		
//		int number = 0;
//		for(ItemInstance item : pc.getInventory().getList()){
//			number+=1;
//		}
//		
//		if(number>Lineage.inventory_max){
//			ChattingController.toChatting(pc, "인벤토리 갯수를 초과하 였습니다.", 20);
//			return;
//		}
//		//for(잠시만용)
//		
//		
//		//if(pc.getInventory().getList())
//		
//		if (box != null) {
//			if (box.open(pc)) {
//				
//				pc.getInventory().count(this, getCount()-1, true);
//			}
//		}
//		}
//	}
