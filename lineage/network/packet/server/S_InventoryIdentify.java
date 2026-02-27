package lineage.network.packet.server;

import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.world.object.instance.ItemInstance;

public class S_InventoryIdentify extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, ItemInstance item, String type, int msg){
		if(bp == null)
			bp = new S_InventoryIdentify(item, type, msg);
		else
			((S_InventoryIdentify)bp).clone(item, type, msg);
		return bp;
	}
	
	public S_InventoryIdentify(ItemInstance item, String type, int msg){
		clone(item, type, msg);
	}
	
	public void clone(ItemInstance item, String type, int msg){
		clear();
		
		writeC(Opcodes.S_OPCODE_ITEMIDENTIFY);
		writeH(msg);			// 상세설명 메세지 번호
		StringBuffer name = new StringBuffer();
		if(item.getBress() == 0)
			name.append("축복받은 ");
		else if(item.getBress() == 1)
			name.append("");
		else
			name.append("저주받은 ");
		
		if(type.equalsIgnoreCase("weapon")){
			writeH(134);		// 아이템 정보 출력 메세지번호 부분
			writeC(0x05);		// 문자열 갯수
			writeS(name.toString() + "+" +item.getEnLevel() +" "+item.getName());
			writeS(item.getItem().getDmgMin()
					+ "+" + item.getEnLevel());
			writeS(item.getItem().getDmgMax()
					+ "+" + item.getEnLevel());
			writeS(String.valueOf(item.getDurability()));
			writeS(String.valueOf(item.getItem().getWeight()));
		}else if(type.equalsIgnoreCase("armor")){
			writeH(135);		// 아이템 정보 출력 메세지번호 부분
			writeC(0x04);		// 문자열 갯수
			writeS(name.toString() + "+" +item.getEnLevel() +" "+item.getName());
			writeS(String.valueOf(item.getItem().getAc() + "+" + item.getEnLevel()));
			writeS(String.valueOf(item.getItem().getWeight()));
			//writeS(String.valueOf(item.getDurability()));
		}else if(type.equalsIgnoreCase("food")){
			writeH(136);		// 아이템 정보 출력 메세지번호 부분
			writeC(0x03);		// 문자열 갯수
			writeS(item.getName());
			writeS("3");
			writeS(String.valueOf(item.getItem().getWeight()));
		}else if(type.equalsIgnoreCase("have")){
			writeH(137);
			writeC(0x03);
			writeS(item.getName());
			writeS(String.valueOf(item.getQuantity()));
			writeS(String.valueOf(item.getItem().getWeight()));
		}else{
			writeH(138);
			writeC(0x02);
			writeS(item.getName());
			writeS(String.valueOf(item.getItem().getWeight()));
		}
	}
	
}
