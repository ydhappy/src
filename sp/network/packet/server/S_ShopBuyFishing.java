package sp.network.packet.server;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Shop;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.server.S_Inventory;
import lineage.share.Lineage;
import lineage.world.controller.DogRaceController;
import lineage.world.controller.SlimeRaceController;
import lineage.world.object.instance.DograceInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.ShopInstance;
import lineage.world.object.instance.SlimeraceInstance;
import sp.controller.FishingController;

public class S_ShopBuyFishing extends S_Inventory {

	static synchronized public BasePacket clone(BasePacket bp, ShopInstance shop, PcInstance pc){
		if(bp == null)
			bp = new S_ShopBuyFishing(shop, pc);
		else
			((S_ShopBuyFishing)bp).toClone(shop, pc);
		return bp;
	}
	
	public S_ShopBuyFishing(ShopInstance shop, PcInstance pc){
		toClone(shop, pc);
	}

	public void toClone(ShopInstance shop, PcInstance pc){
		clear();

		writeC(Opcodes.S_OPCODE_SHOPBUY);
		writeD(shop.getObjectId());
		
		// 일반상점 구성구간.
		toShop(shop, pc);
	}
	
	private void toShop(ShopInstance shop, PcInstance pc) {
		// 유효성 검사.
		List<Shop> list = new ArrayList<Shop>();
		for(Shop s : shop.getNpc().getShop_list()) {
			//
			if(!s.isItemBuy())
				continue;
			if(s.getItemName().equalsIgnoreCase("미끼")) {
				try {
					if(!FishingController.isBaitBuy(pc.getClient().getAccountIp()))
						continue;
				} catch (Exception e) {
					continue;
				}
			}
			//
			list.add(s);
		}
		
		// 출력.
		writeH(list.size());
		for( Shop s : list ) {
			if(s.isItemBuy()){
				Item i = ItemDatabase.find(s.getItemName());
				if(i != null){
					writeD(s.getUid());
					writeH(i.getInvGfx());
					if(s.getPrice() != 0)
						writeD( shop.getTaxPrice(s.getPrice(), false) );
					else
						writeD( shop.getTaxPrice(i.getShopPrice(), false) );

					// 여기가 이름 세팅 하는 부분.
					StringBuffer name = new StringBuffer();
					if(s.getItemBress() == 0)
						name.append("[축] ");
					if(s.getItemBress() == 2)
						name.append("[저주] ");
					if(s.getItemEnLevel() != 0)
						name.append(s.getItemEnLevel()<0 ? "-" : "+").append(s.getItemEnLevel()).append(" ");
					if(shop instanceof DograceInstance){
						name.append(DogRaceController.RacerTicketName(s.getUid()));
					}else if(shop instanceof SlimeraceInstance){
						name.append(SlimeRaceController.SlimeRaceTicketName(s.getUid()));
					}else{
						if(s.getItemCount()>1) {
							name.append(String.format("%s (%d)", i.getName(), s.getItemCount()));
						}else{
							name.append(i.getName());
						}
					}
					// 실제 표현 문구.
					writeS(name.toString());
					// 여기까지.
					
					if(Lineage.server_version>144){
						if(i.getType1().equalsIgnoreCase("armor")){
							//toArmor(i, null,0,0,0,0,0,0,0,0,0,0,0,0, 0, 0);
						}else if(i.getType1().equalsIgnoreCase("weapon")){
							//toWeapon(i, null,0,0,0,0,0,0,0,0,0,0,0,0, 0, 0);
						}else{
							toEtc(i, (int)i.getWeight());
						}
					}
				}
			}
		}
	}
}
