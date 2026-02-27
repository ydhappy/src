package lineage.world.object.instance;

import java.sql.Connection;
import java.util.Calendar;

import lineage.bean.lineage.Inventory;
import lineage.database.ItemDatabase;
import lineage.database.PolyDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_InventoryCount;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryEquippedWindow;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_SoundEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.controller.InventoryController;
import lineage.world.object.Character;

public class ItemArmorInstance extends ItemIllusionInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ItemArmorInstance();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha;
		  if (!equipped) 
			    if (getItem().getType2().equalsIgnoreCase("helm")) {
					 // 투구
				    	pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 2807));
			        } else if (getItem().getType2().equalsIgnoreCase("t")) {
					 // 티셔츠
			        	pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 2800)); 
			        } else if (getItem().getType2().equalsIgnoreCase("armor")) {
					 // 갑옷
			        	pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 2802)); 
		            } else if (getItem().getType2().equalsIgnoreCase("cloak")) {
					 // 망토
		            	pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 2814)); 
		            } else if (getItem().getType2().equalsIgnoreCase("shield")) {
					 // 방패
		            	pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 2810)); 
			        } else if (getItem().getType2().equalsIgnoreCase("glove")) {
					 // 장갑
			        	pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 2805)); 
			        } else if (getItem().getType2().equalsIgnoreCase("boot")) {
					 // 부츠
			        	pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 2820)); 
			        } else if (getItem().getType2().equalsIgnoreCase("belt")) {
					 // 벨트
			        	pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 2815)); 
			        } else if (getItem().getType2().equalsIgnoreCase("necklace")) {
					 // 목걸이
			        	pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 2821)); 
			        } else if (getItem().getType2().equalsIgnoreCase("ring") || getItem().getType2().equalsIgnoreCase("earring")) {
					 // 반지
			        	pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 2822)); 
					  
				    }
		    }
		if (isLvCheck(cha)) {
			if (isClassCheck(cha)) {
				Inventory inv = cha.getInventory();
				if (inv != null && isEquipped(cha, inv, cbp == null)) {
					if (PolyDatabase.toEquipped(cha, this) || equipped) {
						if (equipped) {
							if (bress == 2
									&& !item.getType2()
											.equalsIgnoreCase("ring")) {
								// \f1그렇게 할 수 없습니다. 저주 받은 것 같습니다.
								cha.toSender(S_Message.clone(BasePacketPooling
										.getPool(S_Message.class), 150));
								return;
							}
							setEquipped(false);
						} else {

							if (item.getType2().equalsIgnoreCase("earring")) {
								if (isRoomtiece()) {
									ItemInstance earring = inv.getSlot(12);
									ItemInstance earring2 = inv.getSlot(122);
									ItemInstance 착용아이템 = ItemDatabase
											.newInstance(item);
									if ((earring != null && earring.getItem()
											.getNameIdNumber() == 착용아이템
											.getItem().getNameIdNumber())
											|| (earring2 != null && earring2
													.getItem()
													.getNameIdNumber() == 착용아이템
													.getItem()
													.getNameIdNumber())) {
										ChattingController.toChatting(cha,
												"룸티스악세 동시착용불가", 20);
										return;
									}
								}
							}

							if (item.getType2().equalsIgnoreCase("shield")) {
								ItemInstance weapon = inv.getSlot(8);
								if (weapon != null
										&& weapon.getItem().isTohand()) {
									// \f1두손 무기를 무장하고 방패를 착용할 수 없습니다.
									cha.toSender(S_Message.clone(
											BasePacketPooling
													.getPool(S_Message.class),
											129));
									return;
								}
							}
							setEquipped(true);
						}

						toSetoption(cha, true);
						toEquipped(cha, inv, item.getSlot());
						toOption(cha, true);
						toBuffCheck(cha);
					} else {
						ChattingController.toChatting(cha, "착용할 수 없습니다.", 20);
					}
				}
			} else {
				// \f1당신의 클래스는 이 아이템을 사용할 수 없습니다.
				cha.toSender(S_Message.clone(
						BasePacketPooling.getPool(S_Message.class), 264));
			}
		}
	}

	/**
	 * 방어구 착용 및 해제 처리 메서드.
	 */
	@Override
	public void toEquipped(Character cha, Inventory inv, int slot) {

		if (equipped) {
			cha.setAc(cha.getAc()
					+ getItem().getAc()
					+ (InventoryController.isAccessory(this) ? 0 : getEnLevel()));
			cha.setDynamicAc(cha.getDynamicAc() + getDynamicAc());
		} else {
			cha.setAc(cha.getAc()
					- getItem().getAc()
					- (InventoryController.isAccessory(this) ? 0 : getEnLevel()));
			cha.setDynamicAc(cha.getDynamicAc() - getDynamicAc());
		}
		
		if (slot == Lineage.SLOT_RING1) {
			if (equipped) {
				if (inv.getSlot(Lineage.SLOT_RING1) == null) {
					inv.setSlot(Lineage.SLOT_RING1, this);
				} else {
					inv.setSlot(Lineage.SLOT_RING2, this);
				}
			} else {
				if (inv.getSlot(Lineage.SLOT_RING1) != null
						&& inv.getSlot(Lineage.SLOT_RING1).getObjectId() == getObjectId()) {
					inv.setSlot(Lineage.SLOT_RING1, null);
				} else if (inv.getSlot(Lineage.SLOT_RING2) != null
						&& inv.getSlot(Lineage.SLOT_RING2).getObjectId() == getObjectId()) {
					inv.setSlot(Lineage.SLOT_RING2, null);
				} else {
					inv.setSlot(Lineage.SLOT_RING1, null);
					inv.setSlot(Lineage.SLOT_RING2, null);
				}
			}
		} else if (slot == 12 || slot == 122) {
			if (equipped) {
				if (inv.getSlot(12) == null)
					inv.setSlot(12, this);
				else if (inv.getSlot(122) == null)
					inv.setSlot(122, this);

			} else {
				if (inv.getSlot(12) != null
						&& inv.getSlot(12).getObjectId() == getObjectId()) {
					inv.setSlot(12, null);
				} else if (inv.getSlot(122) != null
						&& inv.getSlot(122).getObjectId() == getObjectId()) {
					inv.setSlot(122, null);
				} else {
					inv.setSlot(12, null);
					inv.setSlot(122, null);
				}
			}
		} else {
			inv.setSlot(item.getSlot(), equipped ? this : null);
		}
		
		if (!cha.isBlessArmor() && inv.getSlot(Lineage.SLOT_HELM) != null
				&& inv.getSlot(Lineage.SLOT_SHIRT) != null
				&& inv.getSlot(Lineage.SLOT_ARMOR) != null
				&& inv.getSlot(Lineage.SLOT_CLOAK) != null
				&& inv.getSlot(Lineage.SLOT_GLOVE) != null
				&& inv.getSlot(Lineage.SLOT_BOOTS) != null) {
			if ((inv.getSlot(Lineage.SLOT_HELM).getBress() == 0 || inv.getSlot(
					Lineage.SLOT_HELM).getBress() == -128)
					&& (inv.getSlot(Lineage.SLOT_SHIRT).getBress() == 0 || inv
							.getSlot(Lineage.SLOT_SHIRT).getBress() == -128)
					&& (inv.getSlot(Lineage.SLOT_ARMOR).getBress() == 0 || inv
							.getSlot(Lineage.SLOT_ARMOR).getBress() == -128)
					&& (inv.getSlot(Lineage.SLOT_CLOAK).getBress() == 0 || inv
							.getSlot(Lineage.SLOT_CLOAK).getBress() == -128)
					&& (inv.getSlot(Lineage.SLOT_GLOVE).getBress() == 0 || inv
							.getSlot(Lineage.SLOT_GLOVE).getBress() == -128)
					&& (inv.getSlot(Lineage.SLOT_BOOTS).getBress() == 0 || inv
							.getSlot(Lineage.SLOT_BOOTS).getBress() == -128)) {
				ChattingController.toChatting(cha, "축복받은 방어구 세트 효과: 대미지 감소+2", Lineage.CHATTING_MODE_MESSAGE);
				cha.setDynamicReduction(cha.getDynamicReduction() + 2);
				cha.setBlessArmor(true);
			}
		}

		if (!cha.isBlessAcc() && inv.getSlot(Lineage.SLOT_NECKLACE) != null
				&& inv.getSlot(Lineage.SLOT_RING1) != null
				&& inv.getSlot(Lineage.SLOT_RING2) != null
				&& inv.getSlot(Lineage.SLOT_BELT) != null) {
			if ((inv.getSlot(Lineage.SLOT_NECKLACE).getBress() == 0 || inv
					.getSlot(Lineage.SLOT_NECKLACE).getBress() == -128)
					&& (inv.getSlot(Lineage.SLOT_RING1).getBress() == 0 || inv
							.getSlot(Lineage.SLOT_RING1).getBress() == -128)
					&& (inv.getSlot(Lineage.SLOT_RING2).getBress() == 0 || inv
							.getSlot(Lineage.SLOT_RING2).getBress() == -128)
					&& (inv.getSlot(Lineage.SLOT_BELT).getBress() == 0 || inv
							.getSlot(Lineage.SLOT_BELT).getBress() == -128)) {
				ChattingController.toChatting(cha, "축복받은 장신구 세트 효과: PvP 대미지+2, 주술력+1", Lineage.CHATTING_MODE_MESSAGE);
				cha.setDynamicAddPvpDmg(cha.getDynamicAddPvpDmg() + 2);
				cha.setDynamicSp(cha.getDynamicSp() + 1);
				cha.setBlessAcc(true);
			}
		}

		if (cha.isBlessArmor()
				&& (inv.getSlot(Lineage.SLOT_HELM) == null
						|| inv.getSlot(Lineage.SLOT_SHIRT) == null
						|| inv.getSlot(Lineage.SLOT_ARMOR) == null
						|| inv.getSlot(Lineage.SLOT_CLOAK) == null
						|| inv.getSlot(Lineage.SLOT_GLOVE) == null || inv
						.getSlot(Lineage.SLOT_BOOTS) == null)) {
			ChattingController.toChatting(cha, "축복받은 방어구 세트 효과 해제", Lineage.CHATTING_MODE_MESSAGE);
			cha.setDynamicReduction(cha.getDynamicReduction() - 2);
			cha.setBlessArmor(false);
		}

		//
		if (cha.isBlessAcc()
				&& (inv.getSlot(Lineage.SLOT_NECKLACE) == null
						|| inv.getSlot(Lineage.SLOT_RING1) == null
						|| inv.getSlot(Lineage.SLOT_RING2) == null || inv
						.getSlot(Lineage.SLOT_BELT) == null)) {
			ChattingController.toChatting(cha, "축복받은 장신구 세트 효과 해제", Lineage.CHATTING_MODE_MESSAGE);
			cha.setDynamicAddPvpDmg(cha.getDynamicAddPvpDmg() - 2);
			cha.setDynamicSp(cha.getDynamicSp() - 1);
			cha.setBlessAcc(false);
		}
		
		if (getItem().getName().equalsIgnoreCase("지존의 귀걸이")) {
			if (equipped) {
				ChattingController.toChatting(cha, "지존의 귀걸이: AC-2, 추가 대미지+3, SP+2, 대미지 감소+3, 경험치 보너스+50%", Lineage.CHATTING_MODE_MESSAGE);
				cha.setDynamicExp(cha.getDynamicExp() + 0.5);
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 3);
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 3);
				cha.setDynamicReduction(cha.getDynamicReduction() + 3);
				cha.setDynamicSp(cha.getDynamicSp() + 2);
				cha.setDynamicAc(cha.getDynamicAc() + 2);
			} else {
				ChattingController.toChatting(cha, "지존의 귀걸이: 옵션 효과 해제", Lineage.CHATTING_MODE_MESSAGE);
				cha.setDynamicExp(cha.getDynamicExp() - 0.5);
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 3);
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 3);
				cha.setDynamicReduction(cha.getDynamicReduction() - 3);
				cha.setDynamicSp(cha.getDynamicSp() - 2);
				cha.setDynamicAc(cha.getDynamicAc() - 2);
			}
		}

		// \f1%0%s 손에 달라 붙었습니다.
		if (getBress() == 2 && equipped)
			cha.toSender(S_Message.clone(
					BasePacketPooling.getPool(S_Message.class), 149, getName()));

		if (!cha.isWorldDelete())
			cha.toSender(S_InventoryEquipped.clone(
					BasePacketPooling.getPool(S_InventoryEquipped.class), this));
		if (Lineage.server_version >= 360)
			cha.toSender(S_InventoryEquippedWindow.clone(
					BasePacketPooling.getPool(S_InventoryEquippedWindow.class),
					this, slot));

		//
		setSlot(slot);
	}

	// $11996
	// $11994
	// $11995
	// $11997
	// 룸티스의 검은빛 귀걸이

	private boolean isRoomtiece() {
		if (getItem().getNameIdNumber() == 11994
				|| getItem().getNameIdNumber() == 11995
				|| getItem().getNameIdNumber() == 11996
				|| getItem().getNameId().equalsIgnoreCase("룸티스의 검은빛 귀걸이")) {
			return true;
		}
		return false;
	}

	/**
	 * 방어구 착용순서 체크하기.
	 */
	protected boolean isEquipped(Character cha, Inventory inv, boolean absolute) {
		// 착용해제 하려는가?
		if (equipped) {
			if(!Lineage.item_equipped_type) {
				// 갑옷해제시 망토 확인
				if (item.getSlot() == Lineage.SLOT_ARMOR && inv.getSlot(Lineage.SLOT_CLOAK) != null) {
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 127));
					return false;
				}
				// 티셔츠해제시 망토 확인
				if (item.getSlot() == Lineage.SLOT_SHIRT && inv.getSlot(Lineage.SLOT_CLOAK) != null) {
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 127));
					return false;
				}
				// 티셔츠해제시 아머 확인
				if (item.getSlot() == Lineage.SLOT_SHIRT && inv.getSlot(Lineage.SLOT_ARMOR) != null) {
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 127));
					return false;
				}
			}
			// 착용 하려는가?
		} else {
			if(!Lineage.item_equipped_type) {
				// 갑옷 착용시 망토 확인
				if (item.getSlot() == Lineage.SLOT_ARMOR && inv.getSlot(Lineage.SLOT_CLOAK) != null) {
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 126, "$226", "$225"));
					return false;
				}
				// 티셔츠 착용시 갑옷 확인
				if (item.getSlot() == Lineage.SLOT_SHIRT && inv.getSlot(Lineage.SLOT_ARMOR) != null) {
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 126, "$168", "$226"));
					return false;
				}
				// 티셔츠 착용시 망토 확인
				if (item.getSlot() == Lineage.SLOT_SHIRT && inv.getSlot(Lineage.SLOT_CLOAK) != null) {
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 126, "$168", "$225"));
					return false;
				}
			}
			// 방패 착용시 양손무기 확인
			if(item.getSlot()==7 && inv.getSlot(8)!=null && inv.getSlot(8).getItem().isTohand()){
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 129));
				return false;
			}
			// 방패 착용시 가더 확인.
			if(item.getSlot()==7 && inv.getSlot(27)!=null){
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 124));
				return false;
			}
			// 가더 착용시 방패 확인.
			if(item.getSlot()==27 && inv.getSlot(7)!=null) {
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 124));
				return false;
			}
			if (item.getSlot() == 18) {
				boolean Old = inv.getSlot(18) != null && inv.getSlot(19) != null;
				boolean New = inv.getSlot(20) != null && inv.getSlot(21) != null;
				if ((Lineage.server_version < 360 && Old) || (Old && New)) {
					if (Lineage.item_equipped_type && inv.getSlot(item.getSlot()).getBress() != 2) {
						int count = 100;
						do {
							if (count-- < 0) {
								break;
							}
							inv.getSlot(item.getSlot()).toClick(cha, null);
						} while (inv.getSlot(item.getSlot()) != null);
					} else {
						// \f1이미 뭔가를 착용하고 있습니다.
						cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 124));
						return false;
					}
				}
				return true;
			} else {
				if (inv.getSlot(item.getSlot()) != null) {
					if (Lineage.item_equipped_type && inv.getSlot(item.getSlot()).getBress() != 2) {
						int count = 100;
						do {
							if (count-- < 0) {
								break;
							}
							inv.getSlot(item.getSlot()).toClick(cha, null);
						} while (inv.getSlot(item.getSlot()) != null);
					} else {
						// \f1이미 뭔가를 착용하고 있습니다.
						cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 124));
						return false;
					}
				}
				return true;
			}
		}
		return true;
	}

	/**
	 * 방어구 상태에따라 ac전체값 계산하여 리턴.
	 */
	protected int getTotalAc() {
		int ac = getItem().getAc() + getDynamicAc();
		if (!InventoryController.isAccessory(this))
			ac += getEnLevel();
		return ac < 0 ? 0 : ac;

	}

	/**
	 * 리니지 월드에 접속했을때 착용중인 아이템 처리를 위해 사용되는 메서드.
	 */
	@Override
	public void toWorldJoin(Connection con, PcInstance pc) {
		super.toWorldJoin(con, pc);
		//
		toEnchantOption(pc, false);
		//
		if (isEquipped()) {
			toSetoption(pc, false);
			toEquipped(pc, pc.getInventory(), item.getSlot());
			toOption(pc, false);
		}
	}

	@Override
	public void toPickup(Character cha) {
		super.toPickup(cha);
		//
		if (!cha.isWorldDelete())
			toEnchantOption(cha,
					cha.getInventory().value(getObjectId()) != null);
	}

	/**
	 * 인첸에 따른 악세서리 옵션 처리. : 월드 재접할때도 호출됨. : 인첸을 성공햇을때도 호출됨. : 아이템 줍거나 지급받앗을때도
	 * 호출됨.
	 * 
	 * @param pc
	 */
	protected void toEnchantOption(Character cha, boolean sendPacket) {
		// 악세서리가 아닐경우 무시.
		if (!InventoryController.isAccessory(this))
			return;
		//
		int addHp = 0;
		int addMp = 0;
		int addDmg = 0;
		int addDmgBow = 0;
		int addReduction = 0;
		int addHealingPercent = 0;
		int addSp = 0;
		// 귀걸이
		if (getItem().getType2().equalsIgnoreCase("earring")) {
			if (getEnLevel() >= 4) {
				addDmg += 1;
				addDmgBow += 1;
			}
			if (getEnLevel() >= 5) {
				addDmg += 1;
				addDmgBow += 1;
			}
			if (getEnLevel() >= 6) {
				addDmg += 1;
				addDmgBow += 1;
			}
			if (getEnLevel() >= 7) {
				addDmg += 1;
				addDmgBow += 1;
				addReduction += 1;
			}
		}

		if (isEquipped() && !cha.isWorldDelete()) {
			// 이전에 세팅값 빼기.
			cha.setDynamicHp(cha.getDynamicHp() - getDynamicHp());
			cha.setDynamicMp(cha.getDynamicMp() - getDynamicMp());
			cha.setDynamicReduction(cha.getDynamicReduction()
					- getDynamicReduction());
			cha.setDynamicHealingPercent(cha.getDynamicHealingPercent()
					- getDynamicHealingPercent());
			cha.setDynamicAddDmg(cha.getDynamicAddDmg() - getDynamicAddDmg());
			cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow()
					- getDynamicAddDmgBow());
			cha.setDynamicSp(cha.getDynamicSp() - getDynamicSp());
			// 인첸에따른 새로운값 적용.
			cha.setDynamicHp(cha.getDynamicHp() + addHp);
			cha.setDynamicMp(cha.getDynamicMp() + addMp);
			cha.setDynamicReduction(cha.getDynamicReduction() + addReduction);
			cha.setDynamicHealingPercent(cha.getDynamicHealingPercent()
					+ addHealingPercent);
			cha.setDynamicAddDmg(cha.getDynamicAddDmg() + addDmg);
			cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + addDmgBow);
			cha.setDynamicSp(cha.getDynamicSp() + addSp);
		}
		setDynamicHp(addHp);
		setDynamicMp(addMp);
		setDynamicReduction(addReduction);
		setDynamicHealingPercent(addHealingPercent);
		setDynamicAddDmg(addDmg);
		setDynamicAddDmgBow(addDmgBow);
		setDynamicSp(addSp);
		//
		if (sendPacket) {
			if (Lineage.server_version <= 144)
				cha.toSender(S_InventoryEquipped.clone(
						BasePacketPooling.getPool(S_InventoryEquipped.class),
						this));
			else
				cha.toSender(S_InventoryStatus.clone(
						BasePacketPooling.getPool(S_InventoryStatus.class),
						this));
		}
	}

	/**
	 * 인첸트 활성화 됫을때 아이템의 뒷처리를 처리하도록 요청하는 메서드.
	 */
	@Override
	public void toEnchant(PcInstance pc, int en) {
		//
		if (en == -127)
			return;
		//
		if (en != 0) {
			// 월드에 메세지 표현처리.
			String worldMessage = null;
			if (Lineage.world_message_enchant_armor > 0
					&& Lineage.world_message_enchant_armor <= getEnLevel())
				worldMessage = String.format("어느 아덴 용사의 %s이 찬란히 파랗게 빛납니다.",
						toStringDB());
			if (worldMessage != null) {
				for (PcInstance use : World.getPcList())
					ChattingController.toChatting(use, worldMessage, 20);
			}
			// 아이템 인첸트값 set
			setEnLevel(getEnLevel() + en);
			if (Lineage.server_version <= 144) {
				pc.toSender(S_InventoryEquipped.clone(
						BasePacketPooling.getPool(S_InventoryEquipped.class),
						this));
				pc.toSender(S_InventoryCount.clone(
						BasePacketPooling.getPool(S_InventoryCount.class), this));
			} else {
				pc.toSender(S_InventoryStatus.clone(
						BasePacketPooling.getPool(S_InventoryStatus.class),
						this));
			}
			//
			if (equipped && !InventoryController.isAccessory(this)
					&& (en < 0 ? getTotalAc() >= 0 : getTotalAc() > 0)) {
				pc.setAc(pc.getAc() + en);
				pc.toSender(S_CharacterStat.clone(
						BasePacketPooling.getPool(S_CharacterStat.class), pc));
			}
			// 인챈트 성공시 이팩트
			if(getItem().getSafeEnchant() < getEnLevel()) {
				switch(getEnLevel()) {
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
						pc.toSender(new S_ObjectEffect(pc, 8683), true);
						break;
					case 6:
						pc.toSender(new S_ObjectEffect(pc, 8684), true);
						break;
					case 7:
						pc.toSender(new S_ObjectEffect(pc, 8685), true);
						break;
					case 8:
						pc.toSender(new S_ObjectEffect(pc, 8773), true);
						break;
					case 9:
						pc.toSender(new S_ObjectEffect(pc, 8686), true);
						break;
					case 10:
						pc.toSender(new S_ObjectEffect(pc, 8684), true);
						pc.toSender(new S_ObjectEffect(pc, 8685), true);
						pc.toSender(new S_ObjectEffect(pc, 8773), true);
						break;
					default:
						if(getEnLevel() >= 11) {
						pc.toSender(new S_ObjectEffect(pc, 8683), true);
						pc.toSender(new S_ObjectEffect(pc, 8684), true);
						pc.toSender(new S_ObjectEffect(pc, 8685), true);
						pc.toSender(new S_ObjectEffect(pc, 8773), true);
						pc.toSender(new S_ObjectEffect(pc, 8686), true);
						}
						break;
				}
				
			}
			toEnchantOption(pc, true);
		} else {
			// \f1%0%s 강렬하게%1 빛나더니 증발되어 사라집니다.
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 164, toString(), " 붉게"));
			//
			Inventory inv = pc.getInventory();
			if(getEnLevel()>=8) {
			pc.toSender(new S_ObjectEffect(pc, 8687), true);
			}
			//
			if (equipped) {
				setEquipped(false);
				toSetoption(pc, true);
				toEquipped(pc, inv, getSlot());
				toOption(pc, true);
				toBuffCheck(pc);
			}
			// 인벤에서 제거하면서 메모리도 함께 제거함.
			inv.count(this, 0, true);
		}
		//
		super.toEnchant(pc, en);
	}

}
