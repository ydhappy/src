package lineage.world.object.item;

import java.util.Random;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class RandomEnchantBox extends ItemInstance {

	private static Random _random = new Random(System.nanoTime());
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new RandomEnchantBox();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		// 아이템지정은 디비 아이템이름 복붙 
		String [] weapon = { "일본도","레이피어","크로스 보우", "힘의 지팡이","다마스커스 검", "메일 브레이커", "마나의 지팡이"
								,"황금 지휘봉"};
		String [] armor = { "티셔츠","보호 망토","마법 망토","기사의 면갑","마법 방어 사슬 갑옷","마법 방어 투구","엘름의 축복","파워 글로브","요정족 판금 갑옷",
							"요정족 티셔츠", "마력서"};
		Item i = null;
		ItemInstance box = null;
		if(getItem().getName().equalsIgnoreCase("랜덤인챈무기상자")){
			i = ItemDatabase.find(weapon[_random.nextInt(weapon.length)]);
			box = ItemDatabase.newInstance(i);
		} else {
			i = ItemDatabase.find(armor[_random.nextInt(armor.length)]);
			box = ItemDatabase.newInstance(i);
		}
		
		int chance = Util.random(0, 1000);
		int enchantLevel;
		if(chance < 900 && chance > 300){
			enchantLevel = Util.random(0, 2);
		} else if(chance <= 300 & chance >=100){
			enchantLevel = Util.random(3, 5);
		} else if(chance < 100 & chance >= 50){
			enchantLevel = Util.random(6, 7);
		} else if(chance < 50 && chance >=45){
			enchantLevel = 7;
		} else {
			enchantLevel = 0;
		}
		
		box.setEnLevel(enchantLevel);
		box.setDefinite(true);
		cha.getInventory().append(box,1);
		cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 143, getName(), "+"+box.getEnLevel() + " " +box.getName()));
		cha.getInventory().count(this, getCount()-1, true);
	}
}