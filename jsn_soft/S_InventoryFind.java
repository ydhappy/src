package jsn_soft;

import lineage.bean.database.Item;
import lineage.bean.database.Shop;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.server.S_Inventory;
import lineage.share.Lineage;
import lineage.world.controller.DogRaceController;
import lineage.world.controller.SlimeRaceController;
import lineage.world.object.object;
import lineage.world.object.instance.DograceInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.ShopInstance;
import lineage.world.object.instance.SlimeraceInstance;

public class S_InventoryFind extends S_Inventory {

	static public BasePacket clone(BasePacket bp, object shop) {
		if (bp == null)
			bp = new S_InventoryFind(shop);
		else
			((S_InventoryFind) bp).toClone(shop);
		return bp;
	}

	// 와서
	public S_InventoryFind(object shop, Object... opt) {
		toClone(shop, opt);
	}

	public void toClone(object shop, Object... opt) {
		clear();

		writeC(Opcodes.S_OPCODE_SHOPBUY);
		writeD(shop.getObjectId());

		// 일반상점 구성구간.
		if (shop instanceof ShopInstance)
			toShop((ShopInstance) shop);
		else {
			if (opt.length > 0)
				writeB((byte[]) opt[0]);
		}
	}

	private void toShop(ShopInstance shop) {
		writeH(shop.getNpc().getBuySize());

		for (Shop s : shop.getNpc().getShop_list()) {
			if (s.isItemBuy()) {
				Item i = ItemDatabase.find(s.getItemName());
				if (i != null) {
					writeD(s.getUid());
					writeH(i.getInvGfx());
					if (s.getPrice() != 0)
						writeD(shop.getTaxPrice(s.getPrice(), false));
					else
						writeD(shop.getTaxPrice(i.getShopPrice(), false));

					StringBuffer name = new StringBuffer();
					if (s.getItemBress() == 0)
						name.append("[축]");
					if (s.getItemBress() == 2)
						name.append("[저주]");
					if (s.getItemEnLevel() != 0)
						name.append(s.getItemEnLevel() > 0 ? "+" : "").append(s.getItemEnLevel()).append(" ");
					if (shop instanceof DograceInstance) {
						name.append(DogRaceController.RacerTicketName(s.getUid()));
					} else if (shop instanceof SlimeraceInstance) {
						name.append(SlimeRaceController.SlimeRaceTicketName(s.getUid()));
					} else {
						if (s.getItemCount() > 1) {
							name.append(String.format("%s (%d)", i.getNameId(), s.getItemCount()));
						} else {
							name.append(i.getNameId());
						}
					}
					writeS(name.toString());
					// 상점 탭 구분.
					// : 주문서 - 6, 7, 8, 9
					// : 물약 - 51
					if (Lineage.server_version >= 380) {
						if (i.getType1().equalsIgnoreCase("weapon"))
							writeD(1);
						else if (i.getType1().equalsIgnoreCase("armor"))
							writeD(2);
						else
							writeD(0);
					}
					//
					if (Lineage.server_version > 144) {
						if (i.getType1().equalsIgnoreCase("armor")) {
							if (i.getName().equalsIgnoreCase("엘름의 축복"))
								toArmor(i, 0, s.getItemEnLevel(), (int) i.getWeight(), s.getItemEnLevel() > 4 ? (s.getItemEnLevel() - 4) * i.getEnchantMr() : 0, s.getItemBress(),
										s.getItemEnLevel() * i.getEnchantStunDefense(), s.getItemEnLevel() * i.getEnchantSp(), s.getItemEnLevel() * i.getEnchantReduction());
							else
								toArmor(i, 0, s.getItemEnLevel(), (int) i.getWeight(), s.getItemEnLevel() * i.getEnchantMr(), s.getItemBress(),
										s.getItemEnLevel() * i.getEnchantStunDefense(), s.getItemEnLevel() * i.getEnchantSp(), s.getItemEnLevel() * i.getEnchantReduction());				
						} else if (i.getType1().equalsIgnoreCase("weapon")) {
							toWeapon(i, 0, s.getItemEnLevel(), (int) i.getWeight(), s.getItemBress(), s.getItemEnLevel() * i.getEnchantMr(), 
									s.getItemEnLevel() * i.getEnchantStunDefense(), s.getItemEnLevel() * i.getEnchantSp(), s.getItemEnLevel() * i.getEnchantReduction());
						} else {
							toEtc(i, (int) i.getWeight());
						}
					}
				}
			}
		}

		writeH(0x07);// 0x00:kaimo 0x01:pearl 0x07:adena
	}

}

/*
 * 포탄 >> 0 고기 >> 0 체력 회복제 >> 51 고급 체력 회복제 >> 51 강력 체력 회복제 >> 51 농축 체력 회복제 >> 51
 * 농축 고급 체력 회복제 >> 51 농축 강력 체력 회복제 >> 51 신속 체력 회복제 >> 51 신속 고급 체력 회복제 >> 51 신속
 * 강력 체력 회복제 >> 51 속도향상 물약 >> 51 용기의 물약 >> 51 엘븐 와퍼 >> 51 유그드라 열매 >> 51 악마의 피 >>
 * 51 지혜의 물약 >> 51 마나 회복 물약 >> 51 해독제 >> 51 에바의 축복 >> 51 눈멀기 물약 >> 51 확인 주문서 >>
 * 7 변신 주문서 >> 16 부활 주문서 >> 8 순간이동 주문서 >> 6 귀환 주문서 >> 9 혈맹 귀환 주문서 >> 9 마력의 돌 >>
 * 0 정령옥 >> 0 흑마석 >> 0 흑요석 >> 0 각인의 뼈조각 >> 0 속성석 >> 0 숫돌 >> 14 펫 호루라기 >> 15 수련자의
 * 허브 >> 0 수련자의 체력 회복제 >> 51 수련자의 마나 회복 물약 >> 51 수련자의 해독제 >> 51 수련자의 속도향상 물약 >>
 * 51 수련자의 확인 주문서 >> 7 수련자의 순간이동 주문서 >> 6 수련자의 귀환 주문서 >> 9 수련자의 혈맹 귀환 주문서 >> 9
 * 수련자의 저주 풀기 주문서 >> 0 수련자의 숫돌 >> 14 수련자의 랜턴 >> 0 에바의 축복 >> 51 펫 호루라기 >> 15 봉인
 * 주문서 >> 14 수련자의 단검 >> 1 수련자의 한손검 >> 1 수련자의 양손검 >> 1 수련자의 도끼 >> 1 수련자의 창 >> 1
 * 수련자의 활 >> 1 수련자의 석궁 >> 1 수련자의 지팡이 >> 1 수련자의 크로우 >> 1 수련자의 이도류 >> 1 수련자의 견고한
 * 투구 >> 22 수련자의 견고한 갑옷 >> 2 수련자의 견고한 망토 >> 19 수련자의 견고한 장갑 >> 20 수련자의 견고한 부츠 >>
 * 21 수련자의 견고한 방패 >> 25 수련자의 신성한 투구 >> 22 수련자의 신성한 로브 >> 2 수련자의 신성한 망토 >> 19
 * 수련자의 신성한 장갑 >> 20 수련자의 신성한 부츠 >> 21 수련자의 신성한 방패 >> 25
 */
