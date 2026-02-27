package lineage.world.object.item.bundle;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.ItemBundle;
import lineage.database.ItemBundleDatabase;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class UnicBundle extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new UnicBundle();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if(isLvCheck(cha)) {
			if(isClassCheck(cha)) {
				// 아이템 지급.
				List<ItemBundle> list = new ArrayList<ItemBundle>();
				ItemBundleDatabase.find(list, getItem().getName());
				for(ItemBundle ib : list){
					
						// 메모리 생성 및 초기화.
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find(ib.getItem()));
						ii.setCount( Util.random(ib.getItemCountMin(), ib.getItemCountMax()) );
						ii.setBress(ib.getItemBless());
						ii.setEnLevel(ib.getItemEnchant());
						
						if(ii.getItem().getType1().equalsIgnoreCase("weapon")){
							ii.setAdd_Min_Dmg(Util.random(30, 70));
							ii.setAdd_Max_Dmg(Util.random(30, 70));
							ii.setAdd_Str(Util.random(5, 10));
							ii.setAdd_Dex(Util.random(5, 10));
							ii.setAdd_Con(Util.random(5, 10));
							ii.setAdd_Int(Util.random(5, 10));
							ii.setAdd_Wiz(Util.random(5, 10));
							ii.setAdd_Cha(Util.random(5, 10));
							ii.setAdd_Hp(Util.random(500, 1000));
							ii.setAdd_Mana(Util.random(500, 1000));
							ii.setAdd_Hpstell(Util.random(14, 17));
							ii.setAdd_Manastell(Util.random(4, 5));
						}else if(ii.getItem().getType1().equalsIgnoreCase("armor")){
							ii.setAdd_Str(Util.random(5, 10));
							ii.setAdd_Dex(Util.random(5, 10));
							ii.setAdd_Con(Util.random(5, 10));
							ii.setAdd_Int(Util.random(5, 10));
							ii.setAdd_Wiz(Util.random(5, 10));
							ii.setAdd_Cha(Util.random(5, 10));
							ii.setAdd_Hp(Util.random(300, 500));
							ii.setAdd_Mana(Util.random(300, 500));
							ii.setAdd_Hpstell(Util.random(14, 17));
							ii.setAdd_Manastell(Util.random(4, 5));
						}
						
						ii.setDefinite(true);
						
						//cha.getInventory().append(ii, ii.getCount());
						cha.toGiveItem(this, ii, ii.getCount());
						// \f1%0%s 당신에게 %1%o 주었습니다.
					//	cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 143, getName(), ii.toString()));
						// 메모리 재사용.
						//ItemDatabase.setPool(ii);

				}
				// 수량 하향.
				cha.getInventory().count(this, getCount()-1, true);
			} else {
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 264));
			}
		}
	}

}
