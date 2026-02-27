package lineage.world.object.item;

import lineage.bean.lineage.Inventory;
import lineage.database.AccountDatabase;
import lineage.database.CharactersDatabase;
import lineage.database.EnchantLostItemDatabase;
import lineage.gui.GuiMain;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_InventoryBress;
import lineage.network.packet.server.S_InventoryCount;
import lineage.network.packet.server.S_InventoryEnchantUpdate;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.plugin.PluginController;
import lineage.share.Common;
import lineage.share.Lineage;
import lineage.share.Log;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.InventoryController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemIllusionInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.scroll.장인의무기마법주문서;

public class Enchant extends ItemIllusionInstance {

	protected String EnMsg[];

	public Enchant() {
		EnMsg = new String[3];
	}

	/**
	 * 인첸트 확률계산할지 여부를 리턴함.
	 */
	public boolean isChance(ItemInstance item) {
		// 안전인첸보다 높거나, 저주일경우 인첸확률 체크.
		return bress == 2 ? item.getEnLevel() <= 0 : item.getEnLevel() >= item.getItem().getSafeEnchant();
	}

	/**
	 * 인첸트를 진행할지 여부.
	 * 
	 * @param item
	 * @return
	 */
	protected boolean isEnchant(ItemInstance item) {
		if (item == null)
			return false;

		// 안전 0
		if (item instanceof ItemArmorInstance && Lineage.item_enchant_0armor_max > 0
				&& Lineage.item_enchant_0armor_max <= item.getEnLevel() && item.getItem().getSafeEnchant() == 0)
			return false;
		if (item instanceof ItemWeaponInstance && Lineage.item_enchant_0weapon_max > 0
				&& Lineage.item_enchant_0weapon_max <= item.getEnLevel() && item.getItem().getSafeEnchant() == 0)
			return false;

		// 방어구 확인.
		if (item instanceof ItemArmorInstance && Lineage.item_enchant_armor_max > 0
				&& Lineage.item_enchant_armor_max <= item.getEnLevel())
			return false;
		// 무기 확인.
		if (item instanceof ItemWeaponInstance && Lineage.item_enchant_weapon_max > 0
				&& Lineage.item_enchant_weapon_max <= item.getEnLevel())
			return false;
		// 봉인 확인.
		if (item.getBress() < 0)
			return false;

		return true;
	}

	/**
	 * 인첸트를 진행할지 여부.
	 * 
	 * @param item
	 * @return
	 */
	protected boolean isEnchantAcc(ItemInstance item) {
		if (item == null)
			return false;
		// 악세들만 확인하기
		if (item instanceof ItemArmorInstance && item.getEnLevel() < Lineage.item_acc_en
				&& ((item.getItem().getType2().equalsIgnoreCase("earring")
						|| (item.getItem().getType2().equalsIgnoreCase("necklace")
								|| (item.getItem().getType2().equalsIgnoreCase("ring")
										|| (item.getItem().getType2().equalsIgnoreCase("belt"))))))) {
			return true;
		} else if (item.getBress() < 0) {
			return false;
		} else {
			return false;
		}
	}

	/**
	 * 인첸트를 처리할 메서드.
	 */
	protected int toEnchant(Character cha, ItemInstance item) {
		// 기본 인첸 가능여부 판단.
		if (!isEnchant(item)) {
			// \f1 아무것도 일어나지 않았습니다.
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
			return -127;
		}

		return toEnchantNew(cha, item);
	}

	protected int toEnchantAcc(Character cha, ItemInstance item) {
		// 기본 인첸 가능여부 판단.
		if (!isEnchantAcc(item)) {
			// \f1 아무것도 일어나지 않았습니다.
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
			return -127;
		}

		return toEnchantNew(cha, item);
	}

	protected int toEnchantNew(Character cha, ItemInstance item) {
		Object o = PluginController.init(Enchant.class, "toEnchantNew", this, cha, item);
		if (o != null)
			return (Integer) o;

		boolean chance = isChance(item);
		boolean isEnchant = true;
		boolean isEnchantTop = false;

		int rnd = bress == 2 ? -1 : 1;
		String item_name = item.toStringDB();
		long item_objid = item.getObjectId();

		// 메세지 설정.
		EnMsg[0] = item.toString();
		// 검게, 파랗게, 은색으로
		EnMsg[1] = bress == 2 ? "$246" : item instanceof ItemWeaponInstance ? "$245" : "$252"; // 검게
		EnMsg[2] = "$247"; // 한 순간

		// 인첸 범위값 추출.
		// 인첸 범위값 추출.
		if (bress == 0 && item.getItem().getSafeEnchant() == 4 && item.getEnLevel() < item.getItem().getSafeEnchant()
				&& item.getItem().getType1().equalsIgnoreCase("armor")) {
			// 축 주문서 일 경우.
			rnd = Util.random(0D, 99D) < Lineage.ssafearmor4_3_5rnd2 ? 2 : 1;
		} else if ((bress == 0 && item.getItem().getSafeEnchant() == 6
				&& item.getEnLevel() < item.getItem().getSafeEnchant()
				&& item.getItem().getType1().equalsIgnoreCase("armor"))) {

			rnd = Util.random(0D, 99D) < Lineage.ssafearmor6_5_7rnd2 ? 2 : 1;

		} else if ((bress == 0 && item.getItem().getSafeEnchant() == 6
				&& item.getEnLevel() < item.getItem().getSafeEnchant()
				&& item.getItem().getType1().equalsIgnoreCase("weapon"))) {

			rnd = Util.random(0D, 99D) < Lineage.ssafeweapon_rnd2 ? 2 : 1;

			// 인첸값이 안전인첸보다 낮을 경우 // 그럼 여기서 rnd 가 2 이고 아이템 안전인첸보다 -1 인게 아이템 인젠레벨보다 크다면이야

			// 이게아니면 만약에 주문서가 축이면서 아이템의 인첸벨이 아이템의 안전인첸과 같다면
		}
		if (bress == 0 && item.getItem().getSafeEnchant() == 4 && item.getEnLevel() == item.getItem().getSafeEnchant()
				&& item.getItem().getType1().equalsIgnoreCase("armor")) {
			rnd = Util.random(0D, 99D) < Lineage.ssafearmor4_4_6rnd2 ? 2 : 1;
			// 인첸값이 안전인첸보다 낮을 경우
		}

		if (bress == 0 && item.getItem().getSafeEnchant() == 4 && item.getEnLevel() == 5
				&& item.getItem().getType1().equalsIgnoreCase("armor")) {
			rnd = Util.random(0D, 99D) < Lineage.ssafearmor4_5_7rnd2 ? 2 : 1;
		}
			
		// 기안의 두루마리 방어구 , 칼바스 무기
		if (this.getItem().getNameIdNumber() == 3359 || this.getItem().getNameIdNumber() == 3360) {
			// 0에지르면 3이 뜨고,
			switch (item.getEnLevel()) {
			case 0:
				rnd = 3;
				break;
			case 1:
				rnd = 2;
				break;
			case 2:
				rnd = Util.random(0D, 100D) < 50D ? 2 : 1;
				break;
			case 3:
				rnd = 2;
				break;
			case 4:
				rnd = Util.random(0D, 100D) < 50D ? 2 : 1;
				break;
			case 5:
				rnd = 2;
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				rnd = 1;
				break;
			}
		}
		/*
		 * //칼바스의 두루마리 무기 if(this.getItem().getNameIdNumber()==3360){
		 * 
		 * }
		 */

		// 고인첸 최대갯수 제한 체크.
		if (item instanceof ItemWeaponInstance && item.getEnLevel() + rnd >= 0
				&& Lineage.item_enchant_weapon_top[item.getEnLevel() + rnd] > 0) {
			if (CharactersDatabase.getInventoryEnchantCount(item.getEnLevel() + rnd,
					true) >= Lineage.item_enchant_weapon_top[item.getEnLevel() + rnd])
				isEnchantTop = true;
		}
		if (item instanceof ItemArmorInstance && item.getEnLevel() + rnd >= 0
				&& Lineage.item_enchant_armor_top[item.getEnLevel() + rnd] > 0) {
			if (CharactersDatabase.getInventoryEnchantCount(item.getEnLevel() + rnd,
					false) >= Lineage.item_enchant_armor_top[item.getEnLevel() + rnd])
				isEnchantTop = true;
		}
		/*
		 * // 제한체크에 걸렸을경우 인첸 실패 처리 유도. if(isEnchantTop) { chance = false; isEnchant =
		 * false; rnd = 0; }
		 */

		// 확률계산 해야한다면.
		if (chance)
			isEnchant = isEnchant(cha, item);

		// 인첸값이 1이상 떳거나 감소햇을경우
		if (rnd > 1 || rnd < -1)
			EnMsg[2] = "$248"; // 잠시 (+2)

//		if (InventoryController.isAccessory(item) && rnd > 0) 
//			rnd = 1;

		// 인첸 성공시 아이템에 실제 변수 설정하는 부분.
		if (isEnchant) {
			/*
			 * 
			 * No [ 307 ] Time [ 03:03:55 ] << S_OPCODE_UNKNOWN2 packbox 네용 [ Server Packet
			 * ] : 134 [ 0x86 ] / 172 [ 0xac ] / Length : 34 86 ac cb b5 66 03 17 00 00 00
			 * 00 00 01 76 f0 2d 6c 00 00 00 00 00 00 00 00 02 00 00 00 00 cc c1
			 * 
			 * No [ 338 ] Time [ 03:04:37 ] [ Server Packet ] : 134 [ 0x86 ] / 172 [ 0xac ]
			 * / Length : 34 86 ac cb b5 66 03 17 00 00 00 00 00 02 76 f0 2d 6c 00 00 00 00
			 * 00 00 00 00 02 00 00 00 00 45 1f
			 * 
			 */
			// \f1%0%s %2 %1 빛납니다.
			cha.toSender(
					S_Message.clone(BasePacketPooling.getPool(S_Message.class), 161, EnMsg[0], EnMsg[1], EnMsg[2]));
			// 0% 확률로 축으로 변경하기.
			if (Lineage.item_enchant_bless && bress == 0 && item.getBress() == 1 && Util.random(0D, 100D) <= 5D) {
				item.setBress(0);
				cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
			}
			/*
			 * if(item.getItem().getType1().equalsIgnoreCase("armor")){
			 * if(item.getItem().getSafeEnchant() <= item.getEnLevel() ){
			 * cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.
			 * class), cha, 2047), true);
			 * cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.
			 * class), cha, 227), true);
			 * cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.
			 * class), cha, 748), true); //블레스드 아머 } }else
			 * if(item.getItem().getType1().equalsIgnoreCase("weapon")){
			 * if(item.getItem().getSafeEnchant() <= item.getEnLevel() ){
			 * cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.
			 * class), cha, 2047), true);
			 * cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.
			 * class), cha, 227), true);
			 * cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.
			 * class), cha, 747), true); //인첸트
			 * cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.
			 * class), cha, 2176), true);//브레스 } }
			 */
		} else {
			if (Lineage.enfail) {
				if (item instanceof ItemWeaponInstance && item.getEnLevel() == 8
						&& (item.getItem().getName().equalsIgnoreCase("레이피어")
								|| item.getItem().getName().equalsIgnoreCase("장궁"))) {
					PcInstance pc = (PcInstance) cha;
					pc.setEnfail(pc.getEnfail() + 1);
					AccountDatabase.updateenfail(pc.getClient().getAccountUid(), pc.getEnfail());
					System.out.println(pc.getEnfail() + pc.getAccountUid());
				}

			}

			rnd = 0;
		}
		long time = System.currentTimeMillis();
		String timeString = Util.getLocaleString(time, true);

		// log
		if (isEnchant) {
			Log.appendItem(cha, "type|인첸트성공", String.format("item_name|%s", item_name),
					String.format("item_objid|%d", item_objid), String.format("scroll_name|%s", toStringDB()),
					String.format("scroll_objid|%d", getObjectId()), String.format("scroll_bress|%d", getBress()),
					String.format("enchant_value|%d", rnd));

			if (!Common.system_config_console && item.getItem().getSafeEnchant() - 1 <= item.getEnLevel() && rnd > 0) {
				String log = String.format("[%s] [인첸트 성공]\t [캐릭터: %s]\t [아이템: %s]\t [주문서: %s]\t [인첸증가: %d]", timeString,
						cha.getName(), Util.getItemNameToString(item, item.getCount()),
						Util.getItemNameToString(this, getCount()), rnd);

				GuiMain.display.asyncExec(new Runnable() {
					public void run() {
						GuiMain.getViewComposite().getEnchantComposite().toLog(log);
					}
				});
			}
		} else {
			if (rnd == 0) {
				EnchantLostItemDatabase.append(cha, item, this);
			}
			Log.appendItem(cha, "type|인첸트실패", String.format("item_name|%s", item_name),
					String.format("item_objid|%d", item_objid), String.format("scroll_name|%s", toStringDB()),
					String.format("scroll_objid|%d", getObjectId()), String.format("scroll_bress|%d", getBress()));

			if (!Common.system_config_console) {
				String log = String.format("[%s] [인첸트 실패]\t [캐릭터: %s]\t [아이템: %s]\t [주문서: %s]", timeString,
						cha.getName(), Util.getItemNameToString(item, item.getCount()),
						Util.getItemNameToString(this, getCount()));

				GuiMain.display.asyncExec(new Runnable() {
					public void run() {
						GuiMain.getViewComposite().getEnchantComposite().toLog(log);
					}
				});
			}
		}
		return rnd;
	}

	/**
	 * 확률 계산한 후 성공여부를 리턴함.
	 * 
	 * @param cha
	 * @param item
	 * @return
	 */
	protected boolean isEnchant(Character cha, ItemInstance item) {
		double enchant_chance = 40D;
		/*
		 * if(bress == 2) { for(int i=0 ; i>item.getEnLevel() ; --i) enchant_chance =
		 * enchant_chance * 0.35; } else { // 안전인첸값을 기준으로 인첸값 만큼 확률 낮추기. for(int
		 * i=item.getItem().getSafeEnchant() ; i<item.getEnLevel() ; ++i) enchant_chance
		 * = enchant_chance * 0.35; } // 인첸트 성공 확률 추출. return Util.random(0.0D,
		 * enchant_chance*Lineage.rate_enchant) >= Util.random(0.0D, 90D);
		 */

//		if (InventoryController.isAccessory(item)) {
//			if(item.getItem().getSafeEnchant()==0){ //안전 0 짜리 방어구
//				switch(item.getEnLevel()){ //50 33 25 20 17 14 12.5 11 
//					case 0: enchant_chance = 55;
//						break;
//					case 1: enchant_chance = 37;
//						break;
//					case 2: enchant_chance = 28;
//						break;
//					case 3: enchant_chance = 25;
//						break;
//					case 4: enchant_chance = 21;
//						break;
//					case 5: enchant_chance = 15;
//						break;
//					case 6: enchant_chance = 13;
//						break;
//					case 7: enchant_chance = 13;
//						break;	
//					default: enchant_chance = 30;
//							for(int i=item.getItem().getSafeEnchant() ; i<item.getEnLevel() ; ++i)
//							enchant_chance = enchant_chance * 0.4;
//						break;
//				}
//			}

		if (item.getItem().getType1().equalsIgnoreCase("armor")) {
			if (item.getItem().getSafeEnchant() == 0) { // 안전 0 짜리 방어구
				switch (item.getEnLevel()) { // 50 33 25 20 17 14 12.5 11
				case 0:
					enchant_chance = 55;
					break;
				case 1:
					enchant_chance = 37;
					break;
				case 2:
					enchant_chance = 28;
					break;
				case 3:
					enchant_chance = 25;
					break;
				case 4:
					enchant_chance = 21;
					break;
				case 5:
					enchant_chance = 15;
					break;
				case 6:
					enchant_chance = 13;
					break;
				case 7:
					enchant_chance = 13;
					break;
				default:
					enchant_chance = 10;
					for (int i = item.getItem().getSafeEnchant(); i < item.getEnLevel(); ++i)
						enchant_chance = enchant_chance * 0.4;
					break;

				}
			} else {// 그외
				switch (item.getEnLevel()) { // 안전인첸 04 이상 갑옷
				case 4:
					enchant_chance = 53; // 25 20 17 14 12.5 11 10
					break;
				case 5:
					enchant_chance = 37;
					break;
				case 6:
					enchant_chance = 28;
					break;
				case 7:
					enchant_chance = 19;
					break;
				case 8:
					enchant_chance = 12.5;
					break;
				case 9:
					enchant_chance = 11;
					break;
				case 10:
					enchant_chance = 13;
					break;
				default:
					enchant_chance = 10;
					for (int i = item.getItem().getSafeEnchant(); i < item.getEnLevel(); ++i)
						enchant_chance = enchant_chance * 0.4;
					break;
				}
			}
		} else if (item.getItem().getType1().equalsIgnoreCase("weapon")) {
			if (item.getItem().getSafeEnchant() == 0) {// 안전 0 짜리 무기 33
				switch (item.getEnLevel()) {
				case 0:
					enchant_chance = 33;
					break;
				case 1:
					enchant_chance = 33;
					break;
				case 2:
					enchant_chance = 33;
					break;
				case 3:
					enchant_chance = 33;
					break;
				case 4:
					enchant_chance = 33;
					break;
				case 5:
					enchant_chance = 33;
					break;
				case 6:
					enchant_chance = 33;
					break;
				case 7:
					enchant_chance = 33;
					break;
				default:
					enchant_chance = 20;
					for (int i = item.getItem().getSafeEnchant(); i < item.getEnLevel(); ++i)
						enchant_chance = enchant_chance * 0.4;
					break;
				}

			} else {
				switch (item.getEnLevel()) { // 안전 6짜리 무기 33
				case 6:
					enchant_chance = 33;
					break;
				case 7:
					enchant_chance = 33;
					break;
				case 8:
					enchant_chance = 33;
					break;
				case 9:
					enchant_chance = 33;
					break;
				case 10:
					enchant_chance = 33;
					break;
				case 11:
					enchant_chance = 33;
					break;
				case 12:
					enchant_chance = 33;
					break;
				case 13:
					enchant_chance = 33;
					break;
				default:
					enchant_chance = 20;
					for (int i = item.getItem().getSafeEnchant(); i < item.getEnLevel(); ++i)
						enchant_chance = enchant_chance * 0.4;
					break;
				}

			}
		}
		// }
		// 인첸트 성공 확률 추출.
		return enchant_chance * Lineage.rate_enchant > Util.random(0.0D, 100D);
	}

	protected int toEnchantOld(Character cha, ItemInstance item) {
		boolean chance = isChance(item);
		boolean enchant = false;
		boolean double_enchant = false;
		double enchant_chance = 30;

		int rnd = bress == 2 ? -1 : 1;
		EnMsg[0] = item.toString();
		EnMsg[2] = "$247"; // 한 순간
		if (item instanceof ItemWeaponInstance)
			EnMsg[1] = "$245"; // 파랗게
		else
			EnMsg[1] = "$252"; // 은색으로
		if (bress == 2)
			EnMsg[1] = "$246"; // 검게

		// 인첸트 성공여부 확률을 체크해야 할경우
		if (chance) {

			if (bress == 2) {
				for (int i = 0; i > item.getEnLevel(); --i)
					enchant_chance /= 2;
			} else {
				if (item instanceof ItemWeaponInstance) {
					// 무기
					for (int i = 6; i < item.getEnLevel(); ++i)
						enchant_chance /= 2;
					if (bress == 2) {
						// 축 주문서일경우.
						if (item.getEnLevel() < 6)
							// 인첸트레벨이 8보다작은 0~7 사이라면 더블찬스 체크.
							double_enchant = Util.random(1, 100) <= 35;
					}
				} else {
					// 방어구
					if (item.getItem().getName().startsWith("요정족")) {
						// 요정족 아이템일 경우.
						enchant_chance = 15;
						for (int i = 6; i < item.getEnLevel(); ++i)
							enchant_chance /= 2;
						if (bress == 0) {
							// 축 주문서일경우.
							if (item.getEnLevel() <= 5)
								// 인첸트레벨이 9보다작은 0~8 사이라면 더블찬스 체크.
								double_enchant = Util.random(1, 100) <= 10;
						}
					} else if (item.getItem().getMaterial() == 9) {
						// 뼈재질아이템일 경우.
						for (int i = 0; i < item.getEnLevel(); ++i)
							enchant_chance /= 2;
					} else {
						// 일반 아이템일 경우.
						for (int i = 4; i < item.getEnLevel(); ++i)
							enchant_chance /= 2;
						if (bress == 2) {
							// 축 주문서일경우.
							if (item.getEnLevel() < 5)
								// 인첸트레벨이 7보다작은 0~6 사이라면 더블찬스 체크.
								double_enchant = Util.random(1, 100) <= 18;
						}
					}
				}
			}
			// 인첸트 성공 확률 추출.
			enchant = Util.random(0.0D, enchant_chance * Lineage.rate_enchant) >= Util.random(0.0D, 100D);
		} else {
			enchant = true;

			if (bress == 0) {
				// 축 주문서이면서 무조건 성공일경우 더블찬스 체크.
				double_enchant = Util.random(1, 100) <= 35;
			}
//			else if(bress == 0) {
//				if(item.getItem().getType1().equalsIgnoreCase("armor")){
//					if(item.getItem().getSafeEnchant() == 6) {
//						if(getEnLevel()== 6) {
//						double_enchant = false;
//					}
//				}
//			}
//		}
		}

		// 인첸값이 더블로 떳을경우 실제 인첸적용할 변수 2로 변경.
		if (double_enchant) {
			EnMsg[2] = "$248"; // 잠시 (+2)
			rnd = 2;
		}

		// 인첸 성공시 아이템에 실제 변수 설정하는 부분.
		if (enchant) {
			item.setEnLevel(item.getEnLevel() + rnd); // 아이템 인첸트값 set
			if (Lineage.server_version <= 144) {
				cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), item));
				cha.toSender(S_InventoryCount.clone(BasePacketPooling.getPool(S_InventoryCount.class), item));
			} else {
				cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
				if (Lineage.server_version >= 380)
					cha.toSender(S_InventoryEnchantUpdate
							.clone(BasePacketPooling.getPool(S_InventoryEnchantUpdate.class), item));
			}
			// \f1%0%s %2 %1 빛납니다.
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 161, EnMsg[0], EnMsg[1], EnMsg[2]));

			// 월드에 메세지 표현처리.
			if (item instanceof ItemWeaponInstance && Lineage.world_message_enchant_weapon > 0
					&& Lineage.world_message_enchant_weapon < item.getEnLevel()) {
				ChattingController.toChatting(null,
						String.format("%s님께서 %d인첸에 성공하였습니다.", cha.getName(), item.getEnLevel()), 3);
			}
			if (item instanceof ItemArmorInstance && Lineage.world_message_enchant_armor > 0
					&& Lineage.world_message_enchant_armor < item.getEnLevel()) {
				ChattingController.toChatting(null,
						String.format("%s님께서 %d인첸에 성공하였습니다.", cha.getName(), item.getEnLevel()), 3);
			}
			// 1% 확률로 축으로 변경하기.
			if (Lineage.item_enchant_bless && bress == 0 && item.getBress() != 0 && Util.random(0, 99) == 0) {
				item.setBress(0);
				cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
			}
		} else {
			rnd = 0;
			// \f1%0%s 강렬하게%1 빛나더니 증발되어 사라집니다.성환(인첸실패)
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 164, item.toString(), " 붉게"));
		}

		return rnd;
	}

}
