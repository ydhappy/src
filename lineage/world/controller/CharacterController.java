package lineage.world.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lineage.bean.database.Exp;
import lineage.bean.database.Item;
import lineage.database.ExpDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_MessageGreen;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectTitle;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.Lineage_Balance;
import lineage.share.Log;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.PetInstance;
import lineage.world.object.instance.RobotInstance;
import lineage.world.object.instance.SummonInstance;
import lineage.world.object.item.cloak.ElvenCloak;
import lineage.world.object.magic.AbsoluteBarrier;
import lineage.world.object.magic.Meditation;

public final class CharacterController {

	static private List<object> list;

	static public void init() {
		TimeLine.start("CharacterController..");

		list = new ArrayList<object>();

		TimeLine.end();
	}

	static public void toWorldJoin(object o) {
		synchronized (list) {
			if (!list.contains(o))
				list.add(o);
		}
		// lineage.share.System.println("CharacterController toWorldJoin :
		// "+list.size()+" -> "+cha.toString());
	}

	static public void toWorldOut(object o) {
		synchronized (list) {
			list.remove(o);
		}

		// lineage.share.System.println("CharacterController toWorldOut :
		// "+list.size()+" -> "+cha.toString());
	}

	static private List<object> getList() {
		synchronized (list) {
			return new ArrayList<object>(list);
		}
	}

	/**
	 * 객체가 이동할대마다 호출됨.
	 * 
	 * @param cha
	 */
	static public void toMoving(Character cha) {
		// 관련 버프 제거.
		if (cha.isBuffMeditation())
			BuffController.remove(cha, Meditation.class);
		// 움직일때 앱솔제거.성환
		// if(cha.isBuffAbsoluteBarrier())
		// BuffController.remove(cha, AbsoluteBarrier.class);
	}

	/**
	 * 타이머에서 주기적으로 호출.
	 * 
	 * @param time
	 *            : 진행중인 현재 시간.
	 */
	static public void toTimer(long time) {
		// 처리할 객체 순회.
		for (object o : getList()) {

			// 자연회복 처리.
			try {
				if (!o.isDead()) {
					if (o instanceof Character) {
						Character cha = (Character) o;
						ItemInstance item = null;
						int tic_hp = cha.isHpTic() ? cha.hpTic() : 0;
						int tic_mp = cha.isMpTic() ? cha.mpTic() : 0;
						// 사용자일때 확인하기.
						if (cha instanceof PcInstance) {
							// 인벤토리 무게오바일때
							if (cha.getInventory() != null && cha.getInventory().isWeightPercent(50) == false) {
								// 여관맵이라면 피 차게해야됨.
								// 엑조틱 바이탈라이즈 시전중일때 차게 해야됨.
								// 여관맵이 아닐때.
								if (!InnController.isInnMap(cha) && !cha.isBuffExoticVitalize()
										&& !cha.isBuffAdditionalFire()) {
									tic_hp = tic_mp = 0;
									// 요정족 망토를 착용중이라면 피차게 해야됨.
									item = cha.getInventory().getSlot(4);
									if (item != null && item instanceof ElvenCloak)
										tic_hp = 1;
								}
							}
						}
						// 버서커상태 무시.
						if (o.isBuffBerserks())
							tic_hp = 0;
						// 틱 처리.
						if (tic_hp > 0 && cha.getTotalHp() != cha.getNowHp())
							cha.setNowHp(cha.getNowHp() + tic_hp);
						if (tic_mp > 0 && cha.getTotalMp() != cha.getNowMp())
							cha.setNowMp(cha.getNowMp() + tic_mp);
					}
				}
			} catch (Exception e) {
				lineage.share.System.println("자연회복 처리.");
				lineage.share.System.println(" : " + o.toString());
				lineage.share.System.println(e);
			}

			// 주기적으로 호출에 사용.
			try {
				o.toTimer(time);
			} catch (Exception e) {
				//lineage.share.System.println("주기적으로 호출에 사용.");
				//lineage.share.System.println(" : " + o.toString() + "  " + o.getName());
				//lineage.share.System.println(e);
			}
		}
	}

	/**
	 * 객체에 경험치 하향 처리 함수.
	 * 
	 * @param cha
	 * @param o
	 */
	static public void toExpDown(Character cha) {
		// 로봇은 무시.
		if (cha instanceof RobotInstance)
			return;
		
		if (cha.getInventory().find(ItemDatabase.find("불멸의 가호")) != null)
			return;
			
		// 공격자에게 경험치 주기. toAttackObject
		Exp e = ExpDatabase.find(cha.getLevel());
		double exp = 0;

		// 경험치 감소할 값 추출 부분
		Object o = PluginController.init(CharacterController.class, "toExpDown.exp", cha, e);
		if (o != null) {
			exp = (Double) o;
		} else {
			if (e.getLevel() < 45) {
				exp = e.getExp() * 0.05;
			} else if (e.getLevel() < 46) {
				exp = e.getExp() * 0.04;
			} else if (e.getLevel() < 47) {
				exp = e.getExp() * 0.03;
			} else if (e.getLevel() < 48) {
				exp = e.getExp() * 0.02;
			} else if (e.getLevel() < 49) {
				exp = e.getExp() * 0.01;
			} else {
				exp = e.getExp() * 0.01;
			}
		}

		// 레벨다운 경험치일경우 이전레벨 경험치로 재 연산.
		if (e.getBonus() - e.getExp() > cha.getExp() - exp) {
			e = ExpDatabase.find(cha.getLevel() - 1);
			if (e.getLevel() < 45) {
				exp = e.getExp() * 0.05;
			} else if (e.getLevel() < 46) {
				exp = e.getExp() * 0.04;
			} else if (e.getLevel() < 47) {
				exp = e.getExp() * 0.03;
			} else if (e.getLevel() < 48) {
				exp = e.getExp() * 0.02;
			} else if (e.getLevel() < 49) {
				exp = e.getExp() * 0.01;
			} else {
				exp = e.getExp() * 0.01;
			}
		}

		// 레벨다운됫는지 확인부분.
		e = ExpDatabase.find(cha.getLevel());
		if (e.getBonus() - e.getExp() > cha.getExp() - exp
				&& PluginController.init(CharacterController.class, "toExpDownSetting", cha) == null) {
			// 레벨 하향
			cha.setLevel(cha.getLevel() - 1);
			// hp & mp 하향.
			int hp = cha.getMaxHp() - toStatusUP(cha, true);
			int mp = cha.getMaxMp() - toStatusUP(cha, false);
			cha.setMaxHp(hp);
			cha.setMaxMp(mp);
		}
		// 경험치 하향.
		cha.setExp(cha.getExp() - exp);
		if (cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha;
			// 경험치 기록.
			pc.setLostExp(exp);
			// 패킷 처리.
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
	}

	/**
	 * 해당 객체에 아이템을 드랍처리할때 사용하는 함수. : 현재는 사용자가 호출해서 사용함. : 추후에는 팻도 이구간을 이용할 수 있도록
	 * 작업해두면 좋을듯.
	 * 
	 * @param cha
	 */
	static public void toItemDrop(Character cha) {
		if (PluginController.init(CharacterController.class, "toItemDrop", cha) != null)
			return;

		int dropCount = 0;
		int dropChance = 0;
		List<ItemInstance> list_itemdrop = new ArrayList<ItemInstance>();
		
		int lawful = cha.getLawful() - Lineage.NEUTRAL;
		
		if (lawful < -29999) {
			// -30000 ~
			dropCount = Util.random(1, 2);
			dropChance = 60;
		} else if (lawful < -19999 && lawful > -30000) {
			// -20000 ~ -29999
			dropCount = Util.random(1, 2);
			dropChance = 50;
		} else if (lawful < 0 && lawful > -20000) {
			// -1 ~ -19999
			dropCount = Util.random(1, 2);
			dropChance = 40;
		} else if (lawful > 0 && lawful < 20000) {
			// 1 ~ 19999
			dropCount = Util.random(0, 2);
			dropChance = 25;
		} else if (lawful > 19999 && lawful < 30000) {
			// 20000 ~ 29999
			dropCount = Util.random(0, 1);
			dropChance = 20;
		} else if (lawful > 29999) {
			// 30000~
			dropCount = Util.random(0, 1);
			dropChance = 5;
			// 풀라우풀보다 낮을경우 드랍적용
			if (lawful == 32767) {
				dropCount = Util.random(0, 1);
				dropChance = 1;
			}
		}

/*		if (cha.getLawful() >= Lineage.NEUTRAL && cha.getLawful() < Lineage.LAWFUL - 2767) {
			dropCount = Util.random(1, 1);
			dropChance = 10;
		} else if (cha.getLawful() < Lineage.NEUTRAL) {
			dropCount = Util.random(0, 3);
			dropChance = 50;
		} else if (cha.getLawful() < Lineage.CHAOTIC) {
			dropCount = Util.random(1, 6);
			dropChance = 100;
		} else {
			dropCount = 0;
			dropChance = 0;
		}

		List<ItemInstance> list = cha.getInventory().getList();
		Collections.shuffle(list); */
		//for (ItemInstance item : list) {
		for (ItemInstance item : cha.getInventory().getList()) {
			if (item.getItem().isDrop() && item.getItem().getNameIdNumber() != 4 && dropChance > Util.random(1, 100)) {
				// 로봇일경우 착용중인거 드랍 안함.
				if (cha instanceof RobotInstance && item.isEquipped())
					continue;
				// 그외엔
				list_itemdrop.add(item);
				if (--dropCount <= 0)
					break;
			}
		}
		for (ItemInstance item : list_itemdrop) {
			//
			long count = item.getCount();
			//
			if (item.isEquipped())
				item.toClick(cha, null);
			// 봉인된 아이템은 삭제처리
			if (item.getBress() < 0) {
				cha.toSender(new S_Message(638, count == 1 ? item.toStringDB() : String.format("%s (%d)", item.getName(), count)));
				cha.getInventory().count(item, 0, true);
			}
			// 그외엔 드랍.
			else {
				cha.toSender(new S_Message(1646, count == 1 ? item.toStringDB() : String.format("%s (%d)", item.getName(), count)));
				cha.getInventory().toDrop(item, item.getCount(), cha.getX(), cha.getY(), false);
			}
		}
	}

	static public int toStatusUP(Character cha, boolean HpMp) {
		// return toStatusUP(cha,
		// cha.getCon()+cha.getLvCon()+cha.getElixirCon(),
		// cha.getWis()+cha.getLvWis()+cha.getElixirWis(), HpMp);
		return toStatusUP(cha, cha.getCon(), cha.getWis(), HpMp);
	}

	/**
	 * 레벨업시 호출하며, hp&mp상승값 리턴함.
	 * 
	 * @param HpMp
	 *            : hp-true mp-false
	 * @return : 상승값.
	 */
	static public int toStatusUP(Character cha, int con, int wis, boolean HpMp) {
		Object o = PluginController.init(CharacterController.class, "toStatusUP", cha, HpMp);
		if (o != null)
			return (Integer) o;

		int start_hp = 0;
		int start_mp = 0;
		int temp = 0;
		int HPMP = 0;

		if (HpMp) { // hp
			if (cha instanceof PcInstance) {
				switch (cha.getClassType()) {
				case Lineage.LINEAGE_CLASS_ROYAL: // 군주
				case Lineage.LINEAGE_CLASS_ELF: // 요정
					// 임시로 랜덤 1~20를 추출한다.
					temp = Util.random(1, 20);

					if (con < 11)
						start_hp = 3;
					else if (con <= 13)
						start_hp = 5;
					else if (con <= 15)
						start_hp = 6;
					else if (con <= 17)
						start_hp = 7;
					else if (con <= 18)
						start_hp = 8;
					else if (con <= 20)
						start_hp = 9;
					else if (con <= 22)
						start_hp = 10;
					else if (con <= 24)
						start_hp = 11;
					else if (con <= 25)
						start_hp = 12;
					else
						start_hp = 13;

					if (temp <= 5) {
						start_hp += 1;
					} else if (temp <= 15) {
						start_hp += 2;
					} else if (temp <= 19) {
						start_hp += 3;
					} else {
						start_hp += 4;
					}
					HPMP = start_hp;
					break;
				case Lineage.LINEAGE_CLASS_KNIGHT:
					temp = Util.random(1, 64);
					if (con <= 15) {
						start_hp = 6;
					} else {
						start_hp = con - 9;
					}
					if (temp <= 7) {
						start_hp += 1;
					} else if (temp <= 22) {
						start_hp += 2;
					} else if (temp <= 42) {
						start_hp += 4;
					} else if (temp <= 57) {
						start_hp += 6;
					} else if (temp <= 63) {
						start_hp += 8;
					} else {
						start_hp += 10;
					}
					HPMP = start_hp;
					break;
				case Lineage.LINEAGE_CLASS_WIZARD: // 법사
					temp = Util.random(1, 8);
					if (con <= 15) {
						start_hp = 3;
					} else {
						start_hp = con - 12;
					}
					if (temp <= 4) {
						start_hp += 1;
					} else if (temp <= 7) {
						start_hp += 2;
					} else {
						start_hp += 3;
					}
					HPMP = start_hp;
					break;
				}
			} else if (cha instanceof SummonInstance) {
				switch (cha.getGfx()) {
				case 3132: // 하이도베
				case 931: // 도베르만
					HPMP = Util.random(6, 7);
					break;
				case 96: // 늑대
					HPMP = Util.random(4, 7);
					break;
				case 936: // 세퍼드
					HPMP = Util.random(5, 7);
					break;
				case 3184: // 하이세퍼드
					HPMP = Util.random(8, 10);
					break;
				case 934: // 콜리
				case 3154: // 하이래빗
					HPMP = Util.random(8, 9);
					break;
				case 3211: // 하이콜리
				case 1642: // 곰
					HPMP = Util.random(10, 13);
					break;
				case 3199: // 하이울프
				case 3143: // 하이비글
				case 4542:
				case 5089: // 배틀 타이거
					HPMP = Util.random(7, 9);
					break;
				case 3188: // 하이베어
					HPMP = Util.random(14, 16);
					break;
				case 2145: // 허스키
					HPMP = Util.random(10, 12);
					break;
				case 3107: // 하이허스키
					HPMP = Util.random(12, 14);
					break;
				case 2734: // 열혈토끼
				case 3783:
					HPMP = Util.random(3, 4);
					break;
				case 929: // 세인트 버나드
					HPMP = Util.random(7, 8);
					break;
				case 3182: // 하이세인트
					HPMP = Util.random(10, 11);
					break;
				case 1540: // 여우
				case 3134: // 고양이
					HPMP = Util.random(5, 6);
					break;
				case 3156: // 하이폭스
				case 3178: // 하이캣
					HPMP = Util.random(6, 7);
					break;
				case 938: // 비글
					HPMP = Util.random(4, 6);
					break;
				case 4038: // 라쿤
				case 4133:// 하이라쿤
					HPMP = Util.random(4, 5);
					break;
				case 5065: // 아기 진돗개
					HPMP = Util.random(11, 15);
					break;
				case 4582: // 진돗개
					HPMP = Util.random(12, 18);
					break;
				case 6325: // 아기캥거루
				case 6310: // 아기 팬더
					HPMP = Util.random(8, 12);
					break;
				case 6322: // 불꽃의 캥거루
				case 6314: // 공포의 판다곰
				case 1310: // 호랑이
					HPMP = Util.random(10, 14);
					break;
				default:
					HPMP = Util.random(3, 7);
					break;
				}

			}
			// else if(cha instanceof SummonInstance){
			// temp = Util.random(1, 32);
			// start_hp = 5;
			// if(temp <= 6) {
			// start_hp += 1;
			// } else if(temp <= 16) {
			// start_hp += 2;
			// } else if(temp <= 26) {
			// start_hp += 3;
			// } else if(temp <= 31) {
			// start_hp += 4;
			// } else {
			// start_hp += 5;
			// }
			//
			// HPMP = start_hp;
			// }
		} else { // mp
			if (cha instanceof PcInstance) {
				switch (cha.getClassType()) {
				case Lineage.LINEAGE_CLASS_ROYAL: // 군주
					if (wis <= 11) {
						start_mp = Util.random(1, 2);
					} else if (wis >= 12 && wis <= 14) {
						temp = Util.random(1, 4);
						if (temp == 1) {
							start_mp = 2;
						} else if (temp <= 3) {
							start_mp = 3;
						} else {
							start_mp = 4;
						}
					} else if (wis >= 15 && wis <= 17) {
						temp = Util.random(1, 4);
						if (temp == 1) {
							start_mp = 3;
						} else if (temp <= 3) {
							start_mp = 4;
						} else {
							start_mp = 5;
						}
					} else if (wis >= 18 && wis <= 20) {
						temp = Util.random(1, 6);
						if (temp == 1) {
							start_mp = 3;
						} else if (temp <= 3) {
							start_mp = 4;
						} else if (temp <= 5) {
							start_mp = 5;
						} else {
							start_mp = 6;
						}
					} else if (wis >= 21 && wis <= 23) {
						temp = Util.random(1, 10);
						if (temp == 1) {
							start_mp = 3;
						} else if (temp <= 3) {
							start_mp = 4;
						} else if (temp <= 7) {
							start_mp = 5;
						} else if (temp <= 9) {
							start_mp = 6;
						} else {
							start_mp = 7;
						}
					} else if (wis >= 24 && wis <= 26) {
						temp = Util.random(1, 14);
						if (temp == 1) {
							start_mp = 3;
						} else if (temp <= 3) {
							start_mp = 4;
						} else if (temp <= 7) {
							start_mp = 5;
						} else if (temp <= 11) {
							start_mp = 6;
						} else if (temp <= 13) {
							start_mp = 7;
						} else {
							start_mp = 8;
						}
					} else {// if(wis >= 27 && wis <= 29){
						temp = Util.random(1, 22);
						if (temp == 1) {
							start_mp = 3;
						} else if (temp <= 3) {
							start_mp = 4;
						} else if (temp <= 7) {
							start_mp = 5;
						} else if (temp <= 15) {
							start_mp = 6;
						} else if (temp <= 19) {
							start_mp = 7;
						} else if (temp <= 21) {
							start_mp = 8;
						} else {
							start_mp = 9;
						}
					}

					HPMP = start_mp;
					break;
				case Lineage.LINEAGE_CLASS_KNIGHT: // 기사
					if (wis <= 9) {
						temp = Util.random(1, 4);
						if (temp == 1) {
							start_mp = 0;
						} else if (temp <= 3) {
							start_mp = 1;
						} else {
							start_mp = 2;
						}
					} else {
						temp = Util.random(1, 4);
						if (temp == 1) {
							start_mp = 1;
						} else if (temp <= 3) {
							start_mp = 2;
						} else {
							start_mp = 3;
						}
					}

					HPMP = start_mp;
					break;
				case Lineage.LINEAGE_CLASS_ELF: // 요정
					if (wis <= 14) {
						temp = Util.random(1, 6);
						if (temp == 1) {
							start_mp = 3;
						} else if (temp <= 3) {
							start_mp = 4;
						} else if (temp <= 5) {
							start_mp = 5;
						} else {
							start_mp = 6;
						}
					} else if (wis >= 15 && wis <= 17) {
						temp = Util.random(1, 6);
						if (temp == 1) {
							start_mp = 4;
						} else if (temp <= 3) {
							start_mp = 5;
						} else if (temp <= 5) {
							start_mp = 6;
						} else {
							start_mp = 7;
						}
					} else if (wis >= 18 && wis <= 20) {
						temp = Util.random(1, 14);
						if (temp == 1) {
							start_mp = 4;
						} else if (temp <= 3) {
							start_mp = 5;
						} else if (temp <= 7) {
							start_mp = 6;
						} else if (temp <= 11) {
							start_mp = 7;
						} else if (temp <= 13) {
							start_mp = 8;
						} else {
							start_mp = 9;
						}
					} else if (wis >= 21 && wis <= 23) {
						temp = Util.random(1, 30);
						if (temp == 1) {
							start_mp = 4;
						} else if (temp <= 3) {
							start_mp = 5;
						} else if (temp <= 7) {
							start_mp = 6;
						} else if (temp <= 15) {
							start_mp = 7;
						} else if (temp <= 23) {
							start_mp = 8;
						} else if (temp <= 27) {
							start_mp = 9;
						} else if (temp <= 29) {
							start_mp = 10;
						} else {
							start_mp = 11;
						}
					} else if (wis >= 24 && wis <= 26) {
						temp = Util.random(1, 62);
						if (temp == 1) {
							start_mp = 4;
						} else if (temp <= 3) {
							start_mp = 5;
						} else if (temp <= 7) {
							start_mp = 6;
						} else if (temp <= 15) {
							start_mp = 7;
						} else if (temp <= 31) {
							start_mp = 8;
						} else if (temp <= 47) {
							start_mp = 9;
						} else if (temp <= 55) {
							start_mp = 10;
						} else if (temp <= 59) {
							start_mp = 11;
						} else if (temp <= 61) {
							start_mp = 12;
						} else {
							start_mp = 13;
						}
					} else { // else if(wis >= 27 && wis <= 29)
						temp = Util.random(1, 126);
						if (temp == 1) {
							start_mp = 4;
						} else if (temp <= 3) {
							start_mp = 5;
						} else if (temp <= 7) {
							start_mp = 6;
						} else if (temp <= 15) {
							start_mp = 7;
						} else if (temp <= 31) {
							start_mp = 8;
						} else if (temp <= 63) {
							start_mp = 9;
						} else if (temp <= 95) {
							start_mp = 10;
						} else if (temp <= 111) {
							start_mp = 11;
						} else if (temp <= 119) {
							start_mp = 12;
						} else if (temp <= 123) {
							start_mp = 13;
						} else if (temp <= 125) {
							start_mp = 14;
						} else {
							start_mp = 15;
						}
					}

					HPMP = start_mp;
					break;
				case Lineage.LINEAGE_CLASS_WIZARD: // 법사

					if (wis <= 14) {
						temp = Util.random(1, 10);
						if (temp == 1) {
							start_mp = 2;
						} else if (temp <= 3) {
							start_mp = 3;
						} else if (temp <= 7) {
							start_mp = 4;
						} else if (temp <= 9) {
							start_mp = 5;
						} else {
							start_mp = 6;
						}
					} else if (wis >= 15 && wis <= 17) {
						temp = Util.random(1, 10);
						if (temp == 1) {
							start_mp = 7;
						} else if (temp <= 3) {
							start_mp = 8;
						} else if (temp <= 7) {
							start_mp = 9;
						} else if (temp <= 9) {
							start_mp = 10;
						} else {
							start_mp = 11;
						}
					} else if (wis >= 18 && wis <= 20) {
						temp = Util.random(1, 22);
						if (temp == 1) {
							start_mp = 8;
						} else if (temp <= 3) {
							start_mp = 9;
						} else if (temp <= 7) {
							start_mp = 10;
						} else if (temp <= 15) {
							start_mp = 11;
						} else if (temp <= 19) {
							start_mp = 12;
						} else if (temp <= 21) {
							start_mp = 13;
						} else {
							start_mp = 14;
						}
					} else if (wis >= 21 && wis <= 23) {
						temp = Util.random(1, 46);
						if (temp == 1) {
							start_mp = 8;
						} else if (temp <= 3) {
							start_mp = 9;
						} else if (temp <= 7) {
							start_mp = 10;
						} else if (temp <= 15) {
							start_mp = 11;
						} else if (temp <= 31) {
							start_mp = 12;
						} else if (temp <= 39) {
							start_mp = 13;
						} else if (temp <= 43) {
							start_mp = 14;
						} else if (temp <= 45) {
							start_mp = 15;
						} else {
							start_mp = 16;
						}
					} else if (wis >= 24 && wis <= 26) {
						temp = Util.random(1, 94);
						if (temp == 1) {
							start_mp = 9;
						} else if (temp <= 3) {
							start_mp = 10;
						} else if (temp <= 7) {
							start_mp = 11;
						} else if (temp <= 15) {
							start_mp = 12;
						} else if (temp <= 31) {
							start_mp = 13;
						} else if (temp <= 63) {
							start_mp = 14;
						} else if (temp <= 79) {
							start_mp = 15;
						} else if (temp <= 87) {
							start_mp = 16;
						} else if (temp <= 91) {
							start_mp = 17;
						} else if (temp <= 93) {
							start_mp = 18;
						} else {
							start_mp = 19;
						}
					} else { // else if(wis >= 27 && wis <= 29)
						temp = Util.random(1, 190);
						if (temp == 1) {
							start_mp = 10;
						} else if (temp <= 3) {
							start_mp = 11;
						} else if (temp <= 7) {
							start_mp = 12;
						} else if (temp <= 15) {
							start_mp = 13;
						} else if (temp <= 31) {
							start_mp = 14;
						} else if (temp <= 63) {
							start_mp = 15;
						} else if (temp <= 127) {
							start_mp = 16;
						} else if (temp <= 159) {
							start_mp = 17;
						} else if (temp <= 175) {
							start_mp = 18;
						} else if (temp <= 183) {
							start_mp = 19;
						} else if (temp <= 187) {
							start_mp = 20;
						} else if (temp <= 189) {
							start_mp = 21;
						} else {
							start_mp = 22;
						}
					}

					HPMP = start_mp;
					break;
				}
			} else if (cha instanceof SummonInstance) {
				switch (cha.getGfx()) {
				case 931: // 도베르만
				case 3132: // 하이도베
				case 936:
				case 3184:
				case 934: // 콜리
				case 3211: // 하이콜리
				case 96: // 늑대
				case 3199: // 하이울프
				case 1642: // 곰
				case 3188: // 하이베어
				case 2145: // 허스키
				case 3107: // 하이허스키
					HPMP = Util.random(1, 2);
					break;
				case 2734: // 열혈토끼
				case 3783:
					HPMP = Util.random(8, 9);
					break;
				case 3154: // 하이래빗
					HPMP = Util.random(5, 6);
					break;
				case 929: // 세인트 버나드
				case 3182: // 하이세인트
				case 3156: // 하이폭스
				case 3178: // 하이캣
					HPMP = Util.random(4, 5);
					break;
				case 1540: // 여우
				case 3134: // 고양이
					HPMP = Util.random(2, 3);
					break;
				case 938: // 비글
				case 3143: // 하이비글
				case 4038: // 라쿤
				case 4133:// 하이라쿤
					HPMP = Util.random(3, 4);
					break;
				case 5065: // 아기 진돗개
					HPMP = Util.random(3, 5);
					break;
				case 4582: // 진돗개
					HPMP = Util.random(3, 6);
					break;
				case 4542:
					HPMP = Util.random(2, 3);
					break;
				case 5089: // 배틀 타이거
					HPMP = Util.random(2, 4);
					break;
				case 6325: // 아기캥거루
				case 6310: // 아기 팬더
					HPMP = Util.random(3, 5);
					break;
				case 1310: // 호랑이
				case 6314: // 공포의 판다곰
				case 6322: // 불꽃의 캥거루
					HPMP = Util.random(4, 6);
					break;
				default:
					HPMP = Util.random(3, 7);
					break;

				}
			}
		}

		if (cha instanceof PcInstance) {
			switch (cha.getClassType()) {
			case Lineage.LINEAGE_CLASS_ROYAL:
				if (HpMp)
					HPMP = (int) (HPMP * Lineage_Balance.level_up_hp_royal);
				else
					HPMP = (int) (HPMP * Lineage_Balance.level_up_mp_royal);
				break;
			case Lineage.LINEAGE_CLASS_KNIGHT:
				if (HpMp)
					HPMP = (int) (HPMP * Lineage_Balance.level_up_hp_knight);
				else
					HPMP = (int) (HPMP * Lineage_Balance.level_up_mp_knight);
				break;
			case Lineage.LINEAGE_CLASS_ELF:
				if (HpMp)
					HPMP = (int) (HPMP * Lineage_Balance.level_up_hp_elf);
				else
					HPMP = (int) (HPMP * Lineage_Balance.level_up_mp_elf);
				break;
			case Lineage.LINEAGE_CLASS_WIZARD:
				if (HpMp)
					HPMP = (int) (HPMP * Lineage_Balance.level_up_hp_wizard);
				else
					HPMP = (int) (HPMP * Lineage_Balance.level_up_mp_wizard);
				break;
			}
		}
		return HPMP;
		// }
	}

	static public int toStatStr(Character cha, String type) {
		int str = cha.getTotalStr();
		// 근거리 대미지
		int dmg = cha.getDynamicAddDmg() + cha.getLevel() / 10;
		// 근거리 명중
		int hit = cha.getDynamicAddHit();
		// 근거리 치명타

		// 최대 소지 무게
		int maxWeight = 0;

		if (str < 9) {
			dmg += 2;
			hit += 5;
		} else if (str < 10) {
			dmg += 2;
			hit += 6;
		} else if (str < 11) {
			dmg += 3;
			hit += 6;
			maxWeight += 100;
		} else if (str < 12) {
			dmg += 3;
			hit += 7;
			maxWeight += 100;
		} else if (str < 14) {
			dmg += 4;
			hit += 8;
			maxWeight += 200;
		} else if (str < 15) {
			dmg += 5;
			hit += 9;
			maxWeight += 300;
		} else if (str < 16) {
			dmg += 5;
			hit += 10;
			maxWeight += 300;
		} else if (str < 17) {
			dmg += 6;
			hit += 10;
			maxWeight += 400;
		} else if (str < 18) {
			dmg += 6;
			hit += 11;
			maxWeight += 400;
		} else if (str < 20) {
			dmg += 7;
			hit += 12;
			maxWeight += 500;
		} else if (str < 21) {
			dmg += 8;
			hit += 13;
			maxWeight += 600;
		} else if (str < 22) {
			dmg += 8;
			hit += 14;
			maxWeight += 600;
		} else if (str < 23) {
			dmg += 9;
			hit += 14;
			maxWeight += 700;
		} else if (str < 24) {
			dmg += 9;
			hit += 15;
			maxWeight += 700;
		} else if (str < 25) {
			dmg += 10;
			hit += 16;
			maxWeight += 800;
		} else if (str < 26) {
			dmg += 11;
			hit += 17;
			maxWeight += 800;
		} else if (str < 27) {
			dmg += 12;
			hit += 18;
			maxWeight += 900;
		} else if (str < 28) {
			dmg += 12;
			hit += 19;
			maxWeight += 900;
		} else if (str < 29) {
			dmg += 13;
			hit += 19;
			maxWeight += 1000;
		} else if (str < 30) {
			dmg += 13;
			hit += 20;
			maxWeight += 1000;
		} else if (str < 32) {
			dmg += 14;
			hit += 21;
			maxWeight += 1100;
		} else if (str < 33) {
			dmg += 15;
			hit += 22;
			maxWeight += 1200;
		} else if (str < 34) {
			dmg += 15;
			hit += 23;
			maxWeight += 1200;
		} else if (str < 35) {
			dmg += 16;
			hit += 23;
			maxWeight += 1300;
		} else if (str < 36) {
			dmg += 17;
			hit += 25;
			maxWeight += 1300;
		} else if (str < 38) {
			dmg += 18;
			hit += 26;
			maxWeight += 1400;
		} else if (str < 39) {
			dmg += 19;
			hit += 27;
			maxWeight += 1500;
		} else if (str < 40) {
			dmg += 19;
			hit += 28;
			maxWeight += 1500;
		} else if (str < 41) {
			dmg += 20;
			hit += 28;

			maxWeight += 1600;
		} else if (str < 42) {
			dmg += 20;
			hit += 29;

			maxWeight += 1600;
		} else if (str < 44) {
			dmg += 21;
			hit += 30;

			maxWeight += 1700;
		} else if (str < 45) {
			dmg += 22;
			hit += 31;

			maxWeight += 1800;
		} else if (str < 46) {
			dmg += 25;
			hit += 35;

			maxWeight += 1800;
		} else if (str < 47) {
			dmg += 26;
			hit += 36;

			maxWeight += 1900;
		} else if (str < 48) {
			dmg += 26;
			hit += 37;

			maxWeight += 1900;
		} else if (str < 49) {
			dmg += 27;
			hit += 37;

			maxWeight += 2000;
		} else if (str < 50) {
			dmg += 27;
			hit += 38;

			maxWeight += 2000;
		} else if (str < 51) {
			dmg += 28;
			hit += 38;

			maxWeight += 2100;
		} else if (str < 52) {
			dmg += 28;
			hit += 39;

			maxWeight += 2100;
		} else if (str < 53) {
			dmg += 29;
			hit += 39;

			maxWeight += 2200;
		} else if (str < 54) {
			dmg += 29;
			hit += 40;

			maxWeight += 2200;
		} else if (str < 55) {
			dmg += 30;
			hit += 40;

			maxWeight += 2300;
		} else if (str < 56) {
			dmg += 32;
			hit += 42;

			maxWeight += 2400;
		} else if (str < 57) {
			dmg += 33;
			hit += 43;

			maxWeight += 2500;
		} else if (str < 58) {
			dmg += 33;
			hit += 43;

			maxWeight += 2500;
		} else if (str < 59) {
			dmg += 34;
			hit += 44;

			maxWeight += 2600;
		} else if (str < 60) {
			dmg += 34;
			hit += 45;

			maxWeight += 2600;
		} else if (str < 61) {
			dmg += 35;
			hit += 45;

			maxWeight += 2700;
		} else if (str < 62) {
			dmg += 35;
			hit += 46;

			maxWeight += 2700;
		} else if (str < 63) {
			dmg += 36;
			hit += 45;

			maxWeight += 2800;
		} else if (str < 64) {
			dmg += 36;
			hit += 46;

			maxWeight += 2800;
		} else if (str < 65) {
			dmg += 37;
			hit += 46;

			maxWeight += 2900;
		} else {
			dmg += 38;
			hit += 47;

			maxWeight += 3000;
		}

		if (type.equalsIgnoreCase("DmgFigure")) {
			switch (cha.getClassType()) {
			case Lineage.LINEAGE_CLASS_ROYAL:
				return (int) Math.round(dmg * Lineage_Balance.royal_damage_figure);
			case Lineage.LINEAGE_CLASS_KNIGHT:
				return (int) Math.round(dmg * Lineage_Balance.knight_damage_figure);
			case Lineage.LINEAGE_CLASS_ELF:
				return (int) Math.round(dmg * Lineage_Balance.elf_damage_figure);
			case Lineage.LINEAGE_CLASS_WIZARD:
				return (int) Math.round(dmg * Lineage_Balance.wizard_damage_figure);
			default:
				return dmg;
			}
		}

		if (type.equalsIgnoreCase("isHitFigure")) {
			switch (cha.getClassType()) {
			case Lineage.LINEAGE_CLASS_ROYAL:
				return (int) Math.round(hit * Lineage_Balance.royal_hit_figure);
			case Lineage.LINEAGE_CLASS_KNIGHT:
				return (int) Math.round(hit * Lineage_Balance.knight_hit_figure);
			case Lineage.LINEAGE_CLASS_ELF:
				return (int) Math.round(hit * Lineage_Balance.elf_hit_figure);
			case Lineage.LINEAGE_CLASS_WIZARD:
				return (int) Math.round(hit * Lineage_Balance.wizard_hit_figure);
			default:
				return hit;
			}
		}

		if (type.equalsIgnoreCase("getMaxWeight")) {
			return cha.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT ? maxWeight + 200 : maxWeight;
		}

		return 0;

	}

	static public int toStatDex(Character cha, String type) {
		int dex = cha.getTotalDex();
		// 원거리 대미지
		int dmg = cha.getDynamicAddDmgBow() + cha.getLevel() / 10;
		// 원거리 명중
		int hit = cha.getDynamicAddHitBow();
		// 원거리 치명타

		if (dex < 8) {
			dmg += 2;
			hit += -3;
		} else if (dex < 9) {
			dmg += 2;
			hit += -2;
		} else if (dex < 10) {
			dmg += 3;
			hit += -1;
		} else if (dex < 11) {
			dmg += 3;
			hit += 0;
		} else if (dex < 12) {
			dmg += 3;
			hit += 1;
		} else if (dex < 13) {
			dmg += 4;
			hit += 2;
		} else if (dex < 14) {
			dmg += 4;
			hit += 3;
		} else if (dex < 15) {
			dmg += 4;
			hit += 4;
		} else if (dex < 16) {
			dmg += 5;
			hit += 5;
		} else if (dex < 17) {
			dmg += 5;
			hit += 6;
		} else if (dex < 18) {
			dmg += 5;
			hit += 7;
		} else if (dex < 19) {
			dmg += 6;
			hit += 8;
		} else if (dex < 20) {
			dmg += 6;
			hit += 9;
		} else if (dex < 21) {
			dmg += 6;
			hit += 10;
		} else if (dex < 22) {
			dmg += 7;
			hit += 11;
		} else if (dex < 23) {
			dmg += 7;
			hit += 12;
		} else if (dex < 24) {
			dmg += 7;
			hit += 13;
		} else if (dex < 25) {
			dmg += 8;
			hit += 14;
		} else if (dex < 26) {
			dmg += 9;
			hit += 16;
		} else if (dex < 27) {
			dmg += 9;
			hit += 17;
		} else if (dex < 28) {
			dmg += 10;
			hit += 18;
		} else if (dex < 29) {
			dmg += 10;
			hit += 19;
		} else if (dex < 30) {
			dmg += 10;
			hit += 20;
		} else if (dex < 31) {
			dmg += 11;
			hit += 21;
		} else if (dex < 32) {
			dmg += 11;
			hit += 22;
		} else if (dex < 33) {
			dmg += 11;
			hit += 23;
		} else if (dex < 34) {
			dmg += 12;
			hit += 24;
		} else if (dex < 35) {
			dmg += 12;
			hit += 25;
		} else if (dex < 36) {
			dmg += 13;
			hit += 27;
		} else if (dex < 37) {
			dmg += 14;
			hit += 28;
		} else if (dex < 38) {
			dmg += 14;
			hit += 29;
		} else if (dex < 39) {
			dmg += 14;
			hit += 30;
		} else if (dex < 40) {
			dmg += 15;
			hit += 31;
		} else if (dex < 41) {
			dmg += 15;
			hit += 32;

		} else if (dex < 42) {
			dmg += 15;
			hit += 33;

		} else if (dex < 43) {
			dmg += 16;
			hit += 34;

		} else if (dex < 44) {
			dmg += 16;
			hit += 35;

		} else if (dex < 45) {
			dmg += 16;
			hit += 36;

		} else if (dex < 46) {
			dmg += 20;
			hit += 40;

		} else if (dex < 47) {
			dmg += 21;
			hit += 41;

		} else if (dex < 48) {
			dmg += 21;
			hit += 42;

		} else if (dex < 49) {
			dmg += 22;
			hit += 43;

		} else if (dex < 50) {
			dmg += 22;
			hit += 43;

		} else if (dex < 51) {
			dmg += 23;
			hit += 44;

		} else if (dex < 52) {
			dmg += 23;
			hit += 44;

		} else if (dex < 53) {
			dmg += 24;
			hit += 45;

		} else if (dex < 54) {
			dmg += 24;
			hit += 45;

		} else if (dex < 55) {
			dmg += 25;
			hit += 46;

		} else if (dex < 56) {
			dmg += 27;
			hit += 48;

		} else if (dex < 57) {
			dmg += 28;
			hit += 49;

		} else if (dex < 58) {
			dmg += 28;
			hit += 50;

		} else if (dex < 59) {
			dmg += 29;
			hit += 50;

		} else if (dex < 60) {
			dmg += 29;
			hit += 51;

		} else if (dex < 61) {
			dmg += 30;
			hit += 51;

		} else if (dex < 62) {
			dmg += 30;
			hit += 52;

		} else if (dex < 63) {
			dmg += 31;
			hit += 52;

		} else if (dex < 64) {
			dmg += 31;
			hit += 53;

		} else if (dex < 65) {
			dmg += 32;
			hit += 53;

		} else {
			dmg += 33;
			hit += 54;

		}

		if (type.equalsIgnoreCase("DmgFigure")) {
			switch (cha.getClassType()) {
			case Lineage.LINEAGE_CLASS_ROYAL:
				return (int) Math.round(dmg * Lineage_Balance.royal_bow_damage_figure);
			case Lineage.LINEAGE_CLASS_KNIGHT:
				return (int) Math.round(dmg * Lineage_Balance.knight_bow_damage_figure);
			case Lineage.LINEAGE_CLASS_ELF:
				return (int) Math.round(dmg * Lineage_Balance.elf_bow_damage_figure);
			case Lineage.LINEAGE_CLASS_WIZARD:
				return (int) Math.round(dmg * Lineage_Balance.wizard_bow_damage_figure);
			default:
				return dmg;
			}
		}

		if (type.equalsIgnoreCase("isHitFigure")) {
			switch (cha.getClassType()) {
			case Lineage.LINEAGE_CLASS_ROYAL:
				return (int) Math.round(hit * Lineage_Balance.royal_bow_hit_figure);
			case Lineage.LINEAGE_CLASS_KNIGHT:
				return (int) Math.round(hit * Lineage_Balance.knight_bow_hit_figure);
			case Lineage.LINEAGE_CLASS_ELF:
				return (int) Math.round(hit * Lineage_Balance.elf_bow_hit_figure);
			case Lineage.LINEAGE_CLASS_WIZARD:
				return (int) Math.round(hit * Lineage_Balance.wizard_bow_hit_figure);
			default:
				return hit;
			}
		}

		return 0;
	}
}
