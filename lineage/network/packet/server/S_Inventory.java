package lineage.network.packet.server;

import lineage.bean.database.Exp;
import lineage.bean.database.Item;
import lineage.bean.database.ItemSetoption;
import lineage.database.ExpDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ItemSetoptionDatabase;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.item.Candle;
import lineage.world.object.item.DogCollar;
import lineage.world.object.item.Letter;
import lineage.world.object.item.MagicDoll;
import lineage.world.object.item.armor.창천갑옷;
import lineage.world.object.item.helm.Turban;
import lineage.world.object.item.pet.PetArmor;
import lineage.world.object.item.pet.PetWeapon;
import lineage.world.object.item.sp.CharacterSaveMarbles;
import lineage.world.object.item.wand.EbonyWand;
import lineage.world.object.item.wand.EbonyWand2;
import lineage.world.object.item.wand.MapleWand;
import lineage.world.object.item.wand.PineWand;
import lineage.world.object.item.weapon.Alice;
import lineage.world.object.item.weapon.창천무기;

public class S_Inventory extends ServerBasePacket {

	protected void toArmor(ItemInstance item) {
		if (item.getItem().getName().equalsIgnoreCase("메티스의 귀걸이") || item.getItem().getNameIdNumber() == 431)
			toEtc(item.getItem(), item.getWeight());
		else
			toArmor(item.getItem(), item.getDurability(), item.getEnLevel(), item.getWeight(), item.getDynamicMr(), item.getBress(), item.getDynamicStunDefense(), item.getDynamicSp(), item.getDynamicReduction());
			
	}

	protected void toWeapon(ItemInstance item) {
		if (item.getItem().getType2().equalsIgnoreCase("fishing_rod"))
			toEtc(item.getItem(), item.getWeight());
		else
			toWeapon(item.getItem(), item.getDurability(), item.getEnLevel(), item.getWeight(), item.getBress(), item.getDynamicMr(), item.getDynamicStunDefense(), item.getDynamicSp(), item.getDynamicReduction());
	}

	protected void toEtc(ItemInstance item) {
		if (item.getItem().getNameIdNumber() == 1173) {
			DogCollar dc = (DogCollar) item;
			writeC(0x0f); // 15
			writeC(0x19);
			writeH(dc.getPetClassId());
			writeC(0x1a);
			writeH(dc.getPetLevel());
			writeC(0x1f);
			writeH(dc.getPetHp());
			writeC(0x17);
			writeC(item.getItem().getMaterial());
			writeD(item.getWeight());
		} else {
			if (!item.getItem().getType2().equalsIgnoreCase("sword_lack") && !item.getItem().getType2().equalsIgnoreCase("자동 칼질") && !item.getItem().getType2().equalsIgnoreCase("자동 물약"))
				toEtc(item.getItem(), item.getWeight());
		}
	}
	// 1 타격치
    // 2 X
    // 3 손상도
    // 4 양손 무기
    // 5 공격 성공 +
    // 6 추가데미지 +
    // 7 [사용가능] 클래스
    // 8 STR [힘]
    // 9 DEX [텍스]
    // 10 CON [콘]
    // 11 WIS [위즈]
    // 12 INT [인트]
    // 13 CHA [카리]
    // 14 최대 HP +
    // 15 마법방어 +
    // 16 마나흡수
    // 17 주술력 +
    // 18 헤이스트효과
    // 19 AC +
    // 20 원거리 명중
    // 21 영양
    // 22 밝기
    // 23 무게
    // 24 최대 MP + > 원거리 데미지로 교체
    // 25 종류
    // 26 레벨
    // 27 물약 회복량(%)
    // 28 MP 회복
    // 29 대미지 감소
    // 30 원거리 대미지
	protected void toArmor(Item item, int durability, int enlevel, int weight, int dynamic_mr, int bless, double dynamic_stun_defence, int dynamic_sp, int dynamic_reduction) {
		ItemSetoption setoption = Lineage.server_version >= 163 ? ItemSetoptionDatabase.find(item.getSetId()) : null;
		writeC(getOptionSize(item, durability, enlevel, setoption, bless, dynamic_mr, dynamic_stun_defence, dynamic_sp, dynamic_reduction));
		writeC(19);
		writeC(item.getAc());
		writeC(item.getMaterial());
		if (Lineage.server_version > 300)
			writeC(-1); // Grade
		writeD(weight);

		// AC 0+enlevel
		if (enlevel != 0) {
			int ac = enlevel;

			// 장신구는 따로 처리
			if (item.isAccessory()) {
				ac = 0;
			}

			writeC(0x02);
			writeC(ac);
		}

		int type = item.getRoyal() == 0 ? 0 : 1;
		type += item.getKnight() == 0 ? 0 : 2;
		type += item.getElf() == 0 ? 0 : 4;
		type += item.getWizard() == 0 ? 0 : 8;
		if (Lineage.server_version >= 270) {
			type += item.getDarkElf() == 0 ? 0 : 16;
			type += item.getDragonKnight() == 0 ? 0 : 32;
			type += item.getBlackWizard() == 0 ? 0 : 64;
		} else {
			if (item.getType1().equalsIgnoreCase("petArmor"))
				type += 64;
		}
		writeC(7);
		writeC(type);

		if (item.getAddHit() != 0) {
			writeC(5);
			writeC(item.getAddHit());
		}
		
		if (item.getAddDmg() != 0) {
			writeC(6);
			writeC(item.getAddDmg());
		}
		
		if (item.getAddBowHit() != 0) {
			writeC(20);
			writeC(item.getAddBowHit());
		}

		if (item.getAddBowDmg() != 0) {
			writeC(6);
			writeC(item.getAddBowDmg());
		}
		
		if (item.getAddStr() != 0) {
			writeC(8);
			writeC(item.getAddStr());
		}
		
		if (item.getAddDex() != 0) {
			writeC(9);
			writeC(item.getAddDex());
		}
		
		if (item.getAddCon() != 0) {
			writeC(10);
			writeC(item.getAddCon());
		}
		
		if (item.getAddInt() != 0) {
			writeC(12);
			writeC(item.getAddInt());
		}
		
		if (item.getAddWis() != 0) {
			writeC(11);
			writeC(item.getAddWis());
		}
		
		if (item.getAddCha() != 0) {
			writeC(13);
			writeC(item.getAddCha());
		}
		
		if (item.getAddHp() != 0) {
			writeC(14);
			writeH(item.getAddHp());
		}
		
		if(item.getAddMr()!=0 || dynamic_mr!=0){
			writeC(15);
			writeH(item.getAddMr() + dynamic_mr);
		}
		
		if(item.getAddSp() != 0){
			writeC(17);
			writeC(item.getAddSp());
		}
		
		if (setoption != null && (setoption.isBrave() || setoption.isHaste()))
			writeC(18);
		
		if (item.getAddMp() != 0) {
			writeC(24);
			writeC(item.getAddMp());
		}
	}
	protected void toWeapon(Item item, int durability, int enlevel, int weight, int bless, int dynamic_mr, double dynamic_stun_defence, int dynamic_sp, int dynamic_reduction) {
		ItemSetoption setoption = Lineage.server_version >= 163 ? ItemSetoptionDatabase.find(item.getSetId()) : null;
		writeC(getOptionSize(item, durability, enlevel, setoption, bless, enlevel * item.getEnchantMr(), enlevel * item.getEnchantStunDefense(), enlevel * item.getEnchantSp(), enlevel * item.getEnchantReduction()));
		writeC(0x01);
		writeC(item.getDmgMin());
		writeC(item.getDmgMax());
		writeC(item.getMaterial());
		writeD(weight);
		if (enlevel != 0) {
			writeC(0x02);
			writeC(enlevel);
		}
		if (durability != 0) {
			writeC(3);
			writeC(durability);
		}

		if (item.isTohand())
			writeC(4);
		
		if (item.getAddHit() != 0) {
			writeC(5);
			writeC(item.getAddHit());
		}
		if (item.getAddBowHit() != 0) {
			writeC(5);
			writeC(item.getAddBowHit());
		}
		if (item.getAddDmg() != 0) {
			int addDmg = item.getAddDmg();
			
			writeC(6);
			writeC(addDmg);
		}
		
		if (item.getAddBowDmg() != 0) {
			int addbowdmg = item.getAddBowDmg();
			
			writeC(6);
			writeC(addbowdmg);
		}
		
		int type = item.getRoyal() == 0 ? 0 : 1;
		type += item.getKnight() == 0 ? 0 : 2;
		type += item.getElf() == 0 ? 0 : 4;
		type += item.getWizard() == 0 ? 0 : 8;
		if (Lineage.server_version >= 270) {
			type += item.getDarkElf() == 0 ? 0 : 16;
			type += item.getDragonKnight() == 0 ? 0 : 32;
			type += item.getBlackWizard() == 0 ? 0 : 64;
		} else {
			if (item.getType1().equalsIgnoreCase("petWeapon"))
				type += 64;
		}
		writeC(7);
		writeC(type);

		if (item.getAddStr() != 0) {
			writeC(8);
			writeC(item.getAddStr());
		}

		if (item.getAddDex() != 0) {
			writeC(9);
			writeC(item.getAddDex());
		}

		if (item.getAddCon() != 0) {
			writeC(10);
			writeC(item.getAddCon());
		}

		if (item.getAddWis() != 0) {
			writeC(11);
			writeC(item.getAddWis());
		}

		if (item.getAddInt() != 0) {
			writeC(12);
			writeC(item.getAddInt());
		}

		if (item.getAddCha() != 0) {
			writeC(13);
			writeC(item.getAddCha());
		}
		
		if ((item.getAddSp() != 0 || dynamic_sp != 0)) {
			int sp = item.getAddSp() + dynamic_sp;
			
			writeC(17);
			writeC(sp);
		}

		if (item.getAddHp() != 0) {
			writeC(14);
			writeH(item.getAddHp());
		}
		
		if (item.getAddMp() != 0) {
			writeC(24);
			writeC(item.getAddMp());
		}
		
		if (item.getStealMp() != 0) {
			writeC(16);
		}
		
		if (item.getAddMr() != 0 || dynamic_mr != 0) {
			int addMr = item.getAddMr() + dynamic_mr;
			
			if (addMr > 0) {
				writeC(15);
				writeH(addMr);
			}
		}
		if (setoption != null && (setoption.isBrave() || setoption.isHaste()))
			writeC(18);
	}

	protected void toEtc(Item item, int weight) {
		writeC(0x06);
		writeC(0x17);
		writeC(item.getMaterial());
		writeD(weight);
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
				String name = null;
				String one = null;
				String two = null;
				String three = null;
				String four = null;

				String five = null;

				Integer element_en = 0;

				// 추가------------1
				if (item.getOne() == 1) {
					one = "수";
				}
				if (item.getOne() == 2) {
					one = "풍";
				}
				if (item.getOne() == 3) {
					one = "화";
				}
				if (item.getOne() == 4) {
					one = "지";
				}
				if (item.getTwo() == 1) {
					two = "수";
				}
				if (item.getTwo() == 2) {
					two = "풍";
				}
				if (item.getTwo() == 3) {
					two = "화";
				}
				if (item.getTwo() == 4) {
					two = "지";
				}
				if (item.getThree() == 1) {
					three = "수";
				}
				if (item.getThree() == 2) {
					three = "풍";
				}
				if (item.getThree() == 3) {
					three = "화";
				}
				if (item.getThree() == 4) {
					three = "지";
				}
				if (item.getFour() == 1) {
					four = "수";
				}
				if (item.getFour() == 2) {
					four = "풍";
				}
				if (item.getFour() == 3) {
					four = "화";
				}
				if (item.getFour() == 4) {
					four = "지";
				}

				if (one != null) {
					sb.append(one);
				}
				if (two != null) {
					sb.append(two);
				}
				if (three != null) {
					sb.append(three);
				}
				if (four != null) {
					sb.append(four);
				}

				if (item.getFour() > 10) {
					switch (item.getFour()) {
					case 11:
						five = "신성한번개";
						break;
					case 12:
						five = "왕의 자비";
						break;
					case 13:
						five = "신성한 불꽃";
						break;
					case 14:
						five = "오크의 심장";
						break;
					case 15:
						five = "통찰력";
						break;
					case 16:
						five = "야수";
						break;
					case 17:
						five = "멜로디";
						break;
					case 18:
						five = "제퍼";
						break;
					case 19:
						five = "인두";
						break;
					case 20:
						five = "신뢰";
						break;
					case 21:
						five = "조화";
						break;
					case 22:
						five = "래쓰";
						break;
					case 23:
						five = "죽음의숨결";
						break;
					case 24:
						five = "침묵";
						break;
					case 25:
						five = "명예";
						break;
					case 26:
						five = "야수";
						break;
					case 27:
						five = "초승달";
						break;
					case 28:
						five = "깊은고뇌";
						break;
					case 29:
						five = "계몽";
						break;
					case 30:
						five = "수수께끼";
						break;
					case 31:
						five = "명예의굴레";
						break;
					case 32:
						five = "고대의서약";
						break;
					case 33:
						five = "찬란한빛";
						break;
					case 34:
						five = "인내";
						break;
					}
				}

				if (five != null) {
					sb.append(five);
				}

				// --------------------1

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
			// 봉인 멘트
			if ((item.getBress() < 0))
				sb.append("[봉인]");

			String name = null;
			if (item.getAdd_Hpstell() > 0 && item.getAdd_Hpstell() <= 5) {
				name = "\\f1"; // 파란색
			}
			if (item.getAdd_Hpstell() > 5 && item.getAdd_Hpstell() <= 9) {
				name = "\\f2"; // 녹색
			}
			if (item.getAdd_Hpstell() > 9 && item.getAdd_Hpstell() <= 13) {
				name = "\\f3"; // 빨간색
			}
			if (item.getAdd_Hpstell() > 13 && item.getAdd_Hpstell() <= 17) {
				name = "\\f="; // 노란색
			}
			if (name != null) {
				sb.append(name);
			}

			// 2017 06 20 추가----------------1
			/*
			 * String name = null; if(item.getNowTime() == 1){ name = "\\f1"; //파란색 }
			 * if(item.getNowTime() == 2){ name = "\\f2"; //녹색 } if(item.getNowTime() == 3){
			 * name = "\\f3"; //빨간색 } if(item.getNowTime() == 4){ name = "\\f="; //노란색 }
			 * if(name != null){ sb.append(name); }
			 */
			// ------------------------------1

			// 앨리스
			if (item instanceof Alice)
				sb.append("(").append(((Alice) item).getLevel()).append(")");
			if (!item.getItem().getNameId().startsWith("$"))
				sb.append(item.getName());
			else
				sb.append(item.isDefinite() && item.getItem().getNameIdCheck().length() > 0
						? item.getItem().getNameIdCheck()
						: item.getItem().getNameId());
			
			// 턴번 아이템 시간 표현. 20180829
			if (item instanceof Turban || item instanceof 창천갑옷 || item instanceof 창천무기) {
				sb.append(" [");
				sb.append(item.getNowTime());
				sb.append("]");
			}

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
			if (item.isDefinite() && (item instanceof MapleWand || item instanceof PineWand || item instanceof EbonyWand
					|| item instanceof EbonyWand2)) {
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
			if (item instanceof CharacterSaveMarbles) {
				CharacterSaveMarbles csm = (CharacterSaveMarbles) item;
				if (csm.getAccountUid() != 0) {
					String class_type = "";
					switch (csm.getCharacterClassType()) {
					case 0:
						class_type = "군주";
						break;
					case 1:
						class_type = "기사";
						break;
					case 2:
						class_type = "요정";
						break;
					case 3:
						class_type = "법사";
						break;
					case 4:
						class_type = "다크엘프";
						break;
					}
					sb.append(class_type + " (" + csm.getCharacterName() + ")");
					sb.append("\r\n");
					sb.append("[Lv." + csm.getCharacterLevel() + " ");
					double exp = 0.0;
					Exp exp_bean = ExpDatabase.find(csm.getCharacterLevel());
					if (exp_bean != null) {
						double e = csm.getCharacterExp() - (exp_bean.getBonus() - exp_bean.getExp());
						exp = (e / exp_bean.getExp()) * 100;
					}
					sb.append(String.format("%.2f%%", exp) + "]");
				} else {
					if (!item.getItem().getNameId().startsWith("$")) {
						sb.append(item.getItem().getNameIdCheck());
					} else {
						sb.append(item.isDefinite() && item.getItem().getNameIdCheck().length() > 0
								? item.getItem().getNameIdCheck()
								: item.getItem().getNameIdCheck());
					}
				}
			}

			if (item.getInnRoomKey() > 0) {
				sb.append(" #");
				sb.append(item.getInnRoomKey());
			}
		}
		if (item.isTimeCheck()) {
			try {
				String[] lastDate = item.getTimestamp().toString().split(" ")[0].split("-");
				int lastMonth = Integer.valueOf(lastDate[1]);
				int lastDay = Integer.valueOf(lastDate[2]);
				String[] lastDate2 = item.getTimestamp().toString().split(" ")[1].split(":");
				int lastHour = Integer.valueOf(lastDate2[0]);
				int lastMin = Integer.valueOf(lastDate2[1]);
				String msg = "["+(lastMonth) +"-"+ (lastDay) +" "+ (lastHour) +":"+ (lastMin)+"]";
				sb.append(msg);
			} catch (Exception e) {
				lineage.share.System.println("삭제시간 표시에러");
			}
		}

		return sb.toString().trim();
	}

	protected int getOptionSize(ItemInstance item) {
		return getOptionSize(item.getItem(), item.getDurability(), item.getEnLevel(), null, item.getBress(), item.getDynamicMr(), item.getDynamicStunDefense(), item.getDynamicSp(), item.getDynamicReduction());
	}

	protected int getOptionSize(Item item, int durability, int enlevel, ItemSetoption setoption, int bless, int dynamic_mr, double dynamic_stun_defence, int dynamic_sp, int dynamic_reduction) {
		int size = 0;

		if (item.getType1().equalsIgnoreCase("armor")) {
			if (Lineage.server_version > 300)
				size += 10;
			else
				size += 9;
			
			if (enlevel != 0)
				size += 2;
			if (item.getAddStr() != 0)
				size += 2;
			if (item.getAddDex() != 0)
				size += 2;
			if (item.getAddCon() != 0)
				size += 2;
			if (item.getAddInt() != 0)
				size += 2;
			if (item.getAddCha() != 0)
				size += 2;
			if (item.getAddWis() != 0)
				size += 2;
			
			if (item.getName().equalsIgnoreCase("완력의 부츠") || item.getName().equalsIgnoreCase("민첩의 부츠") || item.getName().equalsIgnoreCase("지식의 부츠")) {
				if ((bless == 0 || bless == -128) || enlevel > 6)
					size += 3;
				if (enlevel == 9)
					size += 2;
				
				return size;
			}

			if (item.getType2().equalsIgnoreCase("necklace")) {
				if (item.getAddDmg() != 0 || (bless == 0 || bless == -128))
					size += 2;
				if (item.getAddHit() != 0 || (bless == 0 || bless == -128))
					size += 2;
				if (item.getAddHp() != 0 || enlevel > 0)
					size += 3;
				if (item.getAddMp() != 0)
					size += 2;			
				if (item.getAddMr() != 0 || dynamic_mr != 0)
					size += 3;
				if (item.getAddSp() != 0 || dynamic_sp != 0)
					size += 2;
				if (item.getStunDefense() != 0 || dynamic_stun_defence != 0 || enlevel > 6 )
					size += 2;
				if (item.getReduction() > 0 || dynamic_reduction != 0)
					size += 2;

				// 물약 회복량
				if (enlevel > 4)
					size += 2;

				return size;
			}

			if (item.getType2().equalsIgnoreCase("ring")) {
				if (item.getAddDmg() != 0 || (bless == 0 || bless == -128) || enlevel > 4)
					size += 2;
				if (item.getAddHit() != 0 || (bless == 0 || bless == -128))
					size += 2;
				if (item.getAddHp() != 0 || enlevel > 0)
					size += 3;								
				if (item.getAddMp() != 0)
					size += 2;			
				if ((item.getAddMr() != 0 || dynamic_mr != 0) || enlevel > 5)
					size += 3;
				if (item.getAddSp() != 0 || dynamic_sp != 0 || enlevel > 6)
					size += 2;
				if (item.getStunDefense() != 0 || dynamic_stun_defence != 0)
					size += 2;
				if (item.getReduction() > 0 || dynamic_reduction != 0)
					size += 2;

				// PvP 데미지
				if (enlevel > 6)
					size += 2;
				
				return size;
			}

			if (item.getType2().equalsIgnoreCase("belt")) {
				if (item.getAddDmg() != 0 || (bless == 0 || bless == -128))
					size += 2;
				if (item.getAddHit() != 0 || (bless == 0 || bless == -128))
					size += 2;
				if (item.getAddHp() != 0 || enlevel > 5)
					size += 3;
				if (item.getAddMp() != 0 || enlevel > 0)
					size += 2;
				if (item.getAddMr() != 0 || dynamic_mr != 0)
					size += 3;
				if (item.getAddSp() != 0 || dynamic_sp != 0)
					size += 2;	
				if (item.getStunDefense() != 0 || dynamic_stun_defence != 0)
					size += 2;
				if (item.getReduction() > 0 || dynamic_reduction != 0 || enlevel > 4)
					size += 2;

				// PvP 리덕션
				if (enlevel > 6)
					size += 2;

				return size;
			}
			
			if (item.getType2().equalsIgnoreCase("earring")) {
				if (item.getAddDmg() != 0 || (bless == 0 || bless == -128))
					size += 2;
				if (item.getAddHit() != 0 || (bless == 0 || bless == -128))
					size += 2;
				if (item.getAddHp() != 0)
					size += 3;
				if (item.getAddMp() != 0)
					size += 2;
				if (item.getAddMr() != 0)
					size += 3;
				if (item.getAddSp() != 0)
					size += 2;	
				if (item.getStunDefense() != 0)
					size += 2;
				if (item.getReduction() > 0)
					size += 2;

				return size;
			}
		} else if (item.getType1().equalsIgnoreCase("weapon")) {
			size += 10;
			if (enlevel != 0)
				size += 2;
			if (item.getAddStr() != 0)
				size += 2;
			if (item.getAddDex() != 0)
				size += 2;
			if (item.getAddCon() != 0)
				size += 2;
			if (item.getAddInt() != 0)
				size += 2;
			if (item.getAddCha() != 0)
				size += 2;
			if (item.getAddWis() != 0)
				size += 2;
			if (durability != 0)
				size += 2;
			if (item.isTohand())
				size += 1;
			if (item.getStealMp() != 0)
				size += 1;
			if (item.getStealHp() != 0)
				size += 1;
		} else {
			return 0;
		}
		
		if (item.getReduction() != 0 || dynamic_reduction != 0)
			size += 2;
		if (item.getStunDefense() != 0 || dynamic_stun_defence != 0)
			size += 2;
		if (item.getAddMp() != 0)
			size += 2;
		if (item.getAddMr() != 0 || dynamic_mr != 0)
			size += 3;
		if (item.getAddSp() != 0 || dynamic_sp != 0)
			size += 2;
		if (item.getAddHp() != 0)
			size += 3;
		if (item.getAddDmg() != 0)
			size += 2;
		if (item.getAddBowDmg() != 0)
			size += 2;
		if (item.getAddHit() != 0)
			size += 2;
		if (item.getAddBowHit() != 0)
			size += 2;
		if (setoption != null && (setoption.isBrave() || setoption.isHaste()))
			size += 1;
		
		return size;
	}

}
