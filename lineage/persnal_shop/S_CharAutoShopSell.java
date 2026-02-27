package lineage.persnal_shop;

import java.util.ArrayList;
import java.util.List;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.Candle;
import lineage.world.object.item.DogCollar;
import lineage.world.object.item.Letter;
import lineage.world.object.item.RaceTicket;
import lineage.world.object.item.pet.PetArmor;
import lineage.world.object.item.pet.PetWeapon;
import lineage.world.object.item.wand.EbonyWand;
import lineage.world.object.item.wand.EbonyWand2;
import lineage.world.object.item.wand.MapleWand;
import lineage.world.object.item.wand.PineWand;
import lineage.world.object.item.weapon.Alice;

public class S_CharAutoShopSell extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, PcInstance pc, List<ItemInstance> list, PersnalShopInstance shop) {
		if (bp == null)
			bp = new S_CharAutoShopSell(pc, list, shop);
		else
			((S_CharAutoShopSell) bp).toClone(pc, list, shop);
		return bp;
	}

	public S_CharAutoShopSell(PcInstance pc, List<ItemInstance> list, PersnalShopInstance shop) {
		toClone(pc, list, shop);
	}

	public void toClone(PcInstance pc, List<ItemInstance> list, PersnalShopInstance shop) {
		clear();

		writeC(Opcodes.S_OPCODE_WAREHOUSE);
		writeD(pc.getObjectId());

		List<ItemInstance> new_list = new ArrayList<ItemInstance>();
		
		for (ItemInstance wh : list) {
			if (wh.isEquipped())
				continue;

			if (wh instanceof MapleWand || wh instanceof PineWand || wh instanceof EbonyWand|| wh instanceof EbonyWand2|| wh instanceof RaceTicket
					 || wh instanceof Letter || wh instanceof DogCollar) {
				continue;
			}
			
			if (wh.getItem().getType1().equalsIgnoreCase("weapon") || wh.getItem().getType1().equalsIgnoreCase("armor"))
				continue;

			if (wh.getItem().getName().equalsIgnoreCase("아데나") || wh.getInnRoomKey() > 0)
				continue;

			if (!wh.getItem().isTrade())
				continue;
			
			boolean check_shop_item = false;
			for(PersnalShopItem shopItem : shop.getSellList()) {
				if(shopItem.getItem_id() == wh.getItem().getItemId()) {
					check_shop_item = true;
					break;
				}
			}
			
			if(check_shop_item)
				continue;
			
			new_list.add(wh);
		}

		writeH(new_list.size());
		writeC(3);
		readDB(pc, new_list); // 창고 목록
	}

	private void readDB(PcInstance pc, List<ItemInstance> list) {
		for (ItemInstance wh : list) {
			int type = 0;
			if (wh instanceof ItemWeaponInstance)
				type = 1;
			else if (wh instanceof ItemArmorInstance)
				type = 2;
			else
				type = 3;

			writeD(wh.getObjectId()); // 번호
			writeC(type); // 타입
			writeH(wh.getItem().getInvGfx()); // GFX 아이디
			writeC(wh.getBress()); // 1: 보통 0: 축 2: 저주
			writeD(200000); // 구입희망 갯수를 무한으로 해주기위해 2억으로 표기
			writeC(wh.isDefinite() ? 1 : 0); // 1: 확인 0: 미확인
			writeS(getName(wh)); // 이름
			if (Lineage.server_version >= 380)
				writeC(0x00);
		}
		writeD(0);
	}

	protected String getName(ItemInstance item) {
		StringBuffer sb = new StringBuffer();
		if (item.getItem().getNameIdNumber() == 1075 && item.getItem().getInvGfx() != 464) {
			Letter letter = (Letter) item;
			sb.append(letter.getFrom());
			sb.append(" : ");
			sb.append(letter.getSubject());
		} else {
			if (item.isDefinite() && (item instanceof ItemWeaponInstance || item instanceof ItemArmorInstance)) {
				// 속성 인첸 표현.
				String element_name = null;
				Integer element_en = 0;
				if (item.getEnWind() > 0) {
					element_name = "풍령";
					element_en = item.getEnWind();
				}
				if (item.getEnEarth() > 0) {
					element_name = "지령";
					element_en = item.getEnEarth();
				}
				if (item.getEnWater() > 0) {
					element_name = "수령";
					element_en = item.getEnWater();
				}
				if (item.getEnFire() > 0) {
					element_name = "화령";
					element_en = item.getEnFire();
				}
				if (element_name != null) {
					sb.append(element_name).append(":").append(element_en).append("단");
					sb.append(" ");
				}
				// 인첸 표현.
				if (item.getEnLevel() >= 0) {
					sb.append("+");
				}
				sb.append(item.getEnLevel());
				sb.append(" ");
			}
			// 앨리스
			if (item instanceof Alice)
				sb.append("(").append(((Alice) item).getLevel()).append(")");
			if (!item.getItem().getNameId().startsWith("$"))
				sb.append(item.getName());
			else
				sb.append(item.isDefinite() && item.getItem().getNameIdCheck().length() > 0
						? item.getItem().getNameIdCheck()
						: item.getItem().getNameId());

			// 착용중인 아이템 표현
			if (item.isEquipped() && !(item instanceof PetArmor || item instanceof PetWeapon)) {
				if (item instanceof ItemWeaponInstance) {
					sb.append(" ($9)");
				} else if (item instanceof ItemArmorInstance) {
					sb.append(" ($117)");
				} else if (item instanceof Candle) {
					// 양초, 등잔
					sb.append(" ($10)");
				}
			}

			if (item.getCount() > 1) {
				sb.append(" (");
				sb.append(item.getCount());
				sb.append(")");
			}
			if (item.isDefinite()
					&& (item instanceof MapleWand || item instanceof PineWand || item instanceof EbonyWand|| item instanceof EbonyWand2 )) {
				sb.append(" (");
				sb.append(item.getQuantity());
				sb.append(")");
			}
			if (item.getItem().getNameIdNumber() == 1173) {
				DogCollar dc = (DogCollar) item;
				sb.append(" [Lv.");
				sb.append(dc.getPetLevel());
				sb.append(" ");
				sb.append(dc.getPetName());
				sb.append("]");
			}
			if (item.getInnRoomKey() > 0) {
				sb.append(" #");
				sb.append(item.getInnRoomKey());
			}
		}

		return sb.toString().trim();
	}

}
