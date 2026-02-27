package lineage.world.object.instance;

import java.sql.Connection;
import java.util.Calendar;
import java.util.StringTokenizer;

import lineage.bean.database.ItemSkill;
import lineage.bean.database.Skill;
import lineage.bean.lineage.Inventory;
import lineage.database.ItemSkillDatabase;
import lineage.database.PolyDatabase;
import lineage.database.SkillDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CharacterAttackRange;
import lineage.network.packet.server.S_InventoryCount;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryEquippedWindow;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectGfx;
import lineage.network.packet.server.S_ObjectMode;
import lineage.network.packet.server.S_SoundEffect;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.FishingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.magic.DecayPotion;
import lineage.world.object.magic.EraseMagic;
import lineage.world.object.magic.ShapeChange;
import static lineage.world.controller.DamageController.att;

public class ItemWeaponInstance extends ItemIllusionInstance {

	protected int skill_dmg;
	protected int skill_effect;
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ItemWeaponInstance();
		return item;
	}

	@Override
	public void close(){
		super.close();
		
		skill_dmg = skill_effect = 0;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if(isLvCheck(cha)) {
			if(isClassCheck(cha)){
				//if ((isEquippedGfx(cha) && PolyDatabase.toEquipped(cha, this)) || equipped || getItem().getType2().equals("fishing_rod")) {
				if(PolyDatabase.toEquipped(cha, this) || equipped || getItem().getType2().equals("fishing_rod")) {
					//if (!equipped)
					//	ChattingController.toChatting(cha, String.format("\\fY[%s] 안전인첸트: %d", getItem().getName(), getItem().getSafeEnchant()), Lineage.CHATTING_MODE_MESSAGE);
					Inventory inv = cha.getInventory();
					ItemInstance weapon = inv.getSlot(Lineage.SLOT_WEAPON);
					if (weapon != null && weapon.isEquipped() && weapon.getObjectId() != this.getObjectId()) {
						weapon.toClick(cha, null);
					}
						if (!equipped)
							 if (getItem().getType2().equalsIgnoreCase("dagger")) {
								 // 단검
								 cha.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 2825)); 
							} else if (getItem().getType2().equalsIgnoreCase("sword")) {
								 // 한손검
								 cha.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 2826)); 
							} else if (getItem().getType2().equalsIgnoreCase("tohandsword") || getItem().getType2().equalsIgnoreCase("spear") || getItem().getType2().equalsIgnoreCase("axe")) {
								 // 양손검
								 cha.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 2828)); 
							} else if (getItem().getType2().equalsIgnoreCase("wand") || getItem().getType2().equalsIgnoreCase("staff")) {
								 // 지팡이
								 cha.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 2831)); 
							} else if (getItem().getType2().equalsIgnoreCase("bow")) {
								 // 활
								 cha.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 2835)); 
							} else if (getItem().getType2().equalsIgnoreCase("fishing_rod")) {
								 //낚싯대
								 cha.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 2833)); 
							}
					
					if(inv != null){
						int slot = item.getSlot();
						if(equipped){
							if(bress==2){
								// \f1그렇게 할 수 없습니다. 저주 받은 것 같습니다.
								cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 150));
								return;
							}
							setEquipped(false);


		
							/*
							if(getItem().getType2().equalsIgnoreCase("bow") && 
									cha.getClassType() == 0x02
									&& !(cha instanceof RobotInstance)){
								Skill skill = SkillDatabase.find(19, 7);
								if(skill != null)
									SkillController.remove(cha, skill, false);
							}*/
							
							// 양손무기 해제시 스턴삭제 (기사만해당)
							// 한손무기 스턴 해제하고싶음 여기.성환
						//	if(getItem().getType2().equalsIgnoreCase("sword") && cha.getClassType()==1){
						//		Skill skill = SkillDatabase.find(5, 7);
						//		if(skill != null)
						//			SkillController.remove(cha, skill, false);
						//	}
							/*
							if(getItem().isTohand()){
								if(cha.getClassType() == 1 && SkillController.find(cha, 5, 7) != null){
									Skill skill = SkillDatabase.find(5, 7);
									if(skill != null)
										SkillController.remove(cha, skill, false);
								}
							}*/
							
							slot = this.slot;


							if (getItem().getType2().equals("fishing_rod")) {
								cha.setFishStartHeading(0);
								cha.setFishing(false);
								cha.setFishingTime(0L);
								FishingController.setFishEffect(cha);
								ChattingController.toChatting(cha, "낚시를 종료합니다.", Lineage.CHATTING_MODE_MESSAGE);
								PcInstance pc = (PcInstance)cha;
								if (pc.isChangeDarkelf()) {
									if (cha.getClassSex() == 0) { // 남자일때
										cha.setClassGfx(2786);
										cha.setGfx(cha.getClassGfx());
									} else {// 여자일때
										cha.setClassGfx(2796);
										cha.setGfx(cha.getClassGfx());
									}
									cha.toSender(S_ObjectGfx.clone(BasePacketPooling.getPool(S_ObjectGfx.class), cha), true);
								}
							}
						} else {
							// 낚시대 및 낚시존 확인
							if (getItem().getType2().equals("fishing_rod") && !isFishingZone(cha)) {
								ChattingController.toChatting(cha, "낚시터에서 사용 가능합니다.",
										Lineage.CHATTING_MODE_MESSAGE);
								return;
							}
							if (getItem().getType2().equals("fishing_rod")) {
								// 변신 확인
								if (cha.getGfx() != 0 && cha.getGfx() != 1 && cha.getGfx() != 37 && cha.getGfx() != 48
										&& cha.getGfx() != 61 && cha.getGfx() != 138 && cha.getGfx() != 734
										&& cha.getGfx() != 1186) {
									cha.setTempFishing(this);
									
									// 낚시를 시작하면 변신이 해제됩니다.
									ChattingController.toChatting(cha, "낚시를 시작하면 변신이 해제됩니다.", Lineage.CHATTING_MODE_MESSAGE);
									BuffController.remove(cha, ShapeChange.class);
									// 모든 버프 제거.
									//BuffController.removeAll(cha);
									return;
								} else {
									cha.setFishStartHeading(cha.getHeading());
									cha.setFishing(true);
									cha.setFishingTime(System.currentTimeMillis());
									FishingController.setFishEffect(cha);
									ChattingController.toChatting(cha, "낚시를 시작합니다.", Lineage.CHATTING_MODE_MESSAGE);
								}
							}
						
						//	if(getItem().getType2().equalsIgnoreCase("sword") && cha.getClassType()==1){
						//		Skill skill = SkillDatabase.find(5, 7);
						//		SkillController.find(cha).add( skill );
						//		SkillController.sendList(cha);
						//	}
							
							if (getItem().isTohand()) {
								if (inv.getSlot(Lineage.SLOT_SHIELD) != null) {
									if (!Lineage.item_equipped_type) {
										// \f1방패를 착용하고서는 두손 무기를 쓸수 없습니다.
										cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 128));
										return;
									} else {
										inv.getSlot(Lineage.SLOT_SHIELD).toClick(cha, null);
									}
								} 
								// 양손무기 착용시 스턴추가 (기사만해당)
							/*	if(cha.getClassType() == 1){
									Skill skill = SkillDatabase.find(5, 7);
									SkillController.find(cha).add( skill );
									SkillController.sendList(cha);
								}*/
							}
							
							
							if(inv.getSlot(8) != null){
								// 한손 도끼일경우 슬레이어 페시브스킬 있을때 쌍도처리하기. (착용중인 무기가 양손이 아닐때)
								if(getItem().getGfxMode()==0x0B && !getItem().isTohand() && !inv.getSlot(8).getItem().isTohand() && SkillController.find(cha, 29, 12)!=null) {
									if(inv.getSlot(7) != null) {
										// \f1방패를 착용하고서는 두손 무기를 쓸수 없습니다.
										cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 128));
										return;
									}
									slot = 7;
									cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 12534), true);
								} else {
									if(Lineage.item_equipped_type && inv.getSlot(8).getBress()!=2){
										inv.getSlot(8).toClick(cha, null);
									}else{
										// \f1이미 뭔가를 착용하고 있습니다.
										cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 124));
										return;
									}
								}
							}
							/*
							if(getItem().getType2().equalsIgnoreCase("bow") && cha.getClassType() == 0x02
									&& !(cha instanceof RobotInstance)){
								Skill skill = SkillDatabase.find(19, 7);
								SkillController.find(cha).add( skill );
								SkillController.sendList(cha);
							}*/

							
							setEquipped(true);
						}

						toSetoption(cha, true);
						toEquipped(cha, inv, slot);
						toOption(cha, true);
						toBuffCheck(cha);
					}
				} else {
					ChattingController.toChatting(cha, "착용할 수 없습니다.", 20);
				}
			}else{
				// \f1당신의 클래스는 이 아이템을 사용할 수 없습니다.
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 264));
			}
		}
	  }


	/**
	 * 무기 착용 및 해제 처리 메서드.
	 */
	@Override
	public void toEquipped(Character cha, Inventory inv, int slot){
		if(inv == null)
			return;
		
		if(equipped){
			//*/안전인첸6 / 인첸9렙 이상 / 물무기/*
		/*	if(getEnWater()!=0){
			
					switch(getEnWater()){
						case 1: cha.setWaterWeapon(1);
							break;
						case 2:cha.setWaterWeapon(2);
							break;
						case 3:cha.setWaterWeapon(3);
							break;
						case 4:cha.setWaterWeapon(4);
							break;
						case 5:cha.setWaterWeapon(5);
							break;
					}							
				
			}
			/*item.getSafeEnchant()==6 && getEnLevel()>8 && getEnWind()==5||
				(item.getSafeEnchant()==0 && getEnLevel()>2 && getEnWind()==5)*/
			if(getEnWind()!=0){
				
					switch(getEnWind()){
						case 1: cha.setWindWeapon(1);
							break;
						case 2:cha.setWindWeapon(2);
							break;
						case 3:cha.setWindWeapon(3);
							break;
						case 4:cha.setWindWeapon(4);
							break;
						case 5:cha.setWindWeapon(5);
							break;
					}
				
			}
			if(getEnEarth()!=0){
				
					switch(getEnEarth()){
						case 1: cha.setEarthWeapon(1);
							break;
						case 2: cha.setEarthWeapon(2);
							break;
						case 3:cha.setEarthWeapon(3);
							break;
						case 4:cha.setEarthWeapon(4);
							break;
						case 5:cha.setEarthWeapon(5);
							break;
					}
				
			}
			if(getEnFire()!=0){
				
					switch(getEnFire()){
						case 1: cha.setFireWeapon(1);
							break;
						case 2:cha.setFireWeapon(2);
							break;
						case 3:cha.setFireWeapon(3);
							break;
						case 4:cha.setFireWeapon(4);
							break;
						case 5:cha.setFireWeapon(5);
							break;
					}	
				
			}
			
			if(getFour()>10){
				switch(getFour()){
				case 11:cha.setSix(11);
					break;
				case 13:cha.setSix(13);
					break;
				case 14:cha.setSix(14);
					break;
				case 17:cha.setSix(17);
					break;
				case 18:cha.setSix(18);
					break;
				case 20:cha.setSix(20);
					break;
				case 22:cha.setSix(22);
					break;
				case 23:cha.setSix(23);SkillController.append(cha, 28, true);
						SkillController.sendList(cha);
					break;
				case 24:cha.setSix(24);SkillController.append(cha, 71, true);
						SkillController.sendList(cha);
					break;
				case 25:cha.setSix(25);SkillController.append(cha, 53, true);
					SkillController.sendList(cha);
					break;
				case 27:cha.setSix(27);
					break;
					/*switch(cha.getSix()){
					case 23 :SkillController.remove(cha, 28, true);
					break;
					case 24 :SkillController.remove(cha, 71, true);
					break;
					case 25 :SkillController.remove(cha, 53, true);
					break;
					}*/
				}
			}
			inv.setSlot(slot, this);
			/*if(cha.getGfx()==cha.getClassGfx() || Lineage.server_version>=270){
				// 변신상태가 아닐때만 변경하도록 함.
				if(cha.getGfxMode()==0x0B && item.getGfxMode()==0x0B)
					// 현재 도끼착용상태인데 다시 도끼를 착용하려 한다면 쌍수로 판단 mode를 강제 처리.
					cha.setGfxMode(cha.getGfxMode() + 77);
				else
					cha.setGfxMode(cha.getGfxMode()+item.getGfxMode());
				cha.toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), cha), true);
			}*/
			//랭크변신 착용 시에 GFX 설정하기. 
			if (cha.getGfx()==17078 || cha.getGfx()==17083 || cha.getGfx()==19082 || cha.getGfx()==18942 || cha.getGfx()==21248
					|| cha.getGfx()==21066 || cha.getGfx()==17053 || cha.getGfx()==17058
					|| cha.getGfx()==13739 || cha.getGfx()==13741 || cha.getGfx()==21832 || cha.getGfx()==22258 || cha.getGfx()==17541
					|| (cha.getGfx()>=8922 && cha.getGfx()<=8956)){
				int i = 0;
				switch(item.getGfxMode()){
				case 0x00: i = 0x00;
					break;
				case 0x04: i = 0x04;
					break;
				case 0x2E: i = 0x2E;
					break;
				case 0x32: i = 0x32;
					break;
				case 0x0B: i = 0x0B;
					break;
				case 0x14: i = 0x14;
					break;
				case 0x18: i = 0x18;
					break;
				case 0x28: i = 0x28;
					break;
				case 0x42: i = 0x42;
					break;
				}
				cha.setGfxMode(i);
				cha.toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), cha), true);
		}
			
		else if(cha.getGfx()==cha.getClassGfx() || Lineage.server_version>=270){
				// 변신상태가 아닐때만 변경하도록 함.
				cha.setGfxMode(cha.getGfxMode()+item.getGfxMode());
				cha.toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), cha), true);
			}
		else if((cha.getGfx()==15660 ||cha.getGfx()==15657 || cha.getGfx()==3869 || cha.getGfx()==2377) && item.getType2().equalsIgnoreCase("spear")) {	
				
				cha.setGfxMode(cha.getGfxMode()+item.getGfxMode());
				cha.toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), cha), true);
			}
		else if(item.getGfxMode() == 20) {	
			
			cha.setGfxMode(cha.getGfxMode() + item.getGfxMode());
			cha.toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), cha), true);
		}
		if(getBress()==2) {
			//\f1%0%s 손에 달라 붙었습니다.
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 149, getName()));
		}
		}else{
			inv.setSlot(slot, null);
			if(cha.getWaterWeapon() !=0 || cha.getWindWeapon() !=0 || cha.getEarthWeapon() !=0 || cha.getFireWeapon() !=0 ){
				cha.setWaterWeapon(0);
				cha.setWindWeapon(0);
				cha.setEarthWeapon(0);
				cha.setFireWeapon(0);
			}
			
			if(cha.getSix()!=0){
				switch(cha.getSix()){
				case 23 :SkillController.remove(cha, 28, true);
				break;
				case 24 :SkillController.remove(cha, 71, true);
				break;
				case 25 :SkillController.remove(cha, 53, true);
				break;
				}
				cha.setSix(0);
			}
			
			int i = 0;

			if (cha.getGfx()==17078 || cha.getGfx()==17083 || cha.getGfx()==19082 || cha.getGfx()==18942 || cha.getGfx()==21248
					|| cha.getGfx()==21066 || cha.getGfx()==17053 || cha.getGfx()==17058
					|| cha.getGfx()==13739 || cha.getGfx()==13741 || cha.getGfx()==21832 || cha.getGfx()==22258 || cha.getGfx()==17541
					|| (cha.getGfx()>=8922 && cha.getGfx()<=8956)){
			    switch (item.getGfxMode()) {
			        case 0x00:
			        case 0x04:
			        case 0x2E:
			        case 0x32:
			        case 0x0B:
			        case 0x14:
			        case 0x18:
			        case 0x28:
			        case 0x42:
			            i = 0x00;
			            break;
			    }
			} else if (cha.getGfx() == cha.getClassGfx() || Lineage.server_version >= 270) {
			    i = cha.getGfxMode() - item.getGfxMode();
			} else if ((cha.getGfx() == 15660 || cha.getGfx() == 15657 || cha.getGfx() == 3869 || cha.getGfx() == 2377) && item.getType2().equalsIgnoreCase("spear")) {
			    i = cha.getGfxMode() - item.getGfxMode();
			} else if (item.getGfxMode() == 20) {
			    i = cha.getGfxMode() - item.getGfxMode();
			}

			cha.setGfxMode(i);
			cha.toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), cha), true);
		}
		/*	if(cha.getGfx()==cha.getClassGfx() || Lineage.server_version>=270){
				// 변신상태가 아닐때만 변경하도록 함.
				if(cha.getGfxMode() == 88)
					cha.setGfxMode(cha.getGfxMode() - 77);
				else
					cha.setGfxMode(cha.getGfxMode()-item.getGfxMode());
				cha.toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), cha), true);
			}
		}*/
		cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
		if(Lineage.server_version >= 360)
			cha.toSender(S_InventoryEquippedWindow.clone(BasePacketPooling.getPool(S_InventoryEquippedWindow.class), this, slot));
		// 공격거리 패킷.
		if(Lineage.server_version >= 380)
			cha.toSender(S_CharacterAttackRange.clone(BasePacketPooling.getPool(S_CharacterAttackRange.class), this));
		//
		this.slot = slot;
		// 방패슬롯에 무기를 착용중일경우. (무기 해제모드일때만.)
		//	: 방패슬롯에잇는 무기를 해제하고 원래 무기 슬롯으로 이동하기 위해.
		if(!equipped) {
			ItemInstance ii = inv.getSlot(7);
			if(ii!=null && ii instanceof ItemWeaponInstance) {
				ii.toClick(cha, null);	// 해제
				ii.toClick(cha, null);	// 다시 재 착용.
			}
		}
	}

	/**
	 * 무기를 착용할 경우 해당 gfx에 모션이있는지 체크
	 */
	public boolean isEquippedGfx(Character cha) {
		if (!equipped) {
			if (!SpriteFrameDatabase.findGfxMode(cha.getGfx(), item.getGfxMode() + Lineage.GFX_MODE_ATTACK))
				return false;
			else
				return true;
		} else {
			return true;
		}
	}
	
	/**
	 * 리니지 월드에 접속했을때 착용중인 아이템 처리를 위해 사용되는 메서드.
	 */
	@Override
	public void toWorldJoin(Connection con, PcInstance pc){
		super.toWorldJoin(con, pc);
		if(equipped){
			toSetoption(pc, false);
			int slot = item.getSlot();
			if(pc.getInventory().getSlot(8)!=null && getItem().getGfxMode()==0x0B)
				slot = 7;
			pc.getInventory().setSlot(slot, this);
			setSlot(slot);
			if(pc.getGfx()==pc.getClassGfx() || Lineage.server_version>=270)
				pc.setGfxModeClear();
			if((cha.getGfx()==15660 ||cha.getGfx()==15657 ||cha.getGfx()==17549) && item.getType2().equalsIgnoreCase("spear")) 
				pc.setGfxModeClear();
			if(Lineage.server_version >= 360)
				pc.toSender(S_InventoryEquippedWindow.clone(BasePacketPooling.getPool(S_InventoryEquippedWindow.class), this, slot));
			// 무기공격 거리 전송.
			if(Lineage.server_version >= 380)
				pc.toSender(S_CharacterAttackRange.clone(BasePacketPooling.getPool(S_CharacterAttackRange.class), pc.getInventory().getSlot(8)));
			tojoin(pc);
			toOption(pc, false);
		}
	}

	/**
	 * 인첸트 활성화 됫을때 아이템의 뒷처리를 처리하도록 요청하는 메서드.
	 */
	@Override
	public void toEnchant(PcInstance pc, int en){
		//
		if(en == -127)
			return;
		//
		if(en!=0) {
			// 월드에 메세지 표현처리.
			String worldMessage = null;
			if(Lineage.world_message_enchant_weapon>0 && Lineage.world_message_enchant_weapon<=getEnLevel())
				worldMessage = String.format("어느 아덴 용사의 %s이 찬란히 파랗게 빛납니다.", toStringDB());
			if(worldMessage != null) {
				for(PcInstance use : World.getPcList())
					ChattingController.toChatting(use, worldMessage, 20);
			}
			// 아이템 인첸트값 set
			setEnLevel(getEnLevel() + en);
			if(Lineage.server_version<=144){
				pc.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
				pc.toSender(S_InventoryCount.clone(BasePacketPooling.getPool(S_InventoryCount.class), this));
			}else{
				pc.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
			}
			if (getItem().getSafeEnchant() < getEnLevel()) {
				switch (getEnLevel()) {
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
					if (getEnLevel() >= 11) {
						pc.toSender(new S_ObjectEffect(pc, 8683), true);
						pc.toSender(new S_ObjectEffect(pc, 8684), true);
						pc.toSender(new S_ObjectEffect(pc, 8685), true);
						pc.toSender(new S_ObjectEffect(pc, 8773), true);
						pc.toSender(new S_ObjectEffect(pc, 8686), true);
					}
					break;
				}

			}
			// 성공햇으면서 착용중이면 뭔갈 해줘야할까?
			if(isEquipped()) {
				//
			}
		} else {
			// \f1%0%s 강렬하게%1 빛나더니 증발되어 사라집니다.
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 164, toString(), " 붉게"));
			//
			Inventory inv = pc.getInventory();
			if (getEnLevel() >= 9) {
				pc.toSender(new S_ObjectEffect(pc, 8687), true);
			}
			if(equipped){
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

	@Override
	public boolean toDamage(Character cha, object o){
		if(o==null || cha==null || o.isDead() || cha.isDead() || item==null)
			return false;
		
		// 플러그인 확인.
		Object pco = PluginController.init(ItemWeaponInstance.class, "toDamage", this, cha, o);
		if(pco != null)
			return (Boolean)pco;
		
		boolean r_bool = false;
		
		// 마나 스틸하기
		if(o.getNowMp()>0 && item.getStealMp()>0 && (o instanceof MonsterInstance || o instanceof PcInstance)){
			// 1~4랜덤 추출
			int steal_mp = Util.random(1, item.getStealMp());
				// 만약 고인첸 일경우 7부터 +1 되도록 하기 20180928
			/*	if(getEnLevel()>5){
					steal_mp += getEnLevel()-5;
				}
				*/
			if(getItem().getName().equalsIgnoreCase("마나의 지팡이") ||getItem().getName().equalsIgnoreCase("강철 마나의 지팡이")){
	            steal_mp = Util.random(0, item.getStealMp()+getEnLevel());
	            if(getEnLevel()>7) {
	            	 steal_mp = Util.random(getEnLevel(), item.getStealMp()+getEnLevel()+1);
	            }
	         }else{
	            steal_mp = Util.random(0, item.getStealMp()+getEnLevel());
	         }
				

			// 타켓에 mp가 스틸할 값보다 작을경우 현재 가지고있는 mp값으로 변경
			if(steal_mp>0 ){
				if(o.getNowMp()<steal_mp)
					steal_mp = o.getNowMp();
				
				if(cha.getNowMp() < cha.getTotalMp()){
					// mp제거하기.
					o.setNowMp(o.getNowMp()-steal_mp);
					// mp추가하기.
					cha.setNowMp(cha.getNowMp()+steal_mp);
					r_bool = true;
				}
			}
		}
		
		// 체력 스틸하기
		if(o.getNowHp()>0 && item.getStealHp()>0 && (o instanceof MonsterInstance || o instanceof PcInstance)){
			// 1~3랜덤 추출
			int steal_hp = Util.random(1, item.getStealHp());
			// 인첸트 수치만큼 +@
			if(getEnLevel()>0){
				steal_hp += getEnLevel();
				// 만약 고인첸 일경우 7부터 +1 되도록 하기
				if(getEnLevel()>6)
					steal_hp += getEnLevel()-6;
			}
			// 타켓에 mp가 스틸할 값보다 작을경우 현재 가지고있는 mp값으로 변경
			if(o.getNowHp()<steal_hp)
				steal_hp = (o.getNowHp()-1);
			if(steal_hp <= 0)
				steal_hp = 0;
			// hp제거하기.
			o.setNowHp(o.getNowHp()-steal_hp);
			// hp추가하기.
			cha.setNowHp(cha.getNowHp()+steal_hp);
			r_bool = true;
		}
		
		// 마법 발동.
		skill_dmg = skill_effect = 0;
		if(att==false){
		ItemSkill is = ItemSkillDatabase.find(getItem().getName());
		if(is != null) {
			// 인첸에 따른 확률 상향.
			int chance = is.getChance() + (is.getChanceEnLevel() * getEnLevel());
			// 이레이즈 매직이 걸려있을때에 무기(마법)삭제
			if(o instanceof PcInstance){
				if(BuffController.find(o).find(EraseMagic.class) != null) {
					BuffController.remove(o, EraseMagic.class);
				}
			}
			//디케이
			if(Util.random(0, 100)<=chance && is.getSkill()==71){

				att = true;
				r_bool = true;
				// 데미지 +@ 처리
				if(is.getMinDmg()>0 && is.getMaxDmg()>=is.getMinDmg())
					skill_dmg += Util.random(is.getMinDmg(), is.getMaxDmg());
				// 스킬을 통한 데미지와 이팩트 추출.
				Skill skill = SkillDatabase.find(is.getSkill());
				if(skill!=null && !SkillController.toSkill(cha, o, skill, is.isEffectTarget(), is.getDuration(), is.getOption())){
					skill_dmg += SkillController.getDamage(cha, o, o, skill, getEnLevel(), is.getElement()==Lineage.ELEMENT_NONE ? skill.getElement() : is.getElement());
					skill_effect = skill.getCastGfx();
				}
				// 스킬이팩트에 의존하지않고 있을경우 
				if(is.getEffect() > 0)
					skill_effect = is.getEffect();
				// 옵션 정보에 따라 이팩트 표현처리가 따로 있을때.
				if(is.getOption().startsWith("enchantEffect") && is.getOption()!=null){
					StringTokenizer st = new StringTokenizer(is.getOption(), "|");
					while(st.hasMoreTokens()){
						try {
							String data = st.nextToken();
							if(data.length() <= 0)
								continue;
							String[] option = data.split(" ");
							Integer enLv = Integer.valueOf( option[0] );
							Integer effectId = Integer.valueOf( option[1] );
							Integer dmg = Integer.valueOf( option[2] );
							if(getEnLevel() >= enLv){
								skill_effect = effectId;
								skill_dmg += dmg;
							}
						} catch (Exception e) { }
					}
				}
				// 이팩트가 자기자신에게 표현해야할경우 처리.
				if(is.isEffectTarget() && skill_effect>0){
					cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill_effect), true);
					skill_effect = 0;
				}
				
				//디케이 발동
				DecayPotion.init((Character)o, 5);
			}
			//
			else if(chance>=100 || Util.random(0, 100)<=chance) {
				att = true;
				r_bool = true;
				// 데미지 +@ 처리
				if(is.getMinDmg()>0 && is.getMaxDmg()>=is.getMinDmg())
					skill_dmg += Util.random(is.getMinDmg(), is.getMaxDmg());
				// 스킬을 통한 데미지와 이팩트 추출.
				Skill skill = SkillDatabase.find(is.getSkill());
				if(skill!=null && !SkillController.toSkill(cha, o, skill, is.isEffectTarget(), is.getDuration(), is.getOption())){
					skill_dmg += SkillController.getDamage(cha, o, o, skill, getEnLevel(), is.getElement()==Lineage.ELEMENT_NONE ? skill.getElement() : is.getElement());
					skill_effect = skill.getCastGfx();
				}
				// 스킬이팩트에 의존하지않고 있을경우 
				if(is.getEffect() > 0)
					skill_effect = is.getEffect();
				// 옵션 정보에 따라 이팩트 표현처리가 따로 있을때.
				if(is.getOption().startsWith("enchantEffect") && is.getOption()!=null){
					StringTokenizer st = new StringTokenizer(is.getOption(), "|");
					while(st.hasMoreTokens()){
						try {
							String data = st.nextToken();
							if(data.length() <= 0)
								continue;
							String[] option = data.split(" ");
							Integer enLv = Integer.valueOf( option[0] );
							Integer effectId = Integer.valueOf( option[1] );
							Integer dmg = Integer.valueOf( option[2] );
							if(getEnLevel() >= enLv){
								skill_effect = effectId;
								skill_dmg += dmg;
							}
						} catch (Exception e) { }
					}
				}
				// 이팩트가 자기자신에게 표현해야할경우 처리.
				if(is.isEffectTarget() && skill_effect>0){
					cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill_effect), true);
					skill_effect = 0;
				}
			}
		}
		}
		if(cha.getSix()!=0 && Util.random(0, 100)<=10){
			r_bool=true;
			
			switch(cha.getSix()){
			case 14: //오크의 심장
				skill_effect = 8739; //나한테 서짐..근데 이건 봐서.
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill_effect), true);
				skill_effect = 0;
				break;
			case 17: //멜로디
				skill_effect = 4238;
				break;
			case 18: //제퍼
				skill_effect = 2551; // 본인한테 써짐.
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill_effect), true);
				skill_effect = 0;
				break;
			case 20: //신뢰
				skill_effect = 7004;
				break;
			case 27: //초승달				
				skill_effect = 7986; //  <<<개허접인데? 다시 확인
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill_effect), true);
				skill_effect = 0;
				break;
			}
			skill_dmg += 100;
		}
		// 크리티컬 이팩트 처리.
		if(getItem().getCriticalEffect() > 0) {
			if(Util.random(0, 100) >= 10)
				skill_effect = getItem().getCriticalEffect();
		}
		
		if(cha.isKnightHoly()){
			if(Util.random(0, 100) >=5)
				skill_effect = 4591; 
				//o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill_effect), true);
		}
		
		if(att == true)
			att = false;
		
		return r_bool;
	}
	
	@Override
	public int toDamage(int dmg){
		// 
		PluginController.init(ItemWeaponInstance.class, "toDamage", this, dmg+skill_dmg);
		//
		if(item.getCriticalEffect() > 0)
			return skill_effect==0 ? 0 : dmg;
		else
			return skill_dmg;
	}
	
	@Override
	public int toDamageEffect(){
		return skill_effect;
	}
	
	@Override
	public boolean isDamageEffectTarget() {
		ItemSkill is = ItemSkillDatabase.find(getItem().getName());
		return is==null ? true : is.isEffectTarget();
	}

	public void setSkillDmg(int skillDmg) {
		skill_dmg = skillDmg;
	}

	public void setSkillEffect(int skillEffect) {
		skill_effect = skillEffect;
	}
	
	public boolean isFishingZone(Character cha) {
		return cha.getMap() == 5124 && cha.getX() >= Lineage.FISHZONEX1 && cha.getX() <= Lineage.FISHZONEX2
				&& cha.getY() >= Lineage.FISHZONEY1 && cha.getY() <= Lineage.FISHZONEY2;
	}
	
	/*무기에 관해서 한번더 셋팅해주기.*/
	private void tojoin(Character cha){
		if(getEnWater()!=0){
									switch(getEnWater()){
										case 1: cha.setWaterWeapon(1);
											break;
										case 2:cha.setWaterWeapon(2);
											break;
										case 3:cha.setWaterWeapon(3);
											break;
										case 4:cha.setWaterWeapon(4);
											break;
										case 5:cha.setWaterWeapon(5);
											break;
									}
		}

							if(getEnWind()!=0){
						
									switch(getEnWind()){
										case 1: cha.setWindWeapon(1);
											break;
										case 2:cha.setWindWeapon(2);
											break;
										case 3:cha.setWindWeapon(3);
											break;
										case 4:cha.setWindWeapon(4);
											break;
										case 5:cha.setWindWeapon(5);
											break;
									}
								
							}
							if(getEnEarth()!=0){
						
									switch(getEnEarth()){
										case 1: cha.setEarthWeapon(1);
											break;
										case 2:cha.setEarthWeapon(2);
											break;
										case 3:cha.setEarthWeapon(3);
											break;
										case 4:cha.setEarthWeapon(4);
											break;
										case 5:cha.setEarthWeapon(5);
											break;
									}
								
							}
							if(getEnFire()!=0){
							
									switch(getEnFire()){
										case 1: cha.setFireWeapon(1);
											break;
										case 2:cha.setFireWeapon(2);
											break;
										case 3:cha.setFireWeapon(3);
											break;
										case 4:cha.setFireWeapon(4);
											break;
										case 5:cha.setFireWeapon(5);
											break;
									}	
							
							}
							
							if(getFour()>10){
								switch(getFour()){
								case 11:cha.setSix(11);
									break;
								case 13:cha.setSix(13);
									break;
								case 14:cha.setSix(14);
									break;
								case 17:cha.setSix(17);
									break;
								case 18:cha.setSix(18);
									break;
								case 20:cha.setSix(20);
									break;
									
								case 22:cha.setSix(22);
									break;
								case 23:cha.setSix(23);SkillController.append(cha, 28, true);
										SkillController.sendList(cha);
									break;
								case 24:cha.setSix(24);SkillController.append(cha, 71, true);
										SkillController.sendList(cha);
									break;
								case 25:cha.setSix(25);SkillController.append(cha, 53, true);
									SkillController.sendList(cha);
									break;
								case 27:cha.setSix(27);
									break;
									/*switch(cha.getSix()){
									case 23 :SkillController.remove(cha, 28, true);
									break;
									case 24 :SkillController.remove(cha, 71, true);
									break;
									case 25 :SkillController.remove(cha, 53, true);
									break;
									}*/
								}
							}
	}
	
}